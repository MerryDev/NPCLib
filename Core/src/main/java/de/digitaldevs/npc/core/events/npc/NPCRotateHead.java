package de.digitaldevs.npc.core.events.npc;

import de.digitaldevs.npc.core.base.AbstractNPC;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called when an NPC rotates it's head
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
public class NPCRotateHead extends Event {

    @Getter public static final HandlerList HANDLER_LIST = new HandlerList();

    @Getter @NotNull private final AbstractNPC npc;
    @NotNull private final Float[] view;

    public float getYaw() {
        return this.view[0];
    }

    public float getPitch() {
        return this.view[1];
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }
}
