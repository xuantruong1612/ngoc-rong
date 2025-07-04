package nro.models.player;

import nro.card.Card;
import nro.card.CollectionBook;
import nro.consts.ConstAchive;
import nro.consts.ConstPlayer;
import nro.consts.ConstTask;
import nro.data.DataGame;
import nro.dialog.ConfirmDialog;
import nro.models.clan.Buff;
import nro.models.item.CaiTrang;
import nro.models.item.FlagBag;

import nro.models.clan.Clan;
import nro.models.clan.ClanMember;
import nro.models.intrinsic.IntrinsicPlayer;
import nro.models.item.Item;
import nro.models.item.ItemTime;
import nro.models.map.ItemMap;
import nro.models.map.TrapMap;
import nro.models.map.Zone;
import nro.models.map.war.BlackBallWar;
import nro.models.map.mabu.MabuWar;
import nro.models.map.war.NamekBallWar;
import nro.models.mob.MobMe;
import nro.models.npc.specialnpc.MabuEgg;
import nro.models.npc.specialnpc.MagicTree;
import nro.models.pvp.PVP;
import nro.models.skill.PlayerSkill;
import nro.models.task.TaskPlayer;
import nro.quayTamBao.TamBao;
import nro.server.Client;
import nro.server.Manager;
import nro.server.io.Message;
import nro.server.io.Session;
import nro.services.*;
import nro.services.func.ChangeMapService;
import nro.services.func.CombineNew;
import nro.services.func.PVPServcice;
import nro.utils.Log;
import nro.utils.SkillUtil;
import nro.utils.Util;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import nro.models.item.ItemOption;
import nro.models.map.mabu.MabuWar14h;
import nro.models.mob.Mob;
import nro.models.npc.specialnpc.BillEgg;
import nro.models.npc.specialnpc.duahau;
import nro.models.npc.specialnpc.kickvip;
import nro.models.phuban.DragonNamecWar.*;
import nro.models.skill.Skill;
import nro.server.TOP;
import nro.services.func.UseItem;

/**
 * Arriety
 */
public class Player {
    
    public double dameBoss;
    public Clone clone;
    public boolean isClone;
    public int isbienhinh;
    public int DoneVoDaiBaHatMit = 0;
    public long lastTimeTranformation;
    public boolean titleitem;
    public boolean dhtang1;
    public boolean dhtang2;
    
    public int pointPvp;
    
    public int pointThap;
    public int levelThap;
    public int tangThap;
    public List<Integer> listAddCaiTrang = new ArrayList<>();
    public long[] scoreBoards = new long[99]; // So luong cac tab
    // Star Tamkjll Nè
    public String Nametc;
    public double hptc = 0;
    public double dametc = 0;
    public long ctk = 0;
    public long ctkclan = 0;
    public int CapTamkjll = 0;
    public long ExpTamkjll = 0;
    public long diemfam = 0;
      public long diemshe = 0;
    public long cfpass = 0;
    public long premium = 0;
    public long diemsk = 0;
    public long Nrosieucap = 0;
    public long Tindung = 0;
    public long diemsanbat = 0;
    public long naptuan = 0;
    public long napthang = 0;
    public int mocnap;
    public int mocpass;
    public int mocpassvip;
    public int dk_bdkb;
     public int dakethon;
    public int duockethon;
    
    public boolean autonangcaptamkjll = true;
    public int LbTamkjll = 0;
    public long DLbTamkjll = 0;
    public long Diemvip = 0;
    
    public boolean isBot = false;
    public int TamkjllCapPb = 0;
    public long TamkjllCS = 0;
    public int TamkjllThomo = 0;
    public long TamkjllThomoExp = 0;
    public long[] Tamkjlltutien = new long[]{0, 0, 0};
    public long[] TamkjllDauLaDaiLuc = new long[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    public String TamkjllNamePlayer;
    public int TamkjllPetGiong = -1;
    public String TamkjllNamePet;
    public int TamkjllPetHunger;
    public long TamkjllPetPower;
    private long lastTimeHc;
    public long TamkjlllastTimeThucan;
    public int TamkjlllevelPet;
    public long TamkjllExpPet;
    public Player TamkjllPlayerAttack;
    public double Tamkjlldamethanmeo;
    public int Tamkjll_Ma_cot;
    public int Tamkjll_Tu_Ma = 0;
    public long Tamkjll_Exp_Tu_Ma = 0;
    public int Tamkjll_Ma_Hoa = 0;
    public long TamkjllLasttimeMaHoa;
    public byte TamkjllPhiPham = 10;
    public long TamkjllLasttimeMaLenh;
    public long lasttimenhanqua;
    public long lastTimerun;
    public long lasttimechat;
    // End Tamkhjll
 public long lastimelogin2;
    public long diemdanh;
    public byte CheckDayOnl;
    public byte vip;
    
    public long timevip;
    public int account_id;
    
    public SkillSpecial skillSpecial;
    public BillEgg billEgg;
    
    public List<Integer> idEffChar = new ArrayList<>();
    
    public int server;
    public byte[] buyLimit;
    public boolean isLinhThuFollow;
    public long dameAttack = 1000;
    public long lastTimeUpdateBallWar;
    
    public TamBao tamBao;
    public PlayerEvent event;
    public List<String> textRuongGo = new ArrayList<>();
    public boolean receivedWoodChest;
    public int goldChallenge;
    public int levelWoodChest;
    public boolean isInvisible;
    public boolean sendMenuGotoNextFloorMabuWar;
    public long lastTimeBabiday;
    public long lastTimeChangeZone;
    public long lastTimeChatGlobal;
    public long lastTimeChatPrivate;
    public long lastTimeChangeMap;
    public Date firstTimeLogin;
    public Session session;
    public byte countSaveFail;
    public boolean beforeDispose;
    
    public long timeFixInventory;
    public boolean isPet;
    public boolean isBoss;
    public boolean isMiniPet;
    public boolean mocnap1;
    public boolean mocnap2;
    public boolean mocnap3;
    public boolean mocnap4;
    public boolean mocnap5;
    
    public int playerTradeId = -1;
    public Player playerTrade;
    
    public kickvip kichvip;
    public int mapIdBeforeLogout;
    public List<Zone> mapBlackBall;
    public Zone zone;
    public Zone mapBeforeCapsule;
    public List<Zone> mapCapsule;
    public Pet pet;
    public MiniPet minipet;
    
    public int gtPoint;
    public int point_sb;
    public int pointidemdanh;
    public int sukiensonlo;
    public long lastTimeNotifyTimeHoldBlackBall;
    public long lastTimeHoldBlackBall;
    public int tempIdBlackBallHold = -1;
    public boolean isHoldBlackBall;
    public boolean isHoldNamecBall;
    public boolean isHoldNamecBallTranhDoat;
    public int tempIdNamecBallHoldTranhDoat = -1;
    
    public MobMe mobMe;
    public Location location;
    public SetClothes setClothes;
    public EffectSkill effectSkill;
    public MabuEgg mabuEgg;
    
    public duahau duahau;
    public TaskPlayer playerTask;
    public ItemTime itemTime;
    public Fusion fusion;
    public MagicTree magicTree;
    public IntrinsicPlayer playerIntrinsic;
    public Inventory inventory;
    public PlayerSkill playerSkill;
    public CombineNew combineNew;
    public IDMark iDMark;
    public Charms charms;
    public EffectSkin effectSkin;
    public Gift gift;
    public NPoint nPoint;
    public RewardBlackBall rewardBlackBall;
    public EffectFlagBag effectFlagBag;
    
    public Clan clan;
    public ClanMember clanMember;
    public ListFriendEnemy<Friend> friends;
    public ListFriendEnemy<Enemy> enemies;
    
    protected boolean actived = false;
    public boolean loaded;
    
    public long id;
    public String name;
    public byte gender;
    public boolean isNewMember = true;
    public short head;
    
    public byte typePk;
    
    public byte cFlag;
    public long lastTimeChangeFlag;
    public long lastTimeTrade;
    
    public boolean haveTennisSpaceShip;
    private byte useSpaceShip;
    
    public boolean isGoHome;
    
    public boolean justRevived;
    public long lastTimeRevived;
    public boolean immortal;
    
    public int pointsukien;
    public long lastTimeBan;
    public long lastTimeUpdate;
    public boolean isBan;
    public Taixiu taixiu;
    
    public boolean isGotoFuture;
    public long lastTimeGoToFuture;
    public boolean isgotoPrimaryForest;
    public long lastTimePrimaryForest;
    
    public boolean isGoToBDKB;
    public long lastTimeGoToBDKB;
    public long lastTimeAnXienTrapBDKB;
    private short powerPoint;
    private short percentPowerPont;
    
    public int sukien;
    public long lastTimePickItem;
    public int tongnap;
    
    @Setter
    @Getter
    private CollectionBook collectionBook;
    @Getter
    @Setter
    private boolean isSaving, isDisposed;
    @Getter
    @Setter
    private boolean interactWithKarin;
    @Getter
    @Setter
    // private EscortedBoss escortedBoss;
    //
    //
    private ConfirmDialog confirmDialog;
    @Getter
    @Setter
    public byte[] rewardLimit;
    @Setter
    @Getter
    private PetFollow petFollow;
    @Setter
    @Getter
    private Buff buff;
    
    public int goldTai;
    public int goldXiu;
    public long lastimeuseoption;
    public kickvip kickvip;
    
    public Player() {
        lastimeuseoption = System.currentTimeMillis();
        location = new Location();
        nPoint = new NPoint(this);
        inventory = new Inventory(this);
        playerSkill = new PlayerSkill(this);
        setClothes = new SetClothes(this);
        effectSkill = new EffectSkill(this);
        fusion = new Fusion(this);
        playerIntrinsic = new IntrinsicPlayer(this);
        rewardBlackBall = new RewardBlackBall(this);
        effectFlagBag = new EffectFlagBag(this);
        //----------------------------------------------------------------------
        taixiu = new Taixiu();
        iDMark = new IDMark();
        combineNew = new CombineNew();
        playerTask = new TaskPlayer(this);
        friends = new ListFriendEnemy<>(this);
        enemies = new ListFriendEnemy<>(this);
        itemTime = new ItemTime(this);
        charms = new Charms(this);
        gift = new Gift(this);
        effectSkin = new EffectSkin(this);
        event = new PlayerEvent(this);
        buyLimit = new byte[13];
        buff = Buff.NONE;
        skillSpecial = new SkillSpecial(this);
        //tamBao = new TamBao();
    }

    //--------------------------------------------------------------------------
    public short getPowerPoint() {
        return powerPoint;
    }
    
    public void addPowerPoint(int value) {
        powerPoint += value;
    }
    
    public short getPercentPowerPont() {
        return percentPowerPont;
    }
    
    public void addPercentPowerPoint(int value) {
        percentPowerPont += value;
    }
    
    public void resetPowerPoint() {
        percentPowerPont = 0;
        powerPoint = 0;
    }
    
    public void setUseSpaceShip(byte useSpaceShip) {
        // 0 - không dùng
        // 1 - tàu vũ trụ theo hành tinh
        // 2 - dịch chuyển tức thời
        // 3 - tàu tenis
        this.useSpaceShip = useSpaceShip;
    }
    
    public byte getUseSpaceShip() {
        return this.useSpaceShip;
    }
    
    public boolean isDie() {
        if (this.nPoint != null) {
            return this.nPoint.hp <= 0;
        } else {
            return true;
        }
    }

    //--------------------------------------------------------------------------
    public void setSession(Session session) {
        this.session = session;
    }
    
    public void sendMessage(Message msg) {
        if (this.session != null) {
            session.sendMessage(msg);
        }
    }
    
    public Session getSession() {
        return this.session;
    }
    
    public boolean isPl() {
        return !isPet && !isBoss && !isMiniPet && !isClone && !isBot;
    }

//    public int version() {
//        return session.version;
//    }
    public int version() {
        return session == null ? 231 : session.version;
    }
    
    public boolean isVersionAbove(int version) {
        return version() >= version;
    }
    
    public void update() {
        if (!this.beforeDispose) {
            try {
                if (!isBan) {
                    if (nPoint != null) {
                        nPoint.update();
                    }
                    if (fusion != null) {
                        fusion.update();
                    }
                    
                    if (effectSkin != null) {
                        effectSkill.update();
                    }
                    if (mobMe != null) {
                        mobMe.update();
                    }
                    if (effectSkin != null) {
                        effectSkin.update();
                    }
                    if (pet != null) {
                        pet.update();
                    }
                    if (clone != null) {
                        clone.update();
                    }
                    if (minipet != null) {
                        minipet.update();
                    }
                    if (magicTree != null) {
                        magicTree.update();
                    }
                    if (itemTime != null) {
                        itemTime.update();
                    }
                    if (event != null) {
                        event.update();
                        
                    }
                    if (this.isPl() && this.Diemvip >= 100000) {
                        Service.getInstance().sendTitle(this, 98);
                    }
                    if (this.isPl() && Client.gI().getPlayers().size() >= 10
                            && Util.canDoWithTime(this.lasttimenhanqua, 60000)) {
                        this.lasttimenhanqua = System.currentTimeMillis();
                        this.ExpTamkjll += Client.gI().getPlayers().size();
                        this.inventory.ruby += Client.gI().getPlayers().size() / 10;
                        Service.getInstance().sendMoney(this);
                    }
//                    if (this.isPl() && !this.isDie() && this.CapTamkjll >= 50 && this.Tamkjlltutien[2] >= 1
//                            && this.Tamkjlltutien[0] >= TamkjllDieukiencanhgioi(
//                                    Util.maxInt(this.Tamkjlltutien[1]))) {
//                        if (Util.isTrue(Tamkjlltilecanhgioi(Util.maxInt(this.Tamkjlltutien[1])), 100)) {
//                            this.Tamkjlltutien[0] -= TamkjllDieukiencanhgioi(
//                                    Util.maxInt(this.Tamkjlltutien[1]));
//                            this.Tamkjlltutien[1]++;
//                            Service.getInstance().sendThongBao(this, "Bạn đã thăng cảnh giới thành công lên: "
//                                    + this.TamkjllTuviTutien(Util.maxInt(this.Tamkjlltutien[1])));
//                        } else {
//                            this.Tamkjlltutien[0] -= TamkjllDieukiencanhgioi(
//                                    Util.maxInt(this.Tamkjlltutien[1]));
//                            if (Util.isTrue(20f, 100)) {
//                                this.Tamkjll_Ma_cot += Util.nextInt(1, Util.nextInt(1, Util.nextInt(1, 5)));
//                                Service.getInstance().sendThongBaoOK(this, "Trong lúc tu tiên thất bại bạn nhận đc ma cốt");
//                            }
//                            Service.getInstance().sendThongBao(this,
//                                    "Bạn đã thăng cảnh giới thất bại và bị mất Exp tu tiên, cảnh giới bạn vẫn ở: "
//                                    + this.TamkjllTuviTutien(Util.maxInt(this.Tamkjlltutien[1])));
//                            this.setDie(this);
//                        }
                    //             }

                    if (this.Tamkjll_Ma_Hoa == 1
                            && Util.canDoWithTime(this.TamkjllLasttimeMaHoa, this.TamkjllTimeTuMa() * 60 * 1000)) {
                        this.TamkjllLasttimeMaHoa = System.currentTimeMillis();
                        this.Tamkjll_Ma_Hoa = 0;
                        this.nPoint.mp = 0;
                        this.nPoint.calPoint();
                        Service.getInstance().Send_Info_NV(this);
                        Service.getInstance().sendThongBaoOK(this, "Đã hết thời gian ma hóa");
                    }
                    UpdateEffChar.getInstance().updateEff(this);
                    BlackBallWar.gI().update(this);
                    if (this.isPl()) {
                        MabuWar.gI().update(this);
                        MabuWar14h.gI().update(this);
                        TranhNgoc.gI().update(this);
                         send_text_time_nhan_bua_mien_phi();
                         
                        if (Util.canDoWithTime(lastTimeUpdate, 60000)) {
                          this.playerTask.achivements.get(ConstAchive.HOAT_DONG_CHAM_CHI).count++;
                          // send_text_time_nhan_bua_mien_phi();
                       }
                    }
                    if (isGotoFuture && Util.canDoWithTime(lastTimeGoToFuture, 6000)) {
                        ChangeMapService.gI().changeMapBySpaceShip(this, 102, -1, Util.nextInt(60, 200));
                        this.isGotoFuture = false;
                    }
                       
                    if (isGoToBDKB && Util.canDoWithTime(lastTimeGoToBDKB, 6000)) {
                        ChangeMapService.gI().changeMapBySpaceShip(this, 135, -1, 35);
                        this.isGoToBDKB = false;
                    }
                    
                    if (isGoToBDKB && Util.canDoWithTime(22, 00)) {
                        ChangeMapService.gI().changeMapBySpaceShip(this, 135, -1, 35);
                        this.isGoToBDKB = false;
                    }
                    
                    if (isgotoPrimaryForest && Util.canDoWithTime(lastTimePrimaryForest, 6000)) {
                        ChangeMapService.gI().changeMap(this, 161, -1, 169, 312);
                        this.isgotoPrimaryForest = false;
                    }
                    if (setClothes.thienXinHang == 5
                            || setClothes.kirin == 5
                            || setClothes.songoku == 5
                            || setClothes.pikkoroDaimao == 5
                            || setClothes.picolo == 5
                            || setClothes.ocTieu == 5
                            || setClothes.cadic == 5
                            || setClothes.nappa == 5
                            
                            || setClothes.full_set_broly == 5
                            || setClothes.full_set_fide == 5
                            || setClothes.full_set_picolo == 5
                            || setClothes.full_set_vegeta == 5
                            || setClothes.setnrocuon == 5
                            
                            || setClothes.songoku1000pt == 1
                            || setClothes.antomic2000pt == 1
                            || setClothes.masenco3000pt == 1
                            || setClothes.setkichhoat18sao == 5
                            || setClothes.setkichhoat30sao == 5
                            || setClothes.setkichhoat45sao == 5
                            || setClothes.setkichhoat65sao == 5
                            || setClothes.setkichhoat99sao == 5
                            || setClothes.setkichhoat200sao == 5
                            || setClothes.setkichhoat300sao == 5
                            || setClothes.setkichhoat500sao == 5
                            || setClothes.setkichhoat700sao == 5
                            || setClothes.setkichhoat999sao == 5
                            || setClothes.kakarot == 5) {
                        Service.getInstance().sendTitle(this);
                    }
                    if (this.zone != null) {
                        TrapMap trap = this.zone.isInTrap(this);
                        if (trap != null) {
                            trap.doPlayer(this);
                        }
                    }
                } else {
                    if (Util.canDoWithTime(lastTimeBan, 5000)) {
                        Client.gI().kickSession(session);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.error(Player.class, e, "Lỗi tại player: " + this.name);
            }
        }
    }
    
    public boolean checkHutMau() {
        if (this.nPoint.tlHutHp > 0 || this.nPoint.tlHutMp > 0 || this.nPoint.tlHutHpMob > 0) {
            return true;
        }
        return false;
    }
     public long lastTimeSendTextTime;
     
     public void send_text_time_nhan_bua_mien_phi() {
        if (Util.canDoWithTime(lastTimeSendTextTime, 40000)) {
            if (this.CapTamkjll >= 0) {
                ItemTimeService.gI().sendTextTime(this, CheckDayOnl, "CHÚC BẠN CÓ TRẢI NGHIỆM TUYỆT Ở ĐÂY!\nHãy Tham Gia Đua Top, Săn Boss\nSự Kiện, Nạp Game, Up Đồ, Build Đồ", 30);
            }
            lastTimeSendTextTime = System.currentTimeMillis();
        }
    }
    
   
    
    
    private void checkLocation() {
        if (this.location.x > this.zone.map.mapWidth || this.location.x < 0
                || this.location.y > this.zone.map.mapHeight || this.location.y < 0) {
            if (this.inventory.gold >= 500000000) {
                this.inventory.subGold(500000000);
            } else {
                this.inventory.gold = 0;
            }
            PlayerService.gI().sendInfoHpMpMoney(this);
            ChangeMapService.gI().changeMapNonSpaceship(this, this.gender + 21, 400, 336);
            Service.getInstance().sendBigMessage(this, 1139, "|1|Do phát hiện có hành vi bất thường nên\n "
                    + "chúng tôi đã đưa bạn về nhà và xử phạt 500Tr vàng\n"
                    + "|7|nếu còn tiếp tục tái phạm sẽ khóa vĩnh viễn");
        }
    }
    //--------------------------------------------------------------------------
    /*
     * {380, 381, 382}: ht lưỡng long nhất thể xayda trái đất
     * {383, 384, 385}: ht porata xayda trái đất
     * {391, 392, 393}: ht namếc
     * {870, 871, 872}: ht c2 trái đất
     * {873, 874, 875}: ht c2 namếc
     * {867, 878, 869}: ht c2 xayda
     */
    private static final short[][] idOutfitFusion = {
        {380, 381, 382},//ht lưỡng long nhất thể xayda trái đất
        {383, 384, 385},//ht porata xayda trái đất
        {391, 392, 393},//ht namếc
        {870, 871, 872},//ht c2 trái đất
        {873, 874, 875},//ht c2 namếc
        {867, 868, 869},//ht c2 xayda
        {1939, 1940, 1941}, //ht c3 traidat
        {1942, 1943, 1944},
        {1942, 1943, 1944}, //ht c3 namek
    };
    
    public byte getEffFront() {
        if (this.inventory.itemsBody.isEmpty() || this.inventory.itemsBody.size() < 10) {
            return -1;
        }
        int[] levels = new int[5];
        ItemOption[] options = new ItemOption[5];
        Item[] items = new Item[]{
            this.inventory.itemsBody.get(0),
            this.inventory.itemsBody.get(1),
            this.inventory.itemsBody.get(2),
            this.inventory.itemsBody.get(3),
            this.inventory.itemsBody.get(4)
        };
        for (int i = 0; i < items.length; i++) {
            Item item = items[i];
            for (ItemOption io : item.itemOptions) {
                if (io.optionTemplate.id == 72) {
                    levels[i] = (int) io.param;
                    options[i] = io;
                    break;
                }
            }
        }
        int minLevel = Integer.MAX_VALUE;
        int count = 0;
        for (int level : levels) {
            if (level >= 4 && level <= 8) {
                minLevel = Math.min(minLevel, level);
                count++;
            }
        }
        if (count == 5) {
            return (byte) minLevel;
        } else {
            return -1;
        }
    }
    
    public byte getAura() {
        if (Manager.TOP_PLAYERS.contains(this.name)) {
            return 1;
        }
        CollectionBook book = getCollectionBook();
        if (book != null) {
            Card card = book.getCards().stream().filter(t -> t.isUse() && t.getCardTemplate().getAura() != -1).findAny().orElse(null);
            if (card != null) {
                return (byte) card.getCardTemplate().getAura();
            }
        }
        if (this.inventory.itemsBody.isEmpty()
                || this.inventory.itemsBody.size() < 10) {
            return -1;
        }
        Item item = this.inventory.itemsBody.get(5); // Gốc là 8
        if (!item.isNotNullItem()) {
            return 0;
        }
        if (item.template.id >= 0) {
            return 22;
        }
        
        return -1;
    }
    
    public boolean checkSkinFusion() {
        if (inventory != null && inventory.itemsBody.get(5).isNotNullItem()) {
            Short idct = inventory.itemsBody.get(5).template.id;
            if (idct >= 601 && idct <= 603 || idct >= 639 && idct <= 641) {
                return true;
            }
        }
        return false;
    }
    
    public short getHead() {
        if (this.id == 1000000) {
            return 412;
        }
        
        if (effectSkill != null) {
            if (effectSkill.isTranformation) {
                ItemTimeService itemTimeService = ItemTimeService.gI();
                int timeTransformation = this.effectSkill.timeTranformation / 1000;
                switch (this.gender) {
                    case 0:
                        itemTimeService.sendItemTime(this, 20958, timeTransformation);
                        return 1692;
                    case 1:
                        itemTimeService.sendItemTime(this, 20964, timeTransformation);
                        return 1709;
                    case 2:
                        itemTimeService.sendItemTime(this, 20952, timeTransformation);
                        return 1729;
                    default:
                        break;
                }
            } else if (effectSkill.isEvolution) {
                ItemTimeService itemTimeService = ItemTimeService.gI();
                int timeTransformation = this.effectSkill.timeTranformation / 1000;
                switch (this.gender) {
                    case 0:
                        switch (this.isbienhinh) {
                            case 1:
                                itemTimeService.removeItemTime(this, 20958);
                                itemTimeService.sendItemTime(this, 20959, timeTransformation);
                                return 1692;
                            case 2:
                                itemTimeService.removeItemTime(this, 20959);
                                itemTimeService.sendItemTime(this, 20960, timeTransformation);
                                return 1695;
                            case 3:
                                itemTimeService.removeItemTime(this, 20960);
                                itemTimeService.sendItemTime(this, 20961, timeTransformation);
                                return 1698;
                            case 4:
                                itemTimeService.removeItemTime(this, 20961);
                                itemTimeService.sendItemTime(this, 20962, timeTransformation);
                                return 1701;
                            case 5:
                                itemTimeService.removeItemTime(this, 20962);
                                itemTimeService.sendItemTime(this, 20963, timeTransformation);
                                return 1704;
                            default:
                                break;
                        }
                        break;
                    case 1:
                        switch (this.isbienhinh) {
                            case 1:
                                itemTimeService.removeItemTime(this, 20964);
                                itemTimeService.sendItemTime(this, 20965, timeTransformation);
                                return 1712;
                            case 2:
                                itemTimeService.removeItemTime(this, 20965);
                                itemTimeService.sendItemTime(this, 20966, timeTransformation);
                                return 1715;
                            case 3:
                                itemTimeService.removeItemTime(this, 20966);
                                itemTimeService.sendItemTime(this, 20967, timeTransformation);
                                return 1718;
                            case 4:
                                itemTimeService.removeItemTime(this, 20967);
                                itemTimeService.sendItemTime(this, 20968, timeTransformation);
                                return 1721;
                            case 5:
                                itemTimeService.removeItemTime(this, 20968);
                                itemTimeService.sendItemTime(this, 20969, timeTransformation);
                                return 1724;
                            default:
                                break;
                        }
                        break;
                    case 2:
                        switch (this.isbienhinh) {
                            case 1:
                                itemTimeService.removeItemTime(this, 20952);
                                itemTimeService.sendItemTime(this, 20953, timeTransformation);
                                return 1729;
                            case 2:
                                itemTimeService.removeItemTime(this, 20953);
                                itemTimeService.sendItemTime(this, 20954, timeTransformation);
                                return 1732;
                            case 3:
                                itemTimeService.removeItemTime(this, 20954);
                                itemTimeService.sendItemTime(this, 20955, timeTransformation);
                                return 1735;
                            case 4:
                                itemTimeService.removeItemTime(this, 20955);
                                itemTimeService.sendItemTime(this, 20956, timeTransformation);
                                return 1738;
                            case 5:
                                itemTimeService.removeItemTime(this, 20956);
                                itemTimeService.sendItemTime(this, 20957, timeTransformation);
                                return 1741;
                            default:
                                break;
                        }
                        break;
                    default:
                        break;
                }
            }
        }
        
        if (this.isPl() && this.pet != null && this.fusion != null && this.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            Item item = inventory.itemsBody.get(5);
            Item petItem = pet.inventory.itemsBody.get(5);
            
            if (item != null && petItem != null) {
                boolean hasItem1 = item.isNotNullItem() && (item.template.id == 2004 || item.template.id == 2005);
                boolean hasItem2 = petItem.isNotNullItem() && (petItem.template.id == 2004 || petItem.template.id == 2005);
                boolean sameItem = item.isNotNullItem() && petItem.isNotNullItem() && item.template.id == petItem.template.id;
                
                if (hasItem1 && hasItem2 && !sameItem) {
                    return 1305;
                }
            }
        }
        
        if (effectSkill != null) {
            if (effectSkill.isMonkey) {
                return (short) ConstPlayer.HEADMONKEY[effectSkill.levelMonkey - 1];
            } else if (effectSkill.isBienHinh) {
                return 180;
            } else if (effectSkill.isSocola) {
                return 412;
            }
        }
        
        if (effectSkin != null && effectSkin.isHoaDa) {
            return 454;
        }
        
        if (fusion != null && fusion.typeFusion != ConstPlayer.NON_FUSION) {
            if (checkSkinFusion()) {
                CaiTrang ct = Manager.getCaiTrangByItemId(inventory.itemsBody.get(5).template.id);
                return (short) (ct.getID()[0] != -1 ? ct.getID()[0] : inventory.itemsBody.get(5).template.part);
            } else if (fusion.typeFusion == ConstPlayer.LUONG_LONG_NHAT_THE) {
                return idOutfitFusion[this.gender == ConstPlayer.NAMEC ? 2 : 0][0];
            } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA) {
                return idOutfitFusion[this.gender == ConstPlayer.NAMEC ? 2 : 1][0];
            } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2) {
                return idOutfitFusion[3 + this.gender][0];
            } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATASS) {
                return idOutfitFusion[6][0];
            } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATASSS) {
                return idOutfitFusion[8][0];
            }
        }
        
        if (inventory != null) {
            Item item5 = inventory.itemsBody.get(5);
            if (item5 != null && item5.isNotNullItem()) {
                if (checkSkinFusion()) {
                    return this.head;
                }
                CaiTrang ct = Manager.gI().getCaiTrangByItemId(item5.template.id);
                if (ct != null) {
                    return (short) (ct.getID()[0] != -1 ? ct.getID()[0] : item5.template.part);
                }
            }
        }
        
        return this.head;
    }
    
    public short getBody() {
        // Kiểm tra id đặc biệt
        if (this.id == 1000000) {
            return 413;
        }

        // Kiểm tra trạng thái biến hình hoặc tiến hóa của effectSkill
        if (effectSkill != null) {
            if (effectSkill.isTranformation || effectSkill.isEvolution) {
                switch (this.gender) {
                    case 0:
                        return 1707;
                    case 1:
                        return 1727;
                    case 2:
                        return 1744;
                    default:
                        break;
                }
            }
        }

        // Kiểm tra điều kiện Pl, pet, và fusion
        if (this.isPl() && this.pet != null && this.fusion != null && this.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            Item item = inventory.itemsBody.get(5);
            Item petItem = pet.inventory.itemsBody.get(5);
            
            if (item != null && petItem != null) {
                boolean hasItem1 = item.isNotNullItem() && (item.template.id == 2004 || item.template.id == 2005);
                boolean hasItem2 = petItem.isNotNullItem() && (petItem.template.id == 2004 || petItem.template.id == 2005);
                boolean sameItem = item.isNotNullItem() && petItem.isNotNullItem() && item.template.id == petItem.template.id;
                
                if (hasItem1 && hasItem2 && !sameItem) {
                    return 1306;
                }
            }
        }

        // Kiểm tra các trạng thái của effectSkill
        if (effectSkill != null) {
            if (effectSkill.isMonkey) {
                return 193;
            } else if (effectSkill.isBienHinh) {
                return 181;
            } else if (effectSkill.isSocola) {
                return 413;
            }
        }

        // Kiểm tra trạng thái của effectSkin
        if (effectSkin != null && effectSkin.isHoaDa) {
            return 455;
        }

        // Kiểm tra các trạng thái fusion
        if (fusion != null && fusion.typeFusion != ConstPlayer.NON_FUSION) {
            if (checkSkinFusion()) {
                CaiTrang ct = Manager.gI().getCaiTrangByItemId(inventory.itemsBody.get(5).template.id);
                return (short) ct.getID()[1];
            }
            switch (this.fusion.typeFusion) {
                case ConstPlayer.LUONG_LONG_NHAT_THE:
                    return idOutfitFusion[this.gender == ConstPlayer.NAMEC ? 2 : 0][1];
                case ConstPlayer.HOP_THE_PORATA:
                    return idOutfitFusion[this.gender == ConstPlayer.NAMEC ? 2 : 1][1];
                case ConstPlayer.HOP_THE_PORATA2:
                    return idOutfitFusion[3 + this.gender][1];
                case ConstPlayer.HOP_THE_PORATASS:
                    return idOutfitFusion[6][1];
                case ConstPlayer.HOP_THE_PORATASSS:
                    return idOutfitFusion[8][1];
            }
        }

        // Kiểm tra các item trong inventory
        if (inventory != null) {
            Item item5 = inventory.itemsBody.get(5);
            if (item5 != null && item5.isNotNullItem()) {
                if (checkSkinFusion()) {
                    Item item0 = inventory.itemsBody.get(0);
                    if (item0 != null && item0.isNotNullItem()) {
                        return item0.template.part;
                    }
                }
                CaiTrang ct = Manager.gI().getCaiTrangByItemId(item5.template.id);
                if (ct != null && ct.getID()[1] != -1) {
                    return (short) ct.getID()[1];
                }
            }
            
            Item item0 = inventory.itemsBody.get(0);
            if (item0 != null && item0.isNotNullItem()) {
                return item0.template.part;
            }
        }

        // Trả về giá trị mặc định dựa trên gender
        return (short) (gender == ConstPlayer.NAMEC ? 59 : 57);
    }
    
    public short getLeg() {
        // Kiểm tra id đặc biệt
        if (this.id == 1000000) {
            return 414;
        }

        // Kiểm tra trạng thái biến hình hoặc tiến hóa của effectSkill
        if (effectSkill != null) {
            if (effectSkill.isTranformation || effectSkill.isEvolution) {
                switch (this.gender) {
                    case 0:
                        return 1708;
                    case 1:
                        return 1728;
                    case 2:
                        return 1745;
                    default:
                        break;
                }
            }
        }

        // Kiểm tra điều kiện Pl, pet, và fusion
        if (this.isPl() && this.pet != null && this.fusion != null && this.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            Item item = inventory.itemsBody.get(5);
            Item petItem = pet.inventory.itemsBody.get(5);
            
            if (item != null && petItem != null) {
                boolean hasItem1 = item.isNotNullItem() && (item.template.id == 2004 || item.template.id == 2005);
                boolean hasItem2 = petItem.isNotNullItem() && (petItem.template.id == 2004 || petItem.template.id == 2005);
                boolean sameItem = item.isNotNullItem() && petItem.isNotNullItem() && item.template.id == petItem.template.id;
                
                if (hasItem1 && hasItem2 && !sameItem) {
                    return 1307;
                }
            }
        }

        // Kiểm tra các trạng thái của effectSkill
        if (effectSkill != null) {
            if (effectSkill.isBienHinh) {
                return 182;
            } else if (effectSkill.isMonkey) {
                return 194;
            } else if (effectSkill.isSocola) {
                return 414;
            }
        }

        // Kiểm tra trạng thái của effectSkin
        if (effectSkin != null && effectSkin.isHoaDa) {
            return 456;
        }

        // Kiểm tra các trạng thái fusion
        if (fusion != null && fusion.typeFusion != ConstPlayer.NON_FUSION) {
            if (checkSkinFusion()) {
                CaiTrang ct = Manager.gI().getCaiTrangByItemId(inventory.itemsBody.get(5).template.id);
                return (short) ct.getID()[2];
            }
            switch (this.fusion.typeFusion) {
                case ConstPlayer.LUONG_LONG_NHAT_THE:
                    return idOutfitFusion[this.gender == ConstPlayer.NAMEC ? 2 : 0][2];
                case ConstPlayer.HOP_THE_PORATA:
                    return idOutfitFusion[this.gender == ConstPlayer.NAMEC ? 2 : 1][2];
                case ConstPlayer.HOP_THE_PORATA2:
                    return idOutfitFusion[3 + this.gender][2];
                case ConstPlayer.HOP_THE_PORATASS:
                    return idOutfitFusion[6][2];
                case ConstPlayer.HOP_THE_PORATASSS:
                    return idOutfitFusion[8][2];
            }
        }

        // Kiểm tra các item trong inventory
        if (inventory != null) {
            Item item5 = inventory.itemsBody.get(5);
            if (item5 != null && item5.isNotNullItem()) {
                if (checkSkinFusion()) {
                    Item item1 = inventory.itemsBody.get(1);
                    if (item1 != null && item1.isNotNullItem()) {
                        return item1.template.part;
                    }
                }
                CaiTrang ct = Manager.gI().getCaiTrangByItemId(item5.template.id);
                if (ct != null && ct.getID()[2] != -1) {
                    return (short) ct.getID()[2];
                }
            }
            
            Item item1 = inventory.itemsBody.get(1);
            if (item1 != null && item1.isNotNullItem()) {
                return item1.template.part;
            }
        }

        // Trả về giá trị mặc định dựa trên gender
        return (short) (gender == 1 ? 60 : 58);
    }
    
    public void chat(String text) {
        Service.getInstance().chat(this, text);
    }
    
    public short getFlagBag() {
        
        if (this.vip >= 199) {
            return 125;
        }
//        if (this.nPoint.power >= 0 ) {
//            return 80;
//          }

        if (this.isHoldBlackBall) {
            return 31;
        } else if (this.isHoldNamecBall) {
            return 30;
        }
        if (this.inventory.itemsBody.size() >= 8
                && this.inventory.itemsBody.get(7).isNotNullItem()) {
            FlagBag f = FlagBagService.gI().getFlagBagByName(this.inventory.itemsBody.get(7).template.name);
            if (f != null) {
                return (short) f.id;
            }
        }
        if (TaskService.gI().getIdTask(this) == ConstTask.TASK_3_2) {
            return 28;
        }
        if (this.clan != null) {
            return (short) this.clan.imgId;
        }
        return -1;
    }
    
    public short getMount() {
        if (this.isVersionAbove(220)) {
            for (Item item : inventory.itemsBody) {
                if (item.isNotNullItem()) {
                    if (item.template.type == 24) {
                        if (item.template.gender == 3 || item.template.gender == this.gender) {
                            return item.template.id;
                        } else {
                            return -1;
                        }
                    }
                    if (item.template.type == 23) {
                        if (item.template.id < 500) {
                            return item.template.id;
                        } else {
                            Object mount = DataGame.MAP_MOUNT_NUM.get(String.valueOf(item.template.id));
                            if (mount == null) {
                                return -1;
                            }
                            return (short) mount;
                        }
                    }
                }
            }
        } else {
            for (Item item : inventory.itemsBag) {
                if (item.isNotNullItem()) {
                    if (item.template.type == 24) {
                        if (item.template.gender == 3 || item.template.gender == this.gender) {
                            return item.template.id;
                        } else {
                            return -1;
                        }
                    }
                    if (item.template.type == 23) {
                        if (item.template.id < 500) {
                            return item.template.id;
                        } else {
                            Object mount = DataGame.MAP_MOUNT_NUM.get(String.valueOf(item.template.id));
                            if (mount == null) {
                                return -1;
                            }
                            return (short) mount;
                        }
                    }
                }
            }
        }
        return -1;
    }

    //--------------------------------------------------------------------------
    public double injured(Player plAtt, double damage, boolean piercing, boolean isMobAttack) {
        int mstChuong = this.nPoint.mstChuong;
        int giamst = this.nPoint.tlGiamst;
        if (!this.isDie()) {
            if (this.isMiniPet) {
                return 0;
            }
            if (plAtt != null) {
                if (!this.isBoss && plAtt.nPoint.xDameChuong && SkillUtil.isUseSkillChuong(plAtt)) {
                    damage = plAtt.nPoint.tlDameChuong * damage;
                    plAtt.nPoint.xDameChuong = false;
                }
                if (mstChuong > 0 && SkillUtil.isUseSkillChuong(plAtt)) {
                    PlayerService.gI().hoiPhuc(this, 0, damage * mstChuong / 100);
                    damage = 0;
                }
            }
            if (!SkillUtil.isUseSkillBoom(plAtt)) {
                if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 100)) {
                    return 0;
                }
            }
            damage = this.nPoint.subDameInjureWithDeff(damage);
            if (!piercing && effectSkill.isShielding) {
                if (damage > nPoint.hpMax) {
                    EffectSkillService.gI().breakShield(this);
                }
                damage = 1;
            }
            if (this.pet != null && this.pet.status < 3) {
                this.pet.dt(plAtt);
            }
            if (this.isPet && (((Pet) this).status < 3)) {
                ((Pet) this).dt(plAtt);
            }
            if (isMobAttack && this.charms.tdBatTu > System.currentTimeMillis() && damage >= this.nPoint.hp) {
                damage = this.nPoint.hp - 1;
            }
            if (giamst > 0) {
                damage -= nPoint.calPercent(damage, giamst);
            }
            if (this.effectSkill.isHoldMabu) {
                damage = 1;
            }
            
            this.nPoint.subHP(damage);
            if (this.effectSkill.isHoldMabu && Util.isTrue(30, 150)) {
                Service.getInstance().removeMabuEat(this);
            }
            if (isDie()) {
                if (plAtt != null) {
                    if (MapService.gI().isMapMabuWar(plAtt.zone.map.mapId) && MabuWar.gI().isTimeMabuWar()) {
                        plAtt.addPowerPoint(5);
                        Service.getInstance().sendPowerInfo(plAtt, "TL", plAtt.getPowerPoint());
                    }
                }
                setDie(plAtt);
            }
            if (isDie()) {
                if (this != null && this.zone != null && this.zone.map != null) {
                    if (this.zone.map.mapId == 212 && plAtt != null) { // Kiểm tra map 212 và nếu có người tấn công
                        plAtt.pointPvp++; // Tăng điểm PVP cho người tấn công

                        int currentHour = Util.getCurrentHour(); // Lấy giờ hiện tại

                        // Kiểm tra nếu giờ hiện tại nằm trong khoảng từ 21h đến 22h
                        if (currentHour >= 21 && currentHour < 22) {
                            // Cho phép PK và nhận điểm gia tộc trong khoảng 21h đến 22h
                            Service.getInstance().sendThongBao(plAtt, "Bạn nhận được 1 Điểm Gia Tộc");
                        } else {
                            // Sau 22h, khi người chơi tấn công, sẽ bị đưa về nhà
                            ChangeMapService.gI().changeMapBySpaceShip(plAtt, plAtt.gender + 21, -1, 250);
                            Service.getInstance().sendThongBao(plAtt, "Bạn đã bị đưa về nhà vì PK sau 22h.");
                        }
                    }
                }
                setDie(plAtt);
            }
            return damage;
        } else {
            if (this.isClone) {
                ChangeMapService.gI().exitMap(this);
            }
            return 0;
        }
    }
    public long lastTimeDie;
    
    public void setDie(Player plAtt) {
        lastTimeDie = System.currentTimeMillis();
        //xóa phù
        if (this.effectSkin.xHPKI > 1) {
            this.effectSkin.xHPKI = 1;
            Service.getInstance().point(this);
        }
        //xóa tụ skill đặc biệt
        this.playerSkill.prepareQCKK = false;
        this.playerSkill.prepareLaze = false;
        this.playerSkill.prepareTuSat = false;
        //xóa hiệu ứng skill
        this.effectSkill.removeSkillEffectWhenDie();
        //
        nPoint.setHp(0);
        nPoint.setMp(0);
        //xóa trứng
        if (this.mobMe != null) {
            this.mobMe.mobMeDie();
        }
        Service.getInstance().charDie(this);
        //add kẻ thù
        if (!this.isPet && !this.isBoss && plAtt != null && !plAtt.isPet && !plAtt.isBoss) {
            if (!plAtt.itemTime.isUseAnDanh) {
                FriendAndEnemyService.gI().addEnemy(this, plAtt);
            }
        }
        if (this.effectSkin.isSocola) {
            reward(plAtt);
        }
        if (MapService.gI().isMapMabuWar(this.zone.map.mapId)) {
            if (this.powerPoint < 20) {
                this.powerPoint = 0;
            }
            if (this.percentPowerPont < 100) {
                this.percentPowerPont = 0;
            }
        }
        //kết thúc pk
        PVPServcice.gI().finishPVP(this, PVP.TYPE_DIE);
        BlackBallWar.gI().dropBlackBall(this);
        if (isHoldNamecBall) {
            NamekBallWar.gI().dropBall(this);
        }
        if (isHoldNamecBallTranhDoat) {
            TranhNgocService.getInstance().dropBall(this, (byte) -1);
            TranhNgocService.getInstance().sendUpdateLift(this);
        }
    }
    
    public void rewardDuoiKhi(Player pl) {
//        System.out.println("REWARDDDDDDDDDDDDDDDDDDDDDĐD");
        if (pl != null) {
            int x = this.location.x;
            int y = this.zone.map.yPhysicInTop(x, this.location.y - 24);
            ItemMap itemMap = new ItemMap(this.zone, 579, 1, x, y, pl.id);
            RewardService.gI().initBaseOptionClothes(itemMap.itemTemplate.id, itemMap.itemTemplate.type, itemMap.options);
            if (itemMap != null) {
                Service.getInstance().dropItemMap(zone, itemMap);
            }
        }
    }
    
    public void reward(Player pl) {
        if (pl != null) {
            int x = this.location.x;
            int y = this.zone.map.yPhysicInTop(x, this.location.y - 24);
            ItemMap itemMap = new ItemMap(this.zone, 516, 1, x, y, pl.id);
            RewardService.gI().initBaseOptionClothes(itemMap.itemTemplate.id, itemMap.itemTemplate.type, itemMap.options);
            if (itemMap != null) {
                Service.getInstance().dropItemMap(zone, itemMap);
            }
        }
    }

    //--------------------------------------------------------------------------
    public void setClanMember() {
        if (this.clanMember != null) {
            this.clanMember.powerPoint = this.nPoint.power;
            this.clanMember.head = this.getHead();
            this.clanMember.body = this.getBody();
            this.clanMember.leg = this.getLeg();
        }
    }
    
    public boolean isAdmin() {
        return this.session.isAdmin;
    }
    
    public void setJustRevivaled() {
        this.justRevived = true;
        this.lastTimeRevived = System.currentTimeMillis();
        this.immortal = true;
    }
    
    public double TamkjllDameTuMa() {
        switch (this.Tamkjll_Tu_Ma) {
            case 0:
                return 50;
            case 1:
                return 250;
            case 2:
                return 680;
            case 3:
                return 2001;
            case 4:
                return 2700;
            case 5:
                return 3500;
            case 6:
                return 4999;
            case 7:
                return 5892;
            case 8:
                return 7000;
            case 9:
                return 7800;
            case 10:
                return 8900;
            case 11:
                return 9999;
            case 12:
                return 11111;
            case 13:
                return 12555;
            case 14:
                return 13888;
            case 15:
                return 15555;
            case 16:
                return 33333;
            default:
                return -33333;
        }
    }
    
    public String TamkjllNameHoncot(int Honcot) {
        switch (Honcot - 1) {
            case 0:
                return "-Đá Nữ Oa ";
            case 1:
                return "-Hiên Viên kiếm";
            case 2:
                return "-Côn Lôn Kính";
            case 3:
                return "-Đàn Phục Hi";
            case 4:
                return "-Chuông Đông Hoàng";
            case 5:
                return "-Bàn Cổ Phù";
            default:
                return "-Hồn xác lừa đời";
        }
    }
    
    public String TamkjllTuviTutien(int lvtt) {
        switch (lvtt) {
            case 0:
                return "Luyện khí Tầng 1";
            case 1:
                return "Luyện khí Tầng 2";
            case 2:
                return "Luyện khí Tầng 3";
            case 3:
                return "Luyện khí Tầng 4";
            case 4:
                return "Luyện khí Tầng 5";
            case 5:
                return "Luyện khí Tầng 6";
            case 6:
                return "Luyện khí Tầng 7";
            case 7:
                return "Luyện khí Tầng 8";
            case 8:
                return "Luyện khí Tầng 9";
            case 9:
                return "Luyện khí đỉnh phong";
            case 10:
                return "Trúc Cơ Tầng 1";
            case 11:
                return "Trúc Cơ Tầng 2";
            case 12:
                return "Trúc Cơ Tầng 3";
            case 13:
                return "Trúc Cơ Tầng 4";
            case 14:
                return "Trúc Cơ Tầng 5";
            case 15:
                return "Trúc Cơ Tầng 6";
            case 16:
                return "Trúc Cơ Tầng 7";
            case 17:
                return "Trúc Cơ Tầng 8";
            case 18:
                return "Trúc Cơ Tầng 9";
            case 19:
                return "Trúc Cơ đỉnh phong";
            case 20:
                return "Kim Đan Tầng 1";
            case 21:
                return "Kim Đan Tầng 2";
            case 22:
                return "Kim Đan Tầng 3";
            case 23:
                return "Kim Đan Tầng 4";
            case 24:
                return "Kim Đan Tầng 5";
            case 25:
                return "Kim Đan Tầng 6";
            case 26:
                return "Kim Đan Tầng 7";
            case 27:
                return "Kim Đan Tầng 8";
            case 28:
                return "Kim Đan Tầng 9";
            case 29:
                return "Kim Đan đỉnh phong";
            case 30:
                return "Nguyên Anh Tầng 1";
            case 31:
                return "Nguyên Anh Tầng 2";
            case 32:
                return "Nguyên Anh Tầng 3";
            case 33:
                return "Nguyên Anh Tầng 4";
            case 34:
                return "Nguyên Anh Tầng 5";
            case 35:
                return "Nguyên Anh Tầng 6";
            case 36:
                return "Nguyên Anh Tầng 7";
            case 37:
                return "Nguyên Anh Tầng 8";
            case 38:
                return "Nguyên Anh Tầng 9";
            case 39:
                return "Nguyên Anh đỉnh phong";
            case 40:
                return "Hóa Thần Tầng 1";
            case 41:
                return "Hóa Thần Tầng 2";
            case 42:
                return "Hóa Thần Tầng 3";
            case 43:
                return "Hóa Thần Tầng 4";
            case 44:
                return "Hóa Thần Tầng 5";
            case 45:
                return "Hóa Thần Tầng 6";
            case 46:
                return "Hóa Thần Tầng 7";
            case 47:
                return "Hóa Thần Tầng 8";
            case 48:
                return "Hóa Thần Tầng 9";
            case 49:
                return "Hóa Thần đỉnh phong";
            case 50:
                return "Luyện Hư Tầng 1";
            case 51:
                return "Luyện Hư Tầng 2";
            case 52:
                return "Luyện Hư Tầng 3";
            case 53:
                return "Luyện Hư Tầng 4";
            case 54:
                return "Luyện Hư Tầng 5";
            case 55:
                return "Luyện Hư Tầng 6";
            case 56:
                return "Luyện Hư Tầng 7";
            case 57:
                return "Luyện Hư Tầng 8";
            case 58:
                return "Luyện Hư Tầng 9";
            case 59:
                return "Luyện Hư đỉnh phong";
            case 60:
                return "Hợp Thể Tầng 1";
            case 61:
                return "Hợp Thể Tầng 2";
            case 62:
                return "Hợp Thể Tầng 3";
            case 63:
                return "Hợp Thể Tầng 4";
            case 64:
                return "Hợp Thể Tầng 5";
            case 65:
                return "Hợp Thể Tầng 6";
            case 66:
                return "Hợp Thể Tầng 7";
            case 67:
                return "Hợp Thể Tầng 8";
            case 68:
                return "Hợp Thể Tầng 9";
            case 69:
                return "Hợp Thể đỉnh phong";
            case 70:
                return "Quy Nguyên Tầng 1";
            case 71:
                return "Quy Nguyên Tầng 2";
            case 72:
                return "Quy Nguyên Tầng 3";
            case 73:
                return "Quy Nguyên Tầng 4";
            case 74:
                return "Quy Nguyên Tầng 5";
            case 75:
                return "Quy Nguyên Tầng 6";
            case 76:
                return "Quy Nguyên Tầng 7";
            case 77:
                return "Quy Nguyên Tầng 8";
            case 78:
                return "Quy Nguyên Tầng 9";
            case 79:
                return "Quy Nguyên đỉnh phong";
            case 80:
                return "Nhập Đạo Tầng 1";
            case 81:
                return "Nhập Đạo Tầng 2";
            case 82:
                return "Nhập Đạo Tầng 3";
            case 83:
                return "Nhập Đạo Tầng 4";
            case 84:
                return "Nhập Đạo Tầng 5";
            case 85:
                return "Nhập Đạo Tầng 6";
            case 86:
                return "Nhập Đạo Tầng 7";
            case 87:
                return "Nhập Đạo Tầng 8";
            case 88:
                return "Nhập Đạo Tầng 9";
            case 89:
                return "Nhập Đạo Đỉnh cao";
            case 90:
                return "Thánh Nhân Tầng 1";
            case 91:
                return "Thánh Nhân Tầng 2";
            case 93:
                return "Thánh Nhân Tầng 3";
            case 94:
                return "Thánh Nhân Tầng 4";
            case 95:
                return "Thánh Nhân Tầng 5";
            case 96:
                return "Thánh Nhân Tầng 6";
            case 97:
                return "Thánh Nhân Tầng 7";
            case 98:
                return "Thánh Nhân Tầng 8";
            case 99:
                return "Thánh Nhân Tầng 9";
            case 100:
                return "Thánh Nhân Đỉnh Phong";
            case 101:
                return "Thánh Vương Tầng 1";
            case 102:
                return "Thánh Vương Tầng 2";
            case 103:
                return "Thánh Vương Tầng 3";
            case 104:
                return "Thánh Vương Tầng 4";
            case 105:
                return "Thánh Vương Tầng 5";
            case 106:
                return "Thánh Vương Tầng 6";
            case 107:
                return "Thánh Vương Tầng 7";
            case 108:
                return "Thánh Vương Tầng 8";
            case 109:
                return "Thánh Vương Tầng 9";
            case 110:
                return "Thánh Vương Đỉnh Phong";
            case 111:
                return "Thánh Hoàng Tầng 1";
            case 112:
                return "Thánh Hoàng Tầng 2";
            case 113:
                return "Thánh Hoàng Tầng 3";
            case 114:
                return "Thánh Hoàng Tầng 4";
            case 115:
                return "Thánh Hoàng Tầng 5";
            case 116:
                return "Thánh Hoàng Tầng 6";
            case 117:
                return "Thánh Hoàng Tầng 7";
            case 118:
                return "Thánh Hoàng Tầng 8";
            case 119:
                return "Thánh Hoàng Tầng 9";
            case 120:
                return "Thánh Hoàng Đỉnh Phong";
            case 121:
                return "Thánh Tôn Tầng 1";
            case 122:
                return "Thánh Tôn Tầng 2";
            case 123:
                return "Thánh Tôn Tầng 3";
            case 124:
                return "Thánh Tôn Tầng 4";
            case 125:
                return "Thánh Tôn Tầng 5";
            case 126:
                return "Thánh Tôn Tầng 6";
            case 127:
                return "Thánh Tôn Tầng 7";
            case 128:
                return "Thánh Tôn Tầng 8";
            case 129:
                return "Thánh Tôn Tầng 9";
            case 130:
                return "Thánh Tôn Đỉnh Phong";
            case 131:
                return "Hậu Thiên Cổ Thánh Tầng 1";
            case 132:
                return "Hậu Thiên Cổ Thánh Tầng 2";
            case 133:
                return "Hậu Thiên Cổ Thánh Tầng 3";
            case 134:
                return "Hậu Thiên Cổ Thánh Tầng 4";
            case 135:
                return "Hậu Thiên Cổ Thánh Tầng 5";
            case 136:
                return "Hậu Thiên Cổ Thánh Tầng 6";
            case 137:
                return "Hậu Thiên Cổ Thánh Tầng 7";
            case 138:
                return "Hậu Thiên Cổ Thánh Tầng 8";
            case 139:
                return "Hậu Thiên Cổ Thánh Tầng 9";
            case 140:
                return "Hậu Thiên Cổ Thánh Đỉnh Phong";
            case 141:
                return "Tiên Thiên Cổ Thánh Tầng 1";
            case 142:
                return "Tiên Thiên Cổ Thánh Tầng 2";
            case 143:
                return "Tiên Thiên Cổ Thánh Tầng 3";
            case 144:
                return "Tiên Thiên Cổ Thánh Tầng 4";
            case 145:
                return "Tiên Thiên Cổ Thánh Tầng 5";
            case 146:
                return "Tiên Thiên Cổ Thánh Tầng 6";
            case 147:
                return "Tiên Thiên Cổ Thánh Tầng 7";
            case 148:
                return "Tiên Thiên Cổ Thánh Tầng 8";
            case 149:
                return "Tiên Thiên Cổ Thánh Tầng 9";
            case 150:
                return "Tiên Thiên Cổ Thánh Đỉnh Phong";
            case 151:
                return "Chí Tôn Thánh Hiền Tầng 1";
            case 152:
                return "Chí Tôn Thánh Hiền Tầng 2";
            case 153:
                return "Chí Tôn Thánh Hiền Tầng 3";
            case 154:
                return "Chí Tôn Thánh Hiền Tầng 4";
            case 155:
                return "Chí Tôn Thánh Hiền Tầng 5";
            case 156:
                return "Chí Tôn Thánh Hiền Tầng 6";
            case 157:
                return "Chí Tôn Thánh Hiền Tầng 7";
            case 158:
                return "Chí Tôn Thánh Hiền Tầng 8";
            case 159:
                return "Chí Tôn Thánh Hiền Tầng 9";
            case 160:
                return "Chí Tôn Thánh Hiền Đỉnh Phong";
            case 161:
                return "Chuẩn Đế Tầng 1";
            case 162:
                return "Chuẩn Đế Tầng 2";
            case 163:
                return "Chuẩn Đế Tầng 3";
            case 164:
                return "Chuẩn Đế Tầng 4";
            case 165:
                return "Chuẩn Đế Tầng 5";
            case 166:
                return "Chuẩn Đế Tầng 6";
            case 167:
                return "Chuẩn Đế Tầng 7";
            case 168:
                return "Chuẩn Đế Tầng 8";
            case 169:
                return "Chuẩn Đế Tầng 9";
            case 170:
                return "Chuẩn Đế Đỉnh Phong";
            case 171:
                return "Đại Đế Tầng 1";
            case 172:
                return "Đại Đế Tầng 2";
            case 173:
                return "Đại Đế Tầng 3";
            case 174:
                return "Đại Đế Tầng 4";
            case 175:
                return "Đại Đế Tầng 5";
            case 176:
                return "Đại Đế Tầng 6";
            case 177:
                return "Đại Đế Tầng 7";
            case 178:
                return "Đại Đế Tầng 8";
            case 179:
                return "Đại Đế Tầng 9";
            case 180:
                return "Đại Đế Đỉnh Phong";
            case 181:
                return "Cổ Đế Tầng 1";
            case 182:
                return "Cổ Đế Tầng 2";
            case 183:
                return "Cổ Đế Tầng 3";
            case 184:
                return "Cổ Đế Tầng 4";
            case 185:
                return "Cổ Đế Tầng 5";
            case 186:
                return "Cổ Đế Tầng 6";
            case 187:
                return "Cổ Đế Tầng 7";
            case 188:
                return "Cổ Đế Tầng 8";
            case 189:
                return "Cổ Đế Tầng 9";
            case 190:
                return "Cổ Đế Đỉnh Phong";
            case 191:
                return "Hồng Trần Tiên Tầng 1";
            case 192:
                return "Hồng Trần Tiên Tầng 2";
            case 193:
                return "Hồng Trần Tiên Tầng 3";
            case 194:
                return "Hồng Trần Tiên Tầng 4";
            case 195:
                return "Hồng Trần Tiên Tầng 5";
            case 196:
                return "Hồng Trần Tiên Tầng 6";
            case 197:
                return "Hồng Trần Tiên Tầng 7";
            case 198:
                return "Hồng Trần Tiên Tầng 8";
            case 199:
                return "Hồng Trần Tiên Tầng 9";
            case 200:
                return "Hồng Trần Tiên Đỉnh Phong";
            case 201:
                return "Địa Tiên Tầng 1";
            case 202:
                return "Địa Tiên Tầng 2";
            case 203:
                return "Địa Tiên Tầng 3";
            case 204:
                return "Địa Tiên Tầng 4";
            case 205:
                return "Địa Tiên Tầng 5";
            case 206:
                return "Địa Tiên Tầng 6";
            case 207:
                return "Địa Tiên Tầng 7";
            case 208:
                return "Địa Tiên Tầng 8";
            case 209:
                return "Địa Tiên Tầng 9";
            case 210:
                return "Địa Tiên Đỉnh Phong";
            case 211:
                return "Thiên Tiên Tầng 1";
            case 212:
                return "Thiên Tiên Tầng 2";
            case 213:
                return "Thiên Tiên Tầng 3";
            case 214:
                return "Thiên Tiên Tầng 4";
            case 215:
                return "Thiên Tiên Tầng 5";
            case 216:
                return "Thiên Tiên Tầng 6";
            case 217:
                return "Thiên Tiên Tầng 7";
            case 218:
                return "Thiên Tiên Tầng 8";
            case 219:
                return "Thiên Tiên Tầng 9";
            case 220:
                return "Thiên Tiên Đỉnh Phong";
            case 221:
                return "Thái Ất Chân Tiên Tầng 1";
            case 222:
                return "Thái Ất Chân Tiên Tầng 2";
            case 223:
                return "Thái Ất Chân Tiên Tầng 3";
            case 224:
                return "Thái Ất Chân Tiên Tầng 4";
            case 225:
                return "Thái Ất Chân Tiên Tầng 5";
            case 226:
                return "Thái Ất Chân Tiên Tầng 6";
            case 227:
                return "Thái Ất Chân Tiên Tầng 7";
            case 228:
                return "Thái Ất Chân Tiên Tầng 8";
            case 229:
                return "Thái Ất Chân Tiên Tầng 9";
            case 230:
                return "Thái Ất Chân Tiên Đỉnh Phong";
            case 231:
                return "Đại Chí Huyền Tiên Tầng 1";
            case 232:
                return "Đại Chí Huyền Tiên Tầng 2";
            case 233:
                return "Đại Chí Huyền Tiên Tầng 3";
            case 234:
                return "Đại Chí Huyền Tiên Tầng 4";
            case 235:
                return "Đại Chí Huyền Tiên Tầng 5";
            case 236:
                return "Đại Chí Huyền Tiên Tầng 6";
            case 237:
                return "Đại Chí Huyền Tiên Tầng 7";
            case 238:
                return "Đại Chí Huyền Tiên Tầng 8";
            case 239:
                return "Đại Chí Huyền Tiên Tầng 9";
            case 240:
                return "Đại Chí Huyền Tiên Đỉnh Phong";
            case 241:
                return "Đại La Kim Tiên Tầng 1";
            case 242:
                return "Đại La Kim Tiên Tầng 2";
            case 243:
                return "Đại La Kim Tiên Tầng 3";
            case 244:
                return "Đại La Kim Tiên Tầng 4";
            case 245:
                return "Đại La Kim Tiên Tầng 5";
            case 246:
                return "Đại La Kim Tiên Tầng 6";
            case 247:
                return "Đại La Kim Tiên Tầng 7";
            case 248:
                return "Đại La Kim Tiên Tầng 8";
            case 249:
                return "Đại La Kim Tiên Tầng 9";
            case 250:
                return "Đại La Kim Tiên Đỉnh Phong";
            case 251:
                return "Hỗn Nguyên Kim Tiên Tầng 1";
            case 252:
                return "Hỗn Nguyên Kim Tiên Tầng 2";
            case 253:
                return "Hỗn Nguyên Kim Tiên Tầng 3";
            case 254:
                return "Hỗn Nguyên Kim Tiên Tầng 4";
            case 255:
                return "Hỗn Nguyên Kim Tiên Tầng 5";
            case 256:
                return "Hỗn Nguyên Kim Tiên Tầng 6";
            case 257:
                return "Hỗn Nguyên Kim Tiên Tầng 7";
            case 258:
                return "Hỗn Nguyên Kim Tiên Tầng 8";
            case 259:
                return "Hỗn Nguyên Kim Tiên Tầng 9";
            case 260:
                return "Hỗn Nguyên Kim Tiên Đỉnh Phong";
            case 261:
                return "Tiên Vương Tầng 1";
            case 262:
                return "Tiên Vương Tầng 2";
            case 263:
                return "Tiên Vương Tầng 3";
            case 264:
                return "Tiên Vương Tầng 4";
            case 265:
                return "Tiên Vương Tầng 5";
            case 266:
                return "Tiên Vương Tầng 6";
            case 267:
                return "Tiên Vương Tầng 7";
            case 268:
                return "Tiên Vương Tầng 8";
            case 269:
                return "Tiên Vương Tầng 9";
            case 270:
                return "Tiên Vương Đỉnh Phong";
            case 271:
                return "Tiên Quân Tầng 1";
            case 272:
                return "Tiên Quân Tầng 2";
            case 273:
                return "Tiên Quân Tầng 3";
            case 274:
                return "Tiên Quân Tầng 4";
            case 275:
                return "Tiên Quân Tầng 5";
            case 276:
                return "Tiên Quân Tầng 6";
            case 277:
                return "Tiên Quân Tầng 7";
            case 278:
                return "Tiên Quân Tầng 8";
            case 279:
                return "Tiên Quân Tầng 9";
            case 280:
                return "Tiên Quân Đỉnh Phong";
            case 281:
                return "Tiên Tôn Tầng 1";
            case 282:
                return "Tiên Tôn Tầng 2";
            case 283:
                return "Tiên Tôn Tầng 3";
            case 284:
                return "Tiên Tôn Tầng 4";
            case 285:
                return "Tiên Tôn Tầng 5";
            case 286:
                return "Tiên Tôn Tầng 6";
            case 287:
                return "Tiên Tôn Tầng 7";
            case 288:
                return "Tiên Tôn Tầng 8";
            case 289:
                return "Tiên Tôn Tầng 9";
            case 290:
                return "Tiên Tôn Đỉnh Phong";
            case 291:
                return "Tiên Đế Tầng 1";
            case 292:
                return "Tiên Đế Tầng 2";
            case 293:
                return "Tiên Đế Tầng 3";
            case 294:
                return "Tiên Đế Tầng 4";
            case 295:
                return "Tiên Đế Tầng 5";
            case 296:
                return "Tiên Đế Tầng 6";
            case 297:
                return "Tiên Đế Tầng 7";
            case 298:
                return "Tiên Đế Tầng 8";
            case 299:
                return "Tiên Đế Tầng 9";
            case 300:
                return "Tiên Đế Đỉnh Phong";
            case 301:
                return "Chúa Tể Tầng 1";
            case 302:
                return "Chúa Tể Tầng 2";
            case 303:
                return "Chúa Tể Tầng 3";
            case 304:
                return "Chúa Tể Tầng 4";
            case 305:
                return "Chúa Tể Tầng 5";
            case 306:
                return "Chúa Tể Tầng 6";
            case 307:
                return "Chúa Tể Tầng 7";
            case 308:
                return "Chúa Tể Tầng 8";
            case 309:
                return "Chúa Tể Tầng 9";
            case 310:
                return "Chúa Tể Đỉnh Phong";
            case 311:
                return "Cấm Kỵ Tầng 1";
            case 312:
                return "Cấm Kỵ Tầng 2";
            case 313:
                return "Cấm Kỵ Tầng 3";
            case 314:
                return "Cấm Kỵ Tầng 4";
            case 315:
                return "Cấm Kỵ Tầng 5";
            case 316:
                return "Cấm Kỵ Tầng 6";
            case 317:
                return "Cấm Kỵ Tầng 9";
            case 318:
                return "Cấm Kỵ Tầng 8";
            case 319:
                return "Cấm Kỵ Tầng 9";
            case 320:
                return "Cấm Kỵ Đỉnh Phong";
            case 321:
                return "Hậu Thiên Cổ Thần Tầng 1";
            case 322:
                return "Hậu Thiên Cổ Thần Tầng 2";
            case 323:
                return "Hậu Thiên Cổ Thần Tầng 3";
            case 324:
                return "Hậu Thiên Cổ Thần Tầng 4";
            case 325:
                return "Hậu Thiên Cổ Thần Tầng 5";
            case 326:
                return "Hậu Thiên Cổ Thần Tầng 6";
            case 327:
                return "Hậu Thiên Cổ Thần Tầng 7";
            case 328:
                return "Hậu Thiên Cổ Thần Tầng 8";
            case 329:
                return "Hậu Thiên Cổ Thần Tầng 9";
            case 330:
                return "Hậu Thiên Cổ Thần Đỉnh Phong";
            case 331:
                return "Tiên Thiên Cổ Thần Tôn Tầng 1";
            case 332:
                return "Tiên Thiên Cổ Thần Tôn Tầng 2";
            case 333:
                return "Tiên Thiên Cổ Thần Tôn Tầng 3";
            case 334:
                return "Tiên Thiên Cổ Thần Tôn Tầng 4";
            case 335:
                return "Tiên Thiên Cổ Thần Tôn Tầng 5";
            case 336:
                return "Tiên Thiên Cổ Thần Tôn Tầng 6";
            case 337:
                return "Tiên Thiên Cổ Thần Tôn Tầng 7";
            case 338:
                return "Tiên Thiên Cổ Thần Tôn Tầng 8";
            case 339:
                return "Tiên Thiên Cổ Thần Tôn Tầng 9";
            case 340:
                return "Tiên Thiên Cổ Thần Tôn Đỉnh Phong";
            
            default:
                return "Phế vật";
        }
    }
    
    public int TamkjllDieukiencanhgioi(int lvtt) {
        switch (lvtt) {
            case 0:
            
            default:
                return Integer.MAX_VALUE;
        }
    }
    
    public float Tamkjlltilecanhgioi(int lvtt) {
        switch (lvtt) {
            case 0:
            
            default:
                return 10f;
        }
    }
    
    public int TamkjllDametutien(int lvtt) {
        if (lvtt >= 0 && lvtt <= 340) {
            return (int) (lvtt * 7);
        }
        return -1; // Trả về -1 nếu lvtt ngoài phạm vi 0-9
    }
    
    public int TamkjllDametutienSTCM(int lvtt) {
        if (lvtt >= 0 && lvtt <= 340) {
            return (int) (lvtt * 1);
        }
        return -1; // Trả về -1 nếu lvtt ngoài phạm vi 0-9
    }
    
    public int TamkjllDametutientnsm(int lvtt) {// mỗi cấp tăng 2% tnsm
        if (lvtt >= 0 && lvtt <= 340) {
            return (int) (lvtt * 8);
        }
        return -1; // Trả về -1 nếu lvtt ngoài phạm vi 0-9
    }
    
    public int TamkjllTimeTuMa() {
        switch (this.Tamkjll_Tu_Ma) {
            case 0:
                return 5;
            case 1:
                return 12;
            case 2:
                return 17;
            case 3:
                return 25;
            case 4:
                return 33;
            case 5:
                return 38;
            case 6:
                return 43;
            case 7:
                return 50;
            case 8:
                return 55;
            case 9:
                return 59;
            case 10:
                return 65;
            case 11:
                return 69;
            case 12:
                return 76;
            case 13:
                return 83;
            case 14:
                return 89;
            case 15:
                return 95;
            case 16:
                return 100;
            default:
                return -100;
        }
    }
    
    public String TamkjllNameTuMa() {
        switch (this.Tamkjll_Tu_Ma) {
            case 0:
                return "Tiểu ma nhân";
            case 1:
                return "Ma nhân";
            case 2:
                return "Bán Ma";
            case 3:
                return "Hóa ma";
            case 4:
                return "Thiên ma";
            case 5:
                return "Địa ma";
            case 6:
                return "Huyền ma";
            case 7:
                return "Bán ma hoàng";
            case 8:
                return "Ma hoàng sơ kì";
            case 9:
                return "Ma hoàng trung kì";
            case 10:
                return "Ma hoàng hậu kì";
            case 11:
                return "Ma hoàng đỉnh phong";
            case 12:
                return "Ma Thần sơ kì";
            case 13:
                return "Ma Thần trung kì";
            case 14:
                return "Ma Thần hậu kì";
            case 15:
                return "Ma Thần \u0111\u1EC9nh phong";
            case 16:
                return "Thiên Ma Thần";
            default:
                return "Súc vật";
        }
    }
    
    public double TamkjllGiapTuMa() {
        switch (this.Tamkjll_Tu_Ma) {
            case 0:
                return 25;
            case 1:
                return 50;
            case 2:
                return 80;
            case 3:
                return 201;
            case 4:
                return 270;
            case 5:
                return 350;
            case 6:
                return 499;
            case 7:
                return 582;
            case 8:
                return 700;
            case 9:
                return 780;
            case 10:
                return 890;
            case 11:
                return 999;
            case 12:
                return 1111;
            case 13:
                return 1255;
            case 14:
                return 1388;
            case 15:
                return 1555;
            case 16:
                return 3333;
            default:
                return -3333;
        }
    }
    
    public int TamkjllHpKiGiaptutien(int lvtt) {
        if (lvtt >= 0 && lvtt <= 340) {
            return (int) (lvtt * 4);
        }
        return -1; // Trả về -1 nếu lvtt ngoài phạm vi 0-9
    }
    
    public void dispose() {
        //    if (escortedBoss != null) {
        //    escortedBoss.stopEscorting();
        //  }
        if (skillSpecial != null) {
            skillSpecial.dispose();
            skillSpecial = null;
        }
        
        isDisposed = true;
        
    }

    //Offline
    public Mob mobTarget;
    
    public long lastTimeTargetMob;
    
    public long timeTargetMob;
    
    public long lastTimeAttack;
    
    public boolean isDie;
    public boolean isOffline;
    
    public HashMap<ItemMap, Integer> listItemPicked = new HashMap<>();
    
    public void moveTo(int x, int y) {
        PlayerService.gI().playerMove(this, x, y);
    }
    
    public Mob getMobAttack() {
        if (this.mobTarget == null || this.mobTarget.isDie() || !this.zone.equals(this.mobTarget.zone) || Util.canDoWithTime(lastTimeTargetMob, timeTargetMob)) {
            this.mobTarget = this.zone.mobs.stream().filter(m -> m != null && !m.isDie()).findFirst().orElse(null);
            this.lastTimeTargetMob = System.currentTimeMillis();
            this.timeTargetMob = 60000;
        }
        return this.mobTarget;
    }
    
    public void active() {
        this.update();
        if (this.isDie()) {
            if (!this.isDie) {
                this.lastTimeDie = System.currentTimeMillis();
                this.isDie = true;
            }
            if (Util.canDoWithTime(this.lastTimeDie, 30000)) {
                Service.gI().hsChar(this, this.nPoint.hpMax, this.nPoint.mpMax);
                this.isDie = false;
            } else {
                return;
            }
        }
        if (!SkillService.gI().canUseSkillWithMana(this)) {
            InventoryService.gI().eatPea(this);
        }
        if (this.nPoint.stamina * 100 / this.nPoint.maxStamina < 10) {
            if (InventoryService.gI().findItemBagByTemp(this, 221) != null) {
                UseItem.gI().eatGrapes(this, InventoryService.gI().findItemBagByTemp(this, 221));
            } else if (InventoryService.gI().findItemBagByTemp(this, 222) != null) {
                UseItem.gI().eatGrapes(this, (InventoryService.gI().findItemBagByTemp(this, 222)));
            } else {
                Client.gI().remove(this);
                this.dispose();
            }
        }
        this.attack();
    }
    
    public short getRangeCanAttackWithSkillSelect(int skillId) {
        if (skillId == Skill.KAMEJOKO || skillId == Skill.MASENKO || skillId == Skill.ANTOMIC) {
            return Skill.RANGE_ATTACK_CHIEU_CHUONG;
        }
        return Skill.RANGE_ATTACK_CHIEU_DAM;
    }
    
    public void attack() {
        if (this.effectSkill != null && this.effectSkill.isHaveEffectSkill() && this.skillSpecial.isStartSkillSpecial) {
            return;
        }
        try {
            if (this.mobTarget == null || this.mobTarget.isDie()) {
                this.mobTarget = getMobAttack();
            }
            this.playerSkill.skills.get(Util.nextInt(0, this.playerSkill.skills.size() - 1));
            if (Util.getDistance(this, this.mobTarget) <= this.getRangeCanAttackWithSkillSelect(this.playerSkill.skillSelect.template.id)) {
                if (SkillUtil.isUseSkillChuong(this)) {
                    this.moveTo(this.mobTarget.location.x + (Util.getOne(-1, 1) * Util.nextInt(20, 200)),
                            Util.nextInt(10) % 2 == 0 ? this.mobTarget.location.y : this.mobTarget.location.y);
                } else {
                    this.moveTo(this.mobTarget.location.x + (Util.getOne(-1, 1) * Util.nextInt(10, 40)),
                            Util.nextInt(10) % 2 == 0 ? this.mobTarget.location.y : this.mobTarget.location.y);
                }
                SkillService.gI().useSkill(this, null, this.mobTarget, null);
            } else {
                this.moveTo(this.mobTarget.location.x, this.mobTarget.location.y);
            }
        } catch (Exception e) {
        }
    }
}
