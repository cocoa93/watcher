import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;

import java.awt.AWTException;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
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
                    int i = 1000 ; // Delay must be from 0 to 60000
                    while (running) {
                        robot.delay(i);
                        String fileName = num + "FullScreenshot." + format;
                        num = num + 1;

                        Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
                        BufferedImage screenFullImage = robot.createScreenCapture(screenRect);

                        // 서버에 파일 전송 시 파일 객체 이용- 이때 이미지가 경로에 저장되어 있어야 한다.
                        File dir = new File("C:/saveImage");
                        dir.mkdir();
                        ImageIO.write(screenFullImage, format, new File(dir, fileName));
                        System.out.println("A full screenshot saved!"+num);

                        HttpClient httpClient = new DefaultHttpClient();
                        httpClient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);

                        HttpPost httppost = new HttpPost("http://13.125.189.21/image");
                        File file = new File("C:/saveImage/"+fileName);

                        MultipartEntity mpEntity = new MultipartEntity();
                        ContentBody cbFile = new FileBody(file,"image/jpeg");
                        mpEntity.addPart("userfile",cbFile);

                        httppost.setEntity(mpEntity);
                        System.out.println("executing request " + httppost.getRequestLine());
                        HttpResponse response = httpClient.execute(httppost);
                        HttpEntity  resEntity = response.getEntity();

                        System.out.println(response.getStatusLine());
                        if(resEntity != null) System.out.println(EntityUtils.toString(resEntity));
                        if(resEntity != null) resEntity.consumeContent();

                        httpClient.getConnectionManager().shutdown();



                    }// end while

                } catch (AWTException|IOException ex) {
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