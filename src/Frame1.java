import java.awt.AWTException;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import javax.swing.JButton;
import javax.swing.JOptionPane;


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
                        robot.delay(1000*60*10); // 10분
                        String fileName = num + "FullScreenshot." + format;
                        num = num + 1;

                        Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
                        BufferedImage screenFullImage = robot.createScreenCapture(screenRect);

                        // 서버에 파일 전송 시 파일 객체 이용- 이때 이미지가 경로에 저장되어 있어야 한다. - 그냥 파일 이름 써도 될거 같은데?
                        //ImageIO.write(screenFullImage, format, new File(fileName));
                        //System.out.println("A full screenshot saved!"+num);

                        String urlToConnect = "http://13.125.139.100/image";
                        File fileToUpload = new File(fileName);
                        String boundary = Long.toHexString(System.currentTimeMillis());//Just generate some unique

                        URLConnection connection = new URL(urlToConnect).openConnection();
                        connection.setDoOutput(true);
                        connection.setRequestProperty("Content-Type","multipart/form-data; boundary=" + boundary);
                        PrintWriter writer = null;

                        try{
                            writer = new PrintWriter(new OutputStreamWriter(connection.getOutputStream()));
                            writer.println("--" + boundary);
                            writer.println("Content-Disposition: form-data; name=\"picture\"; filename=\"bla.jpg\"");
                            writer.println("Content-Type: image/jpeg");
                            writer.println();
                            BufferedReader reader = null;

                            try{
                                reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileToUpload)));
                                for(String line; (line = reader.readLine()) != null;){
                                    writer.println(line);
                                }

                            }finally {
                                if(reader != null) try {reader.close();} catch(IOException logOrIgnore) {}
                            }
                            writer.println("--");

                        } finally {
                            if(writer != null) writer.close();
                        }

                        int responseCode = ((HttpURLConnection) connection).getResponseCode();
                        if( responseCode == 200) System.out.println("OK");
                        else System.out.println("fail");

                    }// end while

                } catch (AWTException | IOException ex) {
                    System.err.println(ex);
                }
             }}//end Runnable
            ); // end capture_thread

        capture_thread.start();

    }// end startThread




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

    public static void main(String[] args)
    {
        boolean running = false;
        new Frame1();

    }

}