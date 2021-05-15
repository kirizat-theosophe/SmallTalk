package com.example.uke_3_4_oppgave.tabbar

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.uke_3_4_oppgave.R
import com.example.uke_3_4_oppgave.UserManager
import com.example.uke_3_4_oppgave.database.AppDatabase
import com.example.uke_3_4_oppgave.login.LoginActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.main_activity)


            val navView: BottomNavigationView = findViewById(R.id.nav_view)

            val navController = findNavController(R.id.nav_host_fragment)
            navView.setupWithNavController(navController)
        }
        fun logOutUser() {
            val context = this
            CoroutineScope(Dispatchers.IO).launch {
                val userDAO = AppDatabase.getDatabase(context).userDAO()
                userDAO.deleteUser(UserManager.loggedInUser)

                val intent = Intent(context, LoginActivity::class.java)
                intent.flags = intent.flags or Intent.FLAG_ACTIVITY_NO_HISTORY
                startActivity(intent)
            }
        }
    }