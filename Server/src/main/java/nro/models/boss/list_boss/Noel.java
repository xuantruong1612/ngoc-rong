//package nro.models.boss.list_boss;
//
//import java.util.ArrayList;
//import java.util.List;
//import nro.consts.ConstPlayer;
//import nro.models.boss.Boss;
//import nro.models.boss.BossData;
//import static nro.models.boss.BossFactory.NOEN;
//
//import nro.models.item.ItemOption;
//import nro.models.map.ItemMap;
//import nro.models.player.Player;
//import nro.models.skill.Skill;
//import nro.server.Client;
//import nro.services.PlayerService;
//import nro.services.Service;
//import nro.services.func.ChangeMapService;
//import nro.utils.Util;
//
///**
// *
// * @author ADMIN
// */
//public class Noel extends Boss {
//
//    private long lastTimePhatQua;
//    private long TIME_PHAT_QUA;
//    private final long TIME_CHANGE_PLAYER = 1000;
//    private long lastTimeJoinMap;
//
//    public Noel() {
//        super(NOEN, new BossData(
//                "Ông già Noel !!",
//                ConstPlayer.TRAI_DAT, //genderc
//                Boss.DAME_NORMAL, //type dame
//                Boss.HP_NORMAL, //type hp
//                10_000, //dame
//                 10_000, //dame
//                new double[][]{{500_000_000}}, //hp
//                new short[]{805,	806,	807}, //outfit {head, body, leg, bag, aura, eff}
//                new short[]{0}, //map join
//                new int[][]{
//                    {Skill.KAMEJOKO, 4, 10000},
//                    {Skill.LIEN_HOAN, 4, 1000},
//                    {Skill.TAI_TAO_NANG_LUONG, 4, 50000}},//skill
//                1));
//    }
//
//    @Override
//    public void attack() {
//        this.giftItemToPlayer();
//        this.cFlag = 8;
//        Service.gI().changeFlag(this, this.cFlag);
//        this.nPoint.tlNeDon = 10000;
//        if (Util.canDoWithTime(lastTimeChangeMap, 60000)) {
//            this.changeStatus(LEAVE_MAP);
//        }
//    }
//
//    public Player getPlayerPhatQua() {
//        try {
//            if (countChangePlayerAttack < targetCountChangePlayerAttack && plAttack != null && plAttack.zone != null && plAttack.zone.equals(this.zone)) {
//                if (!plAttack.isDie() && !plAttack.effectSkin.isVoHinh && !plAttack.isMiniPet) {
//                    this.countChangePlayerAttack++;
//                    return plAttack;
//                } else {
//                    plAttack = null;
//                }
//            } else {
//                if (this.zone == null) {
//                } else {
//                    this.targetCountChangePlayerAttack = Util.nextInt(10, 20);
//                    this.countChangePlayerAttack = 0;
//                    plAttack = this.zone.getPlayers().stream().filter(pl -> pl.isPl() && !pl.isDie()).findAny().orElse(null);
//                }
//            }
//        } catch (Exception e) {
//        }
//        return plAttack;
//    }
//
//    @Override
//    public void joinMap() {
//        super.joinMap();
//        lastTimeChangeMap = System.currentTimeMillis();
//        lastTimeJoinMap = System.currentTimeMillis() + TIME_CHANGE_PLAYER;
//    }
//
//    public void moveTo(int x, int y) {
//        byte dir = (byte) (this.location.x - x < 0 ? 1 : -1);
//        byte move = (byte) Util.nextInt(20, 30);
//        PlayerService.gI().playerMove(this, this.location.x + (dir == 1 ? move : -move), y);
//    }
//
//    public void giftItemToPlayer() {
//        if (Util.canDoWithTime(lastTimeChangeMap, 30000)) {
//            if (Client.gI().getPlayers().isEmpty() || this.isDie()) {
//                return;
//            }
//            Player ramdonPlayer = Client.gI().getPlayers().get(Util.nextInt(0, Client.gI().getPlayers().size() - 1));
//            if (ramdonPlayer != null && ramdonPlayer.isPl() && !ramdonPlayer.isPet && !ramdonPlayer.isMiniPet && !(ramdonPlayer instanceof BossTheGioi)) {
//                if (ramdonPlayer.zone.map.mapId != 51 && ramdonPlayer.zone.map.mapId != 5 && ramdonPlayer.zone.map.mapId != 21 && ramdonPlayer.zone.map.mapId != 22 && ramdonPlayer.zone.map.mapId != 23 && ramdonPlayer.zone.map.mapId != 113 && ramdonPlayer.zone.map.mapId != 129 && ramdonPlayer.zone.map.mapId != 52) {
//                    ChangeMapService.gI().changeMap(this, ramdonPlayer.zone, ramdonPlayer.location.x, ramdonPlayer.location.y);
//
//                }
//            }
//        }
//        if (Util.canDoWithTime(lastTimeChangeMap, 1000)) {
//            Player pl = getPlayerPhatQua();
//            if (pl == null) {
//                return;
//            }
//            this.moveTo(this.plAttack.location.x, this.plAttack.location.y);
//        }
//        if (!Util.canDoWithTime(this.lastTimePhatQua, this.TIME_PHAT_QUA)) {
//            return;
//        }
//        if (Util.canDoWithTime(this.lastTimePhatQua, this.TIME_PHAT_QUA)) {
//            Player player = getPlayerPhatQua();
//            if (player == null) {
//                return;
//            }
//            this.moveTo(player.location.x, player.location.y);
//            if (Util.isTrue(50, 100)) {
//                // lấy 1 item từ list add sẵn drop ra map
//                Service.gI().dropItemMap(zone, getItemDrop().get(Util.nextInt(getItemDrop().size() - 1)));
//                this.chat("ohohohoho ra Tao Cho Cục cứt");
//            }
//            this.TIME_PHAT_QUA = 20000;
//            this.lastTimePhatQua = System.currentTimeMillis();
//        }
//    }
//
//    // List item drop
//    private List<ItemMap> getItemDrop() {
//        List<ItemMap> listItem = new ArrayList<>();
//        ItemMap itemAdd;
//        itemAdd = new ItemMap(zone, 822, 1, this.location.x, this.location.y, this.plAttack.id);
//         itemAdd.options.add(new ItemOption(174, 2024));
//        listItem.add(itemAdd);
//       
////        itemAdd = new ItemMap(zone, 648, 10, this.location.x, this.location.y, this.plAttack.id);
////        itemAdd.options.add(new ItemOption(30, 1));
////        listItem.add(itemAdd); 
////        
//        
//         itemAdd = new ItemMap(zone, 822, 1, this.location.x, this.location.y, this.plAttack.id);
//        itemAdd.options.add(new ItemOption(174, 2024));
//        listItem.add(itemAdd);
////        
////          itemAdd = new ItemMap(zone, 457, 1, this.location.x, this.location.y, this.plAttack.id);
////        itemAdd.options.add(new ItemOption(30, Util.nextInt(1, 68)));
////        itemAdd.options.add(new ItemOption(93, Util.nextInt(1, 5))); 
////        listItem.add(itemAdd);//.... 
//        
//        
//        
//        
//        
//        
//        
//        
//        
//        
//        
//        
//        
//        
//        
//        
//        
//        
//        
//        
//        
//        
//        
//        return listItem;
//    }
//
//    @Override
//    public void update() {
//        super.update();
//        if (Client.gI().getPlayers().isEmpty() || this.isDie()) {
//            return;
//        }
//        try {
//            Player ramdonPlayer = Client.gI().getPlayers().get(Util.nextInt(Client.gI().getPlayers().size()));
//            if (ramdonPlayer != null && ramdonPlayer.isPl() && !ramdonPlayer.isPet && !ramdonPlayer.isMiniPet && !(ramdonPlayer instanceof BossTheGioi)) {
//                if (ramdonPlayer.zone.map.mapId != 51 && ramdonPlayer.zone.map.mapId != 5 && ramdonPlayer.zone.map.mapId != 21 && ramdonPlayer.zone.map.mapId != 22 && ramdonPlayer.zone.map.mapId != 23 && ramdonPlayer.zone.map.mapId != 113 && ramdonPlayer.zone.map.mapId != 129 && ramdonPlayer.zone.map.mapId != 52 && this.zone.getPlayers().size() <= 0 && System.currentTimeMillis() > this.lastTimeJoinMap) {
//                    lastTimeJoinMap = System.currentTimeMillis() + TIME_CHANGE_PLAYER;
//                    ChangeMapService.gI().changeMap(this, ramdonPlayer.zone, ramdonPlayer.location.x, ramdonPlayer.location.y);
//                }
//            }
//        } catch (Exception e) {
//        }
//    }
//
//    @Override
//    protected boolean useSpecialSkill() {
//        return false;
//    }
//
//    @Override
//    public void rewards(Player pl) {
//    }
//
//    @Override
//    public void idle() {
//    }
//
//    @Override
//    public void checkPlayerDie(Player pl) {
//    }
//
//    @Override
//    public void initTalk() {
//    }
//}
