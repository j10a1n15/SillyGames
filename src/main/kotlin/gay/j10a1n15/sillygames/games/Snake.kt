package gay.j10a1n15.sillygames.games

import gay.j10a1n15.sillygames.events.Events
import gay.j10a1n15.sillygames.utils.Vector2d
import gg.essential.elementa.UIComponent
import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.UIContainer
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.dsl.childOf
import gg.essential.elementa.dsl.constrain
import gg.essential.elementa.dsl.constraint
import gg.essential.elementa.dsl.percent
import gg.essential.elementa.dsl.pixel
import gg.essential.elementa.state.BasicState
import org.lwjgl.input.Keyboard
import java.awt.Color

class Snake : Game() {

    init {
        Events.TICK.register { onTick() }
        Events.KEYBOARD.register { onKeyClick(it) }
    }

    private val gridSize = 20
    private val gridWidth = 30
    private val gridHeight = 20

    private var snake = mutableListOf(Vector2d(10, 10))
    private var direction = Vector2d(1, 0)
    private var foodPosition = randomLocation()
    private var gameOver = BasicState(false)
    private var score = BasicState(0)

    private val gameSpeed = 200L
    private var lastUpdateTime = System.currentTimeMillis()

    private fun onTick() {
        if (gameOver.get()) return
        if (snake.isEmpty()) return

        if (System.currentTimeMillis() - lastUpdateTime < gameSpeed) return
        lastUpdateTime = System.currentTimeMillis()

        val newHead = (snake.first() + direction).updateHeadOutsideBounds()
        if (newHead in snake) {
            gameOver.set(true)
            return
        }

        snake = (mutableListOf(newHead) + snake).let {
            if (newHead == foodPosition) {
                score.set(score.get() + 1)
                foodPosition = randomLocation()
            } else if (it.size > 4) {
                return@let it.dropLast(1)
            }
            it
        }.toMutableList()
    }

    private fun onKeyClick(key: Int) {
        if (gameOver.get()) return

        val newDirection = when (key) {
            Keyboard.KEY_W -> Vector2d(0, -1)
            Keyboard.KEY_S -> Vector2d(0, 1)
            Keyboard.KEY_A -> Vector2d(-1, 0)
            Keyboard.KEY_D -> Vector2d(1, 0)
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

        val background = UIBlock().constrain {
            width = (gridWidth * gridSize).pixel
            height = (gridHeight * gridSize).pixel
            x = CenterConstraint()
            y = CenterConstraint()
            color = Color.GREEN.constraint
        } childOf container

        for (_x in 0 until gridWidth) {
            for (_y in 0 until gridHeight) {
                val gridColor = if ((_x + _y) % 2 == 0) Color(0, 100, 0) else Color(0, 120, 0)
                UIBlock().constrain {
                    width = gridSize.pixel
                    height = gridSize.pixel
                    x = (_x * gridSize).pixel
                    y = (_y * gridSize).pixel
                    color = gridColor.constraint
                } childOf background
            }
        }

        UIBlock().constrain {
            width = gridSize.pixel
            height = gridSize.pixel
            x = (foodPosition.x * gridSize).pixel
            y = (foodPosition.y * gridSize).pixel
            color = Color.BLUE.constraint
        } childOf background

        for ((index, segment) in snake.withIndex()) {
            val segmentColor = if (index == 0) Color.RED else if (index % 2 == 1) Color(23, 172, 16) else Color(72, 232, 40)
            UIBlock().constrain {
                width = gridSize.pixel
                height = gridSize.pixel
                x = (segment.x * gridSize).pixel
                y = (segment.y * gridSize).pixel
                color = segmentColor.constraint
            } childOf background
        }

        return container
    }

    private fun randomLocation(): Vector2d = Vector2d((0 until gridWidth).random(), (0 until gridHeight).random()).let {
        if (it in snake) randomLocation() else it
    }
}
