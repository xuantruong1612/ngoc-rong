//package nro.models.boss.boss_doanh_trai;
//
//import nro.models.boss.BossData;
//import nro.models.boss.BossFactory;
//import nro.services.func.ChangeMapService;
//import nro.models.map.phoban.DoanhTrai;
//import nro.models.player.Player;
//import nro.services.SkillService;
//import nro.utils.SkillUtil;
//import nro.utils.Util;
//
//import java.util.List;
//
///**
// *
// * Arriety
// *
// */
//public class TrungUyThep extends BossDoanhTrai {
//
//    private boolean activeAttack;
//
//    public TrungUyThep(DoanhTrai doanhTrai) {
//        super(BossFactory.TRUNG_UD_THEP, BossData.TRUNG_UY_THEP, doanhTrai);
//    }
//
//    @Override
//    public void attack() {
//        try {
//            if (activeAttack) {
//                Player pl = getPlayerAttack();
//                this.playerSkill.skillSelect = this.getSkillAttack();
//                if (Util.getDistance(this, pl) <= this.getRangeCanAttackWithSkillSelect()) {
//                    SkillService.gI().useSkill(this, pl, null,null);
//                    checkPlayerDie(pl);
//                } else {
//                    goToPlayer(pl, false);
//                }
//            } else {
//                List<Player> notBosses = this.zone.getNotBosses();
//                for (Player pl : notBosses) {
//                    if (pl.location.x >= 650 && !pl.effectSkin.isVoHinh) {
//                        this.activeAttack = true;
//                        break;
//                    }
//                }
//            }
//        } catch (Exception ex) {
////            ex.printStackTrace();
//        }
//    }
//
//    @Override
//    public void joinMap() {
//        try {
//            this.zone = this.doanhTrai.getMapById(mapJoin[Util.nextInt(0, mapJoin.length - 1)]);
//            ChangeMapService.gI().changeMap(this, this.zone, 900, this.zone.map.yPhysicInTop(900, 100));
//        } catch (Exception e) {
//
//        }
//    }
//
//    @Override
//    public double injured(Player plAtt, double damage, boolean piercing, boolean isMobAttack) {
//        if (plAtt != null && !SkillUtil.isUseSkillDam(plAtt)) {
//            return super.injured(plAtt, damage, piercing, isMobAttack);
//        }
//        damage = damage / 100;
//        if (damage <= 0) {
//            damage = 1;
//        }
//        return super.injured(plAtt, damage, piercing, isMobAttack);
//    }
//
//}
