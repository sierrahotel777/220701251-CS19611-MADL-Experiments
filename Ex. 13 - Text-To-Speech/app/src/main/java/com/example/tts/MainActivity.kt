package com.example.tts

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.w3c.dom.Text
import java.util.Locale

class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var etText : EditText
    private lateinit var btSpeak : Button
    private lateinit var textToSpeech: TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        etText=findViewById(R.id.etText)
        btSpeak=findViewById(R.id.btSpeak)
        textToSpeech=TextToSpeech(this,this)

        btSpeak.setOnClickListener {
            val text=etText.text.toString()
            textToSpeech.speak(text,TextToSpeech.QUEUE_FLUSH,null,null)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onInit(status: Int) {
        val result=textToSpeech.setLanguage(Locale.US)
        if(result==TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED)
        {
            Toast.makeText(this,"Language is not supported....!",Toast.LENGTH_LONG).show()
        }
    }
}