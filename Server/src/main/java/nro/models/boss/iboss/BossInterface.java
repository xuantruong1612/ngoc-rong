package nro.models.boss.iboss;

import nro.models.player.Player;

/**
 *
 * Arriety
 *
 */
public interface BossInterface extends IBossStatus {

    void update();

    void rewards(Player pl); //phần thưởng sau khi bị chết

    Player getPlayerAttack() throws Exception; //lấy ra 1 player để đánh

    void joinMap();

//    void active();

    void leaveMap();

    boolean talk();

    void generalRewards(Player player);
}
