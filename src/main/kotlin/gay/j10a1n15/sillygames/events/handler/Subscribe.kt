package gay.j10a1n15.sillygames.events.handler

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class Subscribe(
    val receiveCancelled: Boolean = false,
)
