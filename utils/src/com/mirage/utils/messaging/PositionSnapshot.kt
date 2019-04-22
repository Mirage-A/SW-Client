package com.mirage.utils.messaging

import com.mirage.utils.gameobjects.GameObjects

/**
 * Состояние карты с привяза
 */
data class Snapshot (
        val objects: GameObjects,
        val createdTimeMillis : Long = System.currentTimeMillis()
        ) : Comparable<Snapshot> {


    /**
     * Сравнивает состояния по времени создания
     */
    override fun compareTo(other: Snapshot): Int = when {
        createdTimeMillis > other.createdTimeMillis -> 1
        createdTimeMillis == other.createdTimeMillis -> 0
        else -> -1
    }

}
