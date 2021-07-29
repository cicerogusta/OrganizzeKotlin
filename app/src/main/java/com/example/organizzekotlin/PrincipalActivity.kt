package com.example.organizzekotlin


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.organizzekotlin.adapter.AdapterMovimentacao
import com.example.organizzekotlin.databinding.ActivityPrincipalBinding
import com.example.organizzekotlin.firebase.FirebaseHelper
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
import java.util.*

class PrincipalActivity : AppCompatActivity() {
    private var calendarView: MaterialCalendarView? = null
    private var textoSaudacao: TextView? = null
    private var textoSaldo: TextView? = null
    private var despesaTotal = 0.0
    private var receitaTotal = 0.0
    private var resumoUsuario = 0.0
    private val autenticacao: FirebaseAuth = FirebaseHelper.firebaseAuth()
    private val firebaseRef: DatabaseReference = FirebaseHelper.firebaseConnection()
    private var usuarioRef: DatabaseReference? = null
    private var valueEventListenerUsuario: ValueEventListener? = null
    private var valueEventListenerMovimentacoes: ValueEventListener? = null
    private lateinit var recyclerView: RecyclerView
    private var movimentacao: Movimentacao = Movimentacao()
    private var movimentacaoRef: DatabaseReference? = null
    private var mesAnoSelecionado: String? = null
    lateinit var binding: ActivityPrincipalBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrincipalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        textoSaldo = binding.textSaldo
        textoSaudacao = binding.textSaudacao
        calendarView = binding.calendarView
        recyclerView = binding.recyclerMovimentos
        configuraCalendarView()


        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        binding.menuDespesa.setOnClickListener { adicionarDespesa() }
        binding.menuReceita.setOnClickListener { adicionarReceita() }
    }


    fun recuperarMovimentacoes() {
        val emailUsuario = FirebaseHelper.recuperarEmail()
        val idUsuario = Base64Custom.codificarBase64(emailUsuario)
        movimentacaoRef = firebaseRef.child("movimentacao")
            .child(idUsuario)
            .child(mesAnoSelecionado!!)
        valueEventListenerMovimentacoes =
            movimentacaoRef!!.addValueEventListener(object : ValueEventListener {
                @SuppressLint("RestrictedApi")
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val listMovimentacao = mutableListOf<Movimentacao>()
                    var despesaTotal = 0.0
                    var receitaTotal = 0.0

                    for (movimentacaoSnapshot in dataSnapshot.children) {
                        val movimentacao = movimentacaoSnapshot.getValue(Movimentacao::class.java)
                        if (movimentacao != null) {
                            if (movimentacao.tipo == "d") {
                                despesaTotal += movimentacao.valor

                            }
                            if (movimentacao.tipo == "r") {
                                receitaTotal += movimentacao.valor
                            }
                            movimentacao.key = movimentacaoSnapshot.ref.path.toString()
                            listMovimentacao.add(movimentacao)



                        }
                    }
                    atualizarReceita(despesaTotal)

                    binding.recyclerMovimentos.adapter =
                        AdapterMovimentacao(listMovimentacao, this@PrincipalActivity)
                }


                override fun onCancelled(databaseError: DatabaseError) {}
            })
    }

    fun recuperarResumo() {
        val emailUsuario = autenticacao.currentUser!!.email
        val idUsuario = Base64Custom.codificarBase64(emailUsuario!!)
        usuarioRef = firebaseRef.child("usuarios").child(idUsuario)
        valueEventListenerUsuario = usuarioRef!!.addValueEventListener(object : ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val usuario = dataSnapshot.getValue(Usuario::class.java)
                if (usuario != null) {
                    despesaTotal = usuario.despesaTotal
                    textoSaudacao!!.text = "Olá, " + usuario.nome
                    receitaTotal = usuario.receitaTotal

                }

                resumoUsuario = receitaTotal - despesaTotal

            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    fun atualizarReceita(despesaTotal: Double) {
        val decimalFormat = DecimalFormat("0.##")
        val resultadoFormatado = decimalFormat.format(receitaTotal - despesaTotal)

        textoSaldo!!.text = "R$ $resultadoFormatado"
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

    fun adicionarDespesa() {
        startActivity(Intent(this, DespesasActivity::class.java))
    }

    fun adicionarReceita() {
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
        calendarView!!.setTitleMonths(meses)
        val dataAtual = calendarView!!.currentDate
        val mesSelecionado = String.format("%02d", dataAtual.month + 1)
        mesAnoSelecionado = mesSelecionado + "" + dataAtual.year
        calendarView!!.setOnMonthChangedListener { widget, date ->
            val mesSelecionado = String.format("%02d", date.month + 1)
            mesAnoSelecionado = mesSelecionado + "" + date.year
            movimentacaoRef!!.removeEventListener(valueEventListenerMovimentacoes!!)
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
        usuarioRef!!.removeEventListener(valueEventListenerUsuario!!)
        movimentacaoRef!!.removeEventListener(valueEventListenerMovimentacoes!!)
    }
}




