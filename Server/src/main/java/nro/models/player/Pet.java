package nro.models.player;

import java.util.Random;
import lombok.Getter;
import lombok.Setter;
import nro.consts.ConstPet;
import nro.consts.ConstPlayer;
import nro.models.item.CaiTrang;
import nro.models.mob.Mob;
import nro.models.skill.Skill;
import nro.server.Manager;
import nro.server.io.Message;
import nro.services.*;
import nro.utils.SkillUtil;
import nro.utils.TimeUtil;
import nro.utils.Util;

/**
 * Arriety
 */
public class Pet extends Player {

    @Getter
    @Setter
    private int lever;

    private static final short ARANGE_CAN_ATTACK = 200;
    private static final short ARANGE_ATT_SKILL1 = 50;

    private static final short[][] PET_ID = {{285, 286, 287}, {288, 289, 290}, {282, 283, 284}, {304, 305, 303}};

    public static final byte FOLLOW = 0;
    public static final byte PROTECT = 1;
    public static final byte ATTACK = 2;
    public static final byte GOHOME = 3;
    public static final byte FUSION = 4;
    public static final byte ForeverFusion = 5;
    public static boolean ANGRY;
    boolean dt;

    public Player master;
    private long lastTimeAngry;
    private boolean canUseSkill2;
    public byte status = 0;

    public byte typePet;

//    public boolean isMabu;
    public boolean isTransform;

    private boolean goingHome;

    private Mob mobAttack;
    private Player playerAttack;

    private static final int TIME_WAIT_AFTER_UNFUSION = 5000;
    private long lastTimeUnfusion;

    public byte getStatus() {
        return this.status;
    }

    @Override
    public int version() {
        return 214;
    }

    public Pet(Player master) {
        this.master = master;
        this.isPet = true;
    }

    public void changeStatus(byte status) {
        if (goingHome || master.fusion.typeFusion != 0 || (this.isDie() && status == FUSION) || this.master.zone.map.mapId == 128) {
            Service.getInstance().sendThongBao(master, "Không thể thực hiện");
            return;
        }
        Service.getInstance().chatJustForMe(master, this, getTextStatus(status));
//        if (status == GOHOME) {
//            goHome();
//        } else if (status == FUSION) {
//            fusion(false);
//        }
        switch (status) {
            case GOHOME:
                goHome();
                break;
            case FUSION:
                fusion(false);
                break;
            case ForeverFusion:
                foreverfusion();
                break;
        }
        this.status = status;
    }

    public void joinMapMaster() {
        if (!MapService.gI().isMapVS(master.zone.map.mapId)) {
            if (status != GOHOME && status != FUSION && !isDie()) {
                this.location.x = master.location.x + Util.nextInt(-10, 10);
                this.location.y = master.location.y;
                MapService.gI().goToMap(this, master.zone);
                this.zone.load_Me_To_Another(this);
            }
        }
    }

    public void goHome() {
        if (this.status == GOHOME) {
            return;
        }
        goingHome = true;
        new Thread(() -> {
            try {
                Pet.this.status = Pet.ATTACK;
                Thread.sleep(2000);
            } catch (Exception e) {
            }
            MapService.gI().goToMap(this, MapService.gI().getMapCanJoin(this, master.gender + 21));
            this.zone.load_Me_To_Another(this);
            Pet.this.status = Pet.GOHOME;
            goingHome = false;
        }).start();
    }

    private String getTextStatus(byte status) {
        switch (status) {
            case FOLLOW:
                return "Ok con theo sư phụ";
            case PROTECT:
                return "Ok con sẽ bảo vệ sư phụ";
            case ATTACK:
                return "Ok sư phụ để con lo cho";
            case GOHOME:
                return "Ok con về, bibi sư phụ";
            default:
                return "";
        }
    }

    public void fusion2(boolean porata) {
        if (this.isDie()) {
            Service.getInstance().sendThongBao(master, "Không thể thực hiện");
            return;
        }
        if (Util.canDoWithTime(lastTimeUnfusion, TIME_WAIT_AFTER_UNFUSION)) {
            if (porata) {
                master.fusion.typeFusion = ConstPlayer.HOP_THE_PORATA2;
            } else {
                master.fusion.lastTimeFusion = System.currentTimeMillis();
                master.fusion.typeFusion = ConstPlayer.LUONG_LONG_NHAT_THE;
                ItemTimeService.gI().sendItemTime(master, master.gender == ConstPlayer.NAMEC ? 3901 : 3790, Fusion.TIME_FUSION / 1000);
            }
            this.status = FUSION;
            exitMapFusion();
            fusionEffect(master.fusion.typeFusion);
            Service.getInstance().Send_Caitrang(master);
            master.nPoint.calPoint();
            master.nPoint.setFullHpMp();
            Service.getInstance().point(master);
        } else {
            Service.getInstance().sendThongBao(this.master, "Vui lòng đợi "
                    + TimeUtil.getTimeLeft(lastTimeUnfusion, TIME_WAIT_AFTER_UNFUSION / 1000) + " nữa");
        }
    }

    public void fusionSS(boolean porata) {
        if (this.isDie()) {
            Service.getInstance().sendThongBao(master, "Không thể thực hiện");
            return;
        }
        if (Util.canDoWithTime(lastTimeUnfusion, TIME_WAIT_AFTER_UNFUSION)) {
            if (porata) {
                master.fusion.typeFusion = ConstPlayer.HOP_THE_PORATASS;
            } else {
                master.fusion.lastTimeFusion = System.currentTimeMillis();
                master.fusion.typeFusion = ConstPlayer.LUONG_LONG_NHAT_THE;
                ItemTimeService.gI().sendItemTime(master, master.gender == ConstPlayer.NAMEC ? 3901 : 3790, Fusion.TIME_FUSION / 1000);
            }
            this.status = FUSION;
            exitMapFusion();
            fusionEffect(master.fusion.typeFusion);
            Service.getInstance().Send_Caitrang(master);
            master.nPoint.calPoint();
            master.nPoint.setFullHpMp();
            Service.getInstance().point(master);
        } else {
            Service.getInstance().sendThongBao(this.master, "Vui lòng đợi "
                    + TimeUtil.getTimeLeft(lastTimeUnfusion, TIME_WAIT_AFTER_UNFUSION / 1000) + " nữa");
        }
    }

    public void fusionSSS(boolean porata) {
        if (this.isDie()) {
            Service.getInstance().sendThongBao(master, "Không thể thực hiện");
            return;
        }
        if (Util.canDoWithTime(lastTimeUnfusion, TIME_WAIT_AFTER_UNFUSION)) {
            if (porata) {
                master.fusion.typeFusion = ConstPlayer.HOP_THE_PORATASSS;
            } else {
                master.fusion.lastTimeFusion = System.currentTimeMillis();
                master.fusion.typeFusion = ConstPlayer.LUONG_LONG_NHAT_THE;
                ItemTimeService.gI().sendItemTime(master, master.gender == ConstPlayer.NAMEC ? 3901 : 3790, Fusion.TIME_FUSION / 1000);
            }
            this.status = FUSION;
            exitMapFusion();
            fusionEffect(master.fusion.typeFusion);
            Service.getInstance().Send_Caitrang(master);
            master.nPoint.calPoint();
            master.nPoint.setFullHpMp();
            Service.getInstance().point(master);
        } else {
            Service.getInstance().sendThongBao(this.master, "Vui lòng đợi "
                    + TimeUtil.getTimeLeft(lastTimeUnfusion, TIME_WAIT_AFTER_UNFUSION / 1000) + " nữa");
        }
    }

    
    public void fusion(boolean porata) {
        if (this.isDie()) {
            Service.getInstance().sendThongBao(master, "Không thể thực hiện");
            return;
        }
        if (Util.canDoWithTime(lastTimeUnfusion, TIME_WAIT_AFTER_UNFUSION)) {
            if (porata) {
                master.fusion.typeFusion = ConstPlayer.HOP_THE_PORATA;
            } else {
                master.fusion.lastTimeFusion = System.currentTimeMillis();
                master.fusion.typeFusion = ConstPlayer.LUONG_LONG_NHAT_THE;
                ItemTimeService.gI().sendItemTime(master, master.gender == ConstPlayer.NAMEC ? 3901 : 3790, Fusion.TIME_FUSION / 1000);
            }
            this.status = FUSION;
            exitMapFusion();
            fusionEffect(master.fusion.typeFusion);
            Service.getInstance().Send_Caitrang(master);
            master.nPoint.calPoint();
            master.nPoint.setFullHpMp();
            Service.getInstance().point(master);
        } else {
            Service.getInstance().sendThongBao(this.master, "Vui lòng đợi "
                    + TimeUtil.getTimeLeft(lastTimeUnfusion, TIME_WAIT_AFTER_UNFUSION / 1000) + " nữa");
        }
    }

    private void foreverfusion() {
        if (master.gender != ConstPlayer.NAMEC) {
            Service.getInstance().sendThongBao(master, "Không thể thực hiện");
            return;
        }
        if (this.isDie()) {
            Service.getInstance().sendThongBao(master, "Không thể thực hiện");
            return;
        }
        master.fusion.typeFusion = ConstPlayer.LUONG_LONG_NHAT_THE;
        exitMapFusion();
        if (master.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            master.pet.unFusion();
        }
        MapService.gI().exitMap(master.pet);
        master.pet.dispose();
        master.pet = null;
        fusionEffect(master.fusion.typeFusion);
        Service.getInstance().Send_Caitrang(master);
        long power = this.nPoint.power;
        PlayerService.gI().sendTNSM(master, (byte) 0, +power);
        master.nPoint.tiemNangUp(power);
    }

    public void unFusion() {
        master.fusion.typeFusion = 0;
        this.status = PROTECT;
        Service.getInstance().point(master);
        joinMapMaster();
        fusionEffect(master.fusion.typeFusion);
        Service.getInstance().Send_Caitrang(master);
        Service.getInstance().point(master);
        this.lastTimeUnfusion = System.currentTimeMillis();
    }

    public void fusionEffect(int type) {
        Message msg;
        try {
            msg = new Message(125);
            msg.writer().writeByte(type);
            msg.writer().writeInt((int) master.id);
            Service.getInstance().sendMessAllPlayerInMap(master, msg);
            msg.cleanup();
        } catch (Exception e) {

        }
    }

    private void exitMapFusion() {
        if (this.zone != null) {
            MapService.gI().exitMap(this);
        }
    }

    public long lastTimeMoveIdle;
    private int timeMoveIdle;
    public boolean idle;

    private void moveIdle() {
        if (status == GOHOME || status == FUSION) {
            return;
        }
        if (idle && Util.canDoWithTime(lastTimeMoveIdle, timeMoveIdle)) {
            int dir = this.location.x - master.location.x <= 0 ? -1 : 1;
            PlayerService.gI().playerMove(this, master.location.x
                    + Util.nextInt(dir == -1 ? 30 : -50, dir == -1 ? 50 : 30), master.location.y);
            lastTimeMoveIdle = System.currentTimeMillis();
            timeMoveIdle = Util.nextInt(5000, 8000);
        }
    }

    private long lastTimeMoveAtHome;
    private byte directAtHome = -1;

    @Override
    public void update() {
        try {
            super.update();
            increasePoint(); //cộng chỉ số
            updatePower(); //check mở skill...
            if (isDie()) {
                if (System.currentTimeMillis() - lastTimeDie > 50000) { // cai dong nay tgian hs de phai k đr  hs nhanh, 50000 là 50 giây
                    Service.getInstance().hsChar(this, nPoint.hpMax, nPoint.mpMax);
                } else {
                    return;
                }
            }
            if (justRevived && this.zone == master.zone) {
                Service.getInstance().chatJustForMe(master, this, "Sư phụ ơi, con đây nè!");
                justRevived = false;
            }
            if (this.zone == null || this.zone != master.zone) {
                joinMapMaster();
            }
            if (master.isDie() || this.isDie() || effectSkill.isHaveEffectSkill()) {
                return;
            }
            moveIdle();
            if (dt) {
                Player pl = this.zone.getPlayerInMap((int) playerAttack.id);
                int disToPlayer = Util.getDistance(this, pl);
                if (pl.isDie() || pl == null && (pl.typePk != 3 || pl.typePk != 5)) {
                    playerAttack = null;
                    ANGRY = false;
                } else {
                    mobAttack = null;
                    if (playerAttack != null) {
                        if (disToPlayer <= ARANGE_ATT_SKILL1 && !canUseSkill2) {
                            // đấm
                            this.playerSkill.skillSelect = getSkill(1);
                            if (SkillService.gI().canUseSkillWithCooldown(this)) {
                                if (SkillService.gI().canUseSkillWithMana(this)) {
                                    PlayerService.gI().playerMove(this, pl.location.x + Util.nextInt(-20, 20),
                                            pl.location.y);
                                    SkillService.gI().useSkill(this, pl, null, null);
                                } else {
                                    askPea();
                                }
                            }
                        } else {
                            if (disToPlayer <= ARANGE_CAN_ATTACK + 50) {
                                this.playerSkill.skillSelect = getSkill(2);
                                if (this.playerSkill.skillSelect.skillId != -1) {
                                    if (SkillService.gI().canUseSkillWithCooldown(this)) {
                                        if (SkillService.gI().canUseSkillWithMana(this)) {
                                            SkillService.gI().useSkill(this, pl, null, null);
                                            this.canUseSkill2 = true;
                                        } else {
                                            askPea();
                                            this.canUseSkill2 = false;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            switch (status) {
                case FOLLOW:
//                    followMaster(60);
                    break;
                case PROTECT:
                    if (useSkill3() || useSkill4() || useSkill5()|| useSkill6()) {
                        break;
                    }
                    mobAttack = findMobAttack();
                    if (mobAttack != null) {
                        int disToMob = Util.getDistance(this, mobAttack);
                        if (disToMob <= ARANGE_ATT_SKILL1) {
                            //đấm
                            this.playerSkill.skillSelect = getSkill(1);
                            if (SkillService.gI().canUseSkillWithCooldown(this)) {
                                if (SkillService.gI().canUseSkillWithMana(this)) {
                                    PlayerService.gI().playerMove(this, mobAttack.location.x + Util.nextInt(-20, 20), mobAttack.location.y);
                                    SkillService.gI().useSkill(this, null, mobAttack, null);
                                } else {
                                    askPea();
                                }
                            }
                        } else {
                            //chưởng
                            this.playerSkill.skillSelect = getSkill(2);
                            if (this.playerSkill.skillSelect.skillId != -1) {
                                if (SkillService.gI().canUseSkillWithCooldown(this)) {
                                    if (SkillService.gI().canUseSkillWithMana(this)) {
                                        SkillService.gI().useSkill(this, null, mobAttack, null);
                                    } else {
                                        askPea();
                                    }
                                }
                            }
                        }

                    } else {
                        idle = true;
                    }
                    break;
                case ATTACK:
                    if (useSkill3() || useSkill4()|| useSkill5()|| useSkill6()) {
                        break;
                    }
                    mobAttack = findMobAttack();
                    if (mobAttack != null) {
                        int disToMob = Util.getDistance(this, mobAttack);
                        if (disToMob <= ARANGE_ATT_SKILL1) {
                            this.playerSkill.skillSelect = getSkill(1);
                            if (SkillService.gI().canUseSkillWithCooldown(this)) {
                                if (SkillService.gI().canUseSkillWithMana(this)) {
                                    PlayerService.gI().playerMove(this, mobAttack.location.x + Util.nextInt(-20, 20), mobAttack.location.y);
                                    SkillService.gI().useSkill(this, null, mobAttack, null);
                                } else {
                                    askPea();
                                }
                            }
                        } else {
                            this.playerSkill.skillSelect = getSkill(2);
                            if (this.playerSkill.skillSelect.skillId != -1) {
                                if (SkillService.gI().canUseSkillWithMana(this)) {
                                    SkillService.gI().useSkill(this, null, mobAttack, null);
                                } else {
                                    askPea();
                                }
                            } else {
                                this.playerSkill.skillSelect = getSkill(1);
                                if (SkillService.gI().canUseSkillWithCooldown(this)) {
                                    if (SkillService.gI().canUseSkillWithMana(this)) {
                                        PlayerService.gI().playerMove(this, mobAttack.location.x + Util.nextInt(-20, 20), mobAttack.location.y);
                                        SkillService.gI().useSkill(this, null, mobAttack, null);
                                    } else {
                                        askPea();
                                    }
                                }
                            }
                        }

                    } else {
                        idle = true;
                    }
                    break;

                case GOHOME:
                    if (this.zone != null && (this.zone.map.mapId == 21 || this.zone.map.mapId == 22 || this.zone.map.mapId == 23)) {
                        if (System.currentTimeMillis() - lastTimeMoveAtHome <= 5000) {
                            return;
                        } else {
                            if (this.zone.map.mapId == 21) {
                                if (directAtHome == -1) {
                                    PlayerService.gI().playerMove(this, 250, 336);
                                    directAtHome = 1;
                                } else {
                                    PlayerService.gI().playerMove(this, 200, 336);
                                    directAtHome = -1;
                                }
                            } else if (this.zone.map.mapId == 22) {
                                if (directAtHome == -1) {
                                    PlayerService.gI().playerMove(this, 500, 336);
                                    directAtHome = 1;
                                } else {
                                    PlayerService.gI().playerMove(this, 452, 336);
                                    directAtHome = -1;
                                }
                            } else if (this.zone.map.mapId == 22) {
                                if (directAtHome == -1) {
                                    PlayerService.gI().playerMove(this, 250, 336);
                                    directAtHome = 1;
                                } else {
                                    PlayerService.gI().playerMove(this, 200, 336);
                                    directAtHome = -1;
                                }
                            }
                            Service.getInstance().chatJustForMe(master, this, "Hello sư phụ!");
                            lastTimeMoveAtHome = System.currentTimeMillis();
                        }
                    }
                    break;
            }
//            }
        } catch (Exception e) {
//            Logger.logException(Pet.class, e);
        }
    }

    private long lastTimeAskPea;

    public void askPea() {
        if (this.typePet == 1 && master.charms.tdDeTuMabu > System.currentTimeMillis()) {
            InventoryService.gI().eatPea(master);
        } else if (Util.canDoWithTime(lastTimeAskPea, 10000)) {
            Service.getInstance().chatJustForMe(master, this, "Sư phụ ơi cho con đậu thần");
            lastTimeAskPea = System.currentTimeMillis();
        }
    }

    private int countTTNL;

    private boolean useSkill3() {
        try {
            playerSkill.skillSelect = getSkill(3);
            if (playerSkill.skillSelect.skillId == -1) {
                return false;
            }
            switch (this.playerSkill.skillSelect.template.id) {
                case Skill.THAI_DUONG_HA_SAN:
                    if (SkillService.gI().canUseSkillWithCooldown(this) && SkillService.gI().canUseSkillWithMana(this)) {
                        SkillService.gI().useSkill(this, null, null, null);
                        Service.getInstance().chatJustForMe(master, this, "Thái dương hạ san");
                        return true;
                    }
                    return false;
                case Skill.TAI_TAO_NANG_LUONG:
                    if (this.effectSkill.isCharging && this.countTTNL < Util.nextInt(3, 5)) {
                        this.countTTNL++;
                        return true;
                    }
                    if (SkillService.gI().canUseSkillWithCooldown(this) && SkillService.gI().canUseSkillWithMana(this)
                            && (this.nPoint.getCurrPercentHP() <= 20 || this.nPoint.getCurrPercentMP() <= 20)) {
                        SkillService.gI().useSkill(this, null, null, null);
                        this.countTTNL = 0;
                        return true;
                    }
                    return false;
                case Skill.KAIOKEN:
                    if (SkillService.gI().canUseSkillWithCooldown(this) && SkillService.gI().canUseSkillWithMana(this)) {
                        mobAttack = this.findMobAttack();
                        if (mobAttack == null) {
                            return false;
                        }
                        int dis = Util.getDistance(this, mobAttack);
                        if (dis > ARANGE_ATT_SKILL1) {
                            PlayerService.gI().playerMove(this, mobAttack.location.x, mobAttack.location.y);
                        } else {
                            if (SkillService.gI().canUseSkillWithCooldown(this) && SkillService.gI().canUseSkillWithMana(this)) {
                                PlayerService.gI().playerMove(this, mobAttack.location.x + Util.nextInt(-20, 20), mobAttack.location.y);
                            }
                        }
                        SkillService.gI().useSkill(this, playerAttack, mobAttack, null);
                        getSkill(1).lastTimeUseThisSkill = System.currentTimeMillis();
                        return true;
                    }
                    return false;
                default:
                    return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    private boolean useSkill4() {
        try {
            this.playerSkill.skillSelect = getSkill(4);
            if (this.playerSkill.skillSelect.skillId == -1) {
                return false;
            }
            switch (this.playerSkill.skillSelect.template.id) {
                case Skill.BIEN_KHI:
                    if (!this.effectSkill.isMonkey && SkillService.gI().canUseSkillWithCooldown(this) && SkillService.gI().canUseSkillWithMana(this)) {
                        SkillService.gI().useSkill(this, null, null, null);
                        return true;
                    }
                    return false;
                case Skill.KHIEN_NANG_LUONG:
                    if (!this.effectSkill.isShielding && SkillService.gI().canUseSkillWithCooldown(this) && SkillService.gI().canUseSkillWithMana(this)) {
                        SkillService.gI().useSkill(this, null, null, null);
                        return true;
                    }
                    return false;
                case Skill.DE_TRUNG:
                    if (this.mobMe == null && SkillService.gI().canUseSkillWithCooldown(this) && SkillService.gI().canUseSkillWithMana(this)) {
                        SkillService.gI().useSkill(this, null, null, null);
                        return true;
                    }
                    return false;
                default:
                    return false;
            }
        } catch (Exception e) {
            return false;
        }
    }
    
     private boolean useSkill5() {
        try {
            this.playerSkill.skillSelect = getSkill(5);
            if (this.playerSkill.skillSelect.skillId == -1) {
                return false;
            }
            switch (this.playerSkill.skillSelect.template.id) {
                case Skill.MAFUBA:
                    if (!this.effectSkill.isMonkey && SkillService.gI().canUseSkillWithCooldown(this) && SkillService.gI().canUseSkillWithMana(this)) {
                        SkillService.gI().useSkill(this, null, null, null);
                        return true;
                    }
                    return false;
                case Skill.SUPER_ANTOMIC:
                    if (!this.effectSkill.isShielding && SkillService.gI().canUseSkillWithCooldown(this) && SkillService.gI().canUseSkillWithMana(this)) {
                        SkillService.gI().useSkill(this, null, null, null);
                        return true;
                    }
                    return false;
                case Skill.SUPER_KAME:
                    if (this.mobMe == null && SkillService.gI().canUseSkillWithCooldown(this) && SkillService.gI().canUseSkillWithMana(this)) {
                        SkillService.gI().useSkill(this, null, null, null);
                        return true;
                    }
                    return false;
                default:
                    return false;
            }
        } catch (Exception e) {
            return false;
        }
    }
    
      private boolean useSkill6() {
        try {
            this.playerSkill.skillSelect = getSkill(6);
            if (this.playerSkill.skillSelect.skillId == -1) {
                return false;
            }
            switch (this.playerSkill.skillSelect.template.id) {
                case Skill.MAKANKOSAPPO:
                    if (!this.effectSkill.isMonkey && SkillService.gI().canUseSkillWithCooldown(this) && SkillService.gI().canUseSkillWithMana(this)) {
                        SkillService.gI().useSkill(this, null, null, null);
                        return true;
                    }
                    return false;
                case Skill.QUA_CAU_KENH_KHI:
                    if (!this.effectSkill.isShielding && SkillService.gI().canUseSkillWithCooldown(this) && SkillService.gI().canUseSkillWithMana(this)) {
                        SkillService.gI().useSkill(this, null, null, null);
                        return true;
                    }
                    return false;
                case Skill.BIEN_KHI:
                    if (this.mobMe == null && SkillService.gI().canUseSkillWithCooldown(this) && SkillService.gI().canUseSkillWithMana(this)) {
                        SkillService.gI().useSkill(this, null, null, null);
                        return true;
                    }
                    return false;
                default:
                    return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    private long lastTimeIncreasePoint = 0;
//
//    private void increasePoint() {
//        if (this.nPoint != null && Util.canDoWithTime(lastTimeIncreasePoint, 500)) {
//            if (Util.isTrue(1, 10)) {
//                this.nPoint.increasePoint((byte) Util.nextInt(0, 4), (short) 2);
//            } else {
//                this.nPoint.increasePoint((byte) 3, (short) 1);
//            }
//            lastTimeIncreasePoint = System.currentTimeMillis();
//        }
//    }

    private void increasePoint() {
        if (this.nPoint != null && Util.canDoWithTime(lastTimeIncreasePoint, 1)) {
            if (this.nPoint.power < 15000000l) {
                if (Util.isTrue(50, 100)) {
                    this.nPoint.increasePoint((byte) Util.nextInt(0, 2), (short) 1);
                } else {
                    byte type = (byte) Util.nextInt(0, 2);
                    short point = (short) Util.nextInt(Manager.RATE_EXP_SERVER);
                    this.nPoint.increasePoint(type, point);
                }
                lastTimeIncreasePoint = System.currentTimeMillis();
            } else {
                short points[] = {1, 10, 1, 100, 10, 100, 10, 100, 10, 100, 1, 10, 1, 100, 10, 100, 10, 100, 1, 10, 100, 1, 10, 100, 100, 100, 10, 100, 1, 10, 1, 100};
                if (Util.isTrue(50, 100)) {
                    this.nPoint.increasePoint((byte) Util.nextInt(0, 2), getRandomElement(points));
                } else {
                    byte type = (byte) Util.nextInt(0, 2);
                    short point = getRandomElement(points);
                    this.nPoint.increasePoint(type, point);
                }

                lastTimeIncreasePoint = System.currentTimeMillis();
            }
        }
    }

    public static short getRandomElement(short[] array) {
        if (array.length == 0) {
            throw new IllegalArgumentException("Array must not be empty");
        }

        Random random = new Random();
        int randomIndex = random.nextInt(array.length);
        return array[randomIndex];
    }

    public void followMaster() {
        if (this.isDie() || effectSkill.isHaveEffectSkill()) {
            return;
        }
        switch (this.status) {
            case ATTACK:
                if (ANGRY) {
                    followMaster(80);
                } else {
                    if ((mobAttack != null && Util.getDistance(this, master) <= 500)) {
                        break;
                    }
                }
            case FOLLOW:
            case PROTECT:
                followMaster(60);
                break;
        }
    }

    private void followMaster(int dis) {
        int mX = master.location.x;
        int mY = master.location.y;
        int disX = this.location.x - mX;
        if (Math.sqrt(Math.pow(mX - this.location.x, 2) + Math.pow(mY - this.location.y, 2)) >= dis) {
            if (disX < 0) {
                this.location.x = mX - Util.nextInt(0, dis);
            } else {
                this.location.x = mX + Util.nextInt(0, dis);
            }
            this.location.y = mY;
            PlayerService.gI().playerMove(this, this.location.x, this.location.y);
        }
    }

    public short getAvatar() {
        switch (this.typePet) {
            case ConstPet.MABU:
                return 297;
            case ConstPet.VIDEL:
                return 810;
            case ConstPet.BILL:
                return 508;
            case ConstPet.GOKU_BLUE:
                return 1502;
            case ConstPet.VEGETA_BLUE:
                return 1505;
            case ConstPet.GOHAN_BLUE:
                return 1508;
            case ConstPet.GOKU_GOD:
                return 1511;
            case ConstPet.VEGETA_GOD:
                return 1514;
            case ConstPet.GOHAN_GOD:
                return 1517;
            case ConstPet.GOKU_GODD:
                return 1698;
            case ConstPet.BAT_GIOI:
                return 1821;
            case ConstPet.HANG_NGA:
                return 1824;
            case ConstPet.VEGITO:
                return 1939;
            case ConstPet.VEGITO_SSJ:
                return 1942;
            case ConstPet.GOKU_ULTRA:
                return 1704;
              case ConstPet.BROLY:
                return 2893;   
                
            default:
                return PET_ID[3][this.gender];
        }
    }

    @Override
    public short getHead() {
        if (effectSkill.isMonkey) {
            return (short) ConstPlayer.HEADMONKEY[effectSkill.levelMonkey - 1];
        } else if (effectSkill.isSocola || effectSkin.isSocola) {
            return 412;
        } else if (effectSkin != null && effectSkin.isHoaDa) {
            return 454;
        } else if (this.typePet == ConstPet.MABU && !this.isTransform) {
            return 297;
        } else if (this.typePet == ConstPet.VIDEL && !this.isTransform) {
            return 810;
        } else if (this.typePet == ConstPet.BILL && !this.isTransform) {
            return 508;

        } else if (this.typePet == ConstPet.GOKU_BLUE && !this.isTransform) {
            return 1502;

        } else if (this.typePet == ConstPet.VEGETA_BLUE && !this.isTransform) {
            return 1505;

        } else if (this.typePet == ConstPet.GOHAN_BLUE && !this.isTransform) {
            return 1508;

        } else if (this.typePet == ConstPet.GOKU_GOD && !this.isTransform) {
            return 1511;

        } else if (this.typePet == ConstPet.VEGETA_GOD && !this.isTransform) {
            return 1514;

        } else if (this.typePet == ConstPet.GOHAN_GOD && !this.isTransform) {
            return 1517;

        } else if (this.typePet == ConstPet.GOKU_GODD && !this.isTransform) {
            return 1710;

        } else if (this.typePet == ConstPet.BAT_GIOI && !this.isTransform) {
            return 1821;

        } else if (this.typePet == ConstPet.HANG_NGA && !this.isTransform) {
            return 1824;

        } else if (this.typePet == ConstPet.VEGITO && !this.isTransform) {
            return 1939;

        } else if (this.typePet == ConstPet.VEGITO_SSJ && !this.isTransform) {
            return 1942;

        }else if (this.typePet == ConstPet.GOKU_ULTRA && !this.isTransform) {
            return 1704;
         }else if (this.typePet == ConstPet.BROLY && !this.isTransform) {
            return 294;    
            

        } else if (inventory.itemsBody.get(5).isNotNullItem()) {
            CaiTrang ct = Manager.getCaiTrangByItemId(inventory.itemsBody.get(5).template.id);
            if (ct != null) {
                return (short) ((short) ct.getID()[0] != -1 ? ct.getID()[0] : inventory.itemsBody.get(5).template.part);
            }
        }
        if (this.nPoint.power < 1500000) {
            return PET_ID[this.gender][0];
        } else {
            return PET_ID[3][this.gender];
        }
    }

    @Override
    public short getBody() {
        if (effectSkill.isMonkey) {
            return 193;
        } else if (effectSkill.isSocola || effectSkin.isSocola) {
            return 413;
        } else if (effectSkin != null && effectSkin.isHoaDa) {
            return 455;
        } else if (this.typePet == ConstPet.MABU && !this.isTransform) {
            return 298;
        } else if (this.typePet == ConstPet.VIDEL && !this.isTransform) {
            return 811;
        } else if (this.typePet == ConstPet.BILL && !this.isTransform) {
            return 509;
        } else if (this.typePet == ConstPet.GOKU_BLUE && !this.isTransform) {
            return 1503;
        } else if (this.typePet == ConstPet.VEGETA_BLUE && !this.isTransform) {
            return 1506;
        } else if (this.typePet == ConstPet.GOHAN_BLUE && !this.isTransform) {
            return 1509;
        } else if (this.typePet == ConstPet.GOKU_GOD && !this.isTransform) {
            return 1512;
        } else if (this.typePet == ConstPet.VEGETA_GOD && !this.isTransform) {
            return 1515;
        } else if (this.typePet == ConstPet.GOHAN_GOD && !this.isTransform) {
            return 1518;
        } else if (this.typePet == ConstPet.GOKU_GODD && !this.isTransform) {
            return 1707;
        } else if (this.typePet == ConstPet.BAT_GIOI && !this.isTransform) {
            return 1822;
        } else if (this.typePet == ConstPet.HANG_NGA && !this.isTransform) {
            return 1825;
        } else if (this.typePet == ConstPet.VEGITO && !this.isTransform) {
            return 1940;
        } else if (this.typePet == ConstPet.VEGITO_SSJ && !this.isTransform) {
            return 1943;
        }  else if (this.typePet == ConstPet.GOKU_ULTRA && !this.isTransform) {
            return 1707;
         }  else if (this.typePet == ConstPet.BROLY && !this.isTransform) {
            return 295;   
            
            
        } else if (inventory.itemsBody.get(5).isNotNullItem()) {
            CaiTrang ct = Manager.getCaiTrangByItemId(inventory.itemsBody.get(5).template.id);
            if (ct != null && ct.getID()[1] != -1) {
                return (short) ct.getID()[1];
            }
        }
        if (inventory.itemsBody.get(0).isNotNullItem()) {
            return inventory.itemsBody.get(0).template.part;
        }
        if (this.nPoint.power < 1500000) {
            return PET_ID[this.gender][1];
        } else {
            return (short) (gender == ConstPlayer.NAMEC ? 59 : 57);
        }
    }

    @Override
    public short getLeg() {
        if (effectSkill.isMonkey) {
            return 194;
        } else if (effectSkill.isSocola || effectSkin.isSocola) {
            return 414;
        } else if (effectSkin != null && effectSkin.isHoaDa) {
            return 456;
        } else if (this.typePet == ConstPet.MABU && !this.isTransform) {
            return 299;
        } else if (this.typePet == ConstPet.VIDEL && !this.isTransform) {
            return 812;
        } else if (this.typePet == ConstPet.BILL && !this.isTransform) {
            return 510;
        } else if (this.typePet == ConstPet.GOKU_BLUE && !this.isTransform) {
            return 1504;
        } else if (this.typePet == ConstPet.VEGETA_BLUE && !this.isTransform) {
            return 1507;
        } else if (this.typePet == ConstPet.GOHAN_BLUE && !this.isTransform) {
            return 1510;
        } else if (this.typePet == ConstPet.GOKU_GOD && !this.isTransform) {
            return 1513;
        } else if (this.typePet == ConstPet.VEGETA_GOD && !this.isTransform) {
            return 1516;
        } else if (this.typePet == ConstPet.GOHAN_GOD && !this.isTransform) {
            return 1519;
        } else if (this.typePet == ConstPet.GOKU_GODD && !this.isTransform) {
            return 1708;
        } else if (this.typePet == ConstPet.BAT_GIOI && !this.isTransform) {
            return 1823;
        } else if (this.typePet == ConstPet.HANG_NGA && !this.isTransform) {
            return 1826;
        } else if (this.typePet == ConstPet.VEGITO && !this.isTransform) {
            return 1941;
        } else if (this.typePet == ConstPet.VEGITO_SSJ && !this.isTransform) {
            return 1944;
        } else if (this.typePet == ConstPet.GOKU_ULTRA && !this.isTransform) {
            return 1708;
         }  else if (this.typePet == ConstPet.BROLY && !this.isTransform) {
            return 296;   
            
        } else if (inventory.itemsBody.get(5).isNotNullItem()) {
            CaiTrang ct = Manager.getCaiTrangByItemId(inventory.itemsBody.get(5).template.id);
            if (ct != null && ct.getID()[2] != -1) {
                return (short) ct.getID()[2];
            }
        }
        if (inventory.itemsBody.get(1).isNotNullItem()) {
            return inventory.itemsBody.get(1).template.part;
        }

        if (this.nPoint.power < 1500000) {
            return PET_ID[this.gender][2];
        } else {
            return (short) (gender == ConstPlayer.NAMEC ? 60 : 58);
        }
    }

    private Mob findMobAttack() {
        int dis = ARANGE_CAN_ATTACK;
        Mob mobAtt = null;
        for (Mob mob : zone.mobs) {
            if (mob.isDie()) {
                continue;
            }
            int d = Util.getDistance(this, mob);
            if (d <= dis) {
                dis = d;
                mobAtt = mob;
            }
        }
        if (dt == true) {
            return null;
        }
        return mobAtt;
    }

    public void dt(Player plAtt) {

        if (plAtt != null && plAtt != this && plAtt != this.master) {
            this.playerAttack = plAtt;
            if (System.currentTimeMillis() - lastTimeAngry > 5000) {
                if (this.playerAttack.isPet) {
                    this.chat("Mi làm ta nổi giận rồi đệ " + playerAttack.name
                            .replace("$", ""));
                } else if (this.playerAttack.isBoss) {
                    this.chat("Mi làm ta nổi giận rồi Boss " + playerAttack.name
                            .replace("$", ""));
                } else {
                    this.chat(
                            "Mi làm ta nổi giận rồi thằng " + playerAttack.name
                                    .replace("$", ""));
                }
            }
            lastTimeAngry = System.currentTimeMillis();

            ANGRY = true;
            this.mobAttack = null;
        } else {
            ANGRY = false;
            this.playerAttack = null;
        }
    }

    private void updatePower() {
        if (this.playerSkill != null) {
            switch (this.playerSkill.getSizeSkill()) {
                case 1:
                    if (this.nPoint.power >= 150000000) {
                        openSkill2();
                    }
                    break;
                case 2:
                    if (this.nPoint.power >= 2000000000000l) {
                        openSkill3();
                    }
                    break;
                case 3:
                    if (this.nPoint.power >= 20000000000000l) {
                        openSkill4();
                          }
                    break;
                 case 4:
                    if (this.nPoint.power >= 2000000000000000l) {
                        openSkill5();  
                          }
                    break;
                   case 5:
                    if (this.nPoint.power >= 20000000000000000L) {
                        openSkill6();        
                        
                    }

            }
        }
    }

    public void openSkill2() {
        Skill skill = null;
        int tiLeKame = 10;
        int tiLeMasenko = 60;
        int tiLeAntomic = 30;

        int rd = Util.nextInt(10, 100);
        if (rd <= tiLeKame) {
            skill = SkillUtil.createSkill(Skill.KAMEJOKO, 1);
        } else if (rd <= tiLeKame + tiLeMasenko) {
            skill = SkillUtil.createSkill(Skill.MASENKO, 1);
        } else if (rd <= tiLeKame + tiLeMasenko + tiLeAntomic) {
            skill = SkillUtil.createSkill(Skill.ANTOMIC, 1);
        }
        //   skill.coolDown = 1000;
        this.playerSkill.skills.set(1, skill);
    }

    public void openSkill3() {
        Skill skill = null;
        int tiLeTDHS = 10;
        int tiLeTTNL = 60;
        int tiLeKOK = 30;

        int rd = Util.nextInt(5, 100);
        if (rd <= tiLeTDHS) {
            skill = SkillUtil.createSkill(Skill.THAI_DUONG_HA_SAN, 1);
        } else if (rd <= tiLeTDHS + tiLeTTNL) {
            skill = SkillUtil.createSkill(Skill.TAI_TAO_NANG_LUONG, 1);
        } else if (rd <= tiLeTDHS + tiLeTTNL + tiLeKOK) {
            skill = SkillUtil.createSkill(Skill.KAIOKEN, 1);
        }
        this.playerSkill.skills.set(2, skill);
    }

    public void openSkill4() {
        Skill skill = null;
        int tiLeBienKhi = 10;
        int tiLeDeTrung = 60;
        int tiLeKNL = 30;

        int rd = Util.nextInt(5, 100);
        if (rd <= tiLeBienKhi) {
            skill = SkillUtil.createSkill(Skill.BIEN_KHI, 1);
        } else if (rd <= tiLeBienKhi + tiLeDeTrung) {
            skill = SkillUtil.createSkill(Skill.DE_TRUNG, 1);
        } else if (rd <= tiLeBienKhi + tiLeDeTrung + tiLeKNL) {
            skill = SkillUtil.createSkill(Skill.KHIEN_NANG_LUONG, 1);
        }
        this.playerSkill.skills.set(3, skill);
    }
     public void openSkill5() {
        Skill skill = null;
        int tiLeBienKhi = 10;
        int tiLeDeTrung = 60;
        int tiLeKNL = 30;

        int rd = Util.nextInt(5, 100);
        if (rd <= tiLeBienKhi) {
            skill = SkillUtil.createSkill(Skill.MAFUBA, 9);
        } else if (rd <= tiLeBienKhi + tiLeDeTrung) {
            skill = SkillUtil.createSkill(Skill.SUPER_ANTOMIC, 9);
        } else if (rd <= tiLeBienKhi + tiLeDeTrung + tiLeKNL) {
            skill = SkillUtil.createSkill(Skill.SUPER_KAME, 9);
        }
        this.playerSkill.skills.set(4, skill);
    }
    public void openSkill6() {
        Skill skill = null;
        int tiLeBienKhi = 30;
        int tiLeDeTrung = 30;
        int tiLeKNL = 30;

        int rd = Util.nextInt(5, 100);
        if (rd <= tiLeBienKhi) {
            skill = SkillUtil.createSkill(Skill.MAKANKOSAPPO, 7);
        } else if (rd <= tiLeBienKhi + tiLeDeTrung) {
            skill = SkillUtil.createSkill(Skill.QUA_CAU_KENH_KHI, 7);
        } else if (rd <= tiLeBienKhi + tiLeDeTrung + tiLeKNL) {
            skill = SkillUtil.createSkill(Skill.BIEN_KHI, 7);
        }
        this.playerSkill.skills.set(5, skill);
    } 
     
     
     
    

    private Skill getSkill(int indexSkill) {
        return this.playerSkill.skills.get(indexSkill - 1);
    }

    public void transform() {
        switch (this.typePet) {
            case ConstPet.MABU:
                this.isTransform = !this.isTransform;
                Service.getInstance().Send_Caitrang(this);
                Service.getInstance().chat(this, "Bư bư bư....");
                break;
            case ConstPet.VIDEL:
                this.isTransform = !this.isTransform;
                Service.getInstance().Send_Caitrang(this);
                Service.getInstance().chat(this, "mew mew mew....");
                break;
            default:
        }
    }

    public void angry(Player plAtt) {
        ANGRY = true;
        if (plAtt != null) {
            this.playerAttack = plAtt;
            Service.getInstance().chatJustForMe(master, this, "Mi làm ta nổi giận rồi " + playerAttack.name
                    .replace("$", ""));
        }
    }
}
