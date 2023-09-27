package server;

import com.beust.jcommander.Parameter;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class JSONDatabase {
    private static final Path DATABASE_CLIENT_FILE_PATH = Path.of("client/data/db.json");

    private static final ReadWriteLock rwLock = new ReentrantReadWriteLock();
    private static final Lock readLock = rwLock.readLock();
    private static final Lock writeLock = rwLock.writeLock();
    private static final Map<String, String> data = new HashMap<>();
    @Parameter(names = {"--value", "-v"}, description = "value")
    private static String value;
    GoodResponse goodResponse = new GoodResponse();
    BadResponse badResponse = new BadResponse();

    public String set(String index, String text) {
        try {
            if (text != null && index != null) {
                data.put(index, text);
                return goodResponse.getOkResponse();
            } else {
                throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException e) {
            return badResponse.getErrorResponse();
        }
    }

    public String get(String index) {
        try {
            if (data.get(index) != null) {
                value = data.get(index);
                JsonObject response = JsonParser.parseString(goodResponse.getOkResponse()).getAsJsonObject();
                response.addProperty("value", value);
                String jsonStringValue = response.toString();
                return jsonStringValue;
            } else {
                return badResponse.getNotSuchKey();
            }
        } catch (IllegalArgumentException e) {
            return badResponse.getErrorResponse();
        }
    }

    public String delete(String index) {
        try {
            if (data.get(index) != null) {
                data.remove(index);
                return goodResponse.getOkResponse();
            } else {
                return badResponse.getNotSuchKey();
            }
        } catch (IllegalArgumentException e) {
            return badResponse.getErrorResponse();
        }
    }

    public String loadDatabaseFromFile(String path) {
        String line = null;
        try {
            readLock.lock();
            String absolutePath = "/Users/andrii/Workspace InteliJ IDEA/RepoSoftserve/JSONDatabase/JSON Database (Java) (1)/task/src/client/data/" + path;
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(absolutePath))) {
                StringBuilder sb = new StringBuilder();
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }
                return sb.toString();
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        } finally {
            readLock.unlock();
        }
    }

    public void saveDatabaseToFile() {
        try {
            writeLock.lock();
            BufferedWriter bufferedClientWriter = Files.newBufferedWriter(DATABASE_CLIENT_FILE_PATH);

            for (Map.Entry<String, String> entry : data.entrySet()) {
                bufferedClientWriter.write(entry.getKey() + ":" + entry.getValue());
                bufferedClientWriter.newLine();
            }
            bufferedClientWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            writeLock.unlock();
        }
    }
}