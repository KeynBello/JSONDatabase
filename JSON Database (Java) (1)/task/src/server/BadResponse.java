package server;

import com.beust.jcommander.Parameter;
import com.google.gson.JsonObject;

public class BadResponse {
    @Parameter(names = {"--ERROR", "-ERROR"}, description = "response")
    private static final String ERROR_RESPONSE = "ERROR";

    @Parameter(names = {"--N/A", "-N/A"}, description = "reason")
    private static final String NA_RESPONSE = "No such key";

    public String getErrorResponse() {
        JsonObject responseError = new JsonObject();
        responseError.addProperty("response", ERROR_RESPONSE);
        String jsonResponseError = responseError.toString();
        return jsonResponseError;
    }
    public String getNotSuchKey() {
        JsonObject responseNa = new JsonObject();
        responseNa.addProperty("response", ERROR_RESPONSE);
        responseNa.addProperty("reason", NA_RESPONSE);
        String jsonResponseNa = responseNa.toString();
        return jsonResponseNa;
    }
}
