package com.mirage.view.old

/*
/**
 * Экран главного меню
 */
class MainMenuScreen : ScreenAdapter() {

    private val batch: SpriteBatch = SpriteBatch()
    private var camera: OrthographicCamera = OrthographicCamera()

    private val loadingFont = BitmapFont()

    var loadingText = "PRESS ANY KEY"

    init {
        loadingFont.data.setScale(4f)
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
        loadingFont.draw(batch, loadingText, camera.viewportWidth / 2 - 200, camera.viewportHeight * 1 / 3)
        batch.end()
    }
}*/