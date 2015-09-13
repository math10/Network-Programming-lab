/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pokerserver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sanim
 */
public class ServerThread extends Thread {

    static int ids[] = new int[2];
    static int count = 0;
    static boolean f;
    List<Card> L;

    ServerThread() throws IOException {
        for (int i = 0; i < 2; i++) {
            ids[i] = i;
        }
        L = new ArrayList();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 13; j++) {
                L.add(new Card(i, j));
            }
        }

        for (int i = 0; i < 2; i++) {
            POkerServer.DO[i].writeUTF("ok");
        }
    }
    int cc = 0;

    public void run() {
        while (true) {
            try {
                long seed = System.nanoTime();
                Collections.shuffle(L, new Random(seed));
                for (int i = 0 + (cc % 2); i < 6 + (cc % 2); i++) {
                    Card c = L.get(i);
                    POkerServer.DO[i % 2].writeUTF("card");
                    POkerServer.DO[i % 2].writeInt(c.a);
                    POkerServer.DO[i % 2].writeInt(c.b);
                }

                for (int i = 0; i < 2; i++) {
                    POkerServer.DO[ids[(i + cc) % 2]].writeUTF("finish1");
                }

                for (int i = 0; i < 2; i++) {
                    POkerServer.DO[ids[(i + cc) % 2]].writeUTF("finish");
                    if (i > 0) {
                        POkerServer.DO[ids[(i + cc) % 2]].writeUTF("2nd");
                    } else {
                        POkerServer.DO[ids[(i + cc) % 2]].writeUTF("1st");
                    }
                }
                boolean cnt = true;
                int c1 = 0, c2 = 0, ccc = 0;
                while (true) {
                    if (cnt) {
                        int num = POkerServer.DI[(0+cc)%2].readInt();
                        if (num == -1) {
                            c1 = -1;
                            break;
                        } else {
                            c1 = num;
                        }
                        POkerServer.DO[(1+cc)%2].writeUTF("call");
                        POkerServer.DO[(1+cc)%2].writeInt(num);
                    } else {
                        int num = POkerServer.DI[(1+cc)%2].readInt();
                        if (num == -1) {
                            c2 = -1;
                            break;
                        } else {
                            c2 = num;
                        }
                        POkerServer.DO[(0+cc)%2].writeUTF("call");
                        POkerServer.DO[(0+cc)%2].writeInt(num);
                    }
                    ccc++;
                    for (int i = 0; i < 2; i++) {
                        POkerServer.DO[ids[(i + ccc + cc) % 2]].writeUTF("finish");
                        if (i > 0) {
                            POkerServer.DO[ids[(i + ccc + cc) % 2]].writeUTF("2nd");
                        } else {
                            POkerServer.DO[ids[(i + ccc + cc) % 2]].writeUTF("1st");
                        }
                    }
                    cnt = !cnt;
                    if (c1 == c2) {
                        break;
                    }
                }
                POkerServer.DO[0].writeUTF("winLose");
                POkerServer.DO[1].writeUTF("winLose");
                if (c1 == -1) {
                    POkerServer.DO[0].writeUTF("1");
                    POkerServer.DO[1].writeUTF("0");
                } else if (c2 == -1) {
                    POkerServer.DO[0].writeUTF("1");
                    POkerServer.DO[1].writeUTF("0");
                } else {
                    int x = check();
                    if (x == 1) {
                        POkerServer.DO[0].writeUTF("0");
                        POkerServer.DO[1].writeUTF("1");
                    } else if (x == 0) {
                        POkerServer.DO[0].writeUTF("1");
                        POkerServer.DO[1].writeUTF("0");
                    } else {
                        POkerServer.DO[0].writeUTF("-1");
                        POkerServer.DO[1].writeUTF("-1");
                    }
                }
                cc++;
            } catch (Exception ex) {
                Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    int check() {
        Card[] c1 = new Card[3];
        Card[] c2 = new Card[3];
        for (int i = 0; i < 3; i++) {
            c1[i] = L.get(2 * i);
        }
        for (int i = 0; i < 3; i++) {
            c2[i] = L.get(2 * i + 1);
        }
        int x1 = weight(c1);
        int x2 = weight(c2);
        if(x1 > x2) return 1;
        else if(x1 < x2) return 0;
        else return -1;
        
    }

    int weight(Card c[]) {
        int x = troy(c);
        if (x != -1) {
            x = call(c);
            return 10000 + x;
        }
        x = runnimgFlush(c);
        if (x != -1) {
            x = call(c);
            return 9000 + x;
        }
        x = running(c);
        if (x != -1) {
            return 8000 + x;
        }
        x = color(c);
        if (x != -1) {
            x = call(c);
            return 7000 + x;
        }
        x = doubleCard(c);
        if (x != -1) {
            x = call(c);
            return 6000 + x;
        }

        return call(c);
    }

    int troy(Card c[]) {
        int num = c[0].b;
        for (int i = 0; i < 3; i++) {
            if (num != c[i].b) {
                return -1;
            }
        }
        return num;
    }

    int runnimgFlush(Card c[]) {
        if (color(c) == -1) {
            return -1;
        }
        return running(c);
    }

    int running(Card c[]) {
        int num = 100;
        for (int i = 0; i < 3; i++) {
            num = c[i].b < num ? c[i].b : num;
        }

        for (int i = 0; i < 3; i++) {
            boolean flag = false;
            for (int j = 0; j < 3; j++) {
                if (num == c[i].b) {
                    flag = true;
                }
            }
            if (!flag) {
                return -1;
            }
        }
        return num;
    }

    int color(Card c[]) {
        int num = c[0].a;
        for (int i = 0; i < 3; i++) {
            if (num != c[i].a) {
                return -1;
            }
        }
        return 0;
    }
    
    int doubleCard(Card c[]){
        if(c[0].b == c[1].b || c[0].b == c[2].b || c[1].b == c[2].b) return 1;
        return -1;
    }

    int call(Card c[]) {
        int x = 0;
        int p = 13 * 13;
        for (int i = 0; i < 3; i++) {
            int id = -1, num = -1;
            for (int j = i; j < 3; j++) {
                if (num < c[j].b) {
                    num = c[j].b;
                    id = j;
                }
            }
            Card tmp = c[i];
            c[i] = c[id];
            c[id] = tmp;
            x = x + c[i].b * p;
            p /= 13;
        }
        return x;
    }
}
