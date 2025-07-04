package nro.models.item;

import nro.models.player.NPoint;
import nro.models.player.Player;
import nro.server.Client;
import nro.services.ItemTimeService;
import nro.services.Service;
import nro.utils.Util;

/**
 * @stole Arriety
 */
public class ItemTime {

    //id item text
    public static final byte DOANH_TRAI = 0;
    public static final byte BAN_DO_KHO_BAU = 1;

    public static final byte TOP_DAME_ATTACK_BOSS = 2;

    public static final int TIME_ITEM = 600000;
    public static final int TIME_OPEN_POWER = 86400000;
    public static final int TIME_MAY_DO = 1800000;
    public static final int TIME_EAT_MEAL = 600000;
    public static final int TIME_NUOC_MIA = 600000;
    public static final int TIME_X_EXP = 1800000;
    public static final int TIME_30P = 1800000;
    
    public static final int TIME_MYTOM = 600000;
    public static final int TIME_THANHKIEM = 600000;
    public static final int TIME_HOKIEM = 300000;

    private Player player;
    public boolean isUseBoHuyet2;
    public boolean isUseBoKhi2;
    public boolean isUseGiapXen2;
    public boolean isUseCuongNo2;
    public boolean isDuoiKhi;

    public boolean isBanhTrungThu1Trung;
    public boolean isBanhTrungThu2Trung;
    public boolean isBanhTrungThuDacBiet;
    public boolean isBanhTrungThuGaQuay;
    public boolean isBanhTrungThuThapCam;
    public boolean ismytom;
    public boolean isthanhkiem;
    public boolean ishokiem;
    public boolean isbinhx2;
    public boolean isbinhx3;
    public boolean isbinhx4;
    
     public boolean ismaydotainguyen;
     public boolean ismaydotrangbi;
     public boolean ismaydosaophale;
     public boolean ismaydothucan;
      public boolean ismaydongocrong;

    public long lastTimeBanhTrungThu1Trung;
    public long lastTimeBanhTrungThu2Trung;
    public long lastTimeBanhTrungThuDacBiet;
    public long lastTimeBanhTrungThuGaQuay;
    public long lastTimeBanhTrungThuThapCam;
    public long lastTimeMytom;
    public long lastTimeThanhkiem;
    public long lastTimeHokiem;
    public long lastTimebinhx2;
    public long lastTimebinhx3;
    public long lastTimebinhx4;
    
    public long lastTimetainguyen;
    public long lastTimetrangbi;
    public long lastTimesaophale;
    public long lastTimethucan;
    public long lastTimengocrong;

    public long lastTimeBoHuyet2;
    public long lastTimeBoKhi2;
    public long lastTimeGiapXen2;
    public long lastTimeCuongNo2;
    public long lastTimeDuoiKhi;

    public boolean isUseBanhChung;
    public boolean isUseBanhTet;
    public long lastTimeBanhChung;
    public long lastTimeBanhTet;
    public boolean isUseBoHuyet;
    public boolean isUseBoKhi;
    public boolean isUseGiapXen;
    public boolean isUseCuongNo;
    public boolean isUseAnDanh;
    public long lastTimeBoHuyet;
    public long lastTimeBoKhi;
    public long lastTimeGiapXen;
    public long lastTimeCuongNo;
    public long lastTimeAnDanh;

    public boolean isUseMayDo;
    public long lastTimeUseMayDo;
    public boolean isUseMayDoskh;
    public long lastTimeUseMayDoskh;

    public boolean isOpenPower;
    public long lastTimeOpenPower;

    public boolean isUseTDLT;
    public long lastTimeUseTDLT;
    public long timeTDLT;

    public boolean isUseOffline;
    public long lastTimeUseOffline;
    public long timeOffline;

    public boolean isEatMeal;
    public long lastTimeEatMeal;
    public int iconMeal;

    public long lastnuocmiakhonglo;
    public boolean isnuocmiakhonglo;
    public long lastnuocmiathom;
    public boolean isnuocmiathom;
    public long lastnuocmiasaurieng;
    public boolean isnuocmiasaurieng;

    //star item time tamkjll 
    public boolean isXexpTamkjll1_5;
    public boolean isXexpTamkjll2;
    public boolean isXexpTamkjll3;
    public boolean isXexpTamkjll4;
    public boolean isXexpTamkjll5;
    public boolean isXexpTamkjll6;

    public long lastTimeexpTamkjll1_5;
    public long lastTimeexpTamkjll2;
    public long lastTimeexpTamkjll3;
    public long lastTimeexpTamkjll4;
    public long lastTimeexpTamkjll5;
    public long lastTimeexpTamkjll6;

    public ItemTime(Player player) {
        this.player = player;
    }

    public void update() {
        boolean update = false;
        if (isBanhTrungThu1Trung) {
            if (Util.canDoWithTime(lastTimeBanhTrungThu1Trung, TIME_ITEM)) {
                isBanhTrungThu1Trung = false;
                update = true;
            }
        }
        if (isBanhTrungThu2Trung) {
            if (Util.canDoWithTime(lastTimeBanhTrungThu2Trung, TIME_ITEM)) {
                isBanhTrungThu2Trung = false;
                update = true;
            }
        }
        if (isBanhTrungThuDacBiet) {
            if (Util.canDoWithTime(lastTimeBanhTrungThuDacBiet, TIME_ITEM)) {
                isBanhTrungThuDacBiet = false;
                update = true;
            }
        }
        if (isBanhTrungThuGaQuay) {
            if (Util.canDoWithTime(lastTimeBanhTrungThuGaQuay, TIME_ITEM)) {
                isBanhTrungThuGaQuay = false;
                update = true;
            }
        }
        if (isBanhTrungThuThapCam) {
            if (Util.canDoWithTime(lastTimeBanhTrungThuThapCam, TIME_ITEM)) {
                isBanhTrungThuThapCam = false;
                update = true;
            }

        }
        if (ismytom) {
            if (Util.canDoWithTime(lastTimeMytom, TIME_MYTOM)) {
                ismytom = false;
                update = true;
            }

        }
        if (isthanhkiem) {
            if (Util.canDoWithTime(lastTimeThanhkiem, TIME_THANHKIEM)) {
                isthanhkiem = false;
                update = true;
            }

        }
        if (ishokiem) {
            if (Util.canDoWithTime(lastTimeHokiem, TIME_HOKIEM)) {
                ishokiem = false;
                update = true;
            }
        }
         if (isbinhx2) {
            if (Util.canDoWithTime(lastTimebinhx2, TIME_ITEM)) {
                isbinhx2 = false;
                update = true;
            }
        }
         if (isbinhx3) {
            if (Util.canDoWithTime(lastTimebinhx3, TIME_ITEM)) {
                isbinhx3 = false;
                update = true;
            }
        }
         if (isbinhx4) {
            if (Util.canDoWithTime(lastTimebinhx4, TIME_ITEM)) {
                isbinhx4= false;
                update = true;
            }
        }
        
         if (ismaydotainguyen) {
            if (Util.canDoWithTime(lastTimetainguyen, TIME_30P)) {
                ismaydotainguyen= false;
                update = true;
            }
        }
        if (ismaydotrangbi) {
            if (Util.canDoWithTime(lastTimetrangbi, TIME_30P)) {
                ismaydotrangbi= false;
                update = true;
            }
        }  
          if (ismaydosaophale) {
            if (Util.canDoWithTime(lastTimesaophale, TIME_30P)) {
                ismaydosaophale= false;
                update = true;
            }
        }   
           if (ismaydongocrong) {
            if (Util.canDoWithTime(lastTimengocrong, TIME_30P)) {
                ismaydongocrong= false;
                update = true;
            }
        }   
           if (ismaydothucan) {
            if (Util.canDoWithTime(lastTimethucan, TIME_30P)) {
                ismaydothucan= false;
                update = true;
            }
        }    
          
          
          

        if (isDuoiKhi) {
            if (Util.canDoWithTime(lastTimeDuoiKhi, TIME_ITEM)) {
                isDuoiKhi = false;
                update = true;
            }
        }
        if (isEatMeal) {
            if (Util.canDoWithTime(lastTimeEatMeal, TIME_EAT_MEAL)) {
                isEatMeal = false;
                update = true;
            }
        }
        if (isUseBoHuyet) {
            if (Util.canDoWithTime(lastTimeBoHuyet, TIME_ITEM)) {
                isUseBoHuyet = false;
                update = true;
            }
        }
        if (isUseBoKhi) {
            if (Util.canDoWithTime(lastTimeBoKhi, TIME_ITEM)) {
                isUseBoKhi = false;
                update = true;
            }
        }
        if (isUseGiapXen) {
            if (Util.canDoWithTime(lastTimeGiapXen, TIME_ITEM)) {
                isUseGiapXen = false;
            }
        }
        if (isUseCuongNo) {
            if (Util.canDoWithTime(lastTimeCuongNo, TIME_ITEM)) {
                isUseCuongNo = false;
                update = true;
            }
        }
        if (isUseAnDanh) {
            if (Util.canDoWithTime(lastTimeAnDanh, TIME_ITEM)) {
                isUseAnDanh = false;
            }
        }
        if (isUseBanhChung) {
            if (Util.canDoWithTime(lastTimeBanhChung, TIME_ITEM)) {
                isUseBanhChung = false;
            }
        }
        if (isUseBanhTet) {
            if (Util.canDoWithTime(lastTimeBanhTet, TIME_ITEM)) {
                isUseBanhTet = false;
            }
        }
        if (isUseBoHuyet2) {
            if (Util.canDoWithTime(lastTimeBoHuyet2, TIME_ITEM)) {
                isUseBoHuyet2 = false;
                update = true;
            }
        }
        if (isUseBoKhi2) {
            if (Util.canDoWithTime(lastTimeBoKhi2, TIME_ITEM)) {
                isUseBoKhi2 = false;
                update = true;
            }
        }
        if (isUseGiapXen2) {
            if (Util.canDoWithTime(lastTimeGiapXen2, TIME_ITEM)) {
                isUseGiapXen2 = false;
            }
        }
        if (isUseCuongNo2) {
            if (Util.canDoWithTime(lastTimeCuongNo2, TIME_ITEM)) {
                isUseCuongNo2 = false;
                update = true;
            }
        }
        if (isOpenPower) {
            if (Util.canDoWithTime(lastTimeOpenPower, TIME_OPEN_POWER)) {
                player.nPoint.limitPower++;
                if (player.nPoint.limitPower > NPoint.MAX_LIMIT) {
                    player.nPoint.limitPower = NPoint.MAX_LIMIT;
                }
                player.nPoint.initPowerLimit();
                Service.getInstance().sendThongBao(player, "Giới hạn sức mạnh của bạn đã được tăng lên 1 bậc");
                isOpenPower = false;
            }
        }
        if (isUseMayDo) {
            if (Util.canDoWithTime(lastTimeUseMayDo, TIME_MAY_DO)) {
                isUseMayDo = false;
            }
        }
        if (isUseMayDoskh) {
            if (Util.canDoWithTime(lastTimeUseMayDoskh, TIME_MAY_DO)) {
                isUseMayDoskh = false;
            }
        }

        if (isUseTDLT) {
            if (Util.canDoWithTime(lastTimeUseTDLT, timeTDLT)) {
                this.isUseTDLT = false;
                ItemTimeService.gI().sendCanAutoPlay(this.player);
            }
        }
        if (isUseOffline) {
            if (Util.canDoWithTime(lastTimeUseOffline, timeOffline)) {
                this.isUseOffline = false;
                Client.gI().remove(player);
            }
        }
        if (isUseBanhChung) {
            if (Util.canDoWithTime(lastTimeBanhChung, TIME_ITEM)) {
                isUseBanhChung = false;
                update = true;
            }
        }
        if (isUseBanhTet) {
            if (Util.canDoWithTime(lastTimeBanhTet, TIME_ITEM)) {
                isUseBanhTet = false;
                update = true;
            }
        }
        if (isnuocmiakhonglo) {
            if (Util.canDoWithTime(lastnuocmiakhonglo, TIME_NUOC_MIA)) {
                isnuocmiakhonglo = false;
            }
        }
        if (isnuocmiathom) {
            if (Util.canDoWithTime(lastnuocmiathom, TIME_NUOC_MIA)) {
                isnuocmiathom = false;
            }
        }
        if (isnuocmiasaurieng) {
            if (Util.canDoWithTime(lastnuocmiasaurieng, TIME_NUOC_MIA)) {
                isnuocmiasaurieng = false;
            }
        }
        if (isXexpTamkjll1_5) {
            if (Util.canDoWithTime(lastTimeexpTamkjll1_5, TIME_X_EXP)) {
                isXexpTamkjll1_5 = false;
            }
        }
        if (isXexpTamkjll2) {
            if (Util.canDoWithTime(lastTimeexpTamkjll2, TIME_X_EXP)) {
                isXexpTamkjll2 = false;
            }
        }
        if (isXexpTamkjll3) {
            if (Util.canDoWithTime(lastTimeexpTamkjll3, TIME_X_EXP)) {
                isXexpTamkjll3 = false;
            }
        }
        if (isXexpTamkjll4) {
            if (Util.canDoWithTime(lastTimeexpTamkjll4, TIME_X_EXP)) {
                isXexpTamkjll4 = false;
            }
        }
        if (isXexpTamkjll5) {
            if (Util.canDoWithTime(lastTimeexpTamkjll5, TIME_X_EXP)) {
                isXexpTamkjll5 = false;
            }
        }
        if (isXexpTamkjll6) {
            if (Util.canDoWithTime(lastTimeexpTamkjll6, TIME_X_EXP)) {
                isXexpTamkjll6 = false;
            }
        }
        if (update) {
            Service.getInstance().point(player);
        }
    }

    public void dispose() {
        this.player = null;
    }
}
