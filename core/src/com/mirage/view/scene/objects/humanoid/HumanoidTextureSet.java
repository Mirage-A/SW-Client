package com.mirage.view.scene.objects.humanoid;

public class HumanoidTextureSet {
    protected AnimatedTexture head, body, hand, leg, cloak, weapon1, weapon2;

    public HumanoidTextureSet(AnimatedTexture head, AnimatedTexture body, AnimatedTexture hand,
                              AnimatedTexture leg, AnimatedTexture cloak, AnimatedTexture weapon1, AnimatedTexture weapon2) {
        this.head = head;
        this.body = body;
        this.hand = hand;
        this.leg = leg;
        this.cloak = cloak;
        this.weapon1 = weapon1;
        this.weapon2 = weapon2;
    }
}
