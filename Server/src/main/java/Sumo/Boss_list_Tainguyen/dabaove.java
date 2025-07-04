package Sumo.Boss_list_keytambao;

import nro.models.boss.Boss;
import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.boss.FutureBoss;
import nro.models.item.ItemOption;
import nro.models.map.ItemMap;
import nro.models.player.Player;
import nro.services.PlayerService;
import nro.services.RewardService;
import nro.services.Service;
import nro.services.TaskService;
import nro.utils.TimeUtil;
import nro.utils.Util;

/**
 
 */
public class dabaove extends FutureBoss {

    private long lasttimehakai;
    private int timehakai;
    private int defeatCount = 0; // Số người chơi bị hạ gục
    private boolean isBossSummoned = false;

    public dabaove() {
        super(BossFactory.dabaove, BossData.dabaove);
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
        if (Util.isTrue(2, 100)) { // Tỉ lệ 50%
            int slcarot3 = 2;
            for (int i = 0; i < slcarot3; i++) {
                ItemMap carot = new ItemMap(zone, 987, 1, 10 * i + this.location.x,
                        zone.map.yPhysicInTop(this.location.x, 0), -1);
                carot.options.add(new ItemOption(30, 0));
                // Tỉ lệ 99% ra opti 93
                if (Util.isTrue(998, 1000)) {
                    carot.options.add(new ItemOption(93, 10));
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
      //  this.defeatCount++; // Tăng số lần hạ gục người chơi
    }

    @Override
    public void initTalk() {
        textTalkMidle = new String[]{"Ta chính là đệ nhất vũ trụ cao thủ!"};
        textTalkAfter = new String[]{"Ác quỷ biến hình aaa..."};
    }

   

    private void huydiet() {
        if (!Util.canDoWithTime(this.lasttimehakai, this.timehakai) || !Util.isTrue(50, 100)) {
            return;
        }

        Player pl = this.zone.getRandomPlayerInMap();
        if (pl == null || pl.isDie()) {
            return;
        }

        // Tăng sức mạnh dựa trên số người chơi bị hạ gục
        this.nPoint.dameg += (pl.nPoint.dame * 5 / 100) * this.defeatCount;
        this.nPoint.hpg += (pl.nPoint.hp * 2 / 100) * this.defeatCount;
        this.nPoint.critg++;
        this.nPoint.calPoint();

        // Gây sát thương lớn cho người chơi
        PlayerService.gI().hoiPhuc(this, pl.nPoint.hp, 0);
        pl.injured(null, pl.nPoint.hpMax, true, false);
        Service.gI().sendThongBao(pl, "Bạn vừa bị " + this.name + " hủy diệt!");

        // Boss nói khi hạ gục người chơi
        this.chat("Hắn ta mạnh quá, coi chừng " + pl.name + ", tên " + this.name + " không giống như những kẻ thù trước đây!");
        this.chat("Thật là yếu ớt " + pl.name + "!");

        // Đặt lại thời gian hồi chiêu
        this.lasttimehakai = System.currentTimeMillis();
        this.timehakai = Util.nextInt(10000, 15000); // 10-15 giây mới có thể sử dụng lại
    }
}
