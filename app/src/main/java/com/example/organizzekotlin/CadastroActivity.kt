package com.example.organizzekotlin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.organizzekotlin.databinding.ActivityCadastroBinding
import com.example.organizzekotlin.firebase.firebaseAuth
import com.example.organizzekotlin.helper.Base64Custom
import com.example.organizzekotlin.model.Usuario
import com.example.organizzekotlin.util.isEmail

class CadastroActivity : AppCompatActivity() {


    lateinit var binding: ActivityCadastroBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCadastroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonCadastrar.setOnClickListener {
            if (validarCamposCadastro()) {
                signUp(getUsuario())
                binding.loading = true
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
            usuario.email.isEmpty() || !usuario.email.isEmail() -> {
                mensagem = "email inválido!"
                binding.editEmail.error = mensagem

            }
            usuario.senha.isEmpty() || usuario.senha == " " || usuario.senha.length < 6 -> {
                mensagem = "Digite uma senha mais forte com letras e numeros"
                binding.editSenha.error = mensagem
            }
            else -> {
                return true

            }
        }
        return false

    }


    fun signUp(usuario: Usuario) {
        binding.loading = true
        firebaseAuth().createUserWithEmailAndPassword(usuario.email, usuario.senha)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    val idUsuario: String = Base64Custom.codificarBase64(usuario.email)
                    usuario.idUsuario = idUsuario
                    usuario.salvar()


                }
                binding.loading = false
            }
    }



}






























