package gay.j10a1n15.sillygames.events

import gg.essential.universal.UMatrixStack

object Events {
    val RENDER = EventType<UMatrixStack>()
    val TICK = EventType<Unit>()
}

class EventType<T> {

    private val listeners = mutableListOf<(T) -> Unit>()

    fun register(listener: (T) -> Unit) {
        listeners.add(listener)
    }

    fun post(event: T) {
        listeners.forEach { it(event) }
    }

}
