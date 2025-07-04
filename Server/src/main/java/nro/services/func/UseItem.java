package nro.services.func;

import java.util.ArrayList;
import nro.consts.*;
import nro.dialog.MenuDialog;
import nro.dialog.MenuRunable;
import nro.event.Event;
import nro.lib.RandomCollection;
import nro.manager.NamekBallManager;
import nro.models.item.Item;
import nro.models.item.ItemOption;
import nro.models.map.*;
import nro.models.map.dungeon.zones.ZSnakeRoad;
import nro.models.map.war.NamekBallWar;
import nro.models.player.Inventory;
import nro.models.player.MiniPet;
import nro.models.player.Player;
import nro.models.skill.Skill;
import nro.server.Manager;
import nro.server.io.Message;
import nro.server.io.Session;
import nro.services.*;
import nro.utils.Log;
import nro.utils.SkillUtil;
import nro.utils.TimeUtil;
import nro.utils.Util;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.List;
import java.util.Random;
import nro.jdbc.daos.AccountDAO;
import nro.jdbc.daos.PlayerDAO;
import nro.map.VoDaiSinhTu.VoDaiSinhTuService;
import nro.models.boss.Boss;
import nro.models.boss.BossManager;
import nro.models.boss.broly.HoTong;
import nro.models.npc.specialnpc.BillEgg;
import nro.models.npc.specialnpc.MabuEgg;
import nro.server.Client;

/**
 * @Build Arriety
 */
public class UseItem {

    private static final int ITEM_BOX_TO_BODY_OR_BAG = 0;
    private static final int ITEM_BAG_TO_BOX = 1;
    private static final int ITEM_BODY_TO_BOX = 3;
    private static final int ITEM_BAG_TO_BODY = 4;
    private static final int ITEM_BODY_TO_BAG = 5;
    private static final int ITEM_BAG_TO_PET_BODY = 6;
    private static final int ITEM_BODY_PET_TO_BAG = 7;

    private static final byte DO_USE_ITEM = 0;
    private static final byte DO_THROW_ITEM = 1;
    private static final byte ACCEPT_THROW_ITEM = 2;
    private static final byte ACCEPT_USE_ITEM = 3;

    private static UseItem instance;
    private static final Logger logger = Logger.getLogger(UseItem.class);

    private UseItem() {

    }

    public static UseItem gI() {
        if (instance == null) {
            instance = new UseItem();
        }
        return instance;
    }

    public void getItem(Session session, Message msg) {
        Player player = session.player;
        TransactionService.gI().cancelTrade(player);
        try {
            int type = msg.reader().readByte();
            int index = msg.reader().readByte();
            switch (type) {
                case ITEM_BOX_TO_BODY_OR_BAG:
                    InventoryService.gI().itemBoxToBodyOrBag(player, index);
//                    TaskService.gI().checkDoneTaskGetItemBox(player);
                    break;
                case ITEM_BAG_TO_BOX:
                    InventoryService.gI().itemBagToBox(player, index);
                    break;
                case ITEM_BODY_TO_BOX:
                    InventoryService.gI().itemBodyToBox(player, index);
                    break;
                case ITEM_BAG_TO_BODY:
                    InventoryService.gI().itemBagToBody(player, index);
                    break;
                case ITEM_BODY_TO_BAG:
                    InventoryService.gI().itemBodyToBag(player, index);
                    break;
                case ITEM_BAG_TO_PET_BODY:
                    InventoryService.gI().itemBagToPetBody(player, index);
                    break;
                case ITEM_BODY_PET_TO_BAG:
                    InventoryService.gI().itemPetBodyToBag(player, index);
                    break;
            }
            player.setClothes.setup();
            if (player.pet != null) {
                player.pet.setClothes.setup();
            }
            player.setClanMember();
            Service.getInstance().point(player);
        } catch (Exception e) {
            Log.error(UseItem.class, e);

        }
    }

    public void doItem(Player player, Message _msg) {
        TransactionService.gI().cancelTrade(player);
        Message msg;
        try {
            byte type = _msg.reader().readByte();
            int where = _msg.reader().readByte();
            int index = _msg.reader().readByte();
            switch (type) {
                case DO_USE_ITEM:
                    if (player != null && player.inventory != null) {
                        if (index != -1) {
                            if (index >= 0 && index < player.inventory.itemsBag.size()) {
                                Item item = player.inventory.itemsBag.get(index);
                                if (item.isNotNullItem()) {
                                    if (item.template.type == 21) {
                                        MiniPet.callMiniPet(player, item.template.id);
                                        InventoryService.gI().itemBagToBody(player, index);
                                        break;
                                    }
                                    if (item.template.type == 22) {
                                        msg = new Message(-43);
                                        msg.writer().writeByte(type);
                                        msg.writer().writeByte(where);
                                        msg.writer().writeByte(index);
                                        msg.writer().writeUTF("Bạn có muốn dùng " + player.inventory.itemsBag.get(index).template.name + "?");
                                        player.sendMessage(msg);
                                        msg.cleanup();
                                    } else if (item.template.type == 7) {
                                        msg = new Message(-43);
                                        msg.writer().writeByte(type);
                                        msg.writer().writeByte(where);
                                        msg.writer().writeByte(index);
                                        msg.writer().writeUTF("Bạn chắc chắn học " + player.inventory.itemsBag.get(index).template.name + "?");
                                        player.sendMessage(msg);
                                    } else if (player.isVersionAbove(220) && item.template.type == 23 || item.template.type == 24 || item.template.type == 11) {
                                        InventoryService.gI().itemBagToBody(player, index);
                                    } else if (item.template.id == 401) {
                                        msg = new Message(-43);
                                        msg.writer().writeByte(type);
                                        msg.writer().writeByte(where);
                                        msg.writer().writeByte(index);
                                        msg.writer().writeUTF("Sau khi đổi đệ sẽ mất toàn bộ trang bị trên người đệ tử nếu chưa tháo");
                                        player.sendMessage(msg);
                                    } else {
                                        useItem(player, item, index);
                                    }
                                }
                            }
                        } else {
                            InventoryService.gI().eatPea(player);
                        }
                    }
                    break;
                case DO_THROW_ITEM:
                    // Kiểm tra null cho player, player.zone và player.zone.map
                    if (player == null || player.zone == null || player.zone.map == null) {
                        Service.getInstance().sendThongBao(player, "Không thể thực hiện vì dữ liệu không hợp lệ");
                        break;
                    }

                    // Điều kiện kiểm tra mapId
                    if (!(player.zone.map.mapId == 0 || player.zone.map.mapId == 14 || player.zone.map.mapId == 7)) {
                        Item item = null;
                        if (where == 0) {
                            if (index >= 0 && index < player.inventory.itemsBody.size()) {
                                item = player.inventory.itemsBody.get(index);
                            }
                        } else {
                            if (index >= 0 && index < player.inventory.itemsBag.size()) {
                                item = player.inventory.itemsBag.get(index);
                            }
                        }

                        // Kiểm tra item trước khi xử lý
                        if (item != null && item.isNotNullItem()) {
                            msg = new Message(-43);
                            msg.writer().writeByte(type);
                            msg.writer().writeByte(where);
                            msg.writer().writeByte(index);
                            msg.writer().writeUTF("Bạn chắc chắn muốn vứt " + item.template.name + "?");
                            player.sendMessage(msg);
                        }
                    } else {
                        Service.getInstance().sendThongBao(player, "Không thể thực hiện");
                    }
                    break;

                case ACCEPT_THROW_ITEM:
                    InventoryService.gI().throwItem(player, where, index);
                    break;
                case ACCEPT_USE_ITEM:
                    if (index >= 0 && index < player.inventory.itemsBag.size()) {
                        Item item = player.inventory.itemsBag.get(index);
                        if (item.isNotNullItem()) {
                            useItem(player, item, index);
                        }
                    }
                    break;
            }
        } catch (Exception e) {
            Log.error(UseItem.class, e);
        }
    }

    public void useSatellite(Player player, Item item) {
        Satellite satellite = null;
        if (player.zone != null) {
            int count = player.zone.getSatellites().size();
            if (count < 3) {
                switch (item.template.id) {
                    case ConstItem.VE_TINH_TRI_LUC:
                        satellite = new SatelliteMP(player.zone, ConstItem.VE_TINH_TRI_LUC, player.location.x, player.location.y, player);
                        break;
                    case ConstItem.VE_TINH_TRI_TUE:
                        satellite = new SatelliteExp(player.zone, ConstItem.VE_TINH_TRI_TUE, player.location.x, player.location.y, player);
                        break;

                    case ConstItem.VE_TINH_PHONG_THU:
                        satellite = new SatelliteDefense(player.zone, ConstItem.VE_TINH_PHONG_THU, player.location.x, player.location.y, player);
                        break;

                    case ConstItem.VE_TINH_SINH_LUC:
                        satellite = new SatelliteHP(player.zone, ConstItem.VE_TINH_SINH_LUC, player.location.x, player.location.y, player);
                        break;
                }
                if (satellite != null) {
                    InventoryService.gI().subQuantityItemsBag(player, item, 1);
                    Service.getInstance().dropItemMapForMe(player, satellite);
                    Service.getInstance().dropItemMap(player.zone, satellite);
                }
            } else {
                Service.getInstance().sendThongBaoOK(player, "Số lượng vệ tinh có thể đặt trong khu vực đã đạt mức tối đa.");
            }
        }
    }

    private void useItem(Player pl, Item item, int indexBag) {
        if (Event.isEvent() && Event.getInstance().useItem(pl, item)) {
            return;
        }
        if (item.template.strRequire <= pl.nPoint.power) {
            int type = item.getType();
            switch (type) {
                case 6:
                    InventoryService.gI().eatPea(pl);
                    break;
                case 33:
                    RadaService.getInstance().useItemCard(pl, item);
                    break;
                case 22:
                    useSatellite(pl, item);
                    break;
                case 99:
                    break;
                default:
                    switch (item.template.id) {
//                        case 1418:
//                            if (pl.pet.typePet == ConstPet.MABU) {
//                                BillEgg.createBillEgg(pl);
//                                InventoryService.gI().subQuantityItemsBag(pl, item, 1);
//                                InventoryService.gI().sendItemBags(pl);
//                            } else {
//                                Service.getInstance().sendThongBao(pl, "Yêu cầu có đệ ma bư");
//                            }
//                            break;
                        case 457:
                            UseItem.gI().UseThoiVang(pl);
                            break;
//                        case 1507:
//                            long dk = (pl.CapTamkjll + 1) * 1;
//                            pl.ExpTamkjll += dk / 2;
//                            Service.getInstance().sendThongBaoFromAdmin(pl, "Bạn Đã Cắn Sen Vàng Tăng Random " + );
//                            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
//                            InventoryService.gI().sendItemBags(pl);
//                            break;
//                        case 1508:
//                            dk = (pl.TamkjllThomo + 1) * 50L;
//                            pl.TamkjllThomo += dk / 2;
//                            Service.getInstance().sendThongBao(pl, "Mày đã nhận đc 1 cấp Khai Thác");
//                            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
//                            InventoryService.gI().sendItemBags(pl);
//                            break;

                        case 1507:
                            long tienbang = Util.nextInt(1, 3000000);
                            pl.ExpTamkjll += tienbang;
                            Service.getInstance().sendThongBaoFromAdmin(pl, "Bạn Đã Cắn Sen Vàng Tăng Random [ " + tienbang + " ] EXP\nTiên Bang Là 1 Nội Tại giúp Mạnh Mẽ Khống Chế\nTăng Sức Mạnh Skill Check Tại Npc Thiên Sứ Girl\n Tại Các máp Làng LH admin Để Thông Não");
                            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                            InventoryService.gI().sendItemBags(pl);
                            break;

                        case 1638:
                            pl.CapTamkjll++;
                            Service.getInstance().sendThongBao(pl, "Bạn Đã tăng 1 Cấp Tiên Bang");
                            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                            Service.getInstance().sendMoney(pl);
                            Service.getInstance().point(pl);
                            InventoryService.gI().sendItemBags(pl);
                            break;

                        case 1509:
                            pl.LbTamkjll++;
                            Service.getInstance().sendThongBao(pl, "Đã ăn 100k Sức Đánh Vui Lòng Mặc Lại Cải Trang Để Load ");
                            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                            Service.getInstance().sendMoney(pl);
                            Service.getInstance().point(pl);
                            InventoryService.gI().sendItemBags(pl);
                            break;
                        case 1510:
                            pl.ExpTamkjll += 1000000;
                            Service.getInstance().sendThongBao(pl, "Mày được cộng 1tr exp Tiên Bang");
                            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                            Service.getInstance().sendMoney(pl);
                            Service.getInstance().point(pl);
                            InventoryService.gI().sendItemBags(pl);
                            break;
                        case 1589:
                            pl.ExpTamkjll += 5000;
                            Service.getInstance().sendThongBao(pl, "Mày được cộng 5000 exp Tiên Bang");
                            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                            Service.getInstance().sendMoney(pl);
                            Service.getInstance().point(pl);
                            InventoryService.gI().sendItemBags(pl);
                            break;
                        case 1590:
                            pl.DLbTamkjll += 5000;
                            Service.getInstance().sendThongBao(pl, "Mày được cộng 5000 exp Nhập Ma");
                            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                            Service.getInstance().sendMoney(pl);
                            Service.getInstance().point(pl);
                            InventoryService.gI().sendItemBags(pl);
                            break;

                        case 1111:
                            UseItem.gI().UseThoiVangvip(pl);
                            break;

                        case 1518:
                            String qtsln1 = "";
                            if (Client.gI().getPlayers().size() >= 10) {
                                qtsln1 += "Mỗi 60s nhận: " + Client.gI().getPlayers().size() + " EXP Tiên Bang, "
                                        + Client.gI().getPlayers().size() / 97 + " hồng ngọc. Quà theo số lượng người online";
                            }

//                            String listBoss = "";
//                            if (BossManager.gI().findBossByName("Vận tiêu " + pl.name) != null) {
//                                listBoss += "\n";
//                                for (Boss b : BossManager.BOSSES_IN_GAME) {
//                                    if (b.name.equals("Vận tiêu " + pl.name)) {
//                                        listBoss += "Phẩm Chất: " + ((HoTong) b).phamChatTieu + "\nMap: " + MapService.gI().getMapById(((HoTong) b).mapHoTong).mapName + "\nKhu: " + b.zone.zoneId + "\n";
//                                    }
//                                }
//                            }
                            NpcService.gI().createMenuConMeo(pl, ConstNpc.Checkthongtin, -1,
                                    "|7|Công Ty Trách Nhiệm 1 Thành Viên: @Admin\n"
                                    //                                    + listBoss
                                    + "|4|Ipad PRo Max Thông Minh Đến Từ Hành Tinh X\n"
                                    + "|7|Lệnh Đến Máp Tu Tiên: TuTien\n",
                                    "Check\nBản Thân", "Check\nĐệ Tử", "Check\nMục Tiêu", "Check\nTiên Bang",
                                    "Check Exp", "Soi Boss"
                            );
                            break;

                        case 1532:
                            pl.TamkjllDauLaDaiLuc[0]++;
                            InventoryService.gI().subQuantityItem(pl.inventory.itemsBag, item, 1);
                            InventoryService.gI().sendItemBags(pl);
                            Service.getInstance().sendThongBao(pl, "Ngươi đã hấp thụ " + item.template.name);
                            break;
                        case 1533:
                            pl.TamkjllDauLaDaiLuc[1]++;
                            InventoryService.gI().subQuantityItem(pl.inventory.itemsBag, item, 1);
                            InventoryService.gI().sendItemBags(pl);
                            Service.getInstance().sendThongBao(pl, "Ngươi đã hấp thụ " + item.template.name);
                            break;
                        case 1534:
                            pl.TamkjllDauLaDaiLuc[2]++;
                            InventoryService.gI().subQuantityItem(pl.inventory.itemsBag, item, 1);
                            InventoryService.gI().sendItemBags(pl);
                            Service.getInstance().sendThongBao(pl, "Ngươi đã hấp thụ " + item.template.name);
                            break;
                        case 1535:
                            pl.TamkjllDauLaDaiLuc[3]++;
                            InventoryService.gI().subQuantityItem(pl.inventory.itemsBag, item, 1);
                            InventoryService.gI().sendItemBags(pl);
                            Service.getInstance().sendThongBao(pl, "Ngươi đã hấp thụ " + item.template.name);
                            break;
                        case 1536:
                            pl.TamkjllDauLaDaiLuc[4]++;
                            InventoryService.gI().subQuantityItem(pl.inventory.itemsBag, item, 1);
                            InventoryService.gI().sendItemBags(pl);
                            Service.getInstance().sendThongBao(pl, "Ngươi đã hấp thụ " + item.template.name);
                            break;
                        case 1537:
                            pl.TamkjllDauLaDaiLuc[5]++;
                            InventoryService.gI().subQuantityItem(pl.inventory.itemsBag, item, 1);
                            InventoryService.gI().sendItemBags(pl);
                            Service.getInstance().sendThongBao(pl, "Ngươi đã hấp thụ " + item.template.name);
                            break;
                        case 1538:
                            pl.TamkjllDauLaDaiLuc[6]++;
                            InventoryService.gI().subQuantityItem(pl.inventory.itemsBag, item, 1);
                            InventoryService.gI().sendItemBags(pl);
                            Service.getInstance().sendThongBao(pl, "Ngươi đã hấp thụ " + item.template.name);
                            break;

                        case 1970:
                            ItemService.gI().setthanlinhtd(pl);
                            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                            break;
                        case 1971:
                            ItemService.gI().setthanlinhnm(pl);
                            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                            break;
                        case 1972:
                            ItemService.gI().setthanlinhxd(pl);
                            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                            break;

                        case 1113:
                            ItemService.gI().setthiensutd(pl);
                            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                            break;
                        case 1114:
                            ItemService.gI().setthiensunm(pl);
                            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                            break;

                        case 1115:
                            ItemService.gI().setthiensuxd(pl);
                            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                            break;
                        case 1116:
                            ItemService.gI().setKaioKenchiton(pl);
                            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                            break;
                        case 1117:
                            ItemService.gI().setcombo6masenco(pl);
                            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                            break;
                        case 1118:
                            combodanangcap(pl, item);
                            break;
                        case 1119:
                            combodabaove(pl, item);
                            break;
                        case 1120:
                            comboitemc2(pl, item);
                            break;
                        case 1121:
                            combothoivang(pl, item);
                            break;
                        case 1122:
                            combodns(pl, item);
                            break;
                        case 1123:
                            combothucan(pl, item);
                            break;
                        case 1124:
                            combodaugod(pl, item);
                            break;
                        case 1125:
                            combocskb(pl, item);
                            break;

                        case 1094:
                            UseItem.gI().phanbon(pl, item);
                            break;

                        case 1237:
                        case 1238:
                        case 1239:
                            UseItem.gI().sukiensonlo(pl, item);
                            break;
//                        case 736:
//                            UseItem.gI().mohop500tr(pl, item);
//                            break;
                        case 722:
                            UseItem.gI().capsulepink(pl, item);
                            break;
                        case 1503:
                            int rubynhan = Util.nextInt(1, 1000);
                            pl.inventory.ruby += rubynhan;
                            InventoryService.gI().subQuantityItem(pl.inventory.itemsBag, item, 1);
                            InventoryService.gI().sendItemBags(pl);
                            Service.getInstance().sendMoney(pl);
                            Service.getInstance().sendThongBao(pl, "Bạn nhận đc: " + rubynhan + "Ruby");

                            break;
                        case 1504:
                            int gem = Util.nextInt(1, 1000);
                            pl.inventory.gem += gem;
                            InventoryService.gI().subQuantityItem(pl.inventory.itemsBag, item, 1);
                            InventoryService.gI().sendItemBags(pl);
                            Service.getInstance().sendMoney(pl);
                            Service.getInstance().sendThongBao(pl, "Bạn nhận đc: " + gem + "ngọc xanh");

                            break;

                        case 1505:
                            UseItem.gI().dathanlinh(pl, item);
                            break;

                        case 2002:
                            Input.gI().phieutanghongngoc(pl);
                            break;
                        case 1206:
                            Input.gI().createFormChangeNameByItem(pl);
                            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                            break;

                        case 1265:
                            Input.gI().phieutangdiem(pl);
                            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                            break;
                        case 1266:
                            Input.gI().phieutangdiemvip(pl);
                            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                            break;

                        case 737:
                            Item itemReward;
                            if (Util.isTrue(10, 100)) {
                                itemReward = useCapsuleTrungThu().get(0);
                            } else if (Util.isTrue(1, 100)) {
                                itemReward = useCapsuleTrungThu().get(1);
                            } else {
                                itemReward = useCapsuleTrungThu().get(Util.nextInt(2, useCapsuleTrungThu().size() - 1));
                            }
                            if (itemReward != null) {
                                InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                                InventoryService.gI().addItemBag(pl, itemReward, 1);
                                InventoryService.gI().sendItemBags(pl);
                                CombineServiceNew.gI().sendEffectOpenItem(pl, item.template.iconID, itemReward.template.iconID);
                                Service.gI().sendThongBao(pl, "Bạn vừa nhận được "
                                        + (itemReward.template.id == 76 || itemReward.template.id == 861 ? "x" + itemReward.quantity + " " : "")
                                        + itemReward.template.name);
                            }
                            break;

                        case ConstItem.BON_TAM_GO:
                        case ConstItem.BON_TAM_VANG:
                            useBonTam(pl, item);
                            break;
                        case ConstItem.GOI_10_RADA_DO_NGOC:
                            findNamekBall(pl, item);
                            break;
                        case 2001:
                            capsule8thang3(pl, item);
                            break;

                        case 1447:
                            QCMM(pl, item);
                            break;
                        case 1448:
                            QCMMGOKU(pl, item);
                            break;
                        case 1449:
                            QCMMVAGETA(pl, item);
                            break;
                        case 1450: //cskb
                            openskill9(pl, item);
                            break;
                        case 1451:
                            QCMMVAGETA1(pl, item);
                            break;
                        case 1452:
                            QCMMVAGETA2(pl, item);
                            break;
                        case 1453:
                            QCMMVAGETA3(pl, item);
                            break;
                        case 1454:
                            QCMMVAGETA4(pl, item);
                            break;
                        case 1455:
                            QCMMVAGETA5(pl, item);
                            break;
                        case 1456:
                            QCMMVAGETA6(pl, item);
                            break;
                        case 1551:
                            hoathan(pl, item);
                            break;
                        case 1574:
                            ruonghaitac(pl, item);
                            break;
                        case 1575:
                            ruonghaitacss(pl, item);
                            break;
                        case 1576:
                            ruonghaitacsss(pl, item);
                            break;
                        case 962:
                            capsuleThoiTrang5DAY(pl, item);
                            break;
                        case 963:
                            capsuleThoiTrang7DAY(pl, item);
                            break;
                        case 964:
                            capsulelinhthu7ngay(pl, item);
                            break;
                        case 965:
                            capsulephukien7ngay(pl, item);
                            break;
                        case 966:
                            capsulevatpham7ngay(pl, item);
                            break;

                        case 1145:
                            openRandomVND(pl, item);
                        case 1594:
                            banbitcoin(pl, item);

                            break;
                        case 1146:
                            openRandomVNDLon(pl, item);
                            break;
                        case 1147:
                            openRandomVNDNAP(pl, item);
                            break;

                        case 1719:
                            String qtslno = "";
                            if (Client.gI().getPlayers().size() >= 10) {
                                qtslno += "Mỗi 60s nhận: " + Client.gI().getPlayers().size() + " 1hn, "
                                        + Client.gI().getPlayers().size() / 10 + " hồng ngọc. Quà theo số lượng người online";
                            }
                            NpcService.gI().createMenuConMeo(pl, ConstNpc.bunthoi, -1,
                                    "|8|Ngân Hàng Triệu Đô Toàn Quốc\n"
                                    + "|7|TRUONGCOMBANK\n"
                                    + "|8|Cộng Hòa Xã Hội Chủ Nghĩa Việt Nam\n"
                                    + "Độc Lập - Tự Do - Hạnh Phúc\n"
                                    + "|7|ATM ONLINE\n"
                                    + "Đã Nạp: " + Util.cap(pl.getSession().tongnap) + " Vnđ\n"
                                    + "Nạp Tuần: " + Util.cap(pl.naptuan) + " Vnđ\n"
                                    + "Nạp Tháng: " + Util.cap(pl.napthang) + " Vnđ\n"
                                     + "Mốc Đã Nhận: " + Util.cap(pl.mocnap) + " \n"
                                    ,
                                     "Rút Lưu Ly", "Rút\nThỏi Vàng", "Rút\nHồng Ngọc", "Rút\nNgọc Xanh",
                                    "Rút\nĐậu God", "Mốc Nạp", "Mốc\nSăn Boss"
                            );
                            break;

                        case 1973:
                            pl.nPoint.hpg += Util.nextInt(1, 5000);
                            pl.nPoint.mpg += Util.nextInt(1, 5000);
                            Service.getInstance().sendThongBaoOK(pl, "Bạn Được Random hp Ki Gốc May Mắn...");
                            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                            Service.getInstance().point(pl);
                            InventoryService.gI().sendItemBags(pl);
                            Service.getInstance().sendThongBao(pl, pl.name + " Đã Ăn Random Chỉ số Gốc " + item.template.name + " Lực Chiến Tăng cao vọt..... ");

                            break;
                        case 1974:
                            pl.pet.nPoint.hpg += Util.nextInt(1, 5000);
                            pl.pet.nPoint.mpg += Util.nextInt(1, 5000);
                            Service.getInstance().sendThongBaoOK(pl, "Bạn Được Random hp Ki Gốc Pét May Mắn...");
                            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                            Service.getInstance().point(pl);
                            InventoryService.gI().sendItemBags(pl);
                            Service.getInstance().sendThongBao(pl, pl.name + " Đã Ăn Random Chỉ số Gốc pét " + item.template.name + " Lực Chiến Tăng cao vọt..... ");

                            break;

                        case 1975:
                            pl.nPoint.dameg += Util.nextInt(1, 5000);
                            Service.getInstance().sendThongBaoOK(pl, "Bạn Được Random Sức Đánh Gốc May Mắn...");
                            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                            Service.getInstance().point(pl);
                            InventoryService.gI().sendItemBags(pl);
                            Service.getInstance().sendThongBao(pl, pl.name + " Đã Ăn Random Chỉ số Gốc " + item.template.name + " Lực Chiến Tăng cao vọt..... ");

                            break;
                        case 1976:
                            pl.nPoint.defg += 1000;
                            Service.getInstance().sendThongBaoOK(pl, "Bạn Được Random giáp Gốc May Mắn...");
                            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                            Service.getInstance().point(pl);
                            InventoryService.gI().sendItemBags(pl);
                            Service.getInstance().sendThongBao(pl, pl.name + " Đã ăn Item chỉ số gốc 1k Def " + item.template.name + " Lực Chiến Tăng cao vọt..... ");

                            break;
                        case 1977:
                            pl.nPoint.power += 10000000000l;
                            pl.nPoint.tiemNang += 10000000000l;
                            Service.getInstance().sendThongBaoOK(pl, "Bạn Nhận Được 10 tỷ sức mạnh và tiềm năng ");
                            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                            Service.getInstance().point(pl);
                            InventoryService.gI().sendItemBags(pl);
                            Service.getInstance().sendThongBao(pl, pl.name + " Đã Ăn 10 Tỷ SMTN " + item.template.name + " Lực Chiến Tăng cao vọt..... ");

                            break;
                        case 1978:
                            pl.pet.nPoint.power += 10000000000l;
                            pl.pet.nPoint.tiemNang += 10000000000l;
                            Service.getInstance().sendThongBaoOK(pl, "Bạn Nhận Được 10 tỷ Tiềm Năng pet ");
                            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                            Service.getInstance().point(pl);
                            InventoryService.gI().sendItemBags(pl);
                            Service.getInstance().sendThongBao(pl, "Con Nghiện " + pl.name + " Đã Ăn Item  20 tỷ Sức mạnh Tn Cho Đệ Tử " + item.template.name + " Lực Chiến Tăng cao vọt..... ");

                            break;

                        case 1980:

                            pl.pet.nPoint.dameg += 10000;
                            Service.getInstance().sendThongBaoOK(pl, "Bạn Ăn Tăng 10k dame gốc cho đệ tử");
                            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                            Service.getInstance().point(pl);
                            InventoryService.gI().sendItemBags(pl);
                            Service.getInstance().sendThongBao(pl, pl.name + " Đã ăn Item chỉ số gốc  +10k sdg " + item.template.name + " Lực Chiến Tăng cao vọt..... ");

                            break;
                        case 1981:
                            pl.nPoint.critg += 1;
                            Service.getInstance().sendThongBaoOK(pl, "vừa ăn item nhận 1 chí mạng gốc");
                            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                            Service.getInstance().point(pl);
                            InventoryService.gI().sendItemBags(pl);
                            Service.getInstance().sendThongBao(pl, pl.name + " Đã ăn Item chỉ số gốc  +1 chí mạng " + item.template.name + " Lực Chiến Tăng cao vọt..... ");

                            break;

//                        case 1493:
//
//                            PlayerDAO.congiten(pl, 2000);
//                            Service.getInstance().sendThongBaoOK(pl, "Mày Vừa Hốc item buff bẩn do admin cho +2.000 vnđ Nhé con chó");
//                            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
//                            InventoryService.gI().sendItemBags(pl);
//                             Service.getInstance().sendThongBao(pl, pl.name  + " Đã Hốc Item 2k vnđ  +2k vnđ " + item.template.name + " đại za thần thái Tăng cao vọt..... ");
//
//                            break;
//
//                        case 1494:
//
//                            PlayerDAO.congiten(pl, 5000);
//                            Service.getInstance().sendThongBaoOK(pl, "Mày Vừa Hốc item buff bẩn do admin cho +5.000 vnđ Nhé con chó");
//                            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
//                            InventoryService.gI().sendItemBags(pl);
//                              Service.getInstance().sendThongBao(pl, pl.name  + " Đã Hốc Item 5k vnđ  +5k vnđ " + item.template.name + " đại za thần thái Tăng cao vọt..... ");
//
//                            break;
//
//                        case 1495:
//
//                            PlayerDAO.congiten(pl, 10000);
//                            Service.getInstance().sendThongBaoOK(pl, "Mày Vừa Hốc item buff bẩn do admin cho +10.000 vnđ Nhé con chó");
//                            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
//                            InventoryService.gI().sendItemBags(pl);
//                            Service.getInstance().sendThongBao(pl, pl.name  + " Đã Hốc Item 10k vnđ  +10k vnđ " + item.template.name + " đại za thần thái Tăng cao vọt..... ");
//
//                            break;
//
//                        case 1496:
//
//                            PlayerDAO.congiten(pl, 20000);
//                            Service.getInstance().sendThongBaoOK(pl, "Mày Vừa Hốc item buff bẩn do admin cho +20.000 vnđ Nhé con chó");
//                            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
//                            InventoryService.gI().sendItemBags(pl);
//                             Service.getInstance().sendThongBao(pl, pl.name  +  " Đã Hốc Item 20k vnđ  +20k vnđ " + item.template.name + " đại za thần thái Tăng cao vọt..... ");
//
//                            break;
//
//                        case 1497:
//
//                            PlayerDAO.congiten(pl, 50000);
//                            Service.getInstance().sendThongBaoOK(pl, "Mày Vừa Hốc item buff bẩn do admin cho +50.000 vnđ Nhé con chó");
//                            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
//                            InventoryService.gI().sendItemBags(pl);
//                           Service.getInstance().sendThongBao(pl, pl.name  + " Đã Hốc Item 50k vnđ  +50k vnđ " + item.template.name + " đại za thần thái Tăng cao vọt..... ");
//
//                            break;
//
//                        case 1498:
//
//                            PlayerDAO.congiten(pl, 10000);
//                            Service.getInstance().sendThongBaoOK(pl, "Mày Vừa Hốc item buff bẩn do admin cho +100.000 vnđ Nhé con chó");
//                            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
//                            InventoryService.gI().sendItemBags(pl);
//                             Service.getInstance().sendThongBao(pl, pl.name  + " Đã Hốc Item 100k vnđ  +100k vnđ " + item.template.name + " đại za thần thái Tăng cao vọt..... ");
//
//                            break;
//
//                        case 1499:
//
//                            PlayerDAO.congiten(pl, 200000);
//                            Service.getInstance().sendThongBaoOK(pl, "Mày Vừa Hốc item buff bẩn do admin cho +200.000 vnđ Nhé con chó");
//                            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
//                            InventoryService.gI().sendItemBags(pl);
//                              Service.getInstance().sendThongBao(pl, pl.name  + " Đã Hốc Item 2k vnđ  +2k vnđ " + item.template.name + " đại za thần thái Tăng cao vọt..... ");
//
//                            break;
//
//                        case 1500:
//
//                            PlayerDAO.congiten(pl, 500000);
//                            Service.getInstance().sendThongBaoOK(pl, "Mày Vừa Hốc item buff bẩn do admin cho +500.000 vnđ Nhé con chó");
//                            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
//                            InventoryService.gI().sendItemBags(pl);
//                             Service.getInstance().sendThongBao(pl, pl.name  +  " Đã Hốc Item 500k vnđ  +500k vnđ " + item.template.name + " đại za thần thái Tăng cao vọt..... ");
//
//                            break;
                        case 1549:// hop qua skh, item 2002 xd
                            UseItem.gI().hopQuaKichHoat(pl, item);
                            break;

                        case 1501:
                            String qtslno1 = "";
                            if (Client.gI().getPlayers().size() >= 10) {
                                qtslno1 += "Mỗi 60s nhận: " + Client.gI().getPlayers().size() + " 1hn, "
                                        + Client.gI().getPlayers().size() / 10 + " hồng ngọc. Quà theo số lượng người online";
                            }
                            NpcService.gI().createMenuConMeo(pl, ConstNpc.tindung, -1,
                                    "|7|Chúc Bạn Năm Mới An Khang\n"
                                    + "|6|Xã Hội Chủ Nghĩa Việt Nam\n"
                                    + "|6|Độc Lập-Tự Do-Hạnh Phúc\n"
                                    + "Thẻ Tín Dụng VietComBank Visa Quốc Tế\n",
                                    "Số Dư", "Điểm boss", "Tích Lũy", "Rút\n KNB", "Phá Sản");

                            break;

                        //pl.getSession().vnd += 50;
//                        case 1983:
//                            BossManager.gI().showListBoss(pl);
//                            //    Service.getInstance().sendThongBao(pl, "Soi Cái Đầu cặc");
//                            break;
                        case 574:
                            UseItem.gI().capsuvang(pl);
                            break;

                        case 2039:
                            openboxsukien(pl, item, ConstEvent.SU_KIEN_TET);
                            break;
                        case 570:
                            openWoodChest(pl, item);
                            break;
                        case 982:
                            hopquagiangsinh(pl, item);
                            break;
                        case 2024:
                            hopQuaTanThu(pl, item);
                            break;

                        case 992:
                            ChangeMapService.gI().goToPrimaryForest(pl);
                            break;

                        case 1277:
                            if (pl.pet == null) {
                                Service.getInstance().sendThongBao(pl, "Ngươi làm gì có đệ tử?");
                                break;
                            }

                            if (pl.pet.playerSkill.skills.get(1).skillId != -1) {
                                pl.pet.openSkill2();
                                InventoryService.gI().subQuantityItem(pl.inventory.itemsBag, item, 1);
                                InventoryService.gI().sendItemBags(pl);
                                Service.getInstance().sendThongBao(pl, "Đã đổi thành công chiêu 2 đệ tử");
                            } else {
                                Service.getInstance().sendThongBao(pl, "Đệ tử bạn chưa mở chiêu 2");
                            }
                            break;
                        case 1278:
                            if (pl.pet == null) {
                                Service.getInstance().sendThongBao(pl, "Ngươi làm gì có đệ tử?");
                                break;
                            }

                            if (pl.pet.playerSkill.skills.get(2).skillId != -1) {
                                pl.pet.openSkill3();
                                InventoryService.gI().subQuantityItem(pl.inventory.itemsBag, item, 1);
                                InventoryService.gI().sendItemBags(pl);
                                Service.getInstance().sendThongBao(pl, "Đã đổi thành công chiêu 3 đệ tử");
                            } else {
                                Service.getInstance().sendThongBao(pl, "Đệ tử bạn chưa mở chiêu 3");
                            }
                            break;
                        case 1279:
                            if (pl.pet == null) {
                                Service.getInstance().sendThongBao(pl, "Ngươi làm gì có đệ tử?");
                                break;
                            }

                            if (pl.pet.playerSkill.skills.get(3).skillId != -1) {
                                pl.pet.openSkill4();
                                InventoryService.gI().subQuantityItem(pl.inventory.itemsBag, item, 1);
                                InventoryService.gI().sendItemBags(pl);
                                Service.getInstance().sendThongBao(pl, "Đã đổi thành công chiêu 4 đệ tử");
                            } else {
                                Service.getInstance().sendThongBao(pl, "Đệ tử bạn chưa mở chiêu 4");
                            }
                            break;

                        case 2006: //phiếu cải trang hải tặc
                        case 2007: //phiếu cải trang hải tặc
                        case 2008: //phiếu cải trang hải tặc
                            openPhieuCaiTrangHaiTac(pl, item);
                            break;
                        case 1319:// hộp cải trang hit
                            openCTHIT(pl, item);
                            break;
                        case 1320:// hộp vật phẩm đeo lưng
                            openhopvatpham(pl, item);
                            break;
                        case 1332:
                            openhopvatphamvip(pl, item);
                            break;
                        case 1321: // hộp ván bay
                            openhopvanbay(pl, item);
                            break;

                        case 1322: // hộp pet
                            openhoppet(pl, item);
                            break;

                        case 2051: // hộp pet
                            hopqua20thang11(pl, item);
                            break;
                        case 1323: // hộp pet
                            openhoplinhthu(pl, item);
                            break;
                        case 2012: //Hop Qua Kich Hoat
                            openboxsukien(pl, item, 1);
                            break;
//                        case 2020: //phiếu cải trang 20/10
//                            openbox2010(pl, item);
//                            break;
//                        case 2021:
//                            openboxsukien(pl, item, 2);
//                            break;
                        case 211: //nho tím
                        case 212: //nho xanh
                            eatGrapes(pl, item);
                            break;
                        case 380: //cskb
                            openCSKB(pl, item);
                            break;
                        case 381: //cuồng nộ
                        case 382: //bổ huyết
                        case 383: //bổ khí
                        case 384: //giáp xên
                        case 385: //ẩn danh
                        case 379: //máy dò
                        //  case 1624: //máy dò  
                        case 663: //bánh pudding
                        case 664: //xúc xíc
                        case 665: //kem dâu
                        case 666: //mì ly
                        case 667: //sushi
                        case 752:
                        case 753:
                        case 1150:
                        case 1152:
                        case 1153:
                        case 1151:
                        case ConstItem.DUOI_KHI:
                        case 465:
                        case 466:
                        case 472:
                        case 890:
                        case 891:
                        case 1128:
                        case 1129:
                        case 1130:
                        case 1635:
                        case 1636:
                        case 1637:
                        case 1281:
                        case 1282:
                        case 1283:
                        case 1284:
                        case 1285:
                        case 1487:
                        case 1488:
                        case 1489:
                        case 1511:
                        case 1512:
                        case 1513:
                        case 1514:
                        case 1515:
                        case 1516:
                        case 1517:
                            useItemTime(pl, item);
                            break;
                        case 473:
                            useHopBanhTrungThu(pl, item);
                            break;
                        case 521: //tdlt
                            useTDLT(pl, item);
                            break;
                        case 1890:
                            useOffline(pl, item);
                            break;
                        case 454: //bông tai
                            usePorata(pl);
                            break;
                        case 921: //bông tai c2
                            UseItem.gI().usePorata2(pl);
                            break;
                        case 1624: //bông tai c2
                            UseItem.gI().usePorataSS(pl);
                            break;
                        case 1625: //bông tai c2
                            UseItem.gI().usePorataSSS(pl);
                            break;
                        case 1280: {
                            if (InventoryService.gI().getCountEmptyBag(pl) < 11) {
                                Service.getInstance().sendThongBao(pl, "Hành trang không đủ chỗ trống");
                            } else {
                                InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                                Item linhThu = ItemService.gI().createNewItem((short) 457);
                                Item linhThu1 = ItemService.gI().createNewItem((short) 441);
                                Item linhThu2 = ItemService.gI().createNewItem((short) 442);
                                Item linhThu3 = ItemService.gI().createNewItem((short) 443);
                                Item linhThu4 = ItemService.gI().createNewItem((short) 444);
                                Item linhThu5 = ItemService.gI().createNewItem((short) 445);
                                Item linhThu6 = ItemService.gI().createNewItem((short) 446);
                                Item linhThu7 = ItemService.gI().createNewItem((short) 447);
                                linhThu.quantity = 20;
                                linhThu1.quantity = 10;
                                linhThu2.quantity = 10;
                                linhThu3.quantity = 10;
                                linhThu4.quantity = 10;
                                linhThu5.quantity = 10;
                                linhThu6.quantity = 10;
                                linhThu7.quantity = 10;
                                linhThu.itemOptions.add(new ItemOption(30, 1));
                                InventoryService.gI().addItemBag(pl, linhThu, 0);
                                linhThu1.itemOptions.add(new ItemOption(95, 5));
                                InventoryService.gI().addItemBag(pl, linhThu1, 0);
                                linhThu2.itemOptions.add(new ItemOption(96, 5));
                                InventoryService.gI().addItemBag(pl, linhThu2, 0);
                                linhThu3.itemOptions.add(new ItemOption(97, 5));
                                InventoryService.gI().addItemBag(pl, linhThu3, 0);
                                linhThu4.itemOptions.add(new ItemOption(98, 5));
                                InventoryService.gI().addItemBag(pl, linhThu4, 0);
                                linhThu5.itemOptions.add(new ItemOption(99, 5));
                                InventoryService.gI().addItemBag(pl, linhThu5, 0);
                                linhThu6.itemOptions.add(new ItemOption(100, 5));
                                InventoryService.gI().addItemBag(pl, linhThu6, 0);
                                linhThu7.itemOptions.add(new ItemOption(101, 5));
                                InventoryService.gI().addItemBag(pl, linhThu7, 0);
                                InventoryService.gI().sendItemBags(pl);
                                Service.getInstance().sendThongBao(pl, "Chúc mừng bạn mở hộp tân thủ");
                            }
                        }
                        break;
                        case 193: //gói 10 viên capsule
                            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                        case 194: //capsule đặc biệt
                            openCapsuleUI(pl);
                            break;
                        case 401: //đổi đệ tử
                            changePet(pl, item);
                            break;
                        case 402: //sách nâng chiêu 1 đệ tử
                        case 403: //sách nâng chiêu 2 đệ tử
                        case 404: //sách nâng chiêu 3 đệ tử
                        case 759: //sách nâng chiêu 4 đệ tử
                            upSkillPet(pl, item);
                            break;
                        case 1418: //đổi đệ tử
                            changePetBerus(pl, item);
                            break;
                        case 568: //đổi đệ tử
                            changeMabuPet(pl, item);
                            break;
                        case 1419: //đổi đệ tử
                            changeVidelPet(pl, item);
                            break;
                        case 1929: //đổi đệ tử
                            changeGokuBluePet(pl, item);
                            break;
                        case 1930: //đổi đệ tử
                            changeVegetaBluePet(pl, item);
                            break;
                        case 1931: //đổi đệ tử
                            changeGohanBluePet(pl, item);
                            break;
                        case 1932: //đổi đệ tử
                            changeGokuGodPet(pl, item);
                            break;
                        case 1933: //đổi đệ tử
                            changeVegetaGodPet(pl, item);
                            break;
                        case 1934: //đổi đệ tử
                            changeGohanGodPet(pl, item);
                            break;
                            
                         case 1944: //đổi đệ tử
                            changebroly(pl, item);
                            break;     
                            
                        case 1420:

                            BossManager.gI().showListBoss(pl);
                            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                            break;

                        case 1127:
                            UseItem.gI().setkichhoat(pl, item);
                            break;

                        case ConstItem.CAPSULE_TET_2022:
                            openCapsuleTet2022(pl, item);
                            break;
                        default:
                            switch (item.template.type) {
                                case 7: //sách học, nâng skill
                                    learnSkill(pl, item);
                                    break;
                                case 12: //ngọc rồng các loại
//                                Service.getInstance().sendThongBaoOK(pl, "Bảo trì tính năng.");
                                    controllerCallRongThan(pl, item);
                                    break;
                                case 11: //item flag bag
                                    useItemChangeFlagBag(pl, item);
                                    break;
                                case 74:
                                    InventoryService.gI().itemBagToBody(pl, indexBag);
                                    Service.getInstance().sendFoot(pl, item.template.id);
                                    break;
                                case 77:
                                    InventoryService.gI().itemBagToBody(pl, indexBag);
                                    break;
                                case 99:
                                    InventoryService.gI().itemBagToBody(pl, indexBag);
                                    break;
                                case 115:
                                    InventoryService.gI().itemBagToBody(pl, indexBag);
                                    break;// đấy thế là xong th
                                case 72: {
                                    InventoryService.gI().itemBagToBody(pl, indexBag);
                                    Service.getInstance().sendPetFollow(pl, (short) (item.template.iconID - 1));
                                    break;
                                }
                            }
                    }
                    break;
            }
            InventoryService.gI().sendItemBags(pl);
        } else {
            Service.getInstance().sendThongBaoOK(pl, "Sức mạnh không đủ yêu cầu");
        }
    }

    public void sukiensonlo(Player player, Item item) {
        Item item1 = InventoryService.gI().findItemBagByTemp(player, 1237);
        Item item2 = InventoryService.gI().findItemBagByTemp(player, 1238);
        Item item3 = InventoryService.gI().findItemBagByTemp(player, 1239);
        int soluongitem1 = item1 == null ? 0 : item1.quantity;
        int soluongitem2 = item2 == null ? 0 : item2.quantity;
        int soluongitem3 = item3 == null ? 0 : item3.quantity;
        NpcService.gI().createMenuConMeo(player, ConstNpc.MENU_SUKIENSONLO, ITEM_BAG_TO_BOX, "Hãy thu thập đủ các vật phẩm sau để đổi quà"
                + "\n|1|Thu thập cành khô: " + soluongitem1 + "/33"
                + "\n|1|Thu thập nước suối: " + soluongitem2 + "/33"
                + "\n|1|Thu thập gỗ lớn: " + soluongitem3 + "/33"
                + "\n|7|Lưu ý:"
                + "\n|2| Tiêu diệt quái để thu thập cành khô"
                + "\n|2| Tiêu diệt boss để thu thập nước suối tinh khiết"
                + "\n|2| Làm nhiệm vụ bọ mộng để thu thập gỗ lớn", "Đổi Quà", "Không");
    }

    private void phanbon(Player player, Item item) {
        int soluong = item == null ? 0 : item.quantity;
        NpcService.gI().createMenuConMeo(player, ConstNpc.PHANBON, ITEM_BAG_TO_BOX,
                "Bạn đồng muốn sử dụng phân bón để giảm 20s thời gian trồng dưa hấu không ?"
                + "\n|7|Số phân: " + soluong, "Đồng ý", "Từ chối");
    }

    public void mohop500tr(Player player, Item item) {
        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
            InventoryService.gI().subQuantityItemsBag(player, item, 1);
            InventoryService.gI().sendItemBags(player);
            int gender = player.gender;
            Item caitrang = null;
            short[] icon = new short[2];
            int tile = new Random().nextInt(100);
            if (tile < 40) {
                Item thoivang = ItemService.gI().createNewItem((short) 457, Util.nextInt(1, 3));
                icon[1] = thoivang.template.iconID;
                CombineServiceNew.gI().sendEffectOpenItem(player, icon[0], icon[1]);
                InventoryService.gI().addItemBag(player, thoivang, 99999);
            } else if (tile < 90) {
                switch (gender) {
                    case 0:
                        if (Util.isTrue(80, 100)) {
                            caitrang = ItemService.gI().createNewItem((short) 905, 1);
                            caitrang.itemOptions.add(new ItemOption(50, Util.nextInt(10, 100)));
                            caitrang.itemOptions.add(new ItemOption(77, Util.nextInt(10, 110)));
                            caitrang.itemOptions.add(new ItemOption(103, Util.nextInt(10, 100)));
                            caitrang.itemOptions.add(new ItemOption(236, Util.nextInt(10, 5)));
                        } else {
                            caitrang = ItemService.gI().createNewItem((short) 905, 1);
                            caitrang.itemOptions.add(new ItemOption(50, Util.nextInt(10, 100)));
                            caitrang.itemOptions.add(new ItemOption(77, Util.nextInt(10, 100)));
                            caitrang.itemOptions.add(new ItemOption(103, Util.nextInt(10, 100)));
                            caitrang.itemOptions.add(new ItemOption(236, Util.nextInt(20, 30)));
                        }
                        break;
                    case 1:
                        if (Util.isTrue(80, 100)) {
                            caitrang = ItemService.gI().createNewItem((short) 907, 1);
                            caitrang.itemOptions.add(new ItemOption(50, Util.nextInt(10, 200)));
                            caitrang.itemOptions.add(new ItemOption(77, Util.nextInt(10, 200)));
                            caitrang.itemOptions.add(new ItemOption(103, Util.nextInt(10, 200)));
                            caitrang.itemOptions.add(new ItemOption(238, Util.nextInt(10, 200)));
                        } else {
                            caitrang = ItemService.gI().createNewItem((short) 907, 1);
                            caitrang.itemOptions.add(new ItemOption(50, Util.nextInt(10, 100)));
                            caitrang.itemOptions.add(new ItemOption(77, Util.nextInt(10, 100)));
                            caitrang.itemOptions.add(new ItemOption(103, Util.nextInt(10, 100)));
                            caitrang.itemOptions.add(new ItemOption(238, Util.nextInt(20, 30)));
                        }
                        break;
                    case 2:
                        if (Util.isTrue(80, 100)) {
                            caitrang = ItemService.gI().createNewItem((short) 911, 1);
                            caitrang.itemOptions.add(new ItemOption(50, Util.nextInt(10, 100)));
                            caitrang.itemOptions.add(new ItemOption(77, Util.nextInt(10, 100)));
                            caitrang.itemOptions.add(new ItemOption(103, Util.nextInt(10, 100)));
                            caitrang.itemOptions.add(new ItemOption(237, Util.nextInt(10, 100)));
                        } else {
                            caitrang = ItemService.gI().createNewItem((short) 911, 1);
                            caitrang.itemOptions.add(new ItemOption(50, Util.nextInt(10, 25)));
                            caitrang.itemOptions.add(new ItemOption(77, Util.nextInt(10, 25)));
                            caitrang.itemOptions.add(new ItemOption(103, Util.nextInt(10, 25)));
                            caitrang.itemOptions.add(new ItemOption(237, Util.nextInt(20, 30)));
                        }
                        break;
                }
                icon[1] = caitrang.template.iconID;
                if (Util.isTrue(99, 100)) {
                    caitrang.itemOptions.add(new ItemOption(93, Util.nextInt(1, 2)));
                }
                InventoryService.gI().addItemBag(player, caitrang, 1);
                CombineServiceNew.gI().sendEffectOpenItem(player, icon[0], icon[1]);
            } else {
                if (Util.isTrue(80, 100)) {
                    caitrang = ItemService.gI().createNewItem((short) 884, 1);
                    caitrang.itemOptions.add(new ItemOption(50, Util.nextInt(10, 100)));
                    caitrang.itemOptions.add(new ItemOption(77, Util.nextInt(10, 100)));
                    caitrang.itemOptions.add(new ItemOption(103, Util.nextInt(10, 100)));
                    caitrang.itemOptions.add(new ItemOption(5, Util.nextInt(10, 20)));
                    caitrang.itemOptions.add(new ItemOption(14, Util.nextInt(20, 50)));
                } else {
                    caitrang = ItemService.gI().createNewItem((short) 884, 1);
                    caitrang.itemOptions.add(new ItemOption(50, Util.nextInt(10, 100)));
                    caitrang.itemOptions.add(new ItemOption(77, Util.nextInt(10, 100)));
                    caitrang.itemOptions.add(new ItemOption(103, Util.nextInt(10, 100)));
                    caitrang.itemOptions.add(new ItemOption(5, Util.nextInt(20, 30)));
                    caitrang.itemOptions.add(new ItemOption(14, Util.nextInt(40, 100)));

                }
                icon[1] = caitrang.template.iconID;
                if (Util.isTrue(99, 100)) {
                    caitrang.itemOptions.add(new ItemOption(93, Util.nextInt(1, 2)));
                }
                InventoryService.gI().addItemBag(player, caitrang, 1);
                CombineServiceNew.gI().sendEffectOpenItem(player, icon[0], icon[1]);
            }
            CombineServiceNew.gI().sendEffectOpenItem(player, icon[0], icon[1]);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendMoney(player);
        } else {
            Service.getInstance().sendThongBaoOK(player, "Vui lòng trừa 1 ô hành trang trống");
        }
    }

    private void UseThoiVang(Player player) {
        Item tv = null;
        for (Item item : player.inventory.itemsBag) {
            if (item.isNotNullItem() && item.template.id == 457) {
                tv = item;
                break;
            }
        }
        if (tv != null) {
            if (player.inventory.gold <= player.inventory.getGoldLimit()) {
                InventoryService.gI().subQuantityItemsBag(player, tv, 1);
                player.inventory.gold += 1000000000;
                PlayerService.gI().sendInfoHpMpMoney(player);
                InventoryService.gI().sendItemBags(player);
            } else {
                Service.getInstance().sendThongBao(player, "Max Gioi Hạn Rồi Bạn");
            }
        } else {
            Service.getInstance().sendThongBao(player, "Không thể thực hiện");
        }
    }

    private void UseThoiVangvip(Player player) {
        Item tv = null;
        for (Item item : player.inventory.itemsBag) {
            if (item.isNotNullItem() && item.template.id == 1111) {
                tv = item;
                break;
            }
        }
        if (tv != null /*&& tv.isNotNullItem()*/) {
            if (player.inventory.gold <= player.inventory.getGoldLimit()) {
                InventoryService.gI().subQuantityItemsBag(player, tv, 1);
                player.inventory.gold += 2000000000;
                player.inventory.gem += 10000;
                player.inventory.ruby += 10000;
                PlayerService.gI().sendInfoHpMpMoney(player);
                InventoryService.gI().sendItemBags(player);
            } else {
                Service.getInstance().sendThongBao(player, "Max Gioi Hạn Rồi Bạn");
            }
        } else {
            Service.getInstance().sendThongBao(player, "Không thể thực hiện");
        }
    }

    private void findNamekBall(Player pl, Item item) {
        List<NamekBall> balls = NamekBallManager.gI().getList();

        if (balls.isEmpty()) {
            return;
        }
        StringBuffer sb = new StringBuffer();

        for (NamekBall namekBall : balls) {
            if (namekBall.zone != null && namekBall.zone.map != null) {
                Map m = namekBall.zone.map;
                sb.append(namekBall.getIndex() + 1).append(" Sao: ").append(m.mapName).append(namekBall.getHolderName() == null ? "" : " - " + namekBall.getHolderName()).append("\n");
            }
        }
        final int star = Util.nextInt(0, 6);
        final NamekBall ball = NamekBallManager.gI().findByIndex(star);
        final Inventory inventory = pl.inventory;
        MenuDialog menu = new MenuDialog(sb.toString(), new String[]{"Đến ngay\nViên " + (star + 1) + " Sao\n 50tr Vàng", "Đến ngay\nViên " + (star + 1) + " Sao\n 5 Hồng ngọc"}, new MenuRunable() {
            @Override
            public void run() {
                switch (getIndexSelected()) {
                    case 0:
                        if (inventory.gold < 50000000) {
                            Service.getInstance().sendThongBao(pl, "Không đủ tiền");
                            return;
                        }
                        inventory.subGold(50000000);
                        ChangeMapService.gI().changeMap(pl, ball.zone, ball.x, ball.y);
                        break;
                    case 1:
                        if (inventory.ruby < 5) {
                            Service.getInstance().sendThongBao(pl, "Không đủ tiền");
                            return;
                        }
                        inventory.subRuby(5);
                        ChangeMapService.gI().changeMap(pl, ball.zone, ball.x, ball.y);
                        break;
                }
                if (pl.isHoldNamecBall) {
                    NamekBallWar.gI().dropBall(pl);
                }
                Service.getInstance().sendMoney(pl);
            }
        });
        menu.show(pl);
        InventoryService.gI().sendItemBags(pl);
        InventoryService.gI().subQuantityItemsBag(pl, item, 1);
    }

    private void capsuleThoiTrang5DAY(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            Item it = ItemService.gI().createNewItem((short) Util.nextInt(381, 384), 10);
            it.itemOptions.add(new ItemOption(51, Util.nextInt(1, 100)));
            it.itemOptions.add(new ItemOption(72, Util.nextInt(1, 16)));
            it.itemOptions.add(new ItemOption(30, Util.nextInt(1, 100)));
            InventoryService.gI().addItemBag(pl, it, 1000);
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBags(pl);
            short icon1 = item.template.iconID;
            short icon2 = it.template.iconID;
            CombineServiceNew.gI().sendEffectOpenItem(pl, icon1, icon2);
        } else {
            Service.getInstance().sendThongBao(pl, "Hãy chừa 1 ô trống để mở.");
        }

    }

    private void capsuleThoiTrang7DAY(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            Item it = ItemService.gI().createNewItem((short) Util.nextInt(968, 980));
            it.itemOptions.add(new ItemOption(51, Util.nextInt(1, 100)));
            it.itemOptions.add(new ItemOption(72, Util.nextInt(1, 16)));
            it.itemOptions.add(new ItemOption(50, 100));
            it.itemOptions.add(new ItemOption(77, 100));
            it.itemOptions.add(new ItemOption(103, 100));
            it.itemOptions.add(new ItemOption(79, Util.nextInt(1, 50)));
            it.itemOptions.add(new ItemOption(14, Util.nextInt(1, 7)));
            it.itemOptions.add(new ItemOption(5, Util.nextInt(1, 10)));
            it.itemOptions.add(new ItemOption(106, Util.nextInt(1, 10)));
            //  it.itemOptions.add(new ItemOption(26, 0));
            it.itemOptions.add(new ItemOption(243, Util.nextInt(1, 1500)));
            it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
            InventoryService.gI().addItemBag(pl, it, 0);
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBags(pl);
            short icon1 = item.template.iconID;
            short icon2 = it.template.iconID;
            CombineServiceNew.gI().sendEffectOpenItem(pl, icon1, icon2);
        } else {
            Service.getInstance().sendThongBao(pl, "Hãy chừa 1 ô trống để mở.");
        }

    }

    private void combodanangcap(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            Item it = ItemService.gI().createNewItem((short) Util.nextInt(220, 224), 1000);
            it.itemOptions.add(new ItemOption(30, Util.nextInt(1, 5)));
            InventoryService.gI().addItemBag(pl, it, 0);
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBags(pl);
            short icon1 = item.template.iconID;
            short icon2 = it.template.iconID;
            CombineServiceNew.gI().sendEffectOpenItem(pl, icon1, icon2);
        } else {
            Service.getInstance().sendThongBao(pl, "Hãy chừa 1 ô trống để mở.");
        }

    }

    private void combodabaove(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            Item it = ItemService.gI().createNewItem((short) 987, Util.nextInt(1, 50));
            it.itemOptions.add(new ItemOption(30, Util.nextInt(1, 5)));
            InventoryService.gI().addItemBag(pl, it, 0);
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBags(pl);
            short icon1 = item.template.iconID;
            short icon2 = it.template.iconID;
            CombineServiceNew.gI().sendEffectOpenItem(pl, icon1, icon2);
        } else {
            Service.getInstance().sendThongBao(pl, "Hãy chừa 1 ô trống để mở.");
        }

    }

    private void comboitemc2(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            Item it = ItemService.gI().createNewItem((short) Util.nextInt(1150, 1154), Util.nextInt(1, 99));
            it.itemOptions.add(new ItemOption(30, Util.nextInt(1, 5)));
            InventoryService.gI().addItemBag(pl, it, 0);
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBags(pl);
            short icon1 = item.template.iconID;
            short icon2 = it.template.iconID;
            CombineServiceNew.gI().sendEffectOpenItem(pl, icon1, icon2);
        } else {
            Service.getInstance().sendThongBao(pl, "Hãy chừa 1 ô trống để mở.");
        }

    }

    private void combothoivang(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            Item it = ItemService.gI().createNewItem((short) 457, Util.nextInt(1, 5000));
            it.itemOptions.add(new ItemOption(30, Util.nextInt(1, 5)));
            InventoryService.gI().addItemBag(pl, it, 0);
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBags(pl);
            short icon1 = item.template.iconID;
            short icon2 = it.template.iconID;
            CombineServiceNew.gI().sendEffectOpenItem(pl, icon1, icon2);
        } else {
            Service.getInstance().sendThongBao(pl, "Hãy chừa 1 ô trống để mở.");
        }

    }

    private void combodns(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            Item it = ItemService.gI().createNewItem((short) 674, Util.nextInt(1, 5000));
            it.itemOptions.add(new ItemOption(30, Util.nextInt(1, 5)));
            InventoryService.gI().addItemBag(pl, it, 0);
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBags(pl);
            short icon1 = item.template.iconID;
            short icon2 = it.template.iconID;
            CombineServiceNew.gI().sendEffectOpenItem(pl, icon1, icon2);
        } else {
            Service.getInstance().sendThongBao(pl, "Hãy chừa 1 ô trống để mở.");
        }

    }

    private void combothucan(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            Item it = ItemService.gI().createNewItem((short) Util.nextInt(663, 667), Util.nextInt(1, 50));
            it.itemOptions.add(new ItemOption(30, Util.nextInt(1, 5)));
            InventoryService.gI().addItemBag(pl, it, 0);
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBags(pl);
            short icon1 = item.template.iconID;
            short icon2 = it.template.iconID;
            CombineServiceNew.gI().sendEffectOpenItem(pl, icon1, icon2);
        } else {
            Service.getInstance().sendThongBao(pl, "Hãy chừa 1 ô trống để mở.");
        }

    }

    private void combodaugod(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            Item it = ItemService.gI().createNewItem((short) 1111, 10000);
            it.itemOptions.add(new ItemOption(30, Util.nextInt(1, 5)));
            InventoryService.gI().addItemBag(pl, it, 0);
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBags(pl);
            short icon1 = item.template.iconID;
            short icon2 = it.template.iconID;
            CombineServiceNew.gI().sendEffectOpenItem(pl, icon1, icon2);
        } else {
            Service.getInstance().sendThongBao(pl, "Hãy chừa 1 ô trống để mở.");
        }

    }

    private void combocskb(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            Item it = ItemService.gI().createNewItem((short) 380, Util.nextInt(1, 500));
            it.itemOptions.add(new ItemOption(30, Util.nextInt(1, 5)));
            InventoryService.gI().addItemBag(pl, it, 0);
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBags(pl);
            short icon1 = item.template.iconID;
            short icon2 = it.template.iconID;
            CombineServiceNew.gI().sendEffectOpenItem(pl, icon1, icon2);
        } else {
            Service.getInstance().sendThongBao(pl, "Hãy chừa 1 ô trống để mở.");
        }

    }

    private void capsulelinhthu7ngay(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            Item it = ItemService.gI().createNewItem((short) Util.nextInt(1868, 1882));
            it.itemOptions.add(new ItemOption(51, Util.nextInt(1, 100)));
            it.itemOptions.add(new ItemOption(72, Util.nextInt(1, 16)));
            it.itemOptions.add(new ItemOption(50, 100));
            it.itemOptions.add(new ItemOption(77, 100));
            it.itemOptions.add(new ItemOption(103, 100));

            it.itemOptions.add(new ItemOption(101, Util.nextInt(1, 80)));
            it.itemOptions.add(new ItemOption(107, Util.nextInt(1, 8)));

            it.itemOptions.add(new ItemOption(243, Util.nextInt(1, 1500)));
            it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
            InventoryService.gI().addItemBag(pl, it, 0);
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBags(pl);
            short icon1 = item.template.iconID;
            short icon2 = it.template.iconID;
            CombineServiceNew.gI().sendEffectOpenItem(pl, icon1, icon2);
        } else {
            Service.getInstance().sendThongBao(pl, "Hãy chừa 1 ô trống để mở.");
        }

    }

    private void capsulephukien7ngay(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            Item it = ItemService.gI().createNewItem((short) Util.nextInt(800, 804));
            it.itemOptions.add(new ItemOption(51, Util.nextInt(1, 100)));
            it.itemOptions.add(new ItemOption(72, Util.nextInt(1, 16)));
            it.itemOptions.add(new ItemOption(50, 100));
            it.itemOptions.add(new ItemOption(77, 100));
            it.itemOptions.add(new ItemOption(103, 100));
            it.itemOptions.add(new ItemOption(104, Util.nextInt(1, 5)));

            it.itemOptions.add(new ItemOption(107, Util.nextInt(1, 2)));

            it.itemOptions.add(new ItemOption(243, Util.nextInt(1, 1000)));
            it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
            InventoryService.gI().addItemBag(pl, it, 0);
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBags(pl);
            short icon1 = item.template.iconID;
            short icon2 = it.template.iconID;
            CombineServiceNew.gI().sendEffectOpenItem(pl, icon1, icon2);
        } else {
            Service.getInstance().sendThongBao(pl, "Hãy chừa 1 ô trống để mở.");
        }

    }

    private void capsulevatpham7ngay(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            Item it = ItemService.gI().createNewItem((short) Util.nextInt(220, 224), Util.nextInt(1, 500));
            it.itemOptions.add(new ItemOption(51, Util.nextInt(1, 100)));
            it.itemOptions.add(new ItemOption(72, Util.nextInt(1, 16)));
            it.itemOptions.add(new ItemOption(30, Util.nextInt(1, 1500)));
            InventoryService.gI().addItemBag(pl, it, 9999);
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBags(pl);
            short icon1 = item.template.iconID;
            short icon2 = it.template.iconID;
            CombineServiceNew.gI().sendEffectOpenItem(pl, icon1, icon2);
        } else {
            Service.getInstance().sendThongBao(pl, "Hãy chừa 1 ô trống để mở.");
        }

    }

    private void banbitcoin(Player player, Item item) {
        if (player != null && item != null) {
            int randomRate = 0;
            int rewardVND;
            // Cộng tiền cho người chơi
            if (Util.isTrue(99, 100)) {
                // 80% cơ hội nhận từ 1 - 500 VND
                rewardVND = Util.nextInt(100, 200);
            } else if (Util.isTrue(1, 100)) {
                // 10% cơ hội nhận từ 1,000 - 5,000 VND
                rewardVND = Util.nextInt(100, 500);
            } else {
                // 1% cơ hội nhận từ 100,000 - 1,000,000 VND
                rewardVND = Util.nextInt(0, 50);

            }
            player.getSession().vnd += rewardVND;
            AccountDAO.updateVND(player.getSession());
            // Trừ vật phẩm mở ra tiền
            InventoryService.gI().subQuantityItemsBag(player, item, 1);
            InventoryService.gI().sendItemBags(player);
            // Gửi thông báo
            Service.getInstance().sendThongBaoFromAdmin(player, "Bạn đã Bán Bitcoin Thu Được Lời\n [ " + rewardVND + " ] VNĐ [ Theo Thị Trường Lên Xuống ]\nHãy Tích Cực Hoạt Động Buôn Bán Nhé");
            //   ChatGlobalService.gI().chat(player, "Bạn [" + player.name + "]\n đã Bán Bitcoin Thu Được Lời\n [ " + rewardVND +  " ] VNĐ [ Theo Thị Trường Lên Xuống ]\nHãy Tích Cực Hoạt Động Buôn Bán Nhé");
            // Gửi hiệu ứng (nếu cần)
            CombineServiceNew.gI().sendEffectOpenItem(player, item.template.iconID, (short) 12335);
        } else {
            Service.getInstance().sendThongBao(player, "Không thể mở vật phẩm, vui lòng thử lại.");
        }
    }

    private void openRandomVND(Player player, Item item) {
        if (player != null && item != null) {
            // Xác suất ngẫu nhiên (1-100)
            int randomRate = Util.nextInt(1, 100);
            int rewardVND;

            if (Util.isTrue(99, 100)) {
                // 80% cơ hội nhận từ 1 - 500 VND
                rewardVND = Util.nextInt(1, 500);
            } else if (Util.isTrue(50, 100)) {
                // 10% cơ hội nhận từ 1,000 - 5,000 VND
                rewardVND = Util.nextInt(1000, 5000);
            } else if (Util.isTrue(10, 100)) {
                // 5% cơ hội nhận từ 10,001 - 50,000 VND
                rewardVND = Util.nextInt(1001, 10000);
            } else if (Util.isTrue(1, 100)) {
                // 4% cơ hội nhận từ 50,001 - 100,000 VND
                rewardVND = Util.nextInt(10000, 12000);
            } else {
                // 1% cơ hội nhận từ 100,000 - 1,000,000 VND
                rewardVND = Util.nextInt(20000, 50000);
            }
            // Cộng tiền cho người chơi
            player.getSession().vnd += rewardVND;
            AccountDAO.updateVND(player.getSession());
            // Trừ vật phẩm mở ra tiền
            InventoryService.gI().subQuantityItemsBag(player, item, 1);
            InventoryService.gI().sendItemBags(player);
            // Gửi thông báo
            Service.getInstance().sendThongBao(player, "Bạn đã nhận được " + rewardVND + " VND từ Lixi Nhỏ!");
            //  Service.getInstance().sendThongBaoAllPlayer("Bạn\n|7|[" + player.name + "] \n|4|đã nhận được " + rewardVND + " VND từ Lixi Nhỏ!");
            // Gửi hiệu ứng (nếu cần)
            CombineServiceNew.gI().sendEffectOpenItem(player, item.template.iconID, (short) -1);
        } else {
            Service.getInstance().sendThongBao(player, "Không thể mở vật phẩm, vui lòng thử lại.");
        }
    }

    private void openRandomVNDLon(Player player, Item item) {
        if (player != null && item != null) {
            // Xác suất ngẫu nhiên (1-100)
            int randomRate = Util.nextInt(1, 100);
            int rewardVND;

            if (Util.isTrue(99, 100)) {
                // 80% cơ hội nhận từ 1 - 500 VND
                rewardVND = Util.nextInt(1000, 5000);
            } else if (Util.isTrue(10, 100)) {
                // 10% cơ hội nhận từ 1,000 - 5,000 VND
                rewardVND = Util.nextInt(7000, 12000);
            } else if (Util.isTrue(5, 100)) {
                // 5% cơ hội nhận từ 10,001 - 50,000 VND
                rewardVND = Util.nextInt(15000, 32000);
            } else if (Util.isTrue(1, 100)) {
                // 4% cơ hội nhận từ 50,001 - 100,000 VND
                rewardVND = Util.nextInt(35000, 40000);
            } else {
                // 1% cơ hội nhận từ 100,000 - 1,000,000 VND
                rewardVND = Util.nextInt(40000, 50000);
            }
            // Cộng tiền cho người chơi
            player.getSession().vnd += rewardVND;
            AccountDAO.updateVND(player.getSession());
            // Trừ vật phẩm mở ra tiền
            InventoryService.gI().subQuantityItemsBag(player, item, 1);
            InventoryService.gI().sendItemBags(player);
            // Gửi thông báo
            Service.getInstance().sendThongBao(player, "Bạn đã nhận được " + rewardVND + " VND từ Lixi Lớn!");
            //   Service.getInstance().sendThongBaoAllPlayer("Bạn\n|7|[" + player.name + "] \n|4|đã nhận được " + rewardVND + " VND từ Lixi Lớn!");
            // Gửi hiệu ứng (nếu cần)
            CombineServiceNew.gI().sendEffectOpenItem(player, item.template.iconID, (short) -1);
        } else {
            Service.getInstance().sendThongBao(player, "Không thể mở vật phẩm, vui lòng thử lại.");
        }
    }

    private void openRandomVNDNAP(Player player, Item item) {
        if (player != null && item != null) {
            // Xác suất ngẫu nhiên (1-100)
            int randomRate = Util.nextInt(1, 100);
            int rewardVND;

            if (Util.isTrue(99, 100)) {
                // 80% cơ hội nhận từ 1 - 500 VND
                rewardVND = Util.nextInt(1000, 7000);
            } else if (Util.isTrue(10, 100)) {
                // 10% cơ hội nhận từ 1,000 - 5,000 VND
                rewardVND = Util.nextInt(10000, 15000);
            } else if (Util.isTrue(5, 100)) {
                // 5% cơ hội nhận từ 10,001 - 50,000 VND
                rewardVND = Util.nextInt(10000, 55000);
            } else if (Util.isTrue(1, 100)) {
                // 4% cơ hội nhận từ 50,001 - 100,000 VND
                rewardVND = Util.nextInt(10000, 60000);
            } else {
                // 1% cơ hội nhận từ 100,000 - 1,000,000 VND
                rewardVND = Util.nextInt(10000, 100000);
            }
            // Cộng tiền cho người chơi
            player.getSession().vnd += rewardVND;
            AccountDAO.updateVND(player.getSession());
            // Trừ vật phẩm mở ra tiền
            InventoryService.gI().subQuantityItemsBag(player, item, 1);
            InventoryService.gI().sendItemBags(player);
            // Gửi thông báo
            Service.getInstance().sendThongBao(player, "Bạn đã nhận được " + rewardVND + " VND từ Lixi Quý Tộc!");
            //  Service.getInstance().sendThongBaoAllPlayer("Bạn\n|7|[" + player.name + "] \n|4|đã nhận được " + rewardVND + " VND từ Lixi Quý Tộc!");
            // Gửi hiệu ứng (nếu cần)
            CombineServiceNew.gI().sendEffectOpenItem(player, item.template.iconID, (short) -1);
        } else {
            Service.getInstance().sendThongBao(player, "Không thể mở vật phẩm, vui lòng thử lại.");
        }
    }

    private void useBonTam(Player player, Item item) {
        if (!player.zone.map.isMapLang()) {
            Service.getInstance().sendThongBaoOK(player, "Chỉ có thể sự dụng ở các làng");
            return;
        }
        if (player.event.isUseBonTam()) {
            Service.getInstance().sendThongBaoOK(player, "Không thể sử dụng khi đang tắm");
            return;
        }
        int itemID = item.template.id;
        RandomCollection<Integer> rd = new RandomCollection<>();
        rd.add(1, ConstItem.QUAT_BA_TIEU);
        rd.add(1, ConstItem.CAY_KEM);
        rd.add(1, ConstItem.CA_HEO);
        rd.add(1, ConstItem.DIEU_RONG);

        if (itemID == ConstItem.BON_TAM_GO) {
            rd.add(1, ConstItem.CON_DIEU);
        } else {//bồn tắm vàng
            rd.add(1, ConstItem.XIEN_CA);
            rd.add(1, ConstItem.PHONG_LON);
            rd.add(1, ConstItem.CAI_TRANG_POC_BIKINI_2023);
            rd.add(1, ConstItem.CAI_TRANG_PIC_THO_LAN_2023);
            rd.add(1, ConstItem.CAI_TRANG_KING_KONG_SANH_DIEU_2023);
        }

        int rwID = rd.next();
        Item rw = ItemService.gI().createNewItem((short) rwID);
        if (rw.template.type == 11) {// đồ đeo lưng
            //option
            rw.itemOptions.add(new ItemOption(50, Util.nextInt(5, 15)));
            rw.itemOptions.add(new ItemOption(77, Util.nextInt(5, 15)));
            rw.itemOptions.add(new ItemOption(103, Util.nextInt(5, 15)));
            rw.itemOptions.add(new ItemOption(5, Util.nextInt(15, 25)));
        } else {// cải trang
            //option
            rw.itemOptions.add(new ItemOption(50, Util.nextInt(20, 40)));
            rw.itemOptions.add(new ItemOption(77, Util.nextInt(20, 40)));
            rw.itemOptions.add(new ItemOption(103, Util.nextInt(20, 40)));
            rw.itemOptions.add(new ItemOption(199, 0));
        }

        if (rwID == ConstItem.QUAT_BA_TIEU || rwID == ConstItem.VO_OC || rwID == ConstItem.CAY_KEM || rwID == ConstItem.CA_HEO) {
            //hsd
            rw.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));

        } else if (rwID == ConstItem.XIEN_CA || rwID == ConstItem.PHONG_LON || rwID == ConstItem.CAI_TRANG_POC_BIKINI_2023
                || rwID == ConstItem.CAI_TRANG_PIC_THO_LAN_2023 || rwID == ConstItem.CAI_TRANG_KING_KONG_SANH_DIEU_2023) {
            // hsd - vinh vien
            if (Util.isTrue(1, 30)) {
                rw.itemOptions.add(new ItemOption(174, 2025));
            } else {
                rw.itemOptions.add(new ItemOption(174, 2025));
                rw.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
            }

        }

        int delay = itemID == ConstItem.BON_TAM_GO ? 3 : 1;
        ItemTimeService.gI().sendItemTime(player, 3779, 60 * delay);
        EffectSkillService.gI().startStun(player, System.currentTimeMillis(), 60000 * delay);
        InventoryService.gI().subQuantityItemsBag(player, item, 1);
        InventoryService.gI().sendItemBags(player);
        player.event.setUseBonTam(true);
        Util.setTimeout(() -> {
            InventoryService.gI().addItemBag(player, rw, 99);
            InventoryService.gI().sendItemBags(player);
            player.event.setUseBonTam(false);
        }, 60000 * delay);
    }

    private void openCapsuleTet2022(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) == 0) {
            Service.getInstance().sendThongBao(pl, "Hãy chừa 1 ô trống để mở.");
            return;
        }
        RandomCollection<Integer> rdItemID = new RandomCollection<>();
        rdItemID.add(1, ConstItem.PHAO_HOA);
        rdItemID.add(1, ConstItem.XIEN_CA);
        rdItemID.add(1, ConstItem.NON_HO_VANG);
        switch (pl.gender) {
            case 0:
                rdItemID.add(1, ConstItem.NON_TRAU_MAY_MAN);
                rdItemID.add(1, ConstItem.NON_CHUOT_MAY_MAN);
                break;
            case 1:
                rdItemID.add(1, ConstItem.NON_TRAU_MAY_MAN_847);
                rdItemID.add(1, ConstItem.NON_CHUOT_MAY_MAN_755);
                break;
            default:
                rdItemID.add(1, ConstItem.NON_TRAU_MAY_MAN_848);
                rdItemID.add(1, ConstItem.NON_CHUOT_MAY_MAN_756);
                break;
        }
        rdItemID.add(1, ConstItem.CAI_TRANG_HO_VANG);
        rdItemID.add(1, ConstItem.HO_MAP_VANG);
//        rdItemID.add(2, ConstItem.SAO_PHA_LE);
//        rdItemID.add(2, ConstItem.SAO_PHA_LE_442);
//        rdItemID.add(2, ConstItem.SAO_PHA_LE_443);
//        rdItemID.add(2, ConstItem.SAO_PHA_LE_444);
//        rdItemID.add(2, ConstItem.SAO_PHA_LE_445);
//        rdItemID.add(2, ConstItem.SAO_PHA_LE_446);
//        rdItemID.add(2, ConstItem.SAO_PHA_LE_447);
        rdItemID.add(2, ConstItem.DA_LUC_BAO);
        rdItemID.add(2, ConstItem.DA_SAPHIA);
        rdItemID.add(2, ConstItem.DA_TITAN);
        rdItemID.add(2, ConstItem.DA_THACH_ANH_TIM);
        rdItemID.add(2, ConstItem.DA_RUBY);
        rdItemID.add(3, ConstItem.VANG_190);
        int itemID = rdItemID.next();
        Item newItem = ItemService.gI().createNewItem((short) itemID);
        switch (newItem.template.type) {
            case 9:
                newItem.quantity = Util.nextInt(10, 50) * 1000000;
                break;
            case 14:
            case 30:
                newItem.quantity = 10;
                break;
            default:
                switch (itemID) {
                    case ConstItem.XIEN_CA: {
                        RandomCollection<ItemOption> rdOption = new RandomCollection<>();
                        rdOption.add(2, new ItemOption(77, 30));//%hp
                        rdOption.add(2, new ItemOption(103, 30));//%hp
                        rdOption.add(1, new ItemOption(50, 30));//%hp
                        newItem.itemOptions.add(rdOption.next());
                    }
                    break;

                    case ConstItem.HO_MAP_VANG: {
                        newItem.itemOptions.add(new ItemOption(77, Util.nextInt(10, 35)));
                        newItem.itemOptions.add(new ItemOption(103, Util.nextInt(10, 35)));
                        newItem.itemOptions.add(new ItemOption(50, Util.nextInt(10, 35)));
                    }
                    break;

                    case ConstItem.NON_HO_VANG:
                    case ConstItem.CAI_TRANG_HO_VANG:
                    case ConstItem.NON_TRAU_MAY_MAN:
                    case ConstItem.NON_TRAU_MAY_MAN_847:
                    case ConstItem.NON_TRAU_MAY_MAN_848:
                    case ConstItem.NON_CHUOT_MAY_MAN:
                    case ConstItem.NON_CHUOT_MAY_MAN_755:
                    case ConstItem.NON_CHUOT_MAY_MAN_756:
                        newItem.itemOptions.add(new ItemOption(77, 30));
                        newItem.itemOptions.add(new ItemOption(103, 30));
                        newItem.itemOptions.add(new ItemOption(50, 30));
                        break;
                }
                RandomCollection<Integer> rdDay = new RandomCollection<>();
                rdDay.add(6, 3);
                rdDay.add(3, 7);
                rdDay.add(1, 15);
                int day = rdDay.next();
                newItem.itemOptions.add(new ItemOption(93, day));
                break;
        }
        short icon1 = item.template.iconID;
        short icon2 = newItem.template.iconID;
        if (newItem.template.type == 9) {
            Service.getInstance().sendThongBao(pl, "Bạn nhận được " + Util.numberToMoney(newItem.quantity) + " " + newItem.template.name);
        } else if (newItem.quantity == 1) {
            Service.getInstance().sendThongBao(pl, "Bạn nhận được " + newItem.template.name);
        } else {
            Service.getInstance().sendThongBao(pl, "Bạn nhận được x" + newItem.quantity + " " + newItem.template.name);
        }
        CombineServiceNew.gI().sendEffectOpenItem(pl, icon1, icon2);
        InventoryService.gI().subQuantityItemsBag(pl, item, 1);
        InventoryService.gI().addItemBag(pl, newItem, 99);
        InventoryService.gI().sendItemBags(pl);
    }

    private int randClothes(int level) {
        return ConstItem.LIST_ITEM_CLOTHES[Util.nextInt(0, 2)][Util.nextInt(0, 4)][level - 1];
    }

    private void openWoodChest(Player pl, Item item) {
        int time = (int) TimeUtil.diffDate(new Date(), new Date(item.createTime), TimeUtil.DAY);
        if (time != 0) {
            Item itemReward = null;
            int param = (int) item.itemOptions.get(0).param;
            int gold = 0;
            int[] listItem = {441, 442, 443, 444, 445, 446, 447, 457, 457, 457, 223, 224, 225};
            int[] listClothesReward;
            int[] listItemReward;
            String text = "Bạn nhận được\n";
            if (param < 8) {
                gold = 100000000 * param;
                listClothesReward = new int[]{randClothes(param)};
                listItemReward = Util.pickNRandInArr(listItem, 3);
            } else if (param < 10) {
                gold = 120000000 * param;
                listClothesReward = new int[]{randClothes(param), randClothes(param)};
                listItemReward = Util.pickNRandInArr(listItem, 4);
            } else {
                gold = 150000000 * param;
                listClothesReward = new int[]{randClothes(param), randClothes(param), randClothes(param)};
                listItemReward = Util.pickNRandInArr(listItem, 5);
                int ruby = Util.nextInt(1, 5);
                pl.inventory.ruby += ruby;
                pl.textRuongGo.add(text + "|1| " + ruby + " Hồng Ngọc");
            }
            for (var i : listClothesReward) {
                itemReward = ItemService.gI().createNewItem((short) i);
                RewardService.gI().initBaseOptionClothes(itemReward.template.id, itemReward.template.type, itemReward.itemOptions);
                RewardService.gI().initStarOption(itemReward, new RewardService.RatioStar[]{new RewardService.RatioStar((byte) 1, 1, 2), new RewardService.RatioStar((byte) 2, 1, 3), new RewardService.RatioStar((byte) 3, 1, 4), new RewardService.RatioStar((byte) 4, 1, 5),});
                InventoryService.gI().addItemBag(pl, itemReward, 0);
                pl.textRuongGo.add(text + itemReward.getInfoItem());
            }
            for (var i : listItemReward) {
                itemReward = ItemService.gI().createNewItem((short) i);
                RewardService.gI().initBaseOptionSaoPhaLe(itemReward);
                itemReward.quantity = Util.nextInt(1, 30000);
                InventoryService.gI().addItemBag(pl, itemReward, 0);
                pl.textRuongGo.add(text + itemReward.getInfoItem());
            }
            if (param == 11) {
                itemReward = ItemService.gI().createNewItem((short) 1404);
                itemReward.quantity = Util.nextInt(1, 100000);
                InventoryService.gI().addItemBag(pl, itemReward, 0);
                pl.textRuongGo.add(text + itemReward.getInfoItem());
            }
            NpcService.gI().createMenuConMeo(pl, ConstNpc.RUONG_GO, -1, "Bạn nhận được\n|1|+" + Util.numberToMoney(gold) + " vàng", "OK [" + pl.textRuongGo.size() + "]");
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            pl.inventory.addGold(gold);
            InventoryService.gI().sendItemBags(pl);
            PlayerService.gI().sendInfoHpMpMoney(pl);
        } else {
            Service.getInstance().sendThongBao(pl, "Vì bạn quên không lấy chìa nên cần đợi 24h để bẻ khóa");
        }
    }

    private void useItemChangeFlagBag(Player player, Item item) {
        switch (item.template.id) {
            case 805:// vong thien than
                break;
            case 865: //kiem Z
                if (!player.effectFlagBag.useKiemz) {
                    player.effectFlagBag.reset();
                    player.effectFlagBag.useKiemz = !player.effectFlagBag.useKiemz;
                } else {
                    player.effectFlagBag.reset();
                }
                break;
            case 994: //vỏ ốc
                break;
            case 995: //cây kem
                break;
            case 996: //cá heo
                break;
            case 997: //con diều
                break;
            case 998: //diều rồng
                if (!player.effectFlagBag.useDieuRong) {
                    player.effectFlagBag.reset();
                    player.effectFlagBag.useDieuRong = !player.effectFlagBag.useDieuRong;
                } else {
                    player.effectFlagBag.reset();
                }
                break;
            case 999: //mèo mun
                if (!player.effectFlagBag.useMeoMun) {
                    player.effectFlagBag.reset();
                    player.effectFlagBag.useMeoMun = !player.effectFlagBag.useMeoMun;
                } else {
                    player.effectFlagBag.reset();
                }
                break;
            case 1000: //xiên cá
                if (!player.effectFlagBag.useXienCa) {
                    player.effectFlagBag.reset();
                    player.effectFlagBag.useXienCa = !player.effectFlagBag.useXienCa;
                } else {
                    player.effectFlagBag.reset();
                }
                break;
            case 1001: //phóng heo
                if (!player.effectFlagBag.usePhongHeo) {
                    player.effectFlagBag.reset();
                    player.effectFlagBag.usePhongHeo = !player.effectFlagBag.usePhongHeo;
                } else {
                    player.effectFlagBag.reset();
                }
                break;
            case 954:
                if (!player.effectFlagBag.useHoaVang) {
                    player.effectFlagBag.reset();
                    player.effectFlagBag.useHoaVang = !player.effectFlagBag.useHoaVang;
                } else {
                    player.effectFlagBag.reset();
                }
                break;
            case 955:
                if (!player.effectFlagBag.useHoaHong) {
                    player.effectFlagBag.reset();
                    player.effectFlagBag.useHoaHong = !player.effectFlagBag.useHoaHong;
                } else {
                    player.effectFlagBag.reset();
                }
                break;
            case 852:
                if (!player.effectFlagBag.useGayTre) {
                    player.effectFlagBag.reset();
                    player.effectFlagBag.useGayTre = !player.effectFlagBag.useGayTre;
                } else {
                    player.effectFlagBag.reset();
                }
                break;
        }
        Service.getInstance().point(player);
        Service.getInstance().sendFlagBag(player);
    }

    private void changePet(Player player, Item item) {
        if (player.pet != null) {
            int gender = player.pet.gender + 1;
            if (gender > 2) {
                gender = 0;
            }
            PetService.gI().changeNormalPet(player, gender);
            InventoryService.gI().subQuantityItemsBag(player, item, 1);
        } else {
            Service.getInstance().sendThongBao(player, "Không thể thực hiện");
        }
    }

    public void capsulepink(Player pl, Item it) {
        if (Util.isTrue(50, 100)) {
            Item caitrang = ItemService.gI().createNewItem((short) 409, 1);
            caitrang.itemOptions.add(new ItemOption(50, Util.nextInt(10, 99)));
            caitrang.itemOptions.add(new ItemOption(77, Util.nextInt(10, 99)));
            caitrang.itemOptions.add(new ItemOption(103, Util.nextInt(10, 99)));
            caitrang.itemOptions.add(new ItemOption(14, Util.nextInt(10, 20)));
            caitrang.itemOptions.add(new ItemOption(117, Util.nextInt(1, 10)));
            caitrang.itemOptions.add(new ItemOption(93, Util.nextInt(1, 2)));
            InventoryService.gI().addItemBag(pl, caitrang, 99999);
            InventoryService.gI().sendItemBags(pl);
            Service.getInstance().sendThongBao(pl, "Bạn đã nhận được x1 " + caitrang);
        } else {
            Item caitrang = ItemService.gI().createNewItem((short) 954, 1);
            caitrang.itemOptions.add(new ItemOption(50, Util.nextInt(1, 99)));
            caitrang.itemOptions.add(new ItemOption(77, Util.nextInt(1, 99)));
            caitrang.itemOptions.add(new ItemOption(103, Util.nextInt(1, 99)));
            caitrang.itemOptions.add(new ItemOption(93, Util.nextInt(1, 2)));
            InventoryService.gI().addItemBag(pl, caitrang, 99999);
            InventoryService.gI().sendItemBags(pl);
            Service.getInstance().sendThongBao(pl, "Bạn đã nhận được x1 " + caitrang);
        }
        InventoryService.gI().subQuantityItemsBag(pl, it, 1);
    }

    public void hopQuaTanThu(Player pl, Item it) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 20) {
            int gender = pl.gender;
            int[] id = {gender, 6 + gender, 21 + gender, 27 + gender, 12, 194, 441, 442, 443, 444, 445, 446, 447};
            int[] soluong = {1, 1, 1, 1, 1, 1, 10, 10, 10, 10, 10, 10, 10};
            int[] option = {0, 0, 0, 0, 0, 73, 95, 96, 97, 98, 99, 100, 101};
            int[] param = {0, 0, 0, 0, 0, 0, 5, 5, 5, 3, 3, 5, 5};
            int arrLength = id.length - 1;

            for (int i = 0; i < arrLength; i++) {
                if (i < 5) {
                    Item item = ItemService.gI().createNewItem((short) id[i]);
                    RewardService.gI().initBaseOptionClothes(item.template.id, item.template.type, item.itemOptions);
                    item.itemOptions.add(new ItemOption(107, 6));
                    InventoryService.gI().addItemBag(pl, item, 0);
                } else {
                    Item item = ItemService.gI().createNewItem((short) id[i]);
                    item.quantity = soluong[i];
                    item.itemOptions.add(new ItemOption(option[i], param[i]));
                    InventoryService.gI().addItemBag(pl, item, 0);
                }
            }
            int[] idpet = {1713};
            Item item = ItemService.gI().createNewItem((short) idpet[Util.nextInt(0, idpet.length - 1)]);
            item.itemOptions.add(new ItemOption(51, Util.nextInt(5, 18)));
            item.itemOptions.add(new ItemOption(101, 99));
            item.itemOptions.add(new ItemOption(107, 6));
            item.itemOptions.add(new ItemOption(30, 0));
            InventoryService.gI().addItemBag(pl, item, 0);
            InventoryService.gI().subQuantityItemsBag(pl, it, 1);
            InventoryService.gI().sendItemBags(pl);
            Service.getInstance().sendThongBao(pl, "Chúc bạn chơi game vui vẻ");
        } else {
            Service.getInstance().sendThongBaoFromAdmin(pl, "Về Rương Nhà Lấy Ra 30 ô Hành Trang Để Mở");
        }
    }

    private void openbox2010(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            short[] temp = {17, 16, 15, 675, 676, 677, 678, 679, 680, 681, 580, 581, 582};
            int[][] gold = {{5000, 20000}};
            byte index = (byte) Util.nextInt(0, temp.length - 1);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;

            Item it = ItemService.gI().createNewItem(temp[index]);

            if (temp[index] >= 15 && temp[index] <= 17) {
                it.itemOptions.add(new ItemOption(73, 0));

            } else if (temp[index] >= 580 && temp[index] <= 582 || temp[index] >= 675 && temp[index] <= 681) { // cải trang

                it.itemOptions.add(new ItemOption(77, Util.nextInt(20, 30)));
                it.itemOptions.add(new ItemOption(103, Util.nextInt(20, 30)));
                it.itemOptions.add(new ItemOption(50, Util.nextInt(20, 30)));
                it.itemOptions.add(new ItemOption(95, Util.nextInt(5, 15)));
                it.itemOptions.add(new ItemOption(96, Util.nextInt(5, 15)));

                if (Util.isTrue(1, 200)) {
                    it.itemOptions.add(new ItemOption(74, 0));
                } else {
                    it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                }

            } else {
                it.itemOptions.add(new ItemOption(73, 0));
            }
            InventoryService.gI().addItemBag(pl, it, 0);
            icon[1] = it.template.iconID;

            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBags(pl);

            CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }

    private void capsule8thang3(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            short[] temp = {15, 17, 16, 675, 676, 677, 678, 679, 680, 681, 580, 581, 582, 1154, 1155, 1156, 860, 1041, 1042, 1043, 954, 955};
            byte index = (byte) Util.nextInt(0, temp.length - 1);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            Item it = ItemService.gI().createNewItem(temp[index]);
            if (Util.isTrue(15, 100)) {
                int ruby = Util.nextInt(100, 5000);
                pl.inventory.ruby += ruby;
                CombineServiceNew.gI().sendEffectOpenItem(pl, item.template.iconID, (short) 7743);
                PlayerService.gI().sendInfoHpMpMoney(pl);
                InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                InventoryService.gI().sendItemBags(pl);
                Service.getInstance().sendThongBao(pl, "Bạn nhận được " + ruby + " Hồng Ngọc");
                return;
            }
            if (Util.isTrue(17, 100)) {
                Item mewmew = ItemService.gI().createNewItem((short) 457);
                int thoivang = Util.nextInt(1, 10);
                InventoryService.gI().addItemBag(pl, mewmew, thoivang);
                InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                InventoryService.gI().sendItemBags(pl);
                Service.getInstance().sendThongBao(pl, "Bạn nhận được thỏi vàng");
                CombineServiceNew.gI().sendEffectOpenItem(pl, item.template.iconID, (short) 4028);
                return;
            }
            if (it.template.type == 5) { // cải trang
                it.itemOptions.add(new ItemOption(50, Util.nextInt(1, 100)));
                it.itemOptions.add(new ItemOption(77, Util.nextInt(1, 100)));
                it.itemOptions.add(new ItemOption(103, Util.nextInt(1, 100)));
                it.itemOptions.add(new ItemOption(117, Util.nextInt(6, 10)));
            } else if (it.template.id == 954 || it.template.id == 955) {
                it.itemOptions.add(new ItemOption(50, Util.nextInt(5, 100)));
                it.itemOptions.add(new ItemOption(77, Util.nextInt(5, 100)));
                it.itemOptions.add(new ItemOption(103, Util.nextInt(5, 100)));
            }
            if (it.template.type == 5 || it.template.id == 954 || it.template.id == 955) {
                if (Util.isTrue(1, 10)) {
                    it.itemOptions.add(new ItemOption(74, 0));
                } else {
                    it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 3)));
                }
            }
            InventoryService.gI().addItemBag(pl, it, 0);
            icon[1] = it.template.iconID;
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBags(pl);
            CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }

    private void QCMM(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            short[] temp = {1041, 457, 457, 457, 457, 457, 1419, 1419, 1418, 1418, 381, 382, 383, 384, 457, 457, 457, 1371, 1372, 1373, 1374};
            byte index = (byte) Util.nextInt(0, temp.length - 1);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            Item it = ItemService.gI().createNewItem(temp[index]);

            if (it.template.type == 5) { // cải trang
                it.itemOptions.add(new ItemOption(50, Util.nextInt(2, 100)));
                it.itemOptions.add(new ItemOption(77, Util.nextInt(2, 100)));
                it.itemOptions.add(new ItemOption(103, Util.nextInt(2, 100)));
                it.itemOptions.add(new ItemOption(94, Util.nextInt(2, 100)));
                it.itemOptions.add(new ItemOption(101, Util.nextInt(2, 100)));
                it.itemOptions.add(new ItemOption(72, Util.nextInt(1, 16)));
                it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
                Service.getInstance().sendThongBao(pl, pl.name + " Đã Quay  " + item.template.name + " Với Phong Độ Cực Ngầu Đã ra Vật Phẩm vĩnh viễn Random 100.000% SD HP KI ");

            }
            if (it.template.id == 1371) { // cải trang
                it.itemOptions.add(new ItemOption(50, Util.nextInt(2, 3)));
            }
            if (it.template.id == 1372) { // cải trang
                it.itemOptions.add(new ItemOption(77, Util.nextInt(2, 5)));
            }
            if (it.template.id == 1373) { // cải trang
                it.itemOptions.add(new ItemOption(103, Util.nextInt(2, 5)));
            }
            if (it.template.id == 1374) { // cải trang
                it.itemOptions.add(new ItemOption(94, Util.nextInt(2, 2)));

            }
            // Sửa tỉ lệ xuất hiện của lựa chọn 93 thành 90%
            if (Util.nextInt(1, 100) <= 99) {
                it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 2)));
            }
            InventoryService.gI().addItemBag(pl, it, 0);
            icon[1] = it.template.iconID;
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBags(pl);
            CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");

        }
    }

    private void QCMMGOKU(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            short[] temp = {1350, 457, 457, 457, 457, 457, 1419, 1419, 1418, 1418, 381, 382, 383, 384, 457, 457, 457, 1371, 1372, 1373, 1374, 1227, 2029, 674};
            byte index = (byte) Util.nextInt(0, temp.length - 1);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            Item it = ItemService.gI().createNewItem(temp[index]);

            if (it.template.type == 5) { // cải trang
                it.itemOptions.add(new ItemOption(50, Util.nextInt(2, 100)));
                it.itemOptions.add(new ItemOption(77, Util.nextInt(2, 100)));
                it.itemOptions.add(new ItemOption(103, Util.nextInt(2, 100)));
                it.itemOptions.add(new ItemOption(94, Util.nextInt(2, 100)));
                it.itemOptions.add(new ItemOption(101, Util.nextInt(2, 100)));
                it.itemOptions.add(new ItemOption(72, Util.nextInt(1, 16)));
                it.itemOptions.add(new ItemOption(107, Util.nextInt(1, 10)));
                Service.getInstance().sendThongBao(pl, pl.name + " Đã Quay  " + item.template.name + " Với Phong Độ Cực Ngầu Đã ra Vật Phẩm vĩnh viễn Random 100.000% SD HP KI ");

            }
            if (it.template.id == 1371) { // cải trang
                it.itemOptions.add(new ItemOption(50, Util.nextInt(0, 5)));
            }
            if (it.template.id == 1372) { // cải trang
                it.itemOptions.add(new ItemOption(77, Util.nextInt(0, 5)));
            }
            if (it.template.id == 1373) { // cải trang
                it.itemOptions.add(new ItemOption(103, Util.nextInt(0, 5)));
            }
            if (it.template.id == 1374) { // cải trang
                it.itemOptions.add(new ItemOption(94, Util.nextInt(0, 2)));
            }
            // Sửa tỉ lệ xuất hiện của lựa chọn 93 thành 90%
            if (Util.nextInt(1, 100) <= 99) {
                it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 2)));
            }
            InventoryService.gI().addItemBag(pl, it, 0);
            icon[1] = it.template.iconID;
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBags(pl);
            CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");

        }
    }

    private void QCMMVAGETA(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            short[] temp = {1997, 457, 457, 457, 457, 457, 1419, 1419, 1418, 1418, 381, 382, 383, 384, 457, 457, 457, 1371, 1372, 1373, 1374, 1227, 2029, 457, 14, 15, 16, 17, 18, 19, 20, 674};
            byte index = (byte) Util.nextInt(0, temp.length - 1);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            Item it = ItemService.gI().createNewItem(temp[index]);

            if (it.template.type == 5) { // cải trang
                it.itemOptions.add(new ItemOption(50, Util.nextInt(2, 100)));
                it.itemOptions.add(new ItemOption(77, Util.nextInt(2, 100)));
                it.itemOptions.add(new ItemOption(103, Util.nextInt(2, 100)));
                it.itemOptions.add(new ItemOption(94, Util.nextInt(2, 100)));
                it.itemOptions.add(new ItemOption(101, Util.nextInt(2, 50)));
                it.itemOptions.add(new ItemOption(72, Util.nextInt(1, 16)));
                it.itemOptions.add(new ItemOption(107, Util.nextInt(1, 10)));
                Service.getInstance().sendThongBao(pl, pl.name + " Đã Quay  " + item.template.name + " Với Phong Độ Cực Ngầu Đã ra Cải trang vegeta Untrall vĩnh viễn Random 1000.000% SD HP KI ");

            }
            if (it.template.id == 1371) { // cải trang
                it.itemOptions.add(new ItemOption(50, Util.nextInt(0, 5)));
            }
            if (it.template.id == 1372) { // cải trang
                it.itemOptions.add(new ItemOption(77, Util.nextInt(0, 5)));
            }
            if (it.template.id == 1373) { // cải trang
                it.itemOptions.add(new ItemOption(103, Util.nextInt(0, 5)));
            }
            if (it.template.id == 1374) { // cải trang
                it.itemOptions.add(new ItemOption(94, Util.nextInt(1, 2)));

            }
            // Sửa tỉ lệ xuất hiện của lựa chọn 93 thành 90%
            if (Util.nextInt(1, 100) <= 99) {
                it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 2)));
            }
            InventoryService.gI().addItemBag(pl, it, 0);
            icon[1] = it.template.iconID;
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBags(pl);
            CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");

        }
    }

    private void QCMMVAGETA1(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            short[] temp = {1992, 457, 457, 457, 457, 457, 1419, 1419, 1418, 1418, 381, 382, 383, 384, 457, 457, 457, 1371, 1372, 1373, 1374, 1227, 2029, 457, 14, 15, 16, 17, 18, 19, 20, 674};
            byte index = (byte) Util.nextInt(0, temp.length - 1);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            Item it = ItemService.gI().createNewItem(temp[index]);

            if (it.template.type == 5) { // cải trang
                it.itemOptions.add(new ItemOption(50, Util.nextInt(2, 100)));
                it.itemOptions.add(new ItemOption(77, Util.nextInt(2, 100)));
                it.itemOptions.add(new ItemOption(103, Util.nextInt(2, 100)));
                it.itemOptions.add(new ItemOption(94, Util.nextInt(2, 100)));
                it.itemOptions.add(new ItemOption(101, Util.nextInt(2, 100)));
                it.itemOptions.add(new ItemOption(72, Util.nextInt(1, 16)));
                it.itemOptions.add(new ItemOption(107, Util.nextInt(1, 10)));
                Service.getInstance().sendThongBao(pl, pl.name + pl.name + " Đã Quay  " + item.template.name + " Với Phong Độ Cực Ngầu Đã ra Cải trang vegeta Untrall vĩnh viễn Random 1000.000% SD HP KI ");

            }
            if (it.template.id == 1371) { // cải trang
                it.itemOptions.add(new ItemOption(50, Util.nextInt(1, 5)));
            }
            if (it.template.id == 1372) { // cải trang
                it.itemOptions.add(new ItemOption(77, Util.nextInt(1, 5)));
            }
            if (it.template.id == 1373) { // cải trang
                it.itemOptions.add(new ItemOption(103, Util.nextInt(1, 5)));
            }
            if (it.template.id == 1374) { // cải trang
                it.itemOptions.add(new ItemOption(94, Util.nextInt(1, 5)));
            }
            // Sửa tỉ lệ xuất hiện của lựa chọn 93 thành 90%
            if (Util.nextInt(1, 100) <= 99) {
                it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 2)));
            }
            InventoryService.gI().addItemBag(pl, it, 0);
            icon[1] = it.template.iconID;
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBags(pl);
            CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");

        }
    }

    private void QCMMVAGETA2(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            short[] temp = {1993, 457, 457, 457, 457, 457, 1419, 1419, 1418, 1418, 381, 382, 383, 384, 457, 457, 457, 1371, 1372, 1373, 1374, 1227, 2029, 457, 14, 15, 16, 17, 18, 19, 20, 674};
            byte index = (byte) Util.nextInt(0, temp.length - 1);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            Item it = ItemService.gI().createNewItem(temp[index]);

            if (it.template.type == 5) { // cải trang
                it.itemOptions.add(new ItemOption(50, Util.nextInt(2, 100)));
                it.itemOptions.add(new ItemOption(77, Util.nextInt(2, 100)));
                it.itemOptions.add(new ItemOption(103, Util.nextInt(2, 100)));
                it.itemOptions.add(new ItemOption(94, Util.nextInt(2, 100)));
                it.itemOptions.add(new ItemOption(101, Util.nextInt(2, 100)));
                it.itemOptions.add(new ItemOption(72, Util.nextInt(1, 16)));
                it.itemOptions.add(new ItemOption(107, Util.nextInt(1, 10)));
                Service.getInstance().sendThongBao(pl, pl.name + pl.name + " Đã Quay  " + item.template.name + " Với Phong Độ Cực Ngầu Đã ra Cải trang vegeta Untrall vĩnh viễn Random 1000.000% SD HP KI ");

            }
            if (it.template.id == 1371) { // cải trang
                it.itemOptions.add(new ItemOption(50, Util.nextInt(1, 5)));
            }
            if (it.template.id == 1372) { // cải trang
                it.itemOptions.add(new ItemOption(77, Util.nextInt(2, 5)));
            }
            if (it.template.id == 1373) { // cải trang
                it.itemOptions.add(new ItemOption(103, Util.nextInt(2, 5)));
            }
            if (it.template.id == 1374) { // cải trang
                it.itemOptions.add(new ItemOption(94, Util.nextInt(2, 5)));

            }
            // Sửa tỉ lệ xuất hiện của lựa chọn 93 thành 90%
            if (Util.nextInt(1, 100) <= 99) {
                it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 2)));
            }
            InventoryService.gI().addItemBag(pl, it, 0);
            icon[1] = it.template.iconID;
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBags(pl);
            CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");

        }
    }

    private void QCMMVAGETA3(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            short[] temp = {1994, 457, 457, 457, 457, 457, 1419, 1419, 1418, 1418, 381, 382, 383, 384, 457, 457, 457, 1371, 1372, 1373, 1374, 1227, 2029, 457, 14, 15, 16, 17, 18, 19, 20, 674};
            byte index = (byte) Util.nextInt(0, temp.length - 1);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            Item it = ItemService.gI().createNewItem(temp[index]);

            if (it.template.type == 5) { // cải trang
                it.itemOptions.add(new ItemOption(50, Util.nextInt(2, 100)));
                it.itemOptions.add(new ItemOption(77, Util.nextInt(2, 100)));
                it.itemOptions.add(new ItemOption(103, Util.nextInt(2, 100)));
                it.itemOptions.add(new ItemOption(94, Util.nextInt(2, 100)));
                it.itemOptions.add(new ItemOption(101, Util.nextInt(2, 50)));
                it.itemOptions.add(new ItemOption(72, Util.nextInt(1, 16)));
                it.itemOptions.add(new ItemOption(107, Util.nextInt(1, 10)));
                Service.getInstance().sendThongBao(pl, pl.name + pl.name + " Đã Quay  " + item.template.name + " Với Phong Độ Cực Ngầu Đã ra Cải trang vegeta Untrall vĩnh viễn Random 1000.000% SD HP KI ");

            }
            if (it.template.id == 1371) { // cải trang
                it.itemOptions.add(new ItemOption(50, Util.nextInt(2, 5)));
            }
            if (it.template.id == 1372) { // cải trang
                it.itemOptions.add(new ItemOption(77, Util.nextInt(2, 5)));
            }
            if (it.template.id == 1373) { // cải trang
                it.itemOptions.add(new ItemOption(103, Util.nextInt(2, 5)));
            }
            if (it.template.id == 1374) { // cải trang
                it.itemOptions.add(new ItemOption(94, Util.nextInt(2, 5)));

            }
            // Sửa tỉ lệ xuất hiện của lựa chọn 93 thành 90%
            if (Util.nextInt(1, 100) <= 99) {
                it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 2)));
            }
            InventoryService.gI().addItemBag(pl, it, 0);
            icon[1] = it.template.iconID;
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBags(pl);
            CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");

        }
    }

    private void QCMMVAGETA4(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            short[] temp = {1995, 457, 457, 457, 457, 457, 1419, 1419, 1418, 1418, 381, 382, 383, 384, 457, 457, 457, 1371, 1372, 1373, 1374, 1227, 2029, 457, 14, 15, 16, 17, 18, 19, 20, 674};
            byte index = (byte) Util.nextInt(0, temp.length - 1);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            Item it = ItemService.gI().createNewItem(temp[index]);

            if (it.template.type == 5) { // cải trang
                it.itemOptions.add(new ItemOption(50, Util.nextInt(2, 100)));
                it.itemOptions.add(new ItemOption(77, Util.nextInt(2, 100)));
                it.itemOptions.add(new ItemOption(103, Util.nextInt(2, 100)));
                it.itemOptions.add(new ItemOption(94, Util.nextInt(2, 100)));
                it.itemOptions.add(new ItemOption(101, Util.nextInt(2, 50)));
                it.itemOptions.add(new ItemOption(72, Util.nextInt(1, 16)));
                it.itemOptions.add(new ItemOption(107, Util.nextInt(1, 10)));
                Service.getInstance().sendThongBao(pl, pl.name + pl.name + " Đã Quay  " + item.template.name + " Với Phong Độ Cực Ngầu Đã ra Cải trang vegeta Untrall vĩnh viễn Random 1000.000% SD HP KI ");

            }
            if (it.template.id == 1371) { // cải trang
                it.itemOptions.add(new ItemOption(50, Util.nextInt(0, 5)));
            }
            if (it.template.id == 1372) { // cải trang
                it.itemOptions.add(new ItemOption(77, Util.nextInt(2, 5)));
            }
            if (it.template.id == 1373) { // cải trang
                it.itemOptions.add(new ItemOption(103, Util.nextInt(2, 5)));
            }
            if (it.template.id == 1374) { // cải trang
                it.itemOptions.add(new ItemOption(94, Util.nextInt(2, 2)));

            }
            // Sửa tỉ lệ xuất hiện của lựa chọn 93 thành 90%
            if (Util.nextInt(1, 100) <= 99) {
                it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 2)));
            }
            InventoryService.gI().addItemBag(pl, it, 0);
            icon[1] = it.template.iconID;
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBags(pl);
            CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");

        }
    }

    private void QCMMVAGETA5(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            short[] temp = {1996, 457, 457, 457, 457, 457, 1419, 1419, 1418, 1418, 381, 382, 383, 384, 457, 457, 457, 1371, 1372, 1373, 1374, 1227, 2029, 457, 14, 15, 16, 17, 18, 19, 20, 674};
            byte index = (byte) Util.nextInt(0, temp.length - 1);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            Item it = ItemService.gI().createNewItem(temp[index]);

            if (it.template.type == 5) { // cải trang
                it.itemOptions.add(new ItemOption(50, Util.nextInt(2, 100)));
                it.itemOptions.add(new ItemOption(77, Util.nextInt(2, 100)));
                it.itemOptions.add(new ItemOption(103, Util.nextInt(2, 100)));
                it.itemOptions.add(new ItemOption(94, Util.nextInt(2, 100)));
                it.itemOptions.add(new ItemOption(101, Util.nextInt(2, 50)));
                it.itemOptions.add(new ItemOption(72, Util.nextInt(1, 16)));
                it.itemOptions.add(new ItemOption(107, Util.nextInt(1, 10)));
                Service.getInstance().sendThongBao(pl, pl.name + " Đã Quay  " + item.template.name + " Với Phong Độ Cực Ngầu Đã ra Cải trang vegeta Untrall vĩnh viễn Random 1000.000% SD HP KI ");

            }
            if (it.template.id == 1371) { // cải trang
                it.itemOptions.add(new ItemOption(50, Util.nextInt(2, 5)));
            }
            if (it.template.id == 1372) { // cải trang
                it.itemOptions.add(new ItemOption(77, Util.nextInt(2, 5)));
            }
            if (it.template.id == 1373) { // cải trang
                it.itemOptions.add(new ItemOption(103, Util.nextInt(2, 5)));
            }
            if (it.template.id == 1374) { // cải trang
                it.itemOptions.add(new ItemOption(94, Util.nextInt(2, 5)));

            }
            // Sửa tỉ lệ xuất hiện của lựa chọn 93 thành 90%
            if (Util.nextInt(1, 100) <= 99) {
                it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 2)));
            }
            InventoryService.gI().addItemBag(pl, it, 0);
            icon[1] = it.template.iconID;
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBags(pl);
            CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");

        }
    }

    private void QCMMVAGETA6(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            short[] temp = {1918, 1919, 1920, 1923, 1924, 1925, 1926, 1927, 1928, 1371, 1372, 1373, 1374};
            byte index = (byte) Util.nextInt(0, temp.length - 1);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            Item it = ItemService.gI().createNewItem(temp[index]);

            if (it.template.type == 5) { // cải trang
                it.itemOptions.add(new ItemOption(50, Util.nextInt(2, 1000)));
                it.itemOptions.add(new ItemOption(77, Util.nextInt(2, 1000)));
                it.itemOptions.add(new ItemOption(103, Util.nextInt(2, 1000)));
                it.itemOptions.add(new ItemOption(94, Util.nextInt(2, 1000)));
                it.itemOptions.add(new ItemOption(101, Util.nextInt(2, 1000)));
                it.itemOptions.add(new ItemOption(72, Util.nextInt(1, 16)));
                it.itemOptions.add(new ItemOption(93, Util.nextInt(0, 1)));
                Service.getInstance().sendThongBao(pl, pl.name + " Đã Quay  " + item.template.name + " Với Phong Độ Cực Ngầu Đã ra Random Cải trang  1000.000% SD HP KI ");

            }
            if (it.template.id == 1371) { // cải trang
                it.itemOptions.add(new ItemOption(50, Util.nextInt(2, 5)));
            }
            if (it.template.id == 1372) { // cải trang
                it.itemOptions.add(new ItemOption(77, Util.nextInt(2, 5)));
            }
            if (it.template.id == 1373) { // cải trang
                it.itemOptions.add(new ItemOption(103, Util.nextInt(2, 5)));
            }
            if (it.template.id == 1374) { // cải trang
                it.itemOptions.add(new ItemOption(94, Util.nextInt(2, 5)));

            }
            // Sửa tỉ lệ xuất hiện của lựa chọn 93 thành 90%
            if (Util.nextInt(1, 100) <= 99) {
                it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 2)));
            }
            InventoryService.gI().addItemBag(pl, it, 0);
            icon[1] = it.template.iconID;
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBags(pl);
            CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");

        }
    }

    private void hoathan(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            short[] temp = {1550, 1010, 1011, 1087, 1088, 1089, 1090, 1091};
            byte index = (byte) Util.nextInt(0, temp.length - 1);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            Item it = ItemService.gI().createNewItem(temp[index]);

            if (it.template.type == 5) { // cải trang
                it.itemOptions.add(new ItemOption(50, Util.nextInt(2, 100)));
                it.itemOptions.add(new ItemOption(77, Util.nextInt(2, 100)));
                it.itemOptions.add(new ItemOption(103, Util.nextInt(2, 100)));
                it.itemOptions.add(new ItemOption(94, Util.nextInt(2, 100)));
                it.itemOptions.add(new ItemOption(101, Util.nextInt(2, 100)));
                it.itemOptions.add(new ItemOption(72, Util.nextInt(1, 16)));
                it.itemOptions.add(new ItemOption(34, Util.nextInt(0, 1)));
                Service.getInstance().sendThongBao(pl, pl.name + " Đã Quay  " + item.template.name + " Với Phong Độ Cực Ngầu Đã ra Random Cải trang  1000.000% SD HP KI ");

            }
            if (it.template.id == 1371) { // cải trang
                it.itemOptions.add(new ItemOption(50, Util.nextInt(2, 5)));
            }
            if (it.template.id == 1372) { // cải trang
                it.itemOptions.add(new ItemOption(77, Util.nextInt(2, 5)));
            }
            if (it.template.id == 1373) { // cải trang
                it.itemOptions.add(new ItemOption(103, Util.nextInt(2, 5)));
            }
            if (it.template.id == 1374) { // cải trang
                it.itemOptions.add(new ItemOption(94, Util.nextInt(2, 5)));

            }
            // Sửa tỉ lệ xuất hiện của lựa chọn 93 thành 90%
            if (Util.nextInt(1, 100) <= 99) {
                it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 2)));
            }
            InventoryService.gI().addItemBag(pl, it, 0);
            icon[1] = it.template.iconID;
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBags(pl);
            CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");

        }
    }

    private void ruonghaitac(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            short[] temp = {618, 619, 620, 621, 622, 623, 624, 625, 626, 1575, 457, 457, 457, 1555, 1556, 1557, 1558, 1559, 1560, 1561, 1562, 1563, 1564, 1565, 1566, 1567, 1568, 1569, 1570, 1571, 1572, 1573};
            byte index = (byte) Util.nextInt(0, temp.length - 1);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            Item it = ItemService.gI().createNewItem(temp[index]);

            if (it.template.type == 5) { // cải trang
                it.itemOptions.add(new ItemOption(50, Util.nextInt(2, 200)));
                it.itemOptions.add(new ItemOption(77, Util.nextInt(2, 200)));
                it.itemOptions.add(new ItemOption(103, Util.nextInt(2, 200)));
                it.itemOptions.add(new ItemOption(94, Util.nextInt(2, 200)));
                it.itemOptions.add(new ItemOption(101, Util.nextInt(2, 50)));
                it.itemOptions.add(new ItemOption(35, Util.nextInt(1, 16)));
                it.itemOptions.add(new ItemOption(107, Util.nextInt(1, 10)));
                Service.getInstance().sendThongBao(pl, pl.name + " Đã Mở  " + item.template.name + " Với Phong Độ Cực Ngầu  ");

            }

            if (it.template.id == 457) { // cải trang
                it.itemOptions.add(new ItemOption(30, Util.nextInt(2, 5)));

            }
            // Sửa tỉ lệ xuất hiện của lựa chọn 93 thành 90%
            if (Util.isTrue(99, 100)) {
                it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 2)));
            }
            InventoryService.gI().addItemBag(pl, it, 0);
            icon[1] = it.template.iconID;
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBags(pl);
            CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");

        }
    }

    private void ruonghaitacss(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            short[] temp = {618, 619, 620, 621, 622, 623, 624, 625, 626, 1575, 457, 1576, 1511, 1512, 1503, 1504, 1490, 1491, 1486, 1510};
            byte index = (byte) Util.nextInt(0, temp.length - 1);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            Item it = ItemService.gI().createNewItem(temp[index]);

            if (it.template.type == 5) { // cải trang
                it.itemOptions.add(new ItemOption(50, Util.nextInt(2, 200)));
                it.itemOptions.add(new ItemOption(77, Util.nextInt(2, 200)));
                it.itemOptions.add(new ItemOption(103, Util.nextInt(2, 200)));
                it.itemOptions.add(new ItemOption(94, Util.nextInt(2, 200)));
                it.itemOptions.add(new ItemOption(101, Util.nextInt(2, 200)));
                it.itemOptions.add(new ItemOption(34, Util.nextInt(1, 16)));
                it.itemOptions.add(new ItemOption(107, Util.nextInt(1, 10)));
                Service.getInstance().sendThongBao(pl, pl.name + " Đã Mở  " + item.template.name + " Với Phong Độ Cực Ngầu  ");

            }

            if (it.template.id == 457) { // cải trang
                it.itemOptions.add(new ItemOption(30, Util.nextInt(2, 5)));

            }
            // Sửa tỉ lệ xuất hiện của lựa chọn 93 thành 90%
            if (Util.isTrue(99, 100)) {
                it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 15)));
            }
            InventoryService.gI().addItemBag(pl, it, 0);
            icon[1] = it.template.iconID;
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBags(pl);
            CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");

        }
    }

    private void ruonghaitacsss(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            short[] temp = {618, 619, 620, 621, 622, 623, 624, 625, 626, 1510, 1493, 1513, 1514, 1532, 1532, 1533, 1576, 1944, 1945, 1952};
            byte index = (byte) Util.nextInt(0, temp.length - 1);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            Item it = ItemService.gI().createNewItem(temp[index]);

            if (it.template.type == 5) { // cải trang
                it.itemOptions.add(new ItemOption(50, Util.nextInt(2, 300)));
                it.itemOptions.add(new ItemOption(77, Util.nextInt(2, 300)));
                it.itemOptions.add(new ItemOption(103, Util.nextInt(2, 300)));
                it.itemOptions.add(new ItemOption(94, Util.nextInt(2, 300)));
                it.itemOptions.add(new ItemOption(101, Util.nextInt(2, 300)));
                it.itemOptions.add(new ItemOption(72, Util.nextInt(1, 16)));
                it.itemOptions.add(new ItemOption(107, Util.nextInt(1, 10)));
                Service.getInstance().sendThongBao(pl, pl.name + " Đã Mở  " + item.template.name + " Với Phong Độ Cực Ngầu  ");

            }

            // Sửa tỉ lệ xuất hiện của lựa chọn 93 thành 90%
            if (Util.isTrue(100, 100)) {
                it.itemOptions.add(new ItemOption(30, Util.nextInt(1, 15)));
            }
            InventoryService.gI().addItemBag(pl, it, 0);
            icon[1] = it.template.iconID;
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBags(pl);
            CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");

        }
    }

    private void dathanlinh(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            short[] temp = {1371, 1372, 1373, 1374};
            byte index = (byte) Util.nextInt(0, temp.length - 1);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            Item it = ItemService.gI().createNewItem(temp[index]);

            if (it.template.type == 34) { // cải trang

                it.itemOptions.add(new ItemOption(93, Util.nextInt(0, 1)));
                Service.getInstance().sendThongBao(pl, pl.name + pl.name + " Đã mở  " + item.template.name + " Random Đá Tinh Luyện");

            }
            if (it.template.id == 1371) { // cải trang
                it.itemOptions.add(new ItemOption(50, Util.nextInt(1, 6)));
            }
            if (it.template.id == 1372) { // cải trang
                it.itemOptions.add(new ItemOption(77, Util.nextInt(1, 6)));
            }
            if (it.template.id == 1373) { // cải trang
                it.itemOptions.add(new ItemOption(103, Util.nextInt(1, 6)));
            }
            if (it.template.id == 1374) { // cải trang
                it.itemOptions.add(new ItemOption(94, Util.nextInt(1, 6)));

            }
            // Sửa tỉ lệ xuất hiện của lựa chọn 93 thành 90%
            if (Util.nextInt(1, 100) <= 99) {
                it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 2)));
            }
            InventoryService.gI().addItemBag(pl, it, 0);
            icon[1] = it.template.iconID;
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBags(pl);
            CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");

        }
    }

    private void setkichhoat(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            short[] temp = {230, 231, 232, 233, 234, 235, 236, 237, 238, 239, 240, 241, 242, 243, 244, 245, 246, 247, 248, 249, 250, 251, 252, 253, 254, 255, 256, 257, 258, 259, 260, 261, 262, 263, 264, 265, 266, 267, 268, 269, 270, 271, 272, 273, 274, 275, 276, 277, 278, 279, 280, 281};
            byte index = (byte) Util.nextInt(0, temp.length - 1);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            Item it = ItemService.gI().createNewItem(temp[index]);

            // Thêm chỉ số dựa trên loại item
            switch (it.template.type) {
                case 0: // Áo
                    it.itemOptions.add(new ItemOption(47, Util.nextInt(500, 1000))); // Chỉ số ngẫu nhiên

                    break;
                case 1: // Quần
                    it.itemOptions.add(new ItemOption(6, Util.nextInt(500, 1500)));

                    break;

                case 2: // Găng
                    it.itemOptions.add(new ItemOption(0, Util.nextInt(1000, 5000)));

                    break;
                case 3: // Giày
                    it.itemOptions.add(new ItemOption(7, Util.nextInt(200, 1000)));

                    break;
                case 4: // Rada
                    it.itemOptions.add(new ItemOption(14, Util.nextInt(1, 10)));

                    break;
            }
            if (Util.isTrue(100, 100)) {
                if (it.template.gender == 0) {
                    it.itemOptions.add(new ItemOption(Util.nextInt(127, 129), 1)); // Chỉ số đặc biệt
                    it.itemOptions.add(new ItemOption(30, 1)); // Chỉ số đặc biệt 
                }
                if (it.template.gender == 1) {
                    it.itemOptions.add(new ItemOption(Util.nextInt(130, 132), 1)); // Chỉ số đặc biệt
                    it.itemOptions.add(new ItemOption(30, 1)); // Chỉ số đặc biệt 
                }
                if (it.template.gender == 2) {
                    it.itemOptions.add(new ItemOption(Util.nextInt(133, 135), 1)); // Chỉ số đặc biệt
                    it.itemOptions.add(new ItemOption(30, 1)); // Chỉ số đặc biệt 

                }
                if (it.template.gender == 3) {
                    it.itemOptions.add(new ItemOption(Util.nextInt(127, 135), 1)); // Chỉ số đặc biệt
                    it.itemOptions.add(new ItemOption(30, 1)); // Chỉ số đặc biệt 

                }

            }
            // Thêm vào túi đồ
            InventoryService.gI().addItemBag(pl, it, 0);
            icon[1] = it.template.iconID;
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBags(pl);
            CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }

    private void openhopvatpham(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            short[] temp = {994, 995, 996, 997, 998, 999, 1000, 1001, 1007, 1021, 1022, 1023, 1023};
            byte index = (byte) Util.nextInt(0, temp.length - 1);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            Item it = ItemService.gI().createNewItem(temp[index]);
            if (it.template.type == 11) { // cải trang
                it.itemOptions.add(new ItemOption(50, Util.nextInt(2, 50)));
                it.itemOptions.add(new ItemOption(77, Util.nextInt(2, 50)));
                it.itemOptions.add(new ItemOption(103, Util.nextInt(2, 50)));
            }
            // Sửa tỉ lệ xuất hiện của lựa chọn 93 thành 90%
            if (Util.nextInt(1, 100) <= 90) {
                it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 3)));
            }
            InventoryService.gI().addItemBag(pl, it, 0);
            icon[1] = it.template.iconID;
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBags(pl);
            CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }

    private void openhopvatphamvip(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            short[] temp = {1329, 1330, 1331};
            byte index = (byte) Util.nextInt(0, temp.length - 1);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            Item it = ItemService.gI().createNewItem(temp[index]);
            if (it.template.type == 11) { // cải trang
                it.itemOptions.add(new ItemOption(50, Util.nextInt(10, 50)));
                it.itemOptions.add(new ItemOption(77, Util.nextInt(10, 50)));
                it.itemOptions.add(new ItemOption(103, Util.nextInt(10, 50)));
            }
            // Sửa tỉ lệ xuất hiện của lựa chọn 93 thành 90%
            if (Util.nextInt(1, 100) <= 85) {
                it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 5)));
            }
            InventoryService.gI().addItemBag(pl, it, 0);
            icon[1] = it.template.iconID;
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBags(pl);
            CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }

    private void openhopvanbay(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            short[] temp = {920, 733, 1144, 1252, 1253, 734, 746};
            byte index = (byte) Util.nextInt(0, temp.length - 1);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            Item it = ItemService.gI().createNewItem(temp[index]);
            if (it.template.type == 23) { // cải trang
                it.itemOptions.add(new ItemOption(50, Util.nextInt(2, 50)));
                it.itemOptions.add(new ItemOption(77, Util.nextInt(2, 50)));
                it.itemOptions.add(new ItemOption(103, Util.nextInt(2, 50)));
            }
            // Sửa tỉ lệ xuất hiện của lựa chọn 93 thành 90%
            if (Util.nextInt(1, 100) <= 90) {
                it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 5)));
            }
            InventoryService.gI().addItemBag(pl, it, 0);
            icon[1] = it.template.iconID;
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBags(pl);
            CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }

    private void openhoppet(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            short[] temp = {1039, 1040, 1294, 1295};
            byte index = (byte) Util.nextInt(0, temp.length - 1);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            Item it = ItemService.gI().createNewItem(temp[index]);
            if (it.template.type == 21) { // cải trang
                it.itemOptions.add(new ItemOption(50, Util.nextInt(2, 50)));
                it.itemOptions.add(new ItemOption(77, Util.nextInt(2, 50)));
                it.itemOptions.add(new ItemOption(103, Util.nextInt(2, 50)));
            }
            // Sửa tỉ lệ xuất hiện của lựa chọn 93 thành 90%
            if (Util.nextInt(1, 100) <= 90) {
                it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 5)));
            }
            InventoryService.gI().addItemBag(pl, it, 0);
            icon[1] = it.template.iconID;
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBags(pl);
            CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }

    private void hopqua20thang11(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            short[] temp = {2037, 2038, 2039, 2040, 2041, 2042, 2043, 2044, 2045, 2046, 2047, 2048, 2049, 2050, 2000, 2062, 2063};
            byte index = (byte) Util.nextInt(0, temp.length - 1);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            Item it = ItemService.gI().createNewItem(temp[index]);
            if (it.template.type == 75) { // cải trang
                it.itemOptions.add(new ItemOption(174, Util.nextInt(1, 2025)));

            }
            // Sửa tỉ lệ xuất hiện của lựa chọn 93 thành 90%
            if (Util.nextInt(1, 100) <= 90) {
                it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 60)));
            }
            InventoryService.gI().addItemBag(pl, it, 0);
            icon[1] = it.template.iconID;
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBags(pl);
            CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }

    private void hopquagiangsinh(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            short[] temp = {457, 861, 77, 190, 381, 382, 383, 384, 825, 826, 827, 922, 923, 924, 937, 533, 533, 533};
            byte index = (byte) Util.nextInt(0, temp.length - 1);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            Item it = ItemService.gI().createNewItem(temp[index]);
            if (it.template.type == 27) { // cải trang
                it.itemOptions.add(new ItemOption(174, Util.nextInt(1, 2025)));
            }
            if (it.template.type == 5) { // cải trang
                it.itemOptions.add(new ItemOption(50, Util.nextInt(1, 100)));
                it.itemOptions.add(new ItemOption(77, Util.nextInt(1, 100)));
                it.itemOptions.add(new ItemOption(103, Util.nextInt(1, 100)));
                it.itemOptions.add(new ItemOption(95, Util.nextInt(1, 10)));
                it.itemOptions.add(new ItemOption(96, Util.nextInt(1, 10)));
                it.itemOptions.add(new ItemOption(106, Util.nextInt(1, 2024)));
                it.itemOptions.add(new ItemOption(167, Util.nextInt(1, 2024)));
                it.itemOptions.add(new ItemOption(174, Util.nextInt(1, 2025)));

            }
            // Sửa tỉ lệ xuất hiện của lựa chọn 93 thành 90%
            if (Util.nextInt(1, 100) <= 90) {
                it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
            }
            InventoryService.gI().addItemBag(pl, it, 999999999);
            icon[1] = it.template.iconID;
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBags(pl);
            CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }

    private void openhoplinhthu(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            short[] temp = {2019, 2020, 2021, 2023, 2025, 2026};
            byte index = (byte) Util.nextInt(0, temp.length - 1);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            Item it = ItemService.gI().createNewItem(temp[index]);
            if (it.template.type == 72) { // cải trang
                it.itemOptions.add(new ItemOption(50, Util.nextInt(2, 50)));
                it.itemOptions.add(new ItemOption(77, Util.nextInt(2, 50)));
                it.itemOptions.add(new ItemOption(103, Util.nextInt(2, 50)));
            }
            // Sửa tỉ lệ xuất hiện của lựa chọn 93 thành 90%
            if (Util.nextInt(1, 100) <= 90) {
                it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 5)));
            }
            InventoryService.gI().addItemBag(pl, it, 0);
            icon[1] = it.template.iconID;
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBags(pl);
            CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }

    public void openboxsukien(Player pl, Item item, int idsukien) {
        try {
            switch (idsukien) {
                case 1:
                    if (Manager.EVENT_SEVER == idsukien) {
                        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
                            short[] temp = {16, 15, 865, 999, 1000, 1001, 739, 742, 743};
                            int[][] gold = {{5000, 20000}};
                            byte index = (byte) Util.nextInt(0, temp.length - 1);
                            short[] icon = new short[2];
                            icon[0] = item.template.iconID;
                            Item it = ItemService.gI().createNewItem(temp[index]);
                            if (temp[index] >= 15 && temp[index] <= 16) {
                                it.itemOptions.add(new ItemOption(73, 0));

                            } else if (temp[index] == 865) {

                                it.itemOptions.add(new ItemOption(30, 0));

                                if (Util.isTrue(1, 30)) {
                                    it.itemOptions.add(new ItemOption(93, 365));
                                } else {
                                    it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                                }
                            } else if (temp[index] == 999) { // mèo mun
                                it.itemOptions.add(new ItemOption(77, 15));
                                it.itemOptions.add(new ItemOption(30, 0));
                                if (Util.isTrue(1, 50)) {
                                    it.itemOptions.add(new ItemOption(74, 0));
                                } else {
                                    it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                                }
                            } else if (temp[index] == 1000) { // xiên cá
                                it.itemOptions.add(new ItemOption(103, 15));
                                it.itemOptions.add(new ItemOption(30, 0));
                                if (Util.isTrue(1, 50)) {
                                    it.itemOptions.add(new ItemOption(74, 0));
                                } else {
                                    it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                                }
                            } else if (temp[index] == 1001) { // Phóng heo
                                it.itemOptions.add(new ItemOption(50, 15));
                                it.itemOptions.add(new ItemOption(30, 0));
                                if (Util.isTrue(1, 50)) {
                                    it.itemOptions.add(new ItemOption(74, 0));
                                } else {
                                    it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                                }

                            } else if (temp[index] == 739) { // cải trang Billes

                                it.itemOptions.add(new ItemOption(77, Util.nextInt(30, 40)));
                                it.itemOptions.add(new ItemOption(103, Util.nextInt(30, 40)));
                                it.itemOptions.add(new ItemOption(50, Util.nextInt(30, 45)));

                                if (Util.isTrue(1, 100)) {
                                    it.itemOptions.add(new ItemOption(74, 0));
                                } else {
                                    it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                                }

                            } else if (temp[index] == 742) { // cải trang Caufila

                                it.itemOptions.add(new ItemOption(77, Util.nextInt(30, 40)));
                                it.itemOptions.add(new ItemOption(103, Util.nextInt(30, 40)));
                                it.itemOptions.add(new ItemOption(50, Util.nextInt(30, 45)));

                                if (Util.isTrue(1, 100)) {
                                    it.itemOptions.add(new ItemOption(74, 0));
                                } else {
                                    it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                                }
                            } else if (temp[index] == 743) { // chổi bay
                                it.itemOptions.add(new ItemOption(30, 0));
                                if (Util.isTrue(1, 50)) {
                                    it.itemOptions.add(new ItemOption(74, 0));
                                } else {
                                    it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                                }

                            } else {
                                it.itemOptions.add(new ItemOption(73, 0));
                            }
                            InventoryService.gI().addItemBag(pl, it, 0);
                            icon[1] = it.template.iconID;

                            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                            InventoryService.gI().sendItemBags(pl);

                            CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
                        } else {
                            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");
                        }
                        break;
                    } else {
                        Service.getInstance().sendThongBao(pl, "Sự kiện đã kết thúc");
                    }
                case ConstEvent.SU_KIEN_20_11:
                    if (Manager.EVENT_SEVER == idsukien) {
                        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
                            short[] temp = {16, 15, 1039, 954, 955, 710, 711, 1040, 2023, 999, 1000, 1001};
                            byte index = (byte) Util.nextInt(0, temp.length - 1);
                            short[] icon = new short[2];
                            icon[0] = item.template.iconID;
                            Item it = ItemService.gI().createNewItem(temp[index]);
                            if (temp[index] >= 15 && temp[index] <= 16) {
                                it.itemOptions.add(new ItemOption(73, 0));
                            } else if (temp[index] == 1039) {
                                it.itemOptions.add(new ItemOption(50, 10));
                                it.itemOptions.add(new ItemOption(77, 10));
                                it.itemOptions.add(new ItemOption(103, 10));
                                it.itemOptions.add(new ItemOption(30, 0));
                                it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                            } else if (temp[index] == 954) {
                                it.itemOptions.add(new ItemOption(50, 15));
                                it.itemOptions.add(new ItemOption(77, 15));
                                it.itemOptions.add(new ItemOption(103, 15));
                                it.itemOptions.add(new ItemOption(30, 0));
                                if (Util.isTrue(79, 80)) {
                                    it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                                }
                            } else if (temp[index] == 955) {
                                it.itemOptions.add(new ItemOption(50, 20));
                                it.itemOptions.add(new ItemOption(77, 20));
                                it.itemOptions.add(new ItemOption(103, 20));
                                it.itemOptions.add(new ItemOption(30, 0));
                                if (Util.isTrue(79, 80)) {
                                    it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                                }
                            } else if (temp[index] == 710) {//cải trang quy lão kame
                                it.itemOptions.add(new ItemOption(50, 22));
                                it.itemOptions.add(new ItemOption(77, 20));
                                it.itemOptions.add(new ItemOption(103, 20));
                                it.itemOptions.add(new ItemOption(194, 0));
                                it.itemOptions.add(new ItemOption(160, 35));
                                if (Util.isTrue(99, 100)) {
                                    it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                                }
                            } else if (temp[index] == 711) { // cải trang jacky chun
                                it.itemOptions.add(new ItemOption(50, 23));
                                it.itemOptions.add(new ItemOption(77, 21));
                                it.itemOptions.add(new ItemOption(103, 21));
                                it.itemOptions.add(new ItemOption(195, 0));
                                it.itemOptions.add(new ItemOption(160, 50));
                                if (Util.isTrue(99, 100)) {
                                    it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                                }
                            } else if (temp[index] == 1040) {
                                it.itemOptions.add(new ItemOption(50, 10));
                                it.itemOptions.add(new ItemOption(77, 10));
                                it.itemOptions.add(new ItemOption(103, 10));
                                it.itemOptions.add(new ItemOption(30, 0));
                                it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                            } else if (temp[index] == 2023) {
                                it.itemOptions.add(new ItemOption(30, 0));
                            } else if (temp[index] == 999) { // mèo mun
                                it.itemOptions.add(new ItemOption(77, 15));
                                it.itemOptions.add(new ItemOption(30, 0));
                                if (Util.isTrue(1, 50)) {
                                    it.itemOptions.add(new ItemOption(74, 0));
                                } else {
                                    it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                                }
                            } else if (temp[index] == 1000) { // xiên cá
                                it.itemOptions.add(new ItemOption(103, 15));
                                it.itemOptions.add(new ItemOption(30, 0));
                                if (Util.isTrue(1, 50)) {
                                    it.itemOptions.add(new ItemOption(74, 0));
                                } else {
                                    it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                                }
                            } else if (temp[index] == 1001) { // Phóng heo
                                it.itemOptions.add(new ItemOption(50, 15));
                                it.itemOptions.add(new ItemOption(30, 0));
                                if (Util.isTrue(1, 50)) {
                                    it.itemOptions.add(new ItemOption(74, 0));
                                } else {
                                    it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                                }
                            } else {
                                it.itemOptions.add(new ItemOption(73, 0));
                            }
                            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                            icon[1] = it.template.iconID;
                            CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
                            InventoryService.gI().addItemBag(pl, it, 0);
                            int ruby = Util.nextInt(1, 5);
                            pl.inventory.ruby += ruby;
                            InventoryService.gI().sendItemBags(pl);
                            PlayerService.gI().sendInfoHpMpMoney(pl);
                            Service.getInstance().sendThongBao(pl, "Bạn được tặng kèm " + ruby + " Hồng Ngọc");
                        } else {
                            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");
                        }
                    } else {
                        Service.getInstance().sendThongBao(pl, "Sự kiện đã kết thúc");
                    }
                    break;
                case ConstEvent.SU_KIEN_NOEL:
                    if (Manager.EVENT_SEVER == idsukien) {
                        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
                            int spl = Util.nextInt(441, 445);
                            int dnc = Util.nextInt(220, 224);
                            int nr = Util.nextInt(16, 18);
                            int nrBang = Util.nextInt(926, 931);

                            if (Util.isTrue(5, 90)) {
                                int ruby = Util.nextInt(1, 3);
                                pl.inventory.ruby += ruby;
                                CombineServiceNew.gI().sendEffectOpenItem(pl, item.template.iconID, (short) 7743);
                                PlayerService.gI().sendInfoHpMpMoney(pl);
                                InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                                InventoryService.gI().sendItemBags(pl);
                                Service.getInstance().sendThongBao(pl, "Bạn nhận được " + ruby + " Hồng Ngọc");
                            } else {
                                int[] temp = {spl, dnc, nr, nrBang, 387, 390, 393, 821, 822, 746, 380, 999, 1000, 1001, 936};
                                byte index = (byte) Util.nextInt(0, temp.length - 1);
                                short[] icon = new short[2];
                                icon[0] = item.template.iconID;
                                Item it = ItemService.gI().createNewItem((short) temp[index]);

                                if (temp[index] >= 441 && temp[index] <= 443) {// sao pha le
                                    it.itemOptions.add(new ItemOption(temp[index] - 346, 5));
                                    it.quantity = 10;
                                } else if (temp[index] >= 444 && temp[index] <= 445) {
                                    it.itemOptions.add(new ItemOption(temp[index] - 346, 3));
                                    it.quantity = 10;
                                } else if (temp[index] >= 220 && temp[index] <= 224) { // da nang cap
                                    it.quantity = 10;
                                } else if (temp[index] >= 387 && temp[index] <= 393) { // mu noel do
                                    it.itemOptions.add(new ItemOption(50, Util.nextInt(30, 40)));
                                    it.itemOptions.add(new ItemOption(77, Util.nextInt(30, 40)));
                                    it.itemOptions.add(new ItemOption(103, Util.nextInt(30, 40)));
                                    it.itemOptions.add(new ItemOption(80, Util.nextInt(10, 20)));
                                    it.itemOptions.add(new ItemOption(106, 0));
                                    it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 3)));
                                    it.itemOptions.add(new ItemOption(199, 0));
                                } else if (temp[index] == 936) { // tuan loc
                                    it.itemOptions.add(new ItemOption(50, Util.nextInt(5, 10)));
                                    it.itemOptions.add(new ItemOption(77, Util.nextInt(5, 10)));
                                    it.itemOptions.add(new ItemOption(103, Util.nextInt(5, 10)));
                                    it.itemOptions.add(new ItemOption(93, Util.nextInt(3, 30)));
                                } else if (temp[index] == 822) { //cay thong noel
                                    it.itemOptions.add(new ItemOption(50, Util.nextInt(10, 20)));
                                    it.itemOptions.add(new ItemOption(77, Util.nextInt(10, 20)));
                                    it.itemOptions.add(new ItemOption(103, Util.nextInt(10, 20)));
                                    it.itemOptions.add(new ItemOption(93, Util.nextInt(3, 30)));
                                    it.itemOptions.add(new ItemOption(30, 0));
                                    it.itemOptions.add(new ItemOption(74, 0));
                                } else if (temp[index] == 746) { // xe truot tuyet
                                    it.itemOptions.add(new ItemOption(74, 0));
                                    it.itemOptions.add(new ItemOption(30, 0));
                                    if (Util.isTrue(99, 100)) {
                                        it.itemOptions.add(new ItemOption(93, Util.nextInt(30, 360)));
                                    }
                                } else if (temp[index] == 999) { // mèo mun
                                    it.itemOptions.add(new ItemOption(77, 15));
                                    it.itemOptions.add(new ItemOption(74, 0));
                                    it.itemOptions.add(new ItemOption(30, 0));
                                    if (Util.isTrue(99, 100)) {
                                        it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                                    }
                                } else if (temp[index] == 1000) { // xiên cá
                                    it.itemOptions.add(new ItemOption(103, 15));
                                    it.itemOptions.add(new ItemOption(74, 0));
                                    it.itemOptions.add(new ItemOption(30, 0));
                                    if (Util.isTrue(99, 100)) {
                                        it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                                    }
                                } else if (temp[index] == 1001) { // Phóng heo
                                    it.itemOptions.add(new ItemOption(50, 15));
                                    it.itemOptions.add(new ItemOption(74, 0));
                                    it.itemOptions.add(new ItemOption(30, 0));
                                    if (Util.isTrue(99, 100)) {
                                        it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                                    }
                                } else if (temp[index] == 2022 || temp[index] == 821) {
                                    it.itemOptions.add(new ItemOption(30, 0));
                                } else {
                                    it.itemOptions.add(new ItemOption(73, 0));
                                }
                                InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                                icon[1] = it.template.iconID;
                                CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
                                InventoryService.gI().addItemBag(pl, it, 0);
                                InventoryService.gI().sendItemBags(pl);
                            }
                        } else {
                            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");
                        }
                    } else {
                        Service.getInstance().sendThongBao(pl, "Sự kiện đã kết thúc");
                    }
                    break;
                case ConstEvent.SU_KIEN_TET:
                    if (Manager.EVENT_SEVER == idsukien) {
                        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
                            short[] icon = new short[2];
                            icon[0] = item.template.iconID;
                            RandomCollection<Integer> rd = Manager.HOP_QUA_TET;
                            int tempID = rd.next();
                            Item it = ItemService.gI().createNewItem((short) tempID);
                            if (it.template.type == 11) {//FLAGBAG
                                it.itemOptions.add(new ItemOption(50, Util.nextInt(5, 20)));
                                it.itemOptions.add(new ItemOption(77, Util.nextInt(5, 20)));
                                it.itemOptions.add(new ItemOption(103, Util.nextInt(5, 20)));
                            } else if (tempID >= 1159 && tempID <= 1161) {
                                it.itemOptions.add(new ItemOption(50, Util.nextInt(20, 30)));
                                it.itemOptions.add(new ItemOption(77, Util.nextInt(20, 30)));
                                it.itemOptions.add(new ItemOption(103, Util.nextInt(20, 30)));
                                it.itemOptions.add(new ItemOption(106, 0));
                            } else if (tempID == ConstItem.CAI_TRANG_SSJ_3_WHITE) {
                                it.itemOptions.add(new ItemOption(50, Util.nextInt(30, 40)));
                                it.itemOptions.add(new ItemOption(77, Util.nextInt(30, 40)));
                                it.itemOptions.add(new ItemOption(103, Util.nextInt(30, 40)));
                                it.itemOptions.add(new ItemOption(5, Util.nextInt(10, 25)));
                                it.itemOptions.add(new ItemOption(104, Util.nextInt(5, 15)));
                            }
                            int type = it.template.type;
                            if (type == 5 || type == 11) {// cải trang & flagbag
                                if (Util.isTrue(199, 200)) {
                                    it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                                }
                                it.itemOptions.add(new ItemOption(199, 0));//KHÔNG THỂ GIA HẠN
                            } else if (type == 23) {// thú cưỡi
                                if (Util.isTrue(199, 200)) {
                                    it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 5)));
                                }
                            }
                            if (tempID >= ConstItem.MANH_AO && tempID <= ConstItem.MANH_GANG_TAY) {
                                it.quantity = Util.nextInt(5, 15);
                            } else {
                                it.itemOptions.add(new ItemOption(74, 0));
                            }
                            icon[1] = it.template.iconID;
                            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                            CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
                            InventoryService.gI().addItemBag(pl, it, 0);
                            InventoryService.gI().sendItemBags(pl);
                            break;
                        } else {
                            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");
                        }
                    } else {
                        Service.getInstance().sendThongBao(pl, "Sự kiện đã kết thúc");
                    }
                    break;
                case 5:
                    if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
                        short[] icon = new short[2];
                        icon[0] = item.template.iconID;
                        int tempID;
                        if (Util.isTrue(30, 100)) {
                            byte getRandom = (byte) Util.getOne(0, 1);
                            if (getRandom == 0) {
                                tempID = 584;
                            } else {
                                tempID = 861;
                            }
                        } else if (Util.isTrue(20, 100)) {
                            byte getRandom = (byte) Util.getOne(0, 1);
                            if (getRandom == 0) {
                                tempID = Util.getOne(733, 734);
                            } else {
                                tempID = 861;
                            }
                        } else if (Util.isTrue(15, 100)) {
                            tempID = 457;
                        } else {
                            tempID = 190;
                        }
                        Item it = ItemService.gI().createNewItem((short) tempID);
                        switch (tempID) {
                            case 568:
                                MabuEgg.createMabuEgg(pl);
                                InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                                Service.getInstance().sendThongBao(pl, "Trứng ma bư hiện đang được ấp tại nhà");
                                break;
                            case 457:
                                it.quantity = Util.nextInt(1, 5);
                                break;
                            case 861:
                                it.quantity = Util.nextInt(100, 500);
                                break;
                            case 584:
                                it.itemOptions.add(new ItemOption(77, 35));
                                it.itemOptions.add(new ItemOption(103, 35));
                                it.itemOptions.add(new ItemOption(50, 35));
                                it.itemOptions.add(new ItemOption(116, 0));
                                if (!Util.isTrue(10, 150)) {
                                    it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 3)));
                                }
                                it.itemOptions.add(new ItemOption(74, 0));
                                break;
                            case 733:
                            case 734:
                                it.itemOptions.add(new ItemOption(77, 5));
                                it.itemOptions.add(new ItemOption(103, 5));
                                it.itemOptions.add(new ItemOption(50, 5));
                                it.itemOptions.add(new ItemOption(74, 0));
                                break;
                        }
                        icon[1] = it.template.iconID;
                        InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                        CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
                        InventoryService.gI().addItemBag(pl, it, 0);
                        InventoryService.gI().sendItemBags(pl);
                        break;
                    } else {
                        Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");
                    }
                    break;
            }
        } catch (Exception e) {
            logger.error("Lỗi mở hộp quà", e);
        }
    }

    private void openboxkichhoat(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            short[] temp = {76, 188, 189, 190, 441, 442, 447, 2010, 2009, 865, 938, 939, 940, 16, 17, 18, 19, 20, 946, 947, 948, 382, 383, 384, 385};
            int[][] gold = {{5000, 20000}};
            byte index = (byte) Util.nextInt(0, temp.length - 1);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            if (index <= 3 && index >= 0) {
                pl.inventory.addGold(Util.nextInt(gold[0][0], gold[0][1]));
                PlayerService.gI().sendInfoHpMpMoney(pl);
                icon[1] = 930;
            } else {

                Item it = ItemService.gI().createNewItem(temp[index]);
                if (temp[index] == 441) {
                    it.itemOptions.add(new ItemOption(95, 5));
                } else if (temp[index] == 442) {
                    it.itemOptions.add(new ItemOption(96, 5));
                } else if (temp[index] == 447) {
                    it.itemOptions.add(new ItemOption(101, 5));
                } else if (temp[index] >= 2009 && temp[index] <= 2010) {
                    it.itemOptions.add(new ItemOption(30, 0));
                } else if (temp[index] == 865) {
                    it.itemOptions.add(new ItemOption(30, 0));
                    if (Util.isTrue(1, 20)) {
                        it.itemOptions.add(new ItemOption(93, 365));
                    } else {
                        it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                    }
                } else if (temp[index] >= 938 && temp[index] <= 940) {
                    it.itemOptions.add(new ItemOption(77, 35));
                    it.itemOptions.add(new ItemOption(103, 35));
                    it.itemOptions.add(new ItemOption(50, 35));
                    if (Util.isTrue(1, 50)) {
                        it.itemOptions.add(new ItemOption(116, 0));
                    } else {
                        it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                    }
                } else if (temp[index] >= 946 && temp[index] <= 948) {
                    it.itemOptions.add(new ItemOption(77, 35));
                    it.itemOptions.add(new ItemOption(103, 35));
                    it.itemOptions.add(new ItemOption(50, 35));
                    if (Util.isTrue(1, 20)) {
                        it.itemOptions.add(new ItemOption(93, 365));
                    } else {
                        it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                    }
                } else {
                    it.itemOptions.add(new ItemOption(73, 0));
                }
                InventoryService.gI().addItemBag(pl, it, 0);
                icon[1] = it.template.iconID;

            }
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBags(pl);

            CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }

    private void changePetBerus(Player player, Item item) {
        if (player.pet != null) {
            int gender = player.pet.gender;
            PetService.gI().changeBillPet(player, gender);
            InventoryService.gI().subQuantityItemsBag(player, item, 1);
        } else {
            Service.getInstance().sendThongBao(player, "Không thể thực hiện");
        }
    }

    private void changeMabuPet(Player player, Item item) {
        if (player.pet != null) {
            int gender = player.pet.gender;
            PetService.gI().changeMabuPet(player, gender);
            InventoryService.gI().subQuantityItemsBag(player, item, 1);
        } else {
            Service.getInstance().sendThongBao(player, "Không thể thực hiện");
        }
    }

    private void changeVidelPet(Player player, Item item) {
        if (player.pet != null) {
            int gender = player.pet.gender;
            PetService.gI().changeVidelPet(player, gender);
            InventoryService.gI().subQuantityItemsBag(player, item, 1);
        } else {
            Service.getInstance().sendThongBao(player, "Không thể thực hiện");
        }
    }

    private void changeGokuBluePet(Player player, Item item) {
        if (player.pet != null) {
            int gender = player.pet.gender;
            PetService.gI().changeGokuBluePet(player, gender);
            InventoryService.gI().subQuantityItemsBag(player, item, 1);
        } else {
            Service.getInstance().sendThongBao(player, "Không thể thực hiện");
        }
    }

    private void changeVegetaBluePet(Player player, Item item) {
        if (player.pet != null) {
            int gender = player.pet.gender;
            PetService.gI().changeVegetaBluePet(player, gender);
            InventoryService.gI().subQuantityItemsBag(player, item, 1);
        } else {
            Service.getInstance().sendThongBao(player, "Không thể thực hiện");
        }
    }

    private void changeGohanBluePet(Player player, Item item) {
        if (player.pet != null) {
            int gender = player.pet.gender;
            PetService.gI().changeGohanBluePet(player, gender);
            InventoryService.gI().subQuantityItemsBag(player, item, 1);
        } else {
            Service.getInstance().sendThongBao(player, "Không thể thực hiện");
        }
    }

    private void changeGokuGodPet(Player player, Item item) {
        if (player.pet != null) {
            int gender = player.pet.gender;
            PetService.gI().changeGokuGodPet(player, gender);
            InventoryService.gI().subQuantityItemsBag(player, item, 1);
        } else {
            Service.getInstance().sendThongBao(player, "Không thể thực hiện");
        }
    }

    private void changeVegetaGodPet(Player player, Item item) {
        if (player.pet != null) {
            int gender = player.pet.gender;
            PetService.gI().changeVegetaGodPet(player, gender);
            InventoryService.gI().subQuantityItemsBag(player, item, 1);
        } else {
            Service.getInstance().sendThongBao(player, "Không thể thực hiện");
        }
    }

    private void changeGohanGodPet(Player player, Item item) {
        if (player.pet != null) {
            int gender = player.pet.gender;
            PetService.gI().changeGohanGodPet(player, gender);
            InventoryService.gI().subQuantityItemsBag(player, item, 1);
        } else {
            Service.getInstance().sendThongBao(player, "Không thể thực hiện");
        }
    }
      private void changebroly(Player player, Item item) {
        if (player.pet != null) {
            int gender = player.pet.gender;
            PetService.gI().changebrolyPet(player, gender);
            InventoryService.gI().subQuantityItemsBag(player, item, 1);
        } else {
            Service.getInstance().sendThongBao(player, "Không thể thực hiện");
        }
    } 
    
    

    public void ComfirmMocNap(Player player) {
        if (InventoryService.gI().getCountEmptyBag(player) > 5) {
            Item itemqua;
            Item itemqua1;
            Item itemqua2;
            Item itemqua3;
            Item itemqua4;
            Item itemqua5;
            try {
                int time = 5;
                if (player.getSession().tongnap >= 100000 && player.mocnap == 0) {
                    Service.gI().sendThongBao(player, "Tiến Hành Nhận\nMốc Nạp 100K\nSau " + time + " Giây!");
                    while (time > 0) {
                        time--;
                        Thread.sleep(1000);
                        Service.gI().sendThongBao(player, "|6|" + time);
                    }
                    player.mocnap = 100000;
                    itemqua = ItemService.gI().createNewItem((short) 729, 1);
                    itemqua.itemOptions.add(new ItemOption(46, 0));
                    itemqua.itemOptions.add(new ItemOption(167, 0));
                    itemqua.itemOptions.add(new ItemOption(52, 1999));
                    itemqua.itemOptions.add(new ItemOption(53, 1999));
                    itemqua.itemOptions.add(new ItemOption(54, 1999));
                    itemqua.itemOptions.add(new ItemOption(55, 5));
                    itemqua.itemOptions.add(new ItemOption(56, 5));
                    itemqua.itemOptions.add(new ItemOption(57, 5));
                    itemqua.itemOptions.add(new ItemOption(58, 25));
                    itemqua.itemOptions.add(new ItemOption(61, 50));
                     itemqua.itemOptions.add(new ItemOption(107, 25));
                      itemqua.itemOptions.add(new ItemOption(72, 10));
                    itemqua.itemOptions.add(new ItemOption(30, 1));
                    
                    

                    itemqua1 = ItemService.gI().createNewItem((short) 1983, 1000);
                    itemqua1.itemOptions.add(new ItemOption(72, 10));
                    itemqua1.itemOptions.add(new ItemOption(30, 1));

                    itemqua2 = ItemService.gI().createNewItem((short) 2062, 1000);
                    itemqua2.itemOptions.add(new ItemOption(72, 10));
                    itemqua2.itemOptions.add(new ItemOption(30, 1));

                    itemqua3 = ItemService.gI().createNewItem((short) 2063, 100);
                    itemqua3.itemOptions.add(new ItemOption(72, 10));
                    itemqua3.itemOptions.add(new ItemOption(30, 0));

                    itemqua4 = ItemService.gI().createNewItem((short) 1502, 1000);
                    itemqua4.itemOptions.add(new ItemOption(30, 1));

                    Service.gI().sendThongBaoFromAdmin(player, "Bạn Đã Nhận Được 5 quà Mốc\n"
                            + "Cảm On Bạn Đã Ủng Hộ\n"
                            + "Không Hỗ Trợ Nhận Lai Mốc\n"
                            + "Max Mốc 350tr");
                    InventoryService.gI().addItemBag(player, itemqua, 999999);
                    InventoryService.gI().addItemBag(player, itemqua1, 999999);
                    InventoryService.gI().addItemBag(player, itemqua2, 999999);
                    InventoryService.gI().addItemBag(player, itemqua3, 999999);
                    InventoryService.gI().addItemBag(player, itemqua4, 999999);

                    InventoryService.gI().sendItemBags(player);
                } else if (player.getSession().tongnap >= 200000 && player.mocnap == 100000) {
                    Service.gI().sendThongBao(player, "Tiến Hành Nhận\nMốc Nạp 200K\nSau " + time + " Giây!");
                    while (time > 0) {
                        time--;
                        Thread.sleep(1000);
                        Service.gI().sendThongBao(player, "|7|" + time);
                    }
                    player.mocnap = 200000;
                       itemqua = ItemService.gI().createNewItem((short) 1112, 1);
                    itemqua.itemOptions.add(new ItemOption(46, 0));
                    itemqua.itemOptions.add(new ItemOption(167, 0));
                    itemqua.itemOptions.add(new ItemOption(52, 1999));
                    itemqua.itemOptions.add(new ItemOption(53, 1999));
                    itemqua.itemOptions.add(new ItemOption(54, 1999));
                    itemqua.itemOptions.add(new ItemOption(55, 5));
                    itemqua.itemOptions.add(new ItemOption(56, 5));
                    itemqua.itemOptions.add(new ItemOption(57, 5));
                    itemqua.itemOptions.add(new ItemOption(58, 25));
                    itemqua.itemOptions.add(new ItemOption(61, 50));
                     itemqua.itemOptions.add(new ItemOption(107, 25));
                      itemqua.itemOptions.add(new ItemOption(72, 10));
                    itemqua.itemOptions.add(new ItemOption(30, 1));
                    
                    

                    itemqua1 = ItemService.gI().createNewItem((short) 1983, 1000);
                    itemqua1.itemOptions.add(new ItemOption(72, 10));
                    itemqua1.itemOptions.add(new ItemOption(30, 1));

                    itemqua2 = ItemService.gI().createNewItem((short) 2062, 1000);
                    itemqua2.itemOptions.add(new ItemOption(72, 10));
                    itemqua2.itemOptions.add(new ItemOption(30, 1));

                    itemqua3 = ItemService.gI().createNewItem((short) 2063, 100);
                    itemqua3.itemOptions.add(new ItemOption(72, 10));
                    itemqua3.itemOptions.add(new ItemOption(30, 0));

                    itemqua4 = ItemService.gI().createNewItem((short) 1502, 1000);
                    itemqua4.itemOptions.add(new ItemOption(30, 1));

                    Service.gI().sendThongBaoFromAdmin(player, "Bạn Đã Nhận Được 5 quà Mốc\n"
                            + "Cảm On Bạn Đã Ủng Hộ\n"
                            + "Không Hỗ Trợ Nhận Lai Mốc\n"
                            + "Max Mốc 350tr");
                    InventoryService.gI().addItemBag(player, itemqua, 999999);
                    InventoryService.gI().addItemBag(player, itemqua1, 999999);
                    InventoryService.gI().addItemBag(player, itemqua2, 999999);
                    InventoryService.gI().addItemBag(player, itemqua3, 999999);
                    InventoryService.gI().addItemBag(player, itemqua4, 999999);

                    InventoryService.gI().sendItemBags(player);
                } else if (player.getSession().tongnap >= 300000 && player.mocnap == 200000) {
                    Service.gI().sendThongBao(player, "Tiến Hành Nhận\nMốc Nạp 300K\nSau " + time + " Giây!");
                    while (time > 0) {
                        time--;
                        Thread.sleep(1000);
                        Service.gI().sendThongBao(player, "|7|" + time);
                    }
                    player.mocnap = 300000;
                     itemqua = ItemService.gI().createNewItem((short) 1338, 1);
                    itemqua.itemOptions.add(new ItemOption(46, 0));
                    itemqua.itemOptions.add(new ItemOption(167, 0));
                    itemqua.itemOptions.add(new ItemOption(52, 1999));
                    itemqua.itemOptions.add(new ItemOption(53, 1999));
                    itemqua.itemOptions.add(new ItemOption(54, 1999));
                    itemqua.itemOptions.add(new ItemOption(55, 5));
                    itemqua.itemOptions.add(new ItemOption(56, 5));
                    itemqua.itemOptions.add(new ItemOption(57, 5));
                    itemqua.itemOptions.add(new ItemOption(58, 25));
                    itemqua.itemOptions.add(new ItemOption(61, 50));
                     itemqua.itemOptions.add(new ItemOption(107, 25));
                      itemqua.itemOptions.add(new ItemOption(72, 10));
                    itemqua.itemOptions.add(new ItemOption(30, 1));
                    
                    

                    itemqua1 = ItemService.gI().createNewItem((short) 1983, 1000);
                    itemqua1.itemOptions.add(new ItemOption(72, 10));
                    itemqua1.itemOptions.add(new ItemOption(30, 1));

                    itemqua2 = ItemService.gI().createNewItem((short) 2062, 1000);
                    itemqua2.itemOptions.add(new ItemOption(72, 10));
                    itemqua2.itemOptions.add(new ItemOption(30, 1));

                    itemqua3 = ItemService.gI().createNewItem((short) 2063, 100);
                    itemqua3.itemOptions.add(new ItemOption(72, 10));
                    itemqua3.itemOptions.add(new ItemOption(30, 0));

                    itemqua4 = ItemService.gI().createNewItem((short) 1502, 1000);
                    itemqua4.itemOptions.add(new ItemOption(30, 1));

                    Service.gI().sendThongBaoFromAdmin(player, "Bạn Đã Nhận Được 5 quà Mốc\n"
                            + "Cảm On Bạn Đã Ủng Hộ\n"
                            + "Không Hỗ Trợ Nhận Lai Mốc\n"
                            + "Max Mốc 350tr");
                    InventoryService.gI().addItemBag(player, itemqua, 999999);
                    InventoryService.gI().addItemBag(player, itemqua1, 999999);
                    InventoryService.gI().addItemBag(player, itemqua2, 999999);
                    InventoryService.gI().addItemBag(player, itemqua3, 999999);
                    InventoryService.gI().addItemBag(player, itemqua4, 999999);

                    InventoryService.gI().sendItemBags(player);
                } else if (player.getSession().tongnap >= 500000 && player.mocnap == 300000) {
                    Service.gI().sendThongBao(player, "Tiến Hành Nhận\nMốc Nạp 500K\nSau " + time + " Giây!");
                    while (time > 0) {
                        time--;
                        Thread.sleep(1000);
                        Service.gI().sendThongBao(player, "|7|" + time);
                    }
                    player.mocnap = 500000;
                    itemqua = ItemService.gI().createNewItem((short) 733, 1);
                    itemqua.itemOptions.add(new ItemOption(46, 0));
                    itemqua.itemOptions.add(new ItemOption(167, 0));
                    itemqua.itemOptions.add(new ItemOption(52, 1999));
                    itemqua.itemOptions.add(new ItemOption(53, 1999));
                    itemqua.itemOptions.add(new ItemOption(54, 1999));
                    itemqua.itemOptions.add(new ItemOption(55, 5));
                    itemqua.itemOptions.add(new ItemOption(56, 5));
                    itemqua.itemOptions.add(new ItemOption(57, 5));
                    itemqua.itemOptions.add(new ItemOption(58, 25));
                    itemqua.itemOptions.add(new ItemOption(61, 50));
                     itemqua.itemOptions.add(new ItemOption(107, 25));
                      itemqua.itemOptions.add(new ItemOption(72, 10));
                    itemqua.itemOptions.add(new ItemOption(30, 1));
                    
                    

                    itemqua1 = ItemService.gI().createNewItem((short) 1983, 1000);
                    itemqua1.itemOptions.add(new ItemOption(72, 10));
                    itemqua1.itemOptions.add(new ItemOption(30, 1));

                    itemqua2 = ItemService.gI().createNewItem((short) 2062, 1000);
                    itemqua2.itemOptions.add(new ItemOption(72, 10));
                    itemqua2.itemOptions.add(new ItemOption(30, 1));

                    itemqua3 = ItemService.gI().createNewItem((short) 2063, 100);
                    itemqua3.itemOptions.add(new ItemOption(72, 10));
                    itemqua3.itemOptions.add(new ItemOption(30, 0));

                    itemqua4 = ItemService.gI().createNewItem((short) 1502, 1000);
                    itemqua4.itemOptions.add(new ItemOption(30, 1));

                    Service.gI().sendThongBaoFromAdmin(player, "Bạn Đã Nhận Được 5 quà Mốc\n"
                            + "Cảm On Bạn Đã Ủng Hộ\n"
                            + "Không Hỗ Trợ Nhận Lai Mốc\n"
                            + "Max Mốc 350tr");
                    InventoryService.gI().addItemBag(player, itemqua, 999999);
                    InventoryService.gI().addItemBag(player, itemqua1, 999999);
                    InventoryService.gI().addItemBag(player, itemqua2, 999999);
                    InventoryService.gI().addItemBag(player, itemqua3, 999999);
                    InventoryService.gI().addItemBag(player, itemqua4, 999999);

                    InventoryService.gI().sendItemBags(player);
                } else if (player.getSession().tongnap >= 800000 && player.mocnap == 500000) {
                    Service.gI().sendThongBao(player, "Tiến Hành Nhận\nMốc Nạp 800K\nSau " + time + " Giây!");
                    while (time > 0) {
                        time--;
                        Thread.sleep(1000);
                        Service.gI().sendThongBao(player, "|7|" + time);
                    }
                    player.mocnap = 800000;
                    itemqua = ItemService.gI().createNewItem((short) 1479, 1);
                    itemqua.itemOptions.add(new ItemOption(46, 0));
                    itemqua.itemOptions.add(new ItemOption(167, 0));
                    itemqua.itemOptions.add(new ItemOption(52, 1999));
                    itemqua.itemOptions.add(new ItemOption(53, 1999));
                    itemqua.itemOptions.add(new ItemOption(54, 1999));
                    itemqua.itemOptions.add(new ItemOption(55, 5));
                    itemqua.itemOptions.add(new ItemOption(56, 5));
                    itemqua.itemOptions.add(new ItemOption(57, 5));
                    itemqua.itemOptions.add(new ItemOption(58, 25));
                    itemqua.itemOptions.add(new ItemOption(61, 50));
                     itemqua.itemOptions.add(new ItemOption(107, 25));
                      itemqua.itemOptions.add(new ItemOption(72, 10));
                    itemqua.itemOptions.add(new ItemOption(30, 1));
                    
                    

                    itemqua1 = ItemService.gI().createNewItem((short) 1983, 1000);
                    itemqua1.itemOptions.add(new ItemOption(72, 10));
                    itemqua1.itemOptions.add(new ItemOption(30, 1));

                    itemqua2 = ItemService.gI().createNewItem((short) 2062, 1000);
                    itemqua2.itemOptions.add(new ItemOption(72, 10));
                    itemqua2.itemOptions.add(new ItemOption(30, 1));

                    itemqua3 = ItemService.gI().createNewItem((short) 2063, 100);
                    itemqua3.itemOptions.add(new ItemOption(72, 10));
                    itemqua3.itemOptions.add(new ItemOption(30, 0));

                    itemqua4 = ItemService.gI().createNewItem((short) 1502, 1000);
                    itemqua4.itemOptions.add(new ItemOption(30, 1));

                    Service.gI().sendThongBaoFromAdmin(player, "Bạn Đã Nhận Được 5 quà Mốc\n"
                            + "Cảm On Bạn Đã Ủng Hộ\n"
                            + "Không Hỗ Trợ Nhận Lai Mốc\n"
                            + "Max Mốc 350tr");
                    InventoryService.gI().addItemBag(player, itemqua, 999999);
                    InventoryService.gI().addItemBag(player, itemqua1, 999999);
                    InventoryService.gI().addItemBag(player, itemqua2, 999999);
                    InventoryService.gI().addItemBag(player, itemqua3, 999999);
                    InventoryService.gI().addItemBag(player, itemqua4, 999999);

                    InventoryService.gI().sendItemBags(player);
                } else if (player.getSession().tongnap >= 1000000 && player.mocnap == 800000) {
                    Service.gI().sendThongBao(player, "Tiến Hành Nhận\nMốc Nạp 1 Triệu\nSau " + time + " Giây!");
                    while (time > 0) {
                        time--;
                        Thread.sleep(1000);
                        Service.gI().sendThongBao(player, "|7|" + time);
                    }
                    player.mocnap = 1000000;
                     itemqua = ItemService.gI().createNewItem((short) 1868, 1);
                    itemqua.itemOptions.add(new ItemOption(46, 0));
                    itemqua.itemOptions.add(new ItemOption(167, 0));
                    itemqua.itemOptions.add(new ItemOption(52, 1999));
                    itemqua.itemOptions.add(new ItemOption(53, 1999));
                    itemqua.itemOptions.add(new ItemOption(54, 1999));
                    itemqua.itemOptions.add(new ItemOption(55, 5));
                    itemqua.itemOptions.add(new ItemOption(56, 5));
                    itemqua.itemOptions.add(new ItemOption(57, 5));
                    itemqua.itemOptions.add(new ItemOption(58, 25));
                    itemqua.itemOptions.add(new ItemOption(61, 50));
                     itemqua.itemOptions.add(new ItemOption(107, 25));
                      itemqua.itemOptions.add(new ItemOption(72, 10));
                    itemqua.itemOptions.add(new ItemOption(30, 1));
                    
                    

                    itemqua1 = ItemService.gI().createNewItem((short) 1983, 1000);
                    itemqua1.itemOptions.add(new ItemOption(72, 10));
                    itemqua1.itemOptions.add(new ItemOption(30, 1));

                    itemqua2 = ItemService.gI().createNewItem((short) 2062, 1000);
                    itemqua2.itemOptions.add(new ItemOption(72, 10));
                    itemqua2.itemOptions.add(new ItemOption(30, 1));

                    itemqua3 = ItemService.gI().createNewItem((short) 2063, 100);
                    itemqua3.itemOptions.add(new ItemOption(72, 10));
                    itemqua3.itemOptions.add(new ItemOption(30, 0));

                    itemqua4 = ItemService.gI().createNewItem((short) 1502, 1000);
                    itemqua4.itemOptions.add(new ItemOption(30, 1));

                    Service.gI().sendThongBaoFromAdmin(player, "Bạn Đã Nhận Được 5 quà Mốc\n"
                            + "Cảm On Bạn Đã Ủng Hộ\n"
                            + "Không Hỗ Trợ Nhận Lai Mốc\n"
                            + "Max Mốc 350tr");
                    InventoryService.gI().addItemBag(player, itemqua, 999999);
                    InventoryService.gI().addItemBag(player, itemqua1, 999999);
                    InventoryService.gI().addItemBag(player, itemqua2, 999999);
                    InventoryService.gI().addItemBag(player, itemqua3, 999999);
                    InventoryService.gI().addItemBag(player, itemqua4, 999999);

                    InventoryService.gI().sendItemBags(player);
                } else if (player.getSession().tongnap >= 2000000 && player.mocnap == 1000000) {
                    Service.gI().sendThongBao(player, "Tiến Hành Nhận\nMốc Nạp 2 Triệu\nSau " + time + " Giây!");
                    while (time > 0) {
                        time--;
                        Thread.sleep(1000);
                        Service.gI().sendThongBao(player, "|7|" + time);
                    }
                    player.mocnap = 2000000;
                    itemqua = ItemService.gI().createNewItem((short) 1714, 1);
                    itemqua.itemOptions.add(new ItemOption(46, 0));
                    itemqua.itemOptions.add(new ItemOption(167, 0));
                    itemqua.itemOptions.add(new ItemOption(52, 1999));
                    itemqua.itemOptions.add(new ItemOption(53, 1999));
                    itemqua.itemOptions.add(new ItemOption(54, 1999));
                    itemqua.itemOptions.add(new ItemOption(55, 5));
                    itemqua.itemOptions.add(new ItemOption(56, 5));
                    itemqua.itemOptions.add(new ItemOption(57, 5));
                    itemqua.itemOptions.add(new ItemOption(58, 25));
                    itemqua.itemOptions.add(new ItemOption(61, 50));
                     itemqua.itemOptions.add(new ItemOption(107, 25));
                      itemqua.itemOptions.add(new ItemOption(72, 10));
                    itemqua.itemOptions.add(new ItemOption(30, 1));
                    
                    

                    itemqua1 = ItemService.gI().createNewItem((short) 1983, 1000);
                    itemqua1.itemOptions.add(new ItemOption(72, 10));
                    itemqua1.itemOptions.add(new ItemOption(30, 1));

                    itemqua2 = ItemService.gI().createNewItem((short) 2062, 1000);
                    itemqua2.itemOptions.add(new ItemOption(72, 10));
                    itemqua2.itemOptions.add(new ItemOption(30, 1));

                    itemqua3 = ItemService.gI().createNewItem((short) 2063, 100);
                    itemqua3.itemOptions.add(new ItemOption(72, 10));
                    itemqua3.itemOptions.add(new ItemOption(30, 0));

                    itemqua4 = ItemService.gI().createNewItem((short) 1502, 1000);
                    itemqua4.itemOptions.add(new ItemOption(30, 1));

                    Service.gI().sendThongBaoFromAdmin(player, "Bạn Đã Nhận Được 5 quà Mốc\n"
                            + "Cảm On Bạn Đã Ủng Hộ\n"
                            + "Không Hỗ Trợ Nhận Lai Mốc\n"
                            + "Max Mốc 350tr");
                    InventoryService.gI().addItemBag(player, itemqua, 999999);
                    InventoryService.gI().addItemBag(player, itemqua1, 999999);
                    InventoryService.gI().addItemBag(player, itemqua2, 999999);
                    InventoryService.gI().addItemBag(player, itemqua3, 999999);
                    InventoryService.gI().addItemBag(player, itemqua4, 999999);

                    InventoryService.gI().sendItemBags(player);
                } else if (player.getSession().tongnap >= 3000000 && player.mocnap == 2000000) {
                    Service.gI().sendThongBao(player, "Tiến Hành Nhận\nMốc Nạp 3 Triệu\nSau " + time + " Giây!");
                    while (time > 0) {
                        time--;
                        Thread.sleep(1000);
                        Service.gI().sendThongBao(player, "|7|" + time);
                    }
                    player.mocnap = 3000000;
                   itemqua = ItemService.gI().createNewItem((short) 1356, 1);
                    itemqua.itemOptions.add(new ItemOption(46, 0));
                    itemqua.itemOptions.add(new ItemOption(167, 0));
                    itemqua.itemOptions.add(new ItemOption(52, 1999));
                    itemqua.itemOptions.add(new ItemOption(53, 1999));
                    itemqua.itemOptions.add(new ItemOption(54, 1999));
                    itemqua.itemOptions.add(new ItemOption(55, 5));
                    itemqua.itemOptions.add(new ItemOption(56, 5));
                    itemqua.itemOptions.add(new ItemOption(57, 5));
                    itemqua.itemOptions.add(new ItemOption(58, 25));
                    itemqua.itemOptions.add(new ItemOption(61, 50));
                     itemqua.itemOptions.add(new ItemOption(107, 25));
                      itemqua.itemOptions.add(new ItemOption(72, 10));
                    itemqua.itemOptions.add(new ItemOption(30, 1));
                    
                    

                    itemqua1 = ItemService.gI().createNewItem((short) 1983, 1000);
                    itemqua1.itemOptions.add(new ItemOption(72, 10));
                    itemqua1.itemOptions.add(new ItemOption(30, 1));

                    itemqua2 = ItemService.gI().createNewItem((short) 2062, 1000);
                    itemqua2.itemOptions.add(new ItemOption(72, 10));
                    itemqua2.itemOptions.add(new ItemOption(30, 1));

                    itemqua3 = ItemService.gI().createNewItem((short) 2063, 100);
                    itemqua3.itemOptions.add(new ItemOption(72, 10));
                    itemqua3.itemOptions.add(new ItemOption(30, 0));

                    itemqua4 = ItemService.gI().createNewItem((short) 1502, 1000);
                    itemqua4.itemOptions.add(new ItemOption(30, 1));

                    Service.gI().sendThongBaoFromAdmin(player, "Bạn Đã Nhận Được 5 quà Mốc\n"
                            + "Cảm On Bạn Đã Ủng Hộ\n"
                            + "Không Hỗ Trợ Nhận Lai Mốc\n"
                            + "Max Mốc 350tr");
                    InventoryService.gI().addItemBag(player, itemqua, 999999);
                    InventoryService.gI().addItemBag(player, itemqua1, 999999);
                    InventoryService.gI().addItemBag(player, itemqua2, 999999);
                    InventoryService.gI().addItemBag(player, itemqua3, 999999);
                    InventoryService.gI().addItemBag(player, itemqua4, 999999);

                    InventoryService.gI().sendItemBags(player);
                } else if (player.getSession().tongnap >= 5000000 && player.mocnap == 3000000) {
                    Service.gI().sendThongBao(player, "Tiến Hành Nhận\nMốc Nạp 5 Triệu\nSau " + time + " Giây!");
                    while (time > 0) {
                        time--;
                        Thread.sleep(1000);
                        Service.gI().sendThongBao(player, "|7|" + time);
                    }
                    player.mocnap = 5000000;
                     itemqua = ItemService.gI().createNewItem((short) 1962, 1);
                    itemqua.itemOptions.add(new ItemOption(46, 0));
                    itemqua.itemOptions.add(new ItemOption(167, 0));
                    itemqua.itemOptions.add(new ItemOption(52, 1999));
                    itemqua.itemOptions.add(new ItemOption(53, 1999));
                    itemqua.itemOptions.add(new ItemOption(54, 1999));
                    itemqua.itemOptions.add(new ItemOption(55, 5));
                    itemqua.itemOptions.add(new ItemOption(56, 5));
                    itemqua.itemOptions.add(new ItemOption(57, 5));
                    itemqua.itemOptions.add(new ItemOption(58, 25));
                    itemqua.itemOptions.add(new ItemOption(61, 50));
                     itemqua.itemOptions.add(new ItemOption(107, 25));
                      itemqua.itemOptions.add(new ItemOption(72, 10));
                    itemqua.itemOptions.add(new ItemOption(30, 1));
                    
                    

                    itemqua1 = ItemService.gI().createNewItem((short) 1983, 1000);
                    itemqua1.itemOptions.add(new ItemOption(72, 10));
                    itemqua1.itemOptions.add(new ItemOption(30, 1));

                    itemqua2 = ItemService.gI().createNewItem((short) 2062, 1000);
                    itemqua2.itemOptions.add(new ItemOption(72, 10));
                    itemqua2.itemOptions.add(new ItemOption(30, 1));

                    itemqua3 = ItemService.gI().createNewItem((short) 2063, 100);
                    itemqua3.itemOptions.add(new ItemOption(72, 10));
                    itemqua3.itemOptions.add(new ItemOption(30, 0));

                    itemqua4 = ItemService.gI().createNewItem((short) 1502, 1000);
                    itemqua4.itemOptions.add(new ItemOption(30, 1));

                    Service.gI().sendThongBaoFromAdmin(player, "Bạn Đã Nhận Được 5 quà Mốc\n"
                            + "Cảm On Bạn Đã Ủng Hộ\n"
                            + "Không Hỗ Trợ Nhận Lai Mốc\n"
                            + "Max Mốc 350tr");
                    InventoryService.gI().addItemBag(player, itemqua, 999999);
                    InventoryService.gI().addItemBag(player, itemqua1, 999999);
                    InventoryService.gI().addItemBag(player, itemqua2, 999999);
                    InventoryService.gI().addItemBag(player, itemqua3, 999999);
                    InventoryService.gI().addItemBag(player, itemqua4, 999999);

                    InventoryService.gI().sendItemBags(player);
                } else if (player.getSession().tongnap >= 7000000 && player.mocnap == 5000000) {
                    Service.gI().sendThongBao(player, "Tiến Hành Nhận\nMốc Nạp 5 Triệu\nSau " + time + " Giây!");
                    while (time > 0) {
                        time--;
                        Thread.sleep(1000);
                        Service.gI().sendThongBao(player, "|7|" + time);
                    }
                    player.mocnap = 7000000;
                     itemqua = ItemService.gI().createNewItem((short) 2046, 1);
                    itemqua.itemOptions.add(new ItemOption(46, 0));
                    itemqua.itemOptions.add(new ItemOption(167, 0));
                    itemqua.itemOptions.add(new ItemOption(52, 1999));
                    itemqua.itemOptions.add(new ItemOption(53, 1999));
                    itemqua.itemOptions.add(new ItemOption(54, 1999));
                    itemqua.itemOptions.add(new ItemOption(55, 5));
                    itemqua.itemOptions.add(new ItemOption(56, 5));
                    itemqua.itemOptions.add(new ItemOption(57, 5));
                    itemqua.itemOptions.add(new ItemOption(58, 25));
                    itemqua.itemOptions.add(new ItemOption(61, 50));
                     itemqua.itemOptions.add(new ItemOption(107, 25));
                      itemqua.itemOptions.add(new ItemOption(72, 10));
                    itemqua.itemOptions.add(new ItemOption(30, 1));
                    
                    

                    itemqua1 = ItemService.gI().createNewItem((short) 1983, 1000);
                    itemqua1.itemOptions.add(new ItemOption(72, 10));
                    itemqua1.itemOptions.add(new ItemOption(30, 1));

                    itemqua2 = ItemService.gI().createNewItem((short) 2062, 1000);
                    itemqua2.itemOptions.add(new ItemOption(72, 10));
                    itemqua2.itemOptions.add(new ItemOption(30, 1));

                    itemqua3 = ItemService.gI().createNewItem((short) 2063, 100);
                    itemqua3.itemOptions.add(new ItemOption(72, 10));
                    itemqua3.itemOptions.add(new ItemOption(30, 0));

                    itemqua4 = ItemService.gI().createNewItem((short) 1502, 1000);
                    itemqua4.itemOptions.add(new ItemOption(30, 1));

                    Service.gI().sendThongBaoFromAdmin(player, "Bạn Đã Nhận Được 5 quà Mốc\n"
                            + "Cảm On Bạn Đã Ủng Hộ\n"
                            + "Không Hỗ Trợ Nhận Lai Mốc\n"
                            + "Max Mốc 350tr");
                    InventoryService.gI().addItemBag(player, itemqua, 999999);
                    InventoryService.gI().addItemBag(player, itemqua1, 999999);
                    InventoryService.gI().addItemBag(player, itemqua2, 999999);
                    InventoryService.gI().addItemBag(player, itemqua3, 999999);
                    InventoryService.gI().addItemBag(player, itemqua4, 999999);

                    InventoryService.gI().sendItemBags(player);
                } else if (player.getSession().tongnap >= 10000000 && player.mocnap == 7000000) {
                    Service.gI().sendThongBao(player, "Tiến Hành Nhận\nMốc Nạp  Triệu\nSau " + time + " Giây!");
                    while (time > 0) {
                        time--;
                        Thread.sleep(1000);
                        Service.gI().sendThongBao(player, "|7|" + time);
                    }
                    player.mocnap = 10000000;
                      itemqua = ItemService.gI().createNewItem((short) 2052, 1);
                    itemqua.itemOptions.add(new ItemOption(46, 0));
                    itemqua.itemOptions.add(new ItemOption(167, 0));
                    itemqua.itemOptions.add(new ItemOption(52, 1999));
                    itemqua.itemOptions.add(new ItemOption(53, 1999));
                    itemqua.itemOptions.add(new ItemOption(54, 1999));
                    itemqua.itemOptions.add(new ItemOption(55, 5));
                    itemqua.itemOptions.add(new ItemOption(56, 5));
                    itemqua.itemOptions.add(new ItemOption(57, 5));
                    itemqua.itemOptions.add(new ItemOption(58, 25));
                    itemqua.itemOptions.add(new ItemOption(61, 50));
                     itemqua.itemOptions.add(new ItemOption(107, 25));
                      itemqua.itemOptions.add(new ItemOption(72, 10));
                    itemqua.itemOptions.add(new ItemOption(30, 1));
                    
                    

                    itemqua1 = ItemService.gI().createNewItem((short) 1983, 1000);
                    itemqua1.itemOptions.add(new ItemOption(72, 10));
                    itemqua1.itemOptions.add(new ItemOption(30, 1));

                    itemqua2 = ItemService.gI().createNewItem((short) 2062, 1000);
                    itemqua2.itemOptions.add(new ItemOption(72, 10));
                    itemqua2.itemOptions.add(new ItemOption(30, 1));

                    itemqua3 = ItemService.gI().createNewItem((short) 2063, 100);
                    itemqua3.itemOptions.add(new ItemOption(72, 10));
                    itemqua3.itemOptions.add(new ItemOption(30, 0));

                    itemqua4 = ItemService.gI().createNewItem((short) 1502, 1000);
                    itemqua4.itemOptions.add(new ItemOption(30, 1));

                    Service.gI().sendThongBaoFromAdmin(player, "Bạn Đã Nhận Được 5 quà Mốc\n"
                            + "Cảm On Bạn Đã Ủng Hộ\n"
                            + "Không Hỗ Trợ Nhận Lai Mốc\n"
                            + "Max Mốc 350tr");
                    InventoryService.gI().addItemBag(player, itemqua, 999999);
                    InventoryService.gI().addItemBag(player, itemqua1, 999999);
                    InventoryService.gI().addItemBag(player, itemqua2, 999999);
                    InventoryService.gI().addItemBag(player, itemqua3, 999999);
                    InventoryService.gI().addItemBag(player, itemqua4, 999999);

                    InventoryService.gI().sendItemBags(player);
                } else if (player.getSession().tongnap >= 12000000 && player.mocnap == 10000000) {
                    Service.gI().sendThongBao(player, "Tiến Hành Nhận\nMốc Nạp  Triệu\nSau " + time + " Giây!");
                    while (time > 0) {
                        time--;
                        Thread.sleep(1000);
                        Service.gI().sendThongBao(player, "|7|" + time);
                    }
                    player.mocnap = 12000000;
                       itemqua = ItemService.gI().createNewItem((short) 1992, 1);
                    itemqua.itemOptions.add(new ItemOption(46, 0));
                    itemqua.itemOptions.add(new ItemOption(197, 0));
                    itemqua.itemOptions.add(new ItemOption(52, 1999));
                    itemqua.itemOptions.add(new ItemOption(53, 1999));
                    itemqua.itemOptions.add(new ItemOption(54, 1999));
                    itemqua.itemOptions.add(new ItemOption(55, 5));
                    itemqua.itemOptions.add(new ItemOption(56, 5));
                    itemqua.itemOptions.add(new ItemOption(57, 5));
                    itemqua.itemOptions.add(new ItemOption(58, 25));
                    itemqua.itemOptions.add(new ItemOption(61, 50));
                   itemqua.itemOptions.add(new ItemOption(107, 50));
                      itemqua.itemOptions.add(new ItemOption(72, 10));
                    itemqua.itemOptions.add(new ItemOption(30, 1));
                    
                    

                    itemqua1 = ItemService.gI().createNewItem((short) 1983, 1000);
                    itemqua1.itemOptions.add(new ItemOption(72, 10));
                    itemqua1.itemOptions.add(new ItemOption(30, 1));

                    itemqua2 = ItemService.gI().createNewItem((short) 2062, 1000);
                    itemqua2.itemOptions.add(new ItemOption(72, 10));
                    itemqua2.itemOptions.add(new ItemOption(30, 1));

                    itemqua3 = ItemService.gI().createNewItem((short) 2063, 100);
                    itemqua3.itemOptions.add(new ItemOption(72, 10));
                    itemqua3.itemOptions.add(new ItemOption(30, 0));

                    itemqua4 = ItemService.gI().createNewItem((short) 1502, 1000);
                    itemqua4.itemOptions.add(new ItemOption(30, 1));

                    Service.gI().sendThongBaoFromAdmin(player, "Bạn Đã Nhận Được 5 quà Mốc\n"
                            + "Cảm On Bạn Đã Ủng Hộ\n"
                            + "Không Hỗ Trợ Nhận Lai Mốc\n"
                            + "Max Mốc 350tr");
                    InventoryService.gI().addItemBag(player, itemqua, 999999);
                    InventoryService.gI().addItemBag(player, itemqua1, 999999);
                    InventoryService.gI().addItemBag(player, itemqua2, 999999);
                    InventoryService.gI().addItemBag(player, itemqua3, 999999);
                    InventoryService.gI().addItemBag(player, itemqua4, 999999);

                    InventoryService.gI().sendItemBags(player);
                } else if (player.getSession().tongnap >= 15000000 && player.mocnap == 12000000) {
                    Service.gI().sendThongBao(player, "Tiến Hành Nhận\nMốc Nạp  Triệu\nSau " + time + " Giây!");
                    while (time > 0) {
                        time--;
                        Thread.sleep(1000);
                        Service.gI().sendThongBao(player, "|7|" + time);
                    }
                    player.mocnap = 15000000;
                      itemqua = ItemService.gI().createNewItem((short) 1112, 1);
                    itemqua.itemOptions.add(new ItemOption(46, 0));
                    itemqua.itemOptions.add(new ItemOption(197, 0));
                    itemqua.itemOptions.add(new ItemOption(52, 1999));
                    itemqua.itemOptions.add(new ItemOption(53, 1999));
                    itemqua.itemOptions.add(new ItemOption(54, 1999));
                    itemqua.itemOptions.add(new ItemOption(55, 5));
                    itemqua.itemOptions.add(new ItemOption(56, 5));
                    itemqua.itemOptions.add(new ItemOption(57, 5));
                    itemqua.itemOptions.add(new ItemOption(58, 25));
                    itemqua.itemOptions.add(new ItemOption(61, 50));
                     itemqua.itemOptions.add(new ItemOption(107, 50));
                      itemqua.itemOptions.add(new ItemOption(72, 10));
                    itemqua.itemOptions.add(new ItemOption(30, 1));
                    
                    

                    itemqua1 = ItemService.gI().createNewItem((short) 1983, 1000);
                    itemqua1.itemOptions.add(new ItemOption(72, 10));
                    itemqua1.itemOptions.add(new ItemOption(30, 1));

                    itemqua2 = ItemService.gI().createNewItem((short) 2062, 1000);
                    itemqua2.itemOptions.add(new ItemOption(72, 10));
                    itemqua2.itemOptions.add(new ItemOption(30, 1));

                    itemqua3 = ItemService.gI().createNewItem((short) 2063, 100);
                    itemqua3.itemOptions.add(new ItemOption(72, 10));
                    itemqua3.itemOptions.add(new ItemOption(30, 0));

                    itemqua4 = ItemService.gI().createNewItem((short) 1502, 1000);
                    itemqua4.itemOptions.add(new ItemOption(30, 1));

                    Service.gI().sendThongBaoFromAdmin(player, "Bạn Đã Nhận Được 5 quà Mốc\n"
                            + "Cảm On Bạn Đã Ủng Hộ\n"
                            + "Không Hỗ Trợ Nhận Lai Mốc\n"
                            + "Max Mốc 350tr");
                    InventoryService.gI().addItemBag(player, itemqua, 999999);
                    InventoryService.gI().addItemBag(player, itemqua1, 999999);
                    InventoryService.gI().addItemBag(player, itemqua2, 999999);
                    InventoryService.gI().addItemBag(player, itemqua3, 999999);
                    InventoryService.gI().addItemBag(player, itemqua4, 999999);

                    InventoryService.gI().sendItemBags(player);

                } else if (player.getSession().tongnap >= 17000000 && player.mocnap == 15000000) {
                    Service.gI().sendThongBao(player, "Tiến Hành Nhận\nMốc Nạp  Triệu\nSau " + time + " Giây!");
                    while (time > 0) {
                        time--;
                        Thread.sleep(1000);
                        Service.gI().sendThongBao(player, "|7|" + time);
                    }
                    player.mocnap = 17000000;
                        itemqua = ItemService.gI().createNewItem((short) 1606, 1);
                    itemqua.itemOptions.add(new ItemOption(46, 0));
                    itemqua.itemOptions.add(new ItemOption(197, 0));
                    itemqua.itemOptions.add(new ItemOption(52, 1999));
                    itemqua.itemOptions.add(new ItemOption(53, 1999));
                    itemqua.itemOptions.add(new ItemOption(54, 1999));
                    itemqua.itemOptions.add(new ItemOption(55, 5));
                    itemqua.itemOptions.add(new ItemOption(56, 5));
                    itemqua.itemOptions.add(new ItemOption(57, 5));
                    itemqua.itemOptions.add(new ItemOption(58, 25));
                    itemqua.itemOptions.add(new ItemOption(61, 50));
                    itemqua.itemOptions.add(new ItemOption(107, 50));
                      itemqua.itemOptions.add(new ItemOption(72, 10));
                    itemqua.itemOptions.add(new ItemOption(30, 1));
                    
                    

                    itemqua1 = ItemService.gI().createNewItem((short) 1983, 1000);
                    itemqua1.itemOptions.add(new ItemOption(72, 10));
                    itemqua1.itemOptions.add(new ItemOption(30, 1));

                    itemqua2 = ItemService.gI().createNewItem((short) 2062, 1000);
                    itemqua2.itemOptions.add(new ItemOption(72, 10));
                    itemqua2.itemOptions.add(new ItemOption(30, 1));

                    itemqua3 = ItemService.gI().createNewItem((short) 2063, 100);
                    itemqua3.itemOptions.add(new ItemOption(72, 10));
                    itemqua3.itemOptions.add(new ItemOption(30, 0));

                    itemqua4 = ItemService.gI().createNewItem((short) 1502, 1000);
                    itemqua4.itemOptions.add(new ItemOption(30, 1));

                    Service.gI().sendThongBaoFromAdmin(player, "Bạn Đã Nhận Được 5 quà Mốc\n"
                            + "Cảm On Bạn Đã Ủng Hộ\n"
                            + "Không Hỗ Trợ Nhận Lai Mốc\n"
                            + "Max Mốc 350tr");
                    InventoryService.gI().addItemBag(player, itemqua, 999999);
                    InventoryService.gI().addItemBag(player, itemqua1, 999999);
                    InventoryService.gI().addItemBag(player, itemqua2, 999999);
                    InventoryService.gI().addItemBag(player, itemqua3, 999999);
                    InventoryService.gI().addItemBag(player, itemqua4, 999999);

                    InventoryService.gI().sendItemBags(player);

                } else if (player.getSession().tongnap >= 20000000 && player.mocnap == 17000000) {
                    Service.gI().sendThongBao(player, "Tiến Hành Nhận\nMốc Nạp  Triệu\nSau " + time + " Giây!");
                    while (time > 0) {
                        time--;
                        Thread.sleep(1000);
                        Service.gI().sendThongBao(player, "|7|" + time);
                    }
                    player.mocnap = 20000000;
                       itemqua = ItemService.gI().createNewItem((short) 1990, 1);
                    itemqua.itemOptions.add(new ItemOption(46, 0));
                    itemqua.itemOptions.add(new ItemOption(197, 0));
                    itemqua.itemOptions.add(new ItemOption(52, 1999));
                    itemqua.itemOptions.add(new ItemOption(53, 1999));
                    itemqua.itemOptions.add(new ItemOption(54, 1999));
                    itemqua.itemOptions.add(new ItemOption(55, 5));
                    itemqua.itemOptions.add(new ItemOption(56, 5));
                    itemqua.itemOptions.add(new ItemOption(57, 5));
                    itemqua.itemOptions.add(new ItemOption(58, 25));
                    itemqua.itemOptions.add(new ItemOption(61, 50));
                     itemqua.itemOptions.add(new ItemOption(107, 50));
                      itemqua.itemOptions.add(new ItemOption(72, 10));
                    itemqua.itemOptions.add(new ItemOption(30, 1));
                    
                    

                    itemqua1 = ItemService.gI().createNewItem((short) 1983, 1000);
                    itemqua1.itemOptions.add(new ItemOption(72, 10));
                    itemqua1.itemOptions.add(new ItemOption(30, 1));

                    itemqua2 = ItemService.gI().createNewItem((short) 2062, 1000);
                    itemqua2.itemOptions.add(new ItemOption(72, 10));
                    itemqua2.itemOptions.add(new ItemOption(30, 1));

                    itemqua3 = ItemService.gI().createNewItem((short) 2063, 100);
                    itemqua3.itemOptions.add(new ItemOption(72, 10));
                    itemqua3.itemOptions.add(new ItemOption(30, 0));

                    itemqua4 = ItemService.gI().createNewItem((short) 1502, 1000);
                    itemqua4.itemOptions.add(new ItemOption(30, 1));

                    Service.gI().sendThongBaoFromAdmin(player, "Bạn Đã Nhận Được 5 quà Mốc\n"
                            + "Cảm On Bạn Đã Ủng Hộ\n"
                            + "Không Hỗ Trợ Nhận Lai Mốc\n"
                            + "Max Mốc 350tr");
                    InventoryService.gI().addItemBag(player, itemqua, 999999);
                    InventoryService.gI().addItemBag(player, itemqua1, 999999);
                    InventoryService.gI().addItemBag(player, itemqua2, 999999);
                    InventoryService.gI().addItemBag(player, itemqua3, 999999);
                    InventoryService.gI().addItemBag(player, itemqua4, 999999);

                    InventoryService.gI().sendItemBags(player);

                } else if (player.getSession().tongnap >= 22000000 && player.mocnap == 20000000) {
                    Service.gI().sendThongBao(player, "Tiến Hành Nhận\nMốc Nạp  Triệu\nSau " + time + " Giây!");
                    while (time > 0) {
                        time--;
                        Thread.sleep(1000);
                        Service.gI().sendThongBao(player, "|7|" + time);
                    }
                    player.mocnap = 22000000;
                       itemqua = ItemService.gI().createNewItem((short) 1481, 1);
                    itemqua.itemOptions.add(new ItemOption(46, 0));
                    itemqua.itemOptions.add(new ItemOption(197, 0));
                    itemqua.itemOptions.add(new ItemOption(52, 1999));
                    itemqua.itemOptions.add(new ItemOption(53, 1999));
                    itemqua.itemOptions.add(new ItemOption(54, 1999));
                    itemqua.itemOptions.add(new ItemOption(55, 5));
                    itemqua.itemOptions.add(new ItemOption(56, 5));
                    itemqua.itemOptions.add(new ItemOption(57, 5));
                    itemqua.itemOptions.add(new ItemOption(58, 25));
                    itemqua.itemOptions.add(new ItemOption(61, 50));
                     itemqua.itemOptions.add(new ItemOption(107, 50));
                      itemqua.itemOptions.add(new ItemOption(72, 10));
                    itemqua.itemOptions.add(new ItemOption(30, 1));
                    
                    

                    itemqua1 = ItemService.gI().createNewItem((short) 1983, 1000);
                    itemqua1.itemOptions.add(new ItemOption(72, 10));
                    itemqua1.itemOptions.add(new ItemOption(30, 1));

                    itemqua2 = ItemService.gI().createNewItem((short) 2062, 1000);
                    itemqua2.itemOptions.add(new ItemOption(72, 10));
                    itemqua2.itemOptions.add(new ItemOption(30, 1));

                    itemqua3 = ItemService.gI().createNewItem((short) 2063, 100);
                    itemqua3.itemOptions.add(new ItemOption(72, 10));
                    itemqua3.itemOptions.add(new ItemOption(30, 0));

                    itemqua4 = ItemService.gI().createNewItem((short) 1502, 1000);
                    itemqua4.itemOptions.add(new ItemOption(30, 1));

                    Service.gI().sendThongBaoFromAdmin(player, "Bạn Đã Nhận Được 5 quà Mốc\n"
                            + "Cảm On Bạn Đã Ủng Hộ\n"
                            + "Không Hỗ Trợ Nhận Lai Mốc\n"
                            + "Max Mốc 350tr");
                    InventoryService.gI().addItemBag(player, itemqua, 999999);
                    InventoryService.gI().addItemBag(player, itemqua1, 999999);
                    InventoryService.gI().addItemBag(player, itemqua2, 999999);
                    InventoryService.gI().addItemBag(player, itemqua3, 999999);
                    InventoryService.gI().addItemBag(player, itemqua4, 999999);

                    InventoryService.gI().sendItemBags(player);


                } else if (player.getSession().tongnap >= 25000000 && player.mocnap == 22000000) {
                    Service.gI().sendThongBao(player, "Tiến Hành Nhận\nMốc Nạp  Triệu\nSau " + time + " Giây!");
                    while (time > 0) {
                        time--;
                        Thread.sleep(1000);
                        Service.gI().sendThongBao(player, "|7|" + time);
                    }
                    player.mocnap = 25000000;
                         itemqua = ItemService.gI().createNewItem((short) 1868, 1);
                    itemqua.itemOptions.add(new ItemOption(46, 0));
                    itemqua.itemOptions.add(new ItemOption(197, 0));
                    itemqua.itemOptions.add(new ItemOption(52, 1999));
                    itemqua.itemOptions.add(new ItemOption(53, 1999));
                    itemqua.itemOptions.add(new ItemOption(54, 1999));
                    itemqua.itemOptions.add(new ItemOption(55, 5));
                    itemqua.itemOptions.add(new ItemOption(56, 5));
                    itemqua.itemOptions.add(new ItemOption(57, 5));
                    itemqua.itemOptions.add(new ItemOption(58, 25));
                    itemqua.itemOptions.add(new ItemOption(61, 50));
                    itemqua.itemOptions.add(new ItemOption(107, 50));
                      itemqua.itemOptions.add(new ItemOption(72, 10));
                    itemqua.itemOptions.add(new ItemOption(30, 1));
                    
                    

                    itemqua1 = ItemService.gI().createNewItem((short) 1983, 1000);
                    itemqua1.itemOptions.add(new ItemOption(72, 10));
                    itemqua1.itemOptions.add(new ItemOption(30, 1));

                    itemqua2 = ItemService.gI().createNewItem((short) 2062, 1000);
                    itemqua2.itemOptions.add(new ItemOption(72, 10));
                    itemqua2.itemOptions.add(new ItemOption(30, 1));

                    itemqua3 = ItemService.gI().createNewItem((short) 2063, 100);
                    itemqua3.itemOptions.add(new ItemOption(72, 10));
                    itemqua3.itemOptions.add(new ItemOption(30, 0));

                    itemqua4 = ItemService.gI().createNewItem((short) 1502, 1000);
                    itemqua4.itemOptions.add(new ItemOption(30, 1));

                    Service.gI().sendThongBaoFromAdmin(player, "Bạn Đã Nhận Được 5 quà Mốc\n"
                            + "Cảm On Bạn Đã Ủng Hộ\n"
                            + "Không Hỗ Trợ Nhận Lai Mốc\n"
                            + "Max Mốc 350tr");
                    InventoryService.gI().addItemBag(player, itemqua, 999999);
                    InventoryService.gI().addItemBag(player, itemqua1, 999999);
                    InventoryService.gI().addItemBag(player, itemqua2, 999999);
                    InventoryService.gI().addItemBag(player, itemqua3, 999999);
                    InventoryService.gI().addItemBag(player, itemqua4, 999999);

                    InventoryService.gI().sendItemBags(player);

                } else if (player.getSession().tongnap >= 28000000 && player.mocnap == 25000000) {
                    Service.gI().sendThongBao(player, "Tiến Hành Nhận\nMốc Nạp  Triệu\nSau " + time + " Giây!");
                    while (time > 0) {
                        time--;
                        Thread.sleep(1000);
                        Service.gI().sendThongBao(player, "|7|" + time);
                    }
                    player.mocnap = 28000000;
                         itemqua = ItemService.gI().createNewItem((short) 1711, 1);
                    itemqua.itemOptions.add(new ItemOption(46, 0));
                    itemqua.itemOptions.add(new ItemOption(197, 0));
                    itemqua.itemOptions.add(new ItemOption(52, 1999));
                    itemqua.itemOptions.add(new ItemOption(53, 1999));
                    itemqua.itemOptions.add(new ItemOption(54, 1999));
                    itemqua.itemOptions.add(new ItemOption(55, 5));
                    itemqua.itemOptions.add(new ItemOption(56, 5));
                    itemqua.itemOptions.add(new ItemOption(57, 5));
                    itemqua.itemOptions.add(new ItemOption(58, 25));
                    itemqua.itemOptions.add(new ItemOption(61, 50));
                     itemqua.itemOptions.add(new ItemOption(107, 50));
                      itemqua.itemOptions.add(new ItemOption(72, 10));
                    itemqua.itemOptions.add(new ItemOption(30, 1));
                    
                    

                    itemqua1 = ItemService.gI().createNewItem((short) 1983, 1000);
                    itemqua1.itemOptions.add(new ItemOption(72, 10));
                    itemqua1.itemOptions.add(new ItemOption(30, 1));

                    itemqua2 = ItemService.gI().createNewItem((short) 2062, 1000);
                    itemqua2.itemOptions.add(new ItemOption(72, 10));
                    itemqua2.itemOptions.add(new ItemOption(30, 1));

                    itemqua3 = ItemService.gI().createNewItem((short) 2063, 100);
                    itemqua3.itemOptions.add(new ItemOption(72, 10));
                    itemqua3.itemOptions.add(new ItemOption(30, 0));

                    itemqua4 = ItemService.gI().createNewItem((short) 1502, 1000);
                    itemqua4.itemOptions.add(new ItemOption(30, 1));

                    Service.gI().sendThongBaoFromAdmin(player, "Bạn Đã Nhận Được 5 quà Mốc\n"
                            + "Cảm On Bạn Đã Ủng Hộ\n"
                            + "Không Hỗ Trợ Nhận Lai Mốc\n"
                            + "Max Mốc 350tr");
                    InventoryService.gI().addItemBag(player, itemqua, 999999);
                    InventoryService.gI().addItemBag(player, itemqua1, 999999);
                    InventoryService.gI().addItemBag(player, itemqua2, 999999);
                    InventoryService.gI().addItemBag(player, itemqua3, 999999);
                    InventoryService.gI().addItemBag(player, itemqua4, 999999);

                    InventoryService.gI().sendItemBags(player);


                } else if (player.getSession().tongnap >= 30000000 && player.mocnap == 28000000) {
                    Service.gI().sendThongBao(player, "Tiến Hành Nhận\nMốc Nạp  Triệu\nSau " + time + " Giây!");
                    while (time > 0) {
                        time--;
                        Thread.sleep(1000);
                        Service.gI().sendThongBao(player, "|7|" + time);
                    }
                    player.mocnap = 30000000;
                        itemqua = ItemService.gI().createNewItem((short) 1357, 1);
                    itemqua.itemOptions.add(new ItemOption(46, 0));
                    itemqua.itemOptions.add(new ItemOption(197, 0));
                    itemqua.itemOptions.add(new ItemOption(52, 1999));
                    itemqua.itemOptions.add(new ItemOption(53, 1999));
                    itemqua.itemOptions.add(new ItemOption(54, 1999));
                    itemqua.itemOptions.add(new ItemOption(55, 5));
                    itemqua.itemOptions.add(new ItemOption(56, 5));
                    itemqua.itemOptions.add(new ItemOption(57, 5));
                    itemqua.itemOptions.add(new ItemOption(58, 25));
                    itemqua.itemOptions.add(new ItemOption(61, 50));
                    itemqua.itemOptions.add(new ItemOption(107, 50));
                      itemqua.itemOptions.add(new ItemOption(72, 10));
                    itemqua.itemOptions.add(new ItemOption(30, 1));
                    
                    

                    itemqua1 = ItemService.gI().createNewItem((short) 1983, 1000);
                    itemqua1.itemOptions.add(new ItemOption(72, 10));
                    itemqua1.itemOptions.add(new ItemOption(30, 1));

                    itemqua2 = ItemService.gI().createNewItem((short) 2062, 1000);
                    itemqua2.itemOptions.add(new ItemOption(72, 10));
                    itemqua2.itemOptions.add(new ItemOption(30, 1));

                    itemqua3 = ItemService.gI().createNewItem((short) 2063, 100);
                    itemqua3.itemOptions.add(new ItemOption(72, 10));
                    itemqua3.itemOptions.add(new ItemOption(30, 0));

                    itemqua4 = ItemService.gI().createNewItem((short) 1502, 1000);
                    itemqua4.itemOptions.add(new ItemOption(30, 1));

                    Service.gI().sendThongBaoFromAdmin(player, "Bạn Đã Nhận Được 5 quà Mốc\n"
                            + "Cảm On Bạn Đã Ủng Hộ\n"
                            + "Không Hỗ Trợ Nhận Lai Mốc\n"
                            + "Max Mốc 350tr");
                    InventoryService.gI().addItemBag(player, itemqua, 999999);
                    InventoryService.gI().addItemBag(player, itemqua1, 999999);
                    InventoryService.gI().addItemBag(player, itemqua2, 999999);
                    InventoryService.gI().addItemBag(player, itemqua3, 999999);
                    InventoryService.gI().addItemBag(player, itemqua4, 999999);

                    InventoryService.gI().sendItemBags(player);

                } else if (player.getSession().tongnap >= 35000000 && player.mocnap == 30000000) {
                    Service.gI().sendThongBao(player, "Tiến Hành Nhận\nMốc Nạp  Triệu\nSau " + time + " Giây!");
                    while (time > 0) {
                        time--;
                        Thread.sleep(1000);
                        Service.gI().sendThongBao(player, "|7|" + time);
                    }
                    player.mocnap = 35000000;
                        itemqua = ItemService.gI().createNewItem((short) 1965, 1);
                    itemqua.itemOptions.add(new ItemOption(46, 0));
                    itemqua.itemOptions.add(new ItemOption(197, 0));
                    itemqua.itemOptions.add(new ItemOption(52, 1999));
                    itemqua.itemOptions.add(new ItemOption(53, 1999));
                    itemqua.itemOptions.add(new ItemOption(54, 1999));
                    itemqua.itemOptions.add(new ItemOption(55, 5));
                    itemqua.itemOptions.add(new ItemOption(56, 5));
                    itemqua.itemOptions.add(new ItemOption(57, 5));
                    itemqua.itemOptions.add(new ItemOption(58, 25));
                    itemqua.itemOptions.add(new ItemOption(61, 50));
                     itemqua.itemOptions.add(new ItemOption(107, 50));
                      itemqua.itemOptions.add(new ItemOption(72, 10));
                    itemqua.itemOptions.add(new ItemOption(30, 1));
                    
                    

                    itemqua1 = ItemService.gI().createNewItem((short) 1983, 1000);
                    itemqua1.itemOptions.add(new ItemOption(72, 10));
                    itemqua1.itemOptions.add(new ItemOption(30, 1));

                    itemqua2 = ItemService.gI().createNewItem((short) 2062, 1000);
                    itemqua2.itemOptions.add(new ItemOption(72, 10));
                    itemqua2.itemOptions.add(new ItemOption(30, 1));

                    itemqua3 = ItemService.gI().createNewItem((short) 2063, 100);
                    itemqua3.itemOptions.add(new ItemOption(72, 10));
                    itemqua3.itemOptions.add(new ItemOption(30, 0));

                    itemqua4 = ItemService.gI().createNewItem((short) 1502, 1000);
                    itemqua4.itemOptions.add(new ItemOption(30, 1));

                    Service.gI().sendThongBaoFromAdmin(player, "Bạn Đã Nhận Được 5 quà Mốc\n"
                            + "Cảm On Bạn Đã Ủng Hộ\n"
                            + "Không Hỗ Trợ Nhận Lai Mốc\n"
                            + "Max Mốc 350tr");
                    InventoryService.gI().addItemBag(player, itemqua, 999999);
                    InventoryService.gI().addItemBag(player, itemqua1, 999999);
                    InventoryService.gI().addItemBag(player, itemqua2, 999999);
                    InventoryService.gI().addItemBag(player, itemqua3, 999999);
                    InventoryService.gI().addItemBag(player, itemqua4, 999999);

                    InventoryService.gI().sendItemBags(player);

                   } else if (player.getSession().tongnap >= 40000000 && player.mocnap == 35000000) {
                    Service.gI().sendThongBao(player, "Tiến Hành Nhận\nMốc Nạp  Triệu\nSau " + time + " Giây!");
                    while (time > 0) {
                        time--;
                        Thread.sleep(1000);
                        Service.gI().sendThongBao(player, "|7|" + time);
                    }
                    player.mocnap = 40000000;
                        itemqua = ItemService.gI().createNewItem((short) 2046, 1);
                    itemqua.itemOptions.add(new ItemOption(46, 0));
                    itemqua.itemOptions.add(new ItemOption(197, 0));
                    itemqua.itemOptions.add(new ItemOption(52, 1999));
                    itemqua.itemOptions.add(new ItemOption(53, 1999));
                    itemqua.itemOptions.add(new ItemOption(54, 1999));
                    itemqua.itemOptions.add(new ItemOption(55, 5));
                    itemqua.itemOptions.add(new ItemOption(56, 5));
                    itemqua.itemOptions.add(new ItemOption(57, 5));
                    itemqua.itemOptions.add(new ItemOption(58, 25));
                    itemqua.itemOptions.add(new ItemOption(61, 50));
                    itemqua.itemOptions.add(new ItemOption(107, 50));
                      itemqua.itemOptions.add(new ItemOption(72, 10));
                    itemqua.itemOptions.add(new ItemOption(30, 1));
                    
                    

                    itemqua1 = ItemService.gI().createNewItem((short) 1983, 1000);
                    itemqua1.itemOptions.add(new ItemOption(72, 10));
                    itemqua1.itemOptions.add(new ItemOption(30, 1));

                    itemqua2 = ItemService.gI().createNewItem((short) 2062, 1000);
                    itemqua2.itemOptions.add(new ItemOption(72, 10));
                    itemqua2.itemOptions.add(new ItemOption(30, 1));

                    itemqua3 = ItemService.gI().createNewItem((short) 2063, 100);
                    itemqua3.itemOptions.add(new ItemOption(72, 10));
                    itemqua3.itemOptions.add(new ItemOption(30, 0));

                    itemqua4 = ItemService.gI().createNewItem((short) 1502, 1000);
                    itemqua4.itemOptions.add(new ItemOption(30, 1));

                    Service.gI().sendThongBaoFromAdmin(player, "Bạn Đã Nhận Được 5 quà Mốc\n"
                            + "Cảm On Bạn Đã Ủng Hộ\n"
                            + "Không Hỗ Trợ Nhận Lai Mốc\n"
                            + "Max Mốc 350tr");
                    InventoryService.gI().addItemBag(player, itemqua, 999999);
                    InventoryService.gI().addItemBag(player, itemqua1, 999999);
                    InventoryService.gI().addItemBag(player, itemqua2, 999999);
                    InventoryService.gI().addItemBag(player, itemqua3, 999999);
                    InventoryService.gI().addItemBag(player, itemqua4, 999999);

                    InventoryService.gI().sendItemBags(player);   
                    
                       } else if (player.getSession().tongnap >= 50000000 && player.mocnap == 40000000) {
                    Service.gI().sendThongBao(player, "Tiến Hành Nhận\nMốc Nạp  Triệu\nSau " + time + " Giây!");
                    while (time > 0) {
                        time--;
                        Thread.sleep(1000);
                        Service.gI().sendThongBao(player, "|7|" + time);
                    }
                    player.mocnap = 50000000;
                        itemqua = ItemService.gI().createNewItem((short) 2052, 1);
                    itemqua.itemOptions.add(new ItemOption(46, 0));
                    itemqua.itemOptions.add(new ItemOption(197, 0));
                    itemqua.itemOptions.add(new ItemOption(52, 1999));
                    itemqua.itemOptions.add(new ItemOption(53, 1999));
                    itemqua.itemOptions.add(new ItemOption(54, 1999));
                    itemqua.itemOptions.add(new ItemOption(55, 5));
                    itemqua.itemOptions.add(new ItemOption(56, 5));
                    itemqua.itemOptions.add(new ItemOption(57, 5));
                    itemqua.itemOptions.add(new ItemOption(58, 25));
                    itemqua.itemOptions.add(new ItemOption(61, 50));
                     itemqua.itemOptions.add(new ItemOption(107, 50));
                      itemqua.itemOptions.add(new ItemOption(72, 10));
                    itemqua.itemOptions.add(new ItemOption(30, 1));
                    
                    

                    itemqua1 = ItemService.gI().createNewItem((short) 1983, 1000);
                    itemqua1.itemOptions.add(new ItemOption(72, 10));
                    itemqua1.itemOptions.add(new ItemOption(30, 1));

                    itemqua2 = ItemService.gI().createNewItem((short) 2062, 1000);
                    itemqua2.itemOptions.add(new ItemOption(72, 10));
                    itemqua2.itemOptions.add(new ItemOption(30, 1));

                    itemqua3 = ItemService.gI().createNewItem((short) 2063, 100);
                    itemqua3.itemOptions.add(new ItemOption(72, 10));
                    itemqua3.itemOptions.add(new ItemOption(30, 0));

                    itemqua4 = ItemService.gI().createNewItem((short) 1502, 1000);
                    itemqua4.itemOptions.add(new ItemOption(30, 1));

                    Service.gI().sendThongBaoFromAdmin(player, "Bạn Đã Nhận Được 5 quà Mốc\n"
                            + "Cảm On Bạn Đã Ủng Hộ\n"
                            + "Không Hỗ Trợ Nhận Lai Mốc\n"
                            + "Max Mốc 350tr");
                    InventoryService.gI().addItemBag(player, itemqua, 999999);
                    InventoryService.gI().addItemBag(player, itemqua1, 999999);
                    InventoryService.gI().addItemBag(player, itemqua2, 999999);
                    InventoryService.gI().addItemBag(player, itemqua3, 999999);
                    InventoryService.gI().addItemBag(player, itemqua4, 999999);

                    InventoryService.gI().sendItemBags(player);    
                    
                    

                } else {
                    Service.gI().sendThongBao(player, "Bạn Chưa Đủ Điều Kiện Nhận Mốc Nạp Này!");
                }
            } catch (Exception e) {
            }
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 6 ô trống hành trang");
        }
    }

    public void ComfirmMocPass(Player player) {
        if (InventoryService.gI().getCountEmptyBag(player) > 5) {
            Item itemqua;
            Item itemqua1;
            Item itemqua2;
            Item itemqua3;
            Item itemqua4;
            Item itemqua5;
            try {
                int time = 5;
                if (player.cfpass >= 5000 && player.mocpass == 0) { // cfpass thay cho tổng nap // mocpass là khi nhận mốc 1 sẽ cộng aã nhận
                    Service.gI().sendThongBao(player, "Bắt Đầu\n Nhận Mốc Pass Tiếp Theo\nSau " + time + " Giây!");
                    while (time > 0) {
                        time--;
                        Thread.sleep(1000);
                        Service.gI().sendThongBao(player, "|8|" + time);
                    }
                    player.mocpass = 5000;
                    itemqua = ItemService.gI().createNewItem((short) 2008, 10);
                    itemqua.itemOptions.add(new ItemOption(50, 100));
                    itemqua.itemOptions.add(new ItemOption(30, 1));

                    itemqua1 = ItemService.gI().createNewItem((short) 2009, 10);
                    itemqua1.itemOptions.add(new ItemOption(77, 100));
                    itemqua1.itemOptions.add(new ItemOption(30, 1));

                    itemqua2 = ItemService.gI().createNewItem((short) 2010, 10);
                    itemqua2.itemOptions.add(new ItemOption(103, 100));
                    itemqua2.itemOptions.add(new ItemOption(30, 1));

                    itemqua3 = ItemService.gI().createNewItem((short) 2011, 10);
                    itemqua3.itemOptions.add(new ItemOption(101, 100));
                    itemqua3.itemOptions.add(new ItemOption(30, 500));

                    itemqua4 = ItemService.gI().createNewItem((short) 1502, 500);
                    itemqua4.itemOptions.add(new ItemOption(30, 1));

                    Service.gI().sendThongBaoFromAdmin(player, "Bạn Đã Nhận Được 10 Đá Thần Linh SD,HP,KI,TNSM,ĐÁ NỘITẠI\n"
                            + "Vui Lòng Dùng Hết Đồ Mốc Thấp \nof Sao Pha Lê Hiện Có,Trong Hành Trang Mới Được Nhận Mốc Tiếp Theo\n"
                            + "Nếu Cố Tình Sẽ Lập Tức Mất Mốc Này Và Không Nhận Thêm Được Gì\n"
                            + "Không Hỗ Trợ Nhận Lai Mốc , Mỗi Mùa Được Cày Lại\n"
                            + "Max Pass 500 Triệu Điểm");
                    InventoryService.gI().addItemBag(player, itemqua, 10);
                    InventoryService.gI().addItemBag(player, itemqua1, 10);
                    InventoryService.gI().addItemBag(player, itemqua2, 10);
                    InventoryService.gI().addItemBag(player, itemqua3, 10);
                    InventoryService.gI().addItemBag(player, itemqua4, 500);

                    InventoryService.gI().sendItemBags(player);
                } else if (player.cfpass >= 12000 && player.mocpass == 5000) {
                    Service.gI().sendThongBao(player, "Bắt Đầu\n Nhận Mốc Pass Tiếp Theo\nSau " + time + " Giây!");
                    while (time > 0) {
                        time--;
                        Thread.sleep(1000);
                        Service.gI().sendThongBao(player, "|8|" + time);
                    }
                    player.mocpass = 12000;
                    itemqua = ItemService.gI().createNewItem((short) 2008, 10);
                    itemqua.itemOptions.add(new ItemOption(50, 150));
                    itemqua.itemOptions.add(new ItemOption(30, 1));

                    itemqua1 = ItemService.gI().createNewItem((short) 2009, 10);
                    itemqua1.itemOptions.add(new ItemOption(77, 150));
                    itemqua1.itemOptions.add(new ItemOption(30, 1));

                    itemqua2 = ItemService.gI().createNewItem((short) 2010, 10);
                    itemqua2.itemOptions.add(new ItemOption(103, 150));
                    itemqua2.itemOptions.add(new ItemOption(30, 1));

                    itemqua3 = ItemService.gI().createNewItem((short) 2011, 10);
                    itemqua3.itemOptions.add(new ItemOption(101, 150));
                    itemqua3.itemOptions.add(new ItemOption(30, 500));

                    itemqua4 = ItemService.gI().createNewItem((short) 1502, 500);
                    itemqua4.itemOptions.add(new ItemOption(30, 1));

                    Service.gI().sendThongBaoFromAdmin(player, "Bạn Đã Nhận Được 10 Đá Thần Linh SD,HP,KI,TNSM,ĐÁ NỘITẠI\n"
                            + "Vui Lòng Dùng Hết Đồ Mốc Thấp \nof Sao Pha Lê Hiện Có,Trong Hành Trang Mới Được Nhận Mốc Tiếp Theo\n"
                            + "Nếu Cố Tình Sẽ Lập Tức Mất Mốc Này Và Không Nhận Thêm Được Gì\n"
                            + "Không Hỗ Trợ Nhận Lai Mốc , Mỗi Mùa Được Cày Lại\n"
                            + "Max Pass 500 Triệu Điểm");
                    InventoryService.gI().addItemBag(player, itemqua, 10);
                    InventoryService.gI().addItemBag(player, itemqua1, 10);
                    InventoryService.gI().addItemBag(player, itemqua2, 10);
                    InventoryService.gI().addItemBag(player, itemqua3, 10);
                    InventoryService.gI().addItemBag(player, itemqua4, 500);
                    InventoryService.gI().sendItemBags(player);
                } else if (player.cfpass >= 18000 && player.mocpass == 12000) {
                    Service.gI().sendThongBao(player, "Bắt Đầu\n Nhận Mốc Pass Tiếp Theo\nSau " + time + " Giây!");
                    while (time > 0) {
                        time--;
                        Thread.sleep(1000);
                        Service.gI().sendThongBao(player, "|7|" + time);
                    }
                    player.mocpass = 18000;
                    itemqua = ItemService.gI().createNewItem((short) 2008, 10);
                    itemqua.itemOptions.add(new ItemOption(50, 200));
                    itemqua.itemOptions.add(new ItemOption(30, 1));

                    itemqua1 = ItemService.gI().createNewItem((short) 2009, 10);
                    itemqua1.itemOptions.add(new ItemOption(77, 200));
                    itemqua1.itemOptions.add(new ItemOption(30, 1));

                    itemqua2 = ItemService.gI().createNewItem((short) 2010, 10);
                    itemqua2.itemOptions.add(new ItemOption(103, 200));
                    itemqua2.itemOptions.add(new ItemOption(30, 1));

                    itemqua3 = ItemService.gI().createNewItem((short) 2011, 10);
                    itemqua3.itemOptions.add(new ItemOption(101, 200));
                    itemqua3.itemOptions.add(new ItemOption(30, 500));

                    itemqua4 = ItemService.gI().createNewItem((short) 1502, 500);
                    itemqua4.itemOptions.add(new ItemOption(30, 1));

                    Service.gI().sendThongBaoFromAdmin(player, "Bạn Đã Nhận Được 10 Đá Thần Linh SD,HP,KI,TNSM,ĐÁ NỘITẠI\n"
                            + "Vui Lòng Dùng Hết Đồ Mốc Thấp \nof Sao Pha Lê Hiện Có,Trong Hành Trang Mới Được Nhận Mốc Tiếp Theo\n"
                            + "Nếu Cố Tình Sẽ Lập Tức Mất Mốc Này Và Không Nhận Thêm Được Gì\n"
                            + "Không Hỗ Trợ Nhận Lai Mốc , Mỗi Mùa Được Cày Lại\n"
                            + "Max Pass 500 Triệu Điểm");
                    InventoryService.gI().addItemBag(player, itemqua, 10);
                    InventoryService.gI().addItemBag(player, itemqua1, 10);
                    InventoryService.gI().addItemBag(player, itemqua2, 10);
                    InventoryService.gI().addItemBag(player, itemqua3, 10);
                    InventoryService.gI().addItemBag(player, itemqua4, 500);
                    InventoryService.gI().sendItemBags(player);
                } else if (player.cfpass >= 35000 && player.mocpass == 18000) {
                    Service.gI().sendThongBao(player, "Bắt Đầu\n Nhận Mốc Pass Tiếp Theo\nSau " + time + " Giây!");
                    while (time > 0) {
                        time--;
                        Thread.sleep(1000);
                        Service.gI().sendThongBao(player, "|7|" + time);
                    }
                    player.mocpass = 35000;// mocpass
                    itemqua = ItemService.gI().createNewItem((short) 2008, 10);
                    itemqua.itemOptions.add(new ItemOption(50, 250));
                    itemqua.itemOptions.add(new ItemOption(30, 1));

                    itemqua1 = ItemService.gI().createNewItem((short) 2009, 10);
                    itemqua1.itemOptions.add(new ItemOption(77, 250));
                    itemqua1.itemOptions.add(new ItemOption(30, 1));

                    itemqua2 = ItemService.gI().createNewItem((short) 2010, 10);
                    itemqua2.itemOptions.add(new ItemOption(103, 250));
                    itemqua2.itemOptions.add(new ItemOption(30, 1));

                    itemqua3 = ItemService.gI().createNewItem((short) 2011, 10);
                    itemqua3.itemOptions.add(new ItemOption(101, 250));
                    itemqua3.itemOptions.add(new ItemOption(30, 500));

                    itemqua4 = ItemService.gI().createNewItem((short) 1502, 500);
                    itemqua4.itemOptions.add(new ItemOption(30, 1));

                    Service.gI().sendThongBaoFromAdmin(player, "Bạn Đã Nhận Được 10 Đá Thần Linh SD,HP,KI,TNSM,ĐÁ NỘITẠI\n"
                            + "Vui Lòng Dùng Hết Đồ Mốc Thấp \nof Sao Pha Lê Hiện Có,Trong Hành Trang Mới Được Nhận Mốc Tiếp Theo\n"
                            + "Nếu Cố Tình Sẽ Lập Tức Mất Mốc Này Và Không Nhận Thêm Được Gì\n"
                            + "Không Hỗ Trợ Nhận Lai Mốc , Mỗi Mùa Được Cày Lại\n"
                            + "Max Pass 500 Triệu Điểm");
                    InventoryService.gI().addItemBag(player, itemqua, 10);
                    InventoryService.gI().addItemBag(player, itemqua1, 10);
                    InventoryService.gI().addItemBag(player, itemqua2, 10);
                    InventoryService.gI().addItemBag(player, itemqua3, 10);
                    InventoryService.gI().addItemBag(player, itemqua4, 500);
                    InventoryService.gI().sendItemBags(player);
                } else if (player.cfpass >= 75000 && player.mocpass == 35000) {
                    Service.gI().sendThongBao(player, "Bắt Đầu\n Nhận Mốc Pass Tiếp Theo\nSau " + time + " Giây!");
                    while (time > 0) {
                        time--;
                        Thread.sleep(1000);
                        Service.gI().sendThongBao(player, "|7|" + time);
                    }
                    player.mocpass = 75000;
                    itemqua = ItemService.gI().createNewItem((short) 2008, 10);
                    itemqua.itemOptions.add(new ItemOption(50, 300));
                    itemqua.itemOptions.add(new ItemOption(30, 1));

                    itemqua1 = ItemService.gI().createNewItem((short) 2009, 10);
                    itemqua1.itemOptions.add(new ItemOption(77, 300));
                    itemqua1.itemOptions.add(new ItemOption(30, 1));

                    itemqua2 = ItemService.gI().createNewItem((short) 2010, 10);
                    itemqua2.itemOptions.add(new ItemOption(103, 300));
                    itemqua2.itemOptions.add(new ItemOption(30, 1));

                    itemqua3 = ItemService.gI().createNewItem((short) 2011, 10);
                    itemqua3.itemOptions.add(new ItemOption(101, 300));
                    itemqua3.itemOptions.add(new ItemOption(30, 500));

                    itemqua4 = ItemService.gI().createNewItem((short) 1502, 500);
                    itemqua4.itemOptions.add(new ItemOption(30, 1));

                    Service.gI().sendThongBaoFromAdmin(player, "Bạn Đã Nhận Được 10 Đá Thần Linh SD,HP,KI,TNSM,ĐÁ NỘITẠI\n"
                            + "Vui Lòng Dùng Hết Đồ Mốc Thấp \nof Sao Pha Lê Hiện Có,Trong Hành Trang Mới Được Nhận Mốc Tiếp Theo\n"
                            + "Nếu Cố Tình Sẽ Lập Tức Mất Mốc Này Và Không Nhận Thêm Được Gì\n"
                            + "Không Hỗ Trợ Nhận Lai Mốc , Mỗi Mùa Được Cày Lại\n"
                            + "Max Pass 500 Triệu Điểm");
                    InventoryService.gI().addItemBag(player, itemqua, 10);
                    InventoryService.gI().addItemBag(player, itemqua1, 10);
                    InventoryService.gI().addItemBag(player, itemqua2, 10);
                    InventoryService.gI().addItemBag(player, itemqua3, 10);
                    InventoryService.gI().addItemBag(player, itemqua4, 500);
                    InventoryService.gI().sendItemBags(player);
                } else if (player.cfpass >= 205000 && player.mocpass == 75000) {
                    Service.gI().sendThongBao(player, "Bắt Đầu\n Nhận Mốc Pass Tiếp Theo\nSau " + time + " Giây!");
                    while (time > 0) {
                        time--;
                        Thread.sleep(1000);
                        Service.gI().sendThongBao(player, "|7|" + time);
                    }
                    player.mocpass = 205000;
                    itemqua = ItemService.gI().createNewItem((short) 2008, 10);
                    itemqua.itemOptions.add(new ItemOption(50, 350));
                    itemqua.itemOptions.add(new ItemOption(30, 1));

                    itemqua1 = ItemService.gI().createNewItem((short) 2009, 10);
                    itemqua1.itemOptions.add(new ItemOption(77, 350));
                    itemqua1.itemOptions.add(new ItemOption(30, 1));

                    itemqua2 = ItemService.gI().createNewItem((short) 2010, 10);
                    itemqua2.itemOptions.add(new ItemOption(103, 350));
                    itemqua2.itemOptions.add(new ItemOption(30, 1));

                    itemqua3 = ItemService.gI().createNewItem((short) 2011, 10);
                    itemqua3.itemOptions.add(new ItemOption(101, 350));
                    itemqua3.itemOptions.add(new ItemOption(30, 500));

                    itemqua4 = ItemService.gI().createNewItem((short) 1502, 500);
                    itemqua4.itemOptions.add(new ItemOption(30, 1));

                    Service.gI().sendThongBaoFromAdmin(player, "Bạn Đã Nhận Được 10 Đá Thần Linh SD,HP,KI,TNSM,ĐÁ NỘITẠI\n"
                            + "Vui Lòng Dùng Hết Đồ Mốc Thấp \nof Sao Pha Lê Hiện Có,Trong Hành Trang Mới Được Nhận Mốc Tiếp Theo\n"
                            + "Nếu Cố Tình Sẽ Lập Tức Mất Mốc Này Và Không Nhận Thêm Được Gì\n"
                            + "Không Hỗ Trợ Nhận Lai Mốc , Mỗi Mùa Được Cày Lại\n"
                            + "Max Pass 500 Triệu Điểm");
                    InventoryService.gI().addItemBag(player, itemqua, 10);
                    InventoryService.gI().addItemBag(player, itemqua1, 10);
                    InventoryService.gI().addItemBag(player, itemqua2, 10);
                    InventoryService.gI().addItemBag(player, itemqua3, 10);
                    InventoryService.gI().addItemBag(player, itemqua4, 500);
                    InventoryService.gI().sendItemBags(player);
                } else if (player.cfpass >= 300000 && player.mocpass == 205000) {
                    Service.gI().sendThongBao(player, "Bắt Đầu\n Nhận Mốc Pass Tiếp Theo\nSau " + time + " Giây!");
                    while (time > 0) {
                        time--;
                        Thread.sleep(1000);
                        Service.gI().sendThongBao(player, "|7|" + time);
                    }
                    player.mocpass = 300000;
                    itemqua = ItemService.gI().createNewItem((short) 2008, 10);
                    itemqua.itemOptions.add(new ItemOption(50, 400));
                    itemqua.itemOptions.add(new ItemOption(30, 1));

                    itemqua1 = ItemService.gI().createNewItem((short) 2009, 10);
                    itemqua1.itemOptions.add(new ItemOption(77, 400));
                    itemqua1.itemOptions.add(new ItemOption(30, 1));

                    itemqua2 = ItemService.gI().createNewItem((short) 2010, 10);
                    itemqua2.itemOptions.add(new ItemOption(103, 400));
                    itemqua2.itemOptions.add(new ItemOption(30, 1));

                    itemqua3 = ItemService.gI().createNewItem((short) 2011, 10);
                    itemqua3.itemOptions.add(new ItemOption(101, 400));
                    itemqua3.itemOptions.add(new ItemOption(30, 500));

                    itemqua4 = ItemService.gI().createNewItem((short) 1502, 500);
                    itemqua4.itemOptions.add(new ItemOption(30, 1));

                    Service.gI().sendThongBaoFromAdmin(player, "Bạn Đã Nhận Được 10 Đá Thần Linh SD,HP,KI,TNSM,ĐÁ NỘITẠI\n"
                            + "Vui Lòng Dùng Hết Đồ Mốc Thấp \nof Sao Pha Lê Hiện Có,Trong Hành Trang Mới Được Nhận Mốc Tiếp Theo\n"
                            + "Nếu Cố Tình Sẽ Lập Tức Mất Mốc Này Và Không Nhận Thêm Được Gì\n"
                            + "Không Hỗ Trợ Nhận Lai Mốc , Mỗi Mùa Được Cày Lại\n"
                            + "Max Pass 500 Triệu Điểm");
                    InventoryService.gI().addItemBag(player, itemqua, 10);
                    InventoryService.gI().addItemBag(player, itemqua1, 10);
                    InventoryService.gI().addItemBag(player, itemqua2, 10);
                    InventoryService.gI().addItemBag(player, itemqua3, 10);
                    InventoryService.gI().addItemBag(player, itemqua4, 500);
                    InventoryService.gI().sendItemBags(player);
                } else if (player.cfpass >= 500000 && player.mocpass == 300000) {
                    Service.gI().sendThongBao(player, "Bắt Đầu\n Nhận Mốc Pass Tiếp Theo\nSau " + time + " Giây!");
                    while (time > 0) {
                        time--;
                        Thread.sleep(1000);
                        Service.gI().sendThongBao(player, "|7|" + time);
                    }
                    player.mocpass = 500000;
                    itemqua = ItemService.gI().createNewItem((short) 2008, 10);
                    itemqua.itemOptions.add(new ItemOption(50, 450));
                    itemqua.itemOptions.add(new ItemOption(30, 1));

                    itemqua1 = ItemService.gI().createNewItem((short) 2009, 10);
                    itemqua1.itemOptions.add(new ItemOption(77, 450));
                    itemqua1.itemOptions.add(new ItemOption(30, 1));

                    itemqua2 = ItemService.gI().createNewItem((short) 2010, 10);
                    itemqua2.itemOptions.add(new ItemOption(103, 450));
                    itemqua2.itemOptions.add(new ItemOption(30, 1));

                    itemqua3 = ItemService.gI().createNewItem((short) 2011, 10);
                    itemqua3.itemOptions.add(new ItemOption(101, 450));
                    itemqua3.itemOptions.add(new ItemOption(30, 500));

                    itemqua4 = ItemService.gI().createNewItem((short) 1502, 500);
                    itemqua4.itemOptions.add(new ItemOption(30, 1));

                    Service.gI().sendThongBaoFromAdmin(player, "Bạn Đã Nhận Được 10 Đá Thần Linh SD,HP,KI,TNSM,ĐÁ NỘITẠI\n"
                            + "Vui Lòng Dùng Hết Đồ Mốc Thấp \nof Sao Pha Lê Hiện Có,Trong Hành Trang Mới Được Nhận Mốc Tiếp Theo\n"
                            + "Nếu Cố Tình Sẽ Lập Tức Mất Mốc Này Và Không Nhận Thêm Được Gì\n"
                            + "Không Hỗ Trợ Nhận Lai Mốc , Mỗi Mùa Được Cày Lại\n"
                            + "Max Pass 500 Triệu Điểm");
                    InventoryService.gI().addItemBag(player, itemqua, 10);
                    InventoryService.gI().addItemBag(player, itemqua1, 10);
                    InventoryService.gI().addItemBag(player, itemqua2, 10);
                    InventoryService.gI().addItemBag(player, itemqua3, 10);
                    InventoryService.gI().addItemBag(player, itemqua4, 500);
                    InventoryService.gI().sendItemBags(player);
                } else if (player.cfpass >= 1000000 && player.mocpass == 500000) {
                    Service.gI().sendThongBao(player, "Bắt Đầu\n Nhận Mốc Pass Tiếp Theo\nSau " + time + " Giây!");
                    while (time > 0) {
                        time--;
                        Thread.sleep(1000);
                        Service.gI().sendThongBao(player, "|7|" + time);
                    }
                    player.mocpass = 1000000;
                    itemqua = ItemService.gI().createNewItem((short) 2008, 10);
                    itemqua.itemOptions.add(new ItemOption(50, 500));
                    itemqua.itemOptions.add(new ItemOption(30, 1));

                    itemqua1 = ItemService.gI().createNewItem((short) 2009, 10);
                    itemqua1.itemOptions.add(new ItemOption(77, 500));
                    itemqua1.itemOptions.add(new ItemOption(30, 1));

                    itemqua2 = ItemService.gI().createNewItem((short) 2010, 10);
                    itemqua2.itemOptions.add(new ItemOption(103, 500));
                    itemqua2.itemOptions.add(new ItemOption(30, 1));

                    itemqua3 = ItemService.gI().createNewItem((short) 2011, 10);
                    itemqua3.itemOptions.add(new ItemOption(101, 500));
                    itemqua3.itemOptions.add(new ItemOption(30, 500));

                    itemqua4 = ItemService.gI().createNewItem((short) 1502, 500);
                    itemqua4.itemOptions.add(new ItemOption(30, 1));

                    Service.gI().sendThongBaoFromAdmin(player, "Bạn Đã Nhận Được 10 Đá Thần Linh SD,HP,KI,TNSM,ĐÁ NỘITẠI\n"
                            + "Vui Lòng Dùng Hết Đồ Mốc Thấp \nof Sao Pha Lê Hiện Có,Trong Hành Trang Mới Được Nhận Mốc Tiếp Theo\n"
                            + "Nếu Cố Tình Sẽ Lập Tức Mất Mốc Này Và Không Nhận Thêm Được Gì\n"
                            + "Không Hỗ Trợ Nhận Lai Mốc , Mỗi Mùa Được Cày Lại\n"
                            + "Max Pass 500 Triệu Điểm");
                    InventoryService.gI().addItemBag(player, itemqua, 10);
                    InventoryService.gI().addItemBag(player, itemqua1, 10);
                    InventoryService.gI().addItemBag(player, itemqua2, 10);
                    InventoryService.gI().addItemBag(player, itemqua3, 10);
                    InventoryService.gI().addItemBag(player, itemqua4, 500);
                    InventoryService.gI().sendItemBags(player);
                } else if (player.cfpass >= 1500000 && player.mocpass == 1000000) {
                    Service.gI().sendThongBao(player, "Bắt Đầu\n Nhận Mốc Pass Tiếp Theo\nSau " + time + " Giây!");
                    while (time > 0) {
                        time--;
                        Thread.sleep(1000);
                        Service.gI().sendThongBao(player, "|7|" + time);
                    }
                    player.mocpass = 1500000;
                    itemqua = ItemService.gI().createNewItem((short) 2008, 10);
                    itemqua.itemOptions.add(new ItemOption(50, 550));
                    itemqua.itemOptions.add(new ItemOption(30, 1));

                    itemqua1 = ItemService.gI().createNewItem((short) 2009, 10);
                    itemqua1.itemOptions.add(new ItemOption(77, 550));
                    itemqua1.itemOptions.add(new ItemOption(30, 1));

                    itemqua2 = ItemService.gI().createNewItem((short) 2010, 10);
                    itemqua2.itemOptions.add(new ItemOption(103, 550));
                    itemqua2.itemOptions.add(new ItemOption(30, 1));

                    itemqua3 = ItemService.gI().createNewItem((short) 2011, 10);
                    itemqua3.itemOptions.add(new ItemOption(101, 550));
                    itemqua3.itemOptions.add(new ItemOption(30, 500));

                    itemqua4 = ItemService.gI().createNewItem((short) 1502, 500);
                    itemqua4.itemOptions.add(new ItemOption(30, 1));

                    Service.gI().sendThongBaoFromAdmin(player, "Bạn Đã Nhận Được 10 Đá Thần Linh SD,HP,KI,TNSM,ĐÁ NỘITẠI\n"
                            + "Vui Lòng Dùng Hết Đồ Mốc Thấp \nof Sao Pha Lê Hiện Có,Trong Hành Trang Mới Được Nhận Mốc Tiếp Theo\n"
                            + "Nếu Cố Tình Sẽ Lập Tức Mất Mốc Này Và Không Nhận Thêm Được Gì\n"
                            + "Không Hỗ Trợ Nhận Lai Mốc , Mỗi Mùa Được Cày Lại\n"
                            + "Max Pass 500 Triệu Điểm");
                    InventoryService.gI().addItemBag(player, itemqua, 10);
                    InventoryService.gI().addItemBag(player, itemqua1, 10);
                    InventoryService.gI().addItemBag(player, itemqua2, 10);
                    InventoryService.gI().addItemBag(player, itemqua3, 10);
                    InventoryService.gI().addItemBag(player, itemqua4, 500);
                    InventoryService.gI().sendItemBags(player);
                } else if (player.cfpass >= 2000000 && player.mocpass == 1500000) {
                    Service.gI().sendThongBao(player, "Bắt Đầu\n Nhận Mốc Pass Tiếp Theo\nSau " + time + " Giây!");
                    while (time > 0) {
                        time--;
                        Thread.sleep(1000);
                        Service.gI().sendThongBao(player, "|7|" + time);
                    }
                    player.mocpass = 2000000;
                    itemqua = ItemService.gI().createNewItem((short) 2008, 10);
                    itemqua.itemOptions.add(new ItemOption(50, 600));
                    itemqua.itemOptions.add(new ItemOption(30, 1));

                    itemqua1 = ItemService.gI().createNewItem((short) 2009, 10);
                    itemqua1.itemOptions.add(new ItemOption(77, 600));
                    itemqua1.itemOptions.add(new ItemOption(30, 1));

                    itemqua2 = ItemService.gI().createNewItem((short) 2010, 10);
                    itemqua2.itemOptions.add(new ItemOption(103, 600));
                    itemqua2.itemOptions.add(new ItemOption(30, 1));

                    itemqua3 = ItemService.gI().createNewItem((short) 2011, 10);
                    itemqua3.itemOptions.add(new ItemOption(101, 600));
                    itemqua3.itemOptions.add(new ItemOption(30, 500));

                    itemqua4 = ItemService.gI().createNewItem((short) 1502, 500);
                    itemqua4.itemOptions.add(new ItemOption(30, 1));

                    Service.gI().sendThongBaoFromAdmin(player, "Bạn Đã Nhận Được 10 Đá Thần Linh SD,HP,KI,TNSM,ĐÁ NỘITẠI\n"
                            + "Vui Lòng Dùng Hết Đồ Mốc Thấp \nof Sao Pha Lê Hiện Có,Trong Hành Trang Mới Được Nhận Mốc Tiếp Theo\n"
                            + "Nếu Cố Tình Sẽ Lập Tức Mất Mốc Này Và Không Nhận Thêm Được Gì\n"
                            + "Không Hỗ Trợ Nhận Lai Mốc , Mỗi Mùa Được Cày Lại\n"
                            + "Max Pass 500 Triệu Điểm");
                    InventoryService.gI().addItemBag(player, itemqua, 10);
                    InventoryService.gI().addItemBag(player, itemqua1, 10);
                    InventoryService.gI().addItemBag(player, itemqua2, 10);
                    InventoryService.gI().addItemBag(player, itemqua3, 10);
                    InventoryService.gI().addItemBag(player, itemqua4, 500);
                    InventoryService.gI().sendItemBags(player);
                } else if (player.cfpass >= 2500000 && player.mocpass == 2000000) {
                    Service.gI().sendThongBao(player, "Bắt Đầu\n Nhận Mốc Pass Tiếp Theo\nSau " + time + " Giây!");
                    while (time > 0) {
                        time--;
                        Thread.sleep(1000);
                        Service.gI().sendThongBao(player, "|7|" + time);
                    }
                    player.mocpass = 2500000;
                    itemqua = ItemService.gI().createNewItem((short) 2008, 10);
                    itemqua.itemOptions.add(new ItemOption(50, 650));
                    itemqua.itemOptions.add(new ItemOption(30, 1));

                    itemqua1 = ItemService.gI().createNewItem((short) 2009, 10);
                    itemqua1.itemOptions.add(new ItemOption(77, 650));
                    itemqua1.itemOptions.add(new ItemOption(30, 1));

                    itemqua2 = ItemService.gI().createNewItem((short) 2010, 10);
                    itemqua2.itemOptions.add(new ItemOption(103, 650));
                    itemqua2.itemOptions.add(new ItemOption(30, 1));

                    itemqua3 = ItemService.gI().createNewItem((short) 2011, 10);
                    itemqua3.itemOptions.add(new ItemOption(101, 650));
                    itemqua3.itemOptions.add(new ItemOption(30, 500));

                    itemqua4 = ItemService.gI().createNewItem((short) 1502, 500);
                    itemqua4.itemOptions.add(new ItemOption(30, 1));

                    Service.gI().sendThongBaoFromAdmin(player, "Bạn Đã Nhận Được 10 Đá Thần Linh SD,HP,KI,TNSM,ĐÁ NỘITẠI\n"
                            + "Vui Lòng Dùng Hết Đồ Mốc Thấp \nof Sao Pha Lê Hiện Có,Trong Hành Trang Mới Được Nhận Mốc Tiếp Theo\n"
                            + "Nếu Cố Tình Sẽ Lập Tức Mất Mốc Này Và Không Nhận Thêm Được Gì\n"
                            + "Không Hỗ Trợ Nhận Lai Mốc , Mỗi Mùa Được Cày Lại\n"
                            + "Max Pass 500 Triệu Điểm");
                    InventoryService.gI().addItemBag(player, itemqua, 10);
                    InventoryService.gI().addItemBag(player, itemqua1, 10);
                    InventoryService.gI().addItemBag(player, itemqua2, 10);
                    InventoryService.gI().addItemBag(player, itemqua3, 10);
                    InventoryService.gI().addItemBag(player, itemqua4, 500);
                    InventoryService.gI().sendItemBags(player);
                } else if (player.cfpass >= 3000000 && player.mocpass == 2500000) {
                    Service.gI().sendThongBao(player, "Bắt Đầu\n Nhận Mốc Pass Tiếp Theo\nSau " + time + " Giây!");
                    while (time > 0) {
                        time--;
                        Thread.sleep(1000);
                        Service.gI().sendThongBao(player, "|7|" + time);
                    }
                    player.mocpass = 3000000;
                    itemqua = ItemService.gI().createNewItem((short) 2008, 10);
                    itemqua.itemOptions.add(new ItemOption(50, 700));
                    itemqua.itemOptions.add(new ItemOption(30, 1));

                    itemqua1 = ItemService.gI().createNewItem((short) 2009, 10);
                    itemqua1.itemOptions.add(new ItemOption(77, 700));
                    itemqua1.itemOptions.add(new ItemOption(30, 1));

                    itemqua2 = ItemService.gI().createNewItem((short) 2010, 10);
                    itemqua2.itemOptions.add(new ItemOption(103, 700));
                    itemqua2.itemOptions.add(new ItemOption(30, 1));

                    itemqua3 = ItemService.gI().createNewItem((short) 2011, 10);
                    itemqua3.itemOptions.add(new ItemOption(101, 700));
                    itemqua3.itemOptions.add(new ItemOption(30, 500));

                    itemqua4 = ItemService.gI().createNewItem((short) 1502, 500);
                    itemqua4.itemOptions.add(new ItemOption(30, 1));

                    Service.gI().sendThongBaoFromAdmin(player, "Bạn Đã Nhận Được 10 Đá Thần Linh SD,HP,KI,TNSM,ĐÁ NỘITẠI\n"
                            + "Vui Lòng Dùng Hết Đồ Mốc Thấp \nof Sao Pha Lê Hiện Có,Trong Hành Trang Mới Được Nhận Mốc Tiếp Theo\n"
                            + "Nếu Cố Tình Sẽ Lập Tức Mất Mốc Này Và Không Nhận Thêm Được Gì\n"
                            + "Không Hỗ Trợ Nhận Lai Mốc , Mỗi Mùa Được Cày Lại\n"
                            + "Max Pass 500 Triệu Điểm");
                    InventoryService.gI().addItemBag(player, itemqua, 10);
                    InventoryService.gI().addItemBag(player, itemqua1, 10);
                    InventoryService.gI().addItemBag(player, itemqua2, 10);
                    InventoryService.gI().addItemBag(player, itemqua3, 10);
                    InventoryService.gI().addItemBag(player, itemqua4, 500);
                    InventoryService.gI().sendItemBags(player);

                } else if (player.cfpass >= 3500000 && player.mocpass == 3000000) {
                    Service.gI().sendThongBao(player, "Bắt Đầu\n Nhận Mốc Pass Tiếp Theo\nSau " + time + " Giây!");
                    while (time > 0) {
                        time--;
                        Thread.sleep(1000);
                        Service.gI().sendThongBao(player, "|7|" + time);
                    }
                    player.mocpass = 3500000;
                    itemqua = ItemService.gI().createNewItem((short) 2008, 10);
                    itemqua.itemOptions.add(new ItemOption(50, 750));
                    itemqua.itemOptions.add(new ItemOption(30, 1));

                    itemqua1 = ItemService.gI().createNewItem((short) 2009, 10);
                    itemqua1.itemOptions.add(new ItemOption(77, 750));
                    itemqua1.itemOptions.add(new ItemOption(30, 1));

                    itemqua2 = ItemService.gI().createNewItem((short) 2010, 10);
                    itemqua2.itemOptions.add(new ItemOption(103, 750));
                    itemqua2.itemOptions.add(new ItemOption(30, 1));

                    itemqua3 = ItemService.gI().createNewItem((short) 2011, 10);
                    itemqua3.itemOptions.add(new ItemOption(101, 750));
                    itemqua3.itemOptions.add(new ItemOption(30, 500));

                    itemqua4 = ItemService.gI().createNewItem((short) 1502, 500);
                    itemqua4.itemOptions.add(new ItemOption(30, 1));

                    Service.gI().sendThongBaoFromAdmin(player, "Bạn Đã Nhận Được 10 Đá Thần Linh SD,HP,KI,TNSM,ĐÁ NỘITẠI\n"
                            + "Vui Lòng Dùng Hết Đồ Mốc Thấp \nof Sao Pha Lê Hiện Có,Trong Hành Trang Mới Được Nhận Mốc Tiếp Theo\n"
                            + "Nếu Cố Tình Sẽ Lập Tức Mất Mốc Này Và Không Nhận Thêm Được Gì\n"
                            + "Không Hỗ Trợ Nhận Lai Mốc , Mỗi Mùa Được Cày Lại\n"
                            + "Max Pass 500 Triệu Điểm");
                    InventoryService.gI().addItemBag(player, itemqua, 10);
                    InventoryService.gI().addItemBag(player, itemqua1, 10);
                    InventoryService.gI().addItemBag(player, itemqua2, 10);
                    InventoryService.gI().addItemBag(player, itemqua3, 10);
                    InventoryService.gI().addItemBag(player, itemqua4, 500);
                    InventoryService.gI().sendItemBags(player);
                } else if (player.cfpass >= 4000000 && player.mocpass == 3500000) {
                    Service.gI().sendThongBao(player, "Bắt Đầu\n Nhận Mốc Pass Tiếp Theo\nSau " + time + " Giây!");
                    while (time > 0) {
                        time--;
                        Thread.sleep(1000);
                        Service.gI().sendThongBao(player, "|7|" + time);
                    }
                    player.mocpass = 4000000;
                    itemqua = ItemService.gI().createNewItem((short) 2008, 10);
                    itemqua.itemOptions.add(new ItemOption(50, 800));
                    itemqua.itemOptions.add(new ItemOption(30, 1));

                    itemqua1 = ItemService.gI().createNewItem((short) 2009, 10);
                    itemqua1.itemOptions.add(new ItemOption(77, 800));
                    itemqua1.itemOptions.add(new ItemOption(30, 1));

                    itemqua2 = ItemService.gI().createNewItem((short) 2010, 10);
                    itemqua2.itemOptions.add(new ItemOption(103, 800));
                    itemqua2.itemOptions.add(new ItemOption(30, 1));

                    itemqua3 = ItemService.gI().createNewItem((short) 2011, 10);
                    itemqua3.itemOptions.add(new ItemOption(101, 800));
                    itemqua3.itemOptions.add(new ItemOption(30, 500));

                    itemqua4 = ItemService.gI().createNewItem((short) 1502, 500);
                    itemqua4.itemOptions.add(new ItemOption(30, 1));

                    Service.gI().sendThongBaoFromAdmin(player, "Bạn Đã Nhận Được 10 Đá Thần Linh SD,HP,KI,TNSM,ĐÁ NỘITẠI\n"
                            + "Vui Lòng Dùng Hết Đồ Mốc Thấp \nof Sao Pha Lê Hiện Có,Trong Hành Trang Mới Được Nhận Mốc Tiếp Theo\n"
                            + "Nếu Cố Tình Sẽ Lập Tức Mất Mốc Này Và Không Nhận Thêm Được Gì\n"
                            + "Không Hỗ Trợ Nhận Lai Mốc , Mỗi Mùa Được Cày Lại\n"
                            + "Max Pass 500 Triệu Điểm");
                    InventoryService.gI().addItemBag(player, itemqua, 10);
                    InventoryService.gI().addItemBag(player, itemqua1, 10);
                    InventoryService.gI().addItemBag(player, itemqua2, 10);
                    InventoryService.gI().addItemBag(player, itemqua3, 10);
                    InventoryService.gI().addItemBag(player, itemqua4, 500);
                    InventoryService.gI().sendItemBags(player);
                } else if (player.cfpass >= 4500000 && player.mocpass == 4000000) {
                    Service.gI().sendThongBao(player, "Bắt Đầu\n Nhận Mốc Pass Tiếp Theo\nSau " + time + " Giây!");
                    while (time > 0) {
                        time--;
                        Thread.sleep(1000);
                        Service.gI().sendThongBao(player, "|7|" + time);
                    }
                    player.mocpass = 4500000;
                    itemqua = ItemService.gI().createNewItem((short) 2008, 10);
                    itemqua.itemOptions.add(new ItemOption(50, 850));
                    itemqua.itemOptions.add(new ItemOption(30, 1));

                    itemqua1 = ItemService.gI().createNewItem((short) 2009, 10);
                    itemqua1.itemOptions.add(new ItemOption(77, 850));
                    itemqua1.itemOptions.add(new ItemOption(30, 1));

                    itemqua2 = ItemService.gI().createNewItem((short) 2010, 10);
                    itemqua2.itemOptions.add(new ItemOption(103, 850));
                    itemqua2.itemOptions.add(new ItemOption(30, 1));

                    itemqua3 = ItemService.gI().createNewItem((short) 2011, 10);
                    itemqua3.itemOptions.add(new ItemOption(101, 850));
                    itemqua3.itemOptions.add(new ItemOption(30, 500));

                    itemqua4 = ItemService.gI().createNewItem((short) 1502, 500);
                    itemqua4.itemOptions.add(new ItemOption(30, 1));

                    Service.gI().sendThongBaoFromAdmin(player, "Bạn Đã Nhận Được 10 Đá Thần Linh SD,HP,KI,TNSM,ĐÁ NỘITẠI\n"
                            + "Vui Lòng Dùng Hết Đồ Mốc Thấp \nof Sao Pha Lê Hiện Có,Trong Hành Trang Mới Được Nhận Mốc Tiếp Theo\n"
                            + "Nếu Cố Tình Sẽ Lập Tức Mất Mốc Này Và Không Nhận Thêm Được Gì\n"
                            + "Không Hỗ Trợ Nhận Lai Mốc , Mỗi Mùa Được Cày Lại\n"
                            + "Max Pass 500 Triệu Điểm");
                    InventoryService.gI().addItemBag(player, itemqua, 10);
                    InventoryService.gI().addItemBag(player, itemqua1, 10);
                    InventoryService.gI().addItemBag(player, itemqua2, 10);
                    InventoryService.gI().addItemBag(player, itemqua3, 10);
                    InventoryService.gI().addItemBag(player, itemqua4, 500);
                    InventoryService.gI().sendItemBags(player);

                } else if (player.cfpass >= 5000000 && player.mocpass == 4500000) {
                    Service.gI().sendThongBao(player, "Bắt Đầu\n Nhận Mốc Pass Tiếp Theo\nSau " + time + " Giây!");
                    while (time > 0) {
                        time--;
                        Thread.sleep(1000);
                        Service.gI().sendThongBao(player, "|7|" + time);
                    }
                    player.mocpass = 5000000;
                    itemqua = ItemService.gI().createNewItem((short) 2008, 10);
                    itemqua.itemOptions.add(new ItemOption(50, 900));
                    itemqua.itemOptions.add(new ItemOption(30, 1));

                    itemqua1 = ItemService.gI().createNewItem((short) 2009, 10);
                    itemqua1.itemOptions.add(new ItemOption(77, 900));
                    itemqua1.itemOptions.add(new ItemOption(30, 1));

                    itemqua2 = ItemService.gI().createNewItem((short) 2010, 10);
                    itemqua2.itemOptions.add(new ItemOption(103, 900));
                    itemqua2.itemOptions.add(new ItemOption(30, 1));

                    itemqua3 = ItemService.gI().createNewItem((short) 2011, 10);
                    itemqua3.itemOptions.add(new ItemOption(101, 900));
                    itemqua3.itemOptions.add(new ItemOption(30, 500));

                    itemqua4 = ItemService.gI().createNewItem((short) 1502, 500);
                    itemqua4.itemOptions.add(new ItemOption(30, 1));

                    Service.gI().sendThongBaoFromAdmin(player, "Bạn Đã Nhận Được 10 Đá Thần Linh SD,HP,KI,TNSM,ĐÁ NỘITẠI\n"
                            + "Vui Lòng Dùng Hết Đồ Mốc Thấp \nof Sao Pha Lê Hiện Có,Trong Hành Trang Mới Được Nhận Mốc Tiếp Theo\n"
                            + "Nếu Cố Tình Sẽ Lập Tức Mất Mốc Này Và Không Nhận Thêm Được Gì\n"
                            + "Không Hỗ Trợ Nhận Lai Mốc , Mỗi Mùa Được Cày Lại\n"
                            + "Max Pass 500 Triệu Điểm");
                    InventoryService.gI().addItemBag(player, itemqua, 10);
                    InventoryService.gI().addItemBag(player, itemqua1, 10);
                    InventoryService.gI().addItemBag(player, itemqua2, 10);
                    InventoryService.gI().addItemBag(player, itemqua3, 10);
                    InventoryService.gI().addItemBag(player, itemqua4, 500);
                    InventoryService.gI().sendItemBags(player);
                } else if (player.cfpass >= 5500000 && player.mocpass == 5000000) {
                    Service.gI().sendThongBao(player, "Bắt Đầu\n Nhận Mốc Pass Tiếp Theo\nSau " + time + " Giây!");
                    while (time > 0) {
                        time--;
                        Thread.sleep(1000);
                        Service.gI().sendThongBao(player, "|7|" + time);
                    }
                    player.mocpass = 5500000;
                    itemqua = ItemService.gI().createNewItem((short) 2008, 10);
                    itemqua.itemOptions.add(new ItemOption(50, 950));
                    itemqua.itemOptions.add(new ItemOption(30, 1));

                    itemqua1 = ItemService.gI().createNewItem((short) 2009, 10);
                    itemqua1.itemOptions.add(new ItemOption(77, 950));
                    itemqua1.itemOptions.add(new ItemOption(30, 1));

                    itemqua2 = ItemService.gI().createNewItem((short) 2010, 10);
                    itemqua2.itemOptions.add(new ItemOption(103, 950));
                    itemqua2.itemOptions.add(new ItemOption(30, 1));

                    itemqua3 = ItemService.gI().createNewItem((short) 2011, 10);
                    itemqua3.itemOptions.add(new ItemOption(101, 950));
                    itemqua3.itemOptions.add(new ItemOption(30, 500));

                    itemqua4 = ItemService.gI().createNewItem((short) 1502, 500);
                    itemqua4.itemOptions.add(new ItemOption(30, 1));

                    Service.gI().sendThongBaoFromAdmin(player, "Bạn Đã Nhận Được 10 Đá Thần Linh SD,HP,KI,TNSM,ĐÁ NỘITẠI\n"
                            + "Vui Lòng Dùng Hết Đồ Mốc Thấp \nof Sao Pha Lê Hiện Có,Trong Hành Trang Mới Được Nhận Mốc Tiếp Theo\n"
                            + "Nếu Cố Tình Sẽ Lập Tức Mất Mốc Này Và Không Nhận Thêm Được Gì\n"
                            + "Không Hỗ Trợ Nhận Lai Mốc , Mỗi Mùa Được Cày Lại\n"
                            + "Max Pass 500 Triệu Điểm");
                    InventoryService.gI().addItemBag(player, itemqua, 10);
                    InventoryService.gI().addItemBag(player, itemqua1, 10);
                    InventoryService.gI().addItemBag(player, itemqua2, 10);
                    InventoryService.gI().addItemBag(player, itemqua3, 10);
                    InventoryService.gI().addItemBag(player, itemqua4, 500);
                    InventoryService.gI().sendItemBags(player);

                } else if (player.cfpass >= 6000000 && player.mocpass == 5500000) {
                    Service.gI().sendThongBao(player, "Bắt Đầu\n Nhận Mốc Pass Tiếp Theo\nSau " + time + " Giây!");
                    while (time > 0) {
                        time--;
                        Thread.sleep(1000);
                        Service.gI().sendThongBao(player, "|7|" + time);
                    }
                    player.mocpass = 6000000;
                    itemqua = ItemService.gI().createNewItem((short) 2008, 10);
                    itemqua.itemOptions.add(new ItemOption(50, 1000));
                    itemqua.itemOptions.add(new ItemOption(30, 1));

                    itemqua1 = ItemService.gI().createNewItem((short) 2009, 10);
                    itemqua1.itemOptions.add(new ItemOption(77, 1000));
                    itemqua1.itemOptions.add(new ItemOption(30, 1));

                    itemqua2 = ItemService.gI().createNewItem((short) 2010, 10);
                    itemqua2.itemOptions.add(new ItemOption(103, 1000));
                    itemqua2.itemOptions.add(new ItemOption(30, 1));

                    itemqua3 = ItemService.gI().createNewItem((short) 2011, 10);
                    itemqua3.itemOptions.add(new ItemOption(101, 1000));
                    itemqua3.itemOptions.add(new ItemOption(30, 500));

                    itemqua4 = ItemService.gI().createNewItem((short) 1502, 500);
                    itemqua4.itemOptions.add(new ItemOption(30, 1));

                    Service.gI().sendThongBaoFromAdmin(player, "Bạn Đã Nhận Được 10 Đá Thần Linh SD,HP,KI,TNSM,ĐÁ NỘITẠI\n"
                            + "Vui Lòng Dùng Hết Đồ Mốc Thấp \nof Sao Pha Lê Hiện Có,Trong Hành Trang Mới Được Nhận Mốc Tiếp Theo\n"
                            + "Nếu Cố Tình Sẽ Lập Tức Mất Mốc Này Và Không Nhận Thêm Được Gì\n"
                            + "Không Hỗ Trợ Nhận Lai Mốc , Mỗi Mùa Được Cày Lại\n"
                            + "Max Pass 500 Triệu Điểm");
                    InventoryService.gI().addItemBag(player, itemqua, 10);
                    InventoryService.gI().addItemBag(player, itemqua1, 10);
                    InventoryService.gI().addItemBag(player, itemqua2, 10);
                    InventoryService.gI().addItemBag(player, itemqua3, 10);
                    InventoryService.gI().addItemBag(player, itemqua4, 500);
                    InventoryService.gI().sendItemBags(player);
                } else if (player.cfpass >= 6500000 && player.mocpass == 6000000) {
                    Service.gI().sendThongBao(player, "Bắt Đầu\n Nhận Mốc Pass Tiếp Theo\nSau " + time + " Giây!");
                    while (time > 0) {
                        time--;
                        Thread.sleep(1000);
                        Service.gI().sendThongBao(player, "|7|" + time);
                    }
                    player.mocpass = 6500000;
                    itemqua = ItemService.gI().createNewItem((short) 2008, 10);
                    itemqua.itemOptions.add(new ItemOption(50, 1050));
                    itemqua.itemOptions.add(new ItemOption(30, 1));

                    itemqua1 = ItemService.gI().createNewItem((short) 2009, 10);
                    itemqua1.itemOptions.add(new ItemOption(77, 1050));
                    itemqua1.itemOptions.add(new ItemOption(30, 1));

                    itemqua2 = ItemService.gI().createNewItem((short) 2010, 10);
                    itemqua2.itemOptions.add(new ItemOption(103, 1050));
                    itemqua2.itemOptions.add(new ItemOption(30, 1));

                    itemqua3 = ItemService.gI().createNewItem((short) 2011, 10);
                    itemqua3.itemOptions.add(new ItemOption(101, 1050));
                    itemqua3.itemOptions.add(new ItemOption(30, 500));

                    itemqua4 = ItemService.gI().createNewItem((short) 1502, 500);
                    itemqua4.itemOptions.add(new ItemOption(30, 1));

                    Service.gI().sendThongBaoFromAdmin(player, "Bạn Đã Nhận Được 10 Đá Thần Linh SD,HP,KI,TNSM,ĐÁ NỘITẠI\n"
                            + "Vui Lòng Dùng Hết Đồ Mốc Thấp \nof Sao Pha Lê Hiện Có,Trong Hành Trang Mới Được Nhận Mốc Tiếp Theo\n"
                            + "Nếu Cố Tình Sẽ Lập Tức Mất Mốc Này Và Không Nhận Thêm Được Gì\n"
                            + "Không Hỗ Trợ Nhận Lai Mốc , Mỗi Mùa Được Cày Lại\n"
                            + "Max Pass 500 Triệu Điểm");
                    InventoryService.gI().addItemBag(player, itemqua, 10);
                    InventoryService.gI().addItemBag(player, itemqua1, 10);
                    InventoryService.gI().addItemBag(player, itemqua2, 10);
                    InventoryService.gI().addItemBag(player, itemqua3, 10);
                    InventoryService.gI().addItemBag(player, itemqua4, 500);
                    InventoryService.gI().sendItemBags(player);

                } else {
                    Service.gI().sendThongBaoFromAdmin(player, "Bạn Chưa Đủ Điều Kiện Nhận Mốc Pass Này!");
                }
            } catch (Exception e) {
            }
        } else {
            Service.getInstance().sendThongBaoFromAdmin(player, "Mày phải có ít nhất  Trống 6 ô trống hành trang");
        }
    }

    public void ComfirmMocPassmua(Player player) {
        if (InventoryService.gI().getCountEmptyBag(player) > 5) {
            Item itemqua;
            Item itemqua1;
            Item itemqua2;
            Item itemqua3;
            Item itemqua4;
            Item itemqua5;
            try {
                int time = 5;
                if (player.premium >= 50000 && player.mocpassvip == 0) { // cfpass thay cho tổng nap // mocpass là khi nhận mốc 1 sẽ cộng aã nhận
                    Service.gI().sendThongBao(player, "Bắt Đầu\n Nhận Mốc Pass Tiếp Theo\nSau " + time + " Giây!");
                    while (time > 0) {
                        time--;
                        Thread.sleep(1000);
                        Service.gI().sendThongBao(player, "|8|" + time);
                    }
                    player.mocpassvip = 50000;
                    itemqua = ItemService.gI().createNewItem((short) 1388, 10);
                    itemqua.itemOptions.add(new ItemOption(214, 1000));
                    itemqua.itemOptions.add(new ItemOption(30, 1));

                    itemqua1 = ItemService.gI().createNewItem((short) 1389, 10);
                    itemqua1.itemOptions.add(new ItemOption(215, 1000));
                    itemqua1.itemOptions.add(new ItemOption(30, 1));

                    itemqua2 = ItemService.gI().createNewItem((short) 1390, 10);
                    itemqua2.itemOptions.add(new ItemOption(216, 1000));
                    itemqua2.itemOptions.add(new ItemOption(30, 1));

                    itemqua3 = ItemService.gI().createNewItem((short) 1391, 1);
                    itemqua3.itemOptions.add(new ItemOption(217, 10));
                    itemqua3.itemOptions.add(new ItemOption(30, 500));

                    itemqua4 = ItemService.gI().createNewItem((short) 981, 1);
                    itemqua4.itemOptions.add(new ItemOption(31, 1));

                    Service.gI().sendThongBaoFromAdmin(player, "Bạn Đã Nhận Được 10 Đá Cổ Ngọc Vạn Sắc Khảm Trang Bị\n"
                            + "Vui Lòng Dùng Hết Đồ Mốc Thấp \nof Sao Pha Lê Hiện Có,Trong Hành Trang Mới Được Nhận Mốc Tiếp Theo\n"
                            + "Nếu Cố Tình Sẽ Lập Tức Mất Mốc Này Và Không Nhận Thêm Được Gì\n"
                            + "Không Hỗ Trợ Nhận Lai Mốc , Mỗi Mùa Được Cày Lại\n"
                            + "Max Pass vip 500 Triệu Điểm");
                    InventoryService.gI().addItemBag(player, itemqua, 10);
                    InventoryService.gI().addItemBag(player, itemqua1, 10);
                    InventoryService.gI().addItemBag(player, itemqua2, 10);
                    InventoryService.gI().addItemBag(player, itemqua3, 1);
                    InventoryService.gI().addItemBag(player, itemqua4, 1);

                    InventoryService.gI().sendItemBags(player);
                } else if (player.premium >= 100000 && player.mocpassvip == 50000) {
                    Service.gI().sendThongBao(player, "Bắt Đầu\n Nhận Mốc Pass Tiếp Theo\nSau " + time + " Giây!");
                    while (time > 0) {
                        time--;
                        Thread.sleep(1000);
                        Service.gI().sendThongBao(player, "|8|" + time);
                    }
                    player.mocpassvip = 100000;
                    itemqua = ItemService.gI().createNewItem((short) 1388, 10);
                    itemqua.itemOptions.add(new ItemOption(214, 2000));
                    itemqua.itemOptions.add(new ItemOption(30, 1));

                    itemqua1 = ItemService.gI().createNewItem((short) 1389, 10);
                    itemqua1.itemOptions.add(new ItemOption(215, 2000));
                    itemqua1.itemOptions.add(new ItemOption(30, 1));

                    itemqua2 = ItemService.gI().createNewItem((short) 1390, 10);
                    itemqua2.itemOptions.add(new ItemOption(216, 2000));
                    itemqua2.itemOptions.add(new ItemOption(30, 1));

                    itemqua3 = ItemService.gI().createNewItem((short) 1391, 1);
                    itemqua3.itemOptions.add(new ItemOption(217, 10));
                    itemqua3.itemOptions.add(new ItemOption(30, 500));

                    itemqua4 = ItemService.gI().createNewItem((short) 981, 1);
                    itemqua4.itemOptions.add(new ItemOption(31, 1));

                    Service.gI().sendThongBaoFromAdmin(player, "Bạn Đã Nhận Được 10 Đá Cổ Ngọc Vạn Sắc Khảm Trang Bị\n"
                            + "Vui Lòng Dùng Hết Đồ Mốc Thấp \nof Sao Pha Lê Hiện Có,Trong Hành Trang Mới Được Nhận Mốc Tiếp Theo\n"
                            + "Nếu Cố Tình Sẽ Lập Tức Mất Mốc Này Và Không Nhận Thêm Được Gì\n"
                            + "Không Hỗ Trợ Nhận Lai Mốc , Mỗi Mùa Được Cày Lại\n"
                            + "Max Pass vip 500 Triệu Điểm");
                    InventoryService.gI().addItemBag(player, itemqua, 10);
                    InventoryService.gI().addItemBag(player, itemqua1, 10);
                    InventoryService.gI().addItemBag(player, itemqua2, 10);
                    InventoryService.gI().addItemBag(player, itemqua3, 1);
                    InventoryService.gI().addItemBag(player, itemqua4, 1);
                    InventoryService.gI().sendItemBags(player);
                } else if (player.premium >= 150000 && player.mocpassvip == 100000) {
                    Service.gI().sendThongBao(player, "Bắt Đầu\n Nhận Mốc Pass Tiếp Theo\nSau " + time + " Giây!");
                    while (time > 0) {
                        time--;
                        Thread.sleep(1000);
                        Service.gI().sendThongBao(player, "|7|" + time);
                    }
                    player.mocpassvip = 150000;
                    itemqua = ItemService.gI().createNewItem((short) 1388, 10);
                    itemqua.itemOptions.add(new ItemOption(214, 3000));
                    itemqua.itemOptions.add(new ItemOption(30, 1));

                    itemqua1 = ItemService.gI().createNewItem((short) 1389, 10);
                    itemqua1.itemOptions.add(new ItemOption(215, 3000));
                    itemqua1.itemOptions.add(new ItemOption(30, 1));

                    itemqua2 = ItemService.gI().createNewItem((short) 1390, 10);
                    itemqua2.itemOptions.add(new ItemOption(216, 3000));
                    itemqua2.itemOptions.add(new ItemOption(30, 1));

                    itemqua3 = ItemService.gI().createNewItem((short) 1391, 1);
                    itemqua3.itemOptions.add(new ItemOption(217, 10));
                    itemqua3.itemOptions.add(new ItemOption(30, 500));

                    itemqua4 = ItemService.gI().createNewItem((short) 981, 1);
                    itemqua4.itemOptions.add(new ItemOption(31, 1));

                    Service.gI().sendThongBaoFromAdmin(player, "Bạn Đã Nhận Được 10 Đá Cổ Ngọc Vạn Sắc Khảm Trang Bị\n"
                            + "Vui Lòng Dùng Hết Đồ Mốc Thấp \nof Sao Pha Lê Hiện Có,Trong Hành Trang Mới Được Nhận Mốc Tiếp Theo\n"
                            + "Nếu Cố Tình Sẽ Lập Tức Mất Mốc Này Và Không Nhận Thêm Được Gì\n"
                            + "Không Hỗ Trợ Nhận Lai Mốc , Mỗi Mùa Được Cày Lại\n"
                            + "Max Pass vip 500 Triệu Điểm");
                    InventoryService.gI().addItemBag(player, itemqua, 10);
                    InventoryService.gI().addItemBag(player, itemqua1, 10);
                    InventoryService.gI().addItemBag(player, itemqua2, 10);
                    InventoryService.gI().addItemBag(player, itemqua3, 1);
                    InventoryService.gI().addItemBag(player, itemqua4, 1);
                    InventoryService.gI().sendItemBags(player);
                } else if (player.premium >= 200000 && player.mocpassvip == 150000) {
                    Service.gI().sendThongBao(player, "Bắt Đầu\n Nhận Mốc Pass Tiếp Theo\nSau " + time + " Giây!");
                    while (time > 0) {
                        time--;
                        Thread.sleep(1000);
                        Service.gI().sendThongBao(player, "|7|" + time);
                    }
                    player.mocpassvip = 200000;// mocpass
                    itemqua = ItemService.gI().createNewItem((short) 1388, 10);
                    itemqua.itemOptions.add(new ItemOption(214, 4000));
                    itemqua.itemOptions.add(new ItemOption(30, 1));

                    itemqua1 = ItemService.gI().createNewItem((short) 1389, 10);
                    itemqua1.itemOptions.add(new ItemOption(215, 4000));
                    itemqua1.itemOptions.add(new ItemOption(30, 1));

                    itemqua2 = ItemService.gI().createNewItem((short) 1390, 10);
                    itemqua2.itemOptions.add(new ItemOption(216, 4000));
                    itemqua2.itemOptions.add(new ItemOption(30, 1));

                    itemqua3 = ItemService.gI().createNewItem((short) 1391, 1);
                    itemqua3.itemOptions.add(new ItemOption(217, 10));
                    itemqua3.itemOptions.add(new ItemOption(30, 500));

                    itemqua4 = ItemService.gI().createNewItem((short) 981, 1);
                    itemqua4.itemOptions.add(new ItemOption(31, 1));

                    Service.gI().sendThongBaoFromAdmin(player, "Bạn Đã Nhận Được 10 Đá Cổ Ngọc Vạn Sắc Khảm Trang Bị\n"
                            + "Vui Lòng Dùng Hết Đồ Mốc Thấp \nof Sao Pha Lê Hiện Có,Trong Hành Trang Mới Được Nhận Mốc Tiếp Theo\n"
                            + "Nếu Cố Tình Sẽ Lập Tức Mất Mốc Này Và Không Nhận Thêm Được Gì\n"
                            + "Không Hỗ Trợ Nhận Lai Mốc , Mỗi Mùa Được Cày Lại\n"
                            + "Max Pass vip 500 Triệu Điểm");
                    InventoryService.gI().addItemBag(player, itemqua, 10);
                    InventoryService.gI().addItemBag(player, itemqua1, 10);
                    InventoryService.gI().addItemBag(player, itemqua2, 10);
                    InventoryService.gI().addItemBag(player, itemqua3, 1);
                    InventoryService.gI().addItemBag(player, itemqua4, 1);
                    InventoryService.gI().sendItemBags(player);
                } else if (player.premium >= 250000 && player.mocpassvip == 200000) {
                    Service.gI().sendThongBao(player, "Bắt Đầu\n Nhận Mốc Pass Tiếp Theo\nSau " + time + " Giây!");
                    while (time > 0) {
                        time--;
                        Thread.sleep(1000);
                        Service.gI().sendThongBao(player, "|7|" + time);
                    }
                    player.mocpassvip = 250000;
                    itemqua = ItemService.gI().createNewItem((short) 1388, 10);
                    itemqua.itemOptions.add(new ItemOption(214, 5000));
                    itemqua.itemOptions.add(new ItemOption(30, 1));

                    itemqua1 = ItemService.gI().createNewItem((short) 1389, 10);
                    itemqua1.itemOptions.add(new ItemOption(215, 5000));
                    itemqua1.itemOptions.add(new ItemOption(30, 1));

                    itemqua2 = ItemService.gI().createNewItem((short) 1390, 10);
                    itemqua2.itemOptions.add(new ItemOption(216, 5000));
                    itemqua2.itemOptions.add(new ItemOption(30, 1));

                    itemqua3 = ItemService.gI().createNewItem((short) 1391, 1);
                    itemqua3.itemOptions.add(new ItemOption(217, 10));
                    itemqua3.itemOptions.add(new ItemOption(30, 500));

                    itemqua4 = ItemService.gI().createNewItem((short) 981, 1);
                    itemqua4.itemOptions.add(new ItemOption(31, 1));

                    Service.gI().sendThongBaoFromAdmin(player, "Bạn Đã Nhận Được 10 Đá Cổ Ngọc Vạn Sắc Khảm Trang Bị\n"
                            + "Vui Lòng Dùng Hết Đồ Mốc Thấp \nof Sao Pha Lê Hiện Có,Trong Hành Trang Mới Được Nhận Mốc Tiếp Theo\n"
                            + "Nếu Cố Tình Sẽ Lập Tức Mất Mốc Này Và Không Nhận Thêm Được Gì\n"
                            + "Không Hỗ Trợ Nhận Lai Mốc , Mỗi Mùa Được Cày Lại\n"
                            + "Max Pass vip 500 Triệu Điểm");
                    InventoryService.gI().addItemBag(player, itemqua, 10);
                    InventoryService.gI().addItemBag(player, itemqua1, 10);
                    InventoryService.gI().addItemBag(player, itemqua2, 10);
                    InventoryService.gI().addItemBag(player, itemqua3, 1);
                    InventoryService.gI().addItemBag(player, itemqua4, 1);
                    InventoryService.gI().sendItemBags(player);
                } else if (player.premium >= 500000 && player.mocpassvip == 250000) {
                    Service.gI().sendThongBao(player, "Bắt Đầu\n Nhận Mốc Pass Tiếp Theo\nSau " + time + " Giây!");
                    while (time > 0) {
                        time--;
                        Thread.sleep(1000);
                        Service.gI().sendThongBao(player, "|7|" + time);
                    }
                    player.mocpassvip = 500000;
                    itemqua = ItemService.gI().createNewItem((short) 1388, 10);
                    itemqua.itemOptions.add(new ItemOption(214, 6000));
                    itemqua.itemOptions.add(new ItemOption(30, 1));

                    itemqua1 = ItemService.gI().createNewItem((short) 1389, 10);
                    itemqua1.itemOptions.add(new ItemOption(215, 6000));
                    itemqua1.itemOptions.add(new ItemOption(30, 1));

                    itemqua2 = ItemService.gI().createNewItem((short) 1390, 10);
                    itemqua2.itemOptions.add(new ItemOption(216, 6000));
                    itemqua2.itemOptions.add(new ItemOption(30, 1));

                    itemqua3 = ItemService.gI().createNewItem((short) 1391, 1);
                    itemqua3.itemOptions.add(new ItemOption(217, 10));
                    itemqua3.itemOptions.add(new ItemOption(30, 500));

                    itemqua4 = ItemService.gI().createNewItem((short) 981, 1);
                    itemqua4.itemOptions.add(new ItemOption(31, 1));

                    Service.gI().sendThongBaoFromAdmin(player, "Bạn Đã Nhận Được 10 Đá Cổ Ngọc Vạn Sắc Khảm Trang Bị\n"
                            + "Vui Lòng Dùng Hết Đồ Mốc Thấp \nof Sao Pha Lê Hiện Có,Trong Hành Trang Mới Được Nhận Mốc Tiếp Theo\n"
                            + "Nếu Cố Tình Sẽ Lập Tức Mất Mốc Này Và Không Nhận Thêm Được Gì\n"
                            + "Không Hỗ Trợ Nhận Lai Mốc , Mỗi Mùa Được Cày Lại\n"
                            + "Max Pass vip 500 Triệu Điểm");
                    InventoryService.gI().addItemBag(player, itemqua, 10);
                    InventoryService.gI().addItemBag(player, itemqua1, 10);
                    InventoryService.gI().addItemBag(player, itemqua2, 10);
                    InventoryService.gI().addItemBag(player, itemqua3, 1);
                    InventoryService.gI().addItemBag(player, itemqua4, 1);
                    InventoryService.gI().sendItemBags(player);
                } else if (player.premium >= 1000000 && player.mocpassvip == 500000) {
                    Service.gI().sendThongBao(player, "Bắt Đầu\n Nhận Mốc Pass Tiếp Theo\nSau " + time + " Giây!");
                    while (time > 0) {
                        time--;
                        Thread.sleep(1000);
                        Service.gI().sendThongBao(player, "|7|" + time);
                    }
                    player.mocpassvip = 1000000;
                    itemqua = ItemService.gI().createNewItem((short) 1388, 10);
                    itemqua.itemOptions.add(new ItemOption(214, 7000));
                    itemqua.itemOptions.add(new ItemOption(30, 1));

                    itemqua1 = ItemService.gI().createNewItem((short) 1389, 10);
                    itemqua1.itemOptions.add(new ItemOption(215, 7000));
                    itemqua1.itemOptions.add(new ItemOption(30, 1));

                    itemqua2 = ItemService.gI().createNewItem((short) 1390, 10);
                    itemqua2.itemOptions.add(new ItemOption(216, 7000));
                    itemqua2.itemOptions.add(new ItemOption(30, 1));

                    itemqua3 = ItemService.gI().createNewItem((short) 1391, 1);
                    itemqua3.itemOptions.add(new ItemOption(217, 10));
                    itemqua3.itemOptions.add(new ItemOption(30, 500));

                    itemqua4 = ItemService.gI().createNewItem((short) 981, 1);
                    itemqua4.itemOptions.add(new ItemOption(31, 1));

                    Service.gI().sendThongBaoFromAdmin(player, "Bạn Đã Nhận Được 10 Đá Cổ Ngọc Vạn Sắc Khảm Trang Bị\n"
                            + "Vui Lòng Dùng Hết Đồ Mốc Thấp \nof Sao Pha Lê Hiện Có,Trong Hành Trang Mới Được Nhận Mốc Tiếp Theo\n"
                            + "Nếu Cố Tình Sẽ Lập Tức Mất Mốc Này Và Không Nhận Thêm Được Gì\n"
                            + "Không Hỗ Trợ Nhận Lai Mốc , Mỗi Mùa Được Cày Lại\n"
                            + "Max Pass vip 500 Triệu Điểm");
                    InventoryService.gI().addItemBag(player, itemqua, 10);
                    InventoryService.gI().addItemBag(player, itemqua1, 10);
                    InventoryService.gI().addItemBag(player, itemqua2, 10);
                    InventoryService.gI().addItemBag(player, itemqua3, 1);
                    InventoryService.gI().addItemBag(player, itemqua4, 1);
                    InventoryService.gI().sendItemBags(player);
                } else if (player.premium >= 1500000 && player.mocpassvip == 1000000) {
                    Service.gI().sendThongBao(player, "Bắt Đầu\n Nhận Mốc Pass Tiếp Theo\nSau " + time + " Giây!");
                    while (time > 0) {
                        time--;
                        Thread.sleep(1000);
                        Service.gI().sendThongBao(player, "|7|" + time);
                    }
                    player.mocpassvip = 1500000;
                    itemqua = ItemService.gI().createNewItem((short) 1388, 10);
                    itemqua.itemOptions.add(new ItemOption(214, 8000));
                    itemqua.itemOptions.add(new ItemOption(30, 1));

                    itemqua1 = ItemService.gI().createNewItem((short) 1389, 10);
                    itemqua1.itemOptions.add(new ItemOption(215, 8000));
                    itemqua1.itemOptions.add(new ItemOption(30, 1));

                    itemqua2 = ItemService.gI().createNewItem((short) 1390, 10);
                    itemqua2.itemOptions.add(new ItemOption(216, 8000));
                    itemqua2.itemOptions.add(new ItemOption(30, 1));

                    itemqua3 = ItemService.gI().createNewItem((short) 1391, 1);
                    itemqua3.itemOptions.add(new ItemOption(217, 10));
                    itemqua3.itemOptions.add(new ItemOption(30, 500));

                    itemqua4 = ItemService.gI().createNewItem((short) 981, 1);
                    itemqua4.itemOptions.add(new ItemOption(31, 1));

                    Service.gI().sendThongBaoFromAdmin(player, "Bạn Đã Nhận Được 10 Đá Cổ Ngọc Vạn Sắc Khảm Trang Bị\n"
                            + "Vui Lòng Dùng Hết Đồ Mốc Thấp \nof Sao Pha Lê Hiện Có,Trong Hành Trang Mới Được Nhận Mốc Tiếp Theo\n"
                            + "Nếu Cố Tình Sẽ Lập Tức Mất Mốc Này Và Không Nhận Thêm Được Gì\n"
                            + "Không Hỗ Trợ Nhận Lai Mốc , Mỗi Mùa Được Cày Lại\n"
                            + "Max Pass vip 500 Triệu Điểm");
                    InventoryService.gI().addItemBag(player, itemqua, 10);
                    InventoryService.gI().addItemBag(player, itemqua1, 10);
                    InventoryService.gI().addItemBag(player, itemqua2, 10);
                    InventoryService.gI().addItemBag(player, itemqua3, 1);
                    InventoryService.gI().addItemBag(player, itemqua4, 1);
                    InventoryService.gI().sendItemBags(player);
                } else if (player.premium >= 2000000 && player.mocpassvip == 1500000) {
                    Service.gI().sendThongBao(player, "Bắt Đầu\n Nhận Mốc Pass Tiếp Theo\nSau " + time + " Giây!");
                    while (time > 0) {
                        time--;
                        Thread.sleep(1000);
                        Service.gI().sendThongBao(player, "|7|" + time);
                    }
                    player.mocpassvip = 2000000;
                    itemqua = ItemService.gI().createNewItem((short) 1388, 10);
                    itemqua.itemOptions.add(new ItemOption(214, 9000));
                    itemqua.itemOptions.add(new ItemOption(30, 1));

                    itemqua1 = ItemService.gI().createNewItem((short) 1389, 10);
                    itemqua1.itemOptions.add(new ItemOption(215, 9000));
                    itemqua1.itemOptions.add(new ItemOption(30, 1));

                    itemqua2 = ItemService.gI().createNewItem((short) 1390, 10);
                    itemqua2.itemOptions.add(new ItemOption(216, 9000));
                    itemqua2.itemOptions.add(new ItemOption(30, 1));

                    itemqua3 = ItemService.gI().createNewItem((short) 1391, 1);
                    itemqua3.itemOptions.add(new ItemOption(217, 10));
                    itemqua3.itemOptions.add(new ItemOption(30, 500));

                    itemqua4 = ItemService.gI().createNewItem((short) 981, 1);
                    itemqua4.itemOptions.add(new ItemOption(31, 1));

                    Service.gI().sendThongBaoFromAdmin(player, "Bạn Đã Nhận Được 10 Đá Cổ Ngọc Vạn Sắc Khảm Trang Bị\n"
                            + "Vui Lòng Dùng Hết Đồ Mốc Thấp \nof Sao Pha Lê Hiện Có,Trong Hành Trang Mới Được Nhận Mốc Tiếp Theo\n"
                            + "Nếu Cố Tình Sẽ Lập Tức Mất Mốc Này Và Không Nhận Thêm Được Gì\n"
                            + "Không Hỗ Trợ Nhận Lai Mốc , Mỗi Mùa Được Cày Lại\n"
                            + "Max Pass vip 500 Triệu Điểm");
                    InventoryService.gI().addItemBag(player, itemqua, 10);
                    InventoryService.gI().addItemBag(player, itemqua1, 10);
                    InventoryService.gI().addItemBag(player, itemqua2, 10);
                    InventoryService.gI().addItemBag(player, itemqua3, 1);
                    InventoryService.gI().addItemBag(player, itemqua4, 1);
                    InventoryService.gI().sendItemBags(player);
                } else if (player.premium >= 2500000 && player.mocpassvip == 2000000) {
                    Service.gI().sendThongBao(player, "Bắt Đầu\n Nhận Mốc Pass Tiếp Theo\nSau " + time + " Giây!");
                    while (time > 0) {
                        time--;
                        Thread.sleep(1000);
                        Service.gI().sendThongBao(player, "|7|" + time);
                    }
                    player.mocpassvip = 2500000;
                    itemqua = ItemService.gI().createNewItem((short) 1388, 10);
                    itemqua.itemOptions.add(new ItemOption(214, 10000));
                    itemqua.itemOptions.add(new ItemOption(30, 1));

                    itemqua1 = ItemService.gI().createNewItem((short) 1389, 10);
                    itemqua1.itemOptions.add(new ItemOption(215, 10000));
                    itemqua1.itemOptions.add(new ItemOption(30, 1));

                    itemqua2 = ItemService.gI().createNewItem((short) 1390, 10);
                    itemqua2.itemOptions.add(new ItemOption(216, 10000));
                    itemqua2.itemOptions.add(new ItemOption(30, 1));

                    itemqua3 = ItemService.gI().createNewItem((short) 1391, 1);
                    itemqua3.itemOptions.add(new ItemOption(217, 10));
                    itemqua3.itemOptions.add(new ItemOption(30, 500));

                    itemqua4 = ItemService.gI().createNewItem((short) 981, 1);
                    itemqua4.itemOptions.add(new ItemOption(31, 1));

                    Service.gI().sendThongBaoFromAdmin(player, "Bạn Đã Nhận Được 10 Đá Cổ Ngọc Vạn Sắc Khảm Trang Bị\n"
                            + "Vui Lòng Dùng Hết Đồ Mốc Thấp \nof Sao Pha Lê Hiện Có,Trong Hành Trang Mới Được Nhận Mốc Tiếp Theo\n"
                            + "Nếu Cố Tình Sẽ Lập Tức Mất Mốc Này Và Không Nhận Thêm Được Gì\n"
                            + "Không Hỗ Trợ Nhận Lai Mốc , Mỗi Mùa Được Cày Lại\n"
                            + "Max Pass vip 500 Triệu Điểm");
                    InventoryService.gI().addItemBag(player, itemqua, 10);
                    InventoryService.gI().addItemBag(player, itemqua1, 10);
                    InventoryService.gI().addItemBag(player, itemqua2, 10);
                    InventoryService.gI().addItemBag(player, itemqua3, 1);
                    InventoryService.gI().addItemBag(player, itemqua4, 1);
                    InventoryService.gI().sendItemBags(player);
                } else if (player.premium >= 3000000 && player.mocpassvip == 2500000) {
                    Service.gI().sendThongBao(player, "Bắt Đầu\n Nhận Mốc Pass Tiếp Theo\nSau " + time + " Giây!");
                    while (time > 0) {
                        time--;
                        Thread.sleep(1000);
                        Service.gI().sendThongBao(player, "|7|" + time);
                    }
                    player.mocpassvip = 3000000;
                    itemqua = ItemService.gI().createNewItem((short) 1388, 10);
                    itemqua.itemOptions.add(new ItemOption(214, 11000));
                    itemqua.itemOptions.add(new ItemOption(30, 1));

                    itemqua1 = ItemService.gI().createNewItem((short) 1389, 10);
                    itemqua1.itemOptions.add(new ItemOption(215, 11000));
                    itemqua1.itemOptions.add(new ItemOption(30, 1));

                    itemqua2 = ItemService.gI().createNewItem((short) 1390, 10);
                    itemqua2.itemOptions.add(new ItemOption(216, 11000));
                    itemqua2.itemOptions.add(new ItemOption(30, 1));

                    itemqua3 = ItemService.gI().createNewItem((short) 1391, 1);
                    itemqua3.itemOptions.add(new ItemOption(217, 10));
                    itemqua3.itemOptions.add(new ItemOption(30, 500));

                    itemqua4 = ItemService.gI().createNewItem((short) 981, 1);
                    itemqua4.itemOptions.add(new ItemOption(31, 1));

                    Service.gI().sendThongBaoFromAdmin(player, "Bạn Đã Nhận Được 10 Đá Cổ Ngọc Vạn Sắc Khảm Trang Bị\n"
                            + "Vui Lòng Dùng Hết Đồ Mốc Thấp \nof Sao Pha Lê Hiện Có,Trong Hành Trang Mới Được Nhận Mốc Tiếp Theo\n"
                            + "Nếu Cố Tình Sẽ Lập Tức Mất Mốc Này Và Không Nhận Thêm Được Gì\n"
                            + "Không Hỗ Trợ Nhận Lai Mốc , Mỗi Mùa Được Cày Lại\n"
                            + "Max Pass vip 500 Triệu Điểm");
                    InventoryService.gI().addItemBag(player, itemqua, 10);
                    InventoryService.gI().addItemBag(player, itemqua1, 10);
                    InventoryService.gI().addItemBag(player, itemqua2, 10);
                    InventoryService.gI().addItemBag(player, itemqua3, 1);
                    InventoryService.gI().addItemBag(player, itemqua4, 1);
                    InventoryService.gI().sendItemBags(player);
                } else if (player.premium >= 3500000 && player.mocpassvip == 3000000) {
                    Service.gI().sendThongBao(player, "Bắt Đầu\n Nhận Mốc Pass Tiếp Theo\nSau " + time + " Giây!");
                    while (time > 0) {
                        time--;
                        Thread.sleep(1000);
                        Service.gI().sendThongBao(player, "|7|" + time);
                    }
                    player.mocpassvip = 3500000;
                    itemqua = ItemService.gI().createNewItem((short) 1388, 10);
                    itemqua.itemOptions.add(new ItemOption(214, 12000));
                    itemqua.itemOptions.add(new ItemOption(30, 1));

                    itemqua1 = ItemService.gI().createNewItem((short) 1389, 10);
                    itemqua1.itemOptions.add(new ItemOption(215, 12000));
                    itemqua1.itemOptions.add(new ItemOption(30, 1));

                    itemqua2 = ItemService.gI().createNewItem((short) 1390, 10);
                    itemqua2.itemOptions.add(new ItemOption(216, 12000));
                    itemqua2.itemOptions.add(new ItemOption(30, 1));

                    itemqua3 = ItemService.gI().createNewItem((short) 1391, 1);
                    itemqua3.itemOptions.add(new ItemOption(217, 10));
                    itemqua3.itemOptions.add(new ItemOption(30, 500));

                    itemqua4 = ItemService.gI().createNewItem((short) 981, 1);
                    itemqua4.itemOptions.add(new ItemOption(31, 1));

                    Service.gI().sendThongBaoFromAdmin(player, "Bạn Đã Nhận Được 10 Đá Cổ Ngọc Vạn Sắc Khảm Trang Bị\n"
                            + "Vui Lòng Dùng Hết Đồ Mốc Thấp \nof Sao Pha Lê Hiện Có,Trong Hành Trang Mới Được Nhận Mốc Tiếp Theo\n"
                            + "Nếu Cố Tình Sẽ Lập Tức Mất Mốc Này Và Không Nhận Thêm Được Gì\n"
                            + "Không Hỗ Trợ Nhận Lai Mốc , Mỗi Mùa Được Cày Lại\n"
                            + "Max Pass vip 500 Triệu Điểm");
                    InventoryService.gI().addItemBag(player, itemqua, 10);
                    InventoryService.gI().addItemBag(player, itemqua1, 10);
                    InventoryService.gI().addItemBag(player, itemqua2, 10);
                    InventoryService.gI().addItemBag(player, itemqua3, 1);
                    InventoryService.gI().addItemBag(player, itemqua4, 1);
                    InventoryService.gI().sendItemBags(player);
                } else if (player.premium >= 4000000 && player.mocpassvip == 3500000) {
                    Service.gI().sendThongBao(player, "Bắt Đầu\n Nhận Mốc Pass Tiếp Theo\nSau " + time + " Giây!");
                    while (time > 0) {
                        time--;
                        Thread.sleep(1000);
                        Service.gI().sendThongBao(player, "|7|" + time);
                    }
                    player.mocpassvip = 4000000;
                    itemqua = ItemService.gI().createNewItem((short) 1388, 10);
                    itemqua.itemOptions.add(new ItemOption(214, 13000));
                    itemqua.itemOptions.add(new ItemOption(30, 1));

                    itemqua1 = ItemService.gI().createNewItem((short) 1389, 10);
                    itemqua1.itemOptions.add(new ItemOption(215, 13000));
                    itemqua1.itemOptions.add(new ItemOption(30, 1));

                    itemqua2 = ItemService.gI().createNewItem((short) 1390, 10);
                    itemqua2.itemOptions.add(new ItemOption(216, 13000));
                    itemqua2.itemOptions.add(new ItemOption(30, 1));

                    itemqua3 = ItemService.gI().createNewItem((short) 1391, 1);
                    itemqua3.itemOptions.add(new ItemOption(217, 10));
                    itemqua3.itemOptions.add(new ItemOption(30, 500));

                    itemqua4 = ItemService.gI().createNewItem((short) 981, 1);
                    itemqua4.itemOptions.add(new ItemOption(31, 1));

                    Service.gI().sendThongBaoFromAdmin(player, "Bạn Đã Nhận Được 10 Đá Cổ Ngọc Vạn Sắc Khảm Trang Bị\n"
                            + "Vui Lòng Dùng Hết Đồ Mốc Thấp \nof Sao Pha Lê Hiện Có,Trong Hành Trang Mới Được Nhận Mốc Tiếp Theo\n"
                            + "Nếu Cố Tình Sẽ Lập Tức Mất Mốc Này Và Không Nhận Thêm Được Gì\n"
                            + "Không Hỗ Trợ Nhận Lai Mốc , Mỗi Mùa Được Cày Lại\n"
                            + "Max Pass vip 500 Triệu Điểm");
                    InventoryService.gI().addItemBag(player, itemqua, 10);
                    InventoryService.gI().addItemBag(player, itemqua1, 10);
                    InventoryService.gI().addItemBag(player, itemqua2, 10);
                    InventoryService.gI().addItemBag(player, itemqua3, 1);
                    InventoryService.gI().addItemBag(player, itemqua4, 1);
                    InventoryService.gI().sendItemBags(player);

                } else if (player.premium >= 4500000 && player.mocpassvip == 4000000) {
                    Service.gI().sendThongBao(player, "Bắt Đầu\n Nhận Mốc Pass Tiếp Theo\nSau " + time + " Giây!");
                    while (time > 0) {
                        time--;
                        Thread.sleep(1000);
                        Service.gI().sendThongBao(player, "|7|" + time);
                    }
                    player.mocpassvip = 4500000;
                    itemqua = ItemService.gI().createNewItem((short) 1388, 10);
                    itemqua.itemOptions.add(new ItemOption(214, 14000));
                    itemqua.itemOptions.add(new ItemOption(30, 1));

                    itemqua1 = ItemService.gI().createNewItem((short) 1389, 10);
                    itemqua1.itemOptions.add(new ItemOption(215, 14000));
                    itemqua1.itemOptions.add(new ItemOption(30, 1));

                    itemqua2 = ItemService.gI().createNewItem((short) 1390, 10);
                    itemqua2.itemOptions.add(new ItemOption(216, 14000));
                    itemqua2.itemOptions.add(new ItemOption(30, 1));

                    itemqua3 = ItemService.gI().createNewItem((short) 1391, 1);
                    itemqua3.itemOptions.add(new ItemOption(217, 10));
                    itemqua3.itemOptions.add(new ItemOption(30, 500));

                    itemqua4 = ItemService.gI().createNewItem((short) 981, 1);
                    itemqua4.itemOptions.add(new ItemOption(31, 1));

                    Service.gI().sendThongBaoFromAdmin(player, "Bạn Đã Nhận Được 10 Đá Cổ Ngọc Vạn Sắc Khảm Trang Bị\n"
                            + "Vui Lòng Dùng Hết Đồ Mốc Thấp \nof Sao Pha Lê Hiện Có,Trong Hành Trang Mới Được Nhận Mốc Tiếp Theo\n"
                            + "Nếu Cố Tình Sẽ Lập Tức Mất Mốc Này Và Không Nhận Thêm Được Gì\n"
                            + "Không Hỗ Trợ Nhận Lai Mốc , Mỗi Mùa Được Cày Lại\n"
                            + "Max Pass vip 500 Triệu Điểm");
                    InventoryService.gI().addItemBag(player, itemqua, 10);
                    InventoryService.gI().addItemBag(player, itemqua1, 10);
                    InventoryService.gI().addItemBag(player, itemqua2, 10);
                    InventoryService.gI().addItemBag(player, itemqua3, 1);
                    InventoryService.gI().addItemBag(player, itemqua4, 1);
                    InventoryService.gI().sendItemBags(player);
                } else if (player.premium >= 5000000 && player.mocpassvip == 4500000) {
                    Service.gI().sendThongBao(player, "Bắt Đầu\n Nhận Mốc Pass Tiếp Theo\nSau " + time + " Giây!");
                    while (time > 0) {
                        time--;
                        Thread.sleep(1000);
                        Service.gI().sendThongBao(player, "|7|" + time);
                    }
                    player.mocpassvip = 5000000;
                    itemqua = ItemService.gI().createNewItem((short) 1388, 10);
                    itemqua.itemOptions.add(new ItemOption(214, 15000));
                    itemqua.itemOptions.add(new ItemOption(30, 1));

                    itemqua1 = ItemService.gI().createNewItem((short) 1389, 10);
                    itemqua1.itemOptions.add(new ItemOption(215, 15000));
                    itemqua1.itemOptions.add(new ItemOption(30, 1));

                    itemqua2 = ItemService.gI().createNewItem((short) 1390, 10);
                    itemqua2.itemOptions.add(new ItemOption(216, 15000));
                    itemqua2.itemOptions.add(new ItemOption(30, 1));

                    itemqua3 = ItemService.gI().createNewItem((short) 1391, 1);
                    itemqua3.itemOptions.add(new ItemOption(217, 10));
                    itemqua3.itemOptions.add(new ItemOption(30, 500));

                    itemqua4 = ItemService.gI().createNewItem((short) 981, 1);
                    itemqua4.itemOptions.add(new ItemOption(31, 1));

                    Service.gI().sendThongBaoFromAdmin(player, "Bạn Đã Nhận Được 10 Đá Cổ Ngọc Vạn Sắc Khảm Trang Bị\n"
                            + "Vui Lòng Dùng Hết Đồ Mốc Thấp \nof Sao Pha Lê Hiện Có,Trong Hành Trang Mới Được Nhận Mốc Tiếp Theo\n"
                            + "Nếu Cố Tình Sẽ Lập Tức Mất Mốc Này Và Không Nhận Thêm Được Gì\n"
                            + "Không Hỗ Trợ Nhận Lai Mốc , Mỗi Mùa Được Cày Lại\n"
                            + "Max Pass vip 500 Triệu Điểm");
                    InventoryService.gI().addItemBag(player, itemqua, 10);
                    InventoryService.gI().addItemBag(player, itemqua1, 10);
                    InventoryService.gI().addItemBag(player, itemqua2, 10);
                    InventoryService.gI().addItemBag(player, itemqua3, 1);
                    InventoryService.gI().addItemBag(player, itemqua4, 1);
                    InventoryService.gI().sendItemBags(player);
                } else if (player.premium >= 5500000 && player.mocpassvip == 5000000) {
                    Service.gI().sendThongBao(player, "Bắt Đầu\n Nhận Mốc Pass Tiếp Theo\nSau " + time + " Giây!");
                    while (time > 0) {
                        time--;
                        Thread.sleep(1000);
                        Service.gI().sendThongBao(player, "|7|" + time);
                    }
                    player.mocpass = 5500000;
                    itemqua = ItemService.gI().createNewItem((short) 1388, 10);
                    itemqua.itemOptions.add(new ItemOption(214, 16000));
                    itemqua.itemOptions.add(new ItemOption(30, 1));

                    itemqua1 = ItemService.gI().createNewItem((short) 1389, 10);
                    itemqua1.itemOptions.add(new ItemOption(215, 16000));
                    itemqua1.itemOptions.add(new ItemOption(30, 1));

                    itemqua2 = ItemService.gI().createNewItem((short) 1390, 10);
                    itemqua2.itemOptions.add(new ItemOption(216, 16000));
                    itemqua2.itemOptions.add(new ItemOption(30, 1));

                    itemqua3 = ItemService.gI().createNewItem((short) 1391, 1);
                    itemqua3.itemOptions.add(new ItemOption(217, 10));
                    itemqua3.itemOptions.add(new ItemOption(30, 500));

                    itemqua4 = ItemService.gI().createNewItem((short) 981, 1);
                    itemqua4.itemOptions.add(new ItemOption(31, 1));

                    Service.gI().sendThongBaoFromAdmin(player, "Bạn Đã Nhận Được 10 Đá Cổ Ngọc Vạn Sắc Khảm Trang Bị\n"
                            + "Vui Lòng Dùng Hết Đồ Mốc Thấp \nof Sao Pha Lê Hiện Có,Trong Hành Trang Mới Được Nhận Mốc Tiếp Theo\n"
                            + "Nếu Cố Tình Sẽ Lập Tức Mất Mốc Này Và Không Nhận Thêm Được Gì\n"
                            + "Không Hỗ Trợ Nhận Lai Mốc , Mỗi Mùa Được Cày Lại\n"
                            + "Max Pass vip 500 Triệu Điểm");
                    InventoryService.gI().addItemBag(player, itemqua, 10);
                    InventoryService.gI().addItemBag(player, itemqua1, 10);
                    InventoryService.gI().addItemBag(player, itemqua2, 10);
                    InventoryService.gI().addItemBag(player, itemqua3, 1);
                    InventoryService.gI().addItemBag(player, itemqua4, 1);
                    InventoryService.gI().sendItemBags(player);

                } else if (player.premium >= 6000000 && player.mocpassvip == 5500000) {
                    Service.gI().sendThongBao(player, "Bắt Đầu\n Nhận Mốc Pass Tiếp Theo\nSau " + time + " Giây!");
                    while (time > 0) {
                        time--;
                        Thread.sleep(1000);
                        Service.gI().sendThongBao(player, "|7|" + time);
                    }
                    player.mocpassvip = 6000000;
                    itemqua = ItemService.gI().createNewItem((short) 1388, 10);
                    itemqua.itemOptions.add(new ItemOption(214, 17000));
                    itemqua.itemOptions.add(new ItemOption(30, 1));

                    itemqua1 = ItemService.gI().createNewItem((short) 1389, 10);
                    itemqua1.itemOptions.add(new ItemOption(215, 17000));
                    itemqua1.itemOptions.add(new ItemOption(30, 1));

                    itemqua2 = ItemService.gI().createNewItem((short) 1390, 10);
                    itemqua2.itemOptions.add(new ItemOption(216, 17000));
                    itemqua2.itemOptions.add(new ItemOption(30, 1));

                    itemqua3 = ItemService.gI().createNewItem((short) 1391, 1);
                    itemqua3.itemOptions.add(new ItemOption(217, 10));
                    itemqua3.itemOptions.add(new ItemOption(30, 500));

                    itemqua4 = ItemService.gI().createNewItem((short) 981, 1);
                    itemqua4.itemOptions.add(new ItemOption(31, 1));

                    Service.gI().sendThongBaoFromAdmin(player, "Bạn Đã Nhận Được 10 Đá Cổ Ngọc Vạn Sắc Khảm Trang Bị\n"
                            + "Vui Lòng Dùng Hết Đồ Mốc Thấp \nof Sao Pha Lê Hiện Có,Trong Hành Trang Mới Được Nhận Mốc Tiếp Theo\n"
                            + "Nếu Cố Tình Sẽ Lập Tức Mất Mốc Này Và Không Nhận Thêm Được Gì\n"
                            + "Không Hỗ Trợ Nhận Lai Mốc , Mỗi Mùa Được Cày Lại\n"
                            + "Max Pass vip 500 Triệu Điểm");
                    InventoryService.gI().addItemBag(player, itemqua, 10);
                    InventoryService.gI().addItemBag(player, itemqua1, 10);
                    InventoryService.gI().addItemBag(player, itemqua2, 10);
                    InventoryService.gI().addItemBag(player, itemqua3, 1);
                    InventoryService.gI().addItemBag(player, itemqua4, 1);
                    InventoryService.gI().sendItemBags(player);
                } else if (player.premium >= 6500000 && player.mocpassvip == 5500000) {
                    Service.gI().sendThongBao(player, "Bắt Đầu\n Nhận Mốc Pass Tiếp Theo\nSau " + time + " Giây!");
                    while (time > 0) {
                        time--;
                        Thread.sleep(1000);
                        Service.gI().sendThongBao(player, "|7|" + time);
                    }
                    player.mocpassvip = 6500000;
                    itemqua = ItemService.gI().createNewItem((short) 1388, 10);
                    itemqua.itemOptions.add(new ItemOption(214, 18000));
                    itemqua.itemOptions.add(new ItemOption(30, 1));

                    itemqua1 = ItemService.gI().createNewItem((short) 1389, 10);
                    itemqua1.itemOptions.add(new ItemOption(215, 18000));
                    itemqua1.itemOptions.add(new ItemOption(30, 1));

                    itemqua2 = ItemService.gI().createNewItem((short) 1390, 10);
                    itemqua2.itemOptions.add(new ItemOption(216, 18000));
                    itemqua2.itemOptions.add(new ItemOption(30, 1));

                    itemqua3 = ItemService.gI().createNewItem((short) 1391, 1);
                    itemqua3.itemOptions.add(new ItemOption(217, 10));
                    itemqua3.itemOptions.add(new ItemOption(30, 500));

                    itemqua4 = ItemService.gI().createNewItem((short) 981, 1);
                    itemqua4.itemOptions.add(new ItemOption(31, 1));

                    Service.gI().sendThongBaoFromAdmin(player, "Bạn Đã Nhận Được 10 Đá Cổ Ngọc Vạn Sắc Khảm Trang Bị\n"
                            + "Vui Lòng Dùng Hết Đồ Mốc Thấp \nof Sao Pha Lê Hiện Có,Trong Hành Trang Mới Được Nhận Mốc Tiếp Theo\n"
                            + "Nếu Cố Tình Sẽ Lập Tức Mất Mốc Này Và Không Nhận Thêm Được Gì\n"
                            + "Không Hỗ Trợ Nhận Lai Mốc , Mỗi Mùa Được Cày Lại\n"
                            + "Max Pass vip 500 Triệu Điểm");
                    InventoryService.gI().addItemBag(player, itemqua, 10);
                    InventoryService.gI().addItemBag(player, itemqua1, 10);
                    InventoryService.gI().addItemBag(player, itemqua2, 10);
                    InventoryService.gI().addItemBag(player, itemqua3, 1);
                    InventoryService.gI().addItemBag(player, itemqua4, 1);
                    InventoryService.gI().sendItemBags(player);

                } else if (player.premium >= 7000000 && player.mocpassvip == 6500000) {
                    Service.gI().sendThongBao(player, "Bắt Đầu\n Nhận Mốc Pass Tiếp Theo\nSau " + time + " Giây!");
                    while (time > 0) {
                        time--;
                        Thread.sleep(1000);
                        Service.gI().sendThongBao(player, "|7|" + time);
                    }
                    player.mocpassvip = 7000000;
                    itemqua = ItemService.gI().createNewItem((short) 1388, 10);
                    itemqua.itemOptions.add(new ItemOption(214, 19000));
                    itemqua.itemOptions.add(new ItemOption(30, 1));

                    itemqua1 = ItemService.gI().createNewItem((short) 1389, 10);
                    itemqua1.itemOptions.add(new ItemOption(215, 19000));
                    itemqua1.itemOptions.add(new ItemOption(30, 1));

                    itemqua2 = ItemService.gI().createNewItem((short) 1390, 10);
                    itemqua2.itemOptions.add(new ItemOption(216, 19000));
                    itemqua2.itemOptions.add(new ItemOption(30, 1));

                    itemqua3 = ItemService.gI().createNewItem((short) 1391, 1);
                    itemqua3.itemOptions.add(new ItemOption(217, 10));
                    itemqua3.itemOptions.add(new ItemOption(30, 500));

                    itemqua4 = ItemService.gI().createNewItem((short) 981, 1);
                    itemqua4.itemOptions.add(new ItemOption(31, 1));

                    Service.gI().sendThongBaoFromAdmin(player, "Bạn Đã Nhận Được 10 Đá Cổ Ngọc Vạn Sắc Khảm Trang Bị\n"
                            + "Vui Lòng Dùng Hết Đồ Mốc Thấp \nof Sao Pha Lê Hiện Có,Trong Hành Trang Mới Được Nhận Mốc Tiếp Theo\n"
                            + "Nếu Cố Tình Sẽ Lập Tức Mất Mốc Này Và Không Nhận Thêm Được Gì\n"
                            + "Không Hỗ Trợ Nhận Lai Mốc , Mỗi Mùa Được Cày Lại\n"
                            + "Max Pass vip 500 Triệu Điểm");
                    InventoryService.gI().addItemBag(player, itemqua, 10);
                    InventoryService.gI().addItemBag(player, itemqua1, 10);
                    InventoryService.gI().addItemBag(player, itemqua2, 10);
                    InventoryService.gI().addItemBag(player, itemqua3, 1);
                    InventoryService.gI().addItemBag(player, itemqua4, 1);
                    InventoryService.gI().sendItemBags(player);
                } else if (player.premium >= 7500000 && player.mocpassvip == 6500000) {
                    Service.gI().sendThongBao(player, "Bắt Đầu\n Nhận Mốc Pass Tiếp Theo\nSau " + time + " Giây!");
                    while (time > 0) {
                        time--;
                        Thread.sleep(1000);
                        Service.gI().sendThongBao(player, "|7|" + time);
                    }
                    player.mocpassvip = 7500000;
                    itemqua = ItemService.gI().createNewItem((short) 1388, 10);
                    itemqua.itemOptions.add(new ItemOption(214, 20000));
                    itemqua.itemOptions.add(new ItemOption(30, 1));

                    itemqua1 = ItemService.gI().createNewItem((short) 1389, 10);
                    itemqua1.itemOptions.add(new ItemOption(215, 20000));
                    itemqua1.itemOptions.add(new ItemOption(30, 1));

                    itemqua2 = ItemService.gI().createNewItem((short) 1390, 10);
                    itemqua2.itemOptions.add(new ItemOption(216, 20000));
                    itemqua2.itemOptions.add(new ItemOption(30, 1));

                    itemqua3 = ItemService.gI().createNewItem((short) 1391, 1);
                    itemqua3.itemOptions.add(new ItemOption(217, 10));
                    itemqua3.itemOptions.add(new ItemOption(30, 500));

                    itemqua4 = ItemService.gI().createNewItem((short) 981, 1);
                    itemqua4.itemOptions.add(new ItemOption(31, 1));

                    Service.gI().sendThongBaoFromAdmin(player, "Bạn Đã Nhận Được 10 Đá Cổ Ngọc Vạn Sắc Khảm Trang Bị\n"
                            + "Vui Lòng Dùng Hết Đồ Mốc Thấp \nof Sao Pha Lê Hiện Có,Trong Hành Trang Mới Được Nhận Mốc Tiếp Theo\n"
                            + "Nếu Cố Tình Sẽ Lập Tức Mất Mốc Này Và Không Nhận Thêm Được Gì\n"
                            + "Không Hỗ Trợ Nhận Lai Mốc , Mỗi Mùa Được Cày Lại\n"
                            + "Max Pass vip 500 Triệu Điểm");
                    InventoryService.gI().addItemBag(player, itemqua, 10);
                    InventoryService.gI().addItemBag(player, itemqua1, 10);
                    InventoryService.gI().addItemBag(player, itemqua2, 10);
                    InventoryService.gI().addItemBag(player, itemqua3, 1);
                    InventoryService.gI().addItemBag(player, itemqua4, 1);
                    InventoryService.gI().sendItemBags(player);

                } else {
                    Service.gI().sendThongBaoFromAdmin(player, "Bạn Chưa Đủ Điều Kiện Nhận Mốc Pass vip Này!");
                }
            } catch (Exception e) {
            }
        } else {
            Service.getInstance().sendThongBaoFromAdmin(player, "Mày phải có ít nhất  Trống 6 ô trống hành trang");
        }
    }

    private void openPhieuCaiTrangHaiTac(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            Item ct = ItemService.gI().createNewItem((short) Util.nextInt(618, 626));
            ct.itemOptions.add(new ItemOption(147, 3));
            ct.itemOptions.add(new ItemOption(77, 3));
            ct.itemOptions.add(new ItemOption(103, 3));
            ct.itemOptions.add(new ItemOption(149, 0));
            if (item.template.id == 2006) {
                ct.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
            } else if (item.template.id == 2007) {
                ct.itemOptions.add(new ItemOption(93, Util.nextInt(7, 30)));
            }
            InventoryService.gI().addItemBag(pl, ct, 0);
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBags(pl);
            CombineServiceNew.gI().sendEffectOpenItem(pl, item.template.iconID, ct.template.iconID);
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }

    private void openCTHIT(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            Item ct = ItemService.gI().createNewItem((short) 884, 1);
            ct.itemOptions.add(new ItemOption(147, Util.nextInt(0, 200)));
            ct.itemOptions.add(new ItemOption(77, Util.nextInt(5, 200)));
            ct.itemOptions.add(new ItemOption(103, Util.nextInt(5, 200)));
            ct.itemOptions.add(new ItemOption(5, Util.nextInt(5, 30)));
            ct.itemOptions.add(new ItemOption(14, Util.nextInt(5, 30)));
            // Thay đổi tỉ lệ xuất hiện của lựa chọn 93
            if (Util.nextInt(1, 100) > 1) {
                ct.itemOptions.add(new ItemOption(93, Util.nextInt(1, 2)));
            }
            InventoryService.gI().addItemBag(pl, ct, 0);
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBags(pl);
            CombineServiceNew.gI().sendEffectOpenItem(pl, item.template.iconID, ct.template.iconID);
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }

    public void eatGrapes(Player pl, Item item) {
        int percentCurrentStatima = pl.nPoint.stamina * 100 / pl.nPoint.maxStamina;
        if (percentCurrentStatima > 50) {
            Service.getInstance().sendThongBao(pl, "Thể lực vẫn còn trên 50%");
            return;
        } else if (item.template.id == 211) {
            pl.nPoint.stamina = pl.nPoint.maxStamina;
            Service.getInstance().sendThongBao(pl, "Thể lực của bạn đã được hồi phục 100%");
        } else if (item.template.id == 212) {
            pl.nPoint.stamina += (pl.nPoint.maxStamina * 20 / 100);
            Service.getInstance().sendThongBao(pl, "Thể lực của bạn đã được hồi phục 20%");
        }
        InventoryService.gI().subQuantityItemsBag(pl, item, 1);
        InventoryService.gI().sendItemBags(pl);
        PlayerService.gI().sendCurrentStamina(pl);
    }

    private void openCSKB(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            short[] temp = {76, 188, 189, 190, 381, 382, 383, 384, 385};
            int[][] gold = {{5000, 20000}};
            byte index = (byte) Util.nextInt(0, temp.length - 1);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            if (index <= 3) {
                pl.inventory.addGold(Util.nextInt(gold[0][0], gold[0][1]));
                PlayerService.gI().sendInfoHpMpMoney(pl);
                icon[1] = 930;
            } else {
                Item it = ItemService.gI().createNewItem(temp[index]);
                it.itemOptions.add(new ItemOption(73, 0));
                InventoryService.gI().addItemBag(pl, it, 0);
                icon[1] = it.template.iconID;
            }
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBags(pl);

            CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }

    private void openskill9(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            short[] temp = {1426, 1427, 1428, 1429, 1430, 1431, 1432, 1433, 1434, 1435, 1436, 1437, 1438, 1439, 1440, 1441, 1442, 1443, 1444, 1445, 1446};
            int[][] gold = {{5000, 2000000000}};
            byte index = (byte) Util.nextInt(0, temp.length - 1);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            if (index <= 3) {
                pl.inventory.addGold(Util.nextInt(gold[0][0], gold[0][1]));
                PlayerService.gI().sendInfoHpMpMoney(pl);
                icon[1] = 930;
            } else {
                Item it = ItemService.gI().createNewItem(temp[index]);
                it.itemOptions.add(new ItemOption(73, 0));
                InventoryService.gI().addItemBag(pl, it, 0);
                icon[1] = it.template.iconID;
            }
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBags(pl);

            CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }

    private void useHopBanhTrungThu(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) == 0) {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");
            return;
        }
        int[] listItemRandom = {76, 18, 19, 20, 472, 890, 891, 465, 466};
        Item itemRandom;
        if (Util.isTrue(80, 100)) {
            int[] goldRandom = {10000, 100000, 1000000, 10000000, 100000000};
            itemRandom = ItemService.gI().createNewItem((short) listItemRandom[0]);
            itemRandom.quantity = goldRandom[Util.nextInt(goldRandom.length - 1)];
        } else if (Util.isTrue(60, 100)) {
            itemRandom = ItemService.gI().createNewItem((short) listItemRandom[Util.nextInt(1, 3)]);
        } else {
            itemRandom = ItemService.gI().createNewItem((short) listItemRandom[Util.nextInt(4, listItemRandom.length - 1)]);
        }
        if (itemRandom != null) {
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().addItemBag(pl, itemRandom, 999999);
            InventoryService.gI().sendItemBags(pl);
            CombineServiceNew.gI().sendEffectOpenItem(pl, item.template.iconID, itemRandom.template.iconID);
            Service.gI().sendThongBao(pl, "Bạn vừa nhận được " + itemRandom.template.name);
        }
    }

    public void capsuvang(Player player) {
        try {
            if (InventoryService.gI().getCountEmptyBag(player) <= 2) {
                Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 3 ô trống hành trang");
                return;
            }
            short[] icon = new short[2];
            Item ruonggioto = null;
            for (Item item : player.inventory.itemsBag) {
                if (item.isNotNullItem() && item.template.id == 574) {
                    ruonggioto = item;
                    break;
                }
            }
            if (ruonggioto != null) {
                int rd2 = Util.nextInt(0, 100);
                int rac2 = 60;
                int ruby2 = 15;
                int tv2 = 20;
                int ct2 = 5;
                Item item = randomRac2(true);
                if (rd2 <= rac2) {
                    item = randomRac2(true);
                } else if (rd2 <= rac2 + ruby2) {
                    item = hongngocrdv2();
                } else if (rd2 <= rac2 + ruby2 + tv2) {
                    item = thoivangrdv2(true);
                } else if (rd2 <= rac2 + ruby2 + tv2 + ct2) {
                    item = caitrangrdv2(true);
                }
                icon[0] = ruonggioto.template.iconID;
                icon[1] = item.template.iconID;
                InventoryService.gI().subQuantityItemsBag(player, ruonggioto, 1);
                InventoryService.gI().addItemBag(player, item, 9999999);
                InventoryService.gI().sendItemBags(player);
                Service.getInstance().sendThongBao(player, "Bạn đã nhận được " + item.template.name);
                CombineServiceNew.gI().sendEffectOpenItem(player, icon[0], icon[1]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Item randomRac2(boolean rating) { //Random csv
        short[] rac2 = {406, 407, 408, 409, 410, 411, 412, 413, 414, 415, 416, 417, 418, 419, 420, 421, 422, 423, 424, 425, 426, 427, 428, 429, 430, 431, 432, 433};
        Item item = ItemService.gI().createNewItem(rac2[Util.nextInt(rac2.length - 1)], 1);
        item.itemOptions.add(new ItemOption(76, 1));//VIP
        item.itemOptions.add(new ItemOption(50, Util.nextInt(1, 99)));//hp 500%
        item.itemOptions.add(new ItemOption(77, Util.nextInt(1, 99)));//hp 500%
        item.itemOptions.add(new ItemOption(103, Util.nextInt(1, 99)));//hp 500%
        item.itemOptions.add(new ItemOption(78, Util.nextInt(1, 100)));//hp 500%
        item.itemOptions.add(new ItemOption(95, Util.nextInt(1, 5)));//hút hp 500%
        item.itemOptions.add(new ItemOption(96, Util.nextInt(1, 5)));//ki 500%
        item.itemOptions.add(new ItemOption(0, Util.nextInt(1, 2000)));//sd 500%
        item.itemOptions.add(new ItemOption(22, Util.nextInt(1, 20)));//hút ki 500%
        item.itemOptions.add(new ItemOption(23, Util.nextInt(1, 50)));//phản 500%
        item.itemOptions.add(new ItemOption(181, Util.nextInt(1, 20)));// 500%
        item.itemOptions.add(new ItemOption(182, Util.nextInt(1, 20)));//smtn + 500%
        item.itemOptions.add(new ItemOption(183, Util.nextInt(1, 20)));//smtn + 500%
        item.itemOptions.add(new ItemOption(72, Util.nextInt(1, 16)));
        item.itemOptions.add(new ItemOption(106, 0)); //k thể gd
        item.itemOptions.add(new ItemOption(107, 8)); //k ảnh hưởng bới cái lạnh
        if (Util.isTrue(998, 1000) && rating) {// tỉ lệ ra hsd
            item.itemOptions.add(new ItemOption(93, Util.nextInt(5) + 1));//hsd
        }
        return item;
    }

    public Item caitrangrdv2(boolean rating) { //Random csv
        short[] ct2 = {457, 1891, 1892, 1893, 1894, 1895, 1896, 1897, 1898, 1899, 1900, 1901, 1902, 1903, 1904, 1905, 1906, 1907, 1908, 1909};
        Item item = ItemService.gI().createNewItem(ct2[Util.nextInt(ct2.length - 1)], 1);
        item.itemOptions.add(new ItemOption(76, 1));//VIP
        item.itemOptions.add(new ItemOption(50, Util.nextInt(10, 100)));//hp 500%
        item.itemOptions.add(new ItemOption(77, Util.nextInt(10, 100)));//ki 500%
        item.itemOptions.add(new ItemOption(103, Util.nextInt(10, 100)));//sd 500%
        item.itemOptions.add(new ItemOption(94, Util.nextInt(10, 100)));//smtn + 500%
        item.itemOptions.add(new ItemOption(98, Util.nextInt(10, 20)));//smtn + 500%
        item.itemOptions.add(new ItemOption(99, Util.nextInt(10, 20)));//smtn + 500%
        item.itemOptions.add(new ItemOption(72, Util.nextInt(1, 16)));//smtn + 500%
        item.itemOptions.add(new ItemOption(106, 0)); //k ảnh hưởng bới cái lạnh
        if (Util.isTrue(2, 2) && rating) {// tỉ lệ ra hsd
            item.itemOptions.add(new ItemOption(93, Util.nextInt(1) + 1));//hsd
        }
        return item;
    }

    public Item hongngocrdv2() {
        Item item = ItemService.gI().createNewItem((short) 457);
        item.quantity = Util.nextInt(10, 15);
        return item;
    }

    public Item thoivangrdv2(boolean rating) {
        short[] thoivangrdv2 = {14, 15, 16, 17, 18};
        Item item = ItemService.gI().createNewItem(thoivangrdv2[Util.nextInt(thoivangrdv2.length - 1)], 1);
        return item;
    }

    private List<Item> useCapsuleTrungThu() {
        List<Item> listItem = new ArrayList<>();
        Item itemAdd;
        short[] listLongDen = {735, 467, 800, 801, 802, 803, 804};
        short[] listRac = {457, 76, 77, 861, 1277, 1278, 1279};
        int[] goldRandom = {10000, 100000, 1000000, 10000000, 100000000};
        int[] rubyRandom = {100, 200, 300, 400, 500};
        itemAdd = ItemService.gI().createNewItem((short) listLongDen[Util.nextInt(listLongDen.length - 1)]);
        itemAdd.itemOptions.add(new ItemOption(50, Util.nextInt(5, 15)));
        itemAdd.itemOptions.add(new ItemOption(77, Util.nextInt(5, 15)));
        itemAdd.itemOptions.add(new ItemOption(103, Util.nextInt(5, 15)));
        if (Util.isTrue(99, 100)) {
            itemAdd.itemOptions.add(new ItemOption(93, Util.nextInt(5, 15)));
        }
        itemAdd.itemOptions.add(new ItemOption(30, 1));
        listItem.add(itemAdd);
        itemAdd = ItemService.gI().createNewItem((short) 452);
        itemAdd.itemOptions.add(new ItemOption(50, Util.nextInt(1, 200)));
        itemAdd.itemOptions.add(new ItemOption(77, Util.nextInt(1, 200)));
        itemAdd.itemOptions.add(new ItemOption(103, Util.nextInt(1, 200)));
        if (Util.isTrue(99, 100)) {
            itemAdd.itemOptions.add(new ItemOption(93, Util.nextInt(5, 15)));
        }
        itemAdd.itemOptions.add(new ItemOption(30, 1));
        listItem.add(itemAdd);
        itemAdd = ItemService.gI().createNewItem((short) listRac[Util.nextInt(listRac.length - 1)]);
        if (itemAdd.template.id == 77) {
            itemAdd.quantity = goldRandom[Util.nextInt(goldRandom.length - 1)];
        } else if (itemAdd.template.id == 861) {
            itemAdd.quantity = rubyRandom[Util.nextInt(rubyRandom.length - 1)];
        }
        itemAdd.itemOptions.add(new ItemOption(30, 1));
        listItem.add(itemAdd);
        return listItem;
    }

    private void useItemTime(Player pl, Item item) {
        boolean updatePoint = false;
        switch (item.template.id) {
            case 472:
                if (pl.itemTime.isBanhTrungThuDacBiet) {
                    Service.getInstance().sendThongBao(pl, "Chỉ có thể sự dụng cùng lúc 1 vật phẩm bổ trợ cùng loại");
                    return;
                }
                pl.itemTime.lastTimeBanhTrungThuDacBiet = System.currentTimeMillis();
                pl.itemTime.isBanhTrungThuDacBiet = true;
                updatePoint = true;
                break;
            case 890:
                if (pl.itemTime.isBanhTrungThuGaQuay) {
                    Service.getInstance().sendThongBao(pl, "Chỉ có thể sự dụng cùng lúc 1 vật phẩm bổ trợ cùng loại");
                    return;
                }
                pl.itemTime.lastTimeBanhTrungThuGaQuay = System.currentTimeMillis();
                pl.itemTime.isBanhTrungThuGaQuay = true;
                updatePoint = true;
                break;
            case 891:
                if (pl.itemTime.isBanhTrungThuThapCam) {
                    Service.getInstance().sendThongBao(pl, "Chỉ có thể sự dụng cùng lúc 1 vật phẩm bổ trợ cùng loại");
                    return;
                }
                pl.itemTime.lastTimeBanhTrungThuThapCam = System.currentTimeMillis();
                pl.itemTime.isBanhTrungThuThapCam = true;
                updatePoint = true;
                break;
            case 1128:
                if (pl.itemTime.ismytom) {
                    Service.getInstance().sendThongBao(pl, "Chỉ có thể sự dụng cùng lúc 1 vật phẩm bổ trợ cùng loại");
                    return;
                }
                pl.itemTime.lastTimeMytom = System.currentTimeMillis();
                pl.itemTime.ismytom = true;
                updatePoint = true;
                break;
            case 1129:
                if (pl.itemTime.isthanhkiem) {
                    Service.getInstance().sendThongBao(pl, "Chỉ có thể sự dụng cùng lúc 1 vật phẩm bổ trợ cùng loại");
                    return;
                }
                pl.itemTime.lastTimeThanhkiem = System.currentTimeMillis();
                pl.itemTime.isthanhkiem = true;
                updatePoint = true;
                break;
            case 1130:
                if (pl.itemTime.ishokiem) {
                    Service.getInstance().sendThongBao(pl, "Chỉ có thể sự dụng cùng lúc 1 vật phẩm bổ trợ cùng loại");
                    return;
                }
                pl.itemTime.lastTimeHokiem = System.currentTimeMillis();
                pl.itemTime.ishokiem = true;
                updatePoint = true;
                break;
            case 1635:
                if (pl.itemTime.isbinhx2) {
                    Service.getInstance().sendThongBao(pl, "Chỉ có thể sự dụng cùng lúc 1 vật phẩm bổ trợ cùng loại");
                    return;
                }
                pl.itemTime.lastTimebinhx2 = System.currentTimeMillis();
                pl.itemTime.isbinhx2 = true;
                updatePoint = true;
                break;
            case 1636:
                if (pl.itemTime.isbinhx3) {
                    Service.getInstance().sendThongBao(pl, "Chỉ có thể sự dụng cùng lúc 1 vật phẩm bổ trợ cùng loại");
                    return;
                }
                pl.itemTime.lastTimebinhx3 = System.currentTimeMillis();
                pl.itemTime.isbinhx3 = true;
                updatePoint = true;
                break;
            case 1637:
                if (pl.itemTime.isbinhx4) {
                    Service.getInstance().sendThongBao(pl, "Chỉ có thể sự dụng cùng lúc 1 vật phẩm bổ trợ cùng loại");
                    return;
                }
                pl.itemTime.lastTimebinhx4 = System.currentTimeMillis();
                pl.itemTime.isbinhx4 = true;
                updatePoint = true;
                break;
            case 1281:
                if (pl.itemTime.ismaydotainguyen) {
                    Service.getInstance().sendThongBao(pl, "Chỉ có thể sự dụng cùng lúc 1 vật phẩm bổ trợ cùng loại");
                    return;
                }
                pl.itemTime.lastTimetainguyen = System.currentTimeMillis();
                pl.itemTime.ismaydotainguyen = true;
                updatePoint = true;
                break;
            case 1282:
                if (pl.itemTime.ismaydotrangbi) {
                    Service.getInstance().sendThongBao(pl, "Chỉ có thể sự dụng cùng lúc 1 vật phẩm bổ trợ cùng loại");
                    return;
                }
                pl.itemTime.lastTimetrangbi = System.currentTimeMillis();
                pl.itemTime.ismaydotrangbi = true;
                updatePoint = true;
                break;
            case 1283:
                if (pl.itemTime.ismaydosaophale) {
                    Service.getInstance().sendThongBao(pl, "Chỉ có thể sự dụng cùng lúc 1 vật phẩm bổ trợ cùng loại");
                    return;
                }
                pl.itemTime.lastTimesaophale = System.currentTimeMillis();
                pl.itemTime.ismaydosaophale = true;
                updatePoint = true;
                break;

            case 1284:
                if (pl.itemTime.ismaydothucan) {
                    Service.getInstance().sendThongBao(pl, "Chỉ có thể sự dụng cùng lúc 1 vật phẩm bổ trợ cùng loại");
                    return;
                }
                pl.itemTime.lastTimethucan = System.currentTimeMillis();
                pl.itemTime.ismaydothucan = true;
                updatePoint = true;
                break;
            case 1285:
                if (pl.itemTime.ismaydongocrong) {
                    Service.getInstance().sendThongBao(pl, "Chỉ có thể sự dụng cùng lúc 1 vật phẩm bổ trợ cùng loại");
                    return;
                }
                pl.itemTime.lastTimengocrong = System.currentTimeMillis();
                pl.itemTime.ismaydongocrong = true;
                updatePoint = true;
                break;

            case 465:// banh trung thu 1 trung
                if (pl.itemTime.isBanhTrungThu1Trung) {
                    Service.getInstance().sendThongBao(pl, "Chỉ có thể sự dụng cùng lúc 1 vật phẩm bổ trợ cùng loại");
                    return;
                }
                pl.itemTime.lastTimeBanhTrungThu1Trung = System.currentTimeMillis();
                pl.itemTime.isBanhTrungThu1Trung = true;
                updatePoint = true;
                break;
            case 466:// banh trung thu 2 trung
                if (pl.itemTime.isBanhTrungThu2Trung) {
                    Service.getInstance().sendThongBao(pl, "Chỉ có thể sự dụng cùng lúc 1 vật phẩm bổ trợ cùng loại");
                    return;
                }
                pl.itemTime.lastTimeBanhTrungThu2Trung = System.currentTimeMillis();
                pl.itemTime.isBanhTrungThu2Trung = true;
                updatePoint = true;
                break;
            case 579:
                if (pl.itemTime.isDuoiKhi) {
                    Service.getInstance().sendThongBao(pl, "Chỉ có thể sự dụng cùng lúc 1 vật phẩm bổ trợ cùng loại");
                    return;
                }
                pl.itemTime.lastTimeDuoiKhi = System.currentTimeMillis();
                pl.itemTime.isDuoiKhi = true;
                updatePoint = true;
                break;
            case 382: //bổ huyết
                if (pl.itemTime.isUseBoHuyet2) {
                    Service.getInstance().sendThongBao(pl, "Chỉ có thể sự dụng cùng lúc 1 vật phẩm bổ trợ cùng loại");
                    return;
                }
                pl.itemTime.lastTimeBoHuyet = System.currentTimeMillis();
                pl.itemTime.isUseBoHuyet = true;
                updatePoint = true;
                break;
            case 383: //bổ khí
                if (pl.itemTime.isUseBoKhi2) {
                    Service.getInstance().sendThongBao(pl, "Chỉ có thể sự dụng cùng lúc 1 vật phẩm bổ trợ cùng loại");
                    return;
                }
                pl.itemTime.lastTimeBoKhi = System.currentTimeMillis();
                pl.itemTime.isUseBoKhi = true;
                updatePoint = true;
                break;
            case 384: //giáp xên
                if (pl.itemTime.isUseGiapXen2) {
                    Service.getInstance().sendThongBao(pl, "Chỉ có thể sự dụng cùng lúc 1 vật phẩm bổ trợ cùng loại");
                    return;
                }
                pl.itemTime.lastTimeGiapXen = System.currentTimeMillis();
                pl.itemTime.isUseGiapXen = true;
                updatePoint = true;
                break;
            case 381: //cuồng nộ
                if (pl.itemTime.isUseCuongNo2) {
                    Service.getInstance().sendThongBao(pl, "Chỉ có thể sự dụng cùng lúc 1 vật phẩm bổ trợ cùng loại");
                    return;
                }
                pl.itemTime.lastTimeCuongNo = System.currentTimeMillis();
                pl.itemTime.isUseCuongNo = true;
                updatePoint = true;
                break;
            case 385: //ẩn danh
                pl.itemTime.lastTimeAnDanh = System.currentTimeMillis();
                pl.itemTime.isUseAnDanh = true;
                break;
            case 1152: //bổ huyết 2
                if (pl.itemTime.isUseBoHuyet) {
                    Service.getInstance().sendThongBao(pl, "Chỉ có thể sự dụng cùng lúc 1 vật phẩm bổ trợ cùng loại");
                    return;
                }
                pl.itemTime.lastTimeBoHuyet2 = System.currentTimeMillis();
                pl.itemTime.isUseBoHuyet2 = true;
                updatePoint = true;
                break;
            case 1151: //bổ khí 2
                if (pl.itemTime.isUseBoKhi) {
                    Service.getInstance().sendThongBao(pl, "Chỉ có thể sự dụng cùng lúc 1 vật phẩm bổ trợ cùng loại");
                    return;
                }
                pl.itemTime.lastTimeBoKhi2 = System.currentTimeMillis();
                pl.itemTime.isUseBoKhi2 = true;
                updatePoint = true;
                break;
            case 752:
                pl.itemTime.lastTimeBanhTet = System.currentTimeMillis();
                pl.itemTime.isUseBanhTet = true;
                break;
            case 753:
                pl.itemTime.lastTimeBanhChung = System.currentTimeMillis();
                pl.itemTime.isUseBanhChung = true;

            case 1153: //giáp xên 2
                if (pl.itemTime.isUseGiapXen) {
                    Service.getInstance().sendThongBao(pl, "Chỉ có thể sự dụng cùng lúc 1 vật phẩm bổ trợ cùng loại");
                    return;
                }
                pl.itemTime.lastTimeGiapXen2 = System.currentTimeMillis();
                pl.itemTime.isUseGiapXen2 = true;
                updatePoint = true;
                break;
            case 1150: //cuồng nộ 2
                if (pl.itemTime.isUseCuongNo) {
                    Service.getInstance().sendThongBao(pl, "Chỉ có thể sự dụng cùng lúc 1 vật phẩm bổ trợ cùng loại");
                    return;
                }
                pl.itemTime.lastTimeCuongNo2 = System.currentTimeMillis();
                pl.itemTime.isUseCuongNo2 = true;
                updatePoint = true;
                break;
            case 379: //máy dò
                pl.itemTime.lastTimeUseMayDo = System.currentTimeMillis();
                pl.itemTime.isUseMayDo = true;
                break;

//               case 1624: //máy dò
//                pl.itemTime.lastTimeUseMayDoskh = System.currentTimeMillis();
//                pl.itemTime.isUseMayDoskh = true;
//                break;  
            case 663: //bánh pudding
            case 664: //xúc xíc
            case 665: //kem dâu
            case 666: //mì ly
            case 667: //sushi
                pl.itemTime.lastTimeEatMeal = System.currentTimeMillis();
                pl.itemTime.isEatMeal = true;
                ItemTimeService.gI().removeItemTime(pl, pl.itemTime.iconMeal);
                pl.itemTime.iconMeal = item.template.iconID;
                updatePoint = true;
                break;
            case 1487: //x3EXP
                pl.itemTime.lastnuocmiakhonglo = System.currentTimeMillis();
                pl.itemTime.isnuocmiakhonglo = true;
                break;
            case 1488: //x3EXP
                pl.itemTime.lastnuocmiathom = System.currentTimeMillis();
                pl.itemTime.isnuocmiathom = true;
                break;
            case 1489: //x3EXP
                pl.itemTime.lastnuocmiasaurieng = System.currentTimeMillis();
                pl.itemTime.isnuocmiasaurieng = true;
                break;
            case 1511:
                pl.itemTime.lastTimeexpTamkjll1_5 = System.currentTimeMillis();
                pl.itemTime.isXexpTamkjll1_5 = true;
                break;
            case 1512:
                pl.itemTime.lastTimeexpTamkjll2 = System.currentTimeMillis();
                pl.itemTime.isXexpTamkjll2 = true;
                break;
            case 1513:
                pl.itemTime.lastTimeexpTamkjll3 = System.currentTimeMillis();
                pl.itemTime.isXexpTamkjll3 = true;
                break;
            case 1514:
                pl.itemTime.lastTimeexpTamkjll4 = System.currentTimeMillis();
                pl.itemTime.isXexpTamkjll4 = true;
                break;
            case 1515:
                pl.itemTime.lastTimeexpTamkjll5 = System.currentTimeMillis();
                pl.itemTime.isXexpTamkjll5 = true;
                break;
            case 1516:
                pl.itemTime.lastTimeexpTamkjll6 = System.currentTimeMillis();
                pl.itemTime.isXexpTamkjll6 = true;
                break;
        }
        if (updatePoint) {
            Service.getInstance().point(pl);
        }
        ItemTimeService.gI().sendAllItemTime(pl);
        InventoryService.gI().subQuantityItemsBag(pl, item, 1);
        InventoryService.gI().sendItemBags(pl);
    }

    private void controllerCallRongThan(Player pl, Item item) {
        int tempId = item.template.id;
        if (tempId >= SummonDragon.NGOC_RONG_1_SAO && tempId <= SummonDragon.NGOC_RONG_7_SAO) {
            switch (tempId) {
                case SummonDragon.NGOC_RONG_1_SAO:
                case SummonDragon.NGOC_RONG_2_SAO:
                case SummonDragon.NGOC_RONG_3_SAO:
                    SummonDragon.gI().openMenuSummonShenron(pl, (byte) (tempId - 13), SummonDragon.DRAGON_SHENRON);
                    break;
                default:
                    NpcService.gI().createMenuConMeo(pl, ConstNpc.TUTORIAL_SUMMON_DRAGON, -1, "Bạn chỉ có thể gọi rồng từ ngọc 3 sao, 2 sao, 1 sao", "Hướng\ndẫn thêm\n(mới)", "OK");
                    break;
            }
        } else if (tempId == SummonDragon.NGOC_RONG_SIEU_CAP) {
            SummonDragon.gI().openMenuSummonShenron(pl, (byte) 981, SummonDragon.DRAGON_BLACK_SHENRON);
        } else if (tempId >= SummonDragon.NGOC_RONG_BANG[0] && tempId <= SummonDragon.NGOC_RONG_BANG[6]) {
            switch (tempId) {
                case 925:
                    SummonDragon.gI().openMenuSummonShenron(pl, (byte) 925, SummonDragon.DRAGON_ICE_SHENRON);
                    break;
                default:
                    Service.getInstance().sendThongBao(pl, "Bạn chỉ có thể gọi rồng băng từ ngọc 1 sao");
                    break;
            }
        }
    }

    private void learnSkill(Player pl, Item item) {
        Message msg;
        try {
            if (item.template.gender == pl.gender || item.template.gender == 3) {
                String[] subName = item.template.name.split("");
                byte level = Byte.parseByte(subName[subName.length - 1]);
                Skill curSkill = SkillUtil.getSkillByItemID(pl, item.template.id);
                if (curSkill.point == 7) {
                    Service.getInstance().sendThongBao(pl, "Kỹ năng đã đạt tối đa!");
                } else {
                    if (curSkill.point == 0) {
                        if (level == 1) {
                            curSkill = SkillUtil.createSkill(SkillUtil.getTempSkillSkillByItemID(item.template.id), level);
                            SkillUtil.setSkill(pl, curSkill);
                            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                            msg = Service.getInstance().messageSubCommand((byte) 23);
                            msg.writer().writeShort(curSkill.skillId);
                            pl.sendMessage(msg);
                            msg.cleanup();
                        } else {
                            Skill skillNeed = SkillUtil.createSkill(SkillUtil.getTempSkillSkillByItemID(item.template.id), level);
                            Service.getInstance().sendThongBao(pl, "Vui lòng học " + skillNeed.template.name + " cấp " + skillNeed.point + " trước!");
                        }
                    } else {
                        if (curSkill.point + 1 == level) {
                            curSkill = SkillUtil.createSkill(SkillUtil.getTempSkillSkillByItemID(item.template.id), level);
                            //System.out.println(curSkill.template.name + " - " + curSkill.point);
                            SkillUtil.setSkill(pl, curSkill);
                            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                            msg = Service.getInstance().messageSubCommand((byte) 62);
                            msg.writer().writeShort(curSkill.skillId);
                            pl.sendMessage(msg);
                            msg.cleanup();
                        } else {
                            Service.getInstance().sendThongBao(pl, "Vui lòng học " + curSkill.template.name + " cấp " + (curSkill.point + 1) + " trước!");
                        }
                    }
                    InventoryService.gI().sendItemBags(pl);
                }
            } else {
                Service.getInstance().sendThongBao(pl, "Không thể thực hiện");

            }
        } catch (Exception e) {
            Log.error(UseItem.class,
                    e);
        }
    }

    private void useTDLT(Player pl, Item item) {
        if (pl.itemTime.isUseTDLT) {
            ItemTimeService.gI().turnOffTDLT(pl, item);
        } else {
            ItemTimeService.gI().turnOnTDLT(pl, item);
        }
    }

    private void useOffline(Player pl, Item item) {
        if (pl.itemTime.isUseOffline) {
            ItemTimeService.gI().turnOffOffline(pl, item);
        } else {
            ItemTimeService.gI().turnOnOffline(pl, item);
        }
    }

    private void usePorata(Player pl) {
        if (pl.pet == null || pl.fusion.typeFusion == 4) {
            Service.getInstance().sendThongBao(pl, "Không thể thực hiện");
        } else {
            if (pl.fusion.typeFusion == ConstPlayer.NON_FUSION) {
                pl.pet.fusion(true);
            } else {
                pl.pet.unFusion();
            }
        }
    }

    private void usePorata2(Player pl) {
        if (pl.pet == null || pl.fusion.typeFusion == 4 || pl.fusion.typeFusion == 6 || pl.fusion.typeFusion == 10 || pl.fusion.typeFusion == 12) {
            Service.getInstance().sendThongBao(pl, "Không thể thực hiện");
        } else {
            if (pl.fusion.typeFusion == ConstPlayer.NON_FUSION) {
                pl.pet.fusion2(true);
            } else {
                pl.pet.unFusion();
            }
        }
    }

    private void usePorataSS(Player pl) {
        if (pl.pet == null || pl.fusion.typeFusion == ConstPlayer.LUONG_LONG_NHAT_THE) {
            Service.getInstance().sendThongBao(pl, "Không thể thực hiện");
        } else {
            if (pl.fusion.typeFusion == ConstPlayer.NON_FUSION) {
                pl.pet.fusionSS(true);
            } else {
                pl.pet.unFusion();
            }
        }
    }

    private void usePorataSSS(Player pl) {
        if (pl.pet == null || pl.fusion.typeFusion == ConstPlayer.LUONG_LONG_NHAT_THE) {
            Service.getInstance().sendThongBao(pl, "Không thể thực hiện");
        } else {
            if (pl.fusion.typeFusion == ConstPlayer.NON_FUSION) {
                pl.pet.fusionSSS(true);
            } else {
                pl.pet.unFusion();
            }
        }
    }

    private void openCapsuleUI(Player pl) {
        if (pl.isHoldNamecBall) {
            NamekBallWar.gI().dropBall(pl);
            Service.getInstance().sendFlagBag(pl);
        }
        if (BossManager.gI().findBossByName("Vận tiêu " + pl.name) != null) {
            return;
        }
        pl.iDMark.setTypeChangeMap(ConstMap.CHANGE_CAPSULE);
        ChangeMapService.gI().openChangeMapTab(pl);
    }

    public void choseMapCapsule(Player pl, int index) {
        if (BossManager.gI().findBossByName("Vận tiêu " + pl.name) != null) {
            return;
        }
        int zoneId = -1;
        if (index < 0 || index >= pl.mapCapsule.size()) {
            return;
        }
        Zone zoneChose = pl.mapCapsule.get(index);
        if (index != 0 || zoneChose.map.mapId == 21 || zoneChose.map.mapId == 22 || zoneChose.map.mapId == 23) {
            if (!(pl.zone != null && pl.zone instanceof ZSnakeRoad)) {
                pl.mapBeforeCapsule = pl.zone;
            } else {
                pl.mapBeforeCapsule = null;
            }
        } else {
            zoneId = pl.mapBeforeCapsule != null ? pl.mapBeforeCapsule.zoneId : -1;
            pl.mapBeforeCapsule = null;
        }
        ChangeMapService.gI().changeMapBySpaceShip(pl, pl.mapCapsule.get(index).map.mapId, zoneId, -1);
    }

    private void upSkillPet(Player pl, Item item) {
        if (pl.pet == null) {
            Service.getInstance().sendThongBao(pl, "Không thể thực hiện");
            return;
        }
        try {
            switch (item.template.id) {
                case 402: //skill 1
                    if (SkillUtil.upSkillPet(pl.pet.playerSkill.skills, 0)) {
                        Service.getInstance().chatJustForMe(pl, pl.pet, "Cảm ơn sư phụ");
                        InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                    } else {
                        Service.getInstance().sendThongBao(pl, "Không thể thực hiện");
                    }
                    break;
                case 403: //skill 2
                    if (SkillUtil.upSkillPet(pl.pet.playerSkill.skills, 1)) {
                        Service.getInstance().chatJustForMe(pl, pl.pet, "Cảm ơn sư phụ");
                        InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                    } else {
                        Service.getInstance().sendThongBao(pl, "Không thể thực hiện");
                    }
                    break;
                case 404: //skill 3
                    if (SkillUtil.upSkillPet(pl.pet.playerSkill.skills, 2)) {
                        Service.getInstance().chatJustForMe(pl, pl.pet, "Cảm ơn sư phụ");
                        InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                    } else {
                        Service.getInstance().sendThongBao(pl, "Không thể thực hiện");
                    }
                    break;
                case 759: //skill 4
                    if (SkillUtil.upSkillPet(pl.pet.playerSkill.skills, 3)) {
                        Service.getInstance().chatJustForMe(pl, pl.pet, "Cảm ơn sư phụ");
                        InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                    } else {
                        Service.getInstance().sendThongBao(pl, "Không thể thực hiện");
                    }
                    break;
            }
        } catch (Exception e) {
            Service.getInstance().sendThongBao(pl, "Không thể thực hiện");
        }
    }

    private void hopQuaKichHoat(Player player, Item item) {
        NpcService.gI().createMenuConMeo(player, ConstNpc.MENU_OPTION_USE_ITEM1105, -1, "Chọn hành tinh của mày đi",
                "Set trái đất",
                "Set namec",
                "Set xayda",
                "Từ chổi");

    }
}
