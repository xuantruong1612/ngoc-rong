package Sumo.Boss_list_Fide;
import java.util.Random;
import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.boss.FutureBoss;
import nro.models.item.ItemOption;
import nro.models.map.ItemMap;
import nro.models.player.Player;
import nro.services.Service;
import nro.services.TaskService;
import nro.utils.Util;

/**
 *
 * @Arriety
 *
 */
public class Fide2 extends FutureBoss {

    public Fide2() {
        super(BossFactory.Fide2, BossData.Fide2  );
    }

    
    
    
    @Override
    protected boolean useSpecialSkill() {
        return false;
    }

     @Override
       public void rewards(Player pl) {
          
           int diemsb = 1; //50 sd 
        pl.point_sb += diemsb;
        Service.getInstance().sendThongBao(pl, "Bạn Nhận Được " + diemsb + " điểm boss");
     
       
       
        //*****************************************************************************************************
     
        int[] caitrang = new int[]{1348,1349,1350,1351,1352,1353,1354,1355};
        
         
         
       //*************************************************************************************************************************** 

         int listcaitrang = new Random().nextInt(caitrang.length);
        //****************************************************************list rơi bên dưới****************************************************

      
       if (Util.isTrue(10, 100)) { // tỉ lệ 30 k vv
            Service.getInstance().dropItemMap(this.zone, Util.CAITRANG2(zone, caitrang[listcaitrang], 1,  this.location.x-30, this.location.y, pl.id));
         
        }
       
         int slcarot = Util.nextInt(6, 20);
        for (int i = 0; i < slcarot; i++) {
            ItemMap carot = new ItemMap(zone, 1111, 1, 10 * i + this.location.x, zone.map.yPhysicInTop(this.location.x, 0), -1);
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