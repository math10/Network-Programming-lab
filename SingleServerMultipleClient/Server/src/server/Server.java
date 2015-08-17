/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Sanim
 */
public class Server {
    /**
     * @param args the command line arguments
     */
    static String data[] = new String[100];
    static boolean flag[] = new boolean[100];
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        ServerSocket sc = new ServerSocket(420);

        for (int i = 0; i < 100; i++) {
            flag[i] = false;
        }

        while (true) {
            try {
                int cnt = getId();
                Socket sock = sc.accept();
                DataInputStream DI = new DataInputStream(sock.getInputStream());
                DataOutputStream DO = new DataOutputStream(sock.getOutputStream());
               if(cnt == -1)DO.writeUTF("don't have enough mamory");
                else{
                    String user = DI.readUTF();
                    String pass = DI.readUTF();
                    ClientCheck chk = new ClientCheck(user,pass);
                    if(SendDataToClient.name.containsKey(user)){
                        DO.writeUTF("-1");
                    }
                    else if(chk.check()){
                        DO.writeUTF("1");
                        String st = DI.readUTF();
                        setFlag(cnt,true);
                        SendDataToClient.set(cnt, DO,user,st);
                        Thread t = new Thread(new ServerThread(cnt,DO,DI));
                        t.start();
                    }else{
                        DO.writeUTF("0");
                    }
                }
            } catch (Exception e) {
            } finally {
            }
        }

    }

    static int getId() {
        for(int i = 0;i<100;i++){
            if(flag[i] == false) return i;
        }
        return -1;
    }
    static void setFlag(int id,boolean f){
        flag[id] = f;
    }
}
