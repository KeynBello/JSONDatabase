package client;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Main {

    @Parameter(names = {"--type", "-t"}, description = "type")
    private static String requestType;

    @Parameter(names = {"--key", "-k"}, description = "key")
    private static String cellIndex;

    @Parameter(names = {"--value", "-v"}, description = "value")
    private static String message;

    public static void main(String[] args) throws IOException {
        Main main = new Main();
        JCommander.newBuilder()
                .addObject(main)
                .build()
                .parse(args);
        run();
    }

    private static void run() throws IOException {

        String address = "127.0.0.1";
        int port = 62222;

            try (Socket socket = new Socket(address, port);
                 DataInputStream input = new DataInputStream(socket.getInputStream());
                 DataOutputStream output = new DataOutputStream(socket.getOutputStream())) {
                System.out.println("Client started!");

                JsonObject jsonRequest = new JsonObject();
                jsonRequest.addProperty("type", requestType);
                if (cellIndex != null) {
                    jsonRequest.addProperty("key", cellIndex);
                }
                if (message != null) {
                    jsonRequest.addProperty("value", message);
                }

                if (!requestType.equalsIgnoreCase("exit")) {
                    String request = jsonRequest.toString();
                    output.writeUTF(request);
                    System.out.println("Sent: " + request);
                } else {
                    String request = jsonRequest.toString();
                    output.writeUTF(request);
                    System.out.println("Sent: " + request);
                }

                String serverResponse = input.readUTF();
                JsonObject responseJson = JsonParser.parseString(serverResponse).getAsJsonObject();

                responseJson.addProperty("response", serverResponse);
                String responseType = responseJson.get("response").getAsString();
                System.out.println("Received:" + responseType);

            }
    }
}

