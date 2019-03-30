package com.mirage.utils.maps

/**
 * Интерфейс неизменяемой игровой карты
 */
interface GameMap {

    /**
     * Размеры карты в тайлах
     */
    val width: Int
    val height: Int
    /**
     * Название файла с текстурами тайлов //TODO Попробовать избавиться от этого свойства, оно не нужно в логике
     */
    val tileSetName: String

    /**
     * Возвращает ID тайла по данным координатам.
     * ID тайла означает его номер в файле [tileSetName] с текстурами тайлов.
     */
    fun getTileID(x: Int, y: Int): Int

    /**
     * Является ли тайл по данным координатам проходимым
     */
    fun isTileWalkable(x: Int, y: Int): Boolean

    /**
     * Является ли тайл по данным координатам простреливаемым
     */
    fun isTileShootable(x: Int, y: Int): Boolean

    /**
     * Объекты на слое floor. Должны отрисовываться раньше остальных.
     */
    val floorObjects: GameObjects

    /**
     * Объекты на основном слое.
     */
    val mainObjects: GameObjects

    /**
     * Объекты на слое roof. Должны отрисовываться позже остальных.
     */
    val roofObjects: GameObjects

    /**
     * Выполняет поиск объекта с заданным именем.
     * Имя определяется свойством name у интерфейса [GameObject]
     * Если такие объекты на карте присутствуют, возвращается один из них.
     * Если таких объектов нет, возвращается null.
     */
    fun findObject(name: String) : GameObject?

    /**
     * Выполняет поиск всех объектов с заданным именем.
     */
    fun findAllObjects(name: String) : Collection<GameObject>

}