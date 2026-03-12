package com.example.quizapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Meme (
    @PrimaryKey(true) val id: Int = 0,
    @ColumnInfo(name = "URI") var uri: String,
    val label: String,
    var description: Int,
    var stringInputLabel: String? = null
    )

