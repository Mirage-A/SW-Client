package com.mirage.logic.behavior

import com.mirage.core.extensions.EntityID
import com.mirage.core.extensions.IntervalMillis
import com.mirage.core.extensions.tableOf
import com.mirage.core.game.objects.extended.ExtendedEntity
import com.mirage.logic.data.LogicData
import com.mirage.logic.processors.runAssetScript
import org.luaj.vm2.LuaValue

/** Behavior of a dummy that does nothing, but can take damage */
internal class DummyBehavior(private val id: EntityID, data: LogicData) : Behavior {

    private val entity = data.state.entities[id] ?: ExtendedEntity()

    override fun onUpdate(delta: IntervalMillis, data: LogicData, coercedScriptActions: LuaValue) {
        //onDamage(10, 1L, data, coercedScriptActions)
    }

    override fun onDamage(damage: Int, source: EntityID, data: LogicData, coercedScriptActions: LuaValue) {
        entity.health -= damage
        if (entity.health <= 0) {
            entity.health = 0
            runAssetScript(
                    "scenes/${data.gameMapName}/templates/entities/${entity.template}/death",
                    tableOf("entityID" to id, "sourceID" to source),
                    coercedScriptActions
            )
        }
    }

}