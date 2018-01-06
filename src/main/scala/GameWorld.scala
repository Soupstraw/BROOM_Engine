case class Player(x: Double, y: Double, rot: Double)
case class Wall(x1: Float, y1: Float, x2: Float, y2: Float)

case class GameWorld(player: Player, walls: List[Wall]) {

  def raycastFromPlayer(angle: Double) = {
    val r1 = scala.math.sin(player.rot + angle)
    val r2 = scala.math.cos(player.rot + angle)

    val p1 = player.x
    val p2 = player.y

    walls.foldLeft(List.empty[Double])((l, wall) => {
      val w1 = wall.x2 - wall.x1
      val w2 = wall.y2 - wall.y1
      val a1 = wall.x1
      val a2 = wall.y1

      val beta = (w2*p1 - w2*a1 + w1*a2 - w1*p2)/(w1*r2-w2*r1)
      val alpha = if(w1 != 0) (p1 - a1 + beta*r1)/w1 else (p2 - a2 + beta*r2)/w2

      if(alpha >= 0 && alpha <= 1 && beta >= 0) beta +: l else l
    })
  }

  def movePlayer(hori: Double, vert: Double) = {
    copy(player.copy(
      x = player.x + vert * math.sin(player.rot) + hori * math.cos(player.rot),
      y = player.y + vert * math.cos(player.rot) - hori * math.sin(player.rot)
    ))
  }

  def rotatePlayer(angle: Double) = copy(player.copy(rot = player.rot + angle))
}
