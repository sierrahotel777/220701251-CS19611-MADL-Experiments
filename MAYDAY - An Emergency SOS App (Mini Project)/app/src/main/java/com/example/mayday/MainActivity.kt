package com.example.mayday

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.speech.RecognizerIntent
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var sosButton: FrameLayout
    private lateinit var alarmButton: Button
    private lateinit var capturePhotoButton: Button
    private lateinit var speechToTextButton: Button
    private lateinit var sendEmailButton: Button
    private lateinit var messageInput: EditText
    private var mediaPlayer: MediaPlayer? = null

    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_SPEECH_INPUT = 2
    private val REQUEST_PERMISSIONS = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        sosButton = findViewById(R.id.sosButton)
        alarmButton = findViewById(R.id.alarmButton)
        capturePhotoButton = findViewById(R.id.capturePhotoButton)
        speechToTextButton = findViewById(R.id.speechToTextButton)
        sendEmailButton = findViewById(R.id.sendEmailButton)
        messageInput = findViewById(R.id.messageInput)

        // Set click listeners
        sosButton.setOnClickListener {
            sendSOSMessage()
        }

        alarmButton.setOnClickListener {
            playAlarmSound()
        }

        capturePhotoButton.setOnClickListener {
            capturePhoto()
        }

        speechToTextButton.setOnClickListener {
            startSpeechToText()
        }

        sendEmailButton.setOnClickListener {
            sendEmail()
        }

        // Request necessary permissions
        requestPermissions()
    }

    private fun sendSOSMessage() {
        val message = messageInput.text.toString().ifEmpty { "I need help!" }
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("smsto:")
            putExtra("sms_body", message)
        }

        // Check if there's any app that can handle the intent
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        } else {
            Toast.makeText(this, "No messaging app found.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun playAlarmSound() {
        // Release any existing MediaPlayer instance
        mediaPlayer?.release()

        // Create a new MediaPlayer instance
        mediaPlayer = MediaPlayer.create(this, R.raw.siren)

        // Set a completion listener to release the MediaPlayer when done
        mediaPlayer?.setOnCompletionListener {
            it.release()
            mediaPlayer = null
        }

        // Start playing the sound
        mediaPlayer?.start()
    }

    private fun capturePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, "Camera not available.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun startSpeechToText() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now...")
        }
        try {
            startActivityForResult(intent, REQUEST_SPEECH_INPUT)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, "Speech recognition not supported.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun sendEmail() {
        val message = messageInput.text.toString().ifEmpty { "I need help!" }
        val emailIntent = Intent(Intent.ACTION_SEND).apply {
            type = "message/rfc822" // Use this MIME type for email
            putExtra(Intent.EXTRA_EMAIL, arrayOf("")) // Add recipient email addresses here if needed
            putExtra(Intent.EXTRA_SUBJECT, "Emergency SOS")
            putExtra(Intent.EXTRA_TEXT, message)
        }
        try {
            startActivity(Intent.createChooser(emailIntent, "Send email using..."))
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, "No email app found.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun requestPermissions() {
        val permissions = arrayOf(
            Manifest.permission.SEND_SMS,
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
        )
        val permissionsToRequest = permissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }
        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                permissionsToRequest.toTypedArray(), REQUEST_PERMISSIONS
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSIONS) {
            val deniedPermissions = permissions.zip(grantResults.toTypedArray())
                .filter { it.second != PackageManager.PERMISSION_GRANTED }
                .map { it.first }
            if (deniedPermissions.isNotEmpty()) {
                Toast.makeText(this, "Permissions denied: $deniedPermissions", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_IMAGE_CAPTURE -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val imageBitmap = data.extras?.get("data") as? Bitmap
                    // Handle the captured image as needed
                }
            }

            REQUEST_SPEECH_INPUT -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    if (!result.isNullOrEmpty()) {
                        messageInput.setText(result[0])
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Release the MediaPlayer when the activity is destroyed
        mediaPlayer?.release()
        mediaPlayer = null
    }
}