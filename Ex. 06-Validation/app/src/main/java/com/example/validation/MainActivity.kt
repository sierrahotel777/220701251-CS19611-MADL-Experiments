package com.example.validation

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val username = findViewById<EditText>(R.id.username).text.toString()
        val userid = findViewById<EditText>(R.id.userid).text.toString()
        val button = findViewById<Button>(R.id.validate)

        button.setOnClickListener {
            if(username != "" || userid != ""){
                Toast.makeText(this,"username and userid cannot be left empty",Toast.LENGTH_SHORT).show()
            }
            if (!(username.all { it.isLetter() })){
                Toast.makeText(this, "username does not contain alphabets", Toast.LENGTH_SHORT).show()
            }
            if (userid.length == 4){
                Toast.makeText(this, "userid has more than 4 digits", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()

            }
        }
    }
}