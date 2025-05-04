package com.example.mayday

import android.Manifest
import android.annotation.SuppressLint
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
import android.location.Location
import android.location.LocationManager
import android.location.LocationListener
import android.content.Context
import android.text.Html
import android.util.Log
import androidx.core.content.FileProvider
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date



class MainActivity : AppCompatActivity() {
    private lateinit var sosButton: FrameLayout
    private lateinit var alarmButton: Button
    private lateinit var capturePhotoButton: Button
    private lateinit var speechToTextButton: Button
    private lateinit var sendEmailButton: Button
    private lateinit var messageInput: EditText
    private var mediaPlayer: MediaPlayer? = null
    private lateinit var addEmergencyContactButton: Button
    private var emergencyPhone: String? = null
    private var emergencyEmail: String? = null
    private var photoUri: Uri? = null



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
        addEmergencyContactButton = findViewById(R.id.addEmergencyContactButton)



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

        addEmergencyContactButton.setOnClickListener {
            val intent = Intent(this, AddEmergencyContactActivity::class.java)
            startActivityForResult(intent, 101)
        }


        requestPermissions()
        loadEmergencyContact()

    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun sendSOSMessage() {
        if (emergencyPhone.isNullOrEmpty()) {
            Toast.makeText(this, "No emergency phone number saved.", Toast.LENGTH_SHORT).show()
            return
        }

        getCurrentLocation { location ->
            val locationText = if (location != null) {
                "Location: https://maps.google.com/?q=${location.latitude},${location.longitude}"
            } else {
                "Location: Unavailable"
            }

            val message = """
            🚨 *EMERGENCY ALERT*
            
            I need help urgently. Please contact me immediately.
            
            $locationText
            
            Sent from Emergency SOS app – MAYDAY
        """.trimIndent()

            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("smsto:$emergencyPhone")
                putExtra("sms_body", message)
            }

            if (intent.resolveActivity(packageManager) != null) {
                Log.d("SOS", "Launching SMS intent: $intent")
                Toast.makeText(this, "Trying to send SMS...", Toast.LENGTH_SHORT).show()
                startActivity(intent)
            } else {
                Toast.makeText(this, "No messaging app found.", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun playAlarmSound() {
        mediaPlayer?.release()

        mediaPlayer = MediaPlayer.create(this, R.raw.siren)

        mediaPlayer?.setOnCompletionListener {
            it.release()
            mediaPlayer = null
        }

        mediaPlayer?.start()
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun capturePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(packageManager) != null) {
            val photoFile: File? = try {
                createImageFile()
            } catch (ex: IOException) {
                Toast.makeText(this, "Error creating image file", Toast.LENGTH_SHORT).show()
                null
            }

            photoFile?.also {
                photoUri = FileProvider.getUriForFile(
                    this,
                    "${packageName}.fileprovider",
                    it
                )
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }


    private fun getCurrentLocation(callback: (Location?) -> Unit) {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(this, "Location permission not granted", Toast.LENGTH_SHORT).show()
            callback(null)
            return
        }

        val listener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                callback(location)
                locationManager.removeUpdates(this)
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
            override fun onProviderEnabled(provider: String) {}
            override fun onProviderDisabled(provider: String) {
                callback(null)
            }
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, listener)
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
        if (emergencyEmail.isNullOrEmpty()) {
            Toast.makeText(this, "No emergency email saved.", Toast.LENGTH_SHORT).show()
            return
        }
        Toast.makeText(this, "Trying to send Email...", Toast.LENGTH_SHORT).show()


        getCurrentLocation { location ->
            val locationText = if (location != null) {
                "Location: https://maps.google.com/?q=${location.latitude},${location.longitude}"
            } else {
                "Location: Unavailable"
            }

            val message = """
            <h2>🚨 EMERGENCY ALERT</h2>
            <p>I need help urgently. Please contact me immediately.</p>
            <p><strong>$locationText</strong></p>
            <br>
            <footer>Sent from Emergency SOS app – <b>MAYDAY</b></footer>
        """.trimIndent()

            val emailIntent = Intent(Intent.ACTION_SEND).apply {
                type = "message/rfc822"
                putExtra(Intent.EXTRA_EMAIL, arrayOf(emergencyEmail))
                putExtra(Intent.EXTRA_SUBJECT, "Emergency SOS Alert")
                putExtra(Intent.EXTRA_TEXT, Html.fromHtml(message))
                photoUri?.let {
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    putExtra(Intent.EXTRA_STREAM, it)
                }
            }


            try {
                startActivity(Intent.createChooser(emailIntent, "Send email using..."))
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(this, "No email app found.", Toast.LENGTH_SHORT).show()
            }
        }
    }


    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File = cacheDir
        return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir)
    }


    private fun requestPermissions() {
        val permissions = arrayOf(
            Manifest.permission.SEND_SMS,
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.ACCESS_FINE_LOCATION
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

    private fun loadEmergencyContact() {
        val sharedPref = getSharedPreferences("EmergencyContact", MODE_PRIVATE)
        emergencyPhone = sharedPref.getString("phone", null)
        emergencyEmail = sharedPref.getString("email", null)
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
            101 -> loadEmergencyContact()

            REQUEST_IMAGE_CAPTURE -> {
                if (resultCode == Activity.RESULT_OK) {
                    if (photoUri != null) {
                        Toast.makeText(this, "Photo captured successfully!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Photo capture failed!", Toast.LENGTH_SHORT).show()
                    }
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
        mediaPlayer?.release()
        mediaPlayer = null
    }
}