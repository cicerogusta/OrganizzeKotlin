package com.example.organizzekotlin

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.organizzekotlin.databinding.ActivityCadastroBinding
import com.example.organizzekotlin.firebase.FirebaseHelper
import com.example.organizzekotlin.model.Usuario
import com.example.organizzekotlin.util.isEmail
import com.example.organizzekotlin.util.isPassword

class CadastroActivity : AppCompatActivity() {


    lateinit var binding: ActivityCadastroBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCadastroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonCadastrar.setOnClickListener {
            if (validarCamposCadastro()){ FirebaseHelper.signUp(getUsuario(), this)
                finish()
            }
        }
    }

    fun getUsuario(): Usuario {

        val textoNome = binding.editNome.text.toString()
        val textoEmail = binding.editEmail.text.toString()
        val textoSenha = binding.editSenha.text.toString()

        return Usuario(null, textoNome, textoEmail, textoSenha)

    }


    fun validarCamposCadastro(): Boolean {

       val usuario = getUsuario()

        val mensagem: String
        when {
            usuario.nome.isEmpty() -> {
                mensagem = "nome inválido!"
                binding.editNome.error = mensagem

            }
            usuario.email.isEmpty() || !usuario.email.isEmail()-> {
                mensagem = "email inválido!"
                binding.editEmail.error = mensagem

            }
            usuario.senha.isEmpty() || usuario.senha.isPassword().toString().length < 6-> {
                mensagem = "Sua senha deve conter ao menos 6 variando letra e numero"
                binding.editSenha.error = mensagem
            }
            else -> {
                return true

            }
        }
        return false

    }





}





























