/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pokerserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Sanim
 */
public class POkerServer {

    /**
     * @param args the command line arguments
     */
    static Socket[] sock = new Socket[2];
    static DataInputStream[] DI = new DataInputStream[2];
    static DataOutputStream[] DO = new DataOutputStream[2];
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        ServerSocket sc = new ServerSocket(420);
        for(int i = 0;i<2;i++){
            sock[i] = sc.accept();
            DI[i] = new DataInputStream(sock[i].getInputStream());
            DO[i] = new DataOutputStream(sock[i].getOutputStream());
        }
        
        Thread t1 = new Thread(new ServerThread());
        
        t1.start();
        
    }
}
