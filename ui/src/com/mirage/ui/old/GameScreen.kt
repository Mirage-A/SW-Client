package com.mirage.ui.old

/*
class GameScreen(private val stateManager: SnapshotManager, private val gameMap: GameMap, private val playerID: Long): ScreenAdapter() {

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
     * Отрисовка экрана
     */
    override fun render(delta: Float) {

        val objs = stateManager.getInterpolatedSnapshot()
        drawers.updateDrawers(objs.findDifferenceWith(lastRenderedObjs))

        batch.begin()
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

}*/