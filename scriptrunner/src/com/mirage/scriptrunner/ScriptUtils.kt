package com.mirage.scriptrunner

import com.badlogic.gdx.maps.MapObject
import com.mirage.gamelogic.Model
import com.mirage.gamelogic.extensions.getFloat
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.luaj.vm2.LuaTable

/**
 * Этот объект передаётся в каждый скрипт как аргумент и предоставляет основные функции,
 * которые могут выполнять скрипты.
 * Также этот объект позволяет запускать скрипты из кода программы.
 */
object ScriptUtils {



    fun runScriptAfterDelay(path: String, delayTime: Long, args: LuaTable) {
        GlobalScope.launch {
            delay(delayTime)
            runScript(path, args)
        }
    }

    fun runScriptAfterDelay(path: String, delayTime: Long) = runScriptAfterDelay(path, delayTime, LuaTable())

    fun getModel() = Model

    fun findObject(objName: String) = Model.findObject(objName)

    fun getPlayer() = Model.getPlayer()

    fun getPlayerX() = getPlayer()?.properties?.getFloat("x")

    fun getPlayerY() = getPlayer()?.properties?.getFloat("y")

    fun getProperty(objName: String, propertyName: String) = findObject(objName)?.properties?.get(propertyName)

    fun setProperty(objName: String, propertyName: String, propertyValue: Any?) =
            findObject(objName)?.properties?.put(propertyName, propertyValue)

    fun updateObjectDrawer(obj: MapObject) = (Controller.screen as? GameScreen)?.drawers?.addObjectDrawer(obj)

    fun updateObjectDrawer(objName: String) = findObject(objName)?.run { ScriptUtils.updateObjectDrawer(this) }



}