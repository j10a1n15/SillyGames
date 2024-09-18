package gay.j10a1n15.sillygames.screens

import gay.j10a1n15.sillygames.games.Game
import gg.essential.elementa.ElementaVersion
import gg.essential.elementa.WindowScreen
import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.dsl.childOf
import gg.essential.elementa.dsl.constrain
import gg.essential.elementa.dsl.constraint
import gg.essential.elementa.dsl.percent
import gg.essential.elementa.utils.invisible
import gg.essential.universal.GuiScale
import java.awt.Color

class FullScreen(private val element: Game) : WindowScreen(
    version = ElementaVersion.V5,
    newGuiScale = GuiScale.scaleForScreenSize().ordinal,
) {
    private val container = UIBlock().constrain {
        x = CenterConstraint()
        y = CenterConstraint()
        width = 75.percent
        height = 75.percent
        color = Color.BLACK.invisible().constraint
    } childOf window

    init {
        element.getDisplay().constrain {
            width = 100.percent
            height = 100.percent
        } childOf container
    }

    override fun onTick() {
        super.onTick()

        container.clearChildren()
        element.getDisplay().constrain {
            width = 100.percent
            height = 100.percent
        } childOf container
    }
}

