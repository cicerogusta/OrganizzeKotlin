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


}
























