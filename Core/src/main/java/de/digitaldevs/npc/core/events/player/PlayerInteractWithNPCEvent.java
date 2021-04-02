package de.digitaldevs.npc.core.events.player;

import de.digitaldevs.npc.core.base.AbstractNPC;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Called when a player interacts with an NPC
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
public class PlayerInteractWithNPCEvent extends Event implements Cancellable {

    @Getter public static final HandlerList HANDLER_LIST = new HandlerList();
    private boolean canceled = false;

    @Getter private final AbstractNPC npc;
    @Getter private final Player player;

    @Override
    public boolean isCancelled() {
        return this.canceled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.canceled = b;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }
}
