package nro.models.boss;

import nro.consts.ConstMap;
import nro.consts.ConstPlayer;
import nro.consts.ConstRatio;
import nro.models.boss.cdrd.CBoss;
import nro.models.boss.iboss.BossInterface;

import nro.models.map.ItemMap;
import nro.models.map.Zone;
import nro.models.player.Player;
import nro.models.skill.Skill;
import nro.server.ServerNotify;
import nro.services.*;
import nro.services.func.ChangeMapService;
import nro.utils.Log;
import nro.utils.SkillUtil;
import nro.utils.Util;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import nro.models.map.mabu.MabuWar14h;

/**
 * Arriety
 */
public abstract class Boss extends Player implements BossInterface {

    //type dame
    public static final byte DAME_NORMAL = 0;
    public static final byte DAME_PERCENT_HP_HUND = 1;
    public static final byte DAME_PERCENT_MP_HUND = 2;
    public static final byte DAME_PERCENT_HP_THOU = 3;
    public static final byte DAME_PERCENT_MP_THOU = 4;
    public static final byte DAME_TIME_PLAYER_WITH_HIGHEST_HP_IN_CLAN = 5;
    protected int secondsRest;
    //type hp
    public static final byte HP_NORMAL = 0;

    public static final byte HP_TIME_PLAYER_WITH_HIGHEST_DAME_IN_CLAN = 1;

    protected static final byte DO_NOTHING = 71;
    protected static final byte RESPAWN = 77;
    protected static final byte JUST_RESPAWN = 75; // khởi tạo lại, rồi chuyển sang nghỉ
    protected static final byte REST = 0; //boss chưa xuất hiện
    public static final byte JUST_JOIN_MAP = 1; // vào map rồi chuyển sang nói chuyện lúc đầu
    protected static final byte TALK_BEFORE = 2; //chào hỏi chuyển sang trạng thái khác
    protected static final byte ATTACK = 3;
    protected static final byte IDLE = 4;
    protected static final byte DIE = 5;
    protected static final byte TALK_AFTER = 6;
    protected static final byte LEAVE_MAP = 7;

    //--------------------------------------------------------------------------
    protected BossData data;
    public Zone zoneFinal = null;

    @Setter
    protected byte status;
    protected short[] outfit;
    protected byte typeDame;
    protected byte typeHp;
    protected double percentDame;
    protected short[] mapJoin;

    protected byte indexTalkBefore;
    protected String[] textTalkBefore;
    protected byte indexTalkAfter;
    protected String[] textTalkAfter;
    protected String[] textTalkMidle;

    protected long lastTimeTalk;
    protected int timeTalk;
    protected byte indexTalk;
    protected boolean doneTalkBefore;
    protected boolean doneTalkAffter;

    protected Player playerAtt;

    private long lastTimeRest;
    //thời gian nghỉ chuẩn bị đợt xuất hiện sau
    protected int secondTimeRestToNextTimeAppear = 1800;

    protected int maxIdle;
    protected int countIdle;

    private final List<Skill> skillsAttack;
    private final List<Skill> skillsSpecial;

    public Player plAttack;
    protected int targetCountChangePlayerAttack;
    protected int countChangePlayerAttack;

    private long lastTimeStartLeaveMap;
    private int timeDelayLeaveMap = 2000;

    protected boolean joinMapIdle;
    protected boolean canUpdate;
    private int timeAppear = 0;
    private long lastTimeUpdate;
    private int TIME_RESEND_LOCATION = 15;

    public long timeStartDie;
    public boolean startDie = true;
    public boolean isMabuBoss;
    public int zoneHold;
    public boolean isUseSpeacialSkill;
    public long lastTimeUseSpeacialSkill;

    public void changeStatus(byte status) {
        this.status = status;
    }

    public Boss(int id, BossData data) {
        super();
        this.id = id;
        this.skillsAttack = new ArrayList<>();
        this.skillsSpecial = new ArrayList<>();
        this.data = data;
        this.isBoss = true;
        this.initTalk();
        this.respawn();
        setJustRest();
        if (!(this instanceof CBoss)) {
            BossManager.gI().addBoss(this);
        }
    }
    
    

    @Override
    public void init() {
        this.name = data.name.replaceAll("%1", String.valueOf(Util.nextInt(0, 1000)));
        this.gender = data.gender;
        this.typeDame = data.typeDame;
        this.typeHp = data.typeHp;
        this.nPoint.power = 1;
        this.nPoint.mpg = 752002;
        double dame = data.dame;
        double def = data.def;
        double hp = 1;
        if (data.secondsRest != -1) {
            this.secondTimeRestToNextTimeAppear = data.secondsRest;
        }

        double[] arrHp = data.hp[Util.nextInt(0, data.hp.length - 1)];
        if (arrHp.length == 1) {
            hp = arrHp[0];
        } else {
            hp = Util.nextdouble(arrHp[0], arrHp[1]);
        }

        switch (this.typeHp) {
            case HP_NORMAL:
                this.nPoint.hpg = hp;
                this.nPoint.defg =   data.def;
                this.nPoint.critg = Util.nextInt(80, 100);
                break;
            case HP_TIME_PLAYER_WITH_HIGHEST_DAME_IN_CLAN:
                break;
        }

        switch (this.typeDame) {
            case DAME_NORMAL:
                this.nPoint.dameg = dame;
                //   this.nPoint.def = dame;

                break;
            case DAME_PERCENT_HP_HUND:
                this.percentDame = (int) dame;
                this.nPoint.dameg = this.nPoint.hpg * dame / 100;
                break;
            case DAME_PERCENT_MP_HUND:
                this.percentDame = dame;
                this.nPoint.dameg = this.nPoint.mpg * dame / 100;
                break;
            case DAME_PERCENT_HP_THOU:
                this.percentDame = dame;
                this.nPoint.dameg = this.nPoint.hp * dame / 1000;
                break;
            case DAME_PERCENT_MP_THOU:
                this.percentDame = dame;
                this.nPoint.dameg = this.nPoint.mpg * dame / 1000;
                break;
            case DAME_TIME_PLAYER_WITH_HIGHEST_HP_IN_CLAN:

                break;
        }
        this.nPoint.def = data.def;
        this.nPoint.calPoint();
        this.outfit = data.outfit;
        this.mapJoin = data.mapJoin;
        if (data.timeDelayLeaveMap != -1) {
            this.timeDelayLeaveMap = data.timeDelayLeaveMap;
        }
        this.joinMapIdle = data.joinMapIdle;
        initSkill();
    }

    @Override
    public int version() {
        return 214;
    }
    
    public void changeToTypePK() {
        PlayerService.gI().changeAndSendTypePK(this, ConstPlayer.PK_ALL);
    }
    
    public void activer() {
        if (this.typePk == ConstPlayer.NON_PK) {
            this.changeToTypePK();
        }
        this.attack();
    }

    protected void initSkill() {
        this.playerSkill.skills.clear();
        this.skillsAttack.clear();
        this.skillsSpecial.clear();
        int[][] skillTemp = data.skillTemp;
        for (int i = 0; i < skillTemp.length; i++) {
            Skill skill = SkillUtil.createSkill(skillTemp[i][0], skillTemp[i][1]);
            skill.coolDown = skillTemp[i][2];
            this.playerSkill.skills.add(skill);
            switch (skillTemp[i][0]) {
                case Skill.DRAGON:
                case Skill.DEMON:
                case Skill.GALICK:
                case Skill.KAMEJOKO:
                case Skill.MASENKO:
                case Skill.ANTOMIC:
                case Skill.TAI_TAO_NANG_LUONG:
                case Skill.THAI_DUONG_HA_SAN:
                case Skill.BIEN_KHI:
                case Skill.THOI_MIEN:
                case Skill.TROI:
                case Skill.KHIEN_NANG_LUONG:
                case Skill.SOCOLA:
                case Skill.DE_TRUNG:
                case Skill.KAIOKEN:
                case Skill.DICH_CHUYEN_TUC_THOI:
                case Skill.PHAN_THAN:
                case Skill.QUA_CAU_KENH_KHI:
                case Skill.SUPER_KAME:
                case Skill.SUPER_ANTOMIC:
                case Skill.MAFUBA:
                case Skill.TU_SAT:
                case Skill.EVOLUTION:
               
                    this.skillsAttack.add(skill);
                    break;
            }
        }
    }

    @Override
    public void update() {
        super.update();
        try {
            if (!this.effectSkill.isHaveEffectSkill()
                    && !this.effectSkill.isCharging) {
                this.immortalMp();
                switch (this.status) {
                    case RESPAWN:
                        respawn();
                        break;
                    case JUST_RESPAWN:
                        this.changeStatus(REST);
                        break;
                    case REST:
                        if (Util.canDoWithTime(lastTimeRest, secondTimeRestToNextTimeAppear * 1000)) {
                            this.changeStatus(JUST_JOIN_MAP);
                        }
                        break;
                    case JUST_JOIN_MAP:
                        joinMap();
                        if (this.zone != null) {
                            changeStatus(TALK_BEFORE);
                        }
                        break;
                    case TALK_BEFORE:
                        if (talk()) {
                            if (!this.joinMapIdle) {
                                changeToAttack();
                            } else {
                                this.changeStatus(IDLE);
                            }
                        }
                        break;
                    case ATTACK:
                        this.talk();
                        if (this.playerSkill.prepareTuSat || this.playerSkill.prepareLaze
                                || this.playerSkill.prepareQCKK) {
                            break;
                        } else {
                            this.attack();
                        }
                        break;
                    case IDLE:
                        timeStartDie = System.currentTimeMillis();
                        this.idle();
                        break;
                    case DIE:
                        if (this.joinMapIdle) {
                            this.changeToIdle();
                        }
                        if (MabuWar14h.gI().isTimeMabuWar() && this.isMabuBoss && this.zone.map.mapId == 114) {
                            nextMabu(this.isDie());
                            return;
                        }
                        changeStatus(TALK_AFTER);
                        break;
                    case TALK_AFTER:
                        if (talk()) {
                            changeStatus(LEAVE_MAP);
                            this.lastTimeStartLeaveMap = System.currentTimeMillis();
                        }
                        break;
                    case LEAVE_MAP:
                        if (Util.canDoWithTime(lastTimeStartLeaveMap, timeDelayLeaveMap)) {
                            this.leaveMap();
                            if (this.id != BossFactory.test1) {
                                this.changeStatus(RESPAWN);
                            }
                        }
                        break;
                    case DO_NOTHING:

                        break;
                }
            }
            if (Util.canDoWithTime(lastTimeUpdate, 6000)) {
                if (timeAppear >= TIME_RESEND_LOCATION) {
                    if (this.zone != null && !(this instanceof BossMabuWar)) {
                        ServerNotify.gI().notify("Boss " + this.name + " has just appeared at " + this.zone.map.mapName);
                        timeAppear = 0;
                    }
                } else {
                    timeAppear++;
                }
                lastTimeUpdate = System.currentTimeMillis();
            }
        } catch (Exception e) {
            Log.error(Boss.class, e);
        }
    }

    @Override
    public double injured(Player plAtt, double damage, boolean piercing, boolean isMobAttack) {
        double dame = 0;
        if (this.isDie()) {
            return dame;
        } else {
            dame = super.injured(plAtt, damage, piercing, isMobAttack);
            if (plAtt != null) {
                if (plAtt.dameBoss + dame >= Double.MAX_VALUE) {
                    plAtt.dameBoss = Double.MAX_VALUE;
                } else {
                    plAtt.dameBoss += dame;
                }
            }
            if (this.isDie()) {
                rewards(plAtt);
                notifyPlayeKill(plAtt);
                die();
            }
            return dame;
        }
    }

    public void nextMabu(boolean isDie) {
        if ((isDie ? this.isDie() : true) && this.head != 427 && !Util.canDoWithTime(this.timeStartDie, 3200)) {
            if (this.startDie) {
                this.startDie = false;
                Service.getInstance().hsChar(this, -1, -1);
                EffectSkillService.gI().startCharge(this);
            }
            return;
        }
        this.startDie = false;
        EffectSkillService.gI().stopCharge(this);
        int id = (int) this.id;
        switch (id) {
            case BossFactory.test1:// boss die là bư mập => Summon Super Bư
//                this.leaveMap();
//                this.id = BossFactory.SUPER_BU;
//                this.data = BossData.SUPER_BU;
//                this.changeStatus(RESPAWN);
                break;

            default:
                if (isDie) {
                    this.leaveMap();
                }
                break;
        }
    }

    
    
    protected void notifyPlayeKill(Player player) {
        if (player != null) {
            ServerNotify.gI().notify(player.name + " vừa tiêu diệt được " + this.name + " mọi người đều ngưỡng mộ");
        }
    }

    public double injuredNotCheckDie(Player plAtt, double damage, boolean piercing) {
        if (this.isDie()) {
            return 0;
        } else {
            double dame = super.injured(plAtt, damage, piercing, false);
            return dame;
        }
    }

    protected Skill getSkillAttack() {
        return skillsAttack.get(Util.nextInt(0, skillsAttack.size() - 1));
    }

    protected Skill getSkillSpecial() {
        return skillsSpecial.get(Util.nextInt(0, skillsSpecial.size() - 1));
    }

    protected Skill getSkillById(int skillId) {
        return SkillUtil.getSkillbyId(this, skillId);
    }

    @Override
    public void die() {
        setJustRest();
        changeStatus(DIE);
    }
    
    
    

    @Override
    public void joinMap() {
        if (this.zone == null) {
            this.zone = getMapCanJoin(mapJoin[Util.nextInt(0, mapJoin.length - 1)]);
        }
        if (this.zone != null) {
            ChangeMapService.gI().changeMapBySpaceShip(this, this.zone, ChangeMapService.TELEPORT_YARDRAT);
            ServerNotify.gI().notify("Boss " + this.name + " has just appeared at " + this.zone.map.mapName);
            System.out.println("Boss: [" + this.name + "] appeared on Map: [" + this.zone.map.mapId + "] Zone: " + this.zone.zoneId);
        }
    }

    public Zone getMapCanJoin(int mapId) {
        Zone map = MapService.gI().getMapWithRandZone(mapId);
        if (map.isBossCanJoin(this)) {
            return map;
        } else {
            return getMapCanJoin(mapJoin[Util.nextInt(0, mapJoin.length - 1)]);
        }
    }

    @Override
    public void leaveMap() {
        MapService.gI().exitMap(this);
    }

    @Override
    public boolean talk() {
        switch (status) {
            case TALK_BEFORE:
                if (this.textTalkBefore == null || this.textTalkBefore.length == 0) {
                    return true;
                }
                if (Util.canDoWithTime(lastTimeTalk, 5000)) {
                    if (indexTalkBefore < textTalkBefore.length) {
                        this.chat(textTalkBefore[indexTalkBefore++]);
                        if (indexTalkBefore >= textTalkBefore.length) {
                            return true;
                        }
                        lastTimeTalk = System.currentTimeMillis();
                    } else {
                        return true;
                    }
                }
                break;
            case IDLE:
            case ATTACK:
                if (this.textTalkMidle == null || this.textTalkMidle.length == 0 || !Util.isTrue(1, 30)) {
                    return true;
                }
                if (Util.canDoWithTime(lastTimeTalk, Util.nextInt(15000, 20000))) {
                    this.chat(textTalkMidle[Util.nextInt(0, textTalkMidle.length - 1)]);
                    lastTimeTalk = System.currentTimeMillis();
                }
                break;
            case TALK_AFTER:
                if (this.textTalkAfter == null || this.textTalkAfter.length == 0) {
                    return true;
                }
                if (Util.canDoWithTime(lastTimeTalk, 5000)) {
                    this.chat(textTalkAfter[indexTalkAfter++]);
                    if (indexTalkAfter >= textTalkAfter.length) {
                        return true;
                    }
                    if (indexTalkAfter > textTalkAfter.length - 1) {
                        indexTalkAfter = 0;
                    }
                    lastTimeTalk = System.currentTimeMillis();
                }
                break;
        }
        return false;
    }

    @Override
    public void respawn() {
        this.init();
        this.indexTalkBefore = 0;
        this.indexTalkAfter = 0;
        this.nPoint.setFullHpMp();
        this.changeStatus(JUST_RESPAWN);
    }

    public void joinMapByZone(Zone zone, int x, int y) {
        if (zone != null) {
            this.zone = zone;
            ChangeMapService.gI().changeMapYardrat(this, this.zone, x, y);
        }
    }

    protected void goToPlayer(Player pl, boolean isTeleport) {
        goToXY(pl.location.x, pl.location.y, isTeleport);
    }

    protected void goToXY(int x, int y, boolean isTeleport) {
        if (!isTeleport) {
            byte dir = (byte) (this.location.x - x < 0 ? 1 : -1);
            byte move = (byte) Util.nextInt(50, 100);
            PlayerService.gI().playerMove(this, this.location.x + (dir == 1 ? move : -move), y);
        } else {
            ChangeMapService.gI().changeMapYardrat(this, this.zone, x, y);
        }
    }

    public void joinMapByZone(Zone zone, int x) {
        if (zone != null) {
            this.zone = zone;
            ChangeMapService.gI().changeMapBySpaceShipBoss(this, this.zone, x);
        }
    }

    protected int getRangeCanAttackWithSkillSelect() {
        int skillId = this.playerSkill.skillSelect.template.id;
        if (skillId == Skill.KAMEJOKO || skillId == Skill.MASENKO || skillId == Skill.ANTOMIC) {
            return Skill.RANGE_ATTACK_CHIEU_CHUONG;
        } else {
            return Skill.RANGE_ATTACK_CHIEU_DAM;
        }
    }

    @Override
    public Player getPlayerAttack() {
        try {
            if (countChangePlayerAttack < targetCountChangePlayerAttack && plAttack != null && plAttack.zone != null && plAttack.zone.equals(this.zone)) {
                if (!plAttack.isDie() && !plAttack.effectSkin.isVoHinh && !plAttack.isMiniPet) {
                    this.countChangePlayerAttack++;
                    return plAttack;
                } else {
                    plAttack = null;
                }
            } else {
                if (this.zone == null) {
                } else {
                    this.targetCountChangePlayerAttack = Util.nextInt(10, 20);
                    this.countChangePlayerAttack = 0;
                    plAttack = this.zone.getRandomPlayerInMap();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return plAttack;
    }

    public List<Player> getListPlayerAttack(int dis) {
        List<Player> Players = new ArrayList<>();
        for (int i = 0; i < this.zone.getHumanoids().size(); i++) {
            Player pl = this.zone.getHumanoids().get(i);
            if (pl != null && !pl.isDie() && !pl.effectSkill.isHoldMabu && Util.getDistance(this, pl) <= dis) {
                Players.add(pl);
            }
        }
        return Players;
    }

    @Override
    public synchronized void attack() {
        try {
            Player pl = getPlayerAttack();
            if (pl == null || pl.isDie() || pl.isMiniPet || pl.effectSkin.isVoHinh) {
                return;
            }
            this.playerSkill.skillSelect = this.getSkillAttack();
            if (Util.getDistance(this, pl) <= this.getRangeCanAttackWithSkillSelect()) {
                if (Util.isTrue(15, ConstRatio.PER100)) {
                    if (SkillUtil.isUseSkillChuong(this)) {
                        goToXY(pl.location.x + (Util.getOne(-1, 1) * Util.nextInt(20, 80)),
                                Util.nextInt(10) % 2 == 0 ? pl.location.y : pl.location.y - Util.nextInt(0, 50), false);
                    } else {
                        goToXY(pl.location.x + (Util.getOne(-1, 1) * Util.nextInt(10, 30)),
                                Util.nextInt(10) % 2 == 0 ? pl.location.y : pl.location.y - Util.nextInt(0, 50), false);
                    }
                }
                SkillService.gI().useSkill(this, pl, null, null);
                checkPlayerDie(pl);
            } else {
                goToPlayer(pl, false);

            }
        } catch (Exception ex) {
            Log.error(Boss.class,
                    ex);
        }
    }

    private void immortalMp() {
        this.nPoint.mp = this.nPoint.mpg;
    }

    protected abstract boolean useSpecialSkill();

    @Override
    public void dispose() {
        super.dispose();
    }

    @Override
    public short getHead() {
        return this.outfit[0];
    }

    @Override
    public short getBody() {
        return this.outfit[1];
    }

    @Override
    public short getLeg() {
        return this.outfit[2];
    }

    
    
    
    //status
    protected void changeIdle() {
        this.changeStatus(IDLE);
    }

    /**
     * Đổi sang trạng thái tấn công
     */
    protected void changeAttack() {
        this.changeStatus(ATTACK);
    }

    public void setJustRest() {
        this.lastTimeRest = System.currentTimeMillis();
    }

    public void setJustRestToFuture() {
        this.lastTimeRest = System.currentTimeMillis() + 8640000000L;
    }

    public void Spwand() {
        this.lastTimeRest = System.currentTimeMillis() + 400000L;
    }

    @Override
    public void dropItemReward(int tempId, int playerId, int... quantity) {
        if (!this.zone.map.isMapOffline && this.zone.map.type == ConstMap.MAP_NORMAL) {
            int x = this.location.x + Util.nextInt(-30, 30);
            if (x < 30) {
                x = 30;
            } else if (x > zone.map.mapWidth - 30) {
                x = zone.map.mapWidth - 30;
            }
            int y = this.location.y;
            if (y > 24) {
                y = this.zone.map.yPhysicInTop(x, y - 24);
            }
            ItemMap itemMap = new ItemMap(this.zone, tempId, (quantity != null && quantity.length == 1) ? quantity[0] : 1, x, y, playerId);
            Service.getInstance().dropItemMap(itemMap.zone, itemMap);
        }
    }

//    public void rewardpointsb(Player player, Boss boss) {
//        if (player != null) {
//            int diemsanboss = 0;
//            switch ((int) boss.id) {
//                case BossFactory.MAP_DAU_DINH:
//                case BossFactory.RAMBO:
//                case BossFactory.KUKU:
//                case BossFactory.PIC:
//                case BossFactory.POC:
//                case BossFactory.KINGKONG:
//                case BossFactory.ANDROID_13:
//                case BossFactory.ANDROID_14:
//                case BossFactory.ANDROID_15:
//                case BossFactory.ANDROID_19:
//                case BossFactory.ANDROID_20:
//                case BossFactory.SO1:
//                case BossFactory.SO2:
//                case BossFactory.SO3:
//                case BossFactory.SO4:
//                case BossFactory.TIEU_DOI_TRUONG:
//                case BossFactory.FIDE_DAI_CA_1:
//                case BossFactory.FIDE_DAI_CA_2:
//                case BossFactory.FIDE_DAI_CA_3:
//                    diemsanboss = 1;
//                    break;
//                case BossFactory.BILL:
//                    diemsanboss = 5;
//                    break;
//                case BossFactory.COOLER:
//                case BossFactory.COOLER2:
////                case BossFactory.BLACKGOKU:
////                case BossFactory.SUPERBLACKGOKU:
//                case BossFactory.XEN_BO_HUNG_1:
//                case BossFactory.XEN_BO_HUNG_2:
//                case BossFactory.XEN_BO_HUNG_HOAN_THIEN:
//                    diemsanboss = 3;
//                    break;
//                case BossFactory.XEN_BO_HUNG:
//                case BossFactory.SIEU_BO_HUNG:
//                    diemsanboss = 4;
//                    break;
//                case BossFactory.CUMBER:
//                case BossFactory.FIDEGOLD:
//                    diemsanboss = 5;
//                    break;
//                default:
//                    diemsanboss = 1;
//                    break;
//
//            }
//            player.point_sb += diemsanboss;
//            Service.getInstance().sendThongBao(player, "Bạn đã nhận được " + diemsanboss + " điểm săn boss");
//            rewardngocrong(player, boss);
//        }
//    }
//    public void rewardngocrong(Player player, Boss boss) {
//        if (player != null) {
//            ItemMap dropngocrong = null;
//            int ngocrong = 0;
//            int x = this.location.x;
//            int y = this.zone.map.yPhysicInTop(x, this.location.y - 24);
//            int tile = Util.nextInt(0, 100);
//            switch ((int) boss.id) {
//                case BossFactory.BILL:
//                    if (Util.isTrue(3, 10)) {
//                        ngocrong = 16;
//                    } else if (Util.isTrue(5, 10)) {
//                        ngocrong = 17;
//                    }
//                    break;
//                case BossFactory.COOLER:
//                case BossFactory.COOLER2:
////                case BossFactory.BLACKGOKU:
////                case BossFactory.SUPERBLACKGOKU:
//                    if (Util.isTrue(3, 10)) {
//                        ngocrong = 16;
//                    } else if (Util.isTrue(5, 10)) {
//                        ngocrong = 17;
//                    }
//                    break;
//                case BossFactory.XEN_BO_HUNG_1:
//                case BossFactory.XEN_BO_HUNG_2:
//                case BossFactory.XEN_BO_HUNG_HOAN_THIEN:
//                    if (Util.isTrue(3, 10)) {
//                        ngocrong = 16;
//                    } else if (Util.isTrue(5, 10)) {
//                        ngocrong = 17;
//                    }
//                    break;
//                case BossFactory.XEN_BO_HUNG:
//                case BossFactory.SIEU_BO_HUNG:
//                    if (Util.isTrue(3, 10)) {
//                        ngocrong = 16;
//                    } else if (Util.isTrue(5, 10)) {
//                        ngocrong = 17;
//                    } else if (Util.isTrue(1, 10)) {
//                        ngocrong = 15;
//                    }
//                    break;
//                case BossFactory.SO4:
//                case BossFactory.SO3:
//                case BossFactory.SO2:
//                case BossFactory.SO1:
//                case BossFactory.FIDE_DAI_CA_1:
//                case BossFactory.FIDE_DAI_CA_2:
//                case BossFactory.FIDE_DAI_CA_3:
//                    ngocrong = Util.nextInt(17, 20);
//                    break;
//                case BossFactory.CUMBER:
//                case BossFactory.FIDEGOLD:
//                    if (Util.isTrue(3, 10)) {
//                        ngocrong = 16;
//                    } else if (Util.isTrue(5, 10)) {
//                        ngocrong = 17;
//                    }
//                    break;
//                default:
//                    if (Util.isTrue(5, 10)) {
//                        ngocrong = Util.nextInt(17, 20);
//                    }
//                    break;
//
//            }
//            if (ngocrong != 0) {
//                dropngocrong = new ItemMap(this.zone, ngocrong, 1, x, y, player.id);
//                if (dropngocrong != null) {
//                    Service.getInstance().dropItemMap(zone, dropngocrong);
//                }
//            }
//
//        }
//    }
    public void dropitemc2(Player player) {
        if (player != null) {
            if (Util.isTrue(1, 1)) {
                ItemMap itemMap = null;
                int x = this.location.x;
                int y = this.zone.map.yPhysicInTop(x, this.location.y - 24);
                int itemc2 = Util.nextInt(1150, 1154);
                if (itemc2 != 0) {
                    itemMap = new ItemMap(this.zone, itemc2, 1, x, y, player.id);
                    RewardService.gI().initBaseOptionClothes(itemMap.itemTemplate.id, itemMap.itemTemplate.type, itemMap.options);
                    if (itemMap != null) {
                        Service.getInstance().dropItemMap(zone, itemMap);
                    }
                }

            }
        }
    }

    public void randomdosao(Player player) {
        if (player != null) {
            if (Util.isTrue(1, 3)) {
                ItemMap itemMap = null;
                int x = this.location.x;
                int y = this.zone.map.yPhysicInTop(x, this.location.y - 24);
                int dophale = 0;
                int tile = Util.nextInt(0, 100);
                if (tile < 70) {
                    dophale = Util.nextInt(136, 187);
                } else {
                    dophale = Util.nextInt(230, 281);
                }
                if (dophale != 0) {
                    itemMap = new ItemMap(this.zone, dophale, 1, x, y, player.id);
                    RewardService.gI().initBaseOptionClothes(itemMap.itemTemplate.id, itemMap.itemTemplate.type, itemMap.options);
                    RewardService.gI().initStarOption(itemMap, new RewardService.RatioStar[]{
                        new RewardService.RatioStar((byte) 1, 1, 2),
                        new RewardService.RatioStar((byte) 2, 1, 3),
                        new RewardService.RatioStar((byte) 3, 1, 4),
                        new RewardService.RatioStar((byte) 4, 1, 5),
                        new RewardService.RatioStar((byte) 5, 1, 6),
                        new RewardService.RatioStar((byte) 6, 1, 7),
                        new RewardService.RatioStar((byte) 7, 1, 8)
                    });
                }
                if (itemMap != null) {
                    Service.getInstance().dropItemMap(zone, itemMap);
                }
            }
        }
    }

    public void roidothiensu(Player player) {
        if (player != null) {
            if (Util.isTrue(1, 3)) {
                ItemMap itemMap = null;
                int x = this.location.x;
                int y = this.zone.map.yPhysicInTop(x, this.location.y - 24);
                int dophale = 0;
                int tile = Util.nextInt(0, 100);
                if (tile < 70) {
                    dophale = Util.nextInt(136, 187);
                } else {
                    dophale = Util.nextInt(230, 281);
                }
                if (dophale != 0) {
                    itemMap = new ItemMap(this.zone, dophale, 1, x, y, player.id);
                    RewardService.gI().initBaseOptionClothes(itemMap.itemTemplate.id, itemMap.itemTemplate.type, itemMap.options);
                    RewardService.gI().initStarOption(itemMap, new RewardService.RatioStar[]{
                        new RewardService.RatioStar((byte) 1, 1, 2),
                        new RewardService.RatioStar((byte) 2, 1, 3),
                        new RewardService.RatioStar((byte) 3, 1, 4),
                        new RewardService.RatioStar((byte) 4, 1, 5),
                        new RewardService.RatioStar((byte) 5, 1, 6),
                        new RewardService.RatioStar((byte) 6, 1, 7),
                        new RewardService.RatioStar((byte) 7, 1, 8)
                    });
                }
                if (itemMap != null) {
                    Service.getInstance().dropItemMap(zone, itemMap);
                }
            }
        }
    }

    @Override
    public void generalRewards(Player player) {
        if (player != null) {
        }
    }

    /**
     * Đổi trạng thái máu trắng -> đỏ, chuyển trạng thái tấn công
     */
    public void changeToAttack() {
        PlayerService.gI().changeAndSendTypePK(this, ConstPlayer.PK_ALL);
        changeStatus(ATTACK);
    }

    /**
     * Đổi trạng thái máu đỏ -> trắng, chuyển trạng thái đứng
     */
    public void changeToIdle() {
        PlayerService.gI().changeAndSendTypePK(this, ConstPlayer.NON_PK);
        changeStatus(IDLE);
    }

}
