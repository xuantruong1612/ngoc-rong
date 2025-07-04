/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nro.server;

import lombok.Builder;
import lombok.Data;

/**
 *
 * @author Administrator
 */
@Data
@Builder
public class TOP {
    private int id_player;
    private long power;
    private long ki;
    private long hp;
    private long sd;
    private byte nv;
    private int sk;
    private int pvp;
    private double dameBoss;
    private String info1;
    private String info2;
     private String info3;
}
