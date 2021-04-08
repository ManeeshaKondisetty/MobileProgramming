class Player(val name: String, var lives: Int =3, var level: Int =1, var score: Int =0) {

    fun show(){
        println("""
            name: $name
            lives: $lives
            level: $level
            score: $score
        """)
    }
}