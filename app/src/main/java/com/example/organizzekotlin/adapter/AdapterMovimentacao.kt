package com.example.organizzekotlin.adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.organizzekotlin.R
import com.example.organizzekotlin.databinding.RowMovimentacaoBinding
import com.example.organizzekotlin.firebase.FirebaseHelper
import com.example.organizzekotlin.model.Movimentacao

class AdapterMovimentacao(
    private var movimentacoes: MutableList<Movimentacao>,
    var context: Context
) :
    RecyclerView.Adapter<AdapterMovimentacao.MyViewHolder?>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemLista = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_movimentacao, parent, false)
        return MyViewHolder(itemLista)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val movimentacao = movimentacoes[position]
        holder.binding.movimentacao = movimentacao

        holder.binding.value.setTextColor(context.resources.getColor(R.color.colorAccentReceita, null))
        if (movimentacao.tipo == "d") {
            holder.binding.value.setTextColor(context.resources.getColor(R.color.colorAccent, null))
            holder.binding.value.text = "-" + movimentacao.valor
            holder.binding.value.setTextColor(context.resources.getColor(R.color.colorAccent, null))
        }
        holder.binding.deleteBtn.setOnClickListener {
            val alertDialog: AlertDialog.Builder = AlertDialog.Builder(context)

            //Configura AlertDialog
            alertDialog.setTitle("Excluir Movimentação da Conta")
            alertDialog.setMessage("Você tem certeza que deseja realmente excluir essa movimentação de sua conta?")
            alertDialog.setCancelable(false)
            alertDialog.setPositiveButton(
                "Confirmar"
            ) { _, _ ->


                FirebaseHelper.remover(movimentacao.key)


            }
            alertDialog.setNegativeButton(
                "Cancelar"
            ) { dialog, _ ->

                dialog.dismiss()
            }
            alertDialog.create()?.show()
        }


    }

    override fun getItemCount(): Int {
        return movimentacoes.size
    }

    inner class MyViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val binding: RowMovimentacaoBinding by lazy { RowMovimentacaoBinding.bind(itemView) }
    }

}
