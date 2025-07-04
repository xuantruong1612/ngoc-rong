package Sumo.Boss_list_Tainguyen;

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
public class danangcap extends FutureBoss {

    public danangcap() {
        super(BossFactory.danangcap, BossData.danangcap);
    }

    @Override
    protected boolean useSpecialSkill() {
        return false;
    }

    
     @Override
    public double injured(Player plAtt, double damage, boolean piercing, boolean isMobAttack) {
        if (!this.isDie()) {
            if (plAtt != null) {
                if (Util.isTrue(10, 100)) {
                    ItemMap carot = new ItemMap(zone, 1111, 1, this.location.x, zone.map.yPhysicInTop(this.location.x, 0), -1);
                    carot.options.add(new ItemOption(30, 0));
                    carot.options.add(new ItemOption(93, 30));
                    Service.gI().dropItemMap(this.zone, carot);
                }
            }
            this.playerSkill.skillSelect = this.playerSkill.skills.get(Util.nextInt(0, this.playerSkill.skills.size() - 1));
            SkillService.gI().useSkill(this, plAtt, null, null);
        } else {
            return 0;
        }
        return super.injured(plAtt, 100, piercing, isMobAttack);
    }
    

    @Override
    public void rewards(Player pl) {

        int diemsb = Util.nextInt(1, 2); //50 sd 
        pl.point_sb += diemsb;
         Service.getInstance().sendThongBao(pl, "Bạn Nhận Được " + diemsb + " Điểm Săn Boss");        
      
        //*****************************************************************************************************
    

        //*************************************************************************************************************************** 
      
      
     
        //****************************************************************list rơi bên dưới****************************************************

      int slcarot = Util.nextInt(6, 20);
        for (int i = 0; i < slcarot; i++) {
            ItemMap carot = new ItemMap(zone,  Util.nextInt(220, 224), 10, 10 * i + this.location.x, zone.map.yPhysicInTop(this.location.x, 0), -1);
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

