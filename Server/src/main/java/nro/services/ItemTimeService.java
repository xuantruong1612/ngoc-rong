package nro.services;

import nro.consts.ConstPlayer;
import nro.models.item.Item;
import nro.models.item.ItemOption;
import nro.models.map.phoban.BanDoKhoBau;
import nro.models.map.phoban.DoanhTrai;
import nro.models.player.Fusion;
import nro.models.player.Player;
import nro.server.io.Message;
import nro.utils.Log;

import static nro.models.item.ItemTime.*;
//import nro.utils.Util;

/**
 *
 * @Build by Arriety
 *
 */
public class ItemTimeService {

    private static ItemTimeService i;

    public static ItemTimeService gI() {
        if (i == null) {
            i = new ItemTimeService();
        }
        return i;
    }

//    public void sendTextGas(Player player) {
//        if (player.clan != null
//                && player.clan.timeOpenKhiGas != 0) {
//            int secondPassed = (int) ((System.currentTimeMillis() - player.clan.timeOpenKhiGas) / 1000);
//            int secondsLeft = (gas.TIME_KHI_GAS / 1000) - secondPassed;
//            sendTextTime(player, KHI_GASS, "Khí Gas Hủy Diệt: ", secondsLeft);
//            if (secondsLeft <= 0 || secondsLeft > 1800) {
//                removeTextKhiGas(player);
//            }
//        }
//    }
    //gửi cho client
    public void sendAllItemTime(Player player) {
        sendTextDoanhTrai(player);
        sendTextBanDoKhoBau(player);
        if (player.fusion.typeFusion == ConstPlayer.LUONG_LONG_NHAT_THE) {
            sendItemTime(player, player.gender == ConstPlayer.NAMEC ? 3901 : 3790,
                    (int) ((Fusion.TIME_FUSION - (System.currentTimeMillis() - player.fusion.lastTimeFusion)) / 1000));
        }
        if (player.itemTime.isBanhTrungThu1Trung) {
            sendItemTime(player, 4042, (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeBanhTrungThu1Trung)) / 1000));
        }
        if (player.itemTime.isBanhTrungThu2Trung) {
            sendItemTime(player, 4043, (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeBanhTrungThu2Trung)) / 1000));
        }
        if (player.itemTime.isBanhTrungThuDacBiet) {
            sendItemTime(player, 4125, (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeBanhTrungThuDacBiet)) / 1000));
        }
        if (player.itemTime.isBanhTrungThuGaQuay) {
            sendItemTime(player, 8132, (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeBanhTrungThuGaQuay)) / 1000));
        }
        if (player.itemTime.isBanhTrungThuThapCam) {
            sendItemTime(player, 8131, (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeBanhTrungThuThapCam)) / 1000));
        }
         if (player.itemTime.ismytom) {
            sendItemTime(player, 15459, (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeMytom)) / 1000));
        } 
         if (player.itemTime.isthanhkiem) {
            sendItemTime(player, 18232, (int) ((TIME_THANHKIEM - (System.currentTimeMillis() - player.itemTime.lastTimeThanhkiem)) / 1000));
        }  
         if (player.itemTime.ishokiem) {
            sendItemTime(player, 18234, (int) ((TIME_HOKIEM - (System.currentTimeMillis() - player.itemTime.lastTimeHokiem)) / 1000));
        }  
         if (player.itemTime.isbinhx2) {
            sendItemTime(player, 11913, (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimebinhx2)) / 1000));
        }   
         if (player.itemTime.isbinhx3) {
            sendItemTime(player, 11914, (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimebinhx3)) / 1000));
        }   
         if (player.itemTime.isbinhx4) {
            sendItemTime(player, 11915, (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimebinhx4)) / 1000));
        }  
          if (player.itemTime.ismaydotainguyen) {
            sendItemTime(player, 10612, (int) ((TIME_30P - (System.currentTimeMillis() - player.itemTime.lastTimetainguyen)) / 1000));
        }   
        if (player.itemTime.ismaydotrangbi) {
            sendItemTime(player, 10613, (int) ((TIME_30P - (System.currentTimeMillis() - player.itemTime.lastTimetrangbi)) / 1000));
        }    
         if (player.itemTime.ismaydosaophale) {
            sendItemTime(player, 10614, (int) ((TIME_30P - (System.currentTimeMillis() - player.itemTime.lastTimesaophale)) / 1000));
        }    
         if (player.itemTime.ismaydongocrong) {
            sendItemTime(player, 10616, (int) ((TIME_30P - (System.currentTimeMillis() - player.itemTime.lastTimengocrong)) / 1000));
        }    
          if (player.itemTime.ismaydothucan) {
            sendItemTime(player, 10615, (int) ((TIME_30P - (System.currentTimeMillis() - player.itemTime.lastTimethucan)) / 1000));
        }   
         
         
        
        if (player.itemTime.isDuoiKhi) {
            sendItemTime(player, 5072, (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeDuoiKhi)) / 1000));
        }
        if (player.itemTime.isUseBoHuyet) {
            sendItemTime(player, 2755, (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeBoHuyet)) / 1000));
        }
        if (player.itemTime.isUseBoKhi) {
            sendItemTime(player, 2756, (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeBoKhi)) / 1000));
        }
        if (player.itemTime.isUseGiapXen) {
            sendItemTime(player, 2757, (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeGiapXen)) / 1000));
        }
        if (player.itemTime.isUseCuongNo) {
            sendItemTime(player, 2754, (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeCuongNo)) / 1000));
        }
        if (player.itemTime.isUseAnDanh) {
            sendItemTime(player, 2760, (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeAnDanh)) / 1000));
        }
        if (player.itemTime.isOpenPower) {
            sendItemTime(player, 3783, (int) ((TIME_OPEN_POWER - (System.currentTimeMillis() - player.itemTime.lastTimeOpenPower)) / 1000));
        }
        if (player.itemTime.isUseMayDo) {
            sendItemTime(player, 30047, (int) ((TIME_MAY_DO - (System.currentTimeMillis() - player.itemTime.lastTimeUseMayDo)) / 1000));
        }
       if (player.itemTime.isUseMayDoskh) {
            sendItemTime(player, 30047, (int) ((TIME_MAY_DO - (System.currentTimeMillis() - player.itemTime.lastTimeUseMayDoskh)) / 1000));
        } 
        
        
        if (player.itemTime.isEatMeal) {
            sendItemTime(player, player.itemTime.iconMeal, (int) ((TIME_EAT_MEAL - (System.currentTimeMillis() - player.itemTime.lastTimeEatMeal)) / 1000));
        }
        if (player.itemTime.isUseBanhChung) {
            sendItemTime(player, 7080, (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeBanhChung)) / 1000));
        }
        if (player.itemTime.isUseBanhTet) {
            sendItemTime(player, 7079, (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeBanhTet)) / 1000));
        }
        if (player.itemTime.isUseBoHuyet2) {
            sendItemTime(player, 10714, (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeBoHuyet2)) / 1000));
        }
        if (player.itemTime.isUseBoKhi2) {
            sendItemTime(player, 10715, (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeBoKhi2)) / 1000));
        }
        if (player.itemTime.isUseGiapXen2) {
            sendItemTime(player, 10712, (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeGiapXen2)) / 1000));
        }
        if (player.itemTime.isUseCuongNo2) {
            sendItemTime(player, 10716, (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeCuongNo2)) / 1000));
        }
        if (player.itemTime.isnuocmiakhonglo) {
            sendItemTime(player, 20879, (int) ((TIME_NUOC_MIA - (System.currentTimeMillis() - player.itemTime.lastnuocmiakhonglo)) / 1000));
        }
        if (player.itemTime.isnuocmiathom) {
            sendItemTime(player, 20880, (int) ((TIME_NUOC_MIA - (System.currentTimeMillis() - player.itemTime.lastnuocmiathom)) / 1000));
        }
        if (player.itemTime.isnuocmiasaurieng) {
            sendItemTime(player, 20881, (int) ((TIME_NUOC_MIA - (System.currentTimeMillis() - player.itemTime.lastnuocmiasaurieng)) / 1000));
        }
        if (player.itemTime.isXexpTamkjll1_5) {
            sendItemTime(player, 13312,
                    (int) ((TIME_X_EXP - (System.currentTimeMillis() - player.itemTime.lastTimeexpTamkjll1_5)) / 1000));
        }
        if (player.itemTime.isXexpTamkjll2) {
            sendItemTime(player, 13313,
                    (int) ((TIME_X_EXP - (System.currentTimeMillis() - player.itemTime.lastTimeexpTamkjll2)) / 1000));
        }
        if (player.itemTime.isXexpTamkjll3) {
            sendItemTime(player, 13314,
                    (int) ((TIME_X_EXP - (System.currentTimeMillis() - player.itemTime.lastTimeexpTamkjll3)) / 1000));
        }
        if (player.itemTime.isXexpTamkjll4) {
            sendItemTime(player, 13315,
                    (int) ((TIME_X_EXP - (System.currentTimeMillis() - player.itemTime.lastTimeexpTamkjll4)) / 1000));
        }
        if (player.itemTime.isXexpTamkjll5) {
            sendItemTime(player, 13316,
                    (int) ((TIME_X_EXP - (System.currentTimeMillis() - player.itemTime.lastTimeexpTamkjll5)) / 1000));
        }
        if (player.itemTime.isXexpTamkjll6) {
            sendItemTime(player, 13317,
                    (int) ((TIME_X_EXP - (System.currentTimeMillis() - player.itemTime.lastTimeexpTamkjll6)) / 1000));
        }
    }

    //bật tđlt
    public void turnOnTDLT(Player player, Item item) {
        long min = 0;
        for (ItemOption io : item.itemOptions) {
            if (io.optionTemplate.id == 1) {
                min = io.param;
                io.param = 0;
                break;
            }
        }
        player.itemTime.isUseTDLT = true;
        player.itemTime.timeTDLT = min * 60 * 1000;
        player.itemTime.lastTimeUseTDLT = System.currentTimeMillis();
        sendCanAutoPlay(player);
        sendItemTime(player, 4387, (int) (player.itemTime.timeTDLT / 1000));
        InventoryService.gI().sendItemBags(player);
    }
    
    public void turnOnOffline(Player player, Item item) {
        long min = 0;
        for (ItemOption io : item.itemOptions) {
            if (io.optionTemplate.id == 1) {
                min = io.param;
                io.param = 0;
                break;
            }
        }
        player.itemTime.isUseOffline = true;
        player.itemTime.timeOffline = min * 60 * 1000;
        player.itemTime.lastTimeUseOffline = System.currentTimeMillis();
        sendItemTime(player, 4387, (int) (player.itemTime.timeOffline / 1000));
        InventoryService.gI().sendItemBags(player);
    }
    
    //tắt tđlt
    public void turnOffOffline(Player player, Item item) {
        player.itemTime.isUseOffline = false;
        for (ItemOption io : item.itemOptions) {
            if (io.optionTemplate.id == 1) {
                io.param = (short) ((player.itemTime.timeOffline - (System.currentTimeMillis() - player.itemTime.lastTimeUseOffline)) / 60 / 1000);
                break;
            }
        }
        removeItemTime(player, 4387);
        InventoryService.gI().sendItemBags(player);
    }

    //tắt tđlt
    public void turnOffTDLT(Player player, Item item) {
        player.itemTime.isUseTDLT = false;
        for (ItemOption io : item.itemOptions) {
            if (io.optionTemplate.id == 1) {
                io.param = (short) ((player.itemTime.timeTDLT - (System.currentTimeMillis() - player.itemTime.lastTimeUseTDLT)) / 60 / 1000);
                break;
            }
        }
        sendCanAutoPlay(player);
        removeItemTime(player, 4387);
        InventoryService.gI().sendItemBags(player);
    }

    public void sendCanAutoPlay(Player player) {
        Message msg;
        try {
            msg = new Message(-116);
            msg.writer().writeByte(player.itemTime.isUseTDLT ? 1 : 0);
            player.sendMessage(msg);
        } catch (Exception e) {
            Log.error(ItemTimeService.class, e);
        }
    }

    public void sendTextDoanhTrai(Player player) {
        if (player.clan != null && !player.clan.haveGoneDoanhTrai
                && player.clan.timeOpenDoanhTrai != 0) {
            int secondPassed = (int) ((System.currentTimeMillis() - player.clan.timeOpenDoanhTrai) / 1000);
            int secondsLeft = (DoanhTrai.TIME_DOANH_TRAI / 1000) - secondPassed;
            sendTextTime(player, DOANH_TRAI, "Doanh trại độc nhãn", secondsLeft);
        }
    }

    public void sendTextBanDoKhoBau(Player player) {
        if (player.clan != null && player.clan.timeOpenBanDoKhoBau != 0) {
            int secondPassed = (int) ((System.currentTimeMillis() - player.clan.timeOpenBanDoKhoBau) / 1000);
            int secondsLeft = (BanDoKhoBau.TIME_BAN_DO_KHO_BAU / 1000) - secondPassed;
            sendTextTime(player, BAN_DO_KHO_BAU, "Bản đồ kho báu", secondsLeft);
        }
    }

    public void removeTextDoanhTrai(Player player) {
        removeTextTime(player, DOANH_TRAI);
    }

    public void removeTextBDKB(Player player) {
        removeTextTime(player, BAN_DO_KHO_BAU);
    }

    public void removeTextTime(Player player, byte id) {
        sendTextTime(player, id, "", 0);
    }

    public void sendTextTime(Player player, byte id, String text, int seconds) {
        Message msg;
        try {
            msg = new Message(65);
            msg.writer().writeByte(id);
            msg.writer().writeUTF(text);
            msg.writer().writeShort(seconds);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void sendItemTime(Player player, int itemId, int time) {
        Message msg;
        try {
            msg = new Message(-106);
            msg.writer().writeShort(itemId);
            msg.writer().writeShort(time);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void removeItemTime(Player player, int itemTime) {
        sendItemTime(player, itemTime, 0);
    }

}
