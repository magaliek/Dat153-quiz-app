package com.example.quizapp

import androidx.activity.viewModels
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.quizapp.viewmodels.QuizViewModel
import com.example.quizapp.viewmodels.QuizViewModelFactory
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class QuizActivityTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<Quiz>()
    private lateinit var viewModel: QuizViewModel

    @Before
    fun setUp() {
        viewModel = composeTestRule.activity.viewModels<QuizViewModel> {
            QuizViewModelFactory((composeTestRule.activity.applicationContext as MemeData).memeDao)
        }.value
    }

    @Test
    fun correctAnswerAndIncrementsScore() {
        val correctAnswer = viewModel.question.correctAnswer
        composeTestRule.onNodeWithText(correctAnswer).performClick()
        assert(viewModel.correctAnswers == 1)
        assert(viewModel.attempts == 1)
    }

    @Test
    fun incorrectAnswerAndIncrementsScore() {
        val incorrectAnswer = viewModel.question.options.first { it != viewModel.question.correctAnswer }
        composeTestRule.onNodeWithText(incorrectAnswer).performClick()
        assert(viewModel.correctAnswers == 0)
        assert(viewModel.attempts == 1)
    }
}