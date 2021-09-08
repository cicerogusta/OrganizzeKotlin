package com.example.organizzekotlin

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import com.example.organizzekotlin.databinding.ActivityDespesasBinding
import com.example.organizzekotlin.helper.DateCustom
import com.example.organizzekotlin.model.Movimentacao
import com.example.organizzekotlin.util.MoneyTextWatcher
import com.example.organizzekotlin.util.toCurrency
import java.lang.Exception

class MovimementacaoActivity : AppCompatActivity() {

    lateinit var movimentacao: Movimentacao
    lateinit var binding: ActivityDespesasBinding
    var isDespesa = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDespesasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.editValorDespesa.addTextChangedListener(MoneyTextWatcher(binding.editValorDespesa))
        binding.editValorDespesa.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
        binding.editDataDespesa.setText(DateCustom.dataAtual())


        binding.fabSalvarDespesa.setOnClickListener { if (validarCamposDespesa()) salvarDespesa() }
        val bundle = intent.extras
        if (bundle != null) {
            isDespesa = bundle.getBoolean("isDespesa")
            binding.bg = isDespesa

        }


    }

    fun salvarDespesa() {
        val data: String = binding.editDataDespesa.text.toString()
        val valorDespesa = binding.editValorDespesa.text.toString().toDouble()
        movimentacao = Movimentacao()
        movimentacao.valor = valorDespesa
        movimentacao.categoria = binding.editCategoriaDespesa.text.toString()
        movimentacao.descricao = binding.editDescricaoDespesa.text.toString()
        movimentacao.data = data
        if (isDespesa) {
            movimentacao.tipo = "d"

        } else {
            movimentacao.tipo = "r"

        }

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


}



























