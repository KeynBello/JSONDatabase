package server;

import com.beust.jcommander.Parameter;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.HashMap;
import java.util.Map;

public class JSONDatabase {
    @Parameter(names = {"--value", "-v"}, description = "value")
    private static String value;
    private static Map<String,String> data = new HashMap<>();
    GoodResponse goodResponse = new GoodResponse();
    BadResponse badResponse = new BadResponse();

    public String set(String index, String text) {
        try {
            if (text != null) {
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
                return  jsonStringValue;
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
}