package gay.j10a1n15.sillygames.games

import gay.j10a1n15.sillygames.utils.Vector2d
import gg.essential.elementa.UIComponent
import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.UIContainer
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.dsl.childOf
import gg.essential.elementa.dsl.constrain
import gg.essential.elementa.dsl.constraint
import gg.essential.elementa.dsl.effect
import gg.essential.elementa.dsl.percent
import gg.essential.elementa.dsl.pixel
import gg.essential.elementa.effects.OutlineEffect
import gg.essential.elementa.state.BasicState
import gg.essential.elementa.utils.invisible
import java.awt.Color

class Snake : Game() {

    private val gridSize = 20
    private val gridWidth = 30
    private val gridHeight = 20

    private var snake = mutableListOf(Vector2d(10, 10))
    private var direction = Vector2d(1, 0)
    private var foodPosition = Vector2d(15, 15)
    private var gameOver = BasicState(false)
    private var score = BasicState(0)

    private val gameSpeed = 200L
    private var lastUpdateTime = System.currentTimeMillis()

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
                UIBlock().constrain {
                    width = gridSize.pixel
                    height = gridSize.pixel
                    x = (_x * gridSize).pixel
                    y = (_y * gridSize).pixel
                    color = Color.BLACK.invisible().constraint
                } childOf background effect OutlineEffect(Color.BLACK, 1f)
            }
        }

        val snakeHead = UIBlock().constrain {
            width = gridSize.pixel
            height = gridSize.pixel
            color = Color.RED.constraint
            x = (snake.first().x * gridSize).pixel
            y = (snake.first().y * gridSize).pixel
        } childOf background

        return container
    }

}
