package com.mirage.view.scene.objects.humanoid

import com.mirage.view.TextureLoader
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
        for (i in 0..7) {
            textures["head$i"] = StaticTexture(TextureLoader.load("equipment/head/0000$i.png"))
        }
        textures["body"] = StaticTexture(TextureLoader.load("equipment/body/0000.png"))
        textures["handtop"] = StaticTexture(TextureLoader.load("equipment/handtop/0000.png"))
        textures["handbottom"] = StaticTexture(TextureLoader.load("equipment/handbottom/0000.png"))
        textures["legtop"] = StaticTexture(TextureLoader.load("equipment/legtop/0000.png"))
        textures["legbottom"] = StaticTexture(TextureLoader.load("equipment/legbottom/0000.png"))
        textures["cloak"] = StaticTexture(TextureLoader.load("equipment/cloak/0000.png"))
        textures["neck"] = StaticTexture(TextureLoader.load("equipment/neck/0000.png"))
        textures["weapon1"] = StaticTexture(TextureLoader.load("equipment/onehanded/0000.png"))
        textures["weapon2"] = StaticTexture(TextureLoader.load("equipment/onehanded/0000.png"))
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
        if (timePassed < 501) moveDirection = MoveDirection.LEFT
        else moveDirection = MoveDirection.RIGHT
        when (action) {
            Action.IDLE ->
                when (moveDirection) {
                    MoveDirection.RIGHT ->
                        when (weaponType) {
                            WeaponType.ONE_HANDED ->
                            {
                                batch.draw(textures["cloak"]!!.getTexture(timePassedSinceStart), x + -5.0f - DefaultSizes.defaultWidth["cloak"]!!/2f, y - -31.0f - DefaultSizes.defaultHeight["cloak"]!!/2f, DefaultSizes.defaultWidth["cloak"]!!/2f, DefaultSizes.defaultHeight["cloak"]!!/2f, DefaultSizes.defaultWidth["cloak"]!! + 0f, DefaultSizes.defaultHeight["cloak"]!! + 0f, 15.0f / (DefaultSizes.defaultWidth["cloak"]!! + 0f), 62.0f / (DefaultSizes.defaultHeight["cloak"]!! + 0f), -2.3970263f, 0, 0, DefaultSizes.defaultWidth["cloak"]!!, DefaultSizes.defaultHeight["cloak"]!!, false, false)
                                batch.draw(textures["legbottom"]!!.getTexture(timePassedSinceStart), x + 1.0f - DefaultSizes.defaultWidth["legbottom"]!!/2f, y - -12.0f - DefaultSizes.defaultHeight["legbottom"]!!/2f, DefaultSizes.defaultWidth["legbottom"]!!/2f, DefaultSizes.defaultHeight["legbottom"]!!/2f, DefaultSizes.defaultWidth["legbottom"]!! + 0f, DefaultSizes.defaultHeight["legbottom"]!! + 0f, 7.0f / (DefaultSizes.defaultWidth["legbottom"]!! + 0f), 18.0f / (DefaultSizes.defaultHeight["legbottom"]!! + 0f), 0.84247714f, 0, 0, DefaultSizes.defaultWidth["legbottom"]!!, DefaultSizes.defaultHeight["legbottom"]!!, false, false)
                                batch.draw(textures["legtop"]!!.getTexture(timePassedSinceStart), x + 2.0f - DefaultSizes.defaultWidth["legtop"]!!/2f, y - -26.0f - DefaultSizes.defaultHeight["legtop"]!!/2f, DefaultSizes.defaultWidth["legtop"]!!/2f, DefaultSizes.defaultHeight["legtop"]!!/2f, DefaultSizes.defaultWidth["legtop"]!! + 0f, DefaultSizes.defaultHeight["legtop"]!! + 0f, 6.0f / (DefaultSizes.defaultWidth["legtop"]!! + 0f), 17.0f / (DefaultSizes.defaultHeight["legtop"]!! + 0f), 1.0230161f, 0, 0, DefaultSizes.defaultWidth["legtop"]!!, DefaultSizes.defaultHeight["legtop"]!!, false, false)
                                batch.draw(textures["handtop"]!!.getTexture(timePassedSinceStart), x + 1.0f - DefaultSizes.defaultWidth["handtop"]!!/2f, y - -51.0f - DefaultSizes.defaultHeight["handtop"]!!/2f, DefaultSizes.defaultWidth["handtop"]!!/2f, DefaultSizes.defaultHeight["handtop"]!!/2f, DefaultSizes.defaultWidth["handtop"]!! + 0f, DefaultSizes.defaultHeight["handtop"]!! + 0f, 7.0f / (DefaultSizes.defaultWidth["handtop"]!! + 0f), 14.0f / (DefaultSizes.defaultHeight["handtop"]!! + 0f), 7.1249595f, 0, 0, DefaultSizes.defaultWidth["handtop"]!!, DefaultSizes.defaultHeight["handtop"]!!, false, false)
                                batch.draw(textures["handbottom"]!!.getTexture(timePassedSinceStart), x + 2.0f - DefaultSizes.defaultWidth["handbottom"]!!/2f, y - -40.0f - DefaultSizes.defaultHeight["handbottom"]!!/2f, DefaultSizes.defaultWidth["handbottom"]!!/2f, DefaultSizes.defaultHeight["handbottom"]!!/2f, DefaultSizes.defaultWidth["handbottom"]!! + 0f, DefaultSizes.defaultHeight["handbottom"]!! + 0f, 8.0f / (DefaultSizes.defaultWidth["handbottom"]!! + 0f), 16.0f / (DefaultSizes.defaultHeight["handbottom"]!! + 0f), 3.878523f, 0, 0, DefaultSizes.defaultWidth["handbottom"]!!, DefaultSizes.defaultHeight["handbottom"]!!, false, false)
                                batch.draw(textures["neck"]!!.getTexture(timePassedSinceStart), x + 0.0f - DefaultSizes.defaultWidth["neck"]!!/2f, y - -59.0f - DefaultSizes.defaultHeight["neck"]!!/2f, DefaultSizes.defaultWidth["neck"]!!/2f, DefaultSizes.defaultHeight["neck"]!!/2f, DefaultSizes.defaultWidth["neck"]!! + 0f, DefaultSizes.defaultHeight["neck"]!! + 0f, 10.0f / (DefaultSizes.defaultWidth["neck"]!! + 0f), 4.0f / (DefaultSizes.defaultHeight["neck"]!! + 0f), -2.5168316f, 0, 0, DefaultSizes.defaultWidth["neck"]!!, DefaultSizes.defaultHeight["neck"]!!, false, false)
                                batch.draw(textures["body"]!!.getTexture(timePassedSinceStart), x + 0.0f - DefaultSizes.defaultWidth["body"]!!/2f, y - -44.0f - DefaultSizes.defaultHeight["body"]!!/2f, DefaultSizes.defaultWidth["body"]!!/2f, DefaultSizes.defaultHeight["body"]!!/2f, DefaultSizes.defaultWidth["body"]!! + 0f, DefaultSizes.defaultHeight["body"]!! + 0f, 12.0f / (DefaultSizes.defaultWidth["body"]!! + 0f), 32.0f / (DefaultSizes.defaultHeight["body"]!! + 0f), -0.6127211f, 0, 0, DefaultSizes.defaultWidth["body"]!!, DefaultSizes.defaultHeight["body"]!!, false, false)
                                batch.draw(textures["legbottom"]!!.getTexture(timePassedSinceStart), x + -1.0f - DefaultSizes.defaultWidth["legbottom"]!!/2f, y - -9.0f - DefaultSizes.defaultHeight["legbottom"]!!/2f, DefaultSizes.defaultWidth["legbottom"]!!/2f, DefaultSizes.defaultHeight["legbottom"]!!/2f, DefaultSizes.defaultWidth["legbottom"]!! + 0f, DefaultSizes.defaultHeight["legbottom"]!! + 0f, 8.0f / (DefaultSizes.defaultWidth["legbottom"]!! + 0f), 20.0f / (DefaultSizes.defaultHeight["legbottom"]!! + 0f), -1.1232837f, 0, 0, DefaultSizes.defaultWidth["legbottom"]!!, DefaultSizes.defaultHeight["legbottom"]!!, false, false)
                                batch.draw(textures["legtop"]!!.getTexture(timePassedSinceStart), x + -1.0f - DefaultSizes.defaultWidth["legtop"]!!/2f, y - -24.0f - DefaultSizes.defaultHeight["legtop"]!!/2f, DefaultSizes.defaultWidth["legtop"]!!/2f, DefaultSizes.defaultHeight["legtop"]!!/2f, DefaultSizes.defaultWidth["legtop"]!! + 0f, DefaultSizes.defaultHeight["legtop"]!! + 0f, 8.0f / (DefaultSizes.defaultWidth["legtop"]!! + 0f), 20.0f / (DefaultSizes.defaultHeight["legtop"]!! + 0f), -3.0940294f, 0, 0, DefaultSizes.defaultWidth["legtop"]!!, DefaultSizes.defaultHeight["legtop"]!!, false, false)
                                batch.draw(textures["head0"]!!.getTexture(timePassedSinceStart), x + -2.0f - DefaultSizes.defaultWidth["head0"]!!/2f, y - -68.0f - DefaultSizes.defaultHeight["head0"]!!/2f, DefaultSizes.defaultWidth["head0"]!!/2f, DefaultSizes.defaultHeight["head0"]!!/2f, DefaultSizes.defaultWidth["head0"]!! + 0f, DefaultSizes.defaultHeight["head0"]!! + 0f, 37.0f / (DefaultSizes.defaultWidth["head0"]!! + 0f), 37.0f / (DefaultSizes.defaultHeight["head0"]!! + 0f), -0.3744279f, 0, 0, DefaultSizes.defaultWidth["head0"]!!, DefaultSizes.defaultHeight["head0"]!!, false, false)
                                batch.draw(textures["handtop"]!!.getTexture(timePassedSinceStart), x + -2.0f - DefaultSizes.defaultWidth["handtop"]!!/2f, y - -52.0f - DefaultSizes.defaultHeight["handtop"]!!/2f, DefaultSizes.defaultWidth["handtop"]!!/2f, DefaultSizes.defaultHeight["handtop"]!!/2f, DefaultSizes.defaultWidth["handtop"]!! + 0f, DefaultSizes.defaultHeight["handtop"]!! + 0f, 8.0f / (DefaultSizes.defaultWidth["handtop"]!! + 0f), 16.0f / (DefaultSizes.defaultHeight["handtop"]!! + 0f), -4.4671226f, 0, 0, DefaultSizes.defaultWidth["handtop"]!!, DefaultSizes.defaultHeight["handtop"]!!, false, false)
                                batch.draw(textures["handbottom"]!!.getTexture(timePassedSinceStart), x + -3.0f - DefaultSizes.defaultWidth["handbottom"]!!/2f, y - -40.0f - DefaultSizes.defaultHeight["handbottom"]!!/2f, DefaultSizes.defaultWidth["handbottom"]!!/2f, DefaultSizes.defaultHeight["handbottom"]!!/2f, DefaultSizes.defaultWidth["handbottom"]!! + 0f, DefaultSizes.defaultHeight["handbottom"]!! + 0f, 8.0f / (DefaultSizes.defaultWidth["handbottom"]!! + 0f), 16.0f / (DefaultSizes.defaultHeight["handbottom"]!! + 0f), -2.4895017f, 0, 0, DefaultSizes.defaultWidth["handbottom"]!!, DefaultSizes.defaultHeight["handbottom"]!!, false, false)
                            }
                            WeaponType.ONE_HANDED_AND_SHIELD ->
                            {
                            }
                            WeaponType.DUAL ->
                            {
                            }
                            WeaponType.TWO_HANDED ->
                            {
                            }
                            WeaponType.BOW ->
                            {
                            }
                            WeaponType.STAFF ->
                            {
                            }
                        }
                    MoveDirection.UP_RIGHT ->
                        when (weaponType) {
                            WeaponType.ONE_HANDED ->
                            {
                            }
                            WeaponType.ONE_HANDED_AND_SHIELD ->
                            {
                            }
                            WeaponType.DUAL ->
                            {
                            }
                            WeaponType.TWO_HANDED ->
                            {
                            }
                            WeaponType.BOW ->
                            {
                            }
                            WeaponType.STAFF ->
                            {
                            }
                        }
                    MoveDirection.UP ->
                        when (weaponType) {
                            WeaponType.ONE_HANDED ->
                            {
                            }
                            WeaponType.ONE_HANDED_AND_SHIELD ->
                            {
                            }
                            WeaponType.DUAL ->
                            {
                            }
                            WeaponType.TWO_HANDED ->
                            {
                            }
                            WeaponType.BOW ->
                            {
                            }
                            WeaponType.STAFF ->
                            {
                            }
                        }
                    MoveDirection.UP_LEFT ->
                        when (weaponType) {
                            WeaponType.ONE_HANDED ->
                            {
                            }
                            WeaponType.ONE_HANDED_AND_SHIELD ->
                            {
                            }
                            WeaponType.DUAL ->
                            {
                            }
                            WeaponType.TWO_HANDED ->
                            {
                            }
                            WeaponType.BOW ->
                            {
                            }
                            WeaponType.STAFF ->
                            {
                            }
                        }
                    MoveDirection.LEFT ->
                        when (weaponType) {
                            WeaponType.ONE_HANDED ->
                            {
                                batch.draw(textures["cloak"]!!.getTexture(timePassedSinceStart), x + 4.0f - DefaultSizes.defaultWidth["cloak"]!!/2f, y - -32.0f - DefaultSizes.defaultHeight["cloak"]!!/2f, DefaultSizes.defaultWidth["cloak"]!!/2f, DefaultSizes.defaultHeight["cloak"]!!/2f, DefaultSizes.defaultWidth["cloak"]!! + 0f, DefaultSizes.defaultHeight["cloak"]!! + 0f, 14.0f / (DefaultSizes.defaultWidth["cloak"]!! + 0f), 55.0f / (DefaultSizes.defaultHeight["cloak"]!! + 0f), 1.8279645f, 0, 0, DefaultSizes.defaultWidth["cloak"]!!, DefaultSizes.defaultHeight["cloak"]!!, false, false)
                                batch.draw(textures["handbottom"]!!.getTexture(timePassedSinceStart), x + -2.0f - DefaultSizes.defaultWidth["handbottom"]!!/2f, y - -39.0f - DefaultSizes.defaultHeight["handbottom"]!!/2f, DefaultSizes.defaultWidth["handbottom"]!!/2f, DefaultSizes.defaultHeight["handbottom"]!!/2f, DefaultSizes.defaultWidth["handbottom"]!! + 0f, DefaultSizes.defaultHeight["handbottom"]!! + 0f, 8.0f / (DefaultSizes.defaultWidth["handbottom"]!! + 0f), 16.0f / (DefaultSizes.defaultHeight["handbottom"]!! + 0f), -3.386295f, 0, 0, DefaultSizes.defaultWidth["handbottom"]!!, DefaultSizes.defaultHeight["handbottom"]!!, false, false)
                                batch.draw(textures["handtop"]!!.getTexture(timePassedSinceStart), x + -1.0f - DefaultSizes.defaultWidth["handtop"]!!/2f, y - -50.0f - DefaultSizes.defaultHeight["handtop"]!!/2f, DefaultSizes.defaultWidth["handtop"]!!/2f, DefaultSizes.defaultHeight["handtop"]!!/2f, DefaultSizes.defaultWidth["handtop"]!! + 0f, DefaultSizes.defaultHeight["handtop"]!! + 0f, 8.0f / (DefaultSizes.defaultWidth["handtop"]!! + 0f), 16.0f / (DefaultSizes.defaultHeight["handtop"]!! + 0f), 174.42778f, 0, 0, DefaultSizes.defaultWidth["handtop"]!!, DefaultSizes.defaultHeight["handtop"]!!, false, false)
                                batch.draw(textures["legtop"]!!.getTexture(timePassedSinceStart), x + 0.0f - DefaultSizes.defaultWidth["legtop"]!!/2f, y - -22.0f - DefaultSizes.defaultHeight["legtop"]!!/2f, DefaultSizes.defaultWidth["legtop"]!!/2f, DefaultSizes.defaultHeight["legtop"]!!/2f, DefaultSizes.defaultWidth["legtop"]!! + 0f, DefaultSizes.defaultHeight["legtop"]!! + 0f, 8.0f / (DefaultSizes.defaultWidth["legtop"]!! + 0f), 20.0f / (DefaultSizes.defaultHeight["legtop"]!! + 0f), -3.2396753f, 0, 0, DefaultSizes.defaultWidth["legtop"]!!, DefaultSizes.defaultHeight["legtop"]!!, false, false)
                                batch.draw(textures["legbottom"]!!.getTexture(timePassedSinceStart), x + -2.0f - DefaultSizes.defaultWidth["legbottom"]!!/2f, y - -11.0f - DefaultSizes.defaultHeight["legbottom"]!!/2f, DefaultSizes.defaultWidth["legbottom"]!!/2f, DefaultSizes.defaultHeight["legbottom"]!!/2f, DefaultSizes.defaultWidth["legbottom"]!! + 0f, DefaultSizes.defaultHeight["legbottom"]!! + 0f, 7.0f / (DefaultSizes.defaultWidth["legbottom"]!! + 0f), 17.0f / (DefaultSizes.defaultHeight["legbottom"]!! + 0f), -1.0608886f, 0, 0, DefaultSizes.defaultWidth["legbottom"]!!, DefaultSizes.defaultHeight["legbottom"]!!, false, false)
                                batch.draw(textures["body"]!!.getTexture(timePassedSinceStart), x + 1.0f - DefaultSizes.defaultWidth["body"]!!/2f, y - -42.0f - DefaultSizes.defaultHeight["body"]!!/2f, DefaultSizes.defaultWidth["body"]!!/2f, DefaultSizes.defaultHeight["body"]!!/2f, DefaultSizes.defaultWidth["body"]!! + 0f, DefaultSizes.defaultHeight["body"]!! + 0f, 12.0f / (DefaultSizes.defaultWidth["body"]!! + 0f), 32.0f / (DefaultSizes.defaultHeight["body"]!! + 0f), -0.69024223f, 0, 0, DefaultSizes.defaultWidth["body"]!!, DefaultSizes.defaultHeight["body"]!!, false, false)
                                batch.draw(textures["neck"]!!.getTexture(timePassedSinceStart), x + 1.0f - DefaultSizes.defaultWidth["neck"]!!/2f, y - -58.0f - DefaultSizes.defaultHeight["neck"]!!/2f, DefaultSizes.defaultWidth["neck"]!!/2f, DefaultSizes.defaultHeight["neck"]!!/2f, DefaultSizes.defaultWidth["neck"]!! + 0f, DefaultSizes.defaultHeight["neck"]!! + 0f, 9.0f / (DefaultSizes.defaultWidth["neck"]!! + 0f), 4.0f / (DefaultSizes.defaultHeight["neck"]!! + 0f), 1.8475024f, 0, 0, DefaultSizes.defaultWidth["neck"]!!, DefaultSizes.defaultHeight["neck"]!!, false, false)
                                batch.draw(textures["head4"]!!.getTexture(timePassedSinceStart), x + 2.0f - DefaultSizes.defaultWidth["head4"]!!/2f, y - -68.0f - DefaultSizes.defaultHeight["head4"]!!/2f, DefaultSizes.defaultWidth["head4"]!!/2f, DefaultSizes.defaultHeight["head4"]!!/2f, DefaultSizes.defaultWidth["head4"]!! + 0f, DefaultSizes.defaultHeight["head4"]!! + 0f, 37.0f / (DefaultSizes.defaultWidth["head4"]!! + 0f), 37.0f / (DefaultSizes.defaultHeight["head4"]!! + 0f), 2.0333126f, 0, 0, DefaultSizes.defaultWidth["head4"]!!, DefaultSizes.defaultHeight["head4"]!!, false, false)
                                batch.draw(textures["handbottom"]!!.getTexture(timePassedSinceStart), x + 4.0f - DefaultSizes.defaultWidth["handbottom"]!!/2f, y - -40.0f - DefaultSizes.defaultHeight["handbottom"]!!/2f, DefaultSizes.defaultWidth["handbottom"]!!/2f, DefaultSizes.defaultHeight["handbottom"]!!/2f, DefaultSizes.defaultWidth["handbottom"]!! + 0f, DefaultSizes.defaultHeight["handbottom"]!! + 0f, 8.0f / (DefaultSizes.defaultWidth["handbottom"]!! + 0f), 16.0f / (DefaultSizes.defaultHeight["handbottom"]!! + 0f), 3.2244918f, 0, 0, DefaultSizes.defaultWidth["handbottom"]!!, DefaultSizes.defaultHeight["handbottom"]!!, false, false)
                                batch.draw(textures["handtop"]!!.getTexture(timePassedSinceStart), x + 3.0f - DefaultSizes.defaultWidth["handtop"]!!/2f, y - -51.0f - DefaultSizes.defaultHeight["handtop"]!!/2f, DefaultSizes.defaultWidth["handtop"]!!/2f, DefaultSizes.defaultHeight["handtop"]!!/2f, DefaultSizes.defaultWidth["handtop"]!! + 0f, DefaultSizes.defaultHeight["handtop"]!! + 0f, 8.0f / (DefaultSizes.defaultWidth["handtop"]!! + 0f), 14.0f / (DefaultSizes.defaultHeight["handtop"]!! + 0f), 3.1363137f, 0, 0, DefaultSizes.defaultWidth["handtop"]!!, DefaultSizes.defaultHeight["handtop"]!!, false, false)
                                batch.draw(textures["legtop"]!!.getTexture(timePassedSinceStart), x + 1.0f - DefaultSizes.defaultWidth["legtop"]!!/2f, y - -22.0f - DefaultSizes.defaultHeight["legtop"]!!/2f, DefaultSizes.defaultWidth["legtop"]!!/2f, DefaultSizes.defaultHeight["legtop"]!!/2f, DefaultSizes.defaultWidth["legtop"]!! + 0f, DefaultSizes.defaultHeight["legtop"]!! + 0f, 7.0f / (DefaultSizes.defaultWidth["legtop"]!! + 0f), 19.0f / (DefaultSizes.defaultHeight["legtop"]!! + 0f), 2.1610248f, 0, 0, DefaultSizes.defaultWidth["legtop"]!!, DefaultSizes.defaultHeight["legtop"]!!, false, false)
                                batch.draw(textures["legbottom"]!!.getTexture(timePassedSinceStart), x + 2.0f - DefaultSizes.defaultWidth["legbottom"]!!/2f, y - -9.0f - DefaultSizes.defaultHeight["legbottom"]!!/2f, DefaultSizes.defaultWidth["legbottom"]!!/2f, DefaultSizes.defaultHeight["legbottom"]!!/2f, DefaultSizes.defaultWidth["legbottom"]!! + 0f, DefaultSizes.defaultHeight["legbottom"]!! + 0f, 8.0f / (DefaultSizes.defaultWidth["legbottom"]!! + 0f), 19.0f / (DefaultSizes.defaultHeight["legbottom"]!! + 0f), 1.6682812f, 0, 0, DefaultSizes.defaultWidth["legbottom"]!!, DefaultSizes.defaultHeight["legbottom"]!!, false, false)
                            }
                            WeaponType.ONE_HANDED_AND_SHIELD ->
                            {
                            }
                            WeaponType.DUAL ->
                            {
                            }
                            WeaponType.TWO_HANDED ->
                            {
                            }
                            WeaponType.BOW ->
                            {
                            }
                            WeaponType.STAFF ->
                            {
                            }
                        }
                    MoveDirection.DOWN_LEFT ->
                        when (weaponType) {
                            WeaponType.ONE_HANDED ->
                            {
                            }
                            WeaponType.ONE_HANDED_AND_SHIELD ->
                            {
                            }
                            WeaponType.DUAL ->
                            {
                            }
                            WeaponType.TWO_HANDED ->
                            {
                            }
                            WeaponType.BOW ->
                            {
                            }
                            WeaponType.STAFF ->
                            {
                            }
                        }
                    MoveDirection.DOWN ->
                        when (weaponType) {
                            WeaponType.ONE_HANDED ->
                            {
                            }
                            WeaponType.ONE_HANDED_AND_SHIELD ->
                            {
                            }
                            WeaponType.DUAL ->
                            {
                            }
                            WeaponType.TWO_HANDED ->
                            {
                            }
                            WeaponType.BOW ->
                            {
                            }
                            WeaponType.STAFF ->
                            {
                            }
                        }
                    MoveDirection.DOWN_RIGHT ->
                        when (weaponType) {
                            WeaponType.ONE_HANDED ->
                            {
                            }
                            WeaponType.ONE_HANDED_AND_SHIELD ->
                            {
                            }
                            WeaponType.DUAL ->
                            {
                            }
                            WeaponType.TWO_HANDED ->
                            {
                            }
                            WeaponType.BOW ->
                            {
                            }
                            WeaponType.STAFF ->
                            {
                            }
                        }
                }
        }
    }
}