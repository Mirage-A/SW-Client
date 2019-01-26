package com.mirage.model.scene

/**
 * Тип проходимости тайла
 */
enum class ApproachabilityType {
    ALL_FREE, // Можно пройти и прострелить
    PROJECTILE_FREE, // Нельзя пройти, но стрелы пролетают
    BLOCKED; // Нельзя ни пройти, ни прострелить

    /**
     * Является ли данный тайл проходимым
     */
    fun isWalkable() : Boolean {
        return this == ALL_FREE
    }

    /**
     * Можно ли прострелить данный тайл
     */
    fun isShootable() : Boolean {
        return (this == ALL_FREE) || (this == PROJECTILE_FREE)
    }
}