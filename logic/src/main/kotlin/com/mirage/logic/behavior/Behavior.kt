package com.mirage.logic.behavior

import com.mirage.core.game.objects.SimplifiedEntity
import com.mirage.core.game.objects.SimplifiedObject
import com.mirage.core.utils.EntityID
import com.mirage.core.utils.IntervalMillis
import com.mirage.logic.data.LogicData
import org.luaj.vm2.LuaValue
import kotlin.math.abs
import kotlin.math.atan

/**
 * Interface, encapsulating entity's behavior.
 * Defines actions which entity can perform on each logic loop tick and on taking damage.
 */
internal interface Behavior {

    /** This method is invoked on each logic loop tick */
    fun onUpdate(delta: IntervalMillis, data: LogicData, coercedScriptActions: LuaValue)

    /** This method is invoked every time this entity must take damage. Health should be changed in this method. */
    fun onDamage(damage: Int, source: EntityID, data: LogicData, coercedScriptActions: LuaValue)

}

private const val visionAngle = Math.PI * 2f / 3f

/** Checks if receiver entity looks 'forward' other object, i.e. it can attack that object */
internal fun SimplifiedEntity.looksForward(other: SimplifiedObject): Boolean {
    val moveAngle = this.moveDirection.toMoveAngle()
    val lookVector = other.position - this.position
    val lookTan = lookVector.y / if (lookVector.x != 0f) lookVector.x else 0.01f
    val lookAngle = atan(lookTan) + Math.PI * if (lookVector.x < 0f) 1f else 0f
    return abs(moveAngle - lookAngle) < visionAngle / 2f || abs(moveAngle - lookAngle - Math.PI * 2) < visionAngle / 2f ||
            abs(moveAngle - lookAngle + Math.PI * 2f) < visionAngle / 2f
}