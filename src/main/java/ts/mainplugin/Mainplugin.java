package ts.mainplugin;

import org.bukkit.*;
import org.bukkit.Bukkit;
import org.bukkit.BanList;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.ChatColor;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.checkerframework.checker.units.qual.C;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public final class Mainplugin extends JavaPlugin {

    private Set<Player> playersUnderCheck = new HashSet<>();

    @Override
    public void onEnable() {
        getLogger().info("Плагин Включен!");
        getServer().getPluginManager().registerEvents(new PlayerCheckListener(), this);
        getLogger().info("Проверка доступна!");
        getCommand("almaz").setExecutor(new AlmazCommand());
        getCommand("help").setExecutor(new HelpCommand());
        getCommand("bm").setExecutor(new Bmcommand());
        getCommand("st").setExecutor(new Stcommand());
        getCommand("pv").setExecutor(new Checkcommand());
        getCommand("offpv").setExecutor(new Uncheckcommand());
        getCommand("pvban").setExecutor(new CheckBanCommand());
        getCommand("msg").setExecutor(new Msgcommand());
        getCommand("contact").setExecutor(new Contactcommand());
        getCommand("br").setExecutor(new broadcastcommand());
        getCommand("meteorit").setExecutor(new MeteoritCommand());
    }

    @Override
    public void onDisable() {
        getLogger().info("Плагин Выключен!");
    }

    private boolean isUnderCheck(Player targetplayer) {
        return playersUnderCheck.contains(targetplayer);
    }


    public class PlayerCheckListener implements Listener {
        @EventHandler
        public void onPlayerLogin(PlayerJoinEvent event) {
            String playername = event.getPlayer().getName();
            Player eventplayer = Bukkit.getPlayer(playername);
            if (!eventplayer.isOp()) {
                eventplayer.sendMessage("Вы играете на сервере VMShield");
                event.setJoinMessage(playername + " присоединился");
            } else {
                getLogger().info("Разработчик " + eventplayer + " подключился на сервер");
                event.setJoinMessage(null);
                eventplayer.sendTitle(ChatColor.RED + "Войдите в систему", "авторизации", 10, 50, 10);
                eventplayer.sendMessage("Привет разработчик!");
            }


        }
        @EventHandler
        public void onPlayerQuit(PlayerQuitEvent event) {
            String playername = event.getPlayer().getName();
            Player eventplayer = Bukkit.getPlayer(playername);
            if (!eventplayer.isOp()) {
                event.setQuitMessage(playername + " покинул сервер");
            } else {
                getLogger().info("Разработчик " + eventplayer + " sdпокинул сервер");
                event.setQuitMessage(null);
            }
        }


        @EventHandler
        public void OnPlayerMove(PlayerMoveEvent event){
            Player targetplayer = event.getPlayer();
            if (isUnderCheck(targetplayer)){
                event.setCancelled(true);
            }
        }

        @EventHandler
        public void onPlayerDropItem(PlayerDropItemEvent event) {
            Player targetplayer = event.getPlayer();
            if (isUnderCheck(targetplayer)) {
                event.setCancelled(true);
            }
        }

        @EventHandler
        public void onPlayerInteract(PlayerInteractEvent event) {
            Player targetplayer = event.getPlayer();
            if (isUnderCheck(targetplayer)) {
                event.setCancelled(true);
            }
        }
    }

    public static class HelpCommand implements CommandExecutor {

        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                player.sendMessage("Новый плагин от coffee сделан на Java Maven! бета тест");
            } else {
                sender.sendMessage("Эта команда доступна только для игроков.");
            }
            return true;
        }
    }
    public static class MeteoritCommand implements CommandExecutor {

        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            if (args.length > 0) {
                String message = String.join(" ", args);
                for (Player p : Bukkit.getOnlinePlayers()) {
                    for (int i = 0; i < 4; i++) {
                        p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                        p.sendTitle(ChatColor.DARK_PURPLE + "МЕТЕОРИТ!", message, 10, 10, 10);
                    }
                    p.sendMessage(ChatColor.BOLD + "============================");
                    p.sendMessage(ChatColor.YELLOW + "Метеорит!");
                    p.sendMessage(ChatColor.RED + message);
                    p.sendMessage(ChatColor.BOLD + "============================");

                }
            } else {
                sender.sendMessage("Пожалуйста, введите координаты.");
            }

            return true;
        }
    }

    public static class AlmazCommand implements CommandExecutor {

        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                ItemStack diamond = new ItemStack(Material.DIAMOND, 1);
                player.getInventory().addItem(diamond);
                player.sendMessage(ChatColor.GREEN + "Вы получили алмаз!");
            } else {
                sender.sendMessage("Эта команда доступна только для игроков.");
            }
            return true;
        }
    }

    public static class Bmcommand implements CommandExecutor {

        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            if (args.length > 0) {
                String message = String.join(" ", args);
                Bukkit.broadcastMessage(message);
            } else {
                sender.sendMessage("Пожалуйста, введите сообщение для отправки.");
            }
            return true;
        }
    }
    public static class broadcastcommand implements CommandExecutor {

        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            if (args.length > 0) {
                String message = String.join(" ", args);
                message = ChatColor.translateAlternateColorCodes('&', message);
                for(Player player : Bukkit.getOnlinePlayers()){
                    player.sendTitle(message, "", 10, 70 ,10);
                }
            } else {
                sender.sendMessage("Пожалуйста, введите сообщение для отправки.");
            }
            return true;
        }
    }
    public static class Contactcommand implements CommandExecutor{
        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
            Player player = (Player) sender;
            if(args.length < 1){
                player.sendMessage("Использование /contact <сообщение>");
            }else {
                String playernick = player.getName();
                String targetplayernick = "_WurthyColdy_";
                Player targetplayer = Bukkit.getPlayer(targetplayernick);
                StringBuilder message = new StringBuilder();
                for (int i = 0; i < args.length; i++) {
                    message.append(args[i]).append(" ");
                }
                if(targetplayer !=null && targetplayer.isOnline()){
                    targetplayer.sendMessage(ChatColor.RED + message.toString().trim());
                }else {
                    player.sendMessage("Администратор: " + targetplayernick + "не на сервере.");
                }
            }            return true;
        }

    }
    public static class Msgcommand implements CommandExecutor {
        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
            Player player = (Player) sender;
            if(args.length < 2 || args.length < 1){
                player.sendMessage("Использование /msg <имя> <сообщение>");
            }else {

                String playernick = player.getName();
                String targetplayernick = args[0];
                Player targetplayer = Bukkit.getPlayer(targetplayernick);
                StringBuilder message = new StringBuilder();
                for (int i = 1; i < args.length; i++) {
                    message.append(args[i]).append(" ");
                }
                if(targetplayer !=null && targetplayer.isOnline()){
                    targetplayer.sendMessage(ChatColor.YELLOW + message.toString().trim() + " Получено от: " + playernick);
                }else {
                    player.sendMessage("Игрок: " + targetplayernick + "не на сервере.");
                }
            }            return true;
        }

    }

    public static class Stcommand implements CommandExecutor {

        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            String message = "Сервер выключается!";
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendMessage(message);
            }
            try {
                Thread.sleep(6000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            Bukkit.getServer().shutdown();
            return true;
        }
    }
    public class CheckBanCommand implements CommandExecutor {

        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            if (sender instanceof Player) {
                Player player = (Player) sender;

                if (args.length < 2) {
                    player.sendMessage("Использование: /pvban <ник> <сообщение>");
                    return false;
                }

                String targetPlayerName = args[0];
                String banmessage = args[1];
                Player targetPlayer = Bukkit.getPlayer(targetPlayerName);

                if (targetPlayer != null && targetPlayer.isOnline()) {
                    playersUnderCheck.remove(targetPlayer);
                    long duration = 1 * 60 * 30 * 1000;
                    Bukkit.getBanList(BanList.Type.NAME).addBan(targetPlayerName, banmessage, new Date(System.currentTimeMillis() + duration), "Забанил: " + player);
                    targetPlayer.kickPlayer("Вы были забанены на 30 минут за: " + banmessage);
                } else {
                    player.sendMessage("Игрок " + targetPlayerName + " не найден или не в сети.");
                }
                return true;
            }
            return false;
        }
    }

    public class Checkcommand implements CommandExecutor {

        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
                Player player = (Player) sender;

                if (args.length < 1) {
                    player.sendMessage("Использование: /pv <ник>");
                    return false;
                }

                String targetPlayerName = args[0];
                Player targetPlayer = Bukkit.getPlayer(targetPlayerName);

                if (targetPlayer != null && targetPlayer.isOnline()) {
                    getLogger().info(ChatColor.RED + "Игрок: " + targetPlayerName + " попал на проверку" );
                    playersUnderCheck.add(targetPlayer);
                    PotionEffect slepota = new PotionEffect(PotionEffectType.BLINDNESS, 100000000, 1);
                    PotionEffect zamedlenie = new PotionEffect(PotionEffectType.SLOWNESS, 100000000, 1);
                    targetPlayer.addPotionEffect(slepota);
                    targetPlayer.addPotionEffect(zamedlenie);
                    targetPlayer.sendTitle(ChatColor.RED + "Проверка на читы", "Следуй инструкциям в чате", 15, 1000000000, 30);
                    player.sendMessage(ChatColor.GREEN + "Игрок " + targetPlayerName + " теперь под проверкой.");
                    for (int i = 0; i < 10; i++) {
                        targetPlayer.sendMessage(ChatColor.RED + "Заходи в звонок в группе и включай демонстрацию экрана.");
                        targetPlayer.sendMessage(ChatColor.YELLOW + "Или же используй /contact сообщение");
                    }
                } else {
                    player.sendMessage("Игрок " + targetPlayerName + " не найден или не в сети.");
                }
                return true;

        }
    }

    public class Uncheckcommand implements CommandExecutor {

        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
                Player player = (Player) sender;

                if (args.length < 1) {
                    player.sendMessage("Использование: /offpv <ник>");
                    return false;
                }

                String targetPlayerName = args[0];
                Player targetPlayer = Bukkit.getPlayer(targetPlayerName);

                if (targetPlayer != null && targetPlayer.isOnline()) {
                    getLogger().info(ChatColor.GREEN + "Игрок: " + targetPlayerName + "снят с проверки" );
                    playersUnderCheck.remove(targetPlayer);
                    targetPlayer.removePotionEffect(PotionEffectType.SLOWNESS);
                    targetPlayer.removePotionEffect(PotionEffectType.BLINDNESS);
                    targetPlayer.sendTitle(ChatColor.GREEN + "Проверка окончена!", "Удачной игры", 30, 100, 30);
                    player.sendMessage(ChatColor.BOLD + " " + ChatColor.GREEN + "Проверка отключена игроку " + targetPlayerName);
                } else {
                    player.sendMessage("Игрок " + targetPlayerName + " не найден или не в сети.");
                }
                return true;

        }
    }
}
