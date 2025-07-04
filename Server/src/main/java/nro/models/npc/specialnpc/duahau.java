/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nro.models.npc.specialnpc;

import nro.models.item.Item;
import nro.models.player.Player;
import nro.server.io.Message;
import nro.services.InventoryService;
import nro.services.ItemService;
import nro.services.PetService;
import nro.services.Service;
import nro.services.func.ChangeMapService;
import nro.utils.Log;
import nro.utils.Util;

/**
 *
 * @author delb1
 */
public class duahau {

    private static final long DEFAULT_TIME_DONE = 86400000L;

    private Player player;
    public long lastTimeCreate;
    public long timeDone;

    private final short id = 51;

    public duahau(Player player, long lastTimeCreate, long timeDone) {
        this.player = player;
        this.lastTimeCreate = lastTimeCreate;
        this.timeDone = timeDone;
    }

    public static void createduahau(Player player) {
        if (player != null) {
            player.duahau = new duahau(player, System.currentTimeMillis(), DEFAULT_TIME_DONE);
        }
    }

    public void sendduahau() {
        Message msg;
        try {
            msg = new Message(-122);
            msg.writer().writeShort(this.id);
            msg.writer().writeByte(1);
            msg.writer().writeShort(4672);
            msg.writer().writeByte(0);
            msg.writer().writeInt(this.getSecondDone());
            this.player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {

        }
    }

    public int getSecondDone() {
        int seconds = (int) ((lastTimeCreate + timeDone - System.currentTimeMillis()) / 1000);
        return seconds > 0 ? seconds : 0;
    }

    public void openDuaHau() {
        try {
            destroyDuaHau();
            Thread.sleep(4000);
            player.inventory.ruby += 10000;
            Service.getInstance().sendMoney(player);
            Item duahai = ItemService.gI().createNewItem((short) 669, 1);
            InventoryService.gI().addItemBag(player, duahai, 99999);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player, "Bạn đã nhận được " + duahai.template.name);
            Service.getInstance().sendThongBao(player, "Bạn đã nhận được 10k ngọc");
            ChangeMapService.gI().changeMapInYard(this.player, this.player.gender * 7, -1, Util.nextInt(300, 500));
            player.duahau = null;
        } catch (Exception e) {
        }

    }

    public void destroyDuaHau() {
        try {
            Message msg = new Message(-117);
            msg.writer().writeByte(101);
            player.sendMessage(msg);
            msg.cleanup();
            
        } catch (Exception e) {
        }
        this.player.duahau = null;
    }

    public void subTimeDone(int d, int h, int m, int s) {
        this.timeDone -= ((d * 24 * 60 * 60 * 1000) + (h * 60 * 60 * 1000) + (m * 60 * 1000) + (s * 1000));
        this.sendduahau();
    }

    public void dispose() {
        this.player = null;
    }
}
