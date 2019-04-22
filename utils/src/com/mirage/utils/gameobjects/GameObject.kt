package com.mirage.utils.gameobjects

import com.mirage.utils.extensions.mutableCopy

/**
 * Неизменяемый объект карты
 */
interface GameObject {
        /**
         * Название объекта. Используется, например, для поиска объектов по названию.
         */
        val name: String?
        /**
         * Название шаблона объекта.
         */
        val template: String?
        /**
         * Координата x объекта в тайлах
         */
        val x: Float
        /**
         * Координата y объекта в тайлах
         */
        val y: Float
        /**
         * Ширина объекта в тайлах
         */
        val width: Float
        /**
         * Высота объекта в тайлах
         */
        val height: Float
        /**
         * Является ли объект твердым телом (свойство используется при проверке коллизий)
         */
        val isRigid: Boolean
        /**
         * Скорость движения объекта
         */
        val speed: Float
        /**
         * Направление движения объекта
         */
        val moveDirection: String?
        /**
         * Находится ли объект в движении
         */
        val isMoving: Boolean
        /**
         * Скрипты, которые связаны с объектом и вызываются при определённых условиях
         */
        val scripts: Map<String, String>?

        /**
         * Создаёт изменяемую копию объекта
         */
        fun mutableCopy() : MutableGameObject

        /**
         * Создаёт копию объекта с измененной позицией (движение этого объекта за [deltaTimeMillis] мс)
         */
        fun move(deltaTimeMillis: Long): GameObject =
                this.with(
                        x = this.x + this.speed * when (this.moveDirection) {
                            "UP", "DOWN" -> 0f
                            "LEFT" -> -1f
                            "RIGHT" -> 1f
                            "UP_RIGHT", "DOWN_RIGHT" -> 1.41421f
                            "UP_LEFT", "DOWN_LEFT" -> -1.41421f
                            else -> 0f
                        } * deltaTimeMillis / 1000L,
                        y = this.y + this.speed * when (this.moveDirection) {
                            "LEFT", "RIGHT" -> 0f
                            "DOWN" -> -1f
                            "UP" -> 1f
                            "UP_RIGHT", "UP_LEFT" -> 1.41421f
                            "DOWN_RIGHT", "DOWN_LEFT" -> -1.41421f
                            else -> 0f
                        } * deltaTimeMillis / 1000L
                )

        /**
         * Создаёт копию объекта с измененной позицией
         */
        fun with(x : Float, y : Float) : GameObject
}


/**
 * Неизменяемое строение на карте.
 */
data class Building (
        override val name: String?,
        override val template: String?,
        override val x: Float,
        override val y: Float,
        override val width: Float,
        override val height: Float,
        override val isRigid: Boolean,
        override val speed: Float,
        override val moveDirection: String?,
        override val isMoving: Boolean,
        override val scripts: Map<String, String>?,
        val transparencyRange: Float
) : GameObject {
        /**
         * Функция клонирования объекта с изменением некоторых свойств.
         * Пример синтаксиса вызова:
         * val newObj = obj.with(name="NewName", width=1.4f, state="overpowered")
         */
        fun with(name: String? = this.name,
                 template: String? = this.template,
                 x: Float = this.x,
                 y: Float = this.y,
                 width: Float = this.width,
                 height: Float = this.height,
                 isRigid: Boolean = this.isRigid,
                 speed: Float = this.speed,
                 moveDirection: String? = this.moveDirection,
                 isMoving: Boolean = this.isMoving,
                 scripts: Map<String, String>? = this.scripts,
                 transparencyRange: Float = this.transparencyRange) : Building =
                Building(name, template, x, y, width, height, isRigid, speed, moveDirection, isMoving, scripts, transparencyRange)

        override fun with(x: Float, y: Float): Building = this.with(x = x, y = y, name = this.name)

        override fun mutableCopy() : MutableGameObject = MutableBuilding(
                this.name,
                this.template,
                this.x,
                this.y,
                this.width,
                this.height,
                this.isRigid,
                this.speed,
                this.moveDirection,
                this.isMoving,
                this.scripts?.mutableCopy(),
                this.transparencyRange
        )
}


/**
 * Неизменяемая сущность на карте (персонаж игрока, NPC и т.д.)
 */
data class Entity (
        override val name: String?,
        override val template: String?,
        override val x: Float,
        override val y: Float,
        override val width: Float,
        override val height: Float,
        override val isRigid: Boolean,
        override val speed: Float,
        override val moveDirection: String?,
        override val isMoving: Boolean,
        override val scripts: Map<String, String>?
) : GameObject {
        /**
         * Функция клонирования объекта с изменением некоторых свойств.
         * Пример синтаксиса вызова:
         * val newObj = obj.with(name="NewName", width=1.4f, state="overpowered")
         */
        fun with(name: String? = this.name,
                 template: String? = this.template,
                 x: Float = this.x,
                 y: Float = this.y,
                 width: Float = this.width,
                 height: Float = this.height,
                 isRigid: Boolean = this.isRigid,
                 speed: Float = this.speed,
                 moveDirection: String? = this.moveDirection,
                 isMoving: Boolean = this.isMoving,
                 scripts: Map<String, String>? = this.scripts) : Entity =
                Entity(name, template, x, y, width, height, isRigid, speed, moveDirection, isMoving, scripts)

        override fun with(x: Float, y: Float): Entity = this.with(x = x, y = y, name = this.name)

        override fun mutableCopy() : MutableGameObject = MutableEntity(
                this.name,
                this.template,
                this.x,
                this.y,
                this.width,
                this.height,
                this.isRigid,
                this.speed,
                this.moveDirection,
                this.isMoving,
                this.scripts?.mutableCopy()
        )
}