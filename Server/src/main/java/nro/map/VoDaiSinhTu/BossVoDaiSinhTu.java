package nro.map.VoDaiSinhTu;

import nro.consts.ConstRatio;
import nro.models.boss.Boss;
import nro.models.boss.BossData;
import nro.models.boss.BossManager;

import nro.models.player.Player;
import nro.services.MapService;
import nro.services.PlayerService;
import nro.services.SkillService;
import nro.services.func.ChangeMapService;
import nro.utils.Log;
import nro.utils.SkillUtil;
import nro.utils.Util;

public abstract class BossVoDaiSinhTu extends Boss {

    protected Player playerAtt;

    public BossVoDaiSinhTu(byte id, BossData data) {
        super(id, data);
        this.status = 1;
    }

    @Override
    protected boolean useSpecialSkill() {
        return false;
    }

    @Override
    public void rewards(Player pl) {

    }

    @Override
    public void initTalk() {

    }

    @Override
    public void idle() {

    }

    @Override
    public void checkPlayerDie(Player pl) {

    }

    protected void goToXY(int x, int y, boolean isTeleport) {
        if (!isTeleport) {
            byte dir = (byte) (this.location.x - x < 0 ? 1 : -1);
            byte move = (byte) Util.nextInt(50, 100);
            PlayerService.gI().playerMove(this, this.location.x + (dir == 1 ? move : -move), y);
        } else {
            ChangeMapService.gI().changeMapYardrat(this, this.zone, x, y);
        }
    }

    @Override
    public void attack() {
        try {
            if (playerAtt != null && playerAtt.zone != null && this.zone != null && this.zone.equals(playerAtt.zone)) {
                if (this.isDie()) {
                    return;
                }
                this.playerSkill.skillSelect = this.getSkillAttack();
                if (Util.getDistance(this, playerAtt) <= this.getRangeCanAttackWithSkillSelect()) {
                    if (Util.isTrue(15, ConstRatio.PER100) && SkillUtil.isUseSkillChuong(this)) {
                        goToXY(playerAtt.location.x + (Util.getOne(-1, 1) * Util.nextInt(20, 80)), Util.nextInt(10) % 2 == 0 ? playerAtt.location.y : playerAtt.location.y - Util.nextInt(0, 50), false);
                    }
                    SkillService.gI().useSkill(this, playerAtt, null,null);
                    checkPlayerDie(playerAtt);
                } else {
                    goToPlayer(playerAtt, false);
                }
            } else {
                this.leaveMap();
            }
        } catch (Exception ex) {
            Log.error(BossDHVT.class, ex);
        }
    }

    @Override
    public void changeToAttack() {

    }

    @Override
    public void joinMap() {
        if (playerAtt.zone != null) {
            this.zone = playerAtt.zone;
            ChangeMapService.gI().changeMap(this, this.zone, 545, 336);
        }
    }

    private void immortalMp() {
        this.nPoint.mp = this.nPoint.mpg;
    }

    @Override
    public void update() {
        super.update();
        try {
            if (this.effectSkill != null && !this.effectSkill.isHaveEffectSkill()
                    && !this.effectSkill.isCharging) {
                this.immortalMp();
                switch (this.status) {
                    case JUST_JOIN_MAP -> {
                        joinMap();
                        if (this.zone != null) {
                            changeStatus(ATTACK);
                        }
                    }
                    case ATTACK -> {
                        this.talk();
                        if (this.playerSkill.prepareTuSat || this.playerSkill.prepareLaze
                                || this.playerSkill.prepareQCKK) {
                            break;
                        } else {
                            this.attack();
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.error(BossVoDaiSinhTu.class, e);
        }
    }

    @Override
    protected void notifyPlayeKill(Player player) {

    }

    @Override
    public void leaveMap() {
        MapService.gI().exitMap(this);
        BossManager.gI().removeBoss(this);
        this.canUpdate = false;
    }
}
