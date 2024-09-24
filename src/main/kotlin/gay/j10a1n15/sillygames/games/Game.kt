package gay.j10a1n15.sillygames.games

import gg.essential.elementa.UIComponent

abstract class Game {
    abstract fun getDisplay(): UIComponent

    abstract val name: String

    open fun onTick() {}

    open fun onKeyHeld(key: Int) {}

    open fun onKeyPressed(key: Int): Boolean {
        return false
    }

    open val supportsPictureInPicture = false
}
