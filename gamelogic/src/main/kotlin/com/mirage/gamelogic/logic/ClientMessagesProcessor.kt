package com.mirage.gamelogic.logic

import com.mirage.gamelogic.LogicData
import com.mirage.gamelogic.LogicScriptActions
import com.mirage.utils.datastructures.rangeBetween
import com.mirage.utils.extensions.EntityID
import com.mirage.utils.extensions.tableOf
import com.mirage.utils.messaging.*
import org.luaj.vm2.LuaValue


internal fun LogicData.getNewClientMessages() = ArrayList<Pair<EntityID, ClientMessage>>().apply {
    while (true) {
        val msg = clientMessages.poll()
        msg ?: break
        add(msg)
    }
}

internal fun LogicData.handleClientMessages(coercedScriptActions: LuaValue) {
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