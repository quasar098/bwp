package templatepackage.events;

import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;

@SuppressWarnings("unused")
public class playerjoin implements Listener {
    @SuppressWarnings("FieldMayBeFinal")
    private static ArrayList<Location> myList = new ArrayList<>();
    private static boolean tgttos = false;
    private static Location orgLoc = null;

    @EventHandler
    public static void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        Location loc2 = player.getWorld().getSpawnLocation();
        loc2.setX(loc2.getX() + 0.5);
        loc2.setZ(loc2.getZ() + 0.5);

        player.sendMessage("welcome to the server! plugin by QUASAR098");
        player.sendMessage("Creative is for building the course");
        player.sendMessage("Survival is for playing it");

        player.teleport(loc2);
    }

    @EventHandler
    public static void onWalk(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        World world = player.getWorld();
        Location loc2 = world.getSpawnLocation();
        loc2.setX(loc2.getX() + 0.5);
        loc2.setZ(loc2.getZ() + 0.5);

        Location loc = player.getLocation();
        loc.setY(loc.getY() - 1);

        player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 20, 1, false, false));
        player.setExhaustion(20);
        // gold
        if (world.getBlockAt(loc).getType() == Material.GOLD_BLOCK && player.getGameMode() != GameMode.CREATIVE) {
            if (!tgttos) {
                player.sendMessage("You completed the course! Good job");
                player.teleport(orgLoc);
                player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 1);
            } else {
                for (Player playerz : player.getWorld().getPlayers()) {
                    playerz.sendMessage(player.getDisplayName() + " has beaten the course!");
                    playerz.teleport(orgLoc);
                    playerz.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 1);
                }
            }
            player.getWorld().setSpawnLocation(orgLoc.getBlockX(), orgLoc.getBlockY(), orgLoc.getBlockZ());
            for (Location location : myList) {
                world.getBlockAt(location).setType(Material.AIR);
            }
            myList.clear();
        }
        // coal
        else if (world.getBlockAt(loc).getType() == Material.COAL_BLOCK && player.getGameMode() != GameMode.CREATIVE) {
            player.teleport(loc2);
            player.sendMessage(ChatColor.RED + "Stop cheating!");
            if (!tgttos) {
                for (Location location : myList) {
                    player.getWorld().getBlockAt(location).setType(Material.AIR);
                }
                myList.clear();
            }
        } else if (player.getLocation().getY() < 0) {
            player.teleport(loc2);
            if (player.getGameMode() != GameMode.CREATIVE) {
                player.getInventory().clear();
                player.setItemInHand(new ItemStack(Material.WOOL, 64));
                if (!tgttos) {
                    for (Location location : myList) {
                        player.getWorld().getBlockAt(location).setType(Material.AIR);
                    }
                    myList.clear();
                }
            }
        } else if (world.getBlockAt(loc).getType() == Material.DIAMOND_BLOCK && player.getGameMode() != GameMode.CREATIVE) {
            Location roundLoc = player.getLocation();
            roundLoc.setX((int) roundLoc.getX() - 1);
            roundLoc.setY((int) roundLoc.getY());
            roundLoc.setZ((int) roundLoc.getZ() - 1);
            if (!compareLocations(world.getSpawnLocation(), roundLoc)) {
                world.setSpawnLocation((int) player.getLocation().getX() - 1, (int) player.getLocation().getY(), (int) player.getLocation().getZ() - 1);
                player.playSound(player.getLocation(), Sound.NOTE_PLING, 1, 1);
                player.sendMessage(ChatColor.GREEN + "Checkpoint!");
            }
        }
    }

    public static boolean compareLocations(Location loc1, Location loc2) {
        if (loc1.getX() == loc2.getX()) {
            if (loc1.getY() == loc2.getY()) {
                return loc1.getZ() == loc2.getZ();
            }
        }
        return false;
    }

    @EventHandler
    public static void onBreakBlock(BlockBreakEvent event) {
        Player player = event.getPlayer();

        if (player.getGameMode() != GameMode.CREATIVE) {
            if (event.getBlock().getType() != Material.WOOL) {
                event.setCancelled(true);
            } else {
                myList.remove(event.getBlock().getLocation());
                event.setCancelled(true);
                event.getBlock().setType(Material.AIR);
            }
        }
    }

    @EventHandler
    public static void onDamage(EntityDamageEvent event) {
        if (event.getEntity().getType() == EntityType.PLAYER) {
            if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public static void onPlaceBlock(BlockPlaceEvent event) {
        Player player = event.getPlayer();

        if (player.getGameMode() != GameMode.CREATIVE) {
            if (event.getBlock().getType() == Material.WOOL) {
                player.setItemInHand(new ItemStack(Material.WOOL, 64));
                myList.add(event.getBlock().getLocation());
            }
        }
    }

    @EventHandler
    public static void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();

        if (player.getGameMode() != GameMode.CREATIVE) {
            player.getInventory().clear();
            player.setItemInHand(new ItemStack(Material.WOOL, 64));
            if (!tgttos) {
                for (Location location : myList) {
                    player.getWorld().getBlockAt(location).setType(Material.AIR);
                }
                myList.clear();
            }
        }
    }

    @EventHandler
    public static void changeGameMode(PlayerGameModeChangeEvent event) {
        if (event.getNewGameMode() == GameMode.CREATIVE) {
            for (Location location : myList) {
                event.getPlayer().getWorld().getBlockAt(location).setType(Material.AIR);
            }
            myList.clear();

            event.getPlayer().getInventory().clear();
            event.getPlayer().getInventory().addItem(new ItemStack(Material.GOLD_BLOCK, 1));
            event.getPlayer().getInventory().addItem(new ItemStack(Material.COAL_BLOCK, 1));
            event.getPlayer().getInventory().addItem(new ItemStack(Material.DIAMOND_BLOCK, 1));
            event.getPlayer().sendMessage(ChatColor.BLUE + "-----------------------------------------------------");
            event.getPlayer().sendMessage(ChatColor.GREEN + "You're in creative mode. Build your dream obstacle course!");
            event.getPlayer().sendMessage(ChatColor.YELLOW + "Gold Blocks are the finish line. Step on them to win");
            event.getPlayer().sendMessage(ChatColor.GRAY + "Coal Blocks can be put in places where you don't want the player to go. " +
                    "If they step on it, they will get teleported back to their last checkpoint (or the start)");
            event.getPlayer().sendMessage(ChatColor.AQUA + "Diamond block are checkpoints and should be used sparingly");
            event.getPlayer().sendMessage(ChatColor.WHITE + "Any other block can be used to build the course");
            event.getPlayer().sendMessage(ChatColor.BLUE + "-----------------------------------------------------");
        } else if (event.getNewGameMode() == GameMode.SURVIVAL) {
            event.getPlayer().sendMessage(ChatColor.BLUE + "Good luck!");
            event.getPlayer().getInventory().clear();
            event.getPlayer().getInventory().addItem(new ItemStack(Material.WOOL, 64));
        }
    }
    @EventHandler
    public static void serverStop(PluginDisableEvent event) {
        for (Location location : myList) {
            event.getPlugin().getServer().getWorld("world").getBlockAt(location).setType(Material.AIR);
        }
        myList.clear();
        World world = event.getPlugin().getServer().getWorld("world");
        world.setSpawnLocation((int) orgLoc.getX(), (int) orgLoc.getY(), (int) orgLoc.getZ());
    }
    @EventHandler
    public static void serverStart(PluginEnableEvent event) {
        World world = event.getPlugin().getServer().getWorld("world");
        orgLoc = world.getSpawnLocation();
    }
    @EventHandler
    public static void onWea(WeatherChangeEvent event) {
        if (event.toWeatherState()) {
            event.setCancelled(true);
        }
    }
    public static boolean checkTGTTOS() {
        return tgttos;
    }
    public static void setTGTTOS(Boolean tgttosvalue) {
        tgttos = tgttosvalue;
    }
}
