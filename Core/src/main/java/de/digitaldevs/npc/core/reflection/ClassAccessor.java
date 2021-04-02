package de.digitaldevs.npc.core.reflection;

import lombok.SneakyThrows;

/**
 * This class is used for instantiating classes.
 *
 * @author MerryChrismas
 * @author <a href='https://digitaldevs.de'>DigitalDevs.de</a>
 * @version 1.0
 * @since 1.0
 */
public class ClassAccessor {

    /**
     * Gets the instance of a class. <br>
     * <b>Note:</b> Subclasses can be accessed by the following: 'OuterClassName$InnerClassName'
     *
     * @param clazzName the name of the class.
     * @return the instance of the class
     * @see SneakyThrows
     */
    @SneakyThrows
    public static Class<?> getClass(String clazzName) {
        return Class.forName(clazzName);
    }

    /**
     * Gets a minecraft server's class as an instance of it.
     *
     * @param versionPacketName the net.minecraft.server.'version' String
     * @param clazzName         the name of the class
     * @return the instance of the class
     * @see #getClass(String)
     */
    public static Class<?> getNMSClass(String versionPacketName, String clazzName) {
        return getClass("net.minecraft.server." + versionPacketName + "." + clazzName);
    }

}
