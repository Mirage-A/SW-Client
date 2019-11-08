package com.mirage.gamelogic

import com.mirage.utils.game.objects.Entity
import com.mirage.utils.game.maps.GameMap

/**
 * Создаёт персонажа игрока.
 * //TODO Передавать в эту функцию также информацию о прогрессе игрока для заполнения всяких скиллов/экипировки
 */
fun createPlayer(gameMap: GameMap) : Entity = Entity(
        name = "player",
        template = "player",
        x = gameMap.spawnX,
        y = gameMap.spawnY,
        width = 0.25f,
        height = 0.25f,
        isRigid = true,
        speed = 2.8f,
        moveDirection = "UP_RIGHT",
        isMoving = false,
        scripts = mapOf("on-tile-entered" to "hello")
)