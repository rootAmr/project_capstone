package com.c241.ps341.fomo.ui.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.c241.ps341.fomo.databinding.ActivitySplashBinding
import com.c241.ps341.fomo.ui.model.MainViewModel
import com.c241.ps341.fomo.ui.model.ViewModelFactory
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this, ViewModelFactory(this))[MainViewModel::class.java]
        setContentView(binding.root)

        window.decorView.postDelayed({
            val auth = Firebase.auth

            if (auth.currentUser != null) {
                generateToken(this, auth, viewModel)
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }

        }, TIME.toLong())
    }

    companion object {
        const val TIME = 2000
    }

    fun generateToken(activity: Activity, auth: FirebaseAuth, viewModel: MainViewModel) {
        auth.currentUser?.getIdToken(true)?.addOnCompleteListener {
            if (it.isSuccessful) {
                val idToken = it.result?.token.toString()
                viewModel.setToken(idToken)
                Log.i("Token", idToken)

                Intent(activity, MainActivity::class.java).also { intent ->
                    activity.startActivity(intent)
                    activity.finish()
                }
            }
        }
    }
}