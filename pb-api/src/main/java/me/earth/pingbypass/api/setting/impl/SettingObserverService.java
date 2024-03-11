package me.earth.pingbypass.api.setting.impl;

import me.earth.pingbypass.api.setting.PreAndPostObservable;
import me.earth.pingbypass.api.setting.Setting;
import me.earth.pingbypass.api.setting.SettingEvent;
import me.earth.pingbypass.api.setting.SettingObserver;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

// TODO: this caused more trouble than it was worth in 3arthh4ck. Maybe drop this?
final class SettingObserverService<T> implements PreAndPostObservable<SettingObserver<T>> {
    private final List<SettingObserver<T>> postObservers = new CopyOnWriteArrayList<>();
    private final List<SettingObserver<T>> preObservers = new CopyOnWriteArrayList<>();

    SettingEvent<T> notifyPre(Setting<T> setting, T value) {
        SettingEvent<T> event = new SettingEvent<>(setting, value);
        preObservers.forEach(observer -> observer.onValue(event));
        return event;
    }

    void notifyPost(SettingEvent<T> event) {
        postObservers.forEach(observer -> observer.onValue(event));
    }

    @Override
    public void addPreObserver(SettingObserver<T> observer) {
        preObservers.add(observer);
    }

    @Override
    public void addPostObserver(SettingObserver<T> observer) {
        postObservers.add(observer);
    }

    @Override
    public void removeObserver(SettingObserver<T> observer) {
        postObservers.remove(observer);
        preObservers.remove(observer);
    }

    @Override
    public Collection<SettingObserver<T>> getPreObservers() {
        return preObservers;
    }

    @Override
    public Collection<SettingObserver<T>> getPostObservers() {
        return postObservers;
    }

}

