package client;

import java.util.Scanner;

public class Main implements Runnable{
    public static final Scanner SCANNER = new Scanner(System.in);

    public static void main(String[] args) {
    }

    @Override
    public void run() {
        JSONDatabase jsonDatabase = new JSONDatabase();

        boolean isRun = true;
        while (isRun) {
            String command = SCANNER.nextLine();
            String[] option = command.split(" ");


            StringBuilder sb = new StringBuilder();
            try {
                if (!option[2].isEmpty()) {
                    for (int i = 2; i < jsonDatabase.getData().size() - 1; i++) {
                        sb.append(option[i] + " ");
                    }
                }
            } catch (Exception ignored) {
            }
            String text = sb.toString();

            int cellIndex = 0;
            if (option.length > 1 && !option[1].isEmpty()) {
                 cellIndex = Integer.parseInt(option[1]) - 1;
            }

            switch (option[0]) {
                case "get" -> jsonDatabase.get(cellIndex);
                case "set" -> jsonDatabase.set(cellIndex, text);
                case "delete" -> jsonDatabase.delete(cellIndex);
                case "exit" -> isRun = false;
            }
        }
    }
}
