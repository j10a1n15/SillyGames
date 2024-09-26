package gay.j10a1n15.sillygames.screens

import gay.j10a1n15.sillygames.games.Game
import gay.j10a1n15.sillygames.games.Snake
import gay.j10a1n15.sillygames.games.wordle.Wordle
import gay.j10a1n15.sillygames.utils.SillyUtils.display
import gg.essential.elementa.ElementaVersion
import gg.essential.elementa.UIComponent
import gg.essential.elementa.WindowScreen
import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.UIContainer
import gg.essential.elementa.components.UIImage
import gg.essential.elementa.components.UIText
import gg.essential.elementa.constraints.AspectConstraint
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.constraints.FillConstraint
import gg.essential.elementa.constraints.ImageAspectConstraint
import gg.essential.elementa.constraints.PixelConstraint
import gg.essential.elementa.constraints.SiblingConstraint
import gg.essential.elementa.dsl.childOf
import gg.essential.elementa.dsl.constrain
import gg.essential.elementa.dsl.percent
import gg.essential.elementa.dsl.toConstraint
import gg.essential.elementa.utils.withAlpha
import gg.essential.universal.GuiScale
import java.awt.Color

val gameFactories = listOf<() -> Game>(
    { Wordle() },
    { Snake() },
    { Snake() },
    { Wordle() },
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
        gameFactories.chunked(2).forEachIndexed { row, factoryPair ->
            factoryPair.forEachIndexed { index, factory ->
                createButton(factory, row, index)
            }
        }
    }

    private fun createButton(factory: () -> Game, row: Int, index: Int) {
        val game = factory()
        val button = UIBlock().constrain {
            x = (5 + 50 * index).percent()
            y = (20 + 30 * row).percent()
            width = 40.percent()
            height = 20.percent()
            color = Color(0x424242).toConstraint()
        }.onMouseClick {
            FullScreen(game).display()
        } childOf container

        if (game.icon != null) {
            addIconToButton(button, game.icon!!)
            addTextAndPipSupport(button, game)
        } else {
            addTextAndPipSupport(button, game)
        }
    }

    private fun addIconToButton(button: UIComponent, icon: String) {
        UIImage.ofResource("/assets/sillygames/images/$icon.png").constrain {
            width = ImageAspectConstraint()
            height = 100.percent()
        } childOf button
    }

    private fun addTextAndPipSupport(container: UIComponent, game: Game) {
        val textContainer = UIContainer().constrain {
            x = SiblingConstraint()
            width = FillConstraint()
            height = 100.percent()
        } childOf container

        UIText(game.name).constrain {
            x = CenterConstraint()
            y = CenterConstraint()
        } childOf textContainer

        if (game.supportsPictureInPicture) {
            UIBlock().constrain {
                x = PixelConstraint(0f, alignOpposite = true)
                y = 75.percent()
                width = AspectConstraint(1f)
                height = 25.percent()
                color = Color(0x000000).withAlpha(0.5f).toConstraint()
            }.onMouseClick {
                PictureInPicture.game = game
                PictureInPicture.visible = true
            } childOf textContainer
        }
    }
}
