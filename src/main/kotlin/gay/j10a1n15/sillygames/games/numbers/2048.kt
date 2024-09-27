package gay.j10a1n15.sillygames.games.numbers

import gay.j10a1n15.sillygames.data.KeybindSet
import gay.j10a1n15.sillygames.games.Game
import gay.j10a1n15.sillygames.games.GameInformation
import gay.j10a1n15.sillygames.games.wordle.WordlePalette
import gay.j10a1n15.sillygames.rpc.RpcInfo
import gay.j10a1n15.sillygames.rpc.RpcProvider
import gg.essential.elementa.UIComponent
import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.UIContainer
import gg.essential.elementa.components.UIText
import gg.essential.elementa.constraints.AspectConstraint
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.constraints.MinConstraint
import gg.essential.elementa.dsl.childOf
import gg.essential.elementa.dsl.constrain
import gg.essential.elementa.dsl.effect
import gg.essential.elementa.dsl.minus
import gg.essential.elementa.dsl.percent
import gg.essential.elementa.dsl.pixels
import gg.essential.elementa.dsl.plus
import gg.essential.elementa.dsl.toConstraint
import gg.essential.elementa.effects.OutlineEffect
import gg.essential.elementa.utils.withAlpha
import java.awt.Color

class TwentyFortyEight : Game(), RpcProvider {

    private var state = TwentyFortyEightState()

    private fun getHeader(): UIComponent = UIContainer().apply {
        UIText("2048").constrain {
            this.x = 0.pixels()
            this.y = CenterConstraint()
        } childOf this

        val scoreBox = UIBlock().constrain {
            this.color = Color.WHITE.withAlpha(0.5f).toConstraint()
            this.height = 60.percent()
            this.width = AspectConstraint(1.5f)
            this.x = 0.pixels(alignOpposite = true)
            this.y = CenterConstraint()
        } childOf this

        UIText(state.score.toString()).constrain {
            this.x = CenterConstraint()
            this.y = CenterConstraint()
            this.textScale = 0.75.pixels()
        } childOf scoreBox
    }

    private fun getBoard(): UIComponent = UIBlock().apply {
        constrain {
            this.color = WordlePalette.DARK_GRAY.toConstraint()
            this.width = AspectConstraint()
            this.height = MinConstraint(100.percent(), 200.pixels())
            this.x = CenterConstraint()
            this.y = CenterConstraint()
        }

        val container = UIBlock().constrain {
            this.width = AspectConstraint()
            this.height = 75.percent()
            this.x = CenterConstraint()
            this.y = 20.percent()
        } childOf this

        getHeader().constrain {
            this.width = 100.percent().apply {
                constrainTo = container
            }
            this.height = 20.percent()
            this.x = CenterConstraint()
        } childOf this

        for (y in 0..3) {
            for (x in 0..3) {
                val value = state.get(x, y)
                val block = UIBlock().constrain {
                    this.color = TwentyFortyEightPalette.getTileColor(value).toConstraint()
                    this.height = 20.percent()
                    this.width = 20.percent()
                    this.x = 2.5.percent() + (x * 25).percent()
                    this.y = 2.5.percent() + (y * 25).percent()
                } childOf container

                if (value != 0) {
                    val text = value.toString()
                    UIText(text, shadow = false).constrain {
                        this.x = CenterConstraint()
                        this.y = CenterConstraint() - 1.pixels()
                        this.width = 20.percent()
                        this.height = 20.percent()
                        this.textScale = if (text.length > 3) 1.25.pixels() else 1.5.pixels()
                        this.color = TwentyFortyEightPalette.getTextColor(value).toConstraint()
                    } childOf block
                }
            }
        }
    }

    override fun getDisplay(): UIComponent {
        return UIContainer().apply {
            constrain {
                this.width = 100.percent()
                this.height = 100.percent()
            }

            val board = getBoard() childOf this

            if (state.won || state.lost) {
                val text = if (state.won) "You won!" else "You lost!"
                val fade = ((System.currentTimeMillis() - state.endingTime) / 1000.0f).coerceIn(0.0f, 0.95f)

                val overlay = UIBlock().constrain {
                    this.color = Color.BLACK.withAlpha(fade).toConstraint()
                    this.height = 100.percent()
                    this.width = 100.percent()
                } childOf board

                if (fade > 0.5f) {

                    UIText(text).constrain {
                        this.x = CenterConstraint()
                        this.y = CenterConstraint()
                    } childOf overlay

                    val button = UIBlock().constrain {
                        this.color = WordlePalette.DARK_GRAY.toConstraint()
                        this.height = 25.pixels()
                        this.width = 80.pixels()
                        this.x = CenterConstraint()
                        this.y = CenterConstraint() + 50.pixels()
                    }.onMouseEnter {
                        effect(OutlineEffect(Color.WHITE, 1f))
                    }.onMouseLeave {
                        removeEffect<OutlineEffect>()
                    }.onMouseClick {
                        state = TwentyFortyEightState()
                    } childOf overlay

                    UIText("New Game").constrain {
                        this.x = CenterConstraint()
                        this.y = CenterConstraint()
                    } childOf button
                }
            }
        }
    }


    override fun onKeyPressed(key: Int): Boolean {
        val direction = KeybindSet.configPrimary().getDirection(key) ?: KeybindSet.configSecondary().getDirection(key) ?: return false
        state.input(-direction.x, -direction.y)
        return true
    }

    override fun getRpcInfo(): RpcInfo = state.getRpcInfo()
}

object TwentyFortyEightInformation : GameInformation() {
    override val name: String = "2048"
    override val description: String = "A game where you slide tiles to combine them"
    override val factory: () -> Game = { TwentyFortyEight() }
    override val supportsPictureInPicture: Boolean = true
}
