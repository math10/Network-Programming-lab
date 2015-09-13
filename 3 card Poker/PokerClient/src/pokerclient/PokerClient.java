/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pokerclient;

import java.util.List;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 *
 * @author Sanim
 */
public class PokerClient {

    /**
     * @param args the command line arguments
     */
    static DataInputStream DI;
    static DataOutputStream DO;
    static ClientPage cc;
    static GamePage gg;
    static List<Card> card;
    public static void main(String[] args) throws UnknownHostException, IOException {
        // TODO code application logic here
        Socket sc = new Socket("localhost",420);
        DI = new DataInputStream(sc.getInputStream());
        DO = new DataOutputStream(sc.getOutputStream());
        cc = new ClientPage();
        cc = new ClientPage();
        card = new ArrayList();
        Thread t = new Thread(new ClientThread());
        t.start();
        cc.setVisible(true);
    }
}
