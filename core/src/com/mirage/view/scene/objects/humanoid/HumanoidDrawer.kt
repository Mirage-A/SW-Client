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
                                if (timePassed < 251)
                                {
                                    val progress = (timePassed - 0) / 251f
                                    batch.draw(textures["legbottom"]!!.getTexture(timePassedSinceStart), x + curValue(-6.0f, -16.0f, progress) - DefaultSizes.defaultWidth["legbottom"]!!/2f, y - curValue(-9.0f, -16.0f, progress) - DefaultSizes.defaultHeight["legbottom"]!!/2f, DefaultSizes.defaultWidth["legbottom"]!!/2f, DefaultSizes.defaultHeight["legbottom"]!!/2f, DefaultSizes.defaultWidth["legbottom"]!! + 0f, DefaultSizes.defaultHeight["legbottom"]!! + 0f, curValue(21.0f, 21.0f, progress) / (DefaultSizes.defaultWidth["legbottom"]!! + 0f), curValue(9.0f, 9.0f, progress) / (DefaultSizes.defaultHeight["legbottom"]!! + 0f), curValue(76.551346f, 34.76519f, progress), 0, 0, DefaultSizes.defaultWidth["legbottom"]!!, DefaultSizes.defaultHeight["legbottom"]!!, false, false)
                                    batch.draw(textures["legtop"]!!.getTexture(timePassedSinceStart), x + curValue(-3.0f, -6.0f, progress) - DefaultSizes.defaultWidth["legtop"]!!/2f, y - curValue(-23.0f, -25.0f, progress) - DefaultSizes.defaultHeight["legtop"]!!/2f, DefaultSizes.defaultWidth["legtop"]!!/2f, DefaultSizes.defaultHeight["legtop"]!!/2f, DefaultSizes.defaultWidth["legtop"]!! + 0f, DefaultSizes.defaultHeight["legtop"]!! + 0f, curValue(21.0f, 21.0f, progress) / (DefaultSizes.defaultWidth["legtop"]!! + 0f), curValue(9.0f, 9.0f, progress) / (DefaultSizes.defaultHeight["legtop"]!! + 0f), curValue(84.88954f, 55.813824f, progress), 0, 0, DefaultSizes.defaultWidth["legtop"]!!, DefaultSizes.defaultHeight["legtop"]!!, false, false)
                                    batch.draw(textures["body"]!!.getTexture(timePassedSinceStart), x + curValue(1.0f, 3.0f, progress) - DefaultSizes.defaultWidth["body"]!!/2f, y - curValue(-42.0f, -43.0f, progress) - DefaultSizes.defaultHeight["body"]!!/2f, DefaultSizes.defaultWidth["body"]!!/2f, DefaultSizes.defaultHeight["body"]!!/2f, DefaultSizes.defaultWidth["body"]!! + 0f, DefaultSizes.defaultHeight["body"]!! + 0f, curValue(35.0f, 35.0f, progress) / (DefaultSizes.defaultWidth["body"]!! + 0f), curValue(19.0f, 19.0f, progress) / (DefaultSizes.defaultHeight["body"]!! + 0f), curValue(89.10479f, 81.214645f, progress), 0, 0, DefaultSizes.defaultWidth["body"]!!, DefaultSizes.defaultHeight["body"]!!, false, false)
                                    batch.draw(textures["legbottom"]!!.getTexture(timePassedSinceStart), x + curValue(4.0f, 6.0f, progress) - DefaultSizes.defaultWidth["legbottom"]!!/2f, y - curValue(-9.0f, -10.0f, progress) - DefaultSizes.defaultHeight["legbottom"]!!/2f, DefaultSizes.defaultWidth["legbottom"]!!/2f, DefaultSizes.defaultHeight["legbottom"]!!/2f, DefaultSizes.defaultWidth["legbottom"]!! + 0f, DefaultSizes.defaultHeight["legbottom"]!! + 0f, curValue(21.0f, 21.0f, progress) / (DefaultSizes.defaultWidth["legbottom"]!! + 0f), curValue(9.0f, 9.0f, progress) / (DefaultSizes.defaultHeight["legbottom"]!! + 0f), curValue(83.04703f, 74.53877f, progress), 0, 0, DefaultSizes.defaultWidth["legbottom"]!!, DefaultSizes.defaultHeight["legbottom"]!!, false, false)
                                    batch.draw(textures["legtop"]!!.getTexture(timePassedSinceStart), x + curValue(4.0f, 6.0f, progress) - DefaultSizes.defaultWidth["legtop"]!!/2f, y - curValue(-23.0f, -23.0f, progress) - DefaultSizes.defaultHeight["legtop"]!!/2f, DefaultSizes.defaultWidth["legtop"]!!/2f, DefaultSizes.defaultHeight["legtop"]!!/2f, DefaultSizes.defaultWidth["legtop"]!! + 0f, DefaultSizes.defaultHeight["legtop"]!! + 0f, curValue(21.0f, 21.0f, progress) / (DefaultSizes.defaultWidth["legtop"]!! + 0f), curValue(9.0f, 9.0f, progress) / (DefaultSizes.defaultHeight["legtop"]!! + 0f), curValue(92.54478f, 113.62933f, progress), 0, 0, DefaultSizes.defaultWidth["legtop"]!!, DefaultSizes.defaultHeight["legtop"]!!, false, false)

                                }
                                else if (timePassed < 502)
                                {
                                    val progress = (timePassed - 251) / 251f
                                    batch.draw(textures["legbottom"]!!.getTexture(timePassedSinceStart), x + curValue(-16.0f, 3.0f, progress) - DefaultSizes.defaultWidth["legbottom"]!!/2f, y - curValue(-16.0f, -9.0f, progress) - DefaultSizes.defaultHeight["legbottom"]!!/2f, DefaultSizes.defaultWidth["legbottom"]!!/2f, DefaultSizes.defaultHeight["legbottom"]!!/2f, DefaultSizes.defaultWidth["legbottom"]!! + 0f, DefaultSizes.defaultHeight["legbottom"]!! + 0f, curValue(21.0f, 21.0f, progress) / (DefaultSizes.defaultWidth["legbottom"]!! + 0f), curValue(9.0f, 9.0f, progress) / (DefaultSizes.defaultHeight["legbottom"]!! + 0f), curValue(34.76519f, 81.469215f, progress), 0, 0, DefaultSizes.defaultWidth["legbottom"]!!, DefaultSizes.defaultHeight["legbottom"]!!, false, false)
                                    batch.draw(textures["legtop"]!!.getTexture(timePassedSinceStart), x + curValue(-6.0f, 3.0f, progress) - DefaultSizes.defaultWidth["legtop"]!!/2f, y - curValue(-25.0f, -24.0f, progress) - DefaultSizes.defaultHeight["legtop"]!!/2f, DefaultSizes.defaultWidth["legtop"]!!/2f, DefaultSizes.defaultHeight["legtop"]!!/2f, DefaultSizes.defaultWidth["legtop"]!! + 0f, DefaultSizes.defaultHeight["legtop"]!! + 0f, curValue(21.0f, 21.0f, progress) / (DefaultSizes.defaultWidth["legtop"]!! + 0f), curValue(9.0f, 9.0f, progress) / (DefaultSizes.defaultHeight["legtop"]!! + 0f), curValue(55.813824f, 103.19056f, progress), 0, 0, DefaultSizes.defaultWidth["legtop"]!!, DefaultSizes.defaultHeight["legtop"]!!, false, false)
                                    batch.draw(textures["body"]!!.getTexture(timePassedSinceStart), x + curValue(3.0f, 1.0f, progress) - DefaultSizes.defaultWidth["body"]!!/2f, y - curValue(-43.0f, -42.0f, progress) - DefaultSizes.defaultHeight["body"]!!/2f, DefaultSizes.defaultWidth["body"]!!/2f, DefaultSizes.defaultHeight["body"]!!/2f, DefaultSizes.defaultWidth["body"]!! + 0f, DefaultSizes.defaultHeight["body"]!! + 0f, curValue(35.0f, 35.0f, progress) / (DefaultSizes.defaultWidth["body"]!! + 0f), curValue(19.0f, 19.0f, progress) / (DefaultSizes.defaultHeight["body"]!! + 0f), curValue(81.214645f, 89.10479f, progress), 0, 0, DefaultSizes.defaultWidth["body"]!!, DefaultSizes.defaultHeight["body"]!!, false, false)
                                    batch.draw(textures["legbottom"]!!.getTexture(timePassedSinceStart), x + curValue(6.0f, -5.0f, progress) - DefaultSizes.defaultWidth["legbottom"]!!/2f, y - curValue(-10.0f, -8.0f, progress) - DefaultSizes.defaultHeight["legbottom"]!!/2f, DefaultSizes.defaultWidth["legbottom"]!!/2f, DefaultSizes.defaultHeight["legbottom"]!!/2f, DefaultSizes.defaultWidth["legbottom"]!! + 0f, DefaultSizes.defaultHeight["legbottom"]!! + 0f, curValue(21.0f, 21.0f, progress) / (DefaultSizes.defaultWidth["legbottom"]!! + 0f), curValue(9.0f, 9.0f, progress) / (DefaultSizes.defaultHeight["legbottom"]!! + 0f), curValue(74.53877f, 83.04703f, progress), 0, 0, DefaultSizes.defaultWidth["legbottom"]!!, DefaultSizes.defaultHeight["legbottom"]!!, false, false)
                                    batch.draw(textures["legtop"]!!.getTexture(timePassedSinceStart), x + curValue(6.0f, -3.0f, progress) - DefaultSizes.defaultWidth["legtop"]!!/2f, y - curValue(-23.0f, -22.0f, progress) - DefaultSizes.defaultHeight["legtop"]!!/2f, DefaultSizes.defaultWidth["legtop"]!!/2f, DefaultSizes.defaultHeight["legtop"]!!/2f, DefaultSizes.defaultWidth["legtop"]!! + 0f, DefaultSizes.defaultHeight["legtop"]!! + 0f, curValue(21.0f, 21.0f, progress) / (DefaultSizes.defaultWidth["legtop"]!! + 0f), curValue(9.0f, 9.0f, progress) / (DefaultSizes.defaultHeight["legtop"]!! + 0f), curValue(113.62933f, 81.362816f, progress), 0, 0, DefaultSizes.defaultWidth["legtop"]!!, DefaultSizes.defaultHeight["legtop"]!!, false, false)

                                }
                                else if (timePassed < 753)
                                {
                                    val progress = (timePassed - 502) / 251f
                                    batch.draw(textures["legbottom"]!!.getTexture(timePassedSinceStart), x + curValue(3.0f, 9.0f, progress) - DefaultSizes.defaultWidth["legbottom"]!!/2f, y - curValue(-9.0f, -11.0f, progress) - DefaultSizes.defaultHeight["legbottom"]!!/2f, DefaultSizes.defaultWidth["legbottom"]!!/2f, DefaultSizes.defaultHeight["legbottom"]!!/2f, DefaultSizes.defaultWidth["legbottom"]!! + 0f, DefaultSizes.defaultHeight["legbottom"]!! + 0f, curValue(21.0f, 21.0f, progress) / (DefaultSizes.defaultWidth["legbottom"]!! + 0f), curValue(9.0f, 9.0f, progress) / (DefaultSizes.defaultHeight["legbottom"]!! + 0f), curValue(81.469215f, 75.73545f, progress), 0, 0, DefaultSizes.defaultWidth["legbottom"]!!, DefaultSizes.defaultHeight["legbottom"]!!, false, false)
                                    batch.draw(textures["legtop"]!!.getTexture(timePassedSinceStart), x + curValue(3.0f, 7.0f, progress) - DefaultSizes.defaultWidth["legtop"]!!/2f, y - curValue(-24.0f, -24.0f, progress) - DefaultSizes.defaultHeight["legtop"]!!/2f, DefaultSizes.defaultWidth["legtop"]!!/2f, DefaultSizes.defaultHeight["legtop"]!!/2f, DefaultSizes.defaultWidth["legtop"]!! + 0f, DefaultSizes.defaultHeight["legtop"]!! + 0f, curValue(21.0f, 21.0f, progress) / (DefaultSizes.defaultWidth["legtop"]!! + 0f), curValue(9.0f, 9.0f, progress) / (DefaultSizes.defaultHeight["legtop"]!! + 0f), curValue(103.19056f, 114.77513f, progress), 0, 0, DefaultSizes.defaultWidth["legtop"]!!, DefaultSizes.defaultHeight["legtop"]!!, false, false)
                                    batch.draw(textures["body"]!!.getTexture(timePassedSinceStart), x + curValue(1.0f, 3.0f, progress) - DefaultSizes.defaultWidth["body"]!!/2f, y - curValue(-42.0f, -43.0f, progress) - DefaultSizes.defaultHeight["body"]!!/2f, DefaultSizes.defaultWidth["body"]!!/2f, DefaultSizes.defaultHeight["body"]!!/2f, DefaultSizes.defaultWidth["body"]!! + 0f, DefaultSizes.defaultHeight["body"]!! + 0f, curValue(35.0f, 35.0f, progress) / (DefaultSizes.defaultWidth["body"]!! + 0f), curValue(19.0f, 19.0f, progress) / (DefaultSizes.defaultHeight["body"]!! + 0f), curValue(89.10479f, 81.214645f, progress), 0, 0, DefaultSizes.defaultWidth["body"]!!, DefaultSizes.defaultHeight["body"]!!, false, false)
                                    batch.draw(textures["legbottom"]!!.getTexture(timePassedSinceStart), x + curValue(-5.0f, -16.0f, progress) - DefaultSizes.defaultWidth["legbottom"]!!/2f, y - curValue(-8.0f, -15.0f, progress) - DefaultSizes.defaultHeight["legbottom"]!!/2f, DefaultSizes.defaultWidth["legbottom"]!!/2f, DefaultSizes.defaultHeight["legbottom"]!!/2f, DefaultSizes.defaultWidth["legbottom"]!! + 0f, DefaultSizes.defaultHeight["legbottom"]!! + 0f, curValue(21.0f, 21.0f, progress) / (DefaultSizes.defaultWidth["legbottom"]!! + 0f), curValue(9.0f, 9.0f, progress) / (DefaultSizes.defaultHeight["legbottom"]!! + 0f), curValue(83.04703f, 42.06427f, progress), 0, 0, DefaultSizes.defaultWidth["legbottom"]!!, DefaultSizes.defaultHeight["legbottom"]!!, false, false)
                                    batch.draw(textures["legtop"]!!.getTexture(timePassedSinceStart), x + curValue(-3.0f, -7.0f, progress) - DefaultSizes.defaultWidth["legtop"]!!/2f, y - curValue(-22.0f, -25.0f, progress) - DefaultSizes.defaultHeight["legtop"]!!/2f, DefaultSizes.defaultWidth["legtop"]!!/2f, DefaultSizes.defaultHeight["legtop"]!!/2f, DefaultSizes.defaultWidth["legtop"]!! + 0f, DefaultSizes.defaultHeight["legtop"]!! + 0f, curValue(21.0f, 21.0f, progress) / (DefaultSizes.defaultWidth["legtop"]!! + 0f), curValue(9.0f, 9.0f, progress) / (DefaultSizes.defaultHeight["legtop"]!! + 0f), curValue(81.362816f, 51.801285f, progress), 0, 0, DefaultSizes.defaultWidth["legtop"]!!, DefaultSizes.defaultHeight["legtop"]!!, false, false)

                                }
                                else if (timePassed < 1004)
                                {
                                    val progress = (timePassed - 753) / 251f
                                    batch.draw(textures["legbottom"]!!.getTexture(timePassedSinceStart), x + curValue(9.0f, -6.0f, progress) - DefaultSizes.defaultWidth["legbottom"]!!/2f, y - curValue(-11.0f, -9.0f, progress) - DefaultSizes.defaultHeight["legbottom"]!!/2f, DefaultSizes.defaultWidth["legbottom"]!!/2f, DefaultSizes.defaultHeight["legbottom"]!!/2f, DefaultSizes.defaultWidth["legbottom"]!! + 0f, DefaultSizes.defaultHeight["legbottom"]!! + 0f, curValue(21.0f, 21.0f, progress) / (DefaultSizes.defaultWidth["legbottom"]!! + 0f), curValue(9.0f, 9.0f, progress) / (DefaultSizes.defaultHeight["legbottom"]!! + 0f), curValue(75.73545f, 76.551346f, progress), 0, 0, DefaultSizes.defaultWidth["legbottom"]!!, DefaultSizes.defaultHeight["legbottom"]!!, false, false)
                                    batch.draw(textures["legtop"]!!.getTexture(timePassedSinceStart), x + curValue(7.0f, -3.0f, progress) - DefaultSizes.defaultWidth["legtop"]!!/2f, y - curValue(-24.0f, -23.0f, progress) - DefaultSizes.defaultHeight["legtop"]!!/2f, DefaultSizes.defaultWidth["legtop"]!!/2f, DefaultSizes.defaultHeight["legtop"]!!/2f, DefaultSizes.defaultWidth["legtop"]!! + 0f, DefaultSizes.defaultHeight["legtop"]!! + 0f, curValue(21.0f, 21.0f, progress) / (DefaultSizes.defaultWidth["legtop"]!! + 0f), curValue(9.0f, 9.0f, progress) / (DefaultSizes.defaultHeight["legtop"]!! + 0f), curValue(114.77513f, 84.88954f, progress), 0, 0, DefaultSizes.defaultWidth["legtop"]!!, DefaultSizes.defaultHeight["legtop"]!!, false, false)
                                    batch.draw(textures["body"]!!.getTexture(timePassedSinceStart), x + curValue(3.0f, 1.0f, progress) - DefaultSizes.defaultWidth["body"]!!/2f, y - curValue(-43.0f, -42.0f, progress) - DefaultSizes.defaultHeight["body"]!!/2f, DefaultSizes.defaultWidth["body"]!!/2f, DefaultSizes.defaultHeight["body"]!!/2f, DefaultSizes.defaultWidth["body"]!! + 0f, DefaultSizes.defaultHeight["body"]!! + 0f, curValue(35.0f, 35.0f, progress) / (DefaultSizes.defaultWidth["body"]!! + 0f), curValue(19.0f, 19.0f, progress) / (DefaultSizes.defaultHeight["body"]!! + 0f), curValue(81.214645f, 89.10479f, progress), 0, 0, DefaultSizes.defaultWidth["body"]!!, DefaultSizes.defaultHeight["body"]!!, false, false)
                                    batch.draw(textures["legbottom"]!!.getTexture(timePassedSinceStart), x + curValue(-16.0f, 4.0f, progress) - DefaultSizes.defaultWidth["legbottom"]!!/2f, y - curValue(-15.0f, -9.0f, progress) - DefaultSizes.defaultHeight["legbottom"]!!/2f, DefaultSizes.defaultWidth["legbottom"]!!/2f, DefaultSizes.defaultHeight["legbottom"]!!/2f, DefaultSizes.defaultWidth["legbottom"]!! + 0f, DefaultSizes.defaultHeight["legbottom"]!! + 0f, curValue(21.0f, 21.0f, progress) / (DefaultSizes.defaultWidth["legbottom"]!! + 0f), curValue(9.0f, 9.0f, progress) / (DefaultSizes.defaultHeight["legbottom"]!! + 0f), curValue(42.06427f, 83.04703f, progress), 0, 0, DefaultSizes.defaultWidth["legbottom"]!!, DefaultSizes.defaultHeight["legbottom"]!!, false, false)
                                    batch.draw(textures["legtop"]!!.getTexture(timePassedSinceStart), x + curValue(-7.0f, 4.0f, progress) - DefaultSizes.defaultWidth["legtop"]!!/2f, y - curValue(-25.0f, -23.0f, progress) - DefaultSizes.defaultHeight["legtop"]!!/2f, DefaultSizes.defaultWidth["legtop"]!!/2f, DefaultSizes.defaultHeight["legtop"]!!/2f, DefaultSizes.defaultWidth["legtop"]!! + 0f, DefaultSizes.defaultHeight["legtop"]!! + 0f, curValue(21.0f, 21.0f, progress) / (DefaultSizes.defaultWidth["legtop"]!! + 0f), curValue(9.0f, 9.0f, progress) / (DefaultSizes.defaultHeight["legtop"]!! + 0f), curValue(51.801285f, 92.54478f, progress), 0, 0, DefaultSizes.defaultWidth["legtop"]!!, DefaultSizes.defaultHeight["legtop"]!!, false, false)

                                }
                            }
                            WeaponType.ONE_HANDED_AND_SHIELD ->
                            {
                                if (timePassed < 1001)
                                {
                                    val progress = (timePassed - 0) / 1001f

                                }
                            }
                            WeaponType.DUAL ->
                            {
                                if (timePassed < 1001)
                                {
                                    val progress = (timePassed - 0) / 1001f

                                }
                            }
                            WeaponType.TWO_HANDED ->
                            {
                                if (timePassed < 1001)
                                {
                                    val progress = (timePassed - 0) / 1001f

                                }
                            }
                            WeaponType.BOW ->
                            {
                                if (timePassed < 1001)
                                {
                                    val progress = (timePassed - 0) / 1001f

                                }
                            }
                            WeaponType.STAFF ->
                            {
                                if (timePassed < 1001)
                                {
                                    val progress = (timePassed - 0) / 1001f

                                }
                            }
                        }
                    MoveDirection.UP_RIGHT ->
                        when (weaponType) {
                            WeaponType.ONE_HANDED ->
                            {
                                if (timePassed < 1001)
                                {
                                    val progress = (timePassed - 0) / 1001f

                                }
                            }
                            WeaponType.ONE_HANDED_AND_SHIELD ->
                            {
                                if (timePassed < 1001)
                                {
                                    val progress = (timePassed - 0) / 1001f

                                }
                            }
                            WeaponType.DUAL ->
                            {
                                if (timePassed < 1001)
                                {
                                    val progress = (timePassed - 0) / 1001f

                                }
                            }
                            WeaponType.TWO_HANDED ->
                            {
                                if (timePassed < 1001)
                                {
                                    val progress = (timePassed - 0) / 1001f

                                }
                            }
                            WeaponType.BOW ->
                            {
                                if (timePassed < 1001)
                                {
                                    val progress = (timePassed - 0) / 1001f

                                }
                            }
                            WeaponType.STAFF ->
                            {
                                if (timePassed < 1001)
                                {
                                    val progress = (timePassed - 0) / 1001f

                                }
                            }
                        }
                    MoveDirection.UP ->
                        when (weaponType) {
                            WeaponType.ONE_HANDED ->
                            {
                                if (timePassed < 1001)
                                {
                                    val progress = (timePassed - 0) / 1001f

                                }
                            }
                            WeaponType.ONE_HANDED_AND_SHIELD ->
                            {
                                if (timePassed < 1001)
                                {
                                    val progress = (timePassed - 0) / 1001f

                                }
                            }
                            WeaponType.DUAL ->
                            {
                                if (timePassed < 1001)
                                {
                                    val progress = (timePassed - 0) / 1001f

                                }
                            }
                            WeaponType.TWO_HANDED ->
                            {
                                if (timePassed < 1001)
                                {
                                    val progress = (timePassed - 0) / 1001f

                                }
                            }
                            WeaponType.BOW ->
                            {
                                if (timePassed < 1001)
                                {
                                    val progress = (timePassed - 0) / 1001f

                                }
                            }
                            WeaponType.STAFF ->
                            {
                                if (timePassed < 1001)
                                {
                                    val progress = (timePassed - 0) / 1001f

                                }
                            }
                        }
                    MoveDirection.UP_LEFT ->
                        when (weaponType) {
                            WeaponType.ONE_HANDED ->
                            {
                                if (timePassed < 1001)
                                {
                                    val progress = (timePassed - 0) / 1001f

                                }
                            }
                            WeaponType.ONE_HANDED_AND_SHIELD ->
                            {
                                if (timePassed < 1001)
                                {
                                    val progress = (timePassed - 0) / 1001f

                                }
                            }
                            WeaponType.DUAL ->
                            {
                                if (timePassed < 1001)
                                {
                                    val progress = (timePassed - 0) / 1001f

                                }
                            }
                            WeaponType.TWO_HANDED ->
                            {
                                if (timePassed < 1001)
                                {
                                    val progress = (timePassed - 0) / 1001f

                                }
                            }
                            WeaponType.BOW ->
                            {
                                if (timePassed < 1001)
                                {
                                    val progress = (timePassed - 0) / 1001f

                                }
                            }
                            WeaponType.STAFF ->
                            {
                                if (timePassed < 1001)
                                {
                                    val progress = (timePassed - 0) / 1001f

                                }
                            }
                        }
                    MoveDirection.LEFT ->
                        when (weaponType) {
                            WeaponType.ONE_HANDED ->
                            {
                                if (timePassed < 1001)
                                {
                                    val progress = (timePassed - 0) / 1001f

                                }
                            }
                            WeaponType.ONE_HANDED_AND_SHIELD ->
                            {
                                if (timePassed < 1001)
                                {
                                    val progress = (timePassed - 0) / 1001f

                                }
                            }
                            WeaponType.DUAL ->
                            {
                                if (timePassed < 1001)
                                {
                                    val progress = (timePassed - 0) / 1001f

                                }
                            }
                            WeaponType.TWO_HANDED ->
                            {
                                if (timePassed < 1001)
                                {
                                    val progress = (timePassed - 0) / 1001f

                                }
                            }
                            WeaponType.BOW ->
                            {
                                if (timePassed < 1001)
                                {
                                    val progress = (timePassed - 0) / 1001f

                                }
                            }
                            WeaponType.STAFF ->
                            {
                                if (timePassed < 1001)
                                {
                                    val progress = (timePassed - 0) / 1001f

                                }
                            }
                        }
                    MoveDirection.DOWN_LEFT ->
                        when (weaponType) {
                            WeaponType.ONE_HANDED ->
                            {
                                if (timePassed < 1001)
                                {
                                    val progress = (timePassed - 0) / 1001f

                                }
                            }
                            WeaponType.ONE_HANDED_AND_SHIELD ->
                            {
                                if (timePassed < 1001)
                                {
                                    val progress = (timePassed - 0) / 1001f

                                }
                            }
                            WeaponType.DUAL ->
                            {
                                if (timePassed < 1001)
                                {
                                    val progress = (timePassed - 0) / 1001f

                                }
                            }
                            WeaponType.TWO_HANDED ->
                            {
                                if (timePassed < 1001)
                                {
                                    val progress = (timePassed - 0) / 1001f

                                }
                            }
                            WeaponType.BOW ->
                            {
                                if (timePassed < 1001)
                                {
                                    val progress = (timePassed - 0) / 1001f

                                }
                            }
                            WeaponType.STAFF ->
                            {
                                if (timePassed < 1001)
                                {
                                    val progress = (timePassed - 0) / 1001f

                                }
                            }
                        }
                    MoveDirection.DOWN ->
                        when (weaponType) {
                            WeaponType.ONE_HANDED ->
                            {
                                if (timePassed < 1001)
                                {
                                    val progress = (timePassed - 0) / 1001f

                                }
                            }
                            WeaponType.ONE_HANDED_AND_SHIELD ->
                            {
                                if (timePassed < 1001)
                                {
                                    val progress = (timePassed - 0) / 1001f

                                }
                            }
                            WeaponType.DUAL ->
                            {
                                if (timePassed < 1001)
                                {
                                    val progress = (timePassed - 0) / 1001f

                                }
                            }
                            WeaponType.TWO_HANDED ->
                            {
                                if (timePassed < 1001)
                                {
                                    val progress = (timePassed - 0) / 1001f

                                }
                            }
                            WeaponType.BOW ->
                            {
                                if (timePassed < 1001)
                                {
                                    val progress = (timePassed - 0) / 1001f

                                }
                            }
                            WeaponType.STAFF ->
                            {
                                if (timePassed < 1001)
                                {
                                    val progress = (timePassed - 0) / 1001f

                                }
                            }
                        }
                    MoveDirection.DOWN_RIGHT ->
                        when (weaponType) {
                            WeaponType.ONE_HANDED ->
                            {
                                if (timePassed < 1001)
                                {
                                    val progress = (timePassed - 0) / 1001f

                                }
                            }
                            WeaponType.ONE_HANDED_AND_SHIELD ->
                            {
                                if (timePassed < 1001)
                                {
                                    val progress = (timePassed - 0) / 1001f

                                }
                            }
                            WeaponType.DUAL ->
                            {
                                if (timePassed < 1001)
                                {
                                    val progress = (timePassed - 0) / 1001f

                                }
                            }
                            WeaponType.TWO_HANDED ->
                            {
                                if (timePassed < 1001)
                                {
                                    val progress = (timePassed - 0) / 1001f

                                }
                            }
                            WeaponType.BOW ->
                            {
                                if (timePassed < 1001)
                                {
                                    val progress = (timePassed - 0) / 1001f

                                }
                            }
                            WeaponType.STAFF ->
                            {
                                if (timePassed < 1001)
                                {
                                    val progress = (timePassed - 0) / 1001f

                                }
                            }
                        }
                }
        }
    }
}