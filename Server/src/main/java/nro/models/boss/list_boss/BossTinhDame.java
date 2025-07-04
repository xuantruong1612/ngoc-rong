//package nro.models.boss.list_boss;
//
//import nro.models.boss.*;
//import nro.models.item.ItemOption;
//import nro.models.map.ItemMap;
//import nro.models.player.Player;
//import nro.server.Manager;
//import nro.services.Service;
//import nro.services.SkillService;
//import nro.utils.Util;
//
///**
// *
// * @author Arriety
// *
// */
//public class BossTinhDame extends Boss {
//
//    
//    public BossTinhDame() {
//        super(99999999, BossData.THODAIKA);
//    }
//
//    @Override
//    public double injured(Player plAtt, double damage, boolean piercing, boolean isMobAttack) {
//        if (!this.isDie()) {
//            if (plAtt != null) {
//                if (Util.isTrue(50, 100)) {
//                    ItemMap carot = new ItemMap(zone, 462, 1, this.location.x, zone.map.yPhysicInTop(this.location.x, 0), -1);
//                    carot.options.add(new ItemOption(30, 0));
//                    carot.options.add(new ItemOption(93, 30));
//                    Service.gI().dropItemMap(this.zone, carot);
//                }
//            }
//            this.playerSkill.skillSelect = this.playerSkill.skills.get(Util.nextInt(0, this.playerSkill.skills.size() - 1));
//            SkillService.gI().useSkill(this, plAtt, null, null);
//        } else {
//            return 0;
//        }
//        return super.injured(plAtt, 5000, piercing, isMobAttack);
//    }
//
//    @Override
//    protected boolean useSpecialSkill() {
//        return false;
//    }
//
//    @Override
//    public void rewards(Player pl) {
//        int diemsb = 10;
//        pl.point_sb += diemsb;
//        Service.getInstance().sendThongBao(pl, "Bạn Nhận Được " + diemsb + " Điểm Săn Boss");
//        int slcarot = Util.nextInt(10, 20);
//        for (int i = 0; i < slcarot; i++) {
//            ItemMap carot = new ItemMap(zone, 574, 1, 10 * i + this.location.x, zone.map.yPhysicInTop(this.location.x, 0), -1);
//            carot.options.add(new ItemOption(30, 0));
//            carot.options.add(new ItemOption(93, 30));
//            Service.getInstance().dropItemMap(this.zone, carot);
//        }
//    }
//
//    @Override
//    public void idle() {
//    }
//
//    @Override
//    public void checkPlayerDie(Player pl) {
//
//    }
//
//    @Override
//    public void initTalk() {
//    }
//}
