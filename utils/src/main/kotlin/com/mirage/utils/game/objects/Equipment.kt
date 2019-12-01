package com.mirage.utils.game.objects

import com.mirage.utils.game.objects.enums.WeaponType


data class Equipment(
        val helmet: String,
        val chest: String,
        val gloves: String,
        val cloak: String,
        val legs: String,
        val rightWeapon: String,
        val leftWeapon: String,
        val weaponType: WeaponType
)