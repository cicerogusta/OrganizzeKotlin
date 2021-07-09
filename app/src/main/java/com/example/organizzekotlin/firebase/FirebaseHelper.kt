package com.example.organizzekotlin.firebase

import com.example.organizzekotlin.helper.Base64Custom
import com.example.organizzekotlin.model.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


fun firebaseConnection(): DatabaseReference = FirebaseDatabase.getInstance().reference;


fun firebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

fun signUp(usuario: Usuario) {
    firebaseAuth().createUserWithEmailAndPassword(usuario.email, usuario.senha)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {

                val idUsuario: String = Base64Custom.codificarBase64(usuario.email)
                usuario.idUsuario = idUsuario
                usuario.salvar()


            }
        }
}



fun recuperarEmail() = firebaseAuth().currentUser?.email

































