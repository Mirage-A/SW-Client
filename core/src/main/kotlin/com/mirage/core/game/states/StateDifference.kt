package com.mirage.core.game.states

import com.mirage.core.game.objects.simplified.SimplifiedBuilding
import com.mirage.core.game.objects.simplified.SimplifiedEntity
import com.mirage.core.game.objects.simplified.SimplifiedObject
import java.util.*
import kotlin.collections.HashMap

data class StateDifference(
        val buildingsDifference: Difference<SimplifiedBuilding> = Difference(),
        val entitiesDifference: Difference<SimplifiedEntity> = Difference()
) {

    constructor(originState: SimplifiedState, finalState: SimplifiedState) : this(
            buildingsDifference = Difference(originState.buildings, finalState.buildings),
            entitiesDifference = Difference(originState.entities, finalState.entities)
    )

    fun projectOn(originState: SimplifiedState): SimplifiedState = SimplifiedState(
            buildings = buildingsDifference.projectOn(originState.buildings),
            entities = entitiesDifference.projectOn(originState.entities)
    )

}

data class Difference<T: SimplifiedObject>(
        val new : Map<Long, T> = HashMap(),
        val removed : Collection<Long> = TreeSet(),
        val changed : Map<Long, T> = HashMap()
) {

    constructor(originState: Map<Long, T>, finalState: Map<Long, T>) : this(
            new = finalState.filterKeys { !originState.containsKey(it) },
            removed = originState.keys.filterNot { finalState.containsKey(it) },
            changed = finalState.filterNot { it.value == originState[it.key] ?: it.value }
    )

    fun projectOn(originState: Map<Long, T>) : Map<Long, T> =
            originState
                    .filterKeys { !removed.contains(it) }
                    .mapValues { changed[it.key] ?: it.value }
                    .plus(new)

}