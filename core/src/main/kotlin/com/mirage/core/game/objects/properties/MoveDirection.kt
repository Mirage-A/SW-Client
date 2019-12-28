package com.mirage.core.game.objects.properties

import com.mirage.core.Log

/** Move direction of an entity on scene (not on virtual screen) */
enum class MoveDirection {
    RIGHT,
    UP_RIGHT,
    UP,
    UP_LEFT,
    LEFT,
    DOWN_LEFT,
    DOWN,
    DOWN_RIGHT;

    fun toMoveAngle(): Float = Math.PI.toFloat() * when (this) {
        RIGHT -> 0f
        UP_RIGHT -> 0.25f
        UP -> 0.5f
        UP_LEFT -> 0.75f
        LEFT -> 1f
        DOWN_LEFT -> 1.25f
        DOWN -> 1.5f
        DOWN_RIGHT -> 1.75f
    }

    fun fromSceneToView(): MoveDirection = when (this) {
        RIGHT -> DOWN_RIGHT
        UP_RIGHT -> RIGHT
        UP -> UP_RIGHT
        UP_LEFT -> UP
        LEFT -> UP_LEFT
        DOWN_LEFT -> LEFT
        DOWN -> DOWN_LEFT
        DOWN_RIGHT -> DOWN
    }

    fun fromViewToScene(): MoveDirection = when (this) {
        RIGHT -> UP_RIGHT
        UP_RIGHT -> UP
        UP -> UP_LEFT
        UP_LEFT -> LEFT
        LEFT -> DOWN_LEFT
        DOWN_LEFT -> DOWN
        DOWN -> DOWN_RIGHT
        DOWN_RIGHT -> RIGHT
    }


    companion object {

        @JvmStatic
        fun fromMoveAngle(angle: Float): MoveDirection {
            val partOfPi = (angle % (Math.PI * 2) / Math.PI).toFloat()
            if (partOfPi < 1 / 8f) return RIGHT
            if (partOfPi < 3 / 8f) return UP_RIGHT
            if (partOfPi < 5 / 8f) return UP
            if (partOfPi < 7 / 8f) return UP_LEFT
            if (partOfPi < 9 / 8f) return LEFT
            if (partOfPi < 11 / 8f) return DOWN_LEFT
            if (partOfPi < 13 / 8f) return DOWN
            return if (partOfPi < 15 / 8f) DOWN_RIGHT else RIGHT
        }


        fun fromString(str: String) : MoveDirection {
            return when (str) {
                "RIGHT" -> RIGHT
                "UP_RIGHT" -> UP_RIGHT
                "UP" -> UP
                "UP_LEFT" -> UP_LEFT
                "LEFT" -> LEFT
                "DOWN_LEFT" -> DOWN_LEFT
                "DOWN" -> DOWN
                "DOWN_RIGHT" -> DOWN_RIGHT
                else -> {
                    Log.e("Incorrect move direction: $str")
                    return DOWN
                }
            }
        }
    }
}

