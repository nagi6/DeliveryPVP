package nagi.mcje.deliverypvp;

import org.bukkit.scheduler.BukkitRunnable;

public class CountTimeTask extends BukkitRunnable{

    @Override
    public void run(){
        Main main = Main.getInstance();
        if(main.manager.game){
            main.manager.time++;
            double sec = (double)main.manager.time;
            double min = Math.floor((double)sec / 60);
            sec = sec - (min * 60);
            main.manager.times = " 経過時間:"+(int)min+"分"+(int)sec+"秒";
            new UpdateActionBarTask().runTaskAsynchronously(main);
            new CountTimeTask().runTaskLater(main, 20);
        }
    }
}
