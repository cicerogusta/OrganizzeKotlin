package com.example.organizzekotlin


import android.annotation.SuppressLint
import android.content.Context
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
    private val autenticacao: FirebaseAuth = FirebaseHelper.firebaseAuth()
    private val firebaseRef: DatabaseReference = FirebaseHelper.firebaseConnection()
    private var usuarioRef: DatabaseReference? = null
    private var valueEventListenerUsuario: ValueEventListener? = null
    private var valueEventListenerMovimentacoes: ValueEventListener? = null
    private lateinit var recyclerView: RecyclerView
    private var movimentacaoRef: DatabaseReference? = null
    private lateinit var mesAnoSelecionado: String
    lateinit var binding: ActivityPrincipalBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrincipalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recuperaDadosUsuario()

        val sh = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE)

        sh.edit().putBoolean("jumpSlides", true).apply()

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


    fun recuperarListaMovimentacoes(mesAnoSelecionado: String) {
        val emailUsuario = FirebaseHelper.recuperarEmail()
        val idUsuario = Base64Custom.codificarBase64(emailUsuario)
        movimentacaoRef = firebaseRef.child("movimentacao")
            .child(idUsuario)
            .child(mesAnoSelecionado)
        valueEventListenerMovimentacoes = (object : ValueEventListener {
            @SuppressLint("RestrictedApi")
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val listMovimentacao = mutableListOf<Movimentacao>()


                for (movimentacaoSnapshot in dataSnapshot.children) {
                    val movimentacaoFirebase =
                        movimentacaoSnapshot.getValue(Movimentacao::class.java)

                    movimentacaoFirebase!!.key = movimentacaoSnapshot.ref.path.toString()

                    listMovimentacao.add(movimentacaoFirebase)

                    binding.movimentacao = movimentacaoFirebase
                }

                binding.recyclerMovimentos.adapter =
                    AdapterMovimentacao(listMovimentacao, this@PrincipalActivity)
            }


            override fun onCancelled(databaseError: DatabaseError) {}
        })
        movimentacaoRef!!.addValueEventListener(valueEventListenerMovimentacoes!!)
    }

    fun recuperaDadosUsuario() {
        val emailUsuario = autenticacao.currentUser!!.email
        val idUsuario = Base64Custom.codificarBase64(emailUsuario!!)
        usuarioRef = firebaseRef.child("usuarios").child(idUsuario)
        valueEventListenerUsuario = usuarioRef!!.addValueEventListener(object : ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val usuario = dataSnapshot.getValue(Usuario::class.java)
                if (usuario != null) {
                    textoSaudacao!!.text = "Olá, " + usuario.nome



                }


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
        var mesSelecionado = String.format("%02d", binding.calendarView.currentDate.month + 1)
        mesAnoSelecionado = mesSelecionado + "" + binding.calendarView.currentDate.year
        calendarView!!.setOnMonthChangedListener { widget, date ->
            mesSelecionado = String.format("%02d", date.month + 1)
            mesAnoSelecionado = mesSelecionado + "" + date.year
            recuperarListaMovimentacoes(mesAnoSelecionado)
        }
    }


}






