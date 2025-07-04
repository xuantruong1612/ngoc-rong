package nro.models.player;

import nro.models.mob.Mob;
import nro.models.skill.Skill;
import nro.services.*;
import nro.services.func.ChangeMapService;
import nro.utils.SkillUtil;
import nro.utils.Util;

/**
 * dev by TS
 */
public class Clone extends Player {
    
    public Player mainPl;
    public long lastTimeRevival;

    public Clone(Player plMaster) {
        this.mainPl = plMaster;
        this.isClone = true;
        this.id = -plMaster.id * 2 + Util.nextInt(1,100000);
        this.lastTimeRevival = System.currentTimeMillis() + (1000 * 60 * plMaster.playerSkill.getSkillbyId(Skill.PHAN_THAN).point);
    }

    @Override
    public short getHead() {
        return mainPl.getHead();
    }

    @Override
    public short getBody() {
        return mainPl.getBody();
    }

    @Override
    public short getLeg() {
        return mainPl.getLeg();
    }
    
    @Override
    public int version() {
        return 214;
    }

    public void joinMapMaster() {
        if (!isDie() && !mainPl.isDie()) {
            this.location.x = mainPl.location.x + Util.nextInt(-10, 10);
            this.location.y = mainPl.location.y;
            MapService.gI().goToMap(this, mainPl.zone);
            this.zone.load_Me_To_Another(this);
        }
    }
        
    @Override
    public void update() {
        super.update();
        try {
            if (mainPl != null  && mainPl.zone == null) {
                ChangeMapService.gI().exitMap(this);
                mainPl.clone.dispose();
                mainPl.clone = null;
            }
            if (this.isDie()) {
                ChangeMapService.gI().exitMap(this);
            }
            if (mainPl != null && (this.zone == null || this.zone != mainPl.zone)) {
                joinMapMaster();
            }
            if (mainPl != null && mainPl.isDie() || effectSkill.isHaveEffectSkill()) {
                return;
            }
            if (Util.canDoWithTime(this.lastTimeRevival, 1000)) {
                ChangeMapService.gI().exitMap(this);
                this.dispose();
            }
            if (mainPl != null) {
                followMaster(60);
            }
        } catch (Exception e) {
        }
    }

    public void attackWithMaster(Player plAtt, Mob mAtt) {
        if (plAtt != null) {
            if (SkillUtil.isUseSkillDam(this)) {
                PlayerService.gI().playerMove(this, plAtt.location.x + Util.nextInt(-60, 60),plAtt.location.y);
            }
            SkillService.gI().useSkill(this, plAtt, null, null);
        } else if (mAtt != null) {
            if (SkillUtil.isUseSkillDam(this)) {
                PlayerService.gI().playerMove(this, mAtt.location.x + Util.nextInt(-60, 60),mAtt.location.y);
            }
            SkillService.gI().useSkill(this, null, mAtt, null);
        }
    }
    
    public void followMaster() {
        if (mainPl == null || this.isDie() || effectSkill.isHaveEffectSkill()) {
            return;
        }
        followMaster(60);
    }

    private void followMaster(int dis) {
        int mX = mainPl.location.x;
        int mY = mainPl.location.y;
        int disX = this.location.x - mX;
        if (Math.sqrt(Math.pow(mX - this.location.x, 2) + Math.pow(mY - this.location.y, 2)) >= dis) {
            if (disX < 0) {
                this.location.x = mX - Util.nextInt(0, dis);
            } else {
                this.location.x = mX + Util.nextInt(0, dis);
            }
            this.location.y = mY;
            PlayerService.gI().playerMove(this, this.location.x, this.location.y);
        }
    }
    

    
    
    
    public static void callPoint(Player me) {
       if (me.clone != null) {
           ChangeMapService.gI().exitMap(me.clone); 
          
       } 
        Clone clone = new Clone(me);
        clone.name = "Phân Thân [" + me.name + "]"; 
        clone.gender = me.gender;
        clone.nPoint.hpg = me.nPoint.hpg;
        clone.nPoint.mpg = me.nPoint.mpg;
        clone.nPoint.hp = 1000000000000l;
        clone.nPoint.mp = Long.MAX_VALUE;
        clone.nPoint.dameg = 100000;
        clone.nPoint.defg = 100000;
        clone.nPoint.critg = 0;
        clone.nPoint.crit = 0;
        clone.nPoint.stamina = me.nPoint.stamina;
        clone.nPoint.maxStamina = me.nPoint.maxStamina;
        clone.inventory = me.inventory;
        clone.playerSkill.skills = me.playerSkill.skills;
        clone.nPoint.calPoint();
        clone.nPoint.setFullHpMp();
        me.clone = clone;
    }

        
    @Override
    public void dispose() {
        if (zone != null) {
            ChangeMapService.gI().exitMap(this);
        }
        this.zone = null;
        this.mainPl.clone = null;
        this.mainPl = null;
    }
}
