package com.mirage.view.scene.objects.humanoid;

import com.badlogic.gdx.graphics.Texture;

public class StaticTexture extends AnimatedTexture {
    private Texture texture;

    public StaticTexture(Texture texture) {
        this.texture = texture;
    }

    @Override
    public Texture getTexture(long timePassed) {
        return texture;
    }
}
