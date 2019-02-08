package com.mirage.view

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.BitmapFont

class LoadingView : View() {

    protected val loadingFont = BitmapFont()

    init {
        loadingFont.data.setScale(5f)
    }

    override fun render() {
        if (lastRealScreenWidth != Gdx.graphics.width.toFloat() || lastRealScreenHeight != Gdx.graphics.height.toFloat()) {
            setScreenSize(Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        }
        Gdx.gl.glClearColor(0.25f, 0.25f, 1f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        camera.update()
        batch.projectionMatrix = camera.combined
        batch.begin()
        loadingFont.draw(batch, "Loading....", DEFAULT_SCREEN_WIDTH / 2 - 140, DEFAULT_SCREEN_HEIGHT * 1 / 3)
        batch.end()
    }

    override fun dispose() {
        //TODO
    }

    /**
     * Метод, который должен вызываться при изменении параметров реального экрана.
     * @param realWidth Ширина реального экрана
     * @param realHeight Высота реального экрана
     */
    override fun setScreenSize(realWidth: Float, realHeight: Float) {
        super.setScreenSize(realWidth, realHeight)
        camera.setToOrtho(false, DEFAULT_SCREEN_WIDTH, DEFAULT_SCREEN_HEIGHT)
    }
}