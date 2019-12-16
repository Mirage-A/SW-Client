package com.mirage.gamelogic

import com.mirage.utils.extensions.PlayerCreationListener
import com.mirage.utils.extensions.QuestProgress
import com.mirage.utils.game.states.SimplifiedState
import com.mirage.utils.messaging.ClientMessage
import com.mirage.utils.messaging.ServerMessage

class GameLogicImpl(mapName: String,
                    serverMessageListener: (ServerMessage) -> Unit,
                    stateUpdateListener: (SimplifiedState, Long) -> Unit) : GameLogic {

    private val loop : GameLoop = GameLoopImpl(mapName, serverMessageListener, stateUpdateListener)

    override fun startLogic() = loop.start()

    override fun addNewPlayer(globalQuestProgress: QuestProgress?, onComplete: PlayerCreationListener) : Unit =
            loop.addNewPlayer(globalQuestProgress, onComplete)

    override fun pauseLogic() = loop.pause()

    override fun resumeLogic() = loop.resume()

    override fun stopLogic() = loop.stop()

    override fun dispose() {
       //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        loop.dispose()
    }

    override fun handleMessage(id: Long, msg: ClientMessage) = loop.handleMessage(id, msg)

}