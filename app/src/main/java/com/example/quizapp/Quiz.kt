package com.example.quizapp

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.quizapp.viewmodels.QuizViewModel
import com.example.quizapp.viewmodels.QuizViewModelFactory
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


    @Composable
    fun QuizScreen() {
        val context = LocalContext.current
        val data = context.applicationContext as MemeData
        val viewModel : QuizViewModel by viewModels {
            QuizViewModelFactory(data.memeDao)
        }

        val scope = rememberCoroutineScope()

        var selectedAnswer by remember {mutableStateOf<String?>(null)}

        var showTitle by remember { mutableStateOf(true) }

        LaunchedEffect(Unit) {
            viewModel.generateQuestion()
        }
        var question = viewModel.question
        var isEmpty = false

        if (question.meme == null) {
            isEmpty = true
        }

        if (!isEmpty) {
            val painter = rememberAsyncImagePainter(question.meme!!.uri)
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
                    text = stringResource(R.string.score_display, viewModel.correctAnswers, viewModel.attempts),
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

                    Button(
                        onClick = {
                            if (showTitle) showTitle = false

                            viewModel.attempts++
                            if(isCorrectChoice) viewModel.correctAnswers++
                            selectedAnswer = option

                            scope.launch {
                                delay(1000L)
                                viewModel.generateQuestion()
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
                        Text(text = option)
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .background(Color.Black),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = stringResource(R.string.empty),
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White,
                )
            }
        }
    }
}