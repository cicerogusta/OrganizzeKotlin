package com.example.organizzekotlin

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.organizzekotlin.databinding.ActivityCadastroBinding
import com.example.organizzekotlin.firebase.FirebaseCustom
import com.example.organizzekotlin.helper.Base64Custom
import com.example.organizzekotlin.model.Usuario
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.*

class CadastroActivity : AppCompatActivity() {


    private lateinit var binding: ActivityCadastroBinding
    lateinit var usuario: Usuario

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityCadastroBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.buttonCadastrar.setOnClickListener {

            val textoNome: String = binding.editNome.toString()
            val textoEmail: String = binding.editEmail.toString()
            val textoSenha: String = binding.editSenha.toString()


            if (textoNome.isNotEmpty()) {
                if (textoEmail.isNotEmpty()) {
                    if (textoSenha.isNotEmpty()) {

                        usuario = Usuario()
                        usuario.nome = textoNome
                        usuario.email = textoEmail
                        usuario.senha = textoSenha
                        signUpUser()

                    } else {
                        Toast.makeText(this, "Preencha a senha", Toast.LENGTH_SHORT).show()

                    }
                } else {
                    Toast.makeText(this, "Preencha o email", Toast.LENGTH_SHORT).show()

                }
            } else {
                Toast.makeText(this, "Preencha o nome", Toast.LENGTH_SHORT).show()

            }
        }
    }

    private fun signUpUser() {

        val auth: FirebaseAuth = FirebaseCustom.firebaseAuth();
        auth.createUserWithEmailAndPassword(usuario.email, usuario.senha).addOnCompleteListener(
            this
        ) {
            if (it.isSuccessful) {

                val idUsuario: String = Base64Custom.codificarBase64(usuario.email)
                usuario.idUsuario = idUsuario
                usuario.salvar()
                finish()

            } else {
                var excecao = ""
                try {
                    throw it.exception!!
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


    }
}


