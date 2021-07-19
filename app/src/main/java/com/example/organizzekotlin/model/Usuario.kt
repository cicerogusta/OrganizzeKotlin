package com.example.organizzekotlin.model

import com.example.organizzekotlin.firebase.FirebaseHelper.firebaseConnection

data class Usuario(
    var idUsuario: String? = "",
    var nome: String? = "",
    var email: String = "",
    var senha: String = "",
    var receitaTotal: Double = 0.00,
    var despesaTotal: Double = 0.00
) {

    fun salvar() {

        firebaseConnection().child("usuarios").child(this.idUsuario.toString()).setValue(this)

    }

}




