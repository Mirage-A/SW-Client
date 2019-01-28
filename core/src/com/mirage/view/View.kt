package com.mirage.view

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.mirage.model.Model
import com.mirage.model.scene.Scene

import com.mirage.model.datastructures.Point
import com.mirage.model.scene.objects.SceneObject
import com.mirage.model.scene.objects.entities.Entity
import com.mirage.model.scene.objects.entities.Player
import com.mirage.view.gameobjects.ObjectDrawer
import com.mirage.view.gameobjects.Image
import com.mirage.view.animation.BodyAction
import com.mirage.view.gameobjects.HumanoidDrawer
import com.mirage.view.animation.LegsAction
import com.mirage.view.animation.MoveDirection

import java.util.ArrayList
import java.util.Comparator
import java.util.HashMap

/**
 * Основной класс представления
 * Отвечает за всё визуальное представление
 */
object View {
    /**
     * Эталонный размер экрана
     * Все изображения рисуются под этот размер
     * Для других экранов размеры изображений подгоняются так,
     * чтобы различие с эталонным размером экрана было минимально,
     * но при этом чтобы изображения не сплющивались и не растягивались,
     * т.е. отношение ширины и высоты всех изображений сохранялось.
     */
    internal const val DEFAULT_SCREEN_WIDTH = 1920f
    internal const val DEFAULT_SCREEN_HEIGHT = 1080f
    /**
     * Размер одного тайла на виртуальном экране
     */
    const val TILE_WIDTH = 128f
    const val TILE_HEIGHT = 64f

    /**
     * Отступы в пикселях осей координат виртуального экрана от углов тайловой сетки
     * (запас для фона, если игрок подошёл к краю сцены).
     */
    const val X_MARGIN = 1500f
    const val Y_MARGIN = 1000f
    /**
     * Разница y - координаты между координатами игрока и координатами центра экрана
     * (точка под игроком находится на DELTA_CENTER_Y пикселей ниже центра экрана).
     */
    private const val DELTA_CENTER_Y = 64f


    /**
     * Список текстур, используемых на данной сцене (карте)
     */
    private var tileTextures: MutableList<Image> = ArrayList()

    /**
     * Словарь, где по объекту сцены мы получаем его визуальное представление
     */
    private var objectDrawers: MutableMap<SceneObject, ObjectDrawer> = HashMap()

    private val batch: SpriteBatch
    var camera: OrthographicCamera
    /**
     * Размеры виртуального экрана
     */
    private var scrW: Float = 0f
    private var scrH: Float = 0f

    /**
     * Отображение FPS
     */
    var showFPS = true
    private val fpsFont = BitmapFont()

    /**
     * Реальные размеры экрана при последнем вызове render
     * Если они изменились, то автоматически вызывается setScreenSize
     */
    private var lastRealScreenWidth = 0f
    private var lastRealScreenHeight = 0f

    /**
     * Интервал времени, который должен пройти с последней смены направления движения,
     * чтобы изменение отобразилось
     * (эта задержка убирает моргание анимации при быстром нажатии разных кнопок)
     */
    private val moveDirectionUpdateInterval = 50L

    init {
        loadTileTextures(Model.getScene())
        loadObjectDrawers(Model.getScene())

        camera = OrthographicCamera()
        batch = SpriteBatch()
    }

    /**
     * Отрисовка экрана
     */
    fun render() {
        if (lastRealScreenWidth != Gdx.graphics.width.toFloat() || lastRealScreenHeight != Gdx.graphics.height.toFloat()) {
            setScreenSize(Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        }
        val scene = Model.getScene()
        val playerPosOnVirtualScreen = BasisSwitcher.getVirtualScreenPoint(Model.getPlayerPosition(), scene)
        //TODO Вычислить положение экрана из положения персонажа в сцене
        val scrX = playerPosOnVirtualScreen.x - scrW / 2
        val scrY = playerPosOnVirtualScreen.y - scrH / 2 + DELTA_CENTER_Y

        Gdx.gl.glClearColor(0.25f, 0.25f, 1f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        // tell the camera to update its matrices.
        camera.update()

        // tell the SpriteBatch to render in the
        // coordinate system specified by the camera.
        batch.projectionMatrix = camera.combined

        batch.begin()

        //TODO отрисовка
        drawTiles(scrX, scrY, scene)

        drawObjects(scrX, scrY, scene)


        if (showFPS) {
            fpsFont.draw(batch, "" + Gdx.graphics.framesPerSecond + " FPS", 6f, 20f)
        }

        batch.end()
    }

    fun dispose() {
        batch.dispose()
        for (t in tileTextures) {
            t.getTexture().dispose()
        }
    }

    /**
     * Метод, который должен вызываться при изменении параметров реального экрана.
     * @param realWidth Ширина реального экрана
     * @param realHeight Высота реального экрана
     */
    private fun setScreenSize(realWidth: Float, realHeight: Float) {
        val viewportSize = ScreenSizeCalculator.calculateViewportSize(realWidth, realHeight)
        scrW = viewportSize.width
        scrH = viewportSize.height
        camera.setToOrtho(false, scrW, scrH)
        lastRealScreenWidth = realWidth
        lastRealScreenHeight = realHeight
    }

    /**
     * Отрисовывает все объекты сцены
     * @param scene Сцена
     */
    private fun drawObjects(scrX: Float, scrY: Float, scene: Scene) {
        val sceneObjects: ArrayList<SceneObject>

        synchronized(scene.objects) {
            sceneObjects = ArrayList(scene.objects)
        }

        sceneObjects.sortWith(Comparator { obj1, obj2 ->
            -java.lang.Float.compare(BasisSwitcher.getVirtualScreenPoint(obj1.position, scene).y,
                    BasisSwitcher.getVirtualScreenPoint(obj2.position, scene).y)
        })

        for (sceneObject in sceneObjects) {
            val drawer = objectDrawers[sceneObject]
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
            drawer?.draw(batch, BasisSwitcher.getViewportPointFromScene(Model.getPlayerPosition(), scene, scrX, scrY).x,
                    BasisSwitcher.getViewportPointFromScene(Model.getPlayerPosition(), scene, scrX, scrY).y)
        }

    }

    /**
     * Загружает текстуры тайлов, используемых в данной сцене
     * //TODO
     */
    private fun loadTileTextures(scene: Scene) {
        tileTextures = ArrayList()
        tileTextures.add(TextureLoader.getStaticTexture("tiles/0000.png"))
        tileTextures.add(TextureLoader.getStaticTexture("tiles/0001.png"))
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
    private fun addObjectDrawer(sceneObject: SceneObject) {
        if (sceneObject is Player) {
            objectDrawers[sceneObject] = HumanoidDrawer(loadPlayerTexturesMap(sceneObject), BodyAction.IDLE, LegsAction.IDLE, MoveDirection.fromMoveAngle(sceneObject.moveAngle), sceneObject.weaponType)
        }
    }
    /**
     * Загружает текстуры брони игрока и упаковывает их в словарь
     * @return Словарь с текстурами брони игрока
     * //TODO
     */
    private fun loadPlayerTexturesMap(player: Player): MutableMap<String, Image> {
        val texturesMap = HashMap<String, Image>()
        for (md in MoveDirection.values()) {
            texturesMap["head" + md.toString()] = TextureLoader.getStaticTexture("equipment/head/0000" + md.toString() + ".png")
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
     * @param scrX Координаты экрана
     * @param scrY Координаты экрана
     * @param scene Текущая сцена
     */
    private fun drawTiles(scrX: Float, scrY: Float, scene: Scene) {
        val tileMatrix = scene.tileMatrix
        for (i in 0 until scene.width) {
            for (j in 0 until scene.height) {
                val scenePoint = Point(i + 0.5f, j + 0.5f)
                val cameraPoint = BasisSwitcher.getViewportPointFromScene(scenePoint, scene, scrX, scrY)
                batch.draw(tileTextures[tileMatrix[i][j]].getTexture(),
                        cameraPoint.x - TILE_WIDTH / 2, cameraPoint.y - TILE_HEIGHT / 2)
            }
        }
    }


}
