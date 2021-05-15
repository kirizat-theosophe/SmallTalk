package com.example.uke_3_4_oppgave.database

import androidx.room.Database
import androidx.room.RoomDatabase
import android.content.Context
import androidx.room.Room
import com.example.uke_3_4_oppgave.ChatDAO
import com.example.uke_3_4_oppgave.ChatObject
import com.example.uke_3_4_oppgave.UserObject

@Database(entities = [UserObject::class, ChatObject::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDAO(): UserDAO
    abstract fun chatDAO(): ChatDAO

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()

                INSTANCE = instance
                instance
            }
        }
    }
}