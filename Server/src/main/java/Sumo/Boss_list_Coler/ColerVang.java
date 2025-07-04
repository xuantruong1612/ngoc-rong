package Sumo.Boss_list_Coler;

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
public class ColerVang extends FutureBoss {

    public ColerVang() {
        super(BossFactory.ColerVang, BossData.ColerVang);
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

       int exptb = Util.nextInt(1, 500000); //50 sd 
        pl.ExpTamkjll += exptb;
        Service.getInstance().sendThongBao(pl, "Bạn Nhận Được " + exptb + " Điểm Săn Boss");
        //*****************************************************************************************************
        int[] ramdomroiitem = new int[]{822};//list item

        //*************************************************************************************************************************** 
        int ramdomitem = new Random().nextInt(ramdomroiitem.length);

        //****************************************************************list rơi bên dưới****************************************************
        if (Util.isTrue(50, 100)) { // Tỉ lệ 50%
            int slcarot3 = 6;
            for (int i = 0; i < slcarot3; i++) {
                ItemMap carot = new ItemMap(zone, 1404, 1, 10 * i + this.location.x,
                        zone.map.yPhysicInTop(this.location.x, 0), -1);
//                carot.options.add(new ItemOption(50, Util.nextInt(10, 1000)));
//                carot.options.add(new ItemOption(77, Util.nextInt(10, 1000)));
//                carot.options.add(new ItemOption(103, Util.nextInt(10, 1000)));
//                carot.options.add(new ItemOption(78, Util.nextInt(1, 50)));
//                 carot.options.add(new ItemOption(107, Util.nextInt(1, 18)));
                carot.options.add(new ItemOption(31, 0));
                // Tỉ lệ 99% ra opti 93
                if (Util.isTrue(9, 1000)) {
                    carot.options.add(new ItemOption(93, Util.nextInt(1, 2)));
                }

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
