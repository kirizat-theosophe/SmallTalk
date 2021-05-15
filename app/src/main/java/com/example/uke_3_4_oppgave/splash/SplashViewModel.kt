package com.example.uke_3_4_oppgave.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uke_3_4_oppgave.login.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SplashViewModel : ViewModel() {

    private val brukerRepository = UserRepository()

    fun checkIfUserIsLoggedIn(callback: (Boolean) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val user = brukerRepository.getUser()

            callback(user != null)
        }
    }

}