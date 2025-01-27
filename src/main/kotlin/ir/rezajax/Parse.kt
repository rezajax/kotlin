package ir.rezajax

import kotlinx.serialization.json.Json

fun parseMovies (url: String): tmdbMovies {
    val json = Json { ignoreUnknownKeys = true }
    val movie: tmdbMovies = json.decodeFromString(fetchMovies(url))
    return movie
}
