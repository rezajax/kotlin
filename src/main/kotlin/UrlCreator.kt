package ir.rezajax


fun urlCreator (movieId: Int) : String {
    val api = buildString {
        append("https://api.themoviedb.org/3/movie/")
        append(movieId)
        append("?api_key=")
        append(getApiKey())
    }
    return api
}