package me.earth.earthhack.impl.mixin.ducks;

public interface IPlayerControllerMP
{
    /**
     * Used by MixinPlayerControllerMP,
     * invokes syncCurrentPlayItem.
     */
    void syncItem();

    /**
     * Returns the currentPlayerItem
     * to find out if items are synced.
     *
     * @return currentPlayerItem.
     */
    int getItem();
}
