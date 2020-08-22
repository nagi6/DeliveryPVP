package nagi.mcje.deliverypvp;

import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class EventListener implements Listener {

    public EventListener(){}

    private Main main = null;
    private GameManager manager = null;

    public EventListener(Main _main, GameManager _manager){
        main = _main;
        manager = _manager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){  //参加時
        Player player = event.getPlayer();
        main.eme.put(player.getName(), 0);
        main.dlv.put(player.getName(), 0);
        if(manager.isRedTeam(player.getName())){
            player.setDisplayName("§c"+player.getName());
            player.setPlayerListName("§c"+player.getName());
            manager.notr++;
        }
        if(manager.isBlueTeam(player.getName())){
            player.setDisplayName("§9"+player.getName());
            player.setPlayerListName("§9"+player.getName());
            manager.notb++;
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event){  //退出時
        Player player = event.getPlayer();
        if(manager.isRedTeam(player.getName())) manager.notr--;
        if(manager.isBlueTeam(player.getName())) manager.notb--;
        if(manager.notr == 0 && manager.game) manager.endGame(2, 2);
        if(manager.notb == 0 && manager.game) manager.endGame(1, 2);
    }

    @EventHandler
    public void onTouch(PlayerInteractEvent event){ //ブロックタッチ時
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        if(!manager.game){
            if(main.eme.get(player.getName()) == 1 && event.getAction() == Action.RIGHT_CLICK_BLOCK){
                manager.setRedEmeraldLocation(block.getLocation());
                player.sendMessage("赤チームのエメラルドブロックを設定しました");
                main.eme.put(player.getName(), 0);
                event.setCancelled(true);
            }else if(main.eme.get(player.getName()) == 2 && event.getAction() == Action.RIGHT_CLICK_BLOCK){
                manager.setBlueEmeraldLocation(block.getLocation());
                player.sendMessage("青チームのエメラルドブロックを設定しました");
                main.eme.put(player.getName(), 0);
                event.setCancelled(true);
            }
            if(main.dlv.get(player.getName()) == 1 && event.getAction() == Action.RIGHT_CLICK_BLOCK){
                manager.setRedDeliveryLocation(block.getLocation());
                player.sendMessage("赤チームの納品ブロックを設定しました");
                main.dlv.put(player.getName(), 0);
                event.setCancelled(true);
            }else if(main.dlv.get(player.getName()) == 2 && event.getAction() == Action.RIGHT_CLICK_BLOCK){
                manager.setBlueDeliveryLocation(block.getLocation());
                player.sendMessage("青チームの納品ブロックを設定しました");
                main.dlv.put(player.getName(), 0);
                event.setCancelled(true);
            }
            return;
        }
        if(manager.isRedEmeraldLocation(block) || manager.isBlueEmeraldLocation(block)
            || manager.isRedDeliveryLocation(block) || manager.isBlueDeliveryLocation(block)) event.setCancelled(true);
        if(manager.isRedTeam(player.getName()) && manager.isBlueEmeraldLocation(block)){
            if(manager.getRedEmeraldCarrier().equals("")){
                main.getServer().broadcastMessage("§l§9青チームのエメラルドが盗まれた！");
                manager.setRedEmeraldCarrier(player.getName());
            }else{
                player.sendMessage("§c味方がエメラルドを運んでいます");
            }
        }
        if(manager.isBlueTeam(player.getName()) && manager.isRedEmeraldLocation(block)){
            if(manager.getBlueEmeraldCarrier().equals("")){
                main.getServer().broadcastMessage("§l§c赤チームのエメラルドが盗まれた！");
                manager.setBlueEmeraldCarrier(player.getName());
            }else{
                player.sendMessage("§c味方がエメラルドを運んでいます");
            }
        }
        if(manager.isRedEmeraldCarrier(player.getName()) && manager.isRedDeliveryLocation(block)){
            manager.addRedPoint();
            main.getServer().broadcastMessage("§l§c赤チームが"+manager.getRedPoint()+"個目のエメラルドを持ち帰った！");
            manager.setRedEmeraldCarrier("");
            if(manager.getRedPoint() == manager.amount) manager.endGame(1, 1);
        }
        if(manager.isBlueEmeraldCarrier(player.getName()) && manager.isBlueDeliveryLocation(block)){
            manager.addBluePoint();
            main.getServer().broadcastMessage("§l§9青チームが"+manager.getBluePoint()+"個目のエメラルドを持ち帰った！");
            manager.setBlueEmeraldCarrier("");
            if(manager.getBluePoint() == manager.amount) manager.endGame(2, 1);
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event){
        Block block = event.getBlock();
        if(manager.isRedEmeraldLocation(block) || manager.isBlueEmeraldLocation(block)
                || manager.isRedDeliveryLocation(block) || manager.isBlueDeliveryLocation(block)) event.setCancelled(true);

    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event){  //ダメージを受けた時
        Entity damager = event.getDamager();
        Entity entity = event.getEntity();
        if(manager.isSameTeam((Player)damager, (Player)entity) && manager.game) event.setCancelled(true);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event){    //死亡時
        event.setKeepInventory(true);
        Player player = event.getEntity();
        if(!manager.game) return;
        if(manager.life != 0){
            int life = manager.getLifePoint(player.getName());
            life--;
            manager.setLifePoint(player.getName(), life);
            player.sendMessage("§l§aあなたのライフは残り"+manager.getLifePoint(player.getName())+"です");
            if(manager.getLifePoint(player.getName()) == 0){
                player.sendMessage("§l§aライフが無くなったため観戦モードになりました");
                player.setGameMode(GameMode.SPECTATOR);
                if(manager.isRedTeam(player.getName())) manager.notr--;
                if(manager.isBlueTeam(player.getName())) manager.notb--;
                if(manager.notr == 0) manager.endGame(2, 2);
                if(manager.notb == 0) manager.endGame(1, 2);
            }
        }
        if(manager.isRedEmeraldCarrier(player.getName())){
            manager.setRedEmeraldCarrier("");
            main.getServer().broadcastMessage("赤チームがエメラルドを紛失した");
        }
        if(manager.isBlueEmeraldCarrier(player.getName())){
            manager.setBlueEmeraldCarrier("");
            main.getServer().broadcastMessage("青チームがエメラルドを紛失した");
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event){    //リスポーン時
        if(!manager.game) return;
        Player player = event.getPlayer();
        player.getInventory().clear();
        if(manager.getLifePoint(player.getName()) >= 1) manager.setItem(player);
        if(manager.isRedTeam(player.getName()) && manager.game) player.teleport(manager.getRedSpawnLocation());
        if(manager.isBlueTeam(player.getName()) && manager.game) player.teleport(manager.getBlueSpawnLocation());
    }
}