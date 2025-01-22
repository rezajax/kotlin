package ir.rezajax

import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

fun fetchMovies (stringUrl: String) : String {
    val client = HttpClient.newHttpClient()
    val request = HttpRequest.newBuilder(URI(urlCreator(550))).build()

    val response = client.send(request, HttpResponse.BodyHandlers.ofString())
    return response.body()
}