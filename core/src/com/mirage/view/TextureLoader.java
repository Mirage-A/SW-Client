package com.mirage.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.mirage.controller.Platform;

/**
 * Загружает текстуры из папки ASSETS_PATH и применяет к ним заданные фильтры
 */
public class TextureLoader {
    private static final Texture.TextureFilter MIN_FILTER = Texture.TextureFilter.MipMapLinearNearest;
    private static final Texture.TextureFilter MAG_FILTER = Texture.TextureFilter.MipMapLinearNearest;

    public static Texture load(String path) {
        Texture t = new Texture(Gdx.files.internal(Platform.ASSETS_PATH + path), true);
        t.setFilter(MIN_FILTER, MAG_FILTER);
        return t;
    }
}
