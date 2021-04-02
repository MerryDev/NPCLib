package de.digitaldevs.npc.core.events.npc;

import de.digitaldevs.npc.core.base.AbstractNPC;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called when an NPC focuses an player
 *
 * @author MerryChrismas
 * @author <a href='https://digitaldevs.de'>DigitalDevs.de</a>
 * @version 1.0
 * @see AllArgsConstructor
 * @see Event
 * @see Cancellable
 * @since 1.0
 */
@RequiredArgsConstructor
public class NPCFocusEvent extends Event {

    @Getter public static final HandlerList HANDLER_LIST = new HandlerList();

    @Getter @NotNull private final AbstractNPC npc;
    @Getter @NotNull private final Player player;

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

}
