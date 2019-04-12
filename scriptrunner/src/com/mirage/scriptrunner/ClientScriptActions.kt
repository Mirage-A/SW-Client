package com.mirage.scriptrunner

import com.badlogic.gdx.maps.MapObject
import org.luaj.vm2.LuaTable

/**
 * Интерфейс, который определяет методы, которыми может пользоваться вызываемый скрипт.
 * Этот интерфейс должен быть реализован в другом модуле.
 */
interface ClientScriptActions {

    fun runScript(scriptName: String, args: LuaTable)

    fun runScriptAfterDelay(scriptName: String, args: LuaTable, delayTime: Long)

    fun findObject(objName: String) : MapObject?

    fun findAllObjects(objName: String) : LuaTable

    fun findAllPlayers() : LuaTable

    fun updateObjectDrawer(obj: MapObject)

}