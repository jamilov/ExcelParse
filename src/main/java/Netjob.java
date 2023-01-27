package Project;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.swing.JOptionPane;

public class Netjob {
    public static String newqueryget(String query)throws ArrayIndexOutOfBoundsException{
        int p=0;
        HttpURLConnection connection = null;
        String newquery="https://zachestnyibiznes.ru";
        try {
            connection = (HttpURLConnection) new URL(query).openConnection();
            connection.setRequestMethod("GET");
            connection.setUseCaches(false);
            connection.setConnectTimeout(1000);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            connection.connect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_MOVED_TEMP
                    || connection.getResponseCode() == HttpURLConnection.HTTP_MOVED_PERM) {
                while (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    String location = connection.getHeaderField("Location");
                    URL newUrl = new URL(location);
                    connection = (HttpURLConnection) newUrl.openConnection();
                    connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
                }
            }
            if (HttpURLConnection.HTTP_OK == connection.getResponseCode()) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                while((line = in.readLine()) != null) {
                    if(p==1){
                        String[] mass = line.split("'");
                        newquery += mass[1];
                        break;
                    }
                    if(line.contains("<td data-th=")){
                        p++;
                    }
                }
            }else {
                JOptionPane.showMessageDialog(null,("fail: newQueryGet "+ connection.getResponseCode() + ", " + connection.getResponseMessage()));
            }
        }catch (Throwable cause) {
            cause.printStackTrace();
        }finally {
            if(connection!=null){
                connection.disconnect();
            }
        }
        return newquery;
    }


    public static String result(String query) throws ArrayIndexOutOfBoundsException{
        int p=0;
        HttpURLConnection connection = null;
        String newquery="";
        try {
            connection = (HttpURLConnection) new URL(query).openConnection();
            connection.setRequestMethod("GET");
            connection.setUseCaches(false);
            connection.setConnectTimeout(1000);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            connection.connect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_MOVED_TEMP
                    || connection.getResponseCode() == HttpURLConnection.HTTP_MOVED_PERM) {
                while (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    String location = connection.getHeaderField("Location");
                    URL newUrl = new URL(location);
                    connection = (HttpURLConnection) newUrl.openConnection();
                    connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
                }
            }
            if (HttpURLConnection.HTTP_OK == connection.getResponseCode()) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                while((line = in.readLine()) != null) {
                    if(p==1){
                        String[] mass = line.split("[<>]");
                        for(int j=0;j<mass.length;j++)
                            if(isNumeric(mass[j]))
                                newquery += mass[j];
                        break;
                    }
                    if(line.contains("<td data-th=")){
                        p++;
                    }
                }
            }else {
                JOptionPane.showMessageDialog(null,("fail: Result "+ connection.getResponseCode() + ", " + connection.getResponseMessage()));
            }
        }catch (Throwable cause) {
            cause.printStackTrace();
        }finally {
            if(connection!=null){
                connection.disconnect();
            }
        }
        return newquery;
    }

    public static String ipcheck(String query) throws ArrayIndexOutOfBoundsException{
        int p=0;
        HttpURLConnection connection = null;
        String newquery = "";
        try {
            connection = (HttpURLConnection) new URL(query).openConnection();
            connection.setRequestMethod("GET");
            connection.setUseCaches(false);
            connection.setConnectTimeout(1000);
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            connection.connect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_MOVED_TEMP
                    || connection.getResponseCode() == HttpURLConnection.HTTP_MOVED_PERM) {
                while (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    String location = connection.getHeaderField("Location");
                    URL newUrl = new URL(location);
                    connection = (HttpURLConnection) newUrl.openConnection();
                    connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
                }
            }
            if (HttpURLConnection.HTTP_OK == connection.getResponseCode()) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                while((line = in.readLine()) != null) {
                    if(p==3){
                        String[] mass = line.split("[<>]");
                        for(int j=0;j<mass.length;j++){
                            if(isNumeric(mass[j])){
                                newquery += mass[j];
                            }
                        }
                        break;
                    }
                    if(line.contains("<td data-th=")){
                        p++;
                    }
                }
            }else {
                JOptionPane.showMessageDialog(null,("fail: ipCheck"+ connection.getResponseCode() + ", " + connection.getResponseMessage()));
            }
        }catch (Throwable cause) {
            cause.printStackTrace();
        }finally {
            if(connection!=null){
                connection.disconnect();
            }
        }
        String[] result = newquery.split(" ");
        return result[0];
    }

    public static boolean isNumeric(String str)
    {
        try
        {
            double d = Double.parseDouble(str);
        }
        catch(NumberFormatException nfe)
        {
            return false;
        }
        return true;
    }
}