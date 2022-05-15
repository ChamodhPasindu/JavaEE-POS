package util;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import java.io.PrintWriter;

public class Builder {

    public static void getResponseBuilder(int status, String message, JsonArrayBuilder arrayBuilder, PrintWriter writer) {
        JsonObjectBuilder response = Json.createObjectBuilder();

        if (arrayBuilder == null) {
            response.add("data", "");
        } else {
            response.add("data", arrayBuilder.build());
        }
        response.add("status", status);
        response.add("message", message);
        writer.print(response.build());
    }
}
