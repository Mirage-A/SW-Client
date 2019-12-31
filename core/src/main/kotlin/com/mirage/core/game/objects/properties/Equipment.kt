package com.mirage.core.game.objects.properties

import com.mirage.core.utils.EquipmentLoader
import com.mirage.core.utils.IntervalMillis

private const val unarmedDamage = 5
private const val unarmedRange = 2f
private const val unarmedCooldown = 2000L


data class Equipment(
        val helmet: String = "null",
        val chest: String = "null",
        val cloak: String = "null",
        val legs: String = "null",
        val mainHand: String = "null",
        val offHand: String = "null",
        val weaponType: WeaponType = WeaponType.UNARMED
) {

    fun getItemName(slot: EquipmentSlot) = when (slot) {
        EquipmentSlot.HELMET -> helmet
        EquipmentSlot.CHEST -> chest
        EquipmentSlot.CLOAK -> cloak
        EquipmentSlot.LEGGINGS -> legs
        EquipmentSlot.MAIN_HAND -> mainHand
        EquipmentSlot.OFF_HAND -> offHand
    }
}

class PlayerAttributes(equipmentLoader: EquipmentLoader, equipment: Equipment) {

    private val data = equipmentLoader.getEquipmentData(equipment)

    private val playerWeaponType = equipment.weaponType

    val attackDamage: Int
    val attackCooldown: IntervalMillis
    val attackRange: Float

    init {
        when (equipment.weaponType) {
            WeaponType.ONE_HANDED, WeaponType.SHIELD, WeaponType.TWO_HANDED, WeaponType.BOW, WeaponType.STAFF -> {
                val mainHandData = data[EquipmentSlot.MAIN_HAND] ?: ItemData()
                attackDamage = mainHandData.attackDamage
                attackCooldown = mainHandData.attackCooldown
                attackRange = mainHandData.attackRange
            }
            WeaponType.DUAL -> {
                val mainHandData = data[EquipmentSlot.MAIN_HAND] ?: ItemData()
                val offHandData = data[EquipmentSlot.OFF_HAND] ?: ItemData()
                attackDamage = mainHandData.attackDamage + offHandData.attackDamage
                val dualDPMS = mainHandData.attackDamage.toDouble() / mainHandData.attackCooldown.toDouble() +
                        offHandData.attackDamage.toDouble() / offHandData.attackCooldown.toDouble()
                attackCooldown = (1.0 / (dualDPMS / attackDamage.toDouble())).toLong()
                attackRange = (mainHandData.attackRange + offHandData.attackRange) / 2f
            }
            WeaponType.UNARMED -> {
                attackDamage = unarmedDamage
                attackCooldown = unarmedCooldown
                attackRange = unarmedRange
            }
        }
    }

    val maxHealth = 1000 + data.values.sumBy { it.health }
    val armor = data.values.sumBy { it.armor }
    val haste = 100 + data.values.sumBy { it.haste }
    val power = 100 + data.values.sumBy { it.power }
    val range = 100 + data.values.sumBy { it.range }
    val duration = 100 + data.values.sumBy { it.duration }
    val onAttackScripts = data.values.mapNotNull { it.onAttackScript }
    val onDefenseScripts = data.values.mapNotNull { it.onDefenseScript }

    fun toInventoryInfoString(): String = buildString {
        appendln(when (playerWeaponType) {
            WeaponType.UNARMED -> "Unarmed"
            WeaponType.ONE_HANDED -> "One-handed melee"
            WeaponType.SHIELD -> "One-handed & shield"
            WeaponType.TWO_HANDED -> "Two-handed melee"
            WeaponType.BOW -> "Bow"
            WeaponType.STAFF -> "Staff"
            WeaponType.DUAL -> "Dual wielding"
        })
        appendln("Attack damage: $attackDamage")
        appendln("Attack speed: ${(1000f / attackCooldown.toFloat()).toStringWithTwoDigits()}")
        appendln("Attack range: $attackRange")
        appendln("Maximum health: $maxHealth")
        appendln("Armor: $armor")
        appendln("Haste: $haste%")
        appendln("Power: $power%")
        appendln("Range: $range%")
        appendln("Duration: $duration%")
        data.values.forEach { if (it.modifier != null) appendln(it.modifier) }
    }

}

data class ItemData(
        val name: String = "",
        val description: String = "",
        val weaponType: WeaponType? = null,
        val attackDamage: Int = 0,
        val attackCooldown: IntervalMillis = 0,
        val attackRange: Float = 0f,
        val health: Int = 0,
        val armor: Int = 0,
        val haste: Int = 0,
        val power: Int = 0,
        val range: Int = 0,
        val duration: Int = 0,
        val modifier: String? = null,
        val onAttackScript: String? = null,
        val onDefenseScript: String? = null
) {
    fun toInventoryInfo() = buildString {
        when (weaponType) {
            WeaponType.ONE_HANDED -> appendln("One-handed")
            WeaponType.SHIELD -> appendln("Shield")
            WeaponType.TWO_HANDED -> appendln("Two-handed")
            WeaponType.STAFF -> appendln("Staff")
            WeaponType.BOW -> appendln("Bow")
            else -> {
            }
        }
        if (weaponType != null && weaponType != WeaponType.SHIELD) {
            appendln("Attack damage: $attackDamage")
            appendln("Attack speed: ${(1000f / attackCooldown.toFloat()).toStringWithTwoDigits()}")
            appendln("Attack range: $attackRange")
        }
        val modifiersList = ArrayList<String>()
        if (health != 0) modifiersList += "+$health Health"
        if (weaponType == null || weaponType == WeaponType.SHIELD) {
            appendln("Armor: $armor")
        } else if (armor != 0) {
            modifiersList += "+$armor Armor"
        }
        if (haste != 0) modifiersList += "+$haste Haste"
        if (power != 0) modifiersList += "+$power Power"
        if (range != 0) modifiersList += "+$range Range"
        if (duration != 0) modifiersList += "+$duration Duration"
        if (modifier != null) modifiersList += modifier
        appendln(modifiersList.joinToString())
        appendln(description)
    }
}

typealias EquipmentData = Map<EquipmentSlot, ItemData>

enum class EquipmentSlot {
    HELMET,
    CHEST,
    LEGGINGS,
    CLOAK,
    MAIN_HAND,
    OFF_HAND
}


fun Float.toStringWithTwoDigits() = ((this * 100f).round().toFloat() / 100f).toString()

fun Float.round(): Int {
    val int = this.toInt()
    return if (this - int.toFloat() < 0.5f) int else int + 1
}