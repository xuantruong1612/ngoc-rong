package Sumo.Boss_list_vodaisinhtu;

import nro.map.VoDaiSinhTu.BossVoDaiSinhTu;
import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.player.Player;

public class VuaQuySatan extends BossVoDaiSinhTu {

    public VuaQuySatan(Player player) {
        super(BossFactory.VuaQuySatan, BossData.VuaQuySatan);
        this.playerAtt = player;
    }
}