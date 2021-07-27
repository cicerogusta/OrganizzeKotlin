package com.example.organizzekotlin

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.organizzekotlin.databinding.ActivityReceitasBinding
import com.example.organizzekotlin.firebase.FirebaseHelper
import com.example.organizzekotlin.helper.Base64Custom
import com.example.organizzekotlin.helper.DateCustom
import com.example.organizzekotlin.model.Movimentacao
import com.example.organizzekotlin.model.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

class ReceitasActivity : AppCompatActivity() {

    lateinit var binding: ActivityReceitasBinding
    private lateinit var campoValor: EditText
    private lateinit var campoData: EditText
    private lateinit var campoCategoria: EditText
    private lateinit var campoDescricao: EditText
    private lateinit var movimentacao: Movimentacao
    private val firebaseRef: DatabaseReference = FirebaseHelper.firebaseConnection()
    private val autenticacao: FirebaseAuth = FirebaseHelper.firebaseAuth()
    private var receitaTotal: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReceitasBinding.inflate(layoutInflater)
        setContentView(binding.root)
        campoValor = binding.editValor
        campoData = binding.editDataReceita
        campoCategoria = binding.editCategoria
        campoDescricao = binding.editDescricao

        //Preenche o campo data com a date atual
        campoData.setText(DateCustom.dataAtual())
        recuperarReceitaTotal()

        binding.fabSalvarReceita.setOnClickListener { salvarReceita() }
    }

    fun salvarReceita() {
        if (validarCamposReceita()) {
            movimentacao = Movimentacao()
            val data: String = campoData.getText().toString()
            val valorRecuperado = campoValor.text.toString().toDouble()
            movimentacao.valor = valorRecuperado
            movimentacao.categoria = campoCategoria.text.toString()
            movimentacao.descricao = campoDescricao.text.toString()
            movimentacao.data = data
            movimentacao.tipo = "r"
            val receitaAtualizada = receitaTotal + valorRecuperado
            atualizarReceita(receitaAtualizada)
            movimentacao.salvar(data)
            finish()
        }
    }

    fun validarCamposReceita(): Boolean {
        val textoValor = campoValor.text.toString()
        val textoData: String = campoData.getText().toString()
        val textoCategoria: String = campoCategoria.getText().toString()
        val textoDescricao: String = campoDescricao.getText().toString()

        when {
            textoValor.isEmpty() -> {
                binding.editValor.error = "Digite um valor"

            }
            textoData.isEmpty() -> {
                binding.editDataReceita.error = "Digite uma data"

            }
            textoCategoria.isEmpty() -> {
                binding.editCategoria.error = "Digite uma categoria"

            }
            textoDescricao.isEmpty() -> {
                binding.editDescricao.error = "Digite uma descrição"

            }
            else -> {
                return true
            }
        }
        return false

    }

    fun recuperarReceitaTotal() {
        val emailUsuario = autenticacao.currentUser!!.email
        val idUsuario = Base64Custom.codificarBase64(emailUsuario!!)
        val usuarioRef = firebaseRef.child("usuarios").child(idUsuario)
        usuarioRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val usuario = dataSnapshot.getValue(Usuario::class.java)
                if (usuario != null) {
                    receitaTotal = usuario.receitaTotal
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    fun atualizarReceita(receita: Double?) {
        val emailUsuario = autenticacao.currentUser!!.email
        val idUsuario = Base64Custom.codificarBase64(emailUsuario!!)
        val usuarioRef = firebaseRef.child("usuarios").child(idUsuario)
        usuarioRef.child("receitaTotal").setValue(receita)
    }
}