package com.example.organizzekotlin.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.example.organizzekotlin.R
import com.example.organizzekotlin.model.Movimentacao

internal class AdapterMovimentacao(private var moviesList: List<Movimentacao>) :
    RecyclerView.Adapter<AdapterMovimentacao.MyViewHolder>() {
    internal inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var titulo: TextView = view.findViewById(R.id.textAdapterTitulo)
        var categoria: TextView = view.findViewById(R.id.textAdapterCategoria)
        var valor: TextView = view.findViewById(R.id.textAdapterValor)
    }
    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_movimentacao, parent, false)
        return MyViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val movimentacao = moviesList[position]
        holder.titulo.text = movimentacao.tipo
        holder.categoria.text = movimentacao.categoria
        holder.valor.text = movimentacao.valor.toString()
    }
    override fun getItemCount(): Int {
        return moviesList.size
    }
}