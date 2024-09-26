package gay.j10a1n15.sillygames.screens

import gay.j10a1n15.sillygames.events.Events
import gay.j10a1n15.sillygames.games.Game
import gay.j10a1n15.sillygames.games.GameManager
import gg.essential.elementa.ElementaVersion
import gg.essential.elementa.WindowScreen
import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.UIText
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.dsl.childOf
import gg.essential.elementa.dsl.constrain
import gg.essential.elementa.dsl.percent
import gg.essential.elementa.dsl.toConstraint
import gg.essential.elementa.utils.invisible
import gg.essential.universal.GuiScale
import gg.essential.universal.UMatrixStack
import java.awt.Color

object PictureInPicture : WindowScreen(
    version = ElementaVersion.V5,
    newGuiScale = GuiScale.scaleForScreenSize().ordinal,
) {
    init {
        Events.RENDER.register(::onRender)
    }

    var game: Game? = null
        set(value) {
            field = value
            updateEventListeners()
        }
    var visible = false
        set(value) {
            field = value
            updateEventListeners()
        }

    private fun onRender(matrix: UMatrixStack) {
        if (!visible) return
        window.clearChildren()

        val container = UIBlock().constrain {
            x = 75.percent()
            y = 75.percent()
            width = 25.percent()
            height = 25.percent()
            color = Color.BLACK.invisible().toConstraint()
        } childOf window

        game?.let {
            it.getDisplay() childOf container
        } ?: run {
            UIBlock().constrain {
                width = 100.percent()
                height = 100.percent()
                color = Color.RED.toConstraint()
            }.apply {
                UIText("No game selected").constrain {
                    x = CenterConstraint()
                    y = CenterConstraint()
                } childOf this
            } childOf container
        }

        window.draw(matrix)
    }

    private fun updateEventListeners() {
        GameManager.game = if (visible) game else null
    }
}
