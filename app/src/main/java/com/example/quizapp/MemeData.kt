package com.example.quizapp

import android.app.Application
import androidx.room.Room
import com.example.quizapp.data.Meme
import com.example.quizapp.data.QuizDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MemeData: Application() {
    val db : QuizDatabase by lazy {
        Room.databaseBuilder(
            applicationContext,
            QuizDatabase::class.java,
            "AppDatabase"
        ).build()
    }

    val memeDao by lazy {db.memeDao()}
    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    var isAscending: Boolean? = null

    override fun onCreate() {
        super.onCreate()

        coroutineScope.launch {
            if (memeDao.getAll().isEmpty()) {
                memeDao.insertAll(
                    Meme(uri = "android.resource://${packageName}/drawable/alex_pork",
                        label = getString(R.string.pig_face),
                        description = R.string.desc_pig_face),
                    Meme(
                        uri = "android.resource://${packageName}/drawable/baby_laugh_ai",
                        label = getString(R.string.ai_baby),
                        description = R.string.desc_ai_baby
                    ),
                    Meme(
                        uri = "android.resource://${packageName}/drawable/freddy_fazbear",
                        label = getString(R.string.freddy_fazbear),
                        description = R.string.desc_freddy_fazbear
                    ),
                    Meme(
                        uri = "android.resource://${packageName}/drawable/goated_with_the_sauce",
                        label = getString(R.string.goat_sauce),
                        description = R.string.desc_goat_sauce
                    ),
                    Meme(
                        uri = "android.resource://${packageName}/drawable/griddy",
                        label = getString(R.string.griddy_dance),
                        description = R.string.desc_griddy_dance
                    ),
                    Meme(
                        uri = "android.resource://${packageName}/drawable/hawk_tuah",
                        label = getString(R.string.hawk_tuah),
                        description = R.string.desc_hawk_tuah
                    ),
                    Meme(
                        uri = "android.resource://${packageName}/drawable/i_would_never_order_a_whole_pizza_to_myself",
                        label = getString(R.string.pizza_man),
                        description = R.string.desc_pizza_man
                    ),
                    Meme(
                        uri = "android.resource://${packageName}/drawable/kiki_do_you_love_me",
                        label = getString(R.string.drake_sunset),
                        description = R.string.desc_drake_sunset
                    ),
                    Meme(
                        uri = "android.resource://${packageName}/drawable/lightskin_stare",
                        label = getString(R.string.lightskin_stare),
                        description = R.string.desc_lightskin_stare
                    ),
                    Meme(
                        uri = "android.resource://${packageName}/drawable/looksmaxxing",
                        label = getString(R.string.looksmaxxing),
                        description = R.string.desc_looksmaxxing
                    ),
                    Meme(
                        uri = "android.resource://${packageName}/drawable/sigma_face",
                        label = getString(R.string.sigma_face),
                        description = R.string.desc_sigma_face
                    ),
                    Meme(
                        uri = "android.resource://${packageName}/drawable/skibidi_toilet",
                        label = getString(R.string.skibidi_boss),
                        description = R.string.desc_skibidi_boss
                    ),
                    Meme(
                        uri = "android.resource://${packageName}/drawable/t_pose_for_dominance",
                        label = getString(R.string.mario_tpose),
                        description = R.string.desc_mario_tpose
                    ),
                    Meme(
                        uri = "android.resource://${packageName}/drawable/uwu",
                        label = getString(R.string.uwu_emoji),
                        description = R.string.desc_uwu_emoji
                    )
                )
            }
        }
    }
}