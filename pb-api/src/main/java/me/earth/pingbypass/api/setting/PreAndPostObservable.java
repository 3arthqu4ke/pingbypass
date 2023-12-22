package me.earth.pingbypass.api.setting;

public interface PreAndPostObservable<O> {
    void addPreObserver(O observer);

    void addPostObserver(O observer);

    void removeObserver(O observer);

}
