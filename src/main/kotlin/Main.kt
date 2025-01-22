package ir.rezajax


fun main() {
    val url = urlCreator(503)

    val movies = parseMovies(url)
    println(movies)

}
