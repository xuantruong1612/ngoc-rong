package nro.services.func;

import nro.consts.Cmd;
import nro.consts.ConstNpc;
import nro.consts.ConstPlayer;
import nro.models.item.Item;
import nro.models.item.ItemOption;
import nro.models.map.Zone;
import nro.models.player.Player;
import nro.server.io.Message;
import nro.services.*;
import nro.utils.Log;
import nro.utils.Util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @Build Arriety
 *
 */
public class SummonDragon {

    public static final byte WISHED = 0;
    public static final byte TIME_UP = 1;

    public static final byte DRAGON_SHENRON = 0;
    public static final byte DRAGON_PORUNGA = 1;
    public static final byte DRAGON_BLACK_SHENRON = 2;
    public static final byte DRAGON_ICE_SHENRON = 3;

    public static final short NGOC_RONG_1_SAO = 14;
    public static final short NGOC_RONG_2_SAO = 15;
    public static final short NGOC_RONG_3_SAO = 16;
    public static final short NGOC_RONG_4_SAO = 17;
    public static final short NGOC_RONG_5_SAO = 18;
    public static final short NGOC_RONG_6_SAO = 19;
    public static final short NGOC_RONG_7_SAO = 20;

    public static final short[] NGOC_RONG_HALLOWEEN = {702, 703, 704, 705, 706, 707, 708};
    public static final short[] NGOC_RONG_DEN = {807, 808, 809, 810, 811, 812, 813};
    public static final short[] NGOC_RONG_BANG = {925, 926, 927, 928, 929, 930, 931};
    public static final short[] NGOC_RONG_NAMEK = {353, 354, 355, 356, 357, 358, 359};
    public static final short NGOC_RONG_SIEU_CAP = 981;
    public static final String SUMMON_SHENRON_TUTORIAL
            = "Có 3 cách gọi rồng thần. Gọi từ ngọc 1 sao, gọi từ ngọc 2 sao, hoặc gọi từ ngọc 3 sao\n"
            + "Các ngọc 4 sao đến 7 sao không thể gọi rồng thần được\n"
            + "Để gọi rồng 1 sao cần ngọc từ 1 sao đến 7 sao\n"
            + "Để gọi rồng 2 sao cần ngọc từ 2 sao đến 7 sao\n"
            + "Để gọi rồng 3 sao cần ngọc từ 3 sao đến 7sao\n"
            + "Điều ước rồng 3 sao: Capsule 3 sao, hoặc 2 triệu sức mạnh, hoặc 200k vàng\n"
            + "Điều ước rồng 2 sao: Capsule 2 sao, hoặc 20 triệu sức mạnh, hoặc 2 triệu vàng\n"
            + "Điều ước rồng 1 sao: Capsule 1 sao, hoặc 200 triệu sức mạnh, hoặc 20 triệu vàng, hoặc đẹp trai, hoặc....\n"
            + "Ngọc rồng sẽ mất ngay khi gọi rồng dù bạn có ước hay không\n"
            + "Quá 5 phút nếu không ước rồng thần sẽ bay mất";
    public static final String SHENRON_SAY
            = "Ta sẽ ban cho người 1 điều ước, ngươi có 5 phút, hãy suy nghĩ thật kỹ trước khi quyết định";

    public static final String[] SHENRON_1_STAR_WISHES_1
            = new String[]{"Giàu có\n+2 Tỏi\nVàng", "Găng tay\nđang mang\nlên 1 cấp", "Chí mạng\nGốc +2%",
                "Thay\nChiêu 2-3\nĐệ tử", "Điều ước\nkhác"};
    public static final String[] SHENRON_1_STAR_WISHES_2
            = new String[]{"Đẹp trai\nnhất\nVũ trụ", "Giàu có\n+50\nNgọc hồng", "+200 Tr\nSức mạnh\nvà tiềm\nnăng",
                "Găng tay đệ\nđang mang\nlên 1 cấp",
                "Điều ước\nkhác"};
    public static final String[] SHENRON_2_STARS_WHISHES
            = new String[]{"Giàu có\n+10\nNgọc hồng", "+20 Tr\nSức mạnh\nvà tiềm năng", "Giàu có\n+200 Tr\nVàng"};
    public static final String[] SHENRON_3_STARS_WHISHES
            = new String[]{"Giàu có\n+5\nNgọc hồng", "+2 Tr\nSức mạnh\nvà tiềm năng", "Giàu có\n+20 Tr\nVàng"};
    //--------------------------------------------------------------------------
    public static final String BLACK_SHENRON_SAY
           = "Tao sẽ ban cho người 1 điều ước, ngươi có 5 phút, hãy suy nghĩ thật kỹ trước khi quyết định"
           + "\n|7|"+"Ký Tên Ngọc Rồng online 2D\n";
        

    public static final String[] BLACK_SHENRON_WISHES
            = new String[]{"giàu có","Sách SD","Sách HP"
            ,"Sách KI","+20 cấp\nNhập Ma","+20.000 \nLưu Ly"};
    //-----------------------------------------------------------------------------
    public static final String ICE_SHENRON_SAY
            = "Tao sẽ ban cho người 1 điều ước, ngươi có 5 phút, hãy suy nghĩ thật kỹ trước khi quyết định"
           + "\n|7|"+"Ký Tên Ngọc Rồng online 2D\n";
        ;

    public static final String[] ICE_SHENRON_WISHES
            = new String[]{"Tăng 10k\nSD gốc", "Tăng 50K\n HP gốc", 
                "Tăng 500\n Giáp gốc","Tăng 50K\n Ki gốc","Chí Mạng\n2%"};

    private static SummonDragon instance;
    private final Map pl_dragonStar;
    private long lastTimeShenronAppeared;
    private long lastTimeShenronWait;
    private final int timeResummonShenron = 300000;
    //    private final int timeResummonShenron = 0;
    private boolean isShenronAppear;
    public boolean isBlackShenronAppear;
    public boolean isIcecShenronAppear;
    private final int timeShenronWait = 300000;

    private final Thread update;
    private boolean active;

    public boolean isPlayerDisconnect;
    public Player playerSummonShenron;
    private int playerSummonShenronId;
    private Zone mapShenronAppear;
    private byte shenronStar;
    private int menuShenron;
    private byte select;

    private SummonDragon() {
        this.pl_dragonStar = new HashMap<>();
        this.update = new Thread(() -> {
            while (active) {
                try {
                    if (isShenronAppear) {
                        if (isPlayerDisconnect) {

                            List<Player> players = mapShenronAppear.getPlayers();
                            synchronized (players) {
                                for (Player plMap : players) {
                                    if (plMap.id == playerSummonShenronId) {
                                        playerSummonShenron = plMap;
                                        reSummonShenron(DRAGON_SHENRON);
                                        isPlayerDisconnect = false;
                                        break;
                                    }
                                }
                            }

                        }
                        if (Util.canDoWithTime(lastTimeShenronWait, timeShenronWait)) {
                            shenronLeave(playerSummonShenron, TIME_UP);
                        }
                    } else if (isBlackShenronAppear) {
                        if (isPlayerDisconnect) {

                            List<Player> players = mapShenronAppear.getPlayers();
                            synchronized (players) {
                                for (Player plMap : players) {
                                    if (plMap.id == playerSummonShenronId) {
                                        playerSummonShenron = plMap;
                                        reSummonShenron(DRAGON_BLACK_SHENRON);
                                        isPlayerDisconnect = false;
                                        break;
                                    }
                                }
                            }

                        }
                        if (Util.canDoWithTime(lastTimeShenronWait, timeShenronWait)) {
                            shenronLeave(playerSummonShenron, TIME_UP);
                        }
                    } else if (isIcecShenronAppear) {
                        if (isPlayerDisconnect) {

                            List<Player> players = mapShenronAppear.getPlayers();
                            synchronized (players) {
                                for (Player plMap : players) {
                                    if (plMap.id == playerSummonShenronId) {
                                        playerSummonShenron = plMap;
                                        reSummonShenron(DRAGON_ICE_SHENRON);
                                        isPlayerDisconnect = false;
                                        break;
                                    }
                                }
                            }

                        }
                        if (Util.canDoWithTime(lastTimeShenronWait, timeShenronWait)) {
                            shenronLeave(playerSummonShenron, TIME_UP);
                        }
                    }
                    Thread.sleep(1000);
                } catch (Exception e) {
                    Log.error(SummonDragon.class, e);
                }
            }
        });
        this.active();
    }

    private void active() {
        if (!active) {
            active = true;
            this.update.start();
        }
    }

    public static SummonDragon gI() {
        if (instance == null) {
            instance = new SummonDragon();
        }
        return instance;
    }

    public void openMenuSummonShenron(Player pl, byte dragonBallStar, byte Dragon) {
        switch (Dragon) {
            case DRAGON_SHENRON:
                this.pl_dragonStar.put(pl, dragonBallStar);
                NpcService.gI().createMenuConMeo(pl, ConstNpc.SUMMON_SHENRON, -1, "Bạn muốn gọi rồng thần ?",
                        "Hướng\ndẫn thêm\n(mới)", "Gọi\nRồng Thần\n" + dragonBallStar + " Sao");
                break;
            case DRAGON_BLACK_SHENRON:
                this.pl_dragonStar.put(pl, dragonBallStar);
                NpcService.gI().createMenuConMeo(pl, ConstNpc.SUMMON_BLACK_SHENRON, -1, "Bạn muốn gọi Rồng Siêu Cấp ?",
                        "Đồng ý", "Chê");
                break;
            case DRAGON_ICE_SHENRON:
                this.pl_dragonStar.put(pl, dragonBallStar);
                NpcService.gI().createMenuConMeo(pl, ConstNpc.SUMMON_ICE_SHENRON, -1, "Bạn muốn gọi Rồng Băng ?",
                        "Đồng ý", "Chê");
                break;
        }

    }

    public void summonShenron(Player pl) {
        if (pl.zone.map.mapId == 0 || pl.zone.map.mapId == 7 || pl.zone.map.mapId == 14) {
            if (checkShenronBall(pl, DRAGON_SHENRON)) {
                if (isShenronAppear) {
                    Service.getInstance().sendThongBao(pl, "Không thể thực hiện");
                    return;
                }

                if (Util.canDoWithTime(lastTimeShenronAppeared, timeResummonShenron)) {
                    //gọi rồng
                    playerSummonShenron = pl;
                    playerSummonShenronId = (int) pl.id;
                    mapShenronAppear = pl.zone;
                    byte dragonStar = (byte) pl_dragonStar.get(playerSummonShenron);
                    int begin = NGOC_RONG_1_SAO;
                    switch (dragonStar) {
                        case 2:
                            begin = NGOC_RONG_2_SAO;
                            break;
                        case 3:
                            begin = NGOC_RONG_3_SAO;
                            break;
                    }
                    for (int i = begin; i <= NGOC_RONG_7_SAO; i++) {
                        InventoryService.gI().subQuantityItemsBag(pl, InventoryService.gI().findItemBagByTemp(pl, i), 1);
                    }
                    InventoryService.gI().sendItemBags(pl);
                    sendNotifyShenronAppear(DRAGON_SHENRON);
                    activeShenron(pl, true);
                    sendWhishesShenron(pl);
                } else {
                    int timeLeft = (int) ((timeResummonShenron - (System.currentTimeMillis() - lastTimeShenronAppeared)) / 1000);
                    Service.getInstance().sendThongBao(pl, "Vui lòng đợi " + (timeLeft < 60 ? timeLeft + " giây" : timeLeft / 60 + " phút") + " nữa");
                }
            }
        } else {
            Service.getInstance().sendThongBao(pl, "Chỉ được gọi rồng thần ở ngôi làng trước nhà");
        }
    }

    public void summonBlackShenron(Player pl) {
        if (pl.zone.map.mapId == 5) {
            if (checkShenronBall(pl, DRAGON_BLACK_SHENRON)) {
                if (isBlackShenronAppear) {
                    Service.getInstance().sendThongBao(pl, "Không thể thực hiện");
                    return;
                }
                if (Util.canDoWithTime(lastTimeShenronAppeared, timeResummonShenron)) {
                    playerSummonShenron = pl;
                    playerSummonShenronId = (int) pl.id;
                    mapShenronAppear = pl.zone;
                    InventoryService.gI().subQuantityItemsBag(pl, InventoryService.gI().findItemBagByTemp(pl, NGOC_RONG_SIEU_CAP), 1);
                    InventoryService.gI().sendItemBags(pl);
                    sendNotifyShenronAppear(DRAGON_BLACK_SHENRON);
                    isBlackShenronAppear = true;
                    activeDragonNew(pl, true, (byte) 60);
                    sendWhishesShenron(pl);
                } else {
                    int timeLeft = (int) ((timeResummonShenron - (System.currentTimeMillis() - lastTimeShenronAppeared)) / 1000);
                    Service.getInstance().sendThongBao(pl, "Vui lòng đợi " + (timeLeft < 60 ? timeLeft + " giây" : timeLeft / 60 + " phút") + " nữa");
                }
            }
        } else {
            Service.getInstance().sendThongBao(pl, "Chỉ được gọi rồng siêu cấp ở Đảo Kame");
        }
    }

    public void summonIceShenron(Player pl) {
        if (pl.zone.map.mapId == 110) {
            if (checkShenronBall(pl, DRAGON_ICE_SHENRON)) {
                if (isIcecShenronAppear) {
                    Service.getInstance().sendThongBao(pl, "Không thể thực hiện");
                    return;
                }
                if (Util.canDoWithTime(lastTimeShenronAppeared, timeResummonShenron)) {
                    playerSummonShenron = pl;
                    playerSummonShenronId = (int) pl.id;
                    mapShenronAppear = pl.zone;

                    for (int i : NGOC_RONG_BANG) {
                        InventoryService.gI().subQuantityItemsBag(pl, InventoryService.gI().findItemBagByTemp(pl, i), 1);
                    }
                    InventoryService.gI().sendItemBags(pl);
                    sendNotifyShenronAppear(DRAGON_ICE_SHENRON);
                    isIcecShenronAppear = true;
                    activeDragonNew(pl, true, (byte) 59);
                    sendWhishesShenron(pl);
                } else {
                    int timeLeft = (int) ((timeResummonShenron - (System.currentTimeMillis() - lastTimeShenronAppeared)) / 1000);
                    Service.getInstance().sendThongBao(pl, "Vui lòng đợi " + (timeLeft < 60 ? timeLeft + " giây" : timeLeft / 60 + " phút") + " nữa");
                }
            }
        } else {
            Service.getInstance().sendThongBao(pl, "Chỉ được gọi rồng băng tại Coler");
        }
    }

    private void reSummonShenron(byte Dragon) {
        switch (Dragon) {
            case DRAGON_SHENRON:
                activeShenron(playerSummonShenron, true);
                sendWhishesShenron(playerSummonShenron);
                break;
            case DRAGON_BLACK_SHENRON:
                isBlackShenronAppear = true;
                activeDragonNew(playerSummonShenron, true, (byte) 60);
                sendWhishesShenron(playerSummonShenron);
                break;
            case DRAGON_ICE_SHENRON:
                isIcecShenronAppear = true;
                activeDragonNew(playerSummonShenron, true, (byte) 59);
                sendWhishesShenron(playerSummonShenron);
                break;
        }

    }

    private void sendWhishesShenron(Player pl) {
        byte dragonStar;//id item ngọc rồng
        try {
            dragonStar = (byte) pl_dragonStar.get(pl);
            this.shenronStar = dragonStar;
        } catch (Exception e) {
            dragonStar = this.shenronStar;
        }
        switch (dragonStar) {
            case 1:
                NpcService.gI().createMenuRongThieng(pl, ConstNpc.SHENRON_1_1, SHENRON_SAY, SHENRON_1_STAR_WISHES_1);
                break;
            case 2:
                NpcService.gI().createMenuRongThieng(pl, ConstNpc.SHENRON_2, SHENRON_SAY, SHENRON_2_STARS_WHISHES);
                break;
            case 3:
                NpcService.gI().createMenuRongThieng(pl, ConstNpc.SHENRON_3, SHENRON_SAY, SHENRON_3_STARS_WHISHES);
                break;
            case (byte) NGOC_RONG_SIEU_CAP:
                NpcService.gI().createMenuRongThieng(pl, ConstNpc.BLACK_SHENRON, BLACK_SHENRON_SAY, BLACK_SHENRON_WISHES);
                break;
            case (byte) 925:
                NpcService.gI().createMenuRongThieng(pl, ConstNpc.ICE_SHENRON, ICE_SHENRON_SAY, ICE_SHENRON_WISHES);
                break;
        }
    }

    private void activeShenron(Player pl, boolean appear) {
        Message msg;
        try {
            msg = new Message(-83);
            msg.writer().writeByte(appear ? 0 : (byte) 1);
            if (appear) {
                msg.writer().writeShort(pl.zone.map.mapId);
                msg.writer().writeShort(pl.zone.map.bgId);
                msg.writer().writeByte(pl.zone.zoneId);
                msg.writer().writeInt((int) pl.id);
                msg.writer().writeUTF("");
                msg.writer().writeShort(pl.location.x);
                msg.writer().writeShort(pl.location.y);
                msg.writer().writeByte(DRAGON_SHENRON);
                lastTimeShenronWait = System.currentTimeMillis();
                isShenronAppear = true;
            }
            Service.getInstance().sendMessAllPlayerInMap(pl, msg);
        } catch (Exception e) {
        }
    }

    public void activeDragonNew(Player pl, boolean appear, byte eff) {
        Message msg;
        try {
            msg = new Message(Cmd.CALL_DRAGON);
            msg.writer().writeByte(appear ? 0 : (byte) 1);
            if (appear) {
                msg.writer().writeShort(pl.zone.map.mapId);
                msg.writer().writeShort(pl.zone.map.bgId);
                msg.writer().writeByte(pl.zone.zoneId);
                msg.writer().writeInt((int) pl.id);
                msg.writer().writeUTF("");
                msg.writer().writeShort(pl.location.x);
                msg.writer().writeShort(pl.location.y);
                msg.writer().writeByte(DRAGON_PORUNGA);
                pl.zone.effDragon = eff;
                lastTimeShenronWait = System.currentTimeMillis();
            }
            Service.getInstance().sendMessAllPlayerInMap(pl, msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean checkShenronBall(Player pl, byte Dragon) {
        byte dragonStar = (byte) this.pl_dragonStar.get(pl);
        switch (Dragon) {
            case DRAGON_SHENRON:
                if (dragonStar == 1) {
                    if (!InventoryService.gI().existItemBag(pl, NGOC_RONG_2_SAO)) {
                        Service.getInstance().sendThongBao(pl, "Bạn còn thiếu 1 viên ngọc rồng 2 sao");
                        return false;
                    }
                    if (!InventoryService.gI().existItemBag(pl, NGOC_RONG_3_SAO)) {
                        Service.getInstance().sendThongBao(pl, "Bạn còn thiếu 1 viên ngọc rồng 3 sao");
                        return false;
                    }
                } else if (dragonStar == 2) {
                    if (!InventoryService.gI().existItemBag(pl, NGOC_RONG_3_SAO)) {
                        Service.getInstance().sendThongBao(pl, "Bạn còn thiếu 1 viên ngọc rồng 3 sao");
                        return false;
                    }
                }
                if (!InventoryService.gI().existItemBag(pl, NGOC_RONG_4_SAO)) {
                    Service.getInstance().sendThongBao(pl, "Bạn còn thiếu 1 viên ngọc rồng 4 sao");
                    return false;
                }
                if (!InventoryService.gI().existItemBag(pl, NGOC_RONG_5_SAO)) {
                    Service.getInstance().sendThongBao(pl, "Bạn còn thiếu 1 viên ngọc rồng 5 sao");
                    return false;
                }
                if (!InventoryService.gI().existItemBag(pl, NGOC_RONG_6_SAO)) {
                    Service.getInstance().sendThongBao(pl, "Bạn còn thiếu 1 viên ngọc rồng 6 sao");
                    return false;
                }
                if (!InventoryService.gI().existItemBag(pl, NGOC_RONG_7_SAO)) {
                    Service.getInstance().sendThongBao(pl, "Bạn còn thiếu 1 viên ngọc rồng 7 sao");
                    return false;
                }
                return true;
            case DRAGON_BLACK_SHENRON:
                if (!InventoryService.gI().existItemBag(pl, NGOC_RONG_SIEU_CAP)) {
                    Service.getInstance().sendThongBao(pl, "Bạn còn thiếu 1 viên ngọc rồng siêu cấp");
                    return false;
                }
                return true;
            case DRAGON_ICE_SHENRON:
                for (int i : NGOC_RONG_BANG) {
                    if (!InventoryService.gI().existItemBag(pl, i)) {
                        Service.getInstance().sendThongBao(pl, "Bạn còn thiếu 1 viên ngọc rồng băng " + (i - 924) + " sao");
                        return false;
                    }
                }
                return true;
        }
        return false;
    }

    private void sendNotifyShenronAppear(int Dragon) {
        Message msg;
        try {
            msg = new Message(-25);
            switch (Dragon) {
                case DRAGON_SHENRON:
                    msg.writer().writeUTF(playerSummonShenron.name + " vừa gọi rồng thần tại "
                            + playerSummonShenron.zone.map.mapName + " khu vực " + playerSummonShenron.zone.zoneId);
                    Service.getInstance().sendMessAllPlayerIgnoreMe(playerSummonShenron, msg);
                    break;
                case DRAGON_BLACK_SHENRON:
                    msg.writer().writeUTF(playerSummonShenron.name + " vừa gọi rồng siêu cấp tại "
                            + playerSummonShenron.zone.map.mapName + " khu vực " + playerSummonShenron.zone.zoneId);
                    Service.getInstance().sendMessAllPlayerIgnoreMe(playerSummonShenron, msg);
                    break;
                case DRAGON_ICE_SHENRON:
                    msg.writer().writeUTF(playerSummonShenron.name + " vừa gọi rồng băng tại "
                            + playerSummonShenron.zone.map.mapName + " khu vực " + playerSummonShenron.zone.zoneId);
                    Service.getInstance().sendMessAllPlayerIgnoreMe(playerSummonShenron, msg);
                    break;
            }

            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void confirmWish() {
        switch (this.menuShenron) {
            case ConstNpc.SHENRON_1_1:
                switch (this.select) {
                    case 0: //20 tr vàng
                        this.playerSummonShenron.inventory.addGold(2000000000);
                        PlayerService.gI().sendInfoHpMpMoney(this.playerSummonShenron);
                        break;
                    case 1: //găng tay đang đeo lên 1 cấp
                        if (this.playerSummonShenron != null) {
                            Item item = this.playerSummonShenron.inventory.itemsBody.get(2);
                            if (item.isNotNullItem()) {
                                int level = 0;
                                for (ItemOption io : item.itemOptions) {
                                    if (io.optionTemplate.id == 72) {
                                        level = (int) io.param;
                                        if (level < 2) {
                                            io.param++;
                                        }
                                        break;
                                    }
                                }
                                if (level < 2) {
                                    if (level == 0) {
                                        item.itemOptions.add(new ItemOption(72, 1));
                                    }
                                    for (ItemOption io : item.itemOptions) {
                                        if (io.optionTemplate.id == 0) {
                                            io.param += (io.param * 1 / 100);
                                            break;
                                        }
                                    }
                                    InventoryService.gI().sendItemBody(playerSummonShenron);
                                } else {
                                    Service.getInstance().sendThongBao(playerSummonShenron, "Găng tay của ngươi đã đạt cấp tối đa");
                                    reOpenShenronWishes(playerSummonShenron);
                                    return;
                                }
                            } else {
                                Service.getInstance().sendThongBao(playerSummonShenron, "Ngươi hiện tại có đeo găng đâu");
                                reOpenShenronWishes(playerSummonShenron);
                                return;
                            }
                        } else {
                            Service.getInstance().sendThongBao(playerSummonShenron, "Đã có lỗi xảy ra");
                        }
                        break;
                    case 2: //chí mạng +2%
                        if (this.playerSummonShenron != null) {
                            if (this.playerSummonShenron.nPoint.critg < 4) {
                                this.playerSummonShenron.nPoint.critg += 2;
                            } else {
                                Service.getInstance().sendThongBao(playerSummonShenron, "Điều ước này đã quá sức với ta, ta sẽ cho ngươi chọn lại");
                                reOpenShenronWishes(playerSummonShenron);
                                return;
                            }
                        } else {
                            Service.getInstance().sendThongBao(playerSummonShenron, "Đã có lỗi xảy ra || this.playerSummonShenron == null");
                            reOpenShenronWishes(playerSummonShenron);
                        }
                        break;
                    case 3: //thay chiêu 2-3 đệ tử
                        if (playerSummonShenron != null && playerSummonShenron.pet != null) {
                            if (playerSummonShenron.pet.playerSkill.skills.get(1).skillId != -1) {
                                playerSummonShenron.pet.openSkill2();
                                if (playerSummonShenron.pet.playerSkill.skills.get(2).skillId != -1) {
                                    playerSummonShenron.pet.openSkill3();
                                }
                            } else {
                                Service.getInstance().sendThongBao(playerSummonShenron, "Ít nhất đệ tử ngươi phải có chiêu 2 chứ!");
                                reOpenShenronWishes(playerSummonShenron);
                                return;
                            }
                        } else {
                            Service.getInstance().sendThongBao(playerSummonShenron, "Ngươi làm gì có đệ tử?");
                            reOpenShenronWishes(playerSummonShenron);
                            return;
                        }
                        break;
                }
                break;
            case ConstNpc.SHENRON_1_2:
                switch (this.select) {
                    case 0: //đẹp trai nhất vũ trụ
                        if (playerSummonShenron != null) {
                            if (InventoryService.gI().getCountEmptyBag(playerSummonShenron) > 0) {
                                byte gender = this.playerSummonShenron.gender;
                                Item avtVip = ItemService.gI().createNewItem((short) (gender == ConstPlayer.TRAI_DAT ? 227
                                        : gender == ConstPlayer.NAMEC ? 228 : 229));
                                avtVip.itemOptions.add(new ItemOption(97, Util.nextInt(5, 10)));
                                avtVip.itemOptions.add(new ItemOption(77, Util.nextInt(10, 20)));
                                InventoryService.gI().addItemBag(playerSummonShenron, avtVip, 0);
                                InventoryService.gI().sendItemBags(playerSummonShenron);
                            } else {
                                Service.getInstance().sendThongBao(playerSummonShenron, "Hành trang đã đầy");
                                reOpenShenronWishes(playerSummonShenron);
                                return;
                            }
                        } else {
                            Service.getInstance().sendThongBao(playerSummonShenron, "Đã có lỗi xảy ra!");
                        }
                        break;
                    case 1: //+1,5 ngọc
                        this.playerSummonShenron.inventory.ruby += 5_0;
                        PlayerService.gI().sendInfoHpMpMoney(this.playerSummonShenron);
                        break;
                    case 2: //+200 tr smtn
                        if (this.playerSummonShenron.nPoint.power <= 2000) {
                            Service.getInstance().addSMTN(this.playerSummonShenron, (byte) 2, 200000000, false);
                        } else {
                            Service.getInstance().sendThongBao(playerSummonShenron, "Xin lỗi, điều ước này khó quá, ta không thể thực hiện.");
                            reOpenShenronWishes(playerSummonShenron);
                            return;
                        }
                        break;
                    case 3: //găng tay đệ lên 1 cấp
                        if (this.playerSummonShenron != null && this.playerSummonShenron.pet != null) {
                            Item item = this.playerSummonShenron.pet.inventory.itemsBody.get(2);
                            if (item.isNotNullItem()) {
                                long level = 0;
                                for (ItemOption io : item.itemOptions) {
                                    if (io.optionTemplate.id == 72) {
                                        level = io.param;
                                        if (level < 2) {
                                            io.param++;
                                        }
                                        break;
                                    }
                                }
                                if (level < 2) {
                                    if (level == 0) {
                                        item.itemOptions.add(new ItemOption(72, 1));
                                    }
                                    for (ItemOption io : item.itemOptions) {
                                        if (io.optionTemplate.id == 0) {
                                            io.param += (io.param * 1 / 100);
                                            break;
                                        }
                                    }
                                    Service.getInstance().point(playerSummonShenron);
                                } else {
                                    Service.getInstance().sendThongBao(playerSummonShenron, "Găng tay của đệ ngươi đã đạt cấp tối đa");
                                    reOpenShenronWishes(playerSummonShenron);
                                    return;
                                }
                            } else {
                                Service.getInstance().sendThongBao(playerSummonShenron, "Đệ ngươi hiện tại có đeo găng đâu");
                                reOpenShenronWishes(playerSummonShenron);
                                return;
                            }
                        } else {
                            Service.getInstance().sendThongBao(playerSummonShenron, "Ngươi đâu có đệ tử");
                            reOpenShenronWishes(playerSummonShenron);
                            return;
                        }
                        break;
                }
                break;
            case ConstNpc.SHENRON_2:
                switch (this.select) {
                    case 0: //+150 ngọc
                        this.playerSummonShenron.inventory.ruby += 10;
                        PlayerService.gI().sendInfoHpMpMoney(this.playerSummonShenron);
                        break;
                    case 1: //+20 tr smtn
                        if (this.playerSummonShenron.nPoint.power <= 2000) {
                            Service.getInstance().addSMTN(this.playerSummonShenron, (byte) 2, 20000000, false);
                        } else {
                            Service.getInstance().sendThongBao(playerSummonShenron, "Xin lỗi, điều ước này khó quá, ta không thể thực hiện.");
                            reOpenShenronWishes(playerSummonShenron);
                            return;
                        }

                        break;
                    case 2: //2 tr vàng
                        this.playerSummonShenron.inventory.addGold(200000000);
                        PlayerService.gI().sendInfoHpMpMoney(this.playerSummonShenron);
                        break;
                }
                break;
            case ConstNpc.SHENRON_3:
                switch (this.select) {
                    case 0: //+15 ngọc
                        this.playerSummonShenron.inventory.ruby += 5;
                        PlayerService.gI().sendInfoHpMpMoney(this.playerSummonShenron);
                        break;
                    case 1: //+2 tr smtn
                        if (this.playerSummonShenron.nPoint.power <= 2000) {
                            Service.getInstance().addSMTN(this.playerSummonShenron, (byte) 2, 2000000, false);
                        } else {
                            Service.getInstance().sendThongBao(playerSummonShenron, "Xin lỗi, điều ước này khó quá, ta không thể thực hiện.");
                            reOpenShenronWishes(playerSummonShenron);
                            return;
                        }
                        break;
                    case 2: //200k vàng
                        this.playerSummonShenron.inventory.addGold(20000000);
                        PlayerService.gI().sendInfoHpMpMoney(this.playerSummonShenron);
                        break;
                }
                break;
            case ConstNpc.BLACK_SHENRON:
                switch (this.select) {
                     case 0: // +10k thỏi vàng
                    {
                        Item tv = ItemService.gI().createNewItem((short) 457, 1000);
                        InventoryService.gI().addItemBag(playerSummonShenron, tv,99999999999l);
                        InventoryService.gI().sendItemBags(playerSummonShenron);
                    }
                        break;
                    case 1: // 100 sách ép 50%
                        Item sach = ItemService.gI().createNewItem((short)1522, 1);// id temp
                        sach.itemOptions.add(new ItemOption(50, 10));// suc danh
                         sach.itemOptions.add(new ItemOption(30, 0));// suc danh
                        InventoryService.gI().addItemBag(playerSummonShenron, sach,99999);
                        InventoryService.gI().sendItemBags(playerSummonShenron);
                        break;
                    case 2: // 500tr exp tamkjll
                        Item sach1 = ItemService.gI().createNewItem((short)1523, 1);// id temp
                        sach1.itemOptions.add(new ItemOption(77, 10));// suc danh
                         sach1.itemOptions.add(new ItemOption(30, 0));// suc danh
                        InventoryService.gI().addItemBag(playerSummonShenron, sach1,99999);
                        InventoryService.gI().sendItemBags(playerSummonShenron);
                        break;
                    case 3: // 100 cấp tamkjll
                       Item sach2 = ItemService.gI().createNewItem((short)1524, 1);// id temp
                        sach2.itemOptions.add(new ItemOption(103, 10));// suc danh
                         sach2.itemOptions.add(new ItemOption(30, 0));// suc danh
                        InventoryService.gI().addItemBag(playerSummonShenron, sach2,9999999);
                        InventoryService.gI().sendItemBags(playerSummonShenron);
                        break;
                    case 4: // 100 cấp lăng 3
                        playerSummonShenron.LbTamkjll += 20;
                        Service.gI().point(playerSummonShenron);
                        break;
                    case 5: // 2 tỷ hồng ngọc
                        Item luuly = ItemService.gI().createNewItem((short)1404, 20000);// id temp
                         luuly.itemOptions.add(new ItemOption(30, 0));// suc danh
                        InventoryService.gI().addItemBag(playerSummonShenron, luuly,999999999);
                        InventoryService.gI().sendItemBags(playerSummonShenron);
                        break;
                
                }
                break;
            case ConstNpc.ICE_SHENRON:
                switch (this.select) {
                   case 0: // 500k sdg
                      this.playerSummonShenron.nPoint.dameg += 10000;
                      Service.gI().point(playerSummonShenron);
                      
                        break;
                    case 1: //500k hpg
                     this.playerSummonShenron.nPoint.hpg += 50000;
                        Service.gI().point(this.playerSummonShenron);
                        break;
                    case 2: //500k giáp
                      this.playerSummonShenron.nPoint.defg += 500;
                        Service.gI().point(this.playerSummonShenron);
                        break;
                    case 3: //500k ki
                       this.playerSummonShenron.nPoint.mpg += 50000; //critg
                        Service.gI().point(this.playerSummonShenron);
                        break;
                         case 4: //500k ki
                       this.playerSummonShenron.nPoint.critg += 2; //critg
                        Service.gI().point(this.playerSummonShenron);
                        break;
                       
                }
                break;
        }
        shenronLeave(this.playerSummonShenron, WISHED);
    }

    public void showConfirmShenron(Player pl, int menu, byte select) {
        this.menuShenron = menu;
        this.select = select;
        String wish = null;
        switch (menu) {
            case ConstNpc.SHENRON_1_1:
                wish = SHENRON_1_STAR_WISHES_1[select];
                break;
            case ConstNpc.SHENRON_1_2:
                wish = SHENRON_1_STAR_WISHES_2[select];
                break;
            case ConstNpc.SHENRON_2:
                wish = SHENRON_2_STARS_WHISHES[select];
                break;
            case ConstNpc.SHENRON_3:
                wish = SHENRON_3_STARS_WHISHES[select];
                break;
            case ConstNpc.BLACK_SHENRON:
                wish = BLACK_SHENRON_WISHES[select];
                break;
            case ConstNpc.ICE_SHENRON:
                wish = ICE_SHENRON_WISHES[select];
                break;
        }
        NpcService.gI().createMenuRongThieng(pl, ConstNpc.SHENRON_CONFIRM, "Ngươi có chắc muốn ước?", wish, "Từ chối");
    }

    public void reOpenShenronWishes(Player pl) {
        switch (menuShenron) {
            case ConstNpc.SHENRON_1_1:
                NpcService.gI().createMenuRongThieng(pl, ConstNpc.SHENRON_1_1, SHENRON_SAY, SHENRON_1_STAR_WISHES_1);
                break;
            case ConstNpc.SHENRON_1_2:
                NpcService.gI().createMenuRongThieng(pl, ConstNpc.SHENRON_1_2, SHENRON_SAY, SHENRON_1_STAR_WISHES_2);
                break;
            case ConstNpc.SHENRON_2:
                NpcService.gI().createMenuRongThieng(pl, ConstNpc.SHENRON_2, SHENRON_SAY, SHENRON_2_STARS_WHISHES);
                break;
            case ConstNpc.SHENRON_3:
                NpcService.gI().createMenuRongThieng(pl, ConstNpc.SHENRON_3, SHENRON_SAY, SHENRON_3_STARS_WHISHES);
                break;
            case ConstNpc.BLACK_SHENRON:
                NpcService.gI().createMenuRongThieng(pl, ConstNpc.BLACK_SHENRON, BLACK_SHENRON_SAY, BLACK_SHENRON_WISHES);
                break;
            case ConstNpc.ICE_SHENRON:
                NpcService.gI().createMenuRongThieng(pl, ConstNpc.ICE_SHENRON, ICE_SHENRON_SAY, ICE_SHENRON_WISHES);
                break;
        }
    }

    public void shenronLeave(Player pl, byte type) {
        try {
            if (type == WISHED) {
                NpcService.gI().createTutorial(pl, -1, "Điều ước của ngươi đã trở thành sự thật\nHẹn gặp ngươi lần sau, ta đi ngủ đây, bái bai");
            } else {
                NpcService.gI().createMenuRongThieng(pl, ConstNpc.IGNORE_MENU, "Ta buồn ngủ quá rồi\nHẹn gặp ngươi lần sau, ta đi đây, bái bai");
            }
            activeShenron(pl, false);
            this.isBlackShenronAppear = false;
            this.isShenronAppear = false;
            this.isIcecShenronAppear = false;
            this.menuShenron = -1;
            this.select = -1;
            this.playerSummonShenron = null;
            this.playerSummonShenronId = -1;
            this.shenronStar = -1;
            this.mapShenronAppear = null;
            lastTimeShenronAppeared = System.currentTimeMillis();
            if (pl != null && pl.zone != null) {
                pl.zone.effDragon = -1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Service.getInstance().sendThongBao(pl, "Đã có lỗi xảy ra");
        }

    }

    //--------------------------------------------------------------------------
}
