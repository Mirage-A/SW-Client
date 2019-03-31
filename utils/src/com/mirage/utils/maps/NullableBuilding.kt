package com.mirage.utils.maps

/**
 * Строение, в котором примитивные типы nullable. Нужно при загрузке экземпляров и объединении с шаблоном.
 */
internal data class NullableBuilding (
        val name: String?,
        val template: String?,
        val x: Float?,
        val y: Float?,
        val width: Float?,
        val height: Float?,
        val state: String?,
        val isRigid: Boolean?,
        val scripts: Map<String, String>?
)