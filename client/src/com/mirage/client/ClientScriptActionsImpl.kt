package com.mirage.client

import com.badlogic.gdx.maps.MapObject
import com.mirage.scriptrunner.client.ClientScriptActions
import com.mirage.scriptrunner.runClientScript
import com.mirage.view.screens.GameScreen
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.luaj.vm2.LuaTable
import org.luaj.vm2.lib.jse.CoerceJavaToLua

class ClientScriptActionsImpl(private val client: Client) : ClientScriptActions {

    override fun runScript(scriptName: String, args: LuaTable) {
        runClientScript(scriptName, args, this)
    }

    override fun runScriptAfterDelay(scriptName: String, args: LuaTable, delayTime: Long) {
        GlobalScope.launch {
            delay(delayTime)
            runScript(scriptName, args)
        }
    }

    override fun updateObjectDrawer(obj: MapObject) {
        (client.screen as? GameScreen)?.drawers?.addObjectDrawer(obj)
    }

    override fun findObject(objName: String): MapObject? {
        for ((_, obj) in client.state.objects) {
            if (obj.name == objName) return obj
        }
        return null
    }

    override fun findAllObjects(objName: String): LuaTable {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun findAllPlayers(): LuaTable {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}