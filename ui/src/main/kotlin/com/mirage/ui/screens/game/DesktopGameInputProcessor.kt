package com.mirage.ui.screens.game

import com.badlogic.gdx.Input
import com.mirage.ui.screens.game.quests.questBtnCount
import com.mirage.core.datastructures.Point
import com.mirage.core.datastructures.rangeBetween
import com.mirage.core.game.objects.properties.MoveDirection
import com.mirage.core.messaging.*
import com.mirage.core.preferences.Prefs
import rx.subjects.Subject

internal class DesktopGameInputProcessor(private val uiState: GameUIState) : GameInputProcessor {

    override val inputMessages: Subject<ClientMessage, ClientMessage> = EventSubjectAdapter()


    /**
     * Время отпускания клавиши передвижения
     */
    private var wReleasedTime = 0L
    private var aReleasedTime = 0L
    private var sReleasedTime = 0L
    private var dReleasedTime = 0L

    /**
     * Интервал времени, за который должны быть отпущены две соседние клавиши передвижения,
     * чтобы персонаж остался в диагональном направлении движения
     */
    private val epsTime = 50L

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        val virtualPoint = getVirtualPoint(screenX, screenY)
        uiState.widgets.forEach { it.unpress() }
        uiState.widgets.forEach { if (it.touchUp(virtualPoint)) return true}
        return false
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        val virtualPoint = getVirtualPoint(screenX, screenY)
        uiState.widgets.forEach { if (it.touchDown(virtualPoint)) return true }
        if (!uiState.gameOver) inputMessages.onNext(NewTargetMessage(virtualPoint))
        return false
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        val virtualPoint = getVirtualPoint(screenX, screenY)
        uiState.widgets.forEach { it.mouseMoved(virtualPoint)}
        return false
    }

    override fun keyTyped(character: Char): Boolean {
        //println("keyTyped $character")
        return true
    }

    override fun scrolled(amount: Int): Boolean {
        //println("scrolled $amount")
        return true
    }

    override fun keyUp(keycode: Int): Boolean {
        if (uiState.gameOver) return false
        when (keycode) {
            Input.Keys.W -> {
                wReleasedTime = System.currentTimeMillis()
                if (uiState.bufferedMoving == true) {
                    when (uiState.bufferedMoveDirection) {
                        MoveDirection.UP_LEFT -> {
                            startMoving(MoveDirection.LEFT)
                        }
                        MoveDirection.UP_RIGHT -> {
                            startMoving(MoveDirection.RIGHT)
                        }
                        MoveDirection.UP -> {
                            if (aReleasedTime >= dReleasedTime && wReleasedTime - aReleasedTime < epsTime) {
                                setMoveDirection(MoveDirection.UP_LEFT)
                            } else if (dReleasedTime >= aReleasedTime && wReleasedTime - dReleasedTime < epsTime) {
                                setMoveDirection(MoveDirection.UP_RIGHT)
                            }
                            stopMoving()
                        }
                        else -> {
                        }
                    }
                }
            }
            Input.Keys.A -> {
                aReleasedTime = System.currentTimeMillis()
                if (uiState.bufferedMoving == true) {
                    when (uiState.bufferedMoveDirection) {
                        MoveDirection.UP_LEFT -> {
                            startMoving(MoveDirection.UP)
                        }
                        MoveDirection.DOWN_LEFT -> {
                            startMoving(MoveDirection.DOWN)
                        }
                        MoveDirection.LEFT -> {
                            if (wReleasedTime >= sReleasedTime && aReleasedTime - wReleasedTime < epsTime) {
                                setMoveDirection(MoveDirection.UP_LEFT)
                            } else if (sReleasedTime >= wReleasedTime && aReleasedTime - sReleasedTime < epsTime) {
                                setMoveDirection(MoveDirection.DOWN_LEFT)
                            }
                            stopMoving()
                        }
                        else -> {
                        }
                    }
                }
            }
            Input.Keys.S -> {
                sReleasedTime = System.currentTimeMillis()
                if (uiState.bufferedMoving == true) {
                    when (uiState.bufferedMoveDirection) {
                        MoveDirection.DOWN_LEFT -> {
                            startMoving(MoveDirection.LEFT)
                        }
                        MoveDirection.DOWN_RIGHT -> {
                            startMoving(MoveDirection.RIGHT)
                        }
                        MoveDirection.DOWN -> {
                            if (aReleasedTime >= dReleasedTime && sReleasedTime - aReleasedTime < epsTime) {
                                setMoveDirection(MoveDirection.DOWN_LEFT)
                            } else if (dReleasedTime >= aReleasedTime && sReleasedTime - dReleasedTime < epsTime) {
                                setMoveDirection(MoveDirection.DOWN_RIGHT)
                            }
                            stopMoving()
                        }
                        else -> {
                        }
                    }
                }
            }
            Input.Keys.D -> {
                dReleasedTime = System.currentTimeMillis()
                if (uiState.bufferedMoving == true) {
                    when (uiState.bufferedMoveDirection) {
                        MoveDirection.UP_RIGHT -> {
                            startMoving(MoveDirection.UP)
                        }
                        MoveDirection.DOWN_RIGHT -> {
                            startMoving(MoveDirection.DOWN)
                        }
                        MoveDirection.RIGHT -> {
                            if (wReleasedTime >= sReleasedTime && dReleasedTime - wReleasedTime < epsTime) {
                                setMoveDirection(MoveDirection.UP_RIGHT)
                            } else if (sReleasedTime >= wReleasedTime && dReleasedTime - sReleasedTime < epsTime) {
                                setMoveDirection(MoveDirection.DOWN_RIGHT)
                            }
                            stopMoving()
                        }
                        else -> {
                        }
                    }
                }
            }
        }
        for (i in 0 until 4) {
            if (keycode == Prefs.settings.activeSkillBindings[i]) {
                uiState.activeSkills[i].keyPressed = false
            }
        }
        if (keycode == Prefs.settings.ultimateSkillBinding.get()) {
            uiState.ultimateSkillBtn.keyPressed = false
        }
        return true
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        //println("touchDragged $screenX $screenY $pointer")
        return true
    }


    override fun keyDown(keycode: Int): Boolean {
        if (uiState.gameOver) return false
        when (keycode) {
            Input.Keys.W -> {
                if (uiState.bufferedMoving == true) {
                    when (uiState.bufferedMoveDirection) {
                        MoveDirection.LEFT -> {
                            startMoving(MoveDirection.UP_LEFT)
                        }
                        MoveDirection.RIGHT -> {
                            startMoving(MoveDirection.UP_RIGHT)
                        }
                        else -> {
                            startMoving(MoveDirection.UP)
                        }
                    }
                } else {
                    startMoving(MoveDirection.UP)
                }
            }
            Input.Keys.A -> {
                if (uiState.bufferedMoving == true) {
                    when (uiState.bufferedMoveDirection) {
                        MoveDirection.UP -> {
                            startMoving(MoveDirection.UP_LEFT)
                        }
                        MoveDirection.DOWN -> {
                            startMoving(MoveDirection.DOWN_LEFT)
                        }
                        else -> {
                            startMoving(MoveDirection.LEFT)
                        }
                    }
                } else {
                    startMoving(MoveDirection.LEFT)
                }
            }
            Input.Keys.S -> {
                if (uiState.bufferedMoving == true) {
                    when (uiState.bufferedMoveDirection) {
                        MoveDirection.LEFT -> {
                            startMoving(MoveDirection.DOWN_LEFT)
                        }
                        MoveDirection.RIGHT -> {
                            startMoving(MoveDirection.DOWN_RIGHT)
                        }
                        else -> {
                            startMoving(MoveDirection.DOWN)
                        }
                    }
                } else {
                    startMoving(MoveDirection.DOWN)
                }
            }
            Input.Keys.D -> {
                if (uiState.bufferedMoving == true) {
                    when (uiState.bufferedMoveDirection) {
                        MoveDirection.UP -> {
                            startMoving(MoveDirection.UP_RIGHT)
                        }
                        MoveDirection.DOWN -> {
                            startMoving(MoveDirection.DOWN_RIGHT)
                        }
                        else -> {
                            startMoving(MoveDirection.RIGHT)
                        }
                    }
                } else {
                    startMoving(MoveDirection.RIGHT)
                }
            }
            Input.Keys.ESCAPE -> {
                inputMessages.onNext(ClearTargetMessage())
                inputMessages.onNext(SetTargetClientMessage(null))
            }
            Input.Keys.E -> {
                val targetID = uiState.targetID
                val player = uiState.lastRenderedState.entities[uiState.playerID]
                val target = uiState.lastRenderedState.entities[targetID]
                if (targetID != null && player != null && target != null &&
                        rangeBetween(player.position, target.position) < target.interactionRange) {
                    inputMessages.onNext(InteractionClientMessage(targetID))
                }
            }
        }
        for (i in 0 until 4) {
            if (keycode == Prefs.settings.activeSkillBindings[i]) {
                inputMessages.onNext(CastSkillClientMessage(i, uiState.targetID))
                uiState.activeSkills[i].keyPressed = true
            }
        }
        if (keycode == Prefs.settings.ultimateSkillBinding.get()) {
            inputMessages.onNext(CastSkillClientMessage(4, uiState.targetID))
            uiState.ultimateSkillBtn.keyPressed = true
        }
        return true
    }


    private fun startMoving(md: MoveDirection) {
        uiState.bufferedMoveDirection = md
        uiState.bufferedMoving = true
    }

    private fun stopMoving() {
        uiState.bufferedMoving = false
    }

    private fun setMoveDirection(md: MoveDirection) {
        uiState.bufferedMoveDirection = md
    }


    private fun getVirtualPoint(screenX: Int, screenY: Int) =
            uiState.virtualScreen.projectRealPointOnVirtualScreen(Point(screenX.toFloat(), screenY.toFloat()))

}