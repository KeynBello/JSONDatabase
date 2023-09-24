package server;

import com.beust.jcommander.Parameter;
import com.google.gson.JsonObject;

public class GoodResponse {
    @Parameter(names = {"--OK", "-OK"}, description = "response")
    private static final String OK_RESPONSE = "OK";

    public String getOkResponse() {
        JsonObject responseOk = new JsonObject();
        responseOk.addProperty("response", OK_RESPONSE);
        String jsonResponseOk = responseOk.toString();
        return jsonResponseOk;
    }
}
