package me.earth.pingbypass.client.modules.fakeplayer;

import com.mojang.authlib.GameProfile;
import me.earth.earthhack.api.module.Category;
import me.earth.earthhack.api.module.Module;
import net.minecraft.client.entity.EntityOtherPlayerMP;

import java.util.UUID;

public class FakePlayer extends Module
{
    private static final FakePlayer INSTANCE = new FakePlayer();

    private EntityOtherPlayerMP fakePlayer;

    private FakePlayer()
    {
        super("FakePlayer", Category.Client);
        this.listeners.add(new DisconnectListener(this));
    }

    public static FakePlayer getInstance()
    {
        return INSTANCE;
    }

    @Override
    protected void onEnable()
    {
        if (mc.world != null)
        {
            fakePlayer = createFakePlayer(new GameProfile(UUID.randomUUID(), "FakePlayer"));
            mc.world.addEntityToWorld(-1337, fakePlayer);
        }
    }

    @Override
    protected void onDisable()
    {
        if (mc.world != null && fakePlayer != null)
        {
            mc.world.removeEntity(fakePlayer);
        }

        fakePlayer = null;
    }

    public static EntityOtherPlayerMP createFakePlayer(GameProfile profile)
    {
        EntityOtherPlayerMP fakePlayer = new EntityOtherPlayerMP(mc.world, profile);
        fakePlayer.inventory.copyInventory(mc.player.inventory);
        fakePlayer.copyLocationAndAnglesFrom(mc.player);
        fakePlayer.setHealth(mc.player.getHealth());
        return fakePlayer;
    }

}
