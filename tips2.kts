fun cowsay(message: String): String {
    return """
     _______________________
    < $message >
     -----------------------
            \   ^__^
             \  (oo)\_______
                (__)\       )\/\
                    ||----w |
                    ||     ||
    """
}

// Get system information
val osName = System.getProperty("os.name")
val userName = System.getProperty("user.name")
val javaVersion = System.getProperty("java.version")

// Combine the system info into a single string
val systemInfo = """
    OS: $osName
    User: $userName
    Java Version: $javaVersion
""".trimIndent()

// Generate the cowsay message
val message = cowsay(systemInfo)

// Print the cowsay message
println(message)
