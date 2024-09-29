package gay.j10a1n15.sillygames.games

import gay.j10a1n15.sillygames.events.KeyDownEvent
import gay.j10a1n15.sillygames.events.TickEvent
import gay.j10a1n15.sillygames.events.handler.Subscribe
import gg.essential.elementa.UIComponent

abstract class Game {
    abstract fun getDisplay(): UIComponent

    @Subscribe
    open fun onTick(event: TickEvent) {
    }

    @Subscribe
    open fun onKeyPressed(event: KeyDownEvent) {
    }
}

abstract class GameInformation {
    abstract val name: String
    abstract val description: String
    open val icon: String? = null
    open val supportsPictureInPicture = false
    abstract val factory: () -> Game
}
