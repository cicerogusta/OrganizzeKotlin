package com.example.organizzekotlin

import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.organizzekotlin.databinding.ActivityReceitasBinding
import com.example.organizzekotlin.firebase.FirebaseHelper.firebaseConnection
import com.example.organizzekotlin.firebase.FirebaseHelper.recuperarEmail
import com.example.organizzekotlin.helper.Base64Custom
import com.example.organizzekotlin.helper.DateCustom
import com.example.organizzekotlin.model.Movimentacao
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
    private var receita: Double = 0.0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReceitasBinding.inflate(layoutInflater)
        setContentView(binding.root)
        campoValor = binding.editValor
        campoData = binding.editDataReceita
        campoCategoria = binding.editCategoria
        campoDescricao = binding.editDescricao

        campoData.setText(DateCustom.dataAtual())
        recuperarReceitaTotal()

        binding.fabSalvarReceita.setOnClickListener { salvarReceita() }
    }

    fun salvarReceita() {
        if (validarCamposReceita()) {
            movimentacao = Movimentacao()
            val data: String = campoData.text.toString()
            val valorReceita = campoValor.text.toString().toDouble()
            movimentacao.valor = valorReceita
            movimentacao.categoria = campoCategoria.text.toString()
            movimentacao.descricao = campoDescricao.text.toString()
            movimentacao.data = data
            movimentacao.tipo = "r"

            val receitaAtualizada: Double = receita + valorReceita
            atualizarReceita(receitaAtualizada)

            movimentacao.salvar(data)
            finish()
        }
    }

    fun recuperarReceitaTotal() {
        val emailUsuario: String = recuperarEmail()
        val idUsuario: String = Base64Custom.codificarBase64(emailUsuario)
        val usuarioRef: DatabaseReference = firebaseConnection().child("movimentacao").child(idUsuario)
        usuarioRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val movimentacaoFirebase: Movimentacao? = dataSnapshot.getValue(Movimentacao::class.java)
                if (movimentacaoFirebase != null) {
                    receita = movimentacaoFirebase.receita
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    fun atualizarReceita(receita: Double?) {
        val emailUsuario: String = recuperarEmail()
        val idUsuario = Base64Custom.codificarBase64(emailUsuario)
        val usuarioRef: DatabaseReference = firebaseConnection().child("movimentacao").child(idUsuario)
        usuarioRef.child("receita").setValue(receita)
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



}