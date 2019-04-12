package com.mirage.view.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer
import com.mirage.utils.*
import com.mirage.utils.datastructures.MutablePoint
import com.mirage.utils.extensions.position
import com.mirage.utils.messaging.ClientGameInfo
import com.mirage.utils.messaging.SnapshotManager
import com.mirage.view.game.calculateViewportSize
import com.mirage.view.game.getVirtualScreenPointFromScene
import com.mirage.view.game.renderObjects
import com.mirage.view.gameobjects.Drawers


class GameScreen(private val stateManager: SnapshotManager, private val infoClient: ClientGameInfo): ScreenAdapter() {

    private val batch: SpriteBatch = SpriteBatch()
    var camera: OrthographicCamera = OrthographicCamera()
    private val renderer: IsometricTiledMapRenderer = IsometricTiledMapRenderer(null, batch)

    companion object {
        /**
         * Эталонный размер экрана
         * Все изображения рисуются под этот размер
         * Для других экранов размеры изображений подгоняются так,
         * чтобы различие с эталонным размером экрана было минимально,
         * но при этом чтобы изображения не сплющивались и не растягивались,
         * т.е. отношение ширины и высоты всех изображений сохранялось.
         */
        const val DEFAULT_SCREEN_WIDTH = 1920f
        const val DEFAULT_SCREEN_HEIGHT = 1080f
        /**
         * Размер одного тайла на виртуальном экране
         */
        const val TILE_WIDTH = 128f
        const val TILE_HEIGHT = 64f

        /**
         * Разница y - координаты между координатами игрока и координатами центра экрана
         * (точка под игроком находится на DELTA_CENTER_Y пикселей ниже центра экрана).
         */
        const val DELTA_CENTER_Y = 64f

        val mdAreaTexture = Assets.getRawTexture("ui/mdarea")
        val mdAreaMargin = 64f
        val mdAreaWidth = mdAreaTexture.width
        val mdAreaRadius = mdAreaWidth / 2
        val mdAreaCenterX = mdAreaMargin + mdAreaRadius
        val mdBtnTexture = Assets.getRawTexture("ui/mdbtn")
        val mdBtnWidth = mdBtnTexture.width
        val mdBtnRadius = mdBtnWidth / 2
    }

    /**
     * Словарь, где по объекту сцены мы получаем его визуальное представление
     */
    val drawers = Drawers(camera)

    /**
     * Размеры виртуального экрана
     */
    private var scrW: Float = 0f
    private var scrH: Float = 0f

    /**
     * Отрисовка экрана
     */
    override fun render(delta: Float) {
        val playerID : Long = infoClient.playerID ?: return

        val snapshot = stateManager.getInterpolatedSnapshot()

        val player = infoClient.objects[playerID]
        val playerPosOnScene = snapshot.positions[playerID] ?: infoClient.objects[playerID]?.position ?: DEFAULT_MAP_POINT

        val playerPosOnVirtualScreen = getVirtualScreenPointFromScene(playerPosOnScene)

        Gdx.gl.glClearColor(0.25f, 0.25f, 0.25f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        camera.position.x = playerPosOnVirtualScreen.x
        camera.position.y = playerPosOnVirtualScreen.y + DELTA_CENTER_Y
        camera.position.x = Math.round(camera.position.x).toFloat()
        camera.position.y = Math.round(camera.position.y).toFloat()
        camera.update()

        //TODO отрисовка
        renderer.map = infoClient.map
        renderer.setView(camera)
        renderer.render()

        batch.begin()
        renderObjects(batch, infoClient, snapshot, drawers)
        //TODO
        //Временное решение для управления на андроиде, потом этот код должен быть вынесен в UI
        if (PLATFORM == "android") {

            val mdBtnPos = if (player != null && snapshot.isMoving[playerID] == true) {
                val mdBtnCenterShift = mdAreaRadius - mdBtnRadius
                val angle = (snapshot.moveDirections[playerID]?.toAngle()?.toDouble() ?: (Math.PI / 4)) - Math.PI / 4
                MutablePoint(mdAreaCenterX + mdBtnCenterShift * Math.cos(angle).toFloat() - mdBtnRadius,
                        mdAreaCenterX + mdBtnCenterShift * Math.sin(angle).toFloat() - mdBtnRadius)
            }
            else {
                MutablePoint(mdAreaCenterX - mdBtnRadius, mdAreaCenterX - mdBtnRadius)
            }
            val scrX = camera.position.x - camera.viewportWidth / 2
            val scrY = camera.position.y - camera.viewportHeight / 2
            batch.draw(mdAreaTexture, mdAreaMargin + scrX, mdAreaMargin + scrY)
            batch.draw(mdBtnTexture, mdBtnPos.x + scrX, mdBtnPos.y + scrY)
        }

        batch.end()

    }

    /**
     * Загружает все текстуры, объекты и прочие ресурсы, необходимые на данной сцене
     */
    fun updateResources() {
        loadObjectDrawers(infoClient.map)
    }

    /**
     * Загружает drawers для объектов сцены
     */
    private fun loadObjectDrawers(map: TiledMap) {
        for (layer in map.layers) {
            for (obj in layer.objects) {
                drawers.addObjectDrawer(obj)
            }
        }
    }



    override fun dispose() {

    }


    /**
     * Обновить размер виртуального экрана, вычислив его через ScreenSizeCalculator
     */
    override fun resize(width: Int, height: Int) {
        val viewportSize = calculateViewportSize(width.toFloat(), height.toFloat())
        scrW = viewportSize.width
        scrH = viewportSize.height
        camera.setToOrtho(false, scrW, scrH)
    }

}