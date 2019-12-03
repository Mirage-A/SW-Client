package com.mirage.utils.game.states

import com.mirage.utils.extensions.mutableMap
import com.mirage.utils.game.objects.extended.ExtendedBuilding
import com.mirage.utils.game.objects.extended.ExtendedEntity

class ExtendedState(
        private val extendedBuildings: MutableMap<Long, ExtendedBuilding>,
        private val extendedEntities: MutableMap<Long, ExtendedEntity>,
        private var nextBuildingID: Long,
        private var nextEntityID: Long
) : SimplifiedState(extendedBuildings, extendedEntities) {

    constructor() : this(HashMap(), HashMap(), Long.MIN_VALUE, Long.MIN_VALUE)

    constructor(buildingsList: List<ExtendedBuilding>, entitiesList: List<ExtendedEntity>) : this(
            mutableMap(buildingsList.size, {it + Long.MIN_VALUE}, {buildingsList[it]}),
            mutableMap(entitiesList.size, {it + Long.MIN_VALUE}, {entitiesList[it]}),
            buildingsList.size + Long.MIN_VALUE,
            entitiesList.size + Long.MIN_VALUE
    )

    override val buildings: Map<Long, ExtendedBuilding> = extendedBuildings
    override val entities: Map<Long, ExtendedEntity> = extendedEntities


    fun addBuilding(building: ExtendedBuilding): Long {
        extendedBuildings[nextBuildingID] = building
        return nextBuildingID++
    }

    operator fun plusAssign(building: ExtendedBuilding) {
        addBuilding(building)
    }

    fun removeBuilding(buildingID: Long) = extendedBuildings.remove(buildingID)

    fun addEntity(entity: ExtendedEntity): Long {
        extendedEntities[nextEntityID] = entity
        return nextEntityID++
    }

    operator fun plusAssign(entity: ExtendedEntity) {
        addEntity(entity)
    }

    fun removeEntity(entityID: Long) = extendedEntities.remove(entityID)

}