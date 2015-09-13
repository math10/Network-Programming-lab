/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pokerclient;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sanim
 */
public class ClientThread extends Thread{
    
    ClientThread(){
        
    }
    
    
    public void run(){
        while(true){
            try {
                String st = PokerClient.DI.readUTF();
                if(st.equals("ok")){
                    PokerClient.cc.start();
                }else if (st.equals("card")) {
                    int a = PokerClient.DI.readInt();
                    int b = PokerClient.DI.readInt();
                    PokerClient.card.add(new Card(a, b));
                }else if(st.equals("finish1")){
                    PokerClient.gg.setCard();
                }
                else if(st.equals("finish")){
                    String ss = PokerClient.DI.readUTF();
                    System.out.println(ss);
                    if(ss.equals("1st")){
                        PokerClient.gg.setFlag(true);
                    }else PokerClient.gg.setFlag(false);
                }else if(st.equals("call")){
                    int num = PokerClient.DI.readInt();
                    PokerClient.gg.setYou(num);
                }else if(st.equals("winLose")){
                    String ply =   PokerClient.DI.readUTF();
                    if(ply.equals("0")){
                        PokerClient.gg.setP(true);
                    }else if(ply.equals("1"))PokerClient.gg.setP(false);
                    PokerClient.gg.resetAll();
                }
            } catch (IOException ex) {
                Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
