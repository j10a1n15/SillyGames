package gay.j10a1n15.sillygames.games

import gg.essential.elementa.components.UIText
import gg.essential.elementa.dsl.constrain
import gg.essential.elementa.dsl.percent

class Snake : Game() {

    override fun getDisplay() = UIText("balls").constrain {
        x = 50.percent
        y = 50.percent
    }

}
