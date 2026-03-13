package com.Aaeli.jinun.core.event;

import com.Aaeli.jinun.Jinun;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;

public class EventBus {
    private static final int MAX_EVENTS   = 256;
    private static final int MAX_PRIORITY = Priority.values().length;

    @SuppressWarnings("unchecked")
    private final Consumer<BaseEvent>[][][] table =
            new Consumer[MAX_EVENTS][MAX_PRIORITY][0];

    private final ConcurrentLinkedQueue<BaseEvent> pendingQueue =
            new ConcurrentLinkedQueue<>();

    public <T extends BaseEvent> void bind(int eventId, Consumer<T> listener) {
        bind(eventId, Priority.NORMAL, listener);
    }

    @SuppressWarnings("unchecked")
    public <T extends BaseEvent> void bind(int eventId, Priority priority,
                                           Consumer<T> listener) {
        Consumer<BaseEvent>[] curr = table[eventId][priority.level];
        if (curr == null) curr = new Consumer[0];
        Consumer<BaseEvent>[] next = new Consumer[curr.length + 1];
        System.arraycopy(curr, 0, next, 0, curr.length);
        next[curr.length] = (Consumer<BaseEvent>) listener;
        table[eventId][priority.level] = next;
    }

    @SuppressWarnings("unchecked")
    public <T extends BaseEvent> void unbind(int eventId, Consumer<T> listener) {
        for (int p = 0; p < MAX_PRIORITY; p++) {
            Consumer<BaseEvent>[] curr = table[eventId][p];
            if (curr == null || curr.length == 0) continue;

            int count = 0;
            for (Consumer<BaseEvent> l : curr) if (l == listener) count++;
            if (count == 0) continue;

            Consumer<BaseEvent>[] next = new Consumer[curr.length - count];
            int idx = 0;
            for (Consumer<BaseEvent> l : curr)
                if (l != listener) next[idx++] = l;
            table[eventId][p] = next;
            break;
        }
    }

    public void post(BaseEvent event) {
        String name = Thread.currentThread().getName();
        boolean isMain = name.equals("Render thread")
                || name.startsWith("Server thread")
                || name.equals("main");
        if (isMain) {
            dispatch(event);
        } else {
            pendingQueue.add(event);
        }
    }

    public void flush() {
        BaseEvent e;
        while ((e = pendingQueue.poll()) != null) {
            dispatch(e);
        }
    }

    private void dispatch(BaseEvent event) {
        for (int p = MAX_PRIORITY - 1; p >= 0; p--) {
            Consumer<BaseEvent>[] arr = table[event.getEventId()][p];
            if (arr == null || arr.length == 0) continue;
            for (Consumer<BaseEvent> c : arr) {
                try {
                    // ✅ Crash Guard — listener crash ไม่ทำให้ client พัง
                    c.accept(event);
                } catch (Exception e) {
                    Jinun.LOGGER.error(
                            "[EventBus] Listener crashed on event ID "
                                    + event.getEventId()
                                    + " — listener: " + c, e
                    );
                }
                if (event.isCancelable() && event.isCancelled()) return;
            }
        }
    }
}