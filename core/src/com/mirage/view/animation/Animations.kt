package com.mirage.view.animation

import com.badlogic.gdx.Gdx
import com.mirage.controller.Platform
import java.io.File
import java.io.FileWriter


object Animations {
    /**
     * Словари с загруженными анимациями
     */
    private var bodyAnimations: MutableMap<BodyAction, Animation> = HashMap()
    private var legsAnimations: MutableMap<LegsAction, Animation> = HashMap()
    private var nullAnimations: MutableMap<NullAction, Animation> = HashMap()

    /**
     * Загружает данную анимацию, если она еще не загружена, и возвращает эту анимацию
     */
    fun getBodyAnimation(action: BodyAction) : Animation {
        if (bodyAnimations[action] == null) {
            bodyAnimations[action] = Animation(Gdx.files.internal(Platform.ASSETS_PATH + "animations/BODY/" + action.toString() + ".swa").read())
        }
        return bodyAnimations[action]!!
    }
    fun getLegsAnimation(action: LegsAction) : Animation {
        if (legsAnimations[action] == null) {
            legsAnimations[action] = Animation(Gdx.files.internal(Platform.ASSETS_PATH + "animations/LEGS/" + action.toString() + ".swa").read())
        }
        return legsAnimations[action]!!
    }
    fun getNullAnimation(action: NullAction) : Animation {
        if (nullAnimations[action] == null) {
            nullAnimations[action] = Animation(Gdx.files.internal(Platform.ASSETS_PATH + "animations/NULL/" + action.toString() + ".swa").read())
        }
        return nullAnimations[action]!!
    }
}