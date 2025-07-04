package Sumo.Boss_list_vodaisinhtu;

import nro.map.VoDaiSinhTu.BossVoDaiSinhTu;
import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.player.Player;

public class VoHinh extends BossVoDaiSinhTu {

    public VoHinh(Player player) {
        super(BossFactory.VoHinh, BossData.VO_HINH);
        this.playerAtt = player;
    }
}