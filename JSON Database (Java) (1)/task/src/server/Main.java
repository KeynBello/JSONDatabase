package server;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    private static final JSONDatabase jsonDatabase = new JSONDatabase();
    private static final GoodResponse goodResponse = new GoodResponse();
    private static String request;
    private static String cellIndex;
    private static String message;
    private static String fileName;
    private static boolean isServerRunning = true;

    public static void main(String[] args) throws IOException {
        String address = "127.0.0.1";
        int port = 62222;
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        try (ServerSocket server = new ServerSocket(port, 50, InetAddress.getByName(address))) {
            System.out.println("Server started!");
            while (isServerRunning) {
                Socket clientSocket = server.accept();
                handleClient(clientSocket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket clientSocket) throws IOException {
        try (DataInputStream input = new DataInputStream(clientSocket.getInputStream());
             DataOutputStream output = new DataOutputStream(clientSocket.getOutputStream())) {
            String clientMessage = input.readUTF();

            String response = responseFromDatabase(clientMessage);
            output.writeUTF(response);
        }
    }

    private static String responseFromDatabase(String clientMessage) {

        JsonObject messageJson = JsonParser.parseString(clientMessage).getAsJsonObject();

        try {
            request = messageJson.get("type").getAsString();
        } catch (Exception ignored) {
        }

        try {
            cellIndex = messageJson.get("key").getAsString();
        } catch (Exception ignored) {
        }

        try {
            message = messageJson.get("value").getAsString();
        } catch (Exception ignored) {
        }

        try {
            fileName = messageJson.get("path").getAsString();
        } catch (Exception ignored) {
        }

        switch (request) {
            case "get" -> clientMessage = jsonDatabase.get(cellIndex);
            case "set" -> clientMessage = jsonDatabase.set(cellIndex, message);
            case "delete" -> clientMessage = jsonDatabase.delete(cellIndex);
            case "exit" -> {
                clientMessage = goodResponse.getOkResponse();
                isServerRunning = false;
            }
        }
        return clientMessage;
    }
}
