import java.awt._
import javax.swing.JPanel

class MainPanel() extends JPanel{

  var data = Array.empty[Array[Color]]

  def setData(data: Array[Array[Color]]) = {
    this.data = data
  }

  override def paint(g: Graphics) = {
    if(data.length > 0) {

      val dx = g.getClipBounds.width.toFloat / data.length
      val dy = g.getClipBounds.height.toFloat / data(0).length

      for {
        x <- 0 until data.length
        y <- 0 until data(0).length
      } {
        data(x)(y) match {
          case c => g.setColor(c)
          case _ => g.setColor(Color.WHITE)
        }
        g.fillRect(
          (x * dx).toInt,
          (y * dy).toInt,
          ((x + 1) * dx).toInt,
          ((y + 1) * dy).toInt
        )
      }
    }
  }
}
