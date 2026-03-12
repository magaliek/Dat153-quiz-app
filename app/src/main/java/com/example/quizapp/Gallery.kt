package com.example.quizapp

import android.annotation.SuppressLint
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
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.SortByAlpha
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import com.example.quizapp.data.Meme
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


    @Composable
    fun MemeCard(meme: Meme, onImageClick: () -> Unit, onTextChange: (String) -> Unit, onDelete: () -> Unit) {
        val focusManager = LocalFocusManager.current
        val painter = rememberAsyncImagePainter(meme.uri)

        Box (
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        ) {
            Image(
                painter = painter,
                contentDescription = stringResource(id = meme.description),
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onImageClick() },
                contentScale = ContentScale.Fit
            )

            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = "Remove this meme",
                    modifier = Modifier.align(Alignment.TopEnd).padding(8.dp)
                )
            }
            val text = meme.stringInputLabel ?: meme.label

            OutlinedTextField(
                value = text,
                onValueChange = {onTextChange(it)},
                label = { Text(text) },
                placeholder = run {
                    { Text(meme.label) }
                },
                modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter),
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


    @SuppressLint("LocalContextGetResourceValueCall")
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun GalleryScreen() {
        val context = LocalContext.current
        val data = context.applicationContext as MemeData
        val dao = data.memeDao
        val gridState = rememberLazyGridState()
        val scope = rememberCoroutineScope()

        var clickedIndex by remember { mutableIntStateOf(-1) }
        var count by remember { mutableIntStateOf(0) }
        var memes by remember {mutableStateOf<List<Meme>>(emptyList())}
        val newMemeLabel = stringResource(R.string.new_meme)

        LaunchedEffect(Unit) {
            memes = when (data.isAscending) {
                true -> dao.sortAsc()
                false -> dao.sortDesc()
                null -> dao.getAll()
            }
            count = dao.getCount()
        }

        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia()
        ) { uri ->
            uri?.let {
                if (clickedIndex != -1) {
                    scope.launch {
                        val existing = memes[clickedIndex]
                        existing.let { meme ->
                            val updatedMeme = meme.copy(uri = it.toString())
                            dao.updateMemes(updatedMeme)
                            memes = when (data.isAscending) {
                                true -> dao.sortAsc()
                                false -> dao.sortDesc()
                                null -> dao.getAll()
                            }
                            count = dao.getCount()
                        }
                    }
                }
            }
        }

        val addLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia()
        ) { uri ->
            uri?.let {selectedUri ->
                scope.launch {
                    dao.insertAll(Meme(uri = selectedUri.toString(), label = newMemeLabel, description = R.string.desc_new_meme))
                    memes = when (data.isAscending) {
                        true -> dao.sortAsc()
                        false -> dao.sortDesc()
                        null -> dao.getAll()
                    }
                    count = dao.getCount()
                    gridState.animateScrollToItem(count - 1)
                }
            }
        }


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
                            scope.launch {
                                data.isAscending = !(data.isAscending ?: false)
                                memes = if (data.isAscending == true) dao.sortAsc() else dao.sortDesc()
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Default.SortByAlpha,
                                contentDescription = "Sort Alphabetically"
                            )
                        }
                    }
                )
            }
        ) { paddingValues ->
            LazyVerticalGrid(
                state = gridState,
                columns = GridCells.Fixed(2),
                modifier = Modifier.padding(paddingValues),
                contentPadding = PaddingValues(4.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(count) { index ->
                    val item = memes[index]
                    MemeCard(
                        meme = item,
                        onImageClick = {
                            clickedIndex = index
                            launcher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                        onTextChange = { newText ->
                            val updated = item.copy(stringInputLabel = newText)

                            scope.launch {
                                dao.updateMemes(updated)
                                memes = when (data.isAscending) {
                                    true -> dao.sortAsc()
                                    false -> dao.sortDesc()
                                    null -> dao.getAll()
                                }
                                count = dao.getCount()
                            }
                        },
                        onDelete = {
                            scope.launch {
                                dao.delete(item)
                                memes = when (data.isAscending) {
                                    true -> dao.sortAsc()
                                    false -> dao.sortDesc()
                                    null -> dao.getAll()
                                }
                                count = dao.getCount()
                            }
                        }
                    )
                }
            }
        }

    }
}