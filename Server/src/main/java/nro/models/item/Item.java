package nro.models.item;

import java.util.ArrayList;
import java.util.List;
import nro.utils.Util;

public class Item {

    private static final ItemOption OPTION_NULL = new ItemOption(73, 0);

    public ItemTemplate template;

    public String info;

    public int color = 0;

    public String content;

    public int quantity;

    public List<ItemOption> itemOptions;

    public long createTime;

    public long param;

    public boolean isNotNullItem() {
        return this.template != null;
    }

    public Item() {
        this.itemOptions = new ArrayList<>();
        this.createTime = System.currentTimeMillis();
    }

    public boolean isSKHLEVEL() {
        for (ItemOption itemOption : itemOptions) {
            if (itemOption.optionTemplate.id >= 217 && itemOption.optionTemplate.id <= 225) {
                return true;
            }
        }
        return false;
    }

    public String getInfo() {
        String strInfo = "";
        for (ItemOption itemOption : itemOptions) {
            strInfo += itemOption.getOptionString();
        }
        return strInfo;
    }

    public String getInfoname() {
        String strInfo = "";
        for (ItemOption itemOption : itemOptions) {
            strInfo += itemOption.getOptionString();
        }
        return strInfo;
    }

    public String getInfoItem() {
        String strInfo = "|1|" + template.name + "\n|0|";
        for (ItemOption itemOption : itemOptions) {
            strInfo += itemOption.getOptionString() + "\n";
        }
        strInfo += "|2|" + template.description;
        return strInfo;
    }

    
    public void addOptionParam(int id, long param,int...x) {
        for (int i = 0; i < this.itemOptions.size(); i++) {
            ItemOption itemOption = this.itemOptions.get(i);
            if (itemOption != null && itemOption.optionTemplate.id == id) {
                itemOption.param += param;
                return;
            }
        }
        if(x.length > 0) this.itemOptions.add(x[0],new ItemOption(id, param));
        else this.itemOptions.add(new ItemOption(id, param));
        
    } 
    
    
    public boolean isDTL() {
        if (this.template.id >= 555 && this.template.id <= 567) {
            return true;
        }
        return false;
    }

    public boolean isTrangBiCaitrang() {

        if (this.template.type == 5) {
            return true;
        }

        return false;
    }

    public boolean isTrangBiPet() {

        if (this.template.type == 21) {
            return true;
        }

        return false;
    }

    public boolean isTrangBiPhukienbang() {

        if (this.template.type == 11) {
            return true;
        }

        return false;
    }

    public boolean isTrangBiXoadong() {

        if (this.template.type == 11) {
            return true;
        }

        return false;
    }

    //skh cấp 1
    public boolean isSKHThuong() {
        for (ItemOption itemOption : itemOptions) {
            if (itemOption.optionTemplate.id >= 210 && itemOption.optionTemplate.id <= 218 || itemOption.optionTemplate.id >= 127 && itemOption.optionTemplate.id <= 135) {
                return true;
            }
        }
        return false;
    }

    public List<ItemOption> getDisplayOptions() {
        List<ItemOption> list = new ArrayList<>();
        if (itemOptions.isEmpty()) {
            list.add(OPTION_NULL);
        } else {
            for (ItemOption o : itemOptions) {
                list.add(o.format());
            }
        }
        return list;
    }

    public boolean haveOption(int idOption) {
        if (this != null && this.isNotNullItem()) {
            return this.itemOptions.stream().anyMatch(op -> op != null && op.optionTemplate.id == idOption);
        }
        return false;
    }

    public boolean removeOption(int idOption) {
        if (this != null && this.isNotNullItem()) {
            return this.itemOptions.removeIf(op -> op != null && op.optionTemplate.id == idOption);
        }
        return false;
    }

//    List<Integer> item99s( Item item){
//        List<Integer> idOption = new ArrayList<>();
//        idOption.add(136);
//        
//        return idOption;
//    }
    public String getContent() {
        return "Yêu cầu sức mạnh " + this.template.strRequire + " trở lên";
    }

    public boolean canConsign() {
        for (ItemOption o : itemOptions) {
            int optionId = o.optionTemplate.id;
            if (optionId == 86 || optionId == 87) {
                return true;
            }
        }
        return false;
    }

    public boolean isDHD() {
        if (this.template.id >= 650 && this.template.id <= 662) {
            return true;
        }
        return false;
    }

    public String getOptionName() {
        return Util.replace("|4|" + this.template.name + "\n", "#", Util.getFormatNumber(this.param));
    }

    public void dispose() {
        this.template = null;
        this.info = null;
        this.content = null;
        if (this.itemOptions != null) {
            for (ItemOption io : this.itemOptions) {
                io.dispose();
            }
            this.itemOptions.clear();
        }
        this.itemOptions = null;
    }

    public short getId() {
        return template.id;
    }

    public byte getType() {
        return template.type;
    }

    public String getName() {
        return template.name;
    }

    public boolean isManhTS() {
        if (this.template.id >= 1066 && this.template.id <= 1070) {
            return true;
        }
        return false;
    }

    public boolean isdanangcapDoTs() {
        if (this.template.id >= 1074 && this.template.id <= 1078) {
            return true;
        }
        return false;
    }

    public boolean isDamayman() {
        if (this.template.id >= 1079 && this.template.id <= 1083) {
            return true;
        }
        return false;
    }

    public boolean isCongthucVip() {
        if (this.template.id >= 1084 && this.template.id <= 1086) {
            return true;
        }
        return false;
    }

    public boolean isCongthucNomal() {
        if (this.template.id >= 1071 && this.template.id <= 1073) {
            return true;
        }
        return false;
    }

    public byte typeIdManh() {
        if (!isManhTS()) {
            return -1;
        }
        switch (this.template.id) {
            case 1066:
                return 0;
            case 1067:
                return 1;
            case 1070:
                return 2;
            case 1068:
                return 3;
            case 1069:
                return 4;
            default:
                return -1;
        }
    }

}
