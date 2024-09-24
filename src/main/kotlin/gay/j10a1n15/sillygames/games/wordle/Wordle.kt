package gay.j10a1n15.sillygames.games.wordle

import gay.j10a1n15.sillygames.games.Game
import gay.j10a1n15.sillygames.rpc.RpcProvider
import gg.essential.elementa.UIComponent
import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.UIContainer
import gg.essential.elementa.components.UIRoundedRectangle
import gg.essential.elementa.components.UIText
import gg.essential.elementa.components.input.UITextInput
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.constraints.ChildBasedMaxSizeConstraint
import gg.essential.elementa.constraints.ChildBasedSizeConstraint
import gg.essential.elementa.constraints.RelativeConstraint
import gg.essential.elementa.constraints.SiblingConstraint
import gg.essential.elementa.dsl.childOf
import gg.essential.elementa.dsl.constrain
import gg.essential.elementa.dsl.effect
import gg.essential.elementa.dsl.percent
import gg.essential.elementa.dsl.pixels
import gg.essential.elementa.dsl.plus
import gg.essential.elementa.dsl.toConstraint
import gg.essential.elementa.effects.OutlineEffect
import gg.essential.elementa.utils.withAlpha
import gg.essential.universal.UKeyboard
import java.awt.Color

private const val BOX_SIZE = 30
private const val BOX_SPACING = 5

private const val KEYBOARD_WIDTH = 15
private const val KEYBOARD_HEIGHT = 20
private const val KEYBOARD_SPACING = 5

private const val ROWS = 6
private const val COLS = 5

class Wordle : Game(), RpcProvider {

    private val state: WordleState = WordleState()
    private var text: String = ""
    private var time: Long = 0

    private val wordIndexInput = UITextInput("Custom Word Index").constrain {
        width = 90.percent
        height = 10.pixels()
        x = CenterConstraint()
        y = CenterConstraint()
        color = Color.GRAY.toConstraint()
    }.onKeyType { _, key ->
        if (key == UKeyboard.KEY_ENTER) {
            val index = (this as UITextInput).getText().toIntOrNull()
            if (index != null) {
                state.reset(index)
                setText("")
            }
        }
    }.onMouseEnter {
        grabWindowFocus()
    }.onMouseLeave {
        releaseWindowFocus()
    } as UITextInput

    private fun getWordIndexInputDisplay(): UIComponent? {
        if (state.tries != 0) return null
        else {
            val container = UIBlock().constrain {
                this.width = 20.percent()
                this.height = 10.percent()
                this.x = CenterConstraint()
                this.y = 100.percent()
                this.color = WordlePalette.GRAY.toConstraint()
            }

            wordIndexInput childOf container

            return container
        }
    }

    private fun getWordDisplay(): UIComponent {
        return UIContainer().apply {
            this.constrain {
                this.width = ChildBasedMaxSizeConstraint()
                this.height = ChildBasedSizeConstraint()
            }

            for (y in 0 until ROWS) {
                val row = UIContainer().constrain {
                    this.width = ChildBasedSizeConstraint()
                    this.height = ChildBasedMaxSizeConstraint()
                    this.y = SiblingConstraint(BOX_SPACING.toFloat())
                } childOf this

                val guess: String = state.guesses.getOrNull(y) ?: ""
                for (x in 0 until COLS) {
                    val letter = guess.getOrNull(x)?.toString() ?: ""
                    val square = UIBlock().constrain {
                        this.color = state.colors[y][x].toConstraint()
                        this.x = SiblingConstraint(BOX_SPACING.toFloat())
                        this.width = BOX_SIZE.pixels()
                        this.height = BOX_SIZE.pixels()
                    } childOf row effect OutlineEffect(WordlePalette.GRAY, 1f)

                    UIText(letter.uppercase()).constrain {
                        this.x = CenterConstraint()
                        this.y = CenterConstraint()
                        this.color = WordlePalette.WHITE.toConstraint()
                    } childOf square
                }
            }
        }
    }

    private fun getLetterDisplay(): UIComponent {
        return UIContainer().apply {
            this.constrain {
                this.width = ChildBasedMaxSizeConstraint()
                this.height = ChildBasedSizeConstraint()
            }

            fun addLetter(letter: Char, container: UIContainer) {
                val square = UIRoundedRectangle(4f).constrain {
                    this.color = state.getKeyboardColor(letter).toConstraint()
                    this.width = KEYBOARD_WIDTH.pixels()
                    this.height = KEYBOARD_HEIGHT.pixels()
                    this.x = SiblingConstraint(KEYBOARD_SPACING.toFloat())
                } childOf container

                UIText(letter.toString(), false).constrain {
                    this.x = CenterConstraint()
                    this.y = CenterConstraint()
                    this.color = WordlePalette.WHITE.toConstraint()
                } childOf square

                square.onMouseClick {
                    state.enterChar(letter.lowercaseChar())
                }
            }

            UIContainer().constrain {
                this.width = ChildBasedSizeConstraint()
                this.height = ChildBasedMaxSizeConstraint()
                this.x = CenterConstraint()
            }.apply {
                "QWERTYUIOP".forEach { letter -> addLetter(letter, this) }
            } childOf this

            UIContainer().constrain {
                this.width = ChildBasedSizeConstraint()
                this.height = ChildBasedMaxSizeConstraint()
                this.y = SiblingConstraint(KEYBOARD_SPACING.toFloat())
                this.x = CenterConstraint()
            }.apply {
                "ASDFGHJKL".forEach { letter -> addLetter(letter, this) }
            } childOf this

            UIContainer().constrain {
                this.width = ChildBasedSizeConstraint()
                this.height = ChildBasedMaxSizeConstraint()
                this.y = SiblingConstraint(KEYBOARD_SPACING.toFloat())
                this.x = CenterConstraint()
            }.apply {
                val enter = UIRoundedRectangle(4f).constrain {
                    this.color = WordlePalette.LIGHT_GRAY.toConstraint()
                    this.width = (KEYBOARD_WIDTH * 2).pixels()
                    this.height = KEYBOARD_HEIGHT.pixels()
                    this.x = 0.pixels()
                } childOf this

                UIText("ENTER", false).constrain {
                    this.x = CenterConstraint()
                    this.y = CenterConstraint()
                    this.color = WordlePalette.WHITE.toConstraint()
                    this.width = RelativeConstraint(0.95f)
                } childOf enter

                enter.onMouseClick { guess() }

                "ZXCVBNM".forEach { letter -> addLetter(letter, this) }

                val backspace = UIRoundedRectangle(4f).constrain {
                    this.color = WordlePalette.LIGHT_GRAY.toConstraint()
                    this.width = (KEYBOARD_WIDTH * 2).pixels()
                    this.height = KEYBOARD_HEIGHT.pixels()
                    this.x = SiblingConstraint(KEYBOARD_SPACING.toFloat())
                } childOf this

                UIText("\u232B", false).constrain {
                    this.x = CenterConstraint()
                    this.y = (-3).pixels()
                    this.color = WordlePalette.WHITE.toConstraint()
                    this.width = RelativeConstraint(0.5f)
                } childOf backspace

                backspace.onMouseClick {
                    state.keyPress(UKeyboard.KEY_BACKSPACE)
                }
            } childOf this
        }
    }

    override fun getDisplay(): UIComponent {
        return UIContainer().apply {

            UIBlock().constrain {
                this.color = WordlePalette.DARK_GRAY.toConstraint()
                this.height = ChildBasedSizeConstraint(20f)
                this.width = 250.pixels()
                this.x = CenterConstraint()
                this.y = CenterConstraint()
            }.apply {
                UIText("Wordle", true).constrain {
                    this.x = CenterConstraint()
                    this.y = 20.pixels()
                    this.width = RelativeConstraint(0.25f)
                    this.color = WordlePalette.WHITE.toConstraint()
                } childOf this

                getWordDisplay().constrain {
                    this.x = CenterConstraint()
                    this.y = SiblingConstraint(20f)
                } childOf this

                getLetterDisplay().constrain {
                    this.x = CenterConstraint()
                    this.y = SiblingConstraint(20f)
                } childOf this
            } childOf this

            val timeDiff = System.currentTimeMillis() - time

            val multiLineText = text.split("\n").reversed()
            for ((i, line) in multiLineText.withIndex()) {
                UIText(line, true).constrain {
                    this.x = CenterConstraint()
                    this.y = SiblingConstraint(20f, true) + (20f * i).pixels()
                    this.color = WordlePalette.WHITE.withAlpha(
                        // face in and out over first 1.5 seconds and last 1.5 seconds
                        when (timeDiff) {
                            in 0..1500 -> (timeDiff / 1500f).coerceIn(0f, 1f)
                            in 3500..Int.MAX_VALUE -> ((5000 - timeDiff) / 1500f).coerceIn(0f, 1f)
                            else -> 1f
                        },
                    ).toConstraint()
                } childOf this
            }

            getWordIndexInputDisplay()?.childOf(this)
        }
    }

    private fun guess() {
        this.text = state.guess() ?: ""
        this.time = System.currentTimeMillis()
    }

    override fun onKeyPressed(key: Int): Boolean {
        if (wordIndexInput.hasFocus()) return false
        when (key) {
            UKeyboard.KEY_ENTER -> guess()
            else -> state.keyPress(key)
        }
        return false
    }

    override fun onTick() {
        if (System.currentTimeMillis() - time > 5000) {
            text = ""
            if (state.reset) {
                state.reset()
            }
        }
    }

    override val name = "Wordle"

    override fun getRpcInfo() = this.state.getRpcInfo()
}
