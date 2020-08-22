package nagi.mcje.deliverypvp;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class UpdateActionBarTask extends BukkitRunnable{

    public void run(){
        Main main = Main.getInstance();
        int online = main.getServer().getOnlinePlayers().size();
        int red = main.manager.notr;
        int blue = main.manager.notb;
        String msg = "("+online+"人が参加中 青チーム:"+blue+"人 赤チーム"+red+"人"+main.manager.times;
        if(main.manager.game) msg = msg+" 青チーム:"+main.manager.getRedPoint()+"個 赤チーム:"+main.manager.getBluePoint()+"個";
        else new UpdateActionBarTask().runTaskLaterAsynchronously(main, 20);
        msg = msg+")";
        for(Player player : main.getServer().getOnlinePlayers()){
            TextComponent component = new TextComponent();
            component.setText(msg);
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component);
        }
    }
}
