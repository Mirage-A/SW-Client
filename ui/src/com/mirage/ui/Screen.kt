package com.mirage.ui

import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.mirage.utils.messaging.ClientMessage
import com.mirage.utils.messaging.ServerMessage
import rx.Observable

interface Screen : InputProcessor {

    val inputMessages: Observable<ClientMessage>

    fun handleServerMessage(msg: ServerMessage)

    fun render(batch: SpriteBatch, screenWidth: Int, screenHeight: Int, currentTimeMillis: Long)

}