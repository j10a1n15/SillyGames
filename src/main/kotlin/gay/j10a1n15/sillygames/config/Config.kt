package gay.j10a1n15.sillygames.config

import gay.j10a1n15.sillygames.utils.essentials.info.InfoProperty
import gay.j10a1n15.sillygames.utils.essentials.keybind.KeybindProperty
import gg.essential.vigilance.Vigilant
import gg.essential.vigilance.data.Property
import gg.essential.vigilance.data.PropertyType
import java.io.File

object Config : Vigilant(File("./config/sillygames.toml")) {
    /**
     * --------------------------------------------------
     * |                    About                       |
     * --------------------------------------------------
     */
    @Property(
        type = PropertyType.CUSTOM,
        name = "Information",
        description = "This is the configuration file for Silly Games. You can change settings here.",
        category = "About",
        customPropertyInfo = InfoProperty::class,
    )
    @Suppress("unused")
    var informationAbout = ""


    /**
     * --------------------------------------------------
     * |                   Keybinds                     |
     * --------------------------------------------------
     */

    @Property(
        type = PropertyType.CUSTOM,
        name = "Keybind Information",
        description = "Primary and Secondary Keybinds work if you have a silly game as fullscreen.\n" +
            "Secondary Keybinds are for when you have a game in Picture in Picture mode.",
        category = "Keybinds",
        customPropertyInfo = InfoProperty::class,
    )
    @Suppress("unused")
    var keybindInformation = ""

    @Property(
        type = PropertyType.CUSTOM,
        name = "Keybind Up",
        description = "Keybinds for Silly Games.",
        category = "Keybinds",
        subcategory = "Primary",
        customPropertyInfo = KeybindProperty::class,
    )
    var keybindUp = 17

    @Property(
        type = PropertyType.CUSTOM,
        name = "Keybind Down",
        description = "Keybinds for Silly Games.",
        category = "Keybinds",
        subcategory = "Primary",
        customPropertyInfo = KeybindProperty::class,
    )
    var keybindDown: Int = 31

    @Property(
        type = PropertyType.CUSTOM,
        name = "Keybind Left",
        description = "Keybinds for Silly Games.",
        category = "Keybinds",
        subcategory = "Primary",
        customPropertyInfo = KeybindProperty::class,
    )
    var keybindLeft = 30

    @Property(
        type = PropertyType.CUSTOM,
        name = "Keybind Right",
        description = "Keybinds for Silly Games.",
        category = "Keybinds",
        subcategory = "Primary",
        customPropertyInfo = KeybindProperty::class,
    )
    var keybindRight = 32

    @Property(
        type = PropertyType.CUSTOM,
        name = "Keybind Up",
        description = "Keybinds for Silly Games.",
        category = "Keybinds",
        subcategory = "Secondary",
        customPropertyInfo = KeybindProperty::class,
    )
    var keybindUpSecondary: Int = 200

    @Property(
        type = PropertyType.CUSTOM,
        name = "Keybind Down",
        description = "Keybinds for Silly Games.",
        category = "Keybinds",
        subcategory = "Secondary",
        customPropertyInfo = KeybindProperty::class,
    )
    var keybindDownSecondary = 208

    @Property(
        type = PropertyType.CUSTOM,
        name = "Keybind Left",
        description = "Keybinds for Silly Games.",
        category = "Keybinds",
        subcategory = "Secondary",
        customPropertyInfo = KeybindProperty::class,
    )
    var keybindLeftSecondary = 203

    @Property(
        type = PropertyType.CUSTOM,
        name = "Keybind Right",
        description = "Keybinds for Silly Games.",
        category = "Keybinds",
        subcategory = "Secondary",
        customPropertyInfo = KeybindProperty::class,
    )
    var keybindRightSecondary = 205

    init {
        initialize()

        setCategoryDescription("About", "Information about the mod.")
    }
}
