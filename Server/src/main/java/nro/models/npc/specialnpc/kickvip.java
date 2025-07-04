/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nro.models.npc.specialnpc;

/**
 *
 * @author delb1
 */
import nro.models.item.Item;
import nro.models.item.ItemOption;
import nro.models.player.Player;
import nro.services.InventoryService;
import nro.services.ItemService;
import nro.services.Service;

public class kickvip {

    public static final long goi_thang = 86400000L * 30;
    public static final long goi_tuan = 86400000L * 7;
    public static final long goi_30s = 30000;
    private Player player;
    public long lastTimeCreate;
    public long timeDone;

    public kickvip(Player player, long lastTimeCreate, long timeDone) {
        this.player = player;
        this.lastTimeCreate = lastTimeCreate;
        this.timeDone = timeDone;
    }

    public static void taothethang(Player player, long goithang) {
        player.kickvip = new kickvip(player, System.currentTimeMillis(), goithang);
    }

    public static void rewardday(Player pl) {
        if (pl.diemdanh == 0) {
            if (pl.pointidemdanh >= 0 && pl.pointidemdanh < 16) {
                if (InventoryService.gI().getCountEmptyBag(pl) > 2) {
                    pl.pointidemdanh += 1;
                    pl.diemdanh = System.currentTimeMillis();
                    Service.getInstance().sendThongBao(pl, "Điểm danh thành công");
                    if (pl.pointidemdanh >= 0 && pl.pointidemdanh <= 6) {
                        InventoryService.gI().addItemBag(pl, ItemService.gI().createNewItem((short) 457, 125), 99999999);
                    } else if (pl.pointidemdanh == 7) {
                        Item caitrang = ItemService.gI().createNewItem((short) 2123, 1);
                        caitrang.itemOptions.add(new ItemOption(50, 18));
                        caitrang.itemOptions.add(new ItemOption(77, 18));
                        caitrang.itemOptions.add(new ItemOption(103, 18));
                        caitrang.itemOptions.add(new ItemOption(5, 5));
                        caitrang.itemOptions.add(new ItemOption(93, 7));
                        InventoryService.gI().addItemBag(pl, caitrang, 99999999);
                    } else if (pl.pointidemdanh > 7 && pl.pointidemdanh < 15) {
                        InventoryService.gI().addItemBag(pl, ItemService.gI().createNewItem((short) 457, 275), 99999999);
                    } else if (pl.pointidemdanh == 15) {
                        Item caitrang = ItemService.gI().createNewItem((short) 2123, 1);
                        caitrang.itemOptions.add(new ItemOption(50, 18));
                        caitrang.itemOptions.add(new ItemOption(77, 18));
                        caitrang.itemOptions.add(new ItemOption(103, 18));
                        caitrang.itemOptions.add(new ItemOption(5, 5));
                        InventoryService.gI().addItemBag(pl, caitrang, 9999999);
                    }
                    InventoryService.gI().sendItemBags(pl);
                } else {
                    Service.getInstance().sendThongBao(pl, "Vui lòng để trống 3 ô hành trang");
                }
            } else {
                Service.getInstance().sendThongBao(pl, "Hết lượt nhận rồi");
            }
        } else {
            Service.getInstance().sendThongBao(pl, "Hôm nay đã điểm danh rồi");
        }
    }

    public static void mocnap(Player player) {
        if (player != null) {
            if (InventoryService.gI().getCountEmptyBag(player) > 5) {
                if (player.getSession().tongnap >= 500_000 && player.mocnap1 == false) {
                    InventoryService.gI().addItemBag(player, ItemService.gI().createNewItem((short) 568, 1), 99999999);
                    Service.getInstance().sendThongBao(player, "Nhận thành công mốc 1");
                    player.mocnap1 = true;
                } else if (player.getSession().tongnap >= 1_000_000 && player.mocnap2 == false) {
                    InventoryService.gI().addItemBag(player, ItemService.gI().createNewItem((short) 220, 999), 99999999);
                    InventoryService.gI().addItemBag(player, ItemService.gI().createNewItem((short) 221, 999), 99999999);
                    InventoryService.gI().addItemBag(player, ItemService.gI().createNewItem((short) 222, 999), 99999999);
                    InventoryService.gI().addItemBag(player, ItemService.gI().createNewItem((short) 223, 999), 99999999);
                    InventoryService.gI().addItemBag(player, ItemService.gI().createNewItem((short) 224, 999), 99999999);
                    InventoryService.gI().addItemBag(player, ItemService.gI().createNewItem((short) 987, 200), 99999999);
                    Service.getInstance().sendThongBao(player, "Nhận thành công mốc 2");
                    player.mocnap2 = true;
                } else if (player.getSession().tongnap >= 3_000_000 && player.mocnap3 == false) {
                    Item pgg = ItemService.gI().createNewItem((short) 459, 1);
                    pgg.itemOptions.add(new ItemOption(239, 50));
                    InventoryService.gI().addItemBag(player, pgg, 99999999);
                    InventoryService.gI().addItemBag(player, ItemService.gI().createNewItem((short) 14, 1), 99999999);
                    InventoryService.gI().addItemBag(player, ItemService.gI().createNewItem((short) 15, 1), 99999999);
                    InventoryService.gI().addItemBag(player, ItemService.gI().createNewItem((short) 16, 1), 99999999);
                    InventoryService.gI().addItemBag(player, ItemService.gI().createNewItem((short) 17, 1), 99999999);
                    InventoryService.gI().addItemBag(player, ItemService.gI().createNewItem((short) 18, 1), 99999999);
                    InventoryService.gI().addItemBag(player, ItemService.gI().createNewItem((short) 19, 1), 99999999);
                    InventoryService.gI().addItemBag(player, ItemService.gI().createNewItem((short) 20, 1), 99999999);
                    Service.getInstance().sendThongBao(player, "Nhận thành công mốc 3");
                    player.mocnap3 = true;
                } else if (player.getSession().tongnap >= 5_000_000 && player.mocnap4 == false) {
                    Item caitrang = ItemService.gI().createNewItem((short) 884, 1);
                    caitrang.itemOptions.add(new ItemOption(50, 25));
                    caitrang.itemOptions.add(new ItemOption(77, 25));
                    caitrang.itemOptions.add(new ItemOption(103, 25));
                    caitrang.itemOptions.add(new ItemOption(5, 30));
                    caitrang.itemOptions.add(new ItemOption(14, 100));
                    InventoryService.gI().addItemBag(player, caitrang, 99999999);
                    Service.getInstance().sendThongBao(player, "Nhận thành công mốc 4");
                    player.mocnap4 = true;
                } else if (player.mocnap1 == true && player.mocnap2 == true && player.mocnap3 == true && player.mocnap4 == true) {
                    Service.getInstance().sendThongBao(player, "Bạn đã nhận hết tất cả mốc nạp rồi");
                } else {
                    Service.getInstance().sendThongBao(player, "Bạn không đủ điều kiện để nhận");
                }
                InventoryService.gI().sendItemBags(player);
            } else {
                Service.getInstance().sendThongBao(player, "Vui lòng trừa 5 ô hành trang trống");
            }

        }

    }

    public String getTimeLeft() {
        long timeLeftInMillis = lastTimeCreate + timeDone - System.currentTimeMillis();
        if (timeLeftInMillis <= 0) {
            hethsd();
            return "Gói của bạn đã hết hạn";
        }
        long seconds = timeLeftInMillis / 1000;
        long days = seconds / (24 * 3600);
        seconds = seconds % (24 * 3600);
        long hours = seconds / 3600;
        seconds %= 3600;
        long minutes = seconds / 60;
        seconds %= 60;
        return String.format("%d ngày, %d giờ, %d phút và %d giây", days, hours, minutes, seconds);
    }

    public void hethsd() {
        player.kickvip = null;
        this.player.kickvip = null;

    }

    public void subTimeDone(int d, int h, int m, int s) {
        this.timeDone -= ((d * 24 * 60 * 60 * 1000) + (h * 60 * 60 * 1000) + (m * 60 * 1000) + (s * 1000));

    }

    public void dispose() {
        this.player = null;
    }
}
