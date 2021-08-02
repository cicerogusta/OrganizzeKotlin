package com.example.organizzekotlin

import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.organizzekotlin.databinding.ActivityDespesasBinding
import com.example.organizzekotlin.firebase.FirebaseHelper
import com.example.organizzekotlin.helper.Base64Custom
import com.example.organizzekotlin.helper.DateCustom
import com.example.organizzekotlin.model.Movimentacao
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference

class DespesasActivity : AppCompatActivity() {

    private lateinit var campoData: EditText
    private lateinit var campoCategoria: EditText
    private lateinit var campoDescricao: EditText
    private lateinit var campoValor: EditText
    private lateinit var movimentacao: Movimentacao
    private val firebaseRef: DatabaseReference = FirebaseHelper.firebaseConnection()
    private val autenticacao: FirebaseAuth = FirebaseHelper.firebaseAuth()
    private var despesa: Double = 0.0
    lateinit var binding: ActivityDespesasBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDespesasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        campoData = binding.editDataDespesa
        campoCategoria = binding.editCategoriaDespesa
        campoDescricao = binding.editDescricaoDespesa
        campoValor = binding.editValorDespesa


        //Preenche o campo data com a date atual
        campoData.setText(DateCustom.dataAtual())

        binding.fabSalvarDespesa.setOnClickListener { if (validarCamposDespesa()) salvarDespesa() }


    }

    fun salvarDespesa() {

        val data: String = campoData.getText().toString()
        val valorRecuperado = campoValor.text.toString().toDouble()
        movimentacao.valor = valorRecuperado
        movimentacao.categoria = campoCategoria.getText().toString()
        movimentacao.descricao = campoDescricao.getText().toString()
        movimentacao.data = data
        movimentacao.tipo = "d"
        val despesaAtualizada = despesa + valorRecuperado
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
                binding.editValorDespesa.error = "Digite um valor"

            }
            textoData.isEmpty() -> {
                binding.editDataDespesa.error = "Digite uma data"

            }
            textoCategoria.isEmpty() -> {
                binding.editCategoriaDespesa.error = "Digite uma categoria"

            }
            textoDescricao.isEmpty() -> {
                binding.editDescricaoDespesa.error = "Digite uma descrição"

            }
            else -> {
                movimentacao = Movimentacao()
                movimentacao.valor = textoValor.toDouble()
                movimentacao.data = textoData
                movimentacao.tipo = "d"
                movimentacao.categoria = textoCategoria
                movimentacao.descricao = textoDescricao
                return true
            }
        }
        return false
    }



    fun atualizarDespesa(despesa: Double?) {
        val emailUsuario = autenticacao.currentUser!!.email
        val idUsuario = Base64Custom.codificarBase64(emailUsuario!!)
        val usuarioRef = firebaseRef.child("movimentacao").child(idUsuario)
        usuarioRef.child("despesa").setValue(despesa)
    }

}
























