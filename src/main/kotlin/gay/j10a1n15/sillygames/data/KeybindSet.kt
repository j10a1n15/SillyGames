package gay.j10a1n15.sillygames.data

import gay.j10a1n15.sillygames.SillyGames
import gay.j10a1n15.sillygames.utils.Vector2d

class KeybindSet(
    val keybindUp: Int,
    val keybindDown: Int,
    val keybindLeft: Int,
    val keybindRight: Int,
) {

    fun getDirection(key: Int): Vector2d? {
        return when (key) {
            keybindUp -> Vector2d(0, -1)
            keybindDown -> Vector2d(0, 1)
            keybindLeft -> Vector2d(-1, 0)
            keybindRight -> Vector2d(1, 0)
            else -> null
        }
    }

    companion object {
        private val config get() = SillyGames.config

        fun configPrimary() = KeybindSet(config.keybindUp, config.keybindDown, config.keybindLeft, config.keybindRight)

        fun configSecondary() =
            KeybindSet(config.keybindUpSecondary, config.keybindDownSecondary, config.keybindLeftSecondary, config.keybindRightSecondary)
    }
}
