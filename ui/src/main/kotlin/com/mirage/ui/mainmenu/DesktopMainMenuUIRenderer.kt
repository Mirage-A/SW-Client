package com.mirage.ui.mainmenu

import com.mirage.ui.game.GameUIState
import com.mirage.utils.DEFAULT_SCREEN_HEIGHT
import com.mirage.utils.DEFAULT_SCREEN_WIDTH
import com.mirage.utils.virtualscreen.VirtualScreen
import java.lang.Float.max

class DesktopMainMenuUIRenderer : MainMenuUIRenderer {

    override fun renderUI(virtualScreen: VirtualScreen, uiState: MainMenuUIState, currentTimeMillis: Long) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        val artScale = max(virtualScreen.width / DEFAULT_SCREEN_WIDTH, virtualScreen.height / DEFAULT_SCREEN_HEIGHT)
        virtualScreen.draw("ui/art", 0f, 0f, artScale * DEFAULT_SCREEN_WIDTH, artScale * DEFAULT_SCREEN_HEIGHT)
    }

}