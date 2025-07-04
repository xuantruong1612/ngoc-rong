package nro.map.VoDaiSinhTu;

import Sumo.Boss_list_vodaisinhtu.BongBang;
import Sumo.Boss_list_vodaisinhtu.DRACULA;
import Sumo.Boss_list_vodaisinhtu.ThoDauBac;
import Sumo.Boss_list_vodaisinhtu.VoHinh;
import Sumo.Boss_list_vodaisinhtu.VuaQuySatan;
import lombok.Getter;
import lombok.Setter;
import nro.consts.ConstPlayer;
import nro.models.boss.Boss;
import nro.models.map.challenge.MartialCongressService;
import nro.models.npc.Npc;
import nro.models.player.Player;
import nro.models.skill.Skill;
import nro.server.io.Message;
import nro.services.EffectSkillService;
import nro.services.PlayerService;
import nro.services.Service;
import nro.utils.Log;
import nro.utils.Util;

public class VoDaiSinhTu {

    @Setter
    @Getter
    private Player player;
    @Setter
    private Boss boss;
    @Setter
    private Player npc;

    @Setter
    private int time;
    private int round;
    @Setter
    private int timeWait;

    public void update() {
        if (time > 0) {
            time--;
            if (!player.isDie() && player != null && player.zone != null) {
                if (boss.isDie()) {
                    round++;
                    boss.leaveMap();
                    toTheNextRound();
                }
            } else if (player.zone != null) {
                if (player.zone.map.mapId != 112) {
                    endChallenge();
                }
            } else {
                endChallenge();
            }
        } else {
            timeOut();
        }
        if (timeWait > 0) {
            switch (timeWait) {
                case 5 ->
                    ready();
                case 3 ->
                    Service.getInstance().chat(boss, "Sẵn sàng chưa?");
                case 2 ->
                    Service.getInstance().chat(player, "OK");
            }
            timeWait--;
        }
    }

    public void ready() {
        Util.setTimeout(() -> {
            MartialCongressService.gI().sendTypePK(player, boss);
            PlayerService.gI().changeAndSendTypePK(this.player, ConstPlayer.PK_PVP);
            boss.setStatus((byte) 3);
        }, 5000);
    }

    public void toTheNextRound() {
        PlayerService.gI().changeAndSendTypePK(player, ConstPlayer.NON_PK);
        Boss boss1;
        switch (round) {
            case 0 ->
                boss1 = new DRACULA(player);
            case 1 -> {
                this.npcChat(player, "Tốt lắm, tiếp tục đấm vô mồm nó nào", player.zone.map.npcs.get(0));
                boss1 = new VoHinh(player);
            }
            case 2 -> {
                this.npcChat(player, "Tốt lắm, tiếp tục đấm vô mồm nó nào", player.zone.map.npcs.get(0));
                boss1 = new BongBang(player);
            }
            case 3 -> {
                this.npcChat(player, "Tốt lắm, tiếp tục đấm vô mồm nó nào", player.zone.map.npcs.get(0));
                boss1 = new VuaQuySatan(player);
            }
            case 4 -> {
                this.npcChat(player, "Tốt lắm, tiếp tục đấm vô mồm nó nào", player.zone.map.npcs.get(0));
                boss1 = new ThoDauBac(player);
            }
            default -> {
                this.npcChat(player, "Đây là phần thưởng của con", player.zone.map.npcs.get(0));
                champion();
                return;
            }
        }
        PlayerService.gI().setPos(player, 390, 336, 0);
        setTimeWait(5);
        setBoss(boss1);
        PlayerService.gI().setPos(boss1, 435, 264, 0);
        setTime(185);
        resetSkill();
    }

    public void npcChat(Player player, String text, Npc npc) {
        Message msg;
        try {
            msg = new Message(124);
            msg.writer().writeShort(npc.tempId);
            msg.writer().writeUTF(text);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            Log.error(Service.class, e);
        }
    }

    private void resetSkill() {
        for (Skill skill : player.playerSkill.skills) {
            skill.lastTimeUseThisSkill = 0;
        }
        Service.getInstance().sendTimeSkill(player);
    }

    private void timeOut() {
        Service.getInstance().sendThongBao(player, "Bạn bị xử thua vì hết thời gian");
        endChallenge();
    }

    private void champion() {
        player.DoneVoDaiBaHatMit = 1;
        endChallenge();
    }

    public void leave() {
        setTime(0);
        EffectSkillService.gI().removeStun(player);
        Service.getInstance().sendThongBao(player, "Bạn bị xử thua vì rời khỏi võ đài");
        endChallenge();
    }

    private void reward() {
        if (player.levelWoodChest < round) {
            player.levelWoodChest = round;
        }
    }

    public void endChallenge() {
        reward();
        PlayerService.gI().hoiSinh(player);
        PlayerService.gI().changeAndSendTypePK(player, ConstPlayer.NON_PK);
        if (boss != null) {
            boss.leaveMap();
        }
        PlayerService.gI().setPos(player, 178, 408, 0);
        VoDaiSinhTuManager.gI().remove(this);
    }
}
