/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author Sanim
 */
public class ClientCheck {
    String name,pass;
    ClientCheck(String _a,String _b){
        this.name = _a;
        this.pass = _b;
    }
    
    boolean check() throws IOException{
            FileReader f = null;
            try {
                f = new FileReader("up.txt");
                BufferedReader BR = new BufferedReader(f);
                String uname, upass;
                while ((uname = BR.readLine()) != null) {
                    upass = BR.readLine();
                    if (name.equals(uname) && pass.equals(upass)) {
                        return true;
                    }
                }
            } catch (Exception e) {
                System.out.println("Error ..." + e);
            } finally {
                f.close();
            }
            return false;
    }
}
