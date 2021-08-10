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
    private lateinit var movimentacao: Movimentacao
    private var receita: Double = 0.0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReceitasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.editDataReceita.setText(DateCustom.dataAtual())
        recuperarReceita()

        binding.fabSalvarReceita.setOnClickListener { salvarReceita() }
    }

    fun salvarReceita() {
        if (validarCamposReceita()) {
            movimentacao = Movimentacao()
            val data: String = binding.editDataReceita.text.toString()
            val valorReceita = binding.editValor.text.toString().toDouble()
            movimentacao.valor = valorReceita
            movimentacao.categoria = binding.editCategoriaReceita.text.toString()
            movimentacao.descricao = binding.editDescricaoReceita.text.toString()
            movimentacao.data = data
            movimentacao.tipo = "r"

            val receitaAtualizada: Double = receita + valorReceita
            atualizarReceita(receitaAtualizada)

            movimentacao.salvar(data)
            finish()
        }
    }

    fun recuperarReceita() {
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

        when {
            binding.editValor.text.isEmpty() -> {
                binding.editValor.error = "Digite um valor"

            }
            binding.editDataReceita.text.isEmpty() -> {
                binding.editDataReceita.error = "Digite uma data"

            }
            binding.editCategoriaReceita.text.isEmpty() -> {
                binding.editCategoriaReceita.error = "Digite uma categoria"

            }
            binding.editDescricaoReceita.text.isEmpty() -> {
                binding.editDescricaoReceita.error = "Digite uma descrição"

            }
            else -> {
                return true
            }
        }
        return false

    }



}