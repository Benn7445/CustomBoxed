package me.quartz.customboxed.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class FileUtils {

    public static String locationToString(Location location) {
        return location != null ? (location.getWorld().getName() + "," +
                location.getX() + "," +
                location.getY() + "," +
                location.getZ() + "," +
                location.getYaw() + "," +
                location.getPitch()) : "";
    }

    public static Location stringToLocation(String string) {
        String[] s = string.split(",");
        return !string.isEmpty() ?
                new Location(Bukkit.getWorld(s[0]),
                        Float.parseFloat(s[1]),
                        Float.parseFloat(s[2]),
                        Float.parseFloat(s[3]),
                        Float.parseFloat(s[4]),
                        Float.parseFloat(s[5])
                ) : null;
    }
}
