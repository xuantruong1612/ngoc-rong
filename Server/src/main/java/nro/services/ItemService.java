package nro.services;

import nro.models.item.ItemOptionTemplate;
import nro.models.item.ItemTemplate;
import nro.models.consignment.ConsignmentItem;
import nro.models.item.Item;
import nro.models.item.ItemOption;
import nro.models.map.ItemMap;
import nro.models.shop.ItemShop;
import nro.server.Manager;
import nro.utils.TimeUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import nro.models.player.Player;
import nro.utils.Util;

/**
 * @Build by Arriety
 */
public class ItemService {

    private static ItemService i;

    public static ItemService gI() {
        if (i == null) {
            i = new ItemService();
        }
        return i;
    }

    public Item createItemNull() {
        Item item = new Item();
        return item;
    }

    public Item createItemFromItemShop(ItemShop itemShop) {
        Item item = new Item();
        item.template = itemShop.temp;
        item.quantity = 1;
        item.content = item.getContent();
        item.info = item.getInfo();
        for (ItemOption io : itemShop.options) {
            item.itemOptions.add(new ItemOption(io));
        }
        return item;
    }

    public Item copyItem(Item item) {
        Item it = new Item();
        it.itemOptions = new ArrayList<>();
        it.template = item.template;
        it.info = item.info;
        it.content = item.content;
        it.quantity = item.quantity;
        it.createTime = item.createTime;
        for (ItemOption io : item.itemOptions) {
            it.itemOptions.add(new ItemOption(io));
        }
        return it;
    }

    public Item otpKH(short tempId) {
        return otpKH(tempId, 1);
    }

    public Item otpKH(short tempId, int quantity) {
        Item item = new Item();
        item.template = getTemplate(tempId);
        item.quantity = quantity;
        item.createTime = System.currentTimeMillis();
        switch (item.template.type) {
            case 0:
                item.itemOptions.add(new ItemOption(47, Util.nextInt(1, 4000)));
                break;
            case 1:
                item.itemOptions.add(new ItemOption(6, Util.nextInt(1, 2000000)));
                break;
            case 2:
                item.itemOptions.add(new ItemOption(0, Util.nextInt(7, 30000)));
                break;
            case 3:
                item.itemOptions.add(new ItemOption(7, Util.nextInt(2, 2000000)));
                break;
            case 4:
                item.itemOptions.add(new ItemOption(14, 25));
                break;
            default:
                break;
        }
        item.content = item.getContent();
        item.info = item.getInfo();
        return item;
    }
    
    
       public Item randomChiSoAn(Item item){
        List<Integer> list = new ArrayList<>();
        for(int i = 0;i < item.itemOptions.size();i++){
            ItemOption io = item.itemOptions.get(i);
            list.add(io.optionTemplate.id);
        }
        int count = 0;
        for(int i = 0;i < item.itemOptions.size();i++){
            ItemOption io = item.itemOptions.get(i);
            if(io.optionTemplate.id == 208){
                item.itemOptions.remove(i);
                long param = io.param;
                int size = ChisoAn.OPTION_CHI_SO_AN.length;
                while(param > 0 && count < 1000){
                    count++;
                    int id = Util.nextInt(size);
                    if(!list.contains(ChisoAn.OPTION_CHI_SO_AN[id][0])){
                        int[] tmp = ChisoAn.OPTION_CHI_SO_AN[id];
                        list.add(ChisoAn.OPTION_CHI_SO_AN[id][0]);
                        item.addOptionParam(tmp[0], Util.nextInt(tmp[1],tmp[2]),i);
                        param--;
                    }
                }
                break;
            }
        }
        return item;
    }
    
    
    
    
    
    
    
    
    

    public void setSongoku(Player player) throws Exception {
        Item hq = InventoryService.gI().findItem(player.inventory.itemsBag, 1228);
        Item ao = ItemService.gI().otpKH((short) 0);
        Item quan = ItemService.gI().otpKH((short) 6);
        Item gang = ItemService.gI().otpKH((short) 21);
        Item giay = ItemService.gI().otpKH((short) 27);
        Item rd = ItemService.gI().otpKH((short) 12);
        ao.itemOptions.add(new ItemOption(219, 1));
        quan.itemOptions.add(new ItemOption(219, 1));
        gang.itemOptions.add(new ItemOption(219, 1));
        giay.itemOptions.add(new ItemOption(219, 1));
        rd.itemOptions.add(new ItemOption(219, 1));
        ao.itemOptions.add(new ItemOption(231, 50));
        quan.itemOptions.add(new ItemOption(231, 50));
        gang.itemOptions.add(new ItemOption(231, 50));
        giay.itemOptions.add(new ItemOption(231, 50));
        rd.itemOptions.add(new ItemOption(231, 1));
        ao.itemOptions.add(new ItemOption(30, 1));
        quan.itemOptions.add(new ItemOption(30, 1));
        gang.itemOptions.add(new ItemOption(30, 1));
        giay.itemOptions.add(new ItemOption(30, 1));
        rd.itemOptions.add(new ItemOption(30, 1));
        if (InventoryService.gI().getCountEmptyBag(player) > 4) {
            InventoryService.gI().addItemBag(player, ao, 0);
            InventoryService.gI().addItemBag(player, quan, 0);
            InventoryService.gI().addItemBag(player, gang, 0);
            InventoryService.gI().addItemBag(player, giay, 0);
            InventoryService.gI().addItemBag(player, rd, 0);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player, "Bạn đã nhận được set Kích hoạt Kame");
            InventoryService.gI().subQuantityItemsBag(player, hq, 1);
            InventoryService.gI().sendItemBags(player);
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
        }
    }

    public void setThenXinHang(Player player) throws Exception {
        Item hq = InventoryService.gI().findItem(player.inventory.itemsBag, 1228);
        Item ao = ItemService.gI().otpKH((short) 0);
        Item quan = ItemService.gI().otpKH((short) 6);
        Item gang = ItemService.gI().otpKH((short) 21);
        Item giay = ItemService.gI().otpKH((short) 27);
        Item rd = ItemService.gI().otpKH((short) 12);
        ao.itemOptions.add(new ItemOption(218, 1));
        quan.itemOptions.add(new ItemOption(218, 1));
        gang.itemOptions.add(new ItemOption(218, 1));
        giay.itemOptions.add(new ItemOption(218, 1));
        rd.itemOptions.add(new ItemOption(218, 1));
        ao.itemOptions.add(new ItemOption(230, 1));
        quan.itemOptions.add(new ItemOption(230, 50));
        gang.itemOptions.add(new ItemOption(230, 50));
        giay.itemOptions.add(new ItemOption(230, 50));
        rd.itemOptions.add(new ItemOption(230, 50));
        ao.itemOptions.add(new ItemOption(30, 0));
        quan.itemOptions.add(new ItemOption(30, 0));
        gang.itemOptions.add(new ItemOption(30, 0));
        giay.itemOptions.add(new ItemOption(30, 0));
        rd.itemOptions.add(new ItemOption(30, 0));
        if (InventoryService.gI().getCountEmptyBag(player) > 4) {
            InventoryService.gI().addItemBag(player, ao, 0);
            InventoryService.gI().addItemBag(player, quan, 0);
            InventoryService.gI().addItemBag(player, gang, 0);
            InventoryService.gI().addItemBag(player, giay, 0);
            InventoryService.gI().addItemBag(player, rd, 0);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player, "Bạn đã nhận được set Kích hoạt Thên Xin Hăng");
            InventoryService.gI().subQuantityItemsBag(player, hq, 1);
            InventoryService.gI().sendItemBags(player);
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
        }
    }

    public void setKaioKen(Player player) throws Exception {
        Item hq = InventoryService.gI().findItem(player.inventory.itemsBag, 1228);
        Item ao = ItemService.gI().otpKH((short) 0);
        Item quan = ItemService.gI().otpKH((short) 6);
        Item gang = ItemService.gI().otpKH((short) 21);
        Item giay = ItemService.gI().otpKH((short) 27);
        Item rd = ItemService.gI().otpKH((short) 12);
        ao.itemOptions.add(new ItemOption(217, 1));
        quan.itemOptions.add(new ItemOption(217, 1));
        gang.itemOptions.add(new ItemOption(217, 1));
        giay.itemOptions.add(new ItemOption(217, 1));
        rd.itemOptions.add(new ItemOption(217, 1));
        ao.itemOptions.add(new ItemOption(229, 50));
        quan.itemOptions.add(new ItemOption(229, 50));
        gang.itemOptions.add(new ItemOption(229, 50));
        giay.itemOptions.add(new ItemOption(229, 50));
        rd.itemOptions.add(new ItemOption(229, 50));
        ao.itemOptions.add(new ItemOption(30, 0));
        quan.itemOptions.add(new ItemOption(30, 0));
        gang.itemOptions.add(new ItemOption(30, 0));
        giay.itemOptions.add(new ItemOption(30, 0));
        rd.itemOptions.add(new ItemOption(30, 0));
        if (InventoryService.gI().getCountEmptyBag(player) > 4) {
            InventoryService.gI().addItemBag(player, ao, 0);
            InventoryService.gI().addItemBag(player, quan, 0);
            InventoryService.gI().addItemBag(player, gang, 0);
            InventoryService.gI().addItemBag(player, giay, 0);
            InventoryService.gI().addItemBag(player, rd, 0);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player, "Bạn đã nhận được set Kích hoạt Kaioken");
            InventoryService.gI().subQuantityItemsBag(player, hq, 1);
            InventoryService.gI().sendItemBags(player);
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
        }
    }
    
    
    
     public void setKaioKenchiton(Player player)  {
        Item hq = InventoryService.gI().findItem(player.inventory.itemsBag, 1116);
        Item ao = ItemService.gI().otpKH((short) 1984);
        Item quan = ItemService.gI().otpKH((short) 1985);
        Item gang = ItemService.gI().otpKH((short) 1986);
        Item giay = ItemService.gI().otpKH((short) 1987);
        Item rd = ItemService.gI().otpKH((short) 1988);
        ao.itemOptions.add(new ItemOption(217, 5));
        quan.itemOptions.add(new ItemOption(217, 5));
        gang.itemOptions.add(new ItemOption(217, 5));
        giay.itemOptions.add(new ItemOption(217, 5));
        rd.itemOptions.add(new ItemOption(217, 5));
        
        ao.itemOptions.add(new ItemOption(231, 50));
        quan.itemOptions.add(new ItemOption(231, 50));
        gang.itemOptions.add(new ItemOption(231, 50));
        giay.itemOptions.add(new ItemOption(231, 50));
        rd.itemOptions.add(new ItemOption(231, 50));
        
        ao.itemOptions.add(new ItemOption(30, 0));
        quan.itemOptions.add(new ItemOption(30, 0));
        gang.itemOptions.add(new ItemOption(30, 0));
        giay.itemOptions.add(new ItemOption(30, 0));
        rd.itemOptions.add(new ItemOption(30, 0));
        if (InventoryService.gI().getCountEmptyBag(player) > 4) {
            InventoryService.gI().addItemBag(player, ao, 0);
            InventoryService.gI().addItemBag(player, quan, 0);
            InventoryService.gI().addItemBag(player, gang, 0);
            InventoryService.gI().addItemBag(player, giay, 0);
            InventoryService.gI().addItemBag(player, rd, 0);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player, "Bạn đã nhận được set Kích hoạt ComBo");
            InventoryService.gI().subQuantityItemsBag(player, hq, 1);
            InventoryService.gI().sendItemBags(player);
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
        }
    } 
    
    public void setcombo6masenco(Player player)  {
        Item hq = InventoryService.gI().findItem(player.inventory.itemsBag, 1117);
        Item ao = ItemService.gI().otpKH((short) 1539);
        Item quan = ItemService.gI().otpKH((short) 1540);
        Item gang = ItemService.gI().otpKH((short) 1541);
        Item giay = ItemService.gI().otpKH((short) 1542);
        Item rd = ItemService.gI().otpKH((short) 1543);
        ao.itemOptions.add(new ItemOption(217, 5));
        quan.itemOptions.add(new ItemOption(217, 5));
        gang.itemOptions.add(new ItemOption(217, 5));
        giay.itemOptions.add(new ItemOption(217, 5));
        rd.itemOptions.add(new ItemOption(217, 5));
        
        ao.itemOptions.add(new ItemOption(229, 50));
        quan.itemOptions.add(new ItemOption(229, 50));
        gang.itemOptions.add(new ItemOption(229, 50));
        giay.itemOptions.add(new ItemOption(229, 50));
        rd.itemOptions.add(new ItemOption(229, 50));
        
        ao.itemOptions.add(new ItemOption(30, 0));
        quan.itemOptions.add(new ItemOption(30, 0));
        gang.itemOptions.add(new ItemOption(30, 0));
        giay.itemOptions.add(new ItemOption(30, 0));
        rd.itemOptions.add(new ItemOption(30, 0));
        if (InventoryService.gI().getCountEmptyBag(player) > 4) {
            InventoryService.gI().addItemBag(player, ao, 0);
            InventoryService.gI().addItemBag(player, quan, 0);
            InventoryService.gI().addItemBag(player, gang, 0);
            InventoryService.gI().addItemBag(player, giay, 0);
            InventoryService.gI().addItemBag(player, rd, 0);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player, "Bạn đã nhận được set Kích hoạt ComBo 6 Món");
            InventoryService.gI().subQuantityItemsBag(player, hq, 1);
            InventoryService.gI().sendItemBags(player);
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
        }
    }   
    
    

    public void setLienHoan(Player player) throws Exception {
        Item hq = InventoryService.gI().findItem(player.inventory.itemsBag, 1228);
        Item ao = ItemService.gI().otpKH((short) 1);
        Item quan = ItemService.gI().otpKH((short) 7);
        Item gang = ItemService.gI().otpKH((short) 22);
        Item giay = ItemService.gI().otpKH((short) 28);
        Item rd = ItemService.gI().otpKH((short) 12);
        ao.itemOptions.add(new ItemOption(221, 1));
        quan.itemOptions.add(new ItemOption(221, 1));
        gang.itemOptions.add(new ItemOption(221, 1));
        giay.itemOptions.add(new ItemOption(221, 1));
        rd.itemOptions.add(new ItemOption(221, 1));

        ao.itemOptions.add(new ItemOption(233, 50));
        quan.itemOptions.add(new ItemOption(233, 50));
        gang.itemOptions.add(new ItemOption(233, 50));
        giay.itemOptions.add(new ItemOption(233, 50));
        rd.itemOptions.add(new ItemOption(233, 50));
        ao.itemOptions.add(new ItemOption(30, 0));
        quan.itemOptions.add(new ItemOption(30, 0));
        gang.itemOptions.add(new ItemOption(30, 0));
        giay.itemOptions.add(new ItemOption(30, 0));
        rd.itemOptions.add(new ItemOption(30, 0));
        if (InventoryService.gI().getCountEmptyBag(player) > 4) {
            InventoryService.gI().addItemBag(player, ao, 0);
            InventoryService.gI().addItemBag(player, quan, 0);
            InventoryService.gI().addItemBag(player, gang, 0);
            InventoryService.gI().addItemBag(player, giay, 0);
            InventoryService.gI().addItemBag(player, rd, 0);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player, "Bạn đã nhận được set Kích hoạt Liên hoàn");
            InventoryService.gI().subQuantityItemsBag(player, hq, 1);
            InventoryService.gI().sendItemBags(player);
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
        }
    }

    public void setPicolo(Player player) throws Exception {
        Item hq = InventoryService.gI().findItem(player.inventory.itemsBag, 1228);
        Item ao = ItemService.gI().otpKH((short) 1);
        Item quan = ItemService.gI().otpKH((short) 7);
        Item gang = ItemService.gI().otpKH((short) 22);
        Item giay = ItemService.gI().otpKH((short) 28);
        Item rd = ItemService.gI().otpKH((short) 12);
        ao.itemOptions.add(new ItemOption(220, 1));
        quan.itemOptions.add(new ItemOption(220, 1));
        gang.itemOptions.add(new ItemOption(220, 1));
        giay.itemOptions.add(new ItemOption(220, 1));
        rd.itemOptions.add(new ItemOption(220, 1));

        ao.itemOptions.add(new ItemOption(232, 50));
        quan.itemOptions.add(new ItemOption(232, 50));
        gang.itemOptions.add(new ItemOption(232, 50));
        giay.itemOptions.add(new ItemOption(232, 50));
        rd.itemOptions.add(new ItemOption(232, 50));

        ao.itemOptions.add(new ItemOption(30, 0));
        quan.itemOptions.add(new ItemOption(30, 0));
        gang.itemOptions.add(new ItemOption(30, 0));
        giay.itemOptions.add(new ItemOption(30, 0));
        rd.itemOptions.add(new ItemOption(30, 0));
        if (InventoryService.gI().getCountEmptyBag(player) > 4) {
            InventoryService.gI().addItemBag(player, ao, 0);
            InventoryService.gI().addItemBag(player, quan, 0);
            InventoryService.gI().addItemBag(player, gang, 0);
            InventoryService.gI().addItemBag(player, giay, 0);
            InventoryService.gI().addItemBag(player, rd, 0);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player, "Bạn đã nhận được set Kích hoạt Picolo");
            InventoryService.gI().subQuantityItemsBag(player, hq, 1);
            InventoryService.gI().sendItemBags(player);
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
        }
    }

    public void setPikkoroDaimao(Player player) throws Exception {
        Item hq = InventoryService.gI().findItem(player.inventory.itemsBag, 1228);
        Item ao = ItemService.gI().otpKH((short) 1);
        Item quan = ItemService.gI().otpKH((short) 7);
        Item gang = ItemService.gI().otpKH((short) 22);
        Item giay = ItemService.gI().otpKH((short) 28);
        Item rd = ItemService.gI().otpKH((short) 12);
        ao.itemOptions.add(new ItemOption(222, 1));
        quan.itemOptions.add(new ItemOption(222, 1));
        gang.itemOptions.add(new ItemOption(222, 1));
        giay.itemOptions.add(new ItemOption(222, 1));
        rd.itemOptions.add(new ItemOption(222, 1));

        ao.itemOptions.add(new ItemOption(234, 50));
        quan.itemOptions.add(new ItemOption(234, 50));
        gang.itemOptions.add(new ItemOption(234, 50));
        giay.itemOptions.add(new ItemOption(234, 50));
        rd.itemOptions.add(new ItemOption(234, 50));
        ao.itemOptions.add(new ItemOption(30, 0));
        quan.itemOptions.add(new ItemOption(30, 0));
        gang.itemOptions.add(new ItemOption(30, 0));
        giay.itemOptions.add(new ItemOption(30, 0));
        rd.itemOptions.add(new ItemOption(30, 0));
        if (InventoryService.gI().getCountEmptyBag(player) > 4) {
            InventoryService.gI().addItemBag(player, ao, 0);
            InventoryService.gI().addItemBag(player, quan, 0);
            InventoryService.gI().addItemBag(player, gang, 0);
            InventoryService.gI().addItemBag(player, giay, 0);
            InventoryService.gI().addItemBag(player, rd, 0);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player, "Bạn đã nhận được set Kích hoạt Picolo");
            InventoryService.gI().subQuantityItemsBag(player, hq, 1);
            InventoryService.gI().sendItemBags(player);
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
        }
    }

    public void setKakarot(Player player) throws Exception {
        Item hq = InventoryService.gI().findItem(player.inventory.itemsBag, 1228);
        Item ao = ItemService.gI().otpKH((short) 2);
        Item quan = ItemService.gI().otpKH((short) 8);
        Item gang = ItemService.gI().otpKH((short) 23);
        Item giay = ItemService.gI().otpKH((short) 29);
        Item rd = ItemService.gI().otpKH((short) 12);
        ao.itemOptions.add(new ItemOption(223, 1));
        quan.itemOptions.add(new ItemOption(223, 1));
        gang.itemOptions.add(new ItemOption(223, 1));
        giay.itemOptions.add(new ItemOption(223, 1));
        rd.itemOptions.add(new ItemOption(223, 1));

        ao.itemOptions.add(new ItemOption(226, 50));
        quan.itemOptions.add(new ItemOption(226, 50));
        gang.itemOptions.add(new ItemOption(226, 50));
        giay.itemOptions.add(new ItemOption(226, 50));
        rd.itemOptions.add(new ItemOption(226, 50));
        ao.itemOptions.add(new ItemOption(30, 0));
        quan.itemOptions.add(new ItemOption(30, 0));
        gang.itemOptions.add(new ItemOption(30, 0));
        giay.itemOptions.add(new ItemOption(30, 0));
        rd.itemOptions.add(new ItemOption(30, 0));
        if (InventoryService.gI().getCountEmptyBag(player) > 4) {
            InventoryService.gI().addItemBag(player, ao, 0);
            InventoryService.gI().addItemBag(player, quan, 0);
            InventoryService.gI().addItemBag(player, gang, 0);
            InventoryService.gI().addItemBag(player, giay, 0);
            InventoryService.gI().addItemBag(player, rd, 0);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player, "Bạn đã nhận được set Kích hoạt Kakarot");
            InventoryService.gI().subQuantityItemsBag(player, hq, 1);
            InventoryService.gI().sendItemBags(player);
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
        }
    }

    public void setCadic(Player player) throws Exception {
        Item hq = InventoryService.gI().findItem(player.inventory.itemsBag, 1228);
        Item ao = ItemService.gI().otpKH((short) 2);
        Item quan = ItemService.gI().otpKH((short) 8);
        Item gang = ItemService.gI().otpKH((short) 23);
        Item giay = ItemService.gI().otpKH((short) 29);
        Item rd = ItemService.gI().otpKH((short) 12);
        ao.itemOptions.add(new ItemOption(224, 1));
        quan.itemOptions.add(new ItemOption(224, 1));
        gang.itemOptions.add(new ItemOption(224, 1));
        giay.itemOptions.add(new ItemOption(224, 1));
        rd.itemOptions.add(new ItemOption(224, 1));

        ao.itemOptions.add(new ItemOption(227, 50));
        quan.itemOptions.add(new ItemOption(227, 50));
        gang.itemOptions.add(new ItemOption(227, 50));
        giay.itemOptions.add(new ItemOption(227, 50));
        rd.itemOptions.add(new ItemOption(227, 50));
        ao.itemOptions.add(new ItemOption(30, 0));
        quan.itemOptions.add(new ItemOption(30, 0));
        gang.itemOptions.add(new ItemOption(30, 0));
        giay.itemOptions.add(new ItemOption(30, 0));
        rd.itemOptions.add(new ItemOption(30, 0));
        if (InventoryService.gI().getCountEmptyBag(player) > 4) {
            InventoryService.gI().addItemBag(player, ao, 0);
            InventoryService.gI().addItemBag(player, quan, 0);
            InventoryService.gI().addItemBag(player, gang, 0);
            InventoryService.gI().addItemBag(player, giay, 0);
            InventoryService.gI().addItemBag(player, rd, 0);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player, "Bạn đã nhận được set Kích hoạt Ca đíc");
            InventoryService.gI().subQuantityItemsBag(player, hq, 1);
            InventoryService.gI().sendItemBags(player);
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
        }
    }

    public void setNappa(Player player) throws Exception {
        Item hq = InventoryService.gI().findItem(player.inventory.itemsBag, 1228);
        Item ao = ItemService.gI().otpKH((short) 2);
        Item quan = ItemService.gI().otpKH((short) 8);
        Item gang = ItemService.gI().otpKH((short) 23);
        Item giay = ItemService.gI().otpKH((short) 29);
        Item rd = ItemService.gI().otpKH((short) 12);
        ao.itemOptions.add(new ItemOption(225, 1));
        quan.itemOptions.add(new ItemOption(225, 1));
        gang.itemOptions.add(new ItemOption(225, 1));
        giay.itemOptions.add(new ItemOption(225, 1));
        rd.itemOptions.add(new ItemOption(225, 1));

        ao.itemOptions.add(new ItemOption(228, 50));
        quan.itemOptions.add(new ItemOption(228, 50));
        gang.itemOptions.add(new ItemOption(228, 50));
        giay.itemOptions.add(new ItemOption(228, 50));
        rd.itemOptions.add(new ItemOption(228, 50));
        ao.itemOptions.add(new ItemOption(30, 0));
        quan.itemOptions.add(new ItemOption(30, 0));
        gang.itemOptions.add(new ItemOption(30, 0));
        giay.itemOptions.add(new ItemOption(30, 0));
        rd.itemOptions.add(new ItemOption(30, 0));
        if (InventoryService.gI().getCountEmptyBag(player) > 4) {
            InventoryService.gI().addItemBag(player, ao, 0);
            InventoryService.gI().addItemBag(player, quan, 0);
            InventoryService.gI().addItemBag(player, gang, 0);
            InventoryService.gI().addItemBag(player, giay, 0);
            InventoryService.gI().addItemBag(player, rd, 0);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player, "Bạn đã nhận được set Kích hoạt Nappa");
            InventoryService.gI().subQuantityItemsBag(player, hq, 1);
            InventoryService.gI().sendItemBags(player);
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
        }
    }

    public Item otpthanlinh(short tempId) {
        return otptl(tempId);
    }

    public static int highlightsItem(boolean highlights, int value) {
        double highlightsNumber = 1.1;
        return highlights ? (int) (value * highlightsNumber) : value;
    }

    public Item otptl(short tempId) {
        Item it = ItemService.gI().createNewItem((short) tempId);
        List<Integer> ao = Arrays.asList(555, 557, 559);
        List<Integer> quan = Arrays.asList(556, 558, 560);
        List<Integer> gang = Arrays.asList(562, 564, 566);
        List<Integer> giay = Arrays.asList(563, 565, 567);
        int ntl = 561;
        if (ao.contains((int) tempId)) {
            it.itemOptions.add(new ItemOption(47, highlightsItem(it.template.gender == 2, new Random().nextInt(501) + 2000)));
        }
        if (quan.contains((int) tempId)) {
            it.itemOptions.add(new ItemOption(22, highlightsItem(it.template.gender == 0, new Random().nextInt(11) + 500)));
        }
        if (gang.contains((int) tempId)) {
            it.itemOptions.add(new ItemOption(0, highlightsItem(it.template.gender == 2, new Random().nextInt(1001) + 10000)));
        }
        if (giay.contains((int) tempId)) {
            it.itemOptions.add(new ItemOption(23, highlightsItem(it.template.gender == 1, new Random().nextInt(11) + 500)));
        }
        if (ntl == tempId) {
            it.itemOptions.add(new ItemOption(14, new Random().nextInt(3) + 15));
        }
        it.itemOptions.add(new ItemOption(107, Util.nextInt(1, 8)));
        it.itemOptions.add(new ItemOption(30, 1));
        return it;
    }

      public Item otpthiensu(short tempId) {
        Item it = ItemService.gI().createNewItem((short) tempId);
        List<Integer> ao = Arrays.asList(1048, 1049, 1050);
        List<Integer> quan = Arrays.asList(1051, 1052, 1053);
        List<Integer> gang = Arrays.asList(1054, 1055, 1056);
        List<Integer> giay = Arrays.asList(1057, 1058, 1059);
        int ntl = 1060;
        if (ao.contains((int) tempId)) {
            it.itemOptions.add(new ItemOption(47, highlightsItem(it.template.gender == 2, new Random().nextInt(501) + 5000)));
        }
        if (quan.contains((int) tempId)) {
            it.itemOptions.add(new ItemOption(22, highlightsItem(it.template.gender == 0, new Random().nextInt(11) + 1000)));
        }
        if (gang.contains((int) tempId)) {
            it.itemOptions.add(new ItemOption(0, highlightsItem(it.template.gender == 2, new Random().nextInt(1001) + 20000)));
        }
        if (giay.contains((int) tempId)) {
            it.itemOptions.add(new ItemOption(23, highlightsItem(it.template.gender == 1, new Random().nextInt(11) + 1000)));
        }
        if (ntl == tempId) {
            it.itemOptions.add(new ItemOption(14, new Random().nextInt(3) + 15));
        }
        it.itemOptions.add(new ItemOption(107, Util.nextInt(1, 8)));
        it.itemOptions.add(new ItemOption(30, 1));
        return it;
    }
    
    
    
    public void setchitonkh (Player player) {
        Item hq = InventoryService.gI().findItem(player.inventory.itemsBag, 1970);
        Item ao = ItemService.gI().otpthanlinh((short) 1984);
        Item quan = ItemService.gI().otpthanlinh((short) 1985);
        Item gang = ItemService.gI().otpthanlinh((short) 1986);
        Item giay = ItemService.gI().otpthanlinh((short) 1987);
        Item rd = ItemService.gI().otpthanlinh((short) 1988);
        if (InventoryService.gI().getCountEmptyBag(player) > 4) {
            InventoryService.gI().addItemBag(player, ao, 999999999);
            InventoryService.gI().addItemBag(player, quan, 999999999);
            InventoryService.gI().addItemBag(player, gang, 999999999);
            InventoryService.gI().addItemBag(player, giay, 999999999);
            InventoryService.gI().addItemBag(player, rd, 999999999);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player, "Bạn đã nhận được set thần linh trái đất");
            InventoryService.gI().subQuantityItemsBag(player, hq, 1);
            InventoryService.gI().sendItemBags(player);
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
        }
    } 
    
    
    
    
    
    public void setthanlinhtd(Player player) {
        Item hq = InventoryService.gI().findItem(player.inventory.itemsBag, 1970);
        Item ao = ItemService.gI().otpthanlinh((short) 555);
        Item quan = ItemService.gI().otpthanlinh((short) 556);
        Item gang = ItemService.gI().otpthanlinh((short) 562);
        Item giay = ItemService.gI().otpthanlinh((short) 563);
        Item rd = ItemService.gI().otpthanlinh((short) 561);
        if (InventoryService.gI().getCountEmptyBag(player) > 4) {
            InventoryService.gI().addItemBag(player, ao, 999999999);
            InventoryService.gI().addItemBag(player, quan, 999999999);
            InventoryService.gI().addItemBag(player, gang, 999999999);
            InventoryService.gI().addItemBag(player, giay, 999999999);
            InventoryService.gI().addItemBag(player, rd, 999999999);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player, "Bạn đã nhận được set thần linh trái đất");
            InventoryService.gI().subQuantityItemsBag(player, hq, 1);
            InventoryService.gI().sendItemBags(player);
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
        }
    }

    public void setthanlinhnm(Player player) {
        Item hq = InventoryService.gI().findItem(player.inventory.itemsBag, 1970);
        Item ao = ItemService.gI().otpthanlinh((short) 557);
        Item quan = ItemService.gI().otpthanlinh((short) 558);
        Item gang = ItemService.gI().otpthanlinh((short) 564);
        Item giay = ItemService.gI().otpthanlinh((short) 565);
        Item rd = ItemService.gI().otpthanlinh((short) 561);
        if (InventoryService.gI().getCountEmptyBag(player) > 4) {
            InventoryService.gI().addItemBag(player, ao, 999999999);
            InventoryService.gI().addItemBag(player, quan, 999999999);
            InventoryService.gI().addItemBag(player, gang, 999999999);
            InventoryService.gI().addItemBag(player, giay, 999999999);
            InventoryService.gI().addItemBag(player, rd, 999999999);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player, "Bạn đã nhận được set thần");
            InventoryService.gI().subQuantityItemsBag(player, hq, 1);
            InventoryService.gI().sendItemBags(player);
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
        }
    }

    public void setthanlinhxd(Player player) {
        Item hq = InventoryService.gI().findItem(player.inventory.itemsBag, 1970);
        Item ao = ItemService.gI().otpthanlinh((short) 559);
        Item quan = ItemService.gI().otpthanlinh((short) 560);
        Item gang = ItemService.gI().otpthanlinh((short) 566);
        Item giay = ItemService.gI().otpthanlinh((short) 567);
        Item rd = ItemService.gI().otpthanlinh((short) 561);
        if (InventoryService.gI().getCountEmptyBag(player) > 4) {
            InventoryService.gI().addItemBag(player, ao, 999999999);
            InventoryService.gI().addItemBag(player, quan, 999999999);
            InventoryService.gI().addItemBag(player, gang, 999999999);
            InventoryService.gI().addItemBag(player, giay, 999999999);
            InventoryService.gI().addItemBag(player, rd, 999999999);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player, "Bạn đã nhận được set thần");
            InventoryService.gI().subQuantityItemsBag(player, hq, 1);
            InventoryService.gI().sendItemBags(player);
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
        }
    }

    public void setthiensutd(Player player) {
        Item hq = InventoryService.gI().findItem(player.inventory.itemsBag, 1113);
        Item ao = ItemService.gI().otpthiensu((short) 1048);
        Item quan = ItemService.gI().otpthiensu((short) 1051);
        Item gang = ItemService.gI().otpthiensu((short) 1054);
        Item giay = ItemService.gI().otpthiensu((short) 1057);
        Item rd = ItemService.gI().otpthiensu((short) 1060);
        if (InventoryService.gI().getCountEmptyBag(player) > 4) {
            InventoryService.gI().addItemBag(player, ao, 999999999);
            InventoryService.gI().addItemBag(player, quan, 999999999);
            InventoryService.gI().addItemBag(player, gang, 999999999);
            InventoryService.gI().addItemBag(player, giay, 999999999);
            InventoryService.gI().addItemBag(player, rd, 999999999);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player, "Bạn đã nhận được set thần");
            InventoryService.gI().subQuantityItemsBag(player, hq, 1);
            InventoryService.gI().sendItemBags(player);
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
        }
    }  
    
     public void setthiensunm(Player player) {
        Item hq = InventoryService.gI().findItem(player.inventory.itemsBag, 1114);
        Item ao = ItemService.gI().otpthiensu((short) 1049);
        Item quan = ItemService.gI().otpthiensu((short) 1052);
        Item gang = ItemService.gI().otpthiensu((short) 1055);
        Item giay = ItemService.gI().otpthiensu((short) 1058);
        Item rd = ItemService.gI().otpthiensu((short) 1060);
        if (InventoryService.gI().getCountEmptyBag(player) > 4) {
            InventoryService.gI().addItemBag(player, ao, 999999999);
            InventoryService.gI().addItemBag(player, quan, 999999999);
            InventoryService.gI().addItemBag(player, gang, 999999999);
            InventoryService.gI().addItemBag(player, giay, 999999999);
            InventoryService.gI().addItemBag(player, rd, 999999999);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player, "Bạn đã nhận được set thần");
            InventoryService.gI().subQuantityItemsBag(player, hq, 1);
            InventoryService.gI().sendItemBags(player);
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
        }
    }   
    
    public void setthiensuxd(Player player) {
        Item hq = InventoryService.gI().findItem(player.inventory.itemsBag, 1115);
        Item ao = ItemService.gI().otpthiensu((short) 1050);
        Item quan = ItemService.gI().otpthiensu((short) 1053);
        Item gang = ItemService.gI().otpthiensu((short) 1056);
        Item giay = ItemService.gI().otpthiensu((short) 1059);
        Item rd = ItemService.gI().otpthiensu((short) 1060);
        if (InventoryService.gI().getCountEmptyBag(player) > 4) {
            InventoryService.gI().addItemBag(player, ao, 999999999);
            InventoryService.gI().addItemBag(player, quan, 999999999);
            InventoryService.gI().addItemBag(player, gang, 999999999);
            InventoryService.gI().addItemBag(player, giay, 999999999);
            InventoryService.gI().addItemBag(player, rd, 999999999);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player, "Bạn đã nhận được set thần");
            InventoryService.gI().subQuantityItemsBag(player, hq, 1);
            InventoryService.gI().sendItemBags(player);
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
        }
    }    
    
    
    
    
    
    
    
    
    public Item DoThienSu(int itemId, int gender, int perSuccess, int perLucky) {
        Item dots = createItemSetKichHoat(itemId, 1);
        List<Integer> ao = Arrays.asList(1048, 1049, 1050);
        List<Integer> quan = Arrays.asList(1051, 1052, 1053);
        List<Integer> gang = Arrays.asList(1054, 1055, 1056);
        List<Integer> giay = Arrays.asList(1057, 1058, 1059);
        List<Integer> nhan = Arrays.asList(1060, 1061, 1062);
        //áo
        if (ao.contains(itemId)) {
            switch (gender) {
                case 0:
                    dots.itemOptions.add(new ItemOption(47, Util.nextInt(120, 135) * 1_600 / 100)); // áo TD giáp
                    break;
                case 1:
                    dots.itemOptions.add(new ItemOption(47, Util.nextInt(120, 135) * 1_700 / 100)); // áo NM giáp
                    break;
                case 2:
                    dots.itemOptions.add(new ItemOption(47, Util.nextInt(120, 135) * 1_800 / 100)); // áo XD giáp
                    break;
            }
        }
        //quần
        if (Util.isTrue(80, 100)) {
            if (quan.contains(itemId)) {
                switch (gender) {
                    case 0:
                        dots.itemOptions.add(new ItemOption(22, Util.nextInt(120, 130) * 104 / 100)); // quần TD hp
                        dots.itemOptions.add(new ItemOption(27, Util.nextInt(120, 130) * 16_000 / 100)); // quần td hồi hp
                        break;
                    case 1:
                        dots.itemOptions.add(new ItemOption(22, Util.nextInt(120, 130) * 100 / 100)); // quần NM hp
                        dots.itemOptions.add(new ItemOption(27, Util.nextInt(120, 130) * 14_000 / 100)); // quần NM hồi hp
                        break;
                    case 2:
                        dots.itemOptions.add(new ItemOption(22, Util.nextInt(120, 130) * 96 / 100)); // quần XD hp
                        dots.itemOptions.add(new ItemOption(27, Util.nextInt(120, 130) * 12_000 / 100)); // quần XD hồi hp
                        break;
                }
            }
        } else {
            if (quan.contains(itemId)) {
                switch (gender) {
                    case 0:
                        dots.itemOptions.add(new ItemOption(22, Util.nextInt(120, 135) * 104 / 100)); // quần TD hp
                        dots.itemOptions.add(new ItemOption(27, Util.nextInt(120, 135) * 16_000 / 100)); // quần td hồi hp
                        break;
                    case 1:
                        dots.itemOptions.add(new ItemOption(22, Util.nextInt(120, 135) * 100 / 100)); // quần NM hp
                        dots.itemOptions.add(new ItemOption(27, Util.nextInt(120, 135) * 14_000 / 100)); // quần NM hồi hp
                        break;
                    case 2:
                        dots.itemOptions.add(new ItemOption(22, Util.nextInt(120, 135) * 96 / 100)); // quần XD hp
                        dots.itemOptions.add(new ItemOption(27, Util.nextInt(120, 135) * 12_000 / 100)); // quần XD hồi hp
                        break;
                }
            }
        }
        //găng
        if (Util.isTrue(80, 100)) {
            if (gang.contains(itemId)) {
                switch (gender) {
                    case 0:
                        dots.itemOptions.add(new ItemOption(0, Util.nextInt(120, 130) * 8_800 / 100)); // gang TD sd
                        break;
                    case 1:
                        dots.itemOptions.add(new ItemOption(0, Util.nextInt(120, 130) * 8_600 / 100)); // gang NM sd
                        break;
                    case 2:
                        dots.itemOptions.add(new ItemOption(0, Util.nextInt(120, 130) * 9_000 / 100)); // gang XD sd
                        break;
                }
            }
        } else {
            if (gang.contains(itemId)) {
                switch (gender) {
                    case 0:
                        dots.itemOptions.add(new ItemOption(0, Util.nextInt(120, 135) * 8_800 / 100)); // gang TD sd
                        break;
                    case 1:
                        dots.itemOptions.add(new ItemOption(0, Util.nextInt(120, 135) * 8_600 / 100)); // gang NM sd
                        break;
                    case 2:
                        dots.itemOptions.add(new ItemOption(0, Util.nextInt(120, 135) * 9_000 / 100)); // gang XD sd
                        break;
                }
            }
        }
        //giày
        if (Util.isTrue(80, 100)) {
            if (giay.contains(itemId)) {
                switch (gender) {
                    case 0:
                        dots.itemOptions.add(new ItemOption(23, Util.nextInt(120, 130) * 96 / 100)); // quần TD hp
                        dots.itemOptions.add(new ItemOption(28, Util.nextInt(120, 130) * 12_000 / 100)); // quần td hồi hp
                        break;
                    case 1:
                        dots.itemOptions.add(new ItemOption(23, Util.nextInt(120, 130) * 100 / 100)); // quần NM hp
                        dots.itemOptions.add(new ItemOption(28, Util.nextInt(120, 130) * 12_800 / 100)); // quần NM hồi hp
                        break;
                    case 2:
                        dots.itemOptions.add(new ItemOption(23, Util.nextInt(120, 130) * 92 / 100)); // quần XD hp
                        dots.itemOptions.add(new ItemOption(28, Util.nextInt(120, 130) * 11_200 / 100)); // quần XD hồi hp
                        break;
                }
            }
        } else {
            if (giay.contains(itemId)) {
                switch (gender) {
                    case 0:
                        dots.itemOptions.add(new ItemOption(23, Util.nextInt(120, 135) * 96 / 100)); // quần TD hp
                        dots.itemOptions.add(new ItemOption(28, Util.nextInt(120, 135) * 12_000 / 100)); // quần td hồi hp
                        break;
                    case 1:
                        dots.itemOptions.add(new ItemOption(23, Util.nextInt(120, 135) * 100 / 100)); // quần NM hp
                        dots.itemOptions.add(new ItemOption(28, Util.nextInt(120, 135) * 12_800 / 100)); // quần NM hồi hp
                        break;
                    case 2:
                        dots.itemOptions.add(new ItemOption(28, Util.nextInt(120, 135) * 11_200 / 100)); // quần XD hồi hp
                        break;
                }
            }
        }
        if (nhan.contains(itemId)) {
            dots.itemOptions.add(new ItemOption(14, Util.nextInt(120, 135) * 16 / 100)); // nhẫn 18-20%
        }
        dots.itemOptions.add(new ItemOption(21, 70));
        dots.itemOptions.add(new ItemOption(30, 0));
        if (perSuccess <= perLucky) {
            if (perSuccess >= (perLucky - 3)) {
                perLucky = 3;
            } else if (perSuccess <= (perLucky - 4) && perSuccess >= (perLucky - 10)) {
                perLucky = 2;
            } else {
                perLucky = 1;
            }
            dots.itemOptions.add(new ItemOption(41, perLucky));
            ArrayList<Integer> listOptionBonus = new ArrayList<>();
            listOptionBonus.add(42);
            listOptionBonus.add(43);
            listOptionBonus.add(44);
            listOptionBonus.add(45);
            listOptionBonus.add(46);
            listOptionBonus.add(197);
            listOptionBonus.add(198);
            listOptionBonus.add(199);
            listOptionBonus.add(200);
            listOptionBonus.add(201);
            listOptionBonus.add(202);
            listOptionBonus.add(203);
            listOptionBonus.add(204);
            for (int i = 0; i < perLucky; i++) {
                perSuccess = Util.nextInt(0, listOptionBonus.size() - 1);
                dots.itemOptions.add(new ItemOption(listOptionBonus.get(perSuccess), Util.nextInt(1, 6)));
                listOptionBonus.remove(perSuccess);
            }
        }
        return dots;
    }

    public ConsignmentItem convertToConsignmentItem(Item item) {
        ConsignmentItem it = new ConsignmentItem();
        it.itemOptions = new ArrayList<>();
        it.template = item.template;
        it.info = item.info;
        it.content = item.content;
        it.quantity = item.quantity;
        it.createTime = item.createTime;
        for (ItemOption io : item.itemOptions) {
            it.itemOptions.add(new ItemOption(io));
        }
        it.setPriceGold(-1);
        it.setPriceGem(-1);
        return it;
    }

    public Item createItemSetKichHoat(int tempId, int quantity) {
        Item item = new Item();
        item.template = getTemplate(tempId);
        item.quantity = quantity;
        item.itemOptions = createItemNull().itemOptions;
        item.createTime = System.currentTimeMillis();
        item.content = item.getContent();
        item.info = item.getInfo();
        return item;
    }

    public Item createNewItem(short tempId) {
        return createNewItem(tempId, 1);
    }

    public Item createNewItem(short tempId, int quantity) {
        Item item = new Item();
        item.template = getTemplate(tempId);
        item.quantity = quantity;
        item.createTime = System.currentTimeMillis();

        item.content = item.getContent();
        item.info = item.getInfo();
        return item;
    }

    public ConsignmentItem createNewConsignmentItem(short tempId, int quantity) {
        ConsignmentItem item = new ConsignmentItem();
        item.template = getTemplate(tempId);
        item.quantity = quantity;
        item.createTime = System.currentTimeMillis();
        item.content = item.getContent();
        item.info = item.getInfo();
        return item;
    }

    public Item createItemFromItemMap(ItemMap itemMap) {
        Item item = createNewItem(itemMap.itemTemplate.id, itemMap.quantity);
        item.itemOptions = itemMap.options;
        return item;
    }

    public ItemOptionTemplate getItemOptionTemplate(int id) {
        return Manager.ITEM_OPTION_TEMPLATES.get(id);
    }

    public ItemTemplate getTemplate(int id) {
        return Manager.ITEM_TEMPLATES.get(id);
    }

    public boolean isItemActivation(Item item) {
        return false;
    }

    public double getPercentTrainArmor(Item item) {
        if (item != null) {
            switch (item.template.id) {
                case 529:
                case 534:
                    return 10;
                case 530:
                case 535:
                    return 20;
                case 531:
                case 536:
                    return 30;
                case 1112:
                    return 100;    
                    
                default:
                    return 0;
            }
        } else {
            return 0;
        }
    }

    public boolean isTrainArmor(Item item) {
        if (item != null) {
            switch (item.template.id) {
                case 529:
                case 534:
                case 530:
                case 535:
                case 531:
                case 536:
                  
                default:
                    return false;
            }
        } else {
            return false;
        }
    }

    public boolean isOutOfDateTime(Item item) {
        long now = System.currentTimeMillis();
        if (item != null) {
            for (ItemOption io : item.itemOptions) {
                if (io.optionTemplate.id == 93) {
                    int dayPass = (int) TimeUtil.diffDate(new Date(), new Date(item.createTime), TimeUtil.DAY);
                    if (dayPass != 0) {
                        io.param -= dayPass;
                        if (io.param <= 0) {
                            return true;
                        } else {
                            item.createTime = System.currentTimeMillis();
                        }
                    }
                } else if (io.optionTemplate.id == 196) {
                    long e = io.param * 1000L;
                    if (e <= now) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isItemNoLimitQuantity(int id) {// item k giới hạn số lượng
        if (id >= 1066 && id <= 1070) {// mảnh trang bị thiên sứ
            return true;
        }
        return false;
    }
}
