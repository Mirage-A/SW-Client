package com.mirage.model.scene

/**
 * Тип проходимости тайла
 */
enum class PassabilityType {
    ALL_FREE, // Можно пройти и прострелить
    PROJECTILE_FREE, // Нельзя пройти, но стрелы пролетают
    BLOCKED // Нельзя ни пройти, ни прострелить
}