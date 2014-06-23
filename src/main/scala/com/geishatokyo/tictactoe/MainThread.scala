package com.geishatokyo.tictactoe

import android.content.Context
import android.view.SurfaceHolder
import android.graphics.{Canvas, Paint, Rect, Path}

case object Player {
  case object O extends Player(0)
  case object X extends Player(1)
  val values = Array(O, X)
}

sealed abstract class Player(val code: Int) {
  val name = toString
}

case class Point (x: Int, y: Int)

case class Piece (player: Player, point: Point)

class Board {
  private[this] var pieces: List[Piece] = List.empty
  var hasExited: Boolean = false

  def nextPlayer: Player = {
    if (pieces.length % 2 == 0) Player.O else Player.X
  }

  def currentPlayer: Player = {
    if (pieces.length % 2 == 1) Player.O else Player.X
  }

  def add(p: Point): Unit = {
    pieces = (new Piece(nextPlayer, p)) :: pieces
  }

  def add(x: Int, y: Int): Unit = {
    add(new Point(x, y))
  }

  def points: List[Point] = {
    pieces.map(_.point)
  }

  def isAddible(x: Int, y: Int): Boolean = {
    !points.contains(new Point(x, y))
  }

  def length: Int = {
    pieces.length
  }

  def getPieces: List[Piece] = {
    pieces
  }

  def getPlayerPieces(p: Player): List[Piece] = {
    pieces.filter(x => x.player.name == p.name)
  }

  def hasWon(): Boolean = {
    hasWon(currentPlayer)
  }

  def hasWon(player: Player): Boolean = {
    var ps: List[Point] = getPlayerPieces(player).map(_.point)
    checkVertical(ps) || checkHorizontal(ps) || checkDiagonal(ps)
  }

  def checkVertical(ps: List[Point]): Boolean = {
    (for (i <- 0 until 3) yield ps.filter(p => p.x == i).length).toList.max == 3
  }

  def checkHorizontal(ps: List[Point]): Boolean = {
    (for (i <- 0 until 3) yield ps.filter(p => p.y == i).length).toList.max == 3
  }

  def checkDiagonal(ps: List[Point]): Boolean = {
    (ps.filter(p => p.x == p.y).length == 3) || (ps.filter(p => p.x + p.y == 2).length == 3)
  }

  def exit: Unit = {
    hasExited = true
  }

  def reset: Unit = {
    hasExited = false
    pieces = List.empty
  }
}


class MainThread(holder: SurfaceHolder, context: Context) extends Thread {
  val quantum = 100
  var board: Board = new Board

  val bluishWhite = new Paint
  bluishWhite.setARGB(255, 255, 255, 255)
  val bluishBlack = new Paint
  bluishBlack.setARGB(255, 0, 0, 0)
  var canvasWidth: Int = _
  var canvasHeight: Int = _

  override def run {
    var isRunning: Boolean = true
    while (isRunning) {
      val t0 = System.currentTimeMillis
      drawViews()
      val t1 = System.currentTimeMillis
      if (t1 - t0 < quantum) Thread.sleep(quantum - (t1 - t0))
    }
  }

  def drawViews() {
    withCanvas { g =>
      g drawRect (0, 0, canvasWidth, canvasHeight, bluishBlack)
      onPaint(g)
    }
  }

  def setCanvasSize(w: Int, h: Int) {
    canvasWidth = w
    canvasHeight = h
  }

  def addTouch(x: Int, y: Int){
    touchTile(x, y)
    touchReset(x, y)
  }

  def touchTile (x: Int, y: Int){
    val dx = (x - 0) / 200
    val dy = (y - 200) / 200
    if ((0 <= dx && dx < 3) && (0 <= dy && dy < 3)) {
      if (!board.hasExited && board.isAddible(dx, dy)){
        board.add(dx, dy)
        if (board.hasWon) {
          board.exit
        }
      }
    }
  }

  def touchReset (x: Int, y: Int){
    if ((200 <= x && x < 400) && (800 <= y && y < 900)) {
      board.reset
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

  def drawInfo(g: Canvas) {
    val textPaint = new Paint
    textPaint.setARGB(255, 255, 255, 255)
    textPaint.setTextSize(48)
    g drawText ("OX GAME", 200, 100, textPaint)
    var text: String = ""
    if (board.hasExited) {
      board.currentPlayer match {
        case Player.O => text = "Player O Win!"
        case Player.X => text = "Player X Win!"
      }
    } 
    else if (board.length == 9) {
      text = "Draw Game!"
    }
    else {
      board.nextPlayer match {
        case Player.O => text = "Turn of O"
        case Player.X => text = "Turn of X"
      }
    }
    g drawText (text, 200, 150, textPaint)
  }

  def drawTile(g: Canvas) {
    for (j <- 0 until 3){
      for (i <- 0 until 3) {
        g drawRect (i*200+10, 200+j*200+10, i*200+190, 200+j*200+190, bluishWhite)
      }
    }
    val bluishGray = new Paint
    bluishGray.setARGB(255, 128, 128, 128)
    g drawRect (200, 800, 400, 900, bluishGray)
    val textPaint = new Paint
    textPaint.setARGB(255, 255, 255, 0)
    textPaint.setTextSize(48)
    g drawText ("RESET", 230, 870, textPaint)
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

  def drawMark(g: Canvas, p: Piece) {
    val player: Player = p.player;
    player match {
      case Player.O => drawO(g, p.point.x, p.point.y)
      case Player.X => drawX(g, p.point.x, p.point.y)
    }
  }

  def onPaint(g: Canvas) {
    drawInfo(g)
    drawTile(g)
    for (p <- board.getPieces){
      drawMark(g, p)
    }
  }

}