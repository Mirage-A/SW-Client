package com.mirage.scriptrunner.logic

import com.badlogic.gdx.maps.MapObject
import com.mirage.scriptrunner.client.ClientScriptActions
import org.luaj.vm2.LuaTable

/**
 * Интерфейс, который определяет методы, которыми может пользоваться вызываемый скрипт.
 * Этот интерфейс должен быть реализован в другом модуле.
 */
interface LogicScriptActions {

    fun runLogicScript(scriptName: String, args: LuaTable)

    fun runClientScriptForPlayer(player: MapObject, scriptName: String, args: LuaTable)

    fun runClientScriptForAllInRoom(scriptName: String, args: LuaTable)

    fun findObject(objName: String) : MapObject?

    fun findAllObjects(objName: String) : LuaTable

    fun findAllPlayers() : LuaTable

    fun print(msg: Any?)
}