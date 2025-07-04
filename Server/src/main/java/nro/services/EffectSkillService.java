package nro.services;

import java.io.IOException;
import nro.models.mob.Mob;
import nro.models.player.Player;
import nro.models.skill.Skill;
import nro.server.io.Message;
import nro.utils.SkillUtil;
import nro.utils.Log;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import nro.jdbc.daos.PlayerDAO;
import nro.services.func.RadaService;
import nro.utils.Util;

/**
 *
 * @Build by Arriety
 *
 */
public class EffectSkillService {

    public static final byte TURN_ON_EFFECT = 1;
    public static final byte TURN_OFF_EFFECT = 0;
    public static final byte TURN_OFF_ALL_EFFECT = 2;

    public static final byte HOLD_EFFECT = 32;
    public static final byte SHIELD_EFFECT = 33;
    public static final byte HUYT_SAO_EFFECT = 39;
    public static final byte BLIND_EFFECT = 40;
    public static final byte SLEEP_EFFECT = 41;
    public static final byte STONE_EFFECT = 42;

    private static EffectSkillService i;

    private EffectSkillService() {

    }

    public static EffectSkillService gI() {
        if (i == null) {
            i = new EffectSkillService();
        }
        return i;
    }

    //hiệu ứng player dùng skill
    public void sendEffectUseSkill(Player player, byte skillId) {
        Skill skill = SkillUtil.getSkillbyId(player, skillId);
        Message msg;
        try {
            msg = new Message(-45);
            msg.writer().writeByte(8);
            msg.writer().writeInt((int) player.id);
            msg.writer().writeShort(skill.skillId);
            Service.getInstance().sendMessAnotherNotMeInMap(player, msg);
            msg.cleanup();
        } catch (Exception e) {
            Log.error(EffectSkillService.class, e);
        }
    }

    public void sendEffectPlayer(Player plUseSkill, Player plTarget, byte toggle, byte effect) {
        Message msg;
        try {
            msg = new Message(-124);
            msg.writer().writeByte(toggle); //0: hủy hiệu ứng, 1: bắt đầu hiệu ứng
            msg.writer().writeByte(0); //0: vào phần phayer, 1: vào phần mob
            if (toggle == TURN_OFF_ALL_EFFECT) {
                msg.writer().writeInt((int) plTarget.id);
            } else {
                msg.writer().writeByte(effect); //loại hiệu ứng
                msg.writer().writeInt((int) plTarget.id); //id player dính effect
                msg.writer().writeInt((int) plUseSkill.id); //id player dùng skill
            }
            Service.getInstance().sendMessAllPlayerInMap(plUseSkill, msg);
            msg.cleanup();
        } catch (Exception e) {
            Log.error(EffectSkillService.class, e);
        }
    }

    public void sendEffectMob(Player plUseSkill, Mob mobTarget, byte toggle, byte effect) {
        Message msg;
        try {
            msg = new Message(-124);
            msg.writer().writeByte(toggle); //0: hủy hiệu ứng, 1: bắt đầu hiệu ứng
            msg.writer().writeByte(1); //0: vào phần phayer, 1: vào phần mob
            msg.writer().writeByte(effect); //loại hiệu ứng
            msg.writer().writeByte(mobTarget.id); //id mob dính effect
            msg.writer().writeInt((int) plUseSkill.id); //id player dùng skill
            Service.getInstance().sendMessAllPlayerInMap(mobTarget.zone, msg);
            msg.cleanup();
        } catch (Exception e) {
            Log.error(EffectSkillService.class, e);
        }
    }
    public void setBienHinh(Player player, long lastTimeBienHinh, int timeBienHinh) {
        player.effectSkill.lastTimeBienHinh = lastTimeBienHinh;
        player.effectSkill.timeBienHinh = timeBienHinh;
        player.effectSkill.isBienHinh = true;
        
    }

    public void removeBienHinh(Player player) {
        if (player.effectSkill != null) {
            player.effectSkill.isBienHinh = false;
            Service.getInstance().Send_Caitrang(player);
        }
    }

    //Trói *********************************************************************
    //dừng sử dụng trói
    public void removeUseTroi(Player player) {
        if (player.effectSkill.mobAnTroi != null) {
            player.effectSkill.mobAnTroi.effectSkill.removeAnTroi();
        }
        if (player.effectSkill.plAnTroi != null) {
            removeAnTroi(player.effectSkill.plAnTroi);
        }
        player.effectSkill.useTroi = false;
        player.effectSkill.mobAnTroi = null;
        player.effectSkill.plAnTroi = null;
         Service.getInstance().point(player);
        Service.getInstance().Send_Info_NV(player);
        sendEffectPlayer(player, player, TURN_OFF_EFFECT, HOLD_EFFECT);
    }

    //hết thời gian bị trói
    private void removeAnTroi(Player player) {
        if (player != null && player.effectSkill != null) {
            player.effectSkill.anTroi = false;
            player.effectSkill.plTroi = null;
             Service.getInstance().point(player);
        Service.getInstance().Send_Info_NV(player);
            sendEffectPlayer(player, player, TURN_OFF_EFFECT, HOLD_EFFECT);
        }
    }

    public void setAnTroi(Player player, Player plTroi, long lastTimeAnTroi, int timeAnTroi) {
        player.effectSkill.anTroi = true;
//        player.effectSkill.lastTimeAnTroi = lastTimeAnTroi;
//        player.effectSkill.timeAnTroi = timeAnTroi;
        player.effectSkill.plTroi = plTroi;
         Service.getInstance().point(player);
        Service.getInstance().Send_Info_NV(player);
    }

    public void setUseTroi(Player player, long lastTimeTroi, int timeTroi) {
        player.effectSkill.useTroi = true;
        player.effectSkill.lastTimeTroi = lastTimeTroi;
        player.effectSkill.timeTroi = timeTroi;
         Service.getInstance().point(player);
        Service.getInstance().Send_Info_NV(player);
    }
    //**************************************************************************

    //Thôi miên ****************************************************************
    //thiết lập thời gian bắt đầu bị thôi miên
    public void setThoiMien(Player player, long lastTimeThoiMien, int timeThoiMien) {
        player.effectSkill.isThoiMien = true;
        player.effectSkill.lastTimeThoiMien = lastTimeThoiMien;
        player.effectSkill.timeThoiMien = timeThoiMien;
          Service.getInstance().point(player);
        Service.getInstance().Send_Info_NV(player);
    }

    //hết hiệu ứng thôi miên
    public void removeThoiMien(Player player) {
        player.effectSkill.isThoiMien = false;
        sendEffectPlayer(player, player, TURN_OFF_EFFECT, SLEEP_EFFECT);
          Service.getInstance().point(player);
        Service.getInstance().Send_Info_NV(player);
    }

    //**************************************************************************
    //Thái dương hạ san &&&&****************************************************
    //player ăn choáng thái dương hạ san
    public void startStun(Player player, long lastTimeStartBlind, int timeBlind) {
        player.effectSkill.lastTimeStartStun = lastTimeStartBlind;
        player.effectSkill.timeStun = timeBlind;
        player.effectSkill.isStun = true;
        sendEffectPlayer(player, player, TURN_ON_EFFECT, BLIND_EFFECT);
    }

    //kết thúc choáng thái dương hạ san
    public void removeStun(Player player) {
        player.effectSkill.isStun = false;
        sendEffectPlayer(player, player, TURN_OFF_EFFECT, BLIND_EFFECT);
    }
    //**************************************************************************

    //Socola *******************************************************************
    //player biến thành socola
    public void setSocola(Player player, long lastTimeSocola, int timeSocola) {
        player.effectSkill.lastTimeSocola = lastTimeSocola;
        player.effectSkill.timeSocola = timeSocola;
        player.effectSkill.isSocola = true;
        player.effectSkill.countPem1hp = 0;
        Service.getInstance().point(player);
        Service.getInstance().Send_Info_NV(player);
    }

    //player trở lại thành người
    public void removeSocola(Player player) {
         Service.getInstance().point(player);
        player.effectSkill.isSocola = false;
       Service.getInstance().point(player);
        Service.getInstance().Send_Info_NV(player);
        Service.getInstance().Send_Caitrang(player);
    }

    //quái biến thành socola
    public void sendMobToSocola(Player player, Mob mob, int timeSocola) {
        Message msg;
        try {
            msg = new Message(-112);
            msg.writer().writeByte(1);
            msg.writer().writeByte(mob.id); //mob id
            msg.writer().writeShort(4133); //icon socola
            Service.getInstance().sendMessAllPlayerInMap(player, msg);
            msg.cleanup();
            mob.effectSkill.setSocola(System.currentTimeMillis(), timeSocola);
        } catch (Exception e) {
            Log.error(EffectSkillService.class, e);
        }
    }
    //**************************************************************************

    //Dịch chuyển tức thời *****************************************************
    public void setBlindDCTT(Player player, long lastTimeDCTT, int timeBlindDCTT) {
        player.effectSkill.isBlindDCTT = true;
        player.effectSkill.lastTimeBlindDCTT = lastTimeDCTT;
        player.effectSkill.timeBlindDCTT = timeBlindDCTT;
         Service.getInstance().point(player);
        Service.getInstance().Send_Info_NV(player);
    }

    public void removeBlindDCTT(Player player) {
        player.effectSkill.isBlindDCTT = false;
        sendEffectPlayer(player, player, TURN_OFF_EFFECT, BLIND_EFFECT);
         Service.getInstance().point(player);
        Service.getInstance().Send_Info_NV(player);
    }
    //**************************************************************************

    //Huýt sáo *****************************************************************
    //Hưởng huýt sáo
    public void setStartHuytSao(Player player, int tiLeHP) {
        player.effectSkill.tiLeHPHuytSao = tiLeHP;
        player.effectSkill.lastTimeHuytSao = System.currentTimeMillis();
         Service.getInstance().point(player);
        Service.getInstance().Send_Info_NV(player);
    }

    //Hết hiệu ứng huýt sáo
    public void removeHuytSao(Player player) {
        player.effectSkill.tiLeHPHuytSao = 0;
        sendEffectPlayer(player, player, TURN_OFF_EFFECT, HUYT_SAO_EFFECT);
        Service.getInstance().point(player);
        Service.getInstance().Send_Info_NV(player);
    }

    //**************************************************************************
    //Biến khỉ *****************************************************************
    //Bắt đầu biến khỉ
    public void setIsMonkey(Player player) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(EffectSkillService.class.getName()).log(Level.SEVERE, null, ex);
        }
        int timeMonkey = SkillUtil.getTimeMonkey(player.playerSkill.skillSelect.point);
        if (player.setClothes.cadic == 5) {
            timeMonkey *= 5;
        }
        player.effectSkill.isMonkey = true;
        player.effectSkill.timeMonkey = timeMonkey;
        player.effectSkill.lastTimeUpMonkey = System.currentTimeMillis();
        player.effectSkill.levelMonkey = (byte) player.playerSkill.skillSelect.point;
        player.nPoint.setHp(player.nPoint.hp * 1000);
         Service.getInstance().point(player);
        PlayerService.gI().sendInfoHpMp(player);
        Service.getInstance().Send_Info_NV(player);
    }

    public void setIsTranformation(Player player) {
        Util.setTimeout(() -> {
            int timeTranformation = SkillUtil.getTimeTranformation();
            player.isbienhinh = 0;
            PlayerDAO.saveisBienHinh(player);
            player.effectSkill.isTranformation = true;
            player.effectSkill.timeTranformation = timeTranformation;
            player.effectSkill.levelTranformation = (byte) player.playerSkill.skillSelect.point;
            Service.getInstance().sendSpeedPlayer(player, 6);
            sendEffectTranformation(player);
            Service.getInstance().Send_Caitrang(player);
            switch (player.gender) {
                case 0 ->
                    RadaService.getInstance().setIDAuraEff(player, 12);
                case 1 ->
                    RadaService.getInstance().setIDAuraEff(player, 9);
                case 2 ->
                    RadaService.getInstance().setIDAuraEff(player, 12);
                default -> {
                }
            }
            Service.getInstance().point(player);
        }, 2000);
    }

    public void setIsEvolution(Player player) {
        Util.setTimeout(() -> {
            int timeTranformation = SkillUtil.getTimett();
            player.effectSkill.isEvolution = true;
            player.effectSkill.timeEvolution = timeTranformation;
            player.effectSkill.lastTimeEvolution = System.currentTimeMillis();
            EffectSkillService.gI().sendEffectVolution(player);
            Service.getInstance().Send_Caitrang(player);
            switch (player.gender) {
                case 0 -> {
                    switch (player.isbienhinh) {
                        case 1 -> {
                            RadaService.getInstance().setIDAuraEff(player, 11);
                        }
                        case 2 -> {
                            RadaService.getInstance().setIDAuraEff(player, 11);

                        }
                        case 3 -> {
                            RadaService.getInstance().setIDAuraEff(player, 14);

                        }
                        case 4 -> {
                            RadaService.getInstance().setIDAuraEff(player, 10);

                        }
                        case 5 -> {
                            RadaService.getInstance().setIDAuraEff(player, 15);

                        }
                        default -> {
                        }
                    }
                }
                case 1 -> {
                    switch (player.isbienhinh) {
                        case 1 -> {
                            RadaService.getInstance().setIDAuraEff(player, 13);
                        }
                        case 2 -> {
                            RadaService.getInstance().setIDAuraEff(player, 13);

                        }
                        case 3 -> {
                            RadaService.getInstance().setIDAuraEff(player, 14);

                        }
                        case 4 -> {
                            RadaService.getInstance().setIDAuraEff(player, 8);

                        }
                        case 5 -> {
                            RadaService.getInstance().setIDAuraEff(player, 31);

                        }
                        default -> {
                        }
                    }
                }
                case 2 -> {
                    switch (player.isbienhinh) {
                        case 1 -> {
                            RadaService.getInstance().setIDAuraEff(player, 11);
                        }
                        case 2 -> {
                            RadaService.getInstance().setIDAuraEff(player, 11);

                        }
                        case 3 -> {
                            RadaService.getInstance().setIDAuraEff(player, 14);

                        }
                        case 4 -> {
                            RadaService.getInstance().setIDAuraEff(player, 10);

                        }
                        case 5 -> {
                            RadaService.getInstance().setIDAuraEff(player, 15);

                        }
                        default -> {
                        }
                    }
                }
                default -> {
                }
            }
            Service.getInstance().point(player);
        }, 2000);
    }

    public void monkeyDown(Player player) {
        player.effectSkill.isMonkey = false;
        player.effectSkill.levelMonkey = 0;
        if (player.nPoint.hp > player.nPoint.hpMax) {
            player.nPoint.setHp(player.nPoint.hpMax);
        }

        sendEffectEndCharge(player);
        sendEffectMonkey(player);
        Service.getInstance().setNotMonkey(player);
        Service.getInstance().Send_Caitrang(player);
        Service.getInstance().point(player);
        PlayerService.gI().sendInfoHpMp(player);
        Service.getInstance().Send_Info_NV(player);
        Service.getInstance().sendInfoPlayerEatPea(player);
    }
    
    public void TranformationDown(Player player) {
        player.isbienhinh = 0;
        PlayerDAO.saveisBienHinh(player);
        player.effectSkill.isTranformation = false;
        player.effectSkill.levelTranformation = 0;
        if (player.nPoint.hp > player.nPoint.hpMax) {
            player.nPoint.setHp(player.nPoint.hpMax);
        }
        sendEffectEndCharge(player);
        sendEffectTranformation(player);
        Service.getInstance().setNotTranformation(player);
        Service.getInstance().Send_Caitrang(player);
        RadaService.getInstance().setIDAuraEff(player, player.getAura());
        Service.getInstance().point(player);
        PlayerService.gI().sendInfoHpMp(player);
        Service.getInstance().Send_Info_NV(player);
    }

    public void EvolutionDown(Player player) {
        player.isbienhinh = 0;
        PlayerDAO.saveisBienHinh(player);
        player.effectSkill.isEvolution = false;
        if (player.nPoint.hp > player.nPoint.hpMax) {
            player.nPoint.setHp(player.nPoint.hpMax);
        }
        sendEffectEndCharge(player);
        sendEffectVolution(player);
        Service.getInstance().setNotVolution(player);
        Service.getInstance().Send_Caitrang(player);
        RadaService.getInstance().setIDAuraEff(player, player.getAura());
        Service.getInstance().point(player);
        PlayerService.gI().sendInfoHpMp(player);
        Service.getInstance().Send_Info_NV(player);
    }
    //**************************************************************************
    //Tái tạo năng lượng *******************************************************

    public void startCharge(Player player) {
        if (!player.effectSkill.isCharging) {
            player.effectSkill.isCharging = true;
            sendEffectCharge(player);
        }
    }

    public void stopCharge(Player player) {
        player.effectSkill.countCharging = 0;
        player.effectSkill.isCharging = false;;
        sendEffectStopCharge(player);

    }

    //**************************************************************************
    //Khiên năng lượng *********************************************************
    public void setStartShield(Player player) {
        player.effectSkill.isShielding = true;
        player.effectSkill.lastTimeShieldUp = System.currentTimeMillis();
        player.effectSkill.timeShield = SkillUtil.getTimeShield(player.playerSkill.skillSelect.point);
    }

    public void removeShield(Player player) {
        player.effectSkill.isShielding = false;
        sendEffectPlayer(player, player, TURN_OFF_EFFECT, SHIELD_EFFECT);
    }

    public void breakShield(Player player) {
        removeShield(player);
        Service.getInstance().sendThongBao(player, "Kim Cang Bất Hoại đã bị vỡ!");
        ItemTimeService.gI().removeItemTime(player, 3784);
    }

    //**************************************************************************
    public void sendEffectBlindThaiDuongHaSan(Player plUseSkill, List<Player> players, List<Mob> mobs, int timeStun) {
        Message msg;
        try {
            msg = new Message(-45);
            msg.writer().writeByte(0);
            msg.writer().writeInt((int) plUseSkill.id);
            msg.writer().writeShort(plUseSkill.playerSkill.skillSelect.skillId);
            msg.writer().writeByte(mobs.size());
            for (Mob mob : mobs) {
                msg.writer().writeByte(mob.id);
                msg.writer().writeByte(timeStun / 1000);
            }
            msg.writer().writeByte(players.size());
            for (Player pl : players) {
                msg.writer().writeInt((int) pl.id);
                msg.writer().writeByte(timeStun / 1000);
            }
            Service.getInstance().sendMessAllPlayerInMap(plUseSkill, msg);
            msg.cleanup();
        } catch (Exception e) {
            Log.error(EffectSkillService.class, e);
        }
    }

    //hiệu ứng bắt đầu gồng
    public void sendEffectStartCharge(Player player) {
        Skill skill = SkillUtil.getSkillbyId(player, Skill.TAI_TAO_NANG_LUONG);
        Message msg;
        try {
            msg = new Message(-45);
            msg.writer().writeByte(6);
            msg.writer().writeInt((int) player.id);
            msg.writer().writeShort(skill.skillId);
            Service.getInstance().sendMessAllPlayerInMap(player, msg);
            msg.cleanup();
        } catch (Exception e) {
            Log.error(EffectSkillService.class, e);
        }
    }

    //hiệu ứng đang gồng
    public void sendEffectCharge(Player player) {
        Skill skill = SkillUtil.getSkillbyId(player, Skill.TAI_TAO_NANG_LUONG);
        Message msg;
        try {
            msg = new Message(-45);
            msg.writer().writeByte(1);
            msg.writer().writeInt((int) player.id);
            msg.writer().writeShort(skill.skillId);
            Service.getInstance().sendMessAllPlayerInMap(player, msg);
            msg.cleanup();
        } catch (Exception e) {
            Log.error(EffectSkillService.class, e);
        }
    }

    //dừng gồng
    public void sendEffectStopCharge(Player player) {
        try {
            Message msg = new Message(-45);
            msg.writer().writeByte(3);
            msg.writer().writeInt((int) player.id);
            msg.writer().writeShort(-1);
            Service.getInstance().sendMessAllPlayerInMap(player, msg);
            msg.cleanup();
        } catch (Exception e) {
            Log.error(EffectSkillService.class, e);
        }
    }

    //hiệu ứng nổ kết thúc gồng
    public void sendEffectEndCharge(Player player) {
        Message msg;
        try {
            msg = new Message(-45);
            msg.writer().writeByte(5);
            msg.writer().writeInt((int) player.id);
            msg.writer().writeShort(player.playerSkill.skillSelect.skillId);
            Service.getInstance().sendMessAllPlayerInMap(player, msg);
            msg.cleanup();
        } catch (Exception e) {
            Log.error(EffectSkillService.class, e);
        }
    }

    //hiệu ứng biến khỉ
    public void sendEffectMonkey(Player player) {
        Skill skill = SkillUtil.getSkillbyId(player, Skill.BIEN_KHI);
        Message msg;
        try {
            msg = new Message(-45);
            msg.writer().writeByte(6);
            msg.writer().writeInt((int) player.id);
            msg.writer().writeShort(skill.skillId);
            Service.getInstance().sendMessAllPlayerInMap(player, msg);
            msg.cleanup();
        } catch (Exception e) {
            Log.error(EffectSkillService.class, e);
        }
    }

    public void sendEffectTranformation(Player player) {
        Message msg;
        try {
            msg = new Message(-45);
            msg.writer().writeByte(6);
            msg.writer().writeInt((int) player.id);
            msg.writer().writeShort((short) 97);
            Service.getInstance().sendMessAllPlayerInMap(player, msg);
            msg.cleanup();
        } catch (IOException e) {
            Log.error(EffectSkillService.class, e);
        }
    }

    public void sendEffectVolution(Player player) {
        Message msg;
        try {
            msg = new Message(-45);
            msg.writer().writeByte(6);
            msg.writer().writeInt((int) player.id);
            msg.writer().writeShort((short) 97);
            Service.getInstance().sendMessAllPlayerInMap(player, msg);
            msg.cleanup();
        } catch (IOException e) {
            Log.error(EffectSkillService.class, e);
        }
    }

    public void sendMobToMaPhongBa(Player player, Mob mob, int timeMaPhongBaMOB) {
        Message msg;
        try {
            msg = new Message(-112);
            msg.writer().writeByte(1);
            msg.writer().writeByte(mob.id); //mob id
            msg.writer().writeShort(4133); //icon socola
            Service.getInstance().sendMessAllPlayerInMap(player, msg);
            msg.cleanup();
            mob.effectSkill.setMaPhongBaMOB(System.currentTimeMillis(), timeMaPhongBaMOB);
        } catch (Exception e) {
            nro.utils.Log.error(EffectSkillService.class, e);
        }
    }

    public void setMaPhongBa(Player player, long lastTimeMaPhongBa, int timeMaPhongBa) {
        player.effectSkill.lastTimeMaPhongBa = lastTimeMaPhongBa;
        player.effectSkill.timeMaPhongBa = timeMaPhongBa;
        player.effectSkill.isMaPhongBa = true;
    }

    public void removeMaPhongBa(Player player) {
        player.effectSkill.isMaPhongBa = false;
        Service.getInstance().Send_Caitrang(player);
    }
}
