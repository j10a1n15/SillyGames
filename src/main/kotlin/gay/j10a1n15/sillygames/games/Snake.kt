package gay.j10a1n15.sillygames.games

import gay.j10a1n15.sillygames.utils.Vector2d
import gg.essential.elementa.UIComponent
import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.UIContainer
import gg.essential.elementa.components.UIText
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.constraints.SiblingConstraint
import gg.essential.elementa.dsl.childOf
import gg.essential.elementa.dsl.constrain
import gg.essential.elementa.dsl.constraint
import gg.essential.elementa.dsl.percent
import gg.essential.elementa.dsl.plus
import gg.essential.universal.UKeyboard
import java.awt.Color

class Snake : Game() {

    private val gridWidth = 30
    private val gridHeight = 20

    private var snake = mutableListOf(Vector2d(10, 10))
    private var direction = Vector2d(1, 0)
    private var foodPosition = randomLocation()
    private var gameOver = false
    private var score = 0

    private val gameSpeed = 200L
    private var lastUpdateTime = System.currentTimeMillis()

    override fun onTick() {
        if (gameOver) return
        if (snake.isEmpty()) return

        if (System.currentTimeMillis() - lastUpdateTime < gameSpeed) return
        lastUpdateTime = System.currentTimeMillis()

        val newHead = (snake.first() + direction).updateHeadOutsideBounds()
        if (newHead in snake) {
            gameOver = true
            return
        }

        snake = (mutableListOf(newHead) + snake).let {
            if (newHead == foodPosition) {
                score += 1
                foodPosition = randomLocation()
            } else if (it.size > 4) {
                return@let it.dropLast(1)
            }
            it
        }.toMutableList()
    }

    override fun onKeyHeld(key: Int) {
        if (gameOver) return

        val newDirection = when (key) {
            UKeyboard.KEY_W -> Vector2d(0, -1)
            UKeyboard.KEY_S -> Vector2d(0, 1)
            UKeyboard.KEY_A -> Vector2d(-1, 0)
            UKeyboard.KEY_D -> Vector2d(1, 0)
            else -> return
        }

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

        val cellWidthPercent = 100f / gridWidth
        val cellHeightPercent = 100f / gridHeight

        val totalGridWidthPercent = cellWidthPercent * gridWidth
        val totalGridHeightPercent = cellHeightPercent * gridHeight

        val background = UIBlock().constrain {
            width = totalGridWidthPercent.percent
            height = totalGridHeightPercent.percent
            x = CenterConstraint()
            y = CenterConstraint()
            color = Color.GREEN.constraint
        } childOf container

        for (_x in 0 until gridWidth) {
            for (_y in 0 until gridHeight) {
                val gridColor = if ((_x + _y) % 2 == 0) Color(0, 100, 0) else Color(0, 120, 0)
                UIBlock().constrain {
                    width = cellWidthPercent.percent
                    height = cellHeightPercent.percent
                    x = (_x * cellWidthPercent).percent
                    y = (_y * cellHeightPercent).percent
                    color = gridColor.constraint
                } childOf background
            }
        }

        UIBlock().constrain {
            width = cellWidthPercent.percent
            height = cellHeightPercent.percent
            x = (foodPosition.x * cellWidthPercent).percent
            y = (foodPosition.y * cellHeightPercent).percent
            color = Color.BLUE.constraint
        } childOf background

        for ((index, segment) in snake.withIndex()) {
            val segmentColor = if (index == 0) Color.RED else if (index % 2 == 1) Color(23, 172, 16) else Color(72, 232, 40)
            UIBlock().constrain {
                width = cellWidthPercent.percent
                height = cellHeightPercent.percent
                x = (segment.x * cellWidthPercent).percent
                y = (segment.y * cellHeightPercent).percent
                color = segmentColor.constraint
            } childOf background
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
                snake = mutableListOf(Vector2d(10, 10))
                direction = Vector2d(1, 0)
                foodPosition = randomLocation()
                gameOver = false
                score = 0
            } childOf container
        }

        return container
    }

    private fun randomLocation(): Vector2d = Vector2d((0 until gridWidth).random(), (0 until gridHeight).random()).let {
        if (it in snake) randomLocation() else it
    }

    override val pip = true

    override val name = "Snake"
}
