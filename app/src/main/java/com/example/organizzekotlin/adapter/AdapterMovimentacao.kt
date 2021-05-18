package com.example.organizzekotlin.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.organizzekotlin.R
import com.example.organizzekotlin.model.Movimentacao

class AdapterMovimentacao(val movimentacoes: List<Movimentacao>) : RecyclerView.Adapter<AdapterMovimentacao.ViewHolder>() {

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.adapter_movimentacao, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(movimentacoes[position])
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return movimentacoes.size
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(movimentacao: Movimentacao) {



        }
    }

}