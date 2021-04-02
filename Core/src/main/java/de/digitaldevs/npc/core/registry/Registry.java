package de.digitaldevs.npc.core.registry;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Collection;

/**
 * This class is an abstract place to store any kind of data in a list.
 *
 * @param <T> The type of data witch should be stored
 * @author MerryChrismas
 * @author <a href='https://digitaldevs.de'>DigitalDevs.de</a>
 * @version 1.0
 * @since 1.0
 */
public class Registry<T> {

    /**
     * The list where the data is stored
     *
     * @see Getter
     */
    @Getter private final Collection<T> data;

    /**
     * Instantiates a new Registry.
     */
    public Registry() {
        this.data = new ArrayList<>();
    }

    /**
     * Registers an object.
     *
     * @param toRegister the object
     */
    public void register(T toRegister) {
        this.data.add(toRegister);
    }

    /**
     * Unregisters an object.
     *
     * @param toRemove the object
     */
    public void unregister(T toRemove) {
        this.data.remove(toRemove);
    }

    /**
     * Gets the object at a certain point in the registry.
     *
     * @param i the index in the registry where the object is listed. <b>Note: Starts at the index of 0</b>
     * @return the object
     */
    public T get(int i) {
        T result = null;
        int index = 0;

        for (T data : this.data) {
            result = data;
            index++;
            if (index == i) break;
        }

        return result;
    }

}
