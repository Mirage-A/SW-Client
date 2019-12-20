package com.mirage.utils.preferences

import com.badlogic.gdx.Input
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicIntegerArray

class Settings {

    val desktopFullScreen = AtomicBoolean(true)

    val activeSkillBindings = AtomicIntegerArray(intArrayOf(Input.Keys.NUM_1, Input.Keys.NUM_2, Input.Keys.NUM_4, Input.Keys.NUM_5))
    val ultimateSkillBinding = AtomicInteger(Input.Keys.NUM_3)

}