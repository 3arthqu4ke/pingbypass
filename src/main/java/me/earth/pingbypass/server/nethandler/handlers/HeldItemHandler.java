package me.earth.pingbypass.server.nethandler.handlers;

import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerChangeHeldItemPacket;
import me.earth.pingbypass.client.managers.SwitchManager;
import me.earth.pingbypass.server.nethandler.IHandler;
import me.earth.pingbypass.util.thread.ThreadUtil;

public class HeldItemHandler implements IHandler<ClientPlayerChangeHeldItemPacket>
{
    @Override
    public boolean handle(ClientPlayerChangeHeldItemPacket packet)
    {
        ThreadUtil.runChecked(() -> mc.player.inventory.currentItem = packet.getSlot());
        SwitchManager.getInstance().onSwitch(packet);
        return true;
    }

}
