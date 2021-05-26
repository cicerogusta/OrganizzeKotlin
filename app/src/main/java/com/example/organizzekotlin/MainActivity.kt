package com.example.organizzekotlin

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.example.organizzekotlin.firebase.FirebaseHelper
import com.google.firebase.auth.FirebaseAuth
import com.heinrichreimersoftware.materialintro.app.IntroActivity
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide

class MainActivity : IntroActivity() {
    private lateinit var autenticacao: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      //  setContentView(R.layout.activity_main)

        addSlide(FragmentSlide.Builder()
            .background(android.R.color.white)
            .fragment(R.layout.intro_1)
            .build())

        addSlide(FragmentSlide.Builder()
            .background(android.R.color.white)
            .fragment(R.layout.intro_2)
            .build())

        addSlide(FragmentSlide.Builder()
            .background(android.R.color.white)
            .fragment(R.layout.intro_3)
            .build())

        addSlide(FragmentSlide.Builder()
            .background(android.R.color.white)
            .fragment(R.layout.intro_4)
            .build())

        addSlide(FragmentSlide.Builder()
            .background(android.R.color.white)
            .fragment(R.layout.intro_cadastro)
            .build())


    }

    override fun onStart() {
        super.onStart()
        verificarUsuarioLogado()
    }

    fun abrirTelaEntrar(view: View){
        startActivity(Intent(this, LoginActivity::class.java))
    }

    fun abrirTelaCadastro(view: View){
        startActivity(Intent(this, CadastroActivity::class.java))
    }

    fun verificarUsuarioLogado() {
        autenticacao = FirebaseHelper.firebaseAuth()
        //autenticacao.signOut();
        if (autenticacao.currentUser != null) {
            abrirTelaPrincipal()
        }
    }

    fun abrirTelaPrincipal(){
        startActivity(Intent(this, PrincipalActivity::class.java))
    }


}