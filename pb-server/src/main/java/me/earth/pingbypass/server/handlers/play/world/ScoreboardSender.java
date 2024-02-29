package me.earth.pingbypass.server.handlers.play.world;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import me.earth.pingbypass.server.session.Session;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundSetDisplayObjectivePacket;
import net.minecraft.network.protocol.game.ClientboundSetObjectivePacket;
import net.minecraft.network.protocol.game.ClientboundSetPlayerTeamPacket;
import net.minecraft.network.protocol.game.ClientboundSetScorePacket;
import net.minecraft.server.ServerScoreboard;
import net.minecraft.world.scores.*;

import java.util.List;
import java.util.Set;

/**
 * net.minecraft.server.players.PlayerList#updateEntireScoreboard(ServerScoreboard, ServerPlayer)
 * @see ServerScoreboard#getStartTrackingPackets(Objective)
 */
public class ScoreboardSender {
    public void updateEntireScoreboard(Scoreboard scoreboard, Session session) {
        Set<Objective> objectives = Sets.newHashSet();
        for (PlayerTeam playerTeam : scoreboard.getPlayerTeams()) {
            session.send(ClientboundSetPlayerTeamPacket.createAddOrModifyPacket(playerTeam, true));
        }

        for (DisplaySlot displaySlot : DisplaySlot.values()) {
            Objective objective = scoreboard.getDisplayObjective(displaySlot);
            if (objective != null && !objectives.contains(objective)) {
                for (Packet<?> packet : getStartTrackingPackets(scoreboard, objective)) {
                    session.send(packet);
                }

                objectives.add(objective);
            }
        }
    }

    public List<Packet<?>> getStartTrackingPackets(Scoreboard scoreboard, Objective objective) {
        List<Packet<?>> result = Lists.newArrayList();
        result.add(new ClientboundSetObjectivePacket(objective, 0));

        for (DisplaySlot displaySlot : DisplaySlot.values()) {
            if (scoreboard.getDisplayObjective(displaySlot) == objective) {
                result.add(new ClientboundSetDisplayObjectivePacket(displaySlot, objective));
            }
        }

        for (PlayerScoreEntry entry : scoreboard.listPlayerScores(objective)) {
            result.add(new ClientboundSetScorePacket(entry.owner(), objective.getName(), entry.value(), entry.display(), entry.numberFormatOverride()));
        }

        return result;
    }

}
