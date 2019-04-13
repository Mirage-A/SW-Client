package com.mirage.utils.gameobjects

import com.mirage.utils.Log

interface ObjectDifference {
        var name: String?
        var template: String?
        var x: Float?
        var y: Float?
        var width: Float?
        var height: Float?
        var isRigid: Boolean?
        var speed: Float?
        var moveDirection: String?
        var isMoving: Boolean?
        var scripts: Map<String, String>?

    /**
     * Функция, позволяющая применить изменения к объекту [origin]. Возвращает новый объект с измененными свойствами.
     * Важно, чтобы BuildingDifference применялось к Building (аналогично для Entity),
     * иначе некоторые свойства могут потеряться.
     *
     */
    abstract fun projectOn(origin: GameObject) : GameObject
}

/**
 * Изменяемый объект, в котором удобно хранить промежуточные изменения другого объекта, а затем объединить исходный
 * объект с этим, чтобы перезаписать то, что было изменено.
 * Работать с этим объектом нужно только в однопоточной среде!
 */
data class BuildingDifference (
        override var name: String? = null,
        override var template: String? = null,
        override var x: Float? = null,
        override var y: Float? = null,
        override var width: Float? = null,
        override var height: Float? = null,
        override var isRigid: Boolean? = null,
        override var speed: Float? = null,
        override var moveDirection: String? = null,
        override var isMoving: Boolean? = null,
        override var scripts: Map<String, String>? = null
) : ObjectDifference {
    /**
     * "Проецирует" эту разность на [origin], возвращая новое [Building], свойства которого равны свойствам
     * данного объекта, если они не null, иначе они равны свойствам [origin].
     * Метод позволяет применить некоторые изменения к изначальному объекту и получить новый.
     */
    override fun projectOn(origin: GameObject) : GameObject =
            when (origin) {
                is Building -> Building(
                        name = name ?: origin.name,
                        template = template ?: origin.template,
                        x = x ?: origin.x,
                        y = y ?: origin.y,
                        width = width ?: origin.width,
                        height = height ?: origin.height,
                        isRigid = isRigid ?: origin.isRigid,
                        speed = speed ?: origin.speed,
                        moveDirection = moveDirection ?: origin.moveDirection,
                        isMoving = isMoving ?: origin.isMoving,
                        scripts = scripts ?: origin.scripts
                )
                is Entity -> {
                    Log.e("Warning: BuildingDifference should not be applied to Entity")
                    Entity(
                            name = name ?: origin.name,
                            template = template ?: origin.template,
                            x = x ?: origin.x,
                            y = y ?: origin.y,
                            width = width ?: origin.width,
                            height = height ?: origin.height,
                            isRigid = isRigid ?: origin.isRigid,
                            speed = speed ?: origin.speed,
                            moveDirection = moveDirection ?: origin.moveDirection,
                            isMoving = isMoving ?: origin.isMoving,
                            scripts = scripts ?: origin.scripts
                    )
                }
                else -> {
                    Log.e("Error: Unknown type of GameObject")
                    Building(
                            name = name ?: origin.name,
                            template = template ?: origin.template,
                            x = x ?: origin.x,
                            y = y ?: origin.y,
                            width = width ?: origin.width,
                            height = height ?: origin.height,
                            isRigid = isRigid ?: origin.isRigid,
                            speed = speed ?: origin.speed,
                            moveDirection = moveDirection ?: origin.moveDirection,
                            isMoving = isMoving ?: origin.isMoving,
                            scripts = scripts ?: origin.scripts
                    )
                }
            }

}

/**
 * Изменяемый объект, в котором удобно хранить промежуточные изменения другого объекта, а затем объединить исходный
 * объект с этим, чтобы перезаписать то, что было изменено.
 * Работать с этим объектом нужно только в однопоточной среде!
 */
data class EntityDifference (
        override var name: String? = null,
        override var template: String? = null,
        override var x: Float? = null,
        override var y: Float? = null,
        override var width: Float? = null,
        override var height: Float? = null,
        override var isRigid: Boolean? = null,
        override var speed: Float? = null,
        override var moveDirection: String? = null,
        override var isMoving: Boolean? = null,
        override var scripts: Map<String, String>? = null
) : ObjectDifference {
    /**
     * "Проецирует" эту разность на [origin], возвращая новое [Entity], свойства которого равны свойствам
     * данного объекта, если они не null, иначе они равны свойствам [origin].
     * Метод позволяет применить некоторые изменения к изначальному объекту и получить новый.
     */
    override fun projectOn(origin: GameObject) : GameObject =
            when (origin) {
                is Entity -> Entity(
                        name = name ?: origin.name,
                        template = template ?: origin.template,
                        x = x ?: origin.x,
                        y = y ?: origin.y,
                        width = width ?: origin.width,
                        height = height ?: origin.height,
                        isRigid = isRigid ?: origin.isRigid,
                        speed = speed ?: origin.speed,
                        moveDirection = moveDirection ?: origin.moveDirection,
                        isMoving = isMoving ?: origin.isMoving,
                        scripts = scripts ?: origin.scripts
                )
                is Building -> {
                    Log.e("Warning: EntityDifference should not be applied to Building")
                    Building(
                            name = name ?: origin.name,
                            template = template ?: origin.template,
                            x = x ?: origin.x,
                            y = y ?: origin.y,
                            width = width ?: origin.width,
                            height = height ?: origin.height,
                            isRigid = isRigid ?: origin.isRigid,
                            speed = speed ?: origin.speed,
                            moveDirection = moveDirection ?: origin.moveDirection,
                            isMoving = isMoving ?: origin.isMoving,
                            scripts = scripts ?: origin.scripts
                    )
                }
                else -> {
                    Log.e("Error: Unknown type of GameObject")
                    Building(
                            name = name ?: origin.name,
                            template = template ?: origin.template,
                            x = x ?: origin.x,
                            y = y ?: origin.y,
                            width = width ?: origin.width,
                            height = height ?: origin.height,
                            isRigid = isRigid ?: origin.isRigid,
                            speed = speed ?: origin.speed,
                            moveDirection = moveDirection ?: origin.moveDirection,
                            isMoving = isMoving ?: origin.isMoving,
                            scripts = scripts ?: origin.scripts
                    )
                }
            }
}