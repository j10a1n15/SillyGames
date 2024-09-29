package gay.j10a1n15.sillygames.utils.essentials.config.info

import gg.essential.vigilance.data.PropertyInfo
import gg.essential.vigilance.gui.settings.SettingComponent

/**
 * Nothing special, just name and description
 */
class InfoProperty : PropertyInfo() {
    override fun createSettingComponent(initialValue: Any?): SettingComponent {
        return InfoPropertyComponent()
    }
}
