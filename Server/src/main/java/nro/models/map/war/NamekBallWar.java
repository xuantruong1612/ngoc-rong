package nro.models.map.war;

import lombok.Getter;
import nro.consts.ConstItem;
import nro.consts.ConstPlayer;
import nro.manager.NamekBallManager;
import nro.models.clan.Clan;
import nro.models.dragon.Poruga;
import nro.models.map.NamekBall;
import nro.models.npc.Npc;
import nro.models.player.Player;
import nro.services.PlayerService;
import nro.services.Service;

/**
 * @build by arriety
 */
public class NamekBallWar {

    private static final NamekBallWar INSTANCE = new NamekBallWar();
    @Getter
    private Player[] holders = new Player[7];

    public static NamekBallWar gI() {
        return INSTANCE;
    }

    public void pickBall(Player player, NamekBall item) {
        if (player.isHoldNamecBall) {
            dropBall(player);
        }
        if (item.isStone()) {
            Service.getInstance().sendThongBao(player, "Chỉ là cục đá, vác chi cho nặng!");
            return;
        }
        player.isHoldNamecBall = true;
        Service.getInstance().sendFlagBag(player);
        PlayerService.gI().changeAndSendTypePK(player, ConstPlayer.PK_ALL);
        item.setHolding(true);
        holders[item.getIndex()] = player;
        Service.getInstance().sendFlagBag(player);
        item.reAppearItem();
        item.setHolderName(player.name);
        Service.getInstance().sendThongBao(player, "Bạn đang giữ viên ngọc rồng Namek " + (item.getIndex() + 1) + " sao");
    }

    public void summonDragon(Player player, Npc npc) {
        if (player == null || npc == null || player.clan == null) {
            return;
        }
        Clan clan = player.clan;
        int clanID = clan.id;

        if (holders[0] != player) {
            npc.npcChat(player, "Hãy đem viên ngọc một sao tới gặp em để có thể triệu hồi rồng thần Namek");
            return;
        }

        for (Player holder : holders) {
            if (holder != null) {
                if (holder.clan != null) {
                    if (holder.clan.id != clanID) {
                        Service.getInstance().sendThongBao(holder, "Cần tập hợp đủ 7 viên ngọc rồng mới có thể triệu hồi rồng thần Namek");
                        return;
                    }
                } else {
                    new Poruga(player).summon();
                    Service.getInstance().sendThongBao(holder, "Đã xảy ra lỗi!");
                }
            } else {
                new Poruga(player).summon();
                Service.getInstance().sendThongBao(holder, "Đã xảy ra lỗi!");
            }
        }
        NamekBall oneStar = NamekBallManager.gI().findByID(ConstItem.NGOC_RONG_NAMEK_1_SAO);
        if (!oneStar.isCleaning()) {
            oneStar.setCleaning(true);
            oneStar.setCleaningTime(10 * 60);
            npc.npcChat(player, "Hãy đợi 10 phút em lau sạch ngọc rồng,trong thời gian lau ngọc nếu một trong các viên ngọc bị rơi sẽ làm mới thời gian lau");
            return;
        }
        if (oneStar.isCleaning() && oneStar.getCleaningTime() == 0) {
            new Poruga(player).summon();
        }
    }

    public void dropBall(Player player) {
        if (player == null) {
            return;
        }

        int index = -1;
        for (int i = 0; i < holders.length; i++) {
            if (holders[i] == player) {
                holders[i] = null;
                index = i;
            }
        }

        if (index != -1) {
            player.isHoldNamecBall = false;
            NamekBall ball = NamekBallManager.gI().findByIndex(index);
            if (ball.getIndex() != 0) {
                NamekBall oneStar = NamekBallManager.gI().findByIndex(0);
                oneStar.setCleaning(false);
            }
            ball.setCleaning(false);
            ball.setZone(player.zone);
            ball.x = player.location.x;
            ball.y = player.location.y;
            ball.setHolding(false);
            ball.reAppearItem();
            ball.setHolderName("");
        }

        PlayerService.gI().changeAndSendTypePK(player, ConstPlayer.NON_PK);
        Service.getInstance().sendFlagBag(player);
    }

}
