package nro.utils;

import static java.lang.Math.random;
import nro.models.mob.Mob;
import nro.models.npc.Npc;
import nro.models.player.Player;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import nro.models.item.Item;
import nro.models.item.ItemOption;
import nro.models.map.ItemMap;
import nro.models.map.Zone;
import nro.services.ItemService;
import org.apache.commons.lang3.ArrayUtils;
import java.util.Random;

/**
 * @Build Arriety
 * @author Administrator
 */
public class Util {

    private static final Random rand;
    private static final SimpleDateFormat dateFormat;
    private static SimpleDateFormat dateFormatDay = new SimpleDateFormat("yyyy-MM-dd");
    private static final Locale locale = new Locale("vi", "VN");
    private static final NumberFormat num = NumberFormat.getInstance(locale);

    private static final Random random = new Random();

    static {
        rand = new Random();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    public static long TamkjllGH(double a) {
        // Telegram: @Tamkjll
        if (a > 0.0f && a <= 1f) {
            a = 1;
        }
        if (a > Long.MAX_VALUE) {
            a = Long.MAX_VALUE;
        }
        return (long) a;
    }

    public static String FormatNumber(double power) {
        if (power >= 1000000000000000000000000000000000000d) {
            return Util.num.format(Math.floor(power / 1000000000000000000000000000000000000d)) + "tỷ tỷ tỷ Tỷ";
        } else if (power >= 1000000000000000000000000000000000d) {
            return Util.num.format(Math.floor(power / 1000000000000000000000000000000000d)) + "triệu tỷ tỷ Tỷ";
        } else if (power >= 1000000000000000000000000000000d) {
            return Util.num.format(Math.floor(power / 1000000000000000000000000000000d)) + "nghìn tỷ tỷ Tỷ";
        } else if (power >= 1000000000000000000000000000d) {
            return Util.num.format(Math.floor(power / 1000000000000000000000000000d)) + " tỷ tỷ Tỷ";
        } else if (power >= 1000000000000000000000000d) {
            return Util.num.format(Math.floor(power / 1000000000000000000000000d)) + " triệu tỷ Tỷ";
        } else if (power >= 1000000000000000000000d) {
            return Util.num.format(Math.floor(power / 1000000000000000000000d)) + " nghìn tỷ Tỷ";
        } else if (power >= 1000000000000000000d) {
            return Util.num.format(Math.floor(power / 1000000000000000000d)) + " tỷ Tỷ";
        } else if (power >= 1000000000000000d) {
            return Util.num.format(Math.floor(power / 1000000000000000d)) + " triệu Tỷ";
        } else if (power >= 1000000000000L) {
            return Util.num.format(Math.floor(power / 1000000000000d)) + " nghìn Tỷ";
        } else if (power >= 1000000000) {
            return Util.num.format(Math.floor(power / 1000000000)) + " Tỷ";
        } else if (power >= 1000000) {
            return Util.num.format(Math.floor(power / 1000000)) + " Tr";
        } else if (power >= 1000) {
            return Util.num.format(Math.floor(power / 1000)) + " k";
        } else {
            return Util.num.format(Math.floor(power));
        }
    }

    public static String getFormatNumber(double hp) {
        // Telegram: @Tamkjll
        return Util.num.format(Math.floor(hp));
    }

    public static String format(double power) {
        return num.format(power);
    }

    public static int maxInt(double a) {
        if (a > Integer.MAX_VALUE) {
            a = Integer.MAX_VALUE;
        }
        return (int) a;
    }

    public static double maxdouble(double a) {
        if (a > Double.MAX_VALUE) {
            a = Double.MAX_VALUE;
        }
        return (double) a;
    }

    public static boolean isTrue(int chanceOutOf100) {
        return random.nextInt(100) < chanceOutOf100;
    }

    public static double nextdame(double from, double to) {
        return from + rand.nextInt((int) (to - from + 1));
    }

    private static boolean quayVatPham(double tyLe) {
        Random random = new Random();
        return random.nextDouble() < tyLe;
    }

    public static long gioihantnsm(double a) {
        if (a > 0.0f && a <= 1f) {
            a = 1;
        }
        if (a > 5_000_000_000_000l) {
            a = 5_000_000_000_000l;
        }
        return (long) a;
    }

    public static String msToThang(long ms) {
        ms = ms - System.currentTimeMillis();
        if (ms < 0) {
            ms = 0;
        }
        long mm;
        long ss;
        long hh;
        long hhd;
        long dd;
        ss = ms / 1000;
        mm = (long) (ss / 60);
        ss = ss % 60;
        hh = (long) (mm / 60);
        hhd = hh % 24;
        mm = mm % 60;
        dd = hh / 24;
        String ssString = String.valueOf(ss);
        String mmString = String.valueOf(mm);
        String hhString = String.valueOf(hh);
        String hhdString = String.valueOf(hhd);
        String ddString = String.valueOf(dd);
        String time;
        if (dd != 0) {
            time = ddString + " Ngày (" + hhdString + "H, " + mmString + "M, " + ssString + "s)";
        } else if (hh != 0) {
            time = "0 Ngày (" + hhString + "H " + mmString + "M " + ssString + "s)";
        } else if (mm != 0) {
            time = "0 Ngày (0H" + mmString + "M " + ssString + "s)";
        } else if (ss != 0) {
            time = ssString + " giây";
        } else {
            time = "Hết hạn";
        }
        return time;
    }

    public static String tinhgiay(long power) {
        num.setMaximumFractionDigits(1);
        if (power >= 60) {
            return num.format((double) power / 60) + " phút";
        } else {
            return num.format(power);
        }
    }

    
     public static int getCurrentHour() {
        LocalTime currentTime = LocalTime.now(); // Lấy giờ hiện tại từ hệ thống
        return currentTime.getHour(); // Trả về giờ trong định dạng 24 giờ
    }


    
    
    
    public static int randomlistiem(int... array) {
        if (array == null || array.length == 0) {
            throw new IllegalArgumentException("Mảng không tồn tại hoặc rỗng");
        }
        int randomIndex = random.nextInt(array.length);
        return array[randomIndex];
    }

    public static String strSQL(final String str) {
        return str.replaceAll("['\"\\\\%]", "\\\\$0");
    }

    public static double nextdouble(double from, double to) {
        return from + rand.nextDouble() * (to - from);
    }

    public static int maxvalue(double a) {
        // Telegram: @Tamkjll
        if (a > Integer.MAX_VALUE) {
            a = Integer.MAX_VALUE;
        }
        return (int) a;
    }

    public static int DoubleGioihan(double a) {
        if (a > 2123456789) {
            a = 2123456789;
        }
        return (int) a;
    }

    public static ItemMap khongthegiaodich(Zone zone, int tempId, int quantity, int x, int y, long playerId) {
        ItemMap it = new ItemMap(zone, tempId, quantity, x, y, playerId);
        it.options.add(new ItemOption(30, 8));
        return it;
    }

    public static ItemMap cothegiaodich(Zone zone, int tempId, int quantity, int x, int y, long playerId) {
        ItemMap it = new ItemMap(zone, tempId, quantity, x, y, playerId);
        it.options.add(new ItemOption(174, 2024));
        return it;
    }

    public static ItemMap bayphantramsd(Zone zone, int tempId, int quantity, int x, int y, long playerId) {
        ItemMap it = new ItemMap(zone, tempId, quantity, x, y, playerId);
        it.options.add(new ItemOption(50, 7));
        return it;
    }

    public static ItemMap bayphantramhp(Zone zone, int tempId, int quantity, int x, int y, long playerId) {
        ItemMap it = new ItemMap(zone, tempId, quantity, x, y, playerId);
        it.options.add(new ItemOption(77, 7));
        return it;
    }

    public static ItemMap bayphantramki(Zone zone, int tempId, int quantity, int x, int y, long playerId) {
        ItemMap it = new ItemMap(zone, tempId, quantity, x, y, playerId);
        it.options.add(new ItemOption(103, 7));
        return it;
    }

    public static ItemMap bayphantramtnsm(Zone zone, int tempId, int quantity, int x, int y, long playerId) {
        ItemMap it = new ItemMap(zone, tempId, quantity, x, y, playerId);
        it.options.add(new ItemOption(101, 7));
        return it;
    }

    public static int tile(double a) {
        // Telegram: @Tamkjll
        if (a > 0.0f && a <= 1f) {
            a = 1;
        }
        if (a > Integer.MAX_VALUE) {
            a = Integer.MAX_VALUE;
        }
        return (int) a;
    }

    public static boolean isTimeSpwam(int start, int end) {
        LocalTime now = LocalTime.now();
        return now.isAfter(LocalTime.of(start, 0)) && now.isBefore(LocalTime.of(end, 0));
    }

    public static Item vpdlcui(int tempId) {
        Item gapvip = ItemService.gI().createNewItem((short) tempId);
        int random = new Random().nextInt(100);
        if (tempId == 1883 || tempId == 1884 || tempId == 1885 || tempId == 1886 || tempId == 1887 || tempId == 1888 || tempId == 1889) {
            gapvip.itemOptions.add(new ItemOption(50, Util.nextInt(10, 20)));
            gapvip.itemOptions.add(new ItemOption(103, Util.nextInt(10, 20)));
            gapvip.itemOptions.add(new ItemOption(77, Util.nextInt(5, 20)));
            gapvip.itemOptions.add(new ItemOption(5, Util.nextInt(10, 20)));
            gapvip.itemOptions.add(new ItemOption(14, Util.nextInt(10, 20)));

        }
        if (Util.isTrue(99, 100)) {
            gapvip.itemOptions.add(new ItemOption(93, Util.nextInt(1, 2)));
        }

        return gapvip;
    }

    public static ItemMap thoivang(Zone zone, int tempId, int quantity, int x, int y, long playerId) {
        ItemMap it = new ItemMap(zone, tempId, quantity, x, y, playerId);
        int thoivang = 457;
        if (thoivang == tempId) {
            it.options.add(new ItemOption(209, 1));
            it.options.add(new ItemOption(30, 1));
        }
        return it;
    }

    public static <K, V extends Comparable<? super V>> HashMap<K, V> sortHashMapByValue(HashMap<K, V> hashMap) {
        return hashMap.entrySet()
                .stream()
                .sorted(Map.Entry.<K, V>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    public static int limitgioihan(double a) {
        if (a > Long.MAX_VALUE) {
            a = Long.MAX_VALUE;
        }
        return (int) a;
    }

    public static String numberToMoney(double power) {
        Locale locale = new Locale("vi", "VN");
        NumberFormat num = NumberFormat.getInstance(locale);
        num.setMaximumFractionDigits(1);
        if (power >= 1000000000) {
            return num.format((double) power / 1000000000) + " Tỷ";
        } else if (power >= 1000000) {
            return num.format((double) power / 1000000) + " Tr";
        } else if (power >= 1000) {
            return num.format((double) power / 1000) + " k";
        } else {
            return num.format(power);
        }
    }

    public static String cap(double power) {
        Locale locale = new Locale("vi", "VN");
        NumberFormat num = NumberFormat.getInstance(locale);
        num.setMaximumFractionDigits(1);
        if (power >= 1000000000) {
            return num.format((double) power / 1000000000) + "Tỷ";
        } else if (power >= 1000000) {
            return num.format((double) power / 1000000) + "Tr";
        } else if (power >= 1000) {
            return num.format((double) power / 1000) + "k";
        } else {
            return num.format(power);
        }
    }

    public static long gioihan(long a) {
        if (a > Long.MAX_VALUE) {
            return Long.MAX_VALUE;
        }
        return (long) a;
    }

    public static String powerToString(double power) {
        Locale locale = new Locale("vi", "VN");
        NumberFormat num = NumberFormat.getInstance(locale);
        num.setMaximumFractionDigits(1);
        if (power >= 1000000000) {
            return num.format((double) power / 1000000000) + " Tỷ";
        } else if (power >= 1000000) {
            return num.format((double) power / 1000000) + " Tr";
        } else if (power >= 1000) {
            return num.format((double) power / 1000) + " k";
        } else {
            return num.format(power);
        }
    }

    public static int highlightsItem(boolean highlights, int value) {
        double highlightsNumber = 1.1;
        return highlights ? (int) (value * highlightsNumber) : value;
    }

    public static ItemMap ratiItem(Zone zone, int tempId, int quantity, int x, int y, long playerId) {
        ItemMap it = new ItemMap(zone, tempId, quantity, x, zone.map.yPhysicInTop(x, y - 24), playerId);
        List<Integer> ao = Arrays.asList(555, 557, 559);
        List<Integer> quan = Arrays.asList(556, 558, 560);
        List<Integer> gang = Arrays.asList(562, 564, 566);
        List<Integer> giay = Arrays.asList(563, 565, 567);
        int ntl = 561;
        if (ao.contains(tempId)) {
            it.options.add(new ItemOption(47, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(501) + 1000)));
        }
        if (quan.contains(tempId)) {
            it.options.add(new ItemOption(6, highlightsItem(it.itemTemplate.gender == 0, new Random().nextInt(10001) + 45000)));
        }
        if (gang.contains(tempId)) {
            it.options.add(new ItemOption(0, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(1001) + 3500)));
        }
        if (giay.contains(tempId)) {
            it.options.add(new ItemOption(7, highlightsItem(it.itemTemplate.gender == 1, new Random().nextInt(10001) + 35000)));
        }
        if (ntl == tempId) {
            it.options.add(new ItemOption(14, new Random().nextInt(4) + 15));
        }
        it.options.add(new ItemOption(21, 15));
        it.options.add(new ItemOption(107, new Random().nextInt(7) + 0));
        return it;
    }

    public static byte createIdBossClone(int idPlayer) {
        return (byte) (-idPlayer - 100);
    }

    public static void setTimeout(Runnable runnable, int delay) {
        new Thread(() -> {
            try {
                Thread.sleep(delay);
                runnable.run();
            } catch (Exception e) {
                System.err.println(e);
            }
        }).start();
    }

    public static int getDistance(int x1, int y1, int x2, int y2) {
        return (int) Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }

    public static int getDistance(Player pl1, Player pl2) {
        return getDistance(pl1.location.x, pl1.location.y, pl2.location.x, pl2.location.y);
    }

    public static int getDistance(Player pl, Npc npc) {
        return getDistance(pl.location.x, pl.location.y, npc.cx, npc.cy);
    }

    public static int getDistance(Player pl, Mob mob) {
        return getDistance(pl.location.x, pl.location.y, mob.location.x, mob.location.y);
    }

    public static int getDistance(Mob mob1, Mob mob2) {
        return getDistance(mob1.location.x, mob1.location.y, mob2.location.x, mob2.location.y);
    }

    public static int getDistanceByDir(int x, int x1, int dir) {
        if (dir == -1) {
            return x + x1;
        }
        return x - x1;
    }

    public static int nextInt(int from, int to) {
        return from + rand.nextInt(to - from + 1);
    }

    public static double nextdouble(int from, int to) {
        return from + rand.nextDouble(to - from + 1);
    }

    public static double nextdouble(double max) {
        return rand.nextDouble(max);
    }

    public static int nextInt(int max) {
        return rand.nextInt(max);
    }

    public static long nextLong(int from, long to) {
        return from + rand.nextLong(to - from + 1);
    }

    public static long nextLong(long max) {
        return rand.nextLong(max);
    }

    public static int nextInt(int[] percen) {
        int next = nextInt(1000), i;
        for (i = 0; i < percen.length; i++) {
            if (next < percen[i]) {
                return i;
            }
            next -= percen[i];
        }
        return i;
    }

    public static int getOne(int n1, int n2) {
        return rand.nextInt() % 2 == 0 ? n1 : n2;
    }

    public static ItemMap settd(Zone zone, int tempId, int quantity, int x, int y, long playerId) {
        ItemMap it = new ItemMap(zone, tempId, quantity, x, y, playerId);
        List<Integer> aotd = Arrays.asList(0);
        List<Integer> quantd = Arrays.asList(6);
        List<Integer> gangtd = Arrays.asList(21);
        List<Integer> giaytd = Arrays.asList(27);
        List<Integer> nhan = Arrays.asList(12);

        if (aotd.contains((int) tempId)) {
            it.options.add(new ItemOption(47, Util.nextInt(1, 10)));
        }
        if (quantd.contains((int) tempId)) {

            it.options.add(new ItemOption(6, Util.nextInt(1, 20)));
        }
        if (gangtd.contains((int) tempId)) {

            it.options.add(new ItemOption(0, Util.nextInt(1, 20)));
        }
        if (giaytd.contains((int) tempId)) {
            it.options.add(new ItemOption(7, Util.nextInt(1, 20)));

        }
        if (nhan.contains((int) tempId)) {
            it.options.add(new ItemOption(14, Util.nextInt(1, 2)));
        }
        if (Util.isTrue(20, 40)) {
            if (Util.isTrue(60, 100)) {
                it.options.add(new ItemOption(127, 1));
                it.options.add(new ItemOption(139, 1));
            } else if (Util.isTrue(30, 100)) {
                it.options.add(new ItemOption(128, 1));
                it.options.add(new ItemOption(140, 1));
            } else {
                it.options.add(new ItemOption(129, 1));
                it.options.add(new ItemOption(141, 1));
            }
        }
        return it;
    }
    
    
     public static ItemMap dathu1(Zone zone, int tempId, int quantity, int x, int y, long playerId) {
        ItemMap it = new ItemMap(zone, tempId, quantity, x, zone.map.yPhysicInTop(x, y - 24), playerId);
        List<Integer> tanjiro = Arrays.asList(1727, 1728, 1729, 1730, 1731, 1732, 1733, 1734, 1735, 1736, 1737, 1738, 1739, 1740, 1741, 1723, 1598, 1580, 1581, 1478, 1479, 1480, 1481, 1483, 1917, 1954, 1955);
        if (tanjiro.contains(tempId)) {
            it.options.add(new ItemOption(50, Util.nextInt(1, 500)));
            it.options.add(new ItemOption(77, Util.nextInt(1, 500)));
            it.options.add(new ItemOption(103, Util.nextInt(1, 500)));
            it.options.add(new ItemOption(78, Util.nextInt(1, 200)));
            it.options.add(new ItemOption(117, Util.nextInt(1, 20)));
            it.options.add(new ItemOption(95, Util.nextInt(1, 10)));
            it.options.add(new ItemOption(96, Util.nextInt(1, 10)));
            it.options.add(new ItemOption(73, Util.nextInt(1, 500)));
            it.options.add(new ItemOption(209, Util.nextInt(1, 50)));
            it.options.add(new ItemOption(107, Util.nextInt(1, 18)));
        }
      if (Util.isTrue(999, 1000)) {
            it.options.add(new ItemOption(93, Util.nextInt(1, 2)));
        }
        return it;
    }
    public static ItemMap dathu2(Zone zone, int tempId, int quantity, int x, int y, long playerId) {
        ItemMap it = new ItemMap(zone, tempId, quantity, x, zone.map.yPhysicInTop(x, y - 24), playerId);
        List<Integer> tanjiro = Arrays.asList(1727, 1728, 1729, 1730, 1731, 1732, 1733, 1734, 1735, 1736, 1737, 1738, 1739, 1740, 1741, 1723, 1598, 1580, 1581, 1478, 1479, 1480, 1481, 1483, 1917, 1954, 1955);
        if (tanjiro.contains(tempId)) {
            it.options.add(new ItemOption(50, Util.nextInt(1, 700)));
            it.options.add(new ItemOption(77, Util.nextInt(1, 700)));
            it.options.add(new ItemOption(103, Util.nextInt(1, 700)));
            it.options.add(new ItemOption(78, Util.nextInt(1, 200)));
            it.options.add(new ItemOption(117, Util.nextInt(1, 20)));
            it.options.add(new ItemOption(95, Util.nextInt(1, 10)));
            it.options.add(new ItemOption(96, Util.nextInt(1, 10)));
            it.options.add(new ItemOption(73, Util.nextInt(1, 500)));
            it.options.add(new ItemOption(209, Util.nextInt(1, 50)));
            it.options.add(new ItemOption(107, Util.nextInt(1, 18)));
        }
      if (Util.isTrue(999, 1000)) {
            it.options.add(new ItemOption(93, Util.nextInt(1, 2)));
        }
        return it;
    } 
  public static ItemMap dathu3(Zone zone, int tempId, int quantity, int x, int y, long playerId) {
        ItemMap it = new ItemMap(zone, tempId, quantity, x, zone.map.yPhysicInTop(x, y - 24), playerId);
        List<Integer> tanjiro = Arrays.asList(1727, 1728, 1729, 1730, 1731, 1732, 1733, 1734, 1735, 1736, 1737, 1738, 1739, 1740, 1741, 1723, 1598, 1580, 1581, 1478, 1479, 1480, 1481, 1483, 1917, 1954, 1955);
        if (tanjiro.contains(tempId)) {
            it.options.add(new ItemOption(50, Util.nextInt(1, 1000)));
            it.options.add(new ItemOption(77, Util.nextInt(1, 1000)));
            it.options.add(new ItemOption(103, Util.nextInt(1, 1000)));
            it.options.add(new ItemOption(78, Util.nextInt(1, 200)));
            it.options.add(new ItemOption(117, Util.nextInt(1, 20)));
            it.options.add(new ItemOption(95, Util.nextInt(1, 10)));
            it.options.add(new ItemOption(96, Util.nextInt(1, 10)));
            it.options.add(new ItemOption(73, Util.nextInt(1, 500)));
            it.options.add(new ItemOption(209, Util.nextInt(1, 50)));
            it.options.add(new ItemOption(107, Util.nextInt(1, 18)));
        }
      if (Util.isTrue(999, 1000)) {
            it.options.add(new ItemOption(93, Util.nextInt(1, 2)));
        }
        return it;
    } 
    public static ItemMap dathu4(Zone zone, int tempId, int quantity, int x, int y, long playerId) {
        ItemMap it = new ItemMap(zone, tempId, quantity, x, zone.map.yPhysicInTop(x, y - 24), playerId);
        List<Integer> tanjiro = Arrays.asList(1727, 1728, 1729, 1730, 1731, 1732, 1733, 1734, 1735, 1736, 1737, 1738, 1739, 1740, 1741, 1723, 1598, 1580, 1581, 1478, 1479, 1480, 1481, 1483, 1917, 1954, 1955);
        if (tanjiro.contains(tempId)) {
            it.options.add(new ItemOption(50, Util.nextInt(1, 1200)));
            it.options.add(new ItemOption(77, Util.nextInt(1, 1200)));
            it.options.add(new ItemOption(103, Util.nextInt(1, 1200)));
            it.options.add(new ItemOption(78, Util.nextInt(1, 200)));
            it.options.add(new ItemOption(117, Util.nextInt(1, 20)));
            it.options.add(new ItemOption(95, Util.nextInt(1, 10)));
            it.options.add(new ItemOption(96, Util.nextInt(1, 10)));
            it.options.add(new ItemOption(73, Util.nextInt(1, 500)));
            it.options.add(new ItemOption(209, Util.nextInt(1, 50)));
            it.options.add(new ItemOption(107, Util.nextInt(1, 18)));
        }
      if (Util.isTrue(999, 1000)) {
            it.options.add(new ItemOption(93, Util.nextInt(1, 2)));
        }
        return it;
    }   
    public static ItemMap dathu5(Zone zone, int tempId, int quantity, int x, int y, long playerId) {
        ItemMap it = new ItemMap(zone, tempId, quantity, x, zone.map.yPhysicInTop(x, y - 24), playerId);
        List<Integer> tanjiro = Arrays.asList(1727, 1728, 1729, 1730, 1731, 1732, 1733, 1734, 1735, 1736, 1737, 1738, 1739, 1740, 1741, 1723, 1598, 1580, 1581, 1478, 1479, 1480, 1481, 1483, 1917, 1954, 1955);
        if (tanjiro.contains(tempId)) {
            it.options.add(new ItemOption(50, Util.nextInt(1, 1500)));
            it.options.add(new ItemOption(77, Util.nextInt(1, 1500)));
            it.options.add(new ItemOption(103, Util.nextInt(1, 1500)));
            it.options.add(new ItemOption(78, Util.nextInt(1, 200)));
            it.options.add(new ItemOption(117, Util.nextInt(1, 20)));
            it.options.add(new ItemOption(95, Util.nextInt(1, 10)));
            it.options.add(new ItemOption(96, Util.nextInt(1, 10)));
            it.options.add(new ItemOption(73, Util.nextInt(1, 500)));
            it.options.add(new ItemOption(209, Util.nextInt(1, 50)));
            it.options.add(new ItemOption(107, Util.nextInt(1, 18)));
        }
      if (Util.isTrue(999, 1000)) {
            it.options.add(new ItemOption(93, Util.nextInt(1, 2)));
        }
        return it;
    }     
    public static ItemMap dathu6(Zone zone, int tempId, int quantity, int x, int y, long playerId) {
        ItemMap it = new ItemMap(zone, tempId, quantity, x, zone.map.yPhysicInTop(x, y - 24), playerId);
        List<Integer> tanjiro = Arrays.asList(1727, 1728, 1729, 1730, 1731, 1732, 1733, 1734, 1735, 1736, 1737, 1738, 1739, 1740, 1741, 1723, 1598, 1580, 1581, 1478, 1479, 1480, 1481, 1483, 1917, 1954, 1955);
        if (tanjiro.contains(tempId)) {
            it.options.add(new ItemOption(50, Util.nextInt(1, 1700)));
            it.options.add(new ItemOption(77, Util.nextInt(1, 1700)));
            it.options.add(new ItemOption(103, Util.nextInt(1, 1700)));
            it.options.add(new ItemOption(78, Util.nextInt(1, 200)));
            it.options.add(new ItemOption(117, Util.nextInt(1, 20)));
            it.options.add(new ItemOption(95, Util.nextInt(1, 10)));
            it.options.add(new ItemOption(96, Util.nextInt(1, 10)));
            it.options.add(new ItemOption(73, Util.nextInt(1, 500)));
            it.options.add(new ItemOption(209, Util.nextInt(1, 50)));
            it.options.add(new ItemOption(107, Util.nextInt(1, 18)));
        }
      if (Util.isTrue(999, 1000)) {
            it.options.add(new ItemOption(93, Util.nextInt(1, 2)));
        }
        return it;
    }        
    public static ItemMap dathu7(Zone zone, int tempId, int quantity, int x, int y, long playerId) {
        ItemMap it = new ItemMap(zone, tempId, quantity, x, zone.map.yPhysicInTop(x, y - 24), playerId);
        List<Integer> tanjiro = Arrays.asList(1727, 1728, 1729, 1730, 1731, 1732, 1733, 1734, 1735, 1736, 1737, 1738, 1739, 1740, 1741, 1723, 1598, 1580, 1581, 1478, 1479, 1480, 1481, 1483, 1917, 1954, 1955);
        if (tanjiro.contains(tempId)) {
            it.options.add(new ItemOption(50, Util.nextInt(1, 2000)));
            it.options.add(new ItemOption(77, Util.nextInt(1, 2000)));
            it.options.add(new ItemOption(103, Util.nextInt(1, 2000)));
            it.options.add(new ItemOption(78, Util.nextInt(1, 200)));
            it.options.add(new ItemOption(117, Util.nextInt(1, 20)));
            it.options.add(new ItemOption(95, Util.nextInt(1, 10)));
            it.options.add(new ItemOption(96, Util.nextInt(1, 10)));
            it.options.add(new ItemOption(73, Util.nextInt(1, 500)));
            it.options.add(new ItemOption(209, Util.nextInt(1, 50)));
            it.options.add(new ItemOption(107, Util.nextInt(1, 18)));
        }
      if (Util.isTrue(999, 1000)) {
            it.options.add(new ItemOption(93, Util.nextInt(1, 2)));
        }
        return it;
    }          
     public static ItemMap dathu8(Zone zone, int tempId, int quantity, int x, int y, long playerId) {
        ItemMap it = new ItemMap(zone, tempId, quantity, x, zone.map.yPhysicInTop(x, y - 24), playerId);
        List<Integer> tanjiro = Arrays.asList(1727, 1728, 1729, 1730, 1731, 1732, 1733, 1734, 1735, 1736, 1737, 1738, 1739, 1740, 1741, 1723, 1598, 1580, 1581, 1478, 1479, 1480, 1481, 1483, 1917, 1954, 1955);
        if (tanjiro.contains(tempId)) {
            it.options.add(new ItemOption(50, Util.nextInt(1, 2500)));
            it.options.add(new ItemOption(77, Util.nextInt(1, 2500)));
            it.options.add(new ItemOption(103, Util.nextInt(1, 2500)));
            it.options.add(new ItemOption(78, Util.nextInt(1, 200)));
            it.options.add(new ItemOption(117, Util.nextInt(1, 20)));
            it.options.add(new ItemOption(95, Util.nextInt(1, 10)));
            it.options.add(new ItemOption(96, Util.nextInt(1, 10)));
            it.options.add(new ItemOption(73, Util.nextInt(1, 500)));
            it.options.add(new ItemOption(209, Util.nextInt(1, 50)));
            it.options.add(new ItemOption(107, Util.nextInt(1, 18)));
        }
      if (Util.isTrue(999, 1000)) {
            it.options.add(new ItemOption(93, Util.nextInt(1, 2)));
        }
        return it;
    }            
     
      public static ItemMap CAITRANG1(Zone zone, int tempId, int quantity, int x, int y, long playerId) {
        ItemMap it = new ItemMap(zone, tempId, quantity, x, zone.map.yPhysicInTop(x, y - 24), playerId);
        List<Integer> tanjiro = Arrays.asList(1348,1349,1350,1351,1352,1353,1354,1355);
        if (tanjiro.contains(tempId)) {
            it.options.add(new ItemOption(50, Util.nextInt(1, 1000)));
            it.options.add(new ItemOption(77, Util.nextInt(1, 1000)));
            it.options.add(new ItemOption(103, Util.nextInt(1, 1000)));
            it.options.add(new ItemOption(78, Util.nextInt(1, 200)));
            it.options.add(new ItemOption(5, Util.nextInt(1, 10)));
            it.options.add(new ItemOption(14, Util.nextInt(1, 10)));
            it.options.add(new ItemOption(73, Util.nextInt(1, 500)));
            it.options.add(new ItemOption(209, Util.nextInt(1, 50)));
            it.options.add(new ItemOption(107, Util.nextInt(1, 18)));
        }
      if (Util.isTrue(999, 1000)) {
            it.options.add(new ItemOption(93, Util.nextInt(1, 2)));
        }
        return it;
    }             
      public static ItemMap CAITRANG2(Zone zone, int tempId, int quantity, int x, int y, long playerId) {
        ItemMap it = new ItemMap(zone, tempId, quantity, x, zone.map.yPhysicInTop(x, y - 24), playerId);
        List<Integer> tanjiro = Arrays.asList(1348,1349,1350,1351,1352,1353,1354,1355);
        if (tanjiro.contains(tempId)) {
            it.options.add(new ItemOption(50, Util.nextInt(1, 1200)));
            it.options.add(new ItemOption(77, Util.nextInt(1, 1200)));
            it.options.add(new ItemOption(103, Util.nextInt(1, 1200)));
            it.options.add(new ItemOption(78, Util.nextInt(1, 200)));
            it.options.add(new ItemOption(5, Util.nextInt(1, 10)));
            it.options.add(new ItemOption(14, Util.nextInt(1, 10)));
            it.options.add(new ItemOption(73, Util.nextInt(1, 500)));
            it.options.add(new ItemOption(209, Util.nextInt(1, 50)));
            it.options.add(new ItemOption(107, Util.nextInt(1, 18)));
        }
      if (Util.isTrue(999, 1000)) {
            it.options.add(new ItemOption(93, Util.nextInt(1, 2)));
        }
        return it;
    }              
     public static ItemMap CAITRANG3(Zone zone, int tempId, int quantity, int x, int y, long playerId) {
        ItemMap it = new ItemMap(zone, tempId, quantity, x, zone.map.yPhysicInTop(x, y - 24), playerId);
        List<Integer> tanjiro = Arrays.asList(1348,1349,1350,1351,1352,1353,1354,1355);
        if (tanjiro.contains(tempId)) {
            it.options.add(new ItemOption(50, Util.nextInt(1, 1500)));
            it.options.add(new ItemOption(77, Util.nextInt(1, 1500)));
            it.options.add(new ItemOption(103, Util.nextInt(1, 1500)));
            it.options.add(new ItemOption(78, Util.nextInt(1, 200)));
            it.options.add(new ItemOption(5, Util.nextInt(1, 10)));
            it.options.add(new ItemOption(14, Util.nextInt(1, 10)));
            it.options.add(new ItemOption(73, Util.nextInt(1, 500)));
            it.options.add(new ItemOption(209, Util.nextInt(1, 50)));
            it.options.add(new ItemOption(107, Util.nextInt(1, 18)));
        }
      if (Util.isTrue(999, 1000)) {
            it.options.add(new ItemOption(93, Util.nextInt(1, 2)));
        }
        return it;
    }                
     
     
     
    
  public static ItemMap CHANMENH(Zone zone, int tempId, int quantity, int x, int y, long playerId) {
        ItemMap it = new ItemMap(zone, tempId, quantity, x, zone.map.yPhysicInTop(x, y - 24), playerId);
        List<Integer> tanjiro = Arrays.asList(1356,1357,1358,1359,1360,1361,1362,1363);
        if (tanjiro.contains(tempId)) {
           it.options.add(new ItemOption(50, Util.nextInt(1, 200)));
            it.options.add(new ItemOption(77, Util.nextInt(1, 200)));
            it.options.add(new ItemOption(103, Util.nextInt(1, 200)));
            it.options.add(new ItemOption(98, Util.nextInt(1, 10)));
            it.options.add(new ItemOption(99, Util.nextInt(1, 10)));
            it.options.add(new ItemOption(18, Util.nextInt(1, 3000)));
            it.options.add(new ItemOption(16, Util.nextInt(1, 10)));
            it.options.add(new ItemOption(21, Util.nextInt(1, 550)));
            it.options.add(new ItemOption(209, 1)); // đồ rơi từ boss
        }
         if (Util.isTrue(999, 1000)) {
            it.options.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        return it;
    }    
  
   public static ItemMap DEOLUNG(Zone zone, int tempId, int quantity, int x, int y, long playerId) {
        ItemMap it = new ItemMap(zone, tempId, quantity, x, zone.map.yPhysicInTop(x, y - 24), playerId);
        List<Integer> tanjiro = Arrays.asList(1107,1108,1109,1110,865,1329,1330,1331,1482,1583,1584);
        if (tanjiro.contains(tempId)) {
           it.options.add(new ItemOption(50, Util.nextInt(1, 200)));
            it.options.add(new ItemOption(77, Util.nextInt(1, 200)));
            it.options.add(new ItemOption(103, Util.nextInt(1, 200)));
            it.options.add(new ItemOption(98, Util.nextInt(1, 10)));
            it.options.add(new ItemOption(99, Util.nextInt(1, 10)));
            it.options.add(new ItemOption(18, Util.nextInt(1, 30)));
            it.options.add(new ItemOption(16, Util.nextInt(1, 10)));
            it.options.add(new ItemOption(21, Util.nextInt(1, 550)));
            it.options.add(new ItemOption(209, 1)); // đồ rơi từ boss
        }
        if (Util.isTrue(999, 1000)) {
            it.options.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        return it;
    }    
   public static ItemMap LINHTHU(Zone zone, int tempId, int quantity, int x, int y, long playerId) {
        ItemMap it = new ItemMap(zone, tempId, quantity, x, zone.map.yPhysicInTop(x, y - 24), playerId);
        List<Integer> tanjiro = Arrays.asList(1891,1892,1893,1894,1895,1896,1897,1898,1899,1900,1901,1902,1903,1904,1905,1906,1907,1908,1909);
        if (tanjiro.contains(tempId)) {
           it.options.add(new ItemOption(50, Util.nextInt(1, 200)));
            it.options.add(new ItemOption(77, Util.nextInt(1, 200)));
            it.options.add(new ItemOption(103, Util.nextInt(1, 200)));
            it.options.add(new ItemOption(98, Util.nextInt(1, 10)));
            it.options.add(new ItemOption(99, Util.nextInt(1, 10)));
            it.options.add(new ItemOption(18, Util.nextInt(1, 30)));
            it.options.add(new ItemOption(16, Util.nextInt(1, 10)));
            it.options.add(new ItemOption(21, Util.nextInt(1, 550)));
            it.options.add(new ItemOption(209, 1)); // đồ rơi từ boss
        }
        if (Util.isTrue(999, 1000)) {
            it.options.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        return it;
    }    
    public static ItemMap THUCUOI(Zone zone, int tempId, int quantity, int x, int y, long playerId) {
        ItemMap it = new ItemMap(zone, tempId, quantity, x, zone.map.yPhysicInTop(x, y - 24), playerId);
        List<Integer> tanjiro = Arrays.asList(346,347,348,733,734,735,743,744,795,849,897,920,1989,1990);
        if (tanjiro.contains(tempId)) {
           it.options.add(new ItemOption(50, Util.nextInt(1, 300000)));
            it.options.add(new ItemOption(77, Util.nextInt(1, 300000)));
            it.options.add(new ItemOption(103, Util.nextInt(1, 300000)));
            it.options.add(new ItemOption(98, Util.nextInt(1, 10)));
            it.options.add(new ItemOption(99, Util.nextInt(1, 10)));
            it.options.add(new ItemOption(24, Util.nextInt(1, 30)));
            it.options.add(new ItemOption(26, Util.nextInt(1, 10)));
            it.options.add(new ItemOption(217, Util.nextInt(1, 50)));
           
            it.options.add(new ItemOption(209, Util.nextInt(1, 55000)));
        }
        if (Util.isTrue(999, 1000)) {
            it.options.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        return it;
    }    
  
  public static ItemMap danhhieu(Zone zone, int tempId, int quantity, int x, int y, long playerId) {
        ItemMap it = new ItemMap(zone, tempId, quantity, x, zone.map.yPhysicInTop(x, y - 24), playerId);
        List<Integer> tanjiro = Arrays.asList(1710,1711,1712,1713,1714,1715,1716,1717,1718);
        if (tanjiro.contains(tempId)) {
           it.options.add(new ItemOption(50, Util.nextInt(1, 200)));
            it.options.add(new ItemOption(77, Util.nextInt(1, 200)));
            it.options.add(new ItemOption(103, Util.nextInt(1, 200)));
            it.options.add(new ItemOption(95, Util.nextInt(1, 10)));
            it.options.add(new ItemOption(96, Util.nextInt(1, 10)));
            it.options.add(new ItemOption(18, Util.nextInt(1, 3000)));
            it.options.add(new ItemOption(16, Util.nextInt(1, 10)));
            it.options.add(new ItemOption(21, Util.nextInt(1, 550)));
          
            it.options.add(new ItemOption(209, 1)); // đồ rơi từ boss
        }
        if (Util.isTrue(999, 1000)) {
            it.options.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        return it;
    }     
   
   
   
   
    
   
   
   
  
  
  
    
   public static ItemMap setdoHUYDIET(Zone zone, int tempId, int quantity, int x, int y, long playerId) {
        ItemMap it = new ItemMap(zone, tempId, quantity, x, y, playerId);
        List<Integer> aotd = Arrays.asList(650, 652, 654);
        List<Integer> quantd = Arrays.asList(651, 653, 655);
        List<Integer> gangtd = Arrays.asList(657, 659, 661);
        List<Integer> giaytd = Arrays.asList(658, 660, 662);
        List<Integer> nhan = Arrays.asList(656);

        if (aotd.contains((int) tempId)) {
            it.options.add(new ItemOption(47, Util.nextInt(1, 5000000)));
        }
        if (quantd.contains((int) tempId)) {

            it.options.add(new ItemOption(6, Util.nextInt(1, 50000000)));
        }
        if (gangtd.contains((int) tempId)) {

            it.options.add(new ItemOption(0, Util.nextInt(1, 5000000)));
        }
        if (giaytd.contains((int) tempId)) {
            it.options.add(new ItemOption(7, Util.nextInt(1, 50000000)));

        }
        if (nhan.contains((int) tempId)) {
            it.options.add(new ItemOption(14, Util.nextInt(1, 28)));
        }
        if (Util.isTrue(999, 1000)) {
            it.options.add(new ItemOption(93, Util.nextInt(1, 3)));

        }

        return it;
    }   
    
     public static ItemMap setdoHUYDIETvv(Zone zone, int tempId, int quantity, int x, int y, long playerId) {
        ItemMap it = new ItemMap(zone, tempId, quantity, x, y, playerId);
        List<Integer> aotd = Arrays.asList(650, 652, 654);
        List<Integer> quantd = Arrays.asList(651, 653, 655);
        List<Integer> gangtd = Arrays.asList(657, 659, 661);
        List<Integer> giaytd = Arrays.asList(658, 660, 662);
        List<Integer> nhan = Arrays.asList(656);

        if (aotd.contains((int) tempId)) {
            it.options.add(new ItemOption(47, Util.nextInt(1, 10000)));
        }
        if (quantd.contains((int) tempId)) {

            it.options.add(new ItemOption(6, Util.nextInt(1, 600000)));
        }
        if (gangtd.contains((int) tempId)) {

            it.options.add(new ItemOption(0, Util.nextInt(1, 30000)));
        }
        if (giaytd.contains((int) tempId)) {
            it.options.add(new ItemOption(7, Util.nextInt(1, 600000)));

        }
        if (nhan.contains((int) tempId)) {
            it.options.add(new ItemOption(14, Util.nextInt(1, 28)));
        }
        if (Util.isTrue(80, 100)) {
            it.options.add(new ItemOption(93, Util.nextInt(1, 3)));

        }

        return it;
    }   
    
    
    
      private static void TUYCHINHOPTION(ItemMap it, int tempId) {
    int optionId;

    switch (tempId) {
        case 1048: // Áo Trái Đất
        case 1051: // Quần Trái Đất
        case 1054: // Găng tay Trái Đất
        case 1057: // Giày Trái Đất
            optionId = Util.nextInt(127, 129); // Option 127 - 129
            break;
        case 1049: // Áo Namek
        case 1052: // Quần Namek
        case 1055: // Găng tay Namek
        case 1058: // Giày Namek
            optionId = Util.nextInt(130, 132); // Option 130 - 132
            break;
        case 1050: // Áo Saiyan
        case 1053: // Quần Saiyan
        case 1056: // Găng tay Saiyan
        case 1059: // Giày Saiyan
            optionId = Util.nextInt(133, 135); // Option 133 - 135
            break;
        case 1060:
            optionId = Util.nextInt(127, 135); // Option riêng cho NTL (120 - 124)
            break;
        default:
            optionId = Util.nextInt(127, 136);
            break;
    }

    it.options.add(new ItemOption(optionId, 0));
}



   public static ItemMap SKHTHIENSU(Zone zone, int tempId, int quantity, int x, int y, long playerId) {
        ItemMap it = new ItemMap(zone, tempId, quantity, x, y, playerId);
        List<Integer> ao = Arrays.asList(1048, 1049, 1050);
        List<Integer> quan = Arrays.asList(1051, 1052, 1053);
        List<Integer> gang = Arrays.asList(1054, 1055, 1056);
        List<Integer> giay = Arrays.asList(1057, 1058, 1059);
        int ntl = 1060;
        if (ao.contains(tempId)) {
            it.options.add(new ItemOption(47, Util.nextInt(1, 50000)));
        }
        if (quan.contains(tempId)) {
            it.options.add(new ItemOption(6, Util.nextInt(1, 1500000)));
        }
        if (gang.contains(tempId)) {
            it.options.add(new ItemOption(0, Util.nextInt(1, 80000)));
        }
        if (giay.contains(tempId)) {
           it.options.add(new ItemOption(7, Util.nextInt(1, 1500000)));
        }
        if (ntl == tempId) {
           it.options.add(new ItemOption(14, Util.nextInt(1, 20)));
        }
        if (Util.isTrue(1, 100)) {
            it.options.add(new ItemOption(107, Util.nextInt(1, 25)));
            it.options.add(new ItemOption(Util.nextInt(176, 180), Util.nextInt(1, 20))); 
        }
        return it;
    } 
    
    
    
    
    
    
    
    
    
  
  
  
  
  
  
  
  
  
  
  
  
  
  
    
    
    

    public static ItemMap setdotanthutraidathsd(Zone zone, int tempId, int quantity, int x, int y, long playerId) {
        ItemMap it = new ItemMap(zone, tempId, quantity, x, y, playerId);
        List<Integer> aotd = Arrays.asList(555, 557, 559);
        List<Integer> quantd = Arrays.asList(556, 558, 560);
        List<Integer> gangtd = Arrays.asList(564, 566, 562);
        List<Integer> giaytd = Arrays.asList(563, 565, 657);
        List<Integer> nhan = Arrays.asList(561);

        if (aotd.contains((int) tempId)) {
            it.options.add(new ItemOption(47, Util.nextInt(1, 5000)));
        }
        if (quantd.contains((int) tempId)) {

            it.options.add(new ItemOption(6, Util.nextInt(1, 1000000)));
        }
        if (gangtd.contains((int) tempId)) {

            it.options.add(new ItemOption(0, Util.nextInt(1, 20000)));
        }
        if (giaytd.contains((int) tempId)) {
            it.options.add(new ItemOption(7, Util.nextInt(1, 1000000)));

        }
        if (nhan.contains((int) tempId)) {
            it.options.add(new ItemOption(14, Util.nextInt(1, 10)));
        }
         if (Util.isTrue(995, 1000)) {
             it.options.add(new ItemOption(107, Util.nextInt(1, 18)));
          //  it.options.add(new ItemOption(93, Util.nextInt(1, 7)));

        }

        return it;
    }

  

    public static ItemMap setdoblackgoku(Zone zone, int tempId, int quantity, int x, int y, long playerId) {
        ItemMap it = new ItemMap(zone, tempId, quantity, x, y, playerId);
        List<Integer> aotd = Arrays.asList(555, 557, 559);
        List<Integer> quantd = Arrays.asList(556, 558, 560);
        List<Integer> gangtd = Arrays.asList(564, 566, 562);
        List<Integer> giaytd = Arrays.asList(563, 565, 657);
        List<Integer> nhan = Arrays.asList(561);

        if (aotd.contains((int) tempId)) {
            it.options.add(new ItemOption(47, Util.nextInt(1, 20000)));
        }
        if (quantd.contains((int) tempId)) {

            it.options.add(new ItemOption(6, Util.nextInt(1, 1800000)));
        }
        if (gangtd.contains((int) tempId)) {

            it.options.add(new ItemOption(0, Util.nextInt(1, 70000)));
        }
        if (giaytd.contains((int) tempId)) {
            it.options.add(new ItemOption(7, Util.nextInt(1, 1800000)));

        }
        if (nhan.contains((int) tempId)) {
            it.options.add(new ItemOption(14, Util.nextInt(1, 18)));
        }
        if (Util.isTrue(990, 1000)) {
            it.options.add(new ItemOption(107, Util.nextInt(1, 18)));

        }

        return it;
    }

  

    public static ItemMap caitrangFIDEVV(Zone zone, int tempId, int quantity, int x, int y, long playerId) {
        ItemMap it = new ItemMap(zone, tempId, quantity, x, zone.map.yPhysicInTop(x, y - 24), playerId);
        List<Integer> tanjiro = Arrays.asList(405, 406, 407,629);
        if (tanjiro.contains(tempId)) {
            it.options.add(new ItemOption(50, Util.nextInt(1, 5000)));
            it.options.add(new ItemOption(77, Util.nextInt(1, 5000)));
            it.options.add(new ItemOption(103, Util.nextInt(1, 5000)));
            it.options.add(new ItemOption(98, Util.nextInt(1, 20)));
            it.options.add(new ItemOption(99, Util.nextInt(1, 20)));
            it.options.add(new ItemOption(181, Util.nextInt(1, 10)));
            it.options.add(new ItemOption(209, 1)); // đồ rơi từ boss
        }
        if (Util.isTrue(999, 1000)) {
            it.options.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        return it;
    }

    public static ItemMap caitrangFIDE(Zone zone, int tempId, int quantity, int x, int y, long playerId) {
        ItemMap it = new ItemMap(zone, tempId, quantity, x, zone.map.yPhysicInTop(x, y - 24), playerId);
        List<Integer> tanjiro = Arrays.asList(405, 406, 407,629);
        if (tanjiro.contains(tempId)) {
            it.options.add(new ItemOption(50, Util.nextInt(1, 2500)));
            it.options.add(new ItemOption(77, Util.nextInt(1, 2500)));
            it.options.add(new ItemOption(103, Util.nextInt(1, 2500)));
            it.options.add(new ItemOption(101, Util.nextInt(1, 10)));
            it.options.add(new ItemOption(98, Util.nextInt(1, 10)));
            it.options.add(new ItemOption(99, Util.nextInt(1, 10)));
            it.options.add(new ItemOption(209, 1)); // đồ rơi từ boss
        }
       if (Util.isTrue(999, 1000)) {
            it.options.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        return it;
    }
    
    
      public static ItemMap fidegoddanhhieu(Zone zone, int tempId, int quantity, int x, int y, long playerId) {
        ItemMap it = new ItemMap(zone, tempId, quantity, x, zone.map.yPhysicInTop(x, y - 24), playerId);
        List<Integer> tanjiro = Arrays.asList(1710,1711,1712,1713,1714,1715,1716,1718);
        if (tanjiro.contains(tempId)) {
            it.options.add(new ItemOption(50, Util.nextInt(1, 100000)));
            it.options.add(new ItemOption(77, Util.nextInt(1, 100000)));
            it.options.add(new ItemOption(103, Util.nextInt(1, 100000)));
            it.options.add(new ItemOption(101, Util.nextInt(1, 1000)));
            it.options.add(new ItemOption(5, Util.nextInt(1, 10)));
            it.options.add(new ItemOption(14, Util.nextInt(1, 10)));
            it.options.add(new ItemOption(209, Util.nextInt(1, 10000)));
        }
         if (Util.isTrue(999, 1000)) {
            it.options.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        return it;
    }
    

    public static ItemMap caitrangADR(Zone zone, int tempId, int quantity, int x, int y, long playerId) {
        ItemMap it = new ItemMap(zone, tempId, quantity, x, zone.map.yPhysicInTop(x, y - 24), playerId);
        List<Integer> tanjiro = Arrays.asList(524, 525);
        if (tanjiro.contains(tempId)) {
            it.options.add(new ItemOption(50, Util.nextInt(1, 250)));
            it.options.add(new ItemOption(77, Util.nextInt(1, 250)));
            it.options.add(new ItemOption(103, Util.nextInt(1, 250)));
            it.options.add(new ItemOption(78, Util.nextInt(1, 250)));
            it.options.add(new ItemOption(88, Util.nextInt(1, 10)));
            it.options.add(new ItemOption(14, Util.nextInt(1, 10)));
            it.options.add(new ItemOption(5, Util.nextInt(1, 10)));
            it.options.add(new ItemOption(21, Util.nextInt(1, 550)));
            it.options.add(new ItemOption(107, Util.nextInt(1, 6)));
            it.options.add(new ItemOption(209, 1)); // đồ rơi từ boss
        }
        if (Util.isTrue(100, 100)) {
            it.options.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        return it;
    }

    public static ItemMap caitrangADRvv(Zone zone, int tempId, int quantity, int x, int y, long playerId) {
        ItemMap it = new ItemMap(zone, tempId, quantity, x, zone.map.yPhysicInTop(x, y - 24), playerId);
        List<Integer> tanjiro = Arrays.asList(524, 525);
        if (tanjiro.contains(tempId)) {
            it.options.add(new ItemOption(50, Util.nextInt(1, 250)));
            it.options.add(new ItemOption(77, Util.nextInt(1, 250)));
            it.options.add(new ItemOption(103, Util.nextInt(1, 250)));
            it.options.add(new ItemOption(78, Util.nextInt(1, 250)));
            it.options.add(new ItemOption(88, Util.nextInt(1, 100)));
            it.options.add(new ItemOption(14, Util.nextInt(1, 5)));
            it.options.add(new ItemOption(5, Util.nextInt(1, 20)));
            it.options.add(new ItemOption(21, Util.nextInt(1, 550)));
            it.options.add(new ItemOption(107, Util.nextInt(1, 6)));
            it.options.add(new ItemOption(209, 1)); // đồ rơi từ boss
        }
        if (Util.isTrue(10, 100)) {
            it.options.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        return it;
    }

    public static ItemMap caitrangPOCPIC(Zone zone, int tempId, int quantity, int x, int y, long playerId) {
        ItemMap it = new ItemMap(zone, tempId, quantity, x, zone.map.yPhysicInTop(x, y - 24), playerId);
        List<Integer> tanjiro = Arrays.asList(977, 978);
        if (tanjiro.contains(tempId)) {
            it.options.add(new ItemOption(50, Util.nextInt(1, 300)));
            it.options.add(new ItemOption(77, Util.nextInt(1, 300)));
            it.options.add(new ItemOption(103, Util.nextInt(1, 300)));
            it.options.add(new ItemOption(78, Util.nextInt(1, 250)));
            it.options.add(new ItemOption(88, Util.nextInt(1, 10)));
            it.options.add(new ItemOption(14, Util.nextInt(1, 5)));
            it.options.add(new ItemOption(5, Util.nextInt(1, 3)));
            it.options.add(new ItemOption(21, Util.nextInt(1, 550)));
            it.options.add(new ItemOption(107, Util.nextInt(1, 6)));
            it.options.add(new ItemOption(209, 1)); // đồ rơi từ boss
        }
       if (Util.isTrue(999, 1000)) {
            it.options.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        return it;
    }

    public static ItemMap caitrangPOCPICVV(Zone zone, int tempId, int quantity, int x, int y, long playerId) {
        ItemMap it = new ItemMap(zone, tempId, quantity, x, zone.map.yPhysicInTop(x, y - 24), playerId);
        List<Integer> tanjiro = Arrays.asList(977, 978);
        if (tanjiro.contains(tempId)) {
            it.options.add(new ItemOption(50, Util.nextInt(1, 350)));
            it.options.add(new ItemOption(77, Util.nextInt(1, 350)));
            it.options.add(new ItemOption(103, Util.nextInt(1, 350)));
            it.options.add(new ItemOption(78, Util.nextInt(1, 250)));
            it.options.add(new ItemOption(88, Util.nextInt(1, 10)));
            it.options.add(new ItemOption(14, Util.nextInt(1, 3)));
            it.options.add(new ItemOption(5, Util.nextInt(1, 3)));
            it.options.add(new ItemOption(21, Util.nextInt(1, 550)));
            it.options.add(new ItemOption(107, Util.nextInt(1, 6)));
            it.options.add(new ItemOption(209, 1)); // đồ rơi từ boss
        }
        if (Util.isTrue(999, 1000)) {
            it.options.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        return it;
    }

    public static ItemMap caitrangxenbohung(Zone zone, int tempId, int quantity, int x, int y, long playerId) {
        ItemMap it = new ItemMap(zone, tempId, quantity, x, zone.map.yPhysicInTop(x, y - 24), playerId);
        List<Integer> tanjiro = Arrays.asList(526, 527, 528);
        if (tanjiro.contains(tempId)) {
            it.options.add(new ItemOption(50, Util.nextInt(1, 400)));
            it.options.add(new ItemOption(77, Util.nextInt(1, 400)));
            it.options.add(new ItemOption(103, Util.nextInt(1, 400)));
            it.options.add(new ItemOption(78, Util.nextInt(1, 250)));
            it.options.add(new ItemOption(88, Util.nextInt(1, 10)));
            it.options.add(new ItemOption(14, Util.nextInt(1, 3)));
            it.options.add(new ItemOption(5, Util.nextInt(1, 3)));
            it.options.add(new ItemOption(21, Util.nextInt(1, 550)));
            it.options.add(new ItemOption(107, Util.nextInt(1, 6)));
            it.options.add(new ItemOption(209, 1)); // đồ rơi từ boss
        }
        if (Util.isTrue(999, 1000)) {
            it.options.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        return it;
    }

    public static ItemMap caitrangxenbohungvv(Zone zone, int tempId, int quantity, int x, int y, long playerId) {
        ItemMap it = new ItemMap(zone, tempId, quantity, x, zone.map.yPhysicInTop(x, y - 24), playerId);
        List<Integer> tanjiro = Arrays.asList(526, 527, 528);
        if (tanjiro.contains(tempId)) {
            it.options.add(new ItemOption(50, Util.nextInt(1, 450)));
            it.options.add(new ItemOption(77, Util.nextInt(1, 450)));
            it.options.add(new ItemOption(103, Util.nextInt(1, 450)));
            it.options.add(new ItemOption(78, Util.nextInt(1, 250)));
            it.options.add(new ItemOption(88, Util.nextInt(1, 10)));
            it.options.add(new ItemOption(14, Util.nextInt(1, 5)));
            it.options.add(new ItemOption(5, Util.nextInt(1, 5)));
            it.options.add(new ItemOption(21, Util.nextInt(1, 550)));
            it.options.add(new ItemOption(107, Util.nextInt(1, 6)));
            it.options.add(new ItemOption(209, 1)); // đồ rơi từ boss
        }
       if (Util.isTrue(999, 1000)) {
            it.options.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        return it;
    }

    public static ItemMap caitrangxenCON(Zone zone, int tempId, int quantity, int x, int y, long playerId) {
        ItemMap it = new ItemMap(zone, tempId, quantity, x, zone.map.yPhysicInTop(x, y - 24), playerId);
        List<Integer> tanjiro = Arrays.asList(549);
        if (tanjiro.contains(tempId)) {
            it.options.add(new ItemOption(50, Util.nextInt(1, 5000)));
            it.options.add(new ItemOption(77, Util.nextInt(1, 5000)));
            it.options.add(new ItemOption(103, Util.nextInt(1, 5000)));
            it.options.add(new ItemOption(78, Util.nextInt(1, 500)));
            it.options.add(new ItemOption(88, Util.nextInt(1, 10)));
            it.options.add(new ItemOption(14, Util.nextInt(1, 5)));
            it.options.add(new ItemOption(5, Util.nextInt(1, 5)));
            it.options.add(new ItemOption(21, Util.nextInt(1, 550)));
            it.options.add(new ItemOption(107, Util.nextInt(1, 6)));
            it.options.add(new ItemOption(209, 1)); // đồ rơi từ boss
        }
       if (Util.isTrue(999, 1000)) {
            it.options.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        return it;
    }

  

    public static ItemMap linhthuvv(Zone zone, int tempId, int quantity, int x, int y, long playerId) {
        ItemMap it = new ItemMap(zone, tempId, quantity, x, zone.map.yPhysicInTop(x, y - 24), playerId);
        List<Integer> tanjiro = Arrays.asList(1891, 1892, 1893, 1894, 1895, 1896, 1897, 1898, 1899, 1900, 1901, 1902, 1903, 1904, 1905, 1906, 1907, 1908, 1909);
        if (tanjiro.contains(tempId)) {
            it.options.add(new ItemOption(50, Util.nextInt(1, 200)));
            it.options.add(new ItemOption(77, Util.nextInt(1, 200)));
            it.options.add(new ItemOption(103, Util.nextInt(1, 200)));
            it.options.add(new ItemOption(14, Util.nextInt(1, 10)));
            it.options.add(new ItemOption(5, Util.nextInt(1, 20)));

            it.options.add(new ItemOption(209, 1)); // đồ rơi từ boss
        }
        if (Util.isTrue(10, 100)) {
            it.options.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        return it;
    }

    public static ItemMap linhthu(Zone zone, int tempId, int quantity, int x, int y, long playerId) {
        ItemMap it = new ItemMap(zone, tempId, quantity, x, zone.map.yPhysicInTop(x, y - 24), playerId);
        List<Integer> tanjiro = Arrays.asList(1891, 1892, 1893, 1894, 1895, 1896, 1897, 1898, 1899, 1900, 1901, 1902, 1903, 1904, 1905, 1906, 1907, 1908, 1909);
        if (tanjiro.contains(tempId)) {
            it.options.add(new ItemOption(50, Util.nextInt(1, 30000)));
            it.options.add(new ItemOption(77, Util.nextInt(1, 30000)));
            it.options.add(new ItemOption(103, Util.nextInt(1, 30000)));
            it.options.add(new ItemOption(14, Util.nextInt(1, 10)));
            it.options.add(new ItemOption(5, Util.nextInt(1, 50)));

           
            it.options.add(new ItemOption(209, Util.nextInt(1, 5000)));
        }
        if (Util.isTrue(999, 1000)) {
            it.options.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        return it;
    }

  

    public static ItemMap caitrangdoremon(Zone zone, int tempId, int quantity, int x, int y, long playerId) {
        ItemMap it = new ItemMap(zone, tempId, quantity, x, zone.map.yPhysicInTop(x, y - 24), playerId);
        List<Integer> tanjiro = Arrays.asList(863, 864, 862, 819, 806);
        if (tanjiro.contains(tempId)) {
            it.options.add(new ItemOption(50, Util.nextInt(1, 10000)));
            it.options.add(new ItemOption(77, Util.nextInt(1, 10000)));
            it.options.add(new ItemOption(103, Util.nextInt(1, 10000)));
            it.options.add(new ItemOption(95, Util.nextInt(1, 35)));
            it.options.add(new ItemOption(96, Util.nextInt(1, 10)));
            it.options.add(new ItemOption(209, Util.nextInt(1, 500)));
        }
         if (Util.isTrue(999, 1000)) {
            it.options.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        return it;
    }

    public static ItemMap caitrangblack(Zone zone, int tempId, int quantity, int x, int y, long playerId) {
        ItemMap it = new ItemMap(zone, tempId, quantity, x, zone.map.yPhysicInTop(x, y - 24), playerId);
        List<Integer> tanjiro = Arrays.asList(904, 883, 898);
        if (tanjiro.contains(tempId)) {
            it.options.add(new ItemOption(50, Util.nextInt(1, 18000)));
            it.options.add(new ItemOption(77, Util.nextInt(1, 18000)));
            it.options.add(new ItemOption(103, Util.nextInt(1, 18000)));
            it.options.add(new ItemOption(4, Util.nextInt(1, 3)));
            it.options.add(new ItemOption(5, Util.nextInt(1, 10)));
            it.options.add(new ItemOption(209, Util.nextInt(1, 500)));
        }
        if (Util.isTrue(999, 1000)) {
            it.options.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        return it;
    }
  public static ItemMap chanmenhblack(Zone zone, int tempId, int quantity, int x, int y, long playerId) {
        ItemMap it = new ItemMap(zone, tempId, quantity, x, zone.map.yPhysicInTop(x, y - 24), playerId);
        List<Integer> tanjiro = Arrays.asList(1356,1357,1358,1359,1360,1361,1362,1363,1364);
        if (tanjiro.contains(tempId)) {
            it.options.add(new ItemOption(50, Util.nextInt(1, 18000)));
            it.options.add(new ItemOption(77, Util.nextInt(1, 18000)));
            it.options.add(new ItemOption(103, Util.nextInt(1, 18000)));
            it.options.add(new ItemOption(4, Util.nextInt(1, 3)));
            it.options.add(new ItemOption(5, Util.nextInt(1, 10)));
            it.options.add(new ItemOption(209, Util.nextInt(1, 500)));
        }
        if (Util.isTrue(999, 1000)) {
            it.options.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        return it;
    }
    public static ItemMap zamasuglt(Zone zone, int tempId, int quantity, int x, int y, long playerId) {
        ItemMap it = new ItemMap(zone, tempId, quantity, x, zone.map.yPhysicInTop(x, y - 24), playerId);
        List<Integer> tanjiro = Arrays.asList(1112);
        if (tanjiro.contains(tempId)) {
            it.options.add(new ItemOption(50, Util.nextInt(1, 10000)));
            it.options.add(new ItemOption(77, Util.nextInt(1, 10000)));
            it.options.add(new ItemOption(103, Util.nextInt(1, 10000)));
            it.options.add(new ItemOption(4, Util.nextInt(1, 3)));
            it.options.add(new ItemOption(5, Util.nextInt(1, 10)));
            it.options.add(new ItemOption(209, Util.nextInt(1, 500)));
        }
        if (Util.isTrue(999, 1000)) {
            it.options.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        return it;
    }
  
  
  
  

    public static ItemMap caitrangtraidat(Zone zone, int tempId, int quantity, int x, int y, long playerId) {
        ItemMap it = new ItemMap(zone, tempId, quantity, x, zone.map.yPhysicInTop(x, y - 24), playerId);
        List<Integer> tanjiro = Arrays.asList(423, 424, 425, 426, 427, 426);
        if (tanjiro.contains(tempId)) {
            it.options.add(new ItemOption(50, Util.nextInt(1, 15000)));
            it.options.add(new ItemOption(77, Util.nextInt(1, 15000)));
            it.options.add(new ItemOption(103, Util.nextInt(1, 15000)));
             it.options.add(new ItemOption(0, Util.nextInt(1, 35000)));
              it.options.add(new ItemOption(2, Util.nextInt(1, 3500)));
            it.options.add(new ItemOption(181, Util.nextInt(1, 35)));
            it.options.add(new ItemOption(80, Util.nextInt(1, 10)));
            it.options.add(new ItemOption(81, Util.nextInt(1, 10)));
            it.options.add(new ItemOption(107, Util.nextInt(1, 20)));
            it.options.add(new ItemOption(209, 1)); // đồ rơi từ boss
        }
         if (Util.isTrue(999, 1000)) {
            it.options.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        return it;
    }

    
  

    public static ItemMap caitrangtdst(Zone zone, int tempId, int quantity, int x, int y, long playerId) {
        ItemMap it = new ItemMap(zone, tempId, quantity, x, zone.map.yPhysicInTop(x, y - 24), playerId);
        List<Integer> tanjiro = Arrays.asList(429, 430, 431, 432, 433);
        if (tanjiro.contains(tempId)) {
            it.options.add(new ItemOption(50, Util.nextInt(1, 100)));
            it.options.add(new ItemOption(77, Util.nextInt(1, 100)));
            it.options.add(new ItemOption(103, Util.nextInt(1, 100)));
            it.options.add(new ItemOption(78, Util.nextInt(1, 10)));
            it.options.add(new ItemOption(95, Util.nextInt(1, 2)));
            it.options.add(new ItemOption(96, Util.nextInt(1, 2)));
            it.options.add(new ItemOption(209, 1)); // đồ rơi từ boss
        }
        if (Util.isTrue(100, 100)) {
            it.options.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        return it;
    }

    public static ItemMap caitrangtdstvv(Zone zone, int tempId, int quantity, int x, int y, long playerId) {
        ItemMap it = new ItemMap(zone, tempId, quantity, x, zone.map.yPhysicInTop(x, y - 24), playerId);
        List<Integer> tanjiro = Arrays.asList(429, 430, 431, 432, 433);
        if (tanjiro.contains(tempId)) {
            it.options.add(new ItemOption(50, Util.nextInt(1, 60)));
            it.options.add(new ItemOption(77, Util.nextInt(1, 60)));
            it.options.add(new ItemOption(103, Util.nextInt(1, 60)));
            it.options.add(new ItemOption(78, Util.nextInt(1, 20)));
            it.options.add(new ItemOption(95, Util.nextInt(1, 10)));
            it.options.add(new ItemOption(96, Util.nextInt(1, 10)));
            it.options.add(new ItemOption(209, 1)); // đồ rơi từ boss
        }
        if (Util.isTrue(1, 100)) {
            it.options.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        return it;
    }

    public static Item reward8thang3(int tempId) {
        Item reward = ItemService.gI().createNewItem((short) tempId);
        int random = new Random().nextInt(100);
        if (Util.isTrue(1, 1)) {
            if (random < 20) {
                reward.itemOptions.add(new ItemOption(50, Util.nextInt(20, 30)));
            } else if (random < 40) {
                reward.itemOptions.add(new ItemOption(103, Util.nextInt(20, 30)));
            } else if (random < 60) {
                reward.itemOptions.add(new ItemOption(5, Util.nextInt(20, 30)));
            } else if (random < 80) {
                reward.itemOptions.add(new ItemOption(77, Util.nextInt(20, 30)));
            } else {
                reward.itemOptions.add(new ItemOption(14, Util.nextInt(20, 30)));
            }
            if (Util.isTrue(90, 100)) {
                reward.itemOptions.add(new ItemOption(93, Util.nextInt(1, 2)));

            }

        }
        return reward;
    }

    /////////////////////////////////////////////////////////////////////////////////////////
    public static ItemMap setnm(Zone zone, int tempId, int quantity, int x, int y, long playerId) {
        ItemMap it = new ItemMap(zone, tempId, quantity, x, y, playerId);
        List<Integer> aotd = Arrays.asList(1);
        List<Integer> quantd = Arrays.asList(7);
        List<Integer> gangtd = Arrays.asList(22);
        List<Integer> giaytd = Arrays.asList(28);
        List<Integer> nhan = Arrays.asList(12);

        if (aotd.contains((int) tempId)) {
            it.options.add(new ItemOption(47, Util.nextInt(1, 10)));
        }
        if (quantd.contains((int) tempId)) {

            it.options.add(new ItemOption(6, Util.nextInt(1, 20)));
        }
        if (gangtd.contains((int) tempId)) {

            it.options.add(new ItemOption(0, Util.nextInt(1, 20)));
        }
        if (giaytd.contains((int) tempId)) {
            it.options.add(new ItemOption(7, Util.nextInt(1, 20)));

        }
        if (nhan.contains((int) tempId)) {
            it.options.add(new ItemOption(14, Util.nextInt(1, 2)));
        }
        if (Util.isTrue(20, 40)) {
            if (Util.isTrue(60, 100)) {
                it.options.add(new ItemOption(130, 1));
                it.options.add(new ItemOption(142, 1));
            } else if (Util.isTrue(30, 100)) {
                it.options.add(new ItemOption(132, 1));
                it.options.add(new ItemOption(144, 1));
            } else {
                it.options.add(new ItemOption(131, 1));
                it.options.add(new ItemOption(143, 1));
            }
        }
        return it;
    }

    ////////////////////////////////////////////////////////////////////////////////////
    public static ItemMap setxd(Zone zone, int tempId, int quantity, int x, int y, long playerId) {
        ItemMap it = new ItemMap(zone, tempId, quantity, x, y, playerId);
        List<Integer> aotd = Arrays.asList(2);
        List<Integer> quantd = Arrays.asList(8);
        List<Integer> gangtd = Arrays.asList(23);
        List<Integer> giaytd = Arrays.asList(29);
        List<Integer> nhan = Arrays.asList(12);

        if (aotd.contains((int) tempId)) {
            it.options.add(new ItemOption(47, Util.nextInt(1, 10)));
        }
        if (quantd.contains((int) tempId)) {

            it.options.add(new ItemOption(6, Util.nextInt(1, 20)));
        }
        if (gangtd.contains((int) tempId)) {

            it.options.add(new ItemOption(0, Util.nextInt(1, 20)));
        }
        if (giaytd.contains((int) tempId)) {
            it.options.add(new ItemOption(7, Util.nextInt(1, 20)));

        }
        if (nhan.contains((int) tempId)) {
            it.options.add(new ItemOption(14, Util.nextInt(1, 2)));
        }
        if (Util.isTrue(20, 40)) {
            if (Util.isTrue(60, 100)) {
                it.options.add(new ItemOption(133, 1));
                it.options.add(new ItemOption(136, 1));
            } else if (Util.isTrue(30, 100)) {
                it.options.add(new ItemOption(134, 1));
                it.options.add(new ItemOption(137, 1));
            } else {
                it.options.add(new ItemOption(135, 1));
                it.options.add(new ItemOption(138, 1));
            }
        }
        return it;
    }

    public static int currentTimeSec() {
        return (int) System.currentTimeMillis() / 1000;
    }

    public static String replace(String text, String regex, String replacement) {
        return text.replace(regex, replacement);
    }

    public static void debug(String message) {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        String strDate = formatter.format(date);
        try {
            System.err.println(message);
        } catch (Exception e) {
            System.out.println(message);
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 1008; i++) {
            if (!isTrue(104, 100)) {
                System.out.println("xxx");
            }
        }
    }

    public static boolean isTrue(int ratio, int typeRatio) {
        int num = Util.nextInt(typeRatio);
        if (num < ratio) {
            return true;
        }
        return false;
    }

    public static boolean isTrue(float ratio, int typeRatio) {
        if (ratio < 1) {
            ratio *= 10;
            typeRatio *= 10;
        }
        int num = Util.nextInt(typeRatio);
        if (num < ratio) {
            return true;
        }
        return false;
    }

    public static boolean haveSpecialCharacter(String text) {
        Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(text);
        boolean b = m.find();
        return b || text.contains(" ");
    }

    public static boolean canDoWithTime(long lastTime, long miniTimeTarget) {
        return System.currentTimeMillis() - lastTime > miniTimeTarget;
    }

    private static final char[] SOURCE_CHARACTERS = {'À', 'Á', 'Â', 'Ã', 'È', 'É',
        'Ê', 'Ì', 'Í', 'Ò', 'Ó', 'Ô', 'Õ', 'Ù', 'Ú', 'Ý', 'à', 'á', 'â',
        'ã', 'è', 'é', 'ê', 'ì', 'í', 'ò', 'ó', 'ô', 'õ', 'ù', 'ú', 'ý',
        'Ă', 'ă', 'Đ', 'đ', 'Ĩ', 'ĩ', 'Ũ', 'ũ', 'Ơ', 'ơ', 'Ư', 'ư', 'Ạ',
        'ạ', 'Ả', 'ả', 'Ấ', 'ấ', 'Ầ', 'ầ', 'Ẩ', 'ẩ', 'Ẫ', 'ẫ', 'Ậ', 'ậ',
        'Ắ', 'ắ', 'Ằ', 'ằ', 'Ẳ', 'ẳ', 'Ẵ', 'ẵ', 'Ặ', 'ặ', 'Ẹ', 'ẹ', 'Ẻ',
        'ẻ', 'Ẽ', 'ẽ', 'Ế', 'ế', 'Ề', 'ề', 'Ể', 'ể', 'Ễ', 'ễ', 'Ệ', 'ệ',
        'Ỉ', 'ỉ', 'Ị', 'ị', 'Ọ', 'ọ', 'Ỏ', 'ỏ', 'Ố', 'ố', 'Ồ', 'ồ', 'Ổ',
        'ổ', 'Ỗ', 'ỗ', 'Ộ', 'ộ', 'Ớ', 'ớ', 'Ờ', 'ờ', 'Ở', 'ở', 'Ỡ', 'ỡ',
        'Ợ', 'ợ', 'Ụ', 'ụ', 'Ủ', 'ủ', 'Ứ', 'ứ', 'Ừ', 'ừ', 'Ử', 'ử', 'Ữ',
        'ữ', 'Ự', 'ự',};

    private static final char[] DESTINATION_CHARACTERS = {'A', 'A', 'A', 'A', 'E',
        'E', 'E', 'I', 'I', 'O', 'O', 'O', 'O', 'U', 'U', 'Y', 'a', 'a',
        'a', 'a', 'e', 'e', 'e', 'i', 'i', 'o', 'o', 'o', 'o', 'u', 'u',
        'y', 'A', 'a', 'D', 'd', 'I', 'i', 'U', 'u', 'O', 'o', 'U', 'u',
        'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A',
        'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'E', 'e',
        'E', 'e', 'E', 'e', 'E', 'e', 'E', 'e', 'E', 'e', 'E', 'e', 'E',
        'e', 'I', 'i', 'I', 'i', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o',
        'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O',
        'o', 'O', 'o', 'U', 'u', 'U', 'u', 'U', 'u', 'U', 'u', 'U', 'u',
        'U', 'u', 'U', 'u',};

    public static char removeAccent(char ch) {
        int index = Arrays.binarySearch(SOURCE_CHARACTERS, ch);
        if (index >= 0) {
            ch = DESTINATION_CHARACTERS[index];
        }
        return ch;
    }

    public static String removeAccent(String str) {
        StringBuilder sb = new StringBuilder(str);
        for (int i = 0; i < sb.length(); i++) {
            sb.setCharAt(i, removeAccent(sb.charAt(i)));
        }
        return sb.toString();
    }

    public static int timeToInt(int d, int h, int m) {
        int result = 0;
        try {
            if (d > 0) {
                result += (60 * 60 * 24 * d);
            }
            if (h > 0) {
                result += 60 * 60 * h;
            }
            if (m > 0) {
                result += 60 * m;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String convertSeconds(int sec) {
        int seconds = sec % 60;
        int minutes = sec / 60;
        if (minutes >= 60) {
            int hours = minutes / 60;
            minutes %= 60;
            if (hours >= 24) {
                int days = hours / 24;
                return String.format("%dd%02dh%02d'", days, hours % 24, minutes);
            }
            return String.format("%02dh%02d'", hours, minutes);
        }
        return String.format("%02d'", minutes);
    }

    public static String formatTime(long time) {
        try {
            SimpleDateFormat sdm = new SimpleDateFormat("H%1 m%2");
            String done = sdm.format(new java.util.Date(time));
            done = done.replaceAll("%1", "giờ").replaceAll("%2", "phút");
            return done;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static ItemMap UpDoTL1(Zone zone, int tempId, int quantity, int x, int y, long playerId) {
        ItemMap it = new ItemMap(zone, tempId, quantity, x, y, playerId);
        List<Integer> ao = Arrays.asList(555, 557, 559);
        List<Integer> quan = Arrays.asList(556, 558, 560);
        List<Integer> gang = Arrays.asList(562, 564, 566);
        List<Integer> giay = Arrays.asList(563, 565, 567);
        int ntl = 561;
        if (ao.contains(tempId)) {
            it.options.add(new ItemOption(47, highlightsItem(it.itemTemplate.gender == 0 && it.itemTemplate.gender == 1 && it.itemTemplate.gender == 2, new Random().nextInt(501) + 2000)));
            it.options.add(new ItemOption(72, Util.nextInt(0, 2)));
            it.options.add(new ItemOption(107, Util.nextInt(0, 10)));
            it.options.add(new ItemOption(35, 0));
        }
        if (quan.contains(tempId)) {
            it.options.add(new ItemOption(22, highlightsItem(it.itemTemplate.gender == 0 && it.itemTemplate.gender == 1 && it.itemTemplate.gender == 2, new Random().nextInt(11) + 200)));

            it.options.add(new ItemOption(72, Util.nextInt(0, 2)));
            it.options.add(new ItemOption(107, Util.nextInt(0, 10)));
            it.options.add(new ItemOption(35, 10));
        }
        if (gang.contains(tempId)) {
            it.options.add(new ItemOption(0, highlightsItem(it.itemTemplate.gender == 0 && it.itemTemplate.gender == 1 && it.itemTemplate.gender == 2, new Random().nextInt(1001) + 15500)));

            it.options.add(new ItemOption(72, Util.nextInt(0, 2)));
            it.options.add(new ItemOption(107, Util.nextInt(0, 10)));
            it.options.add(new ItemOption(35, 10));
        }
        if (giay.contains(tempId)) {
            it.options.add(new ItemOption(23, highlightsItem(it.itemTemplate.gender == 0 && it.itemTemplate.gender == 1 && it.itemTemplate.gender == 2, new Random().nextInt(11) + 200)));

            it.options.add(new ItemOption(72, Util.nextInt(0, 2)));
            it.options.add(new ItemOption(107, Util.nextInt(0, 10)));
            it.options.add(new ItemOption(35, 10));
        }
        if (ntl == tempId) {
            it.options.add(new ItemOption(14, new Random().nextInt(3) + 15));

            it.options.add(new ItemOption(72, Util.nextInt(0, 2)));
            it.options.add(new ItemOption(107, Util.nextInt(0, 10)));
            it.options.add(new ItemOption(35, 10));
        }
        // it.options.add(new Item.ItemOption(73, 1));
        return it;
    }

    public static short getTimeCanMove(byte speed) {
        switch (speed) {
            case 1:
                return 2000;
            case 2:
                return 1000;
            case 3:
                return 500;
            case 5:
                return 400;
            default:
                return 0;
        }
    }

    public static synchronized boolean compareDay(Date now, Date when) {
        try {
            Date date1 = Util.dateFormatDay.parse(Util.dateFormatDay.format(now));
            Date date2 = Util.dateFormatDay.parse(Util.dateFormatDay.format(when));
            return !date1.equals(date2) && !date1.before(date2);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Date getDate(String str) {
        try {
            return dateFormat.parse(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String toDateString(Date date) {
        try {
            String a = Util.dateFormat.format(date);
            return a;
        } catch (Exception e) {
            Date now = new Date();
            return dateFormat.format(now);
        }
    }

    public static int[] generateArrRandNumber(int from, int to, int size) {
        return rand.ints(from, to).distinct().limit(size).toArray();
    }

    public static int[] pickNRandInArr(int[] array, int n) {
        List<Integer> list = new ArrayList<Integer>(array.length);
        for (int i : array) {
            list.add(i);
        }
        Collections.shuffle(list);
        int[] answer = new int[n];
        for (int i = 0; i < n; i++) {
            answer[i] = list.get(i);
        }
        Arrays.sort(answer);
        return answer;
    }

    public static Object[] addArray(Object[]... arrays) {
        if (arrays == null || arrays.length == 0) {
            return null;
        }
        if (arrays.length == 1) {
            return arrays[0];
        }
        Object[] arr0 = arrays[0];
        for (int i = 1; i < arrays.length; i++) {
            arr0 = ArrayUtils.addAll(arr0, arrays[i]);
        }
        return arr0;
    }
}
