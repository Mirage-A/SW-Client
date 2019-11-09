package com.mirage.view.objectdrawers.animation

import com.mirage.utils.Assets
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
        val cached = bodyAnimationsCache[action]
        return if (cached == null) {
            val animation = Animation(Assets.loadAnimation("BODY/$action"))
            bodyAnimationsCache[action] = animation
            animation
        }
        else cached
    }
    fun getLegsAnimation(action: LegsAction) : Animation {
        val cached = legsAnimationsCache[action]
        return if (cached == null) {
            val animation = Animation(Assets.loadAnimation("LEGS/$action"))
            legsAnimationsCache[action] = animation
            animation
        }
        else cached
    }
    fun getNullAnimation(action: NullAction) : Animation {
        val cached = nullAnimationsCache[action]
        return if (cached == null) {
            val animation = Animation(Assets.loadAnimation("NULL/$action"))
            nullAnimationsCache[action] = animation
            animation
        }
        else cached
    }
    fun getObjectAnimation(name: String) : Animation {
        val cached = objectAnimationsCache[name]
        return if (cached == null) {
            val animation = Animation(Assets.loadAnimation("OBJECT/$name"))
            objectAnimationsCache[name] = animation
            animation
        }
        else cached
    }

    /**
     * Очищает кэш с загруженными анимациями
     */
    fun clearCache() {
        bodyAnimationsCache.clear()
        legsAnimationsCache.clear()
        nullAnimationsCache.clear()
        objectAnimationsCache.clear()
    }

}