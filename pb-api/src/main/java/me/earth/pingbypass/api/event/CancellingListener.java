package me.earth.pingbypass.api.event;

import me.earth.pingbypass.api.event.event.CancellableEvent;
import me.earth.pingbypass.api.event.listeners.AbstractEventListener;
import me.earth.pingbypass.api.setting.Setting;

public class CancellingListener<E extends CancellableEvent> extends AbstractEventListener<E> {
    public CancellingListener(Class<E> type, Class<?> genericType, int priority) {
        super(type, genericType, priority);
    }

    public CancellingListener(Class<E> type) {
        super(type);
    }

    public CancellingListener(Class<E> type, Class<?> genericType) {
        super(type, genericType);
    }

    public CancellingListener(Class<E> type, int priority) {
        super(type, priority);
    }

    @Override
    public void onEvent(E event) {
        event.setCancelled(true);
    }

    public static class WithSetting<E extends CancellableEvent> extends AbstractEventListener<E> {
        private final Setting<Boolean> setting;

        public WithSetting(Class<E> type, Class<?> genericType, int priority, Setting<Boolean> setting) {
            super(type, genericType, priority);
            this.setting = setting;
        }

        public WithSetting(Class<E> type, Setting<Boolean> setting) {
            this(type, null, DEFAULT_LISTENER_PRIORITY, setting);
        }

        public WithSetting(Class<E> type, int priority, Setting<Boolean> setting) {
            this(type, null, priority, setting);
        }

        public WithSetting(Class<E> type, Class<?> genericType, Setting<Boolean> setting) {
            this(type, genericType, DEFAULT_LISTENER_PRIORITY, setting);
        }

        @Override
        public void onEvent(E event) {
            if (setting.getValue()) {
                event.setCancelled(true);
            }
        }
    }

}
