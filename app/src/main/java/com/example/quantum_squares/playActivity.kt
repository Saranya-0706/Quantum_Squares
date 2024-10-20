package com.example.quantum_squares

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class playActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_play)

        val playBtn = findViewById<Button>(R.id.playBtn)

        playBtn.setOnClickListener {
            val i= Intent(this,GameActivity::class.java)
            startActivity(i)
            finish()
        }
    }
}