package com.example.organizzekotlin

import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.organizzekotlin.databinding.ActivityDespesasBinding
import com.example.organizzekotlin.firebase.FirebaseHelper.firebaseConnection
import com.example.organizzekotlin.firebase.FirebaseHelper.recuperarEmail
import com.example.organizzekotlin.helper.Base64Custom
import com.example.organizzekotlin.helper.DateCustom
import com.example.organizzekotlin.model.Movimentacao
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

class DespesasActivity : AppCompatActivity() {

    var despesa = 0.0
    lateinit var movimentacao: Movimentacao
    lateinit var binding: ActivityDespesasBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDespesasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.editDataDespesa.setText(DateCustom.dataAtual())
        recuperarDespesa()

        binding.fabSalvarDespesa.setOnClickListener { if (validarCamposDespesa()) salvarDespesa() }


    }

    fun salvarDespesa() {

        val data: String = binding.editDataDespesa.text.toString()
        val valorDespesa = binding.editValorDespesa.text.toString().toDouble()
        movimentacao = Movimentacao()
        movimentacao.valor = valorDespesa
        movimentacao.categoria = binding.editCategoriaDespesa.text.toString()
        movimentacao.descricao = binding.editDescricaoDespesa.text.toString()
        movimentacao.data = data
        movimentacao.tipo = "d"

        val despesaAtualizada: Double = despesa + valorDespesa
        atualizarDespesa(despesaAtualizada)

        movimentacao.salvar(data)
        finish()

    }

    fun validarCamposDespesa(): Boolean {

        when {
            binding.editValorDespesa.text.isEmpty() -> {
                binding.editValorDespesa.error = "Digite um valor"

            }
            binding.editDataDespesa.text.isEmpty() -> {
                binding.editDataDespesa.error = "Digite uma data"

            }
            binding.editCategoriaDespesa.text.isEmpty() -> {
                binding.editCategoriaDespesa.error = "Digite uma categoria"

            }
            binding.editDescricaoDespesa.text.isEmpty() -> {
                binding.editDescricaoDespesa.error = "Digite uma descrição"

            }
            else -> {

                return true
            }
        }
        return false
    }

    fun recuperarDespesa() {
        val emailUsuario: String = recuperarEmail()
        val idUsuario = Base64Custom.codificarBase64(emailUsuario)
        val movimentacaoRef: DatabaseReference =
            firebaseConnection().child("movimentacao").child(idUsuario)
        movimentacaoRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val movimentacaoFirebase: Movimentacao? =
                    dataSnapshot.getValue(Movimentacao::class.java)
                if (movimentacaoFirebase != null) {
                    despesa = movimentacaoFirebase.despesa
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    fun atualizarDespesa(despesa: Double?) {
        val emailUsuario: String = recuperarEmail()
        val idUsuario: String = Base64Custom.codificarBase64(emailUsuario)
        val movimentacaoRef: DatabaseReference =
            firebaseConnection().child("movimentacao").child(idUsuario)
        movimentacaoRef.child("despesa").setValue(despesa)
    }


}
























