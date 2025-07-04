package Sumo.Boss_list_Traidat;

import java.util.Random;
import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.boss.FutureBoss;
import nro.models.item.ItemOption;
import nro.models.map.ItemMap;
import nro.models.player.Player;
import nro.services.Service;
import nro.services.SkillService;
import nro.services.TaskService;
import nro.utils.Util;

/**
 *
 * @Arriety
 *
 */
public class Bujin extends FutureBoss {

    public Bujin() {
        super(BossFactory.Bujin, BossData.Bujin);
    }

    @Override
    protected boolean useSpecialSkill() {
        return false;
    }

    
    
    

    @Override
    public void rewards(Player pl) {

        int diemsb = Util.nextInt(1, 2); //50 sd 
        pl.point_sb += diemsb;
         Service.getInstance().sendThongBao(pl, "Bạn Nhận Được " + diemsb + " Điểm Săn Boss");        
        
         int EXPTB = Util.nextInt(1, 100000); //50 sd 
        pl.ExpTamkjll += EXPTB;
         Service.getInstance().sendThongBaoBenDuoi( "Bạn Nhận Được " + EXPTB + " EXP Tiên Bang Từ Săn Boss");     
         
         
       int diemsbTT =  Util.nextInt(1, 20000); //50 sd 
        pl.Tamkjlltutien[0] += diemsbTT;//50 sd 
         Service.getInstance().sendThongBaoBenDuoi( "Bạn Nhận Được " + diemsbTT + " EXP Tu Tiên Săn Boss");
        //*****************************************************************************************************
    

        //*************************************************************************************************************************** 
      
      
     
        //****************************************************************list rơi bên dưới****************************************************

          int[] item = new int[]{1502,1111,1404};//list item
        int[] dohuydiet = new int[]{555,556,557,558,559,560,561};   // list đồ huỷ diệt   // list đồ huỷ diệt
    
       //*************************************************************************************************************************** 
    
        int listdohuydiet = new Random().nextInt(dohuydiet.length);
        int roiitem = new Random().nextInt(item.length);
    
        //****************************************************************list rơi bên dưới****************************************************

        
         if (Util.isTrue(50, 100)) { // tỉ lệ rơi item theo vip player
            Service.getInstance().dropItemMap(this.zone, new ItemMap(zone, item[roiitem], 1, this.location.x,
                    zone.map.yPhysicInTop(this.location.x, this.location.y - 24), pl.id));
        
   
        }
     if (Util.isTrue(50, 100)) { // tỉ lệ 30 k vv
            Service.getInstance().dropItemMap(this.zone, Util.setdotanthutraidathsd(zone, dohuydiet[listdohuydiet], 1,  this.location.x-30, this.location.y, pl.id));
         
     }
       
        
        
        
      int slcarot = Util.nextInt(6, 20);
        for (int i = 0; i < slcarot; i++) {
            ItemMap carot = new ItemMap(zone, 77, 100, 10 * i + this.location.x, zone.map.yPhysicInTop(this.location.x, 0), -1);
            carot.options.add(new ItemOption(31, 0));
            carot.options.add(new ItemOption(72,  Util.nextInt(1, 16))); //50 sd 
            Service.getInstance().dropItemMap(this.zone, carot);
        }
        TaskService.gI().checkDoneTaskKillBoss(pl, this);
        generalRewards(pl);
    }

    @Override
    public void idle() {

    }

    @Override
    public void checkPlayerDie(Player pl) {

    }

    @Override
    public void initTalk() {
        textTalkMidle = new String[]{"Ta chính là đệ nhất vũ trụ cao thủ"};
        textTalkAfter = new String[]{"Ác quỷ biến hình aaa..."};
    }

}

