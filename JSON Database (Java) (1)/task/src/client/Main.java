package client;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class Main {

    @Parameter(names = {"--requestType", "-t"})
    private static String requestType;

    @Parameter(names = {"--cellIndex", "-i"})
    private static int cellIndex;

    @Parameter(names = {"--message", "-m"})
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

                if (!requestType.equalsIgnoreCase("exit")) {
                    String messageToServer = message != null? message : " ";
                    String request = requestType + " " + cellIndex + " " + messageToServer;
                    output.writeUTF(request);
                    System.out.println("Sent: " + request);
                } else {
                    String request = requestType;
                    output.writeUTF(request);
                    System.out.println("Sent: " + request);
                }
                String serverResponse = input.readUTF();
                System.out.println("Received: " + serverResponse);
            }
    }
}

