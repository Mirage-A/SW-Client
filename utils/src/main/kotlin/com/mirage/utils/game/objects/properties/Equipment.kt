package com.mirage.utils.game.objects.properties



data class Equipment(
        val helmet: String,
        val chest: String,
        val cloak: String,
        val legs: String,
        val mainHand: String,
        val offHand: String,
        val weaponType: WeaponType
)

data class EquipmentData(
        val weaponType: WeaponType? = null,
        val attackDamage: Int = 0,
        val attackCooldown: Float = 0f,
        val attackRange: Float = 0f,
        val health: Int = 0,
        val armor: Int = 0,
        val haste: Int = 0,
        val power: Int = 0,
        val range: Int = 0,
        val duration: Int = 0,
        val modifier: String? = null
)