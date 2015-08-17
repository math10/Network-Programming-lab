/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 *
 * @author Sanim
 */
public class ServerThread extends Thread {

    int cnt;
    DataOutputStream ps = null;
    DataInputStream br = null;

    ServerThread(int _c, DataOutputStream _p, DataInputStream _b) {
        cnt = _c;
        ps = _p;
        br = _b;
    }

    public void run() {
        try {
            while (true) {
                String st = br.readUTF();
                if (st.equals("exit")) {
                    break;
                } else if (st.equals("getList")) {
                    String getList = "";
                    for (int i = 0; i < 100; i++) {
                        if (i != cnt && Server.flag[i]) {
                            getList += SendDataToClient.fileList[i];
                            getList += "\n";
                        }
                    }
                    ps.writeUTF("FileLists");
                    ps.writeUTF(getList);
                }else if(st.equals("setFileList")){
                    String ss = br.readUTF();
                    SendDataToClient.fileList[cnt] = ss;
                }else if(st.equals("addFileList")){
                    String ss = br.readUTF();
                    SendDataToClient.fileList[cnt] += ss;
                    SendDataToClient.fileList[cnt] += "\n"; 
                }  
                else if (st.equals("getFile")) {
                    String user = br.readUTF();
                    String fname = br.readUTF();
                    if(user.equals(SendDataToClient.userName[cnt]) || !SendDataToClient.name.containsKey(user)){
                        ps.writeUTF("takeFileContent");
                        ps.writeInt(-1);
                    }else{
                        int x = SendDataToClient.name.get(user);
                        SendDataToClient.ps[x].writeUTF("getFileContent");
                        SendDataToClient.ps[x].writeInt(cnt);
                        SendDataToClient.ps[x].writeUTF(fname);
                    }
                } else if (st.equals("takeFileContent")) {
                    int x = br.readInt();
                    int count = br.readInt();
                    if (count == -1) {
                        SendDataToClient.ps[x].writeUTF("takeFileContent");
                        SendDataToClient.ps[x].writeInt(-1);
                    } else {
                        byte[] bs = new byte[count];
                        br.read(bs);
                        SendDataToClient.ps[x].writeUTF("takeFileContent");
                        SendDataToClient.ps[x].writeInt(count);
                        SendDataToClient.ps[x].write(bs);
                    }
                }
            }
        } catch (Exception e) {
        } finally {
            SendDataToClient.Remove(cnt);
            Server.setFlag(cnt, false);
        }
    }
}
