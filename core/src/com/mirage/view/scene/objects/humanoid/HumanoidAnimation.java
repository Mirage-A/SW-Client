package com.mirage.view.scene.objects.humanoid;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mirage.view.scene.objects.Animation;

public abstract class HumanoidAnimation extends Animation {
    protected Texture head, body, hand, leg, cloak, weapon1, weapon2;

    public HumanoidAnimation(Texture head, Texture body, Texture hand, Texture leg, Texture cloak, Texture weapon1, Texture weapon2) {
        this.head = head;
        this.body = body;
        this.hand = hand;
        this.leg = leg;
        this.cloak = cloak;
        this.weapon1 = weapon1;
        this.weapon2 = weapon2;
    }

    @Override
    protected void draw(SpriteBatch batch, float x, float y, long timePassed) {
        
    }
}
