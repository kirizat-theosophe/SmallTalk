package com.example.uke_3_4_oppgave.login

import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.uke_3_4_oppgave.BASE_URL
import com.example.uke_3_4_oppgave.database.AppDatabase
import com.example.uke_3_4_oppgave.UserObject
import com.example.uke_3_4_oppgave.SmallTalkApplication
import com.example.uke_3_4_oppgave.UserManager
import com.google.gson.Gson

class UserRepository {

   //private val baseUrl = "https://us-central1-smalltalk-3bfb8.cloudfunctions.net/api/"
    private val userDao =
            AppDatabase.getDatabase(SmallTalkApplication.application.applicationContext).userDAO()

    fun logInUser(username: String, password: String, callback: (UserObject?) -> Unit) {
        //var url = "https://us-central1-smalltalk-3bfb8.cloudfunctions.net/api/login"
        val url = "${BASE_URL}login?userName=$username&password=$password"

        val requestQueue =
                Volley.newRequestQueue(SmallTalkApplication.application.applicationContext)

        val stringRequest = StringRequest(
                Request.Method.GET,
                url,
                { jsonResponse ->
                    val user = Gson().fromJson(jsonResponse, UserObject::class.java)
                    callback(user)
                },
                {
                    callback(null)
                }
        )
        requestQueue.add(stringRequest)
    }

    fun saveUser(user: UserObject) {
        userDao.insertUser(user)
        UserManager.loggedInUser = user
    }

    fun getUser(): UserObject? {
        var user: UserObject? = null

        try {
            user = userDao.getUser()

        }catch (e: Exception) {

        }
        if (user != null) {
            UserManager.loggedInUser = user
        }
       return user
    }
}