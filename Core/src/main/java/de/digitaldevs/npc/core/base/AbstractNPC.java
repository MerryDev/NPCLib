package de.digitaldevs.npc.core.base;

import de.digitaldevs.npc.core.enums.NPCAnimation;
import de.digitaldevs.npc.core.enums.NPCEquipmentSlot;
import de.digitaldevs.npc.core.enums.NPCStatus;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

/**
 * This class represents the abstract base of each NPC in this api.
 *
 * @author MerryChrismas
 * @author <a href='https://digitaldevs.de'>DigitalDevs.de</a>
 * @version 1.0
 * @since 1.0
 */
public interface AbstractNPC {

    /**
     * The abstract method for spawning the NPC
     *
     * @since 1.0
     */
    void spawn();

    /**
     * The abstract method for removing the NPC
     *
     * @since 1.0
     */
    void destroy();

    /**
     * The abstract method for changing the NPC's skin.
     *
     * @param skinOwner the name of an existing player having the skin at this moment. Cannot be null.
     * @see NotNull
     * @since 1.0
     */
    void setSkin(@NotNull String skinOwner);

    /**
     * The abstract method for playing status effects by the NPC.
     *
     * @param status the status effect witch should be played. Cannot be null.
     * @see NPCStatus
     * @see NotNull
     * @since 1.0
     */
    void playStatus(@NotNull NPCStatus status);

    /**
     * The abstract method for letting the NPC focusing a player.
     *
     * @since 1.0
     */
    void focusPlayer();

    /**
     * The abstract method for playing animations by the NPC.
     *
     * @param animation the animation witch should be played by the NPC. Cannot be null.
     * @see NPCAnimation
     * @see NotNull
     * @since 1.0
     */
    void playAnimation(@NotNull NPCAnimation animation);

    /**
     * The abstract method for letting the NPC sleep or leaves the bed.
     *
     * @param state {@code true} if the NPC should sleep; {@code false} if the NPC should leave the bed
     * @since 1.0
     */
    void sleep(boolean state);

    /**
     * The abstract method for letting the NPC sneak or unsneak.
     *
     * @param state {@code true} if the NPC should sneak; {@code false} if the NPC should unsneak
     * @since 1.0
     */
    void sneak(boolean state);

    /**
     * The abstract method for rotating the NPC's head.
     *
     * @param yaw   the rotation on the x-axis
     * @param pitch the rotation on the y-axis
     * @since 1.0
     */
    void rotateHead(float yaw, float pitch);

    /**
     * The abstract method for teleporting the NPC to a new location.
     *
     * @param location the location to witch the NPC should be teleported. Cannot be null.
     * @param onGround {@code true} if the NPC will be standing on a solid ground; {@code false} if the NPC will be located somewhere in the sky
     * @see NotNull
     * @since 1.0
     */
    void teleport(@NotNull Location location, boolean onGround);

    /**
     * The abstract method for equipping the NPC.
     *
     * @param slot the slot where the item should be placed into. Cannot be null.
     * @param item the item wich should be placed into the inventory. Cannot be null.
     * @see NotNull
     * @since 1.0
     */
    void equip(@NotNull NPCEquipmentSlot slot, @NotNull ItemStack item);

    /**
     * Gets the coordinate as an int. <br>
     * <b>Implementation Note: </b>This method is equal in all supported NPC versions in this api.
     *
     * @param coordinate the coordinate
     * @return the coordinate as an int
     * @since 1.0
     */
    default int toInt(double coordinate) {
        return (int) Math.floor(coordinate * 32.0D);
    }

    /**
     * Gets the head rotation as an byte converted into degrees. <br>
     * <b>Implementation Note: </b>This method is equal in all supported NPC versions in this api.
     *
     * @param view the rotation
     * @return the rotation as an byte converted into degrees.
     * @since 1.0
     */
    default byte toByte(float view) {
        return (byte) (int) (view * 256.0D / 360.0D);
    }

    /**
     * Normalizes the head rotation. <br>
     * <b>Implementation Note: </b>This method is equal in all supported NPC versions in this api.
     *
     * @param look the rotation.
     * @return the normalized rotation
     * @since 1.0
     */
    default byte normalizeView(float look) {
        return (byte) (int) (look % 360.0F * 256.0F / 360.0F);
    }

    /**
     * Gets the response from an website by an url. <br>
     * <b>Implementation Note: </b>This method is equal in all supported NPC versions in this api.
     *
     * @param url the url witch refers to the website
     * @return the response
     * @since 1.0
     */
    default String getResponseFromURL(@NotNull String url) {
        StringBuilder text = new StringBuilder();
        try {
            Scanner scanner = new Scanner(new URL(url).openStream());
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                while (line.startsWith(" ")) {
                    line = line.substring(1);
                }
                text.append(line);
            }
            scanner.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return text.toString();
    }

}
