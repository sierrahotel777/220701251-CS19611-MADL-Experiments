package com.example.sendemail

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val etEmail:EditText=findViewById(R.id.etEmail)
        val etSubject:EditText=findViewById(R.id.etSubject)
        val etText:EditText=findViewById(R.id.etText)

        val btSend: Button =findViewById(R.id.btSend)

        btSend.setOnClickListener {

            val email=etEmail.text.toString()
            val sub=etSubject.text.toString()
            val text=etText.text.toString()

            val intent = Intent(Intent.ACTION_SEND)
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
            intent.putExtra(Intent.EXTRA_SUBJECT, sub)
            intent.putExtra(Intent.EXTRA_TEXT, text)
            intent.type="message/rfc822"
            startActivity(Intent.createChooser(intent,"Choose a client."))


        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}