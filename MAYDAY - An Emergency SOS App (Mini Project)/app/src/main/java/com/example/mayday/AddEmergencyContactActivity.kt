package com.example.mayday

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AddEmergencyContactActivity : AppCompatActivity() {
    private lateinit var phoneNumberInput: EditText
    private lateinit var emailInput: EditText
    private lateinit var saveButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_emergency_contact)

        phoneNumberInput = findViewById(R.id.phoneNumberInput)
        emailInput = findViewById(R.id.emailInput)
        saveButton = findViewById(R.id.saveContactButton)

        saveButton.setOnClickListener {
            val phone = phoneNumberInput.text.toString().trim()
            val email = emailInput.text.toString().trim()

            // Regex for phone (10 digits or international)
            val phoneRegex = Regex("^\\+?[0-9]{10,15}$")

            // Regex for email
            val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")

            when {
                phone.isEmpty() || email.isEmpty() -> {
                    Toast.makeText(this, "Please enter both phone and email", Toast.LENGTH_SHORT).show()
                }

                !phoneRegex.matches(phone) -> {
                    Toast.makeText(this, "Invalid phone number format", Toast.LENGTH_SHORT).show()
                }

                !emailRegex.matches(email) -> {
                    Toast.makeText(this, "Invalid email address", Toast.LENGTH_SHORT).show()
                }

                else -> {
                    val sharedPref = getSharedPreferences("EmergencyContact", MODE_PRIVATE)
                    with(sharedPref.edit()) {
                        putString("phone", phone)
                        putString("email", email)
                        apply()
                    }
                    Toast.makeText(this, "Contact Saved!", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }


    }
}
