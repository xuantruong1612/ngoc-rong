package nro.models.player;

import java.util.ArrayList;
import java.util.List;
import nro.models.item.Item;
import nro.models.item.ItemOption;

/**
 *
 * Arriety
 *
 */
public class Inventory {

    public static final long LIMIT_GOLD = 10000000000000L;

    public static final int LIMIT_RUBY = 2000000000;

    private Player player;

    public Item trainArmor;

    public List<Item> itemsBody;
    public List<Item> itemsBag;
    public List<Item> itemsBox;

    public List<Item> itemsBoxCrackBall;
//    public List<Item> itemsReward;

    public long gold, goldLimit;
    public int gem;
    public int ruby;
 public int event;
//    public int goldBar;
    public Inventory(Player player) {
        this.player = player;
        itemsBody = new ArrayList<>();
        itemsBag = new ArrayList<>();
        itemsBox = new ArrayList<>();
        itemsBoxCrackBall = new ArrayList<>();
//        itemsReward = new ArrayList<>();
    }

    public int getGem() {
        return this.gem;
    }

    public int getRuby() {
        return this.ruby;
    }

    public long getGold() {
        return this.gold;
    }

    public long getGoldLimit() {
        return goldLimit + LIMIT_GOLD;
    }

    public int getRubyLimit() {
        return ruby + 2000000000;
    }

    public boolean ishaveoption(List<Item> item, int index, int id) {
        Item it = item.get(index);
        if (it != null && it.isNotNullItem()) {
            return it.itemOptions.stream().anyMatch(option -> option != null && option.optionTemplate.id == id);
        }
        return false;
    }

    public double getparam(Item item, int id) {
        for (ItemOption option : item.itemOptions) {
            if (option != null && option.optionTemplate.id == id) {
                return option.param;
            }

        }
        return 0;
    }

    public long getGoldDisplay() {
        long amount = gold;
        if (amount > Integer.MAX_VALUE && !player.isVersionAbove(214)) {
            return Integer.MAX_VALUE;
        }
        return amount;
    }

    public void subGem(int num) {
        this.gem -= num;
    }

    public void subGold(long num) {
        this.gold -= num;
    }

    public void subRuby(int num) {
        this.ruby -= num;
    }

    public void addGold(long gold) {
        this.gold += gold;
        long goldLimit = getGoldLimit();
        if (this.gold > goldLimit) {
            this.gold = goldLimit;
        }
    }

    public void addRuby(int ruby) {
        this.ruby += ruby;
        int rupilimit = getRubyLimit();
        if (this.ruby > rupilimit) {
            this.ruby = rupilimit;
        }
    }

    public void dispose() {
        this.player = null;
        if (this.trainArmor != null) {
            this.trainArmor.dispose();
        }
        this.trainArmor = null;
        if (this.itemsBody != null) {
            for (Item it : this.itemsBody) {
                it.dispose();
            }
            this.itemsBody.clear();
        }
        if (this.itemsBag != null) {
            for (Item it : this.itemsBag) {
                it.dispose();
            }
            this.itemsBag.clear();
        }
        if (this.itemsBox != null) {
            for (Item it : this.itemsBox) {
                it.dispose();
            }
            this.itemsBox.clear();
        }
        if (this.itemsBoxCrackBall != null) {
            for (Item it : this.itemsBoxCrackBall) {
                it.dispose();
            }
            this.itemsBoxCrackBall.clear();
        }
        this.itemsBody = null;
        this.itemsBag = null;
        this.itemsBox = null;
        this.itemsBoxCrackBall = null;
    }
}
