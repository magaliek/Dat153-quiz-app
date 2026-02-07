package com.example.quizapp

import android.net.Uri
import android.os.Bundle
import coil.compose.rememberAsyncImagePainter
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlin.let


class Gallery : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GalleryScreen()
        }
    }

    data class MemeItem(
        val image: Int,
        val label: Int,
        val description: Int,
        var uri: Uri? = null
    )

    @Composable
    fun MemeCard(meme: MemeItem, onImageClick: () -> Unit) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val painter = if (meme.uri != null) {
                rememberAsyncImagePainter(meme.uri)
            } else {
                painterResource(id = meme.image)
            }
            Image(
                painter = painter,
                contentDescription = stringResource(id = meme.description),
                modifier = Modifier
                    .height(250.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onImageClick() },
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(8.dp))

            var text by remember { mutableStateOf("") }
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                label = { Text(stringResource(meme.label)) },
                placeholder = { Text("Add a label") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = false,
                maxLines = 3,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done,
                )
            )
        }
    }

    @Composable
    fun GalleryScreen() {
        val memes = remember {
            mutableStateListOf(
                MemeItem(R.drawable.alex_pork, R.string.pig_face, R.string.desc_pig_face),
                MemeItem(R.drawable.baby_laugh_ai, R.string.ai_baby, R.string.desc_ai_baby),
                MemeItem(
                    R.drawable.freddy_fazbear,
                    R.string.freddy_fazbear,
                    R.string.desc_freddy_fazbear
                ),
                MemeItem(
                    R.drawable.goated_with_the_sauce,
                    R.string.goat_sauce,
                    R.string.desc_goat_sauce
                ),
                MemeItem(R.drawable.griddy, R.string.griddy_dance, R.string.desc_griddy_dance),
                MemeItem(R.drawable.hawk_tuah, R.string.hawk_tuah, R.string.desc_hawk_tuah),
                MemeItem(
                    R.drawable.i_would_never_order_a_whole_pizza_to_myself,
                    R.string.pizza_man,
                    R.string.desc_pizza_man
                ),
                MemeItem(
                    R.drawable.kiki_do_you_love_me,
                    R.string.drake_sunset,
                    R.string.desc_drake_sunset
                ),
                MemeItem(
                    R.drawable.lightskin_stare,
                    R.string.lightskin_stare,
                    R.string.desc_lightskin_stare
                ),
                MemeItem(
                    R.drawable.looksmaxxing,
                    R.string.looksmaxxing,
                    R.string.desc_looksmaxxing
                ),
                MemeItem(R.drawable.sigma_face, R.string.sigma_face, R.string.desc_sigma_face),
                MemeItem(
                    R.drawable.skibidi_toilet,
                    R.string.skibidi_boss,
                    R.string.desc_skibidi_boss
                ),
                MemeItem(
                    R.drawable.t_pose_for_dominance,
                    R.string.mario_tpose,
                    R.string.desc_mario_tpose
                ),
                MemeItem(R.drawable.uwu, R.string.uwu_emoji, R.string.desc_uwu_emoji),
            )
        }

        var clickedIndex by remember { mutableStateOf(-1) }

        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia()
        ) { uri ->
            uri?.let {
                if (clickedIndex != -1) {
                    memes[clickedIndex] = memes[clickedIndex].copy(uri = it)
                }
            }
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.padding(top = 32.dp),
            contentPadding = PaddingValues(4.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(memes.size) { index ->
                val item = memes[index]
                MemeCard(
                    meme = item,
                    onImageClick = {
                        clickedIndex = index
                        launcher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
                )
            }
        }

    }
}