package ar.edu.unicen.seminario.kotlin


class Persona {
    private var nombre: String
    private var apellido: String

    private var edad: Int = 0
        set(value){
            if (value >= 0 ){
                field = value
            }
        }

    constructor(nombre: String, apellido: String, edad: Int){
        this.nombre = nombre
        this.apellido = apellido
        this.edad = edad
    }




    fun getNombre(): String{
        return this.nombre
    }

    fun getApellido(): String{
        return this.apellido
    }

    fun fullName(): String{
        return "${getNombre()} ${getApellido()}"
    }
}