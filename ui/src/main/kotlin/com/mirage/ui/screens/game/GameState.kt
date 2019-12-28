package com.mirage.ui.screens.game

import com.mirage.core.utils.GameMapName
import com.mirage.core.utils.QuestProgress
import com.mirage.core.game.maps.SceneLoader
import com.mirage.core.game.objects.properties.MoveDirection
import com.mirage.core.game.states.SimplifiedState
import com.mirage.core.utils.TestSamples

internal class GameState(val gameMapName: GameMapName) {

    val gameMap = SceneLoader(gameMapName).loadMap()

    var lastReceivedState: SimplifiedState = TestSamples.TEST_NO_GAME_OBJECTS

    val localQuestProgress: QuestProgress = QuestProgress()

    var bufferedMoving: Boolean? = null
    var bufferedMoveDirection: MoveDirection? = null
    var lastSentMoving: Boolean? = null
    var lastSentMoveDirection: MoveDirection? = null

    var targetID: Long? = null
    var playerID: Long? = null

    var gameOver = false
    var gameOverStartTime = 0L

    val skillNames: MutableList<String?> = mutableListOf("flame-strike", "flame-strike", "flame-strike", "flame-strike", "meteor")
    val skillCoolDowns: MutableList<Long> = MutableList(5) { 0L }

}