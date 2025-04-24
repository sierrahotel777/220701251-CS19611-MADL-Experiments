package com.example.graphicalprimitives

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class SampleCanvas @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private  val paint = Paint();
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.setColor(Color.YELLOW)
        canvas.drawPaint(paint)
        paint.setColor(Color.RED)

        canvas.drawCircle(250f,400f,100f,paint)
        paint.textSize=50f
        canvas.drawText("circle",200f,200f,paint)
        paint.setColor(Color.BLUE)

        canvas.drawRect(400f,250f,600f,700f,paint)
        paint.textSize=50f
        canvas.drawText("Rectangle",400f,200f,paint)
        paint.textSize=50f
        canvas.drawText("Line",200f,800f,paint)
        canvas.drawLine(300f,1000f,350f,1100f,paint)
        paint.setColor(Color.GREEN)

        paint.textSize=50f
        canvas.drawText("Square",400f,800f,paint)
        canvas.drawRect(400f,900f,600f,1100f,paint)

    }
}
