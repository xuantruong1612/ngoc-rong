package nro.models.npc;

import nro.models.player.Player;

/**
 *
 * Arriety
 *
 */
public interface IAtionNpc {

    void openBaseMenu(Player player);

    void confirmMenu(Player player, int select);

}
