package com.mirage.ui.game

import com.badlogic.gdx.InputProcessor
import com.mirage.utils.messaging.ClientMessage
import rx.subjects.Subject

interface GameInputProcessor : InputProcessor {

    val inputMessages: Subject<ClientMessage, ClientMessage>
}