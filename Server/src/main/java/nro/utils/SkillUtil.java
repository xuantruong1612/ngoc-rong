package nro.utils;

import nro.models.skill.SkillTemplate;
import nro.models.player.Player;
import nro.models.skill.NClass;
import nro.models.skill.Skill;
import nro.models.skill.SkillNotFocus;
import nro.server.Manager;

import java.util.List;

/**
 * Build Arriety
 */
public class SkillUtil {

    private final static NClass nClassTD;
    private final static NClass nClassNM;
    private final static NClass nClassXD;

    static {
        nClassTD = Manager.NCLASS.get(0);
        nClassNM = Manager.NCLASS.get(1);
        nClassXD = Manager.NCLASS.get(2);
    }

    public static Skill getSkill(int tempId, int level) {
        Skill skill = null;
        try {
            skill = nClassTD.getSkillTemplate(tempId).skillss.get(level - 1);
        } catch (Exception e) {
            try {
                skill = nClassNM.getSkillTemplate(tempId).skillss.get(level - 1);
            } catch (Exception ex) {
                skill = nClassXD.getSkillTemplate(tempId).skillss.get(level - 1);
            }
        }
        return skill;
    }

    public static Skill createSkill(int tempId, int level) {
        Skill skill = null;
        try {
            skill = nClassTD.getSkillTemplate(tempId).skillss.get(level - 1);
        } catch (Exception e) {
            try {
                skill = nClassNM.getSkillTemplate(tempId).skillss.get(level - 1);
            } catch (Exception ex) {
                skill = nClassXD.getSkillTemplate(tempId).skillss.get(level - 1);
            }
        }
        if (skill.template.id >= 24) {
            return new SkillNotFocus(skill);
        }
        return new Skill(skill);
    }

    public static Skill createEmptySkill() {
        Skill skill = new Skill();
        skill.skillId = -1;
        return skill;
    }

    public static Skill createSkillLevel0(int tempId) {
        Skill skill = createEmptySkill();
        skill.template = new SkillTemplate();
        skill.template.id = (byte) tempId;
        return skill;
    }

    public static boolean isUseSkillDam(Player player) {
        try {
            int skillId = player.playerSkill.skillSelect.template.id;
            return (skillId == Skill.DRAGON || skillId == Skill.DEMON
                    || skillId == Skill.GALICK || skillId == Skill.KAIOKEN
                    || skillId == Skill.LIEN_HOAN);
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isUseSkillChuong(Player player) {
        try {
            int skillId = player.playerSkill.skillSelect.template.id;
            return (skillId == Skill.KAMEJOKO || skillId == Skill.MASENKO || skillId == Skill.ANTOMIC);
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isUseSkillBoom(Player player) {
        try {
            int skillId = player.playerSkill.skillSelect.template.id;
            return skillId == Skill.TU_SAT;
        } catch (Exception e) {
            return false;
        }
    }

    public static int getTimeBinh(int level) { //thời gian hóa bình ( Ma Fu Ba )
        return 10000;
    }

    public static int getTimeMonkey(int level) { //thời gian tồn tại khỉ v
        return (level + 5) * 10000;
    }

    public static int getPercentHpMonkey(int level) { //tỉ lệ máu khỉ cộng thêm v
        return (level + 3) * 10;
    }

    public static int getTimeStun(int level) { //thời gian choáng thái dương hạ san v
        return (level + 2) * 1000;
    }

    public static int getTimeSocola() {
        return 30000;
    }

    public static int getTimeShield(int level) { //thời gian tồn tại khiên v
        return (level + 2) * 5000;
//        return (level + 2) * 1000;
    }

    public static int getTimeTroi(int level) { //thời gian trói v
//        return 10000;
        return level * 5000;
    }

    public static int getTimeDCTT(int level) { //thời gian choáng dịch chuyển tức thời v
        return (level + 1) * 500;
    }

    public static int getTimeThoiMien(int level) { //thời gian thôi miên
        return (level + 4) * 1000;
    }

    public static int getRangeStun(int level) { //phạm vi thái dương hạ san
        return 120 + level * 30;
    }

    public static int getRangeBom(int level) { //phạm vi tự sát
        return 400 + level * 30;
    }

    public static int getRangeQCKK(int level) { //phạm vi quả cầu kênh khi
        return 350 + level * 30;
    }

    public static int getPercentHPHuytSao(int level) { //tỉ lệ máu huýt sáo cộng thêm v
        return (level + 3) * 10;
    }

    public static int getPercentTriThuong(int level) { //tỉ lệ máu hồi phục trị thương v
        return (level + 9) * 5;
    }

    public static int getPercentCharge(int level) { //tỉ lệ hp ttnl
        return level + 3;
    }

    public static int getTempMobMe(int level) { //template đẻ trứng
        int[] temp = {8, 11, 32, 25, 43, 49, 50};
        return temp[level - 1];
    }

    public static int getTimeSurviveMobMe(int level) { //thời gian trứng tồn tại
        return getTimeMonkey(level) * 2;
    }

    public static int getTimeTranformation() { //thời gian tồn tại khỉ v
        return 300000;
    }

    public static int getTimett() {
        return 300000;
    }

    public static int getHPMobMe(double hpMaxPlayer, int level) { //lấy hp max của đệ trứng theo hp max player
        int[] perHPs = {30, 40, 50, 60, 70, 80, 90};
        return (int) (hpMaxPlayer * perHPs[level - 1] / 100);
    }

    public static Skill getSkillbyId(Player player, int id) {
        for (Skill skill : player.playerSkill.skills) {
            if (skill.template.id == id) {
                return skill;
            }
        }
        return null;
    }

    public static SkillNotFocus findSkillNotFocus(Player player) {
        for (Skill skill : player.playerSkill.skills) {
            if (skill instanceof SkillNotFocus) {
                return (SkillNotFocus) skill;
            }
        }
        return null;
    }

    public static boolean upSkillPet(List<Skill> skills, int index) {
        int tempId = skills.get(index).template.id;
        int level = skills.get(index).point + 1;
        if (level > 7) {
            return false;
        }
        Skill skill = null;
        try {
            skill = nClassTD.getSkillTemplate(tempId).skillss.get(level - 1);
        } catch (Exception e) {
            try {
                skill = nClassNM.getSkillTemplate(tempId).skillss.get(level - 1);
            } catch (Exception ex) {
                skill = nClassXD.getSkillTemplate(tempId).skillss.get(level - 1);
            }
        }
        skill = new Skill(skill);
        if (index == 1) {
            skill.coolDown = 1000;
        }
        skills.set(index, skill);
        return true;
    }

    public static byte getTempSkillSkillByItemID(int id) {
        if (id >= 66 && id <= 72) {
            return Skill.DRAGON;
        } else if (id >= 79 && id <= 84 || id == 86) {
            return Skill.DEMON;
        } else if (id >= 87 && id <= 93) {
            return Skill.GALICK;
        } else if (id >= 94 && id <= 100) {
            return Skill.KAMEJOKO;
        } else if (id >= 101 && id <= 107) {
            return Skill.MASENKO;
        } else if (id >= 108 && id <= 114) {
            return Skill.ANTOMIC;
        } else if (id >= 115 && id <= 121) {
            return Skill.THAI_DUONG_HA_SAN;
        } else if (id >= 122 && id <= 128) {
            return Skill.TRI_THUONG;
        } else if (id >= 129 && id <= 135) {
            return Skill.TAI_TAO_NANG_LUONG;
        } else if (id >= 300 && id <= 306) {
            return Skill.KAIOKEN;
        } else if (id >= 307 && id <= 313) {
            return Skill.QUA_CAU_KENH_KHI;
        } else if (id >= 314 && id <= 320) {
            return Skill.BIEN_KHI;
        } else if (id >= 321 && id <= 327) {
            return Skill.TU_SAT;
        } else if (id >= 328 && id <= 334) {
            return Skill.MAKANKOSAPPO;
        } else if (id >= 335 && id <= 341) {
            return Skill.DE_TRUNG;
        } else if (id >= 434 && id <= 440) {
            return Skill.KHIEN_NANG_LUONG;
        } else if (id >= 474 && id <= 480) {
            return Skill.SOCOLA;
        } else if (id >= 481 && id <= 487) {
            return Skill.LIEN_HOAN;
        } else if (id >= 488 && id <= 494) {
            return Skill.DICH_CHUYEN_TUC_THOI;
        } else if (id >= 495 && id <= 501) {
            return Skill.THOI_MIEN;
        } else if (id >= 502 && id <= 508) {
            return Skill.TROI;
        } else if (id >= 509 && id <= 515) {
            return Skill.HUYT_SAO;
        } else if (id >= 1426 && id <= 1432) {
            return Skill.SUPER_KAME;
        } else if (id >= 1433 && id <= 1439) {
            return Skill.SUPER_ANTOMIC;
        } else if (id >= 1440 && id <= 1446) {
            return Skill.MAFUBA;
        } else if (id >= 1555 && id <= 1560) {
            return Skill.SUPER_TRANFORMATION;
        } else if (id >= 1561 && id <= 1566) {
            return Skill.SUPER_TRANFORMATION;
        } else if (id >= 1567 && id <= 1572) {
            return Skill.SUPER_TRANFORMATION;
        } else if (id == 1573) {
            return Skill.EVOLUTION;
        } else if (id >= 1628 && id <= 1634) {
            return Skill.PHAN_THAN;
        } else {
            return -1;
        }
    }

    public static Skill getSkillByItemID(Player pl, int tempId) {
        if (tempId >= 66 && tempId <= 72) {
            return getSkillbyId(pl, Skill.DRAGON);
        } else if (tempId >= 79 && tempId <= 84 || tempId == 86) {
            return getSkillbyId(pl, Skill.DEMON);
        } else if (tempId >= 87 && tempId <= 93) {
            return getSkillbyId(pl, Skill.GALICK);
        } else if (tempId >= 94 && tempId <= 100) {
            return getSkillbyId(pl, Skill.KAMEJOKO);
        } else if (tempId >= 101 && tempId <= 107) {
            return getSkillbyId(pl, Skill.MASENKO);
        } else if (tempId >= 108 && tempId <= 114) {
            return getSkillbyId(pl, Skill.ANTOMIC);
        } else if (tempId >= 115 && tempId <= 121) {
            return getSkillbyId(pl, Skill.THAI_DUONG_HA_SAN);
        } else if (tempId >= 122 && tempId <= 128) {
            return getSkillbyId(pl, Skill.TRI_THUONG);
        } else if (tempId >= 129 && tempId <= 135) {
            return getSkillbyId(pl, Skill.TAI_TAO_NANG_LUONG);
        } else if (tempId >= 300 && tempId <= 306) {
            return getSkillbyId(pl, Skill.KAIOKEN);
        } else if (tempId >= 307 && tempId <= 313) {
            return getSkillbyId(pl, Skill.QUA_CAU_KENH_KHI);
        } else if (tempId >= 314 && tempId <= 320) {
            return getSkillbyId(pl, Skill.BIEN_KHI);
        } else if (tempId >= 321 && tempId <= 327) {
            return getSkillbyId(pl, Skill.TU_SAT);
        } else if (tempId >= 328 && tempId <= 334) {
            return getSkillbyId(pl, Skill.MAKANKOSAPPO);
        } else if (tempId >= 335 && tempId <= 341) {
            return getSkillbyId(pl, Skill.DE_TRUNG);
        } else if (tempId >= 434 && tempId <= 440) {
            return getSkillbyId(pl, Skill.KHIEN_NANG_LUONG);
        } else if (tempId >= 474 && tempId <= 480) {
            return getSkillbyId(pl, Skill.SOCOLA);
        } else if (tempId >= 481 && tempId <= 487) {
            return getSkillbyId(pl, Skill.LIEN_HOAN);
        } else if (tempId >= 488 && tempId <= 494) {
            return getSkillbyId(pl, Skill.DICH_CHUYEN_TUC_THOI);
        } else if (tempId >= 495 && tempId <= 501) {
            return getSkillbyId(pl, Skill.THOI_MIEN);
        } else if (tempId >= 502 && tempId <= 508) {
            return getSkillbyId(pl, Skill.TROI);
        } else if (tempId >= 509 && tempId <= 515) {
            return getSkillbyId(pl, Skill.HUYT_SAO);
        } else if (tempId >= 1426 && tempId <= 1432) {
            return getSkillbyId(pl, Skill.SUPER_KAME);
        } else if (tempId >= 1433 && tempId <= 1439) {
            return getSkillbyId(pl, Skill.SUPER_ANTOMIC);
        } else if (tempId >= 1440 && tempId <= 1446) {
            return getSkillbyId(pl, Skill.MAFUBA);
        } else if (tempId >= 1555 && tempId <= 1560) {
            return getSkillbyId(pl, Skill.SUPER_TRANFORMATION);
        } else if (tempId >= 1561 && tempId <= 1566) {
            return getSkillbyId(pl, Skill.SUPER_TRANFORMATION);
        } else if (tempId >= 1567 && tempId <= 1572) {
            return getSkillbyId(pl, Skill.SUPER_TRANFORMATION);
        } else if (tempId == 1573) {
            return getSkillbyId(pl, Skill.EVOLUTION);
        } else if (tempId >= 1628 && tempId <= 1634) {
            return getSkillbyId(pl, Skill.PHAN_THAN);
        } else {
            return null;
        }
    }

    public static Skill getSkillByLevel(int tempId, byte level) {
        if (tempId >= 66 && tempId <= 72) {
            return getSkill(Skill.DRAGON, level);
        } else if (tempId >= 79 && tempId <= 84 || tempId == 86) {
            return getSkill(Skill.DEMON, level);
        } else if (tempId >= 87 && tempId <= 93) {
            return getSkill(Skill.GALICK, level);
        } else if (tempId >= 94 && tempId <= 100) {
            return getSkill(Skill.KAMEJOKO, level);
        } else if (tempId >= 101 && tempId <= 107) {
            return getSkill(Skill.MASENKO, level);
        } else if (tempId >= 108 && tempId <= 114) {
            return getSkill(Skill.ANTOMIC, level);
        } else if (tempId >= 115 && tempId <= 121) {
            return getSkill(Skill.THAI_DUONG_HA_SAN, level);
        } else if (tempId >= 122 && tempId <= 128) {
            return getSkill(Skill.TRI_THUONG, level);
        } else if (tempId >= 129 && tempId <= 135) {
            return getSkill(Skill.TAI_TAO_NANG_LUONG, level);
        } else if (tempId >= 300 && tempId <= 306) {
            return getSkill(Skill.KAIOKEN, level);
        } else if (tempId >= 307 && tempId <= 313) {
            return getSkill(Skill.QUA_CAU_KENH_KHI, level);
        } else if (tempId >= 314 && tempId <= 320) {
            return getSkill(Skill.BIEN_KHI, level);
        } else if (tempId >= 321 && tempId <= 327) {
            return getSkill(Skill.TU_SAT, level);
        } else if (tempId >= 328 && tempId <= 334) {
            return getSkill(Skill.MAKANKOSAPPO, level);
        } else if (tempId >= 335 && tempId <= 341) {
            return getSkill(Skill.DE_TRUNG, level);
        } else if (tempId >= 434 && tempId <= 440) {
            return getSkill(Skill.KHIEN_NANG_LUONG, level);
        } else if (tempId >= 474 && tempId <= 480) {
            return getSkill(Skill.SOCOLA, level);
        } else if (tempId >= 481 && tempId <= 487) {
            return getSkill(Skill.LIEN_HOAN, level);
        } else if (tempId >= 488 && tempId <= 494) {
            return getSkill(Skill.DICH_CHUYEN_TUC_THOI, level);
        } else if (tempId >= 495 && tempId <= 501) {
            return getSkill(Skill.THOI_MIEN, level);
        } else if (tempId >= 502 && tempId <= 508) {
            return getSkill(Skill.TROI, level);
        } else if (tempId >= 509 && tempId <= 515) {
            return getSkill(Skill.HUYT_SAO, level);
        } else if (tempId >= 1433 && tempId <= 1439) {
            return getSkill(Skill.SUPER_KAME, level);
        } else if (tempId >= 1433 && tempId <= 1439) {
            return getSkill(Skill.SUPER_ANTOMIC, level);
        } else if (tempId >= 1440 && tempId <= 1446) {
            return getSkill(Skill.MAFUBA, level);
        } else if (tempId >= 1555 && tempId <= 1560) {
            return getSkill(Skill.SUPER_TRANFORMATION, level);
        } else if (tempId >= 1561 && tempId <= 1566) {
            return getSkill(Skill.SUPER_TRANFORMATION, level);
        } else if (tempId >= 1567 && tempId <= 1572) {
            return getSkill(Skill.SUPER_TRANFORMATION, level);
        } else if (tempId == 1573) {
            return getSkill(Skill.EVOLUTION, level);
        } else if (tempId >= 1588 && tempId <= 1594) {
            return getSkill(Skill.PHAN_THAN, level);
        } else {
            return null;
        }
    }

    public static void setSkill(Player pl, Skill skill) {
        for (int i = 0; i < pl.playerSkill.skills.size(); i++) {
            if (pl.playerSkill.skills.get(i).template.id == skill.template.id) {
                pl.playerSkill.skills.set(i, skill);
                break;
            }
        }
    }
     public static int getPercentDameMonkey(byte levelMonkey) {
        return levelMonkey + 3;
    }

    public static byte getTyleSkillAttack(Skill skill) {
        switch (skill.template.id) {
            case Skill.TRI_THUONG:
                return 2;
            case Skill.KAMEJOKO:
            case Skill.MASENKO:
            case Skill.ANTOMIC:
                return 1;
            default:
                return 0;
        }
    }
    

}
