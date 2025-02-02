package socket.create

import io.netty.bootstrap.Bootstrap
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.*
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.handler.codec.socksx.v5.*

fun main() {
    val bossGroup = NioEventLoopGroup(1)
    val workerGroup = NioEventLoopGroup()

    try {
        val bootstrap = ServerBootstrap()
            .group(bossGroup, workerGroup)
            .channel(NioServerSocketChannel::class.java)
            .childHandler(object : ChannelInitializer<SocketChannel>() {
                override fun initChannel(ch: SocketChannel) {
                    val pipeline = ch.pipeline()

                    // SOCKS5 handlers
                    pipeline.addLast(Socks5ServerEncoder.DEFAULT) // Encode responses
                    pipeline.addLast(Socks5InitialRequestDecoder()) // Decode initial handshake
                    pipeline.addLast(Socks5CommandRequestDecoder()) // Decode connection requests

                    // Custom handler to establish connections
                    pipeline.addLast(Socks5ProxyHandler())
                }
            })
            .option(ChannelOption.SO_BACKLOG, 128)
            .childOption(ChannelOption.SO_KEEPALIVE, true)

        val channelFuture = bootstrap.bind(1122).sync()
        println("SOCKS5 server started on port 1080")
        channelFuture.channel().closeFuture().sync()
    } finally {
        bossGroup.shutdownGracefully()
        workerGroup.shutdownGracefully()
    }
}

/**
 * Handles SOCKS5 requests and establishes connections.
 */
class Socks5ProxyHandler : SimpleChannelInboundHandler<Socks5Message>() {

    override fun channelRead0(ctx: ChannelHandlerContext, msg: Socks5Message) {
        when (msg) {
            is Socks5InitialRequest -> {
                println("Received Socks5InitialRequest")
                ctx.writeAndFlush(DefaultSocks5InitialResponse(Socks5AuthMethod.NO_AUTH))
            }

            is Socks5CommandRequest -> {
                println("Received Socks5CommandRequest: ${msg.type()} to ${msg.dstAddr()}:${msg.dstPort()}")

                if (msg.type() == Socks5CommandType.CONNECT) {
                    connectToTarget(ctx, msg)
                } else {
                    ctx.writeAndFlush(
                        DefaultSocks5CommandResponse(
                            Socks5CommandStatus.COMMAND_UNSUPPORTED,
                            msg.dstAddrType()
                        )
                    )
                    ctx.close()
                }
            }

            else -> {
                println("Unknown message type: $msg")
                ctx.close()
            }
        }
    }

    private fun connectToTarget(ctx: ChannelHandlerContext, msg: Socks5CommandRequest) {
        val targetHost = msg.dstAddr()
        val targetPort = msg.dstPort()

        val clientBootstrap = Bootstrap()
            .group(ctx.channel().eventLoop()) // Use the same thread group
            .channel(NioSocketChannel::class.java)
            .option(ChannelOption.SO_KEEPALIVE, true)
            .handler(object : ChannelInitializer<SocketChannel>() {
                override fun initChannel(ch: SocketChannel) {
                    ch.pipeline().addLast(TargetResponseHandler(ctx))
                }
            })

        clientBootstrap.connect(targetHost, targetPort).addListener { future ->
            if (future.isSuccess) {
                val outboundChannel = (future as ChannelFuture).channel()
                ctx.writeAndFlush(
                    DefaultSocks5CommandResponse(
                        Socks5CommandStatus.SUCCESS,
                        msg.dstAddrType(),
                        targetHost,
                        targetPort
                    )
                )
                ctx.pipeline().remove(this) // Remove handler once connection is established
                ctx.pipeline().addLast(RelayHandler(outboundChannel)) // Handle data relay
                outboundChannel.pipeline().addLast(RelayHandler(ctx.channel())) // Relay back
            } else {
                println("Failed to connect to $targetHost:$targetPort")
                ctx.writeAndFlush(
                    DefaultSocks5CommandResponse(
                        Socks5CommandStatus.FAILURE,
                        msg.dstAddrType()
                    )
                )
                ctx.close()
            }
        }
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        cause.printStackTrace()
        ctx.close()
    }
}

/**
 * Handles relaying data between the client and the destination server.
 */
class RelayHandler(private val targetChannel: Channel) : ChannelInboundHandlerAdapter() {
    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
        targetChannel.writeAndFlush(msg)
    }

    override fun channelInactive(ctx: ChannelHandlerContext) {
        targetChannel.close()
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        cause.printStackTrace()
        ctx.close()
        targetChannel.close()
    }
}

/**
 * Handles responses from the target server.
 */
class TargetResponseHandler(private val clientChannel: ChannelHandlerContext) : ChannelInboundHandlerAdapter() {
    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
        clientChannel.writeAndFlush(msg)
    }

    override fun channelInactive(ctx: ChannelHandlerContext) {
        clientChannel.close()
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        cause.printStackTrace()
        ctx.close()
        clientChannel.close()
    }
}
