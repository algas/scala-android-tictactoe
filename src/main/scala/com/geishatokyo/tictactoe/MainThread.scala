package com.geishatokyo.tictactoe

import android.content.Context
import android.view.SurfaceHolder
import android.graphics.{Canvas, Paint, Rect, Path}

class AbstractUI {
  private[this] var gameTurn: Int = 0
  private[this] var pointX: Int = 0
  private[this] var pointY: Int = 0

  def turn: Int = gameTurn
  def x: Int = pointX
  def y: Int = pointY
  def setX(x: Int) {
    pointX = x
  }
  def setY(y: Int) {
    pointY = y
  }
  def countUp() {
    gameTurn += 1
  }
}

class MainThread(holder: SurfaceHolder, context: Context) extends Thread {
  val quantum = 100
  var ui: Option[AbstractUI] = None
  var pieces: List[(Int,Int)] = List.empty

  val bluishSilver = new Paint
  bluishSilver.setARGB(255, 255, 255, 255)
  val bluishGray = new Paint
  bluishGray.setARGB(255, 0, 0, 0)
  var canvasWidth: Int = _
  var canvasHeight: Int = _

  override def run {
    ui = Some(new AbstractUI)
    var isRunning: Boolean = true
    while (isRunning) {
      val t0 = System.currentTimeMillis
      drawViews()
      val t1 = System.currentTimeMillis
      if (t1 - t0 < quantum) Thread.sleep(quantum - (t1 - t0))
      else ()
    }
  }

  def drawViews() {
    withCanvas { g =>
      g drawRect (0, 0, canvasWidth, canvasHeight, bluishGray)
      onPaint(g)
    }
  }

  def setCanvasSize(w: Int, h: Int) {
    canvasWidth = w
    canvasHeight = h
  }

  def addTouch(x: Int, y: Int){
    val dx = (x - 0) / 200
    val dy = (y - 200) / 200
    if ((0 <= dx && dx < 3) && (0 <= dy && dy < 3)) {
      pieces = (dx,dy) :: pieces
      ui.get.countUp()
    }
  }

  def withCanvas(f: Canvas => Unit) {
    val canvas = holder.lockCanvas(null)
    try {
      f(canvas)
    } finally {
      holder.unlockCanvasAndPost(canvas)
    }
  }

  def drawTile(g: Canvas) {
    val textPaint = new Paint
    textPaint.setARGB(255, 255, 255, 255)
    textPaint.setTextSize(48)
    g drawText ("OX GAME", 160, 100, textPaint)
    g drawText (ui.get.turn.toString, 200, 150, textPaint) // debugging
    for (j <- 0 until 3){
      for (i <- 0 until 3) {
        g drawRect (i*200+10, 200+j*200+10, i*200+190, 200+j*200+190, bluishSilver)
      }
    }
  }

  def drawO(g: Canvas, dx: Int, dy: Int) {
    val green = new Paint
    green.setARGB(255, 0, 255, 0)
    g drawCircle (dx*200+100, dy*200+300, 80, green)
  }

  def drawX(g: Canvas, dx: Int, dy: Int) {
    val red = new Paint
    red.setARGB(255, 255, 0, 0)
    var path = new Path()
    path.moveTo(20, 20)
    path.lineTo(180, 180)
    path.lineTo(180, 20)
    path.lineTo(20, 180)
    path.lineTo(20, 20)
    path.offset(dx*200, dy*200+200)
    g drawPath(path, red)
  }

  def drawMark(g: Canvas, i: Int, dx: Int, dy: Int) {
    if (i % 2 == 0){
      drawO(g, dx, dy)
    } 
    else {
      drawX(g, dx, dy)
    }
  }

  def onPaint(g: Canvas) {
    drawTile(g)
    for (((x,y), i) <- pieces.reverse.zipWithIndex){
      drawMark(g, i, x, y)
    }
  }

}