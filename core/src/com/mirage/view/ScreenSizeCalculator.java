package com.mirage.view;

import com.badlogic.gdx.math.Rectangle;

public class ScreenSizeCalculator {

    /**
     * Вычисляет оптимальный размер виртуального экрана так,
     * чтобы размер тайлов после скейла под экран был равен вычисленному
     * с помощью calculateTileSize,
     * при этом гарантируется, что и ширина, и высота виртуального экрана обязательно четные.
     * @param realWidth Ширина реального экрана
     * @param realHeight Высота реального экрана
     * @return Оптимальный размер виртуального экрана
     */
    public static Rectangle calculateViewportSize(float realWidth, float realHeight) {
        Rectangle tileSize = calculateTileSize(realWidth, realHeight);
        float width = evenRound(realWidth / tileSize.width * View.TILE_WIDTH);
        float height = evenRound(realHeight / tileSize.height * View.TILE_HEIGHT);
        Log.i("Размеры виртуального экрана: " + width + " x " + height + " px");
        return new Rectangle(0, 0, width, height);
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
    public static Rectangle calculateTileSize(float realWidth, float realHeight) {
        // Используем тернарный поиск по высоте тайла
        float L = 0f;
        float R = 512f;
        while (R - L > 0.01f) {
            float mid1 = L + (R - L) / 3;
            float mid2 = L + (R - L) * 2 / 3;
            float diff1 = visionsSymmetricDifference(mid1, realWidth, realHeight);
            float diff2 = visionsSymmetricDifference(mid2, realWidth, realHeight);
            if (diff1 < diff2) {
                R = mid2;
            }
            else {
                L = mid1;
            }
        }
        int tileH = evenRound(L);
        Rectangle res = new Rectangle(0, 0, 2 * tileH, tileH);

        Log.i("Размеры экрана: " + realWidth + " x " + realHeight + " px");
        Log.i("Размеры тайла: " + res.width + " x " + res.height + " px");
        Log.i("Кол-во тайлов на экране: " + realWidth/res.width + " x " + realHeight/res.height);

        return res;
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
    private static float visionsSymmetricDifference(float tileH, float realWidth, float realHeight) {
        float w = realWidth / (2 * tileH);
        float h = realHeight / tileH;
        float dw = View.DEFAULT_SCREEN_WIDTH / View.TILE_WIDTH;
        float dh = View.DEFAULT_SCREEN_HEIGHT / View.TILE_HEIGHT;

        if (w <= dw && h <= dh) {
            return dw * dh - w * h;
        }
        if (dw <= w && dh <= h) {
            return w * h - dw * dh;
        }
        return Math.abs(w - dw) * Math.min(h, dh) +
                Math.abs(h - dh) * Math.min(w, dw);
    }

    /**
     * Вспомогательная функция, необходимая в calculateTileSize.
     * Округляет число a до ближайшего четного целого числа.
     * Если a - целое нечетное, то округление вверх.
     * @param a Число
     * @return Ближайшее четное целое число
     */
    private static int evenRound(float a) {
        int res = Math.round(a);
        if (res % 2 == 0) {
            return res;
        }
        if (a < res) {
            return res - 1;
        }
        return res + 1;
    }
}
