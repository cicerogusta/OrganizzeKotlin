package com.example.organizzekotlin

import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.organizzekotlin.databinding.ActivityReceitasBinding
import com.example.organizzekotlin.firebase.FirebaseHelper
import com.example.organizzekotlin.helper.DateCustom
import com.example.organizzekotlin.model.Movimentacao
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference

class ReceitasActivity : AppCompatActivity() {

    lateinit var binding: ActivityReceitasBinding
    private lateinit var campoValor: EditText
    private lateinit var campoData: EditText
    private lateinit var campoCategoria: EditText
    private lateinit var campoDescricao: EditText
    private lateinit var movimentacao: Movimentacao
    private val firebaseRef: DatabaseReference = FirebaseHelper.firebaseConnection()
    private val autenticacao: FirebaseAuth = FirebaseHelper.firebaseAuth()

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

        binding.fabSalvarReceita.setOnClickListener { salvarReceita() }
    }

    fun salvarReceita() {
        if (validarCamposReceita()) {
            movimentacao = Movimentacao()
            val data: String = campoData.getText().toString()
            val receita = campoValor.text.toString().toDouble()
            movimentacao.valor = receita
            movimentacao.categoria = campoCategoria.text.toString()
            movimentacao.descricao = campoDescricao.text.toString()
            movimentacao.data = data
            movimentacao.tipo = "r"
//            atualizarReceita(receitaAtualizada)
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


//    fun atualizarReceita(receita: Double) {
//        val emailUsuario = autenticacao.currentUser!!.email
//        val idUsuario = Base64Custom.codificarBase64(emailUsuario!!)
//        val usuarioRef = firebaseRef.child("movimentacao").child(idUsuario)
//        usuarioRef.child("receitas").setValue(receita)
//    }
}