package nro.models.boss.broly;

import java.util.Random;
import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.boss.FutureBoss;
import nro.models.item.ItemOption;
import nro.models.map.ItemMap;
import nro.models.player.Player;
import nro.services.PetService;
import nro.services.Service;
import nro.services.TaskService;
import nro.utils.Util;

/**
 *
 * @Arriety
 *
 */
public class SuperBroly extends FutureBoss {

    public int petgender = Util.nextInt(0, 2);

    public SuperBroly() {

        super(BossFactory.SUPERBROLY, BossData.SUPERBROLY);
    }

    @Override
    protected boolean useSpecialSkill() {

        return false;
    }
@Override
public void rewards(Player pl) {
       if (Util.isTrue(10, 100)) { // Tỉ lệ 50%
            int slcarot3 = 1;
            for (int i = 0; i < slcarot3; i++) {
                ItemMap carot = new ItemMap(zone, 1944, 1, 10 * i + this.location.x,
                        zone.map.yPhysicInTop(this.location.x, 0), -1);
                // carot.options.add(new ItemOption(Util.nextInt(176, 180), Util.nextInt(1, 50)));
                carot.options.add(new ItemOption(31, 0));
                // Tỉ lệ 99% ra opti 93
                if (Util.isTrue(999, 1000)) {
                    carot.options.add(new ItemOption(93, 1));
                }

                Service.getInstance().dropItemMap(this.zone, carot);
            }

         

                    TaskService.gI().checkDoneTaskKillBoss(pl, this);
                    generalRewards(pl);
                }

        
    }





    @Override
    public void idle() {

    }

  
    
//    @Override
//  public void attack() {
//     super.attack();
//        if (this.pet == null) {
//            PetService.gI().createNormalPet(this, petgender);
//            this.pet.nPoint.tlNeDon = 20000;
//        }
//  }
    

   @Override
public void checkPlayerDie(Player pl) {
    if (this.pet == null) { // Nếu Boss chưa có Pet, tạo mới ngay
       PetService.gI().createbrolyPet(this, petgender); // 🔹 Gán giá trị cho Boss

        if (this.pet != null) { // Nếu tạo thành công, thiết lập chỉ số
            this.pet.nPoint.tlNeDon = 20000;
            System.out.println("🐲 Boss đã có đệ mới!");
        } else {
            System.err.println("⚠️ Đang Săn Đệ - Không thể tạo Pet!");
        }
    }
}


    @Override
    public void initTalk() {
        textTalkMidle = new String[]{"Ta chính là đệ nhất vũ trụ cao thủ"};
        textTalkAfter = new String[]{"Ác quỷ biến hình aaa..."};
    }

}
