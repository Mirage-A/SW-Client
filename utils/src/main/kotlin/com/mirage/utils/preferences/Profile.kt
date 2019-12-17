package com.mirage.utils.preferences

import com.mirage.utils.extensions.QuestProgress
import com.mirage.utils.game.objects.properties.Equipment
import com.mirage.utils.game.objects.properties.WeaponType

class Profile {

    val profileName: String = "You"

    val activeSkills: MutableList<String?> = mutableListOf("flame-strike", null, null, null)
    val ultimateSkill: String? = "meteor"
    val passiveSkills: MutableList<String?> = mutableListOf(null, null, null, null)

    val currentMap: String = "test"

    val currentEquipment: Equipment = Equipment(
            "default",
            "default",
            "default",
            "default",
            "default",
            "default",
            "default",
            WeaponType.UNARMED
    )

    val globalQuestProgress: QuestProgress = linkedMapOf("global-test-quest" to 1, "completed-quest" to 1000000, "failed-quest" to -1)

    //TODO Available equipment

}