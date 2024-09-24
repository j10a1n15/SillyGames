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

    private var playerPosition = Vector2df(50.0f, 90.0f)
    private val playerSpeed = 1.5f
    private var score = 0
    private val numEntitiesPerRow = 11
    private val numEntityRows = 6
    private val entities = mutableListOf<Vector2df>()
    private val bullets = mutableMapOf<Vector2df, BulletType>()
    private var canShoot = true

    private val playerWidth = 5f
    private val bulletSpeed = 5f
    private val entitySize = 5f
    private var entityDirection = 1
    private val entitySpeed = 0.2f
    private val entityDropDistance = 5f
    private val entityBulletCooldown = 2000
    private var lastEntityShotTime = System.currentTimeMillis()

    private fun resetGame() {
        playerPosition = Vector2df(50.0f, 90.0f)
        entities.clear()
        bullets.clear()
        score = 0
        entityDirection = 1
        lastEntityShotTime = System.currentTimeMillis()
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

    private fun handleInput() {
        when {
            UKeyboard.KEY_A.isKeyDown() -> playerPosition.x -= playerSpeed
            UKeyboard.KEY_D.isKeyDown() -> playerPosition.x += playerSpeed
            UKeyboard.KEY_SPACE.isKeyDown() && canShoot -> {
                bullets[Vector2df(playerPosition.x + playerWidth / 2, 90.0f)] = BulletType.PLAYER
                canShoot = false
            }

            !UKeyboard.KEY_SPACE.isKeyDown() -> canShoot = true
        }

        playerPosition.x = playerPosition.x.coerceIn(0.0f, 100.0f - playerWidth)
    }

    private fun moveEntities() {
        entities.forEach { it.x += entityDirection * entitySpeed }
        adjustEntityDirectionIfNeeded()
    }

    private fun adjustEntityDirectionIfNeeded() {
        val leftMost = entities.minOfOrNull { it.x } ?: 0f
        val rightMost = entities.maxOfOrNull { it.x } ?: 100f

        if (leftMost - entitySize / 2 <= 0 || rightMost + entitySize / 2 >= 100) {
            entityDirection *= -1
            entities.forEach { it.y += entityDropDistance }
        }
    }

    private fun checkBulletCollisions() {
        val bulletIterator = bullets.iterator()
        while (bulletIterator.hasNext()) {
            val bullet = bulletIterator.next()
            val entityIterator = entities.iterator()

            while (entityIterator.hasNext()) {
                val entity = entityIterator.next()
                if (isCollision(bullet.key, entity)) {
                    bulletIterator.remove()
                    entityIterator.remove()
                    score += 100
                    break
                }
            }
        }
    }

    private fun isCollision(bullet: Vector2df, entity: Vector2df): Boolean {
        return (bullet.x - entity.x).absoluteValue < (entitySize / 2 + 0.5f) &&
            (bullet.y - entity.y).absoluteValue < (entitySize / 2 + 1.5f)
    }

    private fun checkPlayerCollisions() {
        for (entity in entities) {
            if (isPlayerHit(entity)) {
                resetGame()
                return
            }
        }
        for (bullet in bullets) {
            if (bullet.value == BulletType.ENEMY && isPlayerHit(bullet.key)) {
                resetGame()
                return
            }
        }
    }

    private fun isPlayerHit(entity: Vector2df): Boolean {
        return (entity.x - entitySize / 2 - playerPosition.x).absoluteValue < (entitySize / 2 + playerWidth / 2) &&
            (entity.y - entitySize / 2 - playerPosition.y).absoluteValue < (entitySize / 2 + 2.5f)
    }

    private fun handleEnemyShooting() {
        val currentTime = System.currentTimeMillis()

        if (currentTime - lastEntityShotTime >= entityBulletCooldown) {
            if ((0..50).random() != 0) return
            if (entities.isNotEmpty()) {
                val shootingEntity = entities.filter { it.y == entities.maxOf { y -> y.y } }.randomOrNull()
                shootingEntity?.let {
                    bullets[Vector2df(it.x + entitySize / 2, it.y + entitySize)] = BulletType.ENEMY
                    lastEntityShotTime = currentTime
                }
            }
        }
    }

    override fun onTick() {
        handleInput()
        updateBullets()
        moveEntities()
        checkBulletCollisions()
        checkPlayerCollisions()
        handleEnemyShooting()

        if (entities.isEmpty()) {
            generateEntities()
        }
    }

    private fun updateBullets() {
        bullets.forEach { (position, type) ->
            when (type) {
                BulletType.PLAYER -> position.y -= bulletSpeed
                BulletType.ENEMY -> position.y += bulletSpeed
            }

            if (position.y < 0 || position.y > 100) {
                bullets.remove(position)
            }
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
            addPlayer()
            addEntities()
            addBullets()
            addScore()
        }
    }

    private fun UIComponent.addPlayer() {
        UIBlock().constrain {
            x = playerPosition.x.percent()
            y = playerPosition.y.percent()
            width = playerWidth.percent()
            height = 5.percent()
            color = Color.WHITE.toConstraint()
        } childOf this
    }

    private fun UIComponent.addEntities() {
        entities.forEach { entity ->
            UIBlock().constrain {
                x = entity.x.percent() - (entitySize / 2).percent()
                y = entity.y.percent() - (entitySize / 2).percent()
                width = entitySize.percent()
                height = entitySize.percent()
                color = Color.WHITE.toConstraint()
            } childOf this
        }
    }

    private fun UIComponent.addBullets() {
        bullets.forEach { (position, type) ->
            UIBlock().constrain {
                x = position.x.percent()
                y = position.y.percent()
                width = 1.percent()
                height = 3.percent()
                color = (if (type == BulletType.PLAYER) Color.GREEN else Color.RED).toConstraint()
            } childOf this
        }
    }

    private fun UIComponent.addScore() {
        UIText("Score: $score").constrain {
            x = CenterConstraint()
            y = 1.percent()
            height = 10.percent()
            color = Color.WHITE.toConstraint()
        } childOf this
    }

    override val name = "Space Invaders"
    override val supportsPictureInPicture = true

    private enum class BulletType {
        PLAYER,
        ENEMY,
    }
}
