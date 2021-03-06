package com.example.organizzekotlin.model


import com.example.organizzekotlin.firebase.FirebaseHelper.firebaseAuth
import com.example.organizzekotlin.firebase.FirebaseHelper.firebaseConnection
import com.example.organizzekotlin.helper.Base64Custom
import com.example.organizzekotlin.helper.DateCustom
import com.example.organizzekotlin.util.toCurrency
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference

data class Movimentacao(
    var data: String = "",
    var categoria: String = "",
    var descricao: String = "",
    var tipo: String = "",
    var valor: Double = 0.00,
    var key: String = ""
) {




    fun salvar(dataEscolhida: String) {

        val autentication: FirebaseAuth = firebaseAuth()
        val idUsuario: String =
            Base64Custom.codificarBase64(autentication.currentUser?.email.toString())
        val mesAno: String = DateCustom.mesAnoDataEscolhida(dataEscolhida)

        val firebase: DatabaseReference = firebaseConnection()
        firebase.child("movimentacao")
            .child(idUsuario)
            .child(mesAno)
            .push()
            .setValue(this)


    }

    fun valorToString(): String {
        return valor.toCurrency()
    }


}







