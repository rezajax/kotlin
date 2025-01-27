package ir.rezajax

import ir.rezajax.module.testFunInModule


fun main() {
    val url = urlCreator(503)

    val movies = parseMovies(url)
    println(movies)

ir.rezajax.module.main()
    testFunInModule()
}
