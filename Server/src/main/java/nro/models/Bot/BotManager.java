package nro.models.Bot;

import java.util.ArrayList;
import java.util.List;
import nro.server.ServerManager;

public class BotManager implements Runnable {

    public static BotManager i;

    public List<Bot> bot = new ArrayList<>();
    private volatile boolean running = true;  // Biến điều kiện để dừng bot

    public static BotManager gI() {
        if (i == null) {
            i = new BotManager();
        }
        return i;
    }

    // Phương thức dùng để dừng bot
    public void stopRunning() {
        running = false;  // Đặt running là false để dừng vòng lặp
        this.bot.clear();
    }

    // Phương thức chạy bot
    @Override
    public void run() {
        while (running && ServerManager.isRunning) {  // Kiểm tra biến running
            try {
                long st = System.currentTimeMillis();
                for (Bot bot : this.bot) {
                    bot.update();
                }
                Thread.sleep(150 - (System.currentTimeMillis() - st));
            } catch (Exception ignored) {
            }
        }
        System.out.println("Bot manager stopped.");
    }
}
