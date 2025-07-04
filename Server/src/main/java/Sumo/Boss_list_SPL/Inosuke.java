package Sumo.Boss_list_SPL;

import java.util.Random;
import nro.models.boss.Boss;
import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.boss.FutureBoss;
import nro.models.item.ItemOption;
import nro.models.map.ItemMap;
import nro.models.player.Player;
import nro.services.ChatGlobalService;
import nro.services.RewardService;
import nro.services.Service;
import nro.services.SkillService;
import nro.services.TaskService;
import nro.utils.Util;

/**
 *
 * @Arriety
 *
 */
public class Inosuke extends FutureBoss {

    public Inosuke() {
        super(BossFactory.Inosuke, BossData.Inosuke);
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
        
       int diemsbTT =  Util.nextInt(1, 20000); //50 sd 
        pl.Tamkjlltutien[0] += diemsbTT;//50 sd 
         Service.getInstance().sendThongBaoBenDuoi( "Bạn Nhận Được " + diemsbTT + " EXP Tu Tiên Săn Boss");
        //*****************************************************************************************************
        int[] ramdomroiitem = new int[]{822};//list item
      

        //*************************************************************************************************************************** 
      
        int ramdomitem = new Random().nextInt(ramdomroiitem.length);
     
        //****************************************************************list rơi bên dưới****************************************************

        
        
         if (Util.isTrue(7, 100)) { // tỉ lệ 30 k vv
      int slcarot = Util.nextInt(1, 6);
        for (int i = 0; i < slcarot; i++) {
            ItemMap carot = new ItemMap(zone, 1522, 1, 10 * i + this.location.x, zone.map.yPhysicInTop(this.location.x, 0), -1);
             carot.options.add(new ItemOption(50,  Util.nextInt(40, 50)));
            carot.options.add(new ItemOption(31, 0));
            Service.getInstance().dropItemMap(this.zone, carot);
        }
        
         }
      if (Util.isTrue(20, 100)) { // tỉ lệ 30 k vv
      int slcarot1 = Util.nextInt(1, 6);
        for (int i = 0; i < slcarot1; i++) {
            ItemMap carot = new ItemMap(zone, 1523, 1, 10 * i + this.location.x, zone.map.yPhysicInTop(this.location.x, 0), -1);
            carot.options.add(new ItemOption(77, Util.nextInt(40, 50)));
            carot.options.add(new ItemOption(31, 0));
            Service.getInstance().dropItemMap(this.zone, carot);
        }  
        
       }
      if (Util.isTrue(50, 100)) { // tỉ lệ 30 k vv
      int slcarot2 = Util.nextInt(1, 6);
        for (int i = 0; i < slcarot2; i++) {
            ItemMap carot = new ItemMap(zone, 1524, 1, 10 * i + this.location.x, zone.map.yPhysicInTop(this.location.x, 0), -1);
            carot.options.add(new ItemOption(103, Util.nextInt(40, 50)));
            carot.options.add(new ItemOption(31, 0));
            Service.getInstance().dropItemMap(this.zone, carot);
        }     
        
        }
      if (Util.isTrue(25, 100)) { // tỉ lệ 30 k vv
      int slcarot3 = Util.nextInt(1, 6);
        for (int i = 0; i < slcarot3; i++) {
            ItemMap carot = new ItemMap(zone, 1525, 1, 10 * i + this.location.x, zone.map.yPhysicInTop(this.location.x, 0), -1);
            carot.options.add(new ItemOption(101, Util.nextInt(40, 50)));
            carot.options.add(new ItemOption(31, 0));
            Service.getInstance().dropItemMap(this.zone, carot);
        }    
        
        
        
        
        
        
        TaskService.gI().checkDoneTaskKillBoss(pl, this);
        generalRewards(pl);
    }
         
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
