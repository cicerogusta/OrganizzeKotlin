package com.example.organizzekotlin.config

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

/**
 * Created by jamiltondamasceno
 */
object ConfiguracaoFirebase {
    private  var  autenticacao: FirebaseAuth? = null
    private   var firebase: DatabaseReference? = null

    //retorna a instancia do FirebaseDatabase
    fun getFirebaseDatabase(): DatabaseReference {
        if (firebase == null){
            firebase = FirebaseDatabase.getInstance().reference
        }
        return firebase
    }

    //retorna a instancia do FirebaseAuth

    fun getFirebaseAuth(): FirebaseAuth{

        if (autenticacao == null) {
            autenticacao = FirebaseAuth.getInstance()


        }
        return autenticacao
    }
}