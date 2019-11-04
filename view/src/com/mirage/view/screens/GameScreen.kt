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
import com.mirage.utils.extensions.findDifferenceWith
import com.mirage.utils.extensions.position
import com.mirage.utils.gameobjects.GameObjects
import com.mirage.utils.maps.GameMap
import com.mirage.utils.messaging.MoveDirection
import com.mirage.utils.messaging.SnapshotManager
import com.mirage.view.game.calculateViewportSize
import com.mirage.view.game.getVirtualScreenPointFromScene
import com.mirage.view.game.renderObjects
import com.mirage.view.gameobjects.Drawers
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin


class GameScreen(private val stateManager: SnapshotManager, private val gameMap: GameMap, private val playerID: Long): ScreenAdapter() {

    private val batch: SpriteBatch = SpriteBatch()
    var camera: OrthographicCamera = OrthographicCamera()

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
     * Состояние, которое было отрисовано на предыдущем вызове [render]
     * Нужно для определения разницы с текущим состоянием и обновления [drawers]
     */
    private var lastRenderedObjs = GameObjects(mapOf(), Long.MIN_VALUE)

    /**
     * Отрисовка экрана
     */
    override fun render(delta: Float) {

        val objs = stateManager.getInterpolatedSnapshot()
        drawers.updateDrawers(objs.findDifferenceWith(lastRenderedObjs))

        val player = objs[playerID]
        val playerPosOnScene = player?.position ?: DEFAULT_MAP_POINT

        val playerPosOnVirtualScreen = getVirtualScreenPointFromScene(playerPosOnScene)

        Gdx.gl.glClearColor(0.25f, 0.25f, 0.25f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        camera.position.x = playerPosOnVirtualScreen.x
        camera.position.y = playerPosOnVirtualScreen.y + DELTA_CENTER_Y
        camera.position.x = camera.position.x.roundToInt().toFloat()
        camera.position.y = camera.position.y.roundToInt().toFloat()
        camera.update()

        //TODO отрисовка карты
        /*renderer.map = infoClient.map
        renderer.setView(camera)
        renderer.render()*/

        batch.begin()

        renderObjects(batch, objs, drawers)
        //TODO
        //Временное решение для управления на андроиде, потом этот код должен быть вынесен в UI
        if (PLATFORM == "android") {

            val mdBtnPos = if (player != null && player.isMoving) {
                val mdBtnCenterShift = mdAreaRadius - mdBtnRadius
                val angle = (MoveDirection.fromString(player.moveDirection ?: "RIGHT").toMoveAngle())
                MutablePoint(mdAreaCenterX + mdBtnCenterShift * cos(angle) - mdBtnRadius,
                        mdAreaCenterX + mdBtnCenterShift * sin(angle) - mdBtnRadius)
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
     * Обновить размер виртуального экрана, вычислив его через ScreenSizeCalculator
     */
    override fun resize(width: Int, height: Int) {
        val viewportSize = calculateViewportSize(width.toFloat(), height.toFloat())
        scrW = viewportSize.width
        scrH = viewportSize.height
        camera.setToOrtho(false, scrW, scrH)
    }

}