package com.mirage.gamelogic

import com.mirage.utils.game.maps.GameMap
import com.mirage.utils.game.maps.SceneLoader
import com.mirage.utils.game.objects.extended.ExtendedEntity
import com.mirage.utils.game.objects.properties.MoveDirection

/**
 * Создаёт персонажа игрока.
 * //TODO Передавать в эту функцию также информацию о прогрессе игрока для заполнения всяких скиллов/экипировки
 */
internal fun createPlayer(gameMap: GameMap) : ExtendedEntity = SceneLoader.loadEntityTemplate("player").with(
        x = gameMap.spawnX,
        y = gameMap.spawnY
)