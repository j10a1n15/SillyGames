package gay.j10a1n15.sillygames.utils

import gay.j10a1n15.sillygames.utils.vectors.Vector2f
import gg.essential.universal.UKeyboard
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiScreen

object SillyUtils {

    val minecraft get() = Minecraft.getMinecraft()

    fun GuiScreen.display() = Thread { minecraft.addScheduledTask { minecraft.displayGuiScreen(this) } }.start()

    fun String.replaceAt(index: Int, replacement: Char) = StringBuilder(this).apply { setCharAt(index, replacement) }.toString()

    @Suppress("DEPRECATION")
    fun Int.getKeyCodeName() = UKeyboard.getKeyName(this, -1)

    fun Int.isKeyDown() = UKeyboard.isKeyDown(this)

    fun isAABBCollision(
        object1Pos: Vector2f, object1Width: Float, object1Height: Float,
        object2Pos: Vector2f, object2Width: Float, object2Height: Float,
    ): Boolean {
        val object1Left = object1Pos.x - object1Width / 2
        val object1Right = object1Pos.x + object1Width / 2
        val object1Top = object1Pos.y - object1Height / 2
        val object1Bottom = object1Pos.y + object1Height / 2

        val object2Left = object2Pos.x - object2Width / 2
        val object2Right = object2Pos.x + object2Width / 2
        val object2Top = object2Pos.y - object2Height / 2
        val object2Bottom = object2Pos.y + object2Height / 2

        return (object1Left < object2Right && object1Right > object2Left) &&
            (object1Top < object2Bottom && object1Bottom > object2Top)
    }

    fun <T> List<T>.pad(amount: Int, left: Boolean = false, factory: () -> T): List<T> {
        val result = ArrayList<T>(this.size + amount)
        if (left) {
            repeat(amount) { result.add(factory()) }
        }
        result.addAll(this)
        if (!left) {
            repeat(amount) { result.add(factory()) }
        }
        return result
    }
}
