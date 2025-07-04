package nro.models.player;

import nro.models.item.Item;
import nro.models.item.ItemOption;

/**
 *
 * @author 💖 Trần Lại 💖
 * @copyright 💖 GirlkuN 💖
 *
 */
public class SetClothes {

    private Player player;

    public SetClothes(Player player) {
        this.player = player;
    }

    public byte songoku;
    public byte songoku1000pt;
    public byte masenco3000pt;
    public byte antomic2000pt;

    public byte full_set_broly;
    public byte full_set_picolo;
    public byte full_set_fide;
    public byte full_set_vegeta;
    public byte setkichhoat18sao;
    public byte setkichhoat30sao;
    public byte setkichhoat45sao;
    public byte setkichhoat65sao;
    public byte setkichhoat99sao;
    public byte setkichhoat200sao;
    public byte setkichhoat300sao;
    public byte setkichhoat500sao;
    public byte setkichhoat700sao;
    public byte setkichhoat999sao;
    public byte tinhluyen16;
    public byte thienXinHang;
    public byte kirin;

    public byte ocTieu;
    public byte pikkoroDaimao;
    public byte picolo;

    public byte kakarot;
    public byte cadic;
    public byte nappa;

    public byte setnrocuon;

    public boolean godClothes;
    public int ctHaiTac = -1;
    public int ctBunmaXecXi = -1;

    public void setup() {
        setDefault();
        setupSKT();
        this.godClothes = true;
        for (int i = 0; i < 6; i++) {
            Item item = this.player.inventory.itemsBody.get(i);
            if (item.isNotNullItem()) {
                if (item.template.id > 567 || item.template.id < 555) {
                    this.godClothes = false;
                    break;
                }
            } else {
                this.godClothes = false;
                break;
            }
        }
        Item ct = this.player.inventory.itemsBody.get(5);
        if (ct.isNotNullItem()) {
            switch (ct.template.id) {
              
                case 6255:
                    this.ctHaiTac = ct.template.id;
                    break;
                case 4654:
                    this.ctBunmaXecXi = ct.template.id;
                    break;
            }
        }
    }

    private void setupSKT() {
        for (int i = 0; i < 7; i++) {
            Item item = this.player.inventory.itemsBody.get(i);
            if (item.isNotNullItem()) {
                boolean isActSet = false;
                for (ItemOption io : item.itemOptions) {
                    switch (io.optionTemplate.id) {
                        case 129:

                            isActSet = true;
                            songoku++;
                            break;

                        case 231:
                            isActSet = true;
                            songoku1000pt++;
                            break;
                        case 230:
                            isActSet = true;
                            antomic2000pt++;
                            break;
                        case 229:
                            isActSet = true;
                            masenco3000pt++;
                            break;

                        case 127:

                            isActSet = true;
                            thienXinHang++;
                            break;
                        case 128:

                            isActSet = true;
                            kirin++;
                            break;
                        case 131:

                            isActSet = true;
                            ocTieu++;
                            break;
                        case 132:

                            isActSet = true;
                            pikkoroDaimao++;
                            break;
                        case 130:

                            isActSet = true;
                            picolo++;
                            break;
                        case 135:

                            isActSet = true;
                            nappa++;
                            break;
                        case 133:
                            isActSet = true;
                            kakarot++;
                            break;
                        case 134:
                            isActSet = true;
                            cadic++;
                            break;

                        case 141:
                            isActSet = true;
                            setkichhoat18sao++;
                            break;
                        case 142:
                            isActSet = true;
                            setkichhoat30sao++;
                            break;
                        case 143:
                            isActSet = true;
                            setkichhoat45sao++;
                            break;
                        case 144:
                            isActSet = true;
                            setkichhoat65sao++;
                            break;
                        case 136:
                            isActSet = true;
                            setkichhoat99sao++;
                            break;
                        case 137:
                            isActSet = true;
                            setkichhoat200sao++;
                            break;
                        case 138:
                            isActSet = true;
                            setkichhoat300sao++;
                            break;
                        case 139:
                            isActSet = true;
                            setkichhoat500sao++;
                            break;
                        case 140:
                            isActSet = true;
                            setkichhoat700sao++;
                            break;
                        case 145:
                            isActSet = true;
                            setkichhoat999sao++;
                            break;
                        case 146:
                            isActSet = true;
                            tinhluyen16++;
                            break;

                        case 176:
                            isActSet = true;
                            full_set_fide++;
                            break;
                        case 177:
                            isActSet = true;
                            full_set_broly++;
                            break;
                        case 178:
                            isActSet = true;
                            full_set_picolo++;
                            break;
                        case 180:
                            isActSet = true;
                            full_set_vegeta++;
                            break;

                    }
                    if (isActSet) {
                        break;
                    }
                }
            } else {
                break;
            }
        }
    }

    private void setkhvip() {
        for (int i = 0; i < 6; i++) {
            Item item = this.player.inventory.itemsBody.get(i);
            if (item.isNotNullItem()) {
                boolean isActSet1 = false;
                for (ItemOption io : item.itemOptions) {
                    switch (io.optionTemplate.id) {

//                        case 231:
//                            isActSet1 = true;
//                            songoku1000pt++;
//                            break;    
                    }
                    if (isActSet1) {
                        break;
                    }
                }
            } else {
                break;
            }
        }
    }

    private void setDefault() {
        this.songoku = 0;
        this.songoku1000pt = 0;
        this.antomic2000pt = 0;
        this.masenco3000pt = 0;
        this.thienXinHang = 0;
        this.kirin = 0;

        this.setkichhoat18sao = 0;
        this.setkichhoat30sao = 0;
        this.setkichhoat45sao = 0;
        this.setkichhoat65sao = 0;
        this.setkichhoat99sao = 0;
        this.setkichhoat200sao = 0;
        this.setkichhoat300sao = 0;
        this.setkichhoat500sao = 0;
        this.setkichhoat700sao = 0;
        this.setkichhoat999sao = 0;
        this.tinhluyen16 = 0;
         this.full_set_broly = 0;
         this.full_set_fide = 0;
         this.full_set_picolo = 0;
         this.full_set_vegeta = 0;

        this.ocTieu = 0;
        this.pikkoroDaimao = 0;
        this.picolo = 0;
        this.kakarot = 0;
        this.setnrocuon = 0;
        this.cadic = 0;
        this.nappa = 0;
        this.godClothes = false;
        this.ctHaiTac = -1;
    }

    public void dispose() {
        this.player = null;
    }
}
