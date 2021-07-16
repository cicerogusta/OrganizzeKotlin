package com.example.organizzekotlin


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.organizzekotlin.databinding.ActivityLoginBinding
import com.example.organizzekotlin.firebase.FirebaseHelper.firebaseAuth
import com.example.organizzekotlin.model.Usuario

abstract class LoginActivity : AppCompatActivity() {


    val campoEmail = binding.editEmailLogin
    val campoSenha = binding.editSenhaLogin
    val botaoEntrar = binding.buttonEntrar

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        botaoEntrar.setOnClickListener {
            if (verificaCamposLogin()) {
                login()
            }
        }

    }

    fun userLogin(): Usuario {

        val textoEmail = campoEmail.text.toString()
        val textoSenha = campoSenha.text.toString()
        return Usuario(null, textoEmail, textoSenha)
    }

    fun verificaCamposLogin(): Boolean {

        when {

            userLogin().email.isEmpty() -> {
                campoEmail.error = "email precisa ser preenchido!"
            }

            userLogin().senha.isEmpty() -> {
                campoSenha.error = "senha precisa ser preenchida!"
            }
            else -> {
                return true
            }
        }
        return false

    }

    fun login() {

        val usuario = userLogin()
        firebaseAuth().signInWithEmailAndPassword(usuario.email, usuario.senha)
            .addOnSuccessListener {
                abrirTelaPrincipal()

            }

    }

    fun abrirTelaPrincipal() {
        startActivity(Intent(this, PrincipalActivity::class.java))
    }

}
