package com.mirage.gamelogic

import com.mirage.gamelogic.scripting.LogicScriptActions
import com.mirage.utils.game.objects.GameObject
import com.mirage.utils.game.objects.GameObjects
import com.mirage.utils.messaging.ClientMessage
import com.mirage.utils.messaging.ServerMessage
import org.luaj.vm2.LuaTable
import rx.Observable

class GameLogicImpl(mapName: String) : GameLogic {

    private val loop : GameLoop = GameLoopImpl(mapName)

    override fun startLogic() = loop.start()

    override fun addNewPlayer(): Long = loop.addNewPlayer()

    override fun pauseLogic() = loop.pause()

    override fun resumeLogic() = loop.resume()

    override fun stopLogic() = loop.stop()

    override fun dispose() {
       //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun handleMessage(id: Long, msg: ClientMessage) = loop.handleMessage(id, msg)

    override val latestState: Observable<Pair<GameObjects, Long>>
        get() = loop.latestState

    override val serverMessages: Observable<ServerMessage>
        get() = loop.serverMessages

    private inner class ScriptActions : LogicScriptActions {

        override fun runLogicScript(scriptName: String, args: LuaTable) = TODO()

        override fun runClientScriptForAllInRoom(scriptName: String, args: LuaTable) = TODO()

        override fun findObject(objName: String): GameObject? = TODO()

        override fun findAllObjects(objName: String): LuaTable = TODO()

        override fun findAllPlayers(): LuaTable = TODO()

        override fun print(msg: Any?) = println(msg.toString())

    }

}