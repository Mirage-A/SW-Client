package com.mirage.logic.processors

import com.mirage.logic.LogicData
import com.mirage.core.datastructures.rangeBetween
import com.mirage.core.extensions.EntityID
import com.mirage.core.extensions.tableOf
import com.mirage.core.messaging.*
import com.mirage.logic.behavior.PlayerBehavior
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