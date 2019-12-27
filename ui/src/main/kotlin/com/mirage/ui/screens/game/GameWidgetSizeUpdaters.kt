package com.mirage.ui.screens.game

import com.mirage.core.datastructures.Point
import com.mirage.core.datastructures.Rectangle


private const val skillPaneMargin = 8f // Отступ между навыками, между навыками и полосой здоровья и между полосой здоровья и экраном
private const val skillIconSize = 64f
internal const val skillBorderSize = 4f
private const val skillBtnSize = skillIconSize + skillBorderSize * 2f

private const val ultimateSkillBtnSize = 134f
private const val ultimateIconSize = 128f

private const val playerHealthWidth = ultimateSkillBtnSize + skillBtnSize * 4f + skillPaneMargin * 4f
private const val playerHealthHeight = 64f
internal const val playerHealthBorderMargin = 4f

private const val targetNameAreaWidth = playerHealthWidth * 0.75f
private const val targetNameAreaHeight = 48f

private const val microMenuBtnSize = 64f
private const val microMenuMargin = 8f
private const val settingsMenuBtnWidth = microMenuBtnSize * 3f + microMenuMargin * 2f

private const val gameOverWidth = 1000f
private const val gameOverHeight = 200f
private const val btnWidth = 400f
private const val btnHeight = 80f
private const val gameOverMargin = 32f
private const val gameOverMessageHeight = 80f

internal fun GameWidgets.initializeSizeUpdaters() {
    activeSkills[0].sizeUpdater = { _, h ->
        Rectangle(-(ultimateSkillBtnSize / 2f + skillPaneMargin * 2f + skillBtnSize * 1.5f),
                -h / 2f + skillPaneMargin * 2f + playerHealthHeight + skillBtnSize / 2f,
                skillBtnSize, skillBtnSize)
    }
    activeSkills[1].sizeUpdater = { _, h ->
        Rectangle(-(ultimateSkillBtnSize / 2f + skillPaneMargin + skillBtnSize * 0.5f),
                -h / 2f + skillPaneMargin * 2f + playerHealthHeight + skillBtnSize / 2f,
                skillBtnSize, skillBtnSize)
    }
    activeSkills[2].sizeUpdater = { _, h ->
        Rectangle(ultimateSkillBtnSize / 2f + skillPaneMargin + skillBtnSize * 0.5f,
                -h / 2f + skillPaneMargin * 2f + playerHealthHeight + skillBtnSize / 2f,
                skillBtnSize, skillBtnSize)
    }
    activeSkills[3].sizeUpdater = { _, h ->
        Rectangle(ultimateSkillBtnSize / 2f + skillPaneMargin * 2f + skillBtnSize * 1.5f,
                -h / 2f + skillPaneMargin * 2f + playerHealthHeight + skillBtnSize / 2f,
                skillBtnSize, skillBtnSize)
    }
    ultimateSkillBtn.sizeUpdater = { _, h ->
        Rectangle(0f, -h / 2f + skillPaneMargin * 2f + playerHealthHeight + ultimateSkillBtnSize / 2f,
                ultimateSkillBtnSize, ultimateSkillBtnSize)
    }
    settingsBtn.sizeUpdater = { w, h ->
        Rectangle(w / 2 - microMenuBtnSize / 2 - microMenuMargin,
                -h / 2 + microMenuBtnSize / 2 + microMenuMargin,
                microMenuBtnSize, microMenuBtnSize)
    }
    questsBtn.sizeUpdater = { w, h ->
        Rectangle(w / 2 - microMenuBtnSize * 3 / 2 - microMenuMargin * 2,
                -h / 2 + microMenuBtnSize / 2 + microMenuMargin,
                microMenuBtnSize, microMenuBtnSize)
    }
    leaveGameBtn.sizeUpdater = { w, h ->
        Rectangle(w / 2 - settingsMenuBtnWidth / 2f - microMenuMargin,
                -h / 2 + microMenuBtnSize * 1.5f + microMenuMargin,
                settingsMenuBtnWidth, microMenuBtnSize)
    }
    playerHealthPane.sizeUpdater = { _, h ->
        Rectangle(0f, -h / 2f + skillPaneMargin + playerHealthHeight / 2f, playerHealthWidth, playerHealthHeight)
    }
    targetHealthPane.sizeUpdater = { _, h ->
        Rectangle(0f, h / 2f - skillPaneMargin - playerHealthHeight / 2f, playerHealthWidth, playerHealthHeight)
    }
    targetNameArea.sizeUpdater = { _, h ->
        Rectangle(0f, h / 2f - skillPaneMargin - playerHealthHeight - targetNameAreaHeight / 2f + playerHealthBorderMargin,
                targetNameAreaWidth, targetNameAreaHeight)
    }
    gameOverText.sizeUpdater = { _, _ ->
        Rectangle(0f, 0f, gameOverWidth, gameOverHeight)
    }
    gameOverMessage.sizeUpdater = { w, h ->
        Rectangle(
                0f, h / 2f - gameOverMessageHeight / 2f - gameOverMargin,
                w - gameOverMargin * 2f, gameOverMessageHeight
        )
    }
    retryBtn.sizeUpdater = { _, h ->
        Rectangle(0f, -h / 2f + btnHeight * 1.5f + gameOverMargin, btnWidth, btnHeight)
    }
    mainMenuBtn.sizeUpdater = {_, h ->
        Rectangle(0f, - h / 2f + btnHeight * 0.5f + gameOverMargin, btnWidth, btnHeight) }
}