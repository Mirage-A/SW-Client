package com.mirage.view.animation

import com.badlogic.gdx.Gdx
import com.mirage.controller.Platform
import com.mirage.view.Log
import java.util.*

/**
 * Синглтон, в котором загружаются и хранятся все анимации, созданные с помощью Animation Editor-а
 * Анимации загружаются лениво при вызове getAnimation и хранятся в кэше
 */
object AnimationLoader {
    /**
     * Словари с загруженными анимациями
     */
    private var bodyAnimationsCache: MutableMap<BodyAction, Animation> = HashMap()
    private var legsAnimationsCache: MutableMap<LegsAction, Animation> = HashMap()
    private var nullAnimationsCache: MutableMap<NullAction, Animation> = HashMap()
    private var objectAnimationsCache: MutableMap<String, Animation> = HashMap()

    /**
     * Возвращает данную анимацию
     * Если анимация еще не загружена, она загружается и сохраняется в кэш
     */
    fun getBodyAnimation(action: BodyAction) : Animation {
        if (bodyAnimationsCache[action] == null) {
            bodyAnimationsCache[action] = Animation(Gdx.files.internal(Platform.ASSETS_PATH + "animations/BODY/" + action.toString() + ".swa").read())
        }
        return bodyAnimationsCache[action]!!
    }
    fun getLegsAnimation(action: LegsAction) : Animation {
        if (legsAnimationsCache[action] == null) {
            legsAnimationsCache[action] = Animation(Gdx.files.internal(Platform.ASSETS_PATH + "animations/LEGS/" + action.toString() + ".swa").read())
        }
        return legsAnimationsCache[action]!!
    }
    fun getNullAnimation(action: NullAction) : Animation {
        if (nullAnimationsCache[action] == null) {
            nullAnimationsCache[action] = Animation(Gdx.files.internal(Platform.ASSETS_PATH + "animations/NULL/" + action.toString() + ".swa").read())
        }
        return nullAnimationsCache[action]!!
    }
    fun getObjectAnimation(name: String) : Animation {
        if (objectAnimationsCache[name] == null) {
            objectAnimationsCache[name] = Animation(Gdx.files.internal(Platform.ASSETS_PATH + "animations/OBJECT/$name.swa").read())
        }
        return objectAnimationsCache[name]!!
    }

    /**
     * Очищает кэш с загруженными анимациями
     */
    fun clearCache() {
        bodyAnimationsCache.clear()
        legsAnimationsCache.clear()
        nullAnimationsCache.clear()
    }

}