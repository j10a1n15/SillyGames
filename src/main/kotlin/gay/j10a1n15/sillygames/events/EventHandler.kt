package gay.j10a1n15.sillygames.events

import gg.essential.universal.UMatrixStack
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent

object EventHandler {

    @SubscribeEvent
    fun onRender(event: TickEvent.RenderTickEvent) {
        if (event.phase == TickEvent.Phase.START) return
        Events.RENDER.post(UMatrixStack.Compat.get())
    }

}
