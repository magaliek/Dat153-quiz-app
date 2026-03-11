package com.example.quizapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface MemeDao {
    @Query("SELECT * FROM Meme")
    suspend fun getAll(): List<Meme>

    @Query("SELECT * FROM Meme WHERE id IN (:ids)")
    suspend fun getAllById(ids: IntArray) : List<Meme>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg memes : Meme)

    @Delete
    suspend fun delete(meme : Meme)

    @Update
    suspend fun updateMemes(vararg memes : Meme)

    @Query("SELECT COUNT(*) FROM Meme")
    suspend fun getCount() : Int

    @Query("SELECT * FROM Meme ORDER BY RANDOM() LIMIT 1")
    suspend fun random() : Meme
}