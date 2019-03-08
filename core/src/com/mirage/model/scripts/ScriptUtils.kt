package com.mirage.model.scripts

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.maps.MapObject
import com.mirage.controller.Controller
import com.mirage.model.Model
import com.mirage.model.config
import com.mirage.model.extensions.getFloat
import com.mirage.view.screens.GameScreen
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.luaj.vm2.LuaTable
import org.luaj.vm2.lib.jse.CoerceJavaToLua
import org.luaj.vm2.lib.jse.JsePlatform

/**
 * Этот объект передаётся в каждый скрипт как аргумент и предоставляет основные функции,
 * которые могут выполнять скрипты.
 * Также этот объект позволяет запускать скрипты из кода программы.
 */
object ScriptUtils {

    /**
     * Запускает скрипт assets/scripts/$name.lua
     */
    fun runScript(name: String, args: LuaTable) {
        val globals = JsePlatform.standardGlobals()
        val chunk = globals.load(Gdx.files.internal("${config["assets"]}scripts/$name.lua").reader(), "$name.lua")
        args.set("utils", CoerceJavaToLua.coerce(ScriptUtils))
        chunk.call(args)
    }

    fun runScript(name: String, args: Map<String, Any?>) {
        val table = LuaTable()
        for ((key, value) in args) {
            table.set(key, CoerceJavaToLua.coerce(value))
        }
        runScript(name, table)
    }

    fun runScript(path: String) = runScript(path, LuaTable())

    fun runScriptAfterDelay(path: String, delayTime: Long, args: LuaTable) {
        GlobalScope.launch {
            delay(delayTime)
            runScript(path, args)
        }
    }

    fun runScriptAfterDelay(path: String, delayTime: Long) = runScriptAfterDelay(path, delayTime, LuaTable())

    fun getModel() = Model

    fun getController() = Controller

    fun getScreen() = Controller.screen

    fun getGameScreen() = Controller.gameScreen

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