package com.mirage.logic.processors

import com.mirage.core.utils.EntityID
import com.mirage.core.messaging.ClientMessage
import com.mirage.logic.behavior.PlayerBehavior
import com.mirage.logic.data.LogicData
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
        (behaviors[id] as? PlayerBehavior)?.handleClientMessage(msg, this, coercedScriptActions)
    }
}