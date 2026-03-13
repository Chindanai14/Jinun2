package com.Aaeli.jinun.features.render;

import com.Aaeli.jinun.JinunContext;
import com.Aaeli.jinun.core.event.EventBus;
import com.Aaeli.jinun.events.EventRender3D;
import com.Aaeli.jinun.features.module.Category;
import com.Aaeli.jinun.features.module.Module;
import com.Aaeli.jinun.features.module.ModuleInfo;
import com.Aaeli.jinun.settings.types.BooleanSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

import java.util.function.Consumer;

@ModuleInfo(name = "ESP", category = Category.RENDER)
public class ESP extends Module {
    private static final MinecraftClient mc = MinecraftClient.getInstance();

    private final BooleanSetting box     = register(new BooleanSetting("Box",     true));
    private final BooleanSetting friends = register(new BooleanSetting("Friends", false));

    private final Consumer<EventRender3D> renderListener = this::onRender;

    public ESP(JinunContext ctx) { super(ctx); }

    @Override protected void bindEvents(EventBus bus)   { bus.bind(EventRender3D.ID, renderListener); }
    @Override protected void unbindEvents(EventBus bus) { bus.unbind(EventRender3D.ID, renderListener); }

    private void onRender(EventRender3D event) {
        if (mc.player == null || !box.get()) return;

        for (PlayerEntity player : ctx.entityTracker.players) {
            boolean isFriend = ctx.friendManager.isFriend(player.getName().getString());
            if (isFriend && !friends.get()) continue;

            float r = isFriend ? 0f : 1f;
            float g = isFriend ? 1f : 0f;
            renderBox(event, player, r, g, 0f);
        }
    }

    private void renderBox(EventRender3D event, PlayerEntity player,
                           float r, float g, float b) {
        Vec3d lerped = player.getLerpedPos(event.tickDelta);
        double x = lerped.x - event.cameraPos.x;
        double y = lerped.y - event.cameraPos.y;
        double z = lerped.z - event.cameraPos.z;

        float w = player.getWidth()  / 2f + 0.05f;
        float h = player.getHeight() + 0.05f;
        Box bb = new Box(x - w, y, z - w, x + w, y + h, z + w);

        // ใช้ managed buffer จาก Fabric — ไม่ต้องจัดการ RenderSystem เอง
        VertexConsumer lines = event.consumers.getBuffer(RenderLayer.getLines());
        MatrixStack.Entry entry = event.matrices.peek();
        Matrix4f mat = entry.getPositionMatrix();

        drawBox(lines, mat, bb, r, g, b, 1f);
    }

    private void drawBox(VertexConsumer vc, Matrix4f mat,
                         Box box, float r, float g, float b, float a) {
        float x1=(float)box.minX, y1=(float)box.minY, z1=(float)box.minZ;
        float x2=(float)box.maxX, y2=(float)box.maxY, z2=(float)box.maxZ;
        // Bottom
        line(vc,mat, x1,y1,z1, x2,y1,z1, r,g,b,a);
        line(vc,mat, x2,y1,z1, x2,y1,z2, r,g,b,a);
        line(vc,mat, x2,y1,z2, x1,y1,z2, r,g,b,a);
        line(vc,mat, x1,y1,z2, x1,y1,z1, r,g,b,a);
        // Top
        line(vc,mat, x1,y2,z1, x2,y2,z1, r,g,b,a);
        line(vc,mat, x2,y2,z1, x2,y2,z2, r,g,b,a);
        line(vc,mat, x2,y2,z2, x1,y2,z2, r,g,b,a);
        line(vc,mat, x1,y2,z2, x1,y2,z1, r,g,b,a);
        // Sides
        line(vc,mat, x1,y1,z1, x1,y2,z1, r,g,b,a);
        line(vc,mat, x2,y1,z1, x2,y2,z1, r,g,b,a);
        line(vc,mat, x2,y1,z2, x2,y2,z2, r,g,b,a);
        line(vc,mat, x1,y1,z2, x1,y2,z2, r,g,b,a);
    }

    private void line(VertexConsumer vc, Matrix4f mat,
                      float x1, float y1, float z1,
                      float x2, float y2, float z2,
                      float r,  float g,  float b, float a) {
        float dx=x2-x1, dy=y2-y1, dz=z2-z1;
        float len=(float)Math.sqrt(dx*dx+dy*dy+dz*dz);
        if (len == 0) return;
        vc.vertex(mat,x1,y1,z1).color(r,g,b,a).normal(dx/len,dy/len,dz/len);
        vc.vertex(mat,x2,y2,z2).color(r,g,b,a).normal(dx/len,dy/len,dz/len);
    }
}