import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import com.fazecast.jSerialComm.SerialPort;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
public class Main {

    static SerialPort chosenPort;

    public static void main(String[] args) throws Exception {

        // create and configure the window
        JFrame window = new JFrame();
        window.setTitle("LCD Clock");
        window.setSize(400, 75);
        window.setLayout(new BorderLayout());
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // create a drop-down box and connect button, then place them at the top of the window
        JComboBox<String> portList = new JComboBox<String>();
        JButton connectButton = new JButton("Connect");
        JPanel topPanel = new JPanel();
        topPanel.add(portList);
        topPanel.add(connectButton);
        window.add(topPanel, BorderLayout.NORTH);

        //get name from api
        String url = "https://api.github.com/users/PUT-PTM/events";
        URL adr = new URL(url);
        HttpURLConnection connect = (HttpURLConnection) adr.openConnection();
        connect.addRequestProperty("User-Agent", "Mozilla/5.0");
        BufferedReader in = new BufferedReader(new InputStreamReader(connect.getInputStream()));


        StringBuilder response = new StringBuilder();
        String line;
        while ( ( line = in.readLine() ) != null)
        {
            response.append("\n" + line);
            //System.out.println(line);
        }
        in.close();


        String [] temp = response.toString().split("\"type\":\"PushEvent\"");

        String temp2[] = temp[1].split("\"repo\":");

        String temp3[] = temp2[1].split("\"url\":");

        String temp4[] = temp3[1].split("\"");

        String URLout = temp4[1];
        //System.out.println(URLout);

        String name[] = URLout.split("PUT-PTM/");
        System.out.println(name[1]);

        String name1 = name[1];


        // populate the drop-down box
        SerialPort[] portNames = SerialPort.getCommPorts();
        for(int i = 0; i < portNames.length; i++)
            portList.addItem(portNames[i].getSystemPortName());

        // configure the connect button and use another thread to send data
        connectButton.addActionListener(new ActionListener(){
            @Override public void actionPerformed(ActionEvent arg0) {
                if(connectButton.getText().equals("Connect")) {
                    // attempt to connect to the serial port
                    chosenPort = SerialPort.getCommPort(portList.getSelectedItem().toString());
                    chosenPort.setComPortTimeouts(SerialPort.TIMEOUT_SCANNER, 0, 0);
                    if(chosenPort.openPort()) {
                        connectButton.setText("Disconnect");
                        portList.setEnabled(false);


                        // create a new thread for sending data to the arduino
                        Thread thread = new Thread(){
                            @Override public void run() {
                                // wait after connecting, so the bootloader can finish
                                try {Thread.sleep(1000); } catch(Exception e) {}
                                // enter an infinite loop that sends text to the arduino
                                PrintWriter output = new PrintWriter(chosenPort.getOutputStream());
                                while(true) {
                                    output.print(name1);
                                    output.flush();
                                    try {Thread.sleep(100); } catch(Exception e) {}
                                }
                            }
                        };
                        thread.start();
                    }
                } else {
                    // disconnect from the serial port
                    chosenPort.closePort();
                    portList.setEnabled(true);
                    connectButton.setText("Connect");
                }
            }
        });

        // show the window
        window.setVisible(true);
    }

}