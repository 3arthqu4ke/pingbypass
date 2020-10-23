package me.earth.earthhack.api.event.bus.api;

/**
 * The EventBus Api.
 */
public interface EventBus
{
    /**
     * Posts an object on the bus. Invokes
     * {@link Invoker#invoke(Object)} for every
     * Listener that targets the objects class.
     *
     * @param object the object posted.
     */
    void post(Object object);

    /**
     * Posts an object on the bus. Invokes
     * {@link Invoker#invoke(Object)} for every
     * Listener that targets the objects class and where
     * {@link Listener#getType()} == type.
     *
     * @param object the object posted.
     * @param type the type.
     */
    void post(Object object, Class<?> type);

    /**
     * Calls {@code  register} for
     * every listener from {@link Subscriber#getListeners()},
     * if the object implements the Subscriber interface.
     *
     * @param object the subscriber whose listeners get registered.
     */
    void subscribe(Object object);

    /**
     * Calls {@code  unregister} for
     * every listener from {@link Subscriber#getListeners()},
     * if the object implements the Subscriber interface.
     *
     * @param object the subscriber whose listeners get unregistered.
     */
    void unsubscribe(Object object);

    /**
     * Registers a listener to the bus. It will now receive
     * Objects posted on the bus.
     *
     * @param listener the listener to be registered.
     */
    void register(Listener<?> listener);

    /**
     * Unregisters a listener from the bus. Now it
     * wont receive Objects posted on the bus.
     *
     * @param listener the listener to be unregistered.
     */
    void unregister(Listener<?> listener);

    /**
     * Returns {@code true} if the object is a subscriber
     * whose listeners are currently registered or a listener
     * which is currently registered.
     *
     * @return {@code true} if the object is subscribed to the bus.
     */
    boolean isSubscribed(Object object);

}
