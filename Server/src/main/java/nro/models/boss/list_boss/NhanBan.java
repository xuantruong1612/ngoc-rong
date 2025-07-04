package nro.models.boss.list_boss;

import nro.models.boss.Boss;
import nro.models.boss.BossData;
import nro.models.boss.BossManager;
import nro.models.item.ItemOption;
import nro.models.map.ItemMap;
import nro.models.player.Player;
import nro.services.Service;
import nro.utils.Util;

public class NhanBan extends Boss {

    Player plAtt;

    public NhanBan(int bossID, BossData bossData, Player plAtt) throws Exception {
        super(bossID, bossData);
        this.plAtt = plAtt;
    }

    @Override
    public void rewards(Player pl) {
        if (Util.isTrue(10, 100)) {
            int[] ramdomroiitem = new int[]{1512, 1227};//list item
            ItemMap itemMap = new ItemMap(zone, ramdomroiitem[Util.nextInt(ramdomroiitem.length - 1)], 1, this.location.x,
                    zone.map.yPhysicInTop(this.location.x, this.location.y - 24), pl.id);
            itemMap.options.add(new ItemOption(30, 1));
            Service.getInstance().dropItemMap(this.zone, itemMap);
        }
        this.leaveMap();
        BossManager.gI().removeBoss(this);
    }

    @Override
    public void update() {
        super.update();
        if (this.isDie() || this.nPoint.hp <= 0) {
            this.rewards(plAttack);
        }
        if (plAtt.isDie()) {
            this.leaveMap();
            BossManager.gI().removeBoss(this);
        }
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
        this.textTalkAfter = new String[]{"|-2|Boss nhân bản đã xuất hiện rồi"}; //text chat 1
        this.textTalkBefore = new String[]{"|-1|Ta sẽ chiếm lấy thân xác của ngươi hahaha!"}; //text chat 2
        this.textTalkMidle = new String[]{"|-1|Lần khác ta sẽ xử đẹp ngươi"};

    }
}
