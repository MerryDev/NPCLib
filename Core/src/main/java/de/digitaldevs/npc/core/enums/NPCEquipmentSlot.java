package de.digitaldevs.npc.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * This class is used for handling the slots where items can be placed in the NPC's inventory. <br>
 * <b>Implementation Note: </b> Offhand is supported from minecraft's version 1.9
 *
 * @author MerryChrismas
 * @author <a href='https://digitaldevs.de'>DigitalDevs.de</a>
 * @version 1.0
 * @see AllArgsConstructor
 * @since 1.0
 */
@AllArgsConstructor
public enum NPCEquipmentSlot {

    /**
     * The slot witch is used by the default hand
     */
    MAIN_HAND("mainhand", 0),

    /**
     * The slot witch is used for the second hand
     */
    OFF_HAND("offhand", 5),

    /**
     * The slot witch is used for the boots
     */
    BOOTS("feet", 1),

    /**
     * The slot witch is used for the leggings
     */
    LEGGINGS("legs", 2),

    /**
     * The slot witch is used for the chestplate
     */
    CHESTPLATE("chest", 3),

    /**
     * The slot witch is sued for the helmet
     */
    HELMET("head", 3);

    /**
     * The name of the slot witch is defined in the EnumItemSlot (supported from minecraft's version 1.9). <br>
     * Used from NPCs since minecraft's version 1.9.
     */
    @Getter private final String name;

    /**
     * The index witch is used by the packet to set items in an inventory
     */
    @Getter private final int nmsIndex;

}
