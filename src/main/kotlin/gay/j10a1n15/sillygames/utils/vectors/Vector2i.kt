package gay.j10a1n15.sillygames.utils.vectors

data class Vector2i(var x: Int, var y: Int) {
    operator fun plus(other: Vector2i) = Vector2i(x + other.x, y + other.y)
    operator fun minus(other: Vector2i) = Vector2i(x - other.x, y - other.y)
    operator fun times(other: Int) = Vector2i(x * other, y * other)
    operator fun div(other: Int) = Vector2i(x / other, y / other)
}
