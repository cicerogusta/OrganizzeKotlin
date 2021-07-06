package com.example.organizzekotlin

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.organizzekotlin.databinding.ActivityCadastroBinding
import com.example.organizzekotlin.firebase.FirebaseHelper
import com.example.organizzekotlin.model.Usuario

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

       val campos = getUsuario()

        val mensagem: String
        when {
            campos.nome.isEmpty() -> {
                mensagem = "preeencha o nome"
                mensagemCampoVazio(mensagem)

            }
            campos.email.isEmpty() -> {
                mensagem = "preencha o e-mail"
                mensagemCampoVazio(mensagem)
            }
            campos.senha.isEmpty() -> {
                mensagem = "preencha a senha"
                mensagemCampoVazio(mensagem)
            }
            else -> {
                return true

            }
        }
        return false

    }



    fun mensagemCampoVazio(mensagem:String){

        Toast.makeText(
            this@CadastroActivity,
            mensagem,
            Toast.LENGTH_SHORT
        ).show()
    }
}





























