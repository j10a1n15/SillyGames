package gay.j10a1n15.sillygames.utils

data class Vector2df(var x: Float, var y: Float) {
    operator fun plus(other: Vector2df) = Vector2df(x + other.x, y + other.y)
    operator fun minus(other: Vector2df) = Vector2df(x - other.x, y - other.y)
    operator fun times(other: Float) = Vector2df(x * other, y * other)
    operator fun div(other: Float) = Vector2df(x / other, y / other)
}
