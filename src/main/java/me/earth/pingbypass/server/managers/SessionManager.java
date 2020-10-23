package me.earth.pingbypass.server.managers;

import com.github.steveice10.packetlib.Session;
import com.github.steveice10.packetlib.packet.Packet;

/**
 * Manages the Servers Session and ensures
 * that only one session at a time is connected.
 */
public class SessionManager
{
    private Session session;

    public boolean requestSession(Session sessionIn)
    {
        if (session != null && session.isConnected())
        {
            sessionIn.disconnect("Only one session allowed!");
            return false;
        }

        session = sessionIn;
        return true;
    }

    public boolean removeSession(Session sessionIn)
    {
        if (sessionIn.equals(session))
        {
            removeSession();
            return true;
        }

        return false;
    }

    public void removeSession()
    {
        if (session != null && session.isConnected())
        {
            session.disconnect("Your session has been removed.");
        }

        session = null;
    }

    public boolean isConnected()
    {
        return session != null && session.isConnected();
    }

    public void sendPacket(Packet packet)
    {
        if (session != null)
        {
            session.send(packet);
        }
    }

}
