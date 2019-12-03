package com.mirage.utils.game.objects.properties


/**
 * Тип оружия гуманоида (одноручное, двуручное, два одноручных, одноручное и щит, лук и т.д.)
 */
enum class WeaponType {
    UNARMED,
    ONE_HANDED,
    ONE_HANDED_AND_SHIELD,
    DUAL,
    TWO_HANDED,
    BOW,
    STAFF;
    companion object {
        /**
         * Преобразует строку в тип оружия (операция, обратная toString())
         * @throws Exception если строка не соответствует никакому типу оружия
         */
        fun fromString(str: String) : WeaponType {
            return when (str) {
                "UNARMED" -> UNARMED
                "ONE_HANDED" -> ONE_HANDED
                "ONE_HANDED_AND_SHIELD" -> ONE_HANDED_AND_SHIELD
                "DUAL" -> DUAL
                "TWO_HANDED" -> TWO_HANDED
                "BOW" -> BOW
                "STAFF" -> STAFF
                else -> throw Exception("Incorrect weapon type: $str")
            }
        }
    }
}