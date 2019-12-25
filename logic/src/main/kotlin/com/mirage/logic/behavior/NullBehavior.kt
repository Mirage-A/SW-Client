package com.mirage.logic.behavior

import com.mirage.core.extensions.EntityID
import com.mirage.core.extensions.IntervalMillis
import com.mirage.logic.LogicData
import org.luaj.vm2.LuaValue

/** Entity does nothing and does not react on any damage */
internal class NullBehavior : Behavior {

    override fun onUpdate(delta: IntervalMillis, data: LogicData, coercedScriptActions: LuaValue) {}

    override fun onDamage(damage: Int, source: EntityID, data: LogicData, coercedScriptActions: LuaValue) {}
}