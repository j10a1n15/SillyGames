package gay.j10a1n15.sillygames.data

import gay.j10a1n15.sillygames.config.Config
import gay.j10a1n15.sillygames.utils.vectors.Vector2i

class KeybindSet(
    val keybindUp: Int,
    val keybindDown: Int,
    val keybindLeft: Int,
    val keybindRight: Int,
) {

    fun getDirection(key: Int): Vector2i? {
        return when (key) {
            keybindUp -> Vector2i(0, -1)
            keybindDown -> Vector2i(0, 1)
            keybindLeft -> Vector2i(-1, 0)
            keybindRight -> Vector2i(1, 0)
            else -> null
        }
    }

    companion object {
        fun configPrimary() = with(Config) {
            KeybindSet(keybindUp, keybindDown, keybindLeft, keybindRight)
        }

        fun configSecondary() = with(Config) {
            KeybindSet(keybindUpSecondary, keybindDownSecondary, keybindLeftSecondary, keybindRightSecondary)
        }
    }
}
