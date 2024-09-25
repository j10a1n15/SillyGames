package gay.j10a1n15.sillygames.games

import gay.j10a1n15.sillygames.utils.SillyUtils.isAABBCollision
import gay.j10a1n15.sillygames.utils.SillyUtils.isKeyDown
import gay.j10a1n15.sillygames.utils.vectors.Vector2f
import gg.essential.elementa.UIComponent
import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.UIText
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.dsl.childOf
import gg.essential.elementa.dsl.constrain
import gg.essential.elementa.dsl.minus
import gg.essential.elementa.dsl.percent
import gg.essential.elementa.dsl.toConstraint
import gg.essential.elementa.utils.invisible
import gg.essential.universal.UKeyboard
import java.awt.Color

class SpaceInvaders : Game() {

    private var playerPosition = Vector2f(50.0f, 90.0f)
    private val playerSpeed = 1.5f
    private var score = 0
    private var phase = 1
    private val numEntitiesPerRow = 11
    private val numEntityRows = 5
    private val entities = mutableListOf<Vector2f>()
    private val playerBullets = mutableListOf<Vector2f>()
    private var canShoot = true

    private val playerHeight = 5f
    private val playerWidth = 5f
    private val bulletSpeed = 5f
    private val bulletHeight = 3f
    private val bulletWidth = 1f
    private val entitySize = 5f
    private var entityDirection = 1
    private var entitySpeed = 0.2f
    private val entityDropDistance = 5f
    private val entityBullets = mutableListOf<Vector2f>()
    private val entityBulletCooldown = 2000
    private var lastEntityShotTime = System.currentTimeMillis()

    private fun resetGame() {
        playerPosition = Vector2f(50.0f, 90.0f)
        entities.clear()
        playerBullets.clear()
        entityBullets.clear()
        score = 0
        phase = 1
        entitySpeed = 0.2f
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
                entities.add(Vector2f(xPosition, yPosition))
            }
        }
    }

    private fun handleInput() {
        if (UKeyboard.KEY_A.isKeyDown()) playerPosition.x -= playerSpeed
        if (UKeyboard.KEY_D.isKeyDown()) playerPosition.x += playerSpeed
        if (UKeyboard.KEY_SPACE.isKeyDown() && canShoot) {
            playerBullets.add(Vector2f(playerPosition.x, 90.0f))
            canShoot = false
        }

        if (!UKeyboard.KEY_SPACE.isKeyDown()) canShoot = true

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
        val bulletIterator = playerBullets.iterator()
        while (bulletIterator.hasNext()) {
            val bullet = bulletIterator.next()
            val entityIterator = entities.iterator()

            while (entityIterator.hasNext()) {
                val entity = entityIterator.next()
                if (isAABBCollision(
                        bullet, bulletWidth, bulletHeight,
                        entity, entitySize, entitySize,
                    )
                ) {
                    bulletIterator.remove()
                    entityIterator.remove()
                    entitySpeed += 0.005f
                    score += 100
                    break
                }
            }
        }
    }

    private fun checkPlayerCollisions() {
        for (entity in entities) {
            if (isAABBCollision(
                    playerPosition, playerWidth, playerHeight,
                    entity, entitySize, entitySize,
                )
            ) {
                resetGame()
                return
            }
        }
        for (bullet in entityBullets) {
            if (isAABBCollision(
                    playerPosition, playerWidth, playerHeight,
                    bullet, bulletWidth, bulletHeight,
                )
            ) {
                resetGame()
                return
            }
        }
    }

    private fun handleEnemyShooting() {
        val currentTime = System.currentTimeMillis()

        if (currentTime - lastEntityShotTime >= entityBulletCooldown) {
            if ((0..50).random() != 0) return
            if (entities.isNotEmpty()) {
                val shootingEntity = entities.filter { it.y == entities.maxOf { y -> y.y } }.randomOrNull()
                shootingEntity?.let {
                    entityBullets.add(Vector2f(it.x + entitySize / 2, it.y + entitySize))
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
            phase += 1
            entitySpeed = 0.2f + (phase - 1) * 0.02f
        }
    }

    private fun updateBullets() {
        val playerIterator = playerBullets.iterator()
        while (playerIterator.hasNext()) {
            val bullet = playerIterator.next()
            bullet.y -= bulletSpeed

            if (bullet.y < 0) {
                playerIterator.remove()
            }
        }

        val entityIterator = entityBullets.iterator()
        while (entityIterator.hasNext()) {
            val bullet = entityIterator.next()
            bullet.y += bulletSpeed

            if (bullet.y > 100) {
                entityIterator.remove()
            }
        }
    }

    override fun getDisplay(): UIComponent {
        return UIBlock().constrain {
            width = 75.percent()
            height = 75.percent()
            color = Color(0x1e1f1e).toConstraint()
        }.apply {
            addPlayer()
            addEntities()
            addBullets()
            addTopBar()
        }
    }

    private fun UIComponent.addPlayer() {
        UIBlock().constrain {
            x = playerPosition.x.percent() - (playerWidth / 2).percent()
            y = playerPosition.y.percent() - (playerHeight / 2).percent()
            width = playerWidth.percent()
            height = playerHeight.percent()
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
        playerBullets.forEach { position ->
            UIBlock().constrain {
                x = position.x.percent() - (bulletWidth / 2).percent()
                y = position.y.percent() - (bulletHeight / 2).percent()
                width = bulletWidth.percent()
                height = bulletHeight.percent()
                color = Color.GREEN.toConstraint()
            } childOf this
        }

        entityBullets.forEach { position ->
            UIBlock().constrain {
                x = position.x.percent() - (bulletWidth / 2).percent()
                y = position.y.percent() - (bulletHeight / 2).percent()
                width = bulletWidth.percent()
                height = bulletHeight.percent()
                color = Color.RED.toConstraint()
            } childOf this
        }
    }

    private fun UIComponent.addTopBar() {
        UIBlock().constrain {
            x = 1.percent()
            y = 0.percent()
            width = 100.percent()
            height = 5.percent()
            color = Color.BLACK.invisible().toConstraint()
        }.apply {
            addScore()
            addPhase()
        } childOf this
    }

    private fun UIComponent.addScore() {
        UIText("Score: $score").constrain {
            x = CenterConstraint()
            y = 1.percent()
            color = Color.WHITE.toConstraint()
        } childOf this
    }

    private fun UIComponent.addPhase() {
        UIText("Phase: $phase").constrain {
            x = 1.percent()
            y = 1.percent()
            color = Color.WHITE.toConstraint()
        } childOf this
    }

    override val name = "Space Invaders"
    override val supportsPictureInPicture = true
}
