package com.mirage.gamelogic

import com.mirage.scriptrunner.LogicScriptActions
import com.mirage.utils.maps.GameObject
import com.mirage.utils.maps.GameStateSnapshot
import com.mirage.utils.messaging.ClientMessage
import org.luaj.vm2.LuaTable
import rx.Observable

class GameLogicImpl(mapName: String) : GameLogic {

    private val loop : GameLoop = GameLoopImpl(mapName)

    override fun startLogic() = loop.start()

    override fun pauseLogic() = loop.pause()

    override fun resumeLogic() = loop.resume()

    override fun stopLogic() = loop.stop()

    override fun dispose() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun handleMessage(msg: ClientMessage) = loop.handleMessage(msg)

    override val observable: Observable<GameStateSnapshot>
        get() = loop.observable

    private inner class ScriptActions : LogicScriptActions {

        override fun runLogicScript(scriptName: String, args: LuaTable) = TODO()

        override fun runClientScriptForAllInRoom(scriptName: String, args: LuaTable) = TODO()

        override fun findObject(objName: String): GameObject? = TODO()

        override fun findAllObjects(objName: String): LuaTable = TODO()

        override fun findAllPlayers(): LuaTable = TODO()

        override fun print(msg: Any?) = println(msg.toString())

    }

}