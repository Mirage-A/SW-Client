package com.mirage.ui.game

import com.badlogic.gdx.Input
import com.mirage.utils.datastructures.Point
import com.mirage.utils.game.oldobjects.GameObject
import com.mirage.utils.messaging.ChangeSceneClientMessage
import com.mirage.utils.messaging.ClientMessage
import com.mirage.utils.messaging.EventSubjectAdapter
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
        uiState.microMenuBtnList.forEach {it.touchUp(virtualPoint)}
        uiState.settingsMenuBtnList.forEach {it.touchUp(virtualPoint)}
        uiState.skillBtns.forEach {it.touchUp(virtualPoint)}
        uiState.confirmExitMessage.touchUp(virtualPoint)
        return false
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        val virtualPoint = getVirtualPoint(screenX, screenY)
        uiState.microMenuBtnList.forEach {it.touchDown(virtualPoint)}
        uiState.settingsMenuBtnList.forEach {it.touchDown(virtualPoint)}
        uiState.skillBtns.forEach {it.touchDown(virtualPoint)}
        uiState.confirmExitMessage.touchDown(virtualPoint)
        return false
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        val virtualPoint = getVirtualPoint(screenX, screenY)
        uiState.microMenuBtnList.forEach {it.mouseMoved(virtualPoint)}
        uiState.settingsMenuBtnList.forEach {it.mouseMoved(virtualPoint)}
        uiState.skillBtns.forEach {it.mouseMoved(virtualPoint)}
        uiState.confirmExitMessage.mouseMoved(virtualPoint)
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
                        GameObject.MoveDirection.UP_LEFT -> {
                            startMoving(GameObject.MoveDirection.LEFT)
                        }
                        GameObject.MoveDirection.UP_RIGHT -> {
                            startMoving(GameObject.MoveDirection.RIGHT)
                        }
                        GameObject.MoveDirection.UP -> {
                            if (aReleasedTime >= dReleasedTime && wReleasedTime - aReleasedTime < epsTime) {
                                setMoveDirection(GameObject.MoveDirection.UP_LEFT)
                            } else if (dReleasedTime >= aReleasedTime && wReleasedTime - dReleasedTime < epsTime) {
                                setMoveDirection(GameObject.MoveDirection.UP_RIGHT)
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
                        GameObject.MoveDirection.UP_LEFT -> {
                            startMoving(GameObject.MoveDirection.UP)
                        }
                        GameObject.MoveDirection.DOWN_LEFT -> {
                            startMoving(GameObject.MoveDirection.DOWN)
                        }
                        GameObject.MoveDirection.LEFT -> {
                            if (wReleasedTime >= sReleasedTime && aReleasedTime - wReleasedTime < epsTime) {
                                setMoveDirection(GameObject.MoveDirection.UP_LEFT)
                            } else if (sReleasedTime >= wReleasedTime && aReleasedTime - sReleasedTime < epsTime) {
                                setMoveDirection(GameObject.MoveDirection.DOWN_LEFT)
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
                        GameObject.MoveDirection.DOWN_LEFT -> {
                            startMoving(GameObject.MoveDirection.LEFT)
                        }
                        GameObject.MoveDirection.DOWN_RIGHT -> {
                            startMoving(GameObject.MoveDirection.RIGHT)
                        }
                        GameObject.MoveDirection.DOWN -> {
                            if (aReleasedTime >= dReleasedTime && sReleasedTime - aReleasedTime < epsTime) {
                                setMoveDirection(GameObject.MoveDirection.DOWN_LEFT)
                            } else if (dReleasedTime >= aReleasedTime && sReleasedTime - dReleasedTime < epsTime) {
                                setMoveDirection(GameObject.MoveDirection.DOWN_RIGHT)
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
                        GameObject.MoveDirection.UP_RIGHT -> {
                            startMoving(GameObject.MoveDirection.UP)
                        }
                        GameObject.MoveDirection.DOWN_RIGHT -> {
                            startMoving(GameObject.MoveDirection.DOWN)
                        }
                        GameObject.MoveDirection.RIGHT -> {
                            if (wReleasedTime >= sReleasedTime && dReleasedTime - wReleasedTime < epsTime) {
                                setMoveDirection(GameObject.MoveDirection.UP_RIGHT)
                            } else if (sReleasedTime >= wReleasedTime && dReleasedTime - sReleasedTime < epsTime) {
                                setMoveDirection(GameObject.MoveDirection.DOWN_RIGHT)
                            }
                            stopMoving()
                        }
                        else -> {
                        }
                    }
                }
            }
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
                        GameObject.MoveDirection.LEFT -> {
                            startMoving(GameObject.MoveDirection.UP_LEFT)
                        }
                        GameObject.MoveDirection.RIGHT -> {
                            startMoving(GameObject.MoveDirection.UP_RIGHT)
                        }
                        else -> {
                            startMoving(GameObject.MoveDirection.UP)
                        }
                    }
                } else {
                    startMoving(GameObject.MoveDirection.UP)
                }
            }
            Input.Keys.A -> {
                if (uiState.bufferedMoving == true) {
                    when (uiState.bufferedMoveDirection) {
                        GameObject.MoveDirection.UP -> {
                            startMoving(GameObject.MoveDirection.UP_LEFT)
                        }
                        GameObject.MoveDirection.DOWN -> {
                            startMoving(GameObject.MoveDirection.DOWN_LEFT)
                        }
                        else -> {
                            startMoving(GameObject.MoveDirection.LEFT)
                        }
                    }
                } else {
                    startMoving(GameObject.MoveDirection.LEFT)
                }
            }
            Input.Keys.S -> {
                if (uiState.bufferedMoving == true) {
                    when (uiState.bufferedMoveDirection) {
                        GameObject.MoveDirection.LEFT -> {
                            startMoving(GameObject.MoveDirection.DOWN_LEFT)
                        }
                        GameObject.MoveDirection.RIGHT -> {
                            startMoving(GameObject.MoveDirection.DOWN_RIGHT)
                        }
                        else -> {
                            startMoving(GameObject.MoveDirection.DOWN)
                        }
                    }
                } else {
                    startMoving(GameObject.MoveDirection.DOWN)
                }
            }
            Input.Keys.D -> {
                if (uiState.bufferedMoving == true) {
                    when (uiState.bufferedMoveDirection) {
                        GameObject.MoveDirection.UP -> {
                            startMoving(GameObject.MoveDirection.UP_RIGHT)
                        }
                        GameObject.MoveDirection.DOWN -> {
                            startMoving(GameObject.MoveDirection.DOWN_RIGHT)
                        }
                        else -> {
                            startMoving(GameObject.MoveDirection.RIGHT)
                        }
                    }
                } else {
                    startMoving(GameObject.MoveDirection.RIGHT)
                }
            }
            Input.Keys.ESCAPE -> {
                inputMessages.onNext(ChangeSceneClientMessage(ChangeSceneClientMessage.Scene.MAIN_MENU))
            }
        }
        return true
    }


    private fun startMoving(md: GameObject.MoveDirection) {
        uiState.bufferedMoveDirection = md
        uiState.bufferedMoving = true
    }

    private fun stopMoving() {
        uiState.bufferedMoving = false
    }

    private fun setMoveDirection(md: GameObject.MoveDirection) {
        uiState.bufferedMoveDirection = md
    }


    private fun getVirtualPoint(screenX: Int, screenY: Int) =
            uiState.virtualScreen.projectRealPointOnVirtualScreen(Point(screenX.toFloat(), screenY.toFloat()))

}