package com.example.organizzekotlin

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.organizzekotlin.config.ConfiguracaoFirebase
import com.example.organizzekotlin.helper.Base64Custom
import com.example.organizzekotlin.model.Usuario
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*

class CadastroActivity : AppCompatActivity() {

     lateinit var campoNome: EditText
     lateinit var campoEmail: EditText
     lateinit var campoSenha: EditText
     lateinit var buttonCadastro: Button
     lateinit var autenticacao: FirebaseAuth
     private lateinit var usuario: Usuario

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)

        campoNome = findViewById(R.id.editNome)
        campoEmail = findViewById(R.id.editEmail)
        campoSenha = findViewById(R.id.editSenha)
        buttonCadastro = findViewById(R.id.buttonCadastrar)

        buttonCadastro.setOnClickListener {

           val textoNome: String = campoNome.text.toString()
           val textoEmail: String = campoEmail.text.toString()
           val textoSenha: String = campoSenha.text.toString()

            if (textoNome.isNotEmpty()){
                if (textoEmail.isNotEmpty()){
                    if (textoSenha.isNotEmpty()){
                        usuario = Usuario()
                        usuario.nome = textoNome
                        usuario.email = textoEmail
                        usuario.senha = textoSenha
                        cadastrarUsuario()
                    }else{
                        Toast.makeText(this, "Preencha a senha", Toast.LENGTH_SHORT ).show()

                    }
                }else{
                    Toast.makeText(this, "Preencha o email", Toast.LENGTH_SHORT ).show()

                }
            }else{
                Toast.makeText(this, "Preencha o nome", Toast.LENGTH_SHORT ).show()

            }
        }
    }

    fun cadastrarUsuario(){
        autenticacao = ConfiguracaoFirebase.getFirebaseAuth()
        autenticacao.createUserWithEmailAndPassword(usuario.email, usuario.senha).addOnCompleteListener{task : Task<AuthResult> ->
            if (task.isSuccessful) run {
                val idUsuario: String = Base64Custom.codificarBase64(usuario.email)
                usuario.idUsuario = idUsuario
                usuario.salvar()
                finish()
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

    }
}