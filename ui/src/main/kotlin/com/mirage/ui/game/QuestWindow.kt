package com.mirage.ui.game

import com.mirage.ui.widgets.CompositeWidget
import com.mirage.ui.widgets.ImageWidget
import com.mirage.utils.datastructures.Rectangle
import com.mirage.utils.virtualscreen.VirtualScreen
import kotlin.math.min

private const val defaultBackgroundWidth = 1000f
private const val defaultBackgroundHeight = 600f
private const val defaultContentWidth = 800f
private const val defaultContentHeight = 500f
private const val questListWidthPart = 0.33f

class QuestWindow(val virtualScreen: VirtualScreen) {

    private fun getWindowScale(virtualWidth: Float, virtualHeight: Float) =
            min(virtualWidth / defaultBackgroundWidth, virtualHeight / defaultBackgroundHeight)

    private fun getBackgroundRect(virtualWidth: Float, virtualHeight: Float): Rectangle {
        val scale = getWindowScale(virtualWidth, virtualHeight)
        return Rectangle(0f, 0f, defaultBackgroundWidth * scale, defaultBackgroundHeight * scale)
    }

    private fun getContentRect(virtualWidth: Float, virtualHeight: Float): Rectangle {
        val scale = getWindowScale(virtualWidth, virtualHeight)
        return Rectangle(0f, 0f, defaultContentWidth * scale, defaultContentHeight * scale)
    }

    val backgroundImage = ImageWidget("ui/game/quests/quest-window-background") {
        w, h -> getBackgroundRect(w, h)
    }

    private fun getGlobalQuestsX(virtualWidth: Float, virtualHeight: Float): Float {
        val contentRect = getContentRect(virtualWidth, virtualHeight)
        return -contentRect.width * (0.5f - questListWidthPart / 2f)
    }

    private fun getLocalQuestsX(virtualWidth: Float, virtualHeight: Float) = -getGlobalQuestsX(virtualWidth, virtualHeight)



    val widget = CompositeWidget(backgroundImage).apply { isVisible = false }

}