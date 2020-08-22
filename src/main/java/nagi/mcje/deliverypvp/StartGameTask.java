package nagi.mcje.deliverypvp;

import org.bukkit.scheduler.BukkitRunnable;

public class StartGameTask extends BukkitRunnable{

    private int time;

    public StartGameTask(int second){
        time = second;
    }

    @Override
    public void run(){
        Main main = Main.getInstance();
        if(time == 0){
            main.manager.startGame();
        }else{
            main.manager.times = " 試合開始まで"+time+"秒";
            time--;
            new StartGameTask(time).runTaskLater(main, 20);
        }
    }
}
