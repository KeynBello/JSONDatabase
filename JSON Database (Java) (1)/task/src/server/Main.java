package server;

import com.beust.jcommander.Parameter;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    @Parameter(names = {"--OK", "-OK"}, description = "response")
    private static final String OK_RESPONSE = "OK";
    @Parameter(names = {"--ERROR", "-ERROR"}, description = "response")
    private static final String ERROR_RESPONSE = "ERROR";
    private static String request;
    private static String cellIndex;
    private static String message;

    private static boolean isServerRunning = true;
    private static JSONDatabase jsonDatabase = new JSONDatabase();



    public static void main(String[] args) throws IOException {
        String address = "127.0.0.1";
        int port = 62222;

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
        try (DataInputStream input = new DataInputStream(clientSocket.getInputStream()); DataOutputStream output = new DataOutputStream(clientSocket.getOutputStream())) {


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
        } catch (Exception ignored){
        }
        GoodResponse goodResponse = new GoodResponse();

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

//    private static HashMap<Integer, Object> messageSplitter(String clientMessage) {
//        String[] option = clientMessage.split(" ");
//        clientRequest = option[0];
//
//        if (option.length > 1) {
//            try {
//                if (!option[1].isEmpty()) {
//                    clientCellIndex = Integer.parseInt(option[1]);
//                }
//            } catch (Exception ignored) {
//            }
//
//            StringBuilder sb = new StringBuilder();
//            try {
//                if (!option[2].isEmpty()) {
//                    for (int i = 2; i < clientMessage.length() - 1; i++) {
//                        sb.append(option[i] + " ");
//                    }
//                }
//            } catch (Exception ignored) {
//            }
//            clientMessageValue = sb.toString();
//        }
//        HashMap<Integer, Object> list = new HashMap<>();
//        list.put(1, clientRequest);
//        list.put(2, clientCellIndex);
//        list.put(3, clientMessageValue);
//
//        return list;
//    }
}
