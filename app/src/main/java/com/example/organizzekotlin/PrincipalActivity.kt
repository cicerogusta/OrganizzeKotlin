package com.example.organizzekotlin

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.organizzekotlin.adapter.AdapterMovimentacao
import com.example.organizzekotlin.firebase.FirebaseCustom
import com.example.organizzekotlin.model.Movimentacao
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.prolificinteractive.materialcalendarview.MaterialCalendarView

class PrincipalActivity : AppCompatActivity() {

    private lateinit var calendarView: MaterialCalendarView
    private lateinit var textoSaudacao: TextView
    private lateinit var textoSaldo: TextView
    private val despesaTotal = 0.0
    private val receitaTotal = 0.0
    private val resumoUsuario = 0.0

    private val autenticacao: FirebaseAuth = FirebaseCustom.firebaseAuth()
    private val firebaseRef: DatabaseReference = FirebaseCustom.firebaseConnection()
    private lateinit var usuarioRef: DatabaseReference
    private lateinit var valueEventListenerUsuario: ValueEventListener
    private lateinit var valueEventListenerMovimentacoes: ValueEventListener

    private lateinit var recyclerView: RecyclerView
    private lateinit var  adapterMovimentacao: AdapterMovimentacao
    private lateinit var movimentacoes: List<Movimentacao>
    private lateinit var  movimentacao: Movimentacao
    private lateinit var  movimentacaoRef: DatabaseReference
    private lateinit var  mesAnoSelecionado: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal)

        val toolbar:androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        toolbar.title = "Organizze"
        setSupportActionBar(toolbar)

        textoSaldo = findViewById(R.id.textSaldo)
        textoSaudacao = findViewById(R.id.textSaudacao)
        calendarView = findViewById(R.id.calendarView)
        recyclerView = findViewById(R.id.recyclerMovimentos)
       //configuraCalendarView()
        //swipe()

        //Configurar adapter

        //Configurar adapter
        adapterMovimentacao = AdapterMovimentacao(emptyList<Movimentacao>())

        //Configurar RecyclerView

        //Configurar RecyclerView
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        recyclerView.setAdapter(adapterMovimentacao)

    }
}