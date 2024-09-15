package gay.j10a1n15.sillygames.utils

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiScreen

object SillyUtils {

    fun displayGuiScreen(guiScreen: GuiScreen) {
        Thread { Minecraft.getMinecraft().addScheduledTask { Minecraft.getMinecraft().displayGuiScreen(guiScreen) } }.start()
    }

}
