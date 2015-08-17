/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client_sanim;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 * @author Sanim
 */
public class ClientThread extends Thread {

    static DataInputStream DI = null;
    static DataOutputStream DO = null;

    ClientThread(DataOutputStream p, DataInputStream b) {
        DO = p;
        DI = b;
    }

    public void run() {
        try {
            while (true) {
                String st = DI.readUTF();
                if (st.equals("FileLists")) {
                    st = DI.readUTF();
                    System.out.println(st);
                    Client_sanim.flag = false;
                } else if (st.equals("getFileContent")) {
                    int x = DI.readInt();
                    String fName = DI.readUTF();

                    try {
                        FileInputStream f = new FileInputStream(fName);
                        DataInputStream ff = new DataInputStream(f);
                        int cnt = ff.available();
                        byte[] bs = new byte[cnt];
                        ff.read(bs);
                        DO.writeUTF("takeFileContent");
                        DO.writeInt(x);
                        DO.writeInt(cnt);
                        DO.write(bs);

                    } catch (IOException e) {
                        DO.writeUTF("takeFileContent");
                        DO.writeInt(x);
                        DO.writeInt(-1);
                    }

                } else if (st.equals("takeFileContent")) {
                    int x = DI.readInt();
                    if (x == -1) {
                        System.out.println("No such File or user");
                        Client_sanim.flag = false;
                    } else {
                        String fn = Client_sanim.filePath;
                        try {
                            FileInputStream f = new FileInputStream(fn);                            
                            System.out.println("You have this file");
                            f.close();
                            Client_sanim.flag = false;
                        } catch (IOException e) {
                            FileOutputStream f = new FileOutputStream(fn);
                            DataOutputStream ff = new DataOutputStream(f);
                            byte[] bt = new byte[x];
                            DI.read(bt);
                            ff.write(bt);
                            ff.close();
                            f.close();
                            
                            Client_sanim.myList += fn;
                            Client_sanim.myList += "\n";
                            System.out.println(Client_sanim.myList);
                            DO.writeUTF("addFileList");
                            DO.writeUTF(fn+" "+Client_sanim.user+"\n");
                            f = new FileOutputStream("fileList.txt");
                            ff = new DataOutputStream(f);
                            ff.writeBytes(Client_sanim.myList);
                            Client_sanim.flag = false;
                        }

                    }
                }
            }
        } catch (Exception e) {
        }
    }
}
