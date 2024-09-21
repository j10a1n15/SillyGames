package gay.j10a1n15.sillygames.games

import gay.j10a1n15.sillygames.events.Events

object GameManager {

    var game: Game? = null

    init {
        Events.TICK.register { game?.onTick() }
        Events.KEYBOARD.register { game?.onKeyHeld(it) }
        Events.KEYBOARD_DOWN.register { game?.onKeyPressed(it) }
    }
}
