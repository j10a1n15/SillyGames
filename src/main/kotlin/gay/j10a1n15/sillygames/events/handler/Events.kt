package gay.j10a1n15.sillygames.events.handler

import org.apache.logging.log4j.LogManager
import java.lang.reflect.Method

object Events {

    private val handlers: MutableMap<Class<*>, EventHandler<*>> = mutableMapOf()
    private val logger = LogManager.getLogger("SillyGames Events")

    @Suppress("UNCHECKED_CAST")
    private fun <T : SillyEvent> getHandler(event: Class<T>): EventHandler<T> {
        return handlers.getOrPut(event) { EventHandler<T>(logger) } as EventHandler<T>
    }

    @Suppress("UNCHECKED_CAST")
    private fun register(method: Method, instance: Any): SubscribeResponse? {
        val subscribe = method.getAnnotation(Subscribe::class.java) ?: return null
        if (method.parameterCount == 0) return SubscribeResponse.NO_PARAMETERS
        if (method.parameterCount > 1) return SubscribeResponse.TOO_MANY_PARAMETERS
        val eventClass = method.parameterTypes[0]
        if (!SillyEvent::class.java.isAssignableFrom(eventClass)) return SubscribeResponse.PARAMETER_NOT_EVENT
        getHandler(eventClass as Class<SillyEvent>).register(method, instance, subscribe)
        return SubscribeResponse.SUCCESS
    }

    fun register(instance: Any) {
        var hadEvent = false
        instance.javaClass.declaredMethods.forEach { method ->
            val response = register(method, instance) ?: return@forEach
            hadEvent = true
            when (response) {
                SubscribeResponse.NO_PARAMETERS, SubscribeResponse.TOO_MANY_PARAMETERS -> {
                    logger.warn("Event subscription on ${method.name} has an incorrect number of parameters (${method.parameterCount})")
                }

                SubscribeResponse.PARAMETER_NOT_EVENT -> {
                    logger.warn("Event subscription on ${method.name} does not have a parameter that extends ${SillyEvent::class.java.name}")
                }

                SubscribeResponse.SUCCESS -> {}
            }
        }
        if (!hadEvent) {
            logger.warn("No events found in ${instance.javaClass.name}")
        }
    }

    fun post(event: SillyEvent) {
        getHandler(event.javaClass).post(event)
    }

    private enum class SubscribeResponse {
        NO_PARAMETERS,
        TOO_MANY_PARAMETERS,
        PARAMETER_NOT_EVENT,
        SUCCESS
    }
}
