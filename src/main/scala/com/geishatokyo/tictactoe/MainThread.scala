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
  def clean()
  {
    gameTurn = 0
  }
}

class MainThread(holder: SurfaceHolder, context: Context) extends Thread {
  val quantum = 100
  var ui: Option[AbstractUI] = None
  var pieces: List[(Int,Int)] = List.empty
  var isFinish = false

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

  def checkFinishImpl(ls: List[List[(Int,Int)]])
  {
    if(pieces.length==9)
    {
      isFinish = true
      return
    }
    for ( l <- ls )
    {
      var containsContents: List[((Int,Int),Int)] = List ( )
      for ( pair <- l )
      {
        if (pieces.zipWithIndex.exists(_._1==pair))
        {
          containsContents = (pair,pieces.zipWithIndex.indexWhere(_._1==pair)) :: containsContents
        }
      }
      if(containsContents.length==3)
      {
        val h = containsContents.head._2
        if(containsContents.count(_._2%2==h%2)==3)
        {
          isFinish = true
          return
        }
      }
    }
  }

  def checkFinish()
  {
    val colmun = for(i <- 0 until 3) yield{(for(j <- 0 until 3) yield{(i,j)}).toList}
    val line = for(i <- 0 until 3) yield{(for(j <- 0 until 3) yield{(j,i)}).toList}
    val cross = List((for(i <- 0 until 3) yield{(i,i)}).toList ,(for(i <- 0 until 3) yield{(2-i,i)}).toList)
    checkFinishImpl(colmun.toList ::: line.toList ::: cross)
  }

  def addTouch(x: Int, y: Int){
    val dx = (x - 0) / 200
    val dy = (y - 200) / 200
    if ((0 <= dx && dx < 3) && (0 <= dy && dy < 3) && !pieces.contains((dx,dy)) && !isFinish)
    {
      pieces = (dx,dy) :: pieces
      checkFinish()
      ui.get.countUp()
    }
    else if (dx == 3 && dy == 1)
    {
      pieces = List ( )
      isFinish = false
      ui.get.clean()
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
    textPaint.setARGB(255 ,255, if(isFinish){0}else{255}, if(isFinish){0}else{255})
    textPaint.setTextSize(48)
    g drawText (if(isFinish){"FINISH!"}else{"OX GAME"}, 160, 100, textPaint)
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

  def drawReset(g: Canvas, dx: Int,dy: Int)
  {
    val textPaint = new Paint
    textPaint.setARGB(255 , 127, 127, 127)
    textPaint.setTextSize(48)
    g drawRect (3*200+10, 200+200+10, 3*200+190, 200+200+190, bluishSilver)
    g drawText ("RESET", 10 + 3 * 200, 190 + 2 * 200 , textPaint)
  }

  def onPaint(g: Canvas) {
    drawTile(g)
    for (((x,y), i) <- pieces.reverse.zipWithIndex){
      drawMark(g, i, x, y)
    }
    drawReset(g, 3, 1)
    drawMark(g, ui.get.turn, 3, 2)
  }

}
