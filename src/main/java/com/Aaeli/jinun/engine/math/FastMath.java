package com.Aaeli.jinun.engine.math;

public class FastMath {
    private static final float PI = (float) Math.PI;

    public static float atan2(float y, float x) {
        float absY = Math.abs(y) + 1e-10f;
        float r, angle;
        if (x >= 0) {
            r     = (x - absY) / (x + absY);
            angle = PI / 4.0f;
        } else {
            r     = (x + absY) / (absY - x);
            angle = 3.0f * PI / 4.0f;
        }
        angle += (0.1963f * r * r - 0.9817f) * r;
        return y < 0 ? -angle : angle;
    }
}