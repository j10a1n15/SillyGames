package gay.j10a1n15.sillygames.events

import gg.essential.universal.UMatrixStack

object Events {
    val RENDER = EventType<UMatrixStack>()
    val TICK = EventType<Unit>()
    val KEYBOARD = EventType<Int>()
    val KEYBOARD_DOWN = CancellableEventType<Int>()
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

class CancellableEventType<T> {

    private val listeners = mutableListOf<(T) -> Boolean>()

    fun register(listener: (T) -> Boolean) {
        listeners.add(listener)
    }

    fun post(event: T): Boolean {
        var cancelled = false
        listeners.forEach {
            if (it(event)) {
                cancelled = true
            }
        }
        return cancelled
    }
}
