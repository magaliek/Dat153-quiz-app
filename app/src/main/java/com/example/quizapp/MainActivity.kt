package com.example.quizapp

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Intent
import kotlin.jvm.java


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val galleryButton = findViewById<Button>(R.id.galleryButton)
        galleryButton.setOnClickListener{
            val intent = Intent(this, Gallery::class.java)
            startActivity(intent)
        }

        val quizButton = findViewById<Button>(R.id.quizButton)
        quizButton.setOnClickListener{
            val intent = Intent(this, Quiz::class.java)
            startActivity(intent)
        }
    }
}