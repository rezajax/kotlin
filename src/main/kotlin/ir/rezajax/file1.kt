//package ir.rezajax.tmp
//
//import com.google.gson.Gson
//import com.google.gson.annotations.SerializedName
//
//fun main() {
//    val json = """
//        {
//           "Name" : "reza",
//           "family" : "jax"
//        }
//    """
//
//
//    data class Person(
//        @SerializedName("name") val name: String,
//        val family: String
//    )
//
//
//    val gson = Gson()
//    val person = gson.fromJson(json, Person::class.java)
//    println(person)
//
//    // Simple manual parsing
//    val jsonString = json.trimIndent().removeSurrounding("{","}")
//    val keyValuePairs = jsonString.split(",").map { it.trim() }
//
//    val name = keyValuePairs[0].split(":")[1].trim().removeSurrounding("\"")
//    val family = keyValuePairs[1].split(":")[1].trim().removeSurrounding("\"")
//
//    println("Name: $name, Family: $family")
//}
//
//
//suspend fun fetchMovie() {
//    // Initialize Ktor client
////    val client = HttpClient(CIO) {
////        install(ContentNegotiation) {
////            gson()
////        }
////    }
//
////    try {
////        // Fetch the movie data
////        val apiKey = "b955b1053d8c0d7dca019181fa21c28e"
////        val movieId = 550
////        val url = "https://api.themoviedb.org/3/movie/$movieId?api_key=$apiKey"
////        val movie: Movie = client.get(url).body()
////
////        // Display the data
////        println("Title: ${movie.title}")
////        println("Overview: ${movie.overview}")
////        println("Genres: ${movie.genres.joinToString { it.name }}")
////        println("Release Date: ${movie.releaseDate}")
////        println("Runtime: ${movie.runtime} minutes")
////        println("Vote Average: ${movie.voteAverage}")
////        println("Production Companies: ${movie.productionCompanies.joinToString { it.name }}")
////        println("Production Countries: ${movie.productionCountries.joinToString { it.name }}")
////    } catch (e: Exception) {
////        println("Error fetching data: ${e.message}")
////    } finally {
////        // Close the client
////        client.close()
////    }
//}