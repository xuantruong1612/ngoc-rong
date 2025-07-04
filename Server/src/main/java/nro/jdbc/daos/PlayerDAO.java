package nro.jdbc.daos;

import com.google.gson.Gson;
import nro.consts.ConstMap;
import nro.jdbc.DBService;
import nro.manager.AchiveManager;
import nro.models.item.Item;
import nro.models.item.ItemOption;
import nro.models.item.ItemTime;
import nro.models.player.*;
import nro.models.skill.Skill;
import nro.models.task.Achivement;
import nro.models.task.AchivementTemplate;
import nro.server.Manager;
import nro.services.MapService;
import nro.utils.Log;
import nro.utils.Util;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import nro.models.npc.specialnpc.duahau;
import nro.services.Service;

/**
 * Arriety
 */
public class PlayerDAO {

    public static boolean updateTimeLogout;

    public static void createNewPlayer(Connection con, int userId, String name, byte gender, int head, PreparedStatement ps) {
        try {
            JSONArray dataInventory = new JSONArray();

            dataInventory.add(50000000);
            dataInventory.add(99999);
            dataInventory.add(99999);
            String inventory = dataInventory.toJSONString();

            JSONArray dataLocation = new JSONArray();
            dataLocation.add(100);
            dataLocation.add(384);
            dataLocation.add(39 + gender);
            String location = dataLocation.toJSONString();

            JSONArray dataPoint = new JSONArray();
            dataPoint.add(0);// nang dong
            dataPoint.add(gender == 1 ? 200 : 100);//mp
            dataPoint.add(gender == 1 ? 200 : 100);//mpg
            dataPoint.add(0);//critg
            dataPoint.add(0);//limitpower
            dataPoint.add(1000);//stamina
            dataPoint.add(gender == 0 ? 200 : 100);//hp
            dataPoint.add(0);//defg
            dataPoint.add(100000000);//tn
            dataPoint.add(10000);//maxsta
            dataPoint.add(gender == 2 ? 15 : 10);//damg
            dataPoint.add(1500000);//pow
            dataPoint.add(gender == 0 ? 200 : 100);//hpg
            String point = dataPoint.toJSONString();

            JSONArray dataMagicTree = new JSONArray();
            dataMagicTree.add(0);//isupgr
            dataMagicTree.add(new Date().getTime());
            dataMagicTree.add(1);//LV
            dataMagicTree.add(new Date().getTime());
            dataMagicTree.add(5);//curr_pea
            String magicTree = dataMagicTree.toJSONString();

            /**
             *
             * [
             * {"temp_id":"1","option":[[5,7],[7,3]],"create_time":"49238749283748957""},
             * {"temp_id":"1","option":[[5,7],[7,3]],"create_time":"49238749283748957""},
             * {"temp_id":"-1","option":[],"create_time":"0""}, ... ]
             */
            int idAo = gender == 0 ? 0 : gender == 1 ? 1 : 2;
            int idQuan = gender == 0 ? 6 : gender == 1 ? 7 : 8;
            int def = gender == 2 ? 3 : 2;
            int hp = gender == 0 ? 30 : 20;

            JSONArray dataBody = new JSONArray();
            for (int i = 0; i < 16; i++) {
                JSONObject item = new JSONObject();
                JSONArray options = new JSONArray();
                JSONArray option = new JSONArray();
                switch (i) {
                    case 0:
                        option.add(47);
                        option.add(def);
                        options.add(option);
                        item.put("temp_id", idAo);
                        item.put("create_time", System.currentTimeMillis());
                        item.put("quantity", 1);
                        break;
                    case 1:
                        option.add(6);
                        option.add(hp);
                        options.add(option);
                        item.put("temp_id", idQuan);
                        item.put("create_time", System.currentTimeMillis());
                        item.put("quantity", 1);
                        break;
                    default:
                        item.put("temp_id", -1);
                        item.put("create_time", 0);
                        item.put("quantity", 1);
                        break;
                }
                item.put("option", options);
                dataBody.add(item);
            }
            String itemsBody = dataBody.toJSONString();

            JSONArray dataBag = new JSONArray();
            for (int i = 0; i < 20; i++) {
                JSONObject item = new JSONObject();
                JSONArray options = new JSONArray();
                JSONArray option = new JSONArray();
                switch (i) {
                    case 0:
                        option.add(30);
                        option.add(0);
                        options.add(option);
                        item.put("temp_id", 2024);
                        item.put("create_time", System.currentTimeMillis());
                        item.put("quantity", 1);
                        break;
                    case 1:
                        option.add(30);
                        option.add(1);
                        options.add(option);
                        item.put("temp_id", 457);
                        item.put("create_time", System.currentTimeMillis());
                        item.put("quantity", 10000);
                        break;
                    case 2:
                        option.add(72);
                        option.add(16);
                        options.add(option);
                        item.put("temp_id", 1719);
                        item.put("create_time", System.currentTimeMillis());
                        item.put("quantity", 1);
                        break;
                    
                        
                      case 3:
                        option.add(30);
                        option.add(16);
                        options.add(option);
                        item.put("temp_id", 962);
                        item.put("create_time", System.currentTimeMillis());
                        item.put("quantity", 1);
                        break;   
                      case 4:
                        option.add(30);
                        option.add(16);
                        options.add(option);
                        item.put("temp_id", 963);
                        item.put("create_time", System.currentTimeMillis());
                        item.put("quantity", 1);
                        break;   
                         case 5:
                        option.add(30);
                        option.add(16);
                        options.add(option);
                        item.put("temp_id", 964);
                        item.put("create_time", System.currentTimeMillis());
                        item.put("quantity", 1);
                        break;   
                         case 6:
                        option.add(30);
                        option.add(16);
                        options.add(option);
                        item.put("temp_id", 965);
                        item.put("create_time", System.currentTimeMillis());
                        item.put("quantity", 1);
                        break;    
                        case 7:
                        option.add(30);
                        option.add(16);
                        options.add(option);
                        item.put("temp_id", 966);
                        item.put("create_time", System.currentTimeMillis());
                        item.put("quantity", 1);
                        break;   
                        case 8:
                        option.add(30);
                        option.add(16);
                        options.add(option);
                        item.put("temp_id", 1420);
                        item.put("create_time", System.currentTimeMillis());
                        item.put("quantity", 1000);
                        break;    
                         case 9:
                        option.add(72);
                        option.add(15);
                        options.add(option);
                        item.put("temp_id", 194);
                        item.put("create_time", System.currentTimeMillis());
                        item.put("quantity", 1);
                        break;     
                         case 10:
                        option.add(30);
                        option.add(15);
                        options.add(option);
                        item.put("temp_id", 1127);
                        item.put("create_time", System.currentTimeMillis());
                        item.put("quantity", 50);
                        break;      
                        
                    default:
                        item.put("temp_id", -1);
                        item.put("create_time", 0);
                        item.put("quantity", 1);
                        break;
                }
                item.put("option", options);
                dataBag.add(item);
            }
            String itemsBag = dataBag.toJSONString();

            JSONArray dataBox = new JSONArray();
            for (int i = 0; i < 20; i++) {
                JSONObject item = new JSONObject();
                JSONArray options = new JSONArray();
                JSONArray option = new JSONArray();
                if (i == 0) {
                    item.put("temp_id", 1281);
                    option.add(30);
                    option.add(1);
                    options.add(option);
                    item.put("create_time", System.currentTimeMillis());
                } else {
                    item.put("temp_id", -1);
                    item.put("create_time", 0);
                }
                item.put("option", options);
                item.put("quantity", 1);
                dataBox.add(item);
            }
            String itemsBox = dataBox.toJSONString();

            JSONArray dataLuckyRound = new JSONArray();
            for (int i = 0; i < 110; i++) {
                JSONObject item = new JSONObject();
                JSONArray options = new JSONArray();
                item.put("temp_id", -1);
                item.put("option", options);
                item.put("create_time", 0);
                item.put("quantity", 1);
                dataLuckyRound.add(item);
            }
            String itemsBoxLuckyRound = dataLuckyRound.toJSONString();

            String friends = "[]";
            String enemies = "[]";

            JSONArray dataIntrinsic = new JSONArray();
            dataIntrinsic.add(0);
            dataIntrinsic.add(0);
            dataIntrinsic.add(0);
            dataIntrinsic.add(0);
            String intrinsic = dataIntrinsic.toJSONString();

            JSONArray dataItemTime = new JSONArray();
            dataItemTime.add(0);
            dataItemTime.add(0);
            dataItemTime.add(0);
            dataItemTime.add(0);
            dataItemTime.add(0);
            dataItemTime.add(0);
            dataItemTime.add(0);
            dataItemTime.add(0);
            dataItemTime.add(0);
            dataItemTime.add(0);
            dataItemTime.add(0);
            dataItemTime.add(0);
            dataItemTime.add(0);
            dataItemTime.add(0);
            dataItemTime.add(0);
            String itemTime = dataItemTime.toJSONString();

            JSONArray dataItemNewTime = new JSONArray();

            dataItemNewTime.add(0);
            dataItemNewTime.add(0);
            dataItemNewTime.add(0);
            String itemTimeNew = dataItemNewTime.toJSONString();

            JSONArray data_exp_tutienne = new JSONArray();
            data_exp_tutienne.add(0);
            data_exp_tutienne.add(0);
            data_exp_tutienne.add(0);
            data_exp_tutienne.add(0);
            data_exp_tutienne.add(0);
            data_exp_tutienne.add(0);
            String data_exp_tutien = dataItemNewTime.toJSONString();

            JSONArray dataTask = new JSONArray();
            dataTask.add(0);
            dataTask.add(20);
            dataTask.add(0);
            String task = dataTask.toJSONString();

            JSONArray dataAchive = new JSONArray();
            for (AchivementTemplate a : AchiveManager.getInstance().getList()) {
                JSONObject jobj = new JSONObject();
                jobj.put("id", a.getId());
                jobj.put("count", 0);
                jobj.put("finish", 0);
                jobj.put("receive", 0);
                dataAchive.add(jobj);
            }
            String achive = dataAchive.toJSONString();

            String mabuEgg = "{}";

            JSONArray dataCharms = new JSONArray();
            dataCharms.add(0);
            dataCharms.add(0);
            dataCharms.add(0);
            dataCharms.add(0);
            dataCharms.add(0);
            dataCharms.add(0);
            dataCharms.add(0);
            dataCharms.add(0);
            dataCharms.add(0);
            dataCharms.add(0);
            String charms = dataCharms.toJSONString();

            int[] skillsArr = gender == 0 ? new int[]{0, 1, 6, 9, 10, 20, 22, 19, 24, 27, 28, 29}
                    : gender == 1 ? new int[]{2, 3, 7, 11, 12, 17, 18, 19, 26, 27, 28, 29}
                    : new int[]{4, 5, 8, 13, 14, 21, 23, 19, 25, 27, 28, 29};
            JSONArray dataSkills = new JSONArray();
            for (int i = 0; i < skillsArr.length; i++) {
                JSONArray skill = new JSONArray();
                skill.add(skillsArr[i]);
                skill.add(0);
                if (i == 0) {
                    skill.add(1); // level skill
                    
           //     } else if (i == 1) {
           //         skill.add(1); // level skill quen de  so 0 dcu day la so cap nhe
                    
//                } else if (i == 2) {
//                    skill.add(7); // level skill quen de  so 0 dcu day la so cap nhe
//                    
//                } else if (i == 3) {
//                    skill.add(7); // level skill quen de  so 0 dcu day la so cap nhe
//                    
//                } else if (i == 4) {
//                    skill.add(7); // level skill quen de  so 0 dcu day la so cap nhe
//                    
//                } else if (i == 5) {
//                    skill.add(7); // level skill quen de  so 0 dcu day la so cap nhe
//                } else if (i == 6) {       
//                    skill.add(7); // level skill quen de  so 0 dcu day la so cap nhe
//                } else if (i == 7) {
//                    skill.add(7); // level skill quen de  so 0 dcu day la so cap nhe
//                } else if (i == 8) {
//                    skill.add(9); // level skill quen de  so 0 dcu day la so cap nhe
//                } else if (i == 9) {
//                    skill.add(1); // level skill quen de  so 0 dcu day la so cap nhe    
//                } else if (i == 10) {
//                    skill.add(1); // level skill quen de  so 0 dcu day la so cap nhe  
//                } else if (i == 11) {
//                    skill.add(5); // level skill quen de  so 0 dcu day la so cap nhe   
//            } else if (i == 11) {
//                    skill.add(5); // level skill quen de  so 0 dcu day la so cap nhe   



                } else {
                    skill.add(0);
                }

                dataSkills.add(skill);
            }
            String skills = dataSkills.toJSONString();

            JSONArray dataSkillShortcut = new JSONArray();
            dataSkillShortcut.add(gender == 0 ? 0 : gender == 1 ? 2 : 4);
            for (int i = 0; i < 9; i++) {
                dataSkillShortcut.add(-1);
            }
            String skillsShortcut = dataSkillShortcut.toJSONString();

            String petInfo = "{}";
            String petPoint = "{}";
            String petBody = "[]";
            String petSkill = "[]";

            JSONArray dataBlackBall = new JSONArray();
            for (int i = 1; i <= 7; i++) {
                JSONArray arr = new JSONArray();
                arr.add(0);
                arr.add(0);
                dataBlackBall.add(arr);
            }
            String blackBall = dataBlackBall.toJSONString();

            ps = con.prepareStatement("insert into player"
                    + "(account_id, name, head, gender, have_tennis_space_ship, clan_id_sv" + Manager.SERVER + ", "
                    + "data_inventory, data_location, data_point, data_magic_tree, items_body, "
                    + "items_bag, items_box, items_box_lucky_round, friends, enemies, data_intrinsic, data_item_time,"
                    + "data_task, data_mabu_egg, data_charm, skills, skills_shortcut, pet_info, pet_point, pet_body, pet_skill,"
                    + "data_black_ball, thoi_vang, data_side_task,achivements, item_new_time, leothap, score_board) "
                    + "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

            ps.setInt(1, userId);
            ps.setString(2, name);
            ps.setInt(3, head);
            ps.setByte(4, gender);
            ps.setBoolean(5, false);
            ps.setInt(6, -1);
            ps.setString(7, inventory);
            ps.setString(8, location);
            ps.setString(9, point);
            ps.setString(10, magicTree);
            ps.setString(11, itemsBody);
            ps.setString(12, itemsBag);
            ps.setString(13, itemsBox);
            ps.setString(14, itemsBoxLuckyRound);
            ps.setString(15, friends);
            ps.setString(16, enemies);
            ps.setString(17, intrinsic);
            ps.setString(18, itemTime);
            ps.setString(19, task);
            ps.setString(20, mabuEgg);
            ps.setString(21, charms);
            ps.setString(22, skills);
            ps.setString(23, skillsShortcut);
            ps.setString(24, petInfo);
            ps.setString(25, petPoint);
            ps.setString(26, petBody);
            ps.setString(27, petSkill);
            ps.setString(28, blackBall);
            ps.setInt(29, 10); //gold bar
            ps.setString(30, "{}");
            ps.setString(31, achive);
            ps.setString(32, itemTimeNew);
            ps.setString(33, "[]");
            ps.setString(34, "[0]");
            ps.executeUpdate();
            Log.success("Tạo player mới thành công!");
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            try {
                if (con != null) {
                    con.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void updatePlayer(Player player, Connection connection) {
        if (player.isDisposed() || player.isSaving()) {
            return;
        }
        player.setSaving(true);
        try {
            int n1s = 0;
            int n2s = 0;
            int n3s = 0;
            int tv = 0;
            if (player.loaded) {
                long st = System.currentTimeMillis();
                try {
                    JSONArray dataInventory = new JSONArray();
                    //data kim lượng
                    dataInventory.add(player.inventory.gold > Inventory.LIMIT_GOLD
                            + (player.CapTamkjll >= 300 ? player.CapTamkjll / 5 * 1000000000L : 0)
                                    ? Inventory.LIMIT_GOLD
                                    + (player.CapTamkjll >= 300 ? player.CapTamkjll / 5 * 1000000000L : 0)
                                    : player.inventory.gold);
                    dataInventory.add(player.inventory.gem);
                    dataInventory.add(player.inventory.ruby);
                    dataInventory.add(player.inventory.goldLimit);
                    String inventory = dataInventory.toJSONString();

                    int mapId = -1;
                    mapId = player.mapIdBeforeLogout;
                    int x = player.location.x;
                    int y = player.location.y;
                    long hp = Util.limitgioihan(player.nPoint.hp);
                    long mp = Util.limitgioihan(player.nPoint.mp);
                    if (player.isDie()) {
                        mapId = player.gender + 21;
                        x = 300;
                        y = 336;
                        hp = 1;
                        mp = 1;
                    } else {
                        if (MapService.gI().isMapDoanhTrai(mapId)
                                || MapService.gI().isMapBlackBallWar(mapId)
                                || mapId == 126
                                || mapId == 164
                                || mapId == ConstMap.CON_DUONG_RAN_DOC
                                || mapId == ConstMap.CON_DUONG_RAN_DOC_142
                                || mapId == ConstMap.CON_DUONG_RAN_DOC_143
                                || mapId == ConstMap.HOANG_MAC) {
                            mapId = player.gender + 21;
                            x = 300;
                            y = 336;
                        }
                    }
                    //data vị trí
                    JSONArray dataLocation = new JSONArray();
                    dataLocation.add(x);
                    dataLocation.add(y);
                    dataLocation.add(mapId);
                    String location = dataLocation.toJSONString();
                    //data chỉ số

                    JSONArray dataPoint = new JSONArray();
                    dataPoint.add(0);
                    dataPoint.add(mp);
                    dataPoint.add(player.nPoint.mpg);
                    dataPoint.add(player.nPoint.critg);
                    dataPoint.add(player.nPoint.limitPower);
                    dataPoint.add(player.nPoint.stamina);
                    dataPoint.add(hp);
                    dataPoint.add(player.nPoint.defg);
                    dataPoint.add(player.nPoint.tiemNang);
                    dataPoint.add(player.nPoint.maxStamina);
                    dataPoint.add(player.nPoint.dameg);
                    dataPoint.add(player.nPoint.power);
                    dataPoint.add(player.nPoint.hpg);
                    String point = dataPoint.toJSONString();

                    //data đậu thần
                    JSONArray dataMagicTree = new JSONArray();
                    dataMagicTree.add(player.magicTree.isUpgrade ? 1 : 0);
                    dataMagicTree.add(player.magicTree.lastTimeUpgrade);
                    dataMagicTree.add(player.magicTree.level);
                    dataMagicTree.add(player.magicTree.lastTimeHarvest);
                    dataMagicTree.add(player.magicTree.currPeas);
                    String magicTree = dataMagicTree.toJSONString();

                    //data body
                    JSONArray dataBody = new JSONArray();
                    for (Item item : player.inventory.itemsBody) {
                        JSONObject dataItem = new JSONObject();
                        if (item.isNotNullItem()) {
                            JSONArray options = new JSONArray();
                            dataItem.put("temp_id", item.template.id);
                            dataItem.put("quantity", item.quantity);
                            dataItem.put("create_time", item.createTime);
                            for (ItemOption io : item.itemOptions) {
                                JSONArray option = new JSONArray();
                                option.add(io.optionTemplate.id);
                                option.add(io.param);
                                options.add(option);
                            }
                            dataItem.put("option", options);
                        } else {
                            JSONArray options = new JSONArray();
                            dataItem.put("temp_id", -1);
                            dataItem.put("quantity", 0);
                            dataItem.put("create_time", 0);
                            dataItem.put("option", options);
                        }
                        dataBody.add(dataItem);
                    }
                    String itemsBody = dataBody.toJSONString();

                    //data bag
                    JSONArray dataBag = new JSONArray();
                    for (Item item : player.inventory.itemsBag) {
                        JSONObject dataItem = new JSONObject();
                        if (item.isNotNullItem()) {
                            JSONArray options = new JSONArray();
                            switch (item.template.id) {
                                case 14:
                                    n1s += item.quantity;
                                    break;
                                case 15:
                                    n2s += item.quantity;
                                    break;
                                case 16:
                                    n3s += item.quantity;
                                    break;
                                case 1404:
                                    tv += item.quantity;
                                    break;
                            }
                            dataItem.put("temp_id", item.template.id);
                            dataItem.put("quantity", item.quantity);
                            dataItem.put("create_time", item.createTime);

                            for (ItemOption io : item.itemOptions) {
                                JSONArray option = new JSONArray();
                                option.add(io.optionTemplate.id);
                                option.add(io.param);
                                options.add(option);
                            }
                            dataItem.put("option", options);
                        } else {
                            JSONArray options = new JSONArray();
                            dataItem.put("temp_id", -1);
                            dataItem.put("quantity", 0);
                            dataItem.put("create_time", 0);
                            dataItem.put("option", options);
                        }
                        dataBag.add(dataItem);
                    }
                    String itemsBag = dataBag.toJSONString();

                    //data box
                    JSONArray dataBox = new JSONArray();
                    for (Item item : player.inventory.itemsBox) {
                        JSONObject dataItem = new JSONObject();
                        if (item.isNotNullItem()) {
                            JSONArray options = new JSONArray();
                            switch (item.template.id) {
                                case 14:
                                    n1s += item.quantity;
                                    break;
                                case 15:
                                    n2s += item.quantity;
                                    break;
                                case 16:
                                    n3s += item.quantity;
                                    break;
                                case 1404:
                                    tv += item.quantity;
                                    break;
                            }
                            dataItem.put("temp_id", item.template.id);
                            dataItem.put("quantity", item.quantity);
                            dataItem.put("create_time", item.createTime);

                            for (ItemOption io : item.itemOptions) {
                                JSONArray option = new JSONArray();
                                option.add(io.optionTemplate.id);
                                option.add(io.param);
                                options.add(option);
                            }
                            dataItem.put("option", options);
                        } else {
                            JSONArray options = new JSONArray();
                            dataItem.put("temp_id", -1);
                            dataItem.put("quantity", 0);
                            dataItem.put("create_time", 0);
                            dataItem.put("option", options);
                        }
                        dataBox.add(dataItem);
                    }
                    String itemsBox = dataBox.toJSONString();

                    //data box crack ball
                    JSONArray dataCrackBall = new JSONArray();
                    for (Item item : player.inventory.itemsBoxCrackBall) {
                        JSONObject dataItem = new JSONObject();
                        if (item.isNotNullItem()) {
                            dataItem.put("temp_id", item.template.id);
                            dataItem.put("quantity", item.quantity);
                            dataItem.put("create_time", item.createTime);
                            JSONArray options = new JSONArray();
                            for (ItemOption io : item.itemOptions) {
                                JSONArray option = new JSONArray();
                                option.add(io.optionTemplate.id);
                                option.add(io.param);
                                options.add(option);
                            }
                            dataItem.put("option", options);
                        } else {
                            JSONArray options = new JSONArray();
                            dataItem.put("temp_id", -1);
                            dataItem.put("quantity", 0);
                            dataItem.put("create_time", 0);
                            dataItem.put("option", options);
                        }
                        dataCrackBall.add(dataItem);
                    }
                    String itemsBoxLuckyRound = dataCrackBall.toJSONString();

                    //data bạn bè
                    JSONArray dataFriends = new JSONArray();
                    for (Friend f : player.friends) {
                        JSONObject friend = new JSONObject();
                        friend.put("id", f.id);
                        friend.put("name", f.name);
                        friend.put("power", f.power);
                        friend.put("head", f.head);
                        friend.put("body", f.body);
                        friend.put("leg", f.leg);
                        friend.put("bag", f.bag);
                        dataFriends.add(friend);
                    }
                    String friend = dataFriends.toJSONString();

                    //data kẻ thù
                    JSONArray dataEnemies = new JSONArray();
                    for (Friend e : player.enemies) {
                        JSONObject enemy = new JSONObject();
                        enemy.put("id", e.id);
                        enemy.put("name", e.name);
                        enemy.put("power", e.power);
                        enemy.put("head", e.head);
                        enemy.put("body", e.body);
                        enemy.put("leg", e.leg);
                        enemy.put("bag", e.bag);
                        dataEnemies.add(enemy);
                    }
                    String enemy = dataEnemies.toJSONString();

                    //data nội tại
                    JSONArray dataIntrinsic = new JSONArray();
                    dataIntrinsic.add(player.playerIntrinsic.intrinsic.id);
                    dataIntrinsic.add(player.playerIntrinsic.intrinsic.param1);
                    dataIntrinsic.add(player.playerIntrinsic.countOpen);
                    dataIntrinsic.add(player.playerIntrinsic.intrinsic.param2);
                    String intrinsic = dataIntrinsic.toJSONString();

                    //data item time
                    JSONArray dataItemTime = new JSONArray();
                    dataItemTime.add(player.itemTime.isUseBoKhi ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeBoKhi)) : 0);
                    dataItemTime.add(player.itemTime.isUseAnDanh ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeAnDanh)) : 0);
                    dataItemTime.add(player.itemTime.isOpenPower ? (ItemTime.TIME_OPEN_POWER - (System.currentTimeMillis() - player.itemTime.lastTimeOpenPower)) : 0);
                    dataItemTime.add(player.itemTime.isUseCuongNo ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeCuongNo)) : 0);
                    dataItemTime.add(player.itemTime.isUseMayDo ? (ItemTime.TIME_MAY_DO - (System.currentTimeMillis() - player.itemTime.lastTimeUseMayDo)) : 0);

                    dataItemTime.add(player.itemTime.isUseBoHuyet ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeBoHuyet)) : 0);
                    dataItemTime.add(player.itemTime.iconMeal);
                    dataItemTime.add(player.itemTime.isEatMeal ? (ItemTime.TIME_EAT_MEAL - (System.currentTimeMillis() - player.itemTime.lastTimeEatMeal)) : 0);
                    dataItemTime.add(player.itemTime.isUseGiapXen ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeGiapXen)) : 0);
                    dataItemTime.add(player.itemTime.isUseBanhChung ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeBanhChung)) : 0);
                    dataItemTime.add(player.itemTime.isUseBanhTet ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeBanhTet)) : 0);
                    dataItemTime.add(player.itemTime.isUseBoKhi2 ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeBoKhi2)) : 0);
                    dataItemTime.add(player.itemTime.isUseGiapXen2 ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeGiapXen2)) : 0);
                    dataItemTime.add(player.itemTime.isUseCuongNo2 ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeCuongNo2)) : 0);
                    dataItemTime.add(player.itemTime.isUseBoHuyet2 ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeBoHuyet2)) : 0);
                    dataItemTime.add(player.itemTime.isUseMayDoskh ? (ItemTime.TIME_MAY_DO - (System.currentTimeMillis() - player.itemTime.lastTimeUseMayDoskh)) : 0);
                    String itemTime = dataItemTime.toJSONString();

                    JSONArray data_exp_tutienn = new JSONArray();
                    data_exp_tutienn.add((player.itemTime.isXexpTamkjll1_5
                            ? (ItemTime.TIME_X_EXP - (System.currentTimeMillis() - player.itemTime.lastTimeexpTamkjll1_5))
                            : 0));
                    data_exp_tutienn.add((player.itemTime.isXexpTamkjll2
                            ? (ItemTime.TIME_X_EXP - (System.currentTimeMillis() - player.itemTime.lastTimeexpTamkjll2))
                            : 0));
                    data_exp_tutienn.add((player.itemTime.isXexpTamkjll3
                            ? (ItemTime.TIME_X_EXP - (System.currentTimeMillis() - player.itemTime.lastTimeexpTamkjll3))
                            : 0));
                    data_exp_tutienn.add((player.itemTime.isXexpTamkjll4
                            ? (ItemTime.TIME_X_EXP - (System.currentTimeMillis() - player.itemTime.lastTimeexpTamkjll4))
                            : 0));
                    data_exp_tutienn.add((player.itemTime.isXexpTamkjll5
                            ? (ItemTime.TIME_X_EXP - (System.currentTimeMillis() - player.itemTime.lastTimeexpTamkjll5))
                            : 0));
                    data_exp_tutienn.add((player.itemTime.isXexpTamkjll6
                            ? (ItemTime.TIME_X_EXP - (System.currentTimeMillis() - player.itemTime.lastTimeexpTamkjll6))
                            : 0));
                    String data_exp_tutien = data_exp_tutienn.toJSONString();

                    JSONArray dataNuocMia = new JSONArray();
                    dataNuocMia.add((player.itemTime.isnuocmiakhonglo ? (ItemTime.TIME_NUOC_MIA - (System.currentTimeMillis() - player.itemTime.lastnuocmiakhonglo)) : 0));
                    dataNuocMia.add((player.itemTime.isnuocmiathom ? (ItemTime.TIME_NUOC_MIA - (System.currentTimeMillis() - player.itemTime.lastnuocmiathom)) : 0));
                    dataNuocMia.add((player.itemTime.isnuocmiasaurieng ? (ItemTime.TIME_NUOC_MIA - (System.currentTimeMillis() - player.itemTime.lastnuocmiasaurieng)) : 0));
                    String Nuoc_mia = dataNuocMia.toJSONString();

                    JSONArray dataDiemdanh = new JSONArray();
                    dataDiemdanh.add(player.CheckDayOnl);
                    dataDiemdanh.add(player.diemdanh);
                    String diemdanh = dataDiemdanh.toJSONString();
                    dataDiemdanh.clear();

                    JSONArray dataItemNew = new JSONArray();
                    dataItemNew.add(player.itemTime.isDuoiKhi ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeDuoiKhi)) : 0); // sai index này
                    dataItemNew.add(player.itemTime.isBanhTrungThu1Trung ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeBanhTrungThu1Trung)) : 0);
                    dataItemNew.add(player.itemTime.isBanhTrungThu2Trung ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeBanhTrungThu2Trung)) : 0);
                    dataItemNew.add(player.itemTime.isBanhTrungThuDacBiet ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeBanhTrungThuDacBiet)) : 0);
                    dataItemNew.add(player.itemTime.isBanhTrungThuGaQuay ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeBanhTrungThuGaQuay)) : 0);
                    dataItemNew.add(player.itemTime.isBanhTrungThuThapCam ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeBanhTrungThuThapCam)) : 0);
                    dataItemNew.add(player.itemTime.ismytom ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeMytom)) : 0);
                     dataItemNew.add(player.itemTime.isthanhkiem ? (ItemTime.TIME_THANHKIEM - (System.currentTimeMillis() - player.itemTime.lastTimeThanhkiem)) : 0);
                     dataItemNew.add(player.itemTime.ishokiem ? (ItemTime.TIME_HOKIEM - (System.currentTimeMillis() - player.itemTime.lastTimeHokiem)) : 0);
                     dataItemNew.add(player.itemTime.isbinhx2 ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimebinhx2)) : 0);
                     dataItemNew.add(player.itemTime.isbinhx3 ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimebinhx3)) : 0);
                     dataItemNew.add(player.itemTime.isbinhx4 ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimebinhx4)) : 0);
                     dataItemNew.add(player.itemTime.ismaydotainguyen ? (ItemTime.TIME_30P - (System.currentTimeMillis() - player.itemTime.lastTimetainguyen)) : 0);
                     dataItemNew.add(player.itemTime.ismaydotrangbi ? (ItemTime.TIME_30P - (System.currentTimeMillis() - player.itemTime.lastTimetrangbi)) : 0);
                     dataItemNew.add(player.itemTime.ismaydosaophale ? (ItemTime.TIME_30P - (System.currentTimeMillis() - player.itemTime.lastTimesaophale)) : 0);
                    dataItemNew.add(player.itemTime.ismaydothucan ? (ItemTime.TIME_30P - (System.currentTimeMillis() - player.itemTime.lastTimethucan)) : 0);
                    dataItemNew.add(player.itemTime.ismaydongocrong ? (ItemTime.TIME_30P - (System.currentTimeMillis() - player.itemTime.lastTimengocrong)) : 0);
                    
                    String itemTimeNew = dataItemNew.toJSONString();

                    JSONArray data_diemne = new JSONArray();
                    data_diemne.add(player.vip);
                    data_diemne.add(player.timevip);
                    String data_diem = data_diemne.toJSONString();
                    data_diemne.clear();

                    //data nhiệm vụ
                    JSONArray dataTask = new JSONArray();
                    dataTask.add(player.playerTask.taskMain.subTasks.get(player.playerTask.taskMain.index).count);
                    dataTask.add(player.playerTask.taskMain.id);
                    dataTask.add(player.playerTask.taskMain.index);
                    String task = dataTask.toJSONString();

                    //data nhiệm vụ hàng ngày
                    JSONArray dataSideTask = new JSONArray();
                    dataSideTask.add(player.playerTask.sideTask.level);
                    dataSideTask.add(player.playerTask.sideTask.count);
                    dataSideTask.add(player.playerTask.sideTask.leftTask);
                    dataSideTask.add(player.playerTask.sideTask.template != null ? player.playerTask.sideTask.template.id : -1);
                    dataSideTask.add(player.playerTask.sideTask.receivedTime);
                    dataSideTask.add(player.playerTask.sideTask.maxCount);
                    String sideTask = dataSideTask.toJSONString();

                    JSONArray dataAchive = new JSONArray();
                    for (Achivement a : player.playerTask.achivements) {
                        JSONObject jobj = new JSONObject();
                        jobj.put("id", a.getId());
                        jobj.put("count", a.getCount());
                        jobj.put("finish", a.isFinish() ? 1 : 0);
                        jobj.put("receive", a.isReceive() ? 1 : 0);
                        dataAchive.add(jobj);
                    }
                    String achive = dataAchive.toJSONString();

                    //data trứng bư
                    JSONObject dataMaBu = new JSONObject();
                    if (player.mabuEgg != null) {
                        dataMaBu.put("create_time", player.mabuEgg.lastTimeCreate);
                        dataMaBu.put("time_done", player.mabuEgg.timeDone);
                    }
                    String mabuEgg = dataMaBu.toJSONString();
                    //data bill
                    JSONObject dataBill = new JSONObject();
                    if (player.billEgg != null) {
                        dataBill.put("create_time", player.billEgg.lastTimeCreate);
                        dataBill.put("time_done", player.billEgg.timeDone);
                    }
                    String billEgg = dataBill.toJSONString();

                    //data duahau
                    JSONObject dataduahau = new JSONObject();
                    if (player.duahau != null) {
                        dataduahau.put("create_time", player.duahau.lastTimeCreate);
                        dataduahau.put("time_done", player.duahau.timeDone);
                    }
                    String duahau = dataduahau.toJSONString();
                    //data kickvip
                    JSONObject kickvip = new JSONObject();
                    if (player.kickvip != null) {
                        kickvip.put("create_time", player.kickvip.lastTimeCreate);
                        kickvip.put("time_done", player.kickvip.timeDone);
                    }
                    String kickgoivip = kickvip.toJSONString();
                    JSONArray reward_mocnap = new JSONArray();
                    reward_mocnap.add(player.mocnap1 == true ? 1 : 0);
                    reward_mocnap.add(player.mocnap2 == true ? 1 : 0);
                    reward_mocnap.add(player.mocnap3 == true ? 1 : 0);
                    reward_mocnap.add(player.mocnap4 == true ? 1 : 0);
                    reward_mocnap.add(player.mocnap5 == true ? 1 : 0);
                    String data_mocnap = reward_mocnap.toJSONString();
                    reward_mocnap.clear();
                    //data bùa
                    JSONArray dataCharms = new JSONArray();
                    dataCharms.add(player.charms.tdTriTue);
                    dataCharms.add(player.charms.tdManhMe);
                    dataCharms.add(player.charms.tdDaTrau);
                    dataCharms.add(player.charms.tdOaiHung);
                    dataCharms.add(player.charms.tdBatTu);
                    dataCharms.add(player.charms.tdDeoDai);
                    dataCharms.add(player.charms.tdThuHut);
                    dataCharms.add(player.charms.tdDeTu);
                    dataCharms.add(player.charms.tdTriTue3);
                    dataCharms.add(player.charms.tdTriTue4);
                    dataCharms.add(player.charms.tdDeTuMabu);
                    String charm = dataCharms.toJSONString();

                    //data skill
                    JSONArray dataSkills = new JSONArray();
                    for (Skill skill : player.playerSkill.skills) {
                        JSONArray dataskill = new JSONArray();
                        dataskill.add(skill.template.id);
                        dataskill.add(skill.lastTimeUseThisSkill);
                        dataskill.add(skill.point);
                        dataSkills.add(dataskill);
                    }
                    String skills = dataSkills.toJSONString();

                    JSONArray dataSkillShortcut = new JSONArray();
                    //data skill shortcut
                    for (int skillId : player.playerSkill.skillShortCut) {
                        dataSkillShortcut.add(skillId);
                    }
                    String skillShortcut = dataSkillShortcut.toJSONString();

                    JSONObject jPetInfo = new JSONObject();
                    JSONObject jPetPoint = new JSONObject();
                    JSONArray jPetBody = new JSONArray();
                    JSONArray jPetSkills = new JSONArray();
                    String petInfo = jPetInfo.toJSONString();
                    String petPoint = jPetPoint.toJSONString();
                    String petBody = jPetBody.toJSONString();
                    String petSkill = jPetSkills.toJSONString();

                    JSONArray dataChallenge = new JSONArray();
                    dataChallenge.add(player.goldChallenge);
                    dataChallenge.add(player.levelWoodChest);
                    dataChallenge.add(player.receivedWoodChest ? 1 : 0);
                    String challenge = dataChallenge.toJSONString();

                    JSONArray dataSuKienTet = new JSONArray();
                    dataSuKienTet.add(player.event.getTimeCookTetCake());
                    dataSuKienTet.add(player.event.getTimeCookChungCake());
                    dataSuKienTet.add(player.event.isCookingTetCake() ? 1 : 0);
                    dataSuKienTet.add(player.event.isCookingChungCake() ? 1 : 0);
                    dataSuKienTet.add(player.event.isReceivedLuckyMoney() ? 1 : 0);
                    String skTet = dataSuKienTet.toJSONString();

                    JSONArray dataBuyLimit = new JSONArray();
                    for (int i = 0; i < player.buyLimit.length; i++) {
                        dataBuyLimit.add(player.buyLimit[i]);
                    }
                    String buyLimit = dataBuyLimit.toJSONString();

                    JSONArray dataRwLimit = new JSONArray();
                    for (int i = 0; i < player.getRewardLimit().length; i++) {
                        dataRwLimit.add(player.getRewardLimit()[i]);
                    }
                    String rwLimit = dataRwLimit.toJSONString();

                    //data pet
                    if (player.pet != null) {
                        jPetInfo.put("name", player.pet.name);
                        jPetInfo.put("gender", player.pet.gender);
                        jPetInfo.put("is_mabu", player.pet.typePet);
                        jPetInfo.put("status", player.pet.status);
                        jPetInfo.put("type_fusion", player.fusion.typeFusion);
                        jPetInfo.put("level", player.pet.getLever());
                        int timeLeftFusion = (int) (Fusion.TIME_FUSION - (System.currentTimeMillis() - player.fusion.lastTimeFusion));
                        jPetInfo.put("left_fusion", timeLeftFusion < 0 ? 0 : timeLeftFusion);
                        petInfo = jPetInfo.toJSONString();

                        jPetPoint.put("power", player.pet.nPoint.power);
                        jPetPoint.put("tiem_nang", player.pet.nPoint.tiemNang);
                        jPetPoint.put("stamina", player.pet.nPoint.stamina);
                        jPetPoint.put("max_stamina", player.pet.nPoint.maxStamina);
                        jPetPoint.put("hpg", player.pet.nPoint.hpg);
                        jPetPoint.put("mpg", player.pet.nPoint.mpg);
                        jPetPoint.put("damg", player.pet.nPoint.dameg);
                        jPetPoint.put("defg", player.pet.nPoint.defg);
                        jPetPoint.put("critg", player.pet.nPoint.critg);
                        jPetPoint.put("limit_power", player.pet.nPoint.limitPower);
                        jPetPoint.put("hp", player.pet.nPoint.hp);
                        jPetPoint.put("mp", player.pet.nPoint.mp);
                        petPoint = jPetPoint.toJSONString();

                        for (Item item : player.pet.inventory.itemsBody) {
                            JSONObject dataItem = new JSONObject();
                            if (item.isNotNullItem()) {
                                dataItem.put("temp_id", item.template.id);
                                dataItem.put("quantity", item.quantity);
                                dataItem.put("create_time", item.createTime);
                                JSONArray options = new JSONArray();
                                for (ItemOption io : item.itemOptions) {
                                    JSONArray option = new JSONArray();
                                    option.add(io.optionTemplate.id);
                                    option.add(io.param);
                                    options.add(option);
                                }
                                dataItem.put("option", options);
                            } else {
                                JSONArray options = new JSONArray();
                                dataItem.put("temp_id", -1);
                                dataItem.put("quantity", 0);
                                dataItem.put("create_time", 0);
                                dataItem.put("option", options);
                            }
                            jPetBody.add(dataItem);
                        }
                        petBody = jPetBody.toJSONString();

                        for (Skill s : player.pet.playerSkill.skills) {
                            JSONArray pskill = new JSONArray();
                            if (s.skillId != -1) {
                                pskill.add(s.template.id);
                                pskill.add(s.point);
                            } else {
                                pskill.add(-1);
                                pskill.add(0);
                            }
                            jPetSkills.add(pskill);
                        }
                        petSkill = jPetSkills.toJSONString();
                    }
                    JSONArray TamkjllTuMaNe = new JSONArray();
                    TamkjllTuMaNe.add(player.Tamkjlltutien[0]);
                    TamkjllTuMaNe.add(player.Tamkjlltutien[1]);
                    TamkjllTuMaNe.add(player.Tamkjlltutien[2]);
                    String Tamkjlltutien = TamkjllTuMaNe.toJSONString();
                    TamkjllTuMaNe.clear();
                    if (player.Tamkjll_Ma_cot > 0) {
                        TamkjllTuMaNe.add(player.Tamkjll_Tu_Ma);
                        TamkjllTuMaNe.add(player.Tamkjll_Exp_Tu_Ma);
                        TamkjllTuMaNe.add(player.Tamkjll_Ma_Hoa);
                        TamkjllTuMaNe.add(player.TamkjllLasttimeMaHoa);
                        TamkjllTuMaNe.add(player.Tamkjll_Ma_cot);
                    }
                    String TamkjllTuMa = TamkjllTuMaNe.toJSONString();
                    TamkjllTuMaNe.clear();

                    JSONArray TamkjllDauLa = new JSONArray();
                    TamkjllDauLa.add(player.TamkjllDauLaDaiLuc[0]);
                    TamkjllDauLa.add(player.TamkjllDauLaDaiLuc[1]);
                    TamkjllDauLa.add(player.TamkjllDauLaDaiLuc[2]);
                    TamkjllDauLa.add(player.TamkjllDauLaDaiLuc[3]);
                    TamkjllDauLa.add(player.TamkjllDauLaDaiLuc[4]);
                    TamkjllDauLa.add(player.TamkjllDauLaDaiLuc[5]);
                    TamkjllDauLa.add(player.TamkjllDauLaDaiLuc[6]);
                    TamkjllDauLa.add(player.TamkjllDauLaDaiLuc[7]);
                    TamkjllDauLa.add(player.TamkjllDauLaDaiLuc[8]);
                    TamkjllDauLa.add(player.TamkjllDauLaDaiLuc[9]);
                    TamkjllDauLa.add(player.TamkjllDauLaDaiLuc[10]);
                    TamkjllDauLa.add(player.TamkjllDauLaDaiLuc[11]);
                    TamkjllDauLa.add(player.TamkjllDauLaDaiLuc[12]);
                    TamkjllDauLa.add(player.TamkjllDauLaDaiLuc[13]);
                    TamkjllDauLa.add(player.TamkjllDauLaDaiLuc[14]);
                    TamkjllDauLa.add(player.TamkjllDauLaDaiLuc[15]);
                    TamkjllDauLa.add(player.TamkjllDauLaDaiLuc[16]);
                    TamkjllDauLa.add(player.TamkjllDauLaDaiLuc[17]);
                    TamkjllDauLa.add(player.TamkjllDauLaDaiLuc[18]);
                    TamkjllDauLa.add(player.TamkjllDauLaDaiLuc[19]);
                    TamkjllDauLa.add(player.TamkjllDauLaDaiLuc[20]);
                    String TamkjllDLDL = TamkjllDauLa.toJSONString();
                    TamkjllDauLa.clear();
                    JSONArray Tamkjll_PetNe = new JSONArray();
                    if (player.TamkjllPetGiong >= 0 && player.TamkjllPetGiong <= 10) {
                        Tamkjll_PetNe.add(player.TamkjllPetGiong);
                        Tamkjll_PetNe.add(player.TamkjllNamePet);
                        Tamkjll_PetNe.add(player.TamkjllPetHunger);
                        Tamkjll_PetNe.add(player.TamkjllPetPower);
                        Tamkjll_PetNe.add(player.TamkjlllastTimeThucan);
                        Tamkjll_PetNe.add(player.TamkjlllevelPet);
                        Tamkjll_PetNe.add(player.TamkjllExpPet);
                    } else {
                        Tamkjll_PetNe.add(-1);
                    }
                    String Tamkjll_Pet = Tamkjll_PetNe.toJSONString();
                    Tamkjll_PetNe.clear();

                    JSONArray dataBlackBall = new JSONArray();
                    //data thưởng ngọc rồng đen
                    for (int i = 1; i <= 7; i++) {
                        JSONArray data = new JSONArray();
                        data.add(player.rewardBlackBall.timeOutOfDateReward[i - 1]);
                        data.add(player.rewardBlackBall.lastTimeGetReward[i - 1]);
                        dataBlackBall.add(data);
                    }
                    String blackBall = dataBlackBall.toJSONString();
                    JSONArray dataScoreBoard = new JSONArray();
                    for(int i = 0;i<player.scoreBoards.length;i++){
                        dataScoreBoard.add(player.scoreBoards[i]);
                    }
                    String scoreBoards = dataScoreBoard.toJSONString();
                    JSONArray dataLeoThap = new JSONArray();
                    dataLeoThap.add(player.tangThap);
                    dataLeoThap.add(player.levelThap);
                    dataLeoThap.add(player.pointThap);
                    String leoThap = dataLeoThap.toJSONString();
                    Gson gson = new Gson();
                    PreparedStatement ps = null;
                    try {
                        ps = connection.prepareStatement("update player set head = ?, have_tennis_space_ship = ?,"
                                + "clan_id_sv" + Manager.SERVER + " = ?, data_inventory = ?, data_location = ?, data_point = ?, data_magic_tree = ?,"
                                + "items_body = ?, items_bag = ?, items_box = ?, items_box_lucky_round = ?, friends = ?,"
                                + "enemies = ?, data_intrinsic = ?, data_item_time = ?, data_task = ?, data_mabu_egg = ?,"
                                + "pet_info = ?, pet_point = ?, pet_body = ?, pet_skill = ? , power = ?, pet_power = ?, "
                                + "data_black_ball = ?, data_side_task = ?, data_charm = ?, skills = ?, skills_shortcut = ?,"
                                + "thoi_vang = ?, 1sao = ?, 2sao = ?, 3sao = ?, collection_book = ?, event_point = ?, firstTimeLogin = ?,"
                                + " challenge = ?, sk_tet = ?, buy_limit = ?, moc_nap = ?,achivements = ? , reward_limit = ?, item_new_time = ?, data_bill_egg = ? , pointsukien = ? , data_duahau = ? , poingapthu = ?,kickvip = ? ,diemdanh = ? , pointdiemdanh  = ? , reward_mocnap = ?, point_sb = ? ,Nuoc_mia = ?,CapTamkjll = ?, ExpTamkjll= ?, TamkjllCapPb= ?,TamkjllCS= ?, LbTamkjll= ?, DLbTamkjll= ?, TamkjllThomo= ?, TamkjllThomoExp= ?,"
                                + "Tamkjlltutien= ?, TamkjllDLDL= ?, Tamkjll_Pet= ?, Tamkjll_Tu_Ma= ?,data_exp_tutien = ?, data_diem=?,pointPvp = ?,"
                                + " isbienhinh =?,DoneVoDaiBaHatMit = ? ,dameBoss = ?, Diemfam = ?, diemsk = ?, diemsanbat = ?, mocnap = ? ,Nrosieucap = ?, Tindung = ?,Naptuan = ?,Napthang = ?, leothap = ?, Diemvip = ?, Cfpass = ?, Cfpremium = ?, mocpass = ?, mocpassvip = ?,diemshe = ?,dakethon = ?,duockethon = ?, score_board = ? where id = ?");

                        ps.setShort(1, player.head);
                        ps.setBoolean(2, player.haveTennisSpaceShip);
                        ps.setShort(3, (short) (player.clan != null ? player.clan.id : -1));
                        ps.setString(4, inventory);
                        ps.setString(5, location);
                        ps.setString(6, point);
                        ps.setString(7, magicTree);
                        ps.setString(8, itemsBody);
                        ps.setString(9, itemsBag);
                        ps.setString(10, itemsBox);
                        ps.setString(11, itemsBoxLuckyRound);
                        ps.setString(12, friend);
                        ps.setString(13, enemy);
                        ps.setString(14, intrinsic);
                        ps.setString(15, itemTime);
                        ps.setString(16, task);
                        ps.setString(17, mabuEgg);
                        ps.setString(18, petInfo);
                        ps.setString(19, petPoint);
                        ps.setString(20, petBody);
                        ps.setString(21, petSkill);
                        ps.setLong(22, player.nPoint.power);
                        ps.setLong(23, player.pet != null ? player.pet.nPoint.power : 0);
                        ps.setString(24, blackBall);
                        ps.setString(25, sideTask);
                        ps.setString(26, charm);
                        ps.setString(27, skills);
                        ps.setString(28, skillShortcut);
                        ps.setInt(29, tv);
                        ps.setInt(30, n1s);
                        ps.setInt(31, n2s);
                        ps.setInt(32, n3s);
                        ps.setString(33, gson.toJson(player.getCollectionBook().getCards()));
                        ps.setInt(34, player.event.getEventPoint());
                        ps.setString(35, Util.toDateString(player.firstTimeLogin));
                        ps.setString(36, challenge);
                        ps.setString(37, skTet);
                        ps.setString(38, buyLimit);
                        ps.setInt(39, player.event.getMocNapDaNhan());
                        ps.setString(40, achive);
                        ps.setString(41, rwLimit);
                        ps.setString(42, itemTimeNew);
                        ps.setString(43, billEgg);
                        ps.setInt(44, player.pointsukien);
                        ps.setString(45, duahau);
                        ps.setInt(46, player.gtPoint);
                        ps.setString(47, kickgoivip);
                        ps.setString(48, diemdanh);
                        ps.setInt(49, player.pointidemdanh);
                        ps.setString(50, data_mocnap);
                        ps.setInt(51, (int) player.point_sb);
                        ps.setString(52, Nuoc_mia);
                        ps.setInt(53, (int) player.CapTamkjll);
                        ps.setInt(54, (int) player.ExpTamkjll);
                        ps.setInt(55, (int) player.TamkjllCapPb);
                        ps.setInt(56, (int) player.TamkjllCS);
                        ps.setInt(57, (int) player.LbTamkjll);
                        ps.setInt(58, (int) player.DLbTamkjll);
                        ps.setInt(59, (int) player.TamkjllThomo);
                        ps.setInt(60, (int) player.TamkjllThomoExp);
                        ps.setString(61, Tamkjlltutien);
                        ps.setString(62, TamkjllDLDL);
                        ps.setString(63, Tamkjll_Pet);
                        ps.setString(64, TamkjllTuMa);
                        ps.setString(65, data_exp_tutien);
                        ps.setString(66, data_diem);
                        ps.setInt(67, (int) player.pointPvp);
                        ps.setInt(68, (int) player.isbienhinh);
                        ps.setInt(69, (int) player.DoneVoDaiBaHatMit);
                        ps.setDouble(70, player.dameBoss);
                        ps.setInt(71, (int) player.diemfam);
                        ps.setInt(72, (int) player.diemsk);
                        ps.setInt(73, (int) player.diemsanbat);
                        ps.setInt(74, (int) player.mocnap);
                        ps.setInt(75, (int) player.Nrosieucap);
                        ps.setInt(76, (int) player.Tindung);
                        ps.setInt(77, (int) player.naptuan);
                        ps.setInt(78, (int) player.napthang);
                        ps.setString(79, leoThap);
                        ps.setInt(80, (int) player.Diemvip);
                         ps.setInt(81, (int) player.cfpass);
                         ps.setInt(82, (int) player.premium);
                         ps.setInt(83, (int) player.mocpass);
                          ps.setInt(84, (int) player.mocpassvip);
                           ps.setInt(85, (int) player.diemshe);
                           ps.setInt(86, (int) player.dakethon); 
                            ps.setInt(87, (int) player.duockethon); 
                            ps.setString(88,scoreBoards);
                        ps.setInt(89, (int) player.id);
                        ps.executeUpdate();
                        if (updateTimeLogout) {
                            AccountDAO.updateAccoutLogout(player.getSession());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            ps.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    Log.error(PlayerDAO.class, e, "Lỗi save player " + player.name);
                } finally {

                }
            }
        } finally {
            player.setSaving(false);
        }
    }

    public static void saveName(Player player) {
        PreparedStatement ps = null;
        try (Connection con = DBService.gI().getConnectionForSaveData();) {
            ps = con.prepareStatement("update player set name = ? where id = ?");
            ps.setString(1, player.name);
            ps.setInt(2, (int) player.id);
            ps.executeUpdate();
        } catch (Exception e) {
        } finally {
            try {
                ps.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void saveisBienHinh(Player player) {
        PreparedStatement ps = null;
        try (Connection con = DBService.gI().getConnectionForSaveData();) {
            ps = con.prepareStatement("update player set isbienhinh = ? where id = ?");
            ps.setInt(1, player.isbienhinh);
            ps.setInt(2, (int) player.id);
            ps.executeUpdate();
        } catch (Exception e) {
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                Log.error(PlayerDAO.class, e);
            }
        }
    }

    public static boolean isExistName(String name) {
        boolean exist = false;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try (Connection con = DBService.gI().getConnectionForGame();) {
            ps = con.prepareStatement("select * from player where name = ?");
            ps.setString(1, name);
            rs = ps.executeQuery();
            if (rs.next()) {
                exist = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                ps.close();
                rs.close();
            } catch (SQLException ex) {
                java.util.logging.Logger.getLogger(AccountDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return exist;
    }

    public static boolean subVND2(Player player, int ruby) {
        try (Connection con = DBService.gI().getConnectionForSaveData(); PreparedStatement ps = con.prepareStatement("UPDATE account SET vnd = (vnd - ?), active = ? WHERE id = ?")) {
            if (!player.getSession().actived) {
                player.getSession().actived = true;
            }
            ps.setInt(1, ruby);
            ps.setInt(2, player.getSession().actived ? 1 : 0);
            ps.setInt(3, player.getSession().userId);
            ps.executeUpdate();
            player.getSession().vnd -= ruby;
            return true;
        } catch (SQLException e) {
            Log.error(PlayerDAO.class, e, "Lỗi update vnd " + player.name);
        }
        return false;
    }

    public static boolean subvnd(Player player, int num) {
        PreparedStatement ps = null;
        try (Connection con = DBService.gI().getConnection();) {
            ps = con.prepareStatement("update account set vnd = (vnd - ?), active = ? where id = ?");
            ps.setInt(1, num);
            ps.setInt(2, player.getSession().actived ? 1 : 0);
            ps.setInt(3, player.getSession().userId);
            ps.executeUpdate();
            ps.close();
            player.getSession().vnd -= num;
        } catch (Exception e) {
            Log.error(PlayerDAO.class, e, "Lỗi update vnd" + player.name);
            return false;
        } finally {
        }
//        if (num > 100000) {
//            insertHistoryGold(player, num);
//        }
        return true;
    }
    
    public static boolean update(Player player, int num) {
        PreparedStatement ps = null;
        try (Connection con = DBService.gI().getConnection();) {
            ps = con.prepareStatement("update account set vnd = (vnd - ?), active = ? where id = ?");
            ps.setInt(1, num);
            ps.setInt(2, player.getSession().actived ? 1 : 0);
            ps.setInt(3, player.getSession().userId);
            ps.executeUpdate();
            ps.close();
            player.getSession().vnd += num;
        } catch (Exception e) {
            Log.error(PlayerDAO.class, e, "Lỗi update vnd" + player.name);
            return false;
        } finally {
        }
//        if (num > 100000) {
//            insertHistoryGold(player, num);
//        }
        return true;
    } 

    public static boolean subtindung(Player player, int num) {
        PreparedStatement ps = null;
        try (Connection con = DBService.gI().getConnection();) {
            ps = con.prepareStatement("update account set vnd = (Tindung - ?), active = ? where id = ?");
            ps.setInt(1, num);
            ps.setInt(2, player.getSession().actived ? 1 : 0);
            ps.setInt(3, player.getSession().userId);
            ps.executeUpdate();
            ps.close();
            player.getSession().vnd -= num;
        } catch (Exception e) {
            Log.error(PlayerDAO.class, e, "Lỗi update vnd" + player.name);
            return false;
        } finally {
        }
//        if (num > 100000) {
//            insertHistoryGold(player, num);
//        }
        return true;
    }

    public static boolean insertHistoryGold(Player player, int quantily) {
        PreparedStatement ps = null;
        try (Connection con = DBService.gI().getConnection();) {
            ps = con.prepareStatement("insert into history_gold(name,gold) values (?,?)");
            ps.setString(1, player.name);
            ps.setInt(2, quantily);
            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            Log.error(PlayerDAO.class, e, "Lỗi insert history_gold " + player.name);
            return false;
        }
        return true;
    }

    public static void subVND(Player player, int sotien) {
        PreparedStatement ps = null;
        try (Connection con = DBService.gI().getConnectionForSaveData();) {
            ps = con.prepareStatement("update account set vnd = (vnd  - ?) where id = ?");
            ps.setInt(1, sotien);
            ps.setInt(2, player.getSession().userId);
            ps.executeUpdate();
            player.getSession().vnd -= sotien;
        } catch (Exception e) {
            Log.error(PlayerDAO.class, e, "Lỗi update vnd " + player.name);
        } finally {
            try {
                ps.close();
            } catch (SQLException ex) {
                java.util.logging.Logger.getLogger(PlayerDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
public static void updateVND(Player player, int sotien) {
    PreparedStatement ps = null;
    try (Connection con = DBService.gI().getConnectionForSaveData();) {
        ps = con.prepareStatement("UPDATE account SET vnd = (vnd + ?) WHERE id = ?");
        ps.setInt(1, sotien);
        ps.setInt(2, player.getSession().userId);
        ps.executeUpdate();
        
        // Cập nhật số tiền trong session
        player.getSession().vnd += sotien;

        // Ghi log thông tin thành công
     //   Log.success(PlayerDAO.class, "Update VND thành công: " + player.name + " - Số tiền thay đổi: " + sotien);
    } catch (Exception e) {
        Log.error(PlayerDAO.class, e, "Lỗi update vnd cho người chơi: " + player.name);
    } finally {
        try {
            if (ps != null) {
                ps.close();
            }
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(PlayerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

    
    
    
    public static void mothanhvien(Player player, int sotien) {
        PreparedStatement ps = null;
        try (Connection con = DBService.gI().getConnectionForSaveData();) {
            ps = con.prepareStatement("update account set vnd = (vnd  - ?) , active = (active + 1)  where id = ?");
            ps.setInt(1, sotien);
            ps.setInt(2, player.getSession().userId);
            ps.executeUpdate();
            player.getSession().vnd -= sotien;
        } catch (Exception e) {
            Log.error(PlayerDAO.class, e, "Lỗi update vnd " + player.name);
        } finally {
            try {
                ps.close();
            } catch (SQLException ex) {
                java.util.logging.Logger.getLogger(PlayerDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void congiten(Player player, int sotien) {
        PreparedStatement ps = null;
        try (Connection con = DBService.gI().getConnectionForSaveData();) {
            ps = con.prepareStatement("update account set vnd = (vnd + ?)");
            ps.setInt(1, sotien);
            ps.setInt(2, sotien);
            ps.setInt(3, player.getSession().userId);
            ps.executeUpdate();
            player.getSession().vnd += sotien;
        } catch (Exception e) {
            Log.error(PlayerDAO.class, e, "Lỗi update vnd " + player.name);
        } finally {
            try {
                ps.close();
            } catch (SQLException ex) {
                java.util.logging.Logger.getLogger(PlayerDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void subGoldBar(Player player, int num) {
        PreparedStatement ps = null;
        try (Connection con = DBService.gI().getConnectionForSaveData();) {
            ps = con.prepareStatement("update account set thoi_vang = (thoi_vang - ?) where id = ?");
            ps.setInt(1, num);
            ps.setInt(2, player.getSession().userId);
            ps.executeUpdate();
        } catch (Exception e) {
            Log.error(PlayerDAO.class, e, "Lỗi update thỏi vàng " + player.name);
        } finally {
            try {
                ps.close();
            } catch (SQLException ex) {
                java.util.logging.Logger.getLogger(PlayerDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void addHistoryReceiveGoldBar(Player player, int goldBefore, int goldAfter,
            int goldBagBefore, int goldBagAfter, int goldBoxBefore, int goldBoxAfter) {
        PreparedStatement ps = null;
        try (Connection con = DBService.gI().getConnectionForSaveData();) {
            ps = con.prepareStatement("insert into history_receive_goldbar(player_id,player_name,gold_before_receive,"
                    + "gold_after_receive,gold_bag_before,gold_bag_after,gold_box_before,gold_box_after) values (?,?,?,?,?,?,?,?)");
            ps.setInt(1, (int) player.id);
            ps.setString(2, player.name);
            ps.setInt(3, goldBefore);
            ps.setInt(4, goldAfter);
            ps.setInt(5, goldBagBefore);
            ps.setInt(6, goldBagAfter);
            ps.setInt(7, goldBoxBefore);
            ps.setInt(8, goldBoxAfter);
            ps.executeUpdate();
        } catch (Exception e) {
            Log.error(PlayerDAO.class, e, "Lỗi update thỏi vàng " + player.name);
        } finally {
            try {
                ps.close();
            } catch (Exception e) {
            }
        }
    }

    public static void updateItemReward(Player player) {
        String dataItemReward = "";
        for (Item item : player.getSession().itemsReward) {
            if (item.isNotNullItem()) {
                dataItemReward += "{" + item.template.id + ":" + item.quantity;
                if (!item.itemOptions.isEmpty()) {
                    dataItemReward += "|";
                    for (ItemOption io : item.itemOptions) {
                        dataItemReward += "[" + io.optionTemplate.id + ":" + io.param + "],";
                    }
                    dataItemReward = dataItemReward.substring(0, dataItemReward.length() - 1) + "};";
                }
            }
        }
        PreparedStatement ps = null;
        ResultSet rs = null;
        try (Connection con = DBService.gI().getConnectionForGetPlayer();) {
            ps = con.prepareStatement("update account set reward = ? where id = ?");
            ps.setString(1, dataItemReward);
            ps.setInt(2, player.getSession().userId);
            ps.executeUpdate();
        } catch (Exception e) {
            Log.error(PlayerDAO.class, e, "Lỗi update phần thưởng " + player.name);
        } finally {
            try {
                ps.close();
            } catch (Exception e) {
            }
        }
    }

    public static void saveBag(Connection con, Player player) {
        if (player.loaded) {
            PreparedStatement ps = null;
            try {
                JSONArray dataBag = new JSONArray();
                for (Item item : player.inventory.itemsBag) {
                    JSONObject dataItem = new JSONObject();

                    if (item.isNotNullItem()) {
                        dataItem.put("temp_id", item.template.id);
                        dataItem.put("quantity", item.quantity);
                        dataItem.put("create_time", item.createTime);
                        JSONArray options = new JSONArray();
                        for (ItemOption io : item.itemOptions) {
                            JSONArray option = new JSONArray();
                            option.add(io.optionTemplate.id);
                            option.add(io.param);
                            options.add(option);
                        }
                        dataItem.put("option", options);
                    } else {
                        JSONArray options = new JSONArray();
                        dataItem.put("temp_id", -1);
                        dataItem.put("quantity", 0);
                        dataItem.put("create_time", 0);
                        dataItem.put("option", options);
                    }
                    dataBag.add(dataItem);
                }
                String itemsBag = dataBag.toJSONString();

                ps = con.prepareStatement("update player set items_bag = ? where id = ?");
                ps.setString(1, itemsBag);
                ps.setInt(2, (int) player.id);
                ps.executeUpdate();
                ps.close();
            } catch (Exception e) {
                Log.error(PlayerDAO.class, e, "Lỗi save bag player " + player.name);
            } finally {
                try {
                    ps.close();
                } catch (SQLException ex) {
                    java.util.logging.Logger.getLogger(PlayerDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }
}
