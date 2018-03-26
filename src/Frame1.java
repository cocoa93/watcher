import java.awt.*;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class Frame1 extends Frame implements ActionListener
{
    JButton btn1 = new JButton("start");
    JButton btn2 = new JButton("stop");
    JButton btnLabel = new JButton("id입력");
    JTextField idField = new JTextField(BorderLayout.CENTER);
    JLabel idLabel = new JLabel();
    boolean running = false;
    int id;


    public Frame1()
    {

        super("캡처프로그램");
        this.setLayout(new FlowLayout());

        this.add(idField);
        this.add(btnLabel);

        this.add(btn1);
        this.add(btn2);
        this.add(idLabel);

        this.setSize(300, 200);
        this.setVisible(true);

        btn1.addActionListener(this);
        btn2.addActionListener(this);
        btnLabel.addActionListener(this::actionPerformed2);
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
                        robot.delay(30000);
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

    public static void main(String[] args) throws Exception
    {

        boolean running = false;
        new Frame1();

        //보낼 서버 아이피
        String serverIP = "localhost";
        Socket socket = null;

        try {
            socket = new Socket(serverIP,80);//소켓 생성
            SenderThread sender = new SenderThread(socket);//스레드 생성
            sender.start();//시작


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void actionPerformed2(ActionEvent e) {
        id = Integer.parseInt(idField.getText());
        idLabel.setText(idField.getText());
    }
}