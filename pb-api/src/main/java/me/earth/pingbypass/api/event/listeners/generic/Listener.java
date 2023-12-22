package me.earth.pingbypass.api.event.listeners.generic;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.earth.pingbypass.api.event.api.EventListener;

/**
 * Important notes: only implement this as an anonymous class, otherwise {@link TypeHelper} will not work.
 * Also do not extend this class with another abstract class which sets the type of event, e.g. like this:
 * {@code ReceiveListener<P extends Packet<?>> extends Listener<PacketEvent.Receive<P>>}, due to the way
 * {@link TypeHelper} works currently this will not work. For that purpose use {@link GenericListener}.
 *
 * @param <E> the type of event handled by this listener.
 */
@Getter
@RequiredArgsConstructor
public abstract class Listener<E> implements EventListener<E> {
    private final Class<E> type;
    private final Class<?> genericType;
    private final int priority;

    public Listener() {
        TypeInfo<E> info = TypeHelper.getTypeInfo(this.getClass());
        this.type = info.type();
        this.genericType = info.generic();
        this.priority = 0;
    }

    public Listener(int priority) {
        TypeInfo<E> info = TypeHelper.getTypeInfo(this.getClass());
        this.type = info.type();
        this.genericType = info.generic();
        this.priority = priority;
    }

}
