/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nro.event;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import nro.jdbc.daos.AccountDAO;
import nro.jdbc.daos.PlayerDAO;
import nro.models.item.*;
import nro.models.player.Player;
import nro.server.Manager;
import nro.server.io.Message;
import nro.services.InventoryService;
import nro.services.RewardService;
import nro.services.Service;
import nro.utils.Util;

/**
 *
 * @author Administrator
 */
public class BoardEvent {

    public static int[] mocNap = new int[]{
        999,
        2999,
        9999,
        19999,
        29999,
        49999,
        99999,
        125999,
        199999,
        499999,
        699999,
        999999,
        1499999,
        1799999,
        1999999,
        2799999,
        3599999,
        4199999,
        5699999,
        6499999,
       9999999     
            

    };

    public static int[][] IDmocNap = new int[][]{
        new int[]{
            2058, 2059, 2060, 2061
        },
        new int[]{
            1638, 1509
        },
        new int[]{
            575, 576
        },
        new int[]{
            1544, 1545, 1546, 1547, 1548
        },
        new int[]{
            1447, 1448, 1449, 1450, 1451, 1452, 1453, 1454, 1455, 1456
        },
        new int[]{
            1511, 1512, 1513, 1514
        },
        new int[]{
            925, 926, 927, 928, 929, 930, 931, 14, 15, 16, 17, 18, 19, 20
        },
        new int[]{
            1388, 1389, 1390, 1391
        },
        new int[]{
            1970, 1971, 1972
        },
        new int[]{
            1112, 1118, 1125, 1113, 1114, 1115
        },
        new int[]{
            2062, 2063, 1118, 1119, 1120, 1121, 1122, 1123, 1124, 1125
        },
        new int[]{
            650, 651, 652, 653, 654, 655, 656,656,656, 657, 658, 659, 660, 661, 662
        },
        new int[]{
            1992, 1993, 1994, 1995
        },
        new int[]{
            1962, 1963, 1964, 1965
        },
        new int[]{
            1868, 1869, 1870, 1871
        },
        new int[]{
            1624, 1625, 1555, 1556, 1557, 1558, 1559, 1560, 1561, 1562, 1563, 1564, 1565, 1566, 1567, 1568, 1569, 1570, 1571, 1572
        },
        new int[]{
            2052, 2046, 1336, 1337, 1338
        },
        new int[]{
            1107, 1108, 1109, 1110
        },
        new int[]{
            1710, 1711, 1712, 1713, 1714, 1715, 1716, 1717, 1718
        },
        new int[]{
            1989, 1990, 1741, 1727
 },
        new int[]{
            1639, 1640, 1641, 1642,1643
                
                
                
                
        },};

    private static Item[][] Items;

    private static int currTabIndex;
    private static int currSelected;

    public static String eventStr
            = "PHẦN THƯỞNG ĐUA TOP NAP:\n" +
"TOP 1: 1 Cải Trang Ghép 10.000%, 25 Sao, Pha Lê 100% Chí Mạng\n" +
"TOP 2: 1 Cải Trang Ghép 8.000%, 18 Sao, Pha Lê 15% Chí Mạng\n" +
"TOP 3: 1 Cải Trang Ghép 5.000%, 12 Sao, Pha Lê 10% Chí Mạng\n" +
"TOP 4: Tặng 1M Coin + 1 danh hiệu Đại gia 1000% 18sao\n"
            + "\nĐua Top Sự Kiện 8/3\n"
            + "TOP 1: 1 Bó Hoa To 5000% 25 sao\n" +
"TOP 2: 1 Bó Hoa To 3000% 18 sao\n" +
"TOP 3: 1 Bó Hoa To 2000% 12 sao\n" +
"TOP 4: 1 Bó Hoa To 1000% 8 sao"
      ;

    public static void Load() {
        Items = new Item[mocNap.length][];

        for (int i = 0; i < Items.length; i++) {
            if (i >= IDmocNap.length) {
                System.out.println("⚠️ Lỗi: IDmocNap thiếu dữ liệu tại index " + i);
                continue;
            }

            Items[i] = new Item[IDmocNap[i].length];
            for (int j = 0; j < Items[i].length; j++) {
                int id = IDmocNap[i][j];
                Items[i][j] = new Item();
                Items[i][j].template = Manager.ITEM_TEMPLATES.get(id);

                // Kiểm tra mảng Options trước khi truy cập
                if (Options != null && i < Options.length && j < Options[i].length && Options[i][j] != null) {
                    Items[i][j].itemOptions = new ArrayList<>(Arrays.asList(Options[i][j]));
                } else {
                    System.out.println("⚠️ Lỗi: Options[" + i + "][" + j + "] bị null");
                }

            }
        }
    }

    public static ItemOption[][][] Options = new ItemOption[][][]{
        { // 🟢 Mốc 50K (Chứa 4 món)
            { // 🔹 Item 1
                new ItemOption(50, 5), // +700 chỉ số từ option ID 50
            },
            { // 🔹 Item 2
                new ItemOption(77, 5), // +700 chỉ số từ option ID 103
            },
            { // 🔹 Item 3
                new ItemOption(103, 5), // +18 chỉ số từ option ID 107
            },
            { // 🔹 Item 4
                new ItemOption(101, 5), // +50 chỉ số từ option ID 34
            }
        }, // 🔥 END MỐC 50K

        { // 🟢 Mốc 50K (Chứa 4 món)
            { // 🔹 Item 1
                new ItemOption(31, 5), // +700 chỉ số từ option ID 50
            },
            { // 🔹 Item 4
                new ItemOption(31, 5), // +50 chỉ số từ option ID 34
            }
        }, // 🔥 END MỐC 50K  

          { // 🟢 Mốc 50K (Chứa 4 món)
            { // 🔹 Item 1
                new ItemOption(50, 200), // +700 chỉ số từ option ID 50
                 new ItemOption(77, 200), // +700 chỉ số từ option ID 50
                 new ItemOption(103, 200), // +700 chỉ số từ option ID 50
                  new ItemOption(101, 200), // +700 chỉ số từ option ID 50
                  new ItemOption(30, 5), // +700 chỉ số từ option ID 50
            },
            { // 🔹 Item 4
               new ItemOption(50, 200), // +700 chỉ số từ option ID 50
                 new ItemOption(77, 200), // +700 chỉ số từ option ID 50
                 new ItemOption(103, 200), // +700 chỉ số từ option ID 50
                  new ItemOption(78, 200), // +700 chỉ số từ option ID 50
                  new ItemOption(30, 5), // +700 chỉ số từ option ID 50
                
            }
        }, // 🔥 END MỐC 50K   
        
        
        
        { // Mốc 100k
            {new ItemOption(47, 10000),
                new ItemOption(101, 1000),
                new ItemOption(107, 8),
                new ItemOption(30, Util.nextInt(1, 100)),}, // Item 1

            {new ItemOption(6, 1000000),
                new ItemOption(101, 1000),
               new ItemOption(107, 8),
                new ItemOption(30, Util.nextInt(1, 100)),}, // Item 1

            {new ItemOption(0, 50000),
                new ItemOption(101, 1000),
               new ItemOption(107, 8),
                new ItemOption(30, Util.nextInt(1, 100)),}, // Item 1

            {new ItemOption(7, 1000000),
                new ItemOption(101, 1000),
                new ItemOption(107, 8),
                new ItemOption(30, Util.nextInt(1, 100)),}, // Item 1

            {new ItemOption(14, 7),
                new ItemOption(101, 200),
               new ItemOption(107, 8),
                new ItemOption(30, Util.nextInt(1, 100)),}, // Item 1
        }, // ---- END MỐC 100K ----

        
          { // 🟢 Mốc 50K (Chứa 4 món)
            { // 🔹 Item 1
                new ItemOption(30, 5), // +700 chỉ số từ option ID 50
            },
            { // 🔹 Item 2
                new ItemOption(30, 5), // +700 chỉ số từ option ID 103
            },
            { // 🔹 Item 3
                new ItemOption(30, 5), // +18 chỉ số từ option ID 107
            },
            { // 🔹 Item 4
                new ItemOption(30, 5), // +50 chỉ số từ option ID 34
            },
            { // 🔹 Item 2
                new ItemOption(30, 5), // +700 chỉ số từ option ID 103
            },       
             { // 🔹 Item 2
                new ItemOption(30, 5), // +700 chỉ số từ option ID 103
            },
            { // 🔹 Item 2
                new ItemOption(30, 5), // +700 chỉ số từ option ID 103
            },  
             { // 🔹 Item 2
                new ItemOption(30, 5), // +700 chỉ số từ option ID 103
            }, 
            { // 🔹 Item 2
                new ItemOption(30, 5), // +700 chỉ số từ option ID 103
            },  
             { // 🔹 Item 2
                new ItemOption(30, 5), // +700 chỉ số từ option ID 103
            }, 
                  
                  
        }, // 🔥 END MỐC 50K
        
        
          { // 🟢 Mốc 50K (Chứa 4 món)
            { // 🔹 Item 1
                new ItemOption(30, 5), // +700 chỉ số từ option ID 50
            },
            { // 🔹 Item 2
                new ItemOption(30, 5), // +700 chỉ số từ option ID 103
            },
            { // 🔹 Item 3
                new ItemOption(30, 5), // +18 chỉ số từ option ID 107
            },
            { // 🔹 Item 4
                new ItemOption(30, 5), // +50 chỉ số từ option ID 34
            }, 
        
          },
          
           { // 🟢 Mốc 50K (Chứa 4 món)
            { // 🔹 Item 1
                new ItemOption(30, 5), // +700 chỉ số từ option ID 50
            },
            { // 🔹 Item 2
                new ItemOption(30, 5), // +700 chỉ số từ option ID 103
            },
            { // 🔹 Item 3
                new ItemOption(30, 5), // +18 chỉ số từ option ID 107
            },
            { // 🔹 Item 4
                new ItemOption(30, 5), // +50 chỉ số từ option ID 34
            }, 
             { // 🔹 Item 4
                new ItemOption(30, 5), // +50 chỉ số từ option ID 34
            }, 
              { // 🔹 Item 4
                new ItemOption(30, 5), // +50 chỉ số từ option ID 34
            }, 
               { // 🔹 Item 4
                new ItemOption(30, 5), // +50 chỉ số từ option ID 34
            }, 
                { // 🔹 Item 4
                new ItemOption(30, 5), // +50 chỉ số từ option ID 34
            }, 
                 { // 🔹 Item 4
                new ItemOption(30, 5), // +50 chỉ số từ option ID 34
            }, 
                  { // 🔹 Item 4
                new ItemOption(30, 5), // +50 chỉ số từ option ID 34
            }, 
                   { // 🔹 Item 4
                new ItemOption(30, 5), // +50 chỉ số từ option ID 34
            }, 
                    { // 🔹 Item 4
                new ItemOption(30, 5), // +50 chỉ số từ option ID 34
            }, 
                     { // 🔹 Item 4
                new ItemOption(30, 5), // +50 chỉ số từ option ID 34
            }, 
                      { // 🔹 Item 4
                new ItemOption(30, 5), // +50 chỉ số từ option ID 34
            }, 
        
          }, 
          
           
            { // 🟢 Mốc 50K (Chứa 4 món)
            { // 🔹 Item 1
                new ItemOption(214, 100), // +700 chỉ số từ option ID 50
            },
            { // 🔹 Item 2
                new ItemOption(215, 100), // +700 chỉ số từ option ID 103
            },
            { // 🔹 Item 3
                new ItemOption(216, 100), // +18 chỉ số từ option ID 107
            },
            { // 🔹 Item 4
                new ItemOption(217, 5), // +50 chỉ số từ option ID 34
            }, 
        
          },  
           
           { // 🟢 Mốc 50K (Chứa 4 món)
            { // 🔹 Item 1
                new ItemOption(30, 5), // +700 chỉ số từ option ID 50
            },
            { // 🔹 Item 2
                new ItemOption(30, 5), // +700 chỉ số từ option ID 103
            },
            { // 🔹 Item 3
                new ItemOption(30, 5), // +18 chỉ số từ option ID 107
            },
           
        
          },  
           
             { // 🟢 Mốc 50K (Chứa 4 món)
            { // 🔹 Item 1
                new ItemOption(50, 1000), // +700 chỉ số từ option ID 50
                new ItemOption(77, 1000),
                new ItemOption(103, 1000),
                new ItemOption(95, 5),
                new ItemOption(96, 5),
                new ItemOption(107, 18),
                new ItemOption(30, 5),
            },
            { // 🔹 Item 2
                new ItemOption(30, 5), // +700 chỉ số từ option ID 103
            },
            { // 🔹 Item 3
                new ItemOption(30, 5), // +18 chỉ số từ option ID 107
            },
            { // 🔹 Item 3
                new ItemOption(30, 5), // +18 chỉ số từ option ID 107
            },
             { // 🔹 Item 3
                new ItemOption(30, 5), // +18 chỉ số từ option ID 107
            },
              { // 🔹 Item 3
                new ItemOption(30, 5), // +18 chỉ số từ option ID 107
            },
        
          },    
           
           { // 🟢 Mốc 50K (Chứa 4 món)
            { // 🔹 Item 1
                new ItemOption(30, 5), // +700 chỉ số từ option ID 50
            },
            { // 🔹 Item 2
                new ItemOption(30, 5), // +700 chỉ số từ option ID 103
            },
            { // 🔹 Item 3
                new ItemOption(30, 5), // +18 chỉ số từ option ID 107
            },
           { // 🔹 Item 3
                new ItemOption(30, 5), // +18 chỉ số từ option ID 107
            },
           { // 🔹 Item 3
                new ItemOption(30, 5), // +18 chỉ số từ option ID 107
            },
           { // 🔹 Item 3
                new ItemOption(30, 5), // +18 chỉ số từ option ID 107
            },
           { // 🔹 Item 3
                new ItemOption(30, 5), // +18 chỉ số từ option ID 107
            },
           { // 🔹 Item 3
                new ItemOption(30, 5), // +18 chỉ số từ option ID 107
            },
           { // 🔹 Item 3
                new ItemOption(30, 5), // +18 chỉ số từ option ID 107
            },
           { // 🔹 Item 3
                new ItemOption(30, 5), // +18 chỉ số từ option ID 107
            },
        
          },   
             
          { // 🟢 Mốc 50K (Chứa 4 món)
            { // 🔹 Item 1
                new ItemOption(47, 20000), 
                new ItemOption(179, 5), 
                new ItemOption(107, 25), 
                 new ItemOption(30, 25), 

            },
            { // 🔹 Item 2
                new ItemOption(6, 5000000), // +700 chỉ số từ option ID 103
                new ItemOption(179, 5), 
                new ItemOption(107, 25), 
                 new ItemOption(30, 25), 
            },
            { // 🔹 Item 3
                new ItemOption(47, 20000), 
                new ItemOption(178, 5), 
                new ItemOption(107, 25), 
                 new ItemOption(30, 25), 
            },
            { // 🔹 Item 3
                new ItemOption(6, 5000000), 
                new ItemOption(178, 5), 
                new ItemOption(107, 25), 
                 new ItemOption(30, 25), 
            },
             { // 🔹 Item 3
                new ItemOption(47, 20000), 
                new ItemOption(180, 5), 
                new ItemOption(107, 25), 
                 new ItemOption(30, 25), 
            },
             { // 🔹 Item 3
               new ItemOption(6, 5000000), 
                new ItemOption(180, 5), 
                new ItemOption(107, 25), 
                 new ItemOption(30, 25), 
            },
             { // 🔹 Item 3
                new ItemOption(14, 30), 
                new ItemOption(179, 5), 
                new ItemOption(107, 25), 
                 new ItemOption(30, 25), 
            },
             { // 🔹 Item 3
               new ItemOption(14, 30), 
                new ItemOption(178, 5), 
                new ItemOption(107, 25), 
                 new ItemOption(30, 25), 
            },
              { // 🔹 Item 3
             new ItemOption(14, 30), 
                new ItemOption(180, 5), 
                new ItemOption(107, 25), 
                 new ItemOption(30, 25), 
            },
             { // 🔹 Item 3
                new ItemOption(0, 20000), 
                new ItemOption(179, 5), 
                new ItemOption(107, 25), 
                 new ItemOption(30, 25), 
            }, 
            { // 🔹 Item 3
                 new ItemOption(7, 5000000), 
                new ItemOption(179, 5), 
                new ItemOption(107, 25), 
                 new ItemOption(30, 25), 
            }, 
             { // 🔹 Item 3
                new ItemOption(0, 20000), 
                new ItemOption(180, 5), 
                new ItemOption(107, 25), 
                 new ItemOption(30, 25), 
            }, 
             { // 🔹 Item 3
                new ItemOption(7, 5000000), 
                new ItemOption(180, 5), 
                new ItemOption(107, 25), 
                 new ItemOption(30, 25), 
            }, 
             { // 🔹 Item 3
                  new ItemOption(0, 20000), 
                new ItemOption(178, 5), 
                new ItemOption(107, 25), 
                 new ItemOption(30, 25), 
            }, 
             { // 🔹 Item 3
                new ItemOption(7, 5000000), 
                new ItemOption(178, 5), 
                new ItemOption(107, 25), 
                 new ItemOption(30, 25), 
            }, 
        
          },       
             
             
             
             
             
          
        { // Mốc 200k
            {new ItemOption(50, 2000),
                new ItemOption(77, 2000),
                new ItemOption(103, 2000),
                new ItemOption(97, 80),
                 new ItemOption(107, 25),
                  new ItemOption(30, 80),
            }, 

           {new ItemOption(50, 2000),
                new ItemOption(77, 2000),
                new ItemOption(103, 2000),
                new ItemOption(18, 80),
                 new ItemOption(107, 25),
                  new ItemOption(30, 80),
            }, 

           {new ItemOption(50, 2000),
                new ItemOption(77, 2000),
                new ItemOption(103, 2000),
                new ItemOption(181, 80),
                 new ItemOption(107, 25),
                  new ItemOption(30, 80),
            }, 
            {new ItemOption(50, 2000),
                new ItemOption(77, 2000),
                new ItemOption(103, 2000),
                new ItemOption(156, 80),
                 new ItemOption(107, 25),
                  new ItemOption(30, 80),
            }, 
           
        }, // ---- END MỐC 200K ----
        
          { // Mốc 200k
            {new ItemOption(50, 2000),
                new ItemOption(77, 2000),
                new ItemOption(103, 2000),
                new ItemOption(5, 10),
                 new ItemOption(107, 25),
                  new ItemOption(30, 80),
            }, 

           {new ItemOption(50, 2000),
                new ItemOption(77, 2000),
                new ItemOption(103, 2000),
               new ItemOption(5, 10),
                 new ItemOption(107, 25),
                  new ItemOption(30, 80),
            }, 

           {new ItemOption(50, 2000),
                new ItemOption(77, 2000),
                new ItemOption(103, 2000),
               new ItemOption(5, 10),
                 new ItemOption(107, 25),
                  new ItemOption(30, 80),
            }, 
            {new ItemOption(50, 2000),
                new ItemOption(77, 2000),
                new ItemOption(103, 2000),
               new ItemOption(5, 10),
                 new ItemOption(107, 25),
                  new ItemOption(30, 80),
            }, 
         }, 
          
         { // Mốc 200k
            {new ItemOption(50, 5000),
                new ItemOption(77, 5000),
                new ItemOption(103, 5000),
                new ItemOption(78, 10),
                 new ItemOption(107, 25),
                  new ItemOption(30, 80),
            }, 

          {new ItemOption(50, 5000),
                new ItemOption(77, 5000),
                new ItemOption(103, 5000),
                new ItemOption(78, 10),
                 new ItemOption(107, 25),
                  new ItemOption(30, 80),
            }, 

            {new ItemOption(50, 5000),
                new ItemOption(77, 5000),
                new ItemOption(103, 5000),
                new ItemOption(78, 10),
                 new ItemOption(107, 25),
                  new ItemOption(30, 80),
            }, 
            {new ItemOption(50, 2000),
                new ItemOption(77, 2000),
                new ItemOption(103, 2000),
              new ItemOption(78, 10),
                 new ItemOption(107, 25),
                  new ItemOption(30, 80),
            }, 
         }, 
         
           { // 🟢 Mốc 50K (Chứa 4 món)
            { // 🔹 Item 1
                new ItemOption(50, 500), // +700 chỉ số từ option ID 50
            },
            { // 🔹 Item 2
                new ItemOption(50, 1000), // +700 chỉ số từ option ID 103
            },
            { // 🔹 Item 3
                new ItemOption(30, 5), // +18 chỉ số từ option ID 107
            },
           { // 🔹 Item 3
                new ItemOption(30, 5), // +18 chỉ số từ option ID 107
            },
           { // 🔹 Item 3
                new ItemOption(30, 5), // +18 chỉ số từ option ID 107
            },
           { // 🔹 Item 3
                new ItemOption(30, 5), // +18 chỉ số từ option ID 107
            },
           { // 🔹 Item 3
                new ItemOption(30, 5), // +18 chỉ số từ option ID 107
            },
           { // 🔹 Item 3
                new ItemOption(30, 5), // +18 chỉ số từ option ID 107
            },
           { // 🔹 Item 3
                new ItemOption(30, 5), // +18 chỉ số từ option ID 107
            },
           { // 🔹 Item 3
                new ItemOption(30, 5), // +18 chỉ số từ option ID 107
            },
         { // 🔹 Item 3
                new ItemOption(30, 5), // +18 chỉ số từ option ID 107
            },
           { // 🔹 Item 3
                new ItemOption(30, 5), // +18 chỉ số từ option ID 107
            }, 
           
            { // 🔹 Item 3
                new ItemOption(30, 5), // +18 chỉ số từ option ID 107
            },
             { // 🔹 Item 3
                new ItemOption(30, 5), // +18 chỉ số từ option ID 107
            },
              { // 🔹 Item 3
                new ItemOption(30, 5), // +18 chỉ số từ option ID 107
            },
               { // 🔹 Item 3
                new ItemOption(30, 5), // +18 chỉ số từ option ID 107
            },
                { // 🔹 Item 3
                new ItemOption(30, 5), // +18 chỉ số từ option ID 107
            },
                 { // 🔹 Item 3
                new ItemOption(30, 5), // +18 chỉ số từ option ID 107
            },
                  { // 🔹 Item 3
                new ItemOption(30, 5), // +18 chỉ số từ option ID 107
            },
                   { // 🔹 Item 3
                new ItemOption(30, 5), // +18 chỉ số từ option ID 107
            },
           
          },   
         
             
         { // Mốc 200k
            {new ItemOption(50, 5000),
                new ItemOption(77, 5000),
                new ItemOption(103, 5000),
              new ItemOption(76, 100),
                 new ItemOption(107, 25),
                  new ItemOption(30, 80),
            }, 

          {new ItemOption(50, 5000),
                new ItemOption(77, 5000),
                new ItemOption(103, 5000),
               new ItemOption(76, 100),
                 new ItemOption(107, 25),
                  new ItemOption(30, 80),
            }, 

            {new ItemOption(50, 5000),
                new ItemOption(77, 5000),
                new ItemOption(103, 5000),
               new ItemOption(76, 100),
                 new ItemOption(107, 25),
                  new ItemOption(30, 80),
            }, 
            {new ItemOption(50, 5000),
                new ItemOption(77, 5000),
                new ItemOption(103, 5000),
              new ItemOption(76, 100),
                 new ItemOption(107, 25),
                  new ItemOption(30, 80),
            }, 
            
             {new ItemOption(50, 5000),
                new ItemOption(77, 5000),
                new ItemOption(103, 5000),
              new ItemOption(76, 100),
                 new ItemOption(107, 25),
                  new ItemOption(30, 80),
            },  
         }, 
         
         { // Mốc 200k
            {new ItemOption(50, 5000),
                new ItemOption(77, 5000),
                new ItemOption(103, 5000),
              new ItemOption(76, 100),
                 new ItemOption(107, 50),
                  new ItemOption(30, 80),
            }, 

          {new ItemOption(50, 5000),
                new ItemOption(77, 5000),
                new ItemOption(103, 5000),
               new ItemOption(76, 100),
                 new ItemOption(107, 50),
                  new ItemOption(30, 80),
            }, 

            {new ItemOption(50, 5000),
                new ItemOption(77, 5000),
                new ItemOption(103, 5000),
               new ItemOption(76, 100),
                 new ItemOption(107, 50),
                  new ItemOption(30, 80),
            }, 
            {new ItemOption(50, 2000),
                new ItemOption(77, 2000),
                new ItemOption(103, 2000),
              new ItemOption(76, 100),
                 new ItemOption(107, 50),
                  new ItemOption(30, 80),
            }, 
            
          
         },   
         
       { // Mốc 200k
            {new ItemOption(50, 10000),
                new ItemOption(77, 10000),
                new ItemOption(103, 10000),
              new ItemOption(5, 100),
                 new ItemOption(107, 50),
                  new ItemOption(30, 80),
            }, 

         {new ItemOption(50, 10000),
                new ItemOption(77, 10000),
                new ItemOption(103, 10000),
              new ItemOption(5, 100),
                 new ItemOption(107, 50),
                  new ItemOption(30, 80),
            }, 

          {new ItemOption(50, 10000),
                new ItemOption(77, 10000),
                new ItemOption(103, 10000),
              new ItemOption(5, 100),
                 new ItemOption(107, 50),
                  new ItemOption(30, 80),
            }, 
           {new ItemOption(50, 10000),
                new ItemOption(77, 10000),
                new ItemOption(103, 10000),
              new ItemOption(5, 100),
                 new ItemOption(107, 50),
                  new ItemOption(30, 80),
            }, 
             {new ItemOption(50, 10000),
                new ItemOption(77, 10000),
                new ItemOption(103, 10000),
              new ItemOption(5, 100),
                 new ItemOption(107, 50),
                  new ItemOption(30, 80),
            },  
           
             {new ItemOption(50, 10000),
                new ItemOption(77, 10000),
                new ItemOption(103, 10000),
              new ItemOption(5, 100),
                 new ItemOption(107, 50),
                  new ItemOption(30, 80),
            }, 
             {new ItemOption(50, 10000),
                new ItemOption(77, 10000),
                new ItemOption(103, 10000),
              new ItemOption(5, 100),
                 new ItemOption(107, 50),
                  new ItemOption(30, 80),
            }, 
             {new ItemOption(50, 10000),
                new ItemOption(77, 10000),
                new ItemOption(103, 10000),
              new ItemOption(5, 100),
                 new ItemOption(107, 50),
                  new ItemOption(30, 80),
            }, 
             {new ItemOption(50, 10000),
                new ItemOption(77, 10000),
                new ItemOption(103, 10000),
              new ItemOption(5, 100),
                 new ItemOption(107, 50),
                  new ItemOption(30, 80),
            }, 
          
         },      
         
     
         { // Mốc 200k
            {new ItemOption(50, 10000),
                new ItemOption(77, 10000),
                new ItemOption(103, 10000),
              new ItemOption(101, 10000),
                 new ItemOption(107, 99),
                  new ItemOption(30, 80),
            }, 

         {new ItemOption(50, 10000),
                new ItemOption(77, 10000),
                new ItemOption(103, 10000),
              new ItemOption(101, 10000),
                 new ItemOption(107, 99),
                  new ItemOption(30, 80),
            }, 

            {new ItemOption(50, 10000),
                new ItemOption(77, 10000),
                new ItemOption(103, 10000),
              new ItemOption(101, 10000),
                 new ItemOption(107, 99),
                  new ItemOption(30, 80),
            }, 
            {new ItemOption(50, 10000),
                new ItemOption(77, 10000),
                new ItemOption(103, 10000),
              new ItemOption(101, 10000),
                 new ItemOption(107, 99),
                  new ItemOption(30, 80),
            }, 
            
            
            
            
            
          
         },     
         
         
           { // Mốc 200k
               
            { new ItemOption(167, 10000),
                new ItemOption(50, 10000),
                new ItemOption(77, 10000),
                new ItemOption(103, 10000),
              new ItemOption(101, 10000),
                 new ItemOption(107, 99),
                  new ItemOption(30, 80),
                   new ItemOption(168, 10000),
            }, 

         { new ItemOption(167, 10000),
                new ItemOption(50, 10000),
                new ItemOption(77, 10000),
                new ItemOption(103, 10000),
              new ItemOption(101, 10000),
                 new ItemOption(107, 99),
                  new ItemOption(30, 80),
                   new ItemOption(168, 10000),
            }, 
         
         
            { new ItemOption(167, 10000),
                new ItemOption(50, 10000),
                new ItemOption(77, 10000),
                new ItemOption(103, 10000),
              new ItemOption(101, 10000),
                 new ItemOption(107, 99),
                  new ItemOption(30, 80),
                   new ItemOption(168, 10000),
            }, 
            
            
            {new ItemOption(50, 10000),
                new ItemOption(77, 10000),
                new ItemOption(103, 10000),
              new ItemOption(101, 10000),
                 new ItemOption(107, 99),
                  new ItemOption(30, 80),
            }, 
            
            {new ItemOption(50, 10000),
                new ItemOption(77, 10000),
                new ItemOption(103, 10000),
              new ItemOption(101, 10000),
                 new ItemOption(107, 99),
                  new ItemOption(30, 80),
            },  
            
         
         
          }, 
         
      
    };

    public static void getCurrentIndex(Message msg, Player player) {
        try {
            // Đọc chỉ số từ message
            int currSelected = msg.reader().readByte() - 1;
            System.out.println("Thằng " + player.name + " Vừa mua Gói Tân Thủ " + currSelected);

            // Kiểm tra player hoặc dữ liệu null
            if (player == null || player.getSession() == null || player.scoreBoards == null || mocNap == null || Items == null) {
                //     System.err.println("Lỗi: Dữ liệu của player hoặc server bị null.");
                return;
            }

            // Kiểm tra nếu currSelected hợp lệ
            if (currSelected < 0 || currSelected >= player.scoreBoards.length) {
                System.err.println("Lỗi: currSelected ngoài phạm vi scoreBoards (currSelected: " + currSelected + ", length: " + player.scoreBoards.length + ")");
                return;
            }

            // Kiểm tra nếu currSelected nằm trong phạm vi của mocNap
            if (currSelected >= mocNap.length) {
                System.err.println("Lỗi: currSelected vượt quá kích thước của mocNap.");
                return;
            }

            if (InventoryService.gI().getCountEmptyBag(player) <= 20) {
                Service.gI().sendThongBao(player, "Hành trang Tối Thiểu Trống 20 ô! Không thể nhận thêm vật phẩm.");
                return;
            }

            // Kiểm tra tiền của người chơi
            if (player.getSession().vnd < mocNap[currSelected]) {
                Service.gI().sendThongBao(player, "Bạn không đủ tiền!");
                return;
            }

            // Trừ tiền
//        player.getSession().vnd -= mocNap[currSelected];
//        AccountDAO.updateVND(player.getSession());
            PlayerDAO.subvnd(player, mocNap[currSelected]); // Thay vì updateVND
            Service.gI().sendThongBao(player, "Số dư hiện tại: " + player.getSession().vnd + " VND");
            // Kiểm tra nếu currSelected hợp lệ với Items
            if (currSelected >= Items.length) {
                System.err.println("Lỗi: currSelected vượt quá kích thước của Items.");
                return;
            }

            // Thêm vật phẩm vào túi
            for (int i = 0; i < Items[currSelected].length; i++) {
                Item item = Items[currSelected][i];
                InventoryService.gI().addItemBag(player, item, 999999999);
                InventoryService.gI().sendItemBags(player);
                Service.getInstance().sendMoney(player);
            }

            // Gửi trạng thái cập nhật
            sendStatusItems(player, currSelected);
            Service.getInstance().sendMoney(player);

            Service.gI().sendThongBao(player, "Đã gửi quà về túi!");

        } catch (IOException ex) {
            Logger.getLogger(BoardEvent.class.getName()).log(Level.SEVERE, "Lỗi đọc message", ex);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static boolean isBagFull(Player player) {
        for (int i = player.inventory.itemsBag.size() - 1; i >= 0; i--) {
            if (player.inventory.itemsBag.get(i) == null) {
                return false;
            }
        }
        return true;
    }

    public static void sendStatusItems(Player player, int index) {
        Message msg;
        try {
            msg = Service.gI().messageBoardEventReward((byte) 2);

            for (int j = 0; j < player.scoreBoards[index]; j++) {
                boolean isBought = (player.scoreBoards[index] & (1L << j)) > 0;
                msg.writer().writeBoolean(isBought); // Gửi trạng thái đã mua về client
            }
            player.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendEventStr(Player player) {
        Message msg;
        try {
            //    System.out.println("str");
            msg = Service.gI().messageBoardEventReward((byte) 3);
            msg.writer().writeUTF(eventStr);
            player.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void SendMocNap(Player player) {
        Message msg;
        try {
            msg = Service.gI().messageBoardEventReward((byte) 0);
//            System.err.println("Tichnap;");
            msg.writer().writeInt(mocNap.length); //size
            msg.writer().writeInt(player.getSession().vnd); //currNap
            for (int i = 0; i < mocNap.length; i++) {
                msg.writer().writeInt(mocNap[i]); //maxNap
                msg.writer().writeInt(Items[i].length);
                for (int j = 0; j < Items[i].length; j++) {
                    msg.writer().writeInt(Items[i][j].template.id);
                    msg.writer().writeInt(Items[i][j].quantity);
                    msg.writer().writeByte(Items[i][j].itemOptions.size());
                    for (ItemOption k : Items[i][j].itemOptions) {
                        msg.writer().writeByte(k.optionTemplate.id);
                        msg.writer().writeLong(k.param);
                    }

                }

            }

            player.sendMessage(msg);
            //msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

