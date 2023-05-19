package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class AddDataActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_data)

        // Obtém a referência para o botão saveButton e para o EditText nameEditText no layout
        val saveButton: Button = findViewById(R.id.saveButton)
        val nameEditText: EditText = findViewById(R.id.nameEditText)

        // Configura o listener para o botão saveButton
        saveButton.setOnClickListener {
            // Obtém o nome digitado no EditText
            val name = nameEditText.text.toString()

            // Cria um Intent para enviar o nome de volta para a MainActivity
            val intent = Intent()
            intent.putExtra("name", name)

            // Define o resultado como RESULT_OK e envia o Intent de volta
            setResult(RESULT_OK, intent)

            // Finaliza a AddDataActivity
            finish()
        }
    }
}
