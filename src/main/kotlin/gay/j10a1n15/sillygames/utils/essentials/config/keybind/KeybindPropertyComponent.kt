package gay.j10a1n15.sillygames.utils.essentials.config.keybind

import gay.j10a1n15.sillygames.events.Events
import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.UIText
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.dsl.childOf
import gg.essential.elementa.dsl.constrain
import gg.essential.elementa.dsl.pixels
import gg.essential.elementa.dsl.toConstraint
import gg.essential.elementa.utils.withAlpha
import gg.essential.vigilance.gui.DataBackedSetting
import gg.essential.vigilance.gui.settings.SettingComponent
import org.lwjgl.input.Keyboard
import java.awt.Color

class KeybindPropertyComponent(initialValue: Int) : SettingComponent() {

    private var listeningForKey = false
    private var currentKey: Int = initialValue
    private val keyDisplay: UIText
    private val container: UIBlock

    init {
        container = UIBlock().constrain {
            x = (DataBackedSetting.INNER_PADDING + 10f).pixels(alignOpposite = true)
            y = CenterConstraint()
            width = 100.pixels()
            height = 20.pixels()
            color = Color.BLACK.withAlpha(0.25f).toConstraint()
        }.onMouseEnter {
            startListeningForKey()
        }.onMouseLeave {
            stopListeningForKey()
        } as UIBlock childOf this

        keyDisplay = UIText(getKeyName(currentKey)).constrain {
            x = CenterConstraint()
            y = CenterConstraint()
        } childOf container


        Events.KEYBOARD_DOWN.register { onKeyTyped(it) }
    }

    private fun startListeningForKey() {
        listeningForKey = true
        keyDisplay.setText("Press a key...")
    }

    private fun onKeyTyped(keyCode: Int): Boolean {
        if (listeningForKey && keyCode != Keyboard.KEY_NONE) {
            setKeybind(keyCode)
            stopListeningForKey()
            return true
        }
        return false
    }

    private fun setKeybind(keyCode: Int) {
        currentKey = keyCode
        keyDisplay.setText(getKeyName(keyCode))
        changeValue(currentKey)
    }

    private fun stopListeningForKey() {
        listeningForKey = false
        keyDisplay.setText(getKeyName(currentKey))
    }

    private fun getKeyName(keyCode: Int): String {
        return Keyboard.getKeyName(keyCode) ?: "None"
    }
}
