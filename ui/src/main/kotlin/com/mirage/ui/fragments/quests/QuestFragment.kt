package com.mirage.ui.fragments.quests

import com.mirage.core.datastructures.Point
import com.mirage.core.extensions.GameMapName
import com.mirage.core.extensions.QuestProgress
import com.mirage.core.virtualscreen.VirtualScreen
import com.mirage.ui.widgets.Widget

/** Window with information about current quest progress */
internal class QuestFragment(virtualScreen: VirtualScreen, gameMapName: GameMapName, localQuestProgress: QuestProgress) : Widget {

    private val questState = QuestState(gameMapName, localQuestProgress)
    private val subWidgets = QuestWidgets(virtualScreen).apply {
        initializeSizeUpdaters()
        initializeListeners(questState)
    }

    val localQuestProgress: QuestProgress = questState.localQuestProgress

    override var isVisible: Boolean = false

    /** This method must be invoked on any quest progress change */
    fun updateQuestWindow() {
        subWidgets.updateQuestWindow(questState)
    }

    override fun resize(virtualWidth: Float, virtualHeight: Float) =
            subWidgets.rootWidget.resize(virtualWidth, virtualHeight)

    override fun touchUp(virtualPoint: Point, pointer: Int, button: Int): Boolean =
            isVisible && subWidgets.rootWidget.touchUp(virtualPoint, pointer, button)

    override fun touchDown(virtualPoint: Point, pointer: Int, button: Int): Boolean =
            isVisible && subWidgets.rootWidget.touchDown(virtualPoint, pointer, button)

    override fun mouseMoved(virtualPoint: Point): Boolean =
            isVisible && subWidgets.rootWidget.mouseMoved(virtualPoint)

    override fun touchDragged(virtualPoint: Point, pointer: Int): Boolean =
            isVisible && subWidgets.rootWidget.touchDragged(virtualPoint, pointer)

    override fun draw(virtualScreen: VirtualScreen) {
        if (isVisible) subWidgets.rootWidget.draw(virtualScreen)
    }
}
