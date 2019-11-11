package com.mirage.ui.game

import com.badlogic.gdx.InputProcessor
import com.mirage.utils.messaging.ClientMessage
import com.mirage.utils.messaging.EventSubjectAdapter
import rx.Observable

interface GameInputProcessor : InputProcessor {

    val inputMessages: Observable<ClientMessage>

    val uiState: GameUIState
}