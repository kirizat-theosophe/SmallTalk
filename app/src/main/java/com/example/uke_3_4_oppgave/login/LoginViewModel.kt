package com.example.uke_3_4_oppgave.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uke_3_4_oppgave.UserObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    fun correctCredentials(username: String, password: String): Boolean {
        return username == "kiriza" && password == "xcv234"
    }

    val loginSuccess = MutableLiveData<Boolean>()
    //val loginError = MutableLiveData<Boolean>()
    val isLoading = MutableLiveData<Boolean>()

   private val userRepository = UserRepository()

    fun logInUser(
            username: String,
            password: String
         ) {
        isLoading.postValue(true)

        userRepository.logInUser(
                username,
                password
        ){ user ->
            if (user != null) {
                saveUser(user)
            }else {
                loginSuccess.postValue(false)
                isLoading.postValue(false)
            }
        }
    }

   private fun saveUser(user: UserObject) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.saveUser(user)

                loginSuccess.postValue(true)
                //isLoading.postValue(false)
        }
    }
}