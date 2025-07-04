package Sumo.Boss_list_ThanThu;

import java.util.Random;
import nro.models.boss.Boss;
import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.boss.FutureBoss;
import nro.models.item.ItemOption;
import nro.models.map.ItemMap;
import nro.models.player.Player;
import nro.services.ChatGlobalService;
import nro.services.PlayerService;
import nro.services.RewardService;
import nro.services.Service;
import nro.services.TaskService;
import nro.utils.Util;

/**
 *
 * @Arriety
 *
 */
public class HaoThienKhuyen extends FutureBoss {

    public HaoThienKhuyen() {
        super(BossFactory.HaoThienKhuyen, BossData.HaoThienKhuyen  );
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
     
        int[] SKHTHIENSU = new int[]{1048,1049,1050,1051,1052,1053,1054,1055,1056,1057,1058,1059,1060};
        
         
         
       //*************************************************************************************************************************** 

          int LISTTHIENSU = new Random().nextInt(SKHTHIENSU.length);
        //****************************************************************list rơi bên dưới****************************************************

      
       if (Util.isTrue(100, 100)) { // thuận theo tỉ lệ vip và rơi 100% vĩnh nếu vip cao cóa tỉ lệ rơi cao
           Service.getInstance().dropItemMap(this.zone, Util.SKHTHIENSU(zone, SKHTHIENSU[LISTTHIENSU], 1,  this.location.x-40, this.location.y, pl.id));
         
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