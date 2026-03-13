package com.Aaeli.jinun.events;

import com.Aaeli.jinun.core.event.BaseEvent;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;

public final class EventRender3D extends BaseEvent {
    public static final int ID = 4;

    public final MatrixStack            matrices;
    public final VertexConsumerProvider consumers;
    public final Vec3d                  cameraPos;
    public final float                  tickDelta;

    public EventRender3D(MatrixStack matrices, VertexConsumerProvider consumers,
                         Vec3d cameraPos, float tickDelta) {
        this.matrices  = matrices;
        this.consumers = consumers;
        this.cameraPos = cameraPos;
        this.tickDelta = tickDelta;
    }

    @Override public int     getEventId()   { return ID; }
    @Override public boolean isCancelable() { return false; }
}