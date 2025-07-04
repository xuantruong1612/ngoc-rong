package nro.models.mob;

import java.io.IOException;
import java.util.ArrayList;
import nro.consts.ConstMap;
import nro.consts.ConstMob;
import java.util.List;
import nro.models.map.Zone;
import nro.models.map.dungeon.zones.ZSnakeRoad;
import nro.models.player.Location;
import nro.models.player.Player;
import nro.power.CaptionManager;
import nro.server.io.Message;
import nro.services.Service;
import nro.utils.Util;
import nro.services.MobService;
import nro.services.TaskService;

public class Mob {

    public int id;
    public Zone zone;
    public int tempId;
    public String name;
    public byte level;

    public MobPoint point;
    public MobEffectSkill effectSkill;
    public Location location;

    public byte pDame;
    public int pTiemNang;
    private long maxTiemNang;

    public long lastTimeDie;
    public int sieuquai = 0;

    public boolean actived;

    private long targetID;

    public Mob(Mob mob) {
        this.point = new MobPoint(this);
        this.effectSkill = new MobEffectSkill(this);
        this.location = new Location();
        this.id = mob.id;
        this.tempId = mob.tempId;
        this.level = mob.level;
        this.point.setHpFull(mob.point.getHpFull());
        this.point.setHP(this.point.getHpFull());
        this.location.x = mob.location.x;
        this.location.y = mob.location.y;
        this.pDame = mob.pDame;
        this.pTiemNang = mob.pTiemNang;
        this.setTiemNang();
        this.status = 5;
    }

    public Mob() {
        this.point = new MobPoint(this);
        this.effectSkill = new MobEffectSkill(this);
        this.location = new Location();
    }

    public int getSys() {
        return 0;
    }

    public void setTiemNang() {
        this.maxTiemNang = (long) this.point.getHpFull() * (this.pTiemNang + Util.nextInt(-2, 2)) / 100;
    }

    public byte status;

//    private List<Player> playerAttack = new LinkedList<>();
    protected long lastTimeAttackPlayer;

    public boolean isDie() {
        return this.point.getHP() <= 0;
    }

    public static void initMopbKhiGas(Mob mob, byte level) {
        mob.point.dame = (level * 3250 * mob.level * 4) * 5;
        mob.point.maxHp = (level * 12472 * mob.point.hp + level * 7263 * mob.tempId) / 2;

    }

    public int lvMob = 0;

    public void sendMobHoiSinh() {
        Message msg;
        try {
            msg = new Message(-13);
            msg.writer().writeByte(this.id);
            msg.writer().writeByte(this.tempId);
            msg.writer().writeByte(lvMob);
            msg.writer().writeDouble((this.point.hp));
            Service.getInstance().sendMessAllPlayerInMap(this.zone, msg);
            msg.cleanup();
        } catch (IOException e) {
        }
    }

   public synchronized void injured(Player plAtt, double damage, boolean dieWhenHpFull) {
        if (!this.isDie()) {
            if (plAtt != null) {
                this.targetID = plAtt.id;
            }

            if (damage >= this.point.hp) {
                damage = this.point.hp;
            }
            if (!dieWhenHpFull) {
                if (this.point.hp == this.point.maxHp && damage >= this.point.hp) {
                    damage = this.point.hp - 1;
                }
                if (this.tempId == 0 && damage > 10) {
                    damage = 10;
                }
            }
            this.point.hp -= damage;
            if (this.isDie()) {
                MobService.gI().sendMobDieAffterAttacked(this, plAtt, damage);
                MobService.gI().dropItemTask(plAtt, this);
                TaskService.gI().checkDoneTaskKillMob(plAtt, this);
                TaskService.gI().checkDoneSideTaskKillMob(plAtt, this);
                setDie();
            } else {
                MobService.gI().sendMobStillAliveAffterAttacked(this, damage, plAtt != null ? plAtt.nPoint.isCrit : false, plAtt);
            }
            if (plAtt != null) {
                Service.getInstance().addSMTN(plAtt, (byte) 2, (long) getTiemNangForPlayer(plAtt,  damage), true);
            }
        }
    }

      public double getTiemNangForPlayer(Player pl, double dame) {
        int levelPlayer = CaptionManager.getInstance().getLevel(pl);
        int n = levelPlayer - this.level;
        double pDameHit = dame * 100 / point.getHpFull();
        double tiemNang = pDameHit * maxTiemNang / 100;
        if (tiemNang <= 0) {
            tiemNang = 1;
        }
        if (n >= 0) {
            for (int i = 0; i < n; i++) {
                double sub = tiemNang * 15 / 100;
                if (sub <= 0) {
                    sub = 1;
                }
                tiemNang -= sub;
            }
        } else {
            for (int i = 0; i < -n; i++) {
                double add = tiemNang * 10 / 100;
                if (add <= 0) {
                    add = 1;
                }
                tiemNang += add;
            }
        }
        if (tiemNang <= 0) {
            tiemNang = 1;
        }
        tiemNang =  pl.nPoint.calSucManhTiemNang((long) tiemNang);
        return tiemNang;
    }


    public void update() {
        if (this.isDie()) {
            if (!(zone instanceof ZSnakeRoad)) {
                if ((zone.map.type == ConstMap.MAP_NORMAL
                        || zone.map.type == ConstMap.MAP_OFFLINE
                        || zone.map.type == ConstMap.MAP_BLACK_BALL_WAR) && (tempId != ConstMob.HIRUDEGARN) && Util.canDoWithTime(lastTimeDie, 2000)) {
                    MobService.gI().hoiSinhMob(this);
                } else if (this.zone.map.type == ConstMap.MAP_DOANH_TRAI && Util.canDoWithTime(lastTimeDie, 10000)) {
                    MobService.gI().hoiSinhMobDoanhTrai(this);
                }
            }
            return;
        }
        if (zone != null) {
            effectSkill.update();
            if (!zone.getPlayers().isEmpty() && Util.canDoWithTime(lastTimeAttackPlayer, 2000)) {
                attackPlayer();
            }
        }
    }

    public void attackPlayer() {
        if (!isDie() && !effectSkill.isHaveEffectSkill() && !(tempId == 0)) {
            Player pl = getPlayerCanAttack();
            if (pl != null) {
                double damage = MobService.gI().mobAttackPlayer(this, pl);
                MobService.gI().sendMobAttackMe(this, pl, damage);
                MobService.gI().sendMobAttackPlayer(this, pl);
            }
            this.lastTimeAttackPlayer = System.currentTimeMillis();
        }
    }

    public List<Player> playerAttackList = new ArrayList<>();

    public void addPlayerAttack(Player player) {
        if (player != null && !this.isDie() && !player.isBoss && !player.isMiniPet) {
            if (!playerAttackList.contains(player)) {
                this.playerAttackList.add(player);
            }
        } else {
            this.playerAttackList.clear();
        }
    }

    public Player getPlayerCanAttack() {
        int distance = 500;
        Player plAttack = null;
        distance = 100;
        try {
            List<Player> players = this.zone.getNotBosses();
            for (Player pl : players) {
                if (!pl.isDie() && !pl.isBoss && !pl.effectSkin.isVoHinh && !pl.isMiniPet && !pl.nPoint.buffDefenseSatellite) {
                    int dis = Util.getDistance(pl, this);
                    if (dis <= distance) {
                        plAttack = pl;
                        distance = dis;
                    }
                }
            }
        } catch (Exception e) {

        }
        return plAttack;
    }

    public void setDie() {
        this.lastTimeDie = System.currentTimeMillis();
    }
}
