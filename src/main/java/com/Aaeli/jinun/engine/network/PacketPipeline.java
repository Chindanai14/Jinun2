package com.Aaeli.jinun.engine.network;

import com.Aaeli.jinun.JinunContext;
import com.Aaeli.jinun.events.EventPacket;
import com.Aaeli.jinun.mixin.PlayerMoveC2SPacketAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

public class PacketPipeline {
    private final JinunContext ctx;

    public PacketPipeline(JinunContext ctx) { this.ctx = ctx; }

    public void onPacket(EventPacket event) {
        if (event.getStage() != EventPacket.Stage.MODIFY) return;
        if (event.isInbound) { handleInbound(event); }
        else                  { handleOutbound(event); }
    }

    private void handleOutbound(EventPacket event) {
        if (!(event.getPacket() instanceof PlayerMoveC2SPacket move)) return;
        float[] rots = ctx.rotationEngine.getActiveRotation();
        if (rots == null) return;

        boolean isLook = move instanceof PlayerMoveC2SPacket.LookAndOnGround;
        boolean isFull = move instanceof PlayerMoveC2SPacket.Full;

        if (isLook || isFull) {
            ((PlayerMoveC2SPacketAccessor) move).setYaw(rots[0]);
            ((PlayerMoveC2SPacketAccessor) move).setPitch(rots[1]);
        } else {
            event.cancel();
            MinecraftClient.getInstance().getNetworkHandler().sendPacket(
                    new PlayerMoveC2SPacket.Full(
                            move.getX(0), move.getY(0), move.getZ(0),
                            rots[0], rots[1],
                            move.isOnGround(),
                            false // horizontalCollision — เพิ่มใน 1.21.11
                    )
            );
        }
    }

    private void handleInbound(EventPacket event) {
        // Hook สำหรับ inbound packets
        // if (event.getPacket() instanceof EntityVelocityUpdateS2CPacket vel) { ... }
    }
}