package com.mirage.gamelogic

import com.mirage.utils.game.maps.GameMap
import com.mirage.utils.game.objects.extended.ExtendedEntity
import com.mirage.utils.game.objects.properties.MoveDirection

/**
 * Создаёт персонажа игрока.
 * //TODO Передавать в эту функцию также информацию о прогрессе игрока для заполнения всяких скиллов/экипировки
 */
internal fun createPlayer(gameMap: GameMap) : ExtendedEntity = ExtendedEntity(
        name = "player",
        template = "player",
        x = gameMap.spawnX,
        y = gameMap.spawnY,
        width = 0.25f,
        height = 0.25f,
        isRigid = true,
        speed = 4.0f,
        moveDirection = MoveDirection.UP_RIGHT,
        isMoving = false,
        state = "",
        action = "idle"
)