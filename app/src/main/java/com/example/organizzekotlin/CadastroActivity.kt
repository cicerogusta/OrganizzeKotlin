package com.example.organizzekotlin

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.organizzekotlin.databinding.ActivityCadastroBinding
import com.example.organizzekotlin.firebase.FirebaseHelper
import com.example.organizzekotlin.helper.Base64Custom
import com.example.organizzekotlin.model.Usuario
import com.example.organizzekotlin.util.Validation.isEmail
import com.google.firebase.auth.*

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

    private fun verificaCampos(): Boolean {
        val textoNome = binding.editNome.text.toString()
        val textoEmail = binding.editEmail.text.toString()
        val textoSenha = binding.editSenha.text.toString()

        when {
            textoNome.isEmpty() -> {
                binding.editNome.error = "Insira um nome!"
            }
            textoEmail.isEmpty() || !textoEmail.isEmail() -> {
                binding.editEmail.error =
                    "Insira um emal válido"
            }
            textoSenha.isEmpty() || textoSenha.length < 6 -> {
                binding.editSenha.error =
                    "Insira uma senha válida"
            }
            else -> {
                usuario = Usuario()
                usuario.nome = textoNome
                usuario.email = textoEmail
                usuario.senha = textoSenha
                return true
            }
        }
        return false
    }

    fun cadastrarUsuario() {
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

        //Configura AlertDialog
        alertDialog.setTitle("Não foi possível cadastrar!")
        alertDialog.setMessage("Verifique se os dados foram digitados corretamente!")
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


