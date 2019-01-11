package com.mirage.controller;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector3;
import com.mirage.model.ModelFacade;
import com.mirage.view.View;
import com.mirage.view.animation.MoveDirection;

import java.io.File;

public class Controller extends ApplicationAdapter implements InputProcessor {
    private View view;
    private ModelFacade model;

    @Override
    public void create() {
        model = new ModelFacade();
        model.loadMapFromFile(new File(""));
        model.startGame();
        model.startLogic();
        view = new View(model);

        Gdx.input.setInputProcessor(this);

        /*Sound dropSound = Gdx.audio.newSound(Gdx.files.internal("android/assets/drop.wav"));
        Music rainMusic = Gdx.audio.newMusic(Gdx.files.internal("android/assets/rain.mp3"));
        rainMusic.setLooping(true);
        rainMusic.play();*/
    }

    @Override
    public void render() {
        float deltaTime = Gdx.graphics.getDeltaTime();
        // long time = TimeUtils.nanoTime()
        // rectangle.overlaps(rectangle) - коллизии
        // process user input
        if(Gdx.input.isTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            view.camera.unproject(touchPos); //Этот метод переводит координаты на экране в координаты игры
            // (без учета всяких передвижений камеры). Нам этот метод не нужен, т.к. мы обрабатываем UI
        }

        view.render();
    }

    @Override
    public void dispose() {
        view.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.W: {
                if (model.isPlayerMoving()) {
                    switch (model.getPlayerMoveDirection()) {
                        case LEFT: {
                            model.startMoving(MoveDirection.UP_LEFT.toAngle());
                            break;
                        }
                        case RIGHT: {
                            model.startMoving(MoveDirection.UP_RIGHT.toAngle());
                            break;
                        }
                        default: {
                            model.startMoving(MoveDirection.UP.toAngle());
                            break;
                        }
                    }
                }
                else {
                    model.startMoving(MoveDirection.UP.toAngle());
                }
                break;
            }
            case Input.Keys.A: {
                if (model.isPlayerMoving()) {
                    switch (model.getPlayerMoveDirection()) {
                        case UP: {
                            model.startMoving(MoveDirection.UP_LEFT.toAngle());
                            break;
                        }
                        case DOWN: {
                            model.startMoving(MoveDirection.DOWN_LEFT.toAngle());
                            break;
                        }
                        default: {
                            model.startMoving(MoveDirection.LEFT.toAngle());
                            break;
                        }
                    }
                }
                else {
                    model.startMoving(MoveDirection.LEFT.toAngle());
                }
                break;
            }
            case Input.Keys.S: {
                if (model.isPlayerMoving()) {
                    switch (model.getPlayerMoveDirection()) {
                        case LEFT: {
                            model.startMoving(MoveDirection.DOWN_LEFT.toAngle());
                            break;
                        }
                        case RIGHT: {
                            model.startMoving(MoveDirection.DOWN_RIGHT.toAngle());
                            break;
                        }
                        default: {
                            model.startMoving(MoveDirection.DOWN.toAngle());
                            break;
                        }
                    }
                }
                else {
                    model.startMoving(MoveDirection.DOWN.toAngle());
                }
                break;
            }
            case Input.Keys.D: {
                if (model.isPlayerMoving()) {
                    switch (model.getPlayerMoveDirection()) {
                        case UP: {
                            model.startMoving(MoveDirection.UP_RIGHT.toAngle());
                            break;
                        }
                        case DOWN: {
                            model.startMoving(MoveDirection.DOWN_RIGHT.toAngle());
                            break;
                        }
                        default: {
                            model.startMoving(MoveDirection.RIGHT.toAngle());
                            break;
                        }
                    }
                }
                else {
                    model.startMoving(MoveDirection.RIGHT.toAngle());
                }
                break;
            }
        }
        return false;
    }

    /**
     * Время отпускания клавиши передвижения
     */
    private long wReleasedTime = 0L;
    private long aReleasedTime = 0L;
    private long sReleasedTime = 0L;
    private long dReleasedTime = 0L;
    /**
     * Интервал времени, за который должны быть отпущены две соседние клавиши передвижения,
     * чтобы персонаж остался в диагональном направлении движения
     */
    private final long EPS_TIME = 50L;

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.W: {
                wReleasedTime = System.currentTimeMillis();
                if (model.isPlayerMoving()) {
                    switch (model.getPlayerMoveDirection()) {
                        case UP_LEFT: {
                            model.startMoving(MoveDirection.LEFT.toAngle());
                            break;
                        }
                        case UP_RIGHT: {
                            model.startMoving(MoveDirection.RIGHT.toAngle());
                            break;
                        }
                        case UP: {
                            if (aReleasedTime >= dReleasedTime && wReleasedTime - aReleasedTime < EPS_TIME) {
                                model.setMoveAngle(MoveDirection.UP_LEFT.toAngle());
                            }
                            else if (dReleasedTime >= aReleasedTime && wReleasedTime - dReleasedTime < EPS_TIME) {
                                model.setMoveAngle(MoveDirection.UP_RIGHT.toAngle());
                            }
                            model.stopMoving();
                            break;
                        }
                    }
                }
                break;
            }
            case Input.Keys.A: {
                aReleasedTime = System.currentTimeMillis();
                if (model.isPlayerMoving()) {
                    switch (model.getPlayerMoveDirection()) {
                        case UP_LEFT: {
                            model.startMoving(MoveDirection.UP.toAngle());
                            break;
                        }
                        case DOWN_LEFT: {
                            model.startMoving(MoveDirection.DOWN.toAngle());
                            break;
                        }
                        case LEFT: {
                            if (wReleasedTime >= sReleasedTime && aReleasedTime - wReleasedTime < EPS_TIME) {
                                model.setMoveAngle(MoveDirection.UP_LEFT.toAngle());
                            }
                            else if (sReleasedTime >= wReleasedTime && aReleasedTime - sReleasedTime < EPS_TIME) {
                                model.setMoveAngle(MoveDirection.DOWN_LEFT.toAngle());
                            }
                            model.stopMoving();
                            break;
                        }
                    }
                }
                break;
            }
            case Input.Keys.S: {
                sReleasedTime = System.currentTimeMillis();
                if (model.isPlayerMoving()) {
                    switch (model.getPlayerMoveDirection()) {
                        case DOWN_LEFT: {
                            model.startMoving(MoveDirection.LEFT.toAngle());
                            break;
                        }
                        case DOWN_RIGHT: {
                            model.startMoving(MoveDirection.RIGHT.toAngle());
                            break;
                        }
                        case DOWN: {
                            if (aReleasedTime >= dReleasedTime && sReleasedTime - aReleasedTime < EPS_TIME) {
                                model.setMoveAngle(MoveDirection.DOWN_LEFT.toAngle());
                            }
                            else if (dReleasedTime >= aReleasedTime && sReleasedTime - dReleasedTime < EPS_TIME) {
                                model.setMoveAngle(MoveDirection.DOWN_RIGHT.toAngle());
                            }
                            model.stopMoving();
                            break;
                        }
                    }
                }
                break;
            }
            case Input.Keys.D: {
                dReleasedTime = System.currentTimeMillis();
                if (model.isPlayerMoving()) {
                    switch (model.getPlayerMoveDirection()) {
                        case UP_RIGHT: {
                            model.startMoving(MoveDirection.UP.toAngle());
                            break;
                        }
                        case DOWN_RIGHT: {
                            model.startMoving(MoveDirection.DOWN.toAngle());
                            break;
                        }
                        case RIGHT: {
                            if (wReleasedTime >= sReleasedTime && dReleasedTime - wReleasedTime < EPS_TIME) {
                                model.setMoveAngle(MoveDirection.UP_RIGHT.toAngle());
                            }
                            else if (sReleasedTime >= wReleasedTime && dReleasedTime - sReleasedTime < EPS_TIME) {
                                model.setMoveAngle(MoveDirection.DOWN_RIGHT.toAngle());
                            }
                            model.stopMoving();
                            break;
                        }
                    }
                }
                break;
            }
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}