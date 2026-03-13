package com.Aaeli.jinun.engine.rotation;

import net.minecraft.client.MinecraftClient;

public class RotationEngine {
    private static final MinecraftClient mc = MinecraftClient.getInstance();

    public enum Slot {
        RENDER(0), MOVEMENT(1), COMBAT(2), OVERRIDE(3);
        public final int level;
        Slot(int level) { this.level = level; }
    }

    // Pre-allocated pool — 0 GC
    private final RotationData[]  slots    = new RotationData[Slot.values().length];
    private final float[][]       speeds   = new float[Slot.values().length][2]; // [speed, jitter]
    private final RotationSmoother smoother = new RotationSmoother();

    public RotationEngine() {
        for (int i = 0; i < slots.length; i++)
            slots[i] = new RotationData(0, 0);
    }

    /** Instant snap */
    public void request(Slot slot, float yaw, float pitch) {
        request(slot, yaw, pitch, 1.0f, 0f);
    }

    /** Smooth + humanized */
    public void request(Slot slot, float yaw, float pitch,
                        float speed, float jitter) {
        RotationData d = slots[slot.level];
        d.set(yaw, pitch);
        d.active         = true;
        speeds[slot.level][0] = speed;
        speeds[slot.level][1] = jitter;
    }

    public float[] getActiveRotation() {
        for (int i = slots.length - 1; i >= 0; i--) {
            if (!slots[i].active) continue;

            slots[i].active = false;
            float speed  = speeds[i][0];
            float jitter = speeds[i][1];
            float[] result;

            if (speed < 1.0f || jitter > 0f) {
                result = smoother.smooth(slots[i].yaw, slots[i].pitch, speed, jitter);
                result = applyGCD(result[0], result[1]);
            } else {
                result = applyGCD(slots[i].yaw, slots[i].pitch);
                smoother.reset();
            }
            return result;
        }
        smoother.reset();
        return null;
    }

    private float[] applyGCD(float yaw, float pitch) {
        float f   = (float)(mc.options.getMouseSensitivity().getValue() * 0.6F + 0.2F);
        float gcd = f * f * f * 1.2F;
        return new float[]{
                yaw - (yaw % gcd),
                Math.max(-90, Math.min(90, pitch - (pitch % gcd)))
        };
    }
}