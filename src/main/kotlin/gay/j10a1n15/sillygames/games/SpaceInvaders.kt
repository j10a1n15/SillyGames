package gay.j10a1n15.sillygames.games

import gay.j10a1n15.sillygames.utils.SillyUtils.isAABBCollision
import gay.j10a1n15.sillygames.utils.SillyUtils.isKeyDown
import gay.j10a1n15.sillygames.utils.vectors.Vector2f
import gg.essential.elementa.UIComponent
import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.UIText
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.constraints.PixelConstraint
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
    private var lives = 3
    private var score = 0
    private var phase = 1
    private val numEntitiesPerRow = 11
    private val numEntityRows = 5
    private val entities = mutableListOf<Vector2f>()
    private val playerBullets = mutableListOf<Vector2f>()
    private var canShoot = true

    private val playerHeight = 5f
    private val playerWidth = 5f
    private val playerBulletSpeed = 5f
    private val entityBulletSpeed = 2f
    private val bulletHeight = 3f
    private val bulletWidth = 1f
    private val entitySize = 5f
    private var entityDirection = 1
    private var entitySpeed = 0.2f
    private val entityDropDistance = 5f
    private val entityBullets = mutableListOf<Vector2f>()
    private val entityBulletCooldown = 1000
    private var lastEntityShotTime = System.currentTimeMillis()

    /**
     * Resets the game to its initial state
     */
    private fun resetGame() {
        score = 0
        phase = 1
        lives = 3
        restartCurrentGame()
    }

    /**
     * Restarts the current game
     */
    private fun restartCurrentGame() {
        playerPosition = Vector2f(50.0f, 90.0f)
        entities.clear()
        playerBullets.clear()
        entityBullets.clear()
        entitySpeed = 0.2f
        entityDirection = 1
        lastEntityShotTime = System.currentTimeMillis()
        generateEntities()
    }

    private fun lifeLost() {
        lives -= 1
        if (lives == 0) resetGame()
        else restartCurrentGame()
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

        playerPosition.x = playerPosition.x.coerceIn(0.0f + playerWidth / 2, 100.0f - playerWidth / 2)
    }


    private fun moveEntities() {
        entities.forEach { it.x += entityDirection * entitySpeed }
        adjustEntityDirectionIfNeeded()
    }

    private fun adjustEntityDirectionIfNeeded() {
        val leftMost = entities.minOfOrNull { it.x } ?: 0.5f
        val rightMost = entities.maxOfOrNull { it.x } ?: 99.5f

        if (leftMost - entitySize / 2 <= 0.5 || rightMost + entitySize / 2 >= 99.5) {
            entityDirection *= -1
            entities.forEach { it.y += entityDropDistance }
            if (entities.any { it.y + entitySize >= playerPosition.y }) {
                resetGame()
            }
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
                lifeLost()
                return
            }
        }
    }

    private fun handleEnemyShooting() {
        val currentTime = System.currentTimeMillis()

        if (currentTime - lastEntityShotTime >= entityBulletCooldown) {
            if ((0..40).random() != 0) return
            if (entities.isNotEmpty()) {
                entities.randomOrNull()?.let {
                    entityBullets.add(Vector2f(it.x, it.y + entitySize))
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
            bullet.y -= playerBulletSpeed

            if (bullet.y < 0) {
                playerIterator.remove()
            }
        }

        val entityIterator = entityBullets.iterator()
        while (entityIterator.hasNext()) {
            val bullet = entityIterator.next()
            bullet.y += entityBulletSpeed

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
            addBullets()
            addPlayer()
            addEntities()
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
            y = 1.percent()
            width = 100.percent()
            height = 5.percent()
            color = Color.BLACK.invisible().toConstraint()
        }.apply {
            addPhase()
            addScore()
            addLives()
        } childOf this
    }

    private fun UIComponent.addPhase() {
        UIText("Phase: $phase").constrain {
            x = PixelConstraint(10f)
        } childOf this
    }

    private fun UIComponent.addScore() {
        UIText("Score: $score").constrain {
            x = CenterConstraint()
        } childOf this
    }

    private fun UIComponent.addLives() {
        UIText("Lives: $lives").constrain {
            x = PixelConstraint(10f, alignOpposite = true)
        } childOf this
    }

    override val name = "Space Invaders"
    override val supportsPictureInPicture = true
}
