package com.mirage.view.gameobjects

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.maps.MapObject
import com.badlogic.gdx.math.Rectangle
import com.mirage.model.datastructures.Point
import com.mirage.model.extensions.getRectangle
import com.mirage.model.extensions.points
import com.mirage.view.getScenePointFromVirtualScreen
import com.mirage.view.getVirtualScreenPointFromScene
import com.mirage.view.screens.GameScreen

class TestObjectFiller(val obj: MapObject, val camera: OrthographicCamera) : ObjectDrawer {

    val sr = ShapeRenderer()

    override fun draw(batch: SpriteBatch, x: Float, y: Float) {
        batch.end()
        sr.begin(ShapeRenderer.ShapeType.Filled)
        val rect = obj.getRectangle()
        /*val trRect = Rectangle(- rect.width / 2, - rect.height / 2, rect.width, rect.height)
        val points = trRect.points().map { getVirtualScreenPointFromScene(it) + Point(x, y) }*/
        val points = rect.points().map { getVirtualScreenPointFromScene(it) - Point(camera.position.x - camera.viewportWidth / 2, camera.position.y - camera.viewportHeight / 2)}
        sr.triangle(points[0].x, points[0].y, points[1].x, points[1].y, points[2].x, points[2].y)
        sr.triangle(points[2].x, points[2].y, points[3].x, points[3].y, points[1].x, points[1].y)
        sr.end()
        batch.begin()
    }
}