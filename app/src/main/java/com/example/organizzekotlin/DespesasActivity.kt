package com.example.organizzekotlin

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.organizzekotlin.databinding.ActivityDespesasBinding
import com.example.organizzekotlin.firebase.FirebaseHelper
import com.example.organizzekotlin.helper.Base64Custom
import com.example.organizzekotlin.model.Movimentacao
import com.example.organizzekotlin.model.Usuario
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class DespesasActivity : AppCompatActivity() {

    lateinit var binding: ActivityDespesasBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDespesasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recuperarDespesaTotal()

        if (validarCamposDespesa()){

            binding.fabSalvar.setOnClickListener {

                salvarDespesa()

            }
        }
    }





    fun salvarDespesa() {

        val textoValor = binding.editValor.text.toString()
        val textoData = binding.editData.text.toString()
        val textoCategoria = binding.editCategoria.text.toString()
        val textoDescricao = binding.editDescricao.text.toString()
        val despesaTotal = 0
        val valorRecuperado: Double = textoValor.toDouble()

        val movimentacao = Movimentacao(textoData, textoCategoria, textoDescricao, "d", valorRecuperado, null)


        val despesaAtualizada:Double = despesaTotal + valorRecuperado
        atualizarDespesa(despesaAtualizada)
        movimentacao.salvar(movimentacao.data)
        finish()

    }

    fun validarCamposDespesa(): Boolean {

        val campos = getCampos()

        var mensagem = ""
        when {
            campos[0].isEmpty() -> {
                mensagem = "preeencha o valor"
                mensagemCampoVazio(mensagem)

            }
            campos[1].isEmpty() -> {
                mensagem = "preencha a data"
                mensagemCampoVazio(mensagem)
            }
            campos[2].isEmpty() -> {
                mensagem = "preencha a categoria"
                mensagemCampoVazio(mensagem)
            }
            campos[3].isEmpty() -> {
                mensagem = "preencha a descrição"
                mensagemCampoVazio(mensagem)
            }
            else -> {
                return true

            }
        }
        return false
    }

    fun getCampos(): List<String> {

        //TODO VERIFICAR CONTEXTO

        val textoValor = binding.editValor.text.toString()
        val textoData = binding.editData.text.toString()
        val textoCategoria = binding.editCategoria.text.toString()
        val textoDescricao = binding.editDescricao.text.toString()


        return listOf(textoValor, textoData, textoCategoria, textoDescricao)


    }



    fun exibirDespesaTotal() {

        val emailUsuario: String? = FirebaseHelper.firebaseAuth().currentUser?.email

        val idUsuario: String? = emailUsuario?.let { Base64Custom.codificarBase64(it) }
        idUsuario?.let {
            FirebaseHelper.firebaseConnection().child("usuarios").child(
                it
            )
        }?.addValueEventListener(object : ValueEventListener {
            var despesaTotal = 0.0

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
        val emailUsuario = FirebaseHelper.firebaseAuth().currentUser?.email.orEmpty()
        val idUsuario = Base64Custom.codificarBase64(emailUsuario)
        val usuarioRef = FirebaseHelper.firebaseConnection().child("usuarios").child(idUsuario)
        usuarioRef.child("despesaTotal").setValue(despesa)
    }






    fun mensagemCampoVazio(mensagem: String) {


        Toast.makeText(
            this,
            mensagem,
            Toast.LENGTH_SHORT
        ).show()



    }
    }















