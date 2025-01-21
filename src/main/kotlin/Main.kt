package ir.rezajax


import java.io.FileInputStream
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.util.Properties


fun main() {
    println("from local.props: ${getApiKey()}")


    val apiKey = System.getenv("TMDB_API") ?: throw IllegalStateException("API Key not set")
    println("from directJava: $apiKey")

    val url = urlCreator(503)
    val fetch = fetchMovies(url)
    println(fetch)

    val apiKey2 = Config.TMDB_API
    println("TMDB API Key: $apiKey2")

}

object Config {
    val TMDB_API: String = System.getenv("TMDB_API") ?: error("TMDB_API environment variable not set")
}



// Get From local.properties
fun getApiKey(): String {
    val props = Properties()
    val inputStream = FileInputStream("local.properties")
    props.load(inputStream)
    return props.getProperty("TMDB_API") ?: throw IllegalStateException("API Key not found")
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
        append(getApiKey())
    }
    return api
}