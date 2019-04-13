package com.mirage.utils.maps

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
         * Состояние объекта.
         * Просто кастомная строка, которую можно использовать для хранения какой-нибудь информации,
         * например, в скриптах.
         */
        val state: String?
        /**
         * Является ли объект твердым телом (свойство используется при проверке коллизий)
         */
        val isRigid: Boolean
        /**
         * Скрипты, которые связаны с объектом и вызываются при определённых условиях
         */
        val scripts: Map<String, String>?
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
        override val state: String?,
        override val isRigid: Boolean,
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
                 state: String? = this.state,
                 isRigid: Boolean = this.isRigid,
                 scripts: Map<String, String>? = this.scripts) : Building =
                Building(name, template, x, y, width, height, state, isRigid, scripts)

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
        override val state: String?,
        override val isRigid: Boolean,
        override val scripts: Map<String, String>?,
        val speed: Float,
        val moveDirection: String?
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
                 state: String? = this.state,
                 isRigid: Boolean = this.isRigid,
                 scripts: Map<String, String>? = this.scripts,
                 speed: Float = this.speed,
                 moveDirection: String? = this.moveDirection) : Entity =
                Entity(name, template, x, y, width, height, state, isRigid, scripts, speed, moveDirection)

}