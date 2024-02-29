package me.earth.pingbypass.api.event.impl;

import lombok.*;
import me.earth.pingbypass.PingBypass;
import me.earth.pingbypass.api.event.Lockable;
import me.earth.pingbypass.api.event.api.EventListener;
import me.earth.pingbypass.api.event.api.Subscriber;
import me.earth.pingbypass.api.side.Side;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * If you are writing a plugin that can be used on different pingbypass implementations but you want to target one
 * specifically its easy to use events and this class. The implementation might not be available at runtime so
 * this works around any {@link ClassNotFoundException} that might occur.
 *
 * @param <P> the type of the PingBypass implementation to target.
 */
@Data
public class SideSpecificSubscriber<P extends PingBypass> implements Subscriber, Lockable {
    @ToString.Exclude
    @Getter(AccessLevel.NONE)
    @EqualsAndHashCode.Exclude
    private final Object lock = new Object();
    private final List<EventListener<?>> listeners = new ArrayList<>();
    private final P pingBypass;

    @Override
    public Object getLock(Object requester) {
        return lock;
    }

    public void subscribe() {
        pingBypass.getEventBus().subscribe(this);
    }

    public void unsubscribe() {
        pingBypass.getEventBus().unsubscribe(this);
    }

    public void addListener(EventListener<?> listener) {
        listeners.add(listener);
    }

    @SuppressWarnings("unchecked")
    public static <T extends PingBypass> List<SideSpecificSubscriber<T>> create(Stream<PingBypass> instances,
                                                                         @Nullable Side side,
                                                                         ClassSupplier<T> classSupplier,
                                                                         Consumer<SideSpecificSubscriber<T>> initialize)
    {
        List<SideSpecificSubscriber<T>> result = new ArrayList<>(1);
        try {
            Class<? extends T> clazz = classSupplier.supply();
            instances.forEach(pb -> {
                if (clazz.isInstance(pb) && (side == null || side.equals(pb.getSide()))) {
                    var subscriber = (SideSpecificSubscriber<T>) new SideSpecificSubscriber<>(pb);
                    initialize.accept(subscriber);
                    result.add(subscriber);
                }
            });
        } catch (ClassNotFoundException | NoClassDefFoundError ignored) { }
        return result;
    }

    @FunctionalInterface
    public interface ClassSupplier<T> {
        Class<? extends T> supply() throws ClassNotFoundException;
    }

}
