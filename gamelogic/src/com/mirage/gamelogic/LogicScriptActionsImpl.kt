package com.mirage.gamelogic

import com.badlogic.gdx.maps.MapObject
import com.mirage.utils.Log
import com.mirage.scriptrunner.logic.LogicScriptActions
import org.luaj.vm2.LuaTable

class LogicScriptActionsImpl(val loop: GameLoop) : LogicScriptActions {

    //TODO
    override fun runLogicScript(scriptName: String, args: LuaTable) {
        Log.d("Logic script $scriptName with args $args")
    }

    //TODO
    override fun runClientScriptForPlayer(player: MapObject, scriptName: String, args: LuaTable) {
        Log.d("Client script $scriptName with args $args for player $player")
    }

    //TODO
    override fun runClientScriptForAllInRoom(scriptName: String, args: LuaTable) {
        Log.d("Client script $scriptName with args $args for all players")
    }

    //TODO
    override fun findObject(objName: String): MapObject? {
        Log.d("Finding object $objName")
        return null
    }

    //TODO
    override fun findAllObjects(objName: String): LuaTable {
        Log.d("Finding all objects $objName")
        return LuaTable()
    }

    //TODO
    override fun findAllPlayers(): LuaTable {
        Log.d("Finding all players")
        return LuaTable()
    }
}