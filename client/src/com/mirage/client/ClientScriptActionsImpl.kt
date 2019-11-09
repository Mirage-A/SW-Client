package com.mirage.client

import com.badlogic.gdx.maps.MapObject
import com.mirage.client.controllers.GameController
import com.mirage.scriptrunner.ClientScriptActions
import com.mirage.gamelogic.scripting.runClientScript
import com.mirage.view.old.GameScreen
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.luaj.vm2.LuaTable

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
        val gameController = client.controller as? GameController ?: return null
        for ((_, obj) in gameController.state.objects) {
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