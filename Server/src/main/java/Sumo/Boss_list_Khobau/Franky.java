package Sumo.Boss_list_Khobau;

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
public class Franky extends FutureBoss {

    public Franky() {
        super(BossFactory.Franky, BossData.Franky);
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
      
       int slcarot1 = Util.nextInt(1, 8);
        for (int i = 0; i < slcarot1; i++) {
            ItemMap carot1 = new ItemMap(zone, Util.nextInt(618, 626), 1, 10 * i + this.location.x, zone.map.yPhysicInTop(this.location.x, 0), -1);
             carot1.options.add(new ItemOption(50,  Util.nextInt(1, 300))); //50 sd 
            carot1.options.add(new ItemOption(77,  Util.nextInt(1, 300))); //50 sd 
             carot1.options.add(new ItemOption(103,  Util.nextInt(1, 300))); //50 sd 
              carot1.options.add(new ItemOption(78,  Util.nextInt(1, 100))); //50 sd 
               carot1.options.add(new ItemOption(101,  Util.nextInt(1, 100))); //50 sd 
                carot1.options.add(new ItemOption(31,  Util.nextInt(1, 16))); //50 sd 
            Service.getInstance().dropItemMap(this.zone, carot1);
        }  
     
        //****************************************************************list rơi bên dưới****************************************************

      int slcarot = Util.nextInt(6, 30);
        for (int i = 0; i < slcarot; i++) {
            ItemMap carot = new ItemMap(zone, 999, 1, 10 * i + this.location.x, zone.map.yPhysicInTop(this.location.x, 0), -1);
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

