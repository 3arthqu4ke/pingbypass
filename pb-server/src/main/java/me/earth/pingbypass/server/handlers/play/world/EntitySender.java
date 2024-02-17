package me.earth.pingbypass.server.handlers.play.world;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import lombok.extern.slf4j.Slf4j;
import me.earth.pingbypass.server.mixins.network.syncher.ISynchedEntityData;
import me.earth.pingbypass.server.mixins.world.IClientLevel;
import me.earth.pingbypass.server.session.Session;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.*;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

@Slf4j
public class EntitySender {
    /**
     * @param session the session to send all entities of the level to.
     * @param level the level containing the entities to send to the client.
     *
     * @see net.minecraft.server.level.ServerEntity#addPairing(ServerPlayer)
     */
    public void sendEntities(Session session, ClientLevel level) {
        for (Entity entity : ((IClientLevel) level).invokeGetEntities().getAll()) {
            if (entity instanceof Player && !(entity instanceof RemotePlayer)) {
                continue;
            }

            List<Packet<ClientGamePacketListener>> packets = new ArrayList<>();
            sendPairingData(entity, packets::add);
            session.send(new ClientboundBundlePacket(packets));
        }
    }

    /**
     * @param entity   the entity to send.
     * @param consumer a consumer accepting all the packets needed for the entity to spawn.
     * @see net.minecraft.server.level.ServerEntity#sendPairingData(ServerPlayer, Consumer)
     */
    private void sendPairingData(Entity entity, Consumer<Packet<ClientGamePacketListener>> consumer) {
        if (entity.isRemoved()) {
            log.warn("Fetching packet for removed entity {}", entity);
        }

        Packet<ClientGamePacketListener> addEntityPacket = entity.getAddEntityPacket();
        consumer.accept(addEntityPacket);
        Int2ObjectMap<SynchedEntityData.DataItem<?>> itemsById = ((ISynchedEntityData) entity.getEntityData()).getItemsById();
        List<SynchedEntityData.DataValue<?>> dataValues = new ArrayList<>(itemsById.size());
        itemsById.values().forEach(dataItem -> dataValues.add(dataItem.value()));
        consumer.accept(new ClientboundSetEntityDataPacket(entity.getId(), dataValues));

        boolean trackDeltas = entity.getType().trackDeltas();
        if (entity instanceof LivingEntity) {
            Collection<AttributeInstance> collection = ((LivingEntity) entity).getAttributes().getSyncableAttributes();
            if (!collection.isEmpty()) {
                consumer.accept(new ClientboundUpdateAttributesPacket(entity.getId(), collection));
            }

            if (((LivingEntity) entity).isFallFlying()) {
                trackDeltas = true;
            }
        }

        Vec3 delta = entity.getDeltaMovement();
        if (trackDeltas && !(entity instanceof LivingEntity)) {
            consumer.accept(new ClientboundSetEntityMotionPacket(entity.getId(), delta));
        }

        if (entity instanceof LivingEntity) {
            List<Pair<EquipmentSlot, ItemStack>> equipment = Lists.newArrayList();

            for (EquipmentSlot equipmentSlot : EquipmentSlot.values()) {
                ItemStack stack = ((LivingEntity) entity).getItemBySlot(equipmentSlot);
                if (!stack.isEmpty()) {
                    equipment.add(Pair.of(equipmentSlot, stack.copy()));
                }
            }

            if (!equipment.isEmpty()) {
                consumer.accept(new ClientboundSetEquipmentPacket(entity.getId(), equipment));
            }
        }

        if (!entity.getPassengers().isEmpty()) {
            consumer.accept(new ClientboundSetPassengersPacket(entity));
        }

        if (entity.isPassenger()) {
            Entity vehicle = entity.getVehicle();
            if (vehicle != null) {
                consumer.accept(new ClientboundSetPassengersPacket(vehicle));
            }
        }

        if (entity instanceof Mob mob && mob.isLeashed()) {
            consumer.accept(new ClientboundSetEntityLinkPacket(mob, mob.getLeashHolder()));
        }
    }

}
