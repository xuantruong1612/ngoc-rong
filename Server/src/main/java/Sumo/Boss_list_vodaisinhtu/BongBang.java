package Sumo.Boss_list_vodaisinhtu;

import nro.map.VoDaiSinhTu.BossVoDaiSinhTu;
import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.player.Player;

public class BongBang extends BossVoDaiSinhTu {

    public BongBang(Player player) {
        super(BossFactory.BongBang, BossData.BongBang);
        this.playerAtt = player;
    }
}