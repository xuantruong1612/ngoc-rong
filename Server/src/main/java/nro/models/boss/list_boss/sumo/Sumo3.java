//package nro.models.boss.list_boss.sumo;
//
//import java.util.Calendar;
//import nro.models.boss.*;
//import nro.models.map.ItemMap;
//import nro.models.player.Player;
//import nro.models.skill.Skill;
//import nro.server.ServerNotify;
//import nro.services.RewardService;
//import nro.services.Service;
//import nro.services.TaskService;
//import nro.services.func.ChangeMapService;
//import nro.utils.TimeUtil;
//import nro.utils.Util;
//
//public class Sumo3 extends FutureBoss {
//
//    public Sumo3() {
//        super(BossFactory.SUMO3, BossData.SUMO3);
//    }
//
//    @Override
//    protected boolean useSpecialSkill() {
//        return false;
//    }
//
//    @Override
//    public void rewards(Player pl) {
//        pl.point_sb += 2;
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
//
//    }
//
//    @Override
//    public void leaveMap() {
//        BossFactory.createBoss(BossFactory.SUMO).setJustRest();
//        super.leaveMap();
//        BossManager.gI().removeBoss(this);
//    }
//
//    @Override
//    public void joinMap() {
//        if (this.zone != null) {
//            ChangeMapService.gI().changeMap(this, zone, this.location.x, this.location.y);
//            ServerNotify.gI().notify("Boss " + this.name + " vừa xuất hiện tại " + this.zone.map.mapName);
//        }
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
//            }
//            return dame;
//        }
//    }
//}
