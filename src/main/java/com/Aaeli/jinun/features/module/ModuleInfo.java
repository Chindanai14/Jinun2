package com.Aaeli.jinun.features.module;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ModuleInfo {
    String   name();
    Category category();
    int      defaultKey() default -1; // GLFW keycode, -1 = ไม่มี default key
}