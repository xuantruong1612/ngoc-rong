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
//public class PETBACHKIM extends FutureBoss {
//
//    public PETBACHKIM() {
//        super(BossFactory.BONG_BANG, BossData.PETBACHKIM);
//    }
//
//    @Override
//    public double injured(Player plAtt, double damage, boolean piercing, boolean isMobAttack) {
//        if (!this.isDie()) {
//            if (plAtt != null) {
//                if (Util.isTrue(20, 1000)) {
//                    ItemMap carot = new ItemMap(zone, 650, 1, this.location.x, zone.map.yPhysicInTop(this.location.x, 0), -1);
//                    carot.options.add(new ItemOption(47, Util.nextInt(1, 50000))); 
//                    carot.options.add(new ItemOption(93, Util.nextInt(1, 3))); 
//                    Service.gI().dropItemMap(this.zone, carot);
//                }
//                 if (Util.isTrue(20, 1000)) {
//                    ItemMap carot = new ItemMap(zone, 651, 1, this.location.x, zone.map.yPhysicInTop(this.location.x, 0), -1);
//                    carot.options.add(new ItemOption(22, Util.nextInt(1, 200))); 
//                    carot.options.add(new ItemOption(93, Util.nextInt(1, 3))); 
//                    Service.gI().dropItemMap(this.zone, carot);
//                } 
//               if (Util.isTrue(20, 1000)) {
//                    ItemMap carot = new ItemMap(zone, 652, 1, this.location.x, zone.map.yPhysicInTop(this.location.x, 0), -1);
//                    carot.options.add(new ItemOption(47, Util.nextInt(1, 5000))); 
//                    carot.options.add(new ItemOption(93, Util.nextInt(1, 3))); 
//                    Service.gI().dropItemMap(this.zone, carot);
//                }   
//                 if (Util.isTrue(20, 1000)) {
//                    ItemMap carot = new ItemMap(zone, 653, 1, this.location.x, zone.map.yPhysicInTop(this.location.x, 0), -1);
//                    carot.options.add(new ItemOption(22, Util.nextInt(1, 200))); 
//                    carot.options.add(new ItemOption(93, Util.nextInt(1, 3))); 
//                    Service.gI().dropItemMap(this.zone, carot);
//                }    
//                  if (Util.isTrue(20, 1000)) {
//                    ItemMap carot = new ItemMap(zone, 654, 1, this.location.x, zone.map.yPhysicInTop(this.location.x, 0), -1);
//                    carot.options.add(new ItemOption(47, Util.nextInt(1, 5000))); 
//                    carot.options.add(new ItemOption(93, Util.nextInt(1, 3))); 
//                    Service.gI().dropItemMap(this.zone, carot);
//                }   
//                 if (Util.isTrue(20, 1000)) {
//                    ItemMap carot = new ItemMap(zone, 655, 1, this.location.x, zone.map.yPhysicInTop(this.location.x, 0), -1);
//                    carot.options.add(new ItemOption(22, Util.nextInt(1, 200))); 
//                    carot.options.add(new ItemOption(93, Util.nextInt(1, 3))); 
//                    Service.gI().dropItemMap(this.zone, carot);
//                }    
//                  if (Util.isTrue(20, 1000)) {
//                    ItemMap carot = new ItemMap(zone, 656, 1, this.location.x, zone.map.yPhysicInTop(this.location.x, 0), -1);
//                    carot.options.add(new ItemOption(14, Util.nextInt(1, 20))); 
//                    carot.options.add(new ItemOption(93, Util.nextInt(1, 3))); 
//                    Service.gI().dropItemMap(this.zone, carot);
//                }   
//                 if (Util.isTrue(20, 1000)) {
//                    ItemMap carot = new ItemMap(zone, 657, 1, this.location.x, zone.map.yPhysicInTop(this.location.x, 0), -1);
//                    carot.options.add(new ItemOption(0, Util.nextInt(1, 100000))); 
//                    carot.options.add(new ItemOption(93, Util.nextInt(1, 2))); 
//                    Service.gI().dropItemMap(this.zone, carot);
//                }     
//                   if (Util.isTrue(20, 1000)) {
//                    ItemMap carot = new ItemMap(zone, 658, 1, this.location.x, zone.map.yPhysicInTop(this.location.x, 0), -1);
//                    carot.options.add(new ItemOption(23, Util.nextInt(1, 500))); 
//                    carot.options.add(new ItemOption(93, Util.nextInt(1, 2))); 
//                    Service.gI().dropItemMap(this.zone, carot);
//                }   
//                  if (Util.isTrue(20, 1000)) {
//                    ItemMap carot = new ItemMap(zone, 659, 1, this.location.x, zone.map.yPhysicInTop(this.location.x, 0), -1);
//                    carot.options.add(new ItemOption(0, Util.nextInt(1, 100000))); 
//                    carot.options.add(new ItemOption(93, Util.nextInt(1, 3))); 
//                    Service.gI().dropItemMap(this.zone, carot);
//                }    
//                   if (Util.isTrue(20, 1000)) {
//                    ItemMap carot = new ItemMap(zone, 660, 1, this.location.x, zone.map.yPhysicInTop(this.location.x, 0), -1);
//                    carot.options.add(new ItemOption(23, Util.nextInt(1, 500))); 
//                    carot.options.add(new ItemOption(93, Util.nextInt(1, 3))); 
//                    Service.gI().dropItemMap(this.zone, carot);
//                }   
//                  if (Util.isTrue(20, 1000)) {
//                    ItemMap carot = new ItemMap(zone, 661, 1, this.location.x, zone.map.yPhysicInTop(this.location.x, 0), -1);
//                    carot.options.add(new ItemOption(0, Util.nextInt(1, 100000))); 
//                    carot.options.add(new ItemOption(93, Util.nextInt(1, 3))); 
//                    Service.gI().dropItemMap(this.zone, carot);
//                }   
//                if (Util.isTrue(20, 1000)) {
//                    ItemMap carot = new ItemMap(zone, 662, 1, this.location.x, zone.map.yPhysicInTop(this.location.x, 0), -1);
//                    carot.options.add(new ItemOption(23, Util.nextInt(1, 500))); 
//                    carot.options.add(new ItemOption(93, Util.nextInt(1, 2))); 
//                    Service.gI().dropItemMap(this.zone, carot);
//                }       
//                  
//            }
//            
//            
//            
//            
//            
//            
//            this.playerSkill.skillSelect = this.playerSkill.skills.get(Util.nextInt(0, this.playerSkill.skills.size() - 1));
//            SkillService.gI().useSkill(this, plAtt, null, null);
//        } else {
//            return 0;
//        }
//        return super.injured(plAtt, 9999999, piercing, isMobAttack);
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
//        int slcarot = Util.nextInt(10, 20);
//        for (int i = 0; i < slcarot; i++) {
//            ItemMap carot = new ItemMap(zone, 457, 1, 10 * i + this.location.x, zone.map.yPhysicInTop(this.location.x, 0), -1);
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
