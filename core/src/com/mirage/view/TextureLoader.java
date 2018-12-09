package com.mirage.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

/**
 * Загружает текстуры из папки android/assets и применяет к ним заданные фильтры
 */
public class TextureLoader {
    private static final Texture.TextureFilter MIN_FILTER = Texture.TextureFilter.MipMapNearestNearest;
    private static final Texture.TextureFilter MAG_FILTER = Texture.TextureFilter.MipMapNearestNearest;

    public static Texture load(String path) {
        Texture t = new Texture(Gdx.files.internal("android/assets/" + path), true);
        t.setFilter(MIN_FILTER, MAG_FILTER);
        return t;
    }
}
