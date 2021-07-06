package com.example.organizzekotlin.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.organizzekotlin.R
import com.example.organizzekotlin.model.Movimentacao

class AdapterMovimentacao(
    var movimentacoes: List<Movimentacao>,
    var context: Context
) :
    RecyclerView.Adapter<AdapterMovimentacao.MyViewHolder?>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemLista = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_movimentacao, parent, false)
        return MyViewHolder(itemLista)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val movimentacao = movimentacoes[position]
        holder.titulo.text = movimentacao.descricao
        holder.valor.text = movimentacao.valor.toString()
        holder.categoria.text = movimentacao.categoria
        holder.valor.setTextColor(context.resources.getColor(R.color.colorAccentReceita))
        if (movimentacao.tipo == "d") {
            holder.valor.setTextColor(context.resources.getColor(R.color.colorAccent))
            holder.valor.text = "-" + movimentacao.valor
        }
    }

    override fun getItemCount(): Int {
        return movimentacoes.size
    }

    inner class MyViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var titulo: TextView = itemView.findViewById(R.id.textAdapterTitulo)
        var valor: TextView = itemView.findViewById(R.id.textAdapterValor)
        var categoria: TextView = itemView.findViewById(R.id.textAdapterCategoria)

    }

}
