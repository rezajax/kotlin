package ir.rezajax

import java.io.FileInputStream
import java.util.Properties

// use: val apiKey2 = Config.TMDB_API
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