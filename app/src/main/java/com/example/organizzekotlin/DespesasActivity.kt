package com.example.organizzekotlin

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.organizzekotlin.databinding.ActivityCadastroBinding
import com.example.organizzekotlin.databinding.ActivityDespesasBinding
import com.example.organizzekotlin.databinding.ActivityPrincipalBinding
import com.example.organizzekotlin.firebase.FirebaseHelper
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

    private lateinit var campoData: EditText
    private lateinit var campoCategoria: EditText
    private lateinit var campoDescricao: EditText
    private lateinit var campoValor: EditText
    private lateinit var movimentacao: Movimentacao
    private val firebaseRef: DatabaseReference = FirebaseHelper.firebaseConnection()
    private val autenticacao: FirebaseAuth = FirebaseHelper.firebaseAuth()
    private var despesaTotal: Double = 0.0
    lateinit var binding: ActivityDespesasBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDespesasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        campoData = binding.editDataDespesa
        campoCategoria = binding.editCategoriaDespesa
        campoDescricao = binding.editDescricaoDespesa
        campoValor = binding.editValor




        //Preenche o campo data com a date atual
        campoData.setText(DateCustom.dataAtual())
        recuperarDespesaTotal()

        binding.fabSalvarDespesa.setOnClickListener { if (validarCamposDespesa()) salvarDespesa() }




    }

    fun salvarDespesa() {

            movimentacao = Movimentacao()
            val data: String = campoData.getText().toString()
            val valorRecuperado = campoValor.text.toString().toDouble()
            movimentacao.valor = valorRecuperado
            movimentacao.categoria = campoCategoria.getText().toString()
            movimentacao.descricao = campoDescricao.getText().toString()
            movimentacao.data = data
            movimentacao.tipo = "d"
            val despesaAtualizada = despesaTotal + valorRecuperado
            atualizarDespesa(despesaAtualizada)
            movimentacao.salvar(data)
            finish()

    }

    fun validarCamposDespesa(): Boolean {
        val textoValor = campoValor.text.toString()
        val textoData: String = campoData.getText().toString()
        val textoCategoria: String = campoCategoria.getText().toString()
        val textoDescricao: String = campoDescricao.getText().toString()

        when {
            textoValor.isEmpty() -> {
                binding.editValor.error = "Digite um valor"

            }
            textoData.isEmpty() -> {
                binding.editDataDespesa.error = "Digite uma data"

            }
            textoCategoria.isEmpty() -> {
                binding.editCategoriaDespesa.error = "Digite uma categoria"

            }
            textoDescricao.isEmpty() -> {
                binding.editDescricaoDespesa.error = "Digite uma descrição"

            }else -> {
            return true
           }
        }
        return false
    }

    fun recuperarDespesaTotal() {
        val emailUsuario = autenticacao.currentUser!!.email
        val idUsuario = Base64Custom.codificarBase64(emailUsuario!!)
        val usuarioRef = firebaseRef.child("usuarios").child(idUsuario)
        usuarioRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val usuario = dataSnapshot.getValue(Usuario::class.java)
                if (usuario != null) {
                    despesaTotal = usuario.despesaTotal
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    fun atualizarDespesa(despesa: Double?) {
        val emailUsuario = autenticacao.currentUser!!.email
        val idUsuario = Base64Custom.codificarBase64(emailUsuario!!)
        val usuarioRef = firebaseRef.child("usuarios").child(idUsuario)
        usuarioRef.child("despesaTotal").setValue(despesa)
    }

}
























