package Sumo.Boss_list_Broly;

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
public class Broly extends FutureBoss {

    public int petgender = Util.nextInt(0, 2);

    public Broly() {

        super(BossFactory.Broly, BossData.Broly);
    }

    @Override
    protected boolean useSpecialSkill() {

        return false;
    }

    @Override
    public void rewards(Player pl) {
        if (pl.pet != null) {
            PetService.gI().removePet(pl); // Xóa đệ cũ trước
        }
        PetService.gI().createNormalPet(pl, petgender); // Gán đệ mới
    }

    @Override
    public void idle() {

    }

    // @Override
    // public void attack() {
    // super.attack();
    // if (this.pet == null) {
    // PetService.gI().createNormalPet(this, petgender);
    // this.pet.nPoint.tlNeDon = 20000;
    // }
    // }

    @Override
    public void checkPlayerDie(Player pl) {
        if (this.pet == null) { // Nếu Boss chưa có Pet, tạo mới ngay
            PetService.gI().createNormalPet(this, petgender); // Gán giá trị cho Boss

            if (this.pet != null) { // Nếu tạo thành công, thiết lập chỉ số
                this.pet.nPoint.tlNeDon = 20000;
                System.out.println("Boss đã có đệ mới!");
            } else {
                System.err.println("Đang Săn Đệ - Không thể tạo Pet!");
            }
        }
    }

    @Override
    public void initTalk() {
        textTalkMidle = new String[] { "Ta chính là đệ nhất vũ trụ cao thủ" };
        textTalkAfter = new String[] { "Ác quỷ biến hình aaa..." };
    }

}
