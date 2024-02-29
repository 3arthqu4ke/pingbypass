package me.earth.pingbypass.api.util;

import lombok.experimental.UtilityClass;

import java.lang.reflect.Constructor;

@UtilityClass
public class ReflectionUtil {
    public static boolean doesClassExist(String name) {
        return forNameOrNull(name) != null;
    }

    public static <T> T instantiateNoArgs(Class<T> clazz) throws ReflectiveOperationException {
        Constructor<T> constructor = clazz.getDeclaredConstructor();
        constructor.setAccessible(true);
        return constructor.newInstance();
    }

    public static Class<?> forNameOrNull(String name) {
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

}
