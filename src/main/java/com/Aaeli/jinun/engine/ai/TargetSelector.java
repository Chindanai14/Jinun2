package com.Aaeli.jinun.engine.ai;

import com.Aaeli.jinun.managers.FriendManager;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class TargetSelector {
    private static final MinecraftClient mc = MinecraftClient.getInstance();

    // ตั้งค่าผ่าน JinunContext.init()
    private FriendManager friendManager;

    public void init(FriendManager friendManager) {
        this.friendManager = friendManager;
    }

    public PlayerEntity getBestTarget(ObjectArrayList<PlayerEntity> entities,
                                      double maxRange, float fovDegrees) {
        if (mc.player == null) return null;

        PlayerEntity best         = null;
        double       highestScore = -1.0;
        double       maxRangeSq   = maxRange * maxRange;
        Vec3d        lookVec      = mc.player.getRotationVec(1.0f);

        for (int i = 0; i < entities.size(); i++) {
            PlayerEntity p = entities.get(i);

            // ข้าม Friend
            if (friendManager != null
                    && friendManager.isFriend(p.getName().getString())) continue;

            double distSq = mc.player.squaredDistanceTo(p);
            if (distSq > maxRangeSq) continue;

            if (fovDegrees < 360f) {
                Vec3d dir = new Vec3d(
                        p.getX() - mc.player.getX(),
                        p.getY() - mc.player.getY(),
                        p.getZ() - mc.player.getZ()
                ).normalize();
                double dot       = lookVec.dotProduct(dir);
                double threshold = Math.cos(Math.toRadians(fovDegrees / 2.0));
                if (dot < threshold) continue;
            }

            if (!mc.player.canSee(p)) continue;

            double iFramePenalty = p.hurtTime > 0 ? p.hurtTime * 3.0 : 0;
            double dist          = Math.sqrt(distSq);
            double distScore     = (maxRange - dist) / maxRange * 40.0;
            double healthScore   = (20.0 - p.getHealth()) * 1.5;
            double armorScore    = (20   - p.getArmor())  * 0.5;
            double angleScore    = getAngleScore(p) * 2.0;

            double score = distScore + healthScore + armorScore + angleScore - iFramePenalty;
            if (score > highestScore) { highestScore = score; best = p; }
        }
        return best;
    }

    public PlayerEntity getBestTarget(ObjectArrayList<PlayerEntity> entities,
                                      double maxRange) {
        return getBestTarget(entities, maxRange, 360f);
    }

    private double getAngleScore(PlayerEntity target) {
        if (mc.player == null) return 0;
        float diff = Math.abs(MathHelper.wrapDegrees(getYawTo(target) - mc.player.getYaw()));
        return Math.max(0, 180 - diff);
    }

    private float getYawTo(PlayerEntity target) {
        double dx = target.getX() - mc.player.getX();
        double dz = target.getZ() - mc.player.getZ();
        return (float) Math.toDegrees(Math.atan2(dz, dx)) - 90f;
    }
}