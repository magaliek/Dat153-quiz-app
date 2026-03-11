package com.example.quizapp.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities= [Meme::class], version = 1)
abstract class QuizDatabase : RoomDatabase(){
    abstract fun memeDao() : MemeDao
}