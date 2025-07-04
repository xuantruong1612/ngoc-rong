package nro.models.boss.BossAnTrom;

import java.util.ArrayList;
import java.util.List;
import nro.consts.ConstPlayer;
import nro.models.boss.Boss;
import nro.models.boss.BossData;
import static nro.models.boss.BossFactory.NOEN;
import static nro.models.boss.BossFactory.TROMNGOC;
import static nro.models.boss.BossFactory.TROMNGOCRUBY;

import nro.models.item.ItemOption;
import nro.models.map.ItemMap;
import nro.models.player.Player;
import nro.models.skill.Skill;
import nro.server.Client;
import nro.services.InventoryService;
import nro.services.PlayerService;
import nro.services.Service;
import nro.services.func.ChangeMapService;
import nro.utils.Util;
import static nro.utils.Util.thoivang;

/**
 *
 * @author ADMIN
 */
public class TromNgocRuby extends Boss {

    private long lastTimePhatQua;
    private long TIME_PHAT_QUA;
    private final long TIME_CHANGE_PLAYER = 1000;
    private long lastTimeJoinMap;
    
    private static final int MIN_GOLD_THRESHOLD = 100000000; // Ngưỡng vàng tối thiểu (100 triệu)
private static final int MIN_GOLD_STEAL = 1; // Số vàng tối thiểu bị trộm
private static final int MAX_GOLD_STEAL = 20000000; // Số vàng tối đa bị trộm

    public TromNgocRuby() {
        super(TROMNGOCRUBY, new BossData(
                "Trộm Ruby",
                ConstPlayer.TRAI_DAT, //genderc
                Boss.DAME_NORMAL, //type dame
                Boss.HP_NORMAL, //type hp
                10_000, //dame
                 10_000, //dame
                new double[][]{{500_000_000d}}, //hp
                new short[]{1821,	1822,	1823}, //outfit {head, body, leg, bag, aura, eff}
                new short[]{0}, //map join
                new int[][]{
                    {Skill.KAMEJOKO, 4, 10000},
                    {Skill.LIEN_HOAN, 4, 1000},
                    {Skill.TAI_TAO_NANG_LUONG, 4, 50000}},//skill
                1));
    }

    @Override
    public void attack() {
        this.stealGoldFromPlayer();
        this.cFlag = 8;
        Service.gI().changeFlag(this, this.cFlag);
        this.nPoint.tlNeDon = 1000;
        if (Util.canDoWithTime(lastTimeChangeMap, 60000)) {
            this.changeStatus(LEAVE_MAP);
        }
    }

    public Player getPlayerPhatQua() {
        try {
            if (countChangePlayerAttack < targetCountChangePlayerAttack && plAttack != null && plAttack.zone != null && plAttack.zone.equals(this.zone)) {
                if (!plAttack.isDie() && !plAttack.effectSkin.isVoHinh && !plAttack.isMiniPet) {
                    this.countChangePlayerAttack++;
                    return plAttack;
                } else {
                    plAttack = null;
                }
            } else {
                if (this.zone == null) {
                } else {
                    this.targetCountChangePlayerAttack = Util.nextInt(10, 20);
                    this.countChangePlayerAttack = 0;
                    plAttack = this.zone.getPlayers().stream().filter(pl -> pl.isPl() && !pl.isDie()).findAny().orElse(null);
                }
            }
        } catch (Exception e) {
        }
        return plAttack;
    }

    @Override
    public void joinMap() {
        super.joinMap();
        lastTimeChangeMap = System.currentTimeMillis();
        lastTimeJoinMap = System.currentTimeMillis() + TIME_CHANGE_PLAYER;
    }

    public void moveTo(int x, int y) {
        byte dir = (byte) (this.location.x - x < 0 ? 1 : -1);
        byte move = (byte) Util.nextInt(20, 30);
        PlayerService.gI().playerMove(this, this.location.x + (dir == 1 ? move : -move), y);
    }

public void stealGoldFromPlayer() {
    if (Util.canDoWithTime(lastTimeChangeMap, 1000)) {
        Player targetPlayer = getPlayerPhatQua(); // Lấy người chơi mục tiêu
        if (targetPlayer == null) {
            return; // Không có người chơi nào, thoát khỏi phương thức
        }

        // Di chuyển đến vị trí của người chơi
        this.moveTo(targetPlayer.location.x, targetPlayer.location.y);
        
        // Kiểm tra số vàng của người chơi
        if (targetPlayer.inventory.ruby >= 10000) { // Đảm bảo người chơi có vàng
            // Lấy vàng ngẫu nhiên từ MIN_GOLD_STEAL đến MAX_GOLD_STEAL
            int goldStolen = Util.nextInt(1, 100);
            
            // Đảm bảo rằng số vàng bị trộm không vượt quá số vàng hiện có
            if (goldStolen > targetPlayer.inventory.ruby) {
               
            }

            // Chỉ trộm vàng nếu số vàng bị trộm là dương
            if (goldStolen > 0) {
                targetPlayer.inventory.ruby -= goldStolen; // Giảm số vàng của người chơi
                this.inventory.ruby += goldStolen; // Tăng số vàng của boss
                this.chat("Haha! Tôi đã trộm\n " + goldStolen + " ruby của bạn! Cẩn thận nhé!\nBạn thật hào phóng\nXin hết nhé!!!");
            }
        } else {
            this.chat("Người chơi này \nNghèo quá\n không có ruby để trộm!");
        }

        this.TIME_PHAT_QUA = 5000; // Thời gian giữa các lần trộm
        this.lastTimePhatQua = System.currentTimeMillis(); // Cập nhật thời gian
    }
}


    // List item drop
    private List<ItemMap> getItemDrop() {
        List<ItemMap> listItem = new ArrayList<>();
        ItemMap itemAdd;
        itemAdd = new ItemMap(zone, 822, 1, this.location.x, this.location.y, this.plAttack.id);
         itemAdd.options.add(new ItemOption(174, 2024));
        listItem.add(itemAdd);
       
//        itemAdd = new ItemMap(zone, 648, 10, this.location.x, this.location.y, this.plAttack.id);
//        itemAdd.options.add(new ItemOption(30, 1));
//        listItem.add(itemAdd); 
//        
        
         itemAdd = new ItemMap(zone, 822, 1, this.location.x, this.location.y, this.plAttack.id);
        itemAdd.options.add(new ItemOption(174, 2024));
        listItem.add(itemAdd);
//        
//          itemAdd = new ItemMap(zone, 457, 1, this.location.x, this.location.y, this.plAttack.id);
//        itemAdd.options.add(new ItemOption(30, Util.nextInt(1, 68)));
//        itemAdd.options.add(new ItemOption(93, Util.nextInt(1, 5))); 
//        listItem.add(itemAdd);//.... 
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        return listItem;
    }

    @Override
    public void update() {
        super.update();
        if (Client.gI().getPlayers().isEmpty() || this.isDie()) {
            return;
        }
        try {
            Player ramdonPlayer = Client.gI().getPlayers().get(Util.nextInt(Client.gI().getPlayers().size()));
            if (ramdonPlayer != null && ramdonPlayer.isPl() && !ramdonPlayer.isPet && !ramdonPlayer.isMiniPet && !(ramdonPlayer instanceof AnTrom)) {
                if (ramdonPlayer.zone.map.mapId != 51 && ramdonPlayer.zone.map.mapId != 5 && ramdonPlayer.zone.map.mapId != 21 && ramdonPlayer.zone.map.mapId != 22 && ramdonPlayer.zone.map.mapId != 23 && ramdonPlayer.zone.map.mapId != 113 && ramdonPlayer.zone.map.mapId != 129 && ramdonPlayer.zone.map.mapId != 52 && this.zone.getPlayers().size() <= 0 && System.currentTimeMillis() > this.lastTimeJoinMap) {
                    lastTimeJoinMap = System.currentTimeMillis() + TIME_CHANGE_PLAYER;
                    ChangeMapService.gI().changeMap(this, ramdonPlayer.zone, ramdonPlayer.location.x, ramdonPlayer.location.y);
                }
            }
        } catch (Exception e) {
        }
    }

    @Override
    protected boolean useSpecialSkill() {
        return false;
    }

    @Override
    public void rewards(Player pl) {
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
