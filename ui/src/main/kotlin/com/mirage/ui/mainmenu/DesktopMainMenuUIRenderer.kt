package com.mirage.ui.mainmenu

import com.mirage.utils.DEFAULT_SCREEN_HEIGHT
import com.mirage.utils.DEFAULT_SCREEN_WIDTH
import com.mirage.utils.datastructures.Rectangle
import com.mirage.utils.virtualscreen.VirtualScreen
import java.lang.Float.max

class DesktopMainMenuUIRenderer(virtualScreen: VirtualScreen) : MainMenuUIRenderer {

    private val testLabel: VirtualScreen.Label = virtualScreen.createLabel("OLOLOLOLO", Rectangle(220f, 110f, 200f, 100f))

    private val testResizableLabel: VirtualScreen.Label = virtualScreen.createAutoResizableLabel("RESIZZZZZZZZZZZZZE") {
        w, h ->
        Rectangle(- w / 2, 0f, 100f, 50f)
    }

    override fun renderUI(virtualScreen: VirtualScreen, uiState: MainMenuUIState, currentTimeMillis: Long) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        val artScale = max(virtualScreen.width / DEFAULT_SCREEN_WIDTH, virtualScreen.height / DEFAULT_SCREEN_HEIGHT)
        virtualScreen.draw("ui/art", 0f, 0f, artScale * DEFAULT_SCREEN_WIDTH, artScale * DEFAULT_SCREEN_HEIGHT)
        testLabel.draw()
        testResizableLabel.draw()

    }

}