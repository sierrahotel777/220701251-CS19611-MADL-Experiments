package com.example.calculator

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var display: TextView
    private var currentInput = ""
    private var operator = ""
    private var firstNumber = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        display = findViewById(R.id.display)

        val buttons = listOf(
            R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
            R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9
        )

        for (buttonId in buttons) {
            findViewById<Button>(buttonId).setOnClickListener {
                val value = (it as Button).text.toString()
                currentInput += value
                display.text = currentInput
            }
        }

        val operations = mapOf(
            R.id.btnAdd to "+",
            R.id.btnSub to "-",
            R.id.btnMul to "*",
            R.id.btnDiv to "/"
        )

        for ((buttonId, op) in operations) {
            findViewById<Button>(buttonId).setOnClickListener {
                if (currentInput.isNotEmpty()) {
                    firstNumber = currentInput
                    operator = op
                    currentInput = ""
                }
            }
        }

        findViewById<Button>(R.id.btnEqual).setOnClickListener {
            if (currentInput.isNotEmpty() && firstNumber.isNotEmpty()) {
                val secondNumber = currentInput
                val result = performCalculation(firstNumber, secondNumber, operator)
                display.text = result.toString()
                currentInput = result.toString()
                firstNumber = ""
                operator = ""
            }
        }

        findViewById<Button>(R.id.btnClear).setOnClickListener {
            currentInput = ""
            firstNumber = ""
            operator = ""
            display.text = "0"
        }
    }

    private fun performCalculation(num1: String, num2: String, op: String): Double {
        val first = num1.toDoubleOrNull() ?: return 0.0
        val second = num2.toDoubleOrNull() ?: return 0.0

        return when (op) {
            "+" -> first + second
            "-" -> first - second
            "*" -> first * second
            "/" -> if (second != 0.0) first / second else 0.0
            else -> 0.0
        }
    }
}
