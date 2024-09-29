package gay.j10a1n15.sillygames.events.handler

abstract class SillyEvent protected constructor() {

    var isCancelled = false
        private set

    open fun post(): Boolean {
        Events.post(this)
        return isCancelled
    }

    interface Cancellable {
        fun cancel() {
            (this as SillyEvent).isCancelled = true
        }
    }
}
