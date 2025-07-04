/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nro.models.mob;

/**
 *
 * @Build by Arriety
 */
public class MobFactory {

    public static Mob newMob(Mob mob) {
        int templateID = mob.tempId;
        switch (templateID) {
            case 72:
                return new GuardRobot(mob);
            case 71:
                return new Octopus(mob);
            case 70:
                return new Hirudegarn(mob);
            default:
                return new Mob(mob);
        }
    }
}
