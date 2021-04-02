package de.digitaldevs.npc.v_1_14_R1;

import de.digitaldevs.npc.core.NpcAPI;
import de.digitaldevs.npc.core.Plugin;
import de.digitaldevs.npc.core.base.AbstractNPC;
import de.digitaldevs.npc.core.events.EventHandler;
import de.digitaldevs.npc.core.events.EventHandler.PlayerEventType;
import de.digitaldevs.npc.core.events.player.PlayerAttackNPCEvent;
import de.digitaldevs.npc.core.events.player.PlayerInteractWithNPCEvent;
import de.digitaldevs.npc.core.reflection.FieldAccessor;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import lombok.RequiredArgsConstructor;

import net.minecraft.server.v1_14_R1.Packet;
import org.bukkit.Bukkit;

import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.Iterator;
import java.util.List;

/**
 * This class is for handling incoming packets in minecraft's version 1.14
 *
 * @author MerryChrismas
 * @author <a href='https://digitaldevs.de'>DigitalDevs.de</a>
 * @version 1.0
 * @see RequiredArgsConstructor
 * @since 1.0
 */
@RequiredArgsConstructor
public class PacketReader {

    /**
     * The player from witch connection the packets should be read
     */
    private final Player player;

    /**
     * The channel of the player's connection where all packets are sent through
     */
    private static Channel channel;

    /**
     * The time when the player interacted with an NPC for the last time
     */
    private static Long lastInteract;

    /**
     * The time when the player attacked an NPC for the last time
     */
    private static Long lastAttack;

    /**
     * Adds an intermediate step to reading the packets in the process.
     *
     * @since 1.0
     */
    public void inject() {
        CraftPlayer craftPlayer = (CraftPlayer) player;
        channel = craftPlayer.getHandle().playerConnection.networkManager.channel;
        channel.pipeline().addAfter("decoder", "PacketInjector", new MessageToMessageDecoder<Packet<?>>() {
            @Override
            protected void decode(ChannelHandlerContext channelHandlerContext, Packet<?> packet, List<Object> list) {
                readPacket(packet);
                list.add(packet);
            }
        });
    }

    /**
     * Removes the intermediate step for reading the packets.
     *
     * @since 1.0
     */
    public void eject() {
        if (channel.pipeline().get("decoder") != null)
            channel.pipeline().remove("PacketInjector");
    }

    /**
     * Reads the incoming packet. Calls the {@link PlayerInteractWithNPCEvent} or the {@link PlayerAttackNPCEvent} if the packet is an instance of the {@code PacketPlayInUseEntity}. <br>
     * The {@link PlayerInteractWithNPCEvent} can be called every 0.3 seconds. <br>
     * The {@link PlayerAttackNPCEvent} can be called every half (0.5) seconds.
     *
     * @param packet the incoming packet
     * @since 1.0
     */
    public void readPacket(Packet<?> packet) {
        Bukkit.getScheduler().runTaskAsynchronously(Plugin.getInstance(), () -> {

            if (packet.getClass().getSimpleName().equals("PacketPlayInUseEntity")) {
                Iterator<AbstractNPC> iterator = NpcAPI.getApi().getRegistry().getData().iterator();

                while (true) {
                    if (!iterator.hasNext()) return;
                    AbstractNPC npc = iterator.next();

                    if (FieldAccessor.get(packet, "action").toString().equalsIgnoreCase("ATTACK")) {
                        boolean canCallEvent;

                        if (lastAttack == null) {
                            canCallEvent = true;
                            lastAttack = System.currentTimeMillis();

                        } else if (lastAttack + 500L <= System.currentTimeMillis()) {
                            canCallEvent = true;
                            lastAttack = System.currentTimeMillis();

                        } else canCallEvent = false;

                        if (canCallEvent)
                            new EventHandler<Player>(npc).invoke(player).callEvent(PlayerEventType.ATTACK);

                    }
                    if (FieldAccessor.get(packet, "action").toString().equalsIgnoreCase("INTERACT_AT")) {
                        boolean canCallEvent;

                        if (lastInteract == null) {
                            canCallEvent = true;
                            lastInteract = System.currentTimeMillis();

                        } else if (lastInteract + 300L <= System.currentTimeMillis()) {
                            lastInteract = System.currentTimeMillis();
                            canCallEvent = true;

                        } else canCallEvent = false;

                        if (canCallEvent)
                            new EventHandler<Player>(npc).invoke(player).callEvent(PlayerEventType.INTERACT);
                    }
                }
            }

        });
    }

}
