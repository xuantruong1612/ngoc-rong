package nro.jdbc.daos;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import nro.card.Card;
import nro.card.CollectionBook;
import nro.consts.ConstAchive;
import nro.consts.ConstMap;
import nro.consts.ConstPlayer;
import nro.jdbc.DBService;
import nro.manager.AchiveManager;
import nro.manager.PetFollowManager;
import nro.models.player.PetFollow;
import nro.models.clan.Clan;
import nro.models.clan.ClanMember;
import nro.models.item.Item;
import nro.models.item.ItemOption;
import nro.models.item.ItemTime;
import nro.models.npc.specialnpc.MabuEgg;
import nro.models.npc.specialnpc.MagicTree;
import nro.models.player.*;
import nro.models.skill.Skill;
import nro.models.task.Achivement;
import nro.models.task.AchivementTemplate;
import nro.models.task.TaskMain;
import nro.server.Client;
import nro.server.Manager;
import nro.server.io.Session;
import nro.server.model.AntiLogin;
import nro.services.*;
import nro.utils.SkillUtil;
import nro.utils.TimeUtil;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import nro.models.npc.specialnpc.BillEgg;
import nro.models.npc.specialnpc.duahau;
import nro.models.npc.specialnpc.kickvip;
import nro.server.ServerNotify;

/**
 * @Build Arriety
 */
public class GodGK {

    public static boolean login(Session session, AntiLogin al) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            Connection conn = DBService.gI().getConnectionForLogin();
            String query = "select * from account where username = ? and password = ? limit 1";
            ps = conn.prepareStatement(query);
            ps.setString(1, session.uu);
            ps.setString(2, session.pp);
            rs = ps.executeQuery();
            if (rs.next()) {
                session.userId = rs.getInt("account.id");
                Player plInGame = Client.gI().getPlayerByUser(session.userId);
                if (plInGame != null) {
                    if (plInGame.zone != null && plInGame.itemTime != null && (plInGame.itemTime.isUseOffline)) {
                        Client.gI().remove(plInGame);
                        Client.gI().kickSession(plInGame.getSession());
                    } else {
                        Client.gI().kickSession(plInGame.getSession());
                        Service.getInstance().sendThongBaoOK(session, "Tài khoản đang được đăng nhập tại máy chủ!");
                        return false;
                    }
                }

                session.isAdmin = rs.getBoolean("is_admin");
                session.lastTimeLogout = rs.getTimestamp("last_time_logout").getTime();
                session.actived = rs.getBoolean("active");
                session.goldBar = rs.getInt("account.thoi_vang");
                session.vnd = rs.getInt("account.vnd");
                session.tongnap = rs.getInt("account.tongnap");
                session.dataReward = rs.getString("reward");
                if (rs.getTimestamp("last_time_login").getTime() > session.lastTimeLogout) {
                    Service.getInstance().sendThongBaoOK(session, "Tài khoản đang đăng nhập máy chủ khác!");
                    return false;
                }
                if (session.version < 998) {
                    Service.getInstance().sendThongBaoOK(session, "Hãy tải phiên bản từ Trang Chủ");
                } else if (session.isAdmin) {
                    Service.getInstance().sendThongBaoOK(session, "Bạn không phải Admin");
                } else if (rs.getBoolean("ban")) {
                    Service.getInstance().sendThongBaoOK(session, "Tài khoản đã bị khóa, do không Hoạt Động Ib FB: Xuân Trường (Sera) Kèm ID này \nId của bạn: " + session.userId);
                } else {
                    long lastTimeLogout = rs.getTimestamp("last_time_logout").getTime();
                    int secondsPass = (int) ((System.currentTimeMillis() - lastTimeLogout) / 1000);
                    if (secondsPass < Manager.SECOND_WAIT_LOGIN && !session.isAdmin) {
                        Service.getInstance().sendThongBaoOK(session, "Thử lại sau " + (Manager.SECOND_WAIT_LOGIN - secondsPass) + " giây");
                    }
                }
                al.reset();
                return true;
            } else {
                Service.getInstance().sendThongBaoOK(session, "Thông tin tài khoản hoặc mật khẩu không chính xác");
                al.wrong();
                // Anti login
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException ex) {
                }
            }
        }
        return false;
    }

    public static Player loadPlayer(Session session) {
        try {
            Connection connection = DBService.gI().getConnectionForLogin();
            PreparedStatement ps = connection.prepareStatement("select * from player where account_id = ? limit 1");
            ps.setInt(1, session.userId);
            ResultSet rs = ps.executeQuery();
            try {
                if (rs.next()) {
                    double plHp = 200000000;
                    double plMp = 200000000;
                    JSONValue jv = new JSONValue();
                    JSONArray dataArray = null;
                    JSONObject dataObject = null;

                    Player player;
                    if (Client.gI().getPlayer(rs.getInt("id")) != null) {
                        player = Client.gI().getPlayer(rs.getInt("id"));
                        if (player.itemTime.isUseOffline) {
                            ItemTimeService.gI().turnOffOffline(player, InventoryService.gI().findItemBagByTemp(player, 1890));
                        }
                        return player;
                    } else {
                        player = new Player();
                    }
                    player.id = rs.getInt("id");
                    player.name = rs.getString("name");
                    player.head = rs.getShort("head");
                    player.gender = rs.getByte("gender");
                    player.pointsukien = rs.getInt("pointsukien");
                    player.gtPoint = rs.getInt("poingapthu");
//                    player.diemdanh = rs.getLong("diemdanh");
                    player.pointidemdanh = rs.getInt("pointdiemdanh");
                    player.point_sb = rs.getInt("point_sb");
                    player.CapTamkjll = rs.getInt("CapTamkjll");
                    if (player.CapTamkjll > 20000) {
                        player.CapTamkjll = 20000;
                    }
                    player.ExpTamkjll = rs.getLong("ExpTamkjll");
                    player.LbTamkjll = rs.getInt("LbTamkjll");
                    if (player.CapTamkjll > 20000) {
                        player.CapTamkjll = 20000;
                    }
                    player.DLbTamkjll = rs.getLong("DLbTamkjll");
                    player.TamkjllCapPb = rs.getInt("TamkjllCapPb");
                    if (player.TamkjllCapPb > 500) {
                        player.TamkjllCapPb = 500;
                    }
                    player.TamkjllThomo = rs.getInt("TamkjllThomo");
                    player.TamkjllThomoExp = rs.getLong("TamkjllThomoExp");
                    player.TamkjllCS = rs.getInt("TamkjllCS");
                    player.pointPvp = rs.getInt("pointPvp");
                    player.diemfam = rs.getInt("Diemfam");
                    player.diemsk = rs.getInt("diemsk");
                    player.diemsanbat = rs.getInt("diemsanbat");

                    player.mocnap = rs.getInt("mocnap");
                    player.Nrosieucap = rs.getInt("Nrosieucap");
                    player.Tindung = rs.getInt("Tindung");
                    player.naptuan = rs.getInt("Naptuan");
                    player.napthang = rs.getInt("Napthang");
                    player.Diemvip = rs.getInt("Diemvip");
                     player.cfpass = rs.getInt("Cfpass");
                       player.premium = rs.getInt("Cfpremium");
                         player.mocpass = rs.getInt("mocpass");
                          player.mocpassvip = rs.getInt("mocpassvip");
                         player.diemshe = rs.getInt("diemshe"); 
                           player.dakethon = rs.getInt("dakethon"); 
                         player.duockethon = rs.getInt("duockethon"); 
                    player.DoneVoDaiBaHatMit = rs.getInt("DoneVoDaiBaHatMit");
                    player.haveTennisSpaceShip = rs.getBoolean("have_tennis_space_ship");
                    int clanId = rs.getInt("clan_id_sv" + Manager.SERVER);
                    if (clanId != -1) {
                        Clan clan = ClanService.gI().getClanById(clanId);
                        if (clan != null) {
                            for (ClanMember cm : clan.getMembers()) {
                                if (cm.id == player.id) {
                                    clan.addMemberOnline(player);
                                    player.clan = clan;
                                    player.clanMember = cm;
                                    player.setBuff(clan.getBuff());
                                    break;
                                }
                            }
                        }
                    }
                    // diem su kien
                    int evPoint = rs.getInt("event_point");
                    player.event.setEventPoint(evPoint);

                    dataArray = (JSONArray) JSONValue.parse(rs.getString("sk_tet"));
                    int timeBanhTet = Integer.parseInt(dataArray.get(0).toString());
                    int timeBanhChung = Integer.parseInt(dataArray.get(1).toString());
                    boolean isNauBanhTet = Integer.parseInt(dataArray.get(2).toString()) == 1;
                    boolean isNauBanhChung = Integer.parseInt(dataArray.get(3).toString()) == 1;
                    boolean receivedLuckMoney = Integer.parseInt(dataArray.get(4).toString()) == 1;

                    player.event.setTimeCookTetCake(timeBanhTet);
                    player.event.setTimeCookChungCake(timeBanhChung);
                    player.event.setCookingTetCake(isNauBanhTet);
                    player.event.setCookingChungCake(isNauBanhChung);
                    player.event.setReceivedLuckyMoney(receivedLuckMoney);
                    dataArray.clear();
                    
                    dataArray = (JSONArray) JSONValue.parse(rs.getString("leothap"));
                    if (dataArray != null && !dataArray.isEmpty()) {
                        player.tangThap = Integer.parseInt(dataArray.get(0).toString());
                        player.levelThap = Integer.parseInt(dataArray.get(1).toString());
                        player.pointThap = Integer.parseInt(dataArray.get(2).toString());
                        dataArray.clear();
                    }
                    //data kim lượng
                    dataArray = (JSONArray) JSONValue.parse(rs.getString("data_inventory"));
                    player.inventory.gold = Long.parseLong(dataArray.get(0).toString());
                    player.inventory.gem = Integer.parseInt(dataArray.get(1).toString());
                    player.inventory.ruby = Integer.parseInt(dataArray.get(2).toString());
                    if (dataArray.size() >= 4) {
                        player.inventory.goldLimit = Long.parseLong(dataArray.get(3).toString());
                    }
                    dataArray.clear();
                    
                    player.isbienhinh = rs.getInt("isbienhinh");

                    dataArray = (JSONArray) JSONValue.parse(rs.getString("Tamkjlltutien"));
                    player.Tamkjlltutien[0] = Long.parseLong(String.valueOf(dataArray.get(0)));
                    player.Tamkjlltutien[1] = Long.parseLong(String.valueOf(dataArray.get(1)));
                    if (player.Tamkjlltutien[1] > 340) {
                        player.Tamkjlltutien[1] = 0;
                    }
                    player.Tamkjlltutien[2] = Long.parseLong(String.valueOf(dataArray.get(2)));
                    if (player.Tamkjlltutien[2] > 10000) {
                        player.Tamkjlltutien[2] = 0;
                    }
                    dataArray.clear();

                    dataArray = (JSONArray) JSONValue.parse(rs.getString("TamkjllDLDL"));
                    player.TamkjllDauLaDaiLuc[0] = Long.parseLong(String.valueOf(dataArray.get(0)));
                    player.TamkjllDauLaDaiLuc[1] = Long.parseLong(String.valueOf(dataArray.get(1)));
                    player.TamkjllDauLaDaiLuc[2] = Long.parseLong(String.valueOf(dataArray.get(2)));
                    player.TamkjllDauLaDaiLuc[3] = Long.parseLong(String.valueOf(dataArray.get(3)));
                    player.TamkjllDauLaDaiLuc[4] = Long.parseLong(String.valueOf(dataArray.get(4)));
                    player.TamkjllDauLaDaiLuc[5] = Long.parseLong(String.valueOf(dataArray.get(5)));
                    player.TamkjllDauLaDaiLuc[6] = Long.parseLong(String.valueOf(dataArray.get(6)));
                    player.TamkjllDauLaDaiLuc[7] = Long.parseLong(String.valueOf(dataArray.get(7)));
                    player.TamkjllDauLaDaiLuc[8] = Long.parseLong(String.valueOf(dataArray.get(8)));
                    player.TamkjllDauLaDaiLuc[9] = Long.parseLong(String.valueOf(dataArray.get(9)));
                    player.TamkjllDauLaDaiLuc[10] = Long.parseLong(String.valueOf(dataArray.get(10)));
                    player.TamkjllDauLaDaiLuc[11] = Long.parseLong(String.valueOf(dataArray.get(11)));
                    player.TamkjllDauLaDaiLuc[12] = Long.parseLong(String.valueOf(dataArray.get(12)));
                    player.TamkjllDauLaDaiLuc[13] = Long.parseLong(String.valueOf(dataArray.get(13)));
                    player.TamkjllDauLaDaiLuc[14] = Long.parseLong(String.valueOf(dataArray.get(14)));
                    player.TamkjllDauLaDaiLuc[15] = Long.parseLong(String.valueOf(dataArray.get(15)));
                    player.TamkjllDauLaDaiLuc[16] = Long.parseLong(String.valueOf(dataArray.get(16)));
                    player.TamkjllDauLaDaiLuc[17] = Long.parseLong(String.valueOf(dataArray.get(17)));
                    player.TamkjllDauLaDaiLuc[18] = Long.parseLong(String.valueOf(dataArray.get(18)));
                    player.TamkjllDauLaDaiLuc[19] = Long.parseLong(String.valueOf(dataArray.get(19)));
                    player.TamkjllDauLaDaiLuc[20] = Long.parseLong(String.valueOf(dataArray.get(20)));
                    dataArray.clear();

                    dataArray = (JSONArray) JSONValue.parse(rs.getString("Tamkjll_Pet"));
                    player.TamkjllPetGiong = Integer.parseInt(String.valueOf(dataArray.get(0)));
                    if (player.TamkjllPetGiong >= 0 && player.TamkjllPetGiong <= 10) {
                        player.TamkjllNamePet = String.valueOf(dataArray.get(1));
                        player.TamkjllPetHunger = Integer.parseInt(String.valueOf(dataArray.get(2)));
                        player.TamkjllPetPower = Long.parseLong(String.valueOf(dataArray.get(3)));
                        player.TamkjlllastTimeThucan = Long.parseLong(String.valueOf(dataArray.get(4)));
                        player.TamkjlllevelPet = Integer.parseInt(String.valueOf(dataArray.get(5)));
                        if (player.TamkjlllevelPet > 10000) {
                            player.TamkjlllevelPet = 10000;
                        }
                        player.TamkjllExpPet = Long.parseLong(String.valueOf(dataArray.get(6)));
                    } else {
                        player.TamkjllPetGiong = -1;
                    }
                    dataArray.clear();
                    dataArray = (JSONArray) JSONValue.parse(rs.getString("Tamkjll_Tu_Ma"));
                    if (!dataArray.isEmpty()) {
                        player.Tamkjll_Tu_Ma = Integer.parseInt(String.valueOf(dataArray.get(0)));
                        player.Tamkjll_Exp_Tu_Ma = Long.parseLong(String.valueOf(dataArray.get(1)));
                        player.Tamkjll_Ma_Hoa = Integer.parseInt(String.valueOf(dataArray.get(2)));
                        player.TamkjllLasttimeMaHoa = Long.parseLong(String.valueOf(dataArray.get(3)));
                        player.Tamkjll_Ma_cot = Integer.parseInt(String.valueOf(dataArray.get(4)));
                    }
                    // data kim lượng
                    dataArray.clear();

                    player.event.setDiemTichLuy(session.diemTichNap);
                    player.event.setMocNapDaNhan(rs.getInt("moc_nap"));
                    player.server = session.server;
                    //data mocnap 
                    dataArray = (JSONArray) JSONValue.parse(rs.getString("reward_mocnap"));
                    player.mocnap1 = Integer.parseInt(String.valueOf(dataArray.get(0))) == 1 ? true : false;
                    player.mocnap2 = Integer.parseInt(String.valueOf(dataArray.get(1))) == 1 ? true : false;
                    player.mocnap3 = Integer.parseInt(String.valueOf(dataArray.get(2))) == 1 ? true : false;
                    player.mocnap4 = Integer.parseInt(String.valueOf(dataArray.get(3))) == 1 ? true : false;
                    player.mocnap5 = Integer.parseInt(String.valueOf(dataArray.get(4))) == 1 ? true : false;
                    //data tọa độ
                    try {
                        dataArray = (JSONArray) jv.parse(rs.getString("data_location"));
                        player.location.x = Integer.parseInt(dataArray.get(0).toString());
                        player.location.y = Integer.parseInt(dataArray.get(1).toString());
                        int mapId = Integer.parseInt(dataArray.get(2).toString());
                        if (MapService.gI().isMapDoanhTrai(mapId) || MapService.gI().isMapBlackBallWar(mapId)
                                || MapService.gI().isMapBanDoKhoBau(mapId) || mapId == 211 || mapId == 126 || mapId == ConstMap.CON_DUONG_RAN_DOC
                                || mapId == ConstMap.CON_DUONG_RAN_DOC_142 || mapId == ConstMap.CON_DUONG_RAN_DOC_143 || mapId == ConstMap.HOANG_MAC) {
                            mapId = player.gender + 21;
                            player.location.x = 300;
                            player.location.y = 336;
                        }
                        player.zone = MapService.gI().getMapCanJoin(player, mapId);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    dataArray.clear();

                    player.dameBoss = rs.getDouble("dameBoss");
                    //data chỉ số
                    dataArray = (JSONArray) jv.parse(rs.getString("data_point"));
                    plMp = Double.parseDouble(dataArray.get(1).toString());
                    player.nPoint.mpg = Double.parseDouble(dataArray.get(2).toString());
                    player.nPoint.critg = Byte.parseByte(dataArray.get(3).toString());
                    player.nPoint.limitPower = Byte.parseByte(dataArray.get(4).toString());
                    player.nPoint.stamina = Short.parseShort(dataArray.get(5).toString());
                    plHp = Double.parseDouble(dataArray.get(6).toString());
                    player.nPoint.defg = Double.parseDouble(dataArray.get(7).toString());
                    player.nPoint.tiemNang = Long.parseLong(dataArray.get(8).toString());
                    player.nPoint.maxStamina = Short.parseShort(dataArray.get(9).toString());
                    player.nPoint.dameg = Double.parseDouble(dataArray.get(10).toString());
                    player.nPoint.power = Long.parseLong(dataArray.get(11).toString());
                    player.nPoint.hpg = Double.parseDouble(dataArray.get(12).toString());
                    dataArray.clear();

                    //data đậu thần
                    dataArray = (JSONArray) jv.parse(rs.getString("data_magic_tree"));
                    boolean isUpgrade = Byte.parseByte(dataArray.get(0).toString()) == 1;
                    long lastTimeUpgrade = Long.parseLong(dataArray.get(1).toString());
                    byte level = Byte.parseByte(dataArray.get(2).toString());
                    long lastTimeHarvest = Long.parseLong(dataArray.get(3).toString());
                    byte currPea = Byte.parseByte(dataArray.get(4).toString());
                    player.magicTree = new MagicTree(player, level, currPea, lastTimeHarvest, isUpgrade, lastTimeUpgrade);
                    dataArray.clear();

                    //data phần thưởng sao đen
                    dataArray = (JSONArray) jv.parse(rs.getString("data_black_ball"));
                    for (int i = 0; i < dataArray.size(); i++) {
                        JSONArray reward = (JSONArray) jv.parse(String.valueOf(dataArray.get(i)));
                        player.rewardBlackBall.timeOutOfDateReward[i] = Long.parseLong(reward.get(0).toString());
                        player.rewardBlackBall.lastTimeGetReward[i] = Long.parseLong(reward.get(1).toString());
                        reward.clear();
                    }
                    dataArray.clear();

                    //data body
                    dataArray = (JSONArray) jv.parse(rs.getString("items_body"));
                    while (dataArray.size() < 16) {
                        JSONObject item = new JSONObject();
                        JSONArray options = new JSONArray();
                        item.put("temp_id", -1);
                        item.put("quantity", 0);
                        item.put("create_time", 0);
                        item.put("option", options);
                        dataArray.add(item);
                        System.out.println("addBody:" + dataArray.size());
                    }
                    for (int i = 0; i < dataArray.size(); i++) {
                        Item item = null;
                        dataObject = (JSONObject) dataArray.get(i);
                        short tempId = Short.parseShort(String.valueOf(dataObject.get("temp_id")));
                        if (tempId != -1) {
                            item = ItemService.gI().createNewItem(tempId, Integer.parseInt(String.valueOf(dataObject.get("quantity"))));
                            JSONArray options = (JSONArray) dataObject.get("option");
                            for (int j = 0; j < options.size(); j++) {
                                JSONArray opt = (JSONArray) options.get(j);
                                item.itemOptions.add(new ItemOption(Integer.parseInt(String.valueOf(opt.get(0))),
                                        Long.parseLong(String.valueOf(opt.get(1)))));
                            }
                            item.createTime = Long.parseLong(String.valueOf(dataObject.get("create_time")));
                            if (ItemService.gI().isOutOfDateTime(item)) {
                                item = ItemService.gI().createItemNull();
                            }
                        } else {
                            item = ItemService.gI().createItemNull();
                        }
                        player.inventory.itemsBody.add(item);
                    }
                    dataArray.clear();
                    dataObject.clear();

                    //data bag
                    dataArray = (JSONArray) jv.parse(rs.getString("items_bag"));
                    for (int i = 0; i < dataArray.size(); i++) {
                        Item item = null;
                        dataObject = (JSONObject) dataArray.get(i);
                        short tempId = Short.parseShort(String.valueOf(dataObject.get("temp_id")));
                        if (tempId != -1) {
                            item = ItemService.gI().createNewItem(tempId, Integer.parseInt(String.valueOf(dataObject.get("quantity"))));
                            JSONArray options = (JSONArray) dataObject.get("option");
                            for (int j = 0; j < options.size(); j++) {
                                JSONArray opt = (JSONArray) options.get(j);
                                item.itemOptions.add(new ItemOption(Integer.parseInt(String.valueOf(opt.get(0))),
                                        Long.parseLong(String.valueOf(opt.get(1)))));
                            }
                            item.createTime = Long.parseLong(String.valueOf(dataObject.get("create_time")));
                            if (ItemService.gI().isOutOfDateTime(item)) {
                                item = ItemService.gI().createItemNull();
                            }
                        } else {
                            item = ItemService.gI().createItemNull();
                        }
                        player.inventory.itemsBag.add(item);
                    }
                    dataArray.clear();
                    dataObject.clear();

                    //data box
                    dataArray = (JSONArray) jv.parse(rs.getString("items_box"));
                    for (int i = 0; i < dataArray.size(); i++) {
                        Item item = null;
                        dataObject = (JSONObject) dataArray.get(i);
                        short tempId = Short.parseShort(String.valueOf(dataObject.get("temp_id")));
                        if (tempId != -1) {
                            item = ItemService.gI().createNewItem(tempId, Integer.parseInt(String.valueOf(dataObject.get("quantity"))));
                            JSONArray options = (JSONArray) dataObject.get("option");
                            for (int j = 0; j < options.size(); j++) {
                                JSONArray opt = (JSONArray) options.get(j);
                                item.itemOptions.add(new ItemOption(Integer.parseInt(String.valueOf(opt.get(0))),
                                        Integer.parseInt(String.valueOf(opt.get(1)))));
                            }
                            item.createTime = Long.parseLong(String.valueOf(dataObject.get("create_time")));
                            if (ItemService.gI().isOutOfDateTime(item)) {
                                item = ItemService.gI().createItemNull();
                            }
                        } else {
                            item = ItemService.gI().createItemNull();
                        }
                        player.inventory.itemsBox.add(item);
                    }
                    dataArray.clear();
                    dataObject.clear();

                    //data box lucky round
                    dataArray = (JSONArray) jv.parse(rs.getString("items_box_lucky_round"));
                    for (int i = 0; i < dataArray.size(); i++) {
                        Item item = null;
                        dataObject = (JSONObject) dataArray.get(i);
                        short tempId = Short.parseShort(String.valueOf(dataObject.get("temp_id")));
                        if (tempId != -1) {
                            item = ItemService.gI().createNewItem(tempId, Integer.parseInt(String.valueOf(dataObject.get("quantity"))));
                            JSONArray options = (JSONArray) dataObject.get("option");
                            for (int j = 0; j < options.size(); j++) {
                                JSONArray opt = (JSONArray) options.get(j);
                                item.itemOptions.add(new ItemOption(Integer.parseInt(String.valueOf(opt.get(0))),
                                        Integer.parseInt(String.valueOf(opt.get(1)))));
                            }
                        } else {
                            item = ItemService.gI().createItemNull();
                        }
                        player.inventory.itemsBoxCrackBall.add(item);
                    }
                    dataArray.clear();
                    dataObject.clear();

                    //data friends
                    dataArray = (JSONArray) jv.parse(rs.getString("friends"));
                    for (int i = 0; i < dataArray.size(); i++) {
                        dataObject = (JSONObject) dataArray.get(i);
                        Friend friend = new Friend();
                        friend.id = Integer.parseInt(String.valueOf(dataObject.get("id")));
                        friend.name = String.valueOf(dataObject.get("name"));
                        friend.head = Short.parseShort(String.valueOf(dataObject.get("head")));
                        friend.body = Short.parseShort(String.valueOf(dataObject.get("body")));
                        friend.leg = Short.parseShort(String.valueOf(dataObject.get("leg")));
                        friend.bag = Byte.parseByte(String.valueOf(dataObject.get("bag")));
                        friend.power = Long.parseLong(String.valueOf(dataObject.get("power")));
                        player.friends.add(friend);
                        dataObject.clear();
                    }
                    dataArray.clear();

                    //data enemies
                    dataArray = (JSONArray) jv.parse(rs.getString("enemies"));
                    for (int i = 0; i < dataArray.size(); i++) {
                        dataObject = (JSONObject) dataArray.get(i);
                        Enemy enemy = new Enemy();
                        enemy.id = Integer.parseInt(String.valueOf(dataObject.get("id")));
                        enemy.name = String.valueOf(dataObject.get("name"));
                        enemy.head = Short.parseShort(String.valueOf(dataObject.get("head")));
                        enemy.body = Short.parseShort(String.valueOf(dataObject.get("body")));
                        enemy.leg = Short.parseShort(String.valueOf(dataObject.get("leg")));
                        enemy.bag = Byte.parseByte(String.valueOf(dataObject.get("bag")));
                        enemy.power = Long.parseLong(String.valueOf(dataObject.get("power")));
                        player.enemies.add(enemy);
                        dataObject.clear();
                    }
                    dataArray.clear();

                    //data nội tại
                    dataArray = (JSONArray) jv.parse(rs.getString("data_intrinsic"));
                    byte intrinsicId = Byte.parseByte(dataArray.get(0).toString());
                    player.playerIntrinsic.intrinsic = IntrinsicService.gI().getIntrinsicById(intrinsicId);
                    player.playerIntrinsic.intrinsic.param1 = Short.parseShort(dataArray.get(1).toString());
                    player.playerIntrinsic.countOpen = Byte.parseByte(dataArray.get(2).toString());
                    player.playerIntrinsic.intrinsic.param2 = Short.parseShort(dataArray.get(3).toString());
                    dataArray.clear();

                    dataArray = (JSONArray) jv.parse(rs.getString("item_new_time"));
                    int duoiKhi = Integer.parseInt(String.valueOf(dataArray.get(0)));
                    int timeBanh1Trung = Integer.parseInt(String.valueOf(dataArray.get(1)));
                    int timeBanh2Trung = Integer.parseInt(String.valueOf(dataArray.get(2)));
                    player.itemTime.lastTimeDuoiKhi = System.currentTimeMillis() - (ItemTime.TIME_ITEM - duoiKhi);
                    player.itemTime.lastTimeBanhTrungThu1Trung = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timeBanh1Trung);
                    player.itemTime.lastTimeBanhTrungThu2Trung = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timeBanh2Trung);
                    player.itemTime.isDuoiKhi = duoiKhi != 0;
                    player.itemTime.isBanhTrungThu1Trung = timeBanh1Trung != 0;
                    player.itemTime.isBanhTrungThu2Trung = timeBanh2Trung != 0;
                    if (dataArray.size() > 3) {
                        int timeBanhTrungThuDacBiet = Integer.parseInt(dataArray.get(3).toString());
                        int timeBanhTrungThuGaQuay = Integer.parseInt(dataArray.get(4).toString());
                        int timeBanhTrungThuThapCam = Integer.parseInt(dataArray.get(5).toString());
                        int timemytom = Integer.parseInt(dataArray.get(6).toString());
                        int timethanhkiem = Integer.parseInt(dataArray.get(7).toString());
                        int timedoankiem = Integer.parseInt(dataArray.get(8).toString());
                        int timebinhx2 = Integer.parseInt(dataArray.get(9).toString());
                        int timebinhx3 = Integer.parseInt(dataArray.get(10).toString());
                        int timebinhx4 = Integer.parseInt(dataArray.get(11).toString());
                        int timetainguyen = Integer.parseInt(dataArray.get(12).toString());
                        int timetrangbi = Integer.parseInt(dataArray.get(13).toString());
                        int timesaophale = Integer.parseInt(dataArray.get(14).toString());
                        int timethucan  = Integer.parseInt(dataArray.get(15).toString());
                        int timengocrong = Integer.parseInt(dataArray.get(16).toString());
                        
                        
                        player.itemTime.lastTimeBanhTrungThuDacBiet = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timeBanhTrungThuDacBiet);
                        player.itemTime.lastTimeBanhTrungThuGaQuay = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timeBanhTrungThuGaQuay);
                        player.itemTime.lastTimeBanhTrungThuThapCam = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timeBanhTrungThuThapCam);
                         player.itemTime.lastTimeMytom = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timemytom);
                        player.itemTime.lastTimeThanhkiem = System.currentTimeMillis() - (ItemTime.TIME_THANHKIEM - timethanhkiem); 
                        player.itemTime.lastTimeHokiem = System.currentTimeMillis() - (ItemTime.TIME_HOKIEM - timedoankiem); 
                         player.itemTime.lastTimebinhx2 = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timebinhx2); 
                        player.itemTime.lastTimebinhx3 = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timebinhx3); 
                        player.itemTime.lastTimebinhx4 = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timebinhx4); 
                        player.itemTime.lastTimetainguyen = System.currentTimeMillis() - (ItemTime.TIME_30P - timetainguyen); 
                        player.itemTime.lastTimetrangbi = System.currentTimeMillis() - (ItemTime.TIME_30P - timetrangbi); 
                        player.itemTime.lastTimesaophale = System.currentTimeMillis() - (ItemTime.TIME_30P - timesaophale); 
                        player.itemTime.lastTimethucan = System.currentTimeMillis() - (ItemTime.TIME_30P - timethucan); 
                        player.itemTime.lastTimengocrong = System.currentTimeMillis() - (ItemTime.TIME_30P - timengocrong); 
                        
                        player.itemTime.isBanhTrungThuDacBiet = timeBanhTrungThuDacBiet != 0;
                        player.itemTime.isBanhTrungThuGaQuay = timeBanhTrungThuGaQuay != 0;
                        player.itemTime.isBanhTrungThuThapCam = timeBanhTrungThuThapCam != 0;
                         player.itemTime.ismytom = timemytom != 0;
                          player.itemTime.isthanhkiem = timethanhkiem != 0;
                          player.itemTime.ishokiem = timedoankiem != 0;
                          player.itemTime.isbinhx2 = timebinhx2 != 0;
                          player.itemTime.isbinhx3 = timebinhx3 != 0;
                          player.itemTime.isbinhx4 = timebinhx4 != 0;
                          player.itemTime.ismaydotainguyen = timetainguyen != 0;
                          player.itemTime.ismaydotrangbi = timetrangbi != 0;
                          player.itemTime.ismaydosaophale = timesaophale != 0;
                          player.itemTime.ismaydothucan = timethucan!= 0;
                          player.itemTime.ismaydongocrong = timengocrong != 0;
                          
                          
                    }
                    dataArray.clear();

                    //data item time
                    dataArray = (JSONArray) jv.parse(rs.getString("data_item_time"));
                    int timeBoKhi = Integer.parseInt(dataArray.get(0).toString());
                    int timeAnDanh = Integer.parseInt(dataArray.get(1).toString());
                    int timeOpenPower = Integer.parseInt(dataArray.get(2).toString());
                    int timeCuongNo = Integer.parseInt(dataArray.get(3).toString());
                    int timeBoHuyet = Integer.parseInt(dataArray.get(5).toString());
                    int timeGiapXen = Integer.parseInt(dataArray.get(8).toString());
                    int timeMayDo = 0;
                    int timeMayDoskh = 0;
                    int timeMeal = 0;
                    int iconMeal = 0;
                    try {
                        timeMayDo = Integer.parseInt(dataArray.get(4).toString());
                        timeMayDoskh = Integer.parseInt(dataArray.get(5).toString());
                        timeMeal = Integer.parseInt(dataArray.get(7).toString());
                        iconMeal = Integer.parseInt(dataArray.get(6).toString());
                    } catch (Exception e) {
                    }
                    int timeBanhChung1 = 0;
                    int timeBanhTet1 = 0;
                    int timeBoKhi2 = 0;
                    int timeGiapXen2 = 0;
                    int timeCuongNo2 = 0;
                    int timeBoHuyet2 = 0;
                    if (dataArray.size() >= 15) {
                        timeBanhChung1 = Integer.parseInt(dataArray.get(9).toString());
                        timeBanhTet1 = Integer.parseInt(dataArray.get(10).toString());
                        timeBoKhi2 = Integer.parseInt(dataArray.get(11).toString());
                        timeGiapXen2 = Integer.parseInt(dataArray.get(12).toString());
                        timeCuongNo2 = Integer.parseInt(dataArray.get(13).toString());
                        timeBoHuyet2 = Integer.parseInt(dataArray.get(14).toString());
                    }
                    player.itemTime.lastTimeBoHuyet = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timeBoHuyet);
                    player.itemTime.lastTimeBoKhi = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timeBoKhi);
                    player.itemTime.lastTimeGiapXen = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timeGiapXen);
                    player.itemTime.lastTimeCuongNo = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timeCuongNo);
                    player.itemTime.lastTimeBoHuyet2 = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timeBoHuyet2);
                    player.itemTime.lastTimeBoKhi2 = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timeBoKhi2);
                    player.itemTime.lastTimeGiapXen2 = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timeGiapXen2);
                    player.itemTime.lastTimeCuongNo2 = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timeCuongNo2);
                    player.itemTime.lastTimeAnDanh = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timeAnDanh);
                    player.itemTime.lastTimeOpenPower = System.currentTimeMillis() - (ItemTime.TIME_OPEN_POWER - timeOpenPower);
                    player.itemTime.lastTimeUseMayDo = System.currentTimeMillis() - (ItemTime.TIME_MAY_DO - timeMayDo);

                    player.itemTime.lastTimeEatMeal = System.currentTimeMillis() - (ItemTime.TIME_EAT_MEAL - timeMeal);
                    player.itemTime.lastTimeBanhChung = System.currentTimeMillis() - (ItemTime.TIME_EAT_MEAL - timeBanhChung1);
                    player.itemTime.lastTimeBanhTet = System.currentTimeMillis() - (ItemTime.TIME_EAT_MEAL - timeBanhTet1);
                    player.itemTime.lastTimeUseMayDoskh = System.currentTimeMillis() - (ItemTime.TIME_MAY_DO - timeMayDoskh);
                    player.itemTime.iconMeal = iconMeal;
                    player.itemTime.isUseBoHuyet = timeBoHuyet != 0;
                    player.itemTime.isUseBoKhi = timeBoKhi != 0;
                    player.itemTime.isUseGiapXen = timeGiapXen != 0;
                    player.itemTime.isUseCuongNo = timeCuongNo != 0;
                    player.itemTime.isUseBoHuyet2 = timeBoHuyet2 != 0;
                    player.itemTime.isUseBoKhi2 = timeBoKhi2 != 0;
                    player.itemTime.isUseGiapXen2 = timeGiapXen2 != 0;
                    player.itemTime.isUseCuongNo2 = timeCuongNo2 != 0;
                    player.itemTime.isUseAnDanh = timeAnDanh != 0;
                    player.itemTime.isOpenPower = timeOpenPower != 0;
                    player.itemTime.isUseMayDo = timeMayDo != 0;
                    player.itemTime.isEatMeal = timeMeal != 0;
                    player.itemTime.isUseBanhChung = timeBanhChung1 != 0;
                    player.itemTime.isUseBanhTet = timeBanhTet1 != 0;
                    player.itemTime.isUseMayDoskh = timeMayDoskh != 0;
                    dataArray.clear();

                    dataArray = (JSONArray) jv.parse(rs.getString("Nuoc_mia"));
                    int nuocmiakhonglo = Integer.parseInt(String.valueOf(dataArray.get(0)));
                    int nuocmiathom = Integer.parseInt(String.valueOf(dataArray.get(1)));
                    int nuocmiasaurieng = Integer.parseInt(String.valueOf(dataArray.get(2)));

                    player.itemTime.lastnuocmiakhonglo = System.currentTimeMillis() - (ItemTime.TIME_NUOC_MIA - nuocmiakhonglo);
                    player.itemTime.lastnuocmiathom = System.currentTimeMillis() - (ItemTime.TIME_NUOC_MIA - nuocmiathom);
                    player.itemTime.lastnuocmiasaurieng = System.currentTimeMillis() - (ItemTime.TIME_NUOC_MIA - nuocmiasaurieng);

                    player.itemTime.isnuocmiakhonglo = nuocmiakhonglo != 0;
                    player.itemTime.isnuocmiathom = nuocmiathom != 0;
                    player.itemTime.isnuocmiasaurieng = nuocmiasaurieng != 0;
                    dataArray.clear();

                    dataArray = (JSONArray) jv.parse(rs.getString("data_exp_tutien"));
                    int timeexptamkjll1_5 = Integer.parseInt(String.valueOf(dataArray.get(0)));
                    int timeexptamkjll2 = Integer.parseInt(String.valueOf(dataArray.get(1)));
                    int timeexptamkjll3 = Integer.parseInt(String.valueOf(dataArray.get(2)));
                    int timeexptamkjll4 = Integer.parseInt(String.valueOf(dataArray.get(3)));
                    int timeexptamkjll5 = Integer.parseInt(String.valueOf(dataArray.get(4)));
                    int timeexptamkjll6 = Integer.parseInt(String.valueOf(dataArray.get(5)));

                    player.itemTime.lastTimeexpTamkjll1_5 = System.currentTimeMillis()
                            - (ItemTime.TIME_X_EXP - timeexptamkjll1_5);
                    player.itemTime.lastTimeexpTamkjll2 = System.currentTimeMillis()
                            - (ItemTime.TIME_X_EXP - timeexptamkjll2);
                    player.itemTime.lastTimeexpTamkjll3 = System.currentTimeMillis()
                            - (ItemTime.TIME_X_EXP - timeexptamkjll3);
                    player.itemTime.lastTimeexpTamkjll4 = System.currentTimeMillis()
                            - (ItemTime.TIME_X_EXP - timeexptamkjll4);
                    player.itemTime.lastTimeexpTamkjll5 = System.currentTimeMillis()
                            - (ItemTime.TIME_X_EXP - timeexptamkjll5);
                    player.itemTime.lastTimeexpTamkjll6 = System.currentTimeMillis()
                            - (ItemTime.TIME_X_EXP - timeexptamkjll6);

                    player.itemTime.isXexpTamkjll1_5 = timeexptamkjll1_5 != 0;
                    player.itemTime.isXexpTamkjll2 = timeexptamkjll2 != 0;
                    player.itemTime.isXexpTamkjll3 = timeexptamkjll3 != 0;
                    player.itemTime.isXexpTamkjll4 = timeexptamkjll4 != 0;
                    player.itemTime.isXexpTamkjll5 = timeexptamkjll5 != 0;
                    player.itemTime.isXexpTamkjll6 = timeexptamkjll6 != 0;
                    dataArray.clear();

                    dataArray = (JSONArray) JSONValue.parse(rs.getString("data_diem"));
                    player.vip = Byte.parseByte(String.valueOf(dataArray.get(0)));
                    player.timevip = Long.parseLong(String.valueOf(dataArray.get(1)));
                    dataArray.clear();

                    //data nhiệm vụ
                    dataArray = (JSONArray) jv.parse(rs.getString("data_task"));
                    TaskMain taskMain = TaskService.gI().getTaskMainById(player, Byte.parseByte(dataArray.get(1).toString()));
                    if (taskMain != null) {
                        int subTaskIndex = Integer.parseInt(dataArray.get(2).toString());
                        if (taskMain.subTasks != null && subTaskIndex < taskMain.subTasks.size()) {
                            taskMain.subTasks.get(subTaskIndex).count = Short.parseShort(dataArray.get(0).toString());
                            taskMain.index = Byte.parseByte(dataArray.get(2).toString());
                            player.playerTask.taskMain = taskMain;
                        } else {
                            System.err.println("lỗi task");
                        }
                    } else {
                        System.err.println("taskMain là null.");
                    }

                    dataArray.clear();

                    //data nhiệm vụ hàng ngày
                    try {
                        dataArray = (JSONArray) jv.parse(rs.getString("data_side_task"));
                        String format = "dd-MM-yyyy";
                        long receivedTime = Long.parseLong(String.valueOf(dataArray.get(4)));
                        Date date = new Date(receivedTime);
                        if (TimeUtil.formatTime(date, format).equals(TimeUtil.formatTime(new Date(), format))) {
                            player.playerTask.sideTask.level = Integer.parseInt(String.valueOf(dataArray.get(0).toString()));
                            player.playerTask.sideTask.count = Integer.parseInt(dataArray.get(1).toString());
                            player.playerTask.sideTask.leftTask = Integer.parseInt(String.valueOf(dataArray.get(2).toString()));
                            player.playerTask.sideTask.template = TaskService.gI().getSideTaskTemplateById(Integer.parseInt(dataArray.get(3).toString()));
                            player.playerTask.sideTask.maxCount = Integer.parseInt(String.valueOf(dataArray.get(5).toString()));
                            player.playerTask.sideTask.receivedTime = receivedTime;
                        }
                    } catch (Exception e) {
                    }

                    dataArray = (JSONArray) jv.parse(rs.getString("achivements"));
                    for (int i = 0; i < dataArray.size(); i++) {
                        dataObject = (JSONObject) jv.parse(String.valueOf(dataArray.get(i)));
                        Achivement achivement = new Achivement();
                        achivement.setId(Integer.parseInt(dataObject.get("id").toString()));
                        achivement.setCount(Integer.parseInt(dataObject.get("count").toString()));
                        achivement.setFinish(Integer.parseInt(dataObject.get("finish").toString()) == 1);
                        achivement.setReceive(Integer.parseInt(dataObject.get("receive").toString()) == 1);
                        AchivementTemplate a = AchiveManager.getInstance().findByID(achivement.getId());
                        achivement.setName(a.getName());
                        achivement.setDetail(a.getDetail());
                        achivement.setMaxCount(a.getMaxCount());
                        achivement.setMoney(a.getMoney());
                        player.playerTask.achivements.add(achivement);
                    }

                    List<AchivementTemplate> listAchivements = AchiveManager.getInstance().getList();
                    if (dataArray.size() < listAchivements.size()) { //add thêm nhiệm vụ khi có nhiệm vụ mới
                        for (int i = dataArray.size(); i < listAchivements.size(); i++) {
                            AchivementTemplate a = AchiveManager.getInstance().findByID(i);
                            Achivement achivement = new Achivement();
                            if (a != null) {
                                achivement.setId(a.getId());
                                achivement.setCount(0);
                                achivement.setFinish(false);
                                achivement.setReceive(false);
                                achivement.setName(a.getName());
                                achivement.setDetail(a.getDetail());
                                achivement.setMaxCount(a.getMaxCount());
                                achivement.setMoney(a.getMoney());
                                player.playerTask.achivements.add(achivement);
                            }
                        }
                    }
                    dataArray.clear();

                    //data trứng bư
                    dataObject = (JSONObject) jv.parse(rs.getString("data_mabu_egg"));
                    Object createTime = dataObject.get("create_time");
                    if (createTime != null) {
                        player.mabuEgg = new MabuEgg(player, Long.parseLong(String.valueOf(createTime)),
                                Long.parseLong(String.valueOf(dataObject.get("time_done"))));
                    }
                    dataObject.clear();
                    //data bill
                    dataObject = (JSONObject) jv.parse(rs.getString("data_bill_egg"));
                    Object ct = dataObject.get("create_time");
                    if (ct != null) {
                        player.billEgg = new BillEgg(player, Long.parseLong(String.valueOf(ct)),
                                Long.parseLong(String.valueOf(dataObject.get("time_done"))));
                    }
                    dataObject.clear();

                    ///duahau
                    dataObject = (JSONObject) jv.parse(rs.getString("data_duahau"));
                    Object duahautime = dataObject.get("create_time");
                    if (duahautime != null) {
                        player.duahau = new duahau(player, Long.parseLong(String.valueOf(duahautime)),
                                Long.parseLong(String.valueOf(dataObject.get("time_done"))));
                    }
                    dataObject.clear();

                    dataObject = (JSONObject) jv.parse(rs.getString("kickvip"));
                    Object datakickvip = dataObject.get("create_time");
                    if (datakickvip != null) {
                        player.kickvip = new kickvip(player, Long.parseLong(String.valueOf(datakickvip)),
                                Long.parseLong(String.valueOf(dataObject.get("time_done"))));
                    }
                    dataObject.clear();

                    //data bùa
                    dataArray = (JSONArray) jv.parse(rs.getString("data_charm"));
                    player.charms.tdTriTue = Long.parseLong(dataArray.get(0).toString());
                    player.charms.tdManhMe = Long.parseLong(dataArray.get(1).toString());
                    player.charms.tdDaTrau = Long.parseLong(dataArray.get(2).toString());
                    player.charms.tdOaiHung = Long.parseLong(dataArray.get(3).toString());
                    player.charms.tdBatTu = Long.parseLong(dataArray.get(4).toString());
                    player.charms.tdDeoDai = Long.parseLong(dataArray.get(5).toString());
                    player.charms.tdThuHut = Long.parseLong(dataArray.get(6).toString());
                    player.charms.tdDeTu = Long.parseLong(dataArray.get(7).toString());
                    player.charms.tdTriTue3 = Long.parseLong(dataArray.get(8).toString());
                    player.charms.tdTriTue4 = Long.parseLong(dataArray.get(9).toString());
                    if (dataArray.size() >= 11) {
                        player.charms.tdDeTuMabu = Long.parseLong(dataArray.get(10).toString());
                    }
                    dataArray.clear();

                    dataArray = (JSONArray) JSONValue.parse(rs.getString("diemdanh"));
                    player.CheckDayOnl = Byte.parseByte(String.valueOf(dataArray.get(0).toString()));
                    player.diemdanh = Long.parseLong(String.valueOf(dataArray.get(1).toString()));
                    dataArray.clear();
                    if (player.CheckDayOnl == 0) {
                        player.CheckDayOnl = (byte) (LocalDate.now().getDayOfMonth());
                    }
                    
                    dataArray = (JSONArray) JSONValue.parse(rs.getString("score_board"));
                    for(int i = 0;i<player.scoreBoards.length && i < dataArray.size();i++){
                        if (LocalDate.now().getDayOfMonth() > player.CheckDayOnl){
                            player.scoreBoards[i] = 0;
                        }else{
                            player.scoreBoards[i] = Long.parseLong(dataArray.get(i).toString());
                        }
                    }
                    dataArray.clear();
                    if (LocalDate.now().getDayOfMonth() > player.CheckDayOnl) {
                        player.CheckDayOnl = (byte) LocalDate.now().getDayOfMonth();
                        player.diemdanh = 0;
                        
                    }

                    //data skill
                    dataArray = (JSONArray) jv.parse(rs.getString("skills"));
                    for (int i = 0; i < dataArray.size(); i++) {
                        JSONArray skillTemp = (JSONArray) jv.parse(String.valueOf(dataArray.get(i)));
                        int tempId = Integer.parseInt(skillTemp.get(0).toString());
                        byte point = Byte.parseByte(skillTemp.get(2).toString());
                        Skill skill = null;
                        if (point != 0) {
                            skill = SkillUtil.createSkill(tempId, point);
                        } else {
                            skill = SkillUtil.createSkillLevel0(tempId);
                        }
                        skill.lastTimeUseThisSkill = Long.parseLong(skillTemp.get(1).toString());
                        player.playerSkill.skills.add(skill);
                        skillTemp.clear();
                    }
                    if (dataArray.size() < 12) {
                        player.playerSkill.skills.add(SkillUtil.createSkillLevel0(Skill.PHAN_THAN));
                    }
                    dataArray.clear();

                    //data skill shortcut
                    dataArray = (JSONArray) jv.parse(rs.getString("skills_shortcut"));
                    for (int i = 0; i < dataArray.size(); i++) {
                        player.playerSkill.skillShortCut[i] = Byte.parseByte(String.valueOf(dataArray.get(i)));
                    }
                    for (int i : player.playerSkill.skillShortCut) {
                        if (player.playerSkill.getSkillbyId(i) != null && player.playerSkill.getSkillbyId(i).damage > 0) {
                            player.playerSkill.skillSelect = player.playerSkill.getSkillbyId(i);
                            break;
                        }
                    }
                    if (player.playerSkill.skillSelect == null) {
                        player.playerSkill.skillSelect = player.playerSkill.getSkillbyId(player.gender == ConstPlayer.TRAI_DAT
                                ? Skill.DRAGON : (player.gender == ConstPlayer.NAMEC ? Skill.DEMON : Skill.GALICK));
                    }
                    dataArray.clear();

                    Gson gson = new Gson();
                    List<Card> cards = gson.fromJson(rs.getString("collection_book"), new TypeToken<List<Card>>() {
                    }.getType());

                    CollectionBook book = new CollectionBook(player);
                    if (cards != null) {
                        book.setCards(cards);
                    } else {
                        book.setCards(new ArrayList<>());
                    }
                    book.init();
                    player.setCollectionBook(book);
                    List<Item> itemsBody = player.inventory.itemsBody;
                    while (itemsBody.size() < 11) {
                        itemsBody.add(ItemService.gI().createItemNull());
                    }

                    if (itemsBody.get(9).isNotNullItem()) {
                        MiniPet.callMiniPet(player, (player.inventory.itemsBody.get(9).template.id));
                    }
                    if (itemsBody.get(10).isNotNullItem()) {
                        PetFollow pet = PetFollowManager.gI().findByID(itemsBody.get(10).getId());
                        player.setPetFollow(pet);
                    }
                    player.firstTimeLogin = rs.getTimestamp("firstTimeLogin");

                    dataArray = (JSONArray) JSONValue.parse(rs.getString("buy_limit"));
                    for (int i = 0; i < dataArray.size(); i++) {
                        player.buyLimit[i] = Byte.parseByte(dataArray.get(i).toString());
                    }

                    dataArray = (JSONArray) JSONValue.parse(rs.getString("reward_limit"));

                    player.rewardLimit = new byte[dataArray.size()];
                    for (int i = 0; i < dataArray.size(); i++) {
                        player.rewardLimit[i] = Byte.parseByte(dataArray.get(i).toString());
                    }

                    //dhvt23
                    dataArray = (JSONArray) JSONValue.parse(rs.getString("challenge"));
                    player.goldChallenge = Integer.parseInt(dataArray.get(0).toString());
                    player.levelWoodChest = Integer.parseInt(dataArray.get(1).toString());
                    player.receivedWoodChest = Integer.parseInt(dataArray.get(2).toString()) == 1;
                    dataArray.clear();

                    PlayerService.gI().dailyLogin(player);

                    //data pet
                    dataObject = (JSONObject) jv.parse(rs.getString("pet_info"));
                    if (!String.valueOf(dataObject).equals("{}")) {
                        Pet pet = new Pet(player);
                        pet.id = -player.id;
                        pet.gender = Byte.parseByte(String.valueOf(dataObject.get("gender")));
                        pet.typePet = Byte.parseByte(String.valueOf(dataObject.get("is_mabu")));
                        pet.name = String.valueOf(dataObject.get("name"));
                        player.fusion.typeFusion = Byte.parseByte(String.valueOf(dataObject.get("type_fusion")));
                        player.fusion.lastTimeFusion = System.currentTimeMillis()
                                - (Fusion.TIME_FUSION - Integer.parseInt(String.valueOf(dataObject.get("left_fusion"))));
                        pet.status = Byte.parseByte(String.valueOf(dataObject.get("status")));

                        try {
                            pet.setLever(Integer.parseInt(String.valueOf(dataObject.get("level"))));
                        } catch (Exception e) {
                            pet.setLever(0);
                        }

                        //data chỉ số
                        dataObject = (JSONObject) jv.parse(rs.getString("pet_point"));
                        pet.nPoint.stamina = Short.parseShort(String.valueOf(dataObject.get("stamina")));
                        pet.nPoint.maxStamina = Short.parseShort(String.valueOf(dataObject.get("max_stamina")));
                        pet.nPoint.hpg = Double.parseDouble(String.valueOf(dataObject.get("hpg")));
                        pet.nPoint.mpg = Double.parseDouble(String.valueOf(dataObject.get("mpg")));
                        pet.nPoint.dameg = Double.parseDouble(String.valueOf(dataObject.get("damg")));
                        pet.nPoint.defg = Double.parseDouble(String.valueOf(dataObject.get("defg")));
                        pet.nPoint.critg = Integer.parseInt(String.valueOf(dataObject.get("critg")));
                        pet.nPoint.power = Long.parseLong(String.valueOf(dataObject.get("power")));
                        pet.nPoint.tiemNang = Long.parseLong(String.valueOf(dataObject.get("tiem_nang")));
                        pet.nPoint.limitPower = Byte.parseByte(String.valueOf(dataObject.get("limit_power")));
                        double hp = Double.parseDouble(String.valueOf(dataObject.get("hp")));
                        double mp = Double.parseDouble(String.valueOf(dataObject.get("mp")));

                        //data body
                        if ((new java.sql.Date(player.diemdanh)).getDay() != (new java.sql.Date(System.currentTimeMillis())).getDay()) {
                            player.diemdanh = 0;
                        }
                        dataArray = (JSONArray) jv.parse(rs.getString("pet_body"));
                        for (int i = 0; i < dataArray.size(); i++) {
                            dataObject = (JSONObject) dataArray.get(i);
                            Item item = null;
                            short tempId = Short.parseShort(String.valueOf(dataObject.get("temp_id")));
                            if (tempId != -1) {
                                item = ItemService.gI().createNewItem(tempId, Integer.parseInt(String.valueOf(dataObject.get("quantity"))));
                                JSONArray options = (JSONArray) dataObject.get("option");
                                for (int j = 0; j < options.size(); j++) {
                                    JSONArray opt = (JSONArray) options.get(j);
                                    item.itemOptions.add(new ItemOption(Integer.parseInt(String.valueOf(opt.get(0))),
                                            Long.parseLong(String.valueOf(opt.get(1)))));
                                }
                                item.createTime = Long.parseLong(String.valueOf(dataObject.get("create_time")));
                                if (ItemService.gI().isOutOfDateTime(item)) {
                                    item = ItemService.gI().createItemNull();
                                }
                            } else {
                                item = ItemService.gI().createItemNull();
                            }
                            pet.inventory.itemsBody.add(item);
                        }
                        //data skills
                        dataArray = (JSONArray) jv.parse(rs.getString("pet_skill"));
                        for (int i = 0; i < dataArray.size(); i++) {
                            JSONArray skillTemp = (JSONArray) dataArray.get(i);
                            int tempId = Integer.parseInt(String.valueOf(skillTemp.get(0)));
                            byte point = Byte.parseByte(String.valueOf(skillTemp.get(1)));
                            Skill skill = null;
                            if (point != 0) {
                                skill = SkillUtil.createSkill(tempId, point);
                            } else {
                                skill = SkillUtil.createSkillLevel0(tempId);
                            }
                            switch (skill.template.id) {
                                case Skill.KAMEJOKO:
                                case Skill.MASENKO:
                                case Skill.ANTOMIC:
                                    skill.coolDown = 1000;
                                    break;
                            }
                            pet.playerSkill.skills.add(skill);
                        }
                        pet.nPoint.hp = hp;
                        pet.nPoint.mp = mp;
//                    pet.nPoint.calPoint();
                        player.pet = pet;
                    }
//                    if (player.vip == 4) {
//                        Service.getInstance().sendThongBaoBenDuoi("Thành viên  VIP 4 " + player.name + " đã đăng nhập!");
//                        Service.getInstance().sendThongBaoAllPlayer("|2|Thành Viên VIP 4\n|7|[" + player.name + "]\n|2| Đã đăng nhập!");
//                    }
//                    if (player.vip == 3) {
//                        Service.getInstance().sendThongBaoBenDuoi("Thành viên  VIP 3 " + player.name + " đã đăng nhập!");
//                        Service.getInstance().sendThongBaoAllPlayer("|2|Thành Viên VIP 3\n|7|[" + player.name + "]\n|2| Đã đăng nhập!");
//                    }
//                    if (player.vip == 2) {
//                        Service.getInstance().sendThongBaoBenDuoi("Thành viên  VIP 2 " + player.name + " đã đăng nhập!");
//                        Service.getInstance().sendThongBaoAllPlayer("|2|Thành Viên VIP 2\n|7|[" + player.name + "]\n|2| Đã đăng nhập!");
//                    }
//                    if (player.vip == 1) {
//                        Service.getInstance().sendThongBaoBenDuoi("Thành viên  VIP 1 " + player.name + " đã đăng nhập!");
//                        Service.getInstance().sendThongBaoAllPlayer("|2|Thành Viên VIP 1\n|7|[" + player.name + "]\n|2| Đã đăng nhập!");
//                    }
//                    if (player.vip == 0) {
//                        Service.getInstance().sendThongBaoBenDuoi("Con Nhà Nghèo " + player.name + " đã đăng nhập!");
//                        Service.getInstance().sendThongBaoAllPlayer("|2|Nghèo\n|7|[" + player.name + "]\n|2| đã đăng nhập!");
//                    }
//                    if (player.vip == 5) {
//                        Service.getInstance().sendThongBaoBenDuoi("Thành viên  VIP 5 " + player.name + " đã đăng nhập!");
//                        Service.getInstance().sendThongBaoAllPlayer("|2|Thành Viên VIP 5\n|7|[" + player.name + "]\n|2| Đã đăng nhập!");
//                    }
//                    if (player.vip == 6) {
//                        Service.getInstance().sendThongBaoBenDuoi("Thành viên  VIP 6 " + player.name + " đã đăng nhập!");
//                        Service.getInstance().sendThongBaoAllPlayer("|2|Thành Viên VIP 6\n|7|[" + player.name + "]\n|2| Đã đăng nhập!");
//                    }
//                    if (player.vip == 7) {
//                        Service.getInstance().sendThongBaoBenDuoi("Thành viên  VIP 7 " + player.name + " đã đăng nhập!");
//                        Service.getInstance().sendThongBaoAllPlayer("|2|Thành Viên VIP 7\n|7|[" + player.name + "]\n|2| Đã đăng nhập!");
//                    }
//                    if (player.vip == 8) {
//                        Service.getInstance().sendThongBaoBenDuoi("Thành viên  VIP 8 " + player.name + " đã đăng nhập!");
//                        Service.getInstance().sendThongBaoAllPlayer("|2|Thành Viên VIP 8\n|7|[" + player.name + "]\n|2| Đã đăng nhập!");
//                    }
//                    if (player.vip == 9) {
//                        Service.getInstance().sendThongBaoBenDuoi("Thành viên  VIP 9 " + player.name + " đã đăng nhập!");
//                        Service.getInstance().sendThongBaoAllPlayer("|2|Thành Viên VIP 9\n|7|[" + player.name + "]\n|2| Đã đăng nhập!");
//                    } 
//                     if (player.vip == 10) {
//                        Service.getInstance().sendThongBaoBenDuoi("Thành viên  VIP 10 " + player.name + " đã đăng nhập!");
//                        Service.getInstance().sendThongBaoAllPlayer("|2|Thành Viên VIP 10\n|7|[" + player.name + "]\n|2| Đã đăng nhập!");
//                    }
                     if (player.vip >= 1) {
                        Service.getInstance().sendThongBaoBenDuoi("Thánh Nhân VIP " + player.vip + " " + player.name + " đã đăng nhập!");
                        Service.getInstance().sendThongBaoAllPlayer("|2|Thánh Nhân VIP " + player.vip + "\n|7|[" + player.name + "]\n|2| Đã đăng nhập!");
                    }
                     if (player.vip == 0) {
                        Service.getInstance().sendThongBaoBenDuoi("Kiến Hôi VIP " + player.vip + " " + player.name + " đã đăng nhập!");
                        Service.getInstance().sendThongBaoAllPlayer("|2|Kiến Hôi VIP " + player.vip + "\n|7|[" + player.name + "]\n|2| Đã đăng nhập!");
                    }

//                    if (session.ruby > 0) {
//                        player.inventory.ruby += session.ruby;
//                        player.playerTask.achivements.get(ConstAchive.LAN_DAU_NAP_NGOC).count += session.ruby;
//                        PlayerDAO.subRuby(player, session.userId, session.ruby);
//                    }
                    player.nPoint.hp = plHp;
                    player.nPoint.mp = plMp;
                    session.player = player;
                    PreparedStatement ps2 = connection.prepareStatement("update account set last_time_login = ?, ip_address = ? where id = ?");
                    ps2.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
                    ps2.setString(2, session.ipAddress);
                    ps2.setInt(3, session.userId);
                    ps2.executeUpdate();
                    ps2.close();
                    return player;
                }
            } finally {
                rs.close();
                ps.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            session.dataLoadFailed = true;
        }
        return null;
    }

    public static Player loadById(int id) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Player player = null;
        if (Client.gI().getPlayer(id) != null) {
            return Client.gI().getPlayer(id);
        }
        try (Connection connection = DBService.gI().getConnectionForGetPlayer();) {
            ps = connection.prepareStatement("select * from player where id = ? limit 1");
            ps.setInt(1, id);
            rs = ps.executeQuery();
            try {
                if (rs.next()) {
                    double plHp = 200000000;
                    double plMp = 200000000;
                    JSONValue jv = new JSONValue();
                    JSONArray dataArray = null;
                    JSONObject dataObject = null;

                    player = new Player();

                    //base info
                    player.id = rs.getInt("id");
                    player.name = rs.getString("name");
                    player.head = rs.getShort("head");
                    player.gender = rs.getByte("gender");
                    player.dameBoss = rs.getDouble("dameBoss");
                    //data chỉ số
                    dataArray = (JSONArray) jv.parse(rs.getString("data_point"));
                    plMp = Double.parseDouble(dataArray.get(1).toString());
                    player.nPoint.mpg = Double.parseDouble(dataArray.get(2).toString());
                    player.nPoint.critg = Byte.parseByte(dataArray.get(3).toString());
                    player.nPoint.limitPower = Byte.parseByte(dataArray.get(4).toString());
                    player.nPoint.stamina = Short.parseShort(dataArray.get(5).toString());
                    plHp = Double.parseDouble(dataArray.get(6).toString());
                    player.nPoint.defg = Double.parseDouble(dataArray.get(7).toString());
                    player.nPoint.tiemNang = Long.parseLong(dataArray.get(8).toString());
                    player.nPoint.maxStamina = Short.parseShort(dataArray.get(9).toString());
                    player.nPoint.dameg = Double.parseDouble(dataArray.get(10).toString());
                    player.nPoint.power = Long.parseLong(dataArray.get(11).toString());
                    player.nPoint.hpg = Double.parseDouble(dataArray.get(12).toString());
                    dataArray.clear();

                    dataArray = (JSONArray) jv.parse(rs.getString("items_body"));
                    for (int i = 0; i < dataArray.size(); i++) {
                        Item item = null;
                        dataObject = (JSONObject) dataArray.get(i);
                        short tempId = Short.parseShort(String.valueOf(dataObject.get("temp_id")));
                        if (tempId != -1) {
                            item = ItemService.gI().createNewItem(tempId, Integer.parseInt(String.valueOf(dataObject.get("quantity"))));
                            JSONArray options = (JSONArray) dataObject.get("option");
                            for (int j = 0; j < options.size(); j++) {
                                JSONArray opt = (JSONArray) options.get(j);
                                item.itemOptions.add(new ItemOption(Integer.parseInt(String.valueOf(opt.get(0))),
                                        Long.parseLong(String.valueOf(opt.get(1)))));
                            }
                            item.createTime = Long.parseLong(String.valueOf(dataObject.get("create_time")));
                            if (ItemService.gI().isOutOfDateTime(item)) {
                                item = ItemService.gI().createItemNull();
                            }
                        } else {
                            item = ItemService.gI().createItemNull();
                        }
                        player.inventory.itemsBody.add(item);
                    }
                    while (player.inventory.itemsBody.size() == 13) {
                        player.inventory.itemsBody.add(ItemService.gI().createItemNull());
                    }
                    dataArray.clear();
                    dataObject.clear();

                    //data nhiệm vụ
                    dataArray = (JSONArray) jv.parse(rs.getString("data_task"));
                    TaskMain taskMain = TaskService.gI().getTaskMainById(player, Byte.parseByte(dataArray.get(1).toString()));
                    taskMain.index = Byte.parseByte(dataArray.get(2).toString());
                    player.playerTask.taskMain = taskMain;
                    dataArray.clear();

                    player.nPoint.hp = plHp;
                    player.nPoint.mp = plMp;
                }
            } finally {
                try {
                    if (rs != null) {
                        rs.close();
                    }
                    if (ps != null) {
                        ps.close();
                    }
                } catch (SQLException ex) {
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return player;
    }

}
