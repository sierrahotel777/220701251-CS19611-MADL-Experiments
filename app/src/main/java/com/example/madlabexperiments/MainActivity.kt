package com.example.madlabexperiments

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout

class MainActivity : AppCompatActivity() {

    private lateinit var textView: TextView
    private lateinit var btnFontSize: Button
    private lateinit var btnFontColor: Button
    private lateinit var btnBgColor: Button
    private lateinit var layout: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView = findViewById(R.id.textView)
        btnFontSize = findViewById(R.id.btnFontSize)
        btnFontColor = findViewById(R.id.btnFontColor)
        btnBgColor = findViewById(R.id.btnBgColor)
        layout = findViewById(R.id.main)

        btnFontSize.setOnClickListener {
            textView.textSize = 35f
            Toast.makeText(this, "Font size changed", Toast.LENGTH_SHORT).show()
        }

        // Change Font Color on Button Click
        btnFontColor.setOnClickListener {
            textView.setTextColor(Color.GREEN)
            Toast.makeText(this, "Font color changed", Toast.LENGTH_SHORT).show()
        }

        // Change Background Color on Button Click
        btnBgColor.setOnClickListener {
            layout.setBackgroundColor(Color.BLUE)
            Toast.makeText(this, "Background color changed", Toast.LENGTH_SHORT).show()
        }
    }
}
