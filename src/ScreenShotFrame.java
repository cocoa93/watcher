import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

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
        this.setLayout(new BorderLayout());
        JPanel p1 = new JPanel();
        p1.setLayout(new GridLayout(1,2));
        JPanel p2 = new JPanel();
        p2.setLayout(new FlowLayout());
        p1.add(btn1);
        p1.add(btn2);
        p2.add(greetingLabel);

        add(p1, BorderLayout.SOUTH);
        add(p2, BorderLayout.NORTH);

        this.setSize(300, 100);
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
            this.greetingLabel.setText("캡처중입니다");
        }
        else
        {
            running = false;
            System.out.print("stop\n");
            this.greetingLabel.setText("캡처를 정지합니다");
            JOptionPane.showMessageDialog(this, "종료되었습니다.");
        }
    }

}