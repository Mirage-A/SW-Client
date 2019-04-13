package com.mirage.utils.maps

import com.mirage.utils.Log

/**
 * Изменяемый объект карты
 */
interface MutableGameObject : GameObject {
    /**
     * Название объекта. Используется, например, для поиска объектов по названию.
     */
    override var name: String?
    /**
     * Название шаблона объекта.
     */
    override var template: String?
    /**
     * Координата x объекта в тайлах
     */
    override var x: Float
    /**
     * Координата y объекта в тайлах
     */
    override var y: Float
    /**
     * Ширина объекта в тайлах
     */
    override var width: Float
    /**
     * Высота объекта в тайлах
     */
    override var height: Float
    /**
     * Является ли объект твердым телом (свойство используется при проверке коллизий)
     */
    override var isRigid: Boolean
    /**
     * Скорость объекта
     */
    override var speed: Float
    /**
     * Направление движения объекта
     */
    override var moveDirection: String?
    /**
     * Двигается ли объект
     */
    override var isMoving: Boolean
    /**
     * Скрипты, которые связаны с объектом и вызываются при определённых условиях
     */
    override var scripts: Map<String, String>?

    /**
     * Находит различия между этим объектом и объектом [origin] и возвращает объект [BuildingDifference],
     * хранящий эти различия.
     */
    fun findDifference(origin: GameObject) : ObjectDifference

    /**
     * Просто возвращает ссылку на этот объект
     */
    override fun mutableCopy(): MutableGameObject = this
}


/**
 * Неизменяемое строение на карте.
 */
class MutableBuilding (
        override var name: String?,
        override var template: String?,
        override var x: Float,
        override var y: Float,
        override var width: Float,
        override var height: Float,
        override var isRigid: Boolean,
        override var speed: Float,
        override var moveDirection: String?,
        override var isMoving: Boolean,
        override var scripts: Map<String, String>?
) : MutableGameObject {

    override fun findDifference(origin: GameObject): ObjectDifference = when (origin) {
        is Building -> BuildingDifference(
                if (name == origin.name) null else name,
                if (template == origin.template) null else template,
                if (x == origin.x) null else x,
                if (y == origin.y) null else y,
                if (width == origin.width) null else width,
                if (height == origin.height) null else height,
                if (isRigid == origin.isRigid) null else isRigid,
                if (speed == origin.speed) null else speed,
                if (moveDirection == origin.moveDirection) null else moveDirection,
                if (isMoving == origin.isMoving) null else isMoving,
                if (scripts == origin.scripts) null else scripts
        )
        is Entity -> {
            Log.e("Warning: MutableBuilding should not find difference with Entity")
            EntityDifference()
        }
        else -> {
            Log.e("Error: Unknown GameObject type")
            BuildingDifference()
        }
    }

}


/**
 * Неизменяемая сущность на карте (персонаж игрока, NPC и т.д.)
 */
data class MutableEntity (
        override var name: String?,
        override var template: String?,
        override var x: Float,
        override var y: Float,
        override var width: Float,
        override var height: Float,
        override var isRigid: Boolean,
        override var speed: Float,
        override var moveDirection: String?,
        override var isMoving: Boolean,
        override var scripts: Map<String, String>?
) : MutableGameObject {

    override fun findDifference(origin: GameObject): ObjectDifference = when (origin) {
        is Entity -> EntityDifference(
                if (name == origin.name) null else name,
                if (template == origin.template) null else template,
                if (x == origin.x) null else x,
                if (y == origin.y) null else y,
                if (width == origin.width) null else width,
                if (height == origin.height) null else height,
                if (isRigid == origin.isRigid) null else isRigid,
                if (speed == origin.speed) null else speed,
                if (moveDirection == origin.moveDirection) null else moveDirection,
                if (isMoving == origin.isMoving) null else isMoving,
                if (scripts == origin.scripts) null else scripts
        )
        is Building -> {
            Log.e("Warning: MutableEntity should not find difference with Building")
            BuildingDifference()
        }
        else -> {
            Log.e("Error: Unknown GameObject type")
            EntityDifference()
        }
    }

}