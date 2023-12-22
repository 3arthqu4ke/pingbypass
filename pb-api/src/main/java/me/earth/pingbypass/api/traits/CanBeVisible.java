package me.earth.pingbypass.api.traits;

@FunctionalInterface
public interface CanBeVisible {
    CanBeVisible ALWAYS_VISIBLE = () -> true;

    boolean isVisible();

}
