package com.Aaeli.jinun.features.combat;

import com.Aaeli.jinun.JinunContext;
import com.Aaeli.jinun.core.event.EventBus;
import com.Aaeli.jinun.engine.rotation.RotationEngine;
import com.Aaeli.jinun.events.EventTickStart;
import com.Aaeli.jinun.features.module.Category;
import com.Aaeli.jinun.features.module.Module;
import com.Aaeli.jinun.features.module.ModuleInfo;
import com.Aaeli.jinun.settings.types.BooleanSetting;
import com.Aaeli.jinun.settings.types.EnumSetting;
import com.Aaeli.jinun.settings.types.NumberSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

import java.util.function.Consumer;

@ModuleInfo(
        name       = "Killaura",
        category   = Category.COMBAT,
        defaultKey = InputUtil.GLFW_KEY_R  // กด R เพื่อ toggle
)
public class Killaura extends Module {
    private static final MinecraftClient mc = MinecraftClient.getInstance();

    public enum AuraMode { SINGLE, SWITCH }

    private final EnumSetting<AuraMode> mode     = register(new EnumSetting<>("Mode", AuraMode.SINGLE));
    private final NumberSetting  range            = register(new NumberSetting("Range",     4.0,  1.0, 6.0));
    private final NumberSetting  fov              = register(new NumberSetting("FOV",     360.0, 30.0, 360.0));
    private final NumberSetting  rotSpeed         = register(new NumberSetting("RotSpeed",  0.5,  0.1, 1.0));
    private final BooleanSetting rotate           = register(new BooleanSetting("Rotations", true));
    private final BooleanSetting predict          = register(new BooleanSetting("Predict",   true));
    private final BooleanSetting critOnly         = register(new BooleanSetting("CritOnly",  false));

    private final Consumer<EventTickStart> tickListener = this::onTick;

    // SWITCH mode — เปลี่ยน target ทุก X ticks
    private int switchTimer = 0;
    private static final int SWITCH_INTERVAL = 5;

    public Killaura(JinunContext ctx) { super(ctx); }

    @Override protected void bindEvents(EventBus bus)   { bus.bind(EventTickStart.ID, tickListener); }
    @Override protected void unbindEvents(EventBus bus) { bus.unbind(EventTickStart.ID, tickListener); }

    @Override
    protected void onEnable() {
        switchTimer = 0;
    }

    @Override
    protected void onWorldLeave() {
        setEnabled(false);
    }

    private void onTick(EventTickStart event) {
        if (mc.player == null) return;

        PlayerEntity target = getTarget();
        if (target == null) return;

        // Rotation
        if (rotate.get()) {
            double tx = target.getX();
            double ty = target.getY() + target.getHeight() * 0.8;
            double tz = target.getZ();

            if (predict.get()) {
                Vec3d pred = ctx.prediction.predict(target, 1);
                tx = pred.x;
                ty = pred.y + target.getHeight() * 0.8;
                tz = pred.z;
            }

            float[] angles = ctx.prediction.getAnglesTo(
                    mc.player.getX(), mc.player.getEyeY(), mc.player.getZ(),
                    tx, ty, tz
            );

            ctx.rotationEngine.request(
                    RotationEngine.Slot.COMBAT,
                    angles[0], angles[1],
                    rotSpeed.get().floatValue(),
                    0.3f
            );
        }

        // Attack
        if (critOnly.get()) {
            if (ctx.attackEngine.canAttack() && ctx.attackEngine.isCriticalReady())
                ctx.attackEngine.attack(target);
        } else {
            ctx.attackEngine.tryAttack(target);
        }

        // SWITCH mode counter
        if (mode.get() == AuraMode.SWITCH) switchTimer++;
    }

    private PlayerEntity getTarget() {
        if (mode.get() == AuraMode.SWITCH) {
            // ทุก SWITCH_INTERVAL ticks เปลี่ยน target โดยใช้ fov กว้างขึ้น
            if (switchTimer >= SWITCH_INTERVAL) {
                switchTimer = 0;
                return ctx.targetSelector.getBestTarget(
                        ctx.entityTracker.players, range.get(), 360f);
            }
        }
        return ctx.targetSelector.getBestTarget(
                ctx.entityTracker.players,
                range.get(),
                fov.get().floatValue()
        );
    }
}