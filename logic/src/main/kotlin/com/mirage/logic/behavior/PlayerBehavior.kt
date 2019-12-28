package com.mirage.logic.behavior

import com.mirage.core.datastructures.rangeBetween
import com.mirage.core.extensions.EntityID
import com.mirage.core.extensions.IntervalMillis
import com.mirage.core.extensions.SkillNames
import com.mirage.core.extensions.tableOf
import com.mirage.core.game.objects.extended.ExtendedEntity
import com.mirage.core.game.objects.properties.Equipment
import com.mirage.core.game.objects.properties.PlayerAttributes
import com.mirage.core.messaging.*
import com.mirage.logic.data.LogicData
import com.mirage.logic.processors.moveObject
import com.mirage.logic.processors.runAssetScript
import org.luaj.vm2.LuaValue

/** Represents player behavior. This implementation should be used for all player entities. */
internal class PlayerBehavior(
        private val id: EntityID,
        equipment: Equipment,
        private val skillNames: SkillNames,
        data: LogicData
) : Behavior {

    private val entity = data.state.entities[id] ?: ExtendedEntity()
    private val attributes = PlayerAttributes(equipment)

    private var targetID: EntityID? = null

    override fun onUpdate(delta: IntervalMillis, data: LogicData, coercedScriptActions: LuaValue) {
        with(data) {
            entity.action = if (entity.isMoving) "running" else "idle"
            if (entity.isMoving) {
                moveObject(entity, delta, gameMap, state)
            }
            val targetBehavior = behaviors[targetID]
            targetBehavior?.onDamage(40, id, this, coercedScriptActions)
        }
    }

    override fun onDamage(damage: Int, source: EntityID, data: LogicData, coercedScriptActions: LuaValue) {
        //TODO
        entity.health -= damage
        if (entity.health <= 0) {
            entity.health = 0
            entity.state = "dead"
        }
    }

    fun handleClientMessage(msg: ClientMessage, data: LogicData, coercedScriptActions: LuaValue) {
        with(data) {
            when (msg) {
                is MoveDirectionClientMessage -> {
                    state.entities[id]?.moveDirection = msg.md
                }
                is SetMovingClientMessage -> {
                    state.entities[id]?.isMoving = msg.isMoving
                }
                is CastSkillClientMessage -> {
                    //TODO Skill casting
                }
                is InteractionClientMessage -> {
                    val player = state.entities[id]
                    val entity = state.entities[msg.entityID]
                    if (player != null && entity != null && rangeBetween(player.position, entity.position) < entity.interactionRange) {
                        runAssetScript(
                                "scenes/$gameMapName/templates/entities/${entity.template}/interaction",
                                tableOf("playerID" to id, "entityID" to msg.entityID),
                                coercedScriptActions
                        )
                    }
                }
                is SetTargetClientMessage -> {
                    //TODO Cancelling attacks
                    targetID = msg.targetID
                }
            }
        }
    }
}