package com.example.sendemail

import android.app.ComponentCaller
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    lateinit var img:ImageView
    private val CAMERA_PERMISSION =1000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        img=findViewById(R.id.img);
        val btn:Button=findViewById(R.id.btn);
        btn.setOnClickListener {
            val intent= Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent,CAMERA_PERMISSION);
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
        caller: ComponentCaller,
    ) {
        super.onActivityResult(requestCode, resultCode, data, caller)
        if(requestCode == CAMERA_PERMISSION && resultCode == RESULT_OK){
            val photo =data!!.extras?.get("data") as Bitmap
            img.setImageBitmap(photo)
        }
    }
}