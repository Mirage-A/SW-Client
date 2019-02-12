package com.mirage.view.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.FPSLogger
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.maps.MapObject
import com.badlogic.gdx.maps.tiled.TideMapLoader
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.maps.tiled.renderers.IsometricStaggeredTiledMapRenderer
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer
import com.badlogic.gdx.math.Vector3
import com.mirage.controller.Platform
import com.mirage.model.Model
import com.mirage.model.Time
import com.mirage.model.datastructures.*
import com.mirage.view.Log
import com.mirage.view.ScreenSizeCalculator
import com.mirage.view.TextureLoader
import com.mirage.view.animation.BodyAction
import com.mirage.view.animation.LegsAction
import com.mirage.view.animation.MoveDirection
import com.mirage.view.animation.WeaponType
import com.mirage.view.gameobjects.HumanoidDrawer
import com.mirage.view.gameobjects.Image
import com.mirage.view.gameobjects.ObjectDrawer
import java.util.ArrayList
import java.util.HashMap
import kotlin.math.sqrt

class GameScreen : ScreenAdapter() {

    override fun resize(width: Int, height: Int) {
        val viewportSize = ScreenSizeCalculator.calculateViewportSize(width.toFloat(), height.toFloat())
        scrW = viewportSize.width
        scrH = viewportSize.height
        camera.setToOrtho(false, scrW, scrH)
    }

    private val batch: SpriteBatch = SpriteBatch()
    private var camera: OrthographicCamera = OrthographicCamera()
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
    }

    /**
     * Словарь, где по объекту сцены мы получаем его визуальное представление
     */
    private var objectDrawers: MutableMap<MapObject, ObjectDrawer> = HashMap()

    /**
     * Размеры виртуального экрана
     */
    private var scrW: Float = 0f
    private var scrH: Float = 0f

    /**
     * Интервал времени, который должен пройти с последней смены направления движения,
     * чтобы изменение отобразилось
     * (эта задержка убирает моргание анимации при быстром нажатии разных кнопок)
     */
    private val moveDirectionUpdateInterval = 50L


    /**
     * Отрисовка экрана
     */
    override fun render(delta: Float) {
        Time.deltaTime = Gdx.graphics.deltaTime
        Model.update()
        val map = Model.getMap()
        val playerPosOnVirtualScreen = getVirtualScreenPointFromScene(Model.getPlayerPosition())

        Gdx.gl.glClearColor(0.25f, 0.25f, 0.25f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        camera.position.x = playerPosOnVirtualScreen.x
        camera.position.y = playerPosOnVirtualScreen.y + DELTA_CENTER_Y
        camera.position.x = Math.round(camera.position.x).toFloat()
        camera.position.y = Math.round(camera.position.y).toFloat()
        camera.update()
        //batch.projectionMatrix = camera.combined

        //TODO отрисовка
        renderer.map = Model.getMap()
        renderer.setView(camera)
        renderer.render()

        batch.begin()
        drawObjects(map)
        batch.end()
    }

    /**
     * Загружает все текстуры, объекты и прочие ресурсы, необходимые на данной сцене
     */
    fun updateResources() {
        loadObjectDrawers(Model.getMap())
    }

    /**
     * Отрисовывает все объекты сцены
     * @param scene Сцена
     */
    private fun drawObjects(map: TiledMap) {
        val objs = ArrayList<MapObject>()

        for (layer in map.layers) {
            for (obj in layer.objects) {
                objs.add(obj)
            }
        }

        //TODO Разобраться с сортировкой по глубине
        objs.sortWith(Comparator { obj1, obj2 ->
            -java.lang.Float.compare(getVirtualScreenPointFromScene(obj1.getPosition()).y,
                    getVirtualScreenPointFromScene(obj2.getPosition()).y)
        })

        for (obj in objs) {
            val drawer = objectDrawers[obj] ?: addObjectDrawer(obj)
            //TODO Направление движения может влиять не только на HumanoidDrawer
            if (drawer is HumanoidDrawer) {
                val updatedMoveDirection = MoveDirection.fromMoveAngle(obj.getMoveAngle())
                if (updatedMoveDirection !== drawer.bufferedMoveDirection) {
                    drawer.lastMoveDirectionUpdateTime = System.currentTimeMillis()
                    drawer.bufferedMoveDirection = updatedMoveDirection
                } else if (System.currentTimeMillis() - drawer.lastMoveDirectionUpdateTime > moveDirectionUpdateInterval) {
                    drawer.moveDirection = drawer.bufferedMoveDirection
                }

                if (obj.isMoving()) {
                    drawer.setBodyAction(BodyAction.RUNNING)
                    drawer.setLegsAction(LegsAction.RUNNING)
                } else {
                    drawer.setBodyAction(BodyAction.IDLE)
                    drawer.setLegsAction(LegsAction.IDLE)
                }
            }
            val pos = getVirtualScreenPointFromScene(obj.getPosition())
            drawer.draw(batch, Math.round(pos.x).toFloat(), Math.round(pos.y).toFloat())
        }
    }

    /**
     * Загружает objectDrawers для объектов сцены
     */
    private fun loadObjectDrawers(map: TiledMap) {
        objectDrawers = HashMap()
        for (layer in map.layers) {
            for (obj in layer.objects) {
                addObjectDrawer(obj)
            }
        }
    }

    /**
     * Добавляет objectDrawer данного объекта сцены в словарь
     * //TODO
     */
    private fun addObjectDrawer(obj: MapObject) : ObjectDrawer {
        objectDrawers[obj] = when (true) {
            obj.name == "player" -> HumanoidDrawer(loadPlayerTexturesMap(obj), BodyAction.IDLE, LegsAction.IDLE, MoveDirection.fromMoveAngle(obj.properties.getFloat("moveAngle", 0f)), WeaponType.UNARMED)
            else -> TextureLoader.getStaticTexture("windows_icon.png")
        }
        return objectDrawers[obj]!!
    }
    /**
     * Загружает текстуры брони игрока и упаковывает их в словарь
     * @return Словарь с текстурами брони игрока
     * //TODO
     */
    private fun loadPlayerTexturesMap(obj: MapObject): MutableMap<String, Image> {
        val texturesMap = HashMap<String, Image>()
        for (md in MoveDirection.values()) {
            texturesMap["head$md"] = TextureLoader.getStaticTexture("equipment/head/0000$md.png")
        }
        texturesMap["body"] = TextureLoader.getStaticTexture("equipment/body/0000.png")
        texturesMap["handtop"] = TextureLoader.getStaticTexture("equipment/handtop/0000.png")
        texturesMap["handbottom"] = TextureLoader.getStaticTexture("equipment/handbottom/0000.png")
        texturesMap["legtop"] = TextureLoader.getStaticTexture("equipment/legtop/0000.png")
        texturesMap["legbottom"] = TextureLoader.getStaticTexture("equipment/legbottom/0000.png")
        texturesMap["cloak"] = TextureLoader.getStaticTexture("equipment/cloak/0000.png")
        texturesMap["neck"] = TextureLoader.getStaticTexture("equipment/neck/0000.png")
        texturesMap["weapon1"] = TextureLoader.getStaticTexture("equipment/onehanded/0000.png")
        texturesMap["weapon2"] = TextureLoader.getStaticTexture("equipment/onehanded/0000.png")
        return texturesMap
    }


    override fun dispose() {

    }

    /**
     * Переход от базиса сцены (тайлы) к базису виртуального экрана (пиксели)
     */
    private fun getVirtualScreenPointFromScene(scenePoint: Point): Point {
        val x = GameScreen.TILE_WIDTH / 2 * scenePoint.x + GameScreen.TILE_WIDTH / 2 * scenePoint.y
        val y = -GameScreen.TILE_HEIGHT / 2 * scenePoint.x + GameScreen.TILE_HEIGHT / 2 * scenePoint.y + TILE_HEIGHT / 2
        return Point(x, y)
    }

    /**
     * Переход от базиса виртуального экрана (пиксели) к базису сцены (тайлы)
     */
    private fun getScenePointFromVirtualScreen(virtualScreenPoint: Point): Point {
        val x = virtualScreenPoint.x / GameScreen.TILE_WIDTH - (virtualScreenPoint.y - TILE_HEIGHT / 2) / GameScreen.TILE_HEIGHT
        val y = virtualScreenPoint.x / GameScreen.TILE_WIDTH + (virtualScreenPoint.y - TILE_HEIGHT / 2) / GameScreen.TILE_HEIGHT
        return Point(x, y)
    }


}