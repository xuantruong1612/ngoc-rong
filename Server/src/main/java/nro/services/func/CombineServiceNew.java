package nro.services.func;

import nro.consts.ConstItem;
import nro.consts.ConstNpc;
import nro.lib.RandomCollection;
import nro.models.item.Item;
import nro.models.item.ItemOption;
import nro.models.npc.Npc;
import nro.models.npc.NpcManager;
import nro.models.player.Player;
import nro.server.ServerLog;
import nro.server.ServerNotify;
import nro.server.io.Message;
import nro.services.InventoryService;
import nro.services.ItemService;
import nro.services.Service;
import nro.utils.Util;
import nro.services.RewardService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;
import nro.models.mob.ArrietyDrop;

/**
 * @Build Arriety
 */
public class CombineServiceNew {

    private static final int COST_DOI_VE_DOI_DO_HUY_DIET = 500000000;
    private static final int COST_DAP_DO_KICH_HOAT = 500000000;
    private static final int COST_DOI_MANH_KICH_HOAT = 500000000;
    private static final int COST_GIA_HAN_CAI_TRANG = 500000000;

    private static final int COST_NANG_DO_TS = 1_000_000_000;
    private static final int COST_RUBY_NANG_DO_TS = 15_000;

    private static final byte MAX_LEVEL_SKH = 10;
    private static final int COST_RUBY_TRADE_TL = 500000000;
    private static final int COST_RUBY_TRADE_TL1 = 5000;

    private static final int TIME_COMBINE = 500;

    private static final int MAX_STAR_ITEM = 40;
    private static final int MAX_LEVEL_ITEM = 16;

    private static final int MAX_LEVEL_CHUC_PHUC = 2000000000;

    private static final byte OPEN_TAB_COMBINE = 0;
    private static final byte REOPEN_TAB_COMBINE = 1;
    private static final byte COMBINE_SUCCESS = 2;
    private static final byte COMBINE_FAIL = 3;
    private static final byte COMBINE_DRAGON_BALL = 5;
    public static final byte OPEN_ITEM = 6;

    public static final int EP_SAO_TRANG_BI = 500;
    public static final int PHA_LE_HOA_TRANG_BI = 501;
    public static final int CHUYEN_HOA_TRANG_BI = 502;
    public static final int DOI_VE_HUY_DIET = 503;
    public static final int DAP_SET_KICH_HOAT = 504;
    public static final int DOI_MANH_KICH_HOAT = 505;
    public static final int NANG_CAP_VAT_PHAM = 506;
    public static final int CHUC_PHUC_SD = 2010;
    public static final int CHUC_PHUC_HP = 2011;
    public static final int CHUC_PHUC_KI = 2012;
    public static final int CHUC_PHUC_DEF = 2013;
    public static final int NANG_CAP_BONG_TAI = 507;
    public static final int MO_CHI_SO_BONG_TAI = 519;
    public static final int LAM_PHEP_NHAP_DA = 508;
    public static final int NHAP_NGOC_RONG = 509;
    public static final int CHE_TAO_DO_THIEN_SU = 510;
    public static final int DAP_SET_KICH_HOAT_CAO_CAP = 511;
    public static final int GIA_HAN_CAI_TRANG = 512;
    public static final int NANG_CAP_DO_THIEN_SU = 513;
    public static final int PHA_LE_HOA_TRANG_BI_X10 = 514;
    public static final int NANG_CAP_DO_TS = 515;
    public static final int TRADE_DO_THAN_LINH = 516;
    public static final int TRADE_DO_THAN_LINH1 = 518;
    public static final int NANG_CAP_DO_KICH_HOAT = 517;
    private static final int COST = 500000000;
    public static final int NANG_CAP_LEVEL_SKH = 52213;
    public static final int EP_CAI_TRANG = 512213;

    private static final int GOLD_MOCS_BONG_TAI = 2_000_000_000;

    private static final int Gem_MOCS_BONG_TAI = 500000;

    private static final int GOLD_BONG_TAI2 = 2000_000_000;

    private static final int GEM_BONG_TAI2 = 100_000;

    private static final int RATIO_NANG_CAP = 100;
    public static final int MO_CHI_SO_CAI_TRANG = 539;
    public static final int XOA_DONG_TRANG_BI = 5288;
    public static final int BONG_TOI_TRANG_BI = 527;
//--------Sách Tuyệt Kỹ
    public static final int GIAM_DINH_SACH = 1233;
    public static final int TAY_SACH = 1234;
    public static final int NANG_CAP_SACH_TUYET_KY = 1235;
    public static final int PHUC_HOI_SACH = 1236;
    public static final int PHAN_RA_SACH = 1237;
    public static final int NANG_CAP_CHAN_MENH = 528;
    public static final int XOA_SPL = 532;
    private final Npc baHatMit;
    private final Npc whis;

    private static CombineServiceNew i;

    public CombineServiceNew() {
        this.baHatMit = NpcManager.getNpc(ConstNpc.BA_HAT_MIT);
        this.whis = NpcManager.getNpc(ConstNpc.WHIS);
    }

    public static CombineServiceNew gI() {
        if (i == null) {
            i = new CombineServiceNew();
        }
        return i;
    }

    /**
     * Mở tab đập đồ
     *
     * @param player
     * @param type kiểu đập đồ
     */
    public void openTabCombine(Player player, int type) {
        player.combineNew.setTypeCombine(type);
        Message msg;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(OPEN_TAB_COMBINE);
            msg.writer().writeUTF(getTextInfoTabCombine(type));
            msg.writer().writeUTF(getTextTopTabCombine(type));
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    private float getRationangbt(int lvbt) { // tile dap do chi hat mit
        switch (lvbt) {
            case 1:
                return 50f;
            case 2:
                return 50f;
            case 3:
                return 50f;

        }
        return 0;
    }

    /**
     * Hiển thị thông tin đập đồ
     *
     * @param player
     */
    public void showInfoCombine(Player player, int[] index) {
        player.combineNew.clearItemCombine();
        if (index.length > 0) {
            for (int i = 0; i < index.length; i++) {
                player.combineNew.itemsCombine.add(player.inventory.itemsBag.get(index[i]));
            }
        }
        switch (player.combineNew.typeCombine) {
            case GIAM_DINH_SACH:
                if (player.combineNew.itemsCombine.size() == 2) {
                    Item sachTuyetKy = null;
                    Item buaGiamDinh = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (issachTuyetKy(item)) {
                            sachTuyetKy = item;
                        } else if (item.template.id == 1423) {
                            buaGiamDinh = item;
                        }
                    }
                    if (sachTuyetKy != null && buaGiamDinh != null) {

                        String npcSay = "|1|" + sachTuyetKy.getName() + "\n";
                        npcSay += "|2|" + buaGiamDinh.getName() + " " + buaGiamDinh.quantity + "/1000";
                        baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                "Giám định", "Từ chối");
                    } else {
                        Service.getInstance().sendThongBaoOK(player, "Cần Sách Tuyệt Kỹ và bùa giám định");
                        return;
                    }
                } else {
                    Service.getInstance().sendThongBaoOK(player, "Cần Sách Tuyệt Kỹ và bùa giám định");
                    return;
                }
                break;
            case TAY_SACH:
                if (player.combineNew.itemsCombine.size() == 1) {
                    Item sachTuyetKy = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (issachTuyetKy(item)) {
                            sachTuyetKy = item;
                        }
                    }
                    if (sachTuyetKy != null) {
                        String npcSay = "|2|Tẩy Sách Tuyệt Kỹ";
                        baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                "Đồng ý", "Từ chối");
                    } else {
                        Service.getInstance().sendThongBaoOK(player, "Cần Sách Tuyệt Kỹ để tẩy");
                        return;
                    }
                } else {
                    Service.getInstance().sendThongBaoOK(player, "Cần Sách Tuyệt Kỹ để tẩy");
                    return;
                }
                break;

            case BONG_TOI_TRANG_BI:
                if (InventoryService.gI().getCountEmptyBag(player) > 0) {
                    if (player.combineNew.itemsCombine.size() == 2) {
                        Item daBongthuat = null;
                        Item daBongma = null;
                        Item daBongyeu = null;
                        Item itemBongToi = null;
                        for (Item item_ : player.combineNew.itemsCombine) {
                            if (item_.template.id == 1722) {
                                daBongthuat = item_;
                            } else if (item_.isTrangBiPet()) {
                                itemBongToi = item_;
                            }
                            if (item_.template.id == 1720) {
                                daBongma = item_;
                            } else if (item_.isTrangBiCaitrang()) {
                                itemBongToi = item_;
                            }
                            if (item_.template.id == 1721) {
                                daBongyeu = item_;
                            } else if (item_.isTrangBiPhukienbang()) {
                                itemBongToi = item_;
                            }
                        }
                        if (daBongma == null) {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Bạn còn thiếu đá bóng ma", "Đóng");
                            return;
                        }
                        if (daBongyeu == null) {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Bạn còn thiếu đá bóng yêu", "Đóng");
                            return;
                        }
                        if (daBongthuat == null) {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Bạn còn thiếu đá bóng thuật", "Đóng");
                            return;
                        }
                        if (itemBongToi == null) {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Bạn còn thiếu trang bị", "Đóng");
                            return;
                        }

                        String npcSay = "|2|Hiện tại " + itemBongToi.template.name + "\n|0|";
                        for (ItemOption io : itemBongToi.itemOptions) {
                            if (io.optionTemplate.id != 72) {
                                npcSay += io.getOptionString() + "\n";
                            }
                        }
                        npcSay += "|2|Sau khi Nâng Cấp sẽ xoá hết các Chỉ Số bóng tối ngẫu nhiên \n|7|"
                                + "\n|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%\n"
                                + "Cần " + Util.numberToMoney(COST) + " vàng";

                        this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
                                npcSay, "Nâng Cấp\n" + Util.numberToMoney(COST) + " vàng", "Từ chối");
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Cần có trang bị có thể bóng tối và 1 loại đá có thể bóng tối", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hành trang cần ít nhất 1 chỗ trống", "Đóng");
                }

                break;
            case XOA_DONG_TRANG_BI:
                if (InventoryService.gI().getCountEmptyBag(player) > 0) {
                    if (player.combineNew.itemsCombine.size() == 2) {
                        Item daAnhsang = null;
                        Item itemBongToi = null;
                        for (Item item_ : player.combineNew.itemsCombine) {
                            if (item_.template.id == 1989) {
                                daAnhsang = item_;
                            } else if (item_.isTrangBiXoadong()) {
                                itemBongToi = item_;
                            }
                        }
                        if (daAnhsang == null) {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Bạn còn thiếu đá bí ẩn", "Đóng");
                            return;
                        }
                        if (itemBongToi == null) {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Bạn còn thiếu trang bị", "Đóng");
                            return;
                        }
                        if (itemBongToi != null) {
                            for (ItemOption itopt : itemBongToi.itemOptions) {
                                if (itopt.optionTemplate.id == 223) {
                                    if (itopt.param >= 8) {
                                        Service.getInstance().sendThongBao(player, "Trang bị đã đạt tới giới hạn bí ẩn");
                                        return;
                                    }
                                }
                            }
                        }

                        player.combineNew.goldCombine = COST;
//                        player.combineNew.gemCombine = RUBY;
                        player.combineNew.ratioCombine = RATIO_NANG_CAP;

                        String npcSay = "|2|Hiện tại " + itemBongToi.template.name + "\n|0|";
                        for (ItemOption io : itemBongToi.itemOptions) {
                            if (io.optionTemplate.id != 72) {
                                npcSay += io.getOptionString() + "\n";
                            }
                        }
                        npcSay += "|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%" + "\n";
                        if (player.combineNew.goldCombine <= player.inventory.gold) {
                            npcSay += "|1|Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng"
                                    + "Cần " + Util.numberToMoney(player.combineNew.gemCombine) + " vàng";

                        }

                        this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
                                npcSay, "Nâng Cấp", "Từ chối");
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Cần có trang bị có thể bí ẩn hóa và đá bí ẩn", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hành trang cần ít nhất 1 chỗ trống", "Đóng");
                }

                break;

            case NANG_CAP_SACH_TUYET_KY:
                if (player.combineNew.itemsCombine.size() == 2) {
                    Item sachTuyetKy = null;
                    Item kimBamGiay = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (issachTuyetKy(item) && (item.template.id == 1412 || item.template.id == 1414 || item.template.id == 1416)) {
                            sachTuyetKy = item;
                        } else if (item.template.id == 1422) {
                            kimBamGiay = item;
                        }
                    }
                    if (sachTuyetKy != null && kimBamGiay != null) {
                        String npcSay = "|2|Nâng Cấp sách tuyệt kỹ\n";
                        npcSay += "Cần 1000 Kìm bấm giấy\n"
                                + "Tỉ lệ thành công: 10%\n"
                                + "Nâng Cấp thất bại sẽ mất 100 Kìm bấm giấy";
                        baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                "Nâng Cấp", "Từ chối");
                    } else {
                        Service.getInstance().sendThongBaoOK(player, "Cần Sách Tuyệt Kỹ 1 và 10 Kìm bấm giấy.");
                        return;
                    }
                } else {
                    Service.getInstance().sendThongBaoOK(player, "Cần Sách Tuyệt Kỹ 1 và 10 Kìm bấm giấy.");
                    return;
                }
                break;
            case PHUC_HOI_SACH:
                if (player.combineNew.itemsCombine.size() == 1) {
                    Item sachTuyetKy = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (issachTuyetKy(item)) {
                            sachTuyetKy = item;
                        }
                    }
                    if (sachTuyetKy != null) {
                        String npcSay = "|2|Phục hồi " + sachTuyetKy.getName() + "\n"
                                + "Cần 1000 cuốn sách cũ\n"
                                + "Phí phục hồi 10 tỷ vàng";
                        baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                "Đồng ý", "Từ chối");
                    } else {
                        Service.getInstance().sendThongBaoOK(player, "Không tìm thấy vật phẩm");
                        return;
                    }
                } else {
                    Service.getInstance().sendThongBaoOK(player, "Không tìm thấy vật phẩm");
                    return;
                }
                break;
            case PHAN_RA_SACH:
                if (player.combineNew.itemsCombine.size() == 1) {
                    Item sachTuyetKy = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (issachTuyetKy(item)) {
                            sachTuyetKy = item;
                        }
                    }
                    if (sachTuyetKy != null) {
                        String npcSay = "|2|Phân rã sách\n"
                                + "Nhận lại 500 cuốn sách cũ\n"
                                + "Phí rã 10 tỷ  vàng";
                        baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                "Đồng ý", "Từ chối");
                    } else {
                        Service.getInstance().sendThongBaoOK(player, "Không tìm thấy vật phẩm");
                        return;
                    }
                } else {
                    Service.getInstance().sendThongBaoOK(player, "Không tìm thấy vật phẩm");
                    return;
                }
                break;
            case NANG_CAP_DO_KICH_HOAT:
                if (player.combineNew.itemsCombine.size() == 0) {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy đưa ta 1  món đồ kích hoạt thường bất kỳ và x5 mảnh Thần Linh", "Đóng");
                    return;
                }
                if (player.combineNew.itemsCombine.size() == 6) {
                    Item manhTL1 = null;
                    Item manhTL2 = null;
                    Item manhTL3 = null;
                    Item manhTL4 = null;
                    Item manhTL5 = null;
                    Item dokh = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (item.isNotNullItem()) {
                            if (item.template.id == 2032) {
                                manhTL1 = item;
                            } else if (item.template.id == 2033) {
                                manhTL2 = item;
                            } else if (item.template.id == 2034) {
                                manhTL3 = item;
                            } else if (item.template.id == 2035) {
                                manhTL4 = item;
                            } else if (item.template.id == 2036) {
                                manhTL5 = item;
                            } else if (item.isSKHThuong()) {
                                dokh = item;
                            }
                        }
                    }
                    if (manhTL1 == null || manhTL2 == null || manhTL3 == null || manhTL4 == null || manhTL5 == null) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu mảnh Thần Linh", "Đóng");
                        return;
                    }
                    if (dokh == null) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu Đồ Kích Hoạt Thường", "Đóng");
                        return;
                    }
                    String npcSay = "|2|Con có muốn Nâng Cấp Đồ KH vip 100% Chỉ Số set bất kì không?\n|7|"
                            + "|1|Cần " + Util.numberToMoney(COST_DAP_DO_KICH_HOAT) + " ngọc xanh";

                    if (player.inventory.gem < COST_DAP_DO_KICH_HOAT) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hết tiền rồi\nẢo ít thôi con", "Đóng");
                        return;
                    }
                    this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
                            npcSay, "Nâng Cấp\n" + Util.numberToMoney(COST_DAP_DO_KICH_HOAT) + "ngọc xanh", "Từ chối");
                } else {
                    if (player.combineNew.itemsCombine.size() > 6) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Nguyên liệu không phù hợp!", "Đóng");
                        return;
                    }
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Còn thiếu nguyên liệu để Nâng Cấp hãy quay lại sau", "Đóng");
                }
                break;
            case MO_CHI_SO_CAI_TRANG:
                if (player.combineNew.itemsCombine.size() == 0) {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Hãy đưa em Đồ chưa kích hoạt Chỉ Số !",
                            "Đóng");
                    return;
                }

                if (player.combineNew.itemsCombine.size() == 2) {
                    if (player.combineNew.itemsCombine.stream().filter(
                            item -> item.isNotNullItem() && (item.template.type == 5))
                            .count() < 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu cải trang kích hoạt", "Đóng");
                        return;
                    }
                    if (player.combineNew.itemsCombine.stream()
                            .filter(item -> item.isNotNullItem() && item.template.id == 1594)
                            .count() < 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu Lưu Ly", "Đóng");
                        return;
                    }

                    String npcSay = "Anh có muốn mở Chỉ Số Cải Trang Vip Này Random 1-1k% !";

                    this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
                            npcSay, "Nâng Cấp", "Từ chối");
                } else {
                    if (player.combineNew.itemsCombine.size() > 2) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Nguyên liệu không phù hợp",
                                "Đóng");
                        return;
                    }
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Còn thiếu nguyên liệu để Nâng Cấp hãy quay lại sau", "Đóng");
                }
                break;
            case EP_CAI_TRANG:
                if (InventoryService.gI().getCountEmptyBag(player) > 0) {
                    if (player.combineNew.itemsCombine.size() == 2) {
                        Item caitrang1 = null;
                        Item caitrang2 = null;
                        for (Item item_ : player.combineNew.itemsCombine) {
                            if (item_.isNotNullItem()) {
                                if (item_.haveOption(167)) {
                                    caitrang1 = item_;
                                } else if (item_.isTrangBiCaitrang() && item_.haveOption(168)) {
                                    caitrang2 = item_;
                                }
                            } else {
                                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU1, "Thiếu Item", "Đóng");
                                return;
                            }

                        }

                        if (caitrang1 == null || caitrang2 == null) {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU1, "Bạn còn thiếu Cải Trang Cường Hóa hoặc Cải Trang có thể Ghép", "Đóng");
                            return;
                        }
                        for (Integer idct : player.listAddCaiTrang) {
                            if (idct == caitrang2.template.id) {
                                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU1, "Bạn chỉ có thể ép một lần cải trang này thôi", "Đóng");
                                return;
                            }
                        }
                        player.combineNew.ratioCombineDaPS = 100;
                        String npcSay = "|2|Hiện tại " + caitrang1.template.name + "\n|0|";
                        for (ItemOption io : caitrang1.itemOptions) {
                            if (io.optionTemplate.id != 72) {
                                npcSay += io.getOptionString() + "\n";
                            }
                        }
                        npcSay += "|2|Sau khi Nâng Cấp sẽ lấy Chỉ Số từ cải trang Có Thể Ghép \n"
                                + "và đồng thời thêm dòng mới vào Cải Trang Cường Hóa\n|7|"
                                + "\n|7|Tỉ lệ thành công: " + player.combineNew.ratioCombineDaPS + "%\n"
                                + "Cần " + Util.numberToMoney(COST) + " vàng";

                        this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
                                npcSay, "Nâng Cấp\n" + Util.numberToMoney(COST) + " vàng", "Từ chối");
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU2, "Cần có Trang Phục Cường Hóa + Cải trang có thể Ghép", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU2, "Hành trang cần ít nhất 1 chỗ trống", "Đóng");
                }

                break;
            case NANG_CAP_LEVEL_SKH:
                if (player.combineNew.itemsCombine.size() >= 3 && player.combineNew.itemsCombine.size() < 4) {
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.type < 4).count() < 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu SKH để Nâng Cấp", "Đóng");
                        break;
                    }
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.type == 14).count() < 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu Đá Nâng Cấp", "Đóng");
                        break;
                    }
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDTL()).count() < 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu 1 món đồ thần linh", "Đóng");
                        break;
                    }

                    Item itemDo = null;
                    Item itemDNC = null;
                    Item itemDBV = null;
                    for (int j = 0; j < player.combineNew.itemsCombine.size(); j++) {
                        if (player.combineNew.itemsCombine.get(j).isNotNullItem()) {
                            if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.get(j).template.id == 987) {
                                itemDBV = player.combineNew.itemsCombine.get(j);
                                continue;
                            }
                            if (player.combineNew.itemsCombine.get(j).template.type < 5) {
                                itemDo = player.combineNew.itemsCombine.get(j);
                            } else {
                                itemDNC = player.combineNew.itemsCombine.get(j);
                            }
                        }
                    }
                    if (isCoupleItemNangCapSKHCheck(itemDo, itemDNC)) {
                        int level = 0;
                        String optionskh = null;
                        for (ItemOption io : itemDo.itemOptions) {
                            if (io.optionTemplate.id == 217
                                    || io.optionTemplate.id == 218
                                    || io.optionTemplate.id == 219
                                    || io.optionTemplate.id == 220
                                    || io.optionTemplate.id == 221
                                    || io.optionTemplate.id == 222
                                    || io.optionTemplate.id == 223
                                    || io.optionTemplate.id == 224
                                    || io.optionTemplate.id == 225) {
                                level = (int) io.param;
                                optionskh = io.optionTemplate.name;
                                break;
                            }
                        }
                        if (level < MAX_LEVEL_SKH) {
                            player.combineNew.goldCombine = getGoldNangCapDo(level);
                            player.combineNew.ratioCombine = (float) getTileNangCapSKH(level);
                            player.combineNew.countDaNangCap = getCountDaNangCapSKH(level);
                            player.combineNew.countDaBaoVe = (short) getCountDaBaoVe(level);
                            String npcSay = "|2|Hiện tại "
                                    + itemDo.template.name + "\n|0|"
                                    + optionskh.replaceAll("#", String.valueOf("+" + level)) + "\n";
                            String option = null;
                            int param = 0;
                            for (ItemOption io : itemDo.itemOptions) {
                                if (io.optionTemplate.id == 226
                                        || io.optionTemplate.id == 227
                                        || io.optionTemplate.id == 228
                                        || io.optionTemplate.id == 229
                                        || io.optionTemplate.id == 230
                                        || io.optionTemplate.id == 231
                                        || io.optionTemplate.id == 232
                                        || io.optionTemplate.id == 233
                                        || io.optionTemplate.id == 234) {
                                    option = io.optionTemplate.name;
                                    param = (int) (io.param + 25);
                                    break;
                                }
                            }
                            npcSay += "|2|Sau khi Nâng Cấp (+" + (level + 1) + ")\n|7|"
                                    + option.replaceAll("#", String.valueOf(param))
                                    + "\n|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%\n"
                                    + (player.combineNew.countDaNangCap > itemDNC.quantity ? "|7|" : "|1|")
                                    + "Cần " + player.combineNew.countDaNangCap + " " + itemDNC.template.name
                                    + "\n" + (player.combineNew.goldCombine > player.inventory.gold ? "|7|" : "|1|")
                                    + "Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";

                            String daNPC = player.combineNew.itemsCombine.size() == 3 && itemDBV != null ? String.format("\nCần tốn %s đá bảo vệ", player.combineNew.countDaBaoVe) : "";
                            if ((level == 2 || level == 4 || level == 6) && !(player.combineNew.itemsCombine.size() == 3 && itemDBV != null)) {
                                npcSay += "\nNếu thất bại sẽ rớt xuống (+" + (level - 1) + ")";
                            }
                            if (player.combineNew.countDaNangCap > itemDNC.quantity) {
                                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                        npcSay, "Còn thiếu\n" + (player.combineNew.countDaNangCap - itemDNC.quantity) + " " + itemDNC.template.name);
                            } else if (player.combineNew.goldCombine > player.inventory.gold) {
                                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                        npcSay, "Còn thiếu\n" + Util.numberToMoney((player.combineNew.goldCombine - player.inventory.gold)) + " vàng");
                            } else if (player.combineNew.itemsCombine.size() == 3 && Objects.nonNull(itemDBV) && itemDBV.quantity < player.combineNew.countDaBaoVe) {
                                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                        npcSay, "Còn thiếu\n" + (player.combineNew.countDaBaoVe - itemDBV.quantity) + " đá bảo vệ");
                            } else {
                                this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINELVL,
                                        npcSay, "Nâng Cấp\n" + Util.numberToMoney(player.combineNew.goldCombine) + " vàng" + daNPC, "Từ chối");
                            }
                        } else {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Trang bị của ngươi đã đạt cấp tối đa", "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy chọn 1 trang bị và 1 loại Đá Nâng Cấp", "Đóng");
                    }
                } else {
                    if (player.combineNew.itemsCombine.size() > 4) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Cất đi con ta không thèm", "Đóng");
                        break;
                    }
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy chọn 1 trang bị SKH và 1 loại Đá Nâng Cấp", "Đóng");
                }
                break;
            case TRADE_DO_THAN_LINH1:
                if (player.combineNew.itemsCombine.size() != 1) {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu Nguyên liệu", "Đóng");
                    break;
                }
                if (InventoryService.gI().getCountEmptyBag(player) < 1) {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Bạn phải có ít Nhất 1 ô trống trong hành trang", "Đóng");
                    break;
                }
                if (player.combineNew.itemsCombine.size() == 1) {
                    Item doTL = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (item.isNotNullItem()) {
                            if (item.template.id >= 555 && item.template.id <= 567) {
                                doTL = item;
                            }
                        }
                    }
                    if (doTL == null || doTL.quantity < 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu Đồ rồi ku!", "Đóng");
                        break;
                    }
                    Item itemMTL = ItemService.gI().createNewItem((short) Util.nextInt(2032, 2036));
                    String npcSay = "Phân rã đồ thần linh\n"
                            + "Trang bị thần linh sẽ được chuyển hóa\n"
                            + "Sang đá cường hóa\n"
                            + "Đá cường hóa dùng để Nâng Cấp SKH\n"
                            + "Tỉ lệ thành công: 100%\n"
                            + "Phí Nâng Cấp: " + Util.numberToMoney(COST_RUBY_TRADE_TL1) + " ruby";
                    if (player.inventory.ruby < COST_RUBY_TRADE_TL1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hết tiền rồi\nNạp tiền vào đay Donate cho taoo", "Đóng");
                        break;
                    }
                    this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
                            npcSay, "Nâng Cấp", "Từ chối");
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Còn thiếu nguyên liệu để Nâng Cấp hãy quay lại sau", "Đóng");
                }
                break;
            case TRADE_DO_THAN_LINH:
                if (player.combineNew.itemsCombine.size() != 2) {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu Nguyên liệu", "Đóng");
                    break;
                }
                if (InventoryService.gI().getCountEmptyBag(player) < 1) {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Bạn phải có ít Nhất 1 ô trống trong hành trang", "Đóng");
                    break;
                }
                if (player.combineNew.itemsCombine.size() == 2) {
                    Item doTL = null;
                    Item thucan = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (item.isNotNullItem()) {
                            if (item.template.id >= 555 && item.template.id <= 567) {
                                doTL = item;
                            }
                            if (item.template.id >= 663 && item.template.id <= 667) {
                                thucan = item;
                            }
                        }
                    }
                    if (doTL == null || doTL.quantity < 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu Đồ rồi thân linh!", "Đóng");
                        break;
                    }
                    if (thucan == null || thucan.quantity < 99) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Cần x99 thức ăn!", "Đóng");
                        break;
                    }
                    String npcSay = "Trang bị Thần Linh sẽ được chuyển hóa\n"
                            + getNameItemC01(doTL.template.gender, doTL.template.type)
                            + "\nTỉ lệ thành công: 100%\n"
                            + "Phí Nâng Cấp: " + Util.numberToMoney(COST_RUBY_TRADE_TL);
                    if (player.inventory.gold < COST_RUBY_TRADE_TL) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hết tiền rồi\nNạp tiền vào đay Donate cho taoo", "Đóng");
                        break;
                    }
                    this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
                            npcSay, "Nâng Cấp", "Từ chối");
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Còn thiếu nguyên liệu để Nâng Cấp hãy quay lại sau", "Đóng");
                }
                break;
            case EP_SAO_TRANG_BI:
                if (player.combineNew.itemsCombine.size() == 2) {
                    Item trangBi = null;
                    Item daPhaLe = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (isTrangBiEpPhaLeHoa(item)) {
                            trangBi = item;
                        } else if (isDaPhaLe(item)) {
                            daPhaLe = item;
                        }
                    }
                    int star = 0; // sao pha lê đã ép
                    int starEmpty = 0; // lỗ sao pha lê
                    if (trangBi != null && daPhaLe != null) {
                        for (ItemOption io : trangBi.itemOptions) {
                            if (io.optionTemplate.id == 102) {
                                star = (int) io.param;
                            } else if (io.optionTemplate.id == 107) {
                                starEmpty = (int) io.param;
                            }
                        }
                        if (star < starEmpty) {
                            player.combineNew.gemCombine = getGemEpSao(star);
                            String npcSay = trangBi.template.name + "\n|2|";
                            for (ItemOption io : trangBi.itemOptions) {
                                if (io.optionTemplate.id != 102) {
                                    npcSay += io.getOptionString() + "\n";
                                }
                            }
                            if (daPhaLe.template.type == 31 || daPhaLe.template.type == 30 || daPhaLe.template.type == 12) {
                                for (ItemOption io : daPhaLe.itemOptions) {
                                    npcSay += "|7|" + io.getOptionString() + "\n";
                                }
                            } else {
                                npcSay += "|7|" + ItemService.gI().getItemOptionTemplate(getOptionDaPhaLe(daPhaLe)).name
                                        .replaceAll("#", getParamDaPhaLe(daPhaLe) + "") + "\n";
                            }
                            npcSay += "|1|Cần " + Util.numberToMoney(player.combineNew.gemCombine) + " ngọc";
                            baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                    "Nâng Cấp\ncần " + player.combineNew.gemCombine + " ngọc");

                        } else {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                    "Cần 1 trang bị có lỗ sao pha lê và 1 loại đá pha lê để ép vào", "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Cần 1 trang bị có lỗ sao pha lê và 1 loại đá pha lê để ép vào", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Cần 1 trang bị có lỗ sao pha lê và 1 loại đá pha lê để ép vào", "Đóng");
                }
                break;
            case PHA_LE_HOA_TRANG_BI:
                if (player.combineNew.itemsCombine.size() == 1) {
                    Item item = player.combineNew.itemsCombine.get(0);
                    if (isTrangBiPhaLeHoa(item)) {
                        long star = 0;
                        for (ItemOption io : item.itemOptions) {
                            if (io.optionTemplate.id == 107) {
                                star = io.param;
                                break;
                            }
                        }
                        if (star < capSaoTheoVip(item, player)) {
                            player.combineNew.goldCombine = getGoldPhaLeHoa(Util.maxInt(star));
                            player.combineNew.gemCombine = getGemPhaLeHoa(Util.maxInt(star));
                            if (star > 1) {
                                player.combineNew.ratioCombine = getRatioPhaLeHoa(Util.maxInt(star)) + ratioTheoVip(player) + getRatioPhaLeHoascam(Util.maxInt(star));

                            } else {
                                player.combineNew.ratioCombine = getRatioPhaLeHoa(Util.maxInt(star));
                            }
                            String npcSay = item.template.name + "\n|2|";
                            for (ItemOption io : item.itemOptions) {
                                if (io.optionTemplate.id != 102) {
                                    npcSay += io.getOptionString() + "\n";
                                }
                            }
                            npcSay += "|7|Tỉ lệ Đục Thành công: " + player.combineNew.ratioCombine + "%" + "\n";
                            npcSay += "|7|Tỉ lệ bonus: " + ratioTheoVip(player) + "% Từ VIP " + player.vip + "\n";
                            npcSay += "|7|Tỉ lệ Đẹp Trai: " + getRatioPhaLeHoascam(Util.maxInt(star)) + "% Từ Hệ Thống\n";
                            if (player.combineNew.goldCombine <= player.inventory.gold) {
                                npcSay += "|1|Cần " + Util.FormatNumber(player.combineNew.goldCombine) + " vàng";
                                baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                        "Nâng Cấp\ncần " + player.combineNew.gemCombine + " ngọc");
                            } else {
                                npcSay += "Còn thiếu "
                                        + Util.FormatNumber(player.combineNew.goldCombine - player.inventory.gold)
                                        + " vàng";
                                baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                            }

                        } else {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                    "Vật phẩm đã đạt tối đa sao pha lê", "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Vật phẩm này không thể đục lỗ\nVui lòng ->Chọn trang bị Thôi",
                                "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy hãy chọn 1 vật phẩm để pha lê hóa",
                            "Đóng");
                }
                break;

            case XOA_SPL:
                if (InventoryService.gI().getCountEmptyBag(player) > 0) {
                    if (player.combineNew.itemsCombine.size() == 2) {
                        Item daPhapSu = null;
//                        Item daBongma = null;
//                        Item daBongyeu = null;
                        Item itemBongToi = null;
                        for (Item item_ : player.combineNew.itemsCombine) {
                            // id để pháp sư
                            if (item_.template.id == 1983) {
                                daPhapSu = item_;
                            } else if (item_.haveOption(102)) {
                                itemBongToi = item_;
                            }

                        }
                        if (daPhapSu == null) {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU1, "Bạn còn thiếu Đá Quý", "Đóng");
                            return;
                        }
                        if (itemBongToi == null) {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU1, "Bạn còn thiếu trang bị để xóa spl", "Đóng");
                            return;
                        }
                        player.combineNew.ratioCombineDaPS = 99;
                        String npcSay = "|2|Hiện tại " + itemBongToi.template.name + "\n|0|";
                        for (ItemOption io : itemBongToi.itemOptions) {
                            if (io.optionTemplate.id != 72) {
                                npcSay += io.getOptionString() + "\n";
                            }
                        }
                        npcSay += "|2|Sau khi Nâng Cấp sẽ xóa một spl nhưng vẫn giữ nguyên Chỉ Số\n|7|"
                                + "\n|7|Tỉ lệ thành công: " + player.combineNew.ratioCombineDaPS + "%\n"
                                + "Cần " + Util.numberToMoney(COST) + " vàng";

                        this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
                                npcSay, "Nâng Cấp\n" + Util.numberToMoney(COST) + " vàng", "Từ chối");
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU2, "Cần có trang bị có thể xóa lỗ spl", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU2, "Hành trang cần ít nhất 1 chỗ trống", "Đóng");
                }

                break;

            case NANG_CAP_DO_TS:
                if (player.combineNew.itemsCombine.size() != 4) {
                    this.whis.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu Nguyên liệu", "Đóng");
                    break;
                }
                if (InventoryService.gI().getCountEmptyBag(player) < 1) {
                    Service.getInstance().sendThongBao(player, "Bạn phải có ít Nhất 1 ô trống trong hành trang");
                    break;
                }
                if (player.combineNew.itemsCombine.size() == 4) {
                    Item manhts = null;
                    Item danc = null;
                    Item damayman = null;
                    Item congthuc = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (item.isNotNullItem()) {
                            if (item.isManhTS()) {
                                manhts = item;
                            } else if (item.isdanangcapDoTs()) {
                                danc = item;
                            } else if (item.isDamayman()) {
                                damayman = item;
                            } else if (item.isCongthucNomal() || item.isCongthucVip()) {
                                congthuc = item;
                            }
                        }
                    }
                    if (manhts == null || manhts.quantity < 99) {
                        Service.getInstance().sendThongBao(player, "Cần x99 mảnh thiên sứ!!");
                        break;
                    }
                    if (danc == null) {
                        Service.getInstance().sendThongBao(player, "Thiếu Đá Nâng Cấp!!");
                        break;
                    }
                    if (damayman == null) {
                        Service.getInstance().sendThongBao(player, "Thiếu Đá May Mắn!!");
                        break;
                    }
                    if (congthuc == null) {
                        Service.getInstance().sendThongBao(player, "Thiếu công thức!!");
                        break;
                    }
                    short[][] itemIds = {{1048, 1051, 1054, 1057, 1060}, {1049, 1052, 1055, 1058, 1061}, {1050, 1053, 1056, 1059, 1062}};
                    Item itemTS = ItemService.gI().createNewItem(itemIds[congthuc.template.gender][manhts.typeIdManh()]);
                    String npcSay = "Chế tạo " + itemTS.template.name + "\n"
                            + "Mạnh hơn trang bị Hủy Diệt từ 20% đến 35%\n"
                            + "Mảnh ghép " + manhts.quantity + "/99 (Thất bại -99 mảnh ghép)\n"
                            + danc.template.name + " (thêm " + (danc.template.id - 1073) * 10 + "% tỉ lệ thành công)\n"
                            + damayman.template.name + " (thêm " + (damayman.template.id - 1078) * 10 + "% tỉ lệ tối đa các Chỉ Số)\n"
                            + "Tỉ lệ thành công: " + get_Tile_nang_Do_TS(danc, congthuc) + "%\n"
                            + "Phí Nâng Cấp: " + Util.numberToMoney(COST_NANG_DO_TS) + " vàng"
                            + "Phí Nâng Cấp: " + Util.numberToMoney(COST_RUBY_NANG_DO_TS) + " hồng ngọc";
                    if (player.inventory.gold < COST_NANG_DO_TS) {
                        this.whis.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hết tiền rồi\nẢo ít thôi con", "Đóng");
                        break;
                    }
                    if (player.inventory.ruby < COST_RUBY_NANG_DO_TS) {
                        this.whis.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hết tiền rồi\nẢo ít thôi con", "Đóng");
                        break;
                    }
                    this.whis.createOtherMenu(player, ConstNpc.MENU_NANG_CAP_DO_TS,
                            npcSay, "Nâng Cấp", "Từ chối");
                } else {
                    this.whis.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Còn thiếu nguyên liệu để Nâng Cấp hãy quay lại sau", "Đóng");
                }
                break;
            case NHAP_NGOC_RONG:
                if (InventoryService.gI().getCountEmptyBag(player) > 0) {
                    if (player.combineNew.itemsCombine.size() == 1) {
                        Item item = player.combineNew.itemsCombine.get(0);
                        if (item != null && item.isNotNullItem()) {
                            if ((item.template.id > 925 && item.template.id <= 931) && item.quantity >= 7) {
                                String npcSay = "|2|Con có muốn biến 7 " + item.template.name + " thành\n" + "1 viên "
                                        + ItemService.gI().getTemplate((short) (item.template.id - 1)).name + "\n"
                                        + "|7|Cần 7 " + item.template.name;
                                this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay, "Làm phép",
                                        "Từ chối");
                            } else {
                                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                        "Cần 7 viên ngọc rồng 2 sao Băng trở lên", "Đóng");
                            }
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Cần 7 viên ngọc rồng Băng 2 sao trở lên", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hành trang cần ít nhất 1 chỗ trống",
                            "Đóng");
                }
                break;
            case NANG_CAP_BONG_TAI:
                if (player.combineNew.itemsCombine.size() == 2) {
                    Item bongtai = null;
                    Item manhvobt = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (checkbongtai(item)) {
                            bongtai = item;
                        } else if (item.template.id == 933) {
                            manhvobt = item;
                        }
                    }

                    if (bongtai != null && manhvobt != null) {
                        int level = 0;
                        for (ItemOption io : bongtai.itemOptions) {
                            if (io.optionTemplate.id == 72) {
                                level = (int) io.param;
                                break;
                            }
                        }
                        if (level < 3) {
                            int lvbt = lvbt(bongtai);
                            int countmvbt = getcountmvbtnangbt(lvbt);
                            player.combineNew.goldCombine = getGoldnangbt(lvbt);
                            player.combineNew.gemCombine = getgemdnangbt(lvbt);
                            player.combineNew.ratioCombine = getRationangbt(lvbt);

                            String npcSay = "Bông Tai Porata Cấp: " + lvbt + " \n|2|";
                            for (ItemOption io : bongtai.itemOptions) {
                                npcSay += io.getOptionString() + "\n";
                            }
                            npcSay += "|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%" + "\n";
                            if (manhvobt.quantity >= countmvbt) {
                                if (player.combineNew.goldCombine <= player.inventory.gold) {
                                    if (player.combineNew.gemCombine <= player.inventory.gem) {
                                        npcSay += "|1|Cần " + Util.numberToMoney(player.combineNew.goldCombine)
                                                + " vàng";
                                        baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                                "Nâng Cấp\ncần " + player.combineNew.gemCombine + " ngọc");
                                    } else {
                                        npcSay += "Còn thiếu " + Util.numberToMoney(
                                                player.combineNew.gemCombine - player.inventory.gem) + " ngọc";
                                        baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                                    }
                                } else {
                                    npcSay += "Còn thiếu "
                                            + Util.numberToMoney(player.combineNew.goldCombine - player.inventory.gold)
                                            + " vàng";
                                    baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                                }
                            } else {
                                npcSay += "Còn thiếu " + Util.numberToMoney(countmvbt - manhvobt.quantity)
                                        + " Mảnh vỡ Bông Tai";
                                baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                            }
                        } else {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                    "Đã đạt cấp tối đa! Nâng con cặc :)))", "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Cần 1 Bông Tai Porata 2 hoặc SS và Mảnh vỡ Bông Tai", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Cần 1 Bông Tai Porata cấp 2 hoặc SS và Mảnh vỡ Bông Tai", "Đóng");
                }
                break;
            case MO_CHI_SO_BONG_TAI:
                if (player.combineNew.itemsCombine.size() == 3) {
                    Item bongTai = null;
                    Item manhHon = null;
                    Item daXanhLam = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        switch (item.template.id) {
                            case 1624:
                            case 1625:
                                bongTai = item;
                                break;
                            case 934:
                                manhHon = item;
                                break;
                            case 935:
                                daXanhLam = item;
                                break;
                            default:
                                break;
                        }
                    }
                    if (bongTai != null && manhHon != null && daXanhLam != null && manhHon.quantity >= 9999) {

                        player.combineNew.goldCombine = GOLD_MOCS_BONG_TAI;
                        player.combineNew.gemCombine = Gem_MOCS_BONG_TAI;
                        player.combineNew.ratioCombine = RATIO_NANG_CAP;

                        String npcSay = "Bông Tai Porata cấp "
                                + (bongTai.template.id == 1624 ? bongTai.template.id == 1625 ? "ProMax" : "ProMax" : "ProMax")
                                + " \n|2|";
                        for (ItemOption io : bongTai.itemOptions) {
                            npcSay += io.getOptionString() + "\n";
                        }
                        npcSay += "|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%" + "\n";
                        if (player.combineNew.goldCombine <= player.inventory.gold) {
                            if (player.combineNew.gemCombine <= player.inventory.gem) {
                                npcSay += "|1|Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";
                                baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                        "Nâng Cấp\ncần " + player.combineNew.gemCombine + " ngọc");
                            } else {
                                npcSay += "Còn thiếu "
                                        + Util.numberToMoney(player.combineNew.gemCombine - player.inventory.gem)
                                        + " ngọc";
                                baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                            }
                        } else {
                            npcSay += "Còn thiếu "
                                    + Util.numberToMoney(player.combineNew.goldCombine - player.inventory.gold)
                                    + " vàng";
                            baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Cần 1 Bông Tai Porata cấp ss hoặc sss, X9999 Mảnh hồn Bông Tai và 1000 Đá xanh lam", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Cần 1 Bông Tai Porata cấp ss hoặc sss, X9999 Mảnh hồn Bông Tai và 1000 Đá xanh lam", "Đóng");
                }

                break;
            case NANG_CAP_VAT_PHAM:
                if (player.combineNew.itemsCombine.size() >= 2 && player.combineNew.itemsCombine.size() < 4) {
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.type < 5).count() < 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu Đồ Nâng Cấp", "Đóng");
                        break;
                    }
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.type == 14).count() < 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu Đá Nâng Cấp", "Đóng");
                        break;
                    }
                    if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.id == 987).count() < 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu Đồ Nâng Cấp", "Đóng");
                        break;
                    }
                    Item itemDo = null;
                    Item itemDNC = null;
                    Item itemDBV = null;
                    for (int j = 0; j < player.combineNew.itemsCombine.size(); j++) {
                        if (player.combineNew.itemsCombine.get(j).isNotNullItem()) {
                            if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.get(j).template.id == 987) {
                                itemDBV = player.combineNew.itemsCombine.get(j);
                                continue;
                            }
                            if (player.combineNew.itemsCombine.get(j).template.type < 5) {
                                itemDo = player.combineNew.itemsCombine.get(j);
                            } else {
                                itemDNC = player.combineNew.itemsCombine.get(j);
                            }
                        }
                    }
                    if (isCoupleItemNangCapCheck(itemDo, itemDNC)) {
                        int level = 0;
                        for (ItemOption io : itemDo.itemOptions) {
                            if (io.optionTemplate.id == 72) {
                                level = (int) io.param;
                                break;
                            }
                        }
                        if (level < MAX_LEVEL_ITEM) {
                            player.combineNew.goldCombine = getGoldNangCapDo(level);
                            player.combineNew.ratioCombine = (float) getTileNangCapDo(level);
                            player.combineNew.countDaNangCap = getCountDaNangCapDo(level);
                            player.combineNew.countDaBaoVe = (short) getCountDaBaoVe(level);
                            String npcSay = "|2|Hiện tại " + itemDo.template.name + " (+" + level + ")\n|0|";
                            for (ItemOption io : itemDo.itemOptions) {
                                if (io.optionTemplate.id != 72) {
                                    npcSay += io.getOptionString() + "\n";
                                }
                            }
                            String option = null;
                            int param = 0;
                            for (ItemOption io : itemDo.itemOptions) {
                                if (io.optionTemplate.id == 47
                                        || io.optionTemplate.id == 6
                                        || io.optionTemplate.id == 0
                                        || io.optionTemplate.id == 7
                                        || io.optionTemplate.id == 14
                                        || io.optionTemplate.id == 22
                                        || io.optionTemplate.id == 23) {
                                    option = io.optionTemplate.name;
                                    param = (int) (io.param + (io.param * 10 / 100));
                                    break;
                                }
                            }
                            npcSay += "|2|Sau khi Nâng Cấp (+" + (level + 1) + ")\n|7|"
                                    + option.replaceAll("#", String.valueOf(param))
                                    + "\n|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%\n"
                                    + (player.combineNew.countDaNangCap > itemDNC.quantity ? "|7|" : "|1|")
                                    + "Cần " + player.combineNew.countDaNangCap + " " + itemDNC.template.name
                                    + "\n" + (player.combineNew.goldCombine > player.inventory.gold ? "|7|" : "|1|")
                                    + "Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";

                            String daNPC = player.combineNew.itemsCombine.size() == 3 && itemDBV != null ? String.format("\nCần tốn %s đá bảo vệ", player.combineNew.countDaBaoVe) : "";
                            if ((level == 2 || level == 4 || level == 6 || level == 8 || level == 9 || level == 10 || level == 11 || level == 12 || level == 13 || level == 14 || level == 15 || level == 16) && !(player.combineNew.itemsCombine.size() == 3 && itemDBV != null)) {
                                npcSay += "\nNếu thất bại sẽ rớt xuống (+" + (level - 2) + ")";
                            }
                            if (player.combineNew.countDaNangCap > itemDNC.quantity) {
                                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                        npcSay, "Còn thiếu\n" + (player.combineNew.countDaNangCap - itemDNC.quantity) + " " + itemDNC.template.name);
                            } else if (player.combineNew.goldCombine > player.inventory.gold) {
                                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                        npcSay, "Còn thiếu\n" + Util.numberToMoney((player.combineNew.goldCombine - player.inventory.gold)) + " vàng");
                            } else if (player.combineNew.itemsCombine.size() == 3 && Objects.nonNull(itemDBV) && itemDBV.quantity < player.combineNew.countDaBaoVe) {
                                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                        npcSay, "Còn thiếu\n" + (player.combineNew.countDaBaoVe - itemDBV.quantity) + " đá bảo vệ");
                            } else {
                                this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
                                        npcSay, "Nâng Cấp\n" + Util.numberToMoney(player.combineNew.goldCombine) + " vàng" + daNPC, "Từ chối");
                            }
                        } else {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Trang bị của ngươi đã đạt cấp tối đa", "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy chọn 1 trang bị và 1 loại Đá Nâng Cấp", "Đóng");
                    }
                } else {
                    if (player.combineNew.itemsCombine.size() > 3) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Cất đi con ta không thèm", "Đóng");
                        break;
                    }
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy chọn 1 trang bị và 1 loại Đá Nâng Cấp", "Đóng");
                }
                break;
            case CHUC_PHUC_SD:
                if (player.combineNew.itemsCombine.size() >= 2 && player.combineNew.itemsCombine.size() < 4) {
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.type < 5).count() < 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu Đồ Nâng Cấp", "Đóng");
                        break;
                    }
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.id == 1404).count() < 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu Đá Nâng Cấp", "Đóng");
                        break;
                    }
                    if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.id == 987).count() < 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu Đồ Nâng Cấp", "Đóng");
                        break;
                    }
                    Item itemDo = null;
                    Item itemDNC = null;
                    Item itemDBV = null;
                    for (int j = 0; j < player.combineNew.itemsCombine.size(); j++) {
                        if (player.combineNew.itemsCombine.get(j).isNotNullItem()) {
                            if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.get(j).template.id == 987) {
                                itemDBV = player.combineNew.itemsCombine.get(j);
                                continue;
                            }
                            if (player.combineNew.itemsCombine.get(j).template.type < 5) {
                                itemDo = player.combineNew.itemsCombine.get(j);
                            } else {
                                itemDNC = player.combineNew.itemsCombine.get(j);
                            }
                        }
                    }
                    if (isItemChucPhuc(itemDo, itemDNC)) {
                        int level = 0;
                        for (ItemOption io : itemDo.itemOptions) {
                            if (io.optionTemplate.id == 210) {
                                level = (int) io.param;
                                break;
                            }
                        }
                        if (level < MAX_LEVEL_CHUC_PHUC) {
                            player.combineNew.goldCombine = getGoldNangCapDo(level);
                            player.combineNew.ratioCombine = (float) getTileNangCapDochucphuc(level);
                            player.combineNew.countDaNangCap = getCountDaNangCapDochucphuc(level);
                            player.combineNew.countDaBaoVe = (short) getCountDaBaoVe(level);
                            String npcSay = "|2|Hiện tại " + itemDo.template.name + " (+" + level + ")\n|0|";
                            for (ItemOption io : itemDo.itemOptions) {
                                if (io.optionTemplate.id != 210) {
                                    npcSay += io.getOptionString() + "\n";
                                }
                            }

                            npcSay += "|2|Sau khi Nâng Cấp (+" + (level + 1) + ")\n|7|"
                                    + "\n|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%\n"
                                    + (player.combineNew.countDaNangCap > itemDNC.quantity ? "|7|" : "|1|")
                                    + "Cần " + player.combineNew.countDaNangCap + " " + itemDNC.template.name
                                    + "\n" + (player.combineNew.goldCombine > player.inventory.gold ? "|7|" : "|1|")
                                    + "Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";

                            String daNPC = player.combineNew.itemsCombine.size() == 3 && itemDBV != null ? String.format("\nCần tốn %s đá bảo vệ", player.combineNew.countDaBaoVe) : "";
                            if ((level >= 2) && !(player.combineNew.itemsCombine.size() == 3 && itemDBV != null)) {
                                npcSay += "\nNếu thất bại sẽ rớt xuống (+" + (level - 1) + ")";
                            }
                            if (player.combineNew.countDaNangCap > itemDNC.quantity) {
                                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                        npcSay, "Còn thiếu\n" + (player.combineNew.countDaNangCap - itemDNC.quantity) + " " + itemDNC.template.name);
                            } else if (player.combineNew.goldCombine > player.inventory.gold) {
                                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                        npcSay, "Còn thiếu\n" + Util.numberToMoney((player.combineNew.goldCombine - player.inventory.gold)) + " vàng");
                            } else if (player.combineNew.itemsCombine.size() == 3 && Objects.nonNull(itemDBV) && itemDBV.quantity < player.combineNew.countDaBaoVe) {
                                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                        npcSay, "Còn thiếu\n" + (player.combineNew.countDaBaoVe - itemDBV.quantity) + " đá bảo vệ");
                            } else {
                                this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
                                        npcSay, "Nâng Cấp\n" + Util.numberToMoney(player.combineNew.goldCombine) + " vàng" + daNPC, "Từ chối");
                            }
                        } else {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Trang bị của ngươi đã đạt cấp tối đa", "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy chọn 1 trang bị và 1 loại Đá Nâng Cấp", "Đóng");
                    }
                } else {
                    if (player.combineNew.itemsCombine.size() > 3) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Cất đi con ta không thèm", "Đóng");
                        break;
                    }
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy chọn 1 trang bị và 1 loại Đá Nâng Cấp", "Đóng");
                }
                break;
            case CHUC_PHUC_HP:
                if (player.combineNew.itemsCombine.size() >= 2 && player.combineNew.itemsCombine.size() < 4) {
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.type < 5).count() < 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu Đồ Nâng Cấp", "Đóng");
                        break;
                    }
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.id == 1404).count() < 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu Đá Nâng Cấp", "Đóng");
                        break;
                    }
                    if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.id == 987).count() < 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu Đồ Nâng Cấp", "Đóng");
                        break;
                    }
                    Item itemDo = null;
                    Item itemDNC = null;
                    Item itemDBV = null;
                    for (int j = 0; j < player.combineNew.itemsCombine.size(); j++) {
                        if (player.combineNew.itemsCombine.get(j).isNotNullItem()) {
                            if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.get(j).template.id == 987) {
                                itemDBV = player.combineNew.itemsCombine.get(j);
                                continue;
                            }
                            if (player.combineNew.itemsCombine.get(j).template.type < 5) {
                                itemDo = player.combineNew.itemsCombine.get(j);
                            } else {
                                itemDNC = player.combineNew.itemsCombine.get(j);
                            }
                        }
                    }
                    if (isItemChucPhuc(itemDo, itemDNC)) {
                        int level = 0;
                        for (ItemOption io : itemDo.itemOptions) {
                            if (io.optionTemplate.id == 211) {
                                level = (int) io.param;
                                break;
                            }
                        }
                        if (level < MAX_LEVEL_CHUC_PHUC) {
                            player.combineNew.goldCombine = getGoldNangCapDo(level);
                            player.combineNew.ratioCombine = (float) getTileNangCapDochucphuc(level);
                            player.combineNew.countDaNangCap = getCountDaNangCapDochucphuc(level);
                            player.combineNew.countDaBaoVe = (short) getCountDaBaoVe(level);
                            String npcSay = "|2|Hiện tại " + itemDo.template.name + " (+" + level + ")\n|0|";
                            for (ItemOption io : itemDo.itemOptions) {
                                if (io.optionTemplate.id != 211) {
                                    npcSay += io.getOptionString() + "\n";
                                }
                            }

                            npcSay += "|2|Sau khi Nâng Cấp (+" + (level + 1) + ")\n|7|"
                                    + "\n|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%\n"
                                    + (player.combineNew.countDaNangCap > itemDNC.quantity ? "|7|" : "|1|")
                                    + "Cần " + player.combineNew.countDaNangCap + " " + itemDNC.template.name
                                    + "\n" + (player.combineNew.goldCombine > player.inventory.gold ? "|7|" : "|1|")
                                    + "Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";

                            String daNPC = player.combineNew.itemsCombine.size() == 3 && itemDBV != null ? String.format("\nCần tốn %s đá bảo vệ", player.combineNew.countDaBaoVe) : "";
                            if ((level >= 2) && !(player.combineNew.itemsCombine.size() == 3 && itemDBV != null)) {
                                npcSay += "\nNếu thất bại sẽ rớt xuống (+" + (level - 1) + ")";
                            }
                            if (player.combineNew.countDaNangCap > itemDNC.quantity) {
                                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                        npcSay, "Còn thiếu\n" + (player.combineNew.countDaNangCap - itemDNC.quantity) + " " + itemDNC.template.name);
                            } else if (player.combineNew.goldCombine > player.inventory.gold) {
                                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                        npcSay, "Còn thiếu\n" + Util.numberToMoney((player.combineNew.goldCombine - player.inventory.gold)) + " vàng");
                            } else if (player.combineNew.itemsCombine.size() == 3 && Objects.nonNull(itemDBV) && itemDBV.quantity < player.combineNew.countDaBaoVe) {
                                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                        npcSay, "Còn thiếu\n" + (player.combineNew.countDaBaoVe - itemDBV.quantity) + " đá bảo vệ");
                            } else {
                                this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
                                        npcSay, "Nâng Cấp\n" + Util.numberToMoney(player.combineNew.goldCombine) + " vàng" + daNPC, "Từ chối");
                            }
                        } else {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Trang bị của ngươi đã đạt cấp tối đa", "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy chọn 1 trang bị và 1 loại Đá Nâng Cấp", "Đóng");
                    }
                } else {
                    if (player.combineNew.itemsCombine.size() > 3) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Cất đi con ta không thèm", "Đóng");
                        break;
                    }
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy chọn 1 trang bị và 1 loại Đá Nâng Cấp", "Đóng");
                }
                break;

            case CHUC_PHUC_KI:
                if (player.combineNew.itemsCombine.size() >= 2 && player.combineNew.itemsCombine.size() < 4) {
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.type < 5).count() < 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu Đồ Nâng Cấp", "Đóng");
                        break;
                    }
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.id == 1404).count() < 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu Đá Nâng Cấp", "Đóng");
                        break;
                    }
                    if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.id == 987).count() < 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu Đồ Nâng Cấp", "Đóng");
                        break;
                    }
                    Item itemDo = null;
                    Item itemDNC = null;
                    Item itemDBV = null;
                    for (int j = 0; j < player.combineNew.itemsCombine.size(); j++) {
                        if (player.combineNew.itemsCombine.get(j).isNotNullItem()) {
                            if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.get(j).template.id == 987) {
                                itemDBV = player.combineNew.itemsCombine.get(j);
                                continue;
                            }
                            if (player.combineNew.itemsCombine.get(j).template.type < 5) {
                                itemDo = player.combineNew.itemsCombine.get(j);
                            } else {
                                itemDNC = player.combineNew.itemsCombine.get(j);
                            }
                        }
                    }
                    if (isItemChucPhuc(itemDo, itemDNC)) {
                        int level = 0;
                        for (ItemOption io : itemDo.itemOptions) {
                            if (io.optionTemplate.id == 212) {
                                level = (int) io.param;
                                break;
                            }
                        }
                        if (level < MAX_LEVEL_CHUC_PHUC) {
                            player.combineNew.goldCombine = getGoldNangCapDo(level);
                            player.combineNew.ratioCombine = (float) getTileNangCapDochucphuc(level);
                            player.combineNew.countDaNangCap = getCountDaNangCapDochucphuc(level);
                            player.combineNew.countDaBaoVe = (short) getCountDaBaoVe(level);
                            String npcSay = "|2|Hiện tại " + itemDo.template.name + " (+" + level + ")\n|0|";
                            for (ItemOption io : itemDo.itemOptions) {
                                if (io.optionTemplate.id != 212) {
                                    npcSay += io.getOptionString() + "\n";
                                }
                            }

                            npcSay += "|2|Sau khi Nâng Cấp (+" + (level + 1) + ")\n|7|"
                                    + "\n|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%\n"
                                    + (player.combineNew.countDaNangCap > itemDNC.quantity ? "|7|" : "|1|")
                                    + "Cần " + player.combineNew.countDaNangCap + " " + itemDNC.template.name
                                    + "\n" + (player.combineNew.goldCombine > player.inventory.gold ? "|7|" : "|1|")
                                    + "Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";

                            String daNPC = player.combineNew.itemsCombine.size() == 3 && itemDBV != null ? String.format("\nCần tốn %s đá bảo vệ", player.combineNew.countDaBaoVe) : "";
                            if ((level >= 2) && !(player.combineNew.itemsCombine.size() == 3 && itemDBV != null)) {
                                npcSay += "\nNếu thất bại sẽ rớt xuống (+" + (level - 1) + ")";
                            }
                            if (player.combineNew.countDaNangCap > itemDNC.quantity) {
                                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                        npcSay, "Còn thiếu\n" + (player.combineNew.countDaNangCap - itemDNC.quantity) + " " + itemDNC.template.name);
                            } else if (player.combineNew.goldCombine > player.inventory.gold) {
                                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                        npcSay, "Còn thiếu\n" + Util.numberToMoney((player.combineNew.goldCombine - player.inventory.gold)) + " vàng");
                            } else if (player.combineNew.itemsCombine.size() == 3 && Objects.nonNull(itemDBV) && itemDBV.quantity < player.combineNew.countDaBaoVe) {
                                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                        npcSay, "Còn thiếu\n" + (player.combineNew.countDaBaoVe - itemDBV.quantity) + " đá bảo vệ");
                            } else {
                                this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
                                        npcSay, "Nâng Cấp\n" + Util.numberToMoney(player.combineNew.goldCombine) + " vàng" + daNPC, "Từ chối");
                            }
                        } else {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Trang bị của ngươi đã đạt cấp tối đa", "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy chọn 1 trang bị và 1 loại Đá Nâng Cấp", "Đóng");
                    }
                } else {
                    if (player.combineNew.itemsCombine.size() > 3) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Cất đi con ta không thèm", "Đóng");
                        break;
                    }
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy chọn 1 trang bị và 1 loại Đá Nâng Cấp", "Đóng");
                }
                break;
            case CHUC_PHUC_DEF:
                if (player.combineNew.itemsCombine.size() >= 2 && player.combineNew.itemsCombine.size() < 4) {
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.type < 5).count() < 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu Đồ Nâng Cấp", "Đóng");
                        break;
                    }
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.id == 1404).count() < 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu Đá Nâng Cấp", "Đóng");
                        break;
                    }
                    if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.id == 987).count() < 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu Đồ Nâng Cấp", "Đóng");
                        break;
                    }
                    Item itemDo = null;
                    Item itemDNC = null;
                    Item itemDBV = null;
                    for (int j = 0; j < player.combineNew.itemsCombine.size(); j++) {
                        if (player.combineNew.itemsCombine.get(j).isNotNullItem()) {
                            if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.get(j).template.id == 987) {
                                itemDBV = player.combineNew.itemsCombine.get(j);
                                continue;
                            }
                            if (player.combineNew.itemsCombine.get(j).template.type < 5) {
                                itemDo = player.combineNew.itemsCombine.get(j);
                            } else {
                                itemDNC = player.combineNew.itemsCombine.get(j);
                            }
                        }
                    }
                    if (isItemChucPhuc(itemDo, itemDNC)) {
                        int level = 0;
                        for (ItemOption io : itemDo.itemOptions) {
                            if (io.optionTemplate.id == 213) {
                                level = (int) io.param;
                                break;
                            }
                        }
                        if (level < MAX_LEVEL_CHUC_PHUC) {
                            player.combineNew.goldCombine = getGoldNangCapDo(level);
                            player.combineNew.ratioCombine = (float) getTileNangCapDochucphuc(level);
                            player.combineNew.countDaNangCap = getCountDaNangCapDochucphuc(level);
                            player.combineNew.countDaBaoVe = (short) getCountDaBaoVe(level);
                            String npcSay = "|2|Hiện tại " + itemDo.template.name + " (+" + level + ")\n|0|";
                            for (ItemOption io : itemDo.itemOptions) {
                                if (io.optionTemplate.id != 213) {
                                    npcSay += io.getOptionString() + "\n";
                                }
                            }

                            npcSay += "|2|Sau khi Nâng Cấp (+" + (level + 1) + ")\n|7|"
                                    + "\n|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%\n"
                                    + (player.combineNew.countDaNangCap > itemDNC.quantity ? "|7|" : "|1|")
                                    + "Cần " + player.combineNew.countDaNangCap + " " + itemDNC.template.name
                                    + "\n" + (player.combineNew.goldCombine > player.inventory.gold ? "|7|" : "|1|")
                                    + "Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";

                            String daNPC = player.combineNew.itemsCombine.size() == 3 && itemDBV != null ? String.format("\nCần tốn %s đá bảo vệ", player.combineNew.countDaBaoVe) : "";
                            if ((level >= 2) && !(player.combineNew.itemsCombine.size() == 3 && itemDBV != null)) {
                                npcSay += "\nNếu thất bại sẽ rớt xuống (+" + (level - 1) + ")";
                            }
                            if (player.combineNew.countDaNangCap > itemDNC.quantity) {
                                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                        npcSay, "Còn thiếu\n" + (player.combineNew.countDaNangCap - itemDNC.quantity) + " " + itemDNC.template.name);
                            } else if (player.combineNew.goldCombine > player.inventory.gold) {
                                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                        npcSay, "Còn thiếu\n" + Util.numberToMoney((player.combineNew.goldCombine - player.inventory.gold)) + " vàng");
                            } else if (player.combineNew.itemsCombine.size() == 3 && Objects.nonNull(itemDBV) && itemDBV.quantity < player.combineNew.countDaBaoVe) {
                                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                        npcSay, "Còn thiếu\n" + (player.combineNew.countDaBaoVe - itemDBV.quantity) + " đá bảo vệ");
                            } else {
                                this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
                                        npcSay, "Nâng Cấp\n" + Util.numberToMoney(player.combineNew.goldCombine) + " vàng" + daNPC, "Từ chối");
                            }
                        } else {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Trang bị của ngươi đã đạt cấp tối đa", "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy chọn 1 trang bị và 1 loại Đá Nâng Cấp", "Đóng");
                    }
                } else {
                    if (player.combineNew.itemsCombine.size() > 3) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Cất đi con ta không thèm", "Đóng");
                        break;
                    }
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy chọn 1 trang bị và 1 loại Đá Nâng Cấp", "Đóng");
                }
                break;

            case DOI_VE_HUY_DIET:
                if (player.combineNew.itemsCombine.size() == 1) {
                    Item item = player.combineNew.itemsCombine.get(0);
                    if (item.isNotNullItem() && item.template.id >= 555 && item.template.id <= 567) {
                        String ticketName = "Vé đổi " + (item.template.type == 0 ? "áo"
                                : item.template.type == 1 ? "quần"
                                        : item.template.type == 2 ? "găng" : item.template.type == 3 ? "giày" : "nhẫn")
                                + " hủy diệt";
                        String npcSay = "|6|Ngươi có chắc chắn muốn đổi\n|7|" + item.template.name + "\n";
                        for (ItemOption io : item.itemOptions) {
                            npcSay += "|2|" + io.getOptionString() + "\n";
                        }
                        npcSay += "|6|Lấy\n|7|" + ticketName + "\n|6|Với giá "
                                + Util.numberToMoney(COST_DOI_VE_DOI_DO_HUY_DIET) + " vàng không?";
                        if (player.inventory.gold >= COST_DOI_VE_DOI_DO_HUY_DIET) {
                            this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay, "Đổi",
                                    "Từ chối");
                        } else {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Còn thiếu\n"
                                    + Util.numberToMoney(COST_DOI_VE_DOI_DO_HUY_DIET - player.inventory.gold) + " vàng",
                                    "Đóng");
                        }

                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Hãy chọn 1 trang bị thần linh ngươi muốn trao đổi", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Hãy chọn 1 trang bị thần linh ngươi muốn trao đổi", "Đóng");
                }
                break;
            case DOI_MANH_KICH_HOAT:
                if (player.combineNew.itemsCombine.size() == 2 || player.combineNew.itemsCombine.size() == 3) {
                    Item nr1s = null, doThan = null, buaBaoVe = null;
                    for (Item it : player.combineNew.itemsCombine) {
                        if (it.template.id == 14) {
                            nr1s = it;
                        } else if (it.template.id == 2010) {
                            buaBaoVe = it;
                        } else if (it.template.id >= 555 && it.template.id <= 567) {
                            doThan = it;
                        }
                    }

                    if (nr1s != null && doThan != null) {
                        int tile = 50;
                        String npcSay = "|6|Ngươi có muốn trao đổi\n|7|" + nr1s.template.name
                                + "\n|7|" + doThan.template.name
                                + "\n";
                        for (ItemOption io : doThan.itemOptions) {
                            npcSay += "|2|" + io.getOptionString() + "\n";
                        }
                        if (buaBaoVe != null) {
                            tile = 100;
                            npcSay += buaBaoVe.template.name
                                    + "\n";
                            for (ItemOption io : buaBaoVe.itemOptions) {
                                npcSay += "|2|" + io.getOptionString() + "\n";
                            }
                        }

                        npcSay += "|6|Lấy\n|7|Mảnh kích hoạt\n"
                                + "|1|Tỉ lệ " + tile + "%\n"
                                + "|6|Với giá " + Util.numberToMoney(COST_DOI_MANH_KICH_HOAT) + " vàng không?";
                        if (player.inventory.gold >= COST_DOI_MANH_KICH_HOAT) {
                            this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
                                    npcSay, "Đổi", "Từ chối");
                        } else {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                    npcSay, "Còn thiếu\n"
                                    + Util.numberToMoney(COST_DOI_MANH_KICH_HOAT - player.inventory.gold) + " vàng", "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy chọn 1 trang bị thần linh và 1 viên ngọc rồng 1 sao", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy chọn 1 trang bị thần linh và 1 viên ngọc rồng 1 sao", "Đóng");
                }
                break;
            case DAP_SET_KICH_HOAT_CAO_CAP:
                if (player.combineNew.itemsCombine.size() == 3) {
                    Item it = player.combineNew.itemsCombine.get(0), it1 = player.combineNew.itemsCombine.get(1),
                            it2 = player.combineNew.itemsCombine.get(2);
                    if (!isDestroyClothes(it.template.id) || !isDestroyClothes(it1.template.id)
                            || !isDestroyClothes(it2.template.id)) {
                        it = null;
                    }
                    if (it != null) {
                        String npcSay = "|1|" + it.template.name + "\n" + it1.template.name + "\n" + it2.template.name
                                + "\n";
                        npcSay += "Ngươi có muốn chuyển hóa thành\n";
                        npcSay += "|7|" + getTypeTrangBi(it.template.type)
                                + " cấp bậc ngẫu nhiên (set kích hoạt ngẫu nhiên)\n|2|Cần "
                                + Util.numberToMoney(COST_DAP_DO_KICH_HOAT) + " vàng";
                        if (player.inventory.gold >= COST_DAP_DO_KICH_HOAT) {
                            this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                    "Cần " + Util.numberToMoney(COST_DAP_DO_KICH_HOAT) + " vàng");
                        } else {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Còn thiếu\n"
                                    + Util.numberToMoney(player.inventory.gold - COST_DAP_DO_KICH_HOAT) + " vàng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Ta cần 3 món đồ hủy diệt của ngươi để có thể chuyển hóa", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Ta cần 3 món đồ hủy diệt của ngươi để có thể chuyển hóa", "Đóng");
                }
                break;
            case DAP_SET_KICH_HOAT:
                if (player.combineNew.itemsCombine.size() == 1) {
                    Item dhd = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (item.isNotNullItem()) {
                            if (item.template.id >= 650 && item.template.id <= 662) {
                                dhd = item;
                            }
                        }
                    }
                    if (dhd != null) {
                        String npcSay = "|6|" + dhd.template.name + "\n";
                        for (ItemOption io : dhd.itemOptions) {
                            npcSay += "|2|" + io.getOptionString() + "\n";
                        }
                        // Tỉ lệ thành công 100%
                        npcSay += "Ngươi có muốn chuyển hóa thành\n";
                        npcSay += "|1|" + getNameItemC0(dhd.template.gender, dhd.template.type)
                                + " (ngẫu nhiên kích hoạt)\n|7|Tỉ lệ thành công 100%" // Thay đổi thành công 100%
                                + "\n|2|Cần " + Util.numberToMoney(COST_DAP_DO_KICH_HOAT) + " vàng";
                        if (player.inventory.gold >= COST_DAP_DO_KICH_HOAT) {
                            this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                    "Cần " + Util.numberToMoney(COST_DAP_DO_KICH_HOAT) + " vàng");
                        } else {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Còn thiếu\n"
                                    + Util.numberToMoney(player.inventory.gold - COST_DAP_DO_KICH_HOAT) + " vàng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Ta cần 1 món đồ hủy diệt của ngươi để có thể chuyển hóa 1", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Ta cần 1 món đồ hủy diệt của ngươi để có thể chuyển hóa 2", "Đóng");
                }
                break;
            case GIA_HAN_CAI_TRANG:
                if (player.combineNew.itemsCombine.size() == 2) {
                    Item caitrang = null, vegiahan = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (item.isNotNullItem()) {
                            if (item.template.type == 5 || item.template.type == 21 || item.template.type == 11 || item.template.type == 72 || item.template.type == 74) {
                                caitrang = item;
                            } else if (item.template.id == 1280) {
                                vegiahan = item;
                            }
                        }
                    }
                    long expiredDate = 0;
                    boolean canBeExtend = true;
                    if (caitrang != null && vegiahan != null) {
                        for (ItemOption io : caitrang.itemOptions) {
                            if (io.optionTemplate.id == 93) {
                                expiredDate = io.param;
                            }
                            if (io.optionTemplate.id == 199) {
                                canBeExtend = false;
                            }
                        }
                        if (canBeExtend) {
                            if (expiredDate > 0) {
                                String npcSay = "|2|" + caitrang.template.name + "\n"
                                        + "Sau khi gia hạn +1 ngày \n Tỷ lệ thành công: 101% \n" + "|7|Cần 5 Tỷ Vàng";
                                if (player.inventory.gold >= COST_GIA_HAN_CAI_TRANG) {
                                    this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                            "Gia hạn");
                                } else {
                                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay,
                                            "Còn thiếu\n"
                                            + Util.numberToMoney(player.inventory.gold - COST_GIA_HAN_CAI_TRANG)
                                            + " vàng");
                                }
                            } else {
                                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                        "Cần cải trang có hạn sử dụng và thẻ gia hạn", "Đóng");
                            }
                        } else {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                    "Vật phẩm này không thể gia hạn", "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Ta Cần cải trang có hạn sử dụng và thẻ gia hạn", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Ta Cần cải trang có hạn sử dụng và thẻ gia hạn", "Đóng");
                }
                break;
            case NANG_CAP_DO_THIEN_SU:
                if (player.combineNew.itemsCombine.size() > 1) {
                    int ratioLuckyStone = 0, ratioRecipe = 0, ratioUpgradeStone = 0, countLuckyStone = 0,
                            countUpgradeStone = 0;
                    Item angelClothes = null;
                    Item craftingRecipe = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        int id = item.template.id;
                        if (item.isNotNullItem()) {
                            if (isAngelClothes(id)) {
                                angelClothes = item;
                            } else if (isLuckyStone(id)) {
                                ratioLuckyStone += getRatioLuckyStone(id);
                                countLuckyStone++;
                            } else if (isUpgradeStone(id)) {
                                ratioUpgradeStone += getRatioUpgradeStone(id);
                                countUpgradeStone++;
                            } else if (isCraftingRecipe(id)) {
                                ratioRecipe += getRatioCraftingRecipe(id);
                                craftingRecipe = item;
                            }
                        }
                    }
                    if (angelClothes == null) {
                        return;
                    }
                    boolean canUpgrade = true;
                    for (ItemOption io : angelClothes.itemOptions) {
                        int optId = io.optionTemplate.id;
                        if (optId == 41) {
                            canUpgrade = false;
                        }
                    }
                    if (angelClothes.template.gender != craftingRecipe.template.gender) {
                        this.whis.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Vui lòng chọn đúng công thức chế tạo",
                                "Đóng");
                        return;
                    }
                    if (canUpgrade) {
                        if (craftingRecipe != null) {
                            if (countLuckyStone < 2 && countUpgradeStone < 2) {
                                int ratioTotal = (20 + ratioUpgradeStone + ratioRecipe);
                                int ratio = ratioTotal > 75 ? ratio = 75 : ratioTotal;
                                String npcSay = "|1| Nâng Cấp " + angelClothes.template.name + "\n|7|";
                                npcSay += ratioRecipe > 0 ? " Công thức VIP (+" + ratioRecipe + "% tỉ lệ thành công)\n"
                                        : "";
                                npcSay += ratioUpgradeStone > 0
                                        ? "Đá Nâng Cấp cấp " + ratioUpgradeStone / 10 + " (+" + ratioUpgradeStone
                                        + "% tỉ lệ thành công)\n"
                                        : "";
                                npcSay += ratioLuckyStone > 0
                                        ? "Đá nâng may mắn cấp " + ratioLuckyStone / 10 + " (+" + ratioLuckyStone
                                        + "% tỉ lệ tối đa các Chỉ Số)\n"
                                        : "";
                                npcSay += "Tỉ lệ thành công: " + ratio + "%\n";
                                npcSay += "Phí Nâng Cấp: " + Util.numberToMoney(COST_DAP_DO_KICH_HOAT) + " vàng";
                                if (player.inventory.gold >= COST_DAP_DO_KICH_HOAT) {
                                    this.whis.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay, "Nâng Cấp");
                                } else {
                                    this.whis.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay,
                                            "Còn thiếu\n"
                                            + Util.numberToMoney(player.inventory.gold - COST_DAP_DO_KICH_HOAT)
                                            + " vàng");
                                }
                            } else {
                                this.whis.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                        "Chỉ có thể sự dụng tối đa 1 loại Nâng Cấp và Đá May Mắn", "Đóng");
                            }
                        } else {
                            this.whis.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                    "Người cần ít nhất 1 trang bị thiên sứ và 1 công thức để có thể Nâng Cấp", "Đóng");
                        }
                    } else {
                        this.whis.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Mỗi vật phẩm chỉ có thể Nâng Cấp 1 lần", "Đóng");
                    }
                } else {
                    this.whis.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Người cần ít nhất 1 trang bị thiên sứ và 1 công thức để có thể Nâng Cấp", "Đóng");
                }
                break;

            case NANG_CAP_CHAN_MENH:
                if (player.combineNew.itemsCombine.size() == 2) {
                    Item bongTai = null;
                    Item manhVo = null;
                    int star = 0;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (item.template.id == 674) {
                            manhVo = item;
                        } else if (item.template.id >= 1599 && item.template.id <= 1606) {
                            bongTai = item;
                            star = item.template.id - 1599;
                        }
                    }
                    if (bongTai != null && bongTai.template.id == 1606) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Cánh đã đạt cấp tối đa", "Đóng");
                        return;
                    }
                    player.combineNew.DiemNangcap = getDiemNangcapChanmenh(star);
                    player.combineNew.DaNangcap = dnsdapdo(star);
                    player.combineNew.TileNangcap = getTiLeNangcapChanmenh(star);
                    if (bongTai != null && manhVo != null && (bongTai.template.id >= 1599 && bongTai.template.id < 1606)) {
                        String npcSay = bongTai.template.name + "\n|2|";
                        for (ItemOption io : bongTai.itemOptions) {
                            npcSay += io.getOptionString() + "\n";
                        }
                        npcSay += "|7|Tỉ lệ thành công: " + player.combineNew.TileNangcap + "%" + "\n";
                        if (player.combineNew.DiemNangcap <= player.diemfam) {
                            npcSay += "|1|Cần " + Util.numberToMoney(player.combineNew.DiemNangcap) + " Điểm TrainFam";
                            baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                    "Nâng Cấp\ncần " + player.combineNew.DaNangcap + " Đá Ngũ Sắc");
                        } else {
                            npcSay += "Còn thiếu " + Util.numberToMoney(player.combineNew.DiemNangcap - player.diemfam) + " Điểm TrainFam";
                            baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Cần 1 Cánh Lv1 và Đá Ngũ Sắc", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Cần 1 Cánh Lv 1 và Đá Ngũ Sắc", "Đóng");
                }
                break;
        }
    }

    /**
     * Bắt đầu đập đồ - điều hướng từng loại đập đồ
     *
     * @param player
     */
    public void startCombine(Player player) {
        if (Util.canDoWithTime(player.combineNew.lastTimeCombine, TIME_COMBINE)) {
            if (false) {
                Service.getInstance().sendThongBao(player, "Tính năng đang tạm khóa");
                return;
            }
            switch (player.combineNew.typeCombine) {
                case NANG_CAP_DO_KICH_HOAT:
                    dapDoKichHoat(player);
                    break;
                case MO_CHI_SO_CAI_TRANG:
                    moChiSoCaiTrang(player);
                    break;
                case EP_CAI_TRANG:
                    epcaiTrang(player);
                    break;
                case DAP_SET_KICH_HOAT_CAO_CAP:
                    dapDoKichHoatCaoCap1(player);
                    break;
                case DAP_SET_KICH_HOAT:
                    dapDoKichHoat1(player);
                    break;
                case NANG_CAP_CHAN_MENH:
                    nangCapChanMenh(player);
                case TRADE_DO_THAN_LINH:
                    tradeDoThanLinh(player);
                case TRADE_DO_THAN_LINH1:
                    tradeDoThanLinh1(player);
                case EP_SAO_TRANG_BI:
                    epSaoTrangBi(player);
                    break;
                case PHA_LE_HOA_TRANG_BI:
                    phaLeHoaTrangBi(player);
                    break;
                case NANG_CAP_DO_TS:
                    openDTS(player);
                    break;
                case NHAP_NGOC_RONG:
                    nhapNgocRong(player);
                    break;
                case NANG_CAP_LEVEL_SKH:
                    nangCapLEVELSKH(player);
                    break;
                case NANG_CAP_VAT_PHAM:
                    nangCapVatPham(player);
                    break;
                case CHUC_PHUC_SD:
                    chucPhucSd(player);
                    break;
                case CHUC_PHUC_HP:
                    chucPhucHp(player);
                    break;
                case CHUC_PHUC_KI:
                    chucPhucKi(player);
                    break;
                case CHUC_PHUC_DEF:
                    chucPhucDef(player);
                    break;
                case DOI_VE_HUY_DIET:
                    doiVeHuyDiet(player);
                    break;
                case GIA_HAN_CAI_TRANG:
                    giaHanCaiTrang(player);
                    break;
                case NANG_CAP_DO_THIEN_SU:
                    nangCapDoThienSu(player);
                    break;
                case XOA_SPL:
                    xoaspl(player);
                    break;

//                case PHA_LE_HOA_TRANG_BI_X10:
//                    phaLeHoaTrangBiX10(player);
//                    break;
                case NANG_CAP_BONG_TAI:
                    nangCapBongTai(player);
                    break;
                case MO_CHI_SO_BONG_TAI:
                    moChiSoBongTai(player);
                    break;
                //Sách Tuyệt Kỹ
                case GIAM_DINH_SACH:
                    giamDinhSach(player);
                    break;
                case TAY_SACH:
                    taySach(player);
                    break;
                case NANG_CAP_SACH_TUYET_KY:
                    nangCapSachTuyetKy(player);
                    break;
                case PHUC_HOI_SACH:
                    phucHoiSach(player);
                    break;
                case PHAN_RA_SACH:
                    phanRaSach(player);
                    break;
                case BONG_TOI_TRANG_BI:
                    BongtoiTrangBi(player);
                    break;
                case XOA_DONG_TRANG_BI:
                    XoadongTrangBi(player);
                    break;

            }
            player.iDMark.setIndexMenu(ConstNpc.IGNORE_MENU);
            player.combineNew.clearParamCombine();
            player.combineNew.lastTimeCombine = System.currentTimeMillis();
        }
    }

//    private void phaLeHoaTrangBiX10(Player player) {
//        for (int i = 0; i < 5; i++) { // số lần pha lê hóa
//            if (phaLeHoaTrangBi(player)) {
//                Service.getInstance().sendThongBao(player,
//                        "Chúc mừng bạn đã pha lê hóa thành công,tổng số lần Nâng Cấp là: " + i);
//                break;
//            }
//        }
//    }
    //sách
    private void giamDinhSach(Player player) {
        if (player.combineNew.itemsCombine.size() == 2) {

            Item sachTuyetKy = null;
            Item buaGiamDinh = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (issachTuyetKy(item)) {
                    sachTuyetKy = item;
                } else if (item.template.id == 1423) {
                    buaGiamDinh = item;
                }
            }
            if (sachTuyetKy != null && buaGiamDinh != null) {
                Item sachTuyetKy_2 = ItemService.gI().createNewItem((short) sachTuyetKy.template.id);
                if (checkHaveOption(sachTuyetKy, 0, 245)) {
                    int tyle = new Random().nextInt(10);
                    if (tyle >= 0 && tyle <= 33) {
                        sachTuyetKy_2.itemOptions.add(new ItemOption(50, new Util().nextInt(5, 150)));
                    } else if (tyle > 33 && tyle <= 66) {
                        sachTuyetKy_2.itemOptions.add(new ItemOption(77, new Util().nextInt(10, 150)));
                    } else {
                        sachTuyetKy_2.itemOptions.add(new ItemOption(103, new Util().nextInt(10, 150)));
                    }
                    for (int i = 1; i < sachTuyetKy.itemOptions.size(); i++) {
                        sachTuyetKy_2.itemOptions.add(new ItemOption(sachTuyetKy.itemOptions.get(i).optionTemplate.id, sachTuyetKy.itemOptions.get(i).param));
                    }
                    sendEffectSuccessCombine(player);
                    InventoryService.gI().addItemBag(player, sachTuyetKy_2, 1);
                    InventoryService.gI().subQuantityItemsBag(player, sachTuyetKy, 1);
                    InventoryService.gI().subQuantityItemsBag(player, buaGiamDinh, 1000);
                    InventoryService.gI().sendItemBags(player);
                    reOpenItemCombine(player);
                } else {
                    Service.getInstance().sendThongBao(player, "Không còn lượt giám định");
                    return;
                }
            }
        }
    }

    private void nangCapSachTuyetKy(Player player) {
        if (player.combineNew.itemsCombine.size() == 2) {

            Item sachTuyetKy = null;
            Item kimBamGiay = null;

            for (Item item : player.combineNew.itemsCombine) {
                if (issachTuyetKy(item)) {
                    sachTuyetKy = item;
                } else if (item.template.id == 1422) {
                    kimBamGiay = item;
                }
            }
            Item sachTuyetKy_2 = ItemService.gI().createNewItem((short) ((short) sachTuyetKy.template.id + 1));
            if (sachTuyetKy != null && kimBamGiay != null) {
                if (kimBamGiay.quantity < 999) {
                    Service.getInstance().sendThongBao(player, "Không đủ Kìm bấm giấy mà đòi Nâng Cấp");
                    return;
                }
                if (checkHaveOption(sachTuyetKy, 0, 245)) {
                    Service.getInstance().sendThongBao(player, "Chưa giám định mà đòi Nâng Cấp");
                    return;
                }
                for (int i = 0; i < sachTuyetKy.itemOptions.size(); i++) {
                    sachTuyetKy_2.itemOptions.add(new ItemOption(sachTuyetKy.itemOptions.get(i).optionTemplate.id, sachTuyetKy.itemOptions.get(i).param));
                }
                sendEffectSuccessCombine(player);
                InventoryService.gI().addItemBag(player, sachTuyetKy_2, 1);
                InventoryService.gI().subQuantityItemsBag(player, sachTuyetKy, 1);
                InventoryService.gI().subQuantityItemsBag(player, kimBamGiay, 999);
                InventoryService.gI().sendItemBags(player);
                reOpenItemCombine(player);

            }
        }
    }

    private void nangCapChanMenh(Player player) {
        if (player.combineNew.itemsCombine.size() == 2) {
            int diem = player.combineNew.DiemNangcap;
            if (player.diemfam < diem) {
                Service.getInstance().sendThongBao(player, "Không đủ Điểm TrainFam để thực hiện");
                return;
            }
            Item chanmenh = null;
            Item dahoangkim = null;
            int capbac = 0;
            for (Item item : player.combineNew.itemsCombine) {
                if (item.template.id == 674) {
                    dahoangkim = item;
                } else if (item.template.id >= 1599 && item.template.id < 1606) {
                    chanmenh = item;
                    capbac = item.template.id - 1598;
                }
            }
            int soluongda = player.combineNew.DaNangcap;
            if (dahoangkim != null && dahoangkim.quantity >= soluongda) {
                if (chanmenh != null && (chanmenh.template.id >= 1599 && chanmenh.template.id < 1606)) {
                    player.diemfam -= diem;
                    if (Util.isTrue(player.combineNew.TileNangcap, 1)) {
                        InventoryService.gI().subQuantityItemsBag(player, dahoangkim, soluongda);
                        sendEffectSuccessCombine(player);
                        switch (capbac) {
                            case 1:
                                chanmenh.template = ItemService.gI().getTemplate(chanmenh.template.id + 1);
                                chanmenh.itemOptions.clear();
                                chanmenh.itemOptions.add(new ItemOption(234, 2));
                                chanmenh.itemOptions.add(new ItemOption(50, Util.nextInt(0, 50)));
                                chanmenh.itemOptions.add(new ItemOption(77, Util.nextInt(0, 50)));
                                chanmenh.itemOptions.add(new ItemOption(103, Util.nextInt(0, 50)));
                                chanmenh.itemOptions.add(new ItemOption(201, Util.nextInt(0, 50)));
                                chanmenh.itemOptions.add(new ItemOption(202, Util.nextInt(0, 50)));
                                chanmenh.itemOptions.add(new ItemOption(203, Util.nextInt(0, 50)));
                                chanmenh.itemOptions.add(new ItemOption(30, 1));
                                break;
                            case 2:
                                chanmenh.template = ItemService.gI().getTemplate(chanmenh.template.id + 1);
                                chanmenh.itemOptions.clear();
                                chanmenh.itemOptions.add(new ItemOption(234, 2));
                                chanmenh.itemOptions.add(new ItemOption(50, Util.nextInt(0, 100)));
                                chanmenh.itemOptions.add(new ItemOption(77, Util.nextInt(0, 100)));
                                chanmenh.itemOptions.add(new ItemOption(103, Util.nextInt(0, 100)));
                                chanmenh.itemOptions.add(new ItemOption(201, Util.nextInt(0, 100)));
                                chanmenh.itemOptions.add(new ItemOption(202, Util.nextInt(0, 100)));
                                chanmenh.itemOptions.add(new ItemOption(203, Util.nextInt(0, 100)));
                                chanmenh.itemOptions.add(new ItemOption(30, 1));
                                break;
                            case 3:
                                chanmenh.template = ItemService.gI().getTemplate(chanmenh.template.id + 1);
                                chanmenh.itemOptions.clear();
                                chanmenh.itemOptions.add(new ItemOption(234, 2));
                                chanmenh.itemOptions.add(new ItemOption(50, Util.nextInt(0, 150)));
                                chanmenh.itemOptions.add(new ItemOption(77, Util.nextInt(0, 150)));
                                chanmenh.itemOptions.add(new ItemOption(103, Util.nextInt(0, 150)));
                                chanmenh.itemOptions.add(new ItemOption(201, Util.nextInt(0, 150)));
                                chanmenh.itemOptions.add(new ItemOption(202, Util.nextInt(0, 150)));
                                chanmenh.itemOptions.add(new ItemOption(203, Util.nextInt(0, 150)));
                                chanmenh.itemOptions.add(new ItemOption(30, 1));
                                break;
                            case 4:
                                chanmenh.template = ItemService.gI().getTemplate(chanmenh.template.id + 1);
                                chanmenh.itemOptions.clear();
                                chanmenh.itemOptions.add(new ItemOption(234, 5));
                                chanmenh.itemOptions.add(new ItemOption(50, Util.nextInt(0, 200)));
                                chanmenh.itemOptions.add(new ItemOption(77, Util.nextInt(0, 200)));
                                chanmenh.itemOptions.add(new ItemOption(103, Util.nextInt(0, 200)));
                                chanmenh.itemOptions.add(new ItemOption(201, Util.nextInt(0, 200)));
                                chanmenh.itemOptions.add(new ItemOption(202, Util.nextInt(0, 200)));
                                chanmenh.itemOptions.add(new ItemOption(203, Util.nextInt(0, 200)));
                                chanmenh.itemOptions.add(new ItemOption(30, 1));
                                break;
                            case 5:
                                chanmenh.template = ItemService.gI().getTemplate(chanmenh.template.id + 1);
                                chanmenh.itemOptions.clear();
                                chanmenh.itemOptions.add(new ItemOption(234, 7));
                                chanmenh.itemOptions.add(new ItemOption(50, Util.nextInt(0, 250)));
                                chanmenh.itemOptions.add(new ItemOption(77, Util.nextInt(0, 250)));
                                chanmenh.itemOptions.add(new ItemOption(103, Util.nextInt(0, 250)));
                                chanmenh.itemOptions.add(new ItemOption(201, Util.nextInt(0, 250)));
                                chanmenh.itemOptions.add(new ItemOption(202, Util.nextInt(0, 250)));
                                chanmenh.itemOptions.add(new ItemOption(203, Util.nextInt(0, 250)));
                                chanmenh.itemOptions.add(new ItemOption(30, 1));
                                break;
                            case 6:
                                chanmenh.template = ItemService.gI().getTemplate(chanmenh.template.id + 1);
                                chanmenh.itemOptions.clear();
                                chanmenh.itemOptions.add(new ItemOption(234, 7));
                                chanmenh.itemOptions.add(new ItemOption(50, Util.nextInt(0, 300)));
                                chanmenh.itemOptions.add(new ItemOption(77, Util.nextInt(0, 300)));
                                chanmenh.itemOptions.add(new ItemOption(103, Util.nextInt(0, 300)));
                                chanmenh.itemOptions.add(new ItemOption(201, Util.nextInt(0, 300)));
                                chanmenh.itemOptions.add(new ItemOption(202, Util.nextInt(0, 300)));
                                chanmenh.itemOptions.add(new ItemOption(203, Util.nextInt(0, 300)));
                                chanmenh.itemOptions.add(new ItemOption(30, 1));
                                break;
                            case 7:
                                chanmenh.template = ItemService.gI().getTemplate(chanmenh.template.id + 1);
                                chanmenh.itemOptions.clear();
                                chanmenh.itemOptions.add(new ItemOption(234, 7));
                                chanmenh.itemOptions.add(new ItemOption(50, Util.nextInt(0, 350)));
                                chanmenh.itemOptions.add(new ItemOption(77, Util.nextInt(0, 350)));
                                chanmenh.itemOptions.add(new ItemOption(103, Util.nextInt(0, 350)));
                                chanmenh.itemOptions.add(new ItemOption(201, Util.nextInt(0, 350)));
                                chanmenh.itemOptions.add(new ItemOption(202, Util.nextInt(0, 350)));
                                chanmenh.itemOptions.add(new ItemOption(203, Util.nextInt(0, 350)));
                                chanmenh.itemOptions.add(new ItemOption(30, 1));
                                break;
                            case 8:
                                chanmenh.template = ItemService.gI().getTemplate(chanmenh.template.id + 1);
                                chanmenh.itemOptions.clear();
                                chanmenh.itemOptions.add(new ItemOption(234, 7));
                                chanmenh.itemOptions.add(new ItemOption(50, Util.nextInt(0, 400)));
                                chanmenh.itemOptions.add(new ItemOption(77, Util.nextInt(0, 400)));
                                chanmenh.itemOptions.add(new ItemOption(103, Util.nextInt(0, 400)));
                                chanmenh.itemOptions.add(new ItemOption(201, Util.nextInt(0, 400)));
                                chanmenh.itemOptions.add(new ItemOption(202, Util.nextInt(0, 400)));
                                chanmenh.itemOptions.add(new ItemOption(203, Util.nextInt(0, 400)));
                                chanmenh.itemOptions.add(new ItemOption(30, 1));
                                break;
                            case 9:
                                chanmenh.template = ItemService.gI().getTemplate(chanmenh.template.id + 1);
                                chanmenh.itemOptions.clear();
                                chanmenh.itemOptions.add(new ItemOption(234, 7));
                                chanmenh.itemOptions.add(new ItemOption(50, Util.nextInt(0, 500)));
                                chanmenh.itemOptions.add(new ItemOption(77, Util.nextInt(0, 500)));
                                chanmenh.itemOptions.add(new ItemOption(103, Util.nextInt(0, 500)));
                                chanmenh.itemOptions.add(new ItemOption(201, Util.nextInt(0, 500)));
                                chanmenh.itemOptions.add(new ItemOption(202, Util.nextInt(0, 500)));
                                chanmenh.itemOptions.add(new ItemOption(203, Util.nextInt(0, 500)));
                                chanmenh.itemOptions.add(new ItemOption(30, 1));
                                break;

                            default:
                                break;
                        }
                    } else {
                        InventoryService.gI().subQuantityItemsBag(player, dahoangkim, soluongda);
                        sendEffectFailCombine(player);
                    }
                    InventoryService.gI().sendItemBags(player);
                    Service.getInstance().sendMoney(player);
                    reOpenItemCombine(player);
                }
            } else {
                Service.getInstance().sendThongBao(player, "Không đủ Đá Ngũ Sắc để thực hiện");
            }
        }
    }

    private void phucHoiSach(Player player) {
        if (player.combineNew.itemsCombine.size() == 1) {
            Item cuonSachCu = InventoryService.gI().findItemBagByTemp(player, (short) 1424);
            long goldPhanra = 10_000_000_000l;
            Item sachTuyetKy = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (issachTuyetKy(item)) {
                    sachTuyetKy = item;
                }
            }
            if (sachTuyetKy != null) {
                double doBen = 0;
                ItemOption optionLevel = null;
                for (ItemOption io : sachTuyetKy.itemOptions) {
                    if (io.optionTemplate.id == 243) {
                        doBen = io.param;
                        optionLevel = io;
                        break;
                    }
                }
                if (cuonSachCu == null) {
                    Service.getInstance().sendThongBaoOK(player, "Cần sách tuyệt kỹ và 1000 cuốn sách cũ");
                    return;
                }
                if (cuonSachCu.quantity < 1000) {
                    Service.getInstance().sendThongBaoOK(player, "Cần sách tuyệt kỹ và 1000 cuốn sách cũ");
                    return;
                }
                if (player.inventory.gold < goldPhanra) {
                    Service.getInstance().sendThongBao(player, "Không có tiền mà đòi phục hồi à");
                    return;
                }
                if (doBen != 1000) {
                    for (int i = 0; i < sachTuyetKy.itemOptions.size(); i++) {
                        if (sachTuyetKy.itemOptions.get(i).optionTemplate.id == 243) {
                            sachTuyetKy.itemOptions.get(i).param = 1000;
                            break;
                        }
                    }
                    player.inventory.gold -= 10_000_000_000l;
                    InventoryService.gI().subQuantityItemsBag(player, cuonSachCu, 1000);
                    InventoryService.gI().sendItemBags(player);
                    Service.getInstance().sendMoney(player);
                    sendEffectSuccessCombine(player);
                    reOpenItemCombine(player);
                } else {
                    Service.getInstance().sendThongBao(player, "Còn dùng được phục hồi ăn cứt à");
                    return;
                }
            }
        }
    }

    private void phanRaSach(Player player) {
        if (player.combineNew.itemsCombine.size() == 1) {
            Item cuonSachCu = ItemService.gI().createNewItem((short) 1424, 500);
            long goldPhanra = 10_000_000_000l;
            Item sachTuyetKy = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (issachTuyetKy(item)) {
                    sachTuyetKy = item;
                }
            }
            if (sachTuyetKy != null) {
                double luotTay = 0;
                ItemOption optionLevel = null;
                for (ItemOption io : sachTuyetKy.itemOptions) {
                    if (io.optionTemplate.id == 242) {
                        luotTay = io.param;
                        optionLevel = io;
                        break;
                    }
                }
                if (player.inventory.gold < goldPhanra) {
                    Service.getInstance().sendThongBao(player, "Không Đủ Tiền Để Phân Rã");
                    return;
                }
                if (luotTay == 0) {

                    player.inventory.gold -= goldPhanra;
                    InventoryService.gI().subQuantityItemsBag(player, sachTuyetKy, 1);
                    InventoryService.gI().addItemBag(player, cuonSachCu, 9);
                    InventoryService.gI().sendItemBags(player);
                    Service.getInstance().sendMoney(player);
                    sendEffectSuccessCombine(player);
                    reOpenItemCombine(player);

                } else {
                    Service.getInstance().sendThongBao(player, "Không Phân Rã Khi Vẫn Dùng Được");
                    return;
                }
            }
        }
    }

    private void taySach(Player player) {
        if (player.combineNew.itemsCombine.size() == 1) {
            Item sachTuyetKy = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (issachTuyetKy(item)) {
                    sachTuyetKy = item;
                }
            }
            if (sachTuyetKy != null) {
                double luotTay = 0;
                ItemOption optionLevel = null;
                for (ItemOption io : sachTuyetKy.itemOptions) {
                    if (io.optionTemplate.id == 242) {
                        luotTay = io.param;
                        optionLevel = io;
                        break;
                    }
                }
                if (luotTay == 0) {
                    Service.getInstance().sendThongBao(player, "Không Còn Lượt Tẩy");
                    return;
                }
                Item sachTuyetKy_2 = ItemService.gI().createNewItem((short) sachTuyetKy.template.id);
                if (checkHaveOption(sachTuyetKy, 0, 245)) {
                    Service.getInstance().sendThongBao(player, "Không Còn Lượt Tẩy");
                    return;
                }
                int tyle = new Random().nextInt(10);
                for (int i = 1; i < sachTuyetKy.itemOptions.size(); i++) {
                    if (sachTuyetKy.itemOptions.get(i).optionTemplate.id == 242) {
                        sachTuyetKy.itemOptions.get(i).param -= 1;
                    }
                }
                sachTuyetKy_2.itemOptions.add(new ItemOption(245, 0));
                for (int i = 1; i < sachTuyetKy.itemOptions.size(); i++) {
                    sachTuyetKy_2.itemOptions.add(new ItemOption(sachTuyetKy.itemOptions.get(i).optionTemplate.id, sachTuyetKy.itemOptions.get(i).param));
                }
                sendEffectSuccessCombine(player);
                InventoryService.gI().addItemBag(player, sachTuyetKy_2, 1);
                InventoryService.gI().subQuantityItemsBag(player, sachTuyetKy, 1);
                InventoryService.gI().sendItemBags(player);
                reOpenItemCombine(player);
            }
        }
    }

    //--end--
    private void tradeDoThanLinh1(Player player) {
        if (player.combineNew.itemsCombine.size() != 1) {
            Service.getInstance().sendThongBao(player, "Thiếu Đồ");
            return;
        }
        if (player.inventory.ruby < COST_RUBY_TRADE_TL1) {
            Service.getInstance().sendThongBao(player, "Nạp tiền vào donate cho taooo!");
            return;
        }
        if (InventoryService.gI().getCountEmptyBag(player) < 1) {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít Nhất 1 ô trống trong hành trang");
            return;
        }
        if (player.combineNew.itemsCombine.size() == 1) {
            Item doTL = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (item.isNotNullItem()) {
                    if (item.template.id >= 555 && item.template.id <= 567) {
                        doTL = item;
                    }
                }
            }
            if (doTL == null) {
                Service.getInstance().sendThongBao(player, "Thiếu Đồ!!");
                return;
            }
            if (doTL.quantity < 1) {
                Service.getInstance().sendThongBao(player, "Cần x1 món đồ thần linh bất kì!");
                return;
            }
            if (doTL != null && doTL.quantity >= 1) {
                Item itemMTL = ItemService.gI().createNewItem((short) 1518, 1);
                sendEffectSuccessCombineDoTS(player, itemMTL.template.iconID);
                InventoryService.gI().addItemBag(player, itemMTL, 0);
                InventoryService.gI().subQuantityItemsBag(player, doTL, 1);
                player.inventory.ruby -= COST_RUBY_TRADE_TL1;
                InventoryService.gI().sendItemBags(player);
                Service.getInstance().sendMoney(player);
                player.combineNew.itemsCombine.clear();
                reOpenItemCombine(player);
            }
        }

    }

 private void epcaiTrang(Player player) {
    // Kiểm tra Số Lượng item ghép có đúng 2 không
    if (player.combineNew.itemsCombine.size() != 2) {
        Service.getInstance().sendThongBao(player, "Thiếu nguyên liệu để Nâng Cấp");
        return;
    }
    
    // Đếm Số Lượng cải trang có option 167 và 168
    int ct1 = (int) player.combineNew.itemsCombine.stream()
            .filter(item -> item.isNotNullItem() && item.haveOption(167) && item.isTrangBiCaitrang())
            .count();
    int ct2 = (int) player.combineNew.itemsCombine.stream()
            .filter(item -> item.isNotNullItem() && item.haveOption(168) && item.isTrangBiCaitrang())
            .count();
    
    // Phải có đúng 1 món có option 167 và 1 món có option 168
    if (ct1 != 1 && ct2 != 1) {
        Service.getInstance().sendThongBao(player, "Thiếu Cải trang Ghép để nâng");
        return;
    }
    
    // Kiểm tra có ít nhất 1 ô trống hành trang
    if (InventoryService.gI().getCountEmptyBag(player) > 1) {
        // Kiểm tra vàng của người chơi có đủ không
        if (player.inventory.gold < COST) {
            Service.getInstance().sendThongBao(player, "Con cần thêm vàng để Nâng trang bị...");
            return;
        }
        
        // Lấy cải trang 1 (option 167) và cải trang 2 (option 168)
        Item caiTrang1 = null;
        Item caiTrang2 = null;
        
        if (caiTrang1 == null && player.combineNew.itemsCombine.stream().anyMatch(Item::isTrangBiCaitrang)) {
            caiTrang1 = player.combineNew.itemsCombine.stream()
                    .filter(item -> item.isNotNullItem() && item.haveOption(167))
                    .findFirst().get();
        }
        if (caiTrang2 == null && player.combineNew.itemsCombine.stream().anyMatch(Item::isTrangBiCaitrang)) {
            caiTrang2 = player.combineNew.itemsCombine.stream()
                    .filter(item -> item.isNotNullItem() && item.haveOption(168) && item.isTrangBiCaitrang())
                    .findFirst().get();
        }
           if (caiTrang1.haveOption(169)) {
            Service.getInstance().sendThongBao(player, "Trang bị đã đạt tối đa Nâng Cấp, không thể ghép thêm!");
            return;
        }
        
        // Nếu cải trang 2 đã có option 93 thì không cho ghép
        if (caiTrang2.haveOption(93)) {
            Service.getInstance().sendThongBao(player, "Ép Cải Trang Này Là Mất Đó");
            return;
        }
        
        if (caiTrang1 == null && caiTrang2 == null) {
            Service.getInstance().sendThongBao(player, "Thiếu cải trang để Nâng Cấp");
            return;
        }
        
        // Xác định tỉ lệ thành công (hiện tại là 100%)
        if (Util.isTrue(100, 100)) {
            sendEffectSuccessCombine(player);
            
            if (caiTrang1 != null && caiTrang2 != null) {
                // Lưu lại id của cải trang 2 (theo dõi đã ghép)
                player.listAddCaiTrang.add((int) caiTrang2.template.id);
                
                // Gộp các option từ cải trang 2 sang cải trang 1
                for (ItemOption itopt2 : caiTrang2.itemOptions) {
                    boolean exists = false;
                    for (ItemOption itopt1 : caiTrang1.itemOptions) {
                        if (itopt1.optionTemplate.id == itopt2.optionTemplate.id) {
                            itopt1.param += itopt2.param;
                            exists = true;
                            break;
                        }
                    }
                    // Nếu chưa tồn tại option tương ứng và không phải các option bị loại trừ thì thêm mới
                    if (!exists && itopt2.optionTemplate.id != 168 
                        && itopt2.optionTemplate.id != 107 
                        && itopt2.optionTemplate.id != 102 
                        && itopt2.optionTemplate.id != 101) {
                        caiTrang1.itemOptions.add(new ItemOption(itopt2.optionTemplate.id, itopt2.param));
                    }
                }
                
                // Thêm luôn dòng option 169 sau khi ghép thành công
                // Kiểm tra xem nếu chưa có option 169 thì mới thêm
                if (!caiTrang1.haveOption(169)) {
                    caiTrang1.itemOptions.add(new ItemOption(169, 10));
                }
            }
            
            Service.getInstance().sendThongBao(player, "Bạn đã Nâng Cấp trang bị thành công");
            // Trừ cải trang 2 khỏi hành trang
            InventoryService.gI().subQuantityItemsBag(player, caiTrang2, 1);
        } else {
            sendEffectFailCombine(player);
        }
        
        InventoryService.gI().sendItemBags(player);
        Service.getInstance().sendMoney(player);
        reOpenItemCombine(player);
    } else {
        Service.getInstance().sendThongBao(player, "Bạn phải có ít Nhất 1 ô trống trong hành trang");
    }
}


    public void moChiSoCaiTrang(Player player) {
        if (player.combineNew.itemsCombine.size() != 2) {
            Service.getInstance().sendThongBao(player, "Thiếu nguyên liệu");
            return;
        }
        if (player.combineNew.itemsCombine.stream()
                .filter(item -> item.isNotNullItem() && (item.template.type == 5))
                .count() != 1) {
            Service.getInstance().sendThongBao(player, "Thiếu cải trang kích hoạt Hiếm");
            return;
        }
        if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.id == 1594)
                .count() != 1) {
            Service.getInstance().sendThongBao(player, "Thiếu Lưu Ly");
            return;
        }
        ///

        Item caiTrang = null;
        Item longDen = null;
        int checkOption = 0;
        for (Item item : player.combineNew.itemsCombine) {
            if (item.template.type == 5) {
                caiTrang = item;
            } else if (item.template.id == 1594) {
                longDen = item;
            }
        }
        for (ItemOption io : caiTrang.itemOptions) {
            if (io.optionTemplate.id == 240) {
                checkOption++;
            } else if (io.optionTemplate.id == 241) {
                checkOption = 0;
            }
        }
        if (checkOption == 0) {
            Service.getInstance().sendThongBao(player, "Cần Cải Trang chưa kích hoạt!");
            return;
        }

        if (caiTrang != null && longDen != null && longDen.quantity >= 10) {
            InventoryService.gI().subQuantityItemsBag(player, longDen, 10);
            caiTrang.itemOptions.clear();
            caiTrang.itemOptions.add(new ItemOption(50, Util.nextInt(1, 500)));
            caiTrang.itemOptions.add(new ItemOption(77, Util.nextInt(1, 500)));
            caiTrang.itemOptions.add(new ItemOption(103, Util.nextInt(1, 500)));
            caiTrang.itemOptions.add(new ItemOption(94, Util.nextInt(1, 500)));
            caiTrang.itemOptions.add(new ItemOption(14, Util.nextInt(1, 10)));
            caiTrang.itemOptions.add(new ItemOption(16, Util.nextInt(1, 10)));
            caiTrang.itemOptions.add(new ItemOption(5, Util.nextInt(1, 10)));
            caiTrang.itemOptions.add(new ItemOption(101, Util.nextInt(1, 500)));
            caiTrang.itemOptions.add(new ItemOption(30, Util.nextInt(1, 20)));
            if (Util.isTrue(99, 100)) {
                caiTrang.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
                sendEffectSuccessCombine(player);
                InventoryService.gI().sendItemBags(player);
                Service.getInstance().sendMoney(player);
                reOpenItemCombine(player);
            } else {
                Service.getInstance().sendThongBao(player, "Không đủ nguyên liệu Nâng Cấp!");
                reOpenItemCombine(player);
            }

            ///
        }
    }

    private void nangCapBongTai(Player player) {
        if (player.combineNew.itemsCombine.size() == 2) {
            int gold = player.combineNew.goldCombine;
            if (player.inventory.gold < gold) {
                Service.getInstance().sendThongBao(player, "Không đủ vàng để thực hiện");
                return;
            }
            int gem = player.combineNew.gemCombine;
            if (player.inventory.gem < gem) {
                Service.getInstance().sendThongBao(player, "Không đủ ngọc để thực hiện");
                return;
            }
            Item bongtai = null;
            Item manhvobt = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (checkbongtai(item)) {
                    bongtai = item;
                } else if (item.template.id == 933) {
                    manhvobt = item;
                }
            }
            if (bongtai != null && manhvobt != null) {
                int level = 0;
                for (ItemOption io : bongtai.itemOptions) {
                    if (io.optionTemplate.id == 72) {
                        level = (int) io.param;
                        break;
                    }
                }
                if (level < 3) {
                    int lvbt = lvbt(bongtai);
                    int countmvbt = getcountmvbtnangbt(lvbt);
                    if (countmvbt > manhvobt.quantity) {
                        Service.getInstance().sendThongBao(player, "Không Đủ Mảnh Vỡ Bông Tai");
                        return;
                    }
                    player.inventory.gold -= gold;
                    player.inventory.gem -= gem;

                    if (Util.isTrue(player.combineNew.ratioCombine, 130)) {
                        bongtai.template = ItemService.gI().getTemplate(getidbtsaukhilencap(lvbt));
                        bongtai.itemOptions.clear();
                        bongtai.itemOptions.add(new ItemOption(72, lvbt + 1));

                        sendEffectSuccessCombine(player);
                        InventoryService.gI().subQuantityItemsBag(player, manhvobt, 9999);
                    } else {
                        sendEffectFailCombine(player);
                        InventoryService.gI().subQuantityItemsBag(player, manhvobt, 999);
                    }
                    InventoryService.gI().sendItemBags(player);
                    Service.getInstance().sendMoney(player);
                    reOpenItemCombine(player);
                }
            }
        }
    }

    private short getidbtsaukhilencap(int lvbtcu) {
        switch (lvbtcu) {
            case 1:
                return 1624;
            case 2:
                return 1625;
        }
        return 0;
    }

    private void moChiSoBongTai(Player player) {
        if (player.combineNew.itemsCombine.size() == 3) {
            int gold = player.combineNew.goldCombine;
            int gem = player.combineNew.gemCombine;

            if (player.inventory.gold < gold) {
                Service.getInstance().sendThongBao(player, "Không đủ vàng để thực hiện");
                return;
            }
            if (player.inventory.gem < gem) {
                Service.getInstance().sendThongBao(player, "Không đủ ngọc để thực hiện");
                return;
            }

            Item bongTai = null, manhHon = null, daXanhLam = null;
            for (Item item : player.combineNew.itemsCombine) {
                switch (item.template.id) {
                    case 1624:
                    case 1625:
                        bongTai = item;
                        break;
                    case 934:
                        manhHon = item;
                        break;
                    case 935:
                        daXanhLam = item;
                        break;
                }
            }

            if (bongTai != null && daXanhLam != null && manhHon != null && manhHon.quantity >= 9999) {
                // Trừ tài nguyên
                player.inventory.gold -= gold;
                player.inventory.gem -= gem;
                InventoryService.gI().subQuantityItemsBag(player, manhHon, 9999);
                InventoryService.gI().subQuantityItemsBag(player, daXanhLam, 1000);

                // Kiểm tra tỷ lệ thành công
                if (Util.isTrue(player.combineNew.ratioCombine, 50)) {
                    // Chỉ xóa Chỉ Số khi thành công
                    bongTai.itemOptions.clear();

                    // Danh sách Chỉ Số ngẫu nhiên
                    int[][] options = {
                        {50, 5, 50}, {77, 5, 50}, {103, 5, 50}, {108, 5, 10}, {94, 5, 50},
                        {14, 2, 10}, {80, 1, 5}, {81, 1, 5}, {5, 1, 10}, {101, 1, 50},
                        {100, 1, 10}, {236, 1, 50}, {237, 1, 50}, {15, 1, 50}, {16, 1, 50},
                        {17, 1, 50}, {18, 1, 50}, {78, 1, 50}, {0, 1, 10000}
                    };

                    // Thêm Chỉ Số ngẫu nhiên
                    int[] option = options[Util.nextInt(0, options.length)];
                    bongTai.itemOptions.add(new ItemOption(option[0], Util.nextInt(option[1], option[2])));

                    sendEffectSuccessCombine(player);
                } else {
                    sendEffectFailCombine(player);
                    Service.getInstance().sendThongBao(player, "Hợp thành thất bại! Chỉ Số cũ được giữ nguyên");
                }

                // Cập nhật lại thông tin người chơi
                InventoryService.gI().sendItemBags(player);
                Service.getInstance().sendMoney(player);
                reOpenItemCombine(player);
            } else {
                Service.getInstance().sendThongBao(player, "Không đủ nguyên liệu để thực hiện!");
            }
        }
    }

    private void nangCapDoThienSu(Player player) {
        if (player.combineNew.itemsCombine.size() > 1) {
            int ratioLuckyStone = 0, ratioRecipe = 0, ratioUpgradeStone = 0;
            List<Item> list = new ArrayList<>();
            Item angelClothes = null;
            Item craftingRecipe = null;
            for (Item item : player.combineNew.itemsCombine) {
                int id = item.template.id;
                if (item.isNotNullItem()) {
                    if (isAngelClothes(id)) {
                        angelClothes = item;
                    } else if (isLuckyStone(id)) {
                        ratioLuckyStone += getRatioLuckyStone(id);
                        list.add(item);
                    } else if (isUpgradeStone(id)) {
                        ratioUpgradeStone += getRatioUpgradeStone(id);
                        list.add(item);
                    } else if (isCraftingRecipe(id)) {
                        ratioRecipe += getRatioCraftingRecipe(id);
                        craftingRecipe = item;
                        list.add(item);
                    }
                }
            }
            boolean canUpgrade = true;
            for (ItemOption io : angelClothes.itemOptions) {
                int optId = io.optionTemplate.id;
                if (optId == 41) {
                    canUpgrade = false;
                }
            }
            if (canUpgrade) {
                if (angelClothes != null && craftingRecipe != null) {
                    int ratioTotal = (20 + ratioUpgradeStone + ratioRecipe);
                    int ratio = ratioTotal > 75 ? ratio = 75 : ratioTotal;
                    if (player.inventory.gold >= COST_DAP_DO_KICH_HOAT) {
                        if (Util.isTrue(ratio, 150)) {
                            int num = 0;
                            if (Util.isTrue(ratioLuckyStone, 150)) {
                                num = 15;
                            } else if (Util.isTrue(5, 100)) {
                                num = Util.nextInt(10, 15);
                            } else if (Util.isTrue(20, 100)) {
                                num = Util.nextInt(1, 10);
                            }
                            RandomCollection<Integer> rd = new RandomCollection<>();
                            rd.add(50, 1);
                            rd.add(25, 2);
                            rd.add(10, 3);
                            rd.add(5, 4);
                            int color = rd.next();
                            for (ItemOption io : angelClothes.itemOptions) {
                                int optId = io.optionTemplate.id;
                                switch (optId) {
                                    case 47: // giáp
                                    case 6: // hp
                                    case 26: // hp/30s
                                    case 22: // hp k
                                    case 0: // sức đánh
                                    case 7: // ki
                                    case 28: // ki/30s
                                    case 23: // ki k
                                    case 14: // crit
                                        io.param += ((long) io.param * num / 100);
                                        break;
                                }
                            }
                            angelClothes.itemOptions.add(new ItemOption(41, color));
                            for (int i = 0; i < color; i++) {
                                angelClothes.itemOptions
                                        .add(new ItemOption(Util.nextInt(201, 212), Util.nextInt(1, 10)));
                            }
                            sendEffectSuccessCombine(player);
                            Service.getInstance().sendThongBao(player, "Chúc mừng bạn đã Nâng Cấp thành công");
                        } else {
                            sendEffectFailCombine(player);
                            Service.getInstance().sendThongBao(player, "Chúc bạn đen nốt lần sau");
                        }
                        for (Item it : list) {
                            InventoryService.gI().subQuantityItemsBag(player, it, 1);
                        }
                        player.inventory.subGold(COST_DAP_DO_KICH_HOAT);
                        InventoryService.gI().sendItemBags(player);
                        Service.getInstance().sendMoney(player);
                        reOpenItemCombine(player);
                    }
                }
            }
        }
    }

    private void giaHanCaiTrang(Player player) {
        if (player.combineNew.itemsCombine.size() == 2) {
            Item caitrang = null, vegiahan = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (item.isNotNullItem()) {
                    if (item.template.type == 5 || item.template.type == 21 || item.template.type == 11 || item.template.type == 72 || item.template.type == 74) {
                        caitrang = item;
                    } else if (item.template.id == 1280) {
                        vegiahan = item;
                    }
                }
            }
            if (caitrang != null && vegiahan != null) {
                if (InventoryService.gI().getCountEmptyBag(player) > 0
                        && player.inventory.gold >= COST_GIA_HAN_CAI_TRANG) {
                    ItemOption expiredDate = null;
                    boolean canBeExtend = true;
                    for (ItemOption io : caitrang.itemOptions) {
                        if (io.optionTemplate.id == 93) {
                            expiredDate = io;
                        }
                        if (io.optionTemplate.id == 199) {
                            canBeExtend = false;
                        }
                    }
                    if (canBeExtend) {
                        if (expiredDate.param > 0) {
                            player.inventory.subGold(COST_GIA_HAN_CAI_TRANG);
                            sendEffectSuccessCombine(player);
                            expiredDate.param += 7;
                            InventoryService.gI().subQuantityItemsBag(player, vegiahan, 1);
                            InventoryService.gI().sendItemBags(player);
                            Service.getInstance().sendMoney(player);
                            reOpenItemCombine(player);
                        }
                    }
                }
            }
        }
    }

    public void openDTS(Player player) {
        if (player.combineNew.itemsCombine.size() != 4) {
            Service.getInstance().sendThongBao(player, "Thiếu Đồ");
            return;
        }
        if (player.inventory.gold < COST_NANG_DO_TS) {
            Service.getInstance().sendThongBao(player, "Ảo ít thôi con...");
            return;
        }
        if (player.inventory.ruby < COST_RUBY_NANG_DO_TS) {
            Service.getInstance().sendThongBao(player, "Ảo ít thôi con...");
            return;
        }
        if (InventoryService.gI().getCountEmptyBag(player) < 1) {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít Nhất 1 ô trống trong hành trang");
            return;
        }
        if (player.combineNew.itemsCombine.size() == 4) {
            Item manhts = null;
            Item danc = null;
            Item damayman = null;
            Item congthuc = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (item.isNotNullItem()) {
                    if (item.isManhTS()) {
                        manhts = item;
                    } else if (item.isdanangcapDoTs()) {
                        danc = item;
                    } else if (item.isDamayman()) {
                        damayman = item;
                    } else if (item.isCongthucNomal() || item.isCongthucVip()) {
                        congthuc = item;
                    }
                }
            }
            if (manhts == null || danc == null || damayman == null || congthuc == null) {
                Service.getInstance().sendThongBao(player, "Thiếu Đồ!!");
                return;
            }
            if (manhts.quantity < 999) {
                Service.getInstance().sendThongBao(player, "Cần x99 mảnh thiên sứ!");
                return;
            }
            if (manhts != null && danc != null && damayman != null && congthuc != null && manhts.quantity >= 99) {
                int tile = get_Tile_nang_Do_TS(danc, congthuc);
                int perLucky = 20;// chi
                perLucky += perLucky * (damayman.template.id - 1078) * 10 / 100;
                int perSuccesslucky = Util.nextInt(0, 100);
                if (Util.isTrue(tile, 100)) {
                    short[][] itemIds = {{1048, 1051, 1054, 1057, 1060}, {1049, 1052, 1055, 1058, 1061}, {1050, 1053, 1056, 1059, 1062}};
                    Item itemTS = ItemService.gI().DoThienSu(itemIds[congthuc.template.gender][manhts.typeIdManh()], congthuc.template.gender, perSuccesslucky, perLucky);
                    sendEffectSuccessCombineDoTS(player, itemTS.template.iconID);
                    InventoryService.gI().addItemBag(player, itemTS, 0);
                    InventoryService.gI().subQuantityItemsBag(player, manhts, 99);
                } else {
                    sendEffectFailCombineDoTS(player);
                    InventoryService.gI().subQuantityItemsBag(player, manhts, 99);
                }
                player.inventory.gold -= COST_NANG_DO_TS;
                player.inventory.ruby -= COST_RUBY_NANG_DO_TS;
                InventoryService.gI().subQuantityItemsBag(player, danc, 1);
                InventoryService.gI().subQuantityItemsBag(player, damayman, 1);
                InventoryService.gI().subQuantityItemsBag(player, congthuc, 1);
                InventoryService.gI().sendItemBags(player);
                Service.getInstance().sendMoney(player);
                player.combineNew.itemsCombine.clear();
                reOpenItemCombine(player);
            }
        }
    }

    private void dapDoKichHoat1(Player player) {
        if (player.combineNew.itemsCombine.size() == 1 || player.combineNew.itemsCombine.size() == 2) {
            Item dhd = null, dtl = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (item.isNotNullItem()) {
                    if (item.template.id >= 650 && item.template.id <= 662) {
                        dhd = item;
                    }
                }
            }
            if (dhd != null) {
                if (InventoryService.gI().getCountEmptyBag(player) > 0
                        && player.inventory.gold >= COST_DAP_DO_KICH_HOAT) {
                    player.inventory.gold -= COST_DAP_DO_KICH_HOAT;
                    int tiLe = dhd != null ? 100 : 40;
                    if (Util.isTrue(tiLe, 100)) {
                        sendEffectSuccessCombine(player);
                        int gender = dhd.template.gender < 3 ? dhd.template.gender : player.gender;
                        Item item = ItemService.gI()
                                .createNewItem((short) getTempIdItemC0(gender, dhd.template.type));
                        RewardService.gI().initBaseOptionClothes(item.template.id, item.template.type,
                                item.itemOptions);
                        RewardService.gI().initActivationOption(
                                gender, item.template.type,
                                item.itemOptions);
                        InventoryService.gI().addItemBag(player, item, 0);
                    } else {
                        sendEffectFailCombine(player);
                    }
                    InventoryService.gI().subQuantityItemsBag(player, dhd, 1);
                    InventoryService.gI().sendItemBags(player);
                    Service.getInstance().sendMoney(player);
                    reOpenItemCombine(player);
                }
            }
        }
    }

    private void dapDoKichHoatCaoCap1(Player player) {
        if (player.combineNew.itemsCombine.size() == 3) {
            Item it = player.combineNew.itemsCombine.get(0), it1 = player.combineNew.itemsCombine.get(1),
                    it2 = player.combineNew.itemsCombine.get(2);
            if (!isDestroyClothes(it.template.id) || !isDestroyClothes(it1.template.id)
                    || !isDestroyClothes(it2.template.id)) {
                it = null;
            }
            if (it != null && it1 != null && it2 != null) {
                if (InventoryService.gI().getCountEmptyBag(player) > 0
                        && player.inventory.gold >= COST_DAP_DO_KICH_HOAT) {
                    player.inventory.subGold(COST_DAP_DO_KICH_HOAT);
                    int soluongitem = ConstItem.LIST_ITEM_CLOTHES[0][0].length;
                    int id;
                    if (Util.isTrue(98, 100)) {
                        if (Util.isTrue(60, 100)) {
                            id = (Util.nextInt(5, soluongitem - 2));// random từ bậc 1 đến bậc 6
                        } else {
                            id = (Util.nextInt(5, soluongitem - 2));// random từ bậc 6 đến bậc 12
                        }
                    } else {
                        id = soluongitem - 1; // đồ thần linh
                    }
                    sendEffectSuccessCombine(player);
                    int gender = it.template.gender < 3 ? it.template.gender : player.gender; // Sử dụng giới tính của người chơi
                    Item item = ItemService.gI().createNewItem((short) ConstItem.LIST_ITEM_CLOTHES[gender][it.template.type][id]); // Chọn loại đồ thứ 0 (loại chung)
                    RewardService.gI().initBaseOptionClothes(item.template.id, item.template.type, item.itemOptions); // Chọn loại đồ thứ 0 (loại chung)
                    RewardService.gI().initActivationOption(gender, item.template.type, item.itemOptions); // Chọn loại đồ thứ 0 (loại chung)
                    InventoryService.gI().addItemBag(player, item, 0);
                    InventoryService.gI().subQuantityItemsBag(player, it, 1);
                    InventoryService.gI().subQuantityItemsBag(player, it1, 1);
                    InventoryService.gI().subQuantityItemsBag(player, it2, 1);
                    InventoryService.gI().sendItemBags(player);
                    Service.getInstance().sendMoney(player);
                    reOpenItemCombine(player);
                } else {
                    Service.getInstance().sendThongBao(player, "Hành trang đã đầy");
                }
            }
        }
    }

    private void dapDoKichHoat(Player player) {
        if (player.combineNew.itemsCombine.size() == 6) {
            Item manhTL1 = null;
            Item manhTL2 = null;
            Item manhTL3 = null;
            Item manhTL4 = null;
            Item manhTL5 = null;
            Item dokh = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (item.isNotNullItem()) {
                    if (item.template.id == 2032) {
                        manhTL1 = item;
                    } else if (item.template.id == 2033) {
                        manhTL2 = item;
                    } else if (item.template.id == 2034) {
                        manhTL3 = item;
                    } else if (item.template.id == 2035) {
                        manhTL4 = item;
                    } else if (item.template.id == 2036) {
                        manhTL5 = item;
                    } else if (item.isSKHThuong()) {
                        dokh = item;
                    }
                }
            }
            if (dokh != null && manhTL1 != null && manhTL2 != null && manhTL3 != null && manhTL4 != null && manhTL5 != null) {
                if (InventoryService.gI().getCountEmptyBag(player) > 0 && player.inventory.ruby >= 10_000) {
                    player.inventory.gem -= 10_000;
                    int tiLe = 60;
                    if (Util.isTrue(70, 100)) {
                        Item item = ItemService.gI().createNewItem((short) getTempIdItemC02TB(dokh.template.gender, dokh.template.type));
                        RewardService.gI().initBaseOptionClothes(item.template.id, item.template.type, item.itemOptions);
                        RewardService.gI().initActivationOption(item.template.gender < 3 ? item.template.gender : player.gender, item.template.type, item.itemOptions);
                        InventoryService.gI().addItemBag(player, item, 0);
                        InventoryService.gI().sendItemBags(player);
                    } else if (Util.isTrue(99, 100)) {
                        Item item = ItemService.gI().createNewItem((short) getTempIdItemC02(dokh.template.gender, dokh.template.type));
                        RewardService.gI().initBaseOptionClothes(item.template.id, item.template.type, item.itemOptions);
                        RewardService.gI().initActivationOption(item.template.gender < 3 ? item.template.gender : player.gender, item.template.type, item.itemOptions);
                        InventoryService.gI().addItemBag(player, item, 0);
                        InventoryService.gI().sendItemBags(player);
                    } else if (Util.isTrue(1, 100)) {
                        Item item = ItemService.gI().createNewItem((short) getTempIdItemC02VIP(dokh.template.gender, dokh.template.type));
                        RewardService.gI().initBaseOptionClothes(item.template.id, item.template.type, item.itemOptions);
                        RewardService.gI().initActivationOption(item.template.gender < 3 ? item.template.gender : player.gender, item.template.type, item.itemOptions);
                        InventoryService.gI().addItemBag(player, item, 0);
                        InventoryService.gI().sendItemBags(player);
                    }
                    sendEffectSuccessCombine(player);
                    InventoryService.gI().subQuantityItemsBag(player, dokh, 1);
                    InventoryService.gI().subQuantityItemsBag(player, manhTL1, 1);
                    InventoryService.gI().subQuantityItemsBag(player, manhTL2, 1);
                    InventoryService.gI().subQuantityItemsBag(player, manhTL3, 1);
                    InventoryService.gI().subQuantityItemsBag(player, manhTL4, 1);
                    InventoryService.gI().subQuantityItemsBag(player, manhTL5, 1);
                    Service.getInstance().sendMoney(player);
                    reOpenItemCombine(player);
                } else {
                    Service.getInstance().sendThongBao(player, "Bạn phải có ít Nhất 1 ô trống trong hành trang");
                }
            }
        }
    }

    private void tradeDoThanLinh(Player player) {
        if (player.combineNew.itemsCombine.size() != 2) {
            Service.getInstance().sendThongBao(player, "Thiếu Đồ");
            return;
        }
        if (player.inventory.gold < COST_RUBY_TRADE_TL) {
            Service.getInstance().sendThongBao(player, "Nạp tiền vào donate cho taooo!");
            return;
        }
        if (InventoryService.gI().getCountEmptyBag(player) < 1) {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít Nhất 1 ô trống trong hành trang");
            return;
        }
        if (player.combineNew.itemsCombine.size() == 2) {
            Item doTL = null;
            Item thucan = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (item.isNotNullItem()) {
                    if (item.template.id >= 555 && item.template.id <= 567) {
                        doTL = item;
                    }
                    if (item.template.id >= 663 && item.template.id <= 667) {
                        thucan = item;
                    }
                }
            }
            if (doTL == null) {
                Service.getInstance().sendThongBao(player, "Thiếu Đồ!!");
                return;
            }
            if (thucan == null) {
                Service.getInstance().sendThongBao(player, "Thiếu thức ăn!!");
                return;
            }
            if (doTL.quantity < 1) {
                Service.getInstance().sendThongBao(player, "Cần x1 món đồ Thần Linh bất kì!");
                return;
            }
            if (thucan.quantity < 99) {
                Service.getInstance().sendThongBao(player, "Cần x99 món đồ Thức ăn bất kì!");
                return;
            }
            if (doTL != null && doTL.quantity >= 1 && thucan != null && thucan.quantity >= 99) {
                int tile = 50;
                Item item = ItemService.gI().createNewItem((short) getTempIdItemC01(doTL.template.gender, doTL.template.type));
                RewardService.gI().initBaseOptionClothes1(item.template.id, item.template.type, item.itemOptions);
                InventoryService.gI().addItemBag(player, item, 0);
                sendEffectSuccessCombineDoTS(player, item.template.iconID);
                InventoryService.gI().subQuantityItemsBag(player, doTL, 1);
                InventoryService.gI().subQuantityItemsBag(player, thucan, 99);

                player.inventory.gold -= COST_RUBY_TRADE_TL;
                InventoryService.gI().sendItemBags(player);
                Service.getInstance().sendMoney(player);
                reOpenItemCombine(player);
            }
        }

    }

    private void doiManhKichHoat(Player player) {
        if (player.combineNew.itemsCombine.size() == 2 || player.combineNew.itemsCombine.size() == 3) {
            Item nr1s = null, doThan = null, buaBaoVe = null;
            for (Item it : player.combineNew.itemsCombine) {
                if (it.template.id == 14) {
                    nr1s = it;
                } else if (it.template.id == 2010) {
                    buaBaoVe = it;
                } else if (it.template.id >= 555 && it.template.id <= 567) {
                    doThan = it;
                }
            }
            if (nr1s != null && doThan != null) {
                if (InventoryService.gI().getCountEmptyBag(player) > 0 && player.inventory.gold >= COST_DOI_MANH_KICH_HOAT) {
                    player.inventory.gold -= COST_DOI_MANH_KICH_HOAT;
                    int tiLe = buaBaoVe != null ? 100 : 50;
                    if (Util.isTrue(tiLe, 100)) {
                        sendEffectSuccessCombine(player);
                        Item item = ItemService.gI().createNewItem((short) 2009);
                        item.itemOptions.add(new ItemOption(30, 0));
                        InventoryService.gI().addItemBag(player, item, 0);
                    } else {
                        sendEffectFailCombine(player);
                    }
                    InventoryService.gI().subQuantityItemsBag(player, nr1s, 1);
                    InventoryService.gI().subQuantityItemsBag(player, doThan, 1);
                    if (buaBaoVe != null) {
                        InventoryService.gI().subQuantityItemsBag(player, buaBaoVe, 1);
                    }
                    InventoryService.gI().sendItemBags(player);
                    Service.getInstance().sendMoney(player);
                    reOpenItemCombine(player);
                }
            } else {
                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                        "Hãy chọn 1 trang bị thần linh và 1 viên ngọc rồng 1 sao", "Đóng");
            }
        }
    }

    private void doiVeHuyDiet(Player player) {
        if (player.combineNew.itemsCombine.size() == 1) {
            Item item = player.combineNew.itemsCombine.get(0);
            if (item.isNotNullItem() && item.template.id >= 555 && item.template.id <= 567) {
                if (InventoryService.gI().getCountEmptyBag(player) > 0
                        && player.inventory.gold >= COST_DOI_VE_DOI_DO_HUY_DIET) {
                    player.inventory.gold -= COST_DOI_VE_DOI_DO_HUY_DIET;
                    Item ticket = ItemService.gI().createNewItem((short) (2001 + item.template.type));
                    ticket.itemOptions.add(new ItemOption(30, 0));
                    InventoryService.gI().subQuantityItemsBag(player, item, 1);
                    InventoryService.gI().addItemBag(player, ticket, 99);
                    sendEffectOpenItem(player, item.template.iconID, ticket.template.iconID);

                    InventoryService.gI().sendItemBags(player);
                    Service.getInstance().sendMoney(player);
                    reOpenItemCombine(player);
                }
            }
        }
    }

    private void epSaoTrangBi(Player player) {
        if (player.combineNew.itemsCombine.size() == 2) {
            int gem = player.combineNew.gemCombine;
            if (player.inventory.gem < gem) {
                Service.getInstance().sendThongBao(player, "Không đủ ngọc để thực hiện");
                return;
            }
            Item trangBi = null;
            Item daPhaLe = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (isTrangBiEpPhaLeHoa(item)) {
                    trangBi = item;
                } else if (isDaPhaLe(item)) {
                    daPhaLe = item;
                }
            }
            int star = 0; // sao pha lê đã ép
            int starEmpty = 0; // lỗ sao pha lê
            if (trangBi != null && daPhaLe != null) {
                ItemOption optionStar = null;
                for (ItemOption io : trangBi.itemOptions) {
                    if (io.optionTemplate.id == 102) {
                        star = (int) io.param;
                        optionStar = io;
                    } else if (io.optionTemplate.id == 107) {
                        starEmpty = (int) io.param;
                    }
                }
                if (star < starEmpty) {
                    player.inventory.subGem(gem);
                    int optionId = getOptionDaPhaLe(daPhaLe);
                    long param = getParamDaPhaLe(daPhaLe);
                    ItemOption option = null;
                    for (ItemOption io : trangBi.itemOptions) {
                        if (io.optionTemplate.id == optionId) {
                            option = io;
                            break;
                        }
                    }
                    if (option != null) {
                        option.param += param;
                    } else {
                        trangBi.itemOptions.add(new ItemOption(optionId, param));
                    }
                    if (optionStar != null) {
                        optionStar.param++;
                    } else {
                        trangBi.itemOptions.add(new ItemOption(102, 1));
//                        trangBi.itemOptions.add(new ItemOption(30, 1));
                    }// ru lai di a

                    if (optionStar != null) {
                        if (!trangBi.haveOption(141) && optionStar.param == 18 && trangBi.template.type < 5) {
                            trangBi.itemOptions.add(new ItemOption(141, 1));//99s

                        } else if (!trangBi.haveOption(142) && optionStar.param == 30 && trangBi.template.type < 5) {
                            trangBi.removeOption(141);
                            trangBi.itemOptions.add(new ItemOption(142, 1));//200s
                        } else if (!trangBi.haveOption(143) && optionStar.param == 45 && trangBi.template.type < 5) {
                            trangBi.removeOption(142);
                            trangBi.itemOptions.add(new ItemOption(143, 1));//300s
                        }
                        if (!trangBi.haveOption(144) && optionStar.param == 65 && trangBi.template.type < 5) {
                            trangBi.removeOption(143);
                            trangBi.itemOptions.add(new ItemOption(144, 1));//99s
                        } else if (!trangBi.haveOption(136) && optionStar.param == 99 && trangBi.template.type < 5) {
                            trangBi.removeOption(144);
                            trangBi.itemOptions.add(new ItemOption(136, 1));//200s
                        } else if (!trangBi.haveOption(137) && optionStar.param == 200 && trangBi.template.type < 5) {
                            trangBi.removeOption(136);
                            trangBi.itemOptions.add(new ItemOption(137, 1));//300s

                        } else if (!trangBi.haveOption(138) && optionStar.param == 300 && trangBi.template.type < 5) {
                            trangBi.removeOption(137);
                            trangBi.itemOptions.add(new ItemOption(138, 1));//300s
                        } else if (!trangBi.haveOption(139) && optionStar.param == 500 && trangBi.template.type < 5) {
                            trangBi.removeOption(138);
                            trangBi.itemOptions.add(new ItemOption(139, 1));//300s

                        } else if (!trangBi.haveOption(140) && optionStar.param == 700 && trangBi.template.type < 5) {
                            trangBi.removeOption(139);
                            trangBi.itemOptions.add(new ItemOption(140, 1));//300s

                        } else if (!trangBi.haveOption(145) && optionStar.param == 999 && trangBi.template.type < 5) {
                            trangBi.removeOption(140);
                            trangBi.itemOptions.add(new ItemOption(145, 1));//300s
                        }
                    }

                    InventoryService.gI().subQuantityItemsBag(player, daPhaLe, 1);
                    sendEffectSuccessCombine(player);
                }
                InventoryService.gI().sendItemBags(player);
                Service.getInstance().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }

    private void phaLeHoaTrangBi(Player player) {
        if (!player.combineNew.itemsCombine.isEmpty()) {
            long gold = player.combineNew.goldCombine;
            int gem = player.combineNew.gemCombine;
            int solandap = player.combineNew.quantities;
            if (player.inventory.gold < gold) {
                Service.getInstance().sendThongBao(player, "Không đủ vàng để thực hiện");
                return;
            } else if (player.inventory.gem < gem) {
                Service.getInstance().sendThongBao(player, "Không đủ ngọc để thực hiện");
                return;
            }
            Item item = player.combineNew.itemsCombine.get(0);
            if (isTrangBiPhaLeHoa(item)) {
                long star = 0;
                ItemOption optionStar = null;
                for (ItemOption io : item.itemOptions) {
                    if (io.optionTemplate.id == 107) {
                        star = io.param;
                        optionStar = io;
                        break;
                    }
                }
                if (star < capSaoTheoVip(item, player)) {
                    player.inventory.gold -= gold;
                    player.inventory.gem -= gem;
                    if (player.name.equals("sumo")) {
                        player.inventory.gold += gold * 2;
                        player.inventory.gem += gem * 2;
                    }
                    byte ratio = (optionStar != null && optionStar.param > 4) ? (byte) 2 : 1;
                    if (Util.isTrue(player.combineNew.ratioCombine, 100 * ratio)) {
                        if (optionStar == null) {
                            item.itemOptions.add(new ItemOption(107, 1));
                        } else {
                            optionStar.param++;
                        }
//                        if (optionStar == null) {
//                            item.itemOptions.add(new ItemOption(30, 1));
//                        }

                        sendEffectSuccessCombine(player);
                        Service.getInstance().sendThongBao(player, "Lên cấp sau " + (solandap - player.combineNew.quantities + 1) + " lần đập");
                        if (optionStar != null && optionStar.param >= 1) {
                            Service.getInstance().sendThongBaoAllPlayer("Chúc mừng Thằng\n|7|[" + player.name + "]\n|4| vừa pha lê hóa\n "
                                    + "thành công \n|7|[" + item.template.name + "]\n"
                                    + "|7|lên " + optionStar.param + " sao pha lê");
                        }
                    } else {
                        ItemOption xit = null;
                        for (ItemOption io : item.itemOptions) {
                            if (io.optionTemplate.id == 238) {
                                xit = io;
                                break;
                            }
                        }
                        if (xit == null) {
                            item.itemOptions.add(new ItemOption(238, 1));
                            //  item.itemOptions.add(new ItemOption(30, 1));
                        } else {
                            xit.param++;
                        }
                        player.combineNew.quantities -= 1;
                        sendEffectFailCombine(player);
                    }
                }
                InventoryService.gI().sendItemBags(player);
                Service.getInstance().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }

    private void nhapNgocRong(Player player) {
        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
            if (!player.combineNew.itemsCombine.isEmpty()) {
                Item item = player.combineNew.itemsCombine.get(0);
                if (item != null && item.isNotNullItem()) {
                    if ((item.template.id > 925 && item.template.id <= 935) && item.quantity >= 7) {
                        Item nr = ItemService.gI().createNewItem((short) (item.template.id - 1));
                        InventoryService.gI().addItemBag(player, nr, 0);
                        InventoryService.gI().subQuantityItemsBag(player, item, 7);
                        InventoryService.gI().sendItemBags(player);
                        reOpenItemCombine(player);
                        sendEffectCombineDB(player, item.template.iconID);
                    }
                }
            }
        }
    }

    private void nangCapLEVELSKH(Player player) {
        if (player.combineNew.itemsCombine.size() >= 2 && player.combineNew.itemsCombine.size() < 5) {
            if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.type < 5).count() != 2) {

            }
            if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.type == 14).count() != 1) {

            }
            if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDTL()).count() != 1) {
            }
            if (player.combineNew.itemsCombine.size() == 4 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.id == 987).count() != 1) {

            }
            Item itemDo = null;
            Item itemDNC = null;
            Item itemDBV = null;
            for (int j = 0; j < player.combineNew.itemsCombine.size(); j++) {
                if (player.combineNew.itemsCombine.get(j).isNotNullItem()) {
                    if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.get(j).template.id == 987) {
                        itemDBV = player.combineNew.itemsCombine.get(j);
                        continue;
                    }
                    if (player.combineNew.itemsCombine.get(j).template.type < 5) {
                        itemDo = player.combineNew.itemsCombine.get(j);
                    } else {
                        itemDNC = player.combineNew.itemsCombine.get(j);
                    }
                }
            }
            if (isCoupleItemNangCapSKHCheck(itemDo, itemDNC)) {
                int countDaNangCap = player.combineNew.countDaNangCap;
                int gold = player.combineNew.goldCombine;
                short countDaBaoVe = player.combineNew.countDaBaoVe;
                if (player.inventory.gold < gold) {
                    Service.getInstance().sendThongBao(player, "Không đủ vàng để thực hiện");
                    return;
                }
                if (itemDNC.quantity < countDaNangCap) {
                    return;
                }
                int level = 0;
                ItemOption optionLevel = null;

                for (ItemOption io : itemDo.itemOptions) {
                    if (io.optionTemplate.id == 217
                            || io.optionTemplate.id == 218
                            || io.optionTemplate.id == 219
                            || io.optionTemplate.id == 220
                            || io.optionTemplate.id == 221
                            || io.optionTemplate.id == 222
                            || io.optionTemplate.id == 223
                            || io.optionTemplate.id == 224
                            || io.optionTemplate.id == 225) {
                        level = (int) io.param;
                        optionLevel = io;
                        break;
                    }

                }
                if (level < MAX_LEVEL_ITEM) {
                    player.inventory.gold -= gold;
                    ItemOption option = null;
                    for (ItemOption io : itemDo.itemOptions) {
                        if (io.optionTemplate.id == 226
                                || io.optionTemplate.id == 227
                                || io.optionTemplate.id == 228
                                || io.optionTemplate.id == 229
                                || io.optionTemplate.id == 230
                                || io.optionTemplate.id == 231
                                || io.optionTemplate.id == 232
                                || io.optionTemplate.id == 233
                                || io.optionTemplate.id == 234) {
                            option = io;
                        }

                    }
                    //thành công
                    if (Util.isTrue(player.combineNew.ratioCombine, 100) || player.isAdmin()) {
                        option.param += 25;
                        optionLevel.param++;
                        sendEffectSuccessCombine(player);
                        System.out.println("lỗi 6:");
                        //thất bại
                    } else {
                        if ((level == 2 || level == 4 || level == 6) && (player.combineNew.itemsCombine.size() != 3)) {
                            option.param -= 25;
                            optionLevel.param--;
                        }

                        sendEffectFailCombine(player);
                    }
                    // trừ item nếu succces or fail 
                    itemDo.itemOptions.removeIf(io -> io.optionTemplate.id == 102
                            || io.optionTemplate.id == 107
                            || io.optionTemplate.id == 50
                            || io.optionTemplate.id == 77
                            || io.optionTemplate.id == 103
                            || io.optionTemplate.id == 101
                            || io.optionTemplate.id == 97
                            || io.optionTemplate.id == 104
                            || io.optionTemplate.id == 105
                            || io.optionTemplate.id == 94
                            || io.optionTemplate.id == 94
                            || io.optionTemplate.id == 14
                    );
                    List<Item> itemDTL = player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDTL()).collect(Collectors.toList());
                    InventoryService.gI().subQuantityItemsBag(player, itemDNC, player.combineNew.countDaNangCap);
                    itemDTL.forEach(i -> InventoryService.gI().subQuantityItemsBag(player, i, 1));
                    InventoryService.gI().sendItemBags(player);
                    Service.getInstance().sendMoney(player);
                    reOpenItemCombine(player);
                }
            }
        }
    }

    private void nangCapVatPham(Player player) {
        if (player.combineNew.itemsCombine.size() >= 2 && player.combineNew.itemsCombine.size() < 4) {
            if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.type < 5).count() != 1) {
                return;
            }
            if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.type == 14).count() != 1) {
                return;
            }
            if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.id == 987).count() != 1) {
                return;//admin
            }
            Item itemDo = null;
            Item itemDNC = null;
            Item itemDBV = null;
            for (int j = 0; j < player.combineNew.itemsCombine.size(); j++) {
                if (player.combineNew.itemsCombine.get(j).isNotNullItem()) {
                    if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.get(j).template.id == 987) {
                        itemDBV = player.combineNew.itemsCombine.get(j);
                        continue;
                    }
                    if (player.combineNew.itemsCombine.get(j).template.type < 5) {
                        itemDo = player.combineNew.itemsCombine.get(j);
                    } else {
                        itemDNC = player.combineNew.itemsCombine.get(j);
                    }
                }
            }
            if (isCoupleItemNangCapCheck(itemDo, itemDNC)) {
                int countDaNangCap = player.combineNew.countDaNangCap;
                int gold = player.combineNew.goldCombine;
                short countDaBaoVe = player.combineNew.countDaBaoVe;
                if (player.inventory.gold < gold) {
                    Service.getInstance().sendThongBao(player, "Không đủ vàng để thực hiện");
                    return;
                }

                if (itemDNC.quantity < countDaNangCap) {
                    return;
                }
                if (player.combineNew.itemsCombine.size() == 3) {
                    if (Objects.isNull(itemDBV)) {
                        return;
                    }
                    if (itemDBV.quantity < countDaBaoVe) {
                        return;
                    }
                }

                int level = 0;
                ItemOption optionLevel = null;
                for (ItemOption io : itemDo.itemOptions) {
                    if (io.optionTemplate.id == 72) {
                        level = (int) io.param;
                        optionLevel = io;
                        break;
                    }
                }
                if (level < MAX_LEVEL_ITEM) {
                    player.inventory.gold -= gold;
                    ItemOption option = null;
                    ItemOption option2 = null;
                    for (ItemOption io : itemDo.itemOptions) {
                        if (io.optionTemplate.id == 47
                                || io.optionTemplate.id == 6
                                || io.optionTemplate.id == 0
                                || io.optionTemplate.id == 7
                                || io.optionTemplate.id == 14
                                || io.optionTemplate.id == 22
                                || io.optionTemplate.id == 23) {
                            option = io;
                        } else if (io.optionTemplate.id == 27
                                || io.optionTemplate.id == 28) {
                            option2 = io;
                        }
                    }
                    if (Util.isTrue(player.combineNew.ratioCombine, 100)) {
                        if (option.optionTemplate.id == 14) {
                            option.param += 1;
                        } else {
                            option.param += (option.param * 5 / 100);
                        }
                        if (option2 != null) {
                            option2.param += (option2.param * 5 / 100);
                        }
                        if (optionLevel == null) {
                            itemDo.itemOptions.add(new ItemOption(72, 1));
                        } else {
                            optionLevel.param++;
                            if (optionLevel.param == 16) {
                                itemDo.itemOptions.add(new ItemOption(146, 1));
                            } else if (optionLevel.param == 15 && !itemDo.haveOption(30)) {
                                itemDo.itemOptions.add(new ItemOption(30, 1));
                            }
                        }
                        if (optionLevel != null && optionLevel.param >= 5) {
                            Service.gI().sendThongBaoAllPlayer("Chúc mừng " + player.name + " vừa Nâng Cấp "
                                    + "thành công " + itemDo.template.name + " lên +" + optionLevel.param);
                        }
                        sendEffectSuccessCombine(player);
                    } else {
                        if ((level == 2 || level == 4 || level == 6 || level == 8 || level == 9 || level == 10 || level == 11 || level == 12 || level == 13 || level == 14 || level == 15 || level == 16) && (player.combineNew.itemsCombine.size() != 3)) {
                            if (option.optionTemplate.id == 14) {
                                option.param -= 1;
                            } else {
                                option.param -= (option.param * 20 / 100);
                            }
                            if (option2 != null) {
                                option2.param -= (option2.param * 20 / 100);
                            }
                            optionLevel.param--;
                        }
                        sendEffectFailCombine(player);
                    }
                    if (player.combineNew.itemsCombine.size() == 3) {
                        InventoryService.gI().subQuantityItemsBag(player, itemDBV, countDaBaoVe);
                    }
                    InventoryService.gI().subQuantityItemsBag(player, itemDNC, player.combineNew.countDaNangCap);
                    InventoryService.gI().sendItemBags(player);
                    Service.getInstance().sendMoney(player);
                    reOpenItemCombine(player);
                }
            }
        }
    }

    private void chucPhucSd(Player player) {
        if (player.combineNew.itemsCombine.size() >= 2 && player.combineNew.itemsCombine.size() < 4) {
            if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.type < 5).count() != 1) {
                return;
            }
            if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.id == 1404).count() != 1) {
                return;
            }
            if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.id == 987).count() != 1) {
                return; // admin
            }

            Item itemDo = null;
            Item itemDNC = null;
            Item itemDBV = null;

            for (int j = 0; j < player.combineNew.itemsCombine.size(); j++) {
                if (player.combineNew.itemsCombine.get(j).isNotNullItem()) {
                    if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.get(j).template.id == 987) {
                        itemDBV = player.combineNew.itemsCombine.get(j);
                        continue;
                    }
                    if (player.combineNew.itemsCombine.get(j).template.type < 5) {
                        itemDo = player.combineNew.itemsCombine.get(j);
                    } else {
                        itemDNC = player.combineNew.itemsCombine.get(j);
                    }
                }
            }

            if (isItemChucPhuc(itemDo, itemDNC)) {
                int countDaNangCap = player.combineNew.countDaNangCap;
                int gold = player.combineNew.goldCombine;
                short countDaBaoVe = player.combineNew.countDaBaoVe;

                if (player.inventory.gold < gold) {
                    Service.gI().sendThongBao(player, "Không đủ vàng để thực hiện");
                    return;
                }

                if (itemDNC.quantity < countDaNangCap) {
                    return;
                }

                if (player.combineNew.itemsCombine.size() == 3) {
                    if (Objects.isNull(itemDBV)) {
                        return;
                    }
                    if (itemDBV.quantity < countDaBaoVe) {
                        return;
                    }
                }

                int level = 0;
                ItemOption optionLevel = null;
                for (ItemOption io : itemDo.itemOptions) {
                    if (io.optionTemplate.id == 210) {
                        level = (int) io.param;
                        optionLevel = io;
                        break;
                    }
                }

                if (level < MAX_LEVEL_CHUC_PHUC) {
                    player.inventory.gold -= gold;
                    if (Util.isTrue(100, 100)) {
                        if (optionLevel == null) {
                            itemDo.itemOptions.add(new ItemOption(210, 500));//
                        } else {
                            //   optionLevel.param++;
                            optionLevel.param += 500;
                        }
                        sendEffectSuccessCombine(player);
                    } else {
                        if ((level >= 1) && (player.combineNew.itemsCombine.size() != 3)) {
                            for (ItemOption io : itemDo.itemOptions) {
                                io.param = Math.max(0, io.param -= 10); //  option.param += 1;
                            }
                            optionLevel.param--;
                        }
                        sendEffectFailCombine(player);
                    }

                    if (player.combineNew.itemsCombine.size() == 3) {
                        InventoryService.gI().subQuantityItemsBag(player, itemDBV, countDaBaoVe);
                    }

                    InventoryService.gI().subQuantityItemsBag(player, itemDNC, player.combineNew.countDaNangCap);
                    InventoryService.gI().sendItemBags(player);
                    Service.gI().sendMoney(player);
                    reOpenItemCombine(player);
                }
            }
        }
    }

    private void chucPhucHp(Player player) {
        if (player.combineNew.itemsCombine.size() >= 2 && player.combineNew.itemsCombine.size() < 4) {
            if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.type < 5).count() != 1) {
                return;
            }
            if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.id == 1404).count() != 1) {
                return;
            }
            if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.id == 987).count() != 1) {
                return; // admin
            }

            Item itemDo = null;
            Item itemDNC = null;
            Item itemDBV = null;

            for (int j = 0; j < player.combineNew.itemsCombine.size(); j++) {
                if (player.combineNew.itemsCombine.get(j).isNotNullItem()) {
                    if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.get(j).template.id == 987) {
                        itemDBV = player.combineNew.itemsCombine.get(j);
                        continue;
                    }
                    if (player.combineNew.itemsCombine.get(j).template.type < 5) {
                        itemDo = player.combineNew.itemsCombine.get(j);
                    } else {
                        itemDNC = player.combineNew.itemsCombine.get(j);
                    }
                }
            }

            if (isItemChucPhuc(itemDo, itemDNC)) {
                int countDaNangCap = player.combineNew.countDaNangCap;
                int gold = player.combineNew.goldCombine;
                short countDaBaoVe = player.combineNew.countDaBaoVe;

                if (player.inventory.gold < gold) {
                    Service.gI().sendThongBao(player, "Không đủ vàng để thực hiện");
                    return;
                }

                if (itemDNC.quantity < countDaNangCap) {
                    return;
                }

                if (player.combineNew.itemsCombine.size() == 3) {
                    if (Objects.isNull(itemDBV)) {
                        return;
                    }
                    if (itemDBV.quantity < countDaBaoVe) {
                        return;
                    }
                }

                int level = 0;
                ItemOption optionLevel = null;
                for (ItemOption io : itemDo.itemOptions) {
                    if (io.optionTemplate.id == 211) {
                        level = (int) io.param;
                        optionLevel = io;
                        break;
                    }
                }

                if (level < MAX_LEVEL_CHUC_PHUC) {
                    player.inventory.gold -= gold;
                    if (Util.isTrue(100, 100)) {
                        if (optionLevel == null) {
                            itemDo.itemOptions.add(new ItemOption(211, 500));
                        } else {
                            optionLevel.param += 500;
                        }
                        sendEffectSuccessCombine(player);
                    } else {
                        if ((level >= 2) && (player.combineNew.itemsCombine.size() != 3)) {
                            for (ItemOption io : itemDo.itemOptions) {
                                io.param = Math.max(0, io.param -= 20);
                            }
                            optionLevel.param--;
                        }
                        sendEffectFailCombine(player);
                    }

                    if (player.combineNew.itemsCombine.size() == 3) {
                        InventoryService.gI().subQuantityItemsBag(player, itemDBV, countDaBaoVe);
                    }

                    InventoryService.gI().subQuantityItemsBag(player, itemDNC, player.combineNew.countDaNangCap);
                    InventoryService.gI().sendItemBags(player);
                    Service.gI().sendMoney(player);
                    reOpenItemCombine(player);
                }
            }
        }
    }

    private void chucPhucKi(Player player) {
        if (player.combineNew.itemsCombine.size() >= 2 && player.combineNew.itemsCombine.size() < 4) {
            if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.type < 5).count() != 1) {
                return;
            }
            if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.id == 1404).count() != 1) {
                return;
            }
            if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.id == 987).count() != 1) {
                return; // admin
            }

            Item itemDo = null;
            Item itemDNC = null;
            Item itemDBV = null;

            for (int j = 0; j < player.combineNew.itemsCombine.size(); j++) {
                if (player.combineNew.itemsCombine.get(j).isNotNullItem()) {
                    if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.get(j).template.id == 987) {
                        itemDBV = player.combineNew.itemsCombine.get(j);
                        continue;
                    }
                    if (player.combineNew.itemsCombine.get(j).template.type < 5) {
                        itemDo = player.combineNew.itemsCombine.get(j);
                    } else {
                        itemDNC = player.combineNew.itemsCombine.get(j);
                    }
                }
            }

            if (isItemChucPhuc(itemDo, itemDNC)) {
                int countDaNangCap = player.combineNew.countDaNangCap;
                int gold = player.combineNew.goldCombine;
                short countDaBaoVe = player.combineNew.countDaBaoVe;

                if (player.inventory.gold < gold) {
                    Service.gI().sendThongBao(player, "Không đủ vàng để thực hiện");
                    return;
                }

                if (itemDNC.quantity < countDaNangCap) {
                    return;
                }

                if (player.combineNew.itemsCombine.size() == 3) {
                    if (Objects.isNull(itemDBV)) {
                        return;
                    }
                    if (itemDBV.quantity < countDaBaoVe) {
                        return;
                    }
                }

                int level = 0;
                ItemOption optionLevel = null;
                for (ItemOption io : itemDo.itemOptions) {
                    if (io.optionTemplate.id == 212) {
                        level = (int) io.param;
                        optionLevel = io;
                        break;
                    }
                }

                if (level < MAX_LEVEL_CHUC_PHUC) {
                    player.inventory.gold -= gold;
                    if (Util.isTrue(100, 100)) {
                        if (optionLevel == null) {
                            itemDo.itemOptions.add(new ItemOption(212, 500));
                        } else {
                            optionLevel.param += 500;
                        }
                        sendEffectSuccessCombine(player);
                    } else {
                        if ((level >= 2) && (player.combineNew.itemsCombine.size() != 3)) {
                            for (ItemOption io : itemDo.itemOptions) {
                                io.param = Math.max(0, io.param -= 30);
                            }
                            optionLevel.param--;
                        }
                        sendEffectFailCombine(player);
                    }

                    if (player.combineNew.itemsCombine.size() == 3) {
                        InventoryService.gI().subQuantityItemsBag(player, itemDBV, countDaBaoVe);
                    }

                    InventoryService.gI().subQuantityItemsBag(player, itemDNC, player.combineNew.countDaNangCap);
                    InventoryService.gI().sendItemBags(player);
                    Service.gI().sendMoney(player);
                    reOpenItemCombine(player);
                }
            }
        }
    }

    private void chucPhucDef(Player player) {
        if (player.combineNew.itemsCombine.size() >= 2 && player.combineNew.itemsCombine.size() < 4) {
            if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.type < 5).count() != 1) {
                return;
            }
            if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.id == 1404).count() != 1) {
                return;
            }
            if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.id == 987).count() != 1) {
                return; // admin
            }

            Item itemDo = null;
            Item itemDNC = null;
            Item itemDBV = null;

            for (int j = 0; j < player.combineNew.itemsCombine.size(); j++) {
                if (player.combineNew.itemsCombine.get(j).isNotNullItem()) {
                    if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.get(j).template.id == 987) {
                        itemDBV = player.combineNew.itemsCombine.get(j);
                        continue;
                    }
                    if (player.combineNew.itemsCombine.get(j).template.type < 5) {
                        itemDo = player.combineNew.itemsCombine.get(j);
                    } else {
                        itemDNC = player.combineNew.itemsCombine.get(j);
                    }
                }
            }

            if (isItemChucPhuc(itemDo, itemDNC)) {
                int countDaNangCap = player.combineNew.countDaNangCap;
                int gold = player.combineNew.goldCombine;
                short countDaBaoVe = player.combineNew.countDaBaoVe;

                if (player.inventory.gold < gold) {
                    Service.gI().sendThongBao(player, "Không đủ vàng để thực hiện");
                    return;
                }

                if (itemDNC.quantity < countDaNangCap) {
                    return;
                }

                if (player.combineNew.itemsCombine.size() == 3) {
                    if (Objects.isNull(itemDBV)) {
                        return;
                    }
                    if (itemDBV.quantity < countDaBaoVe) {
                        return;
                    }
                }

                int level = 0;
                ItemOption optionLevel = null;
                for (ItemOption io : itemDo.itemOptions) {
                    if (io.optionTemplate.id == 213) {
                        level = (int) io.param;
                        optionLevel = io;
                        break;
                    }
                }

                if (level < MAX_LEVEL_CHUC_PHUC) {
                    player.inventory.gold -= gold;
                    if (Util.isTrue(100, 100)) {
                        if (optionLevel == null) {
                            itemDo.itemOptions.add(new ItemOption(213, 500));
                        } else {
                            optionLevel.param += 500;
                        }
                        sendEffectSuccessCombine(player);
                    } else {
                        if ((level >= 2) && (player.combineNew.itemsCombine.size() != 3)) {
                            for (ItemOption io : itemDo.itemOptions) {
                                io.param = Math.max(0, io.param -= 2000);
                            }
                            optionLevel.param--;
                        }
                        sendEffectFailCombine(player);
                    }

                    if (player.combineNew.itemsCombine.size() == 3) {
                        InventoryService.gI().subQuantityItemsBag(player, itemDBV, countDaBaoVe);
                    }

                    InventoryService.gI().subQuantityItemsBag(player, itemDNC, player.combineNew.countDaNangCap);
                    InventoryService.gI().sendItemBags(player);
                    Service.gI().sendMoney(player);
                    reOpenItemCombine(player);
                }
            }
        }
    }

    public void GetTrangBiKichHoatthiensu(Player player, int id) {
        Item item = ItemService.gI().createNewItem((short) id);
        int[][] optionNormal = {{127, 128}, {130, 132}, {133, 135}};
        // int[][] paramNormal = {{139,140},{142,144},{136,138}};
        int[][] optionVIP = {{129}, {131}, {134}};
        //int[][] paramVIP = {{141},{143},{137}};
        int random = Util.nextInt(optionNormal.length);
        int randomSkh = Util.nextInt(100);
        if (item.template.type == 0) {
            item.itemOptions.add(new ItemOption(47, Util.nextInt(2, 50000000)));
        }
        if (item.template.type == 1) {
            item.itemOptions.add(new ItemOption(6, Util.nextInt(1, 50000000)));
        }
        if (item.template.type == 2) {
            item.itemOptions.add(new ItemOption(0, Util.nextInt(1, 50000000)));
        }
        if (item.template.type == 3) {
            item.itemOptions.add(new ItemOption(7, Util.nextInt(1, 50000000)));
        }
        if (item.template.type == 4) {
            item.itemOptions.add(new ItemOption(14, Util.nextInt(1, 45)));
        }
        if (randomSkh <= 2) {//tile ra do kich hoat
            if (randomSkh <= 5) { // tile ra option vip
                item.itemOptions.add(new ItemOption(optionVIP[player.gender][0], 0));
                //   item.itemOptions.add(new ItemOption(paramVIP[player.gender][0], 0));
                item.itemOptions.add(new ItemOption(30, 0));
            } else {// 
                item.itemOptions.add(new ItemOption(optionNormal[player.gender][random], 0));
                // item.itemOptions.add(new ItemOption(paramNormal[player.gender][random], 0));
                item.itemOptions.add(new ItemOption(30, 0));
            }
        }

        InventoryService.gI().addItemBag(player, item, 9999);
        InventoryService.gI().sendItemBags(player);
    }

    public void GetTrangBiKichHoathuydiet(Player player, int id) {
        Item item = ItemService.gI().createNewItem((short) id);
        int[][] optionNormal = {{136, 138}, {140, 141}, {142, 144}};
        int[][] optionVIP = {{137}, {139}, {143}};
        int random = Util.nextInt(optionNormal.length);
        int randomSkh = Util.nextInt(100);
        if (item.template.type == 0) {
            item.itemOptions.add(new ItemOption(47, Util.nextInt(1, 10000000)));
        }
        if (item.template.type == 1) {
            item.itemOptions.add(new ItemOption(6, Util.nextInt(1, 10000000)));
        }
        if (item.template.type == 2) {
            item.itemOptions.add(new ItemOption(0, Util.nextInt(9, 10000000)));
        }
        if (item.template.type == 3) {
            item.itemOptions.add(new ItemOption(7, Util.nextInt(9, 10000000)));
        }
        if (item.template.type == 4) {
            item.itemOptions.add(new ItemOption(14, Util.nextInt(1, 40)));
        }
        if (randomSkh <= 100) {//tile ra do kich hoat
            if (randomSkh <= 5) { // tile ra option vip
                //   item.itemOptions.add(new ItemOption(optionVIP[player.gender][0], 0));
                item.itemOptions.add(new ItemOption(253, 7));
            } else {// 
                // item.itemOptions.add(new ItemOption(optionNormal[player.gender][random], 0));
                item.itemOptions.add(new ItemOption(253, 1));
            }
        }

        InventoryService.gI().addItemBag(player, item, 9999);
        InventoryService.gI().sendItemBags(player);
    }

    private boolean issachTuyetKy(Item item) {
        if (item != null && item.isNotNullItem()) {
            if (item.template.type == 77) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean checkHaveOption(Item item, int viTriOption, int idOption) {
        if (item != null && item.isNotNullItem()) {
            if (item.itemOptions.get(viTriOption).optionTemplate.id == idOption) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean isItemChucPhuc(Item trangBi, Item daNangCap) {
        if (trangBi != null && daNangCap != null) {
            if (trangBi.template.type == 0 && daNangCap.template.id == 1404) {
                return true;
            } else if (trangBi.template.type == 1 && daNangCap.template.id == 1404) {
                return true;
            } else if (trangBi.template.type == 2 && daNangCap.template.id == 1404) {
                return true;
            } else if (trangBi.template.type == 3 && daNangCap.template.id == 1404) {
                return true;
            } else if (trangBi.template.type == 4 && daNangCap.template.id == 1404) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean isCoupleItemNangCapCheck(Item trangBi, Item daNangCap) {
        if (trangBi != null && daNangCap != null) {
            if (trangBi.template.type == 0 && daNangCap.template.id == 223) {
                return true;
            } else if (trangBi.template.type == 1 && daNangCap.template.id == 222) {
                return true;
            } else if (trangBi.template.type == 2 && daNangCap.template.id == 224) {
                return true;
            } else if (trangBi.template.type == 3 && daNangCap.template.id == 221) {
                return true;
            } else if (trangBi.template.type == 4 && daNangCap.template.id == 220) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    // --------------------------------------------------------------------------
    /**
     * Hiệu ứng mở item
     *
     * @param player
     */
    public void sendEffectOpenItem(Player player, short icon1, short icon2) {
        Message msg;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(OPEN_ITEM);
            msg.writer().writeShort(icon1);
            msg.writer().writeShort(icon2);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    /**
     * Hiệu ứng đập đồ thành công
     *
     * @param player
     */
    private void sendEffectSuccessCombine(Player player) {
        Message msg;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(COMBINE_SUCCESS);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    private void sendEffectCombine(Player player) {
        Message msg;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(8);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void BongtoiTrangBi(Player player) {

        if (player.combineNew.itemsCombine.size() != 2) {
            Service.getInstance().sendThongBao(player, "Thiếu nguyên liệu");
            return;
        }
        if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isTrangBiCaitrang()).count() != 1) {
            Service.getInstance().sendThongBao(player, "Thiếu trang bị");
            return;
        }
        if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isTrangBiPet()).count() != 1) {
            Service.getInstance().sendThongBao(player, "Thiếu trang bị");
            return;
        }
        if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isTrangBiPhukienbang()).count() != 1) {
            Service.getInstance().sendThongBao(player, "Thiếu trang bị");
            return;
        }
        if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.id == 1720).count() != 1) {
            Service.getInstance().sendThongBao(player, "Thiếu đá bóng yêu");
            return;
        }
        if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.id == 1721).count() != 1) {
            Service.getInstance().sendThongBao(player, "Thiếu đá bóng ma");
            return;
        }
        if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.id == 1722).count() != 1) {
            Service.getInstance().sendThongBao(player, "Thiếu đá bóng thuật");
            return;
        }
        if (InventoryService.gI().getCountEmptyBag(player) < 1) {
            if (player.inventory.gold < COST) {
                Service.getInstance().sendThongBao(player, "Con cần thêm vàng để Bóng tối trang bị...");
                return;
            }
//            if (player.inventory.ruby < RUBY) {
//                Service.getInstance().sendThongBao(player, "Con cần thêm hồng ngọc để bí ẩn hóa...");
//                return;
//            }
            player.inventory.gold -= COST;
//            player.inventory.ruby -= RUBY;
            Item daBongyeu = player.combineNew.itemsCombine.stream().filter(item -> item.template.id == 1720).findFirst().get();
            Item daBongma = player.combineNew.itemsCombine.stream().filter(item -> item.template.id == 1721).findFirst().get();
            Item daBongthuat = player.combineNew.itemsCombine.stream().filter(item -> item.template.id == 1722).findFirst().get();
            Item trangBibongtoi0 = player.combineNew.itemsCombine.stream().filter(Item::isTrangBiCaitrang).findFirst().get();
            Item trangBibongtoi1 = player.combineNew.itemsCombine.stream().filter(Item::isTrangBiPet).findFirst().get();
            Item trangBibongtoi2 = player.combineNew.itemsCombine.stream().filter(Item::isTrangBiPhukienbang).findFirst().get();
            if (daBongma == null) {
                Service.getInstance().sendThongBao(player, "Thiếu đá bóng ma");
                return;
            }
            if (daBongyeu == null) {
                Service.getInstance().sendThongBao(player, "Thiếu đá bóng yêu");
                return;
            }
            if (daBongthuat == null) {
                Service.getInstance().sendThongBao(player, "Thiếu đá bóng thuật");
                return;
            }
            if (trangBibongtoi0 == null) {
                Service.getInstance().sendThongBao(player, "Thiếu trang bị");
                return;
            }
            if (trangBibongtoi1 == null) {
                Service.getInstance().sendThongBao(player, "Thiếu trang bị");
                return;
            }
            if (trangBibongtoi2 == null) {
                Service.getInstance().sendThongBao(player, "Thiếu trang bị");
                return;
            }

            if (trangBibongtoi0 != null) {
                for (ItemOption itopt : trangBibongtoi0.itemOptions) {
                    if (itopt.optionTemplate.id == 223) {
                        if (itopt.param >= 8) {
                            Service.getInstance().sendThongBao(player, "Trang bị đã đạt tới giới hạn");
                            return;
                        }
                    }
                }
            }
            if (trangBibongtoi1 != null) {
                for (ItemOption itopt : trangBibongtoi1.itemOptions) {
                    if (itopt.optionTemplate.id == 223) {
                        if (itopt.param >= 8) {
                            Service.getInstance().sendThongBao(player, "Trang bị đã đạt tới giới hạn");
                            return;
                        }
                    }
                }
            }
            if (trangBibongtoi2 != null) {
                for (ItemOption itopt : trangBibongtoi2.itemOptions) {
                    if (itopt.optionTemplate.id == 223) {
                        if (itopt.param >= 8) {
                            Service.getInstance().sendThongBao(player, "Trang bị đã đạt tới giới hạn");
                            return;
                        }
                    }
                }
            }

            if (Util.isTrue(50, 100)) {
                sendEffectSuccessCombine(player);
                List<Integer> idOptionbongtoi = Arrays.asList(249, 250, 251);
                int randomOption = idOptionbongtoi.get(Util.nextInt(0, 3));
                if (!trangBibongtoi0.haveOption(252)) {
                    trangBibongtoi0.itemOptions.add(new ItemOption(252, 1));
                }
                if (!trangBibongtoi1.haveOption(252)) {
                    trangBibongtoi1.itemOptions.add(new ItemOption(252, 1));
                }
                if (!trangBibongtoi2.haveOption(252)) {
                    trangBibongtoi2.itemOptions.add(new ItemOption(252, 1));
                } else {
                    for (ItemOption itopt : trangBibongtoi0.itemOptions) {
                        if (itopt.optionTemplate.id == 252) {
                            itopt.param += 1;
                            break;
                        }
                    }
                    for (ItemOption itopt : trangBibongtoi1.itemOptions) {
                        if (itopt.optionTemplate.id == 252) {
                            itopt.param += 1;
                            break;
                        }
                    }
                    for (ItemOption itopt : trangBibongtoi2.itemOptions) {
                        if (itopt.optionTemplate.id == 252) {
                            itopt.param += 1;
                            break;
                        }
                    }
                }
                if (!trangBibongtoi0.haveOption(randomOption)) {
                    if (!trangBibongtoi1.haveOption(randomOption)) {
                        if (!trangBibongtoi2.haveOption(randomOption)) {
                            trangBibongtoi0.itemOptions.add(new ItemOption(randomOption, Util.nextInt(1, 3)));
                            trangBibongtoi1.itemOptions.add(new ItemOption(randomOption, Util.nextInt(1, 3)));
                            trangBibongtoi2.itemOptions.add(new ItemOption(randomOption, Util.nextInt(1, 3)));
                        }
                    }
                } else {
                    for (ItemOption itopt : trangBibongtoi0.itemOptions) {
                        if (itopt.optionTemplate.id == randomOption) {
                            itopt.param += Util.nextInt(1, 3);
                            break;
                        }
                    }
                    for (ItemOption itopt : trangBibongtoi1.itemOptions) {
                        if (itopt.optionTemplate.id == randomOption) {
                            itopt.param += Util.nextInt(1, 3);
                            break;
                        }
                    }
                    for (ItemOption itopt : trangBibongtoi2.itemOptions) {
                        if (itopt.optionTemplate.id == randomOption) {
                            itopt.param += Util.nextInt(1, 3);
                            break;
                        }
                    }
                }
                Service.getInstance().sendThongBao(player, "Bạn đã bóng tối trang bị thành công");
            } else {
                sendEffectFailCombine(player);
            }
            InventoryService.gI().subQuantityItemsBag(player, daBongma, 1);
            InventoryService.gI().subQuantityItemsBag(player, daBongyeu, 1);
            InventoryService.gI().subQuantityItemsBag(player, daBongthuat, 1);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendMoney(player);
            player.combineNew.itemsCombine.clear();
            reOpenItemCombine(player);
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít Nhất 1 ô trống trong hành trang");
        }
    }

    private void xoaspl(Player player) {
        if (player.combineNew.itemsCombine.size() != 2) {
            Service.getInstance().sendThongBao(player, "Thiếu nguyên liệu");
            return;
        }
        Item itemBongToi = null;
        for (Item item_ : player.combineNew.itemsCombine) {
            // id để pháp sư
            if (item_.haveOption(102)) {
                itemBongToi = item_;
            }

        }

        if (itemBongToi == null) {
            Service.getInstance().sendThongBao(player, "Thiếu trang bị có thế xóa dòng");
            return;
        }
        for (ItemOption itopt : itemBongToi.itemOptions) {
            if (itopt.param < 1 && itopt.optionTemplate.id == 102) {
                Service.getInstance().sendThongBao(player, "Số sao đã đạt tối thiểu");
                return;
            }
        }
        // id đá ps
        if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.id == 1983).count() != 1) {
            Service.getInstance().sendThongBao(player, "Thiếu Đá quý");
            return;
        }
        if (InventoryService.gI().getCountEmptyBag(player) > 1) {

            if (player.inventory.gold < COST) {
                Service.getInstance().sendThongBao(player, "Con cần thêm vàng để nâng trang bị...");
                return;
            }

            player.inventory.gold -= COST;
            Item daPhapSu = player.combineNew.itemsCombine.stream().filter(item -> item.template.id == 1983).findFirst().get();
            if (daPhapSu.quantity < 10000) {
                Service.getInstance().sendThongBao(player, "Con còn thiếu " + (10000 - daPhapSu.quantity) + " đá quý để Nâng Cấp");
                return;
            }

            if (daPhapSu == null) {
                Service.getInstance().sendThongBao(player, "Thiếu đá Kim Cương");
                return;
            }
            if (itemBongToi == null) {
                Service.getInstance().sendThongBao(player, "Thiếu trang bị");
                return;
            }

            if (Util.isTrue(99, 100)) {
                sendEffectSuccessCombine(player);
                sendEffectSuccessCombine(player);
                for (ItemOption itopt : itemBongToi.itemOptions) {
                    if (itopt.optionTemplate.id == 102 && itopt.param > 0) {
                        itopt.param -= 1;
                        break;
                    }
                }

                Service.getInstance().sendThongBao(player, "Bạn đã Nâng Cấp trang bị thành công");
            } else {
                sendEffectFailCombine(player);
            }
            InventoryService.gI().subQuantityItemsBag(player, daPhapSu, 10000);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendMoney(player);
            reOpenItemCombine(player);
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít Nhất 1 ô trống trong hành trang");
        }
    }

    private void XoadongTrangBi(Player player) {

        if (player.combineNew.itemsCombine.size() != 2) {
            Service.getInstance().sendThongBao(player, "Thiếu nguyên liệu");
            return;
        }
        if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isTrangBiXoadong()).count() != 1) {
            Service.getInstance().sendThongBao(player, "Thiếu trang bị bóng tối");
            return;
        }
        if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.id == 1989).count() != 1) {
            Service.getInstance().sendThongBao(player, "Thiếu đá ánh sáng");
            return;
        }
        if (InventoryService.gI().getCountEmptyBag(player) < 1) {
            if (player.inventory.gold < 0) {
                Service.getInstance().sendThongBao(player, "Con cần thêm vàng để xoá dòng trang bị bóng tối...");
                return;
            }
            player.inventory.gold -= 0;
            Item daAnhsang = player.combineNew.itemsCombine.stream().filter(item -> item.template.id == 1989).findFirst().get();
            Item trangBibongtoi = player.combineNew.itemsCombine.stream().filter(Item::isTrangBiXoadong).findFirst().get();
            if (daAnhsang == null) {
                Service.getInstance().sendThongBao(player, "Thiếu đá ánh sáng");
                return;
            }
            if (trangBibongtoi == null) {
                Service.getInstance().sendThongBao(player, "Thiếu trang bị bóng tối");
                return;
            }

            if (Util.isTrue(100, 100)) {
                sendEffectSuccessCombine(player);
                List<Integer> idOptionbongtoi = Arrays.asList(249, 250, 251);

                ItemOption option_252 = new ItemOption();
                ItemOption option_249 = new ItemOption();
                ItemOption option_250 = new ItemOption();
                ItemOption option_251 = new ItemOption();

                for (ItemOption itopt : trangBibongtoi.itemOptions) {
                    if (itopt.optionTemplate.id == 252) {
                        System.out.println("252");
                        option_252 = itopt;
                    }
                    if (itopt.optionTemplate.id == 249) {
                        System.out.println("249");
                        option_249 = itopt;
                    }
                    if (itopt.optionTemplate.id == 250) {
                        System.out.println("250");
                        option_250 = itopt;
                    }
                    if (itopt.optionTemplate.id == 251) {
                        System.out.println("251");
                        option_251 = itopt;
                    }
                }
                if (option_252 != null) {
                    trangBibongtoi.itemOptions.remove(option_252);
                }
                if (option_249 != null) {
                    trangBibongtoi.itemOptions.remove(option_249);
                }
                if (option_250 != null) {
                    trangBibongtoi.itemOptions.remove(option_250);
                }
                if (option_251 != null) {
                    trangBibongtoi.itemOptions.remove(option_251);
                }
                Service.getInstance().sendThongBao(player, "Bạn đã xoá dòng thành công");
                InventoryService.gI().sendItemBags(player);
            } else {
                sendEffectFailCombine(player);
            }
            InventoryService.gI().subQuantityItemsBag(player, daAnhsang, 1);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendMoney(player);
            player.combineNew.itemsCombine.clear();
            reOpenItemCombine(player);
        }
    }

    /**
     * Hiệu ứng đập đồ thất bại
     *
     * @param player
     */
    private void sendEffectFailCombine(Player player) {
        Message msg;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(COMBINE_FAIL);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    private void sendEffectFailCombineDoTS(Player player) {
        Message msg;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(8);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Gửi lại danh sách đồ trong tab combine
     *
     * @param player
     */
    private void reOpenItemCombine(Player player) {
        Message msg;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(REOPEN_TAB_COMBINE);
            msg.writer().writeByte(player.combineNew.itemsCombine.size());
            for (Item it : player.combineNew.itemsCombine) {
                for (int j = 0; j < player.inventory.itemsBag.size(); j++) {
                    if (it == player.inventory.itemsBag.get(j)) {
                        msg.writer().writeByte(j);
                    }
                }
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    /**
     * Hiệu ứng ghép ngọc rồng
     *
     * @param player
     * @param icon
     */
    private void sendEffectCombineDB(Player player, short icon) {
        Message msg;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(COMBINE_DRAGON_BALL);
            msg.writer().writeShort(icon);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    private int getCountDaBaoVe(int level) {
        return level + 1;
    }

    // --------------------------------------------------------------------------Ratio,
    // cost combine
    private int getRatioDaMayMan(int id) {
        switch (id) {
            case 1079:
                return 10;
            case 1080:
                return 20;
            case 1081:
                return 30;
            case 1082:
                return 40;
            case 1083:
                return 50;
        }
        return 0;
    }

    private int getRatioDaNangCap(int id) {
        switch (id) {
            case 1074:
                return 10;
            case 1075:
                return 20;
            case 1076:
                return 30;
            case 1077:
                return 40;
            case 1078:
                return 50;
        }
        return 0;
    }

    private int getGoldPhaLeHoa(int star) {
        switch (star) {
            case 0:
                return 10000000;
            case 1:
                return 10000000;
            default:
                return 500000000;
        }

    }

    private float getRatioPhaLeHoa(int star) {
        //    switch (star) {
        switch (star) {
            case 0:
                return 100f;
            case 1:
                return 100f;
            default:// 8s tro di mac dinh
                return 1.82f;
        }

    }

    private float getRatioPhaLeHoascam(int star) {
        //    switch (star) {
        switch (star) {
            case 0:
                return 100f;
            case 1:
                return 90f;

            default:// 8s tro di mac dinh
                return 2.96f;
        }

    }

//private float ratioTheoVip(Player player) {
//    if (player.vip >= 1 && player.vip <= 99) {
//        return 2f * player.vip; // Mỗi cấp VIP sẽ cộng thêm 2% (hiển thị 2% cho mỗi cấp VIP)
//    }
//    return 0f; // Giá trị mặc định nếu VIP không hợp lệ
//}
    private float ratioTheoVip(Player player) {
        if (player.vip >= 1 && player.vip <= 10) {
            return player.vip * 1f; // Mỗi VIP tăng 1%
        }
        return 0f; // Nếu VIP ngoài phạm vi 1-8, không tăng tỷ lệ
    }

    private int getGemPhaLeHoa(int star) {
        switch (star) {
            case 0:
                return 100;
            case 1:
                return 200;

            default:
                return 1200;
        }

    }

    private int getGemEpSao(int star) {
        switch (star) {
            case 0:
                return 1;
            case 1:
                return 2;
            case 2:
                return 5;
            case 3:
                return 10;
            case 4:
                return 25;
            case 5:
                return 50;
            default:
                return 5000;
        }

    }

    private int dnsdapdo(int star) {
        switch (star) {
            case 5:
                return 5000;
            case 6:
                return 5000;
            case 7:
                return 5000;
            case 8:
                return 5000;
            case 9:
                return 5000;
            case 10:
                return 5000;
            default:
                return 5000;
        }

    }

    private boolean isCoupleItemNangCapSKHCheck(Item trangBi, Item daNangCap) {
        if (trangBi != null && daNangCap != null) {
            if (trangBi.template.type == 0 && trangBi.isSKHLEVEL() && daNangCap.template.id == 1518) {
                return true;
            } else if (trangBi.template.type == 1 && trangBi.isSKHLEVEL() && daNangCap.template.id == 1518) {
                return true;
            } else if (trangBi.template.type == 2 && trangBi.isSKHLEVEL() && daNangCap.template.id == 1518) {
                return true;
            } else if (trangBi.template.type == 3 && trangBi.isSKHLEVEL() && daNangCap.template.id == 1518) {
                return true;
            } else if (trangBi.template.type == 4 && trangBi.isSKHLEVEL() && daNangCap.template.id == 1518) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private int getTileNangCapDo(int level) {
        switch (level) {
            case 0:
                return 80;
            case 1:
                return 50;
            case 2:
                return 20;
            case 3:
                return 10;
            case 4:
                return 5;
            case 5:
                return 3;
            case 6:
                return 2;
            default:
                return 1;
        }

    }

    private int getTileNangCapDochucphuc(int level) {
        switch (level) {
            case 0:
                return 100;
            case 1:
                return 100;
            case 2:
                return 100;
            case 3:
                return 100;
            case 4:
                return 100;
            case 5:
                return 100;
            case 6:
                return 100;
            default:
                return 100;
        }

    }

    private int getCountDaNangCapDo(int level) {
        switch (level) {
            case 0:
                return 30;
            case 1:
                return 70;
            case 2:
                return 110;
            case 3:
                return 170;
            case 4:
                return 230;
            case 5:
                return 350;
            case 6:
                return 500;
            default:
                return 999;

        }

    }

    private int getCountDaNangCapDochucphuc(int level) {
        switch (level) {
            case 0:
                return 100000;
            case 1:
                return 100000;
            case 2:
                return 100000;
            case 3:
                return 100000;
            case 4:
                return 100000;
            case 5:
                return 100000;
            case 6:
                return 100000;
            default:
                return 100000;

        }

    }

// private short capSaoTheoVip(Item item, Player player) {
//    // Nếu VIP 0, không có lỗ đục
//    if (player.vip == 0) {
//        return 0;
//    }
//    // Nếu VIP từ 1 đến 99, mỗi VIP tăng 10 lỗ đục
//    if (player.vip >= 1 && player.vip <= 99) {
//        return (short) (10 * player.vip); // VIP 1 => 10 lỗ đục, VIP 2 => 20 lỗ đục, ...
//    }
//    // Trường hợp VIP ngoài phạm vi từ 1 đến 99 (nếu cần xử lý thêm)
//    return 0;
//}
    private short capSaoTheoVip(Item item, Player player) {
       switch (player.vip) {
            case 1:
               return 10;
             case 2:
               return 12;   
             case 3:
               return 15;   
             case 4:
               return 18; 
               case 5:
               return 20; 
                case 6:
               return 25;
               case 7:
               return 30; 
               case 8:
               return 40; 
              case 9:
               return 50;
               case 10:
               return 60; 
               
              default:
                return 0;   
               
       }
    }

    private int lvbt(Item bongtai) {
        switch (bongtai.template.id) {
            case 921:
                return 1;
            case 1624:
                return 2;
            case 1625:
                return 3;

        }
        return 0;

    }

    private double getTileNangCapSKH(int level) {
        switch (level) {
            case 0:
                return 70;
            case 1:
                return 40;
            case 2:
                return 30;
            case 3:
                return 10;
            case 4:
                return 5;
            case 5:
                return 20;
            case 6:
                return 10;
            case 7:
                return 5;

        }
        return 0;
    }

    private int getCountDaNangCapSKH(int level) {
        switch (level) {
            case 0:
                return 10;
            case 1:
                return 2;
            case 2:
                return 4;
            case 3:
                return 3;
            case 4:
                return 50;
            case 5:
                return 60;
            case 6:
                return 70;
            case 7:
                return 60;
            case 8:
                return 80;
        }
        return 0;
    }

    private float getTiLeNangcapChanmenh(int star) {
        switch (star) {
            case 0:
                return 100f;
            case 1:
                return 80f;
            case 2:
                return 50f;
            case 3:
                return 40f;
            case 4:
                return 20f;
            case 5:
                return 15f;
            case 6:
                return 10f;
            case 7:
                return 3f;
            default:
                return 1f;
        }
    }

    private int getGoldNangCapDo(int level) {
        switch (level) {
            case 0:
                return 10000;
            case 1:
                return 70000;
            case 2:
                return 300000;
            case 3:
                return 1500000;
            case 4:
                return 7000000;
            case 5:
                return 23000000;
            case 6:
                return 2000000000;
            default:
                return 2000000000;
        }

    }

    // --------------------------------------------------------------------------check
    public boolean isAngelClothes(int id) {
        if (id >= 1048 && id <= 1062) {
            return true;
        }
        return false;
    }

    public boolean isDestroyClothes(int id) {
        if (id >= 650 && id <= 662) {
            return true;
        }
        return false;
    }

    private String getTypeTrangBi(int type) {
        switch (type) {
            case 0:
                return "Áo";
            case 1:
                return "Quần";
            case 2:
                return "Găng";
            case 3:
                return "Giày";
            case 4:
                return "Nhẫn";
        }
        return "";
    }

    public boolean isManhTrangBi(Item it) {
        switch (it.template.id) {
            case 1066:
            case 1067:
            case 1068:
            case 1069:
            case 1070:
                return true;
        }
        return false;
    }

    public boolean isCraftingRecipe(int id) {
        switch (id) {
            case 1071:
            case 1072:
            case 1073:
            case 1084:
            case 1085:
            case 1086:
                return true;
        }
        return false;
    }

    public int getRatioCraftingRecipe(int id) {
        switch (id) {
            case 1071:
                return 0;
            case 1072:
                return 0;
            case 1073:
                return 0;
            case 1084:
                return 10;
            case 1085:
                return 10;
            case 1086:
                return 10;
        }
        return 0;
    }

    public boolean isUpgradeStone(int id) {
        switch (id) {
            case 1074:
            case 1075:
            case 1076:
            case 1077:
            case 1078:
                return true;
        }
        return false;
    }

    private int getDiemNangcapChanmenh(int star) {
        switch (star) {
            case 0:
                return 2000;
            case 1:
                return 2000;
            case 2:
                return 2000;
            case 3:
                return 2000;
            case 4:
                return 2000;
            case 5:
                return 2000;
            case 6:
                return 2000;
            case 7:
                return 2000;
        }
        return 0;
    }

    public int getRatioUpgradeStone(int id) {
        switch (id) {
            case 1074:
                return 10;
            case 1075:
                return 20;
            case 1076:
                return 30;
            case 1077:
                return 40;
            case 1078:
                return 50;
        }
        return 0;
    }

    public boolean isLuckyStone(int id) {
        switch (id) {
            case 1079:
            case 1080:
            case 1081:
            case 1082:
            case 1083:
                return true;
        }
        return false;
    }

    private int getGoldnangbt(int lvbt) {
        return GOLD_BONG_TAI2;
    }

    private int getgemdnangbt(int lvbt) {
        return GEM_BONG_TAI2;
    }

    private int getcountmvbtnangbt(int lvbt) {
        return 9999;
    }

    private boolean checkbongtai(Item item) {
        if (item.template.id == 921 || item.template.id == 1624
                || item.template.id == 1625) {
            return true;
        }
        return false;
    }

    public int getRatioLuckyStone(int id) {
        switch (id) {
            case 1079:
                return 10;
            case 1080:
                return 20;
            case 1081:
                return 30;
            case 1082:
                return 40;
            case 1083:
                return 50;
        }
        return 0;
    }

    private boolean isCoupleItemNangCap(Item item1, Item item2) {
        Item trangBi = null;
        Item daNangCap = null;
        if (item1 != null && item1.isNotNullItem()) {
            if (item1.template.type < 5) {
                trangBi = item1;
            } else if (item1.template.type == 14) {
                daNangCap = item1;
            }
        }
        if (item2 != null && item2.isNotNullItem()) {
            if (item2.template.type < 5) {
                trangBi = item2;
            } else if (item2.template.type == 14) {
                daNangCap = item2;
            }
        }
        if (trangBi != null && daNangCap != null) {
            if (trangBi.template.type == 0 && daNangCap.template.id == 223) {
                return true;
            } else if (trangBi.template.type == 1 && daNangCap.template.id == 222) {
                return true;
            } else if (trangBi.template.type == 2 && daNangCap.template.id == 224) {
                return true;
            } else if (trangBi.template.type == 3 && daNangCap.template.id == 221) {
                return true;
            } else if (trangBi.template.type == 4 && daNangCap.template.id == 220) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean isDaPhaLe(Item item) {
        if (item != null && item.isNotNullItem()) {
            return item.template.type == 31 || item.template.type == 30 || item.template.type == 12
                    || (item.template.id >= 925 && item.template.id <= 931);
        }
        return false;
    }

    private boolean isTrangBiPhaLeHoa(Item item) {
        if (item != null && item.isNotNullItem()) {
            if (item.template.type < 5 || item.template.type == 32) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean isTrangBiEpPhaLeHoa(Item item) {
        if (item != null && item.isNotNullItem()) {
            if (item.template.type < 5 || item.template.type == 32
                    || item.template.type == 11
                    || item.template.type == 5
                    || item.template.type == 21
                    || item.template.type == 72
                    || item.template.type == 99
                    || item.template.type == 74
                    || item.template.type == 35
                    || item.template.type == 23
                    || item.template.type == 24
                    || item.template.type == 77
                    || item.template.type == 115
                    
                    || item.template.type == 72) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private long getParamDaPhaLe(Item daPhaLe) {
        if (daPhaLe.template.type == 12 || daPhaLe.template.type == 30 || daPhaLe.template.type == 31) {
            return daPhaLe.itemOptions.get(0).param;
        }
        switch (daPhaLe.template.id) {
            case 931:
                return 100; // +5%hp
            case 930:
                return 100; // +5%ki
            case 929:
                return 10; // +5%hp/30s
            case 928:
                return 10; // +5%ki/30s
            case 927:
                return 10; // +3%sđ
            case 926:
                return 5; // +2%giáp
            case 925:
                return 5; // +sd
            default:
                return -1;
        }
    }

    private int getOptionDaPhaLe(Item daPhaLe) {
        if (daPhaLe.template.type == 12 || daPhaLe.template.type == 30 || daPhaLe.template.type == 31) {
            return daPhaLe.itemOptions.get(0).optionTemplate.id;
        }
        switch (daPhaLe.template.id) {
            case 931:
                return 77;
            case 930:
                return 103;
            case 929:
                return 80;
            case 928:
                return 81;
            case 927:
                return 94;
            case 926:
                return 5;
            case 925:
                return 50;
            default:
                return -1;
        }
    }

    /**
     * Trả về id item c0
     *
     * @param gender
     * @param type
     * @return
     */
    private int getTempIdItemC0(int gender, int type) {
        if (type == 4) {
            return 12;
        }
        switch (gender) {
            case 0:
                switch (type) {
                    case 0:
                        return 0;
                    case 1:
                        return 6;
                    case 2:
                        return 21;
                    case 3:
                        return 27;
                }
                break;
            case 1:
                switch (type) {
                    case 0:
                        return 1;
                    case 1:
                        return 7;
                    case 2:
                        return 22;
                    case 3:
                        return 28;
                }
                break;
            case 2:
                switch (type) {
                    case 0:
                        return 2;
                    case 1:
                        return 8;
                    case 2:
                        return 23;
                    case 3:
                        return 29;
                }
                break;
        }
        return -1;
    }

    private int getTempIdItemC02(int gender, int type) {
        if (type == 4) {
            return 281;
        }
        switch (gender) {
            case 0:
                switch (type) {
                    case 0:
                        return 233;
                    case 1:
                        return 245;
                    case 2:
                        return 257;
                    case 3:
                        return 562;
                }
                break;
            case 1:
                switch (type) {
                    case 0:
                        return 237;
                    case 1:
                        return 249;
                    case 2:
                        return 261;
                    case 3:
                        return 273;
                }
                break;
            case 2:
                switch (type) {
                    case 0:
                        return 241;
                    case 1:
                        return 253;
                    case 2:
                        return 265;
                    case 3:
                        return 277;
                }
                break;
        }
        return -1;
    }

    private int getTempIdItemC02TB(int gender, int type) {
        if (type == 4) {
            return 281;
        }
        switch (gender) {
            case 0:
                switch (type) {
                    case 0:
                        return 136;
                    case 1:
                        return 143;
                    case 2:
                        return 147;
                    case 3:
                        return 266;
                }
                break;
            case 1:
                switch (type) {
                    case 0:
                        return 155;
                    case 1:
                        return 158;
                    case 2:
                        return 163;
                    case 3:
                        return 270;
                }
                break;
            case 2:
                switch (type) {
                    case 0:
                        return 171;
                    case 1:
                        return 173;
                    case 2:
                        return 178;
                    case 3:
                        return 274;
                }
                break;
        }
        return -1;
    }

    private int getTempIdItemC02VIP(int gender, int type) {
        if (type == 4) {
            return 281;
        }
        switch (gender) {
            case 0:
                switch (type) {
                    case 0:
                        return 555;
                    case 1:
                        return 555;
                    case 2:
                        return 562;
                    case 3:
                        return 563;
                }
                break;
            case 1:
                switch (type) {
                    case 0:
                        return 557;
                    case 1:
                        return 558;
                    case 2:
                        return 564;
                    case 3:
                        return 565;
                }
                break;
            case 2:
                switch (type) {
                    case 0:
                        return 559;
                    case 1:
                        return 560;
                    case 2:
                        return 566;
                    case 3:
                        return 567;
                }
                break;
        }
        return -1;
    }

    private int getTempIdItemC01(int gender, int type) {
        if (type == 4) {
            return 656;
        }
        switch (gender) {
            case 0:
                switch (type) {
                    case 0:
                        return 650;
                    case 1:
                        return 651;
                    case 2:
                        return 657;
                    case 3:
                        return 658;
                }
                break;
            case 1:
                switch (type) {
                    case 0:
                        return 652;
                    case 1:
                        return 653;
                    case 2:
                        return 659;
                    case 3:
                        return 660;
                }
                break;
            case 2:
                switch (type) {
                    case 0:
                        return 654;
                    case 1:
                        return 655;
                    case 2:
                        return 661;
                    case 3:
                        return 662;
                }
                break;
        }
        return -1;
    }

    // Trả về tên đồ c0
    private String getNameItemC0(int gender, int type) {
        if (type == 4) {
            return "Rada cấp 1";
        }
        switch (gender) {
            case 0:
                switch (type) {
                    case 0:
                        return "Áo vải 3 lỗ";
                    case 1:
                        return "Quần vải đen";
                    case 2:
                        return "Găng thun đen";
                    case 3:
                        return "Giầy nhựa";
                }
                break;
            case 1:
                switch (type) {
                    case 0:
                        return "Áo sợi len";
                    case 1:
                        return "Quần sợi len";
                    case 2:
                        return "Găng sợi len";
                    case 3:
                        return "Giầy sợi len";
                }
                break;
            case 2:
                switch (type) {
                    case 0:
                        return "Áo vải thô";
                    case 1:
                        return "Quần vải thô";
                    case 2:
                        return "Găng vải thô";
                    case 3:
                        return "Giầy vải thô";
                }
                break;
        }
        return "";
    }

    private String getNameItemC01(int gender, int type) {
        if (type == 4) {
            return "Nhẫn Hủy Diệt";
        }
        switch (gender) {
            case 0:
                switch (type) {
                    case 0:
                        return "Áo Hủy Diệt";
                    case 1:
                        return "Quần Hủy Diệt";
                    case 2:
                        return "Găng Hủy Diệt";
                    case 3:
                        return "Giầy Hủy Diệt";
                }
                break;
            case 1:
                switch (type) {
                    case 0:
                        return "Áo Hủy Diệt";
                    case 1:
                        return "Quần Hủy Diệt";
                    case 2:
                        return "Găng Hủy Diệt";
                    case 3:
                        return "Giầy Hủy Diệt";
                }
                break;
            case 2:
                switch (type) {
                    case 0:
                        return "Áo Hủy Diệt";
                    case 1:
                        return "Quần Hủy Diệt";
                    case 2:
                        return "Găng Hủy Diệt";
                    case 3:
                        return "Giầy Hủy Diệt";
                }
                break;
        }
        return "";
    }

    public int get_Tile_nang_Do_TS(Item danc, Item congthuc) {
        int tile = 0;
        if (congthuc.isCongthucVip()) {
            tile = 35;
        } else if (congthuc.isCongthucNomal()) {
            tile = 20;
        }
        if (danc != null && danc.isdanangcapDoTs()) {
            tile += (danc.template.id - 1073) * 10;
        }
        return tile;
    }

    // --------------------------------------------------------------------------Text
    // tab combine
    private String getTextTopTabCombine(int type) {
        switch (type) {

            case MO_CHI_SO_CAI_TRANG:
                return "chào sếp";
                 case EP_CAI_TRANG:
                return "chào sếp";
            case BONG_TOI_TRANG_BI:
                return "chào sếp";
            case XOA_DONG_TRANG_BI:
                return "chào sếp";
            case XOA_SPL:
                return "chào sếp Đến Với Shop EM";

            case NANG_CAP_CHAN_MENH:
                return "chào sếp";
            case NANG_CAP_DO_KICH_HOAT:
                return "Ta sẽ giúp ngươi\n làm điều đó";
            case NANG_CAP_DO_TS:
                return "Chế tạo trang bị thiên sứ!";
            case TRADE_DO_THAN_LINH:
                return "Ta sẽ giúp ngươi trao đổi đồ Thần Linh này\nsang đồ hủy diệt";
            case TRADE_DO_THAN_LINH1:
                return "Ta sẽ giúp ngươi trao đổi đồ thần linh này\nsang đá cường hóa";
            case EP_SAO_TRANG_BI:
                return "Ta sẽ phù phép\ncho trang bị của ngươi\ntrở lên mạnh mẽ";

            case PHA_LE_HOA_TRANG_BI:
            case PHA_LE_HOA_TRANG_BI_X10:
                return "Ta sẽ phù phép\ncho trang bị của ngươi\ntrở thành trang bị pha lê";
            case NHAP_NGOC_RONG:
                return "Ta sẽ phù phép\ncho 7 viên Ngọc Rồng Băng\nthành 1 viên Ngọc Rồng Băng cấp cao";
            case NANG_CAP_VAT_PHAM:
                return "Ta sẽ phù phép cho trang bị của ngươi trở lên mạnh mẽ";
            case DOI_VE_HUY_DIET:
                return "Ta sẽ đưa ngươi 1 vé đổi đồ\nhủy diệt, đổi lại ngươi phải đưa ta\n 1 món đồ thần linh tương ứng";
            case DAP_SET_KICH_HOAT:
                return "Ta sẽ giúp ngươi chuyển hóa\n1 món đồ hủy diệt\nthành 1 món đồ kích hoạt";
            // case DOI_MANH_KICH_HOAT:
            // return "Ta sẽ giúp ngươi biến hóa\nviên ngọc 1 sao và 1 món đồ\nthần linh
            // thành mảnh kích hoạt";
            case DAP_SET_KICH_HOAT_CAO_CAP:
                return "Ta sẽ giúp ngươi chuyển hóa\n3 món đồ hủy diệt\nthành 1 món đồ kích hoạt cao cấp";
            case GIA_HAN_CAI_TRANG:
                return "Ta sẽ phù phép\n cho trang bị của mi\n thêm hạn sử dụng";
            case NANG_CAP_DO_THIEN_SU:
                return "Nâng Cấp\n trang bị thiên sứ";
            case NANG_CAP_BONG_TAI:
                return "Ta sẽ phù phép\ncho Bông Tai Porata của ngươi\nthành cấp ss";
            case MO_CHI_SO_BONG_TAI:
                return "Ta sẽ phù phép\ncho Bông Tai Porata cấp 2 của ngươi\ncó 1 Chỉ Số ngẫu nhiên";
            case GIAM_DINH_SACH:
                return "Ta sẽ phù phép\ngiám định sách đó cho ngươi";
            case TAY_SACH:
                return "Ta sẽ phù phép\ntẩy sách đó cho ngươi";
            case NANG_CAP_SACH_TUYET_KY:
                return "Ta sẽ phù phép\nNâng Cấp Sách Tuyệt Kỹ cho ngươi";
            case PHUC_HOI_SACH:
                return "Ta sẽ phù phép\nphục hồi sách cho ngươi";
            case PHAN_RA_SACH:
                return "Ta sẽ phù phép\nphân rã sách cho ngươi";
            case CHUC_PHUC_SD:
                return "Ta Sẽ chúc phúc cho trang bị của ngươi";
            case CHUC_PHUC_HP:
                return "Ta Sẽ chúc phúc cho trang bị của ngươi";
            case CHUC_PHUC_KI:
                return "Ta Sẽ chúc phúc cho trang bị của ngươi";
            case CHUC_PHUC_DEF:
                return "Ta Sẽ chúc phúc cho trang bị của ngươi";
            default:
                return "";
        }
    }

    private String getTextInfoTabCombine(int type) {
        switch (type) {

            case MO_CHI_SO_CAI_TRANG:
                return "->Chọn Cải Trang Có Dòng Kích Hoạt\n"
                        + "Cần 10 Đồng Bitcoin \n"
                        + "Bitcoin Có Ở Điểm Danh, Hoạt Động\n"
                        + "Buôn Bán Vật Phẩm Hiếm\n"
                        + "Random Chỉ Số 1-500% HP, KI, SD, CM\n"
                        + "Có Tỉ Lệ Ra Vĩnh Viễn Đến 20%\n"
                        + "->Sau Đó Chọn 'Nâng Cấp'";

                   case EP_CAI_TRANG:
                return "->Chọn Cải Trang Có Dòng Có Thể Ghép\n"
                        + "Cần [Cải Trang Cường Hóa] Chính\n"
                        + "Kiếm Ở Săn Boss CT Có Dòng Cường Hóa\n"
                        + "Và Dùng Cải Trang Có Chỉ Số + Có Thể Ghép\n"
                        + "Nâng Cấp Chỉ Số Qua Cải Trang Cường Hóa\n"
                        + "->Sau Đó Chọn 'Nâng Cấp'";
                
            case XOA_SPL:
                return "->Chọn trang bị, Cải Trang Muốn Gỡ Lỗ\n"
                        + "Cần 10k Viên Đá Quý\n"
                        + "Đá Quý Có Thể Săn Boss Hoặc Up Ra\n"
                        + "Ta Sẽ Tiến Hành Gỡ Lỗ Con Đã Ép\n"
                        + "Giữ Nguyên Chỉ Số Đã Ép Và Ép 1 Sao Mới\n"
                        + "->Sau Đó Chọn 'Nâng Cấp'";

            case NANG_CAP_CHAN_MENH:
                return "Chọn Cánh Lv1 Và Đá Ngũ Sắc\n"
                        + "\nCần 5k Đá Ngũ Sắc \n"
                        + "\nVà 2000 Điểm TrainFarm Từ Ngũ Hành Sơn\n"
                        + "Random Chỉ Số Mỗi Cấp Max Thêm 50%\n"
                        + "Tùy Cấp Độ (LV1 Max 50% LV2 100% LV3 150%)\n"
                        + "Kiếm Cánh Lv1 Tại Hành Trang Lúc Tạo Tài Khoản\n"
                        + "->Sau Đó Chọn 'Nâng Cấp'";
            case BONG_TOI_TRANG_BI:
                return "Chọn Chân Mệnh Và Đá Ngũ Sắc\n"
                        + "Cần 1000 Đá Ngũ Sắc \n"
                        + "Random Chỉ Số 1-200% Thần Linh 50%\n"
                        + "Random Chỉ Số Theo cấp\n"
                        + "->Sau Đó Chọn 'Nâng Cấp'";

            case XOA_DONG_TRANG_BI:
                return "Chọn Chân Mệnh Và Đá Ngũ Sắc\n"
                        + "Cần 1000 Dá \n"
                        + "Random Chỉ Số 1-200% Thần Linh 50%\n"
                        + "Random Chỉ Số Theo cấp\n"
                        + "->Sau Đó Chọn 'Nâng Cấp'";

            case NANG_CAP_DO_KICH_HOAT:
                return "Chọn 5 mảnh Thần Linh bất kì\n "
                        + " và 1 món SKH thường bất kỳ\n"
                        + "Chỉ cần chọn 'Nâng Cấp'";
            case NANG_CAP_LEVEL_SKH:
                return "Ta sẽ Nâng Cấp SKH của người lên 1 level "
                        + "\nCần 1 món thần linh"
                        + "\nChỉ cần chọn 'Nâng Cấp'";
            case EP_SAO_TRANG_BI:
                return "->Chọn trang bị\n(Áo, quần, găng, giày hoặc rađa)\n"
                        + " Tổng Ép Max 18 Sao Trên Món\n"
                        + "Khảm Đá Hiếm Tăng Gấp Đôi Chỉ Số\n"
                        + "->Sau Đó Chọn 'Nâng Cấp'";
            case TRADE_DO_THAN_LINH:
                return "->Chọn trang bị\n(Áo, quần, găng, giày hoặc nhẫn)và X99 thức ăn bất kì\n"
                        + "->Sau Đó Chọn 'Nâng Cấp'";
            case TRADE_DO_THAN_LINH1:
                return "->Chọn trang bị thần linh\n(Áo, quần, găng, giày hoặc nhẫn)\n"
                        + "->Sau Đó Chọn 'Nâng Cấp'";
            case PHA_LE_HOA_TRANG_BI:
                return "->Chọn trang bị\n(Áo, quần, găng, giày hoặc rađa)\n"
                        + " Chỉ Đập Được Trang Bị Và Giáp Luyện Tập\n"
                        + "Tỉ Lệ Theo Cấp Vip Đang Sở Hữu\n"
                        + "Tỉ Lệ Thêm Bonus Từ Hệ Thống\n"
                        + "->Sau Đó Chọn 'Nâng Cấp'";
            case PHA_LE_HOA_TRANG_BI_X10:
                return "->Chọn trang bị\n(Áo, quần, găng, giày hoặc rađa)\n->Sau Đó Chọn 'Nâng Cấp'\n Khi Nâng Cấp thành công hoặc đủ 5 lần thì sẽ dừng lại";
            case NHAP_NGOC_RONG:
                return "Chọn 7 viên ngọc Băng cùng sao\n->Sau Đó Chọn 'Làm phép'";
            case NANG_CAP_VAT_PHAM:
                return "|6|->Chọn trang bị\n(Áo, quần, găng, giày hoặc rađa)\nChọn loại đá để Nâng Cấp\n"
                        + "Cộng Đồ Max + 999999, Xịt Trừ 30% Chỉ Số,\nVà Rớt Trực Tiếp 2 Cấp\n"
                        + "->Sau Đó Chọn 'Nâng Cấp'";
            case DOI_VE_HUY_DIET:
                return "Chọn món đồ thần linh tương ứng\n(Áo, quần, găng, giày hoặc nhẫn)\n->Sau Đó Chọn 'Đổi'";
            case DAP_SET_KICH_HOAT:
                return "Chọn món đồ hủy diệt tương ứng\n(Áo, quần, găng, giày hoặc nhẫn))\n->Sau Đó Chọn 'Đập'";
            // case DOI_MANH_KICH_HOAT:
            // return "Chọn món đồ thần linh tương ứng\n(Áo, quần, găng,
            // giày hoặc nhẫn)\n->Sau Đó Chọn 'Đổi'";
            case DAP_SET_KICH_HOAT_CAO_CAP:
                return "Chọn 3 món đồ hủy diệt khác nhau\n(Áo, quần, găng, giày hoặc nhẫn)\n->Sau Đó Chọn 'Đập'";
            case GIA_HAN_CAI_TRANG:
                return "->Chọn cải trang có hạn sử dụng\n->Chọn thẻ gia hạn\n->Sau đó chọn gia hạn\n"
                        + "(Pet, Cải Trang, Đeo Lưng, Chân Mệnh)";
            case NANG_CAP_DO_THIEN_SU:
                return "Cần 1 công thức\nTrang bị thiên sứ\nĐá Nâng Cấp (tùy chọn)\nĐá May Mắn (tùy chọn)";
            case NANG_CAP_BONG_TAI:
                return ""
                        + "Chọn Bông Tai Porata 2\n"
                        + "Chọn mảnh Bông Tai để Nâng Cấp\n"
                        + "Số Lượng\n9999 cái\n"
                        + "Nếu Thất Bại Trừ x999 cái\n"
                        + "Nâng Từ Porata c2 - SS - SSS\n"
                        + "->Sau Đó Chọn 'Nâng Cấp'";
            case NANG_CAP_DO_TS:
                return "Cần 1 công thức\n "
                        + "Mảnh trang bị tương ứng"
                        + "1 Đá Nâng Cấp (tùy chọn)\n"
                        + "1 Đá May Mắn (tùy chọn)\n";
            case MO_CHI_SO_BONG_TAI:
                return "Chọn Bông Tai Porata\nChọn mảnh hồn Bông Tai Số Lượng 9999 cái\nvà đá xanh lam để Nâng Cấp\n->Sau Đó Chọn 'Nâng Cấp'";
            case GIAM_DINH_SACH:
                return "Vào hành trang chọn\n1 sách cần giám định\n"
                        + "Chỉ Số 1-1k5%";
            case TAY_SACH:
                return "Vào hành trang chọn\n1 sách cần tẩy\n"
                        + "Tẩy để mở Chỉ Số lại";
            case NANG_CAP_SACH_TUYET_KY:
                return "Vào hành trang chọn\nSách Tuyệt Kỹ 1 cần Nâng Cấp và 1000 Kìm bấm giấy\n"
                        + "Nâng Cấp sách lên lv2";
            case PHUC_HOI_SACH:
                return "Vào hành trang chọn\nCác Sách Tuyệt Kỹ cần phục hồi\n"
                        + "phục hồi độ bền";
            case PHAN_RA_SACH:
                return "Vào hành trang chọn\n1 sách cần phân rã";
            case CHUC_PHUC_SD:
                return "Ta Sẽ Chúc Phúc Cho Ngươi\n"
                        + "CầnTrang Bị\n"
                        + "100.000 Lưu ly\n"
                        + "Chúc Phúc Nhận 500% SD\n"
                        + "Lưu Ly Có Thể Săn Boss Và Nạp\n"
                        + "->Sau Đó Chọn Nâng Cấp";
            case CHUC_PHUC_HP:
                return "Ta Sẽ Chúc Phúc Cho Ngươi\n"
                        + "CầnTrang Bị\n"
                        + "100.000 Lưu ly\n"
                        + "Chúc Phúc Nhận 500% HP\n"
                        + "Lưu Ly Có Thể Săn Boss Và Nạp\n"
                        + "->Sau Đó Chọn Nâng Cấp";
            case CHUC_PHUC_KI:
                return "Ta Sẽ Chúc Phúc Cho Ngươi\n"
                        + "Cần Trang Bị\n"
                        + "100.000 Lưu ly\n"
                        + "Chúc Phúc Nhận 500% KI\n"
                        + "Lưu Ly Có Thể Săn Boss Và Nạp\n"
                        + "->Sau Đó Chọn Nâng Cấp";
            case CHUC_PHUC_DEF:
                return "Ta Sẽ Chúc Phúc Cho Ngươi\n"
                        + "Cần Trang Bị\n"
                        + "100.000 Lưu ly\n"
                        + "Chúc Phúc Nhận 500% GIÁP\n"
                        + "Lưu Ly Có Thể Săn Boss Và Nạp\n"
                        + "->Sau Đó Chọn Nâng Cấp";
            default:
                return "";
        }
    }

    private void sendEffectSuccessCombineDoTS(Player player, short icon) {
        Message msg;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(7);
            msg.writer().writeShort(icon);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
