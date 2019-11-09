package com.mirage.gamelogic.scripting

import com.mirage.utils.Assets
import org.luaj.vm2.LuaTable
import org.luaj.vm2.lib.jse.CoerceJavaToLua
import org.luaj.vm2.lib.jse.JsePlatform


/**
 * Запускает скрипт c аргументом-таблицей args, добавляя аргумент actions
 */
fun runLogicScript(name: String, args: LuaTable, logicActions: LogicScriptActions) {
    val globals = JsePlatform.standardGlobals()
    val chunk = globals.load(Assets.loadLogicScript(name)?.readText() ?: "")
    args.set("actions", CoerceJavaToLua.coerce(logicActions))
    chunk.call(args)
}