package nro.models.boss.broly;

import nro.consts.ConstPlayer;
import nro.models.boss.Boss;
import nro.models.boss.BossData;
import nro.models.boss.BossManager;
import nro.models.item.Item;
import nro.models.item.ItemOption;
import nro.models.map.ItemMap;
import nro.models.player.Player;
import nro.models.skill.Skill;
import nro.server.ServerNotify;
import nro.services.ChatGlobalService;
import nro.services.InventoryService;
import nro.services.ItemService;
import nro.services.PlayerService;
import nro.services.Service;
import nro.services.TaskService;
import nro.services.func.ChangeMapService;
import nro.utils.Util;

/**
 *
 * @author ADMIN
 */
public class HoTong extends Boss {

    public int mapHoTong;
    public Player plFollow;
    public int phiHoTong;
    protected long lastTimeChat;
    public String phamChatTieu;

    public HoTong(int id, Player plFollow, int phiHoTong, int mapHoTong, String phamChatTieu) {
        super(id, new BossData(
                "Vận tiêu " + plFollow.name,
                ConstPlayer.TRAI_DAT, //genderc
                Boss.DAME_NORMAL, //type dame
                Boss.HP_NORMAL, //type hp
                10_000, //dame
                new double[][]{{500_000_000_000_000_000_000_000_000d}}, //hp
                100_000,
                new short[]{1782, 1783, 1784}, //outfit {head, body, leg, bag, aura, eff}
                new short[]{0}, //map join
                new int[][]{
                    {Skill.KAMEJOKO, 4, 10000}},//skill
                0));
        this.plFollow = plFollow;
        this.phiHoTong = phiHoTong;
        this.mapHoTong = mapHoTong;
        this.phamChatTieu = phamChatTieu;
        this.lastTimeChangeMap = System.currentTimeMillis();
        this.lastTimeChat = System.currentTimeMillis();
    }

    @Override
    public void rewards(Player pl) {
        if (Util.isTrue(100, 100)) {
            int[] ramdomroiitem = new int[]{1511,1512,1513,1514,1515,1516,2062,2063,1510,1502};//list item
            ItemMap itemMap = new ItemMap(zone, ramdomroiitem[Util.nextInt(ramdomroiitem.length - 1)], 1, this.location.x,
                    zone.map.yPhysicInTop(this.location.x, this.location.y - 24), pl.id);
            itemMap.options.add(new ItemOption(30, 1));
          
            Service.getInstance().dropItemMap(this.zone, itemMap);
        }
        this.leaveMap();
        BossManager.gI().removeBoss(this);
    }

    @Override
    public void attack() {
        PlayerService.gI().changeAndSendTypePK(this, ConstPlayer.NON_PK);
        this.followPlayer();
        TaskService.gI().sendTaskMain(plFollow);
    }

    @Override
    public void joinMap() {
        super.joinMap();
    }

    public void moveTo(int x, int y) {
        byte dir = (byte) (this.location.x - x < 0 ? 1 : -1);
        byte move = (byte) Util.nextInt(20, 30);
        PlayerService.gI().playerMove(this, this.location.x + (dir == 1 ? move : -move), y);
    }

    public void followPlayer() {
        if (plFollow == null || plFollow.zone == null || plFollow.isDie() || this.isDie()) {
            this.leaveMap();
            BossManager.gI().removeBoss(this);
            return;
        }
        this.moveTo(this.plFollow.location.x, this.plFollow.location.y);
        if (Util.canDoWithTime(lastTimeChat, 10000)) {
            ChatGlobalService.gI().chatVanTieu(this, "Vận Tiêu Phẩm Chất (" + phamChatTieu
                    + ") tại " + this.zone.map.mapName);
            ServerNotify.gI().notify(plFollow.name + " đang vận tiêu tại " + plFollow.zone.map.mapName);
            lastTimeChat = System.currentTimeMillis();
        }
        if (Util.canDoWithTime(lastTimeChangeMap, 1200000)) {
            super.leaveMap();
            BossManager.gI().removeBoss(this);
        }
        if (this.zone != null && !this.zone.equals(plFollow.zone)) {
            ChangeMapService.gI().changeMap(this, plFollow.zone, plFollow.location.x, plFollow.location.y);
        }
        if (this.cFlag != plFollow.cFlag) {
            Service.gI().changeFlag(this, plFollow.cFlag);
        }
        if (this.zone.map.mapId == this.mapHoTong) {
            int[] listReward = {1005,1010,1015,1020,1037,1038,1039,1040};
            Item item = ItemService.gI().createNewItem((short) listReward[Util.nextInt(listReward.length - 1)], phiHoTong * Util.nextInt(1, 3));
            item.itemOptions.add(new ItemOption(30, 1));
            Service.gI().sendThongBaoFromAdmin(plFollow, "Bạn đã vận tiêu đến đích thành công!\nBạn nhận được x" + item.quantity + " " + item.template.name);
            InventoryService.gI().addItemBag(plFollow, item, 9999);
            InventoryService.gI().sendItemBags(plFollow);
            this.leaveMap();
            BossManager.gI().removeBoss(this);
        }
    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    protected boolean useSpecialSkill() {
        return false;
    }

    @Override
    public void idle() {
    }

    @Override
    public void checkPlayerDie(Player pl) {
    }

    @Override
    public void initTalk() {
    }
}
