package com.mirage.utils.game.states

import com.mirage.utils.extensions.mutableMap
import com.mirage.utils.game.objects.simplified.SimplifiedBuilding
import com.mirage.utils.game.objects.simplified.SimplifiedEntity

open class SimplifiedState(
        open val buildings: Map<Long, SimplifiedBuilding> = HashMap(),
        open val entities: Map<Long, SimplifiedEntity> = HashMap()
) {

    constructor(buildingsList: List<SimplifiedBuilding>, entitiesList: List<SimplifiedEntity>): this(
            mutableMap(buildingsList.size, {it + Long.MIN_VALUE}, {buildingsList[it]}),
            mutableMap(entitiesList.size, {it + Long.MIN_VALUE}, {entitiesList[it]})
    )

    fun simplifiedDeepCopy(): SimplifiedState = SimplifiedState(
            buildings = buildings.mapValues { it.value.simplifiedCopy() },
            entities = entities.mapValues { it.value.simplifiedCopy() }
    )

    fun addBuildingAndCopy(building: SimplifiedBuilding): SimplifiedState {
        val newBuildings = HashMap(buildings)
        newBuildings[newBuildings.keys.max() ?: Long.MIN_VALUE + 1L] = building
        return SimplifiedState(newBuildings, entities)
    }

    fun addEntityAndCopy(entity: SimplifiedEntity): SimplifiedState {
        val newEntities = HashMap(entities)
        newEntities[newEntities.keys.max() ?: Long.MIN_VALUE + 1L] = entity
        return SimplifiedState(buildings, newEntities)
    }

    override fun equals(other: Any?): Boolean {
        if (other !is SimplifiedState) return false
        return buildings == other.buildings && entities == other.entities
    }

    override fun hashCode(): Int {
        return buildings.hashCode() * 31 + entities.hashCode()
    }

    override fun toString(): String {
        return "SimplifiedState(buildings=$buildings entities=$entities)"
    }

}