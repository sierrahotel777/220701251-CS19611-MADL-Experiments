package com.example.alertdialogbox

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val edText: EditText = findViewById(R.id.edText)
        val btDisplay : Button = findViewById(R.id.btDisplay)

        btDisplay.setOnClickListener{
            val alertDialog = AlertDialog.Builder(this)
            .setMessage(edText.text.toString())
            .setTitle("MAD Lab")
            .setCancelable(false)
            .setPositiveButton("OK") { dialog, which ->
                    Toast.makeText(this,"You Clicked OK", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel") {dialog, which ->
                Toast.makeText(this,"You Clicked Cancel", Toast.LENGTH_SHORT).show()
            }
            alertDialog.show()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}