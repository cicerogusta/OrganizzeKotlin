package com.example.organizzekotlin

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.organizzekotlin.databinding.ActivityCadastroBinding
import com.example.organizzekotlin.firebase.FirebaseHelper
import com.example.organizzekotlin.helper.Base64Custom
import com.example.organizzekotlin.model.Usuario
import com.example.organizzekotlin.util.Validation.isEmail
import com.google.firebase.auth.FirebaseAuth

class CadastroActivity : AppCompatActivity() {

    private lateinit var autenticacao: FirebaseAuth
    private lateinit var usuario: Usuario
    lateinit var binding: ActivityCadastroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCadastroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sh = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE)
        sh.edit().putBoolean("jumpSlides", true).apply()

        binding.buttonCadastrar.setOnClickListener { if (verificaCampos()) cadastrarUsuario() }

    }

     fun verificaCampos(): Boolean {


        when {
            binding.editNome.text.isEmpty() -> {
                binding.editNome.error = "Insira um nome!"
            }
            binding.editEmail.text.isEmpty() || !binding.editEmail.text.toString().isEmail() -> {
                binding.editEmail.error =
                    "Insira um emal válido"
            }
            binding.editSenha.text.isEmpty() || binding.editSenha.text.length < 6 -> {
                binding.editSenha.error =
                    "Sua senha deve conter ao menos 6 caracteres!"
            }
            else -> {
                return true
            }
        }
        return false
    }

    fun cadastrarUsuario() {
        usuario = Usuario()
        usuario.nome = binding.editNome.text.toString()
        usuario.email = binding.editEmail.text.toString()
        usuario.senha = binding.editSenha.text.toString()

        binding.loading = true
        autenticacao = FirebaseHelper.firebaseAuth()
        autenticacao.createUserWithEmailAndPassword(
            usuario.email, usuario.senha
        ).addOnCompleteListener(
            this
        ) { task ->
            if (task.isSuccessful) {
                val idUsuario = Base64Custom.codificarBase64(usuario.email)
                usuario.idUsuario = idUsuario
                usuario.salvar()
                finish()
            } else {
                dialogSignUp()
            }
        }
        binding.loading = false
    }

    fun dialogSignUp() {

        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(this)

        alertDialog.setTitle("Não foi possível cadastrar!")
        alertDialog.setMessage("Já existe um usuário cadastrado com este email!")
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


