package gay.j10a1n15.sillygames.utils.essentials.keybind

import gg.essential.vigilance.data.PropertyInfo
import gg.essential.vigilance.gui.settings.SettingComponent

class KeybindProperty : PropertyInfo() {
    override fun createSettingComponent(initialValue: Any?): SettingComponent {
        return KeybindPropertyComponent(initialValue as Int)
    }
}
