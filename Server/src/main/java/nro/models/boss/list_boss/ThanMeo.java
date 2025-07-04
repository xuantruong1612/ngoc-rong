package nro.models.boss.list_boss;

import nro.models.boss.BossData;
import nro.models.boss.BossManager;
import nro.models.boss.FutureBoss;
import nro.models.map.Zone;
import nro.models.player.Player;
import nro.services.Service;
import nro.services.func.ChangeMapService;
import nro.utils.Util;

/**
 *
 * Arriety
 *
 */
public class ThanMeo extends FutureBoss {

    public int idNpc;

    public ThanMeo(byte bossID, BossData bossData, Zone zone, int x, int y, int IDNPC) throws Exception {
        super(bossID, bossData);
        this.zone = zone;
        this.location.x = x;
        this.location.y = y;
        this.idNpc = IDNPC;
    }

    @Override
    public void attack() {
        super.attack();
        if (plAttack == null) {
            Service.getInstance().sendEffectHideNPC(plAttack, (byte) idNpc, (byte) 1);
            ChangeMapService.gI().exitMap(this);
            synchronized (this) {
                BossManager.gI().removeBoss(this);
            }
            this.dispose();
        }
    }

    @Override
    public void idle() {
    }

    @Override
    public void checkPlayerDie(Player pl) {
    }

    @Override
    public void joinMap() {
        if (this.zone != null) {
            ChangeMapService.gI().changeMap(this, this.zone, 400, 408);
        }
    }

    @Override
    public void initTalk() {
        this.textTalkBefore = new String[]{};
        this.textTalkMidle = new String[]{"Tất cả nhào vô", "Mình ta cũng đủ để hủy diệt các ngươi"};
        this.textTalkAfter = new String[]{};
    }

    @Override
    public void leaveMap() {
        super.leaveMap();
        synchronized (this) {
            BossManager.gI().removeBoss(this);
        }
        this.dispose();
    }

    @Override
    protected boolean useSpecialSkill() {
        return false;
    }

    @Override
    public double injured(Player plAtt, double damage, boolean piercing, boolean isMobAttack) {
        this.chat("|7|Tổn thương: " + Util.format(damage));
        return damage;
    }

    @Override
    public void rewards(Player pl) {
        int diemsb = 1;
        pl.point_sb += diemsb;
        Service.getInstance().sendThongBao(pl, "Bạn Nhận Được " + diemsb + " Điểm Săn Boss");
    }

}
