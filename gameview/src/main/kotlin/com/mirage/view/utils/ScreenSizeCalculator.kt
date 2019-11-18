package com.mirage.view.utils

import com.badlogic.gdx.math.Rectangle
import com.mirage.utils.*


/**
     * Вычисляет оптимальный размер виртуального экрана так,
     * чтобы размер тайлов после скейла под экран был равен вычисленному
     * с помощью calculateTileSize,
     * при этом гарантируется, что и ширина, и высота виртуального экрана обязательно четные.
     * @param realWidth Ширина реального экрана
     * @param realHeight Высота реального экрана
     * @return Оптимальный размер виртуального экрана
     */
    fun calculateViewportSize(realWidth: Float, realHeight: Float): Rectangle {
        val tileSize = calculateTileSize(realWidth, realHeight)
        val width = evenRound(realWidth / tileSize.width * TILE_WIDTH).toFloat()
        val height = evenRound(realHeight / tileSize.height * TILE_HEIGHT).toFloat()
        Log.i("Размеры виртуального экрана: $width x $height px")
        return Rectangle(0f, 0f, width, height)
    }

    /**
     * Вычисляет размер одного тайла на экране в зависимости от параметров экрана.
     * Размеры тайла подбираются таким образом, чтобы площадь (в тайлах)
     * симметрической разности эталонного и полученного обзоров была минимальна.
     * Учитывается, что отношение ширины и высоты тайла равно 2,
     * при этом гарантируется, что и ширина, и высота обязательно четные.
     * @param realWidth Ширина реального экрана
     * @param realHeight Высота реального экрана
     * @return Оптимальный размер одного тайла (хранится как ширина и высота прямоугольника)
     */
    fun calculateTileSize(realWidth: Float, realHeight: Float): Rectangle {
        // Используем тернарный поиск по высоте тайла
        var L = 0f
        var R = 512f
        while (R - L > 0.01f) {
            val mid1 = L + (R - L) / 3
            val mid2 = L + (R - L) * 2 / 3
            val diff1 = visionsSymmetricDifference(mid1, realWidth, realHeight)
            val diff2 = visionsSymmetricDifference(mid2, realWidth, realHeight)
            if (diff1 < diff2) {
                R = mid2
            } else {
                L = mid1
            }
        }
        val tileH = evenRound(L)
        val res = Rectangle(0f, 0f, (2 * tileH).toFloat(), tileH.toFloat())

        Log.i("Размеры экрана: $realWidth x $realHeight px")
        Log.i("Размеры тайла: " + res.width + " x " + res.height + " px")
        Log.i("Кол-во тайлов на экране: " + realWidth / res.width + " x " + realHeight / res.height)

        return res
    }

    /**
     * Вспомогательная функция, необходимая для тернарного поиска в calculateTileSize.
     * По данной высоте тайла вычисляет площадь симметрической разности эталонного и
     * возможного при данной высоте обзора.
     * @param tileH Высота тайла
     * @param realWidth Ширина реального экрана
     * @param realHeight Высота реального экрана
     * @return Площадь симметрической разности обзоров
     */
    private fun visionsSymmetricDifference(tileH: Float, realWidth: Float, realHeight: Float): Float {
        val w = realWidth / (2 * tileH)
        val h = realHeight / tileH
        val dw = DEFAULT_SCREEN_WIDTH / TILE_WIDTH
        val dh = DEFAULT_SCREEN_HEIGHT / TILE_HEIGHT

        if (w <= dw && h <= dh) {
            return dw * dh - w * h
        }
        return if (dw <= w && dh <= h) {
            w * h - dw * dh
        } else Math.abs(w - dw) * Math.min(h, dh) + Math.abs(h - dh) * Math.min(w, dw)
    }

    /**
     * Вспомогательная функция, необходимая в calculateTileSize.
     * Округляет число a до ближайшего четного целого числа.
     * Если a - целое нечетное, то округление вверх.
     * @param a Число
     * @return Ближайшее четное целое число
     */
    private fun evenRound(a: Float): Int {
        val res = Math.round(a)
        if (res % 2 == 0) {
            return res
        }
        return if (a < res) {
            res - 1
        } else res + 1
    }
