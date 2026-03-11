package com.example.quizapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.quizapp.data.Meme
import com.example.quizapp.data.MemeDao


data class Question(
    val meme: Meme? = null,
    val options: List<Int> = emptyList(),
    val correctAnswer: Int = 0
)

class QuizViewModelFactory(private val dao : MemeDao) : ViewModelProvider.Factory {
    override fun<T : ViewModel> create(modelClass : Class<T>) : T {
        return QuizViewModel(dao) as T
    }
}

class QuizViewModel(private val dao : MemeDao) : ViewModel() {
    var question by mutableStateOf(Question())
    private set

    suspend fun generateQuestion() {
        val allMemes = dao.getAll()
        if (allMemes.isEmpty()) {
            question = Question()
            return
        }

        val qMemeItem = dao.random()
        val qCorrectAnswer = qMemeItem.label

        val wrongOptions = dao.getAll().filter { it.label != qCorrectAnswer }.distinct().shuffled().take(2).map{it.label}
        val options = (wrongOptions + qCorrectAnswer).shuffled()

        question = Question(qMemeItem, options, qCorrectAnswer)
    }
}