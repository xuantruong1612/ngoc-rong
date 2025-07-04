package nro.models.player;

import nro.attr.Attribute;
import nro.card.Card;
import nro.card.CollectionBook;
import nro.consts.ConstAttribute;
import nro.consts.ConstPlayer;
import nro.consts.ConstRatio;
import nro.models.clan.Buff;
import nro.models.intrinsic.Intrinsic;
import nro.models.item.Item;
import nro.models.item.ItemOption;
import nro.models.skill.Skill;
import nro.power.PowerLimit;
import nro.power.PowerLimitManager;
import nro.server.Manager;
import nro.server.ServerManager;
import nro.services.*;
import nro.utils.SkillUtil;
import nro.utils.Util;

import java.util.ArrayList;
import java.util.List;
import nro.consts.ConstPet;
import nro.services.func.ChangeMapService;

/**
 * @Stole By kita
 */
public class NPoint {

    public static final byte MAX_LIMIT = 9;

    private Player player;
    public boolean isCrit;
    public boolean isCrit100;

    private Intrinsic intrinsic;
    private int percentDameIntrinsic;
    public int dameAfter;

    public boolean TamkjllLucky;

    /*-----------------------Chỉ số cơ bản------------------------------------*/
    public byte numAttack;
    public short stamina, maxStamina;

    public byte limitPower;
    public long power;
    public long tiemNang;

    public double hp, hpMax, hpg;
    public double mp, mpMax, mpg;
    public double dame, dameg;
    public double def, defg;
    public int crit, critg;
    public byte speed = 9;

    public boolean teleport;

    public boolean khangTDHS;

    /**
     * Chỉ số cộng thêm
     */
    public double hpAdd, mpAdd, dameAdd, defAdd, critAdd, hpHoiAdd, mpHoiAdd;

    /**
     * //+#% sức đánh chí mạng
     */
    public List<Integer> tlDameCrit;

    public List<Integer> stlazer;
    public List<Integer> stqckk;

    public boolean buffExpSatellite, buffDefenseSatellite;

    /**
     * Tỉ lệ hp, mp cộng thêm
     */
    public List<Integer> tlHp, tlMp;

    /**
     * Tỉ lệ giáp cộng thêm
     */
    public List<Integer> tlDef;

    /**
     * Tỉ lệ sức đánh/ sức đánh khi đánh quái
     */
    public List<Integer> tlDame, tlDameAttMob;

    /**
     * Lượng hp, mp hồi mỗi 30s, mp hồi cho người khác
     */
    public double hpHoi, mpHoi, mpHoiCute;
    public double hpHoi1, mpHoi1;
    /**
     * Tỉ lệ hp, mp hồi cộng thêm
     */
    public short tlHpHoi, tlMpHoi;

    /**
     * Tỉ lệ hp, mp hồi bản thân và đồng đội cộng thêm
     */
    public short tlHpHoiBanThanVaDongDoi, tlMpHoiBanThanVaDongDoi;

    /**
     * Tỉ lệ hút hp, mp khi đánh, hp khi đánh quái
     */
    public double tlHutHp, tlHutMp, tlHutHpMob;

    /**
     * Tỉ lệ hút hp, mp xung quanh mỗi 5s
     */
    public double tlHutHpMpXQ;

    /**
     * Tỉ lệ phản sát thương
     */
    public double tlPST;

    /**
     * Tỉ lệ tiềm năng sức mạnh
     */
    public List<Integer> tlTNSM;
    public int tlTNSMPet;

    /**
     * Tỉ lệ vàng cộng thêm
     */
    public short tlGold;

    /**
     * Tỉ lệ né đòn
     */
    public short tlNeDon;

    /**
     * Tỉ lệ sức đánh đẹp cộng thêm cho bản thân và người xung quanh
     */
    public List<Integer> tlSDDep;

    /**
     * Tỉ lệ giảm sức đánh
     */
    public short tlSubSD;
    public List<Integer> tlSpeed;
    public int mstChuong;
    public int tlGiamst;

    /*------------------------Effect skin-------------------------------------*/
    public Item trainArmor;
    public boolean wornTrainArmor;
    public boolean wearingTrainArmor;

    public boolean wearingVoHinh;
    public boolean isKhongLanh;

    public short tlHpGiamODo;
    public short tangstbom;
    public PowerLimit powerLimit;
    public boolean wearingDrabula;
    public boolean wearingMabu;
    public boolean wearingBuiBui;

    public boolean wearingNezuko;
    public boolean wearingTanjiro;
    public boolean wearingInosuke;
    public boolean wearingInoHashi;
    public boolean wearingZenitsu;
    public int tlDameChuong;
    public boolean xDameChuong;
    public boolean wearingYacon;
    public boolean wearingRedNoelHat;
    public boolean wearingGrayNoelHat;
    public boolean wearingBlueNoelHat;
    public boolean wearingNoelHat;

    public NPoint(Player player) {
        this.player = player;
        this.tlHp = new ArrayList<>();
        this.tlMp = new ArrayList<>();
        this.tlDef = new ArrayList<>();
        this.tlDame = new ArrayList<>();
        this.tlDameAttMob = new ArrayList<>();
        this.tlSDDep = new ArrayList<>();
        this.stlazer = new ArrayList<>();
        this.stqckk = new ArrayList<>();
        this.tlTNSM = new ArrayList<>();
        this.tlDameCrit = new ArrayList<>();
        this.stlazer = new ArrayList<>();
        this.tlSpeed = new ArrayList<>();
    }

    public void initPowerLimit() {
        powerLimit = PowerLimitManager.getInstance().get(limitPower);
    }

    /*-------------------------------------------------------------------------*/
    /**
     * Tính toán mọi chỉ số sau khi có thay đổi
     */
    public void calPoint() {
        try {
            if (this.player.pet != null) {
                this.player.pet.nPoint.setPointWhenWearClothes();
            }
            this.setPointWhenWearClothes();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setPoint(ItemOption io) {
        switch (io.optionTemplate.id) {
            case 0: //Tấn công +#
                this.dameAdd += io.param;
                break;
            case 2: //HP, KI+#000
                this.hpAdd += io.param * 100;
                this.mpAdd += io.param * 100;
                break;
            case 3: // vô hiệu vả biến st chưởng thành ki
                this.mstChuong += io.param;
                break;
            case 238:
                this.stlazer.add(Util.maxInt(io.param));
                break;
            case 5: //+#% sức đánh chí mạng
                this.tlDameCrit.add(Util.maxInt(io.param));
                break;
            case 217: //+#% sức đánh chí mạng
                this.tlDameCrit.add(Util.maxInt(io.param));
                break;

            case 6: //HP+#
                this.hpAdd += io.param;
                break;
            case 7: //KI+#
                this.mpAdd += io.param;
                break;
            case 8: //Hút #% HP, KI xung quanh mỗi 5 giây
                this.tlHutHpMpXQ += io.param;
                break;
            case 14: //Chí mạng+#%
                this.critAdd += io.param;
                break;
            case 19: //Tấn công+#% khi đánh quái
                this.tlDameAttMob.add(Util.maxInt(io.param));
                break;
            case 22: //HP+#K
                this.hpAdd += io.param * 1000;
                break;
            case 23: //MP+#K
                this.mpAdd += io.param * 1000;
                break;
            case 24:
                this.wearingBuiBui = true;
                break;
            case 25:
                this.wearingYacon = true;
                break;
            case 26:
                this.wearingDrabula = true;
                this.player.effectSkin.lastTimeDrabula = System.currentTimeMillis();
                break;
            case 29:
                this.wearingMabu = true;
                break;
            case 27: //+# HP/30s
                this.hpHoiAdd += io.param;
                break;
            case 28: //+# KI/30s
                this.mpHoiAdd += io.param;
                break;
            case 33: //dịch chuyển tức thời
                this.teleport = true;
                break;
            case 47: //Giáp+#
                this.defAdd += io.param;
                break;
            case 48: //HP/KI+#
                this.hpAdd += io.param;
                this.mpAdd += io.param;
                break;
            case 49: //Tấn công+#%
            case 50: //Sức đánh+#%
                this.tlDame.add(Util.maxInt(io.param));
                break;
            case 52: //Sức đánh+#%
                this.tlDame.add(Util.maxInt(io.param));
                break;
            case 53: //HP+#%
                this.tlHp.add(Util.maxInt(io.param));
                break;
            case 54: //KI +#%
                this.tlMp.add(Util.maxInt(io.param));
                break;
            case 55: //+#% sức đánh chí mạng
                this.tlDameCrit.add(Util.maxInt(io.param));
                break;
            case 56: //HP+#%/30s
                this.tlHpHoi += io.param;
                break;
            case 57: //MP+#%/30s
                this.tlMpHoi += io.param;
                break;
            case 58: //Giáp+#
                this.defAdd += io.param;
                break;
            case 59: //Biến #% tấn công thành HP
                this.tlHutHp += io.param;
                break;
            case 60: //Biến #% tấn công thành MP
                this.tlHutMp += io.param;
                break;
            case 61: //+#% TN,SM
                this.tlTNSM.add(Util.maxInt(io.param));
                break;

            case 165: //Sức đánh+#%
                this.tlDame.add(Util.maxInt(io.param));
                break;
            case 166: //HP+#%
                this.tlHp.add(Util.maxInt(io.param));
                break;
            case 214: //Sức đánh+#%
                this.tlDame.add(Util.maxInt(io.param));
                break;
            case 210: //Sức đánh+#%
                this.tlDame.add(Util.maxInt(io.param));
                break;
            case 247: //Sức đánh+#%
                this.tlDame.add(Util.maxInt(io.param));
                break;
            case 248: //HP+#%
                this.tlHp.add(Util.maxInt(io.param));
                break;
            case 249: //KI +#%
                this.tlMp.add(Util.maxInt(io.param));
                break;
            case 250: //Giáp #%
                this.tlDef.add(Util.maxInt(io.param));
                break;

            case 211: //HP+#%
                this.tlHp.add(Util.maxInt(io.param));
                break;
            case 77: //HP+#%
                this.tlHp.add(Util.maxInt(io.param));
                break;
            case 215: //HP+#%
                this.tlHp.add(Util.maxInt(io.param));
                break;

            case 80: //HP+#%/30s
                this.tlHpHoi += io.param;
                break;
            case 81: //MP+#%/30s
                this.tlMpHoi += io.param;
                break;
            case 88: //Cộng #% exp khi đánh quái
                this.tlTNSM.add(Util.maxInt(io.param));
                break;
            case 94: //Giáp #%
                this.tlDef.add(Util.limitgioihan(io.param));
                break;
            case 213: //Giáp #%
                this.tlDef.add(Util.limitgioihan(io.param));
                break;

            case 95: //Biến #% tấn công thành HP
                this.tlHutHp += io.param;
                break;
            case 96: //Biến #% tấn công thành MP
                this.tlHutMp += io.param;
                break;
            case 97: //Phản #% sát thương
                this.tlPST += io.param;
                break;
            case 100: //+#% vàng từ quái
                this.tlGold += io.param;
                break;
            case 101: //+#% TN,SM
                this.tlTNSM.add(Util.maxInt(io.param));
                break;
            case 103: //KI +#%
                this.tlMp.add(Util.maxInt(io.param));
                break;
            case 216: //KI +#%
                this.tlMp.add(Util.maxInt(io.param));
                break;

            case 212: //KI +#%
                this.tlMp.add(Util.maxInt(io.param));
                break;

            case 104: //Biến #% tấn công quái thành HP
                this.tlHutHpMob += io.param;
                break;
            case 105: //Vô hình khi không đánh quái và boss
                this.wearingVoHinh = true;
                break;
            case 106: //Không ảnh hưởng bởi cái lạnh
                this.isKhongLanh = true;
                break;
            case 252: //#% Né đòn
                this.tlNeDon += io.param;
                break;
            case 251: //Hôi, giảm #% HP
                this.tlHpGiamODo += io.param;
                break;
            case 237: //Hôi, giảm #% HP
                this.tangstbom += io.param;
                break;
            case 114:
                this.tlSpeed.add(Util.maxInt(io.param));
                break;
            case 116: //Kháng thái dương hạ san
                this.khangTDHS = true;
                break;
            case 117: //Đẹp +#% SĐ cho mình và người xung quanh
                this.tlSDDep.add(Util.maxInt(io.param));
                break;

            case 147: //+#% sức đánh
                this.tlDame.add(Util.maxInt(io.param));
                break;
            case 156: //Giảm 50% sức đánh, HP, KI và +#% SM, TN, vàng từ quái
                this.tlSubSD += 50;
                this.tlTNSM.add(Util.maxInt(io.param));
                this.tlGold += io.param;
                break;
            case 160:
                this.tlTNSMPet += io.param;
                break;
            case 162: //Cute hồi #% KI/s bản thân và xung quanh
                this.mpHoiCute += io.param;
                break;
            case 173: //Phục hồi #% HP và KI cho đồng đội
                this.tlHpHoiBanThanVaDongDoi += io.param;
                this.tlMpHoiBanThanVaDongDoi += io.param;
                break;
            case 189:
                this.wearingNezuko = true;
                break;
            case 190:
                this.wearingTanjiro = true;
                break;
            case 191:
                this.wearingInoHashi = true;
                break;
            case 192:
                this.wearingInosuke = true;
                break;
            case 193:
                this.wearingZenitsu = true;
                break;
            case 194:
                this.tlDameChuong = 3;
                break;
            case 195:
                this.tlDameChuong = 4;
                break;
            case 236:
                this.stqckk.add(Util.maxInt(io.param));
                break;

        }
    }

    private void setPointWhenWearClothes() {
        resetPoint();
        int idbt = 454;
        int countbt = 0;
        for (Item item : this.player.inventory.itemsBag) {
            if (countbt >= 1) {
                break;
            }
            if (item.isNotNullItem()) {
                switch (this.player.fusion.typeFusion) {
                    case ConstPlayer.HOP_THE_PORATA2:
                        idbt = 921;
                        break;
                    case ConstPlayer.HOP_THE_PORATASS:
                        idbt = 1624;
                        break;
                    case ConstPlayer.HOP_THE_PORATASSS:
                        idbt = 1625;
                        break;
                }
                if (item.template.id == idbt) {
                    for (ItemOption io : item.itemOptions) {
                        switch (io.optionTemplate.id) {
                            case 80: //HP+#%/30s
                                this.tlHpHoi += io.param;
                                break;
                            case 81: //MP+#%/30s
                                this.tlMpHoi += io.param;
                                break;
                            case 50: //Sức đánh+#%
                                this.tlDame.add(Util.maxInt(io.param));
                                break;
                            case 210: //Sức đánh+#%
                                this.tlDame.add(Util.maxInt(io.param));
                                break;
                            case 214: //Sức đánh+#%
                                this.tlDame.add(Util.maxInt(io.param));
                                break;
                            case 247: //Sức đánh+#%
                                this.tlDame.add(Util.maxInt(io.param));
                                break;
                            case 248: //HP+#%
                                this.tlHp.add(Util.maxInt(io.param));
                                break;
                            case 249: //KI +#%
                                this.tlMp.add(Util.maxInt(io.param));
                                break;
                            case 250: //Giáp #%
                                this.tlDef.add(Util.maxInt(io.param));
                                break;

                            case 217: //+#% sức đánh chí mạng
                                this.tlDameCrit.add(Util.maxInt(io.param));
                                break;

                            case 52: //Sức đánh+#%
                                this.tlDame.add(Util.maxInt(io.param));
                                break;
                            case 53: //HP+#%
                                this.tlHp.add(Util.maxInt(io.param));
                                break;
                            case 54: //KI +#%
                                this.tlMp.add(Util.maxInt(io.param));
                                break;
                            case 55: //+#% sức đánh chí mạng
                                this.tlDameCrit.add(Util.maxInt(io.param));
                                break;
                            case 56: //HP+#%/30s
                                this.tlHpHoi += io.param;
                                break;
                            case 57: //MP+#%/30s
                                this.tlMpHoi += io.param;
                                break;
                            case 58: //Giáp+#
                                this.defAdd += io.param;
                                break;
                            case 59: //Biến #% tấn công thành HP
                                this.tlHutHp += io.param;
                                break;
                            case 60: //Biến #% tấn công thành MP
                                this.tlHutMp += io.param;
                                break;
                            case 61: //+#% TN,SM
                                this.tlTNSM.add(Util.maxInt(io.param));
                                break;

                            case 165: //Sức đánh+#%
                                this.tlDame.add(Util.maxInt(io.param));
                                break;
                            case 166: //HP+#%
                                this.tlHp.add(Util.maxInt(io.param));
                                break;
                            case 216: //KI +#%
                                this.tlMp.add(Util.maxInt(io.param));
                                break;
                            case 212: //KI +#%
                                this.tlMp.add(Util.maxInt(io.param));
                                break;
                            case 213: //Giáp #%
                                this.tlDef.add(Util.limitgioihan(io.param));
                                break;
                            case 215: //HP+#%
                                this.tlHp.add(Util.maxInt(io.param));
                                break;
                            case 211: //HP+#%
                                this.tlHp.add(Util.maxInt(io.param));
                                break;
                            case 77: //HP+#%
                                this.tlHp.add(Util.maxInt(io.param));
                                break;
                            case 94: //Giáp #%
                                this.tlDef.add(Util.limitgioihan(io.param));
                                break;

                            case 101: //+#% TN,SM
                                this.tlTNSM.add(Util.maxInt(io.param));
                                break;
                            case 103: //KI +#%
                                this.tlMp.add(Util.maxInt(io.param));
                                break;
                            case 252: //#% Né đòn
                                this.tlNeDon += io.param;
                                break;
                            case 14:// chí mạng
                                this.critAdd += io.param;
                                break;

                        }

                    }
                    countbt++;
                }
            }
        }

        player.setClothes.setnrocuon = 0;
        player.setClothes.setkichhoat18sao = 0;
        player.setClothes.setkichhoat30sao = 0;
        player.setClothes.setkichhoat45sao = 0;
        player.setClothes.setkichhoat65sao = 0;
        player.setClothes.setkichhoat99sao = 0;
        player.setClothes.setkichhoat200sao = 0;
        player.setClothes.full_set_broly = 0;
        player.setClothes.full_set_fide = 0;
        player.setClothes.full_set_picolo = 0;
        player.setClothes.full_set_vegeta = 0;
        player.setClothes.setkichhoat300sao = 0;
        player.setClothes.setkichhoat500sao = 0;
        player.setClothes.setkichhoat700sao = 0;
        player.setClothes.setkichhoat999sao = 0;
        player.setClothes.tinhluyen16 = 0;

        for (Item item : this.player.inventory.itemsBody) {
            if (item.isNotNullItem()) {
                int tempID = item.template.id;
                for (ItemOption op : item.itemOptions) {
                    switch (op.optionTemplate.id) {
                        case 179:
                            player.setClothes.setnrocuon++;
                            break;

                        case 136:
                            player.setClothes.setkichhoat99sao++;
                            break;
                        case 137:
                            player.setClothes.setkichhoat200sao++;
                            break;
                        case 138:
                            player.setClothes.setkichhoat300sao++;
                            break;
                        case 139:
                            player.setClothes.setkichhoat500sao++;
                            break;
                        case 140:
                            player.setClothes.setkichhoat700sao++;
                            break;
                        case 141:
                            player.setClothes.setkichhoat18sao++;
                            break;

                        case 142:
                            player.setClothes.setkichhoat30sao++;
                            break;
                        case 143:
                            player.setClothes.setkichhoat45sao++;
                            break;
                        case 144:
                            player.setClothes.setkichhoat65sao++;
                            break;
                        case 145:
                            player.setClothes.setkichhoat999sao++;
                            break;

                        case 146:
                            player.setClothes.tinhluyen16++;
                            break;
                        case 229:
                            player.setClothes.masenco3000pt++;
                            break;
                        case 230:
                            player.setClothes.antomic2000pt++;
                            break;
                        case 231:
                            player.setClothes.songoku1000pt++;
                            break;
                        case 176:
                            player.setClothes.full_set_fide++;
                            break;
                        case 177:
                            player.setClothes.full_set_broly++;
                            break;
                        case 178:
                            player.setClothes.full_set_picolo++;
                            break;
                        case 180:
                            player.setClothes.full_set_vegeta++;
                            break;

                    }

                }
                if (tempID >= 592 && tempID <= 594) {
                    teleport = true;
                }
                for (ItemOption io : item.itemOptions) {
                    setPoint(io);

                }

            }
        }
        List<Item> itemsBody = player.inventory.itemsBody;
        if (!player.isBoss && !player.isMiniPet) {
//            Item pants = itemsBody.get(1);
//            if (pants.isNotNullItem() && pants.getId() >= 691 && pants.getId() >= 693) {
//                player.event.setUseQuanHoa(true);
//            }
        }
        if (Manager.EVENT_SEVER == 3) {
            if (!this.player.isBoss && !this.player.isMiniPet) {
                if (itemsBody.get(5).isNotNullItem()) {
                    int tempID = itemsBody.get(5).getId();
                    switch (tempID) {
                        case 386:
                        case 389:
                        case 392:
                            wearingGrayNoelHat = true;
                            wearingNoelHat = true;
                            break;
                        case 387:
                        case 390:
                        case 393:
                            wearingRedNoelHat = true;
                            wearingNoelHat = true;
                            break;
                        case 388:
                        case 391:
                        case 394:
                            wearingBlueNoelHat = true;
                            wearingNoelHat = true;
                            break;
                        default:
                            wearingRedNoelHat = false;
                            wearingBlueNoelHat = false;
                            wearingGrayNoelHat = false;
                            wearingNoelHat = false;
                    }
                }
            }
        }
        CollectionBook book = player.getCollectionBook();

        if (book != null) {
            List<Card> cards = book.getCards();
            if (cards != null) {
                for (Card c : cards) {
                    if (c.getLevel() > 0) {
                        int index = 0;
                        for (ItemOption o : c.getCardTemplate().getOptions()) {
                            if ((index == 0 || c.isUse()) && c.getLevel() >= o.activeCard) {
                                setPoint(o);
                            }
                            index++;
                        }
                    }
                }
            }
        }
//        setDameTrainArmor();
        setBasePoint();
    }

//    private void setDameTrainArmor() {
//        if (!this.player.isPet && !this.player.isClone && !this.player.isBoss && !this.player.isMiniPet) {
//            try {
//                Item gtl = this.player.inventory.itemsBody.get(6);
//                if (gtl.isNotNullItem()) {
//                    this.wearingTrainArmor = true;
//                    this.wornTrainArmor = true;
//                    this.player.inventory.trainArmor = gtl;
//                    this.tlSubSD += ItemService.gI().getPercentTrainArmor(gtl);
//                } else {
//                    if (this.wornTrainArmor) {
//                        this.wearingTrainArmor = false;
//                        for (ItemOption io : this.player.inventory.trainArmor.itemOptions) {
//                            if (io.optionTemplate.id == 9 && io.param > 0) {
//                                this.tlDame.add(Util.maxInt(ItemService.gI().getPercentTrainArmor(this.player.inventory.trainArmor)));
//                                break;
//                            }
//                        }
//                    }
//                }
//            } catch (Exception e) {
//                Log.error("Lỗi get giáp tập luyện " + this.player.name);
//            }
//        }
//    }
    private void setNeDon() {
        //ngọc rồng đen 6 sao
        if (this.player.rewardBlackBall.timeOutOfDateReward[5] > System.currentTimeMillis()) {
            this.tlNeDon += RewardBlackBall.R6S;
        }
        if (this.player.itemTime != null
                && this.player.itemTime.ishokiem) {
            this.tlNeDon += 20;
            //  Service.getInstance().sendThongBao(player, "Đã Tăng 100% Né Đòn Trong 5p");
        }

    }

    private void setHpHoi() {
        this.hpHoi = calPercent(this.hpMax, 1);
        this.hpHoi += this.hpHoiAdd;
        this.hpHoi += calPercent(this.hpMax, this.tlHpHoi);
        this.hpHoi += calPercent(this.hpMax, this.tlHpHoiBanThanVaDongDoi);

        if (this.player.itemTime != null && this.player.itemTime.ismytom) {
            this.hpHoi1 += calPercent(this.hpMax, 2);
            //  Service.getInstance().sendThongBao(player, "Đã hồi 1% HP");
        }

        if (this.player.effectSkin.isNezuko) {
            this.hpHoi += calPercent(this.hpMax, 30);
            Service.getInstance().sendThongBao(player, "Đã hồi 30% HP");
        }
    }

    private void setMpHoi() {
        this.mpHoi = calPercent(this.mpMax, 1);
        this.mpHoi += this.mpHoiAdd;
        this.mpHoi += calPercent(this.mpMax, this.tlMpHoi);
        this.mpHoi += calPercent(this.mpMax, this.tlMpHoiBanThanVaDongDoi);

        if (this.player.itemTime != null && this.player.itemTime.ismytom) {
            this.mpHoi1 += calPercent(this.mpMax, 2);
            //  Service.getInstance().sendThongBao(player, "Đã hồi 1% mp");
        }
        if (this.player.effectSkin.isNezuko) {
            this.mpHoi += calPercent(this.mpMax, 2);
            // Service.getInstance().sendThongBao(player, "Đã hồi 1% ");
        }
    }

    private void setHpMax() {
        this.hpMax = this.hpg;
        this.hpMax += this.hpAdd;
        if (this.player.isPl() && this.player.Tamkjlltutien[2] >= 1) {
            this.hpMax += this.hpMax * this.player.TamkjllHpKiGiaptutien(Util.maxInt(this.player.Tamkjlltutien[1]))
                    / 100d;
        }
        //đồ
        for (Integer tl : this.tlHp) {
            if (tl == null) {
                Service.getInstance().sendThongBao(player, "Đã xảy ra lỗi!");
                continue;
            }
            this.hpMax += calPercent(this.hpMax, tl);
        }

        if (player.tangThap > 0) {
            this.hpMax += calPercent(this.hpMax, 10);
        }

        //set nappa
        if (this.player.setClothes.nappa == 5) {
            this.hpMax *= 2;
        }
       
        

        if (player.setClothes.full_set_broly == 5) {
            this.hpMax += calPercent(this.hpMax, 500);
            //  Service.getInstance().sendThongBao(player, "Đã tăng 100% sức đánh");
        }
        if (this.player.itemTime != null && this.player.itemTime.isnuocmiakhonglo) {
            this.hpMax *= 1.10;
        }
        if (this.player.itemTime != null && this.player.itemTime.isnuocmiathom) {
            this.hpMax *= 1.10;
        }
        if (this.player.itemTime != null && this.player.itemTime.isnuocmiasaurieng) {
            this.hpMax *= 1.10;
        }

        //ngọc rồng đen 2 sao
        if (this.player.rewardBlackBall.timeOutOfDateReward[1] > System.currentTimeMillis()) {
            this.hpMax += calPercent(this.hpMax, RewardBlackBall.R2S);
        }
        //khỉ
        if (this.player.effectSkill.isMonkey) {
            if (!this.player.isPet || (this.player.isPet
                    && ((Pet) this.player).status != Pet.FUSION)) {
                int percent = SkillUtil.getPercentHpMonkey(player.effectSkill.levelMonkey);
                if (this.player.isPl() && this.player.CapTamkjll >= 30) {
                    percent += this.player.CapTamkjll * 3d;
                }
                this.hpMax += calPercent(this.hpMax, percent);
            }
        }
        if (this.player.isPl() && !player.isBot) {
            if (this.player.TamkjllDauLaDaiLuc[9] == 1) {
                this.hpMax += this.hpMax * player.TamkjllDauLaDaiLuc[10] / 100d;
            }
            this.hpMax += this.hpMax * this.player.CapTamkjll / 100d;
            this.hpMax += this.hpMax * (this.player.TamkjllCS * 10d) / 100d;
            if (player.CapTamkjll >= 70) {
                this.hpMax += this.hpMax * (this.player.TamkjllCapPb * 20d) / 100d;
            }
            //  this.hpMax += 100000 * this.player.playerTask.taskMain.id;
            this.hpMax += 5000000 * player.TamkjllDauLaDaiLuc[0];
            this.hpMax += 10000000 * player.TamkjllDauLaDaiLuc[1];
            this.hpMax += 20000000 * player.TamkjllDauLaDaiLuc[2];
            this.hpMax += 40000000 * player.TamkjllDauLaDaiLuc[3];
            this.hpMax += 60000000 * player.TamkjllDauLaDaiLuc[4];
            this.hpMax += 80000000 * player.TamkjllDauLaDaiLuc[5];
            this.hpMax += 100000000 * player.TamkjllDauLaDaiLuc[6];
            if (this.player.TamkjllDauLaDaiLuc[17] == 1) {
                this.hpMax += player.TamkjllDauLaDaiLuc[18] * 3569d;
            }
        }
        if (this.player.isPet && ((Pet) this.player).master.CapTamkjll >= 500) {
            this.hpMax += this.hpMax * (((Pet) this.player).master.CapTamkjll / 10d) / 100d;
        }
        if (this.player.Tamkjll_Ma_Hoa == 1) {
            this.hpMax -= hpMax * 99d / 100d;
        }
        //pet mabư
        if (this.player.isPet) {
            int percent = ((Pet) this.player).getLever() * 4;
            if (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA
                    || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2
                    || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATASS
                    || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATASSS) {
                switch (((Pet) this.player).typePet) {
                    case ConstPet.MABU:
                        this.hpMax += calPercent(this.hpMax, 5);
                        break;
                    case ConstPet.BILL:
                        this.hpMax += calPercent(this.hpMax, 8);
                        break;
                    case ConstPet.VIDEL:
                        this.hpMax += calPercent(this.hpMax, percent);
                        break;
                    case ConstPet.GOKU_GOD:
                        this.hpMax += calPercent(this.hpMax, 100);
                        break;
                    case ConstPet.VEGETA_GOD:
                        this.hpMax += calPercent(this.hpMax, 100);
                        break;
                    case ConstPet.GOHAN_GOD:
                        this.hpMax += calPercent(this.hpMax, 100);
                        break;
                    case ConstPet.GOKU_GODD:
                        this.hpMax += calPercent(this.hpMax, 1000);
                        break;
                    case ConstPet.BAT_GIOI:
                        this.hpMax += calPercent(this.hpMax, 1000);
                        break;
                    case ConstPet.HANG_NGA:
                        this.hpMax += calPercent(this.hpMax, 2000);
                        break;
                    case ConstPet.VEGITO:
                        this.hpMax += calPercent(this.hpMax, 4000);
                        break;
                    case ConstPet.VEGITO_SSJ:
                        this.hpMax += calPercent(this.hpMax, 5000);
                        break;
                    case ConstPet.GOKU_ULTRA:
                        this.hpMax += calPercent(this.hpMax, 3000);
                        break;
                }
            }
        }

        if (this.player.effectSkill.isTranformation || this.player.effectSkill.isEvolution) {
            if (!this.player.isPet || (this.player.isPet && ((Pet) this.player).status != Pet.FUSION)) {
                int percent = SkillUtil.getPercentDameMonkey((byte) player.isbienhinh);
                if (player.effectSkill.levelTranformation == 1) {
                    this.hpMax += ((double) this.hpMax * percent * 5 / 100);
                } else if (player.effectSkill.levelTranformation == 2) {
                    this.hpMax += ((double) this.hpMax * percent * 10 / 100);
                } else if (player.effectSkill.levelTranformation == 3) {
                    this.hpMax += ((double) this.hpMax * percent * 20 / 100);
                } else if (player.effectSkill.levelTranformation == 4) {
                    this.hpMax += ((double) this.hpMax * percent * 30 / 100);
                } else if (player.effectSkill.levelTranformation == 5) {
                    this.hpMax += ((double) this.hpMax * percent * 40 / 100);
                } else if (player.effectSkill.levelTranformation == 6) {
                    this.hpMax += ((double) this.hpMax * percent * 50 / 100);
                }
            }
        }

        //phù
        if (this.player.zone != null && MapService.gI().isMapBlackBallWar(this.player.zone.map.mapId)) {
            this.hpMax *= this.player.effectSkin.xHPKI;
        }
        //phù mabu 14h
        if (this.player.zone != null && MapService.gI().isMapMabuWar14H(this.player.zone.map.mapId)) {
            this.hpMax += 1000000;
        }
        //+hp đệ
        if (this.player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            this.hpMax += this.player.pet.nPoint.hpMax;
        }
        //huýt sáo
        if (!this.player.isPet
                || (this.player.isPet
                && ((Pet) this.player).status != Pet.FUSION)) {
            if (this.player.effectSkill.tiLeHPHuytSao != 0) {
                this.hpMax += calPercent(this.hpMax, 1000);
            }
        }
        if (!this.player.isPet
                || (this.player.isPet
                && ((Pet) this.player).status != Pet.FUSION)) {
            if (this.player.effectSkill.tiLeHPHuytSao != 0) {
                this.hp += calPercent(this.hp, 1000);
            }
        }

        if (this.player.effectSkill.isTranformation || this.player.effectSkill.isEvolution) {
            if (!this.player.isPet || (this.player.isPet && ((Pet) this.player).status != Pet.FUSION)) {
                int percent = SkillUtil.getPercentDameMonkey((byte) player.isbienhinh);
                this.hpMax += ((long) this.hpMax * percent * 2 / 100);
            }
        }
        //bổ huyết
        if (this.player.itemTime != null && this.player.itemTime.isUseBoHuyet) {
            this.hpMax *= 2;
        }
        if (this.player.itemTime != null && this.player.itemTime.isBanhTrungThu1Trung) {
            this.hpMax += calPercent(this.hpMax, 10);
        }
        if (this.player.itemTime != null && this.player.itemTime.isBanhTrungThu2Trung) {
            this.hpMax += calPercent(this.hpMax, 20);
        }
        //bổ huyết 2
        if (this.player.itemTime != null && this.player.itemTime.isUseBoHuyet2) {
            this.hpMax += calPercent(hpMax, 120);
        }
        if (this.player.zone != null && MapService.gI().isMapCold(this.player.zone.map)
                && !this.isKhongLanh) {
            this.hpMax /= 2;
        }
        if (this.player.itemTime != null && this.player.itemTime.isBanhTrungThuGaQuay) {
            this.hpMax += calPercent(this.hpMax, 200);
        }
        if (this.player.zone != null && MapService.gI().ismapnguhanhson(this.player.zone.map)) {
            if (!Util.isTimeSpwam(00, 23)) {
                ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, -1, 250);
                Service.getInstance().sendThongBao(player, "Ngũ hành sơn đã đóng cửa");
            }
//            if (this.player.zone != null && MapService.gI().mapgiatoc(this.player.zone.map)) {
//            if (Util.canDoWithTime(1, 60)) {
//                ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, -1, 250);
//                Service.getInstance().sendThongBao(player, "Thánh Địa  đã đóng cửa");   
//            }  
//            }

        }
        if (!player.isBoss) {
            Attribute at = ServerManager.gI().getAttributeManager().find(ConstAttribute.HP);
            if (at != null && !at.isExpired()) {
                hpMax += calPercent(hpMax, at.getValue());
            }
        }
        if (this.player.itemTime != null) {
            if (this.player.itemTime.isUseBanhTet) {
                hpMax += calPercent(hpMax, 200);
            }
        }
        if (player.getBuff() == Buff.BUFF_HP) {
            hpMax += calPercent(hpMax, 20);
        }

    }

    // (hp sư phụ + hp đệ tử ) + 15%
    // (hp sư phụ + 15% +hp đệ tử)
    private void setHp() {
        if (this.hp > this.hpMax) {
            this.hp = this.hpMax;
        }
    }

    private void setMpMax() {
        this.mpMax = this.mpg;
        this.mpMax += this.mpAdd;
        if (this.player.isPl() && this.player.Tamkjlltutien[2] >= 1) {
            this.mpMax += this.mpMax * this.player.TamkjllHpKiGiaptutien(Util.maxInt(this.player.Tamkjlltutien[1]))
                    / 100d;
        }
        if (player.tangThap > 0) {
            this.mpMax += calPercent(this.mpMax, 10);
        }
        if (this.player.isPl() && !this.player.isBot) {
            if (this.player.TamkjllDauLaDaiLuc[9] == 1) {
                this.mpMax += this.mpMax * player.TamkjllDauLaDaiLuc[10] / 100d;
            }
            this.mpMax += this.mpMax * this.player.CapTamkjll / 100d;
            this.mpMax += this.mpMax * (this.player.TamkjllCS * 10d) / 100d;
            if (player.CapTamkjll >= 30) {
                this.mpMax += this.mpMax * (this.player.TamkjllCapPb * 30d) / 100d;
            }
            //  this.mpMax += 1000 * this.player.playerTask.taskMain.id;
        }
        if (this.player.isPet && ((Pet) this.player).master.CapTamkjll >= 500) {
            this.mpMax += this.mpMax * (((Pet) this.player).master.CapTamkjll / 5d) / 100d;
        }
        //đồ
        for (Integer tl : this.tlMp) {
            this.mpMax += calPercent(this.mpMax, tl);
        }
        if (this.player.setClothes.picolo == 5) {
            this.mpMax *= 2;
        }
        if (this.player.effectSkill.isThoiMien) {
            // Kiểm tra xem người chơi có đang chiến đấu với boss không
            if (!player.isBoss) { // Nếu không phải boss
                this.mpMax -= calPercent(this.mpMax, 98);
                Service.getInstance().sendThongBao(player, "Bạn Bị Thôi Miên\nTrong 30s\ngiảm hp ki 98% ");
            } else {
                Service.getInstance().sendThongBao(player, "Bạn đang chiến đấu với boss, không bị ảnh hưởng bởi thôi miên.");
            }
        }
        //ngọc rồng đen 3 sao
        if (this.player.rewardBlackBall.timeOutOfDateReward[2] > System.currentTimeMillis()) {
            this.mpMax += calPercent(this.mpMax, RewardBlackBall.R3S);
        }
        ///pet mabư
        if (this.player.isPet) {
            int percent = ((Pet) this.player).getLever() * 4;
            if (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA
                    || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2
                    || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATASS
                    || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATASSS) {
                switch (((Pet) this.player).typePet) {
                    case ConstPet.MABU:
                        this.mpMax += calPercent(this.mpMax, 5);
                        break;
                    case ConstPet.BILL:
                        this.mpMax += calPercent(this.mpMax, 8);
                        break;
                    case ConstPet.VIDEL:
                        this.mpMax += calPercent(this.mpMax, percent);
                        break;
                    case ConstPet.GOKU_GODD:
                        this.mpMax += calPercent(this.mpMax, 1000);
                        break;
                    case ConstPet.BAT_GIOI:
                        this.mpMax += calPercent(this.mpMax, 1000);
                        break;
                    case ConstPet.HANG_NGA:
                        this.mpMax += calPercent(this.mpMax, 2000);
                        break;
                    case ConstPet.VEGITO:
                        this.mpMax += calPercent(this.mpMax, 4000);
                        break;
                    case ConstPet.VEGITO_SSJ:
                        this.mpMax += calPercent(this.mpMax, 5000);
                        break;
                    case ConstPet.GOKU_ULTRA:
                        this.mpMax += calPercent(this.mpMax, 3000);
                        break;
                }
            }
        }

        if (this.player.effectSkill.isTranformation || this.player.effectSkill.isEvolution) {
            if (!this.player.isPet || (this.player.isPet && ((Pet) this.player).status != Pet.FUSION)) {
                int percent = SkillUtil.getPercentDameMonkey((byte) player.isbienhinh);
                if (player.effectSkill.levelTranformation == 1) {
                    this.mpMax += ((double) this.mpMax * percent * 5 / 100);
                } else if (player.effectSkill.levelTranformation == 2) {
                    this.mpMax += ((double) this.mpMax * percent * 10 / 100);
                } else if (player.effectSkill.levelTranformation == 3) {
                    this.mpMax += ((double) this.mpMax * percent * 20 / 100);
                } else if (player.effectSkill.levelTranformation == 4) {
                    this.mpMax += ((double) this.mpMax * percent * 30 / 100);
                } else if (player.effectSkill.levelTranformation == 5) {
                    this.mpMax += ((double) this.mpMax * percent * 40 / 100);
                } else if (player.effectSkill.levelTranformation == 6) {
                    this.mpMax += ((double) this.mpMax * percent * 50 / 100);
                }
            }
        }

        //hợp thể
        if (this.player.fusion.typeFusion != 0) {
            this.mpMax += this.player.pet.nPoint.mpMax;
        }
        if (this.player.effectSkill.isTranformation || this.player.effectSkill.isEvolution) {
            if (!this.player.isPet || (this.player.isPet && ((Pet) this.player).status != Pet.FUSION)) {
                int percent = SkillUtil.getPercentDameMonkey((byte) player.isbienhinh);
                this.mpMax += ((long) this.mpMax * percent * 2 / 100);
            }
        }
        //bổ khí
        if (this.player.itemTime != null && this.player.itemTime.isUseBoKhi) {
            this.mpMax *= 2;
        }
        //bổ khí 2
        if (this.player.itemTime != null && this.player.itemTime.isUseBoKhi2) {
            this.mpMax += calPercent(mpMax, 120);
        }
        //phù
        if (this.player.zone != null && MapService.gI().isMapBlackBallWar(this.player.zone.map.mapId)) {
            this.mpMax *= this.player.effectSkin.xHPKI;
        }
        //phù mabu 14h
        if (this.player.zone != null && MapService.gI().isMapMabuWar14H(this.player.zone.map.mapId)) {
            this.mpMax += 1000000;
        }
        //xiên cá
//        if (this.player.effectFlagBag.useXienCa) {
//            this.mpMax += calPercent(this.mpMax, 15);
//        }
        //Kiem z
//        if (this.player.effectFlagBag.useKiemz) {
//            this.mpMax += calPercent(this.mpMax, 20);
//        }
        if (this.player.itemTime != null && this.player.itemTime.isBanhTrungThu1Trung) {
            this.mpMax += calPercent(this.mpMax, 10);
        }
        if (this.player.itemTime != null && this.player.itemTime.isBanhTrungThu2Trung) {
            this.mpMax += calPercent(this.mpMax, 20);
        }
        if (this.player.effectFlagBag.useDieuRong) {
            this.mpMax += calPercent(this.mpMax, 30);
        }
        if (this.player.effectFlagBag.useHoaVang || this.player.effectFlagBag.useHoaHong) {
            this.mpMax += calPercent(this.mpMax, 20);
        }
        if (!player.isBoss) {
            Attribute at = ServerManager.gI().getAttributeManager().find(ConstAttribute.KI);
            if (at != null && !at.isExpired()) {
                mpMax += calPercent(mpMax, at.getValue());
            }
        }
        if (this.player.itemTime != null) {
            if (this.player.itemTime.isUseBanhTet) {
                mpMax += calPercent(mpMax, 20);
            }
        }
        if (player.getBuff() == Buff.BUFF_KI) {
            mpMax += calPercent(mpMax, 20);
        }
    }

    private void setMp() {
        if (this.mp > this.mpMax) {
            this.mp = this.mpMax;
        }
    }

    private void setDame() {
        this.dame = this.dameg;
        this.dame += this.dameAdd;
        if (this.player.isPl() && this.player.Tamkjlltutien[2] >= 1) {
            this.dame += this.dame * this.player.TamkjllDametutien(Util.maxInt(this.player.Tamkjlltutien[1])) / 100d;
        }
        //đồ
        for (Integer tl : this.tlDame) {
            this.dame += calPercent(this.dame, tl);
        }
        for (Integer tl : this.tlSDDep) {
            this.dame += calPercent(this.dame, tl);
        }
        if (player.tangThap >= 1) {
            this.dame += calPercent(this.dame, 10);
        }
        //pet mabư
        if (this.player.effectSkill.isMonkey) {
            if (!this.player.isPet || (this.player.isPet
                    && ((Pet) this.player).status != Pet.FUSION)) {
                int percent = SkillUtil.getPercentHpMonkey(player.effectSkill.levelMonkey);
                if (this.player.CapTamkjll >= 70 && this.player.isPl()) {
                    percent += this.player.CapTamkjll;
                }
                this.dame += calPercent(this.dame, percent);
            }
        }
        if (this.player.isPl() && !this.player.isBot) {
            if (this.player.TamkjllDauLaDaiLuc[9] == 1) {
                this.dame += this.dame * player.TamkjllDauLaDaiLuc[10] / 100d;
            }
            this.dame += this.player.LbTamkjll * 10000;
            this.dame += this.dame * this.player.CapTamkjll / 100d;
            this.dame += this.dame * (this.player.TamkjllCS * 10d) / 100d;
            if (player.CapTamkjll >= 100) {
                this.dame += this.dame * (this.player.TamkjllCapPb * 10d) / 100d;
            }
            if (this.player.TamkjllDauLaDaiLuc[15] == 1) {
                this.dame += this.player.TamkjllDauLaDaiLuc[16] * 2369d;
            }
            //  this.dame += 500000 * this.player.playerTask.taskMain.id;
            this.dame += 5000000 * player.TamkjllDauLaDaiLuc[0];
            this.dame += 10000000 * player.TamkjllDauLaDaiLuc[1];
            this.dame += 20000000 * player.TamkjllDauLaDaiLuc[2];
            this.dame += 40000000 * player.TamkjllDauLaDaiLuc[3];
            this.dame += 60000000 * player.TamkjllDauLaDaiLuc[4];
            this.dame += 80000000 * player.TamkjllDauLaDaiLuc[5];
            this.dame += 100000000 * player.TamkjllDauLaDaiLuc[6];
        }
        if (this.player.Tamkjll_Ma_cot >= 1) {
            double damemahoa = this.player.TamkjllDameTuMa();
            if (this.player.Tamkjll_Ma_Hoa == 1) {
                damemahoa *= 2;
            }
            this.dame += dame * damemahoa / 100d;
        }
        if (this.player.isPet && ((Pet) this.player).master.CapTamkjll >= 500) {
            this.dame += this.dame * (((Pet) this.player).master.CapTamkjll / 15) / 100d;
        }

        if (player.setClothes.setnrocuon == 5) {
            this.dame += calPercent(this.dame, 100);
            //  Service.getInstance().sendThongBao(player, "Đã tăng 100% sức đánh");
        }

        if (player.setClothes.setkichhoat18sao == 5) {
            this.dame += calPercent(this.dame, 50);
        }
        if (player.setClothes.setkichhoat30sao == 5) {
            this.dame += calPercent(this.dame, 100);
        }

        if (player.setClothes.setkichhoat45sao == 5) {
            this.dame += calPercent(this.dame, 150);
        }
        if (player.setClothes.setkichhoat65sao == 5) {
            this.dame += calPercent(this.dame, 250);
        }
        if (player.setClothes.setkichhoat99sao == 5) {
            this.dame += calPercent(this.dame, 500);
        }
        if (player.setClothes.setkichhoat200sao == 5) {
            this.dame += calPercent(this.dame, 1200);
        }
        if (player.setClothes.setkichhoat300sao == 5) {
            this.dame += calPercent(this.dame, 2500);
        }
        if (player.setClothes.setkichhoat500sao == 5) {
            this.dame += calPercent(this.dame, 5000);
        }
        if (player.setClothes.setkichhoat700sao == 5) {
            this.dame += calPercent(this.dame, 8000);
        }
        if (player.setClothes.setkichhoat999sao == 5) {
            this.dame += calPercent(this.dame, 20000);
        }
        //hp
        if (player.setClothes.setkichhoat18sao == 5) {
            this.hpMax += calPercent(this.hpMax, 50);
        }
        if (player.setClothes.setkichhoat30sao == 5) {
            this.hpMax += calPercent(this.hpMax, 100);
        }

        if (player.setClothes.setkichhoat45sao == 5) {
            this.hpMax += calPercent(this.hpMax, 150);
        }
        if (player.setClothes.setkichhoat65sao == 5) {
            this.hpMax += calPercent(this.hpMax, 250);
        }
        if (player.setClothes.setkichhoat99sao == 5) {
            this.hpMax += calPercent(this.hpMax, 500);
        }
        if (player.setClothes.setkichhoat200sao == 5) {
            this.hpMax += calPercent(this.hpMax, 1250);
        }
        if (player.setClothes.setkichhoat300sao == 5) {
            this.hpMax += calPercent(this.hpMax, 2500);
        }
        if (player.setClothes.setkichhoat500sao == 5) {
            this.hpMax += calPercent(this.hpMax, 5000);
        }
        if (player.setClothes.setkichhoat700sao == 5) {
            this.hpMax += calPercent(this.hpMax, 8000);
        }
        if (player.setClothes.setkichhoat999sao == 5) {
            this.hpMax += calPercent(this.hpMax, 20000);
        }

        //ki
        if (player.setClothes.setkichhoat18sao == 5) {
            this.mpMax += calPercent(this.mpMax, 50);
        }
        if (player.setClothes.setkichhoat30sao == 5) {
            this.mpMax += calPercent(this.mpMax, 100);
        }

        if (player.setClothes.setkichhoat45sao == 5) {
            this.mpMax += calPercent(this.mpMax, 150);
        }
        if (player.setClothes.setkichhoat65sao == 5) {
            this.mpMax += calPercent(this.mpMax, 250);
        }
        if (player.setClothes.setkichhoat99sao == 5) {
            this.mpMax += calPercent(this.mpMax, 500);
        }
        if (player.setClothes.setkichhoat200sao == 5) {
            this.mpMax += calPercent(this.mpMax, 1250);
        }
        if (player.setClothes.setkichhoat300sao == 5) {
            this.mpMax += calPercent(this.mpMax, 2500);
        }
        if (player.setClothes.setkichhoat500sao == 5) {
            this.mpMax += calPercent(this.mpMax, 5000);
        }
        if (player.setClothes.setkichhoat700sao == 5) {
            this.mpMax += calPercent(this.mpMax, 8000);
        }
        if (player.setClothes.setkichhoat999sao == 5) {
            this.mpMax += calPercent(this.mpMax, 20000);
        }
        if (player.setClothes.antomic2000pt == 1) {
            this.dame += calPercent(this.dame, 20000);
        }

        if (this.player.effectSkill.isSocola) {
            this.dame -= calPercent(this.dame, 80);
            Service.getInstance().sendThongBao(player, "Bạn Bị giảm 80% Sức Đánh\nTrong 30s\nVì Bị Trúng Chiêu sô cô la... ");
        }

        if (this.player.effectSkin.isNezuko) {
            this.dame += calPercent(this.dame, 12);
            Service.getInstance().sendThongBao(player, "Đã tăng 12% sức đánh");
        }
        if (this.player.isPet) {
            int percent = ((Pet) this.player).getLever() * 4;
            if (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA
                    || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2
                    || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATASS
                    || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATASSS) {
                switch (((Pet) this.player).typePet) {
                    case ConstPet.MABU:
                        this.dame += calPercent(this.dame, 5);
                        break;
                    case ConstPet.BILL:
                        this.dame += calPercent(this.dame, 8);
                        break;
                    case ConstPet.VIDEL:
                        this.dame += calPercent(this.dame, percent);
                        break;
                    case ConstPet.GOKU_BLUE:
                        this.dame += calPercent(this.dame, 100);
                        break;
                    case ConstPet.VEGETA_BLUE:
                        this.dame += calPercent(this.dame, 100);
                        break;
                    case ConstPet.GOHAN_BLUE:
                        this.dame += calPercent(this.dame, 100);
                        break;
                    case ConstPet.GOKU_GODD:
                        this.dame += calPercent(this.dame, 1000);
                        break;
                    case ConstPet.BAT_GIOI:
                        this.dame += calPercent(this.dame, 1000);
                        break;
                    case ConstPet.HANG_NGA:
                        this.dame += calPercent(this.dame, 2000);
                        break;
                    case ConstPet.VEGITO:
                        this.dame += calPercent(this.dame, 4000);
                        break;
                    case ConstPet.VEGITO_SSJ:
                        this.dame += calPercent(this.dame, 5000);
                        break;
                    case ConstPet.GOKU_ULTRA:
                        this.dame += calPercent(this.dame, 3000);
                        break;
                    case ConstPet.BROLY:
                        this.dame += calPercent(this.dame, 200);
                        break;

                }
            }
        }

        switch (player.vip) {
            case 1:
                this.dame += calPercent(this.dame, 10);
                break;
            case 2:
                this.dame += calPercent(this.dame, 100);
                break;
            case 3:
                this.dame += calPercent(this.dame, 500);
                break;
            case 4:
                this.dame += calPercent(this.dame, 1000);
                break;
            case 5:
                this.dame += calPercent(this.dame, 2000);
                break;
            case 6:
                this.dame += calPercent(this.dame, 5000);
                break;
            case 7:
                this.dame += calPercent(this.dame, 10000);
                break;
            case 8:
                this.dame += calPercent(this.dame, 20000);
                break;
            case 9:
                this.dame += calPercent(this.dame, 25000);
                break;     
             case 10:
                this.dame += calPercent(this.dame, 30000);
                break;    
                

        }

        switch (player.vip) {
            case 1:
                this.hpMax += calPercent(this.hpMax, 10);
                break;
            case 2:
                this.hpMax += calPercent(this.hpMax, 100);
                break;
            case 3:
                this.hpMax += calPercent(this.hpMax, 500);
                break;
            case 4:
                this.hpMax += calPercent(this.hpMax, 1000);
                break;
            case 5:
                this.hpMax += calPercent(this.hpMax, 2000);
                break;
            case 6:
                this.hpMax += calPercent(this.hpMax, 5000);
                break;
            case 7:
                this.hpMax += calPercent(this.hpMax, 10000);
                break;
            case 8:
                this.hpMax += calPercent(this.hpMax, 20000);
                break;
                
                case 9:
                this.hpMax += calPercent(this.hpMax, 25000);
                break;     
             case 10:
                this.hpMax += calPercent(this.hpMax, 30000);
                break;      

        }

        switch (player.vip) {
            case 1:
                this.mpMax += calPercent(this.mpMax, 10);
                break;
            case 2:
                this.mpMax += calPercent(this.mpMax, 100);
                break;
            case 3:
                this.mpMax += calPercent(this.mpMax, 500);
                break;
            case 4:
                this.mpMax += calPercent(this.mpMax, 1000);
                break;
            case 5:
                this.mpMax += calPercent(this.mpMax, 2000);
                break;
            case 6:
                this.mpMax += calPercent(this.mpMax, 5000);
                break;
            case 7:
                this.mpMax += calPercent(this.mpMax, 10000);
                break;
            case 8:
                this.mpMax += calPercent(this.mpMax, 20000);
                break;
           case 9:
                this.mpMax += calPercent(this.mpMax, 25000);
                break;     
             case 10:
                this.mpMax += calPercent(this.mpMax, 30000);
                break;      
                
                

        }

        
            switch (player.dakethon) {
            case 1:
                this.dame += calPercent(this.dame, 1000);
                break;
            case 2:
                this.dame += calPercent(this.dame, 2000);
                break;
            case 3:
                this.dame += calPercent(this.dame, 3000);
                break;
            case 4:
                this.dame += calPercent(this.dame, 4000);
                break;
            case 5:
                this.dame += calPercent(this.dame, 5000);
                break;
            case 6:
                this.dame += calPercent(this.dame, 6000);
                break;
            case 7:
                this.dame += calPercent(this.dame, 7000);
                break;
            case 8:
                this.dame += calPercent(this.dame, 8000);
                break;
             case 9:
                this.dame += calPercent(this.dame, 9000);
                break;    
              case 10:
                this.dame += calPercent(this.dame, 10000);
                break;   
              case 11:
                this.dame += calPercent(this.dame, 11000);
                break;
               case 12:
                this.dame += calPercent(this.dame, 12000);
                break;  
                case 13:
                this.dame += calPercent(this.dame, 13000);
                break; 
                case 14:
                this.dame += calPercent(this.dame, 14000);
                break; 
               case 15:
                this.dame += calPercent(this.dame, 15000);
                break;  
               case 16:
                this.dame += calPercent(this.dame, 16000);
                break;  
               case 17:
                this.dame += calPercent(this.dame, 17000);
                break;  
               case 18:
                this.dame += calPercent(this.dame, 18000);
                break;  
               case 19:
                this.dame += calPercent(this.dame, 19000);
                break;  
               case 20:
                this.dame += calPercent(this.dame, 20000);
                break;  

        }
            switch (player.dakethon) {
            case 1:
                this.hpMax += calPercent(this.hpMax, 1000);
                break;
            case 2:
                this.hpMax += calPercent(this.hpMax, 2000);
                break;
            case 3:
                this.hpMax += calPercent(this.hpMax, 3000);
                break;
            case 4:
                this.hpMax += calPercent(this.hpMax, 4000);
                break;
            case 5:
                this.hpMax += calPercent(this.hpMax, 5000);
                break;
            case 6:
                this.hpMax += calPercent(this.hpMax, 6000);
                break;
            case 7:
                this.hpMax += calPercent(this.hpMax, 7000);
                break;
            case 8:
                this.hpMax += calPercent(this.hpMax, 8000);
                break;
             case 9:
                this.hpMax += calPercent(this.hpMax, 9000);
                break;    
              case 10:
                this.hpMax += calPercent(this.hpMax, 10000);
                break;   
              case 11:
                this.hpMax += calPercent(this.hpMax, 11000);
                break;
               case 12:
                this.hpMax += calPercent(this.hpMax, 12000);
                break;  
                case 13:
                this.hpMax += calPercent(this.hpMax, 13000);
                break; 
                case 14:
                this.hpMax += calPercent(this.hpMax, 14000);
                break; 
               case 15:
                this.hpMax += calPercent(this.hpMax, 15000);
                break;  
               case 16:
                this.hpMax += calPercent(this.hpMax, 16000);
                break;  
               case 17:
                this.hpMax += calPercent(this.hpMax, 17000);
                break;  
               case 18:
                this.hpMax += calPercent(this.hpMax, 18000);
                break;  
               case 19:
                this.hpMax += calPercent(this.hpMax, 19000);
                break;  
               case 20:
                this.hpMax += calPercent(this.hpMax, 20000);
                break;  

        }  
            
            
            
            
            
            
            
            
            
            
            
            
            
            
         switch (player.duockethon) {
            case 1:
                this.dame += calPercent(this.dame, 100);
                break;
            case 2:
                this.dame += calPercent(this.dame, 200);
                break;
            case 3:
                this.dame += calPercent(this.dame, 300);
                break;
            case 4:
                this.dame += calPercent(this.dame, 400);
                break;
            case 5:
                this.dame += calPercent(this.dame, 500);
                break;
            case 6:
                this.dame += calPercent(this.dame, 600);
                break;
            case 7:
                this.dame += calPercent(this.dame, 700);
                break;
            case 8:
                this.dame += calPercent(this.dame, 800);
                break;
             case 9:
                this.dame += calPercent(this.dame, 900);
                break;    
              case 10:
                this.dame += calPercent(this.dame, 1000);
                break;   
              case 11:
                this.dame += calPercent(this.dame, 1100);
                break;
               case 12:
                this.dame += calPercent(this.dame, 1200);
                break;  
                case 13:
                this.dame += calPercent(this.dame, 1300);
                break; 
                case 14:
                this.dame += calPercent(this.dame, 1400);
                break; 
               case 15:
                this.dame += calPercent(this.dame, 1500);
                break;  
               case 16:
                this.dame += calPercent(this.dame, 1600);
                break;  
               case 17:
                this.dame += calPercent(this.dame, 1700);
                break;  
               case 18:
                this.dame += calPercent(this.dame, 1800);
                break;  
               case 19:
                this.dame += calPercent(this.dame, 1900);
                break;  
               case 20:
                this.dame += calPercent(this.dame, 2000);
                break;  
                
                
                
                
                
                
                
                

        }
           switch (player.duockethon) {
            case 1:
                this.hpMax += calPercent(this.hpMax, 100);
                break;
            case 2:
                this.hpMax += calPercent(this.hpMax, 200);
                break;
            case 3:
                this.hpMax += calPercent(this.hpMax, 300);
                break;
            case 4:
                this.hpMax += calPercent(this.hpMax, 400);
                break;
            case 5:
                this.hpMax += calPercent(this.hpMax, 500);
                break;
            case 6:
                this.hpMax += calPercent(this.hpMax, 600);
                break;
            case 7:
                this.hpMax += calPercent(this.hpMax, 700);
                break;
            case 8:
                this.hpMax += calPercent(this.hpMax, 800);
                break;
             case 9:
                this.hpMax += calPercent(this.hpMax, 900);
                break;    
              case 10:
                this.hpMax += calPercent(this.hpMax, 1000);
                break;   
              case 11:
                this.hpMax += calPercent(this.hpMax, 1100);
                break;
               case 12:
                this.hpMax += calPercent(this.hpMax, 1200);
                break;  
                case 13:
                this.hpMax += calPercent(this.hpMax, 1300);
                break; 
                case 14:
                this.hpMax += calPercent(this.hpMax, 1400);
                break; 
               case 15:
                this.hpMax += calPercent(this.hpMax, 1500);
                break;  
               case 16:
                this.hpMax += calPercent(this.hpMax, 1600);
                break;  
               case 17:
                this.hpMax += calPercent(this.hpMax, 1700);
                break;  
               case 18:
                this.hpMax += calPercent(this.hpMax, 1800);
                break;  
               case 19:
                this.hpMax += calPercent(this.hpMax, 1900);
                break;  
               case 20:
                this.hpMax += calPercent(this.hpMax, 2000);
                break;  
                
                
                
                
                
                
                
                

        }
        
        
        
        if (this.player.effectSkill.anTroi) {
            this.hpMax += calPercent(this.hpMax, 1000);
            Service.getInstance().sendThongBao(player, "Bạn Bị Trói\nTrong 30s\nTăng 1000% hp... ");
        }
        if (this.player.effectSkill.useTroi) {
            this.hpMax += calPercent(this.hpMax, 1000);
            Service.getInstance().sendThongBao(player, "Bạn Trói\nTrong 30s\nTăng 1000% hp.Và Giap ");
        }
        if (this.player.effectSkill.isThoiMien) {
            // Kiểm tra xem người chơi có đang chiến đấu với boss không
            if (!player.isBoss) { // Nếu không phải boss
                this.hpMax -= calPercent(this.hpMax, 98);
                Service.getInstance().sendThongBao(player, "Bạn Bị Thôi Miên\nTrong 30s\ngiảm hp ki 90% ");
            } else {
                Service.getInstance().sendThongBao(player, "Bạn đang chiến đấu với boss, không bị ảnh hưởng bởi thôi miên.");
            }
        }

        if (this.player.effectSkill.anTroi) {
            this.hp += calPercent(this.hp, 1000);
            //  Service.getInstance().sendThongBao(player, "Bạn Bị Trói\nTrong 30s\nTăng 10.000% hp... ");
        }

        if (this.player.effectSkill.isTranformation || this.player.effectSkill.isEvolution) {
            if (!this.player.isPet || (this.player.isPet && ((Pet) this.player).status != Pet.FUSION)) {
                int percent = SkillUtil.getPercentDameMonkey((byte) player.isbienhinh);
                if (player.effectSkill.levelTranformation == 1) {
                    this.dame += calPercent(this.dame, 50);
                } else if (player.effectSkill.levelTranformation == 2) {
                    this.dame += calPercent(this.dame, 100);
                } else if (player.effectSkill.levelTranformation == 3) {
                    this.dame += calPercent(this.dame, 150);
                } else if (player.effectSkill.levelTranformation == 4) {
                    this.dame += calPercent(this.dame, 200);
                } else if (player.effectSkill.levelTranformation == 5) {
                    this.dame += calPercent(this.dame, 250);
                } else if (player.effectSkill.levelTranformation == 6) {
                    this.dame += calPercent(this.dame, 300);
                }
            }
        }

        //thức ăn
        if (!this.player.isPet && this.player.itemTime.isEatMeal
                || this.player.isPet && ((Pet) this.player).master.itemTime.isEatMeal) {
            this.dame += calPercent(this.dame, 20);
        }
        //hợp thể
        if (this.player.fusion.typeFusion != 0) {
            this.dame += this.player.pet.nPoint.dame;
        }
        if (this.player.effectSkill.isTranformation || this.player.effectSkill.isEvolution) {
            if (!this.player.isPet || (this.player.isPet && ((Pet) this.player).status != Pet.FUSION)) {
                int percent = SkillUtil.getPercentDameMonkey((byte) player.isbienhinh);
                this.dame += ((long) this.dame * percent * 2 / 100);
            }
        }

        if (this.player.itemTime != null && this.player.itemTime.isBanhTrungThuThapCam) {
            this.dame += calPercent(this.dame, 180);
        }

        //cuồng nộ
        if (this.player.itemTime != null && this.player.itemTime.isUseCuongNo) {
            this.dame *= 2;
        }
        //cuồng nộ 2
        if (this.player.itemTime != null && this.player.itemTime.isUseCuongNo2) {
            this.dame += calPercent(dame, 120);
        }
        if (this.player.itemTime != null && this.player.itemTime.isBanhTrungThu1Trung) {
            this.dame += calPercent(this.dame, 10);
        }
        if (this.player.itemTime != null && this.player.itemTime.isBanhTrungThu2Trung) {
            this.dame += calPercent(this.dame, 20);
        }
        //phù mabu 14h
        if (this.player.zone != null && MapService.gI().isMapMabuWar14H(this.player.zone.map.mapId)) {
            this.dame += 10000;
        }
        //giảm dame
        this.dame -= calPercent(this.dame, tlSubSD);
        //map cold
        if (this.player.zone != null && MapService.gI().isMapCold(this.player.zone.map)
                && !this.isKhongLanh) {
            this.dame /= 2;
        }
        //ngọc rồng đen 1 sao
        if (this.player.rewardBlackBall.timeOutOfDateReward[0] > System.currentTimeMillis()) {
            this.dame += calPercent(this.dame, RewardBlackBall.R1S);
        }
        if (!player.isBoss) {
            Attribute at = ServerManager.gI().getAttributeManager().find(ConstAttribute.SUC_DANH);
            if (at != null && !at.isExpired()) {
                this.dame += calPercent(dame, at.getValue());
            }
        }
        if (this.player.itemTime != null) {
            if (this.player.itemTime.isUseBanhChung) {
                dame += calPercent(dame, 200);
            }
        }
        if (player.getBuff() == Buff.BUFF_ATK) {
            dame += calPercent(dame, 20);
        }
    }

    private void setDef() {
        this.def = this.defg * 4;
        this.def += this.defAdd;
        if (this.player.isPl() && this.player.Tamkjlltutien[2] >= 1) {
            this.def += this.def * this.player.TamkjllHpKiGiaptutien(Util.maxInt(this.player.Tamkjlltutien[1]))
                    / 100d;
        }
        //đồ
        for (Integer tl : this.tlDef) {
            this.tlGiamst += tl;
        }
        if (tlGiamst > 60) {
            tlGiamst = 60;
        }
        //ngọc rồng đen 5 sao
        if (this.player.rewardBlackBall.timeOutOfDateReward[4] > System.currentTimeMillis()) {
            this.def += calPercent(this.def, RewardBlackBall.R5S);
        }
        if (player.setClothes.full_set_vegeta == 5) {
            this.def += calPercent(this.def, 500);
            //  Service.getInstance().sendThongBao(player, "Đã tăng 100% sức đánh");
        }

        if (this.player.effectSkill.anTroi) {
            this.def += calPercent(this.def, 10000);
            //  Service.getInstance().sendThongBao(player, "Bạn Bị Trói\nTrong 30s\nTăng 10.000% hp... ");
        }
        if (this.player.isPl() && !this.player.isBot) {
            if (this.player.TamkjllDauLaDaiLuc[9] == 1) {
                this.def += this.def * player.TamkjllDauLaDaiLuc[10] / 100d;
            }
            if (this.player.effectSkill.isMonkey && this.player.CapTamkjll >= 100) {
                this.hpMax += this.hpMax * (this.player.CapTamkjll / 10d) / 100d;
            }
            this.def += this.def * (this.player.TamkjllCS * 10d) / 100d;
            // this.def += 5 * this.player.playerTask.taskMain.id;
        }
        if (this.player.Tamkjll_Ma_Hoa == 1) {
            this.def += this.def * this.player.TamkjllGiapTuMa() / 100d;
        }
        if (this.player.isPet && ((Pet) this.player).master.CapTamkjll >= 500) {
            this.def += this.def * (((Pet) this.player).master.CapTamkjll / 15d) / 100d;
        }
        if (this.player.effectSkin.isInosuke) {
            this.def += calPercent(this.def, 50);
        }

        if (this.player.effectSkin.isInoHashi) {
            this.def += calPercent(this.def, 60);
        }
    }

    private void setCrit() {
        this.crit = this.critg;
        this.crit += this.critAdd;
        //ngọc rồng đen 4 sao
        if (this.player.rewardBlackBall.timeOutOfDateReward[3] > System.currentTimeMillis()) {
            this.crit += RewardBlackBall.R4S;
        }
        //biến khỉ
        if (this.player.effectSkill.isMonkey) {
            this.crit = 100;
        }
        if (player.getBuff() == Buff.BUFF_CRIT) {
            crit += 10;
        }
        if (player.setClothes.full_set_fide == 5) {
            this.crit = 50;
            //   Service.getInstance().sendThongBao(player, "FULL SÉT FIDE tăng 100% CM");
        }

        if (this.player.itemTime != null
                && this.player.itemTime.isthanhkiem) {
            this.crit = 10;

            //  Service.getInstance().sendThongBao(player, "Đã Tăng 100% ST Chí Mạng +100% Chí Mạng Trong 10p");
        }

        if (this.player.itemTime != null && this.player.itemTime.isBanhTrungThuDacBiet) {
            this.crit += calPercent(this.crit, 55);
        }
    }

    private void setCritDame() {
        if (this.player.effectSkin.isTanjiro) {
            this.tlDameCrit.add(30);
        }
        if (this.player.itemTime != null) {
            if (this.player.itemTime.isUseBanhChung) {
                this.tlDameCrit.add(15);
            }
            if (this.player.itemTime != null
                    && this.player.itemTime.isthanhkiem) {
                this.tlDameCrit.add(20);
                // Service.getInstance().sendThongBao(player, "Đã Tăng 100% ST Chí Mạng +100% Chí Mạng Trong 10p");
            }
            if (player.setClothes.full_set_picolo == 5) {
                this.tlDameCrit.add(100);
                //  Service.getInstance().sendThongBao(player, "Đã tăng 100% sức đánh");
            }

            if (player.setClothes.tinhluyen16 == 5) {
                this.tlDameCrit.add(100);
            }

        }
    }

    private void setSpeed() {
        for (Integer tl : this.tlSpeed) {
            this.speed += calPercent(this.speed, tl);
        }
        if (this.player.effectSkin.isSlow) {
            this.speed = 1;
        }
    }

    private void resetPoint() {
        this.hpAdd = 0;
        this.mpAdd = 0;
        this.dameAdd = 0;
        this.defAdd = 0;
        this.critAdd = 0;
        this.tlHp.clear();
        this.tlMp.clear();
        this.tlDef.clear();
        this.tlDame.clear();
        this.tlDameAttMob.clear();
        this.tlDameCrit.clear();
        this.stlazer.clear();
        this.tlHpHoiBanThanVaDongDoi = 0;
        this.tlMpHoiBanThanVaDongDoi = 0;
        this.hpHoi = 0;
        this.mpHoi = 0;
        this.mpHoiCute = 0;
        this.tlHpHoi = 0;
        this.tlMpHoi = 0;
        this.tlHutHp = 0;
        this.tlHutMp = 0;
        this.tlHutHpMob = 0;
        this.tlHutHpMpXQ = 0;
        this.tlPST = 0;
        this.tlTNSM.clear();
        this.tlDameAttMob.clear();
        this.tlDameCrit.clear();
        this.stlazer.clear();
        this.tlGold = 0;
        this.tlNeDon = 0;
        this.tlSDDep.clear();
        this.stlazer.clear();
        this.tlSubSD = 0;
        this.tlHpGiamODo = 0;
        this.tangstbom = 0;
        this.stqckk.clear();
        this.teleport = false;
        this.tlSpeed.clear();
        this.speed = 5;
        this.mstChuong = 0;
        this.tlGiamst = 0;
        this.tlTNSMPet = 0;

        this.wearingVoHinh = false;
        this.isKhongLanh = false;
        this.wearingDrabula = false;
        this.wearingNezuko = false;
        this.wearingZenitsu = false;
        this.wearingInosuke = false;
        this.wearingInoHashi = false;
        this.wearingTanjiro = false;
        this.wearingMabu = false;
        this.wearingBuiBui = false;
        this.xDameChuong = false;
        this.wearingYacon = false;
        this.khangTDHS = false;
    }

    public void addHp(int hp) {
        this.hp += hp;
        if (this.hp > this.hpMax) {
            this.hp = this.hpMax;
        }
    }

    public void addHp2(double hp) {
        this.hp += hp;
        if (this.hp > this.hpMax) {
            this.hp = this.hpMax;
        }
    }

    public void addMp2(double mp) {
        this.mp += mp;
        if (this.mp > this.mpMax) {
            this.mp = this.mpMax;
        }
    }

    public void addMp(int mp) {
        this.mp += mp;
        if (this.mp > this.mpMax) {
            this.mp = this.mpMax;
        }
    }

    public void setHp(double hp) {
        if (hp > this.hpMax) {
            this.hp = this.hpMax;
        } else {
            this.hp = hp;
        }
    }

    public void setMp(double mp) {
        if (mp > this.mpMax) {
            this.mp = this.mpMax;
        } else {
            this.mp = mp;
        }
    }

    private void setIsCrit() {
        if (intrinsic != null && intrinsic.id == 25
                && this.getCurrPercentHP() <= intrinsic.param1) {
            isCrit = true;
        } else if (isCrit100) {
            isCrit100 = false;
            isCrit = true;
        } else {
            isCrit = Util.isTrue(this.crit, ConstRatio.PER100);
        }
    }

    public double getDameAttack(boolean isAttackMob) {
        setIsCrit();
        double dameAttack = this.dame;
        intrinsic = this.player.playerIntrinsic.intrinsic;
        percentDameIntrinsic = 0;
        int percentDameSkill = 0;
        int percentXDame = 0;
        Skill skillSelect = player.playerSkill.skillSelect;

        switch (skillSelect.template.id) {
            case Skill.DRAGON:
                if (intrinsic.id == 1) {
                    percentDameIntrinsic = intrinsic.param1;
                }
                percentDameSkill = skillSelect.damage;
                break;
            case Skill.KAMEJOKO:
                if (intrinsic.id == 2) {
                    percentDameIntrinsic = intrinsic.param1;
                }
                percentDameSkill = skillSelect.damage;
                if (this.player.setClothes.songoku == 5) {
                    dameAttack *= 2;
                }
                percentDameSkill = skillSelect.damage;
                if (this.player.setClothes.songoku1000pt == 1) {
                    dameAttack += dameAttack * 100 / 100;
                }

            case Skill.GALICK:
                if (intrinsic.id == 16) {
                    percentDameIntrinsic = intrinsic.param1;
                }
                percentDameSkill = skillSelect.damage;
                if (this.player.setClothes.kakarot == 5) {
                    dameAttack += dameAttack * 1000 / 100;
                }
            case Skill.ANTOMIC:
                if (intrinsic.id == 17) {
                    percentDameIntrinsic = intrinsic.param1;
                }
                percentDameSkill = skillSelect.damage;
                if (this.player.setClothes.antomic2000pt == 1) {
                    dameAttack += dameAttack * 200 / 100;
                }

                percentDameSkill = skillSelect.damage;
                break;
            case Skill.DEMON:
                if (intrinsic.id == 8) {
                    percentDameIntrinsic = intrinsic.param1;
                }
                percentDameSkill = skillSelect.damage;
                break;
            case Skill.MASENKO:
                if (intrinsic.id == 9) {
                    percentDameIntrinsic = intrinsic.param1;
                }
                percentDameSkill = skillSelect.damage;
                if (this.player.setClothes.masenco3000pt == 1) {
                    dameAttack += dameAttack * 300 / 100;
                }

                percentDameSkill = skillSelect.damage;
                break;
            case Skill.KAIOKEN:
                percentDameSkill = skillSelect.damage;
                if (this.player.isPl() && this.player.CapTamkjll >= 30) {
                    percentXDame += this.player.CapTamkjll;
                }
                if (this.player.setClothes.thienXinHang == 5) {
                    percentXDame *= 2;
                }

            case Skill.LIEN_HOAN:
                if (intrinsic.id == 13) {
                    percentDameIntrinsic = intrinsic.param1;
                }
                percentDameSkill = skillSelect.damage;
                if (this.player.setClothes.ocTieu == 5) {
                    dameAttack *= 2;
                }
                if (this.player.isPl() && this.player.CapTamkjll >= 30) {
                    percentXDame += this.player.CapTamkjll * 2d;
                }
                break;
            case Skill.DICH_CHUYEN_TUC_THOI:
                dameAttack *= 2;
                dameAttack = Util.nextdouble(dameAttack - calPercent(dameAttack, 5),
                        dameAttack + calPercent(dameAttack, 5));
                if (this.player.isPl() && this.player.CapTamkjll >= 30) {
                    percentXDame += this.player.CapTamkjll * 2d; // Chuyển sang double
                }
                return dameAttack; // Trả về kiểu double

            case Skill.MAKANKOSAPPO:
                percentDameSkill = skillSelect.damage;
                double dameSkill = calPercent(this.mpMax, percentDameSkill);
                for (Integer tl : this.stlazer) {
                    dameSkill += (int) calPercent(this.mpMax, tl);
                }
                return dameSkill;
            case Skill.QUA_CAU_KENH_KHI:
                double totalHP = 0;
                if (player.zone != null) {
                    totalHP = player.zone.getTotalHP();
                }
                double damage = ((totalHP / 10) + (this.dame * 100));
                if (this.player.setClothes.kirin == 5) {
                    damage *= 2;
                }
                return damage;
        }
        if (intrinsic.id == 18 && this.player.effectSkill.isMonkey) {
            percentDameIntrinsic = intrinsic.param1;
        }
        if (percentDameSkill != 0) {
            dameAttack = calPercent(dameAttack, percentDameSkill);
        }
        dameAttack += calPercent(dameAttack, percentDameIntrinsic);
        dameAttack += calPercent(dameAttack, dameAfter);

        if (isAttackMob) {
            for (Integer tl : this.tlDameAttMob) {
                dameAttack += calPercent(dameAttack, tl);
            }
            for (Integer tl : this.stqckk) {
                dameAttack += calPercent(dameAttack, tl);
            }
        }
        dameAfter = 0;
        if (this.player.isPet && ((Pet) this.player).master.charms.tdDeTu > System.currentTimeMillis()) {
            dameAttack *= 2;
        }
        if (isCrit) {
            dameAttack *= 2;
            for (Integer tl : this.tlDameCrit) {
                dameAttack += calPercent(dameAttack, tl);
            }
            for (Integer tl : this.stqckk) {
                dameAttack += calPercent(dameAttack, tl);
            }
            if (this.player.isPl() && this.player.CapTamkjll >= 70) {
                dameAttack += dameAttack * this.player.CapTamkjll / 100d;
            }
            if (this.player.isPl() && player.playerSkill.skillSelect.skillId == Skill.LIEN_HOAN
                    && this.player.CapTamkjll >= 100) {
                dameAttack += dameAttack * this.player.CapTamkjll / 100d;
            }
            if (this.player.isPet && ((Pet) this.player).master.CapTamkjll >= 500) {
                dameAttack += dameAttack * (((Pet) this.player).master.CapTamkjll / 20d) / 100d;
            }
            if (this.player.itemTime != null) {
                if (this.player.itemTime.isUseBanhTet && this.player.itemTime.isUseBanhChung) {
                    dameAttack += calPercent(dameAttack, 20);// tăng 20% st chí mạng
                }
            }
        }
        dameAttack += calPercent(dameAttack, percentXDame);
        dameAttack = Util.nextdouble((dameAttack - calPercent(dameAttack, 5)), (dameAttack + calPercent(dameAttack, 5)));
        if (player.isPl() && !player.isBot) {
            if (player.inventory.ishaveoption(player.inventory.itemsBody, 5, 159)) {
                if (Util.canDoWithTime(player.lastimeuseoption, 60000) && (player.playerSkill.skillSelect.template.id == Skill.KAMEJOKO || player.playerSkill.skillSelect.template.id == Skill.ANTOMIC || player.playerSkill.skillSelect.template.id == Skill.MASENKO)) {
                    if (isAttackMob) {
                        dameAttack *= player.inventory.getparam(player.inventory.itemsBody.get(5), 159);
                        player.lastimeuseoption = System.currentTimeMillis();
                        Service.getInstance().sendThongBao(player, "|1|Bạn vừa gây ra x" + player.inventory.getparam(player.inventory.itemsBody.get(5), 159) + " Sát thương Chưởng");
                    }
                }
            }
        }

        return dameAttack;
    }

    public void powerSub(long point) {
        if (MapService.gI().isMapTuongLai(this.player.zone.map.mapId)) {
            this.power -= 100000;
            this.tiemNang -= 100000;
            PlayerService.gI().sendTNSM(player, (byte) 0, -100000);
            PlayerService.gI().sendTNSM(player, (byte) 1, -100000);
        }
    }

    public int getDameAttackSkillNotFocus() {
        setIsCrit();
        double dameAttack = this.dame;
        intrinsic = this.player.playerIntrinsic.intrinsic;
        percentDameIntrinsic = 0;
        int percentDameSkill = 0;
        int percentXDame = 0;
        Skill skillSelect = player.playerSkill.skillSelect;
        switch (skillSelect.template.id) {

        }
        if (intrinsic.id == 18 && this.player.effectSkill.isMonkey) {
            percentDameIntrinsic = intrinsic.param1;
        }
        if (percentDameSkill != 0) {
            dameAttack = calPercent(dameAttack, percentDameSkill);
        }
        dameAttack += calPercent(dameAttack, percentDameIntrinsic);
        dameAttack += calPercent(dameAttack, dameAfter);
        dameAfter = 0;
        if (this.player.isPet && ((Pet) this.player).master.charms.tdDeTu > System.currentTimeMillis()) {
            dameAttack *= 2;
        }
        if (isCrit) {
            dameAttack *= 2;
            for (Integer tl : this.tlDameCrit) {
                dameAttack += calPercent(dameAttack, tl);
            }
        }
        dameAttack += calPercent(dameAttack, percentXDame);
        dameAttack = Util.nextInt((int) (dameAttack - calPercent(dameAttack, 5)), (int) (dameAttack + calPercent(dameAttack, 5)));
        return (int) dameAttack;
    }

    public double getCurrPercentHP() {
        if (this.hpMax == 0) {
            return 100; // Trả về 100% nếu hpMax bằng 0
        }
        return (double) this.hp * 100 / this.hpMax; // Chuyển đổi sang double để tính toán chính xác
    }

    public double getCurrPercentMP() {
        // Trả về kết quả dưới dạng double để tránh mất thông tin
        return (double) this.mp * 100 / this.mpMax;
    }

    public void setFullHpMp() {
        this.hp = this.hpMax;
        this.mp = this.mpMax;
    }

    public void subHP(double sub) {
        this.hp -= sub;
        if (this.hp < 0) {
            this.hp = 0;
        }
    }

    public void subMP(double sub) {
        this.mp -= sub;
        if (this.mp < 0) {
            this.mp = 0;
        }
    }

    public long calSucManhTiemNang(long tiemNang) {
        if (power < getPowerLimit()) {
            int tlexp = 0;
            for (Integer tl : this.tlTNSM) {
                tlexp += tl;
            }
            if (this.intrinsic != null && this.intrinsic.id == 24) {
                tlexp += this.intrinsic.param1;
            }
            tiemNang += (tiemNang * tlexp / 100);
            if (this.player.vip != 0) {
                switch (this.player.vip) {
                    case 1:
                    case 2:
                        tiemNang += ((long) tiemNang * 25 / 100);
                        break;
                    case 3:
                        tiemNang += ((long) tiemNang * 50 / 100);
                        break;
                    case 4:
                        tiemNang += ((long) tiemNang * 75 / 100);
                        break;

                }
            }
            if (buffExpSatellite) {
                tiemNang += calPercent(tiemNang, 20);
            }

            if (player.isPet) {
                Attribute at = ServerManager.gI().getAttributeManager().find(ConstAttribute.TNSM);
                if (at != null && !at.isExpired()) {
                    tiemNang += calPercent(tiemNang, at.getValue());
                }
            }
            if (this.player.isPet) {
                int tltnsm = ((Pet) this.player).master.nPoint.tlTNSMPet;
                if (tltnsm > 0) {
                    tiemNang += calPercent(tiemNang, tltnsm);
                }
            }
            long tn = tiemNang;
            if (this.player.charms.tdTriTue > System.currentTimeMillis()) {
                tlexp += 100;
            }
            if (this.player.charms.tdTriTue3 > System.currentTimeMillis()) {
                tlexp += 200;
            }
            if (this.player.charms.tdTriTue4 > System.currentTimeMillis()) {
                tlexp += 300;
            }
            if (this.power >= 200000000000L) {
                tiemNang -= (tiemNang * 50 / 100);
            }

            if (this.player.isPet) {
                if (((Pet) this.player).master.charms.tdDeTu > System.currentTimeMillis()) {
                    tiemNang += tn * 2;
                }
            }
            if (((this.player.isPl() && this.player.itemTime.isDuoiKhi)
                    || (this.player.isPet && ((Pet) this.player).master.itemTime.isDuoiKhi))) {
                tiemNang += tn * 2;
            }
            tiemNang *= Manager.RATE_EXP_SERVER;
            tiemNang = calSubTNSM(tiemNang);
            if (tiemNang <= 0) {
                tiemNang = 1;
            }
        } else {
            tiemNang = 10;
        }
        return tiemNang;
    }

    public long calSubTNSM(double tiemNang) {
        if (power >= 1) {
            tiemNang /= 50;
        } else if (power == 999999999999999999l) {
            tiemNang = 0;

        }
        return (long) tiemNang;
    }

    public double getTiLeHutHp() {
        return this.tlHutHp;
    }

    public double getTiLeHutMp() {
        return this.tlHutMp;
    }

    public double subDameInjureWithDeff(double dame) {
        double def = this.def;
        dame -= def;
        if (this.player.itemTime.isUseGiapXen) {
            dame /= 2;
        }
        if (this.player.itemTime.isUseGiapXen2) {
            dame -= calPercent(dame, 80);
        }
        if (dame < 0) {
            dame = 1;
        }
        return dame;
    }

    /*------------------------------------------------------------------------*/
    public boolean canOpenPower() {
        return this.power >= getPowerLimit();
    }

    public long getPowerLimit() {
        if (powerLimit != null) {
            return powerLimit.getPower();
        }
        return 0;
    }

    public long getPowerNextLimit() {
        PowerLimit powerLimit = PowerLimitManager.getInstance().get(limitPower + 1);
        if (powerLimit != null) {
            return powerLimit.getPower();
        }
        return 0;
    }

    //**************************************************************************
    //POWER - TIEM NANG
    public void powerUp(double power) {
        //    System.out.println(this.player.name+" powerup: "+power);
        this.power += power;
        TaskService.gI().checkDoneTaskPower(player, this.power);
    }

    public void tiemNangUp(double tiemNang) {
        //   System.out.println(this.player.name+" TNUP: "+tiemNang);
        this.tiemNang += tiemNang;

    }

    public void increasePoint(byte type, short point) {
        if (powerLimit == null) {
            return;
        }
        if (point <= 0) {
            return;
        }
        double tiemNangUse = 0;
        boolean check = false;
        switch (type) {
            case 0:
                double hpOld = hpg;
                switch (point) {
                    case 1:
                        tiemNangUse = hpOld + 1000;
                        break;
                    case 10:
                        tiemNangUse = 10 * (2 * (hpOld + 1000) + 180) / 2;
                        break;
                    case 100:
                        tiemNangUse = 100 * (2 * (hpOld + 1000) + 1980) / 2;
                        break;
                    case 1000:
                        tiemNangUse = 1000 * (2 * (hpOld + 1000) + 19800) / 2;
                        break;

                    default:
                        Service.getInstance().sendThongBaoOK(player, "Giá trị nhập vào không chính xác");
                        return;
                }
                if (tiemNang < tiemNangUse) {
                    Service.getInstance().sendThongBaoOK(player, "Bạn không có đủ tiềm năng để cộng điểm");
                    return;
                }
                double hpNew = hpOld + 20 * point;
                if (hpNew > powerLimit.getHp()) {
                    Service.getInstance().sendThongBaoOK(player, "Hãy mở giới hạn để cộng điểm này");
                    return;
                }
                this.hpg = hpNew;
                check = true;
                break;
            case 1:
                double mpOld = mpg;
                switch (point) {
                    case 1:
                        tiemNangUse = mpOld + 1000;
                        break;
                    case 10:
                        tiemNangUse = 10 * (2 * (mpOld + 1000) + 180) / 2;
                        break;
                    case 100:
                        tiemNangUse = 100 * (2 * (mpOld + 1000) + 1980) / 2;
                        break;
                    case 1000:
                        tiemNangUse = 1000 * (2 * (mpOld + 1000) + 19800) / 2;
                        break;

                    default:
                        Service.getInstance().sendThongBaoOK(player, "Giá trị nhập vào không chính xác");
                        return;
                }
                if (tiemNang < tiemNangUse) {
                    Service.getInstance().sendThongBaoOK(player, "Bạn không có đủ tiềm năng để cộng điểm");
                    return;
                }
                double mpNew = mpOld + 20 * point;
                if (mpNew > powerLimit.getMp()) {
                    Service.getInstance().sendThongBaoOK(player, "Hãy mở giới hạn để cộng điểm này");
                    return;
                }
                mpg = mpNew;
                check = true;
                break;
            case 2:
                double damageOld = this.dameg;
                switch (point) {
                    case 1:
                        tiemNangUse = damageOld * 100;
                        break;
                    case 10:
                        tiemNangUse = 10 * (2 * damageOld + 9) / 2 * 100;
                        break;
                    case 100:
                        tiemNangUse = 100 * (2 * damageOld + 99) / 2 * 100;
                        break;
                    case 1000:
                        tiemNangUse = 1000 * (2 * damageOld + 999) / 2 * 100;
                        break;

                    default:
                        Service.getInstance().sendThongBaoOK(player, "Giá trị nhập vào không chính xác");
                        return;
                }
                if (tiemNang < tiemNangUse) {
                    Service.getInstance().sendThongBaoOK(player, "Bạn không có đủ tiềm năng để cộng điểm");
                    return;
                }
                double damageNew = damageOld + 1 * point;
                if (damageNew > powerLimit.getDamage()) {
                    Service.getInstance().sendThongBaoOK(player, "Hãy mở giới hạn để cộng điểm này");
                    return;
                }
                dameg = damageNew;
                check = true;
                break;
            case 3:
                double giapgoc = this.defg;
                switch (point) {
                    case 1:
                        tiemNangUse = giapgoc * 100;
                        break;
                    case 10:
                        tiemNangUse = 10 * (2 * giapgoc + 9) / 2 * 100;
                        break;
                    case 100:
                        tiemNangUse = 100 * (2 * giapgoc + 99) / 2 * 100;
                        break;
                    case 1000:
                        tiemNangUse = 1000 * (2 * giapgoc + 999) / 2 * 100;
                        break;

                    default:
                        Service.getInstance().sendThongBaoOK(player, "Giá trị nhập vào không chính xác");
                        return;
                }
                if (tiemNang < tiemNangUse) {
                    Service.getInstance().sendThongBaoOK(player, "Bạn không có đủ tiềm năng để cộng điểm");
                    return;
                }
                double giapgocc = giapgoc + 1 * point;
                if (giapgocc > powerLimit.getDefense()) {
                    Service.getInstance().sendThongBaoOK(player, "Hãy mở giới hạn để cộng điểm này");
                    return;
                }
                defg = giapgocc;
                check = true;
                break;

            case 4:
                int critOld = critg;
                tiemNangUse = 50000000;
                for (byte i = 0; i < critOld; i++) {
                    tiemNangUse *= 5;
                }
                if (tiemNang < tiemNangUse) {
                    Service.getInstance().sendThongBaoOK(player, "Bạn không có đủ tiềm năng để cộng điểm");
                    return;
                }
                if (critOld >= powerLimit.getCritical()) {
                    Service.getInstance().sendThongBaoOK(player, "Hãy mở giới hạn để cộng điểm này");
                    return;
                }
                critg += 1;
                check = true;
                break;
        }
        this.tiemNang -= tiemNangUse;
//        TaskService.gI().checkDoneTaskUseTiemNang(player);
        if (check) {
            Service.getInstance().point(player);
        }
    }

    //--------------------------------------------------------------------------
    private long lastTimeHoiPhuc;
    private long lastTimeHoiPhucvip;
    private long lastTimeHoiStamina;

    public void update() {
        if (player != null && player.effectSkill != null) {
            if (player.effectSkill.isCharging && player.effectSkill.countCharging < 10) {
                double tiLeHoiPhuc = SkillUtil.getPercentCharge(player.playerSkill.skillSelect.point);
                if (player.effectSkill.isCharging && !player.isDie() && !player.effectSkill.isHaveEffectSkill()
                        && (hp < hpMax || mp < mpMax)) {
                    PlayerService.gI().hoiPhuc(player, calPercent(hpMax, tiLeHoiPhuc), calPercent(mpMax, tiLeHoiPhuc));
                    if (player.effectSkill.countCharging % 3 == 0) {
                        Service.getInstance().chat(player, "Tao Đang Hồi Phục HP,KI " + getCurrPercentHP() + "%");
                    }
                } else {
                    EffectSkillService.gI().stopCharge(player);
                }
                if (++player.effectSkill.countCharging >= 10) {
                    EffectSkillService.gI().stopCharge(player);
                }
            }
            if (Util.canDoWithTime(lastTimeHoiPhuc, 30000)) {
                PlayerService.gI().hoiPhuc(this.player, hpHoi, mpHoi);
                this.lastTimeHoiPhuc = System.currentTimeMillis();
            }
            if (Util.canDoWithTime(lastTimeHoiPhucvip, 3000)) {
                PlayerService.gI().hoiPhucvip(this.player, hpHoi1, mpHoi1);
                this.lastTimeHoiPhucvip = System.currentTimeMillis();
            }

            if (Util.canDoWithTime(lastTimeHoiStamina, 60000) && this.stamina < this.maxStamina) {
                this.stamina++;
                this.lastTimeHoiStamina = System.currentTimeMillis();
                if (!this.player.isBoss && !this.player.isPet) {
                    PlayerService.gI().sendCurrentStamina(this.player);
                }
            }
        }
        //hồi phục 30s
        //hồi phục thể lực
    }

    private void setBasePoint() {
        setHpMax();
        setMpMax();
        setDame();
        setDef();
        setCrit();
        setHpHoi();
        setMpHoi();
        setNeDon();
        setCritDame();
        setSpeed();
        setAttributeOverLimit();
    }

    public void setAttributeOverLimit() {
        int max = Integer.MAX_VALUE;
        int min = -100000000;
        if (this.hpMax < 0) {
            if (this.hpMax < min) {
                this.hpMax = max;
            } else {
                this.hpMax = 1;
            }
        }
        if (this.mpMax < 0) {
            if (this.mpMax < min) {
                this.mpMax = max;
            } else {
                this.mpMax = 1;
            }
        }
        if (this.dame < 0) {
            if (this.dame < min) {
                this.dame = max;
            } else {
                this.dame = 1;
            }
        }
        if (this.def < 0) {
            if (this.def < min) {
                this.def = max;
            } else {
                this.def = 1;
            }
        }
        if (this.crit < 0) {
            if (this.crit < min) {
                this.crit = max;
            } else {
                this.crit = 1;
            }
        }
        setHp();
        setMp();
    }

    public double calPercent(double param, double percent) {
        return param * percent / 100;
    }

    public double calPercent1(double param, double ignore) {
        return 50000;  // Trả về giá trị cố định là 100
    }

    public void dispose() {
        this.intrinsic = null;
        this.player = null;
        this.tlHp = null;
        this.tlMp = null;
        this.tlDef = null;
        this.tlDame = null;
        this.tlDameAttMob = null;
        this.tlSDDep = null;
        this.stlazer = null;
        this.stqckk = null;
        this.tlTNSM = null;
        this.tlDameCrit = null;
        this.tlSpeed = null;
    }
}
