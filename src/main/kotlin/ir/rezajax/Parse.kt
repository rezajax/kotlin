package ir.rezajax

import kotlinx.serialization.json.Json

fun parseMovies(url: String): tmdbMovies {
    val json = Json { ignoreUnknownKeys = true }
    try {
        val movie: tmdbMovies = json.decodeFromString(fetchMovies(url))
        return movie
    } catch (e: Exception) {
        println("Error parsing JSON: ${e.message}")
        throw e
    }
}