package com.mirage.view.gameobjects

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.mirage.utils.datastructures.Point
import com.mirage.utils.game.objects.GameObject
import com.mirage.view.game.getVirtualScreenPointFromScene

class TestObjectFiller(private val obj: GameObject, private val camera: OrthographicCamera) : ObjectDrawer {

    private val sr = ShapeRenderer()

    override fun draw(batch: SpriteBatch, x: Float, y: Float) {
        batch.end()
        sr.begin(ShapeRenderer.ShapeType.Filled)
        val points = arrayOf(
                Point(obj.x, obj.y),
                Point(obj.x + obj.width, obj.y),
                Point(obj.x + obj.width, obj.y + obj.height),
                Point(obj.x, obj.y + obj.height)
        ).map {
            getVirtualScreenPointFromScene(it) -
                    Point(camera.position.x - camera.viewportWidth / 2,
                            camera.position.y - camera.viewportHeight / 2)
        }
        sr.triangle(points[0].x, points[0].y, points[1].x, points[1].y, points[2].x, points[2].y)
        sr.triangle(points[2].x, points[2].y, points[3].x, points[3].y, points[1].x, points[1].y)
        sr.end()
        batch.begin()
    }
}