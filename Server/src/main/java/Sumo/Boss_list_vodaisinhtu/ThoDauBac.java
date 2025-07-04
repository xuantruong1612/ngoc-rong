package Sumo.Boss_list_vodaisinhtu;

import nro.map.VoDaiSinhTu.BossVoDaiSinhTu;
import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.player.Player;

public class ThoDauBac extends BossVoDaiSinhTu {

    public ThoDauBac(Player player) {
        super(BossFactory.ThoDauBac, BossData.ThoDauBac);
        this.playerAtt = player;
    }
}