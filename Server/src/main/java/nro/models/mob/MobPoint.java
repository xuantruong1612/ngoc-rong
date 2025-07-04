package nro.models.mob;

import nro.utils.Util;

/**
 *
 * Arriety
 *
 */
public class MobPoint {

    public final Mob mob;
    public double hp;
    public double maxHp;
    public int dame;

    public double clanMemHighestDame; //dame lớn nhất trong clan
    public double clanMemHighestHp; //hp lớn nhất trong clan

    public int xHpForDame = 50; //dame gốc = highesHp / xHpForDame;
    public int xDameForHp = 10; //hp gốc = xDameForHp * highestDame;

    public MobPoint(Mob mob) {
        this.mob = mob;
    }

    public double getHpFull() {
        return maxHp;
    }

    public void setHpFull(double hp) {
        maxHp = hp;
    }

    public double getHP() {
        return hp;
    }

    public void setHP(double hp) {
        if (this.hp < 0) {
            this.hp = 0;
        } else {
            this.hp = hp;
        }
    }

    public int getDameAttack() {
        return (int) (this.dame != 0 ? this.dame + Util.nextInt(-(this.dame / 100), (this.dame / 100))
                : this.getHpFull() * Util.nextInt(mob.pDame - 1, mob.pDame + 1) / 100
                + Util.nextInt(-(mob.level * 10), mob.level * 10));
    }
}
