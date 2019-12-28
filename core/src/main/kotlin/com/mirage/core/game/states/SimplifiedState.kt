package com.mirage.core.game.states

import com.mirage.core.extensions.mutableMap
import com.mirage.core.game.objects.simplified.SimplifiedBuilding
import com.mirage.core.game.objects.simplified.SimplifiedEntity

open class SimplifiedState(
        open val buildings: Map<Long, SimplifiedBuilding> = HashMap(),
        open val entities: Map<Long, SimplifiedEntity> = HashMap()
) {

    constructor(buildingsList: List<SimplifiedBuilding>, entitiesList: List<SimplifiedEntity>) : this(
            mutableMap(buildingsList.size, { it.toLong() }, { buildingsList[it] }),
            mutableMap(entitiesList.size, { it.toLong() }, { entitiesList[it] })
    )

    fun simplifiedDeepCopy(): SimplifiedState = SimplifiedState(
            buildings = buildings.mapValues { it.value.simplifiedCopy() },
            entities = entities.mapValues { it.value.simplifiedCopy() }
    )

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