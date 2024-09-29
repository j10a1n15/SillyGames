package gay.j10a1n15.sillygames.games.numbers

import java.awt.Color

object TwentyFortyEightPalette {

    private val TEXT_COLOR_BELOW_8 = Color(119, 110, 101)
    private val TEXT_COLOR_ABOVE_8 = Color(249, 246, 242)

    private val TILE_0 = Color(205, 193, 180)
    private val TILE_2 = Color(238, 228, 218)
    private val TILE_4 = Color(237, 224, 200)
    private val TILE_8 = Color(242, 177, 121)
    private val TILE_16 = Color(245, 149, 99)
    private val TILE_32 = Color(246, 124, 95)
    private val TILE_64 = Color(246, 94, 59)
    private val TILE_128 = Color(237, 207, 114)
    private val TILE_256 = Color(237, 204, 97)
    private val TILE_512 = Color(237, 200, 80)
    private val TILE_1024 = Color(237, 197, 63)
    private val TILE_2048 = Color(237, 194, 46)

    fun getTileColor(value: Int) = when (value) {
        2 -> TILE_2
        4 -> TILE_4
        8 -> TILE_8
        16 -> TILE_16
        32 -> TILE_32
        64 -> TILE_64
        128 -> TILE_128
        256 -> TILE_256
        512 -> TILE_512
        1024 -> TILE_1024
        2048 -> TILE_2048
        else -> TILE_0
    }

    fun getTextColor(value: Int) = if (value < 8) TEXT_COLOR_BELOW_8 else TEXT_COLOR_ABOVE_8
}
