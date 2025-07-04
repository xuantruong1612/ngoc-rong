//package nro.models.boss.list_boss;
//
//import nro.models.boss.*;
//import nro.models.item.ItemOption;
//import nro.models.map.ItemMap;
//import nro.models.player.Player;
//import nro.services.Service;
//import nro.services.SkillService;
//import nro.utils.Util;
//
///**
// *
// * @author Arriety
// *
// */
//public class bossdan extends FutureBoss {
//
//    public bossdan() {
//        super(BossFactory.BONG_BANG, BossData.bossdan);
//    }
//
//    @Override
//    public double injured(Player plAtt, double damage, boolean piercing, boolean isMobAttack) {
//        if (!this.isDie()) {
//            if (plAtt != null) {
//                if (Util.isTrue(10, 100)) {
//                    ItemMap carot = new ItemMap(zone, 1511, 1, this.location.x, zone.map.yPhysicInTop(this.location.x, 0), -1);
//                    carot.options.add(new ItemOption(30, 0));
//                    carot.options.add(new ItemOption(93, 30));
//                    Service.gI().dropItemMap(this.zone, carot);
//                }
//              if (Util.isTrue(1, 100)) {
//                    ItemMap carot = new ItemMap(zone, 1512, 1, this.location.x, zone.map.yPhysicInTop(this.location.x, 0), -1);
//                    carot.options.add(new ItemOption(30, 0));
//                    carot.options.add(new ItemOption(93, 30));
//                    Service.gI().dropItemMap(this.zone, carot);
//                }
//               if (Util.isTrue(1, 100)) {
//                    ItemMap carot = new ItemMap(zone, 1513, 1, this.location.x, zone.map.yPhysicInTop(this.location.x, 0), -1);
//                    carot.options.add(new ItemOption(30, 0));
//                    carot.options.add(new ItemOption(93, 30));
//                    Service.gI().dropItemMap(this.zone, carot);
//                }
//               if (Util.isTrue(1, 100)) {
//                    ItemMap carot = new ItemMap(zone, 1510, 1, this.location.x, zone.map.yPhysicInTop(this.location.x, 0), -1);
//                    carot.options.add(new ItemOption(30, 0));
//                    carot.options.add(new ItemOption(93, 30));
//                    Service.gI().dropItemMap(this.zone, carot);
//                }  
//                
//                
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
//        int diemsb = 1;
//        pl.point_sb += diemsb;
//        Service.getInstance().sendThongBao(pl, "Bạn Nhận Được " + diemsb + " Điểm Săn Boss");
//        int slcarot = Util.nextInt(1, 2);
//        for (int i = 0; i < slcarot; i++) {
//            ItemMap carot = new ItemMap(zone, 1513, 1, 10 * i + this.location.x, zone.map.yPhysicInTop(this.location.x, 0), -1);
//            carot.options.add(new ItemOption(30, 0));
//            carot.options.add(new ItemOption(93, 30));
//            Service.getInstance().dropItemMap(this.zone, carot);
//        }
//         int slcarot1 = Util.nextInt(1, 2);
//        for (int i = 0; i < slcarot1; i++) {
//            ItemMap carot1 = new ItemMap(zone, 1514, 1, 10 * i + this.location.x, zone.map.yPhysicInTop(this.location.x, 0), -1);
//            carot1.options.add(new ItemOption(30, 0));
//            carot1.options.add(new ItemOption(93, 30));
//            Service.getInstance().dropItemMap(this.zone, carot1);
//        }
//         int slcarot2 = Util.nextInt(1, 2);
//        for (int i = 0; i < slcarot2; i++) {
//            ItemMap carot2 = new ItemMap(zone, 1515, 1, 10 * i + this.location.x, zone.map.yPhysicInTop(this.location.x, 0), -1);
//            carot2.options.add(new ItemOption(30, 0));
//            carot2.options.add(new ItemOption(93, 30));
//            Service.getInstance().dropItemMap(this.zone, carot2);
//        }
//         int slcarot3 = Util.nextInt(1, 2);
//        for (int i = 0; i < slcarot3; i++) {
//            ItemMap carot3 = new ItemMap(zone, 1516, 1, 10 * i + this.location.x, zone.map.yPhysicInTop(this.location.x, 0), -1);
//            carot3.options.add(new ItemOption(30, 0));
//            carot3.options.add(new ItemOption(93, 30));
//            Service.getInstance().dropItemMap(this.zone, carot3);
//        }
//        
//        
//        
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
