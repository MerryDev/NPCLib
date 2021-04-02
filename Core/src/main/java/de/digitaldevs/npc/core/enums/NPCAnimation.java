package de.digitaldevs.npc.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * This enum is used for handling animations.
 *
 * @author MerryChrismas
 * @author <a href='https://digitaldevs.de'>DigitalDevs.de</a>
 * @version 1.0
 * @see AllArgsConstructor
 * @since 1.0
 */
@AllArgsConstructor
public enum NPCAnimation {

    SWING_MAIN_ARM(0),
    TAKE_DAMAGE(1),
    LEAVE_BED(2),
    SWING_OFFHAND(3),
    CRITICAL_EFFECT(4),
    MAGICAL_CRITICAL_EFFECT(5);

    /**
     * The index witch is used by the packet to play the animation
     */
    @Getter private final int nmsIndex;

}
