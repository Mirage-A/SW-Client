package com.mirage.view.drawers.animation

import com.mirage.core.utils.Assets
import java.util.*

/**
 * This object should be used to load animations from assets.
 * All animations are cached after first loading.
 */
object AnimationLoader {

    private var bodyAnimationsCache: MutableMap<String, Animation> = HashMap()
    private var legsAnimationsCache: MutableMap<String, Animation> = HashMap()
    private var objectAnimationsCache: MutableMap<String, Animation> = HashMap()

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

    fun clearCache() {
        bodyAnimationsCache.clear()
        legsAnimationsCache.clear()
        objectAnimationsCache.clear()
    }

}