package me.earth.pingbypass.api.traits;

/**
 * A interface for Objects that can be named.
 */
public interface Displayable {
    /**
     * @return the display name for this Object.
     */
    String getDisplayName();

    /**
     * Sets the DisplayName for this Object.
     */
    void setDisplayName(String name);

}
