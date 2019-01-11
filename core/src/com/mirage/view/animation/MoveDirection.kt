package com.mirage.view.animation

/**
 * Направление движения
 */
enum class MoveDirection {
    RIGHT,
    UP_RIGHT,
    UP,
    UP_LEFT,
    LEFT,
    DOWN_LEFT,
    DOWN,
    DOWN_RIGHT;

    fun toAngle() : Float {
        return when (this) {
            DOWN_RIGHT -> 0f
            RIGHT -> Math.PI.toFloat() / 4
            UP_RIGHT -> Math.PI.toFloat() / 2
            UP -> Math.PI.toFloat() * 3 / 4
            UP_LEFT -> Math.PI.toFloat()
            LEFT -> Math.PI.toFloat() * 5 / 4
            DOWN_LEFT -> Math.PI.toFloat() * 3 / 2
            DOWN -> Math.PI.toFloat() * 7 / 4
        }
    }


    companion object {
        /**
         * Вычисляет направление движения на основе угла движения
         */
        @JvmStatic
        fun fromMoveAngle(angle: Float): MoveDirection {
            val partOfPi = (angle % (Math.PI * 2) / Math.PI).toFloat()
            if (partOfPi < 1 / 8f) return DOWN_RIGHT
            if (partOfPi < 3 / 8f) return RIGHT
            if (partOfPi < 5 / 8f) return UP_RIGHT
            if (partOfPi < 7 / 8f) return UP
            if (partOfPi < 9 / 8f) return UP_LEFT
            if (partOfPi < 11 / 8f) return LEFT
            if (partOfPi < 13 / 8f) return DOWN_LEFT
            return if (partOfPi < 15 / 8f) DOWN else DOWN_RIGHT
        }


        /**
         * Преобразует строку в направление движения (операция, обратная toString())
         * @throws Exception если строка не соответствует никакому направлению движения
         */
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
                else -> throw Exception("Incorrect move direction: $str")
            }
        }
    }
}
