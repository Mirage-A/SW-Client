package com.mirage.utils.preferences

import com.mirage.utils.extensions.QuestProgress
import com.mirage.utils.game.objects.properties.Equipment
import com.mirage.utils.game.objects.properties.WeaponType

class Profile {

    val profileName: String = "You"

    var activeSkills: MutableList<String?> = mutableListOf("flame-strike", null, null, null)
    var ultimateSkill: String? = "meteor"
    var passiveSkills: MutableList<String?> = mutableListOf(null, null, null, null)

    var currentMap: String = "test"

    var currentEquipment: Equipment = Equipment(
            "default",
            "default",
            "default",
            "default",
            "default",
            "null",
            WeaponType.ONE_HANDED
    )

    val globalQuestProgress: QuestProgress = linkedMapOf("global-test-quest" to 1, "completed-quest" to 1000000, "failed-quest" to -1)

    //TODO Available equipment

}