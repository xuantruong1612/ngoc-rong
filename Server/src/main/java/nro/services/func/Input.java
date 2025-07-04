package nro.services.func;

import nro.consts.ConstNpc;
import nro.jdbc.daos.PlayerDAO;
import nro.models.item.Item;
import nro.models.map.Zone;
import nro.models.npc.Npc;
import nro.models.npc.NpcManager;
import nro.models.player.Player;
import nro.server.Client;
import nro.server.io.Message;
import nro.services.*;

import java.util.HashMap;
import java.util.Map;
import nro.jdbc.daos.AccountDAO;
import nro.models.Bot.NewBot;
import nro.models.Bot.ShopBot;
import nro.models.item.ItemOption;
import static nro.models.npc.NpcFactory.PLAYERID_OBJECT;
import nro.models.player.Inventory;
import nro.server.TaiXiu;
import nro.utils.TimeUtil;
import nro.utils.Util;

/**
 * @Config Truong
 */
public class Input {

    private static final Map<Integer, Object> PLAYER_ID_OBJECT = new HashMap<Integer, Object>();
    public static final int SUB_CS = 626;
    public static final int CHANGE_PASSWORD = 500;
    public static final int GIFT_CODE = 501;
    public static final int CHANGE_NAME_BY_ITEM = 506;
    public static final int FIND_PLAYER = 502;
    public static final int CHANGE_NAME = 503;
    public static final int CHOOSE_LEVEL_BDKB = 5066;
    public static final int CHOOSE_LEVEL_CDRD = 7700;
    public static final int TANG_NGOC_HONG = 505;
    public static final int ADD_ITEM = 506;
    public static final int buffmoney = 50123336;
    public static final int TRADE_RUBY = 508;
    public static final int BUFF_ITEM_VIP = 50111236;
    public static final int TAI_taixiu = 323508;
    public static final int XIU_taixiu = 323505;
    public static final int SEND_ITEM_OP = 507;
    public static final int TAI = 510;
    public static final int XIU = 511;
    public static final int GIVE_CS = 625;
    public static final int phieutanghongngoc = 2002;
    public static final int tangdiem = 1265;
    static final int tangdiemvip = 1266;

    public static final int THUITEM = 632;
    public static final int QUY_DOI_COIN = 52208;
    public static final int QUY_DOI_COIN_1 = 5022229;
    public static final int CHATALL = 5211;
    public static final int DOILUULY = 67327;
    public static final int doithoivang = 67329;
    public static final int doingocxanh = 67331;
    public static final int doihongngoc = 67332;
    public static final int doidaugod = 67333;

    public static final int doivnd = 67328;
    public static final int BUFF_CAP_TAMKJLL = 513;
    public static final int CHECK_INFO = 518;
    public static final int MO_KHOA_ACC = 514;
    public static final int BUFF_CAP_LANG_BA = 515;
    public static final int BUFF_CAP_THO_MO = 516;
    public static final int BUFF_EXP_TAMKJLL = 517;
    public static final int BUFF_SAO_THIEN_PHU = 521;
    public static final int BUFFVND = 522;
    public static final int BUFFTONGNAP = 529;
    public static final int BUFFNAPTUAN = 530;
    public static final int BUFFTHANG = 531;
    public static final int BUFFSHE = 532;

    public static final int CHUYEN_HONG_NGOC = 553;

    public static final int THIEN_MA_LENH = 524;

    public static final int QUY_DOI_HONG_NGOC = 509;
    public static final int SEND_ITEM = 512;

    public static final byte NUMERIC = 0;
    public static final byte ANY = 1;
    public static final byte PASSWORD = 2;
    public static final int tvsanghn = 246233333;
    public static final int hnsangtv = 244112333;

    public static final int BOTQUAI = 206783;

    public static final int BOTITEM = 206762;

    public static final int BOTBOSS = 2067683;
    private static Input intance;

    private Input() {

    }

    public static Input gI() {
        if (intance == null) {
            intance = new Input();
        }
        return intance;
    }

    public void doInput(Player player, Message msg) {
        try {
            Player pl = null;
            String[] text = new String[msg.reader().readByte()];
            for (int i = 0; i < text.length; i++) {
                text[i] = msg.reader().readUTF();
            }
            switch (player.iDMark.getTypeInput()) {
                case CHECK_INFO:
                    Player plcheck = Client.gI().getPlayer(text[0]);
                    if (plcheck != null) {
                        player.TamkjllNamePlayer = text[0];
                        Service.getInstance().sendThongBao(plcheck,
                                "Người chơi: " + player.name + " đang xem thông tin của bạn");
                        NpcService.gI().createMenuConMeo(player, ConstNpc.TamkjllCheckPlayer, -1,
                                "|1|Chúc Bạn Năm Mới An Khang\n"
                                + "|7|Thông tin của: " + plcheck.name
                                + "\nhãy chọn các menu bên dưới để check",
                                "Thông tin\nnhân vật", "Thông tin\nđệ tử", "Thông tin\nKhác",
                                "chỉ số đồ mặc\nô 1", "chỉ số đồ mặc\nô 2", "chỉ số đồ mặc\nô 3",
                                "chỉ số đồ mặc\nô 4", "chỉ số đồ mặc\nô 5", "chỉ số đồ mặc\nô 6",
                                "chỉ số đồ mặc\nô 7", "chỉ số đồ mặc\nô 8", "chỉ số đồ mặc\nô 9",
                                "chỉ số đồ mặc\nô 10", "chỉ số đồ mặc\nô 11");
                    } else {
                        Service.getInstance().sendThongBao(player, "Người chơi không tồn tại hoặc đang offline");
                    }
                    break;

                case CHATALL:
                    String chat = text[0];
                    Service.gI().ChatAll(22713, "|7|[ - •⊹٭Ngọc Rồng Online THÔNG BÁO٭⊹• - ]" + "\n"
                            + "|1|" + (player.isAdmin() ? "KEY : " : player.isAdmin() ? "ADMIN : " : "") + chat);
                    break;

                case CHUYEN_HONG_NGOC:
                    Player pldcchuyen = Client.gI().getPlayer(text[0]);
                    int slhn = Integer.parseInt(text[1]);
                    if (pldcchuyen != null) {
                        if (player.CapTamkjll < 50) {
                            Service.getInstance().sendThongBao(player,
                                    "yêu cầu Tiên Bang ít nhất là 50 vs người chuyển");
                            return;
                        }
                        if (player.inventory.ruby < slhn || slhn < 10000 || slhn > 100000) {
                            Service.getInstance().sendThongBao(player,
                                    "làm thêm vài cái nx đi khóa acc cho :))\nNhập từ 10000 - 100000 hồng ngọc");
                            return;
                        }
                        if (player.ExpTamkjll < slhn * 500) {
                            Service.getInstance().sendThongBao(player,
                                    "Cần " + (slhn * 500) + " Exp Bún để chuyển " + slhn + " Hồng ngọc");
                            return;
                        }
                        player.ExpTamkjll -= slhn * 500;
                        player.inventory.ruby -= slhn;
                        pldcchuyen.inventory.ruby += slhn;
                        Service.getInstance().sendMoney(player);
                        Service.getInstance().sendMoney(pldcchuyen);
                        Service.getInstance().sendThongBao(player,
                                "Bạn đã chuyển thành công " + slhn + " Hồng ngọc cho " + pldcchuyen.name);
                        Service.getInstance().sendThongBao(pldcchuyen,
                                "Bạn đã nhận " + slhn + " Hồng ngọc từ " + player.name);
                    } else {
                        Service.getInstance().sendThongBao(player, "Người chơi không tồn tại hoặc đang offline");
                    }
                    break;

                case BOTITEM:
                    int slot = Integer.parseInt(text[0]);
                    int idBan = Integer.parseInt(text[1]);
                    int idTraoDoi = Integer.parseInt(text[2]);
                    int slot_TraoDoi = Integer.parseInt(text[3]);
                    ShopBot bs = new ShopBot(idBan, idTraoDoi, slot_TraoDoi);
                    new Thread(() -> {
                        NewBot.gI().runBot(1, bs, slot);
                    }).start();
                    break;
                case BOTBOSS:
                    slot = Integer.parseInt(text[0]);
                    new Thread(() -> {
                        NewBot.gI().runBot(2, null, slot);
                    }).start();
                    break;
                case BOTQUAI:
                    slot = Integer.parseInt(text[0]);
                    new Thread(() -> { // thread ở đây code thêm dừng thread a nhé
                        NewBot.gI().runBot(0, null, slot);
                    }).start();
                    break;
                case TAI:
                    int sotvxiu = Integer.valueOf(text[0]);
                    if (sotvxiu <= 0) {
                        Service.getInstance().sendThongBaoOK(player, "?");
                    } else {
                        TransactionService.gI().cancelTrade(player);
                        Item tv2 = null;
                        for (Item item : player.inventory.itemsBag) {
                            if (item.isNotNullItem() && item.template.id == 457) {
                                tv2 = item;
                                break;
                            }
                        }
                        try {
                            if (tv2 != null && tv2.quantity >= sotvxiu) {
                                InventoryService.gI().subQuantityItemsBag(player, tv2, sotvxiu);
                                InventoryService.gI().sendItemBags(player);
                                int TimeSeconds = 10;
                                Service.getInstance().sendThongBao(player, "Chờ 10 giây để biết kết quả.");
                                while (TimeSeconds > 0) {
                                    TimeSeconds--;
                                    Thread.sleep(1000);
                                }
                                int x = Util.nextInt(1, 6);
                                int y = Util.nextInt(1, 6);
                                int z = Util.nextInt(1, 6);
                                int tong = (x + y + z);
                                if (tong <= 10) {
                                    if (player != null) {

                                        int wintai457 = (int) (sotvxiu * 1.8);
                                        Item tvthang = ItemService.gI().createNewItem((short) 457, wintai457);
                                        InventoryService.gI().addItemBag(player, tvthang, 9999999);
                                        InventoryService.gI().sendItemBags(player);
                                        Service.getInstance().sendThongBao(player, "Kết quả" + "\nSố hệ thống quay ra : " + x + " "
                                                + y + " " + z + "\nTổng là : " + tong + "\nBạn đã cược : " + sotvxiu
                                                + " thỏi vàng vào Xỉu" + "\nKết quả : Xỉu" + "\n\nVề bờ");
                                        return;
                                    }
                                } else if ((x + y + z) > 10) {
                                    if (player != null) {
                                        Service.getInstance().sendThongBao(player, "Kết quả" + "\nSố hệ thống quay ra là :"
                                                + " " + x + " " + y + " " + z + "\nTổng là : " + tong + "\nBạn đã cược : "
                                                + sotvxiu + " thỏi vàng vào Xỉu" + "\nKết quả : Tài" + "\nCòn cái nịt.");
                                        return;
                                    }
                                }
                            } else {
                                Service.getInstance().sendThongBao(player, "Bạn không đủ thỏi vàng để chơi.");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Service.getInstance().sendThongBao(player, "Lỗi.");
                        }
                    }
                    break;
                case XIU:
                    int sotvtai = Integer.valueOf(text[0]);
                    if (sotvtai <= 0) {
                        Service.getInstance().sendThongBao(player, "?");
                    } else {
                        TransactionService.gI().cancelTrade(player);
                        Item tv1 = null;

                        for (Item item : player.inventory.itemsBag) {
                            if (item.isNotNullItem() && item.template.id == 457) {
                                tv1 = item;
                                break;
                            }
                        }
                        try {
                            if (tv1 != null && tv1.quantity >= sotvtai) {
                                InventoryService.gI().subQuantityItemsBag(player, tv1, sotvtai);
                                InventoryService.gI().sendItemBags(player);
                                int TimeSeconds = 10;
                                Service.getInstance().sendThongBao(player, "Chờ 10 giây để biết kết quả.");
                                while (TimeSeconds > 0) {
                                    TimeSeconds--;
                                    Thread.sleep(1000);
                                }
                                int x = Util.nextInt(1, 6);
                                int y = Util.nextInt(1, 6);
                                int z = Util.nextInt(1, 6);
                                int tong = (x + y + z);
                                if (tong <= 10) {
                                    if (player != null) {
                                        Service.getInstance().sendThongBao(player, "Kết quả" + "\nSố hệ thống quay ra là :"
                                                + " " + x + " " + y + " " + z + "\nTổng là : " + tong + "\nBạn đã cược : "
                                                + sotvtai + " thỏi vàng vào Tài" + "\nKết quả : Xỉu" + "\nMày Mất " + sotvtai + " Thòi Vàng");
                                        return;
                                    }
                                } else {
                                    if (player != null) {
                                        int winxiu457 = (int) (sotvtai * 1.8);
                                        Item tvthang = ItemService.gI().createNewItem((short) 457, winxiu457);

                                        InventoryService.gI().addItemBag(player, tvthang, 9999999);
                                        InventoryService.gI().sendItemBags(player);
                                        Service.getInstance().sendThongBao(player, "Kết quả" + "\nSố hệ thống quay ra : " + x + " "
                                                + y + " " + z + "\nTổng là : " + tong + "\nBạn đã cược : " + sotvtai
                                                + " thỏi vàng vào Tài" + "\nKết quả : Tài" + "\n\nVề bờ");
                                        return;
                                    }
                                }
                            } else {
                                Service.getInstance().sendThongBao(player, "Bạn không đủ thỏi vàng để chơi.");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Service.getInstance().sendThongBao(player, "Lỗi.");
                        }
                    }
                    break;

                case BUFF_CAP_TAMKJLL: {
                    Player plb = Client.gI().getPlayer(text[0]);
                    int CCB = Integer.parseInt(text[1]);
                    if (player.isAdmin()) {
                        if (plb != null) {
                            if (CCB > 20000) {
                                Service.getInstance().sendThongBao(player, "Buff ngu vừa thôi lồn ngu");
                                return;
                            }
                            Service.getInstance().sendThongBao(player, "Đã buff: " + CCB + " cấp Tiên Bang cho " + plb.name);
                            Service.getInstance().sendThongBao(plb, "Được ADMIN: " + plb.name + " cho cấp Tiên Bang: " + CCB);
                            plb.CapTamkjll += CCB;
                            if (plb.CapTamkjll > 10000) {
                                plb.CapTamkjll = 10000;
                            }
                        } else {
                            Service.getInstance().sendThongBao(player, "Ko Onl");
                        }
                    } else {
                        PlayerService.gI().banPlayer(player);
                    }
                    break;
                }
                case BUFF_CAP_LANG_BA: {
                    Player plb = Client.gI().getPlayer(text[0]);
                    int CCB = Integer.parseInt(text[1]);
                    if (player.isAdmin()) {
                        if (plb != null) {
                            if (CCB > 10000) {
                                Service.getInstance().sendThongBao(player, "Buff ngu vừa thôi lồn ngu");
                                return;
                            }
                            Service.getInstance().sendThongBao(player, "Đã buff: " + CCB + " cấp Nhập Ma cho " + plb.name);
                            Service.getInstance().sendThongBao(plb, "Được ADMIN: " + plb.name + " cho cấp Nhập Ma: " + CCB);
                            plb.LbTamkjll += CCB;
                            if (plb.LbTamkjll > 10000) {
                                plb.LbTamkjll = 10000;
                            }
                        } else {
                            Service.getInstance().sendThongBao(player, "Ko Onl");
                        }
                    } else {
                        PlayerService.gI().banPlayer(player);
                    }
                    break;
                }

                case BUFF_CAP_THO_MO: {
                    Player plb = Client.gI().getPlayer(text[0]);
                    int CCB = Integer.parseInt(text[1]);
                    if (player.isAdmin()) {
                        if (plb != null) {
                            if (CCB > 200000) {
                                Service.getInstance().sendThongBao(player, "Buff ngu vừa thôi lồn ngu");
                                return;
                            }
                            Service.getInstance().sendThongBao(player, "Đã buff: " + CCB + " cấp Khai Thác cho " + plb.name);
                            Service.getInstance().sendThongBao(plb, "Được ADMIN: " + plb.name + " cho cấp Khai Thác: " + CCB);
                            plb.TamkjllThomo += CCB;
                        } else {
                            Service.getInstance().sendThongBao(player, "Ko Onl");
                        }
                    } else {
                        PlayerService.gI().banPlayer(player);
                    }
                    break;
                }
                case BUFF_EXP_TAMKJLL: {
                    Player plb = Client.gI().getPlayer(text[0]);
                    long CCB = Long.parseLong(text[1]);
                    if (player.isAdmin()) {
                        if (plb != null) {
                            if (CCB > 2000000000) {
                                Service.getInstance().sendThongBao(player, "Tối đa 2000tr");
                                return;
                            }
                            Service.getInstance().sendThongBao(player, "Đã buff: " + CCB + " Exp Tamkjll cho " + plb.name);
                            Service.getInstance().sendThongBao(plb, "Được ADMIN: " + plb.name + " cho Exp Tamkjll: " + CCB);
                            plb.ExpTamkjll += CCB;
                        } else {
                            Service.getInstance().sendThongBao(player, "Ko Onl");
                        }
                    } else {
                        PlayerService.gI().banPlayer(player);
                    }
                    break;
                }
                case BUFF_SAO_THIEN_PHU: {
                    Player plb = Client.gI().getPlayer(text[0]);
                    int CCB = Integer.parseInt(text[1]);
                    if (player.isAdmin()) {
                        if (plb != null) {
                            if (CCB > 50) {
                                Service.getInstance().sendThongBao(player, "Buff ngu vừa thôi lồn ngu");
                                return;
                            }
                            Service.getInstance().sendThongBao(player, "Đã buff: " + CCB + " sao cho " + plb.name);
                            Service.getInstance().sendThongBao(plb,
                                    "Được ADMIN: " + plb.name + " cho " + CCB + " Sao thiên phú");
                            plb.Tamkjlltutien[2] += CCB;
                            if (plb.Tamkjlltutien[2] > 50) {
                                plb.Tamkjlltutien[2] = 50;
                            }
                        } else {
                            Service.getInstance().sendThongBao(player, "Ko Onl");
                        }
                    } else {
                        PlayerService.gI().banPlayer(player);
                    }
                    break;
                }

                case GIVE_CS:
                    Player phucml = ((Player) PLAYERID_OBJECT.get(player.id));
                    if (phucml != null) {
                        long SM = Long.parseLong(text[0]);
                        long TN = Long.parseLong(text[1]);
                        int SD = Integer.parseInt(text[2]);
                        int HPKI = Integer.parseInt(text[3]);
                        int CRIT = Integer.parseInt(text[4]);
                        phucml.pet.nPoint.tiemNangUp(+TN);
                        phucml.pet.nPoint.powerUp(+SM);
                        phucml.pet.nPoint.dameg += SD;
                        phucml.pet.nPoint.mpg += HPKI;
                        phucml.pet.nPoint.hpg += HPKI;
                        phucml.pet.nPoint.critg += CRIT;
                        if (phucml.pet.nPoint.dameg >= 1) {
                            phucml.pet.nPoint.dameg = 2000000000;
                        }
                        if (phucml.pet.nPoint.hpg >= 1 && phucml.pet.nPoint.mpg >= 2000000000) {
                            phucml.pet.nPoint.hpg = 2000000000;
                        }
                        phucml.pet.nPoint.mpg = 2000000000;
                        if (phucml.pet.nPoint.critg >= 1) {
                            phucml.pet.nPoint.critg = 100;
                        }
                        if (phucml.pet.nPoint.power >= 1) {
                            phucml.pet.nPoint.power = 2000000000000000000l;
                        }

                        Service.getInstance().sendThongBaoFromAdmin(player, "|7|[ - BUFF CHỈ SỐ ĐỆ TỬ - ]\n" + "|2|Đến Player : " + ((Player) PLAYERID_OBJECT.get(player.id)).name
                                + "\nPlayer Pet : " + phucml.pet.name
                                + "\nSỨC MẠNH ĐÃ BUFF : " + SM + " SM" + " | SAU KHI BUFF : " + phucml.pet.nPoint.power
                                + "\nTIỀM NĂNG ĐÃ BUFF : " + TN + " TN" + " | SAU KHI BUFF : " + phucml.pet.nPoint.tiemNang
                                + "\nSỨC ĐÁNH ĐÃ BUFF : " + SD + " SDG" + " | SAU KHI BUFF : " + phucml.pet.nPoint.dame
                                + "\nHP, KI ĐÃ BUFF : " + HPKI + " HPKIG" + " | SAU KHI BUFF : " + phucml.pet.nPoint.hp + ", " + phucml.pet.nPoint.mp
                                + "\nCHÍ MẠNG ĐÃ BUFF : " + CRIT + " CM" + " | SAU KHI BUFF : " + phucml.pet.nPoint.crit
                                + "\n(Thời gian thực hiện : " + TimeUtil.getTimeNow("dd/MM/yyyy HH:mm:ss") + ")" + "\n"
                                + "|7|SUCCESS!");
                        break;
                    } else {
                        Service.getInstance().sendThongBao(player, "Không online");
                    }
                    break;
                case SUB_CS:
                    if (((Player) PLAYERID_OBJECT.get(player.id)).pet != null) {
                        long SM = Long.parseLong(text[0]);
                        long TN = Long.parseLong(text[1]);
                        int SD = Integer.parseInt(text[2]);
                        int HPKI = Integer.parseInt(text[3]);
                        int CRIT = Integer.parseInt(text[4]);
                        ((Player) PLAYERID_OBJECT.get(player.id)).pet.nPoint.tiemNangUp(-TN);
                        ((Player) PLAYERID_OBJECT.get(player.id)).pet.nPoint.powerUp(-SM);
                        ((Player) PLAYERID_OBJECT.get(player.id)).pet.nPoint.dameg -= SD;
                        ((Player) PLAYERID_OBJECT.get(player.id)).pet.nPoint.mpg -= HPKI;
                        ((Player) PLAYERID_OBJECT.get(player.id)).pet.nPoint.hpg -= HPKI;
                        ((Player) PLAYERID_OBJECT.get(player.id)).pet.nPoint.critg -= CRIT;
                        Service.getInstance().sendThongBaoFromAdmin(player, "|7|[ - BUFF CHỈ SỐ ĐỆ TỬ - ]\n" + "|2|Đến Player : " + ((Player) PLAYERID_OBJECT.get(player.id)).name
                                + "\nPlayer Pet : " + ((Player) PLAYERID_OBJECT.get(player.id)).pet.name
                                + "\nSỨC MẠNH ĐÃ GIẢM : " + SM + " SM" + " | SAU KHI GIẢM : " + ((Player) PLAYERID_OBJECT.get(player.id)).pet.nPoint.power
                                + "\nTIỀM NĂNG ĐÃ GIẢM : " + TN + " TN" + " | SAU KHI GIẢM : " + ((Player) PLAYERID_OBJECT.get(player.id)).pet.nPoint.tiemNang
                                + "\nSỨC ĐÁNH ĐÃ GIẢM : " + SD + " SDG" + " | SAU KHI GIẢM : " + ((Player) PLAYERID_OBJECT.get(player.id)).pet.nPoint.dame
                                + "\nHP, KI ĐÃ GIẢM : " + HPKI + " HPKIG" + " | SAU KHI GIẢM : " + ((Player) PLAYERID_OBJECT.get(player.id)).pet.nPoint.hp + ", " + ((Player) PLAYERID_OBJECT.get(player.id)).pet.nPoint.mp
                                + "\nCHÍ MẠNG ĐÃ GIẢM : " + CRIT + " CM" + " | SAU KHI GIẢM : " + ((Player) PLAYERID_OBJECT.get(player.id)).pet.nPoint.crit
                                + "\n(Thời gian thực hiện : " + TimeUtil.getTimeNow("dd/MM/yyyy HH:mm:ss") + ")" + "\n"
                                + "|7|SUCCESS!");
                        break;
                    } else {
                        Service.getInstance().sendThongBao(player, "Không online");
                    }
                    break;

                case tvsanghn:
                    int nhapthoivang = Integer.parseInt(text[0]);
                    Item thoivang = InventoryService.gI().findItemBagByTemp(player, 457);

                    if (nhapthoivang > 0 && nhapthoivang < 500) {
                        if (thoivang != null) {
                            if (nhapthoivang < thoivang.quantity) {
                                player.inventory.ruby += nhapthoivang * 300;
                                Service.getInstance().sendThongBao(player, "Bạn đã quy đổi được " + nhapthoivang * 300 + " hồng ngọc");
                                InventoryService.gI().subQuantityItemsBag(player, thoivang, nhapthoivang);
                                InventoryService.gI().sendItemBags(player);
                                Service.getInstance().sendMoney(player);
                            } else {
                                Service.getInstance().sendThongBao(player, "Bạn không đủ thỏi vàng để quy đổi hồng ngọc");
                            }
                        } else {
                            Service.getInstance().sendThongBao(player, "Bạn không đủ thỏi vàng để quy đổi hồng ngọc");
                        }
                    } else {
                        Service.getInstance().sendThongBao(player, "Vượt quá giới hạn thỏi vàng quy đổi");
                    }
                    break;

                case QUY_DOI_COIN:
                    int goldTrade = Integer.parseInt(text[0]);
                    if (goldTrade % 1000 == 0) {
                        if (goldTrade <= 0 || goldTrade >= 1000001) {
                            Service.getInstance().sendThongBao(player, "|7|Quá giới hạn mỗi lần tối đa 1.000.000");
                        } else if (player.getSession().vnd >= goldTrade) {
                            if (goldTrade >= 500000) {
                                Item thehongngoc = null;
                                thehongngoc = InventoryService.gI().findItemBagByTemp(player, 2002);
                                Item hongngoc = ItemService.gI().createNewItem((short) 861, goldTrade);
                                InventoryService.gI().addItemBag(player, hongngoc, 99999999);
                                if (thehongngoc != null) {
                                    Service.getInstance().sendThongBao(player, "|3|Bạn đã có Vé tặng ngọc rồi nên không nhận được nữa !!");
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + Util.format(goldTrade)//* ratioGold * 2
                                            + " " + (hongngoc.template.name));
                                } else {
//                                    Item thehn = ItemService.gI().createNewItem((short) 2002, 1);
//                                    InventoryService.gI().addItemBag(player, thehn, 8888);
//                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + Util.format(goldTrade)//* ratioGold * 2
//                                            + " " + (hongngoc.template.name) + " và 1 Vé tặng hồng ngọc");
                                }
                                InventoryService.gI().sendItemBags(player);
                            } else {
                                Item thoiVang = ItemService.gI().createNewItem((short) 861, goldTrade);
                                InventoryService.gI().addItemBag(player, thoiVang, 999);
                                InventoryService.gI().sendItemBags(player);
                            }
                            PlayerDAO.subVND(player, goldTrade);
                        } else {
                            Service.getInstance().sendThongBao(player, "|7|Số tiền của bạn là " + player.getSession().vnd + " không đủ để quy "
                                    + " đổi " + goldTrade + " Hồng ngọc " + " " + "bạn cần thêm " + (player.getSession().vnd - goldTrade));
                        }
                    } else {
                        Service.getInstance().sendThongBao(player, "|7|Số tiền nhập phải là bội số của 1000");
                    }
                    break;
                case QUY_DOI_COIN_1:
                    int goldTrade1 = Integer.parseInt(text[0]);
                    int heso = 3;
                    if (goldTrade1 % 1000 == 0) {
                        if (goldTrade1 <= 0 || goldTrade1 >= 1000001) {
                            Service.getInstance().sendThongBao(player, "|7|Quá giới hạn mỗi lần tối đa 1.000.000");
                        } else if (player.getSession().vnd >= goldTrade1) {
                            if (goldTrade1 >= 500000) {
                                Item thehongngoc = null;
                                thehongngoc = InventoryService.gI().findItemBagByTemp(player, 2002);
                                Item thoiVang = ItemService.gI().createNewItem((short) 457, (goldTrade1 * heso / 1000));
                                InventoryService.gI().addItemBag(player, thoiVang, 9999);
                                InventoryService.gI().sendItemBags(player);
                                if (thehongngoc != null) {
                                    Service.getInstance().sendThongBao(player, "|3|Bạn đã có Vé tặng ngọc rồi nên không nhận được nữa !!");
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + Util.format((goldTrade1 * heso / 1000))
                                            + " " + (thoiVang.template.name));
                                } else {
////                                    Item thehn = ItemService.gI().createNewItem((short) 2002, 1);
//                                    InventoryService.gI().addItemBag(player, thehn, 999);
//                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + Util.format((goldTrade1 * heso / 1000))
//                                            + " " + (thoiVang.template.name) + " và 1 Vé tặng hồng ngọc");
                                }
                            } else {
                                Item thoiVang = ItemService.gI().createNewItem((short) 457, (goldTrade1 * heso / 1000));
                                InventoryService.gI().addItemBag(player, thoiVang, 999);
                                InventoryService.gI().sendItemBags(player);
                                Service.getInstance().sendThongBao(player, "Bạn nhận được " + Util.format((goldTrade1 * heso / 1000))
                                        + " " + thoiVang.template.name);
                            }
                            PlayerDAO.subVND(player, goldTrade1);
                        } else {
                            Service.getInstance().sendThongBao(player, "|7|Số tiền của bạn là " + player.getSession().vnd + " không đủ để quy đổi " + goldTrade1 / 1000 + " Thỏi vàng" + " " + "bạn cần thêm " + (player.getSession().vnd - goldTrade1));
                        }
                    } else {
                        Service.getInstance().sendThongBao(player, "|7|Số tiền nhập phải là bội số của 1000");
                    }
                    break;
                case hnsangtv:
                    int nhapthoivang1 = Integer.parseInt(text[0]);
                    if (nhapthoivang1 > 0 && nhapthoivang1 < 500) {
                        if ((nhapthoivang1 * 400) < player.inventory.ruby) {
                            player.inventory.ruby -= nhapthoivang1 * 400;
                            Item tv = ItemService.gI().createNewItem((short) 457, nhapthoivang1);
                            Service.getInstance().sendThongBao(player, "Bạn đã quy đổi được " + nhapthoivang1 + " thỏi vàng");
                            InventoryService.gI().addItemBag(player, tv, 999999);
                            Service.getInstance().sendMoney(player);
                            InventoryService.gI().sendItemBags(player);
                        } else {
                            Service.getInstance().sendThongBao(player, "Bạn không đủ hồng ngọc để quy đổi thỏi vàng");
                        }
                    } else {
                        Service.getInstance().sendThongBao(player, "Vượt quá số lượng cho phép");
                    }
                    break;
                case CHANGE_PASSWORD:
                    Service.getInstance().changePassword(player, text[0], text[1], text[2]);
                    break;
                case GIFT_CODE:
                    GiftService.gI().use(player, text[0]);

                    break;

                case FIND_PLAYER:
                    pl = Client.gI().getPlayer(text[0]);
                    if (pl != null) {
                        NpcService.gI().createMenuConMeo(player, ConstNpc.MENU_FIND_PLAYER, -1, "Ngài muốn..?",
                                new String[]{"Đi tới\n" + pl.name, "Gọi " + pl.name + "\ntới đây", "Đổi tên", "Ban"},
                                pl);
                    } else {
                        Service.getInstance().sendThongBao(player, "Người chơi không tồn tại hoặc đang offline");
                    }
                    break;
                case phieutanghongngoc:
                    Player plonline = Client.gI().getPlayer(text[0]);
                    int sohongngoc = Integer.parseInt(text[1]);
                    if (plonline != null) {
                        if (sohongngoc > 0 && sohongngoc <= 1000000) {
                            if (sohongngoc <= player.inventory.ruby) {
                                player.inventory.ruby -= sohongngoc;
                                plonline.inventory.ruby += sohongngoc;
                                Service.getInstance().sendThongBao(player, "Bạn đã tặng cho " + plonline + " " + sohongngoc + " hồng ngọc");
                            } else {
                                Service.getInstance().sendThongBao(player, "Bạn không đủ hồng ngọc để tặng");

                            }
                        } else {
                            Service.getInstance().sendThongBao(player, "Chỉ có thể chuyển tối đa 1.000.000 hồng ngọc");
                        }
                    } else {
                        Service.getInstance().sendThongBao(player, "Người chơi không tồn tại hoặc đang offline");
                    }
                    break;

                case tangdiem:
                    Player plonline1 = Client.gI().getPlayer(text[0]);

                    // Kiểm tra xem có thể chuyển đổi giá trị sang int
                    int sohongngoc1;
                    try {
                        sohongngoc1 = Integer.parseInt(text[1]);
                    } catch (NumberFormatException e) {
                        Service.getInstance().sendThongBao(player, "Giá trị Điểm Farm không hợp lệ.");
                        break;
                    }

                    if (plonline1 != null) {
                        // Kiểm tra xem người chuyển có Điểm Farm không
                        if (player.diemfam <= 0) {
                            break; // Ngừng thực hiện nếu không đủ điểm
                        }

                        if (sohongngoc1 > 0 && sohongngoc1 <= 1000000) {
                            if (sohongngoc1 <= player.diemfam) { // Kiểm tra đủ điểm để tặng
                                player.diemfam -= sohongngoc1;
                                plonline1.diemfam += sohongngoc1;
                                Service.getInstance().sendThongBao(player, "Bạn đã tặng cho " + plonline1.name + " " + sohongngoc1 + " Điểm Farm.");
                            } else {
                                Service.getInstance().sendThongBao(player, "Bạn không đủ Điểm Farm để tặng.");
                            }
                        } else {
                            Service.getInstance().sendThongBao(player, "Chỉ có thể chuyển tối đa 1000.000 Điểm Farm.");
                        }
                    } else {
                        Service.getInstance().sendThongBao(player, "Người chơi không tồn tại hoặc đang offline.");
                    }
                    break;

                case tangdiemvip:
                    Player plonline2 = Client.gI().getPlayer(text[0]);

                    // Kiểm tra xem có thể chuyển đổi giá trị sang int
                    int sohongngoc2;
                    try {
                        sohongngoc2 = Integer.parseInt(text[1]);
                    } catch (NumberFormatException e) {
                        Service.getInstance().sendThongBao(player, "Giá trị Điểm VIP không hợp lệ.");
                        break;
                    }

                    if (plonline2 != null) {
                        // Kiểm tra xem người chuyển có đủ điểm VIP không
                        if (player.Diemvip <= 0) {
                            break; // Ngừng thực hiện nếu không đủ điểm
                        }

                        if (sohongngoc2 > 0 && sohongngoc2 <= 10000000) {
                            if (sohongngoc2 <= player.Diemvip) { // Kiểm tra đủ điểm để tặng
                                player.Diemvip -= sohongngoc2;
                                plonline2.Diemvip += sohongngoc2;
                                Service.getInstance().sendThongBao(player, "Bạn đã tặng cho " + plonline2.name + " " + sohongngoc2 + " Điểm VIP.");
                            } else {
                                Service.getInstance().sendThongBao(player, "Bạn không đủ Điểm VIP để tặng.");
                            }
                        } else {
                            Service.getInstance().sendThongBao(player, "Chỉ có thể chuyển tối đa 10.000.000 Điểm VIP.");
                        }
                    } else {
                        Service.getInstance().sendThongBao(player, "Người chơi không tồn tại hoặc đang offline.");
                    }
                    break;

                case buffmoney:
                    if (player.isAdmin()) {
                        Player playeronline = Client.gI().getPlayer(text[0]);
                        int sotien = Integer.parseInt(text[1]);
                        if (playeronline != null) {
                            PlayerDAO.congiten(playeronline, sotien);
                            Service.getInstance().sendThongBaoOK(player, "Bạn đã cộng" + sotien + " cho người chơi");
                            Service.getInstance().sendThongBaoOK(playeronline, "Bạn đã nhận được: " + sotien + " vnd");
                        } else {
                            Service.getInstance().sendThongBao(player, "Player không online");
                        }

                    }
                    break;
                case TAI_taixiu:
                    int sotvxiu1 = Integer.valueOf(text[0]);
                    try {
                        if (sotvxiu1 >= 1000 && sotvxiu1 <= 1000000) {
                            if (player.inventory.ruby >= sotvxiu1) {
                                player.inventory.ruby -= sotvxiu1;
                                player.goldTai += sotvxiu1;
                                player.taixiu.toptaixiu += sotvxiu1;
                                TaiXiu.gI().goldTai += sotvxiu1;
                                Service.getInstance().sendThongBao(player, "Bạn đã đặt " + Util.format(sotvxiu1) + " Hồng ngọc vào TÀI");
                                TaiXiu.gI().addPlayerTai(player);
                                InventoryService.gI().sendItemBags(player);
                                Service.getInstance().sendMoney(player);
                            } else {
                                Service.getInstance().sendThongBao(player, "Bạn không đủ Hồng ngọc để chơi.");
                            }
                        } else {
                            Service.getInstance().sendThongBao(player, "Cược ít nhất 10.000 Hồng ngọc.");
                        }
                    } catch (NumberFormatException e) {
                        Service.getInstance().sendThongBao(player, "Số tiền cược không hợp lệ.");
                    } catch (Exception e) {
                        e.printStackTrace();
                        Service.getInstance().sendThongBao(player, "Đã xảy ra lỗi khi xử lý cược.");
                        System.out.println("Lỗi khi xử lý cược: " + e.getMessage());
                    }
                    break;

                case XIU_taixiu:
                    int sotvxiu2 = Integer.valueOf(text[0]);
                    try {
                        if (sotvxiu2 >= 1000 && sotvxiu2 <= 1000000) {
                            if (player.inventory.ruby >= sotvxiu2) {
                                player.inventory.ruby -= sotvxiu2;
                                player.goldXiu += sotvxiu2;
                                player.taixiu.toptaixiu += sotvxiu2;
                                TaiXiu.gI().goldXiu += sotvxiu2;
                                Service.getInstance().sendThongBao(player, "Bạn đã đặt " + Util.format(sotvxiu2) + " Hồng ngọc vào XỈU");
                                TaiXiu.gI().addPlayerXiu(player);
                                InventoryService.gI().sendItemBags(player);
                                Service.getInstance().sendMoney(player);

                            } else {
                                Service.getInstance().sendThongBao(player, "Bạn không đủ Hồng ngọc để chơi.");
                            }
                        } else {
                            Service.getInstance().sendThongBao(player, "Cược ít nhất 20.000 - 1.000.000 Hồng ngọc ");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Service.getInstance().sendThongBao(player, "Lỗi.");
                        System.out.println("nnnnn2  ");
                    }
                    break;
                case CHANGE_NAME:
                    if (player.diemfam >= 100000) {
                        if (player != null) {
                            if (PlayerDAO.isExistName(text[0])) {
                                Service.getInstance().sendThongBao(player, "Tên nhân vật đã tồn tại");
                            } else {
                                player.name = text[0];
                                PlayerDAO.saveName(player);
                                player.diemfam -= 100000;
                                Service.getInstance().player(player);
                                Service.getInstance().Send_Caitrang(player);
                                Service.getInstance().sendFlagBag(player);
                                Zone zone = player.zone;
                                ChangeMapService.gI().changeMap(player, zone, player.location.x, player.location.y);
                                Service.getInstance().sendThongBao(player, "Chúc mừng bạn đã có cái tên mới đẹp hơn tên ban đầu");
                                Service.getInstance().sendThongBao(player, "Đổi tên người chơi thành công");
                            }
                        } else {
                            Service.getInstance().sendThongBaoFromAdmin(player, "Không đủ 100k Điểm Farm !");
                        }
                    }
                    break; // *** Đảm bảo có break để kết thúc case **

                case BUFFVND: {
                    Player plb = Client.gI().getPlayer(text[0]);
                    int CCB = Integer.parseInt(text[1]);
                    if (player.isAdmin()) {
                        if (plb != null) {
                            if (CCB <= 0 || CCB > 10000000) {
                                Service.getInstance().sendThongBao(player, "Buff ngu vừa thôi lồn ngu");
                                return;
                            }
                            Service.getInstance().sendThongBao(player, "Đã buff: " + Util.format(CCB) + " VND cho " + plb.name);
                            Service.getInstance().sendThongBaoFromAdmin(plb, "|7|\n" + "Mày Được ADMIN: " + plb.name + " \nTốt Bụng cho: " + CCB + " VND\n" + "|4|\n" + "Lần Sau Nạp To To Vào Nhé");
                            plb.getSession().vnd += CCB;
                            AccountDAO.updateVND(player.getSession());
                            if (CCB > 10000000) {
                                plb.getSession().vnd += 10000000;
                                AccountDAO.updateVND(player.getSession());
                            }
                        } else {
                            Service.getInstance().sendThongBao(player, "Ko Onl");
                        }
                    } else {
                        PlayerService.gI().banPlayer(player);
                    }
                    break;
                }

                case BUFFNAPTUAN: {
                    Player plonline3 = Client.gI().getPlayer(text[0]);

                    // Kiểm tra xem có thể chuyển đổi giá trị sang int
                    int sohongngoc3;
                    try {
                        sohongngoc3 = Integer.parseInt(text[1]);
                    } catch (NumberFormatException e) {
                        Service.getInstance().sendThongBao(player, "Giá trị Điểm VIP không hợp lệ.");
                        break;
                    }

                    if (plonline3 != null) {
                        // Kiểm tra xem người chuyển có đủ điểm VIP không
                        if (player.naptuan <= 0) {
                            break; // Ngừng thực hiện nếu không đủ điểm
                        }

                        if (sohongngoc3 > 0 && sohongngoc3 <= 100000000) {
                            if (sohongngoc3 <= player.naptuan) { // Kiểm tra đủ điểm để tặng
                                player.naptuan -= sohongngoc3;
                                plonline3.naptuan += sohongngoc3;
                                Service.getInstance().sendThongBaoFromAdmin(player, "Bạn đã Cộng cho " + plonline3.name + " " + sohongngoc3 + " Điểm Nạp Tuần.");
                            } else {
                                Service.getInstance().sendThongBao(player, "Bạn không đủ Điểm Nạp để tặng.");
                            }
                        } else {
                            Service.getInstance().sendThongBao(player, "Chỉ có thể chuyển tối đa 10.000.000 Điểm Nạp.");
                        }
                    } else {
                        Service.getInstance().sendThongBao(player, "Người chơi không tồn tại hoặc đang offline.");
                    }
                    break;

                }

                case BUFFTHANG: {
                    Player plonline3 = Client.gI().getPlayer(text[0]);

                    // Kiểm tra xem có thể chuyển đổi giá trị sang int
                    int sohongngoc3;
                    try {
                        sohongngoc3 = Integer.parseInt(text[1]);
                    } catch (NumberFormatException e) {
                        Service.getInstance().sendThongBao(player, "Giá trị Điểm VIP không hợp lệ.");
                        break;
                    }

                    if (plonline3 != null) {
                        // Kiểm tra xem người chuyển có đủ điểm VIP không
                        if (player.naptuan <= 0) {
                            break; // Ngừng thực hiện nếu không đủ điểm
                        }

                        if (sohongngoc3 > 0 && sohongngoc3 <= 100000000) {
                            if (sohongngoc3 <= player.napthang) { // Kiểm tra đủ điểm để tặng
                                player.napthang -= sohongngoc3;
                                plonline3.napthang += sohongngoc3;
                                Service.getInstance().sendThongBaoFromAdmin(player, "Bạn đã Cộng cho " + plonline3.name + " " + sohongngoc3 + " Điểm Nạp Tháng.");
                            } else {
                                Service.getInstance().sendThongBao(player, "Bạn không đủ Điểm Nạp để tặng.");
                            }
                        } else {
                            Service.getInstance().sendThongBao(player, "Chỉ có thể chuyển tối đa 10.000.000 Điểm Nạp.");
                        }
                    } else {
                        Service.getInstance().sendThongBao(player, "Người chơi không tồn tại hoặc đang offline.");
                    }
                    break;

                }

                case BUFFSHE: {
                    Player plonline3 = Client.gI().getPlayer(text[0]);

                    // Kiểm tra xem có thể chuyển đổi giá trị sang int
                    int sohongngoc3;
                    try {
                        sohongngoc3 = Integer.parseInt(text[1]);
                    } catch (NumberFormatException e) {
                        Service.getInstance().sendThongBao(player, "Giá trị Điểm VIP không hợp lệ.");
                        break;
                    }

                    if (plonline3 != null) {
                        // Kiểm tra xem người chuyển có đủ điểm VIP không
                        if (player.diemshe <= 0) {
                            break; // Ngừng thực hiện nếu không đủ điểm
                        }

                        if (sohongngoc3 > 0 && sohongngoc3 <= 100000000) {
                            if (sohongngoc3 <= player.diemshe) { // Kiểm tra đủ điểm để tặng
                                player.diemshe -= sohongngoc3;
                                plonline3.diemshe += sohongngoc3;
                                Service.getInstance().sendThongBaoFromAdmin(player, "Bạn đã Cộng cho " + plonline3.name + " " + sohongngoc3 + " Điểm she.");
                            } else {
                                Service.getInstance().sendThongBao(player, "Bạn không đủ Điểm she để tặng.");
                            }
                        } else {
                            Service.getInstance().sendThongBao(player, "Chỉ có thể chuyển tối đa 10.000.000 Điểm she.");
                        }
                    } else {
                        Service.getInstance().sendThongBao(player, "Người chơi không tồn tại hoặc đang offline.");
                    }
                    break;

                }

                case BUFFTONGNAP: {
                    Player plb = Client.gI().getPlayer(text[0]);
                    int CCB = Integer.parseInt(text[1]);
                    if (player.isAdmin()) {
                        if (plb != null) {
                            if (CCB <= 0 || CCB > 100000000) {
                                Service.getInstance().sendThongBao(player, "Buff ngu vừa thôi lồn ngu");
                                return;
                            }
                            Service.getInstance().sendThongBao(player, "Đã buff: " + Util.format(CCB) + " tích lũy cho " + plb.name);
                            Service.getInstance().sendThongBaoFromAdmin(plb, "|7|\n" + "Mày Được ADMIN: " + plb.name + " \nTốt Bụng cho: " + CCB + " Tổng Nạp\n" + "|4|\n" + "Lần Sau Nạp To To Vào Nhé");
                            plb.getSession().tongnap += CCB;
                            AccountDAO.updateVND(player.getSession());
                            if (CCB > 10000000) {
                                plb.getSession().tongnap += 100000000;
                                AccountDAO.updateVND(player.getSession());
                            }
                        } else {
                            Service.getInstance().sendThongBao(player, "Ko Onl");
                        }
                    } else {
                        PlayerService.gI().banPlayer(player);
                    }
                    break;
                }

                case SEND_ITEM_OP:
                    String name1 = text[0];
                    int id1 = Integer.parseInt(text[1]);
                    int q1 = Integer.parseInt(text[2]);
                    String option = text[3];
                    String param = text[4];
                    String[] option1 = option.split("-");
                    String[] param1 = param.split("-");
                    int length1 = option1.length;
                    int length2 = param1.length;
                    if (length1 == length2) {
                        if (Client.gI().getPlayer(name1) != null) {
                            Item item = ItemService.gI().createNewItem(((short) id1));
                            item.quantity = q1;
                            for (int i = 0; i < length1; i++) {
                                String option2 = option1[i];
                                String param2 = param1[i];
                                int opt;
                                int par;
                                try {
                                    opt = Integer.parseInt(option2);
                                    par = Integer.parseInt(param2);
                                    item.itemOptions.add(new ItemOption(opt, par));
                                    Service.getInstance().sendThongBaoFromAdmin(player, "|7|BUFF ITEM OPTION CHO " + name1 + " THÀNH CÔNG!");
                                } catch (NumberFormatException e) {
                                    break;
                                }

                            }
                            InventoryService.gI().addItemBag(Client.gI().getPlayer(name1), item, 999999999);
                            InventoryService.gI().sendItemBags(Client.gI().getPlayer(name1));
                            Service.getInstance().sendThongBaoOK(Client.gI().getPlayer(name1), "Nhận được [" + item.template.name + "] từ QTV " + player.name);
                        } else {
                            Service.getInstance().sendThongBao(player, "Không online");
                        }
                    } else {
                        Service.getInstance().sendThongBao(player, "Nhập dữ liệu không đúng");
                    }
                    break;
                case SEND_ITEM:
                    if (player.isAdmin()) {
                        int idItemBuff = Integer.parseInt(text[1]);
                        int quantityItemBuff = Integer.parseInt(text[2]);
                        Player pBuffItem = Client.gI().getPlayer(text[0]);
                        if (pBuffItem != null) {
                            String txtBuff = "Buff to player: " + pBuffItem.name + "\b";
                            if (idItemBuff == -1) {
                                pBuffItem.inventory.gold = Math.min(pBuffItem.inventory.gold + (long) quantityItemBuff, Inventory.LIMIT_GOLD);
                                txtBuff += quantityItemBuff + " vàng\b";
                                Service.getInstance().sendMoney(player);
                            } else if (idItemBuff == -2) {
                                pBuffItem.inventory.gem = Math.min(pBuffItem.inventory.gem + quantityItemBuff, 2000000000);
                                txtBuff += quantityItemBuff + " ngọc\b";
                                Service.getInstance().sendMoney(player);
                            } else if (idItemBuff == -3) {
                                pBuffItem.inventory.ruby = Math.min(pBuffItem.inventory.ruby + quantityItemBuff, 2000000000);
                                txtBuff += quantityItemBuff + " ngọc khóa\b";
                                Service.getInstance().sendMoney(player);
                            } else {
                                Item itemBuffTemplate = ItemService.gI().createNewItem((short) idItemBuff);
                                itemBuffTemplate.quantity = quantityItemBuff;
                                InventoryService.gI().addItemBag(pBuffItem, itemBuffTemplate, 999999999);
                                InventoryService.gI().sendItemBags(pBuffItem);
                                txtBuff += "x" + quantityItemBuff + " " + itemBuffTemplate.template.name + "\b";
                            }
                            NpcService.gI().createTutorial(player, 24, txtBuff);
                            if (player.id != pBuffItem.id) {
                                NpcService.gI().createTutorial(pBuffItem, 24, txtBuff);
                            }
                        } else {
                            Service.getInstance().sendThongBao(player, "Player không online");
                        }
                        break;
                    }
                    break;
                case DOILUULY:
                    int goldTrade11 = Integer.parseInt(text[0]);
                    int heso1 = 1000;
                    if (goldTrade11 % 1000 == 0) {
                        if (goldTrade11 <= 0 || goldTrade11 >= 1000000) {
                            Service.getInstance().sendThongBao(player, "|7|Quá giới hạn mỗi lần tối đa 1.000.000");
                        } else if (player.getSession().vnd >= goldTrade11) {
                            if (goldTrade11 >= 1000000) {
                                Item thehongngoc = null;
                                thehongngoc = InventoryService.gI().findItemBagByTemp(player, 2002);
                                Item thoiVang = ItemService.gI().createNewItem((short) 1404, (goldTrade11 * heso1 / 1000));
                                InventoryService.gI().addItemBag(player, thoiVang, 9);
                                InventoryService.gI().sendItemBags(player);
                                if (thehongngoc != null) {
                                    Service.getInstance().sendThongBao(player, "|3|Bạn đã có Vé tặng ngọc rồi nên không nhận được nữa !!");
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + Util.format((goldTrade11 * heso1 / 1000))
                                            + " " + (thoiVang.template.name));
                                } else {
///
                                }
                            } else {
                                Item thoiVang = ItemService.gI().createNewItem((short) 1404, (goldTrade11 * heso1 / 1000));
                                thoiVang.itemOptions.add(new ItemOption(30, 2025));
                                InventoryService.gI().addItemBag(player, thoiVang, 9999999999l);
                                InventoryService.gI().sendItemBags(player);
                                Service.getInstance().sendThongBao(player, "Bạn nhận được " + Util.format((goldTrade11 * heso1 / 1000))
                                        + " " + thoiVang.template.name);
                            }
                            PlayerDAO.subVND(player, goldTrade11);
                        } else {
                            Service.getInstance().sendThongBao(player, "|7|Số tiền của bạn là " + player.getSession().vnd + " không đủ để quy đổi " + goldTrade11 / 1000 + " lưu ly" + " " + "bạn cần thêm " + (player.getSession().vnd - goldTrade11));
                        }
                    } else {
                        Service.getInstance().sendThongBao(player, "|7|Số tiền nhập phải là từ 1000");
                    }
                    break;

                case doithoivang:
                    int goldTrade115 = Integer.parseInt(text[0]);
                    int heso15 = 100;
                    if (goldTrade115 % 1000 == 0) {
                        if (goldTrade115 <= 0 || goldTrade115 >= 1000000) {
                            Service.getInstance().sendThongBao(player, "|7|Quá giới hạn mỗi lần tối đa 1.000.000");
                        } else if (player.getSession().vnd >= goldTrade115) {
                            if (goldTrade115 >= 1000000) {
                                Item thehongngoc = null;
                                thehongngoc = InventoryService.gI().findItemBagByTemp(player, 2002);
                                Item thoiVang = ItemService.gI().createNewItem((short) 457, (goldTrade115 * heso15 / 1000));
                                InventoryService.gI().addItemBag(player, thoiVang, 9);
                                InventoryService.gI().sendItemBags(player);
                                if (thehongngoc != null) {
                                    Service.getInstance().sendThongBao(player, "|3|Bạn đã có Vé tặng ngọc rồi nên không nhận được nữa !!");
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + Util.format((goldTrade115 * heso15 / 1000))
                                            + " " + (thoiVang.template.name));
                                } else {
///
                                }
                            } else {
                                Item thoiVang = ItemService.gI().createNewItem((short) 457, (goldTrade115 * heso15 / 1000));
                                thoiVang.itemOptions.add(new ItemOption(30, 2025));
                                InventoryService.gI().addItemBag(player, thoiVang, 9999999999l);
                                InventoryService.gI().sendItemBags(player);
                                Service.getInstance().sendThongBao(player, "Bạn nhận được " + Util.format((goldTrade115 * heso15 / 1000))
                                        + " " + thoiVang.template.name);
                            }
                            PlayerDAO.subVND(player, goldTrade115);
                        } else {
                            Service.getInstance().sendThongBao(player, "|7|Số tiền của bạn là " + player.getSession().vnd + " không đủ để quy đổi " + goldTrade115 / 1000 + " Thỏi Vàng" + " " + "bạn cần thêm " + (player.getSession().vnd - goldTrade115));
                        }
                    } else {
                        Service.getInstance().sendThongBao(player, "|7|Số tiền nhập phải là từ 1000");
                    }
                    break;

                case doihongngoc:
                    int goldTrade116 = Integer.parseInt(text[0]);
                    int heso16 = 1000;
                    if (goldTrade116 % 1000 == 0) {
                        if (goldTrade116 <= 0 || goldTrade116 >= 100000) {
                            Service.getInstance().sendThongBao(player, "|7|Quá giới hạn mỗi lần tối đa 100.000");
                        } else if (player.getSession().vnd >= goldTrade116) {
                            if (goldTrade116 >= 1000000) {
                                Item thehongngoc = null;
                                thehongngoc = InventoryService.gI().findItemBagByTemp(player, 2002);
                                Item thoiVang = ItemService.gI().createNewItem((short) 861, (goldTrade116 * heso16 / 1000));
                                InventoryService.gI().addItemBag(player, thoiVang, 99);
                                InventoryService.gI().sendItemBags(player);
                                if (thehongngoc != null) {
                                    Service.getInstance().sendThongBao(player, "|3|Bạn đã có Vé tặng ngọc rồi nên không nhận được nữa !!");
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + Util.format((goldTrade116 * heso16 / 1000))
                                            + " " + (thoiVang.template.name));
                                } else {
///
                                }
                            } else {
                                Item thoiVang = ItemService.gI().createNewItem((short) 861, (goldTrade116 * heso16 / 1000));
                                thoiVang.itemOptions.add(new ItemOption(174, 2025));
                                InventoryService.gI().addItemBag(player, thoiVang, 9999999999l);
                                InventoryService.gI().sendItemBags(player);
                                Service.getInstance().sendThongBao(player, "Bạn nhận được " + Util.format((goldTrade116 * heso16 / 1000))
                                        + " " + thoiVang.template.name);
                            }
                            PlayerDAO.subVND(player, goldTrade116);
                        } else {
                            Service.getInstance().sendThongBao(player, "|7|Số tiền của bạn là " + player.getSession().vnd + " không đủ để quy đổi " + goldTrade116 / 1000 + " ruby" + " " + "bạn cần thêm " + (player.getSession().vnd - goldTrade116));
                        }
                    } else {
                        Service.getInstance().sendThongBao(player, "|7|Số tiền nhập phải là từ 1000");
                    }
                    break;

                case doidaugod:
                    int goldTrade118 = Integer.parseInt(text[0]);
                    int heso18 = 1000;
                    if (goldTrade118 % 1000 == 0) {
                        if (goldTrade118 <= 0 || goldTrade118 >= 100000) {
                            Service.getInstance().sendThongBao(player, "|7|Quá giới hạn mỗi lần tối đa 100.000");
                        } else if (player.getSession().vnd >= goldTrade118) {
                            if (goldTrade118 >= 1000000) {
                                Item thehongngoc = null;
                                thehongngoc = InventoryService.gI().findItemBagByTemp(player, 2002);
                                Item thoiVang = ItemService.gI().createNewItem((short) 861, (goldTrade118 * heso18 / 1000));
                                InventoryService.gI().addItemBag(player, thoiVang, 99);
                                InventoryService.gI().sendItemBags(player);
                                if (thehongngoc != null) {
                                    Service.getInstance().sendThongBao(player, "|3|Bạn đã có Vé tặng ngọc rồi nên không nhận được nữa !!");
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + Util.format((goldTrade118 * heso18 / 1000))
                                            + " " + (thoiVang.template.name));
                                } else {
///
                                }
                            } else {
                                Item thoiVang = ItemService.gI().createNewItem((short) 1111, (goldTrade118 * heso18 / 1000));
                                thoiVang.itemOptions.add(new ItemOption(174, 2025));
                                InventoryService.gI().addItemBag(player, thoiVang, 9999999999l);
                                InventoryService.gI().sendItemBags(player);
                                Service.getInstance().sendThongBao(player, "Bạn nhận được " + Util.format((goldTrade118 * heso18 / 1000))
                                        + " " + thoiVang.template.name);
                            }
                            PlayerDAO.subVND(player, goldTrade118);
                        } else {
                            Service.getInstance().sendThongBao(player, "|7|Số tiền của bạn là " + player.getSession().vnd + " không đủ để quy đổi " + goldTrade118 / 1000 + " ruby" + " " + "bạn cần thêm " + (player.getSession().vnd - goldTrade118));
                        }
                    } else {
                        Service.getInstance().sendThongBao(player, "|7|Số tiền nhập phải là từ 1000");
                    }
                    break;

                case doingocxanh:
                    int goldTrade117 = Integer.parseInt(text[0]);
                    int heso17 = 10000;
                    if (goldTrade117 % 1000 == 0) {
                        if (goldTrade117 <= 0 || goldTrade117 >= 100000) {
                            Service.getInstance().sendThongBao(player, "|7|Quá giới hạn mỗi lần tối đa 100.000");
                        } else if (player.getSession().vnd >= goldTrade117) {
                            if (goldTrade117 >= 1000000) {
                                Item thehongngoc = null;
                                thehongngoc = InventoryService.gI().findItemBagByTemp(player, 2002);
                                Item thoiVang = ItemService.gI().createNewItem((short) 77, (goldTrade117 * heso17 / 1000));
                                InventoryService.gI().addItemBag(player, thoiVang, 99);
                                InventoryService.gI().sendItemBags(player);
                                if (thehongngoc != null) {
                                    Service.getInstance().sendThongBao(player, "|3|Bạn đã có Vé tặng ngọc rồi nên không nhận được nữa !!");
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + Util.format((goldTrade117 * heso17 / 1000))
                                            + " " + (thoiVang.template.name));
                                } else {
///
                                }
                            } else {
                                Item thoiVang = ItemService.gI().createNewItem((short) 77, (goldTrade117 * heso17 / 1000));
                                thoiVang.itemOptions.add(new ItemOption(174, 2025));
                                InventoryService.gI().addItemBag(player, thoiVang, 9999999999l);
                                InventoryService.gI().sendItemBags(player);
                                Service.getInstance().sendThongBao(player, "Bạn nhận được " + Util.format((goldTrade117 * heso17 / 1000))
                                        + " " + thoiVang.template.name);
                            }
                            PlayerDAO.subVND(player, goldTrade117);
                        } else {
                            Service.getInstance().sendThongBao(player, "|7|Số tiền của bạn là " + player.getSession().vnd + " không đủ để quy đổi " + goldTrade117 / 1000 + " ngọc xanh" + " " + "bạn cần thêm " + (player.getSession().vnd - goldTrade117));
                        }
                    } else {
                        Service.getInstance().sendThongBao(player, "|7|Số tiền nhập phải là từ 1000");
                    }
                    break;

                case doivnd:
                    int goldTrade1111 = Integer.parseInt(text[0]);
                    int heso1111 = 1;
                    if (goldTrade1111 % 1000 == 0) {
                        if (goldTrade1111 <= 0 || goldTrade1111 >= 10000000) {
                            Service.getInstance().sendThongBao(player, "|7|Quá giới hạn mỗi lần tối đa 10.000.000");
                        } else if (player.getSession().vnd >= goldTrade1111) {
                            if (goldTrade1111 >= 10000000) {
                                Item thehongngoc = null;
                                thehongngoc = InventoryService.gI().findItemBagByTemp(player, 2002);
                                Item thoiVang = ItemService.gI().createNewItem((short) 1588, (goldTrade1111 * heso1111 / 1000));
                                InventoryService.gI().addItemBag(player, thoiVang, 99999);
                                InventoryService.gI().sendItemBags(player);
                                if (thehongngoc != null) {
                                    Service.getInstance().sendThongBao(player, "|3|Bạn đã có Vé tặng ngọc rồi nên không nhận được nữa !!");
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + Util.format((goldTrade1111 * heso1111 / 1000))
                                            + " " + (thoiVang.template.name));
                                } else {
///
                                }
                            } else {
                                Item thoiVang = ItemService.gI().createNewItem((short) 1588, (goldTrade1111 * heso1111 / 1000));
                                InventoryService.gI().addItemBag(player, thoiVang, 999999);
                                InventoryService.gI().sendItemBags(player);
                                Service.getInstance().sendThongBao(player, "Bạn nhận được " + Util.format((goldTrade1111 * heso1111 / 1000))
                                        + " " + thoiVang.template.name);
                            }
                            PlayerDAO.subVND(player, goldTrade1111);
                        } else {
                            Service.getInstance().sendThongBao(player, "|7|Số tiền của bạn là " + player.getSession().vnd + " không đủ để quy đổi " + goldTrade1111 / 1000 + " Tiền" + " " + "bạn cần thêm " + (player.getSession().vnd - goldTrade1111));
                        }
                    } else {
                        Service.getInstance().sendThongBao(player, "|7|Số tiền nhập phải là bội số của 1000");
                    }
                    break;

                case QUY_DOI_HONG_NGOC:
                    int goldTrade111 = Integer.parseInt(text[0]);
                    int heso11 = 40;
                    if (goldTrade111 % 2000 == 0) {
                        if (goldTrade111 <= 0 || goldTrade111 >= 10000000) {
                            Service.getInstance().sendThongBao(player, "|7|Quá giới hạn mỗi lần tối đa 10.000.000");
                        } else if (player.getSession().vnd >= goldTrade111) {
                            if (goldTrade111 >= 10000000) {
                                Item thehongngoc = null;
                                thehongngoc = InventoryService.gI().findItemBagByTemp(player, 2002);
                                Item thoiVang = ItemService.gI().createNewItem((short) 674, (goldTrade111 * heso11 / 2000));
                                InventoryService.gI().addItemBag(player, thoiVang, 99999);
                                InventoryService.gI().sendItemBags(player);
                                if (thehongngoc != null) {
                                    Service.getInstance().sendThongBao(player, "|3|Bạn đã có Vé tặng ngọc rồi nên không nhận được nữa !!");
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + Util.format((goldTrade111 * heso11 / 2000))
                                            + " " + (thoiVang.template.name));
                                } else {
///
                                }
                            } else {
                                Item thoiVang = ItemService.gI().createNewItem((short) 674, (goldTrade111 * heso11 / 2000));
                                InventoryService.gI().addItemBag(player, thoiVang, 99999);
                                InventoryService.gI().sendItemBags(player);
                                Service.getInstance().sendThongBao(player, "Bạn nhận được " + Util.format((goldTrade111 * heso11 / 2000))
                                        + " " + thoiVang.template.name);
                            }
                            PlayerDAO.subVND(player, goldTrade111);
                        } else {
                            Service.getInstance().sendThongBao(player, "|7|Số tiền của bạn là " + player.getSession().vnd + " không đủ để quy đổi " + goldTrade111 / 2000 + " lưu ly" + " " + "bạn cần thêm " + (player.getSession().vnd - goldTrade111));
                        }
                    } else {
                        Service.getInstance().sendThongBao(player, "|7|Số tiền nhập phải là bội số của 1000");
                    }
                    break;

                case THUITEM:
                    Player nameT = (Player) PLAYERID_OBJECT.get(player.id);
                    int idT = Integer.parseInt(text[0]);
                    int qT = Integer.parseInt(text[1]);
                    Item itembag = InventoryService.gI().findItemBagByTemp(nameT, idT);
                    Item itembody = InventoryService.gI().findItemBody(nameT, idT);
                    Item itembox = InventoryService.gI().findItemBagByTemp(nameT, idT);
                    if (nameT != null) {
                        if (itembag != null) {
                            InventoryService.gI().subQuantityItemsBag(nameT, itembag, qT);
                            InventoryService.gI().sendItemBags(nameT);
                            Service.gI().sendThongBao(player, "Thu " + itembag.template.name + " từ player : " + nameT.name);
                        } else if (itembody != null) {
                            InventoryService.gI().subQuantityItemsBody(nameT, itembody, qT);
                            InventoryService.gI().sendItemBags(nameT);
                            Service.gI().sendThongBao(player, "Thu " + itembody.template.name + " từ player : " + nameT.name);
                        } else if (itembox != null) {
                            InventoryService.gI().subQuantityItemsBox(nameT, itembox, qT);
                            InventoryService.gI().sendItemBags(nameT);
                            Service.gI().sendThongBao(player, "Thu " + itembox.template.name + " từ player : " + nameT.name);
                        } else {
                            Service.gI().sendThongBao(player, "Player không sở hữu item");
                        }
                    } else {
                        Service.gI().sendThongBao(player, "Player không online");
                    }
                    break;

                case TRADE_RUBY:
                    int cuantity = Integer.valueOf(text[0]);
                    if (!player.getSession().actived) {
                        Service.getInstance().sendThongBao(player, "Vui lòng kích hoạt tài khoản!");
                        break;
                    }
                    if (cuantity < 1000 || cuantity > 500_000) {
                        Service.getInstance().sendThongBao(player, "Tối thiểu 1000 và tối đa 500000");
                        break;
                    }
                    if (player.getSession().vnd < cuantity) {
                        Service.getInstance().sendThongBao(player, "Số dư không đủ vui lòng nạp thêm!\n Chúc Bạn Năm Mới An Khang");
                    } else {
                        PlayerDAO.subVND2(player, cuantity);
                        player.inventory.ruby += cuantity;
                        Service.getInstance().sendMoney(player);
                        Service.getInstance().sendThongBao(player, "Đã đổi thành công " + cuantity);
                    }
                    break;
                case CHOOSE_LEVEL_BDKB: {
                    int level = Integer.parseInt(text[0]);
                    if (level >= 1 && level <= 110) {
                        Npc npc = NpcManager.getByIdAndMap(ConstNpc.QUY_LAO_KAME, player.zone.map.mapId);
                        if (npc != null) {
                            npc.createOtherMenu(player, ConstNpc.MENU_ACCEPT_GO_TO_BDKB,
                                    "Con có chắc chắn muốn tới bản đồ kho báu cấp độ " + level + "?",
                                    new String[]{"Đồng ý", "Từ chối"}, level);
                        }
                    } else {
                        Service.getInstance().sendThongBao(player, "Không thể thực hiện");
                    }
                }
                break;
                case CHOOSE_LEVEL_CDRD: {
                    int level = Integer.parseInt(text[0]);
                    if (level >= 1 && level <= 110) {
                        Npc npc = NpcManager.getByIdAndMap(ConstNpc.THAN_VU_TRU, player.zone.map.mapId);
                        if (npc != null) {
                            npc.createOtherMenu(player, ConstNpc.MENU_ACCEPT_GO_TO_CDRD,
                                    "Con có chắc chắn muốn đến con đường rắn độc cấp độ " + level + "?",
                                    new String[]{"Đồng ý", "Từ chối"}, level);
                        }
                    } else {
                        Service.getInstance().sendThongBao(player, "Không thể thực hiện");
                    }
                }

//                    BanDoKhoBauService.gI().openBanDoKhoBau(player, (byte) );
                break;
                case TANG_NGOC_HONG:
                    pl = Client.gI().getPlayer(text[0]);
                    int numruby = Integer.parseInt((text[1]));
                    if (pl != null) {
                        if (numruby > 0 && player.inventory.ruby >= numruby) {
                            Item item = InventoryService.gI().findVeTangNgoc(player);
                            player.inventory.subRuby(numruby);
                            PlayerService.gI().sendInfoHpMpMoney(player);
                            pl.inventory.ruby += numruby;
                            PlayerService.gI().sendInfoHpMpMoney(pl);
                            Service.getInstance().sendThongBao(player, "Tặng Hồng ngọc thành công");
                            Service.getInstance().sendThongBao(pl, "Bạn được " + player.name + " tặng " + numruby + " Hồng ngọc");
                            InventoryService.gI().subQuantityItemsBag(player, item, 1);
                            InventoryService.gI().sendItemBags(player);
                        } else {
                            Service.getInstance().sendThongBao(player, "Không đủ Hồng ngọc để tặng");
                        }
                    } else {
                        Service.getInstance().sendThongBao(player, "Người chơi không tồn tại hoặc đang offline");
                    }
                    break;
                case ADD_ITEM:
                    short id = Short.parseShort((text[0]));
                    int quantity = Integer.parseInt(text[1]);
                    Item item = ItemService.gI().createNewItem(id);
                    if (item.template.type < 7) {
                        for (int i = 0; i < quantity; i++) {
                            item = ItemService.gI().createNewItem(id);
                            RewardService.gI().initBaseOptionClothes(item.template.id, item.template.type, item.itemOptions);
                            InventoryService.gI().addItemBag(player, item, 0);
                        }
                    } else {
                        item.quantity = quantity;
                        InventoryService.gI().addItemBag(player, item, 0);
                    }
                    InventoryService.gI().sendItemBags(player);
                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + item.template.name + " Số lượng: " + quantity);
            }
        } catch (Exception e) {
        }
    }

    public void createForm(Player pl, int typeInput, String title, SubInput... subInputs) {
        pl.iDMark.setTypeInput(typeInput);
        Message msg;
        try {
            msg = new Message(-125);
            msg.writer().writeUTF(title);
            msg.writer().writeByte(subInputs.length);
            for (SubInput si : subInputs) {
                msg.writer().writeUTF(si.name);
                msg.writer().writeByte(si.typeInput);
            }
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void createFormChangePassword(Player pl) {
        createForm(pl, CHANGE_PASSWORD, "Đổi mật khẩu", new SubInput("Mật khẩu cũ", PASSWORD),
                new SubInput("Mật khẩu mới", PASSWORD),
                new SubInput("Nhập lại mật khẩu mới", PASSWORD));
    }

    public void createFormQDLUULY(Player pl) {

        createForm(pl, DOILUULY, "Quy Đổi Lưu Ly 1k vnd 1000 cái", new SubInput("Nhập số lượng muốn đổi(1000 Vnđ = 1000 Lưu Ly)", NUMERIC));
    }

    public void createFormQDthoivang(Player pl) {

        createForm(pl, doithoivang, "Quy Đổi Thỏi Vàng", new SubInput("Nhập số lượng muốn đổi(1000 Vnđ = 100 Thỏi Vàng)", NUMERIC));
    }

    public void createFormQDhongngoc(Player pl) {

        createForm(pl, doihongngoc, "Quy Đổi ruby", new SubInput("Nhập số lượng muốn đổi(1000 Vnđ = 1000 ruby)", NUMERIC));
    }

    public void createFormQDngocxanh(Player pl) {

        createForm(pl, doingocxanh, "Quy Đổi nx", new SubInput("Nhập số lượng muốn đổi(1000 Vnđ = 10.000 nx)", NUMERIC));
    }

    public void createFormQDdaugod(Player pl) {

        createForm(pl, doidaugod, "Quy Đổi Đậu", new SubInput("Nhập số lượng muốn đổi(10.000 Vnđ = 10.000 Đậu)", NUMERIC));
    }

    public void createFormQDvnd(Player pl) {

        createForm(pl, doivnd, "Quy Đổi Kim Nguyên Bảo, giới hạn đổi không quá 10000000 và không nhỏ hơn 1", new SubInput("Nhập số lượng muốn đổi(1k Vnđ = 1 KNB)", NUMERIC));
    }

    public void createFormQDHN1(Player pl) {

        createForm(pl, QUY_DOI_HONG_NGOC, "Quy Đổi đá, giới hạn đổi không quá 100000 và không nhỏ hơn 5", new SubInput("Nhập số lượng muốn đổi(2000 vnd = 40 đá Ngũ Sắc", NUMERIC));
    }

    public void congtien(Player pl) {
        createForm(pl, buffmoney, "Menu cộng tiền", new SubInput("Nhập tên player", ANY), new SubInput("Nhập số tiền", ANY));
    }

    public void phieutanghongngoc(Player pl) {
        createForm(pl, phieutanghongngoc, "Table Tặng Hồng Ngọc", new SubInput("Nhập tên player", ANY), new SubInput("Nhập số hồng ngọc", ANY));
    }

    public void phieutangdiem(Player pl) {
        createForm(pl, tangdiem, "Table Tặng Điểm", new SubInput("Nhập tên player", ANY), new SubInput("Nhập số Điểm", ANY));
    }

    public void buffnaptuan(Player pl) {
        createForm(pl, BUFFNAPTUAN, "Table Buff Nạp Tuần", new SubInput("Nhập tên player", ANY), new SubInput("Nhập số Tiền", ANY));
    }

    public void buffnapthang(Player pl) {
        createForm(pl, BUFFTHANG, "Table Buff Nạp Tháng", new SubInput("Nhập tên player", ANY), new SubInput("Nhập số Tiền", ANY));
    }

    public void buffdiemshe(Player pl) {
        createForm(pl, BUFFSHE, "Table BuffSHE", new SubInput("Nhập tên player", ANY), new SubInput("Nhập số Điểm", ANY));
    }

    public void phieutangdiemvip(Player pl) {
        createForm(pl, tangdiemvip, "Table Tặng Điểm vip", new SubInput("Nhập tên player", ANY), new SubInput("Nhập số Điểm vip", ANY));
    }

    public void createFormGiftCode(Player pl) {
        createForm(pl, GIFT_CODE, "GIFTCODE NRO HUYỀN ẢO", new SubInput("Nhập mã giftcode Vào Đây VIP666-VIP888", ANY));
    }

    public void createFormChangeNameByItem(Player pl) {
        createForm(pl, CHANGE_NAME, "Đổi tên " + pl.name, new SubInput("Tên mới", ANY));
    }

    public void createFormFindPlayer(Player pl) {
        createForm(pl, FIND_PLAYER, "Tìm kiếm người chơi", new SubInput("Tên người chơi", ANY));
    }

    public void TAI(Player pl) {
        createForm(pl, TAI, "Chọn số thỏi vàng đặt Xiu", new SubInput("Số thỏi vàng", ANY));
    }

    public void XIU(Player pl) {
        createForm(pl, XIU, "Chọn số thỏi vàng đặt Tai", new SubInput("Số thỏi vàng", ANY));
    }

    public void buffcspet(Player pl) {
        createForm(pl, GIVE_CS, "BUFF CHỈ SỐ ĐỆ TỬ [ TĂNG CS GỐC ]",
                new SubInput("SỨC MẠNH", ANY),
                new SubInput("TIỀM NĂNG", ANY),
                new SubInput("SỨC ĐÁNH", ANY),
                new SubInput("HP, KI", ANY),
                new SubInput("CRIT", ANY));
    }

    public void subcspet(Player pl) {
        createForm(pl, SUB_CS, "GIẢM CHỈ SỐ ĐỆ TỬ [ GIẢM CS GỐC ]",
                new SubInput("GIẢM SỨC MẠNH", ANY),
                new SubInput("GIẢM TIỀM NĂNG", ANY),
                new SubInput("GIẢM SỨC ĐÁNH", ANY),
                new SubInput("GIẢM HP, KI", ANY),
                new SubInput("GIẢM CRIT", ANY));
    }

    public void thuitem(Player pl) {
        Player nameT = (Player) PLAYERID_OBJECT.get(pl.id);
        createForm(pl, THUITEM, "Thu vật phẩm Player : " + nameT.name, new SubInput("Id Item", ANY), new SubInput("Số lượng", ANY));
    }

    public void createFormSenditem(Player pl) {
        createForm(pl, SEND_ITEM, "SEND ITEM",
                new SubInput("Tên người chơi", ANY),
                new SubInput("ID item", NUMERIC),
                new SubInput("Số lượng", NUMERIC));
    }

    public void createFormSenditem1(Player pl) {
        createForm(pl, SEND_ITEM_OP, "BUFF ITEM KÈM LIST OPTION",
                new SubInput("Player Name", ANY),
                new SubInput("Id Item", ANY),
                new SubInput("Quantity", ANY),
                new SubInput("Id Option (List sau dấu -)", ANY),
                new SubInput("Param Option (List sau dấu -)", ANY));
    }

    public void createFormBuffItemVip(Player pl) {
        createForm(pl, BUFF_ITEM_VIP, "BUFF VIP", new SubInput("Tên người chơi", ANY), new SubInput("Id Item", ANY), new SubInput("Chuỗi option vd : 50-20v30-1", ANY), new SubInput("Số lượng", ANY));
    }

    public void createFormTradeRuby(Player pl) {
        createForm(pl, TRADE_RUBY, "Tỉ Lệ 1-1 : 10.000 VNĐ = 10.000 Hồng Ngọc\n VNĐ Bạn Đang Có : " + pl.getSession().vnd, new SubInput("Số lượng", NUMERIC));
    }

    public void createFormChangeName(Player pl, Player plChanged) {
        PLAYER_ID_OBJECT.put((int) pl.id, plChanged);
        createForm(pl, CHANGE_NAME, "Đổi tên " + plChanged.name, new SubInput("Tên mới", ANY));
    }

    public void createFormChooseLevelBDKB(Player pl) {
        createForm(pl, CHOOSE_LEVEL_BDKB, "Chọn cấp độ", new SubInput("Cấp độ (1-110)", NUMERIC));
    }

    public void createFormChooseLevelCDRD(Player pl) {
        createForm(pl, CHOOSE_LEVEL_CDRD, "Chọn cấp độ", new SubInput("Cấp độ (1-110)", NUMERIC));
    }

    public void createFormBotQuai(Player pl) {
        createForm(pl, BOTQUAI, "Buff Bot Quái",
                new SubInput("số lượng bot", NUMERIC));
    }

    public void createFormBotBoss(Player pl) {
        createForm(pl, BOTBOSS, "Buff Bot Boss",
                new SubInput("số lượng bot", NUMERIC));
    }

    public void createFormBotItem(Player pl) {
        createForm(pl, BOTITEM, "Buff Bot Item",
                new SubInput("số lượng bot", NUMERIC),
                new SubInput("id item cần bán", NUMERIC),
                new SubInput("id item trao đổi", NUMERIC),
                new SubInput("số lượng yêu cầu trao đổi", NUMERIC));
    }

    public void TAI_taixiu(Player pl) {
        createForm(pl, TAI_taixiu, "Chọn số hồng ngọc đặt Tài", new SubInput("Số Hồng ngọc cược", ANY));//????
    }

    public void ChatAll(Player pl) {
        createForm(pl, CHATALL, "CHAT ALL PLAYER", new SubInput("Chat Sex", ANY));
    }

    public void XIU_taixiu(Player pl) {
        createForm(pl, XIU_taixiu, "Chọn số hồng ngọc đặt Xỉu", new SubInput("Số Hồng ngọc cược", ANY));//????
    }

    public void BUFFCTK(Player pl) {
        createForm(pl, BUFF_CAP_TAMKJLL, "Chọn số cấp Tiên Bang", new SubInput("Name", ANY),
                new SubInput("Số cấp", NUMERIC));
    }

    public void MOKHOA(Player pl) {
        createForm(pl, MO_KHOA_ACC, "ID acc cần mở", new SubInput("ID", ANY));
    }

    public void BUFFCL3(Player pl) {
        createForm(pl, BUFF_CAP_LANG_BA, "Chọn số cấp Nhập Ma", new SubInput("Name", ANY),
                new SubInput("Số cấp", NUMERIC));
    }

    public void BUFFCTM(Player pl) {
        createForm(pl, BUFF_CAP_THO_MO, "Chọn số cấp Thợ mỏ", new SubInput("Name", ANY),
                new SubInput("Số cấp", NUMERIC));
    }

    public void BUFFETK(Player pl) {
        createForm(pl, BUFF_EXP_TAMKJLL, "Chọn số Exp Tiên Bang", new SubInput("Name", ANY),
                new SubInput("Số Exp", NUMERIC));
    }

    public void BUFFSAOTP(Player pl) {
        createForm(pl, BUFF_SAO_THIEN_PHU, "Số sao thiên phú cần buff", new SubInput("Name", ANY),
                new SubInput("Số sao", NUMERIC));
    }

    public void BUFFVND(Player pl) {
        createForm(pl, BUFFVND, "Số Tiền Buff", new SubInput("Name", ANY),
                new SubInput("Số Tiền", NUMERIC));
    }

    public void BUFFTONGNAP(Player pl) {
        createForm(pl, BUFFTONGNAP, "Số Tiền Buff", new SubInput("Name", ANY),
                new SubInput("Số Tiền", NUMERIC));
    }

    public void createFormTangRuby(Player pl) {
        createForm(pl, TANG_NGOC_HONG, "Tặng ngọc", new SubInput("Tên nhân vật", ANY),
                new SubInput("Số Hồng Ngọc Muốn Tặng", NUMERIC));
    }

    public void createFormAddItem(Player pl) {
        createForm(pl, ADD_ITEM, "Add Item", new SubInput("ID VẬT PHẨM", NUMERIC),
                new SubInput("SỐ LƯỢNG", NUMERIC));
    }

    public void doithoivangsanghongngoc(Player pl) {
        createForm(pl, tvsanghn, "ĐỔI THỎI VÀNG"
                + "\n 1 thỏi vàng = 300 ngọc", new SubInput("Nhập thỏi vàng", NUMERIC));
    }

    public void doihongngocsangthoivang(Player pl) {
        createForm(pl, hnsangtv, "ĐỔI HỒNG NGỌC"
                + "\n400 Ngọc = 1 thỏi vàng", new SubInput("Nhập thỏi vàng", NUMERIC));

    }

    public void createFormQDHN(Player pl) {
        createForm(pl, QUY_DOI_COIN, "ĐỔI HỒNG NGỌC", new SubInput("Nhập số lượng muốn đổi", NUMERIC));
    }

    public void createFormQDTV(Player pl) {
        createForm(pl, QUY_DOI_COIN_1, "ĐỔI THỎI VÀNG", new SubInput("Nhập số lượng muốn đổi", NUMERIC));
    }

    public void CheckInfo(Player pl) {
        createForm(pl, CHECK_INFO, "Xem thông tin người chơi", new SubInput("Tên người muốn xem", ANY));
    }

    public void CHUYENHN(Player pl) {
        createForm(pl, CHUYEN_HONG_NGOC, "1 hồng ngọc cần 500 exp Bún", new SubInput("Name", ANY),
                new SubInput("Số hồng ngọc", NUMERIC));
    }

    public void THIEMALENH(Player pl) {
        createForm(pl, THIEN_MA_LENH, "hãy sài sự phát sét của thiên ma thần", new SubInput("Tên người cần giết", ANY));
    }

    public class SubInput {

        private String name;
        private byte typeInput;

        public SubInput(String name, byte typeInput) {
            this.name = name;
            this.typeInput = typeInput;
        }
    }
}
