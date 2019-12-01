package com.mirage.utils.game.objects.properties

import com.mirage.utils.game.objects.properties.WeaponType


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