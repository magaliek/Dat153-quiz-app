package com.example.quizapp

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Quiz : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QuizScreen()
        }
    }

    /**
     * The Quiz Activity where users test their knowledge of the memes.
     * * Displays a random meme image and generates multiple-choice buttons.
     * It accounts for custom labels set in the Gallery to ensure the quiz
     * stays updated with user changes.
     */
    @Composable
    fun QuizScreen() {
        val context = LocalContext.current
        val data = context.applicationContext as MemeData
        var isEmpty = false

        if (data.allMemes.isEmpty()) {
            isEmpty = true
        }

        var attempts by remember { mutableIntStateOf(0) }
        var correctAnswers by remember { mutableIntStateOf(0) }
        var selectedAnswer by remember {mutableStateOf<Int?>(null)}
        val scope = rememberCoroutineScope()

        var showTitle by remember { mutableStateOf(true) }


        if (!isEmpty) {
            var question by remember {
                mutableStateOf(generateQuestion(data.allMemes))
            }

            val painter = if (question.meme.uri != null) {
                rememberAsyncImagePainter(question.meme.uri)
            } else {
                painterResource(id = question.meme.image)
            }

            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxSize()
                    .statusBarsPadding()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (showTitle) Text(
                    text = stringResource(R.string.quiz_welcome),
                    style = MaterialTheme.typography.headlineSmall

                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(R.string.score_display, correctAnswers, attempts),
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.height(24.dp))

                val memeCorrect = question.correctAnswer
                Image(
                    painter = painter,
                    contentDescription = stringResource(id = question.meme.description),
                    modifier = Modifier
                        .height(300.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Fit
                )

                Spacer(modifier = Modifier.height(24.dp))

                question.options.forEach { option ->
                    val isCorrectChoice = option == memeCorrect

                    val optionItem = data.allMemes.find { it.label == option }
                    val buttonText = optionItem?.customLabel ?: stringResource(id = option)
                    Button(
                        onClick = {
                            if (showTitle) showTitle = false
                            if (selectedAnswer == null) {
                                selectedAnswer = option
                                if (option == memeCorrect) {
                                    correctAnswers++
                                }
                                attempts++
                            }
                            scope.launch {
                                delay(1000L)
                                question = generateQuestion(data.allMemes)
                                selectedAnswer = null
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = when {
                                selectedAnswer == option && isCorrectChoice -> Color(0xFF4CAF50)
                                selectedAnswer == option && !isCorrectChoice -> Color.Red
                                else -> MaterialTheme.colorScheme.primary
                            }
                        )
                    ) {
                        Text(text = buttonText)
                    }
                }
            }
        }
    }
}

/**
 * Logic to generate a new quiz question.
 * * Selects a random "correct" meme and pulls two other "wrong" memes
 * to create a set of three unique options.
 * * @param memes The master list of available [MemeItem]s.
 * @return A [Question] object containing the target meme and shuffled options.
 */
fun generateQuestion(memes: List<MemeItem>): Question {
    val qMemeItem = memes.random()
    val qCorrectAnswer = qMemeItem.label

    val wrongOptions = memes.filter { it.label != qCorrectAnswer }.distinct().shuffled().take(2).map{it.label}
    val options = (wrongOptions + qCorrectAnswer).shuffled()

    return Question(qMemeItem, options, qCorrectAnswer)
}

data class Question(val meme: MemeItem, val options: List<Int>, val correctAnswer: Int)