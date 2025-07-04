//package nro.models.boss.list_boss;
//
//import java.util.Random;
//import nro.models.boss.Boss;
//import nro.models.boss.BossData;
//import nro.models.boss.BossFactory;
//import nro.models.boss.FutureBoss;
//import nro.models.item.ItemOption;
//import nro.models.map.ItemMap;
//import nro.models.player.Player;
//import nro.services.ChatGlobalService;
//import nro.services.PlayerService;
//import nro.services.RewardService;
//import nro.services.Service;
//import nro.utils.TimeUtil;
//import nro.utils.Util;
//
///**
// *
// * @Arriety
// *
// */
//public class BossTheGioi extends FutureBoss {
//
//    public BossTheGioi() {
//        super(BossFactory.BOSS_THE_GIOI, BossData.BOSS_THE_GIOI);
//    }
//
//    private long lasttimehakai;
//    private int timehakai;
//
//    @Override
//    protected boolean useSpecialSkill() {
//        return false;
//    }
//
//    @Override
//    public void rewards(Player pl) {
//        pl.point_sb++;
//        Service.getInstance().sendThongBao(pl, "Bạn Nhận Được 1 điểm boss");
//    }
//
//    @Override
//    public void idle() {
//
//    }
//
//    @Override
//    public void checkPlayerDie(Player pl) {
//
//    }
//
//    @Override
//    public void initTalk() {
//        textTalkMidle = new String[]{"Ta chính là đệ nhất vũ trụ cao thủ"};
//        textTalkAfter = new String[]{"Ác quỷ biến hình aaa..."};
//    }
//
//    private boolean isBossSummoned = false;
//
//    @Override
//    public void update() {
//        if (!isBossSummoned && TimeUtil.getCurrHour() == 20) {
//            this.joinMap();
//            isBossSummoned = true;
//        }
//    }
//
//    private void huydiet() {
//        if (!Util.canDoWithTime(this.lasttimehakai, this.timehakai) || !Util.isTrue(50, 100)) {
//            return;
//        }
//
//        Player pl = this.zone.getRandomPlayerInMap();
//        if (pl == null || pl.isDie()) {
//            return;
//        }
//        this.nPoint.dameg += (pl.nPoint.dame * 5 / 100);
//        this.nPoint.hpg += (pl.nPoint.hp * 2 / 100);
//        this.nPoint.critg++;
//        this.nPoint.calPoint();
//        PlayerService.gI().hoiPhuc(this, pl.nPoint.hp, 0);
//        pl.injured(null, pl.nPoint.hpMax, true, false);
//        Service.gI().sendThongBao(pl, "Bạn vừa bị " + this.name + " cho bay màu");
//        this.chat("Hắn ta mạnh quá,coi chừng " + pl.name + ",tên " + this.name + " hắn không giống như những kẻ thù trước đây");
//        this.chat("Thật là yếu ớt " + pl.name);
//        this.lasttimehakai = System.currentTimeMillis();
//        this.timehakai = Util.nextInt(2000, 30000);
//    }
//
//}
