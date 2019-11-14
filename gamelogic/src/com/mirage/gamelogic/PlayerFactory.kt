package com.mirage.gamelogic

import com.mirage.utils.game.maps.GameMap
import com.mirage.utils.game.objects.GameObject

/**
 * Создаёт персонажа игрока.
 * //TODO Передавать в эту функцию также информацию о прогрессе игрока для заполнения всяких скиллов/экипировки
 */
internal fun createPlayer(gameMap: GameMap) : GameObject = GameObject(
        name = "player",
        template = "player",
        type = GameObject.Type.ENTITY,
        x = gameMap.spawnX,
        y = gameMap.spawnY,
        width = 0.25f,
        height = 0.25f,
        isRigid = true,
        speed = 2.8f,
        moveDirection = GameObject.MoveDirection.UP_RIGHT,
        isMoving = false,
        transparencyRange = 0f,
        state = "",
        action = ""
)