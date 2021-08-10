package com.example.organizzekotlin

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.organizzekotlin.databinding.ActivityLoginBinding
import com.example.organizzekotlin.firebase.FirebaseHelper

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonEntrar.setOnClickListener { if (verificaCamposLogin()) login() }
    }

    fun verificaCamposLogin(): Boolean {


        when {
            binding.editEmailLogin.text.isEmpty() -> {
                binding.editEmailLogin.error = "Digite o email para fazer login"

            }
            binding.editSenhaLogin.text.isEmpty() -> {
                binding.editSenhaLogin.error = "Digite a senha para fazer login"
            }

            else -> {
               return true
            }
        }
        return false
    }

    fun login() {
        binding.loading = true
        FirebaseHelper.firebaseAuth().signInWithEmailAndPassword(binding.editEmailLogin.text.toString(),
            binding.editSenhaLogin.text.toString()
        )
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    abrirTelaPrincipal()

                } else {
                    dialogLogin()
                }
                binding.loading = false

            }

    }


    fun abrirTelaPrincipal() {
        startActivity(Intent(this, PrincipalActivity::class.java))
        finish()
    }

    fun dialogLogin() {

        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(this)

        alertDialog.setTitle("Não foi possível entrar!")
        alertDialog.setMessage("Não existe um usuário cadastrado com este email!")
        alertDialog.setCancelable(false)
        alertDialog.setPositiveButton(
            "Confirmar"
        ) { dialog, which ->


        }
        alertDialog.setNegativeButton(
            "Cancelar"
        ) { dialog, which ->

        }
        alertDialog.create()?.show()
    }


}
