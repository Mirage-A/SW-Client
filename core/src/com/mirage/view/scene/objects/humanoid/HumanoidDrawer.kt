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
     * Должен содержать ключи head[0-7], body, handtop, handbottom, legtop, legbottom, cloak, weapon1, weapon2
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
        batch.draw(TextureLoader.load("tiles/0001.png"), x - 16, y - 8, 32f, 16f)
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
                                batch.draw(textures["cloak"]!!.getTexture(), bodyX + curValue(0.5f, 0.5f, progress) - curValue(114f, 114f, progress) / 2, bodyY - curValue(-39.166668f, -39.166668f, progress) - curValue(124f, 124f, progress) / 2, curValue(114f, 114f, progress) / 2, curValue(124f, 124f, progress) / 2, curValue(114f, 114f, progress), curValue(124f, 124f, progress), curValue(0.3904f, 0.3904f, progress), curValue(0.41480002f, 0.41480002f, progress), curValue(0.0f, 0.0f, progress), 0, 0, 114, 124, false, false)
                                drawLeftLeg(batch, x, y, legsTimePassedSinceStart)
                                drawRightLeg(batch, x, y, legsTimePassedSinceStart)
                                batch.draw(textures["neck"]!!.getTexture(), bodyX + curValue(0.0f, 0.0f, progress) - curValue(22f, 22f, progress) / 2, bodyY - curValue(0.0f, 0.0f, progress) - curValue(10f, 10f, progress) / 2, curValue(22f, 22f, progress) / 2, curValue(10f, 10f, progress) / 2, curValue(22f, 22f, progress), curValue(10f, 10f, progress), curValue(1.0f, 1.0f, progress), curValue(1.0f, 1.0f, progress), curValue(0.0f, 0.0f, progress), 0, 0, 22, 10, false, false)
                                batch.draw(textures["cloak"]!!.getTexture(), bodyX + curValue(0.16666669f, 0.16666669f, progress) - curValue(114f, 114f, progress) / 2, bodyY - curValue(0.16666794f, 0.16666794f, progress) - curValue(124f, 124f, progress) / 2, curValue(114f, 114f, progress) / 2, curValue(124f, 124f, progress) / 2, curValue(114f, 114f, progress), curValue(124f, 124f, progress), curValue(0.13f, 0.13f, progress), curValue(0.155f, 0.155f, progress), curValue(0.0f, 0.0f, progress), 0, 0, 114, 124, false, false)
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
                        when (true) {
                            timePassed < 500.0 -> {
                                val progress = (timePassed - 0.0f) / 500.0f
                                return curValue(Point(0.0f, -29.833336f), Point(0.0f, 42.5f), progress)

                            }
                            timePassed < 1000.0 -> {
                                val progress = (timePassed - 500.0f) / 500.0f
                                return curValue(Point(0.0f, 42.5f), Point(0.0f, -29.833336f), progress)

                            }
                            else -> {
                                val progress = 1f
                                return curValue(Point(0.0f, -29.833336f), Point(0.0f, -29.833336f), progress)
                            }
                        }
                    }
                    MoveDirection.UP_RIGHT -> {
                        val progress = 1f
                        return curValue(Point(0.0f, 0.0f), Point(0.0f, 0.0f), progress)
                    }
                    MoveDirection.UP -> {
                    }
                    MoveDirection.UP_LEFT -> {
                    }
                    MoveDirection.LEFT -> {
                    }
                    MoveDirection.DOWN_LEFT -> {
                    }
                    MoveDirection.DOWN -> {
                    }
                    MoveDirection.DOWN_RIGHT -> {
                    }
                }
            }
            LegsAction.RUNNING -> {
                val timePassed = legsTimePassedSinceStart % 1000L
                when (moveDirection) {
                    MoveDirection.RIGHT -> {
                        val progress = 1f
                        return curValue(Point(0.0f, -0.0f), Point(0.0f, -0.0f), progress)
                    }
                    MoveDirection.UP_RIGHT -> {
                    }
                    MoveDirection.UP -> {
                    }
                    MoveDirection.UP_LEFT -> {
                    }
                    MoveDirection.LEFT -> {
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
                        when (true) {
                            timePassed < 500.0 -> {
                                val progress = (timePassed - 0.0f) / 500.0f
                                batch.draw(textures["legbottom"]!!.getTexture(), x + curValue(-46.166668f, -25.833334f, progress) - curValue(16f, 16f, progress) / 2, y - curValue(-17.166666f, -18.166666f, progress) - curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress) / 2, curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress), curValue(40f, 40f, progress), curValue(1.0f, 1.0f, progress), curValue(1.0f, 1.0f, progress), curValue(0.0f, 32.52954f, progress), 0, 0, 16, 40, false, false)
                                batch.draw(textures["legtop"]!!.getTexture(), x + curValue(-46.333332f, -39.666668f, progress) - curValue(16f, 16f, progress) / 2, y - curValue(-54.0f, -51.833332f, progress) - curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress) / 2, curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress), curValue(40f, 40f, progress), curValue(1.0f, 1.0f, progress), curValue(1.0f, 1.0f, progress), curValue(0.0f, 14.743565f, progress), 0, 0, 16, 40, false, false)

                            }
                            timePassed < 1000.0 -> {
                                val progress = (timePassed - 500.0f) / 500.0f
                                batch.draw(textures["legbottom"]!!.getTexture(), x + curValue(-25.833334f, -46.166668f, progress) - curValue(16f, 16f, progress) / 2, y - curValue(-18.166666f, -17.166666f, progress) - curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress) / 2, curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress), curValue(40f, 40f, progress), curValue(1.0f, 1.0f, progress), curValue(1.0f, 1.0f, progress), curValue(32.52954f, 0.0f, progress), 0, 0, 16, 40, false, false)
                                batch.draw(textures["legtop"]!!.getTexture(), x + curValue(-39.666668f, -46.333332f, progress) - curValue(16f, 16f, progress) / 2, y - curValue(-51.833332f, -54.0f, progress) - curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress) / 2, curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress), curValue(40f, 40f, progress), curValue(1.0f, 1.0f, progress), curValue(1.0f, 1.0f, progress), curValue(14.743565f, 0.0f, progress), 0, 0, 16, 40, false, false)

                            }
                            else -> {
                                val progress = 1f
                                batch.draw(textures["legbottom"]!!.getTexture(), x + curValue(-46.166668f, -46.166668f, progress) - curValue(16f, 16f, progress) / 2, y - curValue(-17.166666f, -17.166666f, progress) - curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress) / 2, curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress), curValue(40f, 40f, progress), curValue(1.0f, 1.0f, progress), curValue(1.0f, 1.0f, progress), curValue(0.0f, 0.0f, progress), 0, 0, 16, 40, false, false)
                                batch.draw(textures["legtop"]!!.getTexture(), x + curValue(-46.333332f, -46.333332f, progress) - curValue(16f, 16f, progress) / 2, y - curValue(-54.0f, -54.0f, progress) - curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress) / 2, curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress), curValue(40f, 40f, progress), curValue(1.0f, 1.0f, progress), curValue(1.0f, 1.0f, progress), curValue(0.0f, 0.0f, progress), 0, 0, 16, 40, false, false)
                            }
                        }
                    }
                    MoveDirection.UP_RIGHT -> {
                        val progress = 1f
                    }
                    MoveDirection.UP -> {
                    }
                    MoveDirection.UP_LEFT -> {
                    }
                    MoveDirection.LEFT -> {
                    }
                    MoveDirection.DOWN_LEFT -> {
                    }
                    MoveDirection.DOWN -> {
                    }
                    MoveDirection.DOWN_RIGHT -> {
                    }
                }
            }
            LegsAction.RUNNING -> {
                val timePassed = legsTimePassedSinceStart % 1000L
                when (moveDirection) {
                    MoveDirection.RIGHT -> {
                        val progress = 1f
                    }
                    MoveDirection.UP_RIGHT -> {
                    }
                    MoveDirection.UP -> {
                    }
                    MoveDirection.UP_LEFT -> {
                    }
                    MoveDirection.LEFT -> {
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
                        when (true) {
                            timePassed < 500.0 -> {
                                val progress = (timePassed - 0.0f) / 500.0f
                                batch.draw(textures["legbottom"]!!.getTexture(), x + curValue(36.833332f, 24.333334f, progress) - curValue(16f, 16f, progress) / 2, y - curValue(-17.0f, -18.5f, progress) - curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress) / 2, curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress), curValue(40f, 40f, progress), curValue(1.0f, 1.0f, progress), curValue(1.0f, 1.0f, progress), curValue(0.0f, -35.65533f, progress), 0, 0, 16, 40, false, false)
                                batch.draw(textures["legtop"]!!.getTexture(), x + curValue(36.833332f, 39.5f, progress) - curValue(16f, 16f, progress) / 2, y - curValue(-54.666664f, -50.666668f, progress) - curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress) / 2, curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress), curValue(40f, 40f, progress), curValue(1.0f, 1.0f, progress), curValue(1.0f, 1.0f, progress), curValue(0.0f, -13.690049f, progress), 0, 0, 16, 40, false, false)

                            }
                            timePassed < 1000.0 -> {
                                val progress = (timePassed - 500.0f) / 500.0f
                                batch.draw(textures["legbottom"]!!.getTexture(), x + curValue(24.333334f, 36.833332f, progress) - curValue(16f, 16f, progress) / 2, y - curValue(-18.5f, -17.0f, progress) - curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress) / 2, curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress), curValue(40f, 40f, progress), curValue(1.0f, 1.0f, progress), curValue(1.0f, 1.0f, progress), curValue(-35.65533f, 0.0f, progress), 0, 0, 16, 40, false, false)
                                batch.draw(textures["legtop"]!!.getTexture(), x + curValue(39.5f, 36.833332f, progress) - curValue(16f, 16f, progress) / 2, y - curValue(-50.666668f, -54.666664f, progress) - curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress) / 2, curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress), curValue(40f, 40f, progress), curValue(1.0f, 1.0f, progress), curValue(1.0f, 1.0f, progress), curValue(-13.690049f, 0.0f, progress), 0, 0, 16, 40, false, false)

                            }
                            else -> {
                                val progress = 1f
                                batch.draw(textures["legbottom"]!!.getTexture(), x + curValue(36.833332f, 36.833332f, progress) - curValue(16f, 16f, progress) / 2, y - curValue(-17.0f, -17.0f, progress) - curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress) / 2, curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress), curValue(40f, 40f, progress), curValue(1.0f, 1.0f, progress), curValue(1.0f, 1.0f, progress), curValue(0.0f, 0.0f, progress), 0, 0, 16, 40, false, false)
                                batch.draw(textures["legtop"]!!.getTexture(), x + curValue(36.833332f, 36.833332f, progress) - curValue(16f, 16f, progress) / 2, y - curValue(-54.666664f, -54.666664f, progress) - curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress) / 2, curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress), curValue(40f, 40f, progress), curValue(1.0f, 1.0f, progress), curValue(1.0f, 1.0f, progress), curValue(0.0f, 0.0f, progress), 0, 0, 16, 40, false, false)
                            }
                        }
                    }
                    MoveDirection.UP_RIGHT -> {
                        val progress = 1f
                    }
                    MoveDirection.UP -> {
                    }
                    MoveDirection.UP_LEFT -> {
                    }
                    MoveDirection.LEFT -> {
                    }
                    MoveDirection.DOWN_LEFT -> {
                    }
                    MoveDirection.DOWN -> {
                    }
                    MoveDirection.DOWN_RIGHT -> {
                    }
                }
            }
            LegsAction.RUNNING -> {
                val timePassed = legsTimePassedSinceStart % 1000L
                when (moveDirection) {
                    MoveDirection.RIGHT -> {
                        val progress = 1f
                    }
                    MoveDirection.UP_RIGHT -> {
                    }
                    MoveDirection.UP -> {
                    }
                    MoveDirection.UP_LEFT -> {
                    }
                    MoveDirection.LEFT -> {
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
