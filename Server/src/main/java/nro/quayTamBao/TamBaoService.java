package nro.quayTamBao;

import java.util.ArrayList;
import java.util.List;

import nro.models.item.Item;
import nro.models.item.ItemOption;
import nro.models.player.Player;
import nro.server.io.Message;
import nro.services.InventoryService;
import nro.services.ItemService;
import nro.services.Service;
import nro.utils.Util;

public class TamBaoService {

    public static TamBaoService instance;

    public static TamBaoService gI() {
        if (instance == null) {
            instance = new TamBaoService();
        }
        return instance;
    }

    public static List<Item> listItem = new ArrayList<Item>();
    public static List<Item> listItemVip = new ArrayList<Item>();
    public static long VAN_MAY = 100;

    public static void loadItem() {
        //tạo vp id 30
        //    if (Util.isTrue(10, 100)) {
        Item a1 = ItemService.gI().createNewItem((short) (282));
        a1.itemOptions.add(new ItemOption(50, Util.nextInt(1, 200)));
        a1.itemOptions.add(new ItemOption(77, Util.nextInt(1, 200)));
        a1.itemOptions.add(new ItemOption(103, Util.nextInt(1, 200)));
        a1.itemOptions.add(new ItemOption(94, Util.nextInt(1, 100)));
        a1.itemOptions.add(new ItemOption(246, Util.nextInt(1, 100)));
        a1.itemOptions.add(new ItemOption(31, Util.nextInt(1, 100)));
        a1.itemOptions.add(new ItemOption(5, Util.nextInt(1, 30)));
        if (Util.isTrue(95, 100)) {
            a1.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        a1.color = 4;
        listItem.add(a1);

        Item a2 = ItemService.gI().createNewItem((short) (283));
        a2.itemOptions.add(new ItemOption(50, Util.nextInt(1, 200)));
        a2.itemOptions.add(new ItemOption(77, Util.nextInt(1, 200)));
        a2.itemOptions.add(new ItemOption(103, Util.nextInt(1, 200)));
        a2.itemOptions.add(new ItemOption(94, Util.nextInt(1, 100)));
        a2.itemOptions.add(new ItemOption(246, Util.nextInt(1, 100)));
        a2.itemOptions.add(new ItemOption(31, Util.nextInt(1, 100)));
        a2.itemOptions.add(new ItemOption(5, Util.nextInt(1, 30)));
        if (Util.isTrue(95, 100)) {
            a2.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        a2.color = 4;
        listItem.add(a2);

       Item a3 = ItemService.gI().createNewItem((short) (284));
        a3.itemOptions.add(new ItemOption(50, Util.nextInt(1, 200)));
        a3.itemOptions.add(new ItemOption(77, Util.nextInt(1, 200)));
        a3.itemOptions.add(new ItemOption(103, Util.nextInt(1, 200)));
        a3.itemOptions.add(new ItemOption(94, Util.nextInt(1, 100)));
        a3.itemOptions.add(new ItemOption(246, Util.nextInt(1, 100)));
        a3.itemOptions.add(new ItemOption(31, Util.nextInt(1, 100)));
        a3.itemOptions.add(new ItemOption(5, Util.nextInt(1, 30)));
        if (Util.isTrue(95, 100)) {
            a3.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        a3.color = 4;
        listItem.add(a3);

         Item a4 = ItemService.gI().createNewItem((short) (285));
        a4.itemOptions.add(new ItemOption(50, Util.nextInt(1, 200)));
        a4.itemOptions.add(new ItemOption(77, Util.nextInt(1, 200)));
        a4.itemOptions.add(new ItemOption(103, Util.nextInt(1, 200)));
        a4.itemOptions.add(new ItemOption(94, Util.nextInt(1, 100)));
        a4.itemOptions.add(new ItemOption(246, Util.nextInt(1, 100)));
        a4.itemOptions.add(new ItemOption(31, Util.nextInt(1, 100)));
        a4.itemOptions.add(new ItemOption(5, Util.nextInt(1, 30)));
        if (Util.isTrue(95, 100)) {
            a4.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        a4.color = 4;
        listItem.add(a4);

         Item a5 = ItemService.gI().createNewItem((short) (286));
        a5.itemOptions.add(new ItemOption(50, Util.nextInt(1, 200)));
        a5.itemOptions.add(new ItemOption(77, Util.nextInt(1, 200)));
        a5.itemOptions.add(new ItemOption(103, Util.nextInt(1, 200)));
        a5.itemOptions.add(new ItemOption(94, Util.nextInt(1, 100)));
        a5.itemOptions.add(new ItemOption(246, Util.nextInt(1, 100)));
        a5.itemOptions.add(new ItemOption(31, Util.nextInt(1, 100)));
        a5.itemOptions.add(new ItemOption(5, Util.nextInt(1, 30)));
        if (Util.isTrue(95, 100)) {
            a5.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        a5.color = 4;
        listItem.add(a5);

         Item a6 = ItemService.gI().createNewItem((short) (287));
        a6.itemOptions.add(new ItemOption(50, Util.nextInt(1, 200)));
        a6.itemOptions.add(new ItemOption(77, Util.nextInt(1, 200)));
        a6.itemOptions.add(new ItemOption(103, Util.nextInt(1, 200)));
        a6.itemOptions.add(new ItemOption(94, Util.nextInt(1, 100)));
        a6.itemOptions.add(new ItemOption(246, Util.nextInt(1, 100)));
        a6.itemOptions.add(new ItemOption(31, Util.nextInt(1, 100)));
        a6.itemOptions.add(new ItemOption(5, Util.nextInt(1, 30)));
        if (Util.isTrue(95, 100)) {
            a6.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        a6.color = 4;
        listItem.add(a6);

        Item a7 = ItemService.gI().createNewItem((short) (288));
        a7.itemOptions.add(new ItemOption(50, Util.nextInt(1, 200)));
        a7.itemOptions.add(new ItemOption(77, Util.nextInt(1, 200)));
        a7.itemOptions.add(new ItemOption(103, Util.nextInt(1, 200)));
        a7.itemOptions.add(new ItemOption(94, Util.nextInt(1, 100)));
        a7.itemOptions.add(new ItemOption(246, Util.nextInt(1, 100)));
        a7.itemOptions.add(new ItemOption(31, Util.nextInt(1, 100)));
        a7.itemOptions.add(new ItemOption(5, Util.nextInt(1, 30)));
        if (Util.isTrue(95, 100)) {
            a7.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        a7.color = 4;
        listItem.add(a7);

        Item a8 = ItemService.gI().createNewItem((short) (289));
        a8.itemOptions.add(new ItemOption(50, Util.nextInt(1, 200)));
        a8.itemOptions.add(new ItemOption(77, Util.nextInt(1, 200)));
        a8.itemOptions.add(new ItemOption(103, Util.nextInt(1, 200)));
        a8.itemOptions.add(new ItemOption(94, Util.nextInt(1, 100)));
        a8.itemOptions.add(new ItemOption(246, Util.nextInt(1, 100)));
        a8.itemOptions.add(new ItemOption(31, Util.nextInt(1, 100)));
        a8.itemOptions.add(new ItemOption(5, Util.nextInt(1, 30)));
        if (Util.isTrue(95, 100)) {
            a8.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        a8.color = 4;
        listItem.add(a8);

         Item a9 = ItemService.gI().createNewItem((short) (290));
        a9.itemOptions.add(new ItemOption(50, Util.nextInt(1, 200)));
        a9.itemOptions.add(new ItemOption(77, Util.nextInt(1, 200)));
        a9.itemOptions.add(new ItemOption(103, Util.nextInt(1, 200)));
        a9.itemOptions.add(new ItemOption(94, Util.nextInt(1, 100)));
        a9.itemOptions.add(new ItemOption(246, Util.nextInt(1, 100)));
        a9.itemOptions.add(new ItemOption(31, Util.nextInt(1, 100)));
        a9.itemOptions.add(new ItemOption(5, Util.nextInt(1, 30)));
        if (Util.isTrue(95, 100)) {
            a9.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        a9.color = 4;
        listItem.add(a9);

        
        
        
       Item a10 = ItemService.gI().createNewItem((short) (291));
        a10.itemOptions.add(new ItemOption(50, Util.nextInt(1, 200)));
        a10.itemOptions.add(new ItemOption(77, Util.nextInt(1, 200)));
        a10.itemOptions.add(new ItemOption(103, Util.nextInt(1, 200)));
        a10.itemOptions.add(new ItemOption(94, Util.nextInt(1, 100)));
        a10.itemOptions.add(new ItemOption(246, Util.nextInt(1, 100)));
        a10.itemOptions.add(new ItemOption(31, Util.nextInt(1, 100)));
        a10.itemOptions.add(new ItemOption(5, Util.nextInt(1, 30)));
        if (Util.isTrue(95, 100)) {
            a10.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        a10.color = 4;
        listItem.add(a10);

         Item a11 = ItemService.gI().createNewItem((short) (800));
        a11.itemOptions.add(new ItemOption(50, Util.nextInt(1, 99)));
        a11.itemOptions.add(new ItemOption(77, Util.nextInt(1, 99)));
        a11.itemOptions.add(new ItemOption(103, Util.nextInt(1, 99)));
        a11.itemOptions.add(new ItemOption(94, Util.nextInt(1, 100)));
        a11.itemOptions.add(new ItemOption(246, Util.nextInt(1, 100)));
        a11.itemOptions.add(new ItemOption(31, Util.nextInt(1, 100)));
        a11.itemOptions.add(new ItemOption(5, Util.nextInt(1, 30)));
        if (Util.isTrue(95, 100)) {
            a11.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        a11.color = 4;
        listItem.add(a11);

        Item a12 = ItemService.gI().createNewItem((short) (2038));
        a12.itemOptions.add(new ItemOption(72, Util.nextInt(0, 16)));
        a12.color = 1;
        listItem.add(a12);

        Item a13 = ItemService.gI().createNewItem((short) (2038));
        a13.itemOptions.add(new ItemOption(72, Util.nextInt(0, 16)));
        a13.color = 1;
        listItem.add(a13);

        Item a14 = ItemService.gI().createNewItem((short) (2038));
        a14.itemOptions.add(new ItemOption(72, Util.nextInt(0, 16)));
        a14.color = 1;
        listItem.add(a14);

        Item a15 = ItemService.gI().createNewItem((short) (2038));
        a15.itemOptions.add(new ItemOption(72, Util.nextInt(0, 16)));
        a15.color = 1;
        listItem.add(a15);

        Item a16 = ItemService.gI().createNewItem((short) (2038));
        a16.itemOptions.add(new ItemOption(72, Util.nextInt(0, 16)));
        a16.color = 1;
        listItem.add(a16);

        Item a17 = ItemService.gI().createNewItem((short) (2038));
        a17.itemOptions.add(new ItemOption(72, Util.nextInt(0, 16)));
        a17.color = 1;
        listItem.add(a17);

        Item a18 = ItemService.gI().createNewItem((short) (2038));
        a18.itemOptions.add(new ItemOption(72, Util.nextInt(0, 16)));
        a18.color = 1;
        listItem.add(a18);

         Item a19 = ItemService.gI().createNewItem((short) (2038));
        a19.itemOptions.add(new ItemOption(72, Util.nextInt(0, 16)));
        a19.color = 1;
        listItem.add(a19);

        Item a20 = ItemService.gI().createNewItem((short) (993),99);
        a20.itemOptions.add(new ItemOption(31, Util.nextInt(1, 100)));
        if (Util.isTrue(90, 100)) {
            a20.itemOptions.add(new ItemOption(93, Util.nextInt(1, 20)));
        }
        a20.color = 4;
        listItem.add(a20);

       Item a21 = ItemService.gI().createNewItem((short) (801));
        a21.itemOptions.add(new ItemOption(50, Util.nextInt(1, 99)));
        a21.itemOptions.add(new ItemOption(77, Util.nextInt(1, 99)));
        a21.itemOptions.add(new ItemOption(103, Util.nextInt(1, 99)));
        a21.itemOptions.add(new ItemOption(94, Util.nextInt(1, 100)));
        a21.itemOptions.add(new ItemOption(246, Util.nextInt(1, 100)));
        a21.itemOptions.add(new ItemOption(31, Util.nextInt(1, 100)));
        a21.itemOptions.add(new ItemOption(5, Util.nextInt(1, 30)));
        if (Util.isTrue(95, 100)) {
            a21.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        a21.color = 4;
        listItem.add(a21);

        Item a22 = ItemService.gI().createNewItem((short) (2038));
        a22.itemOptions.add(new ItemOption(72, Util.nextInt(0, 16)));
        a22.color = 1;
        listItem.add(a22);

        Item a23 = ItemService.gI().createNewItem((short) (2038));
        a23.itemOptions.add(new ItemOption(72, Util.nextInt(0, 16)));
        a23.color = 1;
        listItem.add(a23);

        Item a24 = ItemService.gI().createNewItem((short) (2038));
        a24.itemOptions.add(new ItemOption(72, Util.nextInt(0, 16)));
        a24.color = 1;
        listItem.add(a24);

        Item a25 = ItemService.gI().createNewItem((short) (2038));
        a25.itemOptions.add(new ItemOption(72, Util.nextInt(0, 16)));
        a25.color = 1;
        listItem.add(a25);

        Item a26 = ItemService.gI().createNewItem((short) (2038));
        a26.itemOptions.add(new ItemOption(72, Util.nextInt(0, 16)));
        a26.color = 1;
        listItem.add(a26);

        Item a27 = ItemService.gI().createNewItem((short) (2038));
        a27.itemOptions.add(new ItemOption(72, Util.nextInt(0, 16)));
        a27.color = 1;
        listItem.add(a27);

        Item a28 = ItemService.gI().createNewItem((short) (2038));
        a28.itemOptions.add(new ItemOption(72, Util.nextInt(0, 16)));
        a28.color = 1;
        listItem.add(a28);

        Item a29 = ItemService.gI().createNewItem((short) (2038));
        a29.itemOptions.add(new ItemOption(72, Util.nextInt(0, 16)));
        a29.color = 1;
        listItem.add(a29);

        Item a30 = ItemService.gI().createNewItem((short) (994),99);
        a30.itemOptions.add(new ItemOption(31, Util.nextInt(1, 100)));
        if (Util.isTrue(90, 100)) {
            a30.itemOptions.add(new ItemOption(93, Util.nextInt(1, 20)));
        }
        a30.color = 4;
        listItem.add(a30);

      Item a31 = ItemService.gI().createNewItem((short) (802));
        a31.itemOptions.add(new ItemOption(50, Util.nextInt(1, 99)));
        a31.itemOptions.add(new ItemOption(77, Util.nextInt(1, 99)));
        a31.itemOptions.add(new ItemOption(103, Util.nextInt(1, 99)));
        a31.itemOptions.add(new ItemOption(94, Util.nextInt(1, 100)));
        a31.itemOptions.add(new ItemOption(246, Util.nextInt(1, 100)));
        a31.itemOptions.add(new ItemOption(31, Util.nextInt(1, 100)));
        a31.itemOptions.add(new ItemOption(5, Util.nextInt(1, 30)));
        if (Util.isTrue(95, 100)) {
            a31.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        a31.color = 4;
        listItem.add(a31);

        Item a32 = ItemService.gI().createNewItem((short) (2038));
        a32.itemOptions.add(new ItemOption(72, Util.nextInt(0, 16)));
        a32.color = 1;
        listItem.add(a32);

        Item a33 = ItemService.gI().createNewItem((short) (2038));
        a33.itemOptions.add(new ItemOption(72, Util.nextInt(0, 16)));
        a33.color = 1;
        listItem.add(a33);

        Item a34 = ItemService.gI().createNewItem((short) (2038));
        a34.itemOptions.add(new ItemOption(72, Util.nextInt(0, 16)));
        a34.color = 1;
        listItem.add(a34);

        Item a35 = ItemService.gI().createNewItem((short) (2038));
        a35.itemOptions.add(new ItemOption(72, Util.nextInt(0, 16)));
        a35.color = 1;
        listItem.add(a35);

        Item a36 = ItemService.gI().createNewItem((short) (2038));
        a36.itemOptions.add(new ItemOption(72, Util.nextInt(0, 16)));
        a36.color = 1;
        listItem.add(a36);

        Item a37 = ItemService.gI().createNewItem((short) (2038));
        a37.itemOptions.add(new ItemOption(72, Util.nextInt(0, 16)));
        a37.color = 1;
        listItem.add(a37);

        Item a38 = ItemService.gI().createNewItem((short) (2038));
        a38.itemOptions.add(new ItemOption(72, Util.nextInt(0, 16)));
        a38.color = 1;
        listItem.add(a38);

        Item a39 = ItemService.gI().createNewItem((short) (2038));
        a39.itemOptions.add(new ItemOption(72, Util.nextInt(0, 16)));
        a39.color = 1;
        listItem.add(a39);

        Item a40 = ItemService.gI().createNewItem((short) (995),99);
        a40.itemOptions.add(new ItemOption(31, Util.nextInt(1, 100)));
        if (Util.isTrue(90, 100)) {
            a40.itemOptions.add(new ItemOption(93, Util.nextInt(1, 20)));
        }
        a40.color = 4;
        listItem.add(a40);

         Item a41 = ItemService.gI().createNewItem((short) (803));
        a41.itemOptions.add(new ItemOption(50, Util.nextInt(1, 99)));
        a41.itemOptions.add(new ItemOption(77, Util.nextInt(1, 99)));
        a41.itemOptions.add(new ItemOption(103, Util.nextInt(1, 99)));
        a41.itemOptions.add(new ItemOption(94, Util.nextInt(1, 100)));
        a41.itemOptions.add(new ItemOption(246, Util.nextInt(1, 100)));
        a41.itemOptions.add(new ItemOption(31, Util.nextInt(1, 100)));
        a41.itemOptions.add(new ItemOption(5, Util.nextInt(1, 30)));
        if (Util.isTrue(95, 100)) {
            a41.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        a41.color = 4;
        listItem.add(a41);

        Item a42 = ItemService.gI().createNewItem((short) (2038));
        a42.itemOptions.add(new ItemOption(30, 1));
        a42.color = 1;
        listItem.add(a42);

        Item a43 = ItemService.gI().createNewItem((short) (2038));
        a43.itemOptions.add(new ItemOption(72, Util.nextInt(0, 16)));
        a43.color = 1;
        listItem.add(a43);

        Item a44 = ItemService.gI().createNewItem((short) (2038));
        a44.itemOptions.add(new ItemOption(72, Util.nextInt(0, 16)));
        a44.color = 1;
        listItem.add(a44);

        Item a45 = ItemService.gI().createNewItem((short) (2038));
        a45.itemOptions.add(new ItemOption(72, Util.nextInt(0, 16)));
        a45.color = 1;
        listItem.add(a45);

        Item a46 = ItemService.gI().createNewItem((short) (2038));
        a46.itemOptions.add(new ItemOption(72, Util.nextInt(0, 16)));
        a46.color = 1;
        listItem.add(a46);

        Item a47 = ItemService.gI().createNewItem((short) (2038));
        a47.itemOptions.add(new ItemOption(72, Util.nextInt(0, 16)));
        a47.color = 1;
        listItem.add(a47);

        Item a48 = ItemService.gI().createNewItem((short) (2038));
        a48.itemOptions.add(new ItemOption(72, Util.nextInt(0, 16)));
        a48.color = 1;
        listItem.add(a48);

        Item a49 = ItemService.gI().createNewItem((short) (2038));
        a49.itemOptions.add(new ItemOption(72, Util.nextInt(0, 16)));
        a49.color = 1;
        listItem.add(a49);

        Item a50 = ItemService.gI().createNewItem((short) (996),99);
        a50.itemOptions.add(new ItemOption(31, Util.nextInt(1, 100)));
        if (Util.isTrue(90, 100)) {
            a50.itemOptions.add(new ItemOption(93, Util.nextInt(1, 20)));
        }
        a20.color = 4;
        listItem.add(a50);

        Item a51 = ItemService.gI().createNewItem((short) (804));
        a51.itemOptions.add(new ItemOption(50, Util.nextInt(1, 99)));
        a51.itemOptions.add(new ItemOption(77, Util.nextInt(1, 99)));
        a51.itemOptions.add(new ItemOption(103, Util.nextInt(1, 99)));
        a51.itemOptions.add(new ItemOption(94, Util.nextInt(1, 100)));
        a51.itemOptions.add(new ItemOption(246, Util.nextInt(1, 100)));
        a51.itemOptions.add(new ItemOption(31, Util.nextInt(1, 100)));
        a51.itemOptions.add(new ItemOption(5, Util.nextInt(1, 30)));
        if (Util.isTrue(95, 100)) {
            a51.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        a51.color = 4;
        listItem.add(a51);

     Item a52 = ItemService.gI().createNewItem((short) (1599));
        a52.itemOptions.add(new ItemOption(50, Util.nextInt(1, 99)));
        a52.itemOptions.add(new ItemOption(77, Util.nextInt(1, 99)));
        a52.itemOptions.add(new ItemOption(103, Util.nextInt(1, 99)));
        a52.itemOptions.add(new ItemOption(94, Util.nextInt(1, 100)));
        a52.itemOptions.add(new ItemOption(246, Util.nextInt(1, 100)));
        a52.itemOptions.add(new ItemOption(31, Util.nextInt(1, 100)));
        a52.itemOptions.add(new ItemOption(5, Util.nextInt(1, 30)));
        if (Util.isTrue(95, 100)) {
            a52.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        a52.color = 4;
        listItem.add(a52);

       Item a53 = ItemService.gI().createNewItem((short) (1600));
        a53.itemOptions.add(new ItemOption(50, Util.nextInt(1, 99)));
        a53.itemOptions.add(new ItemOption(77, Util.nextInt(1, 99)));
        a53.itemOptions.add(new ItemOption(103, Util.nextInt(1, 99)));
        a53.itemOptions.add(new ItemOption(94, Util.nextInt(1, 100)));
        a53.itemOptions.add(new ItemOption(246, Util.nextInt(1, 100)));
        a53.itemOptions.add(new ItemOption(31, Util.nextInt(1, 100)));
        a53.itemOptions.add(new ItemOption(5, Util.nextInt(1, 30)));
        if (Util.isTrue(95, 100)) {
            a53.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        a53.color = 4;
        listItem.add(a53);

        Item a54 = ItemService.gI().createNewItem((short) (1601));
        a54.itemOptions.add(new ItemOption(50, Util.nextInt(1, 99)));
        a54.itemOptions.add(new ItemOption(77, Util.nextInt(1, 99)));
        a54.itemOptions.add(new ItemOption(103, Util.nextInt(1, 99)));
        a54.itemOptions.add(new ItemOption(94, Util.nextInt(1, 100)));
        a54.itemOptions.add(new ItemOption(246, Util.nextInt(1, 100)));
        a54.itemOptions.add(new ItemOption(31, Util.nextInt(1, 100)));
        a54.itemOptions.add(new ItemOption(5, Util.nextInt(1, 30)));
        if (Util.isTrue(95, 100)) {
            a54.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        a54.color = 4;
        listItem.add(a54);

         Item a55 = ItemService.gI().createNewItem((short) (1602));
        a55.itemOptions.add(new ItemOption(50, Util.nextInt(1, 99)));
        a55.itemOptions.add(new ItemOption(77, Util.nextInt(1, 99)));
        a55.itemOptions.add(new ItemOption(103, Util.nextInt(1, 99)));
        a55.itemOptions.add(new ItemOption(94, Util.nextInt(1, 100)));
        a55.itemOptions.add(new ItemOption(246, Util.nextInt(1, 100)));
        a55.itemOptions.add(new ItemOption(31, Util.nextInt(1, 100)));
        a55.itemOptions.add(new ItemOption(5, Util.nextInt(1, 30)));
        if (Util.isTrue(95, 100)) {
            a55.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        a55.color = 4;
        listItem.add(a55);

         Item a56 = ItemService.gI().createNewItem((short) (1603));
        a56.itemOptions.add(new ItemOption(50, Util.nextInt(1, 99)));
        a56.itemOptions.add(new ItemOption(77, Util.nextInt(1, 99)));
        a56.itemOptions.add(new ItemOption(103, Util.nextInt(1, 99)));
        a56.itemOptions.add(new ItemOption(94, Util.nextInt(1, 100)));
        a56.itemOptions.add(new ItemOption(246, Util.nextInt(1, 100)));
        a56.itemOptions.add(new ItemOption(31, Util.nextInt(1, 100)));
        a56.itemOptions.add(new ItemOption(5, Util.nextInt(1, 30)));
        if (Util.isTrue(95, 100)) {
            a56.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        a56.color = 4;
        listItem.add(a56);

           Item a57 = ItemService.gI().createNewItem((short) (1604));
        a57.itemOptions.add(new ItemOption(50, Util.nextInt(1, 99)));
        a57.itemOptions.add(new ItemOption(77, Util.nextInt(1, 99)));
        a57.itemOptions.add(new ItemOption(103, Util.nextInt(1, 99)));
        a57.itemOptions.add(new ItemOption(94, Util.nextInt(1, 100)));
        a57.itemOptions.add(new ItemOption(246, Util.nextInt(1, 100)));
        a57.itemOptions.add(new ItemOption(31, Util.nextInt(1, 100)));
        a57.itemOptions.add(new ItemOption(5, Util.nextInt(1, 30)));
        if (Util.isTrue(95, 100)) {
            a57.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        a57.color = 4;
        listItem.add(a57);

         Item a58 = ItemService.gI().createNewItem((short) (1605));
        a58.itemOptions.add(new ItemOption(50, Util.nextInt(1, 99)));
        a58.itemOptions.add(new ItemOption(77, Util.nextInt(1, 99)));
        a58.itemOptions.add(new ItemOption(103, Util.nextInt(1, 99)));
        a58.itemOptions.add(new ItemOption(94, Util.nextInt(1, 100)));
        a58.itemOptions.add(new ItemOption(246, Util.nextInt(1, 100)));
        a58.itemOptions.add(new ItemOption(31, Util.nextInt(1, 100)));
        a58.itemOptions.add(new ItemOption(5, Util.nextInt(1, 30)));
        if (Util.isTrue(95, 100)) {
            a58.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        a58.color = 4;
        listItem.add(a58);

        Item a59 = ItemService.gI().createNewItem((short) (1606));
        a59.itemOptions.add(new ItemOption(50, Util.nextInt(1, 99)));
        a59.itemOptions.add(new ItemOption(77, Util.nextInt(1, 99)));
        a59.itemOptions.add(new ItemOption(103, Util.nextInt(1, 99)));
        a59.itemOptions.add(new ItemOption(94, Util.nextInt(1, 100)));
        a59.itemOptions.add(new ItemOption(246, Util.nextInt(1, 100)));
        a59.itemOptions.add(new ItemOption(31, Util.nextInt(1, 100)));
        a59.itemOptions.add(new ItemOption(5, Util.nextInt(1, 30)));
        if (Util.isTrue(95, 100)) {
            a59.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        a59.color = 4;
        listItem.add(a59);

        Item a60 = ItemService.gI().createNewItem((short) (997),99);
        a60.itemOptions.add(new ItemOption(31, Util.nextInt(1, 100)));
        if (Util.isTrue(90, 100)) {
            a60.itemOptions.add(new ItemOption(93, Util.nextInt(1, 20)));
        }
        a60.color = 4;
        listItem.add(a60);

      

        //  *****************************************************************************************************************************************************************     
        //  *****************************************************************************************************************************************************************     
        Item b1 = ItemService.gI().createNewItem((short) (1868));
        if (Util.isTrue(1, 100)) {
            b1.itemOptions.add(new ItemOption(50, Util.nextInt(1, 99)));
            b1.itemOptions.add(new ItemOption(77, Util.nextInt(1, 99)));
            b1.itemOptions.add(new ItemOption(103, Util.nextInt(1, 99)));
            b1.itemOptions.add(new ItemOption(78, Util.nextInt(1, 99)));
            b1.itemOptions.add(new ItemOption(246, Util.nextInt(1, 5000)));
            b1.itemOptions.add(new ItemOption(5, Util.nextInt(1, 100)));
            if (Util.isTrue(99, 100)) {
                b1.itemOptions.add(new ItemOption(93, Util.nextInt(1, 20)));
            }
        } else if (Util.isTrue(20, 100)) {
           b1.itemOptions.add(new ItemOption(50, Util.nextInt(1, 99)));
            b1.itemOptions.add(new ItemOption(77, Util.nextInt(1, 99)));
            b1.itemOptions.add(new ItemOption(103, Util.nextInt(1, 99)));
            b1.itemOptions.add(new ItemOption(78, Util.nextInt(1, 99)));
            b1.itemOptions.add(new ItemOption(246, Util.nextInt(1, 99)));
            b1.itemOptions.add(new ItemOption(5, Util.nextInt(1, 100)));
            if (Util.isTrue(95, 100)) {
                b1.itemOptions.add(new ItemOption(93, Util.nextInt(1, 20)));
            }
        } else {
          b1.itemOptions.add(new ItemOption(50, Util.nextInt(1, 99)));
            b1.itemOptions.add(new ItemOption(77, Util.nextInt(1, 99)));
            b1.itemOptions.add(new ItemOption(103, Util.nextInt(1, 99)));
            b1.itemOptions.add(new ItemOption(78, Util.nextInt(1, 100)));
            b1.itemOptions.add(new ItemOption(246, Util.nextInt(1, 99)));
            b1.itemOptions.add(new ItemOption(5, Util.nextInt(1, 100)));
            if (Util.isTrue(90, 100)) {
                b1.itemOptions.add(new ItemOption(93, Util.nextInt(1, 20)));
            }
        }

        b1.color = 3;
        listItemVip.add(b1);
        

        Item b2 = ItemService.gI().createNewItem((short) (1869));
        if (Util.isTrue(1, 100)) {
            b2.itemOptions.add(new ItemOption(50, Util.nextInt(1, 99)));
            b2.itemOptions.add(new ItemOption(77, Util.nextInt(1, 99)));
            b2.itemOptions.add(new ItemOption(103, Util.nextInt(1, 99)));
            b2.itemOptions.add(new ItemOption(78, Util.nextInt(1, 99)));
            b2.itemOptions.add(new ItemOption(246, Util.nextInt(1, 99)));
            b2.itemOptions.add(new ItemOption(5, Util.nextInt(1, 100)));
            if (Util.isTrue(99, 100)) {
                b2.itemOptions.add(new ItemOption(93, Util.nextInt(1, 20)));
            }
        } else if (Util.isTrue(20, 100)) {
           b2.itemOptions.add(new ItemOption(50, Util.nextInt(1, 99)));
            b2.itemOptions.add(new ItemOption(77, Util.nextInt(1, 99)));
            b2.itemOptions.add(new ItemOption(103, Util.nextInt(1, 99)));
            b2.itemOptions.add(new ItemOption(78, Util.nextInt(1, 99)));
            b2.itemOptions.add(new ItemOption(246, Util.nextInt(1, 99)));
            b2.itemOptions.add(new ItemOption(5, Util.nextInt(1, 100)));
            if (Util.isTrue(95, 100)) {
                b2.itemOptions.add(new ItemOption(93, Util.nextInt(1, 20)));
            }
        } else {
          b2.itemOptions.add(new ItemOption(50, Util.nextInt(1, 99)));
            b2.itemOptions.add(new ItemOption(77, Util.nextInt(1, 99)));
            b2.itemOptions.add(new ItemOption(103, Util.nextInt(1, 99)));
            b2.itemOptions.add(new ItemOption(78, Util.nextInt(1, 100)));
            b2.itemOptions.add(new ItemOption(246, Util.nextInt(1, 99)));
            b2.itemOptions.add(new ItemOption(5, Util.nextInt(1, 100)));
            if (Util.isTrue(90, 100)) {
                b2.itemOptions.add(new ItemOption(93, Util.nextInt(1, 20)));
            }
        }

        b2.color = 3;
        listItemVip.add(b2);

        Item b3 = ItemService.gI().createNewItem((short) (1870));
        b3.itemOptions.add(new ItemOption(50, Util.nextInt(1, 99)));
        b3.itemOptions.add(new ItemOption(77, Util.nextInt(1, 99)));
        b3.itemOptions.add(new ItemOption(103, Util.nextInt(1, 99)));
        b3.itemOptions.add(new ItemOption(5, Util.nextInt(1, 100)));
        b3.itemOptions.add(new ItemOption(246, 1));
        b3.itemOptions.add(new ItemOption(31, 1));
         if (Util.isTrue(99, 100)) {
            b3.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        b3.color = 3;
        listItemVip.add(b3);
        
        
        
        

        Item b4 = ItemService.gI().createNewItem((short) (1871));
        b4.itemOptions.add(new ItemOption(50, Util.nextInt(1, 99)));
        b4.itemOptions.add(new ItemOption(77, Util.nextInt(1, 99)));
        b4.itemOptions.add(new ItemOption(103, Util.nextInt(1, 99)));
        b4.itemOptions.add(new ItemOption(5, Util.nextInt(1, 100)));
        b4.itemOptions.add(new ItemOption(246, 1));
        b4.itemOptions.add(new ItemOption(31, 1));
         if (Util.isTrue(99, 100)) {
            b4.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        b4.color = 3;
        listItemVip.add(b4);

        
       Item b5 = ItemService.gI().createNewItem((short) (1872));
        b5.itemOptions.add(new ItemOption(50, Util.nextInt(1, 99)));
        b5.itemOptions.add(new ItemOption(77, Util.nextInt(1, 99)));
        b5.itemOptions.add(new ItemOption(103, Util.nextInt(1, 99)));
        b5.itemOptions.add(new ItemOption(5, Util.nextInt(1, 100)));
        b5.itemOptions.add(new ItemOption(246, 1));
        b5.itemOptions.add(new ItemOption(31, 1));
         if (Util.isTrue(99, 100)) {
            b5.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        b5.color = 3;
        listItemVip.add(b5);

       

        Item b6 = ItemService.gI().createNewItem((short) (1873));
        b6.itemOptions.add(new ItemOption(50, Util.nextInt(1, 99)));
        b6.itemOptions.add(new ItemOption(77, Util.nextInt(1, 99)));
        b6.itemOptions.add(new ItemOption(103, Util.nextInt(1, 99)));
        b6.itemOptions.add(new ItemOption(5, Util.nextInt(1, 100)));
        b6.itemOptions.add(new ItemOption(246, 1));
        b6.itemOptions.add(new ItemOption(31, 1));
         if (Util.isTrue(99, 100)) {
            b6.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        b6.color = 3;
        listItemVip.add(b6);

         Item b7 = ItemService.gI().createNewItem((short) (1874));
        b7.itemOptions.add(new ItemOption(50, Util.nextInt(1, 99)));
        b7.itemOptions.add(new ItemOption(77, Util.nextInt(1, 99)));
        b7.itemOptions.add(new ItemOption(103, Util.nextInt(1, 99)));
        b7.itemOptions.add(new ItemOption(5, Util.nextInt(1, 100)));
        b7.itemOptions.add(new ItemOption(246, 1));
        b7.itemOptions.add(new ItemOption(31, 1));
         if (Util.isTrue(99, 100)) {
            b7.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        b7.color = 3;
        listItemVip.add(b7);

          Item b8 = ItemService.gI().createNewItem((short) (1875));
        b8.itemOptions.add(new ItemOption(50, Util.nextInt(1, 99)));
        b8.itemOptions.add(new ItemOption(77, Util.nextInt(1, 99)));
        b8.itemOptions.add(new ItemOption(103, Util.nextInt(1, 99)));
        b8.itemOptions.add(new ItemOption(5, Util.nextInt(1, 100)));
        b8.itemOptions.add(new ItemOption(246, 1));
        b8.itemOptions.add(new ItemOption(31, 1));
         if (Util.isTrue(99, 100)) {
            b8.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        b8.color = 3;
        listItemVip.add(b8);

         Item b9 = ItemService.gI().createNewItem((short) (1876));
        b9.itemOptions.add(new ItemOption(50, Util.nextInt(1, 99)));
        b9.itemOptions.add(new ItemOption(77, Util.nextInt(1, 99)));
        b9.itemOptions.add(new ItemOption(103, Util.nextInt(1, 99)));
        b9.itemOptions.add(new ItemOption(5, Util.nextInt(1, 100)));
        b9.itemOptions.add(new ItemOption(246, 1));
        b9.itemOptions.add(new ItemOption(31, 1));
         if (Util.isTrue(99, 100)) {
            b9.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        b9.color = 3;
        listItemVip.add(b9);

         Item b10 = ItemService.gI().createNewItem((short) (1877));
        b10.itemOptions.add(new ItemOption(50, Util.nextInt(1, 99)));
        b10.itemOptions.add(new ItemOption(77, Util.nextInt(1, 99)));
        b10.itemOptions.add(new ItemOption(103, Util.nextInt(1, 99)));
        b10.itemOptions.add(new ItemOption(5, Util.nextInt(1, 100)));
        b10.itemOptions.add(new ItemOption(246, 1));
        b10.itemOptions.add(new ItemOption(31, 1));
         if (Util.isTrue(99, 100)) {
            b10.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        b10.color = 3;
        listItemVip.add(b10);

        //*****10 ô đầu
        Item b11 = ItemService.gI().createNewItem((short) (1539));
        b11.itemOptions.add(new ItemOption(47, Util.nextInt(1, 5000)));
        b11.itemOptions.add(new ItemOption( Util.nextInt(218, 226),1));
        b11.itemOptions.add(new ItemOption(35, Util.nextInt(1, 100)));
        b11.itemOptions.add(new ItemOption(72, Util.nextInt(1, 7)));
         b11.itemOptions.add(new ItemOption(246, 1));
        b11.itemOptions.add(new ItemOption(30, 1));
         if (Util.isTrue(99, 100)) {
            b11.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        b11.color = 3;
        listItemVip.add(b11);

        
        
        Item b12 = ItemService.gI().createNewItem((short) (2038));
        b11.itemOptions.add(new ItemOption(72, Util.nextInt(0, 16)));
        b12.color = 1;
        listItemVip.add(b12);

        Item b13 = ItemService.gI().createNewItem((short) (2038));
        b13.itemOptions.add(new ItemOption(72, Util.nextInt(0, 16)));
        b13.color = 1;
        listItemVip.add(b13);

        Item b14 = ItemService.gI().createNewItem((short) (2038));
        b14.itemOptions.add(new ItemOption(72, Util.nextInt(0, 16)));
        b14.color = 1;
        listItemVip.add(b14);

        Item b15 = ItemService.gI().createNewItem((short) (2038));
        b15.itemOptions.add(new ItemOption(72, Util.nextInt(0, 16)));
        b15.color = 1;
        listItemVip.add(b15);

        Item b16 = ItemService.gI().createNewItem((short) (2038));
        b16.itemOptions.add(new ItemOption(72, Util.nextInt(0, 16)));
        b16.color = 1;
        listItemVip.add(b16);

        Item b17 = ItemService.gI().createNewItem((short) (2038));
        b17.itemOptions.add(new ItemOption(72, Util.nextInt(0, 16)));
        b17.color = 1;
        listItemVip.add(b17);

        Item b18 = ItemService.gI().createNewItem((short) (2038));
        b18.itemOptions.add(new ItemOption(72, Util.nextInt(0, 16)));
        b18.color = 1;
        listItemVip.add(b18);

        Item b19 = ItemService.gI().createNewItem((short) (2038));
        b19.itemOptions.add(new ItemOption(72, Util.nextInt(0, 16)));
        b19.color = 1;
        listItemVip.add(b19);

        Item b20 = ItemService.gI().createNewItem((short) (1544));
        b20.itemOptions.add(new ItemOption(47, Util.nextInt(1, 5000)));
        b20.itemOptions.add(new ItemOption( Util.nextInt(218, 226),1));
        b20.itemOptions.add(new ItemOption(35, Util.nextInt(1, 100)));
        b20.itemOptions.add(new ItemOption(72, Util.nextInt(1, 7)));
         b20.itemOptions.add(new ItemOption(246, 1));
        b20.itemOptions.add(new ItemOption(30, 1));
         if (Util.isTrue(99, 100)) {
            b20.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        b20.color = 3;
        listItemVip.add(b20);

        Item b21 = ItemService.gI().createNewItem((short) (1540));
        b21.itemOptions.add(new ItemOption(6, Util.nextInt(1, 500000)));
        b21.itemOptions.add(new ItemOption( Util.nextInt(218, 226),1));
        b21.itemOptions.add(new ItemOption(35, Util.nextInt(1, 100)));
        b21.itemOptions.add(new ItemOption(72, Util.nextInt(1, 7)));
         b21.itemOptions.add(new ItemOption(246, 1));
        b21.itemOptions.add(new ItemOption(30, 1));
         if (Util.isTrue(99, 100)) {
            b21.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        b21.color = 3;
        listItemVip.add(b21);

        Item b22 = ItemService.gI().createNewItem((short) (2038));
        b22.itemOptions.add(new ItemOption(72, Util.nextInt(0, 16)));
        b22.color = 1;
        listItemVip.add(b22);

        Item b23 = ItemService.gI().createNewItem((short) (2038));
        b23.itemOptions.add(new ItemOption(72, Util.nextInt(0, 16)));
        b23.color = 1;
        listItemVip.add(b23);

        Item b24 = ItemService.gI().createNewItem((short) (2038));
        b24.itemOptions.add(new ItemOption(72, Util.nextInt(0, 16)));
        b24.color = 1;
        listItemVip.add(b24);

        Item b25 = ItemService.gI().createNewItem((short) (2038));
        b25.itemOptions.add(new ItemOption(72, Util.nextInt(0, 16)));
        b25.color = 1;
        listItemVip.add(b25);

        Item b26 = ItemService.gI().createNewItem((short) (2038));
        b26.itemOptions.add(new ItemOption(72, Util.nextInt(0, 16)));
        b26.color = 1;
        listItemVip.add(b26);

        Item b27 = ItemService.gI().createNewItem((short) (2038));
        b27.itemOptions.add(new ItemOption(72, Util.nextInt(0, 16)));
        b27.color = 1;
        listItemVip.add(b27);

        Item b28 = ItemService.gI().createNewItem((short) (2038));
        b28.itemOptions.add(new ItemOption(72, Util.nextInt(0, 16)));
        b28.color = 1;
        listItemVip.add(b28);

        Item b29 = ItemService.gI().createNewItem((short) (2038));
        b29.itemOptions.add(new ItemOption(72, Util.nextInt(0, 16)));
        b29.color = 1;
        listItemVip.add(b29);

         Item b30 = ItemService.gI().createNewItem((short) (1545));
        b30.itemOptions.add(new ItemOption(6, Util.nextInt(1, 500000)));
        b30.itemOptions.add(new ItemOption( Util.nextInt(218, 226),1));
        b30.itemOptions.add(new ItemOption(35, Util.nextInt(1, 100)));
        b30.itemOptions.add(new ItemOption(72, Util.nextInt(1, 7)));
         b30.itemOptions.add(new ItemOption(246, 1));
        b30.itemOptions.add(new ItemOption(30, 1));
         if (Util.isTrue(99, 100)) {
            b30.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        b30.color = 3;
        listItemVip.add(b30);

        Item b31 = ItemService.gI().createNewItem((short) (1541));
        b31.itemOptions.add(new ItemOption(0, Util.nextInt(1, 50000)));
        b31.itemOptions.add(new ItemOption( Util.nextInt(218, 226),1));
        b31.itemOptions.add(new ItemOption(35, Util.nextInt(1, 100)));
        b31.itemOptions.add(new ItemOption(72, Util.nextInt(1, 7)));
         b31.itemOptions.add(new ItemOption(246, 1));
        b31.itemOptions.add(new ItemOption(30, 1));
         if (Util.isTrue(99, 100)) {
            b31.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        b31.color = 3;
        listItemVip.add(b31);

        Item b32 = ItemService.gI().createNewItem((short) (2038));
        b32.itemOptions.add(new ItemOption(72, Util.nextInt(0, 16)));
        b29.color = 1;
        listItemVip.add(b29);

        Item b33 = ItemService.gI().createNewItem((short) (2038));
        b33.itemOptions.add(new ItemOption(72, Util.nextInt(0, 16)));
        b33.color = 1;
        listItemVip.add(b33);

        Item b34 = ItemService.gI().createNewItem((short) (2038));
        b34.itemOptions.add(new ItemOption(72, Util.nextInt(0, 16)));
        b34.color = 1;
        listItemVip.add(b34);

        Item b35 = ItemService.gI().createNewItem((short) (2038));
        b35.itemOptions.add(new ItemOption(72, Util.nextInt(0, 16)));
        b35.color = 1;
        listItemVip.add(b35);

        Item b36 = ItemService.gI().createNewItem((short) (2038));
        b36.itemOptions.add(new ItemOption(72, Util.nextInt(0, 16)));
        b36.color = 1;
        listItemVip.add(b36);

        Item b37 = ItemService.gI().createNewItem((short) (2038));
        b37.itemOptions.add(new ItemOption(72, Util.nextInt(0, 16)));
        b37.color = 1;
        listItemVip.add(b37);

        Item b38 = ItemService.gI().createNewItem((short) (2038));
        b38.itemOptions.add(new ItemOption(72, Util.nextInt(0, 16)));
        b38.color = 1;
        listItemVip.add(b38);

        Item b39 = ItemService.gI().createNewItem((short) (2038));
        b39.itemOptions.add(new ItemOption(72, Util.nextInt(0, 16)));
        b39.color = 1;
        listItemVip.add(b39);

         Item b40 = ItemService.gI().createNewItem((short) (1546));
        b40.itemOptions.add(new ItemOption(0, Util.nextInt(1, 50000)));
        b40.itemOptions.add(new ItemOption( Util.nextInt(218, 226),1));
        b40.itemOptions.add(new ItemOption(35, Util.nextInt(1, 100)));
        b40.itemOptions.add(new ItemOption(72, Util.nextInt(1, 7)));
         b40.itemOptions.add(new ItemOption(246, 1));
        b40.itemOptions.add(new ItemOption(30, 1));
         if (Util.isTrue(99, 100)) {
            b40.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        b40.color = 3;
        listItemVip.add(b40);

       Item b41 = ItemService.gI().createNewItem((short) (1542));
        b41.itemOptions.add(new ItemOption(7, Util.nextInt(1, 500000)));
        b41.itemOptions.add(new ItemOption( Util.nextInt(218, 226),1));
        b41.itemOptions.add(new ItemOption(35, Util.nextInt(1, 100)));
        b41.itemOptions.add(new ItemOption(72, Util.nextInt(1, 7)));
         b41.itemOptions.add(new ItemOption(246, 1));
        b41.itemOptions.add(new ItemOption(30, 1));
         if (Util.isTrue(99, 100)) {
            b41.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        b41.color = 3;
        listItemVip.add(b41);

       Item b42 = ItemService.gI().createNewItem((short) (2038));
        b42.itemOptions.add(new ItemOption(72, Util.nextInt(0, 16)));
        b42.color = 1;
        listItemVip.add(b42);

        Item b43 = ItemService.gI().createNewItem((short) (2038));
        b43.itemOptions.add(new ItemOption(72, Util.nextInt(0, 16)));
        b43.color = 1;
        listItemVip.add(b43);

        Item b44 = ItemService.gI().createNewItem((short) (2038));
        b44.itemOptions.add(new ItemOption(72, Util.nextInt(0, 16)));
        b44.color = 1;
        listItemVip.add(b44);

        Item b45 = ItemService.gI().createNewItem((short) (2038));
        b45.itemOptions.add(new ItemOption(72, Util.nextInt(0, 16)));
        b45.color = 1;
        listItemVip.add(b45);

        Item b46 = ItemService.gI().createNewItem((short) (2038));
        b46.itemOptions.add(new ItemOption(72, Util.nextInt(0, 16)));
        b46.color = 1;
        listItemVip.add(b46);

        Item b47 = ItemService.gI().createNewItem((short) (2038));
        b47.itemOptions.add(new ItemOption(72, Util.nextInt(0, 16)));
        b47.color = 1;
        listItemVip.add(b47);

        Item b48 = ItemService.gI().createNewItem((short) (2038));
        b48.itemOptions.add(new ItemOption(72, Util.nextInt(0, 16)));
        b48.color = 1;
        listItemVip.add(b48);

        Item b49 = ItemService.gI().createNewItem((short) (2038));
        b49.itemOptions.add(new ItemOption(72, Util.nextInt(0, 16)));
        b49.color = 1;
        listItemVip.add(b49);

        Item b50 = ItemService.gI().createNewItem((short) (1547));
        b50.itemOptions.add(new ItemOption(7, Util.nextInt(1, 500000)));
        b50.itemOptions.add(new ItemOption( Util.nextInt(218, 226),1));
        b50.itemOptions.add(new ItemOption(35, Util.nextInt(1, 100)));
        b50.itemOptions.add(new ItemOption(72, Util.nextInt(1, 7)));
         b50.itemOptions.add(new ItemOption(246, 1));
        b50.itemOptions.add(new ItemOption(30, 1));
         if (Util.isTrue(99, 100)) {
            b50.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        b50.color = 3;
        listItemVip.add(b50);

        Item b51 = ItemService.gI().createNewItem((short) (1543));
        b51.itemOptions.add(new ItemOption(14, Util.nextInt(1, 20)));
        b51.itemOptions.add(new ItemOption( Util.nextInt(218, 226),1));
        b51.itemOptions.add(new ItemOption(35, Util.nextInt(1, 100)));
        b51.itemOptions.add(new ItemOption(72, Util.nextInt(1, 7)));
         b51.itemOptions.add(new ItemOption(246, 1));
        b51.itemOptions.add(new ItemOption(30, 1));
         if (Util.isTrue(99, 100)) {
            b51.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        b51.color = 3;
        listItemVip.add(b51);

      Item b52 = ItemService.gI().createNewItem((short) (1404),Util.nextInt(1, 5000));
       b52.itemOptions.add(new ItemOption(246, 1));
        b52.itemOptions.add(new ItemOption(30, 1));
         if (Util.isTrue(99, 100)) {
            b52.itemOptions.add(new ItemOption(93, Util.nextInt(1, 70)));
        }
        b52.color = 3;
        listItemVip.add(b52);

         Item b53 = ItemService.gI().createNewItem((short) (457),Util.nextInt(1, 5000));
          b53.itemOptions.add(new ItemOption(246, 1));
        b53.itemOptions.add(new ItemOption(30, 1));
         if (Util.isTrue(99, 100)) {
            b53.itemOptions.add(new ItemOption(93, Util.nextInt(1, 70)));
        }
        b53.color = 3;
        listItemVip.add(b53);

        Item b54 = ItemService.gI().createNewItem((short) (1111),Util.nextInt(1, 5000));
         b54.itemOptions.add(new ItemOption(246, 1));
        b53.itemOptions.add(new ItemOption(30, 1));
         if (Util.isTrue(99, 100)) {
            b54.itemOptions.add(new ItemOption(93, Util.nextInt(1, 70)));
        }
        b54.color = 3;
        listItemVip.add(b54);
        

        Item b55 = ItemService.gI().createNewItem((short) (861),Util.nextInt(1, 500000));
         b55.itemOptions.add(new ItemOption(246, 1));
        b55.itemOptions.add(new ItemOption(30, 1));
         if (Util.isTrue(99, 100)) {
            b55.itemOptions.add(new ItemOption(93, Util.nextInt(1, 70)));
        }
        b55.color = 3;
        listItemVip.add(b55);

         Item b56 = ItemService.gI().createNewItem((short) (77),Util.nextInt(1, 500000));
          b56.itemOptions.add(new ItemOption(246, 1));
        b56.itemOptions.add(new ItemOption(30, 1));
         if (Util.isTrue(99, 100)) {
            b56.itemOptions.add(new ItemOption(93, Util.nextInt(1, 70)));
        }
        b56.color = 3;
        listItemVip.add(b56);

         Item b57 = ItemService.gI().createNewItem((short) (987),Util.nextInt(1, 10));
          b57.itemOptions.add(new ItemOption(246, 1));
        b57.itemOptions.add(new ItemOption(30, 1));
         if (Util.isTrue(99, 100)) {
            b57.itemOptions.add(new ItemOption(93, Util.nextInt(1, 70)));
        }
        b57.color = 3;
        listItemVip.add(b57);

       Item b58 = ItemService.gI().createNewItem((short) (1502),Util.nextInt(1, 1000));
        b58.itemOptions.add(new ItemOption(246, 1));
        b58.itemOptions.add(new ItemOption(30, 1));
         if (Util.isTrue(99, 100)) {
            b58.itemOptions.add(new ItemOption(93, Util.nextInt(1, 70)));
        }
        b58.color = 3;
        listItemVip.add(b58);

         Item b59 = ItemService.gI().createNewItem((short) (1983),Util.nextInt(1, 10000));
          b59.itemOptions.add(new ItemOption(246, 1));
        b59.itemOptions.add(new ItemOption(30, 1));
         if (Util.isTrue(99, 100)) {
            b59.itemOptions.add(new ItemOption(93, Util.nextInt(1, 70)));
        }
        b59.color = 3;
        listItemVip.add(b59);

        Item b60 = ItemService.gI().createNewItem((short) (1548));
        b60.itemOptions.add(new ItemOption(14, Util.nextInt(1, 20)));
        b60.itemOptions.add(new ItemOption( Util.nextInt(218, 226),1));
        b60.itemOptions.add(new ItemOption(35, Util.nextInt(1, 100)));
        b60.itemOptions.add(new ItemOption(72, Util.nextInt(1, 7)));
         b60.itemOptions.add(new ItemOption(246, 1));
        b60.itemOptions.add(new ItemOption(30, 1));
         if (Util.isTrue(99, 100)) {
            b60.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
        }
        b60.color = 3;
        listItemVip.add(b60);

   

        for (int i = 457; i < 457; i++) {
            Item item = ItemService.gI().createNewItem((short) i);
            listItemVip.add(item);

            Item itemVip = ItemService.gI().createNewItem((short) (i));

            listItemVip.add(itemVip);

        }

    }

    public static void sendDataQuay(Player player, byte type) {
        Message msg = null;
        try {
            msg = new Message(69);
            msg.writer().writeByte(type);
            int size = type == 0 ? listItem.size() : listItemVip.size();
            msg.writer().writeInt(size);
            for (int i = 0; i < size; i++) {
                msg.writer().writeInt(type == 0 ? listItem.get(i).template.id : listItemVip.get(i).template.id);
                // Màu sắc, để từ 0->7
                msg.writer().writeInt(type == 0 ? listItem.get(i).color : listItemVip.get(i).color);
            }
            //Van may
            msg.writer().writeInt(Util.nextInt(1, 15));
            //icon chia khoa
            msg.writer().writeInt(30207);
            msg.writer().writeInt(30208);
            player.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendListReceive(List<Item> list, Player player) {
        try {
            Message msg = new Message(69);
            msg.writer().writeByte(1);
            int size = list.size();
            msg.writer().writeInt(size);
            for (int i = 0; i < size; i++) {
                msg.writer().writeInt(list.get(i).template.id);
                // Màu sắc, để từ 0->7
                msg.writer().writeInt(list.get(i).color);
            }
            player.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void readData(Message msg, Player player) {
        try {
            int type = msg.reader().readByte();
            if (type == 1 || type == 4) {
                int luotQuay = msg.reader().readInt();
                List<Item> receive = new ArrayList<>();
                for (int i = 0; i < luotQuay; i++) {
                    receive.add(type == 1 ? listItem.get(Util.nextInt(listItem.size() - 1)) : listItemVip.get(Util.nextInt(listItemVip.size() - 1)));
                }
                int dem = 0;
                int slKey = 0;
                for (Item it : player.inventory.itemsBag) {
                    if (it == null || it.template == null) {
                        dem++;
                        continue;
                    }
                    if ((type == 1 && it.template.id == 2062) || (type == 4 && it.template.id == 2063)) {
                        slKey += it.quantity;
                    }

                }
                if (slKey < luotQuay) {
                    Service.getInstance().sendThongBao(player, "Bạn không đủ key để quay");
                    return;
                }
                if (dem < luotQuay) {
                    Service.getInstance().sendThongBao(player, "Hành trang không đủ chỗ trống");
                    return;
                } else {
                    slKey = luotQuay;
                    for (Item i : receive) {
                        InventoryService.gI().addItemBag(player, i, 3);
                    }
                    for (Item it : player.inventory.itemsBag) {
                        if (slKey <= 0) {
                            break;
                        }
                        if (it == null || it.template == null) {
                            continue;
                        }
                        if ((type == 1 && it.template.id == 2062) || (type == 4 && it.template.id == 2063)) {
                            int min = slKey < it.quantity ? slKey : it.quantity;
                            slKey -= min;
                            InventoryService.gI().subQuantityItemsBag(player, it, min);
                            listItem.clear();
                            listItemVip.clear();
                            loadItem();
                        }
                    }
                    InventoryService.gI().sendItemBags(player);
                    sendListReceive(receive, player);
                }
            } else if (type == 0 || type == 3) {
                sendDataQuay(player, (byte) type);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
