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
    var moveDirection: MoveDirection = MoveDirection.DOWN
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

    override fun draw(batch: SpriteBatch, x: Float, y: Float, timePassed: Long) {
        when (action) {
            Action.IDLE -> when (moveDirection) {
                MoveDirection.DOWN -> when (weaponType) {
                    WeaponType.ONE_HANDED -> {
                        batch.draw(textures["weapon1"]!!.getTexture(timePassed), x, y, (19 * 4).toFloat(), (48 * 4).toFloat(), (38 * 4).toFloat(), (96 * 4).toFloat(), 1f, 1f, timePassed / 10f, 0, 0, 38, 96, false, false)
                    }
                }
            }
        }
    }
}
