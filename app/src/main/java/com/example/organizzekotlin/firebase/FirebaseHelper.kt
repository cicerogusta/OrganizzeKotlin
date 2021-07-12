package com.example.organizzekotlin.firebase

import com.example.organizzekotlin.helper.Base64Custom
import com.example.organizzekotlin.model.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


fun firebaseConnection(): DatabaseReference = FirebaseDatabase.getInstance().reference;


fun firebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()




fun recuperarEmail() = firebaseAuth().currentUser?.email

































