package com.mirage.ui

/*
class GameScreen(private val stateManager: SnapshotManager, private val gameMap: GameMap, private val playerID: Long): ScreenAdapter() {

    private val batch: SpriteBatch = SpriteBatch()
    var camera: OrthographicCamera = OrthographicCamera()

    companion object {
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

}*/