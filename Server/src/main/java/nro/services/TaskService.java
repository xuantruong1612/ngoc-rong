package nro.services;

import nro.consts.*;
import nro.models.boss.Boss;
import nro.models.boss.BossFactory;
import nro.models.item.Item;
import nro.models.map.ItemMap;
import nro.models.map.Zone;
import nro.models.mob.Mob;
import nro.models.npc.Npc;
import nro.models.player.Player;
import nro.models.task.Achivement;
import nro.models.task.SideTaskTemplate;
import nro.models.task.SubTaskMain;
import nro.models.task.TaskMain;
import nro.server.Manager;
import nro.server.io.Message;
import nro.utils.Log;
import nro.utils.Util;
//import nro.consts.*;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import nro.models.boss.BossManager;
import nro.models.boss.broly.HoTong;
import nro.models.item.ItemOption;

/**
 * @Build Arriety
 */
public class TaskService {

    /**
     * Làm cùng số người trong bang
     */
    private static final byte NMEMBER_DO_TASK_TOGETHER = 2;

    private static TaskService i;

    public static TaskService gI() {
        if (i == null) {
            i = new TaskService();
        }
        return i;
    }

    public void DoneTask(Player player, int id) {// ho tro nv
        if (player.isPl()) {
            doneTask(player, id);
        }
    }

    public TaskMain getTaskMainById(Player player, int id) {
        for (TaskMain tm : Manager.TASKS) {
            if (tm.id == id) {
                TaskMain newTaskMain = new TaskMain(tm);
                newTaskMain.detail = transformName(player, newTaskMain.detail);
                for (SubTaskMain stm : newTaskMain.subTasks) {
                    stm.mapId = (short) transformMapId(player, stm.mapId);
                    stm.npcId = (byte) transformNpcId(player, stm.npcId);
                    stm.notify = transformName(player, stm.notify);
                    stm.name = transformName(player, stm.name);
                }
                return newTaskMain;
            }
        }
        return player.playerTask.taskMain;
    }

    //gửi thông tin nhiệm vụ chính
    public void sendTaskMain(Player player) {
        Message msg;
        try {
            msg = new Message(40);
            msg.writer().writeShort(player.playerTask.taskMain.id);
            msg.writer().writeByte(player.playerTask.taskMain.index);
            msg.writer().writeUTF(player.playerTask.taskMain.name + "");
            msg.writer().writeUTF(player.playerTask.taskMain.detail + getBossVanTieu(player));
            msg.writer().writeByte(player.playerTask.taskMain.subTasks.size());
            for (SubTaskMain stm : player.playerTask.taskMain.subTasks) {
                msg.writer().writeUTF(stm.name);
                msg.writer().writeByte(stm.npcId);
                msg.writer().writeShort(stm.mapId);
                msg.writer().writeUTF(stm.notify);
            }
            msg.writer().writeShort(player.playerTask.taskMain.subTasks.get(player.playerTask.taskMain.index).count);
            for (SubTaskMain stm : player.playerTask.taskMain.subTasks) {
                msg.writer().writeShort(stm.maxCount);
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            Log.error(TaskService.class, e);
        }
    }

    private String getBossVanTieu(Player player) {
        String listBoss = "";
        if (BossManager.gI().findBossByName("Vận tiêu " + player.name) != null) {
            listBoss += "\n\n                                         [Vận Chuyển Tiêu]\n\n";
            for (Boss b : BossManager.BOSSES_IN_GAME) {
                if (b.name.equals("Vận tiêu " + player.name)) {
                    listBoss += "Phẩm chất: " + ((HoTong) b).phamChatTieu + " [ID: " + b.id + "]\n" + "Mục tiêu: " + MapService.gI().getMapById(((HoTong) b).mapHoTong).mapName + "\nThời Gian: " + ((System.currentTimeMillis() - ((HoTong) b).lastTimeChangeMap) / 1000) + "/1200s\n\n";
                }
            }
        }
        return listBoss;
    }

    //chuyển sang task mới
    public void sendNextTaskMain(Player player) {
        rewardDoneTask(player);
        player.playerTask.taskMain = TaskService.gI().getTaskMainById(player, player.playerTask.taskMain.id + 1);
        sendTaskMain(player);
        Service.getInstance().sendThongBao(player, "Nhiệm vụ tiếp theo của bạn là "
                + player.playerTask.taskMain.subTasks.get(player.playerTask.taskMain.index).name);
    }

    //số lượng đã hoàn thành
    public void sendUpdateCountSubTask(Player player) {
        Message msg;
        try {
            msg = new Message(43);
            msg.writer().writeShort(player.playerTask.taskMain.subTasks.get(player.playerTask.taskMain.index).count);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    //chuyển sub task tiếp theo
    public void sendNextSubTask(Player player) {
        Message msg;
        try {
            msg = new Message(41);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    //gửi thông tin nhiệm vụ hiện tại
    public void sendInfoCurrentTask(Player player) {
        Service.getInstance().sendThongBao(player, "Nhiệm vụ hiện tại của bạn là "
                + player.playerTask.taskMain.subTasks.get(player.playerTask.taskMain.index).name);
    }

    public boolean checkDoneTaskTalkNpc(Player player, Npc npc) {
        switch (npc.tempId) {
            case ConstNpc.QUY_LAO_KAME:
                return (doneTask(player, ConstTask.TASK_11_0)
                        || doneTask(player, ConstTask.TASK_12_0)
                        || doneTask(player, ConstTask.TASK_12_1)
                        || doneTask(player, ConstTask.TASK_13_3)
                        || doneTask(player, ConstTask.TASK_14_2)
                        || doneTask(player, ConstTask.TASK_15_4)
                        || doneTask(player, ConstTask.TASK_16_1)
                        || doneTask(player, ConstTask.TASK_17_4)
                        || doneTask(player, ConstTask.TASK_18_2)
                        || doneTask(player, ConstTask.TASK_20_6)
                        || doneTask(player, ConstTask.TASK_21_3)
                        || doneTask(player, ConstTask.TASK_22_4)
                        || doneTask(player, ConstTask.TASK_23_3)
                        || doneTask(player, ConstTask.TASK_24_2)
                        || doneTask(player, ConstTask.TASK_19_2));
            case ConstNpc.TRUONG_LAO_GURU:
                return player.gender == ConstPlayer.NAMEC && (doneTask(player, ConstTask.TASK_11_0)
                        || doneTask(player, ConstTask.TASK_12_0)
                        || doneTask(player, ConstTask.TASK_12_1)
                        || doneTask(player, ConstTask.TASK_13_3)
                        || doneTask(player, ConstTask.TASK_14_2)
                        || doneTask(player, ConstTask.TASK_15_4)
                        || doneTask(player, ConstTask.TASK_16_1)
                        || doneTask(player, ConstTask.TASK_17_4)
                        || doneTask(player, ConstTask.TASK_18_2)
                        || doneTask(player, ConstTask.TASK_20_6)
                        || doneTask(player, ConstTask.TASK_21_3)
                        || doneTask(player, ConstTask.TASK_22_4)
                        || doneTask(player, ConstTask.TASK_23_3)
                        || doneTask(player, ConstTask.TASK_19_2));
            case ConstNpc.VUA_VEGETA:
                return player.gender == ConstPlayer.XAYDA && (doneTask(player, ConstTask.TASK_11_0)
                        || doneTask(player, ConstTask.TASK_12_0)
                        || doneTask(player, ConstTask.TASK_12_1)
                        || doneTask(player, ConstTask.TASK_13_3)
                        || doneTask(player, ConstTask.TASK_15_4)
                        || doneTask(player, ConstTask.TASK_14_2)
                        || doneTask(player, ConstTask.TASK_16_1)
                        || doneTask(player, ConstTask.TASK_18_2)
                        || doneTask(player, ConstTask.TASK_17_4)
                        || doneTask(player, ConstTask.TASK_20_6)
                        || doneTask(player, ConstTask.TASK_21_3)
                        || doneTask(player, ConstTask.TASK_22_4)
                        || doneTask(player, ConstTask.TASK_23_3)
                        || doneTask(player, ConstTask.TASK_19_2));
            case ConstNpc.ONG_GOHAN:
            case ConstNpc.ONG_MOORI:
            case ConstNpc.ONG_PARAGUS:
                return (doneTask(player, ConstTask.TASK_20_1)
                        || doneTask(player, ConstTask.TASK_21_3)
                        || doneTask(player, ConstTask.TASK_22_3)
                        || doneTask(player, ConstTask.TASK_23_2)
                        || doneTask(player, ConstTask.TASK_24_1)
                        || doneTask(player, ConstTask.TASK_25_2)
                        || doneTask(player, ConstTask.TASK_26_5)
                        || doneTask(player, ConstTask.TASK_27_1)
                         || doneTask(player, ConstTask.TASK_28_5)
                        || doneTask(player, ConstTask.TASK_29_5)
                         || doneTask(player, ConstTask.TASK_30_1)
                        
                        || doneTask(player, ConstTask.TASK_24_0));

            case ConstNpc.BO_MONG:
                return (doneTask(player, ConstTask.TASK_9_0)
                        || doneTask(player, ConstTask.TASK_10_2));
            case ConstNpc.DR_DRIEF:
            case ConstNpc.CARGO:
            case ConstNpc.CUI:
                return (doneTask(player, ConstTask.TASK_6_1)
                        || doneTask(player, ConstTask.TASK_7_2)
                        || player.zone.map.mapId == 19 && doneTask(player, ConstTask.TASK_19_3)
                        || player.zone.map.mapId == 19 && doneTask(player, ConstTask.TASK_20_6)
                        || player.zone.map.mapId == 19 && doneTask(player, ConstTask.TASK_21_4));
            case ConstNpc.BUNMA:
            case ConstNpc.DENDE:
            case ConstNpc.APPULE:
                return doneTask(player, ConstTask.TASK_7_2);
            case ConstNpc.BUNMA_TL:
                return (doneTask(player, ConstTask.TASK_22_0)
                        || doneTask(player, ConstTask.TASK_24_5)
                        || doneTask(player, ConstTask.TASK_25_4)
                        || doneTask(player, ConstTask.TASK_26_4)
                        || doneTask(player, ConstTask.TASK_27_5)
                        || doneTask(player, ConstTask.TASK_28_5)
                        || doneTask(player, ConstTask.TASK_29_5));
            case ConstNpc.CALICK:
                return doneTask(player, ConstTask.TASK_24_1);
            case ConstNpc.THAN_MEO_KARIN:
                if (player.playerTask.taskMain.id == 29) {
                    if (player.nPoint.dameg >= 10000) {
                        return doneTask(player, ConstTask.TASK_29_0);
                    }
                }
                return doneTask(player, ConstTask.TASK_9_3);
        }
        return false;
    }

//    //kiểm tra hoàn thành nhiệm vụ gia nhập bang hội
//    public void checkDoneTaskJoinClan(Player player) {
//        if (!player.isBoss && !player.isPet && !player.isClone) {
//            doneTask(player, ConstTask.TASK_0_1);
//        }
//    }
//    //kiểm tra hoàn thành nhiệm vụ lấy item từ rương
//    public void checkDoneTaskGetItemBox(Player player) {
//        if (!player.isBoss && !player.isPet && !player.isClone && !player.isBot) {
//            doneTask(player, ConstTask.TASK_0_3);
//        }
//    }
    //kiểm tra hoàn thành nhiệm vụ sức mạnh
    public void checkDoneTaskPower(Player player, long power) {
        if (!player.isBoss && !player.isPet && !player.isClone && !player.isBot) {
            if (power >= 1000000000000l) {
                doneTask(player, ConstTask.TASK_22_0);
            }
            if (power >= 2000000000000l) {
                doneTask(player, ConstTask.TASK_22_1);
            }
            if (power >= 5000000000000l) {
                doneTask(player, ConstTask.TASK_22_2);

            }

        }
    }

    //kiểm tra hoàn thành nhiệm vụ khi player sử dụng tiềm năng
//    public void checkDoneTaskUseTiemNang(Player player) {
//        if (!player.isBoss && !player.isPet && !player.isClone && !player.isBot) {
//            doneTask(player, ConstTask.TASK_30_0);
//        }
//    }
    //kiểm tra hoàn thành nhiệm vụ khi vào map nào đó
//    public void checkDoneTaskGoToMap(Player player, Zone zoneJoin) {
//        if (!player.isBoss && !player.isClone && !player.isPet && !player.isClone && !player.isMiniPet && !player.isBot) {
//            switch (zoneJoin.map.mapId) {
//                case 39:
//                case 40:
//                case 41:
//                    if (player.location.x >= 635) {
//                        doneTask(player, ConstTask.TASK_0_0);
//                    }
//                    break;
//                case 21:
//                case 22:
//                case 23:
//                    doneTask(player, ConstTask.TASK_0_1);
//                    doneTask(player, ConstTask.TASK_12_0);
//                    break;
//                case 24:
//                case 25:
//                case 26:
//                    doneTask(player, ConstTask.TASK_6_0);
//                    break;
//                case 3:
//                case 11:
//                case 17:
//                    doneTask(player, ConstTask.TASK_7_0);
//                    break;
//                case 0:
//                case 7:
//                case 14:
//                    doneTask(player, ConstTask.TASK_8_0);
//                    break;
//                case 5:
//                case 13:
//                case 20:
//                    doneTask(player, ConstTask.TASK_9_0);
//                    break;
//                case 19:
//                    doneTask(player, ConstTask.TASK_17_0);
//                    break;
//                case 93:
//                    doneTask(player, ConstTask.TASK_22_1);
//                    break;
//                case 97:
//                    doneTask(player, ConstTask.TASK_23_0);
//                    break;
//                case 100:
//                    doneTask(player, ConstTask.TASK_24_0);
//                    break;
//                case 103:
//                    doneTask(player, ConstTask.TASK_25_2);
//                    break;
//            }
//        }
//    }
    //kiểm tra hoàn thành nhiệm vụ khi nhặt item
    public void checkDoneTaskPickItem(Player player, ItemMap item) {
        if (!player.isBoss && !player.isPet && !player.isBot && !player.isClone && item != null && item.itemTemplate != null) {
            switch (item.itemTemplate.id) {
                case 561: //đùi gà
                    doneTask(player, ConstTask.TASK_5_0);
                    break;
                case 78: //em bé
                    doneTask(player, ConstTask.TASK_3_1);
                    Service.getInstance().sendFlagBag(player);
                    break;
                case 380: //capsule ki bi
                    doneTask(player, ConstTask.TASK_25_1);
                    break;
            }
        }
    }

    //kiểm tra hoàn thành nhiệm vụ kết bạn
    public void checkDoneTaskMakeFriend(Player player, Player friend) {
        if (!player.isBoss && !player.isPet && !player.isClone) {
            switch (friend.gender) {
                case ConstPlayer.TRAI_DAT:
                    doneTask(player, ConstTask.TASK_24_0);
                    break;
                case ConstPlayer.NAMEC:
                    doneTask(player, ConstTask.TASK_24_0);
                    break;
                case ConstPlayer.XAYDA:
                    doneTask(player, ConstTask.TASK_24_0);
                    break;
            }
        }
    }

    //kiểm tra hoàn thành nhiệm vụ khi xác nhận menu npc nào đó
    public void checkDoneTaskConfirmMenuNpc(Player player, Npc npc, byte select) {
        if (!player.isBoss && !player.isPet && !player.isClone) {
            switch (npc.tempId) {
                case ConstNpc.DAU_THAN:
                    switch (player.iDMark.getIndexMenu()) {
                        case ConstNpc.MAGIC_TREE_NON_UPGRADE_LEFT_PEA:
                        case ConstNpc.MAGIC_TREE_NON_UPGRADE_FULL_PEA:
                            if (select == 0) {
                                doneTask(player, ConstTask.TASK_0_4);
                            }
                    }
                    break;
            }
        }
    }

    //kiểm tra hoàn thành nhiệm vụ khi tiêu diệt được boss
    public void checkDoneTaskKillBoss(Player player, Boss boss) {
        if (player != null && !player.isBoss && !player.isPet && !player.isClone && !player.isBot) {
            switch ((int) boss.id) {

                case -104:
                case -105:
                case -106:
                case -107:
                case -108:
                case -109:
                case -110:
                    doneTask(player, ConstTask.TASK_25_3);
                    break;

                case BossFactory.BOSS_THOIVANG:
                    doneTask(player, ConstTask.TASK_23_0);
                    break;
                case BossFactory.BOSS_DAUGOD:
                    doneTask(player, ConstTask.TASK_23_1);
                    break;
                    case BossFactory.Bujin:
                    doneTask(player, ConstTask.TASK_26_0);
                    break;  
                      case BossFactory.Kogu:
                    doneTask(player, ConstTask.TASK_26_1);
                    break;   
                    case BossFactory.Zangya:
                    doneTask(player, ConstTask.TASK_26_2);
                    break;  
                    case BossFactory.Bojack:
                    doneTask(player, ConstTask.TASK_26_3);
                    break;   
                     case BossFactory.SiêuBojack:
                    doneTask(player, ConstTask.TASK_26_4);
                    break;  
                   case BossFactory.DaThu1:
                    doneTask(player, ConstTask.TASK_28_0);
                    break; 
                   case BossFactory.DaThu2:
                    doneTask(player, ConstTask.TASK_28_1);
                    break;   
                    case BossFactory.DaThu3:
                    doneTask(player, ConstTask.TASK_28_2);
                    break;  
                      case BossFactory.DaThu4:
                    doneTask(player, ConstTask.TASK_28_3);
                    break; 
                    case BossFactory.DaThu5:
                    doneTask(player, ConstTask.TASK_28_4);
                    break;   
                
            }
        }
    }

    //kiểm tra hoàn thành nhiệm vụ khi giết được quái
    public void checkDoneTaskKillMob(Player player, Mob mob) {
        if (!player.isBoss && !player.isPet && !player.isBot && !player.isClone && !player.isBot) {
            switch (mob.tempId) {
                case ConstMob.MOC_NHAN:
                    doneTask(player, ConstTask.TASK_20_0);
                    break;
                case ConstMob.QUY_DAT:
                    doneTask(player, ConstTask.TASK_21_0);
                    break;
                case ConstMob.KHUNG_LONG:
                    doneTask(player, ConstTask.TASK_21_1);
                    break;
                case ConstMob.LON_LOI:
                    doneTask(player, ConstTask.TASK_21_2);
                    break;
                case ConstMob.KHI_LONG_DO:
                    doneTask(player, ConstTask.TASK_25_0);
                    break;
                case ConstMob.KHI_LONG_VANG:
                    doneTask(player, ConstTask.TASK_25_1);
                    break;
                  case ConstMob.NAPPA:
                    doneTask(player, ConstTask.TASK_27_0);
                    break;   
                    case ConstMob.XEN_CON_CAP_1:
                    doneTask(player, ConstTask.TASK_29_0);
                    break;   
                    case ConstMob.XEN_CON_CAP_2:
                    doneTask(player, ConstTask.TASK_29_1);
                    break;    
                     case ConstMob.XEN_CON_CAP_3:
                    doneTask(player, ConstTask.TASK_29_2);
                    break;   
                     case ConstMob.XEN_CON_CAP_4:
                    doneTask(player, ConstTask.TASK_29_3);
                    break;   
                    
                    
                    
                    
                    
                    
                    

            }
        }
    }

    //xong nhiệm vụ nào đó
    private boolean doneTask(Player player, int idTaskCustom) {
        if (TaskService.gI().isCurrentTask(player, idTaskCustom)) {
            this.addDoneSubTask(player, 1);
            switch (idTaskCustom) {

                case ConstTask.TASK_20_0:
                    if (isCurrentTask(player, idTaskCustom)) {
                        Service.gI().sendThongBao(player, "Bạn Đã Tiêu Diệt "
                                + player.playerTask.taskMain.subTasks.get(player.playerTask.taskMain.index).count + "/"
                                + player.playerTask.taskMain.subTasks.get(player.playerTask.taskMain.index).maxCount + " quái");
                    }
                    break;
                case ConstTask.TASK_20_1:
                    npcSay(player, ConstTask.NPC_NHA,
                            "OK");

                    Item item = ItemService.gI().createNewItem((short) 457, 10000);
                    item.itemOptions.add(new ItemOption(30, Util.nextInt(15, 16)));
                    InventoryService.gI().addItemBag(player, item, 99999999999l);
                    InventoryService.gI().sendItemBags(player);
                    Service.gI().sendThongBaoFromAdmin(player, "Chúc Mừng Bạn Đã Làm Nhiệm Vụ Tốt lắm\nBạn Đã Nhận Được 10k Thỏi Vàng Hãy Tiếp Tục Nha ");
                    break;
                case ConstTask.TASK_21_0:
                    if (isCurrentTask(player, idTaskCustom)) {
                        Service.gI().sendThongBao(player, "Bạn Đã Tiêu Diệt "
                                + player.playerTask.taskMain.subTasks.get(player.playerTask.taskMain.index).count + "/"
                                + player.playerTask.taskMain.subTasks.get(player.playerTask.taskMain.index).maxCount + " quái");
                    }
                    break;
                case ConstTask.TASK_21_1:
                    if (isCurrentTask(player, idTaskCustom)) {
                        Service.gI().sendThongBao(player, "Bạn Đã Tiêu Diệt "
                                + player.playerTask.taskMain.subTasks.get(player.playerTask.taskMain.index).count + "/"
                                + player.playerTask.taskMain.subTasks.get(player.playerTask.taskMain.index).maxCount + " quái");
                    }
                    break;
                case ConstTask.TASK_21_2:
                    if (isCurrentTask(player, idTaskCustom)) {
                        Service.gI().sendThongBao(player, "Bạn Đã Tiêu Diệt "
                                + player.playerTask.taskMain.subTasks.get(player.playerTask.taskMain.index).count + "/"
                                + player.playerTask.taskMain.subTasks.get(player.playerTask.taskMain.index).maxCount + " quái");
                    }
                    break;
                case ConstTask.TASK_21_3:
                    npcSay(player, ConstTask.NPC_NHA,
                            "OK");

                    Item item1 = ItemService.gI().createNewItem((short) 1544, 1);
                    item1.itemOptions.add(new ItemOption(47, 500));
                    item1.itemOptions.add(new ItemOption(101, 500));
                    item1.itemOptions.add(new ItemOption(107, 8));
                    item1.itemOptions.add(new ItemOption(30, 500));

                    Item item2 = ItemService.gI().createNewItem((short) 1545, 1);
                    item2.itemOptions.add(new ItemOption(6, 200000));
                    item2.itemOptions.add(new ItemOption(101, 500));
                    item2.itemOptions.add(new ItemOption(107, 8));
                    item2.itemOptions.add(new ItemOption(30, 500));

                    Item item3 = ItemService.gI().createNewItem((short) 1546, 1);
                    item3.itemOptions.add(new ItemOption(0, 10000));
                    item3.itemOptions.add(new ItemOption(101, 500));
                    item3.itemOptions.add(new ItemOption(107, 8));
                    item3.itemOptions.add(new ItemOption(30, 500));

                    Item item4 = ItemService.gI().createNewItem((short) 1547, 1);
                    item4.itemOptions.add(new ItemOption(7, 200000));
                    item4.itemOptions.add(new ItemOption(101, 500));
                    item4.itemOptions.add(new ItemOption(107, 8));
                    item4.itemOptions.add(new ItemOption(30, 500));

                    Item item5 = ItemService.gI().createNewItem((short) 1548, 1);
                    item5.itemOptions.add(new ItemOption(14, 5));
                    item5.itemOptions.add(new ItemOption(101, 500));
                    item5.itemOptions.add(new ItemOption(107, 8));
                    item5.itemOptions.add(new ItemOption(30, 500));

                    InventoryService.gI().addItemBag(player, item1, 99999999999l);
                    InventoryService.gI().addItemBag(player, item2, 99999999999l);
                    InventoryService.gI().addItemBag(player, item3, 99999999999l);
                    InventoryService.gI().addItemBag(player, item4, 99999999999l);
                    InventoryService.gI().addItemBag(player, item5, 99999999999l);
                    InventoryService.gI().sendItemBags(player);
                    Service.gI().sendThongBaoFromAdmin(player, "Chúc Mừng Bạn Đã Làm Nhiệm Vụ Tốt lắm\nBạn Đã Nhận Được 1Sét Tiềm Năng Vip ");
                    break;
                case ConstTask.TASK_22_0:
                    if (isCurrentTask(player, idTaskCustom)) {
                        Service.gI().sendThongBao(player, "Đã Ok");
                    }
                    break;
                case ConstTask.TASK_22_1:
                    if (isCurrentTask(player, idTaskCustom)) {
                        Service.gI().sendThongBao(player, "Đã Ok");
                    }
                    break;
                case ConstTask.TASK_22_2:
                    if (isCurrentTask(player, idTaskCustom)) {
                        Service.gI().sendThongBao(player, "Đã Ok");
                    }
                    break;
                case ConstTask.TASK_22_3:
                    npcSay(player, ConstTask.NPC_NHA,
                            "OK");

                    Item item6 = ItemService.gI().createNewItem((short) 1528, 99);
                    item6.itemOptions.add(new ItemOption(50, 5));
                    item6.itemOptions.add(new ItemOption(30, 500));

                    Item item7 = ItemService.gI().createNewItem((short) 1529, 99);
                    item7.itemOptions.add(new ItemOption(77, 5));
                    item7.itemOptions.add(new ItemOption(30, 500));

                    Item item8 = ItemService.gI().createNewItem((short) 1530, 99);
                    item8.itemOptions.add(new ItemOption(103, 5));
                    item8.itemOptions.add(new ItemOption(30, 500));

                    Item item9 = ItemService.gI().createNewItem((short) 1531, 99);
                    item9.itemOptions.add(new ItemOption(101, 5));
                    item9.itemOptions.add(new ItemOption(30, 500));

                    InventoryService.gI().addItemBag(player, item6, 99999999999l);
                    InventoryService.gI().addItemBag(player, item7, 99999999999l);
                    InventoryService.gI().addItemBag(player, item8, 99999999999l);
                    InventoryService.gI().addItemBag(player, item9, 99999999999l);
                    InventoryService.gI().sendItemBags(player);
                    Service.gI().sendThongBaoFromAdmin(player, "Chúc Mừng Bạn Đã Làm Nhiệm Vụ Tốt lắm\nBạn Đã Nhận quà nv mở ra để xem ");
                    break;

                case ConstTask.TASK_23_0:
                    if (isCurrentTask(player, idTaskCustom)) {
                        Service.gI().sendThongBao(player, "Đã Ok");
                    }
                    break;
                case ConstTask.TASK_23_1:
                    if (isCurrentTask(player, idTaskCustom)) {
                        Service.gI().sendThongBao(player, "Đã Ok");
                    }
                    break;
                case ConstTask.TASK_23_2:
                    npcSay(player, ConstTask.NPC_NHA,
                            "OK");

                    Item item10 = ItemService.gI().createNewItem((short) 1111, 10000);
                    item10.itemOptions.add(new ItemOption(30, 500));

                    Item item11 = ItemService.gI().createNewItem((short) 457, 10000);
                    item11.itemOptions.add(new ItemOption(30, 500));
                    InventoryService.gI().addItemBag(player, item10, 99999999999l);
                    InventoryService.gI().addItemBag(player, item10, 99999999999l);

                    InventoryService.gI().sendItemBags(player);
                    Service.gI().sendThongBaoFromAdmin(player, "Chúc Mừng Bạn Đã Làm Nhiệm Vụ Tốt lắm\nBạn Đã Nhận quà nv mở ra để xem ");
                    break;
                case ConstTask.TASK_24_0:
                    if (isCurrentTask(player, idTaskCustom)) {
                        Service.gI().sendThongBao(player, "Đã Ok");
                    }
                    break;

                case ConstTask.TASK_24_1:
                    npcSay(player, ConstTask.NPC_NHA,
                            "OK");

                    Item item12 = ItemService.gI().createNewItem((short) 77, 10000);
                    item12.itemOptions.add(new ItemOption(30, 500));

                    Item item13 = ItemService.gI().createNewItem((short) 861, 10000);
                    item13.itemOptions.add(new ItemOption(30, 500));

                    player.diemfam += 10000;

                    InventoryService.gI().addItemBag(player, item12, 99999999999l);
                    InventoryService.gI().addItemBag(player, item13, 99999999999l);

                    InventoryService.gI().sendItemBags(player);
                    Service.gI().sendThongBaoFromAdmin(player, "Chúc Mừng Bạn Đã Làm Nhiệm Vụ Tốt lắm\nBạn Đã Nhận quà nv mở ra để xem ");
                    break;

                case ConstTask.TASK_25_0:
                    if (isCurrentTask(player, idTaskCustom)) {
                        Service.gI().sendThongBao(player, "Bạn Đã Tiêu Diệt "
                                + player.playerTask.taskMain.subTasks.get(player.playerTask.taskMain.index).count + "/"
                                + player.playerTask.taskMain.subTasks.get(player.playerTask.taskMain.index).maxCount + " quái");
                    }
                    break;

                case ConstTask.TASK_25_1:
                    if (isCurrentTask(player, idTaskCustom)) {
                        Service.gI().sendThongBao(player, "Bạn Đã Tiêu Diệt "
                                + player.playerTask.taskMain.subTasks.get(player.playerTask.taskMain.index).count + "/"
                                + player.playerTask.taskMain.subTasks.get(player.playerTask.taskMain.index).maxCount + " quái");
                    }
                    break;
                case ConstTask.TASK_25_2:
                    npcSay(player, ConstTask.NPC_NHA,
                            "OK");

                    Item item14 = ItemService.gI().createNewItem((short) 1503, 500);
                    item14.itemOptions.add(new ItemOption(30, 500));

                    Item item15 = ItemService.gI().createNewItem((short) 1504, 500);
                    item15.itemOptions.add(new ItemOption(30, 500));
                    InventoryService.gI().addItemBag(player, item14, 99999999999l);
                    InventoryService.gI().addItemBag(player, item15, 99999999999l);

                    InventoryService.gI().sendItemBags(player);
                    Service.gI().sendThongBaoFromAdmin(player, "Chúc Mừng Bạn Đã Làm Nhiệm Vụ Tốt lắm\nBạn Đã Nhận quà nv mở ra để xem ");
                    break;

                case ConstTask.TASK_26_0:
                    if (isCurrentTask(player, idTaskCustom)) {
                        Service.gI().sendThongBao(player, "Đã Ok");
                    }
                    break;
                case ConstTask.TASK_26_1:
                    if (isCurrentTask(player, idTaskCustom)) {
                        Service.gI().sendThongBao(player, "Đã Ok");
                    }
                    break;
                case ConstTask.TASK_26_2:
                    if (isCurrentTask(player, idTaskCustom)) {
                        Service.gI().sendThongBao(player, "Đã Ok");
                    }
                    break;
                case ConstTask.TASK_26_3:
                    if (isCurrentTask(player, idTaskCustom)) {
                        Service.gI().sendThongBao(player, "Đã Ok");
                    }
                    break;
                case ConstTask.TASK_26_4:
                    if (isCurrentTask(player, idTaskCustom)) {
                        Service.gI().sendThongBao(player, "Đã Ok");
                    }
                    break;

                case ConstTask.TASK_26_5:
                    npcSay(player, ConstTask.NPC_NHA,
                            "OK");

                    Item item16 = ItemService.gI().createNewItem((short) 421, 1);
                    item16.itemOptions.add(new ItemOption(50, 500));
                    item16.itemOptions.add(new ItemOption(77, 500));
                    item16.itemOptions.add(new ItemOption(103, 500));
                    item16.itemOptions.add(new ItemOption(5, 10));
                      item16.itemOptions.add(new ItemOption(107, 8));
                    item16.itemOptions.add(new ItemOption(30, 500));
                    InventoryService.gI().addItemBag(player, item16, 99999999999l);

                    InventoryService.gI().sendItemBags(player);
                    Service.gI().sendThongBaoFromAdmin(player, "Chúc Mừng Bạn Đã Làm Nhiệm Vụ Tốt lắm\nBạn Đã Nhận quà nv mở ra để xem ");
                    break;
                    
                   case ConstTask.TASK_27_0:
                    if (isCurrentTask(player, idTaskCustom)) {
                        Service.gI().sendThongBao(player, "Bạn Đã Tiêu Diệt "
                                + player.playerTask.taskMain.subTasks.get(player.playerTask.taskMain.index).count + "/"
                                + player.playerTask.taskMain.subTasks.get(player.playerTask.taskMain.index).maxCount + " quái");
                    }
                    break;
                     case ConstTask.TASK_27_1:
                    npcSay(player, ConstTask.NPC_NHA,
                            "OK");

                    Item item17 = ItemService.gI().createNewItem((short) 77, 1);
                    item17.itemOptions.add(new ItemOption(30, 500));
                    player.CapTamkjll +=50;
                    InventoryService.gI().addItemBag(player, item17, 99999999999l);
                    InventoryService.gI().sendItemBags(player);
                    Service.gI().sendThongBaoFromAdmin(player, "Chúc Mừng Bạn Đã Làm Nhiệm Vụ Tốt lắm\nBạn Đã Nhận quà nv mở ra để xem ");
                    break;  
                  
                   case ConstTask.TASK_28_0:
                    if (isCurrentTask(player, idTaskCustom)) {
                        Service.gI().sendThongBao(player, "Đã Ok");
                    }
                    break;    
                   case ConstTask.TASK_28_1:
                    if (isCurrentTask(player, idTaskCustom)) {
                        Service.gI().sendThongBao(player, "Đã Ok");
                    }
                    break;     
                   case ConstTask.TASK_28_2:
                    if (isCurrentTask(player, idTaskCustom)) {
                        Service.gI().sendThongBao(player, "Đã Ok");
                    }
                    break;     
                    case ConstTask.TASK_28_3:
                    if (isCurrentTask(player, idTaskCustom)) {
                        Service.gI().sendThongBao(player, "Đã Ok");
                    }
                    break;    
                   case ConstTask.TASK_28_4:
                    if (isCurrentTask(player, idTaskCustom)) {
                        Service.gI().sendThongBao(player, "Đã Ok");
                    }
                    break;  
                    
                    
                    
                    case ConstTask.TASK_28_5:
                    npcSay(player, ConstTask.NPC_NHA,
                            "OK");

                    Item item18 = ItemService.gI().createNewItem((short) 1727, 1);
                    item18.itemOptions.add(new ItemOption(50, 500));
                    item18.itemOptions.add(new ItemOption(77, 500));
                    item18.itemOptions.add(new ItemOption(103, 500));
                    item18.itemOptions.add(new ItemOption(181, 10));
                      item18.itemOptions.add(new ItemOption(107, 8));
                    item18.itemOptions.add(new ItemOption(30, 500));
                    InventoryService.gI().addItemBag(player, item18, 99999999999l);
                    InventoryService.gI().sendItemBags(player);
                    Service.gI().sendThongBaoFromAdmin(player, "Chúc Mừng Bạn Đã Làm Nhiệm Vụ Tốt lắm\nBạn Đã Nhận quà nv mở ra để xem ");
                    break;  
//                      case ConstTask.TASK_29_0:
//                    break;    
                    
                   case ConstTask.TASK_29_0:
                    if (isCurrentTask(player, idTaskCustom)) {
                        Service.gI().sendThongBao(player, "Đã Ok");
                    }
                    break;    
                    case ConstTask.TASK_29_1:
                    if (isCurrentTask(player, idTaskCustom)) {
                        Service.gI().sendThongBao(player, "Đã Ok");
                    }
                    break;      
                     case ConstTask.TASK_29_2:
                    if (isCurrentTask(player, idTaskCustom)) {
                        Service.gI().sendThongBao(player, "Đã Ok");
                    }
                    break;     
                      case ConstTask.TASK_29_3:
                    if (isCurrentTask(player, idTaskCustom)) {
                        Service.gI().sendThongBao(player, "Đã Ok");
                    }
                    break;    
                      case ConstTask.TASK_29_4:
                    if (isCurrentTask(player, idTaskCustom)) {
                        Service.gI().sendThongBao(player, "Đã Ok");
                    }
                    break;    
                     case ConstTask.TASK_29_5:
                    npcSay(player, ConstTask.NPC_NHA,
                            "OK");

                    Item item19 = ItemService.gI().createNewItem((short) 549, 1);
                    item19.itemOptions.add(new ItemOption(50, 1000));
                    item19.itemOptions.add(new ItemOption(77, 1000));
                    item19.itemOptions.add(new ItemOption(103, 1000));
                    item19.itemOptions.add(new ItemOption(5, 10));
                      item19.itemOptions.add(new ItemOption(107, 18));
                    item19.itemOptions.add(new ItemOption(30, 500));
                    InventoryService.gI().addItemBag(player, item19, 99999999999l);
                    InventoryService.gI().sendItemBags(player);
                    Service.gI().sendThongBaoFromAdmin(player, "Chúc Mừng Bạn Đã Làm Nhiệm Vụ Tốt lắm\nBạn Đã Nhận quà nv mở ra để xem ");
                    break;    
                     case ConstTask.TASK_30_0:
                   if (isCurrentTask(player, idTaskCustom)) {
                        Service.gI().sendThongBao(player, "Đã Ok");
                    }
                    break;    
                    case ConstTask.TASK_30_1:
                    npcSay(player, ConstTask.NPC_NHA,
                            "OK");
                     Item item20 = ItemService.gI().createNewItem((short) 1918, 1);
                     item20.itemOptions.add(new ItemOption(197, 1200));
                    item20.itemOptions.add(new ItemOption(50, 1200));
                    item20.itemOptions.add(new ItemOption(77, 1200));
                    item20.itemOptions.add(new ItemOption(103, 1200));
                    item20.itemOptions.add(new ItemOption(14, 10));
                      item20.itemOptions.add(new ItemOption(107, 18));
                    item20.itemOptions.add(new ItemOption(30, 500));
                    InventoryService.gI().addItemBag(player, item20, 99999999999l);
                    InventoryService.gI().sendItemBags(player);
                    Service.gI().sendThongBaoFromAdmin(player, "Chúc Mừng Bạn Đã Làm Nhiệm Vụ Tốt lắm\nBạn Đã Nhận quà nv mở ra để xem ");
                    break;    
                    
                    
                    
                    
                      case ConstTask.TASK_31_0:
                   if (isCurrentTask(player, idTaskCustom)) {
                        Service.gI().sendThongBao(player, "Đã Ok");
                    }
                    break;  
                    
                    
                    

            }
            InventoryService.gI().sendItemBags(player);
            return true;
        }
        return false;
    }

    private void npcSay(Player player, int npcId, String text) {
        npcId = transformNpcId(player, npcId);
        text = transformName(player, text);
        int avatar = NpcService.gI().getAvatar(npcId);
        NpcService.gI().createTutorial(player, avatar, text);
    }

    //Thưởng nhiệm vụ
    private void rewardDoneTask(Player player) {
        switch (player.playerTask.taskMain.id) {
            case 0:
                Service.getInstance().addSMTN(player, (byte) 0, 500, false);
                Service.getInstance().addSMTN(player, (byte) 1, 500, false);
                break;
            case 1:
                Service.getInstance().addSMTN(player, (byte) 0, 1000, false);
                Service.getInstance().addSMTN(player, (byte) 1, 1000, false);
                break;
            case 2:
                Service.getInstance().addSMTN(player, (byte) 0, 1200, false);
                Service.getInstance().addSMTN(player, (byte) 1, 1200, false);
                break;
            case 3:
                Service.getInstance().addSMTN(player, (byte) 0, 3000, false);
                Service.getInstance().addSMTN(player, (byte) 1, 3000, false);
                break;
            case 4:
                Service.getInstance().addSMTN(player, (byte) 0, 7000, false);
                Service.getInstance().addSMTN(player, (byte) 1, 7000, false);
                break;
            case 5:
                Service.getInstance().addSMTN(player, (byte) 0, 20000, false);
                Service.getInstance().addSMTN(player, (byte) 1, 20000, false);
                break;
        }
    }

    // vd: pem đc 1 mộc nhân -> +1 mộc nhân vào nv hiện tại
    private void addDoneSubTask(Player player, int numDone) {
        player.playerTask.taskMain.subTasks.get(player.playerTask.taskMain.index).count += numDone;
        if (player.playerTask.taskMain.subTasks.get(player.playerTask.taskMain.index).count
                >= player.playerTask.taskMain.subTasks.get(player.playerTask.taskMain.index).maxCount) {
            player.playerTask.taskMain.index++;
            if (player.playerTask.taskMain.index >= player.playerTask.taskMain.subTasks.size()) {
                this.sendNextTaskMain(player);
            } else {
                this.sendNextSubTask(player);
            }
        } else {
            this.sendUpdateCountSubTask(player);
        }
    }

    private int transformMapId(Player player, int id) {
        if (id == ConstTask.MAP_NHA) {
            return (short) (player.gender + 21);
        } else if (id == ConstTask.MAP_200) {
            return player.gender == ConstPlayer.TRAI_DAT
                    ? 1 : (player.gender == ConstPlayer.NAMEC
                            ? 8 : 15);
        } else if (id == ConstTask.MAP_VACH_NUI) {
            return player.gender == ConstPlayer.TRAI_DAT
                    ? 39 : (player.gender == ConstPlayer.NAMEC
                            ? 40 : 41);
        } else if (id == ConstTask.MAP_200) {
            return player.gender == ConstPlayer.TRAI_DAT
                    ? 2 : (player.gender == ConstPlayer.NAMEC
                            ? 9 : 16);
        } else if (id == ConstTask.MAP_TTVT) {
            return player.gender == ConstPlayer.TRAI_DAT
                    ? 24 : (player.gender == ConstPlayer.NAMEC
                            ? 25 : 26);
        } else if (id == ConstTask.MAP_QUAI_BAY_600) {
            return player.gender == ConstPlayer.TRAI_DAT
                    ? 3 : (player.gender == ConstPlayer.NAMEC
                            ? 11 : 17);
        } else if (id == ConstTask.MAP_LANG) {
            return player.gender == ConstPlayer.TRAI_DAT
                    ? 0 : (player.gender == ConstPlayer.NAMEC
                            ? 7 : 14);
        } else if (id == ConstTask.MAP_QUY_LAO) {
            return player.gender == ConstPlayer.TRAI_DAT
                    ? 5 : (player.gender == ConstPlayer.NAMEC
                            ? 13 : 20);
        }
        return id;
    }

    private int transformNpcId(Player player, int id) {
        if (id == ConstTask.NPC_NHA) {
            return player.gender == ConstPlayer.TRAI_DAT
                    ? ConstNpc.ONG_GOHAN : (player.gender == ConstPlayer.NAMEC
                            ? ConstNpc.ONG_MOORI : ConstNpc.ONG_PARAGUS);
        } else if (id == ConstTask.NPC_TTVT) {
            return player.gender == ConstPlayer.TRAI_DAT
                    ? ConstNpc.DR_DRIEF : (player.gender == ConstPlayer.NAMEC
                            ? ConstNpc.CARGO : ConstNpc.CUI);
        } else if (id == ConstTask.NPC_SHOP_LANG) {
            return player.gender == ConstPlayer.TRAI_DAT
                    ? ConstNpc.BUNMA : (player.gender == ConstPlayer.NAMEC
                            ? ConstNpc.DENDE : ConstNpc.APPULE);
        } else if (id == ConstTask.NPC_QUY_LAO) {
            return player.gender == ConstPlayer.TRAI_DAT
                    ? ConstNpc.QUY_LAO_KAME : (player.gender == ConstPlayer.NAMEC
                            ? ConstNpc.TRUONG_LAO_GURU : ConstNpc.VUA_VEGETA);
        }
        return id;
    }

    //replate %1 %2 -> chữ
    private String transformName(Player player, String text) {
        text = text.replaceAll(ConstTask.TEN_NPC_QUY_LAO, player.gender == ConstPlayer.TRAI_DAT
                ? "Quy Lão Kame" : (player.gender == ConstPlayer.NAMEC
                        ? "Trưởng lão Guru" : "Vua Vegeta"));
        text = text.replaceAll(ConstTask.TEN_MAP_QUY_LAO, player.gender == ConstPlayer.TRAI_DAT
                ? "Đảo Kamê" : (player.gender == ConstPlayer.NAMEC
                        ? "Đảo Guru" : "Vách núi đen"));
        text = text.replaceAll(ConstTask.TEN_QUAI_3000, player.gender == ConstPlayer.TRAI_DAT
                ? "ốc mượn hồn" : (player.gender == ConstPlayer.NAMEC
                        ? "ốc sên" : "heo Xayda mẹ"));
        //----------------------------------------------------------------------
        text = text.replaceAll(ConstTask.TEN_LANG, player.gender == ConstPlayer.TRAI_DAT
                ? "Làng Aru" : (player.gender == ConstPlayer.NAMEC
                        ? "Làng Mori" : "Làng Kakarot"));
        text = text.replaceAll(ConstTask.TEN_NPC_NHA, player.gender == ConstPlayer.TRAI_DAT
                ? "Ông Gôhan" : (player.gender == ConstPlayer.NAMEC
                        ? "Ông Moori" : "Ông Paragus"));
        text = text.replaceAll(ConstTask.TEN_QUAI_200, player.gender == ConstPlayer.TRAI_DAT
                ? "khủng long" : (player.gender == ConstPlayer.NAMEC
                        ? "lợn lòi" : "quỷ đất"));
        text = text.replaceAll(ConstTask.TEN_MAP_200, player.gender == ConstPlayer.TRAI_DAT
                ? "Đồi hoa cúc" : (player.gender == ConstPlayer.NAMEC
                        ? "Đồi nấm tím" : "Đồi hoang"));
        text = text.replaceAll(ConstTask.TEN_VACH_NUI, player.gender == ConstPlayer.TRAI_DAT
                ? "Vách núi Aru" : (player.gender == ConstPlayer.NAMEC
                        ? "Vách núi Moori" : "Vách núi Kakarot"));
        text = text.replaceAll(ConstTask.TEN_MAP_500, player.gender == ConstPlayer.TRAI_DAT
                ? "Thung lũng tre" : (player.gender == ConstPlayer.NAMEC
                        ? "Thị trấn Moori" : "Làng Plane"));
        text = text.replaceAll(ConstTask.TEN_NPC_TTVT, player.gender == ConstPlayer.TRAI_DAT
                ? "Dr. Brief" : (player.gender == ConstPlayer.NAMEC
                        ? "Cargo" : "Cui"));
        text = text.replaceAll(ConstTask.TEN_QUAI_BAY_600, player.gender == ConstPlayer.TRAI_DAT
                ? "thằn lằn bay" : (player.gender == ConstPlayer.NAMEC
                        ? "phi long" : "quỷ bay"));
        text = text.replaceAll(ConstTask.TEN_NPC_SHOP_LANG, player.gender == ConstPlayer.TRAI_DAT
                ? "Bunma" : (player.gender == ConstPlayer.NAMEC
                        ? "Dende" : "Appule"));
        return text;
    }

    public boolean isCurrentTask(Player player, int idTaskCustom) {
        switch (idTaskCustom) {
            case ConstTask.TASK_0_0:
                return player.playerTask.taskMain.id == 0 && player.playerTask.taskMain.index == 0;
            case ConstTask.TASK_0_1:
                return player.playerTask.taskMain.id == 0 && player.playerTask.taskMain.index == 1;
            case ConstTask.TASK_0_2:
                return player.playerTask.taskMain.id == 0 && player.playerTask.taskMain.index == 2;
            case ConstTask.TASK_0_3:
                return player.playerTask.taskMain.id == 0 && player.playerTask.taskMain.index == 3;
            case ConstTask.TASK_0_4:
                return player.playerTask.taskMain.id == 0 && player.playerTask.taskMain.index == 4;
            case ConstTask.TASK_0_5:
                return player.playerTask.taskMain.id == 0 && player.playerTask.taskMain.index == 5;
            case ConstTask.TASK_0_6:
                return player.playerTask.taskMain.id == 0 && player.playerTask.taskMain.index == 6;
            case ConstTask.TASK_1_0:
                return player.playerTask.taskMain.id == 1 && player.playerTask.taskMain.index == 0;
            case ConstTask.TASK_1_1:
                return player.playerTask.taskMain.id == 1 && player.playerTask.taskMain.index == 1;
            case ConstTask.TASK_1_2:
                return player.playerTask.taskMain.id == 1 && player.playerTask.taskMain.index == 2;
            case ConstTask.TASK_1_3:
                return player.playerTask.taskMain.id == 1 && player.playerTask.taskMain.index == 3;
            case ConstTask.TASK_1_4:
                return player.playerTask.taskMain.id == 1 && player.playerTask.taskMain.index == 4;
            case ConstTask.TASK_1_5:
                return player.playerTask.taskMain.id == 1 && player.playerTask.taskMain.index == 5;
            case ConstTask.TASK_1_6:
                return player.playerTask.taskMain.id == 1 && player.playerTask.taskMain.index == 6;
            case ConstTask.TASK_2_0:
                return player.playerTask.taskMain.id == 2 && player.playerTask.taskMain.index == 0;
            case ConstTask.TASK_2_1:
                return player.playerTask.taskMain.id == 2 && player.playerTask.taskMain.index == 1;
            case ConstTask.TASK_2_2:
                return player.playerTask.taskMain.id == 2 && player.playerTask.taskMain.index == 2;
            case ConstTask.TASK_2_3:
                return player.playerTask.taskMain.id == 2 && player.playerTask.taskMain.index == 3;
            case ConstTask.TASK_2_4:
                return player.playerTask.taskMain.id == 2 && player.playerTask.taskMain.index == 4;
            case ConstTask.TASK_2_5:
                return player.playerTask.taskMain.id == 2 && player.playerTask.taskMain.index == 5;
            case ConstTask.TASK_2_6:
                return player.playerTask.taskMain.id == 2 && player.playerTask.taskMain.index == 6;
            case ConstTask.TASK_3_0:
                return player.playerTask.taskMain.id == 3 && player.playerTask.taskMain.index == 0;
            case ConstTask.TASK_3_1:
                return player.playerTask.taskMain.id == 3 && player.playerTask.taskMain.index == 1;
            case ConstTask.TASK_3_2:
                return player.playerTask.taskMain.id == 3 && player.playerTask.taskMain.index == 2;
            case ConstTask.TASK_3_3:
                return player.playerTask.taskMain.id == 3 && player.playerTask.taskMain.index == 3;
            case ConstTask.TASK_3_4:
                return player.playerTask.taskMain.id == 3 && player.playerTask.taskMain.index == 4;
            case ConstTask.TASK_3_5:
                return player.playerTask.taskMain.id == 3 && player.playerTask.taskMain.index == 5;
            case ConstTask.TASK_3_6:
                return player.playerTask.taskMain.id == 3 && player.playerTask.taskMain.index == 6;
            case ConstTask.TASK_4_0:
                return player.playerTask.taskMain.id == 4 && player.playerTask.taskMain.index == 0;
            case ConstTask.TASK_4_1:
                return player.playerTask.taskMain.id == 4 && player.playerTask.taskMain.index == 1;
            case ConstTask.TASK_4_2:
                return player.playerTask.taskMain.id == 4 && player.playerTask.taskMain.index == 2;
            case ConstTask.TASK_4_3:
                return player.playerTask.taskMain.id == 4 && player.playerTask.taskMain.index == 3;
            case ConstTask.TASK_4_4:
                return player.playerTask.taskMain.id == 4 && player.playerTask.taskMain.index == 4;
            case ConstTask.TASK_4_5:
                return player.playerTask.taskMain.id == 4 && player.playerTask.taskMain.index == 5;
            case ConstTask.TASK_4_6:
                return player.playerTask.taskMain.id == 4 && player.playerTask.taskMain.index == 6;
            case ConstTask.TASK_5_0:
                return player.playerTask.taskMain.id == 5 && player.playerTask.taskMain.index == 0;
            case ConstTask.TASK_5_1:
                return player.playerTask.taskMain.id == 5 && player.playerTask.taskMain.index == 1;
            case ConstTask.TASK_5_2:
                return player.playerTask.taskMain.id == 5 && player.playerTask.taskMain.index == 2;
            case ConstTask.TASK_5_3:
                return player.playerTask.taskMain.id == 5 && player.playerTask.taskMain.index == 3;
            case ConstTask.TASK_5_4:
                return player.playerTask.taskMain.id == 5 && player.playerTask.taskMain.index == 4;
            case ConstTask.TASK_5_5:
                return player.playerTask.taskMain.id == 5 && player.playerTask.taskMain.index == 5;
            case ConstTask.TASK_5_6:
                return player.playerTask.taskMain.id == 5 && player.playerTask.taskMain.index == 6;
            case ConstTask.TASK_6_0:
                return player.playerTask.taskMain.id == 6 && player.playerTask.taskMain.index == 0;
            case ConstTask.TASK_6_1:
                return player.playerTask.taskMain.id == 6 && player.playerTask.taskMain.index == 1;
            case ConstTask.TASK_6_2:
                return player.playerTask.taskMain.id == 6 && player.playerTask.taskMain.index == 2;
            case ConstTask.TASK_6_3:
                return player.playerTask.taskMain.id == 6 && player.playerTask.taskMain.index == 3;
            case ConstTask.TASK_6_4:
                return player.playerTask.taskMain.id == 6 && player.playerTask.taskMain.index == 4;
            case ConstTask.TASK_6_5:
                return player.playerTask.taskMain.id == 6 && player.playerTask.taskMain.index == 5;
            case ConstTask.TASK_6_6:
                return player.playerTask.taskMain.id == 6 && player.playerTask.taskMain.index == 6;
            case ConstTask.TASK_7_0:
                return player.playerTask.taskMain.id == 7 && player.playerTask.taskMain.index == 0;
            case ConstTask.TASK_7_1:
                return player.playerTask.taskMain.id == 7 && player.playerTask.taskMain.index == 1;
            case ConstTask.TASK_7_2:
                return player.playerTask.taskMain.id == 7 && player.playerTask.taskMain.index == 2;
            case ConstTask.TASK_7_3:
                return player.playerTask.taskMain.id == 7 && player.playerTask.taskMain.index == 3;
            case ConstTask.TASK_7_4:
                return player.playerTask.taskMain.id == 7 && player.playerTask.taskMain.index == 4;
            case ConstTask.TASK_7_5:
                return player.playerTask.taskMain.id == 7 && player.playerTask.taskMain.index == 5;
            case ConstTask.TASK_7_6:
                return player.playerTask.taskMain.id == 7 && player.playerTask.taskMain.index == 6;
            case ConstTask.TASK_8_0:
                return player.playerTask.taskMain.id == 8 && player.playerTask.taskMain.index == 0;
            case ConstTask.TASK_8_1:
                return player.playerTask.taskMain.id == 8 && player.playerTask.taskMain.index == 1;
            case ConstTask.TASK_8_2:
                return player.playerTask.taskMain.id == 8 && player.playerTask.taskMain.index == 2;
            case ConstTask.TASK_8_3:
                return player.playerTask.taskMain.id == 8 && player.playerTask.taskMain.index == 3;
            case ConstTask.TASK_8_4:
                return player.playerTask.taskMain.id == 8 && player.playerTask.taskMain.index == 4;
            case ConstTask.TASK_8_5:
                return player.playerTask.taskMain.id == 8 && player.playerTask.taskMain.index == 5;
            case ConstTask.TASK_8_6:
                return player.playerTask.taskMain.id == 8 && player.playerTask.taskMain.index == 6;
            case ConstTask.TASK_9_0:
                return player.playerTask.taskMain.id == 9 && player.playerTask.taskMain.index == 0;
            case ConstTask.TASK_9_1:
                return player.playerTask.taskMain.id == 9 && player.playerTask.taskMain.index == 1;
            case ConstTask.TASK_9_2:
                return player.playerTask.taskMain.id == 9 && player.playerTask.taskMain.index == 2;
            case ConstTask.TASK_9_3:
                return player.playerTask.taskMain.id == 9 && player.playerTask.taskMain.index == 3;
            case ConstTask.TASK_9_4:
                return player.playerTask.taskMain.id == 9 && player.playerTask.taskMain.index == 4;
            case ConstTask.TASK_9_5:
                return player.playerTask.taskMain.id == 9 && player.playerTask.taskMain.index == 5;
            case ConstTask.TASK_9_6:
                return player.playerTask.taskMain.id == 9 && player.playerTask.taskMain.index == 6;
            case ConstTask.TASK_10_0:
                return player.playerTask.taskMain.id == 10 && player.playerTask.taskMain.index == 0;
            case ConstTask.TASK_10_1:
                return player.playerTask.taskMain.id == 10 && player.playerTask.taskMain.index == 1;
            case ConstTask.TASK_10_2:
                return player.playerTask.taskMain.id == 10 && player.playerTask.taskMain.index == 2;
            case ConstTask.TASK_10_3:
                return player.playerTask.taskMain.id == 10 && player.playerTask.taskMain.index == 3;
            case ConstTask.TASK_10_4:
                return player.playerTask.taskMain.id == 10 && player.playerTask.taskMain.index == 4;
            case ConstTask.TASK_10_5:
                return player.playerTask.taskMain.id == 10 && player.playerTask.taskMain.index == 5;
            case ConstTask.TASK_10_6:
                return player.playerTask.taskMain.id == 10 && player.playerTask.taskMain.index == 6;
            case ConstTask.TASK_11_0:
                return player.playerTask.taskMain.id == 11 && player.playerTask.taskMain.index == 0;
            case ConstTask.TASK_11_1:
                return player.playerTask.taskMain.id == 11 && player.playerTask.taskMain.index == 1;
            case ConstTask.TASK_11_2:
                return player.playerTask.taskMain.id == 11 && player.playerTask.taskMain.index == 2;
            case ConstTask.TASK_11_3:
                return player.playerTask.taskMain.id == 11 && player.playerTask.taskMain.index == 3;
            case ConstTask.TASK_11_4:
                return player.playerTask.taskMain.id == 11 && player.playerTask.taskMain.index == 4;
            case ConstTask.TASK_11_5:
                return player.playerTask.taskMain.id == 11 && player.playerTask.taskMain.index == 5;
            case ConstTask.TASK_11_6:
                return player.playerTask.taskMain.id == 11 && player.playerTask.taskMain.index == 6;
            case ConstTask.TASK_12_0:
                return player.playerTask.taskMain.id == 12 && player.playerTask.taskMain.index == 0;
            case ConstTask.TASK_12_1:
                return player.playerTask.taskMain.id == 12 && player.playerTask.taskMain.index == 1;
            case ConstTask.TASK_12_2:
                return player.playerTask.taskMain.id == 12 && player.playerTask.taskMain.index == 2;
            case ConstTask.TASK_12_3:
                return player.playerTask.taskMain.id == 12 && player.playerTask.taskMain.index == 3;
            case ConstTask.TASK_12_4:
                return player.playerTask.taskMain.id == 12 && player.playerTask.taskMain.index == 4;
            case ConstTask.TASK_12_5:
                return player.playerTask.taskMain.id == 12 && player.playerTask.taskMain.index == 5;
            case ConstTask.TASK_12_6:
                return player.playerTask.taskMain.id == 12 && player.playerTask.taskMain.index == 6;
            case ConstTask.TASK_13_0:
                return player.playerTask.taskMain.id == 13 && player.playerTask.taskMain.index == 0;
            case ConstTask.TASK_13_1:
                return player.playerTask.taskMain.id == 13 && player.playerTask.taskMain.index == 1;
            case ConstTask.TASK_13_2:
                return player.playerTask.taskMain.id == 13 && player.playerTask.taskMain.index == 2;
            case ConstTask.TASK_13_3:
                return player.playerTask.taskMain.id == 13 && player.playerTask.taskMain.index == 3;
            case ConstTask.TASK_13_4:
                return player.playerTask.taskMain.id == 13 && player.playerTask.taskMain.index == 4;
            case ConstTask.TASK_13_5:
                return player.playerTask.taskMain.id == 13 && player.playerTask.taskMain.index == 5;
            case ConstTask.TASK_13_6:
                return player.playerTask.taskMain.id == 13 && player.playerTask.taskMain.index == 6;
            case ConstTask.TASK_14_0:
                return player.playerTask.taskMain.id == 14 && player.playerTask.taskMain.index == 0;
            case ConstTask.TASK_14_1:
                return player.playerTask.taskMain.id == 14 && player.playerTask.taskMain.index == 1;
            case ConstTask.TASK_14_2:
                return player.playerTask.taskMain.id == 14 && player.playerTask.taskMain.index == 2;
            case ConstTask.TASK_14_3:
                return player.playerTask.taskMain.id == 14 && player.playerTask.taskMain.index == 3;
            case ConstTask.TASK_14_4:
                return player.playerTask.taskMain.id == 14 && player.playerTask.taskMain.index == 4;
            case ConstTask.TASK_14_5:
                return player.playerTask.taskMain.id == 14 && player.playerTask.taskMain.index == 5;
            case ConstTask.TASK_14_6:
                return player.playerTask.taskMain.id == 14 && player.playerTask.taskMain.index == 6;
            case ConstTask.TASK_15_0:
                return player.playerTask.taskMain.id == 15 && player.playerTask.taskMain.index == 0;
            case ConstTask.TASK_15_1:
                return player.playerTask.taskMain.id == 15 && player.playerTask.taskMain.index == 1;
            case ConstTask.TASK_15_2:
                return player.playerTask.taskMain.id == 15 && player.playerTask.taskMain.index == 2;
            case ConstTask.TASK_15_3:
                return player.playerTask.taskMain.id == 15 && player.playerTask.taskMain.index == 3;
            case ConstTask.TASK_15_4:
                return player.playerTask.taskMain.id == 15 && player.playerTask.taskMain.index == 4;
            case ConstTask.TASK_15_5:
                return player.playerTask.taskMain.id == 15 && player.playerTask.taskMain.index == 5;
            case ConstTask.TASK_15_6:
                return player.playerTask.taskMain.id == 15 && player.playerTask.taskMain.index == 6;
            case ConstTask.TASK_16_0:
                return player.playerTask.taskMain.id == 16 && player.playerTask.taskMain.index == 0;
            case ConstTask.TASK_16_1:
                return player.playerTask.taskMain.id == 16 && player.playerTask.taskMain.index == 1;
            case ConstTask.TASK_16_2:
                return player.playerTask.taskMain.id == 16 && player.playerTask.taskMain.index == 2;
            case ConstTask.TASK_16_3:
                return player.playerTask.taskMain.id == 16 && player.playerTask.taskMain.index == 3;
            case ConstTask.TASK_16_4:
                return player.playerTask.taskMain.id == 16 && player.playerTask.taskMain.index == 4;
            case ConstTask.TASK_16_5:
                return player.playerTask.taskMain.id == 16 && player.playerTask.taskMain.index == 5;
            case ConstTask.TASK_16_6:
                return player.playerTask.taskMain.id == 16 && player.playerTask.taskMain.index == 6;
            case ConstTask.TASK_17_0:
                return player.playerTask.taskMain.id == 17 && player.playerTask.taskMain.index == 0;
            case ConstTask.TASK_17_1:
                return player.playerTask.taskMain.id == 17 && player.playerTask.taskMain.index == 1;
            case ConstTask.TASK_17_2:
                return player.playerTask.taskMain.id == 17 && player.playerTask.taskMain.index == 2;
            case ConstTask.TASK_17_3:
                return player.playerTask.taskMain.id == 17 && player.playerTask.taskMain.index == 3;
            case ConstTask.TASK_17_4:
                return player.playerTask.taskMain.id == 17 && player.playerTask.taskMain.index == 4;
            case ConstTask.TASK_17_5:
                return player.playerTask.taskMain.id == 17 && player.playerTask.taskMain.index == 5;
            case ConstTask.TASK_17_6:
                return player.playerTask.taskMain.id == 17 && player.playerTask.taskMain.index == 6;
            case ConstTask.TASK_18_0:
                return player.playerTask.taskMain.id == 18 && player.playerTask.taskMain.index == 0;
            case ConstTask.TASK_18_1:
                return player.playerTask.taskMain.id == 18 && player.playerTask.taskMain.index == 1;
            case ConstTask.TASK_18_2:
                return player.playerTask.taskMain.id == 18 && player.playerTask.taskMain.index == 2;
            case ConstTask.TASK_18_3:
                return player.playerTask.taskMain.id == 18 && player.playerTask.taskMain.index == 3;
            case ConstTask.TASK_18_4:
                return player.playerTask.taskMain.id == 18 && player.playerTask.taskMain.index == 4;
            case ConstTask.TASK_18_5:
                return player.playerTask.taskMain.id == 18 && player.playerTask.taskMain.index == 5;
            case ConstTask.TASK_18_6:
                return player.playerTask.taskMain.id == 18 && player.playerTask.taskMain.index == 6;
            case ConstTask.TASK_19_0:
                return player.playerTask.taskMain.id == 19 && player.playerTask.taskMain.index == 0;
            case ConstTask.TASK_19_1:
                return player.playerTask.taskMain.id == 19 && player.playerTask.taskMain.index == 1;
            case ConstTask.TASK_19_2:
                return player.playerTask.taskMain.id == 19 && player.playerTask.taskMain.index == 2;
            case ConstTask.TASK_19_3:
                return player.playerTask.taskMain.id == 19 && player.playerTask.taskMain.index == 3;
            case ConstTask.TASK_19_4:
                return player.playerTask.taskMain.id == 19 && player.playerTask.taskMain.index == 4;
            case ConstTask.TASK_19_5:
                return player.playerTask.taskMain.id == 19 && player.playerTask.taskMain.index == 5;
            case ConstTask.TASK_19_6:
                return player.playerTask.taskMain.id == 19 && player.playerTask.taskMain.index == 6;
            case ConstTask.TASK_20_0:
                return player.playerTask.taskMain.id == 20 && player.playerTask.taskMain.index == 0;
            case ConstTask.TASK_20_1:
                return player.playerTask.taskMain.id == 20 && player.playerTask.taskMain.index == 1;
            case ConstTask.TASK_20_2:
                return player.playerTask.taskMain.id == 20 && player.playerTask.taskMain.index == 2;
            case ConstTask.TASK_20_3:
                return player.playerTask.taskMain.id == 20 && player.playerTask.taskMain.index == 3;
            case ConstTask.TASK_20_4:
                return player.playerTask.taskMain.id == 20 && player.playerTask.taskMain.index == 4;
            case ConstTask.TASK_20_5:
                return player.playerTask.taskMain.id == 20 && player.playerTask.taskMain.index == 5;
            case ConstTask.TASK_20_6:
                return player.playerTask.taskMain.id == 20 && player.playerTask.taskMain.index == 6;
            case ConstTask.TASK_21_0:
                return player.playerTask.taskMain.id == 21 && player.playerTask.taskMain.index == 0;
            case ConstTask.TASK_21_1:
                return player.playerTask.taskMain.id == 21 && player.playerTask.taskMain.index == 1;
            case ConstTask.TASK_21_2:
                return player.playerTask.taskMain.id == 21 && player.playerTask.taskMain.index == 2;
            case ConstTask.TASK_21_3:
                return player.playerTask.taskMain.id == 21 && player.playerTask.taskMain.index == 3;
            case ConstTask.TASK_21_4:
                return player.playerTask.taskMain.id == 21 && player.playerTask.taskMain.index == 4;
            case ConstTask.TASK_21_5:
                return player.playerTask.taskMain.id == 21 && player.playerTask.taskMain.index == 5;
            case ConstTask.TASK_21_6:
                return player.playerTask.taskMain.id == 21 && player.playerTask.taskMain.index == 6;
            case ConstTask.TASK_22_0:
                return player.playerTask.taskMain.id == 22 && player.playerTask.taskMain.index == 0;
            case ConstTask.TASK_22_1:
                return player.playerTask.taskMain.id == 22 && player.playerTask.taskMain.index == 1;
            case ConstTask.TASK_22_2:
                return player.playerTask.taskMain.id == 22 && player.playerTask.taskMain.index == 2;
            case ConstTask.TASK_22_3:
                return player.playerTask.taskMain.id == 22 && player.playerTask.taskMain.index == 3;
            case ConstTask.TASK_22_4:
                return player.playerTask.taskMain.id == 22 && player.playerTask.taskMain.index == 4;
            case ConstTask.TASK_22_5:
                return player.playerTask.taskMain.id == 22 && player.playerTask.taskMain.index == 5;
            case ConstTask.TASK_22_6:
                return player.playerTask.taskMain.id == 22 && player.playerTask.taskMain.index == 6;
            case ConstTask.TASK_23_0:
                return player.playerTask.taskMain.id == 23 && player.playerTask.taskMain.index == 0;
            case ConstTask.TASK_23_1:
                return player.playerTask.taskMain.id == 23 && player.playerTask.taskMain.index == 1;
            case ConstTask.TASK_23_2:
                return player.playerTask.taskMain.id == 23 && player.playerTask.taskMain.index == 2;
            case ConstTask.TASK_23_3:
                return player.playerTask.taskMain.id == 23 && player.playerTask.taskMain.index == 3;
            case ConstTask.TASK_23_4:
                return player.playerTask.taskMain.id == 23 && player.playerTask.taskMain.index == 4;
            case ConstTask.TASK_23_5:
                return player.playerTask.taskMain.id == 23 && player.playerTask.taskMain.index == 5;
            case ConstTask.TASK_23_6:
                return player.playerTask.taskMain.id == 23 && player.playerTask.taskMain.index == 6;
            case ConstTask.TASK_24_0:
                return player.playerTask.taskMain.id == 24 && player.playerTask.taskMain.index == 0;
            case ConstTask.TASK_24_1:
                return player.playerTask.taskMain.id == 24 && player.playerTask.taskMain.index == 1;
            case ConstTask.TASK_24_2:
                return player.playerTask.taskMain.id == 24 && player.playerTask.taskMain.index == 2;
            case ConstTask.TASK_24_3:
                return player.playerTask.taskMain.id == 24 && player.playerTask.taskMain.index == 3;
            case ConstTask.TASK_24_4:
                return player.playerTask.taskMain.id == 24 && player.playerTask.taskMain.index == 4;
            case ConstTask.TASK_24_5:
                return player.playerTask.taskMain.id == 24 && player.playerTask.taskMain.index == 5;
            case ConstTask.TASK_24_6:
                return player.playerTask.taskMain.id == 24 && player.playerTask.taskMain.index == 6;
            case ConstTask.TASK_25_0:
                return player.playerTask.taskMain.id == 25 && player.playerTask.taskMain.index == 0;
            case ConstTask.TASK_25_1:
                return player.playerTask.taskMain.id == 25 && player.playerTask.taskMain.index == 1;
            case ConstTask.TASK_25_2:
                return player.playerTask.taskMain.id == 25 && player.playerTask.taskMain.index == 2;
            case ConstTask.TASK_25_3:
                return player.playerTask.taskMain.id == 25 && player.playerTask.taskMain.index == 3;
            case ConstTask.TASK_25_4:
                return player.playerTask.taskMain.id == 25 && player.playerTask.taskMain.index == 4;
            case ConstTask.TASK_25_5:
                return player.playerTask.taskMain.id == 25 && player.playerTask.taskMain.index == 5;
            case ConstTask.TASK_25_6:
                return player.playerTask.taskMain.id == 25 && player.playerTask.taskMain.index == 6;
            case ConstTask.TASK_26_0:
                return player.playerTask.taskMain.id == 26 && player.playerTask.taskMain.index == 0;
            case ConstTask.TASK_26_1:
                return player.playerTask.taskMain.id == 26 && player.playerTask.taskMain.index == 1;
            case ConstTask.TASK_26_2:
                return player.playerTask.taskMain.id == 26 && player.playerTask.taskMain.index == 2;
            case ConstTask.TASK_26_3:
                return player.playerTask.taskMain.id == 26 && player.playerTask.taskMain.index == 3;
            case ConstTask.TASK_26_4:
                return player.playerTask.taskMain.id == 26 && player.playerTask.taskMain.index == 4;
            case ConstTask.TASK_26_5:
                return player.playerTask.taskMain.id == 26 && player.playerTask.taskMain.index == 5;
            case ConstTask.TASK_26_6:
                return player.playerTask.taskMain.id == 26 && player.playerTask.taskMain.index == 6;
            case ConstTask.TASK_27_0:
                return player.playerTask.taskMain.id == 27 && player.playerTask.taskMain.index == 0;
            case ConstTask.TASK_27_1:
                return player.playerTask.taskMain.id == 27 && player.playerTask.taskMain.index == 1;
            case ConstTask.TASK_27_2:
                return player.playerTask.taskMain.id == 27 && player.playerTask.taskMain.index == 2;
            case ConstTask.TASK_27_3:
                return player.playerTask.taskMain.id == 27 && player.playerTask.taskMain.index == 3;
            case ConstTask.TASK_27_4:
                return player.playerTask.taskMain.id == 27 && player.playerTask.taskMain.index == 4;
            case ConstTask.TASK_27_5:
                return player.playerTask.taskMain.id == 27 && player.playerTask.taskMain.index == 5;
            case ConstTask.TASK_27_6:
                return player.playerTask.taskMain.id == 27 && player.playerTask.taskMain.index == 6;
            case ConstTask.TASK_28_0:
                return player.playerTask.taskMain.id == 28 && player.playerTask.taskMain.index == 0;
            case ConstTask.TASK_28_1:
                return player.playerTask.taskMain.id == 28 && player.playerTask.taskMain.index == 1;
            case ConstTask.TASK_28_2:
                return player.playerTask.taskMain.id == 28 && player.playerTask.taskMain.index == 2;
            case ConstTask.TASK_28_3:
                return player.playerTask.taskMain.id == 28 && player.playerTask.taskMain.index == 3;
            case ConstTask.TASK_28_4:
                return player.playerTask.taskMain.id == 28 && player.playerTask.taskMain.index == 4;
            case ConstTask.TASK_28_5:
                return player.playerTask.taskMain.id == 28 && player.playerTask.taskMain.index == 5;
            case ConstTask.TASK_28_6:
                return player.playerTask.taskMain.id == 28 && player.playerTask.taskMain.index == 6;
            case ConstTask.TASK_29_0:
                return player.playerTask.taskMain.id == 29 && player.playerTask.taskMain.index == 0;
            case ConstTask.TASK_29_1:
                return player.playerTask.taskMain.id == 29 && player.playerTask.taskMain.index == 1;
            case ConstTask.TASK_29_2:
                return player.playerTask.taskMain.id == 29 && player.playerTask.taskMain.index == 2;
            case ConstTask.TASK_29_3:
                return player.playerTask.taskMain.id == 29 && player.playerTask.taskMain.index == 3;
            case ConstTask.TASK_29_4:
                return player.playerTask.taskMain.id == 29 && player.playerTask.taskMain.index == 4;
            case ConstTask.TASK_29_5:
                return player.playerTask.taskMain.id == 29 && player.playerTask.taskMain.index == 5;
            case ConstTask.TASK_29_6:
                return player.playerTask.taskMain.id == 29 && player.playerTask.taskMain.index == 6;
            case ConstTask.TASK_30_0:
                return player.playerTask.taskMain.id == 30 && player.playerTask.taskMain.index == 0;
            case ConstTask.TASK_30_1:
                return player.playerTask.taskMain.id == 30 && player.playerTask.taskMain.index == 1;
            case ConstTask.TASK_30_2:
                return player.playerTask.taskMain.id == 30 && player.playerTask.taskMain.index == 2;
            case ConstTask.TASK_30_3:
                return player.playerTask.taskMain.id == 30 && player.playerTask.taskMain.index == 3;
            case ConstTask.TASK_30_4:
                return player.playerTask.taskMain.id == 30 && player.playerTask.taskMain.index == 4;
            case ConstTask.TASK_30_5:
                return player.playerTask.taskMain.id == 30 && player.playerTask.taskMain.index == 5;
            case ConstTask.TASK_30_6:
                return player.playerTask.taskMain.id == 30 && player.playerTask.taskMain.index == 6;
            case ConstTask.TASK_31_0:
                return player.playerTask.taskMain.id == 31 && player.playerTask.taskMain.index == 0;
            case ConstTask.TASK_31_1:
                return player.playerTask.taskMain.id == 31 && player.playerTask.taskMain.index == 1;
            case ConstTask.TASK_31_2:
                return player.playerTask.taskMain.id == 31 && player.playerTask.taskMain.index == 2;
            case ConstTask.TASK_31_3:
                return player.playerTask.taskMain.id == 31 && player.playerTask.taskMain.index == 3;
            case ConstTask.TASK_31_4:
                return player.playerTask.taskMain.id == 31 && player.playerTask.taskMain.index == 4;
            case ConstTask.TASK_31_5:
                return player.playerTask.taskMain.id == 31 && player.playerTask.taskMain.index == 5;
            case ConstTask.TASK_31_6:
                return player.playerTask.taskMain.id == 31 && player.playerTask.taskMain.index == 6;
            case ConstTask.TASK_32_0:
                return player.playerTask.taskMain.id == 32 && player.playerTask.taskMain.index == 0;
            case ConstTask.TASK_32_1:
                return player.playerTask.taskMain.id == 32 && player.playerTask.taskMain.index == 1;
            case ConstTask.TASK_32_2:
                return player.playerTask.taskMain.id == 32 && player.playerTask.taskMain.index == 2;
            case ConstTask.TASK_32_3:
                return player.playerTask.taskMain.id == 32 && player.playerTask.taskMain.index == 3;
            case ConstTask.TASK_32_4:
                return player.playerTask.taskMain.id == 32 && player.playerTask.taskMain.index == 4;
            case ConstTask.TASK_32_5:
                return player.playerTask.taskMain.id == 32 && player.playerTask.taskMain.index == 5;
            case ConstTask.TASK_32_6:
                return player.playerTask.taskMain.id == 32 && player.playerTask.taskMain.index == 6;
            case ConstTask.TASK_33_0:
                return player.playerTask.taskMain.id == 33 && player.playerTask.taskMain.index == 0;
            case ConstTask.TASK_33_1:
                return player.playerTask.taskMain.id == 33 && player.playerTask.taskMain.index == 1;
            case ConstTask.TASK_33_2:
                return player.playerTask.taskMain.id == 33 && player.playerTask.taskMain.index == 2;
            case ConstTask.TASK_33_3:
                return player.playerTask.taskMain.id == 33 && player.playerTask.taskMain.index == 3;
            case ConstTask.TASK_33_4:
                return player.playerTask.taskMain.id == 33 && player.playerTask.taskMain.index == 4;
            case ConstTask.TASK_33_5:
                return player.playerTask.taskMain.id == 33 && player.playerTask.taskMain.index == 5;
            case ConstTask.TASK_33_6:
                return player.playerTask.taskMain.id == 33 && player.playerTask.taskMain.index == 6;
            case ConstTask.TASK_34_0:
                return player.playerTask.taskMain.id == 34 && player.playerTask.taskMain.index == 0;
            case ConstTask.TASK_34_1:
                return player.playerTask.taskMain.id == 34 && player.playerTask.taskMain.index == 1;
            case ConstTask.TASK_34_2:
                return player.playerTask.taskMain.id == 34 && player.playerTask.taskMain.index == 2;
            case ConstTask.TASK_34_3:
                return player.playerTask.taskMain.id == 34 && player.playerTask.taskMain.index == 3;
            case ConstTask.TASK_34_4:
                return player.playerTask.taskMain.id == 34 && player.playerTask.taskMain.index == 4;
            case ConstTask.TASK_34_5:
                return player.playerTask.taskMain.id == 34 && player.playerTask.taskMain.index == 5;
            case ConstTask.TASK_34_6:
                return player.playerTask.taskMain.id == 34 && player.playerTask.taskMain.index == 6;
            case ConstTask.TASK_35_0:
                return player.playerTask.taskMain.id == 35 && player.playerTask.taskMain.index == 0;
            case ConstTask.TASK_35_1:
                return player.playerTask.taskMain.id == 35 && player.playerTask.taskMain.index == 1;
            case ConstTask.TASK_35_2:
                return player.playerTask.taskMain.id == 35 && player.playerTask.taskMain.index == 2;
            case ConstTask.TASK_35_3:
                return player.playerTask.taskMain.id == 35 && player.playerTask.taskMain.index == 3;
            case ConstTask.TASK_35_4:
                return player.playerTask.taskMain.id == 35 && player.playerTask.taskMain.index == 4;
            case ConstTask.TASK_35_5:
                return player.playerTask.taskMain.id == 35 && player.playerTask.taskMain.index == 5;
            case ConstTask.TASK_35_6:
                return player.playerTask.taskMain.id == 35 && player.playerTask.taskMain.index == 6;
            case ConstTask.TASK_36_0:
                return player.playerTask.taskMain.id == 36 && player.playerTask.taskMain.index == 0;
            case ConstTask.TASK_36_1:
                return player.playerTask.taskMain.id == 36 && player.playerTask.taskMain.index == 1;
            case ConstTask.TASK_36_2:
                return player.playerTask.taskMain.id == 36 && player.playerTask.taskMain.index == 2;
            case ConstTask.TASK_36_3:
                return player.playerTask.taskMain.id == 36 && player.playerTask.taskMain.index == 3;
            case ConstTask.TASK_36_4:
                return player.playerTask.taskMain.id == 36 && player.playerTask.taskMain.index == 4;
            case ConstTask.TASK_36_5:
                return player.playerTask.taskMain.id == 36 && player.playerTask.taskMain.index == 5;
            case ConstTask.TASK_36_6:
                return player.playerTask.taskMain.id == 36 && player.playerTask.taskMain.index == 6;
            case ConstTask.TASK_37_0:
                return player.playerTask.taskMain.id == 37 && player.playerTask.taskMain.index == 0;
            case ConstTask.TASK_37_1:
                return player.playerTask.taskMain.id == 37 && player.playerTask.taskMain.index == 1;
            case ConstTask.TASK_37_2:
                return player.playerTask.taskMain.id == 37 && player.playerTask.taskMain.index == 2;
            case ConstTask.TASK_37_3:
                return player.playerTask.taskMain.id == 37 && player.playerTask.taskMain.index == 3;
            case ConstTask.TASK_37_4:
                return player.playerTask.taskMain.id == 37 && player.playerTask.taskMain.index == 4;
            case ConstTask.TASK_37_5:
                return player.playerTask.taskMain.id == 37 && player.playerTask.taskMain.index == 5;
            case ConstTask.TASK_37_6:
                return player.playerTask.taskMain.id == 37 && player.playerTask.taskMain.index == 6;
            case ConstTask.TASK_38_0:
                return player.playerTask.taskMain.id == 38 && player.playerTask.taskMain.index == 0;
            case ConstTask.TASK_38_1:
                return player.playerTask.taskMain.id == 38 && player.playerTask.taskMain.index == 1;
            case ConstTask.TASK_38_2:
                return player.playerTask.taskMain.id == 38 && player.playerTask.taskMain.index == 2;
            case ConstTask.TASK_38_3:
                return player.playerTask.taskMain.id == 38 && player.playerTask.taskMain.index == 3;
            case ConstTask.TASK_38_4:
                return player.playerTask.taskMain.id == 38 && player.playerTask.taskMain.index == 4;
            case ConstTask.TASK_38_5:
                return player.playerTask.taskMain.id == 38 && player.playerTask.taskMain.index == 5;
            case ConstTask.TASK_38_6:
                return player.playerTask.taskMain.id == 38 && player.playerTask.taskMain.index == 6;
            case ConstTask.TASK_39_0:
                return player.playerTask.taskMain.id == 39 && player.playerTask.taskMain.index == 0;
            case ConstTask.TASK_39_1:
                return player.playerTask.taskMain.id == 39 && player.playerTask.taskMain.index == 1;
            case ConstTask.TASK_39_2:
                return player.playerTask.taskMain.id == 39 && player.playerTask.taskMain.index == 2;
            case ConstTask.TASK_39_3:
                return player.playerTask.taskMain.id == 39 && player.playerTask.taskMain.index == 3;
            case ConstTask.TASK_39_4:
                return player.playerTask.taskMain.id == 39 && player.playerTask.taskMain.index == 4;
            case ConstTask.TASK_39_5:
                return player.playerTask.taskMain.id == 39 && player.playerTask.taskMain.index == 5;
            case ConstTask.TASK_39_6:
                return player.playerTask.taskMain.id == 39 && player.playerTask.taskMain.index == 6;
            case ConstTask.TASK_40_0:
                return player.playerTask.taskMain.id == 40 && player.playerTask.taskMain.index == 0;
            case ConstTask.TASK_40_1:
                return player.playerTask.taskMain.id == 40 && player.playerTask.taskMain.index == 1;
            case ConstTask.TASK_40_2:
                return player.playerTask.taskMain.id == 40 && player.playerTask.taskMain.index == 2;
            case ConstTask.TASK_40_3:
                return player.playerTask.taskMain.id == 40 && player.playerTask.taskMain.index == 3;
            case ConstTask.TASK_40_4:
                return player.playerTask.taskMain.id == 40 && player.playerTask.taskMain.index == 4;
            case ConstTask.TASK_40_5:
                return player.playerTask.taskMain.id == 40 && player.playerTask.taskMain.index == 5;
            case ConstTask.TASK_40_6:
                return player.playerTask.taskMain.id == 40 && player.playerTask.taskMain.index == 6;
            case ConstTask.TASK_41_0:
                return player.playerTask.taskMain.id == 41 && player.playerTask.taskMain.index == 0;
            case ConstTask.TASK_41_1:
                return player.playerTask.taskMain.id == 41 && player.playerTask.taskMain.index == 1;
            case ConstTask.TASK_41_2:
                return player.playerTask.taskMain.id == 41 && player.playerTask.taskMain.index == 2;
            case ConstTask.TASK_41_3:
                return player.playerTask.taskMain.id == 41 && player.playerTask.taskMain.index == 3;
            case ConstTask.TASK_41_4:
                return player.playerTask.taskMain.id == 41 && player.playerTask.taskMain.index == 4;
            case ConstTask.TASK_41_5:
                return player.playerTask.taskMain.id == 41 && player.playerTask.taskMain.index == 5;
            case ConstTask.TASK_41_6:
                return player.playerTask.taskMain.id == 41 && player.playerTask.taskMain.index == 6;
            case ConstTask.TASK_42_0:
                return player.playerTask.taskMain.id == 42 && player.playerTask.taskMain.index == 0;
            case ConstTask.TASK_42_1:
                return player.playerTask.taskMain.id == 42 && player.playerTask.taskMain.index == 1;
            case ConstTask.TASK_42_2:
                return player.playerTask.taskMain.id == 42 && player.playerTask.taskMain.index == 2;
            case ConstTask.TASK_42_3:
                return player.playerTask.taskMain.id == 42 && player.playerTask.taskMain.index == 3;
            case ConstTask.TASK_42_4:
                return player.playerTask.taskMain.id == 42 && player.playerTask.taskMain.index == 4;
            case ConstTask.TASK_42_5:
                return player.playerTask.taskMain.id == 42 && player.playerTask.taskMain.index == 5;
            case ConstTask.TASK_42_6:
                return player.playerTask.taskMain.id == 42 && player.playerTask.taskMain.index == 6;
            case ConstTask.TASK_43_0:
                return player.playerTask.taskMain.id == 43 && player.playerTask.taskMain.index == 0;
            case ConstTask.TASK_43_1:
                return player.playerTask.taskMain.id == 43 && player.playerTask.taskMain.index == 1;
            case ConstTask.TASK_43_2:
                return player.playerTask.taskMain.id == 43 && player.playerTask.taskMain.index == 2;
            case ConstTask.TASK_43_3:
                return player.playerTask.taskMain.id == 43 && player.playerTask.taskMain.index == 3;
            case ConstTask.TASK_43_4:
                return player.playerTask.taskMain.id == 43 && player.playerTask.taskMain.index == 4;
            case ConstTask.TASK_43_5:
                return player.playerTask.taskMain.id == 43 && player.playerTask.taskMain.index == 5;
            case ConstTask.TASK_43_6:
                return player.playerTask.taskMain.id == 43 && player.playerTask.taskMain.index == 6;
            case ConstTask.TASK_44_0:
                return player.playerTask.taskMain.id == 44 && player.playerTask.taskMain.index == 0;
            case ConstTask.TASK_44_1:
                return player.playerTask.taskMain.id == 44 && player.playerTask.taskMain.index == 1;
            case ConstTask.TASK_44_2:
                return player.playerTask.taskMain.id == 44 && player.playerTask.taskMain.index == 2;
            case ConstTask.TASK_44_3:
                return player.playerTask.taskMain.id == 44 && player.playerTask.taskMain.index == 3;
            case ConstTask.TASK_44_4:
                return player.playerTask.taskMain.id == 44 && player.playerTask.taskMain.index == 4;
            case ConstTask.TASK_44_5:
                return player.playerTask.taskMain.id == 44 && player.playerTask.taskMain.index == 5;
            case ConstTask.TASK_44_6:
                return player.playerTask.taskMain.id == 44 && player.playerTask.taskMain.index == 6;
            case ConstTask.TASK_45_0:
                return player.playerTask.taskMain.id == 45 && player.playerTask.taskMain.index == 0;
            case ConstTask.TASK_45_1:
                return player.playerTask.taskMain.id == 45 && player.playerTask.taskMain.index == 1;
            case ConstTask.TASK_45_2:
                return player.playerTask.taskMain.id == 45 && player.playerTask.taskMain.index == 2;
            case ConstTask.TASK_45_3:
                return player.playerTask.taskMain.id == 45 && player.playerTask.taskMain.index == 3;
            case ConstTask.TASK_45_4:
                return player.playerTask.taskMain.id == 45 && player.playerTask.taskMain.index == 4;
            case ConstTask.TASK_45_5:
                return player.playerTask.taskMain.id == 45 && player.playerTask.taskMain.index == 5;
            case ConstTask.TASK_45_6:
                return player.playerTask.taskMain.id == 45 && player.playerTask.taskMain.index == 6;
            case ConstTask.TASK_46_0:
                return player.playerTask.taskMain.id == 46 && player.playerTask.taskMain.index == 0;
            case ConstTask.TASK_46_1:
                return player.playerTask.taskMain.id == 46 && player.playerTask.taskMain.index == 1;
            case ConstTask.TASK_46_2:
                return player.playerTask.taskMain.id == 46 && player.playerTask.taskMain.index == 2;
            case ConstTask.TASK_46_3:
                return player.playerTask.taskMain.id == 46 && player.playerTask.taskMain.index == 3;
            case ConstTask.TASK_46_4:
                return player.playerTask.taskMain.id == 46 && player.playerTask.taskMain.index == 4;
            case ConstTask.TASK_46_5:
                return player.playerTask.taskMain.id == 46 && player.playerTask.taskMain.index == 5;
            case ConstTask.TASK_46_6:
                return player.playerTask.taskMain.id == 46 && player.playerTask.taskMain.index == 6;
            case ConstTask.TASK_47_0:
                return player.playerTask.taskMain.id == 47 && player.playerTask.taskMain.index == 0;
            case ConstTask.TASK_47_1:
                return player.playerTask.taskMain.id == 47 && player.playerTask.taskMain.index == 1;
            case ConstTask.TASK_47_2:
                return player.playerTask.taskMain.id == 47 && player.playerTask.taskMain.index == 2;
            case ConstTask.TASK_47_3:
                return player.playerTask.taskMain.id == 47 && player.playerTask.taskMain.index == 3;
            case ConstTask.TASK_47_4:
                return player.playerTask.taskMain.id == 47 && player.playerTask.taskMain.index == 4;
            case ConstTask.TASK_47_5:
                return player.playerTask.taskMain.id == 47 && player.playerTask.taskMain.index == 5;
            case ConstTask.TASK_47_6:
                return player.playerTask.taskMain.id == 47 && player.playerTask.taskMain.index == 6;
            case ConstTask.TASK_48_0:
                return player.playerTask.taskMain.id == 48 && player.playerTask.taskMain.index == 0;
            case ConstTask.TASK_48_1:
                return player.playerTask.taskMain.id == 48 && player.playerTask.taskMain.index == 1;
            case ConstTask.TASK_48_2:
                return player.playerTask.taskMain.id == 48 && player.playerTask.taskMain.index == 2;
            case ConstTask.TASK_48_3:
                return player.playerTask.taskMain.id == 48 && player.playerTask.taskMain.index == 3;
            case ConstTask.TASK_48_4:
                return player.playerTask.taskMain.id == 48 && player.playerTask.taskMain.index == 4;
            case ConstTask.TASK_48_5:
                return player.playerTask.taskMain.id == 48 && player.playerTask.taskMain.index == 5;
            case ConstTask.TASK_48_6:
                return player.playerTask.taskMain.id == 48 && player.playerTask.taskMain.index == 6;
            case ConstTask.TASK_49_0:
                return player.playerTask.taskMain.id == 49 && player.playerTask.taskMain.index == 0;
            case ConstTask.TASK_49_1:
                return player.playerTask.taskMain.id == 49 && player.playerTask.taskMain.index == 1;
            case ConstTask.TASK_49_2:
                return player.playerTask.taskMain.id == 49 && player.playerTask.taskMain.index == 2;
            case ConstTask.TASK_49_3:
                return player.playerTask.taskMain.id == 49 && player.playerTask.taskMain.index == 3;
            case ConstTask.TASK_49_4:
                return player.playerTask.taskMain.id == 49 && player.playerTask.taskMain.index == 4;
            case ConstTask.TASK_49_5:
                return player.playerTask.taskMain.id == 49 && player.playerTask.taskMain.index == 5;
            case ConstTask.TASK_49_6:
                return player.playerTask.taskMain.id == 49 && player.playerTask.taskMain.index == 6;
            case ConstTask.TASK_50_0:
                return player.playerTask.taskMain.id == 50 && player.playerTask.taskMain.index == 0;
            case ConstTask.TASK_50_1:
                return player.playerTask.taskMain.id == 50 && player.playerTask.taskMain.index == 1;
            case ConstTask.TASK_50_2:
                return player.playerTask.taskMain.id == 50 && player.playerTask.taskMain.index == 2;
            case ConstTask.TASK_50_3:
                return player.playerTask.taskMain.id == 50 && player.playerTask.taskMain.index == 3;
            case ConstTask.TASK_50_4:
                return player.playerTask.taskMain.id == 50 && player.playerTask.taskMain.index == 4;
            case ConstTask.TASK_50_5:
                return player.playerTask.taskMain.id == 50 && player.playerTask.taskMain.index == 5;
            case ConstTask.TASK_50_6:
                return player.playerTask.taskMain.id == 50 && player.playerTask.taskMain.index == 6;
        }
        return false;
    }

    public int getIdTask(Player player) {
        if (player.isPet || player.isClone || player.isBoss || player.playerTask == null || player.playerTask.taskMain == null) {
            return -1;
        } else if (player.playerTask.taskMain.id == 0 && player.playerTask.taskMain.index == 0) {
            return ConstTask.TASK_0_0;
        } else if (player.playerTask.taskMain.id == 0 && player.playerTask.taskMain.index == 1) {
            return ConstTask.TASK_0_1;
        } else if (player.playerTask.taskMain.id == 0 && player.playerTask.taskMain.index == 2) {
            return ConstTask.TASK_0_2;
        } else if (player.playerTask.taskMain.id == 0 && player.playerTask.taskMain.index == 3) {
            return ConstTask.TASK_0_3;
        } else if (player.playerTask.taskMain.id == 0 && player.playerTask.taskMain.index == 4) {
            return ConstTask.TASK_0_4;
        } else if (player.playerTask.taskMain.id == 0 && player.playerTask.taskMain.index == 5) {
            return ConstTask.TASK_0_5;
        } else if (player.playerTask.taskMain.id == 0 && player.playerTask.taskMain.index == 6) {
            return ConstTask.TASK_0_6;
        } else if (player.playerTask.taskMain.id == 1 && player.playerTask.taskMain.index == 0) {
            return ConstTask.TASK_1_0;
        } else if (player.playerTask.taskMain.id == 1 && player.playerTask.taskMain.index == 1) {
            return ConstTask.TASK_1_1;
        } else if (player.playerTask.taskMain.id == 1 && player.playerTask.taskMain.index == 2) {
            return ConstTask.TASK_1_2;
        } else if (player.playerTask.taskMain.id == 1 && player.playerTask.taskMain.index == 3) {
            return ConstTask.TASK_1_3;
        } else if (player.playerTask.taskMain.id == 1 && player.playerTask.taskMain.index == 4) {
            return ConstTask.TASK_1_4;
        } else if (player.playerTask.taskMain.id == 1 && player.playerTask.taskMain.index == 5) {
            return ConstTask.TASK_1_5;
        } else if (player.playerTask.taskMain.id == 1 && player.playerTask.taskMain.index == 6) {
            return ConstTask.TASK_1_6;
        } else if (player.playerTask.taskMain.id == 2 && player.playerTask.taskMain.index == 0) {
            return ConstTask.TASK_2_0;
        } else if (player.playerTask.taskMain.id == 2 && player.playerTask.taskMain.index == 1) {
            return ConstTask.TASK_2_1;
        } else if (player.playerTask.taskMain.id == 2 && player.playerTask.taskMain.index == 2) {
            return ConstTask.TASK_2_2;
        } else if (player.playerTask.taskMain.id == 2 && player.playerTask.taskMain.index == 3) {
            return ConstTask.TASK_2_3;
        } else if (player.playerTask.taskMain.id == 2 && player.playerTask.taskMain.index == 4) {
            return ConstTask.TASK_2_4;
        } else if (player.playerTask.taskMain.id == 2 && player.playerTask.taskMain.index == 5) {
            return ConstTask.TASK_2_5;
        } else if (player.playerTask.taskMain.id == 2 && player.playerTask.taskMain.index == 6) {
            return ConstTask.TASK_2_6;
        } else if (player.playerTask.taskMain.id == 3 && player.playerTask.taskMain.index == 0) {
            return ConstTask.TASK_3_0;
        } else if (player.playerTask.taskMain.id == 3 && player.playerTask.taskMain.index == 1) {
            return ConstTask.TASK_3_1;
        } else if (player.playerTask.taskMain.id == 3 && player.playerTask.taskMain.index == 2) {
            return ConstTask.TASK_3_2;
        } else if (player.playerTask.taskMain.id == 3 && player.playerTask.taskMain.index == 3) {
            return ConstTask.TASK_3_3;
        } else if (player.playerTask.taskMain.id == 3 && player.playerTask.taskMain.index == 4) {
            return ConstTask.TASK_3_4;
        } else if (player.playerTask.taskMain.id == 3 && player.playerTask.taskMain.index == 5) {
            return ConstTask.TASK_3_5;
        } else if (player.playerTask.taskMain.id == 3 && player.playerTask.taskMain.index == 6) {
            return ConstTask.TASK_3_6;
        } else if (player.playerTask.taskMain.id == 4 && player.playerTask.taskMain.index == 0) {
            return ConstTask.TASK_4_0;
        } else if (player.playerTask.taskMain.id == 4 && player.playerTask.taskMain.index == 1) {
            return ConstTask.TASK_4_1;
        } else if (player.playerTask.taskMain.id == 4 && player.playerTask.taskMain.index == 2) {
            return ConstTask.TASK_4_2;
        } else if (player.playerTask.taskMain.id == 4 && player.playerTask.taskMain.index == 3) {
            return ConstTask.TASK_4_3;
        } else if (player.playerTask.taskMain.id == 4 && player.playerTask.taskMain.index == 4) {
            return ConstTask.TASK_4_4;
        } else if (player.playerTask.taskMain.id == 4 && player.playerTask.taskMain.index == 5) {
            return ConstTask.TASK_4_5;
        } else if (player.playerTask.taskMain.id == 4 && player.playerTask.taskMain.index == 6) {
            return ConstTask.TASK_4_6;
        } else if (player.playerTask.taskMain.id == 5 && player.playerTask.taskMain.index == 0) {
            return ConstTask.TASK_5_0;
        } else if (player.playerTask.taskMain.id == 5 && player.playerTask.taskMain.index == 1) {
            return ConstTask.TASK_5_1;
        } else if (player.playerTask.taskMain.id == 5 && player.playerTask.taskMain.index == 2) {
            return ConstTask.TASK_5_2;
        } else if (player.playerTask.taskMain.id == 5 && player.playerTask.taskMain.index == 3) {
            return ConstTask.TASK_5_3;
        } else if (player.playerTask.taskMain.id == 5 && player.playerTask.taskMain.index == 4) {
            return ConstTask.TASK_5_4;
        } else if (player.playerTask.taskMain.id == 5 && player.playerTask.taskMain.index == 5) {
            return ConstTask.TASK_5_5;
        } else if (player.playerTask.taskMain.id == 5 && player.playerTask.taskMain.index == 6) {
            return ConstTask.TASK_5_6;
        } else if (player.playerTask.taskMain.id == 6 && player.playerTask.taskMain.index == 0) {
            return ConstTask.TASK_6_0;
        } else if (player.playerTask.taskMain.id == 6 && player.playerTask.taskMain.index == 1) {
            return ConstTask.TASK_6_1;
        } else if (player.playerTask.taskMain.id == 6 && player.playerTask.taskMain.index == 2) {
            return ConstTask.TASK_6_2;
        } else if (player.playerTask.taskMain.id == 6 && player.playerTask.taskMain.index == 3) {
            return ConstTask.TASK_6_3;
        } else if (player.playerTask.taskMain.id == 6 && player.playerTask.taskMain.index == 4) {
            return ConstTask.TASK_6_4;
        } else if (player.playerTask.taskMain.id == 6 && player.playerTask.taskMain.index == 5) {
            return ConstTask.TASK_6_5;
        } else if (player.playerTask.taskMain.id == 6 && player.playerTask.taskMain.index == 6) {
            return ConstTask.TASK_6_6;
        } else if (player.playerTask.taskMain.id == 7 && player.playerTask.taskMain.index == 0) {
            return ConstTask.TASK_7_0;
        } else if (player.playerTask.taskMain.id == 7 && player.playerTask.taskMain.index == 1) {
            return ConstTask.TASK_7_1;
        } else if (player.playerTask.taskMain.id == 7 && player.playerTask.taskMain.index == 2) {
            return ConstTask.TASK_7_2;
        } else if (player.playerTask.taskMain.id == 7 && player.playerTask.taskMain.index == 3) {
            return ConstTask.TASK_7_3;
        } else if (player.playerTask.taskMain.id == 7 && player.playerTask.taskMain.index == 4) {
            return ConstTask.TASK_7_4;
        } else if (player.playerTask.taskMain.id == 7 && player.playerTask.taskMain.index == 5) {
            return ConstTask.TASK_7_5;
        } else if (player.playerTask.taskMain.id == 7 && player.playerTask.taskMain.index == 6) {
            return ConstTask.TASK_7_6;
        } else if (player.playerTask.taskMain.id == 8 && player.playerTask.taskMain.index == 0) {
            return ConstTask.TASK_8_0;
        } else if (player.playerTask.taskMain.id == 8 && player.playerTask.taskMain.index == 1) {
            return ConstTask.TASK_8_1;
        } else if (player.playerTask.taskMain.id == 8 && player.playerTask.taskMain.index == 2) {
            return ConstTask.TASK_8_2;
        } else if (player.playerTask.taskMain.id == 8 && player.playerTask.taskMain.index == 3) {
            return ConstTask.TASK_8_3;
        } else if (player.playerTask.taskMain.id == 8 && player.playerTask.taskMain.index == 4) {
            return ConstTask.TASK_8_4;
        } else if (player.playerTask.taskMain.id == 8 && player.playerTask.taskMain.index == 5) {
            return ConstTask.TASK_8_5;
        } else if (player.playerTask.taskMain.id == 8 && player.playerTask.taskMain.index == 6) {
            return ConstTask.TASK_8_6;
        } else if (player.playerTask.taskMain.id == 9 && player.playerTask.taskMain.index == 0) {
            return ConstTask.TASK_9_0;
        } else if (player.playerTask.taskMain.id == 9 && player.playerTask.taskMain.index == 1) {
            return ConstTask.TASK_9_1;
        } else if (player.playerTask.taskMain.id == 9 && player.playerTask.taskMain.index == 2) {
            return ConstTask.TASK_9_2;
        } else if (player.playerTask.taskMain.id == 9 && player.playerTask.taskMain.index == 3) {
            return ConstTask.TASK_9_3;
        } else if (player.playerTask.taskMain.id == 9 && player.playerTask.taskMain.index == 4) {
            return ConstTask.TASK_9_4;
        } else if (player.playerTask.taskMain.id == 9 && player.playerTask.taskMain.index == 5) {
            return ConstTask.TASK_9_5;
        } else if (player.playerTask.taskMain.id == 9 && player.playerTask.taskMain.index == 6) {
            return ConstTask.TASK_9_6;
        } else if (player.playerTask.taskMain.id == 10 && player.playerTask.taskMain.index == 0) {
            return ConstTask.TASK_10_0;
        } else if (player.playerTask.taskMain.id == 10 && player.playerTask.taskMain.index == 1) {
            return ConstTask.TASK_10_1;
        } else if (player.playerTask.taskMain.id == 10 && player.playerTask.taskMain.index == 2) {
            return ConstTask.TASK_10_2;
        } else if (player.playerTask.taskMain.id == 10 && player.playerTask.taskMain.index == 3) {
            return ConstTask.TASK_10_3;
        } else if (player.playerTask.taskMain.id == 10 && player.playerTask.taskMain.index == 4) {
            return ConstTask.TASK_10_4;
        } else if (player.playerTask.taskMain.id == 10 && player.playerTask.taskMain.index == 5) {
            return ConstTask.TASK_10_5;
        } else if (player.playerTask.taskMain.id == 10 && player.playerTask.taskMain.index == 6) {
            return ConstTask.TASK_10_6;
        } else if (player.playerTask.taskMain.id == 11 && player.playerTask.taskMain.index == 0) {
            return ConstTask.TASK_11_0;
        } else if (player.playerTask.taskMain.id == 11 && player.playerTask.taskMain.index == 1) {
            return ConstTask.TASK_11_1;
        } else if (player.playerTask.taskMain.id == 11 && player.playerTask.taskMain.index == 2) {
            return ConstTask.TASK_11_2;
        } else if (player.playerTask.taskMain.id == 11 && player.playerTask.taskMain.index == 3) {
            return ConstTask.TASK_11_3;
        } else if (player.playerTask.taskMain.id == 11 && player.playerTask.taskMain.index == 4) {
            return ConstTask.TASK_11_4;
        } else if (player.playerTask.taskMain.id == 11 && player.playerTask.taskMain.index == 5) {
            return ConstTask.TASK_11_5;
        } else if (player.playerTask.taskMain.id == 11 && player.playerTask.taskMain.index == 6) {
            return ConstTask.TASK_11_6;
        } else if (player.playerTask.taskMain.id == 12 && player.playerTask.taskMain.index == 0) {
            return ConstTask.TASK_12_0;
        } else if (player.playerTask.taskMain.id == 12 && player.playerTask.taskMain.index == 1) {
            return ConstTask.TASK_12_1;
        } else if (player.playerTask.taskMain.id == 12 && player.playerTask.taskMain.index == 2) {
            return ConstTask.TASK_12_2;
        } else if (player.playerTask.taskMain.id == 12 && player.playerTask.taskMain.index == 3) {
            return ConstTask.TASK_12_3;
        } else if (player.playerTask.taskMain.id == 12 && player.playerTask.taskMain.index == 4) {
            return ConstTask.TASK_12_4;
        } else if (player.playerTask.taskMain.id == 12 && player.playerTask.taskMain.index == 5) {
            return ConstTask.TASK_12_5;
        } else if (player.playerTask.taskMain.id == 12 && player.playerTask.taskMain.index == 6) {
            return ConstTask.TASK_12_6;
        } else if (player.playerTask.taskMain.id == 13 && player.playerTask.taskMain.index == 0) {
            return ConstTask.TASK_13_0;
        } else if (player.playerTask.taskMain.id == 13 && player.playerTask.taskMain.index == 1) {
            return ConstTask.TASK_13_1;
        } else if (player.playerTask.taskMain.id == 13 && player.playerTask.taskMain.index == 2) {
            return ConstTask.TASK_13_2;
        } else if (player.playerTask.taskMain.id == 13 && player.playerTask.taskMain.index == 3) {
            return ConstTask.TASK_13_3;
        } else if (player.playerTask.taskMain.id == 13 && player.playerTask.taskMain.index == 4) {
            return ConstTask.TASK_13_4;
        } else if (player.playerTask.taskMain.id == 13 && player.playerTask.taskMain.index == 5) {
            return ConstTask.TASK_13_5;
        } else if (player.playerTask.taskMain.id == 13 && player.playerTask.taskMain.index == 6) {
            return ConstTask.TASK_13_6;
        } else if (player.playerTask.taskMain.id == 14 && player.playerTask.taskMain.index == 0) {
            return ConstTask.TASK_14_0;
        } else if (player.playerTask.taskMain.id == 14 && player.playerTask.taskMain.index == 1) {
            return ConstTask.TASK_14_1;
        } else if (player.playerTask.taskMain.id == 14 && player.playerTask.taskMain.index == 2) {
            return ConstTask.TASK_14_2;
        } else if (player.playerTask.taskMain.id == 14 && player.playerTask.taskMain.index == 3) {
            return ConstTask.TASK_14_3;
        } else if (player.playerTask.taskMain.id == 14 && player.playerTask.taskMain.index == 4) {
            return ConstTask.TASK_14_4;
        } else if (player.playerTask.taskMain.id == 14 && player.playerTask.taskMain.index == 5) {
            return ConstTask.TASK_14_5;
        } else if (player.playerTask.taskMain.id == 14 && player.playerTask.taskMain.index == 6) {
            return ConstTask.TASK_14_6;
        } else if (player.playerTask.taskMain.id == 15 && player.playerTask.taskMain.index == 0) {
            return ConstTask.TASK_15_0;
        } else if (player.playerTask.taskMain.id == 15 && player.playerTask.taskMain.index == 1) {
            return ConstTask.TASK_15_1;
        } else if (player.playerTask.taskMain.id == 15 && player.playerTask.taskMain.index == 2) {
            return ConstTask.TASK_15_2;
        } else if (player.playerTask.taskMain.id == 15 && player.playerTask.taskMain.index == 3) {
            return ConstTask.TASK_15_3;
        } else if (player.playerTask.taskMain.id == 15 && player.playerTask.taskMain.index == 4) {
            return ConstTask.TASK_15_4;
        } else if (player.playerTask.taskMain.id == 15 && player.playerTask.taskMain.index == 5) {
            return ConstTask.TASK_15_5;
        } else if (player.playerTask.taskMain.id == 15 && player.playerTask.taskMain.index == 6) {
            return ConstTask.TASK_15_6;
        } else if (player.playerTask.taskMain.id == 16 && player.playerTask.taskMain.index == 0) {
            return ConstTask.TASK_16_0;
        } else if (player.playerTask.taskMain.id == 16 && player.playerTask.taskMain.index == 1) {
            return ConstTask.TASK_16_1;
        } else if (player.playerTask.taskMain.id == 16 && player.playerTask.taskMain.index == 2) {
            return ConstTask.TASK_16_2;
        } else if (player.playerTask.taskMain.id == 16 && player.playerTask.taskMain.index == 3) {
            return ConstTask.TASK_16_3;
        } else if (player.playerTask.taskMain.id == 16 && player.playerTask.taskMain.index == 4) {
            return ConstTask.TASK_16_4;
        } else if (player.playerTask.taskMain.id == 16 && player.playerTask.taskMain.index == 5) {
            return ConstTask.TASK_16_5;
        } else if (player.playerTask.taskMain.id == 16 && player.playerTask.taskMain.index == 6) {
            return ConstTask.TASK_16_6;
        } else if (player.playerTask.taskMain.id == 17 && player.playerTask.taskMain.index == 0) {
            return ConstTask.TASK_17_0;
        } else if (player.playerTask.taskMain.id == 17 && player.playerTask.taskMain.index == 1) {
            return ConstTask.TASK_17_1;
        } else if (player.playerTask.taskMain.id == 17 && player.playerTask.taskMain.index == 2) {
            return ConstTask.TASK_17_2;
        } else if (player.playerTask.taskMain.id == 17 && player.playerTask.taskMain.index == 3) {
            return ConstTask.TASK_17_3;
        } else if (player.playerTask.taskMain.id == 17 && player.playerTask.taskMain.index == 4) {
            return ConstTask.TASK_17_4;
        } else if (player.playerTask.taskMain.id == 17 && player.playerTask.taskMain.index == 5) {
            return ConstTask.TASK_17_5;
        } else if (player.playerTask.taskMain.id == 17 && player.playerTask.taskMain.index == 6) {
            return ConstTask.TASK_17_6;
        } else if (player.playerTask.taskMain.id == 18 && player.playerTask.taskMain.index == 0) {
            return ConstTask.TASK_18_0;
        } else if (player.playerTask.taskMain.id == 18 && player.playerTask.taskMain.index == 1) {
            return ConstTask.TASK_18_1;
        } else if (player.playerTask.taskMain.id == 18 && player.playerTask.taskMain.index == 2) {
            return ConstTask.TASK_18_2;
        } else if (player.playerTask.taskMain.id == 18 && player.playerTask.taskMain.index == 3) {
            return ConstTask.TASK_18_3;
        } else if (player.playerTask.taskMain.id == 18 && player.playerTask.taskMain.index == 4) {
            return ConstTask.TASK_18_4;
        } else if (player.playerTask.taskMain.id == 18 && player.playerTask.taskMain.index == 5) {
            return ConstTask.TASK_18_5;
        } else if (player.playerTask.taskMain.id == 18 && player.playerTask.taskMain.index == 6) {
            return ConstTask.TASK_18_6;
        } else if (player.playerTask.taskMain.id == 19 && player.playerTask.taskMain.index == 0) {
            return ConstTask.TASK_19_0;
        } else if (player.playerTask.taskMain.id == 19 && player.playerTask.taskMain.index == 1) {
            return ConstTask.TASK_19_1;
        } else if (player.playerTask.taskMain.id == 19 && player.playerTask.taskMain.index == 2) {
            return ConstTask.TASK_19_2;
        } else if (player.playerTask.taskMain.id == 19 && player.playerTask.taskMain.index == 3) {
            return ConstTask.TASK_19_3;
        } else if (player.playerTask.taskMain.id == 19 && player.playerTask.taskMain.index == 4) {
            return ConstTask.TASK_19_4;
        } else if (player.playerTask.taskMain.id == 19 && player.playerTask.taskMain.index == 5) {
            return ConstTask.TASK_19_5;
        } else if (player.playerTask.taskMain.id == 19 && player.playerTask.taskMain.index == 6) {
            return ConstTask.TASK_19_6;
        } else if (player.playerTask.taskMain.id == 20 && player.playerTask.taskMain.index == 0) {
            return ConstTask.TASK_20_0;
        } else if (player.playerTask.taskMain.id == 20 && player.playerTask.taskMain.index == 1) {
            return ConstTask.TASK_20_1;
        } else if (player.playerTask.taskMain.id == 20 && player.playerTask.taskMain.index == 2) {
            return ConstTask.TASK_20_2;
        } else if (player.playerTask.taskMain.id == 20 && player.playerTask.taskMain.index == 3) {
            return ConstTask.TASK_20_3;
        } else if (player.playerTask.taskMain.id == 20 && player.playerTask.taskMain.index == 4) {
            return ConstTask.TASK_20_4;
        } else if (player.playerTask.taskMain.id == 20 && player.playerTask.taskMain.index == 5) {
            return ConstTask.TASK_20_5;
        } else if (player.playerTask.taskMain.id == 20 && player.playerTask.taskMain.index == 6) {
            return ConstTask.TASK_20_6;
        } else if (player.playerTask.taskMain.id == 21 && player.playerTask.taskMain.index == 0) {
            return ConstTask.TASK_21_0;
        } else if (player.playerTask.taskMain.id == 21 && player.playerTask.taskMain.index == 1) {
            return ConstTask.TASK_21_1;
        } else if (player.playerTask.taskMain.id == 21 && player.playerTask.taskMain.index == 2) {
            return ConstTask.TASK_21_2;
        } else if (player.playerTask.taskMain.id == 21 && player.playerTask.taskMain.index == 3) {
            return ConstTask.TASK_21_3;
        } else if (player.playerTask.taskMain.id == 21 && player.playerTask.taskMain.index == 4) {
            return ConstTask.TASK_21_4;
        } else if (player.playerTask.taskMain.id == 21 && player.playerTask.taskMain.index == 5) {
            return ConstTask.TASK_21_5;
        } else if (player.playerTask.taskMain.id == 21 && player.playerTask.taskMain.index == 6) {
            return ConstTask.TASK_21_6;
        } else if (player.playerTask.taskMain.id == 22 && player.playerTask.taskMain.index == 0) {
            return ConstTask.TASK_22_0;
        } else if (player.playerTask.taskMain.id == 22 && player.playerTask.taskMain.index == 1) {
            return ConstTask.TASK_22_1;
        } else if (player.playerTask.taskMain.id == 22 && player.playerTask.taskMain.index == 2) {
            return ConstTask.TASK_22_2;
        } else if (player.playerTask.taskMain.id == 22 && player.playerTask.taskMain.index == 3) {
            return ConstTask.TASK_22_3;
        } else if (player.playerTask.taskMain.id == 22 && player.playerTask.taskMain.index == 4) {
            return ConstTask.TASK_22_4;
        } else if (player.playerTask.taskMain.id == 22 && player.playerTask.taskMain.index == 5) {
            return ConstTask.TASK_22_5;
        } else if (player.playerTask.taskMain.id == 22 && player.playerTask.taskMain.index == 6) {
            return ConstTask.TASK_22_6;
        } else if (player.playerTask.taskMain.id == 23 && player.playerTask.taskMain.index == 0) {
            return ConstTask.TASK_23_0;
        } else if (player.playerTask.taskMain.id == 23 && player.playerTask.taskMain.index == 1) {
            return ConstTask.TASK_23_1;
        } else if (player.playerTask.taskMain.id == 23 && player.playerTask.taskMain.index == 2) {
            return ConstTask.TASK_23_2;
        } else if (player.playerTask.taskMain.id == 23 && player.playerTask.taskMain.index == 3) {
            return ConstTask.TASK_23_3;
        } else if (player.playerTask.taskMain.id == 23 && player.playerTask.taskMain.index == 4) {
            return ConstTask.TASK_23_4;
        } else if (player.playerTask.taskMain.id == 23 && player.playerTask.taskMain.index == 5) {
            return ConstTask.TASK_23_5;
        } else if (player.playerTask.taskMain.id == 23 && player.playerTask.taskMain.index == 6) {
            return ConstTask.TASK_23_6;
        } else if (player.playerTask.taskMain.id == 24 && player.playerTask.taskMain.index == 0) {
            return ConstTask.TASK_24_0;
        } else if (player.playerTask.taskMain.id == 24 && player.playerTask.taskMain.index == 1) {
            return ConstTask.TASK_24_1;
        } else if (player.playerTask.taskMain.id == 24 && player.playerTask.taskMain.index == 2) {
            return ConstTask.TASK_24_2;
        } else if (player.playerTask.taskMain.id == 24 && player.playerTask.taskMain.index == 3) {
            return ConstTask.TASK_24_3;
        } else if (player.playerTask.taskMain.id == 24 && player.playerTask.taskMain.index == 4) {
            return ConstTask.TASK_24_4;
        } else if (player.playerTask.taskMain.id == 24 && player.playerTask.taskMain.index == 5) {
            return ConstTask.TASK_24_5;
        } else if (player.playerTask.taskMain.id == 24 && player.playerTask.taskMain.index == 6) {
            return ConstTask.TASK_24_6;
        } else if (player.playerTask.taskMain.id == 25 && player.playerTask.taskMain.index == 0) {
            return ConstTask.TASK_25_0;
        } else if (player.playerTask.taskMain.id == 25 && player.playerTask.taskMain.index == 1) {
            return ConstTask.TASK_25_1;
        } else if (player.playerTask.taskMain.id == 25 && player.playerTask.taskMain.index == 2) {
            return ConstTask.TASK_25_2;
        } else if (player.playerTask.taskMain.id == 25 && player.playerTask.taskMain.index == 3) {
            return ConstTask.TASK_25_3;
        } else if (player.playerTask.taskMain.id == 25 && player.playerTask.taskMain.index == 4) {
            return ConstTask.TASK_25_4;
        } else if (player.playerTask.taskMain.id == 25 && player.playerTask.taskMain.index == 5) {
            return ConstTask.TASK_25_5;
        } else if (player.playerTask.taskMain.id == 25 && player.playerTask.taskMain.index == 6) {
            return ConstTask.TASK_25_6;
        } else if (player.playerTask.taskMain.id == 26 && player.playerTask.taskMain.index == 0) {
            return ConstTask.TASK_26_0;
        } else if (player.playerTask.taskMain.id == 26 && player.playerTask.taskMain.index == 1) {
            return ConstTask.TASK_26_1;
        } else if (player.playerTask.taskMain.id == 26 && player.playerTask.taskMain.index == 2) {
            return ConstTask.TASK_26_2;
        } else if (player.playerTask.taskMain.id == 26 && player.playerTask.taskMain.index == 3) {
            return ConstTask.TASK_26_3;
        } else if (player.playerTask.taskMain.id == 26 && player.playerTask.taskMain.index == 4) {
            return ConstTask.TASK_26_4;
        } else if (player.playerTask.taskMain.id == 26 && player.playerTask.taskMain.index == 5) {
            return ConstTask.TASK_26_5;
        } else if (player.playerTask.taskMain.id == 26 && player.playerTask.taskMain.index == 6) {
            return ConstTask.TASK_26_6;
        } else if (player.playerTask.taskMain.id == 27 && player.playerTask.taskMain.index == 0) {
            return ConstTask.TASK_27_0;
        } else if (player.playerTask.taskMain.id == 27 && player.playerTask.taskMain.index == 1) {
            return ConstTask.TASK_27_1;
        } else if (player.playerTask.taskMain.id == 27 && player.playerTask.taskMain.index == 2) {
            return ConstTask.TASK_27_2;
        } else if (player.playerTask.taskMain.id == 27 && player.playerTask.taskMain.index == 3) {
            return ConstTask.TASK_27_3;
        } else if (player.playerTask.taskMain.id == 27 && player.playerTask.taskMain.index == 4) {
            return ConstTask.TASK_27_4;
        } else if (player.playerTask.taskMain.id == 27 && player.playerTask.taskMain.index == 5) {
            return ConstTask.TASK_27_5;
        } else if (player.playerTask.taskMain.id == 27 && player.playerTask.taskMain.index == 6) {
            return ConstTask.TASK_27_6;
        } else if (player.playerTask.taskMain.id == 28 && player.playerTask.taskMain.index == 0) {
            return ConstTask.TASK_28_0;
        } else if (player.playerTask.taskMain.id == 28 && player.playerTask.taskMain.index == 1) {
            return ConstTask.TASK_28_1;
        } else if (player.playerTask.taskMain.id == 28 && player.playerTask.taskMain.index == 2) {
            return ConstTask.TASK_28_2;
        } else if (player.playerTask.taskMain.id == 28 && player.playerTask.taskMain.index == 3) {
            return ConstTask.TASK_28_3;
        } else if (player.playerTask.taskMain.id == 28 && player.playerTask.taskMain.index == 4) {
            return ConstTask.TASK_28_4;
        } else if (player.playerTask.taskMain.id == 28 && player.playerTask.taskMain.index == 5) {
            return ConstTask.TASK_28_5;
        } else if (player.playerTask.taskMain.id == 28 && player.playerTask.taskMain.index == 6) {
            return ConstTask.TASK_28_6;
        } else if (player.playerTask.taskMain.id == 29 && player.playerTask.taskMain.index == 0) {
            return ConstTask.TASK_29_0;
        } else if (player.playerTask.taskMain.id == 29 && player.playerTask.taskMain.index == 1) {
            return ConstTask.TASK_29_1;
        } else if (player.playerTask.taskMain.id == 29 && player.playerTask.taskMain.index == 2) {
            return ConstTask.TASK_29_2;
        } else if (player.playerTask.taskMain.id == 29 && player.playerTask.taskMain.index == 3) {
            return ConstTask.TASK_29_3;
        } else if (player.playerTask.taskMain.id == 29 && player.playerTask.taskMain.index == 4) {
            return ConstTask.TASK_29_4;
        } else if (player.playerTask.taskMain.id == 29 && player.playerTask.taskMain.index == 5) {
            return ConstTask.TASK_29_5;
        } else if (player.playerTask.taskMain.id == 29 && player.playerTask.taskMain.index == 6) {
            return ConstTask.TASK_29_6;
        } else if (player.playerTask.taskMain.id == 30 && player.playerTask.taskMain.index == 0) {
            return ConstTask.TASK_30_0;
        } else if (player.playerTask.taskMain.id == 30 && player.playerTask.taskMain.index == 1) {
            return ConstTask.TASK_30_1;
        } else if (player.playerTask.taskMain.id == 30 && player.playerTask.taskMain.index == 2) {
            return ConstTask.TASK_30_2;
        } else if (player.playerTask.taskMain.id == 30 && player.playerTask.taskMain.index == 3) {
            return ConstTask.TASK_30_3;
        } else if (player.playerTask.taskMain.id == 30 && player.playerTask.taskMain.index == 4) {
            return ConstTask.TASK_30_4;
        } else if (player.playerTask.taskMain.id == 30 && player.playerTask.taskMain.index == 5) {
            return ConstTask.TASK_30_5;
        } else if (player.playerTask.taskMain.id == 30 && player.playerTask.taskMain.index == 6) {
            return ConstTask.TASK_30_6;
        } else if (player.playerTask.taskMain.id == 31 && player.playerTask.taskMain.index == 0) {
            return ConstTask.TASK_31_0;
        } else if (player.playerTask.taskMain.id == 31 && player.playerTask.taskMain.index == 1) {
            return ConstTask.TASK_31_1;
        } else if (player.playerTask.taskMain.id == 31 && player.playerTask.taskMain.index == 2) {
            return ConstTask.TASK_31_2;
        } else if (player.playerTask.taskMain.id == 31 && player.playerTask.taskMain.index == 3) {
            return ConstTask.TASK_31_3;
        } else if (player.playerTask.taskMain.id == 31 && player.playerTask.taskMain.index == 4) {
            return ConstTask.TASK_31_4;
        } else if (player.playerTask.taskMain.id == 31 && player.playerTask.taskMain.index == 5) {
            return ConstTask.TASK_31_5;
        } else if (player.playerTask.taskMain.id == 31 && player.playerTask.taskMain.index == 6) {
            return ConstTask.TASK_31_6;
        } else if (player.playerTask.taskMain.id == 32 && player.playerTask.taskMain.index == 0) {
            return ConstTask.TASK_32_0;
        } else if (player.playerTask.taskMain.id == 32 && player.playerTask.taskMain.index == 1) {
            return ConstTask.TASK_32_1;
        } else if (player.playerTask.taskMain.id == 32 && player.playerTask.taskMain.index == 2) {
            return ConstTask.TASK_32_2;
        } else if (player.playerTask.taskMain.id == 32 && player.playerTask.taskMain.index == 3) {
            return ConstTask.TASK_32_3;
        } else if (player.playerTask.taskMain.id == 32 && player.playerTask.taskMain.index == 4) {
            return ConstTask.TASK_32_4;
        } else if (player.playerTask.taskMain.id == 32 && player.playerTask.taskMain.index == 5) {
            return ConstTask.TASK_32_5;
        } else if (player.playerTask.taskMain.id == 32 && player.playerTask.taskMain.index == 6) {
            return ConstTask.TASK_32_6;
        } else if (player.playerTask.taskMain.id == 33 && player.playerTask.taskMain.index == 0) {
            return ConstTask.TASK_33_0;
        } else if (player.playerTask.taskMain.id == 33 && player.playerTask.taskMain.index == 1) {
            return ConstTask.TASK_33_1;
        } else if (player.playerTask.taskMain.id == 33 && player.playerTask.taskMain.index == 2) {
            return ConstTask.TASK_33_2;
        } else if (player.playerTask.taskMain.id == 33 && player.playerTask.taskMain.index == 3) {
            return ConstTask.TASK_33_3;
        } else if (player.playerTask.taskMain.id == 33 && player.playerTask.taskMain.index == 4) {
            return ConstTask.TASK_33_4;
        } else if (player.playerTask.taskMain.id == 33 && player.playerTask.taskMain.index == 5) {
            return ConstTask.TASK_33_5;
        } else if (player.playerTask.taskMain.id == 33 && player.playerTask.taskMain.index == 6) {
            return ConstTask.TASK_33_6;
        } else if (player.playerTask.taskMain.id == 34 && player.playerTask.taskMain.index == 0) {
            return ConstTask.TASK_34_0;
        } else if (player.playerTask.taskMain.id == 34 && player.playerTask.taskMain.index == 1) {
            return ConstTask.TASK_34_1;
        } else if (player.playerTask.taskMain.id == 34 && player.playerTask.taskMain.index == 2) {
            return ConstTask.TASK_34_2;
        } else if (player.playerTask.taskMain.id == 34 && player.playerTask.taskMain.index == 3) {
            return ConstTask.TASK_34_3;
        } else if (player.playerTask.taskMain.id == 34 && player.playerTask.taskMain.index == 4) {
            return ConstTask.TASK_34_4;
        } else if (player.playerTask.taskMain.id == 34 && player.playerTask.taskMain.index == 5) {
            return ConstTask.TASK_34_5;
        } else if (player.playerTask.taskMain.id == 34 && player.playerTask.taskMain.index == 6) {
            return ConstTask.TASK_34_6;
        } else if (player.playerTask.taskMain.id == 35 && player.playerTask.taskMain.index == 0) {
            return ConstTask.TASK_35_0;
        } else if (player.playerTask.taskMain.id == 35 && player.playerTask.taskMain.index == 1) {
            return ConstTask.TASK_35_1;
        } else if (player.playerTask.taskMain.id == 35 && player.playerTask.taskMain.index == 2) {
            return ConstTask.TASK_35_2;
        } else if (player.playerTask.taskMain.id == 35 && player.playerTask.taskMain.index == 3) {
            return ConstTask.TASK_35_3;
        } else if (player.playerTask.taskMain.id == 35 && player.playerTask.taskMain.index == 4) {
            return ConstTask.TASK_35_4;
        } else if (player.playerTask.taskMain.id == 35 && player.playerTask.taskMain.index == 5) {
            return ConstTask.TASK_35_5;
        } else if (player.playerTask.taskMain.id == 35 && player.playerTask.taskMain.index == 6) {
            return ConstTask.TASK_35_6;
        } else if (player.playerTask.taskMain.id == 36 && player.playerTask.taskMain.index == 0) {
            return ConstTask.TASK_36_0;
        } else if (player.playerTask.taskMain.id == 36 && player.playerTask.taskMain.index == 1) {
            return ConstTask.TASK_36_1;
        } else if (player.playerTask.taskMain.id == 36 && player.playerTask.taskMain.index == 2) {
            return ConstTask.TASK_36_2;
        } else if (player.playerTask.taskMain.id == 36 && player.playerTask.taskMain.index == 3) {
            return ConstTask.TASK_36_3;
        } else if (player.playerTask.taskMain.id == 36 && player.playerTask.taskMain.index == 4) {
            return ConstTask.TASK_36_4;
        } else if (player.playerTask.taskMain.id == 36 && player.playerTask.taskMain.index == 5) {
            return ConstTask.TASK_36_5;
        } else if (player.playerTask.taskMain.id == 36 && player.playerTask.taskMain.index == 6) {
            return ConstTask.TASK_36_6;
        } else if (player.playerTask.taskMain.id == 37 && player.playerTask.taskMain.index == 0) {
            return ConstTask.TASK_37_0;
        } else if (player.playerTask.taskMain.id == 37 && player.playerTask.taskMain.index == 1) {
            return ConstTask.TASK_37_1;
        } else if (player.playerTask.taskMain.id == 37 && player.playerTask.taskMain.index == 2) {
            return ConstTask.TASK_37_2;
        } else if (player.playerTask.taskMain.id == 37 && player.playerTask.taskMain.index == 3) {
            return ConstTask.TASK_37_3;
        } else if (player.playerTask.taskMain.id == 37 && player.playerTask.taskMain.index == 4) {
            return ConstTask.TASK_37_4;
        } else if (player.playerTask.taskMain.id == 37 && player.playerTask.taskMain.index == 5) {
            return ConstTask.TASK_37_5;
        } else if (player.playerTask.taskMain.id == 37 && player.playerTask.taskMain.index == 6) {
            return ConstTask.TASK_37_6;
        } else if (player.playerTask.taskMain.id == 38 && player.playerTask.taskMain.index == 0) {
            return ConstTask.TASK_38_0;
        } else if (player.playerTask.taskMain.id == 38 && player.playerTask.taskMain.index == 1) {
            return ConstTask.TASK_38_1;
        } else if (player.playerTask.taskMain.id == 38 && player.playerTask.taskMain.index == 2) {
            return ConstTask.TASK_38_2;
        } else if (player.playerTask.taskMain.id == 38 && player.playerTask.taskMain.index == 3) {
            return ConstTask.TASK_38_3;
        } else if (player.playerTask.taskMain.id == 38 && player.playerTask.taskMain.index == 4) {
            return ConstTask.TASK_38_4;
        } else if (player.playerTask.taskMain.id == 38 && player.playerTask.taskMain.index == 5) {
            return ConstTask.TASK_38_5;
        } else if (player.playerTask.taskMain.id == 38 && player.playerTask.taskMain.index == 6) {
            return ConstTask.TASK_38_6;
        } else if (player.playerTask.taskMain.id == 39 && player.playerTask.taskMain.index == 0) {
            return ConstTask.TASK_39_0;
        } else if (player.playerTask.taskMain.id == 39 && player.playerTask.taskMain.index == 1) {
            return ConstTask.TASK_39_1;
        } else if (player.playerTask.taskMain.id == 39 && player.playerTask.taskMain.index == 2) {
            return ConstTask.TASK_39_2;
        } else if (player.playerTask.taskMain.id == 39 && player.playerTask.taskMain.index == 3) {
            return ConstTask.TASK_39_3;
        } else if (player.playerTask.taskMain.id == 39 && player.playerTask.taskMain.index == 4) {
            return ConstTask.TASK_39_4;
        } else if (player.playerTask.taskMain.id == 39 && player.playerTask.taskMain.index == 5) {
            return ConstTask.TASK_39_5;
        } else if (player.playerTask.taskMain.id == 39 && player.playerTask.taskMain.index == 6) {
            return ConstTask.TASK_39_6;
        } else if (player.playerTask.taskMain.id == 40 && player.playerTask.taskMain.index == 0) {
            return ConstTask.TASK_40_0;
        } else if (player.playerTask.taskMain.id == 40 && player.playerTask.taskMain.index == 1) {
            return ConstTask.TASK_40_1;
        } else if (player.playerTask.taskMain.id == 40 && player.playerTask.taskMain.index == 2) {
            return ConstTask.TASK_40_2;
        } else if (player.playerTask.taskMain.id == 40 && player.playerTask.taskMain.index == 3) {
            return ConstTask.TASK_40_3;
        } else if (player.playerTask.taskMain.id == 40 && player.playerTask.taskMain.index == 4) {
            return ConstTask.TASK_40_4;
        } else if (player.playerTask.taskMain.id == 40 && player.playerTask.taskMain.index == 5) {
            return ConstTask.TASK_40_5;
        } else if (player.playerTask.taskMain.id == 40 && player.playerTask.taskMain.index == 6) {
            return ConstTask.TASK_40_6;
        } else if (player.playerTask.taskMain.id == 41 && player.playerTask.taskMain.index == 0) {
            return ConstTask.TASK_41_0;
        } else if (player.playerTask.taskMain.id == 41 && player.playerTask.taskMain.index == 1) {
            return ConstTask.TASK_41_1;
        } else if (player.playerTask.taskMain.id == 41 && player.playerTask.taskMain.index == 2) {
            return ConstTask.TASK_41_2;
        } else if (player.playerTask.taskMain.id == 41 && player.playerTask.taskMain.index == 3) {
            return ConstTask.TASK_41_3;
        } else if (player.playerTask.taskMain.id == 41 && player.playerTask.taskMain.index == 4) {
            return ConstTask.TASK_41_4;
        } else if (player.playerTask.taskMain.id == 41 && player.playerTask.taskMain.index == 5) {
            return ConstTask.TASK_41_5;
        } else if (player.playerTask.taskMain.id == 41 && player.playerTask.taskMain.index == 6) {
            return ConstTask.TASK_41_6;
        } else if (player.playerTask.taskMain.id == 42 && player.playerTask.taskMain.index == 0) {
            return ConstTask.TASK_42_0;
        } else if (player.playerTask.taskMain.id == 42 && player.playerTask.taskMain.index == 1) {
            return ConstTask.TASK_42_1;
        } else if (player.playerTask.taskMain.id == 42 && player.playerTask.taskMain.index == 2) {
            return ConstTask.TASK_42_2;
        } else if (player.playerTask.taskMain.id == 42 && player.playerTask.taskMain.index == 3) {
            return ConstTask.TASK_42_3;
        } else if (player.playerTask.taskMain.id == 42 && player.playerTask.taskMain.index == 4) {
            return ConstTask.TASK_42_4;
        } else if (player.playerTask.taskMain.id == 42 && player.playerTask.taskMain.index == 5) {
            return ConstTask.TASK_42_5;
        } else if (player.playerTask.taskMain.id == 42 && player.playerTask.taskMain.index == 6) {
            return ConstTask.TASK_42_6;
        } else if (player.playerTask.taskMain.id == 43 && player.playerTask.taskMain.index == 0) {
            return ConstTask.TASK_43_0;
        } else if (player.playerTask.taskMain.id == 43 && player.playerTask.taskMain.index == 1) {
            return ConstTask.TASK_43_1;
        } else if (player.playerTask.taskMain.id == 43 && player.playerTask.taskMain.index == 2) {
            return ConstTask.TASK_43_2;
        } else if (player.playerTask.taskMain.id == 43 && player.playerTask.taskMain.index == 3) {
            return ConstTask.TASK_43_3;
        } else if (player.playerTask.taskMain.id == 43 && player.playerTask.taskMain.index == 4) {
            return ConstTask.TASK_43_4;
        } else if (player.playerTask.taskMain.id == 43 && player.playerTask.taskMain.index == 5) {
            return ConstTask.TASK_43_5;
        } else if (player.playerTask.taskMain.id == 43 && player.playerTask.taskMain.index == 6) {
            return ConstTask.TASK_43_6;
        } else if (player.playerTask.taskMain.id == 44 && player.playerTask.taskMain.index == 0) {
            return ConstTask.TASK_44_0;
        } else if (player.playerTask.taskMain.id == 44 && player.playerTask.taskMain.index == 1) {
            return ConstTask.TASK_44_1;
        } else if (player.playerTask.taskMain.id == 44 && player.playerTask.taskMain.index == 2) {
            return ConstTask.TASK_44_2;
        } else if (player.playerTask.taskMain.id == 44 && player.playerTask.taskMain.index == 3) {
            return ConstTask.TASK_44_3;
        } else if (player.playerTask.taskMain.id == 44 && player.playerTask.taskMain.index == 4) {
            return ConstTask.TASK_44_4;
        } else if (player.playerTask.taskMain.id == 44 && player.playerTask.taskMain.index == 5) {
            return ConstTask.TASK_44_5;
        } else if (player.playerTask.taskMain.id == 44 && player.playerTask.taskMain.index == 6) {
            return ConstTask.TASK_44_6;
        } else if (player.playerTask.taskMain.id == 45 && player.playerTask.taskMain.index == 0) {
            return ConstTask.TASK_45_0;
        } else if (player.playerTask.taskMain.id == 45 && player.playerTask.taskMain.index == 1) {
            return ConstTask.TASK_45_1;
        } else if (player.playerTask.taskMain.id == 45 && player.playerTask.taskMain.index == 2) {
            return ConstTask.TASK_45_2;
        } else if (player.playerTask.taskMain.id == 45 && player.playerTask.taskMain.index == 3) {
            return ConstTask.TASK_45_3;
        } else if (player.playerTask.taskMain.id == 45 && player.playerTask.taskMain.index == 4) {
            return ConstTask.TASK_45_4;
        } else if (player.playerTask.taskMain.id == 45 && player.playerTask.taskMain.index == 5) {
            return ConstTask.TASK_45_5;
        } else if (player.playerTask.taskMain.id == 45 && player.playerTask.taskMain.index == 6) {
            return ConstTask.TASK_45_6;
        } else if (player.playerTask.taskMain.id == 46 && player.playerTask.taskMain.index == 0) {
            return ConstTask.TASK_46_0;
        } else if (player.playerTask.taskMain.id == 46 && player.playerTask.taskMain.index == 1) {
            return ConstTask.TASK_46_1;
        } else if (player.playerTask.taskMain.id == 46 && player.playerTask.taskMain.index == 2) {
            return ConstTask.TASK_46_2;
        } else if (player.playerTask.taskMain.id == 46 && player.playerTask.taskMain.index == 3) {
            return ConstTask.TASK_46_3;
        } else if (player.playerTask.taskMain.id == 46 && player.playerTask.taskMain.index == 4) {
            return ConstTask.TASK_46_4;
        } else if (player.playerTask.taskMain.id == 46 && player.playerTask.taskMain.index == 5) {
            return ConstTask.TASK_46_5;
        } else if (player.playerTask.taskMain.id == 46 && player.playerTask.taskMain.index == 6) {
            return ConstTask.TASK_46_6;
        } else if (player.playerTask.taskMain.id == 47 && player.playerTask.taskMain.index == 0) {
            return ConstTask.TASK_47_0;
        } else if (player.playerTask.taskMain.id == 47 && player.playerTask.taskMain.index == 1) {
            return ConstTask.TASK_47_1;
        } else if (player.playerTask.taskMain.id == 47 && player.playerTask.taskMain.index == 2) {
            return ConstTask.TASK_47_2;
        } else if (player.playerTask.taskMain.id == 47 && player.playerTask.taskMain.index == 3) {
            return ConstTask.TASK_47_3;
        } else if (player.playerTask.taskMain.id == 47 && player.playerTask.taskMain.index == 4) {
            return ConstTask.TASK_47_4;
        } else if (player.playerTask.taskMain.id == 47 && player.playerTask.taskMain.index == 5) {
            return ConstTask.TASK_47_5;
        } else if (player.playerTask.taskMain.id == 47 && player.playerTask.taskMain.index == 6) {
            return ConstTask.TASK_47_6;
        } else if (player.playerTask.taskMain.id == 48 && player.playerTask.taskMain.index == 0) {
            return ConstTask.TASK_48_0;
        } else if (player.playerTask.taskMain.id == 48 && player.playerTask.taskMain.index == 1) {
            return ConstTask.TASK_48_1;
        } else if (player.playerTask.taskMain.id == 48 && player.playerTask.taskMain.index == 2) {
            return ConstTask.TASK_48_2;
        } else if (player.playerTask.taskMain.id == 48 && player.playerTask.taskMain.index == 3) {
            return ConstTask.TASK_48_3;
        } else if (player.playerTask.taskMain.id == 48 && player.playerTask.taskMain.index == 4) {
            return ConstTask.TASK_48_4;
        } else if (player.playerTask.taskMain.id == 48 && player.playerTask.taskMain.index == 5) {
            return ConstTask.TASK_48_5;
        } else if (player.playerTask.taskMain.id == 48 && player.playerTask.taskMain.index == 6) {
            return ConstTask.TASK_48_6;
        } else if (player.playerTask.taskMain.id == 49 && player.playerTask.taskMain.index == 0) {
            return ConstTask.TASK_49_0;
        } else if (player.playerTask.taskMain.id == 49 && player.playerTask.taskMain.index == 1) {
            return ConstTask.TASK_49_1;
        } else if (player.playerTask.taskMain.id == 49 && player.playerTask.taskMain.index == 2) {
            return ConstTask.TASK_49_2;
        } else if (player.playerTask.taskMain.id == 49 && player.playerTask.taskMain.index == 3) {
            return ConstTask.TASK_49_3;
        } else if (player.playerTask.taskMain.id == 49 && player.playerTask.taskMain.index == 4) {
            return ConstTask.TASK_49_4;
        } else if (player.playerTask.taskMain.id == 49 && player.playerTask.taskMain.index == 5) {
            return ConstTask.TASK_49_5;
        } else if (player.playerTask.taskMain.id == 49 && player.playerTask.taskMain.index == 6) {
            return ConstTask.TASK_49_6;
        } else if (player.playerTask.taskMain.id == 50 && player.playerTask.taskMain.index == 0) {
            return ConstTask.TASK_50_0;
        } else if (player.playerTask.taskMain.id == 50 && player.playerTask.taskMain.index == 1) {
            return ConstTask.TASK_50_1;
        } else if (player.playerTask.taskMain.id == 50 && player.playerTask.taskMain.index == 2) {
            return ConstTask.TASK_50_2;
        } else if (player.playerTask.taskMain.id == 50 && player.playerTask.taskMain.index == 3) {
            return ConstTask.TASK_50_3;
        } else if (player.playerTask.taskMain.id == 50 && player.playerTask.taskMain.index == 4) {
            return ConstTask.TASK_50_4;
        } else if (player.playerTask.taskMain.id == 50 && player.playerTask.taskMain.index == 5) {
            return ConstTask.TASK_50_5;
        } else if (player.playerTask.taskMain.id == 50 && player.playerTask.taskMain.index == 6) {
            return ConstTask.TASK_50_6;
        } else {
            return -1;
        }
    }

    //--------------------------------------------------------------------------
    public SideTaskTemplate getSideTaskTemplateById(int id) {
        if (id != -1) {
            return Manager.SIDE_TASKS_TEMPLATE.get(id);
        }
        return null;
    }

    public void changeSideTask(Player player, byte level) {
        if (player.playerTask.sideTask.leftTask > 0) {
            player.playerTask.sideTask.reset();
            SideTaskTemplate temp = Manager.SIDE_TASKS_TEMPLATE.get(Util.nextInt(0, Manager.SIDE_TASKS_TEMPLATE.size() - 1));
            player.playerTask.sideTask.template = temp;
            player.playerTask.sideTask.maxCount = Util.nextInt(temp.count[level][0], temp.count[level][1]);
            player.playerTask.sideTask.leftTask--;
            player.playerTask.sideTask.level = level;
            player.playerTask.sideTask.receivedTime = System.currentTimeMillis();
            Service.getInstance().sendThongBao(player, "Bạn nhận được nhiệm vụ: " + player.playerTask.sideTask.getName());
        } else {
            Service.getInstance().sendThongBao(player,
                    "Bạn đã nhận hết nhiệm vụ hôm nay. Hãy chờ tới ngày mai rồi nhận tiếp");
        }
    }

    public void removeSideTask(Player player) {
        Service.getInstance().sendThongBao(player, "Bạn vừa hủy bỏ nhiệm vụ " + player.playerTask.sideTask.getName());
        player.playerTask.sideTask.reset();
    }

    public void paySideTask(Player player) {
        if (player.playerTask.sideTask.template != null) {
            if (player.playerTask.sideTask.isDone()) {
                int goldReward = 0;
                int rubyReward = 0;
                int giftReward = 0;
                switch (player.playerTask.sideTask.level) {
                    case ConstTask.EASY:
                        goldReward = ConstTask.GOLD_EASY;
                        rubyReward = ConstTask.REWARD_RUBY;
                        giftReward = 1;
                        break;
                    case ConstTask.NORMAL:
                        goldReward = ConstTask.GOLD_NORMAL;
                        rubyReward = ConstTask.REWARD_RUBY;
                        giftReward = 2;
                        break;
                    case ConstTask.HARD:
                        goldReward = ConstTask.GOLD_HARD;
                        rubyReward = ConstTask.REWARD_RUBY;
                        giftReward = 3;
                        break;
                    case ConstTask.VERY_HARD:
                        goldReward = ConstTask.GOLD_VERY_HARD;
                        rubyReward = ConstTask.REWARD_RUBY;
                        giftReward = 4;
                        break;
                    case ConstTask.HELL:
                        goldReward = ConstTask.GOLD_HELL;
                        rubyReward = ConstTask.REWARD_RUBY;
                        giftReward = 5;
                        break;
                }
                player.inventory.addGold(goldReward);
                Service.getInstance().sendMoney(player);
                InventoryService.gI().sendItemBags(player);
                Service.getInstance().sendThongBao(player, "Bạn nhận được "
                        + Util.numberToMoney(goldReward) + " vàng và ");
                player.playerTask.sideTask.reset();
            } else {
                Service.getInstance().sendThongBao(player, "Bạn chưa hoàn thành nhiệm vụ");
            }
        }
    }

    public void checkDoneSideTaskKillMob(Player player, Mob mob) {
        if (player.playerTask.sideTask.template != null) {
            if ((player.playerTask.sideTask.template.id == 0 && mob.tempId == ConstMob.KHUNG_LONG)
                    || (player.playerTask.sideTask.template.id == 1 && mob.tempId == ConstMob.LON_LOI)
                    || (player.playerTask.sideTask.template.id == 2 && mob.tempId == ConstMob.QUY_DAT)
                    || (player.playerTask.sideTask.template.id == 3 && mob.tempId == ConstMob.KHUNG_LONG_ME)
                    || (player.playerTask.sideTask.template.id == 4 && mob.tempId == ConstMob.LON_LOI_ME)
                    || (player.playerTask.sideTask.template.id == 5 && mob.tempId == ConstMob.QUY_DAT_ME)
                    || (player.playerTask.sideTask.template.id == 6 && mob.tempId == ConstMob.THAN_LAN_BAY)
                    || (player.playerTask.sideTask.template.id == 7 && mob.tempId == ConstMob.PHI_LONG)
                    || (player.playerTask.sideTask.template.id == 8 && mob.tempId == ConstMob.QUY_BAY)
                    || (player.playerTask.sideTask.template.id == 9 && mob.tempId == ConstMob.THAN_LAN_ME)
                    || (player.playerTask.sideTask.template.id == 10 && mob.tempId == ConstMob.PHI_LONG_ME)
                    || (player.playerTask.sideTask.template.id == 11 && mob.tempId == ConstMob.QUY_BAY_ME)
                    || (player.playerTask.sideTask.template.id == 12 && mob.tempId == ConstMob.HEO_RUNG)
                    || (player.playerTask.sideTask.template.id == 13 && mob.tempId == ConstMob.HEO_DA_XANH)
                    || (player.playerTask.sideTask.template.id == 14 && mob.tempId == ConstMob.HEO_XAYDA)
                    || (player.playerTask.sideTask.template.id == 15 && mob.tempId == ConstMob.OC_MUON_HON)
                    || (player.playerTask.sideTask.template.id == 16 && mob.tempId == ConstMob.OC_SEN)
                    || (player.playerTask.sideTask.template.id == 17 && mob.tempId == ConstMob.HEO_XAYDA_ME)
                    || (player.playerTask.sideTask.template.id == 18 && mob.tempId == ConstMob.KHONG_TAC)
                    || (player.playerTask.sideTask.template.id == 19 && mob.tempId == ConstMob.QUY_DAU_TO)
                    || (player.playerTask.sideTask.template.id == 20 && mob.tempId == ConstMob.QUY_DIA_NGUC)
                    || (player.playerTask.sideTask.template.id == 21 && mob.tempId == ConstMob.HEO_RUNG_ME)
                    || (player.playerTask.sideTask.template.id == 22 && mob.tempId == ConstMob.HEO_XANH_ME)
                    || (player.playerTask.sideTask.template.id == 23 && mob.tempId == ConstMob.ALIEN)
                    || (player.playerTask.sideTask.template.id == 24 && mob.tempId == ConstMob.TAMBOURINE)
                    || (player.playerTask.sideTask.template.id == 25 && mob.tempId == ConstMob.DRUM)
                    || (player.playerTask.sideTask.template.id == 26 && mob.tempId == ConstMob.AKKUMAN)
                    || (player.playerTask.sideTask.template.id == 27 && mob.tempId == ConstMob.NAPPA)
                    || (player.playerTask.sideTask.template.id == 28 && mob.tempId == ConstMob.SOLDIER)
                    || (player.playerTask.sideTask.template.id == 29 && mob.tempId == ConstMob.APPULE)
                    || (player.playerTask.sideTask.template.id == 30 && mob.tempId == ConstMob.RASPBERRY)
                    || (player.playerTask.sideTask.template.id == 31 && mob.tempId == ConstMob.THAN_LAN_XANH)
                    || (player.playerTask.sideTask.template.id == 32 && mob.tempId == ConstMob.QUY_DAU_NHON)
                    || (player.playerTask.sideTask.template.id == 33 && mob.tempId == ConstMob.QUY_DAU_VANG)
                    || (player.playerTask.sideTask.template.id == 34 && mob.tempId == ConstMob.QUY_DA_TIM)
                    || (player.playerTask.sideTask.template.id == 35 && mob.tempId == ConstMob.QUY_GIA)
                    || (player.playerTask.sideTask.template.id == 36 && mob.tempId == ConstMob.CA_SAU)
                    || (player.playerTask.sideTask.template.id == 37 && mob.tempId == ConstMob.DOI_DA_XANH)
                    || (player.playerTask.sideTask.template.id == 38 && mob.tempId == ConstMob.QUY_CHIM)
                    || (player.playerTask.sideTask.template.id == 39 && mob.tempId == ConstMob.LINH_DAU_TROC)
                    || (player.playerTask.sideTask.template.id == 40 && mob.tempId == ConstMob.LINH_TAI_DAI)
                    || (player.playerTask.sideTask.template.id == 41 && mob.tempId == ConstMob.LINH_VU_TRU)
                    || (player.playerTask.sideTask.template.id == 42 && mob.tempId == ConstMob.KHI_LONG_DEN)
                    || (player.playerTask.sideTask.template.id == 43 && mob.tempId == ConstMob.KHI_GIAP_SAT)
                    || (player.playerTask.sideTask.template.id == 44 && mob.tempId == ConstMob.KHI_LONG_DO)
                    || (player.playerTask.sideTask.template.id == 45 && mob.tempId == ConstMob.KHI_LONG_VANG)
                    || (player.playerTask.sideTask.template.id == 46 && mob.tempId == ConstMob.XEN_CON_CAP_1)
                    || (player.playerTask.sideTask.template.id == 47 && mob.tempId == ConstMob.XEN_CON_CAP_2)
                    || (player.playerTask.sideTask.template.id == 48 && mob.tempId == ConstMob.XEN_CON_CAP_3)
                    || (player.playerTask.sideTask.template.id == 49 && mob.tempId == ConstMob.XEN_CON_CAP_4)
                    || (player.playerTask.sideTask.template.id == 50 && mob.tempId == ConstMob.XEN_CON_CAP_5)
                    || (player.playerTask.sideTask.template.id == 51 && mob.tempId == ConstMob.XEN_CON_CAP_6)
                    || (player.playerTask.sideTask.template.id == 52 && mob.tempId == ConstMob.XEN_CON_CAP_7)
                    || (player.playerTask.sideTask.template.id == 53 && mob.tempId == ConstMob.XEN_CON_CAP_8)
                    || (player.playerTask.sideTask.template.id == 54 && mob.tempId == ConstMob.TAI_TIM)
                    || (player.playerTask.sideTask.template.id == 55 && mob.tempId == ConstMob.ABO)
                    || (player.playerTask.sideTask.template.id == 56 && mob.tempId == ConstMob.KADO)
                    || (player.playerTask.sideTask.template.id == 57 && mob.tempId == ConstMob.DA_XANH)) {
                player.playerTask.sideTask.count++;
                notifyProcessSideTask(player);
            }
        }
    }

    public void checkDoneSideTaskPickItem(Player player, ItemMap item) {
        if (item != null && player.playerTask.sideTask.template != null) {
            if ((player.playerTask.sideTask.template.id == 58 && item.itemTemplate.type == 9)) {
                player.playerTask.sideTask.count += item.quantity;
                notifyProcessSideTask(player);
            }
        }
    }

    private void notifyProcessSideTask(Player player) {
        int percentDone = player.playerTask.sideTask.getPercentProcess();
        boolean notify = false;
        if (percentDone != 100) {
            if (!player.playerTask.sideTask.notify90 && percentDone >= 90) {
                player.playerTask.sideTask.notify90 = true;
                notify = true;
            } else if (!player.playerTask.sideTask.notify80 && percentDone >= 80) {
                player.playerTask.sideTask.notify80 = true;
                notify = true;
            } else if (!player.playerTask.sideTask.notify70 && percentDone >= 70) {
                player.playerTask.sideTask.notify70 = true;
                notify = true;
            } else if (!player.playerTask.sideTask.notify60 && percentDone >= 60) {
                player.playerTask.sideTask.notify60 = true;
                notify = true;
            } else if (!player.playerTask.sideTask.notify50 && percentDone >= 50) {
                player.playerTask.sideTask.notify50 = true;
                notify = true;
            } else if (!player.playerTask.sideTask.notify40 && percentDone >= 40) {
                player.playerTask.sideTask.notify40 = true;
                notify = true;
            } else if (!player.playerTask.sideTask.notify30 && percentDone >= 30) {
                player.playerTask.sideTask.notify30 = true;
                notify = true;
            } else if (!player.playerTask.sideTask.notify20 && percentDone >= 20) {
                player.playerTask.sideTask.notify20 = true;
                notify = true;
            } else if (!player.playerTask.sideTask.notify10 && percentDone >= 10) {
                player.playerTask.sideTask.notify10 = true;
                notify = true;
            } else if (!player.playerTask.sideTask.notify0 && percentDone >= 0) {
                player.playerTask.sideTask.notify0 = true;
                notify = true;
            }
            if (notify) {
                Service.getInstance().sendThongBao(player, "Nhiệm vụ: "
                        + player.playerTask.sideTask.getName() + " đã hoàn thành: "
                        + player.playerTask.sideTask.count + "/" + player.playerTask.sideTask.maxCount + " ("
                        + percentDone + "%)");
            }
        } else {
            Service.getInstance().sendThongBao(player, "Chúc mừng bạn đã hoàn thành nhiệm vụ, "
                    + "bây giờ hãy quay về Bò Mộng trả nhiệm vụ.");
        }
    }

    public void sendAchivement(Player player) {
        List<Achivement> achivements = player.playerTask.achivements;
        Message m = new Message(Cmd.ACHIEVEMENT);
        DataOutputStream ds = m.writer();
        try {
            ds.writeByte(0);
            ds.writeByte(achivements.size());
            for (Achivement a : achivements) {
                String detail = String.format(a.getDetail(), a.getCount(), a.getMaxCount());
                ds.writeUTF(a.getName());
                ds.writeUTF(detail);
                ds.writeShort(a.getMoney());
                ds.writeBoolean(a.isFinish());
                ds.writeBoolean(a.isReceive());
            }
            ds.flush();
            player.sendMessage(m);
            m.cleanup();
        } catch (IOException e) {

        }
    }

    public void rewardAchivement(Player player, byte id) {
        Achivement achivement = player.playerTask.achivements.get(id);
        if (achivement.isFinish()) {
            player.inventory.ruby += achivement.getMoney();
            Service.getInstance().sendMoney(player);
            achivement.setReceive(true);
            sendAchivement(player);
            Service.getInstance().sendThongBao(player, "Bạn nhận được " + achivement.getMoney() + " hồng ngọc");
        }
    }

    public void checkDoneAchivements(Player player) {
        List<Achivement> list = player.playerTask.achivements;
        for (Achivement achivement : list) {
            if (achivement.getId() == ConstAchive.GIA_NHAP_THAN_CAP || achivement.getId() == ConstAchive.SUC_MANH_GIOI_VUONG_THAN) {
                if (achivement.isDone(1000000)) {
                    achivement.setFinish(true);
                }
            } else if (achivement.getId() == ConstAchive.HOAT_DONG_CHAM_CHI) {
                if (achivement.isDone(60)) {
                    achivement.setFinish(true);
                }
            } else if (achivement.isDone()) {
                achivement.setFinish(true);
            }
        }
    }
}
