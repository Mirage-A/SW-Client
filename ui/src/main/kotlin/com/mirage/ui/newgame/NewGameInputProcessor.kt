package com.mirage.ui.newgame

import com.badlogic.gdx.InputProcessor
import com.mirage.utils.messaging.ClientMessage
import rx.subjects.Subject

interface NewGameInputProcessor : InputProcessor {

    val inputMessages: Subject<ClientMessage, ClientMessage>

}