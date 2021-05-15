package com.example.uke_3_4_oppgave

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class UserObject(
    @PrimaryKey
    val userId: String,
    val userName: String,
    val firstName: String
)