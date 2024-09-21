package gay.j10a1n15.sillygames.commands


import gay.j10a1n15.sillygames.screens.GameSelector
import gay.j10a1n15.sillygames.screens.PictureInPicture
import gay.j10a1n15.sillygames.utils.SillyUtils.display
import net.minecraft.command.ICommandSender
import net.minecraft.util.BlockPos
import net.minecraftforge.client.ClientCommandHandler

class CommandManager {

    init {
        registerCommand("games") {
            GameSelector().display()
        }
        registerCommand("pip") {
            PictureInPicture.visible = !PictureInPicture.visible
        }
    }

    private fun registerCommand(name: String, function: (Array<String>) -> Unit) {
        ClientCommandHandler.instance.registerCommand(SimpleCommand(name, createCommand(function)))
    }

    @Suppress("unused")
    private fun registerCommand0(
        name: String,
        function: (Array<String>) -> Unit,
        autoComplete: ((Array<String>) -> List<String>) = { listOf() },
    ) {
        val command = SimpleCommand(
            name,
            createCommand(function),
            object : SimpleCommand.TabCompleteRunnable {
                override fun tabComplete(sender: ICommandSender?, args: Array<String>?, pos: BlockPos?): List<String> {
                    return autoComplete(args ?: emptyArray())
                }
            },
        )
        ClientCommandHandler.instance.registerCommand(command)
    }

    private fun createCommand(function: (Array<String>) -> Unit) = object : SimpleCommand.ProcessCommandRunnable() {
        override fun processCommand(sender: ICommandSender?, args: Array<String>?) {
            if (args != null) function(args.asList().toTypedArray())
        }
    }
}
