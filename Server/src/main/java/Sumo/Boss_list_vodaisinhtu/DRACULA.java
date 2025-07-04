package Sumo.Boss_list_vodaisinhtu;

import nro.map.VoDaiSinhTu.BossVoDaiSinhTu;
import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.player.Player;

public class DRACULA extends BossVoDaiSinhTu {

    public DRACULA(Player player) {
        super(BossFactory.DRACULA, BossData.DRACULA);
        this.playerAtt = player;
    }
}