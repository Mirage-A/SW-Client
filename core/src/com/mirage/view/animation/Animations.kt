package com.mirage.view.animation

import com.badlogic.gdx.Gdx
import com.mirage.controller.Platform
import com.mirage.view.TextureLoader
import java.io.File


object Animations {
    /**
     * Словарь с загруженными анимациями
     */
    private var animations: MutableMap<AnimationType, MutableMap<String, Animation>> = HashMap()

    init {
        for (type in AnimationType.values()) {
            animations[type] = HashMap()
        }
    }

    /**
     * Загружает данную анимацию, если она еще не загружена, и возвращает эту анимацию
     */
    fun getAnimation(animationType: AnimationType, name: String) : Animation {
        if (animations[animationType]!![name] == null) {
            animations[animationType]!![name] = Animation(Gdx.files.internal(Platform.ASSETS_PATH + animationType.toString() + "/" + name + ".swa").file())
        }
        return animations[animationType]!![name]!!
    }
}