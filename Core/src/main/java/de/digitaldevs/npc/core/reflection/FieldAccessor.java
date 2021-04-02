package de.digitaldevs.npc.core.reflection;

import lombok.SneakyThrows;

import java.lang.reflect.Field;

/**
 * This class is used for accessing fields of an object.
 *
 * @author MerryChrismas
 * @author <a href='https://digitaldevs.de'>DigitalDevs.de</a>
 * @version 1.0
 * @since 1.0
 */
public class FieldAccessor {

    /**
     * Sets a new value to a specific field.
     *
     * @param toModify  the object where the field is located
     * @param fieldName the name of the field (variable's name)
     * @param toSet     the new value
     * @see SneakyThrows
     */
    @SneakyThrows
    public static void set(Object toModify, String fieldName, Object toSet) {
        Field field = toModify.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(toModify, toSet);
    }

    /**
     * Gets the value of a specific field.
     *
     * @param target    the object where the field is located.
     * @param fieldName the name of the field (variable's name)
     * @return the value of the filed
     * @see SneakyThrows
     */
    @SneakyThrows
    public static Object get(Object target, String fieldName) {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(target);
    }

}
