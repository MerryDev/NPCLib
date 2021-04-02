package de.digitaldevs.npc.core;

import de.digitaldevs.npc.core.base.AbstractNPC;
import de.digitaldevs.npc.core.registry.Registry;
import lombok.Getter;

/**
 * This class is used to access the registry where all NPCs are stored in.
 *
 * @author MerryChrismas
 * @author <a href='https://digitaldevs.de'>DigitalDevs.de</a>
 * @version 1.0
 * @since 1.0
 */
public class NpcAPI {

    /**
     * Static reference of this class
     *
     * @see Getter
     */
    @Getter private static NpcAPI api;

    /**
     * The registry
     *
     * @see Getter
     */
    @Getter private final Registry<AbstractNPC> registry;

    /**
     * Instantiates a new Npc api.
     *
     * @param registry the registry
     */
    public NpcAPI(Registry<AbstractNPC> registry) {
        api = this;
        this.registry = registry;
    }
}
