package com.mirage.scriptrunner.client

import com.badlogic.gdx.maps.MapObject
import com.mirage.scriptrunner.logic.LogicScriptActions
import org.luaj.vm2.LuaTable

/**
 * Интерфейс, который определяет методы, которыми может пользоваться вызываемый скрипт.
 * Этот интерфейс должен быть реализован в другом модуле.
 */
interface ClientScriptActions {

    fun runLogicScript(scriptName: String, args: LuaTable)

    fun runClientScriptForPlayer(player: MapObject, scriptName: String, args: LuaTable)

    fun runClientScriptForAllInRoom(scriptName: String, args: LuaTable)

    fun findObject(objName: String) : MapObject?

    fun findAllObjects(objName: String) : LuaTable

    fun findAllPlayers() : LuaTable
}