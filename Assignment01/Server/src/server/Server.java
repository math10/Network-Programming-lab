/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sanim
 */
public class Server {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        Server s = new Server();
        while (true) {
            s.call();
        }
    }

   
    public void call() throws IOException {
        ServerSocket MyService = null;
        Socket sock = null;
        InputStreamReader IR = null;
        PrintStream PS = null;
        try {
            MyService = new ServerSocket(80);
            sock = MyService.accept();
            IR = new InputStreamReader(sock.getInputStream());
            BufferedReader BR = new BufferedReader(IR);
            String msg, user, pass;
            msg = BR.readLine();
            System.out.println(msg);
            user = msg;
            PS = new PrintStream(sock.getOutputStream());
            PS.println("User name received.");
            msg = BR.readLine();
            System.out.println(msg);
            pass = msg;
            if (check(user, pass)) {
                PS.println("Success.");
            } else {
                PS.println("Failure.");
            }
            

        } catch (Exception e) {
            System.out.println("ERROR :: " + e);
        }finally{
            sock.close();
            MyService.close();
            IR.close();
            PS.close();
        }
    }

    boolean check(String user, String pass) throws IOException {

        FileReader f = null;
        try {
            f = new FileReader("up.txt");
            BufferedReader BR = new BufferedReader(f);
            String uname, upass;
            while ((uname = BR.readLine()) != null) {
                upass = BR.readLine();
                if (user.equals(uname) && pass.equals(upass)) {
                    return true;
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            f.close();
        }

        return false;
    }
}
