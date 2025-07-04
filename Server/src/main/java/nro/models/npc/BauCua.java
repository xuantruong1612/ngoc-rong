
package nro.models.npc;

import java.util.HashMap;
import java.util.Random;
import nro.models.item.Item;
import nro.models.player.Player;
import nro.server.Client;
import nro.services.InventoryService;
import nro.services.ItemService;
import nro.services.Service;
import org.slf4j.Logger;

/**
 *
 * @author Administrator
 */
public class BauCua {

    private static BauCua instance;
    public static String[] elements = {"Bầu", "Cua", "Cá", "C?p", "Gà", "Tôm"};

    public static HashMap<Long, Integer> bauMap = new HashMap<>();
    public static HashMap<Long, Integer> cuaMap = new HashMap<>();
    public static HashMap<Long, Integer> caMap = new HashMap<>();
    public static HashMap<Long, Integer> copMap = new HashMap<>();
    public static HashMap<Long, Integer> gaMap = new HashMap<>();
    public static HashMap<Long, Integer> tomMap = new HashMap<>();

    public static BauCua getInstance() {
        if (instance == null) {
            synchronized (BauCua.class) {
                if (instance == null) {
                    instance = new BauCua();
                }
            }
        }
        return instance;
    }

    public static String result = "Chua có;";

    public static String getRandomElement(String[] elements) {
        Random random = new Random();
        int randomIndex = random.nextInt(elements.length);
        return elements[randomIndex];
    }

    public void chonBau(Player player, int soLuong) {
        try {
            if (player == null) {
         //     // Logger.error("L?i - Ch?n B?u - không tìm th?y nhân v?t");
                return;
            }
            if (soLuong < 0) {
                Service.getInstance().sendThongBao(player, "S? lu?ng không chính xác");
                return;
            }
            if (soLuong > 1000) {
                Service.getInstance().sendThongBao(player, "B?n ch? du?c d?t 1000 th?i / l?n d?t");
                return;
            }
            Item tv = InventoryService.gI().findItemBagByTemp(player, 457);
            if (tv == null) {
                Service.getInstance().sendThongBao(player, "Không tìm th?y th?i vàng");
                return;
            }
            if (tv.quantity < soLuong) {
                Service.getInstance().sendThongBao(player, "Không d? th?i vàng");
                return;
            }
            InventoryService.gI().subQuantityItemsBag(player, tv, soLuong);
            InventoryService.gI().sendItemBags(player);
            if (bauMap.containsKey(player.id)) {
                int currentValue = bauMap.get(player.id).intValue();
                bauMap.put(player.id, currentValue + soLuong);
                Service.getInstance().sendThongBao(player, "B?n dã d?t cu?c B?U - " + soLuong + " th?i vàng");
                return;
            }
            bauMap.put(player.id, soLuong);
            Service.getInstance().sendThongBao(player, "B?n dã d?t cu?c B?U - " + soLuong + " th?i vàng");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void chonCua(Player player, int soLuong) {
        try {
            if (player == null) {
              // Logger.error("L?i - Ch?n Cua - không tìm th?y nhân v?t");
                return;
            }
            if (soLuong < 0) {
                Service.getInstance().sendThongBao(player, "S? lu?ng không chính xác");
                return;
            }
            if (soLuong > 1000) {
               Service.getInstance().sendThongBao(player, "B?n ch? du?c d?t 1000 th?i / l?n d?t");
                return;
            }
            Item tv = InventoryService.gI().findItemBagByTemp(player, 457);
            if (tv == null) {
               Service.getInstance().sendThongBao(player, "Không tìm th?y th?i vàng");
                return;
            }
            if (tv.quantity < soLuong) {
                Service.getInstance().sendThongBao(player, "Không d? th?i vàng");
                return;
            }
            InventoryService.gI().subQuantityItemsBag(player, tv, soLuong);
            InventoryService.gI().sendItemBags(player);
            if (cuaMap.containsKey(player.id)) {
                int currentValue = cuaMap.get(player.id).intValue();
                cuaMap.put(player.id, currentValue + soLuong);
               Service.getInstance().sendThongBao(player, "B?n dã d?t cu?c CUA - " + soLuong + " th?i vàng");
                return;
            }
            cuaMap.put(player.id, soLuong);
           Service.getInstance().sendThongBao(player, "B?n dã d?t cu?c CUA - " + soLuong + " th?i vàng");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void chonCa(Player player, int soLuong) {
        try {
            if (player == null) {
              //  Logger.error("L?i - Ch?n Cá - không tìm th?y nhân v?t");
                return;
            }
            if (soLuong < 0) {
               Service.getInstance().sendThongBao(player, "S? lu?ng không chính xác");
                return;
            }
            if (soLuong > 1000) {
               Service.getInstance().sendThongBao(player, "B?n ch? du?c d?t 1000 th?i / l?n d?t");
                return;
            }
            Item tv = InventoryService.gI().findItemBagByTemp(player, 457);
            if (tv == null) {
               Service.getInstance().sendThongBao(player, "Không tìm th?y th?i vàng");
                return;
            }
            if (tv.quantity < soLuong) {
               Service.getInstance().sendThongBao(player, "Không d? th?i vàng");
                return;
            }
            InventoryService.gI().subQuantityItemsBag(player, tv, soLuong);
            InventoryService.gI().sendItemBags(player);
            if (caMap.containsKey(player.id)) {
                int currentValue = caMap.get(player.id).intValue();
                caMap.put(player.id, currentValue + soLuong);
               Service.getInstance().sendThongBao(player, "B?n dã d?t cu?c CÁ - " + soLuong + " th?i vàng");
                return;
            }
            caMap.put(player.id, soLuong);
           Service.getInstance().sendThongBao(player, "B?n dã d?t cu?c CÁ - " + soLuong + " th?i vàng");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void chonCop(Player player, int soLuong) {
        try {
            if (player == null) {
             // // Logger.error("L?i - Ch?n C?p - không tìm th?y nhân v?t");
                return;
            }
            if (soLuong < 0) {
               Service.getInstance().sendThongBao(player, "S? lu?ng không chính xác");
                return;
            }
            if (soLuong > 1000) {
               Service.getInstance().sendThongBao(player, "B?n ch? du?c d?t 1000 th?i / l?n d?t");
                return;
            }
            Item tv = InventoryService.gI().findItemBagByTemp(player, 457);
            if (tv == null) {
               Service.getInstance().sendThongBao(player, "Không tìm th?y th?i vàng");
                return;
            }
            if (tv.quantity < soLuong) {
               Service.getInstance().sendThongBao(player, "Không d? th?i vàng");
                return;
            }
            InventoryService.gI().subQuantityItemsBag(player, tv, soLuong);
            InventoryService.gI().sendItemBags(player);
            if (copMap.containsKey(player.id)) {
                int currentValue = copMap.get(player.id).intValue();
                copMap.put(player.id, currentValue + soLuong);
               Service.getInstance().sendThongBao(player, "B?n dã d?t cu?c C?P - " + soLuong + " th?i vàng");
                return;
            }
            copMap.put(player.id, soLuong);
           Service.getInstance().sendThongBao(player, "B?n dã d?t cu?c C?P - " + soLuong + " th?i vàng");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void chonGa(Player player, int soLuong) {
        try {
            if (player == null) {
              // Logger.error("L?i - Ch?n Gà - không tìm th?y nhân v?t");
                return;
            }
            if (soLuong < 0) {
               Service.getInstance().sendThongBao(player, "S? lu?ng không chính xác");
                return;
            }
            if (soLuong > 1000) {
               Service.getInstance().sendThongBao(player, "B?n ch? du?c d?t 1000 th?i / l?n d?t");
                return;
            }
            Item tv = InventoryService.gI().findItemBagByTemp(player, 457);
            if (tv == null) {
               Service.getInstance().sendThongBao(player, "Không tìm th?y th?i vàng");
                return;
            }
            if (tv.quantity < soLuong) {
               Service.getInstance().sendThongBao(player, "Không d? th?i vàng");
                return;
            }
            InventoryService.gI().subQuantityItemsBag(player, tv, soLuong);
            InventoryService.gI().sendItemBags(player);
            if (gaMap.containsKey(player.id)) {
                int currentValue = gaMap.get(player.id).intValue();
                gaMap.put(player.id, currentValue + soLuong);
               Service.getInstance().sendThongBao(player, "B?n dã d?t cu?c GÀ - " + soLuong + " th?i vàng");
                return;
            }
            gaMap.put(player.id, soLuong);
           Service.getInstance().sendThongBao(player, "B?n dã d?t cu?c GÀ - " + soLuong + " th?i vàng");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void chonTom(Player player, int soLuong) {
        try {
            if (player == null) {
              // Logger.error("L?i - Ch?n Tôm - không tìm th?y nhân v?t");
                return;
            }
            if (soLuong < 0) {
               Service.getInstance().sendThongBao(player, "S? lu?ng không chính xác");
                return;
            }
            if (soLuong > 1000) {
               Service.getInstance().sendThongBao(player, "B?n ch? du?c d?t 1000 th?i / l?n d?t");
                return;
            }
            Item tv = InventoryService.gI().findItemBagByTemp(player, 457);
            if (tv == null) {
               Service.getInstance().sendThongBao(player, "Không tìm th?y th?i vàng");
                return;
            }
            if (tv.quantity < soLuong) {
               Service.getInstance().sendThongBao(player, "Không d? th?i vàng");
                return;
            }
            InventoryService.gI().subQuantityItemsBag(player, tv, soLuong);
            InventoryService.gI().sendItemBags(player);
            if (tomMap.containsKey(player.id)) {
                int currentValue = tomMap.get(player.id).intValue();
                tomMap.put(player.id, currentValue + soLuong);
               Service.getInstance().sendThongBao(player, "B?n dã d?t cu?c TÔM - " + soLuong + " th?i vàng");
                return;
            }
            tomMap.put(player.id, soLuong);
           Service.getInstance().sendThongBao(player, "B?n dã d?t cu?c TÔM - " + soLuong + " th?i vàng");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getTimeConLai() {
        try {
            return String.valueOf(BauCuaServices.TimeBauCua / 1000);
        } catch (Exception e) {
            return "-1";
        }
    }

    public String getPlayerChoose(Player player) {
        int bau = 0;
        int cua = 0;
        int ca = 0;
        int cop = 0;
        int ga = 0;
        int tom = 0;
        if (bauMap.containsKey(player.id)) {
            bau = bauMap.get(player.id).intValue();
        }
    
        if (cuaMap.containsKey(player.id)) {
            cua = cuaMap.get(player.id).intValue();
        }

        if (caMap.containsKey(player.id)) {
            ca = caMap.get(player.id).intValue();
        }

        if (copMap.containsKey(player.id)) {
            cop = copMap.get(player.id).intValue();
        }

        if (gaMap.containsKey(player.id)) {
            ga = gaMap.get(player.id).intValue();
        }

        if (tomMap.containsKey(player.id)) {
            tom = tomMap.get(player.id).intValue();
        }
        String re = "Cua " + cua + " || Gà " + ga;
        re += " || Cá " + ca + "\nTôm " + tom;
        re += " || C?p " + cop + " || B?u " + bau;
        return re;
    }
    
    
    
    public String getPlayerChoose() {
        int bau = 0;
        int cua = 0;
        int ca = 0;
        int cop = 0;
        int ga = 0;
        int tom = 0;

        for (java.util.Map.Entry<Long, Integer> entry : bauMap.entrySet()) {
            bau += entry.getValue();
        }
         for (java.util.Map.Entry<Long, Integer> entry : tomMap.entrySet()) {
            tom += entry.getValue();
        }
        for (java.util.Map.Entry<Long, Integer> entry : cuaMap.entrySet()) {
            cua += entry.getValue();
        }
        for (java.util.Map.Entry<Long, Integer> entry : caMap.entrySet()) {
            ca += entry.getValue();
        }

        for (java.util.Map.Entry<Long, Integer> entry : copMap.entrySet()) {
            cop += entry.getValue();
        }

        for (java.util.Map.Entry<Long, Integer> entry : gaMap.entrySet()) {
            ga += entry.getValue();
        }

        String re = "Cua " + cua + " || Gà " + ga;
        re += " || Cá " + ca + "\nTôm " + tom;
        re += " || C?p " + cop + " || B?u " + bau;
        return re;
    }

    public static String ketQuaTruoc = "Chua có";

    public String getNPCSay(Player player) {
        try {
            if (player == null) {
                return "L?i không tìm thây nhân v?t";
            }
            String npcSay = "MiniGame B?u Cua\n"
                    + "Th?i gian còn l?i " + getTimeConLai() + " giây"
                    + "K?t qu? tru?c là " + ketQuaTruoc + "\n"
                    + "Server \n" + getPlayerChoose() +"\n"
                    + "B?n dã d?t\n" + getPlayerChoose(player);
            return npcSay;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public static void traoQua() {
        String myRe[] = result.split(",");
        if (myRe.length > 0) {
            for (String str : myRe) {
                if (str.equals("B?u")) {
                    quaTungMap(bauMap);
                } else if (str.equals("Cua")) {
                    quaTungMap(cuaMap);
                } else if (str.equals("Cá")) {
                    quaTungMap(caMap);
                } else if (str.equals("C?p")) {
                    quaTungMap(copMap);
                } else if (str.equals("Gà")) {
                    quaTungMap(gaMap);
                } else if (str.equals("Tôm")) {
                    quaTungMap(cuaMap);
                }
            }
        }
        bauMap.clear();
        cuaMap.clear();
        caMap.clear();
        copMap.clear();
        gaMap.clear();
        cuaMap.clear();
        tomMap.clear();
    }

    public static void quaTungMap(HashMap<Long, Integer> maps) {
        for (java.util.Map.Entry<Long, Integer> entry : maps.entrySet()) {
            Player player = Client.gI().getPlayer(entry.getKey());
            if (player != null) {
                int rewardTV = (int) (entry.getValue() * 1.9);
                Item tv = ItemService.gI().createNewItem((short) 457);
                 InventoryService.gI().addItemBag(player, tv, rewardTV);
                 
                InventoryService.gI().sendItemBags(player);
               Service.getInstance().sendThongBaoOK(player, "Ch?c m?ng\nB?n dã v? b? và nh?n du?c " + rewardTV + " th?i vàng");
            }
        }

    }

}