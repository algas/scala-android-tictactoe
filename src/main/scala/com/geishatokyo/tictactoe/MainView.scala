package com.geishatokyo.tictactoe

import android.content.Context
import android.util.AttributeSet
import android.view.{View, SurfaceView, SurfaceHolder, GestureDetector, MotionEvent}

class MainView(context: Context, attrs: AttributeSet) extends SurfaceView(context, attrs) {
  val holder = getHolder
  val thread = new MainThread(holder, context)

  holder addCallback (new SurfaceHolder.Callback {
    def surfaceCreated(holder: SurfaceHolder) {
      thread.start()
    }
    def surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
      thread.setCanvasSize(width, height)
    }
    def surfaceDestroyed(holder: SurfaceHolder) {}
  })

  setFocusable(true)
  setLongClickable(true)
  setGesture()

  def setGesture() {
    val gd = new GestureDetector(new GestureDetector.SimpleOnGestureListener() {
    })
    setOnTouchListener(new View.OnTouchListener() {
      def onTouch(v: View, e: MotionEvent): Boolean = {
        if (e.getAction == MotionEvent.ACTION_DOWN) {
          val x = e.getX.toInt
          val y = e.getY.toInt
          thread.addTouch(x, y)
        }
        true
      }
    })
  }
}
