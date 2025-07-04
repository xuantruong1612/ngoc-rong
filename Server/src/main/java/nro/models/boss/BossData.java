package nro.models.boss;

import java.util.HashMap;
import nro.consts.ConstPlayer;
import nro.models.skill.Skill;
import lombok.Builder;
import nro.utils.Util;

/**
 * @Config Truong
 */
public class BossData {
// Tính bằng giây
        public static final int _10_GIAY = 10;
        public static final int _30_GIAY = 30;
        public static final int _60_GIAY = 60;
        public static final int _5_PHUT = 300;
        public static final int _10_PHUT = 600;
        //public static final int _1_GIO = 360;
        // private static final double CSrandom = Util.nextInt(90, 100);

        public static final int HP_1_TY = 1_000_000_000;
        public static final long HP_5_TY = 5_000_000_000l;
        public static final long HP_10_TY = 10_000_000_000l;
        public static final long HP_20_TY = 20_000_000_000l;
        public static final long HP_50_TY = 50_000_000_000l;
        public static final long HP_70_TY = 70_000_000_000l;
        public static final long HP_100_TY = 100_000_000_000l;
        public static final long HP_1_NGHIN_TY = 1_000_000_000_000l;
        public static final long HP_10_NGHIN_TY = 10_000_000_000_000l;
        public static final long HP_20_NGHIN_TY = 20_000_000_000_000l;
        public static final long HP_1_TRIEU_TY = 1_000_000_000_000_000l;

        public static final int DMG_100k = 100_000;
        public static final int DMG_300k = 300_000;
        public static final int DMG_500k = 500_000;
        public static final int DMG_700k = 700_000;
        public static final int DMG_1TR = 1_000_000;
        public static final int DMG_3TR = 3_000_000;
        public static final int DMG_10TR = 10_000_000;

        public static final short DEF_0 = 0;
        public static final int DEF_100k = 100_000;
        public static final int DEF_300k = 300_000;
        public static final int DEF_500k = 500_000;
        public static final int DEF_700k = 700_000;
        public static final int DEF_1TR = 1_000_000;
        public static final int DEF_3TR = 3_000_000;
        public static final int DEF_10TR = 10_000_000;


        // --------------------------------------------------------------------------
        public String name;

        public byte gender;

        public byte typeDame;

        public byte typeHp;

        public double dame;

        public double[][] hp;

        public double def;

        public short[] outfit;

        public short[] mapJoin;

        public int[][] skillTemp;

        public int secondsRest;

        public boolean joinMapIdle;

        public int timeDelayLeaveMap = -1;

        public HashMap<Integer, BossData> nextLevel;

        @Builder
        public BossData(String name, byte gender, byte typeDame, byte typeHp, double dame, double defender,
                        double[][] hp,
                        short[] outfit, short[] mapJoin, int[][] skillTemp, int secondsRest) {
                this.name = name;
                this.gender = gender;
                this.typeDame = typeDame;
                this.typeHp = typeHp;
                this.dame = dame;
                this.def = defender;
                this.hp = hp;
                this.outfit = outfit;
                this.mapJoin = mapJoin;
                this.skillTemp = skillTemp;
                this.secondsRest = secondsRest;
        }

        public BossData(String name, byte gender, byte typeDame, byte typeHp, double dame, double[][] hp,
                        double defender,
                        short[] outfit, short[] mapJoin, int[][] skillTemp, int secondsRest) {
                this.name = name;
                this.gender = gender;
                this.typeDame = typeDame;
                this.typeHp = typeHp;
                this.dame = dame;
                this.hp = hp;
                this.def = defender;
                this.outfit = outfit;
                this.mapJoin = mapJoin;
                this.skillTemp = skillTemp;
                this.secondsRest = secondsRest;
        }

        public BossData(String name, byte gender, byte typeDame, byte typeHp, double dame, double defender,
                        double[][] hp,
                        short[] outfit, short[] mapJoin, int[][] skillTemp, int secondsRest, boolean joinMapIdle) {
                this.name = name;
                this.gender = gender;
                this.typeDame = typeDame;
                this.typeHp = typeHp;
                this.dame = dame;
                this.def = defender;
                this.hp = hp;
                this.outfit = outfit;
                this.mapJoin = mapJoin;
                this.skillTemp = skillTemp;
                this.secondsRest = secondsRest;
                this.joinMapIdle = joinMapIdle;
        }

        public BossData(String name, byte gender, byte typeDame, byte typeHp, double dame, double defender,
                        double[][] hp,
                        short[] outfit, short[] mapJoin, int[][] skillTemp, HashMap<Integer, BossData> nextLevel,
                        int secondsRest, boolean joinMapIdle) {
                this.name = name;
                this.gender = gender;
                this.typeDame = typeDame;
                this.typeHp = typeHp;
                this.dame = dame;
                this.def = defender;
                this.hp = hp;
                this.outfit = outfit;
                this.mapJoin = mapJoin;
                this.skillTemp = skillTemp;
                this.nextLevel = nextLevel;
                this.secondsRest = secondsRest;
                this.joinMapIdle = joinMapIdle;
        }

        public BossData(String name, byte gender, byte typeDame, byte typeHp, double dame, double defender,
                        double[][] hp,
                        short[] outfit, short[] mapJoin, int[][] skillTemp, int secondsRest, int timeDelayLeaveMap) {
                this.name = name;
                this.gender = gender;
                this.typeDame = typeDame;
                this.typeHp = typeHp;
                this.dame = dame;
                this.def = defender;
                this.hp = hp;
                this.outfit = outfit;
                this.mapJoin = mapJoin;
                this.skillTemp = skillTemp;
                this.secondsRest = secondsRest;
                this.timeDelayLeaveMap = timeDelayLeaveMap;
        }

        public BossData(String name, byte gender, byte typeDame, byte typeHp, double dame, double defender,
                        double[][] hp,
                        short[] outfit, short[] mapJoin, int[][] skillTemp, int secondsRest, boolean joinMapIdle,
                        int timeDelayLeaveMap) {
                this.name = name;
                this.gender = gender;
                this.typeDame = typeDame;
                this.typeHp = typeHp;
                this.dame = dame;
                this.def = defender;
                this.hp = hp;

                this.outfit = outfit;
                this.mapJoin = mapJoin;
                this.skillTemp = skillTemp;
                this.secondsRest = secondsRest;
                this.joinMapIdle = joinMapIdle;
                this.timeDelayLeaveMap = timeDelayLeaveMap;
        }

        public static final BossData BOSS_THE_GIOI = new BossData(
                        "Boss Thế Giới 20h00 [Sao Pha Lê 1 Tỉ%]", // name
                        ConstPlayer.XAYDA, // gender
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp
                        Long.MAX_VALUE, // dame
                        DMG_10TR, // dame
                        new double[][] { { HP_100_TY } }, // hp
                        new short[] { 1426, 1427, 1428 }, // outfit
                        new short[] { 3, 4, 5, 6, 27, 28, 29, 30, // traidat
                                        9, 11, 12, 13, 10, 34, 33, 32, 31, // namec
                                        16, 17, 18, 19, 20, 37, 38, 36, 35, // xayda
                                        24, 25, 26 }, // map join
                        new int[][] { // skill
                                        { Skill.DRAGON, 1, 1000 }, { Skill.DRAGON, 2, 2000 }, { Skill.DRAGON, 3, 3000 },
                                        { Skill.DRAGON, 7, 7000 },
                                        { Skill.DE_TRUNG, 1, 7_000 }, },
                        _5_PHUT);

        public static final BossData SUPERBROLY = new BossData(
                        "Super Broly", // name
                        ConstPlayer.XAYDA, // gender
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp
                        DMG_1TR, // dame
                        1,
                        new double[][] { { HP_10_TY } }, // hp
                        new short[] { 294, 295, 296 }, // outfit
                        new short[] { 5, 6, 27, 28, 29, 30, 13, 10, 31, 32, 33, 34, 20, 19, 35, 36, 37, 38 }, // map
                                                                                                              // join
                        new int[][] { // skill
                                        { Skill.DEMON, 3, 450 }, { Skill.DEMON, 6, 400 }, { Skill.DRAGON, 7, 650 },
                                        { Skill.DRAGON, 1, 500 }, { Skill.GALICK, 5, 480 },
                                        { Skill.KAMEJOKO, 7, 2000 }, { Skill.KAMEJOKO, 6, 1800 },
                                        { Skill.KAMEJOKO, 4, 1500 }, { Skill.KAMEJOKO, 2, 1000 },
                                        { Skill.ANTOMIC, 3, 1200 }, { Skill.ANTOMIC, 5, 1700 },
                                        { Skill.ANTOMIC, 7, 2000 },
                                        { Skill.MASENKO, 1, 800 }, { Skill.MASENKO, 5, 1300 },
                                        { Skill.MASENKO, 6, 1500 },
                                        { Skill.TAI_TAO_NANG_LUONG, 1, 15000 }, { Skill.TAI_TAO_NANG_LUONG, 3, 25000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 7, 50000 }
                        },
                        _5_PHUT);

        public static final BossData Broly = new BossData(
                        "Broly Săn Đệ", // name
                        ConstPlayer.XAYDA, // gender
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp
                        DMG_500k, // dame
                        1,
                        new double[][] { { HP_1_TY } }, // hp
                        new short[] { 1565, 1566, 1567 }, // outfit
                        new short[] { 5, 6, 27, 28, 29, 30, 13, 10, 31, 32, 33, 34, 20, 19, 35, 36, 37, 38 }, // map
                                                                                                              // join
                        new int[][] { // skill
                                        { Skill.DEMON, 3, 450 }, { Skill.DEMON, 6, 400 }, { Skill.DRAGON, 7, 650 },
                                        { Skill.DRAGON, 1, 500 }, { Skill.GALICK, 5, 480 },
                                        { Skill.KAMEJOKO, 7, 2000 }, { Skill.KAMEJOKO, 6, 1800 },
                                        { Skill.KAMEJOKO, 4, 1500 }, { Skill.KAMEJOKO, 2, 1000 },
                                        { Skill.ANTOMIC, 3, 1200 }, { Skill.ANTOMIC, 5, 1700 },
                                        { Skill.ANTOMIC, 7, 2000 },
                                        { Skill.MASENKO, 1, 800 }, { Skill.MASENKO, 5, 1300 }, { Skill.MASENKO, 6, 1500
                                        }
                        },
                        _5_PHUT);

        public static final BossData danangcap = new BossData(
                        "Thủ Lĩnh [Tài Nguyên]", // name
                        ConstPlayer.XAYDA, // gender
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp

                        DMG_500k, // dame
                        10,
                        new double[][] { { HP_1_TY } }, // hp
                        new short[] { 1640, 1641, 1642 }, // outfit
                        new short[] { 214 }, // map join
                        new int[][] { // skill
                                        { Skill.MASENKO, 7, 10 },
                                        { Skill.KAMEJOKO, 7, 10 },
                                        { Skill.ANTOMIC, 7, 10 },
                                        { Skill.TAI_TAO_NANG_LUONG, 7, 60000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 3, 20000 },
                                        { Skill.HUYT_SAO, 7, 20000 }, },
                        _5_PHUT);
        public static final BossData BOSS_THOIVANG = new BossData(
                        "Su [Thỏi Vàng]", // name
                        ConstPlayer.XAYDA, // gender
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp
                        DMG_500k, // dame
                        0, // dame
                        new double[][] { { HP_1_TY } }, // hp
                        new short[] { 618, 619, 620 }, // outfit
                        new short[] { 0, 1, 2, 3, 7, 8, 9, 14, 15, 16, 17, 6 }, // map join
                        new int[][] { // skill
                                        { Skill.DRAGON, 1, 100 }, { Skill.MASENKO, 7, 20 },
                                        { Skill.DRAGON, 3, 3000 },
                                        { Skill.DRAGON, 7, 7000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 7, 30000 }, },
                        _5_PHUT);

        public static final BossData Luffy = new BossData(
                        "Luffy", // name
                        ConstPlayer.XAYDA, // gender
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp
                        DMG_1TR, // dame
                        0, // dame
                        new double[][] { { HP_1_TY } }, // hp
                        new short[] { 582, 583, 584 }, // outfit
                        new short[] { 135, 136, 137, 138 }, // map join
                        new int[][] { // skill
                                        { Skill.DRAGON, 1, 100 }, { Skill.MASENKO, 7, 20 },
                                        { Skill.KAMEJOKO, 7, 300 },
                                        { Skill.ANTOMIC, 7, 700 },
                                        { Skill.TAI_TAO_NANG_LUONG, 7, 30000 }, },
                        _60_GIAY);

        public static final BossData Zoro = new BossData(
                        "Zoro", // name
                        ConstPlayer.XAYDA, // gender
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp
                        DMG_1TR, // dame
                        0, // dame
                        new double[][] { { HP_1_TY } }, // hp
                        new short[] { 585, 586, 587 }, // outfit
                        new short[] { 135, 136, 137, 138 }, // map join
                        new int[][] { // skill
                                        { Skill.DRAGON, 1, 100 }, { Skill.MASENKO, 7, 20 },
                                        { Skill.KAMEJOKO, 7, 300 },
                                        { Skill.ANTOMIC, 7, 700 },
                                        { Skill.TAI_TAO_NANG_LUONG, 7, 30000 }, },
                        _60_GIAY);

        public static final BossData Sanji = new BossData(
                        "Sanji", // name
                        ConstPlayer.XAYDA, // gender
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp
                        DMG_1TR, // dame
                        0, // dame
                        new double[][] { { HP_1_TY } }, // hp
                        new short[] { 588, 589, 590 }, // outfit
                        new short[] { 135, 136, 137, 138 }, // map join
                        new int[][] { // skill
                                        { Skill.DRAGON, 1, 100 }, { Skill.MASENKO, 7, 20 },
                                        { Skill.KAMEJOKO, 7, 300 },
                                        { Skill.ANTOMIC, 7, 700 },
                                        { Skill.TAI_TAO_NANG_LUONG, 7, 30000 }, },
                        _60_GIAY);

        public static final BossData Brook = new BossData(
                        "Brook", // name
                        ConstPlayer.XAYDA, // gender
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp
                        DMG_1TR, // dame
                        0, // dame
                        new double[][] { { HP_1_TY } }, // hp
                        new short[] { 591, 592, 593 }, // outfit
                        new short[] { 135, 136, 137, 138 }, // map join
                        new int[][] { // skill
                                        { Skill.DRAGON, 1, 100 }, { Skill.MASENKO, 7, 20 },
                                        { Skill.KAMEJOKO, 7, 300 },
                                        { Skill.ANTOMIC, 7, 700 },
                                        { Skill.TAI_TAO_NANG_LUONG, 7, 30000 }, },
                        _60_GIAY);

        public static final BossData Chopper = new BossData(
                        "Chopper", // name
                        ConstPlayer.XAYDA, // gender
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp
                        499000, // dame
                        0, // dame
                        new double[][] { { HP_1_TY } }, // hp
                        new short[] { 606, 607, 608 }, // outfit
                        new short[] { 135, 136, 137, 138 }, // map join
                        new int[][] { // skill
                                        { Skill.DRAGON, 1, 100 }, { Skill.MASENKO, 7, 20 },
                                        { Skill.KAMEJOKO, 7, 300 },
                                        { Skill.ANTOMIC, 7, 700 },
                                        { Skill.TAI_TAO_NANG_LUONG, 7, 30000 }, },
                        _60_GIAY);

        public static final BossData Nami = new BossData(
                        "Nami", // name
                        ConstPlayer.XAYDA, // gender
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp
                        599000, // dame
                        0, // dame
                        new double[][] { { HP_1_TY } }, // hp
                        new short[] { 600, 601, 602 }, // outfit
                        new short[] { 135, 136, 137, 138 }, // map join
                        new int[][] { // skill
                                        { Skill.DRAGON, 1, 100 }, { Skill.MASENKO, 7, 20 },
                                        { Skill.KAMEJOKO, 7, 300 },
                                        { Skill.ANTOMIC, 7, 700 },
                                        { Skill.TAI_TAO_NANG_LUONG, 7, 30000 }, },
                        _60_GIAY);
        public static final BossData Franky = new BossData(
                        "Franky", // name
                        ConstPlayer.XAYDA, // gender
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp
                        DMG_1TR, // dame
                        0, // dame
                        new double[][] { { HP_1_TY } }, // hp
                        new short[] { 594, 595, 596 }, // outfit
                        new short[] { 135, 136, 137, 138 }, // map join
                        new int[][] { // skill
                                        { Skill.DRAGON, 1, 100 }, { Skill.MASENKO, 7, 20 },
                                        { Skill.KAMEJOKO, 7, 300 },
                                        { Skill.ANTOMIC, 7, 700 },
                                        { Skill.TAI_TAO_NANG_LUONG, 7, 30000 }, },
                        _60_GIAY);

        public static final BossData Usopp = new BossData(
                        "Usopp", // name
                        ConstPlayer.XAYDA, // gender
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp
                        899000, // dame
                        0, // dame
                        new double[][] { { 99938900l } }, // hp
                        new short[] { 597, 598, 599 }, // outfit
                        new short[] { 135, 136, 137, 138 }, // map join
                        new int[][] { // skill
                                        { Skill.DRAGON, 1, 100 }, { Skill.MASENKO, 7, 20 },
                                        { Skill.KAMEJOKO, 7, 300 },
                                        { Skill.ANTOMIC, 7, 700 },
                                        { Skill.TAI_TAO_NANG_LUONG, 7, 30000 }, },
                        _60_GIAY);
        public static final BossData Robin = new BossData(
                        "Robin", // name
                        ConstPlayer.XAYDA, // gender
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp
                        999000, // dame
                        0, // dame
                        new double[][] { { 991130000l } }, // hp
                        new short[] { 603, 604, 605 }, // outfit
                        new short[] { 135, 136, 137, 138 }, // map join
                        new int[][] { // skill
                                        { Skill.DRAGON, 1, 100 }, { Skill.MASENKO, 7, 20 },
                                        { Skill.KAMEJOKO, 7, 300 },
                                        { Skill.ANTOMIC, 7, 700 },
                                        { Skill.TAI_TAO_NANG_LUONG, 7, 30000 }, },
                        _60_GIAY);

        public static final BossData BOSSVIP17 = new BossData(
                        "QUY LÃO [10k lưu ly]", // name
                        ConstPlayer.XAYDA, // gender
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp
                        2000000, // dame
                        0, // dame
                        new double[][] { { 70008200L } }, // hp
                        new short[] { 1434, 1435, 1436 }, // outfit
                        new short[] { 0, 1, 2, 3, 7, 8, 9, 14, 15, 16, 17, 6 }, // map join
                        new int[][] { // skill
                                        { Skill.DRAGON, 1, 100 }, { Skill.MASENKO, 7, 20 },
                                        { Skill.DRAGON, 3, 3000 },
                                        { Skill.DRAGON, 7, 7000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 7, 30000 }, },
                        _10_PHUT);

        public static final BossData BOSS_DAUGOD = new BossData(
                        "Khỉ Đột [Đậu Gold]", // name
                        ConstPlayer.XAYDA, // gender
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp
                        DMG_1TR, // dame
                        DEF_300k, // dame
                        new double[][] { { HP_10_TY } }, // hp
                        new short[] { 1589, 1590, 1591 }, // outfit
                        new short[] { 0, 1, 2, 3, 7, 8, 9, 14, 15, 16, 17, 6 }, // map join
                        new int[][] { // skill
                                        { Skill.DRAGON, 1, 100 }, { Skill.MASENKO, 7, 20 },
                                        { Skill.DRAGON, 3, 3000 },
                                        { Skill.DRAGON, 7, 7000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 7, 30000 }, },
                        _5_PHUT);

        public static final BossData BOSSVIP19 = new BossData(
                        "ZUKA [2M EXP Tu Tiên]", // name
                        ConstPlayer.XAYDA, // gender
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp
                        20000000, // dame
                        0, // dame
                        new double[][] { { 7002828200d } }, // hp
                        new short[] { 1607, 1608, 1609 }, // outfit
                        new short[] { 1, 2, 3, 8, 9, 15, 16, 17, 6 }, // map join
                        new int[][] { // skill
                                        { Skill.DRAGON, 1, 100 }, { Skill.MASENKO, 7, 20 },
                                        { Skill.DRAGON, 3, 3000 },
                                        { Skill.DRAGON, 7, 7000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 7, 30000 }, },
                        _5_PHUT);
        public static final BossData BOSSVIP20 = new BossData(
                        "athur [1000 Điểm vip]", // name
                        ConstPlayer.XAYDA, // gender
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp
                        2000000, // dame
                        0, // dame
                        new double[][] { { 9000142300d } }, // hp
                        new short[] { 1930, 1931, 1932 }, // outfit
                        new short[] { 1, 2, 3, 7, 8, 9, 15, 16, 17, 6 }, // map join
                        new int[][] { // skill
                                        { Skill.DRAGON, 1, 100 }, { Skill.MASENKO, 7, 20 },
                                        { Skill.DRAGON, 3, 3000 },
                                        { Skill.DRAGON, 7, 7000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 7, 30000 }, },
                        _5_PHUT);

        public static final BossData BOSSHONGNGOC = new BossData(
                        "Mai [100k hồng ngọc]", // name
                        ConstPlayer.XAYDA, // gender
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp
                        2000222, // dame
                        0, // dame
                        new double[][] { { 5123000000d } }, // hp
                        new short[] { 615, 616, 617 }, // outfit
                        new short[] { 1, 2, 3, 8, 9, 15, 16, 17, 6 }, // map join
                        new int[][] { // skill
                                        { Skill.DRAGON, 1, 100 }, { Skill.MASENKO, 7, 20 },
                                        { Skill.DRAGON, 3, 3000 },
                                        { Skill.DRAGON, 7, 7000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 7, 30000 }, },
                        _5_PHUT);

        public static final BossData BOSSSK1 = new BossData(
                        "Sự kiện Tết %1", // name
                        ConstPlayer.XAYDA, // gender
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp
                        50000000, // dame
                        0, // dame
                        new double[][] { { 180001300d } }, // hp
                        new short[] { 769, 674, 675 }, // outfit
                        new short[] { 1, 2, 3, 8, 9, 15, 16, 17, 6 }, // map join
                        new int[][] { // skill
                                        { Skill.DRAGON, 1, 100 }, { Skill.MASENKO, 7, 20 },
                                        { Skill.DRAGON, 3, 3000 },
                                        { Skill.DRAGON, 7, 7000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 5, 30000 }, },
                        _5_PHUT);

        public static final BossData BOSS_TRUNGQUY1 = new BossData(
                        "Sự Kiện Kết Hôn %1", // name
                        ConstPlayer.XAYDA, // gender
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp
                        50000000, // dame
                        0, // dame
                        new double[][] { { 2_000_001_300d } }, // hp
                        new short[] { 1553, 1554, 1555 }, // outfit
                        new short[] { 1, 2, 3, 8, 9, 15, 16, 17, 6 }, // map join
                        new int[][] { // skill
                                        { Skill.DRAGON, 1, 100 }, { Skill.KAMEJOKO, 7, 20 },
                                        { Skill.LIEN_HOAN, 3, 300 },
                                        { Skill.DRAGON, 7, 7000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 5, 30000 }, },
                        _5_PHUT);
        public static final BossData BOSS_TRUNGQUY2 = new BossData(
                        "Sự Kiện Kết Hôn %1", // name
                        ConstPlayer.XAYDA, // gender
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp
                        DMG_1TR, // dame
                        0, // dame
                        new double[][] { { HP_1_TY } }, // hp
                        new short[] { 1553, 1554, 1555 }, // outfit
                        new short[] { 1, 2, 3, 8, 9, 15, 16, 17, 6 }, // map join
                        new int[][] { // skill
                                        { Skill.DRAGON, 1, 100 }, { Skill.KAMEJOKO, 7, 20 },
                                        { Skill.LIEN_HOAN, 3, 300 },
                                        { Skill.DRAGON, 7, 7000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 5, 30000 }, },
                        _5_PHUT);

        public static final BossData BOSS_TRUNGQUY3 = new BossData(
                        "Sự Kiện Kết Hôn %1", // name
                        ConstPlayer.XAYDA, // gender
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp
                        DMG_1TR, // dame
                        0, // dame
                        new double[][] { { HP_1_TY } }, // hp
                        new short[] { 1553, 1554, 1555 }, // outfit
                        new short[] { 1, 2, 3, 8, 9, 15, 16, 17, 6 }, // map join
                        new int[][] { // skill
                                        { Skill.DRAGON, 1, 100 }, { Skill.KAMEJOKO, 7, 20 },
                                        { Skill.LIEN_HOAN, 3, 300 },
                                        { Skill.DRAGON, 7, 7000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 5, 30000 }, },
                        _60_GIAY);

        public static final BossData chanmenh1 = new BossData(
                        "Fu [CM1]", // name
                        ConstPlayer.XAYDA, // gender
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp
                        DMG_1TR, // dame
                        0, // dame
                        new double[][] { { HP_1_TY } }, // hp
                        new short[] { 1628, 1629, 1630 }, // outfit
                        new short[] { 29, 30, 31 }, // map join
                        new int[][] { // skill
                                        { Skill.LIEN_HOAN, 1, 10 }, { Skill.MASENKO, 7, 20 },
                                        { Skill.KAMEJOKO, 3, 3000 },
                                        { Skill.SOCOLA, 7, 90000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 5, 60000 }, },
                        _5_PHUT);
        public static final BossData chanmenh2 = new BossData(
                        "Siêu Picolo [CM2]", // name
                        ConstPlayer.XAYDA, // gender
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp
                        59000000L, // dame
                        0, // dame
                        new double[][] { { HP_1_TY } }, // hp
                        new short[] { 1601, 1602, 1603 }, // outfit
                        new short[] { 29, 30, 31 }, // map join
                        new int[][] { // skill
                                        { Skill.LIEN_HOAN, 1, 10 }, { Skill.MASENKO, 7, 20 },
                                        { Skill.KAMEJOKO, 3, 3000 },
                                        { Skill.SOCOLA, 7, 90000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 5, 60000 }, },
                        _5_PHUT);

        public static final BossData chanmenh3 = new BossData(
                        "Siêu vegeta [CM3]", // name
                        ConstPlayer.XAYDA, // gender
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp
                        9900000000L, // dame
                        0, // dame
                        new double[][] { { 99161821300d } }, // hp
                        new short[] { 1595, 1596, 1597 }, // outfit
                        new short[] { 29, 30, 31 }, // map join
                        new int[][] { // skill
                                        { Skill.LIEN_HOAN, 1, 10 }, { Skill.MASENKO, 7, 20 },
                                        { Skill.KAMEJOKO, 3, 3000 },
                                        { Skill.SOCOLA, 7, 90000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 5, 60000 }, },
                        _5_PHUT);
        public static final BossData chanmenh4 = new BossData(
                        "Vegeta Blue [CM4]", // name
                        ConstPlayer.XAYDA, // gender
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp
                        19900000000L, // dame
                        0, // dame
                        new double[][] { { 9936813200d } }, // hp
                        new short[] { 1592, 1593, 1594 }, // outfit
                        new short[] { 29, 30, 31 }, // map join
                        new int[][] { // skill
                                        { Skill.LIEN_HOAN, 1, 10 }, { Skill.MASENKO, 7, 20 },
                                        { Skill.KAMEJOKO, 3, 3000 },
                                        { Skill.SOCOLA, 7, 90000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 5, 60000 }, },
                        _5_PHUT);

        public static final BossData chanmenh5 = new BossData(
                        "Goku Blue [CMmax]", // name
                        ConstPlayer.XAYDA, // gender
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp
                        29900000000L, // dame
                        0, // dame
                        new double[][] { { 9996800d } }, // hp
                        new short[] { 1586, 1587, 1588 }, // outfit
                        new short[] { 29, 30, 31 }, // map join
                        new int[][] { // skill
                                        { Skill.LIEN_HOAN, 1, 10 }, { Skill.MASENKO, 7, 20 },
                                        { Skill.KAMEJOKO, 3, 3000 },
                                        { Skill.SOCOLA, 7, 90000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 5, 60000 }, },
                        _5_PHUT);
        public static final BossData linhthu1 = new BossData(
                        "Cadich Tối Thượng [LinhThú]", // name
                        ConstPlayer.XAYDA, // gender
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp
                        909900000000L, // dame
                        0, // dame
                        new double[][] { { 9899281170d } }, // hp
                        new short[] { 1458, 1463, 1464 }, // outfit
                        new short[] { 131, 132, 133 }, // map join
                        new int[][] { // skill
                                        { Skill.LIEN_HOAN, 1, 10 }, { Skill.MASENKO, 7, 20 },
                                        { Skill.KAMEJOKO, 3, 3000 },
                                        { Skill.SOCOLA, 7, 90000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 5, 60000 }, },
                        _5_PHUT);
        public static final BossData linhthu2 = new BossData(
                        "Cadich Tối Thượng [LinhThú]", // name
                        ConstPlayer.XAYDA, // gender
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp
                        959900000000L, // dame
                        0, // dame
                        new double[][] { { 997873011231300d } }, // hp
                        new short[] { 1459, 1463, 1464 }, // outfit
                        new short[] { 131, 132, 133 }, // map join
                        new int[][] { // skill
                                        { Skill.LIEN_HOAN, 1, 10 }, { Skill.MASENKO, 7, 20 },
                                        { Skill.KAMEJOKO, 3, 3000 },
                                        { Skill.SOCOLA, 7, 90000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 5, 60000 }, },
                        _5_PHUT);
        public static final BossData linhthu3 = new BossData(
                        "Cadich Tối Thượng [LinhThú]", // name
                        ConstPlayer.XAYDA, // gender
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp
                        1059900000000L, // dame
                        0, // dame
                        new double[][] { { 99992833200d } }, // hp
                        new short[] { 1460, 1463, 1464 }, // outfit
                        new short[] { 131, 132, 133 }, // map join
                        new int[][] { // skill
                                        { Skill.LIEN_HOAN, 1, 10 }, { Skill.MASENKO, 7, 20 },
                                        { Skill.KAMEJOKO, 3, 3000 },
                                        { Skill.SOCOLA, 7, 90000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 5, 60000 }, },
                        _5_PHUT);

        public static final BossData linhthu4 = new BossData(
                        "Cadich Tối Thượng[LinhThú]", // name
                        ConstPlayer.XAYDA, // gender
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp
                        1559900000000L, // dame
                        0, // dame
                        new double[][] { { 9929778192300d } }, // hp
                        new short[] { 1461, 1463, 1464 }, // outfit
                        new short[] { 131, 132, 133 }, // map join
                        new int[][] { // skill
                                        { Skill.LIEN_HOAN, 1, 10 }, { Skill.MASENKO, 7, 20 },
                                        { Skill.KAMEJOKO, 3, 3000 },
                                        { Skill.SOCOLA, 7, 90000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 5, 60000 }, },
                        _5_PHUT);

        public static final BossData saophale = new BossData(
                        "Boss Spl [20h-21h]", // name
                        ConstPlayer.XAYDA, // gender
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp
                        1559900000000L, // dame
                        0, // dame
                        new double[][] { { 9929778192200d } }, // hp
                        new short[] { 1948, 1949, 1950 }, // outfit
                        new short[] { 153 }, // map join
                        new int[][] { // skill
                                        { Skill.LIEN_HOAN, 1, 10 }, { Skill.MASENKO, 7, 20 },
                                        { Skill.KAMEJOKO, 3, 3000 },
                                        { Skill.SOCOLA, 7, 90000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 5, 60000 }, },
                        _5_PHUT);

        public static final BossData luuly = new BossData(
                        "Boss Lưu Ly 7h Sáng", // name
                        ConstPlayer.XAYDA, // gender
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp
                        105599000000L, // dame
                        0, // dame
                        new double[][] { { 9950977182231300d } }, // hp
                        new short[] { 1832, 1833, 1834 }, // outfit
                        new short[] { 153 }, // map join
                        new int[][] { // skill
                                        { Skill.LIEN_HOAN, 1, 10 }, { Skill.MASENKO, 7, 20 },
                                        { Skill.KAMEJOKO, 3, 3000 },
                                        { Skill.SOCOLA, 7, 90000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 5, 60000 }, },
                        _5_PHUT);
        public static final BossData danhhieu = new BossData(
                        "Boss danh hiệu 22h", // name
                        ConstPlayer.XAYDA, // gender
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp
                        10559900000000L, // dame
                        0, // dame
                        new double[][] { { 99909778133200d } }, // hp
                        new short[] { 1571, 1572, 1573 }, // outfit
                        new short[] { 153 }, // map join
                        new int[][] { // skill
                                        { Skill.LIEN_HOAN, 1, 10 }, { Skill.MASENKO, 7, 20 },
                                        { Skill.KAMEJOKO, 3, 3000 },
                                        { Skill.SOCOLA, 7, 90000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 5, 60000 }, },
                        _5_PHUT);
        public static final BossData keybac = new BossData(
                        "Boss Key Bạc", // name
                        ConstPlayer.XAYDA, // gender
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp
                        10559900000000L, // dame
                        0, // dame
                        new double[][] { { 771_011_231_300d } }, // hp
                        new short[] { 457, 458, 459 }, // outfit
                        new short[] { 28 }, // map join
                        new int[][] { // skill
                                        { Skill.LIEN_HOAN, 1, 10 }, { Skill.MASENKO, 7, 20 },
                                        { Skill.KAMEJOKO, 3, 3000 },
                                        { Skill.SOCOLA, 7, 90000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 5, 60000 }, },
                        _5_PHUT);
        public static final BossData keyvang = new BossData(
                        "Boss Key Vàng", // name
                        ConstPlayer.XAYDA, // gender
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp
                        10559900000000L, // dame
                        0, // dame
                        new double[][] { { 909_738_233_200d } }, // hp
                        new short[] { 462, 463, 464 }, // outfit
                        new short[] { 28 }, // map join
                        new int[][] { // skill
                                        { Skill.LIEN_HOAN, 1, 10 }, { Skill.MASENKO, 7, 20 },
                                        { Skill.KAMEJOKO, 3, 3000 },
                                        { Skill.SOCOLA, 7, 90000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 5, 60000 }, },
                        _5_PHUT);
        public static final BossData dabaove = new BossData(
                        "Boss Đá Bảo Vệ", // name
                        ConstPlayer.XAYDA, // gender
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp
                        10559900000000L, // dame
                        0, // dame
                        new double[][] { { 99_038_233_200d } }, // hp
                        new short[] { 462, 463, 464 }, // outfit
                        new short[] { 28 }, // map join
                        new int[][] { // skill
                                        { Skill.LIEN_HOAN, 1, 10 }, { Skill.MASENKO, 7, 20 },
                                        { Skill.KAMEJOKO, 3, 3000 },
                                        { Skill.SOCOLA, 7, 90000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 5, 60000 }, },
                        _5_PHUT);

        public static final BossData BOSSSK2 = new BossData(
                        "Sự kiện Tết %1", // name
                        ConstPlayer.XAYDA, // gender
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp
                        5000000, // dame
                        0, // dame
                        new double[][] { { 1_800_000_000d } }, // hp
                        new short[] { 771, 676, 677 }, // outfit
                        new short[] { 1, 2, 3, 8, 9, 15, 16, 17, 6 }, // map join
                        new int[][] { // skill
                                        { Skill.DRAGON, 1, 100 }, { Skill.MASENKO, 7, 20 },
                                        { Skill.DRAGON, 3, 3000 },
                                        { Skill.DRAGON, 7, 7000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 5, 30000 }, },
                        _5_PHUT);
        public static final BossData BOSSSK3 = new BossData(
                        "Sự kiện Tết %1", // name
                        ConstPlayer.XAYDA, // gender
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp
                        5000000, // dame
                        0, // dame
                        new double[][] { { 1_800_000_000d } }, // hp
                        new short[] { 770, 672, 673 }, // outfit
                        new short[] { 1, 2, 3, 8, 9, 15, 16, 17, 6 }, // map join
                        new int[][] { // skill
                                        { Skill.DRAGON, 1, 100 }, { Skill.MASENKO, 7, 20 },
                                        { Skill.DRAGON, 3, 3000 },
                                        { Skill.DRAGON, 7, 7000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 5, 30000 }, },
                        _5_PHUT);

        public static final BossData BOSSNGOCXANH = new BossData(
                        "Pi Lắp  [100k ngọc xanh]", // name
                        ConstPlayer.XAYDA, // gender
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp
                        2000, // dame
                        0, // dame
                        new double[][] { { 5_000_000_000d } }, // hp
                        new short[] { 612, 613, 614 }, // outfit
                        new short[] { 1, 2, 3, 7, 8, 9, 15, 16, 17, 6 }, // map join
                        new int[][] { // skill
                                        { Skill.DRAGON, 1, 100 }, { Skill.MASENKO, 7, 20 }, { Skill.DRAGON, 3, 3000 },
                                        { Skill.DRAGON, 7, 7000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 7, 30000 }, },
                        _5_PHUT);

        public static final BossData TATHAN = new BossData(
                        "Kurama", // name
                        ConstPlayer.XAYDA, // gender
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp
                        2000000, // dame
                        0, // dame
                        new double[][] { { 85000000000L } }, // hp
                        new short[] { 1449, 1450, 1451 }, // outfit
                        new short[] { 108 }, // map join
                        new int[][] { // skill
                                        { Skill.DRAGON, 1, 100 }, { Skill.MASENKO, 7, 20 }, { Skill.DRAGON, 3, 3000 },
                                        { Skill.DRAGON, 7, 7000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 7, 20000 }, },
                        _5_PHUT);

        // --------------------------------------------------------------------------Broly
        public static final BossData Fide1 = new BossData(
                        "Fide 1 [CT 1K%]",
                        ConstPlayer.TRAI_DAT,
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp
                        279000000,
                        0,
                        new double[][] { { 32_000_000_000l } },
                        new short[] { 183, 184, 185 }, // ngoại hình
                        new short[] { 80 }, // map
                        new int[][] { // skill
                                        { Skill.MASENKO, 7, 10 },
                                        { Skill.KAMEJOKO, 7, 10 },
                                        { Skill.ANTOMIC, 7, 10 },
                                        { Skill.TAI_TAO_NANG_LUONG, 7, 60000 },
                                        { Skill.SOCOLA, 7, 10000 },
                                        { Skill.DRAGON, 7, 50 },
                                        { Skill.THAI_DUONG_HA_SAN, 7, 40000 },
                                        { Skill.THOI_MIEN, 7, 50000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 3, 20000 },
                                        { Skill.HUYT_SAO, 7, 20000 }, },
                        _5_PHUT);

        public static final BossData Fide2 = new BossData(
                        "Fide 2 [CT 1K2%]",
                        ConstPlayer.TRAI_DAT,
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp
                        299000000,
                        0,
                        new double[][] { { 40_444_200_000l } },
                        new short[] { 186, 187, 188 }, // ngoại hình
                        new short[] { 80 }, // map
                        new int[][] { // skill
                                        { Skill.MASENKO, 7, 10 },
                                        { Skill.KAMEJOKO, 7, 10 },
                                        { Skill.ANTOMIC, 7, 10 },
                                        { Skill.TAI_TAO_NANG_LUONG, 7, 60000 },
                                        { Skill.SOCOLA, 7, 10000 },
                                        { Skill.DRAGON, 7, 50 },
                                        { Skill.THAI_DUONG_HA_SAN, 7, 40000 },
                                        { Skill.THOI_MIEN, 7, 50000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 3, 20000 },
                                        { Skill.HUYT_SAO, 7, 20000 }, },
                        _10_GIAY);

        public static final BossData Fide3 = new BossData(
                        "Fide 3 [CT 1K5%]",
                        ConstPlayer.TRAI_DAT,
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp
                        499000000,
                        0,
                        new double[][] { { 842_000_000_000l } },
                        new short[] { 189, 190, 191 }, // ngoại hình
                        new short[] { 80 }, // map
                        new int[][] { // skill
                                        { Skill.MASENKO, 7, 10 },
                                        { Skill.KAMEJOKO, 7, 10 },
                                        { Skill.ANTOMIC, 7, 10 },
                                        { Skill.TAI_TAO_NANG_LUONG, 7, 60000 },
                                        { Skill.SOCOLA, 7, 10000 },
                                        { Skill.DRAGON, 7, 50 },
                                        { Skill.THAI_DUONG_HA_SAN, 7, 40000 },
                                        { Skill.THOI_MIEN, 7, 50000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 3, 20000 },
                                        { Skill.HUYT_SAO, 7, 20000 }, },
                        _10_GIAY);
        public static final BossData Tanjiro = new BossData(
                        "Tanjiro [SPL 10-20%]",
                        ConstPlayer.TRAI_DAT,
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp
                        1499000000,
                        0,
                        new double[][] { { 184200000000l } },
                        new short[] { 1119, 1120, 1121 }, // ngoại hình
                        new short[] { 111 }, // map
                        new int[][] { // skill
                                        { Skill.MASENKO, 7, 10 },
                                        { Skill.KAMEJOKO, 7, 10 },
                                        { Skill.ANTOMIC, 7, 10 },
                                        { Skill.TAI_TAO_NANG_LUONG, 7, 60000 },
                                        { Skill.SOCOLA, 7, 10000 },
                                        { Skill.DRAGON, 7, 50 },
                                        { Skill.THAI_DUONG_HA_SAN, 7, 40000 },
                                        { Skill.THOI_MIEN, 7, 50000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 3, 20000 },
                                        { Skill.HUYT_SAO, 7, 20000 }, },
                        _10_GIAY);
        public static final BossData Hashibira = new BossData(
                        "Hashibira [SPL 20-30%]",
                        ConstPlayer.TRAI_DAT,
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp
                        24990000l,
                        0,
                        new double[][] { { HP_100_TY } },
                        new short[] { 1122, 1123, 1124 }, // ngoại hình
                        new short[] { 111 }, // map
                        new int[][] { // skill
                                        { Skill.MASENKO, 7, 10 },
                                        { Skill.KAMEJOKO, 7, 10 },
                                        { Skill.ANTOMIC, 7, 10 },
                                        { Skill.TAI_TAO_NANG_LUONG, 7, 60000 },
                                        { Skill.SOCOLA, 7, 10000 },
                                        { Skill.DRAGON, 7, 50 },
                                        { Skill.THAI_DUONG_HA_SAN, 7, 40000 },
                                        { Skill.THOI_MIEN, 7, 50000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 3, 20000 },
                                        { Skill.HUYT_SAO, 7, 20000 }, },
                        _10_GIAY);
        public static final BossData Inosuke = new BossData(
                        "Inosuke [SPL 40-50%]",
                        ConstPlayer.TRAI_DAT,
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp
                        54990000l,
                        0,
                        new double[][] { { 99_999_000_000d } },
                        new short[] { 1131, 1132, 1133 }, // ngoại hình
                        new short[] { 111 }, // map
                        new int[][] { // skill
                                        { Skill.MASENKO, 7, 10 },
                                        { Skill.KAMEJOKO, 7, 10 },
                                        { Skill.ANTOMIC, 7, 10 },
                                        { Skill.TAI_TAO_NANG_LUONG, 7, 60000 },
                                        { Skill.SOCOLA, 7, 10000 },
                                        { Skill.DRAGON, 7, 50 },
                                        { Skill.THAI_DUONG_HA_SAN, 7, 40000 },
                                        { Skill.THOI_MIEN, 7, 50000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 3, 20000 },
                                        { Skill.HUYT_SAO, 7, 20000 }, },
                        _10_GIAY);

        public static final BossData FIDE4 = new BossData(
                        "FIDE God [Danh Hiệu 200k%]",
                        ConstPlayer.TRAI_DAT,
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp
                        DMG_10TR,
                        DEF_0,
                        new double[][] { { 9_999_000_000d } },
                        new short[] { 502, 503, 504 }, // ngoại hình
                        new short[] { 80 }, // map
                        new int[][] { // skill
                                        { Skill.MASENKO, 7, 10 },
                                        { Skill.KAMEJOKO, 7, 10 },
                                        { Skill.ANTOMIC, 7, 10 },
                                        { Skill.TAI_TAO_NANG_LUONG, 7, 60000 },
                                        { Skill.SOCOLA, 7, 10000 },
                                        { Skill.DRAGON, 7, 50 },
                                        { Skill.THAI_DUONG_HA_SAN, 7, 40000 },
                                        { Skill.THOI_MIEN, 7, 50000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 3, 20000 },
                                        { Skill.HUYT_SAO, 7, 20000 }, },
                        _30_GIAY);

        public static final BossData ADR19 = new BossData(
                        "ANDROI 19",
                        ConstPlayer.TRAI_DAT,
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp
                        DMG_1TR,
                        DEF_0,
                        new double[][] { { 6_000_000_000l } },
                        new short[] { 249, 250, 251, 102, 22 }, // ngoại hình
                        new short[] { 93 }, // map
                        new int[][] { // skill
                                        { Skill.MASENKO, 7, 10 },
                                        { Skill.KAMEJOKO, 7, 10 },
                                        { Skill.ANTOMIC, 7, 10 },
                                        { Skill.TAI_TAO_NANG_LUONG, 7, 60000 },
                                        { Skill.SOCOLA, 7, 10000 },
                                        { Skill.DRAGON, 7, 50 },
                                        { Skill.THAI_DUONG_HA_SAN, 7, 40000 },
                                        { Skill.THOI_MIEN, 7, 50000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 3, 20000 },
                                        { Skill.HUYT_SAO, 7, 20000 }, },
                        _30_GIAY);

        public static final BossData ADR20 = new BossData(
                        "ANDROI 20",
                        ConstPlayer.TRAI_DAT,
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp
                        DMG_1TR,
                        DEF_0,
                        new double[][] { { HP_1_TY } },
                        new short[] { 255, 256, 257 }, // ngoại hình
                        new short[] { 93 }, // map
                        new int[][] { // skill
                                        { Skill.MASENKO, 7, 10 },
                                        { Skill.KAMEJOKO, 7, 10 },
                                        { Skill.ANTOMIC, 7, 10 },
                                        { Skill.TAI_TAO_NANG_LUONG, 7, 60000 },
                                        { Skill.SOCOLA, 7, 10000 },
                                        { Skill.DRAGON, 7, 50 },
                                        { Skill.THAI_DUONG_HA_SAN, 7, 40000 },
                                        { Skill.THOI_MIEN, 7, 50000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 3, 20000 },
                                        { Skill.HUYT_SAO, 7, 20000 }, },
                        _30_GIAY);

        public static final BossData PIC = new BossData(
                        "PIC",
                        ConstPlayer.TRAI_DAT,
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp
                        DMG_10TR,
                        DEF_0,
                        new double[][] { { HP_1_TY } },
                        new short[] { 1049, 1047, 1048 }, // ngoại hình
                        new short[] { 94, 96, 100 }, // map
                        new int[][] { // skill
                                        { Skill.MASENKO, 7, 10 },
                                        { Skill.KAMEJOKO, 7, 10 },
                                        { Skill.ANTOMIC, 7, 10 },
                                        { Skill.TAI_TAO_NANG_LUONG, 7, 60000 },
                                        { Skill.SOCOLA, 7, 10000 },
                                        { Skill.DRAGON, 7, 50 },
                                        { Skill.THAI_DUONG_HA_SAN, 7, 40000 },
                                        { Skill.THOI_MIEN, 7, 50000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 3, 20000 },
                                        { Skill.HUYT_SAO, 7, 20000 }, },
                        _30_GIAY);

        public static final BossData POC = new BossData(
                        "POC",
                        ConstPlayer.TRAI_DAT,
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp
                        DMG_1TR,
                        DEF_0,
                        new double[][] { { HP_1_TY } },
                        new short[] { 1046, 1047, 1048 }, // ngoại hình
                        new short[] { 94, 96, 100 }, // map
                        new int[][] { // skill
                                        { Skill.MASENKO, 7, 10 },
                                        { Skill.KAMEJOKO, 7, 10 },
                                        { Skill.ANTOMIC, 7, 10 },
                                        { Skill.TAI_TAO_NANG_LUONG, 7, 60000 },
                                        { Skill.SOCOLA, 7, 10000 },
                                        { Skill.DRAGON, 7, 50 },
                                        { Skill.THAI_DUONG_HA_SAN, 7, 40000 },
                                        { Skill.THOI_MIEN, 7, 50000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 3, 20000 },
                                        { Skill.HUYT_SAO, 7, 20000 }, },
                        _30_GIAY);

        public static final BossData XENBOHUNG1 = new BossData(
                        "Xên Bọ Hung 1",
                        ConstPlayer.TRAI_DAT,
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp
                        DMG_1TR,
                        DEF_0,
                        new double[][] { { HP_10_TY } },
                        new short[] { 228, 229, 230 }, // ngoại hình
                        new short[] { 97 }, // map
                        new int[][] { // skill
                                        { Skill.MASENKO, 7, 10 },
                                        { Skill.KAMEJOKO, 7, 10 },
                                        { Skill.ANTOMIC, 7, 10 },
                                        { Skill.TAI_TAO_NANG_LUONG, 7, 60000 },
                                        { Skill.SOCOLA, 7, 10000 },
                                        { Skill.DRAGON, 7, 50 },
                                        { Skill.THAI_DUONG_HA_SAN, 7, 40000 },
                                        { Skill.THOI_MIEN, 7, 50000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 3, 20000 },
                                        { Skill.HUYT_SAO, 7, 20000 }, },
                        _30_GIAY);

        public static final BossData XENBOHUNG2 = new BossData(
                        "Xên Bọ Hung 2",
                        ConstPlayer.TRAI_DAT,
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp
                        DMG_1TR,
                        DEF_0,
                        new double[][] { { HP_10_TY } },
                        new short[] { 231, 232, 233 }, // ngoại hình
                        new short[] { 97 }, // map
                        new int[][] { // skill
                                        { Skill.MASENKO, 7, 10 },
                                        { Skill.KAMEJOKO, 7, 10 },
                                        { Skill.ANTOMIC, 7, 10 },
                                        { Skill.TAI_TAO_NANG_LUONG, 7, 60000 },
                                        { Skill.SOCOLA, 7, 10000 },
                                        { Skill.DRAGON, 7, 50 },
                                        { Skill.THAI_DUONG_HA_SAN, 7, 40000 },
                                        { Skill.THOI_MIEN, 7, 50000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 3, 20000 },
                                        { Skill.HUYT_SAO, 7, 20000 }, },
                        _30_GIAY);

        public static final BossData XENBOHUNG3 = new BossData(
                        "Xên Bọ Hung 3",
                        ConstPlayer.TRAI_DAT,
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp
                        19000000,
                        DEF_0,
                        new double[][] { { 5000000000l } },
                        new short[] { 234, 235, 236 }, // ngoại hình
                        new short[] { 97 }, // map
                        new int[][] { // skill
                                        { Skill.MASENKO, 7, 10 },
                                        { Skill.KAMEJOKO, 7, 10 },
                                        { Skill.ANTOMIC, 7, 10 },
                                        { Skill.TAI_TAO_NANG_LUONG, 7, 60000 },
                                        { Skill.SOCOLA, 7, 10000 },
                                        { Skill.DRAGON, 7, 50 },
                                        { Skill.THAI_DUONG_HA_SAN, 7, 40000 },
                                        { Skill.THOI_MIEN, 7, 50000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 3, 20000 },
                                        { Skill.HUYT_SAO, 7, 20000 }, },
                        _30_GIAY);
        public static final BossData XENCON1 = new BossData(
                        "Xên Con 1",
                        ConstPlayer.TRAI_DAT,
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp
                        DMG_1TR,
                        DEF_0,
                        new double[][] { { HP_1_TY } },
                        new short[] { 264, 265, 266 }, // ngoại hình
                        new short[] { 103 }, // map
                        new int[][] { // skill
                                        { Skill.MASENKO, 7, 10 },
                                        { Skill.KAMEJOKO, 7, 10 },
                                        { Skill.ANTOMIC, 7, 10 },
                                        { Skill.TAI_TAO_NANG_LUONG, 7, 60000 },
                                        { Skill.SOCOLA, 7, 10000 },
                                        { Skill.DRAGON, 7, 50 },
                                        { Skill.THAI_DUONG_HA_SAN, 7, 40000 },
                                        { Skill.THOI_MIEN, 7, 50000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 3, 20000 },
                                        { Skill.HUYT_SAO, 7, 20000 }, },
                        _30_GIAY);

        public static final BossData XENCON2 = new BossData(
                        "Xên Con 2",
                        ConstPlayer.TRAI_DAT,
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp
                        DMG_1TR,
                        DEF_0,
                        new double[][] { { HP_1_TY } },
                        new short[] { 264, 265, 266 }, // ngoại hình
                        new short[] { 103 }, // map
                        new int[][] { // skill
                                        { Skill.MASENKO, 7, 10 },
                                        { Skill.KAMEJOKO, 7, 10 },
                                        { Skill.ANTOMIC, 7, 10 },
                                        { Skill.TAI_TAO_NANG_LUONG, 7, 60000 },
                                        { Skill.SOCOLA, 7, 10000 },
                                        { Skill.DRAGON, 7, 50 },
                                        { Skill.THAI_DUONG_HA_SAN, 7, 40000 },
                                        { Skill.THOI_MIEN, 7, 50000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 3, 20000 },
                                        { Skill.HUYT_SAO, 7, 20000 }, },
                        _30_GIAY);

        public static final BossData XENCON3 = new BossData(
                        "Xên Con 3",
                        ConstPlayer.TRAI_DAT,
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp
                        DMG_1TR,
                        DEF_0,
                        new double[][] { { HP_1_TY } },
                        new short[] { 264, 265, 266 }, // ngoại hình
                        new short[] { 103 }, // map
                        new int[][] { // skill
                                        { Skill.MASENKO, 7, 10 },
                                        { Skill.KAMEJOKO, 7, 10 },
                                        { Skill.ANTOMIC, 7, 10 },
                                        { Skill.TAI_TAO_NANG_LUONG, 7, 60000 },
                                        { Skill.SOCOLA, 7, 10000 },
                                        { Skill.DRAGON, 7, 50 },
                                        { Skill.THAI_DUONG_HA_SAN, 7, 40000 },
                                        { Skill.THOI_MIEN, 7, 50000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 3, 20000 },
                                        { Skill.HUYT_SAO, 7, 20000 }, },
                        _30_GIAY);

        public static final BossData XENCON4 = new BossData(
                        "Xên Con 4",
                        ConstPlayer.TRAI_DAT,
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp
                        DMG_1TR,
                        DEF_0,
                        new double[][] { { HP_1_TY } },
                        new short[] { 264, 265, 266 }, // ngoại hình
                        new short[] { 103 }, // map
                        new int[][] { // skill
                                        { Skill.MASENKO, 7, 10 },
                                        { Skill.KAMEJOKO, 7, 10 },
                                        { Skill.ANTOMIC, 7, 10 },
                                        { Skill.TAI_TAO_NANG_LUONG, 7, 60000 },
                                        { Skill.SOCOLA, 7, 10000 },
                                        { Skill.DRAGON, 7, 50 },
                                        { Skill.THAI_DUONG_HA_SAN, 7, 40000 },
                                        { Skill.THOI_MIEN, 7, 50000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 3, 20000 },
                                        { Skill.HUYT_SAO, 7, 20000 }, },
                        _30_GIAY);

        public static final BossData XENCON5 = new BossData(
                        "Xên Con 5",
                        ConstPlayer.TRAI_DAT,
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp
                        DMG_1TR,
                        DEF_0,
                        new double[][] { { HP_1_TY } },
                        new short[] { 264, 265, 266 }, // ngoại hình
                        new short[] { 103 }, // map
                        new int[][] { // skill
                                        { Skill.MASENKO, 7, 10 },
                                        { Skill.KAMEJOKO, 7, 10 },
                                        { Skill.ANTOMIC, 7, 10 },
                                        { Skill.TAI_TAO_NANG_LUONG, 7, 60000 },
                                        { Skill.SOCOLA, 7, 10000 },
                                        { Skill.DRAGON, 7, 50 },
                                        { Skill.THAI_DUONG_HA_SAN, 7, 40000 },
                                        { Skill.THOI_MIEN, 7, 50000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 3, 20000 },
                                        { Skill.HUYT_SAO, 7, 20000 }, },
                        _30_GIAY);

        public static final BossData XENCON6 = new BossData(
                        "Xên Con 6",
                        ConstPlayer.TRAI_DAT,
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp
                        DMG_1TR,
                        DEF_0,
                        new double[][] { { HP_1_TY } },
                        new short[] { 264, 265, 266 }, // ngoại hình
                        new short[] { 103 }, // map
                        new int[][] { // skill
                                        { Skill.MASENKO, 7, 10 },
                                        { Skill.KAMEJOKO, 7, 10 },
                                        { Skill.ANTOMIC, 7, 10 },
                                        { Skill.TAI_TAO_NANG_LUONG, 7, 60000 },
                                        { Skill.SOCOLA, 7, 10000 },
                                        { Skill.DRAGON, 7, 50 },
                                        { Skill.THAI_DUONG_HA_SAN, 7, 40000 },
                                        { Skill.THOI_MIEN, 7, 50000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 3, 20000 },
                                        { Skill.HUYT_SAO, 7, 20000 }, },
                        _30_GIAY);

        public static final BossData XENCON7 = new BossData(
                        "Xên Con 7",
                        ConstPlayer.TRAI_DAT,
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp
                        28000000l,
                        DEF_0,
                        new double[][] { { 1827237000l } },
                        new short[] { 264, 265, 266 }, // ngoại hình
                        new short[] { 103 }, // map
                        new int[][] { // skill
                                        { Skill.MASENKO, 7, 10 },
                                        { Skill.KAMEJOKO, 7, 10 },
                                        { Skill.ANTOMIC, 7, 10 },
                                        { Skill.TAI_TAO_NANG_LUONG, 7, 60000 },
                                        { Skill.SOCOLA, 7, 10000 },
                                        { Skill.DRAGON, 7, 50 },
                                        { Skill.THAI_DUONG_HA_SAN, 7, 40000 },
                                        { Skill.THOI_MIEN, 7, 50000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 3, 20000 },
                                        { Skill.HUYT_SAO, 7, 20000 }, },
                        _30_GIAY);

        public static final BossData Blackgoku = new BossData(
                        "Black Goku [Chân mệnh]",
                        ConstPlayer.TRAI_DAT,
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp
                        7499000000l,
                        0,
                        new double[][] { { 168420000000l } },
                        new short[] { 550, 551, 552 }, // outfit
                        new short[] { 92, 93, 94 }, // map join
                        new int[][] { // skill
                                        { Skill.MASENKO, 7, 10 },
                                        { Skill.KAMEJOKO, 7, 10 },
                                        { Skill.ANTOMIC, 7, 10 },
                                        { Skill.TAI_TAO_NANG_LUONG, 7, 60000 },
                                        { Skill.SOCOLA, 7, 10000 },
                                        { Skill.DRAGON, 7, 50 },
                                        { Skill.THAI_DUONG_HA_SAN, 7, 40000 },
                                        { Skill.THOI_MIEN, 7, 50000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 3, 20000 },
                                        { Skill.HUYT_SAO, 7, 20000 }, },
                        _30_GIAY);
        public static final BossData SuperBlackgoku = new BossData(
                        "Super Black Goku [Chân mệnh]",
                        ConstPlayer.TRAI_DAT,
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp
                        9499000000l,
                        0,
                        new double[][] { { 36842_000_000_000l } },
                        new short[] { 553, 551, 552 }, // outfit
                        new short[] { 92, 93, 94 }, // map join
                        new int[][] { // skill
                                        { Skill.MASENKO, 7, 10 },
                                        { Skill.KAMEJOKO, 7, 10 },
                                        { Skill.ANTOMIC, 7, 10 },
                                        { Skill.TAI_TAO_NANG_LUONG, 7, 60000 },
                                        { Skill.SOCOLA, 7, 10000 },
                                        { Skill.DRAGON, 7, 50 },
                                        { Skill.THAI_DUONG_HA_SAN, 7, 40000 },
                                        { Skill.THOI_MIEN, 7, 50000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 3, 20000 },
                                        { Skill.HUYT_SAO, 7, 20000 }, },
                        _30_GIAY);
        public static final BossData VegetaSS1 = new BossData(
                        "Vegeta SS1 [Đeo Lưng]",
                        ConstPlayer.TRAI_DAT,
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp
                        19499000000l,
                        0,
                        new double[][] { { 18684200000000l } },
                        new short[] { 1460, 1463, 1464 }, // outfit
                        new short[] { 92, 93, 94, 103 }, // map join
                        new int[][] { // skill
                                        { Skill.MASENKO, 7, 10 },
                                        { Skill.KAMEJOKO, 7, 10 },
                                        { Skill.ANTOMIC, 7, 10 },
                                        { Skill.TAI_TAO_NANG_LUONG, 7, 60000 },
                                        { Skill.SOCOLA, 7, 10000 },
                                        { Skill.DRAGON, 7, 50 },
                                        { Skill.THAI_DUONG_HA_SAN, 7, 40000 },
                                        { Skill.THOI_MIEN, 7, 50000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 3, 20000 },
                                        { Skill.HUYT_SAO, 7, 20000 }, },
                        _30_GIAY);
        public static final BossData SieuXen = new BossData(
                        "Siêu Xên 3D [Đeo Lưng]",
                        ConstPlayer.TRAI_DAT,
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp
                        25499000000l,
                        0,
                        new double[][] { { 3868420000000l } },
                        new short[] { 1544, 1545, 1546 }, // outfit
                        new short[] { 103 }, // map join
                        new int[][] { // skill
                                        { Skill.MASENKO, 7, 10 },
                                        { Skill.KAMEJOKO, 7, 10 },
                                        { Skill.ANTOMIC, 7, 10 },
                                        { Skill.TAI_TAO_NANG_LUONG, 7, 60000 },
                                        { Skill.SOCOLA, 7, 10000 },
                                        { Skill.DRAGON, 7, 50 },
                                        { Skill.THAI_DUONG_HA_SAN, 7, 40000 },
                                        { Skill.THOI_MIEN, 7, 50000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 3, 20000 },
                                        { Skill.HUYT_SAO, 7, 20000 }, },
                        _30_GIAY);
        public static final BossData ColerVang = new BossData(
                        "Coler Vàng [EXP Tiên Bang]",
                        ConstPlayer.TRAI_DAT,
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp
                        25499000l,
                        0,
                        new double[][] { { 5869200928l } },
                        new short[] { 709, 710, 711 }, // outfit
                        new short[] { 105, 106, 107, 108, 109, 110 }, // map join
                        new int[][] { // skill
                                        { Skill.MASENKO, 7, 10 },
                                        { Skill.KAMEJOKO, 7, 10 },
                                        { Skill.ANTOMIC, 7, 10 },
                                        { Skill.TAI_TAO_NANG_LUONG, 7, 60000 },
                                        { Skill.SOCOLA, 7, 10000 },
                                        { Skill.DRAGON, 7, 50 },
                                        { Skill.THAI_DUONG_HA_SAN, 7, 40000 },
                                        { Skill.THOI_MIEN, 7, 50000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 3, 20000 },
                                        { Skill.HUYT_SAO, 7, 20000 }, },
                        _60_GIAY);

        public static final BossData obito = new BossData(
                        "Obito",
                        ConstPlayer.TRAI_DAT,
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp
                        200000,
                        0,
                        new double[][] { { 500000000l } },
                        new short[] { 1646, 1647, 1648 }, // outfit
                        new short[] { 42, 8, 17 }, // map join
                        new int[][] { // skill
                                        // {Skill.MASENKO, 7, 10},
                                        { Skill.DICH_CHUYEN_TUC_THOI, 7, 10000 },
                                        { Skill.KAMEJOKO, 7, 10 },
                                        { Skill.ANTOMIC, 7, 10 },
                                        { Skill.TAI_TAO_NANG_LUONG, 7, 60000 },
                                        // {Skill.SOCOLA, 7, 10000},
                                        { Skill.DRAGON, 7, 50 },
                                        // {Skill.THAI_DUONG_HA_SAN, 7, 40000},
                                        // {Skill.THOI_MIEN, 7, 50000},
                                        { Skill.TAI_TAO_NANG_LUONG, 3, 20000 },
                                        { Skill.HUYT_SAO, 7, 20000 }, },
                        _60_GIAY);
        public static final BossData naruto = new BossData(
                        "Naruto",
                        ConstPlayer.TRAI_DAT,
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp
                        300000,
                        0,
                        new double[][] { { 500000000l } },
                        new short[] { 1640, 1641, 1642 }, // outfit
                        new short[] { 42, 8, 17 }, // map join
                        new int[][] { // skill
                                        // {Skill.MASENKO, 7, 10},
                                        { Skill.KAIOKEN, 7, 1000 },
                                        { Skill.KAMEJOKO, 7, 10 },
                                        { Skill.ANTOMIC, 7, 10 },
                                        { Skill.TAI_TAO_NANG_LUONG, 7, 60000 },
                                        // {Skill.SOCOLA, 7, 10000},
                                        { Skill.DRAGON, 7, 50 },
                                        // {Skill.THAI_DUONG_HA_SAN, 7, 40000},
                                        // {Skill.THOI_MIEN, 7, 50000},
                                        { Skill.TAI_TAO_NANG_LUONG, 3, 20000 },
                                        { Skill.HUYT_SAO, 7, 20000 }, },
                        _60_GIAY);

        public static final BossData BOSSNOBITA = new BossData(
                        "Nobita [Linh Thú 50k% HP,KI,SD]",
                        ConstPlayer.TRAI_DAT,
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp
                        1405000d,
                        80000000d,
                        new double[][] { { 98990892170D } },
                        new short[] { 844, 845, 846 }, // outfit
                        new short[] { 19 }, // map join
                        new int[][] { // skill
                                        { Skill.MASENKO, 7, 10 },
                                        { Skill.KAMEJOKO, 7, 10 },
                                        { Skill.ANTOMIC, 7, 10 },
                                        { Skill.TAI_TAO_NANG_LUONG, 7, 60000 },
                                        { Skill.SOCOLA, 7, 10000 },
                                        { Skill.DRAGON, 7, 50 },
                                        { Skill.THAI_DUONG_HA_SAN, 7, 40000 },
                                        { Skill.THOI_MIEN, 7, 50000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 3, 20000 },
                                        { Skill.HUYT_SAO, 7, 20000 }, },
                        _60_GIAY);

        public static final BossData BOSSDOREMON = new BossData(
                        "Đô Rê Mon [Linh Thú 50k% HP,KI,SD]",
                        ConstPlayer.TRAI_DAT,
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp
                        1405900d,
                        8000d,
                        new double[][] { { 989908170D } },
                        new short[] { 790, 791, 792 }, // outfit
                        new short[] { 19 }, // map join
                        new int[][] { // skill
                                        { Skill.MASENKO, 7, 10 },
                                        { Skill.KAMEJOKO, 7, 10 },
                                        { Skill.ANTOMIC, 7, 10 },
                                        { Skill.TAI_TAO_NANG_LUONG, 7, 60000 },
                                        { Skill.SOCOLA, 7, 10000 },
                                        { Skill.DRAGON, 7, 50 },
                                        { Skill.THAI_DUONG_HA_SAN, 7, 40000 },
                                        { Skill.THOI_MIEN, 7, 50000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 3, 20000 },
                                        { Skill.HUYT_SAO, 7, 20000 }, },
                        _60_GIAY);

        public static final BossData BOSSXUKA = new BossData(
                        "XU KA [Linh Thú 50k% HP,KI,SD]",
                        ConstPlayer.TRAI_DAT,
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp
                        1405000d,
                        80000000d,
                        new double[][] { { 284722170000D } },
                        new short[] { 802, 803, 804 }, // outfit
                        new short[] { 19 }, // map join
                        new int[][] { // skill
                                        { Skill.MASENKO, 7, 10 },
                                        { Skill.KAMEJOKO, 7, 10 },
                                        { Skill.ANTOMIC, 7, 10 },
                                        { Skill.TAI_TAO_NANG_LUONG, 7, 60000 },
                                        { Skill.SOCOLA, 7, 10000 },
                                        { Skill.DRAGON, 7, 50 },
                                        { Skill.THAI_DUONG_HA_SAN, 7, 40000 },
                                        { Skill.THOI_MIEN, 7, 50000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 3, 20000 },
                                        { Skill.HUYT_SAO, 7, 20000 }, },
                        _60_GIAY);

        public static final BossData BOSSCHAIEN = new BossData(
                        "Chai En [Linh Thú 50k% HP,KI,SD]",
                        ConstPlayer.TRAI_DAT,
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp
                        1405022000d,
                        80d,
                        new double[][] { { 9899082170D } },
                        new short[] { 847, 848, 849 }, // outfit
                        new short[] { 19 }, // map join
                        new int[][] { // skill
                                        { Skill.MASENKO, 7, 10 },
                                        { Skill.KAMEJOKO, 7, 10 },
                                        { Skill.ANTOMIC, 7, 10 },
                                        { Skill.TAI_TAO_NANG_LUONG, 7, 60000 },
                                        { Skill.SOCOLA, 7, 10000 },
                                        { Skill.DRAGON, 7, 50 },
                                        { Skill.THAI_DUONG_HA_SAN, 7, 40000 },
                                        { Skill.THOI_MIEN, 7, 50000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 3, 20000 },
                                        { Skill.HUYT_SAO, 7, 20000 }, },
                        _60_GIAY);
        public static final BossData BOSSXEKO = new BossData(
                        "XE KO [Linh Thú 50k% HP,KI,SD]",
                        ConstPlayer.TRAI_DAT,
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp
                        1405000088800d,
                        800d,
                        new double[][] { { 989908270D } },
                        new short[] { 850, 851, 852 }, // outfit
                        new short[] { 19 }, // map join
                        new int[][] { // skill
                                        { Skill.MASENKO, 7, 10 },
                                        { Skill.KAMEJOKO, 7, 10 },
                                        { Skill.ANTOMIC, 7, 10 },
                                        { Skill.TAI_TAO_NANG_LUONG, 7, 60000 },
                                        { Skill.SOCOLA, 7, 10000 },
                                        { Skill.DRAGON, 7, 50 },
                                        { Skill.THAI_DUONG_HA_SAN, 7, 40000 },
                                        { Skill.THOI_MIEN, 7, 50000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 3, 20000 },
                                        { Skill.HUYT_SAO, 7, 20000 }, },
                        _60_GIAY);

        public static final BossData BOSSZAMASU = new BossData(
                        "Zamasu Base [giáp luyện tập 10k%]",
                        ConstPlayer.TRAI_DAT,
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp
                        133333d,
                        100l,
                        new double[][] { { 9000000000000d } },
                        new short[] { 903, 904, 905 }, // outfit
                        new short[] { 92 }, // map join
                        new int[][] { // skill
                                        { Skill.MASENKO, 7, 10 },
                                        { Skill.KAMEJOKO, 7, 10 },
                                        { Skill.ANTOMIC, 7, 10 },
                                        { Skill.TAI_TAO_NANG_LUONG, 7, 60000 },
                                        { Skill.SOCOLA, 7, 10000 },
                                        { Skill.DRAGON, 7, 50 },
                                        { Skill.THAI_DUONG_HA_SAN, 7, 40000 },
                                        { Skill.THOI_MIEN, 7, 50000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 3, 20000 },
                                        { Skill.HUYT_SAO, 7, 20000 }, },
                        _60_GIAY);

        public static final BossData DaThu1 = new BossData(
                        "Shukaku",
                        ConstPlayer.TRAI_DAT,
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp
                        8000000,
                        0,
                        new double[][] { { 15000000000l } },
                        new short[] { 1791, 1792, 1793 }, // ngoại hình
                        new short[] { 63, 64, 65, 66, 68, 67, 69, 70, 71, 72, 73, 74, 75, 76, 77, 79, 80, 81, 82, 83 }, // map
                        new int[][] { // skill
                                        { Skill.MASENKO, 7, 10 },
                                        { Skill.KAMEJOKO, 7, 10 },
                                        { Skill.ANTOMIC, 7, 10 },
                                        { Skill.TAI_TAO_NANG_LUONG, 7, 60000 },
                                        { Skill.SOCOLA, 7, 10000 },
                                        { Skill.DRAGON, 7, 50 },
                                        { Skill.THAI_DUONG_HA_SAN, 7, 40000 },
                                        { Skill.THOI_MIEN, 7, 50000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 3, 20000 },
                                        { Skill.HUYT_SAO, 7, 20000 }, },
                        _60_GIAY);

        public static final BossData DaThu2 = new BossData(
                        "Matatabi",
                        ConstPlayer.TRAI_DAT,
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp
                        15000000,
                        0,
                        new double[][] { { 29000000000l } },
                        new short[] { 1794, 1795, 1796 }, // ngoại hình
                        new short[] { 63, 64, 65, 66, 68, 67, 69, 70, 71, 72, 73, 74, 75, 76, 77, 79, 80, 81, 82, 83 }, // map
                        new int[][] { // skill
                                        { Skill.MASENKO, 7, 10 },
                                        { Skill.KAMEJOKO, 7, 10 },
                                        { Skill.ANTOMIC, 7, 10 },
                                        { Skill.TAI_TAO_NANG_LUONG, 7, 60000 },
                                        { Skill.SOCOLA, 7, 10000 },
                                        { Skill.DRAGON, 7, 50 },
                                        { Skill.THAI_DUONG_HA_SAN, 7, 40000 },
                                        { Skill.THOI_MIEN, 7, 50000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 3, 20000 },
                                        { Skill.HUYT_SAO, 7, 20000 }, },
                        _60_GIAY);

        public static final BossData DaThu3 = new BossData(
                        "Isobu",
                        ConstPlayer.TRAI_DAT,
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp
                        19000000,
                        0,
                        new double[][] { { 4200000000l } },
                        new short[] { 1797, 1798, 1799 }, // ngoại hình
                        new short[] { 63, 64, 65, 66, 68, 67, 69, 70, 71, 72, 73, 74, 75, 76, 77, 79, 80, 81, 82, 83 }, // map
                        new int[][] { // skill
                                        { Skill.MASENKO, 7, 10 },
                                        { Skill.KAMEJOKO, 7, 10 },
                                        { Skill.ANTOMIC, 7, 10 },
                                        { Skill.TAI_TAO_NANG_LUONG, 7, 60000 },
                                        { Skill.SOCOLA, 7, 10000 },
                                        { Skill.DRAGON, 7, 50 },
                                        { Skill.THAI_DUONG_HA_SAN, 7, 40000 },
                                        { Skill.THOI_MIEN, 7, 50000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 3, 20000 },
                                        { Skill.HUYT_SAO, 7, 20000 }, },
                        _60_GIAY);

        public static final BossData DaThu4 = new BossData(
                        "Son Goku",
                        ConstPlayer.TRAI_DAT,
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp
                        29000000,
                        0,
                        new double[][] { { 49000000000l } },
                        new short[] { 1800, 1801, 1802 }, // ngoại hình
                        new short[] { 63, 64, 65, 66, 68, 67, 69, 70, 71, 72, 73, 74, 75, 76, 77, 79, 80, 81, 82, 83 }, // map
                        new int[][] { // skill
                                        { Skill.MASENKO, 7, 10 },
                                        { Skill.KAMEJOKO, 7, 10 },
                                        { Skill.ANTOMIC, 7, 10 },
                                        { Skill.TAI_TAO_NANG_LUONG, 7, 60000 },
                                        { Skill.SOCOLA, 7, 10000 },
                                        { Skill.DRAGON, 7, 50 },
                                        { Skill.THAI_DUONG_HA_SAN, 7, 40000 },
                                        { Skill.THOI_MIEN, 7, 50000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 3, 20000 },
                                        { Skill.HUYT_SAO, 7, 20000 }, },
                        _60_GIAY);
        public static final BossData DaThu5 = new BossData(
                        "Kokuo",
                        ConstPlayer.TRAI_DAT,
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp
                        39000000,
                        0,
                        new double[][] { { 79000000000l } },
                        new short[] { 1803, 1804, 1805 }, // ngoại hình
                        new short[] { 63, 64, 65, 66, 68, 67, 69, 70, 71, 72, 73, 74, 75, 76, 77, 79, 80, 81, 82, 83 }, // map
                        new int[][] { // skill
                                        { Skill.MASENKO, 7, 10 },
                                        { Skill.KAMEJOKO, 7, 10 },
                                        { Skill.ANTOMIC, 7, 10 },
                                        { Skill.TAI_TAO_NANG_LUONG, 7, 60000 },
                                        { Skill.SOCOLA, 7, 10000 },
                                        { Skill.DRAGON, 7, 50 },
                                        { Skill.THAI_DUONG_HA_SAN, 7, 40000 },
                                        { Skill.THOI_MIEN, 7, 50000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 3, 20000 },
                                        { Skill.HUYT_SAO, 7, 20000 }, },
                        _60_GIAY);

        public static final BossData DaThu6 = new BossData(
                        "Saiken",
                        ConstPlayer.TRAI_DAT,
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp
                        49000000,
                        0,
                        new double[][] { { 85000000000l } },
                        new short[] { 1806, 1807, 1808 }, // ngoại hình
                        new short[] { 63, 64, 65, 66, 68, 67, 69, 70, 71, 72, 73, 74, 75, 76, 77, 79, 80, 81, 82, 83 }, // map
                        new int[][] { // skill
                                        { Skill.MASENKO, 7, 10 },
                                        { Skill.KAMEJOKO, 7, 10 },
                                        { Skill.ANTOMIC, 7, 10 },
                                        { Skill.TAI_TAO_NANG_LUONG, 7, 60000 },
                                        { Skill.SOCOLA, 7, 10000 },
                                        { Skill.DRAGON, 7, 50 },
                                        { Skill.THAI_DUONG_HA_SAN, 7, 40000 },
                                        { Skill.THOI_MIEN, 7, 50000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 3, 20000 },
                                        { Skill.HUYT_SAO, 7, 20000 }, },
                        _5_PHUT);
        public static final BossData DaThu7 = new BossData(
                        "Chomei",
                        ConstPlayer.TRAI_DAT,
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp
                        79000000,
                        0,
                        new double[][] { { 99000000000l } },
                        new short[] { 1809, 1810, 1811 }, // ngoại hình
                        new short[] { 63, 64, 65, 66, 68, 67, 69, 70, 71, 72, 73, 74, 75, 76, 77, 79, 80, 81, 82, 83 }, // map
                        new int[][] { // skill
                                        { Skill.MASENKO, 7, 10 },
                                        { Skill.KAMEJOKO, 7, 10 },
                                        { Skill.ANTOMIC, 7, 10 },
                                        { Skill.TAI_TAO_NANG_LUONG, 7, 60000 },
                                        { Skill.SOCOLA, 7, 10000 },
                                        { Skill.DRAGON, 7, 50 },
                                        { Skill.THAI_DUONG_HA_SAN, 7, 40000 },
                                        { Skill.THOI_MIEN, 7, 50000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 3, 20000 },
                                        { Skill.HUYT_SAO, 7, 20000 }, },
                        _5_PHUT);
        public static final BossData DaThu8 = new BossData(
                        "Gyuki",
                        ConstPlayer.TRAI_DAT,
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp
                        179000000,
                        0,
                        new double[][] { { 1990000000l } },
                        new short[] { 1812, 1813, 1814 }, // ngoại hình
                        new short[] { 63, 64, 65, 66, 68, 67, 69, 70, 71, 72, 73, 74, 75, 76, 77, 79, 80, 81, 82, 83 }, // map
                        new int[][] { // skill
                                        { Skill.MASENKO, 7, 10 },
                                        { Skill.KAMEJOKO, 7, 10 },
                                        { Skill.ANTOMIC, 7, 10 },
                                        { Skill.TAI_TAO_NANG_LUONG, 7, 60000 },
                                        { Skill.SOCOLA, 7, 10000 },
                                        { Skill.DRAGON, 7, 50 },
                                        { Skill.THAI_DUONG_HA_SAN, 7, 40000 },
                                        { Skill.THOI_MIEN, 7, 50000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 3, 20000 },
                                        { Skill.HUYT_SAO, 7, 20000 }, },
                        _5_PHUT);

        public static final BossData FROST1 = new BossData(
                        "FROST 1 (nro băng)",
                        ConstPlayer.TRAI_DAT,
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp
                        50000d,
                        280000d,
                        new double[][] { { 100_000_000_000d } },
                        new short[] { 493, 494, 495 }, // ngoại hình
                        new short[] { 105, 106, 107, 108, 109, 110 }, // map
                        new int[][] { // skill
                                        { Skill.MASENKO, 7, 10 },
                                        { Skill.KAMEJOKO, 7, 10 },
                                        { Skill.ANTOMIC, 7, 10 },
                                        { Skill.TAI_TAO_NANG_LUONG, 7, 60000 },
                                        { Skill.SOCOLA, 7, 10000 },
                                        { Skill.DRAGON, 7, 50 },
                                        { Skill.THAI_DUONG_HA_SAN, 7, 40000 },
                                        { Skill.THOI_MIEN, 7, 50000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 3, 20000 },
                                        { Skill.HUYT_SAO, 7, 20000 }, },
                        _5_PHUT);

        public static final BossData FROST2 = new BossData(
                        "FROST 2 (nro băng)",
                        ConstPlayer.TRAI_DAT,
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp
                        5722720d,
                        2860000000000000d,
                        new double[][] { { 1000_000_000d } },
                        new short[] { 496, 497, 498 }, // ngoại hình
                        new short[] { 105, 106, 107, 108, 109, 110 }, // map
                        new int[][] { // skill
                                        { Skill.MASENKO, 7, 10 },
                                        { Skill.KAMEJOKO, 7, 10 },
                                        { Skill.ANTOMIC, 7, 10 },
                                        { Skill.TAI_TAO_NANG_LUONG, 7, 60000 },
                                        { Skill.SOCOLA, 7, 10000 },
                                        { Skill.DRAGON, 7, 50 },
                                        { Skill.THAI_DUONG_HA_SAN, 7, 40000 },
                                        { Skill.THOI_MIEN, 7, 50000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 3, 20000 },
                                        { Skill.HUYT_SAO, 7, 20000 }, },
                        _5_PHUT);

        public static final BossData FROST3 = new BossData(
                        "FROST 3 (nro băng)",
                        ConstPlayer.TRAI_DAT,
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp
                        5700330d,
                        2800d,
                        new double[][] { { 100_000_000_000d } },
                        new short[] { 499, 500, 501 }, // ngoại hình
                        new short[] { 105, 106, 107, 108, 109, 110 }, // map
                        new int[][] { // skill
                                        { Skill.MASENKO, 7, 10 },
                                        { Skill.KAMEJOKO, 7, 10 },
                                        { Skill.ANTOMIC, 7, 10 },
                                        { Skill.TAI_TAO_NANG_LUONG, 7, 60000 },
                                        { Skill.SOCOLA, 7, 10000 },
                                        { Skill.DRAGON, 7, 50 },
                                        { Skill.THAI_DUONG_HA_SAN, 7, 40000 },
                                        { Skill.THOI_MIEN, 7, 50000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 3, 20000 },
                                        { Skill.HUYT_SAO, 7, 20000 }, },
                        _5_PHUT);

        public static final BossData Bujin = new BossData(
                        "Bujin",
                        ConstPlayer.TRAI_DAT,
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp
                        8003300,
                        0,
                        new double[][] { { 15000000000l } },
                        new short[] { 341, 342, 343 }, // ngoại hình
                        new short[] { 27, 28, 29, 30 }, // map
                        new int[][] { // skill
                                        { Skill.MASENKO, 7, 10 },
                                        { Skill.KAMEJOKO, 7, 10 },
                                        { Skill.ANTOMIC, 7, 10 },
                                        { Skill.DRAGON, 7, 50 },
                                        { Skill.TAI_TAO_NANG_LUONG, 3, 20000 },
                                        { Skill.HUYT_SAO, 7, 20000 }, },
                        _5_PHUT);

        public static final BossData Kogu = new BossData(
                        "Kogu",
                        ConstPlayer.TRAI_DAT,
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp
                        8033000,
                        0,
                        new double[][] { { 15000000000l } },
                        new short[] { 329, 330, 331 }, // ngoại hình
                        new short[] { 27, 28, 29, 30 }, // map
                        new int[][] { // skill
                                        { Skill.MASENKO, 7, 10 },
                                        { Skill.KAMEJOKO, 7, 10 },
                                        { Skill.ANTOMIC, 7, 10 },
                                        { Skill.DRAGON, 7, 50 },
                                        { Skill.TAI_TAO_NANG_LUONG, 3, 20000 },
                                        { Skill.HUYT_SAO, 7, 20000 }, },
                        _5_PHUT);

        public static final BossData Zangya = new BossData(
                        "Zangya",
                        ConstPlayer.TRAI_DAT,
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp
                        8002200,
                        0,
                        new double[][] { { 1500000000l } },
                        new short[] { 332, 333, 334 }, // ngoại hình
                        new short[] { 27, 28, 29, 30 }, // map
                        new int[][] { // skill
                                        { Skill.MASENKO, 7, 10 },
                                        { Skill.KAMEJOKO, 7, 10 },
                                        { Skill.ANTOMIC, 7, 10 },
                                        { Skill.DRAGON, 7, 50 },
                                        { Skill.TAI_TAO_NANG_LUONG, 3, 20000 },
                                        { Skill.HUYT_SAO, 7, 20000 }, },
                        _5_PHUT);

        public static final BossData Bido = new BossData(
                        "Bido",
                        ConstPlayer.TRAI_DAT,
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp
                        8022000,
                        0,
                        new double[][] { { 1500000000l } },
                        new short[] { 335, 336, 337 }, // ngoại hình
                        new short[] { 27, 28, 29, 30 }, // map
                        new int[][] { // skill
                                        { Skill.MASENKO, 7, 10 },
                                        { Skill.KAMEJOKO, 7, 10 },
                                        { Skill.ANTOMIC, 7, 10 },
                                        { Skill.DRAGON, 7, 50 },
                                        { Skill.TAI_TAO_NANG_LUONG, 3, 20000 },
                                        { Skill.HUYT_SAO, 7, 20000 }, },
                        _5_PHUT);

        public static final BossData Bojack = new BossData(
                        "Bojack",
                        ConstPlayer.TRAI_DAT,
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp
                        8002200,
                        0,
                        new double[][] { { 15000000000l } },
                        new short[] { 323, 324, 325 }, // ngoại hình
                        new short[] { 27, 28, 29, 30 }, // map
                        new int[][] { // skill
                                        { Skill.MASENKO, 7, 10 },
                                        { Skill.KAMEJOKO, 7, 10 },
                                        { Skill.ANTOMIC, 7, 10 },
                                        { Skill.DRAGON, 7, 50 },
                                        { Skill.TAI_TAO_NANG_LUONG, 3, 20000 },
                                        { Skill.HUYT_SAO, 7, 20000 }, },
                        _5_PHUT);

        public static final BossData SiêuBojack = new BossData(
                        "Siêu Bojack",
                        ConstPlayer.TRAI_DAT,
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp
                        8002200,
                        0,
                        new double[][] { { 1500000000l } },
                        new short[] { 326, 327, 328 }, // ngoại hình
                        new short[] { 27, 28, 29, 30 }, // map
                        new int[][] { // skill
                                        { Skill.MASENKO, 7, 10 },
                                        { Skill.KAMEJOKO, 7, 10 },
                                        { Skill.ANTOMIC, 7, 10 },
                                        { Skill.DRAGON, 7, 50 },
                                        { Skill.TAI_TAO_NANG_LUONG, 3, 20000 },
                                        { Skill.HUYT_SAO, 7, 20000 }, },
                        _5_PHUT);

        public static final BossData BOSSCOLOVANG = new BossData(
                        "Cooler Vàng",
                        ConstPlayer.TRAI_DAT,
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp
                        10_989_222l,
                        5000L,
                        new double[][] { { 500_999_900_000d } },
                        new short[] { 709, 710, 711, 99, 99, 99 }, // ngoại hình
                        new short[] { 105, 106, 107, 108, 109, 110 }, // map
                        new int[][] { // skill
                                        { Skill.MASENKO, 7, 10 },
                                        { Skill.KAMEJOKO, 7, 10 },
                                        { Skill.ANTOMIC, 7, 10 },
                                        { Skill.TAI_TAO_NANG_LUONG, 7, 60000 },
                                        { Skill.SOCOLA, 7, 10000 },
                                        { Skill.DRAGON, 7, 50 },
                                        { Skill.THAI_DUONG_HA_SAN, 7, 40000 },
                                        { Skill.THOI_MIEN, 7, 50000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 3, 20000 },
                                        { Skill.HUYT_SAO, 7, 20000 }, },
                        _5_PHUT);
        public static final BossData SO1 = new BossData(
                        "Số 1", // name
                        ConstPlayer.XAYDA, // gender
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp

                        100000l, // dame
                        5000l,
                        new double[][] { { 50000000000l } }, // hp
                        new short[] { 168, 169, 170, 102, 22 }, // outfit
                        new short[] { 77, 79, 80, 81, 82, 83, 33, 34 }, // map join
                        new int[][] { // skill
                                        { Skill.MASENKO, 7, 10 },
                                        { Skill.KAMEJOKO, 7, 10 },
                                        { Skill.ANTOMIC, 7, 10 },
                                        { Skill.TAI_TAO_NANG_LUONG, 7, 60000 },
                                        { Skill.SOCOLA, 7, 10000 },
                                        { Skill.DRAGON, 7, 50 },
                                        { Skill.THAI_DUONG_HA_SAN, 7, 40000 },
                                        { Skill.THOI_MIEN, 7, 50000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 3, 20000 },
                                        { Skill.HUYT_SAO, 7, 20000 }, },
                        _5_PHUT);
        public static final BossData SO2 = new BossData(
                        "Số 2",
                        ConstPlayer.XAYDA, // gender
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp
                        100000l, // dame
                        5000l,
                        new double[][] { { 50000000000l } }, // hp
                        new short[] { 174, 175, 176, 102, 22 }, // outfit
                        new short[] { 77, 79, 80, 81, 82, 83, 33, 34 }, // map join
                        new int[][] { // skill
                                        { Skill.DEMON, 3, 450 }, { Skill.DEMON, 6, 400 }, { Skill.DRAGON, 7, 650 },
                                        { Skill.DRAGON, 1, 500 }, { Skill.GALICK, 5, 480 },
                                        { Skill.KAMEJOKO, 7, 2000 }, { Skill.KAMEJOKO, 6, 1800 },
                                        { Skill.KAMEJOKO, 4, 1500 }, { Skill.KAMEJOKO, 2, 1000 },
                                        { Skill.ANTOMIC, 3, 1200 }, { Skill.ANTOMIC, 5, 1700 },
                                        { Skill.ANTOMIC, 7, 2000 },
                                        { Skill.MASENKO, 1, 800 }, { Skill.MASENKO, 5, 1300 },
                                        { Skill.MASENKO, 6, 1500 },
                                        { Skill.TAI_TAO_NANG_LUONG, 1, 15000 }, { Skill.TAI_TAO_NANG_LUONG, 3, 25000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 7, 50000 }
                        },
                        _5_PHUT // số giây nghỉ
        );
        public static final BossData SO3 = new BossData(
                        "Số 3",
                        ConstPlayer.XAYDA, // gender
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp

                        100000l, // dame
                        500l,
                        new double[][] { { 50000000000l } }, // hp
                        new short[] { 171, 172, 173, 102, 22 }, // outfit
                        new short[] { 77, 79, 80, 81, 82, 83, 33, 34 }, // map join
                        new int[][] { // skill
                                        { Skill.MASENKO, 7, 10 },
                                        { Skill.KAMEJOKO, 7, 10 },
                                        { Skill.ANTOMIC, 7, 10 },
                                        { Skill.TAI_TAO_NANG_LUONG, 7, 60000 },
                                        { Skill.SOCOLA, 7, 10000 },
                                        { Skill.DRAGON, 7, 50 },
                                        { Skill.THAI_DUONG_HA_SAN, 7, 40000 },
                                        { Skill.THOI_MIEN, 7, 50000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 3, 20000 },
                                        { Skill.HUYT_SAO, 7, 20000 }, },
                        _5_PHUT);

        public static final BossData SO4 = new BossData(
                        "Số 4",
                        ConstPlayer.XAYDA, // gender
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp

                        100000l, // dame
                        50000l,
                        new double[][] { { 50000000000l } }, // hp
                        new short[] { 177, 178, 179, 102, 22 }, // outfit
                        new short[] { 77, 79, 80, 81, 82, 83, 33, 34 }, // map join
                        new int[][] { // skill
                                        { Skill.MASENKO, 7, 10 },
                                        { Skill.KAMEJOKO, 7, 10 },
                                        { Skill.ANTOMIC, 7, 10 },
                                        { Skill.TAI_TAO_NANG_LUONG, 7, 60000 },
                                        { Skill.SOCOLA, 7, 10000 },
                                        { Skill.DRAGON, 7, 50 },
                                        { Skill.THAI_DUONG_HA_SAN, 7, 40000 },
                                        { Skill.THOI_MIEN, 7, 50000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 3, 20000 },
                                        { Skill.HUYT_SAO, 7, 20000 }, },
                        _5_PHUT);

        public static final BossData TDTRUONG = new BossData(
                        "Tiểu Đội Trưởng",
                        ConstPlayer.XAYDA, // gender
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp

                        200000l, // dame
                        5000l,
                        new double[][] { { 900000000000l } }, // hp
                        new short[] { 180, 181, 182, 102, 22 }, // outfit
                        new short[] { 77, 79, 80, 81, 82, 83, 33, 34 }, // map join
                        new int[][] { // skill
                                        { Skill.MASENKO, 7, 10 },
                                        { Skill.KAMEJOKO, 7, 10 },
                                        { Skill.ANTOMIC, 7, 10 },
                                        { Skill.TAI_TAO_NANG_LUONG, 7, 60000 },
                                        { Skill.SOCOLA, 7, 10000 },
                                        { Skill.DRAGON, 7, 50 },
                                        { Skill.THAI_DUONG_HA_SAN, 7, 40000 },
                                        { Skill.THOI_MIEN, 7, 50000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 3, 20000 },
                                        { Skill.HUYT_SAO, 7, 20000 }, },
                        _5_PHUT);

        public static final BossData THANHUYDIET = new BossData(
                        "Thần Berrus [Đồ 50 Sao]", // name
                        ConstPlayer.XAYDA, // gender
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp

                        2847700d, // dame
                        18200d, // dame
                        new double[][] { { 9_925_000_000d } }, // hp
                        new short[] { 508, 509, 510 }, // outfit
                        new short[] { 200, 201, 202 }, // map join
                        new int[][] { // skill
                                        { Skill.MASENKO, 7, 10 },
                                        { Skill.KAMEJOKO, 7, 10 },
                                        { Skill.ANTOMIC, 7, 10 },
                                        { Skill.TAI_TAO_NANG_LUONG, 7, 40000 },
                                        { Skill.SOCOLA, 7, 10000 },
                                        { Skill.DRAGON, 7, 50 },
                                        { Skill.THAI_DUONG_HA_SAN, 7, 40000 },
                                        { Skill.THOI_MIEN, 7, 50000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 3, 20000 },
                                        { Skill.HUYT_SAO, 7, 20000 }, },
                        _5_PHUT);

        public static final BossData THIENSUWISH = new BossData(
                        "Thiên sứ Whis [Đồ 60 Sao]", // name
                        ConstPlayer.XAYDA, // gender
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp

                        18231300d, // dame
                        8192300d, // dame
                        new double[][] { { 99_925_000_000d } }, // hp
                        new short[] { 505, 506, 507 }, // outfit
                        new short[] { 200, 201, 202 }, // map join
                        new int[][] { // skill
                                        { Skill.MASENKO, 7, 10 },
                                        { Skill.KAMEJOKO, 7, 10 },
                                        { Skill.ANTOMIC, 7, 10 },
                                        { Skill.TAI_TAO_NANG_LUONG, 7, 60000 },
                                        { Skill.SOCOLA, 7, 10000 },
                                        { Skill.DRAGON, 7, 50 },
                                        { Skill.THAI_DUONG_HA_SAN, 7, 40000 },
                                        { Skill.THOI_MIEN, 7, 50000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 3, 20000 },
                                        { Skill.HUYT_SAO, 7, 20000 }, },
                        _5_PHUT);

        public static final BossData champa = new BossData(
                        "Thần Hủy Diệt Champa [Đá Ảo Hóa]", // name
                        ConstPlayer.XAYDA, // gender
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp

                        2847000d, // dame
                        9810d, // dame
                        new double[][] { { 99_999_999_999_999d } }, // hp
                        new short[] { 511, 512, 513 }, // outfit
                        new short[] { 200, 201, 202 }, // map join
                        new int[][] { // skill
                                        { Skill.MASENKO, 7, 10 },
                                        { Skill.KAMEJOKO, 7, 10 },
                                        { Skill.ANTOMIC, 7, 10 },
                                        { Skill.TAI_TAO_NANG_LUONG, 7, 60000 },
                                        { Skill.SOCOLA, 7, 10000 },
                                        { Skill.DRAGON, 7, 50 },
                                        { Skill.THAI_DUONG_HA_SAN, 7, 40000 },
                                        { Skill.THOI_MIEN, 7, 50000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 3, 20000 },
                                        { Skill.HUYT_SAO, 7, 20000 }, },
                        _5_PHUT);

        public static final BossData panchi = new BossData(
                        "Panchy [CT Ẩn 8 Dòng]", // name
                        ConstPlayer.XAYDA, // gender
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp

                        2000000d, // dame
                        20000d, // dame
                        new double[][] { { 99_999_999_999d } }, // hp
                        new short[] { 1835, 1836, 1837 }, // outfit
                        new short[] { 200, 201, 202 }, // map join
                        new int[][] { // skill
                                        { Skill.MASENKO, 7, 10 },
                                        { Skill.KAMEJOKO, 7, 10 },
                                        { Skill.ANTOMIC, 7, 10 },
                                        { Skill.TAI_TAO_NANG_LUONG, 7, 60000 },
                                        { Skill.SOCOLA, 7, 10000 },
                                        { Skill.DRAGON, 7, 50 },
                                        { Skill.THAI_DUONG_HA_SAN, 7, 40000 },
                                        { Skill.THOI_MIEN, 7, 50000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 3, 20000 },
                                        { Skill.HUYT_SAO, 7, 20000 }, },
                        _5_PHUT);
        public static final BossData babyvegeta = new BossData(
                        "Baby Vegeta [CT Ẩn 10 Dòng]", // name
                        ConstPlayer.XAYDA, // gender
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp

                        20000000d, // dame
                        200d, // dame
                        new double[][] { { 99_999_999_999d } }, // hp
                        new short[] { 1838, 1839, 1840 }, // outfit
                        new short[] { 200, 201, 202 }, // map join
                        new int[][] { // skill
                                        { Skill.MASENKO, 7, 10 },
                                        { Skill.KAMEJOKO, 7, 10 },
                                        { Skill.ANTOMIC, 7, 10 },
                                        { Skill.TAI_TAO_NANG_LUONG, 7, 60000 },
                                        { Skill.SOCOLA, 7, 10000 },
                                        { Skill.DRAGON, 7, 50 },
                                        { Skill.THAI_DUONG_HA_SAN, 7, 40000 },
                                        { Skill.THOI_MIEN, 7, 50000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 3, 20000 },
                                        { Skill.HUYT_SAO, 7, 20000 }, },
                        _5_PHUT);

        public static final BossData superkhi = new BossData(
                        "Super Khỉ [CT Ẩn 12 Dòng]", // name
                        ConstPlayer.XAYDA, // gender
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp

                        20000000d, // dame
                        2000d, // dame
                        new double[][] { { 99_999_999_999d } }, // hp
                        new short[] { 1841, 1842, 1843 }, // outfit
                        new short[] { 200, 201, 202 }, // map join
                        new int[][] { // skill
                                        { Skill.MASENKO, 7, 10 },
                                        { Skill.KAMEJOKO, 7, 10 },
                                        { Skill.ANTOMIC, 7, 10 },
                                        { Skill.TAI_TAO_NANG_LUONG, 7, 60000 },
                                        { Skill.SOCOLA, 7, 10000 },
                                        { Skill.DRAGON, 7, 50 },
                                        { Skill.THAI_DUONG_HA_SAN, 7, 40000 },
                                        { Skill.THOI_MIEN, 7, 50000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 3, 20000 },
                                        { Skill.HUYT_SAO, 7, 20000 }, },
                        _5_PHUT);

        public static final BossData vasdos = new BossData(
                        "Thiên Sứ Vados[Đá ảo hóa]", // name
                        ConstPlayer.XAYDA, // gender
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp
                        100000d, // dame
                        1000d, // dame
                        new double[][] { { 1000000000d } }, // hp
                        new short[] { 530, 531, 532 }, // outfit
                        new short[] { 200, 201, 202 }, // map join
                        new int[][] { // skill
                                        { Skill.MASENKO, 7, 10 },
                                        { Skill.KAMEJOKO, 7, 10 },
                                        { Skill.ANTOMIC, 7, 10 },
                                        { Skill.TAI_TAO_NANG_LUONG, 7, 60000 },
                                        { Skill.SOCOLA, 7, 10000 },
                                        { Skill.DRAGON, 7, 50 },
                                        { Skill.THAI_DUONG_HA_SAN, 7, 40000 },
                                        { Skill.THOI_MIEN, 7, 50000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 3, 20000 },
                                        { Skill.HUYT_SAO, 7, 20000 }, },
                        _5_PHUT);

        public static final BossData BOSSVIP5 = new BossData(
                        "Thần Hủy Diệt Quitela[Đồ Hủy Diệt 50M]", // name
                        ConstPlayer.XAYDA, // gender
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp

                        1000000d, // dame
                        1000d, // dame
                        new double[][] { { 25_123_000_000d } }, // hp
                        new short[] { 1399, 1400, 1401 }, // outfit
                        new short[] { 200, 201, 202 }, // map join
                        new int[][] { // skill
                                        { Skill.MASENKO, 7, 10 },
                                        { Skill.KAMEJOKO, 7, 10 },
                                        { Skill.ANTOMIC, 7, 10 },
                                        { Skill.TAI_TAO_NANG_LUONG, 7, 60000 },
                                        { Skill.SOCOLA, 7, 10000 },
                                        { Skill.DRAGON, 7, 50 },
                                        { Skill.THAI_DUONG_HA_SAN, 7, 40000 },
                                        { Skill.THOI_MIEN, 7, 50000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 3, 20000 },
                                        { Skill.HUYT_SAO, 7, 20000 }, },
                        _5_PHUT);
        public static final BossData BOSSVIP6 = new BossData(
                        "Thiên Sứ Cognac[Đồ Hủy Diệt 50M]", // name
                        ConstPlayer.XAYDA, // gender
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp

                        1002200d, // dame
                        100d, // dame
                        new double[][] { { 95_000_000_000d } }, // hp
                        new short[] { 1476, 1477, 1478 }, // outfit
                        new short[] { 200, 201, 202 }, // map join
                        new int[][] { // skill
                                        { Skill.MASENKO, 7, 10 },
                                        { Skill.KAMEJOKO, 7, 10 },
                                        { Skill.ANTOMIC, 7, 10 },
                                        { Skill.TAI_TAO_NANG_LUONG, 7, 60000 },
                                        { Skill.SOCOLA, 7, 10000 },
                                        { Skill.DRAGON, 7, 50 },
                                        { Skill.THAI_DUONG_HA_SAN, 7, 40000 },
                                        { Skill.THOI_MIEN, 7, 50000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 3, 20000 },
                                        { Skill.HUYT_SAO, 7, 20000 }, },
                        _5_PHUT);

        public static final BossData BOSSVIP7 = new BossData(
                        "Người Yêu Cũ [Thú Cưỡi 500k%]", // name
                        ConstPlayer.XAYDA, // gender
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp

                        90000000d, // dame
                        9000d, // dame
                        new double[][] { { 999_000_000d } }, // hp
                        new short[] { 681, 682, 683 }, // outfit
                        new short[] { 122, 123, 124 }, // map join
                        new int[][] { // skill
                                        { Skill.MASENKO, 7, 10 },
                                        { Skill.KAMEJOKO, 7, 10 },
                                        { Skill.ANTOMIC, 7, 10 },
                                        { Skill.TAI_TAO_NANG_LUONG, 7, 60000 },
                                        { Skill.SOCOLA, 7, 10000 },
                                        { Skill.DRAGON, 7, 50 },
                                        { Skill.THAI_DUONG_HA_SAN, 7, 40000 },
                                        { Skill.THOI_MIEN, 7, 50000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 3, 20000 },
                                        { Skill.HUYT_SAO, 7, 20000 }, },
                        _5_PHUT);

        public static final BossData BOSSVIP8 = new BossData(
                        "Người Yêu Mới [Thú Cưỡi 500k%]", // name
                        ConstPlayer.XAYDA, // gender
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp

                        90000d, // dame
                        1d, // dame
                        new double[][] { { 5_000_999_999_999d } }, // hp
                        new short[] { 684, 685, 686 }, // outfit
                        new short[] { 122, 123, 124 }, // map join
                        new int[][] { // skill
                                        { Skill.MASENKO, 7, 10 },
                                        { Skill.KAMEJOKO, 7, 10 },
                                        { Skill.ANTOMIC, 7, 10 },
                                        { Skill.TAI_TAO_NANG_LUONG, 7, 60000 },
                                        { Skill.SOCOLA, 7, 10000 },
                                        { Skill.DRAGON, 7, 50 },
                                        { Skill.THAI_DUONG_HA_SAN, 7, 40000 },
                                        { Skill.THOI_MIEN, 7, 50000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 3, 20000 },
                                        { Skill.HUYT_SAO, 7, 20000 }, },
                        _5_PHUT);

        public static final BossData BOSSVIP9 = new BossData(
                        "Sơn Tinh [Đeo Lưng 1M%]", // name
                        ConstPlayer.XAYDA, // gender
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp

                        90999990d, // dame
                        90d, // dame
                        new double[][] { { 90_999_999_995d } }, // hp
                        new short[] { 314, 315, 316 }, // outfit
                        new short[] { 24, 25, 26 }, // map join
                        new int[][] { // skill
                                        { Skill.MASENKO, 7, 10 },
                                        { Skill.KAMEJOKO, 7, 10 },
                                        { Skill.ANTOMIC, 7, 10 },
                                        { Skill.TAI_TAO_NANG_LUONG, 7, 60000 },
                                        { Skill.SOCOLA, 7, 10000 },
                                        { Skill.DRAGON, 7, 50 },
                                        { Skill.THAI_DUONG_HA_SAN, 7, 40000 },
                                        { Skill.THOI_MIEN, 7, 50000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 3, 20000 },
                                        { Skill.HUYT_SAO, 7, 20000 }, },
                        _5_PHUT);
        public static final BossData BOSSVIP10 = new BossData(
                        "Thủy Tinh [Đeo Lưng 1M%]", // name
                        ConstPlayer.XAYDA, // gender
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp

                        900009999990d, // dame
                        900000d, // dame
                        new double[][] { { 9000_000_999_999d } }, // hp
                        new short[] { 311, 312, 313 }, // outfit
                        new short[] { 24, 25, 26 }, // map join
                        new int[][] { // skill
                                        { Skill.MASENKO, 7, 10 },
                                        { Skill.KAMEJOKO, 7, 10 },
                                        { Skill.ANTOMIC, 7, 10 },
                                        { Skill.TAI_TAO_NANG_LUONG, 7, 60000 },
                                        { Skill.SOCOLA, 7, 10000 },
                                        { Skill.DRAGON, 7, 50 },
                                        { Skill.THAI_DUONG_HA_SAN, 7, 40000 },
                                        { Skill.THOI_MIEN, 7, 50000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 3, 20000 },
                                        { Skill.HUYT_SAO, 7, 20000 }, },
                        _5_PHUT);

        public static final BossData BOSSVIP11 = new BossData(
                        "Vegeta [Phù Sinh Tử Đệ 1M%]", // name
                        ConstPlayer.XAYDA, // gender
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp

                        9991820d, // dame
                        920d, // dame
                        new double[][] { { 90_999_999_995d } }, // hp
                        new short[] { 1408, 1409, 1410 }, // outfit
                        new short[] { 35, 36 }, // map join
                        new int[][] { // skill
                                        { Skill.MASENKO, 7, 10 },
                                        { Skill.KAMEJOKO, 7, 10 },
                                        { Skill.ANTOMIC, 7, 10 },
                                        { Skill.TAI_TAO_NANG_LUONG, 7, 60000 },
                                        { Skill.SOCOLA, 7, 10000 },
                                        { Skill.DRAGON, 7, 50 },
                                        { Skill.THAI_DUONG_HA_SAN, 7, 40000 },
                                        { Skill.THOI_MIEN, 7, 50000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 3, 20000 },
                                        { Skill.HUYT_SAO, 7, 20000 }, },
                        _5_PHUT);

        public static final BossData BOSSVIP12 = new BossData(
                        "Goku [Phù Sinh Tử Đệ 1M%]", // name
                        ConstPlayer.XAYDA, // gender
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp

                        127374d, // dame
                        21d, // dame
                        new double[][] { { 90_999_999_995d } }, // hp
                        new short[] { 1411, 1412, 1413 }, // outfit
                        new short[] { 35, 36 }, // map join
                        new int[][] { // skill
                                        { Skill.MASENKO, 7, 10 },
                                        { Skill.KAMEJOKO, 7, 10 },
                                        { Skill.ANTOMIC, 7, 10 },
                                        { Skill.TAI_TAO_NANG_LUONG, 7, 60000 },
                                        { Skill.SOCOLA, 7, 10000 },
                                        { Skill.DRAGON, 7, 50 },
                                        { Skill.THAI_DUONG_HA_SAN, 7, 40000 },
                                        { Skill.THOI_MIEN, 7, 50000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 3, 20000 },
                                        { Skill.HUYT_SAO, 7, 20000 }, },
                        _5_PHUT);
        public static final BossData BOSSVIP13 = new BossData(
                        "Truong [Phù Sinh Tử Đệ 1M%]", // name
                        ConstPlayer.XAYDA, // gender
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp

                        1273482174d, // dame
                        21d, // dame
                        new double[][] { { 90_999_999_995d } }, // hp
                        new short[] { 1580, 1581, 1582 }, // outfit
                        new short[] { 33 }, // map join
                        new int[][] { // skill
                                        { Skill.MASENKO, 7, 10 },
                                        { Skill.KAMEJOKO, 7, 10 },
                                        { Skill.ANTOMIC, 7, 10 },
                                        { Skill.TAI_TAO_NANG_LUONG, 7, 60000 },
                                        { Skill.SOCOLA, 7, 10000 },
                                        { Skill.DRAGON, 7, 50 },
                                        { Skill.THAI_DUONG_HA_SAN, 7, 40000 },
                                        { Skill.THOI_MIEN, 7, 50000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 3, 20000 },
                                        { Skill.HUYT_SAO, 7, 20000 }, },
                        _5_PHUT);

        public static final BossData BOSSVIP14 = new BossData(
                        "Goku SSJ [1 Tỷ VND]", // name
                        ConstPlayer.XAYDA, // gender
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp

                        9000000000L, // dame
                        10,
                        new double[][] { { 81923842382332l } }, // hp
                        new short[] { 1417, 1418, 1419 }, // outfit
                        new short[] { 131, 132, 133 }, // map join
                        new int[][] { // skill
                                        { Skill.MASENKO, 7, 10 },
                                        { Skill.KAMEJOKO, 7, 10 },
                                        { Skill.ANTOMIC, 7, 10 },
                                        { Skill.TAI_TAO_NANG_LUONG, 7, 60000 },
                                        { Skill.SOCOLA, 7, 10000 },
                                        { Skill.DRAGON, 7, 50 },
                                        { Skill.THAI_DUONG_HA_SAN, 7, 40000 },
                                        { Skill.THOI_MIEN, 7, 50000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 3, 20000 },
                                        { Skill.HUYT_SAO, 7, 20000 }, },
                        _5_PHUT);

        public static final BossData BOSSVIP15 = new BossData(
                        "Vegeta SSJ [10 Tỷ VND]", // name
                        ConstPlayer.XAYDA, // gender
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp

                        9000000000L, // dame
                        10,
                        new double[][] { { 1827482730112313l } }, // hp
                        new short[] { 1420, 1421, 1422 }, // outfit
                        new short[] { 131, 132, 133 }, // map join
                        new int[][] { // skill
                                        { Skill.MASENKO, 7, 10 },
                                        { Skill.KAMEJOKO, 7, 10 },
                                        { Skill.ANTOMIC, 7, 10 },
                                        { Skill.TAI_TAO_NANG_LUONG, 7, 60000 },
                                        { Skill.SOCOLA, 7, 10000 },
                                        { Skill.DRAGON, 7, 50 },
                                        { Skill.THAI_DUONG_HA_SAN, 7, 40000 },
                                        { Skill.THOI_MIEN, 7, 50000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 3, 20000 },
                                        { Skill.HUYT_SAO, 7, 20000 }, },
                        _5_PHUT);

        public static final BossData BOSSSKH1 = new BossData(
                        "Gohan [SKH THIÊN SỨ 1000%]", // name
                        ConstPlayer.XAYDA, // gender
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp

                        9000000L, // dame
                        0,
                        new double[][] { { 99900000000d } }, // hp
                        new short[] { 1053, 1054, 1055 }, // outfit
                        new short[] { 16, 8, 1, 2, 3 }, // map join
                        new int[][] { // skill
                                        { Skill.MASENKO, 7, 10 },
                                        { Skill.KAMEJOKO, 7, 10 },
                                        { Skill.ANTOMIC, 7, 10 },
                                        { Skill.TAI_TAO_NANG_LUONG, 7, 60000 },
                                        { Skill.SOCOLA, 7, 10000 },
                                        { Skill.DRAGON, 7, 50 },
                                        { Skill.THAI_DUONG_HA_SAN, 7, 40000 },
                                        { Skill.THOI_MIEN, 7, 50000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 3, 20000 },
                                        { Skill.HUYT_SAO, 7, 20000 }, },
                        _5_PHUT);

        public static final BossData BOSSSKH2 = new BossData(
                        "Biden [SKH THIÊN SỨ 1000%]", // name
                        ConstPlayer.XAYDA, // gender
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp

                        9000000000L, // dame
                        10,
                        new double[][] { { 9000000000000d } }, // hp
                        new short[] { 1056, 1057, 1058 }, // outfit
                        new short[] { 16, 8, 1, 2, 3 }, // map join
                        new int[][] { // skill
                                        { Skill.MASENKO, 7, 10 },
                                        { Skill.KAMEJOKO, 7, 10 },
                                        { Skill.ANTOMIC, 7, 10 },
                                        { Skill.TAI_TAO_NANG_LUONG, 7, 60000 },
                                        { Skill.SOCOLA, 7, 10000 },
                                        { Skill.DRAGON, 7, 50 },
                                        { Skill.THAI_DUONG_HA_SAN, 7, 40000 },
                                        { Skill.THOI_MIEN, 7, 50000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 3, 20000 },
                                        { Skill.HUYT_SAO, 7, 20000 }, },
                        _5_PHUT);

        public static final BossData BOSSSKH3 = new BossData(
                        "Cô Nương [SKH THIÊN SỨ 1000%]", // name
                        ConstPlayer.XAYDA, // gender
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp

                        90000000L, // dame
                        10,
                        new double[][] { { 990000000000l } }, // hp
                        new short[] { 1059, 1060, 1061 }, // outfit
                        new short[] { 16, 8, 1, 2, 3 }, // map join
                        new int[][] { // skill
                                        { Skill.MASENKO, 7, 10 },
                                        { Skill.KAMEJOKO, 7, 10 },
                                        { Skill.ANTOMIC, 7, 10 },
                                        { Skill.TAI_TAO_NANG_LUONG, 7, 60000 },
                                        { Skill.SOCOLA, 7, 10000 },
                                        { Skill.DRAGON, 7, 50 },
                                        { Skill.THAI_DUONG_HA_SAN, 7, 40000 },
                                        { Skill.THOI_MIEN, 7, 50000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 3, 20000 },
                                        { Skill.HUYT_SAO, 7, 20000 }, },
                        _5_PHUT);

        public static final BossData BOSSVIP21 = new BossData(
                        "Naruto", // name
                        ConstPlayer.XAYDA, // gender
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp

                        9990000D, // dame
                        10,
                        new double[][] { { 4977777777770d } }, // hp
                        new short[] { 1640, 1641, 1642 }, // outfit
                        new short[] { 146, 153, 154 }, // map join
                        new int[][] { // skill
                                        { Skill.MASENKO, 7, 10 },
                                        { Skill.KAMEJOKO, 7, 10 },
                                        { Skill.ANTOMIC, 7, 10 },
                                        { Skill.TAI_TAO_NANG_LUONG, 7, 60000 },
                                        { Skill.SOCOLA, 7, 10000 },
                                        { Skill.DRAGON, 7, 50 },
                                        { Skill.THAI_DUONG_HA_SAN, 7, 40000 },
                                        { Skill.THOI_MIEN, 7, 50000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 3, 20000 },
                                        { Skill.HUYT_SAO, 7, 20000 }, },
                        _5_PHUT);

        public static final BossData BOSSVIP22 = new BossData(
                        "Obito", // name
                        ConstPlayer.XAYDA, // gender
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp

                        99900000D, // dame
                        10,
                        new double[][] { { 497777777777777d } }, // hp
                        new short[] { 1643, 1644, 1645 }, // outfit
                        new short[] { 146, 153, 154 }, // map join
                        new int[][] { // skill
                                        { Skill.MASENKO, 7, 10 },
                                        { Skill.KAMEJOKO, 7, 10 },
                                        { Skill.ANTOMIC, 7, 10 },
                                        { Skill.TAI_TAO_NANG_LUONG, 7, 60000 },
                                        { Skill.SOCOLA, 7, 10000 },
                                        { Skill.DRAGON, 7, 50 },
                                        { Skill.THAI_DUONG_HA_SAN, 7, 40000 },
                                        { Skill.THOI_MIEN, 7, 50000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 3, 20000 },
                                        { Skill.HUYT_SAO, 7, 20000 }, },
                        _5_PHUT);

        public static final BossData BOSSVIP23 = new BossData(
                        "Hóa Thần [Thánh Thể 20M]", // name
                        ConstPlayer.XAYDA, // gender
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp

                        9990220D, // dame
                        10,
                        new double[][] { { 28748274298374d } }, // hp
                        new short[] { 1631, 1632, 1633 }, // outfit
                        new short[] { 146, 153, 154 }, // map join
                        new int[][] { // skill
                                        { Skill.MASENKO, 7, 10 },
                                        { Skill.KAMEJOKO, 7, 10 },
                                        { Skill.ANTOMIC, 7, 10 },
                                        { Skill.TAI_TAO_NANG_LUONG, 7, 60000 },
                                        { Skill.SOCOLA, 7, 10000 },
                                        { Skill.DRAGON, 7, 50 },
                                        { Skill.THAI_DUONG_HA_SAN, 7, 40000 },
                                        { Skill.THOI_MIEN, 7, 50000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 3, 20000 },
                                        { Skill.HUYT_SAO, 7, 20000 }, },
                        _5_PHUT);
        public static final BossData BOSSVIP24 = new BossData(
                        "Vegito Ultra [Thánh Thể 20M]", // name
                        ConstPlayer.XAYDA, // gender
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp

                        99900099D, // dame
                        10,
                        new double[][] { { 9577777777770d } }, // hp
                        new short[] { 1942, 1943, 1944 }, // outfit
                        new short[] { 146, 153, 154 }, // map join
                        new int[][] { // skill
                                        { Skill.MASENKO, 7, 10 },
                                        { Skill.KAMEJOKO, 7, 10 },
                                        { Skill.ANTOMIC, 7, 10 },
                                        { Skill.TAI_TAO_NANG_LUONG, 7, 60000 },
                                        { Skill.SOCOLA, 7, 10000 },
                                        { Skill.DRAGON, 7, 50 },
                                        { Skill.THAI_DUONG_HA_SAN, 7, 40000 },
                                        { Skill.THOI_MIEN, 7, 50000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 3, 20000 },
                                        { Skill.HUYT_SAO, 7, 20000 }, },
                        _5_PHUT);

        public static final BossData BOSSVIP25 = new BossData(
                        "Vegito [Thánh Thể 40M]", // name
                        ConstPlayer.XAYDA, // gender
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp

                        999273200D, // dame
                        10,
                        new double[][] { { 125777777777770d } }, // hp
                        new short[] { 1939, 1940, 1941 }, // outfit
                        new short[] { 146, 153, 154 }, // map join
                        new int[][] { // skill
                                        { Skill.MASENKO, 7, 10 },
                                        { Skill.KAMEJOKO, 7, 10 },
                                        { Skill.ANTOMIC, 7, 10 },
                                        { Skill.TAI_TAO_NANG_LUONG, 7, 60000 },
                                        { Skill.SOCOLA, 7, 10000 },
                                        { Skill.DRAGON, 7, 50 },
                                        { Skill.THAI_DUONG_HA_SAN, 7, 40000 },
                                        { Skill.THOI_MIEN, 7, 50000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 3, 20000 },
                                        { Skill.HUYT_SAO, 7, 20000 }, },
                        _5_PHUT);

        public static final BossData BOSSVEGETO = new BossData(
                        "Vegito Hợp Thể", // name
                        ConstPlayer.XAYDA, // gender
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp

                        199900D, // dame
                        19944d,
                        new double[][] { { 199999444444d } }, // hp
                        new short[] { 1939, 1940, 1941 }, // outfit
                        new short[] { 103, 104, 105 }, // map join
                        new int[][] { // skill
                                        { Skill.MASENKO, 7, 10 },
                                        { Skill.KAMEJOKO, 7, 10 },
                                        { Skill.ANTOMIC, 7, 10 },
                                        { Skill.TAI_TAO_NANG_LUONG, 7, 60000 },
                                        { Skill.SOCOLA, 7, 10000 },
                                        { Skill.DRAGON, 7, 50 },
                                        { Skill.THAI_DUONG_HA_SAN, 7, 40000 },
                                        { Skill.THOI_MIEN, 7, 50000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 3, 20000 },
                                        { Skill.HUYT_SAO, 7, 20000 }, },
                        _5_PHUT);
        public static final BossData BOSSVEGETOUNTRA = new BossData(
                        "Vegito Untral", // name
                        ConstPlayer.XAYDA, // gender
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp

                        79993600D, // dame
                        79999976d,
                        new double[][] { { 79999777776d } }, // hp
                        new short[] { 1942, 1943, 1944 }, // outfit
                        new short[] { 103, 104, 105 }, // map join
                        new int[][] { // skill
                                        { Skill.MASENKO, 7, 10 },
                                        { Skill.KAMEJOKO, 7, 10 },
                                        { Skill.ANTOMIC, 7, 10 },
                                        { Skill.TAI_TAO_NANG_LUONG, 7, 60000 },
                                        { Skill.SOCOLA, 7, 10000 },
                                        { Skill.DRAGON, 7, 50 },
                                        { Skill.THAI_DUONG_HA_SAN, 7, 40000 },
                                        { Skill.THOI_MIEN, 7, 50000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 3, 20000 },
                                        { Skill.HUYT_SAO, 7, 20000 }, },
                        _5_PHUT);

        public static final BossData HaoThienKhuyen = new BossData(
                        "Chó Săn Thảo Nguyên", // name
                        ConstPlayer.XAYDA, // gender
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp

                        2000000, // dame
                        0,
                        new double[][] { { 7500000000l } }, // hp
                        new short[] { 1945, 1946, 1947 }, // outfit
                        new short[] { 213 }, // map join
                        new int[][] { // skill
                                        { Skill.MASENKO, 7, 10 },
                                        { Skill.KAMEJOKO, 7, 10 },
                                        { Skill.ANTOMIC, 7, 10 },
                                        { Skill.TAI_TAO_NANG_LUONG, 7, 60000 },
                                        { Skill.SOCOLA, 7, 60000 },
                                        { Skill.DRAGON, 7, 50 },
                                        { Skill.THAI_DUONG_HA_SAN, 1, 40000 },
                                        // {Skill.THOI_MIEN, 7, 50000},
                                        { Skill.TAI_TAO_NANG_LUONG, 3, 30000 },
                                        { Skill.HUYT_SAO, 7, 20000 }, },
                        _5_PHUT);

        public static final BossData Panda = new BossData(
                        "Panda Thảo Nguyên", // name
                        ConstPlayer.XAYDA, // gender
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp

                        2500000, // dame
                        500000,
                        new double[][] { { 85000000000l } }, // hp
                        new short[] { 1607, 1608, 1609 }, // outfit
                        new short[] { 213 }, // map join
                        new int[][] { // skill
                                        { Skill.MASENKO, 7, 10 },
                                        { Skill.KAMEJOKO, 7, 10 },
                                        { Skill.ANTOMIC, 7, 10 },
                                        { Skill.TAI_TAO_NANG_LUONG, 7, 60000 },
                                        { Skill.SOCOLA, 7, 60000 },
                                        { Skill.DRAGON, 7, 50 },
                                        { Skill.THAI_DUONG_HA_SAN, 1, 40000 },
                                        // {Skill.THOI_MIEN, 7, 50000},
                                        { Skill.TAI_TAO_NANG_LUONG, 3, 30000 },
                                        { Skill.HUYT_SAO, 7, 20000 }, },
                        _5_PHUT);

        // --------------------------------------------------------------------------Boss
        // xên ginder
        // --------------------------------------------------------------------------Boss
        // TG
        public static final BossData BOSSTG = new BossData(
                        "Boss Thế Giới Rein", // name
                        ConstPlayer.XAYDA, // gender
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp
                        25000, // dame
                        500000,
                        new double[][] { { 2_000_000_000 } }, // hp
                        new short[] { 838, 839, 840 }, // outfit
                        new short[] { 165 }, // map join
                        new int[][] { // skill
                                        { Skill.MASENKO, 7, 10 },
                                        { Skill.KAMEJOKO, 7, 10 },
                                        { Skill.ANTOMIC, 7, 10 },
                                        { Skill.TAI_TAO_NANG_LUONG, 7, 60000 },
                                        { Skill.SOCOLA, 7, 10000 },
                                        { Skill.DRAGON, 7, 50 },
                                        { Skill.THAI_DUONG_HA_SAN, 7, 40000 },
                                        { Skill.THOI_MIEN, 7, 50000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 3, 20000 },
                                        { Skill.HUYT_SAO, 7, 20000 }, },
                        _5_PHUT);
        // --------------------------------------------------------------------------Boss
        // berus
        public static final BossData WHIS = new BossData(
                        "Thần Thiên Sứ", // name
                        ConstPlayer.XAYDA, // gender
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp
                        250000, // dame
                        500000,
                        new double[][] { { 150000000 } }, // hp
                        new short[] { 838, 839, 840 }, // outfit
                        new short[] { 201, 202, 200 }, // map join
                        new int[][] { // skill
                                        { Skill.MASENKO, 7, 10 },
                                        { Skill.KAMEJOKO, 7, 10 },
                                        { Skill.ANTOMIC, 7, 10 },
                                        { Skill.TAI_TAO_NANG_LUONG, 7, 60000 },
                                        { Skill.SOCOLA, 7, 10000 },
                                        { Skill.DRAGON, 7, 50 },
                                        { Skill.THAI_DUONG_HA_SAN, 7, 40000 },
                                        { Skill.THOI_MIEN, 7, 50000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 3, 20000 },
                                        { Skill.HUYT_SAO, 7, 20000 }, },
                        _5_PHUT);

        public static final BossData BILL = new BossData(
                        "Thần Hủy Diệt", // name
                        ConstPlayer.XAYDA, // gender
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp
                        300000, // dame
                        66,
                        new double[][] { { 2_000_000_000 } }, // hp
                        new short[] { 508, 509, 510 }, // outfit
                        new short[] { 201, 202, 200 }, // map join
                        new int[][] { // skill
                                        { Skill.DRAGON, 1, 1000 }, { Skill.DRAGON, 2, 2000 }, { Skill.DRAGON, 3, 3000 },
                                        { Skill.DRAGON, 7, 7000 },
                                        { Skill.ANTOMIC, 1, 1000 }, { Skill.ANTOMIC, 2, 1200 },
                                        { Skill.ANTOMIC, 4, 1500 }, { Skill.ANTOMIC, 5, 1700 },
                                        { Skill.MASENKO, 1, 1000 }, { Skill.MASENKO, 2, 1200 },
                                        { Skill.MASENKO, 4, 1500 }, { Skill.MASENKO, 5, 1700 },
                                        { Skill.GALICK, 1, 1000 }
                        },
                        _5_PHUT, true);

        // --------------------------------------------------------------------------Boss
        // CHILLED
        public static final BossData mabudhvt = new BossData(
                        "Ma bư", // name
                        ConstPlayer.XAYDA, // gender
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp
                        300000, // dame
                        5000,
                        new double[][] { { 500_000_000 } }, // hp
                        new short[] { 297, 298, 299 }, // outfit
                        new short[] { 52 }, // map join
                        new int[][] { // skill
                                        { Skill.DRAGON, 1, 1000 }, { Skill.DRAGON, 2, 2000 }, { Skill.DRAGON, 3, 3000 },
                                        { Skill.DRAGON, 7, 7000 },
                                        { Skill.ANTOMIC, 1, 1000 }, { Skill.ANTOMIC, 2, 1200 },
                                        { Skill.ANTOMIC, 4, 1500 }, { Skill.ANTOMIC, 5, 1700 },
                                        { Skill.MASENKO, 1, 1000 }, { Skill.MASENKO, 2, 1200 },
                                        { Skill.MASENKO, 4, 1500 }, { Skill.MASENKO, 5, 1700 },
                                        { Skill.GALICK, 1, 1000 }
                        },
                        _5_PHUT);

        public static final BossData SUPERBLACKGOKU = new BossData(
                        "Super Black Goku %1", // name
                        ConstPlayer.XAYDA, // gender
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp
                        800000, // dame
                        5000,
                        new double[][] { { 2_000_000_000 } }, // hp
                        new short[] { 553, 551, 552 }, // outfit
                        new short[] { 92, 93, 94 }, // map join
                        new int[][] { // skill
                                        { Skill.DEMON, 3, 450 }, { Skill.DEMON, 6, 400 }, { Skill.DRAGON, 7, 650 },
                                        { Skill.DRAGON, 1, 500 }, { Skill.GALICK, 5, 480 },
                                        { Skill.KAMEJOKO, 7, 2000 }, { Skill.KAMEJOKO, 6, 1800 },
                                        { Skill.KAMEJOKO, 4, 1500 }, { Skill.KAMEJOKO, 2, 1000 },
                                        { Skill.ANTOMIC, 3, 1200 }, { Skill.ANTOMIC, 5, 1700 },
                                        { Skill.ANTOMIC, 7, 2000 },
                                        { Skill.MASENKO, 1, 800 }, { Skill.MASENKO, 5, 1300 },
                                        { Skill.MASENKO, 6, 1500 },
                                        { Skill.TAI_TAO_NANG_LUONG, 1, 5000 }, { Skill.TAI_TAO_NANG_LUONG, 3, 10000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 5, 25000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 6, 30000 }, { Skill.TAI_TAO_NANG_LUONG, 7, 50000 }
                        },
                        _5_PHUT);

        public static final BossData HOA_HONG = BossData.builder()
                        .name("Hoa Hồng")
                        .gender(ConstPlayer.TRAI_DAT)
                        .typeDame(Boss.DAME_NORMAL)
                        .typeHp(Boss.HP_NORMAL)
                        .dame(0)
                        .hp(new double[][] { { 100 } })
                        .outfit(new short[] { 706, 707, 708 })
                        .mapJoin(new short[] {})
                        .skillTemp(new int[][] {})
                        .secondsRest(_5_PHUT)
                        .build();

        public static final BossData SANTA_CLAUS = BossData.builder()
                        .name("Ông già Noen")
                        .gender(ConstPlayer.TRAI_DAT)
                        .typeDame(Boss.DAME_NORMAL)
                        .typeHp(Boss.HP_NORMAL)
                        .dame(0)
                        .hp(new double[][] { { 500000 } })
                        .outfit(new short[] { 657, 658, 659 })
                        .mapJoin(new short[] {})
                        .skillTemp(new int[][] {})
                        .secondsRest(_5_PHUT)
                        .build();

        public static final BossData QILIN = BossData.builder()
                        .name("Lân con")
                        .gender(ConstPlayer.TRAI_DAT)
                        .typeDame(Boss.DAME_NORMAL)
                        .typeHp(Boss.HP_NORMAL)
                        .dame(0)
                        .hp(new double[][] { { 5000000 } })
                        .outfit(new short[] { 763, 764, 765 })
                        .mapJoin(new short[] {})
                        .skillTemp(new int[][] {})
                        .secondsRest(_5_PHUT)
                        .build();

        public static final BossData MABU_MAP = BossData.builder()
                        .name("Mabư")
                        .gender(ConstPlayer.TRAI_DAT)
                        .typeDame(Boss.DAME_NORMAL)
                        .typeHp(Boss.HP_NORMAL)
                        .dame(750000)
                        .hp(new double[][] { { 2_000_000_000 } })
                        .outfit(new short[] { 297, 298, 299 })
                        .mapJoin(new short[] {})
                        .skillTemp(new int[][] {
                                        { Skill.DEMON, 3, 450 }, { Skill.DEMON, 6, 400 }, { Skill.DRAGON, 7, 650 },
                                        { Skill.DRAGON, 1, 500 }, { Skill.GALICK, 5, 480 },
                                        { Skill.KAMEJOKO, 7, 2000 }, { Skill.KAMEJOKO, 6, 1800 },
                                        { Skill.KAMEJOKO, 4, 1500 }, { Skill.KAMEJOKO, 2, 1000 },
                                        { Skill.ANTOMIC, 3, 1200 }, { Skill.ANTOMIC, 5, 1700 },
                                        { Skill.ANTOMIC, 7, 2000 },
                                        { Skill.MASENKO, 1, 800 }, { Skill.MASENKO, 5, 1300 },
                                        { Skill.MASENKO, 6, 1500 },
                                        { Skill.TAI_TAO_NANG_LUONG, 1, 5000 }, { Skill.TAI_TAO_NANG_LUONG, 3, 10000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 5, 25000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 6, 30000 }, { Skill.TAI_TAO_NANG_LUONG, 7, 50000 }
                        })
                        .secondsRest(_5_PHUT)
                        .build();

        public static final BossData MABU_MAP2 = BossData.builder()
                        .name("Bư Mập")
                        .gender(ConstPlayer.TRAI_DAT)
                        .typeDame(Boss.DAME_NORMAL)
                        .typeHp(Boss.HP_NORMAL)
                        .dame(750000)
                        .hp(new double[][] { { 100000000 } })
                        .outfit(new short[] { 297, 298, 299 })
                        .mapJoin(new short[] {})
                        .skillTemp(new int[][] {
                                        { Skill.DEMON, 3, 450 }, { Skill.DRAGON, 1, 500 }, { Skill.GALICK, 5, 480 },
                                        { Skill.KAMEJOKO, 7, 2000 }, { Skill.KAMEJOKO, 6, 1800 },
                                        { Skill.KAMEJOKO, 4, 1500 }, { Skill.KAMEJOKO, 2, 1000 },
                                        { Skill.ANTOMIC, 3, 1200 },
                                        { Skill.TAI_TAO_NANG_LUONG, 1, 25000 }
                        })
                        .secondsRest(_5_PHUT)
                        .build();

        public static final BossData SUPER_BU = BossData.builder()
                        .name("Super Bư")
                        .gender(ConstPlayer.TRAI_DAT)
                        .typeDame(Boss.DAME_NORMAL)
                        .typeHp(Boss.HP_NORMAL)
                        .dame(1200000)
                        .hp(new double[][] { { 200000000 } })
                        .outfit(new short[] { 427, 428, 429 })
                        .mapJoin(new short[] {})
                        .skillTemp(new int[][] {
                                        { Skill.DEMON, 3, 450 }, { Skill.DRAGON, 1, 500 }, { Skill.GALICK, 5, 480 },
                                        { Skill.KAMEJOKO, 7, 2000 }, { Skill.KAMEJOKO, 6, 1800 },
                                        { Skill.KAMEJOKO, 4, 1500 }, { Skill.KAMEJOKO, 2, 1000 },
                                        { Skill.ANTOMIC, 3, 1200 },
                                        { Skill.TAI_TAO_NANG_LUONG, 1, 25000 }
                        })
                        .secondsRest(_5_PHUT)
                        .build();

        public static final BossData KID_BU = BossData.builder()
                        .name("Kid Bư")
                        .gender(ConstPlayer.TRAI_DAT)
                        .typeDame(Boss.DAME_NORMAL)
                        .typeHp(Boss.HP_NORMAL)
                        .dame(1200000)
                        .hp(new double[][] { { 300000000 } })
                        .outfit(new short[] { 439, 440, 441 })
                        .mapJoin(new short[] {})
                        .skillTemp(new int[][] {
                                        { Skill.DEMON, 3, 450 }, { Skill.DRAGON, 1, 500 }, { Skill.GALICK, 5, 480 },
                                        { Skill.KAMEJOKO, 7, 2000 }, { Skill.KAMEJOKO, 6, 1800 },
                                        { Skill.KAMEJOKO, 4, 1500 }, { Skill.KAMEJOKO, 2, 1000 },
                                        { Skill.ANTOMIC, 3, 1200 },
                                        { Skill.TAI_TAO_NANG_LUONG, 1, 25000 }
                        })
                        .secondsRest(_5_PHUT)
                        .build();

        public static final BossData BU_TENK = BossData.builder()
                        .name("Bư Tênk")
                        .gender(ConstPlayer.TRAI_DAT)
                        .typeDame(Boss.DAME_NORMAL)
                        .typeHp(Boss.HP_NORMAL)
                        .dame(1200000)
                        .hp(new double[][] { { 400000000 } })
                        .outfit(new short[] { 439, 440, 441 })
                        .mapJoin(new short[] {})
                        .skillTemp(new int[][] {
                                        { Skill.DEMON, 3, 450 }, { Skill.DRAGON, 1, 500 }, { Skill.GALICK, 5, 480 },
                                        { Skill.KAMEJOKO, 7, 2000 }, { Skill.KAMEJOKO, 6, 1800 },
                                        { Skill.KAMEJOKO, 4, 1500 }, { Skill.KAMEJOKO, 2, 1000 },
                                        { Skill.ANTOMIC, 3, 1200 },
                                        { Skill.TAI_TAO_NANG_LUONG, 1, 25000 }
                        })
                        .secondsRest(_5_PHUT)
                        .build();

        public static final BossData BU_HAN = BossData.builder()
                        .name("Bư Han")
                        .gender(ConstPlayer.TRAI_DAT)
                        .typeDame(Boss.DAME_NORMAL)
                        .typeHp(Boss.HP_NORMAL)
                        .dame(1200000)
                        .hp(new double[][] { { 800000000 } })
                        .outfit(new short[] { 427, 428, 429 })
                        .mapJoin(new short[] {})
                        .skillTemp(new int[][] {
                                        { Skill.DEMON, 3, 450 }, { Skill.DRAGON, 1, 500 }, { Skill.GALICK, 5, 480 },
                                        { Skill.KAMEJOKO, 7, 2000 }, { Skill.KAMEJOKO, 6, 1800 },
                                        { Skill.KAMEJOKO, 4, 1500 }, { Skill.KAMEJOKO, 2, 1000 },
                                        { Skill.ANTOMIC, 3, 1200 },
                                        { Skill.QUA_CAU_KENH_KHI, 7, 1200 },
                                        { Skill.TAI_TAO_NANG_LUONG, 1, 25000 }
                        })
                        .secondsRest(_5_PHUT)
                        .build();

        public static final BossData DRABULA_TANG1 = BossData.builder()
                        .name("Drabula")
                        .gender(ConstPlayer.TRAI_DAT)
                        .typeDame(Boss.DAME_NORMAL)
                        .typeHp(Boss.HP_NORMAL)
                        .dame(250000)
                        .hp(new double[][] { { 250000000 } })
                        .outfit(new short[] { 418, 419, 420 })
                        .mapJoin(new short[] { 114 })
                        .skillTemp(new int[][] {
                                        { Skill.DEMON, 3, 450 }, { Skill.DEMON, 6, 400 }, { Skill.DRAGON, 7, 650 },
                                        { Skill.DRAGON, 1, 500 }, { Skill.GALICK, 5, 480 },
                                        { Skill.KAMEJOKO, 7, 2000 }, { Skill.KAMEJOKO, 6, 1800 },
                                        { Skill.KAMEJOKO, 4, 1500 }, { Skill.KAMEJOKO, 2, 1000 },
                                        { Skill.ANTOMIC, 3, 1200 }, { Skill.ANTOMIC, 5, 1700 },
                                        { Skill.ANTOMIC, 7, 2000 },
                                        { Skill.MASENKO, 1, 800 }, { Skill.MASENKO, 5, 1300 },
                                        { Skill.MASENKO, 6, 1500 },
                                        { Skill.TAI_TAO_NANG_LUONG, 1, 5000 }, { Skill.TAI_TAO_NANG_LUONG, 3, 10000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 5, 25000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 6, 30000 }, { Skill.TAI_TAO_NANG_LUONG, 7, 50000 }
                        })
                        .secondsRest(_5_PHUT)
                        .build();

        public static final BossData DRABULA_TANG5 = BossData.builder()
                        .name("Drabula")
                        .gender(ConstPlayer.TRAI_DAT)
                        .typeDame(Boss.DAME_NORMAL)
                        .typeHp(Boss.HP_NORMAL)
                        .dame(500000)
                        .hp(new double[][] { { 500000000 } })
                        .outfit(new short[] { 418, 419, 420 })
                        .mapJoin(new short[] { 119 })
                        .skillTemp(new int[][] {
                                        { Skill.DEMON, 3, 450 }, { Skill.DEMON, 6, 400 }, { Skill.DRAGON, 7, 650 },
                                        { Skill.DRAGON, 1, 500 }, { Skill.GALICK, 5, 480 },
                                        { Skill.KAMEJOKO, 7, 2000 }, { Skill.KAMEJOKO, 6, 1800 },
                                        { Skill.KAMEJOKO, 4, 1500 }, { Skill.KAMEJOKO, 2, 1000 },
                                        { Skill.ANTOMIC, 3, 1200 }, { Skill.ANTOMIC, 5, 1700 },
                                        { Skill.ANTOMIC, 7, 2000 },
                                        { Skill.MASENKO, 1, 800 }, { Skill.MASENKO, 5, 1300 },
                                        { Skill.MASENKO, 6, 1500 },
                                        { Skill.TAI_TAO_NANG_LUONG, 1, 5000 }, { Skill.TAI_TAO_NANG_LUONG, 3, 10000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 5, 25000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 6, 30000 }, { Skill.TAI_TAO_NANG_LUONG, 7, 50000 }
                        })
                        .secondsRest(_5_PHUT)
                        .build();

        public static final BossData DRABULA_TANG6 = BossData.builder()
                        .name("Drabula")
                        .gender(ConstPlayer.TRAI_DAT)
                        .typeDame(Boss.DAME_NORMAL)
                        .typeHp(Boss.HP_NORMAL)
                        .dame(500000)
                        .hp(new double[][] { { 1000000000 } })
                        .outfit(new short[] { 418, 419, 420 })
                        .mapJoin(new short[] { 120 })
                        .skillTemp(new int[][] {
                                        { Skill.DEMON, 3, 450 }, { Skill.DEMON, 6, 400 }, { Skill.DRAGON, 7, 650 },
                                        { Skill.DRAGON, 1, 500 }, { Skill.GALICK, 5, 480 },
                                        { Skill.KAMEJOKO, 7, 2000 }, { Skill.KAMEJOKO, 6, 1800 },
                                        { Skill.KAMEJOKO, 4, 1500 }, { Skill.KAMEJOKO, 2, 1000 },
                                        { Skill.ANTOMIC, 3, 1200 }, { Skill.ANTOMIC, 5, 1700 },
                                        { Skill.ANTOMIC, 7, 2000 },
                                        { Skill.MASENKO, 1, 800 }, { Skill.MASENKO, 5, 1300 },
                                        { Skill.MASENKO, 6, 1500 },
                                        { Skill.TAI_TAO_NANG_LUONG, 1, 5000 }, { Skill.TAI_TAO_NANG_LUONG, 3, 10000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 5, 25000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 6, 30000 }, { Skill.TAI_TAO_NANG_LUONG, 7, 50000 }
                        })
                        .secondsRest(_5_PHUT)
                        .build();

        public static final BossData BUIBUI_TANG2 = BossData.builder()
                        .name("BuiBui")
                        .gender(ConstPlayer.TRAI_DAT)
                        .typeDame(Boss.DAME_NORMAL)
                        .typeHp(Boss.HP_NORMAL)
                        .dame(250000)
                        .hp(new double[][] { { 500000000 } })
                        .outfit(new short[] { 451, 452, 453 })
                        .mapJoin(new short[] { 115 })
                        .skillTemp(new int[][] {
                                        { Skill.DEMON, 3, 450 }, { Skill.DEMON, 6, 400 }, { Skill.DRAGON, 7, 650 },
                                        { Skill.DRAGON, 1, 500 }, { Skill.GALICK, 5, 480 },
                                        { Skill.KAMEJOKO, 7, 2000 }, { Skill.KAMEJOKO, 6, 1800 },
                                        { Skill.KAMEJOKO, 4, 1500 }, { Skill.KAMEJOKO, 2, 1000 },
                                        { Skill.ANTOMIC, 3, 1200 }, { Skill.ANTOMIC, 5, 1700 },
                                        { Skill.ANTOMIC, 7, 2000 },
                                        { Skill.MASENKO, 1, 800 }, { Skill.MASENKO, 5, 1300 },
                                        { Skill.MASENKO, 6, 1500 },
                                        { Skill.TAI_TAO_NANG_LUONG, 1, 5000 }, { Skill.TAI_TAO_NANG_LUONG, 3, 10000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 5, 25000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 6, 30000 }, { Skill.TAI_TAO_NANG_LUONG, 7, 50000 }
                        })
                        .secondsRest(_5_PHUT)
                        .build();

        public static final BossData BUIBUI_TANG3 = BossData.builder()
                        .name("BuiBui")
                        .gender(ConstPlayer.TRAI_DAT)
                        .typeDame(Boss.DAME_NORMAL)
                        .typeHp(Boss.HP_NORMAL)
                        .dame(250000)
                        .hp(new double[][] { { 500000000 } })
                        .outfit(new short[] { 451, 452, 453 })
                        .mapJoin(new short[] { 117 })
                        .skillTemp(new int[][] {
                                        { Skill.DEMON, 3, 450 }, { Skill.DEMON, 6, 400 }, { Skill.DRAGON, 7, 650 },
                                        { Skill.DRAGON, 1, 500 }, { Skill.GALICK, 5, 480 },
                                        { Skill.KAMEJOKO, 7, 2000 }, { Skill.KAMEJOKO, 6, 1800 },
                                        { Skill.KAMEJOKO, 4, 1500 }, { Skill.KAMEJOKO, 2, 1000 },
                                        { Skill.ANTOMIC, 3, 1200 }, { Skill.ANTOMIC, 5, 1700 },
                                        { Skill.ANTOMIC, 7, 2000 },
                                        { Skill.MASENKO, 1, 800 }, { Skill.MASENKO, 5, 1300 },
                                        { Skill.MASENKO, 6, 1500 },
                                        { Skill.TAI_TAO_NANG_LUONG, 1, 5000 }, { Skill.TAI_TAO_NANG_LUONG, 3, 10000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 5, 25000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 6, 30000 }, { Skill.TAI_TAO_NANG_LUONG, 7, 50000 }
                        })
                        .secondsRest(_5_PHUT)
                        .build();

        public static final BossData CALICH_TANG5 = BossData.builder()
                        .name("Ca Đíc")
                        .gender(ConstPlayer.TRAI_DAT)
                        .typeDame(Boss.DAME_NORMAL)
                        .typeHp(Boss.HP_NORMAL)
                        .dame(350000)
                        .hp(new double[][] { { 500000000 } })
                        .outfit(new short[] { 103, 16, 17 })
                        .mapJoin(new short[] { 119 })
                        .skillTemp(new int[][] {
                                        { Skill.DEMON, 3, 450 }, { Skill.DEMON, 6, 400 }, { Skill.DRAGON, 7, 650 },
                                        { Skill.DRAGON, 1, 500 }, { Skill.GALICK, 5, 480 },
                                        { Skill.KAMEJOKO, 7, 2000 }, { Skill.KAMEJOKO, 6, 1800 },
                                        { Skill.KAMEJOKO, 4, 1500 }, { Skill.KAMEJOKO, 2, 1000 },
                                        { Skill.ANTOMIC, 3, 1200 }, { Skill.ANTOMIC, 5, 1700 },
                                        { Skill.ANTOMIC, 7, 2000 },
                                        { Skill.MASENKO, 1, 800 }, { Skill.MASENKO, 5, 1300 },
                                        { Skill.MASENKO, 6, 1500 },
                                        { Skill.TAI_TAO_NANG_LUONG, 1, 5000 }, { Skill.TAI_TAO_NANG_LUONG, 3, 10000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 5, 25000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 6, 30000 }, { Skill.TAI_TAO_NANG_LUONG, 7, 50000 }
                        })
                        .secondsRest(_5_PHUT)
                        .build();

        public static final BossData GOKU_TANG5 = BossData.builder()
                        .name("Gôku")
                        .gender(ConstPlayer.TRAI_DAT)
                        .typeDame(Boss.DAME_NORMAL)
                        .typeHp(Boss.HP_NORMAL)
                        .dame(350000)
                        .hp(new double[][] { { 500000000 } })
                        .outfit(new short[] { 101, 1, 2 })
                        .mapJoin(new short[] { 119 })
                        .skillTemp(new int[][] {
                                        { Skill.DEMON, 3, 450 }, { Skill.DEMON, 6, 400 }, { Skill.DRAGON, 7, 650 },
                                        { Skill.DRAGON, 1, 500 }, { Skill.GALICK, 5, 480 },
                                        { Skill.KAMEJOKO, 7, 2000 }, { Skill.KAMEJOKO, 6, 1800 },
                                        { Skill.KAMEJOKO, 4, 1500 }, { Skill.KAMEJOKO, 2, 1000 },
                                        { Skill.ANTOMIC, 3, 1200 }, { Skill.ANTOMIC, 5, 1700 },
                                        { Skill.ANTOMIC, 7, 2000 },
                                        { Skill.MASENKO, 1, 800 }, { Skill.MASENKO, 5, 1300 },
                                        { Skill.MASENKO, 6, 1500 },
                                        { Skill.TAI_TAO_NANG_LUONG, 1, 5000 }, { Skill.TAI_TAO_NANG_LUONG, 3, 10000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 5, 25000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 6, 30000 }, { Skill.TAI_TAO_NANG_LUONG, 7, 50000 }
                        })
                        .secondsRest(_5_PHUT)
                        .build();

        public static final BossData YACON_TANG4 = BossData.builder()
                        .name("Yacôn")
                        .gender(ConstPlayer.TRAI_DAT)
                        .typeDame(Boss.DAME_NORMAL)
                        .typeHp(Boss.HP_NORMAL)
                        .dame(350000)
                        .hp(new double[][] { { 500000000 } })
                        .outfit(new short[] { 415, 416, 417 })
                        .mapJoin(new short[] { 118 })
                        .skillTemp(new int[][] {
                                        { Skill.LIEN_HOAN, 3, 450 }, { Skill.LIEN_HOAN, 6, 400 },
                                        { Skill.LIEN_HOAN, 7, 650 }, { Skill.LIEN_HOAN, 1, 500 },
                                        { Skill.LIEN_HOAN, 5, 480 },
                                        { Skill.KAMEJOKO, 7, 2000 }, { Skill.KAMEJOKO, 6, 1800 },
                                        { Skill.KAMEJOKO, 4, 1500 }, { Skill.KAMEJOKO, 2, 1000 },
                                        { Skill.ANTOMIC, 3, 1200 }, { Skill.ANTOMIC, 5, 1700 },
                                        { Skill.ANTOMIC, 7, 2000 },
                                        { Skill.MASENKO, 1, 800 }, { Skill.MASENKO, 5, 1300 },
                                        { Skill.MASENKO, 6, 1500 },
                                        { Skill.TAI_TAO_NANG_LUONG, 1, 5000 }, { Skill.TAI_TAO_NANG_LUONG, 3, 10000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 5, 25000 }, })
                        .secondsRest(_5_PHUT)
                        .build();

        public static final BossData XEN_MAX = BossData.builder()
                        .name("Xên Max")
                        .gender(ConstPlayer.XAYDA)
                        .typeDame(Boss.DAME_NORMAL)
                        .typeHp(Boss.HP_NORMAL)
                        .dame(350000)
                        .hp(new double[][] { { 2_000_000_000 } })
                        .outfit(new short[] { 1296, 1297, 1298 })
                        .mapJoin(new short[] { 99 })
                        .skillTemp(new int[][] {
                                        { Skill.DRAGON, 1, 100 }, { Skill.DRAGON, 2, 200 }, { Skill.DRAGON, 3, 300 },
                                        { Skill.DRAGON, 7, 700 },
                                        { Skill.KAMEJOKO, 1, 1000 }, { Skill.KAMEJOKO, 2, 1200 },
                                        { Skill.KAMEJOKO, 5, 1500 }, { Skill.KAMEJOKO, 7, 1700 },
                                        { Skill.GALICK, 1, 100 }
                        })
                        .secondsRest(_5_PHUT)
                        .build();

        public static final BossData SOI_HEC_QUYN = BossData.builder()
                        .name("Sói Hẹc Quyn")
                        .gender(ConstPlayer.TRAI_DAT)
                        .typeDame(Boss.DAME_NORMAL)
                        .typeHp(Boss.HP_NORMAL)
                        .dame(400000)
                        .hp(new double[][] { { 99999999000d } })
                        .outfit(new short[] { 394, 395, 396 })
                        .mapJoin(new short[] { 129 })
                        .skillTemp(new int[][] {
                                        { Skill.DRAGON, 1, 100 }, { Skill.DRAGON, 2, 200 }, { Skill.DRAGON, 3, 300 },
                                        { Skill.DRAGON, 7, 700 },
                                        { Skill.KAMEJOKO, 1, 1000 }, { Skill.KAMEJOKO, 2, 1200 },
                                        { Skill.KAMEJOKO, 5, 1500 }, { Skill.KAMEJOKO, 7, 1700 },
                                        { Skill.GALICK, 1, 100 }
                        })
                        .secondsRest(_5_PHUT)
                        .build();

        public static final BossData O_DO = BossData.builder()
                        .name("Ở Dơ")
                        .gender(ConstPlayer.TRAI_DAT)
                        .typeDame(Boss.DAME_NORMAL)
                        .typeHp(Boss.HP_NORMAL)
                        .dame(40000000)
                        .hp(new double[][] { { 999999900d } })
                        .outfit(new short[] { 400, 401, 402 })
                        .mapJoin(new short[] { 129 })
                        .skillTemp(new int[][] {
                                        { Skill.DRAGON, 1, 100 }, { Skill.DRAGON, 2, 200 }, { Skill.DRAGON, 3, 300 },
                                        { Skill.DRAGON, 7, 700 },
                                        { Skill.KAMEJOKO, 1, 1000 }, { Skill.KAMEJOKO, 2, 1200 },
                                        { Skill.KAMEJOKO, 5, 1500 }, { Skill.KAMEJOKO, 7, 1700 },
                                        { Skill.GALICK, 1, 100 }
                        })
                        .secondsRest(_5_PHUT)
                        .build();

        public static final BossData XINBATO = BossData.builder()
                        .name("Xinbatô")
                        .gender(ConstPlayer.TRAI_DAT)
                        .typeDame(Boss.DAME_NORMAL)
                        .typeHp(Boss.HP_NORMAL)
                        .dame(90000000)
                        .hp(new double[][] { { 999999927d } })
                        .outfit(new short[] { 359, 360, 361 })
                        .mapJoin(new short[] { 129 })
                        .skillTemp(new int[][] {
                                        { Skill.DRAGON, 1, 100 }, { Skill.DRAGON, 2, 200 }, { Skill.DRAGON, 3, 300 },
                                        { Skill.DRAGON, 7, 700 },
                                        { Skill.KAMEJOKO, 1, 1000 }, { Skill.KAMEJOKO, 2, 1200 },
                                        { Skill.KAMEJOKO, 5, 1500 }, { Skill.KAMEJOKO, 7, 1700 },
                                        { Skill.GALICK, 1, 100 }
                        })
                        .secondsRest(_5_PHUT)
                        .build();

        public static final BossData CHA_PA = BossData.builder()
                        .name("Cha pa")
                        .gender(ConstPlayer.TRAI_DAT)
                        .typeDame(Boss.DAME_NORMAL)
                        .typeHp(Boss.HP_NORMAL)
                        .dame(990000000)
                        .hp(new double[][] { { 9999999900d } })
                        .outfit(new short[] { 362, 363, 364 })
                        .mapJoin(new short[] { 129 })
                        .skillTemp(new int[][] {
                                        { Skill.DRAGON, 1, 100 }, { Skill.DRAGON, 2, 200 }, { Skill.DRAGON, 3, 300 },
                                        { Skill.DRAGON, 7, 700 },
                                        { Skill.KAMEJOKO, 1, 1000 }, { Skill.KAMEJOKO, 2, 1200 },
                                        { Skill.KAMEJOKO, 5, 1500 }, { Skill.KAMEJOKO, 7, 1700 },
                                        { Skill.GALICK, 1, 100 }
                        })
                        .secondsRest(_5_PHUT)
                        .build();

        public static final BossData PON_PUT = BossData.builder()
                        .name("Pon put")
                        .gender(ConstPlayer.TRAI_DAT)
                        .typeDame(Boss.DAME_NORMAL)
                        .typeHp(Boss.HP_NORMAL)
                        .dame(999000l)
                        .hp(new double[][] { { 9999382332d } })
                        .outfit(new short[] { 365, 366, 367 })
                        .mapJoin(new short[] { 129 })
                        .skillTemp(new int[][] {
                                        { Skill.DRAGON, 1, 100 }, { Skill.DRAGON, 2, 200 }, { Skill.DRAGON, 3, 300 },
                                        { Skill.DRAGON, 7, 700 },
                                        { Skill.KAMEJOKO, 1, 1000 }, { Skill.KAMEJOKO, 2, 1200 },
                                        { Skill.KAMEJOKO, 5, 1500 }, { Skill.KAMEJOKO, 7, 1700 },
                                        { Skill.GALICK, 1, 100 }
                        })
                        .secondsRest(_5_PHUT)
                        .build();

        public static final BossData CHAN_XU = BossData.builder()
                        .name("Chan xư")
                        .gender(ConstPlayer.TRAI_DAT)
                        .typeDame(Boss.DAME_NORMAL)
                        .typeHp(Boss.HP_NORMAL)
                        .dame(990000l)
                        .hp(new double[][] { { 9999999990000d } })
                        .outfit(new short[] { 371, 372, 373 })
                        .mapJoin(new short[] { 129 })
                        .skillTemp(new int[][] {
                                        { Skill.DRAGON, 1, 100 }, { Skill.DRAGON, 2, 200 }, { Skill.DRAGON, 3, 300 },
                                        { Skill.DRAGON, 7, 700 },
                                        { Skill.KAMEJOKO, 1, 1000 }, { Skill.KAMEJOKO, 2, 1200 },
                                        { Skill.KAMEJOKO, 5, 1500 }, { Skill.KAMEJOKO, 7, 1700 },
                                        { Skill.GALICK, 1, 100 }
                        })
                        .secondsRest(_5_PHUT)
                        .build();

        public static final BossData TAU_PAY_PAY = BossData.builder()
                        .name("Tàu Pảy Pảy")
                        .gender(ConstPlayer.TRAI_DAT)
                        .typeDame(Boss.DAME_NORMAL)
                        .typeHp(Boss.HP_NORMAL)
                        .dame(9999000l)
                        .hp(new double[][] { { 99990000000000d } })
                        .outfit(new short[] { 92, 93, 94 })
                        .mapJoin(new short[] { 129 })
                        .skillTemp(new int[][] {
                                        { Skill.DRAGON, 1, 100 }, { Skill.DRAGON, 2, 200 }, { Skill.DRAGON, 3, 300 },
                                        { Skill.DRAGON, 7, 700 },
                                        { Skill.KAMEJOKO, 1, 1000 }, { Skill.KAMEJOKO, 2, 1200 },
                                        { Skill.KAMEJOKO, 5, 1500 }, { Skill.KAMEJOKO, 7, 1700 },
                                        { Skill.GALICK, 1, 100 }
                        })
                        .secondsRest(_5_PHUT)
                        .build();

        public static final BossData YAMCHA = BossData.builder()
                        .name("Yamcha")
                        .gender(ConstPlayer.TRAI_DAT)
                        .typeDame(Boss.DAME_NORMAL)
                        .typeHp(Boss.HP_NORMAL)
                        .dame(999990000l)
                        .hp(new double[][] { { 9999000000000d } })
                        .outfit(new short[] { 374, 375, 376 })
                        .mapJoin(new short[] { 129 })
                        .skillTemp(new int[][] {
                                        { Skill.DRAGON, 1, 100 }, { Skill.DRAGON, 2, 200 }, { Skill.DRAGON, 3, 300 },
                                        { Skill.DRAGON, 7, 700 },
                                        { Skill.KAMEJOKO, 1, 1000 }, { Skill.KAMEJOKO, 2, 1200 },
                                        { Skill.KAMEJOKO, 5, 1500 }, { Skill.KAMEJOKO, 7, 1700 },
                                        { Skill.GALICK, 1, 100 }
                        })
                        .secondsRest(_5_PHUT)
                        .build();

        public static final BossData JACKY_CHUN = BossData.builder()
                        .name("Jacky Chun")
                        .gender(ConstPlayer.TRAI_DAT)
                        .typeDame(Boss.DAME_NORMAL)
                        .typeHp(Boss.HP_NORMAL)
                        .dame(999900000l)
                        .hp(new double[][] { { 99999000000d } })
                        .outfit(new short[] { 356, 357, 358 })
                        .mapJoin(new short[] { 129 })
                        .skillTemp(new int[][] {
                                        { Skill.DRAGON, 1, 100 }, { Skill.DRAGON, 2, 200 }, { Skill.DRAGON, 3, 300 },
                                        { Skill.DRAGON, 7, 700 },
                                        { Skill.KAMEJOKO, 1, 1000 }, { Skill.KAMEJOKO, 2, 1200 },
                                        { Skill.KAMEJOKO, 5, 1500 }, { Skill.KAMEJOKO, 7, 1700 },
                                        { Skill.GALICK, 1, 100 }
                        })
                        .secondsRest(_5_PHUT)
                        .build();

        public static final BossData THIEN_XIN_HANG = BossData.builder()
                        .name("Thiên Xin Hăng")
                        .gender(ConstPlayer.TRAI_DAT)
                        .typeDame(Boss.DAME_NORMAL)
                        .typeHp(Boss.HP_NORMAL)
                        .dame(9990000l)
                        .hp(new double[][] { { 9999999000000000d } })
                        .outfit(new short[] { 368, 369, 370 })
                        .mapJoin(new short[] { 129 })
                        .skillTemp(new int[][] {
                                        { Skill.DRAGON, 1, 100 }, { Skill.DRAGON, 2, 200 }, { Skill.DRAGON, 3, 300 },
                                        { Skill.DRAGON, 7, 700 },
                                        { Skill.KAMEJOKO, 1, 1000 }, { Skill.KAMEJOKO, 2, 1200 },
                                        { Skill.KAMEJOKO, 5, 1500 }, { Skill.KAMEJOKO, 7, 1700 },
                                        { Skill.THAI_DUONG_HA_SAN, 1, 15000 }
                        })
                        .secondsRest(_5_PHUT)
                        .build();

        public static final BossData THIEN_XIN_HANG_CLONE = BossData.builder()
                        .name("Thiên Xin Hăng")
                        .gender(ConstPlayer.TRAI_DAT)
                        .typeDame(Boss.DAME_NORMAL)
                        .typeHp(Boss.HP_NORMAL)
                        .dame(9990000d)
                        .hp(new double[][] { { 999990000000d } })
                        .outfit(new short[] { 368, 369, 370 })
                        .mapJoin(new short[] { 129 })
                        .skillTemp(new int[][] {
                                        { Skill.DRAGON, 1, 100 }, { Skill.DRAGON, 2, 200 }, { Skill.DRAGON, 3, 300 },
                                        { Skill.DRAGON, 7, 700 },
                                        { Skill.KAMEJOKO, 1, 1000 }, { Skill.KAMEJOKO, 2, 1200 },
                                        { Skill.KAMEJOKO, 5, 1500 }, { Skill.KAMEJOKO, 7, 1700 },
                                        { Skill.THAI_DUONG_HA_SAN, 1, 15000 }
                        })
                        .secondsRest(_5_PHUT)
                        .build();
        public static final BossData LIU_LIU = BossData.builder()
                        .name("Liu Liu")
                        .gender(ConstPlayer.TRAI_DAT)
                        .typeDame(Boss.DAME_NORMAL)
                        .typeHp(Boss.HP_NORMAL)
                        .dame(9990000d)
                        .hp(new double[][] { { 999999900000d } })
                        .outfit(new short[] { 397, 398, 399 })
                        .mapJoin(new short[] { 129 })
                        .skillTemp(new int[][] {
                                        { Skill.DRAGON, 1, 100 }, { Skill.DRAGON, 2, 200 }, { Skill.DRAGON, 3, 300 },
                                        { Skill.DRAGON, 7, 700 },
                                        { Skill.KAMEJOKO, 1, 1000 }, { Skill.KAMEJOKO, 2, 1200 },
                                        { Skill.KAMEJOKO, 5, 1500 }, { Skill.KAMEJOKO, 7, 1700 },
                                        { Skill.GALICK, 1, 100 }
                        })
                        .secondsRest(_5_PHUT)
                        .build();

        public static final BossData NGO_KHONG = BossData.builder()
                        .name("Tôn Ngộ Không")
                        .gender(ConstPlayer.XAYDA)
                        .typeDame(Boss.DAME_NORMAL)
                        .typeHp(Boss.HP_NORMAL)
                        .dame(50000)
                        .hp(new double[][] { { 500000000 } })
                        .outfit(new short[] { 462, 463, 464 })
                        .mapJoin(new short[] { 124 })
                        .skillTemp(new int[][] {
                                        { Skill.DRAGON, 1, 100 }, { Skill.DRAGON, 2, 200 }, { Skill.DRAGON, 3, 300 },
                                        { Skill.DRAGON, 7, 700 },
                                        { Skill.KAMEJOKO, 1, 1000 }, { Skill.KAMEJOKO, 2, 1200 },
                                        { Skill.KAMEJOKO, 5, 1500 }, { Skill.KAMEJOKO, 7, 1700 },
                                        { Skill.GALICK, 1, 100 }
                        })
                        .secondsRest(_5_PHUT)
                        .build();

        public static final BossData BAT_GIOI = BossData.builder()
                        .name("Chư Bát Giới")
                        .gender(ConstPlayer.XAYDA)
                        .typeDame(Boss.DAME_NORMAL)
                        .typeHp(Boss.HP_NORMAL)
                        .dame(500000)
                        .hp(new double[][] { { 500000000 } })
                        .outfit(new short[] { 465, 466, 467 })
                        .mapJoin(new short[] { 124 })
                        .skillTemp(new int[][] {
                                        { Skill.DRAGON, 1, 100 }, { Skill.DRAGON, 2, 200 }, { Skill.DRAGON, 3, 300 },
                                        { Skill.DRAGON, 7, 700 },
                                        { Skill.KAMEJOKO, 1, 1000 }, { Skill.KAMEJOKO, 2, 1200 },
                                        { Skill.KAMEJOKO, 5, 1500 }, { Skill.KAMEJOKO, 7, 1700 },
                                        { Skill.GALICK, 1, 100 }
                        })
                        .secondsRest(_5_PHUT)
                        .build();

        public static final BossData FIDEGOLD = BossData.builder()
                        .name("Fide Vàng  %1")
                        .gender(ConstPlayer.XAYDA)
                        .typeDame(Boss.DAME_NORMAL)
                        .typeHp(Boss.HP_NORMAL)
                        .dame(500000)
                        .hp(new double[][] { { 1000000000 } })
                        .outfit(new short[] { 502, 503, 504 })
                        .mapJoin(new short[] { 6 })
                        .skillTemp(new int[][] {
                                        { Skill.DRAGON, 1, 100 }, { Skill.DRAGON, 2, 200 }, { Skill.DRAGON, 3, 300 },
                                        { Skill.DRAGON, 7, 700 },
                                        { Skill.KAMEJOKO, 1, 1000 }, { Skill.KAMEJOKO, 2, 1200 },
                                        { Skill.KAMEJOKO, 5, 1500 }, { Skill.KAMEJOKO, 7, 1700 },
                                        { Skill.GALICK, 1, 100 }
                        })
                        .secondsRest(_5_PHUT)
                        .build();

        public static final BossData CUMBER = BossData.builder()
                        .name("Fide Vàng")
                        .gender(ConstPlayer.XAYDA)
                        .typeDame(Boss.DAME_NORMAL)
                        .typeHp(Boss.HP_NORMAL)
                        .dame(500000)
                        .hp(new double[][] { { 1500000000 } })
                        .outfit(new short[] { 502, 503, 504 })
                        .mapJoin(new short[] { 155 })
                        .skillTemp(new int[][] {
                                        { Skill.DRAGON, 1, 100 }, { Skill.DRAGON, 2, 200 }, { Skill.DRAGON, 3, 300 },
                                        { Skill.DRAGON, 7, 700 },
                                        { Skill.KAMEJOKO, 1, 1000 }, { Skill.KAMEJOKO, 2, 1200 },
                                        { Skill.KAMEJOKO, 5, 1500 }, { Skill.KAMEJOKO, 7, 1700 },
                                        { Skill.GALICK, 1, 100 }
                        })
                        .secondsRest(_5_PHUT)
                        .build();

        public static final BossData CUMBER2 = BossData.builder()
                        .name("Cooler Vàng")
                        .gender(ConstPlayer.XAYDA)
                        .typeDame(Boss.DAME_NORMAL)
                        .typeHp(Boss.HP_NORMAL)
                        .dame(500000)
                        .hp(new double[][] { { 2_000_000_000 } })
                        .outfit(new short[] { 709, 710, 711 })
                        .mapJoin(new short[] { 155 })
                        .skillTemp(new int[][] {
                                        { Skill.DRAGON, 1, 100 }, { Skill.DRAGON, 2, 200 }, { Skill.DRAGON, 3, 300 },
                                        { Skill.DRAGON, 7, 700 },
                                        { Skill.KAMEJOKO, 1, 1000 }, { Skill.KAMEJOKO, 2, 1200 },
                                        { Skill.KAMEJOKO, 5, 1500 }, { Skill.KAMEJOKO, 7, 1700 },
                                        { Skill.GALICK, 1, 100 }
                        })
                        .secondsRest(_5_PHUT)
                        .build();
        public static final BossData DRACULA = BossData.builder()
                        .name("Khiêu Chiến")
                        .gender(ConstPlayer.TRAI_DAT)
                        .typeDame(Boss.DAME_NORMAL)
                        .typeHp(Boss.HP_NORMAL)
                        .dame(1000000l)
                        .hp(new double[][] { { 9999999000000d } })
                        .outfit(new short[] { 1501, 1498, 1499 })
                        .mapJoin(new short[] { 129 })
                        .skillTemp(new int[][] {
                                        { Skill.DRAGON, 1, 100 }, { Skill.TAI_TAO_NANG_LUONG, 1, 5000 },
                                        { Skill.DRAGON, 3, 300 }, { Skill.DRAGON, 7, 700 },
                                        { Skill.KAMEJOKO, 1, 1000 }, { Skill.KAMEJOKO, 2, 1200 },
                                        { Skill.KAMEJOKO, 5, 1500 }, { Skill.KAMEJOKO, 7, 1700 },
                                        { Skill.GALICK, 1, 100 }
                        })
                        .secondsRest(_5_PHUT)
                        .build();
        public static final BossData VO_HINH = BossData.builder()
                        .name("Khiêu Chiến")
                        .gender(ConstPlayer.TRAI_DAT)
                        .typeDame(Boss.DAME_NORMAL)
                        .typeHp(Boss.HP_NORMAL)
                        .dame(509000l)
                        .hp(new double[][] { { 99999990000d } })
                        .outfit(new short[] { 1500, 1498, 1499 })
                        .mapJoin(new short[] { 129 })
                        .skillTemp(new int[][] {
                                        { Skill.DRAGON, 1, 100 }, { Skill.TAI_TAO_NANG_LUONG, 1, 5000 },
                                        { Skill.DRAGON, 3, 300 }, { Skill.DRAGON, 7, 700 },
                                        { Skill.KAMEJOKO, 1, 1000 }, { Skill.KAMEJOKO, 2, 1200 },
                                        { Skill.KAMEJOKO, 5, 1500 }, { Skill.KAMEJOKO, 7, 1700 },
                                        { Skill.GALICK, 1, 100 }
                        })
                        .secondsRest(_5_PHUT)
                        .build();
        public static final BossData BongBang = BossData.builder()
                        .name("Khiêu Chiến")
                        .gender(ConstPlayer.TRAI_DAT)
                        .typeDame(Boss.DAME_NORMAL)
                        .typeHp(Boss.HP_NORMAL)
                        .dame(90000090l)
                        .hp(new double[][] { { 999900000000d } })
                        .outfit(new short[] { 1497, 1498, 1499 })
                        .mapJoin(new short[] { 129 })
                        .skillTemp(new int[][] {
                                        { Skill.DRAGON, 1, 100 }, { Skill.TAI_TAO_NANG_LUONG, 1, 5000 },
                                        { Skill.DRAGON, 3, 300 }, { Skill.DRAGON, 7, 700 },
                                        { Skill.KAMEJOKO, 1, 1000 }, { Skill.KAMEJOKO, 2, 1200 },
                                        { Skill.KAMEJOKO, 5, 1500 }, { Skill.KAMEJOKO, 7, 1700 },
                                        { Skill.GALICK, 1, 100 } })
                        .secondsRest(_5_PHUT)
                        .build();

        public static final BossData VuaQuySatan = BossData.builder()
                        .name("Khiêu Chiến")
                        .gender(ConstPlayer.TRAI_DAT)
                        .typeDame(Boss.DAME_NORMAL)
                        .typeHp(Boss.HP_NORMAL)
                        .dame(928300l)
                        .hp(new double[][] { { 99999990000d } })
                        .outfit(new short[] { 1517, 1518, 1519 })
                        .mapJoin(new short[] { 129 })
                        .skillTemp(new int[][] {
                                        { Skill.DRAGON, 1, 100 }, { Skill.TAI_TAO_NANG_LUONG, 1, 5000 },
                                        { Skill.DRAGON, 3, 300 }, { Skill.DRAGON, 7, 700 },
                                        { Skill.KAMEJOKO, 1, 1000 }, { Skill.KAMEJOKO, 2, 1200 },
                                        { Skill.KAMEJOKO, 5, 1500 }, { Skill.KAMEJOKO, 7, 1700 },
                                        { Skill.GALICK, 1, 100 }
                        })
                        .secondsRest(_5_PHUT)
                        .build();

        public static final BossData ThoDauBac = BossData.builder()
                        .name("Khiêu Chiến")
                        .gender(ConstPlayer.TRAI_DAT)
                        .typeDame(Boss.DAME_NORMAL)
                        .typeHp(Boss.HP_NORMAL)
                        .dame(999000d)
                        .hp(new double[][] { { 9999999990d } })
                        .outfit(new short[] { 1514, 1515, 1516 })
                        .mapJoin(new short[] { 129 })
                        .skillTemp(new int[][] {
                                        { Skill.DRAGON, 1, 100 }, { Skill.TAI_TAO_NANG_LUONG, 1, 5000 },
                                        { Skill.DRAGON, 3, 300 }, { Skill.DRAGON, 7, 700 },
                                        { Skill.KAMEJOKO, 1, 1000 }, { Skill.KAMEJOKO, 2, 1200 },
                                        { Skill.KAMEJOKO, 5, 1500 }, { Skill.KAMEJOKO, 7, 1700 },
                                        { Skill.GALICK, 1, 100 }
                        })
                        .secondsRest(_5_PHUT)
                        .build();
        public static final BossData SUMO = new BossData(
                        "Vegeta evo [Đá Bảo Vệ]",
                        ConstPlayer.TRAI_DAT,
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp

                        999000d,
                        990d,
                        new double[][] { { 137423874d } },
                        new short[] { 1408, 1409, 1410 }, // outfit
                        new short[] { 210 }, // map join
                        new int[][] { // skill
                                        { Skill.MASENKO, 7, 10 },
                                        { Skill.KAMEJOKO, 7, 10 },
                                        { Skill.HUYT_SAO, 7, 10000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 7, 10000 },
                                        { Skill.ANTOMIC, 7, 10 }, },
                        _5_PHUT);

        public static final BossData SUMO1 = new BossData(
                        "Goku Mui [Đá Bảo Vệ]",
                        ConstPlayer.TRAI_DAT,
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp

                        137423874d,
                        137874d,
                        new double[][] { { 137423874d } },
                        new short[] { 1411, 1412, 1413 }, // outfit
                        new short[] { 210 }, // map join
                        new int[][] { // skill
                                        { Skill.MASENKO, 7, 10 },
                                        { Skill.KAMEJOKO, 7, 10 },
                                        { Skill.HUYT_SAO, 7, 10000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 7, 10000 },
                                        { Skill.ANTOMIC, 7, 10 }, },
                        _5_PHUT);
        public static final BossData SUMO2 = new BossData(
                        "Goku SSJ 4 [Đá Bảo Vệ]",
                        ConstPlayer.TRAI_DAT,
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp

                        137423874d,
                        133874d,
                        new double[][] { { 137423874d } },
                        new short[] { 1417, 1418, 1419 }, // outfit
                        new short[] { 210 }, // map join
                        new int[][] { // skill
                                        { Skill.MASENKO, 7, 10 },
                                        { Skill.KAMEJOKO, 7, 10 },
                                        { Skill.HUYT_SAO, 7, 10000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 7, 10000 },
                                        { Skill.ANTOMIC, 7, 10 }, },
                        _5_PHUT);
        public static final BossData SUMO3 = new BossData(
                        "Vegeta SSJ 4 [Đá Bảo Vệ]",
                        ConstPlayer.TRAI_DAT,
                        Boss.DAME_NORMAL, // type dame
                        Boss.HP_NORMAL, // type hp

                        137423874d,
                        137474d,
                        new double[][] { { 137423874d } },
                        new short[] { 1420, 1421, 1422 }, // outfit
                        new short[] { 210 }, // map join
                        new int[][] { // skill
                                        { Skill.MASENKO, 7, 10 },
                                        { Skill.KAMEJOKO, 7, 10 },
                                        { Skill.HUYT_SAO, 7, 10000 },
                                        { Skill.TAI_TAO_NANG_LUONG, 7, 10000 },
                                        { Skill.ANTOMIC, 7, 10 }, },
                        _5_PHUT);

}
