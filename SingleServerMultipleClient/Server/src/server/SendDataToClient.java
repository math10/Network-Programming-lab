/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.DataOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Sanim
 */
public class SendDataToClient {
    public static DataOutputStream ps[] = new DataOutputStream[100];
    public static String userName[] = new String[100];
    public static String fileList[] = new String[100];
    static Map<String,Integer >name = new HashMap<>(); 
    void init(){
        for(int i = 0;i<100;i++)ps[i] = null;
    }
    static void set(int id,DataOutputStream _p,String _name,String file){
        ps[id] = _p;
        userName[id] = _name;
        fileList[id] = file;
        name.put(_name, id);
    }
    
    static DataOutputStream get(int id){
        return ps[id];
    }
    static void Remove(int s){
        name.remove(userName[s]);
    }
}
