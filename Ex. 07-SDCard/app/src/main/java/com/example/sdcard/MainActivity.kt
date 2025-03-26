package com.example.sdcard

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.FileWriter

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val etRollNo: EditText = findViewById(R.id.etRoll)
        val etName: EditText = findViewById(R.id.etName)
        val etCGPA: EditText = findViewById(R.id.etCGPA)
        val btSave: Button = findViewById(R.id.btSave)
        val btLoad: Button = findViewById(R.id.btLoad)

        btSave.setOnClickListener{
            val rollNo=etRollNo.text.toString()
            val name=etName.text.toString()
            val cgpa=etCGPA.text.toString()

            val file = File(getExternalFilesDir(null),"students.txt")
            val writer = FileWriter(file)

            writer.write("$rollNo\n$name\n$cgpa")
            writer.close()

            etCGPA.text.clear()
            etName.text.clear()
            etRollNo.text.clear()
            
            Toast.makeText(this, "Data saved!", Toast.LENGTH_LONG).show()
        }

        btLoad.setOnClickListener {
            val file =File(getExternalFilesDir(null), "students.txt")
            val reader=BufferedReader(FileReader(file))

            val rollNo=reader.readLine()
            val name=reader.readLine()
            val cgpa=reader.readLine()

            etRollNo.setText(rollNo)
            etName.setText(name)
            etCGPA.setText(cgpa)

            reader.close()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}