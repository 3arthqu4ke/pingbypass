package me.earth.pingbypass.server.nethandler.handlers;

import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.window.WindowAction;
import com.github.steveice10.mc.protocol.data.game.window.WindowActionParam;
import com.github.steveice10.mc.protocol.packet.ingame.client.window.ClientWindowActionPacket;
import me.earth.pingbypass.mixin.mixins.minecraft.gui.IContainer;
import me.earth.pingbypass.server.nethandler.IHandler;
import me.earth.pingbypass.util.thread.ThreadUtil;
import net.minecraft.inventory.ClickType;

public class WindowClickHandler implements IHandler<ClientWindowActionPacket>
{
    @Override
    public boolean handle(ClientWindowActionPacket packet)
    {
        ThreadUtil.addCheckedTask(() ->
        {
            ((IContainer) mc.player.openContainer).setTransactionID((short) packet.getActionId());
            if (packet.getSlot() != -1337)
            {
                mc.player.openContainer.slotClick(
                        packet.getSlot(),
                        convertMouseButton(packet.getAction(), packet.getParam(), packet.getSlot()),
                        convertClickType(packet.getAction()),
                        mc.player);
            }
        });

        return packet.getSlot() != -1337;
    }

    /**
     * Converts Steveice's WindowAction to a ClickType.
     *
     * Ordinal would work just as well...
     *
     * @param action the action.
     * @return the corresponding ClickType.
     */
    private ClickType convertClickType(WindowAction action)
    {
        switch(action)
        {
            case CLICK_ITEM:
                return ClickType.PICKUP;
            case SHIFT_CLICK_ITEM:
                return ClickType.QUICK_MOVE;
            case MOVE_TO_HOTBAR_SLOT:
                return ClickType.SWAP;
            case CREATIVE_GRAB_MAX_STACK:
                return ClickType.CLONE;
            case DROP_ITEM:
                return ClickType.THROW;
            case SPREAD_ITEM:
                return ClickType.QUICK_CRAFT;
            case FILL_STACK:
                return ClickType.PICKUP_ALL;
            default:
                throw new IllegalArgumentException("Bad ClickType");
        }
    }

    private int convertMouseButton(WindowAction action, WindowActionParam param, int slot)
    {
        if (action == WindowAction.DROP_ITEM)
        {
            return MagicValues.value(Integer.class, param) + (slot != -999 ? 2 : 0);
        }

        return MagicValues.value(Integer.class, param);
    }

}
