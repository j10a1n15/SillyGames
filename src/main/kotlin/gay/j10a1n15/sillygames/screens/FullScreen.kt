package gay.j10a1n15.sillygames.screens

import gay.j10a1n15.sillygames.games.Game
import gg.essential.elementa.ElementaVersion
import gg.essential.elementa.WindowScreen
import gg.essential.elementa.dsl.childOf

class FullScreen(private val element: Game) : WindowScreen(ElementaVersion.V5) {
    init {
        element.getDisplay() childOf window
    }
}
