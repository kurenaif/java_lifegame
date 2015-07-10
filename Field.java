/**
 * Created by koh on 15/06/18 (018).
 */

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import sun.awt.image.BufferedImageDevice;

import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.util.*;

public class Field extends Frame {
    private boolean[] field = new boolean[262144];//512*512‚ªŒÀŠE
    private int dx[] = {1, 1, 0, -1, -1, -1, 0, 1};
    private int dy[] = {0, 1, 1, 1, 0, -1, -1, -1};
    private Random random = new Random();
    public Dimension size = new Dimension(200, 200);

    Field() {
        RandomGenerate();
    }

    boolean Get(int x, int y) {
        if (x < 0 || y < 0 || x >= size.width || y >= size.height) return false;
        return field[y * size.width + x];
    }

    void Set(int x, int y, boolean b) {
        if (x < 0 || y < 0 || x >= size.width || y >= size.height) return;
        field[y * size.width + x] = b;
    }

    void SetSize(int width, int height) {
        size.width = width;
        size.height = height;
    }

    void Load() {
        JFileChooser fileChooser = new JFileChooser();
        int selected = fileChooser.showOpenDialog(this);
        File file = new File("");
        if (selected == JFileChooser.APPROVE_OPTION){
            file = fileChooser.getSelectedFile();
        }else if (selected == JFileChooser.CANCEL_OPTION){
            return;
        }else if (selected == JFileChooser.ERROR_OPTION){
            System.out.println("File Choose Error");
            return;
        }
        try{
            FileReader fileReader = new FileReader(file);
            BufferedReader br = new BufferedReader(fileReader);

            String line;
            line = br.readLine();
            String[] array = line.split(" ");
            size.width = Integer.valueOf(array[0]);
            size.height=Integer.valueOf(array[1]);

            for(int y=0;(line = br.readLine()) != null;++y){
                String[] sl = line.split(" ");
                for(int x=0;x<sl.length;++x){
                    Set(x,y,Boolean.valueOf(sl[x]));
                }
            }

        }catch (FileNotFoundException e){
            System.out.println(e);
        }
        catch (IOException e){
            System.out.println(3);
        }
    }

    void Save(Point start, Point end){
        if(start.x < 0) start.x = 0;
        if(start.y < 0) start.y = 0;
        if(start.x >= size.width-1) start.x = size.width-2;
        if(start.y >= size.height-1) start.y = size.height-2;
        if(end.x < 0) end.x = 0;
        if(end.y < 0) end.y = 0;
        if(end.x >= size.width-1) end.x = size.width-2;
        if(end.y >= size.height-1) end.y = size.height-2;
        if(start.x > end.x) {
            Point p = new Point();
            p = start;
            start = end;
            end = p;
        }
        JFileChooser fileChooser = new JFileChooser();
        int selected = fileChooser.showSaveDialog(this);
        File file = new File("");
        if (selected == JFileChooser.APPROVE_OPTION){
            file = fileChooser.getSelectedFile();
        }else if (selected == JFileChooser.CANCEL_OPTION){
            return;
        }else if (selected == JFileChooser.ERROR_OPTION){
            System.out.println("File Choose Error");
            return;
        }

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));

            bw.write(String.valueOf(end.x - start.x + 1) + " " + String.valueOf(end.y - start.y + 1));
            bw.newLine();
            for(int y=start.y;y<=end.y;++y){
                for(int x=start.x;x<=end.x;++x){
                    bw.write(String.valueOf(Get(x,y)) + " ");
                }
                bw.newLine();
            }

            bw.close();
        }catch(IOException e){
            System.out.println(e);
        }
    }

    void RandomGenerate(){
        for(int y=0;y<size.height;++y){
            for(int x=0;x<size.width;++x){
                Set(x, y, random.nextBoolean());
            }
        }
    }

    void Clear(){
        field = new boolean[262144];
    }

    void Flip(Point start, Point end){
        if(start.x > end.x) {
            Point p = new Point();
            p = start;
            start = end;
            end = p;
        }
        for(int y=start.y;y<=end.y;++y){
            for(int x=start.x;x<=end.x;++x){
                Set(x,y,!Get(x,y));
            }
        }
    }

    void Next(){
        boolean[] temp = new boolean[262144];
        for(int y=0;y<size.height;++y){
            for(int x=0;x<size.width;++x){
                int liveCount = 0;//¶‘¶‚µ‚Ä‚¢‚é˜a
                for(int i=0;i<8;++i) {
                    if(Get(x + dx[i], y + dy[i])) liveCount++;
                }
                if(Get(x,y)){
                        temp[y * size.width + x] = (liveCount==2 || liveCount == 3);
                }
                else{
                    temp[y*size.width+x] = (liveCount == 3);
                }
            }
        }
        field = temp.clone();
    }

    public static void main(String[] args){
        Window window = new Window();
    }
}
