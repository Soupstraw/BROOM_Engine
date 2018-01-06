import java.awt.{Color, Robot}
import java.awt.event._
import javafx.scene.input.KeyCode
import javax.swing.{JFrame, Timer}

import scala.math.BigDecimal.double2bigDecimal

object MainApplication extends JFrame{
  def main(args: Array[String]): Unit = {

    val resWidth = 300
    val resHeight = 240
    val fov = 60f
    val wallHeight = 1
    val turnSpeed = 0.005
    val moveSpeed = 0.05
    val robot = new Robot()

    val keys = Array.fill(256)(0)

    val mainPanel = new MainPanel()

    MainApplication.setSize(800, 600)
    MainApplication.setResizable(false)
    MainApplication.add(mainPanel)
    MainApplication.setVisible(true)

    var gameWorld = new GameWorld(Player(0, 0, 0), Wall(2, -2, 2, 2) +: Wall(-2, -2, -2, 2) +: Wall(-2, -2, 2, -2) +: Wall(-2, 2, 2, 2) +: List.empty[Wall])

    MainApplication.addKeyListener(new KeyListener {
      override def keyPressed(e: KeyEvent): Unit = keys.update(e.getKeyCode, 1)

      override def keyTyped(e: KeyEvent): Unit = {}

      override def keyReleased(e: KeyEvent): Unit = keys.update(e.getKeyCode, 0)
    })

    MainApplication.addMouseMotionListener(new MouseMotionListener {
      override def mouseDragged(e: MouseEvent): Unit = {}

      override def mouseMoved(e: MouseEvent): Unit = {
        val centerX = MainApplication.getLocationOnScreen.x + MainApplication.getWidth/2
        val centerY = MainApplication.getLocationOnScreen.y + MainApplication.getHeight/2
        gameWorld = gameWorld.rotatePlayer((e.getXOnScreen - centerX) * turnSpeed)
        robot.mouseMove(
          centerX,
          centerY
        )
      }
    })

    val loopTimer = new Timer(1000/60, (e: ActionEvent) => {

      if(keys(KeyEvent.VK_ESCAPE) == 1) System.exit(0)

      val dx = keys(KeyEvent.VK_D) - keys(KeyEvent.VK_A)
      val dy = keys(KeyEvent.VK_W) - keys(KeyEvent.VK_S)

      gameWorld = gameWorld.movePlayer(dx * moveSpeed, dy * moveSpeed)

      val arr = Array
        .ofDim[Color](resWidth, resHeight)
        .zipWithIndex
        .map(p => {
          val rayAngle = fov / resWidth * p._2
          val rayCast = gameWorld.raycastFromPlayer(math.toRadians(rayAngle - fov/2))
          val height = if(rayCast.isEmpty) 0 else resHeight*wallHeight/(rayCast.min)
          val clampedCol = 0.5f * math.max(math.min(height/100, 1f), 0f).toFloat
          p._1.zipWithIndex.map(q =>
            if(math.abs(q._2 - resHeight/2) < height/2)
              new Color(clampedCol, clampedCol, clampedCol)
            else{
              val fade = 0.5f * math.max(math.min(math.abs(q._2 - resHeight/2f)/100f, 1f), 0f)
              new Color((216*fade).toInt, (145*fade).toInt, (50*fade).toInt)
            }
          )
        })

      mainPanel.setData(arr)
      mainPanel.repaint()
    })
    loopTimer.setRepeats(true)
    loopTimer.start()
  }
}
