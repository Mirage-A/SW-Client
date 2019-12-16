package com.mirage.utils.preferences

import com.mirage.utils.extensions.ConcurrentQuestProgress
import com.mirage.utils.game.objects.properties.Equipment
import com.mirage.utils.game.objects.properties.WeaponType
import java.util.concurrent.atomic.AtomicReference
import java.util.concurrent.atomic.AtomicReferenceArray

class Profile {

    val profileName = AtomicReference("You")

    val activeSkills = AtomicReferenceArray(arrayOf<String?>("flame-strike", null, null, null))
    val ultimateSkill = AtomicReference<String?>("meteor")
    val passiveSkills = AtomicReferenceArray(arrayOf<String?>(null, null, null, null))

    val currentMap = AtomicReference("test")

    val currentEquipment = AtomicReference(Equipment(
            "default",
            "default",
            "default",
            "default",
            "default",
            "default",
            "default",
            WeaponType.UNARMED
    ))

    val globalQuestProgress = ConcurrentQuestProgress()

    //TODO Available equipment

}