package nro.models.boss;

import Sumo.Boss_list_Broly.Broly;
import Sumo.Boss_list_Chanmenh.chanmenh1;
import Sumo.Boss_list_Chanmenh.chanmenh2;
import Sumo.Boss_list_Chanmenh.chanmenh3;
import Sumo.Boss_list_Chanmenh.chanmenh4;
import Sumo.Boss_list_Chanmenh.chanmenh5;
import Sumo.Boss_list_Coler.ColerVang;
import Sumo.Boss_list_Fide.Fide1;
import Sumo.Boss_list_Fide.Fide2;
import Sumo.Boss_list_Fide.Fide3;
import Sumo.Boss_list_Kết_Hôn.BOSS_TRUNGQUY1;
import Sumo.Boss_list_Kết_Hôn.BOSS_TRUNGQUY2;
import Sumo.Boss_list_Kết_Hôn.BOSS_TRUNGQUY3;
import Sumo.Boss_list_Khobau.Brook;
import Sumo.Boss_list_Khobau.Chopper;
import Sumo.Boss_list_Khobau.Franky;
import Sumo.Boss_list_Khobau.Luffy;
import Sumo.Boss_list_Khobau.Nami;
import Sumo.Boss_list_Khobau.Robin;
import Sumo.Boss_list_Khobau.Sanji;
import Sumo.Boss_list_Khobau.Usopp;
import Sumo.Boss_list_Khobau.Zoro;
import Sumo.Boss_list_Linhthu.linhthu1;
import Sumo.Boss_list_Linhthu.linhthu2;
import Sumo.Boss_list_Linhthu.linhthu3;
import Sumo.Boss_list_Linhthu.linhthu4;
import Sumo.Boss_list_SPL.Hashibira;
import Sumo.Boss_list_SPL.Inosuke;
import Sumo.Boss_list_SPL.TATHAN;
import Sumo.Boss_list_SPL.Tanjiro;
import Sumo.Boss_list_Tainguyen.BOSS_DAUGOD;
import Sumo.Boss_list_Tainguyen.BOSS_THOIVANG;
import Sumo.Boss_list_Tainguyen.danangcap;
import Sumo.Boss_list_Tainguyen.naruto;
import Sumo.Boss_list_Tainguyen.obito;
import Sumo.Boss_list_ThanThu.DaThu1;
import Sumo.Boss_list_ThanThu.DaThu2;
import Sumo.Boss_list_ThanThu.DaThu3;
import Sumo.Boss_list_ThanThu.DaThu4;
import Sumo.Boss_list_ThanThu.DaThu5;
import Sumo.Boss_list_ThanThu.DaThu6;
import Sumo.Boss_list_ThanThu.DaThu7;
import Sumo.Boss_list_ThanThu.DaThu8;
import Sumo.Boss_list_ThanThu.HaoThienKhuyen;
import Sumo.Boss_list_ThanThu.Panda;
import Sumo.Boss_list_Traidat.Bido;
import Sumo.Boss_list_Traidat.Bojack;
import Sumo.Boss_list_Traidat.Bujin;
import Sumo.Boss_list_Traidat.Kogu;
import Sumo.Boss_list_Traidat.SiêuBojack;
import Sumo.Boss_list_Traidat.Zangya;
import Sumo.Boss_list_Tuonglai.Blackgoku;
import Sumo.Boss_list_Tuonglai.SieuXen;
import Sumo.Boss_list_Tuonglai.SuperBlackgoku;
import Sumo.Boss_list_Tuonglai.VegetaSS1;
import Sumo.Boss_list_huydiet.Thanhuydiet;
import Sumo.Boss_list_huydiet.Thiensuwhis;
import Sumo.Boss_list_huydiet.champa;
import Sumo.Boss_list_huydiet.vasdos;
import Sumo.Boss_list_keytambao.dabaove;
import Sumo.Boss_list_keytambao.keybac;
import Sumo.Boss_list_keytambao.keyvang;
import Sumo.Boss_list_khunggio.danhhieu;
import Sumo.Boss_list_khunggio.luuly;
import Sumo.Boss_list_khunggio.saophale;
import Sumo.Boss_list_quaidang.babyvegeta;
import Sumo.Boss_list_quaidang.panchi;
import Sumo.Boss_list_quaidang.superkhi;
import java.util.ArrayList;
import java.util.List;
import nro.consts.ConstEvent;
import nro.consts.ConstMap;
import nro.models.boss.BossAnTrom.TromVang;
import nro.models.boss.BossAnTrom.TromNgoc;
import nro.models.boss.BossAnTrom.TromNgocRuby;
import nro.models.boss.broly.SuperBroly;

//import nro.models.boss.bosstuonglai.*;
import nro.models.map.Map;
import nro.models.player.Player;
import nro.server.Manager;
import nro.services.MapService;
import org.apache.log4j.Logger;

/**
 * @Stole Arriety
 */
public class BossFactory {

    //id boss
    
     public static final byte SOI_HEC_QUYN = -77;
    public static final byte O_DO = -78;
    public static final byte XINBATO = -79;
    public static final byte CHA_PA = -80;
    public static final byte PON_PUT = -81;
    public static final byte CHAN_XU = -82;
    public static final byte TAU_PAY_PAY = -83;
    public static final byte YAMCHA = -84;
    public static final byte JACKY_CHUN = -85;
    public static final byte THIEN_XIN_HANG = -86;
    public static final byte LIU_LIU = -87;
    public static final byte THIEN_XIN_HANG_CLONE = -88;
    public static final byte THIEN_XIN_HANG_CLONE1 = -89;
    public static final byte THIEN_XIN_HANG_CLONE2 = -90;
    public static final byte THIEN_XIN_HANG_CLONE3 = -91;
    
    public static final int test1 = -1;
    public static final int BOSS_TRUNGQUY1 = 1;
    public static final int BOSS_TRUNGQUY2 = 2;
    public static final int BOSS_TRUNGQUY3 = 3;
    public static final int BOSS_THOIVANG = 4;
    public static final int BOSS_DAUGOD = 5;
    public static final int Bujin = 6;
    public static final int Kogu = 7;
    public static final int Zangya = 8;
    public static final int Bido = 9;
    public static final int Bojack = 10;
    public static final int SiêuBojack = 11;
    public static final int TATHAN = 12;
    public static final int Luffy = 13;
    public static final int Zoro = 14;
    public static final int Sanji = 15;
    public static final int Brook = 16;
    public static final int Chopper = 17;
    public static final int Nami = 18;
    public static final int Franky = 19;
    public static final int Usopp = 20;
    public static final int Robin = 21;
    public static final int HaoThienKhuyen = 22;
    public static final int Panda = 23;
    public static final int DaThu1 = 24;
    public static final int DaThu2 = 25;
    public static final int DaThu3 = 26;
    public static final int DaThu4 = 27;
    public static final int DaThu5 = 28;
    public static final int DaThu6 = 29;
    public static final int DaThu7 = 30;
    public static final int DaThu8 = 31;
    public static final int Fide1 = 32;
    public static final int Fide2 = 33;
    public static final int Fide3 = 34;
    public static final int Tanjiro = 35;
    public static final int Hashibira = 36;
    public static final int Inosuke = 37;
    public static final int Blackgoku = 38;
    public static final int SuperBlackgoku = 39;
    public static final int VegetaSS1 = 40;
    public static final int SieuXen = 41;
    public static final int ColerVang = 42;
    public static final int obito = 43;
    public static final int naruto = 44;
    public static final int SUPERBROLY = 45;
    public static final int chanmenh1 = 46;
    public static final int chanmenh2 = 47;
    public static final int chanmenh3 = 48;
    public static final int chanmenh4 = 49;
    public static final int chanmenh5 = 50;
    public static final int linhthu1 = 51;
    public static final int linhthu2 = 52;
    public static final int linhthu3 = 53;
    public static final int linhthu4 = 54;
    public static final int danhhieu = 55;
    public static final int luuly = 56;
    public static final int saophale = 57;
    public static final int keybac = 58;
    public static final int keyvang = 59;
    public static final int dabaove = 60;
    public static final int danangcap = 61;
    public static final int Broly = 62;
    public static final int TROMNGOCRUBY = 63;
    public static final int TROMNGOC = 64;
    public static final int NOEN = 65;
    public static final int THANHUYDIET = 66;
    public static final int THIENSUWISH = 67;
     public static final int champa = 68;
      public static final int vasdos = 69;
       public static final byte DRACULA = 70;
       public static final byte VoHinh = 71;
       public static final byte BongBang = 72;
       public static final byte VuaQuySatan = 73;
      public static final byte ThoDauBac = 74;
      public static final byte panchi = 75;
      public static final byte babyvegeta = 76;
      public static final byte superkhi = 77;
      
    

    private static final Logger logger = Logger.getLogger(BossFactory.class);

    public static final int[] MAP_APPEARED_QILIN = {ConstMap.VACH_NUI_ARU_42, ConstMap.VACH_NUI_MOORI_43, ConstMap.VACH_NUI_KAKAROT,
        ConstMap.LANG_ARU, ConstMap.LANG_MORI, ConstMap.LANG_KAKAROT, ConstMap.DOI_HOA_CUC, ConstMap.DOI_NAM_TIM, ConstMap.DOI_HOANG,
        ConstMap.TRAM_TAU_VU_TRU, ConstMap.TRAM_TAU_VU_TRU_25, ConstMap.TRAM_TAU_VU_TRU_26, ConstMap.LANG_PLANT, ConstMap.RUNG_NGUYEN_SINH,
        ConstMap.RUNG_CO, ConstMap.RUNG_THONG_XAYDA, ConstMap.RUNG_DA, ConstMap.THUNG_LUNG_DEN, ConstMap.BO_VUC_DEN, ConstMap.THANH_PHO_VEGETA,
        ConstMap.THUNG_LUNG_TRE, ConstMap.RUNG_NAM, ConstMap.RUNG_BAMBOO, ConstMap.RUNG_XUONG, ConstMap.RUNG_DUONG_XI, ConstMap.NAM_KAME,
        ConstMap.DAO_BULONG, ConstMap.DONG_KARIN, ConstMap.THI_TRAN_MOORI, ConstMap.THUNG_LUNG_MAIMA, ConstMap.NUI_HOA_TIM, ConstMap.NUI_HOA_VANG,
        ConstMap.NAM_GURU, ConstMap.DONG_NAM_GURU, ConstMap.THUNG_LUNG_NAMEC
    };

    private static BossFactory I;

    public static BossFactory gI() {
        if (BossFactory.I == null) {
            BossFactory.I = new BossFactory();
        }
        return BossFactory.I;
    }
    private final List<Boss> bosses;

    private BossFactory() {
        this.bosses = new ArrayList<>();
        initBoss();
    }

    private static void createAndAddBoss(int bossId) {
        Boss boss = createBoss(bossId);
        if (boss != null) {
        }
    }

    public Boss getBossById(int bossId) {
        return this.bosses.stream()
                .filter(boss -> boss.id == bossId && !boss.isDie())
                .findFirst()
                .orElse(null);
    }

    public void removeBoss(Boss boss) {
        this.bosses.remove(boss);
    }

    public boolean existBossOnPlayer(Player player) {
        return player.zone.getBosses().size() > 0;
    }

    public List<Boss> getBosses() {
        return this.bosses;
    }

    public static boolean isYar(int id) {
        return (//id == TAP_SU_1 || id == TAP_SU_2 || id == TAP_SU_3 || id == TAP_SU_4 || id == TAP_SU_5 || id == TAN_BINH_1 || id == TAN_BINH_2
                //                || id == TAN_BINH_3 || id == TAN_BINH_4 || id == TAN_BINH_5 || id == TAN_BINH_6 || id == DOI_TRUONG_1 || id == CHIEN_BINH_1 || id == CHIEN_BINH_2
                id == test1);
    }

    public static void initBoss() {
        new Thread(() -> {
            try {

                createBoss(BOSS_TRUNGQUY1);
                createBoss(BOSS_TRUNGQUY2);
                createBoss(BOSS_TRUNGQUY3);
                createBoss(BOSS_THOIVANG);
                createBoss(BOSS_DAUGOD);
                createBoss(Bujin);
                createBoss(Kogu);
                createBoss(Bido);
                createBoss(Zangya);
                createBoss(Bojack);
                createBoss(SiêuBojack);
                createBoss(TATHAN);

                createBoss(Luffy);
                createBoss(Chopper);
                createBoss(Franky);
                createBoss(Nami);
                createBoss(Robin);
                createBoss(Sanji);
                createBoss(Usopp);
                createBoss(Zoro);
                createBoss(Brook);
                createBoss(HaoThienKhuyen);
                createBoss(Panda);
                createBoss(DaThu1);
                createBoss(DaThu2);
                createBoss(DaThu3);
                createBoss(DaThu4);
                createBoss(DaThu5);
                createBoss(DaThu6);
                createBoss(DaThu7);
                createBoss(DaThu8);
                createBoss(Fide1);
                createBoss(Fide2);
                createBoss(Fide3);
                createBoss(Hashibira);
                createBoss(Inosuke);
                createBoss(Tanjiro);
                createBoss(Blackgoku);
                createBoss(SuperBlackgoku);
                createBoss(VegetaSS1);
                createBoss(SieuXen);
                createBoss(ColerVang);
                createBoss(obito);
                createBoss(naruto);
                createBoss(SUPERBROLY);
                createBoss(chanmenh1);
                createBoss(chanmenh2);
                createBoss(chanmenh3);

                createBoss(chanmenh4);
                createBoss(chanmenh5);
                createBoss(linhthu1);
                createBoss(linhthu2);
                createBoss(linhthu3);
                createBoss(linhthu4);
                createBoss(saophale);
                createBoss(danhhieu);
                createBoss(luuly);
                createBoss(keybac);
                createBoss(keyvang);
                createBoss(dabaove);
                createBoss(danangcap);
//                createBoss(TROMNGOC);
//                createBoss(TROMNGOCRUBY);
                createBoss(NOEN);
                createBoss(THANHUYDIET);
                 createBoss(THIENSUWISH);
                 createBoss(champa);
                 createBoss(vasdos);
                  createBoss(superkhi);
                  createBoss(panchi);
                  createBoss(babyvegeta);
                

                for (Map map : Manager.MAPS) {
                    if (map != null && !map.zones.isEmpty()) {
                        if (!map.isMapOffline && map.type == ConstMap.MAP_NORMAL
                                && map.tileId > 0 && !MapService.gI().isMapVS(map.mapId)) {
                            if (map.mapWidth > 50 && map.mapHeight > 50) {
                                if (Manager.EVENT_SEVER == ConstEvent.SU_KIEN_20_11) {
                                    //          new HoaHong(map.mapId);
                                }
                                if (Manager.EVENT_SEVER == ConstEvent.SU_KIEN_NOEL) {
                                    //          new SantaClaus(map.mapId);
                                }
                            }
                        }
                    }
                }
                if (Manager.EVENT_SEVER == ConstEvent.SU_KIEN_TET) {
                    for (int mapID : MAP_APPEARED_QILIN) {
                        //   new Qilin(mapID);
                    }
                }
            } catch (Exception e) {
                logger.error("Err initboss", e);
            }
        }).start();
    }

    public static Boss createBoss(int bossId) {
        Boss boss = null;
        switch (bossId) {

            
            
             case champa:
                boss = new champa();
                break;
               case panchi:
                boss = new panchi();
                break;  
                case babyvegeta:
                boss = new babyvegeta();
                break; 
                 case superkhi:
                boss = new superkhi();
                break;
                
             case vasdos:
                boss = new vasdos();
                break;
            case BOSS_TRUNGQUY1:
                boss = new BOSS_TRUNGQUY1();
                break;
            case BOSS_TRUNGQUY2:
                boss = new BOSS_TRUNGQUY2();
                break;
            case BOSS_TRUNGQUY3:
                boss = new BOSS_TRUNGQUY3();
                break;
            case BOSS_THOIVANG:
                boss = new BOSS_THOIVANG();
                break;
            case BOSS_DAUGOD:
                boss = new BOSS_DAUGOD();
                break;
            case Bido:
                boss = new Bido();
                break;
            case Bojack:
                boss = new Bojack();
                break;
            case Zangya:
                boss = new Zangya();
                break;
            case SiêuBojack:
                boss = new SiêuBojack();
                break;
            case Bujin:
                boss = new Bujin();
                break;
            case Kogu:
                boss = new Kogu();
                break;
            case TATHAN:
                boss = new TATHAN();
                break;
            case Luffy:
                boss = new Luffy();
                break;
            case Zoro:
                boss = new Zoro();
                break;
            case Sanji:
                boss = new Sanji();
                break;
            case Brook:
                boss = new Brook();
                break;
            case Chopper:
                boss = new Chopper();
                break;
            case Nami:
                boss = new Nami();
                break;
            case Franky:
                boss = new Franky();
                break;
            case Usopp:
                boss = new Usopp();
                break;
            case Robin:
                boss = new Robin();
                break;
            case HaoThienKhuyen:
                boss = new HaoThienKhuyen();
                break;
            case Panda:
                boss = new Panda();
                break;
            case DaThu1:
                boss = new DaThu1();
                break;
            case DaThu2:
                boss = new DaThu2();
                break;
            case DaThu3:
                boss = new DaThu3();
                break;
            case DaThu4:
                boss = new DaThu4();
                break;
            case DaThu5:
                boss = new DaThu5();
                break;
            case DaThu6:
                boss = new DaThu6();
                break;
            case DaThu7:
                boss = new DaThu7();
                break;
            case DaThu8:
                boss = new DaThu8();
                break;
            case Fide1:
                boss = new Fide1();
                break;
            case Fide2:
                boss = new Fide2();
                break;
            case Fide3:
                boss = new Fide3();
                break;
            case Hashibira:
                boss = new Hashibira();
                break;
            case Inosuke:
                boss = new Inosuke();
                break;
            case Tanjiro:
                boss = new Tanjiro();
                break;
            case Blackgoku:
                boss = new Blackgoku();
                break;
            case SuperBlackgoku:
                boss = new SuperBlackgoku();
                break;
            case VegetaSS1:
                boss = new VegetaSS1();
                break;
            case SieuXen:
                boss = new SieuXen();
                break;
            case ColerVang:
                boss = new ColerVang();
                break;
            case obito:
                boss = new obito();
                break;
            case naruto:
                boss = new naruto();
                break;
            case SUPERBROLY:
                boss = new SuperBroly();
                break;
            case chanmenh1:
                boss = new chanmenh1();
                break;
            case chanmenh2:
                boss = new chanmenh2();
                break;
            case chanmenh3:
                boss = new chanmenh3();
                break;

            case chanmenh4:
                boss = new chanmenh4();
                break;
            case chanmenh5:
                boss = new chanmenh5();
                break;
            case linhthu1:
                boss = new linhthu1();
                break;
            case linhthu2:
                boss = new linhthu2();
                break;
            case linhthu3:
                boss = new linhthu3();
                break;
            case linhthu4:
                boss = new linhthu4();
                break;

            case saophale:
                boss = new saophale();
                break;
            case danhhieu:
                boss = new danhhieu();
                break;
            case luuly:
                boss = new luuly();
                break;

            case keybac:
                boss = new keybac();
                break;
            case keyvang:
                boss = new keyvang();
                break;
            case dabaove:
                boss = new dabaove();
                break;
            case danangcap:
                boss = new danangcap();
                break;
            case TROMNGOC:
                boss = new TromNgoc();
                break;
            case TROMNGOCRUBY:
                boss = new TromNgocRuby();
                break;
            case NOEN:
                boss = new TromVang();
                break;
                case THANHUYDIET:
                boss = new Thanhuydiet();
                break;
              case THIENSUWISH:
                boss = new Thiensuwhis();
                break;      
                
                

        }
        return boss;
    }

}
