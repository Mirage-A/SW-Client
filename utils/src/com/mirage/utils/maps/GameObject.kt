package com.mirage.utils.maps

/**
 * Неизменяемый объект карты
 */
data class GameObject(
        /**
         * Название объекта. Используется, например, для поиска объектов по названию.
         * @see [GameMap.findObject]
         * @see [GameMap.findAllObjects]
         */
        val name: String?,
        /**
         * Координата x объекта в тайлах
         */
        val x: Float,
        /**
         * Координата y объекта в тайлах
         */
        val y: Float,
        /**
         * Ширина объекта в тайлах
         */
        val width: Float,
        /**
         * Высота объекта в тайлах
         */
        val height: Float,
        /**
         * Состояние объекта.
         * Просто кастомная строка, которую можно использовать для хранения какой-нибудь информации,
         * например, в скриптах.
         */
        val state: String?,
        /**
         * Номер слоя, на котором расположен объект.
         * По умолчанию:
         * -128 - слой floor, рисуется раньше всех
         * 0 - слой default
         * 127 - слой roof, рисуется позже всех
         */
        val layer: Int,
        /**
         * Название файла с текстурой объекта.
         * //TODO Убрать и переместить куда-нибудь подальше от логики.
         */
        val texture: String?,
        /**
         * Название файла с прозрачной текстурой объекта.
         */
        val transparentTexture: String?,
        /**
         * Расстояние по вертикали между этим объектом и границей, при наличии других объектов между этим объектом
         * и границей этот объект становится прозрачным (используется текстура [transparentTexture])
         */
        val transparencyRange: Float,
        /**
         * Является ли объект твердым телом (свойство используется при проверке коллизий)
         */
        val isRigid: Boolean
) {
    /**
     * Функция клонирования объекта с изменением некоторых свойств.
     * Пример синтаксиса вызова:
     * val newObj = obj.with(name="NewName", width=1.4f, state="overpowered")
     */
    fun with(name: String? = this.name,
             x: Float = this.x,
             y: Float = this.y,
             width: Float = this.width,
             height: Float = this.height,
             state: String? = this.state,
             layer: Int = this.layer,
             texture: String? = this.texture,
             transparentTexture: String? = this.transparentTexture,
             transparencyRange: Float = this.transparencyRange,
             isRigid: Boolean = this.isRigid) : GameObject =
            GameObject(name, x, y, width, height, state, layer, texture, transparentTexture, transparencyRange, isRigid)

}