package nro.services;

import nro.consts.ConstAchive;
import nro.consts.ConstPlayer;
import nro.models.intrinsic.Intrinsic;
import nro.models.map.Zone;
import nro.models.mob.Mob;
import nro.models.mob.MobMe;
import nro.models.player.Pet;
import nro.models.player.Player;
import nro.models.pvp.PVP;
import nro.models.skill.Hit;
import nro.models.skill.Skill;
import nro.models.skill.SkillNotFocus;
import nro.server.io.Message;
import nro.services.func.PVPServcice;
import nro.utils.SkillUtil;
import nro.utils.Util;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import nro.jdbc.daos.PlayerDAO;
import nro.models.boss.Boss;
import nro.models.boss.BossFactory;
import nro.models.player.Clone;
import nro.models.player.SkillSpecial;

/**
 * @Build By Arriety
 */
public class SkillService {

    private static SkillService i;

    private SkillService() {

    }

    public static SkillService gI() {
        if (i == null) {
            i = new SkillService();
        }
        return i;
    }

    public boolean useSkill(Player player, Player plTarget, Mob mobTarget, Message message) {
        try {
            if (player.playerSkill.skillSelect == null && player.playerSkill == null && player.playerSkill.skillSelect.template == null) {
                return false;
            }
            if (player.playerSkill.skillSelect.template.type == 2 && canUseSkillWithMana(player) && canUseSkillWithCooldown(player)) {
                useSkillBuffToPlayer(player, plTarget);
                return true;
            }
            if ((player.effectSkill.isHaveEffectSkill()
                    && (player.playerSkill.skillSelect.template.id != Skill.TU_SAT
                    && player.playerSkill.skillSelect.template.id != Skill.QUA_CAU_KENH_KHI
                    && player.playerSkill.skillSelect.template.id != Skill.MAKANKOSAPPO))
                    || (plTarget != null && !canAttackPlayer(player, plTarget))
                    || (mobTarget != null && mobTarget.isDie())
                    || !canUseSkillWithMana(player) || !canUseSkillWithCooldown(player)) {
                return false;
            }
            if (player.effectSkill.useTroi) {
                EffectSkillService.gI().removeUseTroi(player);
            }
            if (player.effectSkill.isCharging) {
                EffectSkillService.gI().stopCharge(player);
            }
            if (player.isPet) {
//            ((Pet) player).lastTimeMoveIdle = System.currentTimeMillis();
            }
            byte st = -1;
            byte skillId = -1;
            Short dx = -1;
            Short dy = -1;
            byte dir = -1;
            Short x = -1;
            Short y = -1;

            try {
                st = message.reader().readByte();
                skillId = message.reader().readByte();
                dx = message.reader().readShort();
                dy = message.reader().readShort();
                dir = message.reader().readByte();
                x = message.reader().readShort();
                y = message.reader().readShort();
            } catch (Exception e) {

            }
            if (st == 20 && skillId != player.playerSkill.skillSelect.template.id) {
                selectSkill(player, skillId);
                return false;
            }
            switch (player.playerSkill.skillSelect.template.type) {
                case 1:
                    if (player.clone != null && player.clone.zone != null) {
                        player.clone.playerSkill.skillSelect = player.playerSkill.skillSelect;
                        player.clone.attackWithMaster(plTarget, mobTarget);
                    }
                    useSkillAttack(player, plTarget, mobTarget);
                    break;
                case 3:
                    if (player.clone != null && player.clone.zone != null) {
                        player.clone.playerSkill.skillSelect = player.playerSkill.skillSelect;
                        useSkillAlone(player.clone);
                    }
                    useSkillAlone(player);
                    break;
                case 4:
                    if (player.clone != null && player.clone.zone != null) {
                        player.clone.playerSkill.skillSelect = player.playerSkill.skillSelect;
                        userSkillSpecial(player.clone, st, skillId, dx, dy, dir, x, y);
                    }
                    userSkillSpecial(player, st, skillId, dx, dy, dir, x, y);
                    break;
                default:
                    return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    private void userSkillSpecial(Player player, byte st, byte skillId, short dx, short dy, byte dir, short x, short y) {
        switch (skillId) {
            case Skill.SUPER_KAME:
                if (player.inventory.itemsBody.get(13).isNotNullItem()) {
                    for (int i = 0; i < player.inventory.itemsBody.get(13).itemOptions.size(); i++) {
                        if (player.inventory.itemsBody.get(13).itemOptions.get(i).optionTemplate.id == 243) {
                            if (player.inventory.itemsBody.get(13).itemOptions.get(i).param == 0) {
                                Service.getInstance().sendThongBao(player, "Phục hồi sách hoặc tháo sách ra để dùng skill");
                                return;
                            }
                        }
                    }
                    switch (player.inventory.itemsBody.get(13).template.id) {
                        case 1412:
                            Service.getInstance().SendImgSkill9(skillId, 2);// gửi ảnh tới cilent
                            sendEffSkillSpecialID24(player, dir, 2);
                            break;
                        case 1413:
                            Service.getInstance().SendImgSkill9(skillId, 3);// gửi ảnh tới cilent
                            sendEffSkillSpecialID24(player, dir, 3);
                            break;
                        default:
                            sendEffSkillSpecialID24(player, dir, 0);
                            break;
                    }
                } else {
                    sendEffSkillSpecialID24(player, dir, 0);
                }
                break;
            case Skill.MAFUBA:
                if (player.inventory.itemsBody.get(13).isNotNullItem()) {
                    for (int i = 0; i < player.inventory.itemsBody.get(13).itemOptions.size(); i++) {
                        if (player.inventory.itemsBody.get(13).itemOptions.get(i).optionTemplate.id == 243) {
                            if (player.inventory.itemsBody.get(13).itemOptions.get(i).param == 0) {
                                Service.getInstance().sendThongBao(player, "Phục hồi sách hoặc tháo sách ra để dùng skill");
                                return;
                            }
                        }
                    }
                    switch (player.inventory.itemsBody.get(13).template.id) {
                        case 1414:
                            Service.getInstance().SendImgSkill9(skillId, 2);// gửi ảnh tới cilent
                            sendEffSkillSpecialID26(player, dir, 2);
                            break;
                        case 1415:
                            Service.getInstance().SendImgSkill9(skillId, 3);// gửi ảnh tới cilent
                            sendEffSkillSpecialID26(player, dir, 3);
                            break;
                        default:
                            sendEffSkillSpecialID26(player, dir, 0);
                            break;
                    }
                } else {
                    sendEffSkillSpecialID26(player, dir, 0);
                }
                break;
            case Skill.SUPER_ANTOMIC:
                if (player.inventory.itemsBody.get(13).isNotNullItem()) {
                    for (int i = 0; i < player.inventory.itemsBody.get(13).itemOptions.size(); i++) {
                        if (player.inventory.itemsBody.get(13).itemOptions.get(i).optionTemplate.id == 243) {
                            if (player.inventory.itemsBody.get(13).itemOptions.get(i).param == 0) {
                                Service.getInstance().sendThongBao(player, "Phục hồi sách hoặc tháo sách ra để dùng skill");
                                return;
                            }
                        }
                    }
                    switch (player.inventory.itemsBody.get(13).template.id) {
                        case 1416:
                            Service.getInstance().SendImgSkill9(skillId, 2);// gửi ảnh tới cilent
                            sendEffSkillSpecialID25(player, dir, 2);
                            break;
                        case 1417:
                            Service.getInstance().SendImgSkill9(skillId, 3);// gửi ảnh tới cilent
                            sendEffSkillSpecialID25(player, dir, 3);
                            break;
                        default:
                            sendEffSkillSpecialID25(player, dir, 0);
                            break;
                    }
                } else {
                    sendEffSkillSpecialID25(player, dir, 0);
                }
                break;
        }
        affterUseSkill(player, player.playerSkill.skillSelect.template.id);
        player.skillSpecial.setSkillSpecial(dir, dx, dy, x, y);

    }

    public void updateSkillSpecial(Player player) {
        try {
            if (player.isDie() || player.effectSkill.isHaveEffectSkill()) {
                player.skillSpecial.closeSkillSpecial();
                return;
            }
            if (player.skillSpecial.skillSpecial.template.id == Skill.MAFUBA) {
                if (Util.canDoWithTime(player.skillSpecial.lastTimeSkillSpecial, SkillSpecial.TIME_GONG)) {
                    player.skillSpecial.lastTimeSkillSpecial = System.currentTimeMillis();
                    player.skillSpecial.closeSkillSpecial();
                    Skill curSkill = SkillUtil.getSkillbyId(player, Skill.MAFUBA);
                    int timeBinh = SkillUtil.getTimeBinh(curSkill.point);//thời gian biến thành bình

                    //hút người
                    for (Player playerMap : player.zone.getHumanoids()) {
                        if (playerMap != null && playerMap != player) {
                            if (player.skillSpecial.dir == -1 && !playerMap.isDie() && Util.getDistance(player, playerMap) <= 500 && this.canAttackPlayer(player, playerMap)) {
                                player.skillSpecial.playersTaget.add(playerMap);
                            } else if (player.skillSpecial.dir == 1 && !playerMap.isDie() && Util.getDistance(player, playerMap) <= 500 && this.canAttackPlayer(player, playerMap)) {
                                player.skillSpecial.playersTaget.add(playerMap);
                            }
                        }
                    }

                    //hút quái
                    for (Mob mobMap : player.zone.mobs) {
                        if (player.skillSpecial.dir == -1 && !mobMap.isDie() && Util.getDistance(player, mobMap) <= 500) {
                            player.skillSpecial.mobsTaget.add(mobMap);
                        } else if (player.skillSpecial.dir == 1 && !mobMap.isDie() && Util.getDistance(player, mobMap) <= 500) {
                            player.skillSpecial.mobsTaget.add(mobMap);
                        }
                        if (mobMap == null) {
                            continue;
                        }
                    }

                    //bắt đầu hút
                    if (player.inventory.itemsBody.get(13).isNotNullItem()) {
                        for (int i = 0; i < player.inventory.itemsBody.get(13).itemOptions.size(); i++) {
                            if (player.inventory.itemsBody.get(13).itemOptions.get(i).optionTemplate.id == 243) {
                                player.inventory.itemsBody.get(13).itemOptions.get(i).param -= 1;
                                InventoryService.gI().sendItemBody(player);
                            }
                        }
                        switch (player.inventory.itemsBody.get(13).template.id) {
                            case 1414:
                                this.startSkillSpecialID26(player, 2);
                                break;
                            case 1415:
                                this.startSkillSpecialID26(player, 3);
                                break;
                            default:
                                this.startSkillSpecialID26(player, 0);
                                break;
                        }
                    } else {
                        this.startSkillSpecialID26(player, 0);
                    }

                    //biến quái - bình
                    for (Mob mobMap : player.zone.mobs) {
                        if (!MapService.gI().isMapOffline(player.zone.map.mapId)) {
                            if (player.skillSpecial.dir == -1 && !mobMap.isDie() && Util.getDistance(player, mobMap) <= 500) {
                                player.skillSpecial.mobsTaget.add(mobMap);
                            } else if (player.skillSpecial.dir == 1 && !mobMap.isDie() && Util.getDistance(player, mobMap) <= 500) {
                                player.skillSpecial.mobsTaget.add(mobMap);
                            }
                            if (mobMap == null) {
                                continue;
                            }
                            EffectSkillService.gI().sendMobToMaPhongBa(player, mobMap, timeBinh);//biến mob thành bình
                            this.playerAttackMob(player, mobMap, false, true); // trừ dame 
                        }
                    }

                    //biến người - bình
                    ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

                    for (Player playerMap : player.zone.getHumanoids()) {
                        if (!MapService.gI().isMapOffline(player.zone.map.mapId)) {
                            if (playerMap != null && playerMap != player) {
                                if (player.skillSpecial != null && !playerMap.isDie() && Util.getDistance(player, playerMap) <= 500 && this.canAttackPlayer(player, playerMap)) {
                                    player.skillSpecial.playersTaget.add(playerMap);

                                    if (playerMap != null && playerMap.id != player.id) {
                                        ItemTimeService.gI().sendItemTime(playerMap, 4133, timeBinh / 1000);
                                        EffectSkillService.gI().setMaPhongBa(playerMap, System.currentTimeMillis(), timeBinh);
                                        Service.getInstance().Send_Caitrang(playerMap);

                                        double ptdame = 0;

                                        switch (curSkill.point) {
                                            case 1:
                                            case 2:
                                                ptdame = 0.01;
                                                break;
                                            case 3:
                                            case 4:
                                                ptdame = 0.02;
                                                break;
                                            case 5:
                                            case 6:
                                                ptdame = 0.03;
                                                break;
                                            case 7:
                                            case 8:
                                                ptdame = 0.04;
                                                break;
                                            case 9:
                                                ptdame = 0.06;
                                                break;
                                            default:
                                                ptdame = 0.01;
                                                break;
                                        }

                                        int dameHit = (int) (player.nPoint.hpMax * ptdame);

                                        for (int i = 0; i < timeBinh / 1000; i++) {
                                            final int index = i;
                                            executorService.schedule(() -> {
                                                if (!playerMap.isDie()) {
                                                    PlayerService.gI().subHPPlayer(playerMap, dameHit);
                                                    PlayerService.gI().sendInfoHpMpMoney(playerMap); // Gửi thông tin HP cho người chơi bị nhốt
                                                    Service.getInstance().Send_Info_NV(playerMap);
                                                }
                                            }, index, TimeUnit.SECONDS);
                                        }
                                    }
                                }
                            }
                        } else {
                            if (playerMap != null && playerMap != player) {
                                if (playerMap.id == -player.id) {
                                    if (player.skillSpecial != null && !playerMap.isDie() && Util.getDistance(player, playerMap) <= 500 && this.canAttackPlayer(player, playerMap)) {
                                        player.skillSpecial.playersTaget.add(playerMap);

                                        if (playerMap != null && playerMap.id != player.id) {
                                            ItemTimeService.gI().sendItemTime(playerMap, 4133, timeBinh / 1000);
                                            EffectSkillService.gI().setMaPhongBa(playerMap, System.currentTimeMillis(), timeBinh);
                                            Service.getInstance().Send_Caitrang(playerMap);

                                            double ptdame = 0;

                                            switch (curSkill.point) {
                                                case 1:
                                                case 2:
                                                    ptdame = 0.01;
                                                    break;
                                                case 3:
                                                case 4:
                                                    ptdame = 0.02;
                                                    break;
                                                case 5:
                                                case 6:
                                                    ptdame = 0.03;
                                                    break;
                                                case 7:
                                                case 8:
                                                    ptdame = 0.04;
                                                    break;
                                                case 9:
                                                    ptdame = 0.06;
                                                    break;
                                                default:
                                                    ptdame = 0.01;
                                                    break;
                                            }

                                            int dameHit = (int) (player.nPoint.hpMax * ptdame);

                                            for (int i = 0; i < timeBinh / 1000; i++) {
                                                final int index = i;
                                                executorService.schedule(() -> {
                                                    if (!playerMap.isDie()) {
                                                        PlayerService.gI().subHPPlayer(playerMap, dameHit);
                                                        PlayerService.gI().sendInfoHpMpMoney(playerMap); // Gửi thông tin HP cho người chơi bị nhốt
                                                        Service.getInstance().Send_Info_NV(playerMap);
                                                    }
                                                }, index, TimeUnit.SECONDS);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Sau khi hoàn thành tất cả các tác vụ, hủy bỏ ScheduledExecutorService
                    executorService.shutdown();
                }
            } else {
                // SUPER KAME
                if (player.skillSpecial.stepSkillSpecial == 0 && Util.canDoWithTime(player.skillSpecial.lastTimeSkillSpecial, SkillSpecial.TIME_GONG)) {
                    player.skillSpecial.lastTimeSkillSpecial = System.currentTimeMillis();
                    player.skillSpecial.stepSkillSpecial = 1;
                    if (player.skillSpecial.skillSpecial.template.id == Skill.SUPER_KAME) {
                        if (player.inventory.itemsBody.get(13).isNotNullItem()) {
                            for (int i = 0; i < player.inventory.itemsBody.get(13).itemOptions.size(); i++) {
                                if (player.inventory.itemsBody.get(13).itemOptions.get(i).optionTemplate.id == 243) {
                                    player.inventory.itemsBody.get(13).itemOptions.get(i).param -= 1;
                                    InventoryService.gI().sendItemBody(player);
                                }
                            }
                            switch (player.inventory.itemsBody.get(13).template.id) {
                                case 1412:
                                    this.startSkillSpecialID24(player, 2);
                                    break;
                                case 1413:
                                    this.startSkillSpecialID24(player, 3);
                                    break;
                                default:
                                    this.startSkillSpecialID24(player, 0);
                                    break;
                            }
                        } else {
                            this.startSkillSpecialID24(player, 0);
                        }
                    } else {
                        // CA DIC LIEN HOAN CHUONG
                        if (player.inventory.itemsBody.get(13).isNotNullItem()) {
                            for (int i = 0; i < player.inventory.itemsBody.get(13).itemOptions.size(); i++) {
                                if (player.inventory.itemsBody.get(13).itemOptions.get(i).optionTemplate.id == 243) {
                                    player.inventory.itemsBody.get(13).itemOptions.get(i).param -= 1;
                                    InventoryService.gI().sendItemBody(player);
                                }
                            }
                            switch (player.inventory.itemsBody.get(13).template.id) {
                                case 1416:
                                    this.startSkillSpecialID25(player, 2);
                                    break;
                                case 1417:
                                    this.startSkillSpecialID25(player, 3);
                                    break;
                                default:
                                    this.startSkillSpecialID25(player, 0);
                                    break;
                            }
                        } else {
                            this.startSkillSpecialID25(player, 0);
                        }
                    }
                } else if (player.skillSpecial.stepSkillSpecial == 1 && !Util.canDoWithTime(player.skillSpecial.lastTimeSkillSpecial, SkillSpecial.TIME_END_24_25)) {
                    if (MapService.gI().isMapOffline(player.zone.map.mapId)) {
                        for (Player playerMap : player.zone.getHumanoids()) {
                            if (playerMap != null && playerMap.id == -player.id) {
                                if (player.skillSpecial.dir == -1 && !playerMap.isDie()
                                        && playerMap.location.x <= player.location.x - 15
                                        && Math.abs(playerMap.location.x - player.skillSpecial._xPlayer) <= player.skillSpecial._xObjTaget
                                        && Math.abs(playerMap.location.y - player.skillSpecial._yPlayer) <= player.skillSpecial._yObjTaget
                                        && this.canAttackPlayer(player, playerMap)) {
                                    this.playerAttackPlayer(player, playerMap, false);
                                    PlayerService.gI().sendInfoHpMpMoney(playerMap);
                                }
                                if (player.skillSpecial.dir == 1 && !playerMap.isDie()
                                        && playerMap.location.x >= player.location.x + 15
                                        && Math.abs(playerMap.location.x - player.skillSpecial._xPlayer) <= player.skillSpecial._xObjTaget
                                        && Math.abs(playerMap.location.y - player.skillSpecial._yPlayer) <= player.skillSpecial._yObjTaget
                                        && this.canAttackPlayer(player, playerMap)) {
                                    this.playerAttackPlayer(player, playerMap, false);
                                    PlayerService.gI().sendInfoHpMpMoney(playerMap);
                                }
                            }
                        }
                        return;
                    }
                    for (Player playerMap : player.zone.getHumanoids()) {
                        if (player.skillSpecial.dir == -1 && !playerMap.isDie()
                                && playerMap.location.x <= player.location.x - 15
                                && Math.abs(playerMap.location.x - player.skillSpecial._xPlayer) <= player.skillSpecial._xObjTaget
                                && Math.abs(playerMap.location.y - player.skillSpecial._yPlayer) <= player.skillSpecial._yObjTaget
                                && this.canAttackPlayer(player, playerMap)) {
                            this.playerAttackPlayer(player, playerMap, false);
                            PlayerService.gI().sendInfoHpMpMoney(playerMap);
                        }
                        if (player.skillSpecial.dir == 1 && !playerMap.isDie()
                                && playerMap.location.x >= player.location.x + 15
                                && Math.abs(playerMap.location.x - player.skillSpecial._xPlayer) <= player.skillSpecial._xObjTaget
                                && Math.abs(playerMap.location.y - player.skillSpecial._yPlayer) <= player.skillSpecial._yObjTaget
                                && this.canAttackPlayer(player, playerMap)) {
                            this.playerAttackPlayer(player, playerMap, false);
                            PlayerService.gI().sendInfoHpMpMoney(playerMap);
                        }
                        if (playerMap == null) {
                            continue;
                        }
                    }
                    for (Mob mobMap : player.zone.mobs) {
                        if (player.skillSpecial.dir == -1 && !mobMap.isDie()
                                && mobMap.location.x <= player.skillSpecial._xPlayer - 15
                                && Math.abs(mobMap.location.x - player.skillSpecial._xPlayer) <= player.skillSpecial._xObjTaget
                                && Math.abs(mobMap.location.y - player.skillSpecial._yPlayer) <= player.skillSpecial._yObjTaget) {
                            this.playerAttackMob(player, mobMap, false, false);
                        }
                        if (player.skillSpecial.dir == 1 && !mobMap.isDie()
                                && mobMap.location.x >= player.skillSpecial._xPlayer + 15
                                && Math.abs(mobMap.location.x - player.skillSpecial._xPlayer) <= player.skillSpecial._xObjTaget
                                && Math.abs(mobMap.location.y - player.skillSpecial._yPlayer) <= player.skillSpecial._yObjTaget) {
                            this.playerAttackMob(player, mobMap, false, false);
                        }
                        if (mobMap == null) {
                            continue;
                        }
                    }
                } else if (player.skillSpecial.stepSkillSpecial == 1) {
                    player.skillSpecial.closeSkillSpecial();
                }
            }
        } catch (Exception e) {
        }
    }

    public void sendCurrLevelSpecial(Player player, Skill skill) {
        Message message = null;
        try {
            message = Service.getInstance().messageSubCommand((byte) 62);
            message.writer().writeShort(skill.skillId);
            message.writer().writeByte(0);
            message.writer().writeShort(skill.currLevel);
            player.sendMessage(message);
        } catch (final Exception ex) {
        } finally {
            if (message != null) {
                message.cleanup();
                message = null;
            }
        }
    }

    //============================================================================
    // Skill SuperKame
    public void sendEffSkillSpecialID24(Player player, byte dir, int TypePaintSkill) {
        Message message = null;
        try {
            message = new Message(-45);// passt code k dc vcb 
            message.writer().writeByte(20);
            message.writer().writeInt((int) player.id);
            message.writer().writeShort(24);
            message.writer().writeByte(1);
            message.writer().writeByte(dir); // -1 trai | 1 phai
            message.writer().writeShort(2000);
            message.writer().writeByte(0);
            message.writer().writeByte(TypePaintSkill);// đoạn này là skill paint 
            message.writer().writeByte(0);
            Service.getInstance().sendMessAllPlayerInMap(player, message);
            message.cleanup();
        } catch (Exception e) {
        } finally {
            if (message != null) {
                message.cleanup();
                message = null;
            }
        }
    }

    // Skill liên hoàn chưởng
    public void sendEffSkillSpecialID25(Player player, byte dir, int typeskill) { //Tư thế gồng + hào quang
        Message message = null;
        try {
            message = new Message(-45);// passt code k dc vcb 
            message.writer().writeByte(20);
            message.writer().writeInt((int) player.id);
            message.writer().writeShort(25);
            message.writer().writeByte(2);
            message.writer().writeByte(dir); // -1 trai | 1 phai
            message.writer().writeShort(2000);
            message.writer().writeByte(0);
            message.writer().writeByte(typeskill); // type skill : 0 = defaule, 1,2 = type mới
            message.writer().writeByte(0);
            Service.getInstance().sendMessAllPlayerInMap(player, message);
            message.cleanup();
        } catch (Exception e) {
        } finally {
            if (message != null) {
                message.cleanup();
                message = null;
            }
        }
    }

    // Skill Ma phong ba
    public void sendEffSkillSpecialID26(Player player, byte dir, int typeskill) {
        Message message = null;
        try {
            message = new Message(-45);// passt code k dc vcb 
            message.writer().writeByte(20);
            message.writer().writeInt((int) player.id);
            message.writer().writeShort(26); // id effect
            message.writer().writeByte(3);
            message.writer().writeByte(dir); // -1 trai | 1 phai
            message.writer().writeShort(SkillSpecial.TIME_GONG);
            message.writer().writeByte(0);
            message.writer().writeByte(typeskill);
            message.writer().writeByte(0);
            Service.getInstance().sendMessAllPlayerInMap(player, message);
            message.cleanup();
        } catch (Exception e) {
        } finally {
            if (message != null) {
                message.cleanup();
                message = null;
            }
        }
    }

    public void startSkillSpecialID24(Player player, int TypePaintSkill) {
        Message message = null;
        try {
            message = new Message(-45);
            message.writer().writeByte(21);
            message.writer().writeInt((int) player.id);
            message.writer().writeShort(player.skillSpecial.skillSpecial.template.id);
            message.writer().writeShort(player.skillSpecial._xPlayer + ((player.skillSpecial.dir == -1) ? (-player.skillSpecial._xObjTaget) : player.skillSpecial._xObjTaget));
            message.writer().writeShort(player.skillSpecial._xPlayer);
            message.writer().writeShort(3000); // thời gian skill chưởng chưởng nè
            message.writer().writeShort(player.skillSpecial._yObjTaget);
            message.writer().writeByte(TypePaintSkill);
            message.writer().writeByte(TypePaintSkill);
            message.writer().writeByte(TypePaintSkill);
            Service.getInstance().sendMessAllPlayerInMap(player, message);
            message.cleanup();
        } catch (final Exception ex) {
        } finally {
            if (message != null) {
                message.cleanup();
                message = null;
            }
        }
    }

    public void startSkillSpecialID25(Player player, int typeskill) { // bắt đầu sử dụng skill
        Message message = null;
        try {
            message = new Message(-45);
            message.writer().writeByte(21);
            message.writer().writeInt((int) player.id);
            message.writer().writeShort(player.skillSpecial.skillSpecial.template.id);
            message.writer().writeShort(player.skillSpecial._xPlayer + ((player.skillSpecial.dir == -1) ? (-player.skillSpecial._xObjTaget) : player.skillSpecial._xObjTaget));
            message.writer().writeShort(player.skillSpecial._yPlayer);
            message.writer().writeShort(3000); // thời gian skill chưởng chưởng nè
            message.writer().writeShort(25);
            message.writer().writeByte(typeskill); // skill tung ra : 0 = skill mặc định
            message.writer().writeByte(typeskill); // skill kết : 0 = skill mặc định
            message.writer().writeByte(typeskill); // skill kết : 0 = skill mặc định
            Service.getInstance().sendMessAllPlayerInMap(player, message);
            message.cleanup();
        } catch (final Exception ex) {
        } finally {
            if (message != null) {
                message.cleanup();
                message = null;
            }
        }
    }

    public void startSkillSpecialID26(Player player, int typeskill) {
        Message message = null;
        try {
            message = new Message(-45);
            message.writer().writeByte(21);
            message.writer().writeInt((int) player.id);
            message.writer().writeShort(26);
            message.writer().writeShort(player.skillSpecial._xPlayer + ((player.skillSpecial.dir == -1) ? (-75) : 75));
            message.writer().writeShort(player.skillSpecial._yPlayer);
            message.writer().writeShort(3000);
            message.writer().writeShort(player.skillSpecial._yObjTaget);
            message.writer().writeByte(typeskill);
            final byte size = (byte) (player.skillSpecial.playersTaget.size() + player.skillSpecial.mobsTaget.size());
            message.writer().writeByte(size);
            for (Player playerMap : player.skillSpecial.playersTaget) {
                message.writer().writeByte(1);
                message.writer().writeInt((int) playerMap.id);
            }
            for (Mob mobMap : player.skillSpecial.mobsTaget) {
                message.writer().writeByte(0);
                message.writer().writeByte(mobMap.id);
            }
            message.writer().writeByte(2); // ảnh bình để hút vào : 0 = defaule ; 1 = ảnh cạnh ảnh 0; 2 = ảnh cạnh ảnh 1
            Service.getInstance().sendMessAllPlayerInMap(player, message);
            message.cleanup();
        } catch (final Exception ex) {
        } finally {
            if (message != null) {
                message.cleanup();
                message = null;
            }
        }
    }

    // này hoc5 skill nha
    public void learSkillSpecial(Player player, byte skillID) {
        Message message = null;
        try {
            Skill curSkill = SkillUtil.createSkill(skillID, 1);
            SkillUtil.setSkill(player, curSkill);
            message = Service.getInstance().messageSubCommand((byte) 23);
            message.writer().writeShort(curSkill.skillId);
            player.sendMessage(message);
            message.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (message != null) {
                message.cleanup();
                message = null;
            }

        }
    }

    public static void setEffectFusion(Player pl, int type) {
        Message msg;
        try {
            msg = new Message(125);
            msg.writer().writeByte(type);
            msg.writer().writeInt((int) pl.id);
            Service.getInstance().sendMessAllPlayerInMap(pl, msg);
            msg.cleanup();
        } catch (IOException e) {
        }
    }

//    public void useSkillPhanThan(Player player) {
//        if (!canUseSkillWithCooldown(player)) {
//            return;
//        }
//        if (player.clone != null && player.clone.zone != null) {
//            Service.getInstance().sendThongBao(player, "Phân thân đã được tạo");
//            return;
//        }
//        if (player.clone == null || Util.canDoWithTime(player.clone.lastTimeRevival, 1000)) {
//            setEffectFusion(player,0);
//            Clone.callPoint(player);
//            player.playerSkill.skillSelect.lastTimeUseThisSkill = System.currentTimeMillis();
//            Service.getInstance().sendTimeSkill(player);
//        }
//    }

    public void useSkillTranformation(Player player) {
        if (!canUseSkillWithCooldown(player)) {
            return;
        }
        EffectSkillService.gI().sendEffectTranformation(player);
        EffectSkillService.gI().setIsTranformation(player);
        player.effectSkill.lastTimeTranformation = System.currentTimeMillis();
        affterUseSkill(player, player.playerSkill.skillSelect.template.id);
    }

    public void setSkillEvolution(Player player) {
        if ((player.effectSkill.isTranformation || player.effectSkill.isEvolution)) {
            if (canUseSkillWithCooldown(player, Skill.EVOLUTION)) {
                player.isbienhinh++;
                PlayerDAO.saveisBienHinh(player);
                player.effectSkill.isTranformation = false;
                EffectSkillService.gI().sendEffectVolution(player);
                EffectSkillService.gI().setIsEvolution(player);
                player.lastTimeTranformation = System.currentTimeMillis();
                affterUseSkill(player, player.playerSkill.skillSelect.template.id);
            } else {
                Service.getInstance().sendThongBao(player, "Đợi 3 giây để biến hình tiếp");
            }
        } else {
            Service.getInstance().sendThongBao(player, "Bạn chưa biến hình");
        }
    }

    public void useSkillEvolution(Player player) {

        switch (player.effectSkill.levelTranformation) {
            case 1 -> {
                if (player.isbienhinh == 0) {
                    Service.getInstance().sendThongBao(player, "Skill Biến Hình Phải từ cấp 2 mới có thể tiến hóa");
                }
            }
            case 2 -> {
                if (player.isbienhinh < 1) {
                    setSkillEvolution(player);
                } else {
                    Service.getInstance().sendThongBao(player, "Đã đạt cấp tối đa");
                }
            }
            case 3 -> {
                if (player.isbienhinh < 2) {
                    setSkillEvolution(player);
                } else {
                    Service.getInstance().sendThongBao(player, "Đã đạt cấp tối đa");
                }
            }
            case 4 -> {
                if (player.isbienhinh < 3) {
                    setSkillEvolution(player);
                } else {
                    Service.getInstance().sendThongBao(player, "Đã đạt cấp tối đa");
                }
            }
            case 5 -> {
                if (player.isbienhinh < 4) {
                    setSkillEvolution(player);
                } else {
                    Service.getInstance().sendThongBao(player, "Đã đạt cấp tối đa");
                }
            }
            case 6 -> {
                if (player.isbienhinh < 5) {
                    setSkillEvolution(player);
                } else {
                    Service.getInstance().sendThongBao(player, "Đã đạt cấp tối đa");
                }
            }
            default -> {
            }
        }
    }

    private void useSkillAttack(Player player, Player plTarget, Mob mobTarget) {
        if (!player.isBoss) {
            if (player.isPet) {
                if (player.nPoint.stamina > 0) {
                    player.nPoint.numAttack++;
                    boolean haveCharmPet = ((Pet) player).master.charms.tdDeTu > System.currentTimeMillis();
                    if (haveCharmPet ? player.nPoint.numAttack >= 5 : player.nPoint.numAttack >= 2) {
                        player.nPoint.numAttack = 0;
                        player.nPoint.stamina--;
                    }
                } else {
                    ((Pet) player).askPea();
                    return;
                }
            } else {
                if (player.nPoint.stamina > 0) {
                    if (player.charms.tdDeoDai < System.currentTimeMillis()) {
                        player.nPoint.numAttack++;
                        if (player.nPoint.numAttack == 5) {
                            player.nPoint.numAttack = 0;
                            player.nPoint.stamina--;
                            PlayerService.gI().sendCurrentStamina(player);
                        }
                    }
                } else {
                    Service.getInstance().sendThongBao(player, "Thể lực đã cạn kiệt, hãy nghỉ ngơi để lấy lại sức");
                    return;
                }
            }
        }
        List<Mob> mobs;
        boolean miss = false;
        switch (player.playerSkill.skillSelect.template.id) {
            case Skill.KAIOKEN: //kaioken
                double hpUse = (player.nPoint.hpMax / 100000);
                if (player.nPoint.hp <= hpUse) {
                    break;
                } else {
                   // player.nPoint.setHp(player.nPoint.mp - hpUse);
                    PlayerService.gI().sendInfoHpMpMoney(player);
                    Service.getInstance().Send_Info_NV(player);
                }
            case Skill.DRAGON:
            case Skill.DEMON:
            case Skill.GALICK:
            case Skill.LIEN_HOAN:
            case Skill.KAMEJOKO:
            case Skill.MASENKO:
            case Skill.ANTOMIC:
                if (plTarget != null && Util.getDistance(player, plTarget) > Skill.RANGE_ATTACK_CHIEU_CHUONG) {
                    miss = true;
                }
                if (mobTarget != null && Util.getDistance(player, mobTarget) > Skill.RANGE_ATTACK_CHIEU_CHUONG) {
                    miss = true;
                }
                if (plTarget != null) {
                    playerAttackPlayer(player, plTarget, miss);
                }
                if (mobTarget != null) {
                    playerAttackMob(player, mobTarget, miss, false);
                }
                if (player.mobMe != null) {
                    player.mobMe.attack(plTarget, mobTarget);
                }
                if (player.id >= 0 && !player.playerTask.achivements.isEmpty()) {
                    if (player.playerTask.achivements.size() > ConstAchive.NOI_CONG_CAO_CUONG) {
                        player.playerTask.achivements.get(ConstAchive.NOI_CONG_CAO_CUONG).count++;
                    }
                }
                affterUseSkill(player, player.playerSkill.skillSelect.template.id);
                break;
            //******************************************************************
            case Skill.QUA_CAU_KENH_KHI:
                if (!player.playerSkill.prepareQCKK) {
                    //bắt đầu tụ quả cầu
                    player.playerSkill.prepareQCKK = !player.playerSkill.prepareQCKK;
                    player.playerSkill.lastTimeUseQCKK = System.currentTimeMillis();
                    sendPlayerPrepareSkill(player, 4000);
                } else {
                    //ném cầu
                    player.playerSkill.prepareQCKK = !player.playerSkill.prepareQCKK;
                    mobs = new ArrayList<>();
                    if (plTarget != null) {
                        playerAttackPlayer(player, plTarget, false);
                        for (Mob mob : player.zone.mobs) {
                            if (!mob.isDie()
                                    && Util.getDistance(plTarget, mob) <= SkillUtil.getRangeQCKK(player.playerSkill.skillSelect.point)) {
                                mobs.add(mob);
                            }
                        }
                    }
                    if (player.isBoss && player.id == BossFactory.test1) {
                        for (Player pl : player.zone.getHumanoids()) {
                            if (!pl.isDie()
                                    && Util.getDistance(player, pl) <= SkillUtil.getRangeQCKK(player.playerSkill.skillSelect.point)) {
                                playerAttackPlayer(player, pl, false);
                            }
                        }
                    }
                    if (mobTarget != null) {
                        playerAttackMob(player, mobTarget, false, true);
                        for (Mob mob : player.zone.mobs) {
                            if (!mob.equals(mobTarget) && !mob.isDie()
                                    && Util.getDistance(mob, mobTarget) <= SkillUtil.getRangeQCKK(player.playerSkill.skillSelect.point)) {
                                mobs.add(mob);
                            }
                        }
                    }
                    if (player.isBoss && player.id == BossFactory.test1) {
                        PlayerService.gI().changeAndSendTypePK(player, ConstPlayer.NON_PK);
                        ((Boss) player).changeStatus((byte) 6);
                    }
                    PlayerService.gI().sendInfoHpMpMoney(player);
                    affterUseSkill(player, player.playerSkill.skillSelect.template.id);
                }
                break;
            case Skill.MAKANKOSAPPO:
                if (!player.playerSkill.prepareLaze) {
                    //bắt đầu nạp laze
                    player.playerSkill.prepareLaze = !player.playerSkill.prepareLaze;
                    sendPlayerPrepareSkill(player, 3000);
                } else {
                    //bắn laze
                    player.playerSkill.prepareLaze = !player.playerSkill.prepareLaze;
                    if (plTarget != null) {
                        playerAttackPlayer(player, plTarget, false);
                    }
                    if (mobTarget != null) {
                        playerAttackMob(player, mobTarget, false, true);
//                        mobTarget.attackMob(player, false, true);
                    }
                    affterUseSkill(player, player.playerSkill.skillSelect.template.id);
                }
                PlayerService.gI().sendInfoHpMpMoney(player);
                break;
            case Skill.SOCOLA:
                EffectSkillService.gI().sendEffectUseSkill(player, Skill.SOCOLA);
                int timeSocola = SkillUtil.getTimeSocola();
                if (plTarget != null) {
                    EffectSkillService.gI().setSocola(plTarget, System.currentTimeMillis(), timeSocola);
                    Service.getInstance().Send_Caitrang(plTarget);
                    ItemTimeService.gI().sendItemTime(plTarget, 3780, timeSocola / 1000);
                }
                if (mobTarget != null) {
                    EffectSkillService.gI().sendMobToSocola(player, mobTarget, timeSocola);
                }
                affterUseSkill(player, player.playerSkill.skillSelect.template.id);
                break;
            case Skill.DICH_CHUYEN_TUC_THOI:
                int timeChoangDCTT = SkillUtil.getTimeDCTT(player.playerSkill.skillSelect.point);
                if (plTarget != null) {
                    Service.getInstance().setPos(player, plTarget.location.x, plTarget.location.y);
                    playerAttackPlayer(player, plTarget, miss);
                    EffectSkillService.gI().setBlindDCTT(plTarget, System.currentTimeMillis(), timeChoangDCTT);
                    EffectSkillService.gI().sendEffectPlayer(player, plTarget, EffectSkillService.TURN_ON_EFFECT, EffectSkillService.BLIND_EFFECT);
                    PlayerService.gI().sendInfoHpMpMoney(plTarget);
                    ItemTimeService.gI().sendItemTime(plTarget, 3779, timeChoangDCTT / 1000);
                }
                if (mobTarget != null) {
                    Service.getInstance().setPos(player, mobTarget.location.x, mobTarget.location.y);
                    playerAttackMob(player, mobTarget, false, false);
                    mobTarget.effectSkill.setStartBlindDCTT(System.currentTimeMillis(), timeChoangDCTT);
                    EffectSkillService.gI().sendEffectMob(player, mobTarget, EffectSkillService.TURN_ON_EFFECT, EffectSkillService.BLIND_EFFECT);
                }
                player.nPoint.isCrit100 = true;
                affterUseSkill(player, player.playerSkill.skillSelect.template.id);
                break;
            case Skill.THOI_MIEN:
                EffectSkillService.gI().sendEffectUseSkill(player, Skill.THOI_MIEN);

                int timeSleep = SkillUtil.getTimeThoiMien(player.playerSkill.skillSelect.point);
                if (player.CapTamkjll >= 100 && !player.isPet && !player.isBoss) {
                    timeSleep += (player.CapTamkjll / 100) * 1000L;
                }
                if (plTarget != null) {
                    EffectSkillService.gI().setThoiMien(plTarget, System.currentTimeMillis(), timeSleep);
                    EffectSkillService.gI().sendEffectPlayer(player, plTarget, EffectSkillService.TURN_ON_EFFECT, EffectSkillService.SLEEP_EFFECT);
                    ItemTimeService.gI().sendItemTime(plTarget, 3782, timeSleep / 1000);
                }
                if (mobTarget != null) {
                    mobTarget.effectSkill.setThoiMien(System.currentTimeMillis(), timeSleep);
                    EffectSkillService.gI().sendEffectMob(player, mobTarget, EffectSkillService.TURN_ON_EFFECT, EffectSkillService.SLEEP_EFFECT);
                }
                affterUseSkill(player, player.playerSkill.skillSelect.template.id);
                break;
            case Skill.TROI:
                EffectSkillService.gI().sendEffectUseSkill(player, Skill.TROI);
                int timeHold = SkillUtil.getTimeTroi(player.playerSkill.skillSelect.point);
                EffectSkillService.gI().setUseTroi(player, System.currentTimeMillis(), timeHold);
                if (plTarget != null && (!plTarget.playerSkill.prepareQCKK && !plTarget.playerSkill.prepareLaze && !plTarget.playerSkill.prepareTuSat)) {
                    player.effectSkill.plAnTroi = plTarget;
                    EffectSkillService.gI().sendEffectPlayer(player, plTarget, EffectSkillService.TURN_ON_EFFECT, EffectSkillService.HOLD_EFFECT);
                    EffectSkillService.gI().setAnTroi(plTarget, player, System.currentTimeMillis(), timeHold);
                }
                if (mobTarget != null) {
                    player.effectSkill.mobAnTroi = mobTarget;
                    EffectSkillService.gI().sendEffectMob(player, mobTarget, EffectSkillService.TURN_ON_EFFECT, EffectSkillService.HOLD_EFFECT);
                    mobTarget.effectSkill.setTroi(System.currentTimeMillis(), timeHold);
                }
                affterUseSkill(player, player.playerSkill.skillSelect.template.id);
                break;
        }
        if (!player.isBoss) {
            player.effectSkin.lastTimeAttack = System.currentTimeMillis();
        }
    }

    private void useSkillAlone(Player player) {
        List<Mob> mobs;
        List<Player> players;
        switch (player.playerSkill.skillSelect.template.id) {
            case Skill.THAI_DUONG_HA_SAN:
                int timeStun = SkillUtil.getTimeStun(player.playerSkill.skillSelect.point);
                if (player.CapTamkjll >= 100 && !player.isPet && !player.isBoss) {
                    timeStun += (player.CapTamkjll / 100) * 1000L;
                }
                if (player.setClothes.thienXinHang == 5) {
                    timeStun *= 2;
                }
                mobs = new ArrayList<>();
                players = new ArrayList<>();
                if (player == null || player.zone == null) {
                    break;
                }
                if (!player.zone.map.isMapOffline) {
                    List<Player> playersMap = player.zone.getHumanoids();
                    for (Player pl : playersMap) {
                        if (pl != null && !player.equals(pl)) {
                            if (!pl.nPoint.khangTDHS) {
                                int distance = Util.getDistance(player, pl);
                                int rangeStun = SkillUtil.getRangeStun(player.playerSkill.skillSelect.point);
                                if (distance <= rangeStun && canAttackPlayer(player, pl)) {
                                    if (player.isPet && ((Pet) player).master.equals(pl)) {
                                        continue;
                                    }
                                    EffectSkillService.gI().startStun(pl, System.currentTimeMillis(), timeStun);
                                    if (pl.typePk != ConstPlayer.NON_PK) {
                                        players.add(pl);
                                    }
                                }
                            } else {
                                Service.getInstance().chat(pl, "Lew Lew Lew đòi choáng");
                            }
                        }
                    }
                }
                for (Mob mob : player.zone.mobs) {
                    if (Util.getDistance(player, mob) <= SkillUtil.getRangeStun(player.playerSkill.skillSelect.point)) {
                        mob.effectSkill.startStun(System.currentTimeMillis(), timeStun);
                        mobs.add(mob);
                    }
                }
                EffectSkillService.gI().sendEffectBlindThaiDuongHaSan(player, players, mobs, timeStun);
                affterUseSkill(player, player.playerSkill.skillSelect.template.id);
                break;

            case Skill.DE_TRUNG:
                EffectSkillService.gI().sendEffectUseSkill(player, Skill.DE_TRUNG);
                if (player.mobMe != null) {
                    player.mobMe.mobMeDie();
                }
                player.mobMe = new MobMe(player);
                affterUseSkill(player, player.playerSkill.skillSelect.template.id);
                break;
            case Skill.BIEN_KHI:
                EffectSkillService.gI().sendEffectMonkey(player);
                EffectSkillService.gI().setIsMonkey(player);
                EffectSkillService.gI().sendEffectMonkey(player);

                Service.getInstance().sendSpeedPlayer(player, 0);
                Service.getInstance().Send_Caitrang(player);
                Service.getInstance().sendSpeedPlayer(player, -1);
                if (!player.isPet) {
                    PlayerService.gI().sendInfoHpMp(player);
                }
                Service.getInstance().point(player);
                Service.getInstance().Send_Info_NV(player);
                Service.getInstance().sendInfoPlayerEatPea(player);
                affterUseSkill(player, player.playerSkill.skillSelect.template.id);
                break;
            case Skill.KHIEN_NANG_LUONG:
                EffectSkillService.gI().setStartShield(player);

                EffectSkillService.gI().sendEffectPlayer(player, player, EffectSkillService.TURN_ON_EFFECT, EffectSkillService.SHIELD_EFFECT);
                ItemTimeService.gI().sendItemTime(player, 3784, player.effectSkill.timeShield / 1000);
                affterUseSkill(player, player.playerSkill.skillSelect.template.id);
                break;
            case Skill.HUYT_SAO:
                int tileHP = SkillUtil.getPercentHPHuytSao(player.playerSkill.skillSelect.point);
                if (player.zone != null) {
                    if (!player.zone.map.isMapOffline) {
                        List<Player> playersMap = player.zone.getHumanoids();
                        for (Player pl : playersMap) {
                            if (pl != null && pl.effectSkill != null && pl.effectSkill.useTroi) {
                                continue;
                            }
                            if (pl != null && pl.effectSkill != null && pl.effectSkill.useTroi) {
                                EffectSkillService.gI().removeUseTroi(pl);
                            }
                            if (pl != null && pl.effectSkill != null) {
                                if (!pl.isBoss && pl.gender != ConstPlayer.NAMEC && player.cFlag == pl.cFlag) {
                                    EffectSkillService.gI().setStartHuytSao(pl, tileHP);
                                    EffectSkillService.gI().sendEffectPlayer(pl, pl, EffectSkillService.TURN_ON_EFFECT, EffectSkillService.HUYT_SAO_EFFECT);
                                    pl.nPoint.calPoint();
                                    pl.nPoint.setHp(pl.nPoint.hp + (int) ((long) pl.nPoint.hp * tileHP / 100));
                                    Service.getInstance().point(pl);
                                    Service.getInstance().Send_Info_NV(pl);
                                    ItemTimeService.gI().sendItemTime(pl, 3781, 30);
                                    PlayerService.gI().sendInfoHpMp(pl);
                                }
                            }
                        }
                    } else {
                        EffectSkillService.gI().setStartHuytSao(player, tileHP);
                        EffectSkillService.gI().sendEffectPlayer(player, player, EffectSkillService.TURN_ON_EFFECT, EffectSkillService.HUYT_SAO_EFFECT);
                        player.nPoint.calPoint();
                        player.nPoint.setHp(player.nPoint.hp + (int) ((long) player.nPoint.hp * tileHP / 100));
                        Service.getInstance().point(player);
                        Service.getInstance().Send_Info_NV(player);
                        ItemTimeService.gI().sendItemTime(player, 3781, 30);
                        PlayerService.gI().sendInfoHpMp(player);
                    }
                }
                affterUseSkill(player, player.playerSkill.skillSelect.template.id);
                break;

            case Skill.TAI_TAO_NANG_LUONG:
                EffectSkillService.gI().startCharge(player);
                affterUseSkill(player, player.playerSkill.skillSelect.template.id);
                break;
            case Skill.TU_SAT:

                if (!player.playerSkill.prepareTuSat) {
                    player.playerSkill.prepareTuSat = !player.playerSkill.prepareTuSat;
                    player.playerSkill.lastTimePrepareTuSat = System.currentTimeMillis();
                    sendPlayerPrepareBom(player, 2000);
                } else {
                    if (!player.isBoss && !Util.canDoWithTime(player.playerSkill.lastTimePrepareTuSat, 1500)) {
                        player.playerSkill.skillSelect.lastTimeUseThisSkill = System.currentTimeMillis();
                        player.playerSkill.prepareTuSat = false;
                        return;
                    }
                    player.playerSkill.prepareTuSat = !player.playerSkill.prepareTuSat;
                    int rangeBom = SkillUtil.getRangeBom(player.playerSkill.skillSelect.point);
                    double dame = player.nPoint.hpMax; //dame bom
                    int set = player.nPoint.tangstbom;
                    dame += dame * set / 100;
                    for (Mob mob : player.zone.mobs) {
                        if (Util.getDistance(player, mob) <= rangeBom) {
                            mob.injured(player, dame, true);
                        }
                    }
                    List<Player> playersMap = null;
                    if (player.zone != null && player.isBoss) {
                        playersMap = player.zone.getNotBosses();
                    } else if (player != null && player.zone != null) {
                        playersMap = player.zone.getHumanoids();
                    }
                    if (player.zone != null && player.zone.map != null && !MapService.gI().isMapOffline(player.zone.map.mapId)) {
                        List<Player> playersToInjure = new ArrayList<>();
                        for (Player pl : playersMap) {
                            if (pl != null && !player.equals(pl) && canAttackPlayer(player, pl)) {
                                playersToInjure.add(pl);
                            }
                        }
                        for (Player pl : playersToInjure) {

                            pl.injured(player, pl.isBoss ? (dame / 100) * 100 : dame, false, false);
                            PlayerService.gI().sendInfoHpMpMoney(pl);
                            Service.getInstance().Send_Info_NV(pl);
                        }
                    }
                    if (player != null && player.playerSkill != null && player.playerSkill.skillSelect.template != null) {
                        affterUseSkill(player, player.playerSkill.skillSelect.template.id);
                    }
                    player.injured(null, 2100000000, true, false);
                    if (player != null && player.effectSkill != null) {
                        if (player.effectSkill.tiLeHPHuytSao != 0) {
                            player.effectSkill.tiLeHPHuytSao = 0;
                            EffectSkillService.gI().removeHuytSao(player);
                        }
                    }
                }
                break;
        }
        if (player.playerTask.achivements.size()
                > 0) {
            player.playerTask.achivements.get(ConstAchive.KY_NANG_THANH_THAO).count++;
        }
    }

    private void useSkillBuffToPlayer(Player player, Player plTarget) {
        switch (player.playerSkill.skillSelect.template.id) {
            case Skill.TRI_THUONG:
                List<Player> players = new ArrayList();
                int percentTriThuong = SkillUtil.getPercentTriThuong(player.playerSkill.skillSelect.point);
                if (canHsPlayer(player, plTarget)) {
                    players.add(plTarget);
                    List<Player> playersMap = player.zone.getNotBosses();
                    for (int i = playersMap.size() - 1; i >= 0; i--) {
                        Player pl = playersMap.get(i);
                        if (!pl.equals(plTarget)) {
                            if (canHsPlayer(player, plTarget) && Util.getDistance(player, pl) <= 300) {
                                players.add(pl);
                            }
                        }
                    }
                    playerAttackPlayer(player, plTarget, false);
                    for (Player pl : players) {
                        boolean isDie = pl.isDie();
                        double hpHoi = pl.nPoint.hpMax * percentTriThuong / 100;
                        double mpHoi = pl.nPoint.mpMax * percentTriThuong / 100;
                        pl.nPoint.addHp2(hpHoi);
                        pl.nPoint.addMp2(mpHoi);
                        if (isDie) {
                            Service.getInstance().hsChar(pl, hpHoi, mpHoi);
                            PlayerService.gI().sendInfoHpMp(pl);
                        } else {
                            Service.getInstance().Send_Info_NV(pl);
                            PlayerService.gI().sendInfoHpMp(pl);
                        }
                    }
                    double hpHoiMe = player.nPoint.hp * percentTriThuong / 100;
                    player.nPoint.addHp2(hpHoiMe);
                    PlayerService.gI().sendInfoHp(player);
                }
                affterUseSkill(player, player.playerSkill.skillSelect.template.id);
                break;
        }
    }

    private void phanSatThuong(Player plAtt, Player plTarget, double dame) {
        if (plAtt != null) {
            return;
        }
        double percentPST = plTarget.nPoint.tlPST;
        if (percentPST != 0) {
            double damePST = dame * percentPST / 100;
            Message msg;
            try {
                msg = new Message(56);
                msg.writer().writeInt((int) plAtt.id);
                if (damePST >= plAtt.nPoint.hp) {
                    damePST = plAtt.nPoint.hp - 1;
                }
                damePST = plAtt.injured(null, damePST, true, false);
                msg.writer().writeDouble(plAtt.nPoint.hp);
                msg.writer().writeDouble(damePST);
                msg.writer().writeBoolean(false);
                msg.writer().writeByte(36);
                Service.getInstance().sendMessAllPlayerInMap(plAtt, msg);
                msg.cleanup();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void hutHPMP(Player player, double dame, boolean attackMob) {
        double tiLeHutHp = player.nPoint.getTiLeHutHp();
        double tiLeHutMp = player.nPoint.getTiLeHutMp();
        double hpHoi = dame * tiLeHutHp / 100;
        double mpHoi = dame * tiLeHutMp / 100;
        if (hpHoi > 0 || mpHoi > 0) {
            PlayerService.gI().hoiPhuc(player, hpHoi, mpHoi);
        }
    }

    private void playerAttackPlayer(Player plAtt, Player plInjure, boolean miss) {
        if (plInjure.effectSkill.anTroi) {
            plAtt.nPoint.isCrit100 = true;
        }
        double dameHit = plInjure.injured(plAtt, miss ? 0 : plAtt.nPoint.getDameAttack(false), false, false);
        //   System.out.println("dameHit in playerAttackPlayer: " + Util.numberToMoney(dameHit)); 

        phanSatThuong(plAtt, plInjure, dameHit);
        hutHPMP(plAtt, dameHit, false);
        Message msg;
        try {
            msg = new Message(-60);
            msg.writer().writeInt((int) plAtt.id); //id pem
            msg.writer().writeByte(plAtt.playerSkill.skillSelect.skillId); //skill pem
            msg.writer().writeByte(1); //số người pem
            msg.writer().writeInt((int) plInjure.id); //id ăn pem
            byte typeSkill = SkillUtil.getTyleSkillAttack(plAtt.playerSkill.skillSelect);
            msg.writer().writeByte(typeSkill == 2 ? 0 : 1); //read continue
            msg.writer().writeByte(typeSkill); //type skill
            msg.writer().writeDouble(dameHit); //dame ăn
            msg.writer().writeBoolean(plInjure.isDie()); //is die
            msg.writer().writeBoolean(plAtt.nPoint.isCrit); //crit
            if (typeSkill != 1) {
                Service.getInstance().sendMessAllPlayerInMap(plAtt, msg);
                msg.cleanup();
            } else {
                plInjure.sendMessage(msg);
                msg.cleanup();
                msg = new Message(-60);
                msg.writer().writeInt((int) plAtt.id); //id pem
                msg.writer().writeByte(plAtt.playerSkill.skillSelect.skillId); //skill pem
                msg.writer().writeByte(1); //số người pem
                msg.writer().writeInt((int) plInjure.id); //id ăn pem
                msg.writer().writeByte(typeSkill == 2 ? 0 : 1); //read continue
                msg.writer().writeByte(0); //type skill
                msg.writer().writeDouble(dameHit); //dame ăn
                msg.writer().writeBoolean(plInjure.isDie()); //is die
                msg.writer().writeBoolean(plAtt.nPoint.isCrit); //crit
                Service.getInstance().sendMessAnotherNotMeInMap(plInjure, msg);

                msg.cleanup();
            }
            if (dameHit > 100000000000000000d) {
                Service.getInstance().sendThongBaoBenDuoi("Người chơi " + plAtt.name + " đã dùng  " + plAtt.playerSkill.skillSelect.template.name + " với sát thương " + Util.numberToMoney(dameHit));
          //  }
//            double percentHp = (double) plInjure.nPoint.hp / plInjure.nPoint.hpMax * 100; // Tính % máu còn lại
//            String formattedHp = Util.getFormatNumber(plInjure.nPoint.hp); // Định dạng số HP
//            String formattedPercentHp = String.format("%.1f%%", percentHp); // Định dạng % máu
//
//            if (plInjure != null && !plInjure.isDie() && plInjure.nPoint.hp > 1
//                    && Util.canDoWithTime(plInjure.lasttimechat, 5000)) {
//                plInjure.lasttimechat = System.currentTimeMillis();
//                Service.gI().chat(
//                        plInjure,
//                        "|7|Hp Còn: " + formattedHp + " (" + formattedPercentHp + ")"
//                        + " \n|4|Giết Tao Đi, Đồ Ngu, Đồ Ăn Hại, Chết Cụ Mày Đi"
               // );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (plAtt.isPl() && !plAtt.isDie()) {
            plAtt.Nametc = "Mục tiêu: " + plInjure.name;
            plAtt.hptc = plInjure.nPoint.hp;
            plAtt.dametc = dameHit;
            SetTamkjll(plAtt);
            SetTamkjllHieuungnhan(plAtt);
        }

        try {
            msg = Service.getInstance().messageSubCommand((byte) 14);
            msg.writer().writeInt((int) plInjure.id);
            msg.writer().writeDouble((plInjure.nPoint.hp));
            msg.writer().writeByte(0);
            msg.writer().writeDouble((plInjure.nPoint.hpMax));
            Service.getInstance().sendMessAnotherNotMeInMap(plInjure, msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    private void playerAttackMob(Player plAtt, Mob mob, boolean miss, boolean dieWhenHpFull) {
        if (!mob.isDie()) {
            if (plAtt.effectSkin.isVoHinh) {
                plAtt.effectSkin.isVoHinh = false;
            }
            double dameHit = plAtt.nPoint.getDameAttack(true);
            if (plAtt.charms.tdBatTu > System.currentTimeMillis() && plAtt.nPoint.hp == 1) {
                dameHit = 0;
            }
            if (plAtt.charms.tdManhMe > System.currentTimeMillis()) {
                dameHit += (dameHit * 150 / 100);
            }
            if (plAtt.isPet) {
                if (((Pet) plAtt).charms.tdDeTu > System.currentTimeMillis()) {
                    dameHit *= 2;
                }
            }
            if (miss) {
                dameHit = 0;
            }
            hutHPMP(plAtt, dameHit, true);
            sendPlayerAttackMob(plAtt, mob);
            mob.injured(plAtt, dameHit, dieWhenHpFull);
            if (plAtt.isPl() && !plAtt.isDie()) {
                plAtt.Nametc = "Mục tiêu: quái";
                plAtt.hptc = mob.point.getHP();
                plAtt.dametc = dameHit;
                SetTamkjll(plAtt);
                SetTamkjllHieuungnhan(plAtt);
            }
        }
    }

    private void SetTamkjll(Player p) {
        if (p != null) {
            int lb = Util.nextInt(0, 20);
            int ETk = Util.nextInt(1, 500);
            // ETk *= 2;
            if (p.TamkjllDauLaDaiLuc[11] == 1) {
                ETk += ETk * (p.TamkjllDauLaDaiLuc[12] / 5 >= 20 ? 20
                        : p.TamkjllDauLaDaiLuc[12] / 5) / 100;
            }
            if (p.CapTamkjll >= 500) {
                ETk += ETk * 1 / 100;
            }
            if (p.nPoint.TamkjllLucky) {
                ETk += ETk * 10 / 100;
            }
            ETk += Util.nextInt(0, lb);
            if (p.itemTime.isXexpTamkjll1_5) {
                ETk *= 1;
            }
            if (p.itemTime.isXexpTamkjll2) {
                ETk *= 1;
            }
            if (p.itemTime.isXexpTamkjll3) {
                ETk *= 2;
            }
            if (p.itemTime.isXexpTamkjll4) {
                ETk *= 3;
            }
            if (p.itemTime.isXexpTamkjll5) {
                ETk *= 4;
            }
            if (p.itemTime.isXexpTamkjll6) {
                ETk *= 5;
            }
            if (p.clan != null) {
                p.clan.addExptamkjllClan(p, ETk / 3);
            }
            p.ExpTamkjll += ETk;
            p.ctk = ETk;
            long dk = p.CapTamkjll + 1;
            dk *= 500000L;
            if (p.autonangcaptamkjll) {
                if (dk > 1 && p.ExpTamkjll > dk && p.CapTamkjll < 500) {
                    p.ExpTamkjll -= dk;
                    p.CapTamkjll++;
                    Service.getInstance().sendThongBaoAllPlayer("Bạn\n [" + p.name + "]\n"
                            + " Tiên Bang lên 1 cấp.\n|7| Cấp hiện tại: [" + p.CapTamkjll + "]");
                }
            }
        }
    }

    private void SetTamkjllHieuungnhan(Player p) {
        if (!p.isPl()) {
            return;
        }
        if (p.CapTamkjll >= 1000) {
            if (Util.isTrue(0.01f, 10000)) {
                int tngnhan = Util.nextInt(1, 4);
                int tnhan = (p.CapTamkjll + 1) * 2;
                if (tngnhan == 1) {
                    p.nPoint.hpg += tnhan;
                    Service.getInstance().sendThongBao(p, "bạn đc " + tnhan + " Hp gốc, Do hiệu ứng Tiên Bang");
                    Service.getInstance().sendThongBaoBenDuoi("Trong Lúc Thằng " + p.name + " Tu Luyện Đã Nhận Được " + tnhan + " Hp gốc, Do hiệu ứng Tiên Bang");
                } else if (tngnhan == 2) {
                    p.nPoint.mpg += tnhan;
                    Service.getInstance().sendThongBao(p, "bạn đc " + tnhan + " Ki gốc, Do hiệu ứng Tiên Bang");
                    Service.getInstance().sendThongBaoBenDuoi("Trong Lúc Thằng " + p.name + " Tu Luyện Đã Nhận Được " + tnhan + " Ki gốc, Do hiệu ứng Tiên Bang");
                } else if (tngnhan == 3) {
                    p.nPoint.dameg += tnhan;
                    Service.getInstance().sendThongBao(p, "bạn đc " + tnhan + " Dame gốc, Do hiệu ứng Tiên Bang");
                    Service.getInstance().sendThongBaoBenDuoi("Trong Lúc Thằng " + p.name + " Tu Luyện Đã Nhận Được " + tnhan + " Dame gốc, Do hiệu ứng Tiên Bang");
                } else if (tngnhan == 4) {
                    p.nPoint.defg += tnhan;
                    Service.getInstance().sendThongBao(p, "bạn đc " + tnhan + " giáp gốc, Do hiệu ứng Tiên Bang");
                    Service.getInstance().sendThongBaoBenDuoi("Trong Lúc Thằng " + p.name + " Tu Luyện Đã Nhận Được " + tnhan + " giáp gốc, Do hiệu ứng Tiên Bang");

                }
            }
        }
        if (p.CapTamkjll >= 1000) {
            if (Util.isTrue(1f, 100)) {
                int tngnhan = Util.nextInt(1, 5);
                int tnhan = p.CapTamkjll;
                if (tngnhan == 1) {
                    p.inventory.gem += tnhan;
                    Service.getInstance().sendThongBao(p, "Trong Lúc Bạn Tu Luyện Đã Nhận Được " + tnhan + " Ngọc Xanh, Từ hiệu ứng Tiên Bang");
                    Service.getInstance().sendThongBaoBenDuoi("Trong Lúc Thằng " + p.name + " Tu Luyện Đã Nhận Được " + tnhan + "  Ngọc Xanh, Từ hiệu ứng Tiên Bang");
                } else if (tngnhan == 2) {
                    p.inventory.ruby += tnhan;
                    Service.getInstance().sendThongBao(p, "Trong Lúc Bạn Tu Luyện Đã Nhận Được " + tnhan + " Hồng Ngọc, Từ hiệu ứng Tiên Bang");
                    Service.getInstance().sendThongBaoBenDuoi("Trong Lúc Thằng " + p.name + " Tu Luyện Đã Nhận Được " + tnhan + " Hồng Ngọc, Từ hiệu ứng Tiên Bang");
                } else if (tngnhan == 3) {
                    p.inventory.gold += tnhan;
                    Service.getInstance().sendThongBao(p, "Trong Lúc Bạn Tu Luyện Đã Nhận Được " + tnhan + " gold, Từ hiệu ứng Tiên Bang");
                    Service.getInstance().sendThongBaoBenDuoi("Trong Lúc Thằng " + p.name + " Tu Luyện Đã Nhận Được " + tnhan + "  gold, Từ hiệu ứng Tiên Bang");
                } else if (tngnhan == 4) {
                    p.DLbTamkjll += tnhan;
                    Service.getInstance().sendThongBao(p, "Trong Lúc Bạn Tu Luyện Đã Nhận Được " + tnhan + " Exp Nhập Ma, Từ hiệu ứng Tiên Bang");
                    Service.getInstance().sendThongBaoBenDuoi("Trong Lúc Thằng " + p.name + " Tu Luyện Đã Nhận Được " + tnhan + " Exp Nhập Ma, Từ hiệu ứng Tiên Bang");
                } else if (tngnhan == 5) {
                    p.diemfam += tnhan;
                    Service.getInstance().sendThongBao(p, "Trong Lúc Bạn Tu Luyện Đã Nhận Được " + tnhan + " Điểm Farm, Từ hiệu ứng Tiên Bang");
                    Service.getInstance().sendThongBaoBenDuoi("Trong Lúc Thằng " + p.name + " Tu Luyện Đã Nhận Được " + tnhan + " Điểm Farm, Từ hiệu ứng Tiên Bang Vip");
                }
                Service.getInstance().sendMoney(p);
            }
        }
    }

    public void sendPlayerPrepareSkill(Player player, int affterMiliseconds) {
        Message msg;
        try {
            msg = new Message(-45);
            msg.writer().writeByte(4);
            msg.writer().writeInt((int) player.id);
            msg.writer().writeShort(player.playerSkill.skillSelect.skillId);
            msg.writer().writeShort(affterMiliseconds);
            Service.getInstance().sendMessAllPlayerInMap(player, msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendPlayerPrepareBom(Player player, int affterMiliseconds) {
        Message msg;
        try {
            msg = new Message(-45);
            msg.writer().writeByte(7);
            msg.writer().writeInt((int) player.id);
//            msg.writer().writeShort(player.playerSkill.skillSelect.skillId);
            msg.writer().writeShort(104);
            msg.writer().writeShort(affterMiliseconds);
            Service.getInstance().sendMessAllPlayerInMap(player, msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean canUseSkillWithMana(Player player) {
        if (player.playerSkill.skillSelect != null) {
            if (player.playerSkill.skillSelect.template.id == Skill.KAIOKEN) {
                double hpUse = (player.nPoint.hpMax / 1000000);
                if (player.nPoint.hp <= hpUse) {
                    return false;
                }
            }
            switch (player.playerSkill.skillSelect.template.manaUseType) {
                case 0:
                    if (player.nPoint.mp >= player.playerSkill.skillSelect.manaUse) {
                        return true;
                    } else {
                        return false;
                    }
                case 1:
                    int mpUse = (int) (player.nPoint.mpMax * player.playerSkill.skillSelect.manaUse / 100000);
                    if (player.nPoint.mp >= mpUse) {
                        return true;
                    } else {
                        return false;
                    }
                case 2:
                    if (player.nPoint.mp > 0) {
                        return true;
                    } else {
                        return false;
                    }
                default:
                    return false;
            }
        } else {
            return false;
        }
    }

    public boolean canUseSkillWithCooldown(Player player) {
        if (player.isClone) {
            player.playerSkill.skillSelect = ((Clone) player).mainPl.playerSkill.skillSelect;
        }
        return Util.canDoWithTime(player.playerSkill.skillSelect.lastTimeUseThisSkill,
                player.playerSkill.skillSelect.coolDown);
    }

    private boolean canUseSkillWithCooldown(Player player, int skillId) {
        long cooldownTime = 300000;
        long cooldownTime1 = 3000;
        long currentTime = System.currentTimeMillis();
        if (skillId == Skill.SUPER_TRANFORMATION) {
            return (currentTime - player.effectSkill.lastTimeTranformation) > cooldownTime;
        } else if (skillId == Skill.EVOLUTION) {
            return (currentTime - player.effectSkill.lastTimeEvolution) >= cooldownTime1;
        }
        return false;
    }

    private void affterUseSkill(Player player, int skillId) {
        Intrinsic intrinsic = player.playerIntrinsic.intrinsic;
        switch (skillId) {
            case Skill.DICH_CHUYEN_TUC_THOI:
                if (intrinsic.id == 6) {
                    player.nPoint.dameAfter = intrinsic.param1;
                }
                break;
            case Skill.THOI_MIEN:
                if (intrinsic.id == 7) {
                    player.nPoint.dameAfter = intrinsic.param1;
                }
                break;
            case Skill.SOCOLA:
                if (intrinsic.id == 14) {
                    player.nPoint.dameAfter = intrinsic.param1;
                }
                break;
            case Skill.TROI:
                if (intrinsic.id == 22) {
                    player.nPoint.dameAfter = intrinsic.param1;
                }
                break;
        }
        setMpAffterUseSkill(player);
        setLastTimeUseSkill(player, skillId);
    }

    private void setMpAffterUseSkill(Player player) {
        if (player.playerSkill.skillSelect != null) {
            switch (player.playerSkill.skillSelect.template.manaUseType) {
                case 0:
                    if (player.nPoint.mp >= player.playerSkill.skillSelect.manaUse) {
                        player.nPoint.setMp(player.nPoint.mp - player.playerSkill.skillSelect.manaUse);
                    }
                    break;
                case 1:
                    double mpUse = (player.nPoint.mpMax * player.playerSkill.skillSelect.manaUse / 100);
                    if (player.nPoint.mp >= mpUse) {
                        player.nPoint.setMp(player.nPoint.mp - mpUse);
                    }
                    break;
                case 2:
                    player.nPoint.setMp(0);
                    break;
            }
            PlayerService.gI().sendInfoHpMpMoney(player);
        }
    }

    private void setLastTimeUseSkill(Player player, int skillId) {
        Intrinsic intrinsic = player.playerIntrinsic.intrinsic;
        int subTimeParam = 0;
        switch (skillId) {
            case Skill.TRI_THUONG:
                if (intrinsic.id == 10) {
                    subTimeParam = intrinsic.param1;
                }
                break;
            case Skill.THAI_DUONG_HA_SAN:
                if (intrinsic.id == 3) {
                    subTimeParam = intrinsic.param1;
                }
                break;
            case Skill.QUA_CAU_KENH_KHI:
                if (intrinsic.id == 4) {
                    subTimeParam = intrinsic.param1;
                }
                break;
            case Skill.KHIEN_NANG_LUONG:
                if (intrinsic.id == 5 || intrinsic.id == 15 || intrinsic.id == 20) {
                    subTimeParam = intrinsic.param1;
                }
                break;
            case Skill.MAKANKOSAPPO:
                if (intrinsic.id == 11) {
                    subTimeParam = intrinsic.param1;
                }
                break;
            case Skill.DE_TRUNG:
                if (intrinsic.id == 12) {
                    subTimeParam = intrinsic.param1;
                }
                break;
            case Skill.TU_SAT:
                if (intrinsic.id == 19) {
                    subTimeParam = intrinsic.param1;
                }
                break;
            case Skill.HUYT_SAO:
                if (intrinsic.id == 21) {
                    subTimeParam = intrinsic.param1;
                }
                break;
        }
        int coolDown = player.playerSkill.skillSelect.coolDown;
        player.playerSkill.skillSelect.lastTimeUseThisSkill = System.currentTimeMillis() - (coolDown * subTimeParam / 100);
        if (subTimeParam != 0) {
            Service.getInstance().sendTimeSkill(player);
        }
    }

    private boolean canHsPlayer(Player player, Player plTarget) {
        if (plTarget == null) {
            return false;
        }
        if (plTarget.isBoss) {
            return false;
        }
        if (plTarget.typePk == ConstPlayer.PK_ALL) {
            return false;
        }
        if (plTarget.typePk == ConstPlayer.PK_PVP) {
            return false;
        }
        if (player.cFlag != 0) {
            if (plTarget.cFlag != 0 && plTarget.cFlag != player.cFlag) {
                return false;
            }
        } else if (plTarget.cFlag != 0) {
            return false;
        }
        return true;
    }

    private boolean canAttackPlayer(Player pl1, Player pl2) {
        if (pl2 != null && !pl1.isDie() && !pl2.isDie()) {
            if (pl1.typePk > 0 || pl2.typePk > 0) {
                return true;
            }
            if ((pl1.cFlag != 0 && pl2.cFlag != 0)
                    && (pl1.cFlag == 8 || pl2.cFlag == 8 || pl1.cFlag != pl2.cFlag)) {
                return true;
            }
            PVP pvp = PVPServcice.gI().findPvp(pl1);
            if (pvp != null) {
                if ((pvp.player1.equals(pl1) && pvp.player2.equals(pl2)
                        || (pvp.player1.equals(pl2) && pvp.player2.equals(pl1)))) {
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
    }

    private void sendPlayerAttackMob(Player plAtt, Mob mob) {
        Message msg;
        try {
            msg = new Message(54);
            msg.writer().writeInt((int) plAtt.id);
            msg.writer().writeByte(plAtt.playerSkill.skillSelect.skillId);
            msg.writer().writeByte(mob.id);
            Service.getInstance().sendMessAllPlayerInMap(plAtt, msg);
            msg.cleanup();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void selectSkill(Player player, int skillId) {
        Skill skillBefore = player.playerSkill.skillSelect;
        for (Skill skill : player.playerSkill.skills) {
            if (skill.skillId != -1 && skill.template.id == skillId) {
                player.playerSkill.skillSelect = skill;
                switch (skillBefore.template.id) {
                    case Skill.DRAGON:
                    case Skill.KAMEJOKO:
                    case Skill.DEMON:
                    case Skill.MASENKO:
                    case Skill.LIEN_HOAN:
                    case Skill.GALICK:
                    case Skill.ANTOMIC:
                        switch (skill.template.id) {
                            case Skill.KAMEJOKO:
                            case Skill.DRAGON:
                            case Skill.DEMON:
                            case Skill.MASENKO:
                            case Skill.LIEN_HOAN:
                            case Skill.GALICK:
                            case Skill.ANTOMIC:
//                                skill.lastTimeUseThisSkill = System.currentTimeMillis() + (skill.coolDown / 2);
                                break;
                        }
                        break;
                }
                break;
            }
        }
    }

    public void useSKillNotFocus(Player player, short skillID, short xPlayer, short yPlayer, byte dir, short x, short y) {
        try {
            if (canUseSkillWithMana(player) && canUseSkillWithCooldown(player)) {
                Skill skillSelect = player.playerSkill.skillSelect;
                if (skillSelect instanceof SkillNotFocus skill) {
                    if (player.location.x != xPlayer || player.location.y != yPlayer) {
                        return;
                    }
                    int skillRange = skill.getRange();
                    int range = xPlayer + (dir == 1 ? skillRange : -skillRange);
                    sendEffStartSkillNotFocus(player, skillID, dir, 5000, (byte) 0);
                    Util.setTimeout(() -> {
                        List<Mob> mobs = new ArrayList<>();
                        List<Player> players = new ArrayList<>();
                        Hit hit = new Hit();
                        int dameAttack = player.nPoint.getDameAttackSkillNotFocus();
                        for (Mob mob : player.zone.mobs) {
                            if (player.location.y == mob.location.y) {
                                if (dir == 1) {// phải
                                    if (mob.location.x >= xPlayer && Util.getDistanceByDir(player.location.x, mob.location.x, dir) <= skillRange) {
                                        mobs.add(mob);
                                    }
                                } else {//trái
                                    if (mob.location.x <= xPlayer && Util.getDistanceByDir(player.location.x, mob.location.x, dir) >= skillRange) {
                                        mobs.add(mob);
                                    }
                                }
                                hit.addTarget(mob.id, 0);
                            }
                        }

                        for (Player p : player.zone.getPlayers()) {
                            if (SkillService.i.canAttackPlayer(player, p)) {
                                if (Math.abs(yPlayer - player.location.y) <= 100) {
                                    if (dir == 1) {// phải
                                        if (p.location.x >= xPlayer && Util.getDistanceByDir(player.location.x, p.location.x, dir) <= skillRange) {
                                            players.add(p);
                                        }
                                    } else {//trái
                                        if (p.location.x <= xPlayer && Util.getDistanceByDir(player.location.x, p.location.x, dir) >= skillRange) {
                                            players.add(p);
                                        }
                                    }
                                    hit.addTarget((int) player.id, 1);
                                }
                            }
                        }
                        sendEffEndUseSkillNotFocus(player, skillID, range, skill.getTimeDame(), hit);
                        if (skillID == Skill.MAFUBA) {
                            Util.setTimeout(() -> {
                            try {
                                Thread.sleep(skill.getTimePre());
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            int timeSocola = SkillUtil.getTimeSocola();
                            Zone z = player.zone;
                            for (Map.Entry<Integer, Integer> entry : hit.getTargets().entrySet()) {
                                int type = entry.getValue();
                                if (type == 0) {
                                    Mob mobTarget = z.findMobByID(entry.getKey());
                                    if (mobTarget != null) {
                                        EffectSkillService.gI().sendMobToSocola(player, mobTarget, timeSocola);
                                    }
                                } else {
                                    Player plTarget = z.findPlayerByID(entry.getKey());
                                    if (plTarget != null) {
                                        EffectSkillService.gI().setSocola(plTarget, System.currentTimeMillis(), timeSocola);
                                        Service.getInstance().Send_Caitrang(plTarget);
                                        ItemTimeService.gI().sendItemTime(plTarget, 3780, timeSocola / 1000);
                                    }
                                }
                            }
                            }, skill.getTimePre());
                        } else {
                            for (int i = 0; i < 10; i++) {
                                if (i == 9) {
                                    hit.addHit((dameAttack + (dameAttack / 2)));
                                } else {
                                    hit.addHit(dameAttack);
                                }
                            }
                            dealDamageSkillNotFocus(player, players, mobs, hit);
                        }
                    }, skill.getTimePre());

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendEffStartSkillNotFocus(Player player, short skillID, byte dir, int timePre, byte isFly) {
        try {
            Message m = new Message(-45);
            DataOutputStream ds = m.writer();
            ds.writeByte(20);
            ds.writeInt((int) player.id);
            ds.writeShort(skillID);
            ds.writeByte(player.gender + 1);//typeFrame
            ds.writeByte(dir);
            ds.writeShort(timePre);
            ds.writeByte(isFly);//isfly
            ds.writeByte(player.gender);//typepaint
            ds.writeByte(0);//typeItem
            ds.flush();
            Service.getInstance().sendMessAllPlayerInMap(player.zone, m);
            m.cleanup();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendEffEndUseSkillNotFocus(Player player, short skillID, int x, int time, Hit hits) {
        Message m = new Message(-45);
        DataOutputStream ds = m.writer();
        try {
            ds.writeByte(21);
            ds.writeInt((int) player.id);
            ds.writeShort(skillID);
            ds.writeShort(x);
            ds.writeShort(player.location.y);
            ds.writeShort(time);
            ds.writeShort(player.location.y);
            ds.writeByte(player.gender);//type paint
            Map<Integer, Integer> targets = hits.getTargets();
            ds.writeByte(targets.size());
            for (Map.Entry<Integer, Integer> entry : targets.entrySet()) {
                int type = entry.getValue();
                ds.writeByte(type);
                if (type == 0) {
                    ds.writeByte(entry.getKey());
                } else {
                    ds.writeInt(entry.getKey());
                }
            }

            ds.writeByte(0);//type item
            ds.flush();
            Service.getInstance().sendMessAllPlayerInMap(player.zone, m);
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dealDamageSkillNotFocus(Player player, List<Player> players, List<Mob> mobs, Hit hit) {
        List<Integer> hits = hit.getHits();
        final int maxHit = hits.size();
        final int[] damageCount = {0};
        Timer timer = player.playerSkill.timer;
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                int damage = hits.get(damageCount[0]);
                damageCount[0]++;
                for (Player p : players) {
                    p.injured(player, damage, false, false);
                }
                for (Mob mob : mobs) {
                    mob.injured(player, damage, false);
                }
                if (damageCount[0] >= maxHit) {
                    affterUseSkill(player, player.playerSkill.skillSelect.template.id);
                    cancel();
                }
            }
        }, 0, 500);
    }

    public boolean useSkill(Player player, Player plTarget, Mob mobTarget) {
        try {
            if (player.playerSkill.skillSelect == null && player.playerSkill == null && player.playerSkill.skillSelect.template == null) {
                return false;
            }
            if (player.playerSkill.skillSelect.template.type == 2 && canUseSkillWithMana(player) && canUseSkillWithCooldown(player)) {
                useSkillBuffToPlayer(player, plTarget);
                return true;
            }
            if ((player.effectSkill.isHaveEffectSkill()
                    && (player.playerSkill.skillSelect.template.id != Skill.TU_SAT
                    && player.playerSkill.skillSelect.template.id != Skill.QUA_CAU_KENH_KHI
                    && player.playerSkill.skillSelect.template.id != Skill.MAKANKOSAPPO))
                    || (plTarget != null && !canAttackPlayer(player, plTarget))
                    || (mobTarget != null && mobTarget.isDie())
                    || !canUseSkillWithMana(player) || !canUseSkillWithCooldown(player)) {
                return false;
            }
            if (player.effectSkill.useTroi) {
                EffectSkillService.gI().removeUseTroi(player);
            }
            if (player.effectSkill.isCharging) {
                EffectSkillService.gI().stopCharge(player);
            }
            if (player.isPet) {
            }
            switch (player.playerSkill.skillSelect.template.type) {
                case 1:
                    useSkillAttack(player, plTarget, mobTarget);
                    break;
                case 3:
                    useSkillAlone(player);
                    break;
                default:
                    return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}
