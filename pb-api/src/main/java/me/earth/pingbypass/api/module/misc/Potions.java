package me.earth.pingbypass.api.module.misc;

import me.earth.pingbypass.PingBypass;
import me.earth.pingbypass.api.module.impl.Categories;
import me.earth.pingbypass.api.module.impl.ModuleImpl;
import me.earth.pingbypass.api.setting.Setting;
import me.earth.pingbypass.api.event.network.PacketEvent;
import me.earth.pingbypass.api.event.network.PostListener;
import me.earth.pingbypass.api.event.network.ReceiveListener;
import me.earth.pingbypass.api.mixins.entity.IPlayer;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.game.ClientboundEntityEventPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.player.Player;

public class Potions extends ModuleImpl {
    private final Setting<Boolean> goldenApples = bool("GoldenApples", true, "More accurate damage calculations by tracking golden apples eaten by players.");

    public Potions(PingBypass pingBypass) {
        super(pingBypass, "Potions", Categories.MISC, "Tweaks for potions and their effects.");
        // see 3arthh4ck managers.minecraft.combat.PotionService, allows us to make better Damage calculations
        listen(new PostListener.Safe.Direct<ClientboundEntityEventPacket>(mc) {
            @Override
            public void onSafePacket(ClientboundEntityEventPacket packet, LocalPlayer localPlayer, ClientLevel level, MultiPlayerGameMode gameMode) {
                if (goldenApples.getValue() && packet.getEventId() == EntityEvent.TALISMAN_ACTIVATE && packet.getEntity(level) instanceof Player player && player != localPlayer) {
                    player.getActiveEffectsMap().clear();
                    player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 900, 1));
                    player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 100, 1));
                }
            }
        });

        listen(new ReceiveListener.Scheduled.Safe<ClientboundSetEntityDataPacket>(mc) {
            @Override
            public void onSafeEvent(PacketEvent.Receive<ClientboundSetEntityDataPacket> event, LocalPlayer localPlayer, ClientLevel level, MultiPlayerGameMode gameMode) {
                if (goldenApples.getValue() && level.getEntity(event.getPacket().id()) instanceof Player player && player != localPlayer) {
                    for (SynchedEntityData.DataValue<?> value : event.getPacket().packedItems()) {
                        if (value.id() == IPlayer.getDataPlayerAbsorptionId().getId() && value.value() instanceof Float absorptionAmount) {
                            float previousAbsorptionAmount = player.getAbsorptionAmount();
                            if (absorptionAmount == 4.0f && previousAbsorptionAmount < absorptionAmount) { // normal Golden Apple
                                player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 100, 1));
                                player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 2400, 0));
                            } else if (absorptionAmount == 16.0f) { // Enchanted Golden Apple
                                player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 400, 1));
                                player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 6000, 0));
                                player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 2400, 3));
                            } // sadly no way to detect normal apple on top of enchanted one I think
                        }
                    }
                }
            }
        });
    }

}
