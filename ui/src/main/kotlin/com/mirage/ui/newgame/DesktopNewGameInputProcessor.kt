package com.mirage.ui.newgame

import com.badlogic.gdx.Input
import com.mirage.utils.datastructures.Point
import com.mirage.utils.messaging.ChangeSceneClientMessage
import com.mirage.utils.messaging.ClientMessage
import com.mirage.utils.messaging.EventSubjectAdapter
import com.mirage.utils.preferences.Account
import com.mirage.utils.preferences.Prefs
import com.mirage.utils.preferences.Profile
import com.mirage.utils.preferences.createNewProfile
import rx.subjects.Subject
import kotlin.math.min

class DesktopNewGameInputProcessor(private val uiState: NewGameUIState) : NewGameInputProcessor {

    override val inputMessages: Subject<ClientMessage, ClientMessage> = EventSubjectAdapter()

    private fun chooseNone() {
        uiState.selectedClass = "none"
        uiState.classNameLabel.isVisible = false
        uiState.confirmBtn.isVisible = false
        uiState.classArt.isVisible = false
        uiState.descriptionLabel.label.text = "Choose starting specialization"
        uiState.warriorBtn.borderTextureName = "ui/new-game/description-background"
        uiState.warriorBtn.textureName = "ui/new-game/warrior-icon"
        uiState.rogueBtn.borderTextureName = "ui/new-game/description-background"
        uiState.rogueBtn.textureName = "ui/new-game/rogue-icon"
        uiState.mageBtn.borderTextureName = "ui/new-game/description-background"
        uiState.mageBtn.textureName = "ui/new-game/mage-icon"
    }

    private fun chooseWarrior() {
        uiState.selectedClass = "warrior"
        uiState.classNameLabel.isVisible = true
        uiState.classNameLabel.label.text = "WARFARE"
        uiState.confirmBtn.isVisible = true
        uiState.classArt.isVisible = true
        uiState.classArt.textureName = "ui/new-game/warrior-art"
        uiState.descriptionLabel.label.text = "Warfare description"
        uiState.warriorBtn.borderTextureName = "ui/new-game/description-background-selected"
        uiState.warriorBtn.textureName = "ui/new-game/warrior-icon-highlighted"
        uiState.rogueBtn.borderTextureName = "ui/new-game/description-background"
        uiState.rogueBtn.textureName = "ui/new-game/rogue-icon"
        uiState.mageBtn.borderTextureName = "ui/new-game/description-background"
        uiState.mageBtn.textureName = "ui/new-game/mage-icon"
    }

    private fun chooseRogue() {
        uiState.selectedClass = "rogue"
        uiState.classNameLabel.isVisible = true
        uiState.classNameLabel.label.text = "ASSASSINATION"
        uiState.confirmBtn.isVisible = true
        uiState.classArt.isVisible = true
        uiState.classArt.textureName = "ui/new-game/rogue-art"
        uiState.descriptionLabel.label.text = "Assassination description"
        uiState.warriorBtn.borderTextureName = "ui/new-game/description-background"
        uiState.warriorBtn.textureName = "ui/new-game/warrior-icon"
        uiState.rogueBtn.borderTextureName = "ui/new-game/description-background-selected"
        uiState.rogueBtn.textureName = "ui/new-game/rogue-icon-highlighted"
        uiState.mageBtn.borderTextureName = "ui/new-game/description-background"
        uiState.mageBtn.textureName = "ui/new-game/mage-icon"
    }

    private fun chooseMage() {
        uiState.selectedClass = "mage"
        uiState.classNameLabel.isVisible = true
        uiState.classNameLabel.label.text = "SORCERY"
        uiState.confirmBtn.isVisible = true
        uiState.classArt.isVisible = true
        uiState.classArt.textureName = "ui/new-game/mage-art"
        uiState.descriptionLabel.label.text = "Sorcery description"
        uiState.warriorBtn.borderTextureName = "ui/new-game/description-background"
        uiState.warriorBtn.textureName = "ui/new-game/warrior-icon"
        uiState.rogueBtn.borderTextureName = "ui/new-game/description-background"
        uiState.rogueBtn.textureName = "ui/new-game/rogue-icon"
        uiState.mageBtn.borderTextureName = "ui/new-game/description-background-selected"
        uiState.mageBtn.textureName = "ui/new-game/mage-icon-highlighted"
    }

    init {
        chooseNone()
        uiState.warriorBtn.onPressed = {if (uiState.selectedClass == "warrior") chooseNone() else chooseWarrior()}
        uiState.rogueBtn.onPressed = {if (uiState.selectedClass == "rogue") chooseNone() else chooseRogue()}
        uiState.mageBtn.onPressed = {if (uiState.selectedClass == "mage") chooseNone() else chooseMage()}
        uiState.confirmBtn.onPressed = {
            if (uiState.selectedClass != "none") {
                //TODO For Android, use Gdx.input.getTextInput
                if (createNewProfile(uiState.textField?.text ?: "", uiState.selectedClass)) {
                    inputMessages.onNext(ChangeSceneClientMessage(ChangeSceneClientMessage.Scene.SINGLEPLAYER_GAME))
                }
            }
        }
    }




    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        val virtualPoint = getVirtualPoint(screenX, screenY)
        uiState.widgets.forEach { it.touchUp(virtualPoint) }
        return false
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        val virtualPoint = getVirtualPoint(screenX, screenY)
        uiState.widgets.forEach { it.mouseMoved(virtualPoint) }
        return false
    }

    override fun keyTyped(character: Char): Boolean {
        return false
    }

    override fun scrolled(amount: Int): Boolean {
        return false
    }

    override fun keyUp(keycode: Int): Boolean {
        return false
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        return false
    }

    override fun keyDown(keycode: Int): Boolean {
        if (keycode == Input.Keys.ESCAPE)
            inputMessages.onNext(ChangeSceneClientMessage(ChangeSceneClientMessage.Scene.MAIN_MENU))
        return false
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        val virtualPoint = getVirtualPoint(screenX, screenY)
        uiState.widgets.forEach { it.touchDown(virtualPoint) }
        return false
    }

    private fun getVirtualPoint(screenX: Int, screenY: Int) =
            uiState.virtualScreen.projectRealPointOnVirtualScreen(Point(screenX.toFloat(), screenY.toFloat()))

}