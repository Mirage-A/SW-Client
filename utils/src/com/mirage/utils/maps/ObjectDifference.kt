package com.mirage.utils.maps

import com.mirage.utils.Log

sealed class ObjectDifference(
        var name: String? = null,
        var template: String? = null,
        var x: Float? = null,
        var y: Float? = null,
        var width: Float? = null,
        var height: Float? = null,
        var isRigid: Boolean? = null,
        var speed: Float? = null,
        var moveDirection: String? = null,
        var isMoving: Boolean? = null,
        var scripts: Map<String, String>? = null
)
{
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
class BuildingDifference (
        name: String? = null,
        template: String? = null,
        x: Float? = null,
        y: Float? = null,
        width: Float? = null,
        height: Float? = null,
        isRigid: Boolean? = null,
        speed: Float? = null,
        moveDirection: String? = null,
        isMoving: Boolean? = null,
        scripts: Map<String, String>? = null
) : ObjectDifference(name, template, x, y, width, height, isRigid, speed, moveDirection, isMoving, scripts) {
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
class EntityDifference (
        name: String? = null,
        template: String? = null,
        x: Float? = null,
        y: Float? = null,
        width: Float? = null,
        height: Float? = null,
        isRigid: Boolean? = null,
        speed: Float? = null,
        moveDirection: String? = null,
        isMoving: Boolean? = null,
        scripts: Map<String, String>? = null
) : ObjectDifference(name, template, x, y, width, height, isRigid, speed, moveDirection, isMoving, scripts) {
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