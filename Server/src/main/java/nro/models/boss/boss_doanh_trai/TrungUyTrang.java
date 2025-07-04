//package nro.models.boss.boss_doanh_trai;
//
//import nro.models.boss.BossData;
//import nro.models.boss.BossFactory;
//import nro.services.func.ChangeMapService;
//import nro.consts.ConstMob;
//import nro.consts.ConstRatio;
//import nro.models.mob.Mob;
//import nro.models.map.phoban.DoanhTrai;
//import nro.models.player.Player;
//import nro.services.SkillService;
//import nro.utils.Util;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// *
// * Arriety
// *
// */
//public class TrungUyTrang extends BossDoanhTrai {
//
//    public TrungUyTrang(DoanhTrai doanhTrai) {
//        super(BossFactory.TRUNG_UY_TRANG, BossData.TRUNG_UY_TRANG, doanhTrai);
//    }
//
//    @Override
//    public Player getPlayerAttack() {
//        List<Player> list = new ArrayList<>();
//
//        List<Player> notBosses = this.zone.getNotBosses();
//        for (Player pl : notBosses) {
//            if (!pl.isDie() && pl.location.x >= 755 && pl.location.x <= 1069 && !pl.effectSkin.isVoHinh) {
//                list.add(pl);
//            }
//        }
//        if (!list.isEmpty()) {
//            return list.get(Util.nextInt(0, list.size() - 1));
////        } else {
////            throw new Exception();
//        }
//        return null;
//    }
//
//
//    @Override
//    public void attack() {
//        try {
//            Player pl = getPlayerAttack();
//            this.playerSkill.skillSelect = this.getSkillAttack();
//            if (Util.getDistance(this, pl) <= this.getRangeCanAttackWithSkillSelect()) {
//                if (Util.isTrue(20, ConstRatio.PER100)) {
//                    goToXY(pl.location.x + Util.nextInt(-20, 20), pl.location.y, false);
//                }
//                SkillService.gI().useSkill(this, pl, null,null);
//                checkPlayerDie(pl);
//            } else {
//                goToPlayer(pl, false);
//            }
//        } catch (Exception ex) {
////            ex.printStackTrace();
//        }
//    }
//
//    @Override
//    public double injured(Player plAtt, double damage, boolean piercing, boolean isMobAttack) {
//        if (!piercing) {
//            boolean haveBulon = false;
//            for (Mob mob : this.zone.mobs) {
//                if (mob.tempId == ConstMob.BULON && !mob.isDie()) {
//                    haveBulon = true;
//                    break;
//                }
//            }
//            if (haveBulon) {
//                damage = 1;
//            }
//        }
//        return super.injured(plAtt, damage, piercing, isMobAttack);
//    }
//
//    @Override
//    public void joinMap() {
//        try {
//            this.zone = this.doanhTrai.getMapById(mapJoin[Util.nextInt(0, mapJoin.length - 1)]);
//            int x = Util.nextInt(755, 1069);
//            ChangeMapService.gI().changeMap(this, this.zone, x, this.zone.map.yPhysicInTop(x, 0));
//            System.out.println("boss jion map");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    protected boolean useSpecialSkill() {
//        return false;
//    }
//
//}
