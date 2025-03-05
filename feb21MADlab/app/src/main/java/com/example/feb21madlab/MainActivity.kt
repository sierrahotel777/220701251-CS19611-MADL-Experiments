package com.example.feb21madlab

import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var editText1=findViewById<EditText>(R.id.input1)
        var editText2=findViewById<EditText>(R.id.input2)
        var editText3=findViewById<EditText>(R.id.input3)
        var button_add=findViewById<Button>(R.id.add)
        var button_sub=findViewById<Button>(R.id.sub)
        var button_mul=findViewById<Button>(R.id.mul)
        var button_div=findViewById<Button>(R.id.div)
        var input1:string=editText1.text.toString()
        var input2:string=editText2.text.toString()
        button_add.setOnClickListener(){
            var button_result_add=input1.toInt()+input2.toInt()
            editText3.setText(button_result_add.toString())
            Toast.makeText(context:this,text:"Addition successful")
        }
        button_add.setOnClickListener(){
            var button_result_add=input1.toInt()+input2.toInt()
            editText3.setText(button_result_add.toString())
        }
        button_add.setOnClickListener(){
            var button_result_add=input1.toInt()+input2.toInt()
            editText3.setText(button_result_add.toString())
        }

        button_add.setOnClickListener(){
            var button_result_add=input1.toInt()+input2.toInt()
            editText3.setText(button_result_add.toString())
        }

        button_add.setOnClickListener(){
            var button_result_add=input1.toInt()+input2.toInt()
            editText3.setText(button_result_add.toString())
        }


        // Create an instance of the custom view and add it to the FrameLayout
        val customView = object : View(this) {
            private val paint = Paint().apply {
                isAntiAlias = true // Smooth edges for shapes
            }

            override fun onDraw(canvas: Canvas) {
                super.onDraw(canvas)

                // Draw a Circle
                paint.color = Color.RED
                canvas.drawCircle(300f, 300f, 100f, paint)



                // Draw an Ellipse
                paint.color = Color.GREEN
                val ellipseRect = RectF(150f, 500f, 450f, 700f)
                canvas.drawOval(ellipseRect, paint)

                // Draw a Rectangle
                paint.color = Color.BLUE
                val rect = RectF(100f, 800f, 600f, 1000f)
                canvas.drawRect(rect, paint)

                // Draw Text
                paint.color = Color.BLACK
                paint.textSize = 50f
                canvas.drawText("Eclipse , Circle , Rectangle", 250f, 1200f, paint)
            }
        }

        // Add the custom view to the FrameLayout
        val drawingLayout = findViewById<FrameLayout>(R.id.drawingLayout)
        drawingLayout.addView(customView)
    }
}