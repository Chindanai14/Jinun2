package com.Aaeli.jinun.engine.rotation;

public class RotationData {
    public float   yaw, pitch;
    public boolean active;

    public RotationData(float yaw, float pitch) {
        this.yaw   = yaw;
        this.pitch = pitch;
    }

    public void set(float yaw, float pitch) {
        this.yaw   = yaw;
        this.pitch = pitch;
    }
}