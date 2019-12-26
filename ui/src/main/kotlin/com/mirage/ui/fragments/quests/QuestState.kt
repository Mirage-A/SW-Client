package com.mirage.ui.fragments.quests

import com.mirage.core.extensions.GameMapName
import com.mirage.core.extensions.QuestProgress

internal class QuestState(val gameMapName: GameMapName) {

    /** Only 1 of these can be not-null at a time */
    var selectedLocalQuest: String? = null
    var selectedGlobalQuest: String? = null

    val localQuestProgress = QuestProgress()

}