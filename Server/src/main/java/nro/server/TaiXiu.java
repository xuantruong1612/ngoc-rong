/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nro.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import nro.models.player.Player;
import nro.server.io.Message;
import nro.services.InventoryService;
import nro.services.Service;
import nro.utils.Util;

/**
 *
 * @author Administrator
 */
public class TaiXiu implements Runnable {

    public int goldTai;
    public int goldXiu;
    public boolean ketquaTai = false;
    public boolean ketquaXiu = false;

    public boolean baotri = false;
    public long lastTimeEnd;
    public List<Player> PlayersTai = new ArrayList<>();
    public List<Player> PlayersXiu = new ArrayList<>();
    public List<Integer> tongHistory = new ArrayList<>();
    private static final int MAX_HISTORY_SIZE = 15;
    public List<String> tongHistoryString = new ArrayList<>();
    private static TaiXiu instance;
    public int x, y, z;

    public static TaiXiu gI() {
        if (instance == null) {
            instance = new TaiXiu();
        }
        return instance;
    }

    public void addPlayerXiu(Player pl) {
        if (!PlayersXiu.equals(pl)) {
            PlayersXiu.add(pl);
        }
    }

    public void addPlayerTai(Player pl) {
        if (!PlayersTai.equals(pl)) {
            PlayersTai.add(pl);
        }
    }

    public void removePlayerXiu(Player pl) {
        if (PlayersXiu.equals(pl)) {
            PlayersXiu.remove(pl);
        }
    }

    public void removePlayerTai(Player pl) {
        if (PlayersTai.equals(pl)) {
            PlayersTai.remove(pl);
        }
    }

    private String echotaixiu(int tong) {
        return (tong >= 11) ? "Tài" : "Xỉu";
    }

    private void addvaolist(int tong) {
        tongHistory.add(tong);

    }

    private void trim() {
        while (tongHistory.size() > MAX_HISTORY_SIZE) {
            tongHistory.remove(0);
        }
    }

    private void chuyenthanhstring() {
        tongHistoryString.clear();
        for (int i = 0; i < tongHistory.size(); i++) {
            int tong = tongHistory.get(i);
            String tongStr = echotaixiu(tong);
            tongHistoryString.add(tongStr);
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (((TaiXiu.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) <= 0) {
                    int tile = new Random().nextInt(100);
                    int x, y, z;
                    x = new Random().nextInt(1, 6);
                    y = new Random().nextInt(1, 6);
                    z = new Random().nextInt(1, 6);
                    int tong3so = (x + y + z);
                    if (goldTai > goldXiu) {
                        if (tile > 35) {
                            while (tong3so > 11) {
                                x = new Random().nextInt(1, 6);
                                y = new Random().nextInt(1, 6);
                                z = new Random().nextInt(1, 6);
                                tong3so = x + y + z;
                            }
                        }
                    }
                    if (goldTai < goldXiu) {
                        if (tile > 35) {
                            while (tong3so < 10) {
                                x = new Random().nextInt(1, 6);
                                y = new Random().nextInt(1, 6);
                                z = new Random().nextInt(1, 6);
                                tong3so = x + y + z;
                            }
                        }
                    }
                    this.x = x;
                    this.y = y;
                    this.z = z;
                    int tong = this.x + this.y + this.z;
                    addvaolist(tong);
                    if (tong < 11) {
                        ketquaXiu = true;
                        ketquaTai = false;
                    }
                    if (tong > 10) {
                        ketquaXiu = false;
                        ketquaTai = true;
                    }
                    if (ketquaTai == true) {
                        if (!TaiXiu.gI().PlayersTai.isEmpty()) {
                            for (int i = 0; i < PlayersTai.size(); i++) {
                                Player pl = this.PlayersTai.get(i);
                                if (pl != null && Client.gI().getPlayer(pl.name) != null) {
                                    int goldC = pl.goldTai + pl.goldTai * 80 / 100;
                                    Service.getInstance().sendThongBao(pl, "Số hệ thống quay ra\n" + x + " : "
                                            + y + " : " + z + "\n|5|Tổng là : " + tong + "\n(TÀI)\n\n|1|Bạn đã chiến thắng!!");
                                    Service.getInstance().sendThongBao(pl, "Chúc mừng bạn đã dành chiến thắng và nhận được " + Util.format(goldC) + " Hồng ngọc");
                                    pl.inventory.ruby += goldC;
                                    pl.taixiu.win += pl.goldTai * 80 / 100;
                                    Service.getInstance().sendMoney(pl);
                                    InventoryService.gI().sendItemBags(pl);
                                    Message m;
                                    try {
                                        m = new Message(-126);
                                        m.writer().writeByte(1);
                                        m.writer().writeByte(1);
                                        m.writer().writeUTF("00" + TaiXiu.gI().x + TaiXiu.gI().y + TaiXiu.gI().z);
                                        //   m.writer().writeUTF("99999");
                                        m.writer().writeUTF("00" + TaiXiu.gI().x + TaiXiu.gI().y + TaiXiu.gI().z);
                                        pl.sendMessage(m);
                                        m.cleanup();
                                    } catch (Exception e) {
                                    }
                                }
                            }
                        }
                        for (int i = 0; i < PlayersXiu.size(); i++) {
                            Player pl = this.PlayersXiu.get(i);
                            if (pl != null && Client.gI().getPlayer(pl.name) != null) {
                                Service.getInstance().sendThongBao(pl, "Số hệ thống quay ra\n" + x + " : "
                                        + y + " : " + z + "\n|5|Tổng là : " + tong + "\n(TÀI)\n\n|7|Trắng tay gòi, chơi lại đi!!!");

                            }
                            Message m;
                            try {
                                m = new Message(-126);
                                m.writer().writeByte(1);
                                m.writer().writeByte(1);
                                m.writer().writeUTF("00" + TaiXiu.gI().x + TaiXiu.gI().y + TaiXiu.gI().z);
                                //  m.writer().writeUTF("99999");
                                m.writer().writeUTF("00" + TaiXiu.gI().x + TaiXiu.gI().y + TaiXiu.gI().z);
                                pl.sendMessage(m);
                                m.cleanup();
                            } catch (Exception e) {
                            }
                        }
                    } else if (ketquaXiu == true) {
                        if (!TaiXiu.gI().PlayersXiu.isEmpty()) {
                            for (int i = 0; i < PlayersXiu.size(); i++) {
                                Player pl = this.PlayersXiu.get(i);
                                if (pl != null && Client.gI().getPlayer(pl.name) != null) {
                                    int goldC = pl.goldXiu + pl.goldXiu * 80 / 100;
                                    Service.getInstance().sendThongBao(pl, "Số hệ thống quay ra\n" + x + " : "
                                            + y + " : " + z + "\n|5|Tổng là : " + tong + "\n(XỈU)\n\n|1|Bạn đã chiến thắng!!");
                                    Service.getInstance().sendThongBao(pl, "Chúc mừng bạn đã dành chiến thắng và nhận được " + Util.format(goldC) + " Hồng ngọc");
                                    pl.inventory.ruby += goldC;
                                    pl.taixiu.win += pl.goldXiu * 80 / 100;
                                    Service.getInstance().sendMoney(pl);
                                    InventoryService.gI().sendItemBags(pl);
                                    Message m;
                                    try {
                                        m = new Message(-126);
                                        m.writer().writeByte(1);
                                        m.writer().writeByte(1);
                                        m.writer().writeUTF("00" + TaiXiu.gI().x + TaiXiu.gI().y + TaiXiu.gI().z);
                                        m.writer().writeUTF("00" + TaiXiu.gI().x + TaiXiu.gI().y + TaiXiu.gI().z);
                                        pl.sendMessage(m);
                                        m.cleanup();
                                    } catch (Exception e) {
                                    }
                                }
                            }
                        }
                        for (int i = 0; i < PlayersTai.size(); i++) {
                            Player pl = this.PlayersTai.get(i);
                            if (pl != null && Client.gI().getPlayer(pl.name) != null) {
                                Service.getInstance().sendThongBao(pl, "Số hệ thống quay ra\n" + x + " : "
                                        + y + " : " + z + "\n|5|Tổng là : " + tong + "\n(XỈU)\n\n|7|Trắng tay gòi, chơi lại đi!!!");
                            }
                            Message m;
                            try {
                                m = new Message(-126);
                                m.writer().writeByte(1);
                                m.writer().writeByte(1);
                                m.writer().writeUTF("00" + TaiXiu.gI().x + TaiXiu.gI().y + TaiXiu.gI().z);
                                //m.writer().writeUTF("99999"); // 
                                m.writer().writeUTF("00" + TaiXiu.gI().x + TaiXiu.gI().y + TaiXiu.gI().z);
                                pl.sendMessage(m);
                                m.cleanup();
                            } catch (Exception e) {
                            }

                        }
                    }
                    for (int i = 0; i < TaiXiu.gI().PlayersTai.size(); i++) {
                        Player pl = TaiXiu.gI().PlayersTai.get(i);
                        if (pl != null) {
                            pl.goldTai = 0;
                        }
                    }
                    for (int i = 0; i < TaiXiu.gI().PlayersXiu.size(); i++) {
                        Player pl = TaiXiu.gI().PlayersXiu.get(i);
                        if (pl != null) {
                            pl.goldXiu = 0;
                        }
                    }
                    ketquaXiu = false;
                    ketquaTai = false;
                    TaiXiu.gI().goldTai = 0;
                    TaiXiu.gI().goldXiu = 0;
                    trim();
                    chuyenthanhstring();
                    TaiXiu.gI().PlayersTai.clear();
                    TaiXiu.gI().PlayersXiu.clear();
                    TaiXiu.gI().lastTimeEnd = System.currentTimeMillis() + 10000;
                }
                Thread.sleep(100);
            } catch (Exception e) {
            }
        }
    }
}
