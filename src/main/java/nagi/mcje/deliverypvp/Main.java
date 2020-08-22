package nagi.mcje.deliverypvp;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public final class Main extends JavaPlugin {

    public GameManager manager;
    public HashMap<String, Integer> eme;
    public HashMap<String, Integer> dlv;

    private static Main main;

    @Override
    public void onLoad(){
        main = this;
        manager = new GameManager(this);
        eme = new HashMap<String, Integer>();
        dlv = new HashMap<String, Integer>();
    }

    @Override
    public void onEnable(){
        getCommand("dpvp").setExecutor(this);
        getServer().getPluginManager().registerEvents(new EventListener(this, manager), this);
        new UpdateActionBarTask().runTaskAsynchronously(this);
    }

    public static Main getInstance(){
        return main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        Player player;
        String name;
        int num;
        if(!(sender instanceof Player)){
            sender.sendMessage("サーバー内でコマンドを実行して下さい");
            return true;
        }

        if(cmd.getName().equalsIgnoreCase("dpvp")){
            if(args.length == 0){
                sendCommandHelp((Player) sender);
                return true;
            }
        }

        switch(args[0]){
            case "start":
                if(manager.game){
                    sender.sendMessage("§c試合中です");
                    return true;
                }
                if(manager.getRedSpawnLocation() == null || manager.getBlueSpawnLocation() == null){
                    sender.sendMessage("§cチームのリスポーン地点が設定されていません");
                    return true;
                }else if(manager.getRedEmeraldLocation() == null || manager.getBlueEmeraldLocation() == null){
                    sender.sendMessage("§cチームのエメラルドブロックが設定されていません");
                    return true;
                }else if(manager.getRedDeliveryLocation() == null || manager.getBlueDeliveryLocation() == null){
                    sender.sendMessage("§cチームの納品地点が設定されていません");
                    return true;
                }
                new StartGameTask(5).runTask(this);
                return true;

            case "stop":
                if(manager.game) manager.endGame(3, 3);
                else sender.sendMessage("§c試合は行われていません");
                return true;

            case "add":
                if(manager.game){
                    sender.sendMessage("試合中はチームの変更が出来ません");
                    return true;
                }
                if(args.length < 2){
                    sender.sendMessage("§cチームカラーを入力して下さい(redまたはblue)");
                    return true;
                }
                if(!args[1].equalsIgnoreCase("red") && !args[1].equalsIgnoreCase("blue")){
                    sender.sendMessage("§c正しいチームカラーを入力してください(redまたはblue)");
                    return true;
                }
                if(args.length < 3){
                    sender.sendMessage("§cプレイヤー名を入力して下さい");
                    return true;
                }
                if(!(getServer().getPlayer(args[2]) instanceof Player)){
                    sender.sendMessage("§c"+args[2]+"はサーバーにいません");
                    return true;
                }
                player = main.getServer().getPlayer(args[2]);
                name = player.getName();
                if(args[1].equalsIgnoreCase("red")){
                    if(manager.isRedTeam(name)){
                        sender.sendMessage("§c"+name+"は既に赤チームに入っています");
                    }else{
                        manager.addRedTeam(name);
                        sender.sendMessage(name+"を赤チームに入れました");
                    }
                    return true;
                }else if(args[1].equalsIgnoreCase("blue")){
                    if(manager.isBlueTeam(name)){
                        sender.sendMessage("§c"+name+"は既に青チームに入っています");
                    }else{
                        manager.addBlueTeam(name);
                        sender.sendMessage(name+"を青チームに入れました");
                    }
                    return true;
                }
                return true;

            case "remove":
                if(manager.game){
                    sender.sendMessage("試合中はチームの変更が出来ません");
                    return true;
                }
                if(args.length < 2){
                    sender.sendMessage("§cプレイヤー名を入力して下さい");
                    return true;
                }
                if(!(getServer().getPlayer(args[1]) instanceof Player)){
                    sender.sendMessage("§c"+args[1]+"はサーバーにいません");
                    return true;
                }
                player = main.getServer().getPlayer(args[1]);
                name = player.getName();
                if(manager.isRedTeam(name)){
                    manager.removeRedTeam(name);
                    sender.sendMessage(name+"を赤チームから除外しました");
                }else if(manager.isBlueTeam(name)){
                    manager.removeBlueTeam(name);
                    sender.sendMessage(name+"を青チームから除外しました");
                }else{
                    sender.sendMessage("§c"+name+"はチームに入っていません");
                }
                return true;

            case "reset":
                if(manager.game){
                    sender.sendMessage("試合中はチームの変更が出来ません");
                }else{
                    manager.resetTeam();
                    sender.sendMessage("チームをリセットしました");
                }
                return true;

            case "set":
                if(manager.game){
                    sender.sendMessage("試合中は設定の変更が出来ません");
                    return true;
                }
                if(args.length < 2){
                    sendSettingCommandHelp((Player)sender);
                    return true;
                }
                switch(args[1]){
                    case "life":
                        if(args.length < 3){
                            sender.sendMessage("§c0以上の整数を入力してください(0にした場合ライフ無限)");
                        }else if(!isNumber(args[2])){
                            sender.sendMessage("§c0以上の整数を入力してください(0にした場合ライフ無限)");
                        }else{
                            num = Integer.parseInt(args[2]);
                            if(num < 0){
                                sender.sendMessage("§c0以上の整数を入力してください");
                                return true;
                            }
                            manager.setMaxLifePoint(num);
                            if(args[2].equals("0")) sender.sendMessage("ライフ数を無限にしました");
                            else sender.sendMessage("ライフ数を"+args[2]+"に設定しました");
                        }
                        return true;

                    case "point":
                        if(args.length < 3){
                            sender.sendMessage("§c1以上の整数を入力してください");
                        }else if(!isNumber(args[2])){
                            sender.sendMessage("§c1以上の整数を入力してください");
                        }else{
                            num = Integer.parseInt(args[2]);
                            if(num < 1){
                                sender.sendMessage("§c1以上の整数を入力してください");
                                return true;
                            }
                            manager.setAmount(num);
                            sender.sendMessage("勝利に必要なエメラルドの個数を"+args[2]+"個に設定しました");
                        }
                        return true;

                    case "gamemode":
                        if(args.length < 3){
                            sender.sendMessage("§cゲームモードを入力してください(survivalまたはadventure)");
                        }else if(args[2].equalsIgnoreCase("survival")){
                            manager.setGamemode("survival");
                            sender.sendMessage("試合中のゲームモードをサバイバルに設定しました");
                        }else if(args[2].equalsIgnoreCase("adventure")){
                            manager.setGamemode("adventure");
                            sender.sendMessage("試合中のゲームモードをアドベンチャーに設定しました");
                        }else{
                            sender.sendMessage("§c正しいゲームモードを入力してください(survivalまたはadventure)");
                        }
                        return true;

                    case "spawn":
                        if(args.length < 3){
                            sender.sendMessage("§cチームカラーを入力してください(redまたはblue)");
                        }else if(args[2].equalsIgnoreCase("red")){
                            manager.setRedSpawnLocation(((Player)sender).getLocation());
                            sender.sendMessage("赤チームのリスポーン地点を現在地に設定しました");
                        }else if(args[2].equalsIgnoreCase("blue")){
                            manager.setBlueSpawnLocation(((Player)sender).getLocation());
                            sender.sendMessage("青チームのリスポーン地点を現在地に設定しました");
                        }else{
                            sender.sendMessage("§c正しいチームカラーを入力してください(redまたはblue)");
                        }
                        return true;

                    case "eme":
                        if(args.length < 3){
                            sender.sendMessage("§cチームカラーを入力してください(redまたはblue)");
                        }else if(args[2].equalsIgnoreCase("red")){
                            eme.put(sender.getName(), 1);
                            dlv.put(sender.getName(), 0);
                            sender.sendMessage("赤チームのエメラルドブロックにしたいブロックをタッチして下さい");
                        }else if(args[2].equalsIgnoreCase("blue")){
                            eme.put(sender.getName(), 2);
                            dlv.put(sender.getName(), 0);
                            sender.sendMessage("青チームのエメラルドブロックにしたいブロックをタッチして下さい");
                        }else{
                            sender.sendMessage("§c正しいチームカラーを入力してください(redまたはblue)");
                        }
                        return true;

                    case "dlv":
                        if(args.length < 3){
                            sender.sendMessage("§cチームカラーを入力してください(redまたはblue)");
                        }else if(args[2].equalsIgnoreCase("red")){
                            dlv.put(sender.getName(), 1);
                            eme.put(sender.getName(), 0);
                            sender.sendMessage("赤チームの納品ブロックにしたいブロックをタッチして下さい");
                        }else if(args[2].equalsIgnoreCase("blue")){
                            dlv.put(sender.getName(), 2);
                            eme.put(sender.getName(), 0);
                            sender.sendMessage("青チームの納品ブロックにしたいブロックをタッチして下さい");
                        }else{
                            sender.sendMessage("§c正しいチームカラーを入力してください(redまたはblue)");
                        }
                        return true;
                }
        }
        return false;
    }

    public void sendCommandHelp(Player player){
        player.sendMessage("納品PVPコマンド");
        player.sendMessage("/dpvp start : 試合を開始します");
        player.sendMessage("/dpvp stop : 試合を強制終了します");
        player.sendMessage("/dpvp add <red|blue> <name> : プレイヤーをチームに追加します");
        player.sendMessage("/dpvp remove <name> : プレイヤーをチームから除外します");
        player.sendMessage("/dpvp reset : チームをリセットします");
        player.sendMessage("/dpvp set … : 設定コマンド、詳細は/dpvp setで");
    }

    public void sendSettingCommandHelp(Player player){
        player.sendMessage("納品PVP設定コマンド");
        player.sendMessage("/dpvp set life <number> : ライフ数を設定します(0にした場合ライフ無限)");
        player.sendMessage("/dpvp set point <number> : 勝利に必要なエメラルドの個数を設定します");
        player.sendMessage("/dpvp set gamemode <survival|adventure>");
        player.sendMessage("/dpvp set spawn <red|blue> : 現在地をチームのリスポーン地点に設定します");
        player.sendMessage("/dpvp set eme <red|blue> : タッチしたブロックをエメラルドブロックに設定します");
        player.sendMessage("/dpvp set dlv <red|blue> : タッチしたブロックを納品ブロックに設定します");
    }

    public boolean isNumber(String s){
        try{
            Integer.parseInt(s);
            return true;
        }catch(NumberFormatException e){
            return false;
        }
    }
}
