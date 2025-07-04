package nro.models.boss;

import java.io.IOException;
import nro.utils.Log;
import java.util.ArrayList;
import java.util.List;
import nro.models.Part;
import nro.models.PartManager;
import nro.models.map.Zone;
import nro.models.player.Player;
import nro.server.Client;
import nro.server.io.Message;
import nro.services.MapService;
import nro.services.Service;
import nro.services.func.ChangeMapService;
import nro.utils.Util;

/**
 *
 * @stole Arriety
 *
 */
public class BossManager {

    public static final List<Boss> BOSSES_IN_GAME;
    private static BossManager intance;

    static {
        BOSSES_IN_GAME = new ArrayList<>();
    }

    public void updateAllBoss() {
        for (int i = BOSSES_IN_GAME.size() - 1; i >= 0; i--) {
            try {
                Boss boss = BOSSES_IN_GAME.get(i);
                if (boss != null) {
                    boss.update();
                }
            } catch (Exception e) {
                Log.error(BossManager.class, e);
            }
        }
    }

    private BossManager() {

    }

   

    public void FindBoss(Player player, int id) {
        Boss boss = BossManager.gI().getBossById(id);
        if (boss != null && boss.zone != null && boss.zone.map != null && !boss.isDie()) {
            Zone z = MapService.gI().getMapCanJoin(player, boss.zone.map.mapId, boss.zone.zoneId);
            if (z.getNumOfPlayers() < z.maxPlayer) {
                ChangeMapService.gI().changeMap(player, boss.zone, boss.location.x, boss.location.y);
            } else {
                Service.getInstance().sendThongBao(player, "Khu vực đang full.");
            }
        } else {
            Service.getInstance().sendThongBao(player, "");
        }
    }

 public void showListBoss(Player player) {
    Message msg;
    try {
        msg = new Message(-96);
        msg.writer().writeByte(1);
        msg.writer().writeUTF("Boss (SL: " + BOSSES_IN_GAME.size() + ")");

        // Tạo bản sao của danh sách để tránh ConcurrentModificationException
        List<Boss> bossListCopy;
        synchronized (BOSSES_IN_GAME) {
            bossListCopy = new ArrayList<>(BOSSES_IN_GAME);
        }

        long aliveBossCount = bossListCopy.stream()
                .filter(boss -> boss != null && !BossFactory.isYar((byte) boss.id))
                .count();
        msg.writer().write((int) aliveBossCount);

        for (Boss boss : bossListCopy) {
            if (boss == null || BossFactory.isYar((byte) boss.id)) {
                continue;
            }

            msg.writer().writeInt((int) boss.id);
            msg.writer().writeInt((int) boss.id);
            msg.writer().writeShort(boss.getHead());
            if (player.isVersionAbove(220)) {
                Part part = PartManager.getInstance().find(boss.getHead());
                msg.writer().writeShort(part.getIcon(0));
            }
            msg.writer().writeShort(boss.getBody());
            msg.writer().writeShort(boss.getLeg());
            msg.writer().writeUTF(boss.name);

            if (boss.zone != null && boss.zone.map != null) {
                msg.writer().writeUTF("on");
                msg.writer().writeUTF("Thông Tin Boss\n" +
                        "|4|Bản Đồ : [" + boss.zone.map.mapName + "][" + boss.zone.map.mapId + "] \n" +
                        "Khu Vực: [" + boss.zone.zoneId + "]\n" +
                        "HP: " + Util.numberToMoney(boss.nPoint.hp) + "\n" +
                        "Dame: " + Util.numberToMoney(boss.nPoint.dame) + "\n" +
                        "Chí Mạng: " + Util.numberToMoney(boss.nPoint.crit));
            } else {
                msg.writer().writeUTF("off");
                msg.writer().writeUTF("Thông Tin Boss\n" +
                        "|7|Time Reset: " + (boss.secondsRest == 0 ? "Chưa Xác Định" : Util.tinhgiay(boss.secondsRest)));
            }
        }

        player.sendMessage(msg);
        msg.cleanup();
    } catch (Exception e) {
        e.printStackTrace();
    }
}


    public static BossManager gI() {
        if (intance == null) {
            intance = new BossManager();
        }
        return intance;
    }

    public Boss findBossByName(String name) {
        for (Boss boss : BOSSES_IN_GAME) {
            if (boss != null && boss.name.equalsIgnoreCase(name)) {
                return boss;
            }
        }
        return null;
    }

    public Boss getBossById(int bossId) {
        for (int i = BOSSES_IN_GAME.size() - 1; i >= 0; i--) {
            if (BOSSES_IN_GAME.get(i).id == bossId) {
                return BOSSES_IN_GAME.get(i);
            }
        }
        return null;
    }

    public void addBoss(Boss boss) {
        boolean have = false;
        for (Boss b : BOSSES_IN_GAME) {
            if (boss.equals(b)) {
                have = true;
                break;
            }
        }
        if (!have) {
            BOSSES_IN_GAME.add(boss);
        }
    }

    public void removeBoss(Boss boss) {
        BOSSES_IN_GAME.remove(boss);
        boss.dispose();
    }
    
    public void teleBoss(Player pl, Message msg) {
         if (msg == null || (!pl.isAdmin() && pl.vip < 4)) {
    // System.out.println("Có Người Đang Tele Boss");
              Service.getInstance().sendThongBao(pl, "Chức Năng Dành Từ Vip 4");
            return;
            
        }
            try {
                int id = msg.reader().readInt();
                Boss b = getBossById(id);
                if (b == null) {
                    Player player = Client.gI().getPlayer(id);
                    if (player != null && player.zone != null) {
                        ChangeMapService.gI().changeMapYardrat(pl, player.zone, player.location.x, player.location.y);
                        return;
                    } else {
                        Service.getInstance().sendThongBao(pl, "Nó trốn rồi");
                        return;
                    }
                }
                if (!b.isDie() && b.zone != null) {
                    ChangeMapService.gI().changeMapYardrat(pl, b.zone, b.location.x, b.location.y);
                } else {
                    Service.getInstance().sendThongBao(pl, "Boss Hẹo Rồi");
                }
            } catch (IOException e) {
                System.out.println("Lỗi tele boss");
            }
        }
    
    public void teleBossvip(Player pl, Message msg) {
         if (msg == null || pl.vip >= 6) {
             
              Service.getInstance().sendThongBao(pl, "Chức Năng Dành Tử Vip 6");
            return;
            
        }
            try {
                int id = msg.reader().readInt();
                Boss b = getBossById(id);
                if (b == null) {
                    Player player = Client.gI().getPlayer(id);
                    if (player != null && player.zone != null) {
                        ChangeMapService.gI().changeMapYardrat(pl, player.zone, player.location.x, player.location.y);
                        return;
                    } else {
                        Service.getInstance().sendThongBao(pl, "Nó trốn rồi");
                        return;
                    }
                }
                if (!b.isDie() && b.zone != null) {
                    ChangeMapService.gI().changeMapYardrat(pl, b.zone, b.location.x, b.location.y);
                } else {
                    Service.getInstance().sendThongBao(pl, "Boss Hẹo Rồi");
                }
            } catch (IOException e) {
                System.out.println("Lỗi tele boss");
            }
        }
      
    
    
    

    public void summonBoss(Player pl, Message msg) {
         if (msg == null || (!pl.isAdmin() )) {
              Service.getInstance().sendThongBao(pl, "Chức Năng Dành Cho ADMIN");
          //     System.out.println("Có Người Đang Triệu Hồi Boss");
            return;
        }
        try {
            int id = msg.reader().readInt();
            Boss b = getBossById(id);
            if (b != null && b.zone != null) {
                ChangeMapService.gI().changeMapYardrat(b, pl.zone, pl.location.x, pl.location.y);
            } else if (b == null) {
                Player player = Client.gI().getPlayer(id);
                if (player == null) {
                    Service.getInstance().sendThongBao(pl, "Nó trốn rồi");
                }
                if (player != null && player.zone != null) {
                    
                    ChangeMapService.gI().changeMapYardrat(player, pl.zone, pl.location.x, pl.location.y);
                }
            }
        } catch (IOException e) {
            System.out.println("Lỗi summon boss");
        }
    }

}
