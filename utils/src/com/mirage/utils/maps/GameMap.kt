package com.mirage.utils.maps

/**
 * Неизменяемая информация об игровой карте.
 * Содержит только параметры карты (размеры, тайлы), а не объекты.
 */
data class GameMap(
        /**
         * Ширина карты в тайлах
         */
        val width: Int,
        /**
         * Высота карты в тайлах
         */
        val height: Int,
        /**
         * Название файла с текстурами тайлов //TODO Попробовать избавиться от этого свойства, оно не нужно в логике
         */
        val tileSetName: String,
        /**
         * Список из ID тайлов.
         * ID тайла - номер его текстуры в файле с текстурами тайлов.
         * @see tileSetName
         */
        private val tiles: List<Int>,
        /**
         * Список из ID проходимости тайлов.
         * По умолчанию:
         * 1 - можно пройти
         * 2 - нельзя пройти, но снаряды пролетают
         * 3 - нельзя пройти, снаряды не пролетают
         */
        private val collisions: List<Int>
) {

    /**
     * Возвращает ID тайла по данным координатам.
     * ID тайла означает его номер в файле [tileSetName] с текстурами тайлов.
     */
    fun getTileID(x: Int, y: Int): Int = tiles[x + y * width]

    /**
     * Является ли тайл по данным координатам проходимым
     */
    fun isTileWalkable(x: Int, y: Int): Boolean = collisions[x + y * width] == 1

    /**
     * Является ли тайл по данным координатам простреливаемым
     */
    fun isTileShootable(x: Int, y: Int): Boolean = collisions[x + y * width] == 1 || collisions[x + y * width] == 2

}