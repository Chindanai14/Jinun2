package com.Aaeli.jinun.engine.math;

public class MathLUT {
    private static final int   SIN_BITS  = 16;
    private static final int   SIN_MASK  = ~(-1 << SIN_BITS);
    private static final int   SIN_COUNT = SIN_MASK + 1;
    private static final float radFull   = (float)(Math.PI * 2.0);
    private static final float radToIdx  = SIN_COUNT / radFull;
    private static final float[] SIN     = new float[SIN_COUNT];

    static {
        for (int i = 0; i < SIN_COUNT; i++)
            SIN[i] = (float) Math.sin((i + 0.5f) / SIN_COUNT * radFull);
    }

    public static float sin(float rad) { return SIN[(int)(rad * radToIdx) & SIN_MASK]; }
    public static float cos(float rad) { return SIN[(int)(rad * radToIdx + SIN_COUNT / 4) & SIN_MASK]; }
}