package nro.models.boss;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nro.models.player.Player;
import nro.server.Client;
import nro.services.Service;
import nro.utils.Util;

/**
 *
 * @stole Arriety
 */
public abstract class FutureBoss extends Boss {

    public HashMap<Long, Double> topDame = new HashMap<>();

    public FutureBoss(int id, BossData data) {
        super(id, data);
    } 
    

    @Override
    public double injured(Player plAtt, double damage, boolean piercing, boolean isMobAttack) {
    damage = (damage / 100) * 80;
    double da = super.injured(plAtt, damage, piercing, isMobAttack);

    // Check if plAtt is not null before accessing its properties
    if (plAtt != null) {
        if (this.id == BossFactory.test1 || this.id == BossFactory.test1) {
            double d = da;
            if (topDame.containsKey(plAtt.id)) {
                d += topDame.get(plAtt.id);
            }
            topDame.put(plAtt.id, d);
            HashMap<Long, Double> hashMap = Util.sortHashMapByValue(topDame);
            List<Map.Entry<Long, Double>> entryList = new ArrayList<>(hashMap.entrySet());
            for (int i = 0; i < 3; i++) {
                if (i < entryList.size()) {
                    Map.Entry<Long, Double> entry = entryList.get(i);
                    if (entry != null) {
                        // Check if pl is not null before accessing its properties
                        Player pl = Client.gI().getPlayer(entry.getKey());
                        if (pl != null) {
                            Service.getInstance().sendTextTime(plAtt, (byte) (i + 10), String.format("#%s %s [%s", i + 1, pl.name, Math.ceil((double) entry.getValue() / (double) this.nPoint.hpMax * 100)) + " %]", (short) -1);
                        }
                    }
                }
            }
            entryList.clear();
        }
    }
    return da;
}

}
