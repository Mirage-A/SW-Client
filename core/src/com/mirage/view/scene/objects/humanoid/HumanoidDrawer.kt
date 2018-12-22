package com.mirage.view.scene.objects.humanoid

import com.mirage.view.TextureLoader
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.mirage.model.scene.Point
import com.mirage.view.scene.objects.AnimatedObjectDrawer
import java.util.HashMap


/**
 * ������������ � ������� Animation Editor-� ����� ��� ������ �� ��������� ��������� ����������
 * (�������� ��������� ������� �������� ��������� ������� SpriteAnimation)
 */
class HumanoidDrawer : AnimatedObjectDrawer {
    /**
     * ��������, ������� ����������� (��������, ���, ����� � �.�.)
     */
    var bodyAction = BodyAction.IDLE
    var legsAction = LegsAction.IDLE
    /**
     * ����������� ��������
     */
    var moveDirection: MoveDirection = MoveDirection.RIGHT
    /**
     * ��� ������ ��������� (����������, ���������, ��� ����������, ���������� � ���, ��� � �.�.)
     */
    var weaponType: WeaponType = WeaponType.ONE_HANDED

    /**
     * ������� �� ������� ���������� ������� ���������
     * ������ ��������� ����� head[0-7], body, handtop, handbottom, legtop, legbottom, cloak, weapon1, weapon2
     */
    var textures: MutableMap<String, AnimatedTexture>

    /**
     * ����� ������ �������� body
     */
    var bodyStartTime = 0L

    /**
     * ����� ������ �������� legs
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
                                drawLeftLeg(batch, x, y, legsTimePassedSinceStart)
                                batch.draw(textures["handtop"]!!.getTexture(), bodyX + curValue(1.3333334f, 1.3333334f, progress) - curValue(16f, 16f, progress) / 2, bodyY - curValue(-27.666668f, -27.666668f, progress) - curValue(32f, 32f, progress) / 2, curValue(16f, 16f, progress) / 2, curValue(32f, 32f, progress) / 2, curValue(16f, 16f, progress), curValue(32f, 32f, progress), curValue(0.55f, 0.55f, progress), curValue(0.55f, 0.55f, progress), curValue(3.9909205f, 3.9909205f, progress), 0, 0, 16, 32, false, false)
                                batch.draw(textures["handbottom"]!!.getTexture(), bodyX + curValue(2.6666667f, 2.6666667f, progress) - curValue(16f, 16f, progress) / 2, bodyY - curValue(-13.333334f, -13.333334f, progress) - curValue(32f, 32f, progress) / 2, curValue(16f, 16f, progress) / 2, curValue(32f, 32f, progress) / 2, curValue(16f, 16f, progress), curValue(32f, 32f, progress), curValue(0.5278f, 0.5278f, progress), curValue(0.58f, 0.58f, progress), curValue(5.9061465f, 5.9061465f, progress), 0, 0, 16, 32, false, false)
                                batch.draw(textures["body"]!!.getTexture(), bodyX + curValue(0.3333335f, 0.3333335f, progress) - curValue(30f, 30f, progress) / 2, bodyY - curValue(-16.0f, -16.0f, progress) - curValue(76f, 76f, progress) / 2, curValue(30f, 30f, progress) / 2, curValue(76f, 76f, progress) / 2, curValue(30f, 30f, progress), curValue(76f, 76f, progress), curValue(0.5251f, 0.5251f, progress), curValue(0.61359996f, 0.61359996f, progress), curValue(0.0f, 0.0f, progress), 0, 0, 30, 76, false, false)
                                batch.draw(textures["neck"]!!.getTexture(), bodyX + curValue(0.6666667f, 0.6666667f, progress) - curValue(22f, 22f, progress) / 2, bodyY - curValue(-38.166668f, -38.166668f, progress) - curValue(10f, 10f, progress) / 2, curValue(22f, 22f, progress) / 2, curValue(10f, 10f, progress) / 2, curValue(22f, 22f, progress), curValue(10f, 10f, progress), curValue(0.51f, 0.51f, progress), curValue(0.51f, 0.51f, progress), curValue(0.0f, 0.0f, progress), 0, 0, 22, 10, false, false)
                                batch.draw(textures["handtop"]!!.getTexture(), bodyX + curValue(0.6666666f, 0.6666666f, progress) - curValue(16f, 16f, progress) / 2, bodyY - curValue(-27.333332f, -27.333332f, progress) - curValue(32f, 32f, progress) / 2, curValue(16f, 16f, progress) / 2, curValue(32f, 32f, progress) / 2, curValue(16f, 16f, progress), curValue(32f, 32f, progress), curValue(0.58f, 0.58f, progress), curValue(0.58f, 0.58f, progress), curValue(2.0454094f, 2.0454094f, progress), 0, 0, 16, 32, false, false)
                                batch.draw(textures["handbottom"]!!.getTexture(), bodyX + curValue(0.8333333f, 0.8333333f, progress) - curValue(16f, 16f, progress) / 2, bodyY - curValue(-13.666666f, -13.666666f, progress) - curValue(32f, 32f, progress) / 2, curValue(16f, 16f, progress) / 2, curValue(32f, 32f, progress) / 2, curValue(16f, 16f, progress), curValue(32f, 32f, progress), curValue(0.56549996f, 0.56549996f, progress), curValue(0.65f, 0.65f, progress), curValue(2.1610856f, 2.1610856f, progress), 0, 0, 16, 32, false, false)
                                drawRightLeg(batch, x, y, legsTimePassedSinceStart)
                                batch.draw(textures["cloak"]!!.getTexture(), bodyX + curValue(-7.333333f, -7.333333f, progress) - curValue(114f, 114f, progress) / 2, bodyY - curValue(-5.0f, -5.0f, progress) - curValue(124f, 124f, progress) / 2, curValue(114f, 114f, progress) / 2, curValue(124f, 124f, progress) / 2, curValue(114f, 114f, progress), curValue(124f, 124f, progress), curValue(0.1025f, 0.1025f, progress), curValue(0.57809997f, 0.57809997f, progress), curValue(-2.3373058f, -2.3373058f, progress), 0, 0, 114, 124, false, false)
                                batch.draw(textures["head0"]!!.getTexture(), bodyX + curValue(-1.8333333f, -1.8333333f, progress) - curValue(74f, 74f, progress) / 2, bodyY - curValue(-49.0f, -49.0f, progress) - curValue(74f, 74f, progress) / 2, curValue(74f, 74f, progress) / 2, curValue(74f, 74f, progress) / 2, curValue(74f, 74f, progress), curValue(74f, 74f, progress), curValue(0.57f, 0.57f, progress), curValue(0.57f, 0.57f, progress), curValue(0.0f, 0.0f, progress), 0, 0, 74, 74, false, false)
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
                        val progress = 1f
                        return curValue(Point(-0.16666667f, 34.333332f), Point(-0.16666667f, 34.333332f), progress)
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
                        val progress = 1f
                        return curValue(Point(0.16666667f, 29.833332f), Point(0.16666667f, 29.833332f), progress)
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
                        val progress = 1f
                        batch.draw(textures["legbottom"]!!.getTexture(), x + curValue(-0.5f, -0.5f, progress) - curValue(16f, 16f, progress) / 2, y - curValue(-9.0f, -9.0f, progress) - curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress) / 2, curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress), curValue(40f, 40f, progress), curValue(0.59f, 0.59f, progress), curValue(0.5133f, 0.5133f, progress), curValue(-0.4814658f, -0.4814658f, progress), 0, 0, 16, 40, false, false)
                        batch.draw(textures["legtop"]!!.getTexture(), x + curValue(-0.5f, -0.5f, progress) - curValue(16f, 16f, progress) / 2, y - curValue(-24.833334f, -24.833334f, progress) - curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress) / 2, curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress), curValue(40f, 40f, progress), curValue(0.57f, 0.57f, progress), curValue(0.4902f, 0.4902f, progress), curValue(-1.636577f, -1.636577f, progress), 0, 0, 16, 40, false, false)
                    }
                    MoveDirection.UP_RIGHT -> {
                        val progress = 1f
                    }
                    MoveDirection.UP -> {
                    }
                    MoveDirection.UP_LEFT -> {
                    }
                    MoveDirection.LEFT -> {
                        val progress = 1f
                        batch.draw(textures["legbottom"]!!.getTexture(), x + curValue(-1.5f, -1.5f, progress) - curValue(16f, 16f, progress) / 2, y - curValue(-8.833334f, -8.833334f, progress) - curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress) / 2, curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress), curValue(40f, 40f, progress), curValue(0.66f, 0.66f, progress), curValue(0.57420003f, 0.57420003f, progress), curValue(0.0f, 0.0f, progress), 0, 0, 16, 40, false, false)
                        batch.draw(textures["legtop"]!!.getTexture(), x + curValue(-1.1666667f, -1.1666667f, progress) - curValue(16f, 16f, progress) / 2, y - curValue(-26.166668f, -26.166668f, progress) - curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress) / 2, curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress), curValue(40f, 40f, progress), curValue(0.62f, 0.62f, progress), curValue(0.5022f, 0.5022f, progress), curValue(0.0f, 0.0f, progress), 0, 0, 16, 40, false, false)
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
                        val progress = 1f
                        batch.draw(textures["legbottom"]!!.getTexture(), x + curValue(2.3333335f, 2.3333335f, progress) - curValue(16f, 16f, progress) / 2, y - curValue(-9.0f, -9.0f, progress) - curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress) / 2, curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress), curValue(40f, 40f, progress), curValue(0.7f, 0.7f, progress), curValue(0.595f, 0.595f, progress), curValue(0.0f, 0.0f, progress), 0, 0, 16, 40, false, false)
                        batch.draw(textures["legtop"]!!.getTexture(), x + curValue(2.3333333f, 2.3333333f, progress) - curValue(16f, 16f, progress) / 2, y - curValue(-25.666666f, -25.666666f, progress) - curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress) / 2, curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress), curValue(40f, 40f, progress), curValue(0.68f, 0.68f, progress), curValue(0.476f, 0.476f, progress), curValue(0.4244143f, 0.4244143f, progress), 0, 0, 16, 40, false, false)
                    }
                    MoveDirection.UP_RIGHT -> {
                        val progress = 1f
                    }
                    MoveDirection.UP -> {
                    }
                    MoveDirection.UP_LEFT -> {
                    }
                    MoveDirection.LEFT -> {
                        val progress = 1f
                        batch.draw(textures["legbottom"]!!.getTexture(), x + curValue(1.6666667f, 1.6666667f, progress) - curValue(16f, 16f, progress) / 2, y - curValue(-9.333334f, -9.333334f, progress) - curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress) / 2, curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress), curValue(40f, 40f, progress), curValue(0.61f, 0.61f, progress), curValue(0.51850003f, 0.51850003f, progress), curValue(2.0095646f, 2.0095646f, progress), 0, 0, 16, 40, false, false)
                        batch.draw(textures["legtop"]!!.getTexture(), x + curValue(0.8333333f, 0.8333333f, progress) - curValue(16f, 16f, progress) / 2, y - curValue(-25.833332f, -25.833332f, progress) - curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress) / 2, curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress), curValue(40f, 40f, progress), curValue(0.66f, 0.66f, progress), curValue(0.5082f, 0.5082f, progress), curValue(1.0609196f, 1.0609196f, progress), 0, 0, 16, 40, false, false)
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
