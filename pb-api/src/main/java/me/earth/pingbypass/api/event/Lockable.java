package me.earth.pingbypass.api.event;

public interface Lockable {
    Object getLock(Object requester);

}
