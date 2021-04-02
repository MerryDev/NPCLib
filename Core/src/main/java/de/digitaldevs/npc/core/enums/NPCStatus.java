package de.digitaldevs.npc.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * This enum is used for handling status effects.
 *
 * @author MerryChrismas
 * @author <a href='https://digitaldevs.de'>DigitalDevs.de</a>
 * @version 1.0
 * @see AllArgsConstructor
 * @since 1.0
 */
@AllArgsConstructor
public enum NPCStatus {

    /**
     * Plays the hurt animation and hurt sound.
     */
    TAKE_DAMAGE(2),

    /**
     * Plays the death sound and death animation.
     */
    DIE(3),

    /**
     * Plays totem of undying animation and sound.
     *
     * @since Minecraft 1.11
     */
    USE_TOTEM_OF_UNDYING(35),

    /**
     * Plays the hurt animation and drown hurt sound.
     *
     * @since Minecraft 1.13
     */
    DROWNING(36),

    /**
     * Plays the hurt animation and burn hurt sound.
     */
    BURN(37),

    /**
     * Plays the hurt animation and sweet berry bush hurt sound.
     *
     * @since Minecraft 1.14
     */
    PRICKED_BY_BUSH(44);

    /**
     * The index witch is used by packets to play the status effect.
     *
     * @see Getter
     */
    @Getter private final int nmsIndex;

}
