package me.earth.pingbypass.api.util.exceptions;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import me.earth.pingbypass.api.util.ThrowingConsumer;

@UtilityClass
public class ExceptionUtil {
    @SneakyThrows
    public void sneaky(ThrowingRunnable<?> throwingRunnable) {
        throwingRunnable.run();
    }

    @SneakyThrows
    public <T> T sneaky(ThrowingSupplier<T, ?> throwingSupplier) {
        return throwingSupplier.get();
    }

    public <T, EX extends Throwable> void forEach(Iterable<T> iterable, ThrowingConsumer<T, EX> consumer) throws EX {
        iterable.forEach(consumer);
    }

}
