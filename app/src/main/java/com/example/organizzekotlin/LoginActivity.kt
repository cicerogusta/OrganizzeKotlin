package com.example.organizzekotlin

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

        binding.buttonEntrar.setOnClickListener { login() }
    }

    fun login() {

        val textoEmail = binding.editEmailLogin.text.toString()
        val textoSenha = binding.editSenhaLogin.text.toString()

        val mensagem: String

        when {
            textoEmail.isEmpty()  -> {
                mensagem = "Preencha o campo de email"
                binding.editEmailLogin.error = mensagem

            }
            textoSenha.isEmpty() -> {
                mensagem = "Preencha o campo senha"
                binding.editSenhaLogin.error = mensagem
            }

            else -> {
                binding.loading = true
                FirebaseHelper.firebaseAuth().signInWithEmailAndPassword(textoEmail, textoSenha)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            abrirTelaPrincipal()

                        }else {
                            binding.editEmailLogin.error = "Não existe usuário com este email cadastrado!"
                        }
                        binding.loading = false

                    }

            }
        }
    }

    fun abrirTelaPrincipal() {
        startActivity(Intent(this, PrincipalActivity::class.java))
        finish()
    }


}
