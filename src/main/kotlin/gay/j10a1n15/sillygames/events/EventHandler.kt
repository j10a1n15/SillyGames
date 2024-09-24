package gay.j10a1n15.sillygames.events

import gg.essential.universal.UMatrixStack
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiChat
import net.minecraftforge.client.event.GuiScreenEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import org.lwjgl.input.Keyboard
import org.lwjgl.input.Mouse

object EventHandler {

    @SubscribeEvent
    fun onRender(event: TickEvent.RenderTickEvent) {
        if (event.phase == TickEvent.Phase.START) return
        Events.RENDER.post(UMatrixStack.Compat.get())
    }

    @SubscribeEvent
    fun onTick(event: TickEvent.ClientTickEvent) {
        if (event.phase != TickEvent.Phase.START) Events.TICK.post(Unit)

        val currentScreen = Minecraft.getMinecraft().currentScreen
        if (currentScreen == GuiChat()) return

        if (Mouse.getEventButtonState() && Mouse.getEventButton() != -1) {
            val key = Mouse.getEventButton() - 100
            Events.KEYBOARD.post(key)
            return
        }

        if (Keyboard.getEventKeyState() && Keyboard.getEventKey() != 0) {
            val key = Keyboard.getEventKey()
            Events.KEYBOARD.post(key)
            return
        }
    }

    @SubscribeEvent
    fun onKeyPressed(event: GuiScreenEvent.KeyboardInputEvent.Pre) {
        if (Keyboard.getEventKeyState() && Keyboard.getEventKey() != 0) {
            val key = Keyboard.getEventKey()
            if (Events.KEYBOARD_DOWN.post(key)) {
                event.isCanceled = true
            }
        }
    }
}
