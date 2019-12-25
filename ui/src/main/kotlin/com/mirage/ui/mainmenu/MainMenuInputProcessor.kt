package com.mirage.ui.mainmenu

import com.badlogic.gdx.InputProcessor
import com.mirage.core.messaging.ClientMessage
import rx.subjects.Subject

interface MainMenuInputProcessor : InputProcessor {

    val inputMessages: Subject<ClientMessage, ClientMessage>

}