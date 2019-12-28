package com.mirage.ui.screens.mainmenu

import com.mirage.core.DEFAULT_SCREEN_HEIGHT
import com.mirage.core.DEFAULT_SCREEN_WIDTH
import com.mirage.core.datastructures.Rectangle
import kotlin.math.max
import kotlin.math.min


private const val btnWidth = 400f
private const val btnHeight = 80f
private const val profileWindowWidth = 480f
private const val profileWindowX = -btnWidth / 2f - profileWindowWidth / 2f
private const val profileBodyHeight = 440f
internal const val profileBtnCount = 4
private const val profileBtnMargin = 20f
private const val profileArrowSize = 40f
private const val profileArrowMargin = (btnHeight - profileArrowSize) / 2f
private const val profileArrowShift = profileWindowWidth / 2f - profileArrowMargin - profileArrowSize / 2f

internal fun MainMenuWidgets.initializeSizeUpdaters(mainMenuState: MainMenuState) {
    backgroundArt.sizeUpdater = { virtualWidth, virtualHeight ->
        val scale = max(virtualWidth / DEFAULT_SCREEN_WIDTH, virtualHeight / DEFAULT_SCREEN_HEIGHT)
        Rectangle(0f, 0f, DEFAULT_SCREEN_WIDTH * scale, DEFAULT_SCREEN_HEIGHT * scale)
    }
    singlePlayerBtn.sizeUpdater = { _, virtualHeight ->
        Rectangle(0f, -virtualHeight / 2 + btnHeight * (if (mainMenuState.newGame) 5 else 7) / 2, btnWidth, btnHeight)
    }
    multiPlayerBtn.sizeUpdater = { _, virtualHeight ->
        Rectangle(0f, -virtualHeight / 2 + btnHeight * 5 / 2, btnWidth, btnHeight)
    }
    settingsBtn.sizeUpdater = { _, virtualHeight ->
        Rectangle(0f, -virtualHeight / 2 + btnHeight * 3 / 2, btnWidth, btnHeight)
    }
    exitBtn.sizeUpdater = { _, virtualHeight ->
        Rectangle(0f, -virtualHeight / 2 + btnHeight / 2, btnWidth, btnHeight)
    }
    profileNameArea.sizeUpdater = { _, virtualHeight ->
        Rectangle(0f, virtualHeight / 2 - btnHeight / 2, btnWidth, btnHeight)
    }
    changeProfileBtn.sizeUpdater = { _, virtualHeight ->
        Rectangle(0f, virtualHeight / 2 - btnHeight * 3 / 2, btnWidth, btnHeight)
    }
    profileWindowHead.sizeUpdater = { _, virtualHeight ->
        Rectangle(profileWindowX, virtualHeight / 2f - btnHeight / 2f, profileWindowWidth, btnHeight)
    }
    profileWindowBody.sizeUpdater = { _, virtualHeight ->
        Rectangle(profileWindowX, virtualHeight / 2f - btnHeight - profileBodyHeight / 2f, profileWindowWidth, profileBodyHeight)
    }
    for ((i, btn) in profileWindowButtons.withIndex()) {
        btn.sizeUpdater = { _, h ->
            Rectangle(profileWindowX, h / 2f - btnHeight * (i + 1.5f) - profileBtnMargin * (i + 1), btnWidth, btnHeight)
        }
    }
    profileWindowLeftArrow.sizeUpdater = { _, virtualHeight ->
        Rectangle(profileWindowX - profileArrowShift, virtualHeight / 2f - btnHeight / 2f, profileArrowSize, profileArrowSize)
    }
    profileWindowRightArrow.sizeUpdater = { _, virtualHeight ->
        Rectangle(profileWindowX + profileArrowShift, virtualHeight / 2f - btnHeight / 2f, profileArrowSize, profileArrowSize)
    }
    profileWindowPageLabel.sizeUpdater = { _, virtualHeight ->
        Rectangle(profileWindowX, virtualHeight / 2f - btnHeight / 2f, profileWindowWidth, btnHeight)
    }
}