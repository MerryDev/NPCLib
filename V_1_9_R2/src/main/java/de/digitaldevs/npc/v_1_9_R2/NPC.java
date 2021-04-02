package de.digitaldevs.npc.v_1_9_R2;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import de.digitaldevs.npc.core.Plugin;
import de.digitaldevs.npc.core.base.AbstractNPC;
import de.digitaldevs.npc.core.base.SkinData;
import de.digitaldevs.npc.core.enums.NPCAnimation;
import de.digitaldevs.npc.core.enums.NPCEquipmentSlot;
import de.digitaldevs.npc.core.enums.NPCStatus;
import de.digitaldevs.npc.core.events.EventHandler;
import de.digitaldevs.npc.core.events.EventHandler.NPCEventType;
import de.digitaldevs.npc.core.events.npc.*;
import de.digitaldevs.npc.core.reflection.ClassAccessor;
import de.digitaldevs.npc.core.reflection.FieldAccessor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_9_R2.*;
import net.minecraft.server.v1_9_R2.PacketPlayOutEntity.PacketPlayOutEntityLook;
import net.minecraft.server.v1_9_R2.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_9_R2.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_9_R2.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_9_R2.util.CraftChatMessage;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.UUID;

/**
 * This class represents an NPC in minecraft's version 1.9
 *
 * @author MerryChrismas
 * @author <a href='https://digitaldevs.de'>DigitalDevs.de</a>
 * @version 1.0
 * @since 1.0
 */
public class NPC implements AbstractNPC {

    /**
     * The version of the running server
     */
    private final String serverVersion = this.getVersionPacket();

    /**
     * The unique id of the NPC
     *
     * @see Getter
     */
    @Getter private final int entityID;

    /**
     * The NPC's GameProfile
     *
     * @see Getter
     */
    @Getter private final GameProfile gameProfile;

    /**
     * The data of the NPC's skin
     *
     * @see SkinData
     * @see Getter
     */
    @Getter private SkinData skinData;

    /**
     * The DataWatcher of the NPC
     *
     * @see Getter
     */
    @Getter private final DataWatcher dataWatcher;

    /**
     * The player witch will see the NPC
     *
     * @see Getter
     */
    @Getter private final Player receiver;

    /**
     * The location where the NPC is located
     *
     * @see Getter
     * @see Setter
     */
    @Getter @Setter private Location location;

    /**
     * Defines weather the NPC will be visible on the player list
     *
     * @see Getter
     * @see Setter
     */
    @Getter @Setter private boolean visibleOnTab;

    /**
     * Instantiates a new NPC.
     *
     * @param displayName  the name witch will be visible above the NPC's head. Cannot be null.
     * @param location     the location where the NPC will be located. Cannot be null.
     * @param visibleOnTab weather the NPC will be visible on the player list. Cannot be null.
     * @param receiver     the player that will see the NPC. Cannot be null.
     * @see NotNull
     * @since 1.0
     */
    public NPC(@NotNull String displayName, @NotNull Location location, boolean visibleOnTab, @NotNull Player receiver) {
        this.receiver = receiver;
        this.entityID = (int) (Math.random() * 1000) + 2000;
        this.gameProfile = new GameProfile(UUID.randomUUID(), displayName);
        this.dataWatcher = new DataWatcher(null);
        this.setLocation(location);
        this.setVisibleOnTab(visibleOnTab);
    }

    /**
     * Spawns the NPC and calls the {@link NPCSpawnEvent}. <br>
     * If {@code visibleOnTab} is equal to {@code true} the NPC will be added to the tablist. <br>
     * If {@code visibleOnTab} is equal to {@code false} the NPC will be removed from the tablist within 2 ticks (~ 0.1s)
     *
     * @since 1.0
     */
    @Override
    public void spawn() {
        PacketPlayOutNamedEntitySpawn packet = new PacketPlayOutNamedEntitySpawn();
        FieldAccessor.set(packet, "a", this.entityID);
        FieldAccessor.set(packet, "b", this.gameProfile.getId());
        FieldAccessor.set(packet, "c", this.location.getX());
        FieldAccessor.set(packet, "d", this.location.getY());
        FieldAccessor.set(packet, "e", this.location.getZ());
        FieldAccessor.set(packet, "f", this.toByte(this.location.getYaw()));
        FieldAccessor.set(packet, "g", this.toByte(this.location.getPitch()));
        FieldAccessor.set(packet, "h", this.dataWatcher);
        FieldAccessor.set(packet, "i", null);

        this.modifyPlayerList(EnumPlayerInfoAction.ADD_PLAYER);
        if (!this.visibleOnTab)
            Bukkit.getScheduler().runTaskLaterAsynchronously(Plugin.getInstance(), () -> this.modifyPlayerList(EnumPlayerInfoAction.REMOVE_PLAYER), 2L);

        this.sendPacket(packet);
        new EventHandler<>(this).callEvent(NPCEventType.SPAWN);
    }

    /**
     * Destroys the NPC and calls the {@link NPCDestroyEvent}.
     *
     * @since 1.0
     */
    @Override
    public void destroy() {
        PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(this.entityID);
        this.modifyPlayerList(EnumPlayerInfoAction.REMOVE_PLAYER);

        this.sendPacket(packet);
        new EventHandler<>(this).callEvent(NPCEventType.DESTROY);
    }

    /**
     * Changes the skin of the NPC and calls the {@link NPCUpdateSkinEvent}.
     *
     * @param skinOwner the name of an existing player having the skin at this moment. Cannot be null.
     * @see NotNull
     * @since 1.0
     */
    @Override
    public void setSkin(@NotNull String skinOwner) {
        Gson gson = new Gson();
        String url = "https://api.mojang.com/users/profiles/minecraft/" + skinOwner;
        String json = this.getResponseFromURL(url);
        String uuid = (gson.fromJson(json, JsonObject.class)).get("id").getAsString();
        url = "https://sessionserver.mojang.com/session/minecraft/profile/" + uuid + "?unsigned=false";
        json = this.getResponseFromURL(url);
        JsonObject mainObject = gson.fromJson(json, JsonObject.class);
        JsonObject jObject = mainObject.get("properties").getAsJsonArray().get(0).getAsJsonObject();
        String value = jObject.get("value").getAsString();
        String signature = jObject.get("signature").getAsString();
        this.skinData = new SkinData(signature, value);
        this.gameProfile.getProperties().put("textures", new Property("textures", value, signature));

        new EventHandler<SkinData>(this).invoke(this.skinData).callEvent(NPCEventType.UPDATE_SKIN);
    }

    /**
     * Plays an status effect by the NPC and calls the {@link NPCPlayStatusEvent}.
     *
     * @param status the status effect witch should be played. Cannot be null.
     * @see NPCStatus
     * @see NotNull
     * @since 1.0
     */
    @Override
    public void playStatus(@NotNull NPCStatus status) {
        PacketPlayOutEntityStatus packet = new PacketPlayOutEntityStatus();
        FieldAccessor.set(packet, "a", this.entityID);
        FieldAccessor.set(packet, "b", (byte) status.getNmsIndex());

        this.sendPacket(packet);
        new EventHandler<NPCStatus>(this).invoke(status).callEvent(NPCEventType.PLAY_STATUS);
    }

    /**
     * Focuses the {@code receiver} and calls the {@link NPCFocusEvent}. <br>
     *
     * @since 1.0
     */
    @Override
    public void focusPlayer() {
        this.location.setDirection(this.receiver.getLocation().subtract(this.location).toVector());

        float yaw = this.location.getYaw();
        float pitch = this.location.getPitch();

        PacketPlayOutEntityLook packet = new PacketPlayOutEntityLook(this.entityID, this.normalizeView(yaw), this.normalizeView(pitch), false);

        this.sendPacket(packet);
        this.rotateHead(yaw, pitch);
        new EventHandler<Player>(this).invoke(this.receiver).callEvent(NPCEventType.FOCUS_PLAYER);
    }

    /**
     * Plays an animation by the NPC and calls the {@link NPCPlayAnimationEvent}.
     *
     * @param animation the animation witch should be played by the NPC. Cannot be null.
     * @see NPCAnimation
     * @see NotNull
     * @since 1.0
     */
    @Override
    public void playAnimation(@NotNull NPCAnimation animation) {
        PacketPlayOutAnimation packet = new PacketPlayOutAnimation();
        FieldAccessor.set(packet, "a", this.entityID);
        FieldAccessor.set(packet, "bb", animation.getNmsIndex());

        this.sendPacket(packet);
        new EventHandler<NPCAnimation>(this).invoke(animation).callEvent(NPCEventType.PLAY_ANIMATION);
    }

    /**
     * Lets the NPC sleep or leave a bed and calls the {@link NPCSleepEvent}.
     *
     * @param state {@code true} if the NPC should sleep; {@code false} if the NPC should leave the bed
     * @since 1.0
     */
    @Override
    public void sleep(boolean state) {
        if (state) {
            Location bedLocation = new Location(this.location.getWorld(), 1D, 1D, 1D);

            PacketPlayOutBed packet = new PacketPlayOutBed();
            FieldAccessor.set(packet, "a", this.entityID);
            FieldAccessor.set(packet, "b", new BlockPosition(bedLocation.getX(), bedLocation.getY(), bedLocation.getZ()));

            this.receiver.sendBlockChange(bedLocation, Material.BED_BLOCK, (byte) 0);

            this.sendPacket(packet);

        } else {
            this.playAnimation(NPCAnimation.LEAVE_BED);
        }

        this.teleport(this.location.clone().subtract(0D, 0.3D, 0D), true);
        new EventHandler<Boolean>(this).invoke(state).callEvent(NPCEventType.SLEEP);
    }

    /**
     * Lets the NPC sneak or unsneak and calls the {@link NPCSneakEvent}.
     *
     * @param state {@code true} if the NPC should sneak; {@code false} if the NPC should unsneak
     * @since 1.0
     */
    @Override
    public void sneak(boolean state) {
        DataWatcher dataWatcher = ((CraftPlayer) this.receiver).getHandle().getDataWatcher();
        dataWatcher.set(new DataWatcherObject<>(0, DataWatcherRegistry.a), state ? (byte) 0x02 : (byte) 0x00);
        PacketPlayOutEntityMetadata packet = new PacketPlayOutEntityMetadata(this.entityID, dataWatcher, false);

        this.sendPacket(packet);
        new EventHandler<Boolean>(this).invoke(state).callEvent(NPCEventType.SNEAK);
    }

    /**
     * Rotates the NPC's head and calls the {@link NPCRotateHead}.
     *
     * @param yaw   the rotation on the x-axis
     * @param pitch the rotation on the y-axis
     * @since 1.0
     */
    @Override
    public void rotateHead(float yaw, float pitch) {
        PacketPlayOutEntityLook packetLook = new PacketPlayOutEntityLook(this.entityID, this.toByte(yaw), this.toByte(pitch), true);
        PacketPlayOutEntityHeadRotation packetRotation = new PacketPlayOutEntityHeadRotation();

        FieldAccessor.set(packetRotation, "a", this.entityID);
        FieldAccessor.set(packetRotation, "b", this.toByte(yaw));

        this.sendPacket(packetLook);
        this.sendPacket(packetRotation);
        new EventHandler<Float[]>(this).invoke(new Float[]{yaw, pitch}).callEvent(NPCEventType.ROTATE_HEAD);
    }

    /**
     * Teleports the NPC to a new location and calls the {@link NPCTeleportEvent}.
     *
     * @param location the location to witch the NPC should be teleported. Cannot be null.
     * @param onGround {@code true} if the NPC will be standing on a solid ground; {@code false} if the NPC will be located somewhere in the sky
     * @see NotNull
     * @since 1.0
     */
    @Override
    public void teleport(@NotNull Location location, boolean onGround) {
        PacketPlayOutEntityTeleport packet = new PacketPlayOutEntityTeleport();
        FieldAccessor.set(packet, "a", this.entityID);
        FieldAccessor.set(packet, "b", location.getZ());
        FieldAccessor.set(packet, "c", location.getY());
        FieldAccessor.set(packet, "d", location.getX());
        FieldAccessor.set(packet, "e", this.toByte(location.getYaw()));
        FieldAccessor.set(packet, "f", this.toByte(location.getPitch()));
        FieldAccessor.set(packet, "g", onGround);

        this.setLocation(location);
        this.rotateHead(location.getYaw(), location.getPitch());
        this.sendPacket(packet);
        new EventHandler<Location>(this).invoke(location).callEvent(NPCEventType.TELEPORT);
    }

    /**
     * Equips the NPC with an item and calls the {@link NPCEquipEvent}.
     *
     * @param slot the slot where the item should be placed into. Cannot be null.
     * @param item the item wich should be placed into the inventory. Cannot be null.
     * @see NPCEquipmentSlot
     * @see NotNull
     * @since 1.0
     */
    @Override
    public void equip(@NotNull NPCEquipmentSlot slot, @NotNull ItemStack item) {
        PacketPlayOutEntityEquipment packet = new PacketPlayOutEntityEquipment();
        FieldAccessor.set(packet, "a", this.entityID);
        FieldAccessor.set(packet, "b", EnumItemSlot.a(slot.getName()));
        FieldAccessor.set(packet, "c", CraftItemStack.asCraftCopy(item));

        this.sendPacket(packet);
        new EventHandler<>(this).invoke(slot).invoke(item).callEvent(NPCEventType.EQUIP);
    }

    /**
     * Modifies the player list. <br>
     * <b>Implementation Note: </b> The method was written to add or remove the instance of the NPC's GameProfile and should only be used for that.
     *
     * @param action the modification that should be performed. Cannot be null.
     * @see EnumPlayerInfoAction
     * @see NotNull
     * @since 1.0
     */
    private void modifyPlayerList(@NotNull EnumPlayerInfoAction action) {
        PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo();

        try {
            Class<?> dataClass = ClassAccessor.getNMSClass(this.serverVersion, "PacketPlayOutPlayerInfo$PlayerInfoData");
            Constructor<?> dataConstructor = dataClass.getDeclaredConstructor(PacketPlayOutPlayerInfo.class, GameProfile.class, int.class, IChatBaseComponent.class);
            Object npcInstance = dataConstructor.newInstance(packet, this.gameProfile, 1, CraftChatMessage.fromString(this.gameProfile.getName())[0]);

            @SuppressWarnings("unchecked")
            List<Object> currentPlayerList = (List<Object>) FieldAccessor.get(packet, "b");
            currentPlayerList.add(npcInstance);

            FieldAccessor.set(packet, "a", action);
            FieldAccessor.set(packet, "b", currentPlayerList);

            this.sendPacket(packet);

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Sends a packet to the {@code receiver}.
     *
     * @param packet the packet witch should be send. Cannot be null.
     * @see NotNull
     * @since 1.0
     */
    private void sendPacket(@NotNull Packet<?> packet) {
        ((CraftPlayer) this.receiver).getHandle().playerConnection.sendPacket(packet);
    }

    /**
     * Returns the version of the server. It is located in the 'net.minecraft.server' package.
     *
     * @return the version of the server
     * @since 1.0
     */
    private String getVersionPacket() {
        final String packageName = Bukkit.getServer().getClass().getPackage().getName();
        return packageName.substring(packageName.lastIndexOf('.') + 1);
    }

}
