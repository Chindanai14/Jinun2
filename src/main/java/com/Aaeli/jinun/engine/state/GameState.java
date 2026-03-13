package com.Aaeli.jinun.engine.state;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;

public class GameState {
    private static final MinecraftClient mc = MinecraftClient.getInstance();

    public double  x, y, z;
    public double  prevX, prevY, prevZ;
    public float   yaw, pitch;
    public float   health, maxHealth;
    public int     food;
    public boolean onGround, sprinting;
    public long    tickTime;
    public int     hurtTime;
    public float   fallDistance;
    public int     ping;

    public void update() {
        ClientPlayerEntity p = mc.player;
        if (p == null) return;

        prevX = x; prevY = y; prevZ = z;

        x            = p.getX();
        y            = p.getY();
        z            = p.getZ();
        yaw          = p.getYaw();
        pitch        = p.getPitch();
        health       = p.getHealth();
        maxHealth    = p.getMaxHealth();
        food         = p.getHungerManager().getFoodLevel();
        onGround     = p.isOnGround();
        sprinting    = p.isSprinting();
        tickTime     = System.currentTimeMillis();
        hurtTime     = p.hurtTime;
        fallDistance = (float) p.fallDistance; // explicit cast แก้ lossy conversion

        ClientPlayNetworkHandler net = mc.getNetworkHandler();
        if (net != null && net.getPlayerListEntry(p.getUuid()) != null) {
            ping = net.getPlayerListEntry(p.getUuid()).getLatency();
        }
    }

    public double velX() { return x - prevX; }
    public double velY() { return y - prevY; }
    public double velZ() { return z - prevZ; }
}