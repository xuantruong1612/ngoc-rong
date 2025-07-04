package nro.models.Bot;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import nro.models.item.CaiTrang;
import nro.models.item.FlagBag;
import nro.models.item.ItemTemplate;
import nro.server.Manager;
import nro.utils.Util;

public class NewBot {

    public static NewBot i;

    public boolean LOAD_PART = true;
    public int MAXPART = 0;
    public static int[][] PARTBOT = new int[Manager.ITEM_TEMPLATES.size()][4];

    private final String[] FIRST_NAMES = {"an", "bao", "cuong", "duc", "hai", "linh", "minh", "nam", "quoc", "tuan",
        "ngoc", "hoang", "khanh", "trung", "dat", "duy", "hanh", "hung", "son", "trong",
        "phuc", "quang", "thanh", "thien", "trinh", "viet", "binh", "diep", "han", "hieu",
        "huy", "linh", "nguyen", "phong", "phuoc", "quyen", "tam", "thao", "thi", "thu", "van",
        "phuong", "thuy", "thoai", "thong", "trang", "truc", "trinh", "truong", "tuyen", "uy",
        "y", "yen", "yen", "dung", "duong", "hieu", "huy", "huong", "khoa", "lam", "linh",
        "mai", "nhan", "nhi", "phu", "quyen", "thanh", "truc", "trung", "tuan", "tung",
        "tuyen", "van", "vi", "vu", "an", "bich", "binh", "bong", "chi", "chinh", "cho",
        "chung", "du", "duy", "duyen", "giang", "giau", "hong", "huong", "huyen", "khuong", "lai"};

    private final String[] LAST_NAMES = {"nguyen", "tran", "le", "pham", "hoang", "huynh", "phan", "vo", "dang", "bui",
        "vu", "dinh", "do", "dao", "huu", "trinh", "truong", "ngoc", "hoai", "nhat",
        "phong", "phuc", "quyen", "tam", "thao", "thi", "thu", "van", "dung", "duong",
        "hieu", "huy", "huong", "khoa", "lam", "linh", "mai", "nhan", "nhi", "phu",
        "quyen", "thanh", "truc", "trung", "tuan", "tung", "tuyen", "van", "vi", "vu",
        "anh", "bao", "binh", "chinh", "chung", "du", "duy", "duyen", "giang", "giau",
        "hong", "huong", "huyen", "khuong", "lai", "lan", "luan", "nhon", "phieu", "phuoc",
        "quoc", "sang", "sau", "suong", "tam", "thang", "thao", "thien", "thu", "tinh",
        "tung", "tuyen", "uyen", "vinh", "xuan", "yen", "quynh", "anh", "hoai", "minh"};

    public static NewBot gI() {
        if (i == null) {
            i = new NewBot();
        }
        return i;
    }

    public void LoadPart() {
        if (LOAD_PART) {
            int i = 0;
            for (ItemTemplate it : Manager.ITEM_TEMPLATES) {
                if (it.type == 5) {
                    for (CaiTrang ct : Manager.CAI_TRANGS) {
                        if (ct.getID()[0] != -1 && ct.getID()[1] != -1 && ct.getID()[2] != -1
                                && ct.getID()[2] != 194
                                && ct.tempId == it.id && it.id > 1300) { // lấy cải trang từ id 1300 trở đi
                            PARTBOT[i][0] = ct.getID()[0];
                            PARTBOT[i][1] = ct.getID()[2];
                            PARTBOT[i][2] = ct.getID()[1];
                            PARTBOT[i][3] = it.gender;
                            i++;
                            MAXPART++;
                        }
                    }

                }
            }
            LOAD_PART = false;
        }
    }

    public String Getname() {
        return FIRST_NAMES[new Random().nextInt(FIRST_NAMES.length)] + LAST_NAMES[new Random().nextInt(LAST_NAMES.length)];
    }

//    public int getIndex(int gender) {
//        try {
//            int Random = new Random().nextInt(MAXPART);
//            int gend = PARTBOT[Random][3];
//            //   if(gend == gender || gend == 3){
//            if (gend == gender) {
//                return Random;
//            } else {
//                return getIndex(gender);
//            }
//        } catch (Exception e) {
//            System.out.println(MAXPART);
//            return 0;
//        }
//    }
    public int getIndex(int gender) {
        if (MAXPART == 0) {
            System.out.println("No valid parts available for selection.");
            return -1; // Giá trị đặc biệt để báo lỗi
        }
        try {
            int random = new Random().nextInt(MAXPART);
            int gend = PARTBOT[random][3];
//            if (gend == gender) {
                return random;
//            } else {
//                return getIndex(gender);
//            }
        } catch (Exception e) {
            System.out.println("Error in getIndex: " + e.getMessage());
            return -1;
        }
    }

    public void runBot(int type, ShopBot shop, int slot) {
        LoadPart();

        for (int i = 0; i < slot; i++) {
            int Gender = new Random().nextInt(3);
            int Random1 = getIndex(Gender);
            int head = PARTBOT[Random1][0];
            int leg = PARTBOT[Random1][1];
            int body = PARTBOT[Random1][2];
            if (shop != null) {
                shop = new ShopBot(shop);
            }
            List<FlagBag> filteredBags = Manager.gI().FLAGS_BAGS.stream()
                    .filter(flagBag -> flagBag.id > 80) // lấy flag ko bị lỗi
                    .collect(Collectors.toList());
            int flag = filteredBags.get(new Random().nextInt(filteredBags.size())).id;
            Bot b = new Bot((short) head, (short) body, (short) leg, type, Getname(), shop, (short) flag);
            Sanb bos = new Sanb(b);
            Mobb mo1 = new Mobb(b);
            b.mo1 = mo1;
            b.boss = bos;
            int congThem = new Random().nextInt(50000000);
            b.nPoint.limitPower = 8;
            b.nPoint.power = 1000 + congThem;
            b.nPoint.tiemNang = 20000000 + congThem;
            b.nPoint.dameg = Util.nextInt(10000, 500000);
            b.nPoint.mpg = 2000000000;
            b.nPoint.mpMax = 2000000000;
            b.nPoint.mp = 2000000000;
            b.nPoint.hpg = Long.MAX_VALUE;
            b.nPoint.hpMax = Long.MAX_VALUE;
            b.nPoint.hp = Long.MAX_VALUE;
            b.nPoint.maxStamina = 20000;
            b.nPoint.stamina = 20000;
            b.nPoint.critg = 10;
            b.nPoint.defg = 10;
            b.gender = (byte) Gender;
            b.leakSkill();
            b.joinMap();
            if (shop != null) {
                shop.bot = b;
            }
            if (b != null) {
                BotManager.gI().bot.add(b);
            }
        }
    }
}
