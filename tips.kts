data class Cat(val name: String)

val cats = listOf(Cat("Cat"), Cat("Dog"), Cat("Cat"))

cats.forEach { cat ->
    val cowsay = """
     _______________________
    < ${cat.name}                >
     -----------------------
            \   ^__^
             \  (oo)\_______
                (__)\       )\/\
                    ||----w |
                    ||     ||
    """
    println(cowsay)
}
