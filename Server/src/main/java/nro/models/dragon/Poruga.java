package nro.models.dragon;

import nro.dialog.MenuDialog;
import nro.dialog.MenuRunable;
import nro.manager.NamekBallManager;
import nro.models.clan.Buff;
import nro.models.clan.Clan;
import nro.models.map.Zone;
import nro.models.player.Player;
import nro.server.io.Message;
import nro.services.NpcService;
import nro.services.Service;

import java.io.DataOutputStream;
import nro.models.item.Item;
import nro.services.InventoryService;
import nro.services.ItemService;

/**
 * @build by arriety
 */
public class Poruga extends AbsDragon {

    public Poruga(Player player) {
        super(player);
        this.setWishes(new String[]{"Nhận x10\nĐá Bảo Vệ", "Nhận\nx3 3Sao", "Nhận x99\n Đấ nâng cấp các loại"});
        this.setTutorial("");
        this.setContent("Ta sẽ ban cho ngươi điều ước,ngươi có 5 phút,hãy suy nghĩ thật kĩ trước khi quyết định,tác dụng của chúc phúc sẽ có hiệu lực đến 6h AM");
        this.setName("Rồng thần Namek");
    }

    @Override
    public void openMenu() {
    }

    @Override
    public void summon() {
        setAppear(true);
        callDragon();
        showWishes();
        setLastTimeAppear(System.currentTimeMillis());
        new Thread(this).start();
        sendNotify();
    }

    @Override
    public void reSummon() {

    }

    @Override
    public void showWishes() {
        Clan clan = getSummoner().clan;
        MenuDialog menu = new MenuDialog(getContent(), getWishes(), new MenuRunable() {
            @Override
            public void run() {
                Item item = null;
                switch (this.getIndexSelected()) {
                    case 0: 
                        for (Player player : clan.membersInGame) {
                            InventoryService.gI().addItemBag(player, ItemService.gI().createNewItem((short) 987, 10), 9999);
                            InventoryService.gI().sendItemBags(player);
                            Service.getInstance().sendThongBao(player, "Bạn vừa nhận được chúc phúc của rồng thần Poruga");
                        }
                        break;
                    case 1:
                        for (Player player : clan.membersInGame) {
                            InventoryService.gI().addItemBag(player, ItemService.gI().createNewItem((short) 16, 3), 9999);
                            InventoryService.gI().sendItemBags(player);
                            Service.getInstance().sendThongBao(player, "Bạn vừa nhận được chúc phúc của rồng thần Poruga");
                        }
                        break;
                    case 2:
                        for (Player player : clan.membersInGame) {
                            InventoryService.gI().addItemBag(player, ItemService.gI().createNewItem((short) 220, 99), 9999);
                            InventoryService.gI().addItemBag(player, ItemService.gI().createNewItem((short) 221, 99), 9999);
                            InventoryService.gI().addItemBag(player, ItemService.gI().createNewItem((short) 222, 99), 9999);
                            InventoryService.gI().addItemBag(player, ItemService.gI().createNewItem((short) 223, 99), 9999);
                            InventoryService.gI().addItemBag(player, ItemService.gI().createNewItem((short) 224, 99), 9999);
                            InventoryService.gI().sendItemBags(player);
                            Service.getInstance().sendThongBao(player, "Bạn vừa nhận được chúc phúc của rồng thần Poruga");
                        }
                        break;
                }
                leave();
            }
        });
        menu.show(getSummoner());
    }

    @Override
    public void callDragon() {
        Message msg = new Message(-83);
        DataOutputStream ds = msg.writer();
        try {
            ds.writeByte(isAppear() ? 0 : (byte) 1);
            if (isAppear()) {
                Zone z = getSummoner().zone;
                ds.writeShort(z.map.mapId);
                ds.writeShort(z.map.bgId);
                ds.writeByte(z.zoneId);
                ds.writeInt((int) getSummonerID());
                ds.writeUTF("");
                ds.writeShort(getSummoner().location.x);
                ds.writeShort(getSummoner().location.y);
                ds.writeByte(1);
            }
            ds.flush();
            Service.getInstance().sendMessAllPlayer(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    @Override
    public void leave() {
        NpcService.gI().createTutorial(getSummoner(), -1, "Điều ước của ngươi đã trở thành sự thật\nHẹn gặp ngươi lần sau, ta đi ngủ đây, bái bai");
        setAppear(
                false);
        callDragon();
        NamekBallManager.gI().initFossil();
    }
}
