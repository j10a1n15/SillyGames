package gay.j10a1n15.sillygames.screens

import gay.j10a1n15.sillygames.games.Game
import gay.j10a1n15.sillygames.games.Snake
import gay.j10a1n15.sillygames.games.SpaceInvaders
import gay.j10a1n15.sillygames.games.wordle.Wordle
import gay.j10a1n15.sillygames.utils.SillyUtils.display
import gg.essential.elementa.ElementaVersion
import gg.essential.elementa.WindowScreen
import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.UIText
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.dsl.childOf
import gg.essential.elementa.dsl.constrain
import gg.essential.elementa.dsl.percent
import gg.essential.elementa.dsl.toConstraint
import gg.essential.elementa.utils.withAlpha
import gg.essential.universal.GuiScale
import java.awt.Color

val gameFactories = setOf<() -> Game>(
    { Wordle() },
    { Snake() },
    { SpaceInvaders() },
)

class GameSelector : WindowScreen(
    version = ElementaVersion.V5,
    newGuiScale = GuiScale.scaleForScreenSize().ordinal,
    drawDefaultBackground = false,
) {
    private val container = UIBlock().constrain {
        x = CenterConstraint()
        y = CenterConstraint()
        width = 75.percent()
        height = 75.percent()
        color = Color(0x212121).withAlpha(0.85f).toConstraint()
    } childOf window

    private val title = UIText("Select a game").constrain {
        x = CenterConstraint()
        y = 10.percent()
    } childOf container

    init {
        gameFactories.forEachIndexed { index, factory ->
            val button = UIBlock().constrain {
                x = CenterConstraint()
                y = (20 + 10 * index).percent()
                width = 50.percent()
                height = 5.percent()
                color = Color(0x424242).withAlpha(0.95f).toConstraint()
            } childOf container

            UIText(factory().name).constrain {
                x = CenterConstraint()
                y = CenterConstraint()
            } childOf button

            button.onMouseClick {
                FullScreen(factory.invoke()).display()
            }
        }
    }

}
