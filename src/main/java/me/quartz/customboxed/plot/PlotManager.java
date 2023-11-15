package me.quartz.customboxed.plot;

import me.quartz.customboxed.CustomBoxed;
import me.quartz.customboxed.files.CustomFile;
import me.quartz.customboxed.utils.FileUtils;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public class PlotManager {

    private final List<Plot> plots;

    public PlotManager() {
        this.plots = new ArrayList<>();
        deserialize();
    }

    public Plot getPlot(UUID uuid) {
        return plots.stream().filter(plot -> plot.getOwner().toString().equals(uuid.toString())).findAny().orElse(null);
    }

    public Plot getPlot(Location location) {
        return plots.stream().filter(plot -> plot.getLocation() == location).findAny().orElse(null);
    }

    public void createPlot(Player player) {
        Location location = getPosition();
        location.setY(Objects.requireNonNull(location.getWorld()).getHighestBlockYAt(location));
        
        World world = Bukkit.getWorld(Objects.requireNonNull(CustomBoxed.getInstance().getConfig().getString("world")));
        if(world == null) {
            WorldCreator worldCreator = new WorldCreator(Objects.requireNonNull(CustomBoxed.getInstance().getConfig().getString("world")));
            worldCreator.createWorld();
        }
        world = Bukkit.getWorld(Objects.requireNonNull(CustomBoxed.getInstance().getConfig().getString("world")));
        location.setWorld(world);

        player.teleport(location);

        Location center = getEmptyPosition();

        Plot plot = new Plot(player.getUniqueId(), center);
        plots.add(plot);
        serialize(plot);

        WorldBorder border = Bukkit.createWorldBorder();
        border.setCenter(player.getLocation());
        border.setSize(plot.getBlocks());

        player.setWorldBorder(border);
    }

    private Location getEmptyPosition() {
        Location location = getPosition();
        return getPlot(location) == null ? location : getEmptyPosition();
    }

    private Location getPosition() {
        Location location = new Location(Bukkit.getWorld("world"), 0, 0, 0);
        int curX = CustomBoxed.getInstance().getConfig().getInt("cur-line-x");
        int curY = CustomBoxed.getInstance().getConfig().getInt("cur-line-y");
        int lineX = CustomBoxed.getInstance().getConfig().getInt("lines-x");
        int lineY = CustomBoxed.getInstance().getConfig().getInt("lines-y");
        boolean x = CustomBoxed.getInstance().getConfig().getBoolean("x");

        if(x) {
            location.setX(location.getX()+((curX * CustomBoxed.getInstance().getConfig().getInt("max-border")) + CustomBoxed.getInstance().getConfig().getInt("apart") + (int) (CustomBoxed.getInstance().getConfig().getInt("default-border") / 2) + 1));
            location.setZ(location.getZ()+((lineY * CustomBoxed.getInstance().getConfig().getInt("max-border")) + CustomBoxed.getInstance().getConfig().getInt("apart") + (int) (CustomBoxed.getInstance().getConfig().getInt("default-border") / 2) + 1));
            if(curX == lineX) {
                CustomBoxed.getInstance().getConfig().set("x", false);
                CustomBoxed.getInstance().getConfig().set("cur-line-x", 0);
                CustomBoxed.getInstance().getConfig().set("lines-y", lineY + 1);
            } else {
                CustomBoxed.getInstance().getConfig().set("cur-line-x", curX+1);
            }
        } else {
            location.setX(location.getX()+((lineX * CustomBoxed.getInstance().getConfig().getInt("max-border")) + CustomBoxed.getInstance().getConfig().getInt("apart") + (int) (CustomBoxed.getInstance().getConfig().getInt("default-border") / 2) + 1));
            location.setZ(location.getZ()+((curY * CustomBoxed.getInstance().getConfig().getInt("max-border")) + CustomBoxed.getInstance().getConfig().getInt("apart") + (int) (CustomBoxed.getInstance().getConfig().getInt("default-border") / 2) + 1));
            if(curY == lineY) {
                CustomBoxed.getInstance().getConfig().set("x", true);
                CustomBoxed.getInstance().getConfig().set("cur-line-y", 0);
                CustomBoxed.getInstance().getConfig().set("lines-x", lineX + 1);
            } else {
                CustomBoxed.getInstance().getConfig().set("cur-line-y", curY+1);
            }
        }

        CustomBoxed.getInstance().saveConfig();
        return location;
    }

    public void serialize(Plot plot) {
        CustomFile file = CustomBoxed.getInstance().getFileManager().getMapsFile();
        file.getCustomConfig().set(plot.getOwner() + ".location", FileUtils.locationToString(plot.getLocation()));
        file.getCustomConfig().set(plot.getOwner() + ".blocks", plot.getBlocks());
        try {
            file.getCustomConfig().save(file.getCustomConfigFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void deserialize() {
        CustomFile file = CustomBoxed.getInstance().getFileManager().getMapsFile();
        for(String name : file.getCustomConfig().getKeys(false)) {
            Location center = FileUtils.stringToLocation(Objects.requireNonNull(file.getCustomConfig().getString(name + ".location")));
            int blocks = file.getCustomConfig().getInt(name + ".blocks");
            Plot plot = new Plot(UUID.fromString(name), center, blocks);
            plots.add(plot);
        }
    }
}
