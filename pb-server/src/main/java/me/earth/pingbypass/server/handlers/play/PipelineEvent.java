package me.earth.pingbypass.server.handlers.play;

import lombok.Getter;
import lombok.Setter;
import me.earth.pingbypass.server.session.Session;
import net.minecraft.network.protocol.Packet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
@Setter
public class PipelineEvent<P extends Packet<?>> {
    private final Session session;
    private final P original;
    private @Nullable Packet<?> packet;
    
    public PipelineEvent(Session session, @NotNull P packet) {
        this.session = session;
        this.original = packet;
        this.packet = packet;
    }
    
}
