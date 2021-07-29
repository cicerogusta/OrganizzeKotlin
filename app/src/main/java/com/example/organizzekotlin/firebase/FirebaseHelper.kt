package com.example.organizzekotlin.firebase

import com.example.organizzekotlin.helper.Base64Custom
import com.example.organizzekotlin.model.Movimentacao
import com.example.organizzekotlin.model.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


object FirebaseHelper {

    fun firebaseConnection() = FirebaseDatabase.getInstance().reference

    fun firebaseAuth() = FirebaseAuth.getInstance()

    fun recuperarEmail() = firebaseAuth().currentUser?.email.toString()

    fun removerMovimentacao(path: String) {
        FirebaseDatabase.getInstance().reference.child(path).removeValue()


    }
    fun removerDespesaERecaitaUsuario(movimentacao: Movimentacao){
        val emailUsuario = recuperarEmail()
        val idUsuario = Base64Custom.codificarBase64(emailUsuario)
       val  usuarioRef = firebaseConnection().child("usuarios").child(idUsuario)
       val receitaTotal =- movimentacao.valor
        usuarioRef.child("receitaTotal").setValue(receitaTotal)
    }

}



