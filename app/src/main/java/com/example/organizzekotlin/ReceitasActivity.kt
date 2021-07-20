package com.example.organizzekotlin

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.organizzekotlin.databinding.ActivityReceitasBinding
import com.example.organizzekotlin.firebase.FirebaseHelper
import com.example.organizzekotlin.helper.Base64Custom
import com.example.organizzekotlin.helper.DateCustom
import com.example.organizzekotlin.model.Movimentacao
import com.example.organizzekotlin.model.Usuario
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

class ReceitasActivity : AppCompatActivity() {

    lateinit var binding: ActivityReceitasBinding
    var receitaTotal = 0.0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReceitasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.editDataReceita.setText(DateCustom.dataAtual())
        exibirReceitaTotal()

        exibirReceitaTotal()
        binding.fabSalvar.setOnClickListener {
            if (validarCamposReceita()) {
                salvarReceita()
            }
        }


    }

    fun salvarReceita() {

        val movimentacao = getMovimentacao()
        movimentacao.tipo = "r"
        movimentacao.salvar(movimentacao.data)
        finish()

    }

    fun validarCamposReceita(): Boolean {

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
        val textoData = binding.editDataReceita.text.toString()
        val textoCategoria = binding.editCategoria.text.toString()
        val textoDescricao = binding.editDescricao.text.toString()



        return Movimentacao(
            valor = textoValor,
            data = textoData,
            categoria = textoCategoria,
            descricao = textoDescricao
        )


    }


    fun exibirReceitaTotal() {

        val emailUsuario = FirebaseHelper.recuperarEmail()
        val idUsuario = Base64Custom.codificarBase64(emailUsuario.toString())
        FirebaseHelper.firebaseConnection().child("usuarios").child(idUsuario)

        FirebaseHelper.firebaseConnection().addValueEventListener(object : ValueEventListener {

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val usuario = snapshot.getValue(Usuario::class.java)
                if (usuario != null) {
                    receitaTotal = usuario.receitaTotal
                }
            }

        })

    }

    fun recuperarReceitaTotal() {
        val emailUsuario: String =
            FirebaseHelper.recuperarEmail().toString()
        val idUsuario = Base64Custom.codificarBase64(emailUsuario)
        val usuarioRef: DatabaseReference =
            FirebaseHelper.firebaseConnection().child("usuarios").child(idUsuario)
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

    fun mensagemCampoVazio(mensagem: String) {


        Toast.makeText(
            this,
            mensagem,
            Toast.LENGTH_SHORT
        ).show()


    }
}