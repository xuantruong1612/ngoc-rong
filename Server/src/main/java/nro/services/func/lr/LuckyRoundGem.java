package nro.services.func.lr;

import nro.models.item.Item;
import nro.models.player.Player;
import nro.services.RewardService;
import nro.services.Service;

import java.util.List;

/**
 *
 * @Build Arriety
 *
 */
public class LuckyRoundGem extends AbsLuckyRound {

    private static LuckyRoundGem i;

    public static LuckyRoundGem gI() {
        if (i == null) {
            i = new LuckyRoundGem();
        }
        return i;
    }

    private LuckyRoundGem() {
        this.price = 500;
        this.ticket = 821;
        this.icons.add(419);
        this.icons.add(420);
        this.icons.add(421);
        this.icons.add(422);
        this.icons.add(423);
        this.icons.add(424);
        this.icons.add(425);
    }

    @Override
    public List<Item> reward(Player player, byte quantity) {
        List<Item> list = RewardService.gI().getListItemLuckyRound(player, quantity);
        addItemToBox(player, list);
        return list;
    }

    @Override
    public boolean checkMoney(Player player, int price) {
        if (player.inventory.getRuby() < price) {
            Service.getInstance().sendThongBao(player, "Bạn không đủ ngọc hồng");
            return false;
        }
        return true;
    }

    @Override
    public void payWithMoney(Player player, int price) {
        player.inventory.subRuby(price);
    }

}
