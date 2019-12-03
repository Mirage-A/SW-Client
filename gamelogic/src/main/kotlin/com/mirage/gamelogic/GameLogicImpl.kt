package com.mirage.gamelogic

import com.mirage.gamelogic.scripting.LogicScriptActions
import com.mirage.utils.game.objects.simplified.SimplifiedObject
import com.mirage.utils.game.states.SimplifiedState
import com.mirage.utils.messaging.ClientMessage
import com.mirage.utils.messaging.ServerMessage
import org.luaj.vm2.LuaTable

class GameLogicImpl(mapName: String,
                    serverMessageListener: (ServerMessage) -> Unit,
                    stateUpdateListener: (SimplifiedState, Long) -> Unit) : GameLogic {

    private val loop : GameLoop = GameLoopImpl(mapName, serverMessageListener, stateUpdateListener)

    override fun startLogic() = loop.start()

    override fun addNewPlayer(onComplete: (playerID: Long) -> Unit) : Unit = loop.addNewPlayer(onComplete)

    override fun pauseLogic() = loop.pause()

    override fun resumeLogic() = loop.resume()

    override fun stopLogic() = loop.stop()

    override fun dispose() {
       //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        loop.dispose()
    }

    override fun handleMessage(id: Long, msg: ClientMessage) = loop.handleMessage(id, msg)

    private inner class ScriptActions : LogicScriptActions {

        override fun runLogicScript(scriptName: String, args: LuaTable) = TODO()

        override fun runClientScriptForAllInRoom(scriptName: String, args: LuaTable) = TODO()

        override fun findObject(objName: String): SimplifiedObject? = TODO()

        override fun findAllObjects(objName: String): LuaTable = TODO()

        override fun findAllPlayers(): LuaTable = TODO()

        override fun print(msg: Any?) = println(msg.toString())

    }

}