package com.mirage.view.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.maps.MapObject
import com.badlogic.gdx.maps.MapProperties
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer
import com.badlogic.gdx.math.Rectangle
import com.mirage.controller.Platform
import com.mirage.model.Model
import com.mirage.model.Time
import com.mirage.model.datastructures.*
import com.mirage.model.extensions.*
import com.mirage.view.Log
import com.mirage.view.ScreenSizeCalculator
import com.mirage.view.TextureLoader
import com.mirage.view.animation.BodyAction
import com.mirage.view.animation.LegsAction
import com.mirage.view.animation.MoveDirection
import com.mirage.view.animation.WeaponType
import com.mirage.view.gameobjects.*
import java.util.*



class GameScreen : ScreenAdapter() {

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


        val mdAreaTexture = TextureLoader.getRawTexture("ui/mdarea.png")
        val mdAreaMargin = 64f
        val mdAreaWidth = mdAreaTexture.width
        val mdAreaRadius = mdAreaWidth / 2
        val mdAreaCenterX = mdAreaMargin + mdAreaRadius
        val mdBtnTexture = TextureLoader.getRawTexture("ui/mdbtn.png")
        val mdBtnWidth = mdBtnTexture.width
        val mdBtnRadius = mdBtnWidth / 2
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
        //TODO
        //Временное решение для управления на андроиде, потом этот код должен быть вынесен в Stage
        if (Platform.TYPE == Platform.Types.ANDROID) {

            val mdBtnPos = if (Model.isPlayerMoving()) {
                val mdBtnCenterShift = mdAreaRadius - mdBtnRadius
                val angle = Model.getMoveAngle().toDouble() - Math.PI / 4
                Point(mdAreaCenterX + mdBtnCenterShift * Math.cos(angle).toFloat() - mdBtnRadius,
                        mdAreaCenterX + mdBtnCenterShift * Math.sin(angle).toFloat() - mdBtnRadius)
            }
            else {
                Point(mdAreaCenterX - mdBtnRadius, mdAreaCenterX - mdBtnRadius)
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
        loadObjectDrawers(Model.getMap())
    }

    /**
     * Отрисовывает все объекты сцены
     * @param scene Сцена
     */
    private fun drawObjects(map: TiledMap) {
        val objs = ArrayList<MapObject>()

        for (obj in map) {
            objs.add(obj)
        }

        depthSort(objs)

        for (obj in objs) {
            val drawer = objectDrawers[obj] ?: addObjectDrawer(obj)
            //TODO Направление движения может влиять не только на HumanoidAnimation
            if (drawer is HumanoidAnimation) {
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
            //val pos = getVirtualScreenPointFromScene(obj.getPosition())
            val scenePoint = obj.getPosition()
            val width = obj.properties.getFloat("width", 0f)
            val height = obj.properties.getFloat("height", 0f)
            val sceneCenter = Point(scenePoint.x + width / 2, scenePoint.y + height / 2)
            val pos = getVirtualScreenPointFromScene(sceneCenter)
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
    fun addObjectDrawer(obj: MapObject) : ObjectDrawer {
        objectDrawers[obj] = when (true) {
            obj.name == "player" -> HumanoidAnimation(loadPlayerTexturesMap(obj), BodyAction.IDLE, LegsAction.IDLE, MoveDirection.fromMoveAngle(obj.properties.getFloat("moveAngle", 0f)), WeaponType.UNARMED)
            obj.properties.containsKey("animation") -> ObjectAnimation(obj.properties.getString("animation", "MAIN_GATE_OPEN"))
            obj.properties.containsKey("texture") -> TextureLoader.getStaticTexture("objects/" + obj.properties.getString("texture", "null.png"), Image.Alignment.CENTER)
            else -> TextureLoader.getStaticTexture("windows_icon.png")
        }
        return objectDrawers[obj]!!
    }


    /**
     * Загружает текстуры брони игрока и упаковывает их в словарьddddd
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

    /**
     * Сортировка списка объектов на карте по глубине (по порядку отрисовки)
     * Используется алгоритм топологической сортировки ориентированного графа
     * (На множестве объектов задан частичный порядок, а не линейный)
     */
    private fun depthSort(objs: ArrayList<MapObject>) {

        /**
         * Возвращает 1, если точка p отрисовывается после прямоугольника rect
         * Возвращает -1, если точка p отрисовывается до прямоугольника rect
         * Возвращает 0, если объекты могут отрисовываться в любом относительном порядке
         * (т.е. объекты не сравнимы либо равны)
         */
        fun compare(p : Point, rect: Rectangle) : Int {
            /**
             * Находит значение функции f(x,y) = x + y - x0 - y0 для данной точки
             * Знак функции позволяет узнать расположение точки (x,y) относительно диагональной прямой,
             * проходящей через точку p
             */
            fun f(x: Float, y: Float) : Float = x + y - p.x - p.y

            //Если прямая и прямоугольник пересекаются
            if (f(rect.x, rect.y) <= 0 && f(rect.x + rect.width, rect.y + rect.height) >= 0) {
                //Вычисляем другую функцию - функцию от точки p относительно прямой, соединяющей 2 угловые точки прямоугольника
                val fun2 = - p.y * rect.width + p.x * rect.height + rect.y * rect.width - rect.x * rect.height
                if (fun2 > 0) return 1
                if (fun2 < 0) return -1
            }
            return 0
        }

        /**
         * Возвращает 1, если объект a отрисовывается после объекта b
         * Возвращает -1, если объект a отрисовывается до объекта b
         * Возвращает 0, если объекты могут отрисовываться в любом относительном порядке
         * (т.е. объекты не сравнимы либо равны)
         */
        fun compare(a: MapObject, b: MapObject) : Int {
            val rectA = a.getRectangle()
            val rectB = b.getRectangle()
            val aIsPoint = rectA.width == 0f && rectA.height == 0f
            val bIsPoint = rectB.width == 0f && rectB.height == 0f
            if (!aIsPoint && !bIsPoint && rectA.overlaps(rectB)) return 0
            when {
                aIsPoint && bIsPoint -> {
                    return -java.lang.Float.compare(getVirtualScreenPointFromScene(a.getPosition()).y,
                            getVirtualScreenPointFromScene(b.getPosition()).y)
                }
                aIsPoint && !bIsPoint -> {
                    if (rectA.overlaps(rectB)) return -1
                    return compare(Point(rectA.x, rectA.y), rectB)
                }
                !aIsPoint && bIsPoint -> {
                    if (rectA.overlaps(rectB)) return 1
                    return -compare(Point(rectB.x, rectB.y), rectA)
                }
                else -> {
                    var res = compare(Point(rectA.x, rectA.y), rectB)
                    if (res != 0) return res
                    res = compare(Point(rectA.x + rectA.width, rectA.y + rectA.height), rectB)
                    if (res != 0) return res
                    res = compare(Point(rectB.x, rectB.y), rectA)
                    if (res != 0) return -res
                    res = compare(Point(rectB.x + rectB.width, rectB.y + rectB.height), rectA)
                    if (res != 0) return -res
                    return 0
                }
            }
        }

        val q = ArrayDeque<MapObject>()
        val visited = BooleanArray(objs.size) {false}
        fun dfs(index: Int) {
            if (visited[index]) return
            visited[index] = true
            for (i in 0 until objs.size) {
                if (compare(objs[index], objs[i]) == 1) {
                    dfs(i)
                }
            }
            q.add(objs[index])
        }
        for (i in 0 until objs.size) {
            dfs(i)
        }
        for (i in 0 until objs.size) {
            objs[i] = q.pop()
        }
    }

    /**
     * Обновить размер виртуального экрана, вычислив его через ScreenSizeCalculator
     */
    override fun resize(width: Int, height: Int) {
        val viewportSize = ScreenSizeCalculator.calculateViewportSize(width.toFloat(), height.toFloat())
        scrW = viewportSize.width
        scrH = viewportSize.height
        camera.setToOrtho(false, scrW, scrH)
    }

}