/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client_sanim;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.*;
import java.util.Scanner;

/**
 *
 * @author Sanim
 */
public class Client_sanim {

    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        Scanner sc = new Scanner(System.in);
        Socket sock = null;
        InputStreamReader IR = null;
        PrintStream PS = null;
        try {
            sock = new Socket("localhost", 80);
            System.out.println("Welcome...");
            System.out.println("Enter user name");
            String st = sc.nextLine(), msg;
            PS = new PrintStream(sock.getOutputStream());
            PS.println(st);
            IR = new InputStreamReader(sock.getInputStream());
            BufferedReader BR = new BufferedReader(IR);
            msg = BR.readLine();
            System.out.println(msg);
            System.out.println("Enter your password...");
            st = sc.nextLine();
            PS.println(st);
            msg = BR.readLine();
            System.out.println(msg);
        } catch (Exception e) {
            System.out.println("ERROR :: " + e);
        }finally{
            sock.close();
            IR.close();
            PS.close();
        }
    }
}
