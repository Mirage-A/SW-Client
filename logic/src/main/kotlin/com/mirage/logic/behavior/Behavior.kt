package com.mirage.logic.behavior

import com.mirage.core.utils.EntityID
import com.mirage.core.utils.IntervalMillis
import com.mirage.logic.data.LogicData
import org.luaj.vm2.LuaValue

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