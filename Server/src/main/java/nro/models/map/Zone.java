package nro.models.map;

import lombok.Getter;
import lombok.Setter;
import nro.consts.ConstMap;
import nro.consts.ConstMob;
import nro.consts.ConstTask;
import nro.models.boss.Boss;
import nro.models.item.Item;
import nro.models.item.ItemOption;
import nro.models.map.war.NamekBallWar;
import nro.models.mob.Mob;
import nro.models.npc.Npc;
import nro.models.npc.NpcManager;
import nro.models.player.Pet;
import nro.models.player.Player;
import nro.power.CaptionManager;
import nro.server.io.Message;
import nro.services.*;
import nro.services.func.ChangeMapService;
import nro.utils.FileIO;
import nro.utils.Log;
import nro.models.player.TestDame;
import nro.utils.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import nro.consts.ConstItem;
import nro.consts.ConstPet;
import nro.consts.ConstTranhNgocNamek;
import nro.models.phuban.DragonNamecWar.*;
import nro.server.Manager;

import static nro.services.func.ChangeMapService.NON_SPACE_SHIP;

/**
 * @Config Truong
 * @Build by Arriety
 */
public class Zone {

    public static final byte PLAYERS_TIEU_CHUAN_TRONG_MAP = 7;

    public int countItemAppeaerd = 0;

    public Map map;
    public int zoneId;
    public int mapWidth;
    public int maxPlayer;
    private final List<Player> humanoids; //player, boss, pet
    private final List<Player> notBosses; //player, pet
    private final List<Player> players; //player
    private final List<Player> bosses; //boss
    private final List<Player> pets; //pet
    private final List<Player> minipets; //minpet

    public final List<Mob> mobs;
    private final List<ItemMap> items;
    @Setter
    @Getter
    private Player referee;
    @Setter
    @Getter
    private Player testdame;

    public long lastTimeDropBlackBall;
    public boolean finishBlackBallWar;
    public byte percentMabuEgg;
    public boolean initBossMabu;
    public boolean finishMabuWar;

    //tranh ngọc namek
    public int pointFide;
    public int pointCadic;
    private final List<Player> playersFide;
    private final List<Player> playersCadic;
    public long lastTimeStartTranhNgoc;
    public boolean startZoneTranhNgoc;
    public long lastTimeDropBall;
    public boolean finishMapMaBu;
    public boolean finishMapTN;
    public boolean finishMapPVP;
    public boolean finishMapthanhdia;
    public List<TrapMap> trapMaps;
    public byte effDragon = -1;

    public Zone(Map map, int zoneId, int maxPlayer) {
        this.map = map;
        this.zoneId = zoneId;
        this.maxPlayer = maxPlayer;
        this.humanoids = new ArrayList<>();
        this.notBosses = new ArrayList<>();
        this.players = new ArrayList<>();
        this.bosses = new ArrayList<>();
        this.pets = new ArrayList<>();
        this.minipets = new ArrayList<>();
        this.mobs = new ArrayList<>();
        this.items = new ArrayList<>();
        this.trapMaps = new ArrayList<>();
        this.playersFide = new ArrayList<>();
        this.playersCadic = new ArrayList<>();
    }

    public short[] getXYMabuMap() {
        for (short[] PointMabuMap : Manager.POINT_MABU_MAP) {
            short x = PointMabuMap[0];
            short y = PointMabuMap[1];
            if (!havePlayerInPoint(x, y)) {
                return PointMabuMap;
            }
        }
        return Manager.POINT_MABU_MAP[0];
    }

    public boolean havePlayerInPoint(short x, short y) {
        synchronized (players) {
            for (int j = 0; j < this.players.size(); j++) {
                Player pl = this.players.get(j);
                if (pl != null && pl.effectSkill.isHoldMabu && pl.location.x == x && pl.location.y == y) {
                    return true;
                }
            }
            return false;
        }
    }

    public boolean isFullPlayer() {
        return this.players.size() >= this.maxPlayer;
    }

    public void addMob(Mob mob) {
        mob.id = mobs.size();
        mobs.add(mob);
    }

    private void updateMob() {
        for (Mob mob : this.mobs) {
            mob.update();
        }
    }

    public long getTotalHP() {
        long total = 0;
        synchronized (mobs) {
            for (Mob mob : mobs) {
                if (!mob.isDie()) {
                    total += mob.point.hp;
                }
            }
        }
        synchronized (players) {
            for (Player pl : players) {
                if (pl.nPoint != null && !pl.isDie()) {
                    total += pl.nPoint.hp;
                }
            }
        }
        synchronized (pets) {
            for (Player pl : pets) {
                if (pl.nPoint != null && !pl.isDie()) {
                    total += pl.nPoint.hp;
                }
            }
        }
        return total;
    }

    private void updatePlayer() {
        for (int i = this.notBosses.size() - 1; i >= 0; i--) {
            Player pl = this.notBosses.get(i);
            if (!pl.isClone && !pl.isPet && !pl.isMiniPet) {
                this.notBosses.get(i).update();
            }
        }
    }

    private void udNpc(Player player) {
        for (Npc npc : player.zone.map.npcs) {
            npc.AutoChat();
        }
    }

    private void updateZoneTranhNgoc() {
        if (!TranhNgoc.gI().isTimeStartWar() && startZoneTranhNgoc) {
            startZoneTranhNgoc = false;
            playersCadic.clear();
            playersFide.clear();
            pointCadic = 0;
            pointFide = 0;
            return;
        }
        if (startZoneTranhNgoc) {
            if (Util.canDoWithTime(this.lastTimeStartTranhNgoc, ConstTranhNgocNamek.TIME)) {
                startZoneTranhNgoc = false;
                if (pointCadic > pointFide) {
                    TranhNgocService.getInstance().sendEndPhoBan(this, ConstTranhNgocNamek.WIN, false);
                    TranhNgocService.getInstance().sendEndPhoBan(this, ConstTranhNgocNamek.LOSE, true);
                    TranhNgocService.getInstance().givePrice(getPlayersCadic(), ConstTranhNgocNamek.WIN, pointCadic);
                    TranhNgocService.getInstance().givePrice(getPlayersFide(), ConstTranhNgocNamek.LOSE, pointFide);
                } else if (pointFide > pointCadic) {
                    TranhNgocService.getInstance().sendEndPhoBan(this, ConstTranhNgocNamek.WIN, true);
                    TranhNgocService.getInstance().sendEndPhoBan(this, ConstTranhNgocNamek.LOSE, false);
                    TranhNgocService.getInstance().givePrice(getPlayersFide(), ConstTranhNgocNamek.WIN, pointFide);
                    TranhNgocService.getInstance().givePrice(getPlayersCadic(), ConstTranhNgocNamek.LOSE, pointCadic);
                } else {
                    TranhNgocService.getInstance().sendEndPhoBan(this, ConstTranhNgocNamek.DRAW, true);
                    TranhNgocService.getInstance().sendEndPhoBan(this, ConstTranhNgocNamek.DRAW, false);
                }
                items.clear();
                playersCadic.clear();
                playersFide.clear();
                pointCadic = 0;
                pointFide = 0;
            } else {
                if (pointCadic == 7) {
                    startZoneTranhNgoc = false;
                    TranhNgocService.getInstance().sendEndPhoBan(this, ConstTranhNgocNamek.WIN, false);
                    TranhNgocService.getInstance().sendEndPhoBan(this, ConstTranhNgocNamek.LOSE, true);
                    TranhNgocService.getInstance().givePrice(getPlayersCadic(), ConstTranhNgocNamek.WIN, pointCadic);
                    TranhNgocService.getInstance().givePrice(getPlayersFide(), ConstTranhNgocNamek.LOSE, pointFide);
                    items.clear();
                    playersCadic.clear();
                    playersFide.clear();
                    pointCadic = 0;
                    pointFide = 0;
                } else if (pointFide == 7) {
                    startZoneTranhNgoc = false;
                    TranhNgocService.getInstance().sendEndPhoBan(this, ConstTranhNgocNamek.WIN, true);
                    TranhNgocService.getInstance().sendEndPhoBan(this, ConstTranhNgocNamek.LOSE, false);
                    TranhNgocService.getInstance().givePrice(getPlayersFide(), ConstTranhNgocNamek.WIN, pointFide);
                    TranhNgocService.getInstance().givePrice(getPlayersCadic(), ConstTranhNgocNamek.LOSE, pointCadic);
                    items.clear();
                    playersCadic.clear();
                    playersFide.clear();
                    pointCadic = 0;
                    pointFide = 0;
                }
            }
            if (Util.canDoWithTime(lastTimeDropBall, ConstTranhNgocNamek.LAST_TIME_DROP_BALL)) {
                int id = Util.nextInt(ConstItem.NGOC_RONG_NAMEK_1_SAO, ConstItem.NGOC_RONG_NAMEK_7_SAO);
                ItemMap it = this.getItemMapByTempId(id);
                if (it == null && !findPlayerHaveBallTranhDoat(id)) {
                    lastTimeDropBall = System.currentTimeMillis();
                    int x = Util.nextInt(20, map.mapWidth);
                    int y = map.yPhysicInTop(x, Util.nextInt(20, map.mapHeight - 200));
                    ItemMap itemMap = new ItemMap(this, id, 1, x, y, -1);
                    itemMap.isNamecBallTranhDoat = true;
                    Service.getInstance().dropItemMap(this, itemMap);
                }
            }
        }
    }

    public boolean findPlayerHaveBallTranhDoat(int id) {
        for (Player pl : this.getPlayers()) {
            if (pl != null && pl.isHoldNamecBallTranhDoat && pl.tempIdNamecBallHoldTranhDoat == id) {
                return true;
            }
        }
        return false;
    }

    public boolean isFullPlayer1() {
        return this.players.size() >= this.maxPlayer - 5;
    }

    private void updateReferee() {
        referee.update();
    }

    private void updateTestDame() {
        testdame.update();
    }

    private void updateItem() {
        synchronized (items) {
            for (ItemMap item : items) {
                item.update();
            }
        }
    }

    public void update() {
        updateMob();
        updatePlayer();
        updateItem();
        updateZoneTranhNgoc();
        if (map.mapId == ConstMap.DAI_HOI_VO_THUAT) {
            updateReferee();
        }
        if (map.mapId == 5) {
            updateTestDame();
        }
    }

    public int getNumOfPlayers() {
        return this.players.size();
    }

    public int getNumOfBosses() {
        return this.bosses.size();
    }

    public boolean isBossCanJoin(Boss boss) {
        for (Player b : this.bosses) {
            if (b.id == boss.id) {
                return false;
            }
        }
        return true;
    }

    public List<Player> getNotBosses() {
        return this.notBosses;
    }

    public List<Player> getPlayers() {
        return this.players;
    }

    public List<Player> getHumanoids() {
        return this.humanoids;
    }

    public List<Player> getPlayersCadic() {
        return this.playersCadic;
    }

    public List<Player> getPlayersFide() {
        return this.playersFide;
    }

    public void addPlayersCadic(Player player) {
        synchronized (playersCadic) {
            if (!this.playersCadic.contains(player)) {
                this.playersCadic.add(player);
            }
        }
    }

    public void addPlayersFide(Player player) {
        synchronized (playersFide) {
            if (!this.playersFide.contains(player)) {
                this.playersFide.add(player);
            }
        }
    }

    public void removePlayersCadic(Player player) {
        synchronized (playersCadic) {
            if (this.playersCadic.contains(player)) {
                this.playersCadic.remove(player);
            }
        }
    }

    public void removePlayersFide(Player player) {
        synchronized (playersFide) {
            if (this.playersFide.contains(player)) {
                this.playersFide.remove(player);
            }
        }
    }

    public List<Player> getBosses() {
        return this.bosses;
    }

    public void addPlayer(Player player) {
        if (player != null) {
            synchronized (humanoids) {
                if (!this.humanoids.contains(player)) {
                    this.humanoids.add(player);
                }
            }
            if (!player.isBoss) {
                synchronized (notBosses) {
                    if (!this.notBosses.contains(player)) {
                        this.notBosses.add(player);
                    }
                }
                if (player.isPet || player.isClone) {
                    synchronized (pets) {
                        this.pets.add(player);
                    }
                } else if (player.isMiniPet) {
                    synchronized (minipets) {
                        this.minipets.add(player);
                    }
                } else {
                    synchronized (players) {
                        if (!this.players.contains(player)) {
                            this.players.add(player);
                        }
                    }
                }
            } else {
                synchronized (bosses) {
                    this.bosses.add(player);
                }
            }

        }
    }

    public void removePlayer(Player player) {
        if (player != null) {
            this.humanoids.remove(player);
            if (!player.isBoss) {
                synchronized (notBosses) {
                    this.notBosses.remove(player);
                }
                if (player.isPet || player.isClone) {
                    synchronized (pets) {
                        this.pets.remove(player);
                    }
                } else if (player.isMiniPet) {
                    synchronized (minipets) {
                        this.minipets.remove(player);
                    }
                } else {
                    synchronized (players) {
                        this.players.remove(player);
                    }
                }
            } else {
                synchronized (bosses) {
                    this.bosses.remove(player);
                }

            }
        }

    }

    public ItemMap getItemMapByItemMapId(int itemId) {
        synchronized (items) {
            for (ItemMap item : this.items) {
                if (item.itemMapId == itemId) {
                    return item;
                }
            }
        }
        return null;
    }

    public ItemMap getItemMapByTempId(int tempId) {
        synchronized (items) {
            for (ItemMap item : this.items) {
                if (item.itemTemplate.id == tempId) {
                    return item;
                }
            }
        }
        return null;
    }

    public List<ItemMap> getItemMapsForPlayer(Player player) {
        List<ItemMap> list = new ArrayList<>();
        synchronized (items) {
            for (ItemMap item : items) {
                if (item instanceof NamekBall ball) {
                    if (ball.isHolding()) {
                        continue;
                    }
                }
                if (item != null && item.itemTemplate != null) {
                    if (item.itemTemplate.id == 78) {
                        if (TaskService.gI().getIdTask(player) != ConstTask.TASK_3_1) {
                            continue;
                        }
                    }
                    if (item.itemTemplate.id == 74) {
                        if (TaskService.gI().getIdTask(player) < ConstTask.TASK_3_0) {
                            continue;
                        }
                    }
                    list.add(item);
                }
            }
        }
        return list;
    }

    public List<ItemMap> getSatellites() {
        synchronized (items) {
            return items.stream().filter(i -> i instanceof Satellite).collect(Collectors.toList());
        }
    }

    public Player getPlayerInMap(int idPlayer) {
        for (Player pl : humanoids) {
            if (pl != null && pl.id == idPlayer) {
                return pl;
            }
        }
        return null;
    }

    public List<Player> getPlayersSameClan(int clanID) {
        List<Player> list = new ArrayList<>();
        synchronized (this.players) {
            for (Player pl : this.players) {
                if (pl.clan != null
                        && pl.clan.id == clanID) {
                    list.add(pl);
                }
            }
        }
        return list;
    }

    public void pickItem(Player player, int itemMapId) {
        ItemMap itemMap = getItemMapByItemMapId(itemMapId);
        if (itemMap instanceof Satellite) {
            return;
        }
        if (itemMap != null && !itemMap.isPickedUp) {
            synchronized (itemMap) {
                if (!itemMap.isPickedUp) {
                    if (itemMap.playerId == player.id || itemMap.playerId == -1) {
                        if (itemMap.itemTemplate.id == 648) {
                            Item item = InventoryService.gI().findItemBagByTemp(player, 649);
                            if (item == null) {
                                Service.getInstance().sendThongBao(player, "Bạn không có Tất,Vớ giáng sinh để đựng quà");
                                return;
                            }
                            itemMap.options.add(new ItemOption(74, 0));
                            InventoryService.gI().subQuantityItemsBag(player, item, 1);
                            InventoryService.gI().sendItemBags(player);
                        }
                        if (itemMap instanceof NamekBall ball) {
                            NamekBallWar.gI().pickBall(player, ball);
                            return;
                        }
                        if (itemMap.isNamecBallTranhDoat) {
                            TranhNgocService.getInstance().pickBall(player, itemMap);
                            return;
                        }
                        Item item = ItemService.gI().createItemFromItemMap(itemMap);
                        int maxQuantity = 0;
                        if (ItemService.gI().isItemNoLimitQuantity(item.template.id)) {
                            maxQuantity = 99999;
                        }
                        boolean picked = InventoryService.gI().addItemBag(player, item, maxQuantity);
                        if (picked) {
                            if (itemMap.itemTemplate.id != 74) {
                                itemMap.isPickedUp = true;
                            }
                            int itemType = item.template.type;
                            Message msg;
                            try {
                                msg = new Message(-20);
                                msg.writer().writeShort(itemMapId);
                                switch (itemType) {
                                    case 9:
                                    case 10:
                                    case 34:
                                        msg.writer().writeUTF("");
                                        PlayerService.gI().sendInfoHpMpMoney(player);
                                        break;
                                    default:
                                        switch (item.template.id) {
                                            case 73:
                                                msg.writer().writeUTF("");
                                                msg.writer().writeShort(item.quantity);
                                                player.sendMessage(msg);
                                                msg.cleanup();
                                                break;
                                            case 74:
                                                msg.writer().writeUTF("Bạn vừa ăn " + item.template.name);
                                                break;
                                            case 78:
                                                msg.writer().writeUTF("Wow, một cậu bé dễ thương!");
                                                msg.writer().writeShort(item.quantity);
                                                player.sendMessage(msg);
                                                msg.cleanup();
                                                break;
                                            case 516:
                                                player.nPoint.setFullHpMp();
                                                PlayerService.gI().sendInfoHpMp(player);
                                                Service.getInstance().sendThongBao(player, "Bạn vừa ăn " + itemMap.itemTemplate.name);
                                                break;
                                            default:
                                                msg.writer().writeUTF("Bạn nhặt được " + item.template.name);
                                                InventoryService.gI().sendItemBags(player);
                                                break;
                                        }

                                }
                                msg.writer().writeShort(item.quantity);
                                player.sendMessage(msg);
                                msg.cleanup();
                                Service.getInstance().sendToAntherMePickItem(player, itemMapId);
                                int mapID = this.map.mapId;
                                if (!(mapID >= 21 && mapID <= 23
                                        && itemMap.itemTemplate.id == 74
                                        || mapID >= 42 && mapID <= 44
                                        && itemMap.itemTemplate.id == 78)) {
                                    removeItemMap(itemMap);
                                }
                            } catch (Exception e) {
                                Log.error(Zone.class, e);
                            }
                        } else {
                            if (!ItemMapService.gI().isBlackBall(item.template.id)) {
                                String text = "Hành trang không còn chỗ trống";
                                Service.getInstance().sendThongBao(player, text);
                            }
                        }
                    } else {
                        Service.getInstance().sendThongBao(player, "Không thể nhặt vật phẩm của người khác");
                    }
                }
            }
        } else {
            Service.getInstance().sendThongBao(player, "Không thể thực hiện");
        }
        TaskService.gI().checkDoneTaskPickItem(player, itemMap);
        TaskService.gI().checkDoneSideTaskPickItem(player, itemMap);
    }

    public void addItem(ItemMap itemMap) {
        synchronized (items) {
            items.add(itemMap);
        }
    }

    public void removeItemMap(ItemMap itemMap) {
        synchronized (items) {
            this.items.remove(itemMap);
        }
    }

    public Player getRandomPlayerInMap() {
        if (!this.notBosses.isEmpty()) {
            return this.notBosses.get(Util.nextInt(0, this.notBosses.size() - 1));
        } else {
            return null;
        }
    }

    public void load_Me_To_Another(Player player) {
        try {
            if (player.zone != null) {
                if (this.map.isMapOffline) {
                    if (player.isPet && this.equals(((Pet) player).master.zone)) {
                        infoPlayer(((Pet) player).master, player);
                    }
                } else {
                    synchronized (this.players) {
                        for (int i = 0; i < players.size(); i++) {
                            Player pl = players.get(i);
                            if (!player.equals(pl)) {
                                infoPlayer(pl, player);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.error(MapService.class, e);
        }
    }

    public void loadAnotherToMe(Player player) { //load những player trong map và gửi cho player vào map
        try {
            if (this.map.isMapOffline) {
                for (int i = this.humanoids.size() - 1; i >= 0; i--) {
                    Player pl = this.humanoids.get(i);
                    if (pl != null) {
                        if (pl.id == -player.id) {
                            infoPlayer(player, pl);
                            break;
                        }
                    }
                }
            } else {
                for (int i = this.humanoids.size() - 1; i >= 0; i--) {
                    Player pl = this.humanoids.get(i);
                    if (pl != null) {
                        if (player != pl) {
                            infoPlayer(player, pl);
                            PlayerService.gI().sendPetFollow(player, pl);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.error(MapService.class, e);
        }
    }

    private void infoPlayer(Player plReceive, Player plInfo) {
        Message msg;
        try {
            String name = "";
            msg = new Message(-5);
            msg.writer().writeInt((int) plInfo.id);
            if (plInfo.clan != null) {
                msg.writer().writeInt(plInfo.clan.id);
                name = "[" + plInfo.clan.name + "]" + plInfo.name;
            } else if (plInfo.isPet && ((Pet) plInfo).typePet == ConstPet.VIDEL) {
                msg.writer().writeInt(-1);
                name = plInfo.name + "[Level " + ((Pet) plInfo).getLever() + "]";
            } else {
                msg.writer().writeInt(-1);
                name = plInfo.name;
            }
            int level = CaptionManager.getInstance().getLevel(plInfo);
            msg.writer().writeByte(level);
            msg.writer().writeBoolean(false);
            msg.writer().writeByte(plInfo.typePk);
            msg.writer().writeByte(plInfo.gender);
            msg.writer().writeByte(plInfo.gender);
            msg.writer().writeShort(plInfo.getHead());

            String prefix = plInfo.vip >= 1 && plInfo.vip <= 99 ? "[VIP " + plInfo.vip + "]" : "";
            msg.writer().writeUTF(prefix + plInfo.name);

            msg.writer().writeDouble(plInfo.nPoint.hp);
            msg.writer().writeDouble(plInfo.nPoint.hpMax);
            msg.writer().writeShort(plInfo.getBody());
            msg.writer().writeShort(plInfo.getLeg());
            msg.writer().writeByte(plInfo.getFlagBag()); //bag
            msg.writer().writeByte(-1);
            msg.writer().writeShort(plInfo.location.x);
            msg.writer().writeShort(plInfo.location.y);
            msg.writer().writeShort(0);
            msg.writer().writeShort(0); //

            msg.writer().writeByte(0);

            msg.writer().writeByte(plInfo.getUseSpaceShip());

            msg.writer().writeByte(plInfo.effectSkill.isMonkey ? 1 : 0);
            msg.writer().writeShort(plInfo.getMount());
            msg.writer().writeByte(plInfo.cFlag);
            msg.writer().writeByte(0);

//            if (!plInfo.isPet && !plInfo.isBoss && plInfo.isAdmin()) {
            msg.writer().writeShort(plInfo.getAura()); //idauraeff
            msg.writer().writeByte(-1); //seteff
//            }

            plReceive.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            Log.error(MapService.class, e);
        }
        Service.getInstance().sendFlagPlayerToMe(plReceive, plInfo);

        try {
            if (plInfo.isDie()) {
                msg = new Message(-8);
                msg.writer().writeInt((int) plInfo.id);
                msg.writer().writeByte(0);
                msg.writer().writeShort(plInfo.location.x);
                msg.writer().writeShort(plInfo.location.y);
                plReceive.sendMessage(msg);
                msg.cleanup();
            }
        } catch (Exception e) {

        }
    }

    public void mapInfo(Player pl) {
        Message msg;
        try {
            msg = new Message(-24);
            msg.writer().writeByte(this.map.mapId);
            msg.writer().writeByte(this.map.planetId);
            msg.writer().writeByte(this.map.tileId);
            msg.writer().writeByte(this.map.bgId);
            msg.writer().writeByte(this.map.type);
            msg.writer().writeUTF(this.map.mapName);
            msg.writer().writeByte(this.zoneId);

            msg.writer().writeShort(pl.location.x);
            msg.writer().writeShort(pl.location.y);

            // waypoint
            List<WayPoint> wayPoints = this.map.wayPoints;
            msg.writer().writeByte(wayPoints.size());
            for (WayPoint wp : wayPoints) {
                msg.writer().writeShort(wp.minX);
                msg.writer().writeShort(wp.minY);
                msg.writer().writeShort(wp.maxX);
                msg.writer().writeShort(wp.maxY);
                msg.writer().writeBoolean(wp.isEnter);
                msg.writer().writeBoolean(wp.isOffline);
                msg.writer().writeUTF(wp.name);
            }
            // mob
            List<Mob> mobs = this.mobs;
            msg.writer().writeByte(mobs.size());
            for (Mob mob : mobs) {
                msg.writer().writeBoolean(false); //is disable
                msg.writer().writeBoolean(false); //is dont move
                msg.writer().writeBoolean(false); //is fire
                msg.writer().writeBoolean(false); //is ice
                msg.writer().writeBoolean(false); //is wind
                msg.writer().writeByte(mob.tempId);
                msg.writer().writeByte(mob.getSys());
                msg.writer().writeDouble(mob.point.getHP());
                msg.writer().writeByte(mob.level);
                msg.writer().writeDouble((mob.point.getHpFull()));
                msg.writer().writeShort(mob.location.x);
                msg.writer().writeShort(mob.location.y);
                if (mob.isDie()) {
                    msg.writer().writeByte(ConstMob.MA_INHELL); //status
                } else {
                    msg.writer().writeByte(ConstMob.MA_WALK); //status
                }
                msg.writer().writeByte(0); //level boss
                msg.writer().writeBoolean(false);
            }

            msg.writer().writeByte(0);

            // npc
            List<Npc> npcs = NpcManager.getNpcsByMapPlayer(pl);
            msg.writer().writeByte(npcs.size());
            for (Npc npc : npcs) {
                msg.writer().writeByte(npc.status);
                msg.writer().writeShort(npc.cx);
                msg.writer().writeShort(npc.cy);
                msg.writer().writeByte(npc.tempId);
                msg.writer().writeShort(npc.avartar);
            }

            // item
            List<ItemMap> itemsMap = this.getItemMapsForPlayer(pl);
            msg.writer().writeByte(itemsMap.size());
            for (ItemMap it : itemsMap) {
                msg.writer().writeShort(it.itemMapId);
                msg.writer().writeShort(it.itemTemplate.id);
                msg.writer().writeShort(it.x);
                msg.writer().writeShort(it.y);
                msg.writer().writeInt((int) it.playerId);
                if (it.playerId == -2) {
                    msg.writer().writeShort(it.range);
                }
            }

            // bg item
//                msg.writer().writeShort(0);
            try {
                byte[] bgItem = FileIO.readFile("resources/map/item_bg_map_data/" + this.map.mapId);
                msg.writer().write(bgItem);
            } catch (Exception e) {
                msg.writer().writeShort(0);
            }

            // eff item
//                msg.writer().writeShort(0);
//            try {
//                byte[] effItem = FileIO.readFile("resources/map/eff_map/" + this.map.mapId);
//                msg.writer().write(effItem);
//            } catch (Exception e) {
//                msg.writer().writeShort(0);
//            }
            List<EffectMap> em = this.map.effMap;
            msg.writer().writeShort(em.size());
            for (EffectMap e : em) {
                msg.writer().writeUTF(e.getKey());
                msg.writer().writeUTF(e.getValue());
            }

            msg.writer().writeByte(this.map.bgType);
            msg.writer().writeByte(pl.getUseSpaceShip());
            msg.writer().writeByte(0);
            pl.sendMessage(msg);

            msg.cleanup();

        } catch (Exception e) {
            Log.error(Service.class, e);
        }
    }

    public TrapMap isInTrap(Player player) {
        for (TrapMap trap : this.trapMaps) {
            if (player.location.x >= trap.x && player.location.x <= trap.x + trap.w
                    && player.location.y >= trap.y && player.location.y <= trap.y + trap.h) {
                return trap;
            }
        }
        return null;
    }

 public void changeMapWaypoint(Player player) {
    // Kiểm tra nếu map là null
    if (map == null) {
        System.err.println("[ERROR] Map object is null in changeMapWaypoint.");
        Service.getInstance().sendThongBaoOK(player, "Lỗi: Bản đồ không tồn tại.");
        return;
    }

    Zone zoneJoin = null;
    WayPoint wp = null;
    int xGo = player.location.x;
    int yGo = player.location.y;

    // Xử lý đặc biệt cho mapId 45 và 46
    if (map.mapId == 45 || map.mapId == 46) {
        int x = player.location.x;
        int y = player.location.y;
        if (x >= 35 && x <= 685 && y >= 550 && y <= 560) {
            xGo = map.mapId == 45 ? 420 : 636;
            yGo = 150;
            zoneJoin = MapService.gI().getMapCanJoin(player, map.mapId + 1);

            // Kiểm tra nếu không tìm được khu vực
            if (zoneJoin == null) {
                System.err.println("[WARN] No valid zone found for mapId: " + (map.mapId + 1));
            }
        }
    }

    // Nếu chưa tìm được zoneJoin, kiểm tra WayPoint của người chơi
    if (zoneJoin == null) {
        wp = MapService.gI().getWaypointPlayerIn(player);
        if (wp != null) {
            zoneJoin = MapService.gI().getMapCanJoin(player, wp.goMap);

            // Kiểm tra nếu zoneJoin được tìm thấy
            if (zoneJoin != null) {
                xGo = wp.goX;
                yGo = wp.goY;
            } else {
                System.err.println("[WARN] No valid zone found for waypoint mapId: " + wp.goMap);
            }
        } else {
            System.err.println("[WARN] TRUONG Pro");
        }
    }

    // Thay đổi map nếu tìm được zoneJoin, nếu không thông báo lỗi
    if (zoneJoin != null) {
        ChangeMapService.gI().changeMap(player, zoneJoin, -1, -1, xGo, yGo, NON_SPACE_SHIP);
    } else {
        int x = player.location.x;

        // Đảm bảo người chơi không vượt quá giới hạn bản đồ
        if (player.location.x >= map.mapWidth - 60) {
            x = map.mapWidth - 60;
        } else if (player.location.x <= 60) {
            x = 60;
        }

        Service.getInstance().resetPoint(player, x, player.location.y);
        Service.getInstance().sendThongBaoOK(player, "Không thể đến khu vực này.");
    }
}


    public void playerMove(Player player, int x, int y) {
        if (!player.isDie()) {
            if (player.effectSkill.isCharging) {
                EffectSkillService.gI().stopCharge(player);
            }
            if (player.effectSkill.useTroi) {
                EffectSkillService.gI().removeUseTroi(player);
            }
            player.location.x = x;
            player.location.y = y;
            switch (map.mapId) {
                case 85:
                case 86:
                case 87:
                case 88:
                case 89:
                case 90:
                case 91:
                    if (x < 24 || x > map.mapWidth - 24 || y < 0 || y > map.mapHeight - 24) {
                        if (MapService.gI().getWaypointPlayerIn(player) == null) {
                            ChangeMapService.gI().changeMap(player, 21 + player.gender, 0, 200, 336);
                            return;
                        }
                    }
                    if (!player.isBoss && !player.isPet && !player.isClone) {
                        int yTop = map.yPhysicInTop(player.location.x, player.location.y);
                        if (yTop >= map.mapHeight - 24) {
                            ChangeMapService.gI().changeMap(player, 21 + player.gender, 0, 200, 336);
                            return;
                        }
                    }
                    break;
            }
            if (player.clone != null) {
                player.clone.followMaster();
            }
            if (player.pet != null) {
                player.pet.followMaster();
            }
            if (player.minipet != null) {
                player.minipet.followMaster();
            }
            MapService.gI().sendPlayerMove(player);
//            TaskService.gI().checkDoneTaskGoToMap(player, player.zone);
        }
    }

    public Mob findMobByID(int id) {
        int low = 0;
        int high = mobs.size() - 1;
        while (low <= high) {
            int mid = (low + high) / 2;
            if (mobs.get(mid).id < id) {
                low = mid + 1;
            } else if (mobs.get(mid).id > id) {
                high = mid - 1;
            } else {
                return mobs.get(mid);
            }
        }
        return null;
    }

    public Player findPlayerByID(long id) {
        for (Player p : this.players) {
            if (p.id == id) {
                return p;
            }
        }
        return null;
    }

    public void sendMessage(Message m) {
        for (Player player : players) {
            player.sendMessage(m);
        }
    }
}
