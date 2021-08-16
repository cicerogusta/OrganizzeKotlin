package com.example.organizzekotlin


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.organizzekotlin.adapter.AdapterMovimentacao
import com.example.organizzekotlin.databinding.ActivityHomeBinding
import com.example.organizzekotlin.firebase.FirebaseHelper
import com.example.organizzekotlin.helper.Base64Custom
import com.example.organizzekotlin.model.Movimentacao
import com.example.organizzekotlin.model.Usuario
import com.example.organizzekotlin.util.toCurrency
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import java.text.NumberFormat
import java.util.*

class HomeActivity : Activity() {

    private val autenticacao: FirebaseAuth = FirebaseHelper.firebaseAuth()
    private val firebaseRef: DatabaseReference = FirebaseHelper.firebaseConnection()
    private lateinit var usuarioRef: DatabaseReference
    private lateinit var valueEventListenerUsuario: ValueEventListener
    private lateinit var valueEventListenerMovimentacoes: ValueEventListener
    private lateinit var recyclerView: RecyclerView
    private lateinit var movimentacaoRef: DatabaseReference
    private lateinit var mesAnoSelecionado: String
    lateinit var binding: ActivityHomeBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        configuraCalendarView()


        val sh = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE)

        sh.edit().putBoolean("jumpSlides", true).apply()

        recyclerView = binding.recyclerMovimentos
        configuraCalendarView()


        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        binding.menuDespesa.setOnClickListener { adicionarDespesa() }
        binding.menuReceita.setOnClickListener { adicionarReceita() }

        binding.btnExit.setOnClickListener {
            autenticacao.signOut()
            startActivity(Intent(this, SlideActivity::class.java))
            finish()

        }
    }


    fun recuperarMovimentacoes() {
        val emailUsuario = FirebaseHelper.recuperarEmail()
        val idUsuario = Base64Custom.codificarBase64(emailUsuario)
        movimentacaoRef = firebaseRef.child("movimentacao")
            .child(idUsuario)
            .child(mesAnoSelecionado)
        valueEventListenerMovimentacoes = (object : ValueEventListener {
            @SuppressLint("RestrictedApi", "SetTextI18n")
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val listMovimentacao = mutableListOf<Movimentacao>()

                var somatorio = 0.0

                for (movimentacaoSnapshot in dataSnapshot.children) {
                    val movimentacaoFirebase =
                        movimentacaoSnapshot.getValue(Movimentacao::class.java)

                    if (movimentacaoFirebase?.tipo == "r") {

                        somatorio += movimentacaoFirebase.valor
                    } else if (movimentacaoFirebase?.tipo == "d") {

                        somatorio -= movimentacaoFirebase.valor
                    }


                    if (movimentacaoFirebase != null) {
                        movimentacaoFirebase.key = movimentacaoSnapshot.ref.path.toString()
                        listMovimentacao.add(movimentacaoFirebase)


                    }


                }

                binding.saldo = somatorio.toCurrency()
                changeColorBackground(somatorio)


                binding.recyclerMovimentos.adapter =
                    AdapterMovimentacao(listMovimentacao, this@HomeActivity)


            }


            override fun onCancelled(databaseError: DatabaseError) {}
        })
        movimentacaoRef.addValueEventListener(valueEventListenerMovimentacoes)
    }

    fun changeColorBackground(somatorio: Double) {
        binding.bg = somatorio > 0
    }


    fun recuperaDadosUsuario() {
        val emailUsuario = autenticacao.currentUser!!.email
        val idUsuario = Base64Custom.codificarBase64(emailUsuario!!)
        usuarioRef = firebaseRef.child("usuarios").child(idUsuario)
        valueEventListenerUsuario = usuarioRef.addValueEventListener(object : ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val usuario = dataSnapshot.getValue(Usuario::class.java)
                if (usuario != null) {
                    binding.nome = "Olá, " + usuario.nome


                } else {
                    binding.nome = "Carregando..."
                }


            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })


    }


    fun adicionarDespesa() {
        val intent = (Intent(this, MovimementacaoActivity::class.java))
        val bundle = Bundle()
        bundle.putBoolean("isDespesa", true)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    fun adicionarReceita() {
        val intent = (Intent(this, MovimementacaoActivity::class.java))
        val bundle = Bundle()
        bundle.putBoolean("isDespesa", false)
        intent.putExtras(bundle)
        startActivity(intent)
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
        binding.calendarView.setTitleMonths(meses)
        var mesSelecionado = String.format("%02d", binding.calendarView.currentDate.month + 1)
        mesAnoSelecionado = mesSelecionado + "" + binding.calendarView.currentDate.year
        binding.calendarView.setOnMonthChangedListener { widget, date ->
            mesSelecionado = String.format("%02d", date.month + 1)
            mesAnoSelecionado = mesSelecionado + "" + date.year
            movimentacaoRef.removeEventListener(valueEventListenerMovimentacoes)
            recuperarMovimentacoes()
        }
    }

    override fun onStart() {
        super.onStart()
        recuperarMovimentacoes()
        recuperaDadosUsuario()
    }

    override fun onStop() {
        super.onStop()
        usuarioRef.removeEventListener(valueEventListenerUsuario)
        movimentacaoRef.removeEventListener(valueEventListenerMovimentacoes)
    }


}






