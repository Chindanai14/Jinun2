package com.Aaeli.jinun.engine.combat;

import com.Aaeli.jinun.events.EventTickStart;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.Hand;

public class AttackEngine {
    private static final MinecraftClient mc = MinecraftClient.getInstance();

    private int tickDelay = 0; // CPS limiter

    /** เรียกทุก tick เพื่อลด CPS delay counter */
    public void onTick(EventTickStart event) {
        if (tickDelay > 0) tickDelay--;
    }

    /** vanilla cooldown เต็มหรือยัง */
    public boolean canAttack() {
        if (mc.player == null) return false;
        return mc.player.getAttackCooldownProgress(0f) >= 1.0f;
    }

    /** ตีครั้งนี้จะเป็น crit hit ไหม */
    public boolean isCriticalReady() {
        if (mc.player == null) return false;
        return !mc.player.isOnGround()
                && mc.player.fallDistance > 0f
                && !mc.player.isTouchingWater()
                && !mc.player.isInLava()
                && !mc.player.hasStatusEffect(StatusEffects.BLINDNESS);
    }

    /** ตีทันที (ไม่เช็ค cooldown) */
    public void attack(LivingEntity target) {
        if (mc.player == null || mc.interactionManager == null) return;
        mc.interactionManager.attackEntity(mc.player, target);
        mc.player.swingHand(Hand.MAIN_HAND);
        // CPS randomization: รอ 1-2 ticks (~10-14 CPS)
        tickDelay = 1 + (int)(Math.random() * 2);
    }

    /** ตีถ้า cooldown พร้อม + CPS delay หมดแล้ว → return true ถ้าตีได้ */
    public boolean tryAttack(LivingEntity target) {
        if (tickDelay > 0 || !canAttack()) return false;
        attack(target);
        return true;
    }
}