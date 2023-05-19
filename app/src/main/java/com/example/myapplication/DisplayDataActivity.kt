package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class DisplayDataActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_data)

        // Obtém a referência para o TextView no layout
        val nameTextView: TextView = findViewById(R.id.nameTextView)

        // Obtém o nome enviado através do Intent da MainActivity
        val name = intent.getStringExtra("name")

        // Define o texto do TextView como o nome recebido
        nameTextView.text = name
    }
}