package me.earth.pingbypass.client.managers;

import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerChangeHeldItemPacket;
import me.earth.earthhack.api.event.bus.EventListener;
import me.earth.earthhack.api.event.bus.SubscriberImpl;
import me.earth.earthhack.impl.event.events.PacketEvent;
import me.earth.earthhack.impl.util.math.StopWatch;
import net.minecraft.network.play.server.SPacketHeldItemChange;

public class SwitchManager extends SubscriberImpl
{
    private static final SwitchManager INSTANCE = new SwitchManager();

    private final StopWatch timer = new StopWatch();
    private int last_slot;

    private SwitchManager()
    {
        this.listeners.add(new EventListener<PacketEvent.Receive<SPacketHeldItemChange>>(PacketEvent.Receive.class, SPacketHeldItemChange.class)
        {
            @Override
            public void invoke(PacketEvent.Receive<SPacketHeldItemChange> event)
            {
                last_slot = event.getPacket().getHeldItemHotbarIndex();
            }
        });
    }

    public static SwitchManager getInstance()
    {
        return INSTANCE;
    }

    public long getLastSwitch()
    {
        return timer.getTime();
    }

    public int getSlot()
    {
        return last_slot;
    }

    public void onSwitch(ClientPlayerChangeHeldItemPacket packet)
    {
        timer.reset();
        last_slot = packet.getSlot();
    }

}
