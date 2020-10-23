package me.earth.pingbypass.client.modules.autocrystal;

import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerMovementPacket;
import me.earth.earthhack.api.module.Category;
import me.earth.earthhack.api.module.Module;
import me.earth.earthhack.api.setting.Setting;
import me.earth.earthhack.api.setting.settings.BooleanSetting;
import me.earth.earthhack.api.setting.settings.EnumSetting;
import me.earth.earthhack.api.setting.settings.NumberSetting;
import me.earth.earthhack.impl.util.math.DiscreteTimer;
import me.earth.earthhack.impl.util.math.GuardTimer;
import me.earth.pingbypass.PingBypass;
import me.earth.pingbypass.client.modules.autocrystal.modes.Rotate;
import me.earth.pingbypass.client.modules.autocrystal.modes.Target;
import me.earth.pingbypass.mixin.mixins.library.client.IClientPlayerPacket;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

//TODO: AntiTotem, also check if triple pops could be possible
//TODO: DamageSync with FeetSync
//TODO: AntiNaked
//TODO: MinArmor
//TODO: Switch Cooldown stuff and BreakSlot etc.
public class AutoCrystal extends Module
{
    private static final AutoCrystal INSTANCE = new AutoCrystal();

    final Setting<Boolean> place = register(new BooleanSetting("Place", true));
    final Setting<Target> target = register(new EnumSetting<>("Target", Target.Closest));
    final Setting<Float> placeRange = register(new NumberSetting<>("PlaceRange", 6.0f, 0.0f, 6.0f));
    final Setting<Float> placeTrace = register(new NumberSetting<>("PlaceTrace", 6.0f, 0.0f, 6.0f));
    final Setting<Float> minDamage = register(new NumberSetting<>("MinDamage", 6.0f, 0.0f, 20.0f));
    final Setting<Integer> placeDelay = register(new NumberSetting<>("PlaceDelay", 0, 0, 500));
    final Setting<Float> maxSelfP = register(new NumberSetting<>("MaxSelfPlace", 9.0f, 0.0f, 20.0f));
    final Setting<Float> facePlace = register(new NumberSetting<>("FacePlace", 10.0f, 0.0f, 36.0f));
    final Setting<Integer> multiPlace = register(new NumberSetting<>("MultiPlace", 1, 1, 5));
    final Setting<Boolean> countMin = register(new BooleanSetting("CountMin", true));
    final Setting<Boolean> antiSurr = register(new BooleanSetting("AntiSurround", true));
    final Setting<Boolean> newerVer = register(new BooleanSetting("1.13+", false));

    //Break settings
    final Setting<Boolean> explode = register(new BooleanSetting("Break", true));
    final Setting<Float> breakRange = register(new NumberSetting<>("BreakRange", 6.0f, 0.0f, 6.0f));
    final Setting<Float> breakTrace = register(new NumberSetting<>("BreakTrace", 4.5f, 0.0f, 6.0f));
    final Setting<Integer> breakDelay = register(new NumberSetting<>("BreakDelay", 0, 0, 500));
    final Setting<Float> maxSelfB = register(new NumberSetting<>("MaxSelfBreak", 10.0f, 0.0f, 20.0f));
    final Setting<Boolean> instant = register(new BooleanSetting("Instant", false));

    //Important other settings
    final Setting<Rotate> rotate = register(new EnumSetting<>("Rotate", Rotate.None));
    final Setting<Boolean> multiThread = register(new BooleanSetting("MultiThread", false));
    final Setting<Boolean> suicide = register(new BooleanSetting("Suicide", false));

    //Misc settings
    final Setting<Float> range = register(new NumberSetting<>("Range", 12.0f, 6.0f, 12.0f));
    final Setting<Boolean> override = register(new BooleanSetting("Override", false));
    final Setting<Float> minFP = register(new NumberSetting<>("MinFace", 2.0f, 0.1f, 4.0f));
    final Setting<Boolean> noFriendP = register(new BooleanSetting("AntiFriendPop", true));

    //DEV
    final Setting<Integer> cooldown = register(new NumberSetting<>("Cooldown", 500, 0, 500));
    final Setting<Boolean> multiTask     = register(new BooleanSetting("MultiTask", true));
    final Setting<Float> pbTrace = register(new NumberSetting<>("CombinedTrace", 4.5f, 0.0f, 6.0f));
    final Setting<Boolean> fallBack = register(new BooleanSetting("FallBack", true));
    final Setting<Float> fallbackDmg = register(new NumberSetting<>("FB-Dmg", 2.0f, 0.0f, 6.0f));
    final Setting<Boolean> soundR = register(new BooleanSetting("SoundRemove", false));
    final Setting<Boolean> tick = register(new BooleanSetting("Tick", true));

    final Setting<Integer> threadDelay = register(new NumberSetting<>("ThreadDelay", 30, 0, 100));

    final Map<ClientPlayerMovementPacket, DamageCalc> calcs = new ConcurrentHashMap<>();
    final Set<BlockPos> positions = Collections.newSetFromMap(new ConcurrentHashMap<>());

    final DiscreteTimer placeTimer = new GuardTimer().reset(placeDelay.getValue());
    final DiscreteTimer breakTimer = new GuardTimer().reset(breakDelay.getValue());
    final DiscreteTimer threadTimer = new GuardTimer();

    final BreakHelper breakHelper = new BreakHelper(this);
    final PlaceHelper placeHelper = new PlaceHelper(this);
    boolean confirmed;
    EntityPlayer currentTarget;

    private AutoCrystal()
    {
        super("S-AutoCrystal", Category.Combat);
        this.listeners.addAll(new PreListener(this).getListeners());
        this.listeners.addAll(new PostListener(this).getListeners());
        this.listeners.add(new DestroyEntitiesListener(this));
        this.listeners.add(new SpawnObjectListener(this));
        this.listeners.add(new SoundListener(this));
        this.listeners.add(new TickListener(this));
    }

    public static AutoCrystal getInstance()
    {
        return INSTANCE;
    }

    protected void onTick(ClientPlayerMovementPacket packet)
    {
        if (mc.world != null && mc.player != null && threadTimer.passed(threadDelay.getValue()))
        {
            threadTimer.reset(threadDelay.getValue());
            List<Entity> entities;
            synchronized (mc.world.loadedEntityList)
            {
                entities = new ArrayList<>(mc.world.loadedEntityList);
            }

            List<EntityPlayer> players = fromEntities(entities);

            start(packet, entities, players);
        }
    }

    protected void start(ClientPlayerMovementPacket packetIn, List<Entity> entities, List<EntityPlayer> players)
    {
        DamageCalc calc = new DamageCalc(this, players, entities);

        if (packetIn != null)
        {
            calcs.put(packetIn, calc);
        }

        calc.run();

        currentTarget = calc.getTarget();
        float[] rotations = calc.getRotations();
        if (rotations != null && packetIn != null)
        {
            IClientPlayerPacket accessor = (IClientPlayerPacket) packetIn;
            accessor.setYaw(rotations[0]);
            accessor.setPitch(rotations[1]);
        }
    }

    protected void end(ClientPlayerMovementPacket packetIn)
    {
        DamageCalc calc = calcs.remove(packetIn);
        if (calc != null)
        {
            calc.getPackets().forEach(packet -> PingBypass.client.sendToServer(packet));
        }
    }

    private List<EntityPlayer> fromEntities(List<Entity> entities)
    {
        List<EntityPlayer> players = new ArrayList<>();

        for (Entity entity : entities)
        {
            if (isPlayerValid(entity))
            {
                players.add((EntityPlayer) entity);
            }
        }

        return players;
    }

    private boolean isPlayerValid(Entity entity)
    {
        if (!(entity instanceof EntityPlayer))
        {
            return false;
        }

        EntityPlayer player = (EntityPlayer) entity;
        if (player.isDead || player.getHealth() <= 0.0f)
        {
            return false;
        }

        //324 = (placeRange (6.0) + range (12.0)) ^ 2;
        return mc.player.getDistanceSq(entity) <= 324;
    }

}
