package gay.j10a1n15.sillygames.games

import gay.j10a1n15.sillygames.SillyGames.Companion.registerEvent
import gay.j10a1n15.sillygames.events.TickEvent
import gay.j10a1n15.sillygames.events.handler.Subscribe


object GameManager {

    var game: Game? = null
    private var lastGame: Game? = null

    init {
        game?.let { registerEvent(it) }
    }

    @Subscribe
    fun onTick(event: TickEvent) {
        if (game != lastGame) {
            game?.let { registerEvent(it) }
            lastGame = game
        }
    }
}
