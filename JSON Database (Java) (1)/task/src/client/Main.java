package client;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import server.JSONDatabase;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Main {

    private static final JSONDatabase jsonDatabase = new JSONDatabase();
    @Parameter(names = {"--type", "-t"}, description = "type")
    private static String requestType;
    @Parameter(names = {"--key", "-k"}, description = "key")
    private static String cellIndex;
    @Parameter(names = {"--value", "-v"}, description = "value")
    private static String message;
    @Parameter(names = {"--file", "-in"}, description = "file")
    private static String inputFilePath;

    public static void main(String[] args) throws IOException {
        Main main = new Main();
        JCommander.newBuilder().addObject(main).build().parse(args);
        run();
    }

    private static void run() throws IOException {

        String address = "127.0.0.1";
        int port = 62222;

        try (Socket socket = new Socket(address, port); DataInputStream input = new DataInputStream(socket.getInputStream()); DataOutputStream output = new DataOutputStream(socket.getOutputStream())) {
            System.out.println("Client started!");

            String jsonRequest = getStringJsonObject();
            output.writeUTF(jsonRequest);
            System.out.println("Sent: " + jsonRequest);

            String serverResponse = input.readUTF();
            JsonObject responseJson = JsonParser.parseString(serverResponse).getAsJsonObject();
            responseJson.addProperty("response", serverResponse);
            String responseType = responseJson.get("response").getAsString();
            System.out.println("Received:" + responseType);

        }
    }

    private static String getStringJsonObject() {
        JsonObject jsonRequest = new JsonObject();

        if (inputFilePath != null) {
            String jsonFromFile = jsonDatabase.loadDatabaseFromFile(inputFilePath);
            JsonObject stringToJson = JsonParser.parseString(jsonFromFile).getAsJsonObject();
            return stringToJson.toString();
        } else {
            if ("exit".equalsIgnoreCase(requestType)) {
                jsonRequest.addProperty("type", requestType);
                return jsonRequest.toString();
            } else {
                if (requestType != null) {
                    jsonRequest.addProperty("type", requestType);
                }

                if (cellIndex != null) {
                    jsonRequest.addProperty("key", cellIndex);
                }

                if (message != null) {
                    jsonRequest.addProperty("value", message);
                }
            }
        }
        return jsonRequest.toString();
    }
}

