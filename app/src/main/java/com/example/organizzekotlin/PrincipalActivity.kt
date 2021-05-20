package com.example.organizzekotlin

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.widget.TextView
import android.widget.Toolbar
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.Callback.makeMovementFlags
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.organizzekotlin.adapter.AdapterMovimentacao
import com.example.organizzekotlin.firebase.FirebaseCustom
import com.example.organizzekotlin.helper.Base64Custom
import com.example.organizzekotlin.model.Movimentacao
import com.example.organizzekotlin.model.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import java.text.DecimalFormat

class PrincipalActivity : AppCompatActivity() {

    private lateinit var calendarView: MaterialCalendarView
    private lateinit var textoSaudacao: TextView
    private lateinit var textoSaldo: TextView
    private var despesaTotal = 0.0
    private var receitaTotal = 0.0
    private var resumoUsuario = 0.0

    private val autenticacao: FirebaseAuth = FirebaseCustom.firebaseAuth()
    private val firebaseRef: DatabaseReference = FirebaseCustom.firebaseConnection()
    private lateinit var usuarioRef: DatabaseReference
    private lateinit var valueEventListenerUsuario: ValueEventListener
    private lateinit var valueEventListenerMovimentacoes: ValueEventListener


    private lateinit var recyclerView: RecyclerView
    private lateinit var adapterMovimentacao: AdapterMovimentacao
    private  var movimentacoes = ArrayList<Movimentacao>()
    private lateinit var movimentacao: Movimentacao
    private var movimentacaoRef: DatabaseReference? = null
    private lateinit var mesAnoSelecionado: String



    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal)

        textoSaldo = findViewById(R.id.textSaldo)
        textoSaudacao = findViewById(R.id.textSaudacao)
        calendarView = findViewById(R.id.calendarView)
        recyclerView = findViewById(R.id.recyclerMovimentos)
        configuraCalendarView()
         swipe()

        //Configurar adapter
        adapterMovimentacao = AdapterMovimentacao(movimentacoes)

        //Configurar RecyclerView
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapterMovimentacao


    }

    fun swipe() {


        var itemTouch = object : ItemTouchHelper.Callback(){
            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {

                val dragFlags: Int = ItemTouchHelper.ACTION_STATE_IDLE
                val swipeFlags: Int = ItemTouchHelper.START  ; ItemTouchHelper.END
                return makeMovementFlags(dragFlags, swipeFlags)
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                TODO("Not yet implemented")
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
               excluirMovimentacao(viewHolder)
            }

        }
    }


    fun excluirMovimentacao(viewHolder: RecyclerView.ViewHolder) {
        val alertDialog = AlertDialog.Builder(this)

        //Configura AlertDialog
        alertDialog.setTitle("Excluir Movimentação da Conta")
        alertDialog.setMessage("Você tem certeza que deseja realmente excluir essa movimentação de sua conta?")
        alertDialog.setCancelable(false)
        alertDialog.setPositiveButton(
            "Confirmar"
        ) { _, _ ->
            val position = viewHolder.adapterPosition
            movimentacao = movimentacoes[position]
            val emailUsuario = autenticacao.currentUser!!.email
            val idUsuario: String = Base64Custom.codificarBase64(emailUsuario.toString())
            movimentacaoRef = firebaseRef.child("movimentacao").child(idUsuario).child(mesAnoSelecionado)

            movimentacaoRef!!.child(movimentacao.key).removeValue()
            adapterMovimentacao.notifyItemRemoved(position)
            atualizarSaldo()

           val alert: AlertDialog = alertDialog.create()
            alert.show()

        }
    }

    fun atualizarSaldo() {
        val emailUsuario = autenticacao.currentUser!!.email
        val idUsuario = Base64Custom.codificarBase64(emailUsuario!!)
        usuarioRef = firebaseRef.child("usuarios").child(idUsuario)
        if (movimentacao.tipo == "r") {
            receitaTotal -= movimentacao.valor
            usuarioRef.child("receitaTotal").setValue(receitaTotal)
        }
        if (movimentacao.tipo.equals("d")) {
            despesaTotal -= movimentacao.valor
            usuarioRef.child("despesaTotal").setValue(despesaTotal)
        }
    }

    fun recuperarMovimentacoes() {
        val emailUsuario = autenticacao.currentUser!!.email
        val idUsuario = Base64Custom.codificarBase64(emailUsuario!!)
        movimentacaoRef = firebaseRef.child("movimentacao")
            .child(idUsuario)
            .child(mesAnoSelecionado)
        valueEventListenerMovimentacoes =
            movimentacaoRef!!.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    movimentacoes.clear()
                    for (dados in dataSnapshot.children) {
                        val movimentacao = dados.getValue(Movimentacao::class.java)
                        if (movimentacao != null) {
                            movimentacao.key = dados.key.toString()
                        }
                        if (movimentacao != null) {
                            movimentacoes.add(movimentacao)
                        }
                    }
                    adapterMovimentacao.notifyDataSetChanged()
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
    }


    fun recuperarResumo() {
        val emailUsuario = autenticacao.currentUser!!.email
        val idUsuario = Base64Custom.codificarBase64(emailUsuario!!)
        usuarioRef = firebaseRef.child("usuarios").child(idUsuario)
        valueEventListenerUsuario = usuarioRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val usuario: Usuario? = dataSnapshot.getValue(Usuario::class.java)
                if (usuario != null) {
                    despesaTotal = usuario.despesaTotal
                }
                if (usuario != null) {
                    receitaTotal = usuario.receitaTotal
                }
                resumoUsuario = receitaTotal - despesaTotal
                val decimalFormat = DecimalFormat("0.##")
                val resultadoFormatado = decimalFormat.format(resumoUsuario)
                if (usuario != null) {
                    textoSaudacao.text = "Olá, " + usuario.nome
                }
                textoSaldo.text = "R$ $resultadoFormatado"
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_principal, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuSair -> {
                autenticacao.signOut()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun adicionarDespesa(view: View?) {
        startActivity(Intent(this, DespesasActivity::class.java))
    }

    fun adicionarReceita(view: View?) {
        startActivity(Intent(this, ReceitasActivity::class.java))
    }

    fun configuraCalendarView() {
        val meses = arrayOf<CharSequence>(
            "Janeiro",
            "Fevereiro",
            "Março",
            "Abril",
            "Maio",
            "Junho",
            "Julho",
            "Agosto",
            "Setembro",
            "Outubro",
            "Novembro",
            "Dezembro"
        )
        calendarView.setTitleMonths(meses)
        val dataAtual = calendarView.currentDate
        val mesSelecionado = String.format("%02d", dataAtual.month + 1)
        mesAnoSelecionado = mesSelecionado + "" + dataAtual.year
        calendarView.setOnMonthChangedListener { widget, date ->
            val mesSelecionado =
                String.format("%02d", date.month + 1)
            mesAnoSelecionado = mesSelecionado + "" + date.year
            movimentacaoRef!!.removeEventListener(valueEventListenerMovimentacoes)
            recuperarMovimentacoes()
        }
    }

    override fun onStart() {
        super.onStart()
        recuperarResumo()
        recuperarMovimentacoes()
    }

    override fun onStop() {
        super.onStop()
        usuarioRef.removeEventListener(valueEventListenerUsuario)
        movimentacaoRef!!.removeEventListener(valueEventListenerMovimentacoes)
    }
}