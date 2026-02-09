package com.example.quizapp

import android.app.Application
import androidx.compose.runtime.mutableStateListOf


class MemeData: Application() {
    val allMemes = mutableStateListOf<MemeItem>()

    override fun onCreate() {
        super.onCreate()
        allMemes.addAll(mutableStateListOf(
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
        )
    }
}