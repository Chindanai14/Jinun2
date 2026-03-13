package com.Aaeli.jinun.engine.rotation;

import net.minecraft.util.math.MathHelper;

import java.util.Random;

public class RotationSmoother {
    private float   currentYaw, currentPitch;
    private boolean initialized = false;
    private final Random rng = new Random();

    /**
     * @param speed  0.1=slow/smooth  1.0=instant snap
     * @param jitter 0.0=no noise     1.0=max humanization
     */
    public float[] smooth(float targetYaw, float targetPitch,
                          float speed,     float jitter) {
        if (!initialized) {
            currentYaw   = targetYaw;
            currentPitch = targetPitch;
            initialized  = true;
        }

        // Micro-overshoot: เกินเป้าเล็กน้อยแล้วดึงกลับ (เหมือนมือคน)
        float diffYaw   = MathHelper.wrapDegrees(targetYaw   - currentYaw);
        float diffPitch = MathHelper.wrapDegrees(targetPitch - currentPitch);

        float overshootYaw   = diffYaw   * 0.05f * (rng.nextFloat() * 0.6f + 0.7f);
        float overshootPitch = diffPitch * 0.05f * (rng.nextFloat() * 0.6f + 0.7f);

        // Gaussian noise (hand trembling)
        float noiseY = (float)(rng.nextGaussian() * jitter * 0.12);
        float noiseP = (float)(rng.nextGaussian() * jitter * 0.06);

        currentYaw   = lerp(currentYaw,   targetYaw   + overshootYaw   + noiseY, speed);
        currentPitch = lerp(currentPitch, targetPitch + overshootPitch + noiseP, speed);
        currentPitch = MathHelper.clamp(currentPitch, -90f, 90f);

        return new float[]{ currentYaw, currentPitch };
    }

    public void reset() { initialized = false; }

    private float lerp(float a, float b, float t) {
        return a + MathHelper.wrapDegrees(b - a) * t;
    }
}