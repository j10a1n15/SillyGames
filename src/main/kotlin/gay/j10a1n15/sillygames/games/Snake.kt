package gay.j10a1n15.sillygames.games

import gay.j10a1n15.sillygames.data.KeybindSet
import gay.j10a1n15.sillygames.rpc.RpcInfo
import gay.j10a1n15.sillygames.rpc.RpcProvider
import gay.j10a1n15.sillygames.utils.Vector2d
import gg.essential.elementa.UIComponent
import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.UIContainer
import gg.essential.elementa.components.UIText
import gg.essential.elementa.constraints.AspectConstraint
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.constraints.ChildBasedMaxSizeConstraint
import gg.essential.elementa.constraints.ChildBasedSizeConstraint
import gg.essential.elementa.constraints.SiblingConstraint
import gg.essential.elementa.dsl.childOf
import gg.essential.elementa.dsl.constrain
import gg.essential.elementa.dsl.constraint
import gg.essential.elementa.dsl.percent
import gg.essential.elementa.dsl.plus
import java.awt.Color

class Snake : Game(), RpcProvider {

    private val gridWidth = 30
    private val gridHeight = 20

    private var snake = mutableListOf(Vector2d(10, 10))
    private var direction = Vector2d(1, 0)
    private var foodPosition = randomLocation()
    private var gameOver = false
    private var score = 0

    private val gameSpeed = 200L
    private var lastUpdateTime = System.currentTimeMillis()

    private val rpc = RpcInfo(
        firstLine = "Snake",
        secondLine = "Score: $score",
        start = System.currentTimeMillis(),
    )

    override fun onTick() {
        if (gameOver) return
        if (snake.isEmpty()) return

        if (System.currentTimeMillis() - lastUpdateTime < gameSpeed) return
        lastUpdateTime = System.currentTimeMillis()

        val newHead = (snake.first() + direction).updateHeadOutsideBounds()

        snake = (mutableListOf(newHead) + snake).let {
            if (newHead == foodPosition) {
                score += 1
                foodPosition = randomLocation()
                rpc.secondLine = "Score: $score"
            } else if (it.size > 4) {
                return@let it.dropLast(1)
            }
            it
        }.toMutableList()

        if (newHead in snake.drop(1)) {
            gameOver = true
        }
    }

    override fun onKeyHeld(key: Int) {
        if (gameOver) return

        // TODO: If in PIP, only use secondary keybinds
        val newDirection = KeybindSet.configPrimary().getDirection(key) ?: KeybindSet.configSecondary().getDirection(key) ?: return

        if (newDirection + direction == Vector2d(0, 0)) return
        direction = newDirection
    }

    private fun Vector2d.updateHeadOutsideBounds(): Vector2d {
        if (x < 0) x = gridWidth - 1
        if (x >= gridWidth) x = 0
        if (y < 0) y = gridHeight - 1
        if (y >= gridHeight) y = 0

        return this
    }

    override fun getDisplay(): UIComponent {
        val container = UIContainer().constrain {
            x = CenterConstraint()
            y = CenterConstraint()
            width = 100.percent
            height = 100.percent
        }

        val cellHeightPercent = 100f / gridHeight

        val totalGridHeightPercent = cellHeightPercent * gridHeight

        val background = UIBlock().constrain {
            width = ChildBasedSizeConstraint()
            height = totalGridHeightPercent.percent()
            x = CenterConstraint()
            y = CenterConstraint()
            color = Color.GREEN.constraint
        } childOf container

        val grid = UIContainer().constrain {
            width = ChildBasedMaxSizeConstraint()
            height = 100.percent()
            x = CenterConstraint()
        } childOf background

        for (_y in 0 until gridHeight) {
            val row = UIContainer().constrain {
                width = ChildBasedSizeConstraint()
                height = cellHeightPercent.percent()
                y = SiblingConstraint()
            } childOf grid

            for (_x in 0 until gridWidth) {
                val indexInSnake = snake.indexOfFirst { it.x == _x && it.y == _y }
                val gridColor = when {
                    foodPosition.x == _x && foodPosition.y == _y -> Color.BLUE
                    indexInSnake == 0 -> Color.RED
                    indexInSnake != -1 && indexInSnake % 2 == 1 -> Color(23, 172, 16)
                    indexInSnake != -1 -> Color(72, 232, 40)
                    (_x + _y) % 2 == 0 -> Color(0, 100, 0)
                    else -> Color(0, 120, 0)
                }

                UIBlock().constrain {
                    height = 100.percent()
                    width = AspectConstraint(1f)
                    x = SiblingConstraint()
                    color = gridColor.constraint
                } childOf row
            }
        }

        UIText("Score: $score").constrain {
            x = CenterConstraint()
            y = background.constraints.y
        } childOf container

        if (gameOver) {
            UIBlock().constrain {
                x = CenterConstraint()
                y = CenterConstraint()
                width = 100.percent
                height = 100.percent
                color = Color(0, 0, 0, 150).constraint
            } childOf container

            UIText("Game Over!").constrain {
                x = CenterConstraint()
                y = CenterConstraint()
            } childOf container

            UIText("Click to Restart").constrain {
                x = CenterConstraint()
                y = SiblingConstraint() + 10.percent
            }.onMouseClick {
                reset()
            } childOf container
        }

        return container
    }

    private fun reset() {
        snake = mutableListOf(Vector2d(10, 10))
        direction = Vector2d(1, 0)
        foodPosition = randomLocation()
        gameOver = false
        score = 0
        rpc.secondLine = "Score: $score"
    }

    private fun randomLocation(): Vector2d = Vector2d((0 until gridWidth).random(), (0 until gridHeight).random()).let {
        if (it in snake) randomLocation() else it
    }

    override val supportsPictureInPicture = true

    override val name = "Snake"

    override fun getRpcInfo() = rpc
}
