package com.mrcrayfish.device.proxy;

import com.mrcrayfish.device.api.print.IPrint;
import com.mrcrayfish.device.api.print.PrintingManager;
import lombok.SneakyThrows;

import java.util.Map;

public class ReflectionHelper {
    @SneakyThrows
    public static <T> void setPrivateValue(Class<T> clazz, T o, Object value, String name) {
        clazz.getField(name).set(o, value);
    }

    @SneakyThrows
    public static <T> Object getPrivateValue(Class<T> clazz, T o, String name) {
        return clazz.getField(name).get(o);
    }
}
