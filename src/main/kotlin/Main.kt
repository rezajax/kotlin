package ir.rezajax

import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

fun main() {

}


fun fetchMovies (stringUrl: String) : String {
    val client = HttpClient.newHttpClient()
    val request = HttpRequest.newBuilder(URI(urlCreator(550))).build()

    val response = client.send(request, HttpResponse.BodyHandlers.ofString())
    return response.body()
}



fun urlCreator (movieId: Int) : String {
    val api = buildString {
        append("https://api.themoviedb.org/3/movie/")
        append(movieId)
        append("?api_key=")
        append("b955b1053d8c0d7dca019181fa21c28e")
    }
    return api
}