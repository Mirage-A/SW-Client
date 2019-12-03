package com.mirage.gameview

import com.mirage.gameview.drawers.DrawersManager
import com.mirage.gameview.drawers.DrawersManagerImpl
import com.mirage.gameview.utils.getVirtualScreenPointFromScene
import com.mirage.utils.DELTA_CENTER_Y
import com.mirage.utils.datastructures.Point
import com.mirage.utils.game.maps.GameMap
import com.mirage.utils.game.objects.simplified.SimplifiedEntity
import com.mirage.utils.game.states.SimplifiedState
import com.mirage.utils.game.states.StateDifference
import com.mirage.utils.virtualscreen.VirtualScreen

class GameViewImpl(private val gameMap: GameMap) : GameView {

    private val drawersManager: DrawersManager = DrawersManagerImpl()

    override fun loadDrawers(initialState: SimplifiedState) {
        drawersManager.loadDrawers(initialState)
    }

    override fun updateDrawers(oldState: SimplifiedState, diff: StateDifference) {
        drawersManager.updateDrawers(diff, oldState)
    }

    override fun renderGameState(virtualScreen: VirtualScreen, state: SimplifiedState, playerPositionOnScene: Point, targetID: Long?) {
        val playerPosOnVirtualScreen = getVirtualScreenPointFromScene(playerPositionOnScene)
        val cameraCenterPosition = Point(playerPosOnVirtualScreen.x, playerPosOnVirtualScreen.y + DELTA_CENTER_Y)
        renderGameMap(virtualScreen, gameMap, cameraCenterPosition.x, cameraCenterPosition.y)
        renderGameState(virtualScreen, state, drawersManager, cameraCenterPosition.x, cameraCenterPosition.y, targetID)
    }

    override fun hit(virtualPoint: Point, lastRenderedState: SimplifiedState): Long? {
        var answer: Long? = null
        var answerEntity: SimplifiedEntity? = null
        for ((id, entity) in lastRenderedState.entities) {
            if (drawersManager.getEntityHitbox(id)?.contains(virtualPoint - entity.position) == true) {
                if (answerEntity == null || entity.x - entity.y > answerEntity.x - answerEntity.y) {
                    answer = id
                    answerEntity = entity
                }
            }
        }
        return answer
    }

}