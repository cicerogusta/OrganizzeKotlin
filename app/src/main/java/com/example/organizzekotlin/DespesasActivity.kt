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

    private lateinit var campoData: EditText
    private lateinit var campoCategoria: EditText
    private lateinit var campoDescricao: EditText
    private lateinit var campoValor: EditText
    private lateinit var movimentacao: Movimentacao
    var despesa = 0.0
    lateinit var binding: ActivityDespesasBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDespesasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        campoData = binding.editDataDespesa
        campoCategoria = binding.editCategoriaDespesa
        campoDescricao = binding.editDescricaoDespesa
        campoValor = binding.editValorDespesa


        campoData.setText(DateCustom.dataAtual())
        recuperarDespesaTotal()

        binding.fabSalvarDespesa.setOnClickListener { if (validarCamposDespesa()) salvarDespesa() }


    }

    fun salvarDespesa() {

        val data: String = campoData.text.toString()
        val valorDespesa = campoValor.text.toString().toDouble()
        movimentacao = Movimentacao()
        movimentacao.valor = valorDespesa
        movimentacao.categoria = campoCategoria.text.toString()
        movimentacao.descricao = campoDescricao.text.toString()
        movimentacao.data = data
        movimentacao.tipo = "d"

        val despesaAtualizada: Double = despesa + valorDespesa
        atualizarDespesa(despesaAtualizada)

        movimentacao.salvar(data)
        finish()

    }

    fun validarCamposDespesa(): Boolean {
        val textoValor = campoValor.text.toString()
        val textoData: String = campoData.getText().toString()
        val textoCategoria: String = campoCategoria.text.toString()
        val textoDescricao: String = campoDescricao.text.toString()

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

                return true
            }
        }
        return false
    }

    fun recuperarDespesaTotal() {
        val emailUsuario: String = recuperarEmail()
        val idUsuario = Base64Custom.codificarBase64(emailUsuario)
        val usuarioRef: DatabaseReference = firebaseConnection().child("movimentacao").child(idUsuario)
        usuarioRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val movimentacaoFirebase: Movimentacao? = dataSnapshot.getValue(Movimentacao::class.java)
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
        val usuarioRef: DatabaseReference =
            firebaseConnection().child("movimentacao").child(idUsuario)
        usuarioRef.child("despesa").setValue(despesa)
    }


}
























