package com.mirage.ui.screens.newgame

import com.mirage.core.utils.Log
import com.mirage.core.messaging.ChangeSceneClientMessage
import com.mirage.core.preferences.Preferences
import com.mirage.ui.screens.ClientMessageListener
import kotlin.math.min

internal fun NewGameWidgets.initializeListeners(newGameState: NewGameState, listener: ClientMessageListener) {
    warriorBtn.onPressed = {
        if (newGameState.selectedClass == "warrior") chooseNone(newGameState) else chooseWarrior(newGameState)
    }
    rogueBtn.onPressed = {
        if (newGameState.selectedClass == "rogue") chooseNone(newGameState) else chooseRogue(newGameState)
    }
    mageBtn.onPressed = {
        if (newGameState.selectedClass == "mage") chooseNone(newGameState) else chooseMage(newGameState)
    }
    confirmBtn.onPressed = {
        if (newGameState.selectedClass != "none") {
            //TODO For Android, use Gdx.input.getTextInput
            if (newGameState.preferences.createNewProfile(profileNameField.text, newGameState.selectedClass)) {
                listener(ChangeSceneClientMessage(ChangeSceneClientMessage.Scene.SINGLEPLAYER_GAME))
            }
        }
    }
    chooseNone(newGameState)
}


/**
 * Creates a new profile with name [name], with starting specialization [selectedClass].
 * [selectedClass] can be equal to "mage", "rogue" or "warrior".
 * @return true if profile was successfully created.
 */
private fun Preferences.createNewProfile(name: String, selectedClass: String): Boolean {
    val filtered = name.filter { it.isLetterOrDigit() }
    if (filtered.isEmpty()) return false
    val validatedName = filtered.substring(0, min(filtered.length, 16))
    if (validatedName in account.profiles) return false
    Log.i("Creating profile {$validatedName} and class $selectedClass")
    account.profiles.add(validatedName)
    switchProfile(validatedName)
    with(profile) {
        profileName = validatedName
        when (selectedClass) {
            "mage" -> {
                //TODO Default mage skills, map, equipment etc.
            }
            "rogue" -> {
                //TODO Default rogue skills, map, equipment etc.
            }
            "warrior" -> {
                //TODO Default warrior skills, map, equipment etc.
            }
            else -> return false
        }
    }
    return true
}


private fun NewGameWidgets.chooseNone(newGameState: NewGameState) {
    newGameState.selectedClass = "none"
    classNameLabel.isVisible = false
    confirmBtn.isVisible = false
    classArt.isVisible = false
    descriptionLabel.text = "Choose starting specialization"
    warriorBtn.borderTextureName = "ui/new-game/description-background"
    warriorBtn.textureName = "ui/new-game/warrior-icon"
    rogueBtn.borderTextureName = "ui/new-game/description-background"
    rogueBtn.textureName = "ui/new-game/rogue-icon"
    mageBtn.borderTextureName = "ui/new-game/description-background"
    mageBtn.textureName = "ui/new-game/mage-icon"
}

private fun NewGameWidgets.chooseWarrior(newGameState: NewGameState) {
    newGameState.selectedClass = "warrior"
    classNameLabel.isVisible = true
    classNameLabel.text = "WARFARE"
    confirmBtn.isVisible = true
    classArt.isVisible = true
    classArt.textureName = "ui/new-game/warrior-art"
    descriptionLabel.text = "Warfare description"
    warriorBtn.borderTextureName = "ui/new-game/description-background-selected"
    warriorBtn.textureName = "ui/new-game/warrior-icon-highlighted"
    rogueBtn.borderTextureName = "ui/new-game/description-background"
    rogueBtn.textureName = "ui/new-game/rogue-icon"
    mageBtn.borderTextureName = "ui/new-game/description-background"
    mageBtn.textureName = "ui/new-game/mage-icon"
}

private fun NewGameWidgets.chooseRogue(newGameState: NewGameState) {
    newGameState.selectedClass = "rogue"
    classNameLabel.isVisible = true
    classNameLabel.text = "ASSASSINATION"
    confirmBtn.isVisible = true
    classArt.isVisible = true
    classArt.textureName = "ui/new-game/rogue-art"
    descriptionLabel.text = "Assassination description"
    warriorBtn.borderTextureName = "ui/new-game/description-background"
    warriorBtn.textureName = "ui/new-game/warrior-icon"
    rogueBtn.borderTextureName = "ui/new-game/description-background-selected"
    rogueBtn.textureName = "ui/new-game/rogue-icon-highlighted"
    mageBtn.borderTextureName = "ui/new-game/description-background"
    mageBtn.textureName = "ui/new-game/mage-icon"
}

private fun NewGameWidgets.chooseMage(newGameState: NewGameState) {
    newGameState.selectedClass = "mage"
    classNameLabel.isVisible = true
    classNameLabel.text = "SORCERY"
    confirmBtn.isVisible = true
    classArt.isVisible = true
    classArt.textureName = "ui/new-game/mage-art"
    descriptionLabel.text = "Sorcery description"
    warriorBtn.borderTextureName = "ui/new-game/description-background"
    warriorBtn.textureName = "ui/new-game/warrior-icon"
    rogueBtn.borderTextureName = "ui/new-game/description-background"
    rogueBtn.textureName = "ui/new-game/rogue-icon"
    mageBtn.borderTextureName = "ui/new-game/description-background-selected"
    mageBtn.textureName = "ui/new-game/mage-icon-highlighted"
}