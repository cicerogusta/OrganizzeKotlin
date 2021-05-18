package com.example.organizzekotlin.model

import android.content.Context
import com.example.organizzekotlin.firebase.FirebaseCustom

class Usuario{

    lateinit var idUsuario: String
    lateinit var nome: String
    lateinit var email: String
    lateinit var senha: String
    var receitaTotal: Double = 0.00
    var despesaTotal: Double = 0.00

    class Usuario(){
    }

    fun salvar(){

        FirebaseCustom.firebaseConnection().child("usuarios").child(this.idUsuario).setValue(this)

    }

}


//class AdapterMovimentacao(list: List<Movimentacao>, context: Context) {
//
//    var movimentacoes: List<Movimentacao> = list
//    var context: Context = context
//
//}

