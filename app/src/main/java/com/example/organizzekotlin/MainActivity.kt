package com.example.organizzekotlin


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.example.organizzekotlin.firebase.FirebaseHelper.firebaseAuth
import com.google.firebase.auth.FirebaseAuth
import com.heinrichreimersoftware.materialintro.app.IntroActivity
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide

class MainActivity : IntroActivity() {
    private lateinit var autenticacao: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //  setContentView(R.layout.activity_main)


        isButtonBackVisible = false
        isButtonNextVisible = false

        var list = listOf(
            R.layout.intro_1,
            R.layout.intro_2,
            R.layout.intro_3,
            R.layout.intro_4,
            R.layout.intro_cadastro
        )
        val sh = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE)

        val jumpSlides = sh.getBoolean("jumpSlides", false)
        if (jumpSlides) {
            list = listOf(
                R.layout.intro_cadastro
            )
        }

        list.forEach {
            val isLast = list.last() == it
            addSlide(
                FragmentSlide.Builder()
                    .background(android.R.color.white)
                    .fragment(it)
                    .canGoForward(!isLast)
                    .build()
            )


        }


    }


    override fun onStart() {
        super.onStart()
        verificarUsuarioLogado()
    }

    fun verificarUsuarioLogado() {

        autenticacao = firebaseAuth()
        if (autenticacao.currentUser != null) {
            abrirTelaPrincipal()
        }
    }

    fun abrirTelaEntrar(view: View) {
        startActivity(Intent(this, LoginActivity::class.java))
    }

    fun abrirTelaCadastro(view: View) {
        startActivity(Intent(this, CadastroActivity::class.java))
    }

    fun abrirTelaPrincipal() {
        startActivity(Intent(this, PrincipalActivity::class.java))
    }


}