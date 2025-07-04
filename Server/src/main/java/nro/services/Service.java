package nro.services;

import nro.consts.Cmd;
import nro.consts.ConstNpc;
import nro.consts.ConstPlayer;
import nro.data.DataGame;
import nro.jdbc.daos.AccountDAO;
import nro.manager.TopManager;
import nro.models.Part;
import nro.models.PartManager;
import nro.models.item.Item;
import nro.models.item.ItemOption;
import nro.models.map.ItemMap;
import nro.models.map.Zone;
import nro.models.map.dungeon.zones.ZDungeon;
import nro.models.mob.Mob;
import nro.models.player.Pet;
import nro.models.player.Player;
import nro.models.skill.Skill;
import nro.power.Caption;
import nro.power.CaptionManager;
import nro.server.Client;
import nro.models.dragon.Poruga;
import nro.server.Manager;
import nro.server.io.Message;
import nro.server.io.Session;
import nro.services.func.Input;
import nro.utils.FileIO;
import nro.utils.Log;
import nro.utils.TimeUtil;
import nro.utils.Util;
import nro.jdbc.daos.GodGK;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import nro.jdbc.DBService;
import nro.manager.TopToTask;
import nro.models.Bot.BotManager;
import nro.models.boss.Boss;
import nro.models.boss.BossManager;
import nro.models.dragon.Poruga;
import nro.models.map.NamekBall;
import nro.models.npc.specialnpc.BillEgg;
import nro.models.npc.specialnpc.duahau;
import nro.models.shop.ItemShop;
import nro.models.shop.Shop;
import nro.server.ServerManager;
import nro.server.TOP;
import nro.server.TaiXiu;
import nro.services.func.ChangeMapService;

/**
 * @Build Arriety
 * @author Administrator
 */
public class Service {

    private static Service instance;

    public static Service getInstance() {
        if (instance == null) {
            instance = new Service();
        }
        return instance;
    }

    public static Service gI() {
        if (instance == null) {
            instance = new Service();
        }
        return instance;
    }
    
    
    

   public void showListTop(Player player, List<TOP> tops) {
        Message msg = new Message(Cmd.TOP);
        try {
            msg.writer().writeByte(0);
            if (tops == Manager.TopCapTamKjll) {
                msg.writer().writeUTF("Top Cấp Tiên Bang");
            } else if (tops == Manager.TopLbTamKjll) {
                msg.writer().writeUTF("Top Nhập Ma");
            } else if (tops == Manager.TopGiau) {
                msg.writer().writeUTF("Top Giàu Có");
            } else if (tops == Manager.TopNap) {
                msg.writer().writeUTF("Top Đại Gia");
            } else if (tops == Manager.TopTuTien) {
                msg.writer().writeUTF("Top Tu Tiên");
            } else if (tops == Manager.TopExpTamKjll) {
                msg.writer().writeUTF("Top Exp Tiên Bang");
            } else if (tops == Manager.TopPk) {
                msg.writer().writeUTF("Top Pvp");
            } else if (tops == Manager.TopSb) {
                msg.writer().writeUTF("Top Săn Boss");
            } else if (tops == Manager.TopDame) {
                msg.writer().writeUTF("Top Dame Boss");

            } else if (tops == Manager.TopNhs) {
                msg.writer().writeUTF("Top Ngũ Hàng Sơn");

            } else if (tops == Manager.TOPSK) {
                msg.writer().writeUTF("Top Quyên Góp");
              } else if (tops == Manager.Toppass) {
                msg.writer().writeUTF("Top Pass Free");    
               } else if (tops == Manager.Toppassvip) {
                msg.writer().writeUTF("Top Pass vip");     

            } else if (tops == Manager.TOPSANBAT) {
                msg.writer().writeUTF("Top SĂN BẮT");
            } else if (tops == Manager.TOPsknro) {
                msg.writer().writeUTF("Top Nro Siêu Cấp");
            } else if (tops == Manager.Toptuan) {
                msg.writer().writeUTF("Top Nạp Tuần");
            } else if (tops == Manager.Topthang) {
                msg.writer().writeUTF("Top Nạp Tháng");
            } else {
                msg.writer().writeUTF("Top Leo Tháp");
            }
            
            
            msg.writer().writeByte(tops.size());
            for (int i = 0; i < tops.size(); i++) {
                TOP top = tops.get(i);
                Player pl = GodGK.loadById(top.getId_player());
                msg.writer().writeInt(i + 1);
                msg.writer().writeInt((int) pl.id);
                msg.writer().writeShort(pl.getHead());
                if (player.isVersionAbove(220)) {
                    msg.writer().writeShort(-1);
                }
                msg.writer().writeShort(pl.getBody());
                msg.writer().writeShort(pl.getLeg());
                msg.writer().writeUTF(pl.name);
                if (tops == Manager.TopDame) {
                    msg.writer().writeUTF("Dame: " + Util.powerToString(pl.dameBoss));
                } else {
                    msg.writer().writeUTF(top.getInfo1());
                }
                msg.writer().writeUTF(top.getInfo2());
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void ChatAll(int iconId, String text) {
        Message msg;
        try {
            msg = new Message(-70);
            msg.writer().writeShort(iconId);
            msg.writer().writeUTF(text);
            msg.writer().writeByte(0);
            this.sendMessAllPlayer(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void showListPlayer(Player player) {
        Message msg;
        try {
            msg = new Message(-96);
            msg.writer().writeByte(1);
            msg.writer().writeUTF("Player Online : " + Client.gI().getPlayers().size() + " (" + TimeUtil.getTimeNow("HH:mm:ss") + ")");
            msg.writer().writeByte(Client.gI().getPlayers().size());
            for (int i = 0; i < Client.gI().getPlayers().size(); i++) {
                Player pl = Client.gI().getPlayers().get(i);
                msg.writer().writeInt(i + 1);
                msg.writer().writeInt((int) pl.id);
                msg.writer().writeShort(pl.getHead());
                if (player.getSession().version > 214) {
                    msg.writer().writeShort(-1);
                }
                msg.writer().writeShort(pl.getBody());
                msg.writer().writeShort(pl.getLeg());
                msg.writer().writeUTF(pl.name);
                msg.writer().writeUTF(pl.isAdmin() ? "Cán Bộ" : pl.isAdmin() ? "" : "Cư Dân");
                msg.writer().writeUTF(//"ThỏiVàng: "+Util.numberToMoney(InventoryServiceNew.gI().findItemBag (pl, (short)457).quantity ) 

                        "\nSức Mạnh: " + Util.numberToMoney(pl.nPoint.power)
                        + "\nTiềm Năng: " + Util.numberToMoney(pl.nPoint.tiemNang)
                        //                        +"\nVàng: "+Util.numberToMoney(pl.inventory.gold)
                        //                        +"\nNgọc: "+Util.numberToMoney(pl.inventory.gem)
                        //                        +"\nRuby: "+Util.numberToMoney(pl.inventory.ruby)
                        + "\nHP: " + Util.numberToMoney(pl.nPoint.hpMax)
                        + "\nKI: " + Util.numberToMoney(pl.nPoint.mpMax)
                        + "\nSức Đánh: " + Util.numberToMoney(pl.nPoint.dame)
                        + "\ngiáp: " + Util.numberToMoney(pl.nPoint.def)
                        + "\nCM: " + pl.nPoint.crit + "%"
                        + //    +"\n|7|Active Account: "+(pl.getSession().actived==1?"Activated":"Not Actived")
                        "\n[Map: " + pl.zone.map.mapName + "(" + pl.zone.map.mapId + ") " + "Khu: " + pl.zone.zoneId + "]");
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendTitle(Player player, int id) {
        Message me;
        try {
            me = new Message(-128);
            me.writer().writeByte(0);
            me.writer().writeInt((int) player.id);
            me.writer().writeByte(1);
            me.writer().writeByte(-1);
            me.writer().writeShort(50);
            me.writer().writeByte(-1);
            me.writer().writeByte(-1);
            this.sendMessAllPlayerInMap(player, me);
            me.cleanup();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendTitleRv(Player player, Player p2, int id) {
        Message me;
        try {
            me = new Message(-128);
            me.writer().writeByte(0);
            me.writer().writeInt((int) player.id);
            me.writer().writeByte(1);
            me.writer().writeByte(-1);
            me.writer().writeShort(50);
            me.writer().writeByte(-1);
            me.writer().writeByte(-1);
            this.sendMessAllPlayerInMap(player, me);
            me.cleanup();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendFoot(Player player, int id) {
        Message me;
        try {
            me = new Message(-128);
            me.writer().writeByte(0);
            me.writer().writeInt((int) player.id);
            switch (id) {
                case 1356:
                    me.writer().writeShort(74);
                    break;
                case 1357:
                    me.writer().writeShort(75);
                    break;
                case 1358:
                    me.writer().writeShort(76);
                    break;
                case 1359:
                    me.writer().writeShort(77);
                    break;
                case 1360:
                    me.writer().writeShort(78);
                    break;
                case 1361:
                    me.writer().writeShort(79);
                    break;
                case 1362:
                    me.writer().writeShort(80);
                    break;
                case 1363:
                    me.writer().writeShort(81);
                    break;
                case 1364:
                    me.writer().writeShort(82);
                    break;
                default:
                    break;
            }
            me.writer().writeByte(0);
            me.writer().writeByte(-1);
            me.writer().writeShort(1);
            me.writer().writeByte(-1);
            this.sendMessAllPlayerInMap(player, me);
            me.cleanup();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendFootRv(Player player, Player p2, int id) {
        Message me;
        try {
            me = new Message(-128);
            me.writer().writeByte(0);
            me.writer().writeInt((int) player.id);
            switch (id) {
                case 1356:
                    me.writer().writeShort(74);
                    break;
                case 1357:
                    me.writer().writeShort(75);
                    break;
                case 1358:
                    me.writer().writeShort(76);
                    break;
                case 1359:
                    me.writer().writeShort(77);
                    break;
                case 1360:
                    me.writer().writeShort(78);
                    break;
                case 1361:
                    me.writer().writeShort(79);
                    break;
                case 1362:
                    me.writer().writeShort(80);
                    break;
                case 1363:
                    me.writer().writeShort(81);
                    break;
                case 1364:
                    me.writer().writeShort(82);
                    break;
                default:
                    break;
            }

            me.writer().writeByte(0);
            me.writer().writeByte(-1);
            me.writer().writeShort(1);
            me.writer().writeByte(-1);
            p2.sendMessage(me);
            me.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void removeTitle(Player player) {
        Message me;
        try {
            me = new Message(-128);
            me.writer().writeByte(2);
            me.writer().writeInt((int) player.id);
            player.getSession().sendMessage(me);
            this.sendMessAllPlayerInMap(player, me);
            me.cleanup();
            if (player.inventory.itemsBody.get(11).isNotNullItem()) {
                Service.getInstance().sendFoot(player, (short) player.inventory.itemsBody.get(11).template.id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void SendImgSkill9(short SkillId, int IdAnhSKill) {
        Message msg = new Message(62);
        try {
            msg.writer().writeShort(SkillId);
            msg.writer().writeByte(1);
            msg.writer().writeByte(IdAnhSKill);
            Service.getInstance().sendMessAllPlayer(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void addEffectChar(Player pl, int id, int layer, int loop, int loopcount, int stand) {
        if (!pl.idEffChar.contains(id)) {
            pl.idEffChar.add(id);
        }
        try {
            Message msg = new Message(-128);
            msg.writer().writeByte(0);
            msg.writer().writeInt((int) pl.id);
            msg.writer().writeShort(id);
            msg.writer().writeByte(layer);
            msg.writer().writeByte(loop);
            msg.writer().writeShort(loopcount);
            msg.writer().writeByte(stand);
            sendMessAllPlayerInMap(pl.zone, msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendThongBaoBenDuoi(String text) {
        Message msg = null;
        try {
            msg = new Message(93);
            msg.writer().writeUTF(text);
            sendMessAllPlayer(msg);
        } catch (Exception e) {
//            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
                msg = null;
            }
        }
    }

    public void managePlayer(Player player, Message _msg) {
        if (!player.getSession().isAdmin) {
            Service.getInstance().sendThongBao(player, "Chỉ dành cho Admin");
            return;
        }
        if (_msg != null) {
            try {
                String name = _msg.reader().readUTF();
                System.out.println("Check Player : " + name);
                Player pl = Client.gI().getPlayer(name);
                if (pl != null) {
                    int sl = InventoryService.gI().findItemBagByTemp(pl, (short) 457) == null ? 0 : InventoryService.gI().findItemBagByTemp(pl, (short) 457).quantity;
                    NpcService.gI().createMenuConMeo(player, ConstNpc.QUANLYTK, -1, "|7|[ QUẢN LÝ ACCOUNT ]\n"
                            + "|6|\n" + "Player : " + pl.name
                            + "\nAccount ID : " + pl.id + " | " + "IP Connected: " + pl.getSession().ipAddress + " | " + "Client : " + pl.getSession().version
                            + "\nThỏi Vàng : " + Util.numberToMoney(sl)
                            + ", Hồng Ngọc : " + Util.numberToMoney(pl.inventory.ruby)
                            + "\nSố Dư : " + Util.numberToMoney(pl.getSession().vnd)
                            + ", Tổng Nạp : " + Util.numberToMoney(pl.getSession().tongnap)
                            + "\n"
                            + "|7|\n" + "[ DRAGONBALL ONLINE ]",
                            new String[]{"ĐỔI TÊN", "BAN ID", "SÚT CHÓ", "PET MODE", "THU ITEM", "Chát all"},
                            pl);
                } else {
                    Service.getInstance().sendThongBao(player, "Người chơi không tồn tại hoặc đang offline");
                }
            } catch (Exception e) {
                Log.error(Service.class, e);
            }
        } else {
            System.out.println("Manager Player msg null");
        }
    }

    public void sendTextTime(Player pl, byte id, String name, short time) {
        Message msg;
        try {
            msg = new Message(Cmd.MESSAGE_TIME);
            msg.writer().writeByte(id);
            msg.writer().writeUTF(name);
            msg.writer().writeShort(time);
            sendMessAllPlayerInMap(pl.zone, msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void removedanhhieu(Player player) {
        Message me;
        try {
            me = new Message(-128);
            me.writer().writeByte(2);
            me.writer().writeInt((int) player.id);
            player.getSession().sendMessage(me);
            this.sendMessAllPlayerInMap(player, me);
            me.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void senddanhhieu(Player player, int id) {
        Message me;
        try {
            me = new Message(-128);
            me.writer().writeByte(0);
            me.writer().writeInt((int) player.id);
            switch (id) {
                case 1715:
                    me.writer().writeShort(85);
                    break;
                case 1716:
                    me.writer().writeShort(84);
                    break;
                case 1717:
                    me.writer().writeShort(86);
                    break;
                case 1718:
                    me.writer().writeShort(58);
                    break;
                case 1961:
                    me.writer().writeShort(205);
                    break;
                case 1714:
                    me.writer().writeShort(98);
                    break;
                case 1713:
                    me.writer().writeShort(91);
                    break;
                case 1712:
                    me.writer().writeShort(99);
                    break;
                case 1711:
                    me.writer().writeShort(96);
                    break;
                case 1710:
                    me.writer().writeShort(93);
                    break;
                case 1709:
                    me.writer().writeShort(210);
                    break;

            }
            me.writer().writeByte(1);
            me.writer().writeByte(-1);
            me.writer().writeShort(50);
            me.writer().writeByte(-1);
            me.writer().writeByte(-1);
            me.writer().writeByte(-1);
            this.sendMessAllPlayerInMap(player, me);
            me.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendTitle(Player player) {
        Message me;
        try {
            me = new Message(-128);
            me.writer().writeByte(0);
            me.writer().writeInt((int) player.id);
            if (player.setClothes.kakarot == 5) {
//                me.writer().writeShort(310);
//            } else if (player.setClothes.nappa == 5) {
//                me.writer().writeShort(311);
//            } else if (player.setClothes.cadic == 5) {
//                me.writer().writeShort(313);
//            } else if (player.setClothes.pikkoroDaimao == 5) {
//                me.writer().writeShort(314);
//            } else if (player.setClothes.picolo == 5) {
//                me.writer().writeShort(315);
//            } else if (player.setClothes.ocTieu == 5) {
//                me.writer().writeShort(316);
//            } else if (player.setClothes.thienXinHang == 5) {
//                me.writer().writeShort(318);
//            } else if (player.setClothes.kirin == 5) {
//                me.writer().writeShort(319);
//            }
//              else if (player.diemfam < 50000) {
//                me.writer().writeShort(97);
//
//                }
//             else if (player.diemfam > 50000) {
//                me.writer().writeShort(96);
//
                }
           
            
            
            me.writer().writeByte(1);
            me.writer().writeByte(-1);
            me.writer().writeShort(50);
            me.writer().writeByte(-1);
            me.writer().writeByte(-1);
            me.writer().writeByte(-1);
            this.sendMessAllPlayerInMap(player, me);
            me.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMessAllPlayer(Message msg) {
        msg.transformData();
        PlayerService.gI().sendMessageAllPlayer(msg);
    }

    public void sendMessAllPlayerIgnoreMe(Player player, Message msg) {
        msg.transformData();
        PlayerService.gI().sendMessageIgnore(player, msg);
    }

    public void sendPetFollow(Player player, short smallId) {
        Message msg;
        try {
            if (player != null) {
                msg = new Message(31);
                msg.writer().writeInt((int) player.id);
                if (smallId == 0) {
                    msg.writer().writeByte(0);// type 0
                } else {
                    msg.writer().writeByte(1);// type 1
                    msg.writer().writeShort(smallId);
                    msg.writer().writeByte(1);
                    int[] fr = new int[]{0, 1, 2, 3, 4, 5, 6, 7};
                    msg.writer().writeByte(fr.length);
                    for (int i = 0; i < fr.length; i++) {
                        msg.writer().writeByte(fr[i]);
                    }
                    msg.writer().writeShort(smallId == 15067 ? 65 : 75);
                    msg.writer().writeShort(smallId == 15067 ? 65 : 75);
                }
                sendMessAllPlayerInMap(player, msg);
                msg.cleanup();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendPetFollowToMe(Player me, Player pl) {
        Item linhThu = pl.inventory.itemsBody.get(10);
        if (!linhThu.isNotNullItem()) {
            return;
        }
        short smallId = (short) (linhThu.template.iconID - 1);
        Message msg;
        try {
            msg = new Message(31);
            msg.writer().writeInt((int) pl.id);
            msg.writer().writeByte(1);
            msg.writer().writeShort(smallId);
            msg.writer().writeByte(1);
            int[] fr = new int[]{0, 1, 2, 3, 4, 5, 6, 7};
            msg.writer().writeByte(fr.length);
            for (int i = 0; i < fr.length; i++) {
                msg.writer().writeByte(fr[i]);
            }
            msg.writer().writeShort(smallId == 15067 ? 65 : 75);
            msg.writer().writeShort(smallId == 15067 ? 65 : 75);
            me.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Mabu14hAttack(Boss mabu, Player plAttack, int x, int y, byte skillId) {
        mabu.isUseSpeacialSkill = true;
        mabu.lastTimeUseSpeacialSkill = System.currentTimeMillis();
        try {
            Message msg = new Message(51);
            msg.writer().writeInt((int) mabu.id);
            msg.writer().writeByte(skillId);
            msg.writer().writeShort(x);
            msg.writer().writeShort(y);
            if (skillId == 1) {
                msg.writer().writeByte(1);
                double dame = plAttack.injured(mabu, (int) (mabu.nPoint.getDameAttack(false) * (skillId == 1 ? 1.5 : 1)), false, false);
                msg.writer().writeInt((int) plAttack.id);
                msg.writer().writeDouble(dame);
            } else if (skillId == 0) {
                List<Player> listAttack = mabu.getListPlayerAttack(70);
                msg.writer().writeByte(listAttack.size());
                for (int i = 0; i < listAttack.size(); i++) {
                    Player pl = listAttack.get(i);
                    double dame = pl.injured(mabu, mabu.nPoint.getDameAttack(false), false, false);
                    msg.writer().writeInt((int) pl.id);
                    msg.writer().writeDouble(dame);
                }
                listAttack.clear();
            }
            sendMessAllPlayerInMap(mabu.zone, msg);
            mabu.isUseSpeacialSkill = false;
            msg.cleanup();
        } catch (IOException e) {
        }
    }

    public void sendMabuEat(Player plHold, short... point) {
        Message msg;
        try {
            msg = new Message(52);
            msg.writer().writeByte(1);
            msg.writer().writeInt((int) plHold.id);
            msg.writer().writeShort(point[0]);
            msg.writer().writeShort(point[1]);
            sendMessAllPlayerInMap(plHold.zone, msg);
            plHold.location.x = point[0];
            plHold.location.y = point[1];
            MapService.gI().sendPlayerMove(plHold);
            msg.cleanup();
        } catch (IOException e) {
        }
    }

    public void removeMabuEat(Player plHold) {
        PlayerService.gI().changeAndSendTypePK(plHold, ConstPlayer.NON_PK);
        plHold.effectSkill.isHoldMabu = false;
        plHold.effectSkill.isTaskHoldMabu = -1;
        Message msg;
        try {
            msg = new Message(52);
            msg.writer().writeByte(0);
            msg.writer().writeInt((int) plHold.id);
            sendMessAllPlayerInMap(plHold.zone, msg);
            msg.cleanup();
        } catch (IOException e) {
        }
    }

    public void eatPlayer(Boss mabu, Player plHold) {
        mabu.isUseSpeacialSkill = true;
        mabu.lastTimeUseSpeacialSkill = System.currentTimeMillis();
        plHold.effectSkill.isTaskHoldMabu = 1;
        plHold.effectSkill.lastTimeHoldMabu = System.currentTimeMillis();
        try {
            Message msg = new Message(52);
            msg.writer().writeByte(2);
            msg.writer().writeInt((int) mabu.id);
            msg.writer().writeInt((int) plHold.id);
            sendMessAllPlayerInMap(mabu.zone, msg);
            mabu.isUseSpeacialSkill = false;
            msg.cleanup();
        } catch (IOException e) {
        }
    }

    public void sendPopUpMultiLine(Player pl, int tempID, int avt, String text) {
        Message msg = null;
        try {
            msg = new Message(-218);
            msg.writer().writeShort(tempID);
            msg.writer().writeUTF(text);
            msg.writer().writeShort(avt);
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
//            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
                msg = null;
            }
        }
    }

    public void sendMessAllPlayerInMap(Zone zone, Message msg) {
        msg.transformData();
        if (zone != null) {
            List<Player> players = zone.getPlayers();
            synchronized (players) {
                for (Player pl : players) {
                    if (pl != null) {
                        pl.sendMessage(msg);
                    }
                }
            }
            msg.cleanup();
        }
    }

    public void sendMessAllPlayerInMap(Player player, Message msg) {
        msg.transformData();
        if (player.zone != null) {
            if (player.zone.map.isMapOffline) {
                if (player.isPet) {
                    ((Pet) player).master.sendMessage(msg);
                } else {
                    player.sendMessage(msg);
                }
            } else {
                List<Player> players = player.zone.getPlayers();
                synchronized (players) {
                    for (Player pl : players) {
                        if (pl != null) {
                            pl.sendMessage(msg);
                        }
                    }
                }

                msg.cleanup();
            }
        }
    }

    public void switchToRegisterScr(Session session) {
        try {
            Message message;
            try {
                message = new Message(42);
                message.writer().writeByte(0);
                session.sendMessage(message);
                message.cleanup();
            } catch (Exception e) {
            }
        } catch (Exception e) {
        }
    }

    public void regisAccount(Session session, Message _msg) {
        try {
            _msg.reader().readUTF();
            _msg.reader().readUTF();
            _msg.reader().readUTF();
            _msg.reader().readUTF();
            _msg.reader().readUTF();
            _msg.reader().readUTF();
            _msg.reader().readUTF();
            String user = _msg.reader().readUTF();
            String pass = _msg.reader().readUTF();
            System.out.print("Regis " + session.ipAddress + "\n");
            if (!(user.length() >= 4 && user.length() <= 18)) {
                sendThongBaoOK((Session) session, "Tài khoản phải có độ dài 4-18 ký tự");
                return;
            }
            if (!(pass.length() >= 6 && pass.length() <= 18)) {
                sendThongBaoOK((Session) session, "Mật khẩu phải có độ dài 6-18 ký tự");
                return;
            }
            PreparedStatement ps = DBService.gI().getConnectionForGame().prepareStatement("SELECT * FROM `account` WHERE `username` like ? LIMIT 1;",
                    ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ps.setString(1, user);
            ResultSet rs = ps.executeQuery();
            if (rs.first()) {
                sendThongBaoOK((Session) session, "Tài khoản (" + user + ") đã tồn tại!");
                return;
            } else {
                sendThongBaoOK((Session) session, "Vui lòng đợi...\nĐang tiến hành đăng ký tài khoản!");
                Thread.sleep(3000);
                try {
                    ps = DBService.gI().getConnectionForGame().prepareStatement("insert into `account` (`username`, `password`) VALUES (?,?)");
                    ps.setString(1, user);
                    ps.setString(2, pass);
                    ps.executeUpdate();
                    System.out.print("RegisAccount (" + user + ") Success!\n");
                    sendThongBaoOK((Session) session, "Đăng ký tài khoản thành công!\nTài khoản (" + user + ") đã được tạo mới!");
                } catch (SQLException e) {
                }
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
        }
    }

    public void sendMessAnotherNotMeInMap(Player player, Message msg) {
        if (player.zone != null) {
            List<Player> players = player.zone.getPlayers();
            synchronized (players) {
                for (Player pl : players) {
                    if (pl != null && !pl.equals(player)) {
                        pl.sendMessage(msg);
                    }
                }
            }

            msg.cleanup();
        }
    }

    public void Send_Info_NV(Player pl) {
        Message msg;
        try {
            msg = Service.getInstance().messageSubCommand((byte) 14);//Cập nhật máu
            msg.writer().writeInt((int) pl.id);
            msg.writer().writeDouble(pl.nPoint.hp);
            msg.writer().writeByte(0);//Hiệu ứng Ăn Đậu
            msg.writer().writeDouble(pl.nPoint.hpMax);
            sendMessAnotherNotMeInMap(pl, msg);
            msg.cleanup();
        } catch (Exception e) {

        }
    }

    public void sendInfoPlayerEatPea(Player pl) {
        Message msg;
        try {
            msg = Service.getInstance().messageSubCommand((byte) 14);
            msg.writer().writeInt((int) pl.id);
            msg.writer().writeDouble(pl.nPoint.hp);
            msg.writer().writeByte(1);
            msg.writer().writeDouble(pl.nPoint.hpMax);
            sendMessAnotherNotMeInMap(pl, msg);
            msg.cleanup();
        } catch (Exception e) {

        }
    }

    public void loginDe(Session session, short second) {
        Message msg;
        try {
            msg = new Message(122);
            msg.writer().writeShort(second);
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void resetPoint(Player player, int x, int y) {
        Message msg;
        try {
            player.location.x = x;
            player.location.y = y;
            msg = new Message(46);
            msg.writer().writeShort(x);
            msg.writer().writeShort(y);
            player.sendMessage(msg);
            msg.cleanup();

        } catch (Exception e) {
        }
    }

    public void clearMap(Player player) {
        Message msg;
        try {
            msg = new Message(-22);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void chat(Player player, String text) {
        if (player.getSession() != null && player.isAdmin()) {
            if (text.equals("logskill")) {
                Service.getInstance().sendThongBao(player, player.playerSkill.skillSelect.coolDown + "");
                return;
            } else if (text.startsWith("i ")) {
                int itemId = Integer.parseInt(text.replace("i ", ""));
                Item item = ItemService.gI().createNewItem(((short) itemId));

                InventoryService.gI().addItemBag(player, item, 1000000000);
                InventoryService.gI().sendItemBags(player);
                Service.getInstance().sendThongBao(player, "GET " + item.template.name + " [" + item.template.id + "] SUCCESS !");
                return;
            }

            if (text.startsWith("skill")) {
    try {
        String[] parts = text.split(" ");
        int skillId = Integer.parseInt(parts[1]);
        int level = Integer.parseInt(parts[2]);

        Skill skill = player.playerSkill.getSkillbyId(skillId);
        if (skill != null) {
            skill.point = level;

            // Gửi lại thanh skill cập nhật
            player.playerSkill.sendSkillShortCut();
            player.playerSkill.sendSkillShortCutNew();

            Service.getInstance().sendThongBao(player,
                "Đã nâng kỹ năng ID " + skillId + " lên cấp " + level);
        } else {
            Service.getInstance().sendThongBao(player, "Không tìm thấy kỹ năng ID " + skillId);
        }
    } catch (Exception e) {
        Service.getInstance().sendThongBao(player, "Cú pháp đúng: skill <id> <cấp>");
    }
}


            if (text.equals("videl")) {
                PetService.gI().changeVidelPet(player, player.gender);
                return;
            }
            if (text.equals("update")) {
                player.pet.setLever(player.pet.getLever() + 1);
                player.zone.loadAnotherToMe(player);
                player.zone.load_Me_To_Another(player);
                return;
            }
            if (text.equals("a")) {
                BossManager.gI().showListBoss(player);
                return;
            }
            if (text.equals("dh")) {
                duahau.createduahau(player);
            }
            if (text.equals("bill")) {
                BillEgg.createBillEgg(player);
            }

            if (text.equals("tele")) {
                this.sendThongBao(player, "Thực thi lệnh thành công");
                List<Player> playersMap = Client.gI().getPlayers();
                for (Player pl : playersMap) {
                    if (pl != null && !player.equals(pl)) {
                        if (pl.zone != null) {
                            ChangeMapService.gI().changeMap(pl, player.zone, player.location.x, player.location.y);
                        }
                        Service.getInstance().sendThongBao(pl, "|2|Bạn đã được ADMIN gọi đến đây");
                    }
                }
                return;
            }
            if (text.equals("pk")) {
                this.sendThongBao(player, "Xiên toàn server thành công");
                List<Player> playersMap = Client.gI().getPlayers();
                for (Player pl : playersMap) {
                    if (pl != null && !player.equals(pl)) {
                        pl.isDie();
                        pl.setDie(player);
                        PlayerService.gI().sendInfoHpMpMoney(pl);
                        Service.getInstance().Send_Info_NV(pl);
                        Service.getInstance().sendThongBaoFromAdmin(pl, "|2|\nADMIN ĐÃ TÀN SÁT CẢ SERVER");
                    }
                }
                return;
            }else if (text.equals("bot")) {
                NpcService.gI().createMenuConMeo(player, ConstNpc.MENU_BOT, 31808, "|7| Menu bot\n"
                        + "Player online : " + Client.gI().getPlayers().size() + "\n"
                        + "Bot online : " + BotManager.gI().bot.size(),
                        "Bot\nPem Quái", "Bot\nBán Item", "Đóng");
                return;
            }

            if (text.equals("ad")) {
                showListPlayer(player);
                return;
            }

  
            if (text.equals("client")) {
                Client.gI().show(player);
                return;
            }
            if (text.equals("bu")) {
                Input.gI().createFormSenditem1(player);
                return;
            } else if (text.startsWith("upp ")) {
                try {
                    long power = Long.parseLong(text.replaceAll("upp ", ""));
                    addSMTN(player.pet, (byte) 2, power, false);
                    Service.getInstance().sendThongBaoOK(player, "Thành công");
                    return;
                } catch (Exception e) {
                    System.out.println("Lỗi buff sức mạnh");
                }
            }
            if (text.equals("shop")) {
                Manager.reloadShop();
                this.sendThongBao(player, "Load Shop Success");
                return;
            }
            if (text.equals("top")) {
                Manager.reloadtop();
                this.sendThongBao(player, "Load top Success");
                return;
            }

            if (text.equals("skillxd")) {
                SkillService.gI().learSkillSpecial(player, Skill.SUPER_ANTOMIC);
                return;
            }
            if (text.equals("skilltd")) {
                SkillService.gI().learSkillSpecial(player, Skill.SUPER_KAME);
                return;
            }
            if (text.equals("skillnm")) {
                SkillService.gI().learSkillSpecial(player, Skill.MAFUBA);
                return;
            }
            if (text.equals("r")) { // hồi all skill, Ki
                Service.getInstance().releaseCooldownSkill(player);
                return;

            }

            if (text.equals("admin") || text.equals("sumovip1")) {
                NpcService.gI().createMenuConMeo(player, ConstNpc.MENU_ADMIN, -1,
                        "|1|Chúc Bạn Vui Vẻ\n|1|Time start server: " + ServerManager.timeStart
                        + "\n|1|ONL: " + Client.gI().getPlayers().size()
                        + ", thread: " + Thread.activeCount(),
                        "Sút All sever", "Buff Cấp\nTiên Bang",
                        "Buff cấp\nNhập Ma",
                        "Buff cấp\nKhai Thác",
                        "Buff Exp\nTiên Bang",
                        "Buff Sao\nThiên phú",
                         "Buff\nĐiểm Farm",
                         "Buff\nĐiểm vip",
                         "Buff\n VND",
                         "Buff\nTổng Nạp",
                          "Buff\nnạp Tuần",
                           "Buff\nNạp Tháng",
                         "Buff\nĐiểm She"
                
                        
                );
                return;
            }
             else if (text.equals("vt")) {
                sendThongBaoFromAdmin(player, player.location.x + " - " + player.location.y + "\n"
                        + player.zone.map.yPhysicInTop(player.location.x, player.location.y));

            } else if (text.startsWith("hp")) {
                player.nPoint.hpg = Double.valueOf(text.replace("hp", ""));
                Service.getInstance().point(player);
                Service.getInstance().sendThongBaoOK(player, "Thành công");
                return;
                //buff ki
            } else if (text.startsWith("double")) {
                Item thoivang = ItemService.gI().createNewItem((short) 0, 1);
                thoivang.itemOptions.add(new ItemOption(101, 999999999999999999L));
                InventoryService.gI().addItemBag(player, thoivang, 1);
                InventoryService.gI().sendItemBags(player);

                return;
                //buff ki
            } else if (text.startsWith("ki")) {
                player.nPoint.mpg = Double.valueOf(text.replace("ki", ""));
                Service.getInstance().point(player);
                Service.getInstance().sendThongBaoOK(player, "Thành công");
                return;
                //buff sd
            } else if (text.startsWith("sd")) {
                player.nPoint.dameg = Double.valueOf(text.replace("sd", ""));
                Service.getInstance().point(player);
                Service.getInstance().sendThongBaoOK(player, "Thành công");
                return;
                //buff giap
            } else if (text.startsWith("def")) {
                player.nPoint.defg = Double.valueOf(text.replace("def", ""));
                Service.getInstance().point(player);
                Service.getInstance().sendThongBaoOK(player, "Thành công");
                return;
                //buff chí mạng
            } else if (text.startsWith("crit")) {
                player.nPoint.critg = Integer.valueOf(text.replace("crit", ""));
                Service.getInstance().point(player);
                Service.getInstance().sendThongBaoOK(player, "Thành công");
                return;
                //buff nhiệm vụ
            } else if (text.startsWith("nv")) {
                player.playerTask.taskMain.id = Integer.valueOf(text.replace("nv", ""));
                player.playerTask.taskMain.index = 0;
                TaskService.gI().sendTaskMain(player);
                TaskService.gI().sendNextTaskMain(player);
                Service.getInstance().sendThongBaoOK(player, "Thành công");
                return;

                //**************************************************************
            } else if (text.contains("tsm")) {
                long power = Long.parseLong(text.replaceAll("tsm", ""));
                player.nPoint.power += (long) power;
                addSMTN(player, (byte) 2, power, false);
                sendThongBao(player, "Bạn vừa tăng thành công " + power + " sức mạnh");
            } else if (text.contains("gsm")) {
                long power = Long.parseLong(text.replaceAll("gsm", ""));
                player.nPoint.power -= (long) power;
                addSMTN(player, (byte) 2, -power, false);
                sendThongBao(player, "Bạn vừa giảm thành công " + power + " sức mạnh");
            } else if (text.contains("ttn")) {
                long power = Long.parseLong(text.replaceAll("ttn", ""));
                player.nPoint.tiemNang += (long) power;
                addSMTN(player, (byte) 2, power, false);
                sendThongBao(player, "Bạn vừa tăng thành công " + power + " tiềm năng");
            } else if (text.contains("gtn")) {
                long power = Long.parseLong(text.replaceAll("gtn", ""));
                player.nPoint.tiemNang -= (long) power;
                addSMTN(player, (byte) 2, -power, false);
                sendThongBao(player, "Bạn vừa giảm thành công " + power + " tiềm năng");
            } else if (text.equals("buff")) {
                Input.gI().createFormSenditem(player);
            } else if (text.equals("chiso")) {
                Input.gI().createFormSenditem1(player);
            } else if (text.startsWith("i")) {
                try {
                    String[] item = text.replace("i", "").split(" ");
                    if (Short.parseShort(item[0]) <= 3000) {
                        Item it = ItemService.gI().createNewItem((short) Short.parseShort(item[0]));
                        if (it != null && item.length == 1) {
                            InventoryService.gI().addItemBag(player, it, 99999999999999999999d);
                            InventoryService.gI().sendItemBags(player);
                            Service.getInstance().sendThongBao(player, "Đã nhận được " + it.template.name);
                        } else if (it != null && item.length == 2 && Client.gI().getPlayer(String.valueOf(item[1])) == null) {
                            it.quantity = Integer.parseInt(item[1]);
                            InventoryService.gI().addItemBag(player, it, 99999999999999999999d);
                            InventoryService.gI().sendItemBags(player);
                            Service.getInstance().sendThongBao(player, "Đã nhận được x" + Integer.valueOf(item[1]) + " " + it.template.name);
                        } else if (it != null && item.length == 2 && Client.gI().getPlayer(String.valueOf(item[1])) != null) {
                            String name = String.valueOf(item[1]);
                            InventoryService.gI().addItemBag(Client.gI().getPlayer(name), it, 9999999999999999999d);
                            InventoryService.gI().sendItemBags(Client.gI().getPlayer(name));
                            Service.getInstance().sendThongBao(player, "Đã buff " + it.template.name + " đến player " + name);
                            Service.getInstance().sendThongBao(Client.gI().getPlayer(name), "Đã nhận được " + it.template.name);
                        } else if (it != null && item.length == 3 && Client.gI().getPlayer(String.valueOf(item[2])) != null) {
                            String name = String.valueOf(item[2]);
                            it.quantity = Integer.parseInt(item[1]);
                            InventoryService.gI().addItemBag(Client.gI().getPlayer(name), it, 9999999999999999999d);
                            InventoryService.gI().sendItemBags(Client.gI().getPlayer(name));
                            Service.getInstance().sendThongBao(player, "Đã buff x" + Integer.valueOf(item[1]) + " " + it.template.name + " đến player " + name);
                            Service.getInstance().sendThongBao(Client.gI().getPlayer(name), "Đã nhận được x" + Integer.valueOf(item[1]) + " " + it.template.name);
                        } else {
                            Service.getInstance().sendThongBao(player, "Không tìm thấy player");
                        }
                    } else {
                        Service.getInstance().sendThongBao(player, "Không tìm thấy item");
                    }
                } catch (NumberFormatException e) {
                    Service.getInstance().sendThongBao(player, "Không tìm thấy player");
                }
                return;

            } else if (text.startsWith("sd_")) {
                player.nPoint.hpg = Double.valueOf(text.replaceAll("sd_", ""));
            } else if (text.startsWith("up")) {
                try {
                    long power = Long.valueOf(text.replaceAll("up", ""));
                    addSMTN(player, (byte) 2, power, false);
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (text.startsWith("m")) {
                try {
                    int mapId = Integer.parseInt(text.replace("m", ""));
                    Zone zone = MapService.gI().getZoneJoinByMapIdAndZoneId(player, mapId, 0);
                    if (zone != null) {
                        player.location.x = 500;
                        player.location.y = zone.map.yPhysicInTop(500, 100);
                        MapService.gI().goToMap(player, zone);
                        Service.getInstance().clearMap(player);
                        zone.mapInfo(player);
                        player.zone.loadAnotherToMe(player);
                        player.zone.load_Me_To_Another(player);
                    }
                    return;
                } catch (NumberFormatException e) {

                    System.out.println("lỗi");
                } catch (Exception e) {

                    e.printStackTrace();
                }

            }
        }
        if (text.startsWith("ten con la ")) {
            PetService.gI().changeNamePet(player, text.replaceAll("ten con la ", ""));

        } else if (text.equals("tutien")) {
            if (player.nPoint.power < 5000000) {
                sendThongBaoOK(player,
                        "Hãy cày thêm: " + (5000000 - player.nPoint.power) + " Sức Mạnh, mới vào đc map thăng thiên");
                return;
            }
            
             if (text.equals("kh")) {
            infoKethon(player);
            return;
             }
            if (player.isDie()) {
                player.Diemvip -= 100;
                sendThongBaoOK(player, "Bạn bị phạt 100 Điểm vip do chết còn ấn lệnh này.");
                return;
            }
            if (player.zone.map.mapId >= 141 && player.zone.map.mapId <= 143) {
               player.Diemvip -= 100;
                sendThongBaoOK(player, "Bạn bị phạt 100 Điểm vip do chết còn ấn lệnh này.");
                return;
            }
            ChangeMapService.gI().changeMapBySpaceShip(player, 141, -1, 269);
        } else if (text.equals("mahoa") && player.Tamkjll_Ma_cot > 0) {
            if (player.Tamkjll_Ma_Hoa != 1) {
                if (Util.canDoWithTime(player.TamkjllLasttimeMaHoa, player.TamkjllTimeTuMa() * 60 * 1000)) {
                    player.TamkjllLasttimeMaHoa = System.currentTimeMillis();
                    player.Tamkjll_Ma_Hoa = 1;
                    player.nPoint.calPoint();
                    Send_Info_NV(player);
                    sendThongBaoOK(player, "Đã bật Ma Móa");
                } else {
                    sendThongBaoOK(player,
                            "Đang trong thời gian chờ hồi Ma hóa, Còn: "
                            + TimeUtil.getTimeLeft(player.TamkjllLasttimeMaHoa,
                                    player.TamkjllTimeTuMa() * 60)
                            + " nữa");
                }
            } else {
                player.TamkjllLasttimeMaHoa = System.currentTimeMillis();
                player.Tamkjll_Ma_Hoa = 0;
                player.nPoint.mp = 0;
                player.nPoint.calPoint();
                Send_Info_NV(player);
                sendThongBaoOK(player, "Đã tắt ma hóa");
            }
            return;
        } else if (text.equals("thienmalenh") && player.Tamkjll_Ma_cot > 0) {
            if (player.Tamkjll_Tu_Ma == 16) {
                Input.gI().THIEMALENH(player);
            } else {
                player.ExpTamkjll = 0;
                sendThongBaoOK(player, "chưa đạt đến tu vi này mà dám sài lệnh Exp Bún về 0");
            }

            if (text.equals("fixlag")) {
                Service.getInstance().player(player);
                Service.getInstance().Send_Caitrang(player);
            }
            if (player.pet != null) {
                if (text.equals("di theo") || text.equals("follow")) {//nhìn nè tại vì nó nmawf trong đây:))
                    player.pet.changeStatus(Pet.FOLLOW);
                } else if (text.equals("bao ve") || text.equals("protect")) {
                    player.pet.changeStatus(Pet.PROTECT);
                } else if (text.equals("tan cong") || text.equals("attack")) {
                    player.pet.changeStatus(Pet.ATTACK);
                } else if (text.equals("ve nha") || text.equals("go home")) {
                    player.pet.changeStatus(Pet.GOHOME);
                } else if (text.equals("bien hinh")) {
                    player.pet.transform();
                }
            }
        }
        if (text.length() > 100) {
            text = text.substring(0, 100);
        }
        chatMap(player, text);
    }

    public void chatMap(Player player, String text) {
        Message msg;
        try {
            msg = new Message(44);
            msg.writer().writeInt((int) player.id);
            msg.writer().writeUTF(text);
            sendMessAllPlayerInMap(player, msg);
            msg.cleanup();
        } catch (Exception e) {
            Log.error(Service.class, e);
        }
    }

    public void chatJustForMe(Player me, Player plChat, String text) {
        Message msg;
        try {
            msg = new Message(44);
            msg.writer().writeInt((int) plChat.id);
            msg.writer().writeUTF(text);
            me.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void point(Player player) {
        player.nPoint.calPoint();
        Send_Info_NV(player);
        if (!player.isPet && !player.isClone && !player.isBoss) {
            Message msg;
            try {
                msg = new Message(-42);
                msg.writer().writeDouble(player.nPoint.hpg);
                msg.writer().writeDouble(player.nPoint.mpg);
                msg.writer().writeDouble(player.nPoint.dameg);
                msg.writer().writeDouble(player.nPoint.hpMax);// hp full
                msg.writer().writeDouble(player.nPoint.mpMax);// mp full
                msg.writer().writeDouble(player.nPoint.hp);// hp
                msg.writer().writeDouble(player.nPoint.mp);// mp
                msg.writer().writeByte(player.nPoint.speed);// speed
                msg.writer().writeByte(20);
                msg.writer().writeByte(20);
                msg.writer().writeByte(1);
                msg.writer().writeDouble(player.nPoint.dame);// dam base
                msg.writer().writeDouble(player.nPoint.def);// def full
                msg.writer().writeByte(player.nPoint.crit);// crit full
                msg.writer().writeLong((player.nPoint.tiemNang));
                msg.writer().writeShort(100);
                msg.writer().writeDouble(player.nPoint.defg);
                msg.writer().writeByte(player.nPoint.critg);
                player.sendMessage(msg);
                msg.cleanup();
            } catch (Exception e) {
                Log.error(Service.class, e);
            }
        }
    }

    public void player(Player pl) {
        if (pl == null) {
            return;
        }
        Message msg;
        try {
            msg = messageSubCommand((byte) 0);
            if (pl.getSession().zoomLevel != 1 && pl.getSession().version == 232) {

            }
            msg.writer().writeInt((int) pl.id);
            msg.writer().writeByte(pl.playerTask.taskMain.id);
            msg.writer().writeByte(pl.gender);
            msg.writer().writeShort(pl.head);
            msg.writer().writeUTF(pl.name);
            msg.writer().writeByte(0); //cPK
            msg.writer().writeByte(pl.typePk);
            msg.writer().writeDouble(pl.nPoint.power);
            msg.writer().writeShort(0);
            msg.writer().writeShort(0);
            msg.writer().writeByte(pl.gender);
            //--------skill---------

            ArrayList<Skill> skills = (ArrayList<Skill>) pl.playerSkill.skills;

            msg.writer().writeByte(pl.playerSkill.getSizeSkill());

            for (Skill skill : skills) {
                if (skill.skillId != -1) {
                    msg.writer().writeShort(skill.skillId);
                }
            }

            //---vang---luong--luongKhoa
            if (pl.getSession().version >= 214) {
                msg.writer().writeLong(pl.inventory.gold);
            } else {
                msg.writer().writeDouble((pl.inventory.gold));
            }
            msg.writer().writeInt(pl.inventory.ruby);
            msg.writer().writeInt(pl.inventory.gem);

            //--------itemBody---------
            ArrayList<Item> itemsBody = (ArrayList<Item>) pl.inventory.itemsBody;
            msg.writer().writeByte(itemsBody.size());
            for (Item item : itemsBody) {
                if (!item.isNotNullItem()) {
                    msg.writer().writeShort(-1);
                } else {
                    msg.writer().writeShort(item.template.id);
                    msg.writer().writeInt(item.quantity);
                    msg.writer().writeUTF(item.getInfo());
                    msg.writer().writeUTF(item.getContent());
                    List<ItemOption> itemOptions = item.itemOptions;
                    msg.writer().writeByte(itemOptions.size());
                    for (ItemOption itemOption : itemOptions) {
                        msg.writer().writeByte(itemOption.optionTemplate.id);
                        msg.writer().writeDouble(itemOption.param);
                    }
                }

            }

            //--------itemBag---------
            ArrayList<Item> itemsBag = (ArrayList<Item>) pl.inventory.itemsBag;
            msg.writer().writeByte(itemsBag.size());
            for (int i = 0; i < itemsBag.size(); i++) {
                Item item = itemsBag.get(i);
                if (!item.isNotNullItem()) {
                    msg.writer().writeShort(-1);
                } else {
                    msg.writer().writeShort(item.template.id);
                    msg.writer().writeInt(item.quantity);
                    msg.writer().writeUTF(item.getInfo());
                    msg.writer().writeUTF(item.getContent());
                    List<ItemOption> itemOptions = item.itemOptions;
                    msg.writer().writeByte(itemOptions.size());
                    for (ItemOption itemOption : itemOptions) {
                        msg.writer().writeByte(itemOption.optionTemplate.id);
                        msg.writer().writeDouble(itemOption.param);
                    }
                }

            }

            //--------itemBox---------
            ArrayList<Item> itemsBox = (ArrayList<Item>) pl.inventory.itemsBox;
            msg.writer().writeByte(itemsBox.size());
            for (int i = 0; i < itemsBox.size(); i++) {
                Item item = itemsBox.get(i);
                if (!item.isNotNullItem()) {
                    msg.writer().writeShort(-1);
                } else {
                    msg.writer().writeShort(item.template.id);
                    msg.writer().writeInt(item.quantity);
                    msg.writer().writeUTF(item.getInfo());
                    msg.writer().writeUTF(item.getContent());
                    List<ItemOption> itemOptions = item.itemOptions;
                    msg.writer().writeByte(itemOptions.size());
                    for (ItemOption itemOption : itemOptions) {
                        msg.writer().writeByte(itemOption.optionTemplate.id);
                        msg.writer().writeDouble(itemOption.param);
                    }
                }
            }
            //-----------------
            DataGame.sendHeadAvatar(msg);
            //-----------------
            msg.writer().writeShort(514); //char info id - con chim thông báo
            msg.writer().writeShort(515); //char info id
            msg.writer().writeShort(537); //char info id
            msg.writer().writeByte(pl.fusion.typeFusion != ConstPlayer.NON_FUSION ? 1 : 0); //nhập thể
            msg.writer().writeInt(333); //deltatime
            msg.writer().writeByte(pl.isNewMember ? 1 : 0); //is new member
            msg.writer().writeShort(pl.getAura()); //idauraeff
            msg.writer().writeByte(pl.getEffFront());
//            }
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {

        }
    }

    public Message messageNotLogin(byte command) throws IOException {
        Message ms = new Message(-29);
        ms.writer().writeByte(command);
        return ms;
    }

    public Message messageNotMap(byte command) throws IOException {
        Message ms = new Message(-28);
        ms.writer().writeByte(command);
        return ms;
    }
public Message messageBoardEventReward(byte command) throws IOException {
        Message ms = new Message(70);
        ms.writer().writeByte(command);
        return ms;
    }
    public Message messageSubCommand(byte command) throws IOException {
        Message ms = new Message(-30);
        ms.writer().writeByte(command);
        return ms;
    }

   public void addSMTN(Player player, byte type, long param, boolean isOri) {
        if (player.isPet) {
            if (player.nPoint.power > player.nPoint.getPowerLimit()) {
                return;
            }
            player.nPoint.powerUp(param);
            player.nPoint.tiemNangUp(param);
            Player master = ((Pet) player).master;

            param = master.nPoint.calSubTNSM(param);
            if (master.nPoint.power < master.nPoint.getPowerLimit()) {
                master.nPoint.powerUp(param);
            }
            master.nPoint.tiemNangUp(param);
            addSMTN(master, type, param, true);
        } else {
            if (player.nPoint.power > player.nPoint.getPowerLimit()) {
                return;
            }
            switch (type) {
                case 1:
                    player.nPoint.tiemNangUp(param);
                    break;
                case 2:
                    player.nPoint.powerUp(param);
                    player.nPoint.tiemNangUp(param);
                    break;
                default:
                    player.nPoint.powerUp(param);
                    break;
            }
            PlayerService.gI().sendTNSM(player, type, (long) param);
            if (isOri) {
                if (player.clan != null) {
                    player.clan.addSMTNClan(player, param);
                }
            }
        }
    }

    public String get_HanhTinh(int hanhtinh) {
        switch (hanhtinh) {
            case 0:
                return "Trái Đất";
            case 1:
                return "Xayda";
            case 2:
                return "Namec";
            default:
                return "";
        }
    }

    public String getCurrStrLevel(Player pl) {
        long sucmanh = pl.nPoint.power;
        if (sucmanh < 3000) {
            return "Tân thủ";
        } else if (sucmanh < 15000) {
            return "Tập sự sơ cấp";
        } else if (sucmanh < 40000) {
            return "Tập sự trung cấp";
        } else if (sucmanh < 90000) {
            return "Tập sự cao cấp";
        } else if (sucmanh < 170000) {
            return "Tân binh";
        } else if (sucmanh < 340000) {
            return "Chiến binh";
        } else if (sucmanh < 700000) {
            return "Chiến binh cao cấp";
        } else if (sucmanh < 1500000) {
            return "Vệ binh";
        } else if (sucmanh < 15000000) {
            return "Vệ binh hoàng gia";
        } else if (sucmanh < 150000000) {
            return "Siêu " + get_HanhTinh(pl.gender) + " cấp 1";
        } else if (sucmanh < 1500000000) {
            return "Siêu " + get_HanhTinh(pl.gender) + " cấp 2";
        } else if (sucmanh < 5000000000L) {
            return "Siêu " + get_HanhTinh(pl.gender) + " cấp 3";
        } else if (sucmanh < 10000000000L) {
            return "Siêu " + get_HanhTinh(pl.gender) + " cấp 4";
        } else if (sucmanh < 40000000000L) {
            return "Thần " + get_HanhTinh(pl.gender) + " cấp 1";
        } else if (sucmanh < 50010000000L) {
            return "Thần " + get_HanhTinh(pl.gender) + " cấp 2";
        } else if (sucmanh < 60010000000L) {
            return "Thần " + get_HanhTinh(pl.gender) + " cấp 3";
        } else if (sucmanh < 70010000000L) {
            return "Giới Vương Thần cấp 11";
        } else if (sucmanh < 80010000000L) {
            return "Giới Vương Thần cấp 2";
        } else if (sucmanh < 100010000000L) {
            return "Giới Vương Thần cấp 3";
        } else if (sucmanh < 11100010000000L) {
            return "Thần Huỷ Diệt cấp 1";
        }
        return "Thần Huỷ Diệt cấp 2";
    }

    public void hsChar(Player pl, double hp, double mp) {
        Message msg;
        try {
            pl.setJustRevivaled();
            pl.nPoint.setHp(hp);
            pl.nPoint.setMp(mp);
            if (!pl.isPet && !pl.isClone) {
                msg = new Message(-16);
                pl.sendMessage(msg);
                msg.cleanup();
                PlayerService.gI().sendInfoHpMpMoney(pl);
            }
            msg = messageSubCommand((byte) 15);
            msg.writer().writeInt((int) pl.id);
            msg.writer().writeDouble(hp);
            msg.writer().writeDouble(mp);
            msg.writer().writeShort(pl.location.x);
            msg.writer().writeShort(pl.location.y);
            sendMessAllPlayerInMap(pl, msg);
            msg.cleanup();
            Send_Info_NV(pl);
            PlayerService.gI().sendInfoHpMp(pl);
        } catch (Exception e) {
            Log.error(Service.class, e);
        }
    }

    public void charDie(Player pl) {
        Message msg;
        try {
            if (!pl.isPet && !pl.isClone) {
                msg = new Message(-17);
                msg.writer().writeByte((int) pl.id);
                msg.writer().writeShort(pl.location.x);
                msg.writer().writeShort(pl.location.y);
                pl.sendMessage(msg);
                msg.cleanup();
            } else {
                //    ((Pet) pl).lastTimeDie = System.currentTimeMillis();
            }

            msg = new Message(-8);
            msg.writer().writeShort((int) pl.id);
            msg.writer().writeByte(0); //cpk
            msg.writer().writeShort(pl.location.x);
            msg.writer().writeShort(pl.location.y);
            sendMessAnotherNotMeInMap(pl, msg);
            msg.cleanup();

//            Send_Info_NV(pl);
        } catch (Exception e) {
            Log.error(Service.class, e);
        }
    }

    public void attackMob(Player pl, int mobId) {
        if (pl != null && pl.zone != null) {
            for (Mob mob : pl.zone.mobs) {
                if (mob.id == mobId) {
                    SkillService.gI().useSkill(pl, null, mob, null);
                    break;
                }
            }
        }
    }

    public void sendEffectHideNPC(Player pl, byte npcID, byte status) {
        Message msg;
        try {
            msg = new Message(-73);
            msg.writer().writeByte(npcID);
            msg.writer().writeByte(status); // 0 = hide
            Service.getInstance().sendMessAllPlayerInMap(pl, msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Send_Caitrang(Player player) {
        if (player != null) {
            Message msg;
            try {
                msg = new Message(-90);
                msg.writer().writeByte(1);// check type
                msg.writer().writeInt((int) player.id); //id player
                short head = player.getHead();
                short body = player.getBody();
                short leg = player.getLeg();

                msg.writer().writeShort(head);//set head
                msg.writer().writeShort(body);//setbody
                msg.writer().writeShort(leg);//set leg
                msg.writer().writeByte(player.effectSkill.isMonkey ? 1 : 0);//set khỉ
                sendMessAllPlayerInMap(player, msg);
                msg.cleanup();
            } catch (Exception e) {
                Log.error(Service.class, e);
            }
        }
    }

    public void setNotMonkey(Player player) {
        Message msg;
        try {
            msg = new Message(-90);
            msg.writer().writeByte(-1);
            msg.writer().writeInt((int) player.id);
            Service.getInstance().sendMessAllPlayerInMap(player, msg);
            msg.cleanup();
        } catch (Exception e) {
            Log.error(Service.class, e);
        }
    }

    public void setNotTranformation(Player player) {
        Message msg;
        try {
            msg = new Message(-90);
            msg.writer().writeByte(-1);
            msg.writer().writeInt((int) player.id);
            Service.getInstance().sendMessAllPlayerInMap(player, msg);
            msg.cleanup();
        } catch (IOException e) {
            Log.error(Service.class, e);
        }
    }

    public void setNotVolution(Player player) {
        Message msg;
        try {
            msg = new Message(-90);
            msg.writer().writeByte(-1);
            msg.writer().writeInt((int) player.id);
            Service.getInstance().sendMessAllPlayerInMap(player, msg);
            msg.cleanup();
        } catch (IOException e) {
            Log.error(Service.class, e);
        }
    }

    public void sendFlagBag(Player pl) {
        try {
            if (pl.getFlagBag() == 310 || pl.getFlagBag() == 246) {
                removeTitle(pl);
                Thread.sleep(500);
            }
            Message msg = new Message(-64);
            msg.writer().writeInt((int) pl.id);
            msg.writer().writeByte(pl.getFlagBag());
            sendMessAllPlayerInMap(pl, msg);
            msg.cleanup();
        } catch (IOException | InterruptedException e) {
        }
    }

    public void sendThongBaoOK(Player pl, String text) {
        if (pl.isPet || pl.isClone) {
            return;
        }
        Message msg;
        try {
            msg = new Message(-26);
            msg.writer().writeUTF(text);
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            Log.error(Service.class, e);
        }
    }

    public void sendThongBaoOK(Session session, String text) {
        Message msg;
        try {
            msg = new Message(-26);
            msg.writer().writeUTF(text);
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void sendThongBaoAllPlayer(String thongBao) {
        Message msg;
        try {
            msg = new Message(-25);
            msg.writer().writeUTF(thongBao);
            this.sendMessAllPlayer(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void sendBigMessage(Player player, int iconId, String text) {
        try {
            Message msg;
            msg = new Message(-70);
            msg.writer().writeShort(iconId);
            msg.writer().writeUTF(text);
            msg.writer().writeByte(0);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (IOException e) {
        }
    }

    public void sendThongBaoFromAdmin(Player player, String text) {
        sendBigMessage(player, 1139, text);
    }

    public void sendBigMessAllPlayer(int iconId, String text) {
        try {
            Message msg;
            msg = new Message(-70);
            msg.writer().writeShort(iconId);
            msg.writer().writeUTF(text);
            msg.writer().writeByte(0);
            this.sendMessAllPlayer(msg);
            msg.cleanup();
        } catch (IOException e) {
        }
    }

    public void sendThongBao(Player pl, String thongBao) {
        Message msg2;
        try {
            msg2 = new Message(-25);
            msg2.writer().writeUTF(thongBao);
            pl.sendMessage(msg2);
            msg2.cleanup();
        } catch (Exception e) {
        }
        return;
    }

    public void sendMoney(Player pl) {
        Message msg;
        try {
            msg = new Message(6);
            long gold = pl.inventory.getGoldDisplay();
            if (pl.isVersionAbove(214)) {
                msg.writer().writeLong(gold);
            } else {
                msg.writer().writeInt((int) gold);
            }
            msg.writer().writeInt(pl.inventory.gem);
            msg.writer().writeInt(pl.inventory.ruby);
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {

        }
    }

    public void sendToAntherMePickItem(Player player, int itemMapId) {
        Message msg;
        try {
            msg = new Message(-19);
            msg.writer().writeShort(itemMapId);
            msg.writer().writeInt((int) player.id);
            sendMessAllPlayerIgnoreMe(player, msg);
            msg.cleanup();
        } catch (Exception e) {
            System.err.println("Lỗi " + e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean isItemMoney(int type) {
        return type == 9 || type == 10 || type == 34;
    }

    public void useSkillNotFocus(Player pl, Message m) throws IOException {
        byte status = m.reader().readByte();
        if (status == 20) {
            byte SkillID = m.reader().readByte();
            short xPlayer = m.reader().readShort();
            short yPlayer = m.reader().readShort();
            byte dir = m.reader().readByte();
            short x = m.reader().readShort();
            short y = m.reader().readShort();
            pl.skillSpecial.setSkillSpecial(dir, xPlayer, yPlayer, x, y);
        }
    }

    public void chatGlobal(Player pl, String text) {
        if (pl.inventory.getGem() >= 5) {
            if (pl.isAdmin() || Util.canDoWithTime(pl.lastTimeChatGlobal, 180000)) {
                if (pl.isAdmin() || pl.nPoint.power > 2000000000) {
                    pl.inventory.subGem(5);
                    sendMoney(pl);
                    pl.lastTimeChatGlobal = System.currentTimeMillis();
                    Message msg;
                    try {
                        msg = new Message(92);
                        msg.writer().writeUTF(pl.name);
                        msg.writer().writeUTF("|5|" + text);
                        msg.writer().writeInt((int) pl.id);
                        msg.writer().writeShort(pl.getHead());
                        msg.writer().writeShort(pl.getBody());
                        msg.writer().writeShort(pl.getFlagBag()); //bag
                        msg.writer().writeShort(pl.getLeg());
                        msg.writer().writeByte(0);
                        sendMessAllPlayer(msg);
                        msg.cleanup();
                    } catch (Exception e) {
                    }
                } else {
                    sendThongBao(pl, "Sức mạnh phải ít nhất 2tỷ mới có thể chat thế giới");
                }
            } else {
                sendThongBao(pl, "Không thể chat thế giới lúc này, vui lòng đợi " + TimeUtil.getTimeLeft(pl.lastTimeChatGlobal, 120));
            }
        } else {
            sendThongBao(pl, "Không đủ ngọc chat thế giới");
        }
    }

    private int tiLeXanhDo = 3;

    public int xanhToDo(int n) {
        return n * tiLeXanhDo;
    }

    public int doToXanh(int n) {
        return (int) n / tiLeXanhDo;
    }

    public static final int[] flagTempId = {363, 364, 365, 366, 367, 368, 369, 370, 371, 519, 520, 747};
    public static final int[] flagIconId = {2761, 2330, 2323, 2327, 2326, 2324, 2329, 2328, 2331, 4386, 4385, 2325};

    public void openFlagUI(Player pl) {
        Message msg;
        try {
            msg = new Message(-103);
            msg.writer().writeByte(0);
            msg.writer().writeByte(flagTempId.length);
            for (int i = 0; i < flagTempId.length; i++) {
                msg.writer().writeShort(flagTempId[i]);
                msg.writer().writeByte(1);
                switch (flagTempId[i]) {
                    case 363:
                        msg.writer().writeByte(73);
                        msg.writer().writeShort(0);
                        break;
                    case 371:
                        msg.writer().writeByte(88);
                        msg.writer().writeShort(10);
                        break;
                    default:
                        msg.writer().writeByte(88);
                        msg.writer().writeShort(5);
                        break;
                }
            }
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void changeFlag(Player pl, int index) {
        Message msg;
        try {
            pl.cFlag = (byte) index;
            msg = new Message(-103);
            msg.writer().writeByte(1);
            msg.writer().writeInt((int) pl.id);
            msg.writer().writeByte(index);
            Service.getInstance().sendMessAllPlayerInMap(pl, msg);
            msg.cleanup();

            msg = new Message(-103);
            msg.writer().writeByte(2);
            msg.writer().writeByte(index);
            msg.writer().writeShort(flagIconId[index]);
            Service.getInstance().sendMessAllPlayerInMap(pl, msg);
            msg.cleanup();

            if (pl.pet != null) {
                pl.pet.cFlag = (byte) index;
                msg = new Message(-103);
                msg.writer().writeByte(1);
                msg.writer().writeInt((int) pl.pet.id);
                msg.writer().writeByte(index);
                Service.getInstance().sendMessAllPlayerInMap(pl.pet, msg);
                msg.cleanup();

                msg = new Message(-103);
                msg.writer().writeByte(2);
                msg.writer().writeByte(index);
                msg.writer().writeShort(flagIconId[index]);
                Service.getInstance().sendMessAllPlayerInMap(pl.pet, msg);
                msg.cleanup();
            }
            if (pl.clone != null) {
                pl.clone.cFlag = (byte) index;
                msg = new Message(-103);
                msg.writer().writeByte(1);
                msg.writer().writeInt((int) pl.clone.id);
                msg.writer().writeByte(index);
                Service.getInstance().sendMessAllPlayerInMap(pl.clone, msg);
                msg.cleanup();

                msg = new Message(-103);
                msg.writer().writeByte(2);
                msg.writer().writeByte(index);
                msg.writer().writeShort(flagIconId[index]);
                Service.getInstance().sendMessAllPlayerInMap(pl.clone, msg);
                msg.cleanup();
            }
            pl.lastTimeChangeFlag = System.currentTimeMillis();
        } catch (Exception e) {
            Log.error(Service.class, e);
        }
    }

    public void sendFlagPlayerToMe(Player me, Player pl) {
        Message msg;
        try {
            msg = new Message(-103);
            msg.writer().writeByte(2);
            msg.writer().writeByte(pl.cFlag);
            msg.writer().writeShort(flagIconId[pl.cFlag]);
            me.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void chooseFlag(Player pl, int index) {
        if (Util.canDoWithTime(pl.lastTimeChangeFlag, 60000)) {
            if (!MapService.gI().isMapBlackBallWar(pl.zone.map.mapId) && !MapService.gI().isMapMabuWar(pl.zone.map.mapId) && !pl.isHoldBlackBall) {
                changeFlag(pl, index);
            } else {
                sendThongBao(pl, "Không thể đổi cờ ở khu vực này");
            }
        } else {
            sendThongBao(pl, "Không thể đổi cờ lúc này! Vui lòng đợi " + TimeUtil.getTimeLeft(pl.lastTimeChangeFlag, 60) + " nữa!");
        }
    }

    public void attackPlayer(Player pl, int idPlAnPem) {
        SkillService.gI().useSkill(pl, pl.zone.getPlayerInMap(idPlAnPem), null, null);
    }

    public void openZoneUI(Player pl) {
        if (pl.zone == null || pl.zone.map.isMapOffline) {
            sendThongBaoOK(pl, "Không thể đổi khu vực trong map này");
            return;
        }
        int mapid = pl.zone.map.mapId;
        if (!pl.isAdmin() && (MapService.gI().isMapDoanhTrai(mapid) || MapService.gI().isMapBanDoKhoBau(mapid) || mapid == 120 || MapService.gI().isMapVS(mapid) || mapid == 126 || pl.zone instanceof ZDungeon)) {
            sendThongBaoOK(pl, "Không thể đổi khu vực trong map này");
            return;
        }
        Message msg;
        try {
            msg = new Message(29);
            msg.writer().writeByte(pl.zone.map.zones.size());
            for (Zone zone : pl.zone.map.zones) {
                msg.writer().writeByte(zone.zoneId);
                int numPlayers = zone.getNumOfPlayers();
                msg.writer().writeByte((numPlayers < 5 ? 0 : (numPlayers < 8 ? 1 : 2)));
                msg.writer().writeByte(numPlayers);
                msg.writer().writeByte(zone.maxPlayer);
                msg.writer().writeByte(0);
            }
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }
  public void infoKethon(Player player) {
        NpcService.gI().createMenuConMeo(player, 6565, -1,
                "|7|Thông tin kết hôn"
                + "\n|5|Số lần Kết hôn: " + player.dakethon + " Lần"
                + "\n|4|+" + (50 * player.dakethon) + "% Chỉ số HP,KI,SD"
                + "\n|5|Số lần Được Cầu hôn: " + player.duockethon + " Lần"
                + "\n|4|+" + (player.duockethon == 0 ? 0 : player.duockethon == 1 ? 10 : player.duockethon == 2 ? 20 : player.duockethon == 3 ? 30 : player.duockethon == 4 ? 40 : player.duockethon == 5 ? 50 : player.duockethon == 6 ? 60 : player.duockethon == 7 ? 70 : player.duockethon == 8 ? 80 : player.duockethon == 9 ? 90 : player.duockethon == 10 ? 100 : player.duockethon == 11 ? 110 : player.duockethon == 12 ? 120 : player.duockethon == 13 ? 130 : player.duockethon == 14 ? 140 : player.duockethon == 15 ? 150 : player.duockethon == 16 ? 160 : player.duockethon == 17 ? 170 : player.duockethon == 18 ? 180 : player.duockethon == 19 ? 190 : 200) + "% Chỉ số HP,KI,SD",
                "OK");
    }
    public void minigame_taixiu(Player player) {
        String time = ((TaiXiu.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) + " giây";
        int ketqua = TaiXiu.gI().z + TaiXiu.gI().y + TaiXiu.gI().x;
        if (TaiXiu.gI().baotri == false) {
            NpcService.gI().createMenuConMeo(player, ConstNpc.TAIXIU, -1, "\n|7|---Tài Xỉu Mua Nhà---\n"
                    + "|7|\n" + "Kết quả kì trước:  " + TaiXiu.gI().x + " : " + TaiXiu.gI().y + " : " + TaiXiu.gI().z + " " + (ketqua >= 10 ? "Tài\n" : "Xỉu\n")
                    + "|5|" + "Kết quả kì trước\n"
                    + "|3|\n" + TaiXiu.gI().tongHistoryString
                    + "\n\n|1|Tổng Cược TÀI: " + Util.format(TaiXiu.gI().goldTai) + " Hồng ngọc"
                    + "\n\n|1|Tổng Cược XỈU: " + Util.format(TaiXiu.gI().goldXiu) + " Hồng ngọc\n"
                    + "\n|5|Đếm ngược: " + time, "Cập nhập", "Cược\nTài", "Cược\nXỉu ", "Đóng");
        }
    }

    public void showthongtinplayer(Player player) {
        NpcService.gI().createMenuConMeo(player, ConstNpc.TAIXIU, -1, "\n|7|---Trò chơi may mắn---\n"
                + "|3|Tiêm năng hiện tại: " + Util.powerToString(player.nPoint.tiemNang),
                "Tắt", "Đóng");

    }

    public void releaseCooldownSkill(Player pl) {
        Message msg;
        try {
            msg = new Message(-94);
            for (Skill skill : pl.playerSkill.skills) {
                skill.coolDown = 0;
                msg.writer().writeShort(skill.skillId);
                int leftTime = (int) (skill.lastTimeUseThisSkill + skill.coolDown - System.currentTimeMillis());
                if (leftTime < 0) {
                    leftTime = 0;
                }
                msg.writer().writeInt(leftTime);
            }
            pl.sendMessage(msg);
            pl.nPoint.setMp(pl.nPoint.mpMax);
            PlayerService.gI().sendInfoHpMpMoney(pl);
            msg.cleanup();

        } catch (Exception e) {
        }
    }

    public void sendResetSkill(Player pl, short id, int cooldown) {
        Message msg = null;
        try {
            msg = new Message(-94);
            msg.writer().writeShort(id);
            msg.writer().writeInt(cooldown);
            pl.sendMessage(msg);
        } catch (Exception e) {
        } finally {
            if (msg != null) {
                msg.cleanup();
                msg = null;
            }
        }
    }

    public void sendTimeSkill(Player pl) {
        Message msg;
        try {
            msg = new Message(-94);
            for (Skill skill : pl.playerSkill.skills) {
                msg.writer().writeShort(skill.skillId);

                int timeLeft = (int) (skill.lastTimeUseThisSkill + skill.coolDown - System.currentTimeMillis());
                if (timeLeft < 0) {
                    timeLeft = 0;
                }
                msg.writer().writeInt(timeLeft);
            }
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void dropItemMap(Zone zone, ItemMap item) {
        Message msg;
        try {
            msg = new Message(68);
            msg.writer().writeShort(item.itemMapId);
            msg.writer().writeShort(item.itemTemplate.id);
            msg.writer().writeShort(item.x);
            msg.writer().writeShort(item.y);
            msg.writer().writeInt((int) item.playerId);//
            if (item.playerId == -2) {
                msg.writer().writeShort(item.range);
            }
            sendMessAllPlayerInMap(zone, msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dropItemMapForMe(Player player, ItemMap item) {
        Message msg;
        try {
            msg = new Message(68);
            msg.writer().writeShort(item.itemMapId);
            msg.writer().writeShort(item.itemTemplate.id);
            msg.writer().writeShort(item.x);
            msg.writer().writeShort(item.y);
            msg.writer().writeInt((int) item.playerId);//
            if (item.playerId == -2) {
                msg.writer().writeShort(item.range);
            }
            player.sendMessage(msg);
            msg.cleanup();

        } catch (Exception e) {
            Log.error(Service.class,
                    e);
        }
    }

    public void showInfoPet(Player pl) {
        if (pl != null && pl.pet != null) {
            Message msg;
            try {
                msg = new Message(-107);
                msg.writer().writeByte(2);
                msg.writer().writeShort(pl.pet.getAvatar());
                msg.writer().writeByte(pl.pet.inventory.itemsBody.size());

                for (Item item : pl.pet.inventory.itemsBody) {
                    if (!item.isNotNullItem()) {
                        msg.writer().writeShort(-1);
                    } else {
                        msg.writer().writeShort(item.template.id);
                        msg.writer().writeInt(item.quantity);
                        msg.writer().writeUTF(item.getInfo());
                        msg.writer().writeUTF(item.getContent());

                        int countOption = item.itemOptions.size();
                        msg.writer().writeByte(countOption);
                        for (ItemOption iop : item.itemOptions) {
                            msg.writer().writeByte(iop.optionTemplate.id);
                            msg.writer().writeDouble(iop.param);
                        }
                    }
                }

                if (pl.session != null && pl.session.version == 999) {
                    msg.writer().writeInt(pl.pet.getHead());
                    msg.writer().writeInt(pl.pet.getBody());
                    msg.writer().writeInt(pl.pet.getLeg());

                    msg.writer().writeDouble(pl.pet.nPoint.hpg);
                    msg.writer().writeDouble(pl.pet.nPoint.mpg);
                    msg.writer().writeDouble(pl.pet.nPoint.dameg);
                    msg.writer().writeDouble(pl.pet.nPoint.defg);
                    msg.writer().writeInt(pl.pet.nPoint.critg);
                }
                msg.writer().writeDouble(pl.pet.nPoint.hp); //hp
                msg.writer().writeDouble(pl.pet.nPoint.hpMax); //hpfull
                msg.writer().writeDouble(pl.pet.nPoint.mp); //mp
                msg.writer().writeDouble(pl.pet.nPoint.mpMax); //mpfull
                msg.writer().writeDouble(pl.pet.nPoint.dame); //damefull
                msg.writer().writeUTF(pl.pet.name); //name
                msg.writer().writeUTF(getCurrStrLevel(pl.pet)); //curr level
                msg.writer().writeDouble(pl.pet.nPoint.power); //power
               msg.writer().writeDouble(pl.nPoint.tiemNang);
                msg.writer().writeByte(pl.pet.getStatus()); //status
                msg.writer().writeShort(pl.pet.nPoint.stamina); //stamina
                msg.writer().writeShort(pl.pet.nPoint.maxStamina); //stamina full
                msg.writer().writeByte(pl.pet.nPoint.crit); //crit
                msg.writer().writeDouble((pl.pet.nPoint.def)); //def
                msg.writer().writeByte(6); //counnt pet skill
                for (int i = 0; i < pl.pet.playerSkill.skills.size(); i++) {
                    if (pl.pet.playerSkill.skills.get(i).skillId != -1) {
                        msg.writer().writeShort(pl.pet.playerSkill.skills.get(i).skillId);
                    } else {
                        switch (i) {
                            case 1:
                                msg.writer().writeShort(-1);
                                msg.writer().writeUTF("Cần đạt sức mạnh 150tr để mở");
                                break;
                            case 2:
                                msg.writer().writeShort(-1);
                                msg.writer().writeUTF("Cần đạt sức mạnh 2000 Tỷ để mở");
                                break;
                            case 3:
                                msg.writer().writeShort(-1);
                                msg.writer().writeUTF("Cần đạt sức mạnh 20k Tỷ để mở");
                                break;

                                 case 4:
                                msg.writer().writeShort(-1);
                                msg.writer().writeUTF("Cần đạt sức mạnh 2Tr Tỷ để mở");
                                break;
                                case 5:
                                msg.writer().writeShort(-1);
                                msg.writer().writeUTF("Cần đạt sức mạnh 200Tr Tỷ để mở");
                                break; 
                                
                                
                        }
                    }
                }
                pl.sendMessage(msg);
                msg.cleanup();

            } catch (Exception e) {
                // Logger.logException(Service.class,
                //     e);
            }
        }
    }

    public void sendSpeedPlayer(Player pl, int speed) {
        Message msg;
        try {
            msg = Service.getInstance().messageSubCommand((byte) 8);
            msg.writer().writeInt((int) pl.id);
            msg.writer().writeByte(speed != -1 ? speed : pl.nPoint.speed);
            pl.sendMessage(msg);
//            Service.getInstance().sendMessAllPlayerInMap(pl.map, msg);
            msg.cleanup();

        } catch (Exception e) {
            Log.error(Service.class,
                    e);
        }
    }

    public void setPos(Player player, int x, int y) {
        player.location.x = x;
        player.location.y = y;
        Message msg;
        try {
            msg = new Message(123);
            msg.writer().writeInt((int) player.id);
            msg.writer().writeShort(x);
            msg.writer().writeShort(y);
            msg.writer().writeByte(1);
            sendMessAllPlayerInMap(player, msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void getPlayerMenu(Player player, int playerId) {
        Message msg;
        try {
            msg = new Message(-79);
            Player pl = player.zone.getPlayerInMap(playerId);
            if (pl != null) {
                msg.writer().writeInt(playerId);
                msg.writer().writeLong(pl.nPoint.power);
                msg.writer().writeUTF(Service.getInstance().getCurrStrLevel(pl));
                Service.getInstance().sendThongBao(pl, player.name + " vừa dòm bạn!");
                player.sendMessage(msg);
            }
            msg.cleanup();
            if (player.isAdmin()) {
                SubMenuService.gI().showMenuForAdmin(player);

            }
              Item NhanKetHon = null;
            try {
                NhanKetHon = InventoryService.gI().findItemBagByTemp(player, 2052);
            } catch (Exception e) {
            }
            if (NhanKetHon != null && NhanKetHon.quantity > 0) {
                SubMenuService.gI().showKetHon(player);
            }
        } catch (Exception e) {
            Log.error(Service.class,
                    e);
        }
    }

    public void subMenuPlayer(Player player) {
        Message msg;
        try {
            msg = messageSubCommand((byte) 63);
            msg.writer().writeByte(1);
            msg.writer().writeUTF("String 1");
            msg.writer().writeUTF("String 2");
            msg.writer().writeShort(550);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void hideWaitDialog(Player pl) {
        Message msg;
        try {
            msg = new Message(-99);
            msg.writer().writeByte(-1);
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void chatPrivate(Player plChat, Player plReceive, String text) {
        Message msg;
        try {
            msg = new Message(92);
            msg.writer().writeUTF(plChat.name);
            msg.writer().writeUTF("|5|" + text);
            msg.writer().writeInt((int) plChat.id);
            msg.writer().writeShort(plChat.getHead());
            msg.writer().writeShort(plChat.getBody());
            msg.writer().writeShort(plChat.getFlagBag()); //bag
            msg.writer().writeShort(plChat.getLeg());
            msg.writer().writeByte(1);
            plChat.sendMessage(msg);
            plReceive.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void changePassword(Player player, String oldPass, String newPass, String rePass) {
        if (player.getSession().pp.equals(oldPass)) {
            if (newPass.length() >= 6) {
                if (newPass.equals(rePass)) {
                    player.getSession().pp = newPass;
                    AccountDAO.updateAccount(player.getSession());
                    Service.getInstance().sendThongBao(player, "Đổi mật khẩu thành công!");
                } else {
                    Service.getInstance().sendThongBao(player, "Mật khẩu nhập lại không đúng!");
                }
            } else {
                Service.getInstance().sendThongBao(player, "Mật khẩu ít nhất 6 ký tự!");
            }
        } else {
            Service.getInstance().sendThongBao(player, "Mật khẩu cũ không đúng!");
        }
    }

    public void switchToCreateChar(Session session) {
        Message msg;
        try {
            msg = new Message(2);
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void sendCaption(Session session, byte gender) {
        Message msg;
        try {
            List<Caption> captions = CaptionManager.getInstance().getCaptions();
            msg = new Message(-41);
            msg.writer().writeByte(captions.size());
            for (Caption caption : captions) {
                msg.writer().writeUTF(caption.getCaption(gender));
            }
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void sendHavePet(Player player) {
        Message msg;
        try {
            msg = new Message(-107);
            msg.writer().writeByte(player.pet == null ? 0 : 1);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void sendWaitToLogin(Session session, int secondsWait) {
        Message msg;
        try {
            msg = new Message(122);
            msg.writer().writeShort(secondsWait);
            session.sendMessage(msg);
            msg.cleanup();

        } catch (Exception e) {
            Log.error(Service.class,
                    e);
        }
    }

    public void sendMessage(Session session, int cmd, String path) {
        Message msg;
        try {
            msg = new Message(cmd);
            msg.writer().write(FileIO.readFile(path));
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void sendTopRank(Player pl) {
        Message msg;
        try {
            msg = new Message(Cmd.THELUC);
            msg.writer().writeInt(1);
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void createItemMap(Player player, int tempId) {
        ItemMap itemMap = new ItemMap(player.zone, tempId, 1, player.location.x, player.location.y, player.id);
        dropItemMap(player.zone, itemMap);
    }

    public void sendNangDong(Player player) {
        Message msg;
        try {
            msg = new Message(-97);
            msg.writer().writeInt(100);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void sendPowerInfo(Player pl, String info, short point) {
        Message m = null;
        try {
            m = new Message(-115);
            m.writer().writeUTF(info);
            m.writer().writeShort(point);
            m.writer().writeShort(20);
            m.writer().writeShort(10);
            m.writer().flush();
            if (pl != null && pl.getSession() != null) {
                pl.sendMessage(m);
            }
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (m != null) {
                m.cleanup();
            }
        }
    }

    public void setMabuHold(Player pl, byte type) {
        Message m = null;
        try {
            m = new Message(52);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (m != null) {
                m.cleanup();
            }
        }
    }

    public void sendPercentMabuEgg(Player player, byte percent) {
        try {
            Message msg = new Message(-117);
            msg.writer().writeByte(percent);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void sendPlayerInfo(Player player) {
        try {
            Message msg = messageSubCommand((byte) 7);
            msg.writer().writeInt((int) player.id);
            if (player.clan != null) {
                msg.writer().writeInt(player.clan.id);
            } else {
                msg.writer().writeInt(-1);
            }
            int level = CaptionManager.getInstance().getLevel(player);
            level = player.isInvisible ? 0 : level;
            msg.writer().writeByte(level);
            msg.writer().writeBoolean(player.isInvisible);
            msg.writer().writeByte(player.typePk);
            msg.writer().writeByte(player.gender);
            msg.writer().writeByte(player.gender);
            msg.writer().writeShort(player.getHead());
            msg.writer().writeUTF(player.name);
            msg.writer().writeDouble(player.nPoint.hp);
            msg.writer().writeDouble(player.nPoint.hpMax);
            msg.writer().writeShort(player.getBody());
            msg.writer().writeShort(player.getLeg());
            msg.writer().writeByte(player.getFlagBag());
            msg.writer().writeByte(-1);
            msg.writer().writeShort(player.location.x);
            msg.writer().writeShort(player.location.y);
            msg.writer().writeShort(0);
            msg.writer().writeShort(0);
            msg.writer().writeByte(0);
            sendMessAllPlayer(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getCurrLevel(Player pl) {

    }

    public int getWidthHeightImgPetFollow(int id) {
        if (id == 15067) {
            return 65;
        }
        return 75;
    }

    public void showTopPower(Player player) {
        List<Player> list = TopManager.getInstance().getList();
        Message msg = new Message(Cmd.TOP);
        try {
            msg.writer().writeByte(0);
            msg.writer().writeUTF("Top Sức Mạnh");
            msg.writer().writeByte(list.size());
            for (int i = 0; i < list.size(); i++) {
                Player pl = list.get(i);
                msg.writer().writeInt(i + 1);
                msg.writer().writeInt((int) pl.id);
                msg.writer().writeShort(pl.getHead());
                if (player.isVersionAbove(220)) {
                    Part part = PartManager.getInstance().find(pl.getHead());
                    msg.writer().writeShort(part.getIcon(0));
                }
                msg.writer().writeShort(pl.getBody());
                msg.writer().writeShort(pl.getLeg());
                msg.writer().writeUTF(pl.name);
                msg.writer().writeUTF(Client.gI().getPlayer(pl.id) != null ? "Online" : "");
                msg.writer().writeUTF("Sức mạnh: " + Util.numberToMoney(pl.nPoint.power));
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
