package me.earth.pingbypass.api.event.listeners.generic;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

@UtilityClass
public class TypeHelper {
    @SuppressWarnings("unchecked")
    public static <T> TypeInfo<T> getTypeInfo(@NonNull Class<?> clazz) {
        Type genericSuperclass = clazz.getGenericSuperclass();
        if (genericSuperclass instanceof ParameterizedType parameterizedType) {
            for (Type typeArg : parameterizedType.getActualTypeArguments()) {
                if (typeArg instanceof ParameterizedType genericType) {
                    if (!(genericType.getRawType() instanceof Class)) {
                        throw new CannotFindTypeException(
                            "Cannot find generic type on " + clazz.getName()
                                + ": " + typeArg
                                + " has a raw type that is not a Class: "
                                + genericType.getRawType());
                    }

                    for (Type nested : genericType.getActualTypeArguments()) {
                        if (nested instanceof ParameterizedType generic
                            && generic.getRawType() instanceof Class) {
                            return new TypeInfo<>(
                                (Class<T>) genericType.getRawType(),
                                (Class<?>) generic.getRawType());
                        } else if (nested instanceof Class) {
                            return new TypeInfo<>(
                                (Class<T>) genericType.getRawType(),
                                (Class<?>) nested);
                        }

                        break; // only check the first type
                    }

                    return new TypeInfo<>(
                        (Class<T>) genericType.getRawType(), null);
                } else if (typeArg instanceof Class) {
                    return new TypeInfo<>((Class<T>) typeArg, null);
                }

                throw new CannotFindTypeException(
                    "Cannot find generic type on " + clazz.getName() + ": "
                        + typeArg + " is not a Class or ParameterizedType");
            }
        }

        throw new CannotFindTypeException(clazz.getName()
                                          + " does not seem to be generic!");
    }

}
