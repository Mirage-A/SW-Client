package com.mirage.ui.game

import com.badlogic.gdx.Input
import com.mirage.utils.datastructures.Point
import com.mirage.utils.game.objects.properties.MoveDirection
import com.mirage.utils.messaging.*
import com.mirage.utils.preferences.Prefs
import rx.subjects.Subject

class DesktopGameInputProcessor(private val uiState: GameUIState) : GameInputProcessor {

    override val inputMessages: Subject<ClientMessage, ClientMessage> = EventSubjectAdapter()


    init {
        uiState.settingsBtn.onPressed = {
            for (btn in uiState.settingsMenuBtnList) {
                btn.isVisible = !btn.isVisible
            }
        }
        uiState.leaveGameBtn.onPressed = {
            uiState.confirmExitMessage.isVisible = true
        }
        uiState.confirmExitMessage.setOkAction {
            inputMessages.onNext(ChangeSceneClientMessage(ChangeSceneClientMessage.Scene.MAIN_MENU))
            uiState.confirmExitMessage.isVisible = false
        }
        uiState.confirmExitMessage.setCancelAction {
            uiState.confirmExitMessage.isVisible = false
        }
    }

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
        uiState.widgets.forEach { if (it.touchUp(virtualPoint)) return true}
        return false
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        val virtualPoint = getVirtualPoint(screenX, screenY)
        uiState.widgets.forEach { if (it.touchDown(virtualPoint)) return true }
        inputMessages.onNext(NewTargetMessage(virtualPoint))
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
                uiState.skillBtns[i].keyPressed = false
            }
        }
        if (keycode == Prefs.settings.ultimateSkillBinding.get()) {
            uiState.skillBtns[4].keyPressed = false
        }
        return true
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        //println("touchDragged $screenX $screenY $pointer")
        return true
    }


    override fun keyDown(keycode: Int): Boolean {
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
            }
        }
        for (i in 0 until 4) {
            if (keycode == Prefs.settings.activeSkillBindings[i]) {
                inputMessages.onNext(CastSkillClientMessage(i, uiState.targetID))
                uiState.skillBtns[i].keyPressed = true
            }
        }
        if (keycode == Prefs.settings.ultimateSkillBinding.get()) {
            inputMessages.onNext(CastSkillClientMessage(4, uiState.targetID))
            uiState.skillBtns[4].keyPressed = true
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