/**
 * Created by koh on 15/06/19 (019).
 */

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class Window extends JPanel implements ActionListener, MouseListener, MouseMotionListener{

    private JFrame frame = new JFrame();
    private JPanel buttonPane = new JPanel();
    private JPanel textPane = new JPanel();
    private Field field = new Field();

    private Timer timer = new Timer(100,this);

    private JButton randomButton;
    private JButton nextButton;
    private JButton startButton;
    private JButton stopButton;

    private JLabel xLabel;
    private JTextField xTextField;
    private JLabel yLabel;
    private JTextField yTextField;
    private JButton readButton;
    private JButton clearButton;

    private MyMouse mouse = new MyMouse();

    private JMenuBar menubar = new JMenuBar();
    private JMenu   menuFile = new JMenu("File");
    private JMenuItem   menuItemSave   = new JMenuItem("Save");
    private JMenuItem   menuItemLoad   = new JMenuItem("Load");

    private JMenu   menuHelp = new JMenu("Help");
    private JMenuItem   menuItemAbout = new JMenuItem("About");

    private boolean isStop = false;

    private boolean isSaveMode = false;

    public Window()
    {

        frame.getContentPane().add(this);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.pack();
        frame.setSize(800, 600);
        frame.setTitle("Life Game");
        frame.getRootPane().setDoubleBuffered(true);
        frame.setVisible(true);

        randomButton = new JButton("Random Generate");
        randomButton.addActionListener(this);
        nextButton = new JButton("Next");
        nextButton.addActionListener(this);
        startButton = new JButton("Start");
        startButton.addActionListener(this);
        stopButton = new JButton("Stop");
        stopButton.addActionListener(this);
        clearButton = new JButton("Clear");
        clearButton.addActionListener(this);

        buttonPane.add(randomButton);
        buttonPane.add(nextButton);
        buttonPane.add(startButton);
        buttonPane.add(stopButton);
        buttonPane.add(clearButton);

        textPane.setLayout(new BoxLayout(textPane, BoxLayout.Y_AXIS));

        xLabel = new JLabel("x value");
        xTextField = new JTextField(10);
        yLabel = new JLabel("y value");
        yTextField = new JTextField(10);
        readButton = new JButton("Reflection");
        readButton.addActionListener(this);

        textPane.add(xLabel);
        textPane.add(xTextField);
        textPane.add(yLabel);
        textPane.add(yTextField);
        textPane.add(readButton);

        xTextField.setText(String.valueOf(field.size.width));
        yTextField.setText(String.valueOf(field.size.height));

        menubar.add(menuFile);
        menuFile.add(menuItemSave);
        menuFile.add(menuItemLoad);
        menubar.add(menuHelp);
        menuHelp.add(menuItemAbout);

        menuItemSave.addActionListener(this);
        menuItemLoad.addActionListener(this);
        menuItemAbout.addActionListener(this);

        frame.getContentPane().add(buttonPane, BorderLayout.SOUTH);
        frame.getContentPane().add(textPane, BorderLayout.EAST);

        frame.setJMenuBar(menubar);

        timer.start();

        addMouseListener(this);
        addMouseMotionListener(this);

        frame.repaint();
    }

    public void actionPerformed(ActionEvent event){
        if(event.getSource() == randomButton){
            field.RandomGenerate();
            frame.repaint();
        }
        if(event.getSource() == nextButton){
            field.Next();
            frame.repaint();
        }
        if(event.getSource() == timer){
            if(!isStop)
                field.Next();
            frame.repaint();
        }
        if(event.getSource() == startButton){
            isStop = false;
        }
        if(event.getSource() == stopButton){
            isStop = true;
        }
        if(event.getSource() == readButton){
            field.SetSize(Integer.parseInt(xTextField.getText()), Integer.parseInt(yTextField.getText()));
        }
        if(event.getSource() == clearButton){
            field.Clear();
        }
        if(event.getSource() == menuItemSave){
            isSaveMode = true;
            isStop = true;
        }
        if(event.getSource() == menuItemLoad){
            isStop = true;
            field.Load();
        }
    }

    public void mouseEntered(MouseEvent e){}
    public void mouseExited(MouseEvent e){}
    public void mousePressed(MouseEvent e){
        if(e.getButton() == MouseEvent.BUTTON1)
            mouse.isLPush = true;
        else if(e.getButton() == MouseEvent.BUTTON3)
            mouse.isRPush = true;
        mouse.point.x = e.getPoint().x;
        mouse.point.y = e.getPoint().y;
        mouse.pushPoint.x = e.getPoint().x;
        mouse.pushPoint.y = e.getPoint().y;
    }
    public void mouseReleased(MouseEvent e){
        if(e.getButton() == MouseEvent.BUTTON1) {
            mouse.isLPush = false;
            mouse.isLReleased = true;
        }
        else if(e.getButton() == MouseEvent.BUTTON3){
            mouse.isRPush = false;
            mouse.isRReleased = true;
        }
        mouse.point.x = e.getPoint().x;
        mouse.point.y = e.getPoint().y;
    }
    public void mouseClicked(MouseEvent e){}

    public void mouseMoved(MouseEvent e){
        if(e.getButton() == MouseEvent.BUTTON1)
            mouse.isLPush = false;
        else if(e.getButton() == MouseEvent.BUTTON3)
            mouse.isRPush = false;
        mouse.point.x = e.getPoint().x;
        mouse.point.y = e.getPoint().y;
    }
    public void mouseDragged(MouseEvent e){
        if(e.getButton() == MouseEvent.BUTTON1)
            mouse.isLPush = true;
        else if(e.getButton() == MouseEvent.BUTTON3)
            mouse.isRPush = true;
        mouse.point.x = e.getPoint().x;
        mouse.point.y = e.getPoint().y;
        frame.repaint();
    }

    public void paintComponent(Graphics g0){
        Graphics2D g = (Graphics2D) g0;
        xTextField.setText(String.valueOf(field.size.width));
        yTextField.setText(String.valueOf(field.size.height));
        int sellSize = this.getSize().height/field.size.height;

        for(int y=0;y<field.size.height;++y) {
            for(int x=0;x<field.size.width;++x) {
                if(!field.Get(x,y)) {
                    g.setColor(new Color(0, 0, 0));
                    g.fillRect(x * sellSize, y * sellSize, sellSize, sellSize);
                }
            }
        }

        if(isSaveMode) {
            if(mouse.isLPush) {
                g.setColor(new Color(255, 0, 27, 100));
                g.fillRect(mouse.pushPoint.x, mouse.pushPoint.y, mouse.point.x - mouse.pushPoint.x, mouse.point.y - mouse.pushPoint.y);
                g.setColor(new Color(255, 0, 27, 200));
                g.drawRect(mouse.pushPoint.x, mouse.pushPoint.y, mouse.point.x - mouse.pushPoint.x, mouse.point.y - mouse.pushPoint.y);
            }
            if(mouse.isLReleased){
                mouse.isLReleased = false;
                field.Save(new Point(mouse.pushPoint.x / sellSize, mouse.pushPoint.y / sellSize), new Point(mouse.point.x / sellSize, mouse.point.y / sellSize));
            }
        }
        else {
            if (mouse.isRPush) {
                g.setColor(new Color(255, 0, 27, 100));
                g.fillRect(mouse.pushPoint.x, mouse.pushPoint.y, mouse.point.x - mouse.pushPoint.x, mouse.point.y - mouse.pushPoint.y);
                g.setColor(new Color(255, 0, 27, 200));
                g.drawRect(mouse.pushPoint.x, mouse.pushPoint.y, mouse.point.x - mouse.pushPoint.x, mouse.point.y - mouse.pushPoint.y);
            }

            if (mouse.isRReleased) {
                field.Flip(new Point(mouse.pushPoint.x / sellSize, mouse.pushPoint.y / sellSize), new Point(mouse.point.x / sellSize, mouse.point.y / sellSize));
                mouse.isRReleased = false;
            }

            if (mouse.isLPush) {
                field.Set(mouse.point.x / sellSize, mouse.point.y / sellSize, true);
            }
        }

        g.dispose();
    }
}
