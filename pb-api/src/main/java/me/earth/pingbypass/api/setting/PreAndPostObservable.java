package me.earth.pingbypass.api.setting;

import java.util.Collection;

public interface PreAndPostObservable<O> {
    void addPreObserver(O observer);

    void addPostObserver(O observer);

    void removeObserver(O observer);

    Collection<O> getPreObservers();

    Collection<O> getPostObservers();

}
