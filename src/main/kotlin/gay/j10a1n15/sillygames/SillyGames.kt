package gay.j10a1n15.sillygames

import gay.j10a1n15.sillygames.commands.CommandManager
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
    }

    @Mod.EventHandler
    fun preInit(event: FMLPreInitializationEvent) {
        println("Hi")

        CommandManager()

        /*listOf(

        ).forEach {
            MinecraftForge.EVENT_BUS.register(it)
        }*/
    }
}
