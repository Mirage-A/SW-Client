package com.mirage.gamelogic.logic

import com.mirage.gamelogic.LogicData
import com.mirage.utils.Assets
import com.mirage.utils.datastructures.Point
import com.mirage.utils.extensions.TimeMillis
import com.mirage.utils.extensions.runScript
import com.mirage.utils.extensions.tableOf
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue


/** Runs a script from file "assets/ASSET_NAME.lua" */
internal fun LogicData.runAssetScript(assetName: String, args: LuaTable, coercedScriptActions: LuaValue) {
    args.set("actions", coercedScriptActions)
    val reader = Assets.loadReader("$assetName.lua") ?: return
    runScript(reader, args)
}

/** Process entering and leaving script areas */
internal fun LogicData.processScriptAreas(coercedScriptActions: LuaValue) {
    for ((id, entity) in state.entities) {
        val isPlayer = id in playerIDs
        val lastPosition = lastProcessedPositions[id] ?: Point(Float.MAX_VALUE, Float.MAX_VALUE)
        for (area in scriptAreas) {
            if (isPlayer || !area.playersOnly) {
                val wasInArea = lastPosition in area.area
                val isInArea = entity.position in area.area
                if (wasInArea != isInArea) {
                    val scriptName = if (isInArea) area.onEnter else area.onLeave
                    scriptName ?: continue
                    runAssetScript(
                            "scenes/$gameMapName/areas/$scriptName",
                            tableOf("entityID" to id),
                            coercedScriptActions
                    )
                }
            }
        }
        lastProcessedPositions[id] = entity.position
    }
}

internal fun LogicData.invokeDelayedScripts(currentTime: TimeMillis, coercedScriptActions: LuaValue) {
    while (delayedScripts.isNotEmpty() && delayedScripts.peek().first < currentTime) {
        val (name, args) = delayedScripts.poll().second
        runAssetScript("scenes/$gameMapName/scripts/$name", args, coercedScriptActions)
    }
}