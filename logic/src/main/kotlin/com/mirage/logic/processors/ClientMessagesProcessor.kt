package com.mirage.logic.processors

import com.mirage.logic.LogicData
import com.mirage.core.datastructures.rangeBetween
import com.mirage.core.extensions.EntityID
import com.mirage.core.extensions.tableOf
import com.mirage.core.messaging.*
import org.luaj.vm2.LuaValue


internal fun LogicData.getNewClientMessages() = ArrayList<Pair<EntityID, ClientMessage>>().apply {
    while (true) {
        val msg = clientMessages.poll()
        msg ?: break
        add(msg)
    }
}

internal fun LogicData.processClientMessages(coercedScriptActions: LuaValue) {
    for ((id, msg) in getNewClientMessages()) {
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
        }
    }
}