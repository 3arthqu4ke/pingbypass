package me.earth.pingbypass.api.util;

import lombok.experimental.UtilityClass;
import me.earth.pingbypass.api.mixins.entity.IAbstractClientPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.world.entity.player.Player;

@UtilityClass
public class PingUtil {
    public int getPing(Minecraft mc) {
        return getPing(mc.player);
    }

    public int getPing(Player player) {
        if (player instanceof IAbstractClientPlayer iAbstractClientPlayer) {
            PlayerInfo playerInfo = iAbstractClientPlayer.invokeGetPlayerInfo();
            if (playerInfo != null) {
                return playerInfo.getLatency();
            }
        }

        return 0;
    }

}
