package nro.server;

import java.io.BufferedWriter;
import java.io.FileWriter;
import nro.attr.AttributeManager;
import nro.jdbc.DBService;
import nro.jdbc.daos.AccountDAO;
import nro.jdbc.daos.HistoryTransactionDAO;
import nro.jdbc.daos.PlayerDAO;
import nro.login.LoginSession;
import nro.manager.ConsignManager;
import nro.manager.TopManager;
import nro.models.boss.BossFactory;
import nro.models.boss.BossManager;
import nro.models.map.challenge.MartialCongressManager;
import nro.models.map.dungeon.DungeonManager;
import nro.models.map.phoban.BanDoKhoBau;
import nro.models.map.phoban.DoanhTrai;
import nro.models.player.Player;
import nro.netty.NettyServer;
import nro.quayTamBao.TamBaoService;
import nro.server.io.Session;
import nro.services.ClanService;
import nro.utils.Log;
import nro.utils.TimeUtil;
import nro.utils.Util;
import lombok.Getter;
import lombok.Setter;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import nro.map.VoDaiSinhTu.VoDaiSinhTuManager;

/**
 *
 * @Build Arriety
 */
public class ServerManager {

    public static String timeStart;

    public static final Map CLIENTS = new HashMap();

    public static String NAME = "";
    public static int PORT = 4441;

    private Controller controller;

    private static ServerManager instance;

    public static ServerSocket listenSocket;
    public static boolean isRunning;

    @Getter
    private LoginSession login;
    public static boolean updateTimeLogin;
    @Getter
    @Setter
    private AttributeManager attributeManager;
    private long lastUpdateAttribute;
    @Getter
    private DungeonManager dungeonManager;

    public void init() {
        Manager.gI();
        HistoryTransactionDAO.deleteHistory();
        BossFactory.initBoss();
        this.controller = new Controller();
        if (updateTimeLogin) {
            AccountDAO.updateLastTimeLoginAllAccount();
        }
    }

    public static ServerManager gI() {
        if (instance == null) {
            instance = new ServerManager();
            instance.init();
        }
        return instance;
    }

    public static void main(String[] args) {
        timeStart = TimeUtil.getTimeNow("dd/MM/yyyy HH:mm:ss");
        ServerManager.gI().run();
    }

    long startUd = System.currentTimeMillis();

    public void run() {
        try {
            isRunning = true;
            activeCommandLine();
            activeGame();
            activeLogin();
            autoTask();
            //   autotop();
            TamBaoService.loadItem();
            //(new AutoMaintenance(6, 0, 0)).start();
            //  (new AutoMaintenance(18, 00, 0)).start();
            NettyServer nettyServer = new NettyServer();
            nettyServer.start();
            new Thread(() -> {
                while (isRunning) {
                    try {
                        if (Util.canDoWithTime(startUd, 10000)) {
                            try (Connection con = DBService.gI().getConnectionForGame()) {
                                Manager.realTop(Manager.queryTopDame, con);
                            }
                            startUd = System.currentTimeMillis();
                        }
                        Thread.sleep(1000);
                    } catch (Exception e) {

                    }
                }
            }).start();
            TaiXiu.gI().lastTimeEnd = System.currentTimeMillis() + 1000;
            new Thread(TaiXiu.gI(), "Thread Tài Xỉu").start();
            Manager.reloadtop();
            cleanAccountOutDate();
            activeServerSocket();
        } catch (Exception e) {
            e.printStackTrace();
            // Xử lý ngoại lệ theo yêu cầu của bạn
        }
    }

    public void cleanAccountOutDate() {
        try (Connection con = DBService.gI().getConnectionForGame(); PreparedStatement ps = con.prepareStatement("select * from account"); ResultSet rs = ps.executeQuery()) {
            long now = System.currentTimeMillis();
            while (rs.next()) {
                int id = rs.getInt("id");
                long date = rs.getTimestamp("last_time_logout").getTime() + (1000 * 60 * 60 * 24 * 7);
                if (date < now) {
                    try (BufferedWriter bw = new BufferedWriter(new FileWriter("delete.txt", true))) {
                        bw.append("Delete Player:" + id + " | LastTime:" + TimeUtil.formatTime(date, "dd/MM/yyyy") + " | AffterTime:" + TimeUtil.formatTime(now, "dd/MM/yyyy"));
                        bw.newLine();
                        bw.append("Delete Account:" + rs.getString("username") + " | LastTime:" + TimeUtil.formatTime(date, "dd/MM/yyyy") + " | AffterTime:" + TimeUtil.formatTime(now, "dd/MM/yyyy"));
                        bw.newLine();
                        bw.newLine();
                    }
                    try (PreparedStatement deletePl = con.prepareStatement("delete from player where account_id = ?")) {
                        deletePl.setInt(1, id);
                        deletePl.executeUpdate();
                    }
                    try (PreparedStatement deleteAccount = con.prepareStatement("delete from account where id = ?")) {
                        deleteAccount.setInt(1, id);
                        deleteAccount.executeUpdate();
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    public void activeLogin() {
        login = new LoginSession();
        login.connect(Manager.loginHost, Manager.loginPort);
    }

    private void activeServerSocket() {
        try {
            Log.log("Start server......... Current thread: " + Thread.activeCount());
            listenSocket = new ServerSocket(PORT);
            while (isRunning) {
                try {
                    Socket sc = listenSocket.accept();
                    String ip = (((InetSocketAddress) sc.getRemoteSocketAddress()).getAddress()).toString().replace("/", "");
                    if (canConnectWithIp(ip)) {
                        Session session = new Session(sc, controller, ip);
                        session.ipAddress = ip;
                    } else {
                        sc.close();
                    }
                } catch (Exception e) {
                }
            }
            listenSocket.close();
        } catch (Exception e) {
            Log.error(ServerManager.class, e, "Lỗi mở port");
            System.exit(0);
        }
    }

    private boolean canConnectWithIp(String ipAddress) {
        Object o = CLIENTS.get(ipAddress);
        if (o == null) {
            CLIENTS.put(ipAddress, 1);
            return true;
        } else {
            int n = Integer.parseInt(String.valueOf(o));
            if (n < Manager.MAX_PER_IP) {
                n++;
                CLIENTS.put(ipAddress, n);
                return true;
            } else {
                return false;
            }
        }
    }

    public void disconnect(Session session) {
        Object o = CLIENTS.get(session.ipAddress);
        if (o != null) {
            int n = Integer.parseInt(String.valueOf(o));
            n--;
            if (n < 0) {
                n = 0;
            }
            CLIENTS.put(session.ipAddress, n);
        }
    }

    private void activeCommandLine() {
        new Thread(() -> {
            Scanner sc = new Scanner(System.in);
            while (true) {
                String line = sc.nextLine();
                if (line.equals("baotri")) {
                    new Thread(() -> {
                        Maintenance.gI().start(5);
                    }).start();
                } else if (line.equals("athread")) {
                    ServerNotify.gI().notify("Debug server: " + Thread.activeCount());
                } else if (line.equals("nplayer")) {
                    Log.error("Player in game: " + Client.gI().getPlayers().size());
                } else if (line.equals("admin")) {
                    new Thread(() -> {
                        Client.gI().close();
                    }).start();
                }
            }
        }, "Active line").start();
    }

    private void activeGame() {
        long delay = 500;
        new Thread(() -> {
            while (isRunning) {
                long l1 = System.currentTimeMillis();
                BossManager.gI().updateAllBoss();
                long l2 = System.currentTimeMillis() - l1;
                if (l2 < delay) {
                    try {
                        Thread.sleep(delay - l2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, "Update boss").start();
        new Thread(() -> {
            while (isRunning) {
                long start = System.currentTimeMillis();
                for (DoanhTrai dt : DoanhTrai.DOANH_TRAIS) {
                    dt.update();
                }
                for (BanDoKhoBau bdkb : BanDoKhoBau.BAN_DO_KHO_BAUS) {
                    bdkb.update();
                }
                long timeUpdate = System.currentTimeMillis() - start;
                if (timeUpdate < delay) {
                    try {
                        Thread.sleep(delay - timeUpdate);
                    } catch (InterruptedException e) {
                    }
                }
            }
        }, "Update pho ban").start();
        new Thread(() -> {
            while (isRunning) {
                try {
                    long start = System.currentTimeMillis();
                    if (attributeManager != null) {
                        attributeManager.update();
                        if (Util.canDoWithTime(lastUpdateAttribute, 600000)) {
                            Manager.gI().updateAttributeServer();
                        }
                    }
                    long timeUpdate = System.currentTimeMillis() - start;
                    if (timeUpdate < delay) {
                        Thread.sleep(delay - timeUpdate);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "Update Attribute Server").start();
        dungeonManager = new DungeonManager();
        dungeonManager.start();
        new Thread(dungeonManager, "Phó bản").start();
        new Thread(() -> {
            while (isRunning) {
                try {
                    long start = System.currentTimeMillis();
                    MartialCongressManager.gI().update();
                    VoDaiSinhTuManager.gI().update();
                    long timeUpdate = System.currentTimeMillis() - start;
                    if (timeUpdate < delay) {
                        Thread.sleep(delay - timeUpdate);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "Update dai hoi vo thuat").start();
    }

    public void close(long delay) {
        try {
            dungeonManager.shutdown();
        } catch (Exception e) {
            Log.error(ServerManager.class, e);
        }
        try {
            Manager.gI().updateEventCount();
        } catch (Exception e) {
            Log.error(ServerManager.class, e);
        }
        try {
            Manager.gI().updateAttributeServer();
        } catch (Exception e) {
            Log.error(ServerManager.class, e);
        }
        try {
            Client.gI().close();
        } catch (Exception e) {
            Log.error(ServerManager.class, e);
        }
        try {
            ClanService.gI().close();
        } catch (Exception e) {
            Log.error(ServerManager.class, e);
        }
        try {
            ConsignManager.getInstance().close();
        } catch (Exception e) {
            Log.error(ServerManager.class, e);
        }
        Client.gI().close();
        Log.success("BẢO TRÌ THÀNH CÔNG!...................................");
        System.exit(0);
    }

    public void saveAll(boolean updateTimeLogout) {
        try {
            List<Player> list = Client.gI().getPlayers();
            try (Connection conn = DBService.gI().getConnectionForAutoSave()) {
                for (Player player : list) {
                    try {
                        PlayerDAO.updateTimeLogout = updateTimeLogout;
                        PlayerDAO.updatePlayer(player, conn);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
//public void autotop() {
//        ScheduledExecutorService autoTop = Executors.newScheduledThreadPool(1);
//        autoTop.scheduleWithFixedDelay(() -> {
//            saveAll(false);
//            Manager.reloadtop();
//        }, 0, 60, TimeUnit.MINUTES);
//    }

    public void autoTask() {
        ScheduledExecutorService autoSave = Executors.newScheduledThreadPool(1);
        autoSave.scheduleWithFixedDelay(() -> {
            saveAll(false);
        }, 300000, 300000, TimeUnit.MILLISECONDS);
    }
}
