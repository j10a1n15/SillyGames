package gay.j10a1n15.sillygames.utils

class Vector2d(var x: Int, var y: Int) {
    operator fun plus(other: Vector2d) = Vector2d(x + other.x, y + other.y)
    operator fun minus(other: Vector2d) = Vector2d(x - other.x, y - other.y)
    operator fun times(other: Int) = Vector2d(x * other, y * other)
    operator fun div(other: Int) = Vector2d(x / other, y / other)
}
