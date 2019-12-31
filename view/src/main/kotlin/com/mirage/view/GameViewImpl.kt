package com.mirage.view

import com.mirage.core.DELTA_CENTER_Y
import com.mirage.core.utils.Point
import com.mirage.core.utils.EntityID
import com.mirage.core.utils.GameMapName
import com.mirage.core.game.maps.GameMap
import com.mirage.core.game.maps.SceneLoader
import com.mirage.core.game.objects.properties.Equipment
import com.mirage.core.game.objects.SimplifiedEntity
import com.mirage.core.game.objects.SimplifiedState
import com.mirage.core.game.objects.StateDifference
import com.mirage.core.VirtualScreen
import com.mirage.core.utils.Assets
import com.mirage.view.drawers.DrawersManager
import com.mirage.view.drawers.DrawersManagerImpl
import com.mirage.view.drawers.animation.AnimationLoader
import com.mirage.view.utils.getVirtualScreenPointFromScene

class GameViewImpl(assets: Assets, gameMapName: GameMapName, private val gameMap: GameMap) : GameView {

    private val drawersManager: DrawersManager = DrawersManagerImpl(AnimationLoader(assets), SceneLoader(assets, gameMapName))

    override fun loadDrawers(initialState: SimplifiedState) {
        drawersManager.loadDrawers(initialState)
    }

    override fun updateDrawers(oldState: SimplifiedState, diff: StateDifference) {
        drawersManager.updateDrawers(diff, oldState)
    }

    override fun setHumanoidEquipment(id: EntityID, entity: SimplifiedEntity, newEquipment: Equipment) {
        drawersManager.updateEquipment(id, entity, newEquipment, System.currentTimeMillis())
    }

    override fun renderGameState(virtualScreen: VirtualScreen, state: SimplifiedState, playerPositionOnScene: Point, targetID: Long?, isTargetEnemy: Boolean) {
        val playerPosOnVirtualScreen = getVirtualScreenPointFromScene(playerPositionOnScene)
        val cameraCenterPosition = Point(playerPosOnVirtualScreen.x, playerPosOnVirtualScreen.y + DELTA_CENTER_Y)
        renderGameMap(virtualScreen, gameMap, cameraCenterPosition.x, cameraCenterPosition.y)
        renderGameState(virtualScreen, state, drawersManager, cameraCenterPosition.x, cameraCenterPosition.y, playerPositionOnScene, targetID, isTargetEnemy)
    }

    override fun hit(virtualPoint: Point, lastRenderedState: SimplifiedState): Long? {
        var answer: Long? = null
        var answerEntity: SimplifiedEntity? = null
        for ((id, entity) in lastRenderedState.entities) {
            if (drawersManager.getEntityHitbox(id)?.contains(virtualPoint - getVirtualScreenPointFromScene(entity.position)) == true) {
                if (answerEntity == null || entity.x - entity.y > answerEntity.x - answerEntity.y) {
                    answer = id
                    answerEntity = entity
                }
            }
        }
        return answer
    }

}