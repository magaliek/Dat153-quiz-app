package com.example.quizapp

import android.net.Uri
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.quizapp.data.MemeDao
import kotlinx.coroutines.runBlocking
import android.app.Instrumentation
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GalleryActivityTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<Gallery>()
    private lateinit var dao: MemeDao

    @Before
    fun setUp() {
        dao = (composeTestRule.activity.applicationContext as MemeData).memeDao
        Intents.init()
    }

    @After
    fun release() {
        Intents.release()
    }

    @Test
    fun deletingMeme_decreasesCount() {
        val countBefore = runBlocking { dao.getCount() }

        composeTestRule.onAllNodesWithContentDescription("Remove this meme")[0].performClick()

        composeTestRule.waitForIdle()

        val countAfter = runBlocking { dao.getCount() }

        assert(countAfter == countBefore - 1)
    }

    @Test
    fun addingMeme_incrementCount() {
        val fakeIntent = android.content.Intent().apply {
            data = Uri.parse("android.resource://com.example.quizapp/${R.drawable.alex_pork}")
        }
        val result = Instrumentation.ActivityResult(android.app.Activity.RESULT_OK, fakeIntent)
        intending(hasAction("android.provider.action.PICK_IMAGES")).respondWith(result)

        val countBefore = runBlocking { dao.getCount() }

        composeTestRule.onAllNodesWithContentDescription("Add")[0].performClick()

        composeTestRule.waitForIdle()

        val countAfter = runBlocking { dao.getCount() }

        assert(countAfter == countBefore + 1)
    }
}