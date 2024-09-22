package gay.j10a1n15.sillygames.rpc

import com.google.gson.JsonObject
import com.jagrosh.discordipc.IPCClient
import com.jagrosh.discordipc.IPCListener
import com.jagrosh.discordipc.entities.RichPresence
import com.jagrosh.discordipc.entities.pipe.PipeStatus
import gay.j10a1n15.sillygames.games.GameManager
import gay.j10a1n15.sillygames.utils.Scheduling
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ScheduledFuture
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

private const val CLIENT_ID = 1287128195124039715L

object RpcManager : IPCListener {

    private var client: IPCClient? = null
    private var lastInfo: RpcInfo? = null
    private var scheduler: ScheduledFuture<*>? = null
    private var started: Boolean = false

    fun start() {
        if (started) return
        CompletableFuture.runAsync {
            client = IPCClient(CLIENT_ID)
            client?.setListener(this)
            client?.connect()
        }
    }

    fun stop() {
        if (client?.status != PipeStatus.CONNECTED) return
        CompletableFuture.runAsync {
            close()
        }
    }

    override fun onReady(client: IPCClient?) {
        scheduler = Scheduling.schedule(Duration.ZERO, 1.seconds) {
            val info = (GameManager.game as? RpcProvider)?.getRpcInfo()
            if (info != lastInfo) {
                lastInfo = info?.copy()
                updatePresence()
            }
        }
    }

    override fun onClose(client: IPCClient?, json: JsonObject?) = close()
    override fun onDisconnect(client: IPCClient?, t: Throwable?) = close()

    private fun close() {
        scheduler?.cancel(true)
        scheduler = null
        started = false
        client = null
    }

    private fun updatePresence() {
        val client = client ?: return
        if (lastInfo == null) {
            client.sendRichPresence(null)
        } else {
            val info = lastInfo!!
            client.sendRichPresence(RichPresence.Builder().apply {
                setDetails(info.firstLine)
                setState(info.secondLine)
                setStartTimestamp(info.start)
                info.end?.let { setEndTimestamp(it) }
                info.thumbnail?.let { setLargeImage(it) }
                info.icon?.let { setSmallImage(it) }
            }.build())
        }
    }

}
