package com.mirage.ui.fragments.quests

import com.mirage.core.preferences.Preferences
import com.mirage.core.utils.GameMapName
import com.mirage.core.utils.QuestProgress

internal class QuestState(
        val gameMapName: GameMapName,
        val localQuestProgress: QuestProgress,
        val preferences: Preferences,
        val questLoader: QuestLoader
) {

    /** Only 1 of these can be not-null at a time */
    var selectedLocalQuest: String? = null
    var selectedGlobalQuest: String? = null

}