package gay.j10a1n15.sillygames

import gay.j10a1n15.sillygames.commands.CommandManager
import gay.j10a1n15.sillygames.events.EventHandler
import gay.j10a1n15.sillygames.rpc.RpcManager
import gay.j10a1n15.sillygames.screens.PictureInPicture
import net.fabricmc.api.ClientModInitializer

class SillyGames : ClientModInitializer {
    companion object {
        const val MODID = "sillygames"
        const val VERSION = "1.0.0"
        const val NAME = "Silly Games"
    }

    override fun onInitializeClient() {
        println("Hi")

        CommandManager()

        listOf(
            EventHandler,
            //PictureInPicture,
        ).forEach {

        }

        RpcManager.start()
    }
}
