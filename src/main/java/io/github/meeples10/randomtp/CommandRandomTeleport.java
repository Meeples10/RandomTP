package io.github.meeples10.randomtp;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import io.github.meeples10.meepcore.MCommand;
import io.github.meeples10.meepcore.Messages;
import io.github.meeples10.meepcore.RandomUtils;

public class CommandRandomTeleport extends MCommand {

    public CommandRandomTeleport(String usageKey) {
        super(usageKey);
    }

    public boolean run(CommandSender sender, Command command, String label, String[] args, String locale) {
        if(sender.hasPermission("randomtp.use")) {
            if(sender instanceof Player) {
                Player p = (Player) sender;
                if(!Main.worlds.contains(p.getLocation().getWorld().getName())) {
                    sender.sendMessage(Messages.formatError(locale, String
                            .format(Messages.translate(locale, "command.randomtp.rtp.blacklisted"), "/" + label)));
                    return true;
                }
                int distance = Main.maximumDistance;
                if(args.length > 0) {
                    if(args[0].equalsIgnoreCase("reload")) {
                        if(sender.hasPermission("randomtp.reload")) {
                            sender.sendMessage(Messages.reloadAttempt(locale, Main.NAME));
                            sender.sendMessage(Messages.reloadMessage(locale, Main.NAME, Main.loadConfig()));
                        } else {
                            sender.sendMessage(Messages.noPermissionMessage(locale));
                        }
                        return true;
                    } else if(args[0].equalsIgnoreCase("debug")) {
                        if(sender.hasPermission("randomtp.debug")) {
                            for(UUID u : Main.LAST_USES.keySet()) {
                                long use = System.currentTimeMillis() - Main.LAST_USES.getOrDefault(u, Main.cooldown);
                                sender.sendMessage(Messages.format("%s%s$t: %s", use >= Main.cooldown ? "$hl" : "$e",
                                        u.toString(),
                                        use + "ms " + Messages.translate(locale, "command.randomtp.debug.ago")));
                            }
                            sender.sendMessage(Messages.format("$hl[%s]$t "
                                    + Messages.translate(locale, "command.randomtp.debug.cooldown") + ": $w%s$tms",
                                    Main.NAME, Main.cooldown));
                            sender.sendMessage(Messages.format(
                                    "$hl[%s]$t " + Messages.translate(locale, "command.randomtp.debug.max")
                                            + ": $w%s$t " + Messages.translate(locale, "command.randomtp.debug.blocks"),
                                    Main.NAME, Main.maximumDistance));
                        } else {
                            sender.sendMessage(Messages.noPermissionMessage(locale));
                        }
                        return true;
                    }
                    try {
                        distance = Integer.parseInt(args[0]);
                    } catch(NumberFormatException e) {
                        sender.sendMessage(Messages.invalidArgument(locale, args[0]));
                        return true;
                    }
                }
                long cooldown = System.currentTimeMillis()
                        - Main.LAST_USES.getOrDefault(p.getUniqueId(), Main.cooldown);
                if(cooldown >= Main.cooldown) {
                    int x = RandomUtils.randInt(-distance, distance);
                    int z = RandomUtils.randInt(-distance, distance);
                    Location l = new Location(p.getLocation().getWorld(), x, 256, z);
                    p.teleport(l, TeleportCause.PLUGIN);
                    Main.LAST_USES.put(p.getUniqueId(), System.currentTimeMillis());
                } else {
                    sender.sendMessage(Messages.format(
                            "$hl[" + Main.NAME + "]$t " + Messages.translate(locale, "command.randomtp.rtp.cooldown"),
                            ((Main.cooldown - cooldown) / 1000)));
                }
            } else if(args.length > 0 && args[0].equalsIgnoreCase("reload")) {
                sender.sendMessage(Messages.reloadAttempt(locale, Main.NAME));
                sender.sendMessage(Messages.reloadMessage(locale, Main.NAME, Main.loadConfig()));
            } else {
                sender.sendMessage(Messages.getPlayersOnlyMessage(locale));
            }
        } else {
            sender.sendMessage(Messages.noPermissionMessage(locale));
        }
        return true;
    }
}
