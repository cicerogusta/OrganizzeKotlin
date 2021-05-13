package com.example.organizzekotlin.model

import com.example.organizzekotlin.config.ConfiguracaoFirebase
import com.google.firebase.database.DatabaseReference

class Usuario{

    lateinit var idUsuario: String
    lateinit var nome: String
    lateinit var email: String
    lateinit var senha: String
    var receitaTotal: Double = 0.0
    var despesaTotal: Double = 0.0

    class Usuario(){
    }

    fun salvar(){

        val firebase: DatabaseReference = ConfiguracaoFirebase.getFirebaseDatabase()
        firebase.child("usuarios")
            .child(this.idUsuario).setValue(this)

    }
}




