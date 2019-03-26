package com.mirage.client.controllers

import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.Screen
import com.badlogic.gdx.utils.Disposable

interface Controller : InputProcessor, Disposable {
    val screen: Screen
}