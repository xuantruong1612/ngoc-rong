
package nro.models.player;

import nro.services.Service;

/**
 *
 * @author Arrriety
 */
public class UpdateEffChar {

    private static UpdateEffChar i;

    public static UpdateEffChar getInstance() {
        if (i == null) {
            i = new UpdateEffChar();
        }
        return i;
    }

    public void updateEff(Player player) {
        try {
            if (player.isPl() && player.nPoint != null) {
                int playerIdInt = (int) player.id;
                switch (playerIdInt) {
                    case 16803:
                        Service.getInstance().addEffectChar(player, 80, 1, -1, -1, 1);
                        break;
                    default:
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
