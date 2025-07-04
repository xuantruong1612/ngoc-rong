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
//public class BossNew extends FutureBoss {
//
//    public BossNew() {
//        super(BossFactory.BOSS_NEW, BossData.BOSS_NEW);
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
//        pl.ExpTamkjll += 5000000;
//        Service.getInstance().sendThongBao(pl, "Bạn Nhận Được 5tr exp tiên bang");
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
//    @Override
//    public void active() {
//            this.hapThu();
//            this.attack();
//    }
//
//
//    private long lastTimeHapThu;
//    private int timeHapThu;
//
//    private void hapThu() {
//        if (!Util.canDoWithTime(this.lastTimeHapThu, this.timeHapThu)) {
//            return;
//        }
//
//        Player pl = this.zone.getRandomPlayerInMap();
//        if (pl == null || pl.isDie()) {
//            return;
//        }
//        double HP = this.nPoint.hp + pl.nPoint.hp;
//        if (HP > 2000000000) {
//            HP = 2000000000;
//        }
//        if (this.nPoint.hpg < HP) {
//            this.nPoint.hpg = (int) HP;
//        }
//        this.nPoint.hp = (int) HP;
//        this.nPoint.critg++;
//        PlayerService.gI().hoiPhuc(this, pl.nPoint.hp, 0);
//        Service.gI().sendThongBao(pl, "Bạn vừa bị " + this.name + " hấp thu!");
//        this.chat("Ui cha cha, kinh dị quá. " + pl.name + " vừa bị tên " + this.name + " nuốt chửng kìa!!!");
//        this.chat("Haha, ngọt lắm đấy " + pl.name + "..");
//        this.lastTimeHapThu = System.currentTimeMillis();
//        this.timeHapThu = Util.nextInt(1500, 20000);
//    }
//}
