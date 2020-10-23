package me.earth.pingbypass.client.commands;

import me.earth.pingbypass.client.managers.FriendManager;

import java.util.UUID;

public class FriendCommand extends AbstractCommand
{
    public FriendCommand()
    {
        super("Friend");
    }

    @Override
    public void execute(String[] args)
    {
        if (args.length == 2)
        {
            String command = args[1];
            if (command.equalsIgnoreCase("clear"))
            {
                FriendManager.getInstance().clear();
            }

            return;
        }

        if (args.length == 4 || args.length == 3)
        {
            String command = args[1];
            String friend  = args[2];
            String uuid    = args.length == 3 ? args[2] : args[3];

            if (command.equalsIgnoreCase("add") && args.length == 4)
            {
                FriendManager.getInstance().addFriend(friend, UUID.fromString(uuid));
            }
            else
            {
                FriendManager.getInstance().removeFriend(friend);
            }
        }
    }

}
