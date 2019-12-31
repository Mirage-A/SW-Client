package com.mirage.core.utils

import com.google.gson.Gson
import com.mirage.core.game.objects.properties.Equipment
import com.mirage.core.game.objects.properties.EquipmentData
import com.mirage.core.game.objects.properties.ItemData
import com.mirage.core.game.objects.properties.EquipmentSlot


class EquipmentLoader(private val assets: Assets) {

    private val gson = Gson()

    private val cachedEquipmentData = HashMap<Pair<EquipmentSlot, String>, ItemData>()

    fun getItemData(itemType: EquipmentSlot, itemName: String): ItemData {
        val pair = Pair(itemType, itemName)
        val cached = cachedEquipmentData[pair]
        if (cached != null) return cached
        val onAttackScript: String? = try {
            assets.loadReader("equipment/${getEquipmentFolder(itemType)}/$itemName/on-attack.json")!!.readText()
        } catch (ex: Exception) {
            null
        }
        val onDefenseScript: String? = try {
            assets.loadReader("equipment/${getEquipmentFolder(itemType)}/$itemName/on-defense.json")!!.readText()
        } catch (ex: Exception) {
            null
        }
        val data: ItemData = try {
            gson.fromJson<ItemData>(
                    assets.loadReader("equipment/${getEquipmentFolder(itemType)}/$itemName/data.json")!!
            )!!.copy(
                    onAttackScript = onAttackScript,
                    onDefenseScript = onDefenseScript
            )
        } catch (ex: Exception) {
            Log.e("Error while loading equipment data $itemType $itemName")
            ItemData()
        }

        cachedEquipmentData[pair] = data
        return data
    }

    fun getEquipmentData(equipment: Equipment): EquipmentData = linkedMapOf(
            EquipmentSlot.HELMET to getItemData(EquipmentSlot.HELMET, equipment.helmet),
            EquipmentSlot.CHEST to getItemData(EquipmentSlot.CHEST, equipment.chest),
            EquipmentSlot.CLOAK to getItemData(EquipmentSlot.CLOAK, equipment.cloak),
            EquipmentSlot.LEGGINGS to getItemData(EquipmentSlot.LEGGINGS, equipment.legs),
            EquipmentSlot.MAIN_HAND to getItemData(EquipmentSlot.MAIN_HAND, equipment.mainHand),
            EquipmentSlot.OFF_HAND to getItemData(EquipmentSlot.OFF_HAND, equipment.offHand)
    )

    private fun getEquipmentFolder(type: EquipmentSlot) = when (type) {
        EquipmentSlot.HELMET -> "head"
        EquipmentSlot.CHEST -> "body"
        EquipmentSlot.LEGGINGS -> "legs"
        EquipmentSlot.CLOAK -> "cloak"
        EquipmentSlot.MAIN_HAND, EquipmentSlot.OFF_HAND -> "weapon"
    }


}