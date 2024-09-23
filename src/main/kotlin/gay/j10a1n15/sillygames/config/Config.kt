package gay.j10a1n15.sillygames.config

import gay.j10a1n15.sillygames.utils.essentials.InfoProperty
import gg.essential.vigilance.Vigilant
import gg.essential.vigilance.data.Property
import gg.essential.vigilance.data.PropertyType
import java.io.File

object Config : Vigilant(File("./config/sillygames.toml")) {
    @Property(
        type = PropertyType.CUSTOM,
        name = "Information",
        description = "This is the configuration file for Silly Games. You can change settings here.",
        category = "About",
        customPropertyInfo = InfoProperty::class,
    )
    @Suppress("unused")
    var informationAbout = ""

    init {
        initialize()

        setCategoryDescription("About", "Information about the mod.")
    }
}
