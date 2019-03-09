package com.mirage.view.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch

class LoadingScreen : ScreenAdapter() {

    private val batch: SpriteBatch = SpriteBatch()
    private var camera: OrthographicCamera = OrthographicCamera()

    private val loadingFont = BitmapFont()

    var loadingText = "Loading...."

    init {
        loadingFont.data.setScale(5f)
    }

    override fun resize(width: Int, height: Int) {
        camera.setToOrtho(false, width.toFloat(), height.toFloat())
    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0.25f, 0.25f, 1f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        camera.update()
        batch.projectionMatrix = camera.combined
        batch.begin()
        loadingFont.draw(batch, loadingText, camera.viewportWidth / 2 - 140, camera.viewportHeight * 1 / 3)
        batch.end()
    }
}