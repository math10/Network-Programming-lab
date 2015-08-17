/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client_sanim;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.IOException;
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
    static Socket sock = null;
    static DataInputStream DI = null;
    static DataOutputStream DO = null;
    static String user,myList="";
    static boolean flag;
    static String filePath = "";
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        Scanner sc = new Scanner(System.in);
        
        try {
            sock = new Socket("localhost", 420);
            DI = new DataInputStream(sock.getInputStream());
            DO = new DataOutputStream(sock.getOutputStream());
            
            System.out.println("Welcome...");
            System.out.println("Enter user name");
            String st = sc.nextLine(), msg;    
            user = st;
            DO.writeUTF(st);
            System.out.println("Enter your password...");
            st = sc.nextLine();
            DO.writeUTF(st);
            msg = DI.readUTF();
            if (msg.equals("1")) {
                System.out.println("Login successfully");
                makeFileList();
                Thread t = new Thread(new ClientThread(DO,DI));
                t.start();
                while (true) {
                    System.out.println("1. Get Other List");
                    System.out.println("2. Copy Other File");
                    System.out.println("3. Exit");
                    System.out.println("Enter your choice(1,2,3)...");
                    int it = sc.nextInt();
                    if (it == 3) {
                        DO.writeUTF("exit");
                        sock.close();
                        break;
                    }
                    else if (it == 1) {
                        getList();
                        flag = true;
                        while(flag);
                    } else if(it == 2){
                        String a,b;
                        System.out.println("User Name :: ");
                        sc.nextLine();
                        a = sc.nextLine();
                        System.out.println("File Name :: ");
                        b = sc.nextLine();
                        filePath = b;
                        copyFile(a,b);
                        flag = true;
                        while(flag);
                    }
                }
            } else if(msg.equals("0")){
                System.out.println("wrong user name or password");
            }else if(msg.equals("-1")){
                System.out.println("this user already in");
            }
        } catch (Exception e) {
            System.out.println("ERROR :: " + e);
        } finally {
            
        }
    }

    static void getList() throws IOException {
        
        DO.writeUTF("getList");
        /*String st = DI.readUTF();
        st = DI.readUTF();
        System.out.println(st);*/
    }

    static void copyFile(String a,String b) throws IOException {
       DO.writeUTF("getFile");
       DO.writeUTF(a);
       DO.writeUTF(b);
    }
    
    static void makeFileList() throws IOException{
        FileReader f = null;
            try {
                f = new FileReader("fileList.txt");
                BufferedReader B = new BufferedReader(f);
                String st = "",name;
                while ((name = B.readLine()) != null) {
                   st += name;
                   st += " ";
                   st += user;
                   st += "\n";
                   myList += name;
                   myList += "\n";
                }
                DO.writeUTF(st);
            } catch (Exception e) {
                System.out.println("Error ..." + e);
            } finally {
                f.close();
            }
    }
}
