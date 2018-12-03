package com.mirage.view.scene.objects.humanoid;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mirage.view.scene.objects.Animation;

public class HumanoidDrawer extends Animation {
    private HumanoidTextureSet textures;
    private MoveDirection moveDirection;
    private Action action;

    public HumanoidDrawer(HumanoidTextureSet textures) {
        this.textures = textures;
        moveDirection = MoveDirection.DOWN;
        action = Action.IDLE;
    }

    public void setMoveDirection(MoveDirection moveDirection) {
        this.moveDirection = moveDirection;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public void setTextureSet(HumanoidTextureSet textures) {
        this.textures = textures;
    }

    @Override
    protected void draw(SpriteBatch batch, float x, float y, long timePassed) {
        batch.draw(new Texture(Gdx.files.internal("android/assets/tiles/0000.png")), x, y);
    }
}
