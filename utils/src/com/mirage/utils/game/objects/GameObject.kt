package com.mirage.utils.game.objects

import com.badlogic.gdx.math.Rectangle
import com.mirage.utils.Log
import com.mirage.utils.datastructures.Point

/**
 * Неизменяемый объект карты
 */
data class GameObject (
        /**
         * Название объекта. Используется, например, для поиска объектов по названию.
         */
        val name: String,
        /**
         * Название шаблона объекта.
         */
        val template: String,
        /**
         * Тип объекта.
         */
        val type: Type,
        /**
         * Координата x объекта в тайлах
         */
        val x: Float,
        /**
         * Координата y объекта в тайлах
         */
        val y: Float,
        /**
         * Ширина объекта в тайлах (для обработки коллизий при isRigid == true)
         */
        val width: Float,
        /**
         * Высота объекта в тайлах (для обработки коллизий при isRigid == true)
         */
        val height: Float,
        /**
         * Является ли объект твердым телом (свойство используется при проверке коллизий)
         */
        val isRigid: Boolean,
        /**
         * Скорость движения объекта
         */
        val speed: Float,
        /**
         * Направление движения объекта
         */
        val moveDirection: MoveDirection,
        /**
         * Находится ли объект в движении
         */
        val isMoving: Boolean,
        /**
         * Если на расстоянии менее transparencyRange тайлов к северу от этого объекта находится сущность, это строение становится прозрачным.
         * Этот параметр нужен для строений; для сущностей он обычно равен 0.
         */
        val transparencyRange: Float,
        /**
         * Значение, характеризующее текущее состояние объекта. В основном используется скриптами.
         */
        val state: String,
        /**
         * Значение, характеризующие текущее действие объекта.
         */
        val action: String
) {
    /**
     * Создаёт изменяемую копию объекта.
     */
    fun mutableCopy(): MutableGameObject = MutableGameObject(
            this.name,
            this.template,
            this.type,
            this.x,
            this.y,
            this.width,
            this.height,
            this.isRigid,
            this.speed,
            this.moveDirection,
            this.isMoving,
            this.transparencyRange,
            this.state,
            this.action
    )

    /**
     * Создаёт копию объекта с измененной позицией (движение этого объекта за [deltaTimeMillis] мс)
     */
    fun move(deltaTimeMillis: Long): GameObject =
            this.with(
                    x = this.x + this.speed * when (this.moveDirection) {
                        MoveDirection.UP, MoveDirection.DOWN -> 0f
                        MoveDirection.LEFT -> -1f
                        MoveDirection.RIGHT -> 1f
                        MoveDirection.UP_RIGHT, MoveDirection.DOWN_RIGHT -> 1.41421f
                        MoveDirection.UP_LEFT, MoveDirection.DOWN_LEFT -> -1.41421f
                    } * deltaTimeMillis / 1000L,
                    y = this.y + this.speed * when (this.moveDirection) {
                        MoveDirection.LEFT, MoveDirection.RIGHT -> 0f
                        MoveDirection.DOWN -> -1f
                        MoveDirection.UP -> 1f
                        MoveDirection.UP_RIGHT, MoveDirection.UP_LEFT -> 1.41421f
                        MoveDirection.DOWN_RIGHT, MoveDirection.DOWN_LEFT -> -1.41421f
                    } * deltaTimeMillis / 1000L
            )

    /**
     * Функция клонирования объекта с изменением некоторых свойств.
     * Пример синтаксиса вызова:
     * val newObj = obj.with(name="NewName", width=1.4f, state="overpowered")
     */
    fun with(name: String = this.name,
             template: String = this.template,
             type: Type = this.type,
             x: Float = this.x,
             y: Float = this.y,
             width: Float = this.width,
             height: Float = this.height,
             isRigid: Boolean = this.isRigid,
             speed: Float = this.speed,
             moveDirection: MoveDirection = this.moveDirection,
             isMoving: Boolean = this.isMoving,
             transparencyRange: Float = this.transparencyRange,
             state: String = this.state,
             action: String = this.action): GameObject =
            GameObject(name, template, type, x, y, width, height, isRigid, speed, moveDirection, isMoving, transparencyRange, state, action)


    val rectangle : Rectangle
        get() = Rectangle(
                x - width / 2, y - height / 2, width, height
        )

    val position : Point
        get() = Point(x, y)


    enum class Type {
        BUILDING,
        ENTITY;

        companion object {
            fun fromString(str: String) : Type {
                return when (str) {
                    "BUILDING" -> BUILDING
                    "ENTITY" -> ENTITY
                    else -> {
                        Log.e("Incorrect game object type: $str")
                        return BUILDING
                    }
                }
            }
        }
    }

    /**
     * Направление движения этого объекта НА ВИРТУАЛЬНОЙ СЦЕНЕ.
     * (На реальном экране направление движения будет другим)
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


        companion object {
            /**
             * Вычисляет направление движения на основе угла движения
             */
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


            /**
             * Преобразует строку в направление движения (операция, обратная toString())
             * Если строка не соответствует никакому направлению движения, возвращает MoveDirection.DOWN
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
                    else -> {
                        Log.e("Incorrect move direction: $str")
                        return DOWN
                    }
                }
            }
        }
    }

    /**
     * Тип оружия гуманоида (одноручное, двуручное, два одноручных, одноручное и щит, лук и т.д.)
     */
    enum class WeaponType {
        UNARMED,
        ONE_HANDED,
        ONE_HANDED_AND_SHIELD,
        DUAL,
        TWO_HANDED,
        BOW,
        STAFF;
        companion object {
            /**
             * Преобразует строку в тип оружия (операция, обратная toString())
             * @throws Exception если строка не соответствует никакому типу оружия
             */
            fun fromString(str: String) : WeaponType {
                return when (str) {
                    "UNARMED" -> UNARMED
                    "ONE_HANDED" -> ONE_HANDED
                    "ONE_HANDED_AND_SHIELD" -> ONE_HANDED_AND_SHIELD
                    "DUAL" -> DUAL
                    "TWO_HANDED" -> TWO_HANDED
                    "BOW" -> BOW
                    "STAFF" -> STAFF
                    else -> throw Exception("Incorrect weapon type: $str")
                }
            }
        }
    }

    data class HumanoidEquipment(
            val helmet: String,
            val chest: String,
            val gloves: String,
            val cloak: String,
            val legs: String,
            val rightWeapon: String,
            val leftWeapon: String,
            val weaponType: WeaponType
    )

}
