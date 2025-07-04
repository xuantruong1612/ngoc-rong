package nro.manager;

import lombok.Getter;
import nro.jdbc.DBService;
import nro.models.player.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import nro.server.ServerManager;

/**
 *
 * @author Arriety
 */
public class TopCoin implements Runnable {

    @Getter
    private String namePlayer;
    private int tongNap;
    private static final TopCoin INSTANCE = new TopCoin();

    public static TopCoin getInstance() {
        return INSTANCE;
    }

    @Override
    public void run() {
        try {
            while (ServerManager.isRunning) {
                try (Connection con = DBService.gI().getConnectionLoadCoint(); PreparedStatement ps = con.prepareStatement("SELECT player.id, player.name, player.head, player.gender, player.data_point, tongnap FROM player INNER JOIN account ON account.id = player.account_id WHERE tongnap > 0 ORDER BY tongnap DESC LIMIT 1;"); ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
//                        player.id = rs.getInt("id");
                        namePlayer = rs.getString("name");
//                        player.head = rs.getShort("head");
//                        player.gender = rs.getByte("gender");
                        tongNap = rs.getInt("tongnap");
                    }
                }
                Thread.sleep(1000);
            }
        } catch (InterruptedException | SQLException e) {
            e.printStackTrace();
        }
    }
}
