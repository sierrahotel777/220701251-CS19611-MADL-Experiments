package com.example.shapes

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class SampleCanvas @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var paint = Paint()

    override fun onDraw(canvas: Canvas) {
        paint.setColor(Color.YELLOW)
        canvas.drawPaint(paint)

        paint.setColor(Color.RED)
        canvas.drawCircle(200f, 200f, 100f, paint)
        paint.textSize = 100f
        canvas.drawText("Circle", 100f, 500f, paint)

        paint.setColor(Color.GREEN)
        canvas.drawRect(1000f,400f,400f,200f, paint)
        paint.textSize = 100f
        canvas.drawText("Rectangle", 500f, 600f, paint)

        paint.setColor(Color.BLUE)
        canvas.drawRect(800f,800f,400f,400f, paint)

    }
}