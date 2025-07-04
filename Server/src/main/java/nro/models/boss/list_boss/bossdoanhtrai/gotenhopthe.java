//package nro.models.boss.list_boss.bossdoanhtrai;
//
//import java.util.Random;
//import nro.models.boss.Boss;
//import nro.models.boss.BossData;
//import nro.models.boss.BossFactory;
//import nro.models.boss.FutureBoss;
//import nro.models.item.ItemOption;
//import nro.models.map.ItemMap;
//import nro.models.player.Player;
//import nro.services.ChatGlobalService;
//import nro.services.RewardService;
//import nro.services.Service;
//import nro.utils.Util;
//
///**
// *
// * @Arriety
// *
// */
//public class gotenhopthe extends FutureBoss {
//
//    public gotenhopthe() {
//        super(BossFactory.BONG_BANG, BossData.ANDROID_13);
//    }
//
//    @Override
//    protected boolean useSpecialSkill() {
//        return false;
//    }
//
//     @Override
//       public void rewards(Player pl) {
//           int expgiacngo = Util.nextInt(1, 15000); //50 sd 
//        pl.ExpTamkjll += expgiacngo;
//        Service.getInstance().sendThongBao(pl, "chúc mừng Bạn Nhận Được " + expgiacngo + " EXP Giác Ngộ");
//          ChatGlobalService.gI().chat(pl, "Chúc Mừng Bạn "+ pl.name + " Nhận Được " +  expgiacngo + " EXP Giác Ngộ Từ săn Boss " + this.name +  " Rất Ngưỡng mộ");
//       
//           int diemsb = Util.nextInt(1, 25); //50 sd 
//        pl.point_sb += diemsb;
//        Service.getInstance().sendThongBao(pl, "Bạn Nhận Được " + diemsb + " điểm boss");
//          ChatGlobalService.gI().chat(pl, "Chúc Mừng Bạn "+ pl.name + " Nhận Được " +  diemsb + " Điểm Boss Từ săn Boss  " + this.name +  " Rất Ngưỡng mộ");
//        
//        
//        
//        //*****************************************************************************************************
//      
//        int[] ramdomroiitem = new int[]{1574};//list item
//        int[] caitrang = new int[]{1463};   // list cải trang
//        int[] huydiet = new int[]{650, 651, 652, 653, 654, 655, 656, 657, 658, 659, 660, 661, 662};   // list đồ huỷ diệt
//        int[] thiênsu = new int[]{1048,1049,1050,1051,1052,1053,1054,1055,1056,1057,1058,1059,1060,1061,1062};   // list đồ thiên sứ
//        
//       //*************************************************************************************************************************** 
//        int caitrangvip = new Random().nextInt(caitrang.length);
//        int randomDohd = new Random().nextInt(huydiet.length);
//        int ramdomitem = new Random().nextInt(ramdomroiitem.length);
//         int dothiensu = new Random().nextInt(thiênsu.length);
//        //****************************************************************list rơi bên dưới****************************************************
//
//      if (Util.isTrue(100, 100)) {
//           for(int i=0;i<pl.zone.map.mapWidth;i+=10) Service.getInstance().dropItemMap(this.zone,Util.khongthegiaodich(zone, ramdomroiitem[ramdomitem],1, i, this.location.y, pl.id));
//      
//     }    
//           
//      
//    }
//
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
//        textTalkMidle = new String[]{"Ta chính là đệ nhất vũ trụ cao thủ"};
//        textTalkAfter = new String[]{"Ác quỷ biến hình aaa..."};
//    }
//
//  
//  
//
//}
