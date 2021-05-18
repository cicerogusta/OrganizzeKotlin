package com.example.organizzekotlin

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.organizzekotlin.firebase.FirebaseCustom
import com.example.organizzekotlin.helper.Base64Custom
import com.example.organizzekotlin.helper.DateCustom
import com.example.organizzekotlin.model.Movimentacao
import com.example.organizzekotlin.model.Usuario
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

class DespesasActivity : AppCompatActivity() {
    private lateinit var campoData: TextInputEditText
    private lateinit var campoCategoria: TextInputEditText
    private lateinit var campoDescricao: TextInputEditText
    private lateinit var campoValor: EditText
    private lateinit var movimentacao: Movimentacao
    private val firebaseRef: DatabaseReference = FirebaseCustom.firebaseConnection()
    private val autenticacao: FirebaseAuth = FirebaseCustom.firebaseAuth()
    private var despesaTotal: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_despesas)

        campoValor = findViewById(R.id.editValor)
        campoData = findViewById(R.id.editData)
        campoCategoria = findViewById(R.id.editCategoria)
        campoDescricao = findViewById(R.id.editDescricao)

        //Preenche o campo data com a date atual

        //Preenche o campo data com a date atual
        campoData.setText(DateCustom.dataAtual())
        recuperarDespesaTotal()
    }

    fun salvarDespesa(view: View) {

        if (validarCamposDespesa()) {
             movimentacao = Movimentacao()
            val data: String = campoData.text.toString()
            val valorRecuperado: Double = campoValor.toString().toDouble()
            movimentacao.valor = valorRecuperado
            movimentacao.categoria = campoCategoria.text.toString()
            movimentacao.descricao = campoDescricao.text.toString()
            movimentacao.data = data
            movimentacao.tipo = "d"

            var despesaAtualizada:Double = despesaTotal + valorRecuperado
            atualizarDespesa(despesaAtualizada)
            movimentacao.salvar(data)
            finish()

            }

    }

    fun validarCamposDespesa(): Boolean {
        val textoValor = campoValor.text.toString()
        val textoData = campoData.text.toString()
        val textoCategoria = campoCategoria.text.toString()
        val textoDescricao = campoDescricao.text.toString()
        return if (!textoValor.isEmpty()) {
            if (!textoData.isEmpty()) {
                if (!textoCategoria.isEmpty()) {
                    if (!textoDescricao.isEmpty()) {
                        true
                    } else {
                        Toast.makeText(
                            this@DespesasActivity,
                            "Descrição não foi preenchida!",
                            Toast.LENGTH_SHORT
                        ).show()
                        false
                    }
                } else {
                    Toast.makeText(
                        this@DespesasActivity,
                        "Categoria não foi preenchida!",
                        Toast.LENGTH_SHORT
                    ).show()
                    false
                }
            } else {
                Toast.makeText(
                    this@DespesasActivity,
                    "Data não foi preenchida!",
                    Toast.LENGTH_SHORT
                ).show()
                false
            }
        } else {
            Toast.makeText(
                this@DespesasActivity,
                "Valor não foi preenchido!",
                Toast.LENGTH_SHORT
            ).show()
            false
        }
    }

    fun recuperarDespesaTotal() {
        val emailUsuario: String = autenticacao.currentUser?.email.orEmpty()

        val idUsuario: String = Base64Custom.codificarBase64(emailUsuario)
        val usuarioRef = firebaseRef.child("usuarios").child(idUsuario)
        usuarioRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {

                val usuario: Usuario = snapshot.value as Usuario
                despesaTotal = usuario.despesaTotal
            }

        })

    }

    fun atualizarDespesa(despesa: Double?) {
        val emailUsuario = autenticacao.currentUser?.email.orEmpty()
        val idUsuario = Base64Custom.codificarBase64(emailUsuario)
        val usuarioRef = firebaseRef.child("usuarios").child(idUsuario)
        usuarioRef.child("despesaTotal").setValue(despesa)
    }

}





