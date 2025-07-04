package nro.models.Bot;

import java.util.Random;
import java.util.ArrayList;
import java.util.List;
import nro.models.item.Item;
import nro.models.map.Zone;
import nro.models.player.Player;
import nro.services.ChatGlobalService;
import nro.services.ItemService;
import nro.services.PlayerService;
import nro.services.Service;
import nro.services.func.ChangeMapService;
import nro.services.func.Trade;

public class ShopBot {
   public int idItem;
   public int idItTd;
   public int slot;
   
   private long lastimeChat;
   private long lastimeChatTrain;
   private Trade trade;
   
   public Bot bot;
   private Player pl;
   
   public ShopBot(int item , int traodoi , int slot){
        this.idItem = item;
        this.idItTd = traodoi;
        this.slot = slot;
   }
   
   public ShopBot(ShopBot shop){
        this.idItem = shop.idItem;
        this.idItTd = shop.idItTd;
        this.slot = shop.slot;
   }
   
   public void update(){
       this.mapL();
       this.chat();
   }
   
   public String getChat(){
        Item it = ItemService.gI().createNewItem((short) this.idItem);
        Item it1 = ItemService.gI().createNewItem((short) this.idItTd);
        String text = String.format("ủng Hộ ad Bán %s giá x%d %s/1 %s k%d Lh admin Nhé", it.template.name , this.slot , it1.template.name , this.bot.zone.map.mapName , this.bot.zone.zoneId);
        return text;
   }
   
   public void chat(){
       if (this.lastimeChat < (System.currentTimeMillis() - ((100 + new Random().nextInt(100)) * 1000))){
       ChatGlobalService.gI().chat1(this.bot , this.getChat());
       this.lastimeChat = System.currentTimeMillis();
      }
     if (this.lastimeChatTrain < (System.currentTimeMillis() - ((5 + new Random().nextInt(5)) * 1000))){
      Service.getInstance().chat(this.bot , getChat());
      this.lastimeChatTrain = System.currentTimeMillis();
     }
   }
   
   
   public void activeTraDe(Player pl){
      trade = new Trade(pl , bot);
      this.pl = pl;
      this.trade.openTabTrade();
   }
  
   public void CheckTraDe(List<Item> item){
    int slot1 = item.stream()
    .filter(it -> it.template.id == this.idItTd && it.quantity >= this.slot)
    .mapToInt(it -> it.quantity)
    .findFirst()
    .orElse(0);
    boolean check = slot1 > 0;
     if (check){
         active(slot1);
     } else {
         this.trade.cancelTrade();
     }
   }
   
   public void active(int sl){
       int sl1 = (int) Math.round((double) sl / this.slot);
       Item it = ItemService.gI().createNewItem((short) this.idItem , sl1);
       this.trade.addItemBot(it);
       this.trade.lockTran(this.bot);
       this.trade.acceptTrade();
   }
   
   public void mapL(){
      if(this.bot.zone.map.mapId != 84){
          Zone zone = this.bot.getRandomZone(84);
          if(zone != null){
              ChangeMapService.gI().goToMap(this.bot, zone);
              this.bot.zone.load_Me_To_Another(this.bot);
              PlayerService.gI().playerMove(this.bot, 81 + new Random().nextInt(716), 336);
          }
      }
   }
}