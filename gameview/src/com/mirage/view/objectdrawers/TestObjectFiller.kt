package com.mirage.view.objectdrawers

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.mirage.utils.Assets
import com.mirage.utils.datastructures.Point
import com.mirage.utils.game.objects.GameObject
import com.mirage.view.utils.getVirtualScreenPointFromScene

class TestObjectFiller(private val obj: GameObject) : ObjectDrawer {

    private val sr = ShapeRenderer()

    override fun draw(batch: SpriteBatch, x: Float, y: Float) {
        //TODO Без знания текуших размеров вирт. экрана никак нормально не отрендерить
        //TODO Наверное, нужно просто ничего не рисовать
        batch.end()
        sr.begin(ShapeRenderer.ShapeType.Filled)
        val points = arrayOf(
                Point(obj.x - obj.width / 2, obj.y - obj.height / 2),
                Point(obj.x + obj.width / 2, obj.y - obj.height / 2),
                Point(obj.x + obj.width / 2, obj.y + obj.height / 2),
                Point(obj.x - obj.width / 2, obj.y + obj.height / 2)
        ).map {
            getVirtualScreenPointFromScene(it)// + Point(camera.viewportWidth / 2, camera.viewportHeight / 2)
        }
        sr.triangle(points[0].x, points[0].y, points[1].x, points[1].y, points[2].x, points[2].y)
        sr.triangle(points[2].x, points[2].y, points[3].x, points[3].y, points[0].x, points[0].y)
        sr.end()
        batch.begin()

    }
}