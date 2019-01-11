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
     * ������ ��������� ����� head[RIGHT, DOWN, etc], body, handtop, handbottom, legtop, legbottom, cloak, weapon1, weapon2
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
                                batch.draw(textures["handtop"]!!.getTexture(), bodyX + curValue(-0.28653294f, -0.28653294f, progress) - curValue(16f, 16f, progress) / 2, bodyY - curValue(-30.398281f, -30.398281f, progress) - curValue(32f, 32f, progress) / 2, curValue(16f, 16f, progress) / 2, curValue(32f, 32f, progress) / 2, curValue(16f, 16f, progress), curValue(32f, 32f, progress), curValue(0.705f, 0.705f, progress), curValue(0.735f, 0.735f, progress), curValue(1.0230393f, 1.0230393f, progress), 0, 0, 16, 32, false, false)
                                batch.draw(textures["handbottom"]!!.getTexture(), bodyX + curValue(0.14613175f, 0.14613175f, progress) - curValue(16f, 16f, progress) / 2, bodyY - curValue(-8.7621765f, -8.7621765f, progress) - curValue(32f, 32f, progress) / 2, curValue(16f, 16f, progress) / 2, curValue(32f, 32f, progress) / 2, curValue(16f, 16f, progress), curValue(32f, 32f, progress), curValue(0.6873f, 0.6873f, progress), curValue(0.8216f, 0.8216f, progress), curValue(2.5448055f, 2.5448055f, progress), 0, 0, 16, 32, false, false)
                                drawLeftLeg(batch, x, y, legsTimePassedSinceStart)
                                batch.draw(textures["body"]!!.getTexture(), bodyX + curValue(0.14285707f, 0.14285707f, progress) - curValue(30f, 30f, progress) / 2, bodyY - curValue(-16.821428f, -16.821428f, progress) - curValue(76f, 76f, progress) / 2, curValue(30f, 30f, progress) / 2, curValue(76f, 76f, progress) / 2, curValue(30f, 30f, progress), curValue(76f, 76f, progress), curValue(0.736f, 0.736f, progress), curValue(0.8f, 0.8f, progress), curValue(0.0f, 0.0f, progress), 0, 0, 30, 76, false, false)
                                batch.draw(textures["neck"]!!.getTexture(), bodyX + curValue(0.46428573f, 0.46428573f, progress) - curValue(22f, 22f, progress) / 2, bodyY - curValue(-45.107143f, -45.107143f, progress) - curValue(10f, 10f, progress) / 2, curValue(22f, 22f, progress) / 2, curValue(10f, 10f, progress) / 2, curValue(22f, 22f, progress), curValue(10f, 10f, progress), curValue(0.49f, 0.49f, progress), curValue(0.49f, 0.49f, progress), curValue(0.0f, 0.0f, progress), 0, 0, 22, 10, false, false)
                                drawRightLeg(batch, x, y, legsTimePassedSinceStart)
                                batch.draw(textures["handtop"]!!.getTexture(), bodyX + curValue(-0.4383955f, -0.4383955f, progress) - curValue(16f, 16f, progress) / 2, bodyY - curValue(-28.246418f, -28.246418f, progress) - curValue(32f, 32f, progress) / 2, curValue(16f, 16f, progress) / 2, curValue(32f, 32f, progress) / 2, curValue(16f, 16f, progress), curValue(32f, 32f, progress), curValue(0.7626f, 0.7626f, progress), curValue(0.84459996f, 0.84459996f, progress), curValue(1.1934936f, 1.1934936f, progress), 0, 0, 16, 32, false, false)
                                batch.draw(textures["weapon1"]!!.getTexture(), bodyX + curValue(24.2149f, 24.2149f, progress) - curValue(66f, 66f, progress) / 2, bodyY - curValue(-5.3295135f, -5.3295135f, progress) - curValue(106f, 106f, progress) / 2, curValue(66f, 66f, progress) / 2, curValue(106f, 106f, progress) / 2, curValue(66f, 66f, progress), curValue(106f, 106f, progress), curValue(0.67f, 0.67f, progress), curValue(0.67f, 0.67f, progress), curValue(-75.25026f, -75.25026f, progress), 0, 0, 66, 106, false, false)
                                batch.draw(textures["handbottom"]!!.getTexture(), bodyX + curValue(0.8595989f, 0.8595989f, progress) - curValue(16f, 16f, progress) / 2, bodyY - curValue(-6.90831f, -6.90831f, progress) - curValue(32f, 32f, progress) / 2, curValue(16f, 16f, progress) / 2, curValue(32f, 32f, progress) / 2, curValue(16f, 16f, progress), curValue(32f, 32f, progress), curValue(0.78959996f, 0.78959996f, progress), curValue(0.84839994f, 0.84839994f, progress), curValue(2.2906132f, 2.2906132f, progress), 0, 0, 16, 32, false, false)
                                batch.draw(textures["cloak"]!!.getTexture(), bodyX + curValue(-11.60745f, -11.60745f, progress) - curValue(114f, 114f, progress) / 2, bodyY - curValue(-0.8853874f, -0.8853874f, progress) - curValue(124f, 124f, progress) / 2, curValue(114f, 114f, progress) / 2, curValue(124f, 124f, progress) / 2, curValue(114f, 114f, progress), curValue(124f, 124f, progress), curValue(0.10919999f, 0.10919999f, progress), curValue(0.78f, 0.78f, progress), curValue(-2.3135138f, -2.3135138f, progress), 0, 0, 114, 124, false, false)
                                batch.draw(textures["headRIGHT"]!!.getTexture(), bodyX + curValue(-3.7142859f, -3.7142859f, progress) - curValue(74f, 74f, progress) / 2, bodyY - curValue(-59.071426f, -59.071426f, progress) - curValue(74f, 74f, progress) / 2, curValue(74f, 74f, progress) / 2, curValue(74f, 74f, progress) / 2, curValue(74f, 74f, progress), curValue(74f, 74f, progress), curValue(0.78f, 0.78f, progress), curValue(0.78f, 0.78f, progress), curValue(0.0f, 0.0f, progress), 0, 0, 74, 74, false, false)
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
                                val progress = 1f
                                batch.draw(textures["handtop"]!!.getTexture(), bodyX + curValue(-9.166667f, -9.166667f, progress) - curValue(16f, 16f, progress) / 2, bodyY - curValue(-30.0f, -30.0f, progress) - curValue(32f, 32f, progress) / 2, curValue(16f, 16f, progress) / 2, curValue(32f, 32f, progress) / 2, curValue(16f, 16f, progress), curValue(32f, 32f, progress), curValue(0.8624f, 0.8624f, progress), curValue(0.98f, 0.98f, progress), curValue(-3.447387f, -3.447387f, progress), 0, 0, 16, 32, false, false)
                                batch.draw(textures["handbottom"]!!.getTexture(), bodyX + curValue(-10.333333f, -10.333333f, progress) - curValue(16f, 16f, progress) / 2, bodyY - curValue(-6.833334f, -6.833334f, progress) - curValue(32f, 32f, progress) / 2, curValue(16f, 16f, progress) / 2, curValue(32f, 32f, progress) / 2, curValue(16f, 16f, progress), curValue(32f, 32f, progress), curValue(0.93f, 0.93f, progress), curValue(1.0f, 1.0f, progress), curValue(-3.0664856f, -3.0664856f, progress), 0, 0, 16, 32, false, false)
                                drawLeftLeg(batch, x, y, legsTimePassedSinceStart)
                                batch.draw(textures["body"]!!.getTexture(), bodyX + curValue(-0.83333325f, -0.83333325f, progress) - curValue(30f, 30f, progress) / 2, bodyY - curValue(-15.666668f, -15.666668f, progress) - curValue(76f, 76f, progress) / 2, curValue(30f, 30f, progress) / 2, curValue(76f, 76f, progress) / 2, curValue(30f, 30f, progress), curValue(76f, 76f, progress), curValue(0.82f, 0.82f, progress), curValue(0.82f, 0.82f, progress), curValue(0.0f, 0.0f, progress), 0, 0, 30, 76, false, false)
                                batch.draw(textures["neck"]!!.getTexture(), bodyX + curValue(-0.6666666f, -0.6666666f, progress) - curValue(22f, 22f, progress) / 2, bodyY - curValue(-44.666668f, -44.666668f, progress) - curValue(10f, 10f, progress) / 2, curValue(22f, 22f, progress) / 2, curValue(10f, 10f, progress) / 2, curValue(22f, 22f, progress), curValue(10f, 10f, progress), curValue(0.75f, 0.75f, progress), curValue(0.75f, 0.75f, progress), curValue(0.0f, 0.0f, progress), 0, 0, 22, 10, false, false)
                                drawRightLeg(batch, x, y, legsTimePassedSinceStart)
                                batch.draw(textures["handtop"]!!.getTexture(), bodyX + curValue(9.333333f, 9.333333f, progress) - curValue(16f, 16f, progress) / 2, bodyY - curValue(-28.5f, -28.5f, progress) - curValue(32f, 32f, progress) / 2, curValue(16f, 16f, progress) / 2, curValue(32f, 32f, progress) / 2, curValue(16f, 16f, progress), curValue(32f, 32f, progress), curValue(0.89f, 0.89f, progress), curValue(1.0f, 1.0f, progress), curValue(2.1610856f, 2.1610856f, progress), 0, 0, 16, 32, false, false)
                                batch.draw(textures["weapon1"]!!.getTexture(), bodyX + curValue(38.333332f, 38.333332f, progress) - curValue(66f, 66f, progress) / 2, bodyY - curValue(-5.166666f, -5.166666f, progress) - curValue(106f, 106f, progress) / 2, curValue(66f, 66f, progress) / 2, curValue(106f, 106f, progress) / 2, curValue(66f, 66f, progress), curValue(106f, 106f, progress), curValue(0.77f, 0.77f, progress), curValue(0.77f, 0.77f, progress), curValue(-65.67443f, -65.67443f, progress), 0, 0, 66, 106, false, false)
                                batch.draw(textures["handbottom"]!!.getTexture(), bodyX + curValue(10.5f, 10.5f, progress) - curValue(16f, 16f, progress) / 2, bodyY - curValue(-3.5f, -3.5f, progress) - curValue(32f, 32f, progress) / 2, curValue(16f, 16f, progress) / 2, curValue(32f, 32f, progress) / 2, curValue(16f, 16f, progress), curValue(32f, 32f, progress), curValue(0.9048f, 0.9048f, progress), curValue(1.0919999f, 1.0919999f, progress), curValue(3.0774646f, 3.0774646f, progress), 0, 0, 16, 32, false, false)
                                batch.draw(textures["cloak"]!!.getTexture(), bodyX + curValue(-4.72211f, -4.72211f, progress) - curValue(114f, 114f, progress) / 2, bodyY - curValue(-0.73427963f, -0.73427963f, progress) - curValue(124f, 124f, progress) / 2, curValue(114f, 114f, progress) / 2, curValue(124f, 124f, progress) / 2, curValue(114f, 114f, progress), curValue(124f, 124f, progress), curValue(0.4312f, 0.4312f, progress), curValue(0.77f, 0.77f, progress), curValue(-4.4212427f, -4.4212427f, progress), 0, 0, 114, 124, false, false)
                                batch.draw(textures["headUP_RIGHT"]!!.getTexture(), bodyX + curValue(-0.3333335f, -0.3333335f, progress) - curValue(74f, 74f, progress) / 2, bodyY - curValue(-59.166668f, -59.166668f, progress) - curValue(74f, 74f, progress) / 2, curValue(74f, 74f, progress) / 2, curValue(74f, 74f, progress) / 2, curValue(74f, 74f, progress), curValue(74f, 74f, progress), curValue(0.68f, 0.68f, progress), curValue(0.68f, 0.68f, progress), curValue(0.0f, 0.0f, progress), 0, 0, 74, 74, false, false)
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
                                val progress = 1f
                                batch.draw(textures["body"]!!.getTexture(), bodyX + curValue(-0.25f, -0.25f, progress) - curValue(30f, 30f, progress) / 2, bodyY - curValue(-15.875f, -15.875f, progress) - curValue(76f, 76f, progress) / 2, curValue(30f, 30f, progress) / 2, curValue(76f, 76f, progress) / 2, curValue(30f, 30f, progress), curValue(76f, 76f, progress), curValue(0.873f, 0.873f, progress), curValue(0.7857f, 0.7857f, progress), curValue(-0.0f, -0.0f, progress), 0, 0, 30, 76, false, false)
                                batch.draw(textures["neck"]!!.getTexture(), bodyX + curValue(-0.0f, -0.0f, progress) - curValue(22f, 22f, progress) / 2, bodyY - curValue(-44.625f, -44.625f, progress) - curValue(10f, 10f, progress) / 2, curValue(22f, 22f, progress) / 2, curValue(10f, 10f, progress) / 2, curValue(22f, 22f, progress), curValue(10f, 10f, progress), curValue(0.6308f, 0.6308f, progress), curValue(0.76f, 0.76f, progress), curValue(-0.0f, -0.0f, progress), 0, 0, 22, 10, false, false)
                                drawRightLeg(batch, x, y, legsTimePassedSinceStart)
                                drawLeftLeg(batch, x, y, legsTimePassedSinceStart)
                                batch.draw(textures["handtop"]!!.getTexture(), bodyX + curValue(-14.285714f, -14.285714f, progress) - curValue(16f, 16f, progress) / 2, bodyY - curValue(-29.285713f, -29.285713f, progress) - curValue(32f, 32f, progress) / 2, curValue(16f, 16f, progress) / 2, curValue(32f, 32f, progress) / 2, curValue(16f, 16f, progress), curValue(32f, 32f, progress), curValue(0.78400004f, 0.78400004f, progress), curValue(0.98f, 0.98f, progress), curValue(-1.3496454f, -1.3496454f, progress), 0, 0, 16, 32, false, false)
                                batch.draw(textures["handbottom"]!!.getTexture(), bodyX + curValue(-15.678572f, -15.678572f, progress) - curValue(16f, 16f, progress) / 2, bodyY - curValue(-4.8571434f, -4.8571434f, progress) - curValue(32f, 32f, progress) / 2, curValue(16f, 16f, progress) / 2, curValue(32f, 32f, progress) / 2, curValue(16f, 16f, progress), curValue(32f, 32f, progress), curValue(0.84f, 0.84f, progress), curValue(0.99f, 0.99f, progress), curValue(-0.98775464f, -0.98775464f, progress), 0, 0, 16, 32, false, false)
                                batch.draw(textures["handtop"]!!.getTexture(), bodyX + curValue(12.571428f, 12.571428f, progress) - curValue(16f, 16f, progress) / 2, bodyY - curValue(-28.142857f, -28.142857f, progress) - curValue(32f, 32f, progress) / 2, curValue(16f, 16f, progress) / 2, curValue(32f, 32f, progress) / 2, curValue(16f, 16f, progress), curValue(32f, 32f, progress), curValue(0.7857f, 0.7857f, progress), curValue(1.0185f, 1.0185f, progress), curValue(2.1692576f, 2.1692576f, progress), 0, 0, 16, 32, false, false)
                                batch.draw(textures["weapon1"]!!.getTexture(), bodyX + curValue(39.216217f, 39.216217f, progress) - curValue(66f, 66f, progress) / 2, bodyY - curValue(-7.567566f, -7.567566f, progress) - curValue(106f, 106f, progress) / 2, curValue(66f, 66f, progress) / 2, curValue(106f, 106f, progress) / 2, curValue(66f, 66f, progress), curValue(106f, 106f, progress), curValue(0.73f, 0.73f, progress), curValue(0.73f, 0.73f, progress), curValue(-67.434845f, -67.434845f, progress), 0, 0, 66, 106, false, false)
                                batch.draw(textures["handbottom"]!!.getTexture(), bodyX + curValue(14.214286f, 14.214286f, progress) - curValue(16f, 16f, progress) / 2, bodyY - curValue(-5.535713f, -5.535713f, progress) - curValue(32f, 32f, progress) / 2, curValue(16f, 16f, progress) / 2, curValue(32f, 32f, progress) / 2, curValue(16f, 16f, progress), curValue(32f, 32f, progress), curValue(0.79050004f, 0.79050004f, progress), curValue(0.93f, 0.93f, progress), curValue(2.1016543f, 2.1016543f, progress), 0, 0, 16, 32, false, false)
                                batch.draw(textures["cloak"]!!.getTexture(), bodyX + curValue(-0.34219265f, -0.34219265f, progress) - curValue(114f, 114f, progress) / 2, bodyY - curValue(1.2292366f, 1.2292366f, progress) - curValue(124f, 124f, progress) / 2, curValue(114f, 114f, progress) / 2, curValue(124f, 124f, progress) / 2, curValue(114f, 114f, progress), curValue(124f, 124f, progress), curValue(0.6138f, 0.6138f, progress), curValue(0.79359996f, 0.79359996f, progress), curValue(-0.0f, -0.0f, progress), 0, 0, 114, 124, false, false)
                                batch.draw(textures["headUP"]!!.getTexture(), bodyX + curValue(-0.0f, -0.0f, progress) - curValue(74f, 74f, progress) / 2, bodyY - curValue(-58.75f, -58.75f, progress) - curValue(74f, 74f, progress) / 2, curValue(74f, 74f, progress) / 2, curValue(74f, 74f, progress) / 2, curValue(74f, 74f, progress), curValue(74f, 74f, progress), curValue(0.61f, 0.61f, progress), curValue(0.61f, 0.61f, progress), curValue(-0.0f, -0.0f, progress), 0, 0, 74, 74, false, false)
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
                                val progress = 1f
                                batch.draw(textures["handtop"]!!.getTexture(), bodyX + curValue(9.166667f, 9.166667f, progress) - curValue(16f, 16f, progress) / 2, bodyY - curValue(-30.0f, -30.0f, progress) - curValue(32f, 32f, progress) / 2, curValue(16f, 16f, progress) / 2, curValue(32f, 32f, progress) / 2, curValue(16f, 16f, progress), curValue(32f, 32f, progress), curValue(0.8624f, 0.8624f, progress), curValue(0.98f, 0.98f, progress), curValue(3.447387f, 3.447387f, progress), 0, 0, 16, 32, false, false)
                                batch.draw(textures["handbottom"]!!.getTexture(), bodyX + curValue(10.333333f, 10.333333f, progress) - curValue(16f, 16f, progress) / 2, bodyY - curValue(-6.833334f, -6.833334f, progress) - curValue(32f, 32f, progress) / 2, curValue(16f, 16f, progress) / 2, curValue(32f, 32f, progress) / 2, curValue(16f, 16f, progress), curValue(32f, 32f, progress), curValue(0.93f, 0.93f, progress), curValue(1.0f, 1.0f, progress), curValue(3.0664856f, 3.0664856f, progress), 0, 0, 16, 32, false, false)
                                batch.draw(textures["weapon1"]!!.getTexture(), bodyX + curValue(-14.5f, -14.5f, progress) - curValue(66f, 66f, progress) / 2, bodyY - curValue(-8.5f, -8.5f, progress) - curValue(106f, 106f, progress) / 2, curValue(66f, 66f, progress) / 2, curValue(106f, 106f, progress) / 2, curValue(66f, 66f, progress), curValue(106f, 106f, progress), curValue(0.77f, 0.77f, progress), curValue(0.77f, 0.77f, progress), curValue(69.188675f, 69.188675f, progress), 0, 0, 66, 106, false, false)
                                drawRightLeg(batch, x, y, legsTimePassedSinceStart)
                                batch.draw(textures["body"]!!.getTexture(), bodyX + curValue(0.83333325f, 0.83333325f, progress) - curValue(30f, 30f, progress) / 2, bodyY - curValue(-15.666668f, -15.666668f, progress) - curValue(76f, 76f, progress) / 2, curValue(30f, 30f, progress) / 2, curValue(76f, 76f, progress) / 2, curValue(30f, 30f, progress), curValue(76f, 76f, progress), curValue(0.82f, 0.82f, progress), curValue(0.82f, 0.82f, progress), curValue(-0.0f, -0.0f, progress), 0, 0, 30, 76, false, false)
                                batch.draw(textures["neck"]!!.getTexture(), bodyX + curValue(0.6666666f, 0.6666666f, progress) - curValue(22f, 22f, progress) / 2, bodyY - curValue(-44.666668f, -44.666668f, progress) - curValue(10f, 10f, progress) / 2, curValue(22f, 22f, progress) / 2, curValue(10f, 10f, progress) / 2, curValue(22f, 22f, progress), curValue(10f, 10f, progress), curValue(0.75f, 0.75f, progress), curValue(0.75f, 0.75f, progress), curValue(-0.0f, -0.0f, progress), 0, 0, 22, 10, false, false)
                                drawLeftLeg(batch, x, y, legsTimePassedSinceStart)
                                batch.draw(textures["handtop"]!!.getTexture(), bodyX + curValue(-9.333333f, -9.333333f, progress) - curValue(16f, 16f, progress) / 2, bodyY - curValue(-28.5f, -28.5f, progress) - curValue(32f, 32f, progress) / 2, curValue(16f, 16f, progress) / 2, curValue(32f, 32f, progress) / 2, curValue(16f, 16f, progress), curValue(32f, 32f, progress), curValue(0.89f, 0.89f, progress), curValue(1.0f, 1.0f, progress), curValue(-2.1610856f, -2.1610856f, progress), 0, 0, 16, 32, false, false)
                                batch.draw(textures["handbottom"]!!.getTexture(), bodyX + curValue(-10.5f, -10.5f, progress) - curValue(16f, 16f, progress) / 2, bodyY - curValue(-3.5f, -3.5f, progress) - curValue(32f, 32f, progress) / 2, curValue(16f, 16f, progress) / 2, curValue(32f, 32f, progress) / 2, curValue(16f, 16f, progress), curValue(32f, 32f, progress), curValue(0.9048f, 0.9048f, progress), curValue(1.0919999f, 1.0919999f, progress), curValue(-3.0774646f, -3.0774646f, progress), 0, 0, 16, 32, false, false)
                                batch.draw(textures["cloak"]!!.getTexture(), bodyX + curValue(4.72211f, 4.72211f, progress) - curValue(114f, 114f, progress) / 2, bodyY - curValue(-0.73427963f, -0.73427963f, progress) - curValue(124f, 124f, progress) / 2, curValue(114f, 114f, progress) / 2, curValue(124f, 124f, progress) / 2, curValue(114f, 114f, progress), curValue(124f, 124f, progress), curValue(0.4312f, 0.4312f, progress), curValue(0.77f, 0.77f, progress), curValue(4.4212427f, 4.4212427f, progress), 0, 0, 114, 124, false, false)
                                batch.draw(textures["headUP_LEFT"]!!.getTexture(), bodyX + curValue(0.3333335f, 0.3333335f, progress) - curValue(74f, 74f, progress) / 2, bodyY - curValue(-59.166668f, -59.166668f, progress) - curValue(74f, 74f, progress) / 2, curValue(74f, 74f, progress) / 2, curValue(74f, 74f, progress) / 2, curValue(74f, 74f, progress), curValue(74f, 74f, progress), curValue(0.68f, 0.68f, progress), curValue(0.68f, 0.68f, progress), curValue(-0.0f, -0.0f, progress), 0, 0, 74, 74, false, false)
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
                                batch.draw(textures["handtop"]!!.getTexture(), bodyX + curValue(0.4383955f, 0.4383955f, progress) - curValue(16f, 16f, progress) / 2, bodyY - curValue(-28.246418f, -28.246418f, progress) - curValue(32f, 32f, progress) / 2, curValue(16f, 16f, progress) / 2, curValue(32f, 32f, progress) / 2, curValue(16f, 16f, progress), curValue(32f, 32f, progress), curValue(0.7626f, 0.7626f, progress), curValue(0.84459996f, 0.84459996f, progress), curValue(-1.1934936f, -1.1934936f, progress), 0, 0, 16, 32, false, false)
                                batch.draw(textures["handbottom"]!!.getTexture(), bodyX + curValue(-0.8595989f, -0.8595989f, progress) - curValue(16f, 16f, progress) / 2, bodyY - curValue(-6.90831f, -6.90831f, progress) - curValue(32f, 32f, progress) / 2, curValue(16f, 16f, progress) / 2, curValue(32f, 32f, progress) / 2, curValue(16f, 16f, progress), curValue(32f, 32f, progress), curValue(0.78959996f, 0.78959996f, progress), curValue(0.84839994f, 0.84839994f, progress), curValue(-2.2906132f, -2.2906132f, progress), 0, 0, 16, 32, false, false)
                                drawRightLeg(batch, x, y, legsTimePassedSinceStart)
                                batch.draw(textures["weapon1"]!!.getTexture(), bodyX + curValue(-24.916906f, -24.916906f, progress) - curValue(66f, 66f, progress) / 2, bodyY - curValue(-10.18338f, -10.18338f, progress) - curValue(106f, 106f, progress) / 2, curValue(66f, 66f, progress) / 2, curValue(106f, 106f, progress) / 2, curValue(66f, 66f, progress), curValue(106f, 106f, progress), curValue(0.67f, 0.67f, progress), curValue(0.67f, 0.67f, progress), curValue(75.25026f, 75.25026f, progress), 0, 0, 66, 106, false, false)
                                batch.draw(textures["body"]!!.getTexture(), bodyX + curValue(-0.14285707f, -0.14285707f, progress) - curValue(30f, 30f, progress) / 2, bodyY - curValue(-16.821428f, -16.821428f, progress) - curValue(76f, 76f, progress) / 2, curValue(30f, 30f, progress) / 2, curValue(76f, 76f, progress) / 2, curValue(30f, 30f, progress), curValue(76f, 76f, progress), curValue(0.736f, 0.736f, progress), curValue(0.8f, 0.8f, progress), curValue(-0.0f, -0.0f, progress), 0, 0, 30, 76, false, false)
                                batch.draw(textures["neck"]!!.getTexture(), bodyX + curValue(-0.46428573f, -0.46428573f, progress) - curValue(22f, 22f, progress) / 2, bodyY - curValue(-45.107143f, -45.107143f, progress) - curValue(10f, 10f, progress) / 2, curValue(22f, 22f, progress) / 2, curValue(10f, 10f, progress) / 2, curValue(22f, 22f, progress), curValue(10f, 10f, progress), curValue(0.49f, 0.49f, progress), curValue(0.49f, 0.49f, progress), curValue(-0.0f, -0.0f, progress), 0, 0, 22, 10, false, false)
                                drawLeftLeg(batch, x, y, legsTimePassedSinceStart)
                                batch.draw(textures["handtop"]!!.getTexture(), bodyX + curValue(0.85386825f, 0.85386825f, progress) - curValue(16f, 16f, progress) / 2, bodyY - curValue(-28.392551f, -28.392551f, progress) - curValue(32f, 32f, progress) / 2, curValue(16f, 16f, progress) / 2, curValue(32f, 32f, progress) / 2, curValue(16f, 16f, progress), curValue(32f, 32f, progress), curValue(0.7802f, 0.7802f, progress), curValue(0.8134f, 0.8134f, progress), curValue(-1.0230393f, -1.0230393f, progress), 0, 0, 16, 32, false, false)
                                batch.draw(textures["handbottom"]!!.getTexture(), bodyX + curValue(0.28080225f, 0.28080225f, progress) - curValue(16f, 16f, progress) / 2, bodyY - curValue(-7.464184f, -7.464184f, progress) - curValue(32f, 32f, progress) / 2, curValue(16f, 16f, progress) / 2, curValue(32f, 32f, progress) / 2, curValue(16f, 16f, progress), curValue(32f, 32f, progress), curValue(0.792f, 0.792f, progress), curValue(0.91519994f, 0.91519994f, progress), curValue(-2.5448055f, -2.5448055f, progress), 0, 0, 16, 32, false, false)
                                batch.draw(textures["cloak"]!!.getTexture(), bodyX + curValue(11.60745f, 11.60745f, progress) - curValue(114f, 114f, progress) / 2, bodyY - curValue(-0.8853874f, -0.8853874f, progress) - curValue(124f, 124f, progress) / 2, curValue(114f, 114f, progress) / 2, curValue(124f, 124f, progress) / 2, curValue(114f, 114f, progress), curValue(124f, 124f, progress), curValue(0.10919999f, 0.10919999f, progress), curValue(0.78f, 0.78f, progress), curValue(2.3135138f, 2.3135138f, progress), 0, 0, 114, 124, false, false)
                                batch.draw(textures["headLEFT"]!!.getTexture(), bodyX + curValue(3.7142859f, 3.7142859f, progress) - curValue(74f, 74f, progress) / 2, bodyY - curValue(-59.071426f, -59.071426f, progress) - curValue(74f, 74f, progress) / 2, curValue(74f, 74f, progress) / 2, curValue(74f, 74f, progress) / 2, curValue(74f, 74f, progress), curValue(74f, 74f, progress), curValue(0.78f, 0.78f, progress), curValue(0.78f, 0.78f, progress), curValue(-0.0f, -0.0f, progress), 0, 0, 74, 74, false, false)
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
                                val progress = 1f
                                batch.draw(textures["cloak"]!!.getTexture(), bodyX + curValue(2.4444444f, 2.4444444f, progress) - curValue(114f, 114f, progress) / 2, bodyY - curValue(0.66666794f, 0.66666794f, progress) - curValue(124f, 124f, progress) / 2, curValue(114f, 114f, progress) / 2, curValue(124f, 124f, progress) / 2, curValue(114f, 114f, progress), curValue(124f, 124f, progress), curValue(0.5624f, 0.5624f, progress), curValue(0.76f, 0.76f, progress), curValue(1.3670262f, 1.3670262f, progress), 0, 0, 114, 124, false, false)
                                batch.draw(textures["handtop"]!!.getTexture(), bodyX + curValue(-8.594816f, -8.594816f, progress) - curValue(16f, 16f, progress) / 2, bodyY - curValue(-27.673943f, -27.673943f, progress) - curValue(32f, 32f, progress) / 2, curValue(16f, 16f, progress) / 2, curValue(32f, 32f, progress) / 2, curValue(16f, 16f, progress), curValue(32f, 32f, progress), curValue(0.8277f, 0.8277f, progress), curValue(0.89f, 0.89f, progress), curValue(-4.7927985f, -4.7927985f, progress), 0, 0, 16, 32, false, false)
                                batch.draw(textures["handbottom"]!!.getTexture(), bodyX + curValue(-9.185539f, -9.185539f, progress) - curValue(16f, 16f, progress) / 2, bodyY - curValue(-5.21146f, -5.21146f, progress) - curValue(32f, 32f, progress) / 2, curValue(16f, 16f, progress) / 2, curValue(32f, 32f, progress) / 2, curValue(16f, 16f, progress), curValue(32f, 32f, progress), curValue(1.0f, 1.0f, progress), curValue(1.0f, 1.0f, progress), curValue(-4.597414f, -4.597414f, progress), 0, 0, 16, 32, false, false)
                                batch.draw(textures["weapon1"]!!.getTexture(), bodyX + curValue(-35.666668f, -35.666668f, progress) - curValue(66f, 66f, progress) / 2, bodyY - curValue(-3.5f, -3.5f, progress) - curValue(106f, 106f, progress) / 2, curValue(66f, 66f, progress) / 2, curValue(106f, 106f, progress) / 2, curValue(66f, 66f, progress), curValue(106f, 106f, progress), curValue(0.67f, 0.67f, progress), curValue(0.67f, 0.67f, progress), curValue(74.49496f, 74.49496f, progress), 0, 0, 66, 106, false, false)
                                drawRightLeg(batch, x, y, legsTimePassedSinceStart)
                                batch.draw(textures["body"]!!.getTexture(), bodyX + curValue(1.0f, 1.0f, progress) - curValue(30f, 30f, progress) / 2, bodyY - curValue(-15.333334f, -15.333334f, progress) - curValue(76f, 76f, progress) / 2, curValue(30f, 30f, progress) / 2, curValue(76f, 76f, progress) / 2, curValue(30f, 30f, progress), curValue(76f, 76f, progress), curValue(0.9f, 0.9f, progress), curValue(0.819f, 0.819f, progress), curValue(0.7794863f, 0.7794863f, progress), 0, 0, 30, 76, false, false)
                                batch.draw(textures["neck"]!!.getTexture(), bodyX + curValue(0.33333337f, 0.33333337f, progress) - curValue(22f, 22f, progress) / 2, bodyY - curValue(-43.666668f, -43.666668f, progress) - curValue(10f, 10f, progress) / 2, curValue(22f, 22f, progress) / 2, curValue(10f, 10f, progress) / 2, curValue(22f, 22f, progress), curValue(10f, 10f, progress), curValue(0.81f, 0.81f, progress), curValue(0.81f, 0.81f, progress), curValue(-0.0f, -0.0f, progress), 0, 0, 22, 10, false, false)
                                batch.draw(textures["headDOWN_LEFT"]!!.getTexture(), bodyX + curValue(0.16666651f, 0.16666651f, progress) - curValue(74f, 74f, progress) / 2, bodyY - curValue(-59.0f, -59.0f, progress) - curValue(74f, 74f, progress) / 2, curValue(74f, 74f, progress) / 2, curValue(74f, 74f, progress) / 2, curValue(74f, 74f, progress), curValue(74f, 74f, progress), curValue(0.6f, 0.6f, progress), curValue(0.6f, 0.6f, progress), curValue(-0.0f, -0.0f, progress), 0, 0, 74, 74, false, false)
                                drawLeftLeg(batch, x, y, legsTimePassedSinceStart)
                                batch.draw(textures["handtop"]!!.getTexture(), bodyX + curValue(10.869031f, 10.869031f, progress) - curValue(16f, 16f, progress) / 2, bodyY - curValue(-25.720327f, -25.720327f, progress) - curValue(32f, 32f, progress) / 2, curValue(16f, 16f, progress) / 2, curValue(32f, 32f, progress) / 2, curValue(16f, 16f, progress), curValue(32f, 32f, progress), curValue(1.0f, 1.0f, progress), curValue(1.0f, 1.0f, progress), curValue(1.8661526f, 1.8661526f, progress), 0, 0, 16, 32, false, false)
                                batch.draw(textures["handbottom"]!!.getTexture(), bodyX + curValue(12.096862f, 12.096862f, progress) - curValue(16f, 16f, progress) / 2, bodyY - curValue(-1.9372444f, -1.9372444f, progress) - curValue(32f, 32f, progress) / 2, curValue(16f, 16f, progress) / 2, curValue(32f, 32f, progress) / 2, curValue(16f, 16f, progress), curValue(32f, 32f, progress), curValue(1.0f, 1.0f, progress), curValue(1.0f, 1.0f, progress), curValue(2.215978f, 2.215978f, progress), 0, 0, 16, 32, false, false)
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
                                val progress = 1f
                                batch.draw(textures["cloak"]!!.getTexture(), bodyX + curValue(0.35548162f, 0.35548162f, progress) - curValue(114f, 114f, progress) / 2, bodyY - curValue(-0.069767f, -0.069767f, progress) - curValue(124f, 124f, progress) / 2, curValue(114f, 114f, progress) / 2, curValue(124f, 124f, progress) / 2, curValue(114f, 114f, progress), curValue(124f, 124f, progress), curValue(0.6138f, 0.6138f, progress), curValue(0.79359996f, 0.79359996f, progress), curValue(0.0f, 0.0f, progress), 0, 0, 114, 124, false, false)
                                batch.draw(textures["headDOWN"]!!.getTexture(), bodyX + curValue(0.0f, 0.0f, progress) - curValue(74f, 74f, progress) / 2, bodyY - curValue(-58.75f, -58.75f, progress) - curValue(74f, 74f, progress) / 2, curValue(74f, 74f, progress) / 2, curValue(74f, 74f, progress) / 2, curValue(74f, 74f, progress), curValue(74f, 74f, progress), curValue(0.61f, 0.61f, progress), curValue(0.61f, 0.61f, progress), curValue(0.0f, 0.0f, progress), 0, 0, 74, 74, false, false)
                                batch.draw(textures["body"]!!.getTexture(), bodyX + curValue(0.25f, 0.25f, progress) - curValue(30f, 30f, progress) / 2, bodyY - curValue(-15.875f, -15.875f, progress) - curValue(76f, 76f, progress) / 2, curValue(30f, 30f, progress) / 2, curValue(76f, 76f, progress) / 2, curValue(30f, 30f, progress), curValue(76f, 76f, progress), curValue(0.873f, 0.873f, progress), curValue(0.7857f, 0.7857f, progress), curValue(0.0f, 0.0f, progress), 0, 0, 30, 76, false, false)
                                batch.draw(textures["neck"]!!.getTexture(), bodyX + curValue(0.0f, 0.0f, progress) - curValue(22f, 22f, progress) / 2, bodyY - curValue(-44.625f, -44.625f, progress) - curValue(10f, 10f, progress) / 2, curValue(22f, 22f, progress) / 2, curValue(10f, 10f, progress) / 2, curValue(22f, 22f, progress), curValue(10f, 10f, progress), curValue(0.6308f, 0.6308f, progress), curValue(0.76f, 0.76f, progress), curValue(0.0f, 0.0f, progress), 0, 0, 22, 10, false, false)
                                drawLeftLeg(batch, x, y, legsTimePassedSinceStart)
                                drawRightLeg(batch, x, y, legsTimePassedSinceStart)
                                batch.draw(textures["handtop"]!!.getTexture(), bodyX + curValue(-12.571428f, -12.571428f, progress) - curValue(16f, 16f, progress) / 2, bodyY - curValue(-28.142857f, -28.142857f, progress) - curValue(32f, 32f, progress) / 2, curValue(16f, 16f, progress) / 2, curValue(32f, 32f, progress) / 2, curValue(16f, 16f, progress), curValue(32f, 32f, progress), curValue(0.7857f, 0.7857f, progress), curValue(1.0185f, 1.0185f, progress), curValue(-2.1692576f, -2.1692576f, progress), 0, 0, 16, 32, false, false)
                                batch.draw(textures["handbottom"]!!.getTexture(), bodyX + curValue(-14.214286f, -14.214286f, progress) - curValue(16f, 16f, progress) / 2, bodyY - curValue(-5.535713f, -5.535713f, progress) - curValue(32f, 32f, progress) / 2, curValue(16f, 16f, progress) / 2, curValue(32f, 32f, progress) / 2, curValue(16f, 16f, progress), curValue(32f, 32f, progress), curValue(0.79050004f, 0.79050004f, progress), curValue(0.93f, 0.93f, progress), curValue(-2.1016543f, -2.1016543f, progress), 0, 0, 16, 32, false, false)
                                batch.draw(textures["handtop"]!!.getTexture(), bodyX + curValue(14.285714f, 14.285714f, progress) - curValue(16f, 16f, progress) / 2, bodyY - curValue(-29.285713f, -29.285713f, progress) - curValue(32f, 32f, progress) / 2, curValue(16f, 16f, progress) / 2, curValue(32f, 32f, progress) / 2, curValue(16f, 16f, progress), curValue(32f, 32f, progress), curValue(0.78400004f, 0.78400004f, progress), curValue(0.98f, 0.98f, progress), curValue(1.3496454f, 1.3496454f, progress), 0, 0, 16, 32, false, false)
                                batch.draw(textures["handbottom"]!!.getTexture(), bodyX + curValue(15.678572f, 15.678572f, progress) - curValue(16f, 16f, progress) / 2, bodyY - curValue(-4.8571434f, -4.8571434f, progress) - curValue(32f, 32f, progress) / 2, curValue(16f, 16f, progress) / 2, curValue(32f, 32f, progress) / 2, curValue(16f, 16f, progress), curValue(32f, 32f, progress), curValue(0.84f, 0.84f, progress), curValue(0.99f, 0.99f, progress), curValue(0.98775464f, 0.98775464f, progress), 0, 0, 16, 32, false, false)
                                batch.draw(textures["weapon1"]!!.getTexture(), bodyX + curValue(-39.216217f, -39.216217f, progress) - curValue(66f, 66f, progress) / 2, bodyY - curValue(-7.567566f, -7.567566f, progress) - curValue(106f, 106f, progress) / 2, curValue(66f, 66f, progress) / 2, curValue(106f, 106f, progress) / 2, curValue(66f, 66f, progress), curValue(106f, 106f, progress), curValue(0.73f, 0.73f, progress), curValue(0.73f, 0.73f, progress), curValue(67.434845f, 67.434845f, progress), 0, 0, 66, 106, false, false)
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
                                val progress = 1f
                                batch.draw(textures["cloak"]!!.getTexture(), bodyX + curValue(-4.4444447f, -4.4444447f, progress) - curValue(114f, 114f, progress) / 2, bodyY - curValue(0.33333206f, 0.33333206f, progress) - curValue(124f, 124f, progress) / 2, curValue(114f, 114f, progress) / 2, curValue(124f, 124f, progress) / 2, curValue(114f, 114f, progress), curValue(124f, 124f, progress), curValue(0.5624f, 0.5624f, progress), curValue(0.76f, 0.76f, progress), curValue(-1.3670262f, -1.3670262f, progress), 0, 0, 114, 124, false, false)
                                batch.draw(textures["handtop"]!!.getTexture(), bodyX + curValue(8.594816f, 8.594816f, progress) - curValue(16f, 16f, progress) / 2, bodyY - curValue(-27.673943f, -27.673943f, progress) - curValue(32f, 32f, progress) / 2, curValue(16f, 16f, progress) / 2, curValue(32f, 32f, progress) / 2, curValue(16f, 16f, progress), curValue(32f, 32f, progress), curValue(0.8277f, 0.8277f, progress), curValue(0.89f, 0.89f, progress), curValue(4.7927985f, 4.7927985f, progress), 0, 0, 16, 32, false, false)
                                batch.draw(textures["handbottom"]!!.getTexture(), bodyX + curValue(9.185539f, 9.185539f, progress) - curValue(16f, 16f, progress) / 2, bodyY - curValue(-5.21146f, -5.21146f, progress) - curValue(32f, 32f, progress) / 2, curValue(16f, 16f, progress) / 2, curValue(32f, 32f, progress) / 2, curValue(16f, 16f, progress), curValue(32f, 32f, progress), curValue(1.0f, 1.0f, progress), curValue(1.0f, 1.0f, progress), curValue(4.597414f, 4.597414f, progress), 0, 0, 16, 32, false, false)
                                drawLeftLeg(batch, x, y, legsTimePassedSinceStart)
                                batch.draw(textures["body"]!!.getTexture(), bodyX + curValue(-1.0f, -1.0f, progress) - curValue(30f, 30f, progress) / 2, bodyY - curValue(-15.333334f, -15.333334f, progress) - curValue(76f, 76f, progress) / 2, curValue(30f, 30f, progress) / 2, curValue(76f, 76f, progress) / 2, curValue(30f, 30f, progress), curValue(76f, 76f, progress), curValue(0.9f, 0.9f, progress), curValue(0.819f, 0.819f, progress), curValue(-0.7794863f, -0.7794863f, progress), 0, 0, 30, 76, false, false)
                                batch.draw(textures["neck"]!!.getTexture(), bodyX + curValue(-0.33333337f, -0.33333337f, progress) - curValue(22f, 22f, progress) / 2, bodyY - curValue(-43.666668f, -43.666668f, progress) - curValue(10f, 10f, progress) / 2, curValue(22f, 22f, progress) / 2, curValue(10f, 10f, progress) / 2, curValue(22f, 22f, progress), curValue(10f, 10f, progress), curValue(0.81f, 0.81f, progress), curValue(0.81f, 0.81f, progress), curValue(0.0f, 0.0f, progress), 0, 0, 22, 10, false, false)
                                batch.draw(textures["headDOWN_RIGHT"]!!.getTexture(), bodyX + curValue(-0.16666651f, -0.16666651f, progress) - curValue(74f, 74f, progress) / 2, bodyY - curValue(-59.0f, -59.0f, progress) - curValue(74f, 74f, progress) / 2, curValue(74f, 74f, progress) / 2, curValue(74f, 74f, progress) / 2, curValue(74f, 74f, progress), curValue(74f, 74f, progress), curValue(0.6f, 0.6f, progress), curValue(0.6f, 0.6f, progress), curValue(0.0f, 0.0f, progress), 0, 0, 74, 74, false, false)
                                drawRightLeg(batch, x, y, legsTimePassedSinceStart)
                                batch.draw(textures["handtop"]!!.getTexture(), bodyX + curValue(-10.869031f, -10.869031f, progress) - curValue(16f, 16f, progress) / 2, bodyY - curValue(-25.720327f, -25.720327f, progress) - curValue(32f, 32f, progress) / 2, curValue(16f, 16f, progress) / 2, curValue(32f, 32f, progress) / 2, curValue(16f, 16f, progress), curValue(32f, 32f, progress), curValue(1.0f, 1.0f, progress), curValue(1.0f, 1.0f, progress), curValue(-1.8661526f, -1.8661526f, progress), 0, 0, 16, 32, false, false)
                                batch.draw(textures["weapon1"]!!.getTexture(), bodyX + curValue(14.395209f, 14.395209f, progress) - curValue(66f, 66f, progress) / 2, bodyY - curValue(-0.7005997f, -0.7005997f, progress) - curValue(106f, 106f, progress) / 2, curValue(66f, 66f, progress) / 2, curValue(106f, 106f, progress) / 2, curValue(66f, 66f, progress), curValue(106f, 106f, progress), curValue(0.78f, 0.78f, progress), curValue(0.78f, 0.78f, progress), curValue(-72.72653f, -72.72653f, progress), 0, 0, 66, 106, false, false)
                                batch.draw(textures["handbottom"]!!.getTexture(), bodyX + curValue(-12.096862f, -12.096862f, progress) - curValue(16f, 16f, progress) / 2, bodyY - curValue(-1.9372444f, -1.9372444f, progress) - curValue(32f, 32f, progress) / 2, curValue(16f, 16f, progress) / 2, curValue(32f, 32f, progress) / 2, curValue(16f, 16f, progress), curValue(32f, 32f, progress), curValue(1.0f, 1.0f, progress), curValue(1.0f, 1.0f, progress), curValue(-2.215978f, -2.215978f, progress), 0, 0, 16, 32, false, false)
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
                        return curValue(Point(0.0f, 58.0f), Point(0.0f, 58.0f), progress)
                    }
                    MoveDirection.UP_RIGHT -> {
                        val progress = 1f
                        return curValue(Point(0.16666667f, 58.0f), Point(0.16666667f, 58.0f), progress)
                    }
                    MoveDirection.UP -> {
                        val progress = 1f
                        return curValue(Point(0.0f, 57.833332f), Point(0.0f, 57.833332f), progress)
                    }
                    MoveDirection.UP_LEFT -> {
                        val progress = 1f
                        return curValue(Point(-0.16666667f, 58.0f), Point(-0.16666667f, 58.0f), progress)
                    }
                    MoveDirection.LEFT -> {
                        val progress = 1f
                        return curValue(Point(-0.0f, 58.0f), Point(-0.0f, 58.0f), progress)
                    }
                    MoveDirection.DOWN_LEFT -> {
                        val progress = 1f
                        return curValue(Point(-0.0f, 57.833332f), Point(-0.0f, 57.833332f), progress)
                    }
                    MoveDirection.DOWN -> {
                        val progress = 1f
                        return curValue(Point(-0.0f, 57.833332f), Point(-0.0f, 57.833332f), progress)
                    }
                    MoveDirection.DOWN_RIGHT -> {
                        val progress = 1f
                        return curValue(Point(0.0f, 57.833332f), Point(0.0f, 57.833332f), progress)
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
                        batch.draw(textures["legtop"]!!.getTexture(), x + curValue(0.5f, 0.5f, progress) - curValue(16f, 16f, progress) / 2, y - curValue(-47.5f, -47.5f, progress) - curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress) / 2, curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress), curValue(40f, 40f, progress), curValue(0.93f, 0.93f, progress), curValue(0.8835f, 0.8835f, progress), curValue(1.1934894f, 1.1934894f, progress), 0, 0, 16, 40, false, false)
                        batch.draw(textures["legbottom"]!!.getTexture(), x + curValue(0.6666666f, 0.6666666f, progress) - curValue(16f, 16f, progress) / 2, y - curValue(-18.333334f, -18.333334f, progress) - curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress) / 2, curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress), curValue(40f, 40f, progress), curValue(0.97f, 0.97f, progress), curValue(0.97f, 0.97f, progress), curValue(0.0f, 0.0f, progress), 0, 0, 16, 40, false, false)
                    }
                    MoveDirection.UP_RIGHT -> {
                        val progress = 1f
                        batch.draw(textures["legbottom"]!!.getTexture(), x + curValue(-7.5f, -7.5f, progress) - curValue(16f, 16f, progress) / 2, y - curValue(-16.166666f, -16.166666f, progress) - curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress) / 2, curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress), curValue(40f, 40f, progress), curValue(0.9f, 0.9f, progress), curValue(0.9f, 0.9f, progress), curValue(-1.6683375f, -1.6683375f, progress), 0, 0, 16, 40, false, false)
                        batch.draw(textures["legtop"]!!.getTexture(), x + curValue(-6.5f, -6.5f, progress) - curValue(16f, 16f, progress) / 2, y - curValue(-42.5f, -42.5f, progress) - curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress) / 2, curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress), curValue(40f, 40f, progress), curValue(0.9f, 0.9f, progress), curValue(0.828f, 0.828f, progress), curValue(-1.7357047f, -1.7357047f, progress), 0, 0, 16, 40, false, false)
                    }
                    MoveDirection.UP -> {
                        val progress = 1f
                        batch.draw(textures["legbottom"]!!.getTexture(), x + curValue(7.0f, 7.0f, progress) - curValue(16f, 16f, progress) / 2, y - curValue(-16.5f, -16.5f, progress) - curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress) / 2, curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress), curValue(40f, 40f, progress), curValue(0.86f, 0.86f, progress), curValue(0.8944f, 0.8944f, progress), curValue(0.8069294f, 0.8069294f, progress), 0, 0, 16, 40, false, false)
                        batch.draw(textures["legtop"]!!.getTexture(), x + curValue(6.8333335f, 6.8333335f, progress) - curValue(16f, 16f, progress) / 2, y - curValue(-43.5f, -43.5f, progress) - curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress) / 2, curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress), curValue(40f, 40f, progress), curValue(0.86f, 0.86f, progress), curValue(0.8342f, 0.8342f, progress), curValue(0.7252295f, 0.7252295f, progress), 0, 0, 16, 40, false, false)
                    }
                    MoveDirection.UP_LEFT -> {
                        val progress = 1f
                        batch.draw(textures["legbottom"]!!.getTexture(), x + curValue(-7.6666665f, -7.6666665f, progress) - curValue(16f, 16f, progress) / 2, y - curValue(-13.333334f, -13.333334f, progress) - curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress) / 2, curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress), curValue(40f, 40f, progress), curValue(0.94f, 0.94f, progress), curValue(0.94f, 0.94f, progress), curValue(-2.385949f, -2.385949f, progress), 0, 0, 16, 40, false, false)
                        batch.draw(textures["legtop"]!!.getTexture(), x + curValue(-7.0f, -7.0f, progress) - curValue(16f, 16f, progress) / 2, y - curValue(-41.166668f, -41.166668f, progress) - curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress) / 2, curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress), curValue(40f, 40f, progress), curValue(0.93f, 0.93f, progress), curValue(0.8742f, 0.8742f, progress), curValue(-2.726311f, -2.726311f, progress), 0, 0, 16, 40, false, false)
                    }
                    MoveDirection.LEFT -> {
                        val progress = 1f
                        batch.draw(textures["legtop"]!!.getTexture(), x + curValue(-0.5f, -0.5f, progress) - curValue(16f, 16f, progress) / 2, y - curValue(-47.5f, -47.5f, progress) - curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress) / 2, curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress), curValue(40f, 40f, progress), curValue(0.93f, 0.93f, progress), curValue(0.8835f, 0.8835f, progress), curValue(-1.1934894f, -1.1934894f, progress), 0, 0, 16, 40, false, false)
                        batch.draw(textures["legbottom"]!!.getTexture(), x + curValue(-0.6666666f, -0.6666666f, progress) - curValue(16f, 16f, progress) / 2, y - curValue(-18.333334f, -18.333334f, progress) - curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress) / 2, curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress), curValue(40f, 40f, progress), curValue(0.97f, 0.97f, progress), curValue(0.97f, 0.97f, progress), curValue(-0.0f, -0.0f, progress), 0, 0, 16, 40, false, false)
                    }
                    MoveDirection.DOWN_LEFT -> {
                        val progress = 1f
                        batch.draw(textures["legtop"]!!.getTexture(), x + curValue(5.6666665f, 5.6666665f, progress) - curValue(16f, 16f, progress) / 2, y - curValue(-43.166668f, -43.166668f, progress) - curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress) / 2, curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress), curValue(40f, 40f, progress), curValue(0.93f, 0.93f, progress), curValue(0.93f, 0.93f, progress), curValue(2.0700305f, 2.0700305f, progress), 0, 0, 16, 40, false, false)
                        batch.draw(textures["legbottom"]!!.getTexture(), x + curValue(7.3333335f, 7.3333335f, progress) - curValue(16f, 16f, progress) / 2, y - curValue(-14.666666f, -14.666666f, progress) - curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress) / 2, curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress), curValue(40f, 40f, progress), curValue(0.93f, 0.93f, progress), curValue(0.93f, 0.93f, progress), curValue(2.8624053f, 2.8624053f, progress), 0, 0, 16, 40, false, false)
                    }
                    MoveDirection.DOWN -> {
                        val progress = 1f
                        batch.draw(textures["legbottom"]!!.getTexture(), x + curValue(-7.0f, -7.0f, progress) - curValue(16f, 16f, progress) / 2, y - curValue(-16.5f, -16.5f, progress) - curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress) / 2, curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress), curValue(40f, 40f, progress), curValue(0.86f, 0.86f, progress), curValue(0.8944f, 0.8944f, progress), curValue(-0.8069294f, -0.8069294f, progress), 0, 0, 16, 40, false, false)
                        batch.draw(textures["legtop"]!!.getTexture(), x + curValue(-6.6666665f, -6.6666665f, progress) - curValue(16f, 16f, progress) / 2, y - curValue(-43.166668f, -43.166668f, progress) - curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress) / 2, curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress), curValue(40f, 40f, progress), curValue(0.88f, 0.88f, progress), curValue(0.8712f, 0.8712f, progress), curValue(-1.0912162f, -1.0912162f, progress), 0, 0, 16, 40, false, false)
                    }
                    MoveDirection.DOWN_RIGHT -> {
                        val progress = 1f
                        batch.draw(textures["legtop"]!!.getTexture(), x + curValue(4.833333f, 4.833333f, progress) - curValue(16f, 16f, progress) / 2, y - curValue(-44.333332f, -44.333332f, progress) - curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress) / 2, curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress), curValue(40f, 40f, progress), curValue(0.8536f, 0.8536f, progress), curValue(0.88f, 0.88f, progress), curValue(2.726311f, 2.726311f, progress), 0, 0, 16, 40, false, false)
                        batch.draw(textures["legbottom"]!!.getTexture(), x + curValue(6.6666665f, 6.6666665f, progress) - curValue(16f, 16f, progress) / 2, y - curValue(-17.333334f, -17.333334f, progress) - curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress) / 2, curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress), curValue(40f, 40f, progress), curValue(0.8439f, 0.8439f, progress), curValue(0.87f, 0.87f, progress), curValue(2.7638633f, 2.7638633f, progress), 0, 0, 16, 40, false, false)
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
                        batch.draw(textures["legtop"]!!.getTexture(), x + curValue(0.33333334f, 0.33333334f, progress) - curValue(16f, 16f, progress) / 2, y - curValue(-48.5f, -48.5f, progress) - curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress) / 2, curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress), curValue(40f, 40f, progress), curValue(0.9504f, 0.9504f, progress), curValue(0.8712f, 0.8712f, progress), curValue(0.97518706f, 0.97518706f, progress), 0, 0, 16, 40, false, false)
                        batch.draw(textures["legbottom"]!!.getTexture(), x + curValue(0.5f, 0.5f, progress) - curValue(16f, 16f, progress) / 2, y - curValue(-17.5f, -17.5f, progress) - curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress) / 2, curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress), curValue(40f, 40f, progress), curValue(0.98f, 0.98f, progress), curValue(0.9506f, 0.9506f, progress), curValue(0.57294357f, 0.57294357f, progress), 0, 0, 16, 40, false, false)
                    }
                    MoveDirection.UP_RIGHT -> {
                        val progress = 1f
                        batch.draw(textures["legbottom"]!!.getTexture(), x + curValue(7.6666665f, 7.6666665f, progress) - curValue(16f, 16f, progress) / 2, y - curValue(-13.333334f, -13.333334f, progress) - curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress) / 2, curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress), curValue(40f, 40f, progress), curValue(0.94f, 0.94f, progress), curValue(0.94f, 0.94f, progress), curValue(2.385949f, 2.385949f, progress), 0, 0, 16, 40, false, false)
                        batch.draw(textures["legtop"]!!.getTexture(), x + curValue(7.0f, 7.0f, progress) - curValue(16f, 16f, progress) / 2, y - curValue(-41.166668f, -41.166668f, progress) - curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress) / 2, curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress), curValue(40f, 40f, progress), curValue(0.93f, 0.93f, progress), curValue(0.8742f, 0.8742f, progress), curValue(2.726311f, 2.726311f, progress), 0, 0, 16, 40, false, false)
                    }
                    MoveDirection.UP -> {
                        val progress = 1f
                        batch.draw(textures["legbottom"]!!.getTexture(), x + curValue(-6.166667f, -6.166667f, progress) - curValue(16f, 16f, progress) / 2, y - curValue(-15.0f, -15.0f, progress) - curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress) / 2, curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress), curValue(40f, 40f, progress), curValue(0.86f, 0.86f, progress), curValue(0.86f, 0.86f, progress), curValue(0.0f, 0.0f, progress), 0, 0, 16, 40, false, false)
                        batch.draw(textures["legtop"]!!.getTexture(), x + curValue(-5.8333335f, -5.8333335f, progress) - curValue(16f, 16f, progress) / 2, y - curValue(-42.666668f, -42.666668f, progress) - curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress) / 2, curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress), curValue(40f, 40f, progress), curValue(0.88f, 0.88f, progress), curValue(0.88f, 0.88f, progress), curValue(0.0f, 0.0f, progress), 0, 0, 16, 40, false, false)
                    }
                    MoveDirection.UP_LEFT -> {
                        val progress = 1f
                        batch.draw(textures["legbottom"]!!.getTexture(), x + curValue(7.5f, 7.5f, progress) - curValue(16f, 16f, progress) / 2, y - curValue(-16.166666f, -16.166666f, progress) - curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress) / 2, curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress), curValue(40f, 40f, progress), curValue(0.9f, 0.9f, progress), curValue(0.9f, 0.9f, progress), curValue(1.6683375f, 1.6683375f, progress), 0, 0, 16, 40, false, false)
                        batch.draw(textures["legtop"]!!.getTexture(), x + curValue(6.5f, 6.5f, progress) - curValue(16f, 16f, progress) / 2, y - curValue(-42.5f, -42.5f, progress) - curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress) / 2, curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress), curValue(40f, 40f, progress), curValue(0.9f, 0.9f, progress), curValue(0.828f, 0.828f, progress), curValue(1.7357047f, 1.7357047f, progress), 0, 0, 16, 40, false, false)
                    }
                    MoveDirection.LEFT -> {
                        val progress = 1f
                        batch.draw(textures["legtop"]!!.getTexture(), x + curValue(-0.33333334f, -0.33333334f, progress) - curValue(16f, 16f, progress) / 2, y - curValue(-48.5f, -48.5f, progress) - curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress) / 2, curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress), curValue(40f, 40f, progress), curValue(0.9504f, 0.9504f, progress), curValue(0.8712f, 0.8712f, progress), curValue(-0.97518706f, -0.97518706f, progress), 0, 0, 16, 40, false, false)
                        batch.draw(textures["legbottom"]!!.getTexture(), x + curValue(-0.5f, -0.5f, progress) - curValue(16f, 16f, progress) / 2, y - curValue(-17.5f, -17.5f, progress) - curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress) / 2, curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress), curValue(40f, 40f, progress), curValue(0.98f, 0.98f, progress), curValue(0.9506f, 0.9506f, progress), curValue(-0.57294357f, -0.57294357f, progress), 0, 0, 16, 40, false, false)
                    }
                    MoveDirection.DOWN_LEFT -> {
                        val progress = 1f
                        batch.draw(textures["legtop"]!!.getTexture(), x + curValue(-4.833333f, -4.833333f, progress) - curValue(16f, 16f, progress) / 2, y - curValue(-44.333332f, -44.333332f, progress) - curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress) / 2, curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress), curValue(40f, 40f, progress), curValue(0.8536f, 0.8536f, progress), curValue(0.88f, 0.88f, progress), curValue(-2.726311f, -2.726311f, progress), 0, 0, 16, 40, false, false)
                        batch.draw(textures["legbottom"]!!.getTexture(), x + curValue(-6.6666665f, -6.6666665f, progress) - curValue(16f, 16f, progress) / 2, y - curValue(-17.333334f, -17.333334f, progress) - curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress) / 2, curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress), curValue(40f, 40f, progress), curValue(0.8439f, 0.8439f, progress), curValue(0.87f, 0.87f, progress), curValue(-2.7638633f, -2.7638633f, progress), 0, 0, 16, 40, false, false)
                    }
                    MoveDirection.DOWN -> {
                        val progress = 1f
                        batch.draw(textures["legbottom"]!!.getTexture(), x + curValue(6.166667f, 6.166667f, progress) - curValue(16f, 16f, progress) / 2, y - curValue(-15.0f, -15.0f, progress) - curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress) / 2, curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress), curValue(40f, 40f, progress), curValue(0.86f, 0.86f, progress), curValue(0.86f, 0.86f, progress), curValue(-0.0f, -0.0f, progress), 0, 0, 16, 40, false, false)
                        batch.draw(textures["legtop"]!!.getTexture(), x + curValue(5.8333335f, 5.8333335f, progress) - curValue(16f, 16f, progress) / 2, y - curValue(-42.666668f, -42.666668f, progress) - curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress) / 2, curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress), curValue(40f, 40f, progress), curValue(0.88f, 0.88f, progress), curValue(0.88f, 0.88f, progress), curValue(1.0912184f, 1.0912184f, progress), 0, 0, 16, 40, false, false)
                    }
                    MoveDirection.DOWN_RIGHT -> {
                        val progress = 1f
                        batch.draw(textures["legtop"]!!.getTexture(), x + curValue(-5.6666665f, -5.6666665f, progress) - curValue(16f, 16f, progress) / 2, y - curValue(-43.166668f, -43.166668f, progress) - curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress) / 2, curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress), curValue(40f, 40f, progress), curValue(0.93f, 0.93f, progress), curValue(0.93f, 0.93f, progress), curValue(-2.0700305f, -2.0700305f, progress), 0, 0, 16, 40, false, false)
                        batch.draw(textures["legbottom"]!!.getTexture(), x + curValue(-7.3333335f, -7.3333335f, progress) - curValue(16f, 16f, progress) / 2, y - curValue(-14.666666f, -14.666666f, progress) - curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress) / 2, curValue(40f, 40f, progress) / 2, curValue(16f, 16f, progress), curValue(40f, 40f, progress), curValue(0.93f, 0.93f, progress), curValue(0.93f, 0.93f, progress), curValue(-2.8624053f, -2.8624053f, progress), 0, 0, 16, 40, false, false)
                    }
                }
            }
        }
    }
}
