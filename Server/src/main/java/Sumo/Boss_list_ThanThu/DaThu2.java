package Sumo.Boss_list_ThanThu;
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
public class DaThu2 extends FutureBoss {

    public DaThu2() {
        super(BossFactory.DaThu2, BossData.DaThu2  );
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
     
        int[] PET = new int[]{1727,1728,1729,1730,1731,1732,1733,1734,1735,1736,1737,1738,1739,1740,1741,1723,1598,1580,1581,1478,1479,1480,1481,1483,1917,1954,1955};
        
         
         
       //*************************************************************************************************************************** 

         int LISTPET = new Random().nextInt(PET.length);
        //****************************************************************list rơi bên dưới****************************************************

      
       if (Util.isTrue(10, 100)) { // tỉ lệ 30 k vv
            Service.getInstance().dropItemMap(this.zone, Util.dathu2(zone, PET[LISTPET], 1,  this.location.x-30, this.location.y, pl.id));
         
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