package io.github.meeples10.randomtp;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import io.github.meeples10.meepcore.Messages;
import io.github.meeples10.meepcore.RandomUtils;

public class CommandRandomTeleport implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender.hasPermission("randomtp.use")) {
            if(sender instanceof Player) {
                Player p = (Player) sender;
                if(!Main.isEnabled(p.getLocation().getWorld().getName())) {
                    sender.sendMessage(Messages.formatError("/" + label + " cannot be used in this world."));
                    return true;
                }
                int distance = Main.getMaximumDistance();
                if(args.length > 0) {
                    if(args[0].equalsIgnoreCase("reload")) {
                        if(sender.hasPermission("randomtp.reload")) {
                            reload(sender);
                        } else {
                            sender.sendMessage(Messages.noPermissionMessage());
                        }
                        return true;
                    } else if(args[0].equalsIgnoreCase("debug")) {
                        if(sender.hasPermission("randomtp.debug")) {
                            for(UUID u : Main.getLastUses().keySet()) {
                                long use = System.currentTimeMillis() - Main.getLastUse(u);
                                sender.sendMessage(Messages.format("%s%s$t: %s",
                                        use >= Main.getCooldown() ? "$hl" : "$e", u, use + "ms ago"));
                            }
                            sender.sendMessage(
                                    Messages.format("$hl[%s]$t Cooldown: $w%s$tms", Main.NAME, Main.getCooldown()));
                        } else {
                            sender.sendMessage(Messages.noPermissionMessage());
                        }
                        return true;
                    }
                    try {
                        distance = Integer.parseInt(args[0]);
                    } catch(NumberFormatException e) {
                        sender.sendMessage(Messages.invalidArgument(args[0]));
                        return true;
                    }
                }
                long cooldown = System.currentTimeMillis() - Main.getLastUse(p.getUniqueId());
                if(cooldown >= Main.getCooldown()) {
                    int x = RandomUtils.randInt(-distance, distance);
                    int z = RandomUtils.randInt(-distance, distance);
                    Location l = new Location(p.getLocation().getWorld(), x,
                            p.getLocation().getWorld().getHighestBlockYAt(x, z), z);
                    p.teleport(l, TeleportCause.PLUGIN);
                    Main.setLastUse(p.getUniqueId());
                } else {
                    sender.sendMessage(Messages.format(
                            "$hl[" + Main.NAME + "]$t You must wait $e%s$t seconds before using this command again.",
                            ((Main.getCooldown() - cooldown) / 1000)));
                }
            } else if(args.length > 0 && args[0].equalsIgnoreCase("reload")) {
                reload(sender);
            } else {
                sender.sendMessage(Messages.getPlayersOnlyMessage());
            }
        } else {
            sender.sendMessage(Messages.noPermissionMessage());
        }
        return true;
    }

    private static void reload(CommandSender sender) {
        sender.sendMessage(Messages.reloadAttempt(Main.NAME));
        sender.sendMessage(Messages.reloadMessage(Main.NAME, Main.loadConfig()));
    }
}