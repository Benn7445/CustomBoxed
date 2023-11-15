package me.quartz.customboxed.commands;

import me.quartz.customboxed.CustomBoxed;
import me.quartz.customboxed.plot.Plot;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public class IslandCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender instanceof Player) {
            Player player = (Player) commandSender;
            Plot plot = CustomBoxed.getInstance().getPlotManager().getPlot(player.getUniqueId());
            if(strings.length == 0) {
                if(plot == null) {
                    CustomBoxed.getInstance().getPlotManager().createPlot(player);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(CustomBoxed.getInstance().getConfig().getString("messages.created"))));
                } else {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(CustomBoxed.getInstance().getConfig().getString("messages.already-plot"))));
                }
            } else if (strings.length > 2 && strings[0].equalsIgnoreCase("upgrade")) {
                try {
                    if (strings[1].equalsIgnoreCase("add")) {
                        int multiplier = CustomBoxed.getInstance().getConfig().getInt("cost-multiplier");
                        if(CustomBoxed.getInstance().getEcon().has(player, multiplier * Integer.parseInt(strings[2]))) {
                            CustomBoxed.getInstance().getEcon().withdrawPlayer(player, multiplier * Integer.parseInt(strings[2]));
                            if (plot.upgradePlot(Integer.parseInt(strings[2]))) {
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(CustomBoxed.getInstance().getConfig().getString("messages.upgraded"))));
                            } else {
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(CustomBoxed.getInstance().getConfig().getString("messages.cant-upgrade"))));
                            }
                        } else {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(CustomBoxed.getInstance().getConfig().getString("messages.no-money"))));
                        }
                    } else if (strings[1].equalsIgnoreCase("remove")) {
                        if (plot.reducePlot(Integer.parseInt(strings[2]))) {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(CustomBoxed.getInstance().getConfig().getString("messages.reduced"))));
                        } else {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(CustomBoxed.getInstance().getConfig().getString("messages.cant-reduce"))));
                        }
                    } else {
                        player.sendMessage(ChatColor.RED + "Usage: /is upgrade <add/remove> blocks");
                    }
                } catch (NumberFormatException nfe) {
                    player.sendMessage(ChatColor.RED + "Usage: /is upgrade <add/remove> blocks");
                }
            } else {
                player.sendMessage(ChatColor.RED + "Usage: /is upgrade <add/remove> blocks");
            }
        }
        return true;
    }
}
