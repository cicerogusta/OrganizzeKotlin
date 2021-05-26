package com.example.organizzekotlin.firebase

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.organizzekotlin.helper.Base64Custom
import com.example.organizzekotlin.model.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
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

        fun signUpUser(context: Context) {
            val usuario = Usuario()
            firebaseAuth().createUserWithEmailAndPassword(usuario.email, usuario.senha)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                        val idUsuario: String = Base64Custom.codificarBase64(usuario.email)
                        usuario.idUsuario = idUsuario
                        usuario.salvar()

                    } else {
                        mensagem(context)

                    }
                }


        }

        fun mensagem(activity: Context){

            var excecao = ""
            try {
            } catch (e: FirebaseAuthWeakPasswordException) {
                excecao = "Digite uma senha mais forte!"
            } catch (e: FirebaseAuthInvalidCredentialsException) {
                excecao = "Por favor, digite um e-mail válido"
            } catch (e: FirebaseAuthUserCollisionException) {
                excecao = "Este conta já foi cadastrada"
            } catch (e: Exception) {
                excecao = "Erro ao cadastrar usuário: " + e.message
                e.printStackTrace()
            }

            Toast.makeText(
                activity,
                excecao,
                Toast.LENGTH_SHORT
            ).show()
        }


    }



}

















