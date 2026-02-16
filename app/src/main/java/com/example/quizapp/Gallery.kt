package com.example.quizapp

import android.annotation.SuppressLint
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
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text

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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.SortByAlpha
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import kotlinx.coroutines.launch
import kotlin.let

class Gallery : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GalleryScreen()
        }
    }

    /**
     * A UI component that displays a single meme.
     * * Includes an [Image] that triggers a photo picker on click and an
     * [OutlinedTextField] for editing the meme's name. It switches between
     * floating labels and placeholders based on whether the meme is a "classic"
     * template or a user-added image.
     * * @param meme The [MemeItem] data to display.
     * @param onImageClick Callback triggered when the meme image is tapped.
     * @param onTextChange Callback triggered when the user edits the name field.
     */
    @Composable
    fun MemeCard(meme: MemeItem, onImageClick: () -> Unit, onTextChange: (String) -> Unit) {
        val focusManager = LocalFocusManager.current

        Column(
            modifier = Modifier
                .weight(1f)
                .background(Color.grey)
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
                    .padding(horizontal = 8.dp)
                    .height(250.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onImageClick() },
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(8.dp))

            val text = if (meme.uri == null) {
                meme.customLabel ?: stringResource(meme.label)
            } else {
                meme.customLabel ?: ""
            }
            OutlinedTextField(
                value = text,
                onValueChange = {onTextChange(it)},
                label = { Text(text) },
                placeholder = if(meme.uri != null){
                    { Text(stringResource(meme.label)) }
                }else null,
                modifier = Modifier.fillMaxWidth(),
                singleLine = false,
                maxLines = 3,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done,
                ),
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                )
            )
        }
    }

    /**
     * The main screen for the Meme Gallery.
     * Manages the state of the meme list, handles image picking results,
     * and coordinates the grid layout and sorting logic.
     */
    @SuppressLint("LocalContextGetResourceValueCall")
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun GalleryScreen() {
        val context = LocalContext.current
        val data = context.applicationContext as MemeData
        val gridState = rememberLazyGridState()
        val scope = rememberCoroutineScope()

        remember {
            data.allMemes
            true
        }

        var clickedIndex by remember { mutableStateOf(-1) }

        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia()
        ) { uri ->
            uri?.let {
                if (clickedIndex != -1) {
                    data.allMemes[clickedIndex] = data.allMemes[clickedIndex].copy(uri = it)
                }
            }
        }



        val addLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia()
        ) { uri ->
            uri?.let {selectedUri ->
                val newItem = MemeItem(
                    image = 0,
                    label = R.string.new_meme,
                    description = R.string.desc_new_meme,
                    uri = selectedUri
                )
                data.allMemes.add(newItem)
                scope.launch {
                    gridState.animateScrollToItem(data.allMemes.size - 1)
                }
            }
        }

        var isAscending by remember {mutableStateOf(false)}

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(id = R.string.gallery)) },
                    actions = {
                        IconButton(onClick = {
                            addLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                        }) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
                        }

                        IconButton(onClick = {
                            if (isAscending) {
                                data.allMemes.sortBy { memeItem -> context.getString(memeItem.label).lowercase()}
                            } else {
                                data.allMemes.sortByDescending {context.getString(it.label).lowercase()}
                            }
                            isAscending = !isAscending
                        }) {
                            Icon(
                                imageVector = Icons.Default.SortByAlpha,
                                contentDescription = "Sort Alphabetically"
                            )
                        }
                    }
                )
            }
        ) {paddingValues ->
            LazyVerticalGrid(
                state = gridState,
                columns = GridCells.Fixed(2),
                modifier = Modifier.padding(paddingValues),
                contentPadding = PaddingValues(4.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(data.allMemes.size) { index ->
                    val item = data.allMemes[index]
                    MemeCard(
                        meme = item,
                        onImageClick = {
                            clickedIndex = index
                            launcher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                        onTextChange = { newText ->
                            data.allMemes[index] = data.allMemes[index].copy(customLabel = newText)
                        }
                    )
                }
            }
        }

    }
}

/**
 * Represents a single meme entry in the gallery.
 * @property image The drawable resource ID for the default image.
 * @property label The string resource ID for the meme's name.
 * @property description The string resource ID for the meme's description.
 * @property uri A custom image URI if the user replaced the default image.
 * @property customLabel A user-defined string to override the default name.
 */
data class MemeItem(
    val image: Int,
    val label: Int,
    val description: Int,
    var uri: Uri? = null,
    var customLabel: String? = null
)