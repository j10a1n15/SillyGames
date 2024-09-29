package gay.j10a1n15.sillygames.events

import gg.essential.universal.UMatrixStack
import net.minecraftforge.client.event.GuiScreenEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent
import org.lwjgl.input.Keyboard

object EventHandler {

    @SubscribeEvent
    fun onRender(event: RenderTickEvent) {
        if (event.phase == Phase.START) return
        RenderEvent(UMatrixStack.Compat.get()).post()
    }

    @SubscribeEvent
    fun onTick(event: ClientTickEvent) {
        if (event.phase != Phase.START) TickEvent().post()
    }

    @SubscribeEvent
    fun onKeyPressed(event: GuiScreenEvent.KeyboardInputEvent.Pre) {
        if (Keyboard.getEventKeyState() && Keyboard.getEventKey() != 0) {
            val key = Keyboard.getEventKey()
            if (KeyDownEvent(key).post()) {
                event.isCanceled = true
            }
        }
    }
}
