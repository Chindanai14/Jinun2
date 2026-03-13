package com.Aaeli.jinun.engine.ai;

import com.Aaeli.jinun.events.EventTickStart;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;

import java.util.List;

public class EntityTracker {
    private static final MinecraftClient mc = MinecraftClient.getInstance();
    private static final double RANGE_SQ   = 150.0 * 150.0;

    public final ObjectArrayList<PlayerEntity> players = new ObjectArrayList<>(128);
    private int ticks = 0;

    public void onTick(EventTickStart event) {
        if (mc.world == null || mc.player == null) return;

        if (++ticks >= 20) {
            // Full rebuild ทุก 20 ticks
            players.clear();
            List<net.minecraft.client.network.AbstractClientPlayerEntity> all =
                    mc.world.getPlayers();
            for (int i = 0; i < all.size(); i++) {
                PlayerEntity p = all.get(i);
                if (p != mc.player && p.isAlive()) players.add(p);
            }
            ticks = 0;
        } else {
            // O(1) Swap-Remove — squaredDistanceTo (ไม่ใช้ sqrt)
            for (int i = players.size() - 1; i >= 0; i--) {
                PlayerEntity p = players.get(i);
                if (p.isRemoved() || !p.isAlive()
                        || mc.player.squaredDistanceTo(p) > RANGE_SQ) {
                    int last = players.size() - 1;
                    players.set(i, players.get(last));
                    players.remove(last);
                }
            }
        }
    }
}