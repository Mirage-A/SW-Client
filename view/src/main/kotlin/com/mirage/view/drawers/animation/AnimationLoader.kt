package com.mirage.view.drawers.animation

import com.mirage.core.Assets
import java.util.*

/**
 * Синглтон, в котором загружаются и хранятся все анимации, созданные с помощью Animation Editor-а
 * Анимации загружаются лениво при вызове getAnimation и хранятся в кэше
 */
object AnimationLoader {
    /**
     * Словари с загруженными анимациями.
     * Ключ - значение action.
     */
    private var bodyAnimationsCache: MutableMap<String, Animation> = HashMap()
    private var legsAnimationsCache: MutableMap<String, Animation> = HashMap()
    private var objectAnimationsCache: MutableMap<String, Animation> = HashMap()

    /**
     * Возвращает данную анимацию
     * Если анимация еще не загружена, она загружается и сохраняется в кэш
     */
    fun getBodyAnimation(action: String): Animation {
        val cached = bodyAnimationsCache[action]
        return if (cached == null) {
            val animation = try {
                Animation(Assets.loadAnimation("humanoid/body/$action"))
            } catch (ex: Exception) {
                Animation()
            }
            bodyAnimationsCache[action] = animation
            animation
        } else cached
    }

    fun getLegsAnimation(action: String): Animation {
        val cached = legsAnimationsCache[action]
        return if (cached == null) {
            val animation = try {
                Animation(Assets.loadAnimation("humanoid/legs/$action"))
            } catch (ex: Exception) {
                Animation()
            }
            legsAnimationsCache[action] = animation
            animation
        } else cached
    }

    fun getObjectAnimation(name: String): Animation {
        val cached = objectAnimationsCache[name]
        return if (cached == null) {
            val animation = try {
                Animation(Assets.loadAnimation("object/$name"))
            } catch (ex: Exception) {
                Animation()
            }
            objectAnimationsCache[name] = animation
            animation
        } else cached
    }

    /**
     * Очищает кэш с загруженными анимациями
     */
    fun clearCache() {
        bodyAnimationsCache.clear()
        legsAnimationsCache.clear()
        objectAnimationsCache.clear()
    }

}