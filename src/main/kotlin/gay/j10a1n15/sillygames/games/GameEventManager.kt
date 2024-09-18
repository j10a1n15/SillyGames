package gay.j10a1n15.sillygames.games

import gay.j10a1n15.sillygames.events.Events

object GameEventManager {

    private var game: Game? = null

    init {
        Events.TICK.register { game?.onTick() }
        Events.KEYBOARD.register { game?.onKeyClick(it) }
    }

    fun setGame(game: Game?) {
        this.game = game
    }
}
