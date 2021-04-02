package de.digitaldevs.npc.core;

import de.digitaldevs.npc.core.metrics.NPCMetrics;
import de.digitaldevs.npc.core.registry.Registry;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public class Plugin extends JavaPlugin {

    @Getter static Plugin instance;

    @Override
    public void onEnable() {
        instance = this;
        NPCMetrics.addUsingPlugin(this);
        new NpcAPI(new Registry<>());
    }
}
