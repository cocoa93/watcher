import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class ScreenShotFrame extends Frame implements ActionListener
{
    JButton btn1 = new JButton("start");
    JButton btn2 = new JButton("stop");
    JLabel greetingLabel = new JLabel("aa");


    boolean running = false;


    public ScreenShotFrame()
    {

        super("캡처프로그램");
        this.setLayout(new FlowLayout());

        this.add(btn1);
        this.add(btn2);

        this.setSize(300, 200);
        this.setVisible(false);

        btn1.addActionListener(this);
        btn2.addActionListener(this);

    }
    public void startThread(){

        Thread capture_thread = new Thread(new Runnable() {
            @Override
            public synchronized void run() {
                try {
                    running = true;
                    System.out.print("running\n");
                    int num = 1;
                    Robot robot = new Robot();
                    String format = "jpg";

                    while (running) {
                        robot.delay(1000);
                        String fileName = num + "FullScreenshot." + format;
                        num = num + 1;

                        Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
                        BufferedImage screenFullImage = robot.createScreenCapture(screenRect);
                        ImageIO.write(screenFullImage, format, new File(fileName));

                        System.out.println("A full screenshot saved!"+num);
                    }

                } catch (AWTException | IOException ex) {
                    System.err.println(ex);
                }
            }}
            );
        capture_thread.start();
    }


    @Override
    public synchronized void actionPerformed(ActionEvent e)
    {
        if (e.getSource().equals(btn1)) {
            startThread();
        }
        else
        {
                running = false;
                System.out.print("stop\n");
                JOptionPane.showMessageDialog(this, "종료되었습니다.");
        }
    }

}