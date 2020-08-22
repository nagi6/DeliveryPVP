package nagi.mcje.deliverypvp;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.ArrayList;
import java.util.HashMap;

public class GameManager {

    public GameManager(){}

    private Main main = null;
    private ArrayList<String> teamr = new ArrayList();  //赤チームプレイヤーリスト
    private ArrayList<String> teamb = new ArrayList();  //青チームプレイヤーリスト
    public int notr = 0;    //赤チーム人数
    public int notb = 0;    //青チーム人数
    private HashMap<String, Integer> kills = new HashMap(); //プレイヤー別キル数
    private HashMap<String, Integer> lifes = new HashMap(); //プレイヤー別ライフ値
    private Location spwr = null;   //赤リスポーン地点
    private Location spwb = null;   //青リスポーン地点
    private Location locr = null;   //赤エメラルド位置
    private Location locb = null;   //青エメラルド位置
    private Location dlvr = null;   //赤エメラルド納品場所
    private Location dlvb = null;   //青エメラルド納品場所
    private String hasEmeraldr = "";    //赤チームエメラルド所持者
    private String hasEmeraldb = "";    //青チームエメラルド所持者
    private int pointr = 0; //赤チーム納品数
    private int pointb = 0; //青チーム納品数
    public int amount = 5;  //勝利に必要なエメラルドの個数
    public int life = 5;    //ライフ値設定(0になったらリスポーン不可能、0にした場合無限)
    public int time = 0;    //試合経過時間
    public String times = "";   //時間メッセージ用
    public boolean game = false;    //ゲームが進行中か判定
    public GameMode gamemode = GameMode.SURVIVAL;   //試合中のゲームモード(サバイバルまたはアドベンチャー)

    public GameManager(Main _main){
        main = _main;
    }

/*チーム関連*/
    public void addRedTeam(String name){    //赤チームにプレイヤーを追加
        Player player = main.getServer().getPlayer(name);
        if(player instanceof Player) name = player.getName();
        if(isBlueTeam(name)) removeBlueTeam(name);
        teamr.add(name);
        notr++;
        player.setDisplayName("§c"+name);
        player.setPlayerListName("§c"+name);
        player.sendMessage("赤チームになりました");
    }

    public void addBlueTeam(String name){    //青チームにプレイヤーを追加
        Player player = main.getServer().getPlayer(name);
        if(player instanceof Player) name = player.getName();
        if(isRedTeam(name)) removeRedTeam(name);
        teamb.add(name);
        notb++;
        player.setDisplayName("§9"+name);
        player.setPlayerListName("§9"+name);
        player.sendMessage("青チームになりました");
    }

    public void removeRedTeam(String name) {     //赤チームからプレイヤーを削除
        Player player = main.getServer().getPlayer(name);
        if(player instanceof Player) name = player.getName();
        if(isRedTeam(name)){
            teamr.remove(name);
            notr--;
            player.setDisplayName("§f"+name);
            player.setPlayerListName("§f"+name);
            player.sendMessage("赤チームから除外されました");
        }
    }

    public void removeBlueTeam(String name){    //青チームからプレイヤーを削除
        Player player = main.getServer().getPlayer(name);
        if(player instanceof Player) name = player.getName();
        if(isBlueTeam(name)){
            teamb.remove(name);
            notb--;
            player.setDisplayName("§f"+name);
            player.setPlayerListName("§f"+name);
            player.sendMessage("青チームから除外されました");
        }
    }

    public boolean isRedTeam(String name){      //プレイヤーが赤チームか確認
        if(teamr.contains(name)) return true;
        else return false;
    }

    public boolean isBlueTeam(String name){     //プレイヤーが青チームか確認
        if(teamb.contains(name)) return true;
        else return false;
    }

    public void resetTeam(){    //全チームリセット
        teamr = new ArrayList<>();
        teamb = new ArrayList<>();
        notr = 0;
        notb = 0;
        for(Player player : main.getServer().getOnlinePlayers()){
            player.setDisplayName("§f"+player.getName());
            player.setPlayerListName("§f"+player.getName());
            player.sendMessage("チームがリセットされました");
        }
    }

/*リスポーン地点関連*/
    public Location getRedSpawnLocation(){  //赤チームリスポーン地点の取得
        return spwr;
    }

    public Location getBlueSpawnLocation(){  //青チームリスポーン地点の取得
        return spwb;
    }

    public void setRedSpawnLocation(Location loc){  //赤チームリスポーン地点の設定
        spwr = loc;
    }

    public void setBlueSpawnLocation(Location loc){  //青チームリスポーン地点の設定
        spwb = loc;
    }

/*エメラルドブロック関連*/
    public Location getRedEmeraldLocation(){    //赤チームエメラルド位置の取得
        return locr;
    }

    public Location getBlueEmeraldLocation(){    //青チームエメラルド位置の取得
        return locb;
    }

    public void setRedEmeraldLocation(Location loc){    //赤チームエメラルド位置の設定
        locr = loc;
    }

    public void setBlueEmeraldLocation(Location loc){   //青チームエメラルド位置の設定
        locb = loc;
    }

    public boolean isRedEmeraldLocation(Block block){   //赤チームのエメラルドかを判定
        Location loc = block.getLocation();
        if(loc.getWorld().getName().equalsIgnoreCase(locr.getWorld().getName())
                && loc.getX() == locr.getX() && loc.getY() == locr.getY() && loc.getZ() == locr.getZ()) return true;
        else return false;
    }

    public boolean isBlueEmeraldLocation(Block block){   //青チームのエメラルドかを判定
        Location loc = block.getLocation();
        if(loc.getWorld().getName().equalsIgnoreCase(locb.getWorld().getName())
                && loc.getX() == locb.getX() && loc.getY() == locb.getY() && loc.getZ() == locb.getZ()) return true;
        else return false;
    }

/*納品ブロック関連*/
    public Location getRedDeliveryLocation(){   //赤チーム納品地点の取得
        return dlvr;
    }

    public Location getBlueDeliveryLocation(){   //青チーム納品地点の取得
        return dlvb;
    }

    public void setRedDeliveryLocation(Location loc){  //赤チーム納品地点の設定
        dlvr = loc;
    }

    public void setBlueDeliveryLocation(Location loc){  //青チーム納品地点の設定
        dlvb = loc;
    }

    public boolean isRedDeliveryLocation(Block block){    //赤チーム納品地点かを判定
        Location loc = block.getLocation();
        if(loc.getWorld().getName().equalsIgnoreCase(dlvr.getWorld().getName())
                && loc.getX() == dlvr.getX() && loc.getY() == dlvr.getY() && loc.getZ() == dlvr.getZ()) return true;
        else return false;
    }

    public boolean isBlueDeliveryLocation(Block block){    //青チーム納品地点かを判定
        Location loc = block.getLocation();
        if(loc.getWorld().getName().equalsIgnoreCase(dlvb.getWorld().getName())
                && loc.getX() == dlvb.getX() && loc.getY() == dlvb.getY() && loc.getZ() == dlvb.getZ()) return true;
        else return false;
    }

    public void setAmount(int number){  //エメラルド個数設定
        amount = number;
    }

    public void setGamemode(String mode){   //ゲームモード設定
        if(mode.equals("survival")) gamemode = GameMode.SURVIVAL;
        else if(mode.equals("adventure")) gamemode = GameMode.ADVENTURE;
    }

    public void startGame(){    //ゲーム開始
        game = true;
        main.manager.times = " 経過時間:0分0秒)";
        new UpdateActionBarTask().runTaskAsynchronously(main);
        new CountTimeTask().runTaskLater(main, 20);
        for(Player player : main.getServer().getOnlinePlayers()){
            String name = player.getName();
            main.eme.put(player.getName(), 0);
            main.dlv.put(player.getName(), 0);
            if(isRedTeam(name) || isBlueTeam(name)){
                if (life == 0) lifes.put(name, 1);
                else lifes.put(name, life);
                kills.put(name, 0);
                player.setGameMode(gamemode);
                player.getInventory().clear();
                setItem(player);
                player.sendTitle("§f試合開始", "", 0, 100, 20);
                if(isRedTeam(name)) player.teleport(getRedSpawnLocation());
                else if(isBlueTeam(name)) player.teleport(getBlueSpawnLocation());
            }else{
                lifes.put(name, 0);
                player.setGameMode(GameMode.SPECTATOR);
                player.getInventory().clear();
                player.sendTitle("§f試合開始", "(観戦モード)", 0, 100, 20);
                player.teleport(getRedSpawnLocation());
            }
        }
    }

    public void endGame(int winteam, int reason){      //ゲーム終了
        String msg = "";
        String smsg;
        if(winteam == 1) msg = "§c赤チームの勝利";
        else if(winteam == 2) msg = "§9青チームの勝利";
        else if(winteam == 3) msg = "§f強制終了";
        if(reason == 1) smsg = "§f全てのエメラルドを持ち帰った";
        else if(reason == 2) smsg = "§fチームが全滅した";
        else smsg = "";
        notr = 0;
        notb = 0;
        kills = new HashMap<String, Integer>();
        lifes = new HashMap<String, Integer>();
        hasEmeraldr = "";
        hasEmeraldb = "";
        pointr = 0;
        pointb = 0;
        time = 0;
        game = false;
        for(Player player : main.getServer().getOnlinePlayers()){
            player.sendTitle(msg, smsg, 0, 100, 20);
            if(isRedTeam(player.getName())) notr++;
            if(isBlueTeam(player.getName())) notb++;
        }
        times = "";
        new UpdateActionBarTask().runTaskAsynchronously(main);
    }

    public void setItem(Player player){
        ItemStack sword = new ItemStack(Material.STONE_SWORD, 1);
        ItemStack bow = new ItemStack(Material.BOW, 1);
        ItemStack beef = new ItemStack(Material.COOKED_BEEF, 64);
        ItemStack arrow = new ItemStack(Material.ARROW, 64);
        ItemStack chestplater = new ItemStack(Material.LEATHER_CHESTPLATE);
        LeatherArmorMeta red = (LeatherArmorMeta)chestplater.getItemMeta();
        red.setColor(Color.fromRGB(255, 0, 0));
        chestplater.setItemMeta(red);
        ItemStack chestplateb = new ItemStack(Material.LEATHER_CHESTPLATE);
        LeatherArmorMeta blue = (LeatherArmorMeta)chestplateb.getItemMeta();
        blue.setColor(Color.fromRGB(0, 0, 255));
        chestplateb.setItemMeta(blue);
        player.getInventory().addItem(sword);
        player.getInventory().addItem(bow);
        player.getInventory().addItem(beef);
        player.getInventory().addItem(arrow);
        if(isRedTeam(player.getName())) player.getInventory().setChestplate(chestplater);
        else if(isBlueTeam(player.getName())) player.getInventory().setChestplate(chestplateb);
    }

    public boolean isSameTeam(Player player1, Player player2){  //指定した2人のプレイヤーが同じチームか判定
        if(teamr.contains(player1.getName()) && teamr.contains(player2.getName())) return true;
        else if(teamb.contains(player1.getName()) && teamb.contains(player2.getName())) return true;
        else return false;
    }

    public int getLifePoint(String name){   //プレイヤーの残りライフ数を取得
        return lifes.get(name);
    }

    public void setLifePoint(String name, int number){  //プレイヤーの残りライフ数を設定
        if(number >= 0) lifes.put(name, number);
    }

    public void setMaxLifePoint(int number){    //プレイヤーの初期のライフ数を取得
        if(number >= 0) life = number;
    }

    public void setRedEmeraldCarrier(String name){  //赤エメラルド所持者の設定
        hasEmeraldr = name;
    }

    public void setBlueEmeraldCarrier(String name){ //青エメラルド所持者の設定
        hasEmeraldb = name;
    }

    public String getRedEmeraldCarrier(){   //赤エメラルド所持者の取得
        return hasEmeraldr;
    }

    public String getBlueEmeraldCarrier(){  //青エメラルド所持者の取得
        return hasEmeraldb;
    }

    public boolean isRedEmeraldCarrier(String name){    //赤エメラルド所持者かを判定
        if(getRedEmeraldCarrier().equals(name)) return true;
        else return false;
    }

    public boolean isBlueEmeraldCarrier(String name){   //青エメラルド所持者かを判定
        if(getBlueEmeraldCarrier().equals(name)) return true;
        else return false;
    }

    public int getRedPoint(){   //赤チームの納品数を取得
        return pointr;
    }

    public int getBluePoint(){  //青チームの納品数を取得
        return pointb;
    }

    public void addRedPoint(){  //赤チームの納品数を追加
        pointr++;
    }

    public void addBluePoint(){ //青チームの納品数を追加
        pointb++;
    }
}
