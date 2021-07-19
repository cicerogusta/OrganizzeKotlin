package com.example.organizzekotlin

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.organizzekotlin.databinding.ActivityCadastroBinding
import com.example.organizzekotlin.firebase.FirebaseHelper.firebaseAuth
import com.example.organizzekotlin.helper.Base64Custom
import com.example.organizzekotlin.model.Usuario
import com.example.organizzekotlin.util.Validation.isEmail
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException

class CadastroActivity : AppCompatActivity() {


    lateinit var binding: ActivityCadastroBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCadastroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonCadastrar.setOnClickListener {
            verificarECadastrar()
        }
    }


    fun verificarECadastrar() {

        val textoNome = binding.editNome.text.toString()
        val textoEmail = binding.editEmail.text.toString()
        val textoSenha = binding.editSenha.text.toString()

        val mensagem: String

        when {
            textoNome.isEmpty() -> {
                mensagem = "nome inválido!"
                binding.editNome.error = mensagem

            }
            textoEmail.isEmpty() || !textoEmail.isEmail() -> {
                mensagem = "email inválido!"
                binding.editEmail.error = mensagem

            }
            textoSenha.isEmpty() || textoSenha == " " || textoSenha.length < 6 -> {
                mensagem = "Digite uma senha mais forte com letras e numeros"
                binding.editSenha.error = mensagem
            }
            else -> {
                cadastrar(textoEmail, textoSenha)


            }
        }


    }


    fun cadastrar(email: String, senha: String) {
        binding.loading = true
        firebaseAuth().createUserWithEmailAndPassword(email, senha)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    val idUsuario: String = Base64Custom.codificarBase64(email)
                    Usuario().idUsuario = idUsuario
                    Usuario().salvar()

                } else {
                    var excecao = ""
                    try {
                        throw task.exception!!
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
                        this@CadastroActivity,
                        excecao,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        binding.loading = false
    }
}


