package com.example.organizzekotlin.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


object FirebaseHelper {

    fun firebaseConnection() = FirebaseDatabase.getInstance().reference

    fun firebaseAuth() = FirebaseAuth.getInstance()

    fun recuperarEmail() = firebaseAuth().currentUser?.email

}