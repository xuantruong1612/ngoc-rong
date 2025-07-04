//package nro.models.boss.list_boss.sumo;
//
//import java.util.Calendar;
//
//import nro.models.boss.*;
//import nro.models.player.Player;
//import nro.models.skill.Skill;
//import nro.services.Service;
//import nro.services.TaskService;
//import nro.utils.TimeUtil;
//import nro.utils.Util;
//
//public class Sumo1 extends FutureBoss {
//
//    public Sumo1() {
//        super(BossFactory.SUMO1, BossData.SUMO1);
//    }
//
//    @Override
//    protected boolean useSpecialSkill() {
//        return false;
//    }
//
//    @Override
//    public void rewards(Player pl) {
//        pl.point_sb += 1;
//        TaskService.gI().checkDoneTaskKillBoss(pl, this);
//        generalRewards(pl);
//    }
//
//    @Override
//    public void idle() {
//
//    }
//
//    @Override
//    public void checkPlayerDie(Player pl) {
//
//    }
//
//    @Override
//    public void initTalk() {
//        this.textTalkMidle = new String[]{"Xem bản lĩnh của ngươi như nào đã", "Các ngươi tới số mới gặp phải ta"};
//        this.textTalkAfter = new String[]{"Ác quỷ biến hình, hêy aaa......."};
//    }
//
//    @Override
//    public void leaveMap() {
//        Boss sumo2 = BossFactory.createBoss(BossFactory.SUMO2);
//        sumo2.zone = this.zone;
//        sumo2.location.x = this.location.x;
//        sumo2.location.y = this.location.y;
//        super.leaveMap();
//        this.setJustRestToFuture();
//        BossManager.gI().removeBoss(this);
//    }
//
//    @Override
//    public double injured(Player plAtt, double damage, boolean piercing, boolean isMobAttack) {
//        double dame = 0;
//        if (this.isDie()) {
//            return dame; 
//        } else {
//            if (Util.isTrue(1, 5) && plAtt != null) {
//                switch (plAtt.playerSkill.skillSelect.template.id) {
//                    case Skill.TU_SAT:
//                    case Skill.QUA_CAU_KENH_KHI:
//                    case Skill.MAKANKOSAPPO:
//                        break;
//                    default:
//                        return 0;
//                }
//            }
//            dame = super.injured(plAtt, damage, piercing, isMobAttack);
//            if (this.isDie()) {
//                notifyPlayeKill(plAtt);
//                die();
//                leaveMap();
//            }
//            return dame;
//        }
//    }
//}
