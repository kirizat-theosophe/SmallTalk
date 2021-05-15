package com.example.uke_3_4_oppgave

import android.app.Application

class SmallTalkApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        application = this
    }
    companion object {
        lateinit var application: SmallTalkApplication
    }
}