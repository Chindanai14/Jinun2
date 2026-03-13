package com.Aaeli.jinun.events;

import com.Aaeli.jinun.core.event.BaseEvent;
import net.minecraft.network.packet.Packet;

public final class EventPacket extends BaseEvent {
    public static final int ID = 2;

    private static final ThreadLocal<EventPacket> INSTANCE =
            ThreadLocal.withInitial(EventPacket::new);

    public enum Stage { READ, PRE, MODIFY, CANCEL, SEND }

    private Packet<?> packet;
    private Stage     stage;
    public  boolean   isInbound;

    private EventPacket() {}

    public static EventPacket obtain(Packet<?> packet, Stage stage, boolean isInbound) {
        EventPacket e = INSTANCE.get();
        e.packet     = packet;
        e.stage      = stage;
        e.isInbound  = isInbound;
        e.setCancelled(false);
        return e;
    }

    @Override public int     getEventId()   { return ID; }
    @Override public boolean isCancelable() { return true; }

    public Packet<?> getPacket() { return packet; }
    public Stage     getStage()  { return stage; }
}