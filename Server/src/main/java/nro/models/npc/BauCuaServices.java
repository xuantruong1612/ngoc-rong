/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nro.models.npc;

import nro.server.ServerManager;

/**
 *
 * @author Administrator
 */
public class BauCuaServices extends Thread {

    public static long TimeBauCua = 30000;

    public void subTimeBauCua() {
        TimeBauCua = Math.max(TimeBauCua - 1000, 0);
    }

    public String removeCommaAtEnd(String input) {
        if (input != null && !input.isEmpty() && input.endsWith(",")) {
            input = input.substring(0, input.length() - 1);
        }
        return input;
    }

    @Override
    public void run() {
        while (ServerManager.isRunning) {
            if (TimeBauCua == 0) {

                BauCua.result = "";
                for (int i = 0; i < 3; i++) {
                    BauCua.result += BauCua.getRandomElement(BauCua.elements) + ",";
                }

               // System.out.println("resoyl " + BauCua.result);
                BauCua.result = removeCommaAtEnd(BauCua.result);
                BauCua.ketQuaTruoc = BauCua.result;
                //System.out.println("ketquatruoc " + BauCua.ketQuaTruoc);
                BauCua.traoQua();
                Sleep(5000);
                TimeBauCua = 30000;
            }
            Sleep(1000);
            subTimeBauCua();
        }
    }

    public void Sleep(long j) {
        try {
            Thread.sleep(1000);
        } catch (Exception e) {

        }
    }
}
