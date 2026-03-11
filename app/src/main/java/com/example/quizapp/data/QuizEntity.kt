package com.example.quizapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Meme (
    @PrimaryKey(true) val id: Int = 0,
    @ColumnInfo(name = "URI") var uri: String,
    var label: Int,
    var description: Int,
    var attempts : Int? = 0,
    var correctAnswers: Int? = 0,
    var selectedAnswer: Int? = 0
    )

