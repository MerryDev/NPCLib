package de.digitaldevs.npc.core.metrics;

import de.digitaldevs.npc.core.Plugin;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@NoArgsConstructor(access = AccessLevel.NONE)
public class NPCMetrics {

    private static final String VERSION = "1.0.0-beta1";
    private static final ScheduledExecutorService EXECUTOR = Executors.newScheduledThreadPool(2);

    private static Metrics metrics;
    private static Metrics.Graph graph;

    public static void start(Plugin aPlugin) {
        if (metrics != null) return;
        File dataFolder = aPlugin.getDataFolder().getParentFile();
        try {
            metrics = new Metrics(EXECUTOR, dataFolder, "NPCLib", VERSION);
            graph = metrics.createGraph("Using Plugins");
            metrics.start();
        } catch (IOException ignored) {
        }
    }

    private static final Set<String> usingPlugins = new HashSet<>();

    public static void addUsingPlugin(Plugin plugin) {
        if (metrics == null) start(plugin);
        if (usingPlugins.contains(plugin.getName())) return;
        graph.addPlotter(new Metrics.Plotter(plugin.getName()) {

            @Override
            public int getValue() {
                return 1;
            }
        });
        usingPlugins.add(plugin.getName());
    }

}
