package com.mirage.view

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch

/**
 * Основной класс - визуальный экран
 */
abstract class View {

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
    }

    /**
     * Реальные размеры экрана при последнем вызове render
     * Если они изменились, то автоматически вызывается setScreenSize
     */
    protected var lastRealScreenWidth = 0f
    protected var lastRealScreenHeight = 0f

    val batch: SpriteBatch = SpriteBatch()
    var camera: OrthographicCamera = OrthographicCamera()

    /**
     * Отрисовка экрана
     */
    abstract fun render()

    open fun dispose() {
        batch.dispose()
    }

    /**
     * Метод, который должен вызываться при изменении параметров реального экрана.
     * @param realWidth Ширина реального экрана
     * @param realHeight Высота реального экрана
     */
    open fun setScreenSize(realWidth: Float, realHeight: Float) {
        lastRealScreenWidth = realWidth
        lastRealScreenHeight = realHeight
    }


}
