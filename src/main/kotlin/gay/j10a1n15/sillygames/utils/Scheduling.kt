package gay.j10a1n15.sillygames.utils

import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
object Scheduling {

    private val counter = AtomicInteger(0)
    private val scheduler: ScheduledExecutorService = Executors.newScheduledThreadPool(10) { target: Runnable? ->
        Thread(target, "Scheduling-Thread-${counter.getAndIncrement()}")
    }

    fun schedule(time: Duration, runnable: () -> Unit): ScheduledFuture<*> = scheduler.schedule(
        runnable,
        time.toLong(DurationUnit.MILLISECONDS),
        TimeUnit.MILLISECONDS,
    )

    fun schedule(initalDelay: Duration, delay: Duration, runnable: () -> Unit): ScheduledFuture<*> = scheduler.scheduleAtFixedRate(
        runnable,
        initalDelay.toLong(DurationUnit.MILLISECONDS),
        delay.toLong(DurationUnit.MILLISECONDS), TimeUnit.MILLISECONDS,
    )
}
