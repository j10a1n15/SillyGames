package gay.j10a1n15.sillygames.utils

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiScreen

object SillyUtils {

    val minecraft get() = Minecraft.getMinecraft()

    fun GuiScreen.display() = Thread { minecraft.addScheduledTask { minecraft.displayGuiScreen(this) } }.start()

}
