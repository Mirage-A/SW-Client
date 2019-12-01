package com.mirage.utils.game.objects

import java.util.*
import kotlin.collections.HashMap

data class StateDifference(
        val newObjects : Map<Long, SimplifiedObject> = HashMap(),
        val removedObjects : Collection<Long> = TreeSet(),
        val changedObjects : Map<Long, SimplifiedObject> = HashMap()
) {

    constructor(originState: Map<Long, SimplifiedObject>, finalState: Map<Long, SimplifiedObject>) : this(
            newObjects = finalState.filterKeys { !originState.containsKey(it) },
            removedObjects = originState.keys.filterNot { finalState.containsKey(it) },
            changedObjects = finalState.filterNot { it.value == originState[it.key] ?: it.value }
    )

    fun projectOn(originState: Map<Long, SimplifiedObject>) : Map<Long, SimplifiedObject> =
            originState
                    .filterKeys { !removedObjects.contains(it) }
                    .mapValues { changedObjects[it.key] ?: it.value }
                    .plus(newObjects)

}