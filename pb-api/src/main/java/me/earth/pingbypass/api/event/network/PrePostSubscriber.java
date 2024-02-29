package me.earth.pingbypass.api.event.network;


import me.earth.pingbypass.api.event.SubscriberImpl;
import me.earth.pingbypass.api.event.listeners.AbstractEventListener;
import me.earth.pingbypass.api.event.listeners.generic.TypeHelper;
import net.minecraft.network.protocol.Packet;
import net.minecraft.util.thread.BlockableEventLoop;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Warning: This uses {@link TypeHelper}, and thus should only be implemented as an anonymous class or if you know what
 * you are doing.
 *
 * @param <P> the type of packet handled by this listener.
 */
public abstract class PrePostSubscriber<P extends Packet<?>> extends SubscriberImpl {
    // TODO: this is okayish, but maybe theres a way to do this without @Redirect? @Wrap from MixinExtensions :(
    private final ThreadLocal<List<Runnable>> postEvents = new ThreadLocal<>();
    private final ThreadLocal<Packet<?>> currentPacket = new ThreadLocal<>();

    public PrePostSubscriber() {
        this(thisClass -> TypeHelper.<P>getTypeInfo(thisClass).type());
    }

    public PrePostSubscriber(Class<P> type) {
        this(thisClass -> type);
    }

    public PrePostSubscriber(Function<Class<?>, Class<P>> typeGetter) {
        Class<P> type = typeGetter.apply(this.getClass());
        // TODO: make PingBypass api TypeHelper public and use it here?
        listen(new AbstractEventListener.Unsafe<PacketEvent.Receive<P>>(PacketEvent.Receive.class, type) {
            @Override
            public void onEvent(PacketEvent.Receive<P> event) {
                currentPacket.set(event.getPacket());
                onPreEvent(event);
            }
        });

        listen(new AbstractEventListener.Unsafe<PacketEvent.PostReceive<P>>(PacketEvent.PostReceive.class, type) {
            @Override
            public void onEvent(PacketEvent.PostReceive<P> event) {
                onPostEvent(event);
            }
        });
    }

    public abstract void onPreEvent(PacketEvent.Receive<P> event);

    public void onPostEvent(PacketEvent.PostReceive<P> event) {
        if (event.getPacket() == currentPacket.get() && postEvents.get() != null) {
            postEvents.get().forEach(Runnable::run);
        }

        currentPacket.remove();
        postEvents.remove();
    }

    public void addPostEventThreadLocal(Runnable runnable) {
        if (postEvents.get() == null) {
            postEvents.set(new ArrayList<>());
        }

        postEvents.get().add(runnable);
    }

    public void addScheduledPostEvent(BlockableEventLoop<?> eventLoop, Runnable task) {
        addPostEventThreadLocal(() -> eventLoop.submit(task));
    }

}
