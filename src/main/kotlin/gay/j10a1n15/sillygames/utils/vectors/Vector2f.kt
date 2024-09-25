package gay.j10a1n15.sillygames.utils.vectors

data class Vector2f(var x: Float, var y: Float) {
    operator fun plus(other: Vector2f) = Vector2f(x + other.x, y + other.y)
    operator fun minus(other: Vector2f) = Vector2f(x - other.x, y - other.y)
    operator fun times(other: Float) = Vector2f(x * other, y * other)
    operator fun div(other: Float) = Vector2f(x / other, y / other)
}
