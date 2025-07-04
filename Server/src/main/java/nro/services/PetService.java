package nro.services;

import nro.consts.ConstPet;
import nro.consts.ConstPlayer;
import nro.models.player.Pet;
import nro.models.player.Player;
import nro.utils.SkillUtil;
import nro.utils.Util;

/**
 *
 * @author 💖 Trần Lại 💖
 * @copyright 💖 GirlkuN 💖
 *
 */
public class PetService {

    private static PetService i;

    public static PetService gI() {
        if (i == null) {
            i = new PetService();
        }
        return i;
    }

    public void removePet(Player pl) {
    if (pl.pet != null) {
        pl.pet.dispose(); // Giải phóng Pet cũ
        pl.pet = null;
    }
}

    
    
    public void createNormalPet(Player player, int gender, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet(player, ConstPet.PET, (byte) gender);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                    player.pet.nPoint.initPowerLimit();
                }
                Thread.sleep(1000);
                Service.getInstance().chatJustForMe(player, player.pet, "Xin hãy thu nhận làm đệ tử");
            } catch (Exception e) {
            }
        }).start();
    }

    public void createNormalPet(Player player, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet(player, ConstPet.PET);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                }
                Thread.sleep(1000);
                Service.getInstance().chatJustForMe(player, player.pet, "Xin hãy thu nhận làm đệ tử");
            } catch (Exception e) {
            }
        }).start();
    }

    public void createMabuPet(Player player, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet(player, ConstPet.MABU);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                    player.pet.nPoint.initPowerLimit();
                }
                Thread.sleep(1000);
                Service.getInstance().chatJustForMe(player, player.pet, "Oa oa oa...");
            } catch (Exception e) {
            }
        }).start();
    }

    public void createMabuPet(Player player, int gender, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet(player, ConstPet.MABU, (byte) gender);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                    player.pet.nPoint.initPowerLimit();
                }
                Thread.sleep(1000);
                Service.getInstance().chatJustForMe(player, player.pet, "Oa oa oa...");
            } catch (Exception e) {
            }
        }).start();
    }

    public void createGokuGoddPet(Player player, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet(player, ConstPet.GOKU_GODD);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                    player.pet.nPoint.initPowerLimit();
                }
                Thread.sleep(1000);
                Service.getInstance().chatJustForMe(player, player.pet, "Oa oa oa...");
            } catch (Exception e) {
            }
        }).start();
    }

    public void createGokuGoddPet(Player player, int gender, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet(player, ConstPet.GOKU_GODD, (byte) gender);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                    player.pet.nPoint.initPowerLimit();
                }
                Thread.sleep(1000);
                Service.getInstance().chatJustForMe(player, player.pet, "Oa oa oa...");
            } catch (Exception e) {
            }
        }).start();
    }

    public void createBatGioiPet(Player player, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet(player, ConstPet.BAT_GIOI);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                    player.pet.nPoint.initPowerLimit();
                }
                Thread.sleep(1000);
                Service.getInstance().chatJustForMe(player, player.pet, "Oa oa oa...");
            } catch (Exception e) {
            }
        }).start();
    }

    public void createBatGioiPet(Player player, int gender, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet(player, ConstPet.BAT_GIOI, (byte) gender);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                    player.pet.nPoint.initPowerLimit();
                }
                Thread.sleep(1000);
                Service.getInstance().chatJustForMe(player, player.pet, "Oa oa oa...");
            } catch (Exception e) {
            }
        }).start();
    }

    
      public void createbrolyPet(Player player, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet(player, ConstPet.BROLY);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                    player.pet.nPoint.initPowerLimit();
                }
                Thread.sleep(1000);
                Service.getInstance().chatJustForMe(player, player.pet, "Oa oa oa...");
            } catch (Exception e) {
            }
        }).start();
    }

    public void createbrolyPet(Player player, int gender, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet(player, ConstPet.BROLY, (byte) gender);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                    player.pet.nPoint.initPowerLimit();
                }
                Thread.sleep(1000);
                Service.getInstance().chatJustForMe(player, player.pet, "Oa oa oa...");
            } catch (Exception e) {
            }
        }).start();
    }
    
    
    
    public void createHangNgaPet(Player player, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet(player, ConstPet.HANG_NGA);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                    player.pet.nPoint.initPowerLimit();
                }
                Thread.sleep(1000);
                Service.getInstance().chatJustForMe(player, player.pet, "Oa oa oa...");
            } catch (Exception e) {
            }
        }).start();
    }

    public void createHangNgaPet(Player player, int gender, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet(player, ConstPet.HANG_NGA, (byte) gender);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                    player.pet.nPoint.initPowerLimit();
                }
                Thread.sleep(1000);
                Service.getInstance().chatJustForMe(player, player.pet, "Oa oa oa...");
            } catch (Exception e) {
            }
        }).start();
    }

    public void createVegitoPet(Player player, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet(player, ConstPet.VEGITO);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                    player.pet.nPoint.initPowerLimit();
                }
                Thread.sleep(1000);
                Service.getInstance().chatJustForMe(player, player.pet, "Oa oa oa...");
            } catch (Exception e) {
            }
        }).start();
    }

    public void createVegitoPet(Player player, int gender, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet(player, ConstPet.VEGITO, (byte) gender);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                    player.pet.nPoint.initPowerLimit();
                }
                Thread.sleep(1000);
                Service.getInstance().chatJustForMe(player, player.pet, "Oa oa oa...");
            } catch (Exception e) {
            }
        }).start();
    }
    public void createVegitoSSJPet(Player player, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet(player, ConstPet.VEGITO_SSJ);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                    player.pet.nPoint.initPowerLimit();
                }
                Thread.sleep(1000);
                Service.getInstance().chatJustForMe(player, player.pet, "Oa oa oa...");
            } catch (Exception e) {
            }
        }).start();
    }

    public void createVegitoSSJPet(Player player, int gender, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet(player, ConstPet.VEGITO_SSJ, (byte) gender);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                    player.pet.nPoint.initPowerLimit();
                }
                Thread.sleep(1000);
                Service.getInstance().chatJustForMe(player, player.pet, "Oa oa oa...");
            } catch (Exception e) {
            }
        }).start();
    }
    public void createGokuUltraPet(Player player, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet(player, ConstPet.GOKU_ULTRA);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                    player.pet.nPoint.initPowerLimit();
                }
                Thread.sleep(1000);
                Service.getInstance().chatJustForMe(player, player.pet, "Oa oa oa...");
            } catch (Exception e) {
            }
        }).start();
    }

    public void createGokuUltraPet(Player player, int gender, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet(player, ConstPet.GOKU_ULTRA, (byte) gender);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                    player.pet.nPoint.initPowerLimit();
                }
                Thread.sleep(1000);
                Service.getInstance().chatJustForMe(player, player.pet, "Oa oa oa...");
            } catch (Exception e) {
            }
        }).start();
    }

    public void createBerusPet(Player player, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet(player, ConstPet.BILL);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                }
                Thread.sleep(1000);
                Service.getInstance().chatJustForMe(player, player.pet, "Thần hủy diệt hiện thân tất cả quỳ xuống...");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void createBerusPet(Player player, int gender, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet(player, ConstPet.BILL, (byte) gender);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                }
                Thread.sleep(1000);
                Service.getInstance().chatJustForMe(player, player.pet, "Thần hủy diệt hiện thân tất cả quỳ xuống...");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void createVidelPet(Player player, int gender, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet(player, ConstPet.VIDEL, (byte) gender);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                }
                Thread.sleep(1000);
                Service.getInstance().chatJustForMe(player, player.pet, "Ta sẽ đem hạnh phúc đến Noel này...");
                Service.getInstance().sendThongBao(player, "Bạn đã nhận được Đệ Tử Videl");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void createGokuBluePet(Player player, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet(player, ConstPet.GOKU_BLUE);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                    player.pet.nPoint.initPowerLimit();
                }
                Thread.sleep(1000);
                Service.getInstance().chatJustForMe(player, player.pet, "Oa oa oa...");
            } catch (Exception e) {
            }
        }).start();
    }

    public void createGokuBluePet(Player player, int gender, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet(player, ConstPet.GOKU_BLUE, (byte) gender);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                    player.pet.nPoint.initPowerLimit();
                }
                Thread.sleep(1000);
                Service.getInstance().chatJustForMe(player, player.pet, "Oa oa oa...");
            } catch (Exception e) {
            }
        }).start();
    }

    public void createVegetaBluePet(Player player, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet(player, ConstPet.VEGETA_BLUE);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                    player.pet.nPoint.initPowerLimit();
                }
                Thread.sleep(1000);
                Service.getInstance().chatJustForMe(player, player.pet, "Oa oa oa...");
            } catch (Exception e) {
            }
        }).start();
    }

    public void createVegetaBluePet(Player player, int gender, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet(player, ConstPet.VEGETA_BLUE, (byte) gender);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                    player.pet.nPoint.initPowerLimit();
                }
                Thread.sleep(1000);
                Service.getInstance().chatJustForMe(player, player.pet, "Oa oa oa...");
            } catch (Exception e) {
            }
        }).start();
    }

    public void createGohanBluePet(Player player, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet(player, ConstPet.GOHAN_BLUE);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                    player.pet.nPoint.initPowerLimit();
                }
                Thread.sleep(1000);
                Service.getInstance().chatJustForMe(player, player.pet, "Oa oa oa...");
            } catch (Exception e) {
            }
        }).start();
    }

    public void createGohanBluePet(Player player, int gender, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet(player, ConstPet.GOHAN_BLUE, (byte) gender);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                    player.pet.nPoint.initPowerLimit();
                }
                Thread.sleep(1000);
                Service.getInstance().chatJustForMe(player, player.pet, "Oa oa oa...");
            } catch (Exception e) {
            }
        }).start();
    }

    public void createGokuGodPet(Player player, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet(player, ConstPet.GOKU_GOD);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                    player.pet.nPoint.initPowerLimit();
                }
                Thread.sleep(1000);
                Service.getInstance().chatJustForMe(player, player.pet, "Oa oa oa...");
            } catch (Exception e) {
            }
        }).start();
    }

    public void createGokuGodPet(Player player, int gender, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet(player, ConstPet.GOKU_GOD, (byte) gender);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                    player.pet.nPoint.initPowerLimit();
                }
                Thread.sleep(1000);
                Service.getInstance().chatJustForMe(player, player.pet, "Oa oa oa...");
            } catch (Exception e) {
            }
        }).start();
    }

    public void createVegetaGodPet(Player player, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet(player, ConstPet.VEGETA_GOD);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                    player.pet.nPoint.initPowerLimit();
                }
                Thread.sleep(1000);
                Service.getInstance().chatJustForMe(player, player.pet, "Oa oa oa...");
            } catch (Exception e) {
            }
        }).start();
    }

    public void createVegetaGodPet(Player player, int gender, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet(player, ConstPet.VEGETA_GOD, (byte) gender);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                    player.pet.nPoint.initPowerLimit();
                }
                Thread.sleep(1000);
                Service.getInstance().chatJustForMe(player, player.pet, "Oa oa oa...");
            } catch (Exception e) {
            }
        }).start();
    }

    public void createGohanGodPet(Player player, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet(player, ConstPet.GOHAN_GOD);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                    player.pet.nPoint.initPowerLimit();
                }
                Thread.sleep(1000);
                Service.getInstance().chatJustForMe(player, player.pet, "Oa oa oa...");
            } catch (Exception e) {
            }
        }).start();
    }

    public void createGohanGodPet(Player player, int gender, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet(player, ConstPet.GOHAN_GOD, (byte) gender);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                    player.pet.nPoint.initPowerLimit();
                }
                Thread.sleep(1000);
                Service.getInstance().chatJustForMe(player, player.pet, "Oa oa oa...");
            } catch (Exception e) {
            }
        }).start();
    }

    public void changeNormalPet(Player player, int gender) {
        byte limitPower = player.pet.nPoint.limitPower;
        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            player.pet.unFusion();
        }
        MapService.gI().exitMap(player.pet);
        player.pet.dispose();
        player.pet = null;
        createNormalPet(player, gender, limitPower);
    }

    public void changeNormalPet(Player player) {
        byte limitPower = player.pet.nPoint.limitPower;
        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            player.pet.unFusion();
        }
        MapService.gI().exitMap(player.pet);
        player.pet.dispose();
        player.pet = null;
        createNormalPet(player, limitPower);
    }

    public void changeMabuPet(Player player) {
        byte limitPower = player.pet.nPoint.limitPower;
        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            player.pet.unFusion();
        }
        MapService.gI().exitMap(player.pet);
        player.pet.dispose();
        player.pet = null;
        createMabuPet(player, limitPower);
    }

    public void changeMabuPet(Player player, int gender) {
        byte limitPower = player.pet.nPoint.limitPower;
        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            player.pet.unFusion();
        }
        MapService.gI().exitMap(player.pet);
        player.pet.dispose();
        player.pet = null;
        createMabuPet(player, gender, limitPower);
    }

    public void changeGokuGoddPet(Player player) {
        byte limitPower = player.pet.nPoint.limitPower;
        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            player.pet.unFusion();
        }
        MapService.gI().exitMap(player.pet);
        player.pet.dispose();
        player.pet = null;
        createGokuGoddPet(player, limitPower);
    }

    public void changeGokuGoddPet(Player player, int gender) {
        byte limitPower = player.pet.nPoint.limitPower;
        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            player.pet.unFusion();
        }
        MapService.gI().exitMap(player.pet);
        player.pet.dispose();
        player.pet = null;
        createGokuGoddPet(player, gender, limitPower);
    }

    public void changeBatGioiPet(Player player) {
        byte limitPower = player.pet.nPoint.limitPower;
        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            player.pet.unFusion();
        }
        MapService.gI().exitMap(player.pet);
        player.pet.dispose();
        player.pet = null;
        createBatGioiPet(player, limitPower);
    }

    public void changeBatGioiPet(Player player, int gender) {
        byte limitPower = player.pet.nPoint.limitPower;
        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            player.pet.unFusion();
        }
        MapService.gI().exitMap(player.pet);
        player.pet.dispose();
        player.pet = null;
        createBatGioiPet(player, gender, limitPower);
    }
    
      public void changebrolyPet(Player player) {
        byte limitPower = player.pet.nPoint.limitPower;
        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            player.pet.unFusion();
        }
        MapService.gI().exitMap(player.pet);
        player.pet.dispose();
        player.pet = null;
        createbrolyPet(player, limitPower);
    }

    public void changebrolyPet(Player player, int gender) {
        byte limitPower = player.pet.nPoint.limitPower;
        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            player.pet.unFusion();
        }
        MapService.gI().exitMap(player.pet);
        player.pet.dispose();
        player.pet = null;
        createbrolyPet(player, gender, limitPower);
    }
    
    
    
    
    
    
    

    public void changeHangNgaPet(Player player) {
        byte limitPower = player.pet.nPoint.limitPower;
        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            player.pet.unFusion();
        }
        MapService.gI().exitMap(player.pet);
        player.pet.dispose();
        player.pet = null;
        createHangNgaPet(player, limitPower);
    }

    public void changeHangNgaPet(Player player, int gender) {
        byte limitPower = player.pet.nPoint.limitPower;
        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            player.pet.unFusion();
        }
        MapService.gI().exitMap(player.pet);
        player.pet.dispose();
        player.pet = null;
        createHangNgaPet(player, gender, limitPower);
    }

    public void changeVegitoPet(Player player) {
        byte limitPower = player.pet.nPoint.limitPower;
        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            player.pet.unFusion();
        }
        MapService.gI().exitMap(player.pet);
        player.pet.dispose();
        player.pet = null;
        createVegitoPet(player, limitPower);
    }

    public void changeVegitoPet(Player player, int gender) {
        byte limitPower = player.pet.nPoint.limitPower;
        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            player.pet.unFusion();
        }
        MapService.gI().exitMap(player.pet);
        player.pet.dispose();
        player.pet = null;
        createVegitoPet(player, gender, limitPower);
    }
    public void changeVegitoSSJPet(Player player) {
        byte limitPower = player.pet.nPoint.limitPower;
        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            player.pet.unFusion();
        }
        MapService.gI().exitMap(player.pet);
        player.pet.dispose();
        player.pet = null;
        createVegitoSSJPet(player, limitPower);
    }

    public void changeVegitoSSJPet(Player player, int gender) {
        byte limitPower = player.pet.nPoint.limitPower;
        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            player.pet.unFusion();
        }
        MapService.gI().exitMap(player.pet);
        player.pet.dispose();
        player.pet = null;
        createVegitoSSJPet(player, gender, limitPower);
    }
    public void changeGokuUltraPet(Player player) {
        byte limitPower = player.pet.nPoint.limitPower;
        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            player.pet.unFusion();
        }
        MapService.gI().exitMap(player.pet);
        player.pet.dispose();
        player.pet = null;
        createGokuUltraPet(player, limitPower);
    }

    public void changeGokuUltraPet(Player player, int gender) {
        byte limitPower = player.pet.nPoint.limitPower;
        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            player.pet.unFusion();
        }
        MapService.gI().exitMap(player.pet);
        player.pet.dispose();
        player.pet = null;
        createGokuUltraPet(player, gender, limitPower);
    }

    public void changeBillPet(Player player, int gender) {
        byte limitPower = player.pet.nPoint.limitPower;
        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            player.pet.unFusion();
        }
        MapService.gI().exitMap(player.pet);
        player.pet.dispose();
        player.pet = null;
        createBerusPet(player, gender, limitPower);
    }

    public void changeVidelPet(Player player, int gender) {
        byte limitPower = player.pet.nPoint.limitPower;
        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            player.pet.unFusion();
        }
        MapService.gI().exitMap(player.pet);
        player.pet.dispose();
        player.pet = null;
        createVidelPet(player, gender, limitPower);
    }

    public void changeGokuBluePet(Player player, int gender) {
        byte limitPower = player.pet.nPoint.limitPower;
        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            player.pet.unFusion();
        }
        MapService.gI().exitMap(player.pet);
        player.pet.dispose();
        player.pet = null;
        createGokuBluePet(player, gender, limitPower);
    }

    public void changeVegetaBluePet(Player player, int gender) {
        byte limitPower = player.pet.nPoint.limitPower;
        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            player.pet.unFusion();
        }
        MapService.gI().exitMap(player.pet);
        player.pet.dispose();
        player.pet = null;
        createVegetaBluePet(player, gender, limitPower);
    }

    public void changeGohanBluePet(Player player, int gender) {
        byte limitPower = player.pet.nPoint.limitPower;
        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            player.pet.unFusion();
        }
        MapService.gI().exitMap(player.pet);
        player.pet.dispose();
        player.pet = null;
        createGohanBluePet(player, gender, limitPower);
    }

    public void changeGokuGodPet(Player player, int gender) {
        byte limitPower = player.pet.nPoint.limitPower;
        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            player.pet.unFusion();
        }
        MapService.gI().exitMap(player.pet);
        player.pet.dispose();
        player.pet = null;
        createGokuGodPet(player, gender, limitPower);
    }

    public void changeVegetaGodPet(Player player, int gender) {
        byte limitPower = player.pet.nPoint.limitPower;
        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            player.pet.unFusion();
        }
        MapService.gI().exitMap(player.pet);
        player.pet.dispose();
        player.pet = null;
        createVegetaGodPet(player, gender, limitPower);
    }

    public void changeGohanGodPet(Player player, int gender) {
        byte limitPower = player.pet.nPoint.limitPower;
        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            player.pet.unFusion();
        }
        MapService.gI().exitMap(player.pet);
        player.pet.dispose();
        player.pet = null;
        createGohanGodPet(player, gender, limitPower);
    }

    public void changeNamePet(Player player, String name) {
        if (!InventoryService.gI().existItemBag(player, 400)) {
            Service.getInstance().sendThongBao(player, "Bạn cần thẻ đặt tên đệ tử, mua tại Santa");
            return;
        } else if (Util.haveSpecialCharacter(name)) {
            Service.getInstance().sendThongBao(player, "Tên không được chứa ký tự đặc biệt");
            return;
        } else if (name.length() > 10) {
            Service.getInstance().sendThongBao(player, "Tên quá dài");
            return;
        }
        MapService.gI().exitMap(player.pet);
        player.pet.name = "$" + name.toLowerCase().trim();
        InventoryService.gI().subQuantityItemsBag(player, InventoryService.gI().findItemBagByTemp(player, 400), 1);
        new Thread(() -> {
            try {
                Thread.sleep(1000);
                Service.getInstance().chatJustForMe(player, player.pet, "Cảm ơn sư phụ đã đặt cho con tên " + name);
            } catch (Exception e) {
            }
        }).start();
    }

    private int[] getDataPetNormal() {
        int[] hpmp = {1700, 1800, 1900, 2000, 2100, 2200};
        int[] petData = new int[5];
        petData[0] = Util.nextInt(40, 105) * 20; //hp
        petData[1] = Util.nextInt(40, 105) * 20; //mp
        petData[2] = Util.nextInt(20, 45); //dame
        petData[3] = Util.nextInt(9, 50); //def
        petData[4] = Util.nextInt(0, 2); //crit
        return petData;
    }

    private int[] getDataPetMabu() {
        int[] hpmp = {1700, 1800, 1900, 2000, 2100, 2200};
        int[] petData = new int[5];
        petData[0] = Util.nextInt(40, 105) * 20; //hp
        petData[1] = Util.nextInt(40, 105) * 20; //mp
        petData[2] = Util.nextInt(50, 120); //dame
        petData[3] = Util.nextInt(9, 50); //def
        petData[4] = Util.nextInt(0, 2); //crit
        return petData;
    }

    private int[] getDataPetBill() {
        int[] hpmp = {1700, 1800, 1900, 2000, 2100, 2200};
        int[] petData = new int[5];
        petData[0] = Util.nextInt(40, 105) * 20; //hp
        petData[1] = Util.nextInt(40, 105) * 20; //mp
        petData[2] = Util.nextInt(50, 120); //dame
        petData[3] = Util.nextInt(9, 50); //def
        petData[4] = Util.nextInt(0, 2); //crit
        return petData;
    }

    private int[] getDataPetVidel() {
        int[] hpmp = {1700, 1800, 1900, 2000, 2100, 2200};
        int[] petData = new int[5];
        petData[0] = Util.nextInt(40, 105) * 20; //hp
        petData[1] = Util.nextInt(40, 105) * 20; //mp
        petData[2] = Util.nextInt(50, 120); //dame
        petData[3] = Util.nextInt(9, 50); //def
        petData[4] = Util.nextInt(0, 2); //crit
        return petData;
    }

    private int[] getDataGoKuGodd() {
        int[] hpmp = {1700, 1800, 1900, 2000, 2100, 2200};
        int[] petData = new int[5];
        petData[0] = Util.nextInt(40, 105) * 20; //hp
        petData[1] = Util.nextInt(40, 105) * 20; //mp
        petData[2] = Util.nextInt(50, 120); //dame
        petData[3] = Util.nextInt(9, 50); //def
        petData[4] = Util.nextInt(0, 2); //crit
        return petData;
    }

    private int[] getDataBatGioi() {
        int[] hpmp = {1700, 1800, 1900, 2000, 2100, 2200};
        int[] petData = new int[5];
        petData[0] = Util.nextInt(40, 105) * 20; //hp
        petData[1] = Util.nextInt(40, 105) * 20; //mp
        petData[2] = Util.nextInt(50, 120); //dame
        petData[3] = Util.nextInt(9, 50); //def
        petData[4] = Util.nextInt(0, 2); //crit
        return petData;
    }
    
    private int[] getDatabroly() {
        int[] hpmp = {50000, 100000, 50000, 150000, 50000, 100000};
        int[] petData = new int[5];
        petData[0] = Util.nextInt(4000, 105000); //hp
        petData[1] = Util.nextInt(4000, 105000); //mp
        petData[2] = Util.nextInt(5000, 12000); //dame
        petData[3] = Util.nextInt(90, 500); //def
        petData[4] = Util.nextInt(5, 20); //crit
        return petData;
    } 
    
    

    private int[] getDataHangNga() {
        int[] hpmp = {1700, 1800, 1900, 2000, 2100, 2200};
        int[] petData = new int[5];
        petData[0] = Util.nextInt(40, 105) * 20; //hp
        petData[1] = Util.nextInt(40, 105) * 20; //mp
        petData[2] = Util.nextInt(50, 120); //dame
        petData[3] = Util.nextInt(9, 50); //def
        petData[4] = Util.nextInt(0, 2); //crit
        return petData;
    }

    private int[] getDataVegito() {
        int[] hpmp = {1700, 1800, 1900, 2000, 2100, 2200};
        int[] petData = new int[5];
        petData[0] = Util.nextInt(40, 105) * 20; //hp
        petData[1] = Util.nextInt(40, 105) * 20; //mp
        petData[2] = Util.nextInt(50, 120); //dame
        petData[3] = Util.nextInt(9, 50); //def
        petData[4] = Util.nextInt(0, 2); //crit
        return petData;
    }
    private int[] getDataVegitoSSJ() {
        int[] hpmp = {1700, 1800, 1900, 2000, 2100, 2200};
        int[] petData = new int[5];
        petData[0] = Util.nextInt(40, 105) * 20; //hp
        petData[1] = Util.nextInt(40, 105) * 20; //mp
        petData[2] = Util.nextInt(50, 120); //dame
        petData[3] = Util.nextInt(9, 50); //def
        petData[4] = Util.nextInt(0, 2); //crit
        return petData;
    }
    private int[] getDataGokuUltra() {
        int[] hpmp = {1700, 1800, 1900, 2000, 2100, 2200};
        int[] petData = new int[5];
        petData[0] = Util.nextInt(40, 105) * 20; //hp
        petData[1] = Util.nextInt(40, 105) * 20; //mp
        petData[2] = Util.nextInt(50, 120); //dame
        petData[3] = Util.nextInt(9, 50); //def
        petData[4] = Util.nextInt(0, 2); //crit
        return petData;
    }

    private void createNewPet(Player player, byte typePet, byte... gender) {
        int[] data = new int[0];
        Pet pet = new Pet(player);

        pet.nPoint.power = 15000000;

        switch (typePet) {
            case ConstPet.PET: {
                data = getDataPetNormal();
                pet.name = "$Đệ tử";
                pet.nPoint.power = 2000;
                pet.typePet = ConstPet.PET;
                break;
            }
            case ConstPet.MABU: {
                data = getDataPetMabu();
                pet.name = "$Mabư";
                pet.typePet = ConstPet.MABU;
                break;
            }
            case ConstPet.BILL: {
                data = getDataPetMabu();
                pet.name = "$Bill";
                pet.typePet = ConstPet.BILL;
                break;
            }
            case ConstPet.VIDEL: {
                data = getDataPetVidel();
                pet.name = "$Videl";
                pet.typePet = ConstPet.VIDEL;
                break;
            }
            case ConstPet.GOKU_BLUE: {
                data = getDataPetVidel();
                pet.name = "$Goku SSJ Blue";
                pet.typePet = ConstPet.GOKU_BLUE;
                break;
            }
            case ConstPet.VEGETA_BLUE: {
                data = getDataPetVidel();
                pet.name = "$Vegeta SSJ Blue";
                pet.typePet = ConstPet.VEGETA_BLUE;
                break;
            }
            case ConstPet.GOHAN_BLUE: {
                data = getDataPetVidel();
                pet.name = "$Gohan SSJ Blue";
                pet.typePet = ConstPet.GOHAN_BLUE;
                break;
            }
            case ConstPet.GOKU_GOD: {
                data = getDataPetVidel();
                pet.name = "$Gohan SSJ God";
                pet.typePet = ConstPet.GOKU_GOD;
                break;
            }
            case ConstPet.VEGETA_GOD: {
                data = getDataPetVidel();
                pet.name = "$Gohan SSJ God";
                pet.typePet = ConstPet.VEGETA_GOD;
                break;
            }
            case ConstPet.GOHAN_GOD: {
                data = getDataPetVidel();
                pet.name = "$Gohan SSJ God";
                pet.typePet = ConstPet.GOHAN_GOD;
                break;
            }
            case ConstPet.GOKU_GODD: {
                data = getDataGoKuGodd();
                pet.name = "$Goku God";
                pet.typePet = ConstPet.GOKU_GODD;
                break;
            }
            case ConstPet.BAT_GIOI: {
                data = getDataBatGioi();
                pet.name = "$Bát Giới";
                pet.typePet = ConstPet.BAT_GIOI;
                break;
            }
            
              case ConstPet.BROLY: {
                data = getDatabroly();
                pet.name = "$Đệ Broly";
                pet.typePet = ConstPet.BROLY;
                break;
            }
            
            case ConstPet.HANG_NGA: {
                data = getDataHangNga();
                pet.name = "$Hằng Nga";
                pet.typePet = ConstPet.HANG_NGA;
                break;
            }
            case ConstPet.VEGITO: {
                data = getDataVegito();
                pet.name = "$Vegito";
                pet.typePet = ConstPet.VEGITO;
                break;
            }
            case ConstPet.VEGITO_SSJ: {
                data = getDataVegitoSSJ();
                pet.name = "$Vegito SSJ White";
                pet.typePet = ConstPet.VEGITO_SSJ;
                break;
            }
            case ConstPet.GOKU_ULTRA: {
                data = getDataGokuUltra();
                pet.name = "$Goku Ultratic";
                pet.typePet = ConstPet.GOKU_ULTRA;
                break;
            }
        }

        pet.gender = (gender != null && gender.length != 0) ? gender[0] : (byte) Util.nextInt(0, 2);
        pet.id = -player.id;
        pet.nPoint.stamina = 1000;
        pet.nPoint.maxStamina = 1000;
        pet.nPoint.hpg = data[0];
        pet.nPoint.mpg = data[1];
        pet.nPoint.dameg = data[2];
        pet.nPoint.defg = data[3];
        pet.nPoint.critg = data[4];
        for (int i = 0; i < 16; i++) {
            pet.inventory.itemsBody.add(ItemService.gI().createItemNull());// pet thì doạn này thế th Mèo mở unity lên đi Trường sửa cái index đệ
        }
        pet.playerSkill.skills.add(SkillUtil.createSkill(Util.nextInt(0, 2) * 2, 1));
        for (int i = 0; i < 6; i++) {
            pet.playerSkill.skills.add(SkillUtil.createEmptySkill());
        }
        pet.nPoint.calPoint();
        pet.nPoint.setFullHpMp();
        player.pet = pet;
    }

    //--------------------------------------------------------------------------
}
