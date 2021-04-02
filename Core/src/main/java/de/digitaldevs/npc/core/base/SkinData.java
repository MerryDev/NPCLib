package de.digitaldevs.npc.core.base;

import lombok.AllArgsConstructor;
import lombok.Getter;
/**
 * This class is used for storing the NPC's skin data.
 *
 * @author MerryChrismas
 * @author <a href='https://digitaldevs.de'>DigitalDevs.de</a>
 * @version 1.0
 * @see AllArgsConstructor
 * @since 1.0
 */
@AllArgsConstructor
public class SkinData {

    /**
     * The signature of the skin
     */
    @Getter private final String signature;

    /**
     * The value of the skin
     */
    @Getter private final String value;

}
