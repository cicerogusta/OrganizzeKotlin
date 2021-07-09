package com.example.organizzekotlin

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.organizzekotlin.databinding.ActivityDespesasBinding
import com.example.organizzekotlin.firebase.firebaseConnection
import com.example.organizzekotlin.firebase.recuperarEmail

import com.example.organizzekotlin.helper.Base64Custom
import com.example.organizzekotlin.helper.DateCustom
import com.example.organizzekotlin.model.Movimentacao
import com.example.organizzekotlin.model.Usuario
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class DespesasActivity : AppCompatActivity() {

   var despesaTotal: Double = 0.0

    lateinit var binding: ActivityDespesasBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDespesasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.editData.setText(DateCustom.dataAtual())
        exibirDespesaTotal()

        binding.fabSalvar.setOnClickListener {

            if (validarCamposDespesa()) {

                salvarDespesa()
            }

        }
    }


    fun salvarDespesa() {

        val movimentacao = getMovimentacao()
        movimentacao.tipo = "d"
        movimentacao.salvar(movimentacao.data)
        finish()

    }

    fun validarCamposDespesa(): Boolean {

        val movimentacao = getMovimentacao()

        val mensagem: String
        when {
            movimentacao.valor.toString().isEmpty() -> {
                mensagem = "preeencha o valor"
                mensagemCampoVazio(mensagem)

            }
            movimentacao.data.isEmpty() -> {
                mensagem = "preencha a data"
                mensagemCampoVazio(mensagem)
            }
            movimentacao.categoria.isEmpty() -> {
                mensagem = "preencha a categoria"
                mensagemCampoVazio(mensagem)
            }
            movimentacao.descricao.isEmpty() -> {
                mensagem = "preencha a descrição"
                mensagemCampoVazio(mensagem)
            }
            else -> {
                return true

            }
        }
        return false
    }

    fun getMovimentacao(): Movimentacao {

        val textoValor = binding.editValor.text.toString().toDouble()
        val textoData = binding.editData.text.toString()
        val textoCategoria = binding.editCategoria.text.toString()
        val textoDescricao = binding.editDescricao.text.toString()



        return Movimentacao(
            valor = textoValor,
            data = textoData,
            categoria = textoCategoria,
            descricao = textoDescricao
        )


    }


    fun exibirDespesaTotal() {

        val emailUsuario = recuperarEmail()
        val idUsuario = Base64Custom.codificarBase64(emailUsuario.toString())
       firebaseConnection().child("usuarios").child(idUsuario)

        firebaseConnection().addValueEventListener(object : ValueEventListener {

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val usuario = snapshot.getValue(Usuario::class.java)
                if (usuario != null) {
                    despesaTotal = usuario.despesaTotal
                }
            }

        })

    }


    fun mensagemCampoVazio(mensagem: String) {


        Toast.makeText(
            this,
            mensagem,
            Toast.LENGTH_SHORT
        ).show()


    }
}
























