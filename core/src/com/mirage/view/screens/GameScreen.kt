package com.mirage.view.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.maps.tiled.TideMapLoader
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer
import com.badlogic.gdx.math.Vector3
import com.mirage.controller.Platform
import com.mirage.model.Model
import com.mirage.model.Time
import com.mirage.model.datastructures.Point
import com.mirage.model.scene.Scene
import com.mirage.model.scene.objects.SceneObject
import com.mirage.model.scene.objects.entities.Entity
import com.mirage.model.scene.objects.entities.Player
import com.mirage.view.Log
import com.mirage.view.ScreenSizeCalculator
import com.mirage.view.TextureLoader
import com.mirage.view.animation.BodyAction
import com.mirage.view.animation.LegsAction
import com.mirage.view.animation.MoveDirection
import com.mirage.view.gameobjects.HumanoidDrawer
import com.mirage.view.gameobjects.Image
import com.mirage.view.gameobjects.ObjectDrawer
import java.util.ArrayList
import java.util.HashMap

class GameScreen : ScreenAdapter() {

    override fun resize(width: Int, height: Int) {
        val viewportSize = ScreenSizeCalculator.calculateViewportSize(width.toFloat(), height.toFloat())
        scrW = viewportSize.width
        scrH = viewportSize.height
        camera.setToOrtho(false, scrW, scrH)
    }

    private val batch: SpriteBatch = SpriteBatch()
    private var camera: OrthographicCamera = OrthographicCamera()

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
        const val TILE_WIDTH = 64f
        const val TILE_HEIGHT = 32f

        /**
         * Разница y - координаты между координатами игрока и координатами центра экрана
         * (точка под игроком находится на DELTA_CENTER_Y пикселей ниже центра экрана).
         */
        const val DELTA_CENTER_Y = 64f
    }

    /**
     * Список текстур, используемых на данной сцене (карте)
     */
    private var tileTextures: MutableList<Image> = ArrayList()

    /**
     * Словарь, где по объекту сцены мы получаем его визуальное представление
     */
    private var objectDrawers: MutableMap<SceneObject, ObjectDrawer> = HashMap()

    /**
     * Размеры виртуального экрана
     */
    private var scrW: Float = 0f
    private var scrH: Float = 0f

    /**
     * Отображение FPS
     */
    private var showFPS = true
    private val fpsFont = BitmapFont()

    /**
     * Интервал времени, который должен пройти с последней смены направления движения,
     * чтобы изменение отобразилось
     * (эта задержка убирает моргание анимации при быстром нажатии разных кнопок)
     */
    private val moveDirectionUpdateInterval = 50L

    /**
     * Лямбда, которая по координатам тайла вне сцены возвращает номер тайла в tileTextures
     * Используется для заполнения пространства за сценой
     */
    private var backgroundTileGenerator: (Int, Int) -> Int = {_, _-> 0}

    private val map = TmxMapLoader().load(Platform.ASSETS_PATH + "maps/test.tmx")

    /**
     * Отрисовка экрана
     */
    override fun render(delta: Float) {
        Time.deltaTime = Gdx.graphics.deltaTime
        Model.update()
        val scene = Model.getScene()
        val playerPosOnVirtualScreen = getVirtualScreenPointFromScene(Model.getPlayerPosition())

        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        batch.projectionMatrix = camera.combined
        camera.translate(playerPosOnVirtualScreen.x - camera.position.x, playerPosOnVirtualScreen.y - camera.position.y + DELTA_CENTER_Y)
        camera.update()


        batch.begin()

        //TODO отрисовка
        drawTiles(scene)

        drawObjects(scene)


        if (showFPS) {
            fpsFont.draw(batch, "" + Gdx.graphics.framesPerSecond + " FPS", 6f, 20f)
        }

        batch.end()
    }

    init {
        updateResources()
    }

    /**
     * Загружает все текстуры, объекты и прочие ресурсы, необходимые на данной сцене
     */
    private fun updateResources() {
        loadObjectDrawers(Model.getScene())
        loadTileTextures(Model.getScene())
    }

    /**
     * Загружает текстуры тайлов, используемых в данной сцене
     * //TODO
     */
    private fun loadTileTextures(scene: Scene) {
        tileTextures = ArrayList()
        tileTextures.add(TextureLoader.getStaticTexture("tiles/0001.png"))
        tileTextures.add(TextureLoader.getStaticTexture("tiles/0000.png"))
    }

    /**
     * Отрисовывает все объекты сцены
     * @param scene Сцена
     */
    private fun drawObjects(scene: Scene) {
        val sceneObjects: ArrayList<SceneObject>

        synchronized(scene.objects) {
            sceneObjects = ArrayList(scene.objects)
        }

        sceneObjects.sortWith(Comparator { obj1, obj2 ->
            -java.lang.Float.compare(getVirtualScreenPointFromScene(obj1.position).y,
                    getVirtualScreenPointFromScene(obj2.position).y)
        })

        for (sceneObject in sceneObjects) {
            val drawer = objectDrawers[sceneObject] ?: addObjectDrawer(sceneObject)
            //TODO Направление движения может влиять не только на HumanoidDrawer
            if (sceneObject is Entity && drawer is HumanoidDrawer) {
                val updatedMoveDirection = MoveDirection.fromMoveAngle(sceneObject.moveAngle)
                if (updatedMoveDirection !== drawer.bufferedMoveDirection) {
                    drawer.lastMoveDirectionUpdateTime = System.currentTimeMillis()
                    drawer.bufferedMoveDirection = updatedMoveDirection
                } else if (System.currentTimeMillis() - drawer.lastMoveDirectionUpdateTime > moveDirectionUpdateInterval) {
                    drawer.moveDirection = drawer.bufferedMoveDirection
                }

                if (sceneObject.isMoving) {
                    drawer.setBodyAction(BodyAction.RUNNING)
                    drawer.setLegsAction(LegsAction.RUNNING)
                } else {
                    drawer.setBodyAction(BodyAction.IDLE)
                    drawer.setLegsAction(LegsAction.IDLE)
                }
            }
            drawer.draw(batch, getVirtualScreenPointFromScene(Model.getPlayerPosition()).x,
                    getVirtualScreenPointFromScene(Model.getPlayerPosition()).y)
        }

    }

    /**
     * Загружает objectDrawers для объектов сцены
     */
    private fun loadObjectDrawers(scene: Scene) {
        objectDrawers = HashMap()
        for (sceneObject in scene.objects) {
            addObjectDrawer(sceneObject)
        }
    }

    /**
     * Добавляет objectDrawer данного объекта сцены в словарь
     * //TODO
     */
    private fun addObjectDrawer(sceneObject: SceneObject) : ObjectDrawer {
        objectDrawers[sceneObject] = when (sceneObject) {
            is Player -> HumanoidDrawer(loadPlayerTexturesMap(sceneObject), BodyAction.IDLE, LegsAction.IDLE, MoveDirection.fromMoveAngle(sceneObject.moveAngle), sceneObject.weaponType)
            else -> TextureLoader.getStaticTexture("windows_icon.png")
        }
        return objectDrawers[sceneObject]!!
    }
    /**
     * Загружает текстуры брони игрока и упаковывает их в словарь
     * @return Словарь с текстурами брони игрока
     * //TODO
     */
    private fun loadPlayerTexturesMap(player: Player): MutableMap<String, Image> {
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

    /**
     * Отрисовывает тайлы, попадающие в обзор
     * @param scene Текущая сцена
     */
    private fun drawTiles(scene: Scene) {
        val x1 = getScenePointFromVirtualScreen(Point(camera.position.x - scrW / 2, camera.position.y + scrH / 2)).x.toInt() - 2
        val x2 = getScenePointFromVirtualScreen(Point(camera.position.x + scrW / 2, camera.position.y - scrH / 2)).x.toInt() + 2
        val y1 = getScenePointFromVirtualScreen(Point(camera.position.x - scrW / 2, camera.position.y - scrH / 2)).y.toInt() - 2
        val y2 = getScenePointFromVirtualScreen(Point(camera.position.x + scrW / 2, camera.position.y + scrH / 2)).y.toInt() + 2

        for (i in x1..x2) {
            for (j in y1..y2) {
                val scenePoint = Point(i + 0.5f, j + 0.5f)
                val cameraPoint = getVirtualScreenPointFromScene(scenePoint)
                val tileIndex = when(true) {
                    (i in 0 until scene.width && j in 0 until scene.height) -> scene.getTileId(i, j)
                    else -> backgroundTileGenerator.invoke(i, j)
                }
                batch.draw(tileTextures[tileIndex].getTexture(),
                        cameraPoint.x - TILE_WIDTH / 2, cameraPoint.y - TILE_HEIGHT / 2)
            }
        }
    }

    override fun dispose() {
        for (t in tileTextures) {
            t.getTexture().dispose()
        }
    }

    /**
     * Переход от базиса сцены (тайлы) к базису виртуального экрана (пиксели)
     */
    private fun getVirtualScreenPointFromScene(scenePoint: Point): Point {
        val x = GameScreen.TILE_WIDTH / 2 * scenePoint.x + GameScreen.TILE_WIDTH / 2 * scenePoint.y
        val y = -GameScreen.TILE_HEIGHT / 2 * scenePoint.x + GameScreen.TILE_HEIGHT / 2 * scenePoint.y
        return Point(x, y)
    }

    /**
     * Переход от базиса виртуального экрана (пиксели) к базису сцены (тайлы)
     */
    private fun getScenePointFromVirtualScreen(virtualScreenPoint: Point): Point {
        val x = virtualScreenPoint.x / GameScreen.TILE_WIDTH - virtualScreenPoint.y / GameScreen.TILE_HEIGHT
        val y = virtualScreenPoint.x / GameScreen.TILE_WIDTH + virtualScreenPoint.y / GameScreen.TILE_HEIGHT
        return Point(x, y)
    }
}