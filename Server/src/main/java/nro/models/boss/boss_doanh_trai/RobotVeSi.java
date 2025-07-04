//package nro.models.boss.boss_doanh_trai;
//
//import nro.consts.ConstRatio;
//import nro.models.boss.BossData;
//import nro.services.func.ChangeMapService;
//import nro.models.map.phoban.DoanhTrai;
//import nro.models.player.Player;
//import nro.services.SkillService;
//import nro.utils.Util;
//
///**
// *
// * Arriety
// *
// */
//public class RobotVeSi extends BossDoanhTrai {
//
//    public RobotVeSi(byte id, DoanhTrai doanhTrai) {
//        super(id, BossData.ROBOT_VE_SI, doanhTrai);
//    }
//
//    @Override
//    public void attack() {
//        try {
//            if (!useSpecialSkill()) {
//                Player pl = getPlayerAttack();
//                this.playerSkill.skillSelect = this.getSkillAttack();
//                if (Util.getDistance(this, pl) <= this.getRangeCanAttackWithSkillSelect()) {
//                    if (Util.isTrue(20, ConstRatio.PER100)) {
//                        goToXY(pl.location.x + Util.nextInt(-20, 20), Util.nextInt(pl.location.y - 80,
//                                this.zone.map.yPhysicInTop(pl.location.x, 0)), false);
//                    }
//                    SkillService.gI().useSkill(this, pl, null,null);
//                    checkPlayerDie(pl);
//                } else {
//                    goToPlayer(pl, false);
//                }
//            }
//        } catch (Exception e) {
//        }
//    }
//
//    @Override
//    public void joinMap() {
//        try {
//            this.zone = this.doanhTrai.getMapById(mapJoin[Util.nextInt(0, mapJoin.length - 1)]);
//            int x = Util.nextInt(50, this.zone.map.mapWidth - 50);
//            ChangeMapService.gI().changeMap(this, this.zone, x, this.zone.map.yPhysicInTop(x, 100));
//        } catch (Exception e) {
//
//        }
//    }
//
//}
