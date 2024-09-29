package gay.j10a1n15.sillygames.events

import gay.j10a1n15.sillygames.events.handler.SillyEvent

class KeyDownEvent(val key: Int) : SillyEvent(), SillyEvent.Cancellable
