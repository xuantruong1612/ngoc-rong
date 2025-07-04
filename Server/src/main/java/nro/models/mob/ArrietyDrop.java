/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nro.models.mob;

import nro.models.item.Item;
import nro.models.item.ItemOption;
import nro.models.map.ItemMap;
import nro.models.player.Player;
import nro.services.ItemService;
import nro.services.Service;
import nro.utils.Util;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import nro.services.RewardService;

/**
 *
 * @author Béo
 */
public class ArrietyDrop {

    public static final int[][] list_do_than_linh = {
        {555, 556, 562, 563, 561},//td
        {557, 558, 564, 565, 561},//nm
        {559, 560, 566, 567, 561}//xd
    };

    public static final int[] list_thuc_an = new int[]{663, 664, 665, 666, 667};
    public static final int[] list_manh_thien_xu = new int[]{1066, 1067, 1068, 1069, 1070};
    public static final int[] list_manh_than_linh = new int[]{1141, 1142, 1143, 1144, 1145};
    public static final short[] list_dnc = {220, 221, 222, 223, 224};

    public static final int[][] list_do_huy_diet = {
        {650, 651, 657, 658, 656},//td
        {652, 653, 659, 660, 656},//nm
        {654, 655, 661, 662, 656}//xd
    };

    public static final int[][] list_item_SKH = {
        {0, 6, 21, 27, 12},//td
        {1, 7, 22, 28, 12},//nm
        {2, 8, 23, 29, 12}//xd
    };

    public static void DropItemReWard(Player player, int idItem, int soluong, int x, int y) {
        ItemMap item = new ItemMap(player.zone, idItem, soluong, Util.nextInt((x - 50), (x + 50)), y, player.id);
        item.options.add(new ItemOption(30, 0));
        Service.getInstance().dropItemMap(player.zone, item);
    }

    public static boolean IsItemKhongChoGiaoDich(int id) {
        return (id >= 663 && id <= 667);
    }

    public static Item randomCS_DKH(int itemId, byte type, byte gender) {
        Item it = ItemService.gI().createItemSetKichHoat(itemId, 1);
        List<Integer> ao = Arrays.asList(0, 1, 2);
        List<Integer> quan = Arrays.asList(6, 7, 8);
        List<Integer> gang = Arrays.asList(21, 22, 23);
        List<Integer> giay = Arrays.asList(27, 28, 29);
        int rada = 12;
        if (ao.contains(itemId)) {
            it.itemOptions.add(new ItemOption(47, new Random().nextInt(3) + 2)); // áo từ 2-5 giáp
        }
        if (quan.contains(itemId)) {
            it.itemOptions.add(new ItemOption(6, new Random().nextInt(80) + 20)); // hp 20-100
        }
        if (gang.contains(itemId)) {
            it.itemOptions.add(new ItemOption(0, new Random().nextInt(6) + 4)); // sd 4-6
        }
        if (giay.contains(itemId)) {
            it.itemOptions.add(new ItemOption(7, new Random().nextInt(80) + 20)); // ki 20-100
        }
        if (rada == itemId) {
            it.itemOptions.add(new ItemOption(14, 1)); //chí mạng 1%
        }
        int IDSet = Util.nextInt(0, 6);
        switch (gender) {
            case 0:
                if (IDSet <= 2) {
                    it.itemOptions.add(new ItemOption(217, 1));
                    it.itemOptions.add(new ItemOption(229, 50));
                } else if (IDSet <= 4) {
                    it.itemOptions.add(new ItemOption(218, 1));
                    it.itemOptions.add(new ItemOption(230, 50));
                } else {
                    it.itemOptions.add(new ItemOption(219, 1));
                    it.itemOptions.add(new ItemOption(231, 50));
                }
                break;
            case 1:
                if (IDSet <= 2) {
                    it.itemOptions.add(new ItemOption(220, 1));
                    it.itemOptions.add(new ItemOption(232, 50));
                } else if (IDSet <= 4) {
                    it.itemOptions.add(new ItemOption(221, 1));
                    it.itemOptions.add(new ItemOption(233, 50));
                } else {
                    it.itemOptions.add(new ItemOption(222, 1));
                    it.itemOptions.add(new ItemOption(234, 50));
                }
                break;
            case 2:
                if (IDSet <= 2) {
                    it.itemOptions.add(new ItemOption(223, 1));
                    it.itemOptions.add(new ItemOption(226, 50));
                } else if (IDSet <= 4) {
                    it.itemOptions.add(new ItemOption(224, 1));
                    it.itemOptions.add(new ItemOption(227, 50));
                } else {
                    it.itemOptions.add(new ItemOption(225, 1));
                    it.itemOptions.add(new ItemOption(228, 50));
                }
                break;
        }
        if (Util.isTrue(10, 100)) {
            byte numstar = 1;
            int tile = Util.nextInt(100);
            if (tile > 90) {
                numstar = 3;
            } else if (tile > 60) {
                numstar = 2;
            } else {
                numstar = 1;
            }
            it.itemOptions.add(new ItemOption(30, numstar));
        }
        it.itemOptions.add(new ItemOption(30, 0));// ko the gd
        return it;
    }

    public static ItemMap DropItemSetKichHoat(Player player, int soluong, int x, int y) {
        Item itemkichhoat = ArrietyDrop.randomCS_DKH(ArrietyDrop.list_item_SKH[player.gender][Util.nextInt(0, 4)], (byte) 0, player.gender);
        ItemMap item = new ItemMap(player.zone, itemkichhoat.template.id, soluong, Util.nextInt((x - 50), (x + 50)), y, player.id);
        item.options = itemkichhoat.itemOptions;
        return item;
    }

    
    public static ItemMap DropItemReWardDoTL(Player player, int soluong, int x, int y) {
        Item itemHuyDiet = RewardService.randomCS_DHD(ArrietyDrop.list_do_than_linh[player.gender][Util.nextInt(0, 4)], player.gender);
        ItemMap item = new ItemMap(player.zone, itemHuyDiet.template.id, soluong, Util.nextInt((x - 50), (x + 50)), y, player.id);
        item.options = itemHuyDiet.itemOptions;
        return item;
    }
}
