package com.example.organizzekotlin.model

import com.example.organizzekotlin.firebase.FirebaseHelper

data class Usuario(
    var idUsuario: String? = "",
    var nome: String = "",
    var email: String = "",
    var senha: String = "",
    var receitaTotal: Double = 0.00,
    var despesaTotal: Double = 0.00
){







    fun salvar(){

        FirebaseHelper.firebaseConnection().child("usuarios").child(this.idUsuario.toString()).setValue(this)

    }

}


//class AdapterMovimentacao(list: List<Movimentacao>, context: Context) {
//
//    var movimentacoes: List<Movimentacao> = list
//    var context: Context = context
//
//}

