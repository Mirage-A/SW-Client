package com.mirage.ui.game.quests

import com.mirage.ui.fragments.quests.QuestLoader
import org.junit.jupiter.api.Test

internal class QuestLoaderTest {

    @Test
    fun testPerformance() {
        val count = 1000
        val time = System.currentTimeMillis()
        for (i in 0 until count) {
            QuestLoader.getGuiQuestName("global-test-quest", 0, "test", QuestLoader.QuestType.GLOBAL)
        }
        val delta = System.currentTimeMillis() - time
        println(delta)
        assert(delta < 2000)
    }
}