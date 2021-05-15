package com.example.uke_3_4_oppgave.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.uke_3_4_oppgave.R
import com.example.uke_3_4_oppgave.database.AppDatabase
import com.example.uke_3_4_oppgave.login.LoginActivity
import com.example.uke_3_4_oppgave.tabbar.MainActivity

class SplashActivity : AppCompatActivity() {

    private val splashViewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val userDao = AppDatabase.getDatabase(this).userDAO()
        splashViewModel.checkIfUserIsLoggedIn() { isLoggedIn ->

            val activityIntent = if (isLoggedIn) {
                Intent(this, MainActivity::class.java)
            } else {
                Intent(this, LoginActivity::class.java)
            }

            activityIntent.flags = activityIntent.flags or Intent.FLAG_ACTIVITY_NO_HISTORY
            startActivity(activityIntent)
        }

    }
}