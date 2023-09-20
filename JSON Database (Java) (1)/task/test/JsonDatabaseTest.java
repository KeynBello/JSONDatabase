import org.hyperskill.hstest.dynamic.DynamicTest;
import org.hyperskill.hstest.stage.StageTest;
import org.hyperskill.hstest.testcase.CheckResult;
import org.hyperskill.hstest.testing.TestedProgram;

public class JsonDatabaseTest extends StageTest<String> {

    private static final String CORRECT_SERVER_OUTPUT =
        "Server started!\n" +
            "Received: Give me a record # N\n" +
            "Sent: A record # N was sent!";

    private static final String CORRECT_CLIENT_OUTPUT =
        "Client started!\n" +
            "Sent: Give me a record # N\n" +
            "Received: A record # N was sent!";

    @DynamicTest(order = 1)
    CheckResult test() {

        TestedProgram server = new TestedProgram("server");
        server.startInBackground();

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String serverOutput = server.getOutput().trim();

        if (!serverOutput.trim().equals("Server started!")) {
            return CheckResult.wrong("Server output should be 'Server started!' until a client connects!");
        }

        TestedProgram client = new TestedProgram("client");

        String clientOutput = client.start();
        serverOutput += "\n" + server.getOutput();

        String[] serverOutputLines = serverOutput.split("\n");

        if (serverOutputLines.length != 3) {
            return CheckResult.wrong("After the client connects to the server, the server output should contain 3 lines!");
        }

        String serverOutputLastLine = serverOutputLines[serverOutputLines.length - 1];

        if (!serverOutputLastLine.contains("Sent: A record #") || !serverOutputLastLine.contains("was sent!")) {
            return CheckResult.wrong("Server output after client connects to the server should be:\n"
                + CORRECT_SERVER_OUTPUT + "\n\nWhere N is some number.\n\nYour output:\n" + serverOutput);
        }

        String[] clientOutputLines = clientOutput.split("\n");

        if (clientOutputLines.length != 3) {
            return CheckResult.wrong("After the client connects to the server, the client output should contain 3 lines!");
        }

        String clientOutputLastLine = clientOutputLines[clientOutputLines.length - 1];

        if (!clientOutputLastLine.contains("Received: A record #") || !clientOutputLastLine.contains("was sent!")) {
            return CheckResult.wrong("Client output after client connects to the server should be:\n"
                + CORRECT_CLIENT_OUTPUT + "\n\nWhere N is some number.\n\nYour output:\n" + clientOutput);
        }

        return CheckResult.correct();
    }
}
