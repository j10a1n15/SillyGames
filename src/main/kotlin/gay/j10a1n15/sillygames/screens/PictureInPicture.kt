package gay.j10a1n15.sillygames.screens

import gay.j10a1n15.sillygames.games.Game
import gg.essential.elementa.ElementaVersion
import gg.essential.elementa.WindowScreen
import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.dsl.childOf
import gg.essential.elementa.dsl.constrain
import gg.essential.elementa.dsl.constraint
import gg.essential.elementa.dsl.percent
import gg.essential.elementa.utils.invisible
import java.awt.Color

class PictureInPicture(private val element: Game) : WindowScreen(ElementaVersion.V5) {
    init {
        val container = UIBlock().constrain {
            x = 75.percent
            y = 75.percent
            width = 25.percent
            height = 25.percent
            color = Color.BLACK.invisible().constraint
        } childOf window

        element.getDisplay() childOf container
    }
}
