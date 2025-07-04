
package nro.models.map.dungeon;

import nro.consts.ConstPlayer;
import nro.models.boss.Boss;
import nro.models.boss.BossData;
import nro.models.boss.BossManager;
import nro.models.map.Map;
import nro.models.map.Zone;
import nro.models.player.Player;
import nro.models.skill.Skill;
import nro.services.MapService;
import nro.services.Service;
import nro.services.func.ChangeMapService;
import nro.utils.Util;

/**
 *
 * @author bemeo
 */
public class LeoThap {

    static double getDameBoss(int levelThap, int tangThap) {
    double baseDamage = 80000000000l; // Sát thương gốc cho level 1 tầng 1
    double levelMultiplier = 2; // Tăng 50% cho mỗi level
    double floorMultiplier = 3; // Tăng gấp đôi cho mỗi tầng

    // Tính sát thương cho boss
    double damage = baseDamage * Math.pow(levelMultiplier, levelThap - 1) * Math.pow(floorMultiplier, tangThap - 1);

    // Giới hạn sát thương tối đa để tránh NaN
    if (damage > Double.MAX_VALUE) {
        return Double.MAX_VALUE; // Trả về giá trị tối đa của double nếu quá lớn
    }

    return damage;
}

static double getHpBoss(int levelThap, int tangThap) {
    double baseHp = 90000000000l; // HP gốc cho level 1 tầng 1
    double levelMultiplier = 3; // Tăng 50% cho mỗi level
    double floorMultiplier = 4; // Tăng gấp đôi cho mỗi tầng

    // Tính HP cho boss
    double hp = baseHp * Math.pow(levelMultiplier, levelThap - 1) * Math.pow(floorMultiplier, tangThap - 1);

    // Giới hạn HP tối đa để tránh NaN
    if (hp > Double.MAX_VALUE) {
        return Double.MAX_VALUE; // Trả về giá trị tối đa của double nếu quá lớn
    }

    return hp;
}

    static short[] getOutfitBoss() {
        short[][] of = {{1402	,1403,	1404}, {1402	,1403,	1404}};
        return of[Util.nextInt(of.length - 1)];
    }

    public static Zone getMapLeoThap(Map m) {
        return m.zones.stream().filter(z -> z.getHumanoids().isEmpty()).findAny().orElse(null);
    }

    public static void joinMapLeoThap(Player pl) {
        Map map = MapService.gI().getMapById(211);
        if (getMapLeoThap(map) == null) {
            Zone z = new Zone(MapService.gI().getMapById(211), map.zones.size(), 1);
            map.zones.add(z);
            ChangeMapService.gI().changeMapBySpaceShipBoss(pl, z, 490);
        } else {
            ChangeMapService.gI().changeMapBySpaceShipBoss(pl, getMapLeoThap(map), 490);
        }
        callBoss(pl);
    }

    public static synchronized void callBoss(Player pl) {
        if (pl.levelThap == 0) {
            pl.levelThap = 1;
        }
        if (pl.tangThap == 0) {
            pl.tangThap = 1;
        }
        Boss b = new BossLeoThap(pl.levelThap, pl.tangThap);
        b.zone = pl.zone;
        BossManager.gI().addBoss(b);
    }

    public static class BossLeoThap extends Boss {

        public BossLeoThap(int level, int tang) {
            super(Util.nextInt(1_000_000, 100_000_000), new BossData("Boss Bí Cảnh Tầng " + tang + " [Level " + level + "]", //name
                    ConstPlayer.XAYDA, //gender
                    Boss.DAME_NORMAL, //type dame
                    Boss.HP_NORMAL, //type hp
                    getDameBoss(level, tang), //dame
                    0, //def
                    new double[][]{{getHpBoss(level, tang)}}, //hp
                    getOutfitBoss(), //outfit
                    new short[]{3, 4, 5, 6, 27, 28, 29, 30,//traidat
                        9, 11, 12, 13, 10, 34, 33, 32, 31,//namec
                        16, 17, 18, 19, 20, 37, 38, 36, 35,//xayda
                        24, 25, 26}, //map join
                    new int[][]{ //skill
                        {Skill.KAMEJOKO, 1, 10}, {Skill.LIEN_HOAN, 2, 20}, {Skill.MASENKO, 7, 20}, {Skill.TAI_TAO_NANG_LUONG, 7, 90000}, {Skill.TAI_TAO_NANG_LUONG, 5, 50000}, {Skill.DE_TRUNG, 7, 20000},
                        {Skill.ANTOMIC, 1, 70},},
                    0));
        }

        @Override
        public void update() {
            super.update();
            if (this.zone.getPlayers().isEmpty()) {
                this.leaveMap();
            }
        }

        @Override
        protected boolean useSpecialSkill() {
            return false;
        }

        @Override
        public void rewards(Player pl) {
            
            pl.pointThap++;
            pl.levelThap++;
            int tang = pl.tangThap;
            if (pl.levelThap == 99) {
                pl.levelThap = 1;
                pl.tangThap++;
            }
            if (pl.tangThap > tang) {
                pl.nPoint.calPoint();
            }
            Service.gI().sendThongBaoBenDuoi("Player " + pl.name + " Bí Cảnh tháp đến tầng " + pl.tangThap + " level " + pl.levelThap);
            callBoss(pl);
        }

        @Override
        public void idle() {

        }

        @Override
        public void checkPlayerDie(Player pl) {

        }

        @Override
        public void initTalk() {
            this.textTalkMidle = new String[]{"Xem bản lĩnh của ngươi như nào đã", "Các ngươi tới số mới gặp phải ta"};
            this.textTalkAfter = new String[]{"Ác quỷ biến hình, hêy aaa......."};
        }

        @Override
        public void leaveMap() {
            super.leaveMap();
            BossManager.gI().removeBoss(this);
        }

        @Override
        public double injured(Player plAtt, double damage, boolean piercing, boolean isMobAttack) {
            double dame = 0;
            if (this.isDie()) {
                return dame;
            } else {
                if (Util.isTrue(1, 5) && plAtt != null) {
                    switch (plAtt.playerSkill.skillSelect.template.id) {
                        case Skill.TU_SAT:
                        case Skill.QUA_CAU_KENH_KHI:
                        case Skill.MAKANKOSAPPO:
                            break;
                        default:
                            return 0;
                    }
                }
                dame = super.injured(plAtt, damage, piercing, isMobAttack);
                if (this.isDie()) {
                    notifyPlayeKill(plAtt);
                    die();
                    leaveMap();
                }
                return dame;
            }
        }

    }

}
