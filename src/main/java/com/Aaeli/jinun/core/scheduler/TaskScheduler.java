package com.Aaeli.jinun.core.scheduler;

import com.Aaeli.jinun.Jinun;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TaskScheduler {
    private final List<ScheduledTask> tasks = new ArrayList<>();

    /**
     * รัน 1 ครั้งหลังจาก N ticks
     * ตัวอย่าง: scheduler.runAfterTicks(5, () -> attack())
     */
    public ScheduledTask runAfterTicks(int ticks, Runnable task) {
        ScheduledTask t = new ScheduledTask(task, ScheduledTask.Type.ONCE, ticks, 0);
        tasks.add(t);
        return t;
    }

    /**
     * รันทุก N ticks ตลอดไป (จนกว่าจะ cancel)
     * ตัวอย่าง: scheduler.runEveryTicks(20, () -> scan())
     */
    public ScheduledTask runEveryTicks(int interval, Runnable task) {
        ScheduledTask t = new ScheduledTask(task, ScheduledTask.Type.REPEAT, interval, interval);
        tasks.add(t);
        return t;
    }

    /**
     * รันทุก N ticks โดยเริ่ม delay ก่อน N ticks
     */
    public ScheduledTask runEveryTicks(int delayTicks, int interval, Runnable task) {
        ScheduledTask t = new ScheduledTask(task, ScheduledTask.Type.REPEAT, delayTicks, interval);
        tasks.add(t);
        return t;
    }

    /**
     * เรียกทุก tick จาก MinecraftClientMixin
     */
    public void tick() {
        Iterator<ScheduledTask> it = tasks.iterator();
        while (it.hasNext()) {
            ScheduledTask t = it.next();

            if (t.cancelled) {
                it.remove();
                continue;
            }

            if (--t.ticksLeft <= 0) {
                try {
                    t.task.run();
                } catch (Exception e) {
                    Jinun.LOGGER.error("[TaskScheduler] Task threw an exception", e);
                }

                if (t.type == ScheduledTask.Type.ONCE) {
                    it.remove();
                } else {
                    t.ticksLeft = t.interval; // reset สำหรับ REPEAT
                }
            }
        }
    }

    /** ยกเลิก task ทั้งหมด (เช่นตอน world leave) */
    public void cancelAll() {
        tasks.clear();
    }

    public int getPendingCount() { return tasks.size(); }
}