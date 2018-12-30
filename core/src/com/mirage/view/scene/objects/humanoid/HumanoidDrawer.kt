package com.mirage.view.scene.objects.humanoid

import com.mirage.view.TextureLoader
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.mirage.model.scene.Point
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
    var bodyAction = BodyAction.IDLE
    var legsAction = LegsAction.IDLE
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
     * Должен содержать ключи head[RIGHT, DOWN, etc], body, handtop, handbottom, legtop, legbottom, cloak, weapon1, weapon2
     */
    var textures: MutableMap<String, AnimatedTexture>

    /**
     * Время начала анимации body
     */
    var bodyStartTime = 0L

    /**
     * Время начала анимации legs
     */
    var legsStartTime = 0L

    constructor() {
        textures = HashMap()
        for (md in MoveDirection.values()) {
            textures["head" + md.toString()] = StaticTexture(TextureLoader.load("equipment/head/0000" + md.toString() + ".png"))
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

    constructor(textures: MutableMap<String, AnimatedTexture>, bodyAction: BodyAction, legsAction: LegsAction, moveDirection: MoveDirection, weaponType: WeaponType) {
        this.textures = textures
        this.bodyAction = bodyAction
        this.legsAction = legsAction
        this.moveDirection = moveDirection
        this.weaponType = weaponType
    }


    private fun curValue(startValue: Float, endValue : Float, progress : Float) : Float {
        return startValue + (endValue - startValue) * progress
    }

    private fun curValue(startPoint: Point, endPoint: Point, progress: Float) : Point {
        return Point(curValue(startPoint.x, endPoint.x, progress), curValue(startPoint.y, endPoint.y, progress))
    }

    override fun draw(batch: SpriteBatch, x: Float, y: Float, timePassedSinceStart: Long) {
        val bodyTimePassedSinceStart = timePassedSinceStart - bodyStartTime
        val legsTimePassedSinceStart = timePassedSinceStart - legsStartTime
        val bodyPoint = getBodyPoint(legsTimePassedSinceStart)
        val bodyX = x + bodyPoint.x
        val bodyY = y + bodyPoint.y
        when (bodyAction) {
            BodyAction.IDLE -> {
                val timePassed = bodyTimePassedSinceStart % 1000L
                when (moveDirection) {
                    MoveDirection.RIGHT -> {
                        when (weaponType) {
                            WeaponType.ONE_HANDED -> {
                                val progress = 1f
                                batch.draw(textures["handtop"]!!.getTexture(), bodyX + curValue(0.0f, 0.0f, progress) - curValue(16f, 16f, progress) / 2, bodyY - curValue(-22.666666f, -22.666666f, progress) - curValue(32f, 32f, progress) / 2, curValue(16f, 16f, progress) / 2, curValue(32f, 32f, progress) / 2, curValue(16f, 16f, progress), curValue(32f, 32f, progress), curValue(0.57f, 0.57f, progress), curValue(0.5529f, 0.5529f, progress), curValue(1.0230393f, 1.0230393f, progress), 0, 0, 16, 32, false, false)
                                batch.draw(textures["handbottom"]!!.getTexture(), bodyX + curValue(1.0f, 1.0f, progress) - curValue(16f, 16f, progress) / 2, bodyY - curValue(-8.0f, -8.0f, progress) - curValue(32f, 32f, progress) / 2, curValue(16f, 16f, progress) / 2, curValue(32f, 32f, progress) / 2, curValue(16f, 16f, progress), curValue(32f, 32f, progress), curValue(0.61f, 0.61f, progress), curValue(0.6344f, 0.6344f, progress), curValue(2.5448055f, 2.5448055f, progress), 0, 0, 16, 32, false, false)
                                drawLeftLeg(batch, x, y, legsTimePassedSinceStart)
                                batch.draw(textures["body"]!!.getTexture(), bodyX + curValue(0.0f, 0.0f, progress) - curValue(30f, 30f, progress) / 2, bodyY - curValue(-12.0f, -12.0f, progress) - curValue(76f, 76f, progress) / 2, curValue(30f, 30f, progress) / 2, curValue(76f, 76f, progress) / 2, curValue(30f, 30f, progress), curValue(76f, 76f, progress), curValue(0.61f, 0.61f, progress), curValue(0.61f, 0.61f, progress), curValue(0.0f, 0.0f, progress), 0, 0, 30, 76, false, false)
                                batch.draw(textures["neck"]!!.getTexture(), bodyX + curValue(1.2944983f, 1.2944983f, progress) - curValue(22f, 22f, progress) / 2, bodyY - curValue(-33.585762f, -33.585762f, progress) - curValue(10f, 10f, progress) / 2, curValue(22f, 22f, progress) / 2, curValue(10f, 10f, progress) / 2, curValue(22f, 22f, progress), curValue(10f, 10f, progress), curValue(0.49f, 0.49f, progress), curValue(0.49f, 0.49f, progress), curValue(0.0f, 0.0f, progress), 0, 0, 22, 10, false, false)
                                drawRightLeg(batch, x, y, legsTimePassedSinceStart)
                                batch.draw(textures["handtop"]!!.getTexture(), bodyX + curValue(0.16666663f, 0.16666663f, progress) - curValue(16f, 16f, progress) / 2, bodyY - curValue(-22.0f, -22.0f, progress) - curValue(32f, 32f, progress) / 2, curValue(16f, 16f, progress) / 2, curValue(32f, 32f, progress) / 2, curValue(16f, 16f, progress), curValue(32f, 32f, progress), curValue(0.57f, 0.57f, progress), curValue(0.58709997f, 0.58709997f, progress), curValue(1.1934936f, 1.1934936f, progress), 0, 0, 16, 32, false, false)
                                batch.draw(textures["handbottom"]!!.getTexture(), bodyX + curValue(0.6666667f, 0.6666667f, progress) - curValue(16f, 16f, progress) / 2, bodyY - curValue(-7.333332f, -7.333332f, progress) - curValue(32f, 32f, progress) / 2, curValue(16f, 16f, progress) / 2, curValue(32f, 32f, progress) / 2, curValue(16f, 16f, progress), curValue(32f, 32f, progress), curValue(0.63f, 0.63f, progress), curValue(0.65519994f, 0.65519994f, progress), curValue(2.2906132f, 2.2906132f, progress), 0, 0, 16, 32, false, false)
                                batch.draw(textures["cloak"]!!.getTexture(), bodyX + curValue(-7.119741f, -7.119741f, progress) - curValue(114f, 114f, progress) / 2, bodyY - curValue(-5.82848f, -5.82848f, progress) - curValue(124f, 124f, progress) / 2, curValue(114f, 114f, progress) / 2, curValue(124f, 124f, progress) / 2, curValue(114f, 114f, progress), curValue(124f, 124f, progress), curValue(0.085f, 0.085f, progress), curValue(0.5f, 0.5f, progress), curValue(-5.2279816f, -5.2279816f, progress), 0, 0, 114, 124, false, false)
                                batch.draw(textures["headRIGHT"]!!.getTexture(), bodyX + curValue(-0.22006476f, -0.22006476f, progress) - curValue(74f, 74f, progress) / 2, bodyY - curValue(-43.779934f, -43.779934f, progress) - curValue(74f, 74f, progress) / 2, curValue(74f, 74f, progress) / 2, curValue(74f, 74f, progress) / 2, curValue(74f, 74f, progress), curValue(74f, 74f, progress), curValue(0.54f, 0.54f, progress), curValue(0.54f, 0.54f, progress), curValue(0.0f, 0.0f, progress), 0, 0, 74, 74, false, false)
                            }
                            WeaponType.ONE_HANDED_AND_SHIELD -> {
                            }
                            WeaponType.DUAL -> {
                            }
                            WeaponType.TWO_HANDED -> {
                            }
                            WeaponType.BOW -> {
                            }
                            WeaponType.STAFF -> {
                            }
                        }
                    }
                    MoveDirection.UP_RIGHT -> {
                        when (weaponType) {
                            WeaponType.ONE_HANDED -> {
                            }
                            WeaponType.ONE_HANDED_AND_SHIELD -> {
                            }
                            WeaponType.DUAL -> {
                            }
                            WeaponType.TWO_HANDED -> {
                            }
                            WeaponType.BOW -> {
                            }
                            WeaponType.STAFF -> {
                            }
                        }
                    }
                    MoveDirection.UP -> {
                        when (weaponType) {
                            WeaponType.ONE_HANDED -> {
                            }
                            WeaponType.ONE_HANDED_AND_SHIELD -> {
                            }
                            WeaponType.DUAL -> {
                            }
                            WeaponType.TWO_HANDED -> {
                            }
                            WeaponType.BOW -> {
                            }
                            WeaponType.STAFF -> {
                            }
                        }
                    }
                    MoveDirection.UP_LEFT -> {
                        when (weaponType) {
                            WeaponType.ONE_HANDED -> {
                            }
                            WeaponType.ONE_HANDED_AND_SHIELD -> {
                            }
                            WeaponType.DUAL -> {
                            }
                            WeaponType.TWO_HANDED -> {
                            }
                            WeaponType.BOW -> {
                            }
                            WeaponType.STAFF -> {
                            }
                        }
                    }
                    MoveDirection.LEFT -> {
                        when (weaponType) {
                            WeaponType.ONE_HANDED -> {
                                val progress = 1f
                                batch.draw(textures["handtop"]!!.getTexture(), bodyX + curValue(-0.16666663f, -0.16666663f, progress) - curValue(16f, 16f, progress) / 2, bodyY - curValue(-22.0f, -22.0f, progress) - curValue(32f, 32f, progress) / 2, curValue(16f, 16f, progress) / 2, curValue(32f, 32f, progress) / 2, curValue(16f, 16f, progress), curValue(32f, 32f, progress), curValue(0.57f, 0.57f, progress), curValue(0.58709997f, 0.58709997f, progress), curValue(-1.1934936f, -1.1934936f, progress), 0, 0, 16, 32, false, false)
                                batch.draw(textures["handbottom"]!!.getTexture(), bodyX + curValue(-0.6666667f, -0.6666667f, progress) - curValue(16f, 16f, progress) / 2, bodyY - curValue(-7.333332f, -7.333332f, progress) - curValue(32f, 32f, progress) / 2, curValue(16f, 16f, progress) / 2, curValue(32f, 32f, progress) / 2, curValue(16f, 16f, progress), curValue(32f, 32f, progress), curValue(0.63f, 0.63f, progress), curValue(0.65519994f, 0.65519994f, progress), curValue(-2.2906132f, -2.2906132f, progress), 0, 0, 16, 32, false, false)
                                drawRightLeg(batch, x, y, legsTimePassedSinceStart)
                                batch.draw(textures["body"]!!.getTexture(), bodyX + curValue(-0.0f, -0.0f, progress) - curValue(30f, 30f, progress) / 2, bodyY - curValue(-12.0f, -12.0f, progress) - curValue(76f, 76f, progress) / 2, curValue(30f, 30f, progress) / 2, curValue(76f, 76f, progress) / 2, curValue(30f, 30f, progress), curValue(76f, 76f, progress), curValue(0.61f, 0.61f, progress), curValue(0.61f, 0.61f, progress), curValue(-0.0f, -0.0f, progress), 0, 0, 30, 76, false, false)
                                batch.draw(textures["neck"]!!.getTexture(), bodyX + curValue(-1.2944983f, -1.2944983f, progress) - curValue(22f, 22f, progress) / 2, bodyY - curValue(-33.585762f, -33.585762f, progress) - curValue(10f, 10f, progress) / 2, curValue(22f, 22f, progress) / 2, curValue(10f, 10f, progress) / 2, curValue(22f, 22f, progress), curValue(10f, 10f, progress), curValue(0.49f, 0.49f, progress), curValue(0.49f, 0.49f, progress), curValue(-0.0f, -0.0f, progress), 0, 0, 22, 10, false, false)
                                drawLeftLeg(batch, x, y, legsTimePassedSinceStart)
                                batch.draw(textures["handtop"]!!.getTexture(), bodyX + curValue(-0.0f, -0.0f, progress) - curValue(16f, 16f, progress) / 2, bodyY - curValue(-22.666666f, -22.666666f, progress) - curValue(32f, 32f, progress) / 2, curValue(16f, 16f, progress) / 2, curValue(32f, 32f, progress) / 2, curValue(16f, 16f, progress), curValue(32f, 32f, progress), curValue(0.57f, 0.57f, progress), curValue(0.5529f, 0.5529f, progress), curValue(-1.0230393f, -1.0230393f, progress), 0, 0, 16, 32, false, false)
                                batch.draw(textures["handbottom"]!!.getTexture(), bodyX + curValue(-1.0f, -1.0f, progress) - curValue(16f, 16f, progress) / 2, bodyY - curValue(-8.0f, -8.0f, progress) - curValue(32f, 32f, progress) / 2, curValue(16f, 16f, progress) / 2, curValue(32f, 32f, progress) / 2, curValue(16f, 16f, progress), curValue(32f, 32f, progress), curValue(0.61f, 0.61f, progress), curValue(0.6344f, 0.6344f, progress), curValue(-2.5448055f, -2.5448055f, progress), 0, 0, 16, 32, false, false)
                                batch.draw(textures["cloak"]!!.getTexture(), bodyX + curValue(7.119741f, 7.119741f, progress) - curValue(114f, 114f, progress) / 2, bodyY - curValue(-5.82848f, -5.82848f, progress) - curValue(124f, 124f, progress) / 2, curValue(114f, 114f, progress) / 2, curValue(124f, 124f, progress) / 2, curValue(114f, 114f, progress), curValue(124f, 124f, progress), curValue(0.085f, 0.085f, progress), curValue(0.5f, 0.5f, progress), curValue(5.2279816f, 5.2279816f, progress), 0, 0, 114, 124, false, false)
                                batch.draw(textures["headLEFT"]!!.getTexture(), bodyX + curValue(0.22006476f, 0.22006476f, progress) - curValue(74f, 74f, progress) / 2, bodyY - curValue(-43.779934f, -43.779934f, progress) - curValue(74f, 74f, progress) / 2, curValue(74f, 74f, progress) / 2, curValue(74f, 74f, progress) / 2, curValue(74f, 74f, progress), curValue(74f, 74f, progress), curValue(0.54f, 0.54f, progress), curValue(0.54f, 0.54f, progress), curValue(-0.0f, -0.0f, progress), 0, 0, 74, 74, false, false)
                            }
                            WeaponType.ONE_HANDED_AND_SHIELD -> {
                            }
                            WeaponType.DUAL -> {
                            }
                            WeaponType.TWO_HANDED -> {
                            }
                            WeaponType.BOW -> {
                            }
                            WeaponType.STAFF -> {
                            }
                        }
                    }
                    MoveDirection.DOWN_LEFT -> {
                        when (weaponType) {
                            WeaponType.ONE_HANDED -> {
                            }
                            WeaponType.ONE_HANDED_AND_SHIELD -> {
                            }
                            WeaponType.DUAL -> {
                            }
                            WeaponType.TWO_HANDED -> {
                            }
                            WeaponType.BOW -> {
                            }
                            WeaponType.STAFF -> {
                            }
                        }
                    }
                    MoveDirection.DOWN -> {
                        when (weaponType) {
                            WeaponType.ONE_HANDED -> {
                            }
                            WeaponType.ONE_HANDED_AND_SHIELD -> {
                            }
                            WeaponType.DUAL -> {
                            }
                            WeaponType.TWO_HANDED -> {
                            }
                            WeaponType.BOW -> {
                            }
                            WeaponType.STAFF -> {
                            }
                        }
                    }
                    MoveDirection.DOWN_RIGHT -> {
                        when (weaponType) {
                            WeaponType.ONE_HANDED -> {
                            }
                            WeaponType.ONE_HANDED_AND_SHIELD -> {
                            }
                            WeaponType.DUAL -> {
                            }
                            WeaponType.TWO_HANDED -> {
                            }
                            WeaponType.BOW -> {
                            }
                            WeaponType.STAFF -> {
                            }
                        }
                    }
                }
            }
        }
    }
    private fun getBodyPoint(legsTimePassedSinceStart: Long) : Point {
        when (legsAction) {
            LegsAction.IDLE -> {
                val timePassed = legsTimePassedSinceStart % 1000L
                when (moveDirection) {
                    MoveDirection.RIGHT -> {
                        val progress = 1f
                        return curValue(Point(0.0f, 33.333332f), Point(0.0f, 33.333332f), progress)
                    }
                    MoveDirection.UP_RIGHT -> {
                    }
                    MoveDirection.UP -> {
                    }
                    MoveDirection.UP_LEFT -> {
                    }
                    MoveDirection.LEFT -> {
                        val progress = 1f
                        return curValue(Point(-0.0f, 33.333332f), Point(-0.0f, 33.333332f), progress)
                    }
                    MoveDirection.DOWN_LEFT -> {
                    }
                    MoveDirection.DOWN -> {
                    }
                    MoveDirection.DOWN_RIGHT -> {
                    }
                }
            }
        }
        return Point(0f, 0f)
    }
    private fun drawLeftLeg(batch: SpriteBatch, x: Float, y: Float, legsTimePassedSinceStart: Long) {
        when (legsAction) {
            LegsAction.IDLE -> {
                val timePassed = legsTimePassedSinceStart % 1000L
                when (moveDirection) {
                    MoveDirection.RIGHT -> {
                        val progress = 1f
                        batch.draw(textures["legtop"]!!.getTexture(), x + curValue(0.625f, 0.625f, progress) - curValue(16f, 16f, progress) / 2, y - curValue(-27.25f, -27.25f, progress) - curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress) / 2, curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress), curValue(40f, 40f, progress), curValue(0.56f, 0.56f, progress), curValue(0.4928f, 0.4928f, progress), curValue(1.1934894f, 1.1934894f, progress), 0, 0, 16, 40, false, false)
                        batch.draw(textures["legbottom"]!!.getTexture(), x + curValue(0.875f, 0.875f, progress) - curValue(16f, 16f, progress) / 2, y - curValue(-10.25f, -10.25f, progress) - curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress) / 2, curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress), curValue(40f, 40f, progress), curValue(0.5723f, 0.5723f, progress), curValue(0.5546f, 0.5546f, progress), curValue(0.0f, 0.0f, progress), 0, 0, 16, 40, false, false)
                    }
                    MoveDirection.UP_RIGHT -> {
                    }
                    MoveDirection.UP -> {
                    }
                    MoveDirection.UP_LEFT -> {
                    }
                    MoveDirection.LEFT -> {
                        val progress = 1f
                        batch.draw(textures["legtop"]!!.getTexture(), x + curValue(-0.625f, -0.625f, progress) - curValue(16f, 16f, progress) / 2, y - curValue(-27.25f, -27.25f, progress) - curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress) / 2, curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress), curValue(40f, 40f, progress), curValue(0.56f, 0.56f, progress), curValue(0.4928f, 0.4928f, progress), curValue(-1.1934894f, -1.1934894f, progress), 0, 0, 16, 40, false, false)
                        batch.draw(textures["legbottom"]!!.getTexture(), x + curValue(-0.875f, -0.875f, progress) - curValue(16f, 16f, progress) / 2, y - curValue(-10.25f, -10.25f, progress) - curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress) / 2, curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress), curValue(40f, 40f, progress), curValue(0.5723f, 0.5723f, progress), curValue(0.5546f, 0.5546f, progress), curValue(-0.0f, -0.0f, progress), 0, 0, 16, 40, false, false)
                    }
                    MoveDirection.DOWN_LEFT -> {
                    }
                    MoveDirection.DOWN -> {
                    }
                    MoveDirection.DOWN_RIGHT -> {
                    }
                }
            }
        }
    }
    private fun drawRightLeg(batch: SpriteBatch, x: Float, y: Float, legsTimePassedSinceStart: Long) {
        when (legsAction) {
            LegsAction.IDLE -> {
                val timePassed = legsTimePassedSinceStart % 1000L
                when (moveDirection) {
                    MoveDirection.RIGHT -> {
                        val progress = 1f
                        batch.draw(textures["legtop"]!!.getTexture(), x + curValue(0.875f, 0.875f, progress) - curValue(16f, 16f, progress) / 2, y - curValue(-27.125f, -27.125f, progress) - curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress) / 2, curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress), curValue(40f, 40f, progress), curValue(0.55679995f, 0.55679995f, progress), curValue(0.5046f, 0.5046f, progress), curValue(0.97518706f, 0.97518706f, progress), 0, 0, 16, 40, false, false)
                        batch.draw(textures["legbottom"]!!.getTexture(), x + curValue(1.0f, 1.0f, progress) - curValue(16f, 16f, progress) / 2, y - curValue(-10.375f, -10.375f, progress) - curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress) / 2, curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress), curValue(40f, 40f, progress), curValue(0.55f, 0.55f, progress), curValue(0.55f, 0.55f, progress), curValue(0.57294357f, 0.57294357f, progress), 0, 0, 16, 40, false, false)
                    }
                    MoveDirection.UP_RIGHT -> {
                    }
                    MoveDirection.UP -> {
                    }
                    MoveDirection.UP_LEFT -> {
                    }
                    MoveDirection.LEFT -> {
                        val progress = 1f
                        batch.draw(textures["legtop"]!!.getTexture(), x + curValue(-0.875f, -0.875f, progress) - curValue(16f, 16f, progress) / 2, y - curValue(-27.125f, -27.125f, progress) - curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress) / 2, curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress), curValue(40f, 40f, progress), curValue(0.55679995f, 0.55679995f, progress), curValue(0.5046f, 0.5046f, progress), curValue(-0.97518706f, -0.97518706f, progress), 0, 0, 16, 40, false, false)
                        batch.draw(textures["legbottom"]!!.getTexture(), x + curValue(-1.0f, -1.0f, progress) - curValue(16f, 16f, progress) / 2, y - curValue(-10.375f, -10.375f, progress) - curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress) / 2, curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress), curValue(40f, 40f, progress), curValue(0.55f, 0.55f, progress), curValue(0.55f, 0.55f, progress), curValue(-0.57294357f, -0.57294357f, progress), 0, 0, 16, 40, false, false)
                    }
                    MoveDirection.DOWN_LEFT -> {
                    }
                    MoveDirection.DOWN -> {
                    }
                    MoveDirection.DOWN_RIGHT -> {
                    }
                }
            }
        }
    }
}
