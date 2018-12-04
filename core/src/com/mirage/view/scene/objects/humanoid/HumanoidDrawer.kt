package com.mirage.view.scene.objects.humanoid

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.mirage.view.scene.objects.AnimatedObjectDrawer
import java.util.HashMap


/**
 * Генерируемый с помощью Animation Editor-а класс для работы со скелетной анимацией гуманоидов
 * (анимация остальных существ задается покадрово классом SpriteAnimation)
 */
class HumanoidDrawer : AnimatedObjectDrawer {
    /**
     * Действие, которое анимируется (ожидание, бег, атака и т.д.)
     */
    var action: Action = Action.IDLE
    /**
     * Направление движения
     */
    var moveDirection: MoveDirection = MoveDirection.RIGHT
    /**
     * Тип оружия гуманоида (одноручное, двуручное, два одноручных, одноручное и щит, лук и т.д.)
     */
    var weaponType: WeaponType = WeaponType.ONE_HANDED

    /**
     * Словарь из текстур экипировки данного гуманоида
     * Должен содержать ключи head[0-7], body, handtop, handbottom, legtop, legbottom, cloak, weapon1, weapon2
     */
    var textures: MutableMap<String, AnimatedTexture>

    constructor() {
        textures = HashMap()
        for (i in 0..0) { // TODO поменять 0 на 7
            textures["head$i"] = StaticTexture(Texture(Gdx.files.internal("android/assets/equipment/head/0000$i.png")))
        }
        textures["body"] = StaticTexture(Texture(Gdx.files.internal("android/assets/equipment/body/0000.png")))
        textures["handtop"] = StaticTexture(Texture(Gdx.files.internal("android/assets/equipment/handtop/0000.png")))
        textures["handbottom"] = StaticTexture(Texture(Gdx.files.internal("android/assets/equipment/handbottom/0000.png")))
        textures["legtop"] = StaticTexture(Texture(Gdx.files.internal("android/assets/equipment/legtop/0000.png")))
        textures["legbottom"] = StaticTexture(Texture(Gdx.files.internal("android/assets/equipment/legbottom/0000.png")))
        textures["cloak"] = StaticTexture(Texture(Gdx.files.internal("android/assets/equipment/cloak/0000.png")))
        textures["weapon1"] = StaticTexture(Texture(Gdx.files.internal("android/assets/equipment/twohanded/0000.png")))
        textures["weapon2"] = StaticTexture(Texture(Gdx.files.internal("android/assets/equipment/onehanded/0000.png")))
    }

    constructor(textures: MutableMap<String, AnimatedTexture>) {
        this.textures = textures
    }

    constructor(textures: MutableMap<String, AnimatedTexture>, action: Action, moveDirection: MoveDirection, weaponType: WeaponType) {
        this.textures = textures
        this.action = action
        this.moveDirection = moveDirection
        this.weaponType = weaponType
    }


    fun curValue(startValue: Float, endValue : Float, progress : Float) : Float {
        return startValue + (endValue - startValue) * progress
    }
    override fun draw(batch: SpriteBatch, x: Float, y: Float, timePassedSinceStart: Long) {
        var timePassed = 0f + timePassedSinceStart % 1000
        when (action) {
            Action.IDLE ->
                when (moveDirection) {
                    MoveDirection.RIGHT ->
                        when (weaponType) {
                            WeaponType.ONE_HANDED ->
                            {
                                if (timePassed < 501)
                                {
                                    val progress = (timePassed - 0) / 501f
                                    batch.draw(textures["cloak"]!!.getTexture(timePassedSinceStart), x + curValue(1.0f, 1.0f, progress) - DefaultSizes.defaultWidth["cloak"]!!/2f, y + curValue(3.0f, 3.0f, progress) - DefaultSizes.defaultHeight["cloak"]!!/2f, DefaultSizes.defaultWidth["cloak"]!!/2f, DefaultSizes.defaultHeight["cloak"]!!/2f, DefaultSizes.defaultWidth["cloak"]!! + 0f, DefaultSizes.defaultHeight["cloak"]!! + 0f, curValue(120.0f, 54.0f, progress) / (DefaultSizes.defaultWidth["cloak"]!! + 0f), curValue(109.0f, 49.0f, progress) / (DefaultSizes.defaultHeight["cloak"]!! + 0f), curValue(-0.3237381f, 89.526474f, progress), 0, 0, DefaultSizes.defaultWidth["cloak"]!!, DefaultSizes.defaultHeight["cloak"]!!, false, false)
                                }
                                else if (timePassed < 1002)
                                {
                                    val progress = (timePassed - 501) / 501f
                                    batch.draw(textures["cloak"]!!.getTexture(timePassedSinceStart), x + curValue(1.0f, 1.0f, progress) - DefaultSizes.defaultWidth["cloak"]!!/2f, y + curValue(3.0f, 3.0f, progress) - DefaultSizes.defaultHeight["cloak"]!!/2f, DefaultSizes.defaultWidth["cloak"]!!/2f, DefaultSizes.defaultHeight["cloak"]!!/2f, DefaultSizes.defaultWidth["cloak"]!! + 0f, DefaultSizes.defaultHeight["cloak"]!! + 0f, curValue(54.0f, 120.0f, progress) / (DefaultSizes.defaultWidth["cloak"]!! + 0f), curValue(49.0f, 109.0f, progress) / (DefaultSizes.defaultHeight["cloak"]!! + 0f), curValue(89.526474f, -0.3237381f, progress), 0, 0, DefaultSizes.defaultWidth["cloak"]!!, DefaultSizes.defaultHeight["cloak"]!!, false, false)
                                }
                            }
                            WeaponType.ONE_HANDED_AND_SHIELD ->
                            {
                                if (timePassed < 1001)
                                {

                                }
                            }
                            WeaponType.DUAL ->
                            {
                                if (timePassed < 1001)
                                {

                                }
                            }
                            WeaponType.TWO_HANDED ->
                            {
                                if (timePassed < 1001)
                                {

                                }
                            }
                            WeaponType.BOW ->
                            {
                                if (timePassed < 1001)
                                {

                                }
                            }
                            WeaponType.STAFF ->
                            {
                                if (timePassed < 1001)
                                {

                                }
                            }
                        }
                    MoveDirection.UP_RIGHT ->
                        when (weaponType) {
                            WeaponType.ONE_HANDED ->
                            {
                                if (timePassed < 1001)
                                {

                                }
                            }
                            WeaponType.ONE_HANDED_AND_SHIELD ->
                            {
                                if (timePassed < 1001)
                                {

                                }
                            }
                            WeaponType.DUAL ->
                            {
                                if (timePassed < 1001)
                                {

                                }
                            }
                            WeaponType.TWO_HANDED ->
                            {
                                if (timePassed < 1001)
                                {

                                }
                            }
                            WeaponType.BOW ->
                            {
                                if (timePassed < 1001)
                                {

                                }
                            }
                            WeaponType.STAFF ->
                            {
                                if (timePassed < 1001)
                                {

                                }
                            }
                        }
                    MoveDirection.UP ->
                        when (weaponType) {
                            WeaponType.ONE_HANDED ->
                            {
                                if (timePassed < 1001)
                                {

                                }
                            }
                            WeaponType.ONE_HANDED_AND_SHIELD ->
                            {
                                if (timePassed < 1001)
                                {

                                }
                            }
                            WeaponType.DUAL ->
                            {
                                if (timePassed < 1001)
                                {

                                }
                            }
                            WeaponType.TWO_HANDED ->
                            {
                                if (timePassed < 1001)
                                {

                                }
                            }
                            WeaponType.BOW ->
                            {
                                if (timePassed < 1001)
                                {

                                }
                            }
                            WeaponType.STAFF ->
                            {
                                if (timePassed < 1001)
                                {

                                }
                            }
                        }
                    MoveDirection.UP_LEFT ->
                        when (weaponType) {
                            WeaponType.ONE_HANDED ->
                            {
                                if (timePassed < 1001)
                                {

                                }
                            }
                            WeaponType.ONE_HANDED_AND_SHIELD ->
                            {
                                if (timePassed < 1001)
                                {

                                }
                            }
                            WeaponType.DUAL ->
                            {
                                if (timePassed < 1001)
                                {

                                }
                            }
                            WeaponType.TWO_HANDED ->
                            {
                                if (timePassed < 1001)
                                {

                                }
                            }
                            WeaponType.BOW ->
                            {
                                if (timePassed < 1001)
                                {

                                }
                            }
                            WeaponType.STAFF ->
                            {
                                if (timePassed < 1001)
                                {

                                }
                            }
                        }
                    MoveDirection.LEFT ->
                        when (weaponType) {
                            WeaponType.ONE_HANDED ->
                            {
                                if (timePassed < 1001)
                                {

                                }
                            }
                            WeaponType.ONE_HANDED_AND_SHIELD ->
                            {
                                if (timePassed < 1001)
                                {

                                }
                            }
                            WeaponType.DUAL ->
                            {
                                if (timePassed < 1001)
                                {

                                }
                            }
                            WeaponType.TWO_HANDED ->
                            {
                                if (timePassed < 1001)
                                {

                                }
                            }
                            WeaponType.BOW ->
                            {
                                if (timePassed < 1001)
                                {

                                }
                            }
                            WeaponType.STAFF ->
                            {
                                if (timePassed < 1001)
                                {

                                }
                            }
                        }
                    MoveDirection.DOWN_LEFT ->
                        when (weaponType) {
                            WeaponType.ONE_HANDED ->
                            {
                                if (timePassed < 1001)
                                {

                                }
                            }
                            WeaponType.ONE_HANDED_AND_SHIELD ->
                            {
                                if (timePassed < 1001)
                                {

                                }
                            }
                            WeaponType.DUAL ->
                            {
                                if (timePassed < 1001)
                                {

                                }
                            }
                            WeaponType.TWO_HANDED ->
                            {
                                if (timePassed < 1001)
                                {

                                }
                            }
                            WeaponType.BOW ->
                            {
                                if (timePassed < 1001)
                                {

                                }
                            }
                            WeaponType.STAFF ->
                            {
                                if (timePassed < 1001)
                                {

                                }
                            }
                        }
                    MoveDirection.DOWN ->
                        when (weaponType) {
                            WeaponType.ONE_HANDED ->
                            {
                                if (timePassed < 1001)
                                {

                                }
                            }
                            WeaponType.ONE_HANDED_AND_SHIELD ->
                            {
                                if (timePassed < 1001)
                                {

                                }
                            }
                            WeaponType.DUAL ->
                            {
                                if (timePassed < 1001)
                                {

                                }
                            }
                            WeaponType.TWO_HANDED ->
                            {
                                if (timePassed < 1001)
                                {

                                }
                            }
                            WeaponType.BOW ->
                            {
                                if (timePassed < 1001)
                                {

                                }
                            }
                            WeaponType.STAFF ->
                            {
                                if (timePassed < 1001)
                                {

                                }
                            }
                        }
                    MoveDirection.DOWN_RIGHT ->
                        when (weaponType) {
                            WeaponType.ONE_HANDED ->
                            {
                                if (timePassed < 1001)
                                {

                                }
                            }
                            WeaponType.ONE_HANDED_AND_SHIELD ->
                            {
                                if (timePassed < 1001)
                                {

                                }
                            }
                            WeaponType.DUAL ->
                            {
                                if (timePassed < 1001)
                                {

                                }
                            }
                            WeaponType.TWO_HANDED ->
                            {
                                if (timePassed < 1001)
                                {

                                }
                            }
                            WeaponType.BOW ->
                            {
                                if (timePassed < 1001)
                                {

                                }
                            }
                            WeaponType.STAFF ->
                            {
                                if (timePassed < 1001)
                                {

                                }
                            }
                        }
                }
        }
    }
}