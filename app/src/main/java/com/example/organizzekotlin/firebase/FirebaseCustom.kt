package com.example.organizzekotlin.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class FirebaseCustom {



    companion object{



        fun firebaseConnection(): DatabaseReference {

            return FirebaseDatabase.getInstance().reference;

        }


        fun firebaseAuth(): FirebaseAuth {

            return FirebaseAuth.getInstance()

        }

            }

        }















