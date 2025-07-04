//package nro.models.boss.broly;
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
//public class Broly extends FutureBoss {
//
//    public Broly() {
//        super(BossFactory.BROLY, BossData.BROLY);
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
//        Boss sumo1 = BossFactory.createBoss(BossFactory.Superbroly);
//        sumo1.zone = this.zone;
//        sumo1.location.x = this.location.x;
//        sumo1.location.y = this.location.y;
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
//                     case Skill.ANTOMIC:   
//                     case Skill.KAMEJOKO: 
//                        case Skill.MASENKO: 
//                       case Skill.DICH_CHUYEN_TUC_THOI:  
//                         
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
//
//}
