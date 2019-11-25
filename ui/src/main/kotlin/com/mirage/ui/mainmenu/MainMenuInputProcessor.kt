package com.mirage.ui.mainmenu

import com.badlogic.gdx.InputProcessor
import com.mirage.ui.game.GameUIState
import com.mirage.utils.messaging.ClientMessage
import rx.subjects.Subject

interface MainMenuInputProcessor : InputProcessor {

    val inputMessages: Subject<ClientMessage, ClientMessage>

    val uiState: MainMenuUIState

}