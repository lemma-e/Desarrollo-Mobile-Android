package ar.edu.unicen.seminario.kotlin

const val PI = 3.1416
fun main(){

    println("Hello world!")

    //val no cambia valor
    // kt entiende por si solo el tipo de variable
    val nombre = "Ede"
    println("hello $nombre")

    //var si puede cambiar de valor
    //? -> permite null
    var nombre2: String? = "test"
    nombre2 = "si"
    println(nombre2)
    nombre2 = null;
    println(nombre2)

    //any(no tengo tipo), unit(void), nothing(nunca retorna un resultado)
    var test: Any = true
    test = "true"
    test = 1
    println(test)

    println("///////////")

    val l1 = ArrayList<Int>()
    l1.add(1)
    l1.add(2)
    l1.add(4)
    println(l1)

    val l2 = listOf(1,2,3,4,5)
    println(l2)

    val l3 = mutableListOf(6,7,8)
    l3.add(9)
    println(l3)

    val l4 = setOf(9,10,11)
    println(l4)

    val l5 = mutableSetOf(12)
    l5.add(13)
    println(l5)

    val l6 = mapOf(1 to "uno")
    println(l6)

    val l7 = mutableMapOf(2 to "dosh")
    l7[1] = "tres"
    l7.put(3,"nose")
    println(l7)

    println("/////////////")
    val doble = Pair("hola", 1)
    println(doble)
    println("////////////")
    //if else - when - dowhile - try/catch - for in
    val saludo = "saludo metodo"

    println(saludar(saludo))
    println("////////////")
    val res = sumar(a=1, b=2)
    println(res)
    val res2 = sumar(a=1, b="hello world!")
    println(res2)
    println("////////")
    val c = setOf(1,2,3,4,5)
    println(c.promedio())
}

fun saludar(saludo: String): String{
    return "$saludo contatenado"
}

fun sumar(a: Int, b:Any): Int{
    if(b is Int){
        return a + b
    }else {
        return a
    }
}

fun Set<Int>.promedio(): Double{
    var sum = 0
    for(e in this){
        sum += e
    }
    return  sum.toDouble() / this.size
}