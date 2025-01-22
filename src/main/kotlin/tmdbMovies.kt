package ir.rezajax

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class tmdbMovies (
    val title: String,
    val id: Int,
    @SerialName("overview") val Description: String,
)
