import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import javax.swing.*;

import com.fazecast.jSerialComm.SerialPort;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.json.*;
import java.util.Timer;
public class Main {

    static SerialPort chosenPort;

    public static void main(String[] args) throws Exception {

        // create and configure the window
        JFrame window = new JFrame();
        window.setTitle("Gitter");
        window.setSize(200, 110);
        window.setLayout(new BorderLayout());
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // create a drop-down box and connect button, then place them at the top of the window
        JComboBox<String> portList = new JComboBox<String>();
        JButton connectButton = new JButton("Connect");
        JPanel topPanel = new JPanel();
        JTextArea txtField = new JTextArea("e.g. PUT-PTM");
        topPanel.setLayout(new BorderLayout());
        topPanel.add(portList, BorderLayout.NORTH);
        topPanel.add(txtField, BorderLayout.CENTER);
        topPanel.add(connectButton, BorderLayout.SOUTH);
        window.add(topPanel, BorderLayout.NORTH);

        //get name from api





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


                        // create a new thread for sending data to the STM
                        Thread thread = new Thread(){
                            @Override public void run() {
                                // wait after connecting, so the bootloader can finish
                                try {
                                    Thread.sleep(1000);
                                } catch (Exception e) {
                                }
                                while (true) {
                                    String url = "";
                                    if(txtField.getText() != null)
                                    {
                                        url = "https://api.github.com/users/" + txtField.getText() + "/events";
                                        //url = "https://api.github.com/users/PUT-PTM/events";
                                    }

                                    URL adr = null;
                                    try {
                                        adr = new URL(url);
                                    } catch (MalformedURLException e) {
                                        e.printStackTrace();
                                    }
                                    HttpURLConnection connect = null;
                                    try {
                                        connect = (HttpURLConnection) adr.openConnection();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    connect.addRequestProperty("User-Agent", "Mozilla/5.0");
                                    BufferedReader in = null;
                                    try {
                                        in = new BufferedReader(new InputStreamReader(connect.getInputStream()));
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }


                                    StringBuffer response = new StringBuffer();
                                    String line = "";
                                    while (true) {
                                        try {
                                            if (!((line = in.readLine()) != null)) break;
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        response.append("\n" + line);
                                        //System.out.println(line);
                                    }
                                    try {
                                        in.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }


                                    JSONArray arr = new JSONArray(response.toString());
                                    JSONObject repo = arr.getJSONObject(0);
                                    repo.getJSONObject("repo").getString("name");
                                    System.out.println(repo.getString("created_at"));
                                    String name[] = repo.getJSONObject("repo").getString("name").split("/");
                                    String name1 = name[1];
                                    if(name1.length() < 16)
                                    {
                                        for(int i = 0; i < (16 - name1.length()); i++)
                                        {
                                            name1 = name1 + " ";
                                        }
                                    }
                                    String msg = name1 + repo.getString("created_at");
                                    System.out.println(name[1]);
                                    // enter an infinite loop that sends text to the stm

                                    PrintWriter output = new PrintWriter(chosenPort.getOutputStream());
                                    output.print(name1);
                                    output.flush();

                                    try {
                                        Thread.sleep(60*3 * 1000);
                                    } catch (Exception e) {
                                    }

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