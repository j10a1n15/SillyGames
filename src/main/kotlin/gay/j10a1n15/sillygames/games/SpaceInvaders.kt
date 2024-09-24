package gay.j10a1n15.sillygames.games

import gay.j10a1n15.sillygames.utils.SillyUtils.isKeyDown
import gay.j10a1n15.sillygames.utils.Vector2df
import gg.essential.elementa.UIComponent
import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.UIText
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.dsl.childOf
import gg.essential.elementa.dsl.constrain
import gg.essential.elementa.dsl.minus
import gg.essential.elementa.dsl.percent
import gg.essential.elementa.dsl.toConstraint
import gg.essential.universal.UKeyboard
import java.awt.Color
import kotlin.math.absoluteValue

class SpaceInvaders : Game() {

    private var player = Vector2df(50.0f, 90.0f)
    private var playerSpeed = 1.5f
    private var score = 0
    private val numEntitiesPerRow = 11
    private val numEntityRows = 6
    private var entities = mutableListOf<Vector2df>()
    private var bullets = mutableListOf<Vector2df>()
    private var alreadyShoot = false

    private val playerWidth = 5f
    private val bulletSpeed = 5f
    private val entitySize = 5f

    private var entityDirection = 1
    private var entitySpeed = 0.2f
    private val entityDropDistance = 5f

    private fun start() {
        player = Vector2df(50.0f, 90.0f)
        entities.clear()
        bullets.clear()
        score = 0

        generateEntities()
    }

    private fun generateEntities() {
        val totalWidth = 70
        val entityWidth = entitySize
        val spaceBetweenEntities = (totalWidth - numEntitiesPerRow * entityWidth) / (numEntitiesPerRow + 1)

        for (row in 0 until numEntityRows) {
            for (i in 0 until numEntitiesPerRow) {
                val xPosition = (spaceBetweenEntities + i * (entityWidth + spaceBetweenEntities)) + 15
                val yPosition = 6 + row * (entitySize + 5)
                entities.add(Vector2df(xPosition, yPosition))
            }
        }
    }

    private fun checkKeyPresses() {
        if (UKeyboard.KEY_A.isKeyDown()) player.x -= playerSpeed
        if (UKeyboard.KEY_D.isKeyDown()) player.x += playerSpeed
        if (UKeyboard.KEY_SPACE.isKeyDown() && !alreadyShoot) {
            bullets.add(Vector2df(player.x + playerWidth / 2, 90.0f))
            alreadyShoot = true
        }
        if (!UKeyboard.KEY_SPACE.isKeyDown()) alreadyShoot = false

        player.x = player.x.coerceIn(0.0f, 100.0f - playerWidth)
    }

    private fun moveEntities() {
        entities.forEach { it.x += entityDirection * entitySpeed }

        val leftMost = entities.minOfOrNull { it.x } ?: 0f
        val rightMost = entities.maxOfOrNull { it.x } ?: 100f

        if (leftMost - entitySize / 2 <= 0 || rightMost + entitySize / 2 >= 100) {
            entityDirection *= -1
            entities.forEach { it.y += entityDropDistance }
        }
    }

    private fun onBulletEntityCollision() {
        val iterator = entities.iterator()
        while (iterator.hasNext()) {
            val entity = iterator.next()
            bullets.forEach { bullet ->
                if ((bullet.x - entity.x).absoluteValue < (entitySize / 2 + 0.5f) &&
                    (bullet.y - entity.y).absoluteValue < (entitySize / 2 + 1.5f)
                ) {
                    iterator.remove()
                    bullets.remove(bullet)
                    score += 100
                    return
                }
            }
        }
    }

    private fun checkPlayerEntityCollision() {
        for (entity in entities) {
            if ((entity.x - entitySize / 2 - player.x).absoluteValue < (entitySize / 2 + playerWidth / 2) &&
                (entity.y - entitySize / 2 - player.y).absoluteValue < (entitySize / 2 + 2.5f)
            ) {
                start()
                return
            }
        }
    }

    override fun onTick() {
        checkKeyPresses()

        bullets.forEach { it.y -= bulletSpeed }

        bullets.removeIf { it.y.toDouble() < 0 }

        moveEntities()

        onBulletEntityCollision()

        checkPlayerEntityCollision()

        if (entities.isEmpty()) {
            generateEntities()
        }
    }

    override fun getDisplay(): UIComponent {
        return UIBlock().constrain {
            x = 0.percent()
            y = 0.percent()
            width = 75.percent()
            height = 75.percent()
            color = Color(0x1e1f1e).toConstraint()
        }.apply {
            UIBlock().constrain {
                x = player.x.percent()
                y = player.y.percent()
                width = playerWidth.percent()
                height = 5.percent()
                color = Color.WHITE.toConstraint()
            } childOf this

            entities.forEach {
                UIBlock().constrain {
                    x = it.x.percent() - (entitySize / 2).percent()
                    y = it.y.percent() - (entitySize / 2).percent()
                    width = entitySize.percent()
                    height = entitySize.percent()
                    color = Color.WHITE.toConstraint()
                } childOf this
            }

            bullets.forEach {
                UIBlock().constrain {
                    x = it.x.percent()
                    y = it.y.percent()
                    width = 1.percent()
                    height = 3.percent()
                    color = Color.WHITE.toConstraint()
                } childOf this
            }

            UIText("Score: $score").constrain {
                x = CenterConstraint()
                y = 1.percent()
                height = 10.percent()
                color = Color.WHITE.toConstraint()
            } childOf this
        }
    }

    override val name = "Space Invaders"

    override val supportsPictureInPicture = true
}
