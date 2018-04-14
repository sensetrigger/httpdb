import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.net.Socket;
import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

import com.sensetrigger.jsonparser.Parser;

public class HTTPServer {

    public static void main(String[] args) throws IOException {
        Properties prop = new Properties();
        InputStream input = null;
        int port = 0;
        String location;

        try {
            input = new FileInputStream("config.properties");
            prop.load(input);
            port = Integer.parseInt(prop.getProperty("PORT"));
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        ServerSocket ss = new ServerSocket(port);
        System.out.println("Started: " + ss);
        try {
            Socket socket = ss.accept();
            try {
                while (true) {
                    socket = ss.accept();
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                    String s = in.readLine();
                    if (s != null && s.contains("HTTP/1.1")) {
                        s = s.substring(4, s.indexOf("HTTP/1.1"));
                        response(s, out);
                    }
                }
            }
            finally {
                System.out.println("closing...");
                socket.close();
            }
        }
        finally {
            ss.close();
        }
    }

    private static void response(String s, BufferedWriter writer) {
        Parser jsonParser = new Parser();
        Properties prop = new Properties();
        InputStream input = null;
        try {
            input = new FileInputStream("config.properties");
            prop.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String dir = new File("").getAbsolutePath()+"/"+prop.getProperty("LOCATION")+"/";

        if (s.startsWith("/user/list")) {
            ArrayList<String> filelist= getUsers(dir);
            String query = jsonParser.parser(filelist);
            try {
                String response = String.format("HTTP/1.1 %s\r\n" +
            "Date: %s\r\nServer: java/1.8\r\nContent-Type: %s\r\nContent-Length: %s\r\nConnection: keep-alive\r\n\n%s", "200 OK", getTime(), "text/html", query.length(), query);
                writer.write(response);
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (s.startsWith("/user/create?")) {
            String name = s.substring(s.indexOf("name")+5, s.indexOf("age")-1);
            String age = s.substring(s.indexOf("age")+4, s.indexOf("salary")-1);
            String salary = s.substring(s.indexOf("salary")+7, s.length()-1);
            User user = new User(name, Integer.parseInt(age), Double.parseDouble(salary));
            String id = getID(dir);
            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(dir+id+".bin"))) {
                out.writeObject(user);
                String response = String.format("HTTP/1.1 %s\r\n" +
                        "Date: %s\r\nServer: java/1.8\r\nContent-Type: %s\r\nContent-Length: %s\r\nConnection: keep-alive\r\n\n%s", "200 OK", getTime(), "text/html", id.length(), id);
                writer.write(response);
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (s.startsWith("/user/delete/")) {
            String id = s.substring(13,s.length()-1);
            try {
                File f = new File(dir + id + ".bin");
                if (f.exists()) {
                    Files.delete(Paths.get(dir + id + ".bin"));
                    String response = String.format("HTTP/1.1 %s\r\n" +
                            "Date: %s\r\nServer: java/1.8\r\nContent-Type: %s\r\nContent-Length: %s\r\nConnection: keep-alive\r\n\n%s", "200 OK", getTime(), "text/html", 0, "");
                    writer.write(response);
                    writer.flush();
                }
                else {
                    String response = String.format("HTTP/1.1 %s\r\n" +
                            "Date: %s\r\nServer: java/1.8\r\nContent-Type: %s\r\nContent-Length: %s\r\nConnection: keep-alive\r\n\n%s", "404 Not Found", getTime(), "text/html", 0, "");
                    writer.write(response);
                    writer.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (s.startsWith("/user/")) {
            String id = s.substring(6,s.length()-1);
            String res = "";
            try {
                File f = new File(dir + id + ".bin");
                if (f.exists()) {
                    FileInputStream file = new FileInputStream(dir + id + ".bin");
                    ObjectInputStream in = new ObjectInputStream(file);
                    try {
                        User obj = (User) in.readObject();
                        res = jsonParser.parser(obj);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    String response = String.format("HTTP/1.1 %s\r\n" +
                            "Date: %s\r\nServer: java/1.8\r\nContent-Type: %s\r\nContent-Length: %s\r\nConnection: keep-alive\r\n\n%s", "200 OK", getTime(), "text/html", res.length(), res);
                    writer.write(response);
                    writer.flush();
                }
                else {
                    String response = String.format("HTTP/1.1 %s\r\n" +
                            "Date: %s\r\nServer: java/1.8\r\nContent-Type: %s\r\nContent-Length: %s\r\nConnection: keep-alive\r\n\n%s", "404 Not Found", getTime(), "text/html", 0, "");
                    writer.write(response);
                    writer.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static ArrayList<String> getUsers(String dir) {
        File curDir = new File(dir);
        File[] filesList = curDir.listFiles();
        ArrayList<String> filelist= new ArrayList<>();
        for (File f : filesList) {
            if (f.getName().contains("bin") && f.getName().length() > 4)
                filelist.add(f.getName());
        }
        return filelist;
    }

    private static String getID(String dir) {
        ArrayList<String> users = getUsers(dir);
        if (users.size() == 0) return "1";
        ArrayList<Integer> id = new ArrayList<>();
        if (users.size() > 0) {
            for (int i = 0; i < users.size(); i++)
                id.add(Integer.parseInt(users.get(i).substring(0, users.get(i).indexOf("bin") - 1)));
            for (int i = 0; i < id.size() - 1; i++) {
                if (id.get(i) != id.get(i + 1) - 1)
                    return Integer.toString(id.get(i + 1) - 1);
            }
        }
        return Integer.toString(id.get(id.size() - 1) + 1);
    }
    private static String getTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        Calendar calendar = Calendar.getInstance();
        return dateFormat.format(calendar.getTime());
    }
}