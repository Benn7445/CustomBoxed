package me.quartz.customboxed.plot;

import me.quartz.customboxed.CustomBoxed;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Plot {

    private final UUID owner;
    private final Location location;
    private int blocks;

    public Plot(UUID owner, Location location) {
        this.owner = owner;
        this.location = location;
        this.blocks = CustomBoxed.getInstance().getConfig().getInt("default-border");
    }

    public Plot(UUID owner, Location location, int blocks) {
        this.owner = owner;
        this.location = location;
        this.blocks = blocks;
    }

    public UUID getOwner() {
        return owner;
    }

    public Location getLocation() {
        return location;
    }

    public int getBlocks() {
        return blocks;
    }

    public boolean upgradePlot(int blocks) {
        Player player = Bukkit.getPlayer(owner);
        if(player != null && this.blocks + blocks <= CustomBoxed.getInstance().getConfig().getInt("max-border")) {
            this.blocks += blocks;
            WorldBorder border = Bukkit.createWorldBorder();
            border.setCenter(player.getLocation());
            border.setSize(getBlocks());

            player.setWorldBorder(border);
            CustomBoxed.getInstance().getPlotManager().serialize(this);
            return true;
        }
        return false;
    }

    public boolean reducePlot(int blocks) {
        Player player = Bukkit.getPlayer(owner);
        if(player != null && this.blocks - blocks <= CustomBoxed.getInstance().getConfig().getInt("default-border")) {
            this.blocks -= blocks;
            WorldBorder border = Bukkit.createWorldBorder();
            border.setCenter(player.getLocation());
            border.setSize(getBlocks());

            player.setWorldBorder(border);
            CustomBoxed.getInstance().getPlotManager().serialize(this);
            return true;
        }
        return false;
    }
}
