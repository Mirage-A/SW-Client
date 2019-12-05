package com.mirage.utils.preferences

import com.badlogic.gdx.Input
import java.util.concurrent.atomic.*

class Settings {

    val playerName = AtomicReference("You")

    val desktopFullScreen = AtomicBoolean(true)

    val activeSkillBindings = AtomicIntegerArray(intArrayOf(Input.Keys.NUM_1, Input.Keys.NUM_2, Input.Keys.NUM_4, Input.Keys.NUM_5))
    val ultimateSkillBinding = AtomicInteger(Input.Keys.NUM_3)

    val activeSkills = AtomicReferenceArray(arrayOf<String?>("flame-strike", null, null, null))
    val ultimateSkill = AtomicReference<String?>("meteor")
    val passiveSkills = AtomicReferenceArray(arrayOf<String?>(null, null, null, null))

}