import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Main {

    public static void main(String[] args) throws Exception
    {
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
        System.out.println("Newest commit appeared on repo named: " + name[1]);

    }
}
