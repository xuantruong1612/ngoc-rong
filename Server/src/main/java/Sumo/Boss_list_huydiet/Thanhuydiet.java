package Sumo.Boss_list_huydiet;

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
public class Thanhuydiet extends FutureBoss {

    public Thanhuydiet() {
        super(BossFactory.THANHUYDIET, BossData.THANHUYDIET);
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

        //****************************************************************list rơi bên dưới****************************************************
        if (Util.isTrue(10, 100)) { // Tỉ lệ 50%
            int slcarot3 = 1;
            for (int i = 0; i < slcarot3; i++) {
                ItemMap carot = new ItemMap(zone, Util.nextInt(650, 662), 1, 10 * i + this.location.x,
                        zone.map.yPhysicInTop(this.location.x, 0), -1);
                carot.options.add(new ItemOption(58, Util.nextInt(1, 1000)));
                carot.options.add(new ItemOption(52, Util.nextInt(10, 5000)));
                carot.options.add(new ItemOption(53, Util.nextInt(10, 5000)));
                carot.options.add(new ItemOption(54, Util.nextInt(1, 5000)));
                carot.options.add(new ItemOption(55, Util.nextInt(1, 10)));
                carot.options.add(new ItemOption(61, Util.nextInt(1, 10)));
                carot.options.add(new ItemOption(107, Util.nextInt(1, 50)));
                 carot.options.add(new ItemOption(Util.nextInt(176, 180), Util.nextInt(1, 50)));
                carot.options.add(new ItemOption(31, 0));
                // Tỉ lệ 99% ra opti 93
                if (Util.isTrue(999, 1000)) {
                    carot.options.add(new ItemOption(93, 1));
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
