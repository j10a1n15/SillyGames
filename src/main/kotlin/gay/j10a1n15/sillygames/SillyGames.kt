package gay.j10a1n15.sillygames

import gay.j10a1n15.sillygames.commands.CommandManager
import gay.j10a1n15.sillygames.events.EventHandler
import gay.j10a1n15.sillygames.events.handler.Events
import gay.j10a1n15.sillygames.games.GameManager
import gay.j10a1n15.sillygames.rpc.RpcManager
import gay.j10a1n15.sillygames.screens.PictureInPicture
import net.minecraftforge.common.MinecraftForge.EVENT_BUS
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent

@Mod(
    modid = SillyGames.MODID,
    version = SillyGames.VERSION,
    name = SillyGames.NAME,
    clientSideOnly = true,
)
class SillyGames {
    companion object {
        const val MODID = "sillygames"
        const val VERSION = "1.0.0"
        const val NAME = "Silly Games"

        fun registerEvent(obj: Any) {
            runCatching { EVENT_BUS.register(obj) }.onFailure { it.printStackTrace() }
            runCatching { Events.register(obj) }.onFailure { it.printStackTrace() }
        }
    }

    @Mod.EventHandler
    fun preInit(event: FMLPreInitializationEvent) {
        println("Hi")

        CommandManager()

        listOf(
            EventHandler,
            GameManager,
            PictureInPicture,
        ).forEach {
            registerEvent(it)
        }

        RpcManager.start()
    }
}
