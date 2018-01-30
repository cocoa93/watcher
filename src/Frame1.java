import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class Frame1 extends Frame implements ActionListener
{
    JButton btn1 = new JButton("start");
    JButton btn2 = new JButton("stop");
    boolean running = false;


    public Frame1()
    {

        super("캡처프로그램");
        this.setLayout(new FlowLayout());

        this.add(btn1);
        this.add(btn2);

        this.setSize(300, 200);
        this.setVisible(true);

        btn1.addActionListener(this);
        btn2.addActionListener(this);

    }

    Thread capture_thread = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                running = true;
                System.out.print("running\n");
                int num = 1;

                while (running) {
                    Robot robot = new Robot();
                    robot.delay(1000);
                    String format = "jpg";
                    String fileName = num + "FullScreenshot." + format;
                    num = num + 1;

                    Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
                    BufferedImage screenFullImage = robot.createScreenCapture(screenRect);
                    ImageIO.write(screenFullImage, format, new File(fileName));

                    System.out.println("A full screenshot saved!");
                }

            } catch (AWTException | IOException ex) {
                System.err.println(ex);
            }
        }});


    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource().equals(btn1)) {
            if (!capture_thread.isAlive()){
                capture_thread.start();}
            else{
                running=true;
            }
        }
        else
        {
            running=false;
            System.out.print("stop\n");
            JOptionPane.showMessageDialog(this, "종료되었습니다.");
        }
    }

    public static void main(String[] args)
    {
        boolean running = false;
        new Frame1();

    }

}