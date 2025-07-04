package nro.models.player;

import nro.consts.ConstMap;
import nro.consts.ConstPlayer;
import nro.models.map.Map;
import nro.models.map.Zone;
import nro.server.Manager;
import nro.services.MapService;
import nro.services.PlayerService;
import nro.services.Service;
import nro.utils.Util;

/**
 * @build by arriety
 */
public class Referee extends Player {

    private long lastTimeChat;

    public void initReferee() {
        init();
    }

    private Zone z;

    @Override
    public short getHead() {
        return 1497;
    }

    @Override
    public short getBody() {
        return 1498;
    }

    @Override
    public short getLeg() {
        return 1499;
    }

    public void joinMap(Zone z, Player player) {
        MapService.gI().goToMap(player, z);
        z.load_Me_To_Another(player);
    }

    @Override
    public int version() {
        return 214;
    }

    @Override
    public void update() {
        Service.getInstance().sendMoney(this);
        PlayerService.gI().hoiSinh(this);
        Service.getInstance().hsChar(this, this.nPoint.hpMax, this.nPoint.mpMax);
        PlayerService.gI().sendInfoHpMp(this);
        if (Util.canDoWithTime(lastTimeChat, 20000)) {
            Service.getInstance().chat(this, "Nạp Đi Cày Chay Đòi Ngon , Chỉ Ăn Db");
         //   Service.getInstance().chat(this, "Còn chờ gì nữa mà không đua tham gia để nhận nhiều phẩn quà hấp dẫn");
            lastTimeChat = System.currentTimeMillis();
        }
    }

        
     
    
    
    
    private void init() {
        int id = -1000000;
        for (Map m : Manager.MAPS) {
            if (m.mapId == ConstMap.DAI_HOI_VO_THUAT) {
                for (Zone z : m.zones) {
                    Referee pl = new Referee();
                    pl.name = "Trọng Tài";
                    pl.gender = 0;
                    pl.id = id++;
                    pl.nPoint.hpMax = 69;
                    pl.nPoint.hpg = 69;
                    pl.nPoint.hp = 69;
                    pl.nPoint.setFullHpMp();
                    pl.location.x = 387;
                    pl.location.y = 336;
                    pl.isMiniPet = true;
                    joinMap(z, pl);
                    z.setReferee(pl);
                }
            } else if (m.mapId == ConstMap.DAI_HOI_VO_THUAT_129) {
                for (Zone z : m.zones) {
                    Referee pl = new Referee();
                    pl.name = "Trọng Tài";
                    pl.gender = 0;
                    pl.id = id++;
                    pl.nPoint.hpMax = 69;
                    pl.nPoint.hpg = 69;
                    pl.nPoint.hp = 69;
                    pl.nPoint.setFullHpMp();
                    pl.location.x = 385;
                    pl.location.y = 264;
                    pl.isMiniPet = true;
                    joinMap(z, pl);
                    z.setReferee(pl);
                }
            }else if (m.mapId == 14) {
                for (Zone z : m.zones) {
                    Referee pl = new Referee();
                     pl.name = "admin";
                    pl.gender = 0;
                    pl.id = id++;
                    pl.nPoint.hpMax = Double.MAX_VALUE;
                    pl.nPoint.hpg = 69;
                    pl.nPoint.hp = Double.MAX_VALUE;
                    pl.nPoint.setFullHpMp();
                    pl.location.x = 795;
                    pl.location.y = 336;
                   pl.isClone = true;
                    joinMap(z, pl);
                    z.setReferee(pl);
                   PlayerService.gI().changeAndSendTypePK(pl, ConstPlayer.PK_ALL);
                    joinMap(z, pl);
                    z.setReferee(pl); 
                   }  
                    }else if (m.mapId == 209) {
                for (Zone z : m.zones) {
                    Referee pl = new Referee();
                      pl.name = "admin";
                    pl.gender = 0;
                    pl.id = id++;
                    pl.nPoint.hpMax = Double.MAX_VALUE;
                    pl.nPoint.hpg = 69;
                    pl.nPoint.hp = Double.MAX_VALUE;
                     pl.nPoint.mpg = 69;
                     pl.nPoint.mp = Double.MAX_VALUE;
                    pl.nPoint.setFullHpMp();
                    pl.location.x = 529;
                    pl.location.y = 408;
                    pl.isClone = true;
                    joinMap(z, pl);
                    z.setReferee(pl);
                   PlayerService.gI().changeAndSendTypePK(pl, ConstPlayer.PK_ALL);
                    joinMap(z, pl);
                    z.setReferee(pl); 
                     Service.getInstance().chat(this, "Đấm Con Cặc");
                    
                    
               
                }
            }
        }
    }
}
