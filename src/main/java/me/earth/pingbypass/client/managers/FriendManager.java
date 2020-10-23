package me.earth.pingbypass.client.managers;

import me.earth.earthhack.api.util.Globals;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class FriendManager implements Globals
{
    private static final FriendManager INSTANCE = new FriendManager();

    private final Map<String, UUID> friends = new ConcurrentHashMap<>();

    private FriendManager()
    {
        clear();
    }

    public static FriendManager getInstance()
    {
        return INSTANCE;
    }

    public void clear()
    {
        friends.clear();
        addFriend(mc.getSession().getProfile().getName(), mc.getSession().getProfile().getId());
    }

    public boolean isFriend(EntityPlayer player)
    {
        return isFriend(player.getName());
    }

    public boolean isFriend(String name)
    {
        return friends.containsKey(name);
    }

    public void addFriend(String name, UUID uuid)
    {
        friends.put(name, uuid);
    }

    public void removeFriend(String name)
    {
        friends.remove(name);
    }

}
