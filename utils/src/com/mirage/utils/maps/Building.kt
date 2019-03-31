package com.mirage.utils.maps

/**
 * Неизменяемое строение на карте.
 */
data class Building (
        /**
         * Название объекта. Используется, например, для поиска объектов по названию.
         */
        override val name: String?,
        /**
         * Название шаблона объекта.
         */
        override val template: String?,
        /**
         * Координата x объекта в тайлах
         */
        override val x: Float,
        /**
         * Координата y объекта в тайлах
         */
        override val y: Float,
        /**
         * Ширина объекта в тайлах
         */
        override val width: Float,
        /**
         * Высота объекта в тайлах
         */
        override val height: Float,
        /**
         * Состояние объекта.
         * Просто кастомная строка, которую можно использовать для хранения какой-нибудь информации,
         * например, в скриптах.
         */
        override val state: String?,
        /**
         * Является ли объект твердым телом (свойство используется при проверке коллизий)
         */
        override val isRigid: Boolean
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
             isRigid: Boolean = this.isRigid) : Building =
            Building(name, template, x, y, width, height, state, isRigid)

}