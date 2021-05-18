package com.example.organizzekotlin.model

import com.example.organizzekotlin.firebase.FirebaseCustom
import com.example.organizzekotlin.helper.Base64Custom
import com.example.organizzekotlin.helper.DateCustom
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference

class Movimentacao {

    lateinit var data: String
    lateinit var categoria: String
    lateinit var descricao: String
    lateinit var tipo: String
    var valor: Double = 0.0
    lateinit var key: String

    class Movimentacao() {

    }

    fun salvar(dataEscolhida: String) {
        val autentication: FirebaseAuth = FirebaseCustom.firebaseAuth()
        val idUsuario: String = Base64Custom.codificarBase64(autentication.currentUser?.email.toString())
        val mesAno: String = DateCustom.mesAnoDataEscolhida(dataEscolhida)

        val firebase: DatabaseReference = FirebaseCustom.firebaseConnection()
        firebase.child("movimentacao")
            .child(idUsuario)
            .child(mesAno)
            .push()
            .setValue(this)


    }
}


