package nro.models.Bot;

import java.util.Comparator;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;
import nro.consts.ConstPlayer;
import nro.models.boss.Boss;
import nro.models.boss.BossFactory;
import nro.models.mob.Mob;
import nro.models.skill.Skill;
import nro.services.MapService;
import nro.services.PlayerService;
import nro.services.SkillService;
import nro.services.func.ChangeMapService;
import nro.utils.Util;

public class Sanb {

    public Bot bot;

    public Boss bossAttack;

    public long lastTimeSkill1;

    public Sanb(Bot b) {
        this.bot = b;
    }

    public void update() {
        this.SanBot();
    }

    public boolean isMap(int mapId) {
        return (MapService.gI().isMapDoanhTrai(mapId) || MapService.gI().isMapBlackBallWar(mapId)
                || MapService.gI().isMapBanDoKhoBau(mapId) || MapService.gI().isMapMabuWar(mapId)
                || MapService.gI().isMapMabu14h(mapId));
    }

    public void GetBoss(int status) {
        if (this.bossAttack == null || this.bossAttack.isDie()) {
            List<Boss> bosses = BossFactory.gI().getBosses();

            if (bosses.isEmpty()) {
                return;
            }

            this.bossAttack = bosses.get(new Random().nextInt(bosses.size()));

            boolean bosAction = (!this.bossAttack.isDie()
                    && !this.isMap(this.bossAttack.zone.map.mapId)
                    && !this.bossAttack.zone.isFullPlayer1()
                    && this.bossAttack.zone.mobs.size() >= 1);

            if (bosAction) {
                ChangeMapService.gI().goToMap(this.bot, this.bossAttack.zone);
                this.bot.zone.load_Me_To_Another(this.bot);
            } else {
                if (status < 2) {
                    this.bossAttack = null;
                    this.GetBoss(status + 1);
                } else {
                    BotManager.gI().bot.remove(this.bot);
                    ChangeMapService.gI().exitMap(this.bot);
                    this.bossAttack = null;
                }
            }
        }
    }

    public void GetSkil() {
        if (Util.isTrue(50, 100)) {
            this.bot.playerSkill.skillSelect = this.bot.playerSkill.skills.get(0);
        } else {
            this.bot.playerSkill.skillSelect = this.bot.playerSkill.skills.get(1);
        }

        if (this.lastTimeSkill1 < System.currentTimeMillis() - 50000) {
            switch (this.bot.gender) {
                case ConstPlayer.XAYDA:
                    this.bot.useSkill(Skill.BIEN_KHI);
                    break;
                case ConstPlayer.TRAI_DAT:
                    this.bot.useSkill(Skill.QUA_CAU_KENH_KHI);
                    break;
                case ConstPlayer.NAMEC:
                    this.bot.useSkill(Skill.MAKANKOSAPPO);
                    break;
            }
            this.lastTimeSkill1 = System.currentTimeMillis() - new Random().nextInt(150000);
        }
    }

    public void SanBot() {
        this.GetBoss(0);
        this.GetSkil();

        if (this.bossAttack != null && !this.bossAttack.isDie()) {
            if (this.bot.UseLastTimeSkill()) {
                int y = 0;
                int x = 0;

                for (Mob m : this.bot.zone.mobs) {
                    if (y < m.location.y) {
                        y = m.location.y;
                        x = m.location.x;
                    }
                }

                if (this.bot.zone.map.mapId == 72) {
                    y = 312;
                }

                PlayerService.gI().playerMove(this.bot, this.bossAttack.location.x, this.bossAttack.location.y);

                SkillService.gI().useSkill(this.bot, this.bossAttack, null);
            }
        }
    }
}
