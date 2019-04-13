package com.mirage.utils.maps

/**
 * Изменяемый объект карты
 */
interface MutableGameObject {
    /**
     * Название объекта. Используется, например, для поиска объектов по названию.
     */
    var name: String?
    /**
     * Название шаблона объекта.
     */
    var template: String?
    /**
     * Координата x объекта в тайлах
     */
    var x: Float
    /**
     * Координата y объекта в тайлах
     */
    var y: Float
    /**
     * Ширина объекта в тайлах
     */
    var width: Float
    /**
     * Высота объекта в тайлах
     */
    var height: Float
    /**
     * Состояние объекта.
     * Просто кастомная строка, которую можно использовать для хранения какой-нибудь информации,
     * например, в скриптах.
     */
    var state: String?
    /**
     * Является ли объект твердым телом (свойство используется при проверке коллизий)
     */
    var isRigid: Boolean
    /**
     * Скрипты, которые связаны с объектом и вызываются при определённых условиях
     */
    var scripts: Map<String, String>?

    /**
     * Находит различия между этим объектом и объектом [origin] и возвращает объект [BuildingDifference],
     * хранящий эти различия.
     */
    fun findDifference(origin: GameObject) : ObjectDifference
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
        override var state: String?,
        override var isRigid: Boolean,
        override var scripts: Map<String, String>?
) : MutableGameObject {

    override fun findDifference(origin: GameObject): ObjectDifference {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
        override var state: String?,
        override var isRigid: Boolean,
        override var scripts: Map<String, String>?,
        var speed: Float,
        var moveDirection: String?
) : MutableGameObject {

    override fun findDifference(origin: GameObject): ObjectDifference {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}