import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        String host = "localhost";
        int port = 8989;
        Scanner scanner = new Scanner(System.in);

        try (Socket clientSocket = new Socket(host, port);
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
            String questionFromServer = in.readLine();
            System.out.println(questionFromServer);
            String answerToServer = scanner.nextLine();
            out.println(answerToServer);
            String uglyJsonFromServer = in.readLine();
            System.out.println(getPrettyJson(uglyJsonFromServer));
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public static String getPrettyJson(String uglyJson) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.setPrettyPrinting().create();
        JsonParser jsonParser = new JsonParser();
        JsonElement jsonElement = jsonParser.parse(uglyJson);
        return gson.toJson(jsonElement);
    }
}
