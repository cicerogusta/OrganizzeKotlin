package com.example.organizzekotlin


import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.organizzekotlin.adapter.AdapterMovimentacao
import com.example.organizzekotlin.databinding.ActivityPrincipalBinding
import com.example.organizzekotlin.firebase.FirebaseHelper.firebaseAuth
import com.example.organizzekotlin.firebase.FirebaseHelper.firebaseConnection
import com.example.organizzekotlin.firebase.FirebaseHelper.recuperarEmail
import com.example.organizzekotlin.helper.Base64Custom
import com.example.organizzekotlin.model.Movimentacao
import com.example.organizzekotlin.model.Usuario
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.text.DecimalFormat
import java.util.*

class PrincipalActivity : AppCompatActivity() {

    private var despesaTotal = 0.0
    private var receitaTotal = 0.0
    private var resumoGastos = 0.0
    lateinit var mesAnoSelecionado: String

    private val movimentacao: Movimentacao? = null
    lateinit var binding: ActivityPrincipalBinding
    private lateinit var adapterMovmentacao: AdapterMovimentacao
    private val movimentacoes: List<Movimentacao> =
        ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrincipalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sh = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE)

        sh.edit().putBoolean("jumpSlides", true).apply()


        //Configurar RecyclerView
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        val recyclerView = binding.recyclerMovimentos
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)


        recuperarResumo()
        configuraCalendarView()


        //Configurar adapter
        adapterMovmentacao = AdapterMovimentacao(movimentacoes, this)
        binding.recyclerMovimentos.adapter = adapterMovmentacao


        binding.menuDespesa.setOnClickListener { chamarDespesa() }


    }

//    fun swipe() {
//
//        object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
//            override fun onMove(
//                recyclerView: RecyclerView,
//                viewHolder: RecyclerView.ViewHolder,
//                target: RecyclerView.ViewHolder
//            ): Boolean {
//                TODO("Not yet implemented")
//            }
//
//            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//                TODO("Not yet implemented")
//            }
//
//        }
//    }

    fun atualizarSaldo() {

        val emailUsuario = recuperarEmail().toString()
        val idUsuario = Base64Custom.codificarBase64(emailUsuario)
        firebaseConnection().child("usuarios").child(idUsuario)
        if (movimentacao != null) {
            if (movimentacao.tipo == "r") {
                receitaTotal -= movimentacao.valor
                firebaseConnection().child("receitaTotal").setValue(receitaTotal)
            }
        }
        if (movimentacao != null) {
            if (movimentacao.tipo == "d") {
                despesaTotal -= movimentacao.valor
                firebaseConnection().child("despesaTotal").setValue(despesaTotal)
            }
        }
    }


    fun excluirMovimentacao(viewHolder: RecyclerView.ViewHolder) {


        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(this)

        //Configura AlertDialog
        alertDialog.setTitle("Excluir Movimentação da Conta")
        alertDialog.setMessage("Você tem certeza que deseja realmente excluir essa movimentação de sua conta?")
        alertDialog.setCancelable(false)
        alertDialog.setPositiveButton("Confirmar",
            DialogInterface.OnClickListener { dialog, which ->

                atualizarSaldo()
            })
        alertDialog.setNegativeButton("Cancelar",
            DialogInterface.OnClickListener { dialog, which ->
                Toast.makeText(
                    this@PrincipalActivity,
                    "Cancelado",
                    Toast.LENGTH_SHORT
                ).show()

            })
        alertDialog.create()?.show()
        adapterMovmentacao.notifyDataSetChanged()
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
        var mesSelecionado = String.format("%02d", binding.calendarView.currentDate.month)
        mesAnoSelecionado = mesSelecionado + "" + binding.calendarView.currentDate.year
        binding.calendarView.setOnMonthChangedListener { widget, date ->
            mesSelecionado = String.format("%02d", date.month)
            mesAnoSelecionado = mesSelecionado + "" + date.year
            recuperarMovimentacoes(mesAnoSelecionado)


        }

    }

    fun recuperarMovimentacoes(mesAnoSelecionado: String) {

        val emailUsuario = recuperarEmail()
        val idUsuario = Base64Custom.codificarBase64(emailUsuario.toString())
        val movimentacaoRef = firebaseConnection()
            .child("movimentacao")
            .child(idUsuario)
            .child(mesAnoSelecionado)

        movimentacaoRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val movimentacoes = mutableListOf<Movimentacao>()
                for (dados: DataSnapshot in snapshot.children) {

                    val movimentacao = dados.getValue(Movimentacao::class.java)
                    movimentacao!!.key = dados.key.toString()

                    movimentacoes.add(movimentacao)

                }
                adapterMovmentacao.notifyDataSetChanged()
            }

        })


    }

    fun recuperarResumo() {

        val emailUsuario = recuperarEmail()
        val idUsuario = Base64Custom.codificarBase64(emailUsuario.toString())
        val autenticacao = firebaseConnection().child("usuarios").child(idUsuario)

        autenticacao.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


            @SuppressLint("SetTextI18n")
            override fun onDataChange(snapshot: DataSnapshot) {
                val usuario = snapshot.getValue(Usuario::class.java)

                if (usuario != null) {
                    despesaTotal = usuario.despesaTotal
                    receitaTotal = usuario.receitaTotal
                    resumoGastos = receitaTotal - despesaTotal

                    val decimalFormat = DecimalFormat("0.##").format(resumoGastos)


                    binding.textSaudacao.text = "Olá, " + usuario.nome
                    binding.textSaldo.text = "R$ $decimalFormat"
                }

            }

        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_principal, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuSair -> {
                val autenticacao = firebaseAuth()
                autenticacao.signOut()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }


    fun chamarDespesa() {
        startActivity(Intent(this, DespesasActivity::class.java))
    }


}