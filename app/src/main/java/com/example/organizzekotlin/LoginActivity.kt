package com.example.organizzekotlin

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import com.example.organizzekotlin.firebase.FirebaseHelper
import com.example.organizzekotlin.model.Usuario
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

class LoginActivity : AppCompatActivity() {

    private lateinit var campoEmail: EditText
    private lateinit var campoSenha: EditText
    private lateinit var botaoEntrar: Button
    private lateinit var usuario: Usuario
    private lateinit var autenticacao: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        campoEmail = findViewById(R.id.editEmailLogin)
        campoSenha = findViewById(R.id.editSenhaLogin)
        botaoEntrar = findViewById(R.id.buttonEntrar)

        botaoEntrar.setOnClickListener(View.OnClickListener {
            val textoEmail: String = campoEmail.text.toString()
            val textoSenha: String = campoSenha.text.toString()
            if (!textoEmail.isEmpty()) {
                if (!textoSenha.isEmpty()) {
                    usuario = Usuario()
                    usuario.email = textoEmail
                    usuario.senha = textoSenha
                    validarLogin()
                } else {
                    Toast.makeText(
                        this@LoginActivity,
                        "Preencha a senha!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(
                    this@LoginActivity,
                    "Preencha o email!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

    }

    fun validarLogin() {
        autenticacao = FirebaseHelper.firebaseAuth()
        autenticacao.signInWithEmailAndPassword(
            usuario.email,
            usuario.senha
        ).addOnCompleteListener(OnCompleteListener<AuthResult?> { task ->
            if (task.isSuccessful) {
                abrirTelaPrincipal()
            } else {
                val excecao: String
                try {
                    throw task.exception!!
                } catch (e: FirebaseAuthInvalidUserException) {
                    excecao = "Usuário não está cadastrado."
                } catch (e: FirebaseAuthInvalidCredentialsException) {
                    excecao = "E-mail e senha não correspondem a um usuário cadastrado"
                } catch (e: Exception) {
                    excecao = "Erro ao cadastrar usuário: " + e.message
                    e.printStackTrace()
                }
                Toast.makeText(
                    this@LoginActivity,
                    excecao,
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    fun abrirTelaPrincipal() {
        startActivity(Intent(this, PrincipalActivity::class.java))
        finish()
    }

}
