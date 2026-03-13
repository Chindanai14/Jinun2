package com.Aaeli.jinun.engine.ai;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

public class PredictionEngine {
    // Minecraft ground drag constant
    private static final double DRAG = 0.91;

    /**
     * Predict position N ticks ahead
     * ใช้ velocity × drag เพื่อให้แม่นกว่า linear
     */
    public Vec3d predict(Entity entity, int ticks) {
        Vec3d vel = entity.getVelocity();

        double vx = vel.x * DRAG;
        double vy = vel.y; // gravity จัดการแยก ไม่ใช้ drag แกน Y
        double vz = vel.z * DRAG;

        double px = entity.getX();
        double py = entity.getY();
        double pz = entity.getZ();

        for (int i = 0; i < ticks; i++) {
            px += vx;
            py += vy;
            pz += vz;
            vx *= DRAG;
            vz *= DRAG;
            vy -= 0.08; // gravity per tick
            vy *= 0.98; // air drag แกน Y
        }

        return new Vec3d(px, py, pz);
    }

    /** Predict พร้อม latency compensation */
    public Vec3d predictWithLatency(Entity entity, int latencyMs) {
        return predict(entity, Math.max(1, latencyMs / 50));
    }

    /** คำนวณ yaw/pitch จาก point A → point B */
    public float[] getAnglesTo(double fromX, double fromY, double fromZ,
                               double toX,   double toY,   double toZ) {
        double dx   = toX - fromX;
        double dy   = toY - fromY;
        double dz   = toZ - fromZ;
        double dist = Math.sqrt(dx * dx + dz * dz);
        float  yaw  = (float) Math.toDegrees(Math.atan2(dz, dx)) - 90f;
        float  pit  = (float) -Math.toDegrees(Math.atan2(dy, dist));
        return new float[]{ yaw, pit };
    }
}