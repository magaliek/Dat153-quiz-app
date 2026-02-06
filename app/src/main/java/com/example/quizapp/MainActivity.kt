package com.example.quizapp

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    lateinit var lastImageClicked: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val picker = registerForActivityResult(ActivityResultContracts.GetContent()) {
            uri -> uri?.let {
                lastImageClicked.setImageURI(it)
            }
        }

        val imageIds = listOf(
            R.id.alex,
            R.id.baby,
            R.id.freddy_fazbear,
            R.id.goat,
            R.id.griddy,
            R.id.hawk_tuah,
            R.id.pizza,
            R.id.kiki,
            R.id.lightskin_stare,
            R.id.looksmaxxing,
            R.id.sigma_face,
            R.id.skibidi,
            R.id.t_pose,
            R.id.uwu
        )

        for (imageId in imageIds) {
            val imageView = findViewById<ImageView>(imageId)

            imageView.setOnClickListener {
                picker.launch("image/*")
                lastImageClicked = imageView
            }
        }
    }
}