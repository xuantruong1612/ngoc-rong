package nro.server;

import nro.consts.*;
import nro.data.DataGame;
import nro.data.ItemData;
import nro.jdbc.DBService;
import nro.models.consignment.ConsignmentShop;
import nro.models.map.war.BlackBallWar;
import nro.models.npc.NpcManager;
import nro.models.player.Player;
import nro.models.skill.PlayerSkill;
import nro.noti.NotiManager;
import nro.quayTamBao.TamBaoService;
import nro.resources.Resources;
import nro.server.io.Message;
import nro.server.io.Session;
import nro.services.*;
import nro.services.func.*;
import nro.utils.Log;
import nro.utils.Util;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import nro.event.BoardEvent;
import nro.models.boss.BossManager;
import nro.models.map.ItemMap;
import nro.models.skill.Skill;

public class Controller {

    private static Controller instance;

    public static Controller getInstance() {
        if (instance == null) {
            instance = new Controller();
        }
        return instance;
    }

    private static final Logger logger = Logger.getLogger(Controller.class);

    public void onMessage(Session _session, Message _msg) {
        long st = System.currentTimeMillis();
        try {
            Player player = _session.player;
            byte cmd = _msg.command;
            if (Manager.debug) {
                System.out.println("CMD receive: " + cmd);
            }
            switch (cmd) {
                case 70:
                    if(_session != null && _session.player != null){
                        this.messageBoardEventReward(_session, _msg);
                    }
                    break;
                case 69:
                    TamBaoService.readData(_msg, player);
                    break;
                case 29:
                    if (player != null && player.zone != null && player.zone.map != null) {
                        if (player.zone.map.mapId == ConstTranhNgocNamek.MAP_ID) {
                            Service.getInstance().sendPopUpMultiLine(player, 0, 7184, "Không thể thực hiện");
                            return;
                        }
                        Service.getInstance().openZoneUI(player);
                    }
                    break;
                case 21:
                    if (player != null && player.zone != null && player.zone.map != null) {
                        if (player.zone.map.mapId == ConstTranhNgocNamek.MAP_ID) {
                            Service.getInstance().sendPopUpMultiLine(player, 0, 7184, "Không thể thực hiện");
                            return;
                        }
                        int zoneId = _msg.reader().readByte();
                        ChangeMapService.gI().changeZone(player, zoneId);
                    }
                    break;
                case Cmd.KIGUI:
                    if (player != null) {
                        if (player.getSession().actived) {
                            ConsignmentShop.getInstance().handler(player, _msg);
                        } else {
                            ConsignmentShop.getInstance().show(player);
                            Service.getInstance().sendThongBao(player, "Vui lòng kích hoạt tài khoản!");
                        }
                    }
                    break;
                case Cmd.ACHIEVEMENT:
                    if (player != null) {
                        if (player.getSession().actived) {
                            TaskService.gI().rewardAchivement(player, _msg.reader().readByte());
                        } else {
                            TaskService.gI().sendAchivement(player);
                            Service.getInstance().sendThongBao(player, "Vui lòng kích hoạt thành viên");
                        }
                    }
                    break;
                case Cmd.RADA_CARD:
                    RadaService.getInstance().controller(player, _msg);
                    break;
                case -127:
                    if (player != null) {
                        LuckyRoundService.gI().readOpenBall(player, _msg);
                    }
                    break;
                case -125:
                    if (player != null) {
                        Input.gI().doInput(player, _msg);
                    }
                    break;
                case 112:
                    if (player != null) {
                        IntrinsicService.gI().showMenu(player);
                    }
                    break;
                case -34:
                    if (player != null) {
                        switch (_msg.reader().readByte()) {
                            case 1:
                                player.magicTree.openMenuTree();
                                break;
                            case 2:
                                player.magicTree.loadMagicTree();
                                break;
                        }
                    }
                    break;
                case -99:
                    if (player != null) {
                        FriendAndEnemyService.gI().controllerEnemy(player, _msg);
                    }
                    break;
                case 18:
                    if (player != null) {
                        FriendAndEnemyService.gI().goToPlayerWithYardrat(player, _msg);
                    }
                    break;
                case -72:
                    if (player != null) {
                        FriendAndEnemyService.gI().chatPrivate(player, _msg);
                    }
                    break;
                case -80:
                    if (player != null) {
                        FriendAndEnemyService.gI().controllerFriend(player, _msg);
                    }
                    break;
                case -59:
                    if (player != null) {
                        PVPServcice.gI().controller(player, _msg);
                    }
                    break;
                case -86:
                    if (player != null) {
                        if (player.getSession().actived) {
                            TransactionService.gI().controller(player, _msg);
                        } else {
                            Service.getInstance().sendThongBaoFromAdmin(player, "Vui lòng kích hoạt vip 4 Vừa Mở Khóa Gd All Đồ\n(Bao Gồm Pét ,Cải Trang,Linh Thú,Đồ Linh Tinh,Và Đặc Quyền Của vip 4)!");
                        }
                    }
                    break;
                case -107:
                    if (player != null) {
                        Service.getInstance().showInfoPet(player);
                    }
                    break;
                case -108:
                    if (player != null && player.pet != null) {
                        player.pet.changeStatus(_msg.reader().readByte());
                    }
                    break;
                case 5:
                    if (player != null && !Maintenance.isRuning) {
                        byte typeBuy = _msg.reader().readByte();
                        int tempId = _msg.reader().readShort();
                        int quantity = _msg.reader().readInt();
                        if (quantity < 0) {
                            Service.getInstance().sendThongBao(player, "Số lượng phải lớn hơn 0!");
                        } else {
                            for (int i = 0; i < quantity; i++) {
                                ShopService.gI().buyItem(player, typeBuy, tempId);
                            }
                        }
                    }
                    break;
                case 6: //buy item

                    if (player != null) {
                        byte typeBuy = _msg.reader().readByte();
                        int tempId = _msg.reader().readShort();
                        int quantity = 0;
                        try {
                            quantity = _msg.reader().readShort();
                        } catch (Exception e) {
                        }
                        ShopService.gI().buyItem(player, typeBuy, tempId);
                    }
                    break;
                case 7: //sell item
                    if (player != null) {
                        int action = _msg.reader().readByte();
                        int where = _msg.reader().readByte();
                        int index = _msg.reader().readShort();
                        if (action == 0) {
                            ShopService.gI().showConfirmSellItem(player, where,
                                    !player.isVersionAbove(220) ? index - 3 : index);
                        } else {
                            ShopService.gI().sellItem(player, where, index);
                        }
                    }
                    break;
                case Cmd.THACHDAU:
                    int pId = _msg.reader().readInt();
                    BossManager.gI().FindBoss(player, pId);
                    break;
                case -71:
                    if (player != null) {
                        ChatGlobalService.gI().chat(player, _msg.reader().readUTF());
                    }
                    break;
                case -79:
                    if (player != null) {
                        Service.getInstance().getPlayerMenu(player, _msg.reader().readInt());
                    }
                    break;
                case -113:
                    if (player != null) {
                        PlayerSkill playerSkill = player.playerSkill;
                        int len = _msg.reader().available();
                        for (int i = 0; i < len; i++) {
                            byte b = _msg.reader().readByte();
                            playerSkill.skillShortCut[i] = b;
                        }
                        playerSkill.sendSkillShortCut();
                    }
                    break;
                case -101:
                    login2(_session, _msg);
                    break;
                case 42:
                    Service.getInstance().regisAccount(_session, _msg);
                    break;
                case -120:
                    BossManager.gI().teleBoss(player, _msg);
                    break;
                case -121:
                    BossManager.gI().summonBoss(player, _msg);
                    break;

                case -122:
                    Service.getInstance().managePlayer(player, _msg);
                    break;

                case -103:
                    if (player != null) {
                        byte act = _msg.reader().readByte();
                        if (act == 0) {
                            Service.getInstance().openFlagUI(player);
                        } else if (act == 1) {
                            Service.getInstance().chooseFlag(player, _msg.reader().readByte());
                        } else {
//                        Util.log("id map" + player.map.id);
                        }
                    }
                    break;
                case -7:
                    if (player != null && player.location != null && !player.effectSkill.isHaveEffectSkill()) {
                        int toX = player.location.x;
                        int toY = player.location.y;
                        try {
                            byte b = _msg.reader().readByte();
                            toX = _msg.reader().readShort();
                            if (_msg.reader().available() >= 2) {
                                toY = _msg.reader().readShort();
                            } else {
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        PlayerService.gI().playerMove(player, toX, toY);
                    }
                    break;
                case Cmd.GET_IMAGE_SOURCE:
                    // System.out.println("-74");
                    Resources.getInstance().downloadResources(_session, _msg);
                    break;
                case -81:
                    if (player != null) {
                        _msg.reader().readByte();
                        int[] indexItem = new int[_msg.reader().readByte()];
                        for (int i = 0; i < indexItem.length; i++) {
                            indexItem[i] = _msg.reader().readByte();
                        }
//                    CombineService.gI().showInfoCombine(player, indexItem);
                        CombineServiceNew.gI().showInfoCombine(player, indexItem);
                    }
                    break;
                case -87:
                    DataGame.updateData(_session);
                    break;
                case Cmd.FINISH_UPDATE:
                    _session.finishUpdate();
                    break;
                case Cmd.REQUEST_ICON:
                    int id = _msg.reader().readInt();
                    Resources.getInstance().downloadIconData(_session, id);
                    break;
                case Cmd.GET_IMG_BY_NAME:
                    Resources.getInstance().sendImageByName(_session, _msg.reader().readUTF());
                    break;
                case -66:
                    int effId = _msg.reader().readShort();
                    int idT = effId;

                    if (effId == 25) {
                        idT = 50; // id eff rong muon thay doi ( hien tai la rong xuong)
                    }
                    if (effId == 25 && (player.zone.map.mapId == 1 || player.zone.map.mapId == 8 || player.zone.map.mapId == 15)) {
                        idT = 51; // id eff rong muon thay doi ( hien tai la rong xuong) 
                    }
                    if (effId == 25 && MapService.gI().isMapCold(player.zone.map)) {
                        idT = 59;
                    }
                    if (effId == 25 && player.zone.map.mapId == 5) {
                        idT = 50;
                    }
                    Resources.effData(_session, effId, idT);
                    break;
//                 test

                case -62:
                    if (player != null) {
                        FlagBagService.gI().sendIconFlagChoose(player, _msg.reader().readByte());
                    }
                    break;
                case -63:
                    if (player != null) {
                        FlagBagService.gI().sendIconEffectFlag(player, _msg.reader().readByte());
                    }
                    break;
                case Cmd.BACKGROUND_TEMPLATE:
                    int bgId = _msg.reader().readShort();
                    Resources.getInstance().downloadBGTemplate(_session, bgId);
                    break;
                case 22:
                    if (player != null) {
                        _msg.reader().readByte();
                        NpcManager.getNpc(ConstNpc.DAU_THAN).confirmMenu(player, _msg.reader().readByte());
                    }
                    break;
                case -33:

//                case -23:
//                    if (player != null) {
//                        player.zone.changeMapWaypoint(player);
//                        Service.getInstance().hideWaitDialog(player);
//                    }
//                    break;
                case -23:
                    if (player != null && player.zone != null) {
                        player.zone.changeMapWaypoint(player);
                        Service.getInstance().hideWaitDialog(player);
                    } else {
                        // Xử lý trường hợp player hoặc player.zone là null
                        if (player == null) {
                            System.out.println("Player is null.");
                        } else {
                            System.out.println("Player zone is null for player: " + player.name);
                        }
                    }
                    break;
                case -45:
                    if (player != null) {
                        SkillService.gI().useSkill(player, null, null, _msg);
                    }
                    break;
                case -46:
                    if (player != null) {
                        ClanService.gI().getClan(player, _msg);
                    }
                    break;
                case -51:
                    if (player != null) {
                        ClanService.gI().clanMessage(player, _msg);
                    }
                    break;
                case -54:
                    if (player != null) {
                        ClanService.gI().clanDonate(player, _msg);
//                        Service.getInstance().sendThongBao(player, "Can not invoke clan donate");
                    }
                    break;
                case -49:
                    if (player != null) {
                        ClanService.gI().joinClan(player, _msg);
                    }
                    break;

                case -50:
                    if (player != null) {
                        ClanService.gI().sendListMemberClan(player, _msg.reader().readInt());
                    }
                    break;
                case -56:
                    if (player != null) {
                        ClanService.gI().clanRemote(player, _msg);
                    }
                    break;
                case -47:
                    if (player != null) {
                        ClanService.gI().sendListClan(player, _msg.reader().readUTF());
                    }
                    break;
                case -55:
                    if (player != null) {
                        ClanService.gI().showMenuLeaveClan(player);
                    }
                    break;
                case -57:
                    if (player != null) {
                        ClanService.gI().clanInvite(player, _msg);
                    }
                    break;
                case -40:
                    UseItem.gI().getItem(_session, _msg);
                    break;
                case -41:
                    Service.getInstance().sendCaption(_session, _msg.reader().readByte());
                    break;
                case -43:
                    if (player != null) {
                        UseItem.gI().doItem(player, _msg);
                    }
                    break;
                case -91:
                    if (player != null) {
                        switch (player.iDMark.getTypeChangeMap()) {
                            case ConstMap.CHANGE_CAPSULE:
                                UseItem.gI().choseMapCapsule(player, _msg.reader().readByte());
                                break;
                            case ConstMap.CHANGE_BLACK_BALL:
                                BlackBallWar.gI().changeMap(player, _msg.reader().readByte());
                                break;
                        }
                    }
                    break;
                case -39:
                    if (player != null) {
                        //finishLoadMap
                        ChangeMapService.gI().finishLoadMap(player);
                        if (player.zone.map.mapId == (21 + player.gender)) {
                            if (player.mabuEgg != null) {
                                player.mabuEgg.sendMabuEgg();
                            }
//                            Logger.log(Logger.PURPLE, "done load map nhà!\n");
                        }
                        if (player.zone.map.mapId == 154) {
                            if (player.billEgg != null) {
                                player.billEgg.sendBillEgg();
                            }
                        }
                        if (player.zone.map.mapId == 0) {
                            if (player.duahau != null) {
                                player.duahau.sendduahau();
                            }
                        }
                        EffectMapService.gI().sendEffEvent(player);
                    }
                    break;
                case 11:
                    byte modId = _msg.reader().readByte();
                    Resources.getInstance().loadMoData(_session, modId);
                    break;
                case 44:
                    if (player != null) {
                        String text = _msg.reader().readUTF();
                        Service.getInstance().chat(player, text);
                    }
                    break;
                case 32:
                    if (player != null) {
                        int npcId = _msg.reader().readShort();
                        int select = _msg.reader().readByte();
                        MenuController.getInstance().doSelectMenu(player, npcId, select);
                    }
                    break;
                case 33:
                    if (player != null) {
                        int npcId = _msg.reader().readShort();
                        if (npcId != 54) {
                            MenuController.getInstance().openMenuNPC(_session, npcId, player);
                        } else {
                            Service.getInstance().minigame_taixiu(player);
                        }
                    }
                    break;
                case 34:
                    if (player == null) {
                        return;
                    }
                    int selectSkill = _msg.reader().readShort();
                    SkillService.gI().selectSkill(player, selectSkill);
                    if (selectSkill == Skill.SUPER_TRANFORMATION) {
                        if (player.clone != null && player.clone.zone != null) {
                            SkillService.gI().useSkillTranformation(player.clone);
                        }
                        SkillService.gI().useSkillTranformation(player);
                    } else if (selectSkill == Skill.EVOLUTION) {
                        if (player.clone != null && player.clone.zone != null) {
                            SkillService.gI().useSkillEvolution(player.clone);
                        }
                        SkillService.gI().useSkillEvolution(player);
                    } else if (selectSkill == Skill.PHAN_THAN) {
                      //  SkillService.gI().useSkillPhanThan(player);
                    }
                    break;
                case 54:
                    if (player != null) {
                        Service.getInstance().attackMob(player, (int) (_msg.reader().readByte()));
                    }
                    break;
                case -60:
                    if (player != null) {
                        int playerId = _msg.reader().readInt();
                        Service.getInstance().attackPlayer(player, playerId);
                    }
                    break;
                case -27:
                    _session.sendSessionKey();
                    break;
                case -111:
                    System.out.println("send image version");
                    DataGame.sendDataImageVersion(_session);
                    break;
                case -20:
                    if (player != null && !player.isDie()) {
                        int itemMapId = _msg.reader().readShort();
                        try {
                            ItemMapService.gI().pickItem(player, itemMapId);
                        } catch (Exception e) {

                            System.err.println("Lỗi khi nhặt item: " + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                    break;
                case -28:
                    messageNotMap(_session, _msg);
                    break;
                case -29:
                    messageNotLogin(_session, _msg);
                    break;
                case -30:
                    messageSubCommand(_session, _msg);
                    break;
                case -15: // về nhà
                    if (player != null) {
                        player.isGoHome = true;
                        ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, 0, -1);
                        player.isGoHome = false;
                    }
                    break;
                case -16: // hồi sinh
                    if (player != null) {
                        PlayerService.gI().hoiSinh(player);
                    }
                    break;
                default:
//                    Util.log("CMD: " + cmd);
                    break;
            }
            if (_session.logCheck) {
//                System.out.println("Time do controller (" + cmd + "): " + (System.currentTimeMillis() - st) + " ms");
            }
        } catch (Exception e) {
            logger.error("Err controller message command: " + _msg.command, e);
        }
    }

    public void messageNotLogin(Session session, Message msg) {
        if (msg != null) {
            try {
                byte cmd = msg.reader().readByte();
                switch (cmd) {
                    case 0:
                        session.login(msg.reader().readUTF(), msg.reader().readUTF());
                        break;
                    case 2:
                        session.setClientType(msg);
                        break;
                    default:
                        break;
                }
            } catch (IOException e) {
//                Log.error(Controller.class, e);
            }
        }
    }

    public void messageNotMap(Session _session, Message _msg) {
        if (_msg != null) {
            try {
                byte cmd = _msg.reader().readByte();
//                System.out.println("CMD receive -28 / " + cmd);
                switch (cmd) {
                    case 2:
                        createChar(_session, _msg);
                        break;
                    case 6:
                        DataGame.createMap(_session);
                        break;
                    case 7:
                        DataGame.updateSkill(_session);
                        break;
                    case 8:
                        ItemData.updateItem(_session);
                        break;
                    case 10:
                        DataGame.sendMapTemp(_session, _msg.reader().readUnsignedByte());
                        break;
                    default:
                        break;
                }
            } catch (IOException e) {
                Log.error(Controller.class, e);
            }
        }
    }

    public void messageBoardEventReward(Session _session, Message _msg) {
        try {
            if (_msg != null) {
                Player player = _session.player;
                byte cmd = _msg.reader().readByte();
                switch(cmd){
                    case 0:
                        BoardEvent.SendMocNap(player);
                        break;
                    case 1:
                        BoardEvent.getCurrentIndex(_msg, player);
                        break;
                    case 3:
                        BoardEvent.sendEventStr(player);
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void messageSubCommand(Session _session, Message _msg) {
        if (_msg != null) {
            try {
                Player player = _session.player;
                byte command = _msg.reader().readByte();
                switch (command) {
                    case 20:

                        break;
                    case 16:
                        byte type = _msg.reader().readByte();
                        short point = _msg.reader().readShort();
                        if (player != null && player.nPoint != null) {
                            player.nPoint.increasePoint(type, point);
                        }
                        break;
                    case 64:
                        int playerId = _msg.reader().readInt();
                        int menuId = _msg.reader().readShort();
                        SubMenuService.gI().controller(player, playerId, menuId);
                        break;
                    default:
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void createChar(Session session, Message msg) {
        if (!Maintenance.isRuning) {
            PreparedStatement ps = null;
            ResultSet rs = null;
            boolean created = false;
            try (Connection con = DBService.gI().getConnectionCreatPlayer();) {
                String name = msg.reader().readUTF();
                int gender = msg.reader().readByte();
                int hair = msg.reader().readByte();
                if (name.length() <= 20 && name.length() >= 2) {
                    ps = con.prepareStatement("select * from player where name = ? or account_id = ?");
                    ps.setString(1, name);
                    ps.setInt(2, session.userId);
                    rs = ps.executeQuery();
                    if (rs.next()) {
                        Service.getInstance().sendThongBaoOK(session, "Tên nhân vật đã tồn tại");
                    } else {
                        if (Util.haveSpecialCharacter(name)) {
                            Service.getInstance().sendThongBaoOK(session, "Tên nhân vật không được chứa ký tự đặc biệt");
                        } else {
                            boolean isNotIgnoreName = true;
                            for (String n : ConstIgnoreName.IGNORE_NAME) {
                                if (name.equals(n)) {
                                    Service.getInstance().sendThongBaoOK(session, "Tên nhân vật đã tồn tại");
                                    isNotIgnoreName = false;
                                    break;
                                }
                            }
                            if (isNotIgnoreName) {
                                created = PlayerService.gI().createPlayer(con, session.userId, name.toLowerCase(), gender, hair, ps);
                            }
                        }
                    }
                } else {
                    Service.getInstance().sendThongBaoOK(session, "Tên nhân vật tối thiểu 3 kí tự và tối đa 20 ký tự");
                }
            } catch (Exception e) {
                Log.error(Controller.class, e);
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
            if (created) {
                session.finishUpdate();
            }
        }
    }

    public void login2(Session session, Message msg) {
        Service.getInstance().switchToRegisterScr(session);
        // Service.getInstance().sendThongBaoOK(session, "Vui Lòng Đăng Ký Tài Khoản Tại Trang Chủ");
    }

    public void sendInfo(Session session) {
        Player player = session.player;

        DataGame.sendDataItemBG(session);
        // -82 set tile map
        DataGame.sendTileSetInfo(session);

        // 112 my info intrinsic
        IntrinsicService.gI().sendInfoIntrinsic(player);

        // -42 my point
        Service.getInstance().point(player);

        // 40 task
        TaskService.gI().sendTaskMain(player);

        // -22 reset all
        Service.getInstance().clearMap(player);

        // -53 my clan
        ClanService.gI().sendMyClan(player);

        // -69 max statima
        PlayerService.gI().sendMaxStamina(player);

        // -68 cur statima
        PlayerService.gI().sendCurrentStamina(player);

        // -97 năng động
        // -107 have pet
        Service.getInstance().sendHavePet(player);

        // -119 top rank
        Service.getInstance().sendTopRank(player);

        // -50 thông tin bảng thông báo
        // -24 join map - map info
        player.zone.load_Me_To_Another(player);
        player.zone.mapInfo(player);

        // clear vật phẩm sự kiện
        clearVTSK(player);

        // -70 thông báo bigmessage
        //check activation set
        player.setClothes.setup();
        if (player.pet != null) {
            player.pet.setClothes.setup();
        }
        if (player.isPl()) {
            if (player.inventory.itemsBody.get(10).isNotNullItem()) {
                new Thread(() -> {
                    try {
                        Thread.sleep(1000);
                        Service.getInstance().sendPetFollow(player, (short) (player.inventory.itemsBody.get(10).template.iconID - 1));
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.err.println("Loi player: " + player.name);
                        Service.getInstance().sendThongBaoFromAdmin(player, "Da co loi xay ra");
                    }
                }).start();
            }
        }
        //last time use skill
//        Service.getInstance().sendTimeSkill(player);
//        if (TaskService.gI().getIdTask(player) == ConstTask.TASK_0_0) {
//            NpcService.gI().createTutorial(player, -1,
//                    "Chào mừng " + player.name + " đến với " + "Ngọc Rồng online" + "\n"
//                    + "Nhiệm vụ đầu tiên của bạn là Làm Quen Với Game\n"
//                    + "Bạn hãy Săn Up Từ Boss + quái con Kiếm Nhiều Tài Nguyên nhé");
//        }
        if (player.CapTamkjll >= 1) {
            ServerNotify.gI().notify("User : " + player.name + ", Tiên Bang : "
                    + player.CapTamkjll + " đã vào game");
        }
        if (player.Tamkjlltutien[1] >= 1) {
            ServerNotify.gI().notify("Đạo Hữu : [" + player.name + "], Cảnh giới : "
                    + player.TamkjllTuviTutien(Util.maxInt(player.Tamkjlltutien[1])) + " đã vào game");
        }
//        if (player.nPoint.power >= 1) {
//            Service.getInstance().sendThongBaoFromAdmin(player, "|8|Chào Mừng Đạo Hữu : \n"
//                    + "|7|"
//                    + "[" + player.name + "]\n"
//                    + "|7|\nVIP : " + player.vip + "\n"
//                    + "|8|Cảnh giới : "
//                    + player.TamkjllTuviTutien(Util.maxInt(player.Tamkjlltutien[1]))
//                    + "\n Đã Tham Gia Ngọc Rồng online\n"
//                    + "Chi Tiết Liên Hệ ADMIN\n"
//                    + "Zalo 0XXXXXXX ADMIN");
//        }

        NotiManager.getInstance().sendNoti(player);
        ConsignmentShop.getInstance().sendExpirationNotification(player);
        player.timeFixInventory = System.currentTimeMillis() + 500;

        try (Connection con = DBService.gI().getConnection(); PreparedStatement ps = con.prepareStatement("select * from account where id = ?")) {
            ps.setLong(1, player.getSession().userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    player.getSession().vnd = rs.getInt("vnd");
                    player.getSession().tongnap = rs.getInt("tongnap");
                    player.getSession().ruby = rs.getInt("ruby");

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void clearVTSK(Player player) {// clear item duoi khi
        if (player != null && player.inventory != null && player.inventory.itemsBag != null) {
            player.inventory.itemsBag.stream().filter(item -> item.isNotNullItem() && item.template.id == 579).forEach(item -> {
                InventoryService.gI().subQuantityItemsBag(player, item, item.quantity);
            });
            player.inventory.itemsBox.stream().filter(item -> item.isNotNullItem() && item.template.id == 579).forEach(item -> {
                InventoryService.gI().subQuantityItemsBox(player, item, item.quantity);
            });
            InventoryService.gI().sendItemBags(player);
        }
    }

}
