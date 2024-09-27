package gay.j10a1n15.sillygames.utils.essentials

import gg.essential.elementa.UIComponent
import gg.essential.elementa.constraints.RelativeConstraint

object ElementaUtils {
    infix fun RelativeConstraint.constrainTo(constraint: UIComponent) = apply {
        this.constrainTo = constraint
    }
}
