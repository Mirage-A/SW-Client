package com.mirage.controller;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.mirage.model.ModelFacade;
import com.mirage.view.View;

import java.io.File;

public class Controller extends ApplicationAdapter {
    private View view;
    private ModelFacade model;

    @Override
    public void create() {
        model = new ModelFacade();
        model.loadMapFromFile(new File(""));
        model.startGame();
        model.startLogic();
        view = new View(model);

        model.startMoving((float) Math.PI / 4);

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
}