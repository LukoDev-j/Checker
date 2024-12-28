package org.checker.checker;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashSet;
import java.util.Set;

public final class Checker extends JavaPlugin {
    private Set<Player> playersUnderCheck = new HashSet<>();

    @Override
    public void onEnable() {
        getCommand("contact").setExecutor( new Contactcommand());
        getCommand("check").setExecutor(new Checkcommand());
        getCommand("uncheck").setExecutor(new Uncheckcommand());

    }

    @Override
    public void onDisable() {

    }
    private boolean isUnderCheck(Player targetplayer) {

        return playersUnderCheck.contains(targetplayer);
    }
    public class PlayerCheckListener implements Listener {


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
    public static class Contactcommand implements CommandExecutor {
        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
            Player player = (Player) sender;
            if(args.length < 1){
                player.sendMessage("Использование /contact <сообщение>");
            }else {
                String playernick = player.getName();
                Player opplayer = Adminplayer();
                StringBuilder message = new StringBuilder();
                for (int i = 0; i < args.length; i++) {
                    message.append(args[i]).append(" ");
                }
                if(opplayer !=null && opplayer.isOnline()){
                    opplayer.sendMessage(ChatColor.RED + message.toString().trim());
                }else {
                    player.sendMessage("Администрации нет на сервере.");
                }
            }            return true;
        }

    }
    // AdminPlayer - функция нахождения админа на сервере, у меня в плагине он определяется как чел с опкой
    // но скоро допилю luckperms ну или же сделайте сами для своего сервера
    private static Player Adminplayer(){
        for (Player opplayer : Bukkit.getOnlinePlayers()){
            if (opplayer.isOp()){
                return opplayer;
            }
            else {
                return null;
            }
        }
        return null;
    }
    public class Checkcommand implements CommandExecutor {

        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            Player player = (Player) sender;

            if (args.length < 1) {
                player.sendMessage("Использование: /check <ник>");
                return false;
            }

            String targetPlayerName = args[0];
            Player targetPlayer = Bukkit.getPlayer(targetPlayerName);
            if (!isUnderCheck(targetPlayer)) {
                if (targetPlayer != null && targetPlayer.isOnline()) {
                    getLogger().info(ChatColor.RED + "Игрок: " + targetPlayerName + " попал на проверку");
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
            }else {
                player.sendMessage(ChatColor.RED + "игрок уже под проверкой!");
            }

            return false;
        }
    }

    public class Uncheckcommand implements CommandExecutor {

        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            Player player = (Player) sender;

            if (args.length < 1) {
                player.sendMessage("Использование: /uncheck <ник>");
                return false;
            }

            String targetPlayerName = args[0];
            Player targetPlayer = Bukkit.getPlayer(targetPlayerName);
            if (isUnderCheck(targetPlayer)) {
                if (targetPlayer != null && targetPlayer.isOnline()) {
                    getLogger().info(ChatColor.GREEN + "Игрок: " + targetPlayerName + "снят с проверки");
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
            else {
                player.sendMessage(ChatColor.RED + "игрок не под проверкой");
            }
            return false;
        }
    }
}
