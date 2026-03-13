package com.Aaeli.jinun.engine.combat;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;

public class CriticalEngine {
    private static final MinecraftClient mc = MinecraftClient.getInstance();

    private boolean jumping  = false;
    private int     jumpTick = 0;

    /** เรียกทุก tick เพื่อ force crit window ด้วยการ jump timing */
    public void tick() {
        ClientPlayerEntity p = mc.player;
        if (p == null || !p.isOnGround()) return;

        if (!jumping) {
            p.jump();
            jumping  = true;
            jumpTick = 0;
        } else if (++jumpTick > 10) {
            jumping = false; // reset ถ้า stuck
        }
    }

    /** true = ตอนนี้อยู่ใน falling phase (optimal crit window) */
    public boolean isAtCritWindow() {
        ClientPlayerEntity p = mc.player;
        if (p == null) return false;
        return !p.isOnGround()
                && p.fallDistance > 0f
                && p.getVelocity().y < 0;
    }
}