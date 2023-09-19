package server;

public class Main {

    public static void main(String[] args) {
        Runnable program = new client.Main();
        Thread main = new Thread(program);

        main.run();
    }
}
