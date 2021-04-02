package de.digitaldevs.npc.core.events;

import de.digitaldevs.npc.core.NpcAPI;
import de.digitaldevs.npc.core.base.AbstractNPC;
import de.digitaldevs.npc.core.base.SkinData;
import de.digitaldevs.npc.core.enums.NPCAnimation;
import de.digitaldevs.npc.core.enums.NPCEquipmentSlot;
import de.digitaldevs.npc.core.enums.NPCStatus;
import de.digitaldevs.npc.core.events.npc.*;
import de.digitaldevs.npc.core.events.player.PlayerAttackNPCEvent;
import de.digitaldevs.npc.core.events.player.PlayerInteractWithNPCEvent;
import de.digitaldevs.npc.core.registry.Registry;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.security.InvalidParameterException;

/**
 * This class is used for handling events called by an NPC.
 *
 * @param <E> the type of data witch should be invoked. Can be left blank to not specify the type of data.
 * @author MerryChrismas
 * @author <a href='https://digitaldevs.de'>DigitalDevs.de</a>
 * @version 1.0
 * @see RequiredArgsConstructor
 * @since 1.0
 */
@RequiredArgsConstructor
public class EventHandler<E> {

    /**
     * The registry where the NPCs are stored in
     */
    private final Registry<AbstractNPC> registry = NpcAPI.getApi().getRegistry();

    /**
     * The NPC
     */
    private final AbstractNPC npc;

    /**
     * The NPC's skin
     */
    private SkinData skinData;

    /**
     * The NPC's location
     */
    private Location location;

    /**
     * The animation played by the NPC
     */
    private NPCAnimation animation;

    /**
     * The status effect played by the NPC
     */
    private NPCStatus status;

    /**
     * The slot where an item is placed
     */
    private NPCEquipmentSlot slot;

    /**
     * The player witch sees the NPC
     */
    private Player player;

    /**
     * The item witch should be placed in the NPC's inventory
     */
    private ItemStack item;

    /**
     * The values for rotating the NPC's head <br>
     * Index 0 is the yaw, index 1 the pitch
     */
    private Float[] view;

    /**
     * The state weather the NPC is sleeping or sneaking. <br>
     * False if the NPC stopps sneaking or sleeping
     */
    private Boolean state;

    /**
     * Invokes the needed data for calling an specific event.
     *
     * @param invokable the data. Cannot be null.
     * @return the instance of the EventHandler witch is ready to call an event
     * @throws InvalidParameterException is thrown when data was invoked that doesn't fit with data of the events <br>
     *                                   Suitable types of data are: {@link SkinData}, {@link Location}, {@link NPCAnimation}, {@link NPCStatus}, {@link NPCEquipmentSlot}, {@link Player}, {@link ItemStack}, {@link Float} as an array, {@link Boolean},
     * @see NotNull
     */
    public EventHandler<E> invoke(@NotNull E invokable) {
        if (invokable instanceof SkinData) this.skinData = (SkinData) invokable;
        else if (invokable instanceof Location) this.location = (Location) invokable;
        else if (invokable instanceof NPCAnimation) this.animation = (NPCAnimation) invokable;
        else if (invokable instanceof NPCStatus) this.status = (NPCStatus) invokable;
        else if (invokable instanceof NPCEquipmentSlot) this.slot = (NPCEquipmentSlot) invokable;
        else if (invokable instanceof Player) this.player = (Player) invokable;
        else if (invokable instanceof ItemStack) this.item = (ItemStack) invokable;
        else if (invokable instanceof Float[]) this.view = (Float[]) invokable;
        else if (invokable instanceof Boolean) this.state = (Boolean) invokable;
        else
            throw new InvalidParameterException("The invokable must be type of SkinData, Location, NPCAnimation, NPCStatus, NPCEquipmentSlot, Player, ItemStack, Float[] or Boolean!");
        return this;
    }

    /**
     * Calls an event witch can be called by an NPC.
     *
     * @param eventType the type of the event witch should be called. Cannot be null.
     * @see NotNull
     */
    public void callEvent(@NotNull NPCEventType eventType) {
        switch (eventType) {
            case SPAWN:
                Bukkit.getPluginManager().callEvent(new NPCSpawnEvent(this.npc));
                registry.register(this.npc);
                break;
            case DESTROY:
                Bukkit.getPluginManager().callEvent(new NPCDestroyEvent(this.npc));
                registry.unregister(this.npc);
                break;
            case UPDATE_SKIN:
                Bukkit.getPluginManager().callEvent(new NPCUpdateSkinEvent(this.npc, this.skinData));
                break;
            case PLAY_ANIMATION:
                Bukkit.getPluginManager().callEvent(new NPCPlayAnimationEvent(this.npc, this.animation));
                break;
            case PLAY_STATUS:
                Bukkit.getPluginManager().callEvent(new NPCPlayStatusEvent(this.npc, this.status));
                break;
            case ROTATE_HEAD:
                Bukkit.getPluginManager().callEvent(new NPCRotateHead(this.npc, this.view));
                break;
            case TELEPORT:
                Bukkit.getPluginManager().callEvent(new NPCTeleportEvent(this.npc, this.location));
                break;
            case FOCUS_PLAYER:
                Bukkit.getPluginManager().callEvent(new NPCFocusEvent(this.npc, this.player));
                break;
            case SLEEP:
                Bukkit.getPluginManager().callEvent(new NPCSleepEvent(this.npc, this.state));
                break;
            case SNEAK:
                Bukkit.getPluginManager().callEvent(new NPCSneakEvent(this.npc, this.state));
            case EQUIP:
                Bukkit.getPluginManager().callEvent(new NPCEquipEvent(this.npc, this.slot, this.item));
                break;
        }
    }

    /**
     * Calls an event witch can be called by an player.
     *
     * @param eventType the type of event. Cannot be null.
     * @see NotNull
     */
    public void callEvent(@NotNull PlayerEventType eventType) {
        switch (eventType) {
            case ATTACK:
                Bukkit.getPluginManager().callEvent(new PlayerAttackNPCEvent(this.npc, this.player));
                break;
            case INTERACT:
                Bukkit.getPluginManager().callEvent(new PlayerInteractWithNPCEvent(this.npc, this.player));
                break;
        }
    }

    /**
     * Types of events witch can be called by an NPC.
     */
    public enum NPCEventType {
        SPAWN, DESTROY, UPDATE_SKIN, PLAY_ANIMATION, PLAY_STATUS, ROTATE_HEAD, TELEPORT, FOCUS_PLAYER, SLEEP, SNEAK, EQUIP
    }

    /**
     * Types of events witch can be called by an player.
     */
    public enum PlayerEventType {
        INTERACT, ATTACK
    }

}
