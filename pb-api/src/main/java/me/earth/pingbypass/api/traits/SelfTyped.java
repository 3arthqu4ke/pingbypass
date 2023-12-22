package me.earth.pingbypass.api.traits;

public interface SelfTyped<T extends SelfTyped<T>> {
    @SuppressWarnings("unchecked")
    default T getSelf() {
        return (T) this;
    }

}
