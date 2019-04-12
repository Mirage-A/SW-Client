package com.mirage.utils.maps

/**
 * Изменяемый объект, в котором удобно хранить промежуточные изменения другого объекта, а затем объединить исходный
 * объект с этим, чтобы перезаписать то, что было изменено.
 * Работать с этим объектом нужно только в однопоточной среде!
 */
class BuildingDifference (
        var name: String? = null,
        var template: String? = null,
        var x: Float? = null,
        var y: Float? = null,
        var width: Float? = null,
        var height: Float? = null,
        var state: String? = null,
        var isRigid: Boolean? = null,
        var scripts: Map<String, String>? = null
) {
    /**
     * "Проецирует" эту разность на [origin], возвращая новое [Building], свойства которого равны свойствам
     * данного объекта, если они не null, иначе они равны свойствам [origin].
     * Метод позволяет применить некоторые изменения к изначальному объекту и получить новый.
     */
    fun projectOn(origin: Building) : Building =
            Building(
                    id = origin.id,
                    name = name ?: origin.name,
                    template = template ?: origin.template,
                    x = x ?: origin.x,
                    y = y ?: origin.y,
                    width = width ?: origin.width,
                    height = height ?: origin.height,
                    state = state ?: origin.state,
                    isRigid = isRigid ?: origin.isRigid,
                    scripts = scripts ?: origin.scripts
            )

}