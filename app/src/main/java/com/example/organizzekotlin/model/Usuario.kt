package com.example.organizzekotlin.model

import com.example.organizzekotlin.firebase.FirebaseHelper.firebaseConnection

data class Usuario(
    var idUsuario: String? = "",
    var nome: String? = "",
    var email: String = "",
    var senha: String = ""
) {

    fun salvar() {

        firebaseConnection().child("usuarios").child(this.idUsuario.orEmpty()).setValue(this)

    }

}




