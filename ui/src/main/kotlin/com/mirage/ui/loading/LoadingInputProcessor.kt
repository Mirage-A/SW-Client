package com.mirage.ui.loading

import com.badlogic.gdx.InputProcessor
import com.mirage.core.messaging.ClientMessage
import rx.subjects.Subject

interface LoadingInputProcessor : InputProcessor {

    val inputMessages: Subject<ClientMessage, ClientMessage>

}