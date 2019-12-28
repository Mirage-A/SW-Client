package com.mirage.logic.state

import com.mirage.core.game.states.SimplifiedState
import com.mirage.core.utils.mutableMap

class ExtendedState(
        private val extendedBuildings: MutableMap<Long, ExtendedBuilding>,
        private val extendedEntities: MutableMap<Long, ExtendedEntity>,
        private var nextBuildingID: Long,
        private var nextEntityID: Long
) : SimplifiedState(extendedBuildings, extendedEntities) {

    constructor() : this(HashMap(), HashMap(), 0L, 0L)

    constructor(buildingsList: List<ExtendedBuilding>, entitiesList: List<ExtendedEntity>) : this(
            mutableMap(buildingsList.size, { it.toLong() }, { buildingsList[it] }),
            mutableMap(entitiesList.size, { it.toLong() }, { entitiesList[it] }),
            buildingsList.size.toLong(),
            entitiesList.size.toLong()
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