package nro.models.npc;

import nro.attr.AttributeManager;
import nro.consts.*;
import nro.dialog.ConfirmDialog;
import nro.dialog.MenuDialog;
import nro.jdbc.daos.PlayerDAO;
import nro.lib.RandomCollection;
import nro.models.boss.Boss;
import nro.models.boss.BossFactory;
import nro.models.boss.BossManager;
import nro.models.clan.Clan;
import nro.models.clan.ClanMember;
import nro.models.consignment.ConsignmentShop;
import nro.models.item.Item;
import nro.models.item.ItemOption;
import nro.models.item.ItemTemplate;
import nro.server.TaiXiu;
import nro.models.map.SantaCity;
import nro.models.map.Zone;
import nro.models.map.challenge.MartialCongressService;
import nro.models.map.dungeon.SnakeRoad;
import nro.models.map.dungeon.zones.ZSnakeRoad;
import nro.models.map.mabu.MabuWar;
import nro.models.map.phoban.BanDoKhoBau;
import nro.models.map.phoban.DoanhTrai;
import nro.models.map.war.BlackBallWar;
import nro.models.map.war.NamekBallWar;
import nro.models.player.NPoint;
import nro.models.player.Player;
import nro.server.Manager;
import nro.server.ServerManager;
import nro.services.*;
import nro.services.func.*;
import nro.utils.Log;
import nro.utils.TimeUtil;
import nro.utils.Util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import nro.map.VoDaiSinhTu.VoDaiSinhTuService;
import nro.models.Bot.BotManager;
import nro.models.boss.BossData;
import nro.models.boss.MapMaBu;

import nro.models.boss.broly.HoTong;
import nro.models.boss.list_boss.NhanBan;
import nro.models.map.dungeon.LeoThap;
import nro.models.map.mabu.MabuWar14h;
import nro.models.npc.specialnpc.duahau;
import nro.models.phuban.DragonNamecWar.TranhNgoc;
import nro.models.phuban.DragonNamecWar.TranhNgocService;
import nro.models.player.Inventory;
import nro.models.skill.Skill;
import nro.server.Client;
import nro.server.Maintenance;

import static nro.server.Manager.*;
import nro.server.io.Message;
import static nro.services.func.Input.BUFFTONGNAP;
import static nro.services.func.Input.doithoivang;
import static nro.services.func.SummonDragon.*;
import nro.utils.SkillUtil;

/**
 * @Config Truong
 */
public class NpcFactory {

    private static boolean nhanDeTu = true;

    // playerid - object
    public static final java.util.Map<Long, Object> PLAYERID_OBJECT = new HashMap<Long, Object>();

    private NpcFactory() {

    }

    public static Npc createNPC(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        Npc npc = null;
        try {
            switch (tempId) {
                case ConstNpc.FIDE:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (player.iDMark.getTranhNgoc() == 1) {
                                this.createOtherMenu(player, ConstNpc.BASE_MENU, "Cút! Ta không nói chuyện với sinh vật hạ đẳng", "Đóng");
                                return;
                            }
                            this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                    "Hãy mang ngọc rồng về cho ta", "Đưa ngọc", "Đóng");
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                switch (select) {
                                    case 0:
                                        if (player.iDMark.getTranhNgoc() == 2 && player.isHoldNamecBallTranhDoat) {
                                            if (!Util.canDoWithTime(player.lastTimePickItem, 20000)) {
                                                Service.getInstance().sendThongBao(player, "Vui lòng đợi " + ((player.lastTimePickItem + 20000 - System.currentTimeMillis()) / 1000) + " giây để có thể trả");
                                                return;
                                            }
                                            TranhNgocService.getInstance().dropBall(player, (byte) 2);
                                            player.zone.pointFide++;
                                            if (player.zone.pointFide > ConstTranhNgocNamek.MAX_POINT) {
                                                player.zone.pointFide = ConstTranhNgocNamek.MAX_POINT;
                                            }
                                            TranhNgocService.getInstance().sendUpdatePoint(player);
                                        }
                                        break;
                                    case 1:
                                        break;
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.CADIC:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (player.iDMark.getTranhNgoc() == 2) {
                                this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                        "NEXT! Ta không nói chuyện với sinh vật hạ đẳng", "Đóng");
                                return;
                            }
                            this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                    "Hãy mang ngọc rồng về cho ta", "Đưa ngọc", "Đóng");
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                switch (select) {
                                    case 0:
                                        if (player.iDMark.getTranhNgoc() == 1 && player.isHoldNamecBallTranhDoat) {
                                            if (!Util.canDoWithTime(player.lastTimePickItem, 20000)) {
                                                Service.getInstance().sendThongBao(player, "Vui lòng đợi " + ((player.lastTimePickItem + 20000 - System.currentTimeMillis()) / 1000) + " giây để có thể trả");
                                                return;
                                            }
                                            TranhNgocService.getInstance().dropBall(player, (byte) 1);
                                            player.zone.pointCadic++;
                                            if (player.zone.pointCadic > ConstTranhNgocNamek.MAX_POINT) {
                                                player.zone.pointCadic = ConstTranhNgocNamek.MAX_POINT;
                                            }
                                            TranhNgocService.getInstance().sendUpdatePoint(player);
                                        }
                                        break;
                                    case 1:
                                        break;
                                }
                            }
                        }
                    };
                    break;
//                case ConstNpc.vodai:
//                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
//                        @Override
//                        public void openBaseMenu(Player player) {
//                            if (canOpenNpc(player)) {
//                                long now = System.currentTimeMillis();
//                                if (this.mapId == 207) {
//                                    if (Util.isTimeSpwam(21, 22)) {
//                                        this.createOtherMenu(player, 0,
//                                                "Bạn muốn gì nào?\n"
//                                                + "|7|MAP THÁNH ĐỊA GIA TỘC MỞ CỬA VÀO 21H-22H MỖI NGÀY\n"
//                                                + "\n|6|Để đến được Thánh Địa bạn cần phải Có Clan Trên 5 Thành Viên\n"
//                                                + "\n|7|Farm Quai Nhận Những Vật Phẩm Chỉ Có Nạp Mới Có\n"
//                                                + "Tiêu Diệt Kẻ Khác Để nhận Điểm Gia Tộc\n"
//                                                + "Hãy Tập Luyện Nhé+ Xuất Hiện Boss Cực Vip\n"
//                                                + "|2|Bạn đang có : " + player.pointPvp + " điểm Gia Tộc",
//                                                "Đến\nThánh Địa", "Đổi\nSD Gốc", "ĐỔI\nHP,KI Gốc", "Đổi\nDef Gốc", "Cút");
//                                    } else {
//                                        this.createOtherMenu(player, 0,
//                                                "Bạn muốn gì nào?\n"
//                                                + "|7|MAP THÁNH ĐỊA GIA TỘC MỞ CỬA VÀO 21H-22H MỖI NGÀY\n"
//                                                + "\n|6|Để đến được Thánh Địa bạn cần phải Có Clan Trên 5 Thành Viên\n"
//                                                + "\n|7|Farm Quai Nhận Những Vật Phẩm Chỉ Có Nạp Mới Có\n"
//                                                + "Tiêu Diệt Kẻ Khác Để nhận Điểm Gia Tộc\n"
//                                                + "Hãy Tập Luyện Nhé+ Xuất Hiện Boss Cực Vip\n"
//                                                + "|2|Bạn đang có : " + player.pointPvp + " điểm Gia Tộc",
//                                                "Đến\nThánh Địa", "Đổi\nSD Gốc", "ĐỔI\nHP,KI Gốc", "Đổi\nDef Gốc", "Cút");
//
//                                    }
//                                }
//                                if (this.mapId == 212) {
//                                    this.createOtherMenu(player, 0,
//                                            "Bạn muốn gì nào?\nBạn đang có : " + player.pointPvp + " điểm Đấu Trường", "Về\nĐảo Kame");
//                                }
//                            }
//                        }
//
//                        @Override
//                        public void confirmMenu(Player player, int select) {
//                            if (canOpenNpc(player)) {
//                                if (this.mapId == 207) {
//                                    long now = System.currentTimeMillis();
//                                    if (player.iDMark.getIndexMenu() == 0) {
//                                        switch (select) {
//                                            case 0:
//                                                if (player.clan == null) {
//                                                    Service.getInstance().sendThongBao(player, "Chưa có bang hội");
//                                                    return;
//                                                } else if (player.clan.getMembers().size() >= 1) {
//                                                    if (Util.isTimeSpwam(21, 22)) {
//                                                        ChangeMapService.gI().changeMapBySpaceShip(player, 212, -1, 168);
//                                                        Service.getInstance().changeFlag(player, 8);
//                                                        break; // đến võ đài siêu cấp
//                                                    } else {
//                                                        Service.getInstance().sendThongBaoFromAdmin(player, "[Nếu người ta đã ghét mình.\n Thì mình phải sống làm sao kiếm thật nhiều tiền \ncho người ta ghét mình hơn nữa.]\n"
//                                                                + "|7|\nChưa đến thời gian mở Thánh Địa Gia Tộc");
//                                                        break;
//                                                    }
//                                                } else {
//                                                    Service.getInstance().sendThongBaoFromAdmin(player, "Bang hội phải có ít nhất 5 thành viên mới có thể mở");
//                                                    break; // đến võ đài siêu cấp
//                                                }
//
//                                            case 1:  // 
//                                                if (player.pointPvp >= 100) {
//                                                    player.pointPvp -= 100;
//                                                    player.nPoint.dameg += 5000;
//                                                    Service.getInstance().sendMoney(player);
//                                                    InventoryService.gI().sendItemBags(player);
//                                                    Service.getInstance().sendThongBaoFromAdmin(player, "|7|\nXin Chúc Mừng Bạn\n" + " \n|6|Nhận Đã Đổi Điểm Thành Công!\n" + " Nhận Được 50 Nghìn Dame Gốc\nVui Lòng Mặc Lại Cải Trang Hoặc Thoát Game Vào Lại Để loading");
//                                                } else {
//                                                    Service.getInstance().sendThongBaoFromAdmin(player, "|4|\n Không tiền lấy đâu ra hạnh phúc!\n"
//                                                            + "Không có tiền thì chấp nhận thương đau đi!\n"
//                                                            + "Không có tiền yêu con người ta làm gì?\n"
//                                                            + "Không có tiền đi chơi làm gì?\n"
//                                                            + "Không có tiền đi ngao du làm gì?\n"
//                                                            + "Không có tiền đàn đúm làm gì?\n"
//                                                            + "Không có tiền thì sĩ làm gì?\n"
//                                                            + "Không có tiền rồi thì chấp nhận đi!\n"
//                                                            + " \n|7|Điểm Gia tộc Còn Thiếu " + (100 - player.pointPvp) + " Điểm nữa");
//                                                }
//                                                break;
//
//                                            case 2:
//
//                                                if (player.pointPvp >= 50) {
//                                                    player.pointPvp -= 50;
//                                                    player.nPoint.hpg += 5000;
//                                                    player.nPoint.mpg += 5000;
//                                                    Service.getInstance().sendMoney(player);
//                                                    InventoryService.gI().sendItemBags(player);
//                                                    Service.getInstance().sendThongBaoFromAdmin(player, "|7|\nXin Chúc Mừng Bạn\n" + " \n|6|Nhận Đã Đổi Điểm Thành Công!" + " Nhận Được 50 Nghìn HP,KI Gốc\nVui Lòng Mặc Lại Cải Trang Hoặc Thoát Game Vào Lại Để loading");
//                                                } else {
//                                                    Service.getInstance().sendThongBaoFromAdmin(player, "|4|\nRa xã hội làm ăn bươn chãi,\n liều nhiều thì ăn nhiều,ko liều thì ăn ít.\n Muốn thành công phải trải qua đắng cay ngọt bùi.\n Làm ăn phải chấp nhận mạo hiểm, nguy hiểm.\n"
//                                                            + "Sau này, chỉ có làm, chịu khó, cần cù bù siêng năng, chỉ có làm thì mới có ăn…\n Ko chỉ Ăn Db Ăn sít\n" + " \n|7| Điểm Gia Tộc thiếu " + (50 - player.pointPvp) + " Điểm nữa");
//                                                }
//                                                break;
//                                            case 3:
//
//                                                if (player.pointPvp >= 50) {
//                                                    player.pointPvp -= 50;
//                                                    player.nPoint.defg += 500;
//                                                    Service.getInstance().sendMoney(player);
//                                                    Service.getInstance().sendThongBaoFromAdmin(player, "|7|\nXin Chúc Mừng Bạn\n" + " \n|6|Nhận Đã Đổi Điểm Thành Công!" + " Nhận Được 5 Nghìn giáp Gốc\nVui Lòng Mặc Lại Cải Trang Hoặc Thoát Game Vào Lại Để loading");
//                                                } else {
//                                                    Service.getInstance().sendThongBaoFromAdmin(player, "|4|\nRa xã hội làm ăn bươn chãi,\n liều nhiều thì ăn nhiều,ko liều thì ăn ít.\n Muốn thành công phải trải qua đắng cay ngọt bùi.\n Làm ăn phải chấp nhận mạo hiểm, nguy hiểm.\n"
//                                                            + "Sau này, chỉ có làm, chịu khó, cần cù bù siêng năng, chỉ có làm thì mới có ăn… Ko chỉ Ăn Db Ăn sít\n" + " \n|7|Điểm Gia Tộc thiếu " + (50 - player.pointPvp) + " Điểm nữa");
//                                                }
//                                                break;
//
//                                        }
//                                    }
//                                }
//                                if (this.mapId == 212) {
//                                    switch (select) {
//                                        case 0: // quay ve
//                                            ChangeMapService.gI().changeMapInYard(player, 5, -1, 354);
//                                            Service.getInstance().changeFlag(player, Util.nextInt(1));
//                                            break;
//                                    }
//                                }
//                            }
//                        }
//
//                    };
//
//                    break;
                case ConstNpc.DUONG_TANG:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (this.mapId == MapName.LANG_ARU) {
                                this.createOtherMenu(player, ConstNpc.BASE_MENU, "|7|HELLO BẠN IU\n"
                                        + "|6|Nhớ Nạp Nhiều Nhiều Cho Trường Nha\n"
                                        + "|5|Farm Train Điểm Ngũ Hành Sơn Đổi Quà Vip\n"
                                        + "|5|Điểm TrainFarm: " + Util.cap(player.diemfam) + " Điểm\n",
                                        "Đến Map\nNgũ Hành Sơn", "Đến Map\nThảo Nguyên", "Đến Map\nThànhPhố\nTàiNguyên",
                                        "Đến Map\nRừng Đá", "Đến Map\nThung Lũng", "Đến Map\nRừng Thông");
                            }

                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == MapName.LANG_ARU) {
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {
                                            case 0:
                                                if (Util.isTimeSpwam(00, 22)) {
                                                    Zone zone = MapService.gI().getZoneJoinByMapIdAndZoneId(player, 124, 0);
                                                    if (zone != null) {
                                                        player.location.x = 100;
                                                        player.location.y = 384;
                                                        MapService.gI().goToMap(player, zone);
                                                        Service.getInstance().clearMap(player);
                                                        zone.mapInfo(player);
                                                        player.zone.loadAnotherToMe(player);
                                                        player.zone.load_Me_To_Another(player);
                                                    }
                                                } else {
                                                    Service.getInstance().sendThongBao(player,
                                                            "Lối vào ngũ hành sơn chưa mở");
                                                }
                                                break;

                                            case 1:
                                                ChangeMapService.gI().changeMapBySpaceShip(player, 213, -1, 354);
                                                break;
                                            case 2:
                                                if (player.vip < 1) {
                                                    Service.getInstance().sendThongBaoFromAdmin(player, "Cần Đạt Tối Thiểu VIP 1");
                                                    return;
                                                }
                                                ChangeMapService.gI().changeMapBySpaceShip(player, 214, -1, 354);
                                                break;
                                            case 3:
                                                ChangeMapService.gI().changeMapBySpaceShip(player, 36, -1, 354);
                                                break;
                                            case 4:
                                                ChangeMapService.gI().changeMapBySpaceShip(player, 37, -1, 354);
                                                break;
                                            case 5:
                                                ChangeMapService.gI().changeMapBySpaceShip(player, 18, -1, 354);
                                                break;

                                            case 6:

                                                if (player.vip < 4) {
                                                    Service.getInstance().sendThongBaoFromAdmin(player, "Cần Đạt Tối Thiểu VIP 4");
                                                    return;
                                                }
                                                ChangeMapService.gI().changeMapBySpaceShip(player, 5, -1, 354);
                                                break;
                                            case 7:

                                                if (player.diemfam < 5000) {
                                                    Service.getInstance().sendThongBaoFromAdmin(player, "Bạn cần ít nhất 5000 Điểm TrainFarm\n Để Đổi Vật Phẩm là 1 Sách Ép Hp");
                                                    return;
                                                }
                                                Item manhnro6 = null;
                                                try {
                                                    manhnro6 = InventoryService.gI().findItemBagByTemp(player, 457);
                                                } catch (Exception e) {
                                                }
                                                if (manhnro6 == null) {
                                                    this.npcChat(player, "Bạn cần có 1 Thỏi Vàng còn dư để Trả Phí");
                                                } else if (InventoryService.gI().getCountEmptyBag(player) == 2) {
                                                    this.npcChat(player, "Hành Trang Của Bạn Không Đủ Chỗ Trống Cần Trống 2 ô");

                                                } else {
                                                    InventoryService.gI().subQuantityItemsBag(player, manhnro6, 1);
                                                    Item nro12 = ItemService.gI().createNewItem((short) 2013, 1);
                                                    nro12.itemOptions.add(new ItemOption(77, 50));
                                                    InventoryService.gI().addItemBag(player, nro12, 9999999);
                                                    InventoryService.gI().sendItemBags(player);
                                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + nro12.template.name);
                                                    player.diemfam -= 5000;
                                                    Service.getInstance().sendThongBao(player, "Bạn Bị Trừ 5000 điểm TrainFarm");

                                                }
                                                break;
                                            case 8:

                                                if (player.diemfam < 5000) {
                                                    Service.getInstance().sendThongBaoFromAdmin(player, "Bạn cần ít nhất 5000 Điểm TrainFarm\n Để Đổi Vật Phẩm là 1 Sách Ép Ki");
                                                    return;
                                                }
                                                Item manhnro7 = null;
                                                try {
                                                    manhnro7 = InventoryService.gI().findItemBagByTemp(player, 457);
                                                } catch (Exception e) {
                                                }
                                                if (manhnro7 == null) {
                                                    this.npcChat(player, "Bạn Cần có 1 tv còn dư Để Trả Phí");
                                                } else if (InventoryService.gI().getCountEmptyBag(player) == 2) {
                                                    this.npcChat(player, "Hành Trang Của Bạn Không Đủ Chỗ Trống Cần Trống 2 ô");

                                                } else {
                                                    InventoryService.gI().subQuantityItemsBag(player, manhnro7, 1);
                                                    Item nro12 = ItemService.gI().createNewItem((short) 2014, 1);
                                                    nro12.itemOptions.add(new ItemOption(103, 50));
                                                    InventoryService.gI().addItemBag(player, nro12, 9999999);
                                                    InventoryService.gI().sendItemBags(player);
                                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + nro12.template.name);
                                                    player.diemfam -= 5000;
                                                    Service.getInstance().sendThongBao(player, "Bạn Bị Trừ 5000 điểm trainFarm");

                                                }
                                                break;

                                        }
                                    }
                                }
                                if (this.mapId == MapName.NGU_HANH_SON_3) {
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {
                                            case 0:
                                                Zone zone = MapService.gI().getZoneJoinByMapIdAndZoneId(player, 0, 0);
                                                if (zone != null) {
                                                    player.location.x = 600;
                                                    player.location.y = 432;
                                                    MapService.gI().goToMap(player, zone);
                                                    Service.getInstance().clearMap(player);
                                                    zone.mapInfo(player);
                                                    player.zone.loadAnotherToMe(player);
                                                    player.zone.load_Me_To_Another(player);
                                                }
                                                break;
                                        }
                                    }
                                }
                                if (this.mapId == MapName.NGU_HANH_SON) {
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {
                                            case 0:
                                                // Đổi đào
                                                Item item = InventoryService.gI().findItemBagByTemp(player,
                                                        ConstItem.QUA_HONG_DAO);
                                                if (item == null || item.quantity < 10) {
                                                    npcChat(player,
                                                            "Cần 10 quả đào xanh Để Đổi lấy đào chín từ bần tăng.");
                                                    return;
                                                }
                                                if (InventoryService.gI().getCountEmptyBag(player) == 0) {
                                                    npcChat(player, "Túi đầy rồi kìa.");
                                                    return;
                                                }
                                                Item newItem = ItemService.gI()
                                                        .createNewItem((short) ConstItem.QUA_HONG_DAO_CHIN, 1);
                                                InventoryService.gI().subQuantityItemsBag(player, item, 10);
                                                InventoryService.gI().addItemBag(player, newItem, 0);
                                                InventoryService.gI().sendItemBags(player);
                                                npcChat(player,
                                                        "Ta đã đổi cho thí chủ rồi đó, hãy mang cho đệ tử ta đi nào");
                                                break;

                                            case 1:
                                                // giải phong ấn
                                                if (InventoryService.gI().getCountEmptyBag(player) == 0) {
                                                    npcChat(player, "Túi đầy rồi kìa.");
                                                    return;
                                                }
                                                int[] itemsNeed = {ConstItem.CHU_GIAI, ConstItem.CHU_KHAI,
                                                    ConstItem.CHU_PHONG, ConstItem.CHU_AN};
                                                List<Item> items = InventoryService.gI().getListItem(player, itemsNeed)
                                                        .stream().filter(i -> i.quantity >= 10)
                                                        .collect(Collectors.toList());
                                                boolean[] flags = new boolean[4];
                                                for (Item i : items) {
                                                    switch ((int) i.template.id) {
                                                        case ConstItem.CHU_GIAI:
                                                            flags[0] = true;
                                                            break;

                                                        case ConstItem.CHU_KHAI:
                                                            flags[1] = true;
                                                            break;

                                                        case ConstItem.CHU_PHONG:
                                                            flags[2] = true;
                                                            break;

                                                        case ConstItem.CHU_AN:
                                                            flags[3] = true;
                                                            break;
                                                    }
                                                }
                                                for (int i = 0; i < flags.length; i++) {
                                                    if (!flags[i]) {
                                                        ItemTemplate template = ItemService.gI()
                                                                .getTemplate(itemsNeed[i]);
                                                        npcChat("Thí chủ còn thiếu " + template.name);
                                                        return;
                                                    }
                                                }

                                                for (Item i : items) {
                                                    InventoryService.gI().subQuantityItemsBag(player, i, 10);
                                                }

                                                RandomCollection<Integer> rc = new RandomCollection<>();
                                                rc.add(10, ConstItem.CAI_TRANG_TON_NGO_KHONG_DE_TU);
                                                rc.add(10, ConstItem.CAI_TRANG_BAT_GIOI_DE_TU);
                                                rc.add(50, ConstItem.GAY_NHU_Y);
                                                switch (player.gender) {
                                                    case ConstPlayer.TRAI_DAT:
                                                        rc.add(30, ConstItem.CAI_TRANG_TON_NGO_KHONG);
                                                        break;

                                                    case ConstPlayer.NAMEC:
                                                        rc.add(30, ConstItem.CAI_TRANG_TON_NGO_KHONG_545);
                                                        break;

                                                    case ConstPlayer.XAYDA:
                                                        rc.add(30, ConstItem.CAI_TRANG_TON_NGO_KHONG_546);
                                                        break;
                                                }
                                                int itemID = rc.next();
                                                Item nItem = ItemService.gI().createNewItem((short) itemID);
                                                boolean all = itemID == ConstItem.CAI_TRANG_TON_NGO_KHONG_DE_TU
                                                        || itemID == ConstItem.CAI_TRANG_BAT_GIOI_DE_TU
                                                        || itemID == ConstItem.CAI_TRANG_TON_NGO_KHONG
                                                        || itemID == ConstItem.CAI_TRANG_TON_NGO_KHONG_545
                                                        || itemID == ConstItem.CAI_TRANG_TON_NGO_KHONG_546;
                                                if (all) {
                                                    nItem.itemOptions.add(new ItemOption(50, Util.nextInt(20, 35)));
                                                    nItem.itemOptions.add(new ItemOption(77, Util.nextInt(20, 35)));
                                                    nItem.itemOptions.add(new ItemOption(103, Util.nextInt(20, 35)));
                                                    nItem.itemOptions.add(new ItemOption(94, Util.nextInt(5, 10)));
                                                    nItem.itemOptions.add(new ItemOption(100, Util.nextInt(10, 20)));
                                                    nItem.itemOptions.add(new ItemOption(101, Util.nextInt(10, 20)));
                                                }
                                                if (itemID == ConstItem.CAI_TRANG_TON_NGO_KHONG
                                                        || itemID == ConstItem.CAI_TRANG_TON_NGO_KHONG_545
                                                        || itemID == ConstItem.CAI_TRANG_TON_NGO_KHONG_546) {
                                                    nItem.itemOptions.add(new ItemOption(80, Util.nextInt(5, 15)));
                                                    nItem.itemOptions.add(new ItemOption(81, Util.nextInt(5, 15)));
                                                    nItem.itemOptions.add(new ItemOption(106, 0));
                                                } else if (itemID == ConstItem.CAI_TRANG_TON_NGO_KHONG_DE_TU
                                                        || itemID == ConstItem.CAI_TRANG_BAT_GIOI_DE_TU) {
                                                    nItem.itemOptions.add(new ItemOption(197, 0));
                                                }
                                                if (all) {
                                                    if (Util.isTrue(499, 500)) {
                                                        nItem.itemOptions.add(new ItemOption(93, Util.nextInt(3, 30)));
                                                    }
                                                } else if (itemID == ConstItem.GAY_NHU_Y) {
                                                    RandomCollection<Integer> rc2 = new RandomCollection<>();
                                                    rc2.add(60, 30);
                                                    rc2.add(30, 90);
                                                    rc2.add(10, 365);
                                                    nItem.itemOptions.add(new ItemOption(50, 20));
                                                    nItem.itemOptions.add(new ItemOption(77, 20));
                                                    nItem.itemOptions.add(new ItemOption(103, 20));
                                                    nItem.itemOptions.add(new ItemOption(93, rc2.next()));

                                                }
                                                InventoryService.gI().addItemBag(player, nItem, 0);
                                                InventoryService.gI().sendItemBags(player);
                                                npcChat(player.zone,
                                                        "A mi phò phò, đa tạ thí chủ tương trợ, xin hãy nhận món quà mọn này, bần tăng sẽ niệm chú giải thoát cho Ngộ Không");
                                                break;
                                        }
                                    }
                                }
                            }
                        }
                    };
                    break;

                case ConstNpc.TAPION:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                switch (this.mapId) {
                                    case 19:
                                        this.createOtherMenu(player, 123,
                                                "Đang cập nhật",
                                                "OK", "Từ chối");
                                        break;
                                    case 126:
                                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Tôi sẽ đưa bạn về", "OK",
                                                "Từ chối");
                                        break;
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 19) {
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {
                                            case 0:
                                                SantaCity santaCity = (SantaCity) MapService.gI().getMapById(126);
                                                if (santaCity != null) {
                                                    if (!santaCity.isOpened() || santaCity.isClosed()) {
                                                        Service.getInstance().sendThongBao(player,
                                                                "Hẹn gặp bạn lúc 22h mỗi ngày");
                                                        return;
                                                    }
                                                    santaCity.enter(player);
                                                } else {
                                                    Service.getInstance().sendThongBao(player, "Có lỗi xảy ra!");
                                                }
                                                break;
                                        }
                                    }
                                }
                                if (this.mapId == 126) {
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {
                                            case 0:
                                                SantaCity santaCity = (SantaCity) MapService.gI().getMapById(126);
                                                if (santaCity != null) {
                                                    santaCity.leave(player);
                                                } else {
                                                    Service.getInstance().sendThongBao(player, "Có lỗi xảy ra!");
                                                }
                                                break;
                                        }
                                    }
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.LY_TIEU_NUONG:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 5) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "|7|Cư dân tìm khúc mía và nước đá đến xe nước mía ở đầu làng để mua ly nước mía.\n"
                                            + "- Công thức: 2 nước đá + 5 khúc mía + 5m vàng.\n",
                                            "Đổi Nước Mía", "Từ chối");
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 5) {
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {
                                            case 0:
                                                Item nuocda = null;
                                                Item khucmia = null;

                                                try {
                                                    nuocda = InventoryService.gI().findItemBagByTemp(player, 1490);
                                                    khucmia = InventoryService.gI().findItemBagByTemp(player, 1491);
                                                } catch (Exception e) {
                                                }
                                                if (nuocda == null || nuocda.quantity < 2 && khucmia == null || khucmia.quantity < 5 && player.inventory.gold >= 5000000) {
                                                    this.npcChat(player, "Bạn Không Có Vật Phẩm Nào");
                                                } else if (InventoryService.gI().getCountEmptyBag(player) == 0) {
                                                    this.npcChat(player, "Hành Trang Của Bạn Không Đủ Chỗ Trống");
                                                } else if (player.inventory.gold < 5000000) {
                                                    Service.getInstance().sendThongBaoFromAdmin(player, "Bạn còn thiều " + (10000000 - player.inventory.gold) + " Vàng");
                                                    break;
                                                } else {
                                                    InventoryService.gI().subQuantityItemsBag(player, nuocda, 2);
                                                    InventoryService.gI().subQuantityItemsBag(player, khucmia, 5);
                                                    player.inventory.gold -= 5000000;
                                                    Item lynuocmia = ItemService.gI().createNewItem((short) Util.nextInt(1487, 1489));
                                                    lynuocmia.itemOptions.add(new ItemOption(174, 2024));
                                                    InventoryService.gI().addItemBag(player, lynuocmia, 99999);
                                                    InventoryService.gI().sendItemBags(player);
                                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + lynuocmia.template.name);
                                                }
                                                break;
                                        }

                                    }
                                }
                            }
                        }
                    };
                    break;
//                case ConstNpc.MR_POPO:
//                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
//                        @Override
//                        public void openBaseMenu(Player player) {
//                            if (canOpenNpc(player)) {
//                                if (player.clan == null) {
//                                    createOtherMenu(player, ConstNpc.IGNORE_MENU,
//                                            "|7|KHÍ GAS\n|6|Map Khí Gas chỉ dành cho những người có bang hội",
//                                            "OK", "Hướng\ndẫn\nthêm");
//                                    return;
//                                }
//                                if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
//                                    if (player.getSession().is_gift_box) {
//                                    } else {
//                                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "|7|KHÍ GAS\n|6|Thượng đế vừa phát hiện 1 loại khí đang âm thầm\nhủy diệt mọi mầm sống trên Trái Đất,\nnó được gọi là Destron Gas.\nTa sẽ đưa các cậu đến nơi ấy, các cậu sẵn sàng chưa?", "Thông Tin Chi Tiết", "OK", " Bố Từ Chối");
//                                    }
//                                }
//                            }
//                        }
//
//                        @Override
//                        public void confirmMenu(Player player, int select) {
//                            if (canOpenNpc(player)) {
//                                if (this.mapId == 0) {
//                                    if (player.iDMark.isBaseMenu()) {
//                                        switch (select) {
//                                             case 1:
//                                if (player.clan != null) {
//                                    if (player.clan.khiGas != null) {
//                                        this.createOtherMenu(player, ConstNpc.MENU_OPENED_GAS,
//                                                "|7|KHÍ GAS\n|6|Bang hội của con đang đi DesTroy Gas cấp độ "
//                                                + player.clan.khiGas.level + "\nCon có muốn đi theo không?",
//                                                "Đồng ý", "Từ chối");
//                                    } else {
//                                        this.createOtherMenu(player, ConstNpc.MENU_OPEN_GAS,
//                                                "|7|KHÍ GAS\n|6|Khí Gas Huỷ Diệt đã chuẩn bị tiếp nhận các đợt tấn công của quái vật\n"
//                                                + "các con hãy giúp chúng ta tiêu diệt quái vật \n"
//                                                + "Ở đây có ta lo\nNhớ chọn cấp độ vừa sức mình nhé",
//                                                "Chọn\ncấp độ", "Từ chối");
//                                    }
//                                } else {
//                                    this.npcChat(player, "Con phải có bang hội ta mới có thể cho con đi");
//                                }
//                                break;
//                                            case 1:
//                                                Service.getInstance().showTopTask(player);
//                                                break;
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    };
//
//                    break;
                case ConstNpc.TORIBOT:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                createOtherMenu(player, ConstNpc.BASE_MENU,
                                        "|7|TÔI LÀ OBITO...\n"
                                        + "|7|CẬU MUỐN TÌM BẢNG XẾP HẠNG NÀO\n",
                                        "Top\n Săn Boss",
                                        "Top\n Dame Boss",
                                        "Top\nTổng Nạp",
                                        "TOP\nNạp Tuần",
                                        "Top\nNạp Tháng",
                                        "Top\nTiên bang",
                                        "Top\nNhập ma",
                                        "Top\nTrainFarm"
                                );
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 5) {
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {
//                                            case 0:
//                                                Service.getInstance().showListTop(player, Manager.TopCapTamKjll);
//                                                break;

                                            case 0:
                                                Service.getInstance().showListTop(player, Manager.TopSb);
                                                break;
                                            case 1:
                                                Service.getInstance().showListTop(player, Manager.TopDame);
                                                break;

                                            case 2:
                                                Service.getInstance().showListTop(player, Manager.TopNap);
                                                break;
                                            case 3:
                                                Service.getInstance().showListTop(player, Manager.Toptuan);
                                                break;
                                            case 4:
                                                Service.getInstance().showListTop(player, Manager.Topthang);
                                                break;
                                            case 5:
                                                Service.getInstance().showListTop(player, Manager.TopCapTamKjll);
                                                break;
                                            case 6:
                                                Service.getInstance().showListTop(player, Manager.TopLbTamKjll);
                                                break;

                                            case 7:
                                                Service.getInstance().showListTop(player, Manager.TopNhs);
                                                break;
                                        }

                                    }
                                }
                            }
                        }
                    };
                    break;

                case ConstNpc.bdkb:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                createOtherMenu(player, ConstNpc.BASE_MENU,
                                        "|7|Chào Sếp Chơi Game Vui Vẻ...\n"
                                        + "|7|Sự kiện Săn Boss Hải Tặc...\n"
                                        + "|4|Tiêu Diệt Đám Hải Tặc Nhận Mảnh Nro Siêu Cấp\n"
                                        + "|6|Cần x999 Mảnh Nro Siêu Cấp 1-7 Sao Để Ghép Thành Viên Nro Siêu Cấp Thật\n"
                                        + "|5|Đã Đổi : " + Util.numberToMoney(player.Nrosieucap) + " Nro Siêu Cấp\n",
                                        "Ghép\n Mảnh Nro", "Shop\nTrao Đổi", "Shop\nMua Ngay", "Top\n Sự Kiện"
                                );
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 207) {
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {
                                            case 0:
                                                Item manhnro = null;
                                                Item manhnro1 = null;
                                                Item manhnro2 = null;
                                                Item manhnro3 = null;
                                                Item manhnro4 = null;
                                                Item manhnro5 = null;
                                                Item manhnro6 = null;

                                                try {
                                                    manhnro = InventoryService.gI().findItemBagByTemp(player, 993);
                                                    manhnro1 = InventoryService.gI().findItemBagByTemp(player, 994);
                                                    manhnro2 = InventoryService.gI().findItemBagByTemp(player, 995);
                                                    manhnro3 = InventoryService.gI().findItemBagByTemp(player, 996);
                                                    manhnro4 = InventoryService.gI().findItemBagByTemp(player, 997);
                                                    manhnro5 = InventoryService.gI().findItemBagByTemp(player, 998);
                                                    manhnro6 = InventoryService.gI().findItemBagByTemp(player, 999);

                                                } catch (Exception e) {
                                                }
                                                if (manhnro == null || manhnro.quantity < 999 || manhnro1 == null || manhnro1.quantity < 999
                                                        || manhnro2 == null || manhnro2.quantity < 999 || manhnro3 == null || manhnro3.quantity < 999
                                                        || manhnro4 == null || manhnro4.quantity < 999 || manhnro5 == null || manhnro5.quantity < 999
                                                        || manhnro6 == null || manhnro6.quantity < 999) {
                                                    this.npcChat(player, "Bạn Không Có Vật Phẩm Nào");
                                                } else if (InventoryService.gI().getCountEmptyBag(player) == 0) {
                                                    this.npcChat(player, "Hành Trang Của Bạn Không Đủ Chỗ Trống");

                                                } else {
                                                    InventoryService.gI().subQuantityItemsBag(player, manhnro, 999);
                                                    InventoryService.gI().subQuantityItemsBag(player, manhnro1, 999);
                                                    InventoryService.gI().subQuantityItemsBag(player, manhnro2, 999);
                                                    InventoryService.gI().subQuantityItemsBag(player, manhnro3, 999);
                                                    InventoryService.gI().subQuantityItemsBag(player, manhnro4, 999);
                                                    InventoryService.gI().subQuantityItemsBag(player, manhnro5, 999);
                                                    InventoryService.gI().subQuantityItemsBag(player, manhnro6, 999);

                                                    Item nrosieucap = ItemService.gI().createNewItem((short) 981);
                                                    nrosieucap.itemOptions.add(new ItemOption(174, 2024));
                                                    InventoryService.gI().addItemBag(player, nrosieucap, 99999);
                                                    InventoryService.gI().sendItemBags(player);
                                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + nrosieucap.template.name);
                                                    player.Nrosieucap += 1;
                                                    Service.getInstance().sendThongBao(player, "Bạn Nhận Được 1 Điểm Sự Kiện");
                                                }
                                                break;
                                            case 1:
                                                ShopService.gI().openShopSpecial(player, this, ConstNpc.bdkb1, 0, 3);
                                                break;
                                            case 2:
                                                ShopService.gI().openShopSpecial(player, this, ConstNpc.bdkb2, 1, 3);
                                                break;
                                            case 3:
                                                Service.getInstance().showListTop(player, Manager.TOPsknro);
                                                break;

                                        }

                                    }
                                }
                            }
                        }
                    };
                    break;

                case ConstNpc.QUY_LAO_KAME:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                            "Chào Con, Con Muốn Ta Giúp Gì Nào?", "Chức Năng", "Nhận\nDanh Hiệu", "Đến \nTiên Môn", "Đến\nThái Cực\nĐiện",
                                            "Đến \nNam \nThiên Môn", "Đến\nVõ Đài\nThiên Giới", "Đến\nTây Phương\nCực Lạc",
                                            "Từ Chối");
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (player.iDMark.isBaseMenu()) {
                                    switch (select) {
                                        case 0:
                                            this.createOtherMenu(player, ConstNpc.NOI_CHUYEN,
                                                    "Chào Con, Ta Rất Vui Khi Gặp Con\nCon Muốn Làm Gì Nào?",
                                                    "Nhiệm Vụ", "Bỏ Qua\nNhiệm Vụ", "Về Khu\nVực Bang",
                                                    "Giản Tán\nBang Hội", "Kho Báu\nDưới Biển");
                                            break;
                                        case 1:
                                            this.createOtherMenu(player, ConstNpc.MENU_QUY_DOI_DANH_HIEU, "Con Muốn Nhận Danh Hiệu gì?\n"
                                                    + "Chăm Chỉ Cày Để Đổi Nha Liuliu\n"
                                                    + "Danh Hiệu Tối Thượng 1-100% 18S HP, KI, SD, DEF\n"
                                                    + "Danh Hiệu Săn Boss 1-200% 18S HP, KI, SD, DEF\n"
                                                    + "Danh Hiệu Đại Gia 1-500% 18S HP, KI, SD, DEF\n"
                                                    + "Danh Hiệu Nông Dân 1-1000% 18S HP, KI, SD, DEF\n",
                                                    "Danh Hiệu\nTối Thượng", "Danh Hiệu\n Săn Boss", "Danh Hiệu\nĐại Gia", "Danh Hiệu\nNông Dân");

                                            break;

                                        case 2:

                                            if (player.vip < 0) {
                                                Service.getInstance().sendThongBaoFromAdmin(player, "Cần Đạt Tối Thiểu VIP 4 Để Lên");
                                                return;
                                            }
                                            ChangeMapService.gI().changeMapBySpaceShip(player, 206, -1, 354);
                                            break;
                                        case 3:
                                            if (player.vip < 0) {
                                                Service.getInstance().sendThongBaoFromAdmin(player, "Cần Đạt Tối Thiểu vip 5 Để Lên");
                                                return;
                                            }
                                            ChangeMapService.gI().changeMapBySpaceShip(player, 207, -1, 354);
                                            break;
                                        case 4:
                                            if (player.vip < 0) {
                                                Service.getInstance().sendThongBaoFromAdmin(player, "Cần Đạt Tối Thiểu vip 6 Để Lên");
                                                return;
                                            }
                                            ChangeMapService.gI().changeMapBySpaceShip(player, 208, -1, 354);
                                            break;

                                        case 5:
                                            if (player.vip < 0) {
                                                Service.getInstance().sendThongBaoFromAdmin(player, "Cần Đạt Tối Thiểu vip 7 Để Lên");
                                                return;
                                            }

                                            ChangeMapService.gI().changeMapBySpaceShip(player, 209, -1, 354);
                                            break;

                                        case 6:

                                            if (player.vip < 0) {
                                                Service.getInstance().sendThongBaoFromAdmin(player, "Cần Đạt Tối Thiểu vip 8 Để Lên");
                                                return;
                                            }
                                            ChangeMapService.gI().changeMapBySpaceShip(player, 210, -1, 354);
                                            break;

                                    }
                                } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_QUY_DOI_DANH_HIEU) {
                                    switch (select) {
                                        case 0:
                                            this.createOtherMenu(player, 123, "Bạn Có Muốn Đổi 1k29 Triệu tỉ sức mạnh lấy Danh Hiệu Này Không ?\n"
                                                    + "\n|7|Lưu ý: Danh hiệu có chỉ số ngẫu nhiên tối đa 100%\n"
                                                    + "Số điểm Sức Mạnh: " + player.nPoint.power + " Của Bạn\n"
                                                    + "\n|7|Lưu ý: Mỗi lần quy đổi sẽ tốn 5000 tỷ vàng \n",
                                                    "Đồng ý", "Từ chối");
                                            break;
                                        case 1:
                                            this.createOtherMenu(player, 1234, "Bạn có muốn sử dụng 1 Triệu điểm săn boss Để Đổi Danh Hiệu Này Không?\n"
                                                    + "Số điểm săn boss của bạn: " + player.point_sb + " điểm\n"
                                                    + "\n|7|Lưu ý: Danh hiệu có chỉ số ngẫu nhiên tối đa 200%",
                                                    "Đồng ý", "Từ chối");
                                            break;
                                        case 2:
                                            this.createOtherMenu(player, 12345, "Bạn có muốn sử dụng 10.000 cấp Nhập Ma Để Đổi Danh Hiệu Này Không?\n"
                                                    + "Số Cấp Nhập Ma của bạn: " + player.LbTamkjll + " Cấp\n"
                                                    + "\n|7|Lưu ý: Danh hiệu có chỉ số ngẫu nhiên 1% tối đa 500%",
                                                    "Đồng ý", "Từ chối");
                                            break;
                                        case 3:
                                            this.createOtherMenu(player, 12347, "Bạn có muốn sử dụng 200k Điểm SK Để Đổi Danh Hiệu Này Không?\n"
                                                    + "Số điểm SK của bạn: " + player.diemsk + " điểm\n"
                                                    + "\n|7|Lưu ý: Danh hiệu có chỉ số ngẫu nhiên tối đa 1000%",
                                                    "Đồng ý", "Từ chối");
                                            break;
                                    }

                                } else if (player.iDMark.getIndexMenu() == 123) {
                                    if (select == 0) {
                                        if (player.nPoint.power > 1299000000000000000L && player.inventory.gold >= 5000_000_000_000L) {
                                            player.inventory.gold -= 5000_000_000_000L;
                                            player.nPoint.power -= 1299000000000000000L;
                                            Service.getInstance().point(player);
                                            Service.getInstance().sendMoney(player);
                                            Item danhhieu = null;
                                            if (player.gender == 0) {
                                                danhhieu = ItemService.gI().createNewItem((short) 1715, 1);
                                            } else if (player.gender == 1) {
                                                danhhieu = ItemService.gI().createNewItem((short) 1716, 1);
                                            } else {
                                                danhhieu = ItemService.gI().createNewItem((short) 1717, 1);
                                            }
                                            int tile = Util.nextInt(0, 100);
                                            if (tile < 40) {
                                                danhhieu.itemOptions.add(new ItemOption(50, Util.nextInt(1, 100)));
                                                danhhieu.itemOptions.add(new ItemOption(77, Util.nextInt(1, 100)));
                                                danhhieu.itemOptions.add(new ItemOption(103, Util.nextInt(1, 100)));
                                                danhhieu.itemOptions.add(new ItemOption(30, Util.nextInt(1, 500)));
                                                danhhieu.itemOptions.add(new ItemOption(107, Util.nextInt(1, 18)));
                                            } else if (tile < 80) {
                                                danhhieu.itemOptions.add(new ItemOption(50, Util.nextInt(1, 100)));
                                                danhhieu.itemOptions.add(new ItemOption(77, Util.nextInt(1, 100)));
                                                danhhieu.itemOptions.add(new ItemOption(103, Util.nextInt(1, 100)));
                                                danhhieu.itemOptions.add(new ItemOption(107, Util.nextInt(1, 18)));
                                                danhhieu.itemOptions.add(new ItemOption(30, Util.nextInt(1, 500)));

                                            } else {
                                                danhhieu.itemOptions.add(new ItemOption(50, Util.nextInt(1, 100)));
                                                danhhieu.itemOptions.add(new ItemOption(77, Util.nextInt(1, 100)));
                                                danhhieu.itemOptions.add(new ItemOption(103, Util.nextInt(1, 100)));
                                                danhhieu.itemOptions.add(new ItemOption(30, Util.nextInt(1, 500)));
                                                danhhieu.itemOptions.add(new ItemOption(107, Util.nextInt(1, 18)));
                                            }
                                            InventoryService.gI().addItemBag(player, danhhieu, 999999);
                                            InventoryService.gI().sendItemBags(player);
                                            Service.getInstance().sendThongBao(player, "Bạn Đã Nhận Được danh hiệu Tối Thượng");
                                        } else {
                                            Service.getInstance().sendThongBao(player, "Bạn chưa đạt đủ 100 Triệu tỉ hoặc 5000 tỷ vàng để quy đổi");
                                        }
                                    }
                                } else if (player.iDMark.getIndexMenu() == 1234) {
                                    if (select == 0) {
                                        if (player.point_sb > 1000000) {
                                            player.point_sb -= 1000000;
                                            Item danhhieu = null;

                                            danhhieu = ItemService.gI().createNewItem((short) 1718, 1);

                                            int tile = Util.nextInt(0, 100);
                                            if (tile < 40) {
                                                if (tile < 10) {
                                                    danhhieu.itemOptions.add(new ItemOption(50, Util.nextInt(1, 200)));
                                                    danhhieu.itemOptions.add(new ItemOption(77, Util.nextInt(1, 200)));
                                                    danhhieu.itemOptions.add(new ItemOption(103, Util.nextInt(1, 200)));
                                                    danhhieu.itemOptions.add(new ItemOption(5, Util.nextInt(1, 5)));
                                                    danhhieu.itemOptions.add(new ItemOption(30, Util.nextInt(1, 500)));
                                                    danhhieu.itemOptions.add(new ItemOption(107, Util.nextInt(1, 18)));
                                                } else {
                                                    danhhieu.itemOptions.add(new ItemOption(50, Util.nextInt(1, 200)));
                                                    danhhieu.itemOptions.add(new ItemOption(77, Util.nextInt(1, 200)));
                                                    danhhieu.itemOptions.add(new ItemOption(103, Util.nextInt(1, 200)));
                                                    danhhieu.itemOptions.add(new ItemOption(5, Util.nextInt(1, 5)));
                                                    danhhieu.itemOptions.add(new ItemOption(30, Util.nextInt(1, 500)));
                                                    danhhieu.itemOptions.add(new ItemOption(107, Util.nextInt(1, 18)));
                                                }
                                            } else if (tile < 80) {
                                                if (tile < 50) {
                                                    danhhieu.itemOptions.add(new ItemOption(50, Util.nextInt(1, 200)));
                                                    danhhieu.itemOptions.add(new ItemOption(77, Util.nextInt(1, 200)));
                                                    danhhieu.itemOptions.add(new ItemOption(103, Util.nextInt(1, 200)));
                                                    danhhieu.itemOptions.add(new ItemOption(5, Util.nextInt(1, 5)));
                                                    danhhieu.itemOptions.add(new ItemOption(30, Util.nextInt(1, 500)));
                                                    danhhieu.itemOptions.add(new ItemOption(107, Util.nextInt(1, 18)));
                                                } else {
                                                    danhhieu.itemOptions.add(new ItemOption(50, Util.nextInt(1, 200)));
                                                    danhhieu.itemOptions.add(new ItemOption(77, Util.nextInt(1, 200)));
                                                    danhhieu.itemOptions.add(new ItemOption(103, Util.nextInt(1, 200)));
                                                    danhhieu.itemOptions.add(new ItemOption(5, Util.nextInt(1, 5)));
                                                    danhhieu.itemOptions.add(new ItemOption(30, Util.nextInt(1, 500)));
                                                    danhhieu.itemOptions.add(new ItemOption(107, Util.nextInt(1, 18)));
                                                }
                                            } else {
                                                if (tile < 5) {
                                                    danhhieu.itemOptions.add(new ItemOption(50, Util.nextInt(1, 200)));
                                                    danhhieu.itemOptions.add(new ItemOption(77, Util.nextInt(1, 200)));
                                                    danhhieu.itemOptions.add(new ItemOption(103, Util.nextInt(1, 200)));
                                                    danhhieu.itemOptions.add(new ItemOption(5, Util.nextInt(1, 5)));
                                                    danhhieu.itemOptions.add(new ItemOption(30, Util.nextInt(1, 500)));
                                                    danhhieu.itemOptions.add(new ItemOption(107, Util.nextInt(1, 18)));
                                                } else {
                                                    danhhieu.itemOptions.add(new ItemOption(50, Util.nextInt(1, 200)));
                                                    danhhieu.itemOptions.add(new ItemOption(77, Util.nextInt(1, 200)));
                                                    danhhieu.itemOptions.add(new ItemOption(103, Util.nextInt(1, 200)));
                                                    danhhieu.itemOptions.add(new ItemOption(5, Util.nextInt(1, 5)));
                                                    danhhieu.itemOptions.add(new ItemOption(30, Util.nextInt(1, 500)));
                                                    danhhieu.itemOptions.add(new ItemOption(107, Util.nextInt(1, 18)));
                                                }
                                            }
                                            InventoryService.gI().addItemBag(player, danhhieu, 999999);
                                            InventoryService.gI().sendItemBags(player);
                                            Service.getInstance().sendThongBao(player, "Bạn Đã Nhận Được danh hiệu Săn Boss");
                                        } else {
                                            this.npcChat(player, "Bạn chưa đủ điểm để quy đổi");

                                        }

                                    }
                                } else if (player.iDMark.getIndexMenu() == 12345) {
                                    if (select == 0) {
                                        if (player.LbTamkjll > 10000) {
                                            player.LbTamkjll -= 10000;
                                            Item danhhieu = null;

                                            danhhieu = ItemService.gI().createNewItem((short) 1714, 1);

                                            int tile = Util.nextInt(0, 100);
                                            if (tile < 40) {
                                                if (tile < 10) {
                                                    danhhieu.itemOptions.add(new ItemOption(50, Util.nextInt(1, 500)));
                                                    danhhieu.itemOptions.add(new ItemOption(77, Util.nextInt(1, 500)));
                                                    danhhieu.itemOptions.add(new ItemOption(103, Util.nextInt(1, 500)));
                                                    danhhieu.itemOptions.add(new ItemOption(5, Util.nextInt(1, 10)));
                                                    danhhieu.itemOptions.add(new ItemOption(30, Util.nextInt(1, 500)));
                                                    danhhieu.itemOptions.add(new ItemOption(107, Util.nextInt(1, 18)));
                                                } else {
                                                    danhhieu.itemOptions.add(new ItemOption(50, Util.nextInt(1, 500)));
                                                    danhhieu.itemOptions.add(new ItemOption(77, Util.nextInt(1, 500)));
                                                    danhhieu.itemOptions.add(new ItemOption(103, Util.nextInt(1, 500)));
                                                    danhhieu.itemOptions.add(new ItemOption(5, Util.nextInt(1, 10)));
                                                    danhhieu.itemOptions.add(new ItemOption(30, Util.nextInt(1, 500)));
                                                    danhhieu.itemOptions.add(new ItemOption(107, Util.nextInt(1, 18)));
                                                }
                                            } else if (tile < 80) {
                                                if (tile < 50) {
                                                    danhhieu.itemOptions.add(new ItemOption(50, Util.nextInt(1, 500)));
                                                    danhhieu.itemOptions.add(new ItemOption(77, Util.nextInt(1, 500)));
                                                    danhhieu.itemOptions.add(new ItemOption(103, Util.nextInt(1, 500)));
                                                    danhhieu.itemOptions.add(new ItemOption(5, Util.nextInt(1, 10)));
                                                    danhhieu.itemOptions.add(new ItemOption(30, Util.nextInt(1, 500)));
                                                    danhhieu.itemOptions.add(new ItemOption(107, Util.nextInt(1, 18)));
                                                } else {
                                                    danhhieu.itemOptions.add(new ItemOption(50, Util.nextInt(1, 500)));
                                                    danhhieu.itemOptions.add(new ItemOption(77, Util.nextInt(1, 500)));
                                                    danhhieu.itemOptions.add(new ItemOption(103, Util.nextInt(1, 500)));
                                                    danhhieu.itemOptions.add(new ItemOption(5, Util.nextInt(1, 10)));
                                                    danhhieu.itemOptions.add(new ItemOption(30, Util.nextInt(1, 500)));
                                                    danhhieu.itemOptions.add(new ItemOption(107, Util.nextInt(1, 18)));
                                                }
                                            } else {
                                                if (tile < 5) {
                                                    danhhieu.itemOptions.add(new ItemOption(50, Util.nextInt(1, 500)));
                                                    danhhieu.itemOptions.add(new ItemOption(77, Util.nextInt(1, 500)));
                                                    danhhieu.itemOptions.add(new ItemOption(103, Util.nextInt(1, 500)));
                                                    danhhieu.itemOptions.add(new ItemOption(5, Util.nextInt(1, 10)));
                                                    danhhieu.itemOptions.add(new ItemOption(30, Util.nextInt(1, 500)));
                                                    danhhieu.itemOptions.add(new ItemOption(107, Util.nextInt(1, 18)));
                                                } else {
                                                    danhhieu.itemOptions.add(new ItemOption(50, Util.nextInt(1, 500)));
                                                    danhhieu.itemOptions.add(new ItemOption(77, Util.nextInt(1, 500)));
                                                    danhhieu.itemOptions.add(new ItemOption(103, Util.nextInt(1, 500)));
                                                    danhhieu.itemOptions.add(new ItemOption(5, Util.nextInt(1, 10)));
                                                    danhhieu.itemOptions.add(new ItemOption(30, Util.nextInt(1, 500)));
                                                    danhhieu.itemOptions.add(new ItemOption(107, Util.nextInt(1, 18)));
                                                }
                                            }
                                            InventoryService.gI().addItemBag(player, danhhieu, 999999);
                                            InventoryService.gI().sendItemBags(player);
                                            Service.getInstance().sendThongBao(player, "Bạn Đã Nhận Được danh hiệu đại gia");
                                        } else {
                                            this.npcChat(player, "Bạn chưa đủ điểm để quy đổi");
                                        }

                                    }
                                } else if (player.iDMark.getIndexMenu() == 12347) {
                                    if (select == 0) {
                                        if (player.diemsk > 200000) {
                                            player.diemsk -= 200000;
                                            Item danhhieu = null;

                                            danhhieu = ItemService.gI().createNewItem((short) 1711, 1);

                                            int tile = Util.nextInt(0, 100);
                                            if (tile < 40) {
                                                if (tile < 10) {
                                                    danhhieu.itemOptions.add(new ItemOption(50, Util.nextInt(1, 1000)));
                                                    danhhieu.itemOptions.add(new ItemOption(77, Util.nextInt(1, 1000)));
                                                    danhhieu.itemOptions.add(new ItemOption(103, Util.nextInt(1, 1000)));
                                                    danhhieu.itemOptions.add(new ItemOption(5, Util.nextInt(1, 20)));
                                                    danhhieu.itemOptions.add(new ItemOption(30, Util.nextInt(1, 500)));
                                                    danhhieu.itemOptions.add(new ItemOption(107, Util.nextInt(1, 18)));
                                                } else {
                                                    danhhieu.itemOptions.add(new ItemOption(50, Util.nextInt(1, 1000)));
                                                    danhhieu.itemOptions.add(new ItemOption(77, Util.nextInt(1, 1000)));
                                                    danhhieu.itemOptions.add(new ItemOption(103, Util.nextInt(1, 1000)));
                                                    danhhieu.itemOptions.add(new ItemOption(5, Util.nextInt(1, 20)));
                                                    danhhieu.itemOptions.add(new ItemOption(30, Util.nextInt(1, 500)));
                                                    danhhieu.itemOptions.add(new ItemOption(107, Util.nextInt(1, 18)));
                                                }
                                            } else if (tile < 80) {
                                                if (tile < 50) {
                                                    danhhieu.itemOptions.add(new ItemOption(50, Util.nextInt(1, 1000)));
                                                    danhhieu.itemOptions.add(new ItemOption(77, Util.nextInt(1, 1000)));
                                                    danhhieu.itemOptions.add(new ItemOption(103, Util.nextInt(1, 1000)));
                                                    danhhieu.itemOptions.add(new ItemOption(5, Util.nextInt(1, 10)));
                                                    danhhieu.itemOptions.add(new ItemOption(30, Util.nextInt(1, 500)));
                                                    danhhieu.itemOptions.add(new ItemOption(107, Util.nextInt(1, 18)));
                                                } else {
                                                    danhhieu.itemOptions.add(new ItemOption(50, Util.nextInt(1, 1000)));
                                                    danhhieu.itemOptions.add(new ItemOption(77, Util.nextInt(1, 1000)));
                                                    danhhieu.itemOptions.add(new ItemOption(103, Util.nextInt(1, 1000)));
                                                    danhhieu.itemOptions.add(new ItemOption(5, Util.nextInt(1, 20)));
                                                    danhhieu.itemOptions.add(new ItemOption(30, Util.nextInt(1, 500)));
                                                    danhhieu.itemOptions.add(new ItemOption(107, Util.nextInt(1, 18)));
                                                }
                                            } else {
                                                if (tile < 5) {
                                                    danhhieu.itemOptions.add(new ItemOption(50, Util.nextInt(1, 1000)));
                                                    danhhieu.itemOptions.add(new ItemOption(77, Util.nextInt(1, 1000)));
                                                    danhhieu.itemOptions.add(new ItemOption(103, Util.nextInt(1, 1000)));
                                                    danhhieu.itemOptions.add(new ItemOption(5, Util.nextInt(1, 20)));
                                                    danhhieu.itemOptions.add(new ItemOption(30, Util.nextInt(1, 500)));
                                                    danhhieu.itemOptions.add(new ItemOption(107, Util.nextInt(1, 18)));
                                                } else {
                                                    danhhieu.itemOptions.add(new ItemOption(50, Util.nextInt(1, 1000)));
                                                    danhhieu.itemOptions.add(new ItemOption(77, Util.nextInt(1, 1000)));
                                                    danhhieu.itemOptions.add(new ItemOption(103, Util.nextInt(1, 1000)));
                                                    danhhieu.itemOptions.add(new ItemOption(5, Util.nextInt(1, 20)));
                                                    danhhieu.itemOptions.add(new ItemOption(30, Util.nextInt(1, 500)));
                                                    danhhieu.itemOptions.add(new ItemOption(107, Util.nextInt(1, 18)));
                                                }
                                            }
                                            InventoryService.gI().addItemBag(player, danhhieu, 999999);
                                            InventoryService.gI().sendItemBags(player);
                                            Service.getInstance().sendThongBao(player, "Bạn Đã Nhận Được danh hiệu Nông Dân");
                                        } else {
                                            this.npcChat(player, "Bạn chưa đủ điểm để quy đổi");
                                        }

                                    }

                                } else if (player.iDMark.getIndexMenu() == ConstNpc.NOI_CHUYEN) {
                                    switch (select) {
                                        case 0:
                                            NpcService.gI().createTutorial(player, 564, "Nhiệm vụ tiếp theo của bạn là: "
                                                    + player.playerTask.taskMain.subTasks.get(player.playerTask.taskMain.index).name);
                                            break;
                                        case 1:
                                            this.openShopLearnSkill(player, ConstNpc.SHOP_LEARN_SKILL, 0);
                                            if (player.playerTask.taskMain.id >= 10 && player.playerTask.taskMain.id <= 17) {
                                                player.playerTask.taskMain.id++; // Increment the id
                                                TaskService.gI().sendTaskMain(player);
                                                TaskService.gI().sendNextTaskMain(player);
                                                player.playerTask.taskMain.index = 0;
                                                Service.getInstance().sendThongBao(player, "Bỏ qua nhiệm vụ thành công");

                                            } else {
                                                Service.getInstance().sendThongBao(player, "Chỉ có thể bỏ qua nhiêm vụ liên quan đến\nBang Hội");

                                            }
                                            break;
                                        case 2:
                                            if (player.clan == null) {
                                                Service.getInstance().sendThongBao(player, "Chưa có bang hội");
                                                return;
                                            }
                                            if (player.nPoint.power < 80_000_000_000L) {
                                                Service.getInstance().sendThongBao(player, "Bạn chưa đủ 80 tỉ");
                                                return;
                                            }
                                            Zone zone = MapService.gI().getZoneJoinByMapIdAndZoneId(player, 204, 0);
                                            if (zone != null) {
                                                player.location.x = 832;
                                                player.location.y = 144;
                                                MapService.gI().goToMap(player, zone);
                                                Service.getInstance().clearMap(player);
                                                zone.mapInfo(player);
                                                player.zone.loadAnotherToMe(player);
                                                player.zone.load_Me_To_Another(player);
                                            }
                                            break;
                                        case 3:
                                            Clan clan = player.clan;
                                            if (clan != null) {
                                                ClanMember cm = clan.getClanMember((int) player.id);
                                                if (cm != null) {
                                                    if (clan.members.size() > 1) {
                                                        Service.getInstance().sendThongBao(player, "Bang phải còn một người");
                                                        break;
                                                    }
                                                    if (!clan.isLeader(player)) {
                                                        Service.getInstance().sendThongBao(player, "Phải là bang chủ");
                                                        break;
                                                    }
                                                    NpcService.gI().createMenuConMeo(player, ConstNpc.CONFIRM_DISSOLUTION_CLAN, -1, "Con có chắc chắn muốn giải tán bang hội không? Ta cho con 2 lựa chọn...",
                                                            "Yes you do!", "Từ chối!");
                                                }
                                                break;
                                            }
                                            Service.getInstance().sendThongBao(player, "Có bang hội đâu ba!!!");
                                            break;
                                        case 4:
                                            if (player.clan != null) {
                                                if (player.clan.banDoKhoBau != null) {
                                                    this.createOtherMenu(player, ConstNpc.MENU_OPENED_DBKB,
                                                            "Bang hội của con đang đi tìm kho báu dưới biển cấp độ "
                                                            + player.clan.banDoKhoBau.level
                                                            + "\nCon có muốn đi theo không?",
                                                            "Đồng ý", "Từ chối");
                                                } else {
                                                    this.createOtherMenu(player, ConstNpc.MENU_OPEN_DBKB,
                                                            "Đây là bản đồ kho báu hải tặc tí hon\nCác con cứ yên tâm lên đường\n"
                                                            + "Ở đây có ta lo\nNhớ chọn cấp độ vừa sức mình nhé",
                                                            "Chọn\ncấp độ", "Từ chối");
                                                }
                                            } else {
                                                NpcService.gI().createTutorial(player, 564, "Con phải có bang hội ta mới có thể cho con đi");
                                            }
                                            break;
                                    }
                                } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPENED_DBKB) {
                                    switch (select) {
                                        case 0:
                                            if (player.isAdmin() || player.nPoint.power >= BanDoKhoBau.POWER_CAN_GO_TO_DBKB) {
                                                ChangeMapService.gI().goToDBKB(player);
                                            } else {
                                                this.npcChat(player, "Sức mạnh của con phải ít nhất phải đạt "
                                                        + Util.numberToMoney(BanDoKhoBau.POWER_CAN_GO_TO_DBKB));
                                            }
                                            break;

                                    }
                                } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPEN_DBKB) {
                                    switch (select) {
                                        case 0:
                                            if (player.isAdmin()
                                                    || player.nPoint.power >= BanDoKhoBau.POWER_CAN_GO_TO_DBKB) {
                                                Input.gI().createFormChooseLevelBDKB(player);
                                            } else {
                                                this.npcChat(player, "Sức mạnh của con phải ít nhất phải đạt "
                                                        + Util.numberToMoney(BanDoKhoBau.POWER_CAN_GO_TO_DBKB));
                                            }
                                            break;
                                    }

                                } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_ACCEPT_GO_TO_BDKB) {
                                    switch (select) {
                                        case 0:
                                            BanDoKhoBauService.gI().openBanDoKhoBau(player,
                                                    Byte.parseByte(String.valueOf(PLAYERID_OBJECT.get(player.id))));
                                            break;
                                    }

                                }
                            }
                        }

                    };
                    break;
                case ConstNpc.TUTIEN:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 5) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "|7|SỰ KIỆN 8/3\n"
                                            + "|4|Thu thập Hoa & Tặng Quà\n"
                                            + "NPC:  Hoa Tiên Nữ\n"
                                            + "Người chơi thu thập Hoa Đỏ và Hoa Xanh từ quái\n"
                                            + "Chế tạo Bó Hoa Lớn tại Npc Hoa Tiên Nữ & Tặng 1-3 điểm SK\n"
                                            + "|7|Yêu cầu:  5000 Đậu God & 10.000 Lưu ly & 5000 Thỏi Vàng\n"
                                            + "|6|Bó Hoa dùng tặng NPC:  Hoa Tiên Nữ tại đây\n"
                                            + "Nhận thưởng ngay 1 Phụ Kiện VIP random vĩnh viễn 99 sao 100%\n"
                                            + "Nhận kèm phần quà giá trị gồm 5 cấp Tiên Bang & 5 cấp Nhập Ma\n"
                                            + "|7|Điểm Sự Kiện: " + Util.cap(player.diemsk) + " điểm\n",
                                            "Tạo\nBó Hoa", "Tặng\nBó Hoa", "Shop\nỦng Hộ", "TOP\nSự Kiện", "Từ chối");
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 5) {
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {
                                            case 0:
                                                Item gao = null;
                                                Item la = null;
                                                Item daugod = null;

                                                Item thoivang = null;
                                                Item luuly = null;
                                                try {
                                                    gao = InventoryService.gI().findItemBagByTemp(player, 2048);
                                                    la = InventoryService.gI().findItemBagByTemp(player, 2049);
                                                    daugod = InventoryService.gI().findItemBagByTemp(player, 1111);
                                                    thoivang = InventoryService.gI().findItemBagByTemp(player, 457);
                                                    luuly = InventoryService.gI().findItemBagByTemp(player, 1404);

                                                } catch (Exception e) {
                                                }
                                                if (gao == null
                                                        || la == null
                                                        || daugod == null
                                                        || thoivang == null
                                                        || luuly == null
                                                        || gao.quantity < 99
                                                        || la.quantity < 99
                                                        || daugod.quantity < 5000
                                                        || thoivang.quantity < 5000
                                                        || luuly.quantity < 10000) {
                                                    this.npcChat(player, "Bạn Cần x99 Hoa Xanh + Đỏ + 5000 đậu god + 5k Thỏi Vàng + 10k Lưu Ly");
                                                } else if (InventoryService.gI().getCountEmptyBag(player) == 7) {
                                                    this.npcChat(player, "Hành Trang Của Bạn Không Đủ Chỗ Trống");

                                                } else {

                                                    InventoryService.gI().subQuantityItemsBag(player, gao, 99);
                                                    InventoryService.gI().subQuantityItemsBag(player, la, 99);
                                                    InventoryService.gI().subQuantityItemsBag(player, daugod, 5000);
                                                    InventoryService.gI().subQuantityItemsBag(player, thoivang, 5000);
                                                    InventoryService.gI().subQuantityItemsBag(player, luuly, 10000);

                                                    Item banhtet = ItemService.gI().createNewItem((short) Util.nextInt(954, 955));
                                                    banhtet.itemOptions.add(new ItemOption(30, 2024));
                                                    InventoryService.gI().addItemBag(player, banhtet, 99999999);
                                                    InventoryService.gI().sendItemBags(player);
                                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + banhtet.template.name);

                                                    player.diemsk += Util.nextInt(1, 5);
                                                    Service.getInstance().sendThongBao(player, "Bạn Nhận Được 1-3 Điểm Sự Kiện");
                                                }
                                                break;

                                            case 1:
                                                Item hoa1 = null;
                                                Item hoa2 = null;

                                                try {
                                                    hoa1 = InventoryService.gI().findItemBagByTemp(player, 954);
                                                    hoa2 = InventoryService.gI().findItemBagByTemp(player, 955);

                                                } catch (Exception e) {
                                                }
                                                if (hoa1 == null
                                                        || hoa2 == null
                                                        || hoa1.quantity < 1
                                                        || hoa2.quantity < 1) {
                                                    this.npcChat(player, "Bạn Cần 2 Bó Hoa Hồng Và Hoa Vàng");
                                                } else if (InventoryService.gI().getCountEmptyBag(player) == 7) {
                                                    this.npcChat(player, "Hành Trang Của Bạn Không Đủ Chỗ Trống");

                                                } else {

                                                    InventoryService.gI().subQuantityItemsBag(player, hoa1, 1);
                                                    InventoryService.gI().subQuantityItemsBag(player, hoa2, 1);

                                                    Item banhtet = ItemService.gI().createNewItem((short) Util.nextInt(1552, 1554));
                                                    banhtet.itemOptions.add(new ItemOption(50, Util.nextInt(1, 1000)));
                                                    banhtet.itemOptions.add(new ItemOption(77, Util.nextInt(1, 1000)));
                                                    banhtet.itemOptions.add(new ItemOption(103, Util.nextInt(1, 1000)));
                                                    banhtet.itemOptions.add(new ItemOption(78, Util.nextInt(1, 120)));
                                                    banhtet.itemOptions.add(new ItemOption(14, Util.nextInt(1, 12)));
                                                    banhtet.itemOptions.add(new ItemOption(241, Util.nextInt(1, 12)));
                                                    banhtet.itemOptions.add(new ItemOption(107, Util.nextInt(1, 18)));
                                                    banhtet.itemOptions.add(new ItemOption(30, 2024));

                                                    int randomChance = Util.nextInt(1, 101); // Random từ 1 đến 100
                                                    if (randomChance <= 98) { // 10% tỉ lệ ra opt 93
                                                        banhtet.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7))); // Giá trị random từ 1 - 500
                                                    }
                                                    InventoryService.gI().addItemBag(player, banhtet, 99999999);
                                                    InventoryService.gI().sendItemBags(player);
                                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + banhtet.template.name);

                                                    Item banhtet1 = ItemService.gI().createNewItem((short) Util.nextInt(1150, 1154));
                                                    banhtet1.itemOptions.add(new ItemOption(31, 2025));
                                                    InventoryService.gI().addItemBag(player, banhtet1, 99999999);
                                                    InventoryService.gI().sendItemBags(player);
                                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + banhtet1.template.name);
                                                    player.diemsk += Util.nextInt(1, 3);
                                                    player.CapTamkjll += 5;
                                                    player.LbTamkjll += 5;
                                                    Service.getInstance().sendThongBao(player, "Bạn Nhận Được 5 Cấp Tiên Bang & 5 cấp nhập ma & và điểm skien");
                                                }
                                                break;

//                                      
                                            case 2:
                                                ShopService.gI().openShopSpecial(player, this, ConstNpc.trungthu, 0, 3);
                                                break;//đù nro jav:)) 
//                                            case 3:
////                                                ShopService.gI().openShopSpecial(player, this, ConstNpc.trungthu1, 1, 3);
////                                                break;//đù nro jav:)) 
//                                                this.npcChat(player, "Đang Update");
//                                                break;
//
//                                            case 4:
//                                                if (player.diemsk < 10) {
//                                                    Service.getInstance().sendThongBao(player, "Bạn cần ít nhất 10 Điểm sk. Để Đổi");
//                                                    return;
//                                                }
//                                                Item manhnro = null;
//                                                try {
//                                                    manhnro = InventoryService.gI().findItemBagByTemp(player, 457);
//                                                } catch (Exception e) {
//                                                }
//                                                if (manhnro == null) {
//                                                    this.npcChat(player, "Bạn Không Có Vật Phẩm Nào");
//                                                } else if (InventoryService.gI().getCountEmptyBag(player) == 0) {
//                                                    this.npcChat(player, "Hành Trang Của Bạn Không Đủ Chỗ Trống");
//
//                                                } else {
//                                                    InventoryService.gI().subQuantityItemsBag(player, manhnro, 0);
//                                                    Item nrosieucap = ItemService.gI().createNewItem((short) 1146, 1);
//                                                    nrosieucap.itemOptions.add(new ItemOption(174, 2024));
//                                                    InventoryService.gI().addItemBag(player, nrosieucap, 9999999);
//                                                    InventoryService.gI().sendItemBags(player);
//                                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + nrosieucap.template.name);
//                                                    player.diemsk -= 10;
//                                                    Service.getInstance().sendThongBao(player, "Bạn Bị Trừ 10 Điểm SK Để Đổi Lixi Lớn");
//                                                }
//                                                break;

                                            case 3:
                                                Service.getInstance().showListTop(player, Manager.TOPSK);
                                                break;
                                        }

                                    }
                                }
                            }
                        }
                    };
                    break;

                case ConstNpc.kethon:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                Item trungquy1 = null;
                                Item trungquy2 = null;
                                Item trungquy3 = null;
                                Item thoivang = null;
                                Item bonghong = null;
                                try {
                                    trungquy1 = InventoryService.gI().findItemBagByTemp(player, 1092);
                                    trungquy2 = InventoryService.gI().findItemBagByTemp(player, 1093);
                                    trungquy3 = InventoryService.gI().findItemBagByTemp(player, 1094);
                                    thoivang = InventoryService.gI().findItemBagByTemp(player, 457);
                                    bonghong = InventoryService.gI().findItemBagByTemp(player, 723);
                                } catch (Exception e) {
                                }
                                this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                        "|7|KẾT HÔN"
                                        + "\n|5|Bạn cần Nhẫn cầu hôn để thực hiện Kết hôn với người khác"
                                        + "\nĐiều kiện nhận Nhẫn kết hôn:"
                                        + " \n|6|Trứng quỷ LV1: " + (trungquy1 == null ? 0 : trungquy1.quantity) + " / 500" + " ; " + " Trứng quỷ LV2: " + (trungquy2 == null ? 0 : trungquy2.quantity) + " / 500"
                                        + "\nTrứng quỷ LV3: " + (trungquy3 == null ? 0 : trungquy3.quantity) + " / 1000"
                                        + "\nChuyển Sinh Đạt: " + player.TamkjllCS + " / 10"
                                        + "\nĐạt Tiên Bang: " + player.CapTamkjll + " / 100"
                                        + "\nCảnh giới Tu Tiên: Yêu Cầu Cảnh giới: Đại Đế "
                                        + "\nThỏi vàng: " + (thoivang == null ? 0 : thoivang.quantity) + " / 9.999"
                                        + "\nHồng ngọc: " + Util.format(player.inventory.ruby) + " / 9.999"
                                        + "\nBông hồng: " + (bonghong == null ? 0 : bonghong.quantity) + " / 999",
                                        "Nhận nhẫn", "Thông tin\nKết hôn");
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (player.iDMark.isBaseMenu()) {
                                    switch (select) {
                                        case 0:
                                            Item trungquy1 = null;
                                            Item trungquy2 = null;
                                            Item trungquy3 = null;
                                            Item thoivang = null;
                                            Item bonghong = null;
                                            try {
                                                trungquy1 = InventoryService.gI().findItemBagByTemp(player, 1092);
                                                trungquy2 = InventoryService.gI().findItemBagByTemp(player, 1093);
                                                trungquy3 = InventoryService.gI().findItemBagByTemp(player, 1094);
                                                thoivang = InventoryService.gI().findItemBagByTemp(player, 457);
                                                bonghong = InventoryService.gI().findItemBagByTemp(player, 723);
                                            } catch (Exception e) {
                                            }
                                            if (trungquy1 == null || trungquy1.quantity < 500) {
                                                Service.gI().sendThongBaoFromAdmin(player, "Bạn chưa đủ Trứng quỷ LV1 Hẵn Đi Săn Broly");
                                                return;
                                            }
                                            if (trungquy2 == null || trungquy2.quantity < 500) {
                                                Service.gI().sendThongBaoFromAdmin(player, "Bạn chưa đủ Trứng quỷ LV2 Hãy Đi Săn Super Broly");
                                                return;
                                            }
                                            if (trungquy3 == null || trungquy3.quantity < 1000) {
                                                Service.gI().sendThongBaoFromAdmin(player, "Bạn chưa đủ Trứng quỷ LV3 Hãy Đi Up Tại Coler");
                                                return;
                                            }
                                            if (thoivang == null || thoivang.quantity < 9999) {
                                                Service.gI().sendThongBaoFromAdmin(player, "Bạn chưa đủ 9999 Thỏi vàng Hãy Đi Up Of Săn Boss Thỏi Vàng");
                                                return;
                                            }
                                            if (player.inventory.ruby < 9_999) {
                                                Service.gI().sendThongBaoFromAdmin(player, "Bạn chưa đủ Hồng ngọc Hãy Đổi Tài Nguyên hoặc Săn Boss Hồng Ngọc");
                                                return;
                                            }
                                            if (bonghong == null || bonghong.quantity < 99) {
                                                Service.gI().sendThongBaoFromAdmin(player, "Bạn chưa đủ Bông hồng Hãy Đi Mua Miễn Phí Tại Shop Nguyên Liệu");
                                                return;
                                            }

                                            if (player.TamkjllCS < 10) {
                                                Service.gI().sendThongBaoFromAdmin(player, "Bạn chưa đủ số lẩn Chuyển sinh Hãy Up quái Đủ Điểm Vip hoặc Yêu Cầu Cs");
                                                return;
                                            }
                                            if (player.Tamkjlltutien[1] < 171) {
                                                Service.gI().sendThongBaoFromAdmin(player, "Bạn chưa đủ Cấp Tu tiên Hãy Đạt Đến Cảnh giới 171 Cảnh giới Đại Đế");
                                                return;
                                            }

                                            if (player.CapTamkjll < 100) {
                                                Service.gI().sendThongBaoFromAdmin(player, "Bạn Cần 100 Cấp Tiên Bang Trở lên");
                                            } else {
                                                player.inventory.ruby -= 9_999;
                                                // player.dk_bdkb -= 10;
                                                InventoryService.gI().subQuantityItemsBag(player, trungquy1, 500);
                                                InventoryService.gI().subQuantityItemsBag(player, trungquy2, 500);
                                                InventoryService.gI().subQuantityItemsBag(player, trungquy3, 1000);
                                                InventoryService.gI().subQuantityItemsBag(player, thoivang, 9999);
                                                InventoryService.gI().subQuantityItemsBag(player, bonghong, 99);

                                                Item nhan = ItemService.gI().createNewItem((short) 2052);
                                                nhan.itemOptions.add(new ItemOption(50, Util.nextInt(1, 150)));
                                                nhan.itemOptions.add(new ItemOption(77, Util.nextInt(1, 150)));
                                                nhan.itemOptions.add(new ItemOption(103, Util.nextInt(1, 150)));
                                                nhan.itemOptions.add(new ItemOption(95, Util.nextInt(1, 10)));
                                                nhan.itemOptions.add(new ItemOption(96, Util.nextInt(1, 10)));
                                                nhan.itemOptions.add(new ItemOption(101, Util.nextInt(1, 100)));
                                                nhan.itemOptions.add(new ItemOption(5, Util.nextInt(1, 50)));
                                                nhan.itemOptions.add(new ItemOption(30, Util.nextInt(1, 18)));
                                                InventoryService.gI().addItemBag(player, nhan, 9999);

                                                Service.getInstance().sendMoney(player);
                                                InventoryService.gI().sendItemBags(player);
                                                Service.gI().sendThongBao(player, "|5|Đã Nhận Được " + nhan.template.name);
                                            }
                                            break;
                                        case 1:
                                            this.createOtherMenu(player, 54855, "|7|Thông tin kết hôn"
                                                    + "\n|5|Số lần Kết hôn: " + player.dakethon + " Lần"
                                                    + "\n|4|+" + (10000 * player.dakethon) + "% Chỉ số HP, KI, SD"
                                                    + "\n|5|Số lần Được Cầu hôn: " + player.duockethon + " Lần"
                                                    + "\n|4|+" + (1000 * player.duockethon) + "% Chỉ số HP, KI, SD",
                                                    "OK");
                                            break;

                                    }

                                }
                            }

                        }
                    };
                    break;

//                case ConstNpc.thoren:
//                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
//                        @Override
//                        public void openBaseMenu(Player player) {
//                            if (canOpenNpc(player)) {
//                                if (this.mapId == 5 ||this.mapId == 207) {
//                                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
//                                            "|7|Chào Các Bạn\n"
//                                            + "|6|\b" + "Nhớ Nạp Nhiều Nhiều cho ADMIN Nha nhé\n"
//                                            + "|7|" + "\nĐổi Đồ Thần Linh Lấy Đồ Hủy Diệt Có Tỉ Lệ Ra SKH 5000%\n"
//                                            + "|5|" + "\nĐổi Đồ Hủy Diệt Lấy Đồ Thiên Sứ Có Tỉ Lệ Ra SKH 5000%\n"
//                                            + "|7|" + "\nĐồ Hủy Diệt Chỉ Số Random Rèn MAX 1-10Triệu Chỉ Số\n"
//                                            + "|5|" + "\nĐồ Thiên Sứ Chỉ Số Random Rèn MAX 1-50Triệu Chỉ Số\n",
//                                            "Đổi đồ\nHủy Diệt\nTrái Đất", "Đổi đồ\nHuy Diệt\nNamek", "Đổi Đồ\nHủy Diệt\nxayda", "Đổi Đồ\nThiên Sứ\nTrái Đất", "Đổi Đồ\nThiên Sứ\nNamek", "Đổi Đồ\nThiên Sú\nXayda", "Hướng Dẫn\nKiếm Đồ");
//                                }
//                            }
//                        }
//
//                        @Override
//                        public void confirmMenu(Player player, int select) {
//                            if (canOpenNpc(player)) {
//                                if (this.mapId == 5|| this.mapId == 207) {
//                                    if (player.iDMark.isBaseMenu()) {
//                                        switch (select) {
//                                            case 0:
//                                                this.createOtherMenu(player, 1,
//                                                        "Bạn muốn đổi 1 món đồ thần linh \nTrái đất cùng loại và x30.000 đá ngũ sắc \n|6|Để Đổi lấy 1 món đồ húy diệt có tý lệ ra SKH 5000% ko", "Áo\nHúy Diệt", "Quần\nHúy Diệt", "Găng\nDúy Diệt", "Giày\nHúy Diệt", "Nhẫn\nHúy Diệt", "Thôi Khỏi");
//                                                break;
//                                            case 1:
//                                                this.createOtherMenu(player, 2,
//                                                        "Bạn muốn đổi 1 món đồ thần linh \nNamek cùng loại và x30.000 đá ngũ sắc \n|6|Để Đổi lấy 1 món đồ húy diệt có tý lệ ra SKH 5000% ko", "Áo\nHúy Diệt", "Quần\nHúy Diệt", "Găng\nDúy Diệt", "Giày\nHúy Diệt", "Nhẫn\nHúy Diệt", "Thôi Khỏi");
//                                                break;
//                                            case 2:
//                                                this.createOtherMenu(player, 3,
//                                                        "Bạn muốn đổi 1 món đồ thần linh \nXayda cùng loại và x30.000 đá ngũ sắc \n|6|Để Đổi lấy 1 món đồ húy diệt có tý lệ ra SKH 5000% ko", "Áo\nHúy Diệt", "Quần\nHúy Diệt", "Găng\nDúy Diệt", "Giày\nHúy Diệt", "Nhẫn\nHúy Diệt", "Thôi Khỏi");
//                                                break;
//                                            case 3:
//                                                this.createOtherMenu(player, 4,
//                                                        "Bạn muốn đổi 1 món đồ húy diệt \nTrái đất cùng loại và x9.999 đá ngũ sắc \n|6|Để Đổi lấy 1 món đồ thiên sứ có tý lệ rà SKH 5000% ko", "Áo\nThiên sứ", "Quần\nThiên sứ", "Găng\nThiên sứ", "Giày\nThiên Sứ", "Nhẫn\nThiên Sứ", "Từ Chối");
//                                                break;
//                                            case 4:
//                                                this.createOtherMenu(player, 5,
//                                                        "Bạn muốn đổi 1 món đồ húy diệt \nNamek cùng loại và x9.999 đá ngũ sắc \n|6|Để Đổi lấy 1 món đồ thiên sứ có tý lệ rà SKH 5000% ko", "Áo\nThiên sứ", "Quần\nThiên sứ", "Găng\nThiên sứ", "Giày\nThiên Sứ", "Nhẫn\nThiên Sứ", "Từ Chối");
//                                                break;
//                                            case 5:
//                                                this.createOtherMenu(player, 6,
//                                                        "Bạn muốn đổi 1 món đồ húy diệt \nXayda cùng loại và x9.999 đá ngũ sắc \n|6|Để Đổi lấy 1 món đồ thiên sứ có tý lệ rà SKH 5000% ko", "Áo\nThiên sứ", "Quần\nThiên sứ", "Găng\nThiên sứ", "Giày\nThiên Sứ", "Nhẫn\nThiên Sứ", "Từ Chối");
//                                                break;
//                                            case 6:
//                                                NpcService.gI().createMenuConMeo(player, ConstNpc.huongdan, -1,
//                                                        "|7|Chúc Bạn Một Ngày Vui Vẻ\n"
//                                                        + "|5|Kiếm Đồ Thần Linh : Săn Boss Of Up Map Cooler\n"
//                                                        + "Khi Đổi Có Tỉ lệ Ra SKH 5000% VIP\n"
//                                                        + "cÓ Thể Dùng Đồ Hủy Diệt Mua Tại Npc Bill Dùng Rèn Đồ Thiên Sứ\n"
//                                                        + "Khi Đổi Có Tỉ Lệ Ra Đồ Thiên Sứ SKH 5000% VIP\n"
//                                                        + "Đá Ngũ Sắc Có Thể Săn Boss Of Đổi Sự Kiện\n",
//                                                        "Đóng"
//                                                );
//
//                                                break;
//
//                                        }
//                                    } else if (player.iDMark.getIndexMenu() == 1) { // action đổi dồ húy diệt
//                                        switch (select) {
//                                            case 0: // trade
//                                try {
//                                                Item dns = InventoryService.gI().findItem(player.inventory.itemsBag, 674);
//                                                Item tl = InventoryService.gI().findItem(player.inventory.itemsBag, 555);
//                                                int soLuong = 0;
//                                                if (dns != null) {
//                                                    soLuong = dns.quantity;
//                                                }
//                                                for (int i = 0; i < 12; i++) {
//                                                    Item thl = InventoryService.gI().findItem(player.inventory.itemsBag, 555 + i);
//
//                                                    if (InventoryService.gI().existItemBag(player, 555 + i) && soLuong >= 30000) {
//                                                        InventoryService.gI().subQuantityItemsBag(player, dns, 30000);
//                                                        InventoryService.gI().subQuantityItemsBag(player, thl, 1);
//                                                        CombineServiceNew.gI().GetTrangBiKichHoathuydiet(player, 650 + i);
//                                                        this.npcChat(player, "Chuyển Hóa Thành Công!");
//
//                                                        break;
//                                                    } else {
//                                                        this.npcChat(player, "Yêu cầu cần Áo Thần linh trái đất + x30.000 Đá Ngũ Sắc!");
//                                                    }
//
//                                                }
//                                            } catch (Exception e) {
//
//                                            }
//                                            break;
//                                            case 1: // trade
//                                    try {
//                                                Item dns = InventoryService.gI().findItem(player.inventory.itemsBag, 674);
//                                                Item tl = InventoryService.gI().findItem(player.inventory.itemsBag, 556);
//                                                int soLuong = 0;
//                                                if (dns != null) {
//                                                    soLuong = dns.quantity;
//                                                }
//                                                for (int i = 0; i < 12; i++) {
//                                                    Item thl = InventoryService.gI().findItem(player.inventory.itemsBag, 556 + i);
//
//                                                    if (InventoryService.gI().existItemBag(player, 556 + i) && soLuong >= 30000) {
//                                                        InventoryService.gI().subQuantityItemsBag(player, dns, 30000);
//                                                        InventoryService.gI().subQuantityItemsBag(player, thl, 1);
//                                                        CombineServiceNew.gI().GetTrangBiKichHoathuydiet(player, 651 + i);
//                                                        this.npcChat(player, "Chuyển Hóa Thành Công!");
//
//                                                        break;
//                                                    } else {
//                                                        this.npcChat(player, "Yêu cầu cần Quần Thần linh trái đất + x30.000 Đá Ngũ Sắc!");
//                                                    }
//
//                                                }
//                                            } catch (Exception e) {
//
//                                            }
//                                            break;
//                                            case 2: // trade
//                                    try {
//                                                Item dns = InventoryService.gI().findItem(player.inventory.itemsBag, 674);
//                                                Item tl = InventoryService.gI().findItem(player.inventory.itemsBag, 562);
//                                                int soLuong = 0;
//                                                if (dns != null) {
//                                                    soLuong = dns.quantity;
//                                                }
//                                                for (int i = 0; i < 12; i++) {
//                                                    Item thl = InventoryService.gI().findItem(player.inventory.itemsBag, 562 + i);
//
//                                                    if (InventoryService.gI().existItemBag(player, 562 + i) && soLuong >= 30000) {
//                                                        InventoryService.gI().subQuantityItemsBag(player, dns, 30000);
//                                                        InventoryService.gI().subQuantityItemsBag(player, thl, 1);
//                                                        CombineServiceNew.gI().GetTrangBiKichHoathuydiet(player, 657 + i);
//                                                        this.npcChat(player, "Chuyển Hóa Thành Công!");
//
//                                                        break;
//                                                    } else {
//                                                        this.npcChat(player, "Yêu cầu cần Găng Thần linh trái đất + x30.000 Đá Ngũ Sắc!");
//                                                    }
//
//                                                }
//                                            } catch (Exception e) {
//
//                                            }
//                                            break;
//                                            case 3: // trade
//                                    try {
//                                                Item dns = InventoryService.gI().findItem(player.inventory.itemsBag, 674);
//                                                Item tl = InventoryService.gI().findItem(player.inventory.itemsBag, 563);
//                                                int soLuong = 0;
//                                                if (dns != null) {
//                                                    soLuong = dns.quantity;
//                                                }
//                                                for (int i = 0; i < 12; i++) {
//                                                    Item thl = InventoryService.gI().findItem(player.inventory.itemsBag, 563 + i);
//
//                                                    if (InventoryService.gI().existItemBag(player, 563 + i) && soLuong >= 30000) {
//                                                        InventoryService.gI().subQuantityItemsBag(player, dns, 30000);
//                                                        InventoryService.gI().subQuantityItemsBag(player, thl, 1);
//                                                        CombineServiceNew.gI().GetTrangBiKichHoathuydiet(player, 658 + i);
//                                                        this.npcChat(player, "Chuyển Hóa Thành Công!");
//
//                                                        break;
//                                                    } else {
//                                                        this.npcChat(player, "Yêu cầu cần Giày Thần linh trái đất + x30.000 Đá Ngũ Sắc!");
//                                                    }
//
//                                                }
//                                            } catch (Exception e) {
//
//                                            }
//                                            break;
//                                            case 4: // trade
//                                    try {
//                                                Item dns = InventoryService.gI().findItem(player.inventory.itemsBag, 674);
//                                                Item tl = InventoryService.gI().findItem(player.inventory.itemsBag, 561);
//                                                int soLuong = 0;
//                                                if (dns != null) {
//                                                    soLuong = dns.quantity;
//                                                }
//                                                for (int i = 0; i < 12; i++) {
//                                                    Item thl = InventoryService.gI().findItem(player.inventory.itemsBag, 561 + i);
//
//                                                    if (InventoryService.gI().existItemBag(player, 561 + i) && soLuong >= 30000) {
//                                                        InventoryService.gI().subQuantityItemsBag(player, dns, 30000);
//                                                        InventoryService.gI().subQuantityItemsBag(player, thl, 1);
//                                                        CombineServiceNew.gI().GetTrangBiKichHoathuydiet(player, 656 + i);
//                                                        this.npcChat(player, "Chuyển Hóa Thành Công!");
//                                                        break;
//                                                    } else {
//                                                        this.npcChat(player, "Yêu cầu cần Nhận Thần linh trái đất + x30.000 Đá Ngũ Sắc!");
//                                                    }
//                                                }
//                                            } catch (Exception e) {
//
//                                            }
//                                            break;
//                                            case 5: // canel
//                                                break;
//                                        }
//                                    } else if (player.iDMark.getIndexMenu() == 2) { // action đổi dồ húy diệt
//                                        switch (select) {
//                                            case 0: // trade
//                                try {
//                                                Item dns = InventoryService.gI().findItem(player.inventory.itemsBag, 674);
//                                                Item tl = InventoryService.gI().findItem(player.inventory.itemsBag, 557);
//                                                int soLuong = 0;
//                                                if (dns != null) {
//                                                    soLuong = dns.quantity;
//                                                }
//                                                for (int i = 0; i < 12; i++) {
//                                                    Item thl = InventoryService.gI().findItem(player.inventory.itemsBag, 557 + i);
//
//                                                    if (InventoryService.gI().existItemBag(player, 557 + i) && soLuong >= 30000) {
//                                                        InventoryService.gI().subQuantityItemsBag(player, dns, 30000);
//                                                        InventoryService.gI().subQuantityItemsBag(player, thl, 1);
//                                                        CombineServiceNew.gI().GetTrangBiKichHoathuydiet(player, 652 + i);
//                                                        this.npcChat(player, "Chuyển Hóa Thành Công!");
//
//                                                        break;
//                                                    } else {
//                                                        this.npcChat(player, "Yêu cầu cần Áo Thần linh namec + x30.000 Đá Ngũ Sắc!");
//                                                    }
//
//                                                }
//                                            } catch (Exception e) {
//
//                                            }
//                                            break;
//                                            case 1: // trade
//                                    try {
//                                                Item dns = InventoryService.gI().findItem(player.inventory.itemsBag, 674);
//                                                Item tl = InventoryService.gI().findItem(player.inventory.itemsBag, 558);
//                                                int soLuong = 0;
//                                                if (dns != null) {
//                                                    soLuong = dns.quantity;
//                                                }
//                                                for (int i = 0; i < 12; i++) {
//                                                    Item thl = InventoryService.gI().findItem(player.inventory.itemsBag, 558 + i);
//
//                                                    if (InventoryService.gI().existItemBag(player, 558 + i) && soLuong >= 30000) {
//                                                        InventoryService.gI().subQuantityItemsBag(player, dns, 30000);
//                                                        InventoryService.gI().subQuantityItemsBag(player, thl, 1);
//                                                        CombineServiceNew.gI().GetTrangBiKichHoathuydiet(player, 653 + i);
//                                                        this.npcChat(player, "Chuyển Hóa Thành Công!");
//
//                                                        break;
//                                                    } else {
//                                                        this.npcChat(player, "Yêu cầu cần Quần Thần linh namec + x30.000 Đá Ngũ Sắc!");
//                                                    }
//
//                                                }
//                                            } catch (Exception e) {
//
//                                            }
//                                            break;
//                                            case 2: // trade
//                                    try {
//                                                Item dns = InventoryService.gI().findItem(player.inventory.itemsBag, 674);
//                                                Item tl = InventoryService.gI().findItem(player.inventory.itemsBag, 564);
//                                                int soLuong = 0;
//                                                if (dns != null) {
//                                                    soLuong = dns.quantity;
//                                                }
//                                                for (int i = 0; i < 12; i++) {
//                                                    Item thl = InventoryService.gI().findItem(player.inventory.itemsBag, 564 + i);
//
//                                                    if (InventoryService.gI().existItemBag(player, 564 + i) && soLuong >= 30000) {
//                                                        InventoryService.gI().subQuantityItemsBag(player, dns, 30000);
//                                                        InventoryService.gI().subQuantityItemsBag(player, thl, 1);
//                                                        CombineServiceNew.gI().GetTrangBiKichHoathuydiet(player, 659 + i);
//                                                        this.npcChat(player, "Chuyển Hóa Thành Công!");
//
//                                                        break;
//                                                    } else {
//                                                        this.npcChat(player, "Yêu cầu cần Găng Thần linh namec + x30.000 Đá Ngũ Sắc!");
//                                                    }
//
//                                                }
//                                            } catch (Exception e) {
//
//                                            }
//                                            break;
//                                            case 3: // trade
//                                    try {
//                                                Item dns = InventoryService.gI().findItem(player.inventory.itemsBag, 674);
//                                                Item tl = InventoryService.gI().findItem(player.inventory.itemsBag, 565);
//                                                int soLuong = 0;
//                                                if (dns != null) {
//                                                    soLuong = dns.quantity;
//                                                }
//                                                for (int i = 0; i < 12; i++) {
//                                                    Item thl = InventoryService.gI().findItem(player.inventory.itemsBag, 565 + i);
//
//                                                    if (InventoryService.gI().existItemBag(player, 565 + i) && soLuong >= 30000) {
//                                                        InventoryService.gI().subQuantityItemsBag(player, dns, 30000);
//                                                        InventoryService.gI().subQuantityItemsBag(player, thl, 1);
//                                                        CombineServiceNew.gI().GetTrangBiKichHoathuydiet(player, 660 + i);
//                                                        this.npcChat(player, "Chuyển Hóa Thành Công!");
//
//                                                        break;
//                                                    } else {
//                                                        this.npcChat(player, "Yêu cầu cần Giày Thần linh namec + x30.000 Đá Ngũ Sắc!");
//                                                    }
//
//                                                }
//                                            } catch (Exception e) {
//
//                                            }
//                                            break;
//                                            case 4: // trade
//                                    try {
//                                                Item dns = InventoryService.gI().findItem(player.inventory.itemsBag, 674);
//                                                Item tl = InventoryService.gI().findItem(player.inventory.itemsBag, 561);
//                                                int soLuong = 0;
//                                                if (dns != null) {
//                                                    soLuong = dns.quantity;
//                                                }
//                                                for (int i = 0; i < 12; i++) {
//                                                    Item thl = InventoryService.gI().findItem(player.inventory.itemsBag, 561 + i);
//
//                                                    if (InventoryService.gI().existItemBag(player, 561 + i) && soLuong >= 30000) {
//                                                        InventoryService.gI().subQuantityItemsBag(player, dns, 30000);
//                                                        InventoryService.gI().subQuantityItemsBag(player, thl, 1);
//                                                        CombineServiceNew.gI().GetTrangBiKichHoathuydiet(player, 656 + i);
//                                                        this.npcChat(player, "Chuyển Hóa Thành Công!");
//                                                        break;
//                                                    } else {
//                                                        this.npcChat(player, "Yêu cầu cần Nhận Thần linh namec + x30.000 Đá Ngũ Sắc!");
//                                                    }
//                                                }
//                                            } catch (Exception e) {
//
//                                            }
//                                            break;
//                                            case 5: // canel
//                                                break;
//                                        }
//                                    } else if (player.iDMark.getIndexMenu() == 3) { // action đổi dồ húy diệt
//                                        switch (select) {
//                                            case 0: // trade
//                                try {
//                                                Item dns = InventoryService.gI().findItem(player.inventory.itemsBag, 674);
//                                                Item tl = InventoryService.gI().findItem(player.inventory.itemsBag, 559);
//                                                int soLuong = 0;
//                                                if (dns != null) {
//                                                    soLuong = dns.quantity;
//                                                }
//                                                for (int i = 0; i < 12; i++) {
//                                                    Item thl = InventoryService.gI().findItem(player.inventory.itemsBag, 559 + i);
//
//                                                    if (InventoryService.gI().existItemBag(player, 559 + i) && soLuong >= 30000) {
//                                                        InventoryService.gI().subQuantityItemsBag(player, dns, 30000);
//                                                        InventoryService.gI().subQuantityItemsBag(player, thl, 1);
//                                                        CombineServiceNew.gI().GetTrangBiKichHoathuydiet(player, 654 + i);
//                                                        this.npcChat(player, "Chuyển Hóa Thành Công!");
//
//                                                        break;
//                                                    } else {
//                                                        this.npcChat(player, "Yêu cầu cần Áo Thần linh xayda + x30.000 Đá Ngũ Sắc!");
//                                                    }
//
//                                                }
//                                            } catch (Exception e) {
//
//                                            }
//                                            break;
//                                            case 1: // trade
//                                    try {
//                                                Item dns = InventoryService.gI().findItem(player.inventory.itemsBag, 674);
//                                                Item tl = InventoryService.gI().findItem(player.inventory.itemsBag, 560);
//                                                int soLuong = 0;
//                                                if (dns != null) {
//                                                    soLuong = dns.quantity;
//                                                }
//                                                for (int i = 0; i < 12; i++) {
//                                                    Item thl = InventoryService.gI().findItem(player.inventory.itemsBag, 560 + i);
//
//                                                    if (InventoryService.gI().existItemBag(player, 560 + i) && soLuong >= 30000) {
//                                                        InventoryService.gI().subQuantityItemsBag(player, dns, 30000);
//                                                        InventoryService.gI().subQuantityItemsBag(player, thl, 1);
//                                                        CombineServiceNew.gI().GetTrangBiKichHoathuydiet(player, 655 + i);
//                                                        this.npcChat(player, "Chuyển Hóa Thành Công!");
//
//                                                        break;
//                                                    } else {
//                                                        this.npcChat(player, "Yêu cầu cần Quần Thần linh xayda + x30.000 Đá Ngũ Sắc!");
//                                                    }
//
//                                                }
//                                            } catch (Exception e) {
//
//                                            }
//                                            break;
//                                            case 2: // trade
//                                    try {
//                                                Item dns = InventoryService.gI().findItem(player.inventory.itemsBag, 674);
//                                                Item tl = InventoryService.gI().findItem(player.inventory.itemsBag, 566);
//                                                int soLuong = 0;
//                                                if (dns != null) {
//                                                    soLuong = dns.quantity;
//                                                }
//                                                for (int i = 0; i < 12; i++) {
//                                                    Item thl = InventoryService.gI().findItem(player.inventory.itemsBag, 566 + i);
//
//                                                    if (InventoryService.gI().existItemBag(player, 566 + i) && soLuong >= 30000) {
//                                                        InventoryService.gI().subQuantityItemsBag(player, dns, 30000);
//                                                        InventoryService.gI().subQuantityItemsBag(player, thl, 1);
//                                                        CombineServiceNew.gI().GetTrangBiKichHoathuydiet(player, 661 + i);
//                                                        this.npcChat(player, "Chuyển Hóa Thành Công!");
//
//                                                        break;
//                                                    } else {
//                                                        this.npcChat(player, "Yêu cầu cần Găng Thần linh xayda + x30.000 Đá Ngũ Sắc!");
//                                                    }
//
//                                                }
//                                            } catch (Exception e) {
//
//                                            }
//                                            break;
//                                            case 3: // trade
//                                    try {
//                                                Item dns = InventoryService.gI().findItem(player.inventory.itemsBag, 674);
//                                                Item tl = InventoryService.gI().findItem(player.inventory.itemsBag, 567);
//                                                int soLuong = 0;
//                                                if (dns != null) {
//                                                    soLuong = dns.quantity;
//                                                }
//                                                for (int i = 0; i < 12; i++) {
//                                                    Item thl = InventoryService.gI().findItem(player.inventory.itemsBag, 567 + i);
//
//                                                    if (InventoryService.gI().existItemBag(player, 567 + i) && soLuong >= 30000) {
//                                                        InventoryService.gI().subQuantityItemsBag(player, dns, 30000);
//                                                        InventoryService.gI().subQuantityItemsBag(player, thl, 1);
//                                                        CombineServiceNew.gI().GetTrangBiKichHoathuydiet(player, 662 + i);
//                                                        this.npcChat(player, "Chuyển Hóa Thành Công!");
//
//                                                        break;
//                                                    } else {
//                                                        this.npcChat(player, "Yêu cầu cần Giày Thần linh xayda + x30.000 Đá Ngũ Sắc!");
//                                                    }
//
//                                                }
//                                            } catch (Exception e) {
//
//                                            }
//                                            break;
//                                            case 4: // trade
//                                    try {
//                                                Item dns = InventoryService.gI().findItem(player.inventory.itemsBag, 674);
//                                                Item tl = InventoryService.gI().findItem(player.inventory.itemsBag, 561);
//                                                int soLuong = 0;
//                                                if (dns != null) {
//                                                    soLuong = dns.quantity;
//                                                }
//                                                for (int i = 0; i < 12; i++) {
//                                                    Item thl = InventoryService.gI().findItem(player.inventory.itemsBag, 561 + i);
//
//                                                    if (InventoryService.gI().existItemBag(player, 561 + i) && soLuong >= 30000) {
//                                                        InventoryService.gI().subQuantityItemsBag(player, dns, 30000);
//                                                        InventoryService.gI().subQuantityItemsBag(player, thl, 1);
//                                                        CombineServiceNew.gI().GetTrangBiKichHoathuydiet(player, 656 + i);
//                                                        this.npcChat(player, "Chuyển Hóa Thành Công!");
//                                                        break;
//                                                    } else {
//                                                        this.npcChat(player, "Yêu cầu cần Nhận Thần linh xayde + x30.000 Đá Ngũ Sắc!");
//                                                    }
//                                                }
//                                            } catch (Exception e) {
//
//                                            }
//                                            break;
//                                            case 5: // canel
//                                                break;
//                                        }
//                                    } else if (player.iDMark.getIndexMenu() == 4) { // action đổi dồ thiên sứ
//                                        switch (select) {
//                                            case 0: // trade
//                                try {
//                                                Item dns = InventoryService.gI().findItem(player.inventory.itemsBag, 674);
//                                                Item tl = InventoryService.gI().findItem(player.inventory.itemsBag, 650);
//                                                int soLuong = 0;
//                                                if (dns != null) {
//                                                    soLuong = dns.quantity;
//                                                }
//                                                for (int i = 0; i < 12; i++) {
//                                                    Item thl = InventoryService.gI().findItem(player.inventory.itemsBag, 650 + i);
//
//                                                    if (InventoryService.gI().existItemBag(player, 650 + i) && soLuong >= 99999) {
//                                                        InventoryService.gI().subQuantityItemsBag(player, dns, 99999);
//                                                        InventoryService.gI().subQuantityItemsBag(player, thl, 1);
//                                                        CombineServiceNew.gI().GetTrangBiKichHoatthiensu(player, 1048 + i);
//                                                        this.npcChat(player, "Chuyển Hóa Thành Công!");
//
//                                                        break;
//                                                    } else {
//                                                        this.npcChat(player, "Yêu cầu cần Áo húy diệt trái đất + x99.999 Đá Ngũ Sắc!");
//                                                    }
//
//                                                }
//                                            } catch (Exception e) {
//
//                                            }
//                                            break;
//                                            case 1: // trade
//                                    try {
//                                                Item dns = InventoryService.gI().findItem(player.inventory.itemsBag, 674);
//                                                Item tl = InventoryService.gI().findItem(player.inventory.itemsBag, 651);
//                                                int soLuong = 0;
//                                                if (dns != null) {
//                                                    soLuong = dns.quantity;
//                                                }
//                                                for (int i = 0; i < 12; i++) {
//                                                    Item thl = InventoryService.gI().findItem(player.inventory.itemsBag, 651 + i);
//
//                                                    if (InventoryService.gI().existItemBag(player, 651 + i) && soLuong >= 99999) {
//                                                        InventoryService.gI().subQuantityItemsBag(player, dns, 99999);
//                                                        InventoryService.gI().subQuantityItemsBag(player, thl, 1);
//                                                        CombineServiceNew.gI().GetTrangBiKichHoatthiensu(player, 1051 + i);
//                                                        this.npcChat(player, "Chuyển Hóa Thành Công!");
//
//                                                        break;
//                                                    } else {
//                                                        this.npcChat(player, "Yêu cầu cần Quần húy diệt trái đất + x99.999 Đá Ngũ Sắc!");
//                                                    }
//
//                                                }
//                                            } catch (Exception e) {
//
//                                            }
//                                            break;
//                                            case 2: // trade
//                                    try {
//                                                Item dns = InventoryService.gI().findItem(player.inventory.itemsBag, 674);
//                                                Item tl = InventoryService.gI().findItem(player.inventory.itemsBag, 657);
//                                                int soLuong = 0;
//                                                if (dns != null) {
//                                                    soLuong = dns.quantity;
//                                                }
//                                                for (int i = 0; i < 12; i++) {
//                                                    Item thl = InventoryService.gI().findItem(player.inventory.itemsBag, 657 + i);
//
//                                                    if (InventoryService.gI().existItemBag(player, 657 + i) && soLuong >= 99999) {
//                                                        InventoryService.gI().subQuantityItemsBag(player, dns, 99999);
//                                                        InventoryService.gI().subQuantityItemsBag(player, thl, 1);
//                                                        CombineServiceNew.gI().GetTrangBiKichHoatthiensu(player, 1054 + i);
//                                                        this.npcChat(player, "Chuyển Hóa Thành Công!");
//
//                                                        break;
//                                                    } else {
//                                                        this.npcChat(player, "Yêu cầu cần Găng húy diệt trái đất + x99.999 Đá Ngũ Sắc!");
//                                                    }
//
//                                                }
//                                            } catch (Exception e) {
//
//                                            }
//                                            break;
//                                            case 3: // trade
//                                    try {
//                                                Item dns = InventoryService.gI().findItem(player.inventory.itemsBag, 674);
//                                                Item tl = InventoryService.gI().findItem(player.inventory.itemsBag, 658);
//                                                int soLuong = 0;
//                                                if (dns != null) {
//                                                    soLuong = dns.quantity;
//                                                }
//                                                for (int i = 0; i < 12; i++) {
//                                                    Item thl = InventoryService.gI().findItem(player.inventory.itemsBag, 658 + i);
//
//                                                    if (InventoryService.gI().existItemBag(player, 658 + i) && soLuong >= 99999) {
//                                                        InventoryService.gI().subQuantityItemsBag(player, dns, 99999);
//                                                        InventoryService.gI().subQuantityItemsBag(player, thl, 1);
//                                                        CombineServiceNew.gI().GetTrangBiKichHoatthiensu(player, 1057 + i);
//                                                        this.npcChat(player, "Chuyển Hóa Thành Công!");
//
//                                                        break;
//                                                    } else {
//                                                        this.npcChat(player, "Yêu cầu cần Giày húy diệt trái đất + x99.999 Đá Ngũ Sắc!");
//                                                    }
//
//                                                }
//                                            } catch (Exception e) {
//
//                                            }
//                                            break;
//                                            case 4: // trade
//                                    try {
//                                                Item dns = InventoryService.gI().findItem(player.inventory.itemsBag, 674);
//                                                Item tl = InventoryService.gI().findItem(player.inventory.itemsBag, 656);
//                                                int soLuong = 0;
//                                                if (dns != null) {
//                                                    soLuong = dns.quantity;
//                                                }
//                                                for (int i = 0; i < 12; i++) {
//                                                    Item thl = InventoryService.gI().findItem(player.inventory.itemsBag, 656 + i);
//
//                                                    if (InventoryService.gI().existItemBag(player, 656 + i) && soLuong >= 99999) {
//                                                        InventoryService.gI().subQuantityItemsBag(player, dns, 99999);
//                                                        InventoryService.gI().subQuantityItemsBag(player, thl, 1);
//                                                        CombineServiceNew.gI().GetTrangBiKichHoatthiensu(player, 1060 + i);
//                                                        this.npcChat(player, "Chuyển Hóa Thành Công!");
//
//                                                        break;
//                                                    } else {
//                                                        this.npcChat(player, "Yêu cầu cần nhận húy diệt trái đất + x99.999 Đá Ngũ Sắc!");
//                                                    }
//
//                                                }
//                                            } catch (Exception e) {
//
//                                            }
//                                            break;
//                                            case 5: // canel
//                                                break;
//                                        }
//                                    } else if (player.iDMark.getIndexMenu() == 5) { // action đổi dồ thiên sứ
//                                        switch (select) {
//                                            case 0: // trade
//                                try {
//                                                Item dns = InventoryService.gI().findItem(player.inventory.itemsBag, 674);
//                                                Item tl = InventoryService.gI().findItem(player.inventory.itemsBag, 652);
//                                                int soLuong = 0;
//                                                if (dns != null) {
//                                                    soLuong = dns.quantity;
//                                                }
//                                                for (int i = 0; i < 12; i++) {
//                                                    Item thl = InventoryService.gI().findItem(player.inventory.itemsBag, 652 + i);
//
//                                                    if (InventoryService.gI().existItemBag(player, 652 + i) && soLuong >= 99999) {
//                                                        InventoryService.gI().subQuantityItemsBag(player, dns, 99999);
//                                                        InventoryService.gI().subQuantityItemsBag(player, thl, 1);
//                                                        CombineServiceNew.gI().GetTrangBiKichHoatthiensu(player, 1049 + i);
//                                                        this.npcChat(player, "Chuyển Hóa Thành Công!");
//
//                                                        break;
//                                                    } else {
//                                                        this.npcChat(player, "Yêu cầu cần Áo húy diệt namec + x99.999 Đá Ngũ Sắc!");
//                                                    }
//
//                                                }
//                                            } catch (Exception e) {
//
//                                            }
//                                            break;
//                                            case 1: // trade
//                                    try {
//                                                Item dns = InventoryService.gI().findItem(player.inventory.itemsBag, 674);
//                                                Item tl = InventoryService.gI().findItem(player.inventory.itemsBag, 653);
//                                                int soLuong = 0;
//                                                if (dns != null) {
//                                                    soLuong = dns.quantity;
//                                                }
//                                                for (int i = 0; i < 12; i++) {
//                                                    Item thl = InventoryService.gI().findItem(player.inventory.itemsBag, 653 + i);
//
//                                                    if (InventoryService.gI().existItemBag(player, 653 + i) && soLuong >= 99999) {
//                                                        InventoryService.gI().subQuantityItemsBag(player, dns, 99999);
//                                                        InventoryService.gI().subQuantityItemsBag(player, thl, 1);
//                                                        CombineServiceNew.gI().GetTrangBiKichHoatthiensu(player, 1052 + i);
//                                                        this.npcChat(player, "Chuyển Hóa Thành Công!");
//
//                                                        break;
//                                                    } else {
//                                                        this.npcChat(player, "Yêu cầu cần Quần húy diệt namec + x99.999 Đá Ngũ Sắc!");
//                                                    }
//
//                                                }
//                                            } catch (Exception e) {
//
//                                            }
//                                            break;
//                                            case 2: // trade
//                                    try {
//                                                Item dns = InventoryService.gI().findItem(player.inventory.itemsBag, 674);
//                                                Item tl = InventoryService.gI().findItem(player.inventory.itemsBag, 659);
//                                                int soLuong = 0;
//                                                if (dns != null) {
//                                                    soLuong = dns.quantity;
//                                                }
//                                                for (int i = 0; i < 12; i++) {
//                                                    Item thl = InventoryService.gI().findItem(player.inventory.itemsBag, 659 + i);
//
//                                                    if (InventoryService.gI().existItemBag(player, 659 + i) && soLuong >= 99999) {
//                                                        InventoryService.gI().subQuantityItemsBag(player, dns, 99999);
//                                                        InventoryService.gI().subQuantityItemsBag(player, thl, 1);
//                                                        CombineServiceNew.gI().GetTrangBiKichHoatthiensu(player, 1055 + i);
//                                                        this.npcChat(player, "Chuyển Hóa Thành Công!");
//
//                                                        break;
//                                                    } else {
//                                                        this.npcChat(player, "Yêu cầu cần Găng húy diệt namec + x99.999 Đá Ngũ Sắc!");
//                                                    }
//
//                                                }
//                                            } catch (Exception e) {
//
//                                            }
//                                            break;
//                                            case 3: // trade
//                                    try {
//                                                Item dns = InventoryService.gI().findItem(player.inventory.itemsBag, 674);
//                                                Item tl = InventoryService.gI().findItem(player.inventory.itemsBag, 660);
//                                                int soLuong = 0;
//                                                if (dns != null) {
//                                                    soLuong = dns.quantity;
//                                                }
//                                                for (int i = 0; i < 12; i++) {
//                                                    Item thl = InventoryService.gI().findItem(player.inventory.itemsBag, 660 + i);
//
//                                                    if (InventoryService.gI().existItemBag(player, 660 + i) && soLuong >= 99999) {
//                                                        InventoryService.gI().subQuantityItemsBag(player, dns, 99999);
//                                                        InventoryService.gI().subQuantityItemsBag(player, thl, 1);
//                                                        CombineServiceNew.gI().GetTrangBiKichHoatthiensu(player, 1058 + i);
//                                                        this.npcChat(player, "Chuyển Hóa Thành Công!");
//
//                                                        break;
//                                                    } else {
//                                                        this.npcChat(player, "Yêu cầu cần Giày húy diệt namec + x99.999 Đá Ngũ Sắc!");
//                                                    }
//
//                                                }
//                                            } catch (Exception e) {
//
//                                            }
//                                            break;
//                                            case 4: // trade
//                                    try {
//                                                Item dns = InventoryService.gI().findItem(player.inventory.itemsBag, 674);
//                                                Item tl = InventoryService.gI().findItem(player.inventory.itemsBag, 656);
//                                                int soLuong = 0;
//                                                if (dns != null) {
//                                                    soLuong = dns.quantity;
//                                                }
//                                                for (int i = 0; i < 12; i++) {
//                                                    Item thl = InventoryService.gI().findItem(player.inventory.itemsBag, 656 + i);
//
//                                                    if (InventoryService.gI().existItemBag(player, 656 + i) && soLuong >= 99999) {
//                                                        InventoryService.gI().subQuantityItemsBag(player, dns, 99999);
//                                                        InventoryService.gI().subQuantityItemsBag(player, thl, 1);
//                                                        CombineServiceNew.gI().GetTrangBiKichHoatthiensu(player, 1061 + i);
//                                                        this.npcChat(player, "Chuyển Hóa Thành Công!");
//
//                                                        break;
//                                                    } else {
//                                                        this.npcChat(player, "Yêu cầu cần nhận húy diệt namec + x99.999 Đá Ngũ Sắc!");
//                                                    }
//
//                                                }
//                                            } catch (Exception e) {
//
//                                            }
//                                            break;
//                                            case 5: // canel
//                                                break;
//                                        }
//                                    } else if (player.iDMark.getIndexMenu() == 6) { // action đổi dồ thiên sứ
//                                        switch (select) {
//                                            case 0: // trade
//                                try {
//                                                Item dns = InventoryService.gI().findItem(player.inventory.itemsBag, 674);
//                                                Item tl = InventoryService.gI().findItem(player.inventory.itemsBag, 654);
//                                                int soLuong = 0;
//                                                if (dns != null) {
//                                                    soLuong = dns.quantity;
//                                                }
//                                                for (int i = 0; i < 12; i++) {
//                                                    Item thl = InventoryService.gI().findItem(player.inventory.itemsBag, 654 + i);
//
//                                                    if (InventoryService.gI().existItemBag(player, 654 + i) && soLuong >= 99999) {
//                                                        InventoryService.gI().subQuantityItemsBag(player, dns, 99999);
//                                                        InventoryService.gI().subQuantityItemsBag(player, thl, 1);
//                                                        CombineServiceNew.gI().GetTrangBiKichHoatthiensu(player, 1050 + i);
//                                                        this.npcChat(player, "Chuyển Hóa Thành Công!");
//
//                                                        break;
//                                                    } else {
//                                                        this.npcChat(player, "Yêu cầu cần Áo húy diệt xayda + x99.999 Đá Ngũ Sắc!");
//                                                    }
//
//                                                }
//                                            } catch (Exception e) {
//
//                                            }
//                                            break;
//                                            case 1: // trade
//                                    try {
//                                                Item dns = InventoryService.gI().findItem(player.inventory.itemsBag, 674);
//                                                Item tl = InventoryService.gI().findItem(player.inventory.itemsBag, 655);
//                                                int soLuong = 0;
//                                                if (dns != null) {
//                                                    soLuong = dns.quantity;
//                                                }
//                                                for (int i = 0; i < 12; i++) {
//                                                    Item thl = InventoryService.gI().findItem(player.inventory.itemsBag, 655 + i);
//
//                                                    if (InventoryService.gI().existItemBag(player, 655 + i) && soLuong >= 99999) {
//                                                        InventoryService.gI().subQuantityItemsBag(player, dns, 99999);
//                                                        InventoryService.gI().subQuantityItemsBag(player, thl, 1);
//                                                        CombineServiceNew.gI().GetTrangBiKichHoatthiensu(player, 1053 + i);
//                                                        this.npcChat(player, "Chuyển Hóa Thành Công!");
//
//                                                        break;
//                                                    } else {
//                                                        this.npcChat(player, "Yêu cầu cần Quần húy diệt xayda + x99.999 Đá Ngũ Sắc!");
//                                                    }
//
//                                                }
//                                            } catch (Exception e) {
//
//                                            }
//                                            break;
//                                            case 2: // trade
//                                    try {
//                                                Item dns = InventoryService.gI().findItem(player.inventory.itemsBag, 674);
//                                                Item tl = InventoryService.gI().findItem(player.inventory.itemsBag, 661);
//                                                int soLuong = 0;
//                                                if (dns != null) {
//                                                    soLuong = dns.quantity;
//                                                }
//                                                for (int i = 0; i < 12; i++) {
//                                                    Item thl = InventoryService.gI().findItem(player.inventory.itemsBag, 661 + i);
//
//                                                    if (InventoryService.gI().existItemBag(player, 661 + i) && soLuong >= 99999) {
//                                                        InventoryService.gI().subQuantityItemsBag(player, dns, 99999);
//                                                        InventoryService.gI().subQuantityItemsBag(player, thl, 1);
//                                                        CombineServiceNew.gI().GetTrangBiKichHoatthiensu(player, 1056 + i);
//                                                        this.npcChat(player, "Chuyển Hóa Thành Công!");
//
//                                                        break;
//                                                    } else {
//                                                        this.npcChat(player, "Yêu cầu cần Găng húy diệt xayda + x99.999 Đá Ngũ Sắc!");
//                                                    }
//
//                                                }
//                                            } catch (Exception e) {
//
//                                            }
//                                            break;
//                                            case 3: // trade
//                                    try {
//                                                Item dns = InventoryService.gI().findItem(player.inventory.itemsBag, 674);
//                                                Item tl = InventoryService.gI().findItem(player.inventory.itemsBag, 662);
//                                                int soLuong = 0;
//                                                if (dns != null) {
//                                                    soLuong = dns.quantity;
//                                                }
//                                                for (int i = 0; i < 12; i++) {
//                                                    Item thl = InventoryService.gI().findItem(player.inventory.itemsBag, 662 + i);
//
//                                                    if (InventoryService.gI().existItemBag(player, 662 + i) && soLuong >= 99999) {
//                                                        InventoryService.gI().subQuantityItemsBag(player, dns, 99999);
//                                                        InventoryService.gI().subQuantityItemsBag(player, thl, 1);
//                                                        CombineServiceNew.gI().GetTrangBiKichHoatthiensu(player, 1059 + i);
//                                                        this.npcChat(player, "Chuyển Hóa Thành Công!");
//
//                                                        break;
//                                                    } else {
//                                                        this.npcChat(player, "Yêu cầu cần Giày húy diệt xayda + x99.999 Đá Ngũ Sắc!");
//                                                    }
//
//                                                }
//                                            } catch (Exception e) {
//
//                                            }
//                                            break;
//                                            case 4: // trade
//                                    try {
//                                                Item dns = InventoryService.gI().findItem(player.inventory.itemsBag, 674);
//                                                Item tl = InventoryService.gI().findItem(player.inventory.itemsBag, 656);
//                                                int soLuong = 0;
//                                                if (dns != null) {
//                                                    soLuong = dns.quantity;
//                                                }
//                                                for (int i = 0; i < 12; i++) {
//                                                    Item thl = InventoryService.gI().findItem(player.inventory.itemsBag, 656 + i);
//
//                                                    if (InventoryService.gI().existItemBag(player, 656 + i) && soLuong >= 99999) {
//                                                        InventoryService.gI().subQuantityItemsBag(player, dns, 99999);
//                                                        InventoryService.gI().subQuantityItemsBag(player, thl, 1);
//                                                        CombineServiceNew.gI().GetTrangBiKichHoatthiensu(player, 1062 + i);
//                                                        this.npcChat(player, "Chuyển Hóa Thành Công!");
//
//                                                        break;
//                                                    } else {
//                                                        this.npcChat(player, "Yêu cầu cần nhận húy diệt xayda + x99.999 Đá Ngũ Sắc!");
//                                                    }
//
//                                                }
//                                            } catch (Exception e) {
//
//                                            }
//                                            break;
//                                        }
//
//                                    }
//                                }
//
//                            }
//                        }
//                    };
//                    break;
                case ConstNpc.TRUONG_LAO_GURU:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            Item mcl = InventoryService.gI().findItemBagByTemp(player, 2000);
                            int slMCL = (mcl == null) ? 0 : mcl.quantity;
                            if (canOpenNpc(player)) {
                                if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                            "Ngọc rồng Namếc đang bị 2 thế lực tranh giành\nHãy chọn cấp độ tham gia tùy theo sức mạnh bản thân",
                                            "Tham gia", "Đổi điểm\nThưởng\n[" + slMCL + "]", "Bảng\nxếp hạng", "Từ chối");
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                switch (player.iDMark.getIndexMenu()) {
                                    case ConstNpc.BASE_MENU:
                                        switch (select) {
                                            case 0:
                                                if (TranhNgoc.gI().isTimeRegisterWar()) {
                                                    if (player.iDMark.getTranhNgoc() == -1) {
                                                        this.createOtherMenu(player, ConstNpc.REGISTER_TRANH_NGOC,
                                                                "Ngọc rồng Namếc đang bị 2 thế lực tranh giành\nHãy chọn cấp độ tham gia tùy theo sức mạnh bản thân\nPhe Cadic: " + TranhNgoc.gI().getPlayersCadic().size() + "\nPhe Fide: " + TranhNgoc.gI().getPlayersFide().size(),
                                                                "Tham gia phe Cadic", "Tham gia phe Fide", "Đóng");
                                                    } else {
                                                        this.createOtherMenu(player, ConstNpc.LOG_OUT_TRANH_NGOC,
                                                                "Ngọc rồng Namếc đang bị 2 thế lực tranh giành\nHãy chọn cấp độ tham gia tùy theo sức mạnh bản thân\nPhe Cadic: " + TranhNgoc.gI().getPlayersCadic().size() + "\nPhe Fide: " + TranhNgoc.gI().getPlayersFide().size(),
                                                                "Hủy\nĐăng Ký", "Đóng");
                                                    }
                                                    return;
                                                }
                                                Service.getInstance().sendPopUpMultiLine(player, 0, 7184, "Sự kiện sẽ mở đăng ký vào lúc " + TranhNgoc.HOUR_REGISTER + ":" + TranhNgoc.MIN_REGISTER + "\nSự kiện sẽ bắt đầu vào " + TranhNgoc.HOUR_OPEN + ":" + TranhNgoc.MIN_OPEN + " và kết thúc vào " + TranhNgoc.HOUR_CLOSE + ":" + TranhNgoc.HOUR_CLOSE);
                                                break;
                                            case 1:// Shop
                                                ShopService.gI().openShopSpecial(player, this,
                                                        ConstNpc.TRUONG_LAO_GURU, 0, -1);
                                                break;
                                            case 2:
                                                Service.getInstance().sendThongBao(player, "Update coming soon");
                                                break;
                                        }
                                        break;
                                    case ConstNpc.REGISTER_TRANH_NGOC:
                                        switch (select) {
                                            case 0:
                                                if (!player.getSession().actived) {
                                                    Service.getInstance().sendThongBao(player, "Vui lòng kích hoạt tài khoản để sửa dụng chức năng này!");
                                                    return;
                                                }
                                                player.iDMark.setTranhNgoc((byte) 1);
                                                TranhNgoc.gI().addPlayersCadic(player);
                                                Service.getInstance().sendThongBao(player, "Đăng ký vào phe Cadic thành công");
                                                break;
                                            case 1:
                                                if (!player.getSession().actived) {
                                                    Service.getInstance().sendThongBao(player, "Vui lòng kích hoạt tài khoản để sửa dụng chức năng này!");
                                                    return;
                                                }
                                                player.iDMark.setTranhNgoc((byte) 2);
                                                TranhNgoc.gI().addPlayersFide(player);
                                                Service.getInstance().sendThongBao(player, "Đăng ký vào phe Fide thành công");
                                                break;
                                        }
                                        break;
                                    case ConstNpc.LOG_OUT_TRANH_NGOC:
                                        switch (select) {
                                            case 0:
                                                player.iDMark.setTranhNgoc((byte) -1);
                                                TranhNgoc.gI().removePlayersCadic(player);
                                                TranhNgoc.gI().removePlayersFide(player);
                                                Service.getInstance().sendThongBao(player, "Hủy đăng ký thành công");
                                                break;
                                        }
                                        break;
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.VUA_VEGETA:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                            "Hôm nay tôi buồn 1 mình trong phố đông",
                                            "Từ chối");
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                            }
                        }
                    };
                    break;
                case ConstNpc.EVENT:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                this.createOtherMenu(player, ConstNpc.BASE_MENU, "Xin chào, ta có một số vật phẩm đặt biệt cậu có muốn xem không?",
                                        "Shop\nSự Kiện", "Từ chối");

                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                switch (select) {
                                    case 0:
                                        ShopService.gI().openShopSpecial(player, this, ConstNpc.SHOPEVENT, 0, -1);

                                        break;
                                }
                            }
                        }

                    };
                    break;
                case ConstNpc.GIUMA_DAU_BO:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                this.createOtherMenu(player, ConstNpc.BASE_MENU, "Úp mảnh vỡ bông tai? tỉ lệ 1/100 đó quái bên dưới nhé!!",
                                        "Về đảo\nKame", "Từ chối");

                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                switch (select) {
                                    case 0:

                                        break;
                                }
                            }
                        }

                    };
                    break;
                case ConstNpc.LEO_THAP:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                int pointPerr = player.tangThap * 30;
                                int pointHP = player.tangThap * 88;
                                int pointKI = player.tangThap * 99;
                                int pointPerrR = player.tangThap + 1;
                                int pointPerr1 = player.tangThap * 77;
                                int pointAN = player.tangThap * 31;
                                if (player.zone.map.mapId == 21 + player.gender) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "|7|Bí Cảnh Tháp\n"
                                            + "|4|Điểm Hạ Boss LV: " + player.pointThap + " Điểm\n"
                                            + "Level Boss: " + player.levelThap + " Đang Thách Đấu\n"
                                            + "Đã Leo Được: " + player.tangThap + " Tầng"
                                            + "\nChỉ số cộng thêm Khi Lên Tầng"
                                            + "\nHP: " + pointHP + "%"
                                            + "\nKI: " + pointKI + "%"
                                            + "\nSD: " + pointPerr + "%"
                                            + "\nDEF: " + pointPerr + "%",
                                            "Tham Gia", "Đóng");
                                } else if (player.zone.map.mapId == 211 && player.tangThap == 0) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "|7|Bí Cảnh Tháp\n"
                                            + "|4|Điểm Hạ Boss LV: " + player.pointThap + " Điểm\n"
                                            + "Level Boss: " + player.levelThap + " Đang Thách Đấu\n"
                                            + "Đã Leo Được: " + player.tangThap + " Tầng"
                                            + "\nChỉ số cộng thêm Khi Lên Tầng"
                                            + "\nHP: " + pointHP + "%"
                                            + "\nKI: " + pointKI + "%"
                                            + "\nSD: " + pointPerr + "%"
                                            + "\nDEF: " + pointPerr + "%",
                                            "Hướng Dẫn", "Xem Top", "Đóng");

                                } else if (player.zone.map.mapId == 211 && player.tangThap > 0) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "|7|Bí Cảnh Tháp\n"
                                            + "|4|Điểm Hạ Boss LV: " + player.pointThap + " Điểm\n"
                                            + " Level Boss: " + player.levelThap + " Đang Thách Đấu\n"
                                            + " Đã Leo Được: " + player.tangThap + " Tầng"
                                            + "\nChỉ số cộng thêm Khi Lên Tầng"
                                            + "\nHP: " + pointHP + "%"
                                            + "\nKI: " + pointKI + "%"
                                            + "\nSD: " + pointPerr + "%"
                                            + "\nDEF: " + pointPerr + "%",
                                            "Đổi Điểm\nBí Cảnh", "SHOP\nBí Cảnh\nTầng 7", "SHOP\nBí Cảnh\nTầng 15", "SHOP\nBí Cảnh\nTầng 30", "SHOP\nBí Cảnh\nTầng 50", "SHOP\nBí Cảnh\nTầng 150", "SHOP\nBí Cảnh\nTầng 300", "SHOP\nBí Cảnh\nTầng 500", "Xem\nBảng Top", "Đóng");

                                } else {
                                    super.openBaseMenu(player);
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                switch (player.iDMark.getIndexMenu()) {
                                    case ConstNpc.BASE_MENU:
                                        if (player.zone.map.mapId == 21 || player.zone.map.mapId == 22 || player.zone.map.mapId == 23) {
                                            if (select == 0) {
                                                if (player.vip >= 6) {
                                                    LeoThap.joinMapLeoThap(player);
                                                } else {
                                                    this.npcChat(player, "Vip 6 trở lên mới có thể tham gia");
                                                }
                                            }
                                        } else if (player.zone.map.mapId == 211 && player.tangThap == 0) {
                                            switch (select) {
                                                case 0:// cửa hàng
                                                    NpcService.gI().createMenuConMeo(player, ConstNpc.huongdan, -1,
                                                            "|7|Chúc Bạn Một Ngày Vui Vẻ\n"
                                                            + "|5|Đại Chiến 300 Hiệp : Tiêu diệt All Boss Bí Cảnh\n"
                                                            + "Tiêu Diệt 99 Con Boss LV1-LV99 Hoàn Thành 1 Tầng\n"
                                                            + "Tầng Bí Cảnh Càng Cao Nhận Càng Nhiều HP KI SD GIÁP CAO\n"
                                                            + "YÊU CẦU THỰC LỰC TỪ VIP 6 INGAME TRỞ LÊN\n"
                                                            + "ĐIỂM TIÊU DIỆT DÙNG MUA TRỰC TIẾP ĐỒ TỪ TẦNG THÁP 1 SẼ XUẤT HIỆN\n"
                                                            + "\nSỐ LƯỢNG TẦNG VÔ HẠN -CỐ LÊN CỐ LÊN\n",
                                                            "Đóng");

                                                    break;

                                                case 1:// xem top
                                                    this.createOtherMenu(player, ConstNpc.MENU_LEO_THAP, "|7|Bí Cảnh Thần Thú",
                                                            "Top\nTiêu Diệt", "Đóng");
                                                    break;
                                            }
                                        } else if (player.zone.map.mapId == 211 && player.tangThap > 0) {
                                            switch (select) {

                                                case 0:

                                                    if (player.pointThap < 10) {
                                                        Service.getInstance().sendThongBao(player, "Bạn cần ít nhất 10 Điểm Bí Cảnh. Để Đổi Vật Phẩm");
                                                        return;
                                                    }
                                                    Item manhnro = null;
                                                    try {
                                                        manhnro = InventoryService.gI().findItemBagByTemp(player, 457);
                                                    } catch (Exception e) {
                                                    }
                                                    if (manhnro == null) {
                                                        this.npcChat(player, "Bạn Không Có Vật Phẩm Nào");
                                                    } else if (InventoryService.gI().getCountEmptyBag(player) == 0) {
                                                        this.npcChat(player, "Hành Trang Của Bạn Không Đủ Chỗ Trống");

                                                    } else {
                                                        InventoryService.gI().subQuantityItemsBag(player, manhnro, 1);
                                                        Item nrosieucap = ItemService.gI().createNewItem((short) 2047, 10);
                                                        nrosieucap.itemOptions.add(new ItemOption(30, 2024));
                                                        InventoryService.gI().addItemBag(player, nrosieucap, 9999999);
                                                        InventoryService.gI().sendItemBags(player);
                                                        Service.getInstance().sendThongBao(player, "Bạn nhận được " + nrosieucap.template.name);
                                                        player.pointThap -= 10;
                                                        Service.getInstance().sendThongBao(player, "Bạn Bị Trừ 10 Điểm Bí Cảnh");
                                                    }
                                                    break;
                                                case 1:
                                                    if (player.tangThap < 7) {
                                                        Service.getInstance().sendThongBaoFromAdmin(player, "Cần 7 Tầng Tháp Để Mở");
                                                        return;
                                                    }
                                                    ShopService.gI().openShopSpecial(player, this, ConstNpc.SHOPLEOTHAP, 0, 3);
                                                    break;
                                                case 2:
                                                    if (player.tangThap < 15) {
                                                        Service.getInstance().sendThongBaoFromAdmin(player, "Cần 15 Tầng Tháp Để Mở");
                                                        return;
                                                    }
                                                    ShopService.gI().openShopSpecial(player, this, ConstNpc.SHOPLEOTHAP, 0, 3);
                                                    break;
                                                case 3:
                                                    if (player.tangThap < 30) {
                                                        Service.getInstance().sendThongBaoFromAdmin(player, "Cần 30 Tầng Tháp Để Mở");
                                                        return;
                                                    }
                                                    ShopService.gI().openShopSpecial(player, this, ConstNpc.SHOPLEOTHAP, 0, 3);
                                                    break;
                                                case 4:
                                                    if (player.tangThap < 50) {
                                                        Service.getInstance().sendThongBaoFromAdmin(player, "Cần 50 Tầng Tháp Để Mở");
                                                        return;
                                                    }
                                                    ShopService.gI().openShopSpecial(player, this, ConstNpc.SHOPLEOTHAP, 0, 3);
                                                    break;
                                                case 5:
                                                    if (player.tangThap < 150) {
                                                        Service.getInstance().sendThongBaoFromAdmin(player, "Cần 150 Tầng Tháp Để Mở");
                                                        return;
                                                    }
                                                    ShopService.gI().openShopSpecial(player, this, ConstNpc.SHOPLEOTHAP, 0, 3);
                                                    break;
                                                case 6:
                                                    if (player.tangThap < 300) {
                                                        Service.getInstance().sendThongBaoFromAdmin(player, "Cần 300 Tầng Tháp Để Mở");
                                                        return;
                                                    }
                                                    ShopService.gI().openShopSpecial(player, this, ConstNpc.SHOPLEOTHAP, 0, 3);
                                                    break;
                                                case 7:
                                                    if (player.tangThap < 500) {
                                                        Service.getInstance().sendThongBaoFromAdmin(player, "Cần 500 Tầng Tháp Để Mở");
                                                        return;
                                                    }
                                                    ShopService.gI().openShopSpecial(player, this, ConstNpc.SHOPLEOTHAP, 0, 3);
                                                    break;

                                                case 8:// xem top
                                                    this.createOtherMenu(player, ConstNpc.MENU_LEO_THAP, "|7|\n" + "Bí Cảnh Thần Thú",
                                                            "Top\nTiêu Diệt", "Đóng");
                                                    break;

                                            }
                                        }
                                        break;
                                    case ConstNpc.MENU_LEO_THAP:
                                        switch (select) {
                                            case 0: // top ngày
                                                Service.getInstance().showListTop(player, Manager.topLeoThap);
                                                break;

                                        }
                                        break;
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.ONG_GOHAN:
                case ConstNpc.ONG_MOORI:
                case ConstNpc.ONG_PARAGUS:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                            "|7|Xin Chào Cả Nhà...\n"
                                            + "|6|Code: VIP666 đến VIP888 FREE\n"
                                            + "FREE VIP1 - Điểm Danh hàng Ngày\n"
                                            + "|4|Free All Hãy Cày Cuốc, Lười Thì Niệm\n"
                                                    .replaceAll("%1", player.gender == ConstPlayer.TRAI_DAT ? "Quy lão Kamê"
                                                            : player.gender == ConstPlayer.NAMEC ? "Trưởng lão Guru" : "Vua Vegeta"),
                                            "Đổi mật khẩu", "Nhập code", "Kích Hoạt\nVip", "Nạp\nLần Đầu", "Nhận\nĐệ Tử", "Hướng Dẫn");
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (player.iDMark.isBaseMenu()) {

                                    switch (select) {
                                        case 0:
                                            Input.gI().createFormChangePassword(player);
                                            break;
                                        case 1:
                                            Input.gI().createFormGiftCode(player);
                                            break;

                                        case 2:
                                            this.createOtherMenu(player, 0, "|7|KÍCH HOẠT VIP THÁNG\n"
                                                    + "|2|Số tiền hiện tại: " + Util.format(player.getSession().vnd) + " VNĐ"
                                                    + "\n|5|Trạng Thái MTV: " + (player.getSession().actived == false ? "Chưa kích hoạt" : "Đã kích hoạt")
                                                    + "\n|7|TRẠNG THÁI: VIP " + player.vip
                                                    + (player.timevip > 0 ? "\n|5|Hạn còn: " + "Vĩnh Viễn" : ""), "Kích Hoạt\nTài Khoản", "Kích Hoạt\nVIP", "Nhận\nquà Vip", "Đóng");

                                            break;

                                        case 3:
                                            NpcService.gI().createMenuConMeo(player, ConstNpc.huongdan, 31808,
                                                    "|7|Chúc Bạn Một Ngày Vui Vẻ\n"
                                                    + "|5|" + "Bạn Muốn Nạp Lần Đầu\n"
                                                    + "Với giá 200k Tặng Bạn VIP4 + 100k VNĐ\n"
                                                    + "Mở Khóa Dịch Chuyển Boss + Chức Năng Phụ\n"
                                                    + "Múc Ngay Nhỉ? LH ADMIN \n",
                                                    "LH\nADMIN", "Khi khác"
                                            );

                                            break;

                                        case 4:
                                         if (player.pet == null) {
                                                PetService.gI().createNormalPet(player);
                                                Service.getInstance().sendThongBao(player, "Bạn vừa nhận được đệ tử");
                                            } else {
                                                this.npcChat(player, "Bạn đã có rồi");
                                            }
                                            break;

                                        case 5:
                                            NpcService.gI().createMenuConMeo(player, ConstNpc.huongdan, 31808,
                                                    "|7|Chúc Bạn Một Ngày Vui Vẻ\n"
                                                    + "|5|" + "Đọc kĩ Từng Chức Năng\n"
                                                    + "Game free all Up điểm Farm từ all quái all map\n"
                                                    + "Và đổi tại NPC EVENT CÀY CHAY !\n"
                                                    + "Ngu Không Xem Đừng Hỏi\n",
                                                    "Đóng"
                                            );

                                            break;

                                    }

                                } else if (player.iDMark.getIndexMenu() == 0) {
                                    switch (select) {
                                        case 1:
                                            this.createOtherMenu(player, 1, "|7|MUA VIP THÁNG\n"
                                                    + "|2|Số Dư hiện tại: " + Util.format(player.getSession().vnd) + " VND"
                                                    + "\n|7|TRẠNG THÁI: VIP " + player.vip
                                                    + (player.timevip > 0 ? "\n|5|Hạn còn: " + "Vĩnh Viễn" : ""),
                                                    "Kích Hoạt\nVIP 1\n0 Điểm", "Kích Hoạt\nVIP 2\n50.000$", "Kích Hoạt\nVIP 3\n100.000$", "Kích Hoạt\nVIP 4\n150.000$", "Kích Hoạt\nVIP 5\n500.000$", "Kích Hoạt\nVIP 6\n1M3 $", "Kích Hoạt\nVIP 7\n2M3 $", "Kích Hoạt\nVIP 8\n5M $", "Kích Hoạt\nVIP 9\n8M $", "Kích Hoạt\nVIP 10\n10M $");
                                            break;
                                        case 0:
                                            this.createOtherMenu(player, ConstNpc.CONFIRM_ACTIVE, "|5|KÍCH HOẠT TÀI KHOẢN 0Đ\n" + "|1|Hệ thống sẽ Cho Bạn Quyền Năng GIAO DỊCH  + ĂN CODE VIP HẰNG NGÀY\n" + "|5|Số tiền hiện tại : " + Util.format(player.getSession().vnd) + " VNĐ" + "\n|7|Trạng thái: " + (player.getSession().actived == false ? "Chưa Kích Hoạt" : "Đã Nhận Kích Hoạt"), "Kích Hoạt\nFree", "Đóng");
                                            break;
                                        case 2:
                                            if (player.diemdanh == 0) {

                                                int bitcoin = 0;
                                                int daugod = 0;

                                                switch (player.vip) {

                                                    case 0:
                                                        break;
                                                    case 1:
                                                        bitcoin = 0;
                                                        daugod = 50;
                                                        break;
                                                    case 2:
                                                        bitcoin = 1;
                                                        daugod = 100;
                                                        break;
                                                    case 3:
                                                        bitcoin = 2;
                                                        daugod = 200;
                                                        break;
                                                    case 4:
                                                        bitcoin = 5;
                                                        daugod = 300;
                                                        break;
                                                    case 5:
                                                        bitcoin = 7;
                                                        daugod = 500;
                                                        break;
                                                    case 6:
                                                        bitcoin = 10;
                                                        daugod = 1000;
                                                        break;
                                                    case 7:
                                                        bitcoin = 15;
                                                        daugod = 1300;
                                                        break;
                                                    case 8:
                                                        bitcoin = 20;
                                                        daugod = 2000;
                                                        break;
                                                        case 9:
                                                        bitcoin = 20;
                                                        daugod = 2000;
                                                        break;  
                                                        
                                                        case 10:
                                                        bitcoin = 20;
                                                        daugod = 2000;
                                                        break;  

                                                }

//                                               
                                                Item b3 = ItemService.gI().createNewItem((short) 1594, bitcoin);
                                                b3.itemOptions.add(new ItemOption(30, Util.nextInt(0, 1)));
                                                InventoryService.gI().addItemBag(player, b3, 9999999999l);
                                                InventoryService.gI().sendItemBags(player);
                                                Service.getInstance().sendMoney(player);
//
//                                                //********************************************************************************** 
                                                Item b4 = ItemService.gI().createNewItem((short) 1111, daugod);
                                                b4.itemOptions.add(new ItemOption(30, Util.nextInt(0, 1)));
                                                InventoryService.gI().addItemBag(player, b4, 999999999);
                                                InventoryService.gI().sendItemBags(player);
                                                Service.getInstance().sendMoney(player);
//
//                                              
                                                player.diemdanh = System.currentTimeMillis();
                                                Service.getInstance().sendThongBaoFromAdmin(player, "Bạn Nhận Qua Vip Hôm Nay Thành Công ! ");
                                            } else {
                                                this.npcChat(player, "Hôm nay đã nhận rồi mà mày bug nữa đi tao chém chết con mẹ mày!!!");
                                            }
                                            break;
                                    }
                                } else if (player.iDMark.getIndexMenu() == 1) {
                                    switch (select) {
                                        case 0:
                                            this.createOtherMenu(player, 2, "|7|VIP 1\n"
                                                    + "|4|Quyền lợi Mở Vip đi kèm.."
                                                    + "\nNhận 0 Bitcoin/Mỗi Ngày..."
                                                    + "\nNhận 50 Đậu God/Mỗi Ngày.."
                                                    + "\nTĂNG 1% Tỉ Lệ Đập Đồ vip.."
                                                    + "\nTĂNG 1% Tỉ Lệ UP Vật Phẩm."
                                                    + "\nTĂNG GIỚI HẠN ĐỤC ĐỒ 10S.."
                                                    + "\nTĂNG 10% Tỉ Lệ Up TNSM...."
                                                    + "\nTĂNG 10% HP KI SD GIAP...."
                                                    + "\nĐặc Quyền Thông Báo Vào Game"
                                                    + "\n|7|TRẠNG THÁI: VIP " + player.vip
                                                    + (player.timevip > 0 ? "\nHạn còn: " + "Vĩnh Viễn" : ""), "Kích Hoạt", "Đóng");
                                            break;
                                        case 1:
                                            this.createOtherMenu(player, 3, "|7|VIP 2\n"
                                                    + "|4|Quyền lợi Mở Vip đi kèm.."
                                                    + "\nNhận 1 Bitcoin/Mỗi Ngày..."
                                                    + "\nNhận 100 Đậu God/Mỗi Ngày."
                                                    + "\nTĂNG 2% Tỉ Lệ Đập Đồ vip.."
                                                    + "\nTĂNG 2% Tỉ Lệ UP Vật Phẩm."
                                                      + "\nTĂNG GIỚI HẠN ĐỤC ĐỒ 12S.."
                                                    + "\nTĂNG 20% Tỉ Lệ Up TNSM...."
                                                    + "\nTĂNG 100% HP KI SD GIAP...."
                                                    + "\nĐặc Quyền Thông Báo Vào Game"
                                                    + "\n|7|TRẠNG THÁI: VIP " + player.vip
                                                    + (player.timevip > 0 ? "\nHạn còn: " + "Vĩnh Viễn" : ""), "Kích Hoạt", "Đóng");
                                            break;
                                        case 2:
                                            this.createOtherMenu(player, 4, "|7|VIP 3\n"
                                                    + "|4|Quyền lợi Mở Vip đi kèm.."
                                                    + "\nNhận 2 Bitcoin/Mỗi Ngày..."
                                                    + "\nNhận 200 Đậu God/Mỗi Ngày."
                                                    + "\nTĂNG 3% Tỉ Lệ Đập Đồ vip.."
                                                    + "\nTĂNG 5% Tỉ Lệ UP Vật Phẩm."
                                                      + "\nTĂNG GIỚI HẠN ĐỤC ĐỒ 15S.."
                                                    + "\nTĂNG 50% Tỉ Lệ Up TNSM...."
                                                    + "\nTĂNG 500% HP KI SD GIAP...."
                                                    + "\nĐặc Quyền Thông Báo Vào Game"
                                                    + "\n|7|TRẠNG THÁI: VIP " + player.vip
                                                    + (player.timevip > 0 ? "\nHạn còn: " + "Vĩnh Viễn" : ""), "Kích Hoạt", "Đóng");
                                            break;
                                        case 3:
                                            this.createOtherMenu(player, 5, "|7|VIP 4\n"
                                                    + "|4|Quyền lợi Mở Vip đi kèm.."
                                                    + "\nNhận 5 Bitcoin/Mỗi Ngày..."
                                                    + "\nNhận 300 Đậu God/Mỗi Ngày."
                                                    + "\nTĂNG 4% Tỉ Lệ Đập Đồ vip.."
                                                    + "\nTĂNG 7% Tỉ Lệ UP Vật Phẩm."
                                                     + "\nTĂNG GIỚI HẠN ĐỤC ĐỒ 18S.."
                                                    + "\nTĂNG 100% Tỉ Lệ Up TNSM..."
                                                    + "\nTĂNG 1000% HP KI SD GIAP...."
                                                    + "\nĐặc Quyền Thông Báo Vào Game"
                                                    + "\n|7|TRẠNG THÁI: VIP " + player.vip
                                                    + (player.timevip > 0 ? "\nHạn còn: " + "Vĩnh Viễn" : ""), "Kích Hoạt", "Đóng");
                                            break;
                                        case 4:
                                            this.createOtherMenu(player, 6, "|7|VIP 5\n"
                                                    + "|4|Quyền lợi Mở Vip đi kèm.."
                                                    + "\nNhận 7 Bitcoin/Mỗi Ngày..."
                                                    + "\nNhận 500 Đậu God/Mỗi Ngày."
                                                    + "\nTĂNG 5% Tỉ Lệ Đập Đồ vip.."
                                                    + "\nTĂNG 12% Tỉ Lệ UP Vật Phẩm."
                                                      + "\nTĂNG GIỚI HẠN ĐỤC ĐỒ 20S.."
                                                    + "\nTĂNG 150% Tỉ Lệ Up TNSM..."
                                                    + "\nTĂNG 2000% HP KI SD GIAP...."
                                                    + "\nĐặc Quyền Thông Báo Vào Game"
                                                    + "\n|7|TRẠNG THÁI: VIP " + player.vip
                                                    + (player.timevip > 0 ? "\nHạn còn: " + "Vĩnh Viễn" : ""), "Kích Hoạt", "Đóng");
                                            break;

                                        case 5:
                                            this.createOtherMenu(player, 7, "|7|VIP 6\n"
                                                    + "|4|Quyền lợi Mở Vip đi kèm.."
                                                    + "\nNhận 10 Bitcoin/Mỗi Ngày.."
                                                    + "\nNhận 1000 Đậu God/Mỗi Ngày"
                                                    + "\nTĂNG 6% Tỉ Lệ Đập Đồ vip.."
                                                    + "\nTĂNG 15% Tỉ Lệ UP Vật Phẩm."
                                                      + "\nTĂNG GIỚI HẠN ĐỤC ĐỒ 25S.."
                                                    + "\nTĂNG 200% Tỉ Lệ Up TNSM..."
                                                    + "\nTĂNG 5000% HP KI SD GIAP...."
                                                    + "\nĐặc quyền ThôngBáo Vào Game"
                                                    + "\n|7|TRẠNG THÁI: VIP " + player.vip
                                                    + (player.timevip > 0 ? "\nHạn còn: " + "Vĩnh Viễn" : ""), "Kích Hoạt", "Đóng");
                                            break;
                                        case 6:
                                            this.createOtherMenu(player, 8, "|7|VIP 7\n"
                                                    + "|4|Quyền lợi Mở Vip đi kèm..."
                                                    + "\nNhận 15 Bitcoin/Mỗi Ngày..."
                                                    + "\nNhận 1300 Đậu God/Mỗi Ngày."
                                                    + "\nTĂNG 7% Tỉ Lệ Đập Đồ vip.."
                                                    + "\nTĂNG 20% Tỉ Lệ UP Vật Phẩm.."
                                                      + "\nTĂNG GIỚI HẠN ĐỤC ĐỒ 30S.."
                                                    + "\nTĂNG 250% Tỉ Lệ Up TNSM...."
                                                    + "\nTĂNG 10000% HP KI SD GIAP...."
                                                    + "\nĐặc Quyền Thông Báo Vào Game"
                                                    + "\n|7|TRẠNG THÁI: VIP " + player.vip
                                                    + (player.timevip > 0 ? "\nHạn còn: " + "Vĩnh Viễn" : ""), "Kích Hoạt", "Đóng");
                                            break;
                                        case 7:
                                            this.createOtherMenu(player, 9, "|7|VIP 8\n"
                                                    + "|4|Quyền lợi Mở Vip đi kèm..."
                                                    + "\nNhận 20 Bitcoin/Mỗi Ngày..."
                                                    + "\nNhận 2000 Đậu God/Mỗi Ngày."
                                                    + "\nTĂNG 8% Tỉ Lệ Đập Đồ vip.."
                                                    + "\nTĂNG 25% Tỉ Lệ UP Vật Phẩm."
                                                      + "\nTĂNG GIỚI HẠN ĐỤC ĐỒ 40S.."
                                                    + "\nTĂNG 500% Tỉ Lệ Up TNSM...."
                                                    + "\nTĂNG 20000% HP KI SD GIAP...."
                                                    + "\nĐặc Quyền Thông Báo Vào Game"
                                                    + "\n|7|TRẠNG THÁI: VIP " + player.vip
                                                    + (player.timevip > 0 ? "\nHạn còn: " + "Vĩnh Viễn" : ""), "Kích Hoạt", "Đóng");
                                            break;
                                       case 8:
                                            this.createOtherMenu(player, 10, "|7|VIP 9\n"
                                                    + "|4|Quyền lợi Mở Vip đi kèm..."
                                                    + "\nNhận 20 Bitcoin/Mỗi Ngày..."
                                                    + "\nNhận 2000 Đậu God/Mỗi Ngày."
                                                    + "\nTĂNG 10% Tỉ Lệ Đập Đồ vip.."
                                                    + "\nTĂNG 25% Tỉ Lệ UP Vật Phẩm."
                                                      + "\nTĂNG GIỚI HẠN ĐỤC ĐỒ 50S.."
                                                    + "\nTĂNG 500% Tỉ Lệ Up TNSM...."
                                                    + "\nTĂNG 20000% HP KI SD GIAP...."
                                                    + "\nĐặc Quyền Thông Báo Vào Game"
                                                    + "\n|7|TRẠNG THÁI: VIP " + player.vip
                                                    + (player.timevip > 0 ? "\nHạn còn: " + "Vĩnh Viễn" : ""), "Kích Hoạt", "Đóng");
                                            break;       
                                        case 9:
                                            this.createOtherMenu(player, 11, "|7|VIP 10\n"
                                                    + "|4|Quyền lợi Mở Vip đi kèm..."
                                                    + "\nNhận 20 Bitcoin/Mỗi Ngày..."
                                                    + "\nNhận 2000 Đậu God/Mỗi Ngày."
                                                    + "\nTĂNG 12% Tỉ Lệ Đập Đồ vip.."
                                                    + "\nTĂNG 25% Tỉ Lệ UP Vật Phẩm."
                                                      + "\nTĂNG GIỚI HẠN ĐỤC ĐỒ 60S.."
                                                    + "\nTĂNG 500% Tỉ Lệ Up TNSM...."
                                                    + "\nTĂNG 20000% HP KI SD GIAP...."
                                                    + "\nĐặc Quyền Thông Báo Vào Game"
                                                    + "\n|7|TRẠNG THÁI: VIP " + player.vip
                                                    + (player.timevip > 0 ? "\nHạn còn: " + "Vĩnh Viễn" : ""), "Kích Hoạt", "Đóng");
                                            break;      
                                            
                                            

                                    }
                                } else if (player.iDMark.getIndexMenu() == 2) {
                                    switch (select) {
                                        case 0:
                                            if (player.vip >= 1) {
                                                this.npcChat(player, "|7|Bạn đang là  " + (player.vip == 8 ? "VIP 8" : "VIP" + player.vip) + " rồi");
                                                return;
                                            }
                                            if (player.getSession().vnd >= 0) {
                                                player.vip = 1;
                                                player.timevip = (System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 15)) + (1000 * 60 * 60 * 24 * 16);
                                                PlayerDAO.subvnd(player, 0);
                                                Service.getInstance().sendMoney(player);
                                                this.npcChat(player, "|6|Đã mở thành công\n|7|VIP 1");
                                            } else {
                                                this.npcChat(player, "Bạn không đủ Tiền");
                                            }
                                            break;
                                    }
                                } else if (player.iDMark.getIndexMenu() == 3) {
                                    switch (select) {
                                        case 0:
                                            if (player.vip >= 2) {
                                                this.npcChat(player, "|7|Bạn đang là  " + (player.vip == 8 ? "VIP 8" : "VIP" + player.vip) + " rồi");
                                                return;
                                            }
                                            if (player.getSession().vnd >= 50000) {
                                                player.vip = 2;
                                                player.timevip = (System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 15)) + (1000 * 60 * 60 * 24 * 16);
                                                PlayerDAO.subvnd(player, 50000);
                                                Service.getInstance().sendMoney(player);
                                                this.npcChat(player, "|6|Đã mở thành công\n|7|VIP 2");
                                            } else {
                                                this.npcChat(player, "Bạn không đủ Tiền");
                                            }
                                            break;
                                    }
                                } else if (player.iDMark.getIndexMenu() == 4) {
                                    switch (select) {
                                        case 0:
                                            if (player.vip >= 3) {
                                                this.npcChat(player, "|7|Bạn đang là  " + (player.vip == 8 ? "VIP 8" : "VIP" + player.vip) + " rồi");
                                                return;
                                            }
                                            if (player.getSession().vnd >= 100000) {
                                                player.vip = 3;
                                                player.timevip = (System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 15)) + (1000 * 60 * 60 * 24 * 16);
                                                PlayerDAO.subvnd(player, 100000);
                                                Service.getInstance().sendMoney(player);
                                                this.npcChat(player, "|6|Đã mở thành công\n|7|VIP 3");
                                            } else {
                                                this.npcChat(player, "Bạn không đủ Tiền");
                                            }
                                            break;
                                    }
                                } else if (player.iDMark.getIndexMenu() == 5) {
                                    switch (select) {
                                        case 0:
                                            if (player.vip >= 4) {
                                                this.npcChat(player, "|7|Bạn đang là  " + (player.vip == 8 ? "VIP 8" : "VIP" + player.vip) + " rồi");
                                                return;
                                            }
                                            if (player.getSession().vnd >= 150000) {
                                                player.vip = 4;
                                                player.timevip = (System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 15)) + (1000 * 60 * 60 * 24 * 16);
                                                PlayerDAO.subvnd(player, 150000);
                                                Service.getInstance().sendMoney(player);
                                                this.npcChat(player, "|6|Đã mở thành công\n|7|VIP 4");
                                            } else {
                                                this.npcChat(player, "Bạn không đủ Tiền");
                                            }
                                            break;

                                    }
                                } else if (player.iDMark.getIndexMenu() == 6) {
                                    switch (select) {
                                        case 0:
                                            if (player.vip >= 5) {
                                                this.npcChat(player, "|7|Bạn đang là  " + (player.vip == 8 ? "VIP 8" : "VIP" + player.vip) + " rồi");
                                                return;
                                            }
                                            if (player.getSession().vnd >= 500000) {
                                                player.vip = 5;
                                                player.timevip = (System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 15)) + (1000 * 60 * 60 * 24 * 16);
                                                PlayerDAO.subvnd(player, 500000);
                                                Service.getInstance().sendMoney(player);
                                                this.npcChat(player, "|6|Đã mở thành công\n|7|VIP 5");
                                            } else {
                                                this.npcChat(player, "Bạn không đủ Tiền");
                                            }
                                            break;
                                    }
                                } else if (player.iDMark.getIndexMenu() == 7) {
                                    switch (select) {
                                        case 0:
                                            if (player.vip >= 6) {
                                                this.npcChat(player, "|7|Bạn đang là  " + (player.vip == 8 ? "VIP 8" : "VIP" + player.vip) + " rồi");
                                                return;
                                            }
                                            if (player.getSession().vnd >= 1300000) {
                                                player.vip = 6;
                                                player.timevip = (System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 15)) + (1000 * 60 * 60 * 24 * 16);
                                                PlayerDAO.subvnd(player, 1300000);
                                                Service.getInstance().sendMoney(player);
                                                this.npcChat(player, "|6|Đã mở thành công\n|7|VIP 6");
                                            } else {
                                                this.npcChat(player, "Bạn không đủ Tiền");
                                            }
                                            break;
                                    }
                                } else if (player.iDMark.getIndexMenu() == 8) {
                                    switch (select) {
                                        case 0:
                                            if (player.vip >= 7) {
                                                this.npcChat(player, "|7|Bạn đang là  " + (player.vip == 8 ? "VIP 8" : "VIP" + player.vip) + " rồi");
                                                return;
                                            }
                                            if (player.getSession().vnd >= 2300000) {
                                                player.vip = 7;
                                                player.timevip = (System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 15)) + (1000 * 60 * 60 * 24 * 16);
                                                PlayerDAO.subvnd(player, 2300000);
                                                Service.getInstance().sendMoney(player);
                                                this.npcChat(player, "|6|Đã mở thành công\n|7|VIP 7");
                                            } else {
                                                this.npcChat(player, "Bạn không đủ Tiền");
                                            }
                                            break;
                                    }
                                } else if (player.iDMark.getIndexMenu() == 9) {
                                    switch (select) {
                                        case 0:
                                            if (player.vip >= 8) {
                                                this.npcChat(player, "|7|Bạn đang là  " + (player.vip == 8 ? "VIP 8" : "VIP" + player.vip) + " rồi");
                                                return;
                                            }
                                            if (player.getSession().vnd >= 5000000) {
                                                player.vip = 8;
                                                player.timevip = (System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 15)) + (1000 * 60 * 60 * 24 * 16);
                                                PlayerDAO.subvnd(player, 5000000);
                                                Service.getInstance().sendMoney(player);
                                                this.npcChat(player, "|6|Đã mở thành công\n|7|VIP 8");
                                            } else {
                                                this.npcChat(player, "Bạn không đủ Tiền");
                                            }
                                            break;

                                    }

                                     } else if (player.iDMark.getIndexMenu() == 10) {
                                    switch (select) {
                                        case 0:
                                            if (player.vip >= 9) {
                                                this.npcChat(player, "|7|Bạn đang là  " + (player.vip == 10 ? "VIP 10" : "VIP" + player.vip) + " rồi");
                                                return;
                                            }
                                            if (player.getSession().vnd >= 8000000) {
                                                player.vip = 9;
                                                player.timevip = (System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 15)) + (1000 * 60 * 60 * 24 * 16);
                                                PlayerDAO.subvnd(player, 8000000);
                                                Service.getInstance().sendMoney(player);
                                                this.npcChat(player, "|6|Đã mở thành công\n|7|VIP 9");
                                            } else {
                                                this.npcChat(player, "Bạn không đủ Tiền");
                                            }
                                            break;

                                    }
                                    
                                  } else if (player.iDMark.getIndexMenu() == 11) {
                                    switch (select) {
                                        case 0:
                                            if (player.vip >= 10) {
                                                this.npcChat(player, "|7|Bạn đang là  " + (player.vip == 10 ? "VIP 10" : "VIP" + player.vip) + " rồi");
                                                return;
                                            }
                                            if (player.getSession().vnd >= 10000000) {
                                                player.vip = 10;
                                                player.timevip = (System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 15)) + (1000 * 60 * 60 * 24 * 16);
                                                PlayerDAO.subvnd(player, 10000000);
                                                Service.getInstance().sendMoney(player);
                                                this.npcChat(player, "|6|Đã mở thành công\n|7|VIP 10");
                                            } else {
                                                this.npcChat(player, "Bạn không đủ Tiền");
                                            }
                                            break;

                                    }    
                                    
                                    
                                    
                                    
                                } else if (player.iDMark.getIndexMenu() == ConstNpc.CONFIRM_ACTIVE) {
                                    switch (select) {
                                        case 0:
                                            if (player.getSession().vnd > 0) {
                                                if (PlayerDAO.subVND2(player, 0)) {
                                                    Service.getInstance().sendThongBaoFromAdmin(player, "Đã mở Dấu Ấn Rồng Thành công!");
                                                } else {
                                                    this.npcChat(player, "Lỗi vui lòng báo admin...");
                                                }
                                            } else {
                                                Service.getInstance().sendThongBaoFromAdmin(player, "Không Đủ 0đ Hoặc Nạp Gói Nạp Đầu Để Mở FREE");
                                            }
                                            break;
                                        case 1:
                                            this.npcChat(player, "Lần sau tiếp lúa cho ta nữa nha con!!!");
                                            break;

                                    }
                                }
                            }
                        }
                    };
                    break;

                case ConstNpc.SHOPGOI:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 5) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "|7|Chào Mừng Các Bạn Mới Gia Nhập\n"
                                            + "|4|SHOP DRAGON PHỤC VỤ FREE TÂN THỦ\n"
                                            + "BÁN BẰNG 1 NGỌC\n"
                                            + "|8|EM BÁN SẴN CÁC GÓI ƯU ĐÃI\n"
                                            + "|7|Số Dư: " + Util.cap(player.getSession().vnd) + " Vnđ\n"
                                            + "|7|Tín Dụng: " + Util.cap(player.Tindung) + " Điểm\n"
                                            + "|4|\n",
                                            "Nạp \nLần Đầu", "Gói\nĐập Đồ", "Gói\nĐập Cánh", "Gói\nChuyển Sinh", "Gói\nChữa Lành");
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 5) {
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {

//                                                      if (player.vip < 5) {
//                                                Service.getInstance().sendThongBao(player, "Đặc Quyền Của Vip 5 Trở Lên");
//                                                return;
//                                            }
//                                                ShopService.gI().openShopSpecial(player, this, ConstNpc.thuky2, 1, 3);
//                                                break;
                                            case 0:
                                                NpcService.gI().createMenuConMeo(player, ConstNpc.napdau, 31808,
                                                        "|7|" + "Liên hệ Trường qua fb Xuân Trường (Sera) nhé\n"
                                                        + "|5|\n" + "GÓI ƯU ĐÃI NẠP ĐẦU   \n"
                                                        + "1M Chỉ Số Gốc HP, KI, SD             \n"
                                                        + "50 CẤP NHẬP MA (1M SD/CẤP)  \n"
                                                        + "50 CẤP TIÊN BANG               \n"
                                                        + "10K Đậu God\n"
                                                        + "+99 Điểm Tẩy Luyện Thiên Phú   \n"
                                                        + "|7|" + "Số Dư: " + Util.cap(player.getSession().vnd) + " Vnđ\n",
                                                        "Mua\n99k Vnđ\nGiá Trị\n100%", "Mua\n199k Vnđ\nGiá Trị\n250%", "Mua\n299k Vnđ\nGiá Trị\n350%", "Mua\n599k Vnđ\nGiá Trị\n600%"
                                                );
                                                break;
                                            case 1:
                                                NpcService.gI().createMenuConMeo(player, ConstNpc.goidapdo, 31808,
                                                        "|7|\nLiên hệ với Trường qua https://www.facebook.com/serayeuem\n"
                                                        + "|5|GÓI ĐẬP ĐỒ 150K TRỊ GIÁ 100%\n"
                                                        + "1 set Huyết Ma 10M Chỉ Số               \n"
                                                        + "1 Cải Trang 10.000%             \n"
                                                        + "100 Triệu RuBy,Ngọc Xanh       \n"
                                                        + "X100.000 thỏi vàng            \n"
                                                        + "+150 Điểm Tẩy Luyện Thiên Phú \n"
                                                        + "|7|" + "Số Dư: " + Util.cap(player.getSession().vnd) + " Vnđ\n",
                                                        "Mua\n150k Vnđ\nGiá Trị\n100%", "Mua\n300k Vnđ\nGiá Trị\n250%", "Mua\n600k Vnđ\nGiá Trị\n400%", "Mua\n900k Vnđ\nGiá Trị\n600%"
                                                );
                                                break;
                                            case 2:
                                                NpcService.gI().createMenuConMeo(player, ConstNpc.nangcapcanh, 31808,
                                                        "|7|\nLiên hệ với Trường qua https://www.facebook.com/serayeuem\n"
                                                        + "|5|\n" + "GÓI NÂNG CẤP CÁNH 150K TRỊ GIÁ 100%\n"
                                                        + "+50K Đá Ngũ Sắc               \n"
                                                        + "+1 Cánh Lv 1 Nâng cấp%         \n"
                                                        + "+20k Điểm TrainFarm             \n"
                                                        + "+150 Điểm Tẩy Luyện Thiên Phú   \n"
                                                        + "|7|" + "Số Dư: " + Util.cap(player.getSession().vnd) + " Vnđ\n",
                                                        "Mua\n150k Vnđ\nGiá Trị\n100%", "Mua\n300k Vnđ\nGiá Trị\n250%", "Mua\n600k Vnđ\nGiá Trị\n370%", "Mua\n900k Vnđ\nGiá Trị\n590%"
                                                );

                                                break;
                                            case 3:
                                                NpcService.gI().createMenuConMeo(player, ConstNpc.goichuyensinh, 31808,
                                                        "|7|\nLiên hệ với Trường qua https://www.facebook.com/serayeuem\n"
                                                        + "|5|GÓI CHUYỂN SINH 99K  \n"
                                                        + "1 Thú Cưỡi Vip TNSM,SD,HP,KI 10.000%       \n"
                                                        + "5 Cấp Chuyển Sinh Buff        \n"
                                                        + "Random Lỗ 1-99%"
                                                        + "200M EXP Tiên Bang             \n"
                                                        + "+99 Điểm Tẩy Luyện Thiên Phú    \n"
                                                        + "|7|" + "Số Dư: " + Util.cap(player.getSession().vnd) + " Vnđ\n",
                                                        "Mua\n99k Vnđ\nGiá Trị\n100%", "Mua\n199k Vnđ\nGiá Trị\n200%", "Mua\n299k Vnđ\nGiá Trị\n300%", "Mua\n499k Vnđ\nGiá Trị\n500%"
                                                );

                                                break;
                                            case 4:
                                                NpcService.gI().createMenuConMeo(player, ConstNpc.chualanh, 31808,
                                                        "|7|\nLiên hệ với Trường qua https://www.facebook.com/serayeuem\n"
                                                        + "|5|\b" + "GÓI Chữa Lành 99K   \n"
                                                        + "+99.000 Đá Quý(Gỡ Sao Pha Lê) \n"
                                                        + "+1 Viên Ngọc Rồng Siêu Cấp    \n"
                                                        + "+1 Sao Thiên Phú Tu Tiên      \n"
                                                        + "+10M Exp Tu Tiên              \n"
                                                        + "+120 Điểm Tẩy Luyện Thiên Phú  \n"
                                                        + "|7|" + "Số Dư: " + Util.cap(player.getSession().vnd) + " Vnđ\n",
                                                        "Mua\n120k Vnđ\nGiá Trị\n100%", "Mua\n298k Vnđ\nGiá Trị\n200%", "Mua\n600k Vnđ\nGiá Trị\n300%", "Mua\n1200k Vnđ\nGiá Trị\n500%"
                                                );

                                                break;

                                        }

                                    }
                                }
                            }
                        }
                    };
                    break;

                case ConstNpc.thuky:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 0) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "|7|Chào Mừng Các Bạn Mới Gia Nhập\n"
                                            + "|4|Đây Là Trưởng Quỹ\n"
                                            + "Hãy Up Quái Để Nhận Điểm\n"
                                            + "|7|Điểm TrainFarm: " + Util.cap(player.diemfam) + " Điểm\n"
                                            + "|2|LH Admin\n",
                                            "Đổi\nTàiNguyên", "Đổi\nTrangBị", "Đổi\nCảiTrang", "Đổi\nPhụKiện", "Đổi\nLinhThú", "Đổi\nNhận Đệ");
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 0) {
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {

                                            case 0:
                                                NpcService.gI().createMenuConMeo(player, ConstNpc.doitainguyen, 31808,
                                                        "|7|Chúc Bạn Một Ngày Vui Vẻ\n"
                                                        + "|5|Dưới Đây Là Các Tài Nguyên Tu Luyện\n"
                                                        + "Cày Chay Up quái Nhận Điểm TrainFarm Đến Đây Đổi\n"
                                                        + "Tỉ Lệ Up 50%/100% Tất Cả Các Map Trong Game\n"
                                                        + "Tỉ Lệ Đổi 10k Điểm Đổi/1lần/10k viên\n"
                                                        + "|7|Điểm TrainFarm: " + Util.cap(player.diemfam) + " điểm\n",
                                                        "Đổi\nĐá\nNângCấp", "Đổi\nĐá\nNộiTại", "Đổi\nNgọcX", "Đổi\nRuBy", "Đổi\nVàng", "Đổi\nĐậuGod"
                                                );
                                                break;
                                            case 1:
                                                NpcService.gI().createMenuConMeo(player, ConstNpc.doitrangbi, 31808,
                                                        "|7|Chúc Bạn Một Ngày Vui Vẻ\n"
                                                        + "|5|Dưới Đây Là Các Trang Bị Vip\n"
                                                        + "Cày Chay Up quái Nhận Điểm TrainFarm Đến Đây Đổi\n"
                                                        + "Tỉ Lệ Up 50%/100% Tất Cả Các Map Trong Game\n"
                                                        + "Tỉ Lệ Đổi 50k Điểm Đổi/1lần/set/ Cs SD, HP, KI, GIÁP, CM\n"
                                                        + "|7|" + "Điểm TrainFarm: " + Util.cap(player.diemfam) + " điểm\n",
                                                        "Đổi\nset\nThầnLinh\nTD", "Đổi\nset\nThầnLinh\nXD", "Đổi\nset\nThầnLinh\nNM", "Đổi\nset\nHủyDiệt\nTD", "Đổi\nset\nHuỷDiệt\nXD", "Đổi\nset\nHuỷDiệt\nNM"
                                                );
                                                break;
                                            case 2:
                                                NpcService.gI().createMenuConMeo(player, ConstNpc.doicaitrang, 31808,
                                                        "|7|Chúc Bạn Một Ngày Vui Vẻ\n"
                                                        + "|5|Dưới Đây Là Các Cải Trang Vip\n"
                                                        + "Cày Chay Up quái Nhận Điểm TrainFarm Đến Đây Đổi\n"
                                                        + "Tỉ Lệ Up 50%/100% Tất Cả Các Map Trong Game\n"
                                                        + "Tỉ Lệ Đổi 100k Điểm Đổi/1 lần/300% SD, HP, KI, GIÁP, CM\n"
                                                        
                                                        + "|7|" + "Điểm TrainFarm: " + Util.cap(player.diemfam) + " điểm\n",
                                                        "Đổi\nCảiTrang\nMayMắn", "Đổi\nCảiTrang\nMayMắn", "Đổi\nCảiTrang\nMayMắn", "Đổi\nCảiTrang\nMayMắn", "Đổi\nCảiTrang\nMayMắn", "Đổi\nCảiTrang\nMayMắn"
                                                );
                                                break;
                                            case 3:
                                                NpcService.gI().createMenuConMeo(player, ConstNpc.doiphukien, 31808,
                                                        "|7|Chúc Bạn Một Ngày Vui Vẻ\n"
                                                        + "|5|Dưới Đây Là Các Phụ Kiện Vip\n"
                                                        + "Cày Chay Up quái Nhận Điểm TrainFarm Đến Đây Đổi\n"
                                                        + "Tỉ Lệ Up 5%/100% Tất Cả Các Map Trong Game\n"
                                                        + "Tỉ Lệ Đổi 150k Điểm Đổi/1 lần/150% SD, HP, KI, GIÁP, CM\n"
                                                        
                                                        + "|7|" + "Điểm TrainFarm: " + Util.cap(player.diemfam) + " điểm\n",
                                                        "Đổi\nPhụKiện\nMayMắn", "Đổi\nPhụKiện\nMayMắn", "Đổi\nPhụKiện\nMayMắn", "Đổi\nPhụKiện\nMayMắn", "Đổi\nPhụKiện\nMayMắn", "Đổi\nPhụKiện\nMayMắn"
                                                );
                                                break;
                                            case 4:
                                                NpcService.gI().createMenuConMeo(player, ConstNpc.doilinhthu, 31808,
                                                        "|7|Chúc Bạn Một Ngày Vui Vẻ\n"
                                                        + "|5|Dưới Đây Là Các Linh Thú Vip\n"
                                                        + "Cày Chay Up quái Nhận Điểm TrainFarm Đến Đây Đổi\n"
                                                        + "Tỉ Lệ Up 5%/100% Tất Cả Các Map Trong Game\n"
                                                        + "Tỉ Lệ Đổi 200k Điểm Đổi/1 lần/150% SD, HP, KI, GIÁP, CM\n"
                                                        
                                                        + "|7|" + "Điểm TrainFarm: " + Util.cap(player.diemfam) + " điểm\n",
                                                        "Đổi\nLinhThú\nMayMắn", "Đổi\nLinhThú\nMayMắn", "Đổi\nLinhThú\nMayMắn", "Đổi\nLinhThú\nMayMắn", "Đổi\nLinhThú\nMayMắn", "Đổi\nLinhThú\nMayMắn"
                                                );
                                                break;
                                            case 5:
                                                NpcService.gI().createMenuConMeo(player, ConstNpc.doidetu, 31808,
                                                        "|7|Chúc Bạn Một Ngày Vui Vẻ\n"
                                                        + "|5|Dưới Đây Là Các đệ Vip\n"
                                                        + "Cày Chay Up quái Nhận Điểm TrainFarm Đến Đây Đổi\n"
                                                        + "Tỉ Lệ Up 5%/100% Tất Cả Các Map Trong Game\n"
                                                        + "Tỉ Lệ Đổi 10.000 Điểm Đổi /1 lần\n"
                                                        
                                                        + "|7|" + "Điểm TrainFarm: " + Util.cap(player.diemfam) + " điểm\n",
                                                        "Đổi\nĐệVip\nMayMắn", "Đổi\nĐệVip\nMayMắn", "Đổi\nĐệVip\nMayMắn", "Đổi\nĐệVip\nMayMắn", "Đổi\nĐệVip\nMayMắn", "Đổi\nĐệVip\nMayMắn"
                                                );
                                                break;

//                                            case 0:
//                                                ShopService.gI().openShopSpecial(player, this, ConstNpc.thuky1, 0, 3);
//                                                break;
//
////                                                 case 1:
////                                                      if (player.vip < 5) {
////                                                Service.getInstance().sendThongBao(player, "Đặc Quyền Của Vip 5 Trở Lên");
////                                                return;
////                                            }
////                                                ShopService.gI().openShopSpecial(player, this, ConstNpc.thuky2, 1, 3);
////                                                break;
//                                            case 1:
//                                                NpcService.gI().createMenuConMeo(player, ConstNpc.napdau, -1,
//                                                        "|7|" + "Liên hệ Trường qua fb Xuân Trường (Sera) nhé\n"
//                                                        + "|5|\n" + "GÓI ƯU ĐÃI NẠP ĐẦU   \n"
//                                                        + "1M Chỉ Số Gốc HP, KI, SD             \n"
//                                                        + "100 CẤP NHẬP MA (1M SD/CẤP)  \n"
//                                                        + "100 CẤP TIÊN BANG               \n"
//                                                        + "10K Đậu God\n"
//                                                        + "+99 Điểm Tẩy Luyện Thiên Phú   \n"
//                                                        + "|7|" + "Số Dư: " + Util.cap(player.getSession().vnd) + " Vnđ\n",
//                                                        "Mua\n99k Vnđ\nGiá Trị\n100%", "Mua\n199k Vnđ\nGiá Trị\n250%", "Mua\n299k Vnđ\nGiá Trị\n350%", "Mua\n599k Vnđ\nGiá Trị\n600%"
//                                                );
//                                                break;
//                                            case 2:
//                                                NpcService.gI().createMenuConMeo(player, ConstNpc.goidapdo, -1,
//                                                        "|7|\nLiên hệ với Trường qua https://www.facebook.com/serayeuem\n"
//                                                        + "|5|\n" + "GÓI ĐẬP ĐỒ 150K TRỊ GIÁ 100%\n"
//                                                        + "1 set Huyết Ma 100M Chỉ Số               \n"
//                                                        + "1 Cải Trang 10.000%             \n"
//                                                        + "100 Triệu Ruby,Ngọc Xanh       \n"
//                                                        + "X100.000 thỏi vàng            \n"
//                                                        + "+150 Điểm Tẩy Luyện Thiên Phú \n"
//                                                        + "|7|" + "Số Dư: " + Util.cap(player.getSession().vnd) + " Vnđ\n",
//                                                        "Mua\n150k Vnđ\nGiá Trị\n100%", "Mua\n300k Vnđ\nGiá Trị\n250%", "Mua\n600k Vnđ\nGiá Trị\n400%", "Mua\n900k Vnđ\nGiá Trị\n600%"
//                                                );
//                                                break;
//                                            case 3:
//                                                NpcService.gI().createMenuConMeo(player, ConstNpc.nangcapcanh, -1,
//                                                        "|7|\nLiên hệ với Trường qua https://www.facebook.com/serayeuem\n"
//                                                        + "|5|\n" + "GÓI NÂNG CẤP CÁNH 150K TRỊ GIÁ 100%\n"
//                                                        + "+500K Đá Ngũ Sắc               \n"
//                                                        + "+1 Cánh Lv 1 Nâng cấp%         \n"
//                                                        + "+20k Điểm TrainFam             \n"
//                                                        + "+150 Điểm Tẩy Luyện Thiên Phú   \n"
//                                                        + "|7|" + "Số Dư: " + Util.cap(player.getSession().vnd) + " Vnđ\n",
//                                                        "Mua\n150k Vnđ\nGiá Trị\n100%", "Mua\n300k Vnđ\nGiá Trị\n250%", "Mua\n600k Vnđ\nGiá Trị\n370%", "Mua\n900k Vnđ\nGiá Trị\n590%"
//                                                );
//
//                                                break;
//                                            case 4:
//                                                NpcService.gI().createMenuConMeo(player, ConstNpc.goichuyensinh, -1,
//                                                        "|7|\nLiên hệ với Trường qua https://www.facebook.com/serayeuem\n"
//                                                        + "|5|\b" + "GÓI CHUYỂN SINH 99K  \n"
//                                                        + "1 Thú Cưỡi Vip TNSM,SD,HP,KI 10.000%       \n"
//                                                        + "10 Cấp Chuyển Sinh Buff        \n"
//                                                        + "Random Lỗ 1-99%"
//                                                        + "200M EXP Tiên Bang             \n"
//                                                        + "+99 Điểm Tẩy Luyện Thiên Phú    \n"
//                                                        + "|7|" + "Số Dư: " + Util.cap(player.getSession().vnd) + " Vnđ\n",
//                                                        "Mua\n99k Vnđ\nGiá Trị\n100%", "Mua\n199k Vnđ\nGiá Trị\n200%", "Mua\n299k Vnđ\nGiá Trị\n300%", "Mua\n499k Vnđ\nGiá Trị\n500%"
//                                                );
//
//                                                break;
//                                            case 5:
//                                                NpcService.gI().createMenuConMeo(player, ConstNpc.chualanh, -1,
//                                                        "|7|\nLiên hệ với Trường qua https://www.facebook.com/serayeuem\n"
//                                                        + "|5|\b" + "GÓI Chữa Lành 99K   \n"
//                                                        + "+99.000 Đá Quý(Gỡ Sao Pha Lê) \n"
//                                                        + "+1 Viên Ngọc Rồng Siêu Cấp    \n"
//                                                        + "+1 Sao Thiên Phú Tu Tiên      \n"
//                                                        + "+10M Exp Tu Tiên              \n"
//                                                        + "+120 Điểm Tẩy Luyện Thiên Phú  \n"
//                                                        + "|7|" + "Số Dư: " + Util.cap(player.getSession().vnd) + " Vnđ\n",
//                                                        "Mua\n120k Vnđ\nGiá Trị\n100%", "Mua\n298k Vnđ\nGiá Trị\n200%", "Mua\n600k Vnđ\nGiá Trị\n300%", "Mua\n1200k Vnđ\nGiá Trị\n500%"
//                                                );
//
//                                                break;
                                        }

                                    }
                                }
                            }
                        }
                    };
                    break;

                case ConstNpc.NPCSHE:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 5) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "|7|Chào Mừng Các Bạn Mới Gia Nhập\n"
                                            + "|4|Đây Là Kho quà Share\n"
                                            + "Hãy Chia Sẻ Bài Viết Để Nhận Điểm\n"
                                            + "|8|" + "\nĐiểm Chia sẻ: " + Util.cap(player.diemshe) + " Điểm\n"
                                            + "|2|LH Admin\n",
                                            "Nhận\nTài Nguyên", "Nhận\nĐiểm VIP", "Nhận\nDNC", "Nhận\nTiên Bang", "Nhận\nTiềm Năng", "Nhận\nChỉ Số");
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 5) {
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {

                                            case 0:
                                                NpcService.gI().createMenuConMeo(player, ConstNpc.she1, 31808,
                                                        "|7|Chúc Bạn Một Ngày Vui Vẻ\n"
                                                        + "|5|Dưới Đây Là Các Tài Nguyên Chia Sẻ\n"
                                                        + "Nhận ComBo Share Facebook\n"
                                                        + "100k ngọc + 100k Ruby + 10 tỷ vàng\n"
                                                        + "Yêu Cầu: 20 Điểm Share 1 Bài/1 Điểm"
                                                        + "Ib admin để lấy Bài Và Điểm\n"
                                                        + "|7|" + "Điểm Chia Sẻ: " + Util.cap(player.diemshe) + " Điểm\n",
                                                        "Đổi\nNgay", "Đóng"
                                                );
                                                break;
                                            case 1:
                                                NpcService.gI().createMenuConMeo(player, ConstNpc.she2, 31808,
                                                        "|7|Chúc Bạn Một Ngày Vui Vẻ\n"
                                                        + "|5|Dưới Đây Là Các Tài Nguyên Chia Sẻ\n"
                                                        + "Nhận ComBo Share Facebook\n"
                                                        + "10k điểm vip + 5000 điểm Farm\n"
                                                        + "Yêu Cầu: 50 Điểm Share 1 Bài/1 Điểm"
                                                        + "Ib admin để lấy Bài Và Điểm\n"
                                                        + "|7|" + "Điểm Chia Sẻ: " + Util.cap(player.diemshe) + " Điểm\n",
                                                        "Đổi\nNgay", "Đóng"
                                                );
                                                break;
                                            case 2:
                                                NpcService.gI().createMenuConMeo(player, ConstNpc.she3, 31808,
                                                        "|7|Chúc Bạn Một Ngày Vui Vẻ\n"
                                                        + "|5|Dưới Đây Là Các Tài Nguyên Chia Sẻ\n"
                                                        + "Nhận ComBo Share Facebook\n"
                                                        + "10K DNC + 5000 điểm Farm\n"
                                                        + "Yêu Cầu: 100 Điểm Share 1 Bài/1 Điểm"
                                                        + "Ib admin để lấy Bài Và Điểm\n"
                                                        + "|7|" + "Điểm Chia Sẻ: " + Util.cap(player.diemshe) + " Điểm\n",
                                                        "Đổi\nNgay", "Đóng"
                                                );
                                                break;
                                            case 3:
                                                NpcService.gI().createMenuConMeo(player, ConstNpc.she4, 31808,
                                                        "|7|Chúc Bạn Một Ngày Vui Vẻ\n"
                                                        + "|5|Dưới Đây Là Các Tài Nguyên Chia Sẻ\n"
                                                        + "Nhận ComBo Share Facebook\n"
                                                        + "50 CẤP TB + 5000 Điểm Farm\n"
                                                        + "Yêu Cầu: 150 Điểm Share 1 Bài/1 Điểm"
                                                        + "Ib admin để lấy Bài Và Điểm\n"
                                                        
                                                        + "|7|" + "Điểm Chia Sẻ: " + Util.cap(player.diemshe) + " Điểm\n",
                                                        "Đổi\nNgay", "Đóng"
                                                );
                                                break;
                                            case 4:
                                                NpcService.gI().createMenuConMeo(player, ConstNpc.she5, 31808,
                                                        "|7|Chúc Bạn Một Ngày Vui Vẻ\n"
                                                        + "|5|Dưới Đây Là Các Tài Nguyên Chia Sẻ\n"
                                                        + "Nhận ComBo Share Facebook\n"
                                                        + "100 Tỉ Tiềm Năng + 5000 Điểm Farm\n"
                                                        + "Yêu Cầu: 200 Điểm Share 1 Bài/1 Điểm"
                                                        + "Ib admin để lấy Bài Và Điểm\n"
                                                        
                                                        + "|7|" + "Điểm Chia Sẻ: " + Util.cap(player.diemshe) + " Điểm\n",
                                                        "Đổi\nNgay", "Đóng"
                                                );
                                                break;
                                            case 5:
                                                NpcService.gI().createMenuConMeo(player, ConstNpc.she6, 31808,
                                                        "|7|Chúc Bạn Một Ngày Vui Vẻ\n"
                                                        + "|5|Dưới Đây Là Các Tài Nguyên Chia Sẻ\n"
                                                        + "Nhận Combo Share Facebook\n"
                                                        + "10k CS Gốc + 5000 điểm Farm\n"
                                                        + "Yêu Cầu: 250 Điểm Share 1 Bài/1 Điểm"
                                                        + "Ib admin để lấy Bài Và Điểm\n"
                                                        
                                                        + "|7|" + "Điểm Chia Sẻ: " + Util.cap(player.diemshe) + " Điểm\n",
                                                        "Đổi\nNgay", "Đóng"
                                                );
                                                break;

                                        }

                                    }
                                }
                            }
                        }
                    };
                    break;

                case ConstNpc.hotro:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 21 || this.mapId == 22 || this.mapId == 23) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                            "|7|HELLO\n"
                                            + "|4|Hãy Chịu Khó Đi Up Điểm Farm\n"
                                            + "|7|Điểm Farm: " + Util.cap(player.diemfam) + " Điểm\n",
                                            "Đổi\nHành Tinh\nĐệ", "Đổi\nSkill 1, 2, 3", "Đổi\nSkill 4, 5, 6", "Đổi\nName", "Mở Full\nHành Trang","Mở Full\nRương Đồ");
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 21 || this.mapId == 22 || this.mapId == 23) {
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {

                                            case 0:
                                                NpcService.gI().createMenuConMeo(player, ConstNpc.doihanhtinhde, 31808,
                                                        "|4|Đổi Sang Hành Tinh Đệ Tử Theo Ý Muốn\n"
                                                        + "Chi Phí Mỗi Lần Đổi Là 100k Điểm TrainFarm\n"
                                                        + "|7|" + "Điểm Farm: " + Util.cap(player.diemfam) + " Điểm\n",
                                                        "Đổi\nTrái Đất", "Đổi\nNamec", "Đổi\nXayda", "Đóng"
                                                );
                                                break;
                                            case 1:
                                                NpcService.gI().createMenuConMeo(player, ConstNpc.doiskill2detu, 31808,
                                                        "|4|Đổi Tùy Y Skill 1,2,3 Đệ Tử Theo Ý Muốn\n"
                                                        + "Chi Phí Mỗi Lần Đổi Là 200k Điểm TrainFarm\n"
                                                        + "|7|" + "Điểm Farm: " + Util.cap(player.diemfam) + " Điểm\n",
                                                        "Đổi\nLiên Hoàn", "Đổi\nKamejoko", "Đổi\nAtomic", "Đổi\nMasenco",
                                                        "Đổi TDHS", "Đổi\nKaioken", "Đổi\nTTLN"
                                                );
                                                break;
                                            case 2:
                                                NpcService.gI().createMenuConMeo(player, ConstNpc.doiskill3detu, 31808,
                                                        "|4|Đổi Tùy Y Skill 4,5,6 Đệ Tử Theo Ý Muốn\n"
                                                        + "Chi Phí Mỗi Lần Đổi Là 500k Điểm TrainFarm\n"
                                                        + "|7|" + "Điểm Farm: " + Util.cap(player.diemfam) + " Điểm\n",
                                                        "Đổi\nĐẻ Trứng", "Đổi\nQCKK", "Đổi\nHuýt Sáo", "Đổi\nSuperKame", "Đổi\nMafuba",
                                                        "Đổi\nSuperAtomic", "Đổi\nBiến Khỉ", "Đổi\nLaze", "Đổi\nSôcla"
                                                );
                                                break;
                                            case 3:
                                                NpcService.gI().createMenuConMeo(player, ConstNpc.doiten, 31808,
                                                        "|4|Đây Là Đổi Tên Nhân Vật Cực Vip\n"
                                                        + "Chi Phí 100k Điểm Farm 1 Lần\n"
                                                        + "|7|" + "Điểm Farm: " + Util.cap(player.diemfam) + " Điểm\n",
                                                        "Đổi Ngay", "Đóng"
                                                );
                                                break;
                                            case 4:
                                                NpcService.gI().createMenuConMeo(player, ConstNpc.mohanhtrang, 31808,
                                                        "|4|Mở Full Hành Trang Với 1 Click\n"
                                                        + "Chi Phí 0 Điểm Farm 1 Lần\n"
                                                        + "|7|" + "Điểm Farm: " + Util.cap(player.diemfam) + " Điểm\n",
                                                        "Mở Ngay", "Đóng"
                                                );
                                                break;
                                              case 5:
                                                NpcService.gI().createMenuConMeo(player, ConstNpc.moruongdo, 31808,
                                                        "|4|Mở Full Rương Đồ Với 1 Click\n"
                                                        + "Chi Phí 0 Điểm Farm 1 Lần\n"
                                                        + "|7|" + "Điểm Farm: " + Util.cap(player.diemfam) + " Điểm\n",
                                                        "Mở Ngay", "Đóng"
                                                );
                                                break;    
                                                
                                                

                                        }

                                    }
                                }
                            }
                        }
                    };
                    break;

                case ConstNpc.CFPASS:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 5) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "|7|Chào Mừng Các Bạn Mới Gia Nhập\n"
                                            + "|4|Đây Là CF PASS Mùa 1\n"
                                            + "Hãy Săn Boss of Điểm danh Để Nhận Điểm Free\n"
                                            + "Nhận Mốc Thưởng Phong Phú\n"
                                            + "|8|Free Pass: " + Util.cap(player.cfpass) + " Điểm\n"
                                            + "Premium Pass: " + Util.cap(player.premium) + " Điểm\n"
                                            + "|2|Mua Gói Premium 200k + (50k Điểm) Mở Khóa Cày Điểm Premium \n",
                                            "Nhận\nquà Free", "Nhận\nquà premium", "Mua\nGói premium", "Top\nPass Free", "Top\nPass premium", "Hướng Dẫn");
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 5) {
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {

                                            case 0:
                                                NpcService.gI().createMenuConMeo(player, ConstNpc.cfpassfree, 31808,
                                                        "|7|Chúc Bạn Một Ngày Vui Vẻ\n"
                                                        + "|5|Dưới Đây Là Các Mốc CF PASS Free\n"
                                                        + "|4|Lưu ý:Tối đa 10v/mốc + dùng hết cũ Nhận Mới\n"
                                                        + "|3|Mốc 1: [5k Điểm] Nhận Free SPL 100% SD,HP,KI....\n"
                                                        + "|5|Mốc 2: [12k Điểm] Nhận Free SPL 150% SD,HP,KI...\n"
                                                        + "|4|Mốc 3: [18k Điểm] Nhận Free SPL 200% SD,HP,KI...\n"
                                                        + "|3|Mốc 4: [35k Điểm] Nhận Free SPL 250% SD,HP,KI...\n"
                                                        + "|5|Mốc 5: [75k Điểm] Nhận Free SPL 300% SD,HP,KI...\n"
                                                        + "|4|Mốc 6: [205k Điểm] Nhận Free SPL 350% SD,HP,KI..\n"
                                                        + "|3|Mốc 7 Trở lên Mỗi Mốc Cần: [500k Điểm] + TăngDần\n"
                                                        + "|7|" + "PASS FREE: " + Util.cap(player.cfpass) + " Điểm\n"
                                                        + "PASS Mốc: " + Util.cap(player.mocpass) + "  Điểm Đã Nhận",
                                                        "Nhận Ngay"
                                                );
                                                break;

                                            case 1:
                                                NpcService.gI().createMenuConMeo(player, ConstNpc.cfpassmua, 31808,
                                                        "|7|Chúc Bạn Một Ngày Vui Vẻ\n"
                                                        + "|5|Dưới Đây Là Các Mốc CF PASS premium\n"
                                                        + "|4|Lưu ý:Tối đa 10v Đá Khảm/mốc + dùng hết cũ Nhận Mới\n"
                                                        + "|3|Mốc 1: [50k Điểm] Nhận Free SPL 1000% SD,HP,KI,BẠO....\n"
                                                        + "|5|Mốc 2: [100k Điểm] Nhận Free SPL 2000% SD,HP,KI,BẠO...\n"
                                                        + "|4|Mốc 3: [150k Điểm] Nhận Free SPL 3000% SD,HP,KI,BẠO...\n"
                                                        + "|3|Mốc 4: [200k Điểm] Nhận Free SPL 4000% SD,HP,KI,BẠO...\n"
                                                        + "|5|Mốc 5: [250k Điểm] Nhận Free SPL 5000% SD,HP,KI,BẠO...\n"
                                                        + "|4|Mốc 6: [300k Điểm] Nhận Free SPL 6000% SD,HP,KI,BẠO..\n"
                                                        + "|3|Mốc 7 Trở lên Mỗi Mốc Cần: [500k Điểm] + TăngDần\n"
                                                        + "|7|PASS premium: " + Util.cap(player.premium) + " Điểm\n"
                                                        + "premium: Đã Nhận " + Util.cap(player.mocpassvip) + " Điểm",
                                                        "Nhận Ngay"
                                                );
                                                break;
                                            case 2:
                                                NpcService.gI().createMenuConMeo(player, ConstNpc.muagoipass, 31808,
                                                        "|7|Chúc Bạn Một Ngày Vui Vẻ\n"
                                                        + "|5|Dưới Đây Là Các Gói Mua CFPASS premium\n"
                                                        + "|4|Mua Gói 1: 200K ==> 52K Điểm... \n"
                                                        + "|3|Mua Gói 2: 500K ==> 200K Điểm.. \n"
                                                        + "|5|Mua Gói 3: 700K ==> 270K Điểm.. \n"
                                                        + "|4|Mua Gói 4: 1M   ==> 500K Điểm.. \n"
                                                        + "|3|Mua Gói 5: 2M   ==> 1500K Điểm..\n"
                                                        + "|5|Mua Gói 6: 5M   ==> 5000K Điểm..\n"
                                                        + "|4|Bắt Buộc Mua 1 Gói Để Cấp quyền.\n"
                                                        + "|3|Cày Cuốc Điểm Tại Săn Boss Of Up\n"
                                                        + "|7|PASS premium: " + Util.cap(player.premium) + " Điểm\n"
                                                        + "premium: Đã Nhận " + Util.cap(player.mocpassvip) + " Điểm",
                                                        "Mua\nGói 1", "Mua\nGói 2", "Mua\nGói 3", "Mua\nGói 4", "Mua\nGói 5", "Mua\nGói 6"
                                                );
                                                break;

                                            case 3:
                                                Service.getInstance().showListTop(player, Manager.Toppass);
                                                break;
                                            case 4:
                                                Service.getInstance().showListTop(player, Manager.Toppassvip);
                                                break;
                                            case 5:
                                                NpcService.gI().createMenuConMeo(player, ConstNpc.huongdan, 31808,
                                                        "|7|Chúc Bạn Một Ngày Vui Vẻ\n"
                                                        + "|4|Dưới Đây Là Hưỡng Dẫn\n"
                                                        + "|3|Độc quyền duy nhất kiếm sao pha lê Tại Đây\n"
                                                        + "|5|CFPASS Free Cày Cuốc Kiếm Từ Việc Điểm Danh + Up quái.......\n"
                                                        + "|4|CFPASS premium Bắt Buộc Mua Gói Để Được Cày Điểm premium....\n"
                                                        + "|3|premium Kiếm Điểm Từ Việc Săn Boss TRong all Game+ mua điểm.\n"
                                                        + "|5|Mỗi Mốc Nhận Tối Đa 10 Viên Đá Ép + Không Được Cộng Dồn.....\n"
                                                        + "|4|Mọi Chi Tiết: Xem Tại NPC CF PASS MÙA 1.....................\n"
                                                        + "|7|PASS premium: " + Util.cap(player.premium) + " Điểm\n"
                                                        + "|7|PASS Free:    " + Util.cap(player.cfpass) + " Điểm\n",
                                                        "Đã Hiểu"
                                                );
                                                break;

                                        }

                                    }
                                }
                            }
                        }
                    };
                    break;

                case ConstNpc.huongdan1:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 5) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "|7|HELLO\n"
                                            + "|4|TẤT CẢ HƯỚNG DẪN Ở ĐÂY\n"
                                            + "Hãy Click Từng Mục Để Xem\n"
                                            + "|8|Chú ý Đọc Tất Cả Cơ Chế\n"
                                            + "|2|LH Trường Nếu Bạn Không Biết Chữ\n",
                                            "Kiếm\nTrang Bị", "Kiếm\nSKH", "UP\nTu Tiên", "Up\nTiên Bang", "Up\nChỉ Số", "Mở Vip", "Nạp Game", "Nhập ma");
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 5) {
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {

                                            case 0:
                                                NpcService.gI().createMenuConMeo(player, ConstNpc.huongdan, 31808,
                                                        "|7|Chúc Bạn Một Ngày Vui Vẻ\n"
                                                        + "|6|UP Điểm TrainFam ALL quái tất cả các map\n"
                                                        + "|4|Khi đủ điểm TrainFarm (100k điểm) thì tới đổi NPC Event\n"
                                                        + "|5|Có Thể Đổi: Cải Trang, Trang Bị, Pet, Linh Thú\n"
                                                        + "|3|Chỉ số Của Món đồ RanDom Chỉ Số\n"
                                                        + "|6|Hoặc Up bên coler set Thần 25 sao cực VIP\n"
                                                        + "|4|Vàng Ngọc Ruby Đổi Từ Đường Tăng Map Làng Aru\n"
                                                        + "|7|" + "Điểm TrainFarm: " + Util.cap(player.diemfam) + " điểm\n",
                                                        "Đã Hiểu"
                                                );
                                                break;
                                            case 1:
                                                NpcService.gI().createMenuConMeo(player, ConstNpc.huongdan, 31808,
                                                        "|7|Hướng Dẫn TrainFarm\n"
                                                        + "|6|Up điểm TrainFarm khi đánh quái ở mọi map\n"
                                                        + "|4|Đủ 100k điểm thì tới NPC Event Để Đổi quà\n"
                                                        + "|5|Có thể đổi: Cải Trang, Trang Bị, Pet, Linh Thú\n"
                                                        + "|3|Chỉ số vật phẩm là Random\n"
                                                        + "|6|Vàng / Ngọc / Ruby → Đổi tại Đường Tăng (Làng Aru)\n"
                                                        + "|7|Điểm hiện tại: " + Util.cap(player.diemfam) + " điểm\n",
                                                        "Đã Hiểu"
                                                );
                                                break;
//                                            case 2:
//                                                NpcService.gI().createMenuConMeo(player, ConstNpc.huongdan, 31808,
//                                                        "|7|Chúc Bạn Một Ngày Vui Vẻ\n"
//                                                        + "|5|UP SKH Vip Tại Map Độc quyền\n"
//                                                        + "Yêu Cầu: Từ Vip 6 Trở Lên UP ALL Map\n"
//                                                        + "Chỉ Số Cực Vip: Tỉ Lệ Cực Dễ\n"
//                                                        + "$(5 món SKH +12999% ST Kaioken)\n"
//                                                        + "$(5 món SKH +15999% STQCKK)\n"
//                                                        + "$(5 món SKH +9999% ST Kamejoko)\n"
//                                                        + "$(5 món SKH +18999% KI)\n"
//                                                        + "$(5 món SKH +6666% ST Liên hoàn)\n"
//                                                        + "$(5 món SKH +8888% ST Đẻ Trứng)\n"
//                                                        + "$(5 món SKH +17999% ST đấm Galick)\n"
//                                                        + "$(5 món SKH +14999% ST hóa khỉ)\n"
//                                                        + "$(5 món SKH +29999% HP,KI)\n",
//                                                        "Đã Hiểu", "Đến Kiếm"
//                                                );
//                                                break;

                                            case 2:
                                                NpcService.gI().createMenuConMeo(player, ConstNpc.huongdan, 31808,
                                                        "|7|Chúc Bạn Một Ngày Vui Vẻ\n"
                                                        + "|5|Dưới Đây Là Hưỡng Dẫn Tu Tiên\n"
                                                        + "|4|Chat 'Tutien' để đến map Tu Tiên\n"
                                                        + "|3|Fam quái Vật Để Có EXP (Kinh Nghiệm)\n"
                                                        + "|5|Săn Boss kiếm Được Nhiều Hơn Từ Việc Up"
                                                        + "|4|Tất Cả Tăng Chỉ số Khủng (DAME, HP, KI, DEF)"
                                                        + "|3|Chi Tiết Check Tại NPC Thiên Đạo\n"
                                                        + "|5|Tất Cả Có 500 Cảnh giới Từ Luyện Khí\n"
                                                        + "|4|Đủ 500M EXP Tiến Hành Tới NPC Thiên Đạo Để Độ Kiếp Tu Vi\n"
                                                        + "|3|Thiên Đạo Độ Kiếp Với 10k VNĐ Tỉ Lệ Thành Công 100%\n",
                                                        "Đã Hiểu"
                                                );
                                                break;
                                            case 3:
                                                NpcService.gI().createMenuConMeo(player, ConstNpc.huongdan, 31808,
                                                        "|7|Chúc Bạn Một Ngày Vui Vẻ\n"
                                                        + "|5|Dưới Đây Là Up Tiên Bang\n"
                                                        + "|4|Cày Chay Săn Boss Kiếm EXP Tiên Bang\n"
                                                        + "|3|Đủ Tiến Trình Đạt 100% Auto Lên 1 Cấp\n"
                                                        + "|5|Check Tiến Trình Tại Npc Thiên Sứ Girl Mỗi Làng\n"
                                                        + "|4|Cấp Có Thể ăn Đan Up , Hoặc Săn Boss Kiếm Đan EXP\n"
                                                        + "|3|Tiên Bang giúp chúng Ta có hiệu ứng Đặc Biệt Như\n"
                                                        + "|5|UP Chỉ số gốc, Tăng Dame Chiêu, Nội Tại STCM, Choáng\n"
                                                        + "|7|" + "Cấp Tiên Bang: " + Util.cap(player.CapTamkjll) + " Cấp\n",
                                                        "Đã Hiểu"
                                                );
                                                break;
                                            case 4:
                                                NpcService.gI().createMenuConMeo(player, ConstNpc.huongdan, 31808,
                                                        "|7|Chúc Bạn Một Ngày Vui Vẻ\n"
                                                        + "|5|Dưới Đây Là HD Up Chỉ Số\n"
                                                        + "|4|Cày Chay Max Tiềm Năng Cộng (2M HP,KI 100K SD)\n"
                                                        + "|3|Có Thể Up Ra Chỉ Số Gốc Cao Hơn Bình Thường Và Vô Hạn\n"
                                                        + "|5|Cấp Tiên Bang Càng Cao Up Càng nhanh\n"
                                                        + "|4|Ví dụ Tiên Bang 1000 Cấp(Thì Có Tỉ Lệ Up Ra 40k Sức Đánh Gốc)\n"
                                                        ,
                                                        "Đã Hiểu"
                                                );
                                                break;
//                                            case 6:
//                                                NpcService.gI().createMenuConMeo(player, ConstNpc.huongdan, 31808,
//                                                        "|7|Chúc Bạn Một Ngày Vui Vẻ\n"
//                                                        + "|5|Dưới Đây Là HD Up Sự Kiện\n"
//                                                        + "Sự Kiện Diễn Ra Tại NPC SỰ KIỆN ở Đảo Kame\n"
//                                                        + "Mọi Người Có Thể qua Xem Chi Tiết Vật Phẩm Up Cần\n"
//                                                        + "Đua Top: SỰ KIỆN Theo admin Công Bố\n"
//                                                        ,
//                                                        "Đã Hiểu"
//                                                );
//                                                break;
                                            case 5:
                                                NpcService.gI().createMenuConMeo(player, ConstNpc.huongdan, 31808,
                                                        "|7|Chúc Bạn Một Ngày Vui Vẻ\n"
                                                        + "|5|Dưới Đây Là HD Mở Vip\n"
                                                        + "|4|Cày Chay Free Vip 1 - Mua Gói Vip Tăng Tỉ Lệ Up\n"
                                                        + "|3|Mở vip Để Tăng Tỉ Lệ Đục Đồ + Chức Năng Phụ\n"
                                                        + "|5|Mở vip Để Tăng Tỉ Lệ Up Vật Phẩm Từ Quái Boss\n"
                                                        + "|4|Mở vip Để Tăng Tỉ Lệ Săn Rơi Đồ Boss Ra Đồ Vĩnh Viễn\n",
                                                        "Đã Hiểu"
                                                );
                                                break;
                                            case 6:
                                                NpcService.gI().createMenuConMeo(player, ConstNpc.huongdan, 31808,
                                                        "|7|Chúc Bạn Một Ngày Vui Vẻ\n"
                                                        + "|6|Dưới Đây Là HD Nạp Game\n"
                                                        + "Nạp Game Ủng Hộ admin Duy Trì Game\n"
                                                        + "TK DUY NHẤT MBBANK\n"
                                                        + "0349236955 Vũ Xuân Trường\n"
                                                        + "ADMIN KHÔNG HỎI VAY TIỀN, NGOÀI STK NÀY LÀ LỪA ĐẢO\n"
                                                        ,
                                                        "Đã Hiểu"
                                                );
                                                break;
                                            case 7:
                                                NpcService.gI().createMenuConMeo(player, ConstNpc.huongdan, 31808,
                                                        "|7|Chúc Bạn Một Ngày Vui Vẻ\n"
                                                        + "|6|Dưới Đây Là HD Up Nhập Ma\n"
                                                        + "Yêu Cầu: Chạy Di chuyển liên Tục\n"
                                                        + "Mỗi Lần Chạy Tự Cộng EXP Nhập Ma\n"
                                                        + "Đủ EXP Tự Lên 1 Cấp 10k Sức Đánh/Cấp\n"
                                                        ,
                                                        "Đã Hiểu"
                                                );
                                                break;
                                            case 10:
                                                NpcService.gI().createMenuConMeo(player, ConstNpc.huongdan, 31808,
                                                        "|7|Chúc Bạn Một Ngày Vui Vẻ\n"
                                                        + "|5|Dưới Đây Là HD UP Đậu God\n"
                                                        + "|4|Yêu Cầu: VIP 4 Up All Map\n"
                                                        + "|3|Mỗi Lần Nhặt Được x100 Tỉ Lệ 1/100\n"
                                                        ,
                                                        "Đã Hiểu"
                                                );
                                                break;
                                            case 11:
                                                NpcService.gI().createMenuConMeo(player, ConstNpc.huongdan, 31808,
                                                        "|7|Chúc Bạn Một Ngày Vui Vẻ\n"
                                                        + "|5|Dưới Đây Là HD UP Điểm Vip\n"
                                                        + "Yêu Cầu: VIP 0 Up All Map\n"
                                                        + "Mỗi Lần Nhặt Được x1 Tỉ lệ 1/100\n"
                                                        ,
                                                        "Đã Hiểu"
                                                );
                                                break;
                                            case 12:
                                                NpcService.gI().createMenuConMeo(player, ConstNpc.huongdan, 31808,
                                                        "|7|Chúc Bạn Một Ngày Vui Vẻ\n"
                                                        + "|5|Dưới Đây Là HD UP Điểm Danh\n"
                                                        + "Yêu Cầu: VIP 0\n"
                                                        + "Mỗi Ngày Điểm Danh Free 1 Lần\n"
                                                        + "Nhận Random 1-500 Điểm Pass Free.\n"
                                                        + "Nhận Random 1-50 Điểm Premium....\n"
                                                        + "Nhận Random 1-5M EXP Tiên Bang...\n"
                                                        + "Nhận Random 1-500k EXP Nhập ma...\n"
                                                        + "Nhận Random 1-5000 Điểm Farm......\n"
                                                        + "Nhận Random 1-50 Điểm vip........\n"
                                                        + "Cảm ơn Mọi Người! Lh Admin.......\n",
                                                        "Đã Hiểu"
                                                );
                                                break;

                                        }

                                    }
                                }
                            }
                        }
                    };
                    break;

                case ConstNpc.vaymuon:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 5) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "|7|Chào Mừng Chủ Nhân Thiên Giới\n"
                                            + "|6|Thiên Đạo Độ Kiếp: 10k VND 100% Thành Công Lên Cảnh Giới\n"
                                            + "Độ Kiếp Tu Vi: Yêu Cầu 50M EXP Tu Tiên (Tỉ Lệ 50%)\n",
                                            //  + "|8|Điểm Tẩy Luyện:" + Util.cap(player.Tindung) + " Điểm\n",
                                            "Thông Tin\nCảnh Giới",
                                            "Luyện Hóa\nThiên Phú",
                                            "Thông Tin\nChỉ Số",
                                            "Thiên Đạo\nĐộ Kiếp",
                                            "Độ Kiếp \nTu Vi"
                                    //"Tôi Luyện\nThánh Thể"
                                    );

                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 5) {
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {

                                            case 0:
                                                this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                                        "|7|Chào Mừng Chủ Nhân Thiên Giới\n"
                                                        + "|6|Tu Vi Cảnh giới: [ " + player.TamkjllTuviTutien(Util.maxInt(player.Tamkjlltutien[1])) + " ]\n"
                                                        + "Tu Vi Tiếp Theo: [ " + player.TamkjllTuviTutien(Util.maxInt(player.Tamkjlltutien[1]) + 1) + " ]\n"
                                                        + "Thiên Phú Dị Bẩm: [ " + player.Tamkjlltutien[2] + " ] Sao              \n"
                                                        + "Kinh Nghiệm: [ " + Util.getFormatNumber(player.Tamkjlltutien[0]) + " ] EXP  \n",
                                                        "Thông Tin\nCảnh Giới",
                                                        "Luyện Hóa\nThiên Phú",
                                                        "Thông Tin\nChỉ Số",
                                                        "Thiên Đạo\nĐộ Kiếp",
                                                        "Độ Kiếp \nTu Vi"
                                                //"Tôi Luyện\nThánh Thể"
                                                );
                                                break;

                                            case 1:
                                                if (player.Tamkjlltutien[2] < 1) {
                                                    if (player.ExpTamkjll < 3000000) {
                                                        Service.getInstance().sendThongBaoFromAdmin(player, "|8|\nChào Mừng Chủ Nhân Thiên Giới\nYêu Cầu Chủ Nhân Cần 3M EXP Tb Tẩy Luyện\nKiếm Từ Việc Up quái");
                                                        return;
                                                    }
                                                    player.ExpTamkjll -= 3000000;
                                                    int tp = Util.nextInt(1, Util.nextInt(1, Util.nextInt(1, 2)));
                                                    player.Tamkjlltutien[2] = tp;
                                                    Service.getInstance().sendThongBaoAllPlayer(
                                                            "Chúc Mừng Đạo Hữu : " + player.name + " Đã Tẩy Luyện Mở Ra Thiên Phú\n" + tp + " Sao");
                                                } else {
                                                    if (player.CapTamkjll < 50) {
                                                        Service.getInstance().sendThongBao(player,
                                                                "Muốn tẩy thiên phú Tiên Bang của con ít nhất phải 50.");
                                                        return;
                                                    }
                                                    if (player.ExpTamkjll < 3000000) {
                                                        Service.getInstance().sendThongBaoFromAdmin(player, "|8|\nChào Mừng Chủ Nhân Thiên Giới\nYêu Cầu Chủ Nhân Cần 3M EXP TB Tẩy Luyện\nKiếm Từ Việc Up quái");
                                                        return;
                                                    }
                                                    if (player.Tamkjlltutien[2] >= 50) {
                                                        Service.getInstance().sendThongBaoFromAdmin(player, "Bạn Đã Là Cao Thủ Rồi");
                                                        return;
                                                    }
                                                    player.ExpTamkjll -= 3000000;
                                                    if (Util.isTrue(5f, 100)) {
                                                        player.Tamkjlltutien[2]++;
                                                        Service.getInstance().sendThongBaoAllPlayer(
                                                                "\n|6|Chúc Mừng Bạn :\n [" + player.name + "]\n Đã Tẩy Luyện Thiên Phú Từ "
                                                                + (player.Tamkjlltutien[2] - 1) + " sao lên: "
                                                                + player.Tamkjlltutien[2] + " sao.");
                                                    } else {
                                                        Service.getInstance().sendThongBaoFromAdmin(player, "Tẩy Luyện Thiên Phú Thất Bại.\nBạn Bị Trừ 3M EXP TB");
                                                    }
                                                }
                                                break;
                                            case 2:
                                                this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                                        "Cảnh giới: "
                                                        + player.TamkjllTuviTutien(Util.maxInt(player.Tamkjlltutien[1]))
                                                        + "\nHp: "
                                                        + player.TamkjllHpKiGiaptutien(
                                                                Util.maxInt(player.Tamkjlltutien[1]))
                                                        + "%\nKi: "
                                                        + player.TamkjllHpKiGiaptutien(
                                                                Util.maxInt(player.Tamkjlltutien[1]))
                                                        + "%\nGiáp: "
                                                        + player.TamkjllHpKiGiaptutien(
                                                                Util.maxInt(player.Tamkjlltutien[1]))
                                                        + "%\nDame: "
                                                        + player.TamkjllDametutien(Util.maxInt(player.Tamkjlltutien[1]))
                                                        + "%\nSTCM: "
                                                        + player.TamkjllDametutienSTCM(Util.maxInt(player.Tamkjlltutien[1]))
                                                        + "%\nTiềm Năng: "
                                                        + player.TamkjllDametutientnsm(Util.maxInt(player.Tamkjlltutien[1]))
                                                        + "%\n",
                                                        "Thông Tin\nCảnh Giới",
                                                        "Luyện Hóa\nThiên Phú",
                                                        "Thông Tin\nChỉ Số",
                                                        "Thiên Đạo\nĐộ Kiếp",
                                                        "Độ Kiếp \nTu Vi"
                                                //"Tôi Luyện\nThánh Thể"
                                                );
                                                break;
                                            case 3:
                                                if (player.getSession().vnd < 10000) {
                                                    Service.getInstance().sendThongBaoFromAdmin(player, "|6|Chào Mừng Chủ Nhân Thiên Giới\nYêu Cầu Chủ Nhân Cần 10k vnd Để Độ Kiếp Lên 1 Cảnh Giới\n100% Thành Công");
                                                    return;
                                                }
                                                if (player.Tamkjlltutien[1] >= 340) {
                                                    Service.getInstance().sendThongBaoFromAdmin(player, "Bạn Đã Là Cao Thủ Rồi");
                                                    return;
                                                }
                                                PlayerDAO.subvnd(player, 10000);
                                                if (Util.isTrue(100f, 100)) {
                                                    player.Tamkjlltutien[1]++;
                                                    Service.getInstance().sendThongBaoAllPlayer(
                                                            "|8|Chúc Mừng Bạn :\n [" + player.name + "]\n Đã Được Thiên Đạo Độ Kiếp\n "
                                                            + " Từ Cảnh Giới " + player.TamkjllTuviTutien(Util.maxInt(player.Tamkjlltutien[1]) - 1) + " lên: "
                                                            + player.TamkjllTuviTutien(Util.maxInt(player.Tamkjlltutien[1])) + " !");
                                                } else {
                                                    Service.getInstance().sendThongBaoFromAdmin(player, "Độ Kiếp Thất Bại");

                                                }
                                                break;

                                            case 4:
                                                if (player.Tamkjlltutien[0] < 500000000) {
                                                    Service.getInstance().sendThongBaoFromAdmin(player, "|6|Chào Mừng Chủ Nhân Thiên Giới\nYêu Cầu Chủ Nhân Cần 500M EXP Để Độ Kiếp Lên 1 Cảnh Giới\n30% Thành Công");
                                                    return;
                                                }
                                                if (player.Tamkjlltutien[1] >= 340) {
                                                    Service.getInstance().sendThongBaoFromAdmin(player, "Bạm Đã Là Cao Thủ Rồi");
                                                    return;
                                                }
                                                player.Tamkjlltutien[0] -= 500000000;
                                                if (Util.isTrue(20f, 100)) {
                                                    player.Tamkjlltutien[1]++;
                                                    Service.getInstance().sendThongBaoAllPlayer(
                                                            "|8|Chúc Mừng Bạn :\n [" + player.name + "]\n Đã Được Độ Kiếp\n "
                                                            + " Từ Cảnh Giới " + player.TamkjllTuviTutien(Util.maxInt(player.Tamkjlltutien[1]) - 1) + " lên: "
                                                            + player.TamkjllTuviTutien(Util.maxInt(player.Tamkjlltutien[1])) + " !");
                                                } else {
                                                    Service.getInstance().sendThongBaoFromAdmin(player, "Độ Kiếp Thất Bại \nLần Sau Cố Gắng Hơn");

                                                }
                                                break;

//                                            case 5:
//
//                                                NpcService.gI().createMenuConMeo(player, ConstNpc.debinh, -1,
//                                                        "Chúc Bạn Một Ngày Vui Vẻ\n"
//                                                        + "Thái Âm Thánh Thể: " + player.TamkjllDauLaDaiLuc[0] + " Sở Hữu"
//                                                        + "\nBất Diệt Thánh Thể: " + player.TamkjllDauLaDaiLuc[1] + " Sở Hữu "
//                                                        + "\nVô Địch Thánh Thể: " + player.TamkjllDauLaDaiLuc[2] + " Sở Hữu "
//                                                        + "\nMa Đạo Thánh Thể: " + player.TamkjllDauLaDaiLuc[3] + " Sở Hữu "
//                                                        + "\nNgộ Đạo Thánh Thể: " + player.TamkjllDauLaDaiLuc[4] + " Sở Hữu "
//                                                        + "\nĐại Đạo Thần Thể: " + player.TamkjllDauLaDaiLuc[5] + " Sở Hữu "
//                                                        + "\nHỗn Độn Thần Thể: " + player.TamkjllDauLaDaiLuc[6] + " Sở Hữu ",
//                                                        "Check Tại\nThần \nHủy diệt");
//
//                                                break;
                                        }

                                    }
                                }
                            }
                        }
                    };
                    break;

                case ConstNpc.check:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 14 || this.mapId == 0 || this.mapId == 7) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "|7|HELLO BẠN IU\n"
                                            + "|6|" + "Nhớ Nạp Nhiều Nhiều Cho Trường Nha\n"
                                            + "|7|" + "Nhập Ma: " + Util.cap(player.LbTamkjll) + " Bậc\n"
                                            + "|5|" + "EXP Nhập Ma: " + Util.cap(player.DLbTamkjll) + " Exp\n"
                                            + "|7|" + "Tiên Bang: " + Util.cap(player.CapTamkjll) + " Bậc\n"
                                            + "|5|" + "Exp Tiên Bang  : " + Util.cap(player.ExpTamkjll) + " Exp\n",
                                            "Hiệu ứng\nTiên Bang", "Hiệu Ứng\n Chuyển sinh", "Hiệu Ứng\nNhập Ma", "Hiệu Ứng\nTu Tiên", "Hiệu Ứng\nSố Dư", "Từ chối");

                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 14 || this.mapId == 0 || this.mapId == 7) {
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {
                                            case 0:
                                                NpcService.gI().createMenuConMeo(player, ConstNpc.Tamkjllmenu, 31808,
                                                        "|7|Chúc Bạn Một Ngày Vui Vẻ\n"
                                                        + "|4|\nUP Chung Clan Để Nhận Điểm EXP Đủ 100% Sẽ +1 Cấp\n"
                                                        + "|4|AE ấn Xem Hiệu ứng Cơ Bản Để Xem Chỉ Số Tăng",
                                                        "Sát Thương\n Vừa Đấm", "Hiệu ứng \nTiên Bang",
                                                        "Thông Tin Exp"
                                                );

                                                break;
                                            case 1:
                                                NpcService.gI().createMenuConMeo(player, ConstNpc.TamkjllCS, 31808,
                                                        "|4|Chuyển Sinh : " + Util.cap(player.TamkjllCS) + " Cấp\n"
                                                        + "|7|Sức Mạnh Yêu Cầu : " + Util.cap(player.nPoint.power) + " /500k Tỉ Sức Mạnh\n"
                                                        + "Điểm Farm Cần : " + Util.cap(player.diemfam) + " /5000k\n"
                                                        //  + "|5|" + "Cần Tối Thiểu 0 Cấp Tiên Bang Để Chuyển Sinh\n"
                                                        + "Chuyển Sinh :Yêu Cầu Sức Mạnh 5000k Tỉ + 200.000 Điểm Farm\n"
                                                        + "+10k SDG + 50K HP KIG + 100 GIÁP + 10% HP, KI, SD /1 Cấp",
                                                        "Chuyển Sinh\nSư Phụ", "Chuyển Sinh\nĐệ",
                                                        "Đóng"
                                                );

                                                break;
                                            case 2:
                                                NpcService.gI().createMenuConMeo(player, ConstNpc.huongdan, 31808,
                                                        "|5|\n" + "Nhập Ma : " + Util.cap(player.LbTamkjll) + " Cấp\n"
                                                        + "EXP Nhập Ma : " + Util.cap(player.DLbTamkjll) + " /EXP\n"
                                                        + "|7|" + "Tiến Trình:"
                                                        + (player.DLbTamkjll * 100 / (3000000L + player.LbTamkjll * 200000L))
                                                        + "%:  \n"
                                                        + "|5|" + "Chỉ Cần Di Chuyển Sẽ Được EXP Nhập Ma\n"
                                                        + "Khi Đạt 100% Tiến Trình Sẽ Lên 10K SD/Cấp \n",
                                                        "Đóng"
                                                );

                                                break;
                                            case 3:
                                                NpcService.gI().createMenuConMeo(player, ConstNpc.huongdan, 31808,
                                                        "|5|" + "Cảnh giới : " + player.TamkjllTuviTutien(Util.maxInt(player.Tamkjlltutien[1])) + " \n"
                                                        + "|7|" + "EXP Tu Tiên : " + Util.getFormatNumber(player.Tamkjlltutien[0]) + " EXP\n"
                                                        + "|7|" + "Thiên Phú:" + player.Tamkjlltutien[2] + " Sao\n"
                                                        + "|5|" + "Điều Kiện Tu Tiên Cần Đẹp Trai\n",
                                                        "Đóng"
                                                );

                                                break;
                                            case 4:
                                                NpcService.gI().createMenuConMeo(player, ConstNpc.huongdan, 31808,
                                                        "|5|\b" + "Số Dư  : " + Util.numberToMoney(player.getSession().vnd) + " Vnđ\n"
                                                        + "Số Dư  : " + Util.numberToMoney(player.getSession().tongnap) + " Tích Lũy\n"
                                                        + "ID : " + (player.getSession().userId) + " Tài Khoản\n",
                                                        "Đóng"
                                                );

                                                break;

//                                                 "Cảnh giới: "
//                                                        + player.TamkjllTuviTutien(Util.maxInt(player.Tamkjlltutien[1]))
//                                                        + "\nExp Tu tiên: " + Util.getFormatNumber(player.Tamkjlltutien[0])
//                                                        + "\nThiên phú: " + player.Tamkjlltutien[2] + " Sao"
//                                                        + "\n" + dktt,
                                        }

                                    }
                                }
                            }
                        }
                    };
                    break;

                case ConstNpc.vados:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 5) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "|7|Cư dân tìm khúc mía và nước đá đến xe nước mía ở đầu làng để mua ly nước mía.\n"
                                            + "- Công thức: 999 Cục đá + 500 khúc mía + 5000tỷ vàng.\n"
                                            + "Úp cục đá tại all quái tỉ lệ 1%\n"
                                            + "cây mía đổi hoặc mua tại shop\n"
                                            + "Uống mỗi loại cốc nước mía sẽ Được tăng chỉ số 10p\n"
                                            + "Chúc Bạn Chơi Game Vui Vẻ\n",
                                            "Đổi Nước Mía", "Shop Đổi", "Từ chối");
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 5) {
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {
                                            case 0:
                                                Item nuocda = null;
                                                Item khucmia = null;

                                                try {
                                                    nuocda = InventoryService.gI().findItemBagByTemp(player, 1490);
                                                    khucmia = InventoryService.gI().findItemBagByTemp(player, 1491);
                                                } catch (Exception e) {
                                                }
                                                if (nuocda == null || nuocda.quantity < 999 && khucmia == null || khucmia.quantity < 500 && player.inventory.gold >= 5000000000000l) {
                                                    this.npcChat(player, "Bạn Không Có Vật Phẩm Nào");
                                                } else if (InventoryService.gI().getCountEmptyBag(player) == 0) {
                                                    this.npcChat(player, "Hành Trang Của Bạn Không Đủ Chỗ Trống");
                                                } else if (player.inventory.gold < 5000000000000l) {
                                                    Service.getInstance().sendThongBaoFromAdmin(player, "Bạn còn thiều " + (5000000000000l - player.inventory.gold) + " Vàng");
                                                    break;
                                                } else {
                                                    InventoryService.gI().subQuantityItemsBag(player, nuocda, 999);
                                                    InventoryService.gI().subQuantityItemsBag(player, khucmia, 500);
                                                    player.inventory.gold -= 5000000000000l;
                                                    Item lynuocmia = ItemService.gI().createNewItem((short) Util.nextInt(1484, 1489));
                                                    lynuocmia.itemOptions.add(new ItemOption(174, 2024));
                                                    InventoryService.gI().addItemBag(player, lynuocmia, 99999);
                                                    InventoryService.gI().sendItemBags(player);
                                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + lynuocmia.template.name);
                                                }
                                                break;
//                                            case 1:
//                                                ShopService.gI().openShopSpecial(player, this, ConstNpc.xemia, 0, 3);
//                                                break;

                                            case 1:
                                                ShopService.gI().openShopSpecial(player, this, ConstNpc.xemia1, 1, 3);
                                                break;
                                        }

                                    }
                                }
                            }
                        }
                    };
                    break;

                case ConstNpc.truonglao:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 206 || this.mapId == 5) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "|7|Chúc Bạn Một Ngày Vui Vẻ\n"
                                            + "|5|Chỗ Em Có Bán Ít Đệ Tử Vip Hợp Thể Từ 1000%.\n"
                                            + "ĐỆ TỬ BÁT GIỚI 1000%\n"
                                            + "ĐỆ TỬ HẰNG NGA 2000%\n"
                                            + "ĐỆ TỬ GOKU Ul 3000%"
                                            + "ĐỆ TỬ VEGITO 4000%\n"
                                            + "ĐỆ TỬ VEGITO Ul 5000%\n"
                                            + "Chúc Bạn Chơi Game Vui Vẻ\n",
                                            "Xúc Ngay", "Từ chối");
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 206 || this.mapId == 5) {
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {
                                            case 0:
                                                NpcService.gI().createMenuConMeo(player, ConstNpc.DOI_DTVIP, -1,
                                                        "|7|Chúc Bạn Một Ngày Vui Vẻ\n"
                                                        + "|5|Chỗ Em Có Bán Ít Đệ Tử Vip Hợp Thể Từ 1000%.\n"
                                                        + "ĐỆ TỬ BÁT GIỚI 1000%\n"
                                                        + "ĐỆ TỬ HẰNG NGA 2000%\n"
                                                        + "ĐỆ TỬ GOKU Ul 3000%\n"
                                                        + "ĐỆ TỬ VEGITO 4000%\n"
                                                        + "ĐỆ TỬ VEGITO Ul 5000%\n"
                                                        + "Chúc Bạn Chơi Game Vui Vẻ\n",
                                                        "BÁT GIỚI\n100K", "HẰNG NGA\n200K", "GOKU UNTRAL\n300K", "VEGITO\n500K", "VEGITO UNTRA\n1000K"
                                                );

                                                break;

                                        }

                                    }
                                }
                            }
                        }
                    };
                    break;

                case ConstNpc.BUNMA:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                            "Cậu cần trang bị gì cứ đến chỗ tôi nhé", "Cửa\nhàng");
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (player.iDMark.isBaseMenu()) {
                                    switch (select) {
                                        case 0:// Shop
                                            if (player.gender == ConstPlayer.TRAI_DAT) {
                                                this.openShopWithGender(player, ConstNpc.SHOP_BUNMA_QK_0, 0);
                                            } else {
                                                this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                                        "Xin lỗi cưng, chị chỉ bán đồ cho người Trái Đất", "Đóng");
                                            }
                                            break;
                                        case 1:
                                            this.createOtherMenu(player, ConstNpc.SUKIEN8thang3,
                                                    "Cậu muốn tặng hoa cho tôi sao", "Tặng x1\nbông hoa", "Tặng x99\nbông hoa");
                                            break;
                                        case 2:
                                            this.createOtherMenu(player, ConstNpc.Vongquay8thang3,
                                                    "Vòng quay 8/3 có tỉ lệ ra vật phẩm ,\n cải trang hiếm có tỉ lệ vĩnh viễn cao", "Quay x1\n Tốn 5k", "Quay X10\n Tốn 40k ngọc");
                                            break;
                                    }
                                } else if (player.iDMark.getIndexMenu() == ConstNpc.Vongquay8thang3) {
                                    int[] listitem = new int[]{1144, 1040, 1131};
                                    int[] listItemReward = Util.pickNRandInArr(listitem, 1);
                                    int randomItem = listItemReward[0];
                                    Item reward = Util.reward8thang3(randomItem);
                                    switch (select) {
                                        case 0:
                                            if (InventoryService.gI().getCountEmptyBag(player) >= 1) {
                                                if (player.inventory.ruby >= 5000) {
                                                    player.inventory.ruby -= 5000;
                                                    if (Util.isTrue(30, 100)) {
                                                        InventoryService.gI().addItemBag(player, reward, 9999);
                                                        InventoryService.gI().sendItemBags(player);
                                                        Service.getInstance().sendMoney(player);
                                                        this.createOtherMenu(player, ConstNpc.Vongquay8thang3,
                                                                "Bạn đã  quay ra quà x1 ", "Quay x1\n Tốn 5k", "Quay X10\n Tốn 40k ngọc");
                                                    } else {
                                                        this.createOtherMenu(player, ConstNpc.Vongquay8thang3,
                                                                "Chúc bạn may mắn lần sau", "Quay x1\n Tốn 5k", "Quay X10\n Tốn 40k ngọc");

                                                    }
                                                }
                                            } else {
                                                this.npcChat(player, "Phải thừa ít nhất 1 ô hành trang");
                                            }
                                            break;
                                        case 1:
                                            if (InventoryService.gI().getCountEmptyBag(player) >= 10) {
                                                if (player.inventory.ruby >= 5000 * 10) {
                                                    int solan = 0;
                                                    int quay = 10;
                                                    while (quay > 0) {
                                                        try {
                                                            Thread.sleep(50);
                                                            quay--;
                                                            solan++;
                                                            player.inventory.ruby -= 5000;
                                                            if (Util.isTrue(30, 100)) {
                                                                InventoryService.gI().addItemBag(player, reward, 9999);
                                                                InventoryService.gI().sendItemBags(player);
                                                                Service.getInstance().sendMoney(player);
                                                                this.createOtherMenu(player, ConstNpc.Vongquay8thang3,
                                                                        "Bạn đã quay ra quà x1 "
                                                                        + "\n|7|Đã quay " + solan + " lần", "Quay x1\n Tốn 5k ngọc", "Quay X10\n Tốn 40k ngọc");

                                                            } else {
                                                                this.createOtherMenu(player, ConstNpc.Vongquay8thang3,
                                                                        "Chúc bạn may mắn lần sau"
                                                                        + "\n|7|Đã quay " + solan + " lần", "Quay x1\n Tốn 5k", "Quay X10\n Tốn 40k ngọc");

                                                            }

                                                        } catch (InterruptedException ex) {
                                                            Logger.getLogger(NpcFactory.class.getName()).log(Level.SEVERE, null, ex);
                                                        }
                                                    }
                                                }
                                            } else {
                                                this.npcChat(player, "Phải thừa ít nhất 10 ô hành trang");
                                            }
                                            break;

                                    }
                                } else if (player.iDMark.getIndexMenu() == ConstNpc.SUKIEN8thang3) {
                                    Item hoahong = InventoryService.gI().findItemBagByTemp(player, 589);
                                    switch (select) {
                                        case 0:
                                            if (hoahong != null && hoahong.quantity >= 1) {
                                                if (Util.isTrue(50, 100)) {
                                                    int randomngoc = Util.nextInt(1, 20);
                                                    player.inventory.ruby += randomngoc;
                                                    Service.getInstance().sendMoney(player);
                                                    Service.getInstance().sendThongBao(player, "Bạn Đã Nhận Được " + randomngoc + " ngọc hồng");
                                                } else {
                                                    if (player.pet != null) {
                                                        int randomtnsm = Util.nextInt(10000, 2000000);
                                                        Service.getInstance().addSMTN(player.pet, (byte) 2, randomtnsm, false);
                                                        Service.getInstance().sendThongBao(player, "Bạn Đã Nhận Được cộng " + randomtnsm + " tnsm cho đệ tử");
                                                    } else {
                                                        int randomtnsm = Util.nextInt(10000, 2000000);
                                                        Service.getInstance().addSMTN(player, (byte) 2, randomtnsm, false);
                                                        Service.getInstance().sendThongBao(player, "Bạn Đã Nhận Được cộng " + randomtnsm + " tnsm cho đệ tử");
                                                    }
                                                }
                                                InventoryService.gI().subQuantityItemsBag(player, hoahong, 1);
                                                InventoryService.gI().sendItemBags(player);
                                                player.pointsukien++;
                                            } else {
                                                this.npcChat("Anh không đủ hoa hồng ư chán qué");
                                            }
                                            break;
                                        case 1:
                                            if (hoahong != null && hoahong.quantity >= 99) {
                                                InventoryService.gI().subQuantityItemsBag(player, hoahong, 99);
                                                Item capsulepink = ItemService.gI().createNewItem((short) 722, 1);
                                                InventoryService.gI().addItemBag(player, capsulepink, 9999);

                                                InventoryService.gI().sendItemBags(player);
                                                player.pointsukien += 100;
                                                this.npcChat("Em cảm ơn, tặng lại anh capsule hồng nè ");
                                                Service.getInstance().sendThongBao(player, "Bạn Đã Nhận Được 100 điểm sự kiện");
                                            } else {
                                                this.npcChat("Anh không đủ hoa hồng ư chán qué");
                                            }
                                            break;

                                    }
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.DENDE:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                                    if (player.isHoldNamecBall) {
                                        this.createOtherMenu(player, ConstNpc.ORTHER_MENU,
                                                "Ô, ngọc rồng Namek, anh thật may mắn, nếu tìm đủ 7 viên ngọc có thể triệu hồi Rồng Thần Namek,",
                                                "Gọi rồng", "Từ chối");
                                    } else {
                                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                                "Anh cần trang bị gì cứ đến chỗ em nhé", "Cửa\nhàng");
                                    }
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (player.iDMark.isBaseMenu()) {
                                    switch (select) {
                                        case 0:// Shop
                                            if (player.gender == ConstPlayer.NAMEC) {
                                                this.openShopWithGender(player, ConstNpc.SHOP_DENDE_0, 0);
                                            } else {
                                                this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                                        "Xin lỗi anh, em chỉ bán đồ cho dân tộc Namếc", "Đóng");
                                            }
                                            break;
                                    }
                                } else if (player.iDMark.getIndexMenu() == ConstNpc.ORTHER_MENU) {
                                    NamekBallWar.gI().summonDragon(player, this);
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.APPULE:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                            "Ngươi cần trang bị gì cứ đến chỗ ta nhé", "Cửa\nhàng");
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (player.iDMark.isBaseMenu()) {
                                    switch (select) {
                                        case 0:// Shop
                                            if (player.gender == ConstPlayer.XAYDA) {
                                                this.openShopWithGender(player, ConstNpc.SHOP_APPULE_0, 0);
                                            } else {
                                                this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                                        "Về hành tinh hạ đẳng của ngươi mà mua đồ cùi nhé. Tại đây ta chỉ bán đồ cho người Xayda thôi",
                                                        "Đóng");
                                            }
                                            break;
                                    }
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.DR_DRIEF:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player pl) {
                            if (canOpenNpc(pl)) {
                                if (this.mapId == 84) {
                                    this.createOtherMenu(pl, ConstNpc.BASE_MENU,
                                            "Tàu Vũ Trụ của ta có thể đưa cậu đến hành tinh khác chỉ trong 3 giây. Cậu muốn đi đâu?",
                                            pl.gender == ConstPlayer.TRAI_DAT ? "Đến\nTrái Đất"
                                                    : pl.gender == ConstPlayer.NAMEC ? "Đến\nNamếc" : "Đến\nXayda");
                                } else if (this.mapId == 204) {
                                    Clan clan = pl.clan;
                                    ClanMember cm = pl.clanMember;
                                    if (cm.role == Clan.LEADER) {
                                        this.createOtherMenu(pl, ConstNpc.BASE_MENU,
                                                "|7|Chúc Bạn Một Ngày Vui Vẻ\n"
                                                + "|5|Capsule Bang Đang Có: " + clan.clanPoint
                                                + " \nđể nâng cấp bang hội lên cấp\n "
                                                + (clan.level++) + " LV\n"
                                                + "+1 tối đa số lượng thành viên",
                                                "Về\nĐảoKame", "Góp " + cm.memberPoint + " capsule", "Nâng cấp",
                                                "Từ chối");

                                    } else {
                                        this.createOtherMenu(pl, ConstNpc.BASE_MENU, "Bạn đang có " + cm.memberPoint
                                                + " capsule bang,bạn có muốn đóng góp toàn bộ cho bang hội của mình không ?",
                                                "Về\nĐảoKame", "Đồng ý", "Từ chối");
                                    }
                                } else if (!TaskService.gI().checkDoneTaskTalkNpc(pl, this)) {
                                    if (pl.playerTask.taskMain.id == 7) {
                                        NpcService.gI().createTutorial(pl, this.avartar,
                                                "Hãy lên đường cứu đứa bé nhà tôi\n"
                                                + "Chắc bây giờ nó đang sợ hãi lắm rồi");
                                    } else {
                                        this.createOtherMenu(pl, ConstNpc.BASE_MENU,
                                                "Tàu Vũ Trụ của ta có thể đưa cậu đến hành tinh khác chỉ trong 3 giây. Cậu muốn đi đâu?",
                                                "Đến\nNamếc", "Đến\nXayda", "Siêu thị");
                                    }
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 84) {
                                    ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 24, -1, -1);
                                } else if (mapId == 204) {
                                    if (select == 0) {
                                        ChangeMapService.gI().changeMap(player, ConstMap.DAO_KAME, -1, 1059, 408);
                                        return;
                                    }
                                    Clan clan = player.clan;
                                    ClanMember cm = player.clanMember;
                                    if (select == 1) {
                                        player.clan.clanPoint += cm.memberPoint;
                                        cm.clanPoint += cm.memberPoint;
                                        cm.memberPoint = 0;
                                        Service.getInstance().sendThongBao(player, "Đóng góp thành công");
                                    } else if (select == 2 && cm.role == Clan.LEADER) {
                                        if (clan.level >= 5) {
                                            Service.getInstance().sendThongBao(player,
                                                    "Bang hội của bạn đã đạt cấp tối đa");
                                            return;
                                        }
                                        if (clan.clanPoint < 1000) {
                                            Service.getInstance().sendThongBao(player, "Không đủ capsule");
                                            return;
                                        }
                                        clan.level++;
                                        clan.maxMember++;
                                        clan.clanPoint -= 1000;
                                        Service.getInstance().sendThongBao(player,
                                                "Bang hội của bạn đã được nâng cấp lên cấp " + clan.level);
                                    }
                                } else if (player.iDMark.isBaseMenu()) {
                                    switch (select) {
                                        case 0:
                                            ChangeMapService.gI().changeMapBySpaceShip(player, 25, -1, -1);
                                            break;
                                        case 1:
                                            ChangeMapService.gI().changeMapBySpaceShip(player, 26, -1, -1);
                                            break;
                                        case 2:
                                            ChangeMapService.gI().changeMapBySpaceShip(player, 84, -1, -1);
                                            break;
                                    }
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.CARGO:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player pl) {
                            if (canOpenNpc(pl)) {
                                if (!TaskService.gI().checkDoneTaskTalkNpc(pl, this)) {
                                    if (pl.playerTask.taskMain.id == 7) {
                                        NpcService.gI().createTutorial(pl, this.avartar,
                                                "Hãy lên đường cứu đứa bé nhà tôi\n"
                                                + "Chắc bây giờ nó đang sợ hãi lắm rồi");
                                    } else {
                                        this.createOtherMenu(pl, ConstNpc.BASE_MENU,
                                                "Tàu Vũ Trụ của ta có thể đưa cậu đến hành tinh khác chỉ trong 3 giây. Cậu muốn đi đâu?",
                                                "Đến\nTrái Đất", "Đến\nXayda", "Siêu thị");
                                    }
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (player.iDMark.isBaseMenu()) {
                                    switch (select) {
                                        case 0:
                                            ChangeMapService.gI().changeMapBySpaceShip(player, 24, -1, -1);
                                            break;
                                        case 1:
                                            ChangeMapService.gI().changeMapBySpaceShip(player, 26, -1, -1);
                                            break;
                                        case 2:
                                            ChangeMapService.gI().changeMapBySpaceShip(player, 84, -1, -1);
                                            break;
                                    }
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.DUA_HAU:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player pl) {
                            pl.duahau.sendduahau();
                            if (canOpenNpc(pl)) {
                                if (pl.duahau.getSecondDone() != 0) {
                                    this.createOtherMenu(pl, ConstNpc.CAN_NOT_OPEN_DUA, "Thu hoạch dưa hấu nhận 15000 Hồng ngọc",
                                            "Hủy bỏ\nDưa hấu", "Đóng");
                                } else {
                                    this.createOtherMenu(pl, ConstNpc.CAN_OPEN_DUA, "Dưa chín rồi nè", "Thu hoạch", "Hủy bỏ\nDưa hấu", "Đóng");
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                switch (player.iDMark.getIndexMenu()) {
                                    case ConstNpc.CAN_NOT_OPEN_DUA:
                                        if (select == 0) {
                                            this.createOtherMenu(player, ConstNpc.CONFIRM_DESTROY_DUA,
                                                    "Bạn có chắc chắn muốn hủy bỏ Dưa hấu?", "Đồng ý", "Từ chối");
                                        }
                                        break;
                                    case ConstNpc.CAN_OPEN_DUA:
                                        switch (select) {
                                            case 0:
                                                this.createOtherMenu(player, ConstNpc.CONFIRM_OPEN_DUA,
                                                        "Bạn có chắc chắn THU HOẠCH DƯA?\n"
                                                        + "Sẽ nhận được 15000 hồng ngọc",
                                                        "Thu hoạch");
                                                break;
                                            case 1:
                                                this.createOtherMenu(player, ConstNpc.CONFIRM_DESTROY_DUA,
                                                        "Bạn có chắc chắn muốn hủy bỏ dưa hấu?", "Đồng ý", "Từ chối");
                                                break;
                                        }
                                        break;
                                    case ConstNpc.CONFIRM_OPEN_DUA:
                                        switch (select) {
                                            case 0:
                                                player.duahau.openDuaHau();
                                                break;

                                        }
                                    case ConstNpc.CONFIRM_DESTROY_DUA:
                                        if (select == 0) {
                                            if (player.duahau != null) {
                                                player.duahau.destroyDuaHau();
                                            }
                                        }
                                        break;
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.HUNG_VUONG:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player pl) {
                            if (canOpenNpc(pl)) {
                                this.createOtherMenu(pl, ConstNpc.BASE_MENU,
                                        "|7|Hiện tại đang diễn ra sự kiện 10 tháng 3 ?"
                                        + "\n|2|Người hãy chọn các lưa chọn dưới đây "
                                        + "\n|3|Điểm sự kiện hiện tại: " + pl.pointsukien,
                                        "Quy đổi", "Trồng dưa\nDưa hấu", "Shop Vua\nHùng", "Từ chối");
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                Item item1 = InventoryService.gI().findItemBagByTemp(player, 1220);
                                Item item2 = InventoryService.gI().findItemBagByTemp(player, 1221);
                                Item item3 = InventoryService.gI().findItemBagByTemp(player, 1222);
                                //trồng cây
                                Item item4 = InventoryService.gI().findItemBagByTemp(player, 1093);
                                Item item5 = InventoryService.gI().findItemBagByTemp(player, 1095);
                                if (player.iDMark.isBaseMenu()) {
                                    switch (select) {
                                        case 0:
                                            this.createOtherMenu(player, 1, "Quy đổi bạn sẽ dùng các vật phẩm sau để quy đổi"
                                                    + "\n|7|x99 Cựa gà , X99 Ngà voi , X99 Hồng Mao"
                                                    + "\n|7|Hãy chọn các quy đổi dưới đây",
                                                    "Cải trang", "Pet VIP", "Từ chối");
                                            break;
                                        case 1:
                                            this.createOtherMenu(player, 2,
                                                    "\n|7|Trong dưa hấu mỗi ngày sẽ nhận nhiều phần quà hấp "
                                                    + "\n|7| Tốn x20 hạt giống và x30 ô Đất trồng cây\n Để trồng dưa hấu",
                                                    "Đồng ý", "Từ chối");

                                            break;
                                        case 2:
                                            this.openShopWithGender(player, ConstNpc.SHOPSUKIENHUNGVUONG, 0);
                                            break;

                                    }
                                } else if (player.iDMark.getIndexMenu() == 1) {
                                    switch (select) {
                                        case 0:
                                            if (item1 != null
                                                    && item2 != null
                                                    && item3 != null
                                                    && item1.quantity >= 99
                                                    && item2.quantity >= 99
                                                    && item3.quantity >= 99) {
                                                Item minuong = InventoryService.gI().findItemBagByTemp(player, 860);
                                                minuong.itemOptions.add(new ItemOption(50, Util.nextInt(20, 40)));
                                                minuong.itemOptions.add(new ItemOption(77, Util.nextInt(20, 40)));
                                                minuong.itemOptions.add(new ItemOption(103, Util.nextInt(20, 40)));
                                                minuong.itemOptions.add(new ItemOption(117, Util.nextInt(10, 25)));
                                                minuong.itemOptions.add(new ItemOption(93, Util.nextInt(1, 3)));
                                                Service.getInstance().sendThongBao(player, "Bạn đã nhận đã cải trang mị nương");
                                                InventoryService.gI().subQuantityItemsBag(player, item1, 99);
                                                InventoryService.gI().subQuantityItemsBag(player, item2, 99);
                                                InventoryService.gI().subQuantityItemsBag(player, item3, 99);
                                                InventoryService.gI().sendItemBags(player);
                                            } else {
                                                Service.getInstance().sendThongBao(player, "Bạn không đủ vật phẩm sự kiện");
                                            }

                                            break;
                                        case 1:
                                            if (item1 != null
                                                    && item2 != null
                                                    && item3 != null
                                                    && item1.quantity >= 99
                                                    && item2.quantity >= 99
                                                    && item3.quantity >= 99) {
                                                Item minuong = InventoryService.gI().findItemBagByTemp(player, 860);
                                                minuong.itemOptions.add(new ItemOption(50, Util.nextInt(20, 40)));
                                                minuong.itemOptions.add(new ItemOption(77, Util.nextInt(20, 40)));
                                                minuong.itemOptions.add(new ItemOption(103, Util.nextInt(20, 40)));
                                                minuong.itemOptions.add(new ItemOption(117, Util.nextInt(10, 25)));
                                                minuong.itemOptions.add(new ItemOption(93, Util.nextInt(1, 3)));
                                                InventoryService.gI().subQuantityItemsBag(player, item1, 99);
                                                InventoryService.gI().subQuantityItemsBag(player, item2, 99);
                                                InventoryService.gI().subQuantityItemsBag(player, item3, 99);
                                                InventoryService.gI().sendItemBags(player);
                                                Service.getInstance().sendThongBao(player, "Bạn đã nhận đã cải trang mị nương");

                                            } else {
                                                Service.getInstance().sendThongBao(player, "Bạn không đủ vật phẩm sự kiện");
                                            }
                                            break;
                                    }
                                } else if (player.iDMark.getIndexMenu() == 2) {
                                    switch (select) {
                                        case 0:
                                            if (item4 != null && item5 != null && item4.quantity >= 20 && item5.quantity >= 30) {
                                                InventoryService.gI().subQuantityItemsBag(player, item4, 20);
                                                InventoryService.gI().subQuantityItemsBag(player, item5, 30);
                                                InventoryService.gI().sendItemBags(player);
                                                duahau.createduahau(player);
                                                player.pointsukien++;
                                                Service.getInstance().sendThongBao(player, "Trồng dưa hấu thành công");

                                            } else {
                                                Service.getInstance().sendThongBao(player, "Bạn không đủ vật phẩm sự kiện");
                                            }

                                            break;
                                        case 1:

                                            break;
                                    }
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.CUI:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {

                        private final int COST_FIND_BOSS = 20000000;

                        @Override
                        public void openBaseMenu(Player pl) {
                            if (canOpenNpc(pl)) {
                                if (!TaskService.gI().checkDoneTaskTalkNpc(pl, this)) {
                                    if (pl.playerTask.taskMain.id == 7) {
                                        NpcService.gI().createTutorial(pl, this.avartar,
                                                "Hãy lên đường cứu đứa bé nhà tôi\n"
                                                + "Chắc bây giờ nó đang sợ hãi lắm rồi");
                                    } else {
                                        switch (this.mapId) {
                                            case 19:
                                                int taskId = TaskService.gI().getIdTask(pl);
                                                switch (taskId) {
                                                    case ConstTask.TASK_1_0:
                                                        this.createOtherMenu(pl, ConstNpc.MENU_FIND_KUKU,
                                                                "Đội quân của Fide đang ở Thung lũng Nappa, ta sẽ đưa ngươi đến đó",
                                                                "Đến chỗ\nKuku\n(" + Util.numberToMoney(COST_FIND_BOSS)
                                                                + " vàng)",
                                                                "Đến Cold", "Đến\nNappa", "Từ chối");
                                                        break;
                                                    case ConstTask.TASK_1_1:
                                                        this.createOtherMenu(pl, ConstNpc.MENU_FIND_MAP_DAU_DINH,
                                                                "Đội quân của Fide đang ở Thung lũng Nappa, ta sẽ đưa ngươi đến đó",
                                                                "Đến chỗ\nMập đầu đinh\n("
                                                                + Util.numberToMoney(COST_FIND_BOSS) + " vàng)",
                                                                "Đến Cold", "Đến\nNappa", "Từ chối");
                                                        break;
                                                    case ConstTask.TASK_1_2:
                                                        this.createOtherMenu(pl, ConstNpc.MENU_FIND_RAMBO,
                                                                "Đội quân của Fide đang ở Thung lũng Nappa, ta sẽ đưa ngươi đến đó",
                                                                "Đến chỗ\nRambo\n(" + Util.numberToMoney(COST_FIND_BOSS)
                                                                + " vàng)",
                                                                "Đến Cold", "Đến\nNappa", "Từ chối");
                                                        break;
                                                    default:
                                                        this.createOtherMenu(pl, ConstNpc.BASE_MENU,
                                                                "Đội quân của Fide đang ở Thung lũng Nappa, ta sẽ đưa ngươi đến đó",
                                                                "Đến Cold", "Đến\nNappa", "Từ chối");

                                                        break;
                                                }
                                                break;
                                            case 68:
                                                this.createOtherMenu(pl, ConstNpc.BASE_MENU,
                                                        "Ngươi muốn về Thành Phố Vegeta", "Đồng ý", "Từ chối");
                                                break;
                                            default:
                                                this.createOtherMenu(pl, ConstNpc.BASE_MENU,
                                                        "Tàu vũ trụ Xayda sử dụng công nghệ mới nhất, "
                                                        + "có thể đưa ngươi đi bất kỳ đâu, chỉ cần trả tiền là được.",
                                                        "Đến\nTrái Đất", "Đến\nNamếc", "Siêu thị");
                                                break;
                                        }
                                    }
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 26) {
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {
                                            case 0:
                                                ChangeMapService.gI().changeMapBySpaceShip(player, 24, -1, -1);
                                                break;
                                            case 1:
                                                ChangeMapService.gI().changeMapBySpaceShip(player, 25, -1, -1);
                                                break;
                                            case 2:
                                                ChangeMapService.gI().changeMapBySpaceShip(player, 84, -1, -1);
                                                break;
                                        }
                                    }
                                }
                                if (this.mapId == 19) {
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {
                                            case 0:
                                                ChangeMapService.gI().changeMapBySpaceShip(player, 109, -1, 295);
                                                break;
                                            case 1:
                                                ChangeMapService.gI().changeMapBySpaceShip(player, 68, -1, 90);
                                                break;
                                        }
                                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_FIND_KUKU) {
                                        switch (select) {
                                            case 0:
                                                Boss boss = BossManager.gI().getBossById(BossFactory.test1);
                                                if (boss != null && !boss.isDie()) {
                                                    if (player.inventory.gold >= COST_FIND_BOSS) {
                                                        player.inventory.gold -= COST_FIND_BOSS;
                                                        ChangeMapService.gI().changeMap(player, boss.zone,
                                                                boss.location.x, boss.location.y);
                                                        Service.getInstance().sendMoney(player);
                                                    } else {
                                                        Service.getInstance().sendThongBao(player,
                                                                "Không đủ vàng, còn thiếu "
                                                                + Util.numberToMoney(
                                                                        COST_FIND_BOSS - player.inventory.gold)
                                                                + " vàng");
                                                    }
                                                }
                                                break;
                                            case 1:
                                                ChangeMapService.gI().changeMapBySpaceShip(player, 109, -1, 295);
                                                break;
                                            case 2:
                                                ChangeMapService.gI().changeMapBySpaceShip(player, 68, -1, 90);
                                                break;
                                        }
                                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_FIND_MAP_DAU_DINH) {
                                        switch (select) {
                                            case 0:
                                                Boss boss = BossManager.gI().getBossById(BossFactory.test1);
                                                if (boss != null && !boss.isDie()) {
                                                    if (player.inventory.gold >= COST_FIND_BOSS) {
                                                        player.inventory.gold -= COST_FIND_BOSS;
                                                        ChangeMapService.gI().changeMap(player, boss.zone,
                                                                boss.location.x, boss.location.y);
                                                        Service.getInstance().sendMoney(player);
                                                    } else {
                                                        Service.getInstance().sendThongBao(player,
                                                                "Không đủ vàng, còn thiếu "
                                                                + Util.numberToMoney(
                                                                        COST_FIND_BOSS - player.inventory.gold)
                                                                + " vàng");
                                                    }
                                                }
                                                break;
                                            case 1:
                                                ChangeMapService.gI().changeMapBySpaceShip(player, 109, -1, 295);
                                                break;
                                            case 2:
                                                ChangeMapService.gI().changeMapBySpaceShip(player, 68, -1, 90);
                                                break;
                                        }
                                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_FIND_RAMBO) {
                                        switch (select) {
                                            case 0:
                                                Boss boss = BossManager.gI().getBossById(BossFactory.test1);
                                                if (boss != null && !boss.isDie()) {
                                                    if (player.inventory.gold >= COST_FIND_BOSS) {
                                                        player.inventory.gold -= COST_FIND_BOSS;
                                                        ChangeMapService.gI().changeMap(player, boss.zone,
                                                                boss.location.x, boss.location.y);
                                                        Service.getInstance().sendMoney(player);
                                                    } else {
                                                        Service.getInstance().sendThongBao(player,
                                                                "Không đủ vàng, còn thiếu "
                                                                + Util.numberToMoney(
                                                                        COST_FIND_BOSS - player.inventory.gold)
                                                                + " vàng");
                                                    }
                                                }
                                                break;
                                            case 1:
                                                ChangeMapService.gI().changeMapBySpaceShip(player, 109, -1, 295);
                                                break;
                                            case 2:
                                                ChangeMapService.gI().changeMapBySpaceShip(player, 68, -1, 90);
                                                break;
                                        }
                                    }
                                }
                                if (this.mapId == 68) {
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {
                                            case 0:
                                                ChangeMapService.gI().changeMapBySpaceShip(player, 19, -1, 1100);
                                                break;
                                        }
                                    }
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.SANTA:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                        "|7|" + "Chào Bạn Chơi Game Vui Vẻ...\n"
                                        + "|6|" + "Nhớ Nạp Nhiều Nhiều Cho Trường Nha\n"
                                        + "|4|Tổng Nạp: " + Util.numberToMoney(player.getSession().tongnap) + " Tích Lũy\n"
                                        + "|5|Số Dư: " + Util.numberToMoney(player.getSession().vnd) + " Vnđ\n"
                                        + "|5|Điểm Săn Boss Của Bạn: " + Util.numberToMoney(player.point_sb) + " Điểm\n",
                                        "Shop\nCày Chay",
                                        "Shop\nPrivate", "Shop\nAvatar", "Shop\nĐổi SKH", "Shop\nCombo", "Shop\nThờiTrang");
                            }

                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 5 || this.mapId == 13 || this.mapId == 20) {
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {
//                                            case 0: // shop
//                                                this.openShop(player, ConstNpc.SHOP_SANTA_0, 0);
//                                                break;
                                            case 0: // tiệm hớt tóc
                                                ShopService.gI().openShopSpecial(player, this, ConstNpc.caycuoc, 4, 3);
                                                break;
                                            case 1:
                                                ShopService.gI().openShopSpecial(player, this, ConstNpc.skill9, 9, 3);
                                                break;

                                            case 2:
                                                ShopService.gI().openShopSpecial(player, this, ConstNpc.SHOPAVATA, 10, 3);
                                                break;

                                            case 3:
                                                ShopService.gI().openShopSpecial(player, this, ConstNpc.shopmanh, 11, 3);
                                                break;
                                            case 4:
                                                ShopService.gI().openShopSpecial(player, this, ConstNpc.shopcombo, 12, 3);
                                                break;

                                            case 5:
                                                if (player.CapTamkjll < 10000) {
                                                    Service.gI().sendThongBaoFromAdmin(player, "Shop chỉ dành cho admin vào tham khảo Item của Sever");
                                                    return;
                                                }

                                                ShopService.gI().openShopSpecial(player, this, ConstNpc.shopnap, 13, 3);
                                                break;
                                            case 6:

//                                                if (player.pointThap < 10000) {
//                                                    Service.getInstance().sendThongBao(player, "admin Đang Sửa");
//                                                    return;
//                                                }
                                                NpcService.gI().createMenuConMeo(player, ConstNpc.mocnap, -1,
                                                        "|7|Chúc Bạn Một Ngày Vui Vẻ\n"
                                                        + "|5|\n" + "MỐC NẠP TÍCH LŨY : ĐỦ ĐIỂM TÍCH LŨY NẠP AUTO NHẬN QUÀ MỐC\n"
                                                        + "MỖI MỐC TÍCH LŨY: NHẬN TỐI ĐA 5 ĐÁ KHẢM/1 MỐC\n"
                                                        + "LƯU Ý KHI NHẬN MỐC TRÁNH NGU NGƯỜI\n"
                                                        + "BẮT BUỘC PHẢI DÙNG HẾT ĐÁ MỐC CŨ MỚI ĐƯỢC NHẬN TIẾP\n"
                                                        + "CÓ THỂ VỨT ĐỂ LẤY ĐÁ Ở MỐC CAO HƠN DÙNG\n"
                                                        + "GỒM QUÀ: ĐÁ KHẢM SD, HP, KI, SD 600 VIÊN FULL 1 MỐC THA HỒ ÉP\n"
                                                        + "|7|" + "\nTÍCH LŨY: " + Util.cap(player.getSession().tongnap) + " ĐIỂM\n"
                                                        + "MỐC ĐÃ NHẬN: " + Util.cap(player.mocnap) + " ĐIỂM\n",
                                                        "NHẬN\n MỐC NẠP"
                                                );

                                                break;
                                        }
                                    }
                                }
                            }
                        }
                    };

                    break;
                case ConstNpc.URON:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player pl) {
                            if (canOpenNpc(pl)) {
                                this.openShopWithGender(pl, ConstNpc.SHOP_URON_0, 0);
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {

                            }
                        }
                    };
                    break;
                case ConstNpc.BA_HAT_MIT:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                switch (this.mapId) {
                                    case 5:
                                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Nhớ Nạp Nhiều Nhiều Cho Trường Nha",
                                                "Ép Khảm\ntrang bị", "Đục\nSao\ntrang bị", "Gỡ\nSao\nTrang Bị",
                                                "Mở chỉ Số\nCải Trang\nHiếm", "Nâng Cấp\nTrang Bị", "Gia Hạn\nTrang Bị",
                                                "Ghép\nCải trang","Võ Đài\nSinh Tử");
                                        break;
                                    case 121:
                                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ngươi tìm ta có việc gì?",
                                                "Về đảo\nrùa");
                                        break;
                                    case 112:
                                        if (player.DoneVoDaiBaHatMit == 1) {
                                            this.createOtherMenu(player, ConstNpc.NHAN_QUA_VO_DAI, "Đây là phẩn thưởng của con.", "3 Đan \nUp EXP\n bất kì", "5 key Tầm Bảo Bất Kì");
                                        } else {
                                            this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ngươi muốn đăng ký thi đấu võ đài?\n"
                                                    + "Nhiều phần thưởng giá trị đang đợi ngươi đó\n"
                                                    + "Cố gắng Win hết nhé\n"
                                                    + "Nhận ngẫu nhiên x3 Đan Up x1 -> 3\n"
                                                    + "Nhận ngẫu nhiên x5 Key Tầm Bảo\n"
                                                    + "Có thể đi nhiều lần\n"
                                                    + "|7|\b" + "Số Dư " + player.ExpTamkjll + " EXP Tiên Bang\n", "Top 100", "KhiêuChiến\n100M EXP\nTiênBang", "Từ chối", "Về\nđảo rùa");
                                        }
                                        break;
                                    default:
                                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "|7|HELLO BẠN IU\n"
                                                + "|6|Nhớ Nạp Nhiều Nhiều Cho Trường Nha\n"
                                                + "|7|NÂNG CẤP BÔNG TAI: UP MẢNH VỠ BÔNG TAI TẠI MAP BANG HỘI\n"
                                                + "|5|SÁCH TUYỆT KỸ: NÂNG CẤP MỞ CHỈ SỐ SKILL 10\n"
                                                + "|7|UP ĐẠO CỤ NÂNG CẤP TUYỆT KỸ TẠI CÁC MAP FIDE\n",
                                                "Cửa hàng\nBùa", "Nâng cấp\nBông tai\nPorata",
                                                "Nâng cấp\nChỉ số\nBông tai", "Nhập\nNgọc Rồng\nBăng", "Sách Tuyệt Kỹ");
                                        break;
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                switch (this.mapId) {
                                    case 5:
                                        if (player.iDMark.isBaseMenu()) {
                                            switch (select) {
                                                case 0:
                                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.EP_SAO_TRANG_BI);
                                                    break;
                                                case 1:
                                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.PHA_LE_HOA_TRANG_BI);
                                                    break;
                                                case 2:
                                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.XOA_SPL);
                                                    break;
                                                case 3:
                                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.MO_CHI_SO_CAI_TRANG);
                                                    break;
//                                                case 4:
//                                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_CHAN_MENH);
//                                                    break;
                                                case 4:
                                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_VAT_PHAM);
                                                    break;

                                                case 5:
                                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.GIA_HAN_CAI_TRANG);
                                                    break;

//                                                case 6:
//                                                    ChangeMapService.gI().changeMap(player, 112, -1, 55, 408);
//                                                    break;
//                                                case 6:
//                                                    this.createOtherMenu(player, 2010, "|7|Chào Các Bạn\n"
//                                                            + "|6|\n" + "Đây Là Chúc Phúc Rèn Bồi Dưỡng Trang Bị\n"
//                                                            + "Yêu Cầu Có Trang Bị Đang Mặc Và 100k Lưu Ly\n"
//                                                            + "Chúc Phúc 100% Thành Công Cho Trang Bị\n"
//                                                            + "Tùy ý Chúc Phúc Vào 1 Trong 5 Món Đang Mặc Không giới Hạn\n"
//                                                            + "SD Tăng 500% /1 Lần\n"
//                                                            + "hp Tằng 500% /1 Lần\n"
//                                                            + "Ki Tăng 500% /1 Lần\n"
//                                                            + "Def Tăng 500%/1 Lần\n",
//                                                            "Chúc Phúc\n Sức Đánh", "Chúc Phúc\n HP", "Chúc Phúc\n KI", "Chúc Phúc \nGiáp");
//                                                    break;
                                                case 6:
                                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.EP_CAI_TRANG);
                                                    break;
                                                  case 7:
                                                    ChangeMapService.gI().changeMap(player, 112, -1, 55, 408);
                                                    break;     
                                                    
                                                    
                                                    
                                            }

                                        } else if (player.iDMark.getIndexMenu() == 2010) {
                                            switch (select) {
                                                case 0:
                                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.CHUC_PHUC_SD);
                                                    break;
                                                case 1:
                                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.CHUC_PHUC_HP);
                                                    break;
                                                case 2:
                                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.CHUC_PHUC_KI);
                                                    break;
                                                case 3:
                                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.CHUC_PHUC_DEF);
                                                    break;
                                            }
                                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_START_COMBINE) {
                                            switch (player.combineNew.typeCombine) {
                                                case CombineServiceNew.EP_SAO_TRANG_BI:
                                                case CombineServiceNew.PHA_LE_HOA_TRANG_BI:
                                                case CombineServiceNew.XOA_SPL:
                                                case CombineServiceNew.MO_CHI_SO_CAI_TRANG:
                                                case CombineServiceNew.NANG_CAP_CHAN_MENH:
                                                case CombineServiceNew.NANG_CAP_VAT_PHAM:
                                                case CombineServiceNew.EP_CAI_TRANG:
                                                case CombineServiceNew.GIA_HAN_CAI_TRANG:
//                                                case CombineServiceNew.CHUC_PHUC_SD:
//                                                case CombineServiceNew.CHUC_PHUC_HP:
//                                                case CombineServiceNew.CHUC_PHUC_KI:
//                                                case CombineServiceNew.CHUC_PHUC_DEF:
                                                case CombineServiceNew.DAP_SET_KICH_HOAT_CAO_CAP:
                                                    switch (select) {
                                                        case 0:
                                                            if (player.combineNew.typeCombine == CombineServiceNew.PHA_LE_HOA_TRANG_BI) {
                                                                player.combineNew.dapdonhanh = 1;
                                                            }
                                                            break;
                                                        case 1:
                                                            if (player.combineNew.typeCombine == CombineServiceNew.PHA_LE_HOA_TRANG_BI) {
                                                                player.combineNew.dapdonhanh = 10;
                                                            }
                                                            break;
                                                        case 2:
                                                            if (player.combineNew.typeCombine == CombineServiceNew.PHA_LE_HOA_TRANG_BI) {
                                                                player.combineNew.dapdonhanh = 100;
                                                            }
                                                            break;
                                                    }
                                                    CombineServiceNew.gI().startCombine(player);
                                                case CombineServiceNew.PHA_LE_HOA_TRANG_BI_X10:
                                                case CombineServiceNew.TRADE_DO_THAN_LINH:
                                                case CombineServiceNew.TRADE_DO_THAN_LINH1:

                                                    if (select == 0) {
                                                        CombineServiceNew.gI().startCombine(player);
                                                    }
                                                    break;
                                            }
                                        }
                                        break;

                                    case 112:
                                        if (player.iDMark.isBaseMenu()) {
                                            long goldchallenge = 100000000;
                                            switch (select) {
                                                case 0 -> {
                                                    Service.getInstance().sendThongBaoFromAdmin(player, "Chưa có top");
                                                }
                                                case 1 -> { // xác nhận lên võ đài
                                                    if (player.ExpTamkjll >= goldchallenge) {
                                                        player.ExpTamkjll -= (goldchallenge);
                                                        VoDaiSinhTuService.gI().startChallenge(player);
                                                        PlayerService.gI().sendInfoHpMpMoney(player);
                                                    } else {
                                                        Service.getInstance().sendThongBao(player, "Không đủ EXP Tiên Bang để thực hiện, còn thiếu " + Util.numberToMoney(goldchallenge
                                                                - player.ExpTamkjll) + " EXP");
                                                        return;
                                                    }
                                                }
                                                case 2 -> {
                                                }
                                                case 3 ->
                                                    ChangeMapService.gI().changeMapBySpaceShip(player, 5, -1, 1156);
                                            }
                                        } else if (player.iDMark.getIndexMenu() == ConstNpc.NHAN_QUA_VO_DAI) {
                                            switch (select) {
                                                case 0 -> {
                                                    if (player.DoneVoDaiBaHatMit == 1) {
                                                        int idItem = Util.nextInt(1511, 1516);
                                                        Item item = ItemService.gI().createNewItem((short) idItem, 3);
                                                        InventoryService.gI().addItemBag(player, item, 9999);
                                                        InventoryService.gI().sendItemBags(player);
                                                        Service.getInstance().sendThongBao(player, "Bạn vừa nhận thưởng " + item.getName());
                                                        player.DoneVoDaiBaHatMit = 0;
                                                        break;
                                                    } else {
                                                        Service.getInstance().sendThongBao(player, "Bạn đã nhận phần thưởng này rồi");
                                                    }
                                                }
                                                case 1 -> {
                                                    if (player.DoneVoDaiBaHatMit == 1) {
                                                        int idItem = Util.nextInt(2062, 2063);
                                                        Item item = ItemService.gI().createNewItem((short) idItem, 5);
                                                        InventoryService.gI().addItemBag(player, item, 9999);
                                                        InventoryService.gI().sendItemBags(player);
                                                        Service.getInstance().sendThongBao(player, "Bạn vừa nhận thưởng " + item.getName());
                                                        player.DoneVoDaiBaHatMit = 0;
                                                        break;
                                                    } else {
                                                        Service.getInstance().sendThongBao(player, "Bạn đã nhận phần thưởng này rồi");
                                                    }
                                                }
                                            }
                                        }
                                        break;
                                    case 42:
                                    case 43:
                                    case 44:
                                    case 84:
                                        if (player.iDMark.isBaseMenu()) {
                                            switch (select) {
                                                case 0: // shop bùa
                                                    createOtherMenu(player, ConstNpc.MENU_OPTION_SHOP_BUA,
                                                            "Bùa của ta rất lợi hại, nhìn ngươi yếu đuối thế này, chắc muốn mua bùa để "
                                                            + "mạnh mẽ à, mua không ta bán cho, xài rồi lại thích cho mà xem.",
                                                            "Bùa\n1 giờ", "Bùa\n8 giờ", "Bùa\n1 tháng", "Đóng");
                                                    break;
//                                                case 1:
//                                                    CombineServiceNew.gI().openTabCombine(player,
//                                                            CombineServiceNew.NANG_CAP_VAT_PHAM);
//                                                    break;
                                                case 1: // nâng cấp bông tai
                                                    CombineServiceNew.gI().openTabCombine(player,
                                                            CombineServiceNew.NANG_CAP_BONG_TAI);
                                                    break;
                                                case 2: // làm phép nhập đá
                                                    CombineServiceNew.gI().openTabCombine(player,
                                                            CombineServiceNew.MO_CHI_SO_BONG_TAI);
                                                    break;
                                                case 3:
                                                    CombineServiceNew.gI().openTabCombine(player,
                                                            CombineServiceNew.NHAP_NGOC_RONG);
                                                    break;
                                                case 4:
                                                    createOtherMenu(player, ConstNpc.SACH_TUYET_KY,
                                                            "|5|" + "\nSÁCH TUYỆT KỸ  :NÂNG CẤP MỞ CHỈ SỐ SKILL 10\n"
                                                            + "|7|" + "\nUP ĐẠO CỤ NÂNG CẤP TUYỆT KỸ TẠI CÁC MAP FIDE\n",
                                                            "Đóng thành\nSách cũ",
                                                            "Đổi Sách\nTuyệt kỹ",
                                                            "Giám định\nSách",
                                                            "Tẩy\nSách",
                                                            "Nâng cấp\nSách\nTuyệt kỹ",
                                                            "Hồi phục\nSách",
                                                            "Phân rã\nSách");
                                                    break;
                                            }
                                        } else if (player.iDMark.getIndexMenu() == ConstNpc.SACH_TUYET_KY) {
                                            switch (select) {
                                                case 0:
                                                    Item trangSachCu = InventoryService.gI().findItemBagByTemp(player, 1425);

                                                    Item biaSach = InventoryService.gI().findItemBagByTemp(player, 1421);
                                                    if ((trangSachCu != null && trangSachCu.quantity >= 9999) && (biaSach != null && biaSach.quantity >= 9999)) {
                                                        createOtherMenu(player, ConstNpc.DONG_THANH_SACH_CU,
                                                                "|2|Chế tạo Cuốn sách cũ\n"
                                                                + "|1|Trang sách cũ " + trangSachCu.quantity + "/9999\n"
                                                                + "Bìa sách " + biaSach.quantity + "/9999\n"
                                                                + "Tỉ lệ thành công: 20%\n"
                                                                + "Thất bại mất 99 trang sách và 1 bìa sách", "Đồng ý", "Từ chối");
                                                        break;
                                                    } else {
                                                        String NpcSay = "|2|Chế tạo Cuốn sách cũ\n";
                                                        if (trangSachCu == null) {
                                                            NpcSay += "|7|Trang sách cũ " + "0/9999\n";
                                                        } else {
                                                            NpcSay += "|1|Trang sách cũ " + trangSachCu.quantity + "/9999\n";
                                                        }
                                                        if (biaSach == null) {
                                                            NpcSay += "|7|Bìa sách " + "0/1\n";
                                                        } else {
                                                            NpcSay += "|1|Bìa sách " + biaSach.quantity + "/1\n";
                                                        }

                                                        NpcSay += "|7|Tỉ lệ thành công: 20%\n";
                                                        NpcSay += "|7|Thất bại mất 99 trang sách và 1 bìa sách";
                                                        createOtherMenu(player, ConstNpc.DONG_THANH_SACH_CU_2,
                                                                NpcSay, "Từ chối");
                                                        break;
                                                    }
                                                case 1:
                                                    Item cuonSachCu = InventoryService.gI().findItemBagByTemp(player, 1424);
                                                    Item kimBam = InventoryService.gI().findItemBagByTemp(player, 1422);

                                                    if ((cuonSachCu != null && cuonSachCu.quantity >= 999) && (kimBam != null && kimBam.quantity >= 999)) {
                                                        createOtherMenu(player, ConstNpc.DOI_SACH_TUYET_KY,
                                                                "|2|Đổi sách tuyệt kỹ 1\n"
                                                                + "|1|Cuốn sách cũ " + cuonSachCu.quantity + "/999\n"
                                                                + "Kìm bấm giấy " + kimBam.quantity + "/999\n"
                                                                + "Tỉ lệ thành công: 100%\n", "Đồng ý", "Từ chối");

                                                        break;
                                                    } else {
                                                        String NpcSay = "|2|Đổi sách Tuyệt kỹ 1\n";
                                                        if (cuonSachCu == null) {
                                                            NpcSay += "|7|Cuốn sách cũ " + "0/999\n";
                                                        } else {
                                                            NpcSay += "|1|Cuốn sách cũ " + cuonSachCu.quantity + "/999\n";
                                                        }
                                                        if (kimBam == null) {
                                                            NpcSay += "|7|Kìm bấm giấy " + "0/999\n";
                                                        } else {
                                                            NpcSay += "|1|Kìm bấm giấy " + kimBam.quantity + "/999\n";
                                                        }
                                                        NpcSay += "|7|Tỉ lệ thành công: 20%\n";
                                                        createOtherMenu(player, ConstNpc.DOI_SACH_TUYET_KY_2,
                                                                NpcSay, "Từ chối");
                                                    }
                                                    break;
                                                case 2:// giám định sách
                                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.GIAM_DINH_SACH);
                                                    break;
                                                case 3:// tẩy sách
                                                    CombineServiceNew.gI().openTabCombine(player,
                                                            CombineServiceNew.TAY_SACH);
                                                    break;
                                                case 4:// nâng cấp sách
                                                    CombineServiceNew.gI().openTabCombine(player,
                                                            CombineServiceNew.NANG_CAP_SACH_TUYET_KY);
                                                    break;
                                                case 5:// phục hồi sách
                                                    CombineServiceNew.gI().openTabCombine(player,
                                                            CombineServiceNew.PHUC_HOI_SACH);
                                                    break;
                                                case 6:// phân rã sách
                                                    CombineServiceNew.gI().openTabCombine(player,
                                                            CombineServiceNew.PHAN_RA_SACH);
                                                    break;
                                            }
                                        } else if (player.iDMark.getIndexMenu() == ConstNpc.DOI_SACH_TUYET_KY) {
                                            switch (select) {
                                                case 0:
                                                    Item cuonSachCu = InventoryService.gI().findItemBagByTemp(player, 1424);
                                                    Item kimBam = InventoryService.gI().findItemBagByTemp(player, 1422);

                                                    short baseValue = 1414;
                                                    short genderModifier = (player.gender == 0) ? -2 : ((player.gender == 2) ? 2 : (short) 0);

                                                    Item sachTuyetKy = ItemService.gI().createNewItem((short) (baseValue + genderModifier));

                                                    if (Util.isTrue(20, 100)) {

                                                        sachTuyetKy.itemOptions.add(new ItemOption(245, 0));
                                                        sachTuyetKy.itemOptions.add(new ItemOption(21, 40));
                                                        sachTuyetKy.itemOptions.add(new ItemOption(30, 0));
                                                        sachTuyetKy.itemOptions.add(new ItemOption(87, 1));
                                                        sachTuyetKy.itemOptions.add(new ItemOption(242, 5));
                                                        sachTuyetKy.itemOptions.add(new ItemOption(243, 1000));
                                                        try { // send effect susscess
                                                            Message msg = new Message(-81);
                                                            msg.writer().writeByte(0);
                                                            msg.writer().writeUTF("test");
                                                            msg.writer().writeUTF("test");
                                                            msg.writer().writeShort(tempId);
                                                            player.sendMessage(msg);
                                                            msg.cleanup();
                                                            msg = new Message(-81);
                                                            msg.writer().writeByte(1);
                                                            msg.writer().writeByte(2);
                                                            msg.writer().writeByte(InventoryService.gI().getIndexBag(player, kimBam));
                                                            msg.writer().writeByte(InventoryService.gI().getIndexBag(player, cuonSachCu));
                                                            player.sendMessage(msg);
                                                            msg.cleanup();
                                                            msg = new Message(-81);
                                                            msg.writer().writeByte(7);
                                                            msg.writer().writeShort(sachTuyetKy.template.iconID);
                                                            msg.writer().writeShort(-1);
                                                            msg.writer().writeShort(-1);
                                                            msg.writer().writeShort(-1);
                                                            player.sendMessage(msg);
                                                            msg.cleanup();
                                                        } catch (Exception e) {
                                                            System.out.println("lỗi 4");
                                                        }
                                                        InventoryService.gI().addItemList(player.inventory.itemsBag, sachTuyetKy, 1);
                                                        InventoryService.gI().subQuantityItemsBag(player, cuonSachCu, 10);
                                                        InventoryService.gI().subQuantityItemsBag(player, kimBam, 1);
                                                        InventoryService.gI().sendItemBags(player);
//                                                    npcChat(player, "Thành công gòi cu ơi");
                                                        return;
                                                    } else {
                                                        try { // send effect faile
                                                            Message msg = new Message(-81);
                                                            msg.writer().writeByte(0);
                                                            msg.writer().writeUTF("test");
                                                            msg.writer().writeUTF("test");
                                                            msg.writer().writeShort(tempId);
                                                            player.sendMessage(msg);
                                                            msg.cleanup();
                                                            msg = new Message(-81);
                                                            msg.writer().writeByte(1);
                                                            msg.writer().writeByte(2);
                                                            msg.writer().writeByte(InventoryService.gI().getIndexBag(player, kimBam));
                                                            msg.writer().writeByte(InventoryService.gI().getIndexBag(player, cuonSachCu));
                                                            player.sendMessage(msg);
                                                            msg.cleanup();
                                                            msg = new Message(-81);
                                                            msg.writer().writeByte(8);
                                                            msg.writer().writeShort(-1);
                                                            msg.writer().writeShort(-1);
                                                            msg.writer().writeShort(-1);
                                                            player.sendMessage(msg);
                                                            msg.cleanup();
                                                        } catch (Exception e) {
                                                            System.out.println("lỗi 3");
                                                        }
                                                        InventoryService.gI().subQuantityItemsBag(player, cuonSachCu, 5);
                                                        InventoryService.gI().subQuantityItemsBag(player, kimBam, 1);
                                                        InventoryService.gI().sendItemBags(player);
//                                                    npcChat(player, "Thất bại gòi cu ơi");
                                                    }
                                                    return;
                                                case 1:
                                                    break;
                                            }
                                        } else if (player.iDMark.getIndexMenu() == ConstNpc.DONG_THANH_SACH_CU) {
                                            switch (select) {
                                                case 0:

                                                    Item trangSachCu = InventoryService.gI().findItemBagByTemp(player, 1425);
                                                    Item biaSach = InventoryService.gI().findItemBagByTemp(player, 1421);
                                                    Item cuonSachCu = ItemService.gI().createNewItem((short) 1424);
                                                    if (Util.isTrue(20, 100)) {
                                                        cuonSachCu.itemOptions.add(new ItemOption(30, 0));

                                                        try { // send effect susscess
                                                            Message msg = new Message(-81);
                                                            msg.writer().writeByte(0);
                                                            msg.writer().writeUTF("test");
                                                            msg.writer().writeUTF("test");
                                                            msg.writer().writeShort(tempId);
                                                            player.sendMessage(msg);
                                                            msg.cleanup();

                                                            msg = new Message(-81);
                                                            msg.writer().writeByte(1);
                                                            msg.writer().writeByte(2);
                                                            msg.writer().writeByte(InventoryService.gI().getIndexBag(player, trangSachCu));
                                                            msg.writer().writeByte(InventoryService.gI().getIndexBag(player, biaSach));
                                                            player.sendMessage(msg);
                                                            msg.cleanup();

                                                            msg = new Message(-81);
                                                            msg.writer().writeByte(7);
                                                            msg.writer().writeShort(cuonSachCu.template.iconID);
                                                            msg.writer().writeShort(-1);
                                                            msg.writer().writeShort(-1);
                                                            msg.writer().writeShort(-1);
                                                            player.sendMessage(msg);
                                                            msg.cleanup();

                                                        } catch (Exception e) {
                                                            System.out.println("lỗi 1");
                                                        }

                                                        InventoryService.gI().addItemList(player.inventory.itemsBag, cuonSachCu, 1);
                                                        InventoryService.gI().subQuantityItemsBag(player, trangSachCu, 9999);
                                                        InventoryService.gI().subQuantityItemsBag(player, biaSach, 1);
                                                        InventoryService.gI().sendItemBags(player);
                                                        return;
                                                    } else {
                                                        try { // send effect faile
                                                            Message msg = new Message(-81);
                                                            msg.writer().writeByte(0);
                                                            msg.writer().writeUTF("test");
                                                            msg.writer().writeUTF("test");
                                                            msg.writer().writeShort(tempId);
                                                            player.sendMessage(msg);
                                                            msg.cleanup();
                                                            msg = new Message(-81);
                                                            msg.writer().writeByte(1);
                                                            msg.writer().writeByte(2);
                                                            msg.writer().writeByte(InventoryService.gI().getIndexBag(player, biaSach));
                                                            msg.writer().writeByte(InventoryService.gI().getIndexBag(player, trangSachCu));
                                                            player.sendMessage(msg);
                                                            msg.cleanup();
                                                            msg = new Message(-81);
                                                            msg.writer().writeByte(8);
                                                            msg.writer().writeShort(-1);
                                                            msg.writer().writeShort(-1);
                                                            msg.writer().writeShort(-1);
                                                            player.sendMessage(msg);
                                                            msg.cleanup();
                                                        } catch (Exception e) {
                                                            System.out.println("lỗi 2");
                                                        }
                                                        InventoryService.gI().subQuantityItemsBag(player, trangSachCu, 99);
                                                        InventoryService.gI().subQuantityItemsBag(player, biaSach, 1);
                                                        InventoryService.gI().sendItemBags(player);
                                                    }
                                                    return;
                                                case 1:
                                                    break;
                                            }
                                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPTION_SHOP_BUA) {
                                            switch (select) {
                                                case 0:
                                                    ShopService.gI().openShopBua(player, ConstNpc.SHOP_BA_HAT_MIT_0, 0);
                                                    break;
                                                case 1:
                                                    ShopService.gI().openShopBua(player, ConstNpc.SHOP_BA_HAT_MIT_1, 1);
                                                    break;
                                                case 2:
                                                    ShopService.gI().openShopBua(player, ConstNpc.SHOP_BA_HAT_MIT_2, 2);
                                                    break;
                                            }
                                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_START_COMBINE) {
                                            switch (player.combineNew.typeCombine) {
                                                case CombineServiceNew.NANG_CAP_VAT_PHAM:
                                                    if (select == 0) {
                                                        player.iDMark.isUseTuiBaoVeNangCap = false;
                                                        CombineServiceNew.gI().startCombine(player);
                                                    } else if (select == 1) {
                                                        player.iDMark.isUseTuiBaoVeNangCap = true;
                                                        CombineServiceNew.gI().startCombine(player);
                                                    }
                                                    break;

                                                case CombineServiceNew.NANG_CAP_BONG_TAI:
                                                case CombineServiceNew.MO_CHI_SO_BONG_TAI:
                                                case CombineServiceNew.LAM_PHEP_NHAP_DA:
                                                case CombineServiceNew.NHAP_NGOC_RONG:
                                                case CombineServiceNew.GIAM_DINH_SACH:
                                                case CombineServiceNew.TAY_SACH:
                                                case CombineServiceNew.NANG_CAP_SACH_TUYET_KY:
                                                case CombineServiceNew.PHUC_HOI_SACH:
                                                case CombineServiceNew.PHAN_RA_SACH:
                                                    if (select == 0) {
                                                        CombineServiceNew.gI().startCombine(player);
                                                    }
                                                    break;
                                            }
                                        }
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }
                    };
                    break;

                case ConstNpc.RUONG_DO:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                InventoryService.gI().sendItemBox(player);
                                InventoryService.gI().openBox(player);
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {

                            }
                        }
                    };
                    break;
                case ConstNpc.DAU_THAN:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                player.magicTree.openMenuTree();
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                TaskService.gI().checkDoneTaskConfirmMenuNpc(player, this, (byte) select);
                                switch (player.iDMark.getIndexMenu()) {
                                    case ConstNpc.MAGIC_TREE_NON_UPGRADE_LEFT_PEA:
                                        switch (select) {
                                            case 0:
                                                player.magicTree.harvestPea();
                                                break;
                                            case 1:
                                                if (player.magicTree.level == 10) {
                                                    player.magicTree.fastRespawnPea();
                                                } else {
                                                    player.magicTree.showConfirmUpgradeMagicTree();
                                                }
                                                break;
                                            case 2:
                                                player.magicTree.fastRespawnPea();
                                                break;
                                            default:
                                                break;
                                        }
                                        break;

                                    case ConstNpc.MAGIC_TREE_NON_UPGRADE_FULL_PEA:
                                        if (select == 0) {
                                            player.magicTree.harvestPea();
                                        } else if (select == 1) {
                                            player.magicTree.showConfirmUpgradeMagicTree();
                                        }
                                        break;
                                    case ConstNpc.MAGIC_TREE_CONFIRM_UPGRADE:
                                        if (select == 0) {
                                            player.magicTree.upgradeMagicTree();
                                        }
                                        break;
                                    case ConstNpc.MAGIC_TREE_UPGRADE:
                                        if (select == 0) {
                                            player.magicTree.fastUpgradeMagicTree();
                                        } else if (select == 1) {
                                            player.magicTree.showConfirmUnuppgradeMagicTree();
                                        }
                                        break;
                                    case ConstNpc.MAGIC_TREE_CONFIRM_UNUPGRADE:
                                        if (select == 0) {
                                            player.magicTree.unupgradeMagicTree();
                                        }
                                        break;
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.CALICK:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            player.iDMark.setIndexMenu(ConstNpc.BASE_MENU);
                            if (TaskService.gI().getIdTask(player) < ConstTask.TASK_0_0) {
                                Service.getInstance().hideWaitDialog(player);
                                Service.getInstance().sendThongBao(player, "Không thể thực hiện");
                                return;
                            }
                            if (this.mapId == 102) {
                                this.createOtherMenu(player, ConstNpc.BASE_MENU, "Chào chú, cháu có thể giúp gì?",
                                        "Kể\nChuyện", "Quay về\nQuá khứ");
                            } else {
                                this.createOtherMenu(player, ConstNpc.BASE_MENU, "Chào chú, cháu có thể giúp gì?",
                                        "Kể\nChuyện", "Đi đến\nTương lai", "Từ chối");
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (this.mapId == 102) {
                                if (player.iDMark.isBaseMenu()) {
                                    if (select == 0) {
                                        NpcService.gI().createTutorial(player, this.avartar, ConstNpc.CALICK_KE_CHUYEN);
                                    } else if (select == 1) {
                                        ChangeMapService.gI().goToQuaKhu(player);
                                    }
                                }
                            } else if (player.iDMark.isBaseMenu()) {
                                switch (select) {
                                    case 0:
                                        NpcService.gI().createTutorial(player, this.avartar, ConstNpc.CALICK_KE_CHUYEN);
                                        break;
                                    case 1:
                                        if (TaskService.gI().getIdTask(player) >= ConstTask.TASK_0_0) {
                                            ChangeMapService.gI().goToTuongLai(player);
                                        }
                                        break;
                                    default:
                                        Service.getInstance().sendThongBao(player, "Không thể thực hiện");
                                        break;
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.THAN_MEO_KARIN:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                if (mapId == ConstMap.THAP_KARIN) {
                                    if (player.zone instanceof ZSnakeRoad) {
                                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                                "Hãy cầm lấy hai hạt đậu cuối cùng ở đây\nCố giữ mình nhé "
                                                + player.name,
                                                "Cảm ơn\nsư phụ");
                                    } else if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                                "Con hãy bay theo cây Gậy Như Ý trên đỉnh tháp để đến Thần Điện gặp Thượng đế"
                                                + "\n Con xứng đáng đượng làm đệ tử ông ấy!", "Tập luyện\n'500tr vàng'", "Từ chối");
                                    }
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (mapId == ConstMap.THAP_KARIN) {
                                    if (player.iDMark.isBaseMenu()) {
                                        if (player.zone instanceof ZSnakeRoad) {
                                            switch (select) {
                                                case 0:
                                                    player.setInteractWithKarin(true);
                                                    Service.getInstance().sendThongBao(player,
                                                            "Hãy mau bay xuống chân tháp Karin");
                                                    break;
                                            }
                                        } else {
                                            switch (select) {
                                                case 0:

                                                    break;
                                                case 1:
                                                    break;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.THUONG_DE:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 45) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Con muốn làm gì nào", "Đến Kaio",
                                            "Không làm gì");
                                }
                                if (mapId == 141) {
                                    if (player.Tamkjlltutien[2] < 1) {
                                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                                "|9|\nXin Chào Chủ Nhân Thiên Giới\n"
                                                + "|8|\nChào mừng Đạo Hữu Đến Với Thế Giới Tu Tiên",
                                                "Về nhà",
                                                "Thông Tin\nCảnh Giới",
                                                "Luyện Hóa\nThiên Phú",
                                                "Thông Tin\nChỉ Số",
                                                "Thiên Đạo\nĐộ Kiếp"
                                        );
                                    } else {
                                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                                "|9|\nXin Chào Chủ Nhân Thiên Giới\n"
                                                + "|8|\nChào mừng Đạo Hữu Đến Với Thế Giới Tu Tiên",
                                                "Về nhà",
                                                "Thông Tin\nCảnh Giới",
                                                "Luyện Hóa\nThiên Phú",
                                                "Thông Tin\nChỉ Số",
                                                "Thiên Đạo\nĐộ Kiếp"
                                        );
                                    }
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 45) {
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {
                                            case 0:
                                                ChangeMapService.gI().changeMapBySpaceShip(player, 48, -1, 354);
                                                break;
                                            case 1:
//                                                this.createOtherMenu(player, ConstNpc.MENU_CHOOSE_LUCKY_ROUND,
//                                                        "Con muốn làm gì nào?", "Quay bằng\nhồng ngọc",
//                                                        "Rương phụ\n("
//                                                        + (player.inventory.itemsBoxCrackBall.size()
//                                                        - InventoryService.gI().getCountEmptyListItem(
//                                                                player.inventory.itemsBoxCrackBall))
//                                                        + " món)",
//                                                        "Xóa hết\ntrong rương", "Đóng");
                                                ShopService.gI().openShopSpecial(player, this, ConstNpc.SHOPTHUONGDE, 0, -1);

                                                break;
                                        }
                                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_CHOOSE_LUCKY_ROUND) {
                                        switch (select) {
                                            case 0:
                                                LuckyRoundService.gI().openCrackBallUI(player,
                                                        LuckyRoundService.USING_GEM);
                                                break;
                                            case 1:
                                                ShopService.gI().openBoxItemLuckyRound(player);
                                                break;
                                            case 2:
                                                NpcService.gI().createMenuConMeo(player,
                                                        ConstNpc.CONFIRM_REMOVE_ALL_ITEM_LUCKY_ROUND, this.avartar,
                                                        "Con có chắc muốn xóa hết vật phẩm trong rương phụ? Sau khi xóa "
                                                        + "sẽ không thể khôi phục!",
                                                        "Đồng ý", "Hủy bỏ");
                                                break;
                                        }
                                    }
                                } else if (mapId == 141) {
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {
                                            case 0:
                                                ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, -1, 354);
                                                break;
                                            case 1:
                                                String dktt;
                                                if (player.Tamkjlltutien[1] < 96) {
                                                    dktt = "Điều kiện Phi Tiên lên "
                                                            + player.TamkjllTuviTutien(Util.maxInt(player.Tamkjlltutien[1]) + 1);
                                                    dktt += "\nExp tu tiền cần: "
                                                            + player.TamkjllDieukiencanhgioi(
                                                                    Util.maxInt(player.Tamkjlltutien[1]));
                                                    dktt += "\nTỉ lệ thành công: "
                                                            + player.Tamkjlltilecanhgioi(Util.maxInt(player.Tamkjlltutien[1]))
                                                            + "%";
                                                } else {
                                                    dktt = "XIN CHÀO CHỦ NHÂN THIÊN GIỚI";
                                                }
                                                this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                                        "Cảnh giới: "
                                                        + player.TamkjllTuviTutien(Util.maxInt(player.Tamkjlltutien[1]))
                                                        + "\nExp Tu tiên: " + Util.getFormatNumber(player.Tamkjlltutien[0])
                                                        + "\nThiên phú: " + player.Tamkjlltutien[2] + " Sao"
                                                        + "\n" + dktt,
                                                        "|8|\nXin Chào Chủ Nhân Thiên Giới\n"
                                                        + "Chào mừng Đạo Hữu Đến Với Thế Giới Tu Tiên",
                                                        "Về nhà",
                                                        "Thông Tin\nCảnh Giới",
                                                        "Luyện Hóa\nThiên Phú",
                                                        "Thông Tin\nChỉ Số",
                                                        "Thiên Đạo\nĐộ Kiếp"
                                                );
                                                break;
                                            case 2:
                                                if (player.Tamkjlltutien[2] < 1) {
                                                    if (player.Tindung < 100) {
                                                        Service.getInstance().sendThongBao(player, "cần ít nhất 100 Điểm Tín Dụng");
                                                        return;
                                                    }
                                                    player.Tindung -= 100;
                                                    int tp = Util.nextInt(1, Util.nextInt(1, Util.nextInt(1, 10)));
                                                    player.Tamkjlltutien[2] = tp;
                                                    Service.getInstance().sendThongBaoAllPlayer(
                                                            "Chúc Mừng Bạn : " + player.name + " Đã Tẩy Luyện Mở Ra Thiên Phú\n" + tp + " Sao");
                                                } else {
                                                    if (player.CapTamkjll < 1) {
                                                        Service.getInstance().sendThongBao(player,
                                                                "Muốn tẩy thiên phú Tiên Bang của con ít nhất phải 1.");
                                                        return;
                                                    }
                                                    if (player.Tindung < 100) {
                                                        Service.getInstance().sendThongBao(player, "cần ít nhất 100 Điểm Tín Dụng");
                                                        return;
                                                    }
                                                    if (player.Tamkjlltutien[2] >= 50) {
                                                        Service.getInstance().sendThongBao(player, "Con đã là tuyệt thế thiên tài");
                                                        return;
                                                    }
                                                    player.Tindung -= 100;
                                                    if (Util.isTrue(1f, 100)) {
                                                        player.Tamkjlltutien[2]++;
                                                        Service.getInstance().sendThongBaoAllPlayer(
                                                                "|8|\nChúc Mừng Bạn : " + player.name + " Đã Tẩy Luyện Thiên Phú Từ "
                                                                + (player.Tamkjlltutien[2] - 1) + " sao lên: "
                                                                + player.Tamkjlltutien[2] + " sao.");
                                                    } else {
                                                        Service.getInstance().sendThongBao(player, "Xin lỗi nhưng ta đã cố hết xức.");
                                                    }
                                                }
                                                break;
                                            case 3:
                                                if (player.Tamkjlltutien[2] < 1) {
                                                    Service.getInstance().sendThongBao(player, "Con chưa mở thiên phú.");
                                                    return;
                                                }
                                                this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                                        "Cảnh giới: "
                                                        + player.TamkjllTuviTutien(Util.maxInt(player.Tamkjlltutien[1]))
                                                        + "\nHp: "
                                                        + player.TamkjllHpKiGiaptutien(
                                                                Util.maxInt(player.Tamkjlltutien[1]))
                                                        + "%\nKi: "
                                                        + player.TamkjllHpKiGiaptutien(
                                                                Util.maxInt(player.Tamkjlltutien[1]))
                                                        + "%\nGiáp: "
                                                        + player.TamkjllHpKiGiaptutien(
                                                                Util.maxInt(player.Tamkjlltutien[1]))
                                                        + "%\nDame: "
                                                        + player.TamkjllDametutien(Util.maxInt(player.Tamkjlltutien[1]))
                                                        + "%\n",
                                                        //                                                        + player.TamkjllDametutien(Util.maxInt(player.Tamkjlltutien[1]))
                                                        //                                                        + "%",
                                                        "|8|\nXin Chào Chủ Nhân Thiên Giới\n"
                                                        + "Chào mừng Đạo Hữu Đến Với Thế Giới Tu Tiên",
                                                        "Về nhà",
                                                        "Thông Tin\nCảnh Giới",
                                                        "Luyện Hóa\nThiên Phú",
                                                        "Thông Tin\nChỉ Số",
                                                        "Thiên Đạo\nĐộ Kiếp"
                                                );
                                                break;
                                        }
                                    }
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.THAN_VU_TRU:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 48) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Con muốn làm gì nào", "Di chuyển");
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 48) {
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {
                                            case 0:
                                                this.createOtherMenu(player, ConstNpc.MENU_DI_CHUYEN,
                                                        "Con muốn đi đâu?", "Về\nthần điện", "Thánh địa\nKaio",
                                                        "Con\nđường\nrắn độc", "Từ chối");
                                                break;
                                        }
                                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_DI_CHUYEN) {
                                        switch (select) {
                                            case 0:
                                                ChangeMapService.gI().changeMapBySpaceShip(player, 45, -1, 354);
                                                break;
                                            case 1:
                                                ChangeMapService.gI().changeMap(player, 50, -1, 318, 336);
                                                break;
                                            case 2:
                                                // con đường rắn độc
                                                if (player.clan != null) {
                                                    Calendar calendar = Calendar.getInstance();
                                                    int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                                                    if (!(dayOfWeek == Calendar.MONDAY
                                                            || dayOfWeek == Calendar.WEDNESDAY
                                                            || dayOfWeek == Calendar.FRIDAY
                                                            || dayOfWeek == Calendar.SUNDAY)) {
                                                        Service.getInstance().sendThongBao(player,
                                                                "Chỉ mở vào thứ 2, 4, 6, CN hàng tuần!");
                                                        return;
                                                    }
                                                    if (player.clanMember.getNumDateFromJoinTimeToToday() < 2) {
                                                        Service.getInstance().sendThongBao(player,
                                                                "Phải tham gia bang hội ít nhất 2 ngày mới có thể tham gia!");
                                                        return;
                                                    }
                                                    if (player.clan.snakeRoad == null) {
                                                        this.createOtherMenu(player, ConstNpc.MENU_CHON_CAP_DO,
                                                                "Hãy mau trở về bằng con đường rắn độc\nbọn Xayda đã đến Trái Đất",
                                                                "Chọn\ncấp độ", "Từ chối");
                                                    } else {
                                                        if (player.clan.snakeRoad.isClosed()) {
                                                            Service.getInstance().sendThongBao(player,
                                                                    "Bang hội đã hết lượt tham gia!");
                                                        } else {
                                                            this.createOtherMenu(player,
                                                                    ConstNpc.MENU_ACCEPT_GO_TO_CDRD,
                                                                    "Con có chắc chắn muốn đến con đường rắn độc cấp độ "
                                                                    + player.clan.snakeRoad.getLevel() + "?",
                                                                    "Đồng ý", "Từ chối");
                                                        }
                                                    }
                                                } else {
                                                    Service.getInstance().sendThongBao(player,
                                                            "Chỉ dành cho những người trong bang hội!");
                                                }
                                                break;

                                        }
                                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_CHON_CAP_DO) {
                                        switch (select) {
                                            case 0:
                                                Input.gI().createFormChooseLevelCDRD(player);
                                                break;

                                        }
                                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_ACCEPT_GO_TO_CDRD) {
                                        switch (select) {
                                            case 0:
                                                if (player.clan != null) {
                                                    synchronized (player.clan) {
                                                        if (player.clan.snakeRoad == null) {
                                                            int level = Byte.parseByte(
                                                                    String.valueOf(PLAYERID_OBJECT.get(player.id)));
                                                            SnakeRoad road = new SnakeRoad(level);
                                                            ServerManager.gI().getDungeonManager().addDungeon(road);
                                                            road.join(player);
                                                            player.clan.snakeRoad = road;
                                                        } else {
                                                            player.clan.snakeRoad.join(player);
                                                        }
                                                    }
                                                }
                                                break;
                                        }
                                    }
                                }
                            }
                        }

                    };
                    break;
                case ConstNpc.KIBIT:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 50) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                            "Đến\nKaio", "Từ chối");
                                } else {
                                    super.openBaseMenu(player);
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 50) {
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {
                                            case 0:
                                                ChangeMapService.gI().changeMap(player, 48, -1, 354, 240);
                                                break;
                                        }
                                    }
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.OSIN:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 50) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                            "Đến\nKaio", "Đến\nhành tinh\nBill", "Từ chối");
                                } else if (this.mapId == 52) {
                                    if (MabuWar.gI().isTimeMabuWar() || MabuWar14h.gI().isTimeMabuWar()) {
                                        if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                                            this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                                    "Bây giờ tôi sẽ bí mật...\n đuổi theo 2 tên đồ tể... \n"
                                                    + "Quý vị nào muốn đi theo thì xin mời !",
                                                    "Ok", "Từ chối");
                                        }
                                    } else {
                                        if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                                            this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                                    "Vào lúc 12h tôi sẽ bí mật...\n đuổi theo 2 tên đồ tể... \n"
                                                    + "Quý vị nào muốn đi theo thì xin mời !",
                                                    "Ok", "Từ chối");
                                        }
                                    }
                                } else if (this.mapId == 154) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                            "Về thánh địa", "Đến\nhành tinh\nngục tù", "Từ chối");
                                } else if (this.mapId == 155) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                            "Quay về", "Từ chối");
                                } else if (MapService.gI().isMapMabuWar(this.mapId)) {
                                    if (MabuWar.gI().isTimeMabuWar()) {
                                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                                "Đừng vội xem thường Babyđây,ngay đến cha hắn là thần ma đạo sĩ\n"
                                                + "Bibiđây khi còn sống cũng phải sợ hắn đấy",
                                                "Giải trừ\nphép thuật\n50Tr Vàng",
                                                player.zone.map.mapId != 120 ? "Xuống\nTầng Dưới" : "Rời\nKhỏi đây");
                                    } else if (MabuWar14h.gI().isTimeMabuWar()) {
                                        createOtherMenu(player, ConstNpc.BASE_MENU, "Ta sẽ phù hộ cho ngươi bằng nguồn sức mạnh của Thần Kaiô\n+1 triệu HP, +1 triệu MP, +10k Sức đánh\nLưu ý: sức mạnh sẽ biến mất khi ngươi rời khỏi đây", "Phù hộ\n55 hồng ngọc", "Từ chối", "Về\nĐại Hội\nVõ Thuật");
                                    }
                                } else {
                                    super.openBaseMenu(player);
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 50) {
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {
                                            case 0:
                                                ChangeMapService.gI().changeMap(player, 48, -1, 354, 240);
                                                break;
                                            case 1:
                                                ChangeMapService.gI().changeMap(player, 154, -1, 200, 312);
                                                break;
                                        }
                                    }
                                } else if (this.mapId == 52) {
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {
                                            case 0:
                                                ChangeMapService.gI().changeMap(player, 114, Util.nextInt(0, 24), 354, 240);
                                                break;
                                        }
                                    }
                                } else if (this.mapId == 154) {
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {
                                            case 0:
                                                ChangeMapService.gI().changeMap(player, 50, -1, 318, 336);
                                                break;
                                            case 1:
                                                if (!Manager.gI().getGameConfig().isOpenPrisonPlanet()) {
                                                    Service.getInstance().sendThongBao(player,
                                                            "Lối vào hành tinh ngục tù chưa mở");
                                                    return;
                                                }
                                                if (player.nPoint.power < 60000000000L) {
                                                    Service.getInstance().sendThongBao(player,
                                                            "Yêu cầu tối thiếu 60tỷ sức mạnh");
                                                    return;
                                                }
                                                ChangeMapService.gI().changeMap(player, 155, -1, 111, 792);
                                                break;
                                        }
                                    }
                                } else if (this.mapId == 155) {
                                    if (player.iDMark.isBaseMenu()) {
                                        if (select == 0) {
                                            ChangeMapService.gI().changeMap(player, 154, -1, 200, 312);
                                        }
                                    }
                                } else if (MapService.gI().isMapMabuWar(this.mapId)) {
                                    if (player.iDMark.isBaseMenu()) {
                                        if (MabuWar.gI().isTimeMabuWar()) {
                                            switch (select) {
                                                case 0:
                                                    if (player.inventory.getGold() >= 50000000) {
                                                        Service.getInstance().changeFlag(player, 9);
                                                        player.inventory.subGold(50000000);

                                                    } else {
                                                        Service.getInstance().sendThongBao(player, "Không đủ vàng");
                                                    }
                                                    break;
                                                case 1:
                                                    if (player.zone.map.mapId == 120) {
                                                        ChangeMapService.gI().changeMapBySpaceShip(player,
                                                                player.gender + 21, -1, 250);
                                                    }
                                                    if (player.cFlag == 9) {
                                                        if (player.getPowerPoint() >= 20) {
                                                            if (!(player.zone.map.mapId == 119)) {
                                                                int idMapNextFloor = player.zone.map.mapId == 115
                                                                        ? player.zone.map.mapId + 2
                                                                        : player.zone.map.mapId + 1;
                                                                ChangeMapService.gI().changeMap(player, idMapNextFloor, -1,
                                                                        354, 240);
                                                            } else {
                                                                Zone zone = MabuWar.gI().getMapLastFloor(120);
                                                                if (zone != null) {
                                                                    ChangeMapService.gI().changeMap(player, zone, 354, 240);
                                                                } else {
                                                                    Service.getInstance().sendThongBao(player,
                                                                            "Trận đại chiến đã kết thúc, tàu vận chuyển sẽ đưa bạn về nhà");
                                                                }
                                                            }
                                                            player.resetPowerPoint();
                                                            player.sendMenuGotoNextFloorMabuWar = false;
                                                            Service.getInstance().sendPowerInfo(player, "%",
                                                                    player.getPowerPoint());
                                                            if (Util.isTrue(1, 30)) {
                                                                player.inventory.ruby += 1;
                                                                PlayerService.gI().sendInfoHpMpMoney(player);
                                                                Service.getInstance().sendThongBao(player,
                                                                        "Bạn nhận được 1 Hồng Ngọc");
                                                            } else {
                                                                Service.getInstance().sendThongBao(player,
                                                                        "Bạn đen vô cùng luôn nên không nhận được gì cả");
                                                            }
                                                        } else {
                                                            this.npcChat(player,
                                                                    "Ngươi cần có đủ điểm để xuống tầng tiếp theo");
                                                        }
                                                        break;
                                                    } else {
                                                        this.npcChat(player,
                                                                "Ngươi đang theo phe Babiđây,Hãy qua bên đó mà thể hiện");
                                                    }
                                            }
                                        } else if (MabuWar14h.gI().isTimeMabuWar()) {
                                            switch (select) {
                                                case 0:
                                                    if (player.effectSkin.isPhuHo) {
                                                        this.npcChat("Con đã mang trong mình sức mạnh của thần Kaiô!");
                                                        return;
                                                    }
                                                    if (player.inventory.ruby < 55) {
                                                        Service.getInstance().sendThongBao(player, "Bạn không đủ hồng ngọc");
                                                    } else {
                                                        player.inventory.ruby -= 55;
                                                        player.effectSkin.isPhuHo = true;
                                                        Service.getInstance().point(player);
                                                        this.npcChat("Ta đã phù hộ cho con hãy giúp ta tiêu diệt Mabư!");
                                                    }
                                                    break;
                                                case 2:
                                                    ChangeMapService.gI().changeMapBySpaceShip(player, 52, -1, 250);
                                                    break;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.BABIDAY:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                if (MapService.gI().isMapMabuWar(this.mapId) && MabuWar.gI().isTimeMabuWar()) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                            "Đừng vội xem thường Babyđây,ngay đến cha hắn là thần ma đạo sĩ\n"
                                            + "Bibiđây khi còn sống cũng phải sợ hắn đấy",
                                            "Yểm bùa\n50Tr Vàng",
                                            player.zone.map.mapId != 120 ? "Xuống\nTầng Dưới" : "Rời\nKhỏi đây");
                                } else {
                                    super.openBaseMenu(player);
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (MapService.gI().isMapMabuWar(this.mapId) && MabuWar.gI().isTimeMabuWar()) {
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {
                                            case 0:
                                                if (player.inventory.getGold() >= 50000000) {
                                                    Service.getInstance().changeFlag(player, 10);
                                                    player.inventory.subGold(50000000);
                                                } else {
                                                    Service.getInstance().sendThongBao(player, "Không đủ vàng");
                                                }
                                                break;
                                            case 1:
                                                if (player.zone.map.mapId == 120) {
                                                    ChangeMapService.gI().changeMapBySpaceShip(player,
                                                            player.gender + 21, -1, 250);
                                                }
                                                if (player.cFlag == 10) {
                                                    if (player.getPowerPoint() >= 20) {
                                                        if (!(player.zone.map.mapId == 119)) {
                                                            int idMapNextFloor = player.zone.map.mapId == 115
                                                                    ? player.zone.map.mapId + 2
                                                                    : player.zone.map.mapId + 1;
                                                            ChangeMapService.gI().changeMap(player, idMapNextFloor, -1,
                                                                    354, 240);
                                                        } else {
                                                            Zone zone = MabuWar.gI().getMapLastFloor(120);
                                                            if (zone != null) {
                                                                ChangeMapService.gI().changeMap(player, zone, 354, 240);
                                                            } else {
                                                                Service.getInstance().sendThongBao(player,
                                                                        "Trận đại chiến đã kết thúc, tàu vận chuyển sẽ đưa bạn về nhà");
                                                                ChangeMapService.gI().changeMapBySpaceShip(player,
                                                                        player.gender + 21, -1, 250);
                                                            }
                                                        }
                                                        player.resetPowerPoint();
                                                        player.sendMenuGotoNextFloorMabuWar = false;
                                                        Service.getInstance().sendPowerInfo(player, "TL",
                                                                player.getPowerPoint());
                                                        if (Util.isTrue(1, 30)) {
                                                            player.inventory.ruby += 1;
                                                            PlayerService.gI().sendInfoHpMpMoney(player);
                                                            Service.getInstance().sendThongBao(player,
                                                                    "Bạn nhận được 1 Hồng Ngọc");
                                                        } else {
                                                            Service.getInstance().sendThongBao(player,
                                                                    "Bạn đen vô cùng luôn nên không nhận được gì cả");
                                                        }
                                                    } else {
                                                        this.npcChat(player,
                                                                "Ngươi cần có đủ điểm để xuống tầng tiếp theo");
                                                    }
                                                    break;
                                                } else {
                                                    this.npcChat(player,
                                                            "Ngươi đang theo phe Ôsin,Hãy qua bên đó mà thể hiện");
                                                }
                                        }
                                    }
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.LINH_CANH:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                if (player.clan == null) {
                                    this.createOtherMenu(player, ConstNpc.MENU_KHONG_CHO_VAO_DT,
                                            "Chỉ tiếp các bang hội, miễn tiếp khách vãng lai", "Đóng");
                                } else if (player.clan.getMembers().size() < 0) {
                                    this.createOtherMenu(player, ConstNpc.MENU_KHONG_CHO_VAO_DT,
                                            "Bang hội phải có ít nhất 0 thành viên mới có thể mở", "Đóng");
                                } else {
                                    ClanMember clanMember = player.clan.getClanMember((int) player.id);
                                    if (player.nPoint.dameg < 1_000) {
                                        NpcService.gI().createTutorial(player, avartar,
                                                "Bạn phải đạt 1k sức đánh gốc");
                                        return;
                                    }
                                    int days = (int) (((System.currentTimeMillis() / 1000) - clanMember.joinTime) / 60 / 60 / 24);
                                    if (days < 0) {
                                        NpcService.gI().createTutorial(player, avartar,
                                                "Chỉ những thành viên gia nhập bang hội tối thiểu 3000 ngày mới có thể tham gia");
                                        return;
                                    }
                                    if (!player.clan.haveGoneDoanhTrai && player.clan.timeOpenDoanhTrai != 0) {
                                        createOtherMenu(player, ConstNpc.MENU_VAO_DT,
                                                "Bang hội của ngươi đang đánh trại độc nhãn\n" + "Thời gian còn lại là "
                                                + TimeUtil.getSecondLeft(player.clan.timeOpenDoanhTrai,
                                                        DoanhTrai.TIME_DOANH_TRAI / 1000)
                                                + ". Ngươi có muốn tham gia không?",
                                                "Tham gia", "Không", "Hướng\ndẫn\nthêm");
                                    } else {
                                        List<Player> plSameClans = new ArrayList<>();
                                        List<Player> playersMap = player.zone.getPlayers();
                                        synchronized (playersMap) {
                                            for (Player pl : playersMap) {
                                                if (!pl.equals(player) && pl.clan != null
                                                        && pl.clan.id == player.clan.id && pl.location.x >= 1285
                                                        && pl.location.x <= 1645) {
                                                    plSameClans.add(pl);
                                                }

                                            }
                                        }
//                                        if (plSameClans.size() >= 0) {
                                        if (plSameClans.size() >= 0) {
                                            if (!player.isAdmin() && player.clanMember
                                                    .getNumDateFromJoinTimeToToday() < DoanhTrai.DATE_WAIT_FROM_JOIN_CLAN) {
                                                createOtherMenu(player, ConstNpc.MENU_KHONG_CHO_VAO_DT,
                                                        "Bang hội chỉ cho phép những người ở trong bang trên 0 ngày. Hẹn ngươi quay lại vào lúc khác",
                                                        "OK", "Hướng\ndẫn\nthêm");
                                            } else if (player.clan.haveGoneDoanhTrai) {
                                                createOtherMenu(player, ConstNpc.MENU_KHONG_CHO_VAO_DT,
                                                        "Bang hội của ngươi đã đi trại lúc "
                                                        + Util.formatTime(player.clan.timeOpenDoanhTrai)
                                                        + " hôm nay. Người mở\n" + "("
                                                        + player.clan.playerOpenDoanhTrai.name
                                                        + "). Hẹn ngươi quay lại vào ngày mai",
                                                        "OK", "Hướng\ndẫn\nthêm");

                                            } else {
                                                createOtherMenu(player, ConstNpc.MENU_CHO_VAO_DT,
                                                        "Hôm nay bang hội của ngươi chưa vào trại lần nào. Ngươi có muốn vào\n"
                                                        + "không?\nĐể vào, ta khuyên ngươi nên có 3-4 người cùng bang đi cùng",
                                                        "Vào\n(miễn phí)", "Không", "Hướng\ndẫn\nthêm");
                                            }
                                        } else {
                                            createOtherMenu(player, ConstNpc.MENU_KHONG_CHO_VAO_DT,
                                                    "Ngươi phải có ít nhất 2 đồng đội cùng bang đứng gần mới có thể\nvào\n"
                                                    + "tuy nhiên ta khuyên ngươi nên đi cùng với 3-4 người để khỏi chết.\n"
                                                    + "Hahaha.",
                                                    "OK", "Hướng\ndẫn\nthêm");
                                        }
                                    }
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 27) {
                                    switch (player.iDMark.getIndexMenu()) {
                                        case ConstNpc.MENU_KHONG_CHO_VAO_DT:
                                            if (select == 1) {
                                                NpcService.gI().createTutorial(player, this.avartar,
                                                        ConstNpc.HUONG_DAN_DOANH_TRAI);
                                            }
                                            break;
                                        case ConstNpc.MENU_CHO_VAO_DT:
                                            switch (select) {
                                                case 0:
                                                    DoanhTraiService.gI().openDoanhTrai(player);
                                                    break;
                                                case 2:
                                                    NpcService.gI().createTutorial(player, this.avartar,
                                                            ConstNpc.HUONG_DAN_DOANH_TRAI);
                                                    break;
                                            }
                                            break;
                                        case ConstNpc.MENU_VAO_DT:
                                            switch (select) {
                                                case 0:
                                                    ChangeMapService.gI().changeMap(player, 53, 0, 35, 432);
                                                    break;
                                                case 2:
                                                    NpcService.gI().createTutorial(player, this.avartar,
                                                            ConstNpc.HUONG_DAN_DOANH_TRAI);
                                                    break;
                                            }
                                            break;
                                        default:
                                            break;
                                    }
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.QUA_TRUNG:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {

                        private final int COST_AP_TRUNG_NHANH = 1000000000;

                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == (21 + player.gender)) {
                                    player.mabuEgg.sendMabuEgg();
                                    if (player.mabuEgg.getSecondDone() != 0) {
                                        this.createOtherMenu(player, ConstNpc.CAN_NOT_OPEN_EGG, "Bư bư bư...",
                                                "Hủy bỏ\ntrứng",
                                                "Ấp nhanh\n" + Util.numberToMoney(COST_AP_TRUNG_NHANH) + " vàng", "Đóng");
                                    } else {
                                        this.createOtherMenu(player, ConstNpc.CAN_OPEN_EGG, "Bư bư bư...", "Nở",
                                                "Hủy bỏ\ntrứng", "Đóng");
                                    }
                                }
//                                if (this.mapId == 104) {
//                                    player.egglinhthu.sendEggLinhThu();
//                                    if (player.egglinhthu.getSecondDone() > 0) {
//                                        this.createOtherMenu(player, ConstNpc.CAN_NOT_OPEN_EGG, "Burk Burk...",
//                                                "Hủy bỏ\ntrứng", "Ấp nhanh\n", "Đóng");
//                                    } else {
//                                        this.createOtherMenu(player, ConstNpc.CAN_OPEN_EGG, "Burk Burk...", "Nở", "Hủy bỏ\ntrứng", "Đóng");
//                                    }
//                                }
                                if (this.mapId == 154) {
                                    player.billEgg.sendBillEgg();
                                    if (player.billEgg.getSecondDone() != 0) {
                                        this.createOtherMenu(player, ConstNpc.CAN_NOT_OPEN_EGG, "Gruuu Gruuu Gruuu...",
                                                "Hủy bỏ\ntrứng",
                                                "Ấp nhanh\n1 Gậy thượng đế", "Đóng");
                                    } else {
                                        this.createOtherMenu(player, ConstNpc.CAN_OPEN_EGG, "Gruuu Gruuu Gruuu...", "Nở",
                                                "Hủy bỏ\ntrứng", "Đóng");
                                    }
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == (21 + player.gender)) {
                                    switch (player.iDMark.getIndexMenu()) {
                                        case ConstNpc.CAN_NOT_OPEN_EGG:
                                            if (select == 0) {
                                                this.createOtherMenu(player, ConstNpc.CONFIRM_DESTROY_EGG,
                                                        "Bạn có chắc chắn muốn hủy bỏ trứng Mabư?", "Đồng ý", "Từ chối");
                                            } else if (select == 1) {
                                                if (player.inventory.gold >= COST_AP_TRUNG_NHANH) {
                                                    player.inventory.gold -= COST_AP_TRUNG_NHANH;
                                                    player.mabuEgg.timeDone = 0;
                                                    Service.getInstance().sendMoney(player);
                                                    player.mabuEgg.sendMabuEgg();
                                                } else {
                                                    Service.getInstance().sendThongBao(player,
                                                            "Bạn không đủ vàng để thực hiện, còn thiếu "
                                                            + Util.numberToMoney(
                                                                    (COST_AP_TRUNG_NHANH - player.inventory.gold))
                                                            + " vàng");
                                                }
                                            }
                                            break;
                                        case ConstNpc.CAN_OPEN_EGG:
                                            switch (select) {
                                                case 0:
                                                    this.createOtherMenu(player, ConstNpc.CONFIRM_OPEN_EGG,
                                                            "Bạn có chắc chắn cho trứng nở?\n"
                                                            + "Đệ tử của bạn sẽ được thay thế bằng đệ Mabư",
                                                            "Đệ mabư\nTrái Đất", "Đệ mabư\nNamếc", "Đệ mabư\nXayda",
                                                            "Từ chối");
                                                    break;
                                                case 1:
                                                    this.createOtherMenu(player, ConstNpc.CONFIRM_DESTROY_EGG,
                                                            "Bạn có chắc chắn muốn hủy bỏ trứng Mabư?", "Đồng ý",
                                                            "Từ chối");
                                                    break;
                                            }
                                            break;
                                        case ConstNpc.CONFIRM_OPEN_EGG:
                                            switch (select) {
                                                case 0:
                                                    player.mabuEgg.openEgg(ConstPlayer.TRAI_DAT);
                                                    break;
                                                case 1:
                                                    player.mabuEgg.openEgg(ConstPlayer.NAMEC);
                                                    break;
                                                case 2:
                                                    player.mabuEgg.openEgg(ConstPlayer.XAYDA);
                                                    break;
                                                default:
                                                    break;
                                            }
                                            break;
                                        case ConstNpc.CONFIRM_DESTROY_EGG:
                                            if (select == 0) {
                                                player.mabuEgg.destroyEgg();
                                            }
                                            break;
                                    }
                                }
                                if (this.mapId == 154) {
                                    switch (player.iDMark.getIndexMenu()) {
                                        case ConstNpc.CAN_NOT_OPEN_EGG:
                                            switch (select) {
                                                case 0:
                                                    this.createOtherMenu(player, ConstNpc.CONFIRM_DESTROY_EGG,
                                                            "Bạn có chắc chắn muốn hủy bỏ trứng Bill?", "Đồng ý", "Từ chối");
                                                    break;
                                                case 1:
                                                    Item gayWhist = InventoryService.gI().findItem(player.inventory.itemsBag, 1231);
                                                    if (gayWhist != null) {
                                                        InventoryService.gI().subQuantityItemsBag(player, gayWhist, 1);
                                                        player.billEgg.timeDone = 0;
                                                        InventoryService.gI().sendItemBags(player);
                                                        player.billEgg.sendBillEgg();
                                                    } else {
                                                        Service.getInstance().sendThongBao(player, "Gay whist cua may dau?");
                                                    }
                                                    break;
                                                default:
                                                    break;
                                            }
                                            break;
                                        case ConstNpc.CAN_OPEN_EGG:
                                            switch (select) {
                                                case 0:
                                                    this.createOtherMenu(player, ConstNpc.CONFIRM_OPEN_EGG,
                                                            "Bạn có chắc chắn cho trứng nở?\n"
                                                            + "Đệ tử của bạn sẽ được thay thế bằng đệ Bill",
                                                            "Đệ beerus\nTrái Đất", "Đệ beerus\nNamếc", "Đệ beerus\nXayda",
                                                            "Từ chối");
                                                    break;
                                                case 1:
                                                    this.createOtherMenu(player, ConstNpc.CONFIRM_DESTROY_EGG,
                                                            "Bạn có chắc chắn muốn hủy bỏ trứng Bill?", "Đồng ý",
                                                            "Từ chối");
                                                    break;
                                            }
                                            break;
                                        case ConstNpc.CONFIRM_OPEN_EGG:
                                            switch (select) {
                                                case 0:
                                                    player.billEgg.openEggBill(ConstPlayer.TRAI_DAT);
                                                    break;
                                                case 1:
                                                    player.billEgg.openEggBill(ConstPlayer.NAMEC);
                                                    break;
                                                case 2:
                                                    player.billEgg.openEggBill(ConstPlayer.XAYDA);
                                                    break;
                                                default:
                                                    break;
                                            }
                                            break;
                                        case ConstNpc.CONFIRM_DESTROY_EGG:
                                            if (select == 0) {
                                                player.billEgg.destroyEggBill();
                                            }
                                            break;
                                    }
                                }
                            }
                        }
                    };
                    break;
//                case ConstNpc.QUOC_VUONG:
//                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
//
//                        @Override
//                        public void openBaseMenu(Player player) {
//                            this.createOtherMenu(player, ConstNpc.BASE_MENU,
//                                    "Con muốn nâng giới hạn sức mạnh cho bản thân?", "Bản thân",
//                                    "Từ chối");
//                        }
//
//                        @Override
//                        public void confirmMenu(Player player, int select) {
//                            if (canOpenNpc(player)) {
//                                if (player.iDMark.isBaseMenu()) {
//                                    switch (select) {
//                                        case 0:
//                                            if (player.nPoint.limitPower < NPoint.MAX_LIMIT) {
//                                                this.createOtherMenu(player, ConstNpc.OPEN_POWER_MYSEFT,
//                                                        "Ta sẽ truền năng lượng giúp con mở giới hạn sức mạnh của bản thân lên "
//                                                        + Util.numberToMoney(player.nPoint.getPowerNextLimit()),
//                                                        "Nâng\ngiới hạn\nsức mạnh",
//                                                        "Nâng ngay\n"
//                                                        + Util.numberToMoney(
//                                                                OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER)
//                                                        + " vàng",
//                                                        "Đóng");
//                                            } else {
//                                                this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
//                                                        "Sức mạnh của con đã đạt tới giới hạn", "Đóng");
//                                            }
//                                            break;
//                                    }
//                                } else if (player.iDMark.getIndexMenu() == ConstNpc.OPEN_POWER_MYSEFT) {
//                                    switch (select) {
//                                        case 0:
//                                            OpenPowerService.gI().openPowerBasic(player);
//                                            break;
//                                        case 1:
//                                            if (player.inventory.gold >= OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER) {
//                                                if (OpenPowerService.gI().openPowerSpeed(player)) {
//                                                    player.inventory.gold -= OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER;
//                                                    Service.getInstance().sendMoney(player);
//                                                }
//                                            } else {
//                                                Service.getInstance().sendThongBao(player,
//                                                        "Bạn không đủ vàng để mở, còn thiếu " + Util.numberToMoney(
//                                                                (OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER
//                                                                - player.inventory.gold))
//                                                        + " vàng");
//                                            }
//                                            break;
//                                    }
//                                }
//                            }
//                        }
//                    };
//                    break;
                case ConstNpc.TO_SU_KAIO:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {

                        @Override
                        public void openBaseMenu(Player player) {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                    "Con muốn nâng giới hạn sức mạnh cho đệ tử?", "Đệ tử",
                                    "Từ chối");
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (player.iDMark.isBaseMenu()) {
                                    switch (select) {
                                        case 0:
                                            if (player.pet != null) {
                                                if (player.pet.nPoint.limitPower < NPoint.MAX_LIMIT) {
                                                    this.createOtherMenu(player, ConstNpc.OPEN_POWER_PET,
                                                            "Ta sẽ truyền năng lượng giúp con mở giới hạn sức mạnh của đệ tử lên "
                                                            + Util.numberToMoney(
                                                                    player.pet.nPoint.getPowerNextLimit()),
                                                            "Nâng ngay\n" + Util.numberToMoney(
                                                                    OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER)
                                                            + " vàng",
                                                            "Đóng");
                                                } else {
                                                    this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                                            "Sức mạnh của đệ con đã đạt tới giới hạn", "Đóng");
                                                }
                                            } else {
                                                Service.getInstance().sendThongBao(player, "Không thể thực hiện");
                                            }
                                            // giới hạn đệ tử
                                            break;
                                    }
                                } else if (player.iDMark.getIndexMenu() == ConstNpc.OPEN_POWER_PET) {
                                    if (select == 0) {
                                        if (player.inventory.gold >= OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER) {
                                            if (OpenPowerService.gI().openPowerSpeed(player.pet)) {
                                                player.inventory.gold -= OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER;
                                                Service.getInstance().sendMoney(player);
                                            }
                                        } else {
                                            Service.getInstance().sendThongBao(player,
                                                    "Bạn không đủ vàng để mở, còn thiếu " + Util
                                                            .numberToMoney((OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER
                                                                    - player.inventory.gold))
                                                    + " vàng");
                                        }
                                    }
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.BUNMA_TL:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Nhóc muốn mua gì nào?",
                                            "Cửa hàng", "Đóng");
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (player.iDMark.isBaseMenu()) {
                                    if (select == 0) {
                                        ShopService.gI().openShopNormal(player, this, ConstNpc.SHOP_BUNMA_TL_0, 0,
                                                player.gender);
                                    }
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.RONG_OMEGA:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                BlackBallWar.gI().setTime();
                                if (this.mapId == 24 || this.mapId == 25 || this.mapId == 26) {
                                    try {
                                        long now = System.currentTimeMillis();
                                        if (now > BlackBallWar.TIME_OPEN && now < BlackBallWar.TIME_CLOSE) {
                                            this.createOtherMenu(player, ConstNpc.MENU_OPEN_BDW,
                                                    "Đường đến với ngọc rồng sao đen đã mở, "
                                                    + "ngươi có muốn tham gia không?",
                                                    "Hướng dẫn\nthêm", "Tham gia", "Từ chối");
                                        } else {
                                            String[] optionRewards = new String[7];
                                            int index = 0;
                                            for (int i = 0; i < 7; i++) {
                                                if (player.rewardBlackBall.timeOutOfDateReward[i] > System
                                                        .currentTimeMillis()) {
                                                    optionRewards[index] = "Nhận thưởng\n" + (i + 1) + " sao";
                                                    index++;
                                                }
                                            }
                                            if (index != 0) {
                                                String[] options = new String[index + 1];
                                                for (int i = 0; i < index; i++) {
                                                    options[i] = optionRewards[i];
                                                }
                                                options[options.length - 1] = "Từ chối";
                                                this.createOtherMenu(player, ConstNpc.MENU_REWARD_BDW,
                                                        "Ngươi có một vài phần thưởng ngọc " + "rồng sao đen đây!",
                                                        options);
                                            } else {
                                                this.createOtherMenu(player, ConstNpc.MENU_NOT_OPEN_BDW,
                                                        "Ta có thể giúp gì cho ngươi?", "Hướng dẫn", "Từ chối");
                                            }
                                        }
                                    } catch (Exception ex) {
                                        Log.error("Lỗi mở menu rồng Omega");
                                    }
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                switch (player.iDMark.getIndexMenu()) {
                                    case ConstNpc.MENU_REWARD_BDW:
                                        player.rewardBlackBall.getRewardSelect((byte) select);
                                        break;
                                    case ConstNpc.MENU_OPEN_BDW:
                                        if (select == 0) {
                                            NpcService.gI().createTutorial(player, this.avartar,
                                                    ConstNpc.HUONG_DAN_BLACK_BALL_WAR);
                                        } else if (select == 1) {
                                            player.iDMark.setTypeChangeMap(ConstMap.CHANGE_BLACK_BALL);
                                            ChangeMapService.gI().openChangeMapTab(player);
                                        }
                                        break;
                                    case ConstNpc.MENU_NOT_OPEN_BDW:
                                        if (select == 0) {
                                            NpcService.gI().createTutorial(player, this.avartar,
                                                    ConstNpc.HUONG_DAN_BLACK_BALL_WAR);
                                        }
                                        break;
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.RONG_1S:
                case ConstNpc.RONG_2S:
                case ConstNpc.RONG_3S:
                case ConstNpc.RONG_4S:
                case ConstNpc.RONG_5S:
                case ConstNpc.RONG_6S:
                case ConstNpc.RONG_7S:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                if (player.isHoldBlackBall) {
                                    this.createOtherMenu(player, ConstNpc.MENU_PHU_HP, "Ta có thể giúp gì cho ngươi?",
                                            "Phù hộ", "Từ chối");
                                } else {
                                    this.createOtherMenu(player, ConstNpc.MENU_OPTION_GO_HOME,
                                            "Ta có thể giúp gì cho ngươi?", "Về nhà", "Từ chối");
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (player.iDMark.getIndexMenu() == ConstNpc.MENU_PHU_HP) {
                                    if (select == 0) {
                                        this.createOtherMenu(player, ConstNpc.MENU_OPTION_PHU_HP,
                                                "Ta sẽ giúp ngươi tăng HP lên mức kinh hoàng, ngươi chọn đi",
                                                "x3 HP\n" + Util.numberToMoney(BlackBallWar.COST_X3) + " vàng",
                                                "x5 HP\n" + Util.numberToMoney(BlackBallWar.COST_X5) + " vàng",
                                                "x7 HP\n" + Util.numberToMoney(BlackBallWar.COST_X7) + " vàng",
                                                "Từ chối");
                                    }
                                } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPTION_GO_HOME) {
                                    if (select == 0) {
                                        ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, -1, 250);
                                    }
                                } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPTION_PHU_HP) {
                                    switch (select) {
                                        case 0:
                                            BlackBallWar.gI().xHPKI(player, BlackBallWar.X3);
                                            break;
                                        case 1:
                                            BlackBallWar.gI().xHPKI(player, BlackBallWar.X5);
                                            break;
                                        case 2:
                                            BlackBallWar.gI().xHPKI(player, BlackBallWar.X7);
                                            break;
                                        case 3:
                                            this.npcChat(player, "Để ta xem ngươi trụ được bao lâu");
                                            break;
                                    }
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.BILL:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 5) {
                                    createOtherMenu(player, ConstNpc.BASE_MENU,
                                            "|8|Chúc Bạn Một Ngày Vui Vẻ\n"
                                            + "|4|Thái Âm Thánh Thể: " + player.TamkjllDauLaDaiLuc[0] + " Sở Hữu"
                                            + "\n|4|Bất Diệt Thánh Thể: " + player.TamkjllDauLaDaiLuc[1] + " Sở Hữu "
                                            + "\n|4|Vô Địch Thánh Thể: " + player.TamkjllDauLaDaiLuc[2] + " Sở Hữu "
                                            + "\n|4|Ma Đạo Thánh Thể: " + player.TamkjllDauLaDaiLuc[3] + " Sở Hữu "
                                            + "\n|4|Ngộ Đạo Thánh Thể: " + player.TamkjllDauLaDaiLuc[4] + " Sở Hữu "
                                            + "\n|4|Đại Đạo Thần Thể: " + player.TamkjllDauLaDaiLuc[5] + " Sở Hữu "
                                            + "\n|4|Hỗn Độn Thần Thể: " + player.TamkjllDauLaDaiLuc[6] + " Sở Hữu ",
                                            "Thông Tin\nThánh Thể",
                                            "Thông Tin \nThần Khí\nSở Hữu",
                                            "Truy Tìm \nThần Khí",
                                            "Nâng Cấp\n Thần Khí",
                                            "Thông tin\ncác loại\n Thần Khí"
                                    );
                                } else {
                                    createOtherMenu(player, ConstNpc.BASE_MENU,
                                            "|6|Hãy Up Thức Ăn Mang Tới Cho Ta Đổi Đồ Hủy Diệt...\n"
                                            + "|4|Bằng Cách Farm Quái Tại Coler\n"
                                            + "|4|Mang Đến Cho Ta 9999 Thức Ăn, Ta Cho 1 Đồ Hủy Diệt Cực VIP\n"
                                            + "|4|Tất Cả Chỉ Số Là 10 Triệu SD, HP, KI, Tùy Món Thuộc Tính\n"
                                            + "|7|" + "Chúc Bạn Chơi Game Vui Vẻ...\n",
                                            "Shop\nHủy Diệt"
                                    );
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 48 || this.mapId == 22 || this.mapId == 23) {
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {
                                            case 0:
                                                if (player.getSession().tongnap < 0) {
                                                    Service.getInstance().sendThongBao(player, "Đang Bảo Trì Vài Năm Rồi");
                                                    return;
                                                }
                                                ShopService.gI().openShopSpecial(player, this, ConstNpc.SHOP_BILL_HUY_DIET_0, 0, -1);
                                                break;

                                            case 1: //tiệm hồng ngọc
                                                if (player.getSession().tongnap < 200000) {
                                                    Service.getInstance().sendThongBao(player, "cần ít nhất Nạp Tích Lũy Tối Thiểu 200k");
                                                    return;
                                                }
                                                ShopService.gI().openShopSpecial(player, this, ConstNpc.naptien2, 2, 3);
                                                break;
                                            case 2: //tiệm hồng ngọc
                                                if (player.getSession().tongnap < 500000) {
                                                    Service.getInstance().sendThongBao(player, "cần ít nhất Nạp Tích Lũy Tối Thiểu 500k");
                                                    return;
                                                }
                                                ShopService.gI().openShopSpecial(player, this, ConstNpc.naptien3, 3, 3);
                                                break;
                                            case 3://tiệm hồng ngọc
                                                if (player.getSession().tongnap < 1000000) {
                                                    Service.getInstance().sendThongBao(player, "cần ít nhất Nạp Tích Lũy Tối Thiểu 1000k");
                                                    return;
                                                }
                                                ShopService.gI().openShopSpecial(player, this, ConstNpc.naptien4, 4, 3);
                                                break;
                                            case 4: //tiệm hồng ngọc
                                                if (player.getSession().tongnap < 2000000) {
                                                    Service.getInstance().sendThongBao(player, "cần ít nhất Nạp Tích Lũy Tối Thiểu 2000k");
                                                    return;
                                                }
                                                ShopService.gI().openShopSpecial(player, this, ConstNpc.naptien5, 5, 3);
                                                break;
                                            case 5: //tiệm hồng ngọc
                                                if (player.getSession().tongnap < 3000000) {
                                                    Service.getInstance().sendThongBao(player, "cần ít nhất Nạp Tích Lũy Tối Thiểu 3000k");
                                                    return;
                                                }
                                                ShopService.gI().openShopSpecial(player, this, ConstNpc.naptien6, 6, 3);
                                                break;

                                            case 6: //tiệm hồng ngọc
                                                if (player.getSession().tongnap < 5000000) {
                                                    Service.getInstance().sendThongBao(player, "cần ít nhất Nạp Tích Lũy Tối Thiểu 5000k");
                                                    return;
                                                }
                                                ShopService.gI().openShopSpecial(player, this, ConstNpc.naptien7, 7, 3);
                                                break;
                                            case 7: //tiệm hồng ngọc
                                                if (player.getSession().tongnap < 7000000) {
                                                    Service.getInstance().sendThongBao(player, "cần ít nhất Nạp Tích Lũy Tối Thiểu 7000k");
                                                    return;
                                                }
                                                ShopService.gI().openShopSpecial(player, this, ConstNpc.naptien8, 8, 3);
                                                break;
                                            case 8: //tiệm hồng ngọc
                                                if (player.getSession().tongnap < 10000000) {
                                                    Service.getInstance().sendThongBao(player, "cần ít nhất Nạp Tích Lũy Tối Thiểu 10000k");
                                                    return;
                                                }
                                                ShopService.gI().openShopSpecial(player, this, ConstNpc.naptien9, 9, 3);
                                                break;
                                            case 9: //tiệm hồng ngọc
                                                if (player.getSession().tongnap < 12000000) {
                                                    Service.getInstance().sendThongBao(player, "cần ít nhất Nạp Tích Lũy Tối Thiểu 12000k");
                                                    return;
                                                }
                                                ShopService.gI().openShopSpecial(player, this, ConstNpc.naptien10, 10, 3);
                                                break;
                                            case 10: //tiệm hồng ngọc
                                                if (player.getSession().tongnap < 15000000) {
                                                    Service.getInstance().sendThongBao(player, "cần ít nhất Nạp Tích Lũy Tối Thiểu 15000k");
                                                    return;
                                                }
                                                ShopService.gI().openShopSpecial(player, this, ConstNpc.naptien11, 11, 3);
                                                break;
                                            case 11: //tiệm hồng ngọc
                                                if (player.getSession().tongnap < 17000000) {
                                                    Service.getInstance().sendThongBao(player, "cần ít nhất Nạp Tích Lũy Tối Thiểu 17000k");
                                                    return;
                                                }
                                                ShopService.gI().openShopSpecial(player, this, ConstNpc.naptien12, 12, 3);
                                                break;
                                            case 12: //tiệm hồng ngọc
                                                if (player.getSession().tongnap < 20000000) {
                                                    Service.getInstance().sendThongBao(player, "cần ít nhất Nạp Tích Lũy Tối Thiểu 20000k");
                                                    return;
                                                }
                                                ShopService.gI().openShopSpecial(player, this, ConstNpc.naptien13, 13, 3);
                                                break;
                                            case 13: //tiệm hồng ngọc
                                                if (player.getSession().tongnap < 23000000) {
                                                    Service.getInstance().sendThongBao(player, "cần ít nhất Nạp Tích Lũy Tối Thiểu 23000k");
                                                    return;
                                                }
                                                ShopService.gI().openShopSpecial(player, this, ConstNpc.naptien14, 14, 3);
                                                break;
                                            case 14: //tiệm hồng ngọc
                                                if (player.getSession().tongnap < 25000000) {
                                                    Service.getInstance().sendThongBao(player, "cần ít nhất Nạp Tích Lũy Tối Thiểu 25000k");
                                                    return;
                                                }
                                                ShopService.gI().openShopSpecial(player, this, ConstNpc.naptien15, 15, 3);
                                                break;

                                        }

                                    }
                                } else if (this.mapId == 5) {
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {
                                            case 0:
                                                createOtherMenu(player, ConstNpc.BASE_MENU,
                                                        "Chúc Bạn Một Ngày Vui Vẻ\n"
                                                        + "Thái Âm Thánh Thể: " + player.TamkjllDauLaDaiLuc[0] + " Sở Hữu"
                                                        + "\nBất Diệt Thánh Thể: " + player.TamkjllDauLaDaiLuc[1] + " Sở Hữu "
                                                        + "\nVô Địch Thánh Thể: " + player.TamkjllDauLaDaiLuc[2] + " Sở Hữu "
                                                        + "\nMa Đạo Thánh Thể: " + player.TamkjllDauLaDaiLuc[3] + " Sở Hữu "
                                                        + "\nNgộ Đạo Thánh Thể: " + player.TamkjllDauLaDaiLuc[4] + " Sở Hữu "
                                                        + "\nĐại Đạo Thần Thể: " + player.TamkjllDauLaDaiLuc[5] + " Sở Hữu "
                                                        + "\nHỗn Độn Thần Thể: " + player.TamkjllDauLaDaiLuc[6] + " Sở Hữu ",
                                                        "Thông Tin\nThánh Thể",
                                                        "Thông Tin \nThần Khí\nSở Hữu",
                                                        "Truy Tìm \n Thần Khí",
                                                        "Nâng Cấp\n Thần Khí",
                                                        "Thông tin\ncác loại\nThần Khí"
                                                );
                                                break;
                                            case 1:
                                                String hcnhan = "";
                                                if (player.TamkjllDauLaDaiLuc[9] == 1) {
                                                    hcnhan += player.TamkjllNameHoncot(1) + ":\n";
                                                    hcnhan += "+Tăng: " + player.TamkjllDauLaDaiLuc[10] + " % chỉ số\n";
                                                    hcnhan += "+giảm: "
                                                            + (player.TamkjllDauLaDaiLuc[10] / 3 >= 20 ? 20
                                                                    : player.TamkjllDauLaDaiLuc[10] / 3)
                                                            + "% th\u1EDDi gian Skill đấm\n";
                                                }
                                                if (player.TamkjllDauLaDaiLuc[11] == 1) {
                                                    hcnhan += player.TamkjllNameHoncot(2) + ":\n";
                                                    hcnhan += "+Tăng: "
                                                            + (player.TamkjllDauLaDaiLuc[12] / 5 >= 20 ? 20
                                                                    : player.TamkjllDauLaDaiLuc[12] / 5)
                                                            + "% Khả năng up các loại exp cao cấp của thế giới này.\n";
                                                }
                                                if (player.TamkjllDauLaDaiLuc[13] == 1) {
                                                    hcnhan += player.TamkjllNameHoncot(3) + ":\n";
                                                    hcnhan += "+Giảm: " + (player.TamkjllDauLaDaiLuc[14] / 3 >= 80 ? 80
                                                            : player.TamkjllDauLaDaiLuc[14] / 3)
                                                            + "% ST nhận\n";
                                                    hcnhan += "+Có tỉ lệ x2 dame\n";
                                                }
                                                if (player.TamkjllDauLaDaiLuc[15] == 1) {
                                                    hcnhan += player.TamkjllNameHoncot(4) + ":\n";
                                                    hcnhan += "+Tăng: "
                                                            + Util.getFormatNumber(player.TamkjllDauLaDaiLuc[16] * 2369l)
                                                            + "SD\n";
                                                    hcnhan += "+Giảm: " + (player.TamkjllDauLaDaiLuc[16] / 2 >= 90 ? 90
                                                            : player.TamkjllDauLaDaiLuc[16] / 2)
                                                            + "% SD người ở gần\n";
                                                }
                                                if (player.TamkjllDauLaDaiLuc[17] == 1) {
                                                    hcnhan += player.TamkjllNameHoncot(5) + ":\n";
                                                    hcnhan += "Tăng: "
                                                            + Util.getFormatNumber(player.TamkjllDauLaDaiLuc[18] * 3569l)
                                                            + "HP.\n";
                                                    hcnhan += "+hồi phục: " + (player.TamkjllDauLaDaiLuc[18] / 3 >= 90 ? 90
                                                            : player.TamkjllDauLaDaiLuc[18] / 3)
                                                            + "% HP sau 3s\n";
                                                }
                                                if (player.TamkjllDauLaDaiLuc[19] == 1) {
                                                    hcnhan += player.TamkjllNameHoncot(6) + ":\n";
                                                    hcnhan += "+Đánh ST chuẩn: "
                                                            + Util.getFormatNumber(player.TamkjllDauLaDaiLuc[20] * 4)
                                                            + "SD\n";
                                                }
                                                createOtherMenu(player, ConstNpc.BASE_MENU,
                                                        "Chúc Bạn Một Ngày Vui Vẻ\n"
                                                        + hcnhan,
                                                        "Thông Tin\nThánh Thể",
                                                        "Thông Tin \nThần Khí\nSở Hữu",
                                                        "Truy Tìm \n Thần Khí",
                                                        "Nâng Cấp\n Thần Khí",
                                                        "Thông tin\ncác loại\n Thần Khí"
                                                );
                                                break;
                                            case 2:
                                                if (player.TamkjllDauLaDaiLuc[7] == 0) {
                                                    NpcService.gI().createMenuConMeo(player, ConstNpc.TamkjllTruytim, -1,
                                                            "Chúc Bạn Một Ngày Vui Vẻ\n"
                                                            + "Truy Tìm Bình Thường Bạn Cần 10tr Exp Tiên Bang.\n"
                                                            + "Nếu Bạn Thiếu May Mắn Nên Chọn 100% Thì Chỉ Mất 500M Exp Tiên Bang",
                                                            "Truy Tìm", "Truy Tìm 100%");
                                                } else {
                                                    String hcnhann = player
                                                            .TamkjllNameHoncot(Util.maxInt(player.TamkjllDauLaDaiLuc[7]))
                                                            + "\n";
                                                    if (player.TamkjllDauLaDaiLuc[7] == 1) {
                                                        hcnhann += "Tăng: " + player.TamkjllDauLaDaiLuc[8] + " % chỉ số\n";
                                                        hcnhann += "giảm: " + player.TamkjllDauLaDaiLuc[8] / 3
                                                                + " % thời gian Skill đấm, max 20%.\n";
                                                    }
                                                    if (player.TamkjllDauLaDaiLuc[7] == 2) {
                                                        hcnhann += "Tăng: " + player.TamkjllDauLaDaiLuc[8] / 50l
                                                                + "% Khả năng up các loại exp cao cấp của thế giới này.\n";
                                                    }
                                                    if (player.TamkjllDauLaDaiLuc[7] == 3) {
                                                        hcnhann += "Giảm: " + player.TamkjllDauLaDaiLuc[8] / 3l
                                                                + "% ST nhận.\n";
                                                        hcnhann += "Có tỉ lệ x2 dame.\n";
                                                    }
                                                    if (player.TamkjllDauLaDaiLuc[7] == 4) {
                                                        hcnhann += "Tăng: " + player.TamkjllDauLaDaiLuc[8] * 2369l
                                                                + "SD.\n";
                                                        hcnhann += "Giảm: " + player.TamkjllDauLaDaiLuc[8] / 2l
                                                                + "% SD người ở gần.\n";
                                                    }
                                                    if (player.TamkjllDauLaDaiLuc[7] == 5) {
                                                        hcnhann += "Tăng: " + player.TamkjllDauLaDaiLuc[8] * 3569l
                                                                + "HP.\n";
                                                        hcnhann += "hồi phục: " + player.TamkjllDauLaDaiLuc[8] / 3
                                                                + "% HP sau 3s\n";
                                                    }
                                                    if (player.TamkjllDauLaDaiLuc[7] == 6) {
                                                        hcnhann += "Đánh Sát Thương Chuẩn: " + player.TamkjllDauLaDaiLuc[8] * 4L
                                                                + "dame\n";
                                                    }
                                                    NpcService.gI().createMenuConMeo(player, ConstNpc.TamkjllTruytim, -1,
                                                            "|8|Chúc Bạn Một Ngày Vui Vẻ\n"
                                                            + "Thông Tin Bảo Vật\n"
                                                            + hcnhann
                                                            + "\nHãy Nghe Theo Con Tim Mách Bảo.",
                                                            "đóng",
                                                            "Hủy Thần Khí",
                                                            "Hấp thụ\n Thần Khí");
                                                }
                                                break;
                                            case 3:
                                                NpcService.gI().createMenuConMeo(player, ConstNpc.TamkjllNCHC, -1,
                                                        "Chúc Bạn Một Ngày Vui Vẻ\n"
                                                        + "Tiên Bang ít Nhất Đạt 1000 Cấp Tiên Bang Trở Lên",
                                                        player.TamkjllNameHoncot(1),
                                                        player.TamkjllNameHoncot(2),
                                                        player.TamkjllNameHoncot(3),
                                                        player.TamkjllNameHoncot(4),
                                                        player.TamkjllNameHoncot(5),
                                                        player.TamkjllNameHoncot(6));
                                                break;
                                            case 4:
                                                Service.getInstance().sendThongBaoFromAdmin(player, "|8|\nThông tin về Thần Khí:\n"
                                                        + "|4|Đá Nữ Oa:\nTăng: % chỉ số\n"
                                                        + "giảm: % thời gian Skill đấm, max 20%\n"
                                                        + "|4|Hiên Viên kiếm:\n"
                                                        + "Tăng: % Khả năng up các loại EXP cao cấp của thế giới này\n"
                                                        + "|4|Côn Lôn kính:\n"
                                                        + "Giảm: % ST nhận\n"
                                                        + "Có Tỉ Lệ x2 SD\n"
                                                        + "|4|Đàn Phục Hi:\n"
                                                        + "Tăng: SD\n"
                                                        + "Giảm: % SD người ở gần\n"
                                                        + "|4|Chuông Đông Hoàng:\n"
                                                        + "Tăng: HP\n"
                                                        + "Hồi Phục: % HP sau 3s\n"
                                                        + "|4|Bàn Cổ phù:\n"
                                                        + "Đánh Sát Thương Chuẩn: SD");
                                                break;
                                        }
                                    }

                                }
                            }
                        }
                    };
                    break;

                case ConstNpc.WHIS:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                switch (this.mapId) {
                                    case 48:
                                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Chưa tới giờ thi đấu, xem hướng dẫn để biết thêm chi tiết",
                                                "Hướng\ndẫn\nthêm", "Từ chối");
                                        break;
                                    case 5:
                                        this.createOtherMenu(player, ConstNpc.MENU_WHIS, "Ngươi muốn gì nào", "Nói chuyện", "Học\n Tuyệt kĩ");
                                        break;
                                    default:
                                        super.openBaseMenu(player);
                                        break;
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                switch (this.mapId) {
                                    case 48:
                                        break;
                                    case 5:
                                        switch (player.iDMark.getIndexMenu()) {
                                            case ConstNpc.MENU_WHIS:
                                                switch (select) {
                                                    case 0:
                                                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta sẽ giúp ngươi chế tạo trang bị Thiên Sứ!", "OK", "Đóng");
                                                        break;
                                                }
                                                break;
                                            case ConstNpc.BASE_MENU:
                                                switch (select) {
                                                    case 0:
                                                        CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_DO_TS);
                                                }
                                                break;
                                            case ConstNpc.MENU_NANG_CAP_DO_TS:
                                                switch (select) {
                                                    case 0:
                                                        CombineServiceNew.gI().startCombine(player);
                                                        break;
                                                }
                                                break;
                                        }
                                        break;
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.BO_MONG:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 47 || this.mapId == 84) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Xin chào, cậu muốn tôi giúp gì?",
                                            "Nhiệm vụ\nhàng ngày", "Từ chối");
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 47 || this.mapId == 84) {
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {
                                            case 0:
                                                if (player.playerTask.sideTask.template != null) {
                                                    String npcSay = "Nhiệm vụ hiện tại: "
                                                            + player.playerTask.sideTask.getName() + " ("
                                                            + player.playerTask.sideTask.getLevel() + ")"
                                                            + "\nHiện tại đã hoàn thành: "
                                                            + player.playerTask.sideTask.count + "/"
                                                            + player.playerTask.sideTask.maxCount + " ("
                                                            + player.playerTask.sideTask.getPercentProcess()
                                                            + "%)\nSố nhiệm vụ còn lại trong ngày: "
                                                            + player.playerTask.sideTask.leftTask + "/"
                                                            + ConstTask.MAX_SIDE_TASK;
                                                    this.createOtherMenu(player, ConstNpc.MENU_OPTION_PAY_SIDE_TASK,
                                                            npcSay, "Trả nhiệm\nvụ", "Hủy nhiệm\nvụ");
                                                } else {
                                                    this.createOtherMenu(player, ConstNpc.MENU_OPTION_LEVEL_SIDE_TASK,
                                                            "Tôi có vài nhiệm vụ theo cấp bậc, "
                                                            + "sức cậu có thể làm được cái nào?",
                                                            "Dễ", "Bình thường", "Khó", "Siêu khó", "Từ chối");
                                                }
                                                break;
//                                            case 1:
//                                                TaskService.gI().checkDoneAchivements(player);
//                                                TaskService.gI().sendAchivement(player);
//                                                break;
                                        }
                                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPTION_LEVEL_SIDE_TASK) {
                                        switch (select) {
                                            case 0:
                                            case 1:
                                            case 2:
                                            case 3:
                                                TaskService.gI().changeSideTask(player, (byte) select);
                                                break;
                                        }
                                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPTION_PAY_SIDE_TASK) {
                                        switch (select) {
                                            case 0:
                                                TaskService.gI().paySideTask(player);
                                                break;
                                            case 1:
                                                TaskService.gI().removeSideTask(player);
                                                break;
                                        }
                                    }
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.GOKU_SSJ:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                switch (this.mapId) {
                                    case 80:
                                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "|2|Cấp Khai Thác: " + player.TamkjllThomo
                                                + "\n|2|Exp Khai Thác: " + Util.getFormatNumber(player.TamkjllThomoExp),
                                                "hiệu ứng", "Nâng cấp", "Tới hành tinh\nYardart");
                                        break;
                                    case 131:
                                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                                "Xin chào, tôi có thể giúp gì cho cậu?", "Quay về", "Từ chối");
                                        break;
                                    default:
                                        super.openBaseMenu(player);
                                        break;
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                switch (player.iDMark.getIndexMenu()) {
                                    case ConstNpc.BASE_MENU:
                                        if (this.mapId == 80) {
                                            if (select == 0) {
                                                this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                                        "Tăng thêm vàng nhặt: " + player.TamkjllThomo * 100000
                                                        + "\nNgọc nhặt:" + player.TamkjllThomo * 50,
                                                        "Hiệu ứng", "Nâng cấp");
                                            }
                                            if (select == 1) {
                                                long exptm = player.TamkjllThomo;
                                                if (player.TamkjllThomoExp > 2000000 + exptm * 1000000) {
                                                    player.TamkjllThomoExp -= 2000000 + exptm * 1000000;
                                                    player.TamkjllThomo++;
                                                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                                            "Nâng cấp thành công.\n Cấp Khai Thác hiện tại của bạn là: "
                                                            + player.TamkjllThomo + "\n"
                                                            + "vàng nhặt từ : " + (player.TamkjllThomo * 100000 - 100000)
                                                            + " Lên: " + player.TamkjllThomo * 100000
                                                            + "\nNgọc nhặt từ : " + (player.TamkjllThomo * 50 - 50)
                                                            + " Lên: " + player.TamkjllThomo * 50,
                                                            "Hiệu Ứng", "Nâng Cấp");
                                                } else {
                                                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                                            "Bạn cần: " + ((2000000 + exptm * 1000000) - player.TamkjllThomoExp)
                                                            + "Exp Khai Thác nữa.",
                                                            "Hiệu Ứng", "Nâng Cấp");
                                                }
                                            }
                                            if (select == 2) {
                                                ChangeMapService.gI().changeMapBySpaceShip(player, 131, -1, 940);
                                                break;
                                            }
                                        } else if (this.mapId == 131) {
                                            if (select == 0) {
                                                ChangeMapService.gI().changeMapBySpaceShip(player, 80, -1, 870);
                                            }
                                        }
                                        break;
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.GOKU_SSJ_:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 5) {
                                    Item biKiep = InventoryService.gI().findItem(player.inventory.itemsBag, 590);
                                    int soLuong = 0;
                                    if (biKiep != null) {
                                        soLuong = biKiep.quantity;
                                    }
                                    if (soLuong >= 10000) {
                                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Bạn đang có " + soLuong
                                                + " bí kiếp.\n"
                                                + "Hãy kiếm đủ 10000 bí kiếp tôi sẽ dạy bạn cách dịch chuyển tức thời của người Yardart\n"
                                                + "với ngẫu nhiên 1 cải trang dịch chuyển vip",
                                                "Học dịch\nchuyển", "Đóng");
                                    } else {
                                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Bạn đang có " + soLuong
                                                + " bí kiếp.\n"
                                                + "Hãy kiếm đủ 10000 bí kiếp tôi sẽ dạy bạn cách dịch chuyển tức thời của người Yardart\n"
                                                + "Hãy Săn Boss Tân Binh kiếm bí kíp\n"
                                                + "Chỉ Số Cải Trang Dịch Chuyển 30 Ngày\n"
                                                + "+1000% Sức Đánh Random\n"
                                                + "+1000% HP Random\n"
                                                + "+1000% KI Random",
                                                "Đóng");
                                    }
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 5) {
                                    Item biKiep = InventoryService.gI().findItem(player.inventory.itemsBag, 590);
                                    int soLuong = 0;
                                    if (biKiep != null) {
                                        soLuong = biKiep.quantity;
                                    }
                                    if (soLuong >= 10000 && InventoryService.gI().getCountEmptyBag(player) > 0) {
                                        Item yardart = ItemService.gI().createNewItem((short) (player.gender + 592));
                                        yardart.itemOptions.add(new ItemOption(174, Util.nextInt(1999, 2024)));
                                        yardart.itemOptions.add(new ItemOption(50, Util.nextInt(0, 1000)));
                                        yardart.itemOptions.add(new ItemOption(77, Util.nextInt(0, 1000)));
                                        yardart.itemOptions.add(new ItemOption(103, Util.nextInt(0, 1000)));
                                        yardart.itemOptions.add(new ItemOption(108, Util.nextInt(0, 20)));
                                        //  yardart.itemOptions.add(new ItemOption(30, Util.nextInt(20, 30)));
                                        InventoryService.gI().addItemBag(player, yardart, 0);
                                        InventoryService.gI().subQuantityItemsBag(player, biKiep, 10000);
                                        InventoryService.gI().sendItemBags(player);
                                        Service.getInstance().sendThongBao(player,
                                                "Bạn vừa nhận được trang phục tộc Yardart");
                                    }
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.GHI_DANH:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        String[] menuselect = new String[]{};

                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                switch (this.mapId) {
                                    case ConstMap.DAI_HOI_VO_THUAT:
                                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                                "Chào mừng bạn đến với đại hội võ thuật", "Đại Hội\nVõ Thuật\nLần Thứ\n23");
                                        break;
                                    case ConstMap.DAI_HOI_VO_THUAT_129:
                                        int goldchallenge = 10000;
                                        if (player.levelWoodChest == 0) {
                                            menuselect = new String[]{
                                                "Thi đấu\n" + goldchallenge + " Lưu Ly",
                                                "Về\nĐại Hội\nVõ Thuật"};
                                        } else {
                                            menuselect = new String[]{
                                                "Thi đấu\n" + goldchallenge + "Lưu Ly\nMở Rương 11 Để Kiếm 100k Lưu Ly",
                                                "Nhận thưởng\nRương cấp\n" + player.levelWoodChest,
                                                "Về\nĐại Hội\nVõ Thuật"};
                                        }
                                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                                "Đại hội võ thuật lần thứ 23\nDiễn ra bất kể ngày đêm,ngày nghỉ ngày lễ\nPhần thưởng vô cùng quý giá\nNhanh chóng tham gia nào",
                                                menuselect, "Từ chối");
                                        break;
                                    default:
                                        super.openBaseMenu(player);
                                        break;
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                switch (player.iDMark.getIndexMenu()) {
                                    case ConstNpc.BASE_MENU:
                                        if (this.mapId == ConstMap.DAI_HOI_VO_THUAT) {
                                            switch (select) {
                                                case 0:
                                                    ChangeMapService.gI().changeMapNonSpaceship(player,
                                                            ConstMap.DAI_HOI_VO_THUAT_129, player.location.x, 360);
                                                    break;
                                            }
                                        } else if (this.mapId == ConstMap.DAI_HOI_VO_THUAT_129) {
                                            int goldchallenge = 10000;
                                            Item thoivang = InventoryService.gI().findItemBagByTemp(player, 1404);
                                            if (player.levelWoodChest == 0) {
                                                switch (select) {
                                                    case 0:
                                                        if (InventoryService.gI().finditemWoodChest(player)) {
                                                            if (thoivang != null) {
                                                                if (thoivang.quantity >= goldchallenge) {
                                                                    MartialCongressService.gI().startChallenge(player);
//                                                                player.inventory.subGold(goldchallenge);
                                                                    InventoryService.gI().subQuantityItemsBag(player, thoivang, goldchallenge);
                                                                    PlayerService.gI().sendInfoHpMpMoney(player);
                                                                    player.goldChallenge += 1;
                                                                } else {
                                                                    Service.getInstance().sendThongBao(player,
                                                                            "Không đủ Lưu Ly, còn thiếu "
                                                                            + (goldchallenge
                                                                            - thoivang.quantity)
                                                                            + "Lưu Ly");
                                                                }
                                                            } else {
                                                                Service.getInstance().sendThongBao(player, "Bạn không có đủ Lưu Ly");
                                                            }
                                                        } else {
                                                            Service.getInstance().sendThongBao(player,
                                                                    "Hãy mở rương báu vật trước");
                                                        }
                                                        break;
                                                    case 1:
                                                        ChangeMapService.gI().changeMapNonSpaceship(player,
                                                                ConstMap.DAI_HOI_VO_THUAT, player.location.x, 336);
                                                        break;
                                                }
                                            } else {
                                                switch (select) {
                                                    case 0:
                                                        if (InventoryService.gI().finditemWoodChest(player)) {
                                                            if (thoivang != null) {
                                                                if (thoivang.quantity >= goldchallenge) {
                                                                    MartialCongressService.gI().startChallenge(player);
//                                                                player.inventory.subGold(goldchallenge);
                                                                    InventoryService.gI().subQuantityItemsBag(player, thoivang, goldchallenge);
                                                                    PlayerService.gI().sendInfoHpMpMoney(player);
                                                                    player.goldChallenge += 1;
                                                                } else {
                                                                    Service.getInstance().sendThongBao(player,
                                                                            "Không đủ Lưu Ly, còn thiếu "
                                                                            + (goldchallenge
                                                                            - thoivang.quantity)
                                                                            + "Lưu Ly");
                                                                }
                                                            } else {
                                                                Service.getInstance().sendThongBao(player, "Bạn không có đủ Lưu Ly");
                                                            }
                                                        } else {
                                                            Service.getInstance().sendThongBao(player,
                                                                    "Hãy mở rương báu vật trước");
                                                        }
                                                        break;
                                                    case 1:
                                                        if (!player.receivedWoodChest) {
                                                            if (InventoryService.gI().getCountEmptyBag(player) > 0) {
                                                                Item it = ItemService.gI()
                                                                        .createNewItem((short) ConstItem.RUONG_GO);
                                                                it.itemOptions
                                                                        .add(new ItemOption(72, player.levelWoodChest));
                                                                it.itemOptions.add(new ItemOption(30, 0));
                                                                it.createTime = System.currentTimeMillis();
                                                                InventoryService.gI().addItemBag(player, it, 0);
                                                                InventoryService.gI().sendItemBags(player);

                                                                player.receivedWoodChest = true;
                                                                player.levelWoodChest = 0;
                                                                Service.getInstance().sendThongBao(player,
                                                                        "Bạn nhận được rương gỗ");
                                                            } else {
                                                                this.npcChat(player, "Hành trang đã đầy");
                                                            }
                                                        } else {
                                                            Service.getInstance().sendThongBao(player,
                                                                    "Mỗi ngày chỉ có thể nhận rương báu 1 lần");
                                                        }
                                                        break;
                                                    case 2:
                                                        ChangeMapService.gI().changeMapNonSpaceship(player,
                                                                ConstMap.DAI_HOI_VO_THUAT, player.location.x, 336);
                                                        break;
                                                }
                                            }
                                        }
                                        break;
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.MI_NUONG:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        private int[] listMap = {5, 7, 8, 9, 10, 14, 15, 16, 17, 18, 19, 68, 69, 70, 71, 72, 73, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 73, 74, 75, 76, 77, 78,
                            92, 93, 94, 95, 96, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110};
                        private int mapHoTong;
                        private static int[] phiHoTong = {10000, 20000, 30000, 40000};
                        private static String[] phamChatHoTong = {"Lam", "Tím", "Đỏ", "Vàng"};

                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 0) {
                                    mapHoTong = listMap[Util.nextInt(listMap.length - 1)];
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ngươi muốn gì ở ta?\n"
                                            + "Ngươi Hãy Hộ Tống Ta Đến Nơi An Nghỉ Ta Sẽ Cho Ngươi Kho Báu !\n"
                                            + "Thu Gom Đủ Linh Hồn Để Đổi Trang Sức Cực Vip Haha",
                                            "Vận Tiêu", "Shop Tiêu", "Từ chối");
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                switch (player.iDMark.getIndexMenu()) {
                                    case ConstNpc.BASE_MENU:
                                        switch (select) {
                                            case 0:
                                                mapHoTong = listMap[Util.nextInt(listMap.length - 1)];
                                                this.createOtherMenu(player, ConstNpc.MENU_HO_TONG, "Đa vũ trụ song song"
                                                        + "\b|7|Con có muốn Vận Tiêu đến: " + MapService.gI().getMapById(mapHoTong).mapName + " không?",
                                                        "Phẩm Chất\nLam\n10k\nLưu LY", "Phẩm Chất\nTím\n20k\nLưu LY", "Phẩm Chất\nĐỏ\n30k\nLưu LY", "Phẩm Chất\nVàng\n40k\nLưu LY", "Từ chối");
                                                break;

                                            case 1:
                                                ShopService.gI().openShopSpecial(player, this, ConstNpc.vantieu, 0, 3);
                                                break;
                                        }
                                        break;
                                    case ConstNpc.MENU_HO_TONG:
                                        Item HoTong = InventoryService.gI().findItemBagByTemp(player, 1404);
                                        switch (select) {
                                            case 0:
                                                if (HoTong == null || HoTong.quantity < phiHoTong[select]) {
                                                    this.npcChat(player, "Bạn không đủ tiền phí!");
                                                } else {
                                                    InventoryService.gI().subQuantityItemsBag(player, HoTong, phiHoTong[select]);
                                                    InventoryService.gI().sendItemBags(player);
                                                    player.cFlag = (byte) Util.nextInt(1, 8);
                                                    Service.gI().changeFlag(player, player.cFlag);
                                                    Boss hoTong = new HoTong(Util.nextInt(100000, 1000000), player, phiHoTong[select], mapHoTong, phamChatHoTong[select]);
                                                    hoTong.joinMap();
                                                    ChangeMapService.gI().changeMap(hoTong, player.zone.map.mapId, player.zone.zoneId, player.location.x, player.location.y);
                                                    BossManager.gI().addBoss(hoTong);
                                                    ChatGlobalService.gI().chatVanTieu(player, "Tao đang vận tiêu tại " + player.zone.map.mapName);
                                                }
                                                break;
                                            case 1:
                                                if (HoTong == null || HoTong.quantity < phiHoTong[select]) {
                                                    this.npcChat(player, "Bạn không đủ tiền phí!");
                                                } else {
                                                    InventoryService.gI().subQuantityItemsBag(player, HoTong, phiHoTong[select]);
                                                    InventoryService.gI().sendItemBags(player);
                                                    player.cFlag = (byte) Util.nextInt(1, 8);
                                                    Service.gI().changeFlag(player, player.cFlag);
                                                    Boss hoTong = new HoTong(Util.nextInt(100000, 1000000), player, phiHoTong[select], mapHoTong, phamChatHoTong[select]);
                                                    hoTong.joinMap();
                                                    ChangeMapService.gI().changeMap(hoTong, player.zone.map.mapId, player.zone.zoneId, player.location.x, player.location.y);
                                                    BossManager.gI().addBoss(hoTong);
                                                    ChatGlobalService.gI().chatVanTieu(player, "Tao đang vận tiêu tại " + player.zone.map.mapName);
                                                }
                                                break;
                                            case 2:
                                                if (HoTong == null || HoTong.quantity < phiHoTong[select]) {
                                                    this.npcChat(player, "Bạn không đủ tiền phí!");
                                                } else {
                                                    InventoryService.gI().subQuantityItemsBag(player, HoTong, phiHoTong[select]);
                                                    InventoryService.gI().sendItemBags(player);
                                                    player.cFlag = (byte) Util.nextInt(1, 8);
                                                    Service.gI().changeFlag(player, player.cFlag);
                                                    Boss hoTong = new HoTong(Util.nextInt(100000, 1000000), player, phiHoTong[select], mapHoTong, phamChatHoTong[select]);
                                                    hoTong.joinMap();
                                                    ChangeMapService.gI().changeMap(hoTong, player.zone.map.mapId, player.zone.zoneId, player.location.x, player.location.y);
                                                    BossManager.gI().addBoss(hoTong);
                                                    ChatGlobalService.gI().chatVanTieu(player, "Tao đang vận tiêu tại " + player.zone.map.mapName);
                                                }
                                                break;
                                            case 3:
                                                if (HoTong == null || HoTong.quantity < phiHoTong[select]) {
                                                    this.npcChat(player, "Bạn không đủ tiền phí!");
                                                } else {
                                                    InventoryService.gI().subQuantityItemsBag(player, HoTong, phiHoTong[select]);
                                                    InventoryService.gI().sendItemBags(player);
                                                    player.cFlag = (byte) Util.nextInt(1, 8);
                                                    Service.gI().changeFlag(player, player.cFlag);
                                                    Boss hoTong = new HoTong(Util.nextInt(100000, 1000000), player, phiHoTong[select], mapHoTong, phamChatHoTong[select]);
                                                    hoTong.joinMap();
                                                    ChangeMapService.gI().changeMap(hoTong, player.zone.map.mapId, player.zone.zoneId, player.location.x, player.location.y);
                                                    BossManager.gI().addBoss(hoTong);
                                                    ChatGlobalService.gI().chatVanTieu(player, "Tao đang vận tiêu tại " + player.zone.map.mapName);
                                                }
                                                break;
                                        }
                                        break;
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.POTAGE:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 140) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Đa vũ trụ song song\b|7|Con muốn gọi con trong đa vũ trụ \b|1|Với giá 200tr vàng không?", "Gọi Boss\nNhân bản", "Từ chối");
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                switch (player.iDMark.getIndexMenu()) {
                                    case ConstNpc.BASE_MENU:
                                        switch (select) {
                                            case 0:
                                                Boss oldBossClone = BossManager.gI().findBossByName("Nhân Bản " + player.name);
                                                if (oldBossClone != null) {
                                                    this.npcChat(player, "Nhà ngươi hãy tiêu diệt Boss lúc trước gọi ra đã, con boss đó đang ở khu " + oldBossClone.zone.zoneId);
                                                    return;
                                                }
                                                if (player.inventory.gold < 200_000_000) {
                                                    this.npcChat(player, "Nhà ngươi không đủ 200 Triệu vàng ");
                                                    return;
                                                }
                                                List<Skill> skillList = new ArrayList<>();
                                                for (byte i = 0; i < player.playerSkill.skills.size(); i++) {
                                                    Skill skill = player.playerSkill.skills.get(i);
                                                    if (skill.point > 0 && !(skill.template.id >= 24 && skill.template.id <= 26 && skill.template.id == Skill.TU_SAT)) {
                                                        skillList.add(skill);
                                                    }
                                                }
                                                int[][] skillTemp = new int[skillList.size()][3];
                                                for (byte i = 0; i < skillList.size(); i++) {
                                                    Skill skill = skillList.get(i);
                                                    if (skill.point > 0) {
                                                        skillTemp[i][0] = skill.template.id;
                                                        skillTemp[i][1] = skill.point;
                                                        skillTemp[i][2] = skill.coolDown;
                                                    }
                                                }
                                                BossData bossDataClone = new BossData(
                                                        "Nhân Bản " + player.name,
                                                        player.gender,
                                                        Boss.DAME_NORMAL, //type dame
                                                        Boss.HP_NORMAL, //type hp
                                                        player.nPoint.dame, //dame
                                                        500000,
                                                        new double[][]{{player.nPoint.hpMax}}, //hp
                                                        new short[]{player.getHead(), player.getBody(), player.getLeg()},
                                                        new short[]{140},
                                                        skillTemp,
                                                        0);
                                                try {
                                                    oldBossClone = new NhanBan(Util.nextInt(1000000, 20000000), bossDataClone, player);
                                                    oldBossClone.zone = player.zone;
                                                    oldBossClone.joinMap();
                                                    BossManager.gI().addBoss(oldBossClone);
                                                } catch (Exception e) {
                                                }
                                                //trừ vàng khi gọi boss
                                                player.inventory.gold -= 200000000;
                                                Service.gI().sendMoney(player);
                                                break;
                                        }
                                }
                            }
                        }
                    };
                    break;

                case ConstNpc.CUA_HANG_KY_GUI:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                        "Cửa hàng chúng tôi hiện tại đang mở cửa",
                                        "Hướng dẫn", "Cửa hàng", "Từ chối");
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                switch (player.iDMark.getIndexMenu()) {
                                    case ConstNpc.BASE_MENU:
                                        switch (select) {
                                            case 0:
                                                Service.getInstance().sendPopUpMultiLine(player, tempId, avartar, "Truy lùng Săn boss\n Thỏ Đại Ka Nhặt Cà Rốt Đến đổi Vật Phẩm");
                                                break;
                                            case 1:
                                                ShopService.gI().openShopSpecial(player, this, ConstNpc.trungthu, 0, 3);
                                                break;

                                        }
                                        break;
                                }
                            }
                        }
                    };
                    break;
                default:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                        "Ta Đang Ngủ! Ra Chỗ Khác Chơi",
                                        "Đi Thì Đi");
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                switch (player.iDMark.getIndexMenu()) {
                                    case ConstNpc.BASE_MENU:
                                        switch (select) {
                                            case 1:
                                                Service.getInstance().sendThongBao(player, "Không thể thực hiện");
                                                break;
                                        }
                                        break;
                                }
                            }
                        }
                    };
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log
                    .error(NpcFactory.class,
                            e, "Lỗi load npc");
        }
        return npc;
    }

    public static void createNpcRongThieng() {
        Npc npc = new Npc(-1, -1, -1, -1, ConstNpc.RONG_THIENG, 31808) {
            @Override
            public void confirmMenu(Player player, int select) {
                switch (player.iDMark.getIndexMenu()) {
                    case ConstNpc.IGNORE_MENU:
                        break;
                    case ConstNpc.SHENRON_CONFIRM:
                        if (select == 0) {
                            SummonDragon.gI().confirmWish();
                        } else if (select == 1) {
                            SummonDragon.gI().reOpenShenronWishes(player);
                        }
                        break;
                    case ConstNpc.SHENRON_1_1:
                        if (player.iDMark.getIndexMenu() == ConstNpc.SHENRON_1_1
                                && select == SHENRON_1_STAR_WISHES_1.length - 1) {
                            NpcService.gI().createMenuRongThieng(player, ConstNpc.SHENRON_1_2, SHENRON_SAY,
                                    SHENRON_1_STAR_WISHES_2);
                            break;
                        }
                    case ConstNpc.SHENRON_1_2:
                        if (player.iDMark.getIndexMenu() == ConstNpc.SHENRON_1_2
                                && select == SHENRON_1_STAR_WISHES_2.length - 1) {
                            NpcService.gI().createMenuRongThieng(player, ConstNpc.SHENRON_1_1, SHENRON_SAY,
                                    SHENRON_1_STAR_WISHES_1);
                            break;
                        }
                    case ConstNpc.BLACK_SHENRON:
                        if (player.iDMark.getIndexMenu() == ConstNpc.BLACK_SHENRON
                                && select == BLACK_SHENRON_WISHES.length) {
                            NpcService.gI().createMenuRongThieng(player, ConstNpc.BLACK_SHENRON, BLACK_SHENRON_SAY,
                                    BLACK_SHENRON_WISHES);
                            break;
                        }
                    case ConstNpc.ICE_SHENRON:
                        if (player.iDMark.getIndexMenu() == ConstNpc.ICE_SHENRON
                                && select == ICE_SHENRON_WISHES.length) {
                            NpcService.gI().createMenuRongThieng(player, ConstNpc.ICE_SHENRON, ICE_SHENRON_SAY,
                                    ICE_SHENRON_WISHES);
                            break;
                        }
                    default:
                        SummonDragon.gI().showConfirmShenron(player, player.iDMark.getIndexMenu(), (byte) select);
                        break;
                }
            }
        };
    }

    public static void MenuBH(Player player, String hieuung) {
        long dk = player.CapTamkjll + 1;
        if (player.CapTamkjll >= 700) {
            NpcService.gI().createMenuConMeo(player, ConstNpc.TamkjllHU, -1, "|7|Chúc Bạn Một Ngày Vui Vẻ\n"
                    + "|5|Tiên Bang: " + player.CapTamkjll + " Cấp\n"
                    + "|7|" + "Tiến trình: " + player.ExpTamkjll * 100L / (500000L * dk) + "%\n"
                    + "|4|" + hieuung,
                    "hiệu ứng\nChủ Động", "hiệu ứng\nHỗ Trợ", "hiệu ứng\nPhụ Trợ");
        } else if (player.CapTamkjll >= 500) {
            NpcService.gI().createMenuConMeo(player, ConstNpc.TamkjllHU, -1, "|7|Chúc Bạn Một Ngày Vui Vẻ\n"
                    + "|5|Tiên Bang: " + player.CapTamkjll + " Cấp\n"
                    + "|7|" + "Tiến trình: " + player.ExpTamkjll * 100L / (500000L * dk) + "%\n"
                    + "|4|" + hieuung,
                    "hiệu ứng\nChủ Động", "hiệu ứng\nHỗ Trợ", "hiệu ứng\nPhụ Trợ");
        } else if (player.CapTamkjll >= 300) {
            NpcService.gI().createMenuConMeo(player, ConstNpc.TamkjllHU, -1, "|7|Chúc Bạn Một Ngày Vui Vẻ\n"
                    + "|5|Tiên Bang: " + player.CapTamkjll + " Cấp\n"
                    + "|7|" + "Tiến trình: " + player.ExpTamkjll * 100L / (500000L * dk) + "%\n"
                    + "|4|" + hieuung,
                    "hiệu ứng\nChủ Động", "hiệu ứng\nHỗ Trợ", "hiệu ứng\nPhụ Trợ");
        } else if (player.CapTamkjll >= 100) {
            NpcService.gI().createMenuConMeo(player, ConstNpc.TamkjllHU, -1, "|7|Chúc Bạn Một Ngày Vui Vẻ\n"
                    + "|5|Tiên Bang: " + player.CapTamkjll + " Cấp\n"
                    + "|7|" + "Tiến trình: " + player.ExpTamkjll * 100L / (500000L * dk) + "%\n"
                    + "|4|" + hieuung,
                    "hiệu ứng\nChủ Động", "hiệu ứng\nHỗ Trợ", "hiệu ứng\nPhụ Trợ");
        } else if (player.CapTamkjll >= 70) {
            NpcService.gI().createMenuConMeo(player, ConstNpc.TamkjllHU, -1, "|7|Chúc Bạn Một Ngày Vui Vẻ\n"
                    + "|5|Tiên Bang: " + player.CapTamkjll + " Cấp\n"
                    + "|7|" + "Tiến trình: " + player.ExpTamkjll * 100L / (500000L * dk) + "%\n"
                    + "|4|" + hieuung,
                    "hiệu ứng\nChủ Động", "hiệu ứng\nHỗ Trợ", "hiệu ứng\nPhụ Trợ");
        } else if (player.CapTamkjll >= 30) {
            NpcService.gI().createMenuConMeo(player, ConstNpc.TamkjllHU, -1, "|7|Chúc Bạn Một Ngày Vui Vẻ\n"
                    + "|5|Tiên Bang: " + player.CapTamkjll + " Cấp\n"
                    + "|7|" + "Tiến trình: " + player.ExpTamkjll * 100L / (500000L * dk) + "%\n"
                    + "|4|" + hieuung,
                    "hiệu ứng\nChủ Động", "hiệu ứng\nHỗ Trợ", "hiệu ứng\nPhụ Trợ");
        } else {
            NpcService.gI().createMenuConMeo(player, ConstNpc.TamkjllHU, -1, "|7|Chúc Bạn Một Ngày Vui Vẻ\n"
                    + "|5|Tiên Bang: " + player.CapTamkjll + " Cấp\n"
                    + "|7|" + "Tiến trình: " + player.ExpTamkjll * 100L / (500000L * dk) + "%\n"
                    + "|4|" + hieuung,
                    "hiệu ứng\nChủ Động", "hiệu ứng\nHỗ Trợ", "hiệu ứng\nPhụ Trợ");
        }
    }

    public static void createNpcConMeo() {
        Npc npc = new Npc(-1, -1, -1, -1, ConstNpc.CON_MEO, 31808) {
            @Override
            public void confirmMenu(Player player, int select) {
                switch (player.iDMark.getIndexMenu()) {
                    case ConstNpc.CONFIRM_DIALOG:
                        ConfirmDialog confirmDialog = player.getConfirmDialog();
                        if (confirmDialog != null) {
                            if (confirmDialog instanceof MenuDialog menu) {
                                menu.getRunable().setIndexSelected(select);
                                menu.run();
                                return;
                            }
                            if (select == 0) {
                                confirmDialog.run();
                            } else {
                                confirmDialog.cancel();
                            }
                            //                         player.getConfirmDialog(null);
                        }
                        break;
                    case ConstNpc.MENU_OPTION_USE_ITEM1105:
                        switch (select) {
                            case 0:
                                IntrinsicService.gI().sattd(player);
                                break;
                            case 1:
                                IntrinsicService.gI().satnm(player);
                                break;
                            case 2:
                                IntrinsicService.gI().setxd(player);
                                break;
                            default:
                                break;
                        }
                        break;
                    case ConstNpc.Tamkjllmenu:
                        switch (select) {
                            case 0:
                                if (player.Nametc == null) {
                                    Service.getInstance().sendThongBaoFromAdmin(player, "Đánh 1 Hít Vô Ai Đi Cu");
                                    return;
                                }
                                String huthaydame;
                                if (player.dametc <= 0) {
                                    huthaydame = "Hụt";
                                } else {
                                    huthaydame = Util.FormatNumber(player.dametc);
                                }
                                String expke = "";
                                if (player.ctkclan > 0) {
                                    expke = "\n|1|Exp Tiên Bang Vừa nhận ké: " + player.ctkclan;
                                }
                                NpcService.gI().createMenuConMeo(player, ConstNpc.Tamkjllmenu, -1,
                                        "|7|Công Ty Trách Nhiệm Hữu Hạn 1 Thành Viên: @Admin\n"
                                        + player.Nametc
                                        + "\n|7|Hp Mục tiêu còn: " + Util.FormatNumber(player.hptc)
                                        + "\n|3|Sát thương lên mục tiêu: " + huthaydame
                                        + "\n|1|Exp Tiên Bang Nhận: " + player.ctk
                                        + expke,
                                        "Sát Thương Mục tiêu");
                                break;
                            case 1:
                                int gender = player.gender;
                                String hieuung = "Hành tinh: " + gender;
                                MenuBH(player, hieuung);
                                break;

                            case 2:
                                long exptm = player.TamkjllThomo;
                                NpcService.gI().createMenuConMeo(player, ConstNpc.Tamkjllmenu, -1,
                                        "|7|Công Ty Trách Nhiệm Hữu Hạn 1 Thành Viên: @Admin\n"
                                        + "|4|\nCần Mở Thiên Phú Để Tu Tiên\n"
                                        + "|4|\nExp Tu tiên: " + Util.getFormatNumber(player.Tamkjlltutien[0]),
                                        "Sát Thương \nVừa Đấm", "Hiệu ứng \nTiên Bang",
                                        "Thông Tin Exp"
                                );
                                break;

//                            case 6:
//                                Input.gI().CheckInfo(player);
//                                break;
//                            case 9:
//                                Input.gI().CHUYENHN(player);
//                                break;
                        }
                        break;

                    case ConstNpc.Checkthongtin:
                        switch (select) {
                            case 0:
                                NpcService.gI().createMenuConMeo(player, ConstNpc.Checkthongtin, -1,
                                        "|7|Công Ty Trách Nhiệm Hữu Hạn 1 Thành Viên: @Admin\n"
                                        + "|7|Hp: " + Util.FormatNumber(player.nPoint.hp)
                                        //    + Util.FormatNumber(player.nPoint.hpMax)
                                        + "\n|2|Ki: " + Util.FormatNumber(player.nPoint.mp)
                                        //   + Util.FormatNumber(player.nPoint.mpMax)
                                        + "\n|4|Dame: " + Util.FormatNumber(player.nPoint.dame)
                                        + "\n|8|Giáp: " + Util.FormatNumber(player.nPoint.def)
                                        + "\n-Tiềm năng: " + Util.FormatNumber(player.nPoint.tiemNang)
                                        + "\n|7|Hp Gốc: " + Util.getFormatNumber(player.nPoint.hpg)
                                        + "\n|2|Ki Gốc: " + Util.getFormatNumber(player.nPoint.mpg)
                                        + "\n|4|Dame Gốc: " + Util.getFormatNumber(player.nPoint.dameg)
                                        + "\n|8|Giáp Gốc: " + Util.getFormatNumber(player.nPoint.defg),
                                        "Thông Tin\nBản Thân", "Thông Tin\nĐệ Tử");
                                break;

                            case 1:
                                if (player.pet != null) {
                                    NpcService.gI().createMenuConMeo(player, ConstNpc.Checkthongtin, -1,
                                            "|7|Công Ty Trách Nhiệm Hữu Hạn 1 Thành Viên: @Admin\n"
                                            + "|7|Hp: " + Util.FormatNumber(player.nPoint.hp)
                                            //    + Util.FormatNumber(player.nPoint.hpMax)
                                            + "\n|2|Ki: " + Util.FormatNumber(player.nPoint.mp)
                                            //   + Util.FormatNumber(player.nPoint.mpMax)
                                            + "\n|4|Dame: " + Util.FormatNumber(player.nPoint.dame)
                                            + "\n|8|Giáp: " + Util.FormatNumber(player.nPoint.def)
                                            + "\n-Tiềm năng: " + Util.FormatNumber(player.nPoint.tiemNang)
                                            + "\n|7|Hp Gốc: " + Util.getFormatNumber(player.nPoint.hpg)
                                            + "\n|2|Ki Gốc: " + Util.getFormatNumber(player.nPoint.mpg)
                                            + "\n|4|Dame Gốc: " + Util.getFormatNumber(player.nPoint.dameg)
                                            + "\n|8|Giáp Gốc: " + Util.getFormatNumber(player.nPoint.defg),
                                            "Thông Tin\nBản Thân", "Thông Tin\nĐệ Tử");
                                }
                                break;

                            case 2:
                                if (player.Nametc == null) {
                                    Service.gI().sendThongBao(player, "đánh ai đó đi");
                                    return;
                                }
                                String huthaydame;
                                if (player.dametc <= 0) {
                                    huthaydame = "Hụt";
                                } else {
                                    huthaydame = Util.FormatNumber(player.dametc);
                                }
                                String expke = "";
                                if (player.ctkclan > 0) {
                                    expke = "\n|1|Exp Tiên Bang Vừa nhận ké: " + player.ctkclan;
                                }
                                NpcService.gI().createMenuConMeo(player, ConstNpc.Checkthongtin, -1,
                                        "|7|Công Ty Trách Nhiệm Hữu Hạn 1 Thành Viên: @Admin\n"
                                        + player.Nametc
                                        + "\n|7|Hp Mục tiêu còn: " + Util.FormatNumber(player.hptc)
                                        + "\n|3|ST Lên Mục Tiêu: " + huthaydame
                                        + "\n|1|Exp Up Chung: " + player.ctk
                                        + expke,
                                        "Thông Tin\nBản Thân", "Thông Tin\nMục Tiêu");
                                break;

                            case 3:
                                String hieuung = "Hành tinh: " + Service.gI().get_HanhTinh(player.gender);
                                MenuBH(player, hieuung);
                                break;

                            case 4:
                                long exptm = player.TamkjllThomo;
                                NpcService.gI().createMenuConMeo(player, ConstNpc.Checkthongtin, -1,
                                        "|7|Công Ty Trách Nhiệm Hữu Hạn 1 Thành Viên: @Admin\n"
                                        + "\n|8|Nhập Ma("
                                        + (player.DLbTamkjll * 100 / (3000000L + player.LbTamkjll * 200000L))
                                        + "%): "
                                        + player.LbTamkjll
                                        + "\nExp Nhập Ma: " + Util.getFormatNumber(player.DLbTamkjll)
                                        + "\nExp Tiên Bang: " + Util.getFormatNumber(player.ExpTamkjll)
                                        + "\nExp Tu tiên: " + Util.getFormatNumber(player.Tamkjlltutien[0]),
                                        "Check\nBản Thân", "Check\nĐệ Tử", "Check\nMục Tiêu", "Check\nTiên Bang",
                                        "Check Exp", "Soi Boss");
                                break;

                            case 5:

                                if (player.vip < 6) {
                                    ;
                                    Service.getInstance().sendThongBaoFromAdmin(player, "|8|\nCần Đạt Trình Độ Đẹp Trai Từ Vip 6 Được Dịch Chuyển Tới Boss");
                                    return;
                                }

                                BossManager.gI().showListBoss(player);
                                break;
//                            case 6:
//                                Input.gI().CheckInfo(player);
//                                break;
//                            case 9:
//                                Input.gI().CHUYENHN(player);
//                                break;
                        }
                        break;

                    case ConstNpc.TamkjllCS:
                        switch (select) {
                            case 0: {
                                if (player.CapTamkjll < 100) {
                                    Service.getInstance().sendThongBaoFromAdmin(player, "Bạn cần ít nhất 100 Tiên Bang.");
                                    return;
                                }
                                if (player.diemfam < 200000 + (player.TamkjllCS * 1)) {
                                    Service.getInstance().sendThongBao(player,
                                            "Bạn cần ít nhất "
                                            + Util.FormatNumber(200000 + (player.TamkjllCS * 1))
                                            + " Điểm Farm");
                                    return;
                                }
                                long vangth = 1;
                                if (player.nPoint.power < 5000000000000000l) {
                                    Service.getInstance().sendThongBao(player,
                                            "Bạn cần: " + (5000000000000000l - player.nPoint.power)
                                            + "sức mạnh nữa để thực hiện.");
                                    return;
                                }
                                if (player.inventory.gold < vangth) {
                                    Service.getInstance().sendThongBao(player,
                                            "Bạn cần: " + (vangth - player.inventory.gold) + " vàng nx để thực hiện.");
                                    return;
                                }

                                player.diemfam -= 200000 + (player.TamkjllCS * 1);
                                player.inventory.gold -= vangth;
                                player.nPoint.power = 0;
                                player.nPoint.tiemNang = 0;
                                player.TamkjllCS++;
                                player.nPoint.hpg += 50000;
                                player.nPoint.mpg += 50000;
                                player.nPoint.dameg += 10000;
                                player.nPoint.defg += 100;
                                Service.getInstance().sendMoney(player);
                                Service.getInstance().point(player);
                                Service.getInstance().sendThongBaoFromAdmin(player,
                                        "Bạn Đã Chuyển Sinh Thành Công!\nBạn được:\n50k Hp Ki gốc\n10k dame gốc\n100 giáp gốc\n"
                                        + "+10% HP KI SD Vào Hiệu Ứng Tiên Bang /1 Cấp\n"
                                        + "\nBạn Chuyển Sinh Được: " + Util.cap(player.TamkjllCS) + "  Cấp Thành Công\n"
                                        + "Vui Lòng Thoát Game Vào Lại Để Không Lỗi Tài Khoản");
                                break;
                            }
                            case 1: {
                                if (player.pet == null) {
                                    return;
                                }
                                if (player.CapTamkjll < 500) {
                                    Service.getInstance().sendThongBaoFromAdmin(player, "Bạn cần ít nhất 500 Tiên Bang.");
                                    return;
                                }
                                if (player.diemfam < 200000) {
                                    Service.getInstance().sendThongBaoFromAdmin(player, "Bạn cần ít nhất 200k Điểm Farm");
                                    return;
                                }
                                long vangth = 0;
                                if (player.pet.nPoint.power < 500000000000000l) {
                                    Service.getInstance()
                                            .sendThongBaoFromAdmin(player, "Bạn cần: "
                                                    + (500000000000000l - player.pet.nPoint.power)
                                                    + "s\u1EE9c m\u1EA1nh nx để thực hiện.");
                                    return;
                                }
                                if (player.inventory.gold < vangth) {
                                    Service.getInstance().sendThongBaoFromAdmin(player,
                                            "Bạn cần: " + (vangth - player.inventory.gold) + " vàng nx để thực hiện.");
                                    return;
                                }

                                player.diemfam -= 200000;
                                player.inventory.gold -= vangth;
                                Service.getInstance().sendMoney(player);
                                if (Util.isTrue(15f + (player.CapTamkjll >= 500 ? 3f : 0f), 50)) {
                                    player.pet.nPoint.power = 0;
                                    player.pet.TamkjllCS++;
                                    player.pet.nPoint.hpg += 50000;
                                    player.pet.nPoint.mpg += 50000;
                                    player.pet.nPoint.dameg += 10000;
                                    player.pet.nPoint.defg += 100;
                                    Service.getInstance().point(player.pet);
                                    Service.getInstance().sendThongBaoFromAdmin(player,
                                            "Bạn Đã Chuyển Sinh cho đệ thành công! đệ được:\n50k Hp Ki gốc\n10k dame gốc\n100 giáp gốc");
                                } else {
                                    Service.getInstance().sendThongBao(player,
                                            "Bạn Chuyển Sinh cho đệ Thất bại!");
                                }
                                break;
                            }
                        }
                        break;
                    case ConstNpc.TamkjllParam: {
                        Item it = player.inventory.itemsBody.get(select);
                        if (it.quantity < 1) {
                            NpcService.gI().createMenuConMeo(player, ConstNpc.TamkjllParam, -1,
                                    "|7|Chúc Bạn Một Ngày Vui Vẻ\n"
                                    + "|7|Ô này không có đồ!!!"
                                    + "\nBạn muốn xem chỉ số đồ bị giới hạn hiện thị:",
                                    "chỉ số đồ mặc\nô 1", "chỉ số đồ mặc\nô 2", "chỉ số đồ mặc\nô 3",
                                    "chỉ số đồ mặc\nô 4", "chỉ số đồ mặc\nô 5", "chỉ số đồ mặc\nô 6",
                                    "chỉ số đồ mặc\nô 7", "chỉ số đồ mặc\nô 8", "chỉ số đồ mặc\nô 9",
                                    "chỉ số đồ mặc\nô 10", "chỉ số đồ mặc\nô 11");
                            return;
                        }
                        NpcService.gI().createMenuConMeo(player, ConstNpc.TamkjllParam, -1,
                                "|7|Chúc Bạn Một Ngày Vui Vẻ\n"
                                + "|2|Tên Vật phẩm: " + it.template.name
                                + "\n|7|Chỉ số:"
                                + "\n" + it.getInfoname(),
                                "chỉ số đồ mặc\nô 1", "chỉ số đồ mặc\nô 2", "chỉ số đồ mặc\nô 3",
                                "chỉ số đồ mặc\nô 4", "chỉ số đồ mặc\nô 5", "chỉ số đồ mặc\nô 6",
                                "chỉ số đồ mặc\nô 7", "chỉ số đồ mặc\nô 8", "chỉ số đồ mặc\nô 9",
                                "chỉ số đồ mặc\nô 10", "chỉ số đồ mặc\nô 11");
                    }
                    break;
                    case ConstNpc.TamkjllCheckPlayer: {
                        Player plcheck = Client.gI().getPlayer(player.TamkjllNamePlayer);
                        if (plcheck != null) {
                            if (select == 0) {
                                NpcService.gI().createMenuConMeo(player, ConstNpc.TamkjllCheckPlayer, -1,
                                        "|7|Chúc Bạn Một Ngày Vui Vẻ\n"
                                        + "|7|Thông tin của: " + plcheck.name
                                        + "\n|7|Hp: " + Util.FormatNumber(plcheck.nPoint.hp) + "/"
                                        + Util.FormatNumber(plcheck.nPoint.hpMax)
                                        + "\n|2|Ki: " + Util.FormatNumber(plcheck.nPoint.mp) + "/"
                                        + Util.FormatNumber(plcheck.nPoint.mpMax)
                                        + "\n|4|Dame: " + Util.FormatNumber(plcheck.nPoint.dame)
                                        + "\n|8|Giáp: " + Util.FormatNumber(plcheck.nPoint.def)
                                        + "\n-Tiềm năng:\n"
                                        + "|7|Hp Gốc: " + Util.getFormatNumber(plcheck.nPoint.hpg)
                                        + "\n|2|Ki Gốc: " + Util.getFormatNumber(plcheck.nPoint.mpg)
                                        + "\n|4|Dame Gốc: " + Util.getFormatNumber(plcheck.nPoint.dameg)
                                        + "\n|8|Giáp Gốc: " + Util.getFormatNumber(plcheck.nPoint.defg),
                                        "Thông tin\nnhân vật", "Thông tin\nđệ tử", "Thông tin\nKhác",
                                        "chỉ số đồ mặc\nô 1", "chỉ số đồ mặc\nô 2", "chỉ số đồ mặc\nô 3",
                                        "chỉ số đồ mặc\nô 4", "chỉ số đồ mặc\nô 5", "chỉ số đồ mặc\nô 6",
                                        "chỉ số đồ mặc\nô 7", "chỉ số đồ mặc\nô 8", "chỉ số đồ mặc\nô 9",
                                        "chỉ số đồ mặc\nô 10", "chỉ số đồ mặc\nô 11");
                            } else if (select == 1) {
                                NpcService.gI().createMenuConMeo(player, ConstNpc.TamkjllCheckPlayer, -1,
                                        "|7|Chúc Bạn Một Ngày Vui Vẻ\n"
                                        + "|7|Thông tin đệ tử của: " + plcheck.name
                                        + "\n|7|Hp: " + Util.FormatNumber(plcheck.pet.nPoint.hp) + "/"
                                        + Util.FormatNumber(plcheck.pet.nPoint.hpMax)
                                        + "\n|2|Ki: " + Util.FormatNumber(plcheck.pet.nPoint.mp) + "/"
                                        + Util.FormatNumber(plcheck.pet.nPoint.mpMax)
                                        + "\n|4|Dame: " + Util.FormatNumber(plcheck.pet.nPoint.dame)
                                        + "\n|8|Giáp: " + Util.FormatNumber(plcheck.pet.nPoint.def)
                                        + "\n-Tiềm năng:\n"
                                        + "|7|Hp Gốc: " + Util.getFormatNumber(plcheck.pet.nPoint.hpg)
                                        + "\n|2|Ki Gốc: " + Util.getFormatNumber(plcheck.pet.nPoint.mpg)
                                        + "\n|4|Dame Gốc: " + Util.getFormatNumber(plcheck.pet.nPoint.dameg)
                                        + "\n|8|Giáp Gốc: " + Util.getFormatNumber(plcheck.pet.nPoint.defg),
                                        "Thông tin\nnhân vật", "Thông tin\nđệ tử", "Thông tin\nKhác",
                                        "chỉ số đồ mặc\nô 1", "chỉ số đồ mặc\nô 2", "chỉ số đồ mặc\nô 3",
                                        "chỉ số đồ mặc\nô 4", "chỉ số đồ mặc\nô 5", "chỉ số đồ mặc\nô 6",
                                        "chỉ số đồ mặc\nô 7", "chỉ số đồ mặc\nô 8", "chỉ số đồ mặc\nô 9",
                                        "chỉ số đồ mặc\nô 10", "chỉ số đồ mặc\nô 11");
                            } else if (select == 2) {
                                NpcService.gI().createMenuConMeo(player, ConstNpc.TamkjllCheckPlayer, -1,
                                        "|7|Chúc Bạn Một Ngày Vui Vẻ\n"
                                        + "|7|Thông tin của: " + plcheck.name
                                        + "\n|2|Tiên Bang: " + plcheck.CapTamkjll
                                        + "\n|2|Tu vi: "
                                        + (plcheck.Tamkjlltutien[2] >= 1
                                                ? plcheck.TamkjllTuviTutien(
                                                        Util.maxInt(plcheck.Tamkjlltutien[1]))
                                                : "Chưa mở thiên phú")
                                        + "\n|2|Cấp Nhập Ma: " + plcheck.LbTamkjll
                                        + "\n|2|Cấp Khai Thác: " + plcheck.TamkjllThomo,
                                        "Thông tin\nnhân vật", "Thông tin\nđệ tử", "Thông tin\nKhác",
                                        "chỉ số đồ mặc\nô 1", "chỉ số đồ mặc\nô 2", "chỉ số đồ mặc\nô 3",
                                        "chỉ số đồ mặc\nô 4", "chỉ số đồ mặc\nô 5", "chỉ số đồ mặc\nô 6",
                                        "chỉ số đồ mặc\nô 7", "chỉ số đồ mặc\nô 8", "chỉ số đồ mặc\nô 9",
                                        "chỉ số đồ mặc\nô 10", "chỉ số đồ mặc\nô 11");
                            } else {
                                Item it = plcheck.inventory.itemsBody.get(select - 3);
                                if (it == null) {
                                    NpcService.gI().createMenuConMeo(player, ConstNpc.TamkjllCheckPlayer, -1,
                                            "|7|Chúc Bạn Một Ngày Vui Vẻ\n"
                                            + "|7|Thông tin của: " + plcheck.name
                                            + "\n|7|Ô này không có đồ!!!"
                                            + "\nLỗi item không tồn tại",
                                            "Thông tin\nnhân vật", "Thông tin\nđệ tử", "Thông tin\nKhác",
                                            "chỉ số đồ mặc\nô 1", "chỉ số đồ mặc\nô 2", "chỉ số đồ mặc\nô 3",
                                            "chỉ số đồ mặc\nô 4", "chỉ số đồ mặc\nô 5", "chỉ số đồ mặc\nô 6",
                                            "chỉ số đồ mặc\nô 7", "chỉ số đồ mặc\nô 8", "chỉ số đồ mặc\nô 9",
                                            "chỉ số đồ mặc\nô 10", "chỉ số đồ mặc\nô 11");
                                    return;
                                }
                                if (it.quantity < 1) {
                                    NpcService.gI().createMenuConMeo(player, ConstNpc.TamkjllCheckPlayer, -1,
                                            "|7|Chúc Bạn Một Ngày Vui Vẻ\n"
                                            + "|7|Thông tin của: " + plcheck.name
                                            + "\n|7|Ô này không có đồ!!!"
                                            + "\nBạn muốn xem chỉ số đồ bị giới hạn hiện thị:",
                                            "Thông tin\nnhân vật", "Thông tin\nđệ tử", "Thông tin\nKhác",
                                            "chỉ số đồ mặc\nô 1", "chỉ số đồ mặc\nô 2", "chỉ số đồ mặc\nô 3",
                                            "chỉ số đồ mặc\nô 4", "chỉ số đồ mặc\nô 5", "chỉ số đồ mặc\nô 6",
                                            "chỉ số đồ mặc\nô 7", "chỉ số đồ mặc\nô 8", "chỉ số đồ mặc\nô 9",
                                            "chỉ số đồ mặc\nô 10", "chỉ số đồ mặc\nô 11");
                                    return;
                                }
                                NpcService.gI().createMenuConMeo(player, ConstNpc.TamkjllCheckPlayer, -1,
                                        "|7|Chúc Bạn Một Ngày Vui Vẻ\n"
                                        + "|7|Thông tin của: " + plcheck.name
                                        + "\n|2|Tên Vật phẩm: " + it.template.name
                                        + "\n|7|Chỉ số:"
                                        + "\n" + it.getInfoname(),
                                        "Thông tin\nnhân vật", "Thông tin\nđệ tử", "Thông tin\nKhác",
                                        "chỉ số đồ mặc\nô 1", "chỉ số đồ mặc\nô 2", "chỉ số đồ mặc\nô 3",
                                        "chỉ số đồ mặc\nô 4", "chỉ số đồ mặc\nô 5", "chỉ số đồ mặc\nô 6",
                                        "chỉ số đồ mặc\nô 7", "chỉ số đồ mặc\nô 8", "chỉ số đồ mặc\nô 9",
                                        "chỉ số đồ mặc\nô 10", "chỉ số đồ mặc\nô 11");
                            }
                        } else {
                            Service.getInstance().sendThongBao(player, "Người chơi không tồn tại hoặc đang offline");
                        }
                        break;
                    }

                    case ConstNpc.mocnap:
                        switch (select) {
                            case 0: {

                                //     UseItem.gI().ComfirmMocNap(player);
                                break;
                            }
                        }
                        break;

                    case ConstNpc.doitainguyen:
                        switch (select) {
                            case 0:
                                if (player.diemfam >= 10000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) Util.nextInt(220, 224), 10000);
                                    goiqua.itemOptions.add(new ItemOption(30, 0));
                                    InventoryService.gI().addItemBag(player, goiqua, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);

                                    player.diemfam -= 10000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng Random");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Bạn Không có điểm , lười vừa thôi" + "\nCần " + (10000 - player.diemfam) + " Điểm Nữa");
                                }
                                break;

                            case 1:
                                if (player.diemfam >= 10000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 1502, 10000);
                                    goiqua.itemOptions.add(new ItemOption(30, 2025));
                                    InventoryService.gI().addItemBag(player, goiqua, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);

                                    player.diemfam -= 10000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng Random");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Bạn Không có điểm , lười vừa thôi" + "\nCần " + (10000 - player.diemfam) + " Điểm Nữa");
                                }
                                break;

                            case 2:
                                if (player.diemfam >= 10000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 77, 200000);
                                    goiqua.itemOptions.add(new ItemOption(30, 2025));
                                    InventoryService.gI().addItemBag(player, goiqua, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);

                                    player.diemfam -= 10000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng " + goiqua.quantity);
                                } else {
                                    Service.getInstance().sendThongBao(player, "Bạn Không có điểm , lười vừa thôi" + "\nCần " + (10000 - player.diemfam) + " Điểm Nữa");
                                }
                                break;
                            case 3:
                                if (player.diemfam >= 10000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 861, 200000);
                                    goiqua.itemOptions.add(new ItemOption(174, 2025));
                                    InventoryService.gI().addItemBag(player, goiqua, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);

                                    player.diemfam -= 10000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng " + goiqua.quantity);
                                } else {
                                    Service.getInstance().sendThongBao(player, "Bạn Không có điểm , lười vừa thôi" + "\nCần " + (10000 - player.diemfam) + " Điểm Nữa");
                                }
                                break;
                            case 4:
                                if (player.diemfam >= 10000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 190, 1000000000);
                                    goiqua.itemOptions.add(new ItemOption(174, 2025));
                                    InventoryService.gI().addItemBag(player, goiqua, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);

                                    player.diemfam -= 10000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng " + goiqua.quantity);
                                } else {
                                    Service.getInstance().sendThongBao(player, "Bạn Không có điểm , lười vừa thôi" + "\nCần " + (10000 - player.diemfam) + " Điểm Nữa");
                                }
                                break;

                            case 5:
                                if (player.diemfam >= 10000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 1111, 10000);
                                    goiqua.itemOptions.add(new ItemOption(174, 2025));
                                    InventoryService.gI().addItemBag(player, goiqua, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);

                                    player.diemfam -= 10000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng Random");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Bạn Không có điểm , lười vừa thôi" + "\nCần " + (10000 - player.diemfam) + " Điểm Nữa");
                                }
                                break;
                        }
                        break;

                    case ConstNpc.doitrangbi:
                        switch (select) {
                            case 0:
                                if (player.diemfam >= 50000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 555, 1);
                                    goiqua.itemOptions.add(new ItemOption(47, 10000));
                                    InventoryService.gI().addItemBag(player, goiqua, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);

                                    Item goiqua1 = ItemService.gI().createNewItem((short) 556, 1);
                                    goiqua1.itemOptions.add(new ItemOption(6, 500000));
                                    InventoryService.gI().addItemBag(player, goiqua1, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);

                                    Item goiqua2 = ItemService.gI().createNewItem((short) 562, 1);
                                    goiqua2.itemOptions.add(new ItemOption(0, 30000));
                                    InventoryService.gI().addItemBag(player, goiqua2, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);

                                    Item goiqua3 = ItemService.gI().createNewItem((short) 563, 1);
                                    goiqua3.itemOptions.add(new ItemOption(7, 500000));
                                    InventoryService.gI().addItemBag(player, goiqua3, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);

                                    Item goiqua4 = ItemService.gI().createNewItem((short) 561, 1);
                                    goiqua4.itemOptions.add(new ItemOption(14, 12));
                                    InventoryService.gI().addItemBag(player, goiqua4, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);

                                    player.diemfam -= 50000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng 1");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Bạn Không có điểm , lười vừa thôi" + "\nCần " + (50000 - player.diemfam) + " Điểm Nữa");
                                }
                                break;

                            case 1:
                                if (player.diemfam >= 50000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 559, 1);
                                    goiqua.itemOptions.add(new ItemOption(47, 10000));
                                    InventoryService.gI().addItemBag(player, goiqua, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);

                                    Item goiqua1 = ItemService.gI().createNewItem((short) 560, 1);
                                    goiqua1.itemOptions.add(new ItemOption(6, 500000));
                                    InventoryService.gI().addItemBag(player, goiqua1, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);

                                    Item goiqua2 = ItemService.gI().createNewItem((short) 566, 1);
                                    goiqua2.itemOptions.add(new ItemOption(0, 30000));
                                    InventoryService.gI().addItemBag(player, goiqua2, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);

                                    Item goiqua3 = ItemService.gI().createNewItem((short) 567, 1);
                                    goiqua3.itemOptions.add(new ItemOption(7, 500000));
                                    InventoryService.gI().addItemBag(player, goiqua3, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);

                                    Item goiqua4 = ItemService.gI().createNewItem((short) 561, 1);
                                    goiqua4.itemOptions.add(new ItemOption(14, 12));
                                    InventoryService.gI().addItemBag(player, goiqua4, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);

                                    player.diemfam -= 50000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng 1");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Bạn Không có điểm , lười vừa thôi" + "\nCần " + (50000 - player.diemfam) + " Điểm Nữa");
                                }
                                break;

                            case 2:
                                if (player.diemfam >= 50000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 557, 1);
                                    goiqua.itemOptions.add(new ItemOption(47, 10000));
                                    InventoryService.gI().addItemBag(player, goiqua, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);

                                    Item goiqua1 = ItemService.gI().createNewItem((short) 558, 1);
                                    goiqua1.itemOptions.add(new ItemOption(6, 500000));
                                    InventoryService.gI().addItemBag(player, goiqua1, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);

                                    Item goiqua2 = ItemService.gI().createNewItem((short) 564, 1);
                                    goiqua2.itemOptions.add(new ItemOption(0, 50000));
                                    InventoryService.gI().addItemBag(player, goiqua2, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);

                                    Item goiqua3 = ItemService.gI().createNewItem((short) 565, 1);
                                    goiqua3.itemOptions.add(new ItemOption(7, 500000));
                                    InventoryService.gI().addItemBag(player, goiqua3, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);

                                    Item goiqua4 = ItemService.gI().createNewItem((short) 561, 1);
                                    goiqua4.itemOptions.add(new ItemOption(14, 12));
                                    InventoryService.gI().addItemBag(player, goiqua4, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);

                                    player.diemfam -= 50000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng 1");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Bạn Không có điểm , lười vừa thôi" + "\nCần " + (50000 - player.diemfam) + " Điểm Nữa");
                                }
                                break;
                            case 3:
                                if (player.diemfam >= 50000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 650, 1);
                                    goiqua.itemOptions.add(new ItemOption(47, 15000));
                                    InventoryService.gI().addItemBag(player, goiqua, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);

                                    Item goiqua1 = ItemService.gI().createNewItem((short) 651, 1);
                                    goiqua1.itemOptions.add(new ItemOption(6, 700000));
                                    InventoryService.gI().addItemBag(player, goiqua1, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);

                                    Item goiqua2 = ItemService.gI().createNewItem((short) 657, 1);
                                    goiqua2.itemOptions.add(new ItemOption(0, 50000));
                                    InventoryService.gI().addItemBag(player, goiqua2, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);

                                    Item goiqua3 = ItemService.gI().createNewItem((short) 658, 1);
                                    goiqua3.itemOptions.add(new ItemOption(7, 700000));
                                    InventoryService.gI().addItemBag(player, goiqua3, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);

                                    Item goiqua4 = ItemService.gI().createNewItem((short) 656, 1);
                                    goiqua4.itemOptions.add(new ItemOption(14, 12));
                                    InventoryService.gI().addItemBag(player, goiqua4, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);

                                    player.diemfam -= 50000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng 1");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Bạn Không có điểm , lười vừa thôi" + "\nCần " + (50000 - player.diemfam) + " Điểm Nữa");
                                }
                                break;
                            case 4:
                                if (player.diemfam >= 50000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 654, 1);
                                    goiqua.itemOptions.add(new ItemOption(47, 15000));
                                    InventoryService.gI().addItemBag(player, goiqua, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);

                                    Item goiqua1 = ItemService.gI().createNewItem((short) 655, 1);
                                    goiqua1.itemOptions.add(new ItemOption(6, 700000));
                                    InventoryService.gI().addItemBag(player, goiqua1, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);

                                    Item goiqua2 = ItemService.gI().createNewItem((short) 661, 1);
                                    goiqua2.itemOptions.add(new ItemOption(0, 50000));
                                    InventoryService.gI().addItemBag(player, goiqua2, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);

                                    Item goiqua3 = ItemService.gI().createNewItem((short) 662, 1);
                                    goiqua3.itemOptions.add(new ItemOption(7, 700000));
                                    InventoryService.gI().addItemBag(player, goiqua3, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);

                                    Item goiqua4 = ItemService.gI().createNewItem((short) 656, 1);
                                    goiqua4.itemOptions.add(new ItemOption(14, 12));
                                    InventoryService.gI().addItemBag(player, goiqua4, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);

                                    player.diemfam -= 50000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng 1");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Bạn Không có điểm , lười vừa thôi" + "\nCần " + (50000 - player.diemfam) + " Điểm Nữa");
                                }
                                break;

                            case 5:
                                if (player.diemfam >= 50000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 652, 1);
                                    goiqua.itemOptions.add(new ItemOption(47, Util.nextInt(1, 15000)));
                                    InventoryService.gI().addItemBag(player, goiqua, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);

                                    Item goiqua1 = ItemService.gI().createNewItem((short) 653, 1);
                                    goiqua1.itemOptions.add(new ItemOption(6, 700000));
                                    InventoryService.gI().addItemBag(player, goiqua1, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);

                                    Item goiqua2 = ItemService.gI().createNewItem((short) 659, 1);
                                    goiqua2.itemOptions.add(new ItemOption(0, 50000));
                                    InventoryService.gI().addItemBag(player, goiqua2, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);

                                    Item goiqua3 = ItemService.gI().createNewItem((short) 660, 1);
                                    goiqua3.itemOptions.add(new ItemOption(7, 700000));
                                    InventoryService.gI().addItemBag(player, goiqua3, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);

                                    Item goiqua4 = ItemService.gI().createNewItem((short) 656, 1);
                                    goiqua4.itemOptions.add(new ItemOption(14, 12));
                                    InventoryService.gI().addItemBag(player, goiqua4, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);

                                    player.diemfam -= 50000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng 1");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Bạn Không có điểm , lười vừa thôi" + "\nCần " + (50000 - player.diemfam) + " Điểm Nữa");
                                }
                                break;
                        }
                        break;

                    case ConstNpc.cfpassfree:
                        switch (select) {
                            case 0:
                                UseItem.gI().ComfirmMocPass(player);
                        }
                        break;

                    case ConstNpc.cfpassmua:
                        switch (select) {
                            case 0:
                                UseItem.gI().ComfirmMocPassmua(player);
                        }
                        break;

                    case ConstNpc.muagoipass:
                        switch (select) {
                            case 0:
                                if (player.getSession().vnd >= 200000) {
                                    PlayerDAO.subvnd(player, 200000);
                                    player.premium += 52000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 52k Điểm premium");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Bạn Đã Hết Tiền");
                                }
                                break;
                            case 1:
                                if (player.getSession().vnd >= 500000) {
                                    PlayerDAO.subvnd(player, 500000);
                                    player.premium += 200000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 200k Điểm premium");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Bạn Đã Hết Tiền");
                                }
                                break;
                            case 2:
                                if (player.getSession().vnd >= 700000) {
                                    PlayerDAO.subvnd(player, 700000);
                                    player.premium += 270000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 270k Điểm premium");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Bạn Đã Hết Tiền");
                                }
                                break;
                            case 3:
                                if (player.getSession().vnd >= 1000000) {
                                    PlayerDAO.subvnd(player, 1000000);
                                    player.premium += 500000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 500k Điểm premium");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Bạn Đã Hết Tiền");
                                }
                                break;
                            case 4:
                                if (player.getSession().vnd >= 2000000) {
                                    PlayerDAO.subvnd(player, 2000000);
                                    player.premium += 1500000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 1500k Điểm premium");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Bạn Đã Hết Tiền");
                                }
                                break;
                            case 5:
                                if (player.getSession().vnd >= 5000000) {
                                    PlayerDAO.subvnd(player, 5000000);
                                    player.premium += 5000000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 5000k Điểm premium");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Bạn Đã Hết Tiền");
                                }
                                break;

                        }
                        break;

                    case ConstNpc.doicaitrang:
                        switch (select) {
                            case 0:
                                if (player.diemfam >= 100000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) Util.nextInt(843, 848), 1);
                                    goiqua.itemOptions.add(new ItemOption(50, 300));
                                    goiqua.itemOptions.add(new ItemOption(77, 300));
                                    goiqua.itemOptions.add(new ItemOption(103, 300));
                                    goiqua.itemOptions.add(new ItemOption(101, Util.nextInt(1, 100)));
                                    goiqua.itemOptions.add(new ItemOption(117, Util.nextInt(1, 100)));
                                    goiqua.itemOptions.add(new ItemOption(174, 2025));
                                    InventoryService.gI().addItemBag(player, goiqua, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);

                                    player.diemfam -= 100000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng 1");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Bạn Không có điểm , lười vừa thôi" + "\nCần " + (100000 - player.diemfam) + " Điểm Nữa");
                                }
                                break;

                            case 1:
                                if (player.diemfam >= 100000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) Util.nextInt(843, 848), 1);
                                    goiqua.itemOptions.add(new ItemOption(50, 300));
                                    goiqua.itemOptions.add(new ItemOption(77, 300));
                                    goiqua.itemOptions.add(new ItemOption(103, 300));
                                    goiqua.itemOptions.add(new ItemOption(101, Util.nextInt(1, 100)));
                                    goiqua.itemOptions.add(new ItemOption(117, Util.nextInt(1, 100)));
                                    goiqua.itemOptions.add(new ItemOption(174, 2025));
                                    InventoryService.gI().addItemBag(player, goiqua, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);

                                    player.diemfam -= 100000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng 1");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Bạn Không có điểm , lười vừa thôi" + "\nCần " + (100000 - player.diemfam) + " Điểm Nữa");
                                }
                                break;

                            case 2:
                                if (player.diemfam >= 100000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) Util.nextInt(862, 864), 1);
                                    goiqua.itemOptions.add(new ItemOption(50, 300));
                                    goiqua.itemOptions.add(new ItemOption(77, 300));
                                    goiqua.itemOptions.add(new ItemOption(103, 300));
                                    goiqua.itemOptions.add(new ItemOption(101, Util.nextInt(1, 100)));
                                    goiqua.itemOptions.add(new ItemOption(117, Util.nextInt(1, 100)));
                                    goiqua.itemOptions.add(new ItemOption(174, 2025));
                                    InventoryService.gI().addItemBag(player, goiqua, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);

                                    player.diemfam -= 100000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng 1");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Bạn không có điểm , lười vừa thôi" + "\nCần " + (100000 - player.diemfam) + " Điểm Nữa");
                                }
                                break;
                            case 3:
                                if (player.diemfam >= 100000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) Util.nextInt(937, 941), 1);
                                    goiqua.itemOptions.add(new ItemOption(50, 300));
                                    goiqua.itemOptions.add(new ItemOption(77, 300));
                                    goiqua.itemOptions.add(new ItemOption(103, 300));
                                    goiqua.itemOptions.add(new ItemOption(101, Util.nextInt(1, 100)));
                                    goiqua.itemOptions.add(new ItemOption(117, Util.nextInt(1, 100)));
                                    goiqua.itemOptions.add(new ItemOption(174, 2025));
                                    InventoryService.gI().addItemBag(player, goiqua, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);

                                    player.diemfam -= 100000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng 1");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Bạn không có điểm , lười vừa thôi" + "\nCần " + (100000 - player.diemfam) + " Điểm Nữa");
                                }
                                break;
                            case 4:
                                if (player.diemfam >= 100000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) Util.nextInt(968, 980), 1);
                                    goiqua.itemOptions.add(new ItemOption(50, 300));
                                    goiqua.itemOptions.add(new ItemOption(77, 300));
                                    goiqua.itemOptions.add(new ItemOption(103, 300));
                                    goiqua.itemOptions.add(new ItemOption(101, Util.nextInt(1, 100)));
                                    goiqua.itemOptions.add(new ItemOption(117, Util.nextInt(1, 100)));
                                    goiqua.itemOptions.add(new ItemOption(174, 2025));
                                    InventoryService.gI().addItemBag(player, goiqua, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);

                                    player.diemfam -= 100000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng 1");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Bạn không có điểm , lười vừa thôi" + "\nCần " + (100000 - player.diemfam) + " Điểm Nữa");
                                }
                                break;

                            case 5:
                                if (player.diemfam >= 100000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) Util.nextInt(1343, 1354), 1);
                                    goiqua.itemOptions.add(new ItemOption(50, 300));
                                    goiqua.itemOptions.add(new ItemOption(77, 300));
                                    goiqua.itemOptions.add(new ItemOption(103, 300));
                                    goiqua.itemOptions.add(new ItemOption(101, Util.nextInt(1, 100)));
                                    goiqua.itemOptions.add(new ItemOption(117, Util.nextInt(1, 100)));
                                    goiqua.itemOptions.add(new ItemOption(174, 2025));
                                    InventoryService.gI().addItemBag(player, goiqua, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);

                                    player.diemfam -= 100000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng 1");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Bạn không có điểm , lười vừa thôi" + "\nCần " + (100000 - player.diemfam) + " Điểm Nữa");
                                }
                                break;
                        }
                        break;

                    case ConstNpc.doiphukien:
                        switch (select) {
                            case 0:
                                if (player.diemfam >= 150000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) Util.nextInt(766, 794), 1);
                                    goiqua.itemOptions.add(new ItemOption(50, 150));
                                    goiqua.itemOptions.add(new ItemOption(77, 150));
                                    goiqua.itemOptions.add(new ItemOption(103, 150));
                                    goiqua.itemOptions.add(new ItemOption(101, Util.nextInt(1, 100)));
                                    goiqua.itemOptions.add(new ItemOption(117, Util.nextInt(1, 100)));
                                    goiqua.itemOptions.add(new ItemOption(174, 2025));
                                    InventoryService.gI().addItemBag(player, goiqua, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);

                                    player.diemfam -= 150000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng 1");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Bạn không có điểm , lười vừa thôi" + "\nCần " + (150000 - player.diemfam) + " Điểm Nữa");
                                }
                                break;

                            case 1:
                                if (player.diemfam >= 150000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) Util.nextInt(800, 805), 1);
                                    goiqua.itemOptions.add(new ItemOption(50, 150));
                                    goiqua.itemOptions.add(new ItemOption(77, 150));
                                    goiqua.itemOptions.add(new ItemOption(103, 150));
                                    goiqua.itemOptions.add(new ItemOption(101, Util.nextInt(1, 100)));
                                    goiqua.itemOptions.add(new ItemOption(117, Util.nextInt(1, 100)));
                                    goiqua.itemOptions.add(new ItemOption(174, 2025));
                                    InventoryService.gI().addItemBag(player, goiqua, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);

                                    player.diemfam -= 150000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng 1");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Bạn không có điểm , lười vừa thôi" + "\nCần " + (150000 - player.diemfam) + " Điểm Nữa");
                                }
                                break;

                            case 2:
                                if (player.diemfam >= 150000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) Util.nextInt(814, 817), 1);
                                    goiqua.itemOptions.add(new ItemOption(50, 150));
                                    goiqua.itemOptions.add(new ItemOption(77, 150));
                                    goiqua.itemOptions.add(new ItemOption(103, 150));
                                    goiqua.itemOptions.add(new ItemOption(101, Util.nextInt(1, 100)));
                                    goiqua.itemOptions.add(new ItemOption(117, Util.nextInt(1, 100)));
                                    goiqua.itemOptions.add(new ItemOption(174, 2025));
                                    InventoryService.gI().addItemBag(player, goiqua, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);

                                    player.diemfam -= 150000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng 1");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Bạn không có điểm , lười vừa thôi" + "\nCần " + (150000 - player.diemfam) + " Điểm Nữa");
                                }
                                break;
                            case 3:
                                if (player.diemfam >= 150000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) Util.nextInt(1959, 1961), 1);
                                    goiqua.itemOptions.add(new ItemOption(50, 150));
                                    goiqua.itemOptions.add(new ItemOption(77, 150));
                                    goiqua.itemOptions.add(new ItemOption(103, 150));
                                    goiqua.itemOptions.add(new ItemOption(101, Util.nextInt(1, 100)));
                                    goiqua.itemOptions.add(new ItemOption(117, Util.nextInt(1, 100)));
                                    goiqua.itemOptions.add(new ItemOption(174, 2025));
                                    InventoryService.gI().addItemBag(player, goiqua, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);

                                    player.diemfam -= 150000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng 1");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Bạn không có điểm , lười vừa thôi" + "\nCần " + (150000 - player.diemfam) + " Điểm Nữa");
                                }
                                break;
                            case 4:
                                if (player.diemfam >= 150000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) Util.nextInt(1336, 1339), 1);
                                    goiqua.itemOptions.add(new ItemOption(50, 150));
                                    goiqua.itemOptions.add(new ItemOption(77, 150));
                                    goiqua.itemOptions.add(new ItemOption(103, 150));
                                    goiqua.itemOptions.add(new ItemOption(101, Util.nextInt(1, 100)));
                                    goiqua.itemOptions.add(new ItemOption(117, Util.nextInt(1, 100)));
                                    goiqua.itemOptions.add(new ItemOption(174, 2025));
                                    InventoryService.gI().addItemBag(player, goiqua, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);

                                    player.diemfam -= 150000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nsố lượng 1");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Bạn không có điểm , lười vừa thôi" + "\ncần " + (150000 - player.diemfam) + " Điểm Nữa");
                                }
                                break;

                            case 5:
                                if (player.diemfam >= 150000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) Util.nextInt(1552, 1554), 1);
                                    goiqua.itemOptions.add(new ItemOption(50, 150));
                                    goiqua.itemOptions.add(new ItemOption(77, 150));
                                    goiqua.itemOptions.add(new ItemOption(103, 150));
                                    goiqua.itemOptions.add(new ItemOption(101, Util.nextInt(1, 100)));
                                    goiqua.itemOptions.add(new ItemOption(117, Util.nextInt(1, 100)));
                                    goiqua.itemOptions.add(new ItemOption(174, 2025));
                                    InventoryService.gI().addItemBag(player, goiqua, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);

                                    player.diemfam -= 150000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng 1");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Bạn không có điểm , lười vừa thôi" + "\nCần " + (150000 - player.diemfam) + " Điểm Nữa");
                                }
                                break;
                        }
                        break;

                    case ConstNpc.doilinhthu:
                        switch (select) {
                            case 0:
                                if (player.diemfam >= 200000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) Util.nextInt(1868, 1882), 1);
                                    goiqua.itemOptions.add(new ItemOption(50, 150));
                                    goiqua.itemOptions.add(new ItemOption(77, 150));
                                    goiqua.itemOptions.add(new ItemOption(103, 150));
                                    goiqua.itemOptions.add(new ItemOption(101, Util.nextInt(1, 100)));
                                    goiqua.itemOptions.add(new ItemOption(117, Util.nextInt(1, 100)));
                                    goiqua.itemOptions.add(new ItemOption(174, 2025));
                                    InventoryService.gI().addItemBag(player, goiqua, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);

                                    player.diemfam -= 200000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng 1");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Bạn không có điểm , lười vừa thôi" + "\nCần " + (200000 - player.diemfam) + " Điểm Nữa");
                                }
                                break;

                            case 1:
                                if (player.diemfam >= 200000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) Util.nextInt(1891, 1909), 1);
                                    goiqua.itemOptions.add(new ItemOption(50, 150));
                                    goiqua.itemOptions.add(new ItemOption(77, 150));
                                    goiqua.itemOptions.add(new ItemOption(103, 150));
                                    goiqua.itemOptions.add(new ItemOption(101, Util.nextInt(1, 100)));
                                    goiqua.itemOptions.add(new ItemOption(117, Util.nextInt(1, 100)));
                                    goiqua.itemOptions.add(new ItemOption(174, 2025));
                                    InventoryService.gI().addItemBag(player, goiqua, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);

                                    player.diemfam -= 200000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng 1");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Bạn không có điểm , lười vừa thôi" + "\nCần " + (200000 - player.diemfam) + " Điểm Nữa");
                                }
                                break;

                            case 2:
                                if (player.diemfam >= 200000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) Util.nextInt(1891, 1909), 1);
                                    goiqua.itemOptions.add(new ItemOption(50, 150));
                                    goiqua.itemOptions.add(new ItemOption(77, 150));
                                    goiqua.itemOptions.add(new ItemOption(103, 150));
                                    goiqua.itemOptions.add(new ItemOption(101, Util.nextInt(1, 100)));
                                    goiqua.itemOptions.add(new ItemOption(117, Util.nextInt(1, 100)));
                                    goiqua.itemOptions.add(new ItemOption(174, 2025));
                                    InventoryService.gI().addItemBag(player, goiqua, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);

                                    player.diemfam -= 200000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng 1");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Bạn không có điểm , lười vừa thôi" + "\nCần " + (200000 - player.diemfam) + " Điểm Nữa");
                                }
                                break;
                            case 3:
                                if (player.diemfam >= 200000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) Util.nextInt(1891, 1909), 1);
                                    goiqua.itemOptions.add(new ItemOption(50, 150));
                                    goiqua.itemOptions.add(new ItemOption(77, 150));
                                    goiqua.itemOptions.add(new ItemOption(103, 150));
                                    goiqua.itemOptions.add(new ItemOption(101, Util.nextInt(1, 100)));
                                    goiqua.itemOptions.add(new ItemOption(117, Util.nextInt(1, 100)));
                                    goiqua.itemOptions.add(new ItemOption(174, 2025));
                                    InventoryService.gI().addItemBag(player, goiqua, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);

                                    player.diemfam -= 200000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng 1");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Bạn không có điểm , lười vừa thôi" + "\nCần " + (200000 - player.diemfam) + " Điểm Nữa");
                                }
                                break;
                            case 4:
                                if (player.diemfam >= 200000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) Util.nextInt(1891, 1909), 1);
                                    goiqua.itemOptions.add(new ItemOption(50, 150));
                                    goiqua.itemOptions.add(new ItemOption(77, 150));
                                    goiqua.itemOptions.add(new ItemOption(103, 150));
                                    goiqua.itemOptions.add(new ItemOption(101, Util.nextInt(1, 100)));
                                    goiqua.itemOptions.add(new ItemOption(117, Util.nextInt(1, 100)));
                                    goiqua.itemOptions.add(new ItemOption(174, 2025));
                                    InventoryService.gI().addItemBag(player, goiqua, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);

                                    player.diemfam -= 200000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng 1");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Bạn không có điểm , lười vừa thôi" + "\nCần " + (200000 - player.diemfam) + " Điểm Nữa");
                                }
                                break;

                            case 5:
                                if (player.diemfam >= 200000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) Util.nextInt(1891, 1909), 1);
                                    goiqua.itemOptions.add(new ItemOption(50, 150));
                                    goiqua.itemOptions.add(new ItemOption(77, 150));
                                    goiqua.itemOptions.add(new ItemOption(103, 150));
                                    goiqua.itemOptions.add(new ItemOption(101, Util.nextInt(1, 100)));
                                    goiqua.itemOptions.add(new ItemOption(117, Util.nextInt(1, 100)));
                                    goiqua.itemOptions.add(new ItemOption(174, 2025));
                                    InventoryService.gI().addItemBag(player, goiqua, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);

                                    player.diemfam -= 200000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng 1");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Bạn không có điểm , lười vừa thôi" + "\nCần " + (200000 - player.diemfam) + " Điểm Nữa");
                                }
                                break;
                        }
                        break;

                    case ConstNpc.doidetu:
                        switch (select) {
                            case 0:
                                if (player.diemfam >= 10000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) Util.nextInt(1418, 1419), 1);
                                    goiqua.itemOptions.add(new ItemOption(174, 2025));
                                    InventoryService.gI().addItemBag(player, goiqua, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);

                                    player.diemfam -= 10000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng 1");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Bạn không có điểm , lười vừa thôi" + "\nCần " + (10000 - player.diemfam) + " Điểm Nữa");
                                }
                                break;

                            case 1:
                                if (player.diemfam >= 10000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) Util.nextInt(1929, 1934), 1);
                                    goiqua.itemOptions.add(new ItemOption(174, 2025));
                                    InventoryService.gI().addItemBag(player, goiqua, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);

                                    player.diemfam -= 10000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng 1");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Bạn không có điểm , lười vừa thôi" + "\nCần " + (10000 - player.diemfam) + " Điểm Nữa");
                                }
                                break;

                            case 2:
                                if (player.diemfam >= 10000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) Util.nextInt(1929, 1934), 1);
                                    goiqua.itemOptions.add(new ItemOption(174, 2025));
                                    InventoryService.gI().addItemBag(player, goiqua, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);

                                    player.diemfam -= 10000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng 1");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Bạn không có điểm , lười vừa thôi" + "\nCần " + (10000 - player.diemfam) + " Điểm Nữa");
                                }
                                break;
                            case 3:
                                if (player.diemfam >= 10000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) Util.nextInt(1929, 1934), 1);
                                    goiqua.itemOptions.add(new ItemOption(174, 2025));
                                    InventoryService.gI().addItemBag(player, goiqua, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);

                                    player.diemfam -= 10000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng 1");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Bạn không có điểm , lười vừa thôi" + "\nCần " + (10000 - player.diemfam) + " Điểm Nữa");
                                }
                                break;
                            case 4:
                                if (player.diemfam >= 10000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) Util.nextInt(1929, 1934), 1);
                                    goiqua.itemOptions.add(new ItemOption(174, 2025));
                                    InventoryService.gI().addItemBag(player, goiqua, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);

                                    player.diemfam -= 10000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng 1");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Bạn không có điểm , lười vừa thôi" + "\nCần " + (10000 - player.diemfam) + " Điểm Nữa");
                                }
                                break;

                            case 5:
                                if (player.diemfam >= 10000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) Util.nextInt(1929, 1934), 1);
                                    goiqua.itemOptions.add(new ItemOption(174, 2025));
                                    InventoryService.gI().addItemBag(player, goiqua, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);

                                    player.diemfam -= 10000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng 1");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Bạn không có điểm , lười vừa thôi" + "\nCần " + (10000 - player.diemfam) + " Điểm Nữa");
                                }
                                break;
                        }
                        break;

                    case ConstNpc.she1:
                        switch (select) {
                            case 0:
                                if (player.diemshe >= 20) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 457, 1);
                                    goiqua.itemOptions.add(new ItemOption(174, 2025));
                                    InventoryService.gI().addItemBag(player, goiqua, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);

                                    player.inventory.ruby += 100000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 100k Ruby ");

                                    player.inventory.gem += 100000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 100k gem ");

                                    player.inventory.gold += 10000000000l;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 10 Tỉ Vàng ");

                                    player.diemshe -= 20;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng 1");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Bạn không có điểm , lười vừa thôi" + "\nCần " + (20 - player.diemshe) + " Điểm Nữa");
                                }
                                break;
                        }
                        break;

                    case ConstNpc.she2:
                        switch (select) {
                            case 0:
                                if (player.diemshe >= 50) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 457, 1);
                                    goiqua.itemOptions.add(new ItemOption(174, 2025));
                                    InventoryService.gI().addItemBag(player, goiqua, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);

                                    player.Diemvip += 10000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 10k Điểm Vip ");

                                    player.diemfam += 5000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 5k Điểm Farm");

                                    player.diemshe -= 50;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng 1");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Bạn không có điểm , lười vừa thôi" + "\nCần " + (50 - player.diemshe) + " Điểm Nữa");
                                }
                                break;
                        }
                        break;

                    case ConstNpc.she3:
                        switch (select) {
                            case 0:
                                if (player.diemshe >= 100) {
                                    Item caitrangthoitrang = ItemService.gI().createNewItem((short) Util.nextInt(220, 224), 10000);
                                    caitrangthoitrang.itemOptions.add(new ItemOption(174, 2025));
                                    InventoryService.gI().addItemBag(player, caitrangthoitrang, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);

                                    player.diemfam += 5000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 5k Điểm Farm");

                                    player.diemshe -= 100;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + caitrangthoitrang.template.name + "\nSố Lượng 1");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Bạn không có điểm , lười vừa thôi" + "\nCần " + (100 - player.diemshe) + " Điểm Nữa");
                                }
                                break;
                        }
                        break;
                    case ConstNpc.she4:
                        switch (select) {
                            case 0:
                                if (player.diemshe >= 150) {
                                    Item caitrangthoitrang = ItemService.gI().createNewItem((short) 457, 1);
                                    caitrangthoitrang.itemOptions.add(new ItemOption(174, 2025));
                                    InventoryService.gI().addItemBag(player, caitrangthoitrang, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);

                                    player.CapTamkjll += 50;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 50 TB");

                                    player.diemfam += 5000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 5k Điểm Farm");

                                    player.diemshe -= 150;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + caitrangthoitrang.template.name + "\nSố Lượng 1");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Bạn không có điểm , lười vừa thôi" + "\nCần " + (150 - player.diemshe) + " Điểm Nữa");
                                }
                                break;
                        }
                        break;

                    case ConstNpc.she5:
                        switch (select) {
                            case 0:
                                if (player.diemshe >= 200) {
                                    Item caitrangthoitrang = ItemService.gI().createNewItem((short) 457, 1);
                                    caitrangthoitrang.itemOptions.add(new ItemOption(174, 2025));
                                    InventoryService.gI().addItemBag(player, caitrangthoitrang, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);

                                    player.nPoint.tiemNang += 100000000000l;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().point(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 100 tỉ tn");

                                    player.diemfam += 5000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 50k Điểm Farm");

                                    player.diemshe -= 200;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + caitrangthoitrang.template.name + "\nSố Lượng 1");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Bạn không có điểm , lười vừa thôi" + "\nCần " + (200 - player.diemshe) + " Điểm Nữa");
                                }
                                break;
                        }
                        break;

                    case ConstNpc.she6:
                        switch (select) {
                            case 0:
                                if (player.diemshe >= 250) {
                                    Item caitrangthoitrang = ItemService.gI().createNewItem((short) 457, 1);
                                    caitrangthoitrang.itemOptions.add(new ItemOption(174, 2025));
                                    InventoryService.gI().addItemBag(player, caitrangthoitrang, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);

                                    player.nPoint.dameg += 10000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().point(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 10k dameg");

                                    player.nPoint.hpg += 10000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().point(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 10k hpg");

                                    player.nPoint.mpg += 10000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().point(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 10k kig");

                                    player.diemfam += 5000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 50k Điểm Farm");

                                    player.diemshe -= 250;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + caitrangthoitrang.template.name + "\nSố Lượng 1");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Bạn không có điểm , lười vừa thôi" + "\nCần " + (200 - player.diemshe) + " Điểm Nữa");
                                }
                                break;
                        }
                        break;

                    case ConstNpc.goidapdo:
                        switch (select) {
                            case 0:
                                if (player.getSession().vnd >= 150000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 457, 100000);
                                    goiqua.itemOptions.add(new ItemOption(30, 1));
                                    InventoryService.gI().addItemBag(player, goiqua, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);

                                    Item caitrangthoitrang = ItemService.gI().createNewItem((short) Util.nextInt(629, 637));
                                    caitrangthoitrang.itemOptions.add(new ItemOption(50, 10000));
                                    caitrangthoitrang.itemOptions.add(new ItemOption(77, 10000));
                                    caitrangthoitrang.itemOptions.add(new ItemOption(103, 10000));
                                    caitrangthoitrang.itemOptions.add(new ItemOption(78, 500));
                                    caitrangthoitrang.itemOptions.add(new ItemOption(30, 1));
                                    InventoryService.gI().addItemBag(player, caitrangthoitrang, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được Random cải trang  ");

                                    Item sethuyetma = ItemService.gI().createNewItem((short) 1366);
                                    sethuyetma.itemOptions.add(new ItemOption(47, 10000000));
                                    sethuyetma.itemOptions.add(new ItemOption(35, 10));
                                    InventoryService.gI().addItemBag(player, sethuyetma, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được set huyết ma ");

                                    Item sethuyetma1 = ItemService.gI().createNewItem((short) 1367);
                                    sethuyetma1.itemOptions.add(new ItemOption(6, 10000000));
                                    sethuyetma1.itemOptions.add(new ItemOption(35, 1));
                                    InventoryService.gI().addItemBag(player, sethuyetma1, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được set huyết ma ");

                                    Item sethuyetma2 = ItemService.gI().createNewItem((short) 1368);
                                    sethuyetma2.itemOptions.add(new ItemOption(0, 1000000));
                                    sethuyetma2.itemOptions.add(new ItemOption(35, 1));
                                    InventoryService.gI().addItemBag(player, sethuyetma2, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được set huyết ma ");

                                    Item sethuyetma3 = ItemService.gI().createNewItem((short) 1369);
                                    sethuyetma3.itemOptions.add(new ItemOption(7, 10000000));
                                    sethuyetma3.itemOptions.add(new ItemOption(35, 1));
                                    InventoryService.gI().addItemBag(player, sethuyetma3, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được set huyết ma ");

                                    Item sethuyetma4 = ItemService.gI().createNewItem((short) 1370);
                                    sethuyetma4.itemOptions.add(new ItemOption(14, 25));
                                    sethuyetma4.itemOptions.add(new ItemOption(35, 1));
                                    InventoryService.gI().addItemBag(player, sethuyetma4, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được set huyết ma ");

                                    player.inventory.ruby += 100000000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 100tr Ruby ");
                                    player.inventory.gem += 100000000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 100tr Ngọc Xanh");

                                    player.Tindung += 150;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 150 Điểm ");

                                    PlayerDAO.subvnd(player, 150000);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name);
                                } else {
                                    Service.getInstance().sendThongBao(player, "Bạn Đã Hết Tiền");
                                }
                                break;

                            case 1:
                                if (player.getSession().vnd >= 300000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 457, 250000);
                                    goiqua.itemOptions.add(new ItemOption(30, 1));
                                    InventoryService.gI().addItemBag(player, goiqua, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);

                                    Item caitrangthoitrang = ItemService.gI().createNewItem((short) Util.nextInt(629, 637));
                                    caitrangthoitrang.itemOptions.add(new ItemOption(50, 25000));
                                    caitrangthoitrang.itemOptions.add(new ItemOption(77, 25000));
                                    caitrangthoitrang.itemOptions.add(new ItemOption(103, 25000));
                                    caitrangthoitrang.itemOptions.add(new ItemOption(78, 500));
                                    caitrangthoitrang.itemOptions.add(new ItemOption(30, 1));
                                    InventoryService.gI().addItemBag(player, caitrangthoitrang, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được Random cải trang ");

                                    Item sethuyetma = ItemService.gI().createNewItem((short) 1366);
                                    sethuyetma.itemOptions.add(new ItemOption(47, 25000000));
                                    sethuyetma.itemOptions.add(new ItemOption(35, 1));
                                    InventoryService.gI().addItemBag(player, sethuyetma, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được set huyết ma ");

                                    Item sethuyetma1 = ItemService.gI().createNewItem((short) 1367);
                                    sethuyetma1.itemOptions.add(new ItemOption(6, 20000000));
                                    sethuyetma1.itemOptions.add(new ItemOption(35, 1));
                                    InventoryService.gI().addItemBag(player, sethuyetma1, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được set huyết ma ");

                                    Item sethuyetma2 = ItemService.gI().createNewItem((short) 1368);
                                    sethuyetma2.itemOptions.add(new ItemOption(0, 2000000));
                                    sethuyetma2.itemOptions.add(new ItemOption(35, 1));
                                    InventoryService.gI().addItemBag(player, sethuyetma2, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được set huyết ma ");

                                    Item sethuyetma3 = ItemService.gI().createNewItem((short) 1369);
                                    sethuyetma3.itemOptions.add(new ItemOption(7, 20000000));
                                    sethuyetma3.itemOptions.add(new ItemOption(35, 1));
                                    InventoryService.gI().addItemBag(player, sethuyetma3, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được set huyết ma ");

                                    Item sethuyetma4 = ItemService.gI().createNewItem((short) 1370);
                                    sethuyetma4.itemOptions.add(new ItemOption(14, 30));
                                    sethuyetma4.itemOptions.add(new ItemOption(35, 1));
                                    InventoryService.gI().addItemBag(player, sethuyetma4, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được set huyết ma ");

                                    player.inventory.ruby += 200000000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 200tr Ruby ");
                                    player.inventory.gem += 200000000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 200tr Ngọc Xanh");

                                    player.Tindung += 300;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 300 Điểm ");

                                    PlayerDAO.subvnd(player, 300000);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name);
                                } else {
                                    Service.getInstance().sendThongBao(player, "Bạn Đã Hết Tiền");
                                }
                                break;

                            case 2:
                                if (player.getSession().vnd >= 600000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 457, 650000);
                                    goiqua.itemOptions.add(new ItemOption(30, 1));
                                    InventoryService.gI().addItemBag(player, goiqua, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);

                                    Item caitrangthoitrang = ItemService.gI().createNewItem((short) Util.nextInt(629, 637));
                                    caitrangthoitrang.itemOptions.add(new ItemOption(50, 35000));
                                    caitrangthoitrang.itemOptions.add(new ItemOption(77, 35000));
                                    caitrangthoitrang.itemOptions.add(new ItemOption(103, 35000));
                                    caitrangthoitrang.itemOptions.add(new ItemOption(78, 500));
                                    caitrangthoitrang.itemOptions.add(new ItemOption(30, 1));
                                    InventoryService.gI().addItemBag(player, caitrangthoitrang, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được Random cải trang thời trang ");

                                    Item sethuyetma = ItemService.gI().createNewItem((short) 1366);
                                    sethuyetma.itemOptions.add(new ItemOption(47, 35000000));
                                    sethuyetma.itemOptions.add(new ItemOption(35, 1));
                                    InventoryService.gI().addItemBag(player, sethuyetma, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được set huyết ma ");

                                    Item sethuyetma1 = ItemService.gI().createNewItem((short) 1367);
                                    sethuyetma1.itemOptions.add(new ItemOption(6, 35000000));
                                    sethuyetma1.itemOptions.add(new ItemOption(30, 1));
                                    InventoryService.gI().addItemBag(player, sethuyetma1, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được set huyết ma ");

                                    Item sethuyetma2 = ItemService.gI().createNewItem((short) 1368);
                                    sethuyetma2.itemOptions.add(new ItemOption(0, 350000));
                                    sethuyetma2.itemOptions.add(new ItemOption(35, 1));
                                    InventoryService.gI().addItemBag(player, sethuyetma2, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được set huyết ma ");

                                    Item sethuyetma3 = ItemService.gI().createNewItem((short) 1369);
                                    sethuyetma3.itemOptions.add(new ItemOption(7, 35000000));
                                    sethuyetma3.itemOptions.add(new ItemOption(35, 1));
                                    InventoryService.gI().addItemBag(player, sethuyetma3, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được set huyết ma ");

                                    Item sethuyetma4 = ItemService.gI().createNewItem((short) 1370);
                                    sethuyetma4.itemOptions.add(new ItemOption(14, 35));
                                    sethuyetma4.itemOptions.add(new ItemOption(35, 1));
                                    InventoryService.gI().addItemBag(player, sethuyetma4, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được set huyết ma ");

                                    player.inventory.ruby += 300000000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 300tr Ruby ");
                                    player.inventory.gem += 300000000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 300tr Ngọc Xanh");

                                    player.Tindung += 600;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 600 Điểm Tín Dụng");

                                    PlayerDAO.subvnd(player, 600000);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name);
                                } else {
                                    Service.getInstance().sendThongBao(player, "Bạn Đã Hết Tiền");
                                }
                                break;

                            case 3:
                                if (player.getSession().vnd >= 900000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 457, 900000);
                                    goiqua.itemOptions.add(new ItemOption(30, 1));
                                    InventoryService.gI().addItemBag(player, goiqua, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);

                                    Item caitrangthoitrang = ItemService.gI().createNewItem((short) Util.nextInt(629, 637));
                                    caitrangthoitrang.itemOptions.add(new ItemOption(50, 100000));
                                    caitrangthoitrang.itemOptions.add(new ItemOption(77, 100000));
                                    caitrangthoitrang.itemOptions.add(new ItemOption(103, 100000));
                                    caitrangthoitrang.itemOptions.add(new ItemOption(78, 500));
                                    caitrangthoitrang.itemOptions.add(new ItemOption(30, 1));
                                    InventoryService.gI().addItemBag(player, caitrangthoitrang, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được Random cải trang thời trang ");

                                    Item sethuyetma = ItemService.gI().createNewItem((short) 1366);
                                    sethuyetma.itemOptions.add(new ItemOption(47, 50000000));
                                    sethuyetma.itemOptions.add(new ItemOption(35, 1));
                                    InventoryService.gI().addItemBag(player, sethuyetma, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được set huyết ma ");

                                    Item sethuyetma1 = ItemService.gI().createNewItem((short) 1367);
                                    sethuyetma1.itemOptions.add(new ItemOption(6, 50000000));
                                    sethuyetma1.itemOptions.add(new ItemOption(35, 1));
                                    InventoryService.gI().addItemBag(player, sethuyetma1, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được set huyết ma ");

                                    Item sethuyetma2 = ItemService.gI().createNewItem((short) 1368);
                                    sethuyetma2.itemOptions.add(new ItemOption(0, 5000000));
                                    sethuyetma2.itemOptions.add(new ItemOption(35, 1));
                                    InventoryService.gI().addItemBag(player, sethuyetma2, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được set huyết ma ");

                                    Item sethuyetma3 = ItemService.gI().createNewItem((short) 1369);
                                    sethuyetma3.itemOptions.add(new ItemOption(7, 50000000));
                                    sethuyetma3.itemOptions.add(new ItemOption(35, 1));
                                    InventoryService.gI().addItemBag(player, sethuyetma3, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được set huyết ma ");

                                    Item sethuyetma4 = ItemService.gI().createNewItem((short) 1370);
                                    sethuyetma4.itemOptions.add(new ItemOption(14, 40));
                                    sethuyetma4.itemOptions.add(new ItemOption(35, 1));
                                    InventoryService.gI().addItemBag(player, sethuyetma4, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được set huyết ma ");

                                    player.inventory.ruby += 50000000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 500tr Ruby ");
                                    player.inventory.gem += 500000000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 500tr Ngọc Xanh");

                                    player.Tindung += 900;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 900 Điểm Tín Dụng");

                                    PlayerDAO.subvnd(player, 900000);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name);
                                } else {
                                    Service.getInstance().sendThongBao(player, "Bạn Đã Hết Tiền");
                                }
                                break;

                        }
                        break;

                    case ConstNpc.napdau:
                        switch (select) {
                            case 0:
                                if (player.getSession().vnd >= 99000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 1111, 10000);
                                    goiqua.itemOptions.add(new ItemOption(30, 1));
                                    InventoryService.gI().addItemBag(player, goiqua, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);

                                    player.LbTamkjll += 50;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 50 cấp Nhập Ma");

                                    player.CapTamkjll += 50;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 50 cấp Tiên Bang");

                                    player.nPoint.dameg += 1000000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().point(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 1M Dame Gốc ");

                                    player.nPoint.hpg += 1000000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().point(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 1M hp Gốc ");

                                    player.nPoint.mpg += 1000000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().point(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 1M ki Gốc ");

                                    player.Tindung += 99;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 99 Điểm Tẩy Luyện");

                                    PlayerDAO.subvnd(player, 99000);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name);
                                } else {
                                    Service.getInstance().sendThongBao(player, "Bạn Đã Hết Tiền");
                                }
                                break;

                            case 1:
                                if (player.getSession().vnd >= 199000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 1111, 25000);
                                    goiqua.itemOptions.add(new ItemOption(30, 1));
                                    InventoryService.gI().addItemBag(player, goiqua, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);

                                    player.LbTamkjll += 100;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 100 cấp Nhập Ma");

                                    player.CapTamkjll += 100;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 100 cấp Tiên Bang");

                                    player.nPoint.dameg += 2500000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().point(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 1M5 Dame Gốc ");

                                    player.nPoint.hpg += 1500000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().point(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 1M5 hp Gốc ");

                                    player.nPoint.mpg += 1500000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().point(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 1M5 ki Gốc ");

                                    player.Tindung += 199;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 199 Điểm Tẩy Luyện");

                                    PlayerDAO.subvnd(player, 199000);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name);
                                } else {
                                    Service.getInstance().sendThongBao(player, "Bạn Đã Hết Tiền");
                                }
                                break;

                            case 2:
                                if (player.getSession().vnd >= 299000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 1111, 40000);
                                    goiqua.itemOptions.add(new ItemOption(30, 1));
                                    InventoryService.gI().addItemBag(player, goiqua, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);

                                    player.LbTamkjll += 150;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 150 cấp Nhập Ma");

                                    player.CapTamkjll += 150;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 150 cấp Tiên Bang");

                                    player.nPoint.dameg += 3000000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().point(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 3M Dame Gốc ");

                                    player.nPoint.hpg += 3000000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().point(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 3M hp Gốc ");

                                    player.nPoint.mpg += 3000000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().point(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 3M ki Gốc ");

                                    player.Tindung += 299;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 299 Điểm Tẩy Luyện");

                                    PlayerDAO.subvnd(player, 299000);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name);
                                } else {
                                    Service.getInstance().sendThongBao(player, "Bạn Đã Hết Tiền");
                                }
                                break;

                            case 3:
                                if (player.getSession().vnd >= 600000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 1111, 70000);
                                    goiqua.itemOptions.add(new ItemOption(30, 1));
                                    InventoryService.gI().addItemBag(player, goiqua, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);

                                    player.LbTamkjll += 300;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 300 cấp Nhập Ma");

                                    player.CapTamkjll += 300;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 300 cấp Tiên Bang");

                                    player.nPoint.dameg += 7000000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().point(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 7M Dame Gốc ");

                                    player.nPoint.hpg += 7000000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().point(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 7M hp Gốc ");

                                    player.nPoint.mpg += 7000000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().point(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 7M ki Gốc ");

                                    player.Tindung += 600;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 600 Điểm Tẩy Luyện");

                                    PlayerDAO.subvnd(player, 600000);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name);
                                } else {
                                    Service.getInstance().sendThongBao(player, "Bạn Đã Hết Tiền");
                                }
                                break;

                        }
                        break;
                    case ConstNpc.nangcapcanh:
                        switch (select) {
                            case 0:
                                if (player.getSession().vnd >= 150000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 674, 50000);
                                    goiqua.itemOptions.add(new ItemOption(30, 1));
                                    InventoryService.gI().addItemBag(player, goiqua, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);

                                    Item caitrangthoitrang = ItemService.gI().createNewItem((short) 1599);
                                    caitrangthoitrang.itemOptions.add(new ItemOption(50, 5));
                                    caitrangthoitrang.itemOptions.add(new ItemOption(77, 5));
                                    caitrangthoitrang.itemOptions.add(new ItemOption(103, 5));
                                    caitrangthoitrang.itemOptions.add(new ItemOption(30, 1));
                                    InventoryService.gI().addItemBag(player, caitrangthoitrang, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được Cánh Lv1 ");

                                    player.diemfam += 20000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 20.000 Điểm TrainFarm");

                                    player.Tindung += 150;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 150 Điểm Tín Dụng");

                                    PlayerDAO.subvnd(player, 150000);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name);
                                } else {
                                    Service.getInstance().sendThongBao(player, "Bạn Đã Hết Tiền");
                                }
                                break;

                            case 1:
                                if (player.getSession().vnd >= 300000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 674, 110000);
                                    goiqua.itemOptions.add(new ItemOption(30, 1));
                                    InventoryService.gI().addItemBag(player, goiqua, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);

                                    Item caitrangthoitrang = ItemService.gI().createNewItem((short) 1599);
                                    caitrangthoitrang.itemOptions.add(new ItemOption(50, 5));
                                    caitrangthoitrang.itemOptions.add(new ItemOption(77, 5));
                                    caitrangthoitrang.itemOptions.add(new ItemOption(103, 5));
                                    caitrangthoitrang.itemOptions.add(new ItemOption(30, 1));
                                    InventoryService.gI().addItemBag(player, caitrangthoitrang, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được Cánh Lv1 ");

                                    player.diemfam += 45000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 45.000 Điểm TrainFarm");

                                    player.Tindung += 350;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 350 Điểm Tín Dụng");

                                    PlayerDAO.subvnd(player, 300000);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name);
                                } else {
                                    Service.getInstance().sendThongBao(player, "Bạn Đã Hết Tiền");
                                }
                                break;

                            case 2:
                                if (player.getSession().vnd >= 600000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 674, 250000);
                                    goiqua.itemOptions.add(new ItemOption(30, 1));
                                    InventoryService.gI().addItemBag(player, goiqua, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);

                                    Item caitrangthoitrang = ItemService.gI().createNewItem((short) 1599);
                                    caitrangthoitrang.itemOptions.add(new ItemOption(50, 5));
                                    caitrangthoitrang.itemOptions.add(new ItemOption(77, 5));
                                    caitrangthoitrang.itemOptions.add(new ItemOption(103, 5));
                                    caitrangthoitrang.itemOptions.add(new ItemOption(30, 1));
                                    InventoryService.gI().addItemBag(player, caitrangthoitrang, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được Cánh Lv1 ");

                                    player.diemfam += 100000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 100.000 Điểm TrainFarm");

                                    player.Tindung += 700;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 700 Điểm Tín Dụng");

                                    PlayerDAO.subvnd(player, 600000);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name);
                                } else {
                                    Service.getInstance().sendThongBao(player, "Bạn Đã Hết Tiền");
                                }
                                break;

                            case 3:

                                if (player.getSession().vnd >= 900000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 674, 500000);
                                    goiqua.itemOptions.add(new ItemOption(30, 1));
                                    InventoryService.gI().addItemBag(player, goiqua, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);

                                    Item caitrangthoitrang = ItemService.gI().createNewItem((short) 1599);
                                    caitrangthoitrang.itemOptions.add(new ItemOption(50, 5));
                                    caitrangthoitrang.itemOptions.add(new ItemOption(77, 5));
                                    caitrangthoitrang.itemOptions.add(new ItemOption(103, 5));
                                    caitrangthoitrang.itemOptions.add(new ItemOption(30, 1));
                                    InventoryService.gI().addItemBag(player, caitrangthoitrang, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được Cánh Lv1 ");

                                    player.diemfam += 200000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 200.000 Điểm TrainFarm");

                                    player.Tindung += 1000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 1000 Điểm Tín Dụng");

                                    PlayerDAO.subvnd(player, 600000);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name);
                                } else {
                                    Service.getInstance().sendThongBao(player, "Bạn Đã Hết Tiền");
                                }
                                break;

                        }
                        break;

                    case ConstNpc.goichuyensinh:
                        switch (select) {
                            case 0:
                                if (player.getSession().vnd >= 99000) {
                                    Item caitrangthoitrang = ItemService.gI().createNewItem((short) Util.nextInt(1989, 1990));
                                    caitrangthoitrang.itemOptions.add(new ItemOption(50, 10000));
                                    caitrangthoitrang.itemOptions.add(new ItemOption(77, 10000));
                                    caitrangthoitrang.itemOptions.add(new ItemOption(103, 10000));
                                    caitrangthoitrang.itemOptions.add(new ItemOption(101, 10000));
                                    caitrangthoitrang.itemOptions.add(new ItemOption(107, Util.nextInt(1, 18)));
                                    caitrangthoitrang.itemOptions.add(new ItemOption(30, 1));
                                    InventoryService.gI().addItemBag(player, caitrangthoitrang, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được Thú Vip ");

                                    player.ExpTamkjll += 20000000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 20M EXP Tiên Bang");

                                    player.TamkjllCS += 5;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 5 cấp chuyển sinh");

                                    player.Tindung += 99;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 99 Điểm Tín Dụng");

                                    PlayerDAO.subvnd(player, 99000);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + caitrangthoitrang.template.name);
                                } else {
                                    Service.getInstance().sendThongBao(player, "Bạn Đã Hết Tiền");
                                }
                                break;

                            case 1:
                                if (player.getSession().vnd >= 199000) {
                                    Item caitrangthoitrang = ItemService.gI().createNewItem((short) Util.nextInt(1989, 1990));
                                    caitrangthoitrang.itemOptions.add(new ItemOption(50, 20000));
                                    caitrangthoitrang.itemOptions.add(new ItemOption(77, 20000));
                                    caitrangthoitrang.itemOptions.add(new ItemOption(103, 20000));
                                    caitrangthoitrang.itemOptions.add(new ItemOption(101, 10000));
                                    caitrangthoitrang.itemOptions.add(new ItemOption(107, Util.nextInt(1, 30)));
                                    caitrangthoitrang.itemOptions.add(new ItemOption(30, 1));
                                    InventoryService.gI().addItemBag(player, caitrangthoitrang, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được Thú Vip ");

                                    player.ExpTamkjll += 40000000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 40M EXP Tiên Bang");

                                    player.TamkjllCS += 10;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 10 cấp chuyển sinh");

                                    player.Tindung += 199;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 199 Điểm Tín Dụng");

                                    PlayerDAO.subvnd(player, 199000);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + caitrangthoitrang.template.name);
                                } else {
                                    Service.getInstance().sendThongBao(player, "Bạn Đã Hết Tiền");
                                }
                                break;

                            case 2:
                                if (player.getSession().vnd >= 299000) {
                                    Item caitrangthoitrang = ItemService.gI().createNewItem((short) Util.nextInt(1989, 1990));
                                    caitrangthoitrang.itemOptions.add(new ItemOption(50, 30000));
                                    caitrangthoitrang.itemOptions.add(new ItemOption(77, 30000));
                                    caitrangthoitrang.itemOptions.add(new ItemOption(103, 30000));
                                    caitrangthoitrang.itemOptions.add(new ItemOption(101, 30000));
                                    caitrangthoitrang.itemOptions.add(new ItemOption(107, Util.nextInt(1, 50)));
                                    caitrangthoitrang.itemOptions.add(new ItemOption(30, 1));
                                    InventoryService.gI().addItemBag(player, caitrangthoitrang, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được Thú Vip ");

                                    player.ExpTamkjll += 60000000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 60M EXP Tiên Bang");

                                    player.TamkjllCS += 15;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 15 cấp chuyển sinh");

                                    player.Tindung += 299;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 299 Điểm Tín Dụng");

                                    PlayerDAO.subvnd(player, 299000);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + caitrangthoitrang.template.name);
                                } else {
                                    Service.getInstance().sendThongBao(player, "Bạn Đã Hết Tiền");
                                }
                                break;

                            case 3:

                                if (player.getSession().vnd >= 499000) {
                                    Item caitrangthoitrang = ItemService.gI().createNewItem((short) Util.nextInt(1989, 1990));
                                    caitrangthoitrang.itemOptions.add(new ItemOption(50, 60000));
                                    caitrangthoitrang.itemOptions.add(new ItemOption(77, 60000));
                                    caitrangthoitrang.itemOptions.add(new ItemOption(103, 60000));
                                    caitrangthoitrang.itemOptions.add(new ItemOption(101, 60000));
                                    caitrangthoitrang.itemOptions.add(new ItemOption(107, Util.nextInt(1, 99)));
                                    caitrangthoitrang.itemOptions.add(new ItemOption(30, 1));
                                    InventoryService.gI().addItemBag(player, caitrangthoitrang, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được Thú Vip ");

                                    player.ExpTamkjll += 100000000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 100M EXP Tiên Bang");

                                    player.TamkjllCS += 20;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 20 cấp chuyển sinh");

                                    player.Tindung += 499;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 499 Điểm Tín Dụng");

                                    PlayerDAO.subvnd(player, 499000);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + caitrangthoitrang.template.name);
                                } else {
                                    Service.getInstance().sendThongBao(player, "Bạn Đã Hết Tiền");
                                }
                                break;

                        }
                        break;

                    case ConstNpc.chualanh://  "Mua\n120k Vnđ\nGiá Trị\n100%", "Mua\n239k Vnđ\nGiá Trị\n200%", "Mua\n439k Vnđ\nGiá Trị\n300%", "Mua\n860k Vnđ\nGiá Trị\n500%"
                        switch (select) {
                            case 0:
                                if (player.getSession().vnd >= 120000) {
                                    Item caitrangthoitrang = ItemService.gI().createNewItem((short) 1983, 99000);
                                    InventoryService.gI().addItemBag(player, caitrangthoitrang, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được Đá Qúy ");

                                    Item caitrangthoitrang1 = ItemService.gI().createNewItem((short) 981, 1);
                                    caitrangthoitrang1.itemOptions.add(new ItemOption(30, 200));
                                    InventoryService.gI().addItemBag(player, caitrangthoitrang1, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được Nrsc ");

                                    player.Tamkjlltutien[2] += 1;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 1 Sao Thiên Phú Tu Tiên ");

                                    player.Tamkjlltutien[0] += 10000000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 10M EXP Tu Tiên ");

                                    player.Tindung += 120;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 120 Điểm Tín Dụng");

                                    PlayerDAO.subvnd(player, 120000);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + caitrangthoitrang.template.name);
                                } else {
                                    Service.getInstance().sendThongBao(player, "Bạn Đã Hết Tiền");
                                }
                                break;

                            case 1:
                                if (player.getSession().vnd >= 298000) {
                                    Item caitrangthoitrang = ItemService.gI().createNewItem((short) 1983, 199000);
                                    InventoryService.gI().addItemBag(player, caitrangthoitrang, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được Đá Qúy ");

                                    Item caitrangthoitrang1 = ItemService.gI().createNewItem((short) 981, 2);
                                    caitrangthoitrang1.itemOptions.add(new ItemOption(30, 200));
                                    InventoryService.gI().addItemBag(player, caitrangthoitrang1, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được Nrsc ");

                                    player.Tamkjlltutien[2] += 3;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 3 Sao Thiên Phú Tu Tiên ");

                                    player.Tamkjlltutien[0] += 20000000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 30M EXP Tu Tiên ");

                                    player.Tindung += 298;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 298 Điểm Tín Dụng");

                                    PlayerDAO.subvnd(player, 298000);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + caitrangthoitrang.template.name);
                                } else {
                                    Service.getInstance().sendThongBao(player, "Bạn Đã Hết Tiền");
                                }
                                break;

                            case 2:
                                if (player.getSession().vnd >= 600000) {
                                    Item caitrangthoitrang = ItemService.gI().createNewItem((short) 1983, 499000);
                                    InventoryService.gI().addItemBag(player, caitrangthoitrang, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được Đá Qúy ");

                                    Item caitrangthoitrang1 = ItemService.gI().createNewItem((short) 981, 3);
                                    caitrangthoitrang1.itemOptions.add(new ItemOption(30, 200));
                                    InventoryService.gI().addItemBag(player, caitrangthoitrang1, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được Nrsc ");

                                    player.Tamkjlltutien[2] += 6;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 6 Sao Thiên Phú Tu Tiên ");

                                    player.Tamkjlltutien[0] += 30000000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 60M EXP Tu Tiên ");

                                    player.Tindung += 600;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 600 Điểm Tín Dụng");

                                    PlayerDAO.subvnd(player, 600000);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + caitrangthoitrang.template.name);
                                } else {
                                    Service.getInstance().sendThongBao(player, "Bạn Đã Hết Tiền");
                                }
                                break;

                            case 3:

                                if (player.getSession().vnd >= 1200000) {
                                    Item caitrangthoitrang = ItemService.gI().createNewItem((short) 1983, 799000);
                                    InventoryService.gI().addItemBag(player, caitrangthoitrang, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được Đá Qúy ");

                                    Item caitrangthoitrang1 = ItemService.gI().createNewItem((short) 981, 5);
                                    caitrangthoitrang1.itemOptions.add(new ItemOption(30, 200));
                                    InventoryService.gI().addItemBag(player, caitrangthoitrang1, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được Nrsc ");

                                    player.Tamkjlltutien[2] += 10;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 10 Sao Thiên Phú Tu Tiên ");

                                    player.Tamkjlltutien[0] += 50000000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 100M EXP Tu Tiên ");

                                    player.Tindung += 1200;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 1200 Điểm Tín Dụng");

                                    PlayerDAO.subvnd(player, 1200000);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + caitrangthoitrang.template.name);
                                } else {
                                    Service.getInstance().sendThongBao(player, "Bạn Đã Hết Tiền");
                                }
                                break;
                        }
                        break;

                    case ConstNpc.goispl:
                        switch (select) {
                            case 0:
                                if (player.Tindung >= 888) {
                                    Item caitrangthoitrang = ItemService.gI().createNewItem((short) 1983, 100000);
                                    InventoryService.gI().addItemBag(player, caitrangthoitrang, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được Đá Qúy ");

                                    Item caitrangthoitrang1 = ItemService.gI().createNewItem((short) 2058, 10);
                                    caitrangthoitrang1.itemOptions.add(new ItemOption(50, 200));
                                    InventoryService.gI().addItemBag(player, caitrangthoitrang1, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + caitrangthoitrang1.template.name);

                                    Item caitrangthoitrang2 = ItemService.gI().createNewItem((short) 2059, 10);
                                    caitrangthoitrang2.itemOptions.add(new ItemOption(77, 200));
                                    InventoryService.gI().addItemBag(player, caitrangthoitrang2, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + caitrangthoitrang2.template.name);

                                    Item caitrangthoitrang3 = ItemService.gI().createNewItem((short) 2060, 10);
                                    caitrangthoitrang3.itemOptions.add(new ItemOption(103, 200));
                                    InventoryService.gI().addItemBag(player, caitrangthoitrang3, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + caitrangthoitrang3.template.name);

                                    player.Tindung -= 888;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + caitrangthoitrang.template.name);
                                } else {
                                    Service.getInstance().sendThongBao(player, "Bạn Đã Hết Tiền");
                                }
                                break;

                            case 1:
                                if (player.Tindung >= 1888) {
                                    Item caitrangthoitrang = ItemService.gI().createNewItem((short) 1983, 200000);
                                    InventoryService.gI().addItemBag(player, caitrangthoitrang, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được Đá Qúy ");

                                    Item caitrangthoitrang1 = ItemService.gI().createNewItem((short) 2058, 10);
                                    caitrangthoitrang1.itemOptions.add(new ItemOption(50, 400));
                                    InventoryService.gI().addItemBag(player, caitrangthoitrang1, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + caitrangthoitrang1.template.name);

                                    Item caitrangthoitrang2 = ItemService.gI().createNewItem((short) 2059, 10);
                                    caitrangthoitrang2.itemOptions.add(new ItemOption(77, 400));
                                    InventoryService.gI().addItemBag(player, caitrangthoitrang2, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + caitrangthoitrang2.template.name);

                                    Item caitrangthoitrang3 = ItemService.gI().createNewItem((short) 2060, 10);
                                    caitrangthoitrang3.itemOptions.add(new ItemOption(103, 400));
                                    InventoryService.gI().addItemBag(player, caitrangthoitrang3, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + caitrangthoitrang3.template.name);

                                    player.Tindung -= 1888;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + caitrangthoitrang.template.name);
                                } else {
                                    Service.getInstance().sendThongBao(player, "Bạn Đã Hết Tiền");
                                }
                                break;

                            case 2:
                                if (player.Tindung >= 2888) {
                                    Item caitrangthoitrang = ItemService.gI().createNewItem((short) 1983, 300000);
                                    InventoryService.gI().addItemBag(player, caitrangthoitrang, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được Đá Qúy ");

                                    Item caitrangthoitrang1 = ItemService.gI().createNewItem((short) 2058, 10);
                                    caitrangthoitrang1.itemOptions.add(new ItemOption(50, 600));
                                    InventoryService.gI().addItemBag(player, caitrangthoitrang1, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + caitrangthoitrang1.template.name);

                                    Item caitrangthoitrang2 = ItemService.gI().createNewItem((short) 2059, 10);
                                    caitrangthoitrang2.itemOptions.add(new ItemOption(77, 600));
                                    InventoryService.gI().addItemBag(player, caitrangthoitrang2, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + caitrangthoitrang2.template.name);

                                    Item caitrangthoitrang3 = ItemService.gI().createNewItem((short) 2060, 10);
                                    caitrangthoitrang3.itemOptions.add(new ItemOption(103, 600));
                                    InventoryService.gI().addItemBag(player, caitrangthoitrang3, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + caitrangthoitrang3.template.name);

                                    player.Tindung -= 2888;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + caitrangthoitrang.template.name);
                                } else {
                                    Service.getInstance().sendThongBao(player, "Bạn Đã Hết Tiền");
                                }
                                break;

                            case 3:

                                if (player.Tindung >= 4500) {
                                    Item caitrangthoitrang = ItemService.gI().createNewItem((short) 1983, 500000);
                                    InventoryService.gI().addItemBag(player, caitrangthoitrang, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được Đá Qúy ");

                                    Item caitrangthoitrang1 = ItemService.gI().createNewItem((short) 2058, 10);
                                    caitrangthoitrang1.itemOptions.add(new ItemOption(50, 800));
                                    InventoryService.gI().addItemBag(player, caitrangthoitrang1, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + caitrangthoitrang1.template.name);

                                    Item caitrangthoitrang2 = ItemService.gI().createNewItem((short) 2059, 10);
                                    caitrangthoitrang2.itemOptions.add(new ItemOption(77, 800));
                                    InventoryService.gI().addItemBag(player, caitrangthoitrang2, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + caitrangthoitrang2.template.name);

                                    Item caitrangthoitrang3 = ItemService.gI().createNewItem((short) 2060, 10);
                                    caitrangthoitrang3.itemOptions.add(new ItemOption(103, 800));
                                    InventoryService.gI().addItemBag(player, caitrangthoitrang3, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + caitrangthoitrang3.template.name);

                                    player.Tindung -= 4500;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + caitrangthoitrang.template.name);
                                } else {
                                    Service.getInstance().sendThongBao(player, "Bạn Đã Hết Tiền");
                                }
                                break;
                        }
                        break;

                    case ConstNpc.doihanhtinhde:
                        switch (select) {
                            case 0: {
                                if (player.diemfam >= 100000) {
                                    if (player.pet == null) {
                                        Service.gI().sendThongBaoFromAdmin(player, "Bạn Chưa có đệ!");
                                        break;
                                    }

                                    player.pet.gender = 0;
                                    player.diemfam -= 100000;
                                    ChangeMapService.gI().exitMap(player.pet);
                                    Service.gI().sendThongBaoFromAdmin(player, "Đổi hành tinh đệ Trái Đất thành công!");
                                } else {
                                    Service.getInstance().sendThongBao(player, "không Đủ 100k Điểm Farm");
                                }
                                break;

                            }
                            case 1: {
                                if (player.diemfam >= 100000) {
                                    if (player.pet == null) {
                                        Service.gI().sendThongBaoFromAdmin(player, "Bạn Chưa có đệ!");
                                        break;
                                    }

                                    player.pet.gender = 2;
                                    player.diemfam -= 100000;
                                    ChangeMapService.gI().exitMap(player.pet);
                                    Service.gI().sendThongBaoFromAdmin(player, "Đổi hành tinh đệ Namec thành công!");
                                } else {
                                    Service.getInstance().sendThongBao(player, "không Đủ 100k Điểm Farm");
                                }
                                break;
                            }

                            case 2: {
                                if (player.diemfam >= 100000) {
                                    if (player.pet == null) {
                                        Service.gI().sendThongBaoFromAdmin(player, "Bạn Chưa có đệ!");
                                        break;
                                    }
                                    player.pet.gender = 1;
                                    player.diemfam -= 100000;
                                    ChangeMapService.gI().exitMap(player.pet);
                                    Service.gI().sendThongBaoFromAdmin(player, "Đổi hành tinh đệ Xayda thành công!");
                                } else {
                                    Service.getInstance().sendThongBao(player, "không Đủ 100k Điểm Farm");
                                }
                                break;
                            }

                            default:
                                Service.gI().sendThongBao(player, "Lựa chọn không hợp lệ!");
                                break;
                        }
                        break;

                    case ConstNpc.doiskill2detu:
                        switch (select) {
                            case 0: {
                                // Kiểm tra nếu cả hai điều kiện đều cần đúng (&&)
                                if (player.diemfam >= 200000 && player.vip >= 7) {

                                    if (player.pet == null) {
                                        Service.gI().sendThongBaoFromAdmin(player, "Bạn chưa có đệ!");
                                        return;
                                    }

                                    // Mở skill
                                    player.pet.playerSkill.skills.set(0, SkillUtil.createSkill(Skill.LIEN_HOAN, 7));
                                    Service.gI().sendThongBaoFromAdmin(player, "|4|Đã Mở Skill Liên Hoàn!");

                                    // Trừ điểm farm
                                    player.diemfam -= 1000000;

                                } else {
                                    Service.getInstance().sendThongBaoFromAdmin(player, "không đủ 1000k Điểm Farm! Và Đạt Vip 7 ");
                                }
                                break;
                            }

                            case 1: {
                                // Kiểm tra nếu cả hai điều kiện đều cần đúng (&&)
                                if (player.diemfam >= 200000 && player.nPoint.power >= 150000000) {

                                    if (player.pet == null) {
                                        Service.gI().sendThongBaoFromAdmin(player, "Bạn chưa có đệ!");
                                        return;
                                    }

                                    // Mở skill
                                    player.pet.playerSkill.skills.set(1, SkillUtil.createSkill(Skill.KAMEJOKO, 1));
                                    Service.gI().sendThongBaoFromAdmin(player, "|4|Đã Mở Skill 2 Kamejoko!");

                                    // Trừ điểm farm
                                    player.diemfam -= 200000;

                                } else {
                                    Service.getInstance().sendThongBaoFromAdmin(player, "không đủ 200k Điểm Farm hoặc chưa Mở Skil 2!");
                                }
                                break;
                            }

                            case 2: {
                                if (player.diemfam >= 200000 && player.nPoint.power >= 150000000) {
                                    if (player.pet == null) {
                                        Service.gI().sendThongBaoFromAdmin(player, "Bạn chưa có đệ!");
                                        return;
                                    }

                                    player.pet.playerSkill.skills.set(1, SkillUtil.createSkill(Skill.ANTOMIC, 1));
                                    Service.gI().sendThongBaoFromAdmin(player, "|4|Đã Mở Skill 2 ATOMIC!");

                                    player.diemfam -= 200000;
                                } else {
                                    Service.getInstance().sendThongBaoFromAdmin(player, "không đủ 200k Điểm Farm hoặc chưa Mở Skil 2!");
                                }
                                break;
                            }

                            case 3: {
                                if (player.diemfam >= 200000 && player.nPoint.power >= 150000000) {
                                    if (player.pet == null) {
                                        Service.gI().sendThongBaoFromAdmin(player, "Bạn chưa có đệ!");
                                        return;
                                    }
                                    player.pet.playerSkill.skills.set(1, SkillUtil.createSkill(Skill.MASENKO, 1));
                                    Service.gI().sendThongBaoFromAdmin(player, "|4|Đã Mở Skill 2 MASENKO!");

                                    player.diemfam -= 200000;
                                } else {
                                    Service.getInstance().sendThongBaoFromAdmin(player, "không đủ 200k Điểm Farm hoặc chưa Mở Skil 2!");
                                }
                                break;
                            }

                            case 4: {
                                // Kiểm tra nếu cả hai điều kiện đều cần đúng (&&)
                                if (player.diemfam >= 500000 && player.nPoint.power >= 2000000000000l) {

                                    if (player.pet == null) {
                                        Service.gI().sendThongBaoFromAdmin(player, "Bạn chưa có đệ!");
                                        return;
                                    }

                                    // Mở skill
                                    player.pet.playerSkill.skills.set(2, SkillUtil.createSkill(Skill.THAI_DUONG_HA_SAN, 7));
                                    Service.gI().sendThongBaoFromAdmin(player, "|4|Đã Mở Skill 3 THAI_DUONG_HA_SAN!");

                                    // Trừ điểm farm
                                    player.diemfam -= 500000;

                                } else {
                                    Service.getInstance().sendThongBaoFromAdmin(player, "không đủ 500k Điểm Farm hoặc chưa Mở Skil 3!");
                                }
                                break;
                            }

                            case 5: {
                                // Kiểm tra nếu cả hai điều kiện đều cần đúng (&&)
                                if (player.diemfam >= 500000 && player.nPoint.power >= 2000000000000l) {

                                    if (player.pet == null) {
                                        Service.gI().sendThongBaoFromAdmin(player, "Bạn chưa có đệ!");
                                        return;
                                    }

                                    // Mở skill
                                    player.pet.playerSkill.skills.set(2, SkillUtil.createSkill(Skill.KAIOKEN, 7));
                                    Service.gI().sendThongBaoFromAdmin(player, "|4|Đã Mở Skill 3 KAIOKEN!");

                                    // Trừ điểm farm
                                    player.diemfam -= 500000;

                                } else {
                                    Service.getInstance().sendThongBaoFromAdmin(player, "không đủ 500k Điểm Farm hoặc chưa Mở Skil 3!");
                                }
                                break;
                            }

                            case 6: {
                                // Kiểm tra nếu cả hai điều kiện đều cần đúng (&&)
                                if (player.diemfam >= 500000 && player.nPoint.power >= 2000000000000l) {

                                    if (player.pet == null) {
                                        Service.gI().sendThongBaoFromAdmin(player, "Bạn chưa có đệ!");
                                        return;
                                    }

                                    // Mở skill
                                    player.pet.playerSkill.skills.set(2, SkillUtil.createSkill(Skill.TAI_TAO_NANG_LUONG, 7));
                                    Service.gI().sendThongBaoFromAdmin(player, "|4|Đã Mở Skill 3 TAI_TAO_NANG_LUONG!");

                                    // Trừ điểm farm
                                    player.diemfam -= 500000;

                                } else {
                                    Service.getInstance().sendThongBaoFromAdmin(player, "không đủ 500k Điểm Farm hoặc chưa Mở Skil 3!");
                                }
                                break;
                            }

                            default:
                                Service.gI().sendThongBaoFromAdmin(player, "Lựa chọn không hợp lệ!");
                                break;
                        }
                        break;

                    case ConstNpc.doiskill3detu:
                        switch (select) {

                            case 0: {
                                // Kiểm tra nếu cả hai điều kiện đều cần đúng (&&)
                                if (player.diemfam >= 500000 && player.nPoint.power >= 20000000000000l) {

                                    if (player.pet == null) {
                                        Service.gI().sendThongBaoFromAdmin(player, "Bạn chưa có đệ!");
                                        return;
                                    }

                                    // Mở skill
                                    player.pet.playerSkill.skills.set(3, SkillUtil.createSkill(Skill.DE_TRUNG, 7));
                                    Service.gI().sendThongBaoFromAdmin(player, "|4|Đã Mở Skill 4 DE_TRUNG!");

                                    // Trừ điểm farm
                                    player.diemfam -= 500000;

                                } else {
                                    Service.getInstance().sendThongBaoFromAdmin(player, "không đủ 500k Điểm Farm hoặc chưa Mở Skil 4!");
                                }
                                break;
                            }

                            case 1: {
                                // Kiểm tra nếu cả hai điều kiện đều cần đúng (&&)
                                if (player.diemfam >= 500000 && player.nPoint.power >= 20000000000000l) {

                                    if (player.pet == null) {
                                        Service.gI().sendThongBaoFromAdmin(player, "Bạn chưa có đệ!");
                                        return;
                                    }

                                    // Mở skill
                                    player.pet.playerSkill.skills.set(3, SkillUtil.createSkill(Skill.QUA_CAU_KENH_KHI, 7));
                                    Service.gI().sendThongBaoFromAdmin(player, "|4|Đã Mở Skill 4 QUA_CAU_KENH_KHI!");

                                    // Trừ điểm farm
                                    player.diemfam -= 500000;

                                } else {
                                    Service.getInstance().sendThongBaoFromAdmin(player, "không đủ 500k Điểm Farm hoặc chưa Mở Skil 4!");
                                }
                                break;
                            }

                            case 2: {
                                // Kiểm tra nếu cả hai điều kiện đều cần đúng (&&)
                                if (player.diemfam >= 500000 && player.nPoint.power >= 20000000000000l) {

                                    if (player.pet == null) {
                                        Service.gI().sendThongBaoFromAdmin(player, "Bạn chưa có đệ!");
                                        return;
                                    }

                                    // Mở skill
                                    player.pet.playerSkill.skills.set(3, SkillUtil.createSkill(Skill.HUYT_SAO, 7));
                                    Service.gI().sendThongBaoFromAdmin(player, "|4|Đã Mở Skill 4 HUYT_SAO!");

                                    // Trừ điểm farm
                                    player.diemfam -= 500000;

                                } else {
                                    Service.getInstance().sendThongBaoFromAdmin(player, "không đủ 500k Điểm Farm hoặc chưa Mở Skil 4!");
                                }
                                break;
                            }

                            case 3: {
                                // Kiểm tra nếu cả hai điều kiện đều cần đúng (&&)
                                if (player.diemfam >= 1000000 && player.nPoint.power >= 2000000000000000l) {

                                    if (player.pet == null) {
                                        Service.gI().sendThongBaoFromAdmin(player, "Bạn chưa có đệ!");
                                        return;
                                    }

                                    // Mở skill
                                    player.pet.playerSkill.skills.set(4, SkillUtil.createSkill(Skill.SUPER_KAME, 9));
                                    Service.gI().sendThongBaoFromAdmin(player, "|4|Đã Mở Skill 5 SUPER_KAME!");

                                    // Trừ điểm farm
                                    player.diemfam -= 500000;

                                } else {
                                    Service.getInstance().sendThongBaoFromAdmin(player, "không đủ 500k Điểm Farm hoặc chưa Mở Skil 5!");
                                }
                                break;
                            }

                            case 4: {
                                // Kiểm tra nếu cả hai điều kiện đều cần đúng (&&)
                                if (player.diemfam >= 1000000 && player.nPoint.power >= 2000000000000000l) {

                                    if (player.pet == null) {
                                        Service.gI().sendThongBaoFromAdmin(player, "Bạn chưa có đệ!");
                                        return;
                                    }

                                    // Mở skill
                                    player.pet.playerSkill.skills.set(4, SkillUtil.createSkill(Skill.MAFUBA, 9));
                                    Service.gI().sendThongBaoFromAdmin(player, "|4|Đã Mở Skill 5 MAFUBA!");

                                    // Trừ điểm farm
                                    player.diemfam -= 500000;

                                } else {
                                    Service.getInstance().sendThongBaoFromAdmin(player, "không đủ 500k Điểm Farm hoặc chưa Mở Skil 5!");
                                }
                                break;
                            }

                            case 5: {
                                // Kiểm tra nếu cả hai điều kiện đều cần đúng (&&)
                                if (player.diemfam >= 1000000 && player.nPoint.power >= 2000000000000000l) {

                                    if (player.pet == null) {
                                        Service.gI().sendThongBaoFromAdmin(player, "Bạn chưa có đệ!");
                                        return;
                                    }

                                    // Mở skill
                                    player.pet.playerSkill.skills.set(4, SkillUtil.createSkill(Skill.SUPER_ANTOMIC, 9));
                                    Service.gI().sendThongBaoFromAdmin(player, "|4|Đã Mở Skill 5 SUPER_ANTOMIC!");

                                    // Trừ điểm farm
                                    player.diemfam -= 500000;

                                } else {
                                    Service.getInstance().sendThongBaoFromAdmin(player, "không đủ 500k Điểm Farm hoặc chưa Mở Skil 5!");
                                }
                                break;
                            }

                            case 6: {
                                // Kiểm tra nếu cả hai điều kiện đều cần đúng (&&)
                                if (player.diemfam >= 500000 && player.nPoint.power >= 20000000000000000L) {

                                    if (player.pet == null) {
                                        Service.gI().sendThongBaoFromAdmin(player, "Bạn chưa có đệ!");
                                        return;
                                    }

                                    // Mở skill
                                    player.pet.playerSkill.skills.set(5, SkillUtil.createSkill(Skill.BIEN_KHI, 7));
                                    Service.gI().sendThongBaoFromAdmin(player, "|4|Đã Mở Skill 6 BIEN_KHI!");

                                    // Trừ điểm farm
                                    player.diemfam -= 500000;

                                } else {
                                    Service.getInstance().sendThongBaoFromAdmin(player, "không đủ 500k Điểm Farm hoặc chưa Mở Skil 6!");
                                }
                                break;
                            }

                            case 7: {
                                // Kiểm tra nếu cả hai điều kiện đều cần đúng (&&)
                                if (player.diemfam >= 500000 && player.nPoint.power >= 20000000000000000L) {

                                    if (player.pet == null) {
                                        Service.gI().sendThongBaoFromAdmin(player, "Bạn chưa có đệ!");
                                        return;
                                    }

                                    // Mở skill
                                    player.pet.playerSkill.skills.set(5, SkillUtil.createSkill(Skill.MAKANKOSAPPO, 7));
                                    Service.gI().sendThongBaoFromAdmin(player, "|4|Đã Mở Skill 6 MAKANKOSAPPO!");

                                    // Trừ điểm farm
                                    player.diemfam -= 500000;

                                } else {
                                    Service.getInstance().sendThongBaoFromAdmin(player, "không đủ 500k Điểm Farm hoặc chưa Mở Skil 6!");
                                }
                                break;
                            }

                            case 8: {
                                // Kiểm tra nếu cả hai điều kiện đều cần đúng (&&)
                                if (player.diemfam >= 500000 && player.nPoint.power >= 20000000000000000L) {

                                    if (player.pet == null) {
                                        Service.gI().sendThongBaoFromAdmin(player, "Bạn chưa có đệ!");
                                        return;
                                    }

                                    // Mở skill
                                    player.pet.playerSkill.skills.set(5, SkillUtil.createSkill(Skill.SOCOLA, 7));
                                    Service.gI().sendThongBaoFromAdmin(player, "|4|Đã Mở Skill 6 SOCOLA!");

                                    // Trừ điểm farm
                                    player.diemfam -= 500000;

                                } else {
                                    Service.getInstance().sendThongBaoFromAdmin(player, "không đủ 500k Điểm Farm hoặc chưa Mở Skil 6!");
                                }
                                break;
                            }

                            default:
                                Service.gI().sendThongBaoFromAdmin(player, "Lựa chọn không hợp lệ!");
                                break;
                        }
                        break;

                    case ConstNpc.doiten:
                        switch (select) {

                            case 0: {

                                Input.gI().createFormChangeNameByItem(player);
                            }
                            break;
                        }
                        break;

                    case ConstNpc.mohanhtrang:
                        switch (select) {
                            case 0:
                                int MAX_SLOTS = 100; // Giả sử số ô tối đa là 100
                                int currentSlots = player.inventory.itemsBag.size();
                                int slotsToAdd = MAX_SLOTS - currentSlots;

                                if (slotsToAdd > 0) {

                                    int openSlots = Math.min(slotsToAdd, 99);

                                    for (int i = 0; i < openSlots; i++) {
                                        player.inventory.itemsBag.add(ItemService.gI().createItemNull());
                                    }

                                    Service.getInstance().sendThongBaoFromAdmin(player, "Hành trang của bạn đã được mở rộng thêm " + openSlots + " ô!\nVui Lòng Dùng Vật Phẩm Hoặc Thoát Ra Vô Lại");
                                } else {
                                    Service.getInstance().sendThongBaoFromAdmin(player, "Hành trang của bạn đã đạt tối đa.Đòi ít Thôi");
                                }
                                return;
                        }
                        break;
                        
                      case ConstNpc.moruongdo:
                        switch (select) {
                            case 0:
                                int MAX_SLOTS = 100; // Giả sử số ô tối đa là 100
                                int currentSlots = player.inventory.itemsBox.size();
                                int slotsToAdd = MAX_SLOTS - currentSlots;

                                if (slotsToAdd > 0) {

                                    int openSlots = Math.min(slotsToAdd, 99);

                                    for (int i = 0; i < openSlots; i++) {
                                        player.inventory.itemsBox.add(ItemService.gI().createItemNull());
                                    }

                                    Service.getInstance().sendThongBaoFromAdmin(player, "Rương Đồ của bạn đã được mở rộng thêm " + openSlots + " ô!\nVui Lòng Dùng Vật Phẩm Hoặc Thoát Ra Vô Lại");
                                } else {
                                    Service.getInstance().sendThongBaoFromAdmin(player, "Rương Đồ của bạn đã đạt tối đa.Đòi ít Thôi");
                                }
                                return;
                        }
                        break;     
                        
                        
                        

                    case ConstNpc.goichanmenh:
                        switch (select) {
                            case 0:
                                if (player.Tindung >= 888) {
                                    Item caitrangthoitrang = ItemService.gI().createNewItem((short) 1364, 1);
                                    caitrangthoitrang.itemOptions.add(new ItemOption(50, 2000));
                                    caitrangthoitrang.itemOptions.add(new ItemOption(77, 2000));
                                    caitrangthoitrang.itemOptions.add(new ItemOption(103, 2000));
                                    caitrangthoitrang.itemOptions.add(new ItemOption(101, 2000));
                                    InventoryService.gI().addItemBag(player, caitrangthoitrang, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);
                                    Service.getInstance().sendMoney(player);
                                    //    Service.getInstance().sendThongBao(player, "Bạn nhận được chân mệnh ");

                                    player.Tindung -= 888;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + caitrangthoitrang.template.name);
                                } else {
                                    Service.getInstance().sendThongBao(player, "Bạn Đã Hết Tiền");
                                }
                                break;

                            case 1:
                                if (player.Tindung >= 1888) {
                                    Item caitrangthoitrang = ItemService.gI().createNewItem((short) 1364, 1);
                                    caitrangthoitrang.itemOptions.add(new ItemOption(50, 4000));
                                    caitrangthoitrang.itemOptions.add(new ItemOption(77, 4000));
                                    caitrangthoitrang.itemOptions.add(new ItemOption(103, 4000));
                                    caitrangthoitrang.itemOptions.add(new ItemOption(101, 4000));
                                    InventoryService.gI().addItemBag(player, caitrangthoitrang, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);
                                    Service.getInstance().sendMoney(player);
                                    // Service.getInstance().sendThongBao(player, "Bạn nhận được chân mệnh ");  

                                    player.Tindung -= 1888;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + caitrangthoitrang.template.name);
                                } else {
                                    Service.getInstance().sendThongBao(player, "Bạn Đã Hết Tiền");
                                }
                                break;

                            case 2:
                                if (player.Tindung >= 2888) {
                                    Item caitrangthoitrang = ItemService.gI().createNewItem((short) 1364, 1);
                                    caitrangthoitrang.itemOptions.add(new ItemOption(50, 6000));
                                    caitrangthoitrang.itemOptions.add(new ItemOption(77, 6000));
                                    caitrangthoitrang.itemOptions.add(new ItemOption(103, 6000));
                                    caitrangthoitrang.itemOptions.add(new ItemOption(101, 6000));
                                    InventoryService.gI().addItemBag(player, caitrangthoitrang, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);
                                    Service.getInstance().sendMoney(player);
                                    //    Service.getInstance().sendThongBao(player, "Bạn nhận được chân mệnh ");  

                                    player.Tindung -= 2888;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + caitrangthoitrang.template.name);
                                } else {
                                    Service.getInstance().sendThongBao(player, "Bạn Đã Hết Tiền");
                                }
                                break;

                            case 3:

                                if (player.Tindung >= 4500) {
                                    Item caitrangthoitrang = ItemService.gI().createNewItem((short) 1364, 1);
                                    caitrangthoitrang.itemOptions.add(new ItemOption(50, 8000));
                                    caitrangthoitrang.itemOptions.add(new ItemOption(77, 8000));
                                    caitrangthoitrang.itemOptions.add(new ItemOption(103, 8000));
                                    caitrangthoitrang.itemOptions.add(new ItemOption(101, 8000));
                                    InventoryService.gI().addItemBag(player, caitrangthoitrang, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được chân mệnh ");

                                    player.Tindung -= 4500;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + caitrangthoitrang.template.name);
                                } else {
                                    Service.getInstance().sendThongBao(player, "Bạn Đã Hết Tiền");
                                }
                                break;
                        }
                        break;

                    case ConstNpc.goidanangcap:
                        switch (select) {
                            case 0:
                                if (player.Tindung >= 888) {
                                    Item caitrangthoitrang = ItemService.gI().createNewItem((short) 220, 100000);
                                    caitrangthoitrang.itemOptions.add(new ItemOption(30, 2000));
                                    InventoryService.gI().addItemBag(player, caitrangthoitrang, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);
                                    Service.getInstance().sendMoney(player);

                                    Item caitrangthoitrang1 = ItemService.gI().createNewItem((short) 221, 100000);
                                    caitrangthoitrang1.itemOptions.add(new ItemOption(30, 2000));
                                    InventoryService.gI().addItemBag(player, caitrangthoitrang1, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);
                                    Service.getInstance().sendMoney(player);

                                    Item caitrangthoitrang2 = ItemService.gI().createNewItem((short) 222, 100000);
                                    caitrangthoitrang2.itemOptions.add(new ItemOption(30, 2000));
                                    InventoryService.gI().addItemBag(player, caitrangthoitrang2, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);
                                    Service.getInstance().sendMoney(player);

                                    Item caitrangthoitrang3 = ItemService.gI().createNewItem((short) 223, 100000);
                                    caitrangthoitrang3.itemOptions.add(new ItemOption(30, 2000));
                                    InventoryService.gI().addItemBag(player, caitrangthoitrang3, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);
                                    Service.getInstance().sendMoney(player);

                                    Item caitrangthoitrang4 = ItemService.gI().createNewItem((short) 224, 100000);
                                    caitrangthoitrang4.itemOptions.add(new ItemOption(30, 2000));
                                    InventoryService.gI().addItemBag(player, caitrangthoitrang4, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);
                                    Service.getInstance().sendMoney(player);

                                    Item caitrangthoitrang5 = ItemService.gI().createNewItem((short) 987, 200);
                                    caitrangthoitrang5.itemOptions.add(new ItemOption(30, 2000));
                                    InventoryService.gI().addItemBag(player, caitrangthoitrang5, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);
                                    Service.getInstance().sendMoney(player);

                                    InventoryService.gI().sendItemBags(player);
                                    Service.getInstance().sendMoney(player);
                                    player.Tindung -= 888;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + caitrangthoitrang.template.name);
                                } else {
                                    Service.getInstance().sendThongBao(player, "Bạn Đã Hết Tiền");
                                }
                                break;

                            case 1:
                                if (player.Tindung >= 1888) {
                                    Item caitrangthoitrang = ItemService.gI().createNewItem((short) 220, 200000);
                                    caitrangthoitrang.itemOptions.add(new ItemOption(30, 2000));
                                    InventoryService.gI().addItemBag(player, caitrangthoitrang, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);
                                    Service.getInstance().sendMoney(player);
                                    Item caitrangthoitrang1 = ItemService.gI().createNewItem((short) 221, 200000);
                                    caitrangthoitrang1.itemOptions.add(new ItemOption(30, 2000));
                                    InventoryService.gI().addItemBag(player, caitrangthoitrang1, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);
                                    Service.getInstance().sendMoney(player);
                                    Item caitrangthoitrang2 = ItemService.gI().createNewItem((short) 222, 200000);
                                    caitrangthoitrang2.itemOptions.add(new ItemOption(30, 2000));
                                    InventoryService.gI().addItemBag(player, caitrangthoitrang2, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);
                                    Service.getInstance().sendMoney(player);
                                    Item caitrangthoitrang3 = ItemService.gI().createNewItem((short) 223, 200000);
                                    caitrangthoitrang3.itemOptions.add(new ItemOption(30, 2000));
                                    InventoryService.gI().addItemBag(player, caitrangthoitrang3, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);
                                    Service.getInstance().sendMoney(player);
                                    Item caitrangthoitrang4 = ItemService.gI().createNewItem((short) 224, 200000);
                                    caitrangthoitrang4.itemOptions.add(new ItemOption(30, 2000));
                                    InventoryService.gI().addItemBag(player, caitrangthoitrang4, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);
                                    Service.getInstance().sendMoney(player);
                                    Item caitrangthoitrang5 = ItemService.gI().createNewItem((short) 987, 400);
                                    caitrangthoitrang5.itemOptions.add(new ItemOption(30, 2000));
                                    InventoryService.gI().addItemBag(player, caitrangthoitrang5, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);
                                    Service.getInstance().sendMoney(player);
                                    player.Tindung -= 1888;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + caitrangthoitrang.template.name);
                                } else {
                                    Service.getInstance().sendThongBao(player, "Bạn Đã Hết Tiền");
                                }
                                break;

                            case 2:
                                if (player.Tindung >= 2888) {
                                    Item caitrangthoitrang = ItemService.gI().createNewItem((short) 220, 300000);
                                    caitrangthoitrang.itemOptions.add(new ItemOption(30, 2000));
                                    InventoryService.gI().addItemBag(player, caitrangthoitrang, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);
                                    Service.getInstance().sendMoney(player);
                                    Item caitrangthoitrang1 = ItemService.gI().createNewItem((short) 221, 300000);
                                    caitrangthoitrang1.itemOptions.add(new ItemOption(30, 2000));
                                    InventoryService.gI().addItemBag(player, caitrangthoitrang1, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);
                                    Service.getInstance().sendMoney(player);
                                    Item caitrangthoitrang2 = ItemService.gI().createNewItem((short) 222, 300000);
                                    caitrangthoitrang2.itemOptions.add(new ItemOption(30, 2000));
                                    InventoryService.gI().addItemBag(player, caitrangthoitrang2, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);
                                    Service.getInstance().sendMoney(player);
                                    Item caitrangthoitrang3 = ItemService.gI().createNewItem((short) 223, 300000);
                                    caitrangthoitrang3.itemOptions.add(new ItemOption(30, 2000));
                                    InventoryService.gI().addItemBag(player, caitrangthoitrang3, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);
                                    Service.getInstance().sendMoney(player);
                                    Item caitrangthoitrang4 = ItemService.gI().createNewItem((short) 224, 300000);
                                    caitrangthoitrang4.itemOptions.add(new ItemOption(30, 2000));
                                    InventoryService.gI().addItemBag(player, caitrangthoitrang4, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);
                                    Service.getInstance().sendMoney(player);
                                    Item caitrangthoitrang5 = ItemService.gI().createNewItem((short) 987, 600);
                                    caitrangthoitrang5.itemOptions.add(new ItemOption(30, 2000));
                                    InventoryService.gI().addItemBag(player, caitrangthoitrang5, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);
                                    Service.getInstance().sendMoney(player);
                                    player.Tindung -= 2888;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + caitrangthoitrang.template.name);
                                } else {
                                    Service.getInstance().sendThongBao(player, "Bạn Đã Hết Tiền");
                                }
                                break;

                            case 3:

                                if (player.Tindung >= 4500) {
                                    Item caitrangthoitrang = ItemService.gI().createNewItem((short) 220, 500000);
                                    caitrangthoitrang.itemOptions.add(new ItemOption(30, 2000));
                                    InventoryService.gI().addItemBag(player, caitrangthoitrang, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);
                                    Service.getInstance().sendMoney(player);
                                    Item caitrangthoitrang1 = ItemService.gI().createNewItem((short) 221, 500000);
                                    caitrangthoitrang1.itemOptions.add(new ItemOption(30, 2000));
                                    InventoryService.gI().addItemBag(player, caitrangthoitrang1, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);
                                    Service.getInstance().sendMoney(player);
                                    Item caitrangthoitrang2 = ItemService.gI().createNewItem((short) 222, 500000);
                                    caitrangthoitrang2.itemOptions.add(new ItemOption(30, 2000));
                                    InventoryService.gI().addItemBag(player, caitrangthoitrang2, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);
                                    Service.getInstance().sendMoney(player);
                                    Item caitrangthoitrang3 = ItemService.gI().createNewItem((short) 223, 500000);
                                    caitrangthoitrang3.itemOptions.add(new ItemOption(30, 2000));
                                    InventoryService.gI().addItemBag(player, caitrangthoitrang3, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);
                                    Service.getInstance().sendMoney(player);
                                    Item caitrangthoitrang4 = ItemService.gI().createNewItem((short) 224, 500000);
                                    caitrangthoitrang4.itemOptions.add(new ItemOption(30, 2000));
                                    InventoryService.gI().addItemBag(player, caitrangthoitrang4, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);
                                    Service.getInstance().sendMoney(player);
                                    Item caitrangthoitrang5 = ItemService.gI().createNewItem((short) 987, 800);
                                    caitrangthoitrang5.itemOptions.add(new ItemOption(30, 2000));
                                    InventoryService.gI().addItemBag(player, caitrangthoitrang5, 2000000000000000l);
                                    InventoryService.gI().sendItemBags(player);
                                    Service.getInstance().sendMoney(player);
                                    player.Tindung -= 4500;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + caitrangthoitrang.template.name);
                                } else {
                                    Service.getInstance().sendThongBao(player, "Bạn Đã Hết Tiền");
                                }
                                break;
                        }
                        break;

                    case ConstNpc.TamkjllHU:
                        switch (select) {
                            case 0: {
                                String hieuung = "-Hiệu ứng Đang Có:";
                                hieuung += "\n+Up Chỉ Số Gốc: " + (player.CapTamkjll + 1L) * 20L
                                        + " khi đánh quái Và Pk";
                                hieuung += "\n+Giảm ST của Boss: chia " + (player.CapTamkjll + 5);

                                if (player.CapTamkjll >= 1) {
                                    hieuung += "\n+HP, KI, SD: " + player.CapTamkjll + "% " + " 1%/Cấp";
                                }
                                if (player.TamkjllCS >= 1) {
                                    hieuung += "\nChuyển sinh +HP, KI, SD, DEF: " + player.TamkjllCS * 10L + "% " + " 10%/Cấp";
                                }
                                MenuBH(player, hieuung);
                                break;
                            }
                            case 1: {
                                String hieuung = "-Hiệu ứng Thêm Từ cấp 30 Cộng Dồn:";

                                if (player.gender == 0) {
                                    hieuung += "\n+Kaioken: " + player.CapTamkjll + "% SD";
                                    hieuung += "\n+Qckk: " + player.CapTamkjll + "% Tung Ra";
                                    hieuung += "\n+Tăng: " + player.CapTamkjll + "% DCTT Đòn kế.";
                                    hieuung += "\n+Tăng: " + player.CapTamkjll / 90L + "s Thời gian choáng";
                                    hieuung += "\n+Tăng: " + Util.getFormatNumber(player.CapTamkjll / 100L)
                                            + "S Thời gian Thôi miên.";
                                    hieuung += "\n+Tăng Tăng thời gian khiên năng lượng lên: " + player.CapTamkjll / 50L
                                            + "s";
                                }
                                if (player.gender == 1) {
                                    hieuung += "\n+Liên hoàn: " + player.CapTamkjll * 2L + "% SD";
                                    hieuung += "\n+laZe: " + player.CapTamkjll * 2L + "% SD";
                                    hieuung += "\n+Tăng: " + player.CapTamkjll + "% SD Chim";
                                    hieuung += "\n+Tăng STCM liên hoàn " + player.CapTamkjll + "%";

                                }
                                if (player.gender == 2) {
                                    hieuung += "\n+Khỉ: " + player.CapTamkjll * 3L + "% HP";
                                    hieuung += "\n+Khỉ: " + player.CapTamkjll + "% SD.";
                                    hieuung += "\n+Nổ Bom: " + player.CapTamkjll * 2L + "% ST";
                                    hieuung += "\n+Tăng: " + player.CapTamkjll / 30L + "s Khi Khỉ";
                                    hieuung += "\n+Tăng Giáp: " + player.CapTamkjll / 10L + "% Hp Khi Khỉ";
                                    hieuung += "\n+Tăng Choáng: " + player.CapTamkjll / 10L + "% Khi Trói";
                                    hieuung += "\n+Tăng HP: " + player.CapTamkjll / 50L + "% Khí Huýt Sáo"
                                            + "s";
                                }
                                MenuBH(player, hieuung);
                                break;
                            }

                            case 2: {
                                String hieuung = "-Hiệu ứng Phụ Từ Cấp 30 Cộng Dồn:";
                                hieuung += "\n+Tăng EXP up [Nhập Ma] Theo Tiên Bang";
                                hieuung += "\n+Tăng Tỉ lệ chuyển sinh ở đệ";
                                hieuung += "\n+Up nhận ngọc xanh, hồng ngọc, vàng";
                                hieuung += "\nĐame Chuẩn Lên Boss:" + player.CapTamkjll * 2d + " %";
                                hieuung += "\n+Tăng Exp tu tiên khi nhận được";
                                hieuung += "\n+Tăng thêm: " + player.CapTamkjll / 5 + "tỷ vàng tối đa";
                                MenuBH(player, hieuung);
                                break;
                            }

//                            case 2: {
//                                String hieuung = "-Hiệu ứng Trên cấp 70:";
//                                hieuung += "\nĐánh boss +HP: " + player.TamkjllCapPb * 20L + "%.";
//                                hieuung += "\n+STCM: " + player.CapTamkjll + "%";
//                                if (player.gender == 0) {
//                                    hieuung += "\n+Tăng: " + player.CapTamkjll + "% đòn đánh kế tiếp DCTT.";
//                                }
//                                if (player.gender == 1) {
//                                    hieuung += "\n+Tăng: " + player.CapTamkjll + "% SD Chim.";
//                                }
//                                if (player.gender == 2) {
//                                    hieuung += "\n+Khỉ: " + player.CapTamkjll + "% SD.";
//                                }
//                                MenuBH(player, hieuung);
//                                break;
//                            }
                            case 3: {
                                String hieuung = "-Hiệu ứng Trên cấp 100:";
                                hieuung += "\nĐánh boss +Dame: " + player.TamkjllCapPb * 10L + "%";
                                hieuung += "\n+Tăng tỉ lệ Exp Tiên Bang up ra";
                                hieuung += "\n+Tăng Tăng thời gian khiên năng lượng lên: " + player.CapTamkjll / 50L
                                        + "s";
                                if (player.gender == 0) {
                                    hieuung += "\n+Tăng: " + player.CapTamkjll / 90L + "s Thời gian choáng";
                                    hieuung += "\n+Tăng: " + Util.getFormatNumber(player.CapTamkjll / 100L)
                                            + "s Thời gian Thôi miên.";
                                }
                                if (player.gender == 1) {
                                    hieuung += "\n+Giảm Nữa thời gian hồi chiêu liên hoàn";
                                    hieuung += "\n+Tăng Sát thương chí mạng liên hoàn " + player.CapTamkjll + "%";
                                }
                                if (player.gender == 2) {
                                    hieuung += "\n+Tăng: " + player.CapTamkjll / 30L + "s Khi Khỉ";
                                    hieuung += "\n+Tăng Giáp theo: " + player.CapTamkjll / 10L + "% Hp Khi Khỉ";
                                }
                                MenuBH(player, hieuung);
                                break;
                            }
                            case 4: {
                                String hieuung = "-Hiệu ứng Trên cấp 300:";
                                hieuung += "\n+Tăng tỉ lệ up Nhập Ma theo Tiên Bang";
                                hieuung += "\n+Tăng tỉ lệ số vàng up ra khi ở núi khỉ vàng";
                                hieuung += "\n+Tăng Exp Khai Thác ở map núi khỉ vàng khi up (hơi bị nhanh nhớ) tốc độ theo Tiên Bang";
                                hieuung += "\n+Tăng Exp tu tiên khi nhận được";
                                hieuung += "\n+Tăng thêm: " + player.CapTamkjll / 5 + "tỷ vàng tối đa";
                                MenuBH(player, hieuung);
                                break;
                            }
                            case 5: {
                                String hieuung = "-Hiệu ứng Trên cấp 500:";
                                hieuung += "\n+Khi đổi đệ mới có tỉ lệ nhận đệ có skill 1 là liên hoàn";
                                hieuung += "\n+Tăng tốc độ up của đệ.";
                                hieuung += "\n+Tăng " + player.CapTamkjll / 15L + "% Hp cho đệ";
                                hieuung += "\n+Tăng " + player.CapTamkjll / 10L + "% Mp cho đệ";
                                hieuung += "\n+Tăng " + player.CapTamkjll / 25L + "% Dame cho đệ";
                                hieuung += "\n+Tăng " + player.CapTamkjll / 15L + "% Giáp cho đệ";
                                hieuung += "\n+Khi chí mạng Tăng " + player.CapTamkjll / 20L
                                        + "% Sát thương đánh ra cho đệ.";
                                hieuung += "\n+Tăng Tỉ lệ chuyển sinh ở đệ";
                                MenuBH(player, hieuung);
                                break;
                            }
                            case 6: {
                                String hieuung = "-Hiệu ứng Trên cấp 700:";
                                hieuung += "\n+Giảm 90% thời gian các chiêu đấm trừ Liên hoàn";
                                hieuung += "\n+Giảm " + player.CapTamkjll / 130 + "% Hp bịp";
                                hieuung += "\n+Tăng tốc độ trưởng thành Chiến Thần theo Tiên Bang";
                                MenuBH(player, hieuung);
                                break;
                            }
                        }
                        break;
                    case ConstNpc.TamkjllTruytim:
                        if (player.TamkjllDauLaDaiLuc[7] == 0) {
                            switch (select) {
                                case 0:
                                    if (player.ExpTamkjll < 10000000) {
                                        Service.getInstance().sendThongBaoFromAdmin(player, "Cần 10tr Exp Tiên Bang");
                                        return;
                                    }
                                    player.ExpTamkjll -= 10000000;
                                    if (Util.isTrue(0.1f, 1000)) {
                                        player.TamkjllDauLaDaiLuc[7] += Util.nextInt(1, 6);
                                        player.TamkjllDauLaDaiLuc[8] += Util.nextInt(5, 20);
                                        String hcnhan = player
                                                .TamkjllNameHoncot(Util.maxInt(player.TamkjllDauLaDaiLuc[7]))
                                                + "\n";
                                        if (player.TamkjllDauLaDaiLuc[7] == 1) {
                                            hcnhan += "Tăng: " + player.TamkjllDauLaDaiLuc[8] + " % chỉ số\n";
                                            hcnhan += "giảm: " + player.TamkjllDauLaDaiLuc[8] / 3
                                                    + " % th\u1EDDi gian Skill đấm, max 20%.\n";
                                        }
                                        if (player.TamkjllDauLaDaiLuc[7] == 2) {
                                            hcnhan += "Tăng: " + player.TamkjllDauLaDaiLuc[8] / 5
                                                    + "% Khả năng up các loại exp cao cấp của thế giới này.\n";
                                        }
                                        if (player.TamkjllDauLaDaiLuc[7] == 3) {
                                            hcnhan += "Giảm: " + player.TamkjllDauLaDaiLuc[8] / 3
                                                    + "% sát thương nhận.\n";
                                            hcnhan += "Có Tỉ Lệ x2 SD.\n";
                                        }
                                        if (player.TamkjllDauLaDaiLuc[7] == 4) {
                                            hcnhan += "Tăng: " + player.TamkjllDauLaDaiLuc[8] * 2L
                                                    + "SD.\n";
                                            hcnhan += "Giảm: " + player.TamkjllDauLaDaiLuc[8] / 2
                                                    + "% SD người ở gần.\n";
                                        }
                                        if (player.TamkjllDauLaDaiLuc[7] == 5) {
                                            hcnhan += "Tăng: " + player.TamkjllDauLaDaiLuc[8] * 3L
                                                    + "HP.\n";
                                            hcnhan += "hồi phục: " + player.TamkjllDauLaDaiLuc[8] / 3
                                                    + "% HP sau 3s.\n";
                                        }
                                        if (player.TamkjllDauLaDaiLuc[7] == 6) {
                                            hcnhan += "Đánh ST chuẩn: "
                                                    + player.TamkjllDauLaDaiLuc[8] * 4L
                                                    + "SD.\n";
                                        }
                                        NpcService.gI().createMenuConMeo(player, ConstNpc.TamkjllTruytim, -1,
                                                "Chúc Bạn Một Ngày Vui Vẻ\n"
                                                + "Thông tin Thần Khí\n"
                                                + hcnhan
                                                + "\nHãy chọn theo lí trí của mình.",
                                                "đóng", "Hủy \nThần Khí", "Hấp thụ \nThần Khí");
                                    } else {
                                        NpcService.gI().createMenuConMeo(player, ConstNpc.TamkjllTruytim, -1,
                                                "|8|Chúc Bạn Một Ngày Vui Vẻ\n"
                                                + "Truy tìm thất bại.",
                                                "Truy tìm");
                                    }
                                    break;
                                case 1:
                                    if (player.ExpTamkjll < 500000000) {
                                        Service.getInstance().sendThongBaoFromAdmin(player, "Cần 500tr Exp Tiên Bang");
                                        return;
                                    }
                                    player.ExpTamkjll -= 500000000;
                                    player.TamkjllDauLaDaiLuc[7] += Util.nextInt(1, 6);
                                    player.TamkjllDauLaDaiLuc[8] += Util.nextInt(5, 20);
                                    String hcnhan = player
                                            .TamkjllNameHoncot(Util.maxInt(player.TamkjllDauLaDaiLuc[7]))
                                            + "\n";
                                    if (player.TamkjllDauLaDaiLuc[7] == 1) {
                                        hcnhan += "Tăng: " + player.TamkjllDauLaDaiLuc[8] + " % chỉ số\n";
                                        hcnhan += "giảm: " + player.TamkjllDauLaDaiLuc[8] / 3
                                                + " % th\u1EDDi gian Skill đấm, max 20%.\n";
                                    }
                                    if (player.TamkjllDauLaDaiLuc[7] == 2) {
                                        hcnhan += "Tăng: " + player.TamkjllDauLaDaiLuc[8] / 5
                                                + "% Khả năng up các loại exp cao cấp của thế giới này.\n";
                                    }
                                    if (player.TamkjllDauLaDaiLuc[7] == 3) {
                                        hcnhan += "Giảm: " + player.TamkjllDauLaDaiLuc[8] / 3
                                                + "% ST nhận.\n";
                                        hcnhan += "Có tỉ lệ x2 SD.\n";
                                    }
                                    if (player.TamkjllDauLaDaiLuc[7] == 4) {
                                        hcnhan += "Tăng: " + player.TamkjllDauLaDaiLuc[8] * 236L
                                                + "ST.\n";
                                        hcnhan += "Giảm: " + player.TamkjllDauLaDaiLuc[8] / 2
                                                + "% ST người ở gần.\n";
                                    }
                                    if (player.TamkjllDauLaDaiLuc[7] == 5) {
                                        hcnhan += "Tăng: " + player.TamkjllDauLaDaiLuc[8] * 356L
                                                + "HP.\n";
                                        hcnhan += "hồi phục: " + player.TamkjllDauLaDaiLuc[8] / 3
                                                + "% HP sau 3s.\n";
                                    }
                                    if (player.TamkjllDauLaDaiLuc[7] == 6) {
                                        hcnhan += "Đánh ST chuẩn: "
                                                + player.TamkjllDauLaDaiLuc[8] * 4L
                                                + "ST.\n";
                                    }
                                    NpcService.gI().createMenuConMeo(player, ConstNpc.TamkjllTruytim, -1,
                                            "|8|Chúc Bạn Một Ngày Vui Vẻ\n"
                                            + "Thông tin Thần Khí\n"
                                            + hcnhan
                                            + "\nHãy chọn theo lí trí của mình.",
                                            "đóng", "Hủy\n Thần Khí", "Hấp thụ\n Thần Khí");
                                    break;
                            }
                        } else {
                            switch (select) {
                                case 0:
                                    break;
                                case 1:
                                    player.TamkjllDauLaDaiLuc[7] = 0;
                                    player.TamkjllDauLaDaiLuc[8] = 0;
                                    Service.getInstance().sendThongBaoFromAdmin(player, "Đã hủy Thần Khí");
                                    break;
                                case 2:
                                    NpcService.gI().createMenuConMeo(player, ConstNpc.TamkjllDYHapthu, -1,
                                            "|8|Chúc Bạn Một Ngày Vui Vẻ\n"
                                            + "Chọn phương pháp hấp thụ.",
                                            "Dựa vào bản thân\n(50% thành công)", "Nhờ trợ giúp\n(100% thành công)");
                                    break;
                            }
                        }
                        break;
                    case ConstNpc.TamkjllNCHC:
                        Item mhc = InventoryService.gI().findItem(player.inventory.itemsBag,
                                1156);
                        switch (select) {
                            case 0:
                                if (player.CapTamkjll < 500) {
                                    Service.getInstance().sendThongBaoFromAdmin(player, "Cần ít nhất 500 Tiên Bang");
                                    return;
                                }
                                if (player.ExpTamkjll < 100000000) {
                                    Service.getInstance().sendThongBaoFromAdmin(player, "Cần ít nhất 100M Exp Tiên Bang");
                                    return;
                                }
                                if (player.TamkjllDauLaDaiLuc[9] != 1) {
                                    Service.getInstance().sendThongBaoFromAdmin(player, "Bạn không sỡ hữu Thần Khí này");
                                    return;
                                }
                                if (mhc != null && mhc.quantity > 50000) {
                                    player.TamkjllDauLaDaiLuc[10] += Util.nextInt(5, 20);
                                    NpcService.gI().createMenuConMeo(player, ConstNpc.TamkjllNCHC, -1,
                                            "|8|Chúc Bạn Một Ngày Vui Vẻ\n"
                                            + "Bạn nâng cấp thành công Thần Khí:\n" + player.TamkjllNameHoncot(1)
                                            + "\nChỉ Số sau khi nâng cấp :\n"
                                            + "Tăng: " + player.TamkjllDauLaDaiLuc[10] + " % chỉ số\n"
                                            + "giảm: "
                                            + (player.TamkjllDauLaDaiLuc[10] / 3 >= 20 ? 20
                                                    : player.TamkjllDauLaDaiLuc[10] / 3)
                                            + "% thời gian Skill đấm",
                                            player.TamkjllNameHoncot(1),
                                            player.TamkjllNameHoncot(2),
                                            player.TamkjllNameHoncot(3),
                                            player.TamkjllNameHoncot(4),
                                            player.TamkjllNameHoncot(5),
                                            player.TamkjllNameHoncot(6));
                                    player.ExpTamkjll -= 250000000;
                                    InventoryService.gI().subQuantityItemsBag(player, mhc, 50000);
                                    InventoryService.gI().sendItemBags(player);
                                } else {
                                    Service.getInstance().sendThongBaoFromAdmin(player, "Cần 50k mảnh Thần Khí");
                                }
                                break;
                            case 1:
                                if (player.CapTamkjll < 1000) {
                                    Service.getInstance().sendThongBaoFromAdmin(player, "Cần ít nhất 1000 Tiên Bang");
                                    return;
                                }
                                if (player.ExpTamkjll < 250000000) {
                                    Service.getInstance().sendThongBaoFromAdmin(player, "Cần ít nhất 250tr Exp Tiên Bang");
                                    return;
                                }
                                if (player.TamkjllDauLaDaiLuc[11] != 1) {
                                    Service.getInstance().sendThongBaoFromAdmin(player, "Bạn không sỡ hữu Thần Khí này");
                                    return;
                                }
                                if (player.TamkjllDauLaDaiLuc[12] / 5 >= 20) {
                                    Service.getInstance().sendThongBaoFromAdmin(player, "Đã tối đa không thể tăng thêm nữa");
                                    return;
                                }
                                if (mhc != null && mhc.quantity > 100000) {
                                    player.TamkjllDauLaDaiLuc[12] += Util.nextInt(5, 20);
                                    NpcService.gI().createMenuConMeo(player, ConstNpc.TamkjllNCHC, -1,
                                            "|8|Chúc Bạn Một Ngày Vui Vẻ\n"
                                            + "Bạn nâng cấp thành công Thần Khí:\n" + player.TamkjllNameHoncot(2)
                                            + "\nChỉ Số sau khi nâng cấp :\n"
                                            + "Tăng: "
                                            + (player.TamkjllDauLaDaiLuc[12] / 5 >= 20 ? 20
                                                    : player.TamkjllDauLaDaiLuc[12] / 5)
                                            + "% Khả năng up các loại exp cao cấp của thế giới này.",
                                            player.TamkjllNameHoncot(1),
                                            player.TamkjllNameHoncot(2),
                                            player.TamkjllNameHoncot(3),
                                            player.TamkjllNameHoncot(4),
                                            player.TamkjllNameHoncot(5),
                                            player.TamkjllNameHoncot(6));
                                    player.ExpTamkjll -= 250000000;
                                    InventoryService.gI().subQuantityItemsBag(player, mhc, 100000);
                                    InventoryService.gI().sendItemBags(player);
                                } else {
                                    Service.getInstance().sendThongBaoFromAdmin(player, "Cần 100k mảnh Thần Khí");
                                }
                                break;
                            case 2:
                                if (player.CapTamkjll < 1000) {
                                    Service.getInstance().sendThongBaoFromAdmin(player, "Cần ít nhất 1000 Tiên Bang");
                                    return;
                                }
                                if (player.ExpTamkjll < 250000000) {
                                    Service.getInstance().sendThongBaoFromAdmin(player, "Cần ít nhất 250tr Exp Tiên Bang");
                                    return;
                                }
                                if (player.TamkjllDauLaDaiLuc[13] != 1) {
                                    Service.getInstance().sendThongBaoFromAdmin(player, "Bạn không sỡ hữu Thần Khí này");
                                    return;
                                }
                                if (player.TamkjllDauLaDaiLuc[14] / 3 >= 80) {
                                    Service.getInstance().sendThongBaoFromAdmin(player, "Đã tối đa không thể tăng thêm nữa");
                                    return;
                                }
                                if (mhc != null && mhc.quantity > 100000) {
                                    player.TamkjllDauLaDaiLuc[14] += Util.nextInt(5, 20);
                                    NpcService.gI().createMenuConMeo(player, ConstNpc.TamkjllNCHC, -1,
                                            "|8|Chúc Bạn Một Ngày Vui Vẻ\n"
                                            + "Bạn nâng cấp thành công Thần Khí:\n" + player.TamkjllNameHoncot(3)
                                            + "\nChỉ Số sau khi nâng cấp :\n"
                                            + "Giảm: " + (player.TamkjllDauLaDaiLuc[14] / 3 >= 80 ? 80
                                                    : player.TamkjllDauLaDaiLuc[14] / 3)
                                            + "% ST nhận.\n",
                                            player.TamkjllNameHoncot(1),
                                            player.TamkjllNameHoncot(2),
                                            player.TamkjllNameHoncot(3),
                                            player.TamkjllNameHoncot(4),
                                            player.TamkjllNameHoncot(5),
                                            player.TamkjllNameHoncot(6));
                                    player.ExpTamkjll -= 250000000;
                                    InventoryService.gI().subQuantityItemsBag(player, mhc, 100000);
                                    InventoryService.gI().sendItemBags(player);
                                } else {
                                    Service.getInstance().sendThongBaoFromAdmin(player, "Cần 100k mảnh Thần Khí");
                                }
                                break;
                            case 3:
                                if (player.CapTamkjll < 2000) {
                                    Service.getInstance().sendThongBaoFromAdmin(player, "Cần ít nhất 2000 Tiên Bang");
                                    return;
                                }
                                if (player.ExpTamkjll < 500000000) {
                                    Service.getInstance().sendThongBaoFromAdmin(player, "Cần ít nhất 500tr Exp Tiên Bang");
                                    return;
                                }
                                if (player.TamkjllDauLaDaiLuc[15] != 1) {
                                    Service.getInstance().sendThongBaoFromAdmin(player, "Bạn không sỡ hữu Thần Khí này");
                                    return;
                                }
                                if (mhc != null && mhc.quantity > 100000) {
                                    player.TamkjllDauLaDaiLuc[16] += Util.nextInt(5, 20);
                                    NpcService.gI().createMenuConMeo(player, ConstNpc.TamkjllNCHC, -1,
                                            "|8|Chúc Bạn Một Ngày Vui Vẻ\n"
                                            + "Bạn nâng cấp thành công Thần Khí:\n" + player.TamkjllNameHoncot(4)
                                            + "\nChỉ Số sau khi nâng cấp :\n"
                                            + "Tăng: "
                                            + Util.getFormatNumber(player.TamkjllDauLaDaiLuc[16] * 2369d)
                                            + "SD.\n"
                                            + "Giảm: " + (player.TamkjllDauLaDaiLuc[16] / 2 >= 90 ? 90
                                                    : player.TamkjllDauLaDaiLuc[16] / 2)
                                            + "% SD người ở gần.",
                                            player.TamkjllNameHoncot(1), player.TamkjllNameHoncot(2),
                                            player.TamkjllNameHoncot(3),
                                            player.TamkjllNameHoncot(4), player.TamkjllNameHoncot(5),
                                            player.TamkjllNameHoncot(6));
                                    player.ExpTamkjll -= 500000000;
                                    InventoryService.gI().subQuantityItemsBag(player, mhc, 200000);
                                    InventoryService.gI().sendItemBags(player);
                                } else {
                                    Service.getInstance().sendThongBaoFromAdmin(player, "Cần 200k mảnh Thần Khí");
                                }
                                break;
                            case 4:
                                if (player.CapTamkjll < 1500) {
                                    Service.getInstance().sendThongBaoFromAdmin(player, "Cần ít nhất 1500 Tiên Bang");
                                    return;
                                }
                                if (player.ExpTamkjll < 400000000) {
                                    Service.getInstance().sendThongBaoFromAdmin(player, "Cần ít nhất 400tr Exp Tiên Bang");
                                    return;
                                }
                                if (player.TamkjllDauLaDaiLuc[17] != 1) {
                                    Service.getInstance().sendThongBaoFromAdmin(player, "Bạn không sỡ hữu Thần Khí này");
                                    return;
                                }
                                if (mhc != null && mhc.quantity > 150000) {
                                    player.TamkjllDauLaDaiLuc[18] += Util.nextInt(5, 20);
                                    NpcService.gI().createMenuConMeo(player, ConstNpc.TamkjllNCHC, -1,
                                            "|8|Chúc Bạn Một Ngày Vui Vẻ\n"
                                            + "Bạn nâng cấp thành công Thần Khí:\n" + player.TamkjllNameHoncot(5)
                                            + "\nChỉ Số sau khi nâng cấp :\n"
                                            + "Tăng: "
                                            + Util.getFormatNumber(player.TamkjllDauLaDaiLuc[18] * 3569d)
                                            + "HP.\n"
                                            + "+hồi phục: " + (player.TamkjllDauLaDaiLuc[18] / 3 >= 90 ? 90
                                                    : player.TamkjllDauLaDaiLuc[18] / 3)
                                            + "% HP sau 3s.",
                                            player.TamkjllNameHoncot(1),
                                            player.TamkjllNameHoncot(2),
                                            player.TamkjllNameHoncot(3),
                                            player.TamkjllNameHoncot(4),
                                            player.TamkjllNameHoncot(5),
                                            player.TamkjllNameHoncot(6));
                                    player.ExpTamkjll -= 400000000;
                                    InventoryService.gI().subQuantityItemsBag(player, mhc, 150000);
                                    InventoryService.gI().sendItemBags(player);
                                } else {
                                    Service.getInstance().sendThongBaoFromAdmin(player, "Cần 150k mảnh Thần Khí");
                                }
                                break;
                            case 5:
                                if (player.CapTamkjll < 1000) {
                                    Service.getInstance().sendThongBaoFromAdmin(player, "Cần ít nhất 1000 Tiên Bang");
                                    return;
                                }
                                if (player.ExpTamkjll < 250000000) {
                                    Service.getInstance().sendThongBaoFromAdmin(player, "Cần ít nhất 250tr Exp Tiên Bang");
                                    return;
                                }
                                if (player.TamkjllDauLaDaiLuc[19] != 1) {
                                    Service.getInstance().sendThongBaoFromAdmin(player, "Bạn không sỡ hữu Thần Khí này");
                                    return;
                                }
                                if (mhc != null && mhc.quantity > 100000) {
                                    player.TamkjllDauLaDaiLuc[20] += Util.nextInt(5, 20);
                                    NpcService.gI().createMenuConMeo(player, ConstNpc.TamkjllNCHC, -1,
                                            "|8|Chúc Bạn Một Ngày Vui Vẻ\n"
                                            + "Bạn nâng cấp thành công Thần Khí:\n" + player.TamkjllNameHoncot(6)
                                            + "\nChỉ Số sau khi nâng cấp :\n"
                                            + "Đánh ST chuẩn: "
                                            + Util.getFormatNumber(player.TamkjllDauLaDaiLuc[20] * 4)
                                            + "SD.",
                                            player.TamkjllNameHoncot(1), player.TamkjllNameHoncot(2),
                                            player.TamkjllNameHoncot(3),
                                            player.TamkjllNameHoncot(4), player.TamkjllNameHoncot(5),
                                            player.TamkjllNameHoncot(6));
                                    player.ExpTamkjll -= 250000000;
                                    InventoryService.gI().subQuantityItemsBag(player, mhc, 100000);
                                    InventoryService.gI().sendItemBags(player);
                                } else {
                                    Service.getInstance().sendThongBaoFromAdmin(player, "Cần 100k mảnh Thần Khí");
                                }
                                break;
                        }
                        break;
                    case ConstNpc.TamkjllDYHapthu:
                        switch (select) {
                            case 0:
                                if (player.TamkjllDauLaDaiLuc[7] == 1 && player.TamkjllDauLaDaiLuc[9] != 1) {
                                    if (Util.isTrue(10f, 100)) {
                                        Service.getInstance().sendThongBaoAllPlayer(
                                                "\n|8|Chúc Mừng Đạo Hữu :\n [" + player.name + "]\n Đã Được Hấp thụ\n " + player.TamkjllNameHoncot(
                                                        Util.maxInt(player.TamkjllDauLaDaiLuc[7])));
                                        player.TamkjllDauLaDaiLuc[9] = 1;
                                        player.TamkjllDauLaDaiLuc[10] = player.TamkjllDauLaDaiLuc[8];
                                    } else {
                                        Service.getInstance().sendThongBaoFromAdmin(player, "Hấp thụ thất bại Thần Khí đã tan biến.");
                                    }
                                    player.TamkjllDauLaDaiLuc[7] = 0;
                                    player.TamkjllDauLaDaiLuc[8] = 0;
                                } else if (player.TamkjllDauLaDaiLuc[7] == 2 && player.TamkjllDauLaDaiLuc[11] != 1) {
                                    if (Util.isTrue(10f, 100)) {
                                        Service.getInstance().sendThongBaoAllPlayer(
                                                "\n|8|Chúc Mừng Đạo Hữu :\n [" + player.name + "]\n Đã Được Hấp thụ\n " + player.TamkjllNameHoncot(
                                                        Util.maxInt(player.TamkjllDauLaDaiLuc[7])));
                                        player.TamkjllDauLaDaiLuc[11] = 1;
                                        player.TamkjllDauLaDaiLuc[12] = player.TamkjllDauLaDaiLuc[8];
                                    } else {
                                        Service.getInstance().sendThongBaoFromAdmin(player, "Hấp thụ thất bại Thần Khí đã tan biến.");
                                    }
                                    player.TamkjllDauLaDaiLuc[7] = 0;
                                    player.TamkjllDauLaDaiLuc[8] = 0;
                                } else if (player.TamkjllDauLaDaiLuc[7] == 3 && player.TamkjllDauLaDaiLuc[13] != 1) {
                                    if (Util.isTrue(10f, 100)) {
                                        Service.getInstance().sendThongBaoAllPlayer(
                                                "\n|8|Chúc Mừng Đạo Hữu :\n [" + player.name + "]\n Đã Được Hấp thụ\n " + player.TamkjllNameHoncot(
                                                        Util.maxInt(player.TamkjllDauLaDaiLuc[7])));
                                        player.TamkjllDauLaDaiLuc[13] = 1;
                                        player.TamkjllDauLaDaiLuc[14] = player.TamkjllDauLaDaiLuc[8];
                                    } else {
                                        Service.getInstance().sendThongBaoFromAdmin(player, "Hấp thụ thất bại Thần Khí đã tan biến.");
                                    }
                                    player.TamkjllDauLaDaiLuc[7] = 0;
                                    player.TamkjllDauLaDaiLuc[8] = 0;
                                } else if (player.TamkjllDauLaDaiLuc[7] == 4 && player.TamkjllDauLaDaiLuc[15] != 1) {
                                    if (Util.isTrue(10f, 100)) {
                                        Service.getInstance().sendThongBaoAllPlayer(
                                                "\n|8|Chúc Mừng Đạo Hữu :\n [" + player.name + "]\n Đã Được Hấp thụ\n " + player.TamkjllNameHoncot(
                                                        Util.maxInt(player.TamkjllDauLaDaiLuc[7])));
                                        player.TamkjllDauLaDaiLuc[15] = 1;
                                        player.TamkjllDauLaDaiLuc[16] = player.TamkjllDauLaDaiLuc[8];
                                    } else {
                                        Service.getInstance().sendThongBaoFromAdmin(player, "Hấp thụ thất bại Thần Khí đã tan biến.");
                                    }
                                    player.TamkjllDauLaDaiLuc[7] = 0;
                                    player.TamkjllDauLaDaiLuc[8] = 0;
                                } else if (player.TamkjllDauLaDaiLuc[7] == 5 && player.TamkjllDauLaDaiLuc[17] != 1) {
                                    if (Util.isTrue(10f, 100)) {
                                        Service.getInstance().sendThongBaoAllPlayer(
                                                "\n|8|Chúc Mừng Đạo Hữu :\n [" + player.name + "]\n Đã Được Hấp thụ\n " + player.TamkjllNameHoncot(
                                                        Util.maxInt(player.TamkjllDauLaDaiLuc[7])));
                                        player.TamkjllDauLaDaiLuc[17] = 1;
                                        player.TamkjllDauLaDaiLuc[18] = player.TamkjllDauLaDaiLuc[8];
                                    } else {
                                        Service.getInstance().sendThongBaoFromAdmin(player, "Hấp thụ thất bại Thần Khí đã tan biến.");
                                    }
                                    player.TamkjllDauLaDaiLuc[7] = 0;
                                    player.TamkjllDauLaDaiLuc[8] = 0;
                                } else if (player.TamkjllDauLaDaiLuc[7] == 6 && player.TamkjllDauLaDaiLuc[19] != 1) {
                                    if (Util.isTrue(10f, 100)) {
                                        Service.getInstance().sendThongBaoAllPlayer(
                                                "\n|8|Chúc Mừng Đạo Hữu :\n [" + player.name + "]\n Đã Được Hấp thụ\n " + player.TamkjllNameHoncot(
                                                        Util.maxInt(player.TamkjllDauLaDaiLuc[7])));
                                        player.TamkjllDauLaDaiLuc[19] = 1;
                                        player.TamkjllDauLaDaiLuc[20] = player.TamkjllDauLaDaiLuc[8];
                                    } else {
                                        Service.getInstance().sendThongBaoFromAdmin(player, "Hấp thụ thất bại Thần Khí đã tan biến.");
                                    }
                                    player.TamkjllDauLaDaiLuc[7] = 0;
                                    player.TamkjllDauLaDaiLuc[8] = 0;
                                } else {
                                    NpcService.gI().createMenuConMeo(player, ConstNpc.TamkjllTruytim, -1,
                                            "|8|zalo: @admin\n"
                                            + "Ngươi đã sở hữu Thần Khí này rồi."
                                            + "\nChỉ còn hủy Thần Khí hoặc để đó trưng.",
                                            "đóng", "Hủy \nThần Khí", "Hấp thụ\n Thần Khí");
                                }
                                break;
                            case 1:
                                if (player.ExpTamkjll < 150000000) {
                                    Service.getInstance().sendThongBaoFromAdmin(player, "Cần 150tr Exp Tiên Bang.");
                                    return;
                                }
                                if (player.CapTamkjll < 50) {
                                    Service.getInstance().sendThongBaoFromAdmin(player, "Cần 50 Tiên Bang.");
                                    return;
                                }
                                if (player.TamkjllDauLaDaiLuc[7] == 1 && player.TamkjllDauLaDaiLuc[9] != 1) {
                                    Service.getInstance().sendThongBaoAllPlayer(
                                            "\n|8|Chúc Mừng Đạo Hữu :\n [" + player.name + "]\n Đã Được Hấp thụ\n " + player.TamkjllNameHoncot(Util.maxInt(player.TamkjllDauLaDaiLuc[7])));
                                    player.TamkjllDauLaDaiLuc[9] = 1;
                                    player.TamkjllDauLaDaiLuc[10] = player.TamkjllDauLaDaiLuc[8];
                                    player.TamkjllDauLaDaiLuc[7] = 0;
                                    player.TamkjllDauLaDaiLuc[8] = 0;
                                    player.ExpTamkjll -= 150000000;
                                } else if (player.TamkjllDauLaDaiLuc[7] == 2 && player.TamkjllDauLaDaiLuc[11] != 1) {
                                    Service.getInstance().sendThongBaoAllPlayer(
                                            "\n|8|Chúc Mừng Đạo Hữu :\n [" + player.name + "]\n Đã Được Hấp thụ\n " + player.TamkjllNameHoncot(Util.maxInt(player.TamkjllDauLaDaiLuc[7])));
                                    player.TamkjllDauLaDaiLuc[11] = 1;
                                    player.TamkjllDauLaDaiLuc[12] = player.TamkjllDauLaDaiLuc[8];
                                    player.TamkjllDauLaDaiLuc[7] = 0;
                                    player.TamkjllDauLaDaiLuc[8] = 0;
                                    player.ExpTamkjll -= 150000000;
                                } else if (player.TamkjllDauLaDaiLuc[7] == 3 && player.TamkjllDauLaDaiLuc[13] != 1) {
                                    Service.getInstance().sendThongBaoAllPlayer(
                                            "\n|8|Chúc Mừng Đạo Hữu :\n [" + player.name + "]\n Đã Được Hấp thụ\n " + player.TamkjllNameHoncot(Util.maxInt(player.TamkjllDauLaDaiLuc[7])));
                                    player.TamkjllDauLaDaiLuc[13] = 1;
                                    player.TamkjllDauLaDaiLuc[14] = player.TamkjllDauLaDaiLuc[8];
                                    player.TamkjllDauLaDaiLuc[7] = 0;
                                    player.TamkjllDauLaDaiLuc[8] = 0;
                                    player.ExpTamkjll -= 150000000;
                                } else if (player.TamkjllDauLaDaiLuc[7] == 4 && player.TamkjllDauLaDaiLuc[15] != 1) {
                                    Service.getInstance().sendThongBaoAllPlayer(
                                            "\n|8|Chúc Mừng Đạo Hữu :\n [" + player.name + "]\n Đã Được Hấp thụ\n " + player.TamkjllNameHoncot(Util.maxInt(player.TamkjllDauLaDaiLuc[7])));
                                    player.TamkjllDauLaDaiLuc[15] = 1;
                                    player.TamkjllDauLaDaiLuc[16] = player.TamkjllDauLaDaiLuc[8];
                                    player.TamkjllDauLaDaiLuc[7] = 0;
                                    player.TamkjllDauLaDaiLuc[8] = 0;
                                    player.ExpTamkjll -= 150000000;
                                } else if (player.TamkjllDauLaDaiLuc[7] == 5 && player.TamkjllDauLaDaiLuc[17] != 1) {
                                    Service.getInstance().sendThongBaoAllPlayer(
                                            "\n|8|Chúc Mừng Đạo Hữu :\n [" + player.name + "]\n Đã Được Hấp thụ\n " + player.TamkjllNameHoncot(Util.maxInt(player.TamkjllDauLaDaiLuc[7])));
                                    player.TamkjllDauLaDaiLuc[17] = 1;
                                    player.TamkjllDauLaDaiLuc[18] = player.TamkjllDauLaDaiLuc[8];
                                    player.TamkjllDauLaDaiLuc[7] = 0;
                                    player.TamkjllDauLaDaiLuc[8] = 0;
                                    player.ExpTamkjll -= 150000000;
                                } else if (player.TamkjllDauLaDaiLuc[7] == 6 && player.TamkjllDauLaDaiLuc[19] != 1) {
                                    Service.getInstance().sendThongBaoAllPlayer(
                                            "\n|8|Chúc Mừng Đạo Hữu :\n [" + player.name + "]\n Đã Được Hấp thụ\n " + player.TamkjllNameHoncot(Util.maxInt(player.TamkjllDauLaDaiLuc[7])));
                                    player.TamkjllDauLaDaiLuc[19] = 1;
                                    player.TamkjllDauLaDaiLuc[20] = player.TamkjllDauLaDaiLuc[8];
                                    player.TamkjllDauLaDaiLuc[7] = 0;
                                    player.TamkjllDauLaDaiLuc[8] = 0;
                                    player.ExpTamkjll -= 150000000;
                                } else {
                                    NpcService.gI().createMenuConMeo(player, ConstNpc.TamkjllTruytim, -1,
                                            "|8|zalo: @admin\n"
                                            + "Ngươi đã sở hữu hồn cốt này rồi."
                                            + "\nChỉ còn hủy hồn cốt hoặc để đó trưng.",
                                            "đóng", "Hủy hồn cốt", "Hấp thụ hồn cốt");
                                }
                                break;
                        }
                        break;
                    case ConstNpc.MENU_BOT:
                        switch (select) {
                            case 0:
                                Input.gI().createFormBotQuai(player);
                                break;
                            case 1:
                                Input.gI().createFormBotItem(player);
                                break;
                            case 2:
                                Input.gI().createFormBotBoss(player);
                                break;
                            case 3:
                                BotManager.gI().stopRunning();
                                break;
                        }
                        break;
                    case ConstNpc.menutd:
                        switch (select) {
                            case 0: {// set songoku
                                try {
                                    ItemService.gI().setSongoku(player);

                                } catch (Exception ex) {
                                    Logger.getLogger(NpcFactory.class
                                            .getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                            break;
                            case 1:// set kaioken
                                try {
                                ItemService.gI().setKaioKen(player);
                            } catch (Exception e) {
                            }
                            break;
                            case 2:// set thenxin hang
                                   try {
                                ItemService.gI().setThenXinHang(player);
                            } catch (Exception e) {
                            }
                            break;
                        }
                        break;
                    case ConstNpc.menunm:
                        switch (select) {
                            case 0:
                                try {
                                ItemService.gI().setLienHoan(player);
                            } catch (Exception e) {
                            }
                            break;
                            case 1:
                                try {
                                ItemService.gI().setPicolo(player);
                            } catch (Exception e) {
                            }
                            break;
                            case 2:
                                try {
                                ItemService.gI().setPikkoroDaimao(player);
                            } catch (Exception e) {
                            }
                            break;
                        }
                        break;

                    case ConstNpc.menuxd:
                        switch (select) {
                            case 0:
                                try {
                                ItemService.gI().setKakarot(player);
                            } catch (Exception e) {
                            }
                            break;
                            case 1:
                                try {
                                ItemService.gI().setCadic(player);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                            case 2:
                                try {
                                ItemService.gI().setNappa(player);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        }
                        break;
                    case ConstNpc.CONFIRM_ACTIVE:
                        switch (select) {
                            case 1:
                                if (player.getSession().vnd >= 10_000) {
                                    if (PlayerDAO.subVND2(player, 10_000)) {
                                        Service.getInstance().sendThongBao(player, "Đã mở thành viên thành công!");
                                    } else {
                                        this.npcChat(player, "Lỗi vui lòng báo admin...");
                                    }
                                } else {
                                    Service.getInstance().sendThongBao(player, "Số dư vnd không đủ vui lòng nạp thêm :\nLiên hệ AD ");
                                }
                                break;
                        }
                        break;
                    case ConstNpc.UP_TOP_ITEM:
                        break;
                    case ConstNpc.RUONG_GO:
                        int size = player.textRuongGo.size();
                        if (size
                                > 0) {
                            String menuselect = "OK [" + (size - 1) + "]";
                            if (size == 1) {
                                menuselect = "OK";
                            }
                            NpcService.gI().createMenuConMeo(player, ConstNpc.RUONG_GO, -1,
                                    player.textRuongGo.get(size - 1), menuselect);
                            player.textRuongGo.remove(size - 1);
                        }
                        break;
                    case ConstNpc.MENU_MABU_WAR:
                        if (select
                                == 0) {
                            if (player.zone.finishMabuWar) {
                                ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, -1, 250);
                            } else if (player.zone.map.mapId == 119) {
                                Zone zone = MabuWar.gI().getMapLastFloor(120);
                                if (zone != null) {
                                    ChangeMapService.gI().changeMap(player, zone, 354, 240);
                                } else {
                                    Service.getInstance().sendThongBao(player,
                                            "Trận đại chiến đã kết thúc, tàu vận chuyển sẽ đưa bạn về nhà");
                                    ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, -1, 250);
                                }
                            } else {
                                int idMapNextFloor = player.zone.map.mapId == 115 ? player.zone.map.mapId + 2
                                        : player.zone.map.mapId + 1;
                                ChangeMapService.gI().changeMap(player, idMapNextFloor, -1, 354, 240);
                            }
                            player.resetPowerPoint();
                            player.sendMenuGotoNextFloorMabuWar = false;
                            Service.getInstance().sendPowerInfo(player, "TL", player.getPowerPoint());
                            if (Util.isTrue(1, 30)) {
                                player.inventory.ruby += 1;
                                PlayerService.gI().sendInfoHpMpMoney(player);
                                Service.getInstance().sendThongBao(player, "Bạn nhận được 1 Hồng Ngọc");
                            } else {
                                Service.getInstance().sendThongBao(player,
                                        "Bạn đen vô cùng luôn nên không nhận được gì cả");
                            }
                        }
                        break;
                    case ConstNpc.IGNORE_MENU:
                        break;
                    case ConstNpc.MAKE_MATCH_PVP:
                        PVPServcice.gI()
                                .sendInvitePVP(player, (byte) select
                                );
                        break;
                    case ConstNpc.MAKE_FRIEND:
                        if (select
                                == 0) {
                            Object playerId = PLAYERID_OBJECT.get(player.id);
                            if (playerId != null) {
                                try {
                                    int playerIdInt = Integer.parseInt(String.valueOf(playerId));
                                    FriendAndEnemyService.gI().acceptMakeFriend(player, playerIdInt);
                                } catch (NumberFormatException e) {
//                                    e.printStackTrace();
                                }
                            }
                        }
                        break;
                    case ConstNpc.REVENGE:
                        if (select
                                == 0) {
                            PVPServcice.gI().acceptRevenge(player);
                        }
                        break;
                    case ConstNpc.TUTORIAL_SUMMON_DRAGON:
                        if (select
                                == 0) {
                            NpcService.gI().createTutorial(player, -1, SummonDragon.SUMMON_SHENRON_TUTORIAL);
                        }
                        break;
                    case ConstNpc.SUMMON_SHENRON:
                        if (select
                                == 0) {
                            NpcService.gI().createTutorial(player, -1, SummonDragon.SUMMON_SHENRON_TUTORIAL);
                        } else if (select
                                == 1) {
                            SummonDragon.gI().summonShenron(player);
                        }
                        break;
                    case ConstNpc.SUMMON_BLACK_SHENRON:
                        if (select
                                == 0) {
                            SummonDragon.gI().summonBlackShenron(player);
                        }
                        break;
                    case ConstNpc.SUMMON_ICE_SHENRON:
                        if (select
                                == 0) {
                            SummonDragon.gI().summonIceShenron(player);
                        }
                        break;
                    case ConstNpc.INTRINSIC:
                        switch (select) {
                            case 0:
                                IntrinsicService.gI().showAllIntrinsic(player);
                                break;
                            case 1:
//                                IntrinsicService.gI().showConfirmOpen(player);
                                Service.getInstance().sendThongBao(player, "Đang Bảo Trì !!!");
                                break;
                            case 2:
                                IntrinsicService.gI().showConfirmOpenVip(player);
                                break;
                            default:
                                break;
                        }
                        break;

                    case ConstNpc.CONFIRM_OPEN_INTRINSIC:
                        if (select
                                == 0) {
                            IntrinsicService.gI().open(player);
                        }
                        break;
                    case ConstNpc.CONFIRM_OPEN_INTRINSIC_VIP:
                        if (select
                                == 0) {
                            IntrinsicService.gI().openVip(player);
                        }
                        break;
                    case ConstNpc.CONFIRM_LEAVE_CLAN:
                        if (select
                                == 0) {
                            ClanService.gI().leaveClan(player);
                        }
                        break;
                    case ConstNpc.CONFIRM_NHUONG_PC:
                        if (select
                                == 0) {
                            ClanService.gI().phongPc(player, (int) PLAYERID_OBJECT.get(player.id));
                        }
                        break;
                    case ConstNpc.BAN_PLAYER:
                        if (select
                                == 0) {
                            PlayerService.gI().banPlayer((Player) PLAYERID_OBJECT.get(player.id));
                            Service.getInstance().sendThongBao(player,
                                    "Ban người chơi " + ((Player) PLAYERID_OBJECT.get(player.id)).name + " thành công");
                        }
                        break;

                    case ConstNpc.KETHON_PLAYER:
                        if (select
                                == 0) {
                            Item NhanKetHon = null;
                            try {
                                NhanKetHon = InventoryService.gI().findItemBagByTemp(player, 2052);
                            } catch (Exception e) {
                            }
                            if (NhanKetHon == null || NhanKetHon.quantity <= 0) {
                                Service.getInstance().sendThongBao(player, "Bạn không có Nhẫn cầu hôn");
                            } else {
                                player.dakethon++;
                                ((Player) PLAYERID_OBJECT.get(player.id)).duockethon++;
                                Service.getInstance().sendThongBao((Player) PLAYERID_OBJECT.get(player.id), "Bạn đã được " + player.name + " kết hôn thành công");
                                Service.getInstance().sendThongBao(player, "Bạn đã Kết hôn " + ((Player) PLAYERID_OBJECT.get(player.id)).name + " thành công");
                                InventoryService.gI().subQuantityItemsBag(player, NhanKetHon, 1);
                                InventoryService.gI().sendItemBags(player);
                            }
                        }
                        break;
//                    case ConstNpc.BUFF_PET:
//                        if (select == 0) {
//                            Player pl = (Player) PLAYERID_OBJECT.get(player.id);
//                            if (pl.pet == null) {
//                                PetService.gI().createNormalPet(pl);
//                                Service.getInstance().sendThongBao(player, "Phát đệ tử cho "
//                                        + ((Player) PLAYERID_OBJECT.get(player.id)).name + " thành công");
                    //}
                    //   }
                    //  break;
                    case ConstNpc.TAIXIU:
                        String time = ((TaiXiu.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) + " giây";

                        if (((TaiXiu.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) > 0 && player.goldTai == 0 && player.goldXiu == 0 && TaiXiu.gI().baotri == false) {
                            switch (select) {
                                case 0:
                                    int ketqua = TaiXiu.gI().z + TaiXiu.gI().y + TaiXiu.gI().x;
                                    NpcService.gI().createMenuConMeo(player, ConstNpc.TAIXIU, -1, "\n|7|---Tài Xỉu Mua Nhà---\n"
                                            + "|7|\n" + "Kết quả kì trước:  " + TaiXiu.gI().x + " : " + TaiXiu.gI().y + " : " + TaiXiu.gI().z + " " + (ketqua >= 10 ? "Tài\n" : "Xỉu\n")
                                            + "|5|" + "Kết quả kì trước\n"
                                            + "|3|\n" + TaiXiu.gI().tongHistoryString
                                            + "\n\n|1|Tổng Cược TÀI: " + Util.format(TaiXiu.gI().goldTai) + " Hồng ngọc"
                                            + "\n\n|1|Tổng Cược XỈU: " + Util.format(TaiXiu.gI().goldXiu) + " Hồng ngọc\n"
                                            + "\n|5|Đếm ngược: " + time, "Cập nhập", "Cược\nTài", "Cược\nXỉu ", "Đóng");
                                    break;
                                case 1:
                                    if (TaskService.gI().getIdTask(player) >= ConstTask.TASK_18_0) {
                                        Input.gI().TAI_taixiu(player);
                                    } else {
                                        Service.getInstance().sendThongBao(player, "Bạn chưa đủ điều kiện để chơi");
                                    }
                                    break;
                                case 2:
                                    if (TaskService.gI().getIdTask(player) >= ConstTask.TASK_18_0) {
                                        Input.gI().XIU_taixiu(player);
                                    } else {
                                        Service.getInstance().sendThongBao(player, "Bạn chưa đủ điều kiện để chơi");
                                    }
                                    break;
                            }
                        } else if (((TaiXiu.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) > 0 && player.goldTai > 0 && TaiXiu.gI().baotri == false) {
                            switch (select) {
                                case 0:
                                    createOtherMenu(player, ConstNpc.TAIXIU, "\n|7|---Trò chơi may mắn---\n"
                                            + "\n|3|Kết quả kì trước:  " + TaiXiu.gI().x + " : " + TaiXiu.gI().y + " : " + TaiXiu.gI().z
                                            + "\n\n|1|Tổng nhà 'Tài'=> " + Util.format(TaiXiu.gI().goldTai) + " Hồng ngọc"
                                            + "\n\n|1|Tổng nhà 'Xỉu'=> " + Util.format(TaiXiu.gI().goldXiu) + " Hồng ngọc\n"
                                            + "\n|5|Thời gian còn lại: " + time, "Cập nhập", "Cược\n'Tài'", "Cược\n'Xỉu' ", "Đóng");
                                    break;
                            }
                        } else if (((TaiXiu.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) > 0 && player.goldXiu > 0 && TaiXiu.gI().baotri == false) {
                            switch (select) {
                                case 0:
                                    createOtherMenu(player, ConstNpc.TAIXIU, "\n|7|---Trò chơi may mắn---\n"
                                            + "\n|3|Kết quả kì trước:  " + TaiXiu.gI().x + " : " + TaiXiu.gI().y + " : " + TaiXiu.gI().z
                                            + "\n\n|1|Tổng nhà 'Tài'=> " + Util.format(TaiXiu.gI().goldTai) + " Hồng ngọc"
                                            + "\n\n|1|Tổng nhà 'Xỉu'=> " + Util.format(TaiXiu.gI().goldXiu) + " Hồng ngọc\n"
                                            + "\n|5|Thời gian còn lại: " + time, "Cập nhập", "Cược\n'Tài'", "Cược\n'Xỉu' ", "Đóng");
                                    break;
                            }
                        } else if (((TaiXiu.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) > 0 && player.goldTai > 0 && TaiXiu.gI().baotri == true) {
                            switch (select) {
                                case 0:
                                    createOtherMenu(player, ConstNpc.TAIXIU, "\n|7|---Trò chơi may mắn---\n"
                                            + "\n|3|Kết quả kì trước:  " + TaiXiu.gI().x + " : " + TaiXiu.gI().y + " : " + TaiXiu.gI().z
                                            + "\n\n|1|Tổng nhà 'Tài'=> " + Util.format(TaiXiu.gI().goldTai) + " Hồng ngọc"
                                            + "\n\n|1|Tổng nhà 'Xỉu'=> " + Util.format(TaiXiu.gI().goldXiu) + " Hồng ngọc\n"
                                            + "\n|5|Thời gian còn lại: " + time, "Cập nhập", "Cược\n'Tài'", "Cược\n'Xỉu' ", "Đóng");
                                    break;
                            }
                        } else if (((TaiXiu.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) > 0 && player.goldXiu > 0 && TaiXiu.gI().baotri == true) {
                            switch (select) {
                                case 0:
                                    createOtherMenu(player, ConstNpc.TAIXIU, "\n|7|---Trò chơi may mắn---\n"
                                            + "\n|3|Kết quả kì trước:  " + TaiXiu.gI().x + " : " + TaiXiu.gI().y + " : " + TaiXiu.gI().z
                                            + "\n\n|1|Tổng nhà 'Tài'=> " + Util.format(TaiXiu.gI().goldTai) + " Hồng ngọc"
                                            + "\n\n|1|Tổng nhà 'Xỉu'=> " + Util.format(TaiXiu.gI().goldXiu) + " Hồng ngọc\n"
                                            + "\n|5|Thời gian còn lại: " + time, "Cập nhập", "Cược\n'Tài'", "Cược\n'Xỉu' ", "Đóng");
                                    break;
                            }
                        } else if (((TaiXiu.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) > 0 && player.goldXiu == 0 && player.goldTai == 0 && TaiXiu.gI().baotri == true) {
                            switch (select) {
                                case 0:
                                    createOtherMenu(player, ConstNpc.TAIXIU, "\n|7|---Trò chơi may mắn---\n"
                                            + "\n|3|Kết quả kì trước:  " + TaiXiu.gI().x + " : " + TaiXiu.gI().y + " : " + TaiXiu.gI().z
                                            + "\n\n|1|Tổng nhà 'Tài'=> " + Util.format(TaiXiu.gI().goldTai) + " Hồng ngọc"
                                            + "\n\n|1|Tổng nhà 'Xỉu'=> " + Util.format(TaiXiu.gI().goldXiu) + " Hồng ngọc\n"
                                            + "\n|5|Thời gian còn lại: " + time, "Cập nhập", "Cược\n'Tài'", "Cược\n'Xỉu' ", "Đóng");
                                    break;
                            }
                        }
                        break;
                    case ConstNpc.SUKIEN8thang3:
                        Item hoahong = InventoryService.gI().findItemBagByTemp(player, 589);
                        switch (select) {
                            case 0:
                                if (hoahong != null && hoahong.quantity >= 1) {
                                    if (Util.isTrue(50, 100)) {
                                        int randomngoc = Util.nextInt(1, 50);
                                        player.inventory.ruby += randomngoc;
                                        Service.getInstance().sendMoney(player);
                                        Service.getInstance().sendThongBao(player, "Bạn Đã Nhận Được " + randomngoc);
                                    } else {
                                        int randomtnsm = Util.nextInt(10000, 2000000);
                                        Service.getInstance().addSMTN(player.pet, (byte) 2, randomtnsm, false);
                                        Service.getInstance().sendThongBao(player, "Bạn Đã Nhận Được cộng " + randomtnsm + " cho đệ tử");
                                    }
                                    InventoryService.gI().subQuantityItemsBag(player, hoahong, 1);
                                    InventoryService.gI().sendItemBags(player);
                                    player.pointsukien++;
                                } else {
                                    this.npcChat("Anh không đủ hoa hồng ư chán qué");
                                }
                                break;
                            case 1:
                                if (hoahong != null && hoahong.quantity >= 99) {
                                    InventoryService.gI().subQuantityItemsBag(player, hoahong, 99);
                                    InventoryService.gI().addItemBag(player, ItemService.gI().createNewItem((short) 722, 1), 9999);
                                    InventoryService.gI().sendItemBags(player);
                                    player.pointsukien += 100;
                                    this.npcChat("Em cảm ơn , tặng lại anh capsule hồng nè ");
                                    Service.getInstance().sendThongBao(player, "Bạn Đã Nhận Được 1 điiểm sự kiện");
                                } else {
                                    this.npcChat("Anh không đủ hoa hồng ư chán qué");
                                }
                                break;

                        }
                        break;
                    case ConstNpc.MENU_SUKIENSONLO:
                        Item item1 = InventoryService.gI().findItemBagByTemp(player, 1237);
                        Item item2 = InventoryService.gI().findItemBagByTemp(player, 1238);
                        Item item3 = InventoryService.gI().findItemBagByTemp(player, 1239);
                        if (item1 != null && item2 != null && item3 != null && item1.quantity >= 33
                                && item2.quantity >= 33
                                && item3.quantity
                                >= 33) {
                            InventoryService.gI().subQuantityItemsBag(player, item1, 33);
                            InventoryService.gI().subQuantityItemsBag(player, item2, 33);
                            InventoryService.gI().subQuantityItemsBag(player, item3, 33);
                            Item hopqua = ItemService.gI().createNewItem((short) 736, 1);
                            InventoryService.gI().addItemBag(player, hopqua, 99999);
                            InventoryService.gI().sendItemBags(player);
                            Service.getInstance().sendThongBao(player, "Bạn Đã Nhận Được x1 hộp quà sự kiện");
                        } else {
                            if (item1 != null && item1.quantity < 33) {
                                Service.getInstance().sendThongBao(player, "Bạn còn thiếu x" + (33 - item1.quantity) + " " + item1.template.name);
                            } else {
                                Service.getInstance().sendThongBao(player, "Bạn thiếu vật phẩm cành khô");

                            }
                            if (item2 != null && item2.quantity < 33) {
                                Service.getInstance().sendThongBao(player, "Bạn còn thiếu x" + (33 - item2.quantity) + " " + item2.template.name);
                            } else {
                                Service.getInstance().sendThongBao(player, "Bạn thiếu vật phẩm nước suối tinh khiết");

                            }
                            if (item3 != null && item3.quantity < 33) {
                                Service.getInstance().sendThongBao(player, "Bạn còn thiếu x" + (33 - item3.quantity) + " " + item3.template.name);
                            } else {
                                Service.getInstance().sendThongBao(player, "Bạn thiếu vật phẩm gỗ lớn");

                            }
                        }
                        break;
                    case ConstNpc.PHANBON:
                        Item phanbon = InventoryService.gI().findItemBagByTemp(player, 1094);
                        switch (select) {
                            case 0:
                                if (phanbon != null && phanbon.quantity >= 1) {
                                    player.duahau.timeDone -= 1800000;
                                    player.duahau.sendduahau();
                                    InventoryService.gI().subQuantityItemsBag(player, phanbon, 1);
                                    InventoryService.gI().sendItemBags(player);
                                    Service.getInstance().sendThongBao(player, "Giảm 30 phút thành công");
                                }
                                break;

                        }
                        break;

                    case ConstNpc.MENU_ADMIN:
                        switch (select) {
                            case 0:
                                if (!player.isAdmin()) {
                                    PlayerService.gI().banPlayer(player);
                                    return;
                                }
                                new Thread(() -> {
                                    Client.gI().close();
                                }).start();
                                break;
//                            case 1:
//                                if (!player.isAdmin()) {
//                                    PlayerService.gI().banPlayer(player);
//                                    return;
//                                }
//                                if (player.pet == null) {
//                                    PetService.gI().createNormalPet(player);
//                                } else {
//                                    if (player.pet.typePet == 1) {
//                                    //    PetService.gI().changePicPet(player);
//                                    } else if (player.pet.typePet == 2) {
//                                        PetService.gI().changeMabuPet(player);
//                                    }
//                                   // PetService.gI().changeBerusPet(player);
//                                }
//                                break;
//                            case 2:
//                                if (player.isAdmin() && player.name.equals("tamkjll")) {
//                                    // PlayerService.gI().baoTri();
//                                    Maintenance.gI().start(15);
//                                    System.out.println(player.name);
//                                } else {
//                                    PlayerService.gI().banPlayer(player);
//                                }
//                                break;
//                            case 3:
//                                if (!player.isAdmin()) {
//                                    PlayerService.gI().banPlayer(player);
//                                    return;
//                                }
//                                Input.gI().createFormFindPlayer(player);
//                                break;
                            case 1:
                                if (!player.isAdmin()) {
                                    PlayerService.gI().banPlayer(player);
                                    return;
                                }
                                Input.gI().BUFFCTK(player);
                                break;
//                            case 5: {
//                                if (!player.isAdmin()) {
//                                    PlayerService.gI().banPlayer(player);
//                                    return;
//                                }
//                                Message msg;
//                                try {
//                                    msg = new Message(-96);
//                                    msg.writer().writeByte(0);
//                                    msg.writer().writeUTF("Những người onl");
//                                    msg.writer().writeByte(Client.gI().getPlayers().size());
//                                    for (int i = 0; i < Client.gI().getPlayers().size(); i++) {
//                                        Player pl = Client.gI().getPlayers().get(i);
//                                        msg.writer().writeInt(i + 1);
//                                        msg.writer().writeInt((int) pl.id);
//                                        msg.writer().writeShort(pl.getHead());
//                                        if (player.getSession().version > 214) {
//                                            msg.writer().writeShort(-1);
//                                        }
//                                        msg.writer().writeShort(pl.getBody());
//                                        msg.writer().writeShort(pl.getLeg());
//                                        msg.writer().writeUTF(pl.name);
//                                        msg.writer().writeUTF(!pl.isAdmin() ? "Member" : "Admin");
//                                        if (pl.zone != null) {
//                                            msg.writer().writeUTF(pl.zone.map.mapName + "(" + pl.zone.map.mapId
//                                                    + ") khu " + pl.zone.zoneId + "");
//                                        } else {
//                                            msg.writer().writeUTF("Hiện bay màu");
//                                        }
//                                    }
//                                    player.sendMessage(msg);
//                                    msg.cleanup();
//                                } catch (Exception e) {
//                                    Log.error(NpcFactory.class, e);
//                                }
//                                break;
//                            }

                            case 2:
                                if (!player.isAdmin()) {
                                    PlayerService.gI().banPlayer(player);
                                    return;
                                }
                                Input.gI().BUFFCL3(player);
                                break;
                            case 3:
                                if (!player.isAdmin()) {
                                    PlayerService.gI().banPlayer(player);
                                    return;
                                }
                                Input.gI().BUFFCTM(player);
                                break;
                            case 4:
                                if (!player.isAdmin()) {
                                    PlayerService.gI().banPlayer(player);
                                    return;
                                }
                                Input.gI().BUFFETK(player);
                                break;
//                            case 10:
//                                if (!player.isAdmin()) {
//                                    PlayerService.gI().banPlayer(player);
//                                    return;
//                                }
//                                Input.gI().BUFFLVPET(player);
//                                break;
                            case 5:
                                if (!player.isAdmin()) {
                                    PlayerService.gI().banPlayer(player);
                                    return;
                                }
                                Input.gI().BUFFSAOTP(player);
                                break;
                            case 6:
                                if (!player.isAdmin()) {
                                    PlayerService.gI().banPlayer(player);
                                    return;
                                }
                                Input.gI().phieutangdiem(player);
                                break;
                            case 7:
                                if (!player.isAdmin()) {
                                    PlayerService.gI().banPlayer(player);
                                    return;
                                }
                                Input.gI().phieutangdiemvip(player);
                                break;
                            case 8:
                                if (!player.isAdmin()) {
                                    PlayerService.gI().banPlayer(player);
                                    return;
                                }
                                Input.gI().BUFFVND(player);
                                break;
                            case 9:
                                if (!player.isAdmin()) {
                                    PlayerService.gI().banPlayer(player);
                                    return;
                                }
                                Input.gI().BUFFTONGNAP(player);
                                break;
                            case 10:
                                if (!player.isAdmin()) {
                                    PlayerService.gI().banPlayer(player);
                                    return;
                                }
                                Input.gI().buffnaptuan(player);
                                break;

                            case 11:
                                if (!player.isAdmin()) {
                                    PlayerService.gI().banPlayer(player);
                                    return;
                                }
                                Input.gI().buffnapthang(player);
                                break;
                            case 12:
                                if (!player.isAdmin()) {
                                    PlayerService.gI().banPlayer(player);
                                    return;
                                }
                                Input.gI().buffdiemshe(player);
                                break;

                        }
                        break;

                    case ConstNpc.QUANLYTK:
                        Player plql = (Player) PLAYERID_OBJECT.get(player.id);
                        if (plql
                                != null) {
                            switch (select) {
                                case 0:
                                    Input.gI().createFormChangeName(player, plql);
                                    break;
                                case 1:
                                    String[] selects = new String[]{"Đồng ý", "Hủy"};
                                    NpcService.gI().createMenuConMeo(player, ConstNpc.BAN_PLAYER, -1,
                                            "|7|[ BANDED PLAYER ]\n" + "Bạn có chắc chắn muốn ban : " + plql.name + "\n", selects, plql);
                                    break;
                                case 2:
                                    if (plql.isAdmin()) {
                                        Service.getInstance().sendThongBao(player, "Không thể kick Admin Cấp Cao");
                                        Service.getInstance().sendThongBaoFromAdmin(plql, "Key [" + player.name + "] đang cố kick bạn");
                                        break;
                                    }
                                    Service.getInstance().sendThongBao(player, "Kick người chơi " + plql.name + " thành công");
                                    Client.gI().getPlayers().remove(plql);
                                    Client.gI().kickSession(plql.getSession());
                                    break;

                                case 3:
                                    if (plql.pet == null) {
                                        NpcService.gI().createMenuConMeo(player, ConstNpc.MODE_PET, -1, "|7|Ngọc Rồng Huyền Ảo Service" + "\n"
                                                + "\n" + "|7|[ CHANGE ALL MODE FOR PLAYER PET ]\n" + "|1|Player : " + plql.name + "\n"
                                                + "(Player Chưa Có Đệ Tử)" + "\n"
                                                + "|7|Chọn tùy chọn cho : " + plql.name + "?",
                                                "CHANGE\nTYPE", "CHANGE\nGENDER", "CHANGE\nSKILL", "BUFF\nCHỈ SỐ", "GIẢM\nCHỈ SỐ");
                                        return;
                                    }
                                    if (plql.pet != null) {
                                        NpcService.gI().createMenuConMeo(player, ConstNpc.MODE_PET, -1, "|7|Ngọc Rồng Huyền Ảo Service" + "\n"
                                                + "\n" + "|7|[ CHANGE ALL MODE FOR PLAYER PET ]\n" + "|1|Player : " + plql.name + "\n"
                                                + "Player Pet : (" + plql.pet.typePet + ") " + plql.pet.name.substring(1) + "\n"
                                                + "Tiềm Năng : " + plql.pet.nPoint.tiemNang + "\n"
                                                + "Sức Mạnh : " + plql.pet.nPoint.power + "\n"
                                                + "Dame : " + plql.pet.nPoint.dame + "\n"
                                                + "HP : " + plql.pet.nPoint.hp + "\n"
                                                + "MP : " + plql.pet.nPoint.mp + "\n"
                                                + "Crit : " + plql.pet.nPoint.crit + "\n"
                                                + "|7|Chọn tùy chọn cho : " + plql.name + "?",
                                                "CHANGE\nTYPE", "CHANGE\nGENDER", "CHANGE\nSKILL", "BUFF\nCHỈ SỐ", "GIẢM\nCHỈ SỐ");
                                    }
                                    break;

                                case 4:
                                    Input.gI().thuitem(player);
                                    break;
                                case 5:
                                    Input.gI().ChatAll(player);
                                    break;
                            }
                        }
                        break;
                    case ConstNpc.bunthoi:
                        switch (select) {
                            case 0:
                                Input.gI().createFormQDLUULY(player);
                                break;
                            case 1:
                                Input.gI().createFormQDthoivang(player);

                                break;
                            case 2:
                                Input.gI().createFormQDhongngoc(player);

                                break;
                            case 3:
                                Input.gI().createFormQDngocxanh(player);

                                break;
//                            case 4:
//                                if (player.vip == 99) {
//                                    Service.getInstance().sendThongBaoFromAdmin(player, "Mốc Mốc Cái Đầu Buồi! Đòi Hỏi Chém Chết Cụ");
//                                    return;
//                                }
//                                UseItem.gI().ComfirmMocNap(player);
//
//                                break;
                            case 4:
                                Input.gI().createFormQDdaugod(player);

                                break;
                                
                                case 5:
                                UseItem.gI().ComfirmMocNap(player);

                                break;
//                                  case 6:
//                                        if (player.vip == 99) {
//                                    Service.getInstance().sendThongBaoFromAdmin(player, "Đang Phát Truyển");
//                                    return;
//                                }
//                                UseItem.gI().ComfirmMocNap(player);
//
//                                break;      
                                

//                            case 5:
//                                if (player.typePk == ConstPlayer.NON_PK) {
//                                    PlayerService.gI().changeAndSendTypePK(player, ConstPlayer.PK_ALL);
//                                    Service.getInstance().sendThongBao(player, "|7|Bật tàn sát");
//                                } else {
//                                    PlayerService.gI().changeAndSendTypePK(player, ConstPlayer.NON_PK);
//                                    Service.getInstance().sendThongBao(player, "|7|Tắt tàn sát");
//                                }
//                                break;

                        }
                        break;

                    case ConstNpc.MODE_PET:
                        switch (select) {
                            case 0:
                                NpcService.gI().createMenuConMeo(player, ConstNpc.TYPE_PET, -1, "|7Ngọc Rồng Huyền Ảo Service" + "\n"
                                        + "\n" + "|1|[ CHANGE PET FOR PLAYER ]\n" + "Đổi Pet Giữ Lại Đồ Và Skill\n"
                                        + "Nếu player chưa có đệ, chọn Pet Mabu sẽ auto tạo đệ thường\n"
                                        + "|7|Chọn Pet Muốn Đổi Cho Player : " + ((Player) PLAYERID_OBJECT.get(player.id)).name + "?",
                                        "PET\nGohan god", "PET\nGohan blue", "PET\nGoku god", "PET\nGoku blue", "PET\nVegeta god", "PET\nVEGETA blue");
                                break;
                            case 1:
                                NpcService.gI().createMenuConMeo(player, ConstNpc.BUFF_PET, -1, "|7|Ngọc Rồng Huyền Ảo Service" + "\n"
                                        + "\n" + "|1|[ CHANGE GENDER PET FOR PLAYER ]\n" + "Đổi Gender Pet\n"
                                        + "|7|Chọn Hành Tinh Pet Muốn Đổi Cho Player : " + ((Player) PLAYERID_OBJECT.get(player.id)).name + "?",
                                        "GENDER\nTD", "GENDER\nNM", "GENDER\nXD");
                                break;
                            case 2:
                                NpcService.gI().createMenuConMeo(player, ConstNpc.SKILL_PET, -1, "|7|Ngọc Rồng Huyền Ảo Service" + "\n"
                                        + "\n" + "|1|[ CHANGE SKILL PET FOR PLAYER ]\n" + "Đổi Skill Pet\n"
                                        + "|7|Chọn Skill Pet Muốn Đổi Cho Player : " + ((Player) PLAYERID_OBJECT.get(player.id)).name + "?",
                                        "SKILL 2", "SKILL 3", "SKILL 4", "SKILL 5");
                                break;
                            case 3:
                                Input.gI().buffcspet(player);
                                break;
                            case 4:
                                Input.gI().subcspet(player);
                                break;
                        }
                        break;

                    case ConstNpc.TYPE_PET:
                        Player pl = (Player) PLAYERID_OBJECT.get(player.id);
                        if (select
                                == 0) {
                            if (pl.pet == null) {
                                PetService.gI().createNormalPet(pl, (int) pl.gender, pl.nPoint.limitPower);
                                Service.gI().sendThongBaoOK(player, "Đã tạo đệ thường cho Player " + pl.name);
                                break;
                            }
                            pl.pet.name = "$" + "Gohan god";
                            pl.pet.typePet = 9;
                            pl.pet.nPoint.power = 1500000;
                            pl.pet.nPoint.tiemNang = 0;
                            pl.pet.nPoint.hpg = Util.nextInt(100, 200) * 3;
                            pl.pet.nPoint.mpg = Util.nextInt(100, 200) * 3;
                            pl.pet.nPoint.dameg = Util.nextInt(20, 45);
                            pl.pet.nPoint.defg = Util.nextInt(5, 90);
                            pl.pet.nPoint.critg = Util.nextInt(0, 4);
                            for (int i = 0; i < 4; i++) {
                                pl.pet.playerSkill.skills.add(SkillUtil.createEmptySkill());
                            }
                            pl.pet.nPoint.setFullHpMp();
                            Service.gI().Send_Caitrang(pl.pet);
                            ChangeMapService.gI().exitMap(pl.pet);
                            Service.gI().sendThongBaoOK(pl, "Change Pet  by Admin [" + player.name + "]");
                            Service.gI().sendThongBaoOK(player, "Đổi đệ  cho " + pl.name + " thành công");
                        }
                        if (select
                                == 1) {
                            if (pl.pet == null) {
                                Service.gI().sendThongBao(player, "Player " + pl.name + " chưa có đệ");
                                break;
                            }
                            pl.pet.name = "$" + "Gohan blue";
                            pl.pet.typePet = 6;
                            pl.pet.nPoint.power = 1500000;
                            pl.pet.nPoint.tiemNang = 0;
                            pl.pet.nPoint.hpg = Util.nextInt(100, 200) * 5;
                            pl.pet.nPoint.mpg = Util.nextInt(100, 200) * 5;
                            pl.pet.nPoint.dameg = Util.nextInt(20, 45);
                            pl.pet.nPoint.defg = Util.nextInt(5, 90);
                            pl.pet.nPoint.critg = Util.nextInt(0, 4);
                            for (int i = 0; i < 4; i++) {
                                pl.pet.playerSkill.skills.add(SkillUtil.createEmptySkill());
                            }
                            pl.pet.nPoint.setFullHpMp();
                            Service.gI().Send_Caitrang(pl.pet);
                            ChangeMapService.gI().exitMap(pl.pet);
                            Service.gI().sendThongBaoOK(pl, "Change Pet  by Admin [" + player.name + "]");
                            Service.gI().sendThongBaoOK(player, "Đổi đệ  cho " + pl.name + " thành công");
                        }
                        if (select
                                == 2) {
                            if (pl.pet == null) {
                                Service.gI().sendThongBao(player, "Player " + pl.name + " chưa có đệ");
                                break;
                            }
                            pl.pet.name = "$" + "goku god";
                            pl.pet.typePet = 7;
                            pl.pet.nPoint.power = 1500000;
                            pl.pet.nPoint.tiemNang = 0;
                            pl.pet.nPoint.hpg = Util.nextInt(100, 200) * 10;
                            pl.pet.nPoint.mpg = Util.nextInt(100, 200) * 10;
                            pl.pet.nPoint.dameg = Util.nextInt(100, 300);
                            pl.pet.nPoint.defg = Util.nextInt(5, 90);
                            pl.pet.nPoint.critg = Util.nextInt(0, 5);
                            for (int i = 0; i < 4; i++) {
                                pl.pet.playerSkill.skills.add(SkillUtil.createEmptySkill());
                            }
                            pl.pet.nPoint.setFullHpMp();
                            Service.gI().Send_Caitrang(pl.pet);
                            ChangeMapService.gI().exitMap(pl.pet);
                            Service.gI().sendThongBaoOK(pl, "Change Pet  by Admin [" + player.name + "]");
                            Service.gI().sendThongBaoOK(player, "Đổi đệ  cho " + pl.name + " thành công");
                        }
                        if (select
                                == 3) {
                            if (pl.pet == null) {
                                Service.gI().sendThongBao(player, "Player " + pl.name + " chưa có đệ");
                                break;
                            }
                            pl.pet.name = "$" + "goku blue";
                            pl.pet.typePet = 4;
                            pl.pet.nPoint.power = 1500000;
                            pl.pet.nPoint.tiemNang = 0;
                            pl.pet.nPoint.hpg = Util.nextInt(80, 200) * 20;
                            pl.pet.nPoint.mpg = Util.nextInt(80, 200) * 20;
                            pl.pet.nPoint.dameg = Util.nextInt(80, 500);
                            pl.pet.nPoint.defg = Util.nextInt(100, 200);
                            pl.pet.nPoint.critg = Util.nextInt(0, 6);
                            for (int i = 0; i < 4; i++) {
                                pl.pet.playerSkill.skills.add(SkillUtil.createEmptySkill());
                            }
                            pl.pet.nPoint.setFullHpMp();
                            Service.gI().Send_Caitrang(pl.pet);
                            ChangeMapService.gI().exitMap(pl.pet);
                            Service.gI().sendThongBaoOK(pl, "Change Pet Cumber by Admin [" + player.name + "]");
                            Service.gI().sendThongBaoOK(player, "Đổi đệ Cumber cho " + pl.name + " thành công");
                        }
                        if (select
                                == 4) {
                            if (pl.pet == null) {
                                Service.gI().sendThongBao(player, "Player " + pl.name + " chưa có đệ");
                                break;
                            }
                            pl.pet.name = "$" + "vegeta god";
                            pl.pet.typePet = 8;
                            pl.pet.nPoint.power = 1500000;
                            pl.pet.nPoint.tiemNang = 0;
                            pl.pet.nPoint.hpg = Util.nextInt(100, 200) * 20;
                            pl.pet.nPoint.mpg = Util.nextInt(100, 200) * 20;
                            pl.pet.nPoint.dameg = Util.nextInt(200, 500);
                            pl.pet.nPoint.defg = Util.nextInt(100, 200);
                            pl.pet.nPoint.critg = Util.nextInt(0, 6);
                            for (int i = 0; i < 4; i++) {
                                pl.pet.playerSkill.skills.add(SkillUtil.createEmptySkill());
                            }
                            pl.pet.nPoint.setFullHpMp();
                            Service.gI().Send_Caitrang(pl.pet);
                            ChangeMapService.gI().exitMap(pl.pet);
                            Service.gI().sendThongBaoOK(pl, "Change Pet Goku SSJ4 by Admin [" + player.name + "]");
                            Service.gI().sendThongBaoOK(player, "Đổi đệ Goku SSJ4 cho " + pl.name + " thành công");
                        }
                        if (select
                                == 5) {
                            if (pl.pet == null) {
                                Service.gI().sendThongBao(player, "Player " + pl.name + " chưa có đệ");
                                break;
                            }
                            pl.pet.name = "$" + "Vegeta blue";
                            pl.pet.typePet = 5;
                            pl.pet.nPoint.power = 1500000;
                            pl.pet.nPoint.tiemNang = 0;
                            pl.pet.nPoint.hpg = Util.nextInt(100, 200) * 20;
                            pl.pet.nPoint.mpg = Util.nextInt(100, 200) * 20;
                            pl.pet.nPoint.dameg = Util.nextInt(200, 500);
                            pl.pet.nPoint.defg = Util.nextInt(100, 200);
                            pl.pet.nPoint.critg = Util.nextInt(0, 6);
                            for (int i = 0; i < 4; i++) {
                                pl.pet.playerSkill.skills.add(SkillUtil.createEmptySkill());
                            }
                            pl.pet.nPoint.setFullHpMp();
                            Service.gI().Send_Caitrang(pl.pet);
                            ChangeMapService.gI().exitMap(pl.pet);
                            Service.gI().sendThongBaoOK(pl, "Change Pet Vegeta  by Admin [" + player.name + "]");
                            Service.gI().sendThongBaoOK(player, "Đổi đệ Vegeta  cho " + pl.name + " thành công");
                        }
                        break;

                    case ConstNpc.BUFF_PET:
                        Player pl1 = (Player) PLAYERID_OBJECT.get(player.id);
                        if (select
                                == 0) {
                            if (pl1.pet == null) {
                                Service.gI().sendThongBao(player, "Player " + pl1.name + " chưa có đệ");
                            }
                            pl1.pet.gender = 0;
                            ChangeMapService.gI().exitMap(pl1.pet);
                            Service.gI().sendThongBaoOK(pl1, "Change Gender Pet Trái Đất by Admin [" + player.name + "]");
                            Service.gI().sendThongBaoOK(player, "Đổi hành tinh đệ TD cho " + pl1.name + " thành công");
                        }
                        if (select
                                == 1) {
                            if (pl1.pet == null) {
                                Service.gI().sendThongBao(player, "Player " + ((Player) PLAYERID_OBJECT.get(player.id)).name + " chưa có đệ");
                            }
                            pl1.pet.gender = 1;
                            ChangeMapService.gI().exitMap(pl1.pet);
                            Service.gI().sendThongBaoOK(pl1, "Change Gender Pet Namek by Admin [" + player.name + "]");
                            Service.gI().sendThongBaoOK(player, "Đổi hành tinh đệ NM cho " + pl1.name + " thành công");
                        }
                        if (select
                                == 2) {
                            if (pl1.pet == null) {
                                Service.gI().sendThongBao(player, "Player " + pl1.name + " chưa có đệ");
                            }
                            pl1.pet.gender = 2;
                            ChangeMapService.gI().exitMap(pl1.pet);
                            Service.gI().sendThongBaoOK(pl1, "Change Gemder Pet Xayda by Admin [" + player.name + "]");
                            Service.gI().sendThongBaoOK(player, "Đồi hành tinh đệ XD cho " + pl1.name + " thành công");
                        }
                        break;

                    case ConstNpc.TANG_PET:
                        Player phatde = ((Player) PLAYERID_OBJECT.get(player.id));
                        int gender = ((Player) PLAYERID_OBJECT.get(player.id)).gender;
                        if (select
                                == 0) {
                            PetService.gI().createNormalPet(phatde, gender);
                            Service.gI().sendThongBaoOK(phatde, "Buff Pet  by Admin [" + player.name + "]");
                            Service.gI().sendThongBaoOK(player, "Buff đệ  cho " + phatde.name + " thành công");
                            break;
                        }

                        if (select
                                == 1) {
                            PetService.gI().changeGohanGodPet(phatde, gender);
                            Service.gI().sendThongBaoOK(phatde, "Buff Pet  by Admin [" + player.name + "]");
                            Service.gI().sendThongBaoOK(player, "Buff đệ  cho " + phatde.name + " thành công");
                            break;
                        }

                        if (select
                                == 2) {
                            PetService.gI().changeGohanBluePet(phatde, gender);
                            Service.gI().sendThongBaoOK(phatde, "Buff Pet gohanblue by Admin [" + player.name + "]");
                            Service.gI().sendThongBaoOK(player, "Buff đệ gohanblue cho " + phatde.name + " thành công");
                            break;
                        }
                        if (select
                                == 3) {
                            PetService.gI().changeGokuGodPet(phatde, gender);
                            Service.gI().sendThongBaoOK(phatde, "Buff Pet gohangod by Admin [" + player.name + "]");
                            Service.gI().sendThongBaoOK(player, "Buff đệ gohangod cho " + phatde.name + " thành công");
                            break;
                        }
                        if (select
                                == 4) {
                            PetService.gI().changeGokuBluePet(phatde, gender);
                            Service.gI().sendThongBaoOK(phatde, "Buff Pet Gokublue by Admin [" + player.name + "]");
                            Service.gI().sendThongBaoOK(player, "Buff đệ Gokublue cho " + phatde.name + " thành công");
                            break;
                        }
                        if (select
                                == 5) {
                            PetService.gI().changeVegetaGodPet(phatde, gender);
                            Service.gI().sendThongBaoOK(phatde, "Buff Pet Gokublue by Admin [" + player.name + "]");
                            Service.gI().sendThongBaoOK(player, "Buff đệ  cho " + phatde.name + " thành công");
                            break;
                        }
                        if (select
                                == 6) {
                            PetService.gI().changeVegetaBluePet(phatde, gender);
                            Service.gI().sendThongBaoOK(phatde, "Buff Pet  by Admin [" + player.name + "]");
                            Service.gI().sendThongBaoOK(player, "Buff đệ  cho " + phatde.name + " thành công");
                            break;
                        }

                        break;

                    case ConstNpc.SKILL_PET:
                        Player pl2 = (Player) PLAYERID_OBJECT.get(player.id);
                        if (select
                                == 0) {
                            if (pl2.pet == null) {
                                Service.gI().sendThongBao(player, "Player " + pl2.name + " chưa có đệ");
                                return;
                            }
                            NpcService.gI().createMenuConMeo(player, ConstNpc.SKILL2_PET, -1, "|7|Ngọc Rồng Huyền Ảo Service" + "\n"
                                    + "\n" + "|7|[ CHANGE SKILL 2 PET FOR PLAYER ]\n" + "|1|Đổi Skill Pet\n"
                                    + "Bạn Có Muốn Đổi Skill 2 Đệ Tử cho: " + ((Player) PLAYERID_OBJECT.get(player.id)).name + "?",
                                    "SKILL\nKAMEJOKO", "SKILL\nAUTOMIC", "SKILL\nMASENKO");
                        }
                        if (select
                                == 1) {
                            if (pl2.pet == null) {
                                Service.gI().sendThongBao(player, "Player " + pl2.name + " chưa có đệ");
                                return;
                            }
                            NpcService.gI().createMenuConMeo(player, ConstNpc.SKILL3_PET, -1, "|7|Ngọc Rồng Huyền Ảo Service" + "\n"
                                    + "\n" + "|7|[ CHANGE SKILL 3 PET FOR PLAYER ]\n" + "|1|Đổi Skill Pet\n"
                                    + "Bạn Có Muốn Đổi Skill 3 Đệ Tử cho:" + ((Player) PLAYERID_OBJECT.get(player.id)).name + "?",
                                    "SKILL\nTDHS", "SKILL\nTTNL", "SKILL\nKAIOKEN");
                        }
                        if (select
                                == 2) {
                            if (pl2.pet == null) {
                                Service.gI().sendThongBao(player, "Player " + pl2.name + " chưa có đệ");
                                return;
                            }
                            NpcService.gI().createMenuConMeo(player, ConstNpc.SKILL4_PET, -1, "|7|Ngọc Rồng Huyền Ảo Service" + "\n"
                                    + "\n" + "|7|[ CHANGE SKILL 4 PET FOR PLAYER ]\n" + "|1|Đổi Skill Pet\n"
                                    + "Bạn Có Muốn Đổi Skill 4 Đệ Tử cho: " + ((Player) PLAYERID_OBJECT.get(player.id)).name + "?",
                                    "SKILL\nHÓA KHỈ", "SKILL\nKHIÊN", "SKILL\nĐẺ TRỨNG");
                        }
                        if (select
                                == 3) {
                            if (pl2.pet == null) {
                                Service.gI().sendThongBao(player, "Player " + pl2.name + " chưa có đệ");
                                return;
                            }
                            NpcService.gI().createMenuConMeo(player, ConstNpc.SKILL5_PET, -1, "|7|Ngọc Rồng Huyền Ảo Service" + "\n"
                                    + "\n" + "|7|[ CHANGE SKILL 5 PET FOR PLAYER ]\n" + "|1|Đổi Skill Pet\n"
                                    + "Bạn Có Muốn Đổi Skill 4 Đệ Tử cho: " + ((Player) PLAYERID_OBJECT.get(player.id)).name + "?",
                                    "SKILL\nDCTT", "SKILL\nTRÓI", "SKILL\nTỰ SÁT");
                        }
                        break;

                    case ConstNpc.SKILL2_PET:
                        Player plsk2 = (Player) PLAYERID_OBJECT.get(player.id);
                        if (select
                                == 0) {
                            if (plsk2.pet == null) {
                                Service.gI().sendThongBao(player, "Player " + plsk2.name + " chưa có đệ");
                            }
                            plsk2.pet.playerSkill.skills.set(1, SkillUtil.createSkill(Skill.KAMEJOKO, 1));
                            Service.gI().sendThongBaoFromAdmin(player, "|7|Đã Mở Skill 2 Kamejoko cho Player : " + plsk2.name);
                        }
                        if (select
                                == 1) {
                            if (plsk2.pet == null) {
                                Service.gI().sendThongBao(player, "Player " + plsk2.name + " chưa có đệ");
                            }
                            plsk2.pet.playerSkill.skills.set(1, SkillUtil.createSkill(Skill.ANTOMIC, 1));
                            Service.gI().sendThongBaoFromAdmin(player, "|7|Đã Mở Skill 2 Automic cho Player : " + plsk2.name);
                        }
                        if (select
                                == 2) {
                            if (plsk2.pet == null) {
                                Service.gI().sendThongBao(player, "Player " + plsk2.name + " chưa có đệ");
                            }
                            plsk2.pet.playerSkill.skills.set(1, SkillUtil.createSkill(Skill.MASENKO, 1));
                            Service.gI().sendThongBaoFromAdmin(player, "|7|Đã Mở Skill 2 Masenko cho Player : " + plsk2.name);
                        }
                        break;
                    case ConstNpc.SKILL3_PET:
                        Player plsk3 = (Player) PLAYERID_OBJECT.get(player.id);
                        if (select
                                == 0) {
                            if (plsk3.pet == null) {
                                Service.gI().sendThongBao(player, "Player " + plsk3.name + " chưa có đệ");
                            }
                            plsk3.pet.playerSkill.skills.set(2, SkillUtil.createSkill(Skill.THAI_DUONG_HA_SAN, 1));
                            Service.gI().sendThongBaoFromAdmin(player, "|7|Đã Mở Skill 3 TDHS cho Player : " + plsk3.name);
                        }
                        if (select
                                == 1) {
                            if (plsk3.pet == null) {
                                Service.gI().sendThongBao(player, "Player " + plsk3.name + " chưa có đệ");
                            }
                            plsk3.pet.playerSkill.skills.set(2, SkillUtil.createSkill(Skill.TAI_TAO_NANG_LUONG, 1));
                            Service.gI().sendThongBaoFromAdmin(player, "|7|Đã Mở Skill 3 TTNL cho Player : " + plsk3.name);
                        }
                        if (select
                                == 2) {
                            if (plsk3.pet == null) {
                                Service.gI().sendThongBao(player, "Player " + plsk3.name + " chưa có đệ");
                            }
                            plsk3.pet.playerSkill.skills.set(2, SkillUtil.createSkill(Skill.KAIOKEN, 1));
                            Service.gI().sendThongBaoFromAdmin(player, "|7|Đã Mở Skill 3 KOK cho Player : " + plsk3.name);
                        }
                        break;

                    case ConstNpc.SKILL4_PET:
                        Player plsk4 = (Player) PLAYERID_OBJECT.get(player.id);
                        if (select
                                == 0) {
                            if (plsk4.pet == null) {
                                Service.gI().sendThongBao(player, "Player " + plsk4.name + " chưa có đệ");
                            }
                            plsk4.pet.playerSkill.skills.set(3, SkillUtil.createSkill(Skill.BIEN_KHI, 1));
                            Service.gI().sendThongBaoFromAdmin(player, "|7|Đã Mở Skill 4 Biến Khỉ cho Player : " + plsk4.name);
                        }
                        if (select
                                == 1) {
                            if (plsk4.pet == null) {
                                Service.gI().sendThongBao(player, "Player " + plsk4.name + " chưa có đệ");
                            }
                            plsk4.pet.playerSkill.skills.set(3, SkillUtil.createSkill(Skill.KHIEN_NANG_LUONG, 1));
                            Service.gI().sendThongBaoFromAdmin(player, "|7|Đã Mở Skill 4 Khiên Năng Lượng cho Player : " + plsk4.name);
                        }
                        if (select
                                == 2) {
                            if (plsk4.pet == null) {
                                Service.gI().sendThongBao(player, "Player " + plsk4.name + " chưa có đệ");
                            }
                            plsk4.pet.playerSkill.skills.set(3, SkillUtil.createSkill(Skill.DE_TRUNG, 1));
                            Service.gI().sendThongBaoFromAdmin(player, "|7|Đã Mở Skill 4 Đẻ Trứng cho Player : " + plsk4.name);
                        }
                        if (select
                                == 3) {
                            if (plsk4.pet == null) {
                                Service.gI().sendThongBao(player, "Player " + plsk4.name + " chưa có đệ");
                            }
                            plsk4.pet.playerSkill.skills.set(3, SkillUtil.createSkill(Skill.TU_SAT, 1));
                            Service.gI().sendThongBaoFromAdmin(player, "|7|Đã Mở Skill 4 Tự Sát cho Player : " + plsk4.name);
                        }
                        break;
                    case ConstNpc.SKILL5_PET:
                        Player plsk5 = (Player) PLAYERID_OBJECT.get(player.id);
                        if (select
                                == 0) {
                            if (plsk5.pet == null) {
                                Service.gI().sendThongBao(player, "Player " + plsk5.name + " chưa có đệ");
                            }
                            plsk5.pet.playerSkill.skills.set(4, SkillUtil.createSkill(Skill.DICH_CHUYEN_TUC_THOI, 1));
                            Service.gI().sendThongBaoFromAdmin(player, "|7|Đã Mở Skill 5 DCTT cho Player : " + plsk5.name);
                        }
                        if (select
                                == 1) {
                            if (plsk5.pet == null) {
                                Service.gI().sendThongBao(player, "Player " + plsk5.name + " chưa có đệ");
                            }
                            plsk5.pet.playerSkill.skills.set(4, SkillUtil.createSkill(Skill.TROI, 1));
                            Service.gI().sendThongBaoFromAdmin(player, "|7|Đã Mở Skill 5 Trói cho Player : " + plsk5.name);
                        }
                        if (select
                                == 2) {
                            if (plsk5.pet == null) {
                                Service.gI().sendThongBao(player, "Player " + plsk5.name + " chưa có đệ");
                            }
                            plsk5.pet.playerSkill.skills.set(4, SkillUtil.createSkill(Skill.TU_SAT, 1));
                            Service.gI().sendThongBaoFromAdmin(player, "|7|Đã Mở Skill 5 Tự Sát cho Player : " + plsk5.name);
                        }
                        break;

                    case ConstNpc.tindung:
                        switch (select) {
                            case 0:
                                NpcService.gI().createMenuConMeo(player, ConstNpc.tindung, -1,
                                        "|7|Chúc Bạn Một Ngày Vui Vẻ\n"
                                        + "|5|\b" + "\nSố Dư Khả Dụng : " + Util.numberToMoney(player.getSession().vnd) + " Vnđ\n",
                                        "Số Dư", "Điểm boss", "Tích Lũy", "Rút\n KNB", "Phá Sản");
                                break;
                            case 1:
                                NpcService.gI().createMenuConMeo(player, ConstNpc.tindung, -1,
                                        "|7|Chúc Bạn Một Ngày Vui Vẻ\n"
                                        + "|5|\b" + "\nĐiểm Săn Boss Của Bạn : " + Util.numberToMoney(player.point_sb) + " Điểm\n",
                                        "Số Dư", "Điểm boss", "Tích Lũy", "Rút\n KNB", "Phá Sản");
                                break;
                            case 2:
                                NpcService.gI().createMenuConMeo(player, ConstNpc.tindung, -1,
                                        "|7|Chúc Bạn Một Ngày Vui Vẻ\n"
                                        + "|5|\b" + "\nTổng Tích lũy : " + Util.numberToMoney(player.getSession().tongnap) + " tích lũy\n",
                                        "Số Dư", "Điểm boss", "Tích Lũy", "Rút\n KNB", "Phá Sản");
                                break;

                            case 3:

                                Input.gI().createFormQDvnd(player);
                                break;
//                            case 4:
//
//                                Input.gI().createFormQDLUULY(player);
//                                break;
//                            case 5:
//
//                                Input.gI().createFormQDHN1(player);
//                                break;

                        }
                    case ConstNpc.DOI_DTVIP:  // 
                        switch (select) {
                            case 0:  // Mua Pet Bat Giới

                                // Kiểm tra người chơi có đủ tiền không và đã có pet sơ sinh
                                if (player.getSession() != null && player.getSession().vnd >= 100000) {
                                    // Kiểm tra xem player có pet sơ sinh chưa
                                    if (player.pet == null) {
                                        // Nếu chưa có pet sơ sinh, yêu cầu tạo pet sơ sinh
                                        PetService.gI().createNormalPet(player);  // Tạo pet sơ sinh cho người chơi
                                        Service.getInstance().sendThongBao(player, "Bạn Chưa Có Pet Sơ Sinh! Đã Tạo Pet Sơ Sinh Cho Bạn.");
                                    } else {
                                        // Nếu đã có pet, thực hiện mua pet Bat Giới
                                        PetService.gI().changeBatGioiPet(player, player.gender);
                                        PlayerDAO.subvnd(player, 100000);  // Trừ tiền
                                        Service.getInstance().sendMoney(player);  // Cập nhật tiền
                                        Service.getInstance().sendThongBao(player, "Bạn Đã Mua Đệ Vip");
                                    }
                                } else {
                                    // Thông báo nếu không đủ tiền
                                    Service.getInstance().sendThongBao(player, "Bạn Đã Hết Tiền");
                                }
                                break;

                            case 1:  // Mua Pet Hang Nga

                                // Kiểm tra người chơi có đủ tiền không và đã có pet sơ sinh
                                if (player.getSession() != null && player.getSession().vnd >= 200000) {
                                    // Kiểm tra xem player có pet sơ sinh chưa
                                    if (player.pet == null) {
                                        // Nếu chưa có pet sơ sinh, yêu cầu tạo pet sơ sinh
                                        PetService.gI().createNormalPet(player);  // Tạo pet sơ sinh cho người chơi
                                        Service.getInstance().sendThongBao(player, "Bạn Chưa Có Pet Sơ Sinh! Đã Tạo Pet Sơ Sinh Cho Bạn.");
                                    } else {
                                        // Nếu đã có pet, thực hiện mua pet Hang Nga
                                        PetService.gI().changeHangNgaPet(player, player.gender);
                                        PlayerDAO.subvnd(player, 200000);  // Trừ tiền
                                        Service.getInstance().sendMoney(player);  // Cập nhật tiền
                                        Service.getInstance().sendThongBao(player, "Bạn Đã Mua Đệ Vip");
                                    }
                                } else {
                                    // Thông báo nếu không đủ tiền
                                    Service.getInstance().sendThongBao(player, "Bạn Đã Hết Tiền");
                                }
                                break;
                            case 2:  // Mua Pet Goku Ultra

                                // Kiểm tra người chơi có đủ tiền không và đã có pet sơ sinh
                                if (player.getSession() != null && player.getSession().vnd >= 300000) {
                                    // Kiểm tra xem player có pet sơ sinh chưa
                                    if (player.pet == null) {
                                        // Nếu chưa có pet sơ sinh, yêu cầu tạo pet sơ sinh
                                        PetService.gI().createNormalPet(player);  // Tạo pet sơ sinh cho người chơi
                                        Service.getInstance().sendThongBao(player, "Bạn Chưa Có Pet Sơ Sinh! Đã Tạo Pet Sơ Sinh Cho Bạn.");
                                    } else {
                                        // Nếu đã có pet, thực hiện mua pet Goku Ultra
                                        PetService.gI().changeGokuUltraPet(player, player.gender);
                                        PlayerDAO.subvnd(player, 300000);  // Trừ tiền
                                        Service.getInstance().sendMoney(player);  // Cập nhật tiền
                                        Service.getInstance().sendThongBao(player, "Bạn Đã Mua Đệ Vip");
                                    }
                                } else {
                                    // Thông báo nếu không đủ tiền
                                    Service.getInstance().sendThongBao(player, "Bạn Đã Hết Tiền");
                                }
                                break;

                            case 3:  // Mua Pet Vegito

                                // Kiểm tra người chơi có đủ tiền không và đã có pet sơ sinh
                                if (player.getSession() != null && player.getSession().vnd >= 500000) {
                                    // Kiểm tra xem player có pet sơ sinh chưa
                                    if (player.pet == null) {
                                        // Nếu chưa có pet sơ sinh, yêu cầu tạo pet sơ sinh
                                        PetService.gI().createNormalPet(player);  // Tạo pet sơ sinh cho người chơi
                                        Service.getInstance().sendThongBao(player, "Bạn Chưa Có Pet Sơ Sinh! Đã Tạo Pet Sơ Sinh Cho Bạn.");
                                    } else {
                                        // Nếu đã có pet, thực hiện mua pet Vegito
                                        PetService.gI().changeVegitoPet(player, player.gender);
                                        PlayerDAO.subvnd(player, 500000);  // Trừ tiền
                                        Service.getInstance().sendMoney(player);  // Cập nhật tiền
                                        Service.getInstance().sendThongBao(player, "Bạn Đã Mua Đệ Vip");
                                    }
                                } else {
                                    // Thông báo nếu không đủ tiền
                                    Service.getInstance().sendThongBao(player, "Bạn Đã Hết Tiền");
                                }
                                break;
                            case 4:  // Mua Pet Vegito SSJ

                                // Kiểm tra người chơi có đủ tiền không và đã có pet sơ sinh
                                if (player.getSession() != null && player.getSession().vnd >= 1000000) {
                                    // Kiểm tra xem player có pet sơ sinh chưa
                                    if (player.pet == null) {
                                        // Nếu chưa có pet sơ sinh, yêu cầu tạo pet sơ sinh
                                        PetService.gI().createNormalPet(player);  // Tạo pet sơ sinh cho người chơi
                                        Service.getInstance().sendThongBao(player, "Bạn Chưa Có Pet Sơ Sinh! Đã Tạo Pet Sơ Sinh Cho Bạn.");
                                    } else {
                                        // Nếu đã có pet, thực hiện mua pet Vegito SSJ
                                        PetService.gI().changeVegitoSSJPet(player, player.gender);
                                        PlayerDAO.subvnd(player, 1000000);  // Trừ tiền
                                        Service.getInstance().sendMoney(player);  // Cập nhật tiền
                                        Service.getInstance().sendThongBao(player, "Bạn Đã Mua Đệ Vip");
                                    }
                                } else {
                                    // Thông báo nếu không đủ tiền
                                    Service.getInstance().sendThongBao(player, "Bạn Đã Hết Tiền");
                                }
                                break;
                            case 5:  //

                                if (player.getSession().vnd >= 2000000) {
                                    PetService.gI().changeMabuPet(player, player.gender);
                                    PlayerDAO.subvnd(player, 2000000);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn Đã Mua Đệ Vip");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Bạn Đã Hết Tiền");
                                }
                                break;

                        }
                        break;

                    case ConstNpc.CALL_BOSS:
                        switch (select) {
                            case 0:

                                break;
                            case 1:

                                break;
                            case 2:
                                //            BossFactory.createBoss(BossFactory.BROLY);
                                break;
                            case 3:

                                break;
                            case 4:
                                Service.getInstance().sendThongBao(player, "Không có boss");
                                break;
                            case 5:
                                Service.getInstance().sendThongBao(player, "Chua duoc update");
                                break;
                            case 6:

                                break;
                            case 7:
                                Service.getInstance().sendThongBao(player, "Coming sooonn");
                                break;
                            case 8:

                                break;
                            case 9:

                                break;
                            case 10:

                                break;
                        }
                        break;
                    case ConstNpc.CONFIRM_DISSOLUTION_CLAN:
                        switch (select) {
                            case 0 -> {
                                ClanService.gI().RemoveClanAll(player);
                                Service.getInstance().sendThongBao(player, "Đã giải tán bang hội.");
                            }
                        }
                        break;
                    case ConstNpc.CONFIRM_REMOVE_ALL_ITEM_LUCKY_ROUND:
                        if (select
                                == 0) {
                            player.inventory.itemsBoxCrackBall.clear();
                            Service.getInstance().sendThongBao(player, "Đã xóa hết vật phẩm trong rương");
                        }
                        break;
                    case ConstNpc.BAN_NHIEU_THOI_VANG:
                        Item thoivang = InventoryService.gI().findItemBagByTemp(player, 457);
                        switch (select) {

                            case 0:
                                if (player.CapTamkjll < 500000) {
                                    Service.gI().sendThongBaoFromAdmin(player, "Ấn Sử Dụng ! Không Bán Được");
                                    return;
                                }
                                if (player.inventory.gold + 50000000 < player.inventory.getGoldLimit()) {
                                    if (thoivang != null && player.inventory.gold < Inventory.LIMIT_GOLD) {
                                        player.inventory.gold += 50000000;
                                        InventoryService.gI().subQuantityItemsBag(player, thoivang, 1);
                                        InventoryService.gI().sendItemBags(player);
                                        Service.getInstance().sendMoney(player);
                                        Service.getInstance().sendThongBao(player, "|7|Nhận được 50tr vàng");
                                    } else {
                                        Service.getInstance().sendThongBao(player, "|7|Bạn không đủ thỏi vàng để bán");
                                    }
                                } else {
                                    Service.getInstance().sendThongBao(player, "|7|Số vàng vượt quá giới hạn");
                                }
                                break;
                            case 1:
                                if (player.CapTamkjll < 500000) {
                                    Service.gI().sendThongBaoFromAdmin(player, "Ấn Sử Dụng ! Không Bán Được");
                                    return;
                                }
                                if (player.inventory.gold + 500000000L < player.inventory.getGoldLimit()) {
                                    if (thoivang != null && thoivang.quantity >= 10) {
                                        player.inventory.gold += 500000000L;
                                        InventoryService.gI().subQuantityItemsBag(player, thoivang, 10);
                                        InventoryService.gI().sendItemBags(player);
                                        Service.getInstance().sendMoney(player);
                                        Service.getInstance().sendThongBao(player, "|7|Nhận được 500M vàng");
                                    } else {
                                        Service.getInstance().sendThongBao(player, "|7|Bạn không đủ thỏi vàng để bán");
                                    }
                                } else {
                                    Service.getInstance().sendThongBao(player, "|7|Số vàng vượt quá giới hạn");
                                }
                                break;
                            case 2:
                                if (player.CapTamkjll < 500000) {
                                    Service.gI().sendThongBaoFromAdmin(player, "Ấn Sử Dụng ! Không Bán Được");
                                    return;
                                }
                                if (player.inventory.gold + 5000000000L < player.inventory.getGoldLimit()) {
                                    if (thoivang != null && thoivang.quantity >= 100) {
                                        player.inventory.gold += 5000000000L;
                                        InventoryService.gI().subQuantityItemsBag(player, thoivang, 100);
                                        InventoryService.gI().sendItemBags(player);
                                        Service.getInstance().sendMoney(player);
                                        Service.getInstance().sendThongBao(player, "|7|Nhận được 5 tỉ vàng");
                                    } else {
                                        Service.getInstance().sendThongBao(player, "|7|Bạn không đủ thỏi vàng để bán");
                                    }
                                } else {
                                    Service.getInstance().sendThongBao(player, "|7|Số vàng vượt quá giới hạn");
                                }
                                break;

                        }
                        break;
                    case ConstNpc.MENU_FIND_PLAYER:
                        Player p = (Player) PLAYERID_OBJECT.get(player.id);
                        if (p
                                != null) {
                            switch (select) {
                                case 0:
                                    if (p.zone != null) {
                                        ChangeMapService.gI().changeMapYardrat(player, p.zone, p.location.x,
                                                p.location.y);
                                    }
                                    break;
                                case 1:
                                    if (p.zone != null) {
                                        ChangeMapService.gI().changeMap(p, player.zone, player.location.x,
                                                player.location.y);
                                    }
                                    break;
                                case 2:
                                    if (p != null) {
                                        Input.gI().createFormChangeName(player, p);
                                    }
                                    break;
                                case 3:
                                    if (p != null) {
                                        String[] selects = new String[]{"Đồng ý", "Hủy"};
                                        NpcService.gI().createMenuConMeo(player, ConstNpc.BAN_PLAYER, -1,
                                                "Bạn có chắc chắn muốn ban " + p.name, selects, p);
                                    }
                                    break;
                            }
                        }

                        break;
                }
            }
        };
    }

    public static void openMenuSuKien(Player player, Npc npc, int tempId, int select) {
        switch (Manager.EVENT_SEVER) {
            case 0:
                break;
            case 1:// hlw
                switch (select) {
                    case 0:
                        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
                            Item keo = InventoryService.gI().finditemnguyenlieuKeo(player);
                            Item banh = InventoryService.gI().finditemnguyenlieuBanh(player);
                            Item bingo = InventoryService.gI().finditemnguyenlieuBingo(player);

                            if (keo != null && banh != null && bingo != null) {
                                Item GioBingo = ItemService.gI().createNewItem((short) 2016, 1);

                                // - Số item sự kiện có trong rương
                                InventoryService.gI().subQuantityItemsBag(player, keo, 10);
                                InventoryService.gI().subQuantityItemsBag(player, banh, 10);
                                InventoryService.gI().subQuantityItemsBag(player, bingo, 10);

                                GioBingo.itemOptions.add(new ItemOption(74, 0));
                                InventoryService.gI().addItemBag(player, GioBingo, 0);
                                InventoryService.gI().sendItemBags(player);
                                Service.getInstance().sendThongBao(player, "Đổi quà sự kiện thành công");
                            } else {
                                Service.getInstance().sendThongBao(player,
                                        "Vui lòng chuẩn bị x10 Nguyên Liệu Kẹo, Bánh Quy, Bí Ngô Để Đổi vật phẩm sự kiện");
                            }
                        } else {
                            Service.getInstance().sendThongBao(player, "Hành trang đầy.");
                        }
                        break;
                    case 1:
                        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
                            Item ve = InventoryService.gI().finditemnguyenlieuVe(player);
                            Item giokeo = InventoryService.gI().finditemnguyenlieuGiokeo(player);

                            if (ve != null && giokeo != null) {
                                Item Hopmaquy = ItemService.gI().createNewItem((short) 2017, 1);
                                // - Số item sự kiện có trong rương
                                InventoryService.gI().subQuantityItemsBag(player, ve, 3);
                                InventoryService.gI().subQuantityItemsBag(player, giokeo, 3);

                                Hopmaquy.itemOptions.add(new ItemOption(74, 0));
                                InventoryService.gI().addItemBag(player, Hopmaquy, 0);
                                InventoryService.gI().sendItemBags(player);
                                Service.getInstance().sendThongBao(player, "Đổi quà sự kiện thành công");
                            } else {
                                Service.getInstance().sendThongBao(player,
                                        "Vui lòng chuẩn bị x3 Vé đổi Kẹo và x3 Giỏ kẹo Để Đổi vật phẩm sự kiện");
                            }
                        } else {
                            Service.getInstance().sendThongBao(player, "Hành trang đầy.");
                        }
                        break;
                    case 2:
                        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
                            Item ve = InventoryService.gI().finditemnguyenlieuVe(player);
                            Item giokeo = InventoryService.gI().finditemnguyenlieuGiokeo(player);
                            Item hopmaquy = InventoryService.gI().finditemnguyenlieuHopmaquy(player);

                            if (ve != null && giokeo != null && hopmaquy != null) {
                                Item HopQuaHLW = ItemService.gI().createNewItem((short) 2012, 1);
                                // - Số item sự kiện có trong rương
                                InventoryService.gI().subQuantityItemsBag(player, ve, 3);
                                InventoryService.gI().subQuantityItemsBag(player, giokeo, 3);
                                InventoryService.gI().subQuantityItemsBag(player, hopmaquy, 3);

                                HopQuaHLW.itemOptions.add(new ItemOption(74, 0));
                                HopQuaHLW.itemOptions.add(new ItemOption(30, 0));
                                InventoryService.gI().addItemBag(player, HopQuaHLW, 0);
                                InventoryService.gI().sendItemBags(player);
                                Service.getInstance().sendThongBao(player,
                                        "Đổi quà hộp quà sự kiện Halloween thành công");
                            } else {
                                Service.getInstance().sendThongBao(player,
                                        "Vui lòng chuẩn bị x3 Hộp Ma Quỷ, x3 Vé đổi Kẹo và x3 Giỏ kẹo Để Đổi vật phẩm sự kiện");
                            }
                        } else {
                            Service.getInstance().sendThongBao(player, "Hành trang đầy.");
                        }
                        break;
                }
                break;
            case 2:// 20/11
                switch (select) {
                    case 3:
                        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
                            int evPoint = player.event.getEventPoint();
                            if (evPoint >= 999) {
                                Item HopQua = ItemService.gI().createNewItem((short) 2021, 1);
                                player.event.setEventPoint(evPoint - 999);

                                HopQua.itemOptions.add(new ItemOption(74, 0));
                                HopQua.itemOptions.add(new ItemOption(30, 0));
                                InventoryService.gI().addItemBag(player, HopQua, 0);
                                InventoryService.gI().sendItemBags(player);
                                Service.getInstance().sendThongBao(player, "Bạn nhận được Hộp Quà Teacher Day");
                            } else {
                                Service.getInstance().sendThongBao(player, "Cần 999 điểm tích lũy Để Đổi");
                            }
                        } else {
                            Service.getInstance().sendThongBao(player, "Hành trang đầy.");
                        }
                        break;
                    default:
                        int n = 0;
                        switch (select) {
                            case 0:
                                n = 1;
                                break;
                            case 1:
                                n = 10;
                                break;
                            case 2:
                                n = 99;
                                break;
                        }

                        if (n > 0) {
                            Item bonghoa = InventoryService.gI().finditemBongHoa(player, n);
                            if (bonghoa != null) {
                                int evPoint = player.event.getEventPoint();
                                player.event.setEventPoint(evPoint + n);
                                ;
                                InventoryService.gI().subQuantityItemsBag(player, bonghoa, n);
                                Service.getInstance().sendThongBao(player, "Bạn nhận được " + n + " điểm sự kiện");
                                int pre;
                                int next;
                                String text = null;
                                AttributeManager am = ServerManager.gI().getAttributeManager();
                                switch (tempId) {
                                    case ConstNpc.THAN_MEO_KARIN:
                                        pre = EVENT_COUNT_THAN_MEO / 999;
                                        EVENT_COUNT_THAN_MEO += n;
                                        next = EVENT_COUNT_THAN_MEO / 999;
                                        if (pre != next) {
                                            am.setTime(ConstAttribute.TNSM, 3600);
                                            text = "Toàn bộ máy chủ tăng được 20% TNSM cho đệ tử khi đánh quái trong 60 phút.";
                                        }
                                        break;

                                    case ConstNpc.QUY_LAO_KAME:
                                        pre = EVENT_COUNT_QUY_LAO_KAME / 999;
                                        EVENT_COUNT_QUY_LAO_KAME += n;
                                        next = EVENT_COUNT_QUY_LAO_KAME / 999;
                                        if (pre != next) {
                                            am.setTime(ConstAttribute.VANG, 3600);
                                            text = "Toàn bộ máy chủ được tăng 100% vàng từ quái trong 60 phút.";
                                        }
                                        break;

                                    case ConstNpc.THUONG_DE:
                                        pre = EVENT_COUNT_THUONG_DE / 999;
                                        EVENT_COUNT_THUONG_DE += n;
                                        next = EVENT_COUNT_THUONG_DE / 999;
                                        if (pre != next) {
                                            am.setTime(ConstAttribute.KI, 3600);
                                            text = "Toàn bộ máy chủ được tăng 20% KI trong 60 phút.";
                                        }
                                        break;
                                    case ConstNpc.THAN_VU_TRU:
                                        pre = EVENT_COUNT_THAN_VU_TRU / 999;
                                        EVENT_COUNT_THAN_VU_TRU += n;
                                        next = EVENT_COUNT_THAN_VU_TRU / 999;
                                        if (pre != next) {
                                            am.setTime(ConstAttribute.HP, 3600);
                                            text = "Toàn bộ máy chủ được tăng 20% HP trong 60 phút.";
                                        }
                                        break;

                                    case ConstNpc.BILL:
                                        pre = EVENT_COUNT_THAN_HUY_DIET / 999;
                                        EVENT_COUNT_THAN_HUY_DIET += n;
                                        next = EVENT_COUNT_THAN_HUY_DIET / 999;
                                        if (pre != next) {
                                            am.setTime(ConstAttribute.SUC_DANH, 3600);
                                            text = "Toàn bộ máy chủ được tăng 20% Sức đánh trong 60 phút.";
                                        }
                                        break;
                                }
                                if (text != null) {
                                    Service.getInstance().sendThongBaoAllPlayer(text);
                                }

                            } else {
                                Service.getInstance().sendThongBao(player,
                                        "Cần ít nhất " + n + " bông hoa để có thể tặng");
                            }
                        } else {
                            Service.getInstance().sendThongBao(player, "Cần ít nhất " + n + " bông hoa để có thể tặng");
                        }
                }
                break;
            case 3:
                if (InventoryService.gI().getCountEmptyBag(player) > 0) {
                    Item keogiangsinh = InventoryService.gI().finditemKeoGiangSinh(player);

                    if (keogiangsinh != null && keogiangsinh.quantity >= 99) {
                        Item tatgiangsinh = ItemService.gI().createNewItem((short) 649, 1);
                        // - Số item sự kiện có trong rương
                        InventoryService.gI().subQuantityItemsBag(player, keogiangsinh, 99);

                        tatgiangsinh.itemOptions.add(new ItemOption(74, 0));
                        tatgiangsinh.itemOptions.add(new ItemOption(30, 0));
                        InventoryService.gI().addItemBag(player, tatgiangsinh, 0);
                        InventoryService.gI().sendItemBags(player);
                        Service.getInstance().sendThongBao(player, "Bạn nhận được Tất,vớ giáng sinh");
                    } else {
                        Service.getInstance().sendThongBao(player,
                                "Vui lòng chuẩn bị x99 kẹo giáng sinh Để Đổi vớ tất giáng sinh");
                    }
                } else {
                    Service.getInstance().sendThongBao(player, "Hành trang đầy.");
                }
                break;
            case 4:
                switch (select) {
                    case 0:
                        if (!player.event.isReceivedLuckyMoney()) {
                            Calendar cal = Calendar.getInstance();
                            int day = cal.get(Calendar.DAY_OF_MONTH);
                            if (day >= 22 && day <= 24) {
                                Item goldBar = ItemService.gI().createNewItem((short) ConstItem.THOI_VANG,
                                        Util.nextInt(1, 3));
                                player.inventory.ruby += Util.nextInt(10, 30);
                                goldBar.quantity = Util.nextInt(1, 3);
                                InventoryService.gI().addItemBag(player, goldBar, 99999);
                                InventoryService.gI().sendItemBags(player);
                                PlayerService.gI().sendInfoHpMpMoney(player);
                                player.event.setReceivedLuckyMoney(true);
                                Service.getInstance().sendThongBao(player,
                                        "Nhận lì xì thành công,chúc bạn năm mới dui dẻ");
                            } else if (day > 24) {
                                Service.getInstance().sendThongBao(player, "Hết tết rồi còn đòi lì xì");
                            } else {
                                Service.getInstance().sendThongBao(player, "Đã tết đâu mà đòi lì xì");
                            }
                        } else {
                            Service.getInstance().sendThongBao(player, "Bạn đã nhận lì xì rồi");
                        }
                        break;
                    case 1:
//                        ShopService.gI().openShopNormal(player, npc, ConstNpc.SHOP_SU_KIEN_TET, 1, -1);
                        break;
                }
                break;
            case ConstEvent.SU_KIEN_8_3:
                switch (select) {
                    case 3:
                        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
                            int evPoint = player.event.getEventPoint();
                            if (evPoint >= 999) {
                                Item capsule = ItemService.gI().createNewItem((short) 2052, 1);
                                player.event.setEventPoint(evPoint - 999);

                                capsule.itemOptions.add(new ItemOption(74, 0));
                                capsule.itemOptions.add(new ItemOption(30, 0));
                                InventoryService.gI().addItemBag(player, capsule, 0);
                                InventoryService.gI().sendItemBags(player);
                                Service.getInstance().sendThongBao(player, "Bạn nhận được Capsule Hồng");
                            } else {
                                Service.getInstance().sendThongBao(player, "Cần 999 điểm tích lũy Để Đổi");
                            }
                        } else {
                            Service.getInstance().sendThongBao(player, "Hành trang đầy.");
                        }
                        break;
                    default:
                        int n = 0;
                        switch (select) {
                            case 0:
                                n = 1;
                                break;
                            case 1:
                                n = 10;
                                break;
                            case 2:
                                n = 99;
                                break;
                        }

                        if (n > 0) {
                            Item bonghoa = InventoryService.gI().finditemBongHoa(player, n);
                            if (bonghoa != null) {
                                int evPoint = player.event.getEventPoint();
                                player.event.setEventPoint(evPoint + n);
                                InventoryService.gI().subQuantityItemsBag(player, bonghoa, n);
                                Service.getInstance().sendThongBao(player, "Bạn nhận được " + n + " điểm sự kiện");
                            } else {
                                Service.getInstance().sendThongBao(player,
                                        "Cần ít nhất " + n + " bông hoa để có thể tặng");
                            }
                        } else {
                            Service.getInstance().sendThongBao(player, "Cần ít nhất " + n + " bông hoa để có thể tặng");
                        }
                }
                break;
        }
    }

    public static String getMenuSuKien(int id) {
        switch (id) {
            case ConstEvent.KHONG_CO_SU_KIEN:
                return "Chưa có\n Sự Kiện";
            case ConstEvent.SU_KIEN_HALLOWEEN:
                return "Sự Kiện\nHaloween";
            case ConstEvent.SU_KIEN_20_11:
                return "Sự Kiện\n 20/11";
            case ConstEvent.SU_KIEN_NOEL:
                return "Sự Kiện\n Giáng Sinh";
            case ConstEvent.SU_KIEN_TET:
                return "Sự Kiện\n Tết Nguyên\nĐán 2023";
            case ConstEvent.SU_KIEN_8_3:
                return "Sự Kiện\n 8/3";
        }
        return "Chưa có\n Sự Kiện";
    }

}
