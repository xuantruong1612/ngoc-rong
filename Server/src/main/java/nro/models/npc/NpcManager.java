package nro.models.npc;

import nro.consts.ConstNpc;
import nro.consts.ConstTask;
import nro.models.player.Player;
import nro.server.Manager;
import nro.services.TaskService;

import java.util.ArrayList;
import java.util.List;
import nro.utils.Util;

/**
 *
 * Arriety
 *
 */
public class NpcManager {

    public static Npc getByIdAndMap(int id, int mapId) {
        for (Npc npc : Manager.NPCS) {
            if (npc.tempId == id && npc.mapId == mapId) {
                return npc;
            }
        }
        return null;
    }

    public static Npc getNpc(byte tempId) {
        for (Npc npc : Manager.NPCS) {
            if (npc.tempId == tempId) {
                return npc;
            }
        }
        return null;
    }

    public static List<Npc> getNpcsByMapPlayer(Player player) {
        List<Npc> list = new ArrayList<>();
        if (player.zone != null) {
            for (Npc npc : player.zone.map.npcs) {
                if (npc.tempId == ConstNpc.QUA_TRUNG && player.mabuEgg == null && player.zone.map.mapId == (21 + player.gender)) {
                    continue;
                } else if (npc.tempId == ConstNpc.QUA_TRUNG && player.billEgg == null && player.zone.map.mapId == 154) {
                    continue;
                } else if (npc.tempId == ConstNpc.DUA_HAU && player.duahau == null && player.zone.map.mapId == 0) {
                    continue;
                } else if (npc.tempId == ConstNpc.CALICK && TaskService.gI().getIdTask(player) < ConstTask.TASK_20_0) {
                    continue;
                } else if (npc.tempId == ConstNpc.CALICK && player.zone.map.mapId != 102 && player.zone.map.mapId != 27) {
                    if (Util.isTrue(80, 100)) {
                        continue;
                    } else {
                        npc.cx = Util.nextInt(20, player.zone.map.mapWidth - 20);
                        npc.cy = player.zone.map.yPhysicInTop(npc.cx, 0);
                    }
                }
                list.add(npc);
            }
        }
        return list;
    }
}
