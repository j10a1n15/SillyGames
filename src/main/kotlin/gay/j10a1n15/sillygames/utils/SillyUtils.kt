package gay.j10a1n15.sillygames.utils

import gg.essential.universal.UKeyboard
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiScreen

object SillyUtils {

    val minecraft get() = Minecraft.getMinecraft()

    fun GuiScreen.display() = Thread { minecraft.addScheduledTask { minecraft.displayGuiScreen(this) } }.start()

    fun String.replaceAt(index: Int, replacement: Char) = StringBuilder(this).apply { setCharAt(index, replacement) }.toString()

    @Suppress("DEPRECATION")
    fun Int.getKeyCodeName() = UKeyboard.getKeyName(this, -1)
}
